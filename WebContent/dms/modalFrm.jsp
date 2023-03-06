<%@ page		
	info= "문서분류선택"		
	contentType = "text/html; charset=euc-kr"
	import = "com.anbtech.text.Hanguel"
%>

<%
String frmWidth = request.getParameter("frmWidth");
String frmHeight = request.getParameter("frmHeight");
String src = request.getParameter("src");
String category = request.getParameter("category");
String category_info = Hanguel.toHanguel(request.getParameter("category_info"));
String title = request.getParameter("title");
%>

<HTML><HEAD><TITLE>문서분류선택</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">

<BODY style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px">
<IFRAME id=iframe style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px" src="<%=src%>?category_info=<%=category_info%>&category=<%=category%>" width="<%=frmWidth%>" height="<%=frmHeight%>" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no"></IFRAME>

</BODY></HTML>
