package com.anbtech.dms.db;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.anbtech.admin.SessionLib;
import com.anbtech.dms.entity.*;

public class AccessControlDAO{
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public AccessControlDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	 * 해당 사번의 부서 코드를 구한다.
	 * 여기서의 부서코드는 내부적으로 관리되어지는 코드임.
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
	 * 해당 사번의 이름을 가져온다.
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
	 * 현재 로그인한 사용자의 엑세스 권한 코드를 구한다.
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
	 * 검색된 데이터번호와 버젼에 맞는 레코드 정보를 가져온다.
	 * ancestor : 데이터 번호,동일버젼에 대해서는 모두 동일 데이터번호임
	 * version  : 버젼 코드
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
	 * 현재 엑세스중인 문서가 최종 버젼인지를 체크한다.
	 *******************************************************************/
	public boolean isLastVersion(String tablename,String data_id,String ver_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		boolean is_last_version = false;

		String query = "SELECT count(*) FROM " + tablename + " where ancestor = '" + data_id +"' and ver_code > '" + ver_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		
		if(rs.getString(1) == null || rs.getString(1).equals("0")) is_last_version =  true; // 최신버젼일 경우 참

		stmt.close();
		rs.close();

		return is_last_version;


	} //isLastVersion()

	/*******************************************************************
	 * 현재 문서에 대한 임시엑세스 권한이 있는지 파악한다.
	 *******************************************************************/
	public boolean isTmpAccessPrivilege(String doc_no,String version,String login_id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		//엑세스 시점의 날짜 가져오기
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
	 * 현재 문서를 대출에 의한 액세스 권한이 있는지 검사한다.
	 *******************************************************************/
	public boolean isLoanAccessPrivilege(String doc_no,String version,String login_id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		//엑세스 시점의 날짜 가져오기
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
	 * 현재 엑세스중인 문서에 대한 나의 권한을 체크한다.
	 * 첨부파일 다운로드 권한, 수정 또는 Revision 할 수 있는지에 사용된다.
	 **********************************************************************/
	public boolean getMyPrivilege(String tablename,String mode,String id,String data_id,String ver_code) throws Exception{
		String login_id = id; //현재 로긴한 사용자의 사번

		//현재 로긴한 사용자의 정보 가져오기
		String login_division = getDivisionCode(login_id);	// 사용자 부서코드
		String login_access_code = getAccessCode(login_id);	// 사용자 엑세스코드

		//엑세스대상 문서정보 가져오기
		AccessControlTable table = new AccessControlTable();
		table = getDocInfo(tablename,data_id,ver_code);
		String doc_no			= table.getDocNo();
		String doc_writer		= table.getWriter();		// 문서 작성자 사번
		String doc_register		= table.getRegister();		// 문서 등록자 사번
		String security_level	= table.getSecurityLevel();	// 문서 보안 등급
		String stat				= table.getStat();			// 현재 엑세스 중인 문서의 상태코드		
		String doc_writer_division = getDivisionCode(doc_writer);	// 문서 작성자의 부서코드

		//문서 보안등급에 해당하는 엑세스 권한 코드 파악
		//예)해당 문서가 3급 문서일 경우 로긴한 사용자의 엑세스코드 중 3번째 비트값을 추출한다.
		String access_code = login_access_code.substring(Integer.parseInt(security_level)-1,Integer.parseInt(security_level));

		boolean is_commit = false;

		/**************************************************************
		 * 문서의 등급별 엑세스 권한 체그
		 *
		 * 코드 A : All Access
		 * 코드 C : 동일 사업부(또는 본부)에서 발생한 문서에 대해서만 엑세스 가능
		 *          사업부는 레벨1단계인 것으로로 가정한다.
		 * 코드 D : 동일 부서에서 발생한 문서에 대해서만 엑세스 가능
		 *          부서는 레벨2단계인 것으로 가정한다.
		 * 코드 N : Not Access
		 *
		 * 문서의 각 등급별로 엑세스권한 코드를 부여한다.
		 * 예)
		 * -------------------------------
		 * 1급 | 2급 | 3급 | 대외비 | 일반
		 * -------------------------------
		 *  N  |  N  |  C  |    A   |   A
		 * -------------------------------
		 *
		 **************************************************************/

		//문서 작성자일 경우에는 엑세스 허가
		if(doc_writer.equals(login_id)){		
			is_commit = true;

		}

		//해당 문서등급에 대한 모든 권한을 가지는 경우
		if(access_code.equals("A")){
			is_commit = true;
		}

		//동일 사업부에서 발생한 문서에 대해서만 엑세스 가능한 경우,
		//문서 작성자의 사업부코드와 로긴한 사용자의 사업부코드가 같을 때만 허가
		//여기서는 사업부코드를 3자리로 정의했기 때문에 substring(0,3)으로 코드를
		//가져왔지만, 사업부코드 자리수가 바뀌면 이 부분이 수정되어야 함.
		if(access_code.equals("C")){		
			String doc_writer_center	= doc_writer_division.substring(0,3);	//문서 작성자 사업부 코드
			String login_center			= login_division.substring(0,3);		//로긴한 사용자 사업부 코드

			if(doc_writer_center.equals(login_center)) is_commit = true;
		}

		//동일 부서에서 발생한 문서에 대해서만 엑세스 가능한 경우,
		//문서 작성자의 부서코드와 로긴한 사용자의 부서코드가 같을 때만 허가
		if(access_code.equals("D")){		
			if(doc_writer_division.equals(login_division)) is_commit = true;
		}

		//해당문서에 대해 임시엑세스 권한을 가지는 경우
		if(isTmpAccessPrivilege(doc_no,ver_code,login_id)){									
			is_commit = true;
		}

		//해당문서를 현재 대출에 의한 엑세스 권한을 가지는 경우
		if(isLoanAccessPrivilege(doc_no,ver_code,login_id)){									
			is_commit = true;
		}

		/**************************************************************
		 * 문서의 현재 상태별 엑세스 권한 체그
		 *
		 * stat == 1 : 처리중(상신 전)
		 * stat == 2 : 결재 진행중
		 * stat == 3 : 최종 승인 완료,관리자 확인 대기
		 * stat == 4 : 반려됨
		 * stat == 5 : 정상(엑세스 권한이 있는 경우 열람 가능)
		 * stat == 6 : 대출중
		 * stat == 9 : 삭제됨
		 **************************************************************/

		//현재 문서의 상태코드를 얻어온 후, 
		//상태코드가 1(상신 전) 또는 3(최종승인,관리자 확인대기) 또는 4(반려) 가 아니면 수정할 수 없다.
		//작성자 또는 등록자만이 수정 또는 삭제 가능하다.
		if(mode.equals("modify") || mode.equals("delete") || mode.equals("loan_del")){
			if((stat.equals("1") || stat.equals("3") || stat.equals("4")) && (doc_writer.equals(id) || doc_register.equals(id))) is_commit = true;

			else is_commit = false;
		}

		//상태코드가 5(정상상태)가 아니면 리비젼할 수 없다.
		//또한, 상태코드가 5일지라도 최종 버젼이 아니면 리비젼 할 수 없다.
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