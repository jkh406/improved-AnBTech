<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "임시 BOM 기본정보 LIST"		
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

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("MASTER_List");
	table = new mbomMasterTable();
	Iterator table_iter = table_list.iterator();
	
%>

<HTML><HEAD><TITLE>임시 BOM 기본정보 LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">

<LINK href="../bm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0"  oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0"  onsubmit='Javascript:goSearch(); return false;'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27>
		<TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../bm/images/blet.gif" align="absmiddle"> 임시BOM 등록현황</TD>
					</TR></TBODY></TABLE></TD></TR>
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
					<INPUT type="text" name="sWord" size="15" value="<%=sWord%>">
					<input type='image' src="../bm/images/bt_search3.gif" onblur="Javascript:goSearch()" align='absmiddle'> 
					<INPUT type="hidden" name="mode" size="15" value="">
					<INPUT type="hidden" name="pid" size="15" value="">
						
					</form>
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--리스트-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=30 align=middle class='list_title'>번호</TD>
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
	int j = 1;
	while(table_iter.hasNext()){
		table = (mbomMasterTable)table_iter.next();
		String purpose = table.getPurpose();
		if(purpose.equals("0")) purpose = "제조BOM";
		else if(purpose.equals("1")) purpose = "임시BOM";
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'><%=j%></td>
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
<%		j++;
	}
%>
				</TBODY></TABLE></TD></TR>

</TBODY></TABLE>
</body>
</html>


<script language=javascript>
<!--
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/BomBaseInfoServlet';
	document.sForm.mode.value='tbom_list';
	document.onmousedown=dbclick; // 전송중, 또다른 버튼이 눌려지지 않도록...
	document.sForm.submit();
}
//내용보기
function masterView(pid)
{	
	document.sForm.action='../servlet/BomBaseInfoServlet';
	document.sForm.mode.value='tbom_preapp';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}

//입력페이지에서 전송(등록)버튼 클릭 후, 전송중에 또 버튼 누르지 못하도록 처리
function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}
-->
</script>