<%@ include file="../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../admin/errorpage.jsp"
%>

<%
	String tablename	= request.getParameter("tablename");
	String category		= request.getParameter("category");
	String t_id			= request.getParameter("t_id");
	String data_id		= request.getParameter("data_id");
	String version		= request.getParameter("ver");
%>

<html>
<head>
<title>저장 확인</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../dms/lib/style.css" type="text/css">
</head>
<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0">

<script language='javascript'>
	req_confirm = confirm("전자결재를 진행하시겠습니까?");

	if(req_confirm == true){
		location.href = "../gw/approval/module/technical_FP_App.jsp?mode=report&tablename=<%=tablename%>&data_id=<%=data_id%>&ver=<%=version%>&category=<%=category%>";
//		location.href = "../servlet/AnBDMS?tablename=<%=tablename%>&mode=report&d_id=<%=data_id%>&ver=<%=version%>";
	}else{
		location.href = "../servlet/AnBDMS?mode=mylist&tablename=<%=tablename%>&category=1";				
	}
</script>
</BODY>
</html>