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
	String model_code="",fg_code="",gid="", model_code_temp ="",fg_code_temp = ""; 

	com.anbtech.bm.entity.mbomMasterTable masterT;
	masterT = (mbomMasterTable)request.getAttribute("MASTER_List");
	model_code = masterT.getModelCode();
	fg_code = masterT.getFgCode();
	gid = masterT.getPid();
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
	int w = 0;
	int h = 0;
%>

<HTML>
<HEAD><Title>BOM LIST</Title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display()' oncontextmenu="return false">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>
	<TABLE height="100%" cellSpacing=0 cellPadding=1 width="100%" border=0 valign="top">
	<TBODY>
		<TR height='25'><TD  vAlign='center' style="padding-left:5px"  class='bg_05' BACKGROUND='../bm/images/title_bm_bg.gif'>PART LIST</TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD></TD></TR>
		<TR height='25'><TD vAlign=top align=left width='80%' style="padding-left:5px" bgcolor=''>
			
				<IMG src='../bm/images/sub_model_code.gif' align='absmiddle' border='0'>
				<INPUT type='text' name='' value='<%=model_code_temp%>' size='11' readonly>
				&nbsp;
				<IMG src='../bm/images/sub_fg_code.gif' align='absmiddle' border='0'>
				<INPUT type='text' name='' value='<%=fg_code_temp%>' size='11' readonly>
				&nbsp;&nbsp;
					<a href='javascript:checkBOM()'><IMG src='../bm/images/bt_bm_chk.gif' align="absmiddle" alt='BOM검사' border='0'></a>

					<a href='javascript:reviewBOM()'><IMG src='../bm/images/bt_bm_refresh.gif' align="absmiddle" alt='BOM갱신' border='0'></a></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA' colspan=14></TD></TR>
		<TR>
			<TD vAlign=top><DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					
					<TR vAlign=middle height=23>
						<TD noWrap width=30 align=middle class='list_title'>No</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>LEVEL</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>모품목코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>자품목코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>LOC NO</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>공정코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=400 align=middle class='list_title'>품목설명</TD>
						<TD noWrap width=6 class='list_title'></TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=14></TD></TR>

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
					<TD align=middle class='list_bg'><%=table.getLocation()%></TD>
					<TD><IMG height=1 width=1></TD>
					<TD align=middle class='list_bg'><%=table.getOpCode()%></TD>
					<TD><IMG height=1 width=1></TD>
					<TD align=left class='list_bg'><%=table.getPartSpec()%></TD></TR>
				<TR><TD colSpan=14 background="../bm/images/dot_line.gif"></TD></TR>
	<% 
		cnt++;
		}  //while 

	%>
		</TBODY></TABLE></DIV></TD></TR>
</TBODY></TABLE>
</TD></TR></TABLE>

<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data">
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value=''>
<INPUT type="hidden" name="gid" value='<%=gid%>'>
<INPUT type="hidden" name="model_code" value='<%=model_code%>'>
</FORM>

<DIV id="lding" style="position:absolute;left:150px;top:70px;width:214px;height:200px;visibility:hidden;">
	<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>
<!--
<DIV id="saving" style="position:absolute;left:0px;top:0px;width:250px;height:80px;visibility:hidden;">
<TABLE width="800" border="" cellspacing=1 cellpadding=1 bgcolor="">
	<TR><td height="400" align="center" valign="middle"></TD></TR></TABLE></DIV>
-->

</BODY>
</HTML>

<script language=javascript>
<!--
//다시보기
function reviewBOM() 
{   
	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
	document.sForm.action='../servlet/BomInputServlet';
	document.sForm.mode.value='pl_list';
	document.sForm.submit();
}
//부품 수정 / 삭제하기
function strView(pid,gid) 
{	
	parent.reg.document.eForm.action='../servlet/BomInputServlet';
	parent.reg.document.eForm.mode.value='pl_prewrite';
	parent.reg.document.eForm.pid.value=pid;
	parent.reg.document.eForm.gid.value=gid;
	parent.reg.document.eForm.submit();
}
//BOM등록 검사하기
function checkBOM()
{
	//메시지창 띄우기
	para = "msg=BOM 검사중... 잠시만 기다려 주십시오.&width=500&heigth=300";
	wopen("../bm/msgWindow.jsp?"+para,"proxy","550","260","scrollbars=no,toolbar=no,status=no,resizable=no");

	//검사진행하기
	var gid = document.sForm.gid.value;	wopen("../servlet/BomInputServlet?mode=chk_list&gid="+gid,"proxy","550","260","scrollbars=no,toolbar=no,status=no,resizable=yes");

}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;
	
	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 
}

-->
</script>

<script language=javascript>
<!--
var model_code = document.sForm.model_code.value;
if(model_code != 'null') {
	parent.search.document.sForm.permit.value='Y';		//검색창 enable	
//	document.all['saving'].style.visibility="hidden";	//메뉴버튼 enable [자신]
//	document.body.style.overflow='';					//스크롤바 보이기 [자신]
}

// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 
	var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h -514 ;
	var div_h = c_h - 57;
	item_list.style.height = div_h;
}
-->
</script>



