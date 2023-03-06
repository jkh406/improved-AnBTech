<%@ include file="../admin/configPopUp.jsp"%>
<%@ page		
	info= "설계변경 해당부품 검색"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.bm.entity.mbomStrTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	
	//----------------------------------------------------
	//	파라미터 읽기
	//----------------------------------------------------
	String model_code = (String)request.getAttribute("model_code"); 
	String fg_code = (String)request.getAttribute("fg_code"); 	
	
	
	//-----------------------------------
	//	등록중인 BOM 리스트
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PART_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD><TITLE>부품 검색</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//부품 선택
function itemView(lno,pcd,ccd,loc,spec) 
{	
	//전달할 내용 조합
	var pl = pcd+'/'+ccd+'/'+loc;

	//전달할 곳의 현재 등록된 부품LIST
	var pc = opener.document.forms[0].part_code.value; 
	var list = pc.split('\n'); 
	var len = list.length;

	//동일한 내용이 있는지 검사하여 skip하기
	var scn = 0;
	for(i=0; i<len; i++) {
		var ld = list[i].substring(0,list[i].length-1); //리턴값제거
		if(pl == ld) scn++;
	}
	if(scn != 0) {
		//alert('이미 선택한 부품이 있습니다.');
		var c = confirm('이미 선택한 부품이 있습니다. 삭제하시겠습니까?');
		if(c == true ) {
			pc = "";
			for(i=0; i<len; i++) {
				var rdata = list[i].substring(0,list[i].length-1); //리턴값제거해서 전달받은값
				if(pl != rdata) pc += rdata+"\n";
			}
			pc = pc.substring(0,pc.length-1);
			opener.document.forms[0].part_code.value=pc;
			return;
		} else return;
	}

	//선택한 내용을 추가하여 전달하기
	pc += pl+'\n'; 
	if(pc.length > 300) { 
		alert('선택된 부품이 등록한도를 초과하여 등록할 수 없습니다.관리자에게 문의바랍니다.');
		return;
	}
	
	opener.document.forms[0].part_code.value=pc;
}

// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 
	var w = window.screen.width; 
    var h = window.screen.height; 

	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 475;
	var div_h = c_h - 112;
	item_list.style.height = div_h;
}
-->
</script>

<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display();' oncontextmenu="return false">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign=top>
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
						<TR>
							<TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../dcm/images/pop_item_search.gif" border='0' align='absmiddle' alt='BOM LIST'></TD></TR>
							<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY>
							<TD height="32" valign="middle" style='padding-left:5px;'> 
							<img src="../dcm/images/model_code.gif" border='0' align='absmiddle' alt='모델코드'> <INPUT type='text' name='model_code_view' size='15' maxlength='12' value='<%=model_code%>' readonly>
							<img src="../dcm/images/fg_code.gif" border='0' align='absmiddle' alt='fg_code'> <INPUT type='text' name='fg_code_view'  size='12' maxlength='15' value='<%=fg_code%>' readonly>
							</TD></TR>						
				</TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<!--리스트-->
		<TR height=100%>
		    <TD vAlign=top>
				<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
				<TABLE cellSpacing=0 cellPadding=0 border=0  align='center'>
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
						  <TD noWrap width=60 align=middle class='list_title'>LOC.</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						  <TD noWrap width=400 align=middle class='list_title'>품목규격</TD>
						</TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>

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
						  <TD align=middle class='list_bg'><%=table.getLocation()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg'>&nbsp;<%=table.getPartSpec()%></TD></TR>
						<TR><TD colSpan=11 background="../bm/images/dot_line.gif"></TD></TR>
	<% 
		cnt++;
		}  //while 

	%>
		</TBODY></TABLE></DIV></TD></TR>
		<TR><TD height=5 colspan="2"></TD></TR>
		<!--꼬릿말-->
		<TR><TD>
			<TABLE cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
					<TR><TD width='100%' height="33" align="right" bgcolor="#73AEEF" style='padding-right:4px'>
						<A href='javascript:self.close()'><img src='../dcm/images/bt_close.gif' border='0' align='absright'></a></TD></TR>
					<TR><TD height="" bgcolor="0C2C55" colspan='2'></TD></TR></TBODY></TABLE></TD></TR>		
</TBODY></TABLE>

<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data">
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value=''>
<INPUT type="hidden" name="gid" value=''>
</FORM>
</BODY>
</HTML>

