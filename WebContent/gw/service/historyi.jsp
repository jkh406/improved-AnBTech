<%@ include file="../../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	contentType = "text/html; charset=euc-kr"
	import		= "com.anbtech.date.anbDate"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String query = "";
	String caption = "";
	String j		=	request.getParameter("j")==null?"a":request.getParameter("j");
	String ah_id	=	request.getParameter("ah_id");
	String ap_id = "";	// 고객회사 ID
	String au_id = "";	// 작성자 ID
	String at_id = "";	// 고객 ID

	String rank = "";
	String s_day = "";
	String s_time = "";
	String category = "";
	String subject = "";
	String content	= "";
	String result	= "";
	String other_customer = "";
	String file_name = "";
	String file_size = "";
	String umaks = "";


	if( j.equals("u")){ // 수정
		query = "select a.*,b.ap_id,c.at_id,c.rank from history_table a,company_table b,customer_table c where a.ah_id = '"+ah_id+"' and a.ap_id = b.ap_id and a.at_id = c.at_id";
		bean.openConnection();	
		bean.executeQuery(query);
		while(bean.next()){
			ap_id = bean.getData("ap_id"); // 회사ID
			at_id = bean.getData("at_id"); // 고객ID
			rank	= bean.getData("rank");// 고객직급
			s_day	= bean.getData("s_day");
			s_time	= bean.getData("s_time");
			category = bean.getData("class");
			subject = bean.getData("subject");
			content = bean.getData("content");
			result	= bean.getData("result");
			other_customer = bean.getData("other_customer");
			file_name	= bean.getData("file_name");
			file_size	= bean.getData("file_size");
			umaks	= bean.getData("umask");
		}
		caption		 = "수정";
	}else if(j.equals("a")){
		caption = "추가";
	}

	/*****************************************************
	// 금일의 년월일 및 시각 구하기 
	*****************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();

	//년월일 구하기
	String tD = request.getParameter("DAY");	//from calendar_view.jsp 달력의 날자를 누르면
	String toDay;
	if(tD == null)
		toDay = anbdt.getDate(0);
	else toDay = tD;
	
	//현재시 구하기
	String hrs = anbdt.getHours();			//HH

%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0' oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 고객서비스이력 등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="javascript:checkForm();"><img src="../images/bt_reg.gif" border="0"></a></TD>
			  <TD align=left width=30><a href="javascript:history.go(-1);"><img src="../images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!--내용-->
<form name="frm1" method="post" action="historyp.jsp" encType="multipart/form-data" style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--일자,회사-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../images/basic_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">등록자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size="15" value="<%=login_name%>(<%=login_id%>)" readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문일자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="s_day" size="10" value="<%=toDay%>" readOnly> <a href="Javascript:OpenCalendar('s_day');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문회사명</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" name="ap_name" size="30" readOnly class="text_01"> <a href="javascript:searchCompany();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a><input type="hidden" name="ap_id" value=''></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!-- 방문1 시작 --> 
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../images/history1.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문시간</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="s_time1_s" class="text_01">
				<%
				String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL = "";
					for(int asH=0; asH<24; asH++){
						if(asH == Integer.parseInt(hrs)) msSEL = "SELECTED"; else msSEL="";
						out.println("<OPTION value=" + asHour[asH] + ":" + "00 " + msSEL + ">" + asHour[asH] + ":" + "00");
						out.println("<OPTION value=" + asHour[asH] + ":" + "30"+">" + asHour[asH] + ":" + "30");
					}
					out.println("</SELECT> ~ ");
				%>	
				<SELECT NAME="s_time1_e" class="text_01">
				<%
				String[] aeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String meSEL = "";
					for(int aeH=0; aeH<24; aeH++){
						if(aeH == Integer.parseInt(hrs)) meSEL = "SELECTED"; else meSEL="";
						out.println("<OPTION value=" + aeHour[aeH] + ":" + "00 " + meSEL + ">" + aeHour[aeH] + ":" + "00");
						out.println("<OPTION value=" + aeHour[aeH] + ":" + "30"+ ">" + aeHour[aeH] + ":" + "30");
					}
				%></SELECT>

				<input type='hidden' name='s_time1'></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">고객명</td>
           <td width="37%" height="25" class="bg_04"><input type="hidden" name="at_id1"><input type="text" name="at_name1" size="15" value="" readOnly class="text_01"> <a href="javascript:chooseCustomer('1');"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문목적/제목</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<select size="1" name="category1" class="text_01">
					<option value="">분류선택</option>
					<option value="A/S">A/S</option>
					<option value="상담">상담</option>
					<option value="견적">견적</option>
					<option value="설계">설계</option>
					<option value="제작">제작</option>
					<option value="교육(세미나)">교육(세미나)</option>
					<option value="도입">도입</option>
				</select> <input type="text" name="subject1" size="40" class="text_01">
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="7" name="content1" cols="80" class="text_01"></textarea></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">이슈사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="3" name="result1" cols="80"></textarea></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">파일첨부</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="file" size="40" name=file1></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table><a href="javascript:show_div1(document.all.visit2);"><img src="../images/bt_history2.gif" border="0" align="absmiddle"></a>

	<div id="visit2" style="position:relative; width:100%; height:30; z-index:1; visibility: hidden">
    <!-- 방문2 시작 --> 
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="3"><img src="../images/history2.gif" width="209" height="25" border="0"></td>
		   <td height="25" align='right'><a href="javascript:hide_div1(document.all.visit2);"><img src='../images/b_del.gif' border='0' alt='닫기'></a></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문시간</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="s_time2_s" class="text_01">
				<%
					String[] asHour2 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String msSEL2 = "";
						for(int asH=0; asH<24; asH++){
							if(asH == Integer.parseInt(hrs)) msSEL2 = "SELECTED"; else msSEL2="";
							out.println("<OPTION value=" + asHour2[asH] + ":" + "00 " + msSEL2 + ">" + asHour2[asH] + ":" + "00");
							out.println("<OPTION value=" + asHour2[asH] + ":" + "30"+">" + asHour2[asH] + ":" + "30");
						}
						out.println("</SELECT> ~ ");
				%>	
				<SELECT NAME="s_time2_e" class="text_01">
				<%
					String[] aeHour2 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String meSEL2 = "";
						for(int aeH=0; aeH<24; aeH++){
							if(aeH == Integer.parseInt(hrs)) meSEL2 = "SELECTED"; else meSEL2="";
							out.println("<OPTION value=" + aeHour2[aeH] + ":" + "00 " + meSEL2 + ">" + aeHour2[aeH] + ":" + "00");
							out.println("<OPTION value=" + aeHour2[aeH] + ":" + "30"+ ">" + aeHour2[aeH] + ":" + "30");
						}
				%></SELECT>

				<input type='hidden' name='s_time2'></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">고객명</td>
           <td width="37%" height="25" class="bg_04"><input type="hidden" name="at_id2"><input type="text" name="at_name2" size="15" value="" readOnly class="text_01"> <a href="javascript:chooseCustomer('2');"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문목적/제목</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<select size="1" name="category2" class="text_01">
					<option value="">분류선택</option>
					<option value="A/S">A/S</option>
					<option value="상담">상담</option>
					<option value="견적">견적</option>
					<option value="설계">설계</option>
					<option value="제작">제작</option>
					<option value="교육(세미나)">교육(세미나)</option>
					<option value="도입">도입</option>
				</select> <input type="text" name="subject2" size="40" class="text_01">
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="10" name="content2" cols="80" class="text_01"></textarea></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">이슈사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" name="result2" cols="80"></textarea></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">파일첨부</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="file" size="40" name=file2></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table><a href="javascript:show_div2(document.all.visit3);"><img src="../images/bt_history3.gif" border="0" align="absmiddle"></a></div>

	<div id="visit3" style="position:relative; width:100%; height:30; z-index:1; visibility: hidden">
    <!-- 방문3 시작 --> 
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="3"><img src="../images/history3.gif" width="209" height="25" border="0"></td>
		   <td height="25" align='right'><a href="javascript:hide_div2(document.all.visit3);"><img src='../images/b_del.gif' border='0' alt='닫기'></a></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문시간</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="s_time3_s" class="text_01">
				<%
					String[] asHour3 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String msSEL3 = "";
						for(int asH=0; asH<24; asH++){
							if(asH == Integer.parseInt(hrs)) msSEL3 = "SELECTED"; else msSEL3="";
							out.println("<OPTION value=" + asHour3[asH] + ":" + "00 " + msSEL3 + ">" + asHour3[asH] + ":" + "00");
							out.println("<OPTION value=" + asHour3[asH] + ":" + "30"+">" + asHour3[asH] + ":" + "30");
						}
						out.println("</SELECT> ~ ");
				%>	
				<SELECT NAME="s_time3_e" class="text_01">
				<%
					String[] aeHour3 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String meSEL3 = "";
						for(int aeH=0; aeH<24; aeH++){
							if(aeH == Integer.parseInt(hrs)) meSEL3 = "SELECTED"; else meSEL3="";
							out.println("<OPTION value=" + aeHour3[aeH] + ":" + "00 " + meSEL3 + ">" + aeHour3[aeH] + ":" + "00");
							out.println("<OPTION value=" + aeHour3[aeH] + ":" + "30"+ ">" + aeHour3[aeH] + ":" + "30");
						}
				%></SELECT>

				<input type=hidden name='s_time3'></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">고객명</td>
           <td width="37%" height="25" class="bg_04"><input type="hidden" name="at_id3"><input type="text" name="at_name3" size="15" value="" readOnly class="text_01"> <a href="javascript:chooseCustomer('3');"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문목적/제목</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<select size="1" name="category3" class="text_01">
					<option value="">분류선택</option>
					<option value="A/S">A/S</option>
					<option value="상담">상담</option>
					<option value="견적">견적</option>
					<option value="설계">설계</option>
					<option value="제작">제작</option>
					<option value="교육(세미나)">교육(세미나)</option>
					<option value="도입">도입</option>
				</select> <input type="text" name="subject3" size="40" class="text_01">
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="10" name="content3" cols="80" class="text_01"></textarea></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">이슈사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" name="result3" cols="80"></textarea></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">파일첨부</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="file" size="40" name=file3></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table></div>
</td></tr></table>		 
<input type=hidden name=j value='<%=j%>'>
<input type=hidden name=ah_id value='<%=ah_id%>'>
<input type=hidden name=au_id value='<%=login_id%>'>
<input type=hidden name=visit_count value='1'>
</form>
</body>
</html>

<script language=javascript>

function chooseCustomer(no){
	var sel = document.frm1.ap_id;
	var ap_id;
	var name;

	if(sel.value==''){
		alert("고객사를 먼저 선택하십시오.");
		return;
	}else{
		ap_id = sel.value;

		if(no == 1) name = document.frm1.at_name1.value;
		if(no == 2) name = document.frm1.at_name2.value;
		if(no == 3) name = document.frm1.at_name3.value;
	
		wopen("chooseCustomer.jsp?no="+no+"&ap_id="+ap_id,"search_customer",'500','325','scrollbars=no,toolbar=no,status=no,resizable=no');
	}
}

function show_div1(divid){ 
	document.frm1.visit_count.value = "2";
	divid.style.visibility = 'visible';
}

function hide_div1(divid){ 
	if(document.frm1.visit_count.value == "3"){
		alert("이력정보 3을 먼저 닫은 후 닫을 수 있습니다.");
	}
	else{
		document.frm1.visit_count.value = "1";
		divid.style.visibility = 'hidden'; 
	}
} 

function show_div2(divid){ 
	document.frm1.visit_count.value = "3";
	divid.style.visibility = 'visible'; 
}

function hide_div2(divid){ 
	document.frm1.visit_count.value = "2";
	divid.style.visibility = 'hidden'; 
} 

function OpenCalendar(where) {
	var strUrl = "Calendar.jsp?where="+where;
	wopen(strUrl,"calendar",'180','250','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function checkForm()
{
	var f = document.frm1;

	if(f.ap_id.value == ""){
			alert("대상 고객사를 선택하십시오..");
			return;
	}

	
	if(f.at_id1.value == ""){
			alert("대상 고객을 선택하십시오");
			//f.at_id1.focus();
			return;
	}


	if(f.category1.value == ""){
			alert("분류를 선택하십시오.");
			f.category1.focus();
			return;
	}
	if(f.subject1.value == ""){
			alert("방문 목적을 입력하십시오.");
			f.subject1.focus();
			return;
	}
	if(f.content1.value == ""){
			alert("방문 내용을 입력하십시오.");
			f.content1.focus();
			return;
	}

	if(f.visit_count.value == "2")
	{
		if(f.at_id2.value == ""){
			alert("대상 고객을 선택하십시오.");
			//f.at_id1.focus();
			return;
		}
		if(f.category2.value == ""){
			alert("분류를 선택하십시오.");
			f.category2.focus();
			return;
		}
		if(f.subject2.value == ""){
			alert("방문 목적을 입력하십시오.");
			f.subject2.focus();
			return;
		}
		if(f.content2.value == ""){
			alert("방문 내용을 입력하십시오.");
			f.content2.focus();
			return;
		}	
	}
	if(f.visit_count.value == "3")
	{
		if(f.at_id3.value == ""){
			alert("대상 고객을 선택하십시오.");
			//f.at_id1.focus();
			return;
		}
		if(f.category3.value == ""){
			alert("분류를 선택하십시오.");
			f.category3.focus();
			return;
		}
		if(f.subject3.value == ""){
			alert("방문 목적을 입력하십시오.");
			f.subject3.focus();
			return;
		}
		if(f.content3.value == ""){
			alert("방문 내용을 입력하십시오.");
			f.content3.focus();
			return;
		}	
	}

	var tmp = document.frm1.s_day;
    var fromField=tmp.value.split("/")
    var syear	= fromField[0]
    var smonth	= fromField[1]
    var sday	= fromField[2]

//	if(smonth.length == 1) {smonth="0"+smonth}
//  if(sday.length == 1) {sday="0"+sday}

	tmp.value = syear+smonth+sday;

	var t = document.frm1;
	t.s_time1.value = t.s_time1_s.value + "/" + t.s_time1_e.value;
	t.s_time2.value = t.s_time2_s.value + "/" + t.s_time2_e.value;
	t.s_time3.value = t.s_time3_s.value + "/" + t.s_time3_e.value;

	document.onmousedown=dbclick;
	f.submit();

}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}

function searchCompany(){
//	window.open("searchCompany.jsp","chooseCustomer","width=630,height=400,scrollbar=auto,toolbar=no,status=no,resizable=no");
	wopen("../../crm/company/searchCompany.jsp?sf=frm1&sid=ap_id&sname=ap_name","search_company",'600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
</script>
