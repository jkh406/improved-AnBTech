<%@ include file="../admin/configPopUp.jsp"%>
<%@ page		
	info= "�޽��� ���â"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.Hanguel"
%>
<%
	String msg = Hanguel.toHanguel(request.getParameter("msg"));		//�޽�������
			if(msg == null) msg = "��ø� ��ٷ� �ֽʽÿ�.";
	String width = request.getParameter("width");	if(width == null) width = "400";
	String height = request.getParameter("height"); if(height == null) height = "200";
	
	int left = Integer.parseInt(width) / 7;
	int top = Integer.parseInt(height) / 5;

%>
<html>
<HEAD><Title>�޽��� â</Title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet type="text/css">
<BODY>
<TABLE><TBODY><TR><TD><%=msg%>
<DIV id="lding" style="position:absolute;left:<%=left%>;top:<%=top%>;width:250px;height:100px;visibility:visible;">
		<img src='../bm/images/loading8.gif' border='0' width='214' height='200'></DIV>
</TD></TR>
</TBODY>
</TABLE>
</body>
</html>