<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
	errorPage	= "../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String searchscope = request.getParameter("searchscope");
	String searchword = Hanguel.toHanguel(request.getParameter("searchword"));
	String p_id = request.getParameter("p_id")==null?"1,":request.getParameter("p_id");						// 자신의 부모 노드 아이디

	bean.openConnection();
	String query = "";
	int result_count = 0;

	
	if(searchword != null){
		query = "SELECT COUNT(*) FROM goods_structure WHERE " + searchscope + " LIKE '%" + searchword +"%'";
		
		bean.executeQuery(query);
		if(bean.next()) result_count = Integer.parseInt(bean.getData(1));

		if(result_count == 1){
			query = "SELECT pid FROM goods_structure WHERE " + searchscope + " LIKE '%" + searchword +"%'";

			bean.executeQuery(query);
			bean.next();
			p_id = bean.getData("pid");
		}else if(result_count > 1){
%>
		<script language='javascript'>
			window.open("multi_result_view.jsp?target=GoodsTree.jsp&searchword=<%=searchword%>&searchscope=<%=searchscope%>",'search','width=600,height=200,scrollbars=yes,toolbar=no,status=no,resizable=no');
		</script>
<%
		}
	}
%>

<HTML>
<HEAD>
<TITLE>제품 정보 트리 출력</TITLE>
<link rel="stylesheet" href="css/style.css" type="text/css">
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width=100% border=0>
  <TBODY>
  <TR>
    <TD style="PADDING-LEFT: 15px; PADDING-TOP: 10px" vAlign=top>

		<script language="JavaScript" src="admin/goods_tree_items.js"></script>
		<script language="JavaScript" src="admin/tree.js"></script>
		<script language="JavaScript" src="admin/tree_tpl.js"></script>
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
    </TD></TR></TBODY></TABLE></FORM></BODY></HTML>

