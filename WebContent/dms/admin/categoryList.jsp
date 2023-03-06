<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkDM01.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="tree" class="com.anbtech.dms.admin.makeDocCategory"/>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	/*********************************************************************************
	 * 최상위 분류(레벨0)를 자동으로 입력한다. (예:SLINK 문서)
	 * 최상위 분류는 카테고리 분류에 나타나지 않도록 하기 위해 레벨 1 부터 출력한다.
	 * 아래 tree.viewCategoryTree(1,1) 부분 참조
	 *********************************************************************************/
	bean.openConnection();

	String query = "SELECT COUNT(*) FROM category_data WHERE c_level = '0'";
	bean.executeQuery(query);
	bean.next();

	if(bean.getData(1).equals("0")){	//레벨0 분류가 등록되어 있지 않은 경우
		query =	"INSERT INTO category_data ";
	    query = query + "(c_id,c_level,c_ancestor,c_name,c_code,tablename,enable_rev,";
	    query = query + "enable_pjt,enable_eco,enable_app,security_level,save_period,p_id,loan_period)";
		query = query + "VALUES('1','0','0','전체문서','T','master_data','y','y','y','y','1','0','','10')";
		bean.executeUpdate(query);
	}
	/****************************************************************
	 * 이페이지를 엑세스할 때마다 카테고리 분류 파일을 업데이트한다.
	 * anb/dms/admin/tree_items.js 파일	(트리 메뉴에 사용)
	 * anb/dms/admin/tree_items2.js 파일(카테고리 변경할 때 사용)
	 ****************************************************************/

	com.anbtech.dms.admin.makeCategoryTreeItems app = new com.anbtech.dms.admin.makeCategoryTreeItems();
	com.anbtech.dms.admin.makeCategoryTreeItems app2 = new com.anbtech.dms.admin.makeCategoryTreeItems();
	app.makeCategoryTree(0,0,"../servlet/AnBDMS","c:/tomcat4/webapps/webffice/dms/admin/tree_items.js");
	app2.makeCategoryTree(0,0,"modify_category.jsp","c:/tomcat4/webapps/webffice/dms/admin/tree_items2.js");

%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 문서분류관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'><a href='categoryInput.jsp?j=a'><img src='../images/bt_add_b.gif' border='0' align='absmiddle'></a></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=100% align=middle class='list_title'>문서분류명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>대표<br>문자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>리비젼</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>프로<br>젝트</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>형상<br>(모델)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>ECO</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>전자<br>결재</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>보안<br>등급</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>보존<br>기간</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>대출<br>일수</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>비고</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
			  <%=tree.viewCategoryTree(1,1)%>

		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>



