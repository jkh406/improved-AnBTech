package com.anbtech.es.geuntae.db;
import com.anbtech.date.anbDate;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.lang.Double;

public class scheduleHolidayDAO{
	private Connection con;
	private com.anbtech.date.anbDate anbdt;
	private com.anbtech.dbconn.DBConnectionManager connMgr;	

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public scheduleHolidayDAO() 
	{	
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
		anbdt = new com.anbtech.date.anbDate();
	}	

	/*******************************************************************
	 * �������� �ش�Tag�� �ϼ� �ľ��ϱ�
	 *******************************************************************/
	public int getHdCount(String item) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		int total_count = 0;

		String query = "SELECT COUNT(*) FROM calendar_common where item='"+item+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next())
			total_count = Integer.parseInt(rs.getString(1));
		stmt.close();
		rs.close();
		return total_count;
	}

	/*******************************************************************
	 * ������������ Ķ�������� �ų� ���� ���ڻ��� ������ ã��
	 *******************************************************************/
	public String[][] getHdAtEveryYear() throws Exception
	{
		int lhd_cnt = getHdCount("LHD");			//�ų� ���������� ��������
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = new String[lhd_cnt][2];		//��,��

		String query = "SELECT hdmon,nlist FROM calendar_common where item='LHD'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		int i = 0;
		while(rs.next()) {
			data[i][0] = rs.getString("hdmon");
			String nlist = rs.getString("nlist");
			data[i][1] = nlist.substring(0,2);
			i++;
		}
		stmt.close();
		rs.close();
//		close();

		return data;
	}

	/*******************************************************************
	 * ������������ Ķ�������� �ش�⵵�� ������ ã��
	 *******************************************************************/
	public String[][] getHdAtSameYear() throws Exception
	{
		int bhd_cnt = getHdCount("BHD");			//�ų� ���������� ��������
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = new String[bhd_cnt][3];		//��,��,��

		String query = "SELECT hdyear,hdmon,nlist FROM calendar_common where item='BHD'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		int i = 0;
		while(rs.next()) {
			data[i][0] = rs.getString("hdyear");
			data[i][1] = rs.getString("hdmon");
			String nlist = rs.getString("nlist");
			data[i][2] = nlist.substring(0,2);
			i++;
		}
		stmt.close();
		rs.close();
//		close();

		return data;
	}

	/*********************************************************************
	 * �Ҹ���
	 *********************************************************************/
	public void close() throws Exception{
		connMgr.freeConnection("mssql",con);
	}
}