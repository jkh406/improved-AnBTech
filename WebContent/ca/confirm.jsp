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
<title>���� Ȯ��</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ca/lib/style.css" type="text/css">
</head>
<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0" oncontextmenu="return false">

<%	if(mode.equals("report_w")){	%>
		<script language='javascript'>
			req_confirm = confirm("�����Ƿڰ� ���������� �̷�������ϴ�. ����ؼ� �ٸ� ������ �����Ƿ��Ͻðڽ��ϱ�?\n\n(Ȯ���� ������ ���߽����Ƿ��۾��� �����ϰ�,��Ҹ� ������ ��� ��ϵ� ������ ���ڰ��縦 �����մϴ�.)");

			if(req_confirm == true){
				location.href = "../servlet/ComponentApprovalServlet?mode=write_m&no=<%=no%>";
			}else{
				location.href = "../gw/approval/module/acknowledgment_FP_App.jsp?mode=report_w&no=<%=no%>";
			}
		</script>
<%	}else{	%>
		<script language='javascript'>
			req_confirm = confirm("���ڰ��縦 �����Ͻðڽ��ϱ�?");

			if(req_confirm == true){
				location.href = "../gw/approval/module/acknowledgment_FP_App.jsp?mode=<%=mode%>&no=<%=no%>";
			}else{
				location.href = "../servlet/ComponentApprovalServlet?mode=list";				
			}
		</script>
<%	}	%>
</BODY>
</html>