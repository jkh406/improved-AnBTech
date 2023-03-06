<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page
	language	= "java" 
	import		= "java.sql.*,com.anbtech.text.Hanguel"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../errorpage.jsp"
%>
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree"/>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	bean.openConnection();
	String sql = "SELECT * FROM class_table WHERE ac_level = '0'";
	bean.executeQuery(sql);
	bean.next();

	String comp_name = bean.getData("ac_name");
	String company_ac_id = bean.getData("ac_id");

	int ac_id = request.getParameter("ac_id") == null?Integer.parseInt(company_ac_id):Integer.parseInt(request.getParameter("ac_id"));

	String whereStr = "";
	String s = request.getParameter("s") == null?"":Hanguel.toHanguel(request.getParameter("s"));
	
%>
<html>
<head><title></title></head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<BODY leftmargin=0 topmargin=0 oncontextmenu='return false'>
<form name='frml' action="userl.jsp" method="post">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 사용자관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD align=left width='500' style='padding-left:5px'><input type="text" name="s" size="10"> <IMG src='../images/bt_search3.gif' onclick="javascript:document.frml.submit()" align='absmiddle' style='cursor:hand' > <a href="useri.jsp?j=a&ac_id=<%=ac_id%>"><IMG src='../images/bt_add.gif' border='0' align='absmiddle'></a></TD></TR></TBODY></TABLE></TD></TR>
   <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>


  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	  <TBODY>
	  <tr>
		<td height=25 width="100%" align="left" colspan=13 style="padding-left=5px">
<%	if(s != ""){ // 이름으로 검색했을 경우 %>
			<a href="userl.jsp"><%=comp_name%></a> > 사원 검색</td></tr>
<%
			String total_sql = "select a.*,b.*,c.ar_name from user_table a,class_table b,rank_table c where a.ac_id = b.ac_id and a.name like '%"+s+"%' and a.rank = c.ar_code";

			bean.executeQuery(total_sql);

		}else{ // 검색하지 않은 경우 
%>
			<%=recursion.viewHistory(ac_id,whereStr)%></td></tr>
	  <tr>
		<td height=18 width="100%" align="left" colspan=13 style="padding-left=5px">
<%

		/* 하위 분류를 표시 */
		sql = " select ac_id,ac_name from class_table where ac_ancestor = '"+ac_id+"' and isuse = 'y' order by ac_name ASC,ac_id";
		bean.executeQuery(sql);

		while(bean.next()){
%>
			<a href="userl.jsp?ac_id=<%=bean.getData("ac_id")%>"><%=bean.getData("ac_name")%></a>&nbsp;&nbsp;/
<%		
		}

		/* 해당 분류에 속한 사원 목록 출력 */
		sql = "select ac_id from class_table where ac_ancestor = '"+ac_id+"' order by ac_id desc";
		String classList = "";
		bean.executeQuery(sql);
		while(bean.next()){
			classList += bean.getData("ac_id") + ",";
		}
		classList += ac_id;

		String total_sql = "select a.ac_id,a.au_id,c.ar_name,a.name,a.id,a.office_tel,a.email,b.ac_name from user_table a,class_table b,rank_table c where a.ac_id in ("+classList+") and a.ac_id = b.ac_id and a.rank = c.ar_code and b.isuse='y' order by b.ac_id asc, c.ar_priorty asc";
		bean.executeQuery(total_sql);
	} // if
%>	
	  </td></tr>

	  <TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
	  <tr vAlign=middle height=23>
		<td noWrap width=120 align=middle class='list_title'>소속</td>
	    <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<td noWrap width=100 align=middle class='list_title'>직위</td>
		<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<td noWrap width=80 align=middle class='list_title'>성명</td>
	    <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<td noWrap width=70 align=middle class='list_title'>사번</td>
	    <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<td noWrap width=140 align=middle class='list_title'>전화번호</td>
		 <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<td noWrap width=100% align=middle class='list_title'>전자우편</td>
		 <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<td noWrap width=80 align=middle class='list_title'>상세보기</td>
	  </tr>
	  <TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>

<%		while(bean.next()){	
			String mod_url  = "<a href = 'useri.jsp?j=u&au_id="+bean.getData("au_id")+"&ac_id="+bean.getData("ac_id")+"'>수정</a>";
			String del_url  = "<a href = 'userp.jsp?j=d&au_id="+bean.getData("au_id")+"&ac_id="+bean.getData("ac_id")+"'>삭제</a>";
			String view_url = "<a href = 'view.jsp?ac_id="+bean.getData("ac_id")+"&au_id="+bean.getData("au_id")+"'><img src='../images/lt_view_d.gif' border='0'></a>";
%>
	  <tr onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor="#ffffff">
			<td height='24' class='list_bg' align=center><%=bean.getData("ac_name")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("ar_name")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("name")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("id")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("office_tel")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("email")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=view_url%></td>
	  </tr>
	  <TR><TD colSpan=13 background="../images/dot_line.gif"></TD></tr>
<%	}	
%>
	</tbody>
	</table>
	</form>
</body>
</html>

<%
	//*.js 파일 생성하기
	com.anbtech.admin.business.makeUserTreeItems app	= new com.anbtech.admin.business.makeUserTreeItems();
	com.anbtech.admin.business.makeUserTreeItems app2	= new com.anbtech.admin.business.makeUserTreeItems();

	//사우 정보 보기용 js 파일 생성
	app.makeUserInfoTree(root_path + "/admin/user_tree_items_user_info.js","UserInfoView.jsp",0,0);
	//사용자 선택 페이지용 js 파일 생성
	app2.makeUserInfoTree(root_path + "/admin/user_tree_items_select.js","NA",0,0);
%>