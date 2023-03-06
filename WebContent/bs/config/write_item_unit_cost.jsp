<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.bs.entity.*"
%>
<%
	ItemUnitCostTable table = new ItemUnitCostTable();
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	table = (ItemUnitCostTable)request.getAttribute("UNIT_COST");

	String mode					= request.getParameter("mode");
	String title				= "ǰ�� �ܰ����";
	if(mode.equals("modify_item_unit_cost")) title = "ǰ�� �ܰ�����";	
	String mid					= table.getMid();
	String item_code			= table.getItemCode();     
	String item_name			= table.getItemName();     
	String sale_type			= table.getSaleType();     
	String approval_type		= table.getApprovalType(); 
	String apply_date			= table.getApplyDate();    
	String sale_unit			= table.getSaleUnit();     
	String money_type			= table.getMoneyType();    
	String sale_unit_cost		= sp.getMoneyFormat(table.getSaleUnitCost(),"");

	String readonly				= "";
	if(mode.equals("modify_item_unit_cost")) readonly = "readonly";	
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../bs/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>

<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" action="SalesConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../bs/images/blet.gif"> <%=title%></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
						
						<IMG src='../bs/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
						<%if(mode.equals("modify_item_unit_cost")) {%>
						<IMG src='../bs/images/bt_del.gif' onClick='javascript:del();' style='cursor:hand' align='absmiddle'>
						<%}%>
						<IMG src='../bs/images/bt_cancel.gif' onClick='javascript:history.back();' style='cursor:hand' align='absmiddle'></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ǰ��������� -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">ǰ���ڵ�</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='10' maxlength='15' name='item_code' value='<%=item_code%>' <%=readonly%> class="text_01" maxlength="2">
				<%if(!mode.equals("modify_item_unit_cost")){%>
				<a href="javascript:searchCMInfo();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle">
				<%}%>
			</TD>
            <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">ǰ���</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='30' maxlength='50' name='item_desc' value='<%=item_name%>' class="text_01">
				<INPUT type='hidden' name='item_name' value='<%=item_name%>'>
			</TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">�Ǹ������ڵ�</TD>
            <TD width="35%" height="25" class="bg_04">
				<SELECT name='sale_type'>
					<option value="GE">�Ϲ�</option>
					<option value="DI">����</option>
					<option value="SI">Ư��</option>
				</SELECT>
			<%	if(!sale_type.equals("")) {
			%>	<SCRIPT>document.eForm.sale_type.value='<%=sale_type%>'</SCRIPT>
			<%	}
			%>
			</TD>
            <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">���������ڵ�</TD>
            <TD width="35%" height="25" class="bg_04">
				<SELECT name='approval_type'>
					<option value="CA">����</option>
					<option value="CK">��ǥ</option>
					<option value="BL">����</option>
				</SELECT>
			<%	if(!approval_type.equals("")) {
			%>	<SCRIPT>document.eForm.approval_type.value='<%=approval_type%>'</SCRIPT>
			<%	}
			%>
		</TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">��������</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='10' name='apply_date' value='<%=apply_date%>' class="text_01">
					<a href="Javascript:OpenCalendar('apply_date');"><img src="../bs/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">�ǸŴ���</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='6' name='sale_unit' value='<%=sale_unit%>' class="text_01">
					<a href="javascript:sel_sale_unit();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">ȭ������</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='6' name='money_type' value='<%=money_type%>'  class="text_01">
					<a href="javascript:sel_stock_unit();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">�ǸŴܰ�</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='12' maxlength='15' name='sale_unit_cost' value='<%=sale_unit_cost%>'  class="text_01"  onKeyPress="currency(this);" onKeyup="com(this);"  style="text-align:right;"></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE><BR>

<INPUT type='hidden' name='mode' value='<%=mode%>'>
<INPUT type='hidden' name='mid' value='<%=mid%>'>
</FORM>
</BODY>
</HTML>


<SCRIPT LANGUAGE=JAVASCRIPT>
<!--
function checkForm(){
	var f=document.eForm;

	if(f.item_code.value==""){
		alert("ǰ���ڵ带 �Է��Ͻʽÿ�.");
		f.item_code.focus();
		return;
	}

	if(f.item_name.value==""){
		alert("ǰ���ڵ���� �Է��Ͻʽÿ�.");
		f.item_name.focus();
		return;
	}
	
	f.item_name.value		= f.item_desc.value;
	f.sale_unit_cost.value	= unComma(f.sale_unit_cost.value);
	f.submit();
}

function searchCompany(){
	
	wopen("","search_company",'600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

// ����ó��
function del(){
	var f=document.eForm;
	
	if(confirm("���� �Ͻðڽ��ϱ�?")) 
	location.href="../servlet/SalesConfigMgrServlet?mode=delete_item_unit_cost&mid=<%=mid%>";
	else return;
}

// ǰ�� ���� ��������
function searchCMInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=request_unit";
	wopen(strUrl,"SEARCH_ITEM",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../bs/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
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

// ȭ�� ���� ��������
function sel_stock_unit()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=eForm&type=CURRENCY&div=one&code=money_type&code_field=Type����&name_field=ȭ�󱸺�&minor_code=ȭ�����&minor_field=ȭ�������";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
	
} 

// �Ǹ� ���� ��������
function sel_sale_unit()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=eForm&type=SELL_UNIT&div=one&code=sale_unit&code_field=Type����&name_field=�Ǹű���&minor_code=�ǸŴ���&minor_field=�ǸŴ�����";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
	
} 

-->
</script>