<%@ page		
	info= "����� ���� ó��"		
	contentType = "text/html; charset=euc-kr" 		
	import="com.anbtech.admin.SessionLib"
	import="com.anbtech.admin.CurLogin"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	String user_id			= "";
	String user_password	= "";
	String user_name		= "";
	String user_division	= "";
	String prg_privilege	= "";
	String sql				= "";
	String sel_count		= "0";

	SessionLib sl;

	sql = "select count(id) from user_table where id = '"+request.getParameter("id")+"'";
	bean.openConnection();	
	bean.executeQuery(sql);
	while(bean.next()){
		sel_count = bean.getData(1);
	}

	if (sel_count.equals("0")) { 
%>
	<Script Language='JavaScript'>
		alert('�Է��Ͻ� ����� �������� �ʽ��ϴ�.');
		history.back(-1);
	</Script>

<%
	} else { 
//		sql = "select id,name,passwd from user_table where id = '"+request.getParameter("id")+"' and passwd =password('"+request.getParameter("passwd")+"')";
		sql = "select a.id,a.name,a.passwd,b.ac_name from user_table a,class_table b where a.id = '"+request.getParameter("id")+"' and a.passwd = '"+request.getParameter("passwd")+"' and a.ac_id = b.ac_id";
		bean.executeQuery(sql);
		while(bean.next()){
			user_id = bean.getData("id");
			user_password = bean.getData("passwd");
			user_name = bean.getData("name");
			user_division = bean.getData("ac_name");
		}

		if (user_id.equals("")) {
%>				
				<Script Language='JavaScript'>
					alert('��й�ȣ�� Ʋ���ϴ�. Ȯ���Ͻ� �� �ٽ� �õ��Ͻʽÿ�.\n\n(��й�ȣ�� ��ҹ��ڸ� �����մϴ�.)');
					history.back(-1);
				</Script>
<%
		} else {
				/*���α׷� ��������� üũ*/
				sql = "select code_s from prg_privilege where owner like '%"+user_id+"%'";
				bean.executeQuery(sql);
				while(bean.next()){
					prg_privilege += bean.getData("code_s");
				}

				sl = new SessionLib(user_id, user_password, user_name, user_division, prg_privilege);
				session.setAttribute(session.getId(), sl);
				//���������ð��� 10������ ����
				session.setMaxInactiveInterval(60*10);

				//���� �α����� ����� ����Ʈ�� ���� ����.
//				CurLogin curLogin = new CurLogin(user_id);
//				session.setAttribute("curLogin",curLogin);

/*
				out.print("id:"+user_id+"<br>");
				out.print("name:"+user_name+"<br>");
				out.print("pwd:"+user_password+"<br>");
				out.print("owner:"+prg_privilege+"<br>");

*/				//response.sendRedirect("main.jsp"); 
//				response.sendRedirect("checkMessage.jsp");

				response.sendRedirect("../gw.htm");
		}
	}
%>