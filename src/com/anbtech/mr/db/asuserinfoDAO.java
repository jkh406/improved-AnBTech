package com.anbtech.mr.db;
import com.anbtech.mr.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class asuserinfoDAO
{
	private Connection con;
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����ó��
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	
	//*******************************************************************
	//	������ �����
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
	// ����� ���� ��������󼼺���
	//*******************************************************************/	
	public void getCompanyNo(String login_id) throws Exception
	{
/*		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM as_result where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
*/
	}

	/***************************************************************************
	 * ID�� ���ϴ� �޼ҵ�
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




