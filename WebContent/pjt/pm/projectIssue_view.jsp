<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "과제 이슈 해결내용 보기[PM용]"		
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
	String[][] member = new String[man_cnt][5];
	
	int n=0;
	while(man_iter.hasNext()) {
		man = (projectTable)man_iter.next();
		member[n][0] = man.getPjtMbrId();		
		member[n][1] = man.getPjtMbrName();		
		member[n][2] = man.getPjtMbrGrade();			
		member[n][3] = man.getPjtMbrDiv();	
		member[n][4] = man.getPjtName();
		n++;
	}
	String pjt_name = "";
	if(man_cnt != 0) pjt_name = member[0][4];

	//-----------------------------------
	//	등록된 이슈 내용읽기
	//-----------------------------------
	String issue="",solution="",pid="",book_date="",users="",content="";
	String in_date="",sol_date="",node_code="",pjt_code="";
	com.anbtech.pjt.entity.projectTable issues;
	ArrayList issues_list = new ArrayList();
	issues_list = (ArrayList)request.getAttribute("ISSUE_List");
	issues = new projectTable();
	Iterator issues_iter = issues_list.iterator();
	
	if(issues_iter.hasNext()) {
		issues = (projectTable)issues_iter.next();

		pid = issues.getPid();
		users = issues.getUsers();
		pjt_code = issues.getPjtCode();
		node_code = issues.getNodeCode();
		issue = issues.getIssue();
		solution = issues.getSolution();
		content = issues.getContent();
		book_date = issues.getBookDate();
		in_date = issues.getInDate();
		sol_date = issues.getSolDate();
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--

-->
</script>
<BODY topmargin="0" leftmargin="0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> 이슈 해결내용 보기</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width=500>과제명 : <%=pjt_code%> <%=pjt_name%>
						노드명 : 
					<%
						for(int ti=0; ti<node_cnt; ti++) {
							if(node_code.equals(process[ti][0])) {
								out.println(process[ti][0]+" "+process[ti][1]);
							}
						}
					%></TD>
					<TD align=left width='10%'>
					<a href="javascript:self.close();"><img src="../pjt/images/bt_close.gif" border="0" align="absmiddle"></a>
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
			   <td width="90%" height="25" class="bg_04" colspan=3><%=users%></td>
			</tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">입력일</td>
			   <td width="40%" height="25" class="bg_04"><%=in_date%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">해결일</td>
			   <td width="40%" height="25" class="bg_04"><%=sol_date%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">이슈</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='issue' rows='3' cols='80' readonly><%=issue%></textarea></td>
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

</body>
</html>
