<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.qc.entity.*"
%>
<%!
	FailureInfoTable table;
%>
<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("FAILURE_LIST");
	Iterator table_iter = table_list.iterator();
%>

<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display();">
<form name="srForm" method="post" action="QualityCtrlServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../qc/images/blet.gif"> 불량현황</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../qc/images/bt_search3.gif' onClick='javascript:search();' style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 검색조건 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">모델코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='model_code' value=''></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">품목코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='item_code' value=''></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사일자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='s_date' readOnly> <a href="Javascript:OpenCalendar('s_date');"><img src="../qc/images/bt_search.gif" border="0" align="absmiddle"></a> ~ <input type='text' size='10' name='e_date' readOnly> <a href="Javascript:OpenCalendar('e_date');"><img src="../qc/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">제품일련번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='serial_no' maxlength='15'></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">공급업체</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><INPUT type='text' size='15' name='supplyer_code' readonly> <INPUT type='text' size='20' name='supplyer_name' readonly> <a href="javascript:sel_supplyer();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<!-- 검사결과리스트 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../qc/images/title_baditem_info.gif' border='0' alt='검색결과'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan='23'></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>불량항목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>불량항목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>불량내용</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
			   <TD noWrap width=100 align=middle class='list_title'>모델코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>제품일련번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>검사의뢰번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD> 
			  <TD noWrap width=80 align=middle class='list_title'>검사일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>LOT NO.</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>공급업체명</TD>
			  
<!--			  <TD noWrap width=120 align=middle class='list_title'>공장명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>-->
			 
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan='23'></TD></TR>
<%
		int no = 1;
		while(table_iter.hasNext()){
			table = (FailureInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getInspectionCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getInspectionName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getWhyFailure()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getModelCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getSerialNo()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRequestNo()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getInspectDate()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getLotNo()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getSupplyerName()%></td>
				  
			<!--	  <TD align=middle class='list_bg'><%=table.getFactoryName()%></td>
				  <TD><IMG height=1 width=1></TD>-->
				 
			</TR>
			<TR><TD colSpan='23' background="../qc/images/dot_line.gif"></TD></TR>
<%	
		no++;
	}

	if(no == 1) out.print("<TR><TD height='23' colSpan=15 align='middle' class='list_bg'>▒▒▒ 등록된 불합격 정보가 없습니다. ▒▒▒</TD></TR>");
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
	wopen('../servlet/PsmProcessServlet?mode=search_project&target=srForm.serial_no/srForm.project_name','search_pjt','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function search() {

	var f = document.srForm;

	var model_code = f.model_code.value;
	var item_code = f.item_code.value;
	var s_date = f.s_date.value;
	var e_date = f.e_date.value;
	var inspect_date = s_date.substring(0,4) + s_date.substring(5,7) + s_date.substring(8,10) + e_date.substring(0,4) + e_date.substring(5,7) + e_date.substring(8,10);
	var serial_no = f.serial_no.value;
	var supplyer_code = f.supplyer_code.value;

	var where_sea = '';
	if(model_code != '') where_sea += "model_code|" + model_code + ",";
	if(item_code != '') where_sea += "item_code|" + item_code + ",";
	if(inspect_date != '') where_sea += "inspect_date|" + inspect_date + ",";
	if(serial_no != '') where_sea += "serial_no|" + serial_no + ",";
	if(supplyer_code != '') where_sea += "supplyer_code|" + supplyer_code + ",";

	location.href = "QualityCtrlServlet?mode=failure_info&searchscope=detail&searchword=" + where_sea;
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../qc/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 470;
	var div_h = c_h - 376;
	item_list.style.height = div_h;

}

//공급업체찾기
function sel_supplyer() 
{ 
	url = "../pu/order/searchCompany.jsp?sf=srForm&sid=supplyer_code&sname=supplyer_name";
	wopen(url,'add','600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
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