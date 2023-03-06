<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "정전개 Report출력"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.bm.entity.mbomStrTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("0.0");		//출력형태

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String model_code = (String)request.getAttribute("model_code");
	String fg_code = (String)request.getAttribute("fg_code");
	
	//-----------------------------------
	//	정전개 PART 리스트
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD><title>리포트</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display();' oncontextmenu="return false">
<!-- 로고,제목,버튼 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../bm/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">BOM 보고서</TD>
	<TD width='30%' align="right" valign="bottom">
	<DIV id="print" style="position:relative;visibility:visible;">
			<!--<a href='Javascript:winprint();'><img src='../bm/images/bt_print.gif' align='absmiddle' border='0'></a> --><!-- 출력 -->
			<a href='Javascript:self.close();'><img src='../bm/images/bt_close.gif' align='absmiddle' border='0'></a> <!-- 닫기 -->
	</DIV></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width="25%" height="25" class="bg_03">모델코드 : <%=model_code%></td>
			    <TD align=left width="65%" height="25" class="bg_03">FG코드 : <%=fg_code%></td>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">
		<TABLE cellSpacing=0 cellPadding=0 width="98%" bordercolordark="white" bordercolorlight="#9CA9BA" align='center'>
			<TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=30 align=middle class='list_title'>LEVEL</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>모품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>자품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=350 align=middle class='list_title'>품목규격</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=300 align=middle class='list_title'>Location No</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>갯수</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
	<%  
		while(table_iter.hasNext()) {
			table = (mbomStrTable)table_iter.next();
			int lv = Integer.parseInt(table.getLevelNo());
			String space="&nbsp;";
			for(int i=1; i<lv; i++) space += "&nbsp;";
	%>	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=left height="24"><%=space%><%=table.getLevelNo()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getParentCode()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getChildCode()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getPartSpec()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getLocation()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getQtyUnit()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getQty()%></TD>
			</TR>
			<TR><TD colSpan=19 background="../bm/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

</BODY>
</HTML>

<SCRIPT language='javascript'>
// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var div_h = h - 273 ;
	
	item_list.style.height = div_h;

}
-->
</SCRIPT>

