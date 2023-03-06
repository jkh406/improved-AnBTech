<%@ include file="../../admin/configPopUp.jsp"%>
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
	String monetary_unit		= table.getMonetaryUnit();
	String vat_type				= table.getVatType();
	String vat_rate				= table.getVatRate();
	String exchange_rate		= table.getExchangeRate();
	String is_vat_contained		= table.getIsVatContained().equals("1")?"포함":"별도";
	String approval_type		= table.getApprovalType();
	String approval_period		= table.getApprovalPeriod();
	String payment_type			= table.getPaymentType();
	String supplyer_info		= table.getSupplyerInfo();
	String supplyer_tel			= table.getSupplyerTel();
	String other_info			= table.getOtherInfo();

	String vat_mount			= sp.getMoneyFormat(table.getVatMount(),"");
	double order_total_mount	= 0;

	if(table.getIsVatContained().equals("1")){
		order_total_mount = (Double.parseDouble(table.getOrderTotalMount()) + Double.parseDouble(table.getVatMount()))/Integer.parseInt(exchange_rate);
	}else{
		order_total_mount = Double.parseDouble(table.getOrderTotalMount())/Integer.parseInt(exchange_rate);
	}
		
	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new OrderInfoTable();
	Iterator table_iter = item_list.iterator();

%>
<html>

<head>
<meta http-equiv="Content-Language" content="ko">
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>PURCHASE ORDER</title>
<style type="text/css">
<!--
td {
	font-family: "굴림";
	font-size: 12px;
	color: #000000;
}
.red {
	font-family: "굴림";
	font-size: 11px;
	color: #000000;
}

-->
</style>
</head>

<body oncontextmenu="return false">

<table border="0" width="700" height="132" cellpadding="2">
  <tr>
    <td width="100%" height="16" align="right">
	<div id="print" style="position:relative;visibility:visible;">
		<a href="Javascript:go_print('<%=order_no%>');"><img src="../pu/images/bt_in_order.gif" border="0" alt="내자발주서" align="absmiddle"></a>
		<a href='Javascript:winprint();'><img src="../pu/images/bt_print.gif" border="0" align="absmiddle"></a>
		<a href='Javascript:self.close();'><img src="../pu/images/bt_close.gif" border="0" align="absmiddle"></a></div>
    </td>
  </tr>
  <tr>
    <td width="100%" height="16">
      <table border="0" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="22%"><img src="../images/top_logo_slink.jpg" border="0"></td>
          <td width="78%"><b><i>SpaceLink Corporation - Professional Communication System Integrator</i></b><br>
            10FL. JEI Bldg., 1294-3 Baekseok, Ilsan, Goyang-city, Kyunggido, South Korea, 411-816<br>       
            TEL:+82-31-810-2262, FAX:+82-31-810-2201, E-MAIL:kcpark@spacelink.co.kr</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%" height="7">
      <hr>
    </td>
  </tr>
  <tr>
    <td width="100%" height="50" valign="top">
      <div align="center"><font size='4' face='굴림' color='#000000'><b><u>PURCHASE ORDER</u></b></font></div></td>      
  </tr>
  <tr>
    <td width="100%" height="37">
      <table border="0" width="100%" cellspacing="0" cellpadding="2">
		  <tr>
			<td width="20%">To</td>
			<td width="80%">: <%=supplyer_name%></td></tr>
		  <tr>
			<td width="20%">Date</td>
			<td width="80%">: <%=order_date%></td></tr>
		  <tr>
			<td width="20%"><b>Order No</b></td>
			<td width="80%"><b>: <%=order_no%></b></td></tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%" height="48">We, SPACELINK CORPORATION, hereby place the following order with you:<br>   
    </td> 
  </tr>
  <tr>
    <td width="100%" height="16">
		 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
					<TR vAlign=middle height=23>
					  <TD noWrap width=30 align=middle class='list_title'>No.</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>Item Code</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=100% align=middle class='list_title'>item Desc.</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>Unit Price</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=60 align=middle class='list_title'>Qty</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=60 align=middle class='list_title'>Unit</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>Total Amount</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
		<%
			int no = 1;
			while(table_iter.hasNext()){
				table = (OrderInfoTable)table_iter.next();
				double unit_cost = Double.parseDouble(table.getUnitCost())/Integer.parseInt(exchange_rate);
				double order_cost = Double.parseDouble(table.getOrderCost())/Integer.parseInt(exchange_rate);
		%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle height="24" class='list_bg'><%=no%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getItemCodeLink()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(Double.toString(unit_cost),"00")%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOrderQuantity()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOrderUnit()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(Double.toString(order_cost),"00")%></td>
					</TR>
					<TR><TD colSpan=13 background="../pu/images/dot_line.gif"></TD></TR>
		<%
				no++;	
			}
		%>
					<TR><TD height="24" colSpan=13 class='list_bg'><b>TOTAL AMOUNT</b>: <b><font color="red"><%=sp.getMoneyFormat(Double.toString(order_total_mount),"00")%></font> USD</b></TD></TR>
					<TR><TD colSpan=13 background="../pu/images/dot_line.gif"></TD></TR>
				</TBODY></TABLE>
    </td>
  </tr>
  <tr><td width="100%" height="16">
    <table border="0" width="100%" cellspacing="0" cellpadding="2">
	  <tr>
        <td width="20%">Payment Terms</td>
        <td width="80%">: <%=approval_type%></td></tr>
	  <tr>
        <td width="20%"><b>Freight Forwarder</b></td>
        <td width="80%">:</td></tr>
	  <tr>
        <td width="100%" colspan="2">Factory Test&Inspection Data sheet should be included.</td></tr>
	  <tr>
        <td width="100%" height="10" colspan="2"></td></tr>
	  <tr>
        <td width="20%" valign="top">Ship to and Bill to</td>
        <td width="80%">:KC Park<br>&nbsp;SPACELINK CORPORATION<br>&nbsp;10FL. JEI Bldg., 1294-3 Baekseok, Ilsan, Goyang-city, Kyunggido, South Korea, 411-816</td></tr>
	  <tr>
        <td width="100%" height="10" colspan="2"></td></tr>
	  <tr>
        <td width="20%" valign="top">Remarks</td>
        <td width="80%">:<b>As for order acknowledgment, please sign and return this P/P by fax.</b><br>&nbsp;Above tersm&conditions could be changeable by mutual agreement.</td></tr>
    </table></td></tr>
  <tr><td width="100%" height="30"></td></tr>
  <tr><td width="100%" height="16">
    <table border="0" width="100%" cellspacing="0" cellpadding="2">
	  <tr>
        <td width="50%">ORDER PLACED BY THE BUYER:</td>
        <td width="50%">ACKNOWLEDGED BY THE SELLER:</td></tr>
	  <tr>
        <td width="50%" height="60"></td>
        <td width="50%" height="60"></td></tr>
	  <tr>
        <td width="50%" align="left"><hr width="60%"></td>
        <td width="50%" align="left"><hr width="60%"></td></tr>
	  <tr>
        <td width="50%" align="left">(Authorized Signature by) KC Park</td>
        <td width="50%" align="left">(Authorized Signature by)</td></tr></table></td></tr></table>

</body>

</html>
<script language='javascript'>
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

//메일발송
function send_mail() {
	var url = "../pu/other/send_order.jsp?order_no=<%=order_no%>";
	wopen(url,'SEND_ORDER','500','206','scrollbars=no,toolbar=no,status=no,resizable=no');

}

//내자 발주서출력
function go_print(order_no) {
	var url = "PurchaseMgrServlet?mode=order_print&order_no="+order_no;
	location.href = url;
}
</script>