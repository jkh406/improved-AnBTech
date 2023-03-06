<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MPS 편집하기"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
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

	//권한알아보기 USER:담당자,MGR:관리자
	String GRADE_mgr = (String)request.getAttribute("GRADE_mgr");	
	String GRADE = "N";
	if(GRADE_mgr.indexOf("MGR") != -1) GRADE = "Y";

	//----------------------------------------------------
	//	입력/수정 정보 읽기
	//----------------------------------------------------
	String pid="",mps_no="",order_no="",mps_type="",model_code="",model_name="",fg_code="";
	String item_code="",item_name="",item_spec="",plan_date="",plan_count="",item_unit="",mps_status="";
	String factory_no="",factory_name="",reg_date="",reg_id="",reg_name="",order_comp="";
	String app_date="",app_id="";

	com.anbtech.mm.entity.mpsMasterTable item;
	item = (mpsMasterTable)request.getAttribute("ITEM_List");

	pid = item.getPid();
	mps_no = item.getMpsNo();
	order_no = item.getOrderNo();
	mps_type = item.getMpsType();
	model_code = item.getModelCode();
	model_name = item.getModelName();
	fg_code = item.getFgCode();

	item_code = item.getItemCode();
	item_name = item.getItemName();
	item_spec = item.getItemSpec();
	plan_date = item.getPlanDate();			if(plan_date.length() ==0) plan_date = anbdt.getDate(0);
	plan_count = Integer.toString(item.getPlanCount());
	item_unit = item.getItemUnit();
	mps_status = item.getMpsStatus();		if(mps_status.length() ==0) mps_status = "0";

	factory_no = item.getFactoryNo();
	factory_name = item.getFactoryName();
	reg_date = item.getRegDate();			if(reg_date.length() ==0) reg_date = anbdt.getDate();
	reg_id = item.getRegId();				if(reg_id.length() ==0) reg_id = sl.id;
	reg_name = item.getRegName();			if(reg_name.length() ==0) reg_name = sl.name;
	app_date= anbdt.getSepDate(item.getAppDate(),"-");
	app_id = item.getAppId();
	order_comp = item.getOrderComp();

	//----------------------------------------------------
	//	편집 여부 판단하기
	//----------------------------------------------------
	String icon = "D";						//icon 출력여부
	String rd = "readonly";					//TEXT 수정가능 여부판단하기
	String ab = "disabled";					//선택박스 수정가능 여부판단하기
	if(mps_status.equals("0") || mps_status.equals("1")) { rd=""; ab=""; icon="E";}
%>

<HTML>
<HEAD><TITLE>MPS 편집하기</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=factory_name%> 생산계획등록</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;' >
				<% if(mps_status.equals("0")) {				// 초기등록 %>
					<a href="javascript:sendSave();"><img src="../mm/images/bt_reg.gif" border=0 align='absmiddle'></a>
				<% } else if(mps_status.equals("1")) {		// 편집,상신 %>
					<a href="javascript:sendModify();"><img src="../mm/images/bt_modify.gif" border=0 align='absmiddle'></a>
					<a href="javascript:sendDelete();"><img src="../mm/images/bt_del.gif" border=0 align='absmiddle'></a>
					<a href="javascript:sendRequest();"><img src="../mm/images/bt_sangsin.gif" border=0 align='absmiddle'></a>
				<% } else if(mps_status.equals("2")) {		// 승인,반려 %> 
					<a href="javascript:sendApproval();"><img src="../mm/images/bt_commit_app.gif" border=0 align='absmiddle'></a>
					<a href="javascript:sendCancel();"><img src="../mm/images/bt_reject_app.gif" border=0 align='absmiddle'></a>
				<% } %>
					<a href="javascript:List();"><img src="../mm/images/bt_list.gif" border=0 align='absmiddle'></a>
					<a href="javascript:Process();"><img src="../mm/images/bt_view_d.gif" border=0 align='absmiddle'></a>
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
			<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
				<TBODY>
					<TR><TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">MPS관리번호</TD>
						<TD width="37%" height="25" class="bg_04"><%=mps_no%>
							<INPUT type="hidden" name="mps_no" value="<%=mps_no%>" size="13"></TD>
					    <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산공장명</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="factory_no" value="<%=factory_no%>" size="10" readonly>
							<INPUT class='text_01' type="text" name="factory_name" value="<%=factory_name%>" size="15" readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchFactoryInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></TD>						
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
					<TR><TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산계획구분</TD>
						<TD width="37%" height="25" class="bg_04">
						<SELECT name="mps_type" style=font-size:9pt;color="black"; <%=ab%>>
					<%
						String[] pp_no = {"SP","SO"};
						String[] pp_name = {"판매계획생산","수주생산"};
						String sel = "";
						for(int i=0; i<pp_no.length; i++) {
							if(pp_no[i].equals(mps_type)) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+pp_no[i]+"'>");
							out.println(pp_name[i]+"</option>");
						} 
					%></select></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">수주번호</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT type='text' name='order_no' maxlength='13' value='<%=order_no%>' <%=rd%>></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR><TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">F/G코드</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='fg_code' value='<%=fg_code%>' size='13' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchBomInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">발주업체</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT type='text' name='order_comp' maxlength='50' value='<%=order_comp%>' <%=rd%>></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">모델코드</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='model_code' value='<%=model_code%>' size='13' readonly></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">모델명</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='model_name' value='<%=model_name%>' size='25' readonly></TD>
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
							<INPUT class='text_01' type='text' name='item_spec' value='<%=item_spec%>' size='40' readonly></TD></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산품목명</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='item_name' value='<%=item_name%>' size='13' readonly></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산단위</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='item_unit' value='<%=item_unit%>' size='6' readonly></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산계획일자</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="plan_date" value="<%=anbdt.getSepDate(plan_date,"/")%>" size="10" readonly> 
				<% if(icon.equals("E")) { %>
					<A Href="Javascript:OpenCalendar('plan_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산계획수량</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='plan_count' value='<%=plan_count%>' size='6' <%=rd%>></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">승인자</TD>
						<TD width="37%" height="25" class="bg_04"><%=app_id%></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">승인일자</TD>
						<TD width="37%" height="25" class="bg_04"><%=app_date%></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">진행상태</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
	<%
		String[] status_no = {"1","2","3","4","5","6","7"};
		String[] status_name = {"MPS작성","MPS승인요청","MPS확정","MRP생성","MRP승인","제조진행","제조마감"};
		String status_sel = "";
		for(int i=0; i<status_no.length; i++) {
			if(status_no[i].equals(mps_status)) status_sel = "checked";
			else status_sel = "";
			out.println("<INPUT type='radio' "+status_sel+" value=''>"+status_name[i]);
		} 
	%>					</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					
			</TBODY></TABLE></TD></TR></TABLE>
<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=pid%>'>
<INPUT type='hidden' name='year' value='<%=anbdt.getYear()%>'>
<INPUT type='hidden' name='month' value='<%=anbdt.getMonth()%>'>
</FORM>
</BODY>
</HTML>

<script language=javascript>
<!--
//등록하기
function sendSave()
{
	var f = document.eForm;

	var factory_no = f.factory_no.value;
	if(factory_no == '') { alert('공장번호가 입력되지 않았습니다.'); f.factory_no.focus(); return; }
	var factory_name = f.factory_name.value;
	if(fg_code == '') { alert('공장이름이 입력되지 않았습니다.'); f.factory_name.focus(); return; }
	var fg_code = f.fg_code.value;
	if(fg_code == '') { alert('FG코드가 입력되지 않았습니다.'); f.fg_code.focus(); return; }
	var item_code = f.item_code.value;
	if(item_code == '') { alert('품목코드가 입력되지 않았습니다.'); f.item_code.focus(); return; }
	var plan_date = f.plan_date.value;
	if(plan_date == '') { alert('계획일이 입력되지 않았습니다.'); f.plan_date.focus(); return; }
	var plan_count = f.plan_count.value;
	if(plan_count == '') { alert('계획수량이 입력되지 않았습니다.'); f.plan_count.focus(); return; }
	else if(plan_count == '0') { alert('계획수량이 입력되지 않았습니다.'); f.plan_count.focus(); return; }

	if(isNaN(plan_count)) { alert('계획수량은 숫자만 입력이 가능합니다.'); return; }
	else if(plan_count.indexOf('.') != -1) { alert('계획수량은 정수만 입력이 가능합니다.'); return; }

	var mps_status = '<%=mps_status%>';
	if(mps_status != '0') { alert('초기 등록상태 일때 만 가능합니다.'); return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_save';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//부품 수정하기
function sendModify()
{
	var mps_status = '<%=mps_status%>';
	if(mps_status != '1') { alert('MPS작성상태 일때 만 가능합니다.'); return; }

	var f = document.eForm;
	var plan_count = document.eForm.plan_count.value;
	if(isNaN(plan_count)) { alert('계획수량은 숫자만 입력이 가능합니다.'); return; }
	else if(plan_count.indexOf('.') != -1) { alert('계획수량은 정수만 입력이 가능합니다.'); return; }
	else if(plan_count == '0') { alert('계획수량이 입력되지 않았습니다.'); f.plan_count.focus(); return; }

	var v = confirm('해당내용을 수정하시겠습니까?'); 
	if(v == false) { return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_modify';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//부품 삭제하기
function sendDelete()
{
	var mps_status = '<%=mps_status%>';
	if(mps_status != '1') { alert('MPS작성상태 일때 만 가능합니다.'); return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_delete';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//승인요청하기
function sendRequest()
{
	var mps_status = '<%=mps_status%>';
	if(mps_status != '1') { alert('MPS작성상태 일때 만 가능합니다.'); return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_ask';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//승인확정하기
function sendApproval()
{
	var grade = '<%=GRADE%>';
	if(grade == 'N') { alert('승인확정 권한이 없습니다.'); return; }

	var mps_status = '<%=mps_status%>';
	if(mps_status != '2') { alert('MPS승인요청상태 일때 만 가능합니다.'); return; }

 	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_app';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//승인취소하기
function sendCancel()
{
	var grade = '<%=GRADE%>';
	if(grade == 'N') { alert('승인취소 권한이 없습니다.'); return; }

	var mps_status = '<%=mps_status%>';
	if(mps_status != '2') { alert('MPS승인요청상태 일때 만 가능합니다.'); return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_cancel';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//목록보기
function List()
{
	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='cal_month';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//상세진행상태 보기
function Process() {
	var f = document.eForm;
	var factory_no = f.factory_no.value;
	var mps_no = f.mps_no.value;
	var fg_code = f.fg_code.value;
	var model_code = f.model_code.value;
	var model_name = f.model_name.value;
	
	var para = "factory_no="+factory_no+"&mps_no="+mps_no+"&fg_code="+fg_code;
	para += "&model_code="+model_code+"&model_name="+model_name;

	url = "../servlet/mpsInfoServlet?mode=process_view&"+para;
	wopen(url,'pv','600','265','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//공장정보 찾기
function searchFactoryInfo() {
	var f = document.eForm;
	var factory_no = f.factory_no.name;
	var factory_name = f.factory_name.name;
	
	var para = "field="+factory_no+"/"+factory_name;

	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&"+para;
	wopen(url,'enterCode','400','227','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//FG코드 검색하기
function searchBomInfo(){
	var strUrl = "../gm/openModelInfoWindow.jsp?one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=pd_name&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	
	wopen(strUrl,"search_bominfo",'820','405','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//계획일자 찾기
function OpenCalendar(FieldName) {
	var strUrl = "../mm/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = wopen(strUrl, "Calendar", "200","280","scrollbars=no,toolbar=no,status=no,resizable=no");
}
// 품목정보 가져오기
function searchItemInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_spec&item_unit=item_unit";
	wopen(strUrl,"open_part",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>