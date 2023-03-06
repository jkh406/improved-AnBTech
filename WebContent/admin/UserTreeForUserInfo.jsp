<%@ include file= "configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String sDiv = request.getParameter("sdiv");
	String sWord = Hanguel.toHanguel(request.getParameter("sword"));

	int n_id = request.getParameter("n_id")==null?0:Integer.parseInt(request.getParameter("n_id"));			// 자신의 노드 아이디
	String p_id = request.getParameter("p_id")==null?"1,":request.getParameter("p_id");						// 자신의 부모 노드 아이디

	bean.openConnection();

	String query = "";
	int result_count = 0;

	
	if(sWord != null){
		if(sDiv.equals("usr")) query = "SELECT COUNT(*) FROM user_table WHERE name LIKE '%" + sWord +"%'";
		else query = "SELECT COUNT(*) FROM class_table WHERE ac_name LIKE '%" + sWord +"%'";
		
		bean.executeQuery(query);
		if(bean.next()) result_count = Integer.parseInt(bean.getData(1));

		if(result_count == 1){
			if(sDiv.equals("usr")) query = "SELECT pid FROM user_table WHERE name LIKE '%" + sWord +"%'";
			else query = "SELECT pid FROM class_table WHERE ac_name LIKE '%" + sWord +"%'";

			bean.executeQuery(query);
			bean.next();
			p_id = bean.getData("pid");
		}else if(result_count > 1){
%>
		<script language='javascript'>
			window.open("multi_result_view.jsp?target=UserTreeForUserInfo.jsp&sWord=<%=sWord%>&sDiv=<%=sDiv%>",'search','width=400,height=100,scrollbars=yes,toolbar=no,status=no,resizable=no');
		</script>
<%
		}
	}
%>

<HTML>
<HEAD>
<TITLE>부서 조직 트리</TITLE>
<link rel="stylesheet" href="css/style.css" type="text/css">
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<TABLE height="100%" cellSpacing=10 cellPadding=0 width=100% border=0>
  <TBODY>
  <TR>
    <TD valign='top'>
		<script language="JavaScript" src="user_tree_items_user_info.js"></script>
		<script language="JavaScript" src="user_tree.js"></script>
		<script language="JavaScript" src="user_tree_tpl.js"></script>
		<script language="JavaScript">
			new tree (TREE_ITEMS, tree_tpl);
<%
	StringTokenizer str = new StringTokenizer(p_id, ",");
	int spec_count = str.countTokens();
	String item[] = new String[spec_count];

	for(int i=0; i<spec_count; i++){ 
		item[i] = str.nextToken();
		out.print("trees['0'].toggle('"+item[i]+"');\n");
	}
%>
		</script>
    </TD></TR></TBODY></TABLE></BODY></HTML>