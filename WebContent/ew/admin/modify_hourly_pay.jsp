<%@ include file= "../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String id			= request.getParameter("id");
	String year			= request.getParameter("year");
	String hourly_pay	= request.getParameter("hourly_pay");

	bean.openConnection();
	String sql = "UPDATE member_payinfo SET hourly_pay = '" + hourly_pay + "' WHERE id = '" + id + "' AND this_year = '" + year + "'";
	bean.executeUpdate(sql);
%>
<HTML><HEAD><TITLE>������</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">
<table border='0' width='100%' height='100%' valign='middle'>
<tr><td align='middle' class='bg_03'>���������� �����Ǿ����ϴ�.</td></tr>
<tr><td align='middle'><a href='javascript:save();'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></td></tr>
</table>
</BODY>
</HTML>

<script language='javascript'>
function save(){
	opener.location.reload();
	self.close();
}
</script>