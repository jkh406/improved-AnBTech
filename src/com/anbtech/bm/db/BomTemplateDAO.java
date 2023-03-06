package com.anbtech.bm.db;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class BomTemplateDAO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
	private String query="", update="";
	private ArrayList item_list = null;				//PART정보를 ArrayList에 담기
	private mbomStrTable mst = null;				//help class
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomTemplateDAO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		Template 정보
	//			
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_ENV 데이터 Flag별 LIST  
	// flag 1 : OP CODE
	// flag 2 : Template data
	// flag 3 : 설계변경 항목관리 데이터
	// flag 4 : 설계변경 항목별 분류코드 데이터
	// flag 5 : 설계변경 제품별 변경부위
	//*******************************************************************/	
	public ArrayList getBomEnvList(String flag) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomEnvTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM mbom_env WHERE flag = '"+flag+"' order by m_code asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomEnvTable();		
				table.setPid(rs.getString("pid"));
				table.setMCode(rs.getString("m_code"));
				table.setSpec(rs.getString("spec"));
				table.setTag(rs.getString("tag"));	
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	해당 Template의 공정코드 SPEC정보 구하기
	//*******************************************************************/
	public String getOpCodeSpec(String part_code) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		query = "SELECT spec FROM mbom_env WHERE m_code='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("spec");
			
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	// MBOM_STR에서 공정Template BOM 정보만 query
	//   공정템플릿 BOM을 공정ASSY코드를 생성,등록하기 위해
	//*******************************************************************/	
	public ArrayList getTempBomList(String gid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM mbom_str WHERE gid = '"+gid+"' and tag='1' order by child_code asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();		
				table.setPid(rs.getString("pid"));
				table.setGid(rs.getString("gid"));
				table.setParentCode(rs.getString("parent_code"));
				table.setChildCode(rs.getString("child_code"));
				table.setLevelNo(rs.getString("level_no"));	
				table.setPartSpec(rs.getString("part_spec"));	
				////System.out.println(rs.getString("level_no")+" : "+rs.getString("parent_code")+" : "+rs.getString("child_code"));
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		사용자 정보 구하기
	//						
	//---------------------------------------------------------------------

	//*******************************************************************
	//	사용자 '소속/직급/이름' 구하기
	//*******************************************************************/
	public String getRegInfo(String sabun) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select a.name,b.ac_name,c.ar_name from user_table a,class_table b,rank_table c ";
		query +="where (a.id ='"+sabun+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString("ac_name")+"/"+rs.getString("ar_name")+"/"+rs.getString("name");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//--------------------------------------------------------------------
	//
	//		item master 정보 구하기
	//						
	//---------------------------------------------------------------------

	//*******************************************************************
	//	ASSY코드에서 형상명만 가려내기 : 규격중 , 구분중 첫번째 필드
	//*******************************************************************/
	public String getStateName(String item_code) throws Exception
	{
		//변수 초기화
		String data = "",item_desc="";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "SELECT item_desc FROM item_master WHERE item_no='"+item_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			item_desc = rs.getString("item_desc");
		}
		
		//형상정보만 가려내기
		int comma = item_desc.indexOf(",");
		if(comma == -1) comma = item_desc.length();
		data = item_desc.substring(0,comma);			//형상명

		stmt.close();
		rs.close();
		return data;			
	}

}

