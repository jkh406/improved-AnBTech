<%@ include file= "configHead.jsp"%>
<%@ page
	language = "java"
	info = "�����ȣ ����"		
	contentType = "text/html; charset=euc-kr"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%

	/********************************************************************
		 Get the parameter
	*********************************************************************/
	String id			= request.getParameter("id");
	String old_passwd	= request.getParameter("old_passwd");
	String passwd		= request.getParameter("passwd");

	bean.openConnection();
	String sql="select count(passwd) from user_table where id='"+id+"'";
	bean.executeQuery(sql);
	bean.next();

	if (bean.getData(1).equals("0")){ // �Էµ� ID�� DB�� ���� ��
		out.println("<script>");
		out.println("window.alert('�ش� ����ڴ� �������� �ʽ��ϴ�.')");
		out.println("self.close();");
		out.println("</script>");
	}else{
		sql="select count(*) from user_table where id='"+id+"' and passwd = '"+old_passwd+"'";
		bean.executeQuery(sql);
		bean.next();

		if(bean.getData(1).equals("0")){ // ��й�ȣ�� ��ġ���� ���� ��
			out.println("<script>");
			out.println("alert('���� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.')");
			out.println("self.close();");
			out.println("</script>");
		}else{	// ��й�ȣ�� ��ġ�� ��� �����Ѵ�.
			sql = "update user_table set passwd = '"+passwd+"' where id ='"+id+"'";
			bean.executeUpdate(sql);
			out.println("<script>");
			out.println("window.alert('��й�ȣ�� �����Ͽ����ϴ�. ���� �α��νÿ��� ���ο� ��й�ȣ�� �α����Ͻʽÿ�.')");
			out.println("self.close();");
			out.println("</script>");
		}
	}

%>