<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MFG 관리하기"		
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
	
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	String title_caption = "생산작업계획 등록";

	//----------------------------------------------------
	//	입력/수정 정보 읽기
	//----------------------------------------------------
	String pid="",mrp_no="",mfg_no="",model_code="",model_name="",fg_code="",item_code="",item_name="";
	String item_spec="",item_unit="",mfg_count="",buy_type="",factory_no="",factory_name="";
	String comp_code="",comp_name="",comp_user="",comp_tel="",order_status="",order_type="";
	String reg_date="",reg_id="",reg_name="",plan_date="",order_start_date="",order_end_date="";
	String re_work="",link_mfg_no="",order_type_name="",order_date="";
	String rst_total_count="",rst_good_count="",rst_bad_count="",working_count="";
	int yy=0,mm=0,dd=0;

	com.anbtech.mm.entity.mfgMasterTable item;
	item = (mfgMasterTable)request.getAttribute("MFG_master");

	pid = item.getPid();
	mrp_no = item.getMrpNo();
	mfg_no = item.getMfgNo();
	model_code = item.getModelCode();
	model_name = item.getModelName();
	fg_code = item.getFgCode();
	item_code = item.getItemCode();
	item_name = item.getItemName();
	item_spec = item.getItemSpec();
	item_unit = item.getItemUnit();
	mfg_count = Integer.toString(item.getMfgCount());
	buy_type = item.getBuyType();			if(buy_type.length() ==0) buy_type = "M";
	factory_no = item.getFactoryNo();
	factory_name = item.getFactoryName();
	comp_code = item.getCompCode();
	comp_name = item.getCompName();
	comp_user = item.getCompUser();
	comp_tel = item.getCompTel();
	order_status = item.getOrderStatus();
	order_type = item.getOrderType();
	reg_date = item.getRegDate();			if(reg_date.length() ==0) reg_date = anbdt.getDate();
	reg_id = item.getRegId();				if(reg_id.length() ==0) reg_id = sl.id;
	reg_name = item.getRegName();			if(reg_name.length() ==0) reg_name = sl.name;

	plan_date = item.getPlanDate();	
	if(plan_date.length() != 0) {
		yy = Integer.parseInt(plan_date.substring(0,4));
		mm = Integer.parseInt(plan_date.substring(4,6));
		dd = Integer.parseInt(plan_date.substring(6,8));
	}

	order_start_date = item.getOrderStartDate();
	if(order_start_date.length() ==0) {
		if(plan_date.length() == 0) order_start_date = anbdt.getDate(0);
		else order_start_date = anbdt.getDate(yy,mm,dd,-15);
	}

	order_end_date = item.getOrderEndDate();
	if(order_end_date.length() ==0) {
		if(plan_date.length() == 0) order_end_date = anbdt.getDate(7);
		else order_end_date = anbdt.getDate(yy,mm,dd,-5);
	}
	order_date = item.getOrderDate();
	re_work = item.getReWork(); if(re_work.length() == 0) re_work="작업";
	link_mfg_no = item.getLinkMfgNo(); if(link_mfg_no == null) link_mfg_no="";

	rst_total_count = Integer.toString(item.getRstTotalCount());
	rst_good_count = Integer.toString(item.getRstGoodCount());
	rst_bad_count = Integer.toString(item.getRstBadCount());
	working_count = Integer.toString(item.getWorkingCount());

	if(order_type.equals("MRP")) order_type_name = "MRP오더";
	else if(order_type.equals("MANUAL")) order_type_name = "긴급오더";

	//----------------------------------------------------
	//	편집 여부 판단하기
	//----------------------------------------------------
	String icon = "D";						//icon 출력여부
	String rd = "readonly";					//TEXT 수정가능 여부판단하기
	String ab = "disabled";					//선택박스 수정가능 여부판단하기
	String ra = "enable";					//Radio Button 수정가능 판단
	if(mrp_no.length() != 0) {				//MRP접수로 부터
		if(order_status.equals("") || order_status.equals("1")) {icon="P"; }
		else ra="disabled";
	} else {								//긴급오더로 부터
		if(order_status.equals("") || order_status.equals("1")) {
			rd=""; ab=""; icon="E";
			title_caption = title_caption + " [긴급오더]";
		}
	}

%>

<HTML>
<HEAD><TITLE><%=title_caption%></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">

</HEAD>
<BODY topmargin="0" leftmargin="0" onload='javascript:InitLoad();'>
<FORM name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=title_caption%></TD>
				</TR>
				</TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
				<% if(order_status.length() == 0)	{		//초기 등록
						out.println("<a href='javascript:sendSave();'><img src='../mm/images/bt_reg.gif' border=0 align='absmiddle'></a>");
					} else if(order_status.equals("1")) { //수정,삭제,공정생성
						out.println("<a href='javascript:sendModify();'><img src='../mm/images/bt_modify.gif' border=0 align='absmiddle'></a>");
						if(mrp_no.length() == 0) {		//긴급오더등록시만 삭제가능
							out.println("<a href='javascript:sendDelete();'><img src='../mm/images/bt_del.gif' border=0 align='absmiddle'></a>");
						}
						out.println("<a href='javascript:opCreate();'><img src='../mm/images/bt_op_create.gif' border=0 alt='공정생성' align='absmiddle'></a>");
					} else if(order_status.equals("2")) {	//공정계획,부품조정
						out.println("<a href='javascript:opPlan();'><img src='../mm/images/bt_op_plan.gif' border=0 alt='공정계획' align='absmiddle'></a>");
						out.println("<a href='javascript:itemPlan();'><img src='../mm/images/bt_item_plan.gif' border=0 alt='부품조정' align='absmiddle'></a>");
					} else {
						out.println("<a href='javascript:opPlan();'><img src='../mm/images/bt_op_research.gif' border=0 alt='공정조회' align='absmiddle'></a>");
						out.println("<a href='javascript:itemPlan();'><img src='../mm/images/bt_item_research.gif' border=0 alt='부품조회' align='absmiddle'></a>");
					}
				%>
					<a href="javascript:List();"><img src="../mm/images/bt_list.gif" border=0 align='absmiddle'></a>
					</TD>
				</TR></TBODY></TABLE></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<td align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">작업지시번호</td>
			   <td width="87%" height="25" class="bg_04" colspan='3'><%=mfg_no%>
					<input type="hidden" name="mfg_no" value="<%=mfg_no%>" size="20"></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산공장번호</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='factory_no' value='<%=factory_no%>' size='13' readonly>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산공장명</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='factory_name' value='<%=factory_name%>'  size='13' readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">작성자</td>
			   <td width="37%" height="25" class="bg_04"><%=reg_id%>/<%=reg_name%>
					<input type="hidden" name="reg_name" value="<%=reg_name%>" size="20">
					<input type="hidden" name="reg_id" value="<%=reg_id%>" size="30">
				</td>
				<td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">작성일</td>
			   <td width="37%" height="25" class="bg_04"><%=reg_date%>
					<input type="hidden" name="reg_date" value="<%=reg_date%>" size="10"></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR><td height=5></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">F/G코드</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='fg_code' value='<%=fg_code%>' size='13' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchBomInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">MRP관리번호</td>
			   <td width="37%" height="25" class="bg_04"><%=mrp_no%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산모델코드</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='model_code' value='<%=model_code%>'  size='15' readonly></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산모델명</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='model_name' value='<%=model_name%>' size='20'  readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산품목코드</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='item_code' value='<%=item_code%>' size='13' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchItemInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산품목설명</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='item_spec' value='<%=item_spec%>' size='40' readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산품목명</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='item_name' value='<%=item_name%>' size='13' readonly></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산품목단위</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='item_unit' value='<%=item_unit%>' size='6'  readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산수량</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='mfg_count' value='<%=mfg_count%>' <%=rd%> size='6'></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">지시구분</td>
			   <td width="37%" height="25" class="bg_04"><%=order_type_name%>
					<input type='hidden' name='order_type' value='<%=order_type%>' readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">MPS계획일자</td>
			   <td width="37%" height="25" class="bg_04"><%=anbdt.getSepDate(plan_date,"-")%>
					<input type='hidden' name='plan_date' value='<%=plan_date%>'></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">조달구분</td>
			   <td width="37%" height="25" class="bg_04">
				<%	String sel = "";
					String[] sl_data = {"사내가공품","외주가공품","구매품"};
					String[] sl_value = {"M","O","P"};
					for(int i=0; i<sl_data.length; i++) {
						if(buy_type.equals(sl_value[i])) sel="checked";
						else sel = "";
						out.print("<input type='radio' name='buy_type' value='"+sl_value[i]+"' ");
						out.println(sel+" "+ra+">"+sl_data[i]);
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">착수예정일</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type="text" name="order_start_date" value="<%=order_start_date%>" size="10" readonly> 
				<% if(icon.equals("E") || icon.equals("P")) { %>
					<A Href="Javascript:OpenCalendar('order_start_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">완료예정일</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='order_end_date' value='<%=order_end_date%>'  size='10' readonly>
				<% if(icon.equals("E") || icon.equals("P")) { %>
					<A Href="Javascript:OpenCalendar('order_end_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">계획확정일</td>
			   <td width="37%" height="25" class="bg_04"><%=anbdt.getSepDate(order_date,"-")%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">작업구분</td>
			   <td width="37%" height="25" class="bg_04">
				<%	String re_sel = "";
					String[] re_data = {"작업","재작업"};
					for(int i=0; i<re_data.length; i++) {
						if(re_work.equals(re_data[i])) re_sel="checked";
						else re_sel = "";
						out.print("<input type='radio' name='re_work' value='"+re_data[i]+"' ");
						out.print("onClick='addInputForm();' ");
						out.println(re_sel+" "+ra+">"+re_data[i]);
					}
				%><span id='ad'></span></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">외주처 담당자</td>
			   <td width="37%" height="25" class="bg_04">
					<input type='text' name='comp_user' value='<%=comp_user%>' size='10' readonly>
				<% if(icon.equals("E") || icon.equals("P")) { %>
					<a href="javascript:searchCompInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">외주 연락처</td>
			   <td width="37%" height="25" class="bg_04">
					<input type='text' name='comp_tel' value='<%=comp_tel%>' readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">외주처명</td>
			   <td width="37%" height="25" class="bg_04">
					<input type='text' name='comp_name' value='<%=comp_name%>' size='30' readonly>
			   </td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">사업자번호</td>
			   <td width="37%" height="25" class="bg_04">
					<input type='text' name='comp_code' value='<%=comp_code%>' readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">진행상태</td>
			   <td width="87%" height="25" class="bg_04" colspan=3>
			<%
				String[] status_no = {"1","2","3","4","5","6"};
				String[] status_name = {"오더작성","작지작성","오더확정","부품출고","제조실적","실적마감"};
				String status_sel = "";
				for(int i=0; i<status_no.length; i++) {
					if(status_no[i].equals(order_status)) status_sel = "checked";
					else status_sel = "";
					out.println("<input type='radio' "+status_sel+" value='' disabled>"+status_name[i]);
				} 
			%>		</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR><!-- 실적정보 -->
				<td height="25" colspan="4"><IMG src='../mm/images/title_result_info.gif' border='0' align='absmiddle' alt='실적정보'></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">실적수량</td>
			   <td width="37%" height="25" class="bg_04"><%=rst_total_count%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">양품수량</td>
			   <td width="37%" height="25" class="bg_04"><%=rst_good_count%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">잔량수량</td>
			   <td width="37%" height="25" class="bg_04"><%=working_count%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">불량수량</td>
			   <td width="37%" height="25" class="bg_04"><%=rst_bad_count%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			
		</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='mrp_no' value='<%=mrp_no%>'>
<input type='hidden' name='order_status' value='<%=order_status%>'>
</form>

<DIV id="lding" style="position:absolute;left:300px;top:200px;width:224px;height:150px;visibility:hidden;">
	<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

<div id="saving" style="position:absolute;left:0px;top:10px;width:300px;height:100px;visibility:hidden;">
<TABLE width="800" border="0" cellspacing=1 cellpadding=1 bgcolor="">
	<TR><td height="500" align="center" valign="middle" class='subB'>
	</td> 
	</tr>
</table>
</div>

</body>
</html>
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
	var order_status = '<%=order_status%>';
	if(order_status != '') { alert('생산계획접수상태 에서만 가능합니다.'); return; }

	var f = document.eForm;

	var factory_no = f.factory_no.value;
	if(factory_no == '') { alert('공장번호가 입력되지 않았습니다.'); f.factory_no.focus(); return; }
	
	var fg_code = f.fg_code.value;
	if(fg_code == '') { alert('FG코드가 입력되지 않았습니다.'); f.fg_code.focus(); return; }

	var item_code = f.item_code.value;
	if(item_code == '') { alert('품목코드가 입력되지 않았습니다.'); f.item_code.focus(); return; }

	var mfg_count = f.mfg_count.value;
	if(mfg_count == '') { alert('생산수량이 입력되지 않았습니다.'); f.mfg_count.focus(); return; }
	else if(mfg_count == '0') { alert('생산수량이 입력되지 않았습니다.'); f.mfg_count.focus(); return; }

	if(isNaN(mfg_count)) { alert('생산수량은 숫자만 입력이 가능합니다.'); return; }
	else if(mfg_count.indexOf('.') != -1) { alert('생산수량은 정수만 입력이 가능합니다.'); return; }

	var buy_type = "";
	var nm = eForm.buy_type.length;	
	for(i=0; i<nm; i++) {
		if(eForm.buy_type[i].checked) buy_type = document.eForm.buy_type[i].value;
	}
	//외주가공품인경우 외주처 정보 입력
	if(buy_type == 'O') {
		var comp_code = f.comp_code.value;
		if(comp_code == '') { alert('외주처코드가 입력되지 않았습니다.'); f.comp_code.focus(); return; }
	}

	//재작업시 전작지번호 입력
	var re_work = "";
	var nm = eForm.re_work.length;	
	for(i=0; i<nm; i++) {
		if(eForm.re_work[i].checked) re_work = document.eForm.re_work[i].value;
	}
	if(re_work == "재작업") {
		link_mfg_no = document.eForm.link_mfg_no.value;
		if(link_mfg_no.length <10) {alert('재작업시 전작업지시번호를 입력하십시오.'); return; }
	}

	//데이터 읽기하기
	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
	document.all['saving'].style.visibility="visible";	//처리중 버튼닫기 

	document.eForm.action='../servlet/mfgInfoServlet';
	document.eForm.mode.value='mfg_save';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//수정하기
function sendModify()
{

	var order_status = '<%=order_status%>';
	if(order_status != '1') { alert('오더작성상태 에서만 가능합니다.'); return; }

	var f = document.eForm;

	var mfg_count = f.mfg_count.value;
	if(mfg_count == '') { alert('생산수량이 입력되지 않았습니다.'); f.mfg_count.focus(); return; }
	else if(mfg_count == '0') { alert('생산수량이 입력되지 않았습니다.'); f.mfg_count.focus(); return; }

	if(isNaN(mfg_count)) { alert('생산수량은 숫자만 입력이 가능합니다.'); return; }
	else if(mfg_count.indexOf('.') != -1) { alert('생산수량은 정수만 입력이 가능합니다.'); return; }

	var buy_type = "";
	var nm = eForm.buy_type.length;	
	for(i=0; i<nm; i++) {
		if(eForm.buy_type[i].checked) buy_type = document.eForm.buy_type[i].value;
	}
	//외주가공품인경우 외주처 정보 입력
	if(buy_type == 'O') {
		var comp_code = f.comp_code.value;
		if(comp_code == '') { alert('외주처코드가 입력되지 않았습니다.'); f.comp_code.focus(); return; }
	} else {
		var comp_code="";
		var comp_name="";
		var comp_user="";
		var comp_tel ="";
	}

	//재작업시 전작지번호 입력
	var re_work = "";
	var nm = eForm.re_work.length;	
	for(i=0; i<nm; i++) {
		if(eForm.re_work[i].checked) re_work = document.eForm.re_work[i].value;
	}
	if(re_work == "재작업") {
		link_mfg_no = document.eForm.link_mfg_no.value;
		if(link_mfg_no.length <10) {alert('재작업시 전작업지시번호를 입력하십시오.'); return; }
	}


	var v = confirm('해당내용을 수정하시겠습니까?'); 
	if(v == false) { return; }
	
	//데이터 읽기하기
	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
	document.all['saving'].style.visibility="visible";	//처리중 버튼닫기 
	
	document.eForm.action='../servlet/mfgInfoServlet';
	document.eForm.mode.value='mfg_modify';
	if(buy_type != 'O') {
		document.eForm.comp_code.value=comp_code;
		document.eForm.comp_name.value=comp_name;
		document.eForm.comp_user.value=comp_user;
		document.eForm.comp_tel.value=comp_tel;
	}
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//삭제하기
function sendDelete()
{
	var order_status = '<%=order_status%>';
	if(order_status != '1') { alert('오더작성상태 에서만 가능합니다.'); return; }

	var mrp_no = document.eForm.mrp_no.value;
	if(mrp_no != '') { alert('긴급오더로 작성된 경우만 삭제가 가능합니다.'); return; }

	var v = confirm('해당내용을 삭제하시겠습니까?'); 
	if(v == false) { return; }

	//데이터 읽기하기
	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
	document.all['saving'].style.visibility="visible";	//처리중 버튼닫기 

	document.eForm.action='../servlet/mfgInfoServlet';
	document.eForm.mode.value='mfg_delete';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//공정생성
function opCreate()
{
	var order_status = '<%=order_status%>';
	if(order_status != '1') { alert('오더작성상태 에서만 가능합니다.'); return; }

	//데이터 읽기하기
	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
	document.all['saving'].style.visibility="visible";	//처리중 버튼닫기 

	document.eForm.action='../servlet/mfgInfoServlet';
	document.eForm.mode.value='order_create';
	document.eForm.order_status.value='2';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//목록보기
function List()
{
	document.eForm.action='../servlet/mfgInfoServlet';
	document.eForm.mode.value='mfg_list';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//공정계획
function opPlan()
{
	var order_status = '<%=order_status%>';
	if(order_status == '' || order_status == '1') { alert('생산지시작성상태 에서만 가능합니다.'); return; }

	var pid = document.eForm.pid.value;
	document.eForm.action='../mm/mfg/mfgOperatorFrame.jsp?pid='+pid;
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//부품조정
function itemPlan()
{
	
	var order_status = '<%=order_status%>';
	if(order_status == '' || order_status == '1') { alert('생산지시작성상태 에서만 가능합니다.'); return; }

	var pid = document.eForm.pid.value;
	var factory_no = document.eForm.factory_no.value;
	var mfg_no = document.eForm.mfg_no.value;
	var para = "pid="+pid+"&factory_no="+factory_no+"&mfg_no="+mfg_no;

	document.eForm.action='../mm/mfg/mfgItemFrame.jsp?pid='+para;
	document.onmousedown=dbclick;
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
	newWIndow = wopen(strUrl, "Calendar", "200","280","scrollbars=no,toolbar=no,status=no,resizable=no");
}
// 품목정보 가져오기
function searchItemInfo(){
	
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_spec&item_unit=item_unit";
	wopen(strUrl,"open_part",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//외주처정보 가져오기
function searchCompInfo() {
	wopen("../mm/searchIndustry.jsp?target=eForm.comp_user/eForm.comp_tel/eForm.comp_name/eForm.comp_code","comp","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//재작업시 입력란 만들기
function addInputForm()
{

	var order_status = '<%=order_status%>';
	var rd="readonly";
	if(order_status == '' || order_status == '1') rd="";

	var re_work = "";
	var nm = eForm.re_work.length;	
	for(i=0; i<nm; i++) {
		if(eForm.re_work[i].checked) re_work = document.eForm.re_work[i].value;
	}

	if(re_work == "재작업") {
		//ad.innerHTML = " <font color=red>(前)</font>작업지시번호";
		ad.innerHTML = "<input type='hidden' name=link_mfg_no size=10 value='<%=link_mfg_no%>'"+rd+">";
	} else ad.innerHTML="";
}
//초기 로드시 재작업시 입력란 만들기
function InitLoad()
{
	var order_status = '<%=order_status%>';
	var rd="readonly";
	if(order_status == '' || order_status == '1') rd="";

	var re_work = '<%=re_work%>'; 
	if(re_work == "재작업") {
		ad.innerHTML = " <font color=red>(前)</font>작업지시번호";
		ad.innerHTML += "<input type=text name=link_mfg_no size=10 value='<%=link_mfg_no%>'"+rd+">";
	} else ad.innerHTML="";
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
