<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제 이슈 신규작성[PM용]"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	String pjt_code = request.getParameter("pjt_code");
	String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"));

	String mode = request.getParameter("mode");
	String pid = request.getParameter("pid");
	
%>
<HTML>
<HEAD>
<TITLE> 과제전체적인 이슈 등록[PM용]</TITLE>
<META NAME="Generator" CONTENT="Microsoft FrontPage 5.0">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
</HEAD>

<frameset framespacing="0" border="1" bordercolor=black cols="*" frameborder="0">
	<frameset rows="200,*">
		<frame name="viewT" target="viewT" src="../../servlet/projectIssueServlet?mode=PIS_WV&pjt_code=<%=pjt_code%>&pjt_name=<%=pjt_name%>" scrolling="no" noresize>
	<% if(mode.equals("PIS_NV")) {			//신규 이슈 작성준비 %>
		<frame name="viewB" target=viewB src="../../servlet/projectIssueServlet?mode=PIS_NV&pjt_code=<%=pjt_code%>&pjt_name=<%=pjt_name%>" scrolling="no" noresize>
	<% } else if(mode.equals("PIS_MV")) { //이슈 수정 %>
		<frame name="viewB" target=viewB src="../../servlet/projectIssueServlet?mode=PIS_MV&pjt_code=<%=pjt_code%>&pjt_name=<%=pjt_name%>&pid=<%=pid%>" scrolling="no" noresize>
	<% } else if(mode.equals("PIS_CV")) { //이슈 해결내용 작성준비 %>
		<frame name="viewB" target=viewB src="../../servlet/projectIssueServlet?mode=PIS_CV&pjt_code=<%=pjt_code%>&pjt_name=<%=pjt_name%>&pid=<%=pid%>" scrolling="no" noresize>
	<% } %>
	</frameset>
  <noframes>
<BODY>

</BODY>
</noframes>
</frameset>
</HTML>