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
//String c_no = request.getParameter("c_no");
String title = request.getParameter("title");
String category_full = Hanguel.toHanguel(request.getParameter("category_full"));


%>

<HTML><HEAD><TITLE><%=title%></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">

<BODY style="PADDING-RIGHT: 3px; PADDING-LEFT: 3px; PADDING-BOTTOM: 3px; MARGIN: 3px; PADDING-TOP: 3px" SCROLL="no">
<IFRAME id=iframe style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px" scroll="no" src="<%=src%>?category_full=<%=category_full%>" width="<%=frmWidth%>" height="<%=frmHeight%>"></IFRAME>

</BODY></HTML>
