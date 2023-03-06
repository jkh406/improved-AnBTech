<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "전자문서 반려하기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	
%>

<%
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pass = (String)request.getAttribute("pass"); if(pass == null) pass = "";		//비밀번호묻기여부
	String pid = (String)request.getAttribute("pid"); if(pid == null) pid = "";			//결재관리번호
	String link_id = (String)request.getAttribute("link_id"); if(link_id == null) link_id = "";//관련모듈번호
	String app_id = (String)request.getAttribute("app_id"); if(app_id == null) app_id = "";	//결재승인번호
	String Reload = (String)request.getAttribute("Reload"); if(Reload == null) Reload = ""; //승인/반려처리

%>
<HTML><HEAD><TITLE>결재반려</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../gw/css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='javascript:focus();'>
<form name="eForm" method="get" style="margin:0" onsubmit='javascript:elePassword(); return false;'>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../gw/images/pop_app_d.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

<% if(pass.equals("NO")) {		//반려시 비밀번호 묻기 %>
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
		<tbody>
		  <tr><td height=35></td></tr>
		  <tr><td width='100%' align='left'><font color='565656'>전자결재 비밀번호를 입력하세요.</font></td></tr></tbody></table>
	
	<table cellspacing=0 cellpadding=2 width="94%" border=0><!-- 비밀번호 확인 -->
	   <tbody>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../gw/images/bg-01.gif">비밀번호</td>
           <td width="80%" height="25" class="bg_02"><input type="password" name="passwd">
				<input type="hidden" name="mode" value='PRS_REJ'>
				<input type="hidden" name="pid" value='<%=pid%>'>
				<input type="hidden" name="link_id" value='<%=link_id%>'>
				<input type="hidden" name="app_id" value='<%=app_id%>'>
		  </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
		<tbody>
		  <tr><td height=20></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
				 <a href='javascript:elePassword()'><img src='../gw/images/bt_confirm.gif' border='0' align='absmiddle'></a>
				 <a href='javascript:self.close()'><img src='../gw/images/bt_cancel.gif' border='0' align='absmiddle'></a>
			</TD></TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE>
<% } else if(pass.equals("OK")) { %>
	<table cellspacing=0 cellpadding=2 width="94%" border=0><!-- 비밀번호 확인 -->
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../gw/images/bg-01.gif">간단의견 (100자)</td>
           <td width="80%" height="25" class="bg_02"><TEXTAREA NAME="app_comment" rows=5 cols=35 onKeyUp="charCount();" onMouseUp="charCount();"></TEXTAREA>
				<input type="hidden" name="mode" value='REJ'>
				<input type="hidden" name="pid" value='<%=pid%>'>
				<input type="hidden" name="link_id" value='<%=link_id%>'>
				<input type="hidden" name="app_id" value='<%=app_id%>'>
		  </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td align="right" height=20 colspan="2">글자수: <input class="text_02" type="text" name="count" value='' size=3></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
				 <a href='javascript:eleProcess()'><img src='../gw/images/bt_confirm.gif' border='0' align='absmiddle'></a>
				 <a href='javascript:self.close()'><img src='../gw/images/bt_cancel.gif' border='0' align='absmiddle'></a>
			</TD></TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE>
<% } %>
</td></tr></table></form></BODY></HTML>

<Script Language = "Javascript">
 <!-- 
 //Reload
 var Reload = '<%=Reload%>';
 if(Reload == 'R') {
	//alert('결재되었습니다.');
	opener.parent.menu.location.href="../servlet/ApprovalInitServlet?mode=menu";
	opener.location.href="../servlet/ApprovalMenuServlet?mode=APP_ING";
	alert('반려되었습니다.');
	self.close();
 }

//Load
function focus() 
{
	var pass = '<%=pass%>';
	if(pass == "NO") document.eForm.passwd.focus();
	else document.eForm.app_comment.focus();
}

//결재반려전 비밀번호 묻기
function elePassword()
{
	var passwd = document.eForm.passwd.value;
	var pid = document.eForm.pid.value;
	var data = "mode="+'PRS_REJ'+"&PID="+pid+"&passwd="+passwd;	
	window.open("ApprovalProcessServlet?"+data,"eleA_app_decision","width=200,height=100,scrollbars=yes,toolbar=no,menubar=no,resizable=yes");
}

//결재 반려하기
function eleProcess()
{
	var comment = document.eForm.app_comment.value;
	if(StringLength(comment) < 5 || StringLength(comment) > 100) {
		alert("반려의견을 5자이상 110자이내로 입력하십시요.");	
		return;
	} 
	
	var path = '<%=upload_path%>'+'<%=crp%>';	//멜전송시
	var pid = document.eForm.pid.value;
	var link_id = '<%=link_id%>';				//모듈(승인원,기술문서) 관리번호
	var app_id = '<%=app_id%>';					//전자결재 관리번호
	var data = "mode="+'REJ'+"&PID="+pid+"&comment="+comment+"&path="+path;		//공통
		data += "&link_id="+link_id+"&app_id="+app_id;			//승인원,기술문서
	window.open("ApprovalProcessServlet?"+data,"eleA_app_decision","width=200,height=100,scrollbars=yes,toolbar=no,menubar=no,resizable=yes");
}

//문자열 갯수 즉시알려주기
function charCount()
{
	var str = document.eForm.app_comment.value;
	var c = StringLength(str);
	document.eForm.count.value=c;
	if(c > 100){
		alert("입력하신 내용이 100자를 초과했습니다.");
		document.eForm.app_comment.value = document.eForm.app_comment.value.substring(0,100);
		return false;
	}
}

//문자열갯수 파악하기
function StringLength(str)
{
	var len = 0;
	for(i=0; i<str.length; i++) {
		s = str.charAt(i);
		if(s.search(/\w/) == 0) len = len + 1;
		else if(s.search(/\s/) == 0) len = len + 1;
		else len = len + 2;
	}
	return len;
}
-->
</Script>