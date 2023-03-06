<%@ include file="../../admin/configHead.jsp"%>
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
	estimate = (EstimateInfoTable)request.getAttribute("EstimateInfo");
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../em/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" onLoad="display()">

<FORM method='post' name="writeForm" action='../servlet/EstimateMgrServlet' enctype='multipart/form-data' style="margin:0">
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../em/images/blet.gif"> 견적품목입력</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=100%>
				<a href="javascript:write_estimate('<%=estimate.getEstimateNo()%>','<%=estimate.getVersion()%>');"><img src="../em/images/bt_view_es.gif" border="0" align="absmiddle" alt="견적정보"></a>
				<a href="javascript:Display_tip(1, 'show', true);"  onfocus="this.blur();"><img src="../em/images/bt_add_item.gif" border="0" align="absmiddle" onMouseout="Display_tip(1, 'hide', false)"></a>

				<div id="tip1" style="display:none;position:absolute;z-index:1;margin-top:21px;margin-left:-100px" onMouseout="Display_tip(1, 'hide', false)" onMouseover="Display_tip(1, 'hide', true)">
				<table border="0" cellpadding="0" cellspacing="0" width="200" style="border:#999999 solid 1px" bgcolor="#f6f6f6">
					<tr height="5"><td></td></tr>
					<tr><td>
						<a href="javascript:add_item('<%=estimate.getEstimateNo()%>','<%=estimate.getVersion()%>','1');"><img src="../em/images/i_bullet_04.gif" border="0" width="3" height="3" align="absmiddle" hspace="3" vspace="7">자사개발품목 또는 임의품목</a><br>
						<a href="javascript:add_item('<%=estimate.getEstimateNo()%>','<%=estimate.getVersion()%>','2');"><img src="../em/images/i_bullet_04.gif" border="0" width="3" height="3" align="absmiddle" hspace="3" vspace="7">외부조달품목 또는 임의품목</a></td></tr>
				<tr height="5"><td></td></tr></table></div>

				금액절사 
				<select name="cut_unit">
					<option value="0" <% if(estimate.getCutUnit().equals("0")) out.print("selected");%>>사용안함.</option>
					<option value="1" <% if(estimate.getCutUnit().equals("1")) out.print("selected");%>>원단위절사</option>
					<option value="10" <% if(estimate.getCutUnit().equals("100")) out.print("selected");%>>십단위절사</option>
					<option value="100" <% if(estimate.getCutUnit().equals("100")) out.print("selected");%>>백단위절사</option>
					<option value="1000" <% if(estimate.getCutUnit().equals("1000")) out.print("selected");%>>천단위절사</option>
				</select> 
				특별할인 <input type="text" name="special_change" value="<%=estimate.getSpecialChange()%>" size="2" maxlength="2" style="text-align:right;" onKeyPress="currency(this);">% <a href="javascript:apply();"><img src="../em/images/bt_apply.gif" border="0" align="absmiddle" alt="적용"></a> <a href="javascript:save();"><img src="../em/images/bt_save.gif" border="0" align="absmiddle" alt="저장"></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE></FORM>

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
  <TR><TD width=100% align="center">
	<!-- 견적정보-->
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
         <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">견적번호</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getEstimateNo()%><input type="hidden" name="estimate_no" value="<%=estimate.getEstimateNo()%>"></TD>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">회사명</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getCompanyName()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">견적제목</TD>
           <TD width="37%" height="25" class="bg_04" colspan="3"><%=estimate.getEstimateSubj()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TBODY></TABLE></TD></TR>

  <TR><TD width=100% align="center"><!--총견적금액합계 -->
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
		<TR><TD height=10></TD></TR>
		<TR><TD height=10 class='list_bg'><b>총견적금액: <font color='red'><%=sp.getMoneyFormat(estimate.getTotalAmount(),"")%></font> 원</b></TD></TR>
		</TBODY></TABLE></TD></TR>

  <TR><TD width="100%" align="left"><!--견적품목 -->
	  <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
			<TR vAlign=middle height=25>
			  <TD noWrap width=100 align=middle class='list_title'>품목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>모델명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>할인율(%)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>세율(%)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>견적가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>관리</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>개발원가(원)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>이익율(%)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>공급단가(원)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>규격</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>공급업체</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
	<%
		ArrayList item_list = new ArrayList();
		item_list = (ArrayList)request.getAttribute("Item_List");
		Iterator item_iter = item_list.iterator();
		int count = 0;
		while(item_iter.hasNext()){
			item = (ItemInfoTable)item_iter.next();

	%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=item.getItemName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getModelName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getQuantity()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getUnit()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getDiscountPercent()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getTaxPercent()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getEstimateValue(),"")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><a href="javascript:modify_item('<%=estimate.getEstimateNo()%>','<%=estimate.getVersion()%>','<%=item.getMid()%>');"><img src="../em/images/lt_modify.gif" border="0" align="absmiddle"></a> <a href="javascript:delete_item('<%=estimate.getEstimateNo()%>','<%=estimate.getVersion()%>','<%=item.getMid()%>');"><img src="../em/images/lt_del.gif" border="0" align="absmiddle"></a></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getBuyingCost(),"")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getGainsPercent()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getSupplyCost(),"")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=item.getStandards()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getSupplyerName()%></TD></TR>
			<TR><TD colSpan=25 background="../em/images/dot_line.gif"></TD></TR>
	<%		count++;
		}
	%>
			<TR><TD colSpan=25 class='list_bg'><%=estimate.getSpecialInfo()%></TD></TD></TR>
		</TBODY></TABLE></DIV></TD></TR></TABLE>
</body>
</html>

<script language="javascript">
//팝업창열기
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//현재정보저장후종료
function save(count){
	if(count < 1){
		var saveconfirm = confirm("견적 품목이 추가되지 않았습니다. 나중에 추가하실 수 있습니다.\n\n그래도 완료하시겠습니까?");
		if(saveconfirm){
			location.href = "../servlet/EstimateMgrServlet?mode=mylist";
			return;
		}
		else return;
	}else location.href = "../servlet/EstimateMgrServlet?mode=mylist";
}

//특별할인및절사금액반영
function apply(){
	var f = document.writeForm;
	var special_change = f.special_change.value;
	var cut_unit = f.cut_unit.value;

	var apply_confirm = confirm(cut_unit + "단위 절삭 및 특별할인 " + special_change + "%를 적용시겠습니까?");
	if(apply_confirm){
		location.href = "../servlet/EstimateMgrServlet?mode=apply_special&estimate_no=<%=estimate.getEstimateNo()%>&ver=<%=estimate.getVersion()%>&special_change="+special_change+"&cut_unit="+cut_unit;
		return;
	}
	else return;
}

//견적정보보기
function write_estimate(estimate_no,version){
	location.href = "../servlet/EstimateMgrServlet?mode=modify&estimate_no="+estimate_no+"&ver="+version;
}

//공급가정보변경이력보기
function view_value_info(item_no,supplyer) {

	var url = "../servlet/EstimateMgrServlet?mode=view_supply_info&item_no="+item_no+"&supplyer="+supplyer+"&company_name=<%=estimate.getCompanyName()%>";

	wopen(url,'view_history','600','285','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//견적품목추가
function add_item(estimate_no,version,item_class) {

	var strUrl = "../servlet/EstimateMgrServlet?mode=add_estimate_item&estimate_no="+estimate_no+"&ver="+version+"&item_class="+item_class;
	wopen(strUrl,"ADD_ITEM",'600','350','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//견적품목정보 수정
function modify_item(estimate_no,version,mid){

	var strUrl = "../servlet/EstimateMgrServlet?mode=modify_estimate_item&estimate_no="+estimate_no+"&ver="+version+"&no="+mid;
	wopen(strUrl,"MODIFY_ITEM",'600','350','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//견적품목삭제
function delete_item(estimate_no,version,mid){
	var delete_confirm = confirm("해당 견적품목을 삭제하시겠습니까?");
	if(delete_confirm){
		location.href = "../servlet/EstimateMgrServlet?mode=delete_estimate_item&estimate_no="+estimate_no+"&ver="+version+"&no="+mid;
		return;
	}
	else return;
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

flag=true;

function Display_tip(index , view, flag) {

	if(view == 'show'){
		if(flag) {
			if (navigator.userAgent.indexOf("MSIE") != -1) {		
				document.getElementById('Tip' + index).style.display='';
				flag = false;
			}else {
				location.href = "";
			}		
		} else {
			document.getElementById('Tip' + index).style.display='none';
			flag = true;		
		}
	} else if(view == 'hide'){
		if (flag){
			document.getElementById('Tip' + index).style.display ='';
		}else{
			document.getElementById('Tip' + index).style.display = "none";	
		}
	}
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 430;
	item_list.style.height = div_h;

}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
</script>