<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.text.DecimalFormat"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query	= "";
	String j		=	request.getParameter("j");
	String ar_id	=	request.getParameter("ar_id");
	String rank_name		=	Hanguel.toHanguel(request.getParameter("rank_name"));
	String rank_code		=	Hanguel.toHanguel(request.getParameter("rank_code"));
	String rank_priorty		=	Hanguel.toHanguel(request.getParameter("rank_priorty"));

    bean.openConnection();

	if(j.equals("a")) { //  추가
		query = " insert into rank_table (ar_name,ar_code,ar_priorty) values('"+rank_name+"','"+rank_code+"','"+rank_priorty+"')";
		bean.executeUpdate(query);

	} else if (j.equals("u")) { // 수정모드
		query = "update rank_table set ";
		query += "ar_name='"+rank_name+"',";
		query += "ar_code='"+rank_code+"',";
		query += "ar_priorty='"+rank_priorty+"' ";
		query += "where ar_id = '"+ar_id+"'";
		bean.executeUpdate(query);

	} else if (j.equals("d")) { // 삭제모드
		query = "delete from rank_table where ar_id ='"+ar_id+"'";
		bean.executeUpdate(query);
	}

	response.sendRedirect("rankl.jsp");	
%>
