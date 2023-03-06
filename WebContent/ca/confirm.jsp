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

<%	if(mode.equals("report_w")){	%>
		<script language='javascript'>
			req_confirm = confirm("승인의뢰가 정상적으로 이루어졌습니다. 계속해서 다른 문서를 승인의뢰하시겠습니까?\n\n(확인을 누르면 다중승인의뢰작업을 진행하고,취소를 누르면 방금 등록된 문건의 전자결재를 진행합니다.)");

			if(req_confirm == true){
				location.href = "../servlet/ComponentApprovalServlet?mode=write_m&no=<%=no%>";
			}else{
				location.href = "../gw/approval/module/acknowledgment_FP_App.jsp?mode=report_w&no=<%=no%>";
			}
		</script>
<%	}else{	%>
		<script language='javascript'>
			req_confirm = confirm("전자결재를 진행하시겠습니까?");

			if(req_confirm == true){
				location.href = "../gw/approval/module/acknowledgment_FP_App.jsp?mode=<%=mode%>&no=<%=no%>";
			}else{
				location.href = "../servlet/ComponentApprovalServlet?mode=list";				
			}
		</script>
<%	}	%>
</BODY>
</html>