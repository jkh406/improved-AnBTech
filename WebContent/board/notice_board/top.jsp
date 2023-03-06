<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%@ page import="com.anbtech.board.entity.Board_Env"%>
<%
	//board_env에서 가져오기
	Board_Env board_env = new Board_Env();
	board_env = (Board_Env)request.getAttribute("Board_Env");
	String html_title = board_env.getHtml_title();
	String html_head = board_env.getHtml_head();
	String html_bgcolor = board_env.getHtml_bgcolor();
	String html_background = board_env.getHtml_background();
	String skin = board_env.getSkin();
	String mapping = board_env.getMapping();
%>
<html>
<head>
<title><%=html_title%></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../board/css/style.css" type="text/css">
</head>
<body <%=html_bgcolor%> <%=html_background%> bgColor=#ffffff leftMargin=0 topMargin=0 
marginheight="0" marginwidth="0">