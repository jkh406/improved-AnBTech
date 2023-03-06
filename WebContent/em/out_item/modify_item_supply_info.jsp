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
	
	//품목및공급정보
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
	String link_url				= item.getLinkUrl();

	//공급정보변경이력
	ArrayList list = new ArrayList();
	list = (ArrayList)request.getAttribute("ListSupplyInfo");
	Iterator iter = list.iterator();
%>


<script language=JavaScript>
<!--
<%
	int i = 1;
	int enableupload	= 3;	// 업로드 개수 지정

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

<body topmargin="0" leftmargin="0" onLoad="display();">

<form method=post name="writeForm" action='../servlet/EstimateMgrServlet' enctype='multipart/form-data' style="margin:0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../em/images/blet.gif"> 품목공급정보변경</TD></TR></TBODY>
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
           <td width="37%" height="25" class="bg_04"><%=item_name%></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">품목단위</td>
           <td width="37%" height="25" class="bg_04"><%=unit%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">모델코드</td>
           <td width="37%" height="25" class="bg_04"><%=model_code%></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">모델명</td>
           <td width="37%" height="25" class="bg_04"><%=model_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">모델규격</td>
           <td width="37%" height="25" class="bg_04"><%=standards%></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">제조사명</td>
           <td width="37%" height="25" class="bg_04"><%=maker_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">공급업체</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=supplyer_code%> <%=supplyer_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">공급단가</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type='text' size='12' name='supply_cost' value='<%=supply_cost%>' class="text_01" maxlength="15" style="text-align:right;" onKeyPress="currency(this);" onKeyup="com(this);">원</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">첨부파일</td>
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
				            <input type=file name=attachfile<%=i%> size="30">
				            <font id=id<%=i%>></font>
				<%
						}
					}
				%>				   
		   </td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
 	   </tbody></table>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR><TD align="left">
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
		<TR><TD height=10></TD></TR>
		<TR><TD><IMG src='../em/images/supply_history.gif' border='0' alt='공급단가 변경이력'></TD></TR>
		</TBODY></TABLE></TD></TR>

  <TR><TD align="left">
  <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">
	 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
			<TR bgColor=#9DA9B9 height=1><TD colspan=7></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=100 align=middle class='list_title'>변경일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>공급단가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>첨부파일</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=7></TD></TR>
<%
	while(iter.hasNext()){
		item = (ItemInfoTable)iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=item.getWrittenDay()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getSupplyCost(),"")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getFileName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=7 background="../em/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></DIV></TD></TR></TABLE>

<input type="hidden" name="mode" value="<%=mode%>">
<input type="hidden" name="model_code" value="<%=model_code%>">
<input type="hidden" name="supplyer_code" value="<%=supplyer_code%>">
<input type="hidden" name="supplyer_name" value="<%=supplyer_name%>">
</form>
</body>
</html>

<script language="javascript">

//공급단가변경
function modify_danga(model_code,supplyer_code) {
	location.href = "../servlet/EstimateMgrServlet?mode=modify_out_item_supply_info&model_code="+model_code+"&supplyer_code="+supplyer_code;
}

//공급정보삭제
function delete_info(mid,model_code,supplyer_code) {
	var delete_confirm = confirm("해당 공급단가정보를 삭제하시겠습니까?");

	if(delete_confirm) location.href = "../servlet/EstimateMgrServlet?mode=delete_out_item_supply_info&no="+mid+"&model_code="+model_code+"&supplyer_code="+supplyer_code;
	else return;
}

//목록보기
function go_list() 
{ 
	location.href = '../servlet/EstimateMgrServlet?mode=list_out_item';
}

//필수 입력사항 체크
function checkForm(){ 
	
	var f = document.writeForm;

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