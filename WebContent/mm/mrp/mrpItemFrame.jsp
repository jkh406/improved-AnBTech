<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MRS 전개 Frame"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%

	String pid = request.getParameter("pid"); if(pid == null) pid = "";
	String mrp_no = request.getParameter("mrp_no"); if(mrp_no == null) mrp_no = "";
	String fg_code = request.getParameter("fg_code"); if(fg_code == null) fg_code = "";
	String item_code = request.getParameter("item_code"); if(item_code == null) item_code = "";
	String mrp_start_date = request.getParameter("mrp_start_date"); 
	if(mrp_start_date == null) mrp_start_date = "";
	String mrp_count = request.getParameter("mrp_count"); if(mrp_count == null) mrp_count = "";
	String factory_no = request.getParameter("factory_no"); if(factory_no == null) factory_no = "";
	String mrp_status = request.getParameter("mrp_status"); if(mrp_status == null) mrp_status = "";
	String stock_link = request.getParameter("stock_link"); if(stock_link == null) stock_link = "1";
	String p_count = request.getParameter("p_count"); if(p_count == null) p_count = "0";
	
	String para = "pid="+pid+"&mrp_no="+mrp_no+"&fg_code="+fg_code+"&item_code="+item_code;
	para += "&mrp_start_date="+mrp_start_date+"&mrp_count="+mrp_count+"&factory_no="+factory_no;
	para += "&mrp_status="+mrp_status+"&stock_link="+stock_link+"&p_count="+p_count;

%>
<HTML>
<HEAD>
<TITLE> MRS 전개 Frame</TITLE>
</HEAD>

<FRAMESET framespacing="0" border="1" bordercolor=black rows="*,110" frameborder="0">
	<FRAME name="list" target="list" src="../../servlet/mrpInfoServlet?mode=item_list&<%=para%>" scrolling="no" noresize> 
	<FRAME name="reg" target="reg" src="../../servlet/mrpInfoServlet?mode=item_presave&<%=para%>" scrolling="no" noresize>
	<NOFRAMES>
<BODY>
</BODY>
</NOFRAMES>
</FRAMESET>
</HTML>