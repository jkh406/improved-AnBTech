<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "mail system"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="java.io.*"
	import="java.sql.*"
	import="java.text.*"
	import="javax.servlet.*"
	import="javax.servlet.http.*"
	import="com.anbtech.gw.email.*"
	
%>
<%@	page import="java.io.BufferedInputStream"	%>
<%@	page import="java.io.FileOutputStream"		%>
<%@	page import="java.io.InputStream"		%>
<%@	page import="javax.mail.Message"		%>
<%@	page import="javax.mail.Multipart"		%>
<%@	page import="javax.mail.Part"			%>
<%@ page import="javax.mail.internet.MimeUtility"	%>
<%@	page import="javax.activation.DataSource"	%>
<%@	page import="javax.activation.URLDataSource"	%>
<%@	page import="javax.activation.DataHandler"	%>
<%@	page import="com.anbtech.gw.email.emailReceive"	%>
<%@	page import="com.anbtech.file.FileWriteString"	%>
<%@	page import="com.anbtech.date.anbDate"	%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	//세션정보
	String id = "";			//접속자 id

	String pid = "";		//관리번호
	String name = "";		//접속자 이름
	String division = "";		//접속자 부서명
	String tel = "";		//접속자 전화번호
	//받아오는메일서버 접속환경변수
	String[] prototype;		//받아오는 protocaltype
	String[] hostname;		//받아오는 메일서버명
	String[] username;		//메일계정 id
	String[] password;		//메일계정 비밀번호
	String[] readtype;		//전자메일계정에서 가져온메일 삭제여부(READ_ONLY:RO, READ_WRITE:RW)	

	//email받아오기 변수
	String from = "";		//보낸 사람 주소 및 이름
	String sent_date = "";		//보낸 날자 
	String subject="";		//메일 제목 
	String content="";		//메일 본문 
	String FileName="";		//첨부파일 

	String pad1o = "";		//첨부된 파일명1 원래이름	
	String pad1f = "";		//첨부된 파일명1
	String pad2o = "";		//첨부된 파일명2 원래이름	
	String pad2f = "";		//첨부된 파일명2
	String pad3o = "";		//첨부된 파일명3 원래이름	
	String pad3f = "";		//첨부된 파일명3	
	String pad4o = "";		//첨부된 파일명4 원래이름	
	String pad4f = "";		//첨부된 파일명4
	String pad5o = "";		//첨부된 파일명5 원래이름	
	String pad5f = "";		//첨부된 파일명5

	//저장할 파일명 및 저장디렉토리
	String bonfn;			//저장할 본문파일명 
	String addpath;			//첨부파일 저장할 path
	String textpath;		//본문내용 저장할 path
%>

<HTML><HEAD><TITLE>외부메일 수신(결과)</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_e.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="4"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">결과메시지</td>
           <td width="80%" height="25" colspan="3" class="bg_02">
				<textarea cols=45 rows=10>
<%

	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	FileWriteString text = new com.anbtech.file.FileWriteString();			//내용을 파일로 담기
	emailReceive email = new com.anbtech.gw.email.emailReceive();			//email가져오기

	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	id = sl.id; 		//접속자 login id

	String[] idColumn = {"a.id","a.name","a.office_tel","b.ac_name"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	String item_data = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	while(bean.isAll()) {
		name = bean.getData("name");				//접속자 명
		division = bean.getData("ac_name");		//접속자 부서명
		tel = bean.getData("office_tel");			//접속자 전화번호
	} //while
	
	/*********************************************************************
	 	메일서버 접속하기위한 기초정보 가져오기
	*********************************************************************/	
	String[] emailColumn = {"id","rtype","rserver","loginid","loginpwd","readtype"};
	bean.setTable("emailInfo");			
	bean.setColumns(emailColumn);
	bean.setOrder("id ASC");	
	bean.setSearch("id",id);			
	bean.init_unique();

	//배열 초기화
	int cnt = bean.getTotalCount();
	prototype = new String[cnt];					//프로토콜 type
	hostname = new String[cnt];						//받는 메일서버명
	username = new String[cnt];						//사용자 ID
	password = new String[cnt];						//사용자 비밀번호
	readtype = new String[cnt];						//전자메일에서 가져온 메일 삭제여부

	int ei = 0;
	while(bean.isAll()) {
		prototype[ei] = bean.getData("rtype");		//받아오는 메일서버 protocol  (pop3 or imap)
		hostname[ei] = bean.getData("rserver");		//받아오는 메일서버명			
		username[ei] = bean.getData("loginid");		//메일계정 id	
		password[ei] = bean.getData("loginpwd");	//메일계정 비밀번호		
		readtype[ei] = bean.getData("readtype");	//삭제여부(READ_ONLY:RO, READ_WRITE:RW)
		ei++;
	} //while
	
	/*********************************************************************
	 	메일서버 접속하기
	*********************************************************************/	

int si = 0;
nextRow :	
for(int hi = hostname.length; si < hi; si++) {
	if((hostname[si] != null) && (username[si] != null) && (password[si] != null)){ 
		email.setHost(hostname[si]);
		email.setUserName(username[si]);
		email.setPassWord(password[si]);
	}

	//prototype 가져오기
	String proto="";
	if(prototype[si] != null){
		if(prototype[si].equals("POP3")) proto = "pop3";
		else if(prototype[si].equals("IMAP")) proto = "imap";
	} else proto = "pop3";
	email.setProtocol(proto);

	//받은메일 삭제여부 가져오기
	String rwtype="RW";
	if((readtype[si] != null) || (readtype[si].length() != 0))
		rwtype = readtype[si];
	else rwtype = "RW";
	email.isDelete(rwtype);	
	
	//받는 메일서버 결과보기
	try {
		String Result = email.getConnect();		//메일서버에 접속한다.
		out.println("받는 메일 서버명 : " + hostname[si]);
		out.println("접속 결과 -------- " + Result + "\n");
	} catch (Exception e) {
		out.println("받는 메일 서버명 : " + hostname[si]);
		out.println(e);
		continue nextRow;		//레이블된 장소로 이동한다.
	}

	//====================================================================
	// 	메일내용 분류하기
	//====================================================================
	//저장할 본문파일명
	bonfn = bean.getID() + si;		//중복방지를 위해
				
	//저장될 본문파일 디렉토리
	String contentDir = upload_path + crp + "/email/" + id + "/text";	//저장 Dir     (window만 사용)
	text.setFilepath(contentDir);	//디렉토리 없으면 만들기
	email.setTextPath(contentDir);		

	//저장될 첨부파일 디렉토리
	String addDir = upload_path + crp + "/email/" + id + "/addfile";	//저장 Dir     (window만 사용)
	text.setFilepath(contentDir);	//디렉토리 없으면 만들기
	email.setAddPath(addDir);	
  
	//메시지 수량 파악하기 
	int mcnt = email.getMailCount();			

	//이미 받아온 메일인지 여부를 알려주는 변수
	String flag = "NEW";		//새로운 email  , "OLD"면 이미 가져온  메일	 
			
	//-----------------------------------------------
	//	메시지 처리하기
	//-----------------------------------------------
	//처리할 메시지가 없음  
	if(mcnt == 0) { 			
		email.close();	//종료하기 
	//메시지 처리 하기 		
	} else {       								
		for(int i = 1; i <= mcnt; i++) {
			//0.관련변수 초기화 하기
			from=sent_date=subject=content=FileName="";
			pad1o=pad1f=pad2o=pad2f=pad3o=pad3f=pad4o=pad4f=pad5o=pad5f="";

			//0.관리번호 생성
			pid = bean.getID() + i;		//중복을 피하기 위해

			//0. 메일를 하나씩 가져와서 처리하기
			Message msg = email.getMail(i);

			//1.보낸사람주소(보낸사람이름) 알아보기 
			String fromName = msg.getFrom()[0].toString();
			String fname = email.encordingName(fromName); //인코딩을 디코딩하기	
			if(fname.equals("NOENC")) 
				from = new String(fromName.getBytes("8859_1"),"euc-kr");
			else
				from = fname; 

			//	DB저장을 위한 특수문자 바꾸기 (' -> `) 
			from = str.quoteReplace(from);		
			from = from.replace('"',' ');		
			from = from.replace('<','(');	
			from = from.replace('>',')');
			from = from.trim();

			//2.보낸날자 알아보기 
			java.util.Date now = msg.getSentDate();
			if(now != null) {
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd  HH:mm");
				sent_date = vans.format(now);
			}

			//-------------------------------------------
			//	이미 받아온 메일인지 파악하기
			//--------------------------------------------
			String[] mailColumn = {"writer_id","write_date","post_receiver"};
			bean.setTable("POST_LETTER");			
			bean.setColumns(mailColumn);
			bean.setOrder("post_receiver ASC");	
			bean.setSearch("writer_id",hostname[si],"write_date",sent_date,"post_receiver",id);			
			bean.init_unique();

			int rcnt = bean.getTotalCount();			
			if(rcnt == 0) flag = "NEW";		//새로운 메일
			else flag = "OLD";			//이미 받아옴
	
			//3.제목 알아보기
			String title = msg.getSubject();
			subject = email.encordingSubject(title);
			if(subject.length() == 0) subject = "제목없음";

			//	DB저장을 위한 특수문자 바꾸기 (' -> `) 
			subject = str.quoteReplace(subject);		
			subject = subject.replace('<','(');	
			subject = subject.replace('>',')');	
			
			//4.저장할 본문내용 저장파일명 (중복을 피하기 위해 가공함)
			String bon_filename = bonfn + i + ".html";
				
			//5.저장할 첨부파일 저장파일명 (중복을 피하기 위해 가공함)
			String add_filename = bonfn + i;
				
			//6-1.첨부파일 및 내용 알아보기 
			if(msg.isMimeType("multipart/*")) {			//첨부파일 처리 하기 
				Multipart multipart = (Multipart)msg.getContent();
				int m = multipart.getCount();					//첨부파일 갯수 
				//out.println("첨부파일 수량 = " + m);
				for(int j = 0; j < m; j++) {					
					Part part = multipart.getBodyPart(j);
					String disposition = part.getDisposition();
					
					//out.println("disposition = " + disposition);						
					//메일 본문 ( j=0 : null --> 본문 내용)
					if(disposition == null) {
						//6-1-1.본문 파일로 저장 (Stream으로 처리 하여 한글 깨짐 방지)
						String pbon = msg.getContent().toString();	//인코딩여부 알기위해
						InputStream inFile = part.getInputStream();   //본문을 stream형태로 읽음 
						if(flag.equals("NEW"))		//새로운 메일이면 저장
							email.saveTextInputStream(bon_filename,inFile,pbon);
					} 
						
					//첨부파일 읽기 (j=1 : attachement 첨부파일1 , - - , j=3 : attachement 첨부파일3)
					if(disposition != null && (disposition.equals(part.ATTACHMENT) || disposition.equals(part.INLINE))) {
						
						//6-1-2.첨부파일명 
						String FN = part.getFileName();	
						//out.println("FN = " + FN);
						if(FN != null) {				//첨부파일이 null이 아닌경우만 처리함				
							//첨부파일명 한글처리하기
							FileName = "";	
							String adname = email.encordingName(FN); //인코딩을 디코딩하기	
							if(adname.equals("NOENC")) 
								FileName = new String(FN.getBytes("8859_1"),"euc-kr");
							else
								FileName = adname;
							//out.println("첨부파일명 = " + FileName);
							//저장할 원래 첨부파일명 변수에 담기
							if(j == 1) pad1o = FileName;
							else if(j == 2) pad2o = FileName;
							else if(j == 3) pad3o = FileName;
							else if(j == 4) pad4o = FileName;
							else if(j == 5) pad5o = FileName;

							
							//첨부파일 확장자명 구하기 
							String extFileName = "";
							if(FileName.length() > 0) {
								extFileName = FileName.substring(FileName.indexOf("."),FileName.length());
							}
	
							//6-1-3.첨부파일 저장하기 
							if(FileName.length() > 0) {
								BufferedInputStream in = new BufferedInputStream(part.getInputStream());
								if(flag.equals("NEW"))		//새로운 메일이면 저장
									email.saveAddInputStream(add_filename,extFileName,j,in);	

								//가공 첨부파일명
								if(j == 1) pad1f = add_filename + "_1" + extFileName;
								else if(j == 2) pad2f = add_filename + "_2" + extFileName;
								else if(j == 3) pad3f = add_filename + "_3" + extFileName;
								else if(j == 4) pad4f = add_filename + "_4" + extFileName;
								else if(j == 5) pad5f = add_filename + "_5" + extFileName;						
							} //if	(첨부파일저장)
						}// if (첨부파일명이 null이 아니면)							
					} //if (첨부파일 처리)

				} //for
			//6-2.첨부파일 없음 
			} else {											
				FileName = "";
				//6-2-1.본문 파일로 저장 (Stream으로 처리 하여 한글 깨짐 방지) 
				String nbon = msg.getContent().toString();	//인코딩여부 알기위해
				InputStream inFile = msg.getInputStream();
				if(flag.equals("NEW"))		//새로운 메일이면 저장
					email.saveTextInputStream(bon_filename,inFile,nbon);	

			} //if
				
			//내용 출력 하기 
			if(flag.equals("NEW")) {
				out.println("작성자이름 = " + from);
				out.println("Mail제목 = " + subject);
				out.println("작성일자 = " + sent_date);
				out.println("");	
			}

			//삭제하기위한 DELETED마크 하기
			email.markDeleted();
			
			//-------------------- DB로 저장하기 --------------------------------//
			if(flag.equals("NEW")) {
				//기타 정보
				String recIDs = name + "/" + id + ";";				//받는자 (post_master에 등록시)
				String rec = id;									//받는자 (post_letter에 등록시)
				String del_date = bean.getMonthNoformat("6");		//삭제예정일 (6개월후 : post_letter)
				String del_year = bean.getYearNoformat("1");		//삭제예정일 (1년후   : post_master) 
				String conDir = "/email/" + id + "/text";			//본문path

				//window POST_LETTER저장하기
				String inputs="";
				inputs = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
				inputs += pid + "','" + subject + "','" + hostname[si] + "','" + from + "','" + sent_date + "','" + rec + "','" + "0" + "','" + del_date + "')";
				bean.execute(inputs);
				//out.println("inputs : " + inputs + "<br>");
			
				// POST_MASTER저장
				String m_inputs="";
				m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file,";
				m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
				m_inputs += pid + "','" + subject + "','" + hostname[si] + "','" + from + "','" + sent_date + "','" + recIDs + "','" + "0" + "','";
				m_inputs += "email" + "','" + conDir + "','" + bon_filename + "','" + pad1o + "','" + pad1f + "','";
				m_inputs += pad2o + "','" + pad2f + "','" + pad3o + "','" + pad3f + "','" + del_year + "')";
				bean.execute(m_inputs);
				//out.println("m_inputs : " + m_inputs + "<br>");
			} //if (저장)

		} //for	(하나의 전자메일 계정 메일가져와 처리하기)	

		//종료 하기 (readtype에따라 가져온메일 삭제여부 진행하기) 
		email.close();				
	} //if (메일이 있는경우 처리)

} //for (전자메일계정의 전체메일처리)

%>
				</textarea>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:dclose();"><img src="../images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>


<script>
<!--
function dclose()
{	
	//opener.view.location.reload();
	opener.parent.up.location.reload();
	opener.location.reload();
	self.close();
}

function centerWindow() 
{ 
        var sampleWidth = 610;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 410;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

-->
</script>


