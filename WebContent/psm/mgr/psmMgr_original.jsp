<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제명등록 관리"		
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
	 	등록하기
	*********************************************************************/
	if(mode.equals("ADD")) {
		//등록된 정보인지 판단하기
		String sts = "";
		query = "select * from psm_env where env_type='P' and env_name='"+env_name+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			sts = bean.getData("env_name");
		}
		
		//조건에따라 등록하기
		if(sts.equals(env_name)) { msg = "이미 등록된 정보입니다."; }
		else {
			query = "insert into psm_env(pid,env_type,env_status,env_name) values('";
			query += anbdt.getID()+"','"+env_type+"','"+env_status+"','"+env_name+"')";
			bean.execute(query);
		}

		pid=env_type=env_status=env_name="";
	}
	/*********************************************************************
	 	수정하기
	*********************************************************************/
	if(mode.equals("MOD")) {
		query = "update psm_env set env_type='"+env_type+"',env_status='"+env_status+"',";
		query += "env_name='"+env_name+"' where pid='"+pid+"'";
		bean.execute(query);

		pid=env_type=env_status=env_name="";
	}
	/*********************************************************************
	 	삭제하기
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from psm_env where pid='"+pid+"'";
		bean.execute(query);

		pid=env_type=env_status=env_name="";
	}
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
	
	/*********************************************************************
	 	CATEGORY LIST가져오기
	*********************************************************************/
	query = "SELECT count(*) FROM psm_env where env_type='P'";
	bean.executeQuery(query);
	bean.next();
	int cnt = Integer.parseInt(bean.getData(1));
	String[][] data = new String[cnt][5];

	query = "SELECT * FROM psm_env where env_type='P' order by env_status asc";
	bean.executeQuery(query);
	int n=0;
	while(bean.next()) {
		data[n][0] = bean.getData("pid");
		data[n][1] = bean.getData("env_type");

		status = bean.getData("env_status");
		data[n][2] = status;

		if(status.equals("1")) status="예비등록과제";
		else status = "정식등록과제";
		data[n][3] = status;

		data[n][4] = bean.getData("env_name");
		n++;
	} //while

%>
<HTML>
<HEAD><TITLE>PSM 과제명 등록관리</TITLE>
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
					<TD valign='middle' class="title"><img src="../images/blet.gif"> 과제명등록관리</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%'>
					<a href="javascript:write();"><img src="../images/bt_reg.gif" border=0></a>
					<a href="javascript:contentModify();"><img src="../images/bt_modify.gif" border=0></a>
					<a href="javascript:contentDelete();"><img src="../images/bt_del.gif" border=0></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr><!--기본정보 -->
				<td height="25" colspan="4"><img src="../images/basic_info.gif" width="209" height="25" border="0"></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">과제형태</td>
			   <td width="37%" height="25" class="bg_04">
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
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">과제종류명</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="env_name" maxlength='30' value="<%=env_name%>" size="30"></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
	<TR><TD height='5'></TD></TR>
</table>
<input type="hidden" name="mode" value=''>
<input type="hidden" name="pid" value='<%=pid%>'>
<input type="hidden" name="env_type" value='P'>
</form> 

<!-- 리스트 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
    <TBODY>
		<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
		<TR vAlign=middle height=25>
			<TD noWrap width=20 align=middle class='list_title'>#</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=120 align=middle class='list_title'>과제형태</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=120 align=middle class='list_title'>과제명</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100% align=middle class='list_title'></TD>
		</TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
	<% if (cnt == 0) { %>
		<TR vAlign=center height=22>
			 <td colspan='8' align="middle">***** 내용이 없습니다. ****</td>
		</tr> 
	<% } %>	

	<% 
		for(int i=0; i<data.length; i++) {
	%>	
		<form name="vForm" method="post" style="margin:0">
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			<TD align=middle height="24" class='list_bg'><%=i+1%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=data[i][3]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'><a href="javascript:contentView('<%=data[i][0]%>','<%=data[i][2]%>');"><%=data[i][4]%></a></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;</TD>
		</TR>
		<TR><TD colSpan=8 background="../images/dot_line.gif"></TD></TR>
	<% 
		}  //for

	%>
	</TBODY>
</TABLE>

<input type="hidden" name="mode" value=''>
<input type="hidden" name="pid" value=''>
<input type="hidden" name="env_status" value=''>
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

	document.sForm.action='psmMgr.jsp';
	document.sForm.mode.value='ADD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//내용수정하기
function contentModify()
{
	var f = document.sForm;

	var env_name = f.env_name.value;
	if(env_name == '') { alert('과제명을 입력하십시오.'); f.env_name.focus(); return; }

	var pid = f.pid.value;
	if(pid == '') { alert('등록버튼을 이용하십시오.'); return; }

	document.sForm.action='psmMgr.jsp';
	document.sForm.mode.value='MOD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//내용삭제하기
function contentDelete()
{
	d = confirm("삭제하시겠습니까?");
	if(d == false) return;

	document.sForm.action='psmMgr.jsp';
	document.sForm.mode.value='DEL';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//내용보기
function contentView(pid,env_status)
{
	document.vForm.action='psmMgr.jsp';
	document.vForm.mode.value='VIEW';
	document.vForm.pid.value=pid;
	document.vForm.env_status.value=env_status;
	document.onmousedown=dbclick;
	document.vForm.submit();
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>