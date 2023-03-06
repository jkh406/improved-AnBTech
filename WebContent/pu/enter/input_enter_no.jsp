<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%
	String items = request.getParameter("items");
	String supplyer_code = request.getParameter("supplyer_code");
%>
<html>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<head>
<title>입고등록</title>
</head>

<body topmargin="0" leftmargin="0" oncontextmenu="return false">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_input_reg.gif" border="0" alt="입고번호입력"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE><BR>

<form name="f1" style="margin:0">
<TABLE  cellspacing=0 cellpadding=0 width="98%" border="0">
	<TBODY>
		<tr bgcolor="c7c7c7"><td height="1" colspan="3"></td></tr>
		<tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">등록구분</td>
           <td width="70%" height="25" class="bg_04"><input type="radio" value="new" checked name="R1" onClick="javascript:document.f1.enter_no.readOnly = true;">신규입고 <input type="radio" value="add" name="R1" onClick="javascript:document.f1.enter_no.readOnly = false;document.f1.enter_no.focus();">추가입고</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="3"></td></tr>
		<tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">입고번호</td>
           <td width="70%" height="25" class="bg_04"><input type='text' size='15' name='enter_no' value='' class="text_01" readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="3"></td></tr>
	   </tbody></table><br>
</form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style='padding-right:10px'><a href="javascript:go('<%=items%>','<%=supplyer_code%>');"><img src="../images/bt_confirm.gif" border="0" align='absmiddle'></a> <a href="javascript:self.close();"><img src="../images/bt_cancel.gif" border="0" align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height="3" bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<Script language = "Javascript">
<!-- 
function go(items,supplyer_code){
	var f = document.f1;
	var mode = "enter_item";
	var enter_no = f.enter_no.value;

	if(f.R1[1].checked) mode = "enter_item_add";

	if(f.R1[1].checked && f.enter_no.value == ''){
		alert("입고번호을 입력하십시오.");
		f.enter_no.focus();
		return;
	}
	
	opener.location.href = "../../servlet/PurchaseMgrServlet?mode=" + mode + "&item_code=" + items + "&enter_no=" + enter_no + "&supplyer_code=" + supplyer_code;
	self.close();
}
-->
</Script>