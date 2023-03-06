<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "공정계획 전개 Frame"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%

	String gid = request.getParameter("pid"); if(gid == null) gid = "";

%>
<HTML>
<HEAD>
<TITLE> MRS 전개 Frame</TITLE>
</HEAD>

<frameset framespacing="0" border="1" bordercolor=black rows="*,266" frameborder="0">
	<frame name="list" target="list" src="../../servlet/mfgInfoServlet?mode=order_list&gid=<%=gid%>" scrolling="no" noresize> 
	<frame name="reg" target="reg" src="../../servlet/mfgInfoServlet?mode=order_view&pid=" scrolling="no" noresize>
	<noframes>
<BODY>

</BODY>
</noframes>
</frameset>
</HTML>