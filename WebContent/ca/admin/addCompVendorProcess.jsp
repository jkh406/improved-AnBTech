<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%!
	com.anbtech.ViewQueryBean bean = new com.anbtech.ViewQueryBean();

	// 새 업체 등록
	public void addVendor(String code,String name,String name2) throws Exception{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String w_date 	= vans.format(now);

		bean.openConnection();

		String query = "INSERT INTO maker_code_table (code,name,name2,stat,w_date) VALUES(";
		query += "'"+code+"',";
		query += "'"+name+"',";
		query += "'"+name2+"',";
		query += "'1',";
		query += "'"+w_date+"')";
			
		bean.executeUpdate(query);
	}

	// 업체정보 수정
	public void updVendor(String code,String name,String name2,String stat) throws Exception{
		bean.openConnection();

		String query = "UPDATE maker_code_table SET ";
		query += "name = '"+name+"',";
		query += "name2 = '"+name2+"',";
		query += "stat = '"+stat+"' ";
		query += "WHERE code = '"+code+"'";

		bean.executeUpdate(query);
	}

	// 업체정보 삭제
	public void delVendor(String code) throws Exception{
		bean.openConnection();

		String query = "DELETE FROM maker_code_table WHERE code = '"+code+"'";
		bean.executeUpdate(query);
	}
%>
<%
	String mode		= request.getParameter("mode");
	String code		= request.getParameter("code");
	String name		= Hanguel.toHanguel(request.getParameter("name"));
	String name2	= Hanguel.toHanguel(request.getParameter("name2"));
	String stat		= request.getParameter("stat");
	if(mode == null) mode = "list";

	if ("add".equals(mode)){
		addVendor(code,name,name2);
	}else  if ("update".equals(mode)){
		updVendor(code,name,name2,stat);
	}else  if ("delete".equals(mode)){
		delVendor(code);
	}

%>

<script language=javascript>
	alert("정상적으로 처리되었습니다.");
	opener.location.reload();
	self.close();
</script>