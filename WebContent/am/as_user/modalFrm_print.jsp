<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "카테고리 변경"		
	contentType = "text/html; charset=euc-kr"
	import = "com.anbtech.text.Hanguel"
%>

<%
String frmWidth = request.getParameter("frmWidth");
String frmHeight = request.getParameter("frmHeight");
String src = request.getParameter("src");
String as_no = request.getParameter("as_no");
String title = request.getParameter("title");
String pid = request.getParameter("pid");
String h_no = request.getParameter("h_no");
String mode = request.getParameter("mode");

%>

<HTML><HEAD><TITLE><%=title%></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">

<BODY style="PADDING-RIGHT: 3px; PADDING-LEFT: 3px; PADDING-BOTTOM: 3px; MARGIN: 3px; PADDING-TOP: 3px" SCROLL="no">
<IFRAME id=iframe style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px" scroll="no" src="<%=src%>?mode=<%=mode%>&as_no=<%=as_no%>&h_no=<%=h_no%>&pid=<%=pid%>" width="<%=frmWidth%>" height="<%=frmHeight%>"></IFRAME>

</BODY></HTML>
