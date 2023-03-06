<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info= "출장전도금정산서 작성"		
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
	normalFormat money = new com.anbtech.util.normalFormat("#,000");		//출력형식 (비용)
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
	String[][] btrip = new String[cnt][2];

	int ac_cnt = 0;
	while(bean.isAll()) {
		btrip[ac_cnt][0] = bean.getData("code");			//출장관리코드
		btrip[ac_cnt][1] = bean.getData("code_name");		//출장관리명
		ac_cnt++;
	} //while

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/	
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";	//결재선
	String change_factor = multi.getParameter("change_factor"); 
		if(change_factor == null) change_factor = "";								//변경사유
	String change_note = multi.getParameter("change_note"); 
		if(change_note == null) change_note = "";									//변경내용
	String rec = multi.getParameter("doc_receiver"); if(rec == null) rec = "";		//인수인계자
	String tel = multi.getParameter("doc_tel"); if(tel == null) tel = "";			//긴급연락처
	
	
	//비용금액과 산출내용을 배열에 담기
	int costcnt = btrip.length - 1;
	String[][] cost = new String[costcnt][5];
	String c_code = "";		//출장코드
	String c_cost = "";		//출장신청 비용
	String c_cont = "";		//출장신청 산출내용
	int sum = 0;			//출장신청 비용 합계

	String a_cost = "";		//출장정산 비용
	String a_cont = "";		//출장정산 산출내용
	int a_sum = 0;			//출장정산 비용 합계

	int d_sum = 0;			//출장정산차액 비용 합계

	for(int c=0,m=1; c < costcnt; c++,m++) {
		//출장신청금액
		c_code = "code"+m;
		c_cost = "cost"+m;
		c_cont = "cont"+m;
		cost[c][0] = multi.getParameter(c_code);	if(cost[c][0] == null) cost[c][0] = "";
		cost[c][1] = multi.getParameter(c_cost);	if(cost[c][1] == null) cost[c][1] = "0";
		cost[c][2] = multi.getParameter(c_cont);	if(cost[c][2] == null) cost[c][2] = "";
		
		//비용합계 계산하기
		cost[c][1] = str.repWord(cost[c][1],",","");	//콤마 없애
		try { sum += Integer.parseInt(cost[c][1]); } catch (Exception e) {cost[c][1] = "0";}
		
		//출장정산금액
		a_cost = "a_cost"+m;
		a_cont = "a_cont"+m;
		cost[c][3] = multi.getParameter(a_cost);	if(cost[c][3] == null) cost[c][3] = "0";
		cost[c][4] = multi.getParameter(a_cont);	if(cost[c][4] == null) cost[c][4] = "";
		
		//정산합계 계산하기
		cost[c][3] = str.repWord(cost[c][3],",","");	//콤마 없애
		try { a_sum += Integer.parseInt(cost[c][3]); } catch (Exception e) {cost[c][3] = "0";}
	}
	d_sum = a_sum - sum;		//정산 차액

	//out.print("sum : " + fmt.toDigits(sum));

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

<BODY topmargin="0" leftmargin="0" oncontextmenu="return true">
<form action="chuljanb_jeondo_FP_write.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 출장전도금정산서 작성</TD>
			  </TR></TBODY>
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
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">변경유무</td>
           <td width="37%" height="25" class="bg_04"> <input type="radio" name="change_yn" value='유'>유 <input type="radio" name="change_yn" value='무' checked>무</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">작성일자</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">변경사유</td>
           <td width="37%" height="25" class="bg_04"> <input size="40" type="text" name="change_factor" value='<%=change_factor%>'></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">변경내용</td>
           <td width="37%" height="25" class="bg_04"> <input size="40" type="text" name="change_note" value='<%=change_note%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
			<td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">일 정 별<br>업무실적</td>
			<td width="87%" height="25" class="bg_04" colspan='3'>
				<TABLE cellSpacing=2 cellPadding=0 width=100% border=0 bordercolordark=white bordercolorlight=#9CA9BA>
					<tr><td height=23 class=bg_05 width='20%' align='center'>부터</td>
						<td height=23 class=bg_05 width='20%' align='center'>까지</td>
						<td height=23 class=bg_05 width='20%' align='center'>출장지</td>
						<td height=23 class=bg_05 width='40%' align='center'>업무내용</td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
					<tr><td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="sdate1" value='' readonly><a href="Javascript:OpenCalendar('sdate1');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="edate1" value='' readonly><a href="Javascript:OpenCalendar('edate1');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="15" type="text" name="dest1" value=''></td>
						<td height=23 class=bg_04 width='40%' align='left'>
							<input size="35" type="text" name="note1" value=''></td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
					<tr><td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="sdate2" value='' readonly><a href="Javascript:OpenCalendar('sdate2');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="edate2" value='' readonly><a href="Javascript:OpenCalendar('edate2');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="15" type="text" name="dest2" value=''></td>
						<td height=23 class=bg_04 width='40%' align='left'>
							<input size="35" type="text" name="note2" value=''></td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
					<tr><td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="sdate3" value='' readonly><a href="Javascript:OpenCalendar('sdate3');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="edate3" value='' readonly><a href="Javascript:OpenCalendar('edate3');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="15" type="text" name="dest3" value=''></td>
						<td height=23 class=bg_04 width='40%' align='left'>
							<input size="35" type="text" name="note3" value=''></td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
					<tr><td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="sdate4" value='' readonly><a href="Javascript:OpenCalendar('sdate4');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="edate4" value='' readonly><a href="Javascript:OpenCalendar('edate4');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="15" type="text" name="dest4" value=''></td>
						<td height=23 class=bg_04 width='40%' align='left'>
							<input size="35" type="text" name="note4" value=''></td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
				</TABLE>
			</td>
		 </tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장신청문서</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input size=45 type="text" name="ref_name" class="text_01" readonly>
		   <a href="Javascript:searchRefDocument();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	</tbody></table>
<%	/************************************************************
	 출장비 청구금액및 정산금액 등록
	 ************************************************************/
%>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출 장 비<br>정 산</td>
           <td width="87%" class="bg_04" colspan="3" valign='top'>
			<%
				out.print("<TABLE cellSpacing=2 cellPadding=0 width=100% border=0 bordercolordark=white bordercolorlight=#9CA9BA>");
				out.print("<tr><td height=23 class=bg_05 width='20%' align='center'>항목</td>"); 
				out.print("<td class=bg_05 width='25%' align='center'>금 액</td>"); 
				out.print("<td class=bg_05 width='55%' align='center'>산출내역</td></tr>");

				for(int n=1,p=0; p < cnt; n++,p++) {
					out.print("<tr><td class=bg_07 align='center'><input type='hidden' name='code"+n+"' value='"+btrip[p][0]+"'>");
					out.print(btrip[p][1]+"</td>");
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='a_cost"+n+"' value='0' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);'></td>");//정산비용(금액)
					out.print("<td class=bg_07><input size=40 type='text' name='cont"+n+"' value=''></td></tr>");
				}
				out.print("<tr><td class=bg_07 align='center'><b>합 계</b></td>");
				out.print("<td class=bg_07><input class='money' size=15 type='text' name='a_sum' value='0' readonly></td>");
				out.print("<td class=bg_07>&nbsp;<b>당 초</b> <input class='money' size=15 type='text' name='sum' value='0' readonly>&nbsp;");
				out.print("<b>차 액</b> <input class='money' size=15 type='text' name='d_sum' value='0' readonly></td></tr>");
				out.print("</table>");
			%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">영수인</td>
           <td width="87%" height="25" class="bg_04" colspan='3'><input size=10 type="text" name="receiver_name" value='<%=receiver_name%>'> <a href="Javascript:searchReceiver();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>  
		 </tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>  
</td></tr></table>

<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value=''>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='ac_cnt' value='<%=ac_cnt%>'>
<input type='hidden' name='doc_chuljang' value='<%=doc_chuljang%>'>
<input type="hidden" name="receiver_id" value='<%=receiver_id%>'>
<input type="hidden" name="receiver_date" value='<%=anbdt.getDate()%>'>
<input type="hidden" name="ref_id" value=''>
<input type="hidden" name="plid" value=''>
</form>
</body>
</html>


<script language=javascript>
<!--
//출장코드 등록여부 확인하기
var code = '<%=doc_chuljang%>';
if(code.length == 0) {
	alert("출장코드가 등록되어있지 않습니다. 관리자에게 문의후 사용하십시요.");
}

//출장신청문서 찾기
function searchRefDocument()
{
	wopen("searchChuljang.jsp?target_id=eForm.ref_id&target_name=eForm.ref_name","ref_id","520","310","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//영수인 찾기
function searchReceiver()
{
	wopen("searchUser.jsp?target=eForm.receiver_id/eForm.receiver_name","proxy","250","380","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../Calendar.jsp?FieldName=" + FieldName;
	newWIndow = wopen(strUrl, "Calendar", "180", "250","scrollbars=no,toolbar=no,status=no,resizable=no");
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
	var sum = document.eForm.sum.value;
	sum = unComma(sum);
	var a_sum = 0;
	var d_sum = 0;
	for(i=0,j=1; i<n; i++,j++) {
		a_sum += unCommaObj(eval("document.eForm.a_cost"+j+".value"));
	}
	document.eForm.a_sum.value=Comma(a_sum);
	if(a_sum >= sum) document.eForm.d_sum.value=Comma(a_sum-sum);
	else document.eForm.d_sum.value="-"+Comma(a_sum-sum);
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
//결재 상신 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("결재선을 입력하십시오."); return; }
	if (eForm.ref_name.value =="") { alert("출장신청문서를 입력하십시오."); return; }
	
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
	var ref_name = document.eForm.ref_name.value;
	var doc_sub = "출장전도금정산서 : ["+ref_name+"]";

	document.onmousedown=dbclick;

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/GeunTaeServlet';
	document.eForm.mode.value='CHULJANG_JEONDO';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();

}

function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>
