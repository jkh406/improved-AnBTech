<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.br.entity.*,com.anbtech.date.anbDate"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	String year = request.getParameter("year");
	String month = request.getParameter("month");
	String day = request.getParameter("day");
	
	anbDate anbdt = new com.anbtech.date.anbDate();		
	String hrs = anbdt.getHours();	
	String cur = year + "/" + month + "/" + day;
	
	// 차량정보
	CarInfoTable table = new CarInfoTable();
	table = (CarInfoTable)request.getAttribute("Table");
	String cid			=table.getCid();
	String car_no		=table.getCarNo();
	String model_name	=table.getModelName();

	String query = "";
	String cr_id = "";				//관리코드
	String user_name = "";			//해당자 명
	String rank_code = "";			//해당자 직급code
	String user_rank = "";			//해당자 직급
	String div_id = "";				//해당자 부서명 관리코드
	String div_name = "";			//해당자 부서명
	String div_code = "";			//해당자 부서코드
	String write_name = "";

			
	// 현재 로긴자 정보 가져오기
	String fellow_names = request.getParameter("fellow_names"); 
	if(fellow_names == null) fellow_names = "";	
	String user_id = request.getParameter("user_id"); if(user_id == null) user_id = login_id;
		
	String[] idColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+user_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	
	bean.setSearchWrite(query);
	bean.init_write();
	int i=0;
	while(bean.isAll()) {
		user_name = bean.getData("name");				//해당자 명
		rank_code = bean.getData("ar_code");			//해당자 직급 code
		user_rank = bean.getData("ar_name");			//해당자 직급
		div_id = bean.getData("ac_id");					//해당자 부서명 관리코드
		div_name = bean.getData("ac_name");				//해당자 부서명 
		div_code = bean.getData("ac_code");				//해당자 부서코드
	} //while

	if(i<1){user_name=user_id+"/"+user_name;}
	i++;

	int ch;
	if(user_name.indexOf('/')>0){
		ch = user_name.indexOf('/');
		write_name = user_name.substring(ch+1);
	}
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../br/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../br/images/blet.gif"> 차량예약신청</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href="javascript:checkgosave();"><img src="../br/images/bt_save.gif" border="0"></a> <a href="javascript:history.go(-1)"><img src="../br/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<form method='post' name='eForm' enctype='multipart/form-data' action="../servlet/BookResourceServlet" style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--차량정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량번호</td>
           <td width="37%" height="25" class="bg_04"><%=car_no%></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">모델명</td>
           <td width="37%" height="25" class="bg_04"><%=model_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<!--신청정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">예약일시</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
			<input type=text name=sdate size=10 maxlength=10 value='<%=cur%>' readOnly class="text_01"> <a href="javascript:wopen('../br/car/Calendar.jsp?FieldName=sdate', '', 180, 250);"><img src='../br/images/bt_calendar.gif' border='0' align='absmiddle'></a>

		    <SELECT name="stime" class="text_01">
			<%
			String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL = "";
				for(int asH=0; asH<24; asH++){
					if(asH == Integer.parseInt(hrs)) msSEL = " SELECTED "; else msSEL="";
					out.println(" <OPTION " + msSEL + " value='"+asHour[asH]+":00'>" + asHour[asH] + ":" + "00");
					out.println(" <OPTION value='"+asHour[asH]+":30'>" + asHour[asH] + ":" + "30");
				}
				out.println("</SELECT> ~ ");
			%>

			<input type=text name=edate size=10 maxlength=10 value='<%=cur%>' readOnly class="text_01"> <a href="javascript:wopen('../br/car/Calendar.jsp?FieldName=edate', '', 180, 250)"><img src='../br/images/bt_calendar.gif' border='0' align='absmiddle'></a>

		    <SELECT name="etime" class="text_01">
			<%
			String[] asHour2 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL2 = "";
				for(int asH2=0; asH2<24; asH2++){
					if(asH2 == Integer.parseInt(hrs)) msSEL2 = " SELECTED "; else msSEL2="";
					out.println("<OPTION " + msSEL2 + " value='"+asHour2[asH2]+":00'>" + asHour2[asH2] + ":" + "00");
					out.println("<OPTION value='"+asHour2[asH2]+":30'>" + asHour2[asH2] + ":" + "30");
				}
				out.println("</SELECT>");
			%>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">신청사유</td>
           <td width="37%" height="25" class="bg_04"><input type=text name=cr_purpose size='30' maxlength="40" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">행선지</td>
           <td width="37%" height="25" class="bg_04"><input type=text name=cr_dest size=20 maxlength="20" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>

		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량사용자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="user_name" value='<%=user_name%>' onClick="javascript:searchUsers();" size="15" readOnly class="text_01"> <a href='javascript:searchUsers();'><img src='../br/images/bt_search.gif' border='0' align='absmiddle'></a><input type="hidden" name='user_id' value='<%=user_id%>'></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">긴급연락처</td>
           <td width="37%" height="25" class="bg_04"><input type=text name=em_tel size=20 maxlength="20" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">동행자</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type=text name=fellow_names size=50 readonly> <a href='javascript:searchFellows();'><img src='../br/images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">기타사항</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><textarea name="content" rows="3" cols="75" ></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
</td></tr></table>
	<input type="hidden" name="write_name" value='<%=write_name%>'>
	<input type="hidden" name='write_id' value='<%=user_id%>'>
	<input type='hidden' name='user_code' value='<%=rank_code%>'>
	<input type='hidden' name='user_rank' value='<%=user_rank%>'>
	<input type='hidden' name='ac_id' value='<%=div_id%>'>
	<input type='hidden' name='ac_code' value='<%=div_code%>'>
	<input type='hidden' name='ac_name' value='<%=div_name%>'>

	<input type='hidden' name='cr_id' value='<%=bean.getID()%>'>
	<input type='hidden' name='mode' value='lendingsave'>
	<input type='hidden' name='category' value='car'>
	<input type='hidden' name="tablename" value="charyang_master">
	<input type='hidden' name='cid' value='<%=cid%>'>
	<input type='hidden' name='flag' value="EN">
	<input type='hidden' name='flag2' value="EN">
</form>
</body></html>

<script language="javascript">

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//대상자 찾기
function searchUsers()
{

	var to = "eForm.user_name";
	wopen("../br/car/searchUser.jsp?target="+to,"search_user","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}


//동행자 찾기
function searchFellows()
{
	
	wopen("../br/car/searchFellows.jsp?target=eForm.fellow_names","proxy","510","467","scrollbar=yes,toolbar=no,status=no,resizable=no");

}

//날짜 포맷 변경
function changeFormat(from)
{
    var fromField=from.split("/")
    var syear	= fromField[0]
    var smonth	= fromField[1]
    var sday	= fromField[2]

	var to = syear + smonth + sday;
	return to;
}

function checkgosave(){
	var f=document.eForm
	var tsdate = f.sdate.value + f.stime.value;
	var tedate = f.edate.value + f.etime.value;

	// 3/22일 추가 수정 코드
	var w_stime = f.stime.value;
	var w_shour = w_stime.substring(0,2);
	var w_smin	= w_stime.substring(3,5);
	var w_etime = f.etime.value;
	var w_ehour = w_etime.substring(0,2);
	var w_emin	= w_etime.substring(3,5);
	//
	
	// 오늘날짜
	today = new Date();
	var c_month = today.getMonth()+1;
	var c_date = today.getDate();
	var h = today.getHours();
	var m = today.getMinutes();

	if(f.cr_purpose.value==""){
		alert("신청사유를 입력하십시오.");
		f.cr_purpose.focus();
		return;
	}

	if(f.cr_dest.value==""){
		alert("행선지를 입력하십시오.");
		f.cr_dest.focus();
		return;
	}

	if(f.user_name.value==""){
		alert("차량사용자를 찾아서 선택하십시오.");
		return;
	}

	if(f.em_tel.value==""){
		alert("긴급연락처를 입력하십시오.");
		f.em_tel.focus();
		return;
	}

	if(f.sdate.value=="" || f.stime.value==""){
		alert("예약일자를 선택하십시오.")
		return;
	}
	
	if(f.sdate.value=="" || f.stime.value==""){
		alert("반납일자를 선택하십시오.")
		return;
	}


	if(c_month < 10) c_month = "0" + c_month;
	if(c_date < 10) c_date = "0" + c_date;
		
	var cday = today.getYear()+""+c_month+""+c_date;

	if( changeFormat(f.sdate.value) < cday ) { // 날짜 비교 (검색날짜/현재날짜)
		alert("오늘 이전 날짜로 예약신청을 할 수 없습니다.");		
		return;
	}

	if(f.sdate.value==f.edate.value){
/*
		if(Number(w_shour) > Number(w_ehour) || Number(h) > Number(w_shour)){
			alert("시간 설정이 잘못되었습니다.");
			return;

		}
*/
		if(Number(w_shour) == Number(w_ehour)){
			if(Number(w_smin)>Number(w_emin)){
			alert("시작시간과 마침시간이 잘못되었습니다.");
			return;
			}
		}
/*		
		if(Number(h) == Number(w_shour)){
			if(Number(m)>Number(w_smin)){
				alert("현재 이전시간으로 예약신을을 할 수 없습니다.");
				return;
			}
		}
*/
	}

	// 날짜 포맷 변경
	f.sdate.value = changeFormat(f.sdate.value);
	f.edate.value = changeFormat(f.edate.value);

	document.eForm.submit()
}

</script>


