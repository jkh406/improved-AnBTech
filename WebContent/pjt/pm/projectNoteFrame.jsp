<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���� ������ �ű��ۼ�[PM��]"		
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
<TITLE> ������ü���� ������ ���[PM��]</TITLE>
<META NAME="Generator" CONTENT="Microsoft FrontPage 5.0">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
</HEAD>

<frameset framespacing="0" border="1" bordercolor=black cols="*" frameborder="0">
	<frameset rows="200,*">
		<frame name="viewT" target="viewT" src="../../servlet/projectNoteServlet?mode=PNT_WV&pjt_code=<%=pjt_code%>&pjt_name=<%=pjt_name%>" scrolling="no" noresize>
	<% if(mode.equals("PNT_NV")) {			//�ű� ������ �ۼ��غ� %>
		<frame name="viewB" target=viewB src="../../servlet/projectNoteServlet?mode=PNT_NV&pjt_code=<%=pjt_code%>&pjt_name=<%=pjt_name%>" scrolling="no" noresize>
	<% } else if(mode.equals("PNT_MV")) { //������ ���� %>
		<frame name="viewB" target=viewB src="../../servlet/projectNoteServlet?mode=PNT_MV&pjt_code=<%=pjt_code%>&pjt_name=<%=pjt_name%>&pid=<%=pid%>" scrolling="no" noresize>
	<% } else if(mode.equals("PNT_CV")) { //������ �ذ᳻�� �ۼ��غ� %>
		<frame name="viewB" target=viewB src="../../servlet/projectNoteServlet?mode=PNT_CV&pjt_code=<%=pjt_code%>&pjt_name=<%=pjt_name%>&pid=<%=pid%>" scrolling="no" noresize>
	<% } %>
	</frameset>
  <noframes>
<BODY>

</BODY>
</noframes>
</frameset>
</HTML>