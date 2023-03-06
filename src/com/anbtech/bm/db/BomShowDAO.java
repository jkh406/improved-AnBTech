package com.anbtech.bm.db;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class BomShowDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
	private String query = "";
	private ArrayList item_list = null;				//PART정보를 ArrayList에 담기
	private mbomStrTable mst = null;				//help class
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomShowDAO(Connection con) 
	{
		this.con = con;
	}
	//--------------------------------------------------------------------
	//
	//		BOM STRUCTURE 검색 에 관한 메소드 정의
	//			
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_MASTER 에서 BOM FG LIST
	//*******************************************************************/	
	public ArrayList getBomFGList (String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where bom_status = '5' and ";
		query += sItem+" like '%"+sWord+"%' order by "+sItem+" asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();	
				String pid = rs.getString("pid");
				table.setPid(pid);	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));
				table.setFgCode(rs.getString("fg_code"));
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));	
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//확정후 BOM 구성하기
	//		BOM STRUCTURE 에 관한 메소드 정의
	//			1. 정전개 [단단계]
	//			2. 정전개 [다단계]
	//			3. 역전개
	//---------------------------------------------------------------------
	/**********************************************************************
	 * 정전개 다단계 Item 출력
	 * 확정된 BOM TREE 출력하기
	 * 조건 : 유효시작일,유효종료일,BOM확정상태
	 *********************************************************************/
	public void saveForwardBomItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' and ";
		query += "(((bom_start_date <='"+sel_date+"' and app_status='1') and bom_end_date = '0') or "; 
		query += "((bom_start_date <='"+sel_date+"' and app_status='1') and bom_end_date > '"+sel_date+"'))";
		query += " order by child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));	
			mst.setParentCode(rs.getString("parent_code"));	
			mst.setChildCode(rs.getString("child_code"));	
			mst.setLevelNo(rs.getString("level_no"));	
			mst.setPartName(rs.getString("part_name"));	
			mst.setPartSpec(rs.getString("part_spec"));	
			mst.setLocation(rs.getString("location"));	
			mst.setOpCode(rs.getString("op_code"));	
			mst.setQtyUnit(rs.getString("qty_unit"));	
			mst.setQty(rs.getString("qty"));	
			mst.setMakerName(rs.getString("maker_name"));	
			mst.setMakerCode(rs.getString("maker_code"));	
			mst.setPriceUnit(rs.getString("price_unit"));	
			mst.setPrice(rs.getString("price"));
			mst.setAddDate(rs.getString("add_date"));	
			mst.setBuyType(rs.getString("buy_type"));	
			mst.setEcoNo(rs.getString("eco_no"));	
			mst.setAdTag(rs.getString("adtag"));	
			mst.setBomStartDate(rs.getString("bom_start_date"));	
			mst.setBomEndDate(rs.getString("bom_end_date"));
			mst.setAppStatus(rs.getString("app_status"));
			mst.setTag(rs.getString("tag"));
			String assy_dup = rs.getString("assy_dup");
			mst.setAssyDup(assy_dup);
			item_list.add(mst); 
			
			////System.out.println(rs.getString("level_no")+":"+rs.getString("parent_code")+":"+rs.getString("child_code"));
			lno = Integer.toString(Integer.parseInt(rs.getString("level_no"))+1);
			if(!assy_dup.equals("D")) saveForwardBomItems(gid,lno,rs.getString("child_code"),sel_date);
		}
		rs.close();
		stmt.close();
	} 
	/**********************************************************************
	 * 정전개 다단계 TREE구성 출력
	 * MBOM정보를 담은 item배열을 리턴한다.
	 * MBOM 정전개 TREE구하여 배열에 담아 리턴하기 : 하부구조 전체
	 *********************************************************************/
	public ArrayList getForwardBomItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		item_list = new ArrayList();
		saveForwardBomItems(gid,level_no,parent_code,sel_date);	

/*		//출력해보기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			//System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo()+":"+table.getLocation());
		}
*/
		return item_list;
	}
	/**********************************************************************
	 * 정전개 단단계 Item 출력
	 * 확정된 BOM TREE 출력하기
	 * 조건 : 유효시작일,유효종료일,BOM확정상태
	 *********************************************************************/
	public void saveForwardSingleBomItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' and ";
		query += "(((bom_start_date <='"+sel_date+"' and app_status='1') and bom_end_date = '0') or "; 
		query += "((bom_start_date <='"+sel_date+"' and app_status='1') and bom_end_date > '"+sel_date+"'))";
		query += " order by child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));	
			mst.setParentCode(rs.getString("parent_code"));	
			mst.setChildCode(rs.getString("child_code"));	
			mst.setLevelNo(rs.getString("level_no"));	
			mst.setPartName(rs.getString("part_name"));	
			mst.setPartSpec(rs.getString("part_spec"));	
			mst.setLocation(rs.getString("location"));	
			mst.setOpCode(rs.getString("op_code"));	
			mst.setQtyUnit(rs.getString("qty_unit"));	
			mst.setQty(rs.getString("qty"));	
			mst.setMakerName(rs.getString("maker_name"));	
			mst.setMakerCode(rs.getString("maker_code"));	
			mst.setPriceUnit(rs.getString("price_unit"));	
			mst.setPrice(rs.getString("price"));
			mst.setAddDate(rs.getString("add_date"));	
			mst.setBuyType(rs.getString("buy_type"));	
			mst.setEcoNo(rs.getString("eco_no"));	
			mst.setAdTag(rs.getString("adtag"));	
			mst.setBomStartDate(rs.getString("bom_start_date"));	
			mst.setBomEndDate(rs.getString("bom_end_date"));
			mst.setAppStatus(rs.getString("app_status"));
			mst.setTag(rs.getString("tag"));
			String assy_dup = rs.getString("assy_dup");
			mst.setAssyDup(assy_dup);
			item_list.add(mst); 
		}
		rs.close();
		stmt.close();
	} 
	/**********************************************************************
	 * 정전개 단단계 TREE구성 출력
	 * MBOM정보를 담은 item배열을 리턴한다.
	 * MBOM 정전개 TREE구하여 배열에 담아 리턴하기 : 하부구조 전체
	 *********************************************************************/
	public ArrayList getForwardSingleBomItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		item_list = new ArrayList();
		saveForwardSingleBomItems(gid,level_no,parent_code,sel_date);	
		return item_list;
	}

	/**********************************************************************
	 * MBOM 정보를 배열에 담는다. 
	 * 확정된 BOM TREE 출력하기
	 * 조건 : 유효시작일,유효종료일,BOM확정상태
	 * MBOM 역전개 TREE구하여 ArrayList에 담기
	 *********************************************************************/
	public void saveReverseBomItems(String child_code,String sel_date) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT distinct parent_code,child_code,level_no,part_name,part_spec,op_code from MBOM_STR ";
		query += "where child_code = '"+child_code+"' and ";
		query += "(((bom_start_date <='"+sel_date+"' and app_status='1') and bom_end_date = '0') or "; 
		query += "((bom_start_date <='"+sel_date+"' and app_status='1') and bom_end_date > '"+sel_date+"'))";
		query += " order by child_code asc";

		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setParentCode(rs.getString("parent_code"));
			mst.setChildCode(rs.getString("child_code"));
			mst.setLevelNo(rs.getString("level_no"));
			mst.setPartName(rs.getString("part_name"));
			mst.setPartSpec(rs.getString("part_spec"));
			mst.setOpCode(rs.getString("op_code"));
			item_list.add(mst); 
			////System.out.println(rs.getString("level_no")+":"+rs.getString("parent_code")+":"+rs.getString("child_code")+":"+rs.getString("part_name")+":"+rs.getString("part_spec")+":"+rs.getString("op_code"));
			saveReverseBomItems(rs.getString("parent_code"),sel_date);
		}
		rs.close();
		stmt.close(); 
		
	} //saveReverseItems

	/**********************************************************************
	 * MBOM정보를 담은 item배열을 리턴한다.
	 * MBOM 역전개 TREE구하여 배열에 담아 리턴하기
	 *********************************************************************/
	public ArrayList getReverseBomItems(String child_code,String sel_date) throws Exception
	{
		item_list = new ArrayList();
		saveReverseBomItems(child_code,sel_date);

/*		//출력해보기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			//System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo());
		}
*/		
		return item_list;
	}

	/**********************************************************************
	 * MBOM정보를 담은 item배열을 리턴한다. : 입력값을 배열로 받기
	 * MBOM 역전개 TREE구하여 배열에 담아 리턴하기
	 *********************************************************************/
	public ArrayList getReverseMultiBomItems(String[] child_code,String sel_date) throws Exception
	{
		int a_cnt = child_code.length;
		item_list = new ArrayList();
		for(int i=0; i<a_cnt; i++) {
			saveReverseBomItems(child_code[i],sel_date);
		}

/*		//출력해보기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			//System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo());
		}
*/		
		return item_list;
	}

	//----------------------------------------------------------------------
	//
	//			BOM원가 산출을 위해 필요한 메소드 정의
	//
	//
	//----------------------------------------------------------------------
	//*******************************************************************
	//	주어진 테이블에서 주어진 조건의 금액단위로 읽기
	//*******************************************************************/
	public String getColumCostData(String tablename,String getcolumn,String where) throws Exception
	{
		//변수 초기화
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("0.0");	//포멧
		String data = "0";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = fmt.DoubleToString(rs.getDouble(getcolumn));
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	// MBOM_MASTER 에서 모든 FG LIST
	//*******************************************************************/	
	public ArrayList getFGList (String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where ";
		query += sItem+" like '%"+sWord+"%' order by "+sItem+" asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();	
				String pid = rs.getString("pid");
				table.setPid(pid);	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));
				table.setFgCode(rs.getString("fg_code"));
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));	
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

}
