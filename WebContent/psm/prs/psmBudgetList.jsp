<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "PSM �����̷����� LIST"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.psm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.psm.entity.psmBudgetTable table;

	//----------------------------------------------------
	//	PSM MASTER �Է�/���� ���� �б�
	//----------------------------------------------------
	com.anbtech.psm.entity.psmMasterTable psm;
	psm = (psmMasterTable)request.getAttribute("MASTER_table");
	String plan_sum = psm.getPlanSum();
	String result_sum = psm.getResultSum();
	String diff_sum = psm.getDiffSum();
	
	//--------------------------------------
	//������ ��ũ ���ڿ� ��������
	//--------------------------------------
	String view_pagecut="";
	int view_total=0,view_boardpage=0,view_totalpage=0;

	com.anbtech.psm.entity.psmBudgetTable pageL = new com.anbtech.psm.entity.psmBudgetTable();
	pageL = (psmBudgetTable)request.getAttribute("PAGE_List");
	view_pagecut = pageL.getPageCut();
	view_total = pageL.getTotalArticle();
	view_boardpage = pageL.getCurrentPage();
	view_totalpage = pageL.getTotalPage();

	//--------------------------------------
	//����Ʈ ��������
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("BUDGET_List");
	table = new psmBudgetTable();
	Iterator table_iter = table_list.iterator();
	
%>

<HTML><HEAD><TITLE>PSM �����̷����� LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../psm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- Ÿ��Ʋ �� ������ ���� -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../psm/images/blet.gif" align="absmiddle"> ���� �����̷�</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../psm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../psm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../psm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width='80%' style="padding-left:10px" class="bg_03">
					����: <%=plan_sum%> &nbsp;&nbsp;&nbsp;
					����: <%=result_sum%> &nbsp;&nbsp;&nbsp;
					�ܾ�: <%=diff_sum%></TD>
				<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--����Ʈ-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=90 align=middle class='list_title'>�����ڵ�</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>������(�ѱ�)</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>�����Ѿ�</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>�ΰǺ�</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>����</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>�� ��</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>�ü���</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>�������</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	String status = "";
	while(table_iter.hasNext()){
		table = (psmBudgetTable)table_iter.next();
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'><%=table.getPsmCode()%></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPsmKorea()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanSum()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanLabor()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanMaterial()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanCost()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPlanPlant()%></td>
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

<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="page" size="15" value="">
<input type="hidden" name="pid" size="15" value="">
<input type="hidden" name="psm_status" size="15" value="1">
<input type="hidden" name="psm_type" size="15" value="">
</form>

</body>
</html>
<script language=javascript>
<!--
//�����ϱ�
function psmView(pid)
{
	parent.reg.document.eForm.action='../servlet/PsmBudgetServlet';
	parent.reg.document.eForm.mode.value='bud_prewrite';
	parent.reg.document.eForm.pid.value=pid;
	parent.reg.document.eForm.submit();
}
-->
</script>