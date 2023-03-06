<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "전자메일 작성"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="javax.mail.*"
	import="javax.mail.internet.*"
	import="com.oreilly.servlet.MultipartRequest"
	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.file.FileWriteString"
%>
<%@	page import="com.anbtech.text.Hanguel"				%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<%@	page import="com.anbtech.email.emailSend"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean"	/>

<%
	String Message = "M";
	String id	= "";		//접속자 id
	String[] pid;			//관리번호
	String[] smtp;			//smtp명
	String[] name;			//보내는사람 이름
	String[] address;		//보내는사람 주소

	int rid = 0;			//리턴받은 숫자값(stmp선택시)
	String msg = "S";		//전송 메시지 결과

	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	FileWriteString text = new com.anbtech.file.FileWriteString();			//내용을 파일로 담기
	textFileReader Rtext = new com.anbtech.file.textFileReader();			//text문자 읽기
	emailSend email = new com.anbtech.email.emailSend();					//멜보내기

	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	id = sl.id; 		//접속자 login id

	//변수 초기화
	Message = "M"; msg = "S";
	/********************************************************************
		 MultipartRequest 빈을 생성시키기 위함.
	*********************************************************************/
	int maxUploadSize	= 10; 								// 첨부파일의 최대 크기를 지정(단위:Mbyte)
	String saveDir = upload_path + crp + "/post/" + id + "/addfile";	// 첨부파일의 저장 디렉토리 지정
	text.setFilepath(saveDir);		//directory생성하기

	//조건에따라 생성자 생성
	com.oreilly.servlet.MultipartRequest multi;
	multi = new MultipartRequest(request,saveDir,maxUploadSize*1024*1024,"euc-kr");

	/*********************************************************************
	 	보내는 메일서버명 읽어오기
	*********************************************************************/
	String[] idColumn = {"pid","id","name","address","sserver"};
	bean.setTable("emailInfo");			
	bean.setColumns(idColumn);
	bean.setOrder("pid ASC");	
	bean.setSearch("id",id);			
	bean.init_unique();
	
	int cnt = bean.getTotalCount();
	if(cnt == 0) cnt = 1;

	pid		= new String[cnt];						//관리번호
	smtp	= new  String[cnt];						//smtp명
	name	= new String[cnt];						//보내는사람 이름
	address = new String[cnt];						//보내는사람 주소

	if(bean.isEmpty()) {
		pid[0] = "";
		smtp[0] = "";
		name[0] = "";
		address[0] = "";
		Message = "NODATA";
	} else {
		int i = 0;
		while(bean.isAll()) {
			pid[i]		= bean.getData("pid");				
			smtp[i]		= bean.getData("sserver");		
			name[i]		= bean.getData("name");	
			address[i]	= bean.getData("address");
			i++;
		} //while
	}

	/********************************************************************
		전자메일 보내기
	*********************************************************************/
	String SEND = "";
	SEND = multi.getParameter("SEND");
	if(SEND != null){
		msg = "";
		String toAddress = multi.getParameter("strTo");							//받는사람 주소
		if(toAddress == null) toAddress = "";
		String fromAddress = multi.getParameter("strFrom");						//보내는사람 주소
		if(fromAddress == null) fromAddress = "";
		String fromName = multi.getParameter("strName");						//보내는사람 이름
		if(fromName == null) fromName = "";
		String subject = multi.getParameter("strSubject");						//제목   
		if(subject == null) subject = "";
		String content = multi.getParameter("strContent");						//내용
		if(content == null) content = "";

		String File_Name1 = multi.getFilesystemName("file");					//첨부파일명1
		if(File_Name1 == null) File_Name1 = "";

		String File_Name2 = multi.getFilesystemName("file2");					//첨부파일명2
		if(File_Name2 == null) File_Name2 = "";

		String File_Name3 = multi.getFilesystemName("file3");					//첨부파일명3
		if(File_Name3 == null) File_Name3 = "";

		String host = smtp[rid];												//선택한 smtp명

		//받은사람 주소 분해하여 보내기
		toAddress = toAddress.replace(',',  ' ');
		toAddress = toAddress.replace(';',  ' ');
		toAddress = toAddress.replace('\t', ' ');
		toAddress = toAddress.replace('\r', ' ');
		toAddress = toAddress.replace('\n', ' ');

		//---------------------------------------
		//전송하기 
		//---------------------------------------
		StringTokenizer To = new StringTokenizer(toAddress," ");
		while(To.hasMoreTokens()){
			String toAdd = To.nextToken().trim();
			toAdd = str.repWord(toAdd,";","");

			email.setSmtpUrl(host);				//smtp host명
			email.setFrom(fromAddress);			//보내는 사람 주소
			email.setFromName(fromName);		//보내는 사람 이름
			email.setTo(toAdd);					//받은사람 주소
			email.setSubject(subject);			//제목
			email.setContent(content);			//내용

			email.setFileName(File_Name1);		//첨부파일명1
			email.setFileName2(File_Name2);		//첨부파일명2
			email.setFileName3(File_Name3);		//첨부파일명3
			email.setPath(saveDir);				//첨부파일 디렉토리


			//메시지 메일로 보내기
			String Result = email.sendMessage();//메시지 보내기

			//메시지 전달하기
			msg += toAdd + ":" + Result + " \n ";
		}

		//---------------------------------------
		//보낸메일 저장하기
		//---------------------------------------
		if(msg.indexOf("Send OK") > 0) {
				//1. 본문파일로 저장하기
				content=multi.getParameter("strSubject");
				String DIR = bean.getID();

				//	본문내용 파일로 저장
				String contentDir = upload_path + crp + "/post/" + id + "/text_upload";	//저장 Dir  
				text.setFilepath(contentDir);										//directory생성하기
				String text_file = DIR;												//저장 파일명 (full path)
				text.WriteHanguel(contentDir,text_file,content);					//내용 파일로 저장하기
				

				//2. 본문path,입력일, 삭제예정일 설정
				String conDir = "/post/" + id + "/text_upload";				//본문 path
				String up_date = anbdt.getTime();							//입력일
				String del_date = anbdt.getAddMonthNoformat(6);		//삭제예정일 (6개월후 : post_letter)
				String del_year = anbdt.getAddYearNoformat(1);		//삭제예정일 (1년후   : post_master) 

				//3. 첨부파일 이름바꿔 저장하기
				String nFile1 = "";									//새로저장할 파일명1
				if(File_Name1.length() != 0) {
					int d = File_Name1.indexOf(".");
					String Hfile = File_Name1.substring(0,d);						//파일명
					String Ext = File_Name1.substring(d+1,File_Name1.length());		//확장자명

					nFile1 = DIR + "_1." + Ext;										//새로운 파일명
					String oFilename = saveDir + "/" + File_Name1;					//기존파일명(path 포함)
					String nFilename = saveDir + "/" + nFile1;						//새로운파일명(path 포함)
					Rtext.chgFilename(oFilename,nFilename);
				}

				String nFile2 = "";									//새로저장할 파일명2
				if(File_Name2.length() != 0) {
					int d = File_Name2.indexOf(".");
					String Hfile = File_Name2.substring(0,d);						//파일명
					String Ext = File_Name2.substring(d+1,File_Name2.length());		//확장자명

					nFile2 = DIR + "_2." + Ext;										//새로운 파일명
					String oFilename = saveDir + "/" + File_Name2;					//기존파일명(path 포함)
					String nFilename = saveDir + "/" + nFile2;					//새로운파일명(path 포함)
					Rtext.chgFilename(oFilename,nFilename);
				}

				String nFile3 = "";									//새로저장할 파일명3
				if(File_Name3.length() != 0) {
					int d = File_Name3.indexOf(".");
					String Hfile = File_Name3.substring(0,d);						//파일명
					String Ext = File_Name3.substring(d+1,File_Name3.length());		//확장자명

					nFile3 = DIR + "_3." + Ext;										//새로운 파일명
					String oFilename = saveDir + "/" + File_Name3;					//기존파일명(path 포함)
					String nFilename = saveDir + "/" + nFile3;					//새로운파일명(path 포함)
					Rtext.chgFilename(oFilename,nFilename);
				}

				//4. 관리번호 설정
				String spid = bean.getID();											//관리번호

				//5. DB저장을 위한 특수문자 바꾸기 (' -> `) 
				subject = str.quoteReplace(subject);		//제목

				//6. POST_MASTER에만 저장하기
				String m_inputs = "";		//POST_MASTER Table
				m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
				m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
				m_inputs += spid + "','" + subject + "','" + id + "','" + fromName + "','" + up_date + "','" + toAddress + "','" + "0" + "','";
				m_inputs += "SND" + "','" + "" + "','" + conDir + "','" + text_file + "','" + File_Name1 + "','" + nFile1 + "','";
				m_inputs += File_Name2 + "','" + nFile2 + "','" + File_Name3 + "','" + nFile3 + "','" + del_year + "')";

				try { 	
					bean.execute(m_inputs);							//저장하기 (전송,미전송)
				} 
				catch (Exception e) { 
					//저장실패시 첨부파일 삭제하기
					String Filedir1 = upload_path + crp + "/post/" + id + "/addfile/" + nFile1;
					String Filedir2 = upload_path + crp + "/post/" + id + "/addfile/" + nFile2;
					String Filedir3 = upload_path + crp + "/post/" + id + "/addfile/" + nFile3;
					Rtext.delFilename(Filedir1);Rtext.delFilename(Filedir2);Rtext.delFilename(Filedir3);

					//본문파일 삭제하기 (Windows)
					String bFileD = upload_path + crp + "/post/" + id + "/text_upload/" + text_file;
					Rtext.delFilename(bFileD);
				} //try

		} // if :보내메일 저장하기

		//처리결과 화면에 출력하기
		msg = msg.replace('\t',' ');
		msg = msg.replace('\r',' ');
		msg = msg.replace('\n',' ');
		Message = "SEND";

	} //if

%>

<script>
<!--
//전자메일 전송 처리결과 
if("<%=Message%>" == "SEND"){ alert("<%=msg%>"); self.close(); }
//계정이 없으면 메시지 전달하기
if("<%=Message%>" == "NODATA"){ alert("환경설정을 등록후 진행하십시요."); self.close(); }
-->
</script>
