<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	info		= "상세검색"
	contentType = "text/html; charset=euc-kr"
	import		= "com.anbtech.date.anbDate"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String print_type = request.getParameter("print_type");

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
<HTML><HEAD><TITLE>상세검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_search_d.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="frm1" style="margin:0">
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">등록기간</td>
           <td width="80%" height="25" class="bg_02"><input type="text" name="s_day_s" size="10" value="<%=toDay%>"> <A Href="Javascript:OpenCalendar('s_day_s');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A> ~ <input type="text" name="s_day_e" size="10" value="<%=toDay%>"> <A Href="Javascript:OpenCalendar('s_day_e');"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">검색항목</td>
           <td width="80%" height="25" class="bg_02">
				<SELECT name='sItem'>
					<OPTION value='a.at_name' selected>고객 이름</OPTION>
					<OPTION value='a.ap_name'>고객사 이름</OPTION>
					<OPTION value='a.subject'>서비스 제목</OPTION>
				</SELECT>		   
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">검색어</td>
           <td width="80%" height="25" class="bg_02"><input size="10" name="sWord"> <input type="image" src="../images/bt_search3.gif" onClick="javascript:startSearch();" border="0" align="absmiddle"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></form></td></tr></table></BODY></HTML>


<script language=javascript>

function OpenCalendar(where) {
	var strUrl = "Calendar.jsp?where="+where;
	wopen(strUrl,"Calendar","200","270");
}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

function startSearch()
{
	var f = document.frm1;
	var sItem = f.sItem.value;
	var sWord = f.sWord.value;

    var fromField=f.s_day_s.value.split("/")
    var syear =  fromField[0]
    var smonth = fromField[1]
    if(smonth.length==1) {smonth="0"+smonth}
    var sday = fromField[2]
    if(sday.length==1) {sday="0"+sday}

	var from = syear+smonth+sday; // 시작일(20030326)

    var fromField=f.s_day_e.value.split("/")
    var syear =  fromField[0]
    var smonth = fromField[1]
    if(smonth.length==1) {smonth="0"+smonth}
    var sday = fromField[2]
    if(sday.length==1) {sday="0"+sday}

	var to = syear+smonth+sday; // 종료일(20030330)

	opener.location.href = "historyl.jsp?print_type=<%=print_type%>&from="+from+"&to="+to+"&sItem="+sItem+"&sWord="+sWord;
	self.close();
}

</script>
