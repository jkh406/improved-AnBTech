<%@ include file="../admin/configPopUp.jsp"%>
<%@ page		
	info		= "리비젼형태 선택"		
	contentType = "text/html; charset=euc-kr"
	import		= "com.anbtech.text.Hanguel"
	errorPage	= "../admin/errorpage.jsp"
%>
<%
	String mid = request.getParameter("mid");
%>

<HTML>
<HTML><HEAD><TITLE>모델파생사유</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/pop_revision.gif" alt="모델파생사유선택"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name="revisionForm" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_03" background="images/bg-01.gif">변경내용</td>
           <td width="65%" height="25" class="bg_04">
			  <select size="1" name="why">
				  <option value="d">일부기능,치수,용량,원가절감 등</option>
				  <option value="r">주요기능향상</option>
			  </select>		   
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table></form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:go_revision('<%=mid%>');"><img src='images/bt_confirm.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close();'><img src='images/bt_cancel.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>


<script language='javascript'>
	function go_revision(mid) {
		var why = document.revisionForm.why.value;
		url = "../servlet/GoodsInfoServlet?mode=rev_model&no=" + mid + "&why_revision=" + why;
		opener.location.href = url;
		self.close();
	}
</script>