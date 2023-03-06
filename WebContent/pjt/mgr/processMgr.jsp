<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���μ��� ���Ѱ���"		
	contentType = "text/html; charset=KSC5601" 		
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

		query = "insert into pjt_grade_mgr(pid,keyname,owner,div_code) values('";
		query += bean.getID()+"','"+keyname+"','"+owner+"','"+div_code+"')";
		bean.execute(query);
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("MOD")) {
		query = "update pjt_grade_mgr set keyname='"+keyname+"',owner='"+owner+"' ";
		query += "where pid='"+pid+"'";
		bean.execute(query);
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from pjt_grade_mgr where pid='"+pid+"'";
		bean.execute(query);
	}
	
	/*********************************************************************
	 	���Ѱ��� LIST��������
	*********************************************************************/
	String[] mgrColumn = {"a.pid","a.keyname","a.owner","a.div_code","b.ac_name"};
	bean.setTable("pjt_grade_mgr a,class_table b");		
	bean.setColumns(mgrColumn);
	query = "where (keyname='PRS_MGR' or keyname='PJT_MGR' or keyname='PJT_MGRD') and (a.div_code = b.ac_code) order by keyname DESC";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] data = new String[cnt][6];
	int n = 0;
	while(bean.isAll()) {
		data[n][0] = bean.getData("pid");
		data[n][1] = bean.getData("keyname");
		data[n][2] = bean.getData("owner");		data[n][2] = data[n][2].replace(';',' ');
		data[n][3] = bean.getData("div_code");
		if(data[n][1].equals("PRS_MGR")) data[n][4] = "���� ���μ���������";
		else if(data[n][1].equals("PJT_MGR")) data[n][4] = "���� ����������";
		else if(data[n][1].equals("PJT_MGRD")) data[n][4] = "�μ� ����������";
		else if(data[n][1].equals("PJT_PML")) data[n][4] = "���� PM(�μ����μ�������)";
		data[n][5] = bean.getData("ac_name");
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
				div_name = data[i][5];
			}
		}
	}

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<script language=javascript>
<!--

//����ϱ�
function write()
{
	var owner = document.sForm.owner.value;
	if(owner.length == 0) { alert('�����ڸ� ���� �Է��Ͻʽÿ�.'); return; }

	document.sForm.action='processMgr.jsp';
	document.sForm.mode.value='ADD';
	document.sForm.submit();
}

//��������ϱ�
function contentModify(pid)
{
	document.sForm.action='processMgr.jsp';
	document.sForm.mode.value='MOD';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}

//��������ϱ�
function contentDelete(pid)
{
	d = confirm("�����Ͻðڽ��ϱ�?");
	if(d == false) return;
	document.sForm.action='processMgr.jsp';
	document.sForm.mode.value='DEL';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}

//���뺸��
function contentView(pid)
{
	document.vForm.action='processMgr.jsp';
	document.vForm.mode.value='VIEW';
	document.vForm.pid.value=pid;
	document.vForm.submit();
}
//������ ã��
function select()
{
	window.open("../searchReceiver.jsp?target=sForm.owner","proxy","width=460,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");

}
-->
</script>
</HEAD>

<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0">

<!-- ��� ���� -->
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> ���μ���/��������� ����</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=><!--��ư �� ����-->
		<TD vAlign=top><form name="sForm" method="post" style="margin:0"> 
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD height=32 align=left width='100%'>&nbsp;&nbsp;
					<input type='hidden' name='pid' value='<%=pid%>'>
					<select name='keyname'>
					<%
						String[] list = {"PRS_MGR","PJT_MGR","PJT_MGRD"};
						String[] list_name = {"�������μ���������","�������������","�μ�����������"};
						String sel="";
						
						for(int i=0; i<list.length; i++) {
							if(keyname.equals(list[i])) sel="selected";
							else sel = "";
							out.println("<option value='"+list[i]+"' "+sel+" >"+list_name[i]+"</option>");
						}
					%>
					<input type='text' name='owner' value='<%=owner%>' readonly>
					<a href='javascript:select()'><img src='../images/bt_search2.gif' border='0' align='absmiddle'></a>
	
					<% if(pid.length() == 0) {
						out.println("<a href='javascript:write()'><IMG src='../images/bt_add.gif' border='0' align='absmiddle'></a>");
					} else {
						out.println("<a href='javascript:contentModify("+pid+")'><IMG src='../images/bt_modify.gif' border='0' align='absmiddle'></a>");
						out.println("<a href='javascript:contentDelete("+pid+")'><IMG src='../images/bt_del.gif' border='0' align='absmiddle'></a>");
					}
					%>	
				</TD>
			</TR>
			<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
			<input type="hidden" name="mode" value=''>
			<input type="hidden" name="pid" value=''>
			</form> 
			</TBODY>
	</TR>
	</TBODY>
</TABLE>

<!-- ����Ʈ ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
    <TBODY>
		<TR vAlign=middle height=25>
			<TD noWrap width=20 align=middle class='list_title'>#</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=200 align=middle class='list_title'>���μ��� ������</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=200 align=middle class='list_title'>���/�̸�</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100% align=middle class='list_title'>�μ���</TD>
		</TR>
	<% if (cnt == 0) { %>
		<TR vAlign=center height=22>
			 <td colspan='11' align="middle">***** ������ �����ϴ�. ****</td>
		</tr> 
	<% } %>	

	<% 
		for(int i=0; i<data.length; i++) {
	%>	
		<form name="vForm" method="post" style="margin:0">
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			<TD align=middle height="24" class='list_bg'><%=i%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'><a href="javascript:contentView('<%=data[i][0]%>');"><%=data[i][4]%></a></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=data[i][2]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=data[i][5]%></TD>
		</TR>
		<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
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