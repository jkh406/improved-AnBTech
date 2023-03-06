<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../admin/errorpage.jsp"
%>
<%
	String no	= request.getParameter("no");
	String mode = request.getParameter("mode");
%>

<html>
<head>
<title>저장 확인</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ca/lib/style.css" type="text/css">
</head>
<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0" oncontextmenu="return false">

<script language='javascript'>
	req_confirm = confirm("선택하신 문서들을 상신하시겠습니까?");

	if(req_confirm == true){
		location.href = "../gw/approval/module/acknowledgment_FP_App.jsp?mode=<%=mode%>&no=<%=no%>";
	}else{
		location.href = "../servlet/ComponentApprovalServlet?mode=mylist";				
	}
</script>
</BODY>
</html>