<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제인력정보"		
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
	String msd = "", med = "";

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjt_code="",pjtWord="S",sItem="",sWord="";
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
	} 

	//-----------------------------------
	//	과제인력정보 편집하기
	//-----------------------------------
	//해당PM의 전체 과제 LIST
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

	//해당과제의 구성원 전체 LIST
	com.anbtech.pjt.entity.projectTable man;
	ArrayList man_list = new ArrayList();
	man_list = (ArrayList)request.getAttribute("MAN_List");
	man = new projectTable();
	Iterator man_iter = man_list.iterator();
	int man_cnt = man_list.size();

	String[][] member = new String[man_cnt][15];
	String worker_list = "";
	int m = 0;
	while(man_iter.hasNext()) {
		man = (projectTable)man_iter.next();
		member[m][0] = man.getPid();
		member[m][1] = man.getPjtCode();
		member[m][2] = man.getPjtName();
		member[m][3] = man.getPjtMbrType();

		msd = man.getMbrStartDate();
		if(msd.length() != 0) member[m][4] = msd.substring(0,4)+"/"+msd.substring(4,6)+"/"+msd.substring(6,8);
		else member[m][4] = "";

		med = man.getMbrEndDate();
		if(msd.length() != 0) member[m][5] = med.substring(0,4)+"/"+med.substring(4,6)+"/"+med.substring(6,8);
		else member[m][5] = "";

		member[m][6] = Double.toString(man.getMbrPoration());
		member[m][7] = man.getPjtMbrId();			worker_list += member[m][7]+"|";
		member[m][8] = man.getPjtMbrName();
		member[m][9] = man.getPjtMbrJob();
		member[m][10] = man.getPjtMbrTel();
		member[m][11] = man.getPjtMbrGrade();
		member[m][12] = man.getPjtMbrDiv();
		member[m][13] = man.getModify();
		member[m][14] = man.getDelete();
		m++;
	}

	//해당과제의 1인 구성원 정보
	com.anbtech.pjt.entity.projectTable one;
	ArrayList one_list = new ArrayList();
	one_list = (ArrayList)request.getAttribute("ONE_List");
	one = new projectTable();
	Iterator one_iter = one_list.iterator();
	int one_cnt = one_list.size();

	String[][] worker = new String[one_cnt][13];
	int w = 0;
	while(one_iter.hasNext()) {
		one = (projectTable)one_iter.next();
		worker[w][0] = one.getPid();
		worker[w][1] = one.getPjtCode();
		worker[w][2] = one.getPjtName();
		worker[w][3] = one.getPjtMbrType();

		msd = one.getMbrStartDate();
		if(msd.length() != 0) worker[w][4] = msd.substring(0,4)+"/"+msd.substring(4,6)+"/"+msd.substring(6,8);
		else worker[w][4] = "";

		med = one.getMbrEndDate();
		if(med.length() != 0) worker[w][5] = med.substring(0,4)+"/"+med.substring(4,6)+"/"+med.substring(6,8);
		else worker[w][5] = "";

		worker[w][6] = Double.toString(one.getMbrPoration());
		worker[w][7] = one.getPjtMbrId();		
		worker[w][8] = one.getPjtMbrName();
		worker[w][9] = one.getPjtMbrJob();
		worker[w][10] = one.getPjtMbrTel();
		worker[w][11] = one.getPjtMbrGrade();
		worker[w][12] = one.getPjtMbrDiv();
		w++;
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
	document.sForm.action='../servlet/projectManServlet';
	document.sForm.mode.value='PMA_L';
	document.sForm.submit();
}
//과제선택하기
function goProject()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	document.eForm.action='../servlet/projectManServlet';
	document.eForm.mode.value='PMA_L';
	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();
}
//등록하기
function memberWrite()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var worker_list = '<%=worker_list%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	document.eForm.action='../pjt/pm/projectMember_write.jsp';
	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.worker_list.value=worker_list;
	document.eForm.submit();
}
//수정할 내용 보기
function contentModify(pid)
{
	document.vForm.action='../servlet/projectManServlet';
	document.vForm.mode.value='PMA_V';
	document.vForm.pid.value=pid;
	document.vForm.submit();
}
//내용삭제하기
function contentDelete(pid)
{
	d = confirm("삭제하시겠습니까?");
	if(d == false) return;
	
	document.dForm.action='../servlet/projectManServlet';
	document.dForm.mode.value="PMA_D";
	document.dForm.pid.value=pid;
	document.dForm.submit();
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
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> 과제인력정보</TD>
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
				<TD align=left width='430'>
					<form name="sForm" method="post" style="margin:0">
					<select name="pjtWord" style=font-size:9pt;color="#4EA865"; onChange='javascript:goSearch();'>  
					<%
						String[] s_code = {"","S","0","1","2","3","4"};
						String[] s_name = {"전체과제","멤버미등록","진행전과제","진행중과제","완료과제","DROP과제","HOLD과제"};
						String tsel = "";
						for(int ti=0; ti<s_code.length; ti++) {
							if(pjtWord.equals(s_code[ti])) tsel = "selected";
							else tsel = "";
							out.println("<option "+tsel+" value='"+s_code[ti]+"'>"+s_name[ti]+"</option>");
						}
					%>
					</select>
					<select name="sItem" style=font-size:9pt;color="#4EA865";>  
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
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pid" size="15" value="">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a>  <a href='javascript:memberWrite()'><img src="../pjt/images/bt_add_new2.gif" border='0' align='absmiddle'></a>
					</form>
				</TD>
				<TD width='' align='right' style="padding-right:10px">
					<form name="eForm" method="post" style="margin:0">과 제 명
					<%
						out.println("<select name='project' onChange='javascript:goProject()' style=font-size:9pt;color='#4EA865';>");
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
					<input type="hidden" name="pid" size="15" value="">
					<input type="hidden" name="worker_list" size="15" value="">
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
				<TD noWrap width=60 align=middle class='list_title'>사 번</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=60 align=middle class='list_title'>이 름</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>직 책</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=70 align=middle class='list_title'>시작일</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=70 align=middle class='list_title'>완료일</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=40 align=middle class='list_title'>투입율</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>직 급</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>담당업무</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>개별편집</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=22></TD></TR>
		<% 
		int n = 1;
		String grade = "";
		for(int i=0; i<man_cnt; i++) {
			if(member[i][3].equals("A")) grade = "과제 PM";
			else if(member[i][3].equals("B")) grade = "상기 PL";
			else if(member[i][3].equals("C")) grade = "개발 PL";
			else if(member[i][3].equals("D")) grade = "SUB PL";
			else if(member[i][3].equals("E")) grade = "전담개발";
			else if(member[i][3].equals("F")) grade = "지원개발";
			else if(member[i][3].equals("G")) grade = "외부인력";

		%>
			<form name="vForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=n%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'><%=member[i][7]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=member[i][8]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=grade%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=member[i][4]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=member[i][5]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=member[i][6]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=member[i][11]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=member[i][9]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=member[i][13]%>  <%=member[i][14]%></TD>
			</TR>
			<TR><TD colSpan=22 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			n++;
			}  //while 

		%>
			<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
			<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
			<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
			<input type="hidden" name="mode" size="15" value="">
			<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
			<input type="hidden" name="pjt_name" size="15" value="">
			<input type="hidden" name="pid" size="15" value="">
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

<% // 삭제 form %>
<form name="dForm" method="post" style="margin:0">
	<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
	<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
	<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
	<input type="hidden" name="mode" size="15" value="">
	<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
	<input type="hidden" name="pjt_name" size="15" value="">
	<input type="hidden" name="pid" size="15" value="">
</form>

</BODY>
</HTML>

