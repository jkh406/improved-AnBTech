<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�������� ����"		
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
	String query = "",msg="",status="";
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
		query = "select * from psm_env where env_type='P' and env_name='"+env_name+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			sts = bean.getData("env_name");
		}
		
		//���ǿ����� ����ϱ�
		if(sts.equals(env_name)) { msg = "�̹� ��ϵ� �����Դϴ�."; 
			out.println("<script>alert('"+msg+"');history.go(-1);</script>");		
		} else {
			query = "insert into psm_env(pid,env_type,env_status,env_name) values('";
			query += anbdt.getID()+"','"+env_type+"','"+env_status+"','"+env_name+"')";
			bean.execute(query);
			pid=env_type=env_status=env_name="";
			response.sendRedirect("psmMgrList.jsp");
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
		response.sendRedirect("psmMgrList.jsp");
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from psm_env where pid='"+pid+"'";
		bean.execute(query);

		pid=env_type=env_status=env_name="";
		response.sendRedirect("psmMgrList.jsp");
	}
	/*********************************************************************
	 	���뺸��
	*********************************************************************/
	if(mode.equals("VIEW")) {
		query = "select * from psm_env where pid='"+pid+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			pid = bean.getData("pid");
			env_type = bean.getData("env_type");
			env_status = bean.getData("env_status");
			env_name = bean.getData("env_name");
		}
		response.sendRedirect("psmMgrList.jsp");
	}	
%>
