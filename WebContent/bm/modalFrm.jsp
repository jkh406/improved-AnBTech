<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "MODAL"		
	contentType = "text/html; charset=euc-kr" 
	import="java.io.*"
	import="java.util.*"
%>

<%

String strSrc   = request.getParameter("strSrc");
String pid = request.getParameter("pid");
String model_code = request.getParameter("model_code");
String mode = request.getParameter("mode");
String width = request.getParameter("width");
String height = request.getParameter("height");

%>

<HTML><HEAD><TITLE>내용보기</TITLE>
<LINK href="../bm/css/style.css" rel='stylesheet' type="text/css">
</HEAD>

<BODY style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px;">
<IFRAME id=iframe style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px" src="<%=strSrc%>?pid=<%=pid%>&model_code=<%=model_code%>&mode=<%=mode%>" width="<%=width%>" height="<%=height%>">
</IFRAME>

</BODY>

</HTML>

