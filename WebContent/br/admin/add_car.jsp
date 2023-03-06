<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkBR01.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.br.entity.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	CarInfoTable table;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>

<%
	String mode = request.getParameter("mode");
	String cid	= request.getParameter("cid");

	table = new CarInfoTable();
	table = (CarInfoTable)request.getAttribute("CarInfo");

	String car_type			= table.getCarType()==null?"":table.getCarType();
	String car_no			= table.getCarNo()==null?"":table.getCarNo();
	String model_name		= table.getModelName()==null?"":table.getModelName();
	String produce_year		= table.getProduceYear()==null?"":table.getProduceYear();
	String buy_date			= table.getBuyDate()==null?"":table.getBuyDate();
	String price			= table.getPrice()==null?"":sp.getMoneyFormat(table.getPrice(),"");
	String fuel_type		= table.getFuelType()==null?"":table.getFuelType();
	String fuel_efficiency	= table.getFuelEfficiency()==null?"":table.getFuelEfficiency();
	String car_id			= table.getCarId()==null?"":table.getCarId();
	String maker_company	= table.getMakerCompany()==null?"":table.getMakerCompany();
	String stat				= table.getStat()==null?"":table.getStat();

	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String reg_date 		= table.getRegDate()==null?vans.format(now):table.getRegDate();
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../br/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../br/images/blet.gif"> 차량정보등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href="javascript:checkForm();"><img src="../br/images/bt_save.gif" border="0" align="absmiddle"></a> <a href="javascript:history.go(-1);"><img src="../br/images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<form method=post name="writeForm" action='../servlet/BookResourceServlet' enctype='multipart/form-data' style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--차량정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차종</td>
           <td width="37%" height="25" class="bg_04">
					<select name='car_type' class="text_01">
						<option value=''>차종 선택</option>
						<option value='승용'>승용</option>
						<option value='승합'>승합</option>
						<option value='트럭'>트럭</option>
					</select>
<%	if(car_type != null){	%>
		<script language='javascript'>
			document.writeForm.car_type.value = '<%=car_type%>';
		</script>
<%	}	%>			   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name="car_no" size='15' value='<%=car_no%>' maxlength="15"  class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">모델명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name="model_name" size='20' value='<%=model_name%>' maxlength="20"  class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">년식</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name="produce_year" size='4' value='<%=produce_year%>' maxlength="4">년식</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">구입일자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name="buy_date" size='10' maxlength="10" value='<%=buy_date%>' readOnly> <a href="Javascript:OpenCalendar('buy_date');"><img src="../br/images/bt_calendar.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">구입가</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name="price" size='12' style="text-align:right;" onKeyPress="currency(this);" onKeyup="com(this);" value='<%=price%>'>원</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">연료구분</td>
           <td width="37%" height="25" class="bg_04">
					<select name='fuel_type'>
						<option value='휘발유'>휘발유</option>
						<option value='경유'>경유</option>
						<option value='LPG'>LPG</option>
					</select>				
<%	if(fuel_type != null && !fuel_type.equals("")){	%>
		<script language='javascript'>
			document.writeForm.fuel_type.value = '<%=fuel_type%>';
		</script>
<%	}	%>			   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">연비</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name="fuel_efficiency" size='5' value='<%=fuel_efficiency%>' maxlength="5">km/l</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량관리번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name="car_id" size='10' value='<%=car_id%>' maxlength="10"></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">제조회사</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name="maker_company" size='15' value='<%=maker_company%>' maxlength="20"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량현상태</td>
           <td width="37%" height="25" class="bg_04">
					<select name='stat'>
						<option value='1'>정상(사용가)</option>
						<option value='5'>수리중</option>
						<option value='6'>폐차처리</option>
					</select>								
<%	if(stat != null && !stat.equals("")){	%>
		<script language='javascript'>
			document.writeForm.stat.value = '<%=stat%>';
		</script>
<%	}	%>			   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">등록일자</td>
           <td width="37%" height="25" class="bg_04"><%=reg_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
</td></tr></table>
  <input type='hidden' name='cid' value='<%=cid%>'>
  <input type='hidden' name='mode' value='<%=mode%>'>
  <input type='hidden' name='category' value='car'>
</form>
</body></html>



<script language="JavaScript">
<!--
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

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../br/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function checkForm() 
{ 
	var f = document.writeForm;

	if(f.car_type.value == ''){
		alert("차종을 선택하십시오.");
		f.car_type.focus();
		return;
	}

	if(f.car_no.value == ''){
		alert("차량번호을 입력하십시오.");
		f.car_no.focus();
		return;
	}

	if(f.model_name.value == ''){
		alert("차량 모델명을 입력하십시오.");
		f.model_name.focus();
		return;
	}
	f.price.value	= unComma(f.price.value);
	f.submit();
}

//-->
</script>