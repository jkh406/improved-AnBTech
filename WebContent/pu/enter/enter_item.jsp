<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = ""
	language = "java"
	contentType = "text/html;charset=KSC5601"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.pu.entity.*"
%>
<%!
	EnterInfoTable table;
	PurchaseLinkUrl redirect;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
%>

<%
	String mode = request.getParameter("mode");	// ���

	table = (EnterInfoTable)request.getAttribute("ENTER_INFO");
	String enter_no				= table.getEnterNo();
	String supplyer_code		= table.getSupplyerCode();
	String supplyer_name		= table.getSupplyerName();
	String enter_date			= table.getEnterDate();
	String enter_total_mount	= sp.getMoneyFormat(table.getEnterTotalMount(),"");
	String filelink				= table.getFileLink();

	String item_code			= table.getItemCode();
	String item_name			= table.getItemName();
	String item_desc			= table.getItemDesc();
	String enter_quantity		= table.getEnterQuantity();
	String enter_unit			= table.getEnterUnit();
	String unit_cost			= sp.getMoneyFormat(table.getUnitCost(),"");
	String enter_cost			= sp.getMoneyFormat(table.getEnterCost(),"");
	String factory_code			= table.getFactoryCode();
	String factory_name			= table.getFactoryName();
	String warehouse_code		= table.getWarehouseCode();
	String warehouse_name		= table.getWarehouseName();
	String request_no			= table.getRequestNo();
	String order_no				= table.getOrderNo();

	//����Ʈ ��������
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new EnterInfoTable();
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
	String link_enter_info		= redirect.getLinkRequestInfo();
%>
<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display();">

<form name="reg_enter" method="post" action="PurchaseMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> �԰�ǰ����</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
				<%=link_item_modify%><%=link_item_delete%><%=link_approval%><%=link_list%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰��ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='enter_no' value='<%=enter_no%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰�����</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='enter_date' value='<%=enter_date%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">÷�ι���</td>
           <td width="37%" height="25" class="bg_04"><%=filelink%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�����Ѿ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='enter_total_mount' value='<%=enter_total_mount%>' style="text-align:right;" readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">ǰ���ڵ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='item_code' value='<%=item_code%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">ǰ���</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='20' name='item_name' value='<%=item_name%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰����</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='enter_quantity' value='<%=enter_quantity%>' onKeyPress="currency(this);" onBlur="cal_enter_cost();"></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰����</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='enter_unit' value='<%=enter_unit%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰�ܰ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='unit_cost' value='<%=unit_cost%>' onKeyPress="currency(this);" onKeyup="com(this);" onBlur="cal_enter_cost();" style="text-align:right;"></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���Աݾ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='enter_cost' value='<%=enter_cost%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type='text' size='5' name='factory_code' value='<%=factory_code%>' readOnly> <input type='text' size='20' name='factory_name' value='<%=factory_name%>' readOnly><!-- <a href="javascript:sel_factory();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a>--></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>


<!-- �԰�ǰ�� -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../pu/images/title_input_item.gif' border='0' alt='�԰�ǰ��'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:168; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>ǰ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>ǰ�񼳸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>�԰����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>�԰����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�԰�ܰ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���Աݾ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>ó������</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (EnterInfoTable)table_iter.next();
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
				  <TD align=middle class='list_bg'><%=table.getEnterQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getEnterUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getUnitCost(),"")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getEnterCost(),"")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></td>
			</TR>
			<TR><TD colSpan=17 background="../pu/images/dot_line.gif"></TD></TR>
<%
		no++;	
	}
%>
		</TBODY></TABLE></DIV>

<input type='hidden' name='item_desc' value='<%=item_desc%>'>
<input type='hidden' name='request_no' value='<%=request_no%>'>
<input type='hidden' name='order_no' value='<%=order_no%>'>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='old_quantity' value='<%=enter_quantity%>'>
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
	wopen(url,"SEARCH_ITEM",'800','500','scrollbars=yes,toolbar=no,status=no,resizable=no');
}


//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//�԰�ǰ�� �߰� �� ����
function add_item() 
{ 
	var f = document.reg_enter;

	if(f.item_code.value == ''){
		alert("ǰ���ڵ带 ã�� �����Ͻʽÿ�.");
		return;
	}

	if(f.item_name.value == ''){
		alert("ǰ����� �����Ͻʽÿ�.");
		return;
	}

	if(f.enter_quantity.value == ''){
		alert("�԰������ �Է��Ͻʽÿ�.");
		f.enter_quantity.focus();
		return;
	}

	if(f.enter_unit.value == ''){
		alert("�԰������ �����Ͻʽÿ�.");
		return;
	}

	if(f.unit_cost.value == ''){
		alert("�԰�ܰ��� �Է��Ͻʽÿ�.");
		f.unit_cost.focus();
		return;
	}

	f.enter_total_mount.value = unComma(f.enter_total_mount.value);
	f.unit_cost.value	= unComma(f.unit_cost.value);
	f.enter_cost.value	= unComma(f.enter_cost.value);

	f.submit();
}

/**********************
 * ���Աݾ� ���
 **********************/
function cal_enter_cost() 
{ 
	var f = document.reg_enter;
	var enter_quantity = f.enter_quantity.value;
	var unit_cost = unComma(f.unit_cost.value);

	f.enter_cost.value = Comma(enter_quantity * unit_cost);
}

//ǰ�����
function del_item() 
{ 
	var f = document.reg_enter;
	var enter_no = f.enter_no.value;
	var item_code  = f.item_code.value;
	var request_no = f.request_no.value;
	var order_no = f.order_no.value;

	var c = confirm("ǰ���ȣ:"+item_code+" ��(��) �����Ͻðڽ��ϱ�?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_enter&enter_no="+enter_no+"&order_no="+order_no+"&request_no="+request_no+"&item_code="+item_code;
}

//������
function go_approval() {
	var f = document.reg_enter;
	var enter_no = f.enter_no.value;
	var para = "mode=enter_app_view&enter_no="+enter_no;
	var c = confirm("�԰��ȣ:"+enter_no+" �� ���� ���ڰ��縦 �����Ͻðڽ��ϱ�?");
	if(c) location.href="../gw/approval/module/pu_WarehousingApp.jsp?"+para;
}

//�԰�����������
function enter_info()
{	var f = document.reg_enter;
	var enter_no = f.enter_no.value;
	location.href = "PurchaseMgrServlet?mode=modify_enter_info&enter_no="+enter_no;
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

// ���� ����
function sel_factory(){
	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&field=factory_code/factory_name";
	wopen(url,'enterCode','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

// â�� ����
function sel_warehouse(){
	var f=document.reg_enter;
	// #1.���� ���� �Է��� â������ �����ϰԲ� ó��.
	// #2.���� ������ â�� �����Ͽ� �Է�.
	if(f.factory_code.value=="" || f.factory_name.value=="") {
		alert("������ ���� ������ �ֽʽÿ�.");
		return;
	} else {
		url = "../st/config/searchWarehouseInfo.jsp?tablename=warehouse_info_table&field=warehouse_code/warehouse_name&factory_code="+f.factory_code.value;
		wopen(url,'enterCode','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
	}
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 555;
	item_list.style.height = div_h;

} 
</script>