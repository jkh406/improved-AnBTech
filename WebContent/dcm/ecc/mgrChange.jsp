<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%@ page import="java.sql.*, java.io.*, java.util.*, com.anbtech.text.*"%>

<%
	String pid = request.getParameter("pid");
	String ecr_id = request.getParameter("ecr_id");
	String ecr_name = Hanguel.toHanguel(request.getParameter("ecr_name"));
%>

<HTML><HEAD><TITLE>기술검토 책임자변경</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//수정하기
function changeMgr()
{
	var f = document.eForm;

	var note = f.note.value;
	if(note == '') { alert('변경사유가 입력되지 않았습니다.'); f.note.focus(); return; }
	if(note.length < 10) { alert('변경사유를 10자이상 입력하십시오.'); f.note.focus(); return; }
	
	var mgr_name = f.mgr_name.value;
	if(mgr_name == '') { alert('기술검토책임자가 입력되지 않았습니다.'); f.mgr_name.focus(); return; }
	
	document.eForm.action='../../servlet/CbomProcessServlet';
	document.eForm.mode.value='mgr_change';
	document.eForm.submit();
}
//검토책임자
function checkMgr()
{
	wopen("../searchName.jsp?target=eForm.mgr_id/eForm.mgr_name","proxy","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
-->
</script>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu='return false'>
<form name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
		<!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
		        <TR>
					<TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_app_audit.gif" width="181" height="17" hspace="10" alt='기술검토책임자변경'></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
		<tr><td height="20"></td></tr>
		<table cellspacing=0 cellpadding=0 width="98%" border=0 align='center'>
			<tbody>
				
				<tr bgcolor="c7c7c7"><td height=1 colspan=2></td></tr>
				<tr>
				    <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">검토책임자</td>
				    <td width="80%" height="25" class="bg_04">
						<input type="text" name="mgr_name" value="" size="12" readonly><a href="Javascript:checkMgr();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></td>
				</tr>
				<TR  bgcolor="c7c7c7"><TD height=1 colspan="2"></td></TR>
		        <tr>
					<td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">변경사유</td>
					<td width="80%" height="25" class="bg_04">
						<TEXTAREA NAME="note" rows='5' cols='50'></TEXTAREA></td>	   
				</tr>
				
				
		        <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
				<tr><td height="18" colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:changeMgr();"><img src="../images/bt_confirm.gif" width="46" height="19" border="0"></a> <a href="javascript:self.close();"><img src="../images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr>
</table>
<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="pid" size="15" value="<%=pid%>">
<input type="hidden" name="mgr_id" size="15" value="">
<input type="hidden" name="user_id" size="15" value="<%=ecr_id%>">
<input type="hidden" name="user_name" size="15" value="<%=ecr_name%>">
</BODY>
</HTML>