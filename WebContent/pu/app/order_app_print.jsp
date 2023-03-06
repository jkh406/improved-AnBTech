<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*,com.anbtech.admin.entity.ApprovalInfoTable"
%>
<%!
	OrderInfoTable table;
	PurchaseLinkUrl redirect;
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

	table = (OrderInfoTable)request.getAttribute("ORDER_INFO");
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();

	String order_no				= table.getOrderNo();
	String order_type			= table.getOrderType();
	String process_stat			= table.getProcessStat();
	String order_date			= table.getOrderDate();
	String requestor_div_code	= table.getRequestorDivCode();
	String requestor_div_name	= table.getRequestorDivName();
	String requestor_id			= table.getRequestorId();
	String requestor_info		= table.getRequestorInfo();
	String supplyer_code		= table.getSupplyerCode();
	String supplyer_name		= table.getSupplyerName();
	String order_total_mount	= sp.getMoneyFormat(table.getOrderTotalMount(),"");
	String monetary_unit		= table.getMonetaryUnit();
	String exchange_rate		= sp.getMoneyFormat(table.getExchangeRate(),"");
	String vat_type				= table.getVatType();
	String vat_rate				= table.getVatRate();
	String vat_mount			= sp.getMoneyFormat(table.getVatMount(),"");
	String is_vat_contained		= table.getIsVatContained();
	String approval_type		= table.getApprovalType();
	String approval_period		= table.getApprovalPeriod();//
	String payment_type			= table.getPaymentType();
	String supplyer_info		= table.getSupplyerInfo();
	String supplyer_tel			= table.getSupplyerTel();
	String other_info			= table.getOtherInfo();//
	String inout_type			= table.getInOutType();//

	String item_code			= table.getItemCode();
	String item_name			= table.getItemName();
	String item_desc			= table.getItemDesc();
	String delivery_date		= table.getDeliveryDate();
	String order_quantity		= table.getOrderQuantity();
	String order_unit			= table.getOrderUnit();
	String unit_cost			= sp.getMoneyFormat(table.getUnitCost(),"");
	String order_cost			= sp.getMoneyFormat(table.getOrderCost(),"");
	String request_no			= table.getRequestNo();

	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new OrderInfoTable();
	Iterator table_iter = item_list.iterator();

	
%>
<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title>구매발주의뢰</title>
</head>

<BODY topmargin="5" leftmargin="5">

<form name="reg_order" method="post" action="PurchaseMgrServlet" enctype="multipart/form-data">
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../pu/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">구매발주서</TD>
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
		<TD noWrap width=370 align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=55 readOnly style="border:0"><%=memo%></TEXTAREA></TD>
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
           <td width="13%" height="25" class="bg_05" align="middle">발주번호</td>
           <td width="37%" height="25" class="bg_06"><%=order_no%></td>
           <td width="13%" height="25" class="bg_05" align="middle">등록일자</td>
           <td width="37%" height="25" class="bg_06"><%=order_date%></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_05" align="middle">공급업체명</td>
           <td width="37%" height="25" class="bg_06"><%=supplyer_name%></td>
           <td width="13%" height="25" class="bg_05" align="middle">내외자구분</td>
           <td width="37%" height="25" class="bg_06"><% if(inout_type.equals("D")) {%>내자 <% } else if(inout_type.equals("O")){%>외자<%}%></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_05" align="middle">구매담당자</td>
           <td width="37%" height="25" class="bg_06"><%=requestor_info%></td>
           <td width="13%" height="25" class="bg_05" align="middle">구매담당부서</td>
           <td width="37%" height="25" class="bg_06"><%=requestor_div_name%></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_05" align="middle">화폐단위</td>
           <td width="37%" height="25" class="bg_06"><%=monetary_unit%></td>
           <td width="13%" height="25" class="bg_05" align="middle">환율</td>
           <td width="37%" height="25" class="bg_06"><%=exchange_rate%></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_05" align="middle">VAT율</td>
           <td width="37%" height="25" class="bg_06"><%=vat_rate%></td>
           <td width="13%" height="25" class="bg_05" align="middle">VAT금액</td>
           <td width="37%" height="25" class="bg_06"><%=vat_mount%></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_05" align="middle">VAT포함여부</td>
           <td width="37%" height="25" class="bg_06">
		   <% if(is_vat_contained.equals("0")) {%>별도 <%
		   } else if(is_vat_contained.equals("1")) { %>포함<%}%>
		   </td>
           <td width="13%" height="25" class="bg_05" align="middle">총발주금액</td>
           <td width="37%" height="25" class="bg_06"><%=order_total_mount%></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_05" align="middle">결재방법</td>
           <td width="37%" height="25" class="bg_06"><%=approval_type%></td>
           <td width="13%" height="25" class="bg_05" align="middle">결재기간</td>
           <td width="37%" height="25" class="bg_06"><%=approval_period%> 일</td></tr></TBODY></table><br>

<!-- 발주품목 -->
	  <TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<TBODY>
				<TR vAlign=middle height=23>
				  <TD noWrap width=90 align=middle class='bg_05'>품목코드</TD>
				  <TD noWrap width=100% align=middle class='bg_05'>품목명</TD>
				  <TD noWrap width=60 align=middle class='bg_05'>발주<br>단가</TD>
				  <TD noWrap width=60 align=middle class='bg_05'>발주<br>수량</TD>
				  <TD noWrap width=50 align=middle class='bg_05'>발주<br>단위</TD>
				  <TD noWrap width=90 align=middle class='bg_05'>발주금액</TD>
				  <TD noWrap width=80 align=middle class='bg_05'>희망납기일</TD>
				</TR>
				
<%
			int no = 1;
			while(table_iter.hasNext()){
				table = (OrderInfoTable)table_iter.next();
%>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					  <TD align=middle height="24" class='list_bg'><%=table.getItemCode()%></TD>
					  <TD align=left class='list_bg' style='padding-left:2px'><%=table.getItemDesc()%></TD>
					  <TD align=middle class='list_bg' ><%=sp.getMoneyFormat(table.getUnitCost(),"")%></TD>
					  <TD align=middle class='list_bg' style='padding-right:5px'><%=table.getOrderQuantity()%></TD>
					  <TD align=middle class='list_bg'><%=table.getOrderUnit()%></TD>
					  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getOrderCost(),"")%></TD>
					  <TD align=right class='list_bg' style='padding-right:2px'><%=table.getDeliveryDate()%></TD>
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