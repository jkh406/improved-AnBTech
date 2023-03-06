<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "카테고리 변경"		
	contentType = "text/html; charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp" 
	import = "com.anbtech.text.Hanguel"
%>

<%
String frmWidth = request.getParameter("frmWidth");
String frmHeight = request.getParameter("frmHeight");
String src = request.getParameter("src");
//String title = request.getParameter("title");
%>

<HTML><HEAD><TITLE>감가처리</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">

<BODY style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px" SCROLL="no">
<IFRAME id=iframe style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px" scroll="no" src="<%=src%>? width="<%=frmWidth%>" height="<%=frmHeight%>"></IFRAME>

</BODY></HTML>
