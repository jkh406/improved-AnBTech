<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%!
	ReservedItemInfoTable table;
	StockLinkUrl redirect;
%>
<%
	String mode		= request.getParameter("mode");

	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_LIST");
	Iterator table_iter = table_list.iterator();

	//링크문자열 가져오기
	redirect = new com.anbtech.st.entity.StockLinkUrl();
	redirect = (StockLinkUrl)request.getAttribute("REDIRECT");

	String view_pagecut = redirect.getViewPagecut();
//	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String input_hidden = redirect.getInputHidden();
%>

<html>
<link rel="stylesheet" type="text/css" href="../st/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display()" oncontextmenu="return false">

<form name="srForm" method="post" action="StockMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><IMG src="../st/images/blet.gif"> 생산출고품목현황</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><IMG src="../st/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <IMG src="../st/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <IMG src="../st/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0 border=0>
			<TR>
			  <TD align=left width=5>&nbsp;</TD>
			  <TD align=left width=500>
					<IMG src='../st/images/bt_search3.gif' onClick='javascript:search();' style='cursor:hand' align='absmiddle'>
			  </TD><TD width='300' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">의뢰번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='s_delivery_no'></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">생산지시번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='reference_no'></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>

<!-- 의뢰품목리스트 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../st/images/title_delivery_list.gif' border='0' alt='출고품목리스트'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:340; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=23></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>의뢰번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>생산지시번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>공장명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>요청<br>수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>요청<br>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>요청일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>출고<br>수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>출고<br>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>출고일자</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=23></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (ReservedItemInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveryNo()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRefNo()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getFactoryName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRequestQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRequestUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRequestDate().substring(0,4)+"-"+table.getRequestDate().substring(4,6)+"-"+table.getRequestDate().substring(6,8)%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveryQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveryUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveryDate()%></td>
			</TR>
			<TR><TD colSpan=23 background="../st/images/dot_line.gif"></TD></TR>
<%	
		no++;
	}
%>
		</TBODY></TABLE></DIV>

<%=input_hidden%>
</form>
</body>
</html>

<script language=javascript>
//팝업창 열기
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//검색
function search() {

	var f = document.srForm;

	var delivery_no = f.s_delivery_no.value;
	var reference_no = f.reference_no.value;

	var where_sea = '';
	if(delivery_no != '') where_sea += "delivery_no|" + delivery_no + ",";
	if(reference_no != '') where_sea += "ref_no|" + reference_no + ",";

	location.href = "StockMgrServlet?mode=<%=mode%>&searchscope=detail&searchword=" + where_sea;
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../st/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 공장 선택
function sel_factory(){
	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&field=factory_code/factory_name";
	wopen(url,'enterCode','400','307','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function sel_item_type()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=srForm&type=ITEM_TYPE&div=one&code=item_type&code_name=item_type_name";
	wopen(url,'add','550','307','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function delivery_sel() {

	var f = document.srForm;

	var delivery_quantity = f.quantity.value;
	if(delivery_quantity == '0'){
		alert("출고수량은 0보다 커야 합니다.");
		return;
	}
	f.submit();
}

// 숫자만 입력되게
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

// 천단위 콤마 삽입
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

// 숫자에서 Comma 제거
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
	var div_h = h - 410;
	item_list.style.height = div_h;

}

//선택품목 출고처리
function enter_selected() {
	var f = document.srForm.checkbox;
	var items = "";
	var s_count = 0;
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			items += f[i].value+",";
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("품목을 선택하십시오.");
	   return;
    }
	var url = "../servlet/StockMgrServlet?mode=modify_reserved_item_all&items=" + items;
	var c =  confirm("선택하신 품목들을 일괄 출고처리하시겠습니까?");
	if(c) location.href = url;
//	alert(items);
}

//선택품목 출고 결재상신
function app_selected() {
	var f = document.srForm.checkbox;
	var items = "";
	var s_count = 0;
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			items += f[i].value+",";
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("품목을 선택하십시오.");
	   return;
    }
	var url = "../servlet/StockMgrServlet?mode=app_reserved_item&items=" + items;
	var c =  confirm("선택하신 품목들의 출고를 위한 전자결재를 진행하시겠습니까?");
	if(c) location.href = url;
//	alert(items);
}

var checkflag = false; 

function check(field) { 
	if (checkflag == false) { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = true; 
		} 
	checkflag = true; 
	}else { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = false; 
		} 
	checkflag = false; 
	} 
}


</script>