<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������������"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//--------------------
	//	�ʱ�ȭ ����
	//--------------------
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	bean.openConnection();

	/*********************************************************************
	 	���ε����� �ޱ�
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
	 	CATEGORY LIST��������
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

		if(status.equals("1")) status="�����ϰ���";
		else status = "���ĵ�ϰ���";
		data[n][3] = status;

		data[n][4] = bean.getData("env_name");
		n++;
	} //while

%>
<HTML>
<HEAD><TITLE>�������� ����</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0" oncontextmenu="return false">
<FORM name="sForm" method="post" style="margin:0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../images/blet.gif"> �������� ����</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<a href="javascript:write();"><img src="../images/bt_reg.gif" border=0></a>
					<!--
					<a href="javascript:contentModify();"><img src="../images/bt_modify.gif" border=0></a>
					<a href="javascript:contentDelete();"><img src="../images/bt_del.gif" border=0></a>-->
					</TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9DA8BA'></TD></TR>
	</TABLE>

<TR><TD>
	<!-- ����Ʈ ���� -->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
		<TBODY>
			<TR vAlign=middle height=25>
				<TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>��������</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>������</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'></TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
		<%
			if (cnt == 0) { %>
			<TR vAlign=center height=22>
				 <TD colspan='8' align="middle">***** ������ �����ϴ�. ****</td>
			</TR> 
		<% } %>	

		<% 
			for(int i=0; i<data.length; i++) {
		%>	
			<FORM name="vForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=i+1%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=data[i][3]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><a href="javascript:contentView('<%=data[i][0]%>','<%=data[i][2]%>');"><%=data[i][4]%></a></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;</TD>
			</TR>
			<TR><TD colSpan=8 background="../images/dot_line.gif"></TD></TR>
		<% 
			}  //for

		%>
		</TBODY>
	</TABLE>
</TD></TR>
</TABLE>
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value=''>
<INPUT type="hidden" name="env_status" value=''>
</FORM>
</BODY>
</HTML>

<SCRIPT language=javascript>
<!--
var msg = '<%=msg%>';
if(msg.length != 0) {alert(msg); }
//����ϱ�
function write()
{
	document.sForm.action='psmMgrInput.jsp';
	document.sForm.mode.value='ADD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//���뺸��
function contentView(pid,env_status)
{
	document.sForm.action='psmMgrInput.jsp';
	document.sForm.mode.value='VIEW';
	document.sForm.pid.value=pid;
	document.sForm.env_status.value=env_status;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//������ ó���� ��ư����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</SCRIPT>