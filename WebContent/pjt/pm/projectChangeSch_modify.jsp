<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "일정변경내용 상세수정하기"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();

	//----------------------------------------------------
	//	일정변경내용 상세보기
	//----------------------------------------------------
	String pid="",pjt_code="",pjt_name="",node_code="";	
	String node_name="",user_id="",user_name="",in_date="",chg_note="";
	com.anbtech.pjt.entity.projectTable chg;
	ArrayList chg_list = new ArrayList();
	chg_list = (ArrayList)request.getAttribute("SCH_List");
	chg = new projectTable();
	Iterator chg_iter = chg_list.iterator();

	if(chg_iter.hasNext()) {
		chg = (projectTable)chg_iter.next();
		pid = chg.getPid();	
		pjt_code = chg.getPjtCode();	
		pjt_name = chg.getPjtName();	
		node_code = chg.getNodeCode();	
		node_name = chg.getNodeName();	
		user_id = chg.getUserId();	
		user_name = chg.getUserName();	
		in_date = chg.getInDate();	
		chg_note = chg.getChgNote();	
	}
	String Change = request.getParameter("Change"); if(Change == null) Change = "";

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//일정변경내용 수정하기
function changeSchedule() 
{
	document.sForm.action='../servlet/projectChangeSchServlet';
	document.sForm.mode.value='PCS_M';
	document.sForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="sForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> 일정변경내용 수정</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width=100%>
					<a href="javascript:changeSchedule();"><img src="../pjt/images/bt_modify.gif" border="0"></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><form name="eForm" method="post" style="margin:0">
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">과제이름</td>
			   <td width="90%" height="25" class="bg_04"><%=pjt_code%>  <%=pjt_name%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">노드이름</td>
			   <td width="90%" height="25" class="bg_04"><%=node_code%> <%=node_name%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">작성일자</td>
			   <td width="90%" height="25" class="bg_04"><%=in_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">변경사유</td>
			   <td width="90%" height="25" class="bg_04">
				<textarea rows=6 cols=60 name='chg_note' value=''><%=chg_note%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
</form>

</body>
</html>
