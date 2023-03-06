<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "역전개 결과 LIST"		
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
	String part_code = (String)request.getAttribute("child_code");
	String part_spec = (String)request.getAttribute("part_spec");

	String part_code_empty = part_code;
	String part_spec_empty = part_spec;
	
	int spec_length = 0;
	int code_length = 0;
	if(part_code.equals("") || part_code==null) part_code_empty = "";
	if(part_spec.equals("") || part_spec==null) part_spec_empty = "";
	spec_length = part_spec_empty.length()+10;
	code_length = part_code_empty.length(); 

	//-----------------------------------
	//	등록중인 BOM 리스트
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PART_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();
	int pcnt = table_list.size()+1;		//+1은 붙이기에서 전달받은 변수를 읽기위해

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display()'>
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR valign='top'><TD>	
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height="25"><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign="center"  class="bg_05" BACKGROUND='../bm/images/title_bm_bg.gif' style='padding-left:5px;'>역전개 BOM LIST</TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height="23px"><TD vAlign="middle" align='left' style='padding-left:5px;'  bgcolor=''>
	<!--<SELECT name='' disabled><OPTION value='' >품목코드|DESCRIPTION</OPTION></SELECT>-->
	<IMG src='../bm/images/sub_item_code.gif' align='absmiddle' border='0'>
	<INPUT type='text' name='' value='<%=part_code_empty%>' size='<%=code_length%>'  style='font-size:9pt;color="black";' readonly>
	&nbsp;
	<IMG src='../bm/images/sub_description.gif' align='absmiddle' border='0'>
	<INPUT type='text' name='' value='<%=part_spec_empty%>' size='<%=spec_length%>' readonly></font>
	</TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>

	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
		<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=30 align=middle class='list_title'>No</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>LEVEL</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>자품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>모품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=400 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>LOC NO</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>공정코드</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>

	<%  int cnt = 1;
		while(table_iter.hasNext()) {
			table = (mbomStrTable)table_iter.next();
			int lv = Integer.parseInt(table.getLevelNo());
			String space="&nbsp;";
			for(int i=0; i<lv; i++) space += "&nbsp;&nbsp;";

			String pcd = table.getParentCode();
			if(pcd.equals("0")) pcd = "<font color=blue><b>모델코드</b></font>";
	%>	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=cnt%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left height="24"><%=space%><%=table.getLevelNo()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=pcd%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getChildCode()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getPartSpec()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getLocation()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getOpCode()%></TD>
			</TR>
			<TR><TD colSpan=19 background="../bm/images/dot_line.gif"></TD></TR>
	<% 
		cnt++;
		}  //while 

	%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>

<!-- 해당 PL 읽기 [from 복사]-->
<FORM name="sForm" method="post" style="margin:0">
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="child_code" value=''>
<INPUT type="hidden" name="sel_date" value=''>
</FORM>

<DIV id="lding" style="position:absolute;left:200px;top:130px;width:250px;height:100px;visibility:hidden;">
	<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

<SCRIPT language=javascript>
<!--
var part_code = '<%=part_code%>';
if(part_code.length != 0) parent.search.document.all['saving'].style.visibility="hidden";	//메뉴버튼 enable

function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 400 ;
	var div_h = c_h - 54;
	item_list.style.height = div_h;
}
-->
</SCRIPT>

</BODY>
</HTML>

