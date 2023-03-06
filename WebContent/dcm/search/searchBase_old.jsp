<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "기본관리 검색"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>

<%
	com.anbtech.date.anbDate anbdt = new anbDate();	

	String ecc_subject = Hanguel.toHanguel(request.getParameter("ecc_subject"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_subject"));
	String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
	String ecr_name = Hanguel.toHanguel(request.getParameter("ecr_name"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_name"));
	String eco_name = Hanguel.toHanguel(request.getParameter("eco_name"))==null?"":Hanguel.toHanguel(request.getParameter("eco_name"));
	String ecr_s_date = Hanguel.toHanguel(request.getParameter("ecr_s_date"))==null?anbdt.getDate(-60):Hanguel.toHanguel(request.getParameter("ecr_s_date"));
	String ecr_e_date = Hanguel.toHanguel(request.getParameter("ecr_e_date"))==null?anbdt.getDate(0):Hanguel.toHanguel(request.getParameter("ecr_e_date"));
	String eco_s_date = Hanguel.toHanguel(request.getParameter("eco_s_date"))==null?anbdt.getDate(-30):Hanguel.toHanguel(request.getParameter("eco_s_date"));
	String eco_e_date = Hanguel.toHanguel(request.getParameter("eco_e_date"))==null?anbdt.getDate(30):Hanguel.toHanguel(request.getParameter("eco_e_date"));
	String ecc_status = Hanguel.toHanguel(request.getParameter("ecc_status"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_status"));
		
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//기본관리 검색창
function searchBase()
{
	document.eForm.action='searchBase.jsp';
	document.eForm.submit();
}
//선택조건 검색창
function searchCondition()
{
	document.eForm.action='searchCondition.jsp';
	document.eForm.submit();
}
//내용검색 검색창
function searchContent()
{
	document.eForm.action='searchContent.jsp';
	document.eForm.submit();
}
//검색진행하기
function goSearch()
{
	document.eForm.action='../../servlet/CbomHistoryServlet';
	document.eForm.mode.value='sch_base';
	document.eForm.submit();
}
//유효일자 찾기
function OpenCalendar(FieldName) {
	var strUrl = "../Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
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
<BODY topmargin="0" leftmargin="0">
<form name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../images/blet.gif"> 기본관리검색</TD>
				</TR></TBODY></TABLE></TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=30><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0 border=0>
			<TBODY>
				<TR><TD align=left width='20%'>
						<a href="javascript:searchBase();">[기본관리]</a>
						<a href="javascript:searchCondition();">[선택조건]</a>
						<a href="javascript:searchContent();">[내용검색]</a>
						<a href="javascript:goSearch();">[검색]</a>
					</TD></TR></TBODY></TABLE></TD>
	</TR>
	<TR><TD height='2' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			 <!-- 기초정보 작성 -->
			<tr><td height="25" class="bg_03" colspan="4">기본관리</td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">제 목</td>
			    <td width="87%" height="25" class="bg_04" colspan=3>
					<input type="text" name="ecc_subject" value="<%=ecc_subject%>" size="80"></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">ECO NO</td>
			    <td width="37%" height="25" class="bg_04">
					<input type="text" name="eco_no" value="<%=eco_no%>" size="20"></td>
			    <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">발의일</td>
			    <td width="37%" height="25" class="bg_04">
					<input type="text" name="ecr_s_date" value="<%=ecr_s_date%>" size="10"><A Href="Javascript:OpenCalendar('ecr_s_date');"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A> ~
					<input type="text" name="ecr_e_date" value="<%=ecr_e_date%>" size="10"><A Href="Javascript:OpenCalendar('ecr_e_date');"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">발의자</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="ecr_name" value="<%=ecr_name%>" size="20"></td>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">적용일</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="eco_s_date" value="<%=eco_s_date%>" size="10"><A Href="Javascript:OpenCalendar('eco_s_date');"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A> ~
					<input type="text" name="eco_e_date" value="<%=eco_e_date%>" size="10"><A Href="Javascript:OpenCalendar('eco_e_date');"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">검토자</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="eco_name" value="<%=eco_name%>" size="20"></td>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">진행상태</td>
			   <td width="37%" height="25" class="bg_04">
					<select name="ecc_status"> 
					<OPTGROUP label='---------------'>
				<%
					String[] ecc_no = {"","0","1","2","3","4","5","6","7","8","9"};
					String[] ecc_wd = {"","ECR반려","ECR작성","ECR결재","ECR책임자접수","ECR담당자접수","ECO반려","ECO작성","ECO결재","ECO승인","ECO확정"};
					String sel = "";
					for(int i=0; i<ecc_no.length; i++) {
						if(ecc_status.equals(ecc_no[i])) sel = "selected";
						else sel = "";
						out.print("<option "+sel+" value='"+ecc_no[i]+"'>"+ecc_wd[i]+"</option>");
					} 
				%></select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>

<input type='hidden' name='mode' value=''>
</form>

</body>
</html>
