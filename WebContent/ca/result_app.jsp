<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../admin/errorpage.jsp"
%>

<html>
<head>
<title>��Ű��Ȯ��</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ca/lib/style.css" type="text/css">
</head>
<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0">

<script language='javascript'>
	alert("���������� ��ŵǾ����ϴ�.");
	location.href = "../servlet/ComponentApprovalServlet?mode=mylist";				
</script>

</BODY>
</html>