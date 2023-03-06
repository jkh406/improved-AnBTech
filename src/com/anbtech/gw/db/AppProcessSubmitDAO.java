package com.anbtech.gw.db;
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import com.anbtech.dbconn.*;
import com.anbtech.gw.*;
import com.anbtech.date.*;
import com.anbtech.es.geuntae.db.*;
import com.anbtech.util.normalFormat;
import com.anbtech.file.FileWriteString;
import java.lang.SecurityException;
import java.io.UnsupportedEncodingException;

import com.anbtech.dbconn.DBConnectionManager;		

public class AppProcessSubmitDAO
{
	// Database Wrapper Class 선언
	private DBConnectionManager connMgr;
	private Connection con;

	private com.anbtech.date.anbDate anbdt = null;					//일자 처리
	private com.anbtech.util.normalFormat nmf = null;				//출력포멧
	private com.anbtech.es.geuntae.db.HyuGaDayDAO hyugaDayDAO;		//휴가원 일수 입력(1차 결재승인시만)
	private com.anbtech.es.geuntae.db.ChulJangDayDAO chuljangDayDAO;//출장원 일수 입력(1차 결재승인시만)
	private com.anbtech.file.FileWriteString text;					//내용을 파일로 담기
	private com.anbtech.gw.business.ModuleApprovalBO mdBO;			//기술문서,승인원 승인/반려 처리


	// 상신 전달변수
	private String doc_pid="";			//결재문서 번호
	private String doc_sub="";			//결재문서 제목

	private String doc_wri="";			//결재문서 기안자 id
	private String doc_wna="";			//결재문서 기안자 이름 	
	private String doc_wrd="";			//결재문서 기안일자

	private String doc_rev="";			//결재문서 검토자 id
	private String doc_red="";			//결재문서 검토일자
	private String doc_rem="";			//결재문서 검토의견

	private String doc_agr="";			//결재문서 합의자 id
	private String doc_agd="";			//결재문서 합의일자
	private String doc_agm="";			//결재문서 합의의견

	private String doc_agr2="";			//결재문서 합의자2
	private String doc_agd2="";			//결재문서 합의일자
	private String doc_agm2="";			//결재문서 합의의견

	private String doc_agr3="";			//결재문서 합의자3
	private String doc_agd3="";			//결재문서 합의일자
	private String doc_agm3="";			//결재문서 합의의견

	private String doc_agr4="";			//결재문서 합의자4
	private String doc_agd4="";			//결재문서 합의일자
	private String doc_agm4="";			//결재문서 합의의견

	private String doc_agr5="";			//결재문서 합의자5
	private String doc_agd5="";			//결재문서 합의일자
	private String doc_agm5="";			//결재문서 합의의견

	private String doc_agr6="";			//결재문서 합의자6
	private String doc_agd6="";			//결재문서 합의일자
	private String doc_agm6="";			//결재문서 합의의견

	private String doc_agr7="";			//결재문서 합의자7
	private String doc_agd7="";			//결재문서 합의일자
	private String doc_agm7="";			//결재문서 합의의견

	private String doc_agr8="";			//결재문서 합의자8
	private String doc_agd8="";			//결재문서 합의일자
	private String doc_agm8="";			//결재문서 합의의견

	private String doc_agr9="";			//결재문서 합의자9
	private String doc_agd9="";			//결재문서 합의일자
	private String doc_agm9="";			//결재문서 합의의견
	
	private String doc_agr10="";		//결재문서 합의자10
	private String doc_agd10="";		//결재문서 합의일자
	private String doc_agm10="";		//결재문서 합의의견

	private String agr_met="";			//합의 방법 (순차적 / 일괄적)
	private int agr_cnt=0;				//합의자 총인원 
	private String agr_pas="";			//현재 합의자 총인원

	private String doc_dec="";			//결재문서 승인자
	private String doc_ded="";			//결재문서 승인일자
	private String doc_dem="";			//결재문서 승인의견

	private String doc_rec="";			//결재문서 전달받는자
	private int rec_str_cnt=0;			//전달밭는자 인원수
	
	private String doc_lin="";			//결재문서 결재라인

	private String doc_pat="";			//결재문서 본문내용 path (첨부파일동일)
	private String doc_bon="";			//결재문서 본문내용

	private String doc_atcnt="0";		//첨부파일 갯수

	private String doc_or1="";			//결재문서 첨부파일명1 (원래파일명)
	private String doc_ad1="";			//결재문서 첨부파일명1 (저장되는 파일명)

	private String doc_or2="";			//결재문서 첨부파일명2 (원래파일명)
	private String doc_ad2="";			//결재문서 첨부파일명2 (저장되는 파일명)

	private String doc_or3="";			//결재문서 첨부파일명3 (원래파일명)
	private String doc_ad3="";			//결재문서 첨부파일명3 (저장되는 파일명)

	private String doc_per="";			//결재문서 보존기간
	private String doc_del="";			//결재문서 삭제일자 (승인후 계산)
	private String doc_sec="";			//결재문서 보안등급

	//외부문서 처리변수
	private String doc_flag="";			//외부문서 종류
	private String doc_lid ="";			//외부문서 관리번호

	// 결재자 전달변수
	private String doc_ste="";			//결재문서 결재상태
	private String doc_dat="";			//결재문서 상신일자
	private String content="";			//결재문서 본문 읽기 (unix만 적용됨)

	// 내부처리 변수
	private static String doc_rev_title="";		// 결재선 입력 요청상태
	private String[] SEC_DATA;			// 결재선 입력 내용 분할데이터
	private String[] LINE_ORD;			// 결재자 결재순서
	private int lin_str_cnt = 0;		// 결재자 결재순서 총수량 

	private String[] receiver;			// 결재내용 통보받은자
	private String line_order_data;		// 결재선 내용

	private String[] sdiv = null;		// 통보자가 부서인 경우 하위부서 찾아 배열에 담기

	// 전달받아온 변수
	private String DOC_REQ="";			//문서 요청상태
	private String DOC_REQ_NAME="";		//문서 요청함 이름
	private String DOC_PID="";			//문서 번호	
	
	private String Message="";			//message 전달
	private String mail_path = "";		//결재승인후 메일을 보낼때 본문내용 저장할 upload+crp path
	private String ip_addr = "";		//게시판에 보낼 승인권자 ip_address
	
	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public AppProcessSubmitDAO(Connection con) 
	{	
		this.con = con;

		anbdt = new com.anbtech.date.anbDate();			//날자처리
		nmf = new com.anbtech.util.normalFormat("000");	//문서번호 일련번호
		hyugaDayDAO = new com.anbtech.es.geuntae.db.HyuGaDayDAO(con);			//근태일수 입력하기(1차 결재 승인시만)
		chuljangDayDAO = new com.anbtech.es.geuntae.db.ChulJangDayDAO(con);		//출장 일수 입력하기(1차 결재 승인시만)
		text = new com.anbtech.file.FileWriteString();	//내용을 파일로 담기 (개인우편보낼때 사용)
		mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);//기술문서,승인원 승인/반려 처리
	}
	
	/***************************************************************************
	 * 문서번호를 이용한 내용을 인스턴스 변수로 담기 
	 * 해당되는 내용을 각각의 인스턴스 변수로 담는다.
	 **************************************************************************/
	public boolean eleDecisionReadDoc(String idNo) throws SQLException 
	{
		boolean rtn = false;
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT distinct ";
		query += "pid,app_subj,writer,writer_name,write_date,app_state,reviewer,review_date,review_comment,";
		query += "agree,agree_date,agree_comment,agree2,agree2_date,agree2_comment,agree3,agree3_date,agree3_comment,";
		query += "agree4,agree4_date,agree4_comment,agree5,agree5_date,agree5_comment,agree6,agree6_date,agree6_comment,";
		query += "agree7,agree7_date,agree7_comment,agree8,agree8_date,agree8_comment,agree9,agree9_date,agree9_comment,";
		query += "agree10,agree10_date,agree10_comment,agree_method,agree_count,agree_pass,decision,decision_date,decision_comment,";
		query += "receivers,app_line,bon_path,bon_file,add_counter,add_1_original,add_1_file,add_2_original,add_2_file,";
		query += "add_3_original,add_3_file,save_period,delete_date,security_level,plid,flag ";
		query += "from APP_MASTER where pid='"+idNo+"'";
	
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			this.doc_pid=rs.getString("pid");							//결재문서 번호
			this.doc_sub=rs.getString("app_subj");						//결재문서 제목
			this.doc_wri=rs.getString("writer");						//결재문서 기안자 id
			this.doc_wna=rs.getString("writer_name");					//결재문서 기안자 이름
			this.doc_wrd=rs.getString("write_date");					//결재문서 기안일자
			this.doc_ste=rs.getString("app_state");						//결재문서 현재결재 상태

			this.doc_rev=rs.getString("reviewer");						//결재문서 검토자 id
			this.doc_red=rs.getString("review_date");					//결재문서 검토일자
			this.doc_rem=rs.getString("review_comment");				//결재문서 검토자 의견

			this.doc_agr=rs.getString("agree");							//결재문서 합의자 id
			this.doc_agd=rs.getString("agree_date");					//결재문서 합의일자
			this.doc_agm=rs.getString("agree_comment");					//결재문서 합의자 의견
			
			this.doc_agr2=rs.getString("agree2");						//결재문서 합의자
			this.doc_agd2=rs.getString("agree2_date");					//결재문서 합의일자
			this.doc_agm2=rs.getString("agree2_comment");				//결재문서 합의자 의견
			
			this.doc_agr3=rs.getString("agree3");						//결재문서 합의자
			this.doc_agd3=rs.getString("agree3_date");					//결재문서 합의일자
			this.doc_agm3=rs.getString("agree3_comment");				//결재문서 합의자 의견
			
			this.doc_agr4=rs.getString("agree4");						//결재문서 합의자
			this.doc_agd4=rs.getString("agree4_date");					//결재문서 합의일자
			this.doc_agm4=rs.getString("agree4_comment");				//결재문서 합의자 의견
	
			this.doc_agr5=rs.getString("agree5");						//결재문서 합의자
			this.doc_agd5=rs.getString("agree5_date");					//결재문서 합의일자
			this.doc_agm5=rs.getString("agree5_comment");				//결재문서 합의자 의견
	
			this.doc_agr6=rs.getString("agree6");						//결재문서 합의자
			this.doc_agd6=rs.getString("agree6_date");					//결재문서 합의일자
			this.doc_agm6=rs.getString("agree6_comment");				//결재문서 합의자 의견
	
			this.doc_agr7=rs.getString("agree7");						//결재문서 합의자
			this.doc_agd7=rs.getString("agree7_date");					//결재문서 합의일자
			this.doc_agm7=rs.getString("agree7_comment");				//결재문서 합의자 의견

			this.doc_agr8=rs.getString("agree8");						//결재문서 합의자
			this.doc_agd8=rs.getString("agree8_date");					//결재문서 합의일자
			this.doc_agm8=rs.getString("agree8_comment");				//결재문서 합의자 의견
	
			this.doc_agr9=rs.getString("agree9");						//결재문서 합의자
			this.doc_agd9=rs.getString("agree9_date");					//결재문서 합의일자
			this.doc_agm9=rs.getString("agree9_comment");				//결재문서 합의자 의견
			
			this.doc_agr10=rs.getString("agree10");						//결재문서 합의자
			this.doc_agd10=rs.getString("agree10_date");				//결재문서 합의일자
			this.doc_agm10=rs.getString("agree10_comment");				//결재문서 합의자 의견
			
			this.agr_met=rs.getString("agree_method");					//합의자 결재순서
			String doc_cnt = rs.getString("agree_count");				//합의자 총인원수
			if(doc_cnt == null) doc_cnt = "0";
			else if(doc_cnt.length() == 0) doc_cnt = "0";
			this.agr_cnt = Integer.parseInt(doc_cnt);

			this.agr_pas = rs.getString("agree_pass");					//현재 합의자 총인원 
			if(this.agr_pas == null) this.agr_pas = "0";
			else if(this.agr_pas.length() == 0) this.agr_pas = "0";

			this.doc_dec=rs.getString("decision");						//결재문서 승인자
			this.doc_ded=rs.getString("decision_date");					//결재문서 승인일자
			this.doc_dem=rs.getString("decision_comment");				//결재문서 승인자 의견
		
			this.doc_rec=rs.getString("receivers");						//결재문서 전달받는자
			this.doc_lin=rs.getString("app_line");						//결재문서 결재라인

			this.doc_pat=rs.getString("bon_path");						//결재문서 본문내용 path (첨부파일동일)
			this.doc_bon=rs.getString("bon_file");						//결재문서 본문내용 file명

			this.doc_atcnt=rs.getString("add_counter");					//첨부파일 갯수
			if(doc_atcnt == null) this.doc_atcnt = "0";
			this.doc_or1=rs.getString("add_1_original");				//결재문서 첨부파일명1 (원래파일명)
			this.doc_ad1=rs.getString("add_1_file");					//결재문서 첨부파일명1 (저장되는 파일명)

			this.doc_or2=rs.getString("add_2_original");				//결재문서 첨부파일명2 (원래파일명)
			this.doc_ad2=rs.getString("add_2_file");					//결재문서 첨부파일명2 (저장되는 파일명)

			this.doc_or3=rs.getString("add_3_original");				//결재문서 첨부파일명3 (원래파일명)
			this.doc_ad3=rs.getString("add_3_file");					//결재문서 첨부파일명3 (저장되는 파일명)

			this.doc_per=rs.getString("save_period");					//결재문서 보존기간
			this.doc_del=rs.getString("delete_date");					//결재문서 삭제일자 (승인후 계산)
			this.doc_sec=rs.getString("security_level");				//결재문서 보안등급

			this.doc_lid=rs.getString("plid");							//외부문서 관리번호
			this.doc_flag=rs.getString("flag");							//외부문서 종류

			rtn = true;
		}

		stmt.close();
		rs.close();

		return rtn;
	}	
	
	/******************************************************************************
		결재순서  파악하기 (내용:LINE_ORD[], 수량:lin_str_cnt+1)
	******************************************************************************/
	public String[] eleDecisionLineOrder() throws SQLException 
	{
		//결재순서 알아보기 (결재라인을 이용)
		int lin_str_len = this.doc_lin.length();			//문자열 길이
		this.lin_str_cnt = 0;								//결재순서 구분자 찾기
		for(int a = 0 ; a < lin_str_len; a++) {
			String l_str = "" + doc_lin.charAt(a);
			if(l_str.equals(",")) this.lin_str_cnt++;		//총결재 단계 횟수파악 
		}

		//결재순서 배열에 담기 
		if(this.doc_lin != null) {
			LINE_ORD = new String[lin_str_cnt+1];			
			String ord_data = this.doc_lin;
			for(int aa = 0; aa <= lin_str_len; aa++) {
				int sec_aa = ord_data.indexOf(",");
				if(sec_aa == -1) {
					LINE_ORD[aa] = ord_data;
					break;
				}
				else {
					LINE_ORD[aa] = ord_data.substring(0,sec_aa);
				} //if
				ord_data = ord_data.substring(sec_aa+1,ord_data.length());
			} //for
		} //if
		return LINE_ORD;			
	}

	/******************************************************************************
		통보인원 파악하기 (내용:receiver[], 수량:rec_str_cnt+1)
	******************************************************************************/
	public int eleDecisionReceiver() throws SQLException 
	{	
		//통보자 인원수 파악
		int rec_str_len = doc_rec.length();			//문자열 길이
		this.rec_str_cnt = 0;						//통보자 수 (부서도 하나로 간주)
		for(int b = 0 ; b < rec_str_len; b++) {
			String r_str = "" + doc_rec.charAt(b);
			if(r_str.equals(",")) this.rec_str_cnt++;
			if(b == rec_str_len-1) this.rec_str_cnt++;//마지막 콤마없어 보상 
		}

		//통보자명단 배열에 담기
		if(doc_rec != null) {
			receiver = new String[rec_str_cnt];			
			String red_data = doc_rec;
			for(int bb = 0; bb < rec_str_len; bb++) {
				int sec_bb = red_data.indexOf(",");
				if(sec_bb == -1) {
					//out.println("org line data : " + ord_data + "<br>");
					receiver[bb] = red_data;
					break;
				}
				else {
					receiver[bb] = red_data.substring(0,sec_bb);
				} //if
				red_data = red_data.substring(sec_bb+1,red_data.length());
			} //for
			//out.println("org receiver data : " + doc_rec + "<br>");
//			for(int bbb = 0; bbb < rec_str_cnt; bbb++)
				//System.out.println("receiver_data :" + receiver[bbb] + "<br>");
		} //if
		return rec_str_cnt;
	}
		
	/******************************************************************************
		결재완료된 문서 입고시키기 
		(APP_MASTER:APS, APP_SAVE:입고) 
	******************************************************************************/
	public String eleDecisionSave(String status, String comment) throws SQLException 
	{
		String STE = status;						//상태 
		
		//1. 상태에 따라 의견및 일자 구하기  
		if(STE.equals("APV")) {						//검토
			this.doc_rem += comment;		this.doc_red = anbdt.getTime();	}
		if(STE.equals("APG")) {						//합의 
			this.doc_agm += comment;		this.doc_agd = anbdt.getTime();	}
		if(STE.equals("APG2")) {					//합의2 
			this.doc_agm2 += comment;	this.doc_agd2 = anbdt.getTime();	}
		if(STE.equals("APG3")) {					//합의3 
			this.doc_agm3 += comment;	this.doc_agd3 = anbdt.getTime();	}
		if(STE.equals("APG4")) {					//합의4 
			this.doc_agm4 += comment;	this.doc_agd4 = anbdt.getTime();	}
		if(STE.equals("APG5")) {					//합의5 
			this.doc_agm5 += comment;	this.doc_agd5 = anbdt.getTime();	}
		if(STE.equals("APG6")) {					//합의6 
			this.doc_agm6 += comment;	this.doc_agd6 = anbdt.getTime();	}						
		if(STE.equals("APG7")) {					//합의7 
			this.doc_agm7 += comment;	this.doc_agd7 = anbdt.getTime();	}
		if(STE.equals("APG8")) {					//합의8 
			this.doc_agm8 += comment;	this.doc_agd8 = anbdt.getTime();	}														
		if(STE.equals("APG9")) {					//합의9 
			this.doc_agm9 += comment;	this.doc_agd9 = anbdt.getTime();	}
		if(STE.equals("APG10")) {					//합의10 
			this.doc_agm10 += comment;	this.doc_agd10 = anbdt.getTime();}
		if(STE.equals("APL")) {						//승인  
			this.doc_dem += comment;		this.doc_ded = anbdt.getTime();	}																	
																
		//2. 삭제일자 구하기
		String doc_del_date="";
		if(this.doc_per.equals("0"))	doc_del_date = anbdt.getAddMonthNoformat(1);		//처리후 폐기 (1개월)
		else if(this.doc_per.equals("1")) doc_del_date = anbdt.getAddYearNoformat(1);		//1년
		else if(this.doc_per.equals("2")) doc_del_date = anbdt.getAddYearNoformat(2);		//2년
		else if(this.doc_per.equals("3")) doc_del_date = anbdt.getAddYearNoformat(3);		//3년
		else if(this.doc_per.equals("5")) doc_del_date = anbdt.getAddYearNoformat(5);		//5년
		else if(this.doc_per.equals("EVER")) doc_del_date = anbdt.getAddYearNoformat(100);	//영구 (100년)

		//3. 마스터 DB을 업데이트 시킴 : 입고 시킴
		String apv_data_ste ="";
		String apv_data_red ="";
		String apv_data_com =""; 
		String apv_data_del ="";
			
		apv_data_ste = "update APP_MASTER set app_state='APS' where pid='" + this.doc_pid + "'";  //상태
		if(STE.equals("APV")) {
			this.doc_red=anbdt.getTime();		//결재문서 검토일자
			apv_data_red = "update APP_MASTER set review_date='" + this.doc_red +"' where pid='" + this.doc_pid + "'";  //검토일자
			apv_data_com = "update APP_MASTER set review_comment='" + this.doc_rem + "' where pid='" + this.doc_pid + "'";  //검토의견
		} else if(STE.equals("APG")) {
			this.doc_agd=anbdt.getTime();		//결재문서 합의일자
			apv_data_red = "update APP_MASTER set agree_date='" + this.doc_agd +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree_comment='" + this.doc_agm + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APG2")) {
			this.doc_agd2=anbdt.getTime();		//결재문서 합의일자2
			apv_data_red = "update APP_MASTER set agree2_date='" + this.doc_agd2 +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree2_comment='" + this.doc_agm2 + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APG3")) {
			this.doc_agd3=anbdt.getTime();		//결재문서 합의일자3
			apv_data_red = "update APP_MASTER set agree3_date='" + this.doc_agd3 +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree3_comment='" + this.doc_agm3 + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APG4")) {
			this.doc_agd4=anbdt.getTime();		//결재문서 합의일자4
			apv_data_red = "update APP_MASTER set agree4_date='" + this.doc_agd4 +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree4_comment='" + this.doc_agm4 + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APG5")) {
			this.doc_agd5=anbdt.getTime();		//결재문서 합의일자5
			apv_data_red = "update APP_MASTER set agree5_date='" + this.doc_agd5 +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree5_comment='" + this.doc_agm5 + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APG6")) {
			this.doc_agd6=anbdt.getTime();		//결재문서 합의일자6
			apv_data_red = "update APP_MASTER set agree6_date='" + this.doc_agd6 +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree6_comment='" + this.doc_agm6 + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APG7")) {
			this.doc_agd7=anbdt.getTime();		//결재문서 합의일자7
			apv_data_red = "update APP_MASTER set agree7_date='" + this.doc_agd7 +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree7_comment='" + this.doc_agm7 + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APG8")) {
			this.doc_agd8=anbdt.getTime();		//결재문서 합의일자8
			apv_data_red = "update APP_MASTER set agree8_date='" + this.doc_agd8 +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree8_comment='" + this.doc_agm8 + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APG9")) {
			this.doc_agd9=anbdt.getTime();		//결재문서 합의일자9
			apv_data_red = "update APP_MASTER set agree9_date='" + this.doc_agd9 +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree9_comment='" + this.doc_agm9 + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APG10")) {
			this.doc_agd10=anbdt.getTime();		//결재문서 합의일자10
			apv_data_red = "update APP_MASTER set agree10_date='" + this.doc_agd10 +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set agree10_comment='" + this.doc_agm10 + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else if(STE.equals("APL")) {
			this.doc_ded=anbdt.getTime();		//결재문서 승인일자
			apv_data_red = "update APP_MASTER set decision_date='" + this.doc_ded +"' where pid='" + this.doc_pid + "'";  //합의 일자
			apv_data_com = "update APP_MASTER set decision_comment='" + this.doc_dem + "' where pid='" + this.doc_pid + "'";  //합의 의견
		} else {
			apv_data_red ="";
			apv_data_com ="";
		} //if 	
		apv_data_del = "update APP_MASTER set delete_date='" + doc_del_date + "' where pid='" + this.doc_pid + "'";  //삭제일자
					
		//4.입고 DB에 신규로 데이터 입력시킴[전부]
		String apv_data_in = "";
		if(STE.equals("APL")) {				//승인시 app_save에 입고시킴
			apv_data_in = "INSERT INTO APP_SAVE(pid,app_subj,writer,writer_name,write_date,app_state,reviewer,review_date,review_comment,";
			apv_data_in += "agree,agree_date,agree_comment,agree2,agree2_date,agree2_comment,agree3,agree3_date,agree3_comment,";
			apv_data_in += "agree4,agree4_date,agree4_comment,agree5,agree5_date,agree5_comment,agree6,agree6_date,agree6_comment,";
			apv_data_in += "agree7,agree7_date,agree7_comment,agree8,agree8_date,agree8_comment,agree9,agree9_date,agree9_comment,";
			apv_data_in += "agree10,agree10_date,agree10_comment,agree_method,agree_count,agree_pass,";
			apv_data_in += "decision,decision_date,decision_comment,receivers,app_line,bon_path,";
			apv_data_in += "bon_file,add_counter,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,";
			apv_data_in += "save_period,delete_date,security_level,plid,flag) values('";
			apv_data_in += doc_pid + "','" + doc_sub + "','" + doc_wri + "','" + doc_wna + "','" + doc_wrd + "','" + "APS" + "','";
			apv_data_in += doc_rev + "','" + doc_red + "','" + doc_rem + "','" + doc_agr + "','" + doc_agd + "','" + doc_agm + "','";
			apv_data_in += doc_agr2 + "','" + doc_agd2 + "','" + doc_agm2 + "','" + doc_agr3 + "','" + doc_agd3 + "','" + doc_agm3 + "','"; 
			apv_data_in += doc_agr4 + "','" + doc_agd4 + "','" + doc_agm4 + "','" + doc_agr5 + "','" + doc_agd5 + "','" + doc_agm5 + "','";
			apv_data_in += doc_agr6 + "','" + doc_agd6 + "','" + doc_agm6 + "','" + doc_agr7 + "','" + doc_agd7 + "','" + doc_agm7 + "','";
			apv_data_in += doc_agr8 + "','" + doc_agd8 + "','" + doc_agm8 + "','" + doc_agr9 + "','" + doc_agd9 + "','" + doc_agm9 + "','";
			apv_data_in += doc_agr10 + "','" + doc_agd10 + "','" + doc_agm10 + "','" + agr_met + "','" + Integer.toString(agr_cnt) + "','" + agr_pas + "','";
			apv_data_in += doc_dec + "','" + doc_ded + "','" + doc_dem + "','" + doc_rec + "','" + doc_lin + "','" + doc_pat + "','";
			apv_data_in += doc_bon + "','" + doc_atcnt + "','" + doc_or1 + "','" + doc_ad1 + "','" + doc_or2 + "','" + doc_ad2 + "','" + doc_or3 + "','" + doc_ad3 + "','";
			apv_data_in += doc_per + "','" + doc_del_date + "','" + doc_sec + "','"+ doc_lid + "','" + doc_flag+"')";
		}

		//5. DB에 저장 실행문
		Message="";
		Statement stmt = null;
		stmt = con.createStatement();
		stmt.executeUpdate(apv_data_ste);		////System.out.println("apv_data_ste : " + apv_data_ste);
		stmt.executeUpdate(apv_data_red);		////System.out.println("apv_data_red : " + apv_data_red);
		stmt.executeUpdate(apv_data_com);		////System.out.println("apv_data_com : " + apv_data_com);
		stmt.executeUpdate(apv_data_del);		////System.out.println("apv_data_del : " + apv_data_del);
		stmt.executeUpdate(apv_data_in);		////System.out.println("apv_data_in : " + apv_data_in);
		stmt.close();

		if(STE.equals("APL")) {		//승인시 
			eleInformSave();		//이름,부서명,직급 update
		}

		Message="APPROVAL"; 
	
		return Message;
	}

	/******************************************************************************
		결재완료된 문서 작성자부서코드,이름,부서명,직급 update 하기 
	*******************************************************************************/
	private void eleInformSave() throws SQLException 
	{
		String[] id_name = {doc_wri,doc_rev,doc_dec,doc_agr,doc_agr2,doc_agr3,doc_agr4,doc_agr5,doc_agr6,doc_agr7,doc_agr8,doc_agr9,doc_agr10};
		String[] Aname = {"writer","reviewer","decision","agree","agree2","agree3","agree4","agree5","agree6","agree7","agree8","agree9","agree10"};
		String query = "";
		String name = "";		//이름 
		String div = "";		//부서명 
		String rank = "";		//직급 
		
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "select distinct a.id,a.name,b.ac_name,c.ar_name from ";
		query += "user_table a,class_table b,rank_table c ";

		for(int i=0; i<id_name.length; i++) {
			if(id_name[i].length() != 0) {
				String query1 = query + "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.id='"+id_name[i]+"'";
				rs = stmt.executeQuery(query1);
				while(rs.next()) {
					name = rs.getString("name");
					div = rs.getString("ac_name");
					rank = rs.getString("ar_name");	
				}
				String query2 = "update APP_SAVE set "+Aname[i]+"_name='"+name+"',"+Aname[i]+"_div='"+div+"',"+Aname[i]+"_rank='"+rank+"' where pid='"+this.doc_pid+"'";
				stmt.executeUpdate(query2);
			}
		}				
		stmt.close();
		rs.close();
	}

	/******************************************************************************
		결재완료된 문서중 통보자가 있을경우 처리하기 
		(APP_RECEIVE:통보) 
	******************************************************************************/
	public void eleDecisionInform(String plid,String flag) throws SQLException 
	{
		//전달변수 
		String lid = plid;			//기타문서 전자결재 상신시 관리번호 
		String doc_flag = flag;		//문서의 종류 (SERVICE:고객관리, BOM:부품리스트 등)
		String query = "";

		Statement stmt = null;
		ResultSet rs = null;
		
		//1. 통보자 인원 파악하기 
		rec_str_cnt = eleDecisionReceiver();
//		//System.out.println("통보자 수 : " + rec_str_cnt);	

		//2. 삭제일자 구하기
		String doc_del_date="";
		if(this.doc_per.equals("0"))	doc_del_date = anbdt.getAddMonthNoformat(1);		//처리후 폐기 (1개월)
		else if(this.doc_per.equals("1")) doc_del_date = anbdt.getAddYearNoformat(1);		//1년
		else if(this.doc_per.equals("2")) doc_del_date = anbdt.getAddYearNoformat(2);		//2년
		else if(this.doc_per.equals("3")) doc_del_date = anbdt.getAddYearNoformat(3);		//3년
		else if(this.doc_per.equals("5")) doc_del_date = anbdt.getAddYearNoformat(5);		//5년
		else if(this.doc_per.equals("EVER")) doc_del_date = anbdt.getAddYearNoformat(100);	//영구 (100년)

		
		//3.통보DB (app_reveive)에 데이터 저장하기
		for(int rec_on = 0 ; rec_on < rec_str_cnt; rec_on++){
//			//System.out.println("통보자 명단 : " + receiver[rec_on]);
			if(checkSabun(receiver[rec_on])) {				//개인별로 통보한 경우임
				stmt = con.createStatement();
				query = "select distinct a.id,a.name,b.ac_name,c.ar_name from user_table a, class_table b, rank_table c ";
				query += "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.id='"+receiver[rec_on]+"'";
//				//System.out.println("개인 query :" + query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					String receive_data = "";
					receive_data = "INSERT INTO APP_RECEIVE(pid,app_subj,writer,writer_name,write_date,add_counter,isOpen,receiver,delete_date,plid,send_bom,request_date,flag,receiver_name,receiver_div,receiver_rank) values('";
					receive_data += doc_pid + "','" + doc_sub + "','" + doc_wri + "','" + doc_wna + "','" + doc_wrd + "','" + doc_atcnt + "','" + "0" + "','";
					receive_data += receiver[rec_on]+"','"+doc_del_date+"','"+lid+"','"+"0"+"','" + anbdt.getTime() + "','" + doc_flag + "','"+rs.getString("name")+"','"+rs.getString("ac_name")+"','"+rs.getString("ar_name")+"')";
//					//System.out.println("사번:" + receive_data);
					// DB에 저장 실행문
					stmt.executeUpdate(receive_data);	
				} //while
				stmt.close();
				rs.close();
			} 
			else  {		//부서별임 따라서 개인별 사번을 찾아 통보한다.
				//최상위 부서인 경우 하위부서원 전부에게 통보한다.
				//1. 하위부서 모두 찾기
				searchDiv(receiver[rec_on]);		//하위부서 찾기 [배열 sdiv ]에 임시로 담는다. private선언
				int div_cnt = sdiv.length;			//하위부서 갯수

				//2. 부서원을 찾아 개별 통보하기
				for(int n=0; n<div_cnt; n++) {
					stmt = con.createStatement();	
					query = "select distinct a.id,a.name,b.ac_name,c.ar_name from user_table a, class_table b, rank_table c ";
					query += "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.ac_id='"+sdiv[n]+"'";
//					//System.out.println("부서 query :" + query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						String receive_data = "";
						
						//같은부서일 경우 기안,검토,승인,합의자 제외
						String sabun = rs.getString("id");			//사번
						String name = rs.getString("name");			//이름
						String ac_name = rs.getString("ac_name");	//부서명
						String ar_name = rs.getString("ar_name");	//직급명
						sendInform(sabun,name,ac_name,ar_name,lid,doc_del_date);
					} //while
					stmt.close();
					rs.close();
				} //for
			} //if
		} //for	
	}

	/******************************************************************************
		결재완료된 문서중 통보자 추가 처리하기 
		(APP_RECEIVE:통보) 
	******************************************************************************/
	public void addInformReceiver(String pid,String receivers) throws SQLException 
	{
		//전달변수 
		String query = "",sabun="",input="",lid="",flag="",exist_id="",where="",update="";
		String recs="",app_line="";		//마스터 테이블의 통보자 및 결재순서라인
		String[] data = new String[3]; 

		//선행실행
		eleDecisionReadDoc(pid);	//상세내용 읽기
		eleDecisionLineOrder();		//결재선 해석
		lid=doc_lid;flag=doc_flag;

		Statement stmt = null;
		stmt = con.createStatement();
		
		//삭제일자 구하기
		String doc_del_date="";
		if(this.doc_per.equals("0"))	doc_del_date = anbdt.getAddMonthNoformat(1);		//처리후 폐기 (1개월)
		else if(this.doc_per.equals("1")) doc_del_date = anbdt.getAddYearNoformat(1);		//1년
		else if(this.doc_per.equals("2")) doc_del_date = anbdt.getAddYearNoformat(2);		//2년
		else if(this.doc_per.equals("3")) doc_del_date = anbdt.getAddYearNoformat(3);		//3년
		else if(this.doc_per.equals("5")) doc_del_date = anbdt.getAddYearNoformat(5);		//5년
		else if(this.doc_per.equals("EVER")) doc_del_date = anbdt.getAddYearNoformat(100);	//영구 (100년)

		StringTokenizer rlist = new StringTokenizer(receivers,"\n");
		while(rlist.hasMoreTokens()) {
			String id_name = rlist.nextToken();
			StringTokenizer receiver = new StringTokenizer(id_name,"/");
			data[0]=data[1]=data[2]="";
			while(receiver.hasMoreTokens()) {
				sabun = receiver.nextToken();
				data = getRankInfo(sabun);			//성명,부서명,직급명 구하기

				//이미 전송한 통보자인지 판단
				where = "where pid='"+pid+"' and receiver='"+sabun+"'";
				exist_id=getColumnData("app_receive","receiver",where);

				//해당사번의 이름이 있고, 미전송된 통보자 이면 전송한다.
				if(data[0].length() != 0 && exist_id.length() == 0) {
					//통보자 추가
					input = "INSERT INTO APP_RECEIVE(pid,app_subj,writer,writer_name,write_date,add_counter,isOpen,receiver,delete_date,plid,send_bom,request_date,flag,receiver_name,receiver_div,receiver_rank) values('";
					input += doc_pid + "','" + doc_sub + "','" + doc_wri + "','" + doc_wna + "','" + doc_wrd + "','" + doc_atcnt + "','" + "0" + "','";
					input += sabun+"','"+doc_del_date+"','"+lid+"','"+"0"+"','" + anbdt.getTime() + "','" + doc_flag + "','"+data[0]+"','"+data[1]+"','"+data[2]+"')";
					////System.out.println("input :" + input);
					stmt.executeUpdate(input);

					//통보자 마스터에 update
					//1.결재라인에 통보가 포함되었나 확인
					where = "where pid='"+pid+"'";
					app_line=getColumnData("app_master","app_line",where);

					//2.통보자 추가하기
					where = "where pid='"+pid+"'";
					recs=getColumnData("app_master","receivers",where);
					if(app_line.indexOf("API") == -1) { 
						recs = sabun; 
						app_line += ",API"; 
					} else recs += ","+sabun;

					//3.마스터에 update하기
					update = "UPDATE app_master SET receivers='"+recs+"',app_line='"+app_line+"' ";
					update += "where pid='"+pid+"'";
					////System.out.println("update :" + update);
					stmt.executeUpdate(update);
					
				}
			} //while
		} //while

		//닫기
		stmt.close();
	}

	/******************************************************************************
		사번으로 이름,부서명,직급 찾기
	******************************************************************************/
	public String[] getRankInfo(String sabun) throws SQLException 
	{
		//전달변수 
		String query = "";
		String[] data = new String[3]; 
		data[0]=data[1]=data[2]="";

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		query = "select distinct a.id,a.name,b.ac_name,c.ar_name from user_table a, class_table b, rank_table c ";
		query += "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.id='"+sabun+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("name");			//이름
			data[1] = rs.getString("ac_name");		//부서 이름
			data[2] = rs.getString("ar_name");		//직급 이름
		}
////System.out.println("data :" + data[0]+" : "+data[1]+" : "+data[2]);
		//닫기
		stmt.close();
		rs.close();

		return data;
	}

	/******************************************************************************
		결재완료된 문서중 통보자가 있을경우 코드의 종류 판단하기 
		통보자 코드가 사번인지, 부서관리코드인지 판단하기
	******************************************************************************/
	private boolean checkSabun(String id) throws SQLException 
	{
		boolean rtn = true;			//사번임

		//1. 숫자로 구성되었나 판단하기 [num:숫자, Nnum:알파벳포함 (부서관리코드로 ac_id : tinyint형임)]
		String tag = "num";	
		try{ Integer.parseInt(id); } catch(Exception e) {tag = "Nnum"; }
//		//System.out.println("tag : " + tag);

		//2. 숫자로 구성된 경우 부서관리코드 인지를 판단한다.
		String div = "Ndiv";		//부서인지 판단 div:부서, Ndiv:개인
		if(tag.equals("num")) {
			Statement stmt = null;
			ResultSet rs = null;
			stmt = con.createStatement();
			String query = "select distinct ac_id from class_table where ac_id='"+id+"'";
			rs = stmt.executeQuery(query);
			if(rs.next()) div = "div";
			else div = "Ndiv";
			stmt.close();
			rs.close();
		}
//		//System.out.println("div : " + div);

		//3. 사번인지 판단하기 [사번:true, 부서관리코드:false]
		if(div.equals("div")) rtn = false;
		else rtn = true;
		return rtn;
		
	}

	/******************************************************************************
		부서통보자를 개별로 구분하여 통보처리 하기
	******************************************************************************/
	private void sendInform(String sabun,String name,String ac_name,String ar_name,String lid,String deldate) throws SQLException 
	{
		String receive_data = "";

		Statement stmt = null;
		stmt = con.createStatement();

		if(!doc_wri.equals(sabun) && !doc_rev.equals(sabun) && !doc_agr.equals(sabun) && 
				!doc_agr2.equals(sabun) && !doc_agr3.equals(sabun) && !doc_agr4.equals(sabun) && 
				!doc_agr5.equals(sabun) && !doc_agr6.equals(sabun) && !doc_agr7.equals(sabun) &&
				!doc_agr8.equals(sabun) && !doc_agr9.equals(sabun) && !doc_agr10.equals(sabun) &&
				!doc_dec.equals(sabun)) {
			//통보부서자 명단에 개별통보자가 포함되면 skip
			int dind = doc_rec.indexOf(sabun);
			if(dind == -1) {
				receive_data = "INSERT INTO APP_RECEIVE(pid,app_subj,writer,writer_name,write_date,add_counter,isOpen,receiver,delete_date,plid,send_bom,request_date,flag,receiver_name,receiver_div,receiver_rank) values('";
				receive_data += doc_pid + "','" + doc_sub + "','" + doc_wri + "','" + doc_wna+"','"+doc_wrd + "','" + doc_atcnt + "','" + "0" + "','";
				receive_data += sabun + "','" + deldate + "','" + lid + "','" + "0" + "','" + anbdt.getTime() + "','" + doc_flag + "','"+name+"','"+ac_name+"','"+ar_name+"')";
				//System.out.println("부서:" + receive_data);
				// DB에 저장 실행문
				stmt.executeUpdate(receive_data);
			}
		}				
		stmt.close();
	}

	/******************************************************************************
		통보자가 부서인 경우 하위부서 모두 찾기
	******************************************************************************/
	private void searchDiv(String div) throws SQLException 
	{
		String query = "";
		String code = "";			//code
		int div_cnt = 0;

		Statement stmt = null;
		ResultSet rs = null;

		//1.주어진 부서관리코드로 해당부서의 코드찾기
		stmt = con.createStatement();
		query = "select distinct code from class_table where ac_id='"+div+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) code = rs.getString("code");
		stmt.close();
		rs.close();

		//2.찾은 code가 포함된 모든 부서관리코드의 갯수를 파악한다.
		stmt = con.createStatement();
		query = "select count(*) from class_table where code like '"+code+"%'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			div_cnt = rs.getInt(1);
		}
		stmt.close();
		rs.close();

		//배열선언
		sdiv = new String[div_cnt];
		for(int i=0; i<div_cnt; i++) sdiv[i] = "";

		//3.찾은 code가 포함된 모든 부서관리코드 찾기
		stmt = con.createStatement();
		query = "select distinct ac_id from class_table where code like '"+code+"%'";
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) {
			sdiv[n] = rs.getString("ac_id");
			n++;
		}
		stmt.close();
		rs.close();

	}

	/******************************************************************************
		결재 문서중 합의자가 있을경우 처리하기 (순차적 합의일경우만 사용) 
		(status :상태, docid :관리번호, comment:의견) 
	******************************************************************************/
	public void eleSerialAgree(String status,String docid,String comment) throws SQLException 
	{
		Statement stmt = null;
		stmt = con.createStatement();
		
		for(int ap = 0; ap <= this.lin_str_cnt; ap++) {		//결재라인 배열을 읽는다.
			if(LINE_ORD[ap].equals(status)) {				//같은상태만 다음단계로 처리
				//결재절차 진행 (합의단계에서 결재문서가 끝날때 문서입고)
				if(ap == this.lin_str_cnt) {	
					eleDecisionSave(status,comment); 
				}  
				else {	//결재라인의 다음단계로 간다 (마스터 DB을 업데이트 시킴 [상태,합의일자,합의의견])
					String doc_agd=anbdt.getTime();		//결재문서 승인일자
					String query = "update APP_MASTER set app_state='" + LINE_ORD[ap+1] +"', ";			
					if(status.equals("APG2"))
						   query += "agree2_date='" + doc_agd +"', agree2_comment='" + this.doc_agm2+comment + "' where pid='" + docid + "'";	
					else if(status.equals("APG3"))
						   query += "agree3_date='" + doc_agd +"', agree3_comment='" + this.doc_agm3+comment + "' where pid='" + docid + "'";	
					else if(status.equals("APG4"))
						   query += "agree4_date='" + doc_agd +"', agree4_comment='" + this.doc_agm4+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG5"))
						   query += "agree5_date='" + doc_agd +"', agree5_comment='" + this.doc_agm5+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG6"))
						   query += "agree6_date='" + doc_agd +"', agree6_comment='" + this.doc_agm6+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG7"))
						   query += "agree7_date='" + doc_agd +"', agree7_comment='" + this.doc_agm7+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG8"))
						   query += "agree8_date='" + doc_agd +"', agree8_comment='" + this.doc_agm8+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG9"))
						   query += "agree9_date='" + doc_agd +"', agree9_comment='" + this.doc_agm9+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG10"))
						   query += "agree10_date='" + doc_agd +"', agree10_comment='" + this.doc_agm10+comment + "' where pid='" + docid + "'";
					// DB에 저장 실행문
					stmt.executeUpdate(query);
				} //if		
			} //if
		} //for	
		stmt.close();	
	}
	
	/******************************************************************************
		통보자 인원수 리턴하기 
		리턴 : 통보자 인원
	******************************************************************************/
	public int getInformCount() throws SQLException 
	{
		return this.rec_str_cnt;
	}
		
	/******************************************************************************
		결재선 결재수 리턴하기 
		리턴 : 결재선수 
	******************************************************************************/
	public int getLineCount() throws SQLException 
	{
		return this.lin_str_cnt;
	}

	/******************************************************************************
		검토단계 데이터 처리하기
	******************************************************************************/
	public void processAPV(String pid,String comment) throws SQLException 
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//선행실행
		eleDecisionReadDoc(pid);	//상세내용 읽기
		eleDecisionLineOrder();		//결재선 해석

		//데이터 처리하기
		for(int ap = 0; ap <= lin_str_cnt; ap++) {		//결재라인 배열을 읽는다.
			if(LINE_ORD[ap].equals("APV")) {			//같은상태만 다음단계로 처리
				//결재절차 진행
				if(ap == lin_str_cnt) {					//검토단계에서 결재문서가 끝날때 문서입고
					eleDecisionSave("APV",comment);
				}
				else {					//결재라인의 다음단계로 간다.
					//마스터 DB을 업데이트 시킴 [상태,검토일자,검토의견]
					String query = "update APP_MASTER set app_state='" + LINE_ORD[ap+1] +"', ";	//상태
						  query += "review_date='" + anbdt.getTime() +"', ";						//검토일자
						  query += "review_comment='" + this.doc_rem+comment + "' where pid='" + pid + "'";  //검토의견
					
					// DB에 저장 실행문
					stmt.executeUpdate(query);
				} //if		
			} //if
		} //for
		stmt.close();
	}

	/******************************************************************************
		승인단계 데이터 처리하기
	******************************************************************************/
	public void processAPL(String pid,String comment) throws Exception 
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		//선행실행
		eleDecisionReadDoc(pid);	//상세내용 읽기
		eleDecisionLineOrder();		//결재선 해석

		//데이터 처리하기
		for(int ap = 0; ap <= lin_str_cnt; ap++) {		//결재라인 배열을 읽는다.
			if(LINE_ORD[ap].equals("APL")) {			//같은상태만 다음단계로 처리
				//------------------------------
				//결재승인임으로 무조건 입고 시킨다.
				//------------------------------
				eleDecisionSave("APL",comment);
	
				//-------------------------------
				//기타문서 전자결재상신시 전자결재 완료(EF)임을 알려주기(각문서별 상이함)
				//-------------------------------
				if(this.doc_flag.equals("SERVICE")) {		//고객관리 서비스 결재상신
					StringTokenizer strid = new StringTokenizer(this.doc_lid,",");//콤마로 구분된 관련문서
					while(strid.hasMoreTokens()) {
						String EE = "update HISTORY_TABLE set flag='EF' where ah_id='"+strid.nextToken()+"'";
						stmt.executeUpdate(EE);
					} //while
				}
				//기술 문서인경우 
				else if(doc_flag.equals("TD")) {	
					String[] data = new String[3]; 
					if(doc_lid.length() != 0) {
						StringTokenizer num = new StringTokenizer(doc_lid,"|");
						int sn = 0;
						while(num.hasMoreTokens()) {
							data[sn] = num.nextToken();				//관리번호,table name,version
							sn++;
						}
					}
					mdBO.approvalTD(pid,data[0],data[1],data[2]);	//전자결재관리코드,기술문서관리코드,tablename,version
				}
				//승인원 문서인경우 
				else if(doc_flag.equals("AKG")) {	
					mdBO.approvalCA(pid,doc_lid);		//전자결재관리코드,승인원관리코드
				}
				//일반보고 문서인경우 
				else if(doc_flag.equals("GEN")) {
					setAcCode();				 					//부서코드 입력하기 (app_save)
					numberingDoc("APP_SAVE",doc_flag,"pid",doc_pid);	//문서번호 입력
				}
				//견적 문서인 경우
				else if(doc_flag.equals("EST")) {
					String estimate_no="";			//견적번호
					String ver = "";				//견적버젼
					int bar = doc_lid.indexOf("|");
					estimate_no = doc_lid.substring(0,bar);
					ver = doc_lid.substring(bar+1,doc_lid.length());

					com.anbtech.em.db.EstimateDAO estDAO = new com.anbtech.em.db.EstimateDAO(con);
					String real_est_no = estDAO.calculateEstimateNo();

					com.anbtech.em.business.EstimateBO estBO = new com.anbtech.em.business.EstimateBO(con);
					estBO.makeCommitStat(estimate_no,ver,doc_pid,real_est_no);

					//견적관리번호 정식번호로 입력하기
					String n_plid = real_est_no+"|"+ver;
					updateColumn("APP_MASTER","plid",n_plid,pid);		//마스터함
					updateColumn("APP_SAVE","plid",n_plid,pid);		//저장함

				}
				//특근관리 문서인경우 
				else if(doc_flag.equals("EWK")) {
					com.anbtech.ew.business.ExtraWorkModuleBO ewkBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);//특근신청
					ewkBO.ewAppInfoProcess(doc_pid,doc_lid,"approval");
				}
				//BOM관리 문서인경우 
				else if(doc_flag.equals("BOM")) {
					com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);//BOM승인신청
					String where = "where id='"+doc_dec+"'";
					String app_name = getColumnData("user_table","name",where);
					bomDAO.setBomStatus(doc_lid,"5",doc_dec,app_name,doc_pid);
				}
				//설계변경관리 문서인경우 
				else if(doc_flag.equals("DCM")) {
					com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);//설계변경승인신청
					int sep_no = doc_lid.indexOf("|");
					int len_no = doc_lid.length();
					String eco_pid = doc_lid.substring(0,sep_no);
					String eco_no = doc_lid.substring(sep_no+1,len_no);
					bomDAO.setEccStatus(eco_pid,doc_pid,"app");
				}
				//구매요청관리 문서인경우 
				else if(doc_flag.equals("PCR")) {
					com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);//MRP 
					com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);//MRP
					com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//구매관리
					int sep_no = doc_lid.indexOf("|");
					int len_no = doc_lid.length();
					String req_pid = doc_lid.substring(0,sep_no);			//구매관리번호
					String req_type = doc_lid.substring(sep_no+1,len_no);	//구매TYPE (MRP : 생산관리 MRP구매요청)
					//생산관리 MRP구매요청인 경우만 진행
					if(req_type.equals("MRP")) {
						String where = "where pu_req_no = '"+req_pid+"'";
						String mrp_pid = mrpDAO.getColumData("MRP_MASTER","pid",where);
						mrpBO.setMrpStatus(mrp_pid,"31","3",""); 
					}
					purBO.puRequestAppInfoProcess("commit_req",doc_pid,req_pid);
				}
				//발주요청관리 문서인경우 
				else if(doc_flag.equals("ODR")) {
					com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//구매관리
					purBO.puOrderAppInfoProcess("commit_order",doc_pid,doc_lid);
				}
				//구매입고관리 문서인경우 
				else if(doc_flag.equals("PWH")) {
					com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//구매관리
					purBO.puEnterAppInfoProcess("commit_enter",doc_pid,doc_lid);
				}
				//부품출고관리 문서인경우 
				else if(doc_flag.equals("TGW")) {
					com.anbtech.st.business.StockMgrBO stcBO = new com.anbtech.st.business.StockMgrBO(con);//재고관리
					stcBO.DeliveryAppInfoProcess("commit_delivery",doc_lid,doc_lid);
				}
				//전자결재 양식인 경우
				else {
					formPaperUp(doc_flag,doc_lid);
				}

				//-------------------------------
				//승인 다음 --> 통보로 넘어갈때 통보DB (app_reveive)에 데이터 저장하기
				//-------------------------------
				if((ap < lin_str_cnt) && (LINE_ORD[ap+1].equals("API"))) {		
					eleDecisionInform(this.doc_lid,this.doc_flag);	//통보자 테이블에 담기 (기타문서관리번호 전달)
				} //if	
			} //if
		} //for
		stmt.close();
	}

	/******************************************************************************
		협조단계 데이터 처리하기
	******************************************************************************/
	public void processAPG(String pid,String comment,String login_owner) throws SQLException 
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		//선행실행
		eleDecisionReadDoc(pid);	//상세내용 읽기
		eleDecisionLineOrder();		//결재선 해석

		//데이터 처리하기
		for(int ap = 0; ap <= lin_str_cnt; ap++) {		//결재라인 배열을 읽는다.
			if(LINE_ORD[ap].equals("APG")) {		//같은상태만 다음단계로 처리
				//결재절차 진행 (합의단계에서 결재문서가 끝날때 문서입고)
				if(ap == lin_str_cnt) {	eleDecisionSave("APG",comment); }
				else {	//결재라인의 다음단계로 간다 (마스터 DB을 업데이트 시킴 [상태,합의일자,합의의견])
					String doc_agd=anbdt.getTime();		//결재문서 합의일자

					//합의단계 순차적 진행 (합의단계 반영)
					if(this.agr_met.equals("SERIAL")) {  	
						String query = "update APP_MASTER set app_state='" + LINE_ORD[ap+1] +"', ";			//상태
							query += "agree_date='" + doc_agd +"', ";										//합의일자
							query += "agree_comment='" + this.doc_agm+comment + "' where pid='" + doc_pid + "'";		//합의의견
						// DB에 저장 실행문
						stmt.executeUpdate(query);					

					//합의단계 일괄적 진행 (현 합의자 인원수 및 합의단계 반영)
					} else if (agr_met.equals("PARALLEL")) {  	
						int pass_cnt = Integer.parseInt(this.agr_pas);	//현재 협의자 수
						pass_cnt += 1;									//현 합의자 자신을 인원에 포함한다.
						String updata = "";								//update문자
						// 합의상태 및 현 합의자 총인원수(pass_cnt) 입력  [총인원수 : this.agr_cnt]
						if(this.agr_cnt > pass_cnt) {			//합의진행중
							updata = "update APP_MASTER set app_state='" + LINE_ORD[ap] +"',";				//상태
							updata += "agree_pass='" + Integer.toString(pass_cnt) +"',";					//합의자 인원
						} else {								//마지막 합의자 결재
							updata = "update APP_MASTER set app_state='" + LINE_ORD[ap+1] +"',";			//상태
							updata += "agree_pass='" + Integer.toString(pass_cnt) +"',";					//합의자 인원
						} //if
			
						// 각 합의자별 합의일자 및 의견입력
						if(login_owner.equals(this.doc_agr)){			//합의자
							updata += "agree_date='" + doc_agd +"',";						  				//합의일자
							updata += "agree_comment='" + this.doc_agm+comment + "' where pid='" + doc_pid + "'";  	//합의의견
						} else if(login_owner.equals(this.doc_agr2)){	//합의자2
							updata += "agree2_date='" + doc_agd +"',";  									//합의일자
							updata += "agree2_comment='" + this.doc_agm2+comment + "' where pid='" + doc_pid + "'";  	//합의의견2
						} else if(login_owner.equals(this.doc_agr3)){	//합의자3
							updata += "agree3_date='" + doc_agd +"',";  									//합의일자
							updata += "agree3_comment='" + this.doc_agm3+comment + "' where pid='" + doc_pid + "'";  	//합의의견3
						} else if(login_owner.equals(this.doc_agr4)){	//합의자4
							updata += "agree4_date='" + doc_agd +"',"; 										//합의일자
							updata += "agree4_comment='" + this.doc_agm4+comment + "' where pid='" + doc_pid + "'";  	//합의의견4
						} else if(login_owner.equals(this.doc_agr5)){	//합의자5
							updata += "agree5_date='" + doc_agd +"',"; 										//합의일자
							updata += "agree5_comment='" + this.doc_agm5+comment + "' where pid='" + doc_pid + "'";  	//합의의견5
						} else if(login_owner.equals(this.doc_agr6)){	//합의자6
							updata += "agree6_date='" + doc_agd +"',"; 										//합의일자
							updata += "agree6_comment='" + this.doc_agm6+comment + "' where pid='" + doc_pid + "'";  	//합의의견6
						} else if(login_owner.equals(this.doc_agr7)){	//합의자7
							updata += "agree7_date='" + doc_agd +"',";  									//합의일자
							updata += "agree7_comment='" + this.doc_agm7+comment + "' where pid='" + doc_pid + "'";  	//합의의견7
						} else if(login_owner.equals(this.doc_agr8)){	//합의자8
							updata += "agree8_date='" + doc_agd +"',"; 										//합의일자
							updata += "agree8_comment='" + this.doc_agm8+comment + "' where pid='" + doc_pid + "'";  	//합의의견8
						} else if(login_owner.equals(this.doc_agr9)){	//합의자9
							updata += "agree9_date='" + doc_agd +"',";  									//합의일자
							updata += "agree9_comment='" + this.doc_agm9+comment + "' where pid='" + doc_pid + "'";  	//합의의견9
						} else if(login_owner.equals(this.doc_agr10)){	//합의자10
							updata += "agree10_date='" + doc_agd +"',"; 									//합의일자
							updata += "agree10_comment='" + this.doc_agm10+comment + "' where pid='" + doc_pid + "'";  //합의의견10
						} //if
						// DB에 저장 실행문
						stmt.executeUpdate(updata);
					} //if
				} //if		
			} //if
		} //for
		stmt.close();
	}

	/******************************************************************************
		반려 데이터 처리하기
	******************************************************************************/
	public void processAPR(String pid,String comment) throws Exception  
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		//선행실행
		eleDecisionReadDoc(pid);	//상세내용 읽기
		eleDecisionLineOrder();		//결재선 해석
		
		//데이터 처리하기
		String query = "update APP_MASTER set app_state='APR',";
		if("APV".equals(this.doc_ste)) 				//검토단계에서 반려시 의견을 검토란에
			query += "review_date='" + anbdt.getTime() +"',review_comment='" + this.doc_rem+comment + "',";
		else if("APL".equals(this.doc_ste)) 		//승인단계에서 반려시 의견을 승인란에
			query += "decision_date='" + anbdt.getTime() +"',decision_comment='" + this.doc_dem+comment + "',";
		query += "delete_date='" + anbdt.getAddDateNoformat(7) + "' where pid='" + pid + "'";  //삭제예정일
		stmt.executeUpdate(query);

		//기타문서 전자결재상신시 전자결재 반려임을 알려주기(각문서별 상이함)
		if(this.doc_flag.equals("SERVICE")) {		//고객관리 서비스 결재상신
			StringTokenizer strid = new StringTokenizer(doc_lid,",");//콤마로 구분된 관련문서
			while(strid.hasMoreTokens()) {
				String EE = "update HISTORY_TABLE set flag='' where ah_id='"+strid.nextToken()+"'";
				stmt.executeUpdate(EE);
			} //while
		} 
		//기술 문서인경우 
		else if(doc_flag.equals("TD")) {	
			String[] data = new String[3]; 
			if(doc_lid.length() != 0) {
					StringTokenizer num = new StringTokenizer(doc_lid,"|");
					int sn = 0;
					while(num.hasMoreTokens()) {
						data[sn] = num.nextToken();				//관리번호,table name,version
						sn++;
					}
			}
			mdBO.rejectTD(pid,data[0],data[1],data[2]);	//전자결재관리코드,기술문서관리코드,tablename,version
		} 
		//승인원 문서인경우 
		else if(doc_flag.equals("AKG")) {	
			int no = doc_lid.indexOf("|");
			String tm_id = doc_lid.substring(0,no);							//승인관리번호
			String tm_report = doc_lid.substring(no+1,doc_lid.length());	//승인종류
			//신규등록 
			if(tm_report.equals("report_w")) {
				String EN = "update ca_master set aid='EN' where tmp_approval_no='"+tm_id+"'";
				stmt.executeUpdate(EN);
			}
		}
		//견적 문서인 경우
		else if(doc_flag.equals("EST")) {
			String estimate_no="";			//견적번호
			String ver = "";				//견적버젼
			int bar = doc_lid.indexOf("|");
			estimate_no = doc_lid.substring(0,bar);
			ver = doc_lid.substring(bar+1,doc_lid.length());

			com.anbtech.em.business.EstimateBO estBO = new com.anbtech.em.business.EstimateBO(con);
			estBO.makeReturnStat(estimate_no,ver);
		}
		//특근관리 문서인경우 
		else if(doc_flag.equals("EWK")) {
			com.anbtech.ew.business.ExtraWorkModuleBO ewkBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);//특근신청
			ewkBO.ewAppInfoProcess(doc_pid,doc_lid,"reject");
		}
		//BOM관리 문서인경우 
		else if(doc_flag.equals("BOM")) {
			com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);//BOM승인신청
			String where = "where id='"+doc_dec+"'";
			String app_name = getColumnData("user_table","name",where);
			bomDAO.setBomStatus(doc_lid,"0",doc_dec,app_name,doc_pid);
		}
		//설계변경관리 문서인경우 
		else if(doc_flag.equals("DCM")) {
			com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);//설계변경승인신청
			int sep_no = doc_lid.indexOf("|");
			int len_no = doc_lid.length();
			String eco_pid = doc_lid.substring(0,sep_no);
			String eco_no = doc_lid.substring(sep_no+1,len_no);
			bomDAO.setEccStatus(eco_pid,doc_pid,"rej");			//ECC_COM,ECC_REQ or ECC_ORD
			bomDAO.setEccBomStatus(eco_no,"0");					//ECC_BOM
		}
		//구매요청관리 문서인경우 
		else if(doc_flag.equals("PCR")) {
			com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);//MRP 
			com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);//MRP
			com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//구매관리
			int sep_no = doc_lid.indexOf("|");
			int len_no = doc_lid.length();
			String req_pid = doc_lid.substring(0,sep_no);			//구매관리번호
			String req_type = doc_lid.substring(sep_no+1,len_no);	//구매TYPE (MRP : 생산관리 MRP구매요청)
			//생산관리 MRP구매요청인 경우만 진행
			if(req_type.equals("MRP")) {
				String where = "where pu_req_no = '"+req_pid+"'";
				String mrp_pid = mrpDAO.getColumData("MRP_MASTER","pid",where);
				mrpBO.setMrpStatus(mrp_pid,"3","0",""); 
			}
			purBO.puRequestAppInfoProcess("reject_req",doc_pid,req_pid);
		}
		//발주요청관리 문서인경우 
		else if(doc_flag.equals("ODR")) {
			com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//구매관리
			purBO.puOrderAppInfoProcess("reject_order",doc_pid,doc_lid);
		}
		//구매입고관리 문서인경우 
		else if(doc_flag.equals("PWH")) {
			com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//구매관리
			purBO.puEnterAppInfoProcess("reject_enter",doc_pid,doc_lid);
		}
		//부품출고관리 문서인경우 
		else if(doc_flag.equals("TGW")) {
			com.anbtech.st.business.StockMgrBO stcBO = new com.anbtech.st.business.StockMgrBO(con);//재고관리
			stcBO.DeliveryAppInfoProcess("reject_delivery",doc_lid,doc_lid);
		}
		//기타 양식문서인 경우
		else {
			formPaperDown(doc_flag,doc_lid,comment);	
		}
		stmt.close();
	}
	/******************************************************************************
		결재완료된 일반보고문서 작성자 부서코드 update하기 (app_save만)
	*******************************************************************************/
	private void setAcCode() throws SQLException 
	{
		String name = "";			//이름
		String ac_code = "";		//부서코드
		String div = "";			//사업부명
		String rank = "";			//직급명
		String query = "";
		
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "select distinct a.id,a.name,b.ac_code,b.ac_name,c.ar_name from ";
		query += "user_table a,class_table b,rank_table c ";

		if(doc_wri.length() != 0) {
			String query1 = query + "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.id='"+doc_wri+"'";
			//System.out.println("q1 : " + query1);
			rs = stmt.executeQuery(query1);
			while(rs.next()) {
				name = rs.getString("name");
				ac_code = rs.getString("ac_code");
				div = rs.getString("ac_name");
				rank = rs.getString("ar_name");	
			}
			String query2 = "update APP_SAVE set ac_code='"+ac_code+"' where pid='"+this.doc_pid+"'";
			//System.out.println("q2 : " + query2);
			stmt.executeUpdate(query2);
		}
					
		stmt.close();
		rs.close();
	}
	/******************************************************************************
		각 양식문서별 데이터 처리하기 (승인여부및 문서관리번호)
	******************************************************************************/
	private void formPaperUp(String doc_flag,String doc_lid) throws Exception 
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		String data = "";		//update
		String tag = "";		//flag or flag2 

		//근태관리(휴가원,외출계,출장신청서)
		if(doc_flag.equals("HYU_GA") || doc_flag.equals("OE_CHUL") || doc_flag.equals("CHULJANG_SINCHEONG")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("GEUNTAE_MASTER","gt_id",doc_lid);

			//결재승인 확인
			data = "update GEUNTAE_MASTER set "+tag+"='EF' where gt_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//문서번호 및 근태일수 입력(1차측 결재시만)
			if(tag.equals("flag")){
				numberingDoc("GEUNTAE_MASTER",doc_flag,"gt_id",doc_lid);	//문서번호 입력

				//근태일수 입력(geuntae_count)
				if(doc_flag.equals("HYU_GA")) {								//휴가원
					try {hyugaDayDAO.processCount(doc_lid); } catch (Exception e) {} 	
				} else if(doc_flag.equals("CHULJANG_SINCHEONG"))	{		//출장
					try {chuljangDayDAO.processChulJangCount(doc_lid);} catch (Exception e) {} 
				} else if(doc_flag.equals("OE_CHUL")) {						//외출
					try {chuljangDayDAO.processEoChulCount(doc_lid);} catch (Exception e) {} 
				}

			}

		} 
		//차량관리(배차신청)
		else if(doc_flag.equals("BAE_CHA")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("CHARYANG_MASTER","cr_id",doc_lid);

			//문서번호 입력(1차측 결재시만)
			if(tag.equals("flag")){
				//결재승인 확인
				data = "update CHARYANG_MASTER set flag='EF' where cr_id='"+doc_lid+"'";
				stmt.executeUpdate(data);
				//문서번호입력
				numberingDoc("CHARYANG_MASTER",doc_flag,"cr_id",doc_lid);
			} 
			//email 보내기(2차측 결재시만)
			else if(tag.equals("flag2")) {
				//결재승인 확인
				data = "update CHARYANG_MASTER set flag2='EF' where cr_id='"+doc_lid+"'";
				stmt.executeUpdate(data);
				//email 보내기
				ApprovalToEmail(this.doc_wri,this.doc_wna,"BAE_CHA",doc_lid,"APP",""); //APP:승인
			}
		} 
		//보고서 관리(보고서,출장보고서)
		else if(doc_flag.equals("BOGO") || doc_flag.equals("CHULJANG_BOGO")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("BOGOSEO_MASTER","bg_id",doc_lid);

			//결재승인 확인
			data = "update BOGOSEO_MASTER set "+tag+"='EF' where bg_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//문서번호 입력(1차측 결재시만)
			if(tag.equals("flag")){
				numberingDoc("BOGOSEO_MASTER",doc_flag,"bg_id",doc_lid);	//문서번호입력
			} 
		} 
		//지원 관리(기안서,명함신청서,사유서,협조전)
		else if(doc_flag.equals("GIAN") || doc_flag.equals("MYEONGHAM") || doc_flag.equals("SAYU") || doc_flag.equals("HYEOPJO")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("JIWEON_MASTER","jw_id",doc_lid);

			//결재승인 확인
			data = "update JIWEON_MASTER set "+tag+"='EF' where jw_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//문서번호 입력(1차측 결재시만)
			if(tag.equals("flag")){
				numberingDoc("JIWEON_MASTER",doc_flag,"jw_id",doc_lid);	//문서번호입력
			} 
		} 
		//연장근무(잔업) 관리(연장근무신청서)
		else if(doc_flag.equals("YEONJANG")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("JANEUP_MASTER","ju_id",doc_lid);

			//결재승인 확인
			data = "update JANEUP_MASTER set "+tag+"='EF' where ju_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//문서번호 입력(1차측 결재시만)
			if(tag.equals("flag")){
				numberingDoc("JANEUP_MASTER",doc_flag,"ju_id",doc_lid);	//문서번호입력
			} 
		}
		//인사 관리(구인의뢰서)
		else if(doc_flag.equals("GUIN")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("INSA_MASTER","is_id",doc_lid);

			//결재승인 확인
			data = "update INSA_MASTER set "+tag+"='EF' where is_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//문서번호 입력(1차측 결재시만)
			if(tag.equals("flag")){
				numberingDoc("INSA_MASTER",doc_flag,"is_id",doc_lid);	//문서번호입력
			} 
		}
		//교육 관리(교육일지)
		else if(doc_flag.equals("GYOYUK_ILJI")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("GYOYUK_MASTER","gy_id",doc_lid);

			//결재승인 확인
			data = "update GYOYUK_MASTER set "+tag+"='EF' where gy_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//문서번호 입력(1차측 결재시만)
			if(tag.equals("flag")){
				numberingDoc("GYOYUK_MASTER",doc_flag,"gy_id",doc_lid);	//문서번호입력
			} 
		}
		//공지 공문
		else if(doc_flag.equals("ODT")) {	
			//결재승인,관리코드,결재승인일자,삭제예정일자 확인
			data = "update OfficialDocument set flag='EF',";
			data += "app_id='"+this.doc_pid+"',app_date='"+this.doc_ded+"',";
			data += "delete_date='"+anbdt.getAddMonthNoformat(1)+"' where id='"+doc_lid+"'";
			//System.out.println("Flag update : " + data);
			stmt.executeUpdate(data);
			//문서번호 입력
			numberingDoc("OfficialDocument",doc_flag,"id",doc_lid);
			//관련모듈로 전송 및 결재선 입력하기(to OfficialDocument_app)
			com.anbtech.gw.db.ModuleApprovalOffiDocDAO appDAO = new com.anbtech.gw.db.ModuleApprovalOffiDocDAO(con);
			appDAO.readODT(doc_lid,"ODT",this.ip_addr);
		}
		//사내공문
		else if(doc_flag.equals("IDS")) {	
			//결재승인,관리코드,결재승인일자,삭제예정일자 확인
			data = "update InDocument_send set flag='EF',";
			data += "app_id='"+this.doc_pid+"',app_date='"+this.doc_ded+"',";
			data += "delete_date='"+anbdt.getAddMonthNoformat(1)+"' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
			//문서번호 입력
			numberingDoc("InDocument_send",doc_flag,"id",doc_lid);
			//관련모듈로 전송 및 결재선 입력하기(to OfficialDocument_app)
			com.anbtech.gw.db.ModuleApprovalOffiDocDAO appDAO = new com.anbtech.gw.db.ModuleApprovalOffiDocDAO(con);
			appDAO.readIDS(doc_lid,"IDS",this.ip_addr);
		}
		//사외공문
		else if(doc_flag.equals("ODS")) {	
			//결재승인,관리코드,결재승인일자,삭제예정일자 확인
			data = "update OutDocument_send set flag='EF',";
			data += "app_id='"+this.doc_pid+"',app_date='"+this.doc_ded+"',";
			data += "delete_date='"+anbdt.getAddMonthNoformat(1)+"' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
			//문서번호 입력
			numberingDoc("OutDocument_send",doc_flag,"id",doc_lid);
			//관련모듈로 전송 및 결재선 입력하기(to OfficialDocument_app)
			com.anbtech.gw.db.ModuleApprovalOffiDocDAO appDAO = new com.anbtech.gw.db.ModuleApprovalOffiDocDAO(con);
			appDAO.readODS(doc_lid,"ODS",this.ip_addr);
		}
		//자산관리
		else if(doc_flag.equals("ASSET")) {	
			//1차측(as_status:2) 인지 2차측(as_status:4)인지 판단
			tag = checkFlag("as_history",doc_lid);

			//1차 결재승인 
			if(tag.equals("2")) {
				//결재선 자산관리에 저장하기
				com.anbtech.am.business.AssetModuleBO appAs = new com.anbtech.am.business.AssetModuleBO(con);
				appAs.asAppInfoProcess(doc_pid,doc_lid,"approval1","1");
			}
			//2차 결재 승인 
			else if(tag.equals("4")) {
				//결재선 자산관리에 저장하기
				com.anbtech.am.business.AssetModuleBO appAs = new com.anbtech.am.business.AssetModuleBO(con);
				appAs.asAppInfoProcess(doc_pid,doc_lid,"approval1","2");
				//전자 우편으로 통보하기
				//ApprovalToEmail(this.doc_wri,this.doc_wna,"ASSET",doc_lid,"APP",""); //APP:승인
			}
		}

		stmt.close();

	}
	/******************************************************************************
		각 양식문서별 데이터 처리하기 (반려시 flag반영하기 : EN)
	******************************************************************************/
	private void formPaperDown(String doc_flag,String doc_lid,String rej_comment) throws Exception 
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		String data = "";		//update
		String tag = "";		//flag or flag2 

		//근태관리(휴가원,외출계,출장신청서)
		if(doc_flag.equals("HYU_GA") || doc_flag.equals("OE_CHUL") || doc_flag.equals("CHULJANG_SINCHEONG")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("GEUNTAE_MASTER","gt_id",doc_lid);

			//결재반려 처리
			data = "update GEUNTAE_MASTER set "+tag+"='EN' where gt_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		} 
		//차량관리(배차신청)
		else if(doc_flag.equals("BAE_CHA")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("CHARYANG_MASTER","cr_id",doc_lid);

			//결재반려 처리
			if(tag.equals("flag")) {
				data = "update CHARYANG_MASTER set flag='EN' where cr_id='"+doc_lid+"'";
				stmt.executeUpdate(data);
			} else if(tag.equals("flag2")) {
				data = "update CHARYANG_MASTER set flag='EN' where cr_id='"+doc_lid+"'";
				String data2 = "update CHARYANG_MASTER set flag2='EN' where cr_id='"+doc_lid+"'";
				stmt.executeUpdate(data);
				stmt.executeUpdate(data2);
				//email 보내기
				ApprovalToEmail(this.doc_wri,this.doc_wna,"BAE_CHA",doc_lid,"REJ",rej_comment); //REJ : 반려
			}
		} 
		//보고서 관리(보고서,출장보고서)
		else if(doc_flag.equals("BOGO") || doc_flag.equals("CHULJANG_BOGO")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("BOGOSEO_MASTER","bg_id",doc_lid);

			//결재반려 처리
			data = "update BOGOSEO_MASTER set "+tag+"='EN' where bg_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		} 
		//지원 관리(기안서,명함신청서,사유서,협조전)
		else if(doc_flag.equals("GIAN") || doc_flag.equals("MYEONGHAM") || doc_flag.equals("SAYU") || doc_flag.equals("HYEOPJO")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("JIWEON_MASTER","jw_id",doc_lid);

			//결재반려 처리
			data = "update JIWEON_MASTER set "+tag+"='EN' where jw_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		} 
		//연장근무(잔업) 관리(연장근무신청서)
		else if(doc_flag.equals("YEONJANG")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("JANEUP_MASTER","ju_id",doc_lid);

			//결재반려 처리
			data = "update JANEUP_MASTER set "+tag+"='EN' where ju_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}
		//인사 관리(구인의뢰서)
		else if(doc_flag.equals("GUIN")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("INSA_MASTER","is_id",doc_lid);

			//결재반려 처리
			data = "update INSA_MASTER set "+tag+"='EN' where is_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}
		//교육 관리(교육일지)
		else if(doc_flag.equals("GYOYUK_ILJI")) {	
			//1차측(주관부서:flag) 인지 2차측(집행부서:flag2)인지 판단
			tag = checkFlag("GYOYUK_MASTER","gy_id",doc_lid);

			//결재반려 처리
			data = "update GYOYUK_MASTER set "+tag+"='EN' where gy_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}
		//공지 공문
		else if(doc_flag.equals("ODT")) {	
			//결재반려 처리
			data = "update OfficialDocument set flag='EN' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}

		//사내공문
		else if(doc_flag.equals("IDS")) {	
			//결재승인 확인
			data = "update InDocument_send set flag='EN' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}

		//사외공문
		else if(doc_flag.equals("ODS")) {	
			//결재승인 확인
			data = "update OutDocument_send set flag='EN' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}
		//자산관리
		else if(doc_flag.equals("ASSET")) {	
			//1차측(as_status:2) 인지 2차측(as_status:4)인지 판단
			tag = checkFlag("as_history",doc_lid);

			//1차 결재반려 
			if(tag.equals("2")) {
				//결재선 자산관리에 저장하기
				com.anbtech.am.business.AssetModuleBO appAs = new com.anbtech.am.business.AssetModuleBO(con);
				appAs.asAppInfoProcess(doc_pid,doc_lid,"reject","1");
			}
			//2차 결재반려
			else if(tag.equals("4")) {
				com.anbtech.am.business.AssetModuleBO appAs = new com.anbtech.am.business.AssetModuleBO(con);
				appAs.asAppInfoProcess(doc_pid,doc_lid,"reject","2");
				//전자 우편으로 통보하기
				//ApprovalToEmail(this.doc_wri,this.doc_wna,"ASSET",doc_lid,"REJ",""); //REJ:반려
			}

		}

		stmt.close();
	}

	/******************************************************************************
		양식문서 데이터 처리하기키 위해 주관부서인지 집행부서인지 판단하기
		1차측(주관부서) : flag,  2차측(집행부서) : flag2 을 리턴한다.
	******************************************************************************/
	private String checkFlag(String tablename,String id_name,String doc_lid) throws SQLException 
	{	
		String flg1 = "";	//flag의 값
		String flg2 = "";	//flag2의 값

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct flag,flag2 from "+tablename+" where "+id_name+"='"+doc_lid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			flg1 = rs.getString("flag");
			flg2 = rs.getString("flag2");
		}
		stmt.close();
		rs.close();

		String rdata = "";
		if(flg1.equals("EE")) rdata = "flag";
		else if(flg1.equals("EF")) rdata = "flag2";
		
		return rdata;
	}

	/******************************************************************************
		자산관리에서 1차 결재인지 2차 결재인지 판단하기
		as_history as_status [2:1차상신, 3:1차결재완료, 4:2차상신, 5:2차결재완료
	******************************************************************************/
	public String checkFlag(String tablename,String doc_lid) throws SQLException 
	{	
		String rtn = "";	//리턴값
		
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct as_status from "+tablename+" where h_no='"+doc_lid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			rtn = rs.getString("as_status");
		}
		stmt.close();
		rs.close();

		return rtn;
	}

	
	/******************************************************************************
		양식 Table별 문서번호 입력하기  : 1차측 (주관부서)에서 결재승인시만
		[부서약어(2) + 양식별 구분자 + 년도(2) + 일련번호(3)]
	******************************************************************************/
	private void numberingDoc(String tablename,String doc_flag,String id_name,String doc_lid) throws SQLException 
	{
		String doc_num = "";

		Statement stmt = null;
		ResultSet rs = null;

		//1.부서코드 찾기
		String head = searchDivCode(tablename,id_name,doc_lid);

		//2.양식별 구분자 찾기
		String fp = searchFPCode(doc_flag);

		//3.년도 마지막 두자리 찾기
		String getyear = anbdt.getYear();
		String year = getyear.substring(2,4);

		doc_num = head + fp + year + "-";

		//4.해당부서코드별 최종입력 내용찾기 (sorting후 최초값이 최종입력값임)
		String flag_col_name = "ys_kind";
		if(doc_flag.equals("GEN")) flag_col_name = "flag";			//일반보고문서
		else if(doc_flag.equals("ODT")) {flag_col_name = "flag"; doc_flag="EF"; }	//공지공문
		else if(doc_flag.equals("IDS")) {flag_col_name = "flag"; doc_flag="EF"; }	//사내공문
		else if(doc_flag.equals("ODS")) {flag_col_name = "flag"; doc_flag="EF"; }	//사외공문

		stmt = con.createStatement();
		String query = "select distinct doc_id,ac_code from "+tablename+" where ac_code='"+head+"' and "+flag_col_name+"='"+doc_flag+"' order by doc_id desc";
		rs = stmt.executeQuery(query);
	
		String doc_id = "";
		if(rs.next()) doc_id = rs.getString("doc_id");
		if(doc_id == null) doc_id = "";
	
		
		//최초 입력하기
		if(doc_id.length() == 0) {
			doc_num += "001";
		} 
		//일련번호 증가하기
		else {
			//일련번호 3자리 찾기
			int len = doc_id.length();
			int serial = Integer.parseInt(doc_id.substring(len-3,len));	
			serial++;	//1증가
			
			//조합하기 (부서코드+알파벳구분+YY+"-"+일련번호(3)
			doc_num += nmf.toDigits(serial);
		}
		//System.out.println("문서관리번호 : " + doc_num); 
		//5.등록하기
		numberingDocInput(tablename,doc_num,id_name,doc_lid);
	
		stmt.close();
		rs.close();
	}

	/******************************************************************************
		양식 Table별 문서번호 입력하기  : 1차측 (주관부서)에서 결재승인시만
		관련 TABLE에 문서관리번호 입력하기 실행
	******************************************************************************/
	private void numberingDocInput(String tablename,String doc_num,String id_name,String doc_lid) throws SQLException 
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String data = "update "+tablename+" set doc_id='"+doc_num+"' where "+id_name+"='"+doc_lid+"'";
		//System.out.println("문서번호 입력 : " + data);
		stmt.executeUpdate(data);
		stmt.close();
	}

	/******************************************************************************
		양식 Table별 문서번호입력할 부서코드찾기 (각양식 공통부서컬럼명 : ac_code)
	******************************************************************************/
	private String searchDivCode(String tablename,String id_name,String doc_lid) throws SQLException 
	{

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//해당문서번호의 해당자 부서코드 구하기
		String query = "select distinct ac_code from "+tablename+" where "+id_name+"='"+doc_lid+"'";
		rs = stmt.executeQuery(query);
		
		String div_code = "";
		if(rs.next()) div_code = rs.getString("ac_code");
		if(div_code == null) div_code = "";

		stmt.close();
		rs.close();
		return div_code;
	}

	/******************************************************************************
		양식별 구분자 찾기 (table : system_minor_code)
	******************************************************************************/
	private String searchFPCode(String doc_flag) throws SQLException 
	{

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//해당문서번호의 해당자 부서코드 구하기
		String query = "select distinct code from system_minor_code where code_name='"+doc_flag+"'";
		rs = stmt.executeQuery(query);
		
		String form_code = "";
		if(rs.next()) form_code = rs.getString("code");
		if(form_code == null) form_code = "";

		stmt.close();
		rs.close();
		return form_code;
	}

	/******************************************************************************
		2차 부서 결재 승인후 개인우편으로 전달하기
		writer_id/writer_name: 작성자로서 통제부서 기안자 사번/이름
		doc_kind : 문서종류
		link_id : 관련문서 관리번호
		tag : 승인[APP] or 반려[REJ]
	******************************************************************************/
	private void ApprovalToEmail(String writer_id,String writer_name,String doc_kind,String link_id,String tag,String comment) throws SQLException 
	{
		String subj = "";						//제목
		String receiver = "";					//수신자 (사번/이름;) post_master
		String l_receiver = "";					//수신자 (사번) post_letter
		String pid = getID();					//관리번호
		String w_date = anbdt.getTime();		//작성일
		String bon_path="/post/"+writer_id+"/text_upload";	//본문path
		String delete_date = anbdt.getAddMonthNoformat(1);	//삭제예정일
		String filename = "";					//저장파일명

		Statement stmt = null;
		stmt = con.createStatement();
		
		//배차관리 제목 수신인 구하기
		if(doc_kind.equals("BAE_CHA")) {	
			receiver = getBaeChaReceiver(link_id);	//수신자 (post_master의 수신자 : 사번/이름;)
			if(tag.equals("APP")) {		//승인
				subj = "배차신청 예약완료";					//제목
				filename = pid;								//본문저장 파일명
				setBaeChaContent(link_id,filename);			//본문 작성내용 파일로 담기(pid : 본문저장 파일명)
			} else if(tag.equals("REJ")) { //반려
				subj = "배차신청 예약취소";					//제목
				filename = pid+"_r";						//본문저장 파일명
				setBaeChaRejContent(link_id,filename,comment);	//본문 작성내용 파일로 담기(pid : 본문저장 파일명)
			}
		}
		else if(doc_kind.equals("ASSET")) {	
			receiver = getAssetReceiver(link_id);	//수신자 (post_master의 수신자 : 사번/이름;)
			if(tag.equals("APP")) {		//승인
				subj = "자산반출신청 승인완료";				//제목
				filename = pid;								//본문저장 파일명
				setAssetContent(link_id,filename);			//본문 작성내용 파일로 담기(pid : 본문저장 파일명)
			} else if(tag.equals("REJ")) { //반려
				subj = "자산반출신청 승인취소";				//제목
				filename = pid+"_r";						//본문저장 파일명
				setAssetRejContent(link_id,filename,comment);	//본문 작성내용 파일로 담기(pid : 본문저장 파일명)
			}

		}

		//post_letter 테이블에 입력할 수신자 (사번만 필요)
		l_receiver = receiver.substring(0,receiver.indexOf("/"));
			
		//관련내용 post tabel(post_master,post_letter)로 담기
		String mquery = "";
		String lquery = "";
		//post_master
		mquery = "insert into post_master(pid,post_subj,writer_id,writer_name,write_date,";
		mquery += "post_receiver,isopen,post_state,post_select,bon_path,bon_file,delete_date) values('";
		mquery += pid+"','"+subj+"','"+writer_id+"','"+writer_name+"','"+w_date+"','"+receiver+"','";
		mquery += "0"+"','"+"SND"+"','"+"CFM"+"','"+bon_path+"','"+filename+"','"+delete_date+"')";
		//System.out.println("email master : " + mquery);
		//post_letter
		lquery = "insert into post_letter(pid,post_subj,writer_id,writer_name,write_date,";
		lquery += "post_receiver,isopen,post_select,delete_date) values('";
		lquery += pid+"','"+subj+"','"+writer_id+"','"+writer_name+"','"+w_date+"','"+l_receiver+"','";
		lquery += "0"+"','"+"CFM"+"','"+delete_date+"')";
		//System.out.println("email letter : " + lquery);
		try{ 
			stmt.executeUpdate(mquery);
			stmt.executeUpdate(lquery);
		} catch (Exception e) {}
		stmt.close();
	}

	/******************************************************************************
		2차부서 결재 승인후 개인우편으로 전달하기
		본문내용 저장할 full path 
	******************************************************************************/
	public void setMailFullPath(String path)
	{
		this.mail_path = path;
	}

	/******************************************************************************
		2차부서 결재 승인후 개인우편으로 전달하기
		수신자 사번/이름 구하기 (배차신청시)
	******************************************************************************/
	private String getBaeChaReceiver(String link_id) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct user_id,user_name from charyang_master where cr_id='"+link_id+"'";
		rs = stmt.executeQuery(query);
		
		String receiver = "";
		if(rs.next()) receiver = rs.getString("user_id")+"/"+rs.getString("user_name")+";";
		if(receiver == null) receiver = "";

		stmt.close();
		rs.close();
		return receiver;
	}

	/******************************************************************************
		2차부서 결재 승인후 개인우편으로 전달하기
		본문내용 저장하기 (배차신청시)
	******************************************************************************/
	private void setBaeChaContent(String link_id,String file_name) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct u_year,u_month,u_date,u_time,tu_year,tu_month,tu_date,tu_time ";
		query += "from charyang_master where cr_id='"+link_id+"'";
		rs = stmt.executeQuery(query);

		String content="배차신청이 예약되었습니다.\n\n";
		content += "배차기간 : ";
		while(rs.next()) {
			content += rs.getString("u_year")+"-"+rs.getString("u_month")+"-"+rs.getString("u_date")+" "+rs.getString("u_time")+" ~ ";
			content += rs.getString("tu_year")+"-"+rs.getString("tu_month")+"-"+rs.getString("tu_date")+" "+rs.getString("tu_time");
		}
		stmt.close();
		rs.close();

		String path = this.mail_path + "/post/" + this.doc_wri + "/text_upload";
		////System.out.println("content : " + content);
		////System.out.println("path : " + path);
		text.setFilepath(path);							//directory생성하기
		text.WriteHanguel(path,file_name,content);		//내용 파일로 저장하기
	}

	/******************************************************************************
		2차부서 결재 반려후 개인우편으로 전달하기
		본문내용 저장하기 (배차신청시)
	******************************************************************************/
	private void setBaeChaRejContent(String link_id,String file_name,String comment) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct u_year,u_month,u_date,u_time,tu_year,tu_month,tu_date,tu_time ";
		query += "from charyang_master where cr_id='"+link_id+"'";
		rs = stmt.executeQuery(query);

		String content="배차신청 예약이 취소되었습니다.\n\n";
		content += "예약기간 : ";
		while(rs.next()) {
			content += rs.getString("u_year")+"-"+rs.getString("u_month")+"-"+rs.getString("u_date")+" "+rs.getString("u_time")+" ~ ";
			content += rs.getString("tu_year")+"-"+rs.getString("tu_month")+"-"+rs.getString("tu_date")+" "+rs.getString("tu_time");
		}
		content +="\n\n취소의견 : "+comment+"\n\n";

		stmt.close();
		rs.close();

		String path = this.mail_path + "/post/" + this.doc_wri + "/text_upload";
		////System.out.println("content : " + content);
		////System.out.println("path : " + path);
		text.setFilepath(path);							//directory생성하기
		text.WriteHanguel(path,file_name,content);		//내용 파일로 저장하기
	}

	/******************************************************************************
		2차부서 결재 승인후 개인우편으로 전달하기
		수신자 사번/이름 구하기 (자산반출 신청결과 전자우편 수신자)
	******************************************************************************/
	private String getAssetReceiver(String link_id) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct w_name from as_history where h_no='"+link_id+"'";
		rs = stmt.executeQuery(query);
		
		String receiver = "";
		if(rs.next()) receiver = rs.getString("w_name")+";";
		if(receiver == null) receiver = "";

		stmt.close();
		rs.close();
		return receiver;
	}

	/******************************************************************************
		2차부서 결재 승인후 개인우편으로 전달하기
		본문내용 저장하기 (자산반출 승인시)
	******************************************************************************/
	private void setAssetContent(String link_id,String file_name) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct u_date,tu_date ";
		query += "from as_history where h_no='"+link_id+"'";
		rs = stmt.executeQuery(query);

		String content="반출신청이 승인되었습니다.\n\n";
		content += "반출신청기간 : ";
		while(rs.next()) {
			String from = rs.getString("u_date");
			String to = rs.getString("tu_date"); if(to == null) to = "";
			content += from.substring(0,4)+"-"+from.substring(4,6)+"-"+from.substring(6,8)+" ~ ";
			if(to.length() > 4)
				content += to.substring(0,4)+"-"+to.substring(4,6)+"-"+to.substring(6,8);
		}
		stmt.close();
		rs.close();

		String path = this.mail_path + "/post/" + this.doc_wri + "/text_upload";
		////System.out.println("content : " + content);
		////System.out.println("path : " + path);
		text.setFilepath(path);							//directory생성하기
		text.WriteHanguel(path,file_name,content);		//내용 파일로 저장하기
	}

	/******************************************************************************
		2차부서 결재 반려후 개인우편으로 전달하기
		본문내용 저장하기 (자산반출 취소시)
	******************************************************************************/
	private void setAssetRejContent(String link_id,String file_name,String comment) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct u_date,tu_date ";
		query += "from as_history where h_no='"+link_id+"'";
		rs = stmt.executeQuery(query);

		String content="반출신청이 승인취소되었습니다.\n\n";
		content += "반출신청기간 : ";
		while(rs.next()) {
			String from = rs.getString("u_date");
			String to = rs.getString("tu_date");
			content += from.substring(0,4)+"-"+from.substring(4,6)+"-"+from.substring(6,8)+" ~ ";
			content += to.substring(0,4)+"-"+to.substring(4,6)+"-"+to.substring(6,8);
		}
		content +="\n\n취소의견 : "+comment+"\n\n";

		stmt.close();
		rs.close();

		String path = this.mail_path + "/post/" + this.doc_wri + "/text_upload";
		////System.out.println("content : " + content);
		////System.out.println("path : " + path);
		text.setFilepath(path);							//directory생성하기
		text.WriteHanguel(path,file_name,content);		//내용 파일로 저장하기
	}

	/******************************************************************************
		임시보관함,반려함의 경우 삭제하기
	******************************************************************************/
	public void appDelete(String pid) throws SQLException 
	{
		String where="", flag="",plid="",updata="",delete="",add_file="";
		String user_id="";

		Statement stmt = null;
		stmt = con.createStatement();

		//양식문서의 종류구하기
		where = "where pid='"+pid+"'";
		flag = getColumnData("app_master","flag",where);
		plid = getColumnData("app_master","plid",where);

		//근태관리(외출계,출장신청,휴가원) 삭제
		if(flag.equals("OE_CHUL") || flag.equals("CHULJANG_SINCHEONG") || flag.equals("HYU_GA")) { 
			delete = "delete from geuntae_master where gt_id='"+plid+"'";
			stmt.executeUpdate(delete);
			
			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//보고관리(보고서,출장보고서) 삭제
		else if(flag.equals("BOGO") || flag.equals("CHULJANG_BOGO")) { 
			//본문및 첨부파일삭제
			where = "where bg_id='"+plid+"'";
			add_file = getColumnData("bogoseo_master","sname",where);
			user_id = getColumnData("bogoseo_master","user_id",where);
			appDeleteAttachFile(plid,"es",user_id,add_file);

			delete = "delete from bogoseo_master where bg_id='"+plid+"'";
			stmt.executeUpdate(delete);
			
			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//지원관리(기안서,명함,사유서,협조전) 삭제
		else if(flag.equals("GIAN") || flag.equals("MYEONGHAM") || flag.equals("SAYU") || flag.equals("HYEOPJO")) { 
			//본문및 첨부파일삭제
			where = "where jw_id='"+plid+"'";
			add_file = getColumnData("jiweon_master","sname",where);
			user_id = getColumnData("jiweon_master","user_id",where);
			appDeleteAttachFile(plid,"es",user_id,add_file);

			delete = "delete from jiweon_master where jw_id='"+plid+"'";
			stmt.executeUpdate(delete);

			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//인사관리(구인의뢰서) 삭제
		else if(flag.equals("GUIN")) { 
			delete = "delete from insa_master where is_id='"+plid+"'";
			stmt.executeUpdate(delete);

			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//교육관리(교육) 삭제
		else if(flag.equals("GYOYUK_ILJI")) { 
			//본문및 첨부파일삭제
			where = "where gy_id='"+plid+"'";
			add_file = getColumnData("gyoyuk_master","sname",where);
			user_id = getColumnData("gyoyuk_master","user_id",where);
			appDeleteAttachFile(plid,"es",user_id,add_file);

			delete = "delete from gyoyuk_master where gy_id='"+plid+"'";
			stmt.executeUpdate(delete);

			delete = "delete from gyoyuk_part where gy_id='"+plid+"'";
			stmt.executeUpdate(delete);

			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//모듈문서 삭제
		else {
			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		stmt.close();

	}
	/******************************************************************************
		양식문서 첨부파일 삭제하기
	******************************************************************************/
	public void appDeleteAttachFile(String plid,String ext_path,String id_path,String add_file) throws SQLException 
	{
		String bonmun_path="",addfile_path="",af="";

		com.anbtech.admin.db.ServerConfig config = new com.anbtech.admin.db.ServerConfig();
		String upload_path = config.getConf("upload_path");				//루트 PATH
		
		//본문파일 삭제하기
		bonmun_path = upload_path+"/"+ext_path+"/"+id_path+"/bonmun/"+plid;	
////System.out.println("b : " + bonmun_path);
		File BN = new File(bonmun_path);
		if(BN.exists()) BN.delete();

		//첨부파일 삭제하기
		if(add_file == null) add_file = "";
		StringTokenizer f = new StringTokenizer(add_file,"|");
		while(f.hasMoreTokens()) {
			af = f.nextToken().trim();
			addfile_path = upload_path+"/"+ext_path+"/"+id_path+"/addfile/"+af+".bin";
////System.out.println("a : " + addfile_path);
			File FN = new File(addfile_path);
			if(FN.exists()) FN.delete();
		}
	}

	/******************************************************************************
		상신취소하기
	******************************************************************************/
	public void appCancel(String pid) throws SQLException 
	{
		String where="", flag="",plid="",updata="",delete="";
		String form_paper = "HYU_GA,OE_CHUL,CHULJANG_SINCHEONG,BOGO,CHULJANG_BOGO,GIAN,";
			  form_paper += "MYEONGHAM,SAYU,HYEOPJO,GUIN,GYOYUK_ILJI,GEN,YEONGJANG";
		Statement stmt = null;
		stmt = con.createStatement();

		//양식문서의 종류구하기
		where = "where pid='"+pid+"'";
		flag = getColumnData("app_master","flag",where);
		plid = getColumnData("app_master","plid",where);

		//양식문서인 경우는 전자결재내에서 임시보관함으로 전환
		if(form_paper.indexOf(flag) != -1) {
			updata = "update app_master set app_state='APT' where pid='"+pid+"'";
			stmt.executeUpdate(updata);
		} else {
			//공지공문
			if(flag.equals("ODT")) {
				updata = "update OfficialDocument set flag='EC' where id='"+plid+"'";
				stmt.executeUpdate(updata);

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}
			//사내공문
			else if(flag.equals("IDS")) {
				updata = "update InDocument_send set flag='EC' where id='"+plid+"'";
				stmt.executeUpdate(updata);

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}
			//사외공문
			else if(flag.equals("ODS")) {
				updata = "update OutDocument_send set flag='EC' where id='"+plid+"'";
				stmt.executeUpdate(updata);

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}
			//BOM 구성
			else if(flag.equals("BOM")) {
				updata = "update mbom_master set bom_status='3' where pid='"+plid+"'";
				stmt.executeUpdate(updata);

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}
			//설계변경
			else if(flag.equals("DCM")) {
				String cpid = plid.substring(0,plid.indexOf("|"));			//설변관리번호
				where = "where pid='"+cpid+"'";
				String st = getColumnData("ecc_com","ecc_status",where);	//현진행상태
				if(st.equals("2")) {										//ECR결재중
					updata = "update ecc_com set edd_status='1' where pid='"+cpid+"'";
					stmt.executeUpdate(updata);
				} else if(st.equals("7")) {									//ECO결재중
					updata = "update ecc_com set edd_status='6' where pid='"+cpid+"'";
					stmt.executeUpdate(updata);
				}

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}

		}
		stmt.close();
	}

	/******************************************************************************
		전자결재의 특정컬럼을 수정하기
		 ex)견적서시 결재승인 관리번호바뀜
	******************************************************************************/
	private void updateColumn(String tablename,String column_name,String new_data,String pid) throws SQLException 
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String data = "update "+tablename+" set "+column_name+"='"+new_data+"' where pid='"+pid+"'";
		stmt.executeUpdate(data);
		stmt.close();
	}

	/******************************************************************************
		주어진 Table name에서 Column name의 값을 구하기
	******************************************************************************/
	private String getColumnData(String tablename,String column_name,String where) throws SQLException 
	{
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query= "SELECT "+column_name+" FROM "+tablename +" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString(column_name);
		stmt.close();
		rs.close();
		return data;
	}

	/******************************************************************************
		flag값 가져오기
	******************************************************************************/
	public String getFlag()
	{
		return this.doc_flag;
	}

	/******************************************************************************
		승인권자 ip address 
	******************************************************************************/
	public void setIPADDR(String ip_addr)
	{
		this.ip_addr = ip_addr;
	}

	/******************************************************************************
	// ID을 구하는 메소드
	******************************************************************************/
	private String getID()
	{
		String ID;
		
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		nmf.setFormat("000");		//일련번호 출력 형식(6자리)		
		ID = y + nmf.toDigits(Integer.parseInt(s));
		
		return ID;
	}
	/***************************************************************************
	 * 웹서버가 종료될때 자원을 회수하기 위한 메소드 
	 **************************************************************************/
	protected void finalize() throws Throwable 
	{
		

	}

}	