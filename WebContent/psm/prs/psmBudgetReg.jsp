<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제예산상태 편집"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.psm.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	com.anbtech.psm.entity.psmEnvTable env;
	String msg = (String)request.getAttribute("msg");	if(msg == null) msg = "";
	
	//----------------------------------------------------
	//	PSM MASTER 입력/수정 정보 읽기
	//----------------------------------------------------
	com.anbtech.psm.entity.psmBudgetTable psm;
	psm = (psmBudgetTable)request.getAttribute("BUDGET_table");

	String pid = psm.getPid();	
	String psm_code = psm.getPsmCode();	
	String psm_type = psm.getPsmType();	
	String comp_name = psm.getCompName();	
	String comp_category = psm.getCompCategory();	
	String psm_korea = psm.getPsmKorea();	
	String psm_english = psm.getPsmEnglish();
	String psm_start_date = psm.getPsmStartDate();
	String psm_end_date = psm.getPsmEndDate();
	String psm_pm = psm.getPsmPm();
	String psm_mgr = psm.getPsmMgr();
	String psm_budget = psm.getPsmBudget();
	String psm_user = psm.getPsmUser();	if(psm_user.length() == 0) psm_user=sl.id+"/"+sl.name;
	String plan_sum = psm.getPlanSum();
	String plan_labor = psm.getPlanLabor();
	String plan_material = psm.getPlanMaterial();
	String plan_cost = psm.getPlanCost();
	String plan_plant = psm.getPlanPlant();
	String change_desc = psm.getChangeDesc();
	String change_date = psm.getChangeDate();
	String budget_type = psm.getBudgetType();
	
	//수정,삭제 가능여부 판단(1일자동마감) : 수정,삭제할때 사용
	String to_date = anbdt.getDate();
	String mod = "N";
	if(to_date.equals(change_date)) mod = "Y";
	if(budget_type.equals("2")) mod = "N";			//지출항목은 무조건 수정불가

	//현재내용이 등록상태인지 아닌지 판단하기 : 등록할때 사용
	String reg = "N";
	if(change_desc.length() == 0) reg = "Y";
	
	//승인권자인지 판단하기
	String mgr = "";
	if(psm_budget.indexOf(sl.id) != -1) mgr = "승인권자";

	//--------------------------------------
	// 과제종류 리스트 가져오기
	//--------------------------------------
	ArrayList psm_list = new ArrayList();
	psm_list = (ArrayList)request.getAttribute("PSM_List");
	env = new psmEnvTable();
	Iterator env_iter = psm_list.iterator();

	int env_cnt = psm_list.size();
	String[][] pjt_list = new String[env_cnt][3];
	int n=0;
	while(env_iter.hasNext()) {
		env = (psmEnvTable)env_iter.next();
		pjt_list[n][0] = env.getEnvType();			//구분 : 과제표시
		pjt_list[n][1] = env.getEnvStatus();		//상태 : 1:예비등록과제, 2:정식등록과제
		pjt_list[n][2] = env.getEnvName();			//이름 : 과제종류명
		n++;
	}

	//제목달기
	String title = "과제";
	for(int i=0; i<n; i++) {
		if(pjt_list[i][2].equals(psm_type)) {
			if(pjt_list[i][1].equals("1")) title = "예비과제";
			else if(pjt_list[i][1].equals("2")) title = "정식과제";
		}
	}
	

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	com.anbtech.psm.entity.psmBudgetTable table;
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("BUDGET_List");
	table = new psmBudgetTable();
	Iterator table_iter = table_list.iterator();

	//----------------------------------------------------
	//	PSM MASTER 입력/수정 정보 읽기
	//----------------------------------------------------
	com.anbtech.psm.entity.psmMasterTable psm_price;
	psm_price = (psmMasterTable)request.getAttribute("MASTER_table");
	String total_plan_sum = psm_price.getPlanSum();
	String total_result_sum = psm_price.getResultSum();
	String total_diff_sum = psm_price.getDiffSum();
%>

<HTML>
<HEAD><TITLE>과제예산상태 편집</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../psm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../psm/images/blet.gif"> <%=title%> 추가예산등록</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
				<% if(change_desc.length() == 0) {		//신규등록 %>
					<a href="javascript:sendSave();"><img src="../psm/images/bt_reg.gif" border=0 border=0 align='absmiddle'></a>
				<% } else if(mod.equals("Y")) {			//수정삭제 %>		
					<a href="javascript:sendModify();"><img src="../psm/images/bt_modify.gif" border=0 border=0 align='absmiddle'></a>
					<a href="javascript:sendDelete();"><img src="../psm/images/bt_del.gif" border=0 border=0 align='absmiddle'></a>
					<a href="javascript:sendClear();"><img src="../psm/images/bt_cancel.gif" border=0 border=0 align='absmiddle'></a>
				<% } else {								//신규등록모드로 %>
					<a href="javascript:sendClear();"><img src="../psm/images/bt_cancel.gif" border=0 border=0 align='absmiddle'></a>
				<% } %>
					<a href="javascript:sendList();"><img src="../psm/images/bt_list.gif" border=0 border=0 align='absmiddle'></a>
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">작성자</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_user%>
					<input type="hidden" name="psm_user" value="<%=psm_user%>" size="20"></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">작성일</td>
			   <td width="37%" height="25" class="bg_04"><%=change_date%>
					<input type="hidden" name="change_date" value="<%=change_date%>" size="30"></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제코드</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_code%></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제명</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_korea%> [<%=psm_english%>]</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산종류</td>
				<td width="87%" height="25" class="bg_04" colspan="3">
				<%
					if(budget_type.equals("2")) {
						out.println("<input type='hidden' name='budget_type' value='2'>지출");
					} else if(budget_type.equals("1")){
						out.println("<select name='budget_type'>");
						out.println("<option value='1' selected>추가</option>");
						out.println("<option value='3'>삭감</option>");
						out.println("</select>");
					} else if(budget_type.equals("3")){
						out.println("<select name='budget_type'>");
						out.println("<option value='1'>추가</option>");
						out.println("<option value='3' selected>삭감</option>");
						out.println("</select>");
					}
				
				%>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산총액</td>
				<td width="37%" height="25" class="bg_04">
					<input type='text' name='plan_sum' value='<%=plan_sum%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly>
					<input type=text name=plan_sum_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
				<td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제시작일</td>
			    <td width="37%" height="25" class="bg_04"><%=psm_start_date%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">인건비</td>
				<td width="37%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=plan_labor%>' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_labor_money);">
					<input type=text name=plan_labor_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
				<td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제종료일</td>
			    <td width="37%" height="25" class="bg_04"><%=psm_end_date%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">재료비</td>
				<td width="37%" height="25" class="bg_04">
					<input type='text' name='plan_material' value='<%=plan_material%>' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_material_money);">
					<input type=text name=plan_material_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
			    <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산담당자</td>
			    <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_budget' value='<%=psm_budget%>' readonly>
					<a href="javascript:searchBUDGET();"><img src="../psm/images/bt_search.gif" border="0" align='absbottom'></a></td>
				
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">경비</td>
				<td width="37%" height="25" class="bg_04">
					<input type='text' name='plan_cost' value='<%=plan_cost%>' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_cost_money);">
					<input type=text name=plan_cost_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif" rowspan=3>변경사유</td>
			   <td width="37%" height="25" class="bg_04" rowspan=3>
					<TEXTAREA class='text_01' NAME='change_desc' rows=3 cols=40 style='border:1px solid #787878;'><%=change_desc%></TEXTAREA></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">용역비</td>
				<td width="37%" height="25" class="bg_04">
					<input type='text' name='plan_plant' value='<%=plan_plant%>' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_plant_money);">
					<input type=text name=plan_plant_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
	<TR><TD height='2' bgcolor='#FFFFFF'></TD></TR>

	<TR><TD height='25' bgcolor='' style='padding-left:5px;'>
			<img src='../psm/images/budget.gif' border='0' align='absmiddle'><font color='#FF3333'> <%=total_plan_sum%></font>원 &nbsp;&nbsp;&nbsp;
			<img src='../psm/images/result.gif' border='0' align='absmiddle'><font color='#FF3333'> <%=total_result_sum%></font>원 &nbsp;&nbsp;&nbsp;
			<img src='../psm/images/balance.gif' border='0' align='absmiddle'><font color='#FF3333'> <%=total_diff_sum%></font>원</TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</table>

<!--list-->
<TABLE height="" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					
					<TR vAlign=middle height=23>
						<TD noWrap width=90 align=middle class='list_title'>과제코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>과제명(한글)</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=30 align=middle class='list_title'>종류</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>예산총액</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>인건비</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>재료비</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>경 비</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>시설비</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>등록일자</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	String status = "",budget_kind="";
	while(table_iter.hasNext()){
		table = (psmBudgetTable)table_iter.next();
		budget_kind=table.getBudgetType();
		if(budget_kind.equals("1")) budget_kind="추가";
		else if(budget_kind.equals("2")) budget_kind="지출";
		else if(budget_kind.equals("3")) budget_kind="삭감";
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'><%=table.getPsmCode()%></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPsmKorea()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=budget_kind%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanSum()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanLabor()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanMaterial()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanCost()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanPlant()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getChangeDate()%></td>
						<TD><IMG height=1 width=1></TD>
					</TR>
					<TR><TD colspan=19 background="../psm/images/dot_line.gif"></TD></TR>
<%		
	}
%>
				</TBODY></TABLE></TD></TR>

</TBODY></TABLE>

<!--form hidden -->
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='psm_code' value='<%=psm_code%>'>
<input type='hidden' name='psm_type' value='<%=psm_type%>'>
<input type='hidden' name='comp_name' value='<%=comp_name%>'>
<input type='hidden' name='comp_category' value='<%=comp_category%>'>
<input type='hidden' name='psm_korea' value='<%=psm_korea%>'>
<input type='hidden' name='psm_english' value='<%=psm_english%>'>
<input type='hidden' name='psm_start_date' value='<%=psm_start_date%>'>
<input type='hidden' name='psm_end_date' value='<%=psm_end_date%>'>
<input type='hidden' name='psm_pm' value='<%=psm_pm%>'>
<input type='hidden' name='psm_mgr' value='<%=psm_mgr%>'>

<input type='hidden' name='reg' value='<%=reg%>'>
<input type='hidden' name='mod' value='<%=mod%>'>
</form>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">데이터 처리중입니다. 잠시만 기다려 주십시요.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>
<script language=javascript>
<!--
//msg 처리
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}

//등록하기
function sendSave()
{
	var reg = document.eForm.reg.value;
	if(reg == 'N') { alert('등록할 수 없습니다. 현재는 조회상태입니다.'); return; }

	var f = document.eForm;
	var plan_sum = f.plan_sum.value;
	if(plan_sum.length < 6) { alert('예산 총금액이 입력되지 않았습니다. '); f.plan_material.focus(); return; }
	var plan_material = f.plan_material.value;
	if(plan_material.length < 6) { 
		alert('재료비 예산금액이 입력되지 않았습니다. '); f.plan_material.focus(); return; 
	}
	var change_desc = f.change_desc.value;
	if(change_desc.length < 10) { alert('변경사유가 입력되지 않았습니다. 10자이상 입력하십시오.'); return; }
	
	if(!confirm('등록하시겠습니까?'))return;

	//금액에서 ','없애기
	var psum = unCommaObj(document.eForm.plan_sum.value);		//계획총액
	var plab = unCommaObj(document.eForm.plan_labor.value);		//계획인건비
	var pmat = unCommaObj(document.eForm.plan_material.value);	//계획재료비
	var pcst = unCommaObj(document.eForm.plan_cost.value);		//계획비용
	var pant = unCommaObj(document.eForm.plan_plant.value);		//계획시설비

	document.eForm.action='../servlet/PsmBudgetServlet';
	document.eForm.mode.value='bud_write';
	document.eForm.plan_sum.value=psum;
	document.eForm.plan_labor.value=plab;
	document.eForm.plan_material.value=pmat;
	document.eForm.plan_cost.value=pcst;
	document.eForm.plan_plant.value=pant;
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//수정하기
function sendModify()
{
	var reg = document.eForm.reg.value;
	if(reg == 'Y') { alert('수정할 수 없습니다. 현재는 등록상태입니다.'); return; }

	var mod = document.eForm.mod.value;
	if(mod == 'N') { alert('수정할 수 없습니다. 변경 당일에만 가능합니다.'); return; }

	var f = document.eForm;
	var f = document.eForm;
	var plan_sum = f.plan_sum.value;
	if(plan_sum.length < 6) { alert('예산 총금액이 입력되지 않았습니다. '); f.plan_material.focus(); return; }
	var plan_material = f.plan_material.value;
	if(plan_material.length < 6) { 
		alert('재료비 예산금액이 입력되지 않았습니다. '); f.plan_material.focus(); return; 
	}
	var change_desc = f.change_desc.value;
	if(change_desc.length < 10) { alert('변경사유가 입력되지 않았습니다. 10자이상 입력하십시오.'); return; }

	if(!confirm('수정하시겠습니까?'))return;

	//금액에서 ','없애기
	var psum = unCommaObj(document.eForm.plan_sum.value);		//계획총액
	var plab = unCommaObj(document.eForm.plan_labor.value);		//계획인건비
	var pmat = unCommaObj(document.eForm.plan_material.value);	//계획재료비
	var pcst = unCommaObj(document.eForm.plan_cost.value);		//계획비용
	var pant = unCommaObj(document.eForm.plan_plant.value);		//계획시설비

	document.eForm.action='../servlet/PsmBudgetServlet';
	document.eForm.mode.value='bud_modify';
	document.eForm.plan_sum.value=psum;
	document.eForm.plan_labor.value=plab;
	document.eForm.plan_material.value=pmat;
	document.eForm.plan_cost.value=pcst;
	document.eForm.plan_plant.value=pant;
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//삭제하기
function sendDelete()
{
	var reg = document.eForm.reg.value;
	if(reg == 'Y') { alert('삭제할 수 없습니다. 현재는 등록상태입니다.'); return; }

	var mod = document.eForm.mod.value;
	if(mod == 'N') { alert('삭제할 수 없습니다. 변경 당일에만 가능합니다.'); return; }

	var budget_type = document.eForm.budget_type.value;	
	if(budget_type == '2') { alert('구매에 의한 지출은 삭제할 수 없습니다.'); return; }

	if(!confirm('삭제하시겠습니까?'))return;

	document.eForm.action='../servlet/PsmBudgetServlet';
	document.eForm.mode.value='bud_delete';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//등록준비하기
function sendClear()
{
	document.eForm.action='../servlet/PsmBudgetServlet';
	document.eForm.mode.value='bud_prewrite';
	document.eForm.pid.value='';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//편집[수정,삭제]하기
function psmView(pid)
{
	document.eForm.action='../servlet/PsmBudgetServlet';
	document.eForm.mode.value='bud_prewrite';
	document.eForm.pid.value=pid;
	document.eForm.submit();
}
//목록으로
function sendList()
{
	document.eForm.action='../servlet/PsmBudgetServlet';
	document.eForm.mode.value='master_list';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../psm/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = wopen(strUrl, "Calendar", "180", "250", "scrollbar=no,toolbar=no,status=no,resizable=no");
}
//예산담당자 찾기
function searchBUDGET()
{
	var strUrl = "../psm/searchName.jsp?target=eForm.psm_budget";
	newWIndow = wopen(strUrl, "BudgetInfo", "280", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
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
//금액 천단위에 콤마넣기
function InputMoney(input,obj){
	str = input.value;
	str = unComma(str);
	MoneyToHan(str,obj)
	str = Comma(str);
	input.value = str;

	sumMoney();
}
//총액표시하기
function sumMoney(){
	labor = unCommaObj(document.eForm.plan_labor.value);
	material = unCommaObj(document.eForm.plan_material.value);
	cost = unCommaObj(document.eForm.plan_cost.value);
	plant = unCommaObj(document.eForm.plan_plant.value);
	
	sum = eval(labor+material+cost+plant); 
	MoneyToHanguel(sum);
	document.eForm.plan_sum.value=Comma(sum);
}
//한글금액 포시
function MoneyToHan(str,obj){
	arrayNum=new Array("0","1","2","3","4","5","6","7","8","9");
	arrayUnit=new Array("","","","","만 ","","","","억 ","","","","조 ","","","","경 ");
	arrayStr= new Array()
	len = str.length; 
	hanStr = "";
	for(i=0;i<len;i++) { arrayStr[i] = str.substr(i,1) }
	code = len;
	for(i=0;i<len;i++) {
		code--;
		tmpUnit = "";
		if(arrayNum[arrayStr[i]] != ""){
			tmpUnit = arrayUnit[code];
			if(code>4) {
				if(( Math.floor(code/4) == Math.floor((code-1)/4)
				     && arrayNum[arrayStr[i+1]] != "") || 
				   ( Math.floor(code/4) == Math.floor((code-2)/4) 
				     && arrayNum[arrayStr[i+2]] != "")) {
					tmpUnit=arrayUnit[code].substr(0,1);
				} 
			}
		}
		hanStr +=  arrayNum[arrayStr[i]]+tmpUnit;
    }
	obj.value = hanStr;
}
//총금액 한글금액 표시
function MoneyToHanguel(str){
	arrayNum=new Array("0","1","2","3","4","5","6","7","8","9");
	arrayUnit=new Array("","","","","만 ","","","","억 ","","","","조 ","","","","경 ");
	arrayStr= new Array()
	str = str.toString();
	len = str.length; 
	hanStr = "";
	for(i=0;i<len;i++) { arrayStr[i] = str.substr(i,1) }
	code = len;
	for(i=0;i<len;i++) {
		code--;
		tmpUnit = "";
		if(arrayNum[arrayStr[i]] != ""){
			tmpUnit = arrayUnit[code];
			if(code>4) {
				if(( Math.floor(code/4) == Math.floor((code-1)/4)
				     && arrayNum[arrayStr[i+1]] != "") || 
				   ( Math.floor(code/4) == Math.floor((code-2)/4) 
				     && arrayNum[arrayStr[i+2]] != "")) {
					tmpUnit=arrayUnit[code].substr(0,1);
				} 
			}
		}
		hanStr +=  arrayNum[arrayStr[i]]+tmpUnit;
    }
	document.eForm.plan_sum_money.value = hanStr;
}
function isNumObj(obj)
{
	for (var i = 0; i < obj.value.length ; i++){
		chr = obj.value.substr(i,1);		
		chr = escape(chr);
		key_eg = chr.charAt(1);
		if (key_eg == 'u'){
			key_num = chr.substr(i,(chr.length-1));			
			if((key_num < "AC00") || (key_num > "D7A3")) { 
				event.returnValue = false;
			} 			
		}
	}
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false;
	}
}
function Comma(num) {
	re = /(\d+)/;
	if(re.test(num)){ 
		re.exec(num); num = RegExp.$1; 
		re = /(\d+)(\d{3})/;
		while(re.test(num)){ num = num.replace(re,"$1,$2"); }
	}
    return (num);
}
function unComma(str) {
	return str.replace(/,/g,"");
}

//obj로받아 콤마 없애기
function unCommaObj(input) {
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
-->
</script>