package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BmToMmBO
{
	private Connection con;
	private com.anbtech.bm.db.BmToMmDAO btmDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	
	private String query = "";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BmToMmBO(Connection con) 
	{
		this.con = con;
		btmDAO = new com.anbtech.bm.db.BmToMmDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		BOM [���躯��] ������ ����������� �������̽� 
	//		[MBOM_STR ---> MBOM_ITEM]	
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * �ʱ�BOM���ν� MBOM_STR���� MBOM_ITEM���� ������ ������
	 **********************************************************************/
	public String tranceBOMList(String gid) throws Exception
	{
		String input="",data="",delete="";
		double loss_rate = 1.0;
		ArrayList trans_list = new ArrayList();
		String trans_date = anbdt.getDateNoformat();	//I/F���� 
		com.anbtech.bm.entity.mbomStrTable mst = new com.anbtech.bm.entity.mbomStrTable();

		//�ӽ�BOM������ ������� ������ ���۵ʿ� ���� �̹� ���۵� �����Ͱ� ������ 
		//���籸���� mbom_item�� �����͸� ������. ���� �ϴ� �ش�GID�� ������ I/F��.
		delete = "DELETE FROM mbom_item WHERE gid='"+gid+"'";
		btmDAO.executeUpdate(delete);

		//MBOM_STR������ �б�
		trans_list = btmDAO.getBomList(gid);

		//MBOM_STR�� MBOM_ITEM���� �ű��
		Iterator trans_iter = trans_list.iterator();
		while(trans_iter.hasNext()) {
			mst = (mbomStrTable)trans_iter.next();

			input = "INSERT INTO mbom_item (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
			input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,";
			input += "item_type,loss_rate,buy_type,eco_no,adtag,bom_start_date,bom_end_date,assy_dup,trans_date) values('";
			input += mst.getPid()+"','"+mst.getGid()+"','"+mst.getParentCode()+"','"+mst.getChildCode()+"','";
			input += mst.getLevelNo()+"','"+mst.getPartName()+"','"+mst.getPartSpec()+"','"+mst.getLocation()+"','";
			input += mst.getOpCode()+"','"+mst.getQtyUnit()+"','"+mst.getQty()+"','"+mst.getMakerName()+"','";
			input += mst.getMakerCode()+"','"+mst.getPriceUnit()+"','"+mst.getPrice()+"','"+mst.getAddDate()+"','";
			input += mst.getItemType()+"','"+loss_rate+"','"+mst.getBuyType()+"','"+mst.getEcoNo()+"','"+mst.getAdTag()+"','";
			input += mst.getBomStartDate()+"','"+mst.getBomEndDate()+"','"+mst.getAssyDup()+"','"+trans_date+"')";
			btmDAO.executeUpdate(input);

		} //while
		data = "OK";

		return data;
	}

	/**********************************************************************
	 * ���躯�� ����-Ȯ���� MBOM_STR���� MBOM_ITEM���� ������ ������
	 **********************************************************************/
	public String tranceChangeBOMList(String eco_no) throws Exception
	{
		String input="",update="",data="";
		double loss_rate = 1.0;
		ArrayList trans_list = new ArrayList();
		String trans_date = anbdt.getDateNoformat();	//I/F���� 
		com.anbtech.bm.entity.mbomStrTable mst = new com.anbtech.bm.entity.mbomStrTable();

		//MBOM_STR������ �б�
		trans_list = btmDAO.getChangeBomList(eco_no);

		//�������� ���ϱ�
		//adtag='D','RB'�� bom_end_date�� ��.
		String where = "where eco_no='"+eco_no+"'";
		String order_date = btmDAO.getColumData("ecc_com","order_date",where);

		//MBOM_STR�� MBOM_ITEM���� �ű��
		Iterator trans_iter = trans_list.iterator();
		while(trans_iter.hasNext()) {
			mst = (mbomStrTable)trans_iter.next();

			String adtag = mst.getAdTag();
			//�߰�[A]�� ������ ��ǰ[RA]�� ���
			if(adtag.equals("A") || adtag.equals("RA")) {
				input = "INSERT INTO mbom_item (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
				input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,";
				input += "item_type,loss_rate,buy_type,eco_no,adtag,bom_start_date,bom_end_date,assy_dup,trans_date) values('";
				input += mst.getPid()+"','"+mst.getGid()+"','"+mst.getParentCode()+"','"+mst.getChildCode()+"','";
				input += mst.getLevelNo()+"','"+mst.getPartName()+"','"+mst.getPartSpec()+"','"+mst.getLocation()+"','";
				input += mst.getOpCode()+"','"+mst.getQtyUnit()+"','"+mst.getQty()+"','"+mst.getMakerName()+"','";
				input += mst.getMakerCode()+"','"+mst.getPriceUnit()+"','"+mst.getPrice()+"','"+mst.getAddDate()+"','";
				input += mst.getItemType()+"','"+loss_rate+"','"+mst.getBuyType()+"','"+mst.getEcoNo()+"','"+mst.getAdTag()+"','";
				input += mst.getBomStartDate()+"','"+mst.getBomEndDate()+"','"+mst.getAssyDup()+"','"+trans_date+"')";
				btmDAO.executeUpdate(input);
			}
			//����[D]�� ������ ��ǰ[RB]�� ���
			else if(adtag.equals("D") || adtag.equals("RB")){
				update = "UPDATE mbom_item SET eco_no='"+eco_no+"',adtag='"+adtag+"',";
				update += "bom_start_date='"+mst.getBomStartDate()+"',bom_end_date='"+order_date+"',";
				update += "trans_date='"+trans_date+"' where gid='"+mst.getGid()+"' and pid='"+mst.getPid()+"'";
				//System.out.println("update : " + update);
				btmDAO.executeUpdate(update);
			}

		} //while
		data = "OK";

		return data;
	}
}
