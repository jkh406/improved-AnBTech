<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	ItemSupplyInfoTable table = new ItemSupplyInfoTable();
	table = (ItemSupplyInfoTable)request.getAttribute("ITEM_SUPPLY_INFO");

	String mode = request.getParameter("mode");

	String mid					= table.getMid();
	String item_code			= table.getItemCode();
	String supplyer_code		= table.getSupplyerCode();
	String order_weight			= table.getOrderWeight();
	String lead_time			= table.getLeadTime();
	String is_trade_now			= table.getIsTradeNow();
	String is_main_supplyer		= table.getIsMainSupplyer();
	String min_order_quantity	= table.getMinOrderQuantity();
	String max_order_quantity	= table.getMaxOrderQuantity();
	if(min_order_quantity!=null && !min_order_quantity.equals(""))
		min_order_quantity  = sp.getMoneyFormat(min_order_quantity,"");
	if(max_order_quantity!=null && !max_order_quantity.equals(""))
		max_order_quantity  = sp.getMoneyFormat(max_order_quantity,"");
	String order_unit			= table.getOrderUnit();
	String supplyer_item_code	= table.getSupplyerItemCode();
	String supplyer_item_name	= table.getSupplyerItemName();
	String supplyer_item_desc	= table.getSupplyerItemDesc();
	String maker_name			= table.getMakerName();
	String supply_unit_cost		= table.getSupplyUnitCost();
	if(supply_unit_cost!=null && !supply_unit_cost.equals(""))
		supply_unit_cost    = sp.getMoneyFormat(supply_unit_cost,"");
	String request_unit_cost	= table.getRequestUnitCost();
	if(request_unit_cost!=null && !request_unit_cost.equals(""))
			request_unit_cost    = sp.getMoneyFormat(request_unit_cost,"");
	String supplyer_name		= table.getSupplyerName();
	String item_desc			= table.getItemDesc();
	String item_name			= table.getItemName();

	String caption ="품목공급업체등록";
	if(mode.equals("modify_item_supply_info")) caption="품목공급업체정보수정";
%>
<HTML>
<LINK rel="stylesheet" type="text/css" href="../pu/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>

<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" action="PurchaseConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> <%=caption%></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../pu/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
					<IMG src='../pu/images/bt_cancel.gif' onClick='javascript:history.back();' style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></td></tr>
		 <TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목번호</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='12' name='item_code' value='<%=item_code%>' class="text_01" readonly> <a href="javascript:searchCMInfo();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목명</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='20' name='item_name' value='<%=item_name%>' readonly></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		 <TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목설명</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='35' name='item_desc' value='<%=item_desc%>' readonly></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목단위</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='5' name='order_unit' value='<%=order_unit%>' readOnly></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체코드</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='15' name='supplyer_code' value='<%=supplyer_code%>' readonly class="text_01"> <a href="javascript:sel_supplyer();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체명</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='30' name='supplyer_name' value='<%=supplyer_name%>' readonly></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주배정가중치</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='3' name='order_weight' value='<%=order_weight%>' onKeyPress="currency(this);" maxlength="3" class="text_01">%</td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매L/T</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='3' name='lead_time' value='<%=lead_time%>' maxlength="3" class="text_01">일</td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">사용여부</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type="radio" value="y" <%if(is_trade_now.equals("y")) out.print("checked"); %> name="is_trade_now">예 <INPUT type="radio" value="n" <%if(is_trade_now.equals("n")) out.print("checked"); %> name="is_trade_now">아니오</td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">주공급처여부</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type="radio" value="y" <%if(is_main_supplyer.equals("y")) out.print("checked"); %> name="is_main_supplyer">예 <INPUT type="radio" value="n" name="is_main_supplyer" <%if(is_main_supplyer.equals("n")) out.print("checked"); %>>아니오</td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">최소발주량</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='6' name='min_order_quantity' value='<%=min_order_quantity%>'  onKeyPress="currency(this);" onKeyup="com(this);" style="text-align:right;" maxlength="5"></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">최대발주량</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='6' name='max_order_quantity' value='<%=max_order_quantity%>'  onKeyPress="currency(this);" onKeyup="com(this);" style="text-align:right;" maxlength="5"></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주단가</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='10' name='supply_unit_cost' value='<%=supply_unit_cost%>' onKeyPress="currency(this);" onKeyup="com(this);" style="text-align:right;" maxlength="12" class="text_01">원</td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매요청단가</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='10' name='request_unit_cost' value='<%=request_unit_cost%>' onKeyPress="currency(this);" onKeyup="com(this);" style="text-align:right;" maxlength="12" class="text_01">원</td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr></tbody></table><br>
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체품목코드</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='15' name='supplyer_item_code' value='<%=supplyer_item_code%>'></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체품목명</td>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='20' name='supplyer_item_name' value='<%=supplyer_item_name%>'></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목규격</td>
           <TD width="87%" height="25" class="bg_04" colspan="3"><INPUT type='text' size='30' name='supplyer_item_desc' value='<%=supplyer_item_desc%>'></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>

	   </tbody></table><br>
<INPUT type='hidden' name='mode' value='<%=mode%>'>
<INPUT type='hidden' name='mid' value='<%=mid%>'>
</form>
</body>
</html>


<script language=javascript>

//공급업체찾기
function sel_supplyer() 
{ 
	url = "../pu/order/searchCompany.jsp?sf=eForm&sid=supplyer_code&sname=supplyer_name";
	wopen(url,'add','600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

// 품목 정보 가져오기
function searchCMInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=order_unit";
	wopen(strUrl,"open_calnedar",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 저장하기
function checkForm(){
	var f = document.eForm;

	if (f.item_code.value==''){
		alert("품목번호를 찾아서 선택하십시오.");
		f.item_code.focus();
		return;
	}

	if (f.supplyer_code.value==''){
		alert("품목공급처를 찾아서 선택하십시오.");
		f.supplyer_code.focus();
		return;
	}

	if (f.order_weight.value==''){
		alert("발주배정 가중치를 입력하십시오.(1~100 사이)");
		f.order_weight.focus();
		return;
	}

	if (f.lead_time.value==''){
		alert("구매L/T(Lead Time)을 입력하십시오.");
		f.lead_time.focus();
		return;
	}

	if (f.supply_unit_cost.value=='' || f.supply_unit_cost.value=='0'){
		alert("발주단가를 입력하십시오.");
		f.supply_unit_cost.focus();
		return;
	}

	f.min_order_quantity.value = unComma(f.min_order_quantity.value);
	f.max_order_quantity.value = unComma(f.max_order_quantity.value);
	f.supply_unit_cost.value = unComma(f.supply_unit_cost.value);
	f.request_unit_cost.value = unComma(f.request_unit_cost.value);
	
	document.eForm.submit()
}


/**********************
 * 숫자만 입력되게
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
 * 천단위 콤마 삽입
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
 * 숫자에서 Comma 제거
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

// WINDOW format
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
</script>