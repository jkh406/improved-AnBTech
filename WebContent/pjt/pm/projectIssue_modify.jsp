<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���� �̽��� ��å �����ϱ�[PM��]"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();

	//-----------------------------------
	//	�Ķ���� �ޱ�
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
	//	���� ��ü��� ã��
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
		process[a][0] = node.getChildNode();		//����ڵ�
		process[a][1] = node.getNodeName();			//����
		process[a][2] = node.getUserId();			//����ڻ��
		process[a][3] = node.getUserName();			//������̸�
		a++;
	}

	//-----------------------------------
	//	���� ��� ã��
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
	//	��ϵ� �̽� �����б�
	//-----------------------------------
	String issue="",solution="",pid="",book_date="",users="";
	com.anbtech.pjt.entity.projectTable issues;
	ArrayList issues_list = new ArrayList();
	issues_list = (ArrayList)request.getAttribute("ISSUE_List");
	issues = new projectTable();
	Iterator issues_iter = issues_list.iterator();
	
	if(issues_iter.hasNext()) {
		issues = (projectTable)issues_iter.next();

		pid = issues.getPid();
		users = issues.getUsers();
		node_code = issues.getNodeCode();
		issue = issues.getIssue();
		solution = issues.getSolution();
		book_date = issues.getBookDate(); book_date = book_date.replace('-','/');
	}

	//���� ���� ���ϱ� : format : yyyy-MM-dd
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
//���Ȯ�� �޽���
var RD = '<%=RD%>';
if(RD == 'R') {
	alert('�����Ǿ����ϴ�.');
}
//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pjt/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}
//�����ϱ�
function issueModify()
{
	var issue = document.sForm.issue.value;
	if(issue.length == 0) { alert('�̽��� �Է��Ͻʽÿ�.'); return; }

	var book_date = document.sForm.book_date.value;
	book_date = book_date.replace('/','-');
	book_date = book_date.replace('/','-');
	if(book_date.length == 0) { alert('�ذΌ������ �Է��Ͻʽÿ�.'); return; }

	var r = confirm('�����Ͻðڽ��ϱ�? ����� ����ڸ� Ȯ�ιٶ��ϴ�.');
	if(r == false) return;

	document.sForm.action='../servlet/projectIssueServlet';
	document.sForm.mode.value='PIS_MW';
	document.sForm.book_date.value=book_date;
	document.sForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="sForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> �ش���� �̽� ����</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width='100%'>������ : <%=pjt_code%> <%=pjt_name%> 
					&nbsp;&nbsp;���� : 
					<select name="node_code" style=font-size:9pt;color="black";>  
					<%
						String nsel = "";
						for(int ti=0; ti<node_cnt; ti++) {
							if(node_code.equals(process[ti][0])) nsel = "selected";
							else nsel = "";
							out.print("<option "+nsel+" value='"+process[ti][0]+"'>");
							out.println(process[ti][0]+" "+process[ti][1]+"</option>");
						}
					%>
					</select>
					<a href="javascript:issueModify();"><img src="../pjt/images/bt_modify.gif" border="0" align="absmiddle"></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����</td>
			   <td width="40%" height="25" class="bg_04">
				<select name="users" style=font-size:9pt;color="black";> 
				<%
					String sel = "";
					for(int i=0; i<man_cnt; i++) {
						if(users.equals(member[i][1])) sel = "selected";
						else sel = "";
						out.print("<option "+sel+" value='"+member[i][1]+"'>"+member[i][1]+" "+member[i][2]);
						out.println("</option>");
					} 
				%>
				</select></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�ۼ���</td>
			   <td width="40%" height="25" class="bg_04"><%=toDate%>
					<input type='hidden' name='in_date' value='<%=toDate%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�̽�</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='issue' rows='5' cols='80'><%=issue%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� å</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='solution' rows='5' cols='80'><%=solution%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�ذΌ����</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<input type='text' name='book_date' value='<%=book_date%>' readonly>
					<A Href="Javascript:OpenCalendar('book_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A></td>
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
<input type='hidden' name='issue_status' value='0'>
</form>

</body>
</html>
