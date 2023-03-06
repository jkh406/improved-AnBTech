<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "제품 생산현황 LIST"		
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
	com.anbtech.mm.entity.mfgProductMasterTable table;
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//포멧
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String msg="";
	String start_date = (String)request.getAttribute("start_date"); if(start_date == null) start_date = "";
	if(start_date.length() != 0) start_date = anbdt.getSepDate(start_date,"/");
	String end_date = (String)request.getAttribute("end_date"); if(end_date == null) end_date = "";
	if(end_date.length() != 0) end_date = anbdt.getSepDate(end_date,"/");
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String factory_no = (String)request.getAttribute("factory_no"); if(factory_no == null) factory_no = "";
	if(factory_no.length() == 0) msg ="해당공장번호를 선택하십시오.";

	//--------------------------------------
	//페이지 링크 문자열 가져오기
	//--------------------------------------
	String view_pagecut="";
	int view_total=0,view_boardpage=0,view_totalpage=0;

	com.anbtech.mm.entity.mfgProductMasterTable pageL = new com.anbtech.mm.entity.mfgProductMasterTable();
	pageL = (mfgProductMasterTable)request.getAttribute("PAGE_List");
	view_pagecut = pageL.getPageCut();
	view_total = pageL.getTotalArticle();
	view_boardpage = pageL.getCurrentPage();
	view_totalpage = pageL.getTotalPage();

	//--------------------------------------
	//리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PRODUCT_List");
	table = new mfgProductMasterTable();
	Iterator table_iter = table_list.iterator();
	
%>

<HTML><HEAD><TITLE>제품 생산현황 LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<FORM name="sForm" method="post" style="margin:0" onsubmit="javascript:goSearch(); return false;">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><IMG src="../mm/images/blet.gif"> 제품 생산현황</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../mm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../mm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../mm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR>
					<TD align=left width=5 ></TD>
					<TD align=left width=500>
						<IMG src='../mm/images/bt_search3.gif' onClick='javascript:goSearch();' style='cursor:hand' align='absmiddle'>
					</TD>
					<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY></TABLE></TD></TR></TABLE>
<!-- 검색정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">품목코드</TD>
			<TD width="37%" height="25" class="bg_04">
				
				<select name="sItem" style=font-size:9pt;color="black";>  
			<%
				String[] sitems = {"","model_name","fg_code","item_code"};
				String[] snames = {"전체검색","모델명","FG코드","재공품코드"};
				String sel = "";
				for(int si=0; si<sitems.length; si++) {
					if(sItem.equals(sitems[si])) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
				}
			%>
			</select>
				<INPUT type='text' size='20' name='sWord' value='<%=sWord%>'></TD>
			<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">공장</TD>
			<TD width="37%" height="25" class="bg_04">
				<INPUT type='text' size='5' name='factory_no' value='<%=factory_no%>' readonly> 
				<INPUT type='text' size='20' name='factory_name' value='' readonly> 
				<a href="javascript:searchFactoryInfo();"><img src="../mm/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">검색일자</TD>
           <TD width="37%" height="25" class="bg_04">
				<INPUT type='text' size='10' maxlength='10' name='start_date' value='<%=start_date%>' readonly> <a href="Javascript:OpenCalendar('start_date')"><img src="../mm/images/bt_search.gif" border="0" align="absmiddle"></a> ~ <INPUT type='text' size='10' maxlength='10' name='end_date' value='<%=end_date%>' readonly> <a href="Javascript:OpenCalendar('end_date')"><img src="../mm/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif"></TD>
           <TD width="37%" height="25" class="bg_04"></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	   </TBODY></TABLE><br>

<!-- 출력정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=90 align=middle class='list_title'>일 자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>모델명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=90 align=middle class='list_title'>제품코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>제품규격</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>생산계획</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>생산실적</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>실적율[%]</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (mfgProductMasterTable)table_iter.next();
		
%>
			<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle class='list_bg' height=23><%=table.getOutputDate()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getModelName()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'> <%=table.getItemCode()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getItemSpec()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=nfm.toDigits(table.getOrderCount())%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=nfm.toDigits(table.getTotalCount())%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getProductRate()%></TD>
				<TD><IMG height=1 width=1></TD>
			</TR>
			<TR><TD colspan=17 background="../mm/images/dot_line.gif"></TD></TR>
<%		
	}
%>
		</TBODY></TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="page" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="">
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
	var sd = document.sForm.start_date.value;	sd=sd.replace(/\//g,"");
	var ed = document.sForm.end_date.value;		ed=ed.replace(/\//g,"");
	if(sd > ed) { alert('검색종료일이 시작일보다 빠른 일자입니다.'); return; }

	document.sForm.action='../servlet/mfgViewServlet';
	document.sForm.mode.value='view_pd_glist';
	document.sForm.page.value='1';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../mm/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//공장정보 찾기
function searchFactoryInfo() {
	var f = document.sForm;
	var factory_no = f.factory_no.name;
	var factory_name = f.factory_name.name;
	
	var para = "field="+factory_no+"/"+factory_name;
	
	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&"+para;
	wopen(url,'enterCode','400','228','scrollbars=yes,toolbar=no,status=no,resizable=no');
}
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>
