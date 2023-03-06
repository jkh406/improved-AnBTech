package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class productProcessBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.mm.db.mpsModifyDAO mpsDAO = null;			
	private String query = "";
	private String where = "";
	private String factory_no="";
	private String mps_no="";
	private String mrp_no="";
	private String pu_req_no="";
	private String mfg_no="";
	private String fg_code="";
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public productProcessBO(Connection con) 
	{
		this.con = con;
		mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		�� �ܰ躰 ������� �ľ��ϱ�
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  �� �ܰ躰 �ش�Ǵ� ������ȣ ã��
	//*******************************************************************/	
	public void getLinkNo(String mps_code,String factory_code) throws Exception
	{
		if(mps_code.length() == 0) return;

		factory_no = factory_code;			//�����ȣ
		mps_no = mps_code;					//�����ȹ(MPS)��ȣ

		//�����ȹ��ǰ ���ϱ�
		where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		fg_code = mpsDAO.getColumData("MRP_MASTER","fg_code",where);

		//MRP NO ���ϱ�
		where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		mrp_no = mpsDAO.getColumData("MRP_MASTER","mrp_no",where);

		//��ǰ�����Ƿڹ�ȣ ���ϱ�
		where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		pu_req_no = mpsDAO.getColumData("MRP_MASTER","pu_req_no",where);

		//MFG NO ���ϱ�
		where = "where mrp_no='"+mrp_no+"' and order_type='MRP' and factory_no='"+factory_no+"'";
		mfg_no = mpsDAO.getColumData("MFG_MASTER","mfg_no",where);

		//��°˻�
		//System.out.println("factory_no : " + factory_no);
		//System.out.println("mps_no : " + mps_no);
		//System.out.println("mrp_no : " + mrp_no);
		//System.out.println("pu_req_no : " + pu_req_no);
		//System.out.println("mfg_no : " + mfg_no);
	}

	//*******************************************************************
	//  �����ȹ(MPS) ������� ���ϱ�
	//*******************************************************************/	
	public String getMpsStatus() throws Exception
	{
		String data="";

		where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		data = mpsDAO.getColumData("MPS_MASTER","mps_status",where);
		return data;
	}
	//*******************************************************************
	//  ��ǰ�ҿ䷮����(MRP) ������� ���ϱ�
	//*******************************************************************/	
	public String getMrpStatus() throws Exception
	{
		String data="";

		where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
		data = mpsDAO.getColumData("MRP_MASTER","mrp_status",where);
		return data;
	}
	//*******************************************************************
	//  �������� ������� ���ϱ�
	//*******************************************************************/	
	public String getMfgStatus() throws Exception
	{
		String data="";

		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		data = mpsDAO.getColumData("MFG_MASTER","order_status",where);
		return data;
	}
	//*******************************************************************
	//  ���Ź��� ������� ���ϱ�
	//*******************************************************************/	
	public String getPurchaseStatus() throws Exception
	{
		String data="";
		
		com.anbtech.pu.db.PurchaseMgrDAO pmDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
		data = pmDAO.getMaxStatForEnterItemByRequestNo(pu_req_no);
		return data;
	}
	//*******************************************************************
	//  �������� ������� ���ϱ� 
	//*******************************************************************/	
	public String getProductStatus() throws Exception
	{
		String data="";

		where = "where mfg_no='"+mfg_no+"' and item_code='"+fg_code+"' and factory_no='"+factory_no+"'";
		data = mpsDAO.getColumData("MFG_PRODUCT_MASTER","output_status",where);
		return data;
	}
	//*******************************************************************
	//  ǰ���˻� ������� ���ϱ� 
	//*******************************************************************/	
	public String getQcStatus() throws Exception
	{
		String data="";

		where = "where LOT_NO='"+mfg_no+"' and FACTORY_CODE='"+factory_no+"'";
		data = mpsDAO.getColumData("QC_INSPECTION_MASTER","PROCESS_STAT",where);
		return data;
	}

}

