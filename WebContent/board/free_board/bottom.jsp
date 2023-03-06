<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%@ page import="com.anbtech.board.entity.Board_Env"%>
<%
	//board_env에서 가져오기
	Board_Env board_env = new Board_Env();
	board_env = (Board_Env)request.getAttribute("Board_Env");
	String html_tail = board_env.getHtml_tail();
%>
</body>
</html>