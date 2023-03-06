<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "작업지시현황 LIST"		
	contentType = "text/html; charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.mm.entity.mfgOperatorTable table;
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//포멧
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "fg_code";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String factory_no = (String)request.getAttribute("factory_no"); if(factory_no == null) factory_no = "";

	//--------------------------------------
	//페이지 링크 문자열 가져오기
	//--------------------------------------
	String view_pagecut="";
	int view_total=0,view_boardpage=0,view_totalpage=0;

	com.anbtech.mm.entity.mfgOperatorTable pageL = new com.anbtech.mm.entity.mfgOperatorTable();
	pageL = (mfgOperatorTable)request.getAttribute("PAGE_List");
	view_pagecut = pageL.getPageCut();
	view_total = pageL.getTotalArticle();
	view_boardpage = pageL.getCurrentPage();
	view_totalpage = pageL.getTotalPage();

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("REQUEST_List");
	table = new mfgOperatorTable();
	Iterator table_iter = table_list.iterator();
	
%>

<HTML><HEAD><TITLE>생산지시현황 LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form name="sForm" method="post" style="margin:0" onsubmit="javascript:goSearch(); return false;">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif" align="absmiddle"> 생산대기현황</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../mm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../mm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../mm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width='80%' style="padding-left:10px">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"mfg_no","assy_code","mfg_count"};
						String[] snames = {"생산지시번호","제품코드","제조수량"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<INPUT type="text" name="sWord" size="15" value="<%=sWord%>">
					<a href='Javascript:goSearch();'><img src='../mm/images/bt_search3.gif' border='0' align='absmiddle'></a>
					</TD>
				<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=90 align=middle class='list_title'>생산지시번호</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>생산품목코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>생산품목설명</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>생산수량</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>예정시작일</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>예정종료일</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>지시일자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>진행상태</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (mfgOperatorTable)table_iter.next();
		String status = table.getOpOrder();
		if(status.equals("1")) status = "작업지시";
		else if(status.equals("2")) status = "자재마감";
		else if(status.equals("3")) status = "생산완료";
		
%>
					<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg' height=23><%=table.getMfgNo()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getAssyCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'> <%=table.getAssySpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=nfm.toDigits(table.getMfgCount())%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getOpStartDate()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'> <%=table.getOpEndDate()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getOrderDate()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=status%></td>
						<TD><IMG height=1 width=1></TD>
					</TR>
					<TR><TD colspan=19 background="../mm/images/dot_line.gif"></TD></TR>
<%		
	}
%>
				</TBODY></TABLE></TD></TR>

</TBODY></TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="page" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="">
<INPUT type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
</FORM>
</BODY>
</HTML>

<script language=javascript>
<!--
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='order_receive';
	document.sForm.page.value='1';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//작업지시내용보기
function mfgView(pid)
{
	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='order_view';
	document.sForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>