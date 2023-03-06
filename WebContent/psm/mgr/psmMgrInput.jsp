<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제종류 관리"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//--------------------
	//	초기화 정보
	//--------------------
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	bean.openConnection();

	/*********************************************************************
	 	내부데이터 받기
	*********************************************************************/
	String query = "",msg="",status="";
	String mode = request.getParameter("mode"); if(mode == null) mode = "";
	String pid = request.getParameter("pid"); if(pid == null) pid = ""; 
	String env_type = Hanguel.toHanguel(request.getParameter("env_type"));
	if(env_type == null) env_type=""; 
	String env_status = Hanguel.toHanguel(request.getParameter("env_status"));
	if(env_status == null) env_status=""; 
	String env_name = Hanguel.toHanguel(request.getParameter("env_name"));
	if(env_name == null) env_name=""; else env_name = env_name.toUpperCase();

	/*********************************************************************
	 	내용보기
	*********************************************************************/
	if(mode.equals("VIEW")) {
		query = "select * from psm_env where pid='"+pid+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			pid = bean.getData("pid");
			env_type = bean.getData("env_type");
			env_status = bean.getData("env_status");
			env_name = bean.getData("env_name");
		}
	}
	
	String caption = "";
	if(mode.equals("ADD")){
		caption = "등록";
	} else if(mode.equals("VIEW")){
		caption = "정보";
	}
%>
<HTML>
<HEAD><TITLE>과제종류 <%=caption%></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0" oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../images/blet.gif"> 과제종류 <%=caption%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<% if(mode.equals("ADD")) {%>
					<a href="javascript:write();"><img src="../images/bt_reg.gif" border=0></a>
					<% } else if(mode.equals("VIEW") || mode.equals("MOD")) {%>
					<a href="javascript:contentModify();"><img src="../images/bt_modify.gif" border=0></a>
					<a href="javascript:contentDelete();"><img src="../images/bt_del.gif" border=0></a>
					<% } %>
					<a href="javascript:contentList();"><img src="../images/bt_cancel.gif" border=0></a>
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../images/bg-01.gif">과제형태</td>
			   <TD width="37%" height="25" class="bg_04">
					<select name='env_status'>
				<%
						String[] list = {"1","2"};
						String[] list_name = {"예비등록과제","정식등록과제"};
						String sel="";
						
						for(int i=0; i<list.length; i++) {
							if(env_status.equals(list[i])) sel="selected";
							else sel = "";
							out.println("<option value='"+list[i]+"' "+sel+" >"+list_name[i]+"</option>");
						}
				%></select></td>
			   <TD width="13%" height="25" class="bg_03" background="../images/bg-01.gif">과제종류명</td>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type="text" name="env_name" size="15" maxlength='30' value="<%=env_name%>"></td>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='5'></TD></TR>
</TABLE>
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value='<%=pid%>'>
<INPUT type="hidden" name="env_type" value='P'>
</form> 
</body>
</html>

<script language=javascript>
<!--
var msg = '<%=msg%>';
if(msg.length != 0) {alert(msg); }
//등록하기
function write()
{
	var f = document.sForm;

	var env_name = f.env_name.value;
	if(env_name == '') { alert('과제명을 입력하십시오.'); f.env_name.focus(); return; }

	var pid = f.pid.value;
	if(pid != '') { alert('수정버튼을 이용하십시오.'); return; }

	document.sForm.action='psmMgrProcess.jsp';
	document.sForm.mode.value='ADD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//내용수정하기
function contentModify()
{
	var f = document.sForm;
	
	if(!confirm("수정하시겠습니까?")){
		return;
	}

	var env_name = f.env_name.value;
	if(env_name == '') { alert('과제명을 입력하십시오.'); f.env_name.focus(); return; }

	var pid = f.pid.value;
	if(pid == '') { alert('등록버튼을 이용하십시오.'); return; }

	document.sForm.action='psmMgrProcess.jsp';
	document.sForm.mode.value='MOD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//내용삭제하기
function contentDelete()
{
	d = confirm("삭제하시겠습니까?");
	if(d == false) return;

	document.sForm.action='psmMgrProcess.jsp';
	document.sForm.mode.value='DEL';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

// 목록보기
function contentList()
{
	document.sForm.action='psmMgrList.jsp';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>