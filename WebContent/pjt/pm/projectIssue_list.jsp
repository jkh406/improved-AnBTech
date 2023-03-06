<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�ش���� �̽� LIST"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();

	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String pjt_code="",pjt_name="",issue_status="",pjtWord="1",sItem="issue",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjt_name = para.getPjtName();
		issue_status = para.getNoteStatus();
		pjtWord = para.getPjtword();
		sItem = para.getSitem();
		sWord = para.getSword();
	}
	String tpage = request.getParameter("Tpage"); if(tpage.equals("0")) tpage = "1";
	String cpage = request.getParameter("Cpage"); 
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	//-----------------------------------
	//	�ش�PM�� ���� ��ü LIST
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
	//	�ش������ �̽� LIST
	//-----------------------------------
	com.anbtech.pjt.entity.projectTable issue;
	ArrayList issue_list = new ArrayList();
	issue_list = (ArrayList)request.getAttribute("ISSUE_List");
	issue = new projectTable();
	Iterator issue_iter = issue_list.iterator();
	int issue_cnt = issue_list.size();

	String[][] pjt_issue = new String[issue_cnt][15];
	int n = 0;
	while(issue_iter.hasNext()) {
		issue = (projectTable)issue_iter.next();
		pjt_issue[n][0] = issue.getPid();
		pjt_issue[n][1] = issue.getPjtCode();
		pjt_issue[n][2] = issue.getPjtName();
		pjt_issue[n][3] = issue.getNodeCode();
		pjt_issue[n][4] = issue.getUsers();
		pjt_issue[n][5] = issue.getInDate();
		pjt_issue[n][6] = issue.getBookDate();
		pjt_issue[n][7] = issue.getIssue();
		pjt_issue[n][8] = issue.getSolution();
		pjt_issue[n][9] = issue.getContent();
		pjt_issue[n][10] = issue.getSolDate();
		pjt_issue[n][11] = issue.getNoteStatus();
		pjt_issue[n][12] = issue.getView();
		pjt_issue[n][13] = issue.getModify();
		pjt_issue[n][14] = issue.getDelete();
		n++;
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//������ �̵��ϱ�
function goPage(a) 
{
	document.sForm.action='../servlet/projectIssueServlet';
	document.sForm.mode.value='PIS_L';
	document.sForm.page.value=a;
	document.sForm.submit();
}
//�˻��ϱ�
function goSearch()
{
	document.sForm.action='../servlet/projectIssueServlet';
	document.sForm.mode.value='PIS_L';
	document.sForm.submit();
}
//���������ϱ�
function goProject()
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	document.eForm.action='../servlet/projectIssueServlet';
	document.eForm.mode.value='PIS_L';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();

}
//�̽� �ۼ��ϱ� �غ�
function issueWrite()
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var pjtWord = '<%=pjtWord%>';
	if(pjtWord != '1') {alert('�������� ������ ����� �����մϴ�.'); return; }

	document.eForm.action='../pjt/pm/projectIssueFrame.jsp';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.mode.value='PIS_NV';
	document.eForm.pid.value='';
	document.eForm.submit();

}
//������ �̽� �����ϱ�
function issueModify(pid)
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var pjtWord = '<%=pjtWord%>';
	if(pjtWord != '1') {alert('�������� ������ ����� �����մϴ�.'); return; }

	document.eForm.action='../pjt/pm/projectIssueFrame.jsp';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.mode.value='PIS_MV';
	document.eForm.pid.value=pid;
	document.eForm.submit();
}
//������ �̽� �����ϱ�
function issueDelete(pid)
{
	var r = confirm('�����Ͻðڽ��ϱ�?');
	if(r == false) return;

	document.vForm.action='../servlet/projectIssueServlet';
	document.vForm.mode.value='PIS_D';
	document.vForm.pid.value=pid;
	document.vForm.submit();

}
//������ �̽� �ذ᳻�� �ۼ� �غ�
function issueSolution(pid)
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var pjtWord = '<%=pjtWord%>';
	if(pjtWord != '1') {alert('�������� ������ ����� �����մϴ�.'); return; }

	document.eForm.action='../pjt/pm/projectIssueFrame.jsp';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.mode.value='PIS_CV';
	document.eForm.pid.value=pid;
	document.eForm.submit();
}
//������ �̽� �ذ᳻�� ���� �غ�
function solModify(pid)
{
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var pjtWord = '<%=pjtWord%>';
	if(pjtWord != '1') {alert('�������� ������ ����� �����մϴ�.'); return; }

	document.eForm.action='../pjt/pm/projectIssueFrame.jsp';
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.mode.value='PIS_CV';
	document.eForm.pid.value=pid;
	document.eForm.submit();
}
//������ �̽� �ذ᳻�� �ۼ� ����ϱ� : ���Ͽ� ����
function solRecovery(pid)
{
	var r = confirm('�Էµ� �ذ᳻���� ����Ͻðڽ��ϱ�?');
	if(r == false) return;

	document.vForm.action='../servlet/projectIssueServlet';
	document.vForm.mode.value='PIS_RW';
	document.vForm.pid.value=pid;
	document.vForm.submit();

}
//������ �̽� �ذ᳻�� ����
function issueContent(pid)
{
	sParam = "strSrc=../servlet/projectIssueServlet&pid="+pid+"&mode=PIS_SV";	showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:440px;resizable=0");
}
-->
</script>

<LINK href="../pjt/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY> <!--��ư �� ����¡-->
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD"> 
				<TD width='50%' valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> �̽� ��������</TD>
				<TD width='50%' style="padding-right:10px" align='right' valign='middle'><a href='Javascript:issueWrite();'><img src='../pjt/images/bt_add.gif' border='0' align='absmiddle'></a> <%=Cpage%>/<%=Tpage%> <img src="../pjt/images/setup_pages_nowpage.gif" border="0" align="absmiddle">
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
	<TR height=32><!--�˻� �޴� -->
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
						String[] s_name = {"��ü����","����̵��","����������","�����߰���","�Ϸ����","DROP����","HOLD����"};
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
						String[] sitems = {"issue","solution","content"};
						String[] snames = {"�̽�","��å","�ذ᳻��"};
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
					<input type="hidden" name="issue_status" size="15" value="<%=issue_status%>">
					<input type="hidden" name="page" size="15" value="">
					</form>
				</TD>
				<TD align=left width=''>
					<form name="eForm" method="post" style="margin:0">����
					<select name="issue_status" style=font-size:9pt;color="black"; onChange='javascript:goProject()'>  
					<%
						String[] pstatus = {"","0","1"};
						String[] stnames = {"����","����","�Ϸ�"};
						String ts = "";
						for(int si=0; si<pstatus.length; si++) {
							if(issue_status.equals(pstatus[si])) ts = "selected";
							else ts = "";
							out.println("<option "+ts+" value='"+pstatus[si]+"'>"+stnames[si]+"</option>");
						}
					%>
					</select>
					<%
						out.println("������");
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
					<input type="hidden" name="issue_status" size="15" value="<%=issue_status%>">
					</form>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<!--����Ʈ-->
	<TR height=100%>
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=25>
				<TD noWrap width=20 align=middle class='list_title'>NO</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>�̽� ����</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>�����</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>�ۼ���</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>������</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>�ذ���</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>����</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=90 align=middle class='list_title'>�������</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=18></TD></TR>
		<% 
		String status="",isd="",ssd="";		//�̽�,����,�ۼ���,�ذ���
		int m = 1;
		for(int i=0; i<issue_cnt; i++) {
			status=pjt_issue[i][11];
			if(status.equals("0")) status="������";
			else if(status.equals("1")) status="�� ��";
		%>	
			<form name="vForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=m%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'><%=pjt_issue[i][7]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_issue[i][4]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_issue[i][5]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_issue[i][6]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_issue[i][10]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=status%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=pjt_issue[i][12]%> <%=pjt_issue[i][13]%> <%=pjt_issue[i][14]%></TD>
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
			<input type="hidden" name="issue_status" size="15" value="<%=issue_status%>">
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

</BODY>
</HTML>

