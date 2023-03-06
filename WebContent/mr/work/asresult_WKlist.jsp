<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "AS실적 조회(업체용)"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mr.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();

	//---------------------------------------------------------
	//	파라미터 받기
	//--------------------------------------------------------
	String company_no="",sItem="",sWord="";
	com.anbtech.mr.entity.assupportTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");

	para = new assupportTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (assupportTable)para_iter.next();
		company_no = para.getCompanyNo();			//업체코드
		sItem = para.getSitem();		
		sWord = para.getSword();
	}

	//----------------------------------------------------
	//	실적정보 LIST읽기 : as_result
	//----------------------------------------------------
	com.anbtech.mr.entity.assupportTable work;
	ArrayList work_list = new ArrayList();
	work_list = (ArrayList)request.getAttribute("WORK_List");
	work = new assupportTable();
	Iterator work_iter = work_list.iterator();
	int work_cnt = work_list.size();

	String[][] result = new String[work_cnt][7];
	int e = 0;
	while(work_iter.hasNext()) {
		work = (assupportTable)work_iter.next();
		result[e][0] = work.getRegisterNo();	//등록번호
		result[e][1] = work.getAsContent();		//AS내용
		result[e][2] = work.getRequestDate();	//요청일	
		result[e][3] = work.getAsDate();		//방문일	
		result[e][4] = work.getModify();		//수정
		result[e][5] = work.getDelete();		//삭제
		result[e][6] = work.getMail();			//메일보내기
		e++;
	}

	//----------------------------------------------------
	//	페이지 링크정보 읽기
	//----------------------------------------------------
	com.anbtech.mr.entity.assupportTable pages;
	ArrayList pages_list = new ArrayList();
	pages_list = (ArrayList)request.getAttribute("PAGE_List");
	pages = new assupportTable();
	Iterator pages_iter = pages_list.iterator();
	
	pages = (assupportTable)pages_iter.next();
	String total_article = 	Integer.toString(pages.getTotalArticle());		//총 항목수
	String current_page = Integer.toString(pages.getCurrentPage());			//현재페이지
	String total_page = Integer.toString(pages.getTotalPage());				//총페이지	
	String page_cut = pages.getPageCut();									//페이지 링크

//TEST
company_no = "C02";
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mr/css/style.css" rel=stylesheet></head>
<script language='javascript'>
<!--
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/asresultworkServlet';
	document.sForm.mode.value='ART_L';
	document.sForm.page.value='1';
	document.sForm.submit();
}
//내용보기
function workView(pid)
{
	var para = "strSrc=../servlet/asresultworkServlet&mode=ART_V&pid="+pid+"&width=830&height=520";	showModalDialog('../mr/modalFrm.jsp?'+para,'view','dialogWidth:845px;dialogHeight:550px;toolbar=0;scrollBars=auto;status=no;resizable=no');

	
	//document.open('../servlet/asresultworkServlet?mode=ART_V&pid='+pid,'view','Width:350px;Height:232px;toolbar=0;scrollBars=no;status=no;resizable=no');
}
//수정하기
function workModify(pid)
{
	document.sForm.action='../servlet/asresultworkServlet';
	document.sForm.mode.value='ART_MV';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}
//삭제하기
function workDelete(pid)
{
	document.sForm.action='../servlet/asresultworkServlet';
	document.sForm.mode.value='ART_D';
	document.sForm.pid.value=pid;
	document.sForm.page.value='1';
	document.sForm.submit();
}
//멜 보내기
function workMail(pid)
{
	document.sForm.action='../servlet/asresultworkServlet';
	document.sForm.mode.value='ART_S';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}
-->
</script>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mr/images/blet.gif" align="absmiddle"> A/S 실적조회</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../mr/images/setup_total.gif" border="0" align="absmiddle"><%=total_article%> <img src="../mr/images/setup_articles.gif" border="0" align="absmiddle"> <%=current_page%>/<%=total_page%> <img src="../mr/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD width='2%'>&nbsp;</TD>
				<TD align=left width='53%'>
					<form name="sForm" method="post" style="margin:0">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"as_content","as_delay","as_issue","as_date"};
						String[] snames = {"주요내용","지연사유","이슈사항","방문일"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<a href='Javascript:goSearch();'><img src='../mr/images/bt_search3.gif' border='0' align='absmiddle'></a> 
					<input type='hidden' name='mode' value=''>
					<input type='hidden' name='pid' value=''>
					<input type='hidden' name='page' value=''>
					<input type='hidden' name='company_no' value='<%=company_no%>'>
					</form> 
					</TD>
				<TD width='' align='right' style="padding-right:10px"><%=page_cut%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=20 align=middle class='list_title'>No</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>등록번호</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>A/S내용</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>요청일</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>방문일</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
						<TD noWrap width=140 align=middle class='list_title'>비고</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
				<%
					for(int i=0,j=1; i<work_cnt; i++,j++) {	 
				%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'><%=j%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=result[i][0]%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'><%=result[i][1]%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=result[i][2]%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=result[i][3]%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=result[i][4]%> <%=result[i][5]%> <%=result[i][6]%></td>
					</TR>
					<TR><TD colspan=19 background="../mr/images/dot_line.gif"></TD></TR>
				<%
					}
				%>
				</TBODY></TABLE></TD></TR>

</TBODY></TABLE>
</body>
</html>

