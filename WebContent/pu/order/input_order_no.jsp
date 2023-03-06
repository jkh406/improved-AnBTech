<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%
	String items = request.getParameter("items");
%>
<html>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<head>
<title>발주의뢰</title>
</head>

<body topmargin="0" leftmargin="0">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_balju_req.gif" border="0" alt="발주의뢰"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE><BR>

<form name="f1" style="margin:0">
<TABLE  cellspacing=0 cellpadding=0 width="98%" border="0">
	<TBODY>
		<tr bgcolor="c7c7c7"><td height="1" colspan="3"></td></tr>
		<tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">등록구분</td>
           <td width="70%" height="25" class="bg_04"><input type="radio" value="new" checked name="R1" onClick="javascript:document.f1.order_no.readOnly = true;">신규발주 <input type="radio" value="add" name="R1" onClick="javascript:document.f1.order_no.readOnly = false;document.f1.order_no.focus();">품목추가</td></tr>
	    <tr bgcolor="c7c7c7"><td height="1" colspan="3"></td></tr>
		<tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">발주번호</td>
           <td width="70%" height="25" class="bg_04"><input type='text' size='15' name='order_no' value='' class="text_01" readOnly></td></tr>
	    <tr bgcolor="c7c7c7"><td height="1" colspan="3"></td></tr>
		<tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">내외자구분</td>
           <td width="70%" height="25" class="bg_04"><input type='radio' name='inout_type' value='D' checked>내자 <input type='radio' name='inout_type' value='O'>외자</td></tr>
	    <tr bgcolor="c7c7c7"><td height="1" colspan="3"></td></tr>
	   </tbody></table><br>
</form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style='padding-right:10px'><a href="javascript:go('<%=items%>');"><img src="../images/bt_confirm.gif" border="0" align='absmiddle'></a> <a href="javascript:self.close();"><img src="../images/bt_cancel.gif" border="0" align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height="3" bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<Script language = "Javascript">
<!-- 
function go(items){
	var f = document.f1;
	var mode = "order_item";
	var order_no = f.order_no.value;
	var inout_type = "D";
	if(f.inout_type[1].checked) inout_type = "O";

	if(f.R1[1].checked) mode = "order_item_add";

	if(f.R1[1].checked && f.order_no.value == ''){
		alert("발주번호을 입력하십시오.");
		f.order_no.focus();
		return;
	}
	
	opener.location.href = "../../servlet/PurchaseMgrServlet?mode=" + mode + "&item_code=" + items + "&order_no=" + order_no + "&inout_type=" + inout_type;
	self.close();
}
-->
</Script>