<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%
	OrderTypeTable orderTable = new OrderTypeTable();
	ArrayList type_list = new ArrayList();
	type_list = (ArrayList)request.getAttribute("ORDER_TYPE_INFO");
	Iterator type_iter = type_list.iterator();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../pu/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif" align="absmiddle"> �������°���</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'><a href="PurchaseConfigMgrServlet?mode=write_order_type"><img src="../pu/images/bt_reg.gif" border="0" align="absmiddle"></a></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=80 align=middle class='list_title'>�����ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>���¸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>���Կ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�԰���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>���Կ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>��ǰ����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>��޿���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>��뿩��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>����</TD>
		    </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
<%
	while(type_iter.hasNext()) {
		orderTable = (OrderTypeTable)type_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=orderTable.getOrderType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=orderTable.getOrderName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=orderTable.getIsImport()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=orderTable.getIsShipping()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=orderTable.getIsPass()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=orderTable.getIsEnter()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=orderTable.getIsPurchase()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=orderTable.getIsReturn()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=orderTable.getIsSageup()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=orderTable.getIsUsing()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><A HREF="javascript:modify('<%=orderTable.getMid()%>')"><IMG src='../pu/images/lt_modify.gif' align='absmiddle' border='0' alt='����'></a>&nbsp;<A HREF="javascript:del('<%=orderTable.getMid()%>')"><IMG src='../pu/images/lt_del.gif' align='absmiddle' border='0' alt='����'></td>
			</TR>
			<TR><TD colSpan=21 background="../pu/images/dot_line.gif"></TD></TR>
<%
	}				
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>



<script language='javascript'>

function modify(mid){
	location.href="../servlet/PurchaseConfigMgrServlet?mode=modify_order_type&mid="+mid;
}

function del(mid){
	if(confirm("���� ���� �Ͻðڽ��ϱ�?")) {
	location.href="../servlet/PurchaseConfigMgrServlet?mode=delete_order_type&mid="+mid;
	}
}
</script>