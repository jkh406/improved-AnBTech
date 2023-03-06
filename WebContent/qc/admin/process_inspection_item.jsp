<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	import		= "java.sql.*,com.anbtech.text.Hanguel" 
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query		= "";
	String mid			=   request.getParameter("mid");
	String mode			=	request.getParameter("mode");
	String class_code	=	request.getParameter("class_code");
	String class_name	=	Hanguel.toHanguel(request.getParameter("class_name"));
	String inspection_code	=	request.getParameter("inspection_code");
	String inspection_name	=	Hanguel.toHanguel(request.getParameter("inspection_name"));
	String inspection_result_type	=	Hanguel.toHanguel(request.getParameter("inspection_result_type"));

    bean.openConnection();
		
	if(mode.equals("write")) { // 추가모드
		query = "INSERT INTO qc_inspection_item (inspection_class_code,inspection_class_name,inspection_code,inspection_name,inspection_result_type) values('"+class_code+"','"+class_name+"','"+inspection_code+"','"+inspection_name+"','"+inspection_result_type+"')";
		bean.executeUpdate(query);
		response.sendRedirect("list_inspection_item.jsp?class_code="+class_code);
	} else if (mode.equals("modify")) { //수정모드
		query = "UPDATE qc_inspection_item SET ";
		query += "inspection_class_code='"+class_code+"',";
		query += "inspection_class_name='"+class_name+"',";
		query += "inspection_code='"+inspection_code+"',";
		query += "inspection_name='"+inspection_name+"',";
		query += "inspection_result_type='"+inspection_result_type+"' WHERE ";
		query += "mid='"+mid+"'";
		bean.executeUpdate(query);
		response.sendRedirect("list_inspection_item.jsp?class_code="+class_code);	
	} else if (mode.equals("delete")) { //삭제모드
		query = "DELETE FROM qc_inspection_item WHERE mid ='"+mid+"'";
		bean.executeUpdate(query);
		response.sendRedirect("list_inspection_item.jsp?class_code="+class_code);
	}
%>
