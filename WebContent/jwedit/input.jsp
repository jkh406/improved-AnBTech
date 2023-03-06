<%@ page contentType="text/html;charset=MS949"%>

<%
	// request로부터 전송받는 데이터를 MS949로 인코딩하게 설정하여 한글 입력값을 별도의 추가 과정없이
	// 직접 처리하게 합니다.
	// edit.jsp에서 전송된 데이터는 일반적인 POST방식이므로 Multipart에 대한 처리가 필요 없습니다.
	request.setCharacterEncoding("MS949");
%>

제목 : <%=request.getParameter("title")%><br>
작성자 : <%=request.getParameter("name")%><br>
암호 : <%=request.getParameter("pw")%><br><br>
=== 컨텐츠 =============================================<br><br>

<%
	out.println(request.getParameter("htmlBODY"));
%>
