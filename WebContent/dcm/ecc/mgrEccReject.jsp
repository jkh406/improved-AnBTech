<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%@ page 
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.sql.*, java.io.*, java.util.*, com.anbtech.text.*"
%>
<%
	String pid = request.getParameter("pid");
	String ecr_id = request.getParameter("ecr_id");
	String ecr_name = Hanguel.toHanguel(request.getParameter("ecr_name"));
	String mgr_id = request.getParameter("mgr_id");
	String mgr_name = Hanguel.toHanguel(request.getParameter("mgr_name"));
%>

<HTML><HEAD><TITLE>설계변경 반려</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//반려하기
function rejectEcr()
{
	var f = document.eForm;

	var note = f.note.value;
	if(note == '') { alert('변경사유가 입력되지 않았습니다.'); f.note.focus(); return; }
	if(note.length < 10) { alert('변경사유를 10자이상 입력하십시오.'); f.note.focus(); return; }
	
	document.eForm.action='../../servlet/CbomProcessServlet';
	document.eForm.mode.value='mgr_reject';
	document.eForm.submit();
}
-->
</script>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form name="eForm" method="post" style="margin:0" encType="multipart/form-data" oncontextmenu="return false">

<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
	<!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR>
					<TD height="32" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_cbom_reject.gif" align='absmiddle' border='0' alt='설계변경 반려'></TD></TR>
		        <TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

		<TABLE cellspacing=0 cellpadding=0 width="98%" border=0 align='middle'>
		   <TBODY>
		        <TR><TD height="12"></TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan=2></TD></TR>
		        <TR>
					<TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">반려사유</TD>
					<TD width="80%" height="25" class="bg_04">
						<TEXTAREA NAME="note" rows='6' cols='49'></TEXTAREA></TD>	   
				</TR>
		        <TR bgcolor="C7C7C7"><TD height="1" colspan="2"></TD></TR>
		        <TR><TD height="12" colspan="2"></TD></TR></TBODY></TABLE>
	<!--꼬릿말-->
	<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
		<TBODY>
			<TR>
				<TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:rejectEcr();"><img src="../images/bt_confirm.gif" width="46" height="19" border="0"></a> <a href="javascript:self.close();"><img src="../images/close.gif" width="46" height="19" border="0"></a></TD>
			</TR>
			<TR><TD width="100%" height=1 colSpan=4 bgcolor="0C2C55"></TD>
	        </TR>
        </TBODY></TABLE></TD></TR>
</table>
<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="pid" size="15" value="<%=pid%>">
<input type="hidden" name="eco_id" size="15" value="<%=mgr_id%>">     <% //반려자 %>
<input type="hidden" name="eco_name" size="15" value="<%=mgr_name%>">
<input type="hidden" name="user_id" size="15" value="<%=ecr_id%>">    <% //수신자 %>
<input type="hidden" name="user_name" size="15" value="<%=ecr_name%>">
</BODY>
</HTML>