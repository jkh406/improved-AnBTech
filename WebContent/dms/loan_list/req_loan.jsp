<%@ include file= "../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "com.anbtech.text.Hanguel"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%
	String tablename	= request.getParameter("tablename");
	String category		= request.getParameter("category");
	String searchscope	= request.getParameter("searchscope");
	String searchword	= Hanguel.toHanguel(request.getParameter("searchword"));
	String page_no		= request.getParameter("page");
	String no			= request.getParameter("no");
	String d_id			= request.getParameter("d_id");
	String ver			= request.getParameter("ver");
%>
<html>
<HTML><HEAD><TITLE>대출신청</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_loan.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name="reqLoanForm" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">대출사유</td>
           <td width="80%" height="25" class="bg_04"><textarea name='why' rows="5" cols="40" class="text_01"></textarea></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table></form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:go();'><img src='../images/bt_confirm.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close();'><img src='../images/bt_cancel.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script language='javascript'>

function go() {

	var why = document.reqLoanForm.why.value;
	if(why.length < 3){
		alert("대출신청 사유를 간단히 입력하십시오.(5자 이상)");
		document.reqLoanForm.why.focus();
		return;
	}
	var sParam = "tablename=<%=tablename%>&category=<%=category%>&searchscope=<%=searchscope%>&searchword=<%=searchword%>";
	sParam += "&page=<%=page_no%>&no=<%=no%>&d_id=<%=d_id%>&ver=<%=ver%>&why="+why;
	opener.location.href = "../../servlet/AnBDMS?mode=req_loan&" + sParam;
	self.close();
}
</script>