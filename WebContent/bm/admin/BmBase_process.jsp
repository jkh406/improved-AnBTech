<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = "MBOM ȯ����� : �߰�,����,������ ó��"
	language = "java"
	contentType = "text/html;charset=KSC5601"
	errorPage = "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.date.anbDate"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query	= "";
	String j		= request.getParameter("j")==null?"":request.getParameter("j");
	String pid		= request.getParameter("pid") ==""?"":request.getParameter("pid"); 
	String flag		= request.getParameter("flag") == ""?"":request.getParameter("flag");
	String m_code	= request.getParameter("m_code") == ""?"":request.getParameter("m_code");
	String spec		= request.getParameter("spec") == ""?"":Hanguel.toHanguel(request.getParameter("spec"));
	String tag		= request.getParameter("tag") == ""?"":request.getParameter("tag");

	bean.openConnection();	

	if(j.equals("a")) { // �� �����ڵ� ���� ���
		// �ߺ� üũ
		anbDate anbdate = new com.anbtech.date.anbDate();
		pid = anbdate.getID();
		boolean bool = true;

		query = "SELECT m_code,tag FROM mbom_env WHERE flag='"+flag+"'";
		bean.executeQuery(query);
		while(bean.next()){
			String temp1 = bean.getData("m_code");
			String temp2 = bean.getData("tag");
			if(temp1.equals(m_code)){
				bool = false;
				out.println("<script>alert('"+m_code+"�� �ߺ��� ���Դϴ�.');history.go(-1);</script>");
			}
			if(temp2.equals(tag)){
				bool = false;
				out.println("<script>alert('"+tag+"�� �ߺ��� ���Դϴ�.');history.go(-1);</script>");
			}
		}
		
	    if (bool) {
		query = "INSERT INTO mbom_env VALUES('"+pid+"','"+flag+"','"+m_code+"','"+spec+"','"+tag+"')";
		bean.executeUpdate(query);
		}		

	} else if (j.equals("u")) { // �������
        query = "UPDATE mbom_env SET m_code='"+m_code+"',spec='"+spec+"',tag='"+tag+"' WHERE pid = '"+pid+"'";
		bean.executeUpdate(query);
		
		
	} else if (j.equals("d")) { // �������
		query = "DELETE FROM mbom_env WHERE pid = '"+pid+"'";
		bean.executeUpdate(query);

		response.sendRedirect("BmBase_list.jsp");
	}
%>
<script language=javascript>

	opener.location.reload();
	this.close();
</script>