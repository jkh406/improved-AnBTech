<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "�������°���"		
	contentType = "text/html; charset=euc-kr" 	
	import="java.sql.*, java.io.*, java.util.*"
%>

<HTML><HEAD><TITLE>�������°���</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<META content="MSHTML 6.00.2800.1170" name=GENERATOR></HEAD>
<FRAMESET border=0 frameSpacing=0 frameBorder=0 cols=175,*>
	<FRAMESET border=0 frameSpacing=0 rows=82,*>
		<FRAME name=title-frame marginWidth=0 marginHeight=0 src="psmTitle.html" frameBorder=NO scrolling=no>
		<FRAME name=menu marginWidth=0 marginHeight=0 src="psmMenuLeft.jsp" frameBorder=0 noResize>
	</FRAMESET>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/PsmBaseInfoServlet?mode=psm_bylist" frameBorder=0 noResize scrolling=yes>
</FRAMESET>
</HTML>