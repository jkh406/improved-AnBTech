package com.anbtech.bm.db;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class BmToMmDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	private String query = "";
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BmToMmDAO(Connection con) 
	{
		this.con = con;
	}
	//--------------------------------------------------------------------
	//
	//		BOM [설계변경] 정보를 생산관리모듈로 인터페이스 
	//		[MBOM_STR ---> MBOM_ITEM]	
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// 최초BOM 결재승인후 MBOM리스트 얻기
	//*******************************************************************/	
	public ArrayList getBomList(String gid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable mst = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM MBOM_STR where gid='"+gid+"' order by level_no asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
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
				mst.setAssyDup(rs.getString("assy_dup"));
				mst.setItemType(rs.getString("item_type"));
				////System.out.println(rs.getString("item_type")+":"+rs.getString("parent_code")+":"+rs.getString("child_code"));
				table_list.add(mst); 
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 설계변경 확정후 MBOM리스트 얻기 : 결재승인-->확정 단계로 구성됨
	//*******************************************************************/	
	public ArrayList getChangeBomList(String eco_no) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable mst = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM ECC_BOM where eco_no='"+eco_no+"' order by level_no asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
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
				mst.setAssyDup(rs.getString("assy_dup"));
				mst.setItemType(rs.getString("item_type"));
				////System.out.println(rs.getString("level_no")+":"+rs.getString("parent_code")+":"+rs.getString("child_code"));
				table_list.add(mst); 
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		공통 메소드 정의
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// SQL update 실행하기
	//*******************************************************************/	
	public void executeUpdate(String update) throws Exception
	{
		Statement stmt = con.createStatement();
		stmt.executeUpdate(update);
		stmt.close();
	}
	//*******************************************************************
	//	주어진 테이블에서 주어진 조건의 컬럼의 데이터 읽기
	//*******************************************************************/
	public String getColumData(String tablename,String getcolumn,String where) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString(getcolumn);
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
}
