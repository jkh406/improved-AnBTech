package com.anbtech.gw.db;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class AppCheckProcessDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();	//����
	private String query = "";
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public AppCheckProcessDAO(Connection con) 
	{
		this.con = con;
	}

	/*************************************************************
		���ڰ��� ���ν� ��й�ȣ ���⿩�� : ���δܰ� ���뿩�� (��,�ϰ������ø�)
		PWDAPL [0:�����ȣ �䱸���� ����,  1:��й�ȣ �䱸]
	/*************************************************************/
	public String applyPwdAPL() throws Exception
	{
		
		String where ="where env_name='PWDAPL'";
		String pass = getColumData("APP_ENV","env_value",where);
		if(pass.length() == 0) pass = "0";
		return pass;
	}

	/*************************************************************
		���ڰ��� ����� ��й�ȣ ���� : ����ܰ� ���뿩��
		PWDAPV [0:�����ȣ �䱸���� ����,  1:��й�ȣ �䱸]
	/*************************************************************/
	public String applyPwdAPV() throws Exception
	{

		String where ="where env_name='PWDAPV'";
		String pass = getColumData("APP_ENV","env_value",where);
		if(pass.length() == 0) pass = "0";
		return pass;
	}

	/*************************************************************
		���ڰ��� ������ ��й�ȣ ���� : �����ܰ� ���뿩�� (��,�ϰ������ø�)
		PWDAPG [0:�����ȣ �䱸���� ����,  1:��й�ȣ �䱸]
	/*************************************************************/
	public String applyPwdAPG() throws Exception
	{

		String where ="where env_name='PWDAPG'";
		String pass = getColumData("APP_ENV","env_value",where);
		if(pass.length() == 0) pass = "0";
		return pass;
	}

	/*************************************************************
		���ڰ��� ���� ����ܰ�
	/*************************************************************/
	public String checkStatus(String pid) throws Exception
	{
		String where ="where pid='"+pid+"'";
		String status = getColumData("APP_MASTER","app_state",where);

		return status;
	}

	/*************************************************************
		��й�ȣ �˻��Ͽ� �´��� ���� �Ǵ��ϱ� : ��й�ȣ����
	/*************************************************************/
	public String checkPasswd(String login_id,String passwd) throws Exception
	{

		String status = "",where="";	
		String pmg = getPwdMgr();		//���ڰ���� ��й�ȣ ��뿩�� �Ǵ�(1:���,0:�̻��)

		//���ڰ��� ȯ�����̺��� ��й�ȣ ã��
		if(pmg.equals("1")) {
			where ="where env_name='"+login_id+"' and env_value='"+passwd+"'";
			status = getColumData("APP_ENV","env_value",where);
		}
		//����� ���̺��� ��й�ȣ �´��� �Ǵ��ϱ�
		else {
			where ="where id='"+login_id+"' and passwd='"+passwd+"'";
			status = getColumData("user_table","passwd",where);
		}
		return status;
	}

	/*************************************************************
		���ڰ��� Admin ����˾Ƴ��� 
		APPADMIN : ���
	/*************************************************************/
	public String getAdminID() throws Exception
	{
		String where ="where env_name='APPADMIN'";
		String pass = getColumData("APP_ENV","env_value",where);
		if(pass.length() == 0) pass = "0";
		return pass;
	}

	/*************************************************************
		���ڰ��� ��й�ȣ �������� �Ǵ��ϱ�
		APPMGR : 0 / 1
	/*************************************************************/
	public String getPwdMgr() throws Exception
	{
		String where ="where env_name='PWDMGR'";
		String pass = getColumData("APP_ENV","env_value",where);
		if(pass.length() == 0) pass = "0";
		return pass;
	}

	//--------------------------------------------------------------------
	//
	//		���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// SQL update �����ϱ�
	//*******************************************************************/	
	public void executeUpdate(String update) throws Exception
	{
		Statement stmt = con.createStatement();
		stmt.executeUpdate(update);
		stmt.close();
	}

	//*******************************************************************
	//	�־��� ���̺��� �־��� ������ �÷��� ������ �б�
	//*******************************************************************/
	public String getColumData(String tablename,String getcolumn,String where) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString(getcolumn);
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
}