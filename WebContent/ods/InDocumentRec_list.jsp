<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "사내공문수신 LIST"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage = "../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dms.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.dms.entity.OfficialDocumentTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	
	//-----------------------------------
	//	공문공지 내용 & 전체 갯수 파악하기
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_List");
	table = new OfficialDocumentTable();
	Iterator table_iter = table_list.iterator();
	
	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	String search_item = Hanguel.toHanguel(request.getParameter("sItem")); 
	if(search_item == null) search_item = "subject";
	String search_word = Hanguel.toHanguel(request.getParameter("sWord"));
	if(search_word == null) search_word = "";

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//페이지 이동하기
function goPage(a) 
{
	document.sForm.action='../servlet/InDocumentRecServlet';
	document.sForm.page.value=a;
	document.sForm.submit();
}

//검색하기
function goSearch()
{
	document.sForm.action='../servlet/InDocumentRecServlet';
	document.sForm.page.value='1';
	document.sForm.submit();
}

//개별내용 보기 (결재승인전)
function contentReview(id)
{
	//sParam = "strSrc=InDocumentRecServlet&mode=IDR_V&id="+id;
	
	//var rval = showModalDialog("<%=server_path%>/ods/DocModalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	wopen('InDocumentRecServlet?mode=IDR_V&id='+id,'view_doc','680','650','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//개별내용 보기 (결재승인후)
function contentAppview(id,app_id)
{
	//sParam = "strSrc=InDocumentRecServlet&mode=IDR_A&id="+id+"&doc_id="+app_id;
	
	//var rval = showModalDialog("../ods/DocModalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	wopen('InDocumentRecServlet?mode=IDR_A&id='+id+'&doc_id='+app_id,'view_doc','680','650','scrollbars=yes,toolbar=no,status=no,resizable=no');
}
//공문서 공유하기
function contentShare(id,subj)
{
	var mode = "IDR_S";
	var tablename = "InDocument_receive";
	wopen('../ods/shareReceiver.jsp?target=sForm&id='+id+'&tablename='+tablename+'&subj='+subj+'&mode='+mode,'proxy','510','467','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>
<LINK href="../ods/css/style.css" rel='stylesheet'>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ods/images/blet.gif" align="absmiddle"> 사내공문수신</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><%=Cpage%>/<%=Tpage%> <img src="../ods/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'>
					<form name="sForm" method="post" style="margin:0">
					<select name="sItem" style="font-size:9pt;">  
					<%
						String[] sitems = {"subject","sending","in_date"};
						String[] snames = {"제목","발신","작성일"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(search_item.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select> <input type="text" name="sWord" size="10" value="<%=search_word%>"> <a href='Javascript:goSearch();'><img src='../ods/images/bt_search3.gif' border='0' align='absmiddle'></a>
					<input type="hidden" name="mode" size="15" value="IDR_L">
					<input type="hidden" name="id" size="15" value="">
					<input type="hidden" name="page" size="15" value=""></form></TD>
			  <TD width='' align='right' style="padding-right:10px">
					<%	if (Cpage <= 1) {	%>		
						<img src='../ods/images/bt_previous.gif' border='0' align='absmiddle'>
					 <%	} else 	{	%>		
						<a href='javascript:goPage(<%=Cpage-1%>)'>
						<img src='../ods/images/bt_previous.gif' border='0' align='absmiddle'></a>

					 <%	} if ((Cpage != Tpage) && (Tpage != -1 )) { %>		
							<a href='javascript:goPage(<%=Cpage+1%>)'>
							<img src='../ods/images/bt_next.gif' border='0' align='absmiddle'></a> 		
					 <%	} else 	{  %>		
							<img src='../ods/images/bt_next.gif' border='0' align='absmiddle'>
					 <%	} %></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--리스트-->
  <TR height=100%>
    <TD vAlign=top><form name="aForm" method="post" style="margin:0">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=70 align=middle class='list_title'>접수번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>문서번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>제목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>접수일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>발신처</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>비고</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
	<% 
		while(table_iter.hasNext()) {
			table = (OfficialDocumentTable)table_iter.next();
			String rdate = table.getInDate();		//작성일자
			rdate = rdate.substring(0,10);
	%>	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=table.getSerialNo()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=table.getDocId()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getSubject()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=rdate%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getSending()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getModify()%> <%=table.getDelete()%></TD></TR>
			<TR><TD colSpan=11 background="../ods/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>

</BODY>
</HTML>
