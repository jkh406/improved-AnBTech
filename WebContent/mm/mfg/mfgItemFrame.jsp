<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���� ��ǰ�ҿ䷮ ���� ���� Frame"		
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
	String factory_no = request.getParameter("factory_no"); if(factory_no == null) factory_no = "";
	String mfg_no = request.getParameter("mfg_no"); if(mfg_no == null) mfg_no = "";
	String para = "gid="+gid+"&factory_no="+factory_no+"&mfg_no="+mfg_no;
%>
<HTML>
<HEAD>
<TITLE> ���� ��ǰ�ҿ䷮���� ���� Frame</TITLE>
</HEAD>

<frameset framespacing="0" border="1" bordercolor=black rows="*,108" frameborder="0">
	<frame name="list" target="list" src="../../servlet/mfgInfoServlet?mode=item_list&<%=para%>" scrolling="no" noresize> 
	<frame name="reg" target="reg" src="../../servlet/mfgInfoServlet?mode=item_view&pid=" scrolling="no" noresize>
	<noframes>
<BODY>

</BODY>
</noframes>
</frameset>
</HTML>