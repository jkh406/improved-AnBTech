<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "노드승인[진행중과제LIST]"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	String psd="",ped="",csd="",ced="",rsd="",red="";

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjt_code="",pjtWord="",sItem="",sWord="",parent_node="",child_node="";
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
	}

	//-----------------------------------
	//	과제일정정보 편집하기
	//-----------------------------------
	//해당멤버의 전체 과제 LIST
	com.anbtech.pjt.entity.projectTable pjt;
	ArrayList pjt_list = new ArrayList();
	pjt_list = (ArrayList)request.getAttribute("PJT_List");
	pjt = new projectTable();
	Iterator pjt_iter = pjt_list.iterator();
	int pjt_cnt = pjt_list.size();

	String[][] project = new String[pjt_cnt][2];
	int p = 0;
	while(pjt_iter.hasNext()) {
		pjt = (projectTable)pjt_iter.next();
		project[p][0] = pjt.getPjtCode();
		project[p][1] = pjt.getPjtName();
		p++;
	}

	//해당과제의 일정 전체 LIST
	com.anbtech.pjt.entity.projectTable sch;
	ArrayList sch_list = new ArrayList();
	sch_list = (ArrayList)request.getAttribute("SCH_List");
	sch = new projectTable();
	Iterator sch_iter = sch_list.iterator();
	int sch_cnt = sch_list.size();

	String[][] schedule = new String[sch_cnt][22];
	int m = 0;
	while(sch_iter.hasNext()) {
		sch = (projectTable)sch_iter.next();
		schedule[m][0] = sch.getPid();
		schedule[m][1] = sch.getPjtCode();
		schedule[m][2] = sch.getPjtName();
		schedule[m][3] = sch.getParentNode();
		schedule[m][4] = sch.getChildNode();
		schedule[m][5] = sch.getLevelNo();
		schedule[m][6] = sch.getNodeName();
		schedule[m][7] = sch.getUserId();
		schedule[m][8] = sch.getUserName();
		schedule[m][9] = sch.getPjtNodeMbr();

		psd = sch.getPlanStartDate();	if(psd == null) psd = "";
		if(psd.length() != 0)schedule[m][10] = psd.substring(2,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		else schedule[m][10] = "";
		ped = sch.getPlanEndDate();	if(ped == null) ped = "";
		if(ped.length() != 0)schedule[m][11] = ped.substring(2,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);
		else schedule[m][11] = "";

		csd = sch.getChgStartDate();	if(csd == null) csd = "";
		if(csd.length() != 0)schedule[m][12] = csd.substring(2,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		else schedule[m][12] = "";
		ced = sch.getChgEndDate();	if(ced == null) ced = "";
		if(ced.length() != 0)schedule[m][13] = ced.substring(2,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		else schedule[m][13] = "";

		rsd = sch.getRstStartDate();	if(rsd == null) rsd = "";
		if(rsd.length() != 0)schedule[m][14] = rsd.substring(2,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		else schedule[m][14] = "";
		red = sch.getRstEndDate();	if(red == null) red = "";
		if(red.length() != 0)schedule[m][15] = red.substring(2,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);
		else schedule[m][15] = "";

		schedule[m][16] = Integer.toString(sch.getPlanCnt());
		schedule[m][17] = Integer.toString(sch.getChgCnt());
		schedule[m][18] = Integer.toString(sch.getResultCnt());
		schedule[m][19] = sch.getNodeStatus();
		schedule[m][20] = sch.getRemark();
		if(pjt_code.length() == 0) pjt_code = schedule[m][1];
		schedule[m][21] = Double.toString(sch.getProgress()*100);
		m++;
	}

	//과제전체 진행율 구하기
	String pjt_progress = "0.0";
	if(sch_cnt > 0) {
		pjt_progress = schedule[0][21];
		pjt_progress = Double.toString(Double.parseDouble(pjt_progress)+0.001);
		pjt_progress = pjt_progress.substring(0,pjt_progress.indexOf('.')+2);
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/projectNodeAppServlet';
	document.sForm.mode.value='PSN_L';
	document.sForm.submit();
}
//과제선택하기
function goProject()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	document.eForm.action='../servlet/projectNodeAppServlet';
	document.eForm.mode.value='PSN_L';
	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();
}
//과제 보기선택하기
function goView()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var v = document.gForm.v.value;

	document.eForm.action='../servlet/projectNodeAppServlet';
	if(v == 'ganttPP') document.eForm.mode.value='PSN_GPP';			//gantt chart 보기 : 진행율/계획일
	else if(v == 'ganttPC') document.eForm.mode.value='PSN_GPC';	//gantt chart 보기 : 진행율/수정일
	else if(v == 'ganttSP') document.eForm.mode.value='PSN_GSP';	//gantt chart 보기 : 실적일/계획일
	else if(v == 'ganttSC') document.eForm.mode.value='PSN_GSC';	//gantt chart 보기 : 실적일/수정일
	else document.eForm.mode.value='PSN_L';							//text로 보기
	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();
}
//해당노드 이력보기
function detailNode(pjt_code,parent_node,child_node)
{
	document.vForm.action='../servlet/projectNodeAppServlet';
	document.vForm.mode.value='PSN_AV';
	document.vForm.pjt_code.value=pjt_code;
	document.vForm.parent_node.value=parent_node;
	document.vForm.child_node.value=child_node;
	document.vForm.submit();
}
-->
</script>

<LINK href="../pjt/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> 과제진행정보</TD>
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
				<TD width=4>&nbsp;</TD>
				<TD align=left width='280'>
					<form name="sForm" method="post" style="margin:0">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"pjt_name","pjt_code"};
						String[] snames = {"과제이름","과제코드"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pid" size="15" value="">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a>
					</form>
				</TD>
				<TD align=left width=''>
					<form name="eForm" method="post" style="margin:0">과 제 명
					<%
						out.println("<select name='project' onChange='javascript:goProject()' style=font-size:9pt;color='black';>");
						String psel = "";
						for(int i=0; i<pjt_cnt; i++) {
							if(pjt_code.equals(project[i][0])) psel = "selected";
							else psel = "";
							out.print("<option "+psel+" value='"+project[i][0]+"|"+project[i][1]+"'>");
							out.println(project[i][0]+" "+project[i][1]+"</option>");
						}
					%>
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
					<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="">
					<input type="hidden" name="pjt_name" size="15" value="">
					<input type="hidden" name="level_no" size="15" value="0">
					<input type="hidden" name="parent_node" size="15" value="0">
					<input type="hidden" name="pid" size="15" value="">
					</form>
				</TD>
				<TD align=left width=''>
					<form name="gForm" method="post" style="margin:0">
					<select name='v' onChange='javascript:goView()' style=font-size:9pt;color='black';>
						<option selected value='text'>TEXT</option>
						<option value='ganttPP'>그래프[진행율/계획일]</option>
						<option value='ganttPC'>그래프[진행율/수정일]</option>
						<option value='ganttSP'>그래프[실적일/계획일]</option>
						<option value='ganttSC'>그래프[실적일/수정일]</option>
					</select>
					</form>
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
				<TD noWrap width=20 align=middle class='list_title'>NO</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>일정단계 [진행율 <%=pjt_progress%>%]</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=40 align=middle class='list_title'>담당자</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>상태</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>계획기간</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>수정기간</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>실적기간</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=14></TD></TR>
		<% 
			int n = 1;
			String status = "", space = "", progress="";
			for(int i=1; i<sch_cnt; i++) {
				 progress = schedule[i][21];
				 progress = Double.toString(Double.parseDouble(progress)+0.001);
				 progress = progress.substring(0,progress.indexOf('.')+2);
				 if(schedule[i][5].equals("1")) {
					status = ""; space="<IMG src='../pjt/icons/base.gif'>";
					progress = "<font color=blue>["+progress+"%]</font>";
				 } else if(schedule[i][5].equals("2")) {
					 status = ""; space="&nbsp;&nbsp;<IMG src='../pjt/icons/folder.gif'>";
					 progress = "<font color=red>["+progress+"%]</font>";
				 } else if(schedule[i][5].equals("3")) {
					space = "&nbsp;&nbsp;&nbsp;&nbsp;<IMG src='../pjt/icons/page.gif'>";
					if(schedule[i][19].equals("")) status = "미등록";
					else if(schedule[i][19].equals("0")) status = "진행전";
					else if(schedule[i][19].equals("1")) status = "진행중";
					else if(schedule[i][19].equals("2")) status = "완료";
					else if(schedule[i][19].equals("3")) status = "DROP";
					else if(schedule[i][19].equals("4")) status = "HOLD";
					else if(schedule[i][19].equals("5")) status = "SKIP";

					if(progress.equals("100.0") && (schedule[i][19].equals("11"))) 
						status = "<font color=red>승인대기</font>";
					progress = "<font color=green>["+progress+"%]</font>";
				}
		%>
			<form name="vForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=n%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'><%=space%><%=schedule[i][6]%><%=progress%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=schedule[i][8]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=status%></TD>
				<TD><IMG height=1 width=1></TD>
				<% if(schedule[i][5].equals("1")) {					//PHASE 일정 %>
					<TD align=middle height="24" class='list_bg'><font color=blue><%=schedule[i][10]%>~<%=schedule[i][11]%></font></TD>
					<TD><IMG height=1 width=1></TD>
					<TD align=middle height="24" class='list_bg'><font color=blue><%=schedule[i][12]%>~<%=schedule[i][13]%></font></TD>
					<TD><IMG height=1 width=1></TD>
					<TD align=middle height="24" class='list_bg'><font color=blue><%=schedule[i][14]%>~<%=schedule[i][15]%></font></TD>
					<TD><IMG height=1 width=1></TD>
				<% } else if(schedule[i][5].equals("2")) {			//STEP 일정 %>
					<TD align=middle height="24" class='list_bg'><font color=darkyellow><%=schedule[i][10]%>~<%=schedule[i][11]%></font></TD>
					<TD><IMG height=1 width=1></TD>
					<TD align=middle height="24" class='list_bg'><font color=darkyellow><%=schedule[i][12]%>~<%=schedule[i][13]%></font></TD>
					<TD><IMG height=1 width=1></TD>
					<TD align=middle height="24" class='list_bg'><font color=darkyellow><%=schedule[i][14]%>~<%=schedule[i][15]%></font></TD>
					<TD><IMG height=1 width=1></TD>
				<% } else {											//Activity 일정 %>
					<TD align=middle height="24" class='list_bg'><%=schedule[i][10]%>~<%=schedule[i][11]%></TD>
					<TD><IMG height=1 width=1></TD>
					<TD align=middle height="24" class='list_bg'><%=schedule[i][12]%>~<%=schedule[i][13]%></TD>
					<TD><IMG height=1 width=1></TD>
					<TD align=middle height="24" class='list_bg'><%=schedule[i][14]%>~<%=schedule[i][15]%></TD>
					<TD><IMG height=1 width=1></TD>
				<% }  %>
			</TR>
			<TR><TD colSpan=14 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			n++;
			}  //while 

		%>
			<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
			<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
			<input type="hidden" name="mode" size="15" value="">
			<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
			<input type="hidden" name="pjt_name" size="15" value="">
			<input type="hidden" name="level_no" size="15" value="0">
			<input type="hidden" name="parent_node" size="15" value="">
			<input type="hidden" name="child_node" size="15" value="">
			<input type="hidden" name="pid" size="15" value="">
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

</BODY>
</HTML>

