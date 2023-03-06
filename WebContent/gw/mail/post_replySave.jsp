<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "개인우편 작성 저장하기"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.text.*"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.email.emailSend"
%>
<%@	page import="com.anbtech.date.anbDate"			%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%

	//메시지 전달변수
	String Message="";		//메시지 전달 변수 
	String msg = "";		//사외메일전송 내용

	String id = "";			//접속자 id
	String name = "";		//접속자 이름
	String division = "";	//접속자 부서명
	String tel = "";		//접속자 전화번호

	//내부처리 변수
	String LIST="";			//수신인명단
	String RES="";			//발송/미발송 받기
	String subject="";		//문서제목
	String content="";		//본문

	String path = "";		//본문path
	String pfie = "";		//본문 파일명 (window)
	String state= "";		//email 인지 자체개인우편인지 판단

	String apath = "";		//첨부파일 Path
	String pad1o = "";		//첨부된 파일명1 원래이름	
	String pad1f = "";		//첨부된 파일명1
	String pad2o = "";		//첨부된 파일명2 원래이름	
	String pad2f = "";		//첨부된 파일명2
	String pad3o = "";		//첨부된 파일명3 원래이름	
	String pad3f = "";		//첨부된 파일명3	

	String SMSG = "";		//배달선택사항 다시저장하기
	String RYN = "";		//회신메뉴 디스플레이여부 (내부편지만 회신가능)

	//전달받은 pid (from post_view.jsp)
	String rpid ="";		//전달받은 pid로 전송/미전송시 해당내용 삭제키 위해

	//Email전송인지 여부판단
	String isEmail = "";	//전자메일인지 판단
	String host = "";		//보내는 서버명
	String toAddress = "";	//받는사람 주소
	String fromAddress = "";//보내는사람 주소
	String fromName = "";	//보내는사람 이름


	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	FileWriteString text = new com.anbtech.file.FileWriteString();			//내용을 파일로 담기
	textFileReader Rtext = new com.anbtech.file.textFileReader();			//text문자 읽기
	emailSend email = new com.anbtech.email.emailSend();					//외부메일 보내기

	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	id = login_id; 		//접속자 login id

	String[] idColumn = {"a.id","a.name","b.ac_name","a.office_tel"};
	String query = "where a.ac_id = b.ac_id and a.id='" + id + "'";
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);	
	bean.setSearchWrite(query);			
	bean.init_write();

	while(bean.isAll()) {
		name = bean.getData("name");				//접속자 명
		division = bean.getData("ac_name");		//접속자 부서명
		tel = bean.getData("office_tel");			//접속자 전화번호
	} //while

	/********************************************************************
		 MultipartRequest 빈을 생성시키기 위함.
	*********************************************************************/
	String DIR = bean.getID();	//Directory생성 PID (text file and upload file을 동일한 파일명으로 갖는다. 저장Dir은 다름)
	int maxUploadSize	= 50; 								// 첨부파일의 최대 크기를 지정(단위:Mbyte)
	String saveDir = upload_path+crp+"/post/"+id+"/addfile";// 첨부파일의 저장 디렉토리 지정(Full path을 쓴다.)
	text.setFilepath(saveDir);		//directory생성하기

	//조건에따라 생성자 생성
	com.oreilly.servlet.MultipartRequest multi;
	multi = new MultipartRequest(request,saveDir,maxUploadSize*1024*1024,"euc-kr");


	/*********************************************************************
	 	답장할 편지 전달받아 화면에 출력하기 (from post_view.jsp)
	*********************************************************************/
	String mPID = multi.getParameter("PID");
	//pid을 이용하여 관련내용을 읽는다.
	if(mPID != null) {
		//초기화
		subject=content=LIST=rpid=SMSG=""; 
		pad1o=pad1f=pad2o=pad2f=pad3o=pad3f="";
		path=pfie=host=toAddress=isEmail="";
		//전송/미전송시 해당내용 삭제함
		rpid = mPID;

		//해당내용읽기
		String[] mCls = {"pid","post_subj","writer_id","writer_name","bon_path","bon_file","post_state"};
		bean.setTable("POST_MASTER");			
		bean.setColumns(mCls);	
		bean.setOrder("pid ASC");	
		bean.setClear();
		bean.setSearch("pid",mPID);			
		bean.init_unique();	
	
		while(bean.isAll()) {
			subject = bean.getData("post_subj");			//제목
			String wid = bean.getData("writer_id");			//보낸사람 사번
			if(wid == null) wid = "";
			String wna = bean.getData("writer_name");		//보낸사람 이름
			if(wna == null) wna = "";
			LIST = wid + "/" + wna + ";";					//수신자로 조합
			SMSG="CFM";										//기본으로 수신확인 지정

			String Path = bean.getData("bon_path");			//본문path
			if(Path == null) Path = "/";
			path = crp + Path;							

			pfie = bean.getData("bon_file");				//본문 파일명 (window)
			if(pfie == null) pfie = "";

			state = bean.getData("post_state");				//email인지 여부판단

			//Email로 보낼것인지 판단하기
			if(state.equals("email")) {
				isEmail = "Y";									//email임
				LIST = wna;										//수신자로 조합
				if(wna.indexOf("(") > 0) {						
					toAddress = wna.substring(wna.indexOf("(")+1,wna.indexOf(")"));
				} else toAddress = wna;							//받는사람 주소
			} else isEmail = "N";								//email 아님
		} //while
	} //if

	/*********************************************************************
	 	메일서버 접속하기위한 기초정보 가져오기 (Email회신인 경우만)
		최초로 등록된 내용의 서버명과 이름,주소를 갖는다.
	*********************************************************************/	
	if(isEmail.equals("Y")) {
		String[] emailColumn = {"pid","id","sserver","name","address"};
		bean.setTable("emailInfo");			
		bean.setColumns(emailColumn);
		bean.setOrder("pid ASC");	
		bean.setSearch("id",id);			
		bean.init_unique();

		if(bean.isEmpty()) Message="NO_EMAIL";
		else {
			if(bean.isAll()) {								//처음 하나만 가져온다.
				host = bean.getData("sserver");				//보내는 메일서버명
				fromName = bean.getData("name");			//이름			
				fromAddress = bean.getData("address");		//주소	
			}// if
		} //if
	} //if

	/*********************************************************************
		배달선택사항값 디폴트로 수신확인만 기록함
	*********************************************************************/
	String SEL_DATA = SMSG;			

	/*********************************************************************
	 	내용 저장하기
	*********************************************************************/
	/*--------------------------------
	// 발송/미발송[저장] 처리 (최초 메뉴입력시 여기서 중단)
	---------------------------------*/
	RES = multi.getParameter("res");						//발송(SND) 미발송(TMP)
	
	/*---------------------------------
	//제목 읽기
	----------------------------------*/
	subject = multi.getParameter("SUBJECT");			//제목 

	/*---------------------------------
	//본문내용 읽기
	----------------------------------*/
	content=multi.getParameter("CONTENT");								//쓴내용(unix oracle에서 한글입력)
	//	본문내용 파일로 저장
	String contentDir = upload_path + crp + "/post/" + id + "/text_upload";	//저장 Dir  
	text.setFilepath(contentDir);											//directory생성하기
	String text_file = DIR;													//저장 파일명 (full path)
	text.WriteHanguel(contentDir,text_file,content);						//내용 파일로 저장하기

	/*-----------------------------------
	 	수신인 개인별 입력받기
	------------------------------------*/
	LIST=multi.getParameter("rec_name");

	/*--------------------------------
	//본문path,입력일, 삭제예정일 설정
	---------------------------------*/
	String conDir = "/post/" + id + "/text_upload";				//본문 path
	String up_date = anbdt.getTime();							//입력일
	String del_date = anbdt.getAddMonthNoformat(6);				//삭제예정일 (6개월후 : post_letter)
	String del_year = anbdt.getAddYearNoformat(1);				//삭제예정일 (1년후   : post_master) 

	/*--------------------------------
	// 첨부파일1 처리
	---------------------------------*/
	String File_Name1 = multi.getFilesystemName("UP_FILE1");//첨부파일명
	if(File_Name1 == null) File_Name1 = "";

	/*--------------------------------
	// 첨부파일2 처리
	---------------------------------*/
	String File_Name2 = multi.getFilesystemName("UP_FILE2");//첨부파일명
	if(File_Name2 == null) File_Name2 = "";

	/*--------------------------------
	// 첨부파일3 처리
	---------------------------------*/
	String File_Name3 = multi.getFilesystemName("UP_FILE3");//첨부파일명
	if(File_Name3 == null) File_Name3 = "";

	/*--------------------------------
	// 관리번호 설정
	---------------------------------*/
	String pid = bean.getID();											//관리번호

	/*----------------------------------
	// 수신자를 분해해서 저장하기 (이름/사번;)
	-----------------------------------*/
	//콜론으로 나누어 수신자 인원수 파악
	int Rcnt = 0;														//수신자 총인원파악
	for(int i=0; i < LIST.length(); i++) 
		if(LIST.charAt(i) == ';') Rcnt++;

	//사번만을 배열에 담기	
	int Scnt = 0;														//찾은 콜론 리턴번호 갖기
	int Tcnt = 0;
	String[] LIST_ID = new String[LIST.length()];						//배열만들기
	for(int j=0; j < LIST.length(); j++) {
		if(LIST.charAt(j) == ';') {
			String FID = LIST.substring(Scnt,j);						//개인별 전체 : 이름/사번
			String CID = FID.substring(0,FID.indexOf('/'));				//사번만      : 사번
			LIST_ID[Tcnt] = CID;										//사번만 배열에 담기
			//out.println("ID : " + LIST_ID[Tcnt] + "<br>");
			Scnt = j+3;				//리턴키(2) + ;값(1) = 3을 더한다.  
			Tcnt++;
		}
	}

	/*--------------------------------
	// 관련내용을 DB로 저장하기
	// (INFROM_BRD)
	---------------------------------*/	
	//	DB저장을 위한 특수문자 바꾸기 (' -> `) 
	subject = str.quoteReplace(subject);		//제목

	String inputs = ""; 		//POST_LETTER Table
	String m_inputs = "";		//POST_MASTER Table
	String rpid_del = "";		//from post_main.jsp의 보낼편지로 부터 읽은 편지일경우
	
	/*******************************************************
	//----------- 사내메일인 경우 실행하기 ----------------//
	********************************************************/
	if(RES.equals("SND") && isEmail.equals("N")) {				//전송(사내메일)
		//파일저장 이름 바꾸기1
		String nFile1 = "";													//새로저장할 파일명
		if(File_Name1.length() != 0) {
			int d = File_Name1.indexOf(".");
			String Hfile = File_Name1.substring(0,d);						//파일명
			String Ext = File_Name1.substring(d+1,File_Name1.length());		//확장자명

			nFile1 = DIR + "_1." + Ext;										//새로운 파일명
			String oFilename = saveDir + "/" + File_Name1;					//기존파일명(path 포함)
			String nFilename = saveDir + "/" + nFile1;						//새로운파일명(path 포함)
			Rtext.chgFilename(oFilename,nFilename);
		}

		//파일저장 이름 바꾸기
		String nFile2 = "";													//새로저장할 파일명
		if(File_Name2.length() != 0) {
			int d = File_Name2.indexOf(".");
			String Hfile = File_Name2.substring(0,d);						//파일명
			String Ext = File_Name2.substring(d+1,File_Name2.length());		//확장자명

			nFile2 = DIR + "_2." + Ext;										//새로운 파일명
			String oFilename = saveDir + "/" + File_Name2;					//기존파일명(path 포함)
			String nFilename = saveDir + "/" + nFile2;						//새로운파일명(path 포함)
			Rtext.chgFilename(oFilename,nFilename);
		}

		//파일저장 이름 바꾸기
		String nFile3 = "";													//새로저장할 파일명
		if(File_Name3.length() != 0) {
			int d = File_Name3.indexOf(".");
			String Hfile = File_Name3.substring(0,d);						//파일명
			String Ext = File_Name3.substring(d+1,File_Name3.length());		//확장자명

			nFile3 = DIR + "_3." + Ext;										//새로운 파일명
			String oFilename = saveDir + "/" + File_Name3;					//기존파일명(path 포함)
			String nFilename = saveDir + "/" + nFile3;						//새로운파일명(path 포함)
			Rtext.chgFilename(oFilename,nFilename);
		}

		bean.setAutoCommit(false);
		bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try {
			for(int k=0; k < Rcnt; k++) {		//POST_LETTER저장
				inputs = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_select,delete_date) values('";
				inputs += pid + "','" + subject + "','" + id + "','" + name + "','" + up_date + "','" + LIST_ID[k] + "','" + "0" + "','" + SEL_DATA + "','" + del_date + "')";
				bean.execute(inputs);
				//out.println("inputs : " + inputs + "<br>");
			}
			// POST_MASTER저장
			m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
			m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			m_inputs += pid + "','" + subject + "','" + id + "','" + name + "','" + up_date + "','" + LIST + "','" + "0" + "','";
			m_inputs += RES + "','" + SEL_DATA + "','" + conDir + "','" + text_file + "','" + File_Name1 + "','" + nFile1 + "','";
			m_inputs += File_Name2 + "','" + nFile2 + "','" + File_Name3 + "','" + nFile3 + "','" + del_year + "')";

			bean.execute(m_inputs);							//저장하기 (전송,미전송)
			LIST = "";
			if(RES.equals("SND")) Message = "SEND";
			else if(RES.equals("TMP")) Message = "SAVE";

			bean.commit();
		} catch (Exception e) { 
			bean.rollback();

			//저장실패시 첨부파일 삭제하기
			String Filedir1 = upload_path + crp + "/post/" + id + "/addfile/" + nFile1;
			String Filedir2 = upload_path + crp + "/post/" + id + "/addfile/" + nFile2;
			String Filedir3 = upload_path + crp + "/post/" + id + "/addfile/" + nFile3;
			Rtext.delFilename(Filedir1);	Rtext.delFilename(Filedir2);	Rtext.delFilename(Filedir3);

			//본문파일 삭제하기 (Windows)
			String bFileD = upload_path + crp + "/post/" + id + "/text_upload/" + text_file;
			Rtext.delFilename(bFileD);	
		
			//LIST CLEAR
			LIST = "";
			Message = "QUERY";
			
			out.println("메시지 : " + e);
		} finally{
			bean.setAutoCommit(true);
		}
	}
	/*******************************************************
	//----------- 사외메일인 경우 실행하기 ----------------//
	********************************************************/
	else if(RES.equals("SND") && isEmail.equals("Y")) {	//사외메일
		content = content;

		email.setSmtpUrl(host);				//smtp host명
		email.setFrom(fromAddress);			//보내는 사람 주소
		email.setFromName(fromName);		//보내는 사람 이름
		email.setTo(toAddress);				//받은사람 주소
		email.setSubject(subject);			//제목
		email.setContent(content);			//내용

		email.setFileName(File_Name1);		//첨부파일명1
		email.setFileName2(File_Name2);		//첨부파일명2
		email.setFileName3(File_Name3);		//첨부파일명3
		email.setPath(saveDir);				//첨부파일 디렉토리

		//메시지 메일로 보내기
		String Result = email.sendMessage();//메시지 보내기
		
		//메시지 전달하기
		msg = toAddress + ":" + Result + "; ";

		//처리결과 화면에 출력하기
		msg = msg.replace('\t',' ');
		msg = msg.replace('\r',' ');
		msg = msg.replace('\n',' ');	
		
		//---------------------------------------
		//보낸사외 메일 저장하기
		//---------------------------------------
		if(msg.indexOf("Send OK") > 0) {
				//파일저장 이름 바꾸기1
				String nFile1 = "";													//새로저장할 파일명
				if(File_Name1.length() != 0) {
					int d = File_Name1.indexOf(".");
					String Hfile = File_Name1.substring(0,d);						//파일명
					String Ext = File_Name1.substring(d+1,File_Name1.length());		//확장자명

					nFile1 = DIR + "_1." + Ext;										//새로운 파일명
					String oFilename = saveDir + "/" + File_Name1;					//기존파일명(path 포함)
					String nFilename = saveDir + "/" + nFile1;						//새로운파일명(path 포함)
					Rtext.chgFilename(oFilename,nFilename);
				}

				//파일저장 이름 바꾸기
				String nFile2 = "";													//새로저장할 파일명
				if(File_Name2.length() != 0) {
					int d = File_Name2.indexOf(".");
					String Hfile = File_Name2.substring(0,d);						//파일명
					String Ext = File_Name2.substring(d+1,File_Name2.length());		//확장자명

					nFile2 = DIR + "_2." + Ext;										//새로운 파일명
					String oFilename = saveDir + "/" + File_Name2;					//기존파일명(path 포함)
					String nFilename = saveDir + "/" + nFile2;						//새로운파일명(path 포함)
					Rtext.chgFilename(oFilename,nFilename);
				}

				//파일저장 이름 바꾸기
				String nFile3 = "";													//새로저장할 파일명
				if(File_Name3.length() != 0) {
					int d = File_Name3.indexOf(".");
					String Hfile = File_Name3.substring(0,d);						//파일명
					String Ext = File_Name3.substring(d+1,File_Name3.length());		//확장자명

					nFile3 = DIR + "_3." + Ext;										//새로운 파일명
					String oFilename = saveDir + "/" + File_Name3;					//기존파일명(path 포함)
					String nFilename = saveDir + "/" + nFile3;						//새로운파일명(path 포함)
					Rtext.chgFilename(oFilename,nFilename);
				}


				bean.setAutoCommit(false);
				bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try {
					//1. DB저장을 위한 특수문자 바꾸기 (' -> `) 
					subject = str.quoteReplace(subject);		//제목

					//2. POST_MASTER에만 저장하기
					m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
					m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
					m_inputs += pid + "','" + subject + "','" + id + "','" + fromName + "','" + up_date + "','" + toAddress + "','" + "0" + "','";
					m_inputs += "SND" + "','" + "" + "','" + conDir + "','" + text_file + "','" + File_Name1 + "','" + nFile1 + "','";
					m_inputs += File_Name2 + "','" + nFile2 + "','" + File_Name3 + "','" + nFile3 + "','" + del_year + "')";
	
					bean.execute(m_inputs);							//저장하기 (전송,미전송)
					Message = "ESEND";

					bean.commit();
				} catch (Exception e){
					bean.rollback();

					//저장실패시 첨부파일 삭제하기
					String Filedir1 = upload_path + crp + "/post/" + id + "/addfile/" + nFile1;
					String Filedir2 = upload_path + crp + "/post/" + id + "/addfile/" + nFile2;
					String Filedir3 = upload_path + crp + "/post/" + id + "/addfile/" + nFile3;
					Rtext.delFilename(Filedir1);Rtext.delFilename(Filedir2);Rtext.delFilename(Filedir3);

					//본문파일 삭제하기 (Windows)
					String bFileD = upload_path + crp + "/post/" + id + "/text_upload/" + text_file;
					Rtext.delFilename(bFileD);	   		
					out.println("메시지 : " + e);
				} finally{
					bean.setAutoCommit(true);
				}

		} else {
				//본문파일 삭제하기 (Windows)
				String bFileD = upload_path + crp + "/post/" + id + "/text_upload/" + text_file;
				Rtext.delFilename(bFileD);	
				
				//첨부파일 전송실패로 삭제하기
				String Filed1 = upload_path + crp + "/post/" + id + "/addfile/" + File_Name1;
				String Filed2 = upload_path + crp + "/post/" + id + "/addfile/" + File_Name2;
				String Filed3 = upload_path + crp + "/post/" + id + "/addfile/" + File_Name3;
				Rtext.delFilename(Filed1);Rtext.delFilename(Filed2);Rtext.delFilename(Filed3);

				Message = "FSEND";
		}// if :보낸사외 메일 저장하기	
		
	} //메일보내기 (사외/사내)

%>
<script>
<!--
var Message = '<%=Message%>';
if(Message == 'SEND') { 
	alert("발송 되었습니다."); 
	opener.parent.Left.location.reload();
	opener.document.location.reload();
	self.close(); 
} else if(Message == "NO_EMAIL") {
	alert("외부메일에 대한 환경설정이 없습니다. \n 먼저 환경설정에서 등록하십시요.");
	self.close();
} else if(Message == "ESEND") {
	alert("<%=msg%>");
	opener.parent.Left.location.reload();
	opener.document.location.reload();
	self.close();
} else if(Message == "FSEND") {
	alert("<%=msg%>");
	opener.parent.Left.location.reload();
	opener.document.location.reload();
	self.close();
}
-->
</script>

