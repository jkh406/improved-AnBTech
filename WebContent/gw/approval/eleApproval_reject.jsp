<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "���ڹ��� �ݷ��ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	
%>

<%
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String pass = (String)request.getAttribute("pass"); if(pass == null) pass = "";		//��й�ȣ���⿩��
	String pid = (String)request.getAttribute("pid"); if(pid == null) pid = "";			//���������ȣ
	String link_id = (String)request.getAttribute("link_id"); if(link_id == null) link_id = "";//���ø���ȣ
	String app_id = (String)request.getAttribute("app_id"); if(app_id == null) app_id = "";	//������ι�ȣ
	String Reload = (String)request.getAttribute("Reload"); if(Reload == null) Reload = ""; //����/�ݷ�ó��

%>
<HTML><HEAD><TITLE>����ݷ�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../gw/css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='javascript:focus();'>
<form name="eForm" method="get" style="margin:0" onsubmit='javascript:elePassword(); return false;'>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../gw/images/pop_app_d.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

<% if(pass.equals("NO")) {		//�ݷ��� ��й�ȣ ���� %>
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
		<tbody>
		  <tr><td height=35></td></tr>
		  <tr><td width='100%' align='left'><font color='565656'>���ڰ��� ��й�ȣ�� �Է��ϼ���.</font></td></tr></tbody></table>
	
	<table cellspacing=0 cellpadding=2 width="94%" border=0><!-- ��й�ȣ Ȯ�� -->
	   <tbody>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../gw/images/bg-01.gif">��й�ȣ</td>
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

	<!--������-->
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
	<table cellspacing=0 cellpadding=2 width="94%" border=0><!-- ��й�ȣ Ȯ�� -->
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../gw/images/bg-01.gif">�����ǰ� (100��)</td>
           <td width="80%" height="25" class="bg_02"><TEXTAREA NAME="app_comment" rows=5 cols=35 onKeyUp="charCount();" onMouseUp="charCount();"></TEXTAREA>
				<input type="hidden" name="mode" value='REJ'>
				<input type="hidden" name="pid" value='<%=pid%>'>
				<input type="hidden" name="link_id" value='<%=link_id%>'>
				<input type="hidden" name="app_id" value='<%=app_id%>'>
		  </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td align="right" height=20 colspan="2">���ڼ�: <input class="text_02" type="text" name="count" value='' size=3></td></tr></tbody></table>

	<!--������-->
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
	//alert('����Ǿ����ϴ�.');
	opener.parent.menu.location.href="../servlet/ApprovalInitServlet?mode=menu";
	opener.location.href="../servlet/ApprovalMenuServlet?mode=APP_ING";
	alert('�ݷ��Ǿ����ϴ�.');
	self.close();
 }

//Load
function focus() 
{
	var pass = '<%=pass%>';
	if(pass == "NO") document.eForm.passwd.focus();
	else document.eForm.app_comment.focus();
}

//����ݷ��� ��й�ȣ ����
function elePassword()
{
	var passwd = document.eForm.passwd.value;
	var pid = document.eForm.pid.value;
	var data = "mode="+'PRS_REJ'+"&PID="+pid+"&passwd="+passwd;	
	window.open("ApprovalProcessServlet?"+data,"eleA_app_decision","width=200,height=100,scrollbars=yes,toolbar=no,menubar=no,resizable=yes");
}

//���� �ݷ��ϱ�
function eleProcess()
{
	var comment = document.eForm.app_comment.value;
	if(StringLength(comment) < 5 || StringLength(comment) > 100) {
		alert("�ݷ��ǰ��� 5���̻� 110���̳��� �Է��Ͻʽÿ�.");	
		return;
	} 
	
	var path = '<%=upload_path%>'+'<%=crp%>';	//�����۽�
	var pid = document.eForm.pid.value;
	var link_id = '<%=link_id%>';				//���(���ο�,�������) ������ȣ
	var app_id = '<%=app_id%>';					//���ڰ��� ������ȣ
	var data = "mode="+'REJ'+"&PID="+pid+"&comment="+comment+"&path="+path;		//����
		data += "&link_id="+link_id+"&app_id="+app_id;			//���ο�,�������
	window.open("ApprovalProcessServlet?"+data,"eleA_app_decision","width=200,height=100,scrollbars=yes,toolbar=no,menubar=no,resizable=yes");
}

//���ڿ� ���� ��þ˷��ֱ�
function charCount()
{
	var str = document.eForm.app_comment.value;
	var c = StringLength(str);
	document.eForm.count.value=c;
	if(c > 100){
		alert("�Է��Ͻ� ������ 100�ڸ� �ʰ��߽��ϴ�.");
		document.eForm.app_comment.value = document.eForm.app_comment.value.substring(0,100);
		return false;
	}
}

//���ڿ����� �ľ��ϱ�
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