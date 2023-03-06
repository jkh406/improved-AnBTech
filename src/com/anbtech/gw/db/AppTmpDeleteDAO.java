package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.file.textFileReader;

public class AppTmpDeleteDAO
{
	private DBConnectionManager connMgr;
	private Connection con;
	private String login_id = "";		//접속자 사번
	private String root_path = "";		//첨부파일 root directory
	private String tablename = "";		//table name
	private String bon_path = ""; 		//본문path									
	private String bon_file = ""; 		//본문파일명
	private String add_f1 = "";			//첨부1
	private String add_f2 = "";			//첨부2
	private String add_f3 = "";			//첨부3
	private String flag = "";			//전자결재 종류
	private String plid = "";			//관련전자결재 관리번호

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public AppTmpDeleteDAO(Connection con) 
	{
		this.con = con;
	}

	public AppTmpDeleteDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}

	//*******************************************************************
	//	주어진 관리번호로 필요내용 찾아 관련내용 삭제하기
	//*******************************************************************/	
	public void deletePid (String pid,String id,String root_path) throws Exception
	{
		//변수 초기화
		if(pid == null) pid = "";			//관리번호
		this.login_id = id;					//사번
		this.root_path = root_path;			//첨부파일 저장 root path

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query문장 만들기
		String query  = "SELECT * FROM APP_MASTER where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			this.bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = ""; //본문path									
			this.bon_file = rs.getString("bon_file");	if(bon_file == null) bon_file = ""; //본문파일명
			this.add_f1 = rs.getString("add_1_file");	if(add_f1 == null) add_f1 = "";		//첨부1
			this.add_f2 = rs.getString("add_2_file");	if(add_f2 == null) add_f2 = "";		//첨부2
			this.add_f3 = rs.getString("add_3_file");	if(add_f3 == null)	add_f3 = "";	//첨부3		
			this.plid = rs.getString("Plid");			if(plid == null)	plid = "";		//관련문서 관리번호	
			this.flag = rs.getString("flag");			if(flag == null)	flag = "";		//관련문서 종류	
		}
		//관련내용 출력하기
/*		//System.out.println("bon_path : " + this.bon_path);
		//System.out.println("bon_file : " + this.bon_file);
		//System.out.println("add_f1 : " + this.add_f1);
		//System.out.println("add_f2 : " + this.add_f2);
		//System.out.println("add_f3 : " + this.add_f3);
		//System.out.println("plid : " + this.plid);
		//System.out.println("flag : " + this.flag);
*/
		//관련문서 종류별 구분하여 Table내용,첨부파일,본문파일등 삭제하기
		//일반문서
		if(flag.equals("GEN")) {
			deleteBonText(this.root_path,this.bon_path,this.bon_file);		//본문파일 삭제하기
			if(add_f1.length() != 0)
				deleteAddFile(this.root_path,this.bon_path,this.add_f1);	//첨부파일1 삭제하기
			if(add_f2.length() != 0)
				deleteAddFile(this.root_path,this.bon_path,this.add_f2);	//첨부파일2 삭제하기
			if(add_f3.length() != 0)
				deleteAddFile(this.root_path,this.bon_path,this.add_f3);	//첨부파일1 삭제하기
			deleteTableLine("app_master","pid",pid);						//app_master의 관련line의 데이터 삭제하기
		}
		//고객관리 문서
		else if(flag.equals("SERVICE")) {
			deleteTableLine("app_master","pid",pid);						//app_master의 관련line의 데이터 삭제하기
		}
		//근태(휴가원,외출계,출장신청서) 문서
		else if(flag.equals("HYU_GA") || flag.equals("OE_CHUL") || flag.equals("CHULJANG_SINCHEONG")) {
			deleteTableLine("app_master","pid",pid);			//app_master의 관련line의 데이터 삭제하기
			deleteTableLink("geuntae_master","gt_id",pid);			//근태관리 테이블 MASTER 의 관련 관리번호삭제
			deleteTableSubLink("geuntae_account","gt_id",pid);		//근태관리 테이블 ACCOUNT 의 관련 관리번호삭제
		}
		//보고서 관리(보고서,출장보고서)
		else if(flag.equals("BOGO") || flag.equals("CHULJANG_BOGO")) {	
			deleteTableLine("app_master","pid",pid);			//app_master의 관련line의 데이터 삭제하기
			deleteBonmunLink("bogoseo_master","bg_id",pid,this.root_path);	 //본문저장파일 삭제
			deleteAttacheLink("bogoseo_master","bg_id",pid,this.root_path);	 //첨부저장파일 삭제
			deleteTableLink("bogoseo_master","bg_id",pid);			//보고서관리 테이블 MASTER 의 관련 관리번호삭제
		}
		//지원 관리(기안서,명함신청서,사유서,협조전)
		else if(flag.equals("GIAN") || flag.equals("MYEONGHAM") || flag.equals("SAYU") || flag.equals("HYEOPJO")) {	
			deleteTableLine("app_master","pid",pid);			//app_master의 관련line의 데이터 삭제하기
			deleteBonmunLink("jiweon_master","jw_id",pid,this.root_path);	//본문저장파일 삭제
			deleteNoteLink("jiweon_master","jw_id",pid,this.root_path);		//Note저장파일 삭제
			deleteAttacheLink("jiweon_master","jw_id",pid,this.root_path);	//첨부저장파일 삭제
			deleteTableLink("jiweon_master","jw_id",pid);			//보고서관리 테이블 MASTER 의 관련 관리번호삭제
		}
		//연장근무(잔업) 관리(연장근무신청서)
		else if(flag.equals("YEONJANG")) {
			deleteTableLine("app_master","pid",pid);			//app_master의 관련line의 데이터 삭제하기
			deleteTableLink("janeup_master","ju_id",pid);			//지원관리 테이블 MASTER 의 관련 관리번호삭제
			deleteTableSubLink("janeup_worker","ju_id",pid);			//지원관리 테이블 WORKER 의 관련 관리번호삭제
		}
		//인사 관리(구인의뢰서)
		else if(flag.equals("GUIN")) {
			deleteTableLine("app_master","pid",pid);			//app_master의 관련line의 데이터 삭제하기
			deleteTableLink("insa_master","is_id",pid);				//인사관리 테이블 MASTER 의 관련 관리번호삭제
		}
		//교육 관리(교육일지)
		else if(flag.equals("GYOYUK_ILJI")) {
			deleteTableLine("app_master","pid",pid);			//app_master의 관련line의 데이터 삭제하기
			deleteBonmunLink("gyoyuk_master","gy_id",pid,this.root_path);	//본문저장파일 삭제
			deleteTableLink("gyoyuk_master","gy_id",pid);			//교육관리 테이블 MASTER 의 관련 관리번호삭제
			deleteTableSubLink("gyoyuk_part","gy_id",pid);			//교육관리 테이블 PART 의 관련 관리번호삭제
		}
		//기타 [반려함을 전자결재내의 내용만 삭제함]
		else {
			deleteTableLine("app_master","pid",pid);			//app_master의 관련line의 데이터 삭제하기
		}
			

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}
	//*******************************************************************
	//	본문파일 삭제하기
	//*******************************************************************/	
	public void deleteBonText (String root_path,String bon_path,String bon_file)
	{
		com.anbtech.file.textFileReader text = new com.anbtech.file.textFileReader();
		String filename = root_path + bon_path + "/bonmun/" + bon_file;
		//System.out.println("bonmun file : " + filename);
		text.delFilename(filename);	//해당 파일삭제 하기
	}
	
	//*******************************************************************
	//	첨부파일 삭제하기
	//*******************************************************************/	
	public void deleteAddFile (String root_path,String bon_path,String add_file)
	{
		com.anbtech.file.textFileReader text = new com.anbtech.file.textFileReader();
		String filename = root_path + bon_path + "/addfile/" + add_file;
		//System.out.println("add file : " + filename);
		text.delFilename(filename);	//해당 파일삭제 하기
	}

	//*******************************************************************
	//	APP Master Table의 Line삭제하기 
	//*******************************************************************/	
	public void deleteTableLine (String tablename,String pid_column_name,String pid) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String query  = "delete FROM "+tablename+" where "+pid_column_name+"='"+pid+"'";
		//System.out.println("delete : " + query);
		stmt.execute(query);
		stmt.close();
	}

	//*******************************************************************
	//	관련문서의 Table의 Line삭제하기 
	// 1차측(주관부서)의 결재시만 가능, 2차측(집행부서)의 결재시는 실행않됨)
	//*******************************************************************/	
	public void deleteTableLink (String tablename,String pid_column_name,String pid) throws Exception
	{
		if(checkFlag(tablename,pid_column_name,pid)) {
			Statement stmt = null;
			stmt = con.createStatement();
			String query  = "delete FROM "+tablename+" where "+pid_column_name+"='"+pid+"'";
			//System.out.println("deleteTableLink : " + query);
			stmt.execute(query);
			stmt.close();
		}
		
	}

	//*******************************************************************
	//	관련문서의 sub TABLE삭제 
	// 1차측(주관부서)의 결재시만 가능, 2차측(집행부서)의 결재시는 실행않됨)
	//*******************************************************************/	
	public void deleteTableSubLink (String tablename,String pid_column_name,String pid) throws Exception
	{	
		Statement stmt = null;
		stmt = con.createStatement();
		String query  = "delete FROM "+tablename+" where "+pid_column_name+"='"+pid+"'";
		//System.out.println("deleteTableLink : " + query);
		stmt.execute(query);
		stmt.close();	
	}

	//*******************************************************************
	//	결재양식의 본문내용 삭제하기
	// 1차측(주관부서)의 결재시만 가능, 2차측(집행부서)의 결재시는 실행않됨)
	//*******************************************************************/	
	private void deleteBonmunLink(String tablename,String pid_column_name,String pid,String root_path) throws Exception
	{
		if(checkFlag(tablename,pid_column_name,pid)) {
			String query  = "";
			Statement stmt = null;
			ResultSet rs = null;
			stmt = con.createStatement();

			//본문path 및 본문파일 구하여 삭제하기
			query = "select bon_path,bon_file from "+tablename+" where "+pid_column_name+"='"+pid+"'";
//			//System.out.println("본문삭제 query : " + query );
			rs = stmt.executeQuery(query);
			if(rs.next()) { 
				String bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = ""; //본문path									
				String bon_file = rs.getString("bon_file");	if(bon_file == null) bon_file = ""; //본문파일명
//				//System.out.println("본문삭제 path,파일명 : " + bon_path +  " : " + bon_file );
				deleteBonText(root_path,bon_path,bon_file);
			}
			stmt.close();
			rs.close();
		}
	}

	//*******************************************************************
	//	결재양식의 note내용 삭제하기
	// 1차측(주관부서)의 결재시만 가능, 2차측(집행부서)의 결재시는 실행않됨)
	//*******************************************************************/	
	private void deleteNoteLink(String tablename,String pid_column_name,String pid,String root_path) throws Exception
	{
		if(checkFlag(tablename,pid_column_name,pid)) {
			String query  = "";
			Statement stmt = null;
			ResultSet rs = null;
			stmt = con.createStatement();

			//본문path 및 Note파일 구하여 삭제하기
			query = "select bon_path,note_file from "+tablename+" where "+pid_column_name+"='"+pid+"'";
			rs = stmt.executeQuery(query);
			if(rs.next()) { 
				String bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = ""; //본문path									
				String note_file = rs.getString("note_file");if(note_file == null) note_file = ""; //Note파일명
				deleteBonText(root_path,bon_path,note_file);
			}
			stmt.close();
			rs.close();
		}
	}

	//*******************************************************************
	//	결재양식의 첨부파일 삭제하기
	// 1차측(주관부서)의 결재시만 가능, 2차측(집행부서)의 결재시는 실행않됨)
	//*******************************************************************/	
	private void deleteAttacheLink(String tablename,String pid_column_name,String pid,String root_path) throws Exception
	{
		if(checkFlag(tablename,pid_column_name,pid)) {
			String query  = "";
			Statement stmt = null;
			ResultSet rs = null;
			stmt = con.createStatement();

			//본문path 및 첨부파일 구하여 삭제하기
			query = "select bon_path,sname from "+tablename+" where "+pid_column_name+"='"+pid+"'";
			rs = stmt.executeQuery(query);
			if(rs.next()) { 
				String bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = ""; //본문path									
				String sname = rs.getString("sname");if(sname == null) sname = ""; //첨부 파일명

				StringTokenizer s = new StringTokenizer(sname,"|");		//저장된 첨부파일
				while(s.hasMoreTokens()) {
					String add_file = s.nextToken();
					add_file = add_file.trim()+".bin";
					////System.out.println("첨부파일 삭제 : " + add_file);
					deleteAddFile(root_path,bon_path,add_file);
				}
			}
			stmt.close();
			rs.close();
		}
	}

	//*******************************************************************
	//	결재양식의 Table의 Line삭제하기 여부 판단하기 
	// 1차측(주관부서)의 결재시만 가능, 2차측(집행부서)의 결재시는 실행않됨)
	//*******************************************************************/	
	private boolean checkFlag(String tablename,String pid_column_name,String pid) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String data = "";
		String query = "select flag from "+tablename+" where "+pid_column_name+"='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("flag");
		
		stmt.close();
		rs.close();
		
		if(data == null) data = "";
		if(data.equals("null")) data = "";
//		//System.out.println("data : " + data);

		if(data.length() == 0) return true;		//임시저장으로 삭제가능
		else return false;						//결재중
	}
}
