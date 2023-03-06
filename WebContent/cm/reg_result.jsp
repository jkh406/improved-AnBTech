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
<title>품목채번의뢰결과</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../cm/lib/style.css" type="text/css">
</head>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<script language='javascript'>
	alert("품목번호 : <%=item_no%> 로 채번/등록되었습니다.");
	location.href = "../servlet/CodeMgrServlet?mode=list_item";
</script>
</BODY>
</html>