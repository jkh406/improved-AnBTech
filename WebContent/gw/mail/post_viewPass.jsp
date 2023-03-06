<%@ include file="../../admin/configPopUp.jsp"%>
<%@ 	page		
	info= "비밀문서 비밀번호 물어보기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%

	//메시지 전달변수
	String Message="";		//메시지 전달 변수  

	String id = "";			//접속자 id
	String passwd="";		//접속자 비밀번호
	String name="";			//접속자 이름

	//전달받은 변수 (from post_main.jsp)
	String pid = "";		//관리편지 번호
	String TITLE = "";		//읽을 편지함 종류


	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	id = sl.id; 		//접속자 login id

	/*********************************************************************
	 	전달받은 변수 읽기 (from post_main.jsp)
	*********************************************************************/
	String rPID = request.getParameter("PID");
	if(rPID != null) pid = rPID;

	String TIT = request.getParameter("Title");
	if(TIT != null) {
		if(TIT.equals("REC_ING")) TITLE = "REC_ING";
		else if(TIT.equals("SND_ING")) TITLE = "SND_ING";
		else if(TIT.equals("TMP_ING")) TITLE = "TMP_ING";
		else if(TIT.equals("WST_ING")) TITLE = "WST_ING";
	}

	/*********************************************************************
	 	입력한 비밀번호
	*********************************************************************/		
	String PASSWD = request.getParameter("passwd");

	/***********************************************************************
		비밀번호 알기
	***********************************************************************/
	if(PASSWD != null) {
		String[] idColumn = {"id","name","passwd"};
		//String query = "where id = '"+id+"' and passwd = password('"+PASSWD+"')";
		String query = "where id = '"+id+"' and passwd ='"+PASSWD+"'";
		bean.setTable("user_table");			
		bean.setColumns(idColumn);
		bean.setSearchWrite(query);	
		bean.init_write();
		if(bean.isEmpty()) Message="ERROR";
		else Message="PASS";
	}
%>

<HTML><HEAD><TITLE>비밀편지 암호확인</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='javascript:load();'>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_chk_pwd.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form method="post" action="post_viewPass.jsp" name="pForm" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="40%" height="25" class="bg_01" background="../images/bg-01.gif">비밀번호</td>
           <td width="60%" height="25" class="bg_02"><input type="password" name="passwd" size="10"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>
<input type='hidden' name='PID' value='<%=rPID%>'>
<input type='hidden' name='Title' value='<%=TIT%>'>
</form>	

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:document.pForm.submit();"><img border="0" src="../images/bt_confirm.gif" align="absmiddle"></a> <a href='javascript:self.close()'><img border="0" src="../images/bt_close.gif" align="absmiddle"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script>
<!--
function load()
{
	document.pForm.passwd.focus();
}
var Message = '<%=Message%>';
if(Message == 'PASS') { 
	location.href="post_view.jsp?PID="+'<%=pid%>'+"&Title="+'<%=TITLE%>';
} else if(Message == "ERROR") {
	alert("비밀번호가 다릅니다.");
} 
-->
</script>
