<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "PSM 미진행 등록/수정하기"		
	contentType = "text/html; charset=euc-kr" 	
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
	String env_status = (String)request.getAttribute("env_status");	if(env_status == null) env_status = "1";

	//----------------------------------------------------
	//	PSM MASTER 입력/수정 정보 읽기
	//----------------------------------------------------
	com.anbtech.psm.entity.psmMasterTable psm;
	psm = (psmMasterTable)request.getAttribute("MASTER_table");

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
	String psm_user = psm.getPsmUser();		if(psm_user.length()==0) psm_user = sl.id+"/"+sl.name;
	String psm_desc = psm.getPsmDesc();

	String plan_sum = psm.getPlanSum();
	String plan_labor = psm.getPlanLabor();
	String plan_material = psm.getPlanMaterial();
	String plan_cost = psm.getPlanCost();
	String plan_plant = psm.getPlanPlant();

	String result_sum = psm.getResultSum();
	String result_labor = psm.getResultLabor();
	String result_material = psm.getResultMaterial();
	String result_cost = psm.getResultCost();
	String result_plant = psm.getResultPlant();

	String contract_date = psm.getContractDate();		
	String contract_name = psm.getContractName();
	String contract_price = psm.getContractPrice();
	String complete_date = psm.getCompleteDate();
	String psm_status = psm.getPsmStatus();
	String reg_date = psm.getRegDate();			
	String app_date = psm.getAppDate();	

	//과제담당자인지 판단하기 : 승인권자인지 판단하기
	String mgr = (String)request.getAttribute("statusMgr");
	if(mgr.indexOf(sl.id) != -1) mgr = "과제담당자";

	//----------------------------------------------------
	//	첨부파일 읽기
	//----------------------------------------------------
	String id_path = "";
	if(psm_user.length() !=0) id_path = psm_user.substring(0,psm_user.indexOf("/"));
	String bon_path = "/psm/"+id_path;			//첨부파일 저장 확장path
	String fname = psm.getFname();				//첨부파일명
	String sname = psm.getSname();				//첨부파일 저장명
	String ftype = psm.getFtype();				//첨부파일Type
	String fsize = psm.getFsize();				//첨부파일Size
	int attache_cnt = 4;						//첨부파일 최대갯수 (미만)

	//첨부파일 개별로 읽기
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	String[][] addFile = new String[cnt][5];
	for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

	if(fname.length() != 0) {
		StringTokenizer f = new StringTokenizer(fname,"|");		//파일명 담기
		int m = 0;
		while(f.hasMoreTokens()) {
			addFile[m][0] = f.nextToken();
			addFile[m][0] = addFile[m][0].trim(); 
			if(addFile[m][0] == null) addFile[m][0] = "";
			m++;
		}
		StringTokenizer t = new StringTokenizer(ftype,"|");		//파일type 담기
		m = 0;
		while(t.hasMoreTokens()) {
			addFile[m][1] = t.nextToken();
			addFile[m][1] = addFile[m][1].trim();
			if(addFile[m][1] == null) addFile[m][1] = "";
			m++;
		}
		StringTokenizer s = new StringTokenizer(fsize,"|");		//파일크기 담기
		m = 0;
		while(s.hasMoreTokens()) {
			addFile[m][2] = s.nextToken();
			addFile[m][2] = addFile[m][2].trim();
			if(addFile[m][2] == null) addFile[m][2] = "";
			m++;
		}
		StringTokenizer o = new StringTokenizer(sname,"|");		//저장파일 담기
		m = 0;
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();	
			addFile[m][4] = addFile[m][3];
			addFile[m][3] = addFile[m][3].trim() + ".bin";			//저장파일명
			addFile[m][4] = addFile[m][4].trim();					//TEMP 저장파일명
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}

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
	String title = "과제정보";
	if(env_cnt > 0) {
		if(pjt_list[0][1].equals("1")) title = "예비과제 등록";
		else if(pjt_list[0][1].equals("2")) title = "정식과제 등록";	    
	}

	//----------------------------------------
	//	편집여부 판단
	//----------------------------------------
	String icon = "E";						//icon 출력여부
	String rd = "";							//TEXT 수정가능 여부판단하기
	String sbox = "disabled";				//선택박스 수정가능 여부판단하기
	if(psm_status.equals("11")) {
		rd="readonly";
		sbox="disabled";
		icon="D";
	}

%>

<HTML>
<HEAD><TITLE>PSM 미진행 등록/수정하기</TITLE>
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
					<TD valign='middle' class="title"><img src="../psm/images/blet.gif"> <%=title%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<% if(psm_status.equals("S")) {%>
					<a href="javascript:sendSave();"><img src="../psm/images/bt_reg.gif" border=0></a>
					<% } else if(psm_status.equals("1")) {%>
					<a href="javascript:sendModify();"><img src="../psm/images/bt_modify.gif" border=0></a>
					<a href="javascript:sendDelete();"><img src="../psm/images/bt_del.gif" border=0></a>
					<a href="javascript:sendRequest();"><img src="../psm/images/bt_sangsin.gif" border=0></a>
					<% } else if(psm_status.equals("11")) {%>
					<a href="javascript:sendApproval();"><img src="../psm/images/bt_commit_app.gif" border=0></a>
					<a href="javascript:sendReject();"><img src="../psm/images/bt_reject_app.gif" border=0></a>
					<a href="javascript:sendList();"><img src="../psm/images/bt_list.gif" border=0></a>
					<% } %>
					</TD></TR></TBODY></TABLE></TD></TR>
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
			   <td width="37%" height="25" class="bg_04"><%=reg_date%>
					<input type="hidden" name="reg_date" value="<%=reg_date%>" size="30"></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제코드</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_code%>
					<input class='text_01' type='hidden' name='psm_code' value='<%=psm_code%>'></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제종류</td>
			   <td width="37%" height="25" class="bg_04">
					<select name="psm_type" style=font-size:9pt;color="black";>
					<%
						String sel = "";
						for(int i=0; i<env_cnt; i++) {
							if(pjt_list[i][2].equals(psm_type)) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+pjt_list[i][2]+"'>");
							out.println(pjt_list[i][2]+"</option>");
						} 
					%></select>
					</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제고객</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='comp_name' value='<%=comp_name%>' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchPsmInfo();"><img src="../psm/images/bt_search.gif" border="0" align='absbottom'></a>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제카테고리</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='comp_category' value='<%=comp_category%>' readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제명(한글)</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_korea' maxlength='50' value='<%=psm_korea%>' <%=rd%>></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제명(영문)</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_english' maxlength='50' value='<%=psm_english%>' <%=rd%>></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제시작일</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_start_date'  size=10 value='<%=psm_start_date%>' readonly>
				<% if(icon.equals("E")) { %>
					<A Href="Javascript:OpenCalendar('psm_start_date');"><img src="../psm/images/bt_calendar.gif" border="0" align='absbottom'></A>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제완료일</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_end_date' size=10  value='<%=psm_end_date%>' readonly>
			   <% if(icon.equals("E")) { %>
					<A Href="Javascript:OpenCalendar('psm_end_date');"><img src="../psm/images/bt_calendar.gif" border="0" align='absbottom'></A>
				<% } %></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제담당자</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_mgr' size=15 value='<%=psm_mgr%>' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchMGR();"><img src="../psm/images/bt_search.gif" border="0" align='absbottom'></a>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산담당자</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_budget' size=15 value='<%=psm_budget%>' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchBUDGET();"><img src="../psm/images/bt_search.gif" border="0" align='absbottom'></a>
				<% } %></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제PM</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_pm' size=15  value='<%=psm_pm%>' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchPM();"><img src="../psm/images/bt_search.gif" border="0" align='absbottom'></a>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif" rowspan=3>과제설명</td>
			   <td width="37%" height="25" class="bg_04" rowspan=3>
					<TEXTAREA class='text_01' NAME='psm_desc' rows=3 cols=42 style='border:1px solid #787878;' <%=rd%>><%=psm_desc%></TEXTAREA></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">진행상태</td>
			   <td width="37%" height="25" class="bg_04">
					<select name='psm_status' disabled>
			<%
				String[] list = {"S","1","11","2","3","4","5","6"};
				String[] list_name = {"신규등록","미진행","확정상신","진행","재진행","완료","보류","취소"};
				String csel="";
						
				for(int i=0; i<list.length; i++) {
					if(psm_status.equals(list[i])) csel="selected";
					else csel = "";
					out.println("<option value='"+list[i]+"' "+csel+" >"+list_name[i]+"</option>");
				}
			%></select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- 예산정보 -->
				<td height="25" colspan="4"><img src='../psm/images/title_money_info.gif' border='0' align='absmiddle' alt='과제 예산정보'></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산 총액</td>
				<td width="90%" height="25" class="bg_04" colspan=3>
					<input type='text' name='plan_sum' value='<%=plan_sum%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly>
					<input type=text name=plan_sum_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">인건비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=plan_labor%>' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_labor_money);" <%=rd%>>
					<input type=text name=plan_labor_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">재료비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_material' value='<%=plan_material%>' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_material_money);" <%=rd%>>
					<input type=text name=plan_material_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">경 비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_cost' value='<%=plan_cost%>' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_cost_money);" <%=rd%>>
					<input type=text name=plan_cost_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">시설비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_plant' value='<%=plan_plant%>' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_plant_money);" <%=rd%>>
					<input type=text name=plan_plant_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- 수주정보 -->
				<td height="25" colspan="4"><img src='../psm/images/title_order_info.gif' border='0' align='absmiddle' alt='과제 수주정보'></b></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">계약명</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='contract_name' maxlength='100' value='<%=contract_name%>' <%=rd%>></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">계약일</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='contract_date' size=10  value='<%=contract_date%>' readonly>
				<% if(icon.equals("E")) { %>
					<A Href="Javascript:OpenCalendar('contract_date');"><img src="../psm/images/bt_calendar.gif" border="0" align='absbottom'></A>
				<% } %></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">계약금액</td>
			   <td width="37%" height="25" class="bg_04">
					<input type='text' name='contract_price' value='<%=contract_price%>' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.contract_price_money);" <%=rd%>>
					<input type=text name=contract_price_money style="border:1px;background-color:#FFEFDF;text-align:right;color:#FF0033" readonly size=20> 원</td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">납기일자</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='complete_date'  size=10 value='<%=complete_date%>' readonly>
				<% if(icon.equals("E")) { %>
					<A Href="Javascript:OpenCalendar('complete_date');"><img src="../psm/images/bt_calendar.gif" border="0" align='absbottom'></A>
				<% } %></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">첨부파일</td>
			   <td width="87%" height="25" class="bg_04" colspan="3">
				<% 
				for(int i=0,no=1; no<attache_cnt; i++,no++) {
					if(icon.equals("E"))
						out.println("<input type='file' name='attachfile"+no+"' size='42'>");

					if((i<cnt) && (addFile[i][0].length() != 0)) {		//등록된 첨부가 있을때
						out.print("&nbsp;<input type='checkbox' name='delfile"+no+"' value='"+addFile[i][3]+"'>");
						out.print("<a href='../psm/attach_download.jsp?fname="+addFile[i][0]);
						out.print("&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]);
						out.print("&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
						out.println("<br>"); 
					}
				}
				%>			   
			   </td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>
<input type='hidden' name='fname' value='<%=fname%>'>
<input type='hidden' name='sname' value='<%=sname%>'>
<input type='hidden' name='ftype' value='<%=ftype%>'>
<input type='hidden' name='fsize' value='<%=fsize%>'>
<input type='hidden' name='env_status' value='<%=env_status%>'> <!-- 과제종류 -->
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
	history.back(-1);
}

//등록하기
function sendSave()
{
	var psm_status = document.eForm.psm_status.value;
	if(psm_status == '1') { alert('등록상태 입니다. 수정을 진행하십시오.'); return; }
	else if(psm_status == '11') { alert('상신상태 입니다.'); return; }

	var f = document.eForm;
	var comp_name = f.comp_name.value;
	if(comp_name == '') { alert('과제고객이 입력되지 않았습니다.'); return; }
	var psm_korea = f.psm_korea.value; 
	if(psm_korea == '') { alert('과제명(한글)이 입력되지 않았습니다.'); f.psm_korea.focus(); return; }
	var psm_english = f.psm_english.value;
	if(psm_english == '') { alert('과제명(영문)이 입력되지 않았습니다.'); f.psm_korea.focus(); return; }
	var psm_mgr = f.psm_mgr.value;
	if(psm_mgr == '') { alert('과제담당자이 입력되지 않았습니다.'); return; }
	var psm_budget = f.psm_budget.value;
	if(psm_budget == '') { alert('과제예산담당자이 입력되지 않았습니다.'); return; }
	var psm_pm = f.psm_pm.value;
	if(psm_pm == '') { alert('과제PM이 입력되지 않았습니다.'); return; }
	var psm_desc = f.psm_desc.value;
	if(psm_desc.length < 10) { alert('과제설명이 입력되지 않았습니다. 10자이상 입력하십시오.'); return; }
	var plan_sum = f.plan_sum.value;
	if(plan_sum.length < 6) { alert('예산 총금액이 입력되지 않았습니다. '); f.plan_material.focus(); return; }
	var plan_material = f.plan_material.value;
	if(plan_material.length < 6) { 
		alert('재료비 예산금액이 입력되지 않았습니다. '); f.plan_material.focus(); return; 
	}

	var env_status = f.env_status.value;
	if(env_status == '2') {		//정식과제
		var contract_name = f.contract_name.value;
		if(contract_name == '') { alert('계약명이 입력되지 않았습니다. '); f.contract_name.focus(); return; }
	}

	//금액에서 ','없애기
	var psum = unCommaObj(document.eForm.plan_sum.value);		//계획총액
	var plab = unCommaObj(document.eForm.plan_labor.value);		//계획인건비
	var pmat = unCommaObj(document.eForm.plan_material.value);	//계획재료비
	var pcst = unCommaObj(document.eForm.plan_cost.value);		//계획비용
	var pant = unCommaObj(document.eForm.plan_plant.value);		//계획시설비
	var cprs = unCommaObj(document.eForm.contract_price.value);	//계약금액

	document.eForm.action='../servlet/PsmBaseInfoServlet';
	document.eForm.mode.value='psm_write';
	document.eForm.plan_sum.value=psum;
	document.eForm.plan_labor.value=plab;
	document.eForm.plan_material.value=pmat;
	document.eForm.plan_cost.value=pcst;
	document.eForm.plan_plant.value=pant;
	document.eForm.contract_price.value=cprs;
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//수정하기
function sendModify()
{
	var psm_status = document.eForm.psm_status.value;
	if(psm_status == 'S') { alert('초기등록상태 입니다. 등록을 진행하십시오.'); return; }
	else if(psm_status == '11') { alert('상신상태 입니다.'); return; }

	var f = document.eForm;
	var comp_name = f.comp_name.value;
	if(comp_name == '') { alert('과제고객이 입력되지 않았습니다.'); return; }
	var psm_korea = f.psm_korea.value;
	if(psm_korea == '') { alert('과제명(한글)이 입력되지 않았습니다.'); f.psm_korea.focus(); return; }
	var psm_english = f.psm_english.value;
	if(psm_english == '') { alert('과제명(영문)이 입력되지 않았습니다.'); f.psm_korea.focus(); return; }
	var psm_mgr = f.psm_mgr.value;
	if(psm_mgr == '') { alert('과제담당자이 입력되지 않았습니다.'); return; }
	var psm_budget = f.psm_budget.value;
	if(psm_budget == '') { alert('과제예산담당자이 입력되지 않았습니다.'); return; }
	var psm_pm = f.psm_pm.value;
	if(psm_pm == '') { alert('과제PM이 입력되지 않았습니다.'); return; }
	var psm_desc = f.psm_desc.value;
	if(psm_desc.length < 10) { alert('과제설명이 입력되지 않았습니다. 10자이상 입력하십시오.'); return; }
	var plan_sum = f.plan_sum.value;
	if(plan_sum.length < 6) { alert('예산 총금액이 입력되지 않았습니다. '); f.plan_material.focus(); return; }
	var plan_material = f.plan_material.value;
	if(plan_material.length < 6) { 
		alert('재료비 예산금액이 입력되지 않았습니다. '); f.plan_material.focus(); return; 
	}

	var env_status = f.env_status.value;
	if(env_status == '2') {		//정식과제
		var contract_name = f.contract_name.value;
		if(contract_name == '') { alert('계약명이 입력되지 않았습니다. '); f.contract_name.focus(); return; }
	}

	if(!confirm('수정하시겠습니까?'))return;

	//금액에서 ','없애기
	var psum = unCommaObj(document.eForm.plan_sum.value);		//계획총액
	var plab = unCommaObj(document.eForm.plan_labor.value);		//계획인건비
	var pmat = unCommaObj(document.eForm.plan_material.value);	//계획재료비
	var pcst = unCommaObj(document.eForm.plan_cost.value);		//계획비용
	var pant = unCommaObj(document.eForm.plan_plant.value);		//계획시설비
	var cprs = unCommaObj(document.eForm.contract_price.value);	//계약금액
	
	document.eForm.action='../servlet/PsmBaseInfoServlet';
	document.eForm.mode.value='psm_modify';
	document.eForm.plan_sum.value=psum;
	document.eForm.plan_labor.value=plab;
	document.eForm.plan_material.value=pmat;
	document.eForm.plan_cost.value=pcst;
	document.eForm.plan_plant.value=pant;
	document.eForm.contract_price.value=cprs;
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//삭제하기
function sendDelete()
{
	var psm_status = document.eForm.psm_status.value;
	if(psm_status == 'S') { alert('초기등록상태 입니다. 등록을 진행하십시오.'); return; }
	else if(psm_status == '11') { alert('상신상태 입니다.'); return; }

	if(!confirm('삭제하시겠습니까?'))return;

	document.eForm.action='../servlet/PsmBaseInfoServlet';
	document.eForm.mode.value='psm_delete';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//상신
function sendRequest()
{
	var psm_status = document.eForm.psm_status.value;
	if(psm_status != '1') { alert('등록상태 일때 만 가능합니다.'); return; }
	else if(psm_status == '11') { alert('상신상태 입니다.'); return; }

	if(!confirm('상신하시겠습니까?'))return;

	document.eForm.action='../servlet/PsmBaseInfoServlet';
	document.eForm.mode.value='psm_request';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//승인
function sendApproval()
{
	var psm_status = document.eForm.psm_status.value;
	if(psm_status != '11') { alert('상신상태 일때 만 가능합니다.'); return; }

	var mgr = '<%=mgr%>';
	if(mgr != '과제담당자') { alert('과제 상태관리권한이 없습니다.'); return; }

	if(!confirm('승인하시겠습니까?'))return;

	document.eForm.action='../servlet/PsmBaseInfoServlet';
	document.eForm.mode.value='psm_approval';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//반려
function sendReject()
{
	var psm_status = document.eForm.psm_status.value;
	if(psm_status != '11') { alert('상신상태 일때 만 가능합니다.'); return; }

	var mgr = '<%=mgr%>';
	if(mgr != '과제담당자') { alert('과제 상태관리권한이 없습니다.'); return; }

	if(!confirm('반려하시겠습니까?'))return;

	document.eForm.action='../servlet/PsmBaseInfoServlet';
	document.eForm.mode.value='psm_reject';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//목록으로
function sendList()
{
	document.eForm.action='../servlet/PsmBaseInfoServlet';
	document.eForm.mode.value='psm_bylist';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../psm/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = wopen(strUrl, "Calendar", "180", "250", "scrollbar=no,toolbar=no,status=no,resizable=no");
}
//과제담당자 찾기
function searchMGR()
{
	var strUrl = "../psm/searchName.jsp?target=eForm.psm_mgr";
	newWIndow = wopen(strUrl, "MgrInfo", "260", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//예산담당자 찾기
function searchBUDGET()
{
	var strUrl = "../psm/searchName.jsp?target=eForm.psm_budget";
	newWIndow = wopen(strUrl, "BudgetInfo", "260", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//PM 찾기
function searchPM()
{
	var strUrl = "../psm/searchName.jsp?target=eForm.psm_pm";
	newWIndow = wopen(strUrl, "PmInfo", "260", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
}

//과제고객 찾기
function searchPsmInfo()
{
	var env_status = document.eForm.env_status.value;
	var strUrl = "../psm/psmCategory.jsp?target=eForm.comp_name/eForm.comp_category&env_status="+env_status;
	newWIndow = wopen(strUrl, "PsmInfo", "400", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
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