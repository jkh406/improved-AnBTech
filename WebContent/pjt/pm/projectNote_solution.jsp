<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제 문제점해결내용 입력하기[PM용]"		
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

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjt_code="",pjt_name="",node_code="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjt_name = para.getPjtName();
		node_code = para.getNodeCode();
	}
	String RD = request.getParameter("RD"); if(RD == null) RD="";

	//-----------------------------------
	//	과제 전체노드 찾기
	//-----------------------------------
	com.anbtech.pjt.entity.projectTable node;
	ArrayList node_list = new ArrayList();
	node_list = (ArrayList)request.getAttribute("NODE_List");
	node = new projectTable();
	Iterator node_iter = node_list.iterator();
	int node_cnt = node_list.size();
	String[][] process = new String[node_cnt][4];
	
	int a=0;
	while(node_iter.hasNext()) {
		node = (projectTable)node_iter.next();
		process[a][0] = node.getChildNode();		//노드코드
		process[a][1] = node.getNodeName();			//노드명
		process[a][2] = node.getUserId();			//담당자사번
		process[a][3] = node.getUserName();			//담당자이름
		a++;
	}

	//-----------------------------------
	//	개발 멤버 찾기
	//-----------------------------------
	com.anbtech.pjt.entity.projectTable man;
	ArrayList man_list = new ArrayList();
	man_list = (ArrayList)request.getAttribute("MAN_List");
	man = new projectTable();
	Iterator man_iter = man_list.iterator();
	int man_cnt = man_list.size();
	String[][] member = new String[man_cnt][4];
	
	int n=0;
	while(man_iter.hasNext()) {
		man = (projectTable)man_iter.next();
		member[n][0] = man.getPjtMbrId();		
		member[n][1] = man.getPjtMbrName();		
		member[n][2] = man.getPjtMbrGrade();			
		member[n][3] = man.getPjtMbrDiv();		
		n++;
	}

	//-----------------------------------
	//	등록된 문제점 내용읽기
	//-----------------------------------
	String note="",solution="",pid="",book_date="",users="",content="";
	com.anbtech.pjt.entity.projectTable notes;
	ArrayList notes_list = new ArrayList();
	notes_list = (ArrayList)request.getAttribute("NOTE_List");
	notes = new projectTable();
	Iterator notes_iter = notes_list.iterator();
	
	if(notes_iter.hasNext()) {
		notes = (projectTable)notes_iter.next();

		pid = notes.getPid();
		users = notes.getUsers();
		node_code = notes.getNodeCode();
		note = notes.getNote();
		solution = notes.getSolution();
		content = notes.getContent();
		book_date = notes.getBookDate();	book_date = book_date.replace('-','/');
	}

	//금일 날자 구하기 : format : yyyy-MM-dd
	String toDate = anbdt.getDate(0);
	toDate = toDate.replace('/','-');
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//등록확인 메시지
var RD = '<%=RD%>';
if(RD == 'R') {
	alert('수정되었습니다.');
}

//해결내용 입력하기
function noteContent()
{
	var content = document.sForm.content.value;
	if(content.length == 0) { alert('해결내용을 입력하십시요.'); return; }

	var r = confirm('입력하시겠습니까?');
	if(r == false) return;

	document.sForm.action='../servlet/projectNoteServlet';
	document.sForm.mode.value='PNT_CW';
	document.sForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="sForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> 과제문제점 해결내용 입력</TD>
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
					<TD align=left width='100%'>과제명 : <%=pjt_code%> <%=pjt_name%> 
					&nbsp;&nbsp;노드명 : 
					<%
						for(int ti=0; ti<node_cnt; ti++) {
							if(node_code.equals(process[ti][0])) {
								out.println(process[ti][0]+" "+process[ti][1]);
							}
						}
					%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:noteContent();"><img src="../pjt/images/bt_add.gif" border="0" align="absmiddle"></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">담당자</td>
			   <td width="40%" height="25" class="bg_04"><%=users%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">해결일</td>
			   <td width="40%" height="25" class="bg_04"><%=toDate%>
					<input type='hidden' name='sol_date' value='<%=toDate%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">문제점</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='note' rows='3' cols='80' readonly><%=note%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">대 책</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='solution' rows='3' cols='80' readonly><%=solution%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">해결내용</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='content' rows='5' cols='80'><%=content%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
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
<input type='hidden' name='note_status' value='1'>
</form>

</body>
</html>
