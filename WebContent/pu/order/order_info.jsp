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

	//�������� ��������
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
	String inout_type			= table.getInOutType();

	//����ǰ�� ����Ʈ ��������
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
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> ���Ź��ֵ��</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=100%>
					<%=link_item_add%><%=link_info_modify%><%=link_info_delete%><%=link_app_info%><%=link_print%><%=link_list%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- �������� -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ֹ�ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='order_no' value='<%=order_no%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='order_date' value='<%=order_date%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���־�ü</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='supplyer_code' value='<%=supplyer_code%>' class="text_01" readOnly> <input type='text' size='18' name='supplyer_name' value='<%=supplyer_name%>' class="text_01" readOnly> <a href="javascript:sel_supplyer();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�����ڱ���</td>
           <td width="37%" height="25" class="bg_04"><input type='radio' name='inout_type' value='D' <% if(inout_type.equals("D")) out.print("checked"); %>>���� <input type='radio' name='inout_type' value='O' <% if(inout_type.equals("O")) out.print("checked"); %>>����</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">ȭ�����</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='monetary_unit' value='<%=monetary_unit%>' class="text_01" readOnly> <a href="javascript:sel_currency()"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>

           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">ȯ��</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='exchange_rate' value='<%=exchange_rate%>' onKeyPress="currency(this);" onKeyup="com(this);" style="text-align:right;" maxlength="5"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>

		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">VAT��</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='vat_rate' value='<%=vat_rate%>' onKeyPress="currency(this);" onBlur="cal_vat_mount();" class="text_01"> %</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">VAT�ݾ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='vat_mount' value='<%=vat_mount%>' style="text-align:right;" readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">VAT���Կ���</td>
           <td width="37%" height="25" class="bg_04"><input type='radio' name='is_vat_contained' value='0' <% if(is_vat_contained.equals("0")) out.print("checked"); %>>���� <input type='radio' name='is_vat_contained' value='1' <% if(is_vat_contained.equals("1")) out.print("checked"); %>>����</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�ѹ��ֱݾ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='order_total_mount' value='<%=order_total_mount%>' readOnly style="text-align:right;"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='approval_type' value='<%=approval_type%>' class="text_01" maxlength="15"> (��:����50%,����50%)</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">����Ⱓ</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='3' name='approval_period' value='<%=approval_period%>' class="text_01" maxlength="3"> ��</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���Ŵ����</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='requester_info' value='<%=requestor_info%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���Ŵ��μ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='requester_div_name' value='<%=requestor_div_name%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr><!--
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='order_type' value='<%=order_type%>'> <a href="javascript:get_order_code()"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif"></td>
           <td width="37%" height="25" class="bg_04"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>-->
	   </tbody></table>

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
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
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

<input type='hidden' name='requester_id' value='<%=requestor_id%>'>
<input type='hidden' name='requester_div_code' value='<%=requestor_div_code%>'>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='order_type' value='<%=order_type%>'>
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

//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//���־�üã��
function sel_supplyer() 
{ 
	url = "../pu/order/searchCompany.jsp?sf=reg_order&sid=supplyer_code&sname=supplyer_name";
	wopen(url,'add','600','308','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function get_order_code(){
	url = "../pu/config/searchCode.jsp?tablename=pu_order_type&field=order_type/order_name";
	wopen(url,'purchase','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//��������üũ
function save_order() 
{ 
	var f = document.reg_order;

	if(f.supplyer_code.value == '' || f.supplyer_name.value == ''){
		alert("���־�ü�� ã�Ƽ� �����Ͻʽÿ�");
		return;
	}

	if(f.monetary_unit.value == ''){
		alert("ȭ������� ã�Ƽ� �����Ͻʽÿ�.");
		return;
	}

	if(f.inout_type[1].checked == true && (f.exchange_rate.value == '0' || f.exchange_rate.value == '')){
		alert("�����ϰ�� �ݵ�� ȯ���� �Է��ؾ��մϴ�.");
		f.exchange_rate.focus();
		return;
	}

	if(f.vat_rate.value == '' || f.vat_mount.value == ''){
		alert("VAT���� �Է��Ͻʽÿ�.");
		return;
	}

	if(f.approval_type.value == ''){
		alert("�������� �Է��Ͻʽÿ�.");
		f.approval_type.focus();
		return;
	}

	if(f.approval_period.value == ''){
		alert("����Ⱓ�� �Է��Ͻʽÿ�.");
		f.approval_period.focus();
		return;
	}

	f.exchange_rate.value		= unComma(f.exchange_rate.value);
	f.vat_mount.value			= unComma(f.vat_mount.value);
	f.order_total_mount.value	= unComma(f.order_total_mount.value);
	
//	f.mode.value = "modify_order_info";
	f.submit();
}

//������������
function delete_order() 
{ 
	var f = document.reg_order;
	var order_no = f.order_no.value;

	var c = confirm("����ǰ���� ��� �����˴ϴ�. ���� ���ְ��� �����Ͻðڽ��ϱ�?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_order_info&order_no="+order_no;
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

//��ȭ �ڵ� ��������
function sel_currency()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=reg_order&type=CURRENCY&div=one&code=monetary_unit&&code_field=Type����&name_field=��������&minor_code=ȭ�����&minor_field=ȭ�������";
	wopen(url,'add','550','307','scrollbars=no,toolbar=no,status=no,resizable=no');
} 

//���γ�������
function viewAppInfo(){
		var order_no = document.reg_order.order_no.value;
		wopen('../servlet/PurchaseMgrServlet?mode=order_app_print&order_no='+order_no,'order','650','600','scrollbars=yes,toolbar=no,status=no,resizable=no');

}

//���ּ����
function go_print() {
	var f = document.reg_order;
	var order_no = f.order_no.value;
	var url = "PurchaseMgrServlet?mode=order_print&order_no="+order_no;
	wopen(url,'add','750','600','scrollbars=yes,toolbar=no,status=no,resizable=no');

}

//������
function order_app_view(){
	var f = document.reg_order;

	if(f.supplyer_code.value == '' || f.supplyer_name.value == ''){
		alert("�ʿ��� ���������� ������� �ʾҽ��ϴ�. ���������� ���� �����Ͻ� �� �����Ͻʽÿ�.");
		location.reload();
		return;
	}

	if(f.monetary_unit.value == ''){
		alert("�ʿ��� ���������� ������� �ʾҽ��ϴ�. ���������� ���� �����Ͻ� �� �����Ͻʽÿ�.");
		location.reload();
		return;
	}

	if(f.inout_type[1].checked == true && (f.exchange_rate.value == '0' || f.exchange_rate.value == '')){
		alert("�ʿ��� ���������� ������� �ʾҽ��ϴ�. ���������� ���� �����Ͻ� �� �����Ͻʽÿ�.");
		location.reload();
		return;
	}

	if(f.vat_rate.value == '' || f.vat_mount.value == ''){
		alert("�ʿ��� ���������� ������� �ʾҽ��ϴ�. ���������� ���� �����Ͻ� �� �����Ͻʽÿ�.");
		location.reload();
		return;
	}

	if(f.approval_type.value == ''){
		alert("�ʿ��� ���������� ������� �ʾҽ��ϴ�. ���������� ���� �����Ͻ� �� �����Ͻʽÿ�.");
		location.reload();
		return;
	}

	if(f.approval_period.value == ''){
		alert("�ʿ��� ���������� ������� �ʾҽ��ϴ�. ���������� ���� �����Ͻ� �� �����Ͻʽÿ�.");
		location.reload();
		return;
	}

	var order_no = f.order_no.value;
	var para = "mode=order_app_view&request_no="+order_no+"&request_type="; 
	
	var c = confirm("���ֹ�ȣ:"+order_no+" �� ���� ���ڰ��縦 �����Ͻðڽ��ϱ�?");
	if(c) location.href="../gw/approval/module/pu_OrderApp.jsp?"+para;
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 

	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//	var div_h = h - 560;
	var div_h = c_h - 264;
	item_list.style.height = div_h;

} 
</script>