<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "연장근무신청서 재작성"		
	contentType = "text/html; charset=euc-kr" 		
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
	String read_worker = multi.getParameter("read_worker"); //연장근무자 명단을 받기위한 임시꼬리표
	String doc_id = multi.getParameter("doc_id");
	String old_id = multi.getParameter("old_id"); if(old_id == null) old_id = doc_id;	//최초로 넘겨받을때만
	
	//근무자 찾기
	String[][] works = new String[work_cnt][5]; //근무자사번,이름,내용,퇴실시간,부서장확인
	for(int i=0; i<work_cnt; i++) for(int j=0; j<5; j++) works[i][j] = "";
	if(read_worker != null) {
		String[] workColumn = {"worker_id","worker_name","content","close_time","cfm"};
		bean.setTable("janeup_worker");			
		bean.setColumns(workColumn);
		bean.setOrder("ju_cid ASC");	
		query = "where (ju_id ='"+doc_id+"')";
		bean.setSearchWrite(query);
		bean.init_write();
		
		int w = 0;
		while(bean.isAll()) {
			works[w][0] = bean.getData("worker_id");			//근무자사번
			works[w][1] = bean.getData("worker_name");		//이름
			works[w][2] = bean.getData("content");			//내용
			works[w][3] = bean.getData("close_time");		//퇴실시간
			works[w][4] = bean.getData("cfm");				//부서장확인
			w++;
		} //while
	}

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";		//결재선
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

	//근무자 찾기
	if(read_worker == null) {
		for(int n=0; n<work_cnt; n++) {
			String work_id = multi.getParameter("work_id"+n); if(work_id == null) work_id = "";
			String[] wColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
			bean.setTable("user_table a,class_table b,rank_table c");			
			bean.setColumns(wColumn);
			bean.setOrder("a.id ASC");	
			query = "where (a.id ='"+work_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
			bean.setSearchWrite(query);
			bean.init_write();

			while(bean.isAll()) {
				works[n][0] = work_id;							//근무자 사번
				works[n][1] = bean.getData("name");				//근무자 명
			} //while
			works[n][2] = multi.getParameter("content"+n); if(works[n][2] == null) works[n][2] = "";
			works[n][3] = multi.getParameter("close_time"+n); if(works[n][3] == null) works[n][3] = "";
			works[n][4] = multi.getParameter("cfm"+n); if(works[n][4] == null) works[n][4] = "";
		}
	}

	//휴일근무
	String job_kind = multi.getParameter("job_kind"); if(job_kind == null) job_kind = "";
	String hd_sel = "";
	String nd_sel = "";
	if(job_kind.equals("휴일근무")) hd_sel = "selected";
	else if(job_kind.equals("야간근무")) nd_sel = "selected";

	//식비지급확인
	String cost_prs = multi.getParameter("cost_prs"); if(cost_prs == null) cost_prs = "";
	
%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>연장근무신청서</title>
<LINK href="../../../gw/css/style.css" rel=stylesheet>
</head>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<center>
<form name="eForm" method="post" encType="multipart/form-data">

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='right' height=20>
		<div id="print" style="position:absolute;left:370px;top:60px;width:300px;height:10px;visibility:visible;">
			<a href="Javascript:eleApprovalManagerLineSelect();"><img src='../../../gw/img/button_line_call.gif' align='middle' border='0'></a> <!-- 결재선 -->
			<a href="Javascript:eleApprovalRequest();"><img src='../../../gw/img/button_sangshin.gif' align='middle' border='0'></a> <!-- 상신 -->
			<% if(user_id.equals(login_id)) { //대리등록시는 임시저장메뉴 없음 %>
			<a href="Javascript:eleApprovalTemp();"><img src='../../../gw/img/002_007_save.gif' align='middle' border='0'></a> <!-- 임시저장 -->
			<% } %>
			<a href='Javascript:winprint();'><img src='../../../gw/img/button_print.gif' align='middle' border='0'></a> <!-- 출력 -->
		</div>
	</td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='center' height=30><font size=3><b>연 장 근 무 신 청 서 </b></font></td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
	<tr><td width=100% align='left' height=20><img src='../../../gw/img/slink_logo.jpg' align='middle' border='0'></td></tr>
</table>

<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111" bgcolor='#F2F2F2'> 
	<tr>            
		<td width="20" height="96" rowspan=3 align="center">메<p>모</td>
		<td width="420" height="96" rowspan=3>
		<TEXTAREA NAME="doc_app_line" rows=6 cols=57 readOnly style="border:1px solid #787878;"><%=line%></TEXTAREA>
		</td>
		<td width="20" height="96" rowspan=3 align="center">결<p>재</td>                     
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">기 안</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">검 토</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">승 인</td>            
	</tr>          
	<tr height="26">         
		<td width="60" height="50" align="center">&nbsp;</td>   
		<td width="60" height="50" align="center">&nbsp;</td>   
		<td width="60" height="50" align="center">&nbsp;</td>   
	</tr>   
	<tr height="26">         
		<td width="60" height="24" align="center">&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;</td>   
	</tr>   
</table>
		
<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111"> 
	<tr>
		<td width="100" height="30" align="center" valign="middle">소 속 부 서</td>
		<td width="540" height="30" align="center" colspan=3>
			<input type="hidden" name="user_code" value='<%=rank_code%>'>
			<input type="hidden" name="user_rank" value='<%=user_rank%>'>
			<input type="hidden" name="user_id" value='<%=user_id%>'>
			<input type="hidden" name="user_name" value='<%=user_name%>'>
			<input type="hidden" name="div_id" value='<%=div_id%>'>
			<input type="hidden" name="div_code" value='<%=div_code%>'>
			<input type="hidden" name="div_name" value='<%=div_name%>'>&nbsp;&nbsp;<%=div_name%></td>
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle">연장 근무일</td>
		<td width="540" height="30" align="center" colspan=3>
		<%
			//시작 년도						
			out.println("&nbsp;<SELECT NAME='doc_syear'>");
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
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle">근 무 자</td>
		<td width="340" height="30" align="center" valign="middle">업무내용</td>
		<td width="100" height="30" align="center" valign="middle">퇴실시간</td>
		<td width="100" height="30" align="center" valign="middle">부서장확인</td>
	</tr>
	<tr>
		<td width="100" height="470" align="center" valign="middle">
			<table width='100' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%	//근무자
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='100' height='20' align='center' valign='middle'>");
					out.println("<input class='box' type='hidden' name='work_id"+i+"' value='"+works[i][0]+"'>");
					out.println("<input class='box' size='5' type='text' name='work_name"+i+"' value='"+works[i][1]+"'>");
					out.println("<a href=\"Javascript:searchUser('eForm.work_id"+i+"','eForm.work_name"+i+"');\">A</a>");
					out.println("<a href=\"Javascript:deleteUser('eForm.work_id"+i+"','eForm.work_name"+i+"');\">D</a>");
					out.println("</td></tr>");
				}
				%>
			</table>
		</td>
		<td width="340" height="470" align="center" valign="middle">
			<table width='340' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='340' height='20' align='center' valign='middle'>");
					out.println("<input class='box' size='45' type='text' name='content"+i+"' value='"+works[i][2]+"'></td>");
					out.println("</tr>");
				}
				%>
			</table>
		</td>
		<td width="100" height="470" align="center" valign="middle">
			<table width='100' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='100' height='20' align='center' valign='middle'>");
					out.println("<input class='box' size='8' type='text' name='close_time"+i+"' value='"+works[i][3]+"'></td>");
					out.println("</tr>");
				}
				%>
			</table>
		</td>
		<td width="90" height="470" align="center" valign="middle">
			<table width='90' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='90' height='20' align='center' valign='middle'>");
					out.println("<input class='box' size='8' type='text' name='cfm"+i+"' value='"+works[i][4]+"'></td>");
					out.println("</tr>");
				}
				%>
			</table>
		</td>
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle">분 류</td>
		<td width="340" height="30" align="center" valign="middle">
			<select name='job_kind'">
				<OPTION value='휴일근무' <%=hd_sel%> >휴일근무</OPTION>
				<OPTION value='야간근무' <%=nd_sel%> >야간근무</OPTION>
			</select></td>
		<td width="100" height="30" align="center" valign="middle">식비지급확인</td>
		<td width="100" height="30" align="center" valign="middle">
			<input class=box type="text" size=8 name="cost_prs" value='<%=cost_prs%>'></td>
	</tr>
	<tr>
		<td width="640" height="60" align="center" colspan=4><br><br>
			위와 같이 특별 근무를 신청합니다. <br><br>
			<%=anbdt.getYear()%> 년 <%=anbdt.getMonth()%> 월 <%=anbdt.getDates()%> 일 <br><br>
			신청인 : <%=user_name%> <br><br></td>
	</tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0'> 
	<tr>            
		<td width="50%" height="40" rowspan=3 align="left">SLCG-005-0</td> 
		<td width="50%" height="40" rowspan=3 align="right">A4(210x297mm)복사용지75g/m<sup>2</sup></td> 
	</tr>   
</table>
<% //서브릿 데이터 처리 %>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='work_cnt' value='<%=work_cnt%>'>
<input type='hidden' name='doc_sub' value='[연장근무신청서]'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='old_id' value='<%=old_id%>'>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
</form>  

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">데이터 처리중입니다. 잠시만 기다려 주십시요.</marquee>
	</td> 
	</tr>
</table>
</div>

</center>
</body>
</html>
<script language=javascript>
<!--
//근무자 찾기
function searchUser(a,b)
{
	window.open("searchUser.jsp?target="+a+"/"+b,"user","width=460","height=480","scrollbars=yes,toolbar=no,status=no,resizable=no");

}
//입력한 근무자 취소하기 
function deleteUser(a,b)
{
	var ask = confirm("선택한 근무자를 명단에서 삭제하시겠습니까?");
	if(ask == false) return;

	var id = eval("document."+a);
	id.value = "";
	var name = eval("document."+b);
	name.value = "";
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

	//처리중 메시지 출력
	document.all['lding'].style.visibility="visible";

	document.onmousedown=dbclick;  // 더블클릭 check

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/JanEupServlet';
	document.eForm.mode.value='R_YEONJANG';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();
}

//결재 임시보관
function eleApprovalTemp()
{
	//처리중 메시지 출력
	document.all['lding'].style.visibility="visible";

	document.onmousedown=dbclick;  // 더블클릭 check

	document.eForm.action="../../../servlet/JanEupServlet";
	document.eForm.mode.value='R_YEONJANG_TMP';
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
