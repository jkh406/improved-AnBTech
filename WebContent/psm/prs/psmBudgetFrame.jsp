<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "苞力 柳青惑怕 包府"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	String psm_code = request.getParameter("psm_code");
	
%>
<HTML>
<HEAD>
<TITLE> 苞力 柳青惑怕 包府</TITLE>
<META NAME="Generator" CONTENT="Microsoft FrontPage 5.0">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
</HEAD>

<frameset framespacing="0" border="1" bordercolor=black cols="*" frameborder="0">
	<frameset rows="250,*">
		<frame name="reg" target="reg" src="../../servlet/PsmBudgetServlet?mode=bud_prewrite&psm_code=<%=psm_code%>" scrolling="no" noresize>
		<frame name="list" target="list" src="../../servlet/PsmBudgetServlet?mode=bud_list&psm_code=<%=psm_code%>" scrolling="no" noresize>
	</frameset>
  <noframes>
<BODY>

</BODY>
</noframes>
</frameset>
</HTML>