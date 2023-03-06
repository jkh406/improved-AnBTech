<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
%>


<%
	String h_no		= request.getParameter("h_no");
	String as_no	= request.getParameter("as_no");
	String o_status	= request.getParameter("o_status");
	String as_status= request.getParameter("as_status");
%>

<html>
<head>
<title>결재 상신 확인</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ew/css/style.css" type="text/css">
</head>
<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0">

<script language='javascript'>
	req_confirm = confirm("전자결재를 진행하시겠습니까?");
	
	if(req_confirm == true){
		location.href = "../gw/approval/module/Asset_App.jsp?mode=app_asset_view&h_no=<%=h_no%>&as_no=<%=as_no%>&o_status=<%=o_status%>&as_status=<%=as_status%>";		
	}else{
		location.href = "../servlet/AssetServlet?mode=req_app_list";				
	}
</script>
</BODY>
</html>