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
	String order_total_mount	= sp.getMoneyFormat(table.getOrderTotalMount(),"");
	String monetary_unit		= table.getMonetaryUnit();
	String exchange_rate		= sp.getMoneyFormat(table.getExchangeRate(),"");
	String vat_type				= table.getVatType();
	String vat_rate				= table.getVatRate();
	String vat_mount			= sp.getMoneyFormat(table.getVatMount(),"");
	String is_vat_contained		= table.getIsVatContained();
	String approval_type		= table.getApprovalType();
	String approval_period		= table.getApprovalPeriod();
	String payment_type			= table.getPaymentType();
	String supplyer_info		= table.getSupplyerInfo();
	String supplyer_tel			= table.getSupplyerTel();
	String other_info			= table.getOtherInfo();

	String item_code			= table.getItemCode();
	String item_name			= table.getItemName();
	String item_desc			= table.getItemDesc();
	String delivery_date		= table.getDeliveryDate();
	String order_quantity		= table.getOrderQuantity();
	String order_unit			= table.getOrderUnit();
	String unit_cost			= sp.getMoneyFormat(table.getUnitCost(),"");
	String order_cost			= sp.getMoneyFormat(table.getOrderCost(),"");
	String request_no			= table.getRequestNo();

	//����Ʈ ��������
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new OrderInfoTable();
	Iterator table_iter = item_list.iterator();

	//��ũ ���ڿ� ��������
	redirect = new PurchaseLinkUrl();
	redirect = (PurchaseLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut			= redirect.getViewPagecut();
	String view_total			= redirect.getViewTotal();
	String view_boardpage		= redirect.getViewBoardpage();
	String view_totalpage		= redirect.getViewTotalpage();

	String link_info_modify 	= redirect.getLinkInfoModify();
	String link_info_delete 	= redirect.getLinkInfoDelete();
	String link_item_add	 	= redirect.getLinkItemAdd();
	String link_item_modify 	= redirect.getLinkItemModify();
	String link_item_delete 	= redirect.getLinkItemDelete();
	String link_list 			= redirect.getLinkList();
	String link_approval		= redirect.getLinkApproval();
	String link_print			= redirect.getLinkPrint();
	String link_order_info		= redirect.getLinkRequestInfo();
	String link_app_info		= redirect.getLinkAppInfo();
%>
<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display();">

<form name="reg_order" method="post" action="PurchaseMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> ���Ź���ǰ����</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=100%>
	<%=link_item_modify%><%=link_item_delete%><%=link_approval%><%=link_print%><%=link_app_info%><%=link_list%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ǰ��������� -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ֹ�ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=order_no%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�ѹ��ֱݾ�</td>
           <td width="37%" height="25" class="bg_04"><%=order_total_mount%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���޾�ü��</td>
           <td width="37%" height="25" class="bg_04" colspan="3"><%=supplyer_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">ǰ���ڵ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='item_code' value='<%=item_code%>' class="text_01" readOnly><!--<a href="javascript:searchCMInfo();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a>--></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">ǰ���</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='20' name='item_name' value='<%=item_name%>' class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">ǰ�񼳸�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='35' name='item_desc' value='<%=item_desc%>'></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���������</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='delivery_date' value='<%=delivery_date%>' class="text_01" readOnly> <a href="Javascript:OpenCalendar('delivery_date');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ּ���</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='order_quantity' value='<%=order_quantity%>' onKeyPress="currency(this);" onBlur="cal_order_cost();" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ִ���</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='order_unit' value='<%=order_unit%>' class="text_01" readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ִܰ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='unit_cost' value='<%=unit_cost%>' onKeyPress="currency(this);" onKeyup="com(this);" onBlur="cal_order_cost();" style="text-align:right;" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ֱݾ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='order_cost' value='<%=order_cost%>' readOnly style="text-align:right;"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>

<input type='hidden' name='requester_id' value='<%=requestor_id%>'>
<input type='hidden' name='requester_div_code' value='<%=requestor_div_code%>'>
<input type='hidden' name='request_no' value='<%=request_no%>'>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='old_quantity' value='<%=order_quantity%>'>
<input type='hidden' name='order_no' value='<%=order_no%>'>

<!-- ����ǰ�� -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../pu/images/title_balju_item.gif' border='0' alt='����ǰ��'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:218; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>ǰ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>ǰ�񼳸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>���ִܰ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>���ּ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>���ִ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���ֱݾ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>���԰����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>ó������</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
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
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
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
			<TR><TD colSpan=21 background="../pu/images/dot_line.gif"></TD></TR>
<%
		no++;	
	}
%>
		</TBODY></TABLE></DIV>

</form>
</body>
</html>


<script language=javascript>
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}


// ǰ�� ���� ��������
function searchCMInfo(){
	var url = "../servlet/CodeMgrServlet?mode=list_item_p";	
	wopen(url,"return_noDesc",'800','500','scrollbars=yes,toolbar=no,status=no,resizable=no');
}


//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//���޾�üã��
function sel_supplyer() 
{ 
	url = "../pu/order/searchCompany.jsp?sf=reg_order&sid=supplyer_code&sname=supplyer_name";
	wopen(url,'add','600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function get_order_code(){
	url = "../pu/config/searchCode.jsp?tablename=pu_order_type&field=order_type/order_name";
	wopen(url,'purchase','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//������������
function delete_order() 
{ 
	var f = document.reg_order;
	var order_no = f.order_no.value;

	var c = confirm("����ǰ���� ��� �����˴ϴ�. ���� ���ְ��� �����Ͻðڽ��ϱ�?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_order_info&order_no="+order_no;
}

//����ǰ���߰�
function add_item() 
{ 
	var f = document.reg_order;

	if(f.item_code.value == ''){
		alert("ǰ���ڵ带 ã�� �����Ͻʽÿ�.");
		return;
	}

	if(f.item_name.value == ''){
		alert("ǰ����� �Է��Ͻʽÿ�.");
		return;
	}

	if(f.delivery_date.value == ''){
		alert("����������� ã�Ƽ� �����Ͻʽÿ�.");
		return;
	}

	if(f.order_quantity.value == ''){
		alert("���ּ����� �Է��Ͻʽÿ�.");
		f.order_quantity.focus();
		return;
	}

	if(f.order_unit.value == ''){
		alert("���ִ����� ã�Ƽ� �����Ͻʽÿ�.");
		f.order_unit.focus();
		return;
	}

	if(f.unit_cost.value == ''){
		alert("���ִܰ��� �Է��Ͻʽÿ�.");
		f.unit_cost.focus();
		return;
	}

//	f.exchange_rate.value		= unComma(f.exchange_rate.value);
//	f.vat_mount.value			= unComma(f.vat_mount.value);
//	f.order_total_mount.value	= unComma(f.order_total_mount.value);

	f.unit_cost.value	= unComma(f.unit_cost.value);
	f.order_cost.value	= unComma(f.order_cost.value);
//	f.order_quantity.value	= f.order_quantity.value - f.old_quantity.value;
//	alert(f.order_quantity.value);

	f.submit();
}

//ǰ�����
function del_item() 
{ 
	var f = document.reg_order;
	var order_no = f.order_no.value;
	var item_code  = f.item_code.value;
	var request_no = f.request_no.value;

	var c = confirm("ǰ���ȣ:"+item_code+" ��(��) �����Ͻðڽ��ϱ�?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_order&order_no="+order_no+"&item_code="+item_code+"&request_no="+request_no;
}

//���޾�ü������
function reflect_supplyer() 
{ 
	var f = document.reg_order;
	var supplyer_code = f.supplyer_code.value;
	var mode = f.mode.value;

	if(mode == 'order_item_add'){
		f.mode.value = "reflect_supply_info";
		f.submit();
	}
} 

//���ּ����
function go_print() {
	var f = document.reg_order;
	var order_no = f.order_no.value;
	var url = "PurchaseMgrServlet?mode=order_print&order_no="+order_no;
	wopen(url,'add','750','600','scrollbars=yes,toolbar=no,status=no,resizable=no');

}

//������������
function order_info()
{	var f = document.reg_order;
	var order_no = f.order_no.value;
	location.href = "PurchaseMgrServlet?mode=modify_order_info&order_no="+order_no;
}
/**********************
 * ���� ���
 **********************/
function cal_vat_mount() 
{ 
	var f = document.reg_order;
	var vat_rate = f.vat_rate.value;
	var order_total_mount = unComma(f.order_total_mount.value);

	f.vat_mount.value = Comma(vat_rate * order_total_mount / 100);
}

/**********************
 * ���ֱݾ� ���
 **********************/
function cal_order_cost() 
{ 
	var f = document.reg_order;
	var order_quantity = f.order_quantity.value;
	var unit_cost = unComma(f.unit_cost.value);

	f.order_cost.value = Comma(order_quantity * unit_cost);
}

/**********************
 * ���ڸ� �Էµǰ�
 **********************/
function currency(obj)
{
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false
	}
}

function com(obj)
{
	obj.value = unComma(obj.value);
	obj.value = Comma(obj.value);
}

/**********************
 * õ���� �޸� ����
 **********************/
function Comma(input) {

  var inputString = new String;
  var outputString = new String;
  var counter = 0;
  var decimalPoint = 0;
  var end = 0;
  var modval = 0;

  inputString = input.toString();
  outputString = '';
  decimalPoint = inputString.indexOf('.', 1);

  if(decimalPoint == -1) {
     end = inputString.length - (inputString.charAt(0)=='0' ? 1:0);
     for (counter=1;counter <=inputString.length; counter++)
     {
        var modval =counter - Math.floor(counter/3)*3;
        outputString = (modval==0 && counter <end ? ',' : '') + inputString.charAt(inputString.length - counter) + outputString;
     }
  }
  else {
     end = decimalPoint - ( inputString.charAt(0)=='-' ? 1 :0);
     for (counter=1; counter <= decimalPoint ; counter++)
     {
        outputString = (counter==0  && counter <end ? ',' : '') +  inputString.charAt(decimalPoint - counter) + outputString;
     }
     for (counter=decimalPoint; counter < decimalPoint+3; counter++)
     {
        outputString += inputString.charAt(counter);
     }
 }
    return (outputString);
}

/**********************
 * ���ڿ��� Comma ����
 **********************/
function unComma(input) {
   var inputString = new String;
   var outputString = new String;
   var outputNumber = new Number;
   var counter = 0;
   if (input == '')
   {
	return 0
   }
   inputString=input;
   outputString='';
   for (counter=0;counter <inputString.length; counter++)
   {
      outputString += (inputString.charAt(counter) != ',' ?inputString.charAt(counter) : '');
   }
   outputNumber = parseFloat(outputString);
   return (outputNumber);
}

function order_app_view(){

	var order_no = '<%=order_no%>';
	//location.href ="../servlet/PurchaseMgrServlet?mode=order_app_view&order_no="+order_no;
	var para = "mode=order_app_view&request_no="+order_no+"&request_type="; 
	location.href="../gw/approval/module/pu_OrderApp.jsp?"+para;
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 555;
	item_list.style.height = div_h;

} 
</script>