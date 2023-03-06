<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "BOM 기본정보 LIST"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.bm.entity.mbomMasterTable table;
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "pid";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";

	//--------------------------------------
	//페이지 링크 문자열 가져오기
	//--------------------------------------
	String view_pagecut="";
	int view_total=0,view_boardpage=0,view_totalpage=0;

	com.anbtech.bm.entity.mbomMasterTable pageL = new com.anbtech.bm.entity.mbomMasterTable();
	pageL = (mbomMasterTable)request.getAttribute("PAGE_List");
	view_pagecut = pageL.getPageCut();
	view_total = pageL.getTotalArticle();
	view_boardpage = pageL.getCurrentPage();
	view_totalpage = pageL.getTotalPage();

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("MASTER_List");
	table = new mbomMasterTable();
	Iterator table_iter = table_list.iterator();
	
%>

<HTML><HEAD><TITLE>BOM 기본정보등록현황</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" >
<FORM method=post name=sForm style="margin:0" onsubmit='Javascript:goSearch(); return false;' >

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top"  oncontextmenu="return false">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../bm/images/blet.gif" align="absmiddle"> BOM정보등록현황</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../bm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../bm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../bm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
					
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width='80%' style="padding-left:5px">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"model_name","model_code","fg_code"};
						String[] snames = {"모델명","모델코드","FG코드"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<input type='image' src="../bm/images/bt_search3.gif" onblur="Javascript:goSearch()" align='absmiddle'> 
					<a href='javascript:app()'><img src="../bm/images/bt_sangsin.gif" border='0' align='absmiddle' onclick=''></a>
					</TD>
				<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=30 align=middle class='list_title'>선택</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>모델코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>모델명</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
						
						<TD noWrap width=90 align=middle class='list_title'>F/G코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
						<TD noWrap width=70 align=middle class='list_title'>BOM구분</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
						<TD noWrap width=55 align=middle class='list_title'>등록자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>등록일</TD>
						
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
						<TD noWrap width=55 align=middle class='list_title'>승인자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>승인일</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
						<TD noWrap width=70 align=middle class='list_title'>진행상태</TD>
						

					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (mbomMasterTable)table_iter.next();
		String purpose = table.getPurpose();
		if(purpose.equals("0")) purpose = "제조BOM";
		else if(purpose.equals("1")) purpose = "임시BOM";
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'>
					<% 
						//제조BOM이면서 BOM등록중인 경우만 상신할 수 있음
						if ("0".equals(table.getPurpose()) && "BOM등록".equals(table.getBomStatus()))	{ 
							out.println("<input type='checkbox' name='checkbox' value='"+table.getPid()+"'>");
						 } 
					%>
						</TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getModelCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=table.getModelName()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getFgCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=purpose%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getRegName()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=anbdt.getSepDate(table.getRegDate(),"-")%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getAppName()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=anbdt.getSepDate(table.getAppDate(),"-")%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=table.getBomStatus()%></td>
					</TR>
					<TR><TD colspan=19 background="../bm/images/dot_line.gif"></TD></TR>
<%		}
%>				</TBODY></TABLE></TD></TR>
</TBODY></TABLE>

<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="page" size="15" value="">
<input type="hidden" name="pid" size="15" value="">
</form>

</body>
</html>


<script language=javascript>
<!--
//메시지 전달
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/BomBaseInfoServlet';
	document.sForm.mode.value='info_list';
	document.sForm.page.value='1';
	document.sForm.submit();
}
//편집하기
function masterView(pid)
{
	document.sForm.action='../servlet/BomBaseInfoServlet';
	document.sForm.mode.value='info_premodify';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}

//상신하기
function app()
{

	sParam = "../gw/approval/module/mbom_App.jsp?pid=";
	var f = document.sForm.checkbox;	
	if(f == undefined) { alert("상신할 문서를 선택한 후, 실행하세요."); return; }
	var s_count = 0;
    for(i=0;i<f.length;i++){
		if(f[i].checked){
			sParam += f[i].value;
			s_count++;
		}
    }
	var t = document.sForm.checkbox.checked;
	if(t == true) { sParam += document.sForm.checkbox.value; s_count = 1; }

    if(s_count == 0){
	   alert("상신할 문서를 선택한 후, 실행하세요.");
	   return;
    } else if(s_count > 1) {
		alert("하나만 선택한 후, 실행하세요.");
	   return;
	}

	document.sForm.action=sParam;
	document.sForm.submit();
}

-->
</script>