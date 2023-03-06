package com.anbtech.mr.db;
import com.anbtech.mr.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class asuserinfoDAO
{
	private Connection con;
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//날자처리
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//문자열 처리

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public asuserinfoDAO(Connection con) 
	{
		this.con = con;
	}

	public asuserinfoDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	// 사용자 정보 지정내용상세보기
	//*******************************************************************/	
	public void getCompanyNo(String login_id) throws Exception
	{
/*		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM as_result where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new assupportTable();
								
				table.setPid(rs.getString("pid"));
				String register_no = rs.getString("register_no"); if(register_no == null) register_no = "";
				table.setRegisterNo(register_no);	
				table.setRegisterDate(rs.getString("register_date"));
				table.setAsField(rs.getString("as_field"));	
				table.setCode(rs.getString("code"));
				table.setRequestName(rs.getString("request_name"));
				table.setSerialNo(rs.getString("serial_no"));
				table.setRequestDate(rs.getString("request_date"));
				table.setAsDate(rs.getString("as_date"));
				table.setAsType(rs.getString("as_type"));
				table.setAsContent(rs.getString("as_content"));
				table.setAsResult(rs.getString("as_result"));
				table.setAsDelay(rs.getString("as_delay"));
				table.setAsIssue(rs.getString("as_issue"));
				table.setWorker(rs.getString("worker"));
				table.setCompanyNo(rs.getString("company_no"));
				table.setCompanyNo(rs.getString("value_request"));
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
*/
	}

	/***************************************************************************
	 * ID을 구하는 메소드
	 **************************************************************************/
	public String getID()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}
}




