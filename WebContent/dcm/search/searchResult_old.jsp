<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "검색결과 LIST"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dcm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();						//일자
	com.anbtech.text.StringProcess str = new StringProcess();			//문자열처리
	com.anbtech.dcm.entity.eccComTable table;
	
	//--------------------------------------
	//페이지 링크 문자열 가져오기
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
	//리스트 가져오기
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
//내용보기
function eccView(pid)
{
	wopen("../servlet/CbomHistoryServlet?mode=sch_view&pid="+pid,"eco_view","800","670");
}
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=yes,toolbar=no,status=no,resizable=no');
}
-->
</script>
<LINK href="../dcm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form name="sForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../bm/images/blet.gif" align="absmiddle"> 검색결과</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../dms/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../dms/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../dms/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
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
						<TD noWrap width=90 align=middle class='list_title'>설계변경번호</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>설계변경제목</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>진행상태</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>발생모델(FG)</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>발의자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>발의일자</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (eccComTable)table_iter.next();
		String fg_code = table.getFgCode();
		//fg_code = str.repWord(fg_code,"\n","<br>");			//전부 출력
		int len = fg_code.indexOf("\n");
		if(len != -1) fg_code = fg_code.substring(0,len);		//대표FG만 출력
%>
					<TR height=24 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'><%=table.getEcoNo()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=table.getEccSubject()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getEccStatus()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=fg_code%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getEcrName()%></td>
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
</form>

</body>
</html>
