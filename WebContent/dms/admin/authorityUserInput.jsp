<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkDM01.jsp"%>
<%@ page 
	contentType	= "text/html;charset=KSC5601"
	import		= "java.util.*"
	errorPage	= "../../admin/errorpage.jsp"
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> �ӽù��������������ڵ��</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300><a href="javascript:check();"><img src="../images/bt_save.gif" border="0" align="absmiddle"></a> <a href="javascript:history.go(-1);"><img src="../images/bt_cancel.gif" border="0" align="absmiddle"></a></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>
<!--����-->
<form name="aform" method="post" action="authorityDocProcess.jsp" style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">������ȣ</td>
           <td width="30%" height="25" class="bg_04"><INPUT type="text" name="doc_no" size="10" maxlength="10" class="text_01"></td>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">��������</td>
           <td width="30%" height="25" class="bg_04"><INPUT type="text" name="version" size="5" maxlength="5" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">����ϼ�</td>
           <td width="30%" height="25" class="bg_04">����Ϸκ��� <INPUT type="text" name="valid_day" size="3" maxlength="3" class="text_01">�ϰ�</td>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">����ڻ��</td>
           <td width="30%" height="25" class="bg_04"><INPUT type="text" name="user_id" size="10" maxlength="10" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

<INPUT type="hidden" name="mode" value="i">
</form>
</td></tr></table>
</body>
</html>

<SCRIPT LANGUAGE="JAVASCRIPT">
<!--
	
function check(){
	var f=document.aform
	if(f.doc_no.value=="") {
		alert("������ȣ�� �Է��Ͻʽÿ�.")
		f.doc_no.focus()
		return;
	}

	if(f.version.value=="") {
		alert("���������� �Է��Ͻʽÿ�.")
		f.version.focus()
		return;
	}

	if(f.valid_day.value=="") {
		alert("������ ����ϼ��� �Է��Ͻʽÿ�.")
		f.valid_day.focus()
		return;
	}
		
	if(f.user_id.value=="") {
		alert("������������ �ο��� ����� ����� �Է��Ͻʽÿ�.")
		f.user_id.focus()
		return;
	}

	f.submit()
}
-->
</SCRIPT>

