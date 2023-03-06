<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "전사공통 과제상태변경 LIST"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.pjt.entity.projectTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	
	//-----------------------------------
	//	공문공지 내용 & 전체 갯수 파악하기
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_List");
	table = new projectTable();
	Iterator table_iter = table_list.iterator();
	
	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	String search_item = Hanguel.toHanguel(request.getParameter("sItem")); 
	if(search_item == null) search_item = "pjt_name";
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
	document.sForm.action='../servlet/projectStatusServlet';
	document.sForm.mode.value='PJS_LA';
	document.sForm.page.value=a;
	document.sForm.submit();
}

//검색하기
function goSearch()
{
	document.sForm.action='../servlet/projectStatusServlet';
	document.sForm.mode.value='PJS_LA';
	document.sForm.page.value='1';
	document.sForm.submit();
}

//수정할 내용읽기
function pjtChgStatus(pjt_code)
{
	document.sForm.action='../servlet/projectStatusServlet';
	document.sForm.mode.value='PJS_MVA';
	document.sForm.pjt_code.value=pjt_code;
	document.sForm.submit();
}

//내용삭제하기
function pjtDelete(pjt_code)
{
	d = confirm("삭제하시겠습니까?");
	if(d == false) return;
	document.dForm.action='../servlet/projectStatusServlet';
	document.dForm.mode.value="PJS_DA";
	document.dForm.pjt_code.value=pjt_code;
	document.dForm.submit();
}
//개별 상세보기
function pjtView(pjt_code)
{
	sParam = "strSrc=../servlet/projectStatusServlet&pjt_code="+pjt_code+"&mode=PJS_VA";	showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:340px;resizable=0");
	
}
-->
</script>

<LINK href="../pjt/css/style.css" rel=stylesheet>
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
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> 전사공통 과제상태변경</TD>
				<TD style="padding-right:10px" align='right' valign='middle'><%=Cpage%>/<%=Tpage%> <img src="../pjt/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD width=4>&nbsp;</TD>
				<TD align=left width='400'>
					<form name="sForm" method="post" style="margin:0">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"pjt_name","pjt_code"};
						String[] snames = {"과제이름","과제코드"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(search_item.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=search_word%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="">
					<input type="hidden" name="page" size="15" value="">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a>
					</form>
				</TD>
				<TD width='' align='right' style="padding-right:10px">
					<%	if (Cpage <= 1) {	%>		
						<img src='../pjt/images/bt_previous.gif' border='0' align='absmiddle'>
					 <%	} else 	{	%>		
						<a href='javascript:goPage(<%=Cpage-1%>)'>
						<img src='../pjt/images/bt_previous.gif' border='0' align='absmiddle'></a>

					 <%	} if ((Cpage != Tpage) && (Tpage != -1 )) { %>		
							<a href='javascript:goPage(<%=Cpage+1%>)'>
							<img src='../pjt/images/bt_next.gif' border='0' align='absmiddle'></a> 		
					 <%	} else 	{  %>		
							<img src='../pjt/images/bt_next.gif' border='0' align='absmiddle'>
					 <%	} %>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=25>
				<TD noWrap width=100 align=middle class='list_title'>과제코드</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>과제이름</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>변경일</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>진행상태</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=140 align=middle class='list_title'>OPTION</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
		<% if (table_list.size() == 0) { %>
			<TR vAlign=middle height=25>
				<TD colspan='11' align="middle">***** 내용이 없습니다. ****</TD>
			</TD> 
		<% } %>	

		<% 
			int n = 1;
			String status = "";
			while(table_iter.hasNext()) {
				table = (projectTable)table_iter.next();
				status = table.getPjtStatus();
				if(status.equals("S")) status = "미착수";
				else if(status.equals("0")) status = "등록중";
				else if(status.equals("1")) status = "진행중";
				else if(status.equals("2")) status = "완료";
				else if(status.equals("3")) status = "DROP";
				else if(status.equals("4")) status = "HOLD";
		%>
			<form name="aForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=table.getPjtCode()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=table.getPjtName()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=table.getInDate()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=status%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=table.getModify()%>&nbsp;<%=table.getDelete()%>&nbsp;<%=table.getView()%></TD>
			</TR>
			<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			n++;
			}  //while 

		%>
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

<% // 삭제 form %>
<form name="dForm" method="post" style="margin:0">
<input type="hidden" name="mode" value=''>
<input type="hidden" name="pjt_code" value=''>
</form>

</BODY>
</HTML>

