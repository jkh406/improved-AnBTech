<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "명함 신청서"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.es.geuntae.db.HyuGaDayDAO"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	FileWriteString text = new com.anbtech.file.FileWriteString();			//디렉토리 생성하기
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식

	String filepath =  upload_path+"/es/"+login_id+"/addfile";
	text.setFilepath(filepath);		//directory생성하기

	int maxFileSize = 10;
	com.oreilly.servlet.MultipartRequest multi;
	multi = new com.oreilly.servlet.MultipartRequest(request,filepath,maxFileSize*1024*1024,"euc-kr");

	String query = "";
	String writer_id = "";			//등록자(대리등록일수도 있음) 사번
	String writer_name = "";		//등록자(대리등록일수도 있음) 이름

	String user_name = "";			//해당자 명
	String rank_code = "";			//해당자 직급code
	String user_rank = "";			//해당자 직급
	String div_id = "";				//해당자 부서명 관리코드
	String div_name = "";			//해당자 부서명
	String div_code = "";			//해당자 부서코드

	int con_cnt = 9;				//변경내용 라인수

	/*********************************************************************
	 	등록자(login) 알아보기 : 전자결재 등록을 위해 [공통]
	*********************************************************************/	
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//등록자 사번
		writer_name = bean.getData("name");				//등록자 명
	} //while


	/*********************************************************************
	 	해당자 정보 알아보기 (대상자) : 대상자 정보 [공통]
	*********************************************************************/	
	String user_id = multi.getParameter("user_id"); if(user_id == null) user_id = login_id;
	String[] idColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+user_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_name = bean.getData("name");				//해당자 명
		rank_code = bean.getData("ar_code");			//해당자 직급 code
		user_rank = bean.getData("ar_name");			//해당자 직급
		div_id = bean.getData("ac_id");					//해당자 부서명 관리코드
		div_name = bean.getData("ac_name");				//해당자 부서명 
		div_code = bean.getData("ac_code");				//해당자 부서코드
	} //while

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";		//결재선
	String purpose = multi.getParameter("purpose"); if(purpose == null) purpose = "";	//신청사유

	//대상자 찾기
	String applicant_id = multi.getParameter("applicant_id");
			if(applicant_id == null) applicant_id = login_id;
	String applicant_name = "";
	String[] wColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(wColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+applicant_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		applicant_id = applicant_id;						//대상자 사번
		applicant_name = bean.getData("name");				//대상자 명
	} //while

	//변경내용
	String[][] data = new String[con_cnt][3];
	for(int i=0; i<con_cnt; i++) {
		data[i][0] = multi.getParameter("item"+i); if(data[i][0] == null) data[i][0] = "";	//내용
		data[i][1] = multi.getParameter("before"+i); if(data[i][1] == null) data[i][1] = "";//변경전
		data[i][2] = multi.getParameter("after"+i); if(data[i][2] == null) data[i][2] = "";	//변경후
	}
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form action="myeongham_FP_write.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 명함신청서</TD></TR></TBODY>
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
				<a href="Javascript:eleApprovalTemp();"><img src="../../images/bt_save_tmp.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<br>재<br>선</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
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
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청일자</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=anbdt.getYear()%>년 <%=anbdt.getMonth()%>월 <%=anbdt.getDates()%>일</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">소속부서</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">대상자</td>
           <td width="37%" height="25" class="bg_04"><input size=10 type="text" name="applicant_name" value='<%=applicant_name%>' readonly> <a href="Javascript:searchUser();"><img src='../../images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input size=50 type="text" name="purpose" value='<%=purpose%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">변경내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<TABLE cellSpacing=2 cellPadding=0 width=90% border=0 bordercolordark=white bordercolorlight=#9CA9BA>
					<tr>
						<td width="80" height="23" align="center" class=bg_05>내용</td>
						<td width="230" height="23" align="center" class=bg_05>변경전</td>
						<td width="230" height="23" align="center" class=bg_05>변경후</td>
					</tr>
					<%
					for(int i=0; i<con_cnt; i++) {
						out.println("<tr>");
						out.println("<td width='80' class=bg_07 align='center' valign='middle'>");
						out.println("<input size='8' type='text' name='item"+i+"' value='"+data[i][0]+"'></td>");
						out.println("<td width='230' class=bg_07 align='center'>");
						out.println("<input size='30' type='text' name='before"+i+"' value='"+data[i][1]+"'></td>");
						out.println("<td width='230' class=bg_07 align='center' >");
						out.println("<input size='30' type='text' name='after"+i+"' value='"+data[i][2]+"'></td>");
						out.println("</tr>");
					}
					%>
				</table>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_name" value='<%=user_name%>'>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type="hidden" name="applicant_id" value='<%=applicant_id%>'>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='con_cnt' value='<%=con_cnt%>'>
<input type='hidden' name='doc_sub' value=''>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
</form>
</body>
</html>

<script language=javascript>
<!--
//대상자 찾기
function searchUser()
{
	wopen("searchUser.jsp?target=eForm.applicant_id/eForm.applicant_name","search_user","250","380","scrollbars=auto,toolbar=no,status=no,resizable=no");

}
//출력하기
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
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

//결재 상신 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("결재선을 입력하십시요."); return; }
	
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
	if(decision == 0) { alert("승인자가 빠졌습니다"); return; }
	
	//제목구하기
	var purpose = document.eForm.purpose.value;
	var doc_sub = "명함신청서 : "+purpose;

	document.onmousedown=dbclick;  // 더블클릭 check

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/JiWeonServlet';
	document.eForm.mode.value='MYEONGHAM';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
}

//결재 임시보관
function eleApprovalTemp()
{
	//제목구하기
	var purpose = document.eForm.purpose.value;
	var doc_sub = "명함신청서 : "+purpose;

	document.onmousedown=dbclick;  // 더블클릭 check

	document.eForm.action="../../../servlet/JiWeonServlet";
	document.eForm.mode.value='MYEONGHAM_TMP';
	document.eForm.app_mode.value='TMP';	
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
	
}

// 더블클릭 방지
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>
