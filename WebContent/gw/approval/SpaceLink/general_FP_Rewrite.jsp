<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "전자결재 본문 재작성"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="java.util.StringTokenizer"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.file.textFileReader"
%>

<%
	//------------------------------------------------------------------
	//	초기화 & 파일 upload 디렉토리
	//------------------------------------------------------------------
	String uploadDir = upload_path + "/gw/approval/eleApproval/"+ login_id + "/addfile";	//multipart용
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자

	//-----------------------------------
	//	변수 선언
	//-----------------------------------
	// 읽은문서 변수로 받기
	String doc_pid="";			//관리번호
	String doc_lin="";			//읽은문서 결재선
	String doc_sub="";			//읽은문서 제목
	String doc_ste="";			//읽은문서 현 결재단계
	String doc_per="";			//읽은문서 보존기간
	String doc_sec="";			//읽은문서 보존등급
	String doc_bon="";			//읽은문서 본문내용
	String doc_or1="";			//읽은문서 첨부 원래이름1
	String doc_ad1="";			//읽은문서 첨부 저장이름1
	String doc_or2="";			//읽은문서 첨부 원래이름2
	String doc_ad2="";			//읽은문서 첨부 저장이름2
	String doc_or3="";			//읽은문서 첨부 원래이름3
	String doc_ad3="";			//읽은문서 첨부 저장이름3
	String doc_path="";			//읽은문서 관련 path

	String lid="";				//기타문서 전자결재의뢰 관리번호
	String doc_flag="";			//관련정보 종류(SERVICE:고객서비스, BOM:PartList 등)
	String bon_path = "";		//본문내용 path
	String file1_path = "";		//첨부파일1 path
	String file2_path = "";		//첨부파일2 path
	String file3_path = "";		//첨부파일3 path
	String file1_size = "";		//첨부파일1 크기
	String file2_size = "";		//첨부파일2 크기
	String file3_size = "";		//첨부파일3 크기

	//메시지 전달변수
	String Message="";		//메시지 전달 변수  

	//기안/검토/승인자 이름찾기
	String wid = "";			//기안자사번
	String vid = "";			//검토자사번
	String did = "";			//승인자사번
	String wname = "";			//기안자
	String vname = "";			//검토자
	String dname = "";			//승인자
	String vcomm = "";			//검토자 코멘트 (있으면 결재하고 없으면 결재않됨)
	String dcomm = "";			//승인자 코멘트 (있으면 결재하고 없으면 결재않됨)
	
	com.anbtech.gw.entity.TableAppMaster table = new TableAppMaster();	
	ArrayList table_list = new ArrayList();

	table_list = (ArrayList)request.getAttribute("Table_List");
	Iterator table_iter = table_list.iterator();
	while(table_iter.hasNext()) {
			 table = (TableAppMaster)table_iter.next();	
			
			 doc_pid=table.getAmPid();					//관리번호
			 doc_sub=table.getAmAppSubj();				//제목
			 doc_ste=table.getAmAppStatus();			//현 결재단계
			 doc_per=table.getAmSavePeriod();			//보존기간
			 doc_sec=table.getAmSecurityLevel();		//보존등급
			 doc_path=table.getAmBonPath();				//문서 Path

			 doc_or1=table.getAmAdd1Original();			//첨부 원래이름1
			 doc_ad1=table.getAmAdd1File();				//첨부 저장이름1
			 file1_path = upload_path + doc_path + "/addfile/" + doc_ad1;

			 File fn1 = new File(file1_path);
			 file1_size = Long.toString(fn1.length());

			 doc_or2=table.getAmAdd2Original();			//첨부 원래이름2
			 doc_ad2=table.getAmAdd2File();				//첨부 저장이름2
			 file2_path = upload_path + doc_path + "/addfile/" + doc_ad2;
			 File fn2 = new File(file2_path);
			 file2_size = Long.toString(fn2.length());

			 doc_or3=table.getAmAdd3Original();			//첨부 원래이름3
			 doc_ad3=table.getAmAdd3File();				//첨부 저장이름3
			 file3_path = upload_path + doc_path + "/addfile/" + doc_ad3;
			 File fn3 = new File(file3_path);
			 file3_size = Long.toString(fn3.length());

			 lid=table.getAmPlid();						//기타문서 관리번호
			 doc_flag=table.getAmFlag();				//관련정보종류
			 wid = table.getAmWriter();					//기안자사번
			 vid = table.getAmReviewer();				//검토자사번
			 did = table.getAmDecision();				//승인자사번

			 //본문내용 읽기
			 com.anbtech.file.textFileReader text = new textFileReader();
			 bon_path = upload_path + doc_path + "/bonmun/" + table.getAmBonFile();
			 doc_bon = text.getFileString(bon_path);	//읽은문서 본문내용 
	} //while
	

	//결재함 내용 읽기
	com.anbtech.gw.entity.TableAppLine line = new TableAppLine();			
	ArrayList table_line = new ArrayList();

	table_line = (ArrayList)request.getAttribute("Table_Line");
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		line = (TableAppLine)line_iter.next();
									
		if(line.getApStatus().equals("기안"))  wname = line.getApName();	//기안자
		if(line.getApStatus().equals("검토"))  vname = line.getApName();	//검토자
		if(line.getApStatus().equals("승인"))  dname = line.getApName();	//승인자
		if(line.getApStatus().equals("검토")) 
			 vcomm = line.getApComment();//검토자코멘트 (있으면 결재하고 없으면 결재않됨)
		if(line.getApStatus().equals("승인")) 
			 dcomm = line.getApComment();//승인자코멘트 (있으면 결재하고 없으면 결재않됨)
		doc_lin += line.getApStatus()+" "+line.getApSabun()+" "+line.getApName()+" "+line.getApRank()+" "+line.getApDivision()+"\r";	
	}

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../gw/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form action="hyu_ga_FP_Rewrite.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../gw/images/blet.gif"> 일반문서보고</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
<% if(doc_flag.equals("GEN")) {		//일반문서 전자결재 %>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../gw/images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../gw/images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequestPara();"><img src="../gw/images/bt_agree_batch.gif" border="0" align="absmiddle" alt='일괄합의'></a>
				<a href="Javascript:eleApprovalTemp();"><img src="../gw/images/bt_save_tmp.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../gw/images/bt_cancel.gif" border="0" align="absmiddle"></a>
<% } else {		//기타정보 전자결재 상신시 %>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../gw/images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../gw/images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../gw/images/bt_cancel.gif" border="0" align="absmiddle"></a>
<% } %>
			</TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../gw/images/bg-01.gif">결<br>재<br>선</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0" class='text_01'><%=doc_lin%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../gw/images/bg-01.gif">결<p>재</TD>
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
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">문서제목</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text name='doc_sub' size='70' value='<%=doc_sub%>' class='text_01'></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">보존기간</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="doc_per">
				<OPTION <%if(doc_per.equals("0")) out.println("SELECTED");%> VALUE="0">처리후폐기
				<OPTION <%if(doc_per.equals("1")) out.println("SELECTED");%> VALUE="1">1년
				<OPTION <%if(doc_per.equals("2")) out.println("SELECTED");%> VALUE="2">2년
				<OPTION <%if(doc_per.equals("3")) out.println("SELECTED");%> VALUE="3">3년
				<OPTION <%if(doc_per.equals("5")) out.println("SELECTED");%> VALUE="5">5년
				<OPTION <%if(doc_per.equals("EVER")) out.println("SELECTED");%> VALUE="EVER">영구</SELECT>
		   </td>
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">보안등급</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="doc_sec">
				<OPTION <%if(doc_sec.equals("1")) out.println("SELECTED");%> VALUE="1">1급
				<OPTION <%if(doc_sec.equals("2")) out.println("SELECTED");%> VALUE="2">2급
				<OPTION <%if(doc_sec.equals("3")) out.println("SELECTED");%> VALUE="3">3급
				<OPTION <%if(doc_sec.equals("INDOR")) out.println("SELECTED");%> VALUE="INDOR">대외비
				<OPTION <%if(doc_sec.equals("GENER")) out.println("SELECTED");%> VALUE="GENER">일반</SELECT>
			</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="CONTENT" rows=25 cols=88 class='text_01'><%=doc_bon%></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<% if(doc_or1.length() == 0) { %>
					<input type="file" size=60 name="doc_ad1"><br>
				<% } else { %>
					&nbsp; <a href='../gw/approval/eleApproval_downloadp.jsp?fname=<%=doc_or1%>&fsize=<%=file1_size%>&umask=<%=doc_ad1%>&extend=<%=doc_path%>'><%=doc_or1%></a>&nbsp;&nbsp;
					<a href=javascript:addFile1_Delete('<%=upload_path%><%=doc_path%>/addfile/<%=doc_ad1%>')>삭제</a><br>
				<% } %>
				
				<% if(doc_or2.length() == 0) { %>
					<input type="file" size=60 name="doc_ad2"><br>
				<% } else { %>
					&nbsp; <a href='../gw/approval/eleApproval_downloadp.jsp?fname=<%=doc_or2%>&fsize=<%=file2_size%>&umask=<%=doc_ad2%>&extend=<%=doc_path%>'><%=doc_or2%></a>&nbsp;&nbsp;
					<a href=javascript:addFile2_Delete('<%=upload_path%><%=doc_path%>/addfile/<%=doc_ad2%>')>삭제</a><br>
				<% } %>

				<% if(doc_or3.length() == 0) { %>
					<input type="file" size=60 name="doc_ad3"><br>
				<% } else { %>
					&nbsp; <a href='../gw/approval/eleApproval_downloadp.jsp?fname=<%=doc_or3%>&fsize=<%=file3_size%>&umask=<%=doc_ad3%>&extend=<%=doc_path%>'><%=doc_or3%></a>&nbsp;&nbsp;
					<a href=javascript:addFile3_Delete('<%=upload_path%><%=doc_path%>/addfile/<%=doc_ad3%>')>삭제</a><br>
				<% } %>	
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='addFileDir' value='<%=uploadDir%>'>
<input type='hidden' name='login_id' value='<%=sl.id%>'>
<input type='hidden' name='login_name' value='<%=sl.name%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_id' value='<%=doc_pid%>'>
<input type='hidden' name='doc_flag' value='<%=doc_flag%>'>
<input type='hidden' name='doc_lid' value='<%=lid%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
<input type='hidden' name='mode' value=''>
</form>

<% //파일삭제을 스크립트로 처리하기 위해 작성 %>
<form name=d1Form method="post">
<input type="hidden" name="file1" value='<%=file1_path%>'>
<input type="hidden" name="PID" value='<%=doc_pid%>'>
<input type="hidden" name="mode">
</form>

<form name=d2Form method="post">
<input type="hidden" name="file2" value='<%=file2_path%>'>
<input type="hidden" name="PID" value='<%=doc_pid%>'>
<input type="hidden" name="mode">
</form>

<form name=d3Form method="post">
<input type="hidden" name="file3" value='<%=file3_path%>'>
<input type="hidden" name="PID" value='<%=doc_pid%>'>
<input type="hidden" name="mode">
</form>
</body>
</html>

<script language=javascript>
<!--

//첨부파일1 삭제
function addFile1_Delete(file_name)
{
	document.d1Form.action="ApprovalDetailServlet";
	document.d1Form.PID.value='<%=doc_pid%>';
	document.d1Form.mode.value='DELFILE';
	document.d1Form.submit();
}

//첨부파일2 삭제
function addFile2_Delete(file_name)
{
	document.d2Form.action="ApprovalDetailServlet";
	document.d2Form.PID.value='<%=doc_pid%>';
	document.d2Form.mode.value='DELFILE';
	document.d2Form.submit();
}

//첨부파일3 삭제
function addFile3_Delete(file_name)
{
	document.d3Form.action="ApprovalDetailServlet";
	document.d3Form.PID.value='<%=doc_pid%>';
	document.d3Form.mode.value='DELFILE';
	document.d3Form.submit();
}


//결재선 닫기
function winClose()
{
	self.close();
}

//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//결재선 지정지정 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//결재 상신 (일괄합의)
function eleApprovalRequest()
{
	if(eForm.doc_sub.value == ""){
		alert("제목을 입력하십시오.");
		return;
	}
	else if (eForm.CONTENT.value == "") {
		alert("본문을 입력하십시오.");
		return;
	}
	else if (eForm.doc_app_line.value =="") {
		alert("결재선을 입력하십시오.");
		return;
	}
	 
	 //결재선 검사
	data = eForm.doc_app_line.value;		//결재선 내용
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
//	if(agree < 1) {
//		alert("협의자가 2인 이상일때 가능합니다.");
//		return;
//	}

	document.onmousedown=dbclick;// 더블클릭 check

	//일괄합의 결재상신진행
	document.eForm.action='ApprovalInputServlet';
	document.eForm.mode.value='ABAT_UP';	
	document.eForm.submit();
	
}

//결재 상신 (순차적 합의)
function eleApprovalRequestPara()
{
		if(eForm.doc_sub.value == ""){
		alert("제목을 입력하십시오.");
		return;
	}
	else if (eForm.CONTENT.value == "") {
		alert("본문을 입력하십시오.");
		return;
	}
	else if (eForm.doc_app_line.value =="") {
		alert("결재선을 입력하십시오.");
		return;
	}

	//결재선 검사
	data = eForm.doc_app_line.value;		//결재선 내용
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

	document.onmousedown=dbclick;// 더블클릭 check
	//결재상신진행
	document.eForm.action="ApprovalInputServlet";
	document.eForm.mode.value='REQ_UP';
	document.eForm.submit();
	
}

//결재 임시보관 다시 임시보관시 update하기
function eleApprovalTemp()
{
	document.onmousedown=dbclick;// 더블클릭 check

	document.eForm.action="ApprovalInputServlet";
	document.eForm.mode.value='TMP_UP';
	document.eForm.submit();


   // document.lding.visibility="visible";
	
}

// 더블클릭 방지
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}

-->
</script>
