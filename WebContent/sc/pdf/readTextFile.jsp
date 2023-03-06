<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "TEXT 입력폼"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	
%>

<HTML>
<HEAD><TITLE>TEXT 입력폼</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../sc/css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../sc/images/blet.gif">TEXT PDF변환</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32<!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px'>
					<a href="javascript:sendSave();"><img src="../sc/images/bt_reg.gif" border=0></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">TEXT FILE</td>
			   <td width="87%" height="25" class="bg_04">
					<input type="file" size='60' name="file_name">
					</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
</form>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">데이터 처리중입니다. 잠시만 기다려 주십시요.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>


<script language=javascript>
<!--
//등록하기
function sendSave()
{
	var a=document.eForm.file_name.value; var b=a.length; var c=a.lastIndexOf(".")+1;
	var ext_name=a.substring(b,c);
	if(ext_name != 'txt') {alert('TEXT File만 입력이 가능합니다.'); return; }

	document.eForm.action='../servlet/pdfCtrlServlet';
	document.eForm.mode.value='read_textpdf';
	document.eForm.submit();
}
//창
function wopen(url, t, w, h,st) {
	var sw = (screen.Width - w) / 2;
	var sh = (screen.Height - h) / 2 - 50;
	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>