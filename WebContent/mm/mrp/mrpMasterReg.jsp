<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MRP 등록"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	String factory_no = (String)request.getAttribute("factory_no"); if(factory_no == null) factory_no = "";
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";

	//----------------------------------------------------
	//	입력/수정 정보 읽기
	//----------------------------------------------------
	String pid="",mps_no="",mrp_no="",mrp_start_date="",mrp_end_date="",model_code="",model_name="";
	String fg_code="",item_code="",item_name="",item_spec="",p_count="",plan_date="",item_unit="";
	String mrp_status="",factory_name="",reg_date="",reg_id="",reg_name="",pu_dev_date="",pu_req_no="";
	String stock_link="",pjt_code="",pjt_name="";

	com.anbtech.mm.entity.mrpMasterTable item;
	item = (mrpMasterTable)request.getAttribute("MRP_master");

	pid = item.getPid();
	mps_no = item.getMpsNo();
	mrp_no = item.getMrpNo();
	mrp_start_date = item.getMrpStartDate();if(mrp_start_date.length() ==0) mrp_start_date = anbdt.getDate(0);
	mrp_end_date = item.getMrpEndDate();
	model_code = item.getModelCode();
	model_name = item.getModelName();
	fg_code = item.getFgCode();

	item_code = item.getItemCode();
	item_name = item.getItemName();
	item_spec = item.getItemSpec();
	p_count = Integer.toString(item.getPCount());
	plan_date = item.getPlanDate();
	item_unit = item.getItemUnit();
	mrp_status = item.getMrpStatus();		if(mrp_status.length() ==0) mrp_status = "S";

	factory_no = item.getFactoryNo();
	factory_name = item.getFactoryName();
	reg_date = item.getRegDate();			if(reg_date.length() ==0) reg_date = anbdt.getDate();
	reg_id = item.getRegId();				if(reg_id.length() ==0) reg_id = sl.id;
	reg_name = item.getRegName();			if(reg_name.length() ==0) reg_name = sl.name;
	pu_dev_date = item.getPuDevDate();		if(pu_dev_date.length() ==0) pu_dev_date = anbdt.getDate(0);
	pu_req_no = item.getPuReqNo();
	stock_link = item.getStockLink();		if(stock_link.length() ==0) stock_link="0";
	pjt_code = item.getPjtCode();
	pjt_name = item.getPjtName();

	//----------------------------------------------------
	//	편집 여부 판단하기
	//----------------------------------------------------
	String icon = "D";						//icon 출력여부
	String rd = "readonly";					//TEXT 수정가능 여부판단하기
	String ab = "disabled";					//선택박스 수정가능 여부판단하기
	String ra = "enable";					//Radio Button 수정가능 판단
	String temp_caption = "";				// 임의소요량산출 시, 
	if(mps_no.length() != 0) {				//MPS접수로 부터
		if(mrp_status.equals("S") || mrp_status.equals("0")) {icon="P"; }
		else ra="disabled";
	} else {								//temp로 부터
		rd=""; ab=""; icon="E";
		//temp_caption = "소요량산출대상";
	}
	
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//메시지 전달
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}

//등록하기
function sendSave()
{
	var mrp_status = '<%=mrp_status%>';
	if(mrp_status != 'S') { alert('MRP접수상태 일때 만 가능합니다.'); return; }

	var f = document.eForm;
	var mrp_start_date = f.mrp_start_date.value;
	if(mrp_start_date == '') { alert('기준일이 입력되지 않았습니다.'); f.mrp_start_date.focus(); return; }
	var p_count = f.p_count.value;
	if(p_count == '') { alert('계획수량이 입력되지 않았습니다.'); f.p_count.focus(); return; }
	else if(p_count == '0') { alert('계획수량이 입력되지 않았습니다.'); f.p_count.focus(); return; }

	if(isNaN(p_count)) { alert('계획수량은 숫자만 입력이 가능합니다.'); return; }
	else if(p_count.indexOf('.') != -1) { alert('계획수량은 정수만 입력이 가능합니다.'); return; }

	var pjt_code = f.pjt_code.value;
	if(pjt_code == '') { alert('과제코드가 입력되지 않았습니다.'); return; }

	//입력메시지 출력
	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력

	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_save';
	document.eForm.submit();
}
//수정하기
function sendModify()
{
	var mrp_status = '<%=mrp_status%>';
	if(mrp_status != '0' && mrp_status != '1') { alert('MRP작성 또는 반려상태 일때 만 가능합니다.'); return; }

	var p_count = document.eForm.p_count.value;
	if(isNaN(p_count)) { alert('계획수량은 숫자만 입력이 가능합니다.'); return; }
	else if(p_count.indexOf('.') != -1) { alert('계획수량은 정수만 입력이 가능합니다.'); return; }

	var pjt_code = document.eForm.pjt_code.value;
	if(pjt_code == '') { alert('과제코드가 입력되지 않았습니다.'); return; }

	var v = confirm('해당내용을 수정하시겠습니까?'); 
	if(v == false) { return; }
	
	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_modify';
	document.eForm.submit();
}
//MRP전개
function mrpCount()
{
	var f = document.eForm;

	var pid = document.eForm.pid.value;
	var mrp_no = document.eForm.mrp_no.value;
	var fg_code = document.eForm.fg_code.value;			
		if(fg_code == '') { alert('FG코드가 입력되지 않았습니다.'); f.fg_code.focus(); return; }
	var item_code = document.eForm.item_code.value;
		if(item_code == '') { alert('품목코드가 입력되지 않았습니다.'); f.fg_code.focus(); return; }
	var mrp_start_date = document.eForm.mrp_start_date.value;
	var mrp_count = document.eForm.p_count.value;
	var factory_no = document.eForm.factory_no.value;
	var mrp_status = document.eForm.mrp_status.value;

	var stock_link = "";
	var nm = eForm.stock_link.length;	
	for(i=0; i<nm; i++) {
		if(eForm.stock_link[i].checked) stock_link = document.eForm.stock_link[i].value;
	}

	var p_count = document.eForm.p_count.value;
	
	var para = "pid="+pid+"&mrp_no="+mrp_no+"&fg_code="+fg_code+"&item_code="+item_code;
	para += "&mrp_start_date="+mrp_start_date+"&mrp_count="+mrp_count+"&factory_no="+factory_no;
	para += "&mrp_status="+mrp_status+"&stock_link="+stock_link+"&p_count="+p_count;

	document.eForm.action='../mm/mrp/mrpItemFrame.jsp?'+para;
	document.eForm.submit();
}
//MRP확정하기
function mrpConfirm()
{
	var mrp_status = '<%=mrp_status%>';
	if(mrp_status == '3') { alert('MRP확정상태 입니다.'); return; }
	else if(mrp_status == '4') { alert('구매발주상태 입니다.'); return; }
	else if(mrp_status == 'S') { alert('MRP접수상태 입니다.'); return; }

	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_confirm';
	document.eForm.submit();
}
//MRP확정 취소하기 
function mrpCancel()
{
	var mrp_status = '<%=mrp_status%>';
	if(mrp_status != '3') { alert('MRP확정상태 일때 만 가능합니다.'); return; }

	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_cancel';
	document.eForm.submit();
}
//목록보기
function List()
{
	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_list';
	document.eForm.submit();
}


//FG코드 검색하기
function searchBomInfo(){

	var strUrl = "../gm/openModelInfoWindow.jsp?one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=pd_name&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	
	wopen(strUrl,"search_bominfo",'820','405','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//계획일자 찾기
function OpenCalendar(FieldName) {
	var strUrl = "../mm/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}
// 품목정보 가져오기
function searchItemInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_spec&item_unit=item_unit";
	wopen(strUrl,"open_part",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}
// 과제찾기
function searchProject() {
	
	para = "&target=eForm.pjt_code/eForm.pjt_name";	wopen('../servlet/PsmProcessServlet?mode=search_project'+para,'search_pjt','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=factory_name%> MRP 등록</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
						<TD align=left width='20%' style='padding-left:5px;'>
				<% 
					if(mps_no.length() == 0) {			//임의 소요량 
						out.println("<a href='javascript:mrpCount();'><img src='../mm/images/bt_mrp_unfolding.gif' border='0' alt='MRP전개' align='absmiddle'></a>");
				 }  else {								//MPS에 의한 정식 
					if(mrp_status.equals("S"))	{		//초기 등록
						out.println("<a href='javascript:sendSave();'><img src='../mm/images/bt_reg.gif' border=0 align='absmiddle'></a>");
					} else if(mrp_status.equals("0") || mrp_status.equals("1")) { //수정,전개,확정
						out.println("<a href='javascript:sendModify();'><img src='../mm/images/bt_modify.gif' border=0 align='absmiddle'></a>");
						out.println("<a href='javascript:mrpCount();'><img src='../mm/images/bt_mrp_unfolding.gif' border=0 alt='MRP전개' align='absmiddle'></a>");
						out.println("<a href='javascript:mrpConfirm();'><img src='../mm/images/bt_mrp_confirm.gif' border=0 alt='MRP확정' align='absmiddle'></a>");
					} else if(mrp_status.equals("2")) {	//사용하지 않음
					} else if(mrp_status.equals("3")) {	//MRP보기,MRP확정취소
						out.println("<a href='javascript:mrpCount();'><img src='../mm/images/bt_mrp_searching.gif' border=0 alt='MRP조회' align='absmiddle'></a>");
						out.println("<a href='javascript:mrpCancel();'><img src='../mm/images/bt_mrp_cancel.gif' border=0 alt='MRP취소' align='absmiddle'></a>");
					} else {							//MRP보기
						out.println("<a href='javascript:mrpCount();'><img src='../mm/images/bt_mrp_searching.gif' border=0 alt='MRP조회' align='absmiddle'></a>");
					}
				} %>
					<a href="javascript:List();"><img src="../mm/images/bt_list.gif" border=0 align='absmiddle'></a>
					</TD>
			</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
	<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
		<TR>
			<TD align="center">
				<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
					<TBODY>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">MRP관리번호</TD>
						<TD width="37%" height="25" class="bg_04" colspan='3'><%=mrp_no%>
							<INPUT type="hidden" name="mrp_no" value="<%=mrp_no%>" size="20"></TD>
						</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산공장코드</TD>
						<TD width="37%" height="25" class="bg_04"><%=factory_no%></TD>			
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산공장명</TD>
						<TD width="37%" height="25" class="bg_04"><%=factory_name%></TD>			
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">작성자</TD>
						<TD width="37%" height="25" class="bg_04"><%=reg_name%>/<%=reg_id%>
							<INPUT type="hidden" name="reg_id" value="<%=reg_id%>" size="30">
							<INPUT type="hidden" name="reg_name" value="<%=reg_name%>" size="13">
						</TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">작성일</TD>
						<TD width="37%" height="25" class="bg_04"><%=reg_date%>
							<INPUT type="hidden" name="reg_date" value="<%=reg_date%>" size="30"></TD>		
					</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height='5' colspan='4'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">F/G코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='fg_code' value='<%=fg_code%>' size='13'  readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchBomInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">MPS관리번호</TD>
			   <TD width="37%" height="25" class="bg_04"><%=mps_no%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">모델코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='model_code' value='<%=model_code%>' size='13' readonly></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">모델명</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='model_name' value='<%=model_name%>' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산품목코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='item_code' value='<%=item_code%>' size='13' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchItemInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산품목설명</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='item_spec' value='<%=item_spec%>' size='40' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산품목명</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='item_name' value='<%=item_name%>' readonly></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산단위</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='item_unit' value='<%=item_unit%>'  size='6' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">BOM기준일자</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type="text" name="mrp_start_date" value="<%=anbdt.getSepDate(mrp_start_date,"/")%>" size="10" readonly> 
				<% if(icon.equals("E") || icon.equals("P")) { %>
					<A Href="Javascript:OpenCalendar('mrp_start_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산계획수량</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='p_count' value='<%=p_count%>' <%=rd%>  size='6' ></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<!--기타정보 
			<TR>
				<TD height="25" colspan="4"><IMG src='../mm/images/title_eqinfo.gif' border='0'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>-->
			<TR><TD height='5' colspan='4'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">부품재고연계</TD>
			   <TD width="87%" height="25" class="bg_04" colspan=3>
			   <%	String sel = "";
					String[] sl_data = {"고려함","고려하지않음"};
					String[] sl_value = {"1","0"};
					for(int i=0; i<sl_data.length; i++) {
						if(stock_link.equals(sl_value[i])) sel="checked";
						else sel = "";
						out.print("<INPUT type='radio' name='stock_link' value='"+sl_value[i]+"' ");
						out.println(sel+" "+ra+">"+sl_data[i]);
					}
				%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">구매입고희망일</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type="text" name="pu_dev_date" value="<%=anbdt.getSepDate(pu_dev_date,"/")%>" size="10" readonly> 
				<% if(!mrp_status.equals("4") & !mrp_status.equals("3")) { %>
					<A Href="Javascript:OpenCalendar('pu_dev_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">구매요청번호</TD>
			   <TD width="37%" height="25" class="bg_04"><%=pu_req_no%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">과제코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type="text" name="pjt_code" value="<%=pjt_code%>" size="20" readonly> 
				<% if(!mrp_status.equals("4") & !mrp_status.equals("3")) { %>
					<A Href="Javascript:searchProject();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></A>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">과제명</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type="text" name="pjt_name" value="<%=pjt_name%>" size="20" readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">진행상태</TD>
			   <TD width="87%" height="25" class="bg_04" colspan=3>
				<%
					String[] status_no = {"0","1","3","4"};
					String[] status_name = {"결재반려","MRP등록","MRP확정(구매등록)","구매발주"};
					String status_sel = "";
					for(int i=0; i<status_no.length; i++) {
						if(status_no[i].equals(mrp_status)) status_sel = "checked";
						else status_sel = "";
						out.println("<INPUT type='radio' "+status_sel+" value='' disabled>"+status_name[i]);
					} 
				%>		</TD>
			</TR>
			
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY></TABLE></TD></TR></TABLE>

<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=pid%>'>
<INPUT type='hidden' name='mps_no' value='<%=mps_no%>'>
<INPUT type='hidden' name='factory_no' value='<%=factory_no%>'>
<INPUT type='hidden' name='factory_name' value='<%=factory_name%>'>
<INPUT type='hidden' name='mrp_status' value='<%=mrp_status%>'>
<INPUT type='hidden' name='plan_date' value='<%=plan_date%>'>
</FORM>

<DIV id="lding" style="position:absolute;left:200px;top:110px;width:224px;height:150px;visibility:hidden;">
	<img src='../mm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</BODY>
</HTML>

