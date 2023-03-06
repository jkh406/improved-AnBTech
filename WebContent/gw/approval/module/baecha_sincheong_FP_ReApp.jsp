<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "배차신청서 2차부서 결재상신"		
	contentType = "text/html; charset=euc-kr" 
	errorPage	= "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기
	AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(); //전자결재내용 & 결재선

	//배차신청서 내용관련
	String query = "";
	String c_id = "";				//차량관리번호
	String v_no = "";				//차량번호
	String v_model = "";			//차량기종
	String in_date = "";			//신청일자
	String wyear = "";				//작성년
	String wmonth = "";				//	  월
	String wdate = "";				//	  일
	String ac_name = "";			//소속부서명
	String user_name = "";			//사용자 명
	String user_rank = "";			//사용자 직급
	String fellow_names = "";		//동행자	 사번/이름;
	String f_names = "";			//동행자	 이름,
	String u_year = "";				//배차요청시작 년
	String u_month = "";			//배차요청시작 월
	String u_date = "";				//배차요청시작 일
	String u_time = "";				//배차요청시작 시
	String tu_year = "";			//배차요청종료 년
	String tu_month = "";			//배차요청종료 월
	String tu_date = "";			//배차요청종료 일
	String tu_time = "";			//배차요청종료 시
	String purpose = "";			//사유
	String cr_dest = "";			//행선지
	String content = "";			//업무내용
	String em_tel = "";				//긴급연락처

	//결재선 관련
	String doc_id = "";				//전자결재 관리번호
	String link_id = "";			//관련문서 관리번호
	String line="";					//읽은문서 결재선
	String r_line = "";				//재작성으로 넘겨주기
	String vdate = "";				//검토자 검토 일자
	String ddate = "";				//승인자 승인 일자
	String wid = "";				//기안자사번
	String vid = "";				//검토자사번
	String did = "";				//승인자사번
	String wname = "";				//기안자
	String vname = "";				//검토자
	String dname = "";				//승인자
	String PROCESS = "";			//PROCESS
	String doc_ste = "";			//doc_ste

	//2차측 결재 관련
	String line2="";				//읽은문서 결재선
	String writer_id = "";			//등록자 사번
	String writer_name = "";		//등록자 명

	/*********************************************************************
	 	등록자(login) 알아보기
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//등록자 사번
		writer_name = bean.getData("name");				//등록자 명
	} //while

	//*********************************************************************
	// 2차 결재상신할 관련문서 관리번호 내용 받기
	//*********************************************************************
	link_id = request.getParameter("link_id");	if(link_id == null) link_id = "";	//관련문서 관리번호

	//*********************************************************************
	// 1차 주관부서 결재선 내용 받기
	//*********************************************************************
	masterDAO.getTable_MasterPid(link_id);	
	TableAppLine rline = new com.anbtech.gw.entity.TableAppLine();	
	ArrayList table_line = new ArrayList();				//결재선
	table_line = masterDAO.getTable_line();		
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		rline = (TableAppLine)line_iter.next();
										
		if(rline.getApStatus().equals("기안")) {
			wname = rline.getApName();	//기안자
			wid = rline.getApSabun();	//기안자 사번
		}
		if(rline.getApStatus().equals("검토"))  {
			vname = rline.getApName();	//검토자
			vid = rline.getApSabun();	//검토자 사번
			vdate = rline.getApDate();	//검토자 검토일자 (있으면 결재하고 없으면 결재않됨)
		}
		if(rline.getApStatus().equals("승인"))  {
			dname = rline.getApName();	//승인자
			did = rline.getApSabun();	//승인자 사번
			ddate = rline.getApDate();	//승인자 승인일자 (있으면 결재하고 없으면 결재않됨)\
		}
			
		line += rline.getApStatus()+" "+rline.getApSabun()+" "+rline.getApName()+" "+rline.getApRank()+" "+rline.getApDivision()+" "+rline.getApDate()+" "+rline.getApComment()+"<br>";
	}

	/*********************************************************************
	// 	배차 정보 알아보기
	*********************************************************************/	
	String[] Column = {"c_id","in_date","ac_name","user_name","user_rank","fellow_names",
				"u_year","u_month","u_date","u_time","tu_year","tu_month","tu_date","tu_time",
				"cr_purpose","cr_dest","content","em_tel"};
	bean.setTable("charyang_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (cr_id ='"+link_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	while(bean.isAll()) {
		c_id = bean.getData("c_id");					//차량관리번호
		in_date = bean.getData("in_date");				//신청일자
		ac_name = bean.getData("ac_name");				//소속부서명
		user_name = bean.getData("user_name");			//사용자 명
		user_rank = bean.getData("user_rank");			//사용자 직급
		fellow_names = bean.getData("fellow_names");	//동행자	 사번/이름;
		u_year = bean.getData("u_year");				//배차요청시작 년
		u_month = bean.getData("u_month");				//배차요청시작 월
		u_date = bean.getData("u_date");				//배차요청시작 일
		u_time = bean.getData("u_time");				//배차요청시작 시
		tu_year = bean.getData("tu_year");				//배차요청종료 년
		tu_month = bean.getData("tu_month");			//배차요청종료 월
		tu_date = bean.getData("tu_date");				//배차요청종료 일
		tu_time = bean.getData("tu_time");				//배차요청종료 시
		purpose = bean.getData("cr_purpose");			//사유
		cr_dest = bean.getData("cr_dest");				//행선지
		content = bean.getData("content");				//업무내용
		em_tel = bean.getData("em_tel");				//긴급 연락처
	} //while	

	//작성년월일 구하기
	wyear = in_date.substring(0,4);		//작성년
	wmonth = in_date.substring(5,7);	//	  월
	wdate = in_date.substring(8,10);	//	  일

	//동행자 이름만 구하기
	StringTokenizer names = new StringTokenizer(fellow_names,";");
	while(names.hasMoreTokens()) {
		String nms = names.nextToken();
		if(nms.length() < 3) break;
		
		StringTokenizer name = new StringTokenizer(nms,"/");
		int nm = 0;
		while(name.hasMoreTokens()) {
			String n = name.nextToken();
			if(nm == 1) f_names += n + ",";
			nm++;
			if(nm > 2) break;
		}
	}
	if(f_names.length() != 0) f_names = f_names.substring(0,f_names.length()-1);

	//차량 정보
	String[] carColumn = {"car_no","model_name"};
	bean.setTable("car_info");			
	bean.setColumns(carColumn);
	bean.setOrder("car_no ASC");	
	query = "where (cid ='"+c_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	while(bean.isAll()) {
		v_no = bean.getData("car_no");
		v_model = bean.getData("model_name");
	}

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/	
	line2 = request.getParameter("doc_app_line"); if(line2 == null) line2 = "";	//결재선
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
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 차량예약신청서</TD></TR></TBODY>
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
<form action="baecha_sincheong_FP_ReApp.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">메<p>모</TD>
		<TD noWrap width=64% align=left valign='top'><%=line%></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=36% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
						<img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
						if(vdate.length() == 0)	{//검토자
						if(ddate.length() == 0) out.println("&nbsp;");
							else out.println("전결");
						} else {
						out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
						}
					%>
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;
					<%
						if(ddate.length() == 0)	{//승인자
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
				%></TD>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">차량번호</td>
           <td width="37%" height="25" class="bg_04"><%=v_no%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">모델명</td>
           <td width="37%" height="25" class="bg_04"><%=v_model%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr><td height=5 colspan="4"></td></tr></tbody></table>

  <tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">예약일시</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=u_year%>년 <%=u_month%>월 <%=u_date%>일 <%=u_time%> ~  <%=tu_year%> 년<%=tu_month%>월 <%=tu_date%>일 <%=tu_time%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청사유</td>
           <td width="37%" height="25" class="bg_04"><%=purpose%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">행선지</td>
           <td width="37%" height="25" class="bg_04"><%=cr_dest%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">차량사용자</td>
           <td width="37%" height="25" class="bg_04"><%=user_rank%> <%=user_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">긴급연락처</td>
           <td width="37%" height="25" class="bg_04"><%=em_tel%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">동행자</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=f_names%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">기타사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=content%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr><td height=5 colspan="4"></td></tr></tbody></table>

  <tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청일자</td>
           <td width="37%" height="25" class="bg_04"><%=wyear%>년 <%=wmonth%>월 <%=wdate%>일</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청부서명</td>
           <td width="37%" height="25" class="bg_04"><%=ac_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr><td height=10 colspan="4"></td></tr></tbody></table>  
  </td></tr></table>


<!-- 2차결재정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<br>재<br>선</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line2%></TEXTAREA></TD>
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

<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='link_id' value='<%=link_id%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value=''>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
</form>   

</body>
</html>

<script language=javascript>
<!--
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
	if (document.eForm.doc_app_line.value =="") { alert("결재선을 입력하십시요."); return; }
	
	 //결재선 검사
	data = document.eForm.doc_app_line.value;		//결재선 내용
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
	var doc_sub = "배차신청서 : "+'<%=purpose%>';

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/ChaRyangServlet';
	document.eForm.mode.value='BAE_CHA_SEC';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
}

//출력하기
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//닫기
function winClose()
{
	window.returnValue='';
	self.close();
}
-->
</script>