<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = ""
	language = "java"
	contentType = "text/html;charset=KSC5601"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.st.entity.*"
%>
<%!
	ReservedItemInfoTable table;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
%>

<%
	String mode = request.getParameter("mode");	// ���

	table = (ReservedItemInfoTable)request.getAttribute("DELIVERY_INFO");
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
			  <TD valign='middle' class="title"><img src="../st/images/blet.gif"> ��޻�������Ƿ�</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
				<img src='../st/images/bt_save.gif' onClick='javascript:add_item();' style='cursor:hand' align='absmiddle' alt="����"> <a href="javascript:history.go(-1);"><img src='../st/images/bt_cancel.gif' style='cursor:hand' align='absmiddle' alt="���" border="0"></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">�Ƿڹ�ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='delivery_no' value='<%=table.getDeliveryNo()%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">�Ƿ�����</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' value='<%=table.getRequestDate().substring(0,4)+"-"+table.getRequestDate().substring(4,6)+"-"+table.getRequestDate().substring(6,8)%>' readOnly><input type='hidden' name='reg_date' value='<%=table.getRequestDate()%>'></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">ǰ���ڵ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='item_code' value='<%=table.getItemCode()%>' class="text_01" readOnly> <a href="javascript:searchCMInfo();"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">ǰ���</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='20' name='item_name' value='<%=table.getItemName()%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">��û����</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='quantity' value='<%=table.getRequestQuantity()%>' onKeyPress="currency(this);" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">��û����</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='item_unit' value='<%=table.getRequestUnit()%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">��û����</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='factory_code' value='<%=table.getFactoryCode()%>' class="text_01" readOnly> <input type='text' size='20' name='factory_name' value='<%=table.getFactoryName()%>' class="text_01" readOnly> <a href="javascript:sel_factory();"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">�������ù�ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='ref_no' value='<%=table.getRefNo()%>' class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>

<input type='hidden' name='item_desc' value='<%=table.getItemDesc()%>'>
<input type='hidden' name='item_type' value='<%=table.getItemType()%>'>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='requestor_div_code' value='<%=table.getRequestorDivCode()%>'>
<input type='hidden' name='requestor_div_name' value='<%=table.getRequestorDivName()%>'>
<input type='hidden' name='requestor_id' value='<%=table.getRequestorId()%>'>
<input type='hidden' name='requestor_info' value='<%=table.getRequestorInfo()%>'>
</form>
</body>
</html>

<script language=javascript>
//�˾�â
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}


//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../st/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//����
function add_item() 
{ 
	var f = document.reg;

	if(f.item_code.value == ''){
		alert("ǰ���ڵ带 ã�� �����Ͻʽÿ�.");
		return;
	}

	if(f.item_name.value == ''){
		alert("ǰ����� �����Ͻʽÿ�.");
		return;
	}

	if(f.quantity.value == '0' || f.quantity.value == ''){
		alert("��û������ �Է��Ͻʽÿ�.(��û������ 0�̻��̾�� �մϴ�)");
		f.quantity.focus();
		return;
	}

	if(f.factory_code.value == ''){
		alert("��û������ ã�� �����Ͻʽÿ�.");
		return;
	}

	if(f.ref_no.value == ''){
		alert("�۾����ù�ȣ�� �Է��Ͻʽÿ�.");
		f.ref_no.focus();
		return;
	}

	document.onmousedown=dbclick;
	f.submit();
}

function dbclick() 
{
    if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�."); 	
}

/**********************
 * ���Աݾ� ���
 **********************/
function cal_enter_cost() 
{ 
	var f = document.reg;
	var quantity = f.quantity.value;
	var unit_cost = unComma(f.unit_cost.value);

	f.inout_cost.value = Comma(quantity * unit_cost);
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

// ǰ�� ���� ��������
function searchCMInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=item_unit";
	wopen(strUrl,"SEL_ITEM",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}
</script>