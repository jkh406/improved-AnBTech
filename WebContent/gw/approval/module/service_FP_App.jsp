<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info		= "고객관리 본문작성"		
	contentType = "text/html; charset=euc-kr" 		
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.file.textFileReader"
	errorPage	= "../../../admin/errorpage.jsp"
%>
<%
	//------------------------------------------------------------------
	//	초기화 & 파일 upload 디렉토리
	//------------------------------------------------------------------
	String uploadDir = upload_path + "/gw/approval/eleApproval/"+ login_id + "/addfile";	//multipart용
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	textFileReader text = new com.anbtech.file.textFileReader();			//ID값 구하기

	/*********************************************************************
	 	전자결재 메뉴가 아닌 기타정보 전자결재상신시 처리
		ex) flag가 SERVICE:고객서비스결재, BOM:부품정보결재 
	*********************************************************************/
	//정보의 종류 (SERVICE:고객서비스결재, BOM:부품정보결재)
	String doc_flag = request.getParameter("flag");				//flag종류
	if(doc_flag == null) doc_flag= "SERVICE";

	//정보의 관리번호
	String doc_lid = request.getParameter("plid");				//관련정보 관리번호(중복시 꼼마로 구분)
	if(doc_lid == null) doc_lid = "";
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 고객서비스이력</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<form action="" name="eForm" method="post" encType="multipart/form-data" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<br>재<br>선</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=36% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--제목-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">제목</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text name='doc_sub' size='40' maxlength="40" class="text_01"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=3 colspan="4"></td></tr></tbody></table>  

    <!--이력정보-->
	<table cellspacing=0 cellpadding=0 width="100%" height='100%' border=0>
	   <tbody>
		 <tr>
           <td width="100%" height="100%" valign="top">
				<%
						StringTokenizer strid = new StringTokenizer(doc_lid,",");
						if(doc_flag.equals("SERVICE")) {	
							int vcnt=1;
							while(strid.hasMoreTokens()) {
				%>
									<jsp:include page='service_viewHistory.jsp' flush='true'>
									<jsp:param name='ah_id' value='<%=strid.nextToken()%>' />
									<jsp:param name='vcnt' value='<%=vcnt%>' />
									</jsp:include>

				<%	
									vcnt++;
							} //while
						} //if
				%>
		   </td></tr></tbody></table>  

  </td></tr></table>

<input type='hidden' name='addFileDir' value='<%=uploadDir%>'>
<input type='hidden' name='login_id' value='<%=sl.id%>'>
<input type='hidden' name='login_name' value='<%=sl.name%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_id' value='<%=text.getID()%>'>
<input type='hidden' name='doc_flag' value='SERVICE'>
<input type='hidden' name='doc_lid' value='<%=doc_lid%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>

<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='CONTENT' value='고객관리문서'>
<input type='hidden' name='mode' value=''>
</form>

</body>
</html>


<script language=javascript>
<!--
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//결재선 지정지정 
function eleApprovalManagerLineSelect()
{
	wopen("../eleApproval_Share.jsp?target=eForm.doc_app_line","eleA_app_search_select","520","467");
}


//결재 상신 (일괄합의)
function eleApprovalRequest()
{
	var f = document.eForm;

	if(f.doc_sub.value == ""){
		alert("제목을 입력하십시요.");
		return;
	}
	if (f.doc_app_line.value =="") {
		alert("결재선을 입력하십시요.");
		return;
	}
	 
	 //결재선 검사
	data = f.doc_app_line.value;		//결재선 내용
	s = 0;								//substring시작점
	e = data.length;					//문자열 길이
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("승인") != -1) decision++;
		if(rstr.indexOf("협조") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) {
		alert("승인자가 빠졌습니다");
		return;
	}

	//일괄합의 결재상신진행
	f.action='../../../servlet/ApprovalInputServlet';
	f.mode.value='ABAT';	
	f.submit();
	
}

//결재 상신 (순차적 합의)
function eleApprovalRequestPara()
{
	var f = document.eForm;

	if(f.doc_sub.value == ""){
		alert("제목을 입력하십시요.");
		return;
	}
	if (f.doc_app_line.value =="") {
		alert("결재선을 입력하십시요.");
		return;
	}

	//결재선 검사
	data = f.doc_app_line.value;	//결재선 내용
	s = 0;								//substring시작점
	e = data.length;					//문자열 길이
	decision = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("승인") != -1) decision++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) {
		alert("승인자가 빠졌습니다");
		return;
	}

	//결재상신진행
	f.action="../../../servlet/ApprovalInputServlet";
	f.mode.value='REQ';
	f.submit();
	
}

//결재 임시보관
function eleApprovalTemp()
{

	document.eForm.action="../../../servlet/ApprovalInputServlet";
	document.eForm.mode.value='TMP';
	document.eForm.submit();
	
}

//기타문서 전자결재시 첨부파일로 상세하게 보기
function openWin(url)
{
	window.open(url,'open','width=500,height=400,scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//결재선 닫기
function winClose()
{
	self.close();
}
-->
</script>