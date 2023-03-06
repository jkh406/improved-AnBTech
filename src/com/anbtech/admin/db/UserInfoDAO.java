package com.anbtech.admin.db;

import com.anbtech.admin.entity.UserInfoTable;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class UserInfoDAO{
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public UserInfoDAO(Connection con){
		this.con = con;
	}

	/*******************************************
	 * 부서별 사용자 리스트를 가져온다.(부서코드값을 가지고)
	 * 상위조직과 하위조직의 코드값의 패턴이 틀릴경우 이 메서드는 사용할 수없음.
	 * 즉, 개발부서 코드가 C03 일경우 개발1팀의 코드는 C03-01 식으로 활당되어야
	 * 하위부서를 쿼리할 수 있음. 이런 문제를 보완한 것이 아래 getUserListByInnerCode
	 * 메서드임.
	 *
	 * 입력 파라미터
	 * ac_code : 부서코드값(class_table 의 ac_code 필드값)
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
	 * 부서별 사용자 리스트를 가져온다.(내부코드값을 가지고)
	 *
	 * 입력 파라미터
	 * code : 내부코드값(class_table 의 code 필드값)
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
	 * ID에 해당하는 사용자 정보를 가져온다.
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
	 * 사용자 수를 파악하여 리턴
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
	 * 부서코드로 내부관리코드 구하기
	 ***********************************/
	public String getCode(String ac_id) throws Exception {
		String code = "";	//부서 내부관리코드
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