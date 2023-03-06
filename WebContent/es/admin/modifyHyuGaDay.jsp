<%@ include file= "../../admin/configPopUp.jsp"%>
<%@ include file= "../../admin/chk/chkES01.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String id = request.getParameter("id");
	String kind =  request.getParameter("kind");
	String year = request.getParameter("year");
	String num = request.getParameter("num");

	bean.openConnection();
	String sql = "UPDATE geuntae_hd_max SET hdmax = '" + num + "' WHERE id = '" + id + "' AND byyear = '" + year + "' AND kind = '" + kind + "'";
	bean.executeUpdate(sql);
%>
<HTML><HEAD><TITLE>실행결과</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">
<table border='0' width='100%' height='100%' valign='middle'>
<tr><td align='middle' class='bg_03'>정상적으로 수정되었습니다.</td></tr>
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