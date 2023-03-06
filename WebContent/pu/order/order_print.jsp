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
	String mode = request.getParameter("mode");	// ���

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
	String exchange_rate		= sp.getMoneyFormat(table.getExchangeRate(),"");
	String is_vat_contained		= table.getIsVatContained().equals("1")?"����":"����";
	String approval_type		= table.getApprovalType();
	String approval_period		= table.getApprovalPeriod();
	String payment_type			= table.getPaymentType();
	String supplyer_info		= table.getSupplyerInfo();
	String supplyer_tel			= table.getSupplyerTel();
	String other_info			= table.getOtherInfo();

	String vat_mount			= sp.getMoneyFormat(table.getVatMount(),"");
	String order_total_mount	= "";

	if(table.getIsVatContained().equals("1")){
		order_total_mount = sp.getMoneyFormat(Double.toString(Double.parseDouble(table.getOrderTotalMount()) + Double.parseDouble(table.getVatMount())),"");
	}else{
		order_total_mount = sp.getMoneyFormat(table.getOrderTotalMount(),"");
	}
		
	//����Ʈ ��������
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
<title>���ּ�</title>
<style type="text/css">
<!--
td {
	font-family: "����";
	font-size: 12px;
	color: #000000;
}
.red {
	font-family: "����";
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
		<a href="Javascript:go_print2('<%=order_no%>');"><img src="../pu/images/bt_out_order.gif" border="0" alt="���ڹ��ּ�" align="absmiddle"></a>
		<a href='Javascript:winprint();'><img src="../pu/images/bt_print.gif" border="0" alt="�μ�" align="absmiddle"></a>
		<a href='Javascript:send_mail();'><img src="../pu/images/bt_export.gif" border="0" alt="���Ϲ߼�" align="absmiddle"></a>
		<a href='Javascript:self.close();'><img src="../pu/images/bt_close.gif" border="0" alt="�ݱ�" align="absmiddle"></a></div>
    </td>
  </tr>
  <tr>
    <td width="100%" height="16">
      <table border="0" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="22%"><img src="../images/top_logo_slink.jpg" border="0"></td>
          <td width="78%">(��)�����̽���ũ<br>
            411-816 ��⵵ ���� �ϻ걸 �鼮�� 1294-3        
            ��ɱ������� 10��<br>       
            TEL:(82)031-810-2262, FAX:(82)031-810-2203,        
            HomePage:www.spacelink.co.kr</td>
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
    <td width="100%" height="16">
      <div align="center"><font size='4' face='����' color='#000000'><b><u>�� �� ��</u></b></font></div></td>      
  </tr>
  <tr>
    <td width="100%" height="37">
      <table border="0" width="100%" cellspacing="0" cellpadding="2">
        <tr>
          <td width="15%">
            <p align="left">���޾�ü��</td>
          <td width="35%">:<%=supplyer_name%></td>
          <td width="25%">
            <p align="right">��������:</td>
          <td width="25%"><%=order_date%></td>
        </tr>
        <tr>
          <td width="15%">
            <p align="left">���޾�ü�ڵ�</td>
          <td width="35%">:<%=supplyer_code%></td>
          <td width="25%">
            <p align="right">���ֹ�ȣ:</td>
          <td width="25%"><%=order_no%></td>
        </tr>
        <tr>
          <td width="15%">��</td>
          <td width="35%">��</td>
          <td width="25%">
            <p align="right">���ִ��:</td>
          <td width="25%"><%=requestor_info%></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%" height="48">�ϱ�� ���� �ֹ��Ͽ��� ���Ǵ��     
      ��ǰ�� �� �ֵ��� �ͻ��� ������ ��Ź�帳�ϴ�.<br>   
    </td> 
  </tr>
  <tr>
    <td width="100%" height="16">

    
    <table border="0" width="100%" cellspacing="0" cellpadding="2">
      <tr>
        <td width="11%">������</td>
        <td width="89%">: <%=approval_type%></td>
      </tr>
      <tr>
        <td width="11%">����Ⱓ</td>
        <td width="89%">: <%=approval_period%>��</td>
      </tr>
      <tr>
        <td width="11%">ȭ�����</td>
        <td width="89%">: <%=monetary_unit%></td>
      </tr>
      <tr>
        <td width="11%">����ȯ��</td>
        <td width="89%">: <%=exchange_rate%>��</td>
      </tr>
      <tr>
        <td width="11%">VAT��</td>
        <td width="89%">: <%=vat_rate%>%</td>
      </tr>
      <tr>
        <td width="11%">VAT�ݾ�</td>
        <td width="89%">: <%=vat_mount%>��</td>
      </tr>
      <tr>
        <td width="11%">VAT���Կ���</td>
        <td width="89%">: <%=is_vat_contained%></td>
      </tr>
      <tr>
        <td width="11%">�ѹ��ֱݾ�</td>
        <td width="89%">: <font color="red"><b><%=order_total_mount%></b></font>��</td>
      </tr>
    </table>

    
    </td>
  </tr>
  <tr>
    <td width="100%" height="16"></td>
  </tr>
  <tr>
    <td width="100%" height="16">
		 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
					<TR vAlign=middle height=23>
					  <TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>ǰ���ڵ�</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=100% align=middle class='list_title'>ǰ���</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>���ִܰ�</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=60 align=middle class='list_title'>���ּ���</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=60 align=middle class='list_title'>���ִ���</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>���ֱݾ�</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>���������</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
		<%
			int no = 1;
			while(table_iter.hasNext()){
				table = (OrderInfoTable)table_iter.next();
		%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle height="24" class='list_bg'><%=no%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getItemCodeLink()%></td>
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
					</TR>
					<TR><TD colSpan=15 background="../pu/images/dot_line.gif"></TD></TR>
		<%
				no++;	
			}
		%>
				</TBODY></TABLE>
    </td>
  </tr>
</table>

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

//���Ϲ߼�
function send_mail() {
	var url = "../pu/other/send_order.jsp?order_no=<%=order_no%>";
	wopen(url,'SEND_ORDER','500','206','scrollbars=no,toolbar=no,status=no,resizable=no');

}

//���� ���ּ����
function go_print2(order_no) {
	var url = "PurchaseMgrServlet?mode=order_print2&order_no="+order_no;
	location.href = url;
}
</script>