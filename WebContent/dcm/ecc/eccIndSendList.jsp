<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "���� �߽��� LIST"		
	contentType = "text/html; charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();						//����
	com.anbtech.text.StringProcess str = new StringProcess();			//���ڿ�ó��
	com.anbtech.dcm.entity.eccComTable table;
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "pid";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";

	//--------------------------------------
	//������ ��ũ ���ڿ� ��������
	//--------------------------------------
	String view_pagecut="";
	int view_total=0,view_boardpage=0,view_totalpage=0;

	com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
	pageL = (eccComTable)request.getAttribute("PAGE_List");
	view_pagecut = pageL.getPageCut();
	view_total = pageL.getTotalArticle();
	view_boardpage = pageL.getCurrentPage();
	view_totalpage = pageL.getTotalPage();

	//--------------------------------------
	//����Ʈ ��������
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ECC_List");
	table = new eccComTable();
	Iterator table_iter = table_list.iterator();
	
%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//�޽��� �����ϱ�
var msg = '<%=msg%>';
if(msg.length != 0) { alert(msg); }

//�˻��ϱ�
function goSearch()
{
	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value='ecc_islist';
	document.sForm.page.value='1';
	document.sForm.submit();
}
//���뺸��
function eccView(pid)
{
	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value='ecc_view';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}
-->
</script>
<LINK href="../dcm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- Ÿ��Ʋ �� ������ ���� -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../bm/images/blet.gif" align="absmiddle"> ECR/ECO �߽���</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../dcm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../dcm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../dcm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width='80%' style="padding-left:10px">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"ecc_subject","eco_no","fg_code"};
						String[] snames = {"����","ECO NO","�߻���"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<a href='Javascript:goSearch();'><img src='../dcm/images/bt_search3.gif' border='0' align='absmiddle'></a>
					</TD>
				<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--����Ʈ-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=100 align=middle class='list_title'>���躯���ȣ</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>���躯������</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>�������</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
						<TD noWrap width=110 align=middle class='list_title'>�����߻���(F/G)</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>��������</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (eccComTable)table_iter.next();
		String fg_code = table.getFgCode();
		//fg_code = str.repWord(fg_code,"\n","<br>");			//���� ���
		int len = fg_code.indexOf("\n");
		if(len != -1) fg_code = fg_code.substring(0,len);		//��ǥFG�� ���
%>
					<TR height='24' onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=table.getEcoNo()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=table.getEccSubject()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getEccStatus()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=fg_code%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=anbdt.getSepDate(table.getEcrDate(),"-")%></td>
						<TD><IMG height=1 width=1></TD>
					</TR>
					<TR><TD colspan=19 background="../bm/images/dot_line.gif"></TD></TR>
<%		
	}
%>
				</TBODY></TABLE></TD></TR>

</TBODY></TABLE>

<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="page" size="15" value="">
<input type="hidden" name="pid" size="15" value="">
<input type="hidden" name="v_list" size="15" value="ecc_islist">
</FORM>

</BODY>
</HTML>
