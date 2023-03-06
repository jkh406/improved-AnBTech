<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "사용자 인증 처리"		
	contentType = "text/html; charset=euc-kr" 
	import="java.io.*"
	import="java.util.*"
%>

<%

String strSrc   = request.getParameter("strSrc");
	   strSrc = "../../servlet/"+strSrc;
String PID = request.getParameter("PID");
String mode = request.getParameter("mode");
String title = request.getParameter("title");

%>

<HTML><HEAD></HEAD><TITLE><%=title%></TITLE>

<BODY style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px;">
<IFRAME id=iframe style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px" src="<%=strSrc%>?PID=<%=PID%>&mode=<%=mode%>" width="720" height="750">
</IFRAME>
</BODY>
</HTML>

