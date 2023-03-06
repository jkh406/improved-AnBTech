package com.anbtech.dms.db;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.anbtech.admin.SessionLib;
import com.anbtech.dms.entity.*;

public class AccessControlDAO{
	private Connection con;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public AccessControlDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	 * �ش� ����� �μ� �ڵ带 ���Ѵ�.
	 * ���⼭�� �μ��ڵ�� ���������� �����Ǿ����� �ڵ���.
	 *******************************************************************/
	public String getDivisionCode(String id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT a.code FROM class_table a, user_table b where a.ac_id = b.ac_id and b.id = '" + id +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String division_code = rs.getString("code");
		stmt.close();
		rs.close();

		return division_code;
	} //getDivisionCode()


	/*******************************************************************
	 * �ش� ����� �̸��� �����´�.
	 *******************************************************************/
	public String getUserName(String id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT name FROM user_table where id = '" + id +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String user_name = rs.getString("name");
		stmt.close();
		rs.close();

		return user_name;
	} //getUserName()


	/*******************************************************************
	 * ���� �α����� ������� ������ ���� �ڵ带 ���Ѵ�.
	 *******************************************************************/
	public String getAccessCode(String id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT access_code FROM user_table where id = '" + id +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String access_code = rs.getString("access_code");
		stmt.close();
		rs.close();

		return access_code;
	} // getAccessCode()

	/*******************************************************************
	 * �˻��� �����͹�ȣ�� ������ �´� ���ڵ� ������ �����´�.
	 * ancestor : ������ ��ȣ,���Ϲ����� ���ؼ��� ��� ���� �����͹�ȣ��
	 * version  : ���� �ڵ�
	 *******************************************************************/
	public AccessControlTable getDocInfo(String tablename,String data_no,String ver_code) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		AccessControlTable table = new AccessControlTable();

		String query = "SELECT * FROM " + tablename + " WHERE ancestor ='"+data_no+"' and ver_code = '"+ver_code+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		table.setTid(rs.getString("t_id"));
		table.setVerCode(rs.getString("ver_code"));
		table.setAncestor(rs.getString("ancestor"));
		table.setDocNo(rs.getString("doc_no"));
		table.setSavePeriod(rs.getString("save_period"));
		table.setSecurityLevel(rs.getString("security_level"));
		table.setDocType(rs.getString("doc_type"));
		table.setWriter(rs.getString("writer"));
		table.setWrittenDay(rs.getString("written_day"));
		table.setRegister(rs.getString("register"));
		table.setRegisterDay(rs.getString("register_day"));
		table.setCategoryId(rs.getString("category_id"));
		table.setStat(rs.getString("stat"));

		stmt.close();
		rs.close();
		return table;
	}

	/*******************************************************************
	 * ���� ���������� ������ ���� ���������� üũ�Ѵ�.
	 *******************************************************************/
	public boolean isLastVersion(String tablename,String data_id,String ver_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		boolean is_last_version = false;

		String query = "SELECT count(*) FROM " + tablename + " where ancestor = '" + data_id +"' and ver_code > '" + ver_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		
		if(rs.getString(1) == null || rs.getString(1).equals("0")) is_last_version =  true; // �ֽŹ����� ��� ��

		stmt.close();
		rs.close();

		return is_last_version;


	} //isLastVersion()

	/*******************************************************************
	 * ���� ������ ���� �ӽÿ����� ������ �ִ��� �ľ��Ѵ�.
	 *******************************************************************/
	public boolean isTmpAccessPrivilege(String doc_no,String version,String login_id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		//������ ������ ��¥ ��������
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String now_time = vans.format(now);
		boolean is_privilege = false;

		String query = "SELECT COUNT(*) FROM authority_doc_table WHERE doc_no = '" + doc_no +"' AND version = '" + version + "' AND user_id = '" + login_id + "' AND today <= '" + now_time + "' AND valid_date >= '" + now_time + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		
		if(rs.getInt(1) > 0) is_privilege =  true;

		stmt.close();
		rs.close();

		return is_privilege;
	} //isLastVersion()

	/*******************************************************************
	 * ���� ������ ���⿡ ���� �׼��� ������ �ִ��� �˻��Ѵ�.
	 *******************************************************************/
	public boolean isLoanAccessPrivilege(String doc_no,String version,String login_id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		//������ ������ ��¥ ��������
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String now_time = vans.format(now);
		boolean is_privilege = false;

		String query = "SELECT COUNT(*) FROM loan_list WHERE doc_no = '" + doc_no +"' AND ver_code = '" + version + "' AND requestor = '" + login_id + "' AND req_date <= '" + now_time + "' AND end_date >= '" + now_time + "' AND stat = '3'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		if(rs.getInt(1) > 0) is_privilege =  true;

		stmt.close();
		rs.close();

		return is_privilege;
	} //isLastVersion()

	/**********************************************************************
	 * ���� ���������� ������ ���� ���� ������ üũ�Ѵ�.
	 * ÷������ �ٿ�ε� ����, ���� �Ǵ� Revision �� �� �ִ����� ���ȴ�.
	 **********************************************************************/
	public boolean getMyPrivilege(String tablename,String mode,String id,String data_id,String ver_code) throws Exception{
		String login_id = id; //���� �α��� ������� ���

		//���� �α��� ������� ���� ��������
		String login_division = getDivisionCode(login_id);	// ����� �μ��ڵ�
		String login_access_code = getAccessCode(login_id);	// ����� �������ڵ�

		//��������� �������� ��������
		AccessControlTable table = new AccessControlTable();
		table = getDocInfo(tablename,data_id,ver_code);
		String doc_no			= table.getDocNo();
		String doc_writer		= table.getWriter();		// ���� �ۼ��� ���
		String doc_register		= table.getRegister();		// ���� ����� ���
		String security_level	= table.getSecurityLevel();	// ���� ���� ���
		String stat				= table.getStat();			// ���� ������ ���� ������ �����ڵ�		
		String doc_writer_division = getDivisionCode(doc_writer);	// ���� �ۼ����� �μ��ڵ�

		//���� ���ȵ�޿� �ش��ϴ� ������ ���� �ڵ� �ľ�
		//��)�ش� ������ 3�� ������ ��� �α��� ������� �������ڵ� �� 3��° ��Ʈ���� �����Ѵ�.
		String access_code = login_access_code.substring(Integer.parseInt(security_level)-1,Integer.parseInt(security_level));

		boolean is_commit = false;

		/**************************************************************
		 * ������ ��޺� ������ ���� ü��
		 *
		 * �ڵ� A : All Access
		 * �ڵ� C : ���� �����(�Ǵ� ����)���� �߻��� ������ ���ؼ��� ������ ����
		 *          ����δ� ����1�ܰ��� �����η� �����Ѵ�.
		 * �ڵ� D : ���� �μ����� �߻��� ������ ���ؼ��� ������ ����
		 *          �μ��� ����2�ܰ��� ������ �����Ѵ�.
		 * �ڵ� N : Not Access
		 *
		 * ������ �� ��޺��� ���������� �ڵ带 �ο��Ѵ�.
		 * ��)
		 * -------------------------------
		 * 1�� | 2�� | 3�� | ��ܺ� | �Ϲ�
		 * -------------------------------
		 *  N  |  N  |  C  |    A   |   A
		 * -------------------------------
		 *
		 **************************************************************/

		//���� �ۼ����� ��쿡�� ������ �㰡
		if(doc_writer.equals(login_id)){		
			is_commit = true;

		}

		//�ش� ������޿� ���� ��� ������ ������ ���
		if(access_code.equals("A")){
			is_commit = true;
		}

		//���� ����ο��� �߻��� ������ ���ؼ��� ������ ������ ���,
		//���� �ۼ����� ������ڵ�� �α��� ������� ������ڵ尡 ���� ���� �㰡
		//���⼭�� ������ڵ带 3�ڸ��� �����߱� ������ substring(0,3)���� �ڵ带
		//����������, ������ڵ� �ڸ����� �ٲ�� �� �κ��� �����Ǿ�� ��.
		if(access_code.equals("C")){		
			String doc_writer_center	= doc_writer_division.substring(0,3);	//���� �ۼ��� ����� �ڵ�
			String login_center			= login_division.substring(0,3);		//�α��� ����� ����� �ڵ�

			if(doc_writer_center.equals(login_center)) is_commit = true;
		}

		//���� �μ����� �߻��� ������ ���ؼ��� ������ ������ ���,
		//���� �ۼ����� �μ��ڵ�� �α��� ������� �μ��ڵ尡 ���� ���� �㰡
		if(access_code.equals("D")){		
			if(doc_writer_division.equals(login_division)) is_commit = true;
		}

		//�ش繮���� ���� �ӽÿ����� ������ ������ ���
		if(isTmpAccessPrivilege(doc_no,ver_code,login_id)){									
			is_commit = true;
		}

		//�ش繮���� ���� ���⿡ ���� ������ ������ ������ ���
		if(isLoanAccessPrivilege(doc_no,ver_code,login_id)){									
			is_commit = true;
		}

		/**************************************************************
		 * ������ ���� ���º� ������ ���� ü��
		 *
		 * stat == 1 : ó����(��� ��)
		 * stat == 2 : ���� ������
		 * stat == 3 : ���� ���� �Ϸ�,������ Ȯ�� ���
		 * stat == 4 : �ݷ���
		 * stat == 5 : ����(������ ������ �ִ� ��� ���� ����)
		 * stat == 6 : ������
		 * stat == 9 : ������
		 **************************************************************/

		//���� ������ �����ڵ带 ���� ��, 
		//�����ڵ尡 1(��� ��) �Ǵ� 3(��������,������ Ȯ�δ��) �Ǵ� 4(�ݷ�) �� �ƴϸ� ������ �� ����.
		//�ۼ��� �Ǵ� ����ڸ��� ���� �Ǵ� ���� �����ϴ�.
		if(mode.equals("modify") || mode.equals("delete") || mode.equals("loan_del")){
			if((stat.equals("1") || stat.equals("3") || stat.equals("4")) && (doc_writer.equals(id) || doc_register.equals(id))) is_commit = true;

			else is_commit = false;
		}

		//�����ڵ尡 5(�������)�� �ƴϸ� �������� �� ����.
		//����, �����ڵ尡 5������ ���� ������ �ƴϸ� ������ �� �� ����.
		if(mode.equals("revision")){
			if(!stat.equals("5")) is_commit = false;
			if(!isLastVersion(tablename,data_id,ver_code)) is_commit = false;
		}

		return is_commit;

	}

	public static void main(String args[])
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		AccessControlDAO tst = new AccessControlDAO(con);
		try{
//			tst.isLoanAccessPrivilege("T04-001","1.0","G000402");
tst.getMyPrivilege("techdoc_data","download","G000402","1101280413718","1.0");
		}catch (Exception e){
			System.out.println(e);
		}
	}
}		