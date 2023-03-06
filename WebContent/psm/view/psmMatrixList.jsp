<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "PSM 전체현황(TABLE) LIST"		
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
	String psm_start_date = (String)request.getAttribute("psm_start_date"); 
	if(psm_start_date == null) psm_start_date = "";
	String first_year = (String)request.getAttribute("first_year"); if(first_year == null) first_year = "";
	String last_year = (String)request.getAttribute("last_year"); if(last_year == null) last_year = "";
	String max_cnt = (String)request.getAttribute("max_cnt"); if(max_cnt == null) max_cnt = "0";
	int max_count = Integer.parseInt(max_cnt); 		//동일카테고리중 최대 과제수
	int span=max_count * 2 + 6;

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

<HTML><HEAD><TITLE>과제카테고리별현황</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../psm/css/style.css" rel=stylesheet type="text/css">
<style type='text/css'>
<!--
/* 카테고리의 과제명 */
.spc_text_01   {
	COLOR:#000000;FONT WEIGHT : 700;font-size: 9pt;padding-left:10px;
}
A:link { color: #400040; font-size: 8pt; text-decoration: none ; font-weight:100;}
A:visited { color: #400040; font-size: 8pt; text-decoration: none ; font-weight:100; } 
A:hover { color: #400040; font-size: 8pt; text-decoration: none ; font-weight:100;} 
A:active { color: #400040; font-size: 8pt; text-decoration: none; font-weight:100; } 
-->
</style>
</HEAD>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false" onLoad="display()">
<form name="sForm" method="post" style="margin:0" encType="multipart/form-data">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../psm/images/blet.gif" align="absmiddle"> 과제카테고리별현황</TD>
					</TR></TBODY></TABLE></TD></TR>
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
					<a href='Javascript:goSearch();'><img src='../psm/images/bt_search3.gif' border='0' align='absmiddle'></a> <a href="javascript:sendExcel();"><img src="../psm/images/bt_excel.gif" border=0 align='absbottom'></a></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TR bgColor=#9DA9B9 height=2><TD></TD></TR>
</TBODY></TABLE>

<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=23>
						<TD noWrap width=120 align=middle class='list_title'>과제고객</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>카테고리</TD>
<% for(int i=0,t=1; i<max_count; i++,t++) { 
		out.println("<TD noWrap width=6 class='list_title'><IMG src='../psm/images/list_tep2.gif'></TD>");
		out.println("<TD noWrap width=100 align=middle class='list_title'>"+t+"</TD>");
	}
%>	
					<TD noWrap width=6 class='list_title'><IMG src="../psm/images/list_tep2.gif"></TD>
					<TD noWrap width=100% align=middle class='list_title'></TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=<%=span%>></TD></TR>

<%
	String status="",colour="",psm_subject="";
	int sep=0;
	while(table_iter.hasNext()){
		table = (psmMasterTable)table_iter.next();
		status = table.getPsmStatus(); if(status.equals("11")) status="1";
		if(status.equals("1")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "미진행";
			psm_subject = "<a href=\"javascript:psmView('"+table.getPid()+"');\"><B>";
			psm_subject += table.getPsmKorea()+"["+status+"]"+"</B></a>";
		} else if(status.equals("2")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "진행";
			psm_subject = "<a href=\"javascript:psmView('"+table.getPid()+"');\"><B>";
			psm_subject += table.getPsmKorea()+"["+status+"]"+"</B></a>";
		}else if(status.equals("3")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "재진행";
			psm_subject = "<a href=\"javascript:psmView('"+table.getPid()+"');\"><B>";
			psm_subject += table.getPsmKorea()+"["+status+"]"+"</B></a>";
		}else if(status.equals("4")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "완료";
			psm_subject = "<a href=\"javascript:psmView('"+table.getPid()+"');\"><B>";
			psm_subject += table.getPsmKorea()+"["+status+"]"+"</B></a>";
		}else if(status.equals("5")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "보류";
			psm_subject = "<a href=\"javascript:psmView('"+table.getPid()+"');\"><B>";
			psm_subject += table.getPsmKorea()+"["+status+"]"+"</B></a>";
		}else if(status.equals("6")) {
			for(int i=0; i<color_cnt; i++) if(status_color[i][0].equals(status)) colour=status_color[i][1];
			status = "취소";
			psm_subject = "<a href=\"javascript:psmView('"+table.getPid()+"');\"><B>";
			psm_subject += table.getPsmKorea()+"["+status+"]"+"</B></a>";
		} else {
			colour="#F5FFF2";
			status = "";
			psm_subject = "";
		}

		//HTML 문장 시작점
		if(sep == 0) {
			out.println("<TR>");
			out.println("<TD noWrap width=120 align=middle height='24' class='bg_01'>"+table.getCompName()+"</TD>");
			out.println("<TD noWrap width=6><IMG src='../psm/images/list_tep2.gif'></TD>");
			out.println("<TD noWrap width=80 align=middle class='spc_text_01'>"+table.getCompCategory()+"</td>");
			out.println("<TD noWrap width=6><IMG src='../psm/images/list_tep2.gif'></TD>");
		}  

		//HTML 문장 중간
		out.println("<TD noWrap width=100 align=middle class='list_bg' bgcolor="+colour+">");
		if(status.equals("진행") || status.equals("재진행")) {
			out.println("<marquee behavior='alternate' width='80%' scrollamount=1>");
			//out.println("<blink>");
		}
		out.println(psm_subject);

		if(status.equals("진행") || status.equals("재진행")) {
			//out.println("</blink>");
			out.println("</marquee>");
		}
		out.println("</td>");
		out.println("<TD noWrap width=6><IMG height=1 width=1></TD>");
	
		//HTML 문장 마지막
		sep++;
		if(max_count == sep) {
			out.println("</TR>");
			out.println("<TR><TD colspan='"+span+"' background='../psm/images/dot_line.gif'></TD></TR>");
			sep=0;
		}  
	}
%>
		
			</TBODY></TABLE></TD></TR>
</TBODY></TABLE>
</DIV>

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
	document.sForm.mode.value='view_matrix';
	document.sForm.page.value='1';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//편집하기
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
	var strUrl = "../servlet/PsmProcessServlet?mode=excel_matrix&psm_start_date="+psm_start_date;
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
//리스트가 긴경우 상단메뉴 DIV처리
function display()
{
	var w = window.screen.width;
	var h = window.screen.height;
	var div_h = h - 328;		//355
	item_list.style.height = div_h;
}
//깜빡이는 글자 만들기
function blinkIt()
{
  for(i=0; i<document.all.tags('blink').length; i++)
  {
    s = document.all.tags('blink')[i];
    s.style.visibility = (s.style.visibility == 'visible') ? 'hidden' : 'visible';
  }
}
setInterval('blinkIt()',100)

-->
</script>