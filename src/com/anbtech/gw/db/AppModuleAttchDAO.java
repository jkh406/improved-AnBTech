package com.anbtech.gw.db;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.anbtech.dbconn.DBConnectionManager;		

public class AppModuleAttchDAO
{
	// Database Wrapper Class ����
	private DBConnectionManager connMgr;
	private Connection con;

	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public AppModuleAttchDAO(Connection con) 
	{	
		this.con = con;
	}

	/******************************************************************************
		������� ÷������ ���� �ľ��ϱ�
	******************************************************************************/
	public int searchAttchCntTD(String mid) throws SQLException 
	{
		String afile = "";	//÷������ �� (���� : | )
		int cnt = 0;		//÷������ ����

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//�ش繮����ȣ�� �ش��� �μ��ڵ� ���ϱ�
		String query = "select fname from techdoc_data where ancestor='"+mid+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) afile += rs.getString("fname");
		
		//÷������ ���� �ľ��ϱ�
		StringTokenizer adata = new StringTokenizer(afile,"|");	
		while(adata.hasMoreTokens()) {
			adata.nextToken();
			cnt++;
		} //while

		stmt.close();
		rs.close();
		return cnt;
	}

	/******************************************************************************
		���ο� ÷������ ���� �ľ��ϱ�
	******************************************************************************/
	public int searchAttchCntAKG(String mid) throws SQLException 
	{
		String afile = "";	//÷������ �� (���� : | )
		int cnt = 0;		//÷������ ����

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//�ش繮����ȣ�� �ش��� �μ��ڵ� ���ϱ�
		String query = "select fname from ca_master where tmp_approval_no='"+mid+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) afile += rs.getString("fname");
		
		//÷������ ���� �ľ��ϱ�
		StringTokenizer adata = new StringTokenizer(afile,"|");	
		while(adata.hasMoreTokens()) {
			adata.nextToken();
			cnt++;
		} //while

		stmt.close();
		rs.close();
		return cnt;
	}

	/******************************************************************************
		������ ÷������ ���� �ľ��ϱ�
	******************************************************************************/
	public int searchAttchCntSERVICE(String mid) throws SQLException 
	{
		String afile = "";	//÷������ �� (���� : ����)
		int cnt = 0;		//÷������ ����

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		StringTokenizer sid = new StringTokenizer(mid,",");
		while(sid.hasMoreTokens()) {
			String tid = sid.nextToken();

			//�ش繮����ȣ�� �ش��� �μ��ڵ� ���ϱ�
			String query = "select file_name from history_table where ah_id='"+tid+"'";
			rs = stmt.executeQuery(query);
			while(rs.next()) afile = rs.getString("file_name");
			
			//÷������ ���� �ľ��ϱ�
			StringTokenizer adata = new StringTokenizer(afile,"|");	
			while(adata.hasMoreTokens()) {
				String dd = adata.nextToken();
				if(!dd.equals("null"))	cnt++;
			} //while
		}

		stmt.close();
		rs.close();
		return cnt;
	}

}