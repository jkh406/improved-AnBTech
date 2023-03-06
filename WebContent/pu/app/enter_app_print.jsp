<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*,com.anbtech.admin.entity.ApprovalInfoTable"
%>
<%!
	EnterInfoTable table;
	PurchaseLinkUrl redirect;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
	ApprovalInfoTable app_table;
%>

<%
	app_table = new ApprovalInfoTable();
	app_table = (ApprovalInfoTable)request.getAttribute("Approval_Info");

	String writer_sign		= app_table.getWriterSig()==null?"":app_table.getWriterSig();
	String writer_name		= app_table.getWriterName()==null?"":app_table.getWriterName();
	String reviewer_sign	= app_table.getReviewerSig()==null?"":app_table.getReviewerSig();
	String reviewer_name	= app_table.getReviewerName()==null?"":app_table.getReviewerName();
	String decision_sign	= app_table.getDecisionSig()==null?"":app_table.getDecisionSig();
	String decision_name	= app_table.getDecisionName()==null?"":app_table.getDecisionName();
	String memo				= app_table.getMemo()==null?"":app_table.getMemo();

	String mode = request.getParameter("mode");	// 모드

	table = (EnterInfoTable)request.getAttribute("ENTER_INFO");
	String enter_no				= table.getEnterNo();
	String supplyer_code		= table.getSupplyerCode();
	String supplyer_name		= table.getSupplyerName();
	String enter_date			= table.getEnterDate();
	String enter_total_mount	= sp.getMoneyFormat(table.getEnterTotalMount(),"");
	String requestor_div_code	= table.getRequestorDivCode();
	String requestor_div_name	= table.getRequestorDivName();
	String requestor_id			= table.getRequestorId();
	String requester_info		= table.getRequestorInfo();
	String enter_type			= table.getEnterType();
	String monetary_unit		= table.getMonetaryUnit();
	String filelink				= table.getFileLink();

	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new EnterInfoTable();
	Iterator table_iter = item_list.iterator();
%>
<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title>구매입고표</title>
</head>

<BODY topmargin="5" leftmargin="5">

<form name="reg_order" method="post" action="PurchaseMgrServlet" enctype="multipart/form-data">
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../pu/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">구매입고표</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
		<a href='Javascript:winprint();'><img src="../pu/images/bt_print.gif" border="0"></a>
		<a href='Javascript:self.close();'><img src="../pu/images/bt_close.gif" border="0"></a></div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>
<!-- 결재정보 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">메<p>모</TD>
		<TD noWrap width=370 align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=53 readOnly style="border:0"><%=memo%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">결<p>재</TD>
		<TD noWrap width=50 align=middle class="bg_07">기안자</TD>
		<TD noWrap width=50 align=middle class="bg_07">검토자</TD>
		<TD noWrap width=50 align=middle class="bg_07">승인자</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><%=writer_sign%><img width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=reviewer_sign%><img width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=decision_sign%><img width='0' height='0'></TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=writer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=reviewer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=decision_name%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE><BR>

<TABLE cellSpacing=0 cellPadding=0 width="620" border=1  bordercolordark="white" bordercolorlight="#9CA9BA">
	<TBODY>
	   <tr>
           <td width="15%" height="25" class="bg_05" align="middle">입고번호</td>
           <td width="35%" height="25" class="bg_06"><%=enter_no%></td>
           <td width="15%" height="25" class="bg_05" align="middle">입고일자</td>
           <td width="35%" height="25" class="bg_06"><%=enter_date%></td></tr>
		<tr>
           <td width="15%" height="25" class="bg_05" align="middle">입고담당자</td>
           <td width="35%" height="25" class="bg_06"><%=requester_info%></td>
           <td width="15%" height="25" class="bg_05" align="middle">입고담당부서</td>
           <td width="35%" height="25" class="bg_06"><%=requestor_div_name%></td></tr>
		<tr>
           <td width="15%" height="25" class="bg_05" align="middle">매입총액</td>
           <td width="35%" height="25" class="bg_06"><%=enter_total_mount%></td>
           <td width="15%" height="25" class="bg_05" align="middle">공급업체</td>
           <td width="35%" height="25" class="bg_06">[<%=supplyer_code%>] <%=supplyer_name%></td></tr></TBODY></table><br>

<!-- 입고품목 -->
	  <TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<TBODY>
				<TR vAlign=middle height=23>
				  <TD noWrap width=90 align=middle class='bg_05'>품목코드</TD>
				  <TD noWrap width=100% align=middle class='bg_05'>품목규격</TD>
				  <TD noWrap width=50 align=middle class='bg_05'>입고<br>수량</TD>
				  <TD noWrap width=50 align=middle class='bg_05'>입고<br>단위</TD>
				  <TD noWrap width=80 align=middle class='bg_05'>입고단가</TD>
				  <TD noWrap width=80 align=middle class='bg_05'>매입금액</TD>
				</TR>
				
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (EnterInfoTable)table_iter.next();
%>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					  <TD align=middle height="24" class='list_bg'><%=table.getItemCode()%></TD>
					  <TD align=left class='list_bg' style='padding-left:2px'><%=table.getItemDesc()%></TD>
					  <TD align=middle class='list_bg'><%=table.getEnterQuantity()%></TD>
					  <TD align=middle class='list_bg'><%=table.getEnterUnit()%></TD>
					  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getUnitCost(),"")%></TD>
					  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getEnterCost(),"")%></TD>
				</TR>
<%
		no++;	
	}
%>
		</TBODY></TABLE>
</form>
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