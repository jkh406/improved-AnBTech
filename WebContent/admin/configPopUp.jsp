<%@ page
	import = "com.anbtech.admin.SessionLib"
%>
<%@ include file="config.jsp"%>

<%
// ���� ó��
SessionLib sl;
sl = (SessionLib)session.getAttribute(session.getId());

String message = "";
if(sl == null){
%>
	<script>
		opener.top.location.href = "<%=server_path%>/admin/notice_session.jsp";
		self.close();
	</script>
<%
	return;
}
// �α��� ����� ����
String login_id = sl.id;
String login_passwd = sl.passwd;
String login_name = sl.name;
String login_div = sl.division;
%>
