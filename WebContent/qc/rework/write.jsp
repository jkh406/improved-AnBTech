<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
    contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.qc.entity.*"
%>
<%!
	InspectionMasterTable table;
	InspectionItemByItemTable table2;
	QualityCtrlLinkUrl redirect;
%>

<%
	String mode					= request.getParameter("mode");

	table = (InspectionMasterTable)request.getAttribute("ITEM_INFO");

	String mid					= table.getMid();
	String request_no			= table.getRequestNo();
	String item_code			= table.getItemCode();
	String item_name			= table.getItemName();
	String item_desc			= table.getItemDesc();
	String supplyer_code		= table.getSupplyerCode();
	String supplyer_name		= table.getSupplyerName();
	String lot_no				= table.getLotNo();
	String lot_quantity			= table.getLotQuantity();
	String inspected_quantity	= table.getInspectedQuantity();
	String bad_item_quantity	= table.getBadItemQuantity();
	String inspection_result	= table.getInspectionResult();
	String process_stat			= table.getProcessStat();
	String request_date			= table.getRequestDate();
	String requester_id			= table.getRequesterId();
	String requester_info		= table.getRequesterInfo();
	String requester_div_code	= table.getRequesterDivCode();
	String requester_div_name	= table.getRequesterDivName();
	String inspect_date			= table.getInspectDate();
	String inspector_id			= table.getInspectorId();
	String inspector_info		= table.getInspectorInfo();
	String inspector_div_code	= table.getInspectorDivCode();
	String inspector_div_name	= table.getInspectorDivName();
	String other_info			= table.getOtherInfo();
	String bad_percentage		= table.getBadPercentage();
	String factory_code			= table.getFactoryCode();
	String factory_name			= table.getFactoryName();
	String pre_request_no		= table.getPreRequestNo();
	String item_type			= table.getItemType();
	String serial_no_s			= table.getSerialNoS();
	String serial_no_e			= table.getSerialNoE();

	ArrayList inspection_item_list = new ArrayList();
	inspection_item_list = (ArrayList)request.getAttribute("INSPECTION_ITEM_LIST");
	Iterator table_iter = inspection_item_list.iterator();

	//링크 문자열 가져오기
	redirect = new QualityCtrlLinkUrl();
	redirect = (QualityCtrlLinkUrl)request.getAttribute("Redirect");
	
	String link_list = redirect.getLinkList();

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../qc/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form method=post name="writeForm" action='../servlet/QualityCtrlServlet' enctype='multipart/form-data' style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../qc/images/blet.gif"> 수리작업결과등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=100% style='padding-left:5px;'><a href="javascript:checkForm();"><img src="../qc/images/bt_reg.gif" border="0" align="absmiddle" alt="결과등록"></a> <a href="javascript:go_list('<%=link_list%>');"><img src="../qc/images/bt_list.gif" border="0" align="absmiddle" alt="목록보기"></a></TD><TD align=left width=100><%=pre_request_no%></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>
 
<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center"><!--검사의뢰정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../qc/images/title_rework_info.gif"  border="0" alt="재작업의뢰정보"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">의뢰번호</td>
           <td width="37%" height="25" class="bg_04"><%=request_no%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">품목코드</td>
           <td width="37%" height="25" class="bg_04"><%=item_code%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">품목명</td>
           <td width="37%" height="25" class="bg_04"><%=item_name%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">품목설명</td>
           <td width="37%" height="25" class="bg_04"><%=item_desc%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">입고공장</td>
           <td width="37%" height="25" class="bg_04">[<%=factory_code%>] <%=factory_name%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">일련번호</td>
           <td width="37%" height="25" class="bg_04"><%=serial_no_s%> ~ <%=serial_no_e%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">전작업지시번호</td>
           <td width="37%" height="25" class="bg_04"><%=lot_no%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">재작업대상수량</td>
           <td width="37%" height="25" class="bg_04"><%=lot_quantity%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">작업의뢰자</td>
           <td width="37%" height="25" class="bg_04"><%=requester_info%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">작업의뢰일자</td>
           <td width="37%" height="25" class="bg_04"><%=request_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table></td></tr>

  <tr><td align="center"><!--검사결과-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../qc/images/title_rework_result.gif"  border="0" alt="재작업결과"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">작업지시번호</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type="text" name="lot_no" size="15" maxlength="10" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
		   <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">작업내용</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><textarea rows="3" name="other_info" cols="80"></textarea></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>		 
         <tr><td height=10 colspan="4"></td></tr></tbody></table></td></tr></table>

<input type="hidden" name="mode" value="<%=mode%>">
<input type="hidden" name="request_no" value="<%=request_no%>">
<input type="hidden" name="item_code" value="<%=item_code%>">
<input type="hidden" name="item_name" value="<%=item_name%>">
<input type="hidden" name="item_desc" value="<%=item_desc%>">
<input type="hidden" name="factory_code" value="<%=factory_code%>">
<input type="hidden" name="factory_name" value="<%=factory_name%>">
<input type="hidden" name="lot_quantity" value="<%=lot_quantity%>">
<input type="hidden" name="item_type" value="<%=item_type%>">
</form>
</body>
</html>

<script language="javascript">
//팝업창 열기
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../qc/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//숫자만 입력되게
function currency(obj)
{
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false
	}
}

//필수 입력사항 체크
function checkForm(){ 
	var f = document.writeForm;

	if(f.lot_no.value == ''){
		alert("작업지시번호를 입력하십시오.");
		f.lot_no.focus();
		return;
	}

	var msg = "재검사의뢰 하시겠습니까?";

	if(confirm(msg)){
		document.onmousedown=dbclick;
		f.submit();
	}
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}


function myRound(num, pos) { 
	var posV = Math.pow(10, (pos ? pos : 2))
	return Math.round(num*posV)/posV
}


//리스트
function go_list(url){
	location.href = url;
}

//이전검사이력보기
function view_pre_info(item_code){
	var f = document.writeForm;
	var request_no = f.pre_request_no.value;
	if(request_no != ''){
		var url = "../servlet/QualityCtrlServlet?mode=view_result_p&request_no=" + request_no + "&item_code=" + item_code;
		wopen(url,'view_pre_info','850','600','scrollbars=yes,toolbar=no,status=no,resizable=no');
	}
	f.pre_request_no.value = '';
}
</script>