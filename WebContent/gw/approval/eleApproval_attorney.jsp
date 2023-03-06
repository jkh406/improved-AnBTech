<%@ include file="../../admin/configHead.jsp"%>
<%@ page
	language = "java"
	info = "전자결재 대리결재자 지정하기"		
	contentType = "text/html; charset=euc-kr"
	import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
	import="com.anbtech.gw.entity.*"

%>
<%
	/********************************************************************
		 현재 지정된 대리결재자
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//날자
	String attorney_id="",attorney_name="",start_date="",end_date="",sd="",ed="";
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");

	com.anbtech.gw.entity.TableAppMaster table = new com.anbtech.gw.entity.TableAppMaster();
	Iterator table_iter = table_list.iterator();
	if(table_iter.hasNext()) {
		table = (TableAppMaster)table_iter.next();
		attorney_id = table.getAttorneyId();
		attorney_name = table.getAttorneyName();

		start_date = table.getStartDate(); if(start_date == null) start_date = "";

		if((start_date.length() > 0) && (attorney_id.length() > 0)) 
			sd = start_date.substring(0,4)+"/"+start_date.substring(4,6)+"/"+start_date.substring(6,8);
		else sd = anbdt.getDate(0);

		end_date = table.getEndDate();		if(end_date == null) end_date = "";
		if((end_date.length() > 0) && (attorney_id.length() > 0))
			ed = end_date.substring(0,4)+"/"+end_date.substring(4,6)+"/"+end_date.substring(6,8);

	}
	if(sd.length() == 0) sd = anbdt.getDate(0);		//최초 입력시

	//처리결과 알기
	String Rep = request.getParameter("Rep");	if(Rep == null) Rep = "";


%>

<HTML><HEAD><TITLE>부재중 결재자 지정</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../gw/css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<center>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../gw/images/pop_app_a.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="eForm" method="post" style="margin:0">
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../gw/images/bg-01.gif">부재대상자</td>
           <td width="70%" height="25" class="bg_02"><%=login_id%> <%=login_name%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../gw/images/bg-01.gif">대리결재자</td>
           <td width="70%" height="25" class="bg_02">
			  <% if(attorney_id.length() > 0) { %>
				<input type=input name=attorney size=20 value='<%=attorney_id%>/<%=attorney_name%>' style="width:150" readonly>
			  <% } else { %>
				<input type=input name=attorney size=20 value='' style="width:150" readonly>
			  <% } %>
			   <a href="Javascript:searchAttorney();"><img src="../gw/images/bt_search2.gif" border="0" align='absbottom'></a>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../gw/images/bg-01.gif">시작일</td>
           <td width="70%" height="25" class="bg_02"><input type=input name=start_date size=20 value='<%=sd%>' style="width:150" readonly> <A Href="Javascript:OpenCalendar('start_date');"><img src="../gw/images/bt_calendar.gif" border="0" align='absbottom'></A></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../gw/images/bg-01.gif">종료일</td>
           <td width="70%" height="25" class="bg_02"><input type=input name=end_date size=20 value='<%=ed%>' style="width:150" readonly> <A Href="Javascript:OpenCalendar('end_date');"><img src="../gw/images/bt_calendar.gif" border="0" align='absbottom'></A></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='attorney_id' value=''>
<input type='hidden' name='attorney_name' value=''></form>
	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
				  <a href="Javascript:setAttorney();"><img src='../gw/images/bt_save.gif' border='0' valign='absmiddle'></a> <a href="Javascript:delAttorney();"><img src='../gw/images/bt_del.gif' border='0' valign='absmiddle'></a> <a href="Javascript:self.close();"><img src='../gw/images/bt_close.gif' border='0' valign='absmiddle'></a></TD></TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr>
</table>
</BODY>
</center>
</HTML>

<script language=javascript>
<!--
//처리메시지 알려주기
var Rep = '<%=Rep%>';
if(Rep == 'U') { alert('대리결재인이 지정되었습니다.'); }
else if(Rep == 'D') { alert('대리결재인이 해지되었습니다.'); }
else if(Rep == 'DUP') { alert('다른결재자의 대리결재인으로 이미 지정되었습니다.'); }

//대리결재인 지정하기
function setAttorney()
{
	//검사
	var rtn = '';
	rtn = document.eForm.attorney.value;
	if(rtn.length == 0) { alert('부재중 대리결재자을 지정하십시요.');  return; }
	var a = rtn.split('/');

	//일자에서 '/'삭제하기
	var psd = document.eForm.start_date.value;		//계획기간 시작일
	for(i=0;i<2;i++) psd = psd.replace('/','');	
	var ped = document.eForm.end_date.value;		//계획기간 종료일
	for(i=0;i<2;i++) ped = ped.replace('/','');	
	if(ped.length == 0) { alert('종료예정일을 입력하십시요.');  return; }

	if(psd > ped ) { alert('종료예정일이 시작일보다 빠릅니다. 다시 입력하십시요.');  return; }
	
	document.eForm.action='../servlet/ApprovalAttorneyServlet';
	document.eForm.mode.value='ATT_U';
	document.eForm.attorney_id.value=a[0];	
	document.eForm.attorney_name.value=a[1];	
	document.eForm.start_date.value=psd;
	document.eForm.end_date.value=ped;
	document.eForm.submit();
}
//대리결재인 삭제하기
function delAttorney()
{
	//검사
	var rtn = '';
	rtn = document.eForm.attorney.value;
	if(rtn.length == 0) { alert('대리결재자가 없습니다.');  return; }
	var a = rtn.split('/');

	document.eForm.action='../servlet/ApprovalAttorneyServlet';
	document.eForm.mode.value='ATT_D';	
	document.eForm.attorney_id.value=a[0];	
	document.eForm.attorney_name.value=a[1];	
	document.eForm.submit();
}

//대리결재자찾기
function searchAttorney()
{
	//window.open("../gw/approval/searchAttorney.jsp?target=eForm.attorney","proxy","width=250,height=380,scrollbar=yes,toolbar=no,status=no,resizable=no");
	wopen('../gw/approval/searchAttorney.jsp?target=eForm.attorney','proxy','250','380','scrollbars=no,toolbar=no,status=no,resizable=no');

}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../gw/approval/Calendar.jsp?FieldName=" + FieldName;
//	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
	wopen(strUrl,'Calendar','180','250','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
		var sw;
		var sh;

		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>
