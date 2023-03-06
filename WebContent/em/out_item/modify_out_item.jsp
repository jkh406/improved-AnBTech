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
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	
	//품목및공급정보
	item = (ItemInfoTable)request.getAttribute("ItemInfo");

	String item_name			= item.getItemName();       
	String model_code			= item.getModelCode();      
	String model_name			= item.getModelName();      
	String maker_name			= item.getMakerName();      
	String standards			= item.getStandards();      
	String unit					= item.getUnit();           
	String writer				= item.getWriter();         
	String written_day			= item.getWrittenDay();

	//공급처리스트
	ArrayList list = new ArrayList();
	list = (ArrayList)request.getAttribute("SupplyerList");
	Iterator iter = list.iterator();
%>

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
			  <TD valign='middle' class="title"><img src="../em/images/blet.gif"> 품목정보수정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5>&nbsp;</TD>
			  <TD align=left width=100%>
					<a href="javascript:checkForm();"><img src='../em/images/bt_save.gif' border='0' align='absmiddle' alt="저장"></a> <a href="javascript:history.go(-1);"><img src='../em/images/bt_cancel.gif' border='0' align='absmiddle' alt="취소"></a>
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
           <td width="37%" height="25" class="bg_04"><%=model_code%></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">모델명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="model_name" size="20" value="<%=model_name%>" maxlength="30" class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">모델규격</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="standards" size="35" value="<%=standards%>" maxlength="40"></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">제조사명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="maker_name" size="20" value="<%=maker_name%>" maxlength="30" class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR><TD align="left">
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
		<TR><TD height=10></TD></TR>
		<TR><TD><IMG src='../em/images/supply_history.gif' border='0' alt='품목공급처현황'></TD></TR>
		</TBODY></TABLE></TD></TR>

  <TR><TD align="left">
	 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>공급처코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>공급처명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>공급단가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>첨부파일</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>최종갱신일</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	int i = 1;
	while(iter.hasNext()){
		item = (ItemInfoTable)iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=i%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getSupplyerCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getSupplyerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getSupplyCost(),"")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getFileName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getWrittenDay()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=13 background="../em/images/dot_line.gif"></TD></TR>
<%
		i++;
	}
%>
		</TBODY></TABLE></TD></TR></TABLE>

<input type="hidden" name="mode" value="<%=mode%>">
<input type="hidden" name="model_code" value="<%=model_code%>">
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

	document.onmousedown=dbclick;
	f.submit();
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
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

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 590;
	item_list.style.height = div_h;

}
</script>