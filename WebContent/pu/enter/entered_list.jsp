<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.pu.entity.*"
%>
<%!
	EnterInfoTable table;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>
<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ENTER_LIST");
	Iterator table_iter = table_list.iterator();

	//총입고금액 가져오기
	String total_cost = (String)request.getAttribute("TOTAL_COST");
	total_cost = sp.getMoneyFormat(total_cost,"");
%>

<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>
<body topmargin="0" leftmargin="0" onLoad="display();">
<form name="srForm" method="post" action="PurchaseMgrServlet" enctype="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
					<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD">
						<TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 과재별구매현황</TD></TR></TBODY>
			</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
					<TR>
						<TD align=left width=5 ></TD>
						<TD align=left width=500>
							<IMG src='../pu/images/bt_search3.gif' onClick='javascript:search();' style='cursor:hand' align='absmiddle'>
						</TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고번호</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='10' name='enter_no' value=''></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목코드</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='10' name='item_code' value=''></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고일자</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='10' name='s_date' readOnly> <a href="Javascript:OpenCalendar('s_date');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a> ~ <input type='text' size='10' name='e_date' readOnly> <a href="Javascript:OpenCalendar('e_date');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">과제코드</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='12' name='project_code' readOnly> <input type='text' name='project_name' size='10' readOnly> <a href="javascript:sel_pjt_code();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	   </TBODY></TABLE>

<!-- 품목상제정보 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../pu/images/title_input_item.gif' border='0' alt='입고품목' align='absmiddle'></TD><TD width='100%' align=left><IMG src='../pu/images/total_inmoney.gif' border='0' alt='총입고금액' align='absmiddle'><font color='#639DE9'>&nbsp;<%=total_cost%></font>원</TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:278; overflow-x:auto; overflow-y:auto;">
  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>과제명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>입고번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>품목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>입고수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>입고단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>입고단가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>입고금액</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>입고일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>공급업체명</TD>
			  
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (EnterInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></TD>
			      <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getProjectName()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getEnterNo()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemName()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getEnterQuantity()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getEnterUnit()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getUnitCost(),"")%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getEnterCost(),"")%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getEnterDate()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getSupplyerName()%></TD>
				 
			</TR>
			<TR><TD colSpan=25 background="../pu/images/dot_line.gif"></TD></TR>
<%	
		no++;
	}
%>
		</TBODY></TABLE></DIV>
</form>
</body>
</html>

<script language=javascript>
/*
//최초 실행시 오늘날짜로 세팅
var today = new Date();
var yr = today.getYear() ;
var mon = today.getMonth()+1 ;
var date = today.getDate();
if(mon < 10) mon = "0" + mon;
if(date < 10) date = "0" + date;

var f = document.srForm;
f.s_date.value = yr + "-" + mon + "-" + date;
f.e_date.value = yr + "-" + mon + "-" + date;
*/

//팝업창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

// 과제찾기
function sel_pjt_code() {
	wopen('../servlet/PsmProcessServlet?mode=search_project&target=srForm.project_code/srForm.project_name','search_pjt','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function search() {

	var f = document.srForm;

	var enter_no = f.enter_no.value;
	var item_code = f.item_code.value;
	var s_date = f.s_date.value;
	var e_date = f.e_date.value;
	var enter_date = s_date.substring(0,4) + s_date.substring(5,7) + s_date.substring(8,10) + e_date.substring(0,4) + e_date.substring(5,7) + e_date.substring(8,10);
	var project_code = f.project_code.value;

	var where_sea = '';
	if(enter_no != '') where_sea += "enter_no|" + enter_no + ",";
	if(item_code != '') where_sea += "item_code|" + item_code + ",";
	if(enter_date != '') where_sea += "enter_date|" + enter_date + ",";
	if(project_code != '') where_sea += "project_code|" + project_code + ",";

//alert(where_sea);
	location.href = "PurchaseMgrServlet?mode=entered_list&searchscope=detail&searchword=" + where_sea;
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 

	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 435;
	var div_h = c_h - 64;
	item_list.style.height = div_h;

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
	var url = "../pu/enter/input_enter_no.jsp?items=" + items;
	wopen(url,'enter','300','157','scrollbars=no,toolbar=no,status=no,resizable=no');
//	alert(items);
}
</script>