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
	//	������ �����
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
	// mgr�� �־��� ������ �ִ��� �Ǵ��Ͽ� boolean���� �����Ѵ�.
	// true : ���� ����,  false : ���Ѿ���
	//*******************************************************************/	
	public boolean getUseMgr(String login_id,String mgr) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		int cnt = 0;
		boolean rtn = false;

		stmt = con.createStatement();
		
		//query���� �����
		query = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where owner like '%"+login_id+"%' and keyname like '%"+mgr+"%'";		
		rs = stmt.executeQuery(query);

		//������ �б�
		if(rs.next()) cnt = rs.getInt(1);

		//�Ǵ��ϱ�
		if(cnt != 0) rtn = true;

		//mgr���� �������� ������ true�� ������ �������� �����Ѵ�. �� �����Ѵ�.
		if(mgr == null) mgr = "";
		if(mgr.length() == 0) rtn = true;

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return rtn;
	}
}

