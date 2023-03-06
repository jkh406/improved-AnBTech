package com.anbtech.admin.db;

import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class SysConfigDAO{
	private Connection con;

	public SysConfigDAO(Connection con){
		this.con = con;
	}


	/*******************************************************************************
	 * 특정 구분의 코드의 코드명을 가져온다.
	 *******************************************************************************/
	public String getMinorCodeName(String type,String code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String code_name = "";

		String sql = "SELECT code_name FROM system_minor_code WHERE code = '" + code + "' and type = '"+type+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()){
			code_name = rs.getString("code_name");
		}
		rs.close();
		stmt.close();

		return code_name;
	}

}		