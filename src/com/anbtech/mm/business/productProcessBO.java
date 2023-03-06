package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class productProcessBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
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
	//	생성자 만들기
	//*******************************************************************/
	public productProcessBO(Connection con) 
	{
		this.con = con;
		mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		각 단계별 진행상태 파악하기
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  각 단계별 해당되는 관리번호 찾기
	//*******************************************************************/	
	public void getLinkNo(String mps_code,String factory_code) throws Exception
	{
		if(mps_code.length() == 0) return;

		factory_no = factory_code;			//공장번호
		mps_no = mps_code;					//생산계획(MPS)번호

		//생산계획제품 구하기
		where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		fg_code = mpsDAO.getColumData("MRP_MASTER","fg_code",where);

		//MRP NO 구하기
		where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		mrp_no = mpsDAO.getColumData("MRP_MASTER","mrp_no",where);

		//부품구매의뢰번호 구하기
		where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		pu_req_no = mpsDAO.getColumData("MRP_MASTER","pu_req_no",where);

		//MFG NO 구하기
		where = "where mrp_no='"+mrp_no+"' and order_type='MRP' and factory_no='"+factory_no+"'";
		mfg_no = mpsDAO.getColumData("MFG_MASTER","mfg_no",where);

		//출력검사
		//System.out.println("factory_no : " + factory_no);
		//System.out.println("mps_no : " + mps_no);
		//System.out.println("mrp_no : " + mrp_no);
		//System.out.println("pu_req_no : " + pu_req_no);
		//System.out.println("mfg_no : " + mfg_no);
	}

	//*******************************************************************
	//  생산계획(MPS) 진행상태 구하기
	//*******************************************************************/	
	public String getMpsStatus() throws Exception
	{
		String data="";

		where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		data = mpsDAO.getColumData("MPS_MASTER","mps_status",where);
		return data;
	}
	//*******************************************************************
	//  부품소요량산출(MRP) 진행상태 구하기
	//*******************************************************************/	
	public String getMrpStatus() throws Exception
	{
		String data="";

		where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
		data = mpsDAO.getColumData("MRP_MASTER","mrp_status",where);
		return data;
	}
	//*******************************************************************
	//  제조오더 진행상태 구하기
	//*******************************************************************/	
	public String getMfgStatus() throws Exception
	{
		String data="";

		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		data = mpsDAO.getColumData("MFG_MASTER","order_status",where);
		return data;
	}
	//*******************************************************************
	//  구매발주 진행상태 구하기
	//*******************************************************************/	
	public String getPurchaseStatus() throws Exception
	{
		String data="";
		
		com.anbtech.pu.db.PurchaseMgrDAO pmDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
		data = pmDAO.getMaxStatForEnterItemByRequestNo(pu_req_no);
		return data;
	}
	//*******************************************************************
	//  제조생산 진행상태 구하기 
	//*******************************************************************/	
	public String getProductStatus() throws Exception
	{
		String data="";

		where = "where mfg_no='"+mfg_no+"' and item_code='"+fg_code+"' and factory_no='"+factory_no+"'";
		data = mpsDAO.getColumData("MFG_PRODUCT_MASTER","output_status",where);
		return data;
	}
	//*******************************************************************
	//  품질검사 진행상태 구하기 
	//*******************************************************************/	
	public String getQcStatus() throws Exception
	{
		String data="";

		where = "where LOT_NO='"+mfg_no+"' and FACTORY_CODE='"+factory_no+"'";
		data = mpsDAO.getColumData("QC_INSPECTION_MASTER","PROCESS_STAT",where);
		return data;
	}

}

