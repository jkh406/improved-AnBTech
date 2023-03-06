<%@ include file="../../../admin/configHead.jsp"%>
<%@ include file="../../../admin/chk/chkDM01.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel" 
	contentType	= "text/HTML;charset=KSC5601"
	errorPage	= "../../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String au_id		= request.getParameter("au_id");
	String ac_id		= request.getParameter("ac_id");
	String access_code  = request.getParameter("authorityString");

    bean.openConnection();
	String query = "UPDATE user_table SET access_code = '" + access_code + "' WHERE au_id = '" + au_id + "'";
	bean.executeUpdate(query);
	response.sendRedirect("view.jsp?au_id="+au_id+"&ac_id="+ac_id);	
%>
