<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "유지보수 실적/평가관리"		
	contentType = "text/html; charset=euc-kr" 	
	import="java.sql.*, java.io.*, java.util.*"
%>

<%
	//권한알아봐 메뉴출력 제어하기
	String mgr = "WK";
	
%>
<HTML><HEAD><TITLE>유지보수 실적평가관리</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<META content="MSHTML 6.00.2800.1170" name=GENERATOR></HEAD>
<FRAMESET border=0 frameSpacing=0 frameBorder=0 cols=175,*>
	<FRAMESET border=0 frameSpacing=0 rows=82,*>
		<FRAME name=title-frame marginWidth=0 marginHeight=0 src="mrTitle.htm" frameBorder=NO scrolling=no>
		<FRAME name=menu marginWidth=0 marginHeight=0 src="mrMenuLeft.jsp" frameBorder=0 noResize>
	</FRAMESET>
<% if(mgr.indexOf("WK") != -1) {				//업체%>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/asresultworkServlet?mode=ART_L" frameBorder=0 noResize scrolling=yes>
<% } else if(mgr.indexOf("DV") != -1) {			//사업부%>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/asresultdivServlet?mode=ART_L" frameBorder=0 noResize scrolling=yes>
<% } else if(mgr.indexOf("IT") != -1) {			//IT%>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/asresultitServlet?mode=ART_SL" frameBorder=0 noResize scrolling=yes>
<% } %>
</FRAMESET>
</HTML>