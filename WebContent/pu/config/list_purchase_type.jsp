<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%
	PurchaseTypeTable purchaseTable = new PurchaseTypeTable();
	ArrayList type_list = new ArrayList();
	type_list = (ArrayList)request.getAttribute("PURCHASE_TYPE_INFO");
	Iterator type_iter = type_list.iterator();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../pu/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pu/images/blet.gif" align="absmiddle"> 매입형태관리</TD>
					<TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
	<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD width=4>&nbsp;</TD>
				<TD align=left width='500'><a href="PurchaseConfigMgrServlet?mode=write_purchase_type"><img src="../pu/images/bt_reg.gif" border="0" align="absmiddle"></a></TD>
				<TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
	<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
				<TD noWrap width=100 align=middle class='list_title'>매입형태코드</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
				<TD noWrap width=150 align=middle class='list_title'>매입형태명</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>수입여부</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>예외여부</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>반품여부</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>사용여부</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
				<TD noWrap width=60 align=middle class='list_title'>관리</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
		   <TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
<%
	while(type_iter.hasNext()) {
		purchaseTable = (PurchaseTypeTable)type_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=purchaseTable.getPurchaseType()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=purchaseTable.getPurchaseName()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=purchaseTable.getIsImport()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=purchaseTable.getIsExcept()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=purchaseTable.getIsReturn()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=purchaseTable.getIsUsing()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=left class='list_bg'><A HREF="javascript:modify('<%=purchaseTable.getMid()%>')"><IMG src='../pu/images/lt_modify.gif' align='absmiddle' border='0' alt='수정'></a>&nbsp;<A HREF="javascript:del('<%=purchaseTable.getMid()%>')"><IMG src='../pu/images/lt_del.gif' align='absmiddle' border='0' alt='삭제'></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'></td>
			</TR>
			<TR><TD colSpan=15 background="../pu/images/dot_line.gif"></TD></TR>
<%	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>

<script language='javascript'>

function add(){
	location.href="../servlet/PurchaseConfigMgrServlet?mode=write_purchase_type";
}

function modify(mid){
	location.href="../servlet/PurchaseConfigMgrServlet?mode=modify_purchase_type&mid="+mid;
}

function del(mid){
	if(confirm("정말 삭제 하시겠습니까?")) {
	location.href="../servlet/PurchaseConfigMgrServlet?mode=delete_purchase_type&mid="+mid;
	}
}

</script>