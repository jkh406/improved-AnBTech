<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info= "휴(공)가원 작성"		
	contentType = "text/html; charset=euc-kr"
	errorPage = "../../../admin/errorpage.jsp"
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
	FileWriteString text = new com.anbtech.file.FileWriteString();			//내용을 파일로 담기
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	HyuGaDayDAO hdc = new com.anbtech.es.geuntae.db.HyuGaDayDAO();			//근태일수

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

	//휴가정보 변수
	String[][] huga;				//휴가관리ID,NAME
	String fromDate = "";			//from 일자
	String toDate = "";				//to 일자
	double period = 0.0;			//from ~ to 기간 : 일

	int attache_cnt = 4;			//첨부파일 최대갯수 (미만)

	/*********************************************************************
	 	등록자(login) 알아보기
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
	 	해당자 정보 알아보기 (대상자)
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
	 	휴가 정보
	*********************************************************************/	
	String[] hugaColumn = {"code","code_name"};
	bean.setTable("system_minor_code");			
	bean.setColumns(hugaColumn);
	bean.setOrder("mid DESC");	
	query = "WHERE type = 'GEUNTAE' AND code LIKE 'HD_%'";
	bean.setSearchWrite(query);
	bean.init_write();
	int cnt = bean.getTotalCount();
	huga = new String[cnt][2];

	int i = 0;
	while(bean.isAll()) {
		huga[i][0] = bean.getData("code");				//휴가관리코드
		huga[i][1] = bean.getData("code_name");			//휴가관리명
		i++;
	} //while

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";	//결재선
	String rec = multi.getParameter("doc_receiver"); if(rec == null) rec = "";		//인수인계자
	String tel = multi.getParameter("doc_tel"); if(tel == null) tel = "";			//긴급연락처
	String purpose = multi.getParameter("purpose"); if(purpose == null) purpose = "";//사유 

	//시작 년도
	String year = anbdt.getYear();			
	int syear = Integer.parseInt(year);
	int ey = syear + 5;
	String sel_syear = multi.getParameter("doc_syear"); if(sel_syear == null) sel_syear = year;
	//시작 월
	String month = anbdt.getMonth();
	String sel_smonth = multi.getParameter("doc_smonth");	
	if(sel_smonth == null) sel_smonth = month;
	//시작 일
	String dates = anbdt.getDates();
	String sel_sdate = multi.getParameter("doc_sdate");	
	if(sel_sdate == null) sel_sdate = dates;
	int maxdates = anbdt.getDateMaximum(Integer.parseInt(sel_syear),Integer.parseInt(sel_smonth),1);
	//끝 년도
	String edyear = anbdt.getYear();			
	int eyear = Integer.parseInt(edyear);
	int edy = eyear + 5;
	String sel_eyear = multi.getParameter("doc_edyear"); if(sel_eyear == null) sel_eyear = edyear;
	//끝 월
	String edmonth = anbdt.getMonth();
	String sel_emonth = multi.getParameter("doc_edmonth");	
	if(sel_emonth == null) sel_emonth = edmonth;
	//끝 일
	String eddates = anbdt.getDates();
	String sel_edate = multi.getParameter("doc_eddate");	
	if(sel_edate == null) sel_edate = eddates;
	int emaxdates = anbdt.getDateMaximum(Integer.parseInt(sel_eyear),Integer.parseInt(sel_emonth),1);
	
	period = hdc.getAACount(sel_syear,sel_smonth,sel_sdate,sel_eyear,sel_emonth,sel_edate);
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form action="hyu_ga_FP_write.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 휴(공)가원 작성</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a> <!-- 상신 -->
				<% if(user_id.equals(login_id)) { //대리등록시는 임시저장메뉴 없음 %>
				<a href="Javascript:eleApprovalTemp();"><img src="../../images/bt_save_tmp.gif" border="0" align="absmiddle"></a> <!-- 임시저장 -->
				<% } %><a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

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
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">소속부서</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">대상자</td>
           <td width="37%" height="25" class="bg_04"><input size=10 type="text" name="rank_text" value='<%=user_rank%>' readonly class="text_01"> <input size=10 type="text" name="user_name" value='<%=user_name%>' readonly class="text_01"> <a href="Javascript:searchSabun();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">휴가구분</td>
           <td width="37%" height="25" class="bg_04">
		<%
			String rhuga = multi.getParameter("doc_huga"); if(rhuga == null) rhuga = "";

			out.print("<select name='doc_huga' class='text_01'>");
			String rsel = "";
			for(int n=0; n<i; n++) {
				if(rhuga.equals(huga[n][0])) rsel = "selected";
				else rsel = "";
				out.println("<option "+rsel+" value='"+huga[n][0]+"'>"+huga[n][1]);
			}
			out.println("</select>");
		
		%>		   		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청사유</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="purpose" value='<%=purpose%>' size="30" class="text_01" maxlength="30"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">시작일</td>
           <td width="37%" height="25" class="bg_04">
		<%
			//시작 년도						
			out.println("<SELECT NAME='doc_syear' onChange='javascript:selDate();'>");
			for(int iy = syear; iy < ey; iy++) {
				String sel = "";
				if(iy == Integer.parseInt(sel_syear)) sel = "selected";
				else sel = "";
				out.println("<option "+sel+" value='"+iy+"'>"+iy);
			}
			out.println("</SELECT>년"); 

			//시작 월
			out.println("<SELECT NAME='doc_smonth' onChange='javascript:selDate();'>");
			for(int iy = 1; iy < 13; iy++) {
				String sel = "";
				if(iy == Integer.parseInt(sel_smonth)) sel = "selected";
				else sel = "";
				out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
			}
			out.println("</SELECT>월"); 

			//시작 일
			out.println("<SELECT NAME='doc_sdate' onChange='javascript:selDate();'>");
			for(int iy = 1; iy <= maxdates; iy++) {
				String sel = "";
				if(iy == Integer.parseInt(sel_sdate)) sel = "selected";
				else sel = "";
				out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
			}
			out.println("</SELECT>일");
		%>			
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">종료일</td>
           <td width="37%" height="25" class="bg_04">
		<%

			//끝 년도
			out.println("<SELECT NAME='doc_edyear' onChange='javascript:selDate();'>");
			for(int iy = syear; iy < edy; iy++) {
				String sel = "";
				if(iy == Integer.parseInt(sel_eyear)) sel = "selected";
				else sel = "";
				out.println("<option "+sel+" value='"+iy+"'>"+iy);
			}
			out.println("</SELECT>년"); 

			//끝 월
			out.println("<SELECT NAME='doc_edmonth' onChange='javascript:selDate();'>");
			for(int iy = 1; iy < 13; iy++) {
				String sel = "";
				if(iy == Integer.parseInt(sel_emonth)) sel = "selected";
				else sel = "";
				out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
			}
			out.println("</SELECT>월"); 

			//끝 일
			out.println("<SELECT NAME='doc_eddate' onChange='javascript:selDate();'>");
			for(int iy = 1; iy <= emaxdates; iy++) {
				String sel = "";
				if(iy == Integer.parseInt(sel_edate)) sel = "selected";
				else sel = "";
				out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
			}
			out.println("</SELECT>일");
	   %>
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">휴가일수</td>
           <td width="37%" height="25" class="bg_04">총 <b><%=period%></b> 일간</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">긴급연락처</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_tel" value='<%=tel%>' size="15" maxlength="20" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">업무인수자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_receiver" value='<%=rec%>'  size="15" maxlength="15" class="text_01" readOnly> <a href="Javascript:jobReceiver();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청일자</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getYear()%> 년 <%=anbdt.getMonth()%> 월 <%=anbdt.getDates()%>일</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			for(int a=1; a<attache_cnt; a++) {
				out.println("&nbsp;<input type=file name=attachfile"+a+" size=60><br>");
			}
		%>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value=''>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='period' value='<%=period%>'>
</form>
</body>
</html>


<script language=javascript>
<!--
//대상자 찾기
function searchSabun()
{
	wopen("searchSabun.jsp?target=hyu_ga_FP_write.jsp/eForm.user_id","user","250","380","scrollbars=auto,toolbar=no,status=no,resizable=no");

}
//업무인수인계자
function jobReceiver()
{
	wopen("searchName.jsp?target=eForm.doc_receiver","proxy","250","380","scrollbars=auto,toolbar=no,status=no,resizable=no");

}
//휴가종류 선택
function selHoliday()
{
	document.eForm.action = "hyu_ga_FP_write.jsp";
	document.eForm.submit();
}

//날자 선택
function selDate()
{
	document.eForm.action = "hyu_ga_FP_write.jsp";
	document.eForm.submit();
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
	if (eForm.doc_app_line.value =="") { alert("결재선을 입력하십시오."); return; }
	else if(eForm.purpose.value == "") { alert("사유를 입력하십시오."); return; }
	else if(eForm.doc_receiver.value == "") { alert("업무인수인계자를 입력하십시오."); return; }
	else if(eForm.doc_tel.value == "") { alert("긴급연락처를 입력하십시오."); return; }
	
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
	var a =	document.eForm.doc_huga.selectedIndex;	
	var doc_huga = document.eForm.doc_huga.options[a].text;
	var doc_sub = "휴(공)가원 : "+doc_huga;

	document.onmousedown=dbclick;  // 더블클릭 check

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/GeunTaeServlet';
	document.eForm.mode.value='HYU_GA';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
}

//결재 임시보관
function eleApprovalTemp()
{
	//제목구하기
	var a =	document.eForm.doc_huga.selectedIndex;	
	var doc_huga = document.eForm.doc_huga.options[a].text;
	var doc_sub = "휴(공)가원 : "+doc_huga;

	document.onmousedown=dbclick;  // 더블클릭 check

	document.eForm.action="../../../servlet/GeunTaeServlet";
	document.eForm.mode.value='HYU_GA_TMP';
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

