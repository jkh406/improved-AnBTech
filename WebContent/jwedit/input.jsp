<%@ page contentType="text/html;charset=MS949"%>

<%
	// request�κ��� ���۹޴� �����͸� MS949�� ���ڵ��ϰ� �����Ͽ� �ѱ� �Է°��� ������ �߰� ��������
	// ���� ó���ϰ� �մϴ�.
	// edit.jsp���� ���۵� �����ʹ� �Ϲ����� POST����̹Ƿ� Multipart�� ���� ó���� �ʿ� �����ϴ�.
	request.setCharacterEncoding("MS949");
%>

���� : <%=request.getParameter("title")%><br>
�ۼ��� : <%=request.getParameter("name")%><br>
��ȣ : <%=request.getParameter("pw")%><br><br>
=== ������ =============================================<br><br>

<%
	out.println(request.getParameter("htmlBODY"));
%>
