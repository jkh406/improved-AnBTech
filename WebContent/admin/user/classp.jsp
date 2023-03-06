<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.text.DecimalFormat"
	contentType	= "text/html;charset=KSC5601"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String query	= "";
	String j		= request.getParameter("j")==null?"":request.getParameter("j");
	String ac_id	= request.getParameter("ac_id")==null?"":request.getParameter("ac_id");
	String ac_name	= request.getParameter("ac_name") == ""?"":Hanguel.toHanguel(request.getParameter("ac_name"));
	String ac_code	= request.getParameter("ac_code") == ""?"":request.getParameter("ac_code");
	String chief_id	= request.getParameter("chief_id") == ""?"":request.getParameter("chief_id");
	String ac_level	= request.getParameter("ac_level") == ""?"":request.getParameter("ac_level");
	String code		= request.getParameter("code") == ""?"":request.getParameter("code");
	String isuse	= request.getParameter("isuse")== null?"":request.getParameter("isuse");
		
	String error = "no";
	
	bean.openConnection();

	///////////////
	// ȸ���Է�
	///////////////
	if(j.equals("")) {
		//ȸ���ڵ� �ߺ����� �˻�
		query = "SELECT COUNT(*) FROM class_table WHERE ac_code = '" + ac_code + "' and ac_level='0' and isuse='y'";
		bean.executeQuery(query);
		bean.next();
		String sel_count = bean.getData(1);
		
		//�ߺ��ڵ��� ��� �޽��� ���
		if(!sel_count.equals("0")) {
%>
				<script>alert('ȸ���ڵ尡 �ߺ��˴ϴ�. ȸ�� �ڵ带 ������ ����Ͻʽÿ�.'); history.go(-1);</script>
<%
		//�ߺ��ڵ尡 �ƴҰ�� ��� �Է�ó��
		}else{
			query = "INSERT INTO class_table(ac_name,ac_code,ac_level,ac_ancestor,code,chief_id,isuse) VALUES('" + ac_name + "','" + ac_code + "', '0', '0','"+ac_code+"','','y')";
			bean.executeUpdate(query);
			response.sendRedirect("classl.jsp");
		}
	}

	///////////////////
	// �μ� ���� ���� ó��
	///////////////////
	else if(j.equals("u")) {
		if(isuse.equals("n")) { // �μ� ��� ��Ȱ��ȭ ó��
			query = " select count(au_id) from user_table where ac_id ='" + ac_id +"'";
			bean.executeQuery(query);
			bean.next();

			//���� ��Ȱ��ȭó����Ű�����ϴ� ������ �������� ���ԵǾ� ���� ��� ���� �޽��� ���
			if(Integer.parseInt(bean.getData(1)) > 0)  {
				error = "yes";
%>
				<script>alert("�� �μ��� ���Ե� �������� �־� �μ��� ��Ȱ��ȭ��ų �� �����ϴ�.");history.go(-2);</script>
<%
			}

			query = " select count(ac_id) from class_table where ac_ancestor ='" + ac_id +"' and isuse='y'";
			bean.executeQuery(query);
			bean.next();

			//���� ��Ȱ��ȭó����Ű�����ϴ� ������ ���������� ���� ��� ���� �޽��� ���
			if(Integer.parseInt(bean.getData(1)) > 0) {
				error = "yes";
%>
				<script>alert("�� �μ��� ���� ������ �־� �μ��� ��Ȱ��ȭ��ų �� �����ϴ�.");history.go(-2);</script>
<%
			}
			
			//������ ���� ��쿡 �μ� ��Ȱ��ȭ ó��
			if(error.equals("no")){
				query = "update class_table set isuse='n' where ac_id='"+ac_id+"'";
				bean.executeUpdate(query);
				response.sendRedirect("classl.jsp");
			}

		} else { // �μ� ��� ��Ȱ��ȭ ó������ ������ ������ ���
			// class_table �μ� ���� update
			query = "update class_table set ac_name='"+ac_name+"', ac_code='"+ac_code+"', chief_id = '" + chief_id + "',isuse='"+isuse+"' where ac_id = '"+ac_id+"'";
			bean.executeUpdate(query);
			
			// user_table �μ��� ���� update
			query = "update user_table set ac_code='"+ac_code+"' where code='"+code+"'";
			bean.executeUpdate(query);
			out.print("<script>location.href='classl.jsp';</script>");
			response.sendRedirect("classl.jsp");		
		}
	}

	///////////////////
	// �����з� �߰�
	///////////////////
	else if(j.equals("a")) {	
		String code_str = "";	

		query = "SELECT MAX(code) FROM class_table WHERE ac_ancestor = '"+ac_id+"' and isuse='y'";
		bean.executeQuery(query);
		bean.next();
		
		if(bean.getData(1) == null){
			code_str = code + "01";
		} else {
			int code_len = bean.getData(1).length();
			DecimalFormat fmt = new DecimalFormat("00");
			code_str = fmt.format(Integer.parseInt(bean.getData(1).substring(code_len-2,code_len))+1);
			code_str = bean.getData(1).substring(0,code_len-2)+code_str;
		}

		query = " insert into class_table(ac_name,ac_code,ac_level,ac_ancestor,code,chief_id,isuse) values ('"+ac_name+"','"+ac_code+"','"+ac_level+"','"+ac_id+"','"+code_str+"','"+chief_id+"','y')";
			
		bean.executeUpdate(query);
		response.sendRedirect("classl.jsp");
	}

	///////////////
	// ����ó��
	///////////////
	else if (j.equals("d")) { 

		query = "select count(au_id) 'no' from user_table where ac_id ='" + ac_id +"'";
		bean.executeQuery(query);
		
		while(bean.next()){
		if(Integer.parseInt(bean.getData("no")) > 0) error = "�� �з��� ���Ե� ����� �־� ������ �� �����ϴ�.";}

	    query = " select count(ac_id) 'no' from class_table where ac_ancestor ='" + ac_id +"'";
		bean.executeQuery(query);
		while(bean.next()){
		if(Integer.parseInt(bean.getData("no")) > 0) error = "�� �з��� ���� �з��� �־� ������ �� �����ϴ�.";}

		if(error.equals("no")){
			query = " DELETE FROM class_table WHERE ac_id = '"+ac_id+"'";
			bean.executeUpdate(query);

			response.sendRedirect("classl.jsp");
		}else{
			out.println("<script>alert('"+error+"');history.go(-1);</script>");			
		}
	}

	///////////////////
	// ȸ�� ���� ���� ó��
	///////////////////
	else if(ac_level.equals("0") && j.equals("u")){
		if(isuse.equals("n")) { // ȸ������ ��뿩�� �����
			
			// ȸ��(�ֻ���) ���� ��Ȱ��ȭ ó���� ���� ������ �������� ���ԵǾ� �ִ��� Ȯ��
			query = " select count(au_id) from user_table where code like '"+ac_code+"%'";
			bean.executeQuery(query);
			bean.next();

			//���� ��Ȱ��ȭó����Ű�����ϴ� ������ �������� ���ԵǾ� ���� ��� ���� �޽��� ���
			if(Integer.parseInt(bean.getData(1)) > 0)  {
				error = "yes";
%>
				<script>alert("������ ���Ե� �������� �־� ��Ȱ��ȭ��ų �� �����ϴ�.");history.go(-2);</script>
<%
			} else {
				query = "update class_table set isuse='n' where code like '"+code+"%'";
				bean.executeUpdate(query);
				response.sendRedirect("classl.jsp");
			}
		} else { // ȸ��� �����������
			query ="update class_table set ac_name='"+ac_name+"',isuse = '" + isuse + "' WHERE ac_id='"+ac_id+"'";			
			bean.executeUpdate(query);
			response.sendRedirect("classl.jsp");
		}	
	}
%>