<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "BOM GROUP����"		
	contentType = "text/html; charset=KSC5601" 
	errorPage="../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	/*********************************************************************
	 	���ε����� �ޱ�
	*********************************************************************/
	String query = "",div_name="",div_code="";
	String mode = request.getParameter("mode"); if(mode == null) mode = "";
	String pid = request.getParameter("pid"); if(pid == null) pid = ""; 
	String keyname = request.getParameter("keyname"); if(keyname == null) keyname = ""; 
	String owner = Hanguel.toHanguel(request.getParameter("owner")); if(owner == null) owner = ""; 
	String msg = "";

	/*********************************************************************
	 	����ϱ�
	*********************************************************************/
	if(mode.equals("ADD")) {
		//������ڵ� ���ϱ�
		String id = owner.substring(0,owner.indexOf("/"));
		String[] idColumn = {"b.ac_code"};
		bean.setTable("user_table a,class_table b");		
		bean.setColumns(idColumn);
		query = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
		bean.setSearchWrite(query);
		bean.init_write();
		if(bean.isAll()) div_code = bean.getData("ac_code");
		
		//BOM�� �ۼ��ϰ� �ִ� FG�ڵ������� �Ǵ��Ѵ�. : �ۼ����� BOM�� FG�ڵ忡 ���ؼ��� �Է°���
		String fg_code = "";
		String[] bomColumn = {"fg_code"};
		bean.setTable("mbom_master");		
		bean.setColumns(bomColumn);
		query = "where fg_code='"+keyname+"' and bom_status in('0','1','2','3')";
		bean.setSearchWrite(query);
		bean.init_write();
	
		if(bean.isAll()) fg_code = bean.getData("fg_code");

		//��ϵ��� ���� �˻��ϱ�
		String one = "";
		String[] bmColumn = {"owner"};
		bean.setTable("mbom_grade_mgr");		
		bean.setColumns(bmColumn);
		query = "where owner like '%"+owner+"%' and keyname ='"+keyname+"'";
		bean.setSearchWrite(query);
		bean.init_write();
		if(bean.isAll()) one = bean.getData("owner");
		
		if(!one.equals(owner) && (fg_code.length() !=0)){
			query = "insert into mbom_grade_mgr(pid,keyname,owner,div_code) values('";
			query += bean.getID()+"','"+keyname+"','"+owner+"','"+div_code+"')";
			bean.execute(query);
		}
		else if(fg_code.length() ==0)msg = "�ۼ����� BOM�� ��츸 ��ϰ����մϴ�.";
		else msg = "�̹� ��ϵǾ� �ֽ��ϴ�.";
		owner="";	//clear
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("MOD")) {
		query = "update mbom_grade_mgr set keyname='"+keyname+"',owner='"+owner+"' ";
		query += "where pid='"+pid+"'";
		bean.execute(query);
		owner="";	//clear
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from mbom_grade_mgr where pid='"+pid+"'";
		bean.execute(query);
		owner="";	//clear
	}
	
	/*********************************************************************
	 	GROUP���� LIST��������
	*********************************************************************/
	String[] mgrColumn = {"a.pid","a.keyname","a.owner","a.div_code","b.ac_name"};
	bean.setTable("mbom_grade_mgr a,class_table b");		
	bean.setColumns(mgrColumn);
	query = "where (keyname='"+keyname+"') and (a.div_code = b.ac_code) order by owner DESC";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] data = new String[cnt][5];
	int n = 0;
	while(bean.isAll()) {
		data[n][0] = bean.getData("pid");
		data[n][1] = bean.getData("keyname");
		data[n][2] = bean.getData("owner");		data[n][2] = data[n][2].replace(';',' ');
		data[n][3] = bean.getData("div_code");
		data[n][4] = bean.getData("ac_name");
		n++;
	} //while

	/*********************************************************************
	 	�������� ���뺸��
	*********************************************************************/
	if(mode.equals("VIEW")) {
		for(int i=0; i<n; i++) {
			if(data[i][0].equals(pid)) {
				keyname = data[i][1];
				owner = data[i][2];
				div_name = data[i][4];
			}
		}
	}

%>

<HTML>
<HEAD><TITLE>BOM GROUP ����</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<script language=javascript>
<!--
//�޽��� ���
var msg = '<%=msg%>';
if(msg.length > 0) {
	alert(msg);
}
//����ϱ�
function write()
{
	var owner = document.sForm.owner.value;
	if(owner.length == 0) { alert('�ش����� ���� �Է��Ͻʽÿ�.'); return; }

	document.sForm.action='BmGroupMgr.jsp';
	document.sForm.mode.value='ADD';
	document.sForm.submit();
}
//��������ϱ�
function contentModify(pid)
{
	document.sForm.action='BmGroupMgr.jsp';
	document.sForm.mode.value='MOD';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}
//��������ϱ�
function contentDelete(pid)
{
	d = confirm("�����Ͻðڽ��ϱ�?");
	if(d == false) return;
	document.sForm.action='BmGroupMgr.jsp';
	document.sForm.mode.value='DEL';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}
//���뺸��
function contentView(pid)
{
	var keyname = document.sForm.keyname.value;

	document.vForm.action='BmGroupMgr.jsp';
	document.vForm.mode.value='VIEW';
	document.vForm.pid.value=pid;
	document.vForm.keyname.value=keyname;
	document.vForm.submit();
}
//��Ϻ���
function regView()
{
	document.sForm.action='BmGroupMgr.jsp';
	document.sForm.owner.value="";
	document.sForm.pid.value="";
	document.sForm.submit();
}
//������ ã��
function select()
{
	wopen("../searchReceiver.jsp?target=sForm.owner","mgr","510","467","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//â
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>
</HEAD>

<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0"  oncontextmenu="return false">
<!-- ��� ���� -->
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
			<TD vAlign=top>
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
					<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD">
						<TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> BOM GROUP ����ڰ���</TD>
					</TR></TBODY></TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=><!--��ư �� ����-->
			<TD vAlign=top><form name="sForm" method="post" style="margin:0"> 
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border='0'>
				<TBODY>
					<TR>
						<TD height=32 align=left width='100%' style='padding-left:5px'>
							<INPUT type='hidden' name='pid' value='<%=pid%>'>
								<IMG src='../images/fg_code.gif' align='absmiddle' border='0'>
							<INPUT type='text' name='keyname' size='12' value='<%=keyname%>'>
								<a href='javascript:regView()'><img src='../images/bt_search3.gif' border='0' align='absmiddle'></a>&nbsp;&nbsp;&nbsp;&nbsp;
							<INPUT type='text' name='owner' value='<%=owner%>' size='16' readonly>
								<a href='javascript:select()'><img src='../images/bt_search2.gif' border='0' align='absmiddle'></a>
	
					<% if(owner.length() == 0) {
						out.println("<a href='javascript:write()'><IMG src='../images/bt_add.gif' border='0' align='absmiddle'></a>");
					} else {
						//out.println("<a href='javascript:contentModify("+pid+")'><IMG src='../images/bt_modify.gif' border='0' align='absmiddle'></a>");
						out.println("<a href='javascript:contentDelete("+pid+")'><IMG src='../images/bt_del.gif' border='0' align='absmiddle'></a>");
					}
					%>	
						</TD>
					</TR>
					<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
							<INPUT type="hidden" name="mode" value=''>
							<INPUT type="hidden" name="pid" value=''>
			</FORM> 
			</TBODY></TR></TBODY>
</TABLE>

<!-- ����Ʈ ���� -->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
		<TBODY>
			<TR vAlign=middle height=25>
				<TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>F/G�ڵ�</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>���/�̸�</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
				<TD noWrap width=120 align=middle class='list_title'>�μ���</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'></TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
	<% 
			if (cnt == 0) { 
	%>
			<TR vAlign=center height=22>
				 <td colspan='10' align="middle">***** ������ �����ϴ�. ****</td>
			</tr> 
	<%		} 
	%>	

	<% 
		for(int i=0; i<data.length; i++) {
	%>	
			<form name="vForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=i+1%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=data[i][1]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><a href="javascript:contentView('<%=data[i][0]%>');"><%=data[i][2]%></a></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=data[i][4]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'></TD>
			</TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
	<% 
		}  //for

	%>
	</TBODY>
</TABLE>

<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value=''>
<INPUT type="hidden" name="keyname" value=''>
</form>
</body>
</html>