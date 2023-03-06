<%@ include file="../../checkAdmin.jsp"%>
<%@ include file= "../../configHead.jsp"%>
<%@ page language="java" import="java.sql.*,java.io.*,com.anbtech.text.Hanguel" contentType="text/html;charset=KSC5601" %>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query		= "";
	String mid			=   request.getParameter("mid");
	String mode			=	request.getParameter("mode");
	String type			=	request.getParameter("type");
	String type_name	=	Hanguel.toHanguel(request.getParameter("type_name"));
	String code			=	request.getParameter("code");
	String code_name	=	Hanguel.toHanguel(request.getParameter("code_name"));

    bean.openConnection();
		
	if(mode.equals("write")) { // 등록코드 추가모드

		query = " SELECT count(*) FROM system_minor_code WHERE type='"+type+"' and code='"+code+"'";
		bean.executeQuery(query);
		bean.next();
		if(Integer.parseInt(bean.getData(1)) > 0) {
	%>
			<script language='javascript'> alert('이미 등록된 정보가 있습니다.');history.go(-1);</script>
	<%
		} else {
		
		query = " INSERT INTO system_minor_code (type,type_name,code,code_name,fixed) values('"+type+"','"+type_name+"','"+code+"','"+code_name+"','n')";
		bean.executeUpdate(query);
		response.sendRedirect("minor_list.jsp?type="+type);
		
		}
		
	} else if (mode.equals("modify")) { // 코드 수정모드
			
		query = " SELECT count(*) FROM system_minor_code WHERE type='"+type+"' and code='"+code+"'";
		bean.executeQuery(query);
		bean.next();
		if(Integer.parseInt(bean.getData(1)) >= 2) {
	%>
			<script language='javascript'> alert('이미 등록된 정보가 있습니다.');history.go(-1);</script>
	<%
		} else {
		query = "UPDATE system_minor_code SET ";
		query += "type='"+type+"',";
		query += "type_name='"+type_name+"',";
		query += "code='"+code+"',";
		query += "code_name='"+code_name+"' WHERE ";
		query += "mid='"+mid+"'";
		bean.executeUpdate(query);
		response.sendRedirect("minor_list.jsp?type="+type);	
		}
	} else if (mode.equals("delete")) { // 코드 삭제모드
		query = "DELETE FROM system_minor_code WHERE mid ='"+mid+"'";
		bean.executeUpdate(query);
		response.sendRedirect("minor_list.jsp?type="+type);
	}

	
%>
