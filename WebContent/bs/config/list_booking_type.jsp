<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.bs.entity.*"
%>
<%
	BookingTypeTable table = new BookingTypeTable();
	ArrayList type_list = new ArrayList();
	type_list = (ArrayList)request.getAttribute("BOOKING_TYPE_LIST");
	Iterator type_iter = type_list.iterator();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../bs/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../bs/images/blet.gif" align="absmiddle"> �������°���</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
					<a href="SalesConfigMgrServlet?mode=write_booking_type"><img src="../bs/images/bt_reg.gif" border="0" align="absmiddle"></a>
					<a href="javascript:history.back();"><img src="../bs/images/bt_cancel.gif" border="0" align="absmiddle"></a>
			  </TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�����ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>���¸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=55 align=middle class='list_title'>���⿩��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=55 align=middle class='list_title'>��ǰ����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=55 align=middle class='list_title'>�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=55 align=middle class='list_title'>���Ͽ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�ڵ����ϻ�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=55 align=middle class='list_title'>���⿩��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=55 align=middle class='list_title'>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=55 align=middle class='list_title'>��뿩��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>����</TD>
		    </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=23></TD></TR>
<%  int no = 1;
	while(type_iter.hasNext()) {
		table = (BookingTypeTable)type_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=table.getOrderCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getOrderName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getIsExport()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getIsReturn()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getIsEntry()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getIsShipping()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getIsAutoShip()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getIsSale()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getShippingType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getIsUse()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><A HREF="javascript:modify('<%=table.getMid()%>')"><IMG src='../bs/images/lt_modify.gif' align='absmiddle' border='0' alt='����'></a>&nbsp;<A HREF="javascript:del('<%=table.getMid()%>')"><IMG src='../bs/images/lt_del.gif' align='absmiddle' border='0' alt='����'></td>
			</TR>
			<TR><TD colSpan=23 background="../bs/images/dot_line.gif"></TD></TR>
<%		no++;
	}				
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>



<script language='javascript'>

function modify(mid){
	location.href="../servlet/SalesConfigMgrServlet?mode=modify_booking_type&mid="+mid;
}

function del(mid){
	if(confirm("���� ���� �Ͻðڽ��ϱ�?")) {
	location.href="../servlet/SalesConfigMgrServlet?mode=delete_booking_type&mid="+mid;
	}
}
</script>