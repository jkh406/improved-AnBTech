<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "노드 작업일지 리스트"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();

	//---------------------------------------------------------
	//	파라미터 받기
	//--------------------------------------------------------
	String pjt_code="",pjt_name="",node_code="",pjtWord="",sItem="",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");

	para = new projectTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();				//프로젝트코드
		pjt_name = para.getPjtName();				//프로젝트명
		node_code = para.getNodeCode();				//노드코드
		pjtWord = para.getPjtword();				//주간[W]/월간[M] 구분
		sItem = para.getSitem();		
		sWord = para.getSword();
	}

	//----------------------------------------------------
	//	해당과제/노드의 실적정보 LIST읽기 : pjt_event
	//----------------------------------------------------
	com.anbtech.pjt.entity.projectTable evt;
	ArrayList evt_list = new ArrayList();
	evt_list = (ArrayList)request.getAttribute("EVENT_List");
	evt = new projectTable();
	Iterator evt_iter = evt_list.iterator();
	int evt_cnt = evt_list.size();

	String[][] event = new String[evt_cnt][6];
	int e = 0;
	while(evt_iter.hasNext()) {
		evt = (projectTable)evt_iter.next();
		event[e][0] = evt.getUserName();	if(event[e][0] == null) event[e][0] = "";	
		event[e][1] = evt.getInDate();		if(event[e][1] == null) event[e][1] = "";		
		event[e][2] = evt.getWmType();		if(event[e][2] == null) event[e][2] = "";	
		event[e][3] = evt.getEvtContent();  if(event[e][3] == null) event[e][3] = "";	
			if(event[e][3].length() > 30) event[e][3] = event[e][3].substring(0,30) + "...";
		event[e][4] = evt.getView()+" "+evt.getModify()+" "+evt.getDelete();
			if(event[e][4] == null) event[e][4] = "";	
		event[e][5] = Double.toString(evt.getProgress()*100);
		e++;
	}
	//가장 최근에 입력한 날자 구하기 (동일날자 입력 방지위해)
	String lred = "";
	if(evt_cnt > 0)
		lred = str.repWord(event[0][1],"-","");				//최종실적 입력한 날자

	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	//----------------------------------------------------
	//	해당과제 Activity리스트 읽기 : pjt_schedule
	//----------------------------------------------------
	String psd="",csd="";	//계획시작일,수정시작일
	String user_id="",user_name="",pjt_node_mbr="",node_status="",red="";
	com.anbtech.pjt.entity.projectTable act;
	ArrayList act_list = new ArrayList();
	act_list = (ArrayList)request.getAttribute("ACT_List");
	act = new projectTable();
	Iterator act_iter = act_list.iterator();
	int act_cnt = act_list.size();

	String[][] activity = new String[act_cnt][8];
	int a = 0;
	while(act_iter.hasNext()) {
		act = (projectTable)act_iter.next();
		activity[a][0] = act.getPjtCode();	
		activity[a][1] = act.getChildNode();			
		activity[a][2] = act.getNodeName();	

		activity[a][3] = act.getUserId();			user_id += activity[a][3]+"|";
		activity[a][4] = act.getUserName();			user_name += activity[a][4]+"|";
		activity[a][5] = act.getPjtNodeMbr();		pjt_node_mbr += activity[a][5].trim()+"|"; 
		activity[a][6] = act.getNodeStatus();		node_status += activity[a][6]+"|";
		activity[a][7] = act.getRstEndDate();		red += activity[a][7]+"|";

		//실적입력가능 판단키 위해
		if(node_code.equals(activity[a][1])) {
			psd = act.getPlanStartDate();	if(psd == null) psd = "";
			csd = act.getChgStartDate();	if(csd == null) csd = "";
		}
		a++;
	}
	pjt_node_mbr = str.repWord(pjt_node_mbr,";","/");
	pjt_node_mbr = str.repWord(pjt_node_mbr,"\r","");
	pjt_node_mbr = str.repWord(pjt_node_mbr,"\n","");

	String tsd = anbdt.getDateNoformat();		//오늘날자 : 실적입력가능여부 비교하기 위해

	//----------------------------------------------------
	//	노드 승인요청 알려주기
	//----------------------------------------------------
	String ReqApp = request.getParameter("ReqApp"); if(ReqApp == null) ReqApp = "";

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//노드승인요청 메시지 알려주기
var RA = '<%=ReqApp%>';
if(RA == 'Y') { alert('노드완료 승인요청이 되었습니다.'); }
else if(RA == 'A') { alert('승인완료된 노드로 입력할 수 없습니다.'); }

//노드선택하기
function goNode()
{
	//선택된 sel : pjt_code|node_code
	var node = document.nForm.node.value;
	var pjtnode = node.split('|');

	document.nForm.action='../servlet/projectEventServlet';
	document.nForm.mode.value='PSM_EL';
	document.nForm.pjt_code.value=pjtnode[0];
	document.nForm.node_code.value=pjtnode[1];
	document.nForm.submit();

}
//페이지 이동하기
function goPage(a) 
{
	document.sForm.action='../servlet/projectEventServlet';
	document.sForm.mode.value='PSM_EL';
	document.sForm.page.value=a;
	document.sForm.submit();
}
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/projectEventServlet';
	document.sForm.mode.value='PSM_EL';
	document.sForm.page.value='1';
	document.sForm.submit();
}
//해당노드 실적입력하기
function eventWrite()
{
	//선택된 sel : pjt_code|node_code
	var node = document.nForm.node.value;
	var pjtnode = node.split('|');

	//선택한 인텍스 번호
	var num = document.nForm.node.selectedIndex;
	
	//노드상태 판단하기 : 진행중인 노드만 입력가능
	var status = '<%=node_status%>';
	var node_status = status.split('|');		//노드상태
	var red = '<%=red%>';
	var node_red = red.split('|');				//노드종료일

	//완료노드와 승인대기노드 검사
	if(node_status[num] == '2') { alert('완료된 노드는 실적을 입력할 수 없습니다.'); return; }
	else if(node_status[num] == '11') {
		alert('승인대기 노드는 실적을 입력할 수 없습니다.'); return;
	}

	//노드담당자및 개발멤버만이 입력가능
	var login_id = '<%=login_id%>';			//노드담당자 전체
	var uid = '<%=user_id%>';
	var user_id = uid.split('|');
	var uid_tag = 'N';
	if(login_id.indexOf(user_id[num]) != -1) uid_tag = 'Y';		//노드담당자인지 판단

	var mbr = '<%=pjt_node_mbr%>';			//개발멤버 전체
	var mbr_id = mbr.split('|');
	var pjt_mbr_id = mbr_id[num];			//노드개발멤버
	var mid_cnt = 0;
	for(i=0; i<pjt_mbr_id.length; i++) { if(pjt_mbr_id.charAt(i) == '/') mid_cnt++; }

	var mbr_tag = 'N';
	if(pjt_mbr_id.length > 0) {
		var mid = pjt_mbr_id.split('/');
		for(i=0; i<mid_cnt; i++) { if(login_id.indexOf(mid[i]) != -1) mbr_tag = 'Y'; }
	}

	if((uid_tag == 'N') && (mbr_tag == 'N')){ 
		alert('노드입력은 노드개발멤버만이 입력할 수 있습니다.'); return; 
	} 

	//노드작업지시가 없을 경우 계획시작일 또는 수정시작일으 비교하여 입력가능여부 판단하기
	//수정일이 없으면 계획일이, 있으면 수정일이 기준임
	var tsd = '<%=tsd%>';			//금일
	var lred = '<%=lred%>';			//최종 실적입력한 날자 
	var psd = '<%=psd%>';			//계획시작일
	var csd = '<%=csd%>';			//수정시작일
	var ssd = psd;
	if(csd.length != 0) ssd = csd;	//기준일 
	if(node_status[num] == '0') {	
		if(ssd > tsd) { 
			var cmt = '실적입력은 노드시작지시가 있거나, 계획 or 수정시작일이 금일보다 크거나 같을때 가능합니다.\n\n';
			cmt += '실적입력가능 시작일 : '+ssd.substring(0,4)+'/'+ssd.substring(4,6)+'/'+ssd.substring(6,8);
			alert(cmt); return; 
		};
	}

	if(tsd == lred) { alert('같은날자에 중복으로 실적/승인을 입력할 수 없습니다.'); return; }
	
	document.nForm.action='../servlet/projectEventServlet';
	document.nForm.mode.value='PSM_EWV';
	document.nForm.pjt_code.value=pjtnode[0];
	document.nForm.node_code.value=pjtnode[1];
	document.nForm.node_status.value=node_status[num];
	document.nForm.submit();
}
//해당노드 완료 승인요청준비
function nodeAppReq()
{
	//선택된 sel : pjt_code|node_code
	var node = document.nForm.node.value;
	var pjtnode = node.split('|');

	//선택한 인텍스 번호
	var num = document.nForm.node.selectedIndex;
	
	//노드담당자 완료승인요청 가능
	var login_id = '<%=login_id%>';			//노드담당자 전체
	var uid = '<%=user_id%>';
	var user_id = uid.split('|');
	var uid_tag = 'N';
	if(login_id.indexOf(user_id[num]) != -1) uid_tag = 'Y';		//노드담당자인지 판단

	if(uid_tag == 'N'){ 
		alert('노드담당자만이 완료승인요청을 할 수 있습니다.'); return; 
	} 

	//노드상태 판단하기 : 진행중인 노드만 입력가능
	var status = '<%=node_status%>';
	var node_status = status.split('|');		//노드 상태
	var red = '<%=red%>';
	var node_red = red.split('|');				//노드종료일

	if(node_status[num] == '0') { alert('진행전 노드는 완료승인요청을 할 수 없습니다.'); return; }
	else if(node_status[num] == '2') { alert('완료된 노드는 완료승인요청을 할 수 없습니다.'); return; }
	else if(node_status[num] == '11') {
		alert('승인대기 노드는 승인요청을 할 수 없습니다.'); return;
	}

	//같은날자 중복 입력 검사
	var tsd = '<%=tsd%>';			//금일
	var lred = '<%=lred%>';			//최종 실적입력한 날자 
	if(tsd == lred) { alert('같은날자에 중복으로 실적/승인을 입력할 수 없습니다.'); return; }

	document.nForm.action='../servlet/projectEventServlet';
	document.nForm.mode.value='PSM_EAV';
	document.nForm.pjt_code.value=pjtnode[0];
	document.nForm.node_code.value=pjtnode[1];
	document.nForm.node_status.value=node_status[num];
	document.nForm.submit();
}
//해당노드의 개별이력 상세보기
function eventView(pid,wm_type)
{
	sParam = "strSrc=../servlet/projectEventServlet&pid="+pid+"&mode=PSM_EV";
	if((wm_type == 'A') || (wm_type == 'R')) {	
		showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:640px;resizable=0");
	} else { 
		showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:520px;resizable=0");
	}
}
//해당노드의 개별이력 수정[당일작성/당일수정]
function eventModify(pid)
{
	document.aForm.action='../servlet/projectEventServlet';
	document.aForm.mode.value='PSM_EMV';
	document.aForm.pid.value=pid;
	document.aForm.submit();
}
//해당노드의 개별이력 삭제[당일작성/당일삭제]
function eventDelete(pid)
{
	document.aForm.action='../servlet/projectEventServlet';
	document.aForm.mode.value='PSM_ED';
	document.aForm.pid.value=pid;
	document.aForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">

<!-- 리스트 -->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> 노드실적 이력정보 &nbsp;&nbsp;&nbsp;&nbsp;[과제명: <%=pjt_code%> <%=pjt_name%>]</TD>
				<TD style="padding-right:10px" align='right' valign='middle'><%=Cpage%>/<%=Tpage%> <img src="../pjt/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD width='2%'>&nbsp;</TD>
				<TD align=left width='53%'>
					<form name="sForm" method="post" style="margin:0">
					<select name="pjtWord" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
					<%
						String[] s_code = {"","W","M","N","A","R"};
						String[] s_name = {"전체","주간실적","월간실적","승인요청","노드승인","승인반려"};
						String tsel = "";
						for(int ti=0; ti<s_code.length; ti++) {
							if(pjtWord.equals(s_code[ti])) tsel = "selected";
							else tsel = "";
							out.println("<option "+tsel+" value='"+s_code[ti]+"'>"+s_name[ti]+"</option>");
						}
					%>
					</select>
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"evt_content","evt_note","evt_issue"};
						String[] snames = {"주요내용","문제점","이슈사항"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a> <a href='javascript:eventWrite()'><b>실적작성</b></a> 	<a href='javascript:nodeAppReq()'><b>승인요청</b></a>
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pid" size="15" value="">
					<input type="hidden" name="page" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
					<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
					<input type="hidden" name="node_code" size="15" value="<%=node_code%>">
					<input type="hidden" name="user_id" size="15" value="<%=user_id%>">
					<input type="hidden" name="user_name" size="15" value="<%=user_name%>">
					<input type="hidden" name="pjt_node_mbr" size="15" value="<%=pjt_node_mbr%>">
					<input type="hidden" name="node_status" size="15" value="<%=node_status%>">
					</form> 
					</TD>
				<TD align=left width='25%'>
					<form name="nForm" method="post" style="margin:0">
					<select name="node" style=font-size:9pt;color="black"; onChange='javascript:goNode();'>  
					<%	
						String asel = "";
						for(int ai=0; ai<act_cnt; ai++) {
							if(node_code.equals(activity[ai][1])) asel = "selected";
							else asel = "";
							out.println("<option "+asel+" value='"+activity[ai][0]+"|"+activity[ai][1]+"'>");
							out.println(activity[ai][1]+" "+activity[ai][2]+"</option>");
						}
					%>
					</select>
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="sItem" size="15" value="evt_content">
					<input type="hidden" name="sWord" size="15" value="">
					<input type="hidden" name="pjtWord" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="">
					<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
					<input type="hidden" name="node_code" size="15" value="">

					<input type="hidden" name="user_id" size="15" value="">
					<input type="hidden" name="user_name" size="15" value="">
					<input type="hidden" name="pjt_node_mbr" size="15" value="">
					<input type="hidden" name="pjt_status" size="15" value="">
					<input type="hidden" name="node_status" size="15" value="">
					</form>
					</TD>
				<TD width='15%' align='right' style="padding-right:10px">
					<%	if (Cpage <= 1) {	%>		
						<img src='../pjt/images/bt_previous.gif' border='0' align='absmiddle'>
					 <%	} else 	{	%>		
						<a href='javascript:goPage(<%=Cpage-1%>)'>
						<img src='../pjt/images/bt_previous.gif' border='0' align='absmiddle'></a>

					 <%	} if ((Cpage != Tpage) && (Tpage != -1 )) { %>		
							<a href='javascript:goPage(<%=Cpage+1%>)'>
							<img src='../pjt/images/bt_next.gif' border='0' align='absmiddle'></a> 		
					 <%	} else 	{  %>		
							<img src='../pjt/images/bt_next.gif' border='0' align='absmiddle'>
					 <%	} %>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=25>
				<TD noWrap width=60 align=middle class='list_title'>구분</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=60 align=middle class='list_title'>작성자</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>작성일</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>진행율</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>주요내용</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>View</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
		<% if (evt_list.size() == 0) { %>
			<TR vAlign=center height=22>
				 <td colspan='11' align="middle">***** 내용이 없습니다. ****</td>
			</tr> 
		<% } %>	

		<% 
			String type = "";
			for(int i=0; i<evt_cnt; i++) {
				if(event[i][2].equals("W")) type = "주간실적";
				else if(event[i][2].equals("M")) type = "월간실적";
				else if(event[i][2].equals("N")) type = "승인요청";
				else if(event[i][2].equals("A")) type = "노드승인";
				else if(event[i][2].equals("R")) type = "승인반려";
		%>
			<form name="aForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=type%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=event[i][0]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'><%=event[i][1]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;<%=event[i][5]%>%</TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;<%=event[i][3]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'>&nbsp;<%=event[i][4]%></TD>
			</TR>
			<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			}  //for 

		%>
			<input type="hidden" name="mode" size="15" value="">
			<input type="hidden" name="pid" size="15" value="">
			<input type="hidden" name="page" size="15" value="">
			<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
			<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
			<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">

			<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
			<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
			<input type="hidden" name="node_code" size="15" value="<%=node_code%>">
			<input type="hidden" name="user_id" size="15" value="<%=user_id%>">
			<input type="hidden" name="user_name" size="15" value="<%=user_name%>">
			<input type="hidden" name="pjt_node_mbr" size="15" value="<%=pjt_node_mbr%>">
			<input type="hidden" name="node_status" size="15" value="">
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

</body>
</html>
