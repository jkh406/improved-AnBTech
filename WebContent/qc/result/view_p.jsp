<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	language="java"
	contentType="text/html;charset=KSC5601"
	errorPage="../../admin/errorpage.jsp"
	import="java.sql.*, java.io.*, java.util.*,com.anbtech.qc.entity.*"
%>
<%!
	InspectionMasterTable table;
	InspectionItemByItemTable table2;
	QualityCtrlLinkUrl redirect;
%>

<%
	String mode					= request.getParameter("mode");

	table = (InspectionMasterTable)request.getAttribute("ITEM_INFO");

	String mid					=	table.getMid();
	String request_no			=	table.getRequestNo();
	String item_code			=	table.getItemCode();
	String item_name			=	table.getItemName();
	String item_desc			=	table.getItemDesc();
	String supplyer_code		=	table.getSupplyerCode();
	String supplyer_name		=	table.getSupplyerName();
	String lot_no				=	table.getLotNo();
	String lot_quantity			=	table.getLotQuantity();
	String inspected_quantity	=	table.getInspectedQuantity();
	String bad_item_quantity	=	table.getBadItemQuantity();
	String inspection_result	=	table.getInspectionResult();
	String process_stat			=	table.getProcessStat();
	String request_date			=	table.getRequestDate();
	String requester_id			=	table.getRequesterId();
	String requester_info		=	table.getRequesterInfo();
	String requester_div_code	=	table.getRequesterDivCode();
	String requester_div_name	=	table.getRequesterDivName();
	String inspect_date			=	table.getInspectDate();
	String inspector_id			=	table.getInspectorId();
	String inspector_info		=	table.getInspectorInfo();
	String inspector_div_code	=	table.getInspectorDivCode();
	String inspector_div_name	=	table.getInspectorDivName();
	String other_info			=	table.getOtherInfo();
	String bad_percentage		=	table.getBadPercentage();
	String factory_code			=	table.getFactoryCode();
	String factory_name			=	table.getFactoryName();
	String pre_request_no		=	table.getPreRequestNo();
	String serial_no_s			= table.getSerialNoS();
	String serial_no_e			= table.getSerialNoE();
	String item_type			= table.getItemType();

	if(inspection_result.equals("PASS")) inspection_result = "합격";
	else if(inspection_result.equals("REWORK")) inspection_result = "재작업";
	else if(inspection_result.equals("FAIL")) inspection_result = "폐기";
	else if(inspection_result.equals("RETURN")) inspection_result = "반품";

	ArrayList inspection_item_list = new ArrayList();
	inspection_item_list = (ArrayList)request.getAttribute("INSPECTION_ITEM_LIST");
	Iterator table_iter = inspection_item_list.iterator();

	//링크 문자열 가져오기
	redirect = new QualityCtrlLinkUrl();
	redirect = (QualityCtrlLinkUrl)request.getAttribute("Redirect");
	
	String link_list = redirect.getLinkList();
%>

<html>
<head><title>품목검사결과</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../qc/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form method=post name="writeForm" action='../servlet/QualityCtrlServlet' enctype='multipart/form-data' style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
  <TR><TD align="middle"><!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../qc/images/pop_item_chkresult.gif"  alt="품목검사결과" border='0' align='absmiddle'></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE></TD></TR>

	<TR><TD height=32 align="middle"><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0 width="98%">
			<TBODY>
			<TR>
			  <TD align=left width=5>&nbsp;</TD>
			  <TD align=left width=100%>
<% if(!bad_item_quantity.equals("0")){	%>			  
			  <a href="javascript:view_failure_info('<%=request_no%>','<%=item_code%>');"><img src="../qc/images/bt_bad_info.gif" border="0" align="absmiddle" alt="불량내역보기"></a>
<% }	%>
			  <a href="javascript:self.close();"><img src="../qc/images/bt_close.gif" border="0" align='absmiddle'></a></TD>
			  <TD align=left width=100></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD align="middle">
		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
			<TR><TD height="2" bgcolor="#9CA9BA"></TD></TR></TABLE></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center"><!--검사의뢰정보-->
	<table cellspacing=0 cellpadding=2 width="98%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../qc/images/title_chkreq_info.gif" border="0" alt="검사의뢰정보"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사의뢰번호</td>
           <td width="37%" height="25" class="bg_04"><%=request_no%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사대상품목</td>
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
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">공급업체명</td>
           <td width="37%" height="25" class="bg_04">[<%=supplyer_code%>] <%=supplyer_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">
<%
	if(item_type.equals("M") || item_type.equals("1") || item_type.equals("2")) out.print("작업지시번호");
	else out.print("구매입고번호");
%>		   
		   </td>
           <td width="37%" height="25" class="bg_04"><%=lot_no%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">품목수량</td>
           <td width="37%" height="25" class="bg_04"><%=lot_quantity%> EA</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사의뢰자</td>
           <td width="37%" height="25" class="bg_04"><%=requester_info%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사의뢰일자</td>
           <td width="37%" height="25" class="bg_04"><%=request_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table></td></tr>

  <tr><td align="middle"> <!--검사결과-->
	<table cellspacing=0 cellpadding=2 width="98%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../qc/images/title_chkitem_info.gif"  border="0" alt="검사결과"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사수량</td>
           <td width="37%" height="25" class="bg_04"><%=inspected_quantity%> EA</td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">불량수량</td>
           <td width="37%" height="25" class="bg_04"><%=bad_item_quantity%> EA</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사결과</td>
           <td width="37%" height="25" class="bg_04"><%=inspection_result%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">불량율</td>
           <td width="37%" height="25" class="bg_04"><%=bad_percentage%> %</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<% if(item_type.equals("M") || item_type.equals("1") || item_type.equals("2")){	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">일련번호</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=serial_no_s%> ~ <%=serial_no_e%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<%	}	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">특이사항</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=other_info%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사자</td>
           <td width="37%" height="25" class="bg_04"><%=inspector_info%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사일자</td>
           <td width="37%" height="25" class="bg_04"><%=inspect_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table></td></tr>
  <tr><td align="middle" valign="top" height="200"><!--검사항목 -->
	  <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0><form name='listForm' style='magrgin:0'>
        <TBODY>
	        <tr><td height="25" colspan="19"><img src="../qc/images/title_chkitem_info.gif"  border="0" alt="검사항목"></td></tr>
			<TR height=3><TD colspan=19></TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
			<TR vAlign=middle height=22>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>검사코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>검사항목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>검사방식</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>중요도</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>검사수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>합격판정수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=95 align=middle class='list_title'>불합격판정수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>검사결과보기</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table2 = (InspectionItemByItemTable)table_iter.next();
		String sampled_quantity = table2.getSampledQuantity()==null?"":table2.getSampledQuantity();
		String good_quantity = table2.getGoodQuantity()==null?"":table2.getGoodQuantity();
		String bad_quantity = table2.getBadQuantity()==null?"":table2.getBadQuantity();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table2.getInspectionCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table2.getInspectionName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table2.getInspectionTypeName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table2.getInspectionGrade()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=sampled_quantity%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=good_quantity%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bad_quantity%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:view_detail('<%=request_no%>','<%=item_code%>','<%=table2.getInspectionCode()%>','<%=sampled_quantity%>')"><IMG src='../qc/images/bt_view_chk_result.gif' border='0' align='absmiddle' alt='검사결과보기'></a></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=19 background="../qc/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
	if(no == 1) out.print("<TR><TD height='30' colSpan=19 align='middle' class='list_bg'>▒▒▒ 검사된 항목이 없습니다. ▒▒▒</TD></TR>");
%>
		</TBODY></TABLE></td></tr>
		
		
  <TR><TD align="left"><!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"> 
		    <IMG src='../qc/images/bt_close.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD>
          </TR>
          <TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR>
        </TBODY></TABLE></TD></TR></TABLE>		

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
	var lot_quantity = f.lot_quantity.value;

	if(f.inspected_quantity.value > lot_quantity){
		alert("검사수량이 검사의뢰 품목수량보다 클 수 없습니다.");
		f.inspected_quantity.focus();
		return;	
	}

	if(f.bad_item_quantity.value > f.inspected_quantity.value){
		alert("불량수량이 검사수량보다 클 수 없습니다.");
		f.bad_item_quantity.focus();
		return;	
	}

	if(f.inspected_quantity.value <= 0){
		alert("검사수량을 입력하십시오.");
		f.inspected_quantity.focus();
		return;
	}

	if(f.inspection_result.value == ''){
		alert("검사결과를 선택하십시오.");
		return;
	}

	var result = f.inspection_result.value == 'PASS'?'합격':'불합격';

	var msg = "검사결과를 전송하기 전에 입력된 내용을 다시한번 확인하십시오.\n\n";
	msg += "*검사품목코드:" + f.item_code.value + "\n";
	msg += "*검사대상수량:" + f.lot_quantity.value + "\n";
	msg += "*검사실시수량:" + f.inspected_quantity.value + "\n";
	msg += "*불량품수량   :" + f.bad_item_quantity.value + "\n";
	msg += "*불량율         :" + f.bad_percentage.value + "(%)\n";
	msg += "*검사결과      :" + result + "\n\n";
	msg += "검사결과를 저장하시겠습니까?";

	if(confirm(msg)) f.submit();
}

/**********************
 * 발주금액 계산
 **********************/
function cal_bad_percentage() 
{ 
	var f = document.writeForm;
	var inspected_quantity = f.inspected_quantity.value;
	var bad_quantity = f.bad_item_quantity.value;
	var bad_percentage = bad_quantity / inspected_quantity * 100;

	f.bad_percentage.value = myRound(bad_percentage,2);
}

function myRound(num, pos) { 
	var posV = Math.pow(10, (pos ? pos : 2))
	return Math.round(num*posV)/posV
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 650;
	item_list.style.height = div_h;

}

//검사내역 보기
function view_detail(request_no,item_code,inspection_code,sampled_quantity){
	var f = document.writeForm;

	var url = "../servlet/QualityCtrlServlet?mode=view_inspection_value&request_no="+request_no+"&item_code=" + item_code + "&inspection_code=" + inspection_code + "&sampled_quantity=" + sampled_quantity;
	wopen(url,'write_detail','350','300','scrollbars=yes,toolbar=no,status=no,resizable=no');
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

//불량정보 보기
function view_failure_info(request_no,item_code){
	var f = document.writeForm;

	var url = "../servlet/QualityCtrlServlet?mode=view_failure_info&request_no="+request_no+"&item_code=" + item_code;
	wopen(url,'view_failure_info','600','340','scrollbars=yes,toolbar=no,status=no,resizable=no');
}
</script>