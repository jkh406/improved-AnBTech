<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "BOM 변경 검색 Frame"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> BOM 변경 검색 Frame</TITLE>
<META NAME="Generator" CONTENT="Microsoft FrontPage 5.0">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
</HEAD>

<frameset framespacing="0" border="1" bordercolor=black cols="*" frameborder="0">
	<frameset rows="200,*">
		<frame name="search" target="search" src="searchBase.jsp" scrolling="no" noresize>
		<frame name="list" target="list" src="../../servlet/CbomHistoryServlet?mode=sch_base" scrolling="no" noresize> 
	</frameset>
  <noframes>
<BODY>

</BODY>
</noframes>
</HTML>