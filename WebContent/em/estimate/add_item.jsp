<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.em.entity.*"
%>
<%!
	ItemInfoTable item;
	EstimateInfoTable estimate;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>

<%
	String mode = request.getParameter("mode");
	item = (ItemInfoTable)request.getAttribute("ItemInfo");
%>
<HTML><HEAD><TITLE>����ǰ���Է�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../em/css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../em/images/pop_add_item2.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

<form method="post" name="writeForm" action="../servlet/EstimateMgrServlet" enctype="multipart/form-data" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="98%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">ǰ���</td>
           <td width="83%" height="25" class="bg_04" colspan="3"><input type="text" name="item_name" value="<%=item.getItemName()%>" size="15" class="text_01"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">���ڵ�</td>
           <td width="83%" height="25" class="bg_04" colspan="3"><input type='text' size='15' name='model_code' value='<%=item.getModelCode()%>' class="text_01" readOnly> <a href="javascript:searchModelCode('<%=item.getItemClass()%>');"><img src="../bm/images/bt_search.gif" border="0" align='absbottom'></a></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">�𵨸�</td>
           <td width="83%" height="25" class="bg_04" colspan="3"><input type="text" name="model_name" value="<%=item.getModelName()%>" size="20" class="text_01"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">�𵨱԰�</td>
           <td width="83%" height="25" class="bg_04" colspan="3"><input type="text" name="standards" value="<%=item.getStandards()%>" size="40" class="text_01"></td></tr>
<% if(item.getItemClass().equals("2")){ %>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">����ȸ���</td>
           <td width="83%" height="25" class="bg_04" colspan="3"><input type="text" name="maker_name" value="<%=item.getMakerName()%>" size="20"></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">���޾�ü�ڵ�</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="supplyer_code" value="<%=item.getSupplyerCode()%>" size="15" readOnly></td>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">���޾�ü��</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="supplyer_name" value="<%=item.getSupplyerName()%>" size="15"></td></tr>
<%	}	%>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="98%" border=0>
	   <tbody>
         <tr><td height=5 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif"><% if(item.getItemClass().equals("1")) out.print("���߿���"); else out.print("���ſ���"); %></td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="buying_cost" value="<%=sp.getMoneyFormat(item.getBuyingCost(),"")%>" size="15" style="text-align:right;" onKeyPress="currency(this);" onKeyup="com(this);" onBlur="calEstimateValue();" maxlength="15" class="text_01"></td>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">�����ݾ�</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="estimate_value" value="<%=sp.getMoneyFormat(item.getEstimateValue(),"")%>" size="15" style="text-align:right;" readOnly></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">������</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="gains_percent" value="<%=item.getGainsPercent()%>" size="3" onBlur="calEstimateValue();" onKeyPress="currency(this);" maxlength="3" class="text_01"> %</td>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">���ͱݾ�</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="gains_value" value="<%=sp.getMoneyFormat(item.getGainsValue(),"")%>" size="15" style="text-align:right;" readOnly></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">��������</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="quantity" value="<%=item.getQuantity()%>" size="5" onBlur="calEstimateValue();" onKeyPress="currency(this);" maxlength="3" class="text_01"> <input type="text" name="unit" value="<%=item.getUnit()%>" size="5" class="text_01" maxlength="10"></td>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">���޿���</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="supply_cost" value="<%=sp.getMoneyFormat(item.getSupplyCost(),"")%>" size="15" style="text-align:right;" readOnly></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">����</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="tax_percent" value="<%=item.getTaxPercent()%>" size="3" onBlur="calEstimateValue();" onKeyPress="currency(this);" maxlength="3" class="text_01"> %</td>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">����</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="tax_value" value="<%=sp.getMoneyFormat(item.getTaxValue(),"")%>" size="15" style="text-align:right;" readOnly></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">������</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="discount_percent" value="<%=item.getDiscountPercent()%>" size="3" onBlur="calEstimateValue();" onKeyPress="currency(this);" maxlength="3" class="text_01"> %</td>
           <td width="17%" height="25" class="bg_03" background="../em/images/bg-01.gif">���αݾ�</td>
           <td width="33%" height="25" class="bg_04"><input type="text" name="discount_value" value="<%=sp.getMoneyFormat(item.getDiscountValue(),"")%>" size="15" style="text-align:right;" readOnly></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

<input type="hidden" name="mode" value="<%=mode%>">
<input type="hidden" name="no" value="<%=item.getMid()%>">
<input type="hidden" name="estimate_no" value="<%=item.getEstimateNo()%>">
<input type="hidden" name="ver" value="<%=item.getVersion()%>">
<input type="hidden" name="item_class" value="<%=item.getItemClass()%>">
<% if(item.getItemClass().equals("1")){ %>
<input type="hidden" name="maker_name" value="�����̽���ũ(��)">
<input type="hidden" name="supplyer_code" value="126-81-55555">
<input type="hidden" name="supplyer_name" value="�����̽���ũ(��)">
<% } %>
</form>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:javascript:checkForm()'><img src='../em/images/bt_save.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close()'><img src='../em/images/bt_cancel.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>


<SCRIPT LANGUAGE="JavaScript"> 
<!-- 
//�ʼ� �Է»��� üũ
function checkForm(){ 
	var f = document.writeForm;

	if(f.item_name.value == ''){
		alert("ǰ����� �Է��Ͻʽÿ�.");
		f.item_name.focus();
		return;
	}

	if(f.model_code.value == ''){
		date = new Date();
		f.model_code.value = date.getTime();
	}

	if(f.model_name.value == ''){
		alert("�𵨸��� �Է��Ͻʽÿ�.");
		f.model_name.focus();
		return;
	}

	if(f.buying_cost.value == '' || f.buying_cost.value == '0'){
		alert("���߿����� �Է��Ͻʽÿ�.");
		f.buying_cost.focus();
		return;
	}

	if(f.gains_percent.value == '' || f.gains_percent.value == '0'){
		alert("�������� �Է��Ͻʽÿ�.");
		f.gains_percent.focus();
		return;
	}

	if(f.unit.value == ''){
		alert("ǰ������� �Է��Ͻʽÿ�.");
		f.unit.focus();
		return;
	}

	f.buying_cost.value		= unComma(f.buying_cost.value);		//����(����)����
	f.supply_cost.value		= unComma(f.supply_cost.value);		//���޴ܰ�
	f.tax_value.value		= unComma(f.tax_value.value);		//����
	f.discount_value.value	= unComma(f.discount_value.value);	//���ξ�
	f.gains_value.value		= unComma(f.gains_value.value);		//���ͱݾ�
	f.estimate_value.value	= unComma(f.estimate_value.value);	//�����ݾ�

	f.submit();
}


/**********************
 * ���ͱݾ� ���
 **********************/
function calGainsValue() 
{ 
	var f = document.writeForm;
	var buying_cost = unComma(f.buying_cost.value);
	var gains_percent = f.gains_percent.value;

	//���ͱݾ� =  ���߿���*������/100
	f.gains_value.value = Comma(Math.round(buying_cost * gains_percent / 100));
}

/**********************
 * ���޿��� ��� 
 **********************/
function calSupplyCost() 
{ 
	var f = document.writeForm;
	var buying_cost = unComma(f.buying_cost.value);
	var gains_value = unComma(f.gains_value.value);

	//���޿��� =  ���߿��� + ���ͱݾ�
	f.supply_cost.value = Comma(buying_cost + gains_value);
} 

/**********************
 * ���ξ� ��� 
 **********************/
function calDiscountValue() 
{ 
	var f = document.writeForm;
	var supply_cost			= unComma(f.supply_cost.value);
	var quantity			= f.quantity.value;
	var discount_percent	= f.discount_percent.value;

	//���ξ� = ���޿���*���޼���*������/100
	f.discount_value.value = Comma(Math.round((supply_cost * quantity) * discount_percent / 100));
}

/**********************
 * ���� ��� 
 **********************/
function calTaxValue() 
{ 
	var f = document.writeForm;
	var supply_cost		= unComma(f.supply_cost.value);
	var quantity		= f.quantity.value;
	var discount_value	= unComma(f.discount_value.value);
	var tax_percent		= f.tax_percent.value;

	//���� =  (���޿���*���޼��� - ���ξ�)*����/100
	f.tax_value.value = Comma(Math.round((supply_cost * quantity - discount_value) * tax_percent / 100));
}


/**********************
 * ������ �ڵ� ��� 
 **********************/
function calEstimateValue() 
{ 
	var f = document.writeForm;
	calGainsValue();	//���ͱݾװ��

	calSupplyCost();	//���޿������
	calDiscountValue();	//���αݾװ��
	calTaxValue();		//���װ��

	var supply_cost		= unComma(f.supply_cost.value);
	var quantity		= f.quantity.value;
	var discount_value	= unComma(f.discount_value.value);
	var tax_value		= unComma(f.tax_value.value);

	//���������� = ���޿���*���޼��� - ���ξ� + ����
	f.estimate_value.value = Comma(supply_cost * quantity - discount_value + tax_value);

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

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//���ڵ� �˻��ϱ�
function searchModelCode(item_class){
	var url = "";
	if(item_class == 1){
		url = "../gm/openModelInfoWindow.jsp?fname=writeForm&one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=item_name&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
		wopen(url,"SEARCH_MODEL",'800','400','scrollbars=yes,toolbar=no,status=no,resizable=no');
	}else{
		url = "../servlet/EstimateMgrServlet?mode=search_out_item";	wopen(url,"SEARCH_MODEL",'800','450','scrollbars=yes,toolbar=no,status=no,resizable=no');
	}
}
//-->
</SCRIPT>