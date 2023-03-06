<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "등록중인 BOM TEXT LIST"		
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
	
	//----------------------------------------------------
	//	BOM MASTER정보
	//----------------------------------------------------
	String model_code="",fg_code="",gid="",bom_status="",model_code_temp="",fg_code_temp=""; 

	com.anbtech.bm.entity.mbomMasterTable masterT;
	masterT = (mbomMasterTable)request.getAttribute("MASTER_List");
	model_code = masterT.getModelCode();
	fg_code = masterT.getFgCode();
	gid = masterT.getPid();
	bom_status = masterT.getBomStatus();

	model_code_temp = model_code;
	fg_code_temp = fg_code;

	if(model_code==null || model_code.equals("")) model_code_temp = "";
	if(fg_code==null || fg_code.equals("")) fg_code_temp = "";

	//-----------------------------------
	//	등록중인 BOM 리스트
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("STR_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'  onload='display()'  oncontextmenu="return false">

<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>
	<TABLE height="100%" cellSpacing=0 cellPadding=1 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25><TD vAlign='center' style='padding-left:5px' class='bg_05' background='../bm/images/title_bm_bg.gif'>PART LIST</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height='25'>
			<TD vAlign=top align=left style="padding-left:5px"  bgcolor=''>
				<IMG src='../bm/images/sub_model_code.gif' align='absmiddle' border='0'>
				<INPUT type='text' name='' value='<%=model_code_temp%>' size='11' readonly>
				&nbsp;
				<IMG src='../bm/images/sub_fg_code.gif' align='absmiddle' border='0'>
				<INPUT type='text' name='' value='<%=fg_code_temp%>' size='11' readonly></TD></TR>
		<TR height=100%>
			<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">	
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=1><TD colspan=10></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=30 align=middle class='list_title'>No</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>LEVEL</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>모품목코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>자품목코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=400 align=middle class='list_title'>품목설명</TD>
						<TD noWrap width=6 class='list_title'></TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=10></TD></TR>

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
						<TD align=left class='list_bg'>&nbsp;<%=table.getParentCode()%></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'>&nbsp;<%=table.getChildCode()%></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'>&nbsp;<%=table.getPartSpec()%></TD></TR>
					<TR><TD colSpan=10 background="../bm/images/dot_line.gif"></TD></TR>
	<% 
		cnt++;
		}  //while 

	%>
			</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>
<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data">
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value=''>
<INPUT type="hidden" name="gid" value=''>
<INPUT type="hidden" name="model_code" value='<%=model_code%>'>
</FORM>
</BODY>
</HTML>

<script language="javascript">
<!--
var model_code = document.sForm.model_code.value;
if(model_code != 'null') parent.search.document.sForm.permit.value='Y';
// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 528 ;
	var div_h = c_h - 56;
	item_list.style.height = div_h;
}
//Template 및 해당레벨 정보
function strView(pid,gid,model_code) 
{
	var bom_status = '<%=bom_status%>';
	if(bom_status == '2') {
		parent.reg.document.eForm.action='../servlet/BomTemplateServlet';
		parent.reg.document.eForm.mode.value='temp_preassy';
		parent.reg.document.eForm.pid.value=pid;
		parent.reg.document.eForm.gid.value=gid;
		parent.reg.document.eForm.flag.value='2';
		parent.reg.document.eForm.model_code.value=model_code;
		parent.reg.document.eForm.submit();
	} else {
		parent.reg.document.eForm.action='../servlet/BomTemplateServlet';
		parent.reg.document.eForm.mode.value='temp_prewrite';
		parent.reg.document.eForm.pid.value=pid;
		parent.reg.document.eForm.gid.value=gid;
		parent.reg.document.eForm.flag.value='2';
		parent.reg.document.eForm.model_code.value=model_code;
		parent.reg.document.eForm.submit();
	}
}
-->
</script>
