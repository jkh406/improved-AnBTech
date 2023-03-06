<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.bs.entity.*"
%>
<%
	ItemPremiumTable table = new ItemPremiumTable();
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	table = (ItemPremiumTable)request.getAttribute("ITEM_PREMIUM");

	String mode					= request.getParameter("mode");
	String title				= "품목할증정보 등록";
	if(mode.equals("modify_item_premium")) title = "품목할증정보 수정";	
	String mid					= table.getMid();
	String item_code			= table.getItemCode();     
	String item_name			= table.getItemName();     
	String approval_type		= table.getApprovalType(); 
	String apply_date			= table.getApplyDate();    
	String sale_unit			= table.getSaleUnit();     
	String premium_type			= table.getPremiumType();    
	String premium_name			= table.getPremiumName();    
	String premium_standard_quantity	= table.getPremiumStandardQuantity();
	String premium_value		= table.getPremiumValue();

	String readonly				= "";
	if(mode.equals("modify_item_premium")) readonly = "readonly";	
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../bs/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>

<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" action="SalesConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../bs/images/blet.gif"> <%=title%></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
						
						<IMG src='../bs/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
						<%if(mode.equals("modify_item_premium")) {%>
						<IMG src='../bs/images/bt_del.gif' onClick='javascript:del();' style='cursor:hand' align='absmiddle'>
						<%}%>
						<IMG src='../bs/images/bt_cancel.gif' onClick='javascript:history.back();' style='cursor:hand' align='absmiddle'></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">품목코드</TD>
            <TD width="85%" height="25" class="bg_04" colspan='3'>
				<INPUT type='text' size='12' maxlength='14' name='item_code' value='<%=item_code%>' <%=readonly%> class="text_01" maxlength="2">
				<%if(!mode.equals("modify_item_premium")){%>
				<a href="javascript:searchCMInfo();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle">
				<%}%>
			</TD>
        </TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR> <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">품목명</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='30' name='item_desc' value='<%=item_name%>' class="text_01">
				<INPUT type='hidden' name='item_name' value='<%=item_name%>'>
			</TD>
            <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">결재유형코드</TD>
            <TD width="35%" height="25" class="bg_04">
				<SELECT name='approval_type'>
					<option value="CA">현금</option>
					<option value="CK">수표</option>
					<option value="BL">어음</option>
				</SELECT>
			<%	if(!approval_type.equals("")) {
			%>	<SCRIPT>document.eForm.approval_type.value='<%=approval_type%>'</SCRIPT>
			<%	}
			%>
		</TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">적용일자</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='10' name='apply_date' value='<%=apply_date%>' class="text_01" readonly>
					<a href="Javascript:OpenCalendar('apply_date');"><img src="../bs/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">판매단위</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='6' name='sale_unit' value='<%=sale_unit%>' class="text_01">
					<a href="javascript:sel_sale_unit();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">할증유형</TD>
           <TD width="35%" height="25" class="bg_04">				
				<SELECT name='premium_type' onChange="javascript:change(this)">
					<option value="01">백분률</option>
					<option value="02">수량</option>
				</SELECT>
				<%	if(!premium_type.equals("")) {
				%>	<SCRIPT>document.eForm.premium_type.value='<%=premium_type%>'</SCRIPT>
				<%	}
				%>
		   </TD>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">할증유형명</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='12' maxlength='15' name='premium_name' value='<%=premium_name%>'  class="text_01"></TD></TR>
		 <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		 <TR>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">할증적용기준수량</TD>
           <TD width="35%" height="25" class="bg_04">
		   		<INPUT type='text' size='6' name='premium_standard_quantity' value='<%=premium_standard_quantity%>'  class="text_01" onKeyPress="currency(this);" style="text-align:right;"></TD>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">할증값</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='6' maxlength='15' name='premium_value' value='<%=premium_value%>'  class="text_01"  onKeyPress="currency(this);" style="text-align:right;">
			</TD></TR>
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
		alert("품목코드를 입력하십시요.");
		f.item_code.focus();
		return;
	}

	if(f.item_name.value==""){
		alert("품목코드명을 입력하십시요.");
		f.item_name.focus();
		return;
	}
	
	f.item_name.value		= f.item_desc.value;
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

// 삭제처리
function del(){
	var f=document.eForm;
	
	if(confirm("삭제 하시겠습니까?")) 
	location.href="../servlet/SalesConfigMgrServlet?mode=delete_item_premium&mid=<%=mid%>";
	else return;
}

// 품목 정보 가져오기
function searchCMInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=request_unit";
	wopen(strUrl,"SEARCH_ITEM",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../bs/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
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

// 화폐 단위 가져오기
function sel_stock_unit()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=eForm&type=CURRENCY&div=one&code=money_type&code_field=Type구분&name_field=화폐구분&minor_code=화폐단위&minor_field=화폐단위명";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
	
} 

// 판매 단위 가져오기
function sel_sale_unit()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=eForm&type=SELL_UNIT&div=one&code=sale_unit&code_field=Type구분&name_field=판매구분&minor_code=판매단위&minor_field=판매단위명";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
	
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

// 할증유형에 따라 field setting
function change(obj){
	if(obj.value=="01") {
		//alert("할증값은 백분률(%) 수치를 넣어주십시요");
		document.eForm.premium_standard_quantity.value="0";
		document.eForm.premium_standard_quantity.readonly = false;	
	}
}
-->
</script>