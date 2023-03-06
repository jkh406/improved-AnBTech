<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "구인의뢰서 재작성"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"

	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"

	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.util.normalFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	textFileReader text = new com.anbtech.file.textFileReader();			//본문파일 읽기
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식

	String filepath =  upload_path+"/es/"+login_id+"/addfile";
	int maxFileSize = 10;
	com.oreilly.servlet.MultipartRequest multi;
	multi = new com.oreilly.servlet.MultipartRequest(request,filepath,maxFileSize*1024*1024,"euc-kr");

	//기안서 내용관련
	String query = "";
	String writer_id = "";			//등록자(대리등록일수도 있음) 사번
	String writer_name = "";		//등록자(대리등록일수도 있음) 이름

	String user_name = "";			//해당자 명
	String rank_code = "";			//해당자 직급code
	String user_rank = "";			//해당자 직급
	String div_id = "";				//해당자 부서명 관리코드
	String div_name = "";			//해당자 부서명
	String div_code = "";			//해당자 부서코드

	int work_cnt = 22;				//연장근무신청 작성 컬럼수
	
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
	 	view.jsp로부터 받은 첨부파일 정보
	*********************************************************************/	
	String doc_id = multi.getParameter("doc_id");
	String old_id = multi.getParameter("old_id"); if(old_id == null) old_id = doc_id;	//최초로 넘겨받을때만
	
	//구인정보 읽기
	String job_kind = "";			//모집직종
	String job_content = "";		//업무내용
	String career = "";				//학력
	String major = "";				//전공
	String req_qualify = "";		//필요자격증
	String status = "";				//입사형태
	String job_career = "";			//요구경력
	String job_etc = "";			//요구경력 기타
	String req_count = "";			//모집인원
	String marray = "";				//혼인
	String army = "";				//병역
	String employ = "";				//고용형태
	String employ_per = "";			//고용형태 계약직 기간
	String language_grade = "";		//외국어
	String language_exam = "";		//공인시험
	String language_score = "";		//등급/점수
	String comp_grade = "";			//전산능력
	String comp_etc = "";			//전산능력 기타
	String papers = "";				//제출서류
	String note = "";				//기타 필요사항
	String[] gColumn = {"job_kind","job_content","career","major","req_qualify","status","job_career",
						"job_etc","req_count","marray","army","employ","employ_per","language_grade",
						"language_exam","language_score","comp_grade","comp_etc","papers","note"};
	bean.setTable("insa_master");			
	bean.setColumns(gColumn);
	bean.setOrder("ac_name ASC");	
	query = "where (is_id ='"+old_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		job_kind = bean.getData("job_kind");		//모집직종
		job_content = bean.getData("job_content");	//업무내용
		career = bean.getData("career");			//학력
		major = bean.getData("major");				//전공
		req_qualify = bean.getData("req_qualify");	//필요자격증
		status = bean.getData("status");			//입사형태
		job_career = bean.getData("job_career");	//요구경력
		job_etc = bean.getData("job_etc");			//요구경력 기타
		req_count = bean.getData("req_count");		//모집인원
		marray = bean.getData("marray");			//혼인
		army = bean.getData("army");				//병역
		employ = bean.getData("employ");			//고용형태
		employ_per = bean.getData("employ_per");	//고용형태 계약직 기간
		language_grade = bean.getData("language_grade");	//외국어
		language_exam = bean.getData("language_exam");		//공인시험
		language_score = bean.getData("language_score");	//등급/점수
		comp_grade = bean.getData("comp_grade");			//전산능력
		comp_etc = bean.getData("comp_etc");		//전산능력 기타
		papers = bean.getData("papers");			//제출서류
		note = bean.getData("note");				//기타 필요사항
	} //while
	
	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";		//결재선	
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form action="guin_FP_Rewrite.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 구인의뢰서</TD></TR></TBODY>
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
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">문서번호</td>
           <td width="37%" height="25" class="bg_04">결재완료후 자동채번</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">작성일자</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getYear()%>년 <%=anbdt.getMonth()%>월 <%=anbdt.getDates()%>일</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">모집집종</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="job_kind" rows=2 cols=60><%=job_kind%></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">업무내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="job_content" rows=2 cols=60><%=job_content%></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">학력</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size=17 name="career" value='<%=career%>'></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">전공</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size=30 name="major" value='<%=major%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">필요자격증</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" size=60 name="req_qualify" value='<%=req_qualify%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">입사형태</td>
           <td width="37%" height="25" class="bg_04">
		<%
			String[] sel_list = {"신입","경력","무관"};
			int sel_cnt = sel_list.length;
			//기존의 값
			status = StringProcess.repWord(status,":"," :");
			StringTokenizer status_list = new StringTokenizer(status,":");
			String[] sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			int c = 0;
			while(status_list.hasMoreTokens()) {
				sts[c] = status_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//화면출력
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(sel_list[i])) 
					 out.println("<input type='checkbox' checked name='status"+k+"' value='"+sel_list[i]+"'>"+sel_list[i]);
				else out.println("<input type='checkbox' name='status"+k+"' value='"+sel_list[i]+"'>"+sel_list[i]);
			}
		%>
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">모집인원</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size=3 name="req_count" value='<%=req_count%>'> 명</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">요구경력</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" size=5 name="job_career" value='<%=job_career%>'>년이상 (기타:<input type="text" size=20 name="job_etc" value=''>)</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">고용형태</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			String[] em_list = {"정규직","계약직","시간제","파견근로"};
			sel_cnt = em_list.length;
			//기존의 값
			employ = StringProcess.repWord(employ,":"," :");
			StringTokenizer employ_list = new StringTokenizer(employ,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(employ_list.hasMoreTokens()) {
				sts[c] = employ_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//화면출력
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(em_list[i])) {
					if(em_list[i].equals("계약직")) {
						out.println("<input type='checkbox' checked name='employ"+k+"' value='"+em_list[i]+"'>"+em_list[i]);
						out.println("(기간 <input type='text' size=5 name='employ_per' value='"+employ_per+"'> 개월)");
					} else {
						 out.println("<input type='checkbox' checked name='employ"+k+"' value='"+em_list[i]+"'>"+em_list[i]);
					}
				} else { 
					if(em_list[i].equals("계약직")) {
						out.println("<input type='checkbox' name='employ"+k+"' value='"+em_list[i]+"'>"+em_list[i]);
						out.println("(기간 <input type='text' size=5 name='employ_per' value='"+employ_per+"'> 개월)");
					} else {
						out.println("<input type='checkbox' name='employ"+k+"' value='"+em_list[i]+"'>"+em_list[i]);
					}
				}
			}

		%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">혼인여부</td>
           <td width="37%" height="25" class="bg_04">
		<%
			String[] mry_list = {"미혼","기혼","무관"};
			sel_cnt = mry_list.length;
			//기존의 값
			marray = StringProcess.repWord(marray,":"," :");
			StringTokenizer marray_list = new StringTokenizer(marray,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(marray_list.hasMoreTokens()) {
				sts[c] = marray_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//화면출력
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(mry_list[i])) 
					 out.println("<input type='checkbox' checked name='marray"+k+"' value='"+mry_list[i]+"'>"+mry_list[i]);
				else out.println("<input type='checkbox' name='marray"+k+"' value='"+mry_list[i]+"'>"+mry_list[i]);
			}

		%>
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">병력</td>
           <td width="37%" height="25" class="bg_04">
		<%
			String[] am_list = {"필","무관"};
			sel_cnt = am_list.length;
			//기존의 값
			army = StringProcess.repWord(army,":"," :");
			StringTokenizer army_list = new StringTokenizer(army,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(army_list.hasMoreTokens()) {
				sts[c] = army_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//화면출력
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(am_list[i])) 
					 out.println("<input type='checkbox' checked name='army"+k+"' value='"+am_list[i]+"'>"+am_list[i]);
				else out.println("<input type='checkbox' name='army"+k+"' value='"+am_list[i]+"'>"+am_list[i]);
			}

		%>
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">외국어</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			String[] eg_list = {"상","중","하"};
			sel_cnt = eg_list.length;
			//기존의 값
			language_grade = StringProcess.repWord(language_grade,":"," :");
			StringTokenizer language_grade_list = new StringTokenizer(language_grade,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(language_grade_list.hasMoreTokens()) {
				sts[c] = language_grade_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//화면출력
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(eg_list[i])) 
					 out.println("<input type='checkbox' checked name='language_grade"+k+"' value='"+eg_list[i]+"'>"+eg_list[i]);
				else out.println("<input type='checkbox' name='language_grade"+k+"' value='"+eg_list[i]+"'>"+eg_list[i]);
			}

		%>)&nbsp;
			(공인시험:<input type="text" size=10 name="language_exam" value='<%=language_exam%>'>, 
			등급/점수:<input type="text" size=10 name="language_score" value='<%=language_score%>'> )
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">전산능력</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			String[] cg_list = {"문서작성","엑셀","프리젠테이션","인터넷","홈페이제작"};
			sel_cnt = cg_list.length;
			//기존의 값
			comp_grade = StringProcess.repWord(comp_grade,":"," :");
			StringTokenizer comp_grade_list = new StringTokenizer(comp_grade,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(comp_grade_list.hasMoreTokens()) {
				sts[c] = comp_grade_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//화면출력
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(cg_list[i])) 
					 out.println("<input type='checkbox' checked name='comp_grade"+k+"' value='"+cg_list[i]+"'>"+cg_list[i]);
				else out.println("<input type='checkbox' name='comp_grade"+k+"' value='"+cg_list[i]+"'>"+cg_list[i]);
			}

		%>
		기타( <input type="text" size=10 name="comp_etc" value='<%=comp_etc%>'> )
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">제출서류</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			String[] ps_list = {"이력서","자기소개서","성적증명서","졸업증명서","경력증명서","자격증사본"};
			sel_cnt = ps_list.length;
			//기존의 값
			papers = StringProcess.repWord(papers,":"," :");
			StringTokenizer papers_list = new StringTokenizer(papers,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(papers_list.hasMoreTokens()) {
				sts[c] = papers_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//화면출력
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(ps_list[i])) 
					 out.println("<input type='checkbox' checked name='papers"+k+"' value='"+ps_list[i]+"'>"+ps_list[i]);
				else out.println("<input type='checkbox' name='papers"+k+"' value='"+ps_list[i]+"'>"+ps_list[i]);
			}

		%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">기타요구사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="note" rows=3 cols=60><%=note%></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>


         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_name" value='<%=user_name%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>

<% //서브릿 데이터 처리 %>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='work_cnt' value='<%=work_cnt%>'>
<input type='hidden' name='doc_sub' value='구인의뢰서'>
<input type='hidden' name='doc_per' value='100'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='old_id' value='<%=old_id%>'>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
</form>  
</body>
</html>

<script language=javascript>
<!--
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

	document.onmousedown=dbclick;// 더블클릭 check

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/InSaServlet';
	document.eForm.mode.value='R_GUIN';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();
}

//결재 임시보관
function eleApprovalTemp()
{
	document.onmousedown=dbclick;// 더블클릭 check

	document.eForm.action="../../../servlet/InSaServlet";
	document.eForm.mode.value='R_GUIN_TMP';
	document.eForm.app_mode.value='TMP';	
	document.eForm.submit();
}

// 더블클릭 방지
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>
