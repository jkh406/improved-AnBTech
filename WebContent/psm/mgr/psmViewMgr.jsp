<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ȸ���Ѱ���"		
	contentType = "text/html; charset=KSC5601" 	
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
	String query = "",msg="";
	String mode = request.getParameter("mode"); if(mode == null) mode = "";
	String pid = request.getParameter("pid"); if(pid == null) pid = ""; 
	String pjt_type = Hanguel.toHanguel(request.getParameter("pjt_type"));
	if(pjt_type == null) pjt_type="A"; 
	String pjt_grade = Hanguel.toHanguel(request.getParameter("pjt_grade"));
	if(pjt_grade == null) pjt_grade=""; 
	String user_id = Hanguel.toHanguel(request.getParameter("user_id"));
	if(user_id == null) user_id=""; else user_id = user_id.toUpperCase();
	String user_name = Hanguel.toHanguel(request.getParameter("user_name"));
	if(user_name == null) user_name=""; 
	String div_code = Hanguel.toHanguel(request.getParameter("div_code"));
	if(div_code == null) div_code=""; 
	String div_name = Hanguel.toHanguel(request.getParameter("div_name"));
	if(div_name == null) div_name=""; 

	//����� �ڵ� �� ����� �� ���ϱ�
	if(user_id.length() != 0) {
		query = "select b.ac_code,b.ac_name from user_table a,class_table b ";
		query += "where a.id='"+user_id+"' and a.ac_id=b.ac_id";
		bean.executeQuery(query);
		
		if(bean.next()) {
			div_code = bean.getData("ac_code");
			div_name = bean.getData("ac_name");
		}
	}


	/*********************************************************************
	 	����ϱ�
	*********************************************************************/
	if(mode.equals("ADD")) {
		//��ϵ� �������� �Ǵ��ϱ�
		String sts = "";
		query = "select * from psm_view_mgr where user_id='"+user_id+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			sts = bean.getData("user_id");
		}

		//���ǿ����� ����ϱ�
		if(sts.equals(user_id)) { msg = "�̹� ��ϵ� �������Դϴ�."; }
		else {
			query = "insert into psm_view_mgr(pid,pjt_type,pjt_grade,user_id,user_name,div_code,";
			query += "div_name) values('";
			query += anbdt.getID()+"','"+pjt_type+"','"+pjt_grade+"','"+user_id+"','";
			query += user_name+"','"+div_code+"','"+div_name+"')";
			bean.execute(query);
		}

		pid=pjt_type=pjt_grade=user_id=user_name=div_code=div_name="";
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("MOD")) {
		query = "update psm_view_mgr set pjt_type='"+pjt_type+"',pjt_grade='"+pjt_grade+"',";
		query += "user_id='"+user_id+"',user_name='"+user_name+"',div_code='"+div_code+"',";
		query += "div_name='"+div_name+"' where pid='"+pid+"'";
		bean.execute(query);

		pid=pjt_type=pjt_grade=user_id=user_name=div_code=div_name="";
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from psm_view_mgr where pid='"+pid+"'";
		bean.execute(query);

		pid=pjt_type=pjt_grade=user_id=user_name=div_code=div_name="";
	}
	/*********************************************************************
	 	���뺸��
	*********************************************************************/
	if(mode.equals("VIEW")) {
		query = "select * from psm_view_mgr where pid='"+pid+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			pid = bean.getData("pid");
			pjt_type = bean.getData("pjt_type");
			pjt_grade = bean.getData("pjt_grade");
			user_id = bean.getData("user_id");
			user_name = bean.getData("user_name");
			div_code = bean.getData("div_code");
			div_name = bean.getData("div_name");
		}
	}
	
	/*********************************************************************
	 	������ȸ���� LIST��������
	*********************************************************************/
	query = "SELECT count(*) FROM psm_view_mgr";
	bean.executeQuery(query);
	bean.next();
	int cnt = Integer.parseInt(bean.getData(1));
	String[][] data = new String[cnt][7];

	query = "SELECT * FROM psm_view_mgr order by user_id asc";
	bean.executeQuery(query);
	int n=0;
	while(bean.next()) {
		data[n][0] = bean.getData("pid");
		data[n][1] = bean.getData("pjt_type");
		data[n][2] = bean.getData("pjt_grade");
		data[n][3] = bean.getData("user_id");
		data[n][4] = bean.getData("user_name");
		data[n][5] = bean.getData("div_code");
		data[n][6] = bean.getData("div_name");
		n++;
	} //while

%>
<HTML>
<HEAD><TITLE>PSM ������ȸ���Ѱ���</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0" oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../images/blet.gif"> ������ȸ����</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--��ư-->
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

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr><!--�⺻���� -->
				<td height="25" colspan="4"><img src="../images/basic_info.gif" width="209" height="25" border="0"></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">���</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="user_id" value="<%=user_id%>" size="20" readonly>
					<a href="javascript:selectUserInfo();"><img src="../images/bt_search.gif" border="0" align='absmiddle'></a></td>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�̸�</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="user_name" value="<%=user_name%>" size="30" readonly></td>
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
<input type="hidden" name="pjt_type" value="A" size="20">
<input type="hidden" name="pjt_grade" value="" size="20">
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
</form> 

<!-- ����Ʈ ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
    <TBODY>
		<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
		<TR vAlign=middle height=25>
			<TD noWrap width=20 align=middle class='list_title'>#</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100 align=middle class='list_title'>���</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=150 align=middle class='list_title'>�̸�</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100 align=middle class='list_title'>�μ��ڵ�</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100% align=middle class='list_title'>�μ���</TD>
		</TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
	<% if (cnt == 0) { %>
		<TR vAlign=center height=22>
			 <td colspan='15' align="middle">***** ������ �����ϴ�. ****</td>
		</tr> 
	<% } %>	

	<% 
		for(int i=0; i<data.length; i++) {
	%>	
		<form name="vForm" method="post" style="margin:0">
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			<TD align=middle height="24" class='list_bg'><%=i+1%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><a href="javascript:contentView('<%=data[i][0]%>');"><%=data[i][3]%></a></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=data[i][4]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=data[i][5]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=data[i][6]%></TD>
		</TR>
		<TR><TD colSpan=15 background="../images/dot_line.gif"></TD></TR>
	<% 
		}  //for

	%>
	</TBODY>
</TABLE>

<input type="hidden" name="mode" value=''>
<input type="hidden" name="pid" value=''>
</form>
</body>
</html>

<script language=javascript>
<!--
var msg = '<%=msg%>';
if(msg.length != 0) {alert(msg); }
//����ϱ�
function write()
{
	var f = document.sForm;

	var user_id = f.user_id.value;
	if(user_id == '') { alert('����� �Է��Ͻʽÿ�.'); f.user_id.focus(); return; }

	var pid = f.pid.value;
	if(pid != '') { alert('������ư�� �̿��Ͻʽÿ�.'); return; }

	document.sForm.action='psmViewMgr.jsp';
	document.sForm.mode.value='ADD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//��������ϱ�
function contentModify()
{
	var f = document.sForm;

	var user_id = f.user_id.value;
	if(user_id == '') { alert('����� �Է��Ͻʽÿ�.'); f.user_id.focus(); return; }

	var pid = f.pid.value;
	if(pid == '') { alert('��Ϲ�ư�� �̿��Ͻʽÿ�.'); return; }

	document.sForm.action='psmViewMgr.jsp';
	document.sForm.mode.value='MOD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//��������ϱ�
function contentDelete()
{
	d = confirm("�����Ͻðڽ��ϱ�?");
	if(d == false) return;

	document.sForm.action='psmViewMgr.jsp';
	document.sForm.mode.value='DEL';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//���뺸��
function contentView(pid)
{
	document.vForm.action='psmViewMgr.jsp';
	document.vForm.mode.value='VIEW';
	document.vForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.vForm.submit();
}
//������� ã��
function selectUserInfo()
{

	var strUrl = "../searchUserInfo.jsp?target=sForm.user_id/sForm.user_name";
	newWIndow = wopen(strUrl, "PsmUserInot", "260", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//â
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//������ ó���� ��ư����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>