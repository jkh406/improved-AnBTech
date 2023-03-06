<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "붙이기한 BOM TREE LIST"		
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
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";

	//----------------------------------------------------
	// MBOM MASTER정보 읽기
	//----------------------------------------------------
	String model_code_temp = "", fg_code_temp = "";
	com.anbtech.bm.entity.mbomMasterTable masterT;
	masterT = (mbomMasterTable)request.getAttribute("MASTER_List");
	String pdg_code = masterT.getPdgCode();
	String model_code = masterT.getModelCode();
	String fg_code = masterT.getFgCode();

	model_code_temp = model_code;
	fg_code_temp = fg_code;

	if(model_code==null || model_code.equals("")) model_code_temp = "";
	if(fg_code==null || fg_code.equals("")) fg_code_temp = "";

	//-----------------------------------
	//	등록중인 BOM 리스트
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PART_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<SCRIPT language=javaSCRIPT>
<!--
//message
var msg = '<%=msg%>';
if(msg.length > 0) {
	alert(msg);
}
-->
</SCRIPT>
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'  onload='display()' oncontextmenu="return false">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>	
	<TABLE height="100%" cellSpacing=0 cellPadding=1 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign="center" class="bg_05" style='padding-left;5px;' background='../bm/images/title_bm_bg.gif'>복사한 PART LIST</TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height="23px"><TD vAlign="center" align='left' style='padding-left:5px;'  bgcolor=''>
		<IMG src='../bm/images/sub_model_code.gif' align='absmiddle' border='0'>
				<INPUT type='text' name='' value='<%=model_code_temp%>' size='11' readonly>
				&nbsp;
				<IMG src='../bm/images/sub_fg_code.gif' align='absmiddle' border='0'>
				<INPUT type='text' name='' value='<%=fg_code_temp%>' size='11' readonly></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
		<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=25>
				<TD noWrap width=30 align=middle class='list_title'>번호</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>LEVEL</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				<TD noWrap width=90 align=middle class='list_title'>모품목코드</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>자품목코드</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				<TD noWrap width=400 align=middle class='list_title'>품목설명</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				<TD noWrap width=60 align=middle class='list_title'>LOC NO</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
				<TD noWrap width=60 align=middle class='list_title'>공정코드</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>

	<%  int cnt = 0;
		while(table_iter.hasNext()) {
			table = (mbomStrTable)table_iter.next();
			int lv = Integer.parseInt(table.getLevelNo());
			String space="&nbsp;";
			for(int i=0; i<lv; i++) space += "&nbsp;&nbsp;";
	%>	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=cnt%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left height="24"><%=space%><%=table.getLevelNo()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getParentCode()%></TD>
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
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>
<!-- 해당 PL 읽기 [from 복사]-->
<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data">
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value=''>
<INPUT type="hidden" name="gid" value=''>
<INPUT type="hidden" name="level_no" value=''>
<INPUT type="hidden" name="parent_code" value=''>
<INPUT type="hidden" name="status" value='PASTE'>
<INPUT type="hidden" name="model_code" value='<%=model_code%>'>
</FORM>

<DIV id="lding" style="position:absolute;left:300px;top:50px;width:250px;height:100px;visibility:hidden;">
		<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

<SCRIPT language=javaSCRIPT>
<!--
var model_code = document.sForm.model_code.value;
if(model_code != 'null') {
	parent.copy.document.all['saving'].style.visibility="hidden";	//메뉴버튼 enable [복사]
	parent.paste.document.all['saving'].style.visibility="hidden";	//메뉴버튼 enable [붙이기]
}

// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 528 ;	
	var div_h = c_h - 57;
	item_list.style.height = div_h;
}
-->
</SCRIPT>

</BODY>
</HTML>

