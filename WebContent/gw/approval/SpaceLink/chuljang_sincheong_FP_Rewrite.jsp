<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info= "출장신청서 재작성"		
	contentType = "text/html; charset=euc-kr"
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	FileWriteString text = new com.anbtech.file.FileWriteString();			//내용을 파일로 담기
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식 (월,일)
	normalFormat money = new com.anbtech.util.normalFormat("#,###");		//출력형식 (비용)
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기

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

	int period_n = 0;				//from ~ to 기간 : 박
	int period = 0;					//from ~ to 기간 : 일

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
	 	출장비 영수인 알아보기
	*********************************************************************/	
	String receiver_id = multi.getParameter("receiver_id");	if(receiver_id == null) receiver_id = "";	
	if(receiver_id.length() == 0) receiver_id = writer_id;
	String[] recColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(recColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+receiver_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	String receiver_name = "";
	while(bean.isAll()) {
		receiver_name = bean.getData("name");			//영수인 명
	} //while
	
	String doc_chuljang = "BT_001";
	/*********************************************************************
	 	출장비 항목 정보
	*********************************************************************/	
	String[] csColumn = {"code","code_name"};
	bean.setTable("system_minor_code");			
	bean.setColumns(csColumn);
	bean.setClear();
	bean.setOrder("code DESC");	
	query = "WHERE type = 'BT_COST'";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] btrip = new String[cnt][4];

	int i = 0;
	while(bean.isAll()) {
		btrip[i][0] = bean.getData("code");				//출장관리코드
		btrip[i][1] = bean.getData("code_name");		//출장관리명
		i++;
	} //while

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/	
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";	//결재선
	String prj_code = multi.getParameter("prj_code"); 
		if(prj_code == null || prj_code.length() == 0) prj_code = ":";  			//프로젝트 코드
	String project_code = prj_code.substring(0,prj_code.indexOf(":"));
	String prj_name = prj_code.substring(prj_code.indexOf(":")+1,prj_code.length());

	String fellow_names = multi.getParameter("fellow_names"); 
		if(fellow_names == null) fellow_names = "";									//동행자 사번/이름;
	String bistrip_kind = multi.getParameter("bistrip_kind"); 
		if(bistrip_kind == null) bistrip_kind = "국내";								//출장지 구분(국내/외)
	String bistrip_country = multi.getParameter("bistrip_country"); 
		if(bistrip_country == null) bistrip_country = "";							//출장지 국가명
	String bistrip_city = multi.getParameter("bistrip_city"); 
		if(bistrip_city == null) bistrip_city = "";									//출장지 도시명
	String traffic_way = multi.getParameter("traffic_way"); 
		if(traffic_way == null) traffic_way = "";									//교통편
	String purpose = multi.getParameter("purpose"); 
		if(purpose == null) purpose = "";											//출장지 목적
	String rec = multi.getParameter("doc_receiver"); if(rec == null) rec = "";		//인수인계자
	String tel = multi.getParameter("doc_tel"); if(tel == null) tel = "";			//긴급연락처
	String bank_no = multi.getParameter("bank_no"); if(bank_no == null) bank_no = "";//계좌번호
	
	
	//비용금액과 산출내용을 배열에 담기
	int costcnt = btrip.length;
	String[][] cost = new String[costcnt][3];
	String c_code = "";		//출장코드
	String c_cost = "";		//출장비용
	String c_cont = "";		//산출내용
	int sum = 0;			//비용 합계
	for(int c=0,m=1; c < costcnt; c++,m++) {
		c_code = "code"+m;
		c_cost = "cost"+m;
		c_cont = "cont"+m;
		cost[c][0] = multi.getParameter(c_code);	if(cost[c][0] == null) cost[c][0] = "";
		cost[c][1] = multi.getParameter(c_cost);	if(cost[c][1] == null) cost[c][1] = "0";
		cost[c][2] = multi.getParameter(c_cont);	if(cost[c][2] == null) cost[c][2] = "";
		
		//비용합계 계산하기
		cost[c][1] = str.repWord(cost[c][1],",","");
		try { sum += Integer.parseInt(cost[c][1]); } catch (Exception e) {cost[c][1] = "0";} 
	}
	//out.println("sum : " + fmt.toDigits(sum));

	//----------------------------
	// from chuljang_sincheoung_FP_view.jsp
	//----------------------------
	String old_id = "";
	old_id = multi.getParameter("old_id"); if(old_id == null) old_id = "";		//자체적으로
	if(old_id.length() == 0) {
		old_id = multi.getParameter("doc_id"); if(old_id == null) old_id = "";	//from ..FP_view.jsp
	}
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return true" onLoad='selectBisKind()'>
<form action="chuljanb_sincheong_FP_Rewrite.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 출장신청서 작성</TD></TR></TBODY>
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

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">소속부서</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장자</td>
           <td width="37%" height="25" class="bg_04"> <input size="10" type="text" name="user_name" value='<%=user_name%>' class="text_01" readonly> <a href="Javascript:searchUser();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">시작일</td>
           <td width="37%" height="25" class="bg_04">
			<%
				//시작 년도
				String year = anbdt.getYear();			
				int syear = Integer.parseInt(year);
				int ey = syear + 5;

				String sel_syear = multi.getParameter("doc_syear"); if(sel_syear == null) sel_syear = year;
							
				out.print("<SELECT NAME='doc_syear' onChange='javascript:selDate();'>");
				for(int iy = syear; iy < ey; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_syear)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+iy+"'>"+iy);
				}
				out.print("</SELECT>년 "); 

				//시작 월
				String month = anbdt.getMonth();
				String sel_smonth = multi.getParameter("doc_smonth");	
				if(sel_smonth == null) sel_smonth = Integer.toString(Integer.parseInt(month));

				out.print("<SELECT NAME='doc_smonth' onChange='javascript:selDate();'>");
				for(int iy = 1; iy < 13; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_smonth)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.print("</SELECT>월 "); 

				//시작 일
				//getDateMaximum(int syear,int smonth,int sdate)
				String dates = anbdt.getDates();
				String sel_sdate = multi.getParameter("doc_sdate");	
				if(sel_sdate == null) sel_sdate = Integer.toString(Integer.parseInt(dates));
				int maxdates = anbdt.getDateMaximum(Integer.parseInt(sel_syear),Integer.parseInt(sel_smonth),1);

				out.print("<SELECT NAME='doc_sdate' onChange='javascript:selDate();'>");
				for(int iy = 1; iy <= maxdates; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_sdate)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.print("</SELECT>일");
			%>
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">종료일</td>
           <td width="37%" height="25" class="bg_04">
			<%
				//끝 년도
				String edyear = anbdt.getYear();			
				int eyear = Integer.parseInt(edyear);
				int edy = eyear + 5;
				String sel_eyear = multi.getParameter("doc_edyear"); if(sel_eyear == null) sel_eyear = edyear;
				
				out.print("<SELECT NAME='doc_edyear' onChange='javascript:selDate();'>");
				for(int iy = syear; iy < edy; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_eyear)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+iy+"'>"+iy);
				}
				out.print("</SELECT>년 "); 

				//끝 월
				String edmonth = anbdt.getMonth();
				String sel_emonth = multi.getParameter("doc_edmonth");	
				if(sel_emonth == null) sel_emonth = Integer.toString(Integer.parseInt(edmonth));

				out.print("<SELECT NAME='doc_edmonth' onChange='javascript:selDate();'>");
				for(int iy = 1; iy < 13; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_emonth)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.print("</SELECT>월 "); 

				//끝 일
				//getDateMaximum(int syear,int smonth,int sdate)
				String eddates = anbdt.getDates();
				String sel_edate = multi.getParameter("doc_eddate");	
				if(sel_edate == null) sel_edate = Integer.toString(Integer.parseInt(eddates));
				int emaxdates = anbdt.getDateMaximum(Integer.parseInt(sel_eyear),Integer.parseInt(sel_emonth),1);

				out.print("<SELECT NAME='doc_eddate' onChange='javascript:selDate();'>");
				for(int iy = 1; iy <= emaxdates; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_edate)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.print("</SELECT>일");

			   //기간 구하기
			   period_n = anbdt.getPeriodDate(Integer.parseInt(sel_syear),Integer.parseInt(sel_smonth),Integer.parseInt(sel_sdate),Integer.parseInt(sel_eyear),Integer.parseInt(sel_emonth),Integer.parseInt(sel_edate));
			   period = period_n + 1;
			%>
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장일수</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='gab_days' value='<%=period_n%>박 <%=period%>일간' class="text_01" readonly></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장지</td>
           <td width="37%" height="25" class="bg_04">
			<select name='bistrip_kind' onChange="javascript:selectBisKind();">
				<% 
					String seln = "";	String sela = "";
					if(bistrip_kind.equals("국내")) seln = " selected ";
					else if(bistrip_kind.equals("국외")) sela = " selected ";
					out.print("<OPTION value='국내'"+seln+">국내</OPTION>");
					out.print("<OPTION value='국외'"+sela+">국외</OPTION>");
					out.print("</select>");
				%>
				도시명<input size=10 type='text' name='bistrip_city' class='text_01' value='<%=bistrip_city%>'><span id='ad'></span>			   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장목적</td>
           <td width="37%" height="25" class="bg_04"><input size=30 type="text" name="purpose" value='<%=purpose%>' maxlength="50" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교통편</td>
           <td width="37%" height="25" class="bg_04">
					<select name='traffic_way'>
				<% 
					String a = ""; if(traffic_way.equals("버스")) a = " selected ";
					String b = ""; if(traffic_way.equals("기차")) b = " selected ";
					String c = ""; if(traffic_way.equals("항공기")) c = " selected ";
					String d = ""; if(traffic_way.equals("자가운전")) d = " selected ";
					String e = ""; if(traffic_way.equals("회사차")) e = " selected ";
					String f = ""; if(traffic_way.equals("도보")) f = " selected ";
					String g = ""; if(traffic_way.equals("기타")) g = " selected ";
					out.print("<OPTION value='버스'"+a+">버스</OPTION>");
					out.print("<OPTION value='기차'"+b+">기차</OPTION>");
					out.print("<OPTION value='항공기'"+c+">항공기</OPTION>");
					out.print("<OPTION value='자가운전'"+d+">자가운전</OPTION>");
					out.print("<OPTION value='회사차'"+e+">회사차</OPTION>");
					out.print("<OPTION value='도보'"+f+">도보</OPTION>");
					out.print("<OPTION value='기타'"+g+">기타</OPTION>");
				%>
					</select>		   		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">프로젝트코드</td>
           <td width="37%" height="25" class="bg_04"><input size="15" type="text" name="project_code" value='<%=project_code%>' readonly> <input size="10" type="text" name="prj_name" value='<%=prj_name%>' readonly> <a href="javascript:sel_pjt_code();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">동행자</td>
           <td width="37%" height="25" class="bg_04"><TEXTAREA NAME="fellow_names" rows=1 cols=16 readOnly><%=fellow_names%></TEXTAREA> <a href="Javascript:searchProxy();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">업무인수자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_receiver" value='<%=rec%>' size="15" readOnly> <a href="Javascript:jobReceiver();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">긴급연락처</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_tel" value='<%=tel%>' size="15" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청일자</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=anbdt.getYear()%> 년 <%=anbdt.getMonth()%> 월 <%=anbdt.getDates()%> 일</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
<%	/************************************************************
	 출장비 부분을 제외시킴. 출장비 부분을 추가시키려면 다음사항을 보완할 것.
	 - 출장비용항목을 yangsic_env 에서 system_minor_code 에서 가져오게 수정
	 - 출장비용항목의 개수가 제대로 나타나지 않는 문제 해결
	 - 금액란의 초기값이 000이 아닌 0로 나타나게
	 - 자동계산을 JSP가 아닌 스크립트로 처리하여 화면이 껌벅이지 않게
	 ************************************************************/
%>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출 장 비<br>청 구</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
			<%
				out.print("<TABLE cellSpacing=2 cellPadding=0 width=90% border=0 bordercolordark=white bordercolorlight=#9CA9BA>");
				out.print("<tr><td height=23 class=bg_05 width='15%' align='center'>항목</td>"); 
				out.print("<td class=bg_05 width='20%' align='center'>청구금액(원)</td>"); 
				out.print("<td class=bg_05 width='20%' align='center'>지급금액(원)</td>"); 
				out.print("<td class=bg_05 width='45%' align='center'>청구금액산출내역</td></tr>");

				for(int n=1,p=0; p < cnt; n++,p++) {
					out.print("<tr><td class=bg_07 align='center'><input type='hidden' name='code"+n+"' value='"+btrip[p][0]+"'>");
					out.print(btrip[p][1]+"</td>");
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='cost"+n+"' value='"+money.StringToString(cost[p][1])+"' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);'></td>");//청구비용(금액)
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='ep_cost"+n+"' value='0' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);' readonly></td>");//지급비용(금액)
					out.print("<td class=bg_07><input size=40 type='text' name='cont"+n+"' value='"+cost[p][2]+"'></td></tr>");
				}
				out.print("<tr><td class=bg_07 align='center'><b>합계</b></td><td class=bg_07><input class='money' size=15 type='text' name='sum' value='"+ money.toDigits(sum) +"' readonly></td>");
				out.print("</td><td class=bg_07><input class='money' size=15 type='text' name='ep_sum' value='0' readonly></td></tr>");
				out.print("</table>");
			%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">영수인</td>
           <td width="37%" height="25" class="bg_04"><input size=10 type="text" name="receiver_name" value='<%=receiver_name%>'> <a href="Javascript:searchReceiver();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>
		   <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">계좌번호</td>
           <td width="37%" height="25" class="bg_04"><input size=30 type="text" name="bank_no" value='<%=bank_no%>'></td>
		 </tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table> 

</td></tr></table>

<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'></td>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='old_id' value='<%=old_id%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='[출장신청서]'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='account_cnt' value='<%=i%>'>
<input type='hidden' name='doc_chuljang' value='<%=doc_chuljang%>'>
<input type='hidden' name='period' value='<%=period%>'>
<input type='hidden' name='prj_code' value=''>
</form>
</body>
</html>

<script language=javascript>
<!--
//대상자 찾기
function searchUser()
{
	wopen("searchUser.jsp?target=eForm.user_id/eForm.user_name","user","250","380","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//프로젝트 찾기
function sel_pjt_code()
{
	wopen('../../../servlet/PsmProcessServlet?mode=search_project&target=eForm.project_code/eForm.prj_name','search_pjt','400','230','scrollbars=no,toolbar=no,status=no,resizable=no');

}
//동행자 찾기
function searchProxy()
{
	var fellows = document.eForm.fellow_names.value;
	var url = "searchFellows.jsp?target=eForm.fellow_names&fellows="+fellows;
	wopen(url,"proxy","510","467","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//업무인수인계자
function jobReceiver()
{
	wopen("searchName.jsp?target=eForm.doc_receiver","proxy","250","380","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//영수인 찾기
function searchReceiver()
{
	wopen("searchUser.jsp?target=eForm.receiver_id/eForm.receiver_name","proxy","250","380","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//출장지 선택
function selectBisKind()
{
	var kind = document.eForm.bistrip_kind.value;
	if(kind == "국내") ad.innerHTML="";
	else ad.innerHTML=" 국가명<input size=10 type='text' name='bistrip_country' class='text_01' value='<%=bistrip_country%>'>";
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
//기간 구하기 (날자선택)
function selDate() {
	var sy = document.eForm.doc_syear.value;
	var sm = document.eForm.doc_smonth.value;
	var sd = document.eForm.doc_sdate.value;
	var start_day = new Date(sy,sm-1,sd);
	start_day = start_day.getTime();

	var ey = document.eForm.doc_edyear.value;
	var em = document.eForm.doc_edmonth.value;
	var ed = document.eForm.doc_eddate.value;
	var end_day = new Date(ey,em-1,ed);
	end_day = end_day.getTime();

	var night_gab = Math.floor((end_day-start_day)/(60*60*24*1000));
	var day_gab = night_gab+1;

	document.eForm.gab_days.value=night_gab+"박 "+day_gab+"일간";

}
//금액 천단위에 콤마넣기
function InputMoney(input){
	str = input.value;	
	str = unComma(str);
	str = Comma(str);
	input.value = str;

	sumMoney();
}
//총액표시하기
function sumMoney(){
	var n = '<%=cnt%>';
	var sum = 0;
	for(i=0,j=1; i<n; i++,j++) {
		sum += unCommaObj(eval("document.eForm.cost"+j+".value"));
	}
	document.eForm.sum.value=Comma(sum);
}
function isNumObj(obj)
{
	for (var i = 0; i < obj.value.length ; i++){
		chr = obj.value.substr(i,1);		
		chr = escape(chr);
		key_eg = chr.charAt(1);
		if (key_eg == 'u'){
			key_num = chr.substr(i,(chr.length-1));			
			if((key_num < "AC00") || (key_num > "D7A3")) { 
				event.returnValue = false;
			} 			
		}
	}
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false;
	}
}
function Comma(num) {
	re = /(\d+)/;
	if(re.test(num)){ 
		re.exec(num); num = RegExp.$1; 
		re = /(\d+)(\d{3})/;
		while(re.test(num)){ num = num.replace(re,"$1,$2"); }
	}
    return (num);
}
function unComma(str) {
	return str.replace(/,/g,"");
}

//obj로받아 콤마 없애기
function unCommaObj(input) {
   var inputString = new String;
   var outputString = new String;
   var outputNumber = new Number;
   var counter = 0;
   if (input == '')
   {
	return 0
   }
   inputString=input;
   outputString='';
   for (counter=0;counter <inputString.length; counter++)
   {
      outputString += (inputString.charAt(counter) != ',' ?inputString.charAt(counter) : '');
   }
   outputNumber = parseFloat(outputString);
   return (outputNumber);
}
//결재선 지정지정 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//결재 재상신 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("결재선을 입력하십시요."); return; }
	else if(eForm.bistrip_city.value == "") { alert("출장지 도시명을 입력하십시요."); return; }
	else if(eForm.purpose.value == "") { alert("출장목적을 입력하십시요."); return; }

	if(eForm.bistrip_kind.value =="국외") {
		if (eForm.bistrip_country.value =="") { alert("출장지 국가명을 입력하십시요."); return; }
	} else if(eForm.bistrip_kind.value =="국내"){
		if (eForm.doc_tel.value =="") { alert("긴급연락처를 입력하십시요."); return; }
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
	if(decision == 0) { alert("승인자가 빠졌습니다"); return; }

	//제목구하기
	var purpose = document.eForm.purpose.value;
	var doc_sub = "출장신청서 : "+purpose;

	//pjt_code구하기
	prj_code = document.eForm.project_code.value+":"+document.eForm.prj_name.value;

	document.onmousedown=dbclick;
	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/GeunTaeServlet';
	document.eForm.mode.value='R_CHULJANG_SINCHEONG';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.prj_code.value=prj_code;
	document.eForm.submit();
}

//결재 재 임시보관
function eleApprovalTemp()
{
	//제목구하기
	var purpose = document.eForm.purpose.value;
	var doc_sub = "출장신청서 : "+purpose;

	//pjt_code구하기
	prj_code = document.eForm.project_code.value+":"+document.eForm.prj_name.value;

	document.onmousedown=dbclick;
	document.eForm.action="../../../servlet/GeunTaeServlet";
	document.eForm.mode.value='R_CHULJANG_SINCHEONG_TMP';
	document.eForm.app_mode.value='TMP';	
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.prj_code.value=prj_code;
	document.eForm.submit();
	
}

function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>
