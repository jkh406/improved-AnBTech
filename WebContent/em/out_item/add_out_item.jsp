<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.em.entity.*"
%>
<%!
	ItemInfoTable item;
	EmLinkUrl link;
%>

<%
	String mode = request.getParameter("mode");
	item = (ItemInfoTable)request.getAttribute("ItemInfo");
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

	String category_code		= item.getCategoryCode();   
	String item_name			= item.getItemName();       
	String model_code			= item.getModelCode();      
	String model_name			= item.getModelName();      
	String maker_name			= item.getMakerName();      
	String standards			= item.getStandards();      
	String unit					= item.getUnit();           
	String supplyer_code		= item.getSupplyerCode();   
	String supplyer_name		= item.getSupplyerName();   
	String supply_cost			= sp.getMoneyFormat(item.getSupplyCost(),"");     
	String other_info			= item.getOtherInfo();      
	String writer				= item.getWriter();         
	String written_day			= item.getWrittenDay();
%>


<script language=JavaScript>
<!--
<%
	int i = 1;
	int enableupload = 3;

	while(i < enableupload){
		if(i == enableupload-1){
%>

		function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%> size=30>"
		}
	<%
		 break;
		}
	%>
	function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=30><font id=id<%=i+1%>></font>"
	}
<%
		i++;
	}
%>
//-->
</script>
<html>
<link rel="stylesheet" type="text/css" href="../em/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0">

<form method=post name="writeForm" action='../servlet/EstimateMgrServlet' enctype='multipart/form-data' style="margin:0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../em/images/blet.gif"> 견적품목등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5>&nbsp;</TD>
			  <TD align=left width=100%>
					<a href='javascript:checkForm();'><img src='../em/images/bt_save.gif' border='0' align='absmiddle'></a> <% if(mode.equals("modify_out_item")){ %><a href="javascript:delete_item('');"><img src='../em/images/bt_del.gif' border='0' align='absmiddle'></a> <% } %><a href="javascript:history.go(-1);"><img src='../em/images/bt_cancel.gif' border='0' align='absmiddle'></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">품목명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="item_name" size="20" value="<%=item_name%>" maxlength="30" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">품목단위</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="unit" size="5" value="<%=unit%>" maxlength="10" class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">모델코드</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="model_code" size="20" value="<%=model_code%>" maxlength="30" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">모델명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="model_name" size="20" value="<%=model_name%>" maxlength="30" class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">모델규격</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="standards" size="35" value="<%=standards%>" maxlength="40"></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">제조사명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="maker_name" size="20" value="<%=maker_name%>" maxlength="30" class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">공급업체코드</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type='text' size='12' name='supplyer_code' value='<%=supplyer_code%>' class="text_01" readOnly> <input type='text' size='20' name='supplyer_name' value='<%=supplyer_name%>' class="text_01" readOnly> <a href="javascript:sel_supplyer();"><img src="../em/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">공급단가</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type='text' size='12' name='supply_cost' value='<%=supply_cost%>' class="text_01" maxlength="15" style="text-align:right;" onKeyPress="currency(this);" onKeyup="com(this);">원</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">관련문서</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
				<%
					if (enableupload > 0){
						if(i < enableupload){
				%>
				            <input type=file name=attachfile<%=i%> onClick='fileadd_action<%=i%>()' size="30">
				            <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <input type=file name=attachfile<%=i%> size="50">
				            <font id=id<%=i%>></font>
				<%
						}
					}
				%>					   
		   </td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
 	   </tbody></table>
<input type="hidden" name="mode" value="<%=mode%>">
</form>
</body>
</html>

<script language="javascript">

//필수 입력사항 체크
function checkForm(){ 
	
	var f = document.writeForm;

	if(f.item_name.value == ''){
		alert("품목명을 입력하십시오.");
		f.item_name.focus();
		return;
	}

	if(f.unit.value == ''){
		alert("품목단위를 입력하십시오.");
		f.unit.focus();
		return;
	}

	if(f.model_code.value == ''){
		alert("모델코드를 입력하십시오.");
		f.model_code.focus();
		return;
	}

	if(f.model_code.value.indexOf(' ') != -1){
		alert("모델코드는 공백을 포함할 수 없습니다.");
		f.model_code.focus();
		return;
	}

	if(f.model_name.value == ''){
		alert("모델명을 입력하십시오.");
		f.model_name.focus();
		return;
	}

	if(f.maker_name.value == ''){
		alert("제조회사명을 입력하십시오.");
		f.maker_name.focus();
		return;
	}

	if(f.supplyer_code.value == ''){
		alert("공급업체를 찾아서 선택하십시오.");
		f.supplyer_code.focus();
		return;
	}

	if(f.supply_cost.value == '0' || f.supply_cost.value == ''){
		alert("공급단가를 입력하십시오.");
		f.supply_cost.focus();
		return;
	}

	f.supply_cost.value = unComma(f.supply_cost.value);

	document.onmousedown=dbclick;
	f.submit();
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}

function delete_item(item_no) {
	var delete_confirm = confirm("정말로 삭제하시겠습니까? 품목삭제시 품목공급정보도 함께 삭제됩니다.");
	if(delete_confirm) location.href = "../servlet/EstimateMgrServlet?mode=delete_out_item&item_no="+item_no;
	else return;
}

//공급업체찾기
function sel_supplyer() 
{ 
	url = "../crm/company/searchCompany.jsp?sf=writeForm&sid=supplyer_code&sname=supplyer_name";
	wopen(url,'SEARCH_COMP','600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function myRound(num, pos) { 
	var posV = Math.pow(10, (pos ? pos : 2))
	return Math.round(num*posV)/posV
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