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
	String mode = request.getParameter("mode");	// 모드

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

	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new EnterInfoTable();
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
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 입고품목등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
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
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='enter_no' value='<%=enter_no%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고일자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='enter_date' value='<%=enter_date%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">첨부문서</td>
           <td width="37%" height="25" class="bg_04"><%=filelink%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">매입총액</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='enter_total_mount' value='<%=enter_total_mount%>' style="text-align:right;" readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='item_code' value='<%=item_code%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='20' name='item_name' value='<%=item_name%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고수량</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='enter_quantity' value='<%=enter_quantity%>' onKeyPress="currency(this);" onBlur="cal_enter_cost();"></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고단위</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='enter_unit' value='<%=enter_unit%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고단가</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='unit_cost' value='<%=unit_cost%>' onKeyPress="currency(this);" onKeyup="com(this);" onBlur="cal_enter_cost();" style="text-align:right;"></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">매입금액</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='enter_cost' value='<%=enter_cost%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고공장</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type='text' size='5' name='factory_code' value='<%=factory_code%>' readOnly> <input type='text' size='20' name='factory_name' value='<%=factory_name%>' readOnly><!-- <a href="javascript:sel_factory();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a>--></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>


<!-- 입고품목 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../pu/images/title_input_item.gif' border='0' alt='입고품목'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:168; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>품목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>입고수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>입고단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>입고단가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>매입금액</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>처리상태</TD>
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


// 품목 정보 가져오기
function searchCMInfo(){
	var url = "../servlet/CodeMgrServlet?mode=list_item_p";	
	wopen(url,"SEARCH_ITEM",'800','500','scrollbars=yes,toolbar=no,status=no,resizable=no');
}


//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//입고품목 추가 및 수정
function add_item() 
{ 
	var f = document.reg_enter;

	if(f.item_code.value == ''){
		alert("품목코드를 찾아 선택하십시오.");
		return;
	}

	if(f.item_name.value == ''){
		alert("품목명을 기입하십시요.");
		return;
	}

	if(f.enter_quantity.value == ''){
		alert("입고수량을 입력하십시오.");
		f.enter_quantity.focus();
		return;
	}

	if(f.enter_unit.value == ''){
		alert("입고단위를 선택하십시오.");
		return;
	}

	if(f.unit_cost.value == ''){
		alert("입고단가를 입력하십시오.");
		f.unit_cost.focus();
		return;
	}

	f.enter_total_mount.value = unComma(f.enter_total_mount.value);
	f.unit_cost.value	= unComma(f.unit_cost.value);
	f.enter_cost.value	= unComma(f.enter_cost.value);

	f.submit();
}

/**********************
 * 매입금액 계산
 **********************/
function cal_enter_cost() 
{ 
	var f = document.reg_enter;
	var enter_quantity = f.enter_quantity.value;
	var unit_cost = unComma(f.unit_cost.value);

	f.enter_cost.value = Comma(enter_quantity * unit_cost);
}

//품목삭제
function del_item() 
{ 
	var f = document.reg_enter;
	var enter_no = f.enter_no.value;
	var item_code  = f.item_code.value;
	var request_no = f.request_no.value;
	var order_no = f.order_no.value;

	var c = confirm("품목번호:"+item_code+" 을(를) 삭제하시겠습니까?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_enter&enter_no="+enter_no+"&order_no="+order_no+"&request_no="+request_no+"&item_code="+item_code;
}

//결재상신
function go_approval() {
	var f = document.reg_enter;
	var enter_no = f.enter_no.value;
	var para = "mode=enter_app_view&enter_no="+enter_no;
	var c = confirm("입고번호:"+enter_no+" 에 대해 전자결재를 진행하시겠습니까?");
	if(c) location.href="../gw/approval/module/pu_WarehousingApp.jsp?"+para;
}

//입고등록정보보기
function enter_info()
{	var f = document.reg_enter;
	var enter_no = f.enter_no.value;
	location.href = "PurchaseMgrServlet?mode=modify_enter_info&enter_no="+enter_no;
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

// 공장 선택
function sel_factory(){
	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&field=factory_code/factory_name";
	wopen(url,'enterCode','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

// 창고 선택
function sel_warehouse(){
	var f=document.reg_enter;
	// #1.공장 정보 입력후 창고선택이 가능하게끔 처리.
	// #2.공장 산하의 창고 선택하여 입력.
	if(f.factory_code.value=="" || f.factory_name.value=="") {
		alert("공장을 먼저 선택해 주십시요.");
		return;
	} else {
		url = "../st/config/searchWarehouseInfo.jsp?tablename=warehouse_info_table&field=warehouse_code/warehouse_name&factory_code="+f.factory_code.value;
		wopen(url,'enterCode','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
	}
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 555;
	item_list.style.height = div_h;

} 
</script>