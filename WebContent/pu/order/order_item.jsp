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

	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new OrderInfoTable();
	Iterator table_iter = item_list.iterator();

	//링크 문자열 가져오기
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
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 구매발주품목등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=100%>
	<%=link_item_modify%><%=link_item_delete%><%=link_approval%><%=link_print%><%=link_app_info%><%=link_list%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주번호</td>
           <td width="37%" height="25" class="bg_04"><%=order_no%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">총발주금액</td>
           <td width="37%" height="25" class="bg_04"><%=order_total_mount%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체명</td>
           <td width="37%" height="25" class="bg_04" colspan="3"><%=supplyer_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='item_code' value='<%=item_code%>' class="text_01" readOnly><!--<a href="javascript:searchCMInfo();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a>--></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='20' name='item_name' value='<%=item_name%>' class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목설명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='35' name='item_desc' value='<%=item_desc%>'></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">희망납기일</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='delivery_date' value='<%=delivery_date%>' class="text_01" readOnly> <a href="Javascript:OpenCalendar('delivery_date');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주수량</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='order_quantity' value='<%=order_quantity%>' onKeyPress="currency(this);" onBlur="cal_order_cost();" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주단위</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='order_unit' value='<%=order_unit%>' class="text_01" readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주단가</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='unit_cost' value='<%=unit_cost%>' onKeyPress="currency(this);" onKeyup="com(this);" onBlur="cal_order_cost();" style="text-align:right;" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주금액</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='order_cost' value='<%=order_cost%>' readOnly style="text-align:right;"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>

<input type='hidden' name='requester_id' value='<%=requestor_id%>'>
<input type='hidden' name='requester_div_code' value='<%=requestor_div_code%>'>
<input type='hidden' name='request_no' value='<%=request_no%>'>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='old_quantity' value='<%=order_quantity%>'>
<input type='hidden' name='order_no' value='<%=order_no%>'>

<!-- 발주품목 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../pu/images/title_balju_item.gif' border='0' alt='발주품목'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:218; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>품목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>발주단가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>발주수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>발주단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>발주금액</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>희망납기일</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>기입고수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>처리상태</TD>
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


// 품목 정보 가져오기
function searchCMInfo(){
	var url = "../servlet/CodeMgrServlet?mode=list_item_p";	
	wopen(url,"return_noDesc",'800','500','scrollbars=yes,toolbar=no,status=no,resizable=no');
}


//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//공급업체찾기
function sel_supplyer() 
{ 
	url = "../pu/order/searchCompany.jsp?sf=reg_order&sid=supplyer_code&sname=supplyer_name";
	wopen(url,'add','600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function get_order_code(){
	url = "../pu/config/searchCode.jsp?tablename=pu_order_type&field=order_type/order_name";
	wopen(url,'purchase','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//발주정보삭제
function delete_order() 
{ 
	var f = document.reg_order;
	var order_no = f.order_no.value;

	var c = confirm("발주품목이 모두 삭제됩니다. 현재 발주건을 삭제하시겠습니까?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_order_info&order_no="+order_no;
}

//발주품목추가
function add_item() 
{ 
	var f = document.reg_order;

	if(f.item_code.value == ''){
		alert("품목코드를 찾아 선택하십시오.");
		return;
	}

	if(f.item_name.value == ''){
		alert("품목명을 입력하십시오.");
		return;
	}

	if(f.delivery_date.value == ''){
		alert("희망납기일을 찾아서 선택하십시오.");
		return;
	}

	if(f.order_quantity.value == ''){
		alert("발주수량을 입력하십시오.");
		f.order_quantity.focus();
		return;
	}

	if(f.order_unit.value == ''){
		alert("발주단위를 찾아서 선택하십시오.");
		f.order_unit.focus();
		return;
	}

	if(f.unit_cost.value == ''){
		alert("발주단가를 입력하십시오.");
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

//품목삭제
function del_item() 
{ 
	var f = document.reg_order;
	var order_no = f.order_no.value;
	var item_code  = f.item_code.value;
	var request_no = f.request_no.value;

	var c = confirm("품목번호:"+item_code+" 을(를) 삭제하시겠습니까?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_order&order_no="+order_no+"&item_code="+item_code+"&request_no="+request_no;
}

//공급업체지정시
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

//발주서출력
function go_print() {
	var f = document.reg_order;
	var order_no = f.order_no.value;
	var url = "PurchaseMgrServlet?mode=order_print&order_no="+order_no;
	wopen(url,'add','750','600','scrollbars=yes,toolbar=no,status=no,resizable=no');

}

//발주정보보기
function order_info()
{	var f = document.reg_order;
	var order_no = f.order_no.value;
	location.href = "PurchaseMgrServlet?mode=modify_order_info&order_no="+order_no;
}
/**********************
 * 세액 계산
 **********************/
function cal_vat_mount() 
{ 
	var f = document.reg_order;
	var vat_rate = f.vat_rate.value;
	var order_total_mount = unComma(f.order_total_mount.value);

	f.vat_mount.value = Comma(vat_rate * order_total_mount / 100);
}

/**********************
 * 발주금액 계산
 **********************/
function cal_order_cost() 
{ 
	var f = document.reg_order;
	var order_quantity = f.order_quantity.value;
	var unit_cost = unComma(f.unit_cost.value);

	f.order_cost.value = Comma(order_quantity * unit_cost);
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

function order_app_view(){

	var order_no = '<%=order_no%>';
	//location.href ="../servlet/PurchaseMgrServlet?mode=order_app_view&order_no="+order_no;
	var para = "mode=order_app_view&request_no="+order_no+"&request_type="; 
	location.href="../gw/approval/module/pu_OrderApp.jsp?"+para;
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 555;
	item_list.style.height = div_h;

} 
</script>