<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%!
	EnterInfoTable table;
	PurchaseLinkUrl redirect;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
%>

<%
	String mode = request.getParameter("mode");	// 모드
	
	int enableupload	= 5;		// 업로드 개수 지정

	table = (EnterInfoTable)request.getAttribute("ENTER_INFO");
	String enter_no				= table.getEnterNo();
	String supplyer_code		= table.getSupplyerCode();
	String supplyer_name		= table.getSupplyerName();
	String enter_date			= table.getEnterDate();
	String enter_total_mount	= sp.getMoneyFormat(table.getEnterTotalMount(),"");
	String requestor_div_code	= table.getRequestorDivCode();
	String requestor_div_name	= table.getRequestorDivName();
	String requestor_id			= table.getRequestorId();
	String requester_info		= table.getRequestorInfo();
	String enter_type			= table.getEnterType();
	String monetary_unit		= table.getMonetaryUnit();
	String filelink				= table.getFileLink();

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
	String link_app_info		= redirect.getLinkAppInfo();
%>



<SCRIPT language='JavaScript'>
<%
		int i = 1;
		while(i < enableupload){
			if(i == enableupload-1){
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%> size=40 >";
			}
<%			break;
			}
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=40 ><FONT id=id<%=i+1%>></FONT>";
			}
<%			i++;
		}
%>

</SCRIPT>
<%

	String file_stat = "";
	if("modify_enter_info".equals(mode)) {
	com.anbtech.pu.entity.EnterInfoTable file = new com.anbtech.pu.entity.EnterInfoTable();

	ArrayList file_list = new ArrayList();
	file_list = (ArrayList)request.getAttribute("ITEM_FILE");
	Iterator file_iter = file_list.iterator();

	i = 1;
	
	while(file_iter.hasNext()){
		file = (EnterInfoTable)file_iter.next();
		file_stat = file_stat + "<INPUT type=file name='attachfile"+i+"' size=40> " + file.getFname()+" 삭제! <INPUT type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
		i++;
		}

	} else {
		i=1;
	}
%>

<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display();">

<form name="reg_enter" method="post" action="PurchaseMgrServlet?upload_folder=receipt" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 구매입고등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<%=link_item_add%><%=link_info_modify%><%=link_info_delete%><%=link_app_info%><%=link_list%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='enter_no' value='<%=enter_no%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고일자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='enter_date' value='<%=enter_date%>' class="text_01" readOnly> <a href="Javascript:OpenCalendar('enter_date');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고담당부서</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='20' name='requester_div_name' value='<%=requestor_div_name%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고담당자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='requester_info' value='<%=requester_info%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">매입총액</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='enter_total_mount' value='<%=enter_total_mount%>' style="text-align:right;" readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체</td>
           <td width="37%" height="25" class="bg_04">[<%=supplyer_code%>] <%=supplyer_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	if(!link_info_modify.equals("")){	%>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
				<%	if (enableupload > 0){
				%>
		            <%=file_stat%>
				<%	if(i < enableupload){
				%>
			    <INPUT type='file' name='attachfile<%=i%>' onClick='fileadd_action<%=i%>()' size='40' > 
			           <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <INPUT type='file' name='attachfile<%=i%>' size='40' >
				            <font id='id<%=i%>'></font>
				<%		}
					}
				%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	}else{	%>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=filelink%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	}	%>
	   </tbody></table>

<!-- 입고품목 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../pu/images/title_input_item.gif' border='0' alt='입고품목'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:168; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
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
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>구매요청번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>구매발주번호</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (EnterInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
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
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRequestNo()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getOrderNo()%></td>
			</TR>
			<TR><TD colSpan=19 background="../pu/images/dot_line.gif"></TD></TR>
<%
		no++;	
	}
%>
		</TBODY></TABLE></DIV>

<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='requester_id' value='<%=requestor_id%>'>
<input type='hidden' name='requester_div_code' value='<%=requestor_div_code%>'>
<input type='hidden' name='monetary_unit' value='<%=monetary_unit%>'>
<input type='hidden' name='enter_type' value='<%=enter_type%>'>
<input type='hidden' name='supplyer_code' value='<%=supplyer_code%>'>
<input type='hidden' name='supplyer_name' value='<%=supplyer_name%>'>
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

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//입고정보저장
function save_enter() 
{ 
	var f = document.reg_enter;
	f.enter_total_mount.value	= unComma(f.enter_total_mount.value);

	f.submit();
}

//입고정보삭제
function delete_enter() 
{ 
	var f = document.reg_enter;
	var enter_no = f.enter_no.value;

	var c = confirm("입고품목정보와 함께 입고정보가 모두 삭제됩니다. 삭제하시겠습니까?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_enter_info&enter_no="+enter_no;
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

//통화 코드 가져오기
function sel_currency()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=reg_order&type=CURRENCY&div=one&code=monetary_unit&&code_field=Type구분&name_field=단위구분&minor_code=화폐단위&minor_field=화폐단위명";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
} 

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 

	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 485;
	var div_h = c_h - 58;
	item_list.style.height = div_h;

}

//승인내역보기
function viewAppInfo(){
		var enter_no = document.reg_enter.enter_no.value;
		wopen('../servlet/PurchaseMgrServlet?mode=enter_app_print&enter_no='+enter_no,'VIEW_APP_INFO','650','600','scrollbars=yes,toolbar=no,status=no,resizable=no');

}
</script>