<%@ page		
	info= "전자결재 입장하기"		
	contentType = "text/html; charset=euc-kr" 		
%>
<%
	String mode = request.getParameter("mode");
	if(mode == null) mode = "APP_ING";
%>
<HTML><HEAD><TITLE>전자결재</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<META content="MSHTML 6.00.2800.1170" name=GENERATOR></HEAD>
<FRAMESET border=0 frameSpacing=0 frameBorder=0 cols=175,*>
	<FRAMESET border=0 frameSpacing=0 rows=82,*>
		<FRAME name=title-frame marginWidth=0 marginHeight=0 src="ApprovalTitle.htm" frameBorder=NO scrolling=no>
		<FRAME name=menu marginWidth=0 marginHeight=0 src="../../servlet/ApprovalInitServlet?mode=menu" frameBorder=0 noResize>
	</FRAMESET>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../../servlet/ApprovalMenuServlet?mode=<%=mode%>" frameBorder=0 noResize scrolling=yes>
</FRAMESET>
</HTML>