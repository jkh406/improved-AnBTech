<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "생산관리"		
	contentType = "text/html; charset=euc-kr" 	
	import="java.sql.*, java.io.*, java.util.*"
%>
<%
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	String year = anbdt.getYear();
	String month = anbdt.getMonth();
	String factory_no = "";
	String para = "cal_month&year="+year+"&month="+month+"&factory_no="+factory_no;
%>

<HTML><HEAD><TITLE>생산관리</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<META content="MSHTML 6.00.2800.1170" name=GENERATOR></HEAD>
<FRAMESET border=0 frameSpacing=0 frameBorder=0 cols=175,*>
	<FRAMESET border=0 frameSpacing=0 rows=82,*>
		<FRAME name=title-frame marginWidth=0 marginHeight=0 src="mmTitle.html" frameBorder=NO scrolling=no>
		<FRAME name=menu marginWidth=0 marginHeight=0 src="mmMenuLeft.jsp" frameBorder=0 noResize>
	</FRAMESET>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/mpsInfoServlet?mode=<%=para%>" frameBorder=0 noResize scrolling=yes>
</FRAMESET>
</HTML>