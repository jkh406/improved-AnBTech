<%@ include file="../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.ca.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	CaMasterTable master;
	CaLinkUrl	redirect;
%>

<%
	int ref_count		= 5;	// 최대 참조문서 개수 지정
	int enableupload	= 4;	// 업로드 개수 지정

	String mode				 = request.getParameter("mode");
	String no				 = request.getParameter("no");

	//ca_master 에서 가져오기
	master = new CaMasterTable();
	master = (CaMasterTable)request.getAttribute("CA_Info");
	
	String requestor_info	= master.getRequestorInfo();
	String request_date		= master.getRequestDate();
	String pjt_code			= master.getPrjCode()==null?"":master.getPrjCode();
	String pjt_name			= master.getPrjName()==null?"":master.getPrjName();
	String model_code		= master.getModelCode()==null?"":master.getModelCode();
	String model_name		= master.getModelName()==null?"":master.getModelName();
	String item_no			= master.getItemNo()==null?"":master.getItemNo();
	String item_name		= master.getItemName()==null?"":master.getItemName();
	String item_desc		= master.getItemDesc()==null?"":master.getItemDesc();
	String item_unit		= master.getItemUnit()==null?"":master.getItemUnit();
	String maker_code		= master.getMakerCode()==null?"":master.getMakerCode();
	String maker_name		= master.getMakerName()==null?"":master.getMakerName();
	String maker_part_no	= master.getMakerPartNo()==null?"":master.getMakerPartNo();
	String approve_type		= master.getApproveType()==null?"":master.getApproveType();
	String apply_date		= master.getApplyDate()==null?"":master.getApplyDate();
	String sdate			= apply_date.equals("")?"":apply_date.substring(0,10);
	String edate			= apply_date.equals("")?"":apply_date.substring(13,23);
	String apply_quantity	= master.getApplyQuantity()==null?"":master.getApplyQuantity();
	String why_approve		= master.getWhyApprove()==null?"":master.getWhyApprove();
	String other_info		= master.getOtherInfo()==null?"":master.getOtherInfo();
	String no_type			= master.getNoType()==null?"":master.getNoType();

	String title = "";
	if(mode.equals("write_s")){
		title = "신규품목 승인의뢰";
	}else if(mode.equals("write_m")){
		title = "다중승인 의뢰";
	}else if(mode.equals("write_r")){
		title = "사양변경 신청의뢰";
	}else if(mode.equals("write_a")){
		title = "승인업체 추가의뢰";
	}else if(mode.equals("modify")){
		title = "승인의뢰 정보수정";
	}
%>

<script language=JavaScript>
<!--
<%
	int i = 1;

	while(i < enableupload){
		if(i == enableupload-1){
%>

		function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%> size=50>"
		}
	<%
		 break;
		}
	%>
	function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=50><font id=id<%=i+1%>></font>"
	}
<%
	i++;
	}
%>
//-->
</script>

<%

	CaMasterTable file = new CaMasterTable();

	ArrayList file_list = new ArrayList();
	file_list = (ArrayList)request.getAttribute("File_List");
	Iterator file_iter = file_list.iterator();

	i = 1;

	String file_stat = "";

	while(file_iter.hasNext()){
		file = (CaMasterTable)file_iter.next();
		file_stat = file_stat + "<input type=file name='attachfile"+i+"' size=50>" + file.getFileName()+" 삭제! <input type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
		i++;
	}

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ca/css/style.css" type="text/css">
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ca/images/blet.gif"> 품목승인의뢰</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300><a href="javascript:checkForm();"><img src="../ca/images/bt_save.gif" border="0" align="absmiddle"></a> <a href="javascript:location.href='../servlet/ComponentApprovalServlet?mode=mylist';"><img src="../ca/images/bt_cancel.gif" border="0" align="absmiddle"></a></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>
<!--내용-->
<form method="post" name="writeForm" action='../servlet/ComponentApprovalServlet' enctype='multipart/form-data' style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">의뢰구분</td>
           <td width="37%" height="25" class="bg_04"><b><%=title%></b></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목종류</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="E" name="no_type" <% if(no_type.equals("E")) out.print("checked");%> checked>회로부품 &nbsp;&nbsp;<input type="radio" value="M" name="no_type" <% if(no_type.equals("M")) out.print("checked");%>>기구부품</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">의뢰자</td>
           <td width="37%" height="25" class="bg_04"><%=requestor_info%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">의뢰일자</td>
           <td width="37%" height="25" class="bg_04"><%=request_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">관련과제</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name='pjt_code' value='<%=pjt_code%>' size="15" class="text_01" readOnly> <input type='text' name='pjt_name' value='<%=pjt_name%>' size="10" class="text_01" readOnly> <a href='javascript:sel_pjt_code();'><img src='../ca/images/bt_search.gif' border='0' align='absmiddle'></a></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">관련모델</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name='model_code' value='<%=model_code%>' size='10' class="text_01" readOnly> <input type='text' name='model_name' value='<%=model_name%>' size="20" class="text_01" readOnly> <a href='javascript:m_search();'><img src='../ca/images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name='item_no' value='<%=item_no%>' size='10' class="text_01" readOnly> <% if(!mode.equals("write_a")){ %><a href='javascript:searchCMInfo();'><img src='../ca/images/bt_search.gif' border='0' align='absmiddle'></a><% } %></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name='item_name' value='<%=item_name%>' class="text_01" size='15' readOnly></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목설명</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type='text' name='item_desc' value='<%=item_desc%>' size='50' class="text_01" readOnly></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인업체</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name='maker_code' value='<%=maker_code%>' size="5" class="text_01" readOnly> <input type='text' name='maker_name' size="20" value='<%=maker_name%>' class="text_01" readOnly> <a href='javascript:searchCompany();'><img src='../ca/images/bt_search.gif' border='0' align='absmiddle'></a></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">업체부품번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name='maker_part_no' value='<%=maker_part_no%>' size='30' class='text_01'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인원첨부</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<% if (mode.equals("write_m")){	%>
					<input type='checkbox' name='is_file_same' value="same" onClick='chkAttachFile();'>이전과 동일<br>
				<%	}	%>
					<div id="attach_file" class="expanded">
					<table><tr><td width='100%'>
		            <%=file_stat%>
				<%		if(i < enableupload){	%>
				            <input type=file name=attachfile<%=i%> onClick='fileadd_action<%=i%>()' size="50"><font id=id<%=i%>></font>
				<%		}else if(i == enableupload){	%>
				            <input type=file name=attachfile<%=i%> size="50"><font id=id<%=i%>></font>
				<%		}	%>	
					</td></tr></table></div>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인구분</td>
           <td width="37%" height="25" class="bg_04">
					<select name='approve_type' onChange="javascript:selApproveType();">
						<option value='A'>합격</option>
						<option value='B'>조건부 승인</option>
					</select>		
					<script language='javascript'>
					<%	if(!approve_type.equals("")){	%>
							document.writeForm.approve_type.value = '<%=approve_type%>';
					<%	}	%>
					</script>		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">적용수량</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name='apply_quantity' value='<%=apply_quantity%>' size='3'> <input type='text' name='item_unit' value='<%=item_unit%>' size='5' readOnly> *조건부승인인 경우만 입력</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">적용일자</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type='text' size='10' name='sdate' value='<%=sdate%>' readOnly> <a href="Javascript:OpenCalendar('sdate');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a> ~ <input type='text' size='10' name='edate' value='<%=edate%>' readOnly> <a href="Javascript:OpenCalendar('edate');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a> *조건부승인인 경우만 입력</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>		 
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">판정사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" name="why_approve" cols="93"><%=why_approve%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">비고</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" name="other_info" cols="93"><%=other_info%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

  <input type='hidden' name='mode' value='<%=mode%>'>
  <input type='hidden' name='no' value='<%=no%>'>
  <input type='hidden' name='apply_date' value='<%=apply_date%>'>
</form>

<%
//멀티 등록 시 이전 정보를 보여준다.
if(mode.equals("write_m")){
	ArrayList approval_list = new ArrayList();
	approval_list = (ArrayList)request.getAttribute("Approval_List");
	Iterator approval_iter = approval_list.iterator();

	while(approval_iter.hasNext()){
		master = (CaMasterTable)approval_iter.next();
		item_no			= master.getItemNo();
		item_name		= master.getItemName();
		requestor_info	= master.getRequestorInfo();
		request_date	= master.getRequestDate();
		maker_code		= master.getMakerCode();
		maker_name		= master.getMakerName();
		maker_part_no	= master.getMakerPartNo();
		item_desc		= master.getItemDesc();
		pjt_code		= master.getPrjCode();
		pjt_name		= master.getPrjName();
		model_code		= master.getModelCode();
		model_name		= master.getModelName();
		item_unit		= master.getItemUnit();
		approve_type	= master.getApproveType();
		if(approve_type.equals("A")) approve_type = "합격";
		else if(approve_type.equals("B")) approve_type = "조건부승인";
		else if(approve_type.equals("F")) approve_type = "사용금지";
		apply_date		= master.getApplyDate()==null?"":master.getApplyDate();
		apply_quantity	= master.getApplyQuantity()==null?"":master.getApplyQuantity();
		why_approve		= master.getWhyApprove()==null?"":master.getWhyApprove();
		String file_link= master.getFileLink();
		other_info		= master.getOtherInfo()==null?"":master.getOtherInfo();
%>
	<table><tr><td height="5"><hr width="100%" size="1"></td></tr></table>
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">의뢰자</td>
           <td width="37%" height="25" class="bg_04"><%=requestor_info%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">의뢰일자</td>
           <td width="37%" height="25" class="bg_04"><%=request_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">관련과제</td>
           <td width="37%" height="25" class="bg_04">[<%=pjt_code%>] <%=pjt_name%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">관련모델</td>
           <td width="37%" height="25" class="bg_04">[<%=model_code%>] <%=model_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목번호</td>
           <td width="37%" height="25" class="bg_04"><%=item_no%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목명</td>
           <td width="37%" height="25" class="bg_04"><%=item_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목설명</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=item_desc%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>		 
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인업체</td>
           <td width="37%" height="25" class="bg_04">[<%=maker_code%>] <%=maker_name%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">업체부품번호</td>
           <td width="37%" height="25" class="bg_04"><%=maker_part_no%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">첨부문서</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=file_link%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인구분</td>
           <td width="37%" height="25" class="bg_04"><%=approve_type%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">적용수량</td>
           <td width="37%" height="25" class="bg_04"><%=apply_quantity%> <%=item_unit%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">적용일자</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=apply_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">판정사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=why_approve%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">비고</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=other_info%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
<%
	}
}
%>
</td></tr></table>
</body>
</html>

<script language='javascript'>
<!--

// 필수입력항목 체크
function checkForm(){ 
	var f = document.writeForm;

	if(f.pjt_code.value == ''){
		alert("관련과제를 찾아서 선택하십시오.");
		f.pjt_code.focus();
		return;
	}

	if(f.model_code.value == ''){
		alert("관련모델을 찾아서 선택하십시.");
		f.model_code.focus();
		return;
	}

	if(f.item_no.value == ''){
		alert("품목번호를 찾아서 선택하십시오.");
		f.item_no.focus();
		return;
	}

	if(f.maker_code.value == ''){
		alert("승인업체를 찾아서 선택하십시오.");
		f.maker_code.focus();
		return;
	}

	if(f.maker_part_no.value == ''){
		alert("업체부품번호를 입력하십시오.");
		f.maker_part_no.focus();
		return;
	}

	if(f.item_desc.value.length < 5){
		alert("품목에 대한 간략한 설명을 입력하십시오.");
		f.item_desc.focus();
		return;
	}

	if(f.why_approve.value.length < 3){
		alert("판정사유를 입력하십시오.");
		f.why_approve.focus();
		return;
	}

	if(f.approve_type.value == 'B' && f.apply_quantity.value == ''){
		alert("적용수량를 입력하십시오.");
		f.apply_quantity.focus();
		return;
	}

	if(f.approve_type.value == 'B' && (f.sdate.value == '' || f.edate.value == '')){
		alert("적용일자를 입력하십시오.");
		return;
	}

	if(f.approve_type.value == 'B')	f.apply_date.value = f.sdate.value + " ~ " + f.edate.value;

	document.onmousedown=dbclick;
	f.submit();

}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}

// 팝업창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 승인구분 변경 처리
function selApproveType(){
	var f = document.writeForm;

	if(f.approve_type.value == 'A'){
		f.sdate.value = '';
		f.edate.value = '';
		f.apply_date.value = '';
		f.apply_quantity.value = '';
	}
}

// 첨부파일
function chkAttachFile(){
	var f = document.writeForm;

	if(f.is_file_same.checked){
		hide('attach_file');
	}else{
		show('attach_file');
	}
}

// 선택된 레이어를 숨김
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
	  document.layers[menuname].visibility="hide";
  } else {
	  document.all[menuname].className="collapsed"
   }
}

// 선택된 레이이를 보여줌
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
	   document.layers[menuname].visibility="show";
  } else {
	   document.all[menuname].className="expanded"
  }
}

// 승인업체 선택
function searchCompany()
{
	wopen('../ca/searchMakerCode.jsp?opener_fname=writeForm&opener_code=maker_code&opener_name=maker_name','search_comp','550','300','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

// 과제찾기
function sel_pjt_code() {
	wopen('../servlet/PsmProcessServlet?mode=search_project&target=writeForm.pjt_code/writeForm.pjt_name','search_pjt','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 승인품목 찾기
function searchCMInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_no&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=item_unit";
	wopen(strUrl,"search_item",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//모델코드 검색하기
function m_search(){
	var url = "../gm/openModelInfoWindow.jsp?fname=writeForm&one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=blank&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	wopen(url,"SEARCH_MODEL",'800','400','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//-->
</script>