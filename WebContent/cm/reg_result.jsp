<%@ include file="../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
	errorPage	= "../admin/errorpage.jsp"
%>


<%
	String item_no	= Hanguel.toHanguel(request.getParameter("item_no"));
//	String item_no	= (String)request.getAttribute("ITEM_NOS");
%>

<html>
<head>
<title>ǰ��ä���Ƿڰ��</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../cm/lib/style.css" type="text/css">
</head>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<script language='javascript'>
	alert("ǰ���ȣ : <%=item_no%> �� ä��/��ϵǾ����ϴ�.");
	location.href = "../servlet/CodeMgrServlet?mode=list_item";
</script>
</BODY>
</html>