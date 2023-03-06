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
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
	private String query = "";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BmToMmBO(Connection con) 
	{
		this.con = con;
		btmDAO = new com.anbtech.bm.db.BmToMmDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		BOM [설계변경] 정보를 생산관리모듈로 인터페이스 
	//		[MBOM_STR ---> MBOM_ITEM]	
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * 초기BOM승인시 MBOM_STR에서 MBOM_ITEM으로 정보를 보내기
	 **********************************************************************/
	public String tranceBOMList(String gid) throws Exception
	{
		String input="",data="",delete="";
		double loss_rate = 1.0;
		ArrayList trans_list = new ArrayList();
		String trans_date = anbdt.getDateNoformat();	//I/F일자 
		com.anbtech.bm.entity.mbomStrTable mst = new com.anbtech.bm.entity.mbomStrTable();

		//임시BOM구성시 생산모듈로 데이터 전송됨에 따라 이미 전송된 데이터가 있으면 
		//현재구성된 mbom_item의 데이터를 삭제함. 따라서 일단 해당GID를 삭제후 I/F함.
		delete = "DELETE FROM mbom_item WHERE gid='"+gid+"'";
		btmDAO.executeUpdate(delete);

		//MBOM_STR데이터 읽기
		trans_list = btmDAO.getBomList(gid);

		//MBOM_STR을 MBOM_ITEM으로 옮기기
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
	 * 설계변경 승인-확정후 MBOM_STR에서 MBOM_ITEM으로 정보를 보내기
	 **********************************************************************/
	public String tranceChangeBOMList(String eco_no) throws Exception
	{
		String input="",update="",data="";
		double loss_rate = 1.0;
		ArrayList trans_list = new ArrayList();
		String trans_date = anbdt.getDateNoformat();	//I/F일자 
		com.anbtech.bm.entity.mbomStrTable mst = new com.anbtech.bm.entity.mbomStrTable();

		//MBOM_STR데이터 읽기
		trans_list = btmDAO.getChangeBomList(eco_no);

		//적용일자 구하기
		//adtag='D','RB'의 bom_end_date가 됨.
		String where = "where eco_no='"+eco_no+"'";
		String order_date = btmDAO.getColumData("ecc_com","order_date",where);

		//MBOM_STR을 MBOM_ITEM으로 옮기기
		Iterator trans_iter = trans_list.iterator();
		while(trans_iter.hasNext()) {
			mst = (mbomStrTable)trans_iter.next();

			String adtag = mst.getAdTag();
			//추가[A]및 변경후 부품[RA]인 경우
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
			//삭제[D]및 변경전 부품[RB]인 경우
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
