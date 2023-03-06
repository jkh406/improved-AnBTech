<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "작업 지시 하기"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();

	//---------------------------------------------------------
	//	파라미터 받기
	//--------------------------------------------------------
	String pjt_code="",pjtWord="0",sItem="",sWord="",parent_node="",child_node="",chg_note="";;
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjtWord = para.getPjtword();
		sItem = para.getSitem();
		sWord = para.getSword();
		parent_node = para.getParentNode();
		child_node = para.getChildNode();
		chg_note = para.getChgNote();				//일정변경사유
	}

	//----------------------------------------------------
	//	해당과제 계획일/수정일 및 과제상태 찾기 [기본정보에서 찾기]
	//----------------------------------------------------
	String plan_sd="",plan_ed="",change_sd="",change_ed="";	//계획시작,종료일, 수정시작,종료일
	String pjt_status = "";									//과제진행상태
	com.anbtech.pjt.entity.projectTable gen;
	ArrayList gen_list = new ArrayList();
	gen_list = (ArrayList)request.getAttribute("GEN_List");
	gen = new projectTable();
	Iterator gen_iter = gen_list.iterator();

	if(gen_iter.hasNext()) {
		gen = (projectTable)gen_iter.next();
		plan_sd = gen.getPlanStartDate();		if(plan_sd == null) plan_sd = "";		//계획시작일
		plan_ed = gen.getPlanEndDate();			if(plan_ed == null) plan_ed = "";		//계획종료일
		change_sd = gen.getChgStartDate();		if(change_sd == null) change_sd = "";	//수정시작일
		change_ed = gen.getChgEndDate();		if(change_ed == null) change_ed = "";	//수정종료일
		pjt_status = gen.getPjtStatus();
	}

	//------------------------------------------------------------
	//	해당 과제의 멤버 LIST
	//------------------------------------------------------------
	com.anbtech.pjt.entity.projectTable man;
	ArrayList man_list = new ArrayList();
	man_list = (ArrayList)request.getAttribute("MAN_List");
	man = new projectTable();
	Iterator man_iter = man_list.iterator();
	int man_cnt = man_list.size();

	String[][] member = new String[man_cnt][4];
	String pjt_member = "";		//개발멤버선택을 위해
	int p = 0;
	while(man_iter.hasNext()) {
		man = (projectTable)man_iter.next();
		member[p][0] = man.getPjtMbrType();
		member[p][1] = man.getPjtMbrId();
		member[p][2] = man.getPjtMbrName();
		member[p][3] = man.getPjtMbrJob();
		pjt_member += member[p][0]+"|"+member[p][1]+"|"+member[p][2]+"|"+member[p][3]+";";
		p++;
	}

	//-----------------------------------------------------------
	//	해당 과제의 노드 상세정보 [상세일정 입력위해]
	//-----------------------------------------------------------
	String pid="",pjt_name="",level_no="",node_name="",user_id="",user_name="",pjt_node_mbr="";
	String plan_cnt="",chg_cnt="",rst_cnt="",node_status="",remark="";
	String psd="",ped="",csd="",ced="",rsd="",red="";		//DB상의 계획일,수정일,완료일
	String dpsd="",dped="",dcsd="",dced="",drsd="",dred="";	//화면출력용
	com.anbtech.pjt.entity.projectTable sch;
	ArrayList sch_list = new ArrayList();
	sch_list = (ArrayList)request.getAttribute("NODE_List");
	sch = new projectTable();
	Iterator sch_iter = sch_list.iterator();

	if(sch_iter.hasNext()) {
		sch = (projectTable)sch_iter.next();

		pid = sch.getPid();
		pjt_code = sch.getPjtCode();
		pjt_name = sch.getPjtName();
		parent_node = sch.getParentNode();
		child_node = sch.getChildNode();
		level_no = sch.getLevelNo();
		node_name = sch.getNodeName();
		user_id = sch.getUserId();					if(user_id == null) user_id = "";
		user_name = sch.getUserName();				if(user_name == null) user_name = "";
		pjt_node_mbr = sch.getPjtNodeMbr();			if(pjt_node_mbr == null) pjt_node_mbr = "";

		psd = sch.getPlanStartDate();	if(psd == null) psd = "";
		if(psd.length() != 0) dpsd = psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		else dpsd = "";
		ped = sch.getPlanEndDate();	if(ped == null) ped = "";
		if(ped.length() != 0) dped = ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);
		else dped = "";

		csd = sch.getChgStartDate();	if(csd == null) csd = "";
		if(csd.length() != 0) dcsd = csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		else dcsd = "";
		ced = sch.getChgEndDate();	if(ced == null) ced = "";
		if(ced.length() != 0) dced = ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		else dced = "";

		rsd = sch.getRstStartDate();	if(rsd == null) rsd = anbdt.getDate(0);
		if(rsd.length() != 0) drsd = rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		else drsd = anbdt.getDate(0);
		red = sch.getRstEndDate();	if(red == null) red = "";
		if(red.length() != 0) dred = red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);
		else dred = "";

		plan_cnt = Integer.toString(sch.getPlanCnt());
		chg_cnt = Integer.toString(sch.getChgCnt());
		rst_cnt = Integer.toString(sch.getResultCnt());
		node_status = sch.getNodeStatus();	if(node_status == null) node_status = "0";
		remark = sch.getRemark();			if(remark == null) remark = "";
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//일정변경내용 수정하기
//작업지시 입력하기
function orderJob()
{
	var psd = '<%=psd%>';								//계획기간 시작일
	var ped = '<%=ped%>';								//계획기간 종료일
	var csd = document.eForm.chg_start_date.value;		//수정기간 시작일
	for(i=0;i<2;i++) csd = csd.replace('/','');	
	var ced = document.eForm.chg_end_date.value;		//수정기간 완료일
	for(i=0;i<2;i++) ced = ced.replace('/','');	
	if(csd > ced) { alert('수정기간의 시작일과 완료일 입력에 잘못있습니다.'); return; } 

	var rsd = document.eForm.rst_start_date.value;		//실적기간 시작일
	for(i=0;i<2;i++) rsd = rsd.replace('/','');	
	var red = document.eForm.rst_end_date.value;		//실적기간 완료일
	for(i=0;i<2;i++) red = red.replace('/','');	

	//실적기간 시작일이 계획기간내[또는 수정기간내]에 포함되는지 검사
	if((csd.length == 0) && (ced.length == 0)) {		//수정기간이 없을때[계획기간으로 검사]
		if((psd > rsd) || (ped < rsd)) { 
			alert('계획기간에서 벗어난 작업지시로 수정기간을 입력후 진행하십시요.'); return;}
	} else {											//수정기간이 있을때[수정기간으로 검사]
		if((csd > rsd) || (ced < rsd)) { 
			alert('실적시작일을 벗어난 수정기간입니다. 확인후 진행하십시요.'); return;}
	}
	
	//수정기간을 입력했을때 기본정보의 계획일정 및 수정일정과 비교하여 메시지 알려주기
	var plan_sd='<%=plan_sd%>';	
	var plan_ed='<%=plan_ed%>';
	var change_sd='<%=change_sd%>';
	var change_ed='<%=change_ed%>';

	var pnsd = plan_sd.substring(0,4)+'/'+plan_sd.substring(4,6)+'/'+plan_sd.substring(6,8);
	var pned = plan_ed.substring(0,4)+'/'+plan_ed.substring(4,6)+'/'+plan_ed.substring(6,8);
	var cned = change_sd.substring(0,4)+'/'+change_sd.substring(4,6)+'/'+change_sd.substring(6,8);
	var cned = change_ed.substring(0,4)+'/'+change_ed.substring(4,6)+'/'+change_ed.substring(6,8);

	if((csd.length != 0) && (ced.length != 0)) {		//수정기간을 다시 입력했을때
		if(change_sd.length == 0) {		//수정계획이 입력되지 않으면 계획일과 비교
			if(csd < plan_sd) { 
				alert('과제 기본정보의 계획시작일에서 벗어난 일자입니다.\n\n[계획기간: '+pnsd+' ~ '+pned+']'); return; }
			if(ced > plan_ed) { 
				alert('과제 기본정보의 계획종료일에서 벗어난 일자입니다.\n\n[계획기간: '+pnsd+' ~ '+pned+']'); return; }
		} else {						//수정계획이 입력된 경우
			if(csd < change_sd) { 
				alert('과제 기본정보의 수정시작일에서 벗어난 일자입니다.\n\n[수정기간: '+cned+' ~ '+cned+']'); return; }
			if(ced > change_ed) { 
				alert('과제 기본정보의 수정종료일에서 벗어난 일자입니다.\n\n[수정기간: '+cned+' ~ '+cned+']'); return; }
		}
	}

	//작업지시사항 입력여부 확인
	var order = document.eForm.remark.value;
	if(order.length < 4) { alert('4자이상의 작업지시사항이 필요합니다. 지시사항 입력후 다시 진행하십시요.'); return; }
	
	//기존의 수정일과 입력할 수정일과를 비교하여 다를경우 변경사유 입력여부확인
	var db_csd = '<%=csd%>';		//DB 등록된 변경시작일
	var db_ced = '<%=ced%>';		//DB 등록된 변경종료일
	var chg_note = document.eForm.chg_note.value; 
	if((csd != db_csd) || (ced != db_ced)) {
		if(chg_note.length < 4) {alert('4자이상의 일정변경 사유를 입력하십시요.'); return; }
	}


	//입력처리
	document.eForm.action='../servlet/projectSchServlet';
	document.eForm.mode.value='PSC_J';
	document.eForm.plan_start_date.value=psd;
	document.eForm.plan_end_date.value=ped;
	document.eForm.chg_start_date.value=csd;
	document.eForm.chg_end_date.value=ced;
	document.eForm.rst_start_date.value=rsd;
	document.eForm.rst_end_date.value=red;
	document.eForm.submit();
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pjt/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
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
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> 해당과제 노드작업지시</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width=100%>&nbsp;&nbsp;과제코드 : <%=pjt_code%> 
				&nbsp;&nbsp;&nbsp;&nbsp;과제명 : <%=pjt_name%>
					<% if(!node_status.equals("2")) {		//완료상태가 아니면 %>
						&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:orderJob();"><B>작업지시</B></a>
					<% } %>
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
	<tr><form name="eForm" method="post" style="margin:0">
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">노드이름</td>
			   <td width="90%" height="25" class="bg_04"><%=child_node%> <%=node_name%> &nbsp;&nbsp;&nbsp;&nbsp;
				<%
					String status = "";
					if(node_status.equals("")) status = "미등록";
					else if(node_status.equals("0")) status = "진행전";
					else if(node_status.equals("1")) status = "진행중";
					else if(node_status.equals("2")) status = "완료";
					else if(node_status.equals("3")) status = "DROP";
					else if(node_status.equals("4")) status = "HOLD";
					else if(node_status.equals("5")) status = "SKIP";
					out.print("<font color=blue>[노드상태 : "+status+"]</font>"); 
				%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">담 당 자</td>
			   <td width="90%" height="25" class="bg_04">
				<%
					String[] grade_no = {"B","C","D","E","F","G"};
					String[] grade_name = {"상기 PL","개발 PL","SUB-PL","전담개발","지원개발","외부인력"};
					
					out.print(user_name+" [");					//이름
					for(int i=0; i<man_cnt; i++) {
						if(user_id.equals(member[i][1])) {	//사번이 같으면
							for(int g=0; g<grade_no.length; g++) {	//직책:담당업무
								if(member[i][0].equals(grade_no[g]))out.print(grade_name[g]+" : ");
							}
							out.println(member[i][3] + "]");
						}
					}
				%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">개발멤버</td>
			   <td width="90%" height="25" class="bg_04">
			   <%
					out.println("<textarea rows=4 name='' value='"+pjt_node_mbr+"' readonly>"+pjt_node_mbr+"</textarea>");
				%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">계획기간</td>
			   <td width="90%" height="25" class="bg_04">
					<input type='text' name='plan_start_date' value='<%=dpsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='plan_end_date' value='<%=dped%>' size=10 readonly></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">수정기간</td>
			   <td width="90%" height="25" class="bg_04">
				<% if(node_status.equals("0")) {		//진행전 %>
					<input type='text' name='chg_start_date' value='<%=dcsd%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_start_date');"><img src="../pjt/img/calendar.gif" border="0" valign='absbottom'></A>&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=dced%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_end_date');"><img src="../pjt/img/calendar.gif" border="0" valign='absbottom'></A>
				<% } else {								//진행이후 %>
					<input type='text' name='chg_start_date' value='<%=dcsd%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_start_date');"><img src="../pjt/img/calendar.gif" border="0" valign='absbottom'></A>&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=dced%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_end_date');"><img src="../pjt/img/calendar.gif" border="0" valign='absbottom'></A>
				<% } %></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">실적기간</td>
			   <td width="90%" height="25" class="bg_04">
					<input type='text' name='rst_start_date' value='<%=drsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='rst_end_date' value='<%=dred%>' size=10 readonly>
					&nbsp;&nbsp;&nbsp;&nbsp;[노드시작지시할때 실적시작일이 등록되며 완료일은 담당자가 입력합니다.]</td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">작업지시사항</td>
			   <td width="90%" height="25" class="bg_04">
					<textarea rows=10 cols=60 name='remark' value='<%=remark%>'><%=remark%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<% if(!node_status.equals("2")){				//노드상태:노드완료가아님	%>
				<tr>
				   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">일정변경<br>사 유</td>
				   <td width="90%" height="25" class="bg_04">
						<textarea rows=3 cols=60 name='chg_note' value=''><%=chg_note%></textarea></td>
				</tr>
				<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<% } %>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
<input type='hidden' name='pjtWord' value='1'>
<input type='hidden' name='sItem' value='<%=sItem%>'>
<input type='hidden' name='sWord' value='<%=sWord%>'>

<input type='hidden' name='parent_node' value='<%=parent_node%>'>
<input type='hidden' name='child_node' value='<%=child_node%>'>
<input type='hidden' name='node_name' value='<%=node_name%>'>
<input type='hidden' name='node_status' value='<%=node_status%>'>
<input type='hidden' name='user_id' value='<%=user_id%>'>
<input type='hidden' name='user_name' value='<%=user_name%>'>
</form>

</body>
</html>
