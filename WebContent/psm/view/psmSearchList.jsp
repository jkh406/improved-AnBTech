<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "PSM 전체현황 LIST"		
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
	com.anbtech.psm.entity.psmMasterTable table;
	com.anbtech.psm.entity.psmEnvTable color;
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "pid";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String psm_start_date = (String)request.getAttribute("psm_start_date"); 
	if(psm_start_date == null) psm_start_date = "";
	String first_year = (String)request.getAttribute("first_year"); if(first_year == null) first_year = "";
	String last_year = (String)request.getAttribute("last_year"); if(last_year == null) last_year = "";

	//년도 구간 구하기
	int fy = Integer.parseInt(first_year);
	int ly = Integer.parseInt(last_year);
	int df = ly - fy + 2;

	String[][] year = new String[df][2];
	year[0][0] = "";
	year[0][1] = "전체년도";
	for(int i=1,j=0; i<df; i++,j++) {
		year[i][0] = Integer.toString(fy+j);
		year[i][1] = Integer.toString(fy+j);
	}

	//--------------------------------------
	//페이지 링크 문자열 가져오기
	//--------------------------------------
	String view_pagecut="";
	int view_total=0,view_boardpage=0,view_totalpage=0;

	com.anbtech.psm.entity.psmMasterTable pageL = new com.anbtech.psm.entity.psmMasterTable();
	pageL = (psmMasterTable)request.getAttribute("PAGE_List");
	view_pagecut = pageL.getPageCut();
	view_total = pageL.getTotalArticle();
	view_boardpage = pageL.getCurrentPage();
	view_totalpage = pageL.getTotalPage();

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PSM_List");
	table = new psmMasterTable();
	Iterator table_iter = table_list.iterator();

	//--------------------------------------
	//과제진행별 COLOR리스트 가져오기
	//--------------------------------------
	ArrayList color_list = new ArrayList();
	color_list = (ArrayList)request.getAttribute("COLOR_List");
	int color_cnt = color_list.size();
	String[][] status_color = new String[color_cnt][2];

	color = new psmEnvTable();
	Iterator color_iter = color_list.iterator();
	int n=0;
	while(color_iter.hasNext()){
		color = (psmEnvTable)color_iter.next();
		status_color[n][0] = color.getEnvStatus();
		status_color[n][1] = color.getEnvName();
		n++;
	}
	
%>

<HTML><HEAD><TITLE>과제상태 현황</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../psm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0" encType="multipart/form-data" onSubmit='javascript:goSearch();return false;'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../psm/images/blet.gif" align="absmiddle"> 과제상태 현황</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../psm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../psm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../psm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width='80%' style="padding-left:10px">
					<select name="psm_start_date" style=font-size:9pt;color="black";>  
					<%
						String ysel = "";
						for(int si=0; si<df; si++) {
							if(psm_start_date.equals(year[si][0])) ysel = "selected";
							else ysel = "";
							out.println("<option "+ysel+" value='"+year[si][0]+"'>"+year[si][1]+"</option>");
						}
					%>
					</select>
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"psm_type","comp_name","contract_date","psm_korea"};
						String[] snames = {"과제종류","과제고객","계약일자(yyyyMMdd)","과제명(한글)"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<a href='Javascript:goSearch();'><img src='../psm/images/bt_search3.gif' border='0' align='absmiddle'></a> <a href="javascript:sendExcel();"><img src="../psm/images/bt_excel.gif" border=0 align='absbottom'></a></TD>
				<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=23></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=90 align=middle class='list_title'>과제코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>과제명(한글)</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=70 align=middle class='list_title'>과제종류</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>과제고객</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>카테고리</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>과제상태</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>등록일자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>계약일자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=110 align=middle class='list_title'>과제PM</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=23></TD></TR>
<%
	String status="",colour="";
	while(table_iter.hasNext()){
		table = (psmMasterTable)table_iter.next();
		status = table.getPsmStatus();	if(status.equals("11")) status = "1";
		if(status.equals("1")) {
			status = "미진행";
		} else if(status.equals("2")) {
			status = "진행";
		}else if(status.equals("3")) {
			status = "재진행";
		}else if(status.equals("4")) {
			status = "완료";
		}else if(status.equals("5")) {
			status = "보류";
		}else if(status.equals("6")) {
			status = "취소";
		}
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
						<TD align=middle class='list_bg'><%=status%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getRegDate()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getContractDate()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getPsmPm()%></td>
						<TD><IMG height=1 width=1></TD>
					</TR>
					<TR><TD colspan=23 background="../psm/images/dot_line.gif"></TD></TR>
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

//검색하기
function goSearch()
{
	document.sForm.action='../servlet/PsmProcessServlet';
	document.sForm.mode.value='view_search';
	document.sForm.page.value='1';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//상세내용보기
function psmView(pid)
{
	document.sForm.action='../servlet/PsmProcessServlet';
	document.sForm.mode.value='view_project';
	document.sForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//EXCEL출력
function sendExcel()
{
	psm_start_date = document.sForm.psm_start_date.value;
	sItem = document.sForm.sItem.value;
	sWord = document.sForm.sWord.value;
	var para = "psm_start_date="+psm_start_date+"&sItem="+sItem+"&sWord="+sWord;
	var strUrl = "../servlet/PsmProcessServlet?mode=excel_search&"+para;
	wopen(strUrl,"proxy","870","600","scrollbars=yes,toolbar=no,menubar=yes,status=yes,resizable=yes");
}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>