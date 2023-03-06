<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*,java.util.*,com.anbtech.pu.entity.*"
%>
<%!
	RequestInfoTable table;
	PurchaseLinkUrl redirect;
%>

<%
	int enableupload	= 5;		// 업로드 개수 지정
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();

	String mode			= request.getParameter("mode");	// 모드
	String request_type = request.getParameter("request_type");

	table = (RequestInfoTable)request.getAttribute("REQUEST_INFO");
	String request_no			= table.getRequestNo();
	String request_date			= table.getRequestDate();
	String requester_div_code	= table.getRequesterDivCode();
	String requester_div_name	= table.getRequesterDivName();
	String requester_id			= table.getRequesterId();
	String requester_info		= table.getRequesterInfo();
	String request_total_mount  = sp.getMoneyFormat(table.getRequestTotalMount(),"");
	String project_code			= table.getProjectCode();

	String item_code			= table.getItemCode();
	String item_name			= table.getItemName();
	String item_desc			= table.getItemDesc();
	String delivery_date		= table.getDeliveryDate();
	String request_quantity		= table.getRequestQuantity();
	String supplyer_code		= table.getSupplyerCode();
	String supplyer_name		= table.getSupplyerName();
	String supply_cost			= sp.getMoneyFormat(table.getSupplyCost(),"");
	String request_cost			= sp.getMoneyFormat(table.getRequestCost(),"");
	String request_unit			= table.getRequestUnit();
	request_type				= table.getRequestType();

	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new RequestInfoTable();
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
	String link_request_info	= redirect.getLinkRequestInfo();
	String link_approval		= redirect.getLinkApproval();
	String link_supplyer_assign	= redirect.getLinkSupplyerAssign();
	String link_app_info		= redirect.getLinkAppInfo();

%>
<SCRIPT language='JavaScript'>
<%
		int i = 1;
		while(i < enableupload){
			if(i == enableupload-1){
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%> size=45 >";
			}
<%			break;
			}
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=45 ><FONT id=id<%=i+1%>></FONT>";
			}
<%			i++;
		}
%>

</SCRIPT>
<%
	String file_stat = "";
	if("modify_request".equals(mode)) {
		RequestInfoTable file = new RequestInfoTable();

		ArrayList file_list = new ArrayList();
		file_list = (ArrayList)request.getAttribute("ITEM_FILE");
		Iterator file_iter = file_list.iterator();

		i = 1;
		
		while(file_iter.hasNext()){
			file = (RequestInfoTable)file_iter.next();
			file_stat = file_stat + "<INPUT type=file name='attachfile"+i+"' size=45> " + file.getFname()+" 삭제! <INPUT type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
			i++;
		}

	} else {
		i=1;
	}
%>
<HTML>
<LINK rel="stylesheet" type="text/css" href="../pu/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0" onLoad="display();">
<FORM name="request_form" method="post" action="PurchaseMgrServlet?upload_folder=estimate" enctype="multipart/form-data">
<!--타이틀-->

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			 <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 구매품목입력</TD></TR></TBODY>
		</TABLE></TD></TR>

  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			 	<TD align=left style='padding-left:5px;'>
				<%=link_item_add%><%=link_item_modify%><%=link_item_delete%> <%=link_supplyer_assign%><%=link_approval%><%=link_list%></TD></TR></TBODY>
		</TABLE></TD></TR>
  
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=25><!--공통정보-->
    <TD vAlign=top>
		<TABLE height=25 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			 	<TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">요청번호</TD>
				<TD width="35%" height="25" class="bg_04"><INPUT type='text' size='15' name='request_no' value='<%=request_no%>' readOnly> </TD>
				<TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">예상발주총액</TD>
				<TD width="35%" height="25" class="bg_04"><INPUT type='text' size='10' name='request_total_mount' value='<%=request_total_mount%>' style="text-align:right;">원</TD></TR></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR><TD height='10px;'></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=25><!--요청품목정보-->
    <TD vAlign=top>
		<TABLE height=25 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
					<TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목코드</TD>
					<TD width="35%" height="25" class="bg_04"><INPUT type='text' size='12' name='item_code' value='<%=item_code%>' class="text_01" readOnly> <a href="javascript:searchCMInfo();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
				    <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목명</TD>
				    <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='20' name='item_name' value='<%=item_name%>' class="text_01"></TD></TR>
				<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
				<TR>
				   <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목설명</TD>
				   <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='35' name='item_desc' value='<%=item_desc%>'></TD>
				   <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">희망입고일</TD>
				   <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='10' name='delivery_date' value='<%=delivery_date%>' class="text_01" readOnly> <a href="Javascript:OpenCalendar('delivery_date');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
				<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
				<TR>
				   <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">요청수량</TD>
				   <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='5' name='request_quantity' value='<%=request_quantity%>' onKeyPress="currency(this);"  onBlur="cal_request_cost();" class="text_01"></TD>
				   <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">요청단위</TD>
				   <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='5' name='request_unit' value='<%=request_unit%>' class="text_01" readOnly> <a href="javascript:sel_stock_unit();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
				<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
				<TR>
				   <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">희망발주업체코드</TD>
				   <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='12' name='supplyer_code' value='<%=supplyer_code%>' readOnly> <a href="javascript:sel_supplyer();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
				   <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">희망발주업체명</TD>
				   <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='20' name='supplyer_name' value='<%=supplyer_name%>'> </TD></TR>
				<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
				<TR>
				   <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">희망단가</TD>
				   <TD width="35%" height="25" class="bg_04" ><INPUT type='text' size='12' name='supply_cost' value='<%=supply_cost%>' onKeyPress="currency(this);" onKeyup="com(this);" onBlur="cal_request_cost();com(this);"; style="text-align:right;">원</TD>
				   <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">요청금액</TD>
				   <TD width="35%" height="25" class="bg_04" ><INPUT type='text' size='12' name='request_cost' value='<%=request_cost%>' style="text-align:right;">원</TD>
				</TR>
				<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
				<TR><TD width="15%" height="25" class="bg_03" background="../share/images/bg-01.gif">첨부파일</TD>
					<TD width="85%" height="25" class="bg_04" colspan="3">
						<%	if (enableupload > 0){	%>
								<%=file_stat%>
						<%		if(i < enableupload){	%>
									<INPUT type='file' name='attachfile<%=i%>' onClick='fileadd_action<%=i%>()' size=45 ><font id=id<%=i%>></font>
						<%		}else if(i == enableupload){	%>
									<INPUT type='file' name='attachfile<%=i%>' size=40 ><font id='id<%=i%>'></font>
						<%		}
							}
						%>			
						</TD></TR>
				<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
					<INPUT type='hidden' name='mode' value='<%=mode%>'>
					<INPUT type='hidden' name='request_type' value='<%=request_type%>'>
					<INPUT type='hidden' name='project_code' value='<%=project_code%>'>
					<INPUT type='hidden' name='requester_div_code' value='<%=requester_div_code%>'>
					<INPUT type='hidden' name='requester_id' value='<%=requester_id%>'>
				</TBODY></TABLE></TD></TR>
</FORM>
	
	<TR><TD height='5px;'></TD></TR>
	<TR><TD align=left><IMG src='../pu/images/request_item.gif' border='0' alt='구매요청품목'></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<TR height=100%><!--리스트-->
		<TD vAlign=top>
		    <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:scroll; overflow-y:auto;">
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=23>
						  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=150 align=middle class='list_title'>품목명</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>단가</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=40 align=middle class='list_title'>수량</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=40 align=middle class='list_title'>단위</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>금액</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=200 align=middle class='list_title'>희망발주업체명</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>희망입고일</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>기발주수량</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>기입고수량</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=60 align=middle class='list_title'>진행상태</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100% align=middle class='list_title'>첨부화일</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
<%
			int no = 1;
			while(table_iter.hasNext()){
				table = (RequestInfoTable)table_iter.next();
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle height="24" class='list_bg'><%=no%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getItemCodeLink()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg' style='padding-left:5px'><%=table.getItemName()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getSupplyCost(),"")%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getRequestQuantity()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getRequestUnit()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getRequestCost(),"")%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg' style='padding-left:5px'><%=table.getSupplyerName()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getDeliveryDate()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOrderQuantity()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getDeliveryQuantity()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getFileLink()%></TD>
					</TR>
					<TR><TD colSpan=25 background="../cm/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
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

function modify_request() 
{ 
	var f = document.request_form;
	f.mode.value = "modify_request_info";
	f.submit();
}


// 품목 정보 가져오기
function searchCMInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=request_unit";
	wopen(strUrl,"SEARCH_ITEM",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 재고 단위 가져오기
function sel_stock_unit()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=request_form&type=STOCK_UNIT&div=one&code=request_unit&code_field=Type구분&name_field=단위구분&minor_code=품목단위&minor_field=품목단위명";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
	//추가: &code_field=품목분류코드&name_field=&minor_code=품목분류명&minor_field=품목분류설명"
} 

function delete_request() 
{ 
	var f = document.request_form;
	var request_no = f.request_no.value;

	var c = confirm("구매요청품목이 모두 삭제됩니다. 구매요청건을 삭제하시겠습니까?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_request_info&request_no="+request_no;
}

function supplyer_assign(assign_rule) 
{ 
	var f = document.request_form;
	var request_no = f.request_no.value;
//	var assign_rule = "order_weight";

	var c = confirm("희망발주업체를 일괄지정하시겠습니까?");
	if(c) location.href = "PurchaseMgrServlet?mode=auto_supplyer_assign&request_no="+request_no+"&assign_rule="+assign_rule;
}

function add_item() 
{ 
	var f = document.request_form;
	var request_type = "<%=request_type%>";

	if((request_type == 'MAN' || request_type == 'MRP' || request_type == 'ROP') && f.item_code.value == ''){
		alert("품목코드를 찾아 선택하십시오.");
		return;
	}

	if(f.item_name.value == ''){
		alert("품목명을 입력하십시오.");
		return;
	}

	if(f.request_quantity.value == ''){
		alert("요청수량을 입력하십시오.");
		f.request_quantity.focus();
		return;
	}

	if(f.request_unit.value == ''){
		alert("요청단위를 선택하십시오.");
		return;
	}

	if(f.delivery_date.value == ''){
		alert("희망입고일을 선택하십시오.");
		return;
	}

	// item_code값이 없을 경우 임시 품목코드 생성
	// 일반자산성품목은 품목코드가 없을 것이므로...
	id = new Date();
	if(f.item_code.value == '' && f.item_name.value != '')	f.item_code.value = id.getTime();

	f.request_cost.value	= unComma(f.request_cost.value);
	f.supply_cost.value		= unComma(f.supply_cost.value);

	f.submit();
}

function del_item() 
{ 
	var f = document.request_form;
	var request_no = f.request_no.value;
	var item_code  = f.item_code.value;

	var c = confirm("품목번호:"+item_code+" 을(를) 삭제하시겠습니까?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_request&request_no="+request_no+"&item_code="+item_code;
} 

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//공급업체찾기
function sel_supplyer() 
{ 
	var item_code = document.request_form.item_code.value;
	url = "../pu/config/search_item_supply_info.jsp?item_code="+item_code+"&sf=request_form&sid=supplyer_code&sname=supplyer_name&scost=supply_cost";
	wopen(url,'add','600','292','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//요청정보보기
function request_info()
{	var f = document.request_form;
	var request_no = f.request_no.value;
	location.href = "PurchaseMgrServlet?mode=modify_request_info&request_no="+request_no;
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

/**********************
 * 발주금액 계산
 **********************/
function cal_request_cost() 
{ 
	var f = document.request_form;
	var request_quantity = f.request_quantity.value;
	var supply_cost = unComma(f.supply_cost.value);

	f.request_cost.value = Comma(request_quantity * supply_cost);
}

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
			document.getElementById('Tip' + index).style.display = 'none';	
		}
	}
}


//결재 상신
function request_app_view(){
	var f = document.request_form;
	var project_code = f.project_code.value;
	var request_no	 = f.request_no.value;
	var request_type = f.request_type.value;
	var total_mount  = unComma(f.request_total_mount.value);
	var no_=<%=no%> // 구매요청 품목 Check변수
	
	// 구매요청품목체크. 없으면 결재로 넘어가지 않음.
	if(no_<2) {
		alert("구매요청품목이 없습니다. 품목을 한개 이상 추가하신 후 전자결재를 진행하십시오.");	
	return;}

	//결재상신 전에 과제별 재료비 체크를 먼저 수행 후, 결재상신이 이루어짐.
	if(request_type == "MRP" || request_type == "RES") {
		location.href = "../servlet/PurchaseOtherMgrServlet?mode=chk_budget&request_no="+request_no+"&request_type="+request_type+"&project_code="+project_code+"&total_mount="+total_mount;
	
	//일반자재(request_type==GEN or ROP)일경우는 바로 결재상신.
	}else{
		var para = "mode=request_app_view&request_no="+request_no+"&request_type="+request_type; 
		location.href="../gw/approval/module/pu_PurchaseApp.jsp?"+para;	
	}
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 577;
	var div_h = c_h - 286;
	item_list.style.height = div_h;

} 
</script>