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
	 * �ֻ��� �з�(����0)�� �ڵ����� �Է��Ѵ�. (��:SLINK ����)
	 * �ֻ��� �з��� ī�װ� �з��� ��Ÿ���� �ʵ��� �ϱ� ���� ���� 1 ���� ����Ѵ�.
	 * �Ʒ� tree.viewCategoryTree(1,1) �κ� ����
	 *********************************************************************************/
	bean.openConnection();

	String query = "SELECT COUNT(*) FROM category_data WHERE c_level = '0'";
	bean.executeQuery(query);
	bean.next();

	if(bean.getData(1).equals("0")){	//����0 �з��� ��ϵǾ� ���� ���� ���
		query =	"INSERT INTO category_data ";
	    query = query + "(c_id,c_level,c_ancestor,c_name,c_code,tablename,enable_rev,";
	    query = query + "enable_pjt,enable_eco,enable_app,security_level,save_period,p_id,loan_period)";
		query = query + "VALUES('1','0','0','��ü����','T','master_data','y','y','y','y','1','0','','10')";
		bean.executeUpdate(query);
	}
	/****************************************************************
	 * ���������� �������� ������ ī�װ� �з� ������ ������Ʈ�Ѵ�.
	 * anb/dms/admin/tree_items.js ����	(Ʈ�� �޴��� ���)
	 * anb/dms/admin/tree_items2.js ����(ī�װ� ������ �� ���)
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
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> �����з�����</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'><a href='categoryInput.jsp?j=a'><img src='../images/bt_add_b.gif' border='0' align='absmiddle'></a></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=100% align=middle class='list_title'>�����з���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>��ǥ<br>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����<br>��Ʈ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����<br>(��)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>ECO</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����<br>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����<br>���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����<br>�Ⱓ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����<br>�ϼ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>���</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
			  <%=tree.viewCategoryTree(1,1)%>

		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>



