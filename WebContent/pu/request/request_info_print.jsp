<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*,com.anbtech.admin.entity.ApprovalInfoTable"
%>
<%!
	RequestInfoTable table;
	PurchaseLinkUrl redirect;
    ApprovalInfoTable app_table;
%>
<%
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();


	String mode = request.getParameter("mode");	// 모드

	table = (RequestInfoTable)request.getAttribute("REQUEST_INFO");
	String request_no			= table.getRequestNo();
	String request_date			= table.getRequestDate();
	String requester_div_code	= table.getRequesterDivCode();
	String requester_div_name	= table.getRequesterDivName();
	String requester_id			= table.getRequesterId();
	String requester_info		= table.getRequesterInfo();
	String request_total_mount  = sp.getMoneyFormat(table.getRequestTotalMount(),"");
	String request_type			= table.getRequestType();
	String project_code			= table.getProjectCode();	// 과제코드(임의추가)
	String project_name			= table.getProjectName();	// 과제명(임의추가)
	
	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new RequestInfoTable();
	Iterator table_iter = item_list.iterator();	
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../pu/css/style.css">
<HEAD>
<TITLE>인쇄화면</TITLE>
</HEAD>

<BODY topmargin="5" leftmargin="5">
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../pu/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">품목구매의뢰</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
		<a href='Javascript:winprint();'><img src="../pu/images/bt_print.gif" border="0"></a>
		<a href='Javascript:self.close();'><img src="../pu/images/bt_close.gif" border="0"></a></div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">구매요청</td>
				<td width="35%" class="bg_06"><%=request_no%></td>
				<td width="15%" height="25" align="middle" class="bg_05">구매요청일자</td>
				<td width="35%" class="bg_06"><%=request_date%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">구매요청자</td>
				<td width="35%" class="bg_06"><%=requester_info%></td>
				<td width="15%" height="25" align="middle" class="bg_05">구매요청부서</td>
				<td width="35%" class="bg_06"><%=requester_div_name%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">과제코드</td>
				<td width="35%" class="bg_06"><img src='' width='0' height='0'><%=project_code%></td>
				<td width="15%" height="25" align="middle" class="bg_05">과제명</td>
				<td width="35%" class="bg_06"><img src='' width='0' height='0'><%=project_name%></td></tr><table><br>

	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<TBODY>
				<TR vAlign=middle height=23>
				  <TD noWrap width=100 align=middle class='bg_05'>품목코드</TD>
				  <TD noWrap width=100% align=middle class='bg_05'>품목설명</TD>
				  <TD noWrap width=50 align=middle class='bg_05'>수량</TD>
				  <TD noWrap width=50 align=middle class='bg_05'>단위</TD>
				  <TD noWrap width=50 align=middle class='bg_05'>발주<br>수량</TD>
				  <TD noWrap width=50 align=middle class='bg_05'>입고<br>수량</TD>
				  <TD noWrap width=90 align=middle class='bg_05'>희망입고일</TD>
				</TR>
				
	<%
		int no = 1;
		while(table_iter.hasNext()){
			table = (RequestInfoTable)table_iter.next();
	%>
				<TR vAlign=middle height=23>
					  <TD align=middle class='bg_06' height="25"><%=table.getItemCode()%></TD>
					  <TD align=left class='bg_06' style='padding-left:5px'>&nbsp;<%=table.getItemDesc()%></TD>
					  <TD align=middle class='bg_06'>&nbsp;<%=table.getRequestQuantity()%></TD>
					  <TD align=middle class='bg_06'>&nbsp;<%=table.getRequestUnit()%></TD>
					  <TD align=middle class='bg_06'>&nbsp;<%=table.getOrderQuantity()%></TD>
					  <TD align=middle class='bg_06'>&nbsp;<%=table.getDeliveryQuantity()%></TD>
					  <TD align=middle class='bg_06'>&nbsp;<%=table.getDeliveryDate()%><img width='0' height='0'></TD>
				</TR>
	<%
			no++;	
		}
	%>
			</TBODY></TABLE>
	</td></tr></table>
</body>
</html>

<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
</script>