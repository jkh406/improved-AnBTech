<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "AS실적 수정(업체용)"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mr.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%

	//---------------초기화 선언 --------------------------//
	com.anbtech.date.anbDate anbdt = new anbDate();
	String pid="",register_no="",register_date="",as_field="",code="",request_name="",serial_no="";
	String request_date="",as_date="",as_type="",as_content="",as_result="",as_delay="",as_issue="";
	String worker="",company_no="";
	//----------------------------------------------------
	//	실적정보 LIST읽기 : as_result
	//----------------------------------------------------
	com.anbtech.mr.entity.assupportTable work;
	ArrayList work_list = new ArrayList();
	work_list = (ArrayList)request.getAttribute("WORK_List");
	work = new assupportTable();
	Iterator work_iter = work_list.iterator();
	
	if(work_iter.hasNext()) {
		work = (assupportTable)work_iter.next();

		pid = work.getPid();
		register_no = work.getRegisterNo();	
		register_date = work.getRegisterDate();
		as_field = work.getAsField();
		code = work.getCode();
		request_name = work.getRequestName();
		serial_no = work.getSerialNo();
		request_date = work.getRequestDate();
		as_date = work.getAsDate();
		as_type = work.getAsType();
		as_content = work.getAsContent();	
		as_result = work.getAsResult();
		as_delay = work.getAsDelay();
		as_issue = work.getAsIssue();
		worker = work.getWorker();	
		company_no = work.getCompanyNo();	
	}

	//---------------------------------------------------------
	//	AS지원목록 정보
	//--------------------------------------------------------
	com.anbtech.mr.entity.assupportTable field;
	ArrayList field_list = new ArrayList();
	field_list = (ArrayList)request.getAttribute("FIELD_List");
	field = new assupportTable();
	Iterator field_iter = field_list.iterator();
	int field_cnt = field_list.size();

	String[][] asfield = new String[field_cnt][6];
	int e = 0;
	while(field_iter.hasNext()) {
		field = (assupportTable)field_iter.next();
		asfield[e][0] = field.getRegisterName();		//지원이름
		asfield[e][1] = field.getSno();					//지원약속표기
		e++;
	}

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../mr/css/style.css" type="text/css">
<script language='javascript'>
<!--
//실적입력하기
function modifyResult()
{
	var request_name = sForm.request_name.value;
	if(request_name.length == 0) { alert("요청자가 입력되지 않았습니다."); return; }

	//요청일자와 지원일자를 비교하여 1일이내지원인지 파악하기
	var request_date = sForm.request_date.value;
	var as_date = sForm.as_date.value;
	if(request_date > as_date) { alert('방문일자가 요청일자보다 빠릅니다.'); return; }

	var as_content = sForm.as_content.value;
	if(as_content.length == 0) { alert("A/S내용이 입력되지 않았습니다."); return; }
	
	//지원이 2일이상이면 지연사유 입력검사
	if(compdate(request_date,as_date)) {
		var as_delay = sForm.as_delay.value;
		if(as_delay.length == 0) { 
			alert('요청일과 방문일이 2일이상이면 \n\n지연사유를 입력하셔야 합니다.'); return; }
	}
	
	//요청일,지원일날자에서 - 삭제
	for(i=0; i<2; i++) request_date = request_date.replace('-','');
	for(i=0; i<2; i++) as_date = as_date.replace('-','');

	document.sForm.action='../servlet/asresultworkServlet';
	document.sForm.mode.value='ART_M';
	document.sForm.request_date.value=request_date;
	document.sForm.as_date.value=as_date;
	document.sForm.submit();
}
//입력취소하기
function cancelResult()
{
	document.sForm.action='../servlet/asresultworkServlet';
	document.sForm.mode.value='ART_L';
	document.sForm.submit();
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../mr/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}

//날자비교하기
function compdate(a,b)
{
	//a에서 +2일을 한후 Date을 다시 Setting하기
	var syear	= a.substring(0, 4);			
    var smonth	= a.substring(5, 7) ;			
    var sday	= a.substring(8, 10);			
		sday = eval(eval(sday)+2);			//2일을 초과하는지 판단하기 위해
	var days = DaysInMonth(eval(smonth),syear);
	if(sday > days) {
		smonth = eval(eval(smonth)+1);
		sday = eval(sday - days);			//일 구하기
		if(smonth > 12) {
			smonth = eval(smonth - 12);		//월 구하기
			syear = eval(syear + 1);		//년 구하기
		}
	}
    now = new Date();
    now.setTime(Date.UTC(syear, smonth, sday))

	//setting된 날자구하기
	var nyear	= now.getYear();
	var nmonth	= now.getMonth();	if(nmonth < 10) nmonth = "0"+nmonth;
	var nday	= now.getDate();	if(nday < 10) nday = "0"+nday;
	var c = nyear+nmonth+nday;

	//b 와 c 비교하기
	for(i=0; i<2; i++) b=b.replace('-','');
	if(b >= c) return true; 
	else return false;
}
//윤년을 포함하여 각 월의 날 수 계산하는 함수
function DaysInMonth(WhichMonth, WhichYear)
{
  var DaysInMonth = 31;
  if (WhichMonth == "4" || WhichMonth == "6" || WhichMonth == "9" || WhichMonth == "11") DaysInMonth = 30;
  if (WhichMonth == "2" && (WhichYear/4) != Math.floor(WhichYear/4))	DaysInMonth = 28;
  if (WhichMonth == "2" && (WhichYear/4) == Math.floor(WhichYear/4))	DaysInMonth = 29;
  return DaysInMonth;
}
-->
</script>
</head>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../mr/images/blet.gif"> A/S실적수정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300><a href="javascript:modifyResult();"><img src="../mr/images/bt_modify.gif" border="0" align="absmiddle"></a> <a href="javascript:cancelResult();"><img src="../mr/images/bt_cancel.gif" border="0" align="absmiddle"></a></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<form method=post name="sForm" style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">작성자</td>
           <td width="37%" height="25" class="bg_04"><%=worker%>
				<input type="hidden" name="worker" value='<%=worker%>'></td>
		   <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">관리코드</td>
           <td width="37%" height="25" class="bg_04"><%=company_no%>
				<input type="hidden" name="company_no" value='<%=company_no%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">지원분야</td>
           <td width="37%" height="25" class="bg_04">
				<select name="as_field" style=font-size:9pt;color="black";>  
				<%
					String sel = "";
					for(int si=0; si<field_cnt; si++) {
						if(as_field.equals(asfield[si][1])) sel = "selected";
						else sel = "";
						out.println("<option "+sel+" value='"+asfield[si][1]+"'>"+asfield[si][0]+"</option>");
					}
				%>
				</select></td>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">등록일시</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
		    <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">요청자</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='request_name' value='<%=request_name%>'></td>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">요청일</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='request_date' value='<%=request_date%>' size=10 readonly> <A Href="Javascript:OpenCalendar('request_date');"><img src="../mr/images/bt_calendar.gif" border="0" align='absmiddle'></A></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">지원형태</td>
           <td width="37%" height="25" class="bg_04">
		   <%	String[] tno={"1","2"};
				String[] tna={"방문지원","유선지원"};
				String chk = "";
				for(int i=0; i<2; i++) {
					if(as_type.equals(tno[i])) chk="checked";
					else chk="";
					out.println("<input type='radio' "+chk+" name='as_type' value='"+tno[i]+"'>"+tna[i]);	
				}
			%></td>
		   <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">처리여부</td>
           <td width="37%" height="25" class="bg_04">
		   <%	String[] rno={"1","2"};
				String[] rna={"완료","미처리"};
				String rchk = "";
				for(int i=0; i<2; i++) {
					if(as_result.equals(tno[i])) rchk="checked";
					else rchk="";
					out.println("<input type='radio' "+rchk+" name='as_result' value='"+rno[i]+"'>"+rna[i]);	
				}
			%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">장비일련번호</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='serial_no' value='<%=serial_no%>'> [S/W는 제외]</td>
		   <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">방문일</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='as_date' value='<%=as_date%>' size=10 readonly> <A Href="Javascript:OpenCalendar('as_date');"><img src="../mr/images/bt_calendar.gif" border="0" align='absmiddle'></A></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">A/S내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<textarea rows="9" name="as_content" cols="93" style="background:#FFFFFF;border:1 solid #787878;"><%=as_content%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">이슈사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<textarea rows="6" name="as_issue" cols="93" style="background:#FFFFFF;border:1 solid #787878;"><%=as_issue%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">지연사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<textarea rows="5" name="as_delay" cols="93" style="background:#FFFFFF;border:1 solid #787878;"><%=as_delay%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
</td></tr></table>
<input type="hidden" name="mode" value="">
<input type="hidden" name="pid" value="<%=pid%>">
<input type="hidden" name="register_no" value=""> 
<input type="hidden" name="register_date" value=""> 
<input type="hidden" name="code" value="C02">			<!-- 수정부위 -->
<input type="hidden" name="value_request" value="0">
</form>

</body>
</html>
