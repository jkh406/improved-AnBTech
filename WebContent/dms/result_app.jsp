<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%
	String tablename = request.getParameter("tablename");
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
	location.href = "../servlet/AnBDMS?mode=mylist&tablename=<%=tablename%>&category=1";				
</script>
</BODY>
</html>