<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>

<%
	String target = Hanguel.toHanguel(request.getParameter("target"));
%>

<html>

<head>
<meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ks_c_5601-1987">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>����ȭ��</title>
</head>

<BODY background="" marginwidth="0" topmargin="0" leftmargin="2">
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
  <tr>
    <td width="60%" height="100%">
    <iframe name="tree" src="../admin/UserTreeMainForMulti.jsp" width="100%" height="100%" border="0" frameborder="0">
    �������� �ζ��� �������� �������� �ʰų� ���� �ζ��� �������� ǥ������ �ʵ��� �����Ǿ� �ֽ��ϴ�.</iframe></td>
    <td width="40%" height="100%">
    <iframe name="list" src="ReceiverList.jsp?target=<%=target%>" width="100%" height="100%" border="0" frameborder="0">
    �������� �ζ��� �������� �������� �ʰų� ���� �ζ��� �������� ǥ������ �ʵ��� �����Ǿ� �ֽ��ϴ�.</iframe></td>
  </tr>
</table>
</BODY>

</html>
