package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import com.anbtech.file.textFileReader;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;
import com.anbtech.dbconn.DBConnectionManager;

public class AppMasterDetailDAO
{
	private DBConnectionManager connMgr;
	private Connection con;
	private textFileReader text;

	private String line_doc="";			//결재선 읽기
	private String[] LINE_ORD;			//결재선 배열에 담기

	private String writer = "";			//기안자 사번
	private String writer_d = "";		//기안자 date
	private String writer_c = "";		//기안자 comment

	private String review = "";			//검토자 사번
	private String review_d = "";		//검토자 date
	private String review_c = "";		//검토자 comment

	private String decision ="";		//승인자 사번
	private String decision_d ="";		//승인자 date
	private String decision_c ="";		//승인자 comment

	private int agree_cnt = 0;			//합의자 총인원수
	private String[][] agree;			//협의자id, 일자, 코멘트

	private String receivers="";		//통보자 명단(id)
	private String[][] receiver;		//통보자id, 일자, 코멘트

	private String agree_method="";		//협조 진행방법은

	private String pid = "";			//관리번호
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public AppMasterDetailDAO(Connection con) 
	{
		this.con = con;
	}

	public AppMasterDetailDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}

	//*******************************************************************
	// APP_MASTER 에서 해당 PID의 내용만 가져오기 
	//*******************************************************************/	
	public ArrayList getTable_MasterPid (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		text = new textFileReader();

		//협의자 사번,일자,코멘트 담기 초기화
		agree = new String[10][3];
		for(int i=0; i<10; i++) for(int j=0; j<3; j++) agree[i][j]="";
		
		stmt = con.createStatement();
		TableAppMaster table = null;
		ArrayList table_list = new ArrayList();
			
		//query문장 만들기
		query = "SELECT * FROM APP_MASTER where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new TableAppMaster();
				this.pid = rs.getString("pid");	if(this.pid == null) this.pid = "";
				table.setAmPid(this.pid);									//pid
				table.setAmAppSubj(rs.getString("app_subj"));				//제목

				this.writer = rs.getString("writer");			if(this.writer == null) this.writer = "";
				table.setAmWriterName(rs.getString("writer_name"));			//작성자 이름
				this.writer_d = rs.getString("write_date");		if(this.writer_d == null) this.writer_d = "";
				this.writer_c = "";								if(this.writer_c == null) this.writer_c = "";
				table.setAmWriter(this.writer);								//작성자 사번
				table.setAmWriteDate(this.writer_d);						//작성 일자

				table.setAmAppStatus(rs.getString("app_state"));			//상태

				this.review = rs.getString("reviewer");			if(this.review == null) this.review = "";
				this.review_d = rs.getString("review_date");	if(this.review_d == null) this.review_d = "";
				this.review_c = rs.getString("review_comment");	if(this.review_c == null) this.review_c = "";
				table.setAmReviewer(this.review);							//검토자 사번
				table.setAmReviewDate(this.review_d);						//검토 일자
				table.setAmReviewComment(this.review_c);					//검토자 의견

				agree[0][0]=rs.getString("agree");agree[0][1]=rs.getString("agree_date");agree[0][2]=rs.getString("agree_comment");		
				for(int i=0; i<3; i++) if(agree[0][i] == null) agree[0][i]= "";
				table.setAmAgree(agree[0][0]);								//헙의자1 사번
				table.setAmAgreeDate(agree[0][1]);							//헙의자1 일자
				table.setAmAgreeComment(agree[0][2]);						//헙의자1 의견

				agree[1][0]=rs.getString("agree2");agree[1][1]=rs.getString("agree2_date");agree[1][2]=rs.getString("agree2_comment");
				for(int i=0; i<3; i++) if(agree[1][i] == null) agree[1][i]= "";
				table.setAmAgree2(agree[1][0]);								//헙의자2 사번
				table.setAmAgree2Date(agree[1][1]);							//헙의자2 일자
				table.setAmAgree2Comment(agree[1][2]);						//헙의자2 의견

				agree[2][0]=rs.getString("agree3");agree[2][1]=rs.getString("agree3_date");agree[2][2]=rs.getString("agree3_comment");
				for(int i=0; i<3; i++) if(agree[2][i] == null) agree[2][i]= "";
				table.setAmAgree3(agree[2][0]);								//헙의자3 사번
				table.setAmAgree3Date(agree[2][1]);							//헙의자3 일자
				table.setAmAgree3Comment(agree[2][2]);						//헙의자3 의견
				
				agree[3][0]=rs.getString("agree4");agree[3][1]=rs.getString("agree4_date");agree[3][2]=rs.getString("agree4_comment");
				for(int i=0; i<3; i++) if(agree[3][i] == null) agree[3][i]= "";
				table.setAmAgree4(agree[3][0]);								//헙의자4 사번
				table.setAmAgree4Date(agree[3][1]);							//헙의자4 일자
				table.setAmAgree4Comment(agree[3][2]);						//헙의자4 의견
				
				agree[4][0]=rs.getString("agree5");agree[4][1]=rs.getString("agree5_date");agree[4][2]=rs.getString("agree5_comment");
				for(int i=0; i<3; i++) if(agree[4][i] == null) agree[4][i]= "";
				table.setAmAgree5(agree[4][0]);								//헙의자5 사번
				table.setAmAgree5Date(agree[4][1]);							//헙의자5 일자
				table.setAmAgree5Comment(agree[4][2]);						//헙의자5 의견
				
				agree[5][0]=rs.getString("agree6");agree[5][1]=rs.getString("agree6_date");agree[5][2]=rs.getString("agree6_comment");
				for(int i=0; i<3; i++) if(agree[5][i] == null) agree[5][i]= "";
				table.setAmAgree6(agree[5][0]);								//헙의자6 사번
				table.setAmAgree6Date(agree[5][1]);							//헙의자6 일자
				table.setAmAgree6Comment(agree[5][2]);						//헙의자6 의견
				
				agree[6][0]=rs.getString("agree7");agree[6][1]=rs.getString("agree7_date");agree[6][2]=rs.getString("agree7_comment");
				for(int i=0; i<3; i++) if(agree[6][i] == null) agree[6][i]= "";
				table.setAmAgree7(agree[6][0]);								//헙의자7 사번
				table.setAmAgree7Date(agree[6][1]);							//헙의자7 일자
				table.setAmAgree7Comment(agree[6][2]);						//헙의자7 의견
				
				agree[7][0]=rs.getString("agree8");agree[7][1]=rs.getString("agree8_date");agree[7][2]=rs.getString("agree8_comment");
				for(int i=0; i<3; i++) if(agree[7][i] == null) agree[7][i]= "";
				table.setAmAgree8(agree[7][0]);								//헙의자8 사번
				table.setAmAgree8Date(agree[7][1]);							//헙의자8 일자
				table.setAmAgree8Comment(agree[7][2]);						//헙의자8 의견
				
				agree[8][0]=rs.getString("agree9");agree[8][1]=rs.getString("agree9_date");agree[8][2]=rs.getString("agree9_comment");
				for(int i=0; i<3; i++) if(agree[8][i] == null) agree[8][i]= "";
				table.setAmAgree9(agree[8][0]);								//헙의자9 사번
				table.setAmAgree9Date(agree[8][1]);							//헙의자9 일자
				table.setAmAgree9Comment(agree[8][2]);						//헙의자9 의견
				
				agree[9][0]=rs.getString("agree10");agree[9][1]=rs.getString("agree10_date");agree[9][2]=rs.getString("agree10_comment");
				for(int i=0; i<3; i++) if(agree[9][i] == null) agree[9][i]= "";
				table.setAmAgree10(agree[9][0]);							//헙의자10 사번
				table.setAmAgree10Date(agree[9][1]);						//헙의자10 일자
				table.setAmAgree10Comment(agree[9][2]);						//헙의자10 의견
				
				this.agree_method = rs.getString("agree_method");
				table.setAmAgreeMethod(this.agree_method);					//헙의절차 (Serial Parallel)
				String ag_cnt = rs.getString("agree_count");
				if(ag_cnt != null)
					this.agree_cnt = Integer.parseInt(ag_cnt); 
				table.setAmAgreeCount(Integer.toString(this.agree_cnt));	//협의자 총원수 

				table.setAmAgreePass(rs.getString("agree_pass"));			//현 합의자 수 (parallel의 경우)

				this.decision = rs.getString("decision");			if(this.decision == null) this.decision = "";
				this.decision_d = rs.getString("decision_date");	if(this.decision_d == null) this.decision_d = "";
				this.decision_c = rs.getString("decision_comment");	if(this.decision_c == null) this.decision_c = "";
				table.setAmDecision(this.decision);							//승인자 사번
				table.setAmDecisionDate(this.decision_d);					//승인 일자
				table.setAmDecisionComment(this.decision_c);				//승인자 의견
				
				this.receivers = rs.getString("receivers");			if(this.receivers == null) this.receivers = "";
				table.setAmReceivers(this.receivers);						//통보자 사번 or 부서코드

				this.line_doc = rs.getString("app_line");			if(this.line_doc == null) this.line_doc = "";		
				table.setAmAppLine(this.line_doc);							//결재 순서

				String bon_path = rs.getString("bon_path");
				table.setAmBonPath(bon_path);								//본문path
				String bon_file = rs.getString("bon_file");
				table.setAmBonFile(bon_file);								//본문파일명

				String ao1 = rs.getString("add_1_original");			if(ao1 == null) ao1 = "";
				String af1 = rs.getString("add_1_file");				if(af1 == null) af1 = "";
				String ao2 = rs.getString("add_2_original");			if(ao2 == null) ao2 = "";
				String af2 = rs.getString("add_2_file");				if(af2 == null) af2 = "";
				String ao3 = rs.getString("add_3_original");			if(ao3 == null) ao3 = "";
				String af3 = rs.getString("add_3_file");				if(af3 == null) af3 = "";
				table.setAmAdd1Original(ao1);								//첨부파일 1 original
				table.setAmAdd1File(af1);									//첨부파일 1
				table.setAmAdd2Original(ao2);								//첨부파일 2 original
				table.setAmAdd2File(af2);									//첨부파일 2
				table.setAmAdd3Original(ao3);								//첨부파일 3 original
				table.setAmAdd3File(af3);									//첨부파일 3

				String sp = rs.getString("save_period");			if(sp == null) sp = "";
				table.setAmSavePeriod(sp);									//보존기간
				String sl = rs.getString("security_level");			if(sl == null) sl = "";
				table.setAmSecurityLevel(sl);								//보안등급
				String pd = rs.getString("plid");					if(pd == null) pd = "";
				table.setAmPlid(pd);										//기타문서 관리번호
				String fg = rs.getString("flag");					if(fg == null) fg = "";
				table.setAmFlag(fg);										//기타문서 종류

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// APP_MASTER 에서 해당 PID의 내용만 가져오기 
	//*******************************************************************/	
	public ArrayList getTable_MasterPid (String pid,String tablename) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		text = new textFileReader();

		//협의자 사번,일자,코멘트 담기 초기화
		agree = new String[10][3];
		for(int i=0; i<10; i++) for(int j=0; j<3; j++) agree[i][j]="";
		
		stmt = con.createStatement();
		TableAppMaster table = null;
		ArrayList table_list = new ArrayList();
			
		//query문장 만들기
		query = "SELECT * FROM "+tablename+" where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new TableAppMaster();
				this.pid = rs.getString("pid");	if(this.pid == null) this.pid = "";
				table.setAmPid(this.pid);									//pid
				table.setAmAppSubj(rs.getString("app_subj"));				//제목

				this.writer = rs.getString("writer");			if(this.writer == null) this.writer = "";
				table.setAmWriterName(rs.getString("writer_name"));			//작성자 이름
				this.writer_d = rs.getString("write_date");		if(this.writer_d == null) this.writer_d = "";
				this.writer_c = "";								if(this.writer_c == null) this.writer_c = "";
				table.setAmWriter(this.writer);								//작성자 사번
				table.setAmWriteDate(this.writer_d);						//작성 일자

				table.setAmAppStatus(rs.getString("app_state"));			//상태

				this.review = rs.getString("reviewer");			if(this.review == null) this.review = "";
				this.review_d = rs.getString("review_date");	if(this.review_d == null) this.review_d = "";
				this.review_c = rs.getString("review_comment");	if(this.review_c == null) this.review_c = "";
				table.setAmReviewer(this.review);							//검토자 사번
				table.setAmReviewDate(this.review_d);						//검토 일자
				table.setAmReviewComment(this.review_c);					//검토자 의견

				agree[0][0]=rs.getString("agree");agree[0][1]=rs.getString("agree_date");agree[0][2]=rs.getString("agree_comment");		
				for(int i=0; i<3; i++) if(agree[0][i] == null) agree[0][i]= "";
				table.setAmAgree(agree[0][0]);								//헙의자1 사번
				table.setAmAgreeDate(agree[0][1]);							//헙의자1 일자
				table.setAmAgreeComment(agree[0][2]);						//헙의자1 의견

				agree[1][0]=rs.getString("agree2");agree[1][1]=rs.getString("agree2_date");agree[1][2]=rs.getString("agree2_comment");
				for(int i=0; i<3; i++) if(agree[1][i] == null) agree[1][i]= "";
				table.setAmAgree2(agree[1][0]);								//헙의자2 사번
				table.setAmAgree2Date(agree[1][1]);							//헙의자2 일자
				table.setAmAgree2Comment(agree[1][2]);						//헙의자2 의견

				agree[2][0]=rs.getString("agree3");agree[2][1]=rs.getString("agree3_date");agree[2][2]=rs.getString("agree3_comment");
				for(int i=0; i<3; i++) if(agree[2][i] == null) agree[2][i]= "";
				table.setAmAgree3(agree[2][0]);								//헙의자3 사번
				table.setAmAgree3Date(agree[2][1]);							//헙의자3 일자
				table.setAmAgree3Comment(agree[2][2]);						//헙의자3 의견
				
				agree[3][0]=rs.getString("agree4");agree[3][1]=rs.getString("agree4_date");agree[3][2]=rs.getString("agree4_comment");
				for(int i=0; i<3; i++) if(agree[3][i] == null) agree[3][i]= "";
				table.setAmAgree4(agree[3][0]);								//헙의자4 사번
				table.setAmAgree4Date(agree[3][1]);							//헙의자4 일자
				table.setAmAgree4Comment(agree[3][2]);						//헙의자4 의견
				
				agree[4][0]=rs.getString("agree5");agree[4][1]=rs.getString("agree5_date");agree[4][2]=rs.getString("agree5_comment");
				for(int i=0; i<3; i++) if(agree[4][i] == null) agree[4][i]= "";
				table.setAmAgree5(agree[4][0]);								//헙의자5 사번
				table.setAmAgree5Date(agree[4][1]);							//헙의자5 일자
				table.setAmAgree5Comment(agree[4][2]);						//헙의자5 의견
				
				agree[5][0]=rs.getString("agree6");agree[5][1]=rs.getString("agree6_date");agree[5][2]=rs.getString("agree6_comment");
				for(int i=0; i<3; i++) if(agree[5][i] == null) agree[5][i]= "";
				table.setAmAgree6(agree[5][0]);								//헙의자6 사번
				table.setAmAgree6Date(agree[5][1]);							//헙의자6 일자
				table.setAmAgree6Comment(agree[5][2]);						//헙의자6 의견
				
				agree[6][0]=rs.getString("agree7");agree[6][1]=rs.getString("agree7_date");agree[6][2]=rs.getString("agree7_comment");
				for(int i=0; i<3; i++) if(agree[6][i] == null) agree[6][i]= "";
				table.setAmAgree7(agree[6][0]);								//헙의자7 사번
				table.setAmAgree7Date(agree[6][1]);							//헙의자7 일자
				table.setAmAgree7Comment(agree[6][2]);						//헙의자7 의견
				
				agree[7][0]=rs.getString("agree8");agree[7][1]=rs.getString("agree8_date");agree[7][2]=rs.getString("agree8_comment");
				for(int i=0; i<3; i++) if(agree[7][i] == null) agree[7][i]= "";
				table.setAmAgree8(agree[7][0]);								//헙의자8 사번
				table.setAmAgree8Date(agree[7][1]);							//헙의자8 일자
				table.setAmAgree8Comment(agree[7][2]);						//헙의자8 의견
				
				agree[8][0]=rs.getString("agree9");agree[8][1]=rs.getString("agree9_date");agree[8][2]=rs.getString("agree9_comment");
				for(int i=0; i<3; i++) if(agree[8][i] == null) agree[8][i]= "";
				table.setAmAgree9(agree[8][0]);								//헙의자9 사번
				table.setAmAgree9Date(agree[8][1]);							//헙의자9 일자
				table.setAmAgree9Comment(agree[8][2]);						//헙의자9 의견
				
				agree[9][0]=rs.getString("agree10");agree[9][1]=rs.getString("agree10_date");agree[9][2]=rs.getString("agree10_comment");
				for(int i=0; i<3; i++) if(agree[9][i] == null) agree[9][i]= "";
				table.setAmAgree10(agree[9][0]);							//헙의자10 사번
				table.setAmAgree10Date(agree[9][1]);						//헙의자10 일자
				table.setAmAgree10Comment(agree[9][2]);						//헙의자10 의견
				
				this.agree_method = rs.getString("agree_method");
				table.setAmAgreeMethod(this.agree_method);					//헙의절차 (Serial Parallel)
				String ag_cnt = rs.getString("agree_count");
				if(ag_cnt != null)
					this.agree_cnt = Integer.parseInt(ag_cnt); 
				table.setAmAgreeCount(Integer.toString(this.agree_cnt));	//협의자 총원수 

				table.setAmAgreePass(rs.getString("agree_pass"));			//현 합의자 수 (parallel의 경우)

				this.decision = rs.getString("decision");			if(this.decision == null) this.decision = "";
				this.decision_d = rs.getString("decision_date");	if(this.decision_d == null) this.decision_d = "";
				this.decision_c = rs.getString("decision_comment");	if(this.decision_c == null) this.decision_c = "";
				table.setAmDecision(this.decision);							//승인자 사번
				table.setAmDecisionDate(this.decision_d);					//승인 일자
				table.setAmDecisionComment(this.decision_c);				//승인자 의견
				
				this.receivers = rs.getString("receivers");			if(this.receivers == null) this.receivers = "";
				table.setAmReceivers(this.receivers);						//통보자 사번 or 부서코드

				this.line_doc = rs.getString("app_line");			if(this.line_doc == null) this.line_doc = "";		
				table.setAmAppLine(this.line_doc);							//결재 순서

				String bon_path = rs.getString("bon_path");
				table.setAmBonPath(bon_path);								//본문path
				String bon_file = rs.getString("bon_file");
				table.setAmBonFile(bon_file);								//본문파일명

				String ao1 = rs.getString("add_1_original");			if(ao1 == null) ao1 = "";
				String af1 = rs.getString("add_1_file");				if(af1 == null) af1 = "";
				String ao2 = rs.getString("add_2_original");			if(ao2 == null) ao2 = "";
				String af2 = rs.getString("add_2_file");				if(af2 == null) af2 = "";
				String ao3 = rs.getString("add_3_original");			if(ao3 == null) ao3 = "";
				String af3 = rs.getString("add_3_file");				if(af3 == null) af3 = "";
				table.setAmAdd1Original(ao1);								//첨부파일 1 original
				table.setAmAdd1File(af1);									//첨부파일 1
				table.setAmAdd2Original(ao2);								//첨부파일 2 original
				table.setAmAdd2File(af2);									//첨부파일 2
				table.setAmAdd3Original(ao3);								//첨부파일 3 original
				table.setAmAdd3File(af3);									//첨부파일 3

				String sp = rs.getString("save_period");			if(sp == null) sp = "";
				table.setAmSavePeriod(sp);									//보존기간
				String sl = rs.getString("security_level");			if(sl == null) sl = "";
				table.setAmSecurityLevel(sl);								//보안등급
				String pd = rs.getString("plid");					if(pd == null) pd = "";
				table.setAmPlid(pd);										//기타문서 관리번호
				String fg = rs.getString("flag");					if(fg == null) fg = "";
				table.setAmFlag(fg);										//기타문서 종류

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	결재순서 알아보기 (APL,APGx,APL,API)
	//*******************************************************************/	
	private void getLineOrder () 
	{
		//배열 크기 찾기
		int cnt = 0;
		if(this.line_doc.length() > 0) {
			cnt++;
			for(int i=0; i<this.line_doc.length(); i++) if(this.line_doc.charAt(i) == ',') cnt++;
		}
		//System.out.println("결재순서 : " + this.line_doc + " : " + cnt);

		//결재선 배열에 담기
		LINE_ORD = new String[cnt];			
		StringTokenizer Rlist = new StringTokenizer(line_doc,",");
		int Ri = 0;
		while(Rlist.hasMoreTokens()) {
			LINE_ORD[Ri] = Rlist.nextToken();
			//System.out.println("결재순서 개별배열 : " + LINE_ORD[Ri] + " : " + Ri);
			Ri++;
		}
	}

	//*******************************************************************
	//	결재 통보자 명단 알아보기
	//*******************************************************************/
	private void getReceivers() throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;

		//통보인원 배열 크기 찾기 (부서통보도 하나로 간주)
		//System.out.println("통보자 명단 : " + this.receivers);
		int cnt = 0;
		if(this.receivers.length() > 0) {
			cnt++;
			for(int i=0; i<this.receivers.length(); i++) if(this.receivers.charAt(i) == ',') cnt++;
		} else return;
		
		//통보자 사번,일자,코멘트 담기 초기화 (결재완료전 통보자 수 : 부서통보시 부서인원 미적용)
		//receiver = new String[cnt][3];
		//for(int i=0; i<cnt; i++) for(int j=0; j<3; j++) receiver[i][j]="";

		//공통 항목
		stmt = con.createStatement();

		String cnt_query = "SELECT COUNT(*) FROM APP_RECEIVE where pid ='"+this.pid+"'";
		rs = stmt.executeQuery(cnt_query);

		if(rs.next()) {
			//결재된 문서일 때
			int api_cnt = rs.getInt(1);		//통보함에 저장된 총수량
			if(api_cnt > 0) {
				receiver = new String[api_cnt][3];	//부서 + 개인별 통보로 다시배열크기 조절함.
				for(int i=0; i<api_cnt; i++) for(int j=0; j<3; j++) receiver[i][j]="";
				String query = "SELECT receiver,read_date,isopen FROM APP_RECEIVE where pid='"+this.pid+"'";
				rs = stmt.executeQuery(query);

				int Ri = 0;
				while(rs.next()) {
					receiver[Ri][0] = rs.getString("receiver");		if(receiver[Ri][0] == null) receiver[Ri][0] = "";
					receiver[Ri][1] = rs.getString("read_date");	if(receiver[Ri][1] == null) receiver[Ri][1] = "";
					String isRead = rs.getString("isopen");
					if(isRead.equals("1")) receiver[Ri][2] = "확인함";
					else receiver[Ri][2] = "";
					//System.out.println("결재된 통보자 명단 배열 : " + receiver[Ri][0] + " : " + Ri);
					Ri++;
				}
			} 
			//결재중인 문서일 때
			else {	
				receiver = new String[cnt][3];		//개인통보 (부서도 하나의 개인간주)	
				for(int i=0; i<cnt; i++) for(int j=0; j<3; j++) receiver[i][j]="";
				StringTokenizer Rlist = new StringTokenizer(receivers,",");
				int Ri = 0;
				while(Rlist.hasMoreTokens()) {
					receiver[Ri][0] = Rlist.nextToken();	if(receiver[Ri][0] == null) receiver[Ri][0] = "";
					receiver[Ri][1] = "";
					receiver[Ri][2] = "";
					//System.out.println("결재중 통보자 명단 배열 : " + receiver[Ri][0] + " : " + Ri);
					Ri++;
				}
			}
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
				
	}	
	//*******************************************************************
	// APP_MASTER 에서 해당 PID의 결재선 가져오기 
	//*******************************************************************/	
	public ArrayList getTable_line() throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		String tablename = "";
		String where = "";

		//결재선및 통보자 읽기
		getLineOrder();						//결재선 가져오기
		getReceivers();						//통보자 읽기

		stmt = con.createStatement();
		TableAppLine table = null;
		ArrayList table_list = new ArrayList();
				
		//결재순서대로 결재선 만들기 (권한 사번 이름 직급 부서 일자 코멘트)
		//1. 기안자
		tablename="USER_TABLE a,CLASS_TABLE b,RANK_TABLE c ";
		where = "where (a.id='"+this.writer+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
		query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
		
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			table = new TableAppLine();
			table.setApStatus("기안");						//GIAN
			table.setApSabun(this.writer);					//사번
			table.setApName(rs.getString("name"));			//이름
			table.setApRank(rs.getString("ar_name"));		//직급
			table.setApDivision(rs.getString("ac_name"));	//부서명
			table.setApDate(this.writer_d);					//일자
			table.setApComment(this.writer_c);				//코멘트
			table_list.add(table);
		}

		//2. 기타 나머지
		int cnt = LINE_ORD.length;
		for(int i = 0; i < cnt; i++) {
			//검토 단계
			if(LINE_ORD[i].equals("APV")) {
				where = "where (a.id='"+this.review+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("검토");				//APV
					table.setApSabun(this.review);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.review_d);
					table.setApComment(this.review_c);	
					table_list.add(table);
				}
			}

			//승인 단계
			else if(LINE_ORD[i].equals("APL")) {
				where = "where (a.id='"+this.decision+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("승인");				//APL
					table.setApSabun(this.decision);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.decision_d);
					table.setApComment(this.decision_c);
					table_list.add(table);
				}
			}


			//합의 단계 1 (입력한 순서대로 출력키위해 모두 풀어서 작성함)
			else if(LINE_ORD[i].equals("APG")) {
				if(this.agree_method.equals("PARALLEL")) {
					for(int n=0,m=0; n<10; n++) {					//병렬진행때문에 처리
						if(agree[m][0].length() == 0) break;		//병렬진행때문에 처리
						where = "where (a.id='"+agree[m][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
						query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
						////System.out.println(query);
						rs = stmt.executeQuery(query);
						if(rs.next()) {
							table = new TableAppLine();
							table.setApStatus("협조");					//APG
							table.setApSabun(agree[m][0]);
							table.setApName(rs.getString("name"));
							table.setApRank(rs.getString("ar_name"));
							table.setApDivision(rs.getString("ac_name"));
							table.setApDate(this.agree[m][1]);
							table.setApComment(this.agree[m][2]);
							table_list.add(table);
						} 
						m++;
					}//for
				} else {
						where = "where (a.id='"+agree[0][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
						query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
						////System.out.println(query);
						rs = stmt.executeQuery(query);
						if(rs.next()) {
							table = new TableAppLine();
							table.setApStatus("협조");					//APG
							table.setApSabun(agree[0][0]);
							table.setApName(rs.getString("name"));
							table.setApRank(rs.getString("ar_name"));
							table.setApDivision(rs.getString("ac_name"));
							table.setApDate(this.agree[0][1]);
							table.setApComment(this.agree[0][2]);
							table_list.add(table);
						} 
				} //if
			}

			//합의 단계 2
			else if(LINE_ORD[i].equals("APG2")) {
				where = "where (a.id='"+agree[1][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("협조");					//APG
					table.setApSabun(agree[1][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[1][1]);
					table.setApComment(this.agree[1][2]);
					table_list.add(table);
				}
			}
			//합의 단계 3
			else if(LINE_ORD[i].equals("APG3")) {
				where = "where (a.id='"+agree[2][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("협조");					//APG
					table.setApSabun(agree[2][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[2][1]);
					table.setApComment(this.agree[2][2]);
					table_list.add(table);
				}
			}
			//합의 단계 4
			else if(LINE_ORD[i].equals("APG4")) {
				where = "where (a.id='"+agree[3][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("협조");					//APG
					table.setApSabun(agree[3][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[3][1]);
					table.setApComment(this.agree[3][2]);
					table_list.add(table);
				}
			}
			//합의 단계 5
			else if(LINE_ORD[i].equals("APG5")) {
					where = "where (a.id='"+agree[4][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("협조");					//APG
					table.setApSabun(agree[4][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[4][1]);
					table.setApComment(this.agree[4][2]);
					table_list.add(table);
				}
			}
			//합의 단계 6
			else if(LINE_ORD[i].equals("APG6")) {
				where = "where (a.id='"+agree[5][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("협조");					//APG
					table.setApSabun(agree[5][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[5][1]);
					table.setApComment(this.agree[5][2]);
					table_list.add(table);
				}
			}
			//합의 단계 7
			else if(LINE_ORD[i].equals("APG7")) {
				where = "where (a.id='"+agree[6][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("협조");					//APG
					table.setApSabun(agree[6][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[6][1]);
					table.setApComment(this.agree[6][2]);
					table_list.add(table);
				}
			}
			//합의 단계 8
			else if(LINE_ORD[i].equals("APG8")) {
				where = "where (a.id='"+agree[7][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("협조");					//APG
					table.setApSabun(agree[7][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[7][1]);
					table.setApComment(this.agree[7][2]);
					table_list.add(table);
				}
			}
			//합의 단계 9
			else if(LINE_ORD[i].equals("APG9")) {
					where = "where (a.id='"+agree[8][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("협조");					//APG
					table.setApSabun(agree[8][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[8][1]);
					table.setApComment(this.agree[8][2]);
					table_list.add(table);
				}
			}
			//합의 단계 10
			else if(LINE_ORD[i].equals("APG10")) {
				where = "where (a.id='"+agree[9][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("협조");					//APG
					table.setApSabun(agree[9][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[9][1]);
					table.setApComment(this.agree[9][2]);
					table_list.add(table);
				}
			}

			//통보 단계 (부서통보시 user_table에 구성원이 없으면 통보는 출력되지 않음)
			else if(LINE_ORD[i].equals("API")) {
				int rec_cnt = receiver.length;
				//System.out.println("결재선 배열 통보자수 : " + rec_cnt);
				for(int r=0; r<rec_cnt; r++) {
					//사용자 통보
					where = "where (a.id='"+receiver[r][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
					query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
					////System.out.println("사용자통보 결재선 : " + query);
					rs = stmt.executeQuery(query);
					if(rs.next()) {
						table = new TableAppLine();
						table.setApStatus("통보");				//API
						table.setApSabun(receiver[r][0]);
						table.setApName(rs.getString("name"));
						table.setApRank(rs.getString("ar_name"));
						table.setApDivision(rs.getString("ac_name"));
						table.setApDate(this.receiver[r][1]);
						table.setApComment(this.receiver[r][2]);
						table_list.add(table);
					}

					//부서 통보
					else {
						tablename="CLASS_TABLE ";		//부서만 쿼리할때
						where = "where (ac_id = '"+receiver[r][0]+"')";
						query = "SELECT ac_name FROM "+tablename+where;	
						////System.out.println("사용자통보 부서 결재선 : "+query);
						rs = stmt.executeQuery(query);
						if(rs.next()) {
							String ac_name = rs.getString("ac_name");
							table = new TableAppLine();
							table.setApStatus("통보");				//API
							table.setApSabun(receiver[r][0]);
							table.setApName("부서통보");
							table.setApRank("부서원");
							table.setApDivision(ac_name);
							table.setApDate(this.receiver[r][1]);
							table.setApComment(this.receiver[r][2]);
							table_list.add(table);
						}
						tablename="USER_TABLE a,CLASS_TABLE b,RANK_TABLE c ";	//원복시킴
					}
				} //for
			}

		}//for

		//공통 항목 끝내기 (닫기)
		rs.close();
		stmt.close();
		return table_list;
	}	

	//*******************************************************************
	// 전자결재 첨부파일 삭제하기
	//*******************************************************************/	
	public void deleteFile(String pid,String File1,String File2,String File3) throws Exception
	{
		//파일 삭제 진행
		text = new textFileReader();
		if(pid == null) pid = "";
		if(File1 == null) File1 = "";
		if(File2 == null) File2 = "";
		if(File3 == null) File3 = "";

		//DB Update 진행
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		if(File1.length() != 0) {
			update = "UPDATE APP_MASTER set add_1_original='', add_1_file='' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			//System.out.println(update);
			//System.out.println(File1);
			text.delFilename(File1);	//해당 파일삭제 하기
		}

		if(File2.length() != 0) {
			update = "UPDATE APP_MASTER set add_2_original='', add_2_file='' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			text.delFilename(File2);	//해당 파일삭제 하기
		}

		if(File3.length() != 0) {
			update = "UPDATE APP_MASTER set add_3_original='', add_3_file='' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			text.delFilename(File3);	//해당 파일삭제 하기
		}
		stmt.close();
		
	}
}