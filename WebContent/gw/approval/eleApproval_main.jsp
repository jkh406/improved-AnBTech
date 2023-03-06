<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "전자결재 메인"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
//	com.anbtech.gw.db.AppProcessMasterDAO masterDAO = new com.anbtech.gw.db.AppProcessMasterDAO();
	com.anbtech.gw.entity.TableAppMaster table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	String PROCESS_NAME = "";			//전자결재함 이름
	String Message = "";
	String STATE = "";
	String pageNo = "";

	//-----------------------------------
	//	전자결재 내용 & 전체 갯수 파악하기
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new TableAppMaster();
	Iterator table_iter = table_list.iterator();

	String PROCESS = request.getParameter("PROCESS");
	String RTpage = request.getParameter("Tpage");			//전체페이지 수
	int Tpage = Integer.parseInt(RTpage);
	String RCpage = request.getParameter("Cpage");			//현지페이지 수
	int Cpage = Integer.parseInt(RCpage);

	if(PROCESS.equals("APP_ING")) PROCESS_NAME = "미결함";
	else if(PROCESS.equals("ASK_ING")) PROCESS_NAME = "진행함";
	else if(PROCESS.equals("APP_BOX")) PROCESS_NAME = "기결함";
	else if(PROCESS.equals("APP_GEN")) PROCESS_NAME = "기결함 (일반문서)";
	else if(PROCESS.equals("APP_SER")) PROCESS_NAME = "기결함 (고객관리)";
	else if(PROCESS.equals("REJ_BOX")) PROCESS_NAME = "반려함";
	else if(PROCESS.equals("TMP_BOX")) PROCESS_NAME = "저장함";
	else if(PROCESS.equals("SEE_BOX")) PROCESS_NAME = "통보함";
	else if(PROCESS.equals("DEL_BOX")) PROCESS_NAME = "삭제함";
	else PROCESS_NAME = "기결함 (모든양식결재)";

	String search_item = Hanguel.toHanguel(request.getParameter("sItem")); 
	if(search_item == null) search_item = "app_subj";
	String search_word = Hanguel.toHanguel(request.getParameter("sWord"));
	if(search_word == null) search_word = "";

	//-------------------------------------------
	// 부재중 대리결재인 지정자 있는지 판단
	// attorney_yn : Y[지정자 있음], N[지정자 없음]
	//-------------------------------------------
	String attorney_yn = Hanguel.toHanguel(request.getParameter("attorney_yn")); 
	if(attorney_yn == null) attorney_yn = "N";			

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
var prs = '<%=PROCESS%>';
if(prs == "TMP_BOX") parent.menu.location.reload();
//페이지 이동하기
function goPage(a) {
	document.sForm.action='../servlet/ApprovalMenuServlet';
	document.sForm.mode.value='<%=PROCESS%>';
	document.sForm.page.value=a;
	document.sForm.submit();
}
//검색하기
function goSearch()
{
	//num = sForm.sItem.selectedIndex;
	//s_item = sForm.sItem.options[num].value;
	document.sForm.action ='../servlet/ApprovalMenuServlet'
	document.sForm.mode.value='<%=PROCESS%>';
	document.sForm.page.value=1;
	document.sForm.submit();
}

//결재문서 보기
function eleApprovalView(a,b)
{
	//미결함이면서 대리결재자가 지정되어있으면 종료
	var PROCESS = '<%=PROCESS%>';
	var ATTORNEY = '<%=attorney_yn%>';
	if((PROCESS == 'APP_ING') && (ATTORNEY == 'Y')) {
		alert('부재중 대리결재자가 지정되어있습니다.\n\n 대리결재자를 해지후 진행하십시요.'); 
		return; 
	}

	//미결문서
	//if(PROCESS == 'APP_ING') {
		location.href ='../servlet/ApprovalDetailServlet?mode='+b+'&PID='+a;
	//} 
	//그외문서
	//else {
//		sParam = "strSrc=ApprovalDetailServlet&title=APPROVAL&PID="+a+"&mode="+b;
		//var rval = showModalDialog("../gw/approval/modalFrm.jsp?"+sParam,"","dialogWidth:750px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=no;resizable=0");

		//wopen('../servlet/ApprovalDetailServlet?PID='+a+'&mode='+b,'view_doc','730','600','scrollbars=yes,toolbar=no,status=no,resizable=no');
/*
		if(rval == "RL"){
			parent.menu.location.href="../servlet/ApprovalInitServlet?mode=menu";
			parent.view.location.href="../servlet/ApprovalMenuServlet?mode="+PROCESS;
		}
*/
	//} 
}

//임시저장문서 삭제
function pidDelete(a)
{
	d = confirm("삭제하시겠습니까?");
	if(d == false) return;
	
	//삭제
	document.sForm.action ='../servlet/ApprovalDeleteServlet';
	document.sForm.pid.value=a;
	document.sForm.submit();
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
<LINK href="../gw/css/style.css" rel=stylesheet>
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
			  <TD valign='middle' class="title"><img src="../gw/images/blet.gif" align="absmiddle">
				 <%
					 if((PROCESS == null) || (PROCESS.equals("APP_ING")))	//미결함
						out.println("미결함");
					 else if (PROCESS.equals("ASK_ING"))					//진행함
						out.println("진행함");
					 else if (PROCESS.equals("REJ_BOX"))					//반려함
						out.println("반려함");
					 else if (PROCESS.equals("TMP_BOX"))					//임시함
						out.println("저장함");
					 else if (PROCESS.equals("SEE_BOX"))					//통보함
						out.println("통보함");
					 else if (PROCESS.equals("DEL_BOX"))					//삭제함
						out.println("삭제함");
					else if (PROCESS.equals("APP_BOX"))						//기결함
						out.println("기결함");
					 else 
						out.println("보관함");
				  %>
			  </TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><%=Cpage%>/<%=Tpage%> <img src="../gw/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='600'>
				<form name="sForm" method="post" style="margin:0">
					<%	//삭제함 문서검색을위해 추가 [문서종류,년도포함]
						int byear=Integer.parseInt(anbdt.getYear())-1; //1년전
						if (PROCESS.equals("DEL_BOX")) {  
						out.println("<input type='text' name='syear' size='5' value='"+byear+"'>");	
						
						out.println("<select name='flag' style='font-size:9pt;';>"); 
						String[] dflg = {"","HYU_GA","OE_CHUL","CHULJANG_SINCHEONG","BOGO","CHULJANG_BOGO","GIAN","MYEONGHAM","SAYU","HYEOPJO","GUIN","GYOYUK_ILJI","BAE_CHA","AKG","TD","SERVICE","ODT","IDS","ODS","ASSET","EST","EWK","BOM","DCM","PCR","ODR","PWH","TGW"};
						String[] dname = {"전체","휴(공)가원","외출계","출장신청서","보고서","출장보고","기안서","명함신청서","사유서","협조전","구인의뢰서","교육일지","배차신청서","승인원","기술문서","고객관리","공지공문","사내공문","사외공문","자산관리","견적관리","특근관리","BOM관리","설계변경","구매요청","발주요청","구매입고","부품출고"};
						for(int di=0; di<dflg.length; di++) {
							out.println("<option value='"+dflg[di]+"'>"+dname[di]+"</option>");
						}
						out.println("</select>");
				   } %>

					<select name="sItem" style="font-size:9pt;";>  
					<%
						String[] sitems = {"app_subj","writer_name","write_date"};
						String[] snames = {"제목","작성자","작성일"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(search_item.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=search_word%>">
					<a href='Javascript:goSearch();'><img src='../gw/images/bt_search.gif' border='0' align='absmiddle'></a>
					<input type="hidden" name="mode" value="">	
					<input type="hidden" name="page" value="<%=Cpage%>">
					<input type="hidden" name="pid" value="">
					<input type="hidden" name="PID" value=""></form></TD>
			  <TD width='' align='right' style="padding-right:10px">
					<%	if (Cpage <= 1) {	%>		
						<img src='../gw/images/bt_previous.gif' border='0' align="absmiddle">
					 <%	} else 	{	%>		
						<a href='javascript:goPage(<%=Cpage-1%>)'>
						<img src='../gw/images/bt_previous.gif' border='0' align="absmiddle"></a>

					 <%	} if ((Cpage != Tpage) && (Tpage != -1 )) { %>		
							<a href='javascript:goPage(<%=Cpage+1%>)'>
							<img src='../gw/images/bt_next.gif' border='0' align="absmiddle"></a> 		
					 <%	} else 	{  %>		
							<img src='../gw/images/bt_next.gif' border='0' align="absmiddle">
					 <%	} %></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
<!--리스트-->
  <TR height=100%>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>진행상태</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>제목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>첨부</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>기안자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>기안일시</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>비고</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
	<% 
		int count=1;
		while(table_iter.hasNext()) {
			table = (TableAppMaster)table_iter.next();		
	%>	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=count%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=table.getAmAppStatus()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style="padding-left:3px"><%=table.getAmAppSubj()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg' style="padding-left:3px"><%=table.getAmAddCounter()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getAmWriterName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getAmWriteDate()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>
	<% if (PROCESS.equals("TMP_BOX") || PROCESS.equals("REJ_BOX"))	{	//임시함,반려함 %>
				<a href=javascript:pidDelete('<%=table.getAmPid()%>');><img src='../gw/images/lt_del.gif' border='0' align='absmiddle'></a>
    <% } %>
			  </TD></TR>
			<TR><TD colSpan=13 background="../gw/images/dot_line.gif"></TD></TR>
	<% 
			count++;
		}  //while 

	%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>


</body>
</html>