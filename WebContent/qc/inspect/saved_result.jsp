<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<html>
<head>
<title>������</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<LINK href="../qc/css/style.css" rel=stylesheet>

</head>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false" onLoad='centerWindow();'>
<table border='0' width='100%' height='100%' valign='middle'>
	<tr><td align='middle' class='bg_03'>���������� ����Ǿ����ϴ�.</td></tr>
	<tr><td align='middle'><a href='javascript:save();'><img src='../qc/images/bt_close.gif' border='0' align='absmiddle'></a></td></tr>
</table>
</BODY>
</html>

<script language='javascript'>
function centerWindow() 
{ 
        var sampleWidth = 300;                        // �������� ���� ������ ���� 
        var sampleHeight =150 ;                       // �������� ���� ������ ���� 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
}

function save(){
	opener.location.reload();
	self.close();
}
</script>