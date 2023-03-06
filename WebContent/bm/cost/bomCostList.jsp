<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "BOM 원가산출 LIST"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.bm.entity.primeCostTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");		//출력형태

	//----------------------------------------------------
	//	BOM MASTER정보
	//----------------------------------------------------
	String model_code="",fg_code="",gid=""; 

	com.anbtech.bm.entity.mbomMasterTable masterT;
	masterT = (mbomMasterTable)request.getAttribute("MASTER_List");
	model_code = masterT.getModelCode();
	fg_code = masterT.getFgCode();

	String model_code_empty = model_code;
	String fg_code_empty = fg_code;

	if(model_code==null) model_code_empty ="";
	if(fg_code==null) fg_code_empty ="";

	//-----------------------------------
	//	BOM 원가산출 LIST
	//-----------------------------------
	float std_total=0,ave_total=0,cur_total=0; 
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PART_List");
	table = new primeCostTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display()'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY><!-- 타이틀 및 페이지 정보 -->
		<!--<TR height=25>
			<TD vAlign="center" align='left' class="bg_05"  background='../bm/images/title_bm_bg.gif' style='padding-left:5px;'>BOM 원가산출 LIST</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>-->
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height="23px"><TD vAlign="center" align='left' style='padding-left:5px;'  bgcolor=''>
		<% if (!model_code_empty.equals("")) {%>
			<IMG src='../bm/images/sub_model_code.gif' align='absmiddle' border='0'>&nbsp;<font color='#639DE9'><%=model_code_empty%></font>
			&nbsp;
			<IMG src='../bm/images/sub_fg_code.gif' align='absmiddle' border='0'>&nbsp;<font color='#639DE9'><%=fg_code_empty%></font>
		<%}%>
		</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<!--리스트-->
		<TR height=100%>
			<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=25>
				  <TD noWrap width=90 align=middle class='list_title'>품목코드</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				  <TD noWrap width=140 align=middle class='list_title'>품목명</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				  <TD noWrap width=100% align=middle class='list_title'>품목설명</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				  <TD noWrap width=40 align=middle class='list_title'>수량</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				  <TD noWrap width=60 align=middle class='list_title'>평균단가</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				  <TD noWrap width=70 align=middle class='list_title'>평균총액</TD>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>

	<%  int cnt = 1;
		while(table_iter.hasNext()) {
			table = (primeCostTable)table_iter.next();
			//std_total += Double.parseDouble(table.getStdSum());
			ave_total += Double.parseDouble(table.getAveSum());
			//cur_total += Double.parseDouble(table.getCurSum()); 
	%>	
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=table.getItemCode()%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg'><%=table.getItemName()%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg'><%=table.getItemDesc()%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCount()%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'><%=nfm.StringToString(table.getAvePrice())%>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'><%=nfm.StringToString(table.getAveSum())%>&nbsp;</TD>
				</TR>
				<TR><TD colSpan=19 background="../bm/images/dot_line.gif"></TD></TR>
	<% 
		cnt++;
		}  //while 

	%>
	<%  if(cnt>1) {	%>
				<TR bgColor=#F5F5F5>
				  <TD align=middle height="24" class='list_bg'>총 합계</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left height="24">&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg'>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'>&nbsp;</TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg'><%=nfm.DoubleToString(ave_total)%>&nbsp;</TD>
				</TR>	
				<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
	<%}%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

<!-- 원가산출실행-->
<FORM name="sForm" method="post" style="margin:0">
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="gid" value=''>
<INPUT type="hidden" name="level_no" value=''>
<INPUT type="hidden" name="parent_code" value=''>
<INPUT type="hidden" name="sel_date" value=''>
<INPUT type="hidden" name="step" value=''>
<INPUT type="hidden" name="model_code" value='<%=model_code%>'>
</FORM>

<DIV id="lding" style="position:absolute;left:300px;top:130px;width:250px;height:100px;visibility:hidden;">
	<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

<SCRIPT language=javascript>
<!--
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 373 ;
	var div_h = c_h - 26;
	item_list.style.height = div_h;
}
-->
</SCRIPT>


</BODY>
</HTML>

