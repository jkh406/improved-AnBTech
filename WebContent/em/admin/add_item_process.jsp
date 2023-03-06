<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String mode			= request.getParameter("mode");
	String item_class	= request.getParameter("item_class");
	String mid			= request.getParameter("mid");
	String item_name	= Hanguel.toHanguel(request.getParameter("item_name"));

	String query		= "";
	bean.openConnection();

	if(mode.equals("modify") && mid != null){
		query = "UPDATE em_input_item_table SET item_name = '" + item_name + "' WHERE mid = '" + mid + "'";
		bean.executeUpdate(query);
	}
	else if(mode.equals("delete") && mid != null){
		query = "DELETE em_input_item_table WHERE mid = '" + mid + "'";
		bean.executeUpdate(query);
	}
	else if(mode.equals("add") && item_name != null && item_class != null){
		query = "INSERT INTO em_input_item_table (item_class,class_name,item_code,item_name,essential) SELECT DISTINCT(item_class),class_name,item_code,'" + item_name + "','0' FROM em_input_item_table WHERE item_class = '" + item_class + "'";
		bean.executeUpdate(query);
	}
%>
<script language='javascript'>
	opener.location.href = "input_item_mgr.jsp?c_class=<%=item_class%>";
	self.close();
</script>