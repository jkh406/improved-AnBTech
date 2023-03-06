<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>

<HTML><HEAD><TITLE>저장결과</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">
<table border='0' width='100%' height="100%" valign='middle'>
<tr><td align='middle' class='bg_03'>정상적으로 저장되었습니다.<br><br><a href='javascript:win_close();'><img src='../st/images/bt_confirm.gif' border='0' align='absmiddle'></a></td></tr>
</table>
</BODY>
</HTML>

<script language='javascript'>
function win_close(){
	opener.location.reload();
	self.close();
}

function go(){
	opener.parent.location.href = "../servlet/PurchaseMgrServlet?mode=list_memo";
	self.close();
}
</script>