<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "해당과제 문제점 LIST"		
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

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjt_code="",pjt_name="",note_status="",pjtWord="1",sItem="note",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjt_name = para.getPjtName();
		note_status = para.getNoteStatus();
		pjtWord = para.getPjtword();
		sItem = para.getSitem();
		sWord = para.getSword();
	}
	String tpage = request.getParameter("Tpage"); if(tpage.equals("0")) tpage = "1";
	String cpage = request.getParameter("Cpage"); 
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	//-----------------------------------
	//	해당PM의 과제 전체 LIST
	//-----------------------------------
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

	//-----------------------------------
	//	해당과제의 문제점 LIST
	//-----------------------------------
	com.anbtech.pjt.entity.projectTable note;
	ArrayList note_list = new ArrayList();
	note_list = (ArrayList)request.getAttribute("NOTE_List");
	note = new projectTable();
	Iterator note_iter = note_list.iterator();
	int note_cnt = note_list.size();

	String[][] pjt_note = new String[note_cnt][15];
	int n = 0;
	while(note_iter.hasNext()) {
		note = (projectTable)note_iter.next();
		pjt_note[n][0] = note.getPid();
		pjt_note[n][1] = note.getPjtCode();
		pjt_note[n][2] = note.getPjtName();
		pjt_note[n][3] = note.getNodeCode();
		pjt_note[n][4] = note.getUsers();
		pjt_note[n][5] = note.getInDate();
		pjt_note[n][6] = note.getBookDate();
		pjt_note[n][7] = note.getNote();
		pjt_note[n][8] = note.getSolution();
		pjt_note[n][9] = note.getContent();
		pjt_note[n][10] = note.getSolDate();
		pjt_note[n][11] = note.getNoteStatus();
		pjt_note[n][12] = note.getView();
		pjt_note[n][13] = note.getModify();
		pjt_note[n][14] = note.getDelete();
		n++;
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//페이지 이동하기
function goPage(a) 
{
	document.sForm.action='../servlet/projectNoteServlet';
	document.sForm.mode.value='PNT_L';
	document.sForm.page.value=a;
	document.sForm.submit();
}
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/projectNoteServlet';
	document.sForm.mode.value='PNT_L';
	document.sForm.submit();
}
//과제선택하기
function goProject()
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	document.eForm.action='../servlet/projectNoteServlet';
	document.eForm.mode.value='PNT_L';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();

}
//문제점 작성하기 준비
function noteWrite()
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var pjtWord = '<%=pjtWord%>';
	if(pjtWord != '1') {alert('진행중인 과제만 등록이 가능합니다.'); return; }

	document.eForm.action='../pjt/pm/projectNoteFrame.jsp';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.mode.value='PNT_NV';
	document.eForm.pid.value='';
	document.eForm.submit();

}
//선택한 문제점 수정하기
function noteModify(pid)
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var pjtWord = '<%=pjtWord%>';
	if(pjtWord != '1') {alert('진행중인 과제만 등록이 가능합니다.'); return; }

	document.eForm.action='../pjt/pm/projectNoteFrame.jsp';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.mode.value='PNT_MV';
	document.eForm.pid.value=pid;
	document.eForm.submit();
}
//선택한 문제점 삭제하기
function noteDelete(pid)
{
	var r = confirm('삭제하시겠습니까?');
	if(r == false) return;

	document.vForm.action='../servlet/projectNoteServlet';
	document.vForm.mode.value='PNT_D';
	document.vForm.pid.value=pid;
	document.vForm.submit();

}
//선택한 문제점 해결내용 작성 준비
function noteSolution(pid)
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var pjtWord = '<%=pjtWord%>';
	if(pjtWord != '1') {alert('진행중인 과제만 등록이 가능합니다.'); return; }

	document.eForm.action='../pjt/pm/projectNoteFrame.jsp';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.mode.value='PNT_CV';
	document.eForm.pid.value=pid;
	document.eForm.submit();
}
//선택한 문제점 해결내용 수정 준비
function solModify(pid)
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var pjtWord = '<%=pjtWord%>';
	if(pjtWord != '1') {alert('진행중인 과제만 등록이 가능합니다.'); return; }

	document.eForm.action='../pjt/pm/projectNoteFrame.jsp';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.mode.value='PNT_CV';
	document.eForm.pid.value=pid;
	document.eForm.submit();
}
//선택한 문제점 해결내용 작성 취소하기 : 당일에 한해
function solRecovery(pid)
{
	var r = confirm('입력된 해결내용을 취소하시겠습니까?');
	if(r == false) return;

	document.vForm.action='../servlet/projectNoteServlet';
	document.vForm.mode.value='PNT_RW';
	document.vForm.pid.value=pid;
	document.vForm.submit();

}
//선택한 문제점 해결내용 보기
function noteContent(pid)
{
	sParam = "strSrc=../servlet/projectNoteServlet&pid="+pid+"&mode=PNT_SV";	showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:440px;resizable=0");
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
			<TBODY> <!--버튼 및 페이징-->
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD"> 
				<TD width='50%' valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> 문제점 관리정보</TD>
				<TD width='50%' style="padding-right:10px" align='right' valign='middle'><a href='Javascript:noteWrite();'><img src='../pjt/images/bt_add.gif' border='0' align='absmiddle'></a> <%=Cpage%>/<%=Tpage%> <img src="../pjt/images/setup_pages_nowpage.gif" border="0" align="absmiddle">
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
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--검색 메뉴 -->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD width=4>&nbsp;</TD>
				<TD align=left width='370'>
					<form name="sForm" method="post" style="margin:0">
					<select name="pjtWord" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
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
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"note","solution","content"};
						String[] snames = {"문제점","대책","해결내용"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a>
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pid" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
					<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="note_status" size="15" value="<%=note_status%>">
					<input type="hidden" name="page" size="15" value="">
					</form>
				</TD>
				<TD align=left width=''>
					<form name="eForm" method="post" style="margin:0">상태
					<select name="note_status" style=font-size:9pt;color="black"; onChange='javascript:goProject()'>  
					<%
						String[] pstatus = {"","0","1"};
						String[] stnames = {"전부","진행","완료"};
						String ts = "";
						for(int si=0; si<pstatus.length; si++) {
							if(note_status.equals(pstatus[si])) ts = "selected";
							else ts = "";
							out.println("<option "+ts+" value='"+pstatus[si]+"'>"+stnames[si]+"</option>");
						}
					%>
					</select>
					<%
						out.println("과제명");
						out.println("<select name='project' onChange='javascript:goProject()' style=font-size:9pt;color='black';>");
						String psel = "";
						for(int i=0; i<pjt_cnt; i++) {
							pjt_name = project[i][1];
							if(pjt_name.length() > 15) pjt_name = pjt_name.substring(0,15)+" ...";

							if(pjt_code.equals(project[i][0])) psel = "selected";
							else psel = "";
							out.print("<option "+psel+" value='"+project[i][0]+"|"+project[i][1]+"'>");
							out.println(project[i][0]+" "+pjt_name+"</option>");
						}
					%>
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
					<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pid" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="">
					<input type="hidden" name="pjt_name" size="15" value="">
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="note_status" size="15" value="<%=note_status%>">
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
				<TD noWrap width=100% align=middle class='list_title'>문제점 내용</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>담당자</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>작성일</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>예정일</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>해결일</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>상태</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=90 align=middle class='list_title'>내용수정</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=18></TD></TR>
		<% 
		String status="",isd="",ssd="";		//문제점,상태,작성일,해결일
		int m = 1;
		for(int i=0; i<note_cnt; i++) {
			status=pjt_note[i][11];
			if(status.equals("0")) status="진행중";
			else if(status.equals("1")) status="완 료";
		%>	
			<form name="vForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=m%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'><%=pjt_note[i][7]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_note[i][4]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_note[i][5]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_note[i][6]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_note[i][10]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=status%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_note[i][12]%> <%=pjt_note[i][13]%> <%=pjt_note[i][14]%></TD>
			</TR>
			<TR><TD colSpan=18 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			m++;
			}  //while 

		%>
			<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
			<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
			<input type="hidden" name="mode" size="15" value="">
			<input type="hidden" name="pid" size="15" value="">
			<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
			<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
			<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
			<input type="hidden" name="note_status" size="15" value="<%=note_status%>">
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

</BODY>
</HTML>

