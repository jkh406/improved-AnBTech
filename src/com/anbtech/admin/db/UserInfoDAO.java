package com.anbtech.admin.db;

import com.anbtech.admin.entity.UserInfoTable;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class UserInfoDAO{
	private Connection con;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public UserInfoDAO(Connection con){
		this.con = con;
	}

	/*******************************************
	 * �μ��� ����� ����Ʈ�� �����´�.(�μ��ڵ尪�� ������)
	 * ���������� ���������� �ڵ尪�� ������ Ʋ����� �� �޼���� ����� ������.
	 * ��, ���ߺμ� �ڵ尡 C03 �ϰ�� ����1���� �ڵ�� C03-01 ������ Ȱ��Ǿ��
	 * �����μ��� ������ �� ����. �̷� ������ ������ ���� �Ʒ� getUserListByInnerCode
	 * �޼�����.
	 *
	 * �Է� �Ķ����
	 * ac_code : �μ��ڵ尪(class_table �� ac_code �ʵ尪)
	 *******************************************/
	public ArrayList getUserListByDivision(String ac_code) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList user_list = new ArrayList();
		com.anbtech.admin.entity.UserInfoTable table = new com.anbtech.admin.entity.UserInfoTable();
		String query = "";

		//
		// user_table:a, rank_table:b,class_table:c
		//
		query = "select a.id,a.name,a.passwd,a.email,a.enter_day,a.access_code,";
		query += "b.ar_name,c.ac_name from user_table a,rank_table b,class_table c ";
		query += "where a.rank = b.ar_code and a.ac_id = c.ac_id ";

		if(ac_code != null && !ac_code.equals(""))
			query += "and c.ac_code like '" + ac_code + "%'";

		stmt=con.createStatement();
		rs=stmt.executeQuery(query);

		while(rs.next()) {
			table = new com.anbtech.admin.entity.UserInfoTable();

			table.setUserId(rs.getString("id"));
			table.setUserName(rs.getString("name"));
			table.setPasswd(rs.getString("passwd"));
			table.setEmail(rs.getString("email"));
			table.setEnterDay(rs.getString("enter_day"));
			table.setAccessCode(rs.getString("access_code"));
			table.setUserRank(rs.getString("ar_name"));
			table.setDivision(rs.getString("ac_name"));
			user_list.add(table);
		}

		stmt.close();
		rs.close();

		return user_list;
	}

	/*******************************************
	 * �μ��� ����� ����Ʈ�� �����´�.(�����ڵ尪�� ������)
	 *
	 * �Է� �Ķ����
	 * code : �����ڵ尪(class_table �� code �ʵ尪)
	 *******************************************/
	public ArrayList getUserListByInnerCode(String code) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList user_list = new ArrayList();
		com.anbtech.admin.entity.UserInfoTable table = new com.anbtech.admin.entity.UserInfoTable();
		String query = "";

		//
		// user_table:a, rank_table:b,class_table:c
		//
		query = "select a.id,a.name,a.passwd,a.email,a.enter_day,a.access_code,";
		query += "b.ar_name,b.ar_priorty,c.ac_name from user_table a,rank_table b,class_table c ";
		query += "where a.rank = b.ar_code and a.ac_id = c.ac_id ";

		if(code != null && !code.equals("")) query += "and c.code like '" + code + "%'";
		query += " order by c.ac_name ASC, b.ar_priorty ASC";

		stmt=con.createStatement();
		rs=stmt.executeQuery(query);

		while(rs.next()) {
			table = new com.anbtech.admin.entity.UserInfoTable();

			table.setUserId(rs.getString("id"));
			table.setUserName(rs.getString("name"));
			table.setPasswd(rs.getString("passwd"));
			table.setEmail(rs.getString("email"));
			table.setEnterDay(rs.getString("enter_day"));
			table.setAccessCode(rs.getString("access_code"));
			table.setUserRank(rs.getString("ar_name"));
			table.setDivision(rs.getString("ac_name"));
			user_list.add(table);

		}
		
		stmt.close();
		rs.close();

		return user_list;
	}

	/*******************************************
	 * ID�� �ش��ϴ� ����� ������ �����´�.
	 *******************************************/
	public UserInfoTable getUserListById(String id) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.admin.entity.UserInfoTable table = new com.anbtech.admin.entity.UserInfoTable();
		String query = "";

		//
		// user_table:a, rank_table:b,class_table:c
		//
		query = "select a.id,a.name,a.passwd,a.email,a.enter_day,a.access_code,c.ac_code,b.ar_code,c.ac_id,a.code,";
		query += "b.ar_name,c.ac_name from user_table a,rank_table b,class_table c ";
		query += "where a.rank = b.ar_code and a.ac_id = c.ac_id ";
		query += "and a.id = '" + id + "'";

		stmt=con.createStatement();
		rs=stmt.executeQuery(query);

		if(rs.next()) {
			table.setUserId(rs.getString("id"));
			table.setUserName(rs.getString("name"));
			table.setPasswd(rs.getString("passwd"));
			table.setEmail(rs.getString("email"));
			table.setEnterDay(rs.getString("enter_day"));
			table.setAccessCode(rs.getString("access_code"));
			table.setUserRank(rs.getString("ar_name"));	
			table.setArCode(rs.getString("ar_code"));
			table.setDivision(rs.getString("ac_name"));
			table.setAcId(rs.getString("ac_id"));
			table.setAcCode(rs.getString("ac_code"));
			table.setCode(rs.getString("code"));
		}

		stmt.close();
		rs.close();

		return table;
	}

	/***********************************
	 * ����� ���� �ľ��Ͽ� ����
	 ***********************************/
	public int getDivisionCount() throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select count(*) from user_table";

		stmt=con.createStatement();
		rs=stmt.executeQuery(sql);

		rs.next();
		int total_count = Integer.parseInt(rs.getString(1));
		stmt.close();
		rs.close();
		return total_count;
	} // getDivisionCount()

	/***********************************
	 * �μ��ڵ�� ���ΰ����ڵ� ���ϱ�
	 ***********************************/
	public String getCode(String ac_id) throws Exception {
		String code = "";	//�μ� ���ΰ����ڵ�
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select code from user_table where ac_id = '"+ac_id+"'";

		stmt=con.createStatement();
		rs=stmt.executeQuery(sql);

		if(rs.next()) code = rs.getString("code");
		
		stmt.close();
		rs.close();
		return code;
	} // getCode()
}		