package com.anbtech.gw.db;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class AppCheckProcessDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();	//포멧
	private String query = "";
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public AppCheckProcessDAO(Connection con) 
	{
		this.con = con;
	}

	/*************************************************************
		전자결재 승인시 비밀번호 묻기여부 : 승인단계 적용여부 (단,일괄협조시만)
		PWDAPL [0:비빌번호 요구하지 않음,  1:비밀번호 요구]
	/*************************************************************/
	public String applyPwdAPL() throws Exception
	{
		
		String where ="where env_name='PWDAPL'";
		String pass = getColumData("APP_ENV","env_value",where);
		if(pass.length() == 0) pass = "0";
		return pass;
	}

	/*************************************************************
		전자결재 검토시 비밀번호 묻기 : 검토단계 적용여부
		PWDAPV [0:비빌번호 요구하지 않음,  1:비밀번호 요구]
	/*************************************************************/
	public String applyPwdAPV() throws Exception
	{

		String where ="where env_name='PWDAPV'";
		String pass = getColumData("APP_ENV","env_value",where);
		if(pass.length() == 0) pass = "0";
		return pass;
	}

	/*************************************************************
		전자결재 협조시 비밀번호 묻기 : 협조단계 적용여부 (단,일괄협조시만)
		PWDAPG [0:비빌번호 요구하지 않음,  1:비밀번호 요구]
	/*************************************************************/
	public String applyPwdAPG() throws Exception
	{

		String where ="where env_name='PWDAPG'";
		String pass = getColumData("APP_ENV","env_value",where);
		if(pass.length() == 0) pass = "0";
		return pass;
	}

	/*************************************************************
		전자결재 현재 결재단계
	/*************************************************************/
	public String checkStatus(String pid) throws Exception
	{
		String where ="where pid='"+pid+"'";
		String status = getColumData("APP_MASTER","app_state",where);

		return status;
	}

	/*************************************************************
		비밀번호 검색하여 맞는지 여부 판단하기 : 비밀번호리턴
	/*************************************************************/
	public String checkPasswd(String login_id,String passwd) throws Exception
	{

		String status = "",where="";	
		String pmg = getPwdMgr();		//전자결재시 비밀번호 사용여부 판단(1:사용,0:미사용)

		//전자결재 환경테이블에서 비밀번호 찾기
		if(pmg.equals("1")) {
			where ="where env_name='"+login_id+"' and env_value='"+passwd+"'";
			status = getColumData("APP_ENV","env_value",where);
		}
		//사용자 테이블에서 비밀번호 맞는지 판단하기
		else {
			where ="where id='"+login_id+"' and passwd='"+passwd+"'";
			status = getColumData("user_table","passwd",where);
		}
		return status;
	}

	/*************************************************************
		전자결재 Admin 사번알아내기 
		APPADMIN : 사번
	/*************************************************************/
	public String getAdminID() throws Exception
	{
		String where ="where env_name='APPADMIN'";
		String pass = getColumData("APP_ENV","env_value",where);
		if(pass.length() == 0) pass = "0";
		return pass;
	}

	/*************************************************************
		전자결재 비밀번호 관리여부 판단하기
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
	//		공통 메소드 정의
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// SQL update 실행하기
	//*******************************************************************/	
	public void executeUpdate(String update) throws Exception
	{
		Statement stmt = con.createStatement();
		stmt.executeUpdate(update);
		stmt.close();
	}

	//*******************************************************************
	//	주어진 테이블에서 주어진 조건의 컬럼의 데이터 읽기
	//*******************************************************************/
	public String getColumData(String tablename,String getcolumn,String where) throws Exception
	{
		//변수 초기화
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