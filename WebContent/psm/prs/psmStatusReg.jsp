<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제진행상태 편집"		
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
	com.anbtech.psm.entity.psmStatusTable psm;
	psm = (psmStatusTable)request.getAttribute("STATUS_table");

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
	String psm_status = psm.getPsmStatus(); if(psm_status.length()==0) psm_status="1";
	String change_desc = psm.getChangeDesc();
	String change_date = psm.getChangeDate();
	
	//처음은 신규등록상태로
	if(psm_status.equals("1")) { 
		change_desc="";
		pid = anbdt.getID();
	}

	//수정,삭제 가능여부 판단(1일자동마감) : 수정,삭제할때 사용
	String to_date = anbdt.getDate();
	String mod = "N";
	if(to_date.equals(change_date)) mod = "Y";

	//현재내용이 등록상태인지 아닌지 판단하기 : 등록할때 사용
	String reg = "N";
	if(change_desc.length() == 0) reg = "Y";
	
	//승인권자인지 판단하기
	String mgr = "";
	if(psm_pm.indexOf(sl.id) != -1) mgr = "승인권자";

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
	com.anbtech.psm.entity.psmStatusTable table;
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("STATUS_List");
	table = new psmStatusTable();
	Iterator table_iter = table_list.iterator();
%>

<HTML>
<HEAD><TITLE>과제진행상태 편집</TITLE>
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
					<TD valign='middle' class="title"><img src="../psm/images/blet.gif"> <%=title%> 상태등록</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
				<% if(psm_status.equals("1")) { //신규등록 %>
					<a href="javascript:sendSave();"><img src="../psm/images/bt_reg.gif" border=0 align='absmiddle'></a>
				<% } else if(mod.equals("Y")) { //수정,삭제 %>
					<a href="javascript:sendModify();"><img src="../psm/images/bt_modify.gif" border=0 border=0 align='absmiddle'></a>
					<a href="javascript:sendDelete();"><img src="../psm/images/bt_del.gif" border=0 border=0 align='absmiddle'></a>
					<a href="javascript:sendClear();"><img src="../psm/images/bt_cancel.gif" border=0 border=0 align='absmiddle'></a>
				<% } else {						//신규등록모드로 %>
					<a href="javascript:sendClear();"><img src="../psm/images/bt_cancel.gif" border=0 border=0 align='absmiddle'></a>
				<% } %>
					<a href="javascript:sendList();"><img src="../psm/images/bt_list.gif" border=0 border=0 align='absmiddle'></a>
				
				
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
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
			   <td width="37%" height="25" class="bg_04"><%=psm_code%>
					<input class='text_01' type='hidden' name='psm_code' value='<%=psm_code%>'></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제종류</td>
			   <td width="37%" height="25" class="bg_04">
					<select name="psm_type_sel" style=font-size:9pt;color="black";>
					<%
						String sel = "";
						for(int i=0; i<env_cnt; i++) {
							if(pjt_list[i][2].equals(psm_type)) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+pjt_list[i][1]+"|"+pjt_list[i][2]+"'>");
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
					<a href="javascript:searchPsmInfo();"><img src="../psm/images/bt_search.gif" border="0" align='absbottom'></a></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제카테고리</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='comp_category' value='<%=comp_category%>' readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제명(한글)</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_korea' maxlength='50' value='<%=psm_korea%>'></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제명(영문)</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_english' maxlength='50' value='<%=psm_english%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제시작일</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_start_date' value='<%=psm_start_date%>'  size='12' readonly>
					<A Href="Javascript:OpenCalendar('psm_start_date');"><img src="../psm/images/bt_calendar.gif" border="0" align='absbottom'></A></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제완료일</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_end_date' value='<%=psm_end_date%>' size='12' readonly>
					<A Href="Javascript:OpenCalendar('psm_end_date');"><img src="../psm/images/bt_calendar.gif" border="0" align='absbottom'></A></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제담당자</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='psm_mgr' value='<%=psm_mgr%>' size=15 readonly>
					<a href="javascript:searchMGR();"><img src="../psm/images/bt_search.gif" border="0" align='absbottom'></a></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif" rowspan=3>변경사유</td>
			   <td width="37%" height="25" class="bg_04" rowspan=3>
					<TEXTAREA class='text_01' NAME='change_desc' rows=3 cols=42 style='border:1px solid #787878;'><%=change_desc%></TEXTAREA></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">진행상태</td>
			   <td width="37%" height="25" class="bg_04">
					<select name='psm_status'>
					<%
						String[] list = {"","2","3","4","5","6"};
						String[] list_name = {"상태선택","진행","재진행","완료","보류","취소"};
						String csel="";
						
						for(int i=0; i<list.length; i++) {
							if(psm_status.equals(list[i])) csel="selected";
							else csel = "";
							out.println("<option value='"+list[i]+"' "+csel+" >"+list_name[i]+"</option>");
						}
					%></select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
	<TR><TD height='1' bgcolor='#FFFFFF'></TD></TR>
	<TR><TD height='25'></TD></TR>
</table>

<!-- list -->

<TABLE height="" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR><TD height='1' bgcolor='#9DA9B9'></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=23>
						<TD noWrap width=90 align=middle class='list_title'>과제코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>과제명(한글)</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>과제종류</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=120 align=middle class='list_title'>과제고객</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>카테고리</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=170 align=middle class='list_title'>과제기간</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>과제상태</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>등록일자</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	String status = "";
	while(table_iter.hasNext()){
		table = (psmStatusTable)table_iter.next();
		status = table.getPsmStatus();
		if(status.equals("1")) status = "미진행";
		else if(status.equals("2")) status = "진행";
		else if(status.equals("3")) status = "재진행";
		else if(status.equals("4")) status = "완료";
		else if(status.equals("5")) status = "보류";
		else if(status.equals("6")) status = "취소";
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'><%=table.getPsmCode()%></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPsmKorea()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPsmType()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getCompName()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getCompCategory()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPsmStartDate()%> ~ <%=table.getPsmEndDate()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=status%></td>
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

<!-- form hidden -->
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='psm_type' value=''>
<input type='hidden' name='psm_pm' value='<%=psm_pm%>'>
<input type='hidden' name='psm_budget' value='<%=psm_budget%>'>

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
	var comp_name = f.comp_name.value;
	if(comp_name == '') { alert('과제고객이 입력되지 않았습니다.'); return; }
	var psm_korea = f.psm_korea.value;
	if(psm_korea == '') { alert('과제명(한글)이 입력되지 않았습니다.'); f.psm_korea.focus(); return; }
	var psm_english = f.psm_english.value;
	if(psm_english == '') { alert('과제명(영문)이 입력되지 않았습니다.'); f.psm_korea.focus(); return; }
	var psm_pm = f.psm_pm.value;
	if(psm_pm == '') { alert('과제PM이 입력되지 않았습니다.'); return; }
	var psm_status = f.psm_status.value;
	if(psm_status == '') { alert('진행상태를 선택하십시오.'); return; }
	var change_desc = f.change_desc.value;
	if(change_desc.length < 10) { alert('변경사유가 입력되지 않았습니다. 10자이상 입력하십시오.'); return; }
	
	var psm_type_sel = document.eForm.psm_type_sel.value;
	var psm_sep = psm_type_sel.split("|");
	var psm_type = psm_sep[1];
	 
	if(!confirm('등록하시겠습니까?'))return;

	document.eForm.action='../servlet/PsmStatusServlet';
	document.eForm.mode.value='sts_write';
	document.eForm.psm_type.value=psm_type;
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
	var comp_name = f.comp_name.value;
	if(comp_name == '') { alert('과제고객이 입력되지 않았습니다.'); return; }
	var psm_korea = f.psm_korea.value;
	if(psm_korea == '') { alert('과제명(한글)이 입력되지 않았습니다.'); f.psm_korea.focus(); return; }
	var psm_english = f.psm_english.value;
	if(psm_english == '') { alert('과제명(영문)이 입력되지 않았습니다.'); f.psm_korea.focus(); return; }
	var psm_pm = f.psm_pm.value;
	if(psm_pm == '') { alert('과제PM이 입력되지 않았습니다.'); return; }
	var psm_status = f.psm_status.value;
	if(psm_status == '') { alert('진행상태를 선택하십시오.'); return; }
	var change_desc = f.change_desc.value;
	if(change_desc.length < 10) { alert('변경사유 입력되지 않았습니다. 10자이상 입력하십시오.'); return; }
	

	var psm_type_sel = document.eForm.psm_type_sel.value;
	var psm_sep = psm_type_sel.split("|");
	var psm_type = psm_sep[1];

	if(!confirm('수정하시겠습니까?'))return;

	document.eForm.action='../servlet/PsmStatusServlet';
	document.eForm.mode.value='sts_modify';
	document.eForm.psm_type.value=psm_type;
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

	if(!confirm('삭제하시겠습니까?'))return;

	document.eForm.action='../servlet/PsmStatusServlet';
	document.eForm.mode.value='sts_delete';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//등록준비하기
function sendClear()
{
	document.eForm.action='../servlet/PsmStatusServlet';
	document.eForm.mode.value='sts_prewrite';
	document.eForm.pid.value='';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//편집[수정,삭제]하기
function psmView(pid)
{
	document.eForm.action='../servlet/PsmStatusServlet';
	document.eForm.mode.value='sts_prewrite';
	document.eForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//목록으로
function sendList()
{
	document.eForm.action='../servlet/PsmStatusServlet';
	document.eForm.mode.value='master_list';
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
	newWIndow = wopen(strUrl, "UserInfo", "260", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//과제고객 찾기
function searchPsmInfo()
{
	var psm_type_sel = document.eForm.psm_type_sel.value;
	var psm_sep = psm_type_sel.split("|");
	var env_status = psm_sep[0];
	
	var strUrl = "../psm/psmCategory.jsp?target=eForm.comp_name/eForm.comp_category&env_status="+env_status;
	newWIndow = wopen(strUrl, "CustInfo", "400", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
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