<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*"
%>

<%
	String order_no = request.getParameter("order_no");
%>

<HTML><HEAD><TITLE>발주서 이메일 발송</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_email_order.gif" hspace="10" alt="발주서 이메일 발송"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name="f1" method="post" enctype="multipart/form-data" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">수신자 이름</td>
           <td width="70%" height="25" class="bg_04"><input type='text' name='receiver_name' size='10' class="text_01" maxlength="20"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">수신자 전자우편</td>
           <td width="70%" height="25" class="bg_04"><input type='text' name='receiver_email' size='30' class="text_01" maxlength="40"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">간단 메시지<br>(100자 이내)</td>
           <td width="70%" height="25" class="bg_04"><textarea rows="2" name="req_message" cols="45"></textarea></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>
	<input type='hidden' name='mode' value='send_order'>
	<input type='hidden' name='order_no' value='<%=order_no%>'>
	</form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:go()'><img src='../images/bt_export.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script language='javascript'>
function go(){
	if(document.f1.receiver_name.value == ''){
		alert("수신자 이름을 입력하십시오.");
		document.f1.receiver_name.focus();
		return;
	}

	if(document.f1.receiver_email.value == ''){
		alert("수신자 전자우편 주소를 입력하십시오.");
		document.f1.receiver_email.focus();
		return;
	}

	if (!CheckEmail(document.f1.receiver_email.value)){
		alert("수신자 전자우편 주소를 올바르게 입력하십시오.");
		document.f1.receiver_email.focus();
		return;
    }

	document.f1.action = "../../servlet/PurchaseOtherMgrServlet";
	document.f1.submit();
}

function CheckEmail(strEmail) {
    var regDoNot = /(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/; 
    var regMust = /^[a-zA-Z0-9\-\.\_]+\@[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3})$/;
    if ( !regDoNot.test(strEmail) && regMust.test(strEmail) )
        return true;
    else
      return false;
} 
</script>