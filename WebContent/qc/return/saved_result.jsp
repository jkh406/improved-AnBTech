<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<html>
<head>
<title>저장결과</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../qc/lib/style.css" type="text/css">
</head>
<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0">

<script language='javascript'>
	opener.location.reload();
	alert("정상적으로 등록되었습니다.");
	self.close();
</script>
</BODY>
</html>