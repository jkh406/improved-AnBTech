<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "BOM 품목변경[신규/변경/삭제]"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//----------------------------------------------------
	//	parameter 선언
	//----------------------------------------------------
	String pid="",chg_id="",chg_name="";
	
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	String todate = anbdt.getDateNoformat();				//오늘날자

	//----------------------------------------------------
	// 	접속자 login 알아보기
	//----------------------------------------------------
	chg_id = sl.id; 		//접속자 login id
	
	String[] idColumn = {"a.id","a.name","b.code","b.ac_code","b.ac_name","a.office_tel"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	String item_data = "where (a.id ='"+chg_id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	if(bean.isAll()) {
		chg_name = bean.getData("name");			//접속자 명
	} //while
	
	//----------------------------------------------------
	//	파라메터 읽기
	//----------------------------------------------------
	String fg_code = (String)request.getAttribute("fg_code");			//FG code
	String order_date = (String)request.getAttribute("order_date");		//적용일
	String ecc_reason = (String)request.getAttribute("ecc_reason");		//변경이유
	String eco_no = (String)request.getAttribute("eco_no");				//설게변경 번호
	String gid = (String)request.getAttribute("gid");					//관리코드

	//----------------------------------------------------
	//	선택한 해당품목 정보 읽기
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomStrTable item_info;
	item_info = (mbomStrTable)request.getAttribute("ITEM_Info");
	String part_type = item_info.getPartType();	//A:Assy로 하부구조 있음, P:부품으로 하부구조없음
	String cpid = item_info.getPid();			//현재등록된 대상FG의 관리번호 

	//----------------------------------------------------
	//	설계변경 선택항목[F01:변경이유] 데이터 읽기
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable ecr;
	ArrayList ecr_list = new ArrayList();
	ecr_list = (ArrayList)request.getAttribute("ECR_List");
	ecr = new mbomEnvTable();
	Iterator ecr_iter = ecr_list.iterator();
	
%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<form name="eForm" method="post" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TBODY>
			<TR height='25'><TD vAlign=center bgcolor='#DBE7FD' class='bg_05' style="padding-left:5px"  BACKGROUND='../bm/images/title_bm_bg.gif'> BOM 변경작업</TD></TR>
			</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
			<TR height=25>
				<TD align=left width='80%' style="padding-left:5px">

						<% if((cpid.length() != 0) && (part_type.equals("P"))){			//부품일때%>
							<a href="javascript:chgItem();"><img src='../bm/images/bt_item_chg.gif' alt='품목변경' align='absmiddle' border='0'></a>
							<a href="javascript:delItem();"><img src="../dcm/images/bt_del.gif" border=0 align='absmiddle'></a>
							<a href="javascript:newAddItem();"><img src="../dcm/images/bt_add_new2.gif" border=0 align='absmiddle'></a>
						<% } else if((cpid.length() != 0) && (part_type.equals("A"))){	//ASSY일때%>
							<a href="javascript:chgItem();"><img src='../bm/images/bt_item_chg.gif' alt='품목변경' align='absmiddle' border='0' align='absmiddle'></a>
							<a href="javascript:newAddItem();"><img src="../dcm/images/bt_add_new2.gif" align='absmiddle' border=0 align='absmiddle'></a>
						<% } else { %>
							<a href="javascript:addItem();"><img src="../dcm/images/bt_add.gif" align='absmiddle' border=0></a>
						<% } %>
							<a href="javascript:searchItems();"><img src='../bm/images/bt_item_search.gif' alt='부품검색' align='absmiddle' border='0'></a>
							<a href="javascript:searchOPInfo();"><img src='../bm/images/bt_work_search.gif' alt='공정검색' align='absmiddle' border='0'></a>
							</TD></TR>
				
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
			<!--리스트-->
			<TR height=100%>
				<TD vAlign=top>
				<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
					<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
						<TBODY>
							<tr>
							   <td noWrap width='40' height="25"  class='list_title'></td>
							   <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							   <td noWrap width='120' height="25" class='list_title' align='middle'>모품목코드</td>
							   <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							   <td noWrap width='120' height="25" class='list_title' align='middle'>자품목코드</td>
							   <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							   <td noWrap width='400' height="25" class='list_title' align='middle'>품목규격</td>
							   <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							   <td noWrap width='60' height="25" class='list_title' align='middle'>LOC</td>
							   <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							   <td noWrap width='60' height="25" class='list_title' align='middle'>OPC</td>
							   <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							   <td noWrap width='200' height="25" class='list_title' align='middle'>변경이유</td>
							   <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							   <td noWrap width='200' height="25" class='list_title' align='middle'>기타의견</td>
							<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
							<tr>
							   <td width='40' height="25" class='list_title' align='middle'>전</td>
							   <TD noWrap width=1 class='list_title'></TD>
							   <td width="120" height="25" class="bg_04"  	align='middle'><%=item_info.getParentCode()%></td>
							   <TD><IMG height=1 width=1></TD>
							   <td width="120" height="25" class="bg_04" align='middle'><%=item_info.getChildCode()%></td>
							   <TD><IMG height=1 width=1></TD>
							   <td width="400" height="25" class="bg_04" 	align='middle'><%=item_info.getPartSpec()%></td>
							   <TD><IMG height=1 width=1></TD>
							   <td width="60" height="25" class="bg_04" align='middle'><%=item_info.getLocation()%></td>
							   <TD><IMG height=1 width=1></TD>
							   <td width="60" height="25" class="bg_04" align='middle'><%=item_info.getOpCode()%></td>
							   <TD><IMG height=1 width=1></TD>
							   <td width="200" height="25" class="bg_04" align='middle'></td>
							   <TD><IMG height=1 width=1></TD>
								<td width="200" height="25" class="bg_04" align='middle'></td>
							</tr>
							<tr bgcolor="c7c7c7"><td height=1 colspan="15"></td></tr>
							<tr>
								<td width='40' height="25" class='list_title' align='middle'>후</td>
								<TD noWrap width=1 class='list_title'></TD>
								<td width="120" height="25" class="bg_04" align='middle'>
						   <% if(cpid.length() != 0) { 
								out.print(item_info.getParentCode());
								out.println("<INPUT type='hidden' name='parent_code' value='"+item_info.getParentCode()+"'>");
							} else { 
								out.println("<INPUT type='text' name='parent_code' value='' size='10' class='text_01'>");
							}
						   %></td>
								<TD><IMG height=1 width=1></TD>
								<td width="120" height="25" class="bg_04" align='middle'>
									<INPUT type='text' name='child_code' value='' size='10' class="text_01" readonly></td>
								<TD><IMG height=1 width=1></TD>
								<td width="400" height="25" class="bg_04" align='middle'>
									<INPUT type='text' name='part_spec' value='' size='30' readonly></td>
								<TD><IMG height=1 width=1></TD>
								<td width="60" height="25" class="bg_04" align='middle'>
									<INPUT type='text' name='location' value='' size='5'></td>
								<TD><IMG height=1 width=1></TD>
								<td width="60" height="25" class="bg_04" align='middle'>
									<INPUT type='text' name='op_code' value='<%=item_info.getOpCode()%>' size='5' readonly></td>
								<TD><IMG height=1 width=1></TD>
								<td width="200" height="25" class="bg_04" align='middle'>
									<select name="ecc_reason"> 
									<OPTGROUP label='---------------'>
								<%
									String rsel="";
									while(ecr_iter.hasNext()) {
										ecr = (mbomEnvTable)ecr_iter.next(); 
										if(ecc_reason.equals(ecr.getSpec())) rsel = "selected";
										else rsel = "";
										out.print("<option "+rsel+" value='"+ecr.getSpec()+"'>");
										out.println(ecr.getSpec()+"</option>");
									} 
								%></select></td>
								<TD><IMG height=1 width=1></TD>
								 <td width="200" height="25" class="bg_04" align='middle'>
						 	<INPUT type='text' size='20'></td>
											</tr> 
						<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
		</TBODY></TABLE></DIV></TD></TR>
	</TBODY>
</TABLE>
</TD></TR></TABLE>

<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='cpid' value='<%=item_info.getPid()%>'>		<% //등록된 PID %>
<INPUT type='hidden' name='pid' value='<%=anbdt.getID()%>'>				<% //등록할 PID %>
<INPUT type='hidden' name='gid' value='<%=gid%>'>
<INPUT type='hidden' name='eco_no' value='<%=eco_no%>'>
<INPUT type='hidden' name='order_date' value='<%=order_date%>'>
<INPUT type='hidden' name='fg_code' value='<%=fg_code%>'>
<INPUT type='hidden' name='adtag' value=''>
<INPUT type='hidden' name='chg_id' value='<%=chg_id%>'>
<INPUT type='hidden' name='chg_name' value='<%=chg_name%>'>
<INPUT type='hidden' name='op_name' value=''>
</form>

</body>
</html>


<script language=javascript>
<!--
//신규 추가하기
function addItem()
{
	var f = document.eForm;

	var parent_code = f.parent_code.value;
	if(parent_code == '') { alert('신규 모품목코드가 입력되지 않았습니다.'); f.parent_code.focus(); return; }

	var child_code = f.child_code.value;
	if(child_code == '') { alert('신규 자품목코드가 입력되지 않았습니다.'); f.child_code.focus(); return; }

	var ecc_reason = f.ecc_reason.value;
	if(ecc_reason == '') { alert('변경이유가 입력되지 않았습니다.'); f.ecc_reason.focus(); return; }

	document.eForm.action='../servlet/CbomChangeServlet';
	document.eForm.mode.value='eco_add';
	document.eForm.adtag.value='A';
	document.eForm.submit();
}
//변경하기
function chgItem()
{
	var f = document.eForm;

	var parent_code = f.parent_code.value;
	if(parent_code == '') { alert('변경 모품목코드가 입력되지 않았습니다.'); f.parent_code.focus(); return; }

	var child_code = f.child_code.value;
	if(child_code == '') { alert('변경 자품목코드가 입력되지 않았습니다.'); f.child_code.focus(); return; }

	var ecc_reason = f.ecc_reason.value;
	if(ecc_reason == '') { alert('변경사유가 입력되지 않았습니다.'); f.ecc_reason.focus(); return; }

	document.eForm.action='../servlet/CbomChangeServlet';
	document.eForm.mode.value='eco_change';
	document.eForm.adtag.value='R';
	document.eForm.submit();
}
//삭제하기
function delItem()
{
	document.eForm.action='../servlet/CbomChangeServlet';
	document.eForm.mode.value='eco_delete';
	document.eForm.adtag.value='D';
	document.eForm.submit();
}
//발생부품 찾기
function searchItems()
{
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=child_code&item_name=part_name&item_type=item_type&item_desc=part_spec&item_unit=qty_unit";
	wopen(strUrl,"open_part",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');

}
//공정정보 가져오기
function searchOPInfo() {
	wopen("../dcm/searchOPcode.jsp?target=eForm.op_code/eForm.op_name","opcode","250","350","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//신규등록 메뉴로 돌아가기
function newAddItem()
{
	document.eForm.action='../servlet/CbomChangeServlet';
	document.eForm.mode.value='eco_prechg';
	document.eForm.submit();
}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 
	var w = window.screen.width; 
    var h = window.screen.height; 
	//var div_w = w - 430 ; 
	var div_h = h - 700;
	item_list.style.height = div_h;
}
-->
</script>