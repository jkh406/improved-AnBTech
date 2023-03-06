<%@ page		
	info= "입력항목선택"		
	contentType = "text/html; charset=euc-kr"
	import = "com.anbtech.text.Hanguel"
%>

<%
String frmWidth		= request.getParameter("frmWidth");
String frmHeight	= request.getParameter("frmHeight");
String src			= request.getParameter("src");
String c_class		= request.getParameter("c_class");
%>

<HTML><HEAD><TITLE>입력항목선택</TITLE>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<IFRAME id='iframe' src="<%=src%>?c_class=<%=c_class%>" width="380" height="100%" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="yes"></IFRAME>

</BODY></HTML>
