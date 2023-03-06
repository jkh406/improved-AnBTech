<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "생산실적 Frame"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	
	String mfg_no="",assy_code="",level_no="",factory_no="",para="",mfg_count="";
	String src_list="",src_reg="";


	mfg_no = request.getParameter("mfg_no"); if(mfg_no == null) mfg_no = "";
	assy_code = request.getParameter("assy_code"); if(assy_code == null) assy_code = "";
	level_no = request.getParameter("level_no"); if(level_no == null) level_no = "";
	factory_no = request.getParameter("factory_no"); if(factory_no == null) factory_no = "";
	mfg_count = request.getParameter("mfg_count"); if(mfg_count == null) mfg_count = "";
	para = "mfg_no="+mfg_no+"&assy_code="+assy_code+"&level_no="+level_no+"&factory_no="+factory_no;
	para += "&mfg_count="+mfg_count;
	src_list = "../../servlet/mfgOrderServlet?mode=product_list&"+para;
	src_reg = "../../servlet/mfgOrderServlet?mode=product_preview&pid=&"+para;

	
%>
<HTML>
<HEAD>
<TITLE> 실적등록 전개 Frame</TITLE>
</HEAD>

<frameset framespacing="0" border="1" bordercolor=black rows="*,162" frameborder="0">
	<frame name="list" target="list" src="<%=src_list%>" scrolling="no" noresize> 
	<frame name="reg" target="reg" src="<%=src_reg%>" scrolling="no" noresize>
	<noframes>
<BODY>

</BODY>
</noframes>
</frameset>
</HTML>