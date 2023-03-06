package com.anbtech.dms.db;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.business.*;

import java.sql.*;
import java.util.*;

public class DmsEnvDAO{
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public DmsEnvDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	 * category_id 에 해당하는 category_data 테이블 내용을 가져온다.
	 *******************************************************************/
	public DmsEnvTable getDmsEnv(String category_id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.dms.entity.DmsEnvTable table = new com.anbtech.dms.entity.DmsEnvTable();
		String query = "SELECT * FROM category_data WHERE c_id = '"+category_id+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		table.setCategoryId(rs.getString("c_id"));
		table.setCategoryName(rs.getString("c_name"));
		table.setInitialChar(rs.getString("c_code"));
		table.setEnableRevision(rs.getString("enable_rev"));
		table.setEnableProject(rs.getString("enable_pjt"));
		table.setEnableModel(rs.getString("enable_model"));
		table.setEnableEco(rs.getString("enable_eco"));
		table.setEnableApproval(rs.getString("enable_app"));
		table.setSecurityLevel(rs.getString("security_level"));
		table.setSavePeriod(rs.getString("save_period"));
		table.setLoanPeriod(rs.getString("loan_period"));
		table.setTableName(rs.getString("tablename"));
		stmt.close();
		rs.close();
		return table;
	} //getDmsEnv()
}		