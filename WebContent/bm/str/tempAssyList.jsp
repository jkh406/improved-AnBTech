<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "Template BOM List"		
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
	String gid = "";
	String model_code = (String)request.getAttribute("model_code"); 
	
	//-----------------------------------
	//	등록중인 BOM 리스트
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("TEMP_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();
	int temp_cnt = table_list.size();

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//공정템플릿 BOM구성 공정Assy로 등록하기
function regTemplate()
{
	document.eForm.action='../servlet/BomTemplateServlet';
	document.eForm.mode.value='temp_assywrite';
	document.eForm.submit();	
}

//공정템플릿 BOM구성 삭제하기
function delTemplate() 
{
	var temp_cnt = '<%=temp_cnt%>';
	if(temp_cnt == 0) {
		alert('삭제할 내용이 없습니다.'); return;
	}

	var a = confirm('정말로 삭제하시겠습니까?');
	if(a == false) { return; }
	
	document.eForm.action='../servlet/BomTemplateServlet';
	document.eForm.mode.value='temp_delete';
	document.eForm.submit();
}
-->
</script>
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<FORM name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>	
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25><!-- 타이틀 -->
			<TD vAlign="center" valign='middle' class="bg_05" style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'>공정TEMPLATE 등록 LIST</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=25><!-- 버튼 및 검색-->
			<TD vAlign="center" style="padding-left:5px" bgcolor=''>
				<a href='Javascript:regTemplate();'><img src='../bm/images/bt_reg.gif' border='0' align='absmiddle'></a><a href='Javascript:delTemplate();'><img src='../bm/images/bt_del.gif' border='0' align='absmiddle'></a></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<!--리스트-->
		<TR height=100%>
			<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD noWrap width=30 align=middle class='list_title'>No</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>LEVEL</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>모품목코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>자품목코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=400 align=middle class='list_title'>품목설명</TD></TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=10></TD></TR>
	<%  
		int cnt = 0;
		while(table_iter.hasNext()) {
			table = (mbomStrTable)table_iter.next();
			gid = table.getGid();
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
						<TD align=left class='list_bg'><%=table.getPartSpec()%></TD></TR>
					</TR>
					<TR><TD colSpan=10 background="../bm/images/dot_line.gif"></TD></TR>
	<% 			cnt++;
				}  //while 	
%>
			</TBODY></TABLE></DIV></TD></TR>
			</TBODY></TABLE>
</TD></TR></TABLE>
		<FORM name="eForm" method="post" style="margin:0" encType="multipart/form-data">
		<INPUT type="hidden" name="mode" value=''>
		<INPUT type="hidden" name="pid" value=''>
		<INPUT type="hidden" name="gid" value='<%=gid%>'>
		<INPUT type="hidden" name="flag" value=''>
		<INPUT type="hidden" name="model_code" value='<%=model_code%>'>
		</FORM>

</BODY>
</HTML>

<SCRIPT language='javascript'>
<!--
// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height;
	
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 672;
	var div_h = c_h - 57;
	item_list.style.height = div_h;
}
-->
</SCRIPT>