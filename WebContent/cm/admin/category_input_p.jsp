<%@ include file="../../admin/configPopUp.jsp"%>
<%@ include file="../../admin/chk/chkCM01.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query	= "";
	String j		= request.getParameter("j")==null?"":request.getParameter("j");
	String ancestor	= request.getParameter("ancestor") == null?"0":request.getParameter("ancestor");
	String id		= request.getParameter("id"); 
	String error	= "no";

	String code		= request.getParameter("code") == ""?"":request.getParameter("code");
	String name		= request.getParameter("name") == ""?"":Hanguel.toHanguel(request.getParameter("name"));
	String desc		= request.getParameter("desc") == ""?"":Hanguel.toHanguel(request.getParameter("desc"));
	String level	= request.getParameter("level") == ""?"":request.getParameter("level");

	bean.openConnection();	


	if(j.equals("d")) { // ���� ���
	    query = "SELECT COUNT(*) FROM item_class WHERE item_ancestor ='" + id +"'";
		bean.executeQuery(query);
		while(bean.next()){
		    if(Integer.parseInt(bean.getData(1)) > 0) error = "�����ϰ��� �з��� ���� �з��� �־� ������ �� �����ϴ�.";
		}
	}

	if(j.equals("a")) { // �� ǰ�� �Է�

		query = "SELECT COUNT(*) FROM item_class WHERE item_code='"+code+"'";
		bean.executeQuery(query);
		
		bean.next();
		int count = Integer.parseInt(bean.getData(1));

		if(count > 0){
%>
				<script>alert("�Է��Ͻ� �ڵ�� �ٸ� �ڵ�� �ߺ��˴ϴ�. �ٸ� �ڵ带 �Է��Ͻʽÿ�.");history.go(-1);</script>
<%
		}else{
			query = "INSERT INTO item_class (item_code,item_name,item_desc,item_level,item_ancestor) VALUES('"+code+"', '"+name+"', '"+desc+"','"+level+"','"+ancestor+"')";
			bean.executeUpdate(query);
		}

	} else if (j.equals("u")) { // �������
		query = "UPDATE item_class SET item_code='"+code+"',item_name='"+name+"',item_desc='"+desc+"' WHERE mid = '"+id+"'";
		bean.executeUpdate(query);

	} else if (j.equals("d")) { // �������
		if(error.equals("no")){
			query = "DELETE FROM item_class WHERE mid = '"+id+"'";
			bean.executeUpdate(query);
		}else{
			out.println("<script>alert('"+error+"');history.go(-1);</script>");			
		}
	}
%>
<script language=javascript>
	opener.location.reload();
	self.close();
</script>