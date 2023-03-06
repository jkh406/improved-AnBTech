<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkES02.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>

<html>
<head><title> </title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<form name='wForm' method='get' action='../../servlet/GeunTaeServlet'>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif"> 출퇴근시각변경</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32>
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href='javascript:save();'><img src="../images/bt_save.gif" align='absmiddle' border='0'></a> <a href='javascript:history.go(-1);'><img src="../images/bt_cancel.gif" align='absmiddle' border='0'></a>
			  </TD></TR></TBODY></TABLE></TD></TR>
			  <TR><TD height='1' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!--내용-->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr>
			<td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">출퇴근구분</td>
			<td width="80%" height="25" class="bg_04"><input type='radio' name='mode' value='chk_in' checked>출근 <input type='radio' name='mode' value='chk_out'>퇴근</td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
		<tr>
			<td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">출퇴근일시</td>
			<td width="80%" height="25" class="bg_04">
					<SELECT name='y'>
						<OPTION value="2004">2004년</OPTION>
						<OPTION value="2005">2005년</OPTION>
						<OPTION value="2006">2006년</OPTION>
						<OPTION value="2007">2007년</OPTION>
						<OPTION value="2008">2008년</OPTION>
						<OPTION value="2009">2009년</OPTION>
						<OPTION value="2010">2010년</OPTION>
						<OPTION value="2011">2011년</OPTION>
					</SELECT>&nbsp;
					<SELECT name='m'>
						<OPTION value="01">1월</OPTION>
						<OPTION value="02">2월</OPTION>
						<OPTION value="03">3월</OPTION>
						<OPTION value="04">4월</OPTION>
						<OPTION value="05">5월</OPTION>
						<OPTION value="06">6월</OPTION>
						<OPTION value="07">7월</OPTION>
						<OPTION value="08">8월</OPTION>
						<OPTION value="09">9월</OPTION>
						<OPTION value="10">10월</OPTION>
						<OPTION value="11">11월</OPTION>
						<OPTION value="12">12월</OPTION>
					</SELECT>&nbsp;
					<SELECT name='d'>
						<OPTION value="01">1일</OPTION>
						<OPTION value="02">2일</OPTION>
						<OPTION value="03">3일</OPTION>
						<OPTION value="04">4일</OPTION>
						<OPTION value="05">5일</OPTION>
						<OPTION value="06">6일</OPTION>
						<OPTION value="07">7일</OPTION>
						<OPTION value="08">8일</OPTION>
						<OPTION value="09">9일</OPTION>
						<OPTION value="10">10일</OPTION>
						<OPTION value="11">11일</OPTION>
						<OPTION value="12">12일</OPTION>
						<OPTION value="13">13일</OPTION>
						<OPTION value="14">14일</OPTION>
						<OPTION value="15">15일</OPTION>
						<OPTION value="16">16일</OPTION>
						<OPTION value="17">17일</OPTION>
						<OPTION value="18">18일</OPTION>
						<OPTION value="19">19일</OPTION>
						<OPTION value="20">20일</OPTION>
						<OPTION value="21">21일</OPTION>
						<OPTION value="22">22일</OPTION>
						<OPTION value="23">23일</OPTION>
						<OPTION value="24">24일</OPTION>
						<OPTION value="25">25일</OPTION>
						<OPTION value="26">26일</OPTION>
						<OPTION value="27">27일</OPTION>
						<OPTION value="28">28일</OPTION>
						<OPTION value="29">29일</OPTION>
						<OPTION value="30">30일</OPTION>
						<OPTION value="31">31일</OPTION>
					</SELECT>&nbsp;
					<input type='text' name='hour' size='2' maxlength='2' class='text_01'>시 <input type='text' name='minute' size='2' maxlength='2' class='text_01'>분
			</td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
		<tr>
			<td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">출퇴근대상자</td>
			<td width="80%" height="25" class="bg_04"><input type='text' name='id' size='10' maxlength='15' class='text_01' readOnly> <input type='text' name='name' size='10' maxlength='15' class='text_01' readOnly> <a href="Javascript:searchUser();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a> </td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
		</tbody></table></td></tr></table>
</form>
</body>
</html>

<script language=javascript>
<!--
	//오늘날짜
	var today = new Date();
	var yr = today.getYear() ;
	var mon = today.getMonth()+1 ;
	var date = today.getDate();
	if(mon < 10) mon = "0" + mon;
	if(date < 10) date = "0" + date;
	
	wForm.y.value = yr;
	wForm.m.value = mon;
	wForm.d.value = date;

	//대상자 찾기
	function searchUser()
	{
		wopen("searchUser.jsp","search_user","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

	}

	//팝업창
	function wopen(url, t, w, h) {
		var sw;
		var sh;

		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
	}

	//저장
	function save() {
		var f = document.wForm;

		if(f.hour.value == '' || f.hour.value.length != 2 || f.hour.value < 0 || f.hour.value > 23){
			alert("시간을 올바로 입력하십시오.");
			f.hour.focus();
			return;
		}

		if(f.minute.value == '' || f.minute.value.length != 2 || f.minute.value < 0 || f.minute.value >= 60){
			alert("분을 올바로 입력하십시오.");
			f.minute.focus();
			return;
		}

		if(f.id.value == ''){
			alert("출퇴근대상자를 찾아서 선택하십시오.");
			return;
		}

		var y = f.y.value;
		var m = f.m.value;
		var d = f.d.value;
		var t = f.hour.value + ":" + f.minute.value;
		var id = f.id.value;
		var name = f.name.value;

		var mode = "chk_in";
		var mode_s = "출근";
		if(f.mode[1].checked){
			mode = "chk_out";
			mode_s = "퇴근";
		}
		
		var c = confirm(name+"("+id+")을(를) "+y+"년 "+m+"월 "+d+"일 "+t+"에 "+mode_s+"처리하시겠습니까?");
		if(c) location.href = "../../servlet/GeunTaeServlet?mode="+mode+"&y="+y+"&m="+m+"&d="+d+"&t="+t+"&id="+id;
	}
-->
</script>