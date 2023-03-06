<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "사용자 인증 처리"		
	contentType = "text/html; charset=euc-kr" 		
%>

<%
String strSrc   = request.getParameter("strSrc");
String pid   = request.getParameter("pid");
String mode = request.getParameter("mode");
String pjt_code = request.getParameter("pjt_code");

%>

<HTML><HEAD><TITLE></TITLE>

<BODY style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px" scroll=no>
<IFRAME id=iframe style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px" src="<%=strSrc%>?pid=<%=pid%>&mode=<%=mode%>&pjt_code=<%=pjt_code%>" width="720" height="750"></IFRAME>
</BODY></HTML>

