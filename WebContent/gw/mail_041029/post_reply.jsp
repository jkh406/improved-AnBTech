<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "개인우편 회신하기"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.text.*"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.file.textFileReader"

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

	/*********************************************************************
	 	답장할 편지 전달받아 화면에 출력하기 (from post_view.jsp)
	*********************************************************************/
	String mPID = request.getParameter("PID");

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
%>

<HTML><HEAD><TITLE>답장편지작성</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_n.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--버튼-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right">
		  <a href="Javascript:postSend();"><img border="0" src="../images/bt_export.gif" align="absmiddle"></a>
          <a href="Javascript:postClose();"><img border="0" src="../images/bt_close.gif" align="absmiddle"></a></td></tr></tbody></table>

    <!--발송정보-->
	<form method="post" name="sForm" encType="multipart/form-data" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">작성자</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=name%> [전화: <%=tel%>,  부서명:<%=division%>,  작성일:<%=bean.getTime()%>]</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">수신자</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="rec_name" rows=2 cols=75 readOnly><%=LIST%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">제목</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><input type="text" name="SUBJECT" size=76 value="[RE]<%=subject%>"></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">내용</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
		  <% 
			//state.equals("email")  //email 받은것			
			//Textfile 읽기
			textFileReader text = new com.anbtech.file.textFileReader();			//파일읽기
			String Textfile = upload_path + path + "/" + pfie;

			String buf = "";
			buf += ">";
			buf += text.getFileString(Textfile);
			buf += "\r=========================== 회신 내용 ================================";
	 
			//화면출력하기
			if(state.equals("email")) {
				if(buf.indexOf("><") == -1) {
					out.println("<TEXTAREA name='CONTENT' rows=22 cols=75>");		//본문 파일
					out.println(buf.toString());
					out.println("</TEXTAREA>"); 
				} else {
					out.println("<TEXTAREA name='CONTENT' rows=22 cols=75>");		//본문 파일
					out.println("\r=========================== 회신 내용 ================================");
					out.println("</TEXTAREA>"); 
				}
			} else {
				out.println("<TEXTAREA name='CONTENT' rows=22 cols=75>");		//본문 파일
				out.println(buf.toString());
				out.println("</TEXTAREA>"); 
			}
			
		%>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
				<input type="file" name="UP_FILE1" size=55><br>
				<input type="file" name="UP_FILE2" size=55><br>
				<input type="file" name="UP_FILE3" size=55>
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/bt_close.gif"  hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
	<input type="hidden" name="ReturnReceipt">
	<input type="hidden" name="SecretSetup">
	<input type="hidden" name="ReplySetup">
	<input type="hidden" name="PID">
	<input type="hidden" name="res">
  </form>
  
<% //파일삭제을 스크립트로 처리하기 위해 작성 %>
<form name=d1Form method="post" style="margin:0">
<input type="hidden" name="file1" value='<%=pad1f%>'>
</form>

<form name=d2Form method="post" style="margin:0">
<input type="hidden" name="file2" value='<%=pad2f%>'>
</form>

<form name=d3Form method="post" style="margin:0">
<input type="hidden" name="file3" value='<%=pad3f%>'>
</form>

</BODY></HTML>

<script>
<!--
//윈도우창 초기화
function setFocus() 
{
	defaultStatus = "새편지쓰기";			//윈도우 하단 출력메시지
	window.resizeTo(800,500);			//윈도우 크기조절	
	window.moveTo(200,200);				//윈도우 출력위치 조절	
	document.sForm.SUBJECT.focus();
}

//새편지작성 닫기
function postClose()
{
	opener.location.reload();
	self.close();
}

//발송하기
function postSend()
{
	if(sForm.SUBJECT.value == ""){	alert("제목을 입력하십시요.");	return;	}
	if(sForm.CONTENT.value == ""){	alert("내용을 입력하십시요.");	return;	}
	if(sForm.rec_name.value == ""){	alert("수신인을 지정하십시요.");return;	}

	document.sForm.action="post_replySave.jsp";
	document.sForm.PID.value='<%=mPID%>';
	document.sForm.res.value="SND";
	var sel = confirm('발송 하시겠습니까?');
	if(sel == false) {
		alert("발송하지 않습니다.");
		return;
	} else {
		document.sForm.submit();
	}
//	opener.location.reload();
//	opener.parent.Left.location.reload();
//	self.close();
}

function centerWindow() 
{ 
        var sampleWidth = 650;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 620;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 


-->
</script>

<!-- ****************** 메시지 전달부분 ****************************** -->
<% if(Message == "SEND") { %>
<script>
alert("발송 되었습니다.")
opener.parent.menu.location.reload();
close()
</script>
<% Message = "" ; } %>

<% if(Message == "NO_EMAIL") { %>
<script>
alert("외부메일에 대한 환경설정이 없습니다. \n 먼저 환경설정에서 등록하십시요.")
close()
</script>
<% Message = "" ; } %>

<% if((Message == "ESEND") || (Message == "FSEND")) { %>
<script>
	alert("<%=msg%>");
	close();
</script>
<% Message = "" ; } %>