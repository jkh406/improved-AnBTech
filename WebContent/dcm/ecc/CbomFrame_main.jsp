<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "BOM ���� Frame"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
%>
<%
	//parameter ���޹ޱ�
	String fg_code = request.getParameter("fg_code"); if(fg_code == null) fg_code = "";
	String eco_no = request.getParameter("eco_no"); if(eco_no == null) eco_no = "";
	String para = "fg_code="+fg_code+"&eco_no="+eco_no;

%>
<HTML>
<HEAD>
<TITLE> BOM ���� Frame</TITLE>
</HEAD>

<FRAMESET framespacing="3" border="1" bordercolor=black cols="*" frameborder="0">
	<FRAMESET rows="*,155">
		<FRAMESET cols="50%,*">
			<FRAME name="search" target="search" src="../../servlet/CbomChangeServlet?mode=eco_bomlist&<%=para%>" scrolling="no">
			<FRAME name="change" target="change" src="../../servlet/CbomChangeServlet?mode=eco_chglist&<%=para%>" scrolling="no">
		</FRAMESET>
		<FRAME name="reg" target="reg" src="../../servlet/CbomChangeServlet?mode=eco_prechg&<%=para%>" scrolling="no" noresize> 
	</FRAMESET>
  <NOFRAMES>
<BODY>
</BODY>
</NOFRAMES>
</HTML>