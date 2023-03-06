<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>

<%!
	OrderInfoTable table;
	PurchaseLinkUrl redirect;
%>

<%
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

	String item_code			= "";//table.getItemCode();
	String item_name			= "";//table.getItemName();
	String item_desc			= "";//table.getItemDesc();
	String delivery_date		= "";//table.getDeliveryDate();
	String order_quantity		= "";//table.getOrderQuantity();
	String order_unit			= "";//table.getOrderUnit();
	String unit_cost			= "";//sp.getMoneyFormat(table.getUnitCost(),"");
	String order_cost			= "";//sp.getMoneyFormat(table.getOrderCost(),"");
	String request_no			= "";//table.getRequestNo();

	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new OrderInfoTable();
	Iterator table_iter = item_list.iterator();

	
%>
<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display()">
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주번호</td>
           <td width="37%" height="25" class="bg_04"><%=order_no%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">등록일자</td>
           <td width="37%" height="25" class="bg_04"><%=order_date%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체명</td>
           <td width="37%" height="25" class="bg_04"><%=supplyer_name%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">내외자구분</td>
           <td width="37%" height="25" class="bg_04"><% if(inout_type.equals("D")) {%>내자 <% } else if(inout_type.equals("O")){%>외자<%}%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매담당자</td>
           <td width="37%" height="25" class="bg_04"><%=requestor_info%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매담당부서</td>
           <td width="37%" height="25" class="bg_04"><%=requestor_div_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>

		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">화폐단위</td>
           <td width="37%" height="25" class="bg_04"><%=monetary_unit%></td>

           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">환율</td>
           <td width="37%" height="25" class="bg_04"><%=exchange_rate%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>

		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">VAT율</td>
           <td width="37%" height="25" class="bg_04"><%=vat_rate%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">VAT금액</td>
           <td width="37%" height="25" class="bg_04"><%=vat_mount%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>


		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">VAT포함여부</td>
           <td width="37%" height="25" class="bg_04">
		   <% if(is_vat_contained.equals("0")) {%>별도 <%
		   } else if(is_vat_contained.equals("1")) { %>포함<%}%>
		   </td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">총발주금액</td>
           <td width="37%" height="25" class="bg_04"><%=order_total_mount%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">결재방법</td>
           <td width="37%" height="25" class="bg_04"><%=approval_type%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">결재기간</td>
           <td width="37%" height="25" class="bg_04"><%=approval_period%> 일</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>
<!-- 발주품목 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../pu/images/title_balju_item.gif' border='0' alt='발주품목'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>품목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>단가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>금액</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>희망납기일</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>기입고수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>진행상태</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (OrderInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getUnitCost(),"")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getOrderQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getOrderUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getOrderCost(),"")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveryDate()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveryQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></td>
			</TR>
			<TR><TD colSpan=19 background="../pu/images/dot_line.gif"></TD></TR>
<%
		no++;	
	}
%>
		</TBODY></TABLE></DIV>
</body>
</html>

<script language="javascript">
//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 625;
	item_list.style.height = div_h;
}
</script>
