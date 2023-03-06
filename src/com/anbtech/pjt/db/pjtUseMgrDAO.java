package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;

public class pjtUseMgrDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtUseMgrDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtUseMgrDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	// mgr로 주어진 권한이 있는지 판단하여 boolean값을 리턴한다.
	// true : 권한 있음,  false : 권한없음
	//*******************************************************************/	
	public boolean getUseMgr(String login_id,String mgr) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		int cnt = 0;
		boolean rtn = false;

		stmt = con.createStatement();
		
		//query문장 만들기
		query = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where owner like '%"+login_id+"%' and keyname like '%"+mgr+"%'";		
		rs = stmt.executeQuery(query);

		//데이터 읽기
		if(rs.next()) cnt = rs.getInt(1);

		//판단하기
		if(cnt != 0) rtn = true;

		//mgr값이 없을때는 무조건 true로 권한이 있음으로 간주한다. 즉 무시한다.
		if(mgr == null) mgr = "";
		if(mgr.length() == 0) rtn = true;

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return rtn;
	}
}

