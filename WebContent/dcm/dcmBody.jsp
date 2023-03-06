<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "설계변경관리"		
	contentType = "text/html; charset=euc-kr" 	
	import="java.sql.*, java.io.*, java.util.*"
%>

<HTML><HEAD><TITLE>설계변경관리</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<META content="MSHTML 6.00.2800.1170" name=GENERATOR></HEAD>
<FRAMESET border=0 frameSpacing=0 frameBorder=0 cols=175,*>
	<FRAMESET border=0 frameSpacing=0 rows=82,*>
		<FRAME name=title-frame marginWidth=0 marginHeight=0 src="dcmTitle.html" frameBorder=NO scrolling=no>
		<FRAME name=menu marginWidth=0 marginHeight=0 src="dcmMenuLeft.jsp" frameBorder=0 noResize>
	</FRAMESET>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/CbomBaseInfoServlet?mode=ecr_prewrite" frameBorder=0 noResize scrolling=yes>
</FRAMESET>
</HTML>