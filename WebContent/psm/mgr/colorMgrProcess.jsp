<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������� ����"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//--------------------
	//	�ʱ�ȭ ����
	//--------------------
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	bean.openConnection();

	/*********************************************************************
	 	���ε����� �ޱ�
	*********************************************************************/
	String query = "",msg="";
	String mode = request.getParameter("mode"); if(mode == null) mode = "";
	String pid = request.getParameter("pid"); if(pid == null) pid = ""; 
	String env_type = Hanguel.toHanguel(request.getParameter("env_type"));
	if(env_type == null) env_type=""; 
	String env_status = Hanguel.toHanguel(request.getParameter("env_status"));
	if(env_status == null) env_status=""; 
	String env_name = Hanguel.toHanguel(request.getParameter("env_name"));
	if(env_name == null) env_name=""; else env_name = env_name.toUpperCase();


	/*********************************************************************
	 	����ϱ�
	*********************************************************************/
	if(mode.equals("ADD")) {
		//��ϵ� �������� �Ǵ��ϱ�
		String sts = "";
		query = "select * from psm_env where env_type='C' and env_status='"+env_status+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			sts = bean.getData("env_status");
		}
		
		//���ǿ����� ����ϱ�
		if(sts.equals(env_status)) { msg = "�̹� ��ϵ� �����Դϴ�."; 
			out.println("<script>alert('"+msg+"');history.go(-1);</script>");
		}else {
			query = "insert into psm_env(pid,env_type,env_status,env_name) values('";
			query += anbdt.getID()+"','"+env_type+"','"+env_status+"','"+env_name+"')";
			bean.execute(query);
			pid=env_type=env_status=env_name="";
			response.sendRedirect("colorMgrList.jsp");
		}

		
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("MOD")) {
		query = "update psm_env set env_type='"+env_type+"',env_status='"+env_status+"',";
		query += "env_name='"+env_name+"' where pid='"+pid+"'";
		bean.execute(query);

		pid=env_type=env_status=env_name="";
		response.sendRedirect("colorMgrList.jsp");
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from psm_env where pid='"+pid+"'";
		bean.execute(query);

		pid=env_type=env_status=env_name="";
		response.sendRedirect("colorMgrList.jsp");
	}	
%>