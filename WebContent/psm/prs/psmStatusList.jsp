<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "PSM 상태이력정보 LIST"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.psm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.psm.entity.psmStatusTable table;
	
	//--------------------------------------
	//페이지 링크 문자열 가져오기
	//--------------------------------------
	String view_pagecut="";
	int view_total=0,view_boardpage=0,view_totalpage=0;

	com.anbtech.psm.entity.psmStatusTable pageL = new com.anbtech.psm.entity.psmStatusTable();
	pageL = (psmStatusTable)request.getAttribute("PAGE_List");
	view_pagecut = pageL.getPageCut();
	view_total = pageL.getTotalArticle();
	view_boardpage = pageL.getCurrentPage();
	view_totalpage = pageL.getTotalPage();

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("STATUS_List");
	table = new psmStatusTable();
	Iterator table_iter = table_list.iterator();
	
%>

<HTML><HEAD><TITLE>PSM 상태이력정보 LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../psm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../psm/images/blet.gif" align="absmiddle"> 과제 진행상태이력</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../psm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../psm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../psm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width='80%' style="padding-left:10px"></TD>
				<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=90 align=middle class='list_title'>과제코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>과제명(한글)</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>과제종류</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=120 align=middle class='list_title'>과제고객</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>카테고리</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=170 align=middle class='list_title'>과제기간</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>과제상태</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>등록일자</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	String status = "";
	while(table_iter.hasNext()){
		table = (psmStatusTable)table_iter.next();
		status = table.getPsmStatus();
		if(status.equals("1")) status = "미진행";
		else if(status.equals("2")) status = "진행";
		else if(status.equals("3")) status = "재진행";
		else if(status.equals("4")) status = "완료";
		else if(status.equals("5")) status = "보류";
		else if(status.equals("6")) status = "취소";
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'><%=table.getPsmCode()%></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPsmKorea()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPsmType()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getCompName()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getCompCategory()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPsmStartDate()%> ~ <%=table.getPsmEndDate()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=status%></td>
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
//편집하기
function psmView(pid)
{
	parent.reg.document.eForm.action='../servlet/PsmStatusServlet';
	parent.reg.document.eForm.mode.value='sts_prewrite';
	parent.reg.document.eForm.pid.value=pid;
	parent.reg.document.eForm.submit();
}
-->
</script>