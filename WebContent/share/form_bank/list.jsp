<%@ include file= "../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage="../../admin/errorpage.jsp"
%>
<%@ page import="java.util.*,com.anbtech.share.entity.*,com.anbtech.text.Hanguel"%>
<%!
	com.anbtech.share.entity.ShareBdTable table;
	com.anbtech.share.entity.ShareLinkTable redirect;
%>
<%
	com.anbtech.share.entity.ShareParameterTable sbpara = new com.anbtech.share.entity.ShareParameterTable();
	sbpara  = (ShareParameterTable)request.getAttribute("sbParameter");
	String category		= sbpara.getCategory();
	String tablename	= sbpara.getTableName();
	String mode			= sbpara.getMode();
	String categorycombo= sbpara.getCategoryCombo();
	String searchscope  = sbpara.getSearchScope();
	
	//loan_list ����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Arry");
	table = new ShareBdTable();
	Iterator table_iter = table_list.iterator();

	//��ũ ���ڿ� ��������
	redirect = new com.anbtech.share.entity.ShareLinkTable();
	redirect = (ShareLinkTable)request.getAttribute("Redirect");
	String view_pagecut = redirect.getViewPagecut();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../share/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
			<TD vAlign=top>
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
					<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD">
						<TD valign='middle' class="title"><img src="../share/images/blet.gif" align="absmiddle"> ��Ĳٷ���</TD>
						<TD style="padding-right:10px" align='right' valign='middle'><img src="../share/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../share/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../share/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
<TR height=32><!--��ư �� ����¡-->
	<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR><TD width=4></TD>
				<TD align=left width='500'>
				<form method=get action='../servlet/ShareBdServlet' name=srForm 
								onSubmit="if( !document.srForm.searchscope.value=='' && document.srForm.searchword.value=='') {
												alert('�˻�� ������ �ֽʽÿ�');
												return false;
											} else if(document.srForm.searchscope.value=='' && !document.srForm.searchword.value==''){
												alert('�˻� �׸��� ������ �ֽʽÿ�');
												return false;
											} else return true" style="margin:0">
				
					<%=categorycombo%>
					<%if(!category.equals("")){%>
						<script language='javascript'>
							document.srForm.category.value = '<%=category%>'
						</script>
					<%}%>
					<select name=searchscope>
						<option value='' >�׸���</option>
						<option value='subject' >��Ը�</option>
						<option value='wname' >������̸�</option>
						<option value='doc_no' >������ȣ</option>
						<option value='ac_name' >�ְ��μ�</option>
					</select>
					<%if(!searchscope.equals("")){%>
						<script language='javascript'>
							document.srForm.searchscope.value = '<%=searchscope%>'
						</script>
					<%}%>
						<input type=text name=searchword size='10'>
						<input type="image" onfocus=blur() src="../share/images/bt_search3.gif" border="0" align="absmiddle">
						
						<%	if(sbpara.getBool()){	%>	
						<a href='javascript:go_input()'>	
						<IMG src='../share/images/bt_reg.gif' border="0" align="absmiddle"></a>
						<%}%>
						<INPUT TYPE='hidden' NAME='tablename' VALUE='<%=tablename%>'>
						<INPUT TYPE='hidden' NAME='mode' VALUE='<%=mode%>'>
						<INPUT TYPE='hidden' NAME='page' VALUE='<%=view_boardpage%>'>
					</form></TD>
			<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<TR height=100%><!--����Ʈ-->
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=23>
				<TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../share/images/list_tep2.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>��Ĺ�ȣ</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../share/images/list_tep2.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>��Ը�</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../share/images/list_tep2.gif"></TD>
				<TD noWrap width=60 align=middle class='list_title'>����</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../share/images/list_tep2.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>�ְ��μ�</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../share/images/list_tep2.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>�������</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../share/images/list_tep2.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>÷��ȭ��</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../share/images/list_tep2.gif"></TD>
				<TD noWrap width=40 align=middle class='list_title'>��ȸ��</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>

<%
	int number	= 0;
	number		= Integer.parseInt(view_total) - 15*(Integer.parseInt(view_boardpage)-1);
	//******************************
	//������ table������ �������� �� loop  **
	//******************************
	while(table_iter.hasNext()){
	table = (ShareBdTable)table_iter.next();
	
	 String	fname	= table.getFname();		//	ȭ���̸�	
	 String  flink	= table.getFlink()==null?"":table.getFlink();
	 String subject_link = table.getSubjectLink();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=number--%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=table.getDocNo()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=subject_link%><%=table.getSubject()%></a></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getVer()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getAcName()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getWdate()%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=flink%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getCnt()%></td></TR>
			<TR><TD colSpan=15 background="../share/images/dot_line.gif"></TD></TR>
<%	}
%>		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>

<SCRIPT language='javascript'>
	function go_input(){
		location.href="../servlet/ShareBdServlet?tablename=<%=tablename%>&mode=write&page=<%=view_boardpage%>";
	}
</SCRIPT>