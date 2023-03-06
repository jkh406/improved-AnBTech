<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "전자결재 본문보기"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
%>

<%
	//-----------------------------------
	//	변수 선언
	//-----------------------------------
	// 읽은문서 변수로 받기
	String doc_pid="";			//관리번호
	String doc_lin="";			//읽은문서 결재선
	String send_line = "";		//결재선 양식결재로 넘겨주기
	String doc_sub="";			//읽은문서 제목
	String doc_ste="";			//읽은문서 현 결재단계
	String doc_per="";			//읽은문서 보존기간
	String doc_sec="";			//읽은문서 보존등급
	String doc_bon="";			//읽은문서 본문내용
	String doc_or1="";			//읽은문서 첨부 원래이름1
	String doc_ad1="";			//읽은문서 첨부 저장이름1
	String doc_or2="";			//읽은문서 첨부 원래이름2
	String doc_ad2="";			//읽은문서 첨부 저장이름2
	String doc_or3="";			//읽은문서 첨부 원래이름3
	String doc_ad3="";			//읽은문서 첨부 저장이름3
	String doc_path="";			//읽은문서 관련 path

	String lid="";				//기타문서 전자결재의뢰 관리번호
	String doc_flag="";			//관련정보 종류(SERVICE:고객서비스, BOM:PartList 등)
	String bon_path = "";		//본문내용 path
	String file1_path = "";		//첨부파일1 path
	String file2_path = "";		//첨부파일2 path
	String file3_path = "";		//첨부파일3 path
	String file1_size = "";		//첨부파일1 크기
	String file2_size = "";		//첨부파일2 크기
	String file3_size = "";		//첨부파일3 크기

	//기안/검토/승인자 이름찾기
	String wid = "";			//기안자사번
	String vid = "";			//검토자사번
	String did = "";			//승인자사번
	String wname = "";			//기안자
	String vname = "";			//검토자
	String dname = "";			//승인자
	String vcomm = "";			//검토자 코멘트 
	String dcomm = "";			//승인자 코멘트 
	String vdate = "";			//검토자 검토 일자
	String ddate = "";			//승인자 승인 일자 
	String PROCESS_NAME = "";	//process이름

	//-----------------------------------
	//	전자결재 내용 & 기타 변수 받기
	//-----------------------------------
	String PROCESS = request.getParameter("PROCESS");	//결재함 상태

	if(PROCESS.equals("APP_ING")) PROCESS_NAME = "미결함";
	else if(PROCESS.equals("ASK_ING")) PROCESS_NAME = "진행함";
	else if(PROCESS.equals("APP_BOX")) PROCESS_NAME = "기결함";
	else if(PROCESS.equals("APP_GEN")) PROCESS_NAME = "기결함 (일반문서)";
	else if(PROCESS.equals("APP_SER")) PROCESS_NAME = "기결함 (고객관리)";
	else if(PROCESS.equals("REJ_BOX")) PROCESS_NAME = "반려함";
	else if(PROCESS.equals("TMP_BOX")) PROCESS_NAME = "저장함";
	else if(PROCESS.equals("SEE_BOX")) PROCESS_NAME = "통보함";
	else if(PROCESS.equals("DEL_BOX")) PROCESS_NAME = "삭제함";	
	else if(PROCESS.equals("APP_PNT")) PROCESS_NAME = "출력하기";
	else if(PROCESS.equals("APP_LNK")) PROCESS_NAME = "관련문서보기";
	else PROCESS_NAME = "기결함 (모든양식결재)";

	//app_master에서 데이터 읽기
	if(PROCESS.equals("APP_ING") || (PROCESS.equals("ASK_ING")) || (PROCESS.equals("REJ_BOX")) || (PROCESS.equals("TMP_BOX")) || PROCESS.equals("APP_PNT")) {
		com.anbtech.gw.entity.TableAppMaster table = new TableAppMaster();	
		ArrayList table_list = new ArrayList();

		table_list = (ArrayList)request.getAttribute("Table_List");
		Iterator table_iter = table_list.iterator();
		while(table_iter.hasNext()) {
			 table = (TableAppMaster)table_iter.next();	
			
			 doc_pid=table.getAmPid();					//관리번호
			 doc_sub=table.getAmAppSubj();				//제목
			 doc_ste=table.getAmAppStatus();			//현 결재단계
			 doc_per=table.getAmSavePeriod();			//보존기간
			 doc_sec=table.getAmSecurityLevel();		//보존등급
			 doc_path=table.getAmBonPath();				//문서 Path

			 doc_or1=table.getAmAdd1Original();			//첨부 원래이름1
			 doc_ad1=table.getAmAdd1File();				//첨부 저장이름1
			 file1_path = upload_path + doc_path + "/addfile/" + doc_ad1;

			 File fn1 = new File(file1_path);
			 file1_size = Long.toString(fn1.length());

			 doc_or2=table.getAmAdd2Original();			//첨부 원래이름2
			 doc_ad2=table.getAmAdd2File();				//첨부 저장이름2
			 file2_path = upload_path + doc_path + "/addfile/" + doc_ad2;
			 File fn2 = new File(file2_path);
			 file2_size = Long.toString(fn2.length());

			 doc_or3=table.getAmAdd3Original();			//첨부 원래이름3
			 doc_ad3=table.getAmAdd3File();				//첨부 저장이름3
			 file3_path = upload_path + doc_path + "/addfile/" + doc_ad3;
			 File fn3 = new File(file3_path);
			 file3_size = Long.toString(fn3.length());

			 lid=table.getAmPlid();						//기타문서 관리번호
			 doc_flag=table.getAmFlag();				//관련정보종류
			 wid = table.getAmWriter();					//기안자사번
			 vid = table.getAmReviewer();				//검토자사번
			 did = table.getAmDecision();				//승인자사번

			 //본문내용 읽기
			 com.anbtech.file.textFileReader text = new textFileReader();
			 bon_path = upload_path + doc_path + "/bonmun/" + table.getAmBonFile();
			 doc_bon = text.getFileString(bon_path);	//읽은문서 본문내용 
		} //while
	} 
	//app_save에서 데이터 읽기
	else {
		com.anbtech.gw.entity.TableAppSave tableS = new TableAppSave();	
		ArrayList table_list = new ArrayList();

		table_list = (ArrayList)request.getAttribute("Table_List");
		Iterator table_iter = table_list.iterator();
		while(table_iter.hasNext()) {
			 tableS = (TableAppSave)table_iter.next();	

			 doc_pid=tableS.getAmPid();					//관리번호
			 doc_sub=tableS.getAmAppSubj();				//제목
			 doc_ste=tableS.getAmAppStatus();			//현 결재단계
			 doc_per=tableS.getAmSavePeriod();			//보존기간
			 doc_sec=tableS.getAmSecurityLevel();		//보존등급
			 doc_path=tableS.getAmBonPath();			//문서 Path
			 doc_or1=tableS.getAmAdd1Original();		//첨부 원래이름1
			 doc_ad1=tableS.getAmAdd1File();			//첨부 저장이름1
			 file1_path = upload_path + doc_path + "/addfile/" + doc_ad1;
			 File fn1 = new File(file1_path);
			 file1_size = Long.toString(fn1.length());

			 doc_or2=tableS.getAmAdd2Original();		//첨부 원래이름2
			 doc_ad2=tableS.getAmAdd2File();			//첨부 저장이름2
			 file2_path = upload_path + doc_path + "/addfile/" + doc_ad2;
			 File fn2 = new File(file2_path);
			 file2_size = Long.toString(fn2.length());

			 doc_or3=tableS.getAmAdd3Original();		//첨부 원래이름3
			 doc_ad3=tableS.getAmAdd3File();			//첨부 저장이름3
			 file3_path = upload_path + doc_path + "/addfile/" + doc_ad3;
			 File fn3 = new File(file3_path);
			 file3_size = Long.toString(fn3.length());

			 lid=tableS.getAmPlid();					//기타문서 관리번호
			 doc_flag=tableS.getAmFlag();				//관련정보종류
			 wid = tableS.getAmWriter();				//기안자사번
			 vid = tableS.getAmReviewer();				//검토자사번
			 did = tableS.getAmDecision();				//승인자사번

			 //본문내용 읽기
			 com.anbtech.file.textFileReader text = new textFileReader();
			 bon_path = upload_path + doc_path + "/bonmun/" + tableS.getAmBonFile();
			 doc_bon = text.getFileString(bon_path);	//읽은문서 본문내용 
		} //while

	} //if

	//결재함 내용 읽기
	com.anbtech.gw.entity.TableAppLine line = new TableAppLine();			
	ArrayList table_line = new ArrayList();

	table_line = (ArrayList)request.getAttribute("Table_Line");
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		line = (TableAppLine)line_iter.next();
									
		if(line.getApStatus().equals("기안"))  wname = line.getApName();	//기안자
		if(line.getApStatus().equals("검토"))  vname = line.getApName();	//검토자
		if(line.getApStatus().equals("승인"))  dname = line.getApName();	//승인자
		if(line.getApStatus().equals("검토")) {
			 vcomm = line.getApComment();	//검토자코멘트 
			 vdate = line.getApDate();		//검토자 검토일자 (있으면 결재하고 없으면 결재않됨)
		}
		if(line.getApStatus().equals("승인")) {
			 dcomm = line.getApComment();	//승인자코멘트 
			 ddate = line.getApDate();		//승인자 승인일자 (있으면 결재하고 없으면 결재않됨)\
		}
 			
		doc_lin += line.getApStatus()+" "+line.getApSabun()+" "+line.getApName()+" "+line.getApRank()+" "+line.getApDivision()+" "+line.getApDate()+" "+line.getApComment()+"\r";
		
		send_line += line.getApStatus()+" "+line.getApSabun()+" "+line.getApName()+" "+line.getApRank()+" "+line.getApDivision()+" "+line.getApDate()+" "+line.getApComment()+"@";
	}
	
	//-----------------------------------
	//	양식함의 전자결재 내용보기 (분기하기)
	//-----------------------------------
	if(doc_flag.equals("GEN") || doc_flag.equals("SERVICE")) lid = doc_pid;		//일반문서 및 고객관리

	//프린터로 출력하기
	if(PROCESS.equals("APP_PNT")) {
		response.sendRedirect("../gw/approval/eleApproval_PrintLink.jsp?doc_id="+lid+"&flag="+doc_flag+"&line="+send_line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+doc_pid);
	}
	//관련전자결재문서 보기
	else if(PROCESS.equals("APP_LNK")) {
		response.sendRedirect("../gw/approval/eleApproval_OtherLink.jsp?doc_id="+lid+"&flag="+doc_flag+"&line="+send_line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+doc_pid);
	}
	//해당내용 보기 : 모든 처리절차 진행
	else {
		response.sendRedirect("../gw/approval/eleApproval_ViewLink.jsp?doc_id="+lid+"&flag="+doc_flag+"&line="+send_line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+doc_pid);
	}

%>
