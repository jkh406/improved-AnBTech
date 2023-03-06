<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		="java.util.*,com.anbtech.st.entity.*"
%>
<%
	String mode = request.getParameter("mode");

	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	StockInfoTable table = new StockInfoTable();
	table = (StockInfoTable)request.getAttribute("ITEM_UNIT_COST");

	String item_type	= table.getItemType();
	String item_code	= table.getItemCode();
	String item_name	= table.getItemName();
	String item_desc	= table.getItemDesc();
	String unit_cost_a	= sp.getMoneyFormat(table.getUnitCostA(),"");
	String unit_cost_c	= sp.getMoneyFormat(table.getUnitCostC(),"");
	String unit_cost_s	= sp.getMoneyFormat(table.getUnitCostS(),"");
	String last_updated_date = table.getLastUpdatedDate();
%>

<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0">

<form name="writeForm" method="post" action="PurchaseConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 품목단가등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../pu/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
					<IMG src='../pu/images/bt_list.gif' onClick="javascript:location.href='../servlet/PurchaseConfigMgrServlet?mode=list_item_cost';" style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='item_code' value='<%=item_code%>' class="text_01" readOnly> <%if(mode.equals("write_unit_cost")){%><a href="javascript:searchCMInfo();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a><%}%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='20' name='item_name' value='<%=item_name%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목설명</td>
           <td width="37%" height="25" class="bg_04" colspan="3"><input type='text' size='40' name='item_desc' value='<%=item_desc%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">평균가</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='unit_cost_a' value='<%=unit_cost_a%>' onKeyPress="currency(this);" onKeyup="com(this);" onBlur="com(this);"; style="text-align:right;" readOnly onClick="javascript:alert('평균가는 임의로 입력할 수 없습니다.(구매입고시 자동 등록 및 갱신됩니다.)');" maxlength="10">원</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">표준가</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='unit_cost_s' value='<%=unit_cost_s%>' maxlength="10" onKeyPress="currency(this);" onKeyup="com(this);" onBlur="com(this);"; style="text-align:right;" class="text_01">원</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">최종갱신일</td>
           <td width="37%" height="25" class="bg_04" colspan="3"><%=last_updated_date%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>


	   </tbody></table><br>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='item_type' value='<%=item_type%>'>
<input type='hidden' name='unit_cost_c' value='<%=unit_cost_c%>'>
<input type='hidden' name='last_updated_date' value=''>
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
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=request_unit";
	wopen(strUrl,"SEARCH_ITEM",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function checkForm() 
{ 
	// 오늘일자 가져오기
	var today = new Date();
    var month = today.getMonth()+1;
	var day   = today.getDate();
		if(month<10) month = "0" + month;
		if(day<10)   day   = "0" + day;
	var update_day = today.getYear()+"-"+month+"-"+day;

	var f = document.writeForm;

	if(f.item_code.value == ''){
		alert("품목코드를 찾아 선택하십시오.");
		return;
	}

	if(f.unit_cost_a.value == ''){
		f.unit_cost_a.value = '0';
	}

	if(f.unit_cost_s.value == ''){
		f.unit_cost_s.value = '0';
	}
	
	if(confirm("품목단가 정보를 갱신하시겠습니까?")){
		
		f.last_updated_date.value=update_day;
		f.unit_cost_a.value	= unComma(f.unit_cost_a.value);
		f.unit_cost_s.value	= unComma(f.unit_cost_s.value);

		f.submit();
	
	} else {
		return;
	}
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
</script>