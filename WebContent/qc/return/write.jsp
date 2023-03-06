<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkQC02.jsp"%>
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
			  <TD valign='middle' class="title"><img src="../qc/images/blet.gif"> 품목재검사결과등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=100% style='padding-left:5px;'><a href="javascript:checkForm();"><img src="../qc/images/bt_justice.gif" border="0" align="absmiddle" alt="최종판정"></a> <a href="javascript:write_failure_info('<%=request_no%>','<%=item_code%>');"><img src="../qc/images/bt_badinfo_reg.gif" border="0" align="absmiddle" alt="불량내역등록"></a> <a href="javascript:go_list('<%=link_list%>');"><img src="../qc/images/bt_list.gif" border="0" align="absmiddle" alt="목록보기"></a></TD><TD align=left width=100><%=pre_request_no%></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>
 
<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center"><!--검사의뢰정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../qc/images/title_chkreq_info.gif"  border="0" alt="검사의뢰정보"></td></tr>
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
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">전작업지시번호</td>
           <td width="37%" height="25" class="bg_04"><%=lot_no%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">재검사대상수량</td>
           <td width="37%" height="25" class="bg_04"><%=lot_quantity%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">일련번호</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=serial_no_s%> ~ <%=serial_no_e%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사의뢰자</td>
           <td width="37%" height="25" class="bg_04"><%=requester_info%></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사의뢰일자</td>
           <td width="37%" height="25" class="bg_04"><%=request_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table></td></tr>

  <tr><td align="center"><!--검사결과-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../qc/images/title_chkresult_info.gif"  border="0" alt="검사결과"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">재검사수량</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='inspected_quantity' value='<%=lot_quantity%>' class="text_01" onKeyPress="currency(this);" onBlur="cal_bad_percentage();" maxlength="5" onFocus="chk_inspected_quantity('<%=item_type%>');"></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">불량수량</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='bad_item_quantity' value='<%=bad_item_quantity%>' class="text_01" onKeyPress="currency(this);" onBlur="cal_bad_percentage();" maxlength="5"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">재검사결과</td>
           <td width="37%" height="25" class="bg_04">
			<select name="inspection_result" class="text_01">
				<option value="">선택</option>
				<option value="PASS">합격</option>
				<option value="REWORK">재작업</option>
				<option value="FAIL">폐기</option>
			</select>
			<%	if(!inspection_result.equals("")){	%>
					<script language='javascript'>
						document.writeForm.inspection_result.value = '<%=inspection_result%>';
					</script>
				<%	}	%>			
			</td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">불량율(%)</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='bad_percentage' value='<%=bad_percentage%>' class="text_01" onKeyPress="currency(this);" readOnly></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
		   <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">특이사항</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type="text" name="other_info" value="<%=other_info%>" size="70" maxlength="50"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>		 
         <tr><td height=10 colspan="4"></td></tr></tbody></table></td></tr>

  <tr><td align="center"><!--검사항목 -->
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0><form name='listForm' style='magrgin:0'>
        <TBODY>
	        <TR><TD height="30" colspan="19"><img src="../qc/images/title_chkitem_info.gif" border="0" alt="검사항목정보"></TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
			<TR vAlign=middle height=22>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>검사항목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>검사항목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>검사방식</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>중요도</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>재검사대상수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>합격판정수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=95 align=middle class='list_title'>불합격판정수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>재검사결과등록</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	int no = 1;
	String inspection_items = "";	// 정의된 검사항목코드들
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
			  <TD align=middle class='list_bg'><input type='text' size='5' maxlength='3' name='sampled_quantity_<%=table2.getInspectionCode()%>' onKeyPress="currency(this);" value='<%=sampled_quantity%>' class="text_01"></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><input type='text' size='5' maxlength='3' name='good_quantity_<%=table2.getInspectionCode()%>' onKeyPress="currency(this);" value='<%=good_quantity%>' class="text_01" readOnly></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><input type='text' size='5' maxlength='3' name='bad_quantity_<%=table2.getInspectionCode()%>' onKeyPress="currency(this);" value='<%=bad_quantity%>' class="text_01" readOnly></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:write_detail('<%=request_no%>','<%=item_code%>','<%=table2.getInspectionCode()%>')">
			  <IMG src='../qc/images/bt_reg_rechk_result.gif' border='0' align='absmiddle' alt='재검사결과등록'></a></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=19 background="../qc/images/dot_line.gif"></TD></TR>
<%
		inspection_items += table2.getInspectionCode() + ",";
		no++;
	}
	if(no == 1) out.print("<TR><TD height='30' colSpan=19 align='middle' class='list_bg'>▒▒▒ 이 품목에는 수행할 검사항목이 정의되어 있지 않습니다. ▒▒▒</TD></TR>");
%>
		</TBODY></TABLE></td></tr></table>

<input type="hidden" name="mode" value="<%=mode%>">
<input type="hidden" name="request_no" value="<%=request_no%>">
<input type="hidden" name="item_code" value="<%=item_code%>">
<input type="hidden" name="item_name" value="<%=item_name%>">
<input type="hidden" name="item_desc" value="<%=item_desc%>">
<input type="hidden" name="factory_code" value="<%=factory_code%>">
<input type="hidden" name="factory_name" value="<%=factory_name%>">
<input type="hidden" name="lot_quantity" value="<%=lot_quantity%>">
<input type="hidden" name="inspection_items" value="<%=inspection_items%>">
<input type="hidden" name="item_type" value="<%=item_type%>">
<input type="hidden" name="result_str">
<input type="hieen" name="serial_no_s" value="<%=serial_no_s%>">
<input type="hidden" name="serial_no_e" value="<%=serial_no_e%>">
<input type="hidden" name="lot_no" value="<%=lot_no%>">
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

	var inspected = f.inspected_quantity.value;
	var lot		  = f.lot_quantity.value;
	var bad		  = f.bad_item_quantity.value;
	
	if(Number(inspected) > Number(lot)){
		alert("검사수량이 반품수량보다 클 수 없습니다.");
		f.inspected_quantity.focus();
		return;	
	}

	if(Number(bad) > Number(inspected)){
		alert("불량수량(="+f.bad_item_quantity.value+")이 검사수량(="+f.inspected_quantity.value+")보다 클 수 없습니다.");
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

	if(f.lot_no.value == ''){
		alert("작업지시번호를 입력하십시오.");
		f.lot_no.focus();
		return;
	}

	var result = f.inspection_result.value;
	if(result == 'PASS') result = '합격';
	else if(result == 'REWORK') result = '재작업';
	else if(result == 'FAIL') result = '폐기';
	else if(result == 'RETURN') result = '반품';

	//검사항목 입력체크 및 검사결과문자열 생성
	var inspection_item = f.inspection_items.value.split(",");
	var inspection_result = "";
	for(var i=0; i<inspection_item.length-1; i++){
		if(eval("f.sampled_quantity_" + inspection_item[i] + ".value;") == ''){
			alert("검사항목코드 " + inspection_item[i] + "의 시료수량을 입력하십시오.");
			eval("f.sampled_quantity_" + inspection_item[i] + ".focus();");
			return;
		}

		if(eval("f.good_quantity_" + inspection_item[i] + ".value;") == ''){
			alert("검사항목코드 " + inspection_item[i] + "의 합격판정수량을 입력하십시오. 먼저 검사내역을 등록하십시오.");
//			eval("f.good_quantity_" + inspection_item[i] + ".focus();");
			return;
		}

		if(eval("f.bad_quantity_" + inspection_item[i] + ".value;") == ''){
			alert("검사항목코드 " + inspection_item[i] + "의 불합격판정수량을 입력하십시오. 먼저 검사내역을 등록하십시오.");
//			eval("f.bad_quantity_" + inspection_item[i] + ".focus();");
			return;
		}
			
		inspection_result += inspection_item[i] + "|" + eval("f.sampled_quantity_" + inspection_item[i] + ".value;") + "|" + eval("f.good_quantity_" + inspection_item[i] + ".value;") + "|" + eval("f.bad_quantity_" + inspection_item[i] + ".value;") + ",";
	}
	f.result_str.value = inspection_result;

	var msg = "검사결과를 전송하기 전에 입력된 내용을 다시한번 확인하십시오.\n\n";
	msg += "*검사품목코드:" + f.item_code.value + "\n";
	msg += "*검사대상수량:" + f.lot_quantity.value + "\n";
	msg += "*검사실시수량:" + f.inspected_quantity.value + "\n";
	msg += "*불량품수량   :" + f.bad_item_quantity.value + "\n";
	msg += "*불량율         :" + f.bad_percentage.value + "(%)\n";
	msg += "*검사결과      :" + result + "\n\n";
	msg += "검사결과를 저장하시겠습니까?";

	if(confirm(msg)){
		document.onmousedown=dbclick;
		f.submit();
	}
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}

// 불량율 계산
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

//검사내역 등록
function write_detail(request_no,item_code,inspection_code){
	var f = document.writeForm;

	if(eval("f.sampled_quantity_" + inspection_code + ".value;") == ''){
		alert("검사수량을 먼저 입력하십시오.");
		eval("f.sampled_quantity_" + inspection_code + ".focus();");
		return;
	}
	
	var sampled_quantity = eval("f.sampled_quantity_" + inspection_code + ".value;");
	var url = "../servlet/QualityCtrlServlet?mode=write_inspection_value&request_no="+request_no+"&item_code=" + item_code + "&inspection_code=" + inspection_code + "&sampled_quantity=" + sampled_quantity;
	wopen(url,'write_detail','350','300','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//불량정보 등록
function write_failure_info(request_no,item_code){
	var f = document.writeForm;

	var bad_quantity = f.bad_item_quantity.value;
	if(bad_quantity == '0'){
		alert("불량품이 없거나 검사된 항목이 없을 경우에는 불량내역을 등록할 수 없습니다.");
		f.bad_item_quantity.focus();
		return;
	}

	var bad_goods_serial_no_s = f.serial_no_s.value;
	var bad_goods_serial_no_e = f.serial_no_e.value;
	
	var url = "../servlet/QualityCtrlServlet?mode=write_failure_info&request_no="+request_no+"&item_code=" + item_code + "&bad_quantity=" + bad_quantity + "&serial_no_s=" + bad_goods_serial_no_s + "&serial_no_e=" + bad_goods_serial_no_e;

	wopen(url,'write_failure_info','600','400','scrollbars=yes,toolbar=no,status=no,resizable=no');
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

// 검사수량 체크
function chk_inspected_quantity(item_type) 
{ 
	var f = document.writeForm;
	if(item_type == 'M' || item_type == '1' || item_type == '2'){
		alert("완(반)제품은 전수검사 대상입니다. 검사수량을 임의로 수정할 수 없습니다.");
		f.inspected_quantity.blur();
		return;
	}

}
</script>