<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<HTML><HEAD><TITLE>실행결과</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">
<table border='0' width='100%' height='100%' valign='middle'>
<tr><td align='middle' class='bg_03'>정상적으로 구매요청되었습니다.<br>구매요청현황으로 이동하시겠습니까?</td></tr>
<tr><td align='middle'><a href='javascript:go();'><img src='../st/images/bt_confirm.gif' border='0' align='absmiddle'></a> <a href='javascript:win_close();'><img src='../st/images/bt_cancel.gif' border='0' align='absmiddle'></a></td></tr>
</table>
</BODY>
</HTML>

<script language='javascript'>
function win_close(){
	opener.location.reload();
	self.close();
}

function go(){
//	opener.location.href = "../servlet/PurchaseMgrServlet?mode=request_search";
	opener.parent.location.href = "../pu/PuBody.htm";
	self.close();
}
</script>