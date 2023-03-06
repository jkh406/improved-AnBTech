<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%!
	EtcInOutInfoTable table;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
%>

<%
	String mode			= request.getParameter("mode");	// ���
	String inout_type	= request.getParameter("inout_type");	// �������

	table = (EtcInOutInfoTable)request.getAttribute("INOUT_INFO");
	String inout_no				= table.getInOutNo();
	String supplyer_code		= table.getSupplyerCode();
	String supplyer_name		= table.getSupplyerName();
	String inout_date			= table.getInOutDate();
	String total_mount			= sp.getMoneyFormat(table.getTotalMount(),"");

	String item_code			= table.getItemCode();
	String item_name			= table.getItemName();
	String item_desc			= table.getItemDesc();
	String quantity				= table.getQuantity();
	String item_unit			= table.getItemUnit();
	String unit_cost			= sp.getMoneyFormat(table.getUnitCost(),"");
	String inout_cost			= sp.getMoneyFormat(table.getInOutCost(),"");
	String factory_code			= table.getFactoryCode();
	String factory_name			= table.getFactoryName();
	String warehouse_code		= table.getWarehouseCode();
	String warehouse_name		= table.getWarehouseName();
	String unit_type			= table.getUnitType();

	//����Ʈ ��������
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new EtcInOutInfoTable();
	Iterator table_iter = item_list.iterator();
%>
<html>
<link rel="stylesheet" type="text/css" href="../st/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" oncontextmenu="return false">

<form name="reg" method="post" action="StockMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../st/images/blet.gif"> �������ǰ����</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5>&nbsp;</TD>
			  <TD align=left width=500>
				<img src='../st/images/bt_save.gif' onClick='javascript:add_item();' style='cursor:hand' align='absmiddle' alt="ǰ���߰�"> <a href="../servlet/StockMgrServlet?mode=list_etc_inout&in_or_out=OUT"><img src='../st/images/bt_cancel.gif' style='cursor:hand' align='absmiddle' alt="���" border="0"></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">����ȣ</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type='text' size='15' name='inout_no' value='<%=inout_no%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">ǰ���ڵ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='item_code' value='<%=item_code%>' class="text_01" readOnly> <a href="javascript:searchCMInfo();"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">ǰ���</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='20' name='item_name' value='<%=item_name%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='quantity' value='<%=quantity%>' onKeyPress="currency(this);" onBlur="cal_enter_cost();" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='item_unit' value='<%=item_unit%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">���ܰ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='unit_cost' value='<%=unit_cost%>' onKeyPress="currency(this);" onKeyup="com(this);" onBlur="cal_enter_cost();" style="text-align:right;" class="text_01">��</td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">���ݾ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='inout_cost' value='<%=inout_cost%>' readOnly>��</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='8' name='factory_code' value='<%=factory_code%>' class="text_01" readOnly> <input type='text' size='15' name='factory_name' value='<%=factory_name%>' class="text_01" readOnly> <a href="javascript:sel_factory();"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif"></td>
           <td width="37%" height="25" class="bg_04"><!--<input type='text' size='8' name='warehouse_code' value='<%=warehouse_code%>' readOnly> <input type='text' size='15' name='warehouse_name' value='<%=warehouse_name%>' readOnly> <a href="javascript:sel_warehouse();"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a>--></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>


<!-- ���ǰ�� -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../st/images/title_out_item.gif' border='0' alt='���ǰ��'></TD></TR></TABLE>
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=60 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>ǰ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���ܰ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���ݾ�</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (EtcInOutInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getUnitCost(),"")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getInOutCost(),"")%></td>
			</TR>
			<TR><TD colSpan=13 background="../st/images/dot_line.gif"></TD></TR>
<%
		no++;	
	}
%>
		</TBODY></TABLE>

<input type='hidden' name='item_desc' value='<%=item_desc%>'>
<input type='hidden' name='item_type'>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='in_or_out' value='OUT'>
<input type='hidden' name='inout_type' value='<%=inout_type%>'>

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
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=item_unit";
	wopen(strUrl,"SEARCH_ITEM",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}


//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../st/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//���ǰ�� �߰� �� ����
function add_item() 
{ 
	var f = document.reg;

	if(f.item_code.value == ''){
		alert("ǰ���� ã�� �����Ͻʽÿ�.");
		return;
	}

	if(f.quantity.value == '0' || f.quantity.value == ''){
		alert("�������� �Է��Ͻʽÿ�.(�������� 0�̻��� �մϴ�)");
		f.quantity.focus();
		return;
	}

	if(f.item_unit.value == ''){
		alert("�������� �����Ͻʽÿ�.");
		return;
	}

	if(f.unit_cost.value == '0' || f.unit_cost.value == ''){
		alert("���ܰ��� �Է��Ͻʽÿ�.");
		f.unit_cost.focus();
		return;
	}

	if(f.factory_code.value == ''){
		alert("�������� ã�Ƽ� �����Ͻʽÿ�.");
		f.factory_code.focus();
		return;
	}

	f.unit_cost.value	= unComma(f.unit_cost.value);
	f.inout_cost.value	= unComma(f.inout_cost.value);

	f.submit();
}

// ���ݾ� ���
function cal_enter_cost() 
{ 
	var f = document.reg;
	var quantity = f.quantity.value;
	var unit_cost = unComma(f.unit_cost.value);

	f.inout_cost.value = Comma(quantity * unit_cost);
}

// ���ڸ� �Էµǰ�
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

// õ���� �޸� ����
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

// ���ڿ��� Comma ����
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
	wopen(url,'enterCode','400','307','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// â�� ����
function sel_warehouse(){
	var f=document.reg;
	// #1.���� ���� �Է��� â������ �����ϰԲ� ó��.
	// #2.���� ������ â�� �����Ͽ� �Է�.
	if(f.factory_code.value=="" || f.factory_name.value=="") {
		alert("������ ���� ������ �ֽʽÿ�.");
		return;
	} else {
		url = "../st/config/searchWarehouseInfo.jsp?tablename=warehouse_info_table&field=warehouse_code/warehouse_name&factory_code="+f.factory_code.value;
		wopen(url,'enterCode','400','307','scrollbars=no,toolbar=no,status=no,resizable=no');
	}
}
</script>