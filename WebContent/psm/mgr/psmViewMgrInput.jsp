<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ȸ�� ����"		
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
	String psm_code = Hanguel.toHanguel(request.getParameter("psm_code"));
	if(psm_code == null) psm_code=""; 
	String psm_korea = Hanguel.toHanguel(request.getParameter("psm_korea"));
	if(psm_korea == null) psm_korea=""; 

	//����� �ڵ� �� ����� �� ���ϱ�
	if(user_id.length() != 0) {
		query = "select b.ac_code,b.ac_name from user_TABLE a,class_TABLE b ";
		query += "where a.id='"+user_id+"' and a.ac_id=b.ac_id";
		bean.executeQuery(query);
		
		if(bean.next()) {
			div_code = bean.getData("ac_code");
			div_name = bean.getData("ac_name");
		}
	}

	/*********************************************************************
	 	���뺸��
	*********************************************************************/
	String asel="",gsel="";
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
			psm_code = bean.getData("psm_code");
			psm_korea = bean.getData("psm_korea");
			if(pjt_type.equals("A")) asel="selected";
			else if(pjt_type.equals("G")) gsel="selected";
		}
	}
	
	String caption = "";
	if(mode.equals("ADD")){
		caption = "���";
	} else if(mode.equals("VIEW")){
		caption = "����";
	}
%>
<HTML>
<HEAD><TITLE>PSM ������ȸ���� <%=caption%></TITLE>
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
					<TD valign='middle' class="title"><img src="../images/blet.gif"> ������ȸ�� ����  <%=caption%></TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<%if(mode.equals("ADD")){%>
					<a href="javascript:write();"><img src="../images/bt_reg.gif" border=0></a>
					<%}%>
					<%if(mode.equals("MOD") || mode.equals("VIEW")){%>
					<a href="javascript:contentModify();"><img src="../images/bt_modify.gif" border=0></a>
					<%}%>
					<%if(mode.equals("DEL") || mode.equals("VIEW")){%>
					<a href="javascript:contentDelete();"><img src="../images/bt_del.gif" border=0></a>
					<%}%>
					<a href="javascript:contentList();"><img src="../images/bt_list.gif" border=0></a>
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����</TD>
			   <TD width="87%" height="25" class="bg_04" colspan="3">
					<SELECT NAME='pjt_type'>
						<OPTION value='G' <%=gsel%>>����������ȸ</OPTION>
						<OPTION value='A' <%=asel%>>��������ȸ</OPTION>
					</SELECT>
					</TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../images/bg-01.gif">���</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type="text" name="user_id" value="<%=user_id%>" size="20" readonly>
					<a href="javascript:selectUserInfo();"><img src="../images/bt_search.gif" border="0" align='absmiddle'></a></TD>
			   <TD width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�̸�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type="text" name="user_name" value="<%=user_name%>" size="20" readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�����ڵ�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type="text" name="psm_code" value="<%=psm_code%>" size="20" readonly>
					<a href="javascript:selectPsmInfo();"><img src="../images/bt_search.gif" border="0" align='absmiddle'></a></TD>
			   <TD width="13%" height="25" class="bg_03" background="../images/bg-01.gif">������</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type="text" name="psm_korea" value="<%=psm_korea%>" size="20" readonly>
					</TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='5'></TD></TR>
</TABLE>
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value='<%=pid%>'>
<INPUT type="hidden" name="pjt_grade" value="" size="20">
<INPUT type="hidden" name="div_code" value='<%=div_code%>'>
<INPUT type="hidden" name="div_name" value='<%=div_name%>'>
</FORM> 
</BODY>
</HTML>

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
	var pjt_type = f.pjt_type.value;
	var psm_code = f.psm_code.value;
	if(pjt_type=='G' && psm_code == '') { alert('�����ڵ��� �Է��Ͻʽÿ�.'); return; }

	var pid = f.pid.value;
	if(pid != '') { alert('������ư�� �̿��Ͻʽÿ�.'); return; }

	document.sForm.action='psmViewMgrProcess.jsp';
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
	var pjt_type = f.pjt_type.value;
	var psm_code = f.psm_code.value;
	if(pjt_type=='G' && psm_code == '') { alert('�����ڵ��� �Է��Ͻʽÿ�.'); return; }

	var pid = f.pid.value;
	if(pid == '') { alert('��Ϲ�ư�� �̿��Ͻʽÿ�.'); return; }

	document.sForm.action='psmViewMgrProcess.jsp';
	document.sForm.mode.value='MOD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//��������ϱ�
function contentDelete()
{
	d = confirm("�����Ͻðڽ��ϱ�?");
	if(d == false) return;

	document.sForm.action='psmViewMgrProcess.jsp';
	document.sForm.mode.value='DEL';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//������� ã��
function selectUserInfo()
{

	var strUrl = "../searchUserInfo.jsp?target=sForm.user_id/sForm.user_name";
	newWIndow = wopen(strUrl, "PsmUserInot", "260", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
}

//�������� ã��
function selectPsmInfo()
{

	para = "&target=sForm.psm_code/sForm.psm_korea";	wopen('../../servlet/PsmProcessServlet?mode=search_project'+para,'search_pjt','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
}
// ��Ϻ���
function contentList()
{
	document.sForm.action='psmViewMgrList.jsp';
	document.onmousedown=dbclick;
	document.sForm.submit();
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