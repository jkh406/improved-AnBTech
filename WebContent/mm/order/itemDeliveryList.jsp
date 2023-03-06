<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "부품출고의뢰현황 LIST"		
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
	com.anbtech.mm.entity.mfgReqMasterTable table;
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//포멧
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String msg = "";
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "fg_code";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String factory_no = (String)request.getAttribute("factory_no"); if(factory_no == null) factory_no = "";
	if(factory_no.length() == 0) msg ="해당공장번호를 선택하십시오.";

	//--------------------------------------
	//페이지 링크 문자열 가져오기
	//--------------------------------------
	String view_pagecut="";
	int view_total=0,view_boardpage=0,view_totalpage=0;

	com.anbtech.mm.entity.mfgReqMasterTable pageL = new com.anbtech.mm.entity.mfgReqMasterTable();
	pageL = (mfgReqMasterTable)request.getAttribute("PAGE_List");
	view_pagecut = pageL.getPageCut();
	view_total = pageL.getTotalArticle();
	view_boardpage = pageL.getCurrentPage();
	view_totalpage = pageL.getTotalPage();

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("REQ_MASTER_List");
	table = new mfgReqMasterTable();
	Iterator table_iter = table_list.iterator();
	
%>

<HTML><HEAD><TITLE>부품출고의뢰현황 LIST</TITLE>
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
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif" align="absmiddle"> 부품출고의뢰현황</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../mm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../mm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../mm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width='80%' style="padding-left:10px">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"mfg_no","mfg_req_no","assy_code"};
						String[] snames = {"생산지시번호","출고의뢰번호","제품코드"};
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
						<TD noWrap width=90 align=middle class='list_title'>출고의뢰번호</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>생산제품코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>생산제품설명</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>등록일자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>생산공장번호</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>진행상태</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
		while(table_iter.hasNext()){
			table = (mfgReqMasterTable)table_iter.next();
			String status = table.getReqStatus();
			if(status.equals("1")) status = "출고작성";
			else if(status.equals("2")) status = "출고요청";
			else if(status.equals("3")) status = "자재출고";
		
%>
					<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg' height=23><%=table.getMfgNo()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getMfgReqNo()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getAssyCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'> <%=table.getAssySpec()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=anbdt.getSepDate(table.getReqDate(),"-")%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getFactoryNo()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=status%></td>
						<TD><IMG height=1 width=1></TD>
					</TR>
					<TR><TD colspan=19 background="../mm/images/dot_line.gif"></TD></TR>
<%		}
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
//메시지 처리
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
	history.back(-1);
}
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='out_delivery';
	document.sForm.page.value='1';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//부품출고의뢰내용보기
function mfgReqView(delivery_no)
{
	var factory_no = document.sForm.factory_no.value;
	var para = "mfg_req_no="+delivery_no+"&factory_no="+factory_no;

	document.sForm.action='../mm/order/itemOutFrame.jsp?'+para;
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