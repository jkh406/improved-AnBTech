<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�ڽ��� �������� ������ü List"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.pjt.entity.projectTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();

	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String pjtWord="S",sItem="",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjtWord = para.getPjtword();	
		sItem = para.getSitem();		
		sWord = para.getSword();		
	} 
	
	//-----------------------------------
	//	�������� ���� LIST
	//-----------------------------------
	String status = "",pm_name="";
	String psd="",ped="",csd="",ced="",rsd="",red="";

	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PJT_List");
	table = new projectTable();
	Iterator table_iter = table_list.iterator();
	int sch_cnt = table_list.size();

	String[][] schedule = new String[sch_cnt][10];
	int m = 0;
	while(table_iter.hasNext()) {
		table = (projectTable)table_iter.next();
		schedule[m][0] = table.getPid();
		schedule[m][1] = table.getPjtCode();
		schedule[m][2] = table.getPjtName();

		psd = table.getPlanStartDate();	if(psd == null) psd = "";
		if(psd.length() != 0)schedule[m][3] = psd.substring(2,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		else schedule[m][3] = "";
		ped = table.getPlanEndDate();	if(ped == null) ped = "";
		if(ped.length() != 0)schedule[m][4] = ped.substring(2,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);
		else schedule[m][4] = "";

		csd = table.getChgStartDate();	if(csd == null) csd = "";
		if(csd.length() != 0)schedule[m][5] = csd.substring(2,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		else schedule[m][5] = "";
		ced = table.getChgEndDate();	if(ced == null) ced = "";
		if(ced.length() != 0)schedule[m][6] = ced.substring(2,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		else schedule[m][6] = "";

		rsd = table.getRstStartDate();	if(rsd == null) rsd = "";
		if(rsd.length() != 0)schedule[m][7] = rsd.substring(2,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		else schedule[m][7] = "";
		red = table.getRstEndDate();	if(red == null) red = "";
		if(red.length() != 0)schedule[m][8] = red.substring(2,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);
		else schedule[m][8] = "";

		pm_name = table.getPjtMbrId();
		schedule[m][9] = pm_name.substring(pm_name.indexOf("/")+1,pm_name.length());
		m++;
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
	document.sForm.action='../servlet/projectStaffServlet';
	document.sForm.mode.value='PSM_PL';
	document.sForm.page.value=a;
	document.sForm.submit();
}

//�������� �����������ϱ�
function pjtView(pjt_code)
{
	document.aForm.action='../servlet/projectStaffServlet';
	document.aForm.mode.value='PSM_PV';
	document.aForm.pjt_code.value=pjt_code;
	document.aForm.submit();
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
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> �������� ����LIST</TD>
				<TD style="padding-right:10px" align='right' valign='middle'><%=Cpage%>/<%=Tpage%> <img src="../pjt/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--��ư �� ����¡-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR><form name="sForm" method="post" style="margin:0">
				<TD width=4>&nbsp;</TD>
				<TD width='' align='right' style="padding-right:10px">
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
			<input type="hidden" name="mode" size="15" value="">
			<input type="hidden" name="page" size="15" value="">
			</form>
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
				<TD noWrap width=80 align=middle class='list_title'>�����ڵ�</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>�����̸�</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>PM��</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>��ȹ��</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>������</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>������</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
		<% if (table_list.size() == 0) { %>
			<TR vAlign=middle height=25>
				<TD colspan='11' align="middle">***** ������ �����ϴ�. ****</TD>
			</TD> 
		<% } %>	

		<% 
			for(int i=0; i<sch_cnt; i++) {
		%>
			<form name="aForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=schedule[i][1]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'><%=schedule[i][2]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=schedule[i][9]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=schedule[i][3]%>~<%=schedule[i][4]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=schedule[i][5]%>~<%=schedule[i][6]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=schedule[i][7]%>~<%=schedule[i][8]%></TD>
			</TR>
			<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			}  //for

		%>
			<input type="hidden" name="mode" size="15" value="">
			<input type="hidden" name="pjt_code" size="15" value="">
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>


</BODY>
</HTML>

