<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������� �����⺻���� LIST"		
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
	//	�����⺻���� ���� & ��ü ���� �ľ��ϱ�
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_List");
	table = new projectTable();
	Iterator table_iter = table_list.iterator();
	
	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//������ �̵��ϱ�
function goPage(a) 
{
	document.sForm.action='../servlet/projectGenServlet';
	document.sForm.mode.value='PBS_LA';
	document.sForm.page.value=a;
	document.sForm.submit();
}

//�˻��ϱ�
function goSearch()
{
	document.sForm.action='../servlet/projectGenServlet';
	document.sForm.mode.value='PBS_LA';
	document.sForm.page.value='1';
	document.sForm.submit();
}

//����ϱ�
function write()
{
	document.sForm.action='../pjt/pm/projectGenAll_write.jsp';
	document.sForm.submit();
}

//��������ϱ�
function contentModify(pid)
{
	document.sForm.action='../servlet/projectGenServlet';
	document.sForm.mode.value='PBS_VA';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}

//��������ϱ�
function contentDelete(pjt_code)
{
	d = confirm("�����Ͻðڽ��ϱ�?");
	if(d == false) return;
	document.dForm.action='../servlet/projectGenServlet';
	document.dForm.mode.value="PBS_DA";
	document.dForm.pjt_code.value=pjt_code;
	document.dForm.submit();
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
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> ������� �����⺻����</TD>
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
			<TR>
				<TD width=4>&nbsp;</TD>
				<TD align=left width='500'>
					<form name="sForm" method="post" style="margin:0">
					<select name="pjtWord" style=font-size:9pt;color="#4EA865"; onChange='javascript:goSearch();'>  
					<%
						String[] s_code = {"","S","0","1","2","3","4"};
						String[] s_name = {"��ü����","����������","����߰���","�����߰���","�Ϸ����","DROP����","HOLD����"};
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
						String[] sitems = {"pjt_name","pjt_code","pjt_mbr_id"};
						String[] snames = {"�����̸�","�����ڵ�","PM��"};
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
					<input type="hidden" name="mode" size="15" value="PBS_LA">
					<input type="hidden" name="pid" size="15" value="">
					<input type="hidden" name="page" size="15" value="">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a>  <a href='javascript:write()'><img src="../pjt/images/bt_add_new2.gif" border='0' align='absmiddle'></a>
					</form>
				</TD>
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
				<TD noWrap width=100 align=middle class='list_title'>�����ڵ�</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=380 align=middle class='list_title'>�����̸�</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>PM��</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>�������</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>��������</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
		<% if (table_list.size() == 0) { %>
			<TR vAlign=middle height=25>
				<TD colspan='11' align="middle">***** ������ �����ϴ�. ****</TD>
			</TD> 
		<% } %>	

		<% 
			int n = 1;
			String status = "";
			while(table_iter.hasNext()) {
				table = (projectTable)table_iter.next();
				status = table.getPjtStatus();
				if(status.equals("S")) status = "������";
				else if(status.equals("0")) status = "�����";
				else if(status.equals("1")) status = "������";
				else if(status.equals("2")) status = "�Ϸ�";
				else if(status.equals("3")) status = "DROP";
				else if(status.equals("4")) status = "HOLD";
		%>
			<form name="aForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=table.getPjtCode()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=table.getPjtName()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=table.getPjtMbrId()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=status%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=table.getModify()%>&nbsp;&nbsp;<%=table.getDelete()%></TD>
			</TR>
			<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			n++;
			}  //while 

		%>
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

<% // ���� form %>
<form name="dForm" method="post" style="margin:0">
<input type="hidden" name="mode" value=''>
<input type="hidden" name="pid" value=''>
<input type="hidden" name="pjt_code" value=''>
<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
</form>

</BODY>
</HTML>

