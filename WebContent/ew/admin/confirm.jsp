<%@ page language="java" contentType="text/html;charset=euc-kr" %>


<%
	String mno		= request.getParameter("mno");
	String ono_plus	= request.getParameter("ono_plus");
%>

<html>
<head>
<title>���� ��� Ȯ��</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ew/css/style.css" type="text/css">
</head>
<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0">

<script language='javascript'>
	req_confirm = confirm("���ڰ��縦 �����Ͻðڽ��ϱ�?");

	if(req_confirm == true){
		location.href = "../gw/approval/module/extrawork_FP_App.jsp?mno=<%=mno%>&ono_plus=<%=ono_plus%>";		
	}else{
		location.href = "../servlet/extraWorkServlet?mode=ewReqList";				
	}
</script>
</BODY>
</html>