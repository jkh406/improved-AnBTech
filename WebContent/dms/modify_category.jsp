<%@ include file= "../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.dms.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<jsp:useBean id="tree" class="com.anbtech.dms.admin.makeCategoryTreeItems"/>
<jsp:useBean id="cat" class="com.anbtech.dms.admin.makeDocCategory"/>

<% 
	String category = request.getParameter("category")==null?"1":request.getParameter("category");

	// 카테고리 분류 가져오기
	String where_category = cat.viewCategory(Integer.parseInt(category),"");
%>
<HTML><HEAD><TITLE>문서분류선택</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <TR><TD align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/pop_cat_ch.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<TABLE cellspacing=0 cellpadding=0 width="98%" border=0>
	   <TBODY>
         <TR>
		   <TD height=40 style='padding-left:5px'><font color='#565656'><%=where_category%></font></TD></TR>
         <TR bgcolor="c7c7c7"><TD height=1></TD></TR>
         <TR>
           <TD width="100%" style="padding-left:4px" background="images/bg-011.gif"><!-- 삽입시작 -->
			<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%">
			  <tbody>
			  <tr>
				<td width='100% valign='top' style='padding-top:10px;padding-left:10px;padding-bottom:10px'>
					<DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:156px; width:100%;overflow:auto;">


						<%
							// 자신의 노드 아이디
							int n_id = request.getParameter("n_id")==null?0:Integer.parseInt(request.getParameter("n_id"));
							// 부모 노드 아이디
							String p_id = request.getParameter("p_id")==null?"1,":request.getParameter("p_id");	

							// 부모 노드 아이디를 구분자(,)로 잘라서 배열에 저장
							StringTokenizer str = new StringTokenizer(p_id, ",");
							int spec_count = str.countTokens();
							String item[] = new String[spec_count];

						%>

						<script language="JavaScript" src="admin/tree_items2.js"></script>
						<script language="JavaScript" src="admin/tree2.js"></script>
						<script language="JavaScript" src="admin/tree_tpl.js"></script>
						<script language="JavaScript">
							new tree (TREE_ITEMS, tree_tpl);
						
						<%
							for(int i=0; i<spec_count; i++){ 
								item[i] = str.nextToken();
								out.print("trees['0'].toggle('"+item[i]+"');\n");
							}
						%>

						</script></DIV>

				</td>
			  </tr></tbody></table><!-- 삽입끝 -->   
		   </TD></TR>
         <TR bgcolor="C7C7C7"><TD height="1"></TD></TR>
         <TR><TD height=20></TD></TR></TBODY></TABLE>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:go();"><img src="images/bt_sel_end.gif" border="0" align="absmiddle"></a> <a href="javascript:self.close();"><img src="images/bt_close.gif" border="0" align="absmiddle"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></TD></TR></table></BODY></HTML>

<script>
	function go() {
		window.returnValue = <%=category%>;
		self.close();
	}
	function cancel(){
		self.close();
	}
</script>