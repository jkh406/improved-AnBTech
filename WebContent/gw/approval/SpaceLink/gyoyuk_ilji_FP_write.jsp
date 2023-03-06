<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "교육일지"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.util.normalFormat"
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
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";	//결재선

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

	//강사 찾기
	String lecturer_id = multi.getParameter("lecturer_id"); if(lecturer_id == null) lecturer_id = "";
	String lecturer_name = "";
	String[] wColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(wColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+lecturer_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		lecturer_id = lecturer_id;							//강사 사번
		lecturer_name = bean.getData("name");				//강사 명
	} //while
	
	//교육주관 (분류)
	String major_kind = multi.getParameter("major_kind"); if(major_kind == null) major_kind = "";
//	String major_kind2 = multi.getParameter("major_kind2"); if(major_kind2 == null) major_kind2 = "";
	String mjr1 = "";
	String mjr2 = "";
	if(major_kind.equals("집합")) mjr1 = "checked";
	if(major_kind.equals("부서")) mjr2 = "checked";
	if(major_kind.length()==0) mjr2 = "checked";

	String place = multi.getParameter("place"); if(place == null) place = "";		//장소
	String participators_cnt = multi.getParameter("participators_cnt"); 
		if(participators_cnt == null) participators_cnt = "0";						//교육대상인원
		else if(participators_cnt.length() == 0) participators_cnt = "0";	
	//교육인원에 따라 작성할 피교육자 명단컬럼 조절하기 (기본 : 20명)
	int content_height = 360;		//교육내용 높이 (기본값)
	int row_cnt = 24;				//content_height의 row값
	int people_height = 210;		//피교육자 명단 높이 (기본값 : 20명)
	int people_line = 10;			//세로줄 최대기입 명단수
	int people_total = 20;			//전체 피교육생 명단수
	int prt_cnt = Integer.parseInt(participators_cnt);
	if(prt_cnt >20) {
		int l_cnt = (prt_cnt + 1) / 2;		//계산된 세로줄 최대기입 명단수
		int l_add = l_cnt - 10;				//기본에서 추가된 line 수
		int l_hgt = l_add * 21;				//추가된 line당 line 크기조절 
		content_height = 210 - l_hgt;
		row_cnt = 24 - l_add;
		people_height = 210 + l_hgt;
		people_line = 10 + l_add;
		people_total = people_line * 2;
	}


	String edu_subject = multi.getParameter("edu_subject"); 
		if(edu_subject == null) edu_subject = "";									//교육명
	String content = multi.getParameter("content"); 
		if(content == null) content = "교육내용:";									//교육내용

	//불참자 처리
	String antiprt_prs= multi.getParameter("antiprt_prs"); if(antiprt_prs == null) antiprt_prs = "";
//	String antiprt_prs2= multi.getParameter("antiprt_prs2"); if(antiprt_prs2 == null) antiprt_prs2 = "";
//	String antiprt_prs3= multi.getParameter("antiprt_prs3"); if(antiprt_prs3 == null) antiprt_prs3 = "";
	String ant1 = "";
	String ant2 = "";
	String ant3 = "";
	if(antiprt_prs.equals("재교육")) ant1 = "checked";
	if(antiprt_prs.equals("전달교육")) ant2 = "checked";
	if(antiprt_prs.equals("기타")) ant3 = "checked";
	if(antiprt_prs.length()==0) ant1 = "checked";

	
	//피교육자 찾기
	String[][] participators = new String[people_total][3]; 
	for(int n=0; n<people_total; n++) for(int m=0; m<3; m++) participators[n][m] = "";
	for(int n=0; n<people_total; n++) {
		String participators_id = multi.getParameter("participators_id"+n);
		if(participators_id == null) participators_id = "";
		String[] pColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
		bean.setTable("user_table a,class_table b,rank_table c");			
		bean.setColumns(pColumn);
		bean.setOrder("a.id ASC");	
		query = "where (a.id ='"+participators_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
		bean.setSearchWrite(query);
		bean.init_write();

		while(bean.isAll()) {
			participators[n][0] = participators_id;					//피교육자 사번
			participators[n][1] = bean.getData("name");				//피교육자 명
		} //while
		String etc = multi.getParameter("prt_etc"+n); if(etc == null) etc = "";
		participators[n][2] = etc;
	}
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form action="gyoyuk_ilji_FP_write.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 교육일지</TD></TR></TBODY>
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
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교육일자</td>
           <td width="37%" height="25" class="bg_04">
			<%
				//시작 년도						
				out.println("<SELECT NAME='doc_syear'>");
				for(int iy = syear; iy < ey; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_syear)) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+iy+"'>"+iy);
				}
				out.println("</SELECT>년"); 

				//시작 월
				out.println("<SELECT NAME='doc_smonth'>");
				for(int iy = 1; iy < 13; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_smonth)) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.println("</SELECT>월"); 

				//시작 일
				out.println("<SELECT NAME='doc_sdate'>");
				for(int iy = 1; iy <= maxdates; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_sdate)) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.println("</SELECT>일");
		   %>		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">강사</td>
           <td width="37%" height="25" class="bg_04"><input size=8 type="text" name="lecturer_name" value='<%=lecturer_name%>' class='text_01' readonly>&nbsp;<a href="Javascript:searchLecturer();"><img src='../../images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교육주관</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" name="major_kind" value="집합" <%=mjr1%> >집합
			<input type="radio" name="major_kind" value="부서" <%=mjr2%> >부서</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">장소</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size=28 name="place" value='<%=place%>' class='text_01'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">주관부서</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교육대상</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size=5 name="participators_cnt" value='<%=participators_cnt%>' OnBlur='javascript:sumPeople()' class='text_01' > 명</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교육명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size=28 name="edu_subject" value='<%=edu_subject%>' class='text_01' ></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">불참자처리</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" name="antiprt_prs" value="재교육" <%=ant1%> >재교육
			<input type="radio" name="antiprt_prs" value="전달교육" <%=ant2%> >전달교육
			<input type="radio" name="antiprt_prs" value="기타" <%=ant3%> >기타</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교육내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="content" rows=3 cols=60  class='text_01'></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교육참석자</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<TABLE cellSpacing=2 cellPadding=0 width=100% border=0 bordercolordark=white bordercolorlight=#9CA9BA> 
					<tr>
						<td width="300" height="23" align="center" class=bg_05>성 명</td>
						<td width="100" height="23" align="center" class=bg_05>서 명</td>
						<td width="100" height="23" align="center" class=bg_05>비 고</td>
						<td width="300" height="23" align="center" class=bg_05>성 명</td>
						<td width="100" height="23" align="center" class=bg_05>서 명</td>
						<td width="100" height="23" align="center" class=bg_05>비 고</td>
					</tr>
					<tr>
						<td width="300" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //성명 1 ~ people_line
								for(int i=0; i<people_line; i++) {
									out.println("<tr>");
									out.println("<td width='100%' height='20' align='center' valign='middle'>"+fmt.toDigits(i+1));
									out.println("<input type='hidden' name='participators_id"+i+"' value='"+participators[i][0]+"'>");
									out.println("<input size='5' type='text' name='participators_name"+i+"' value='"+participators[i][1]+"'>");
									out.println("<a href=\"Javascript:searchUser('eForm.participators_id"+i+"','eForm.participators_name"+i+"');\"><img src='../../images/bt_search.gif' border='0' align='absmiddle'></a>");
									out.println("<a href=\"Javascript:deleteUser('eForm.participators_id"+i+"','eForm.participators_name"+i+"');\"><img src='../../images/bt_del.gif' border='0' align='absmiddle'></a></a>");
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						</td>
						<td width="100" height="210" align="center" class=bg_07>
								<% //서명 1 ~ people_line
								/*for(int i=0; i<people_line; i++) {
									if(participators[i][0].length() != 0) {
										out.println("<img src='../../approval/sign/"+participators[i][0]+".gif' width=60 height=15 align='center'>");
									}
								}*/
								%>
						</td>
						<td width="100" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //비고 1 ~ people_line
								for(int i=0; i<people_line; i++) {
									out.println("<tr>");
									out.println("<td width='100%' height='20' align='center' valign='middle'>");
									out.println("<input type='text' size=10 name='prt_etc"+i+"' value='"+participators[i][2]+"'>");
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						</td>
						<td width="300" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //성명 people_line ~ people_total
								for(int i=people_line; i<people_total; i++) {
									out.println("<tr>");
									out.println("<td width='100%' height='20' align='center' valign='middle'>"+fmt.toDigits(i+1));
									out.println("<input type='hidden' name='participators_id"+i+"' value='"+participators[i][0]+"'>");
									out.println("<input size='5' type='text' name='participators_name"+i+"' value='"+participators[i][1]+"'>");
									out.println("<a href=\"Javascript:searchUser('eForm.participators_id"+i+"','eForm.participators_name"+i+"');\"><img src='../../images/bt_search.gif' border='0' align='absmiddle'></a></a>");
									out.println("<a href=\"Javascript:deleteUser('eForm.participators_id"+i+"','eForm.participators_name"+i+"');\"><img src='../../images/bt_del.gif' border='0' align='absmiddle'></a></a>");
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						</td>
						<td width="90" height="210" align="center" class=bg_07>
								<% //서명 people_line ~ people_total
								/*for(int i=people_line; i<people_total; i++) {
									if(participators[i][0].length() != 0) {
										out.println("<img src='../../../approval/sign/"+participators[i][0]+".gif' width=60 height=15 align='center'>");
									}
								}*/
								%>
						</td>
						<td width="100" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //비고 people_line ~ people_total
								for(int i=people_line; i<people_total; i++) {
									out.println("<tr>");
									out.println("<td width='100%' height='20' align='center' valign='middle'>");
									out.println("<input type='text' size=10 name='prt_etc"+i+"' value='"+participators[i][2]+"'>");
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						</td>
					</tr>
				</table>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type="hidden" name="lecturer_id" value='<%=lecturer_id%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_name" value='<%=user_name%>'>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value=''>
<input type='hidden' name='doc_per' value='3'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
<input type='hidden' name='people_total' value='<%=people_total%>'>

</form>
</body>
</html>


<script language=javascript>
<!--
//강사 찾기
function searchLecturer()
{
	//window.open("searchUser.jsp?target=eForm.lecturer_id/eForm.lecturer_name","user","width=460,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");
	wopen("searchUser.jsp?target=eForm.lecturer_id/eForm.lecturer_name","search_user","250","380","scrollbars=auto,toolbar=no,status=no,resizable=no");

}
//피교육생 찾기
function searchUser(a,b)
{
//	window.open("searchUser.jsp?target="+a+"/"+b,"user","width=460,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");
	wopen("searchUser.jsp?target="+a+"/"+b,"search_user","250","380","scrollbars=auto,toolbar=no,status=no,resizable=no");

}
//입력한 피교육생 취소하기 
function deleteUser(a,b)
{
	var ask = confirm("선택한 피교육생을 명단에서 삭제하시겠습니까?");
	if(ask == false) return;

	var id = eval("document."+a);
	id.value = "";
	var name = eval("document."+b);
	name.value = "";
}
//피교육생 인원
function sumPeople()
{
	document.eForm.action = "gyoyuk_ilji_FP_write.jsp";
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
	var edu_subject = document.eForm.edu_subject.value;
	var doc_sub = "교육일지 : "+edu_subject;

	if(eForm.lecturer_name.value == ""){	alert("강사를 입력하십시오."); eForm.lecturer_name.focus(); return;	}
	if(eForm.place.value == ""){	alert("교육 장소를 입력하십시오."); eForm.place.focus(); return;	}
	if(eForm.participators_cnt.value == ""){	alert("교육 대상을 입력하십시오."); eForm.participators_cnt.focus(); return;	}
	if(eForm.edu_subject.value == ""){	alert("교육명을 입력하십시오."); eForm.edu_subject.focus(); return;	}
	if(eForm.content.value == ""){	alert("교육 내용을 입력하십시오."); eForm.content.focus(); return;	}
	
	document.onmousedown=dbclick;// 더블클릭 check

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/GyoYukServlet';
	document.eForm.mode.value='GYOYUK_ILJI';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
}

//결재 임시보관
function eleApprovalTemp()
{
	//제목구하기
	var edu_subject = document.eForm.edu_subject.value;
	var doc_sub = "교육일지 : "+edu_subject;

	document.onmousedown=dbclick;// 더블클릭 check

	document.eForm.action="../../../servlet/GyoYukServlet";
	document.eForm.mode.value='GYOYUK_ILJI_TMP';
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
