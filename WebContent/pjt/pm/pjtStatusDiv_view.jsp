<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�μ����� �������º��� ���뺸��"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.pjt.entity.*"
%>

<%
	/*********************************************************************
	 	�ʱ�ȭ ����
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	com.anbtech.pjt.entity.projectTable table;
	String pid = "";
	String pjt_code = "";
	String pjt_name = "";
	String in_date = "";
	String mgr_id = login_id;
	String mgr_name = login_name;
	String type = "";
	String pjt_status = "";
	String note = "";

	//-----------------------------------
	//	�������� ���� & ��ü ���� �ľ��ϱ�
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_List");
	table = new projectTable();
	Iterator table_iter = table_list.iterator();

	if(table_iter.hasNext()){
		table = (projectTable)table_iter.next();

		pid=table.getPid();							//������ȣ
		pjt_code=table.getPjtCode();				//project code
		pjt_name=table.getPjtName();				//project name
		type=table.getType();						//P:�������, �μ��ڵ�:�μ�����
		in_date = table.getInDate();
		if(in_date == null) in_date = "";
		pjt_status=table.getPjtStatus();			//project ����
		note = table.getNote();	
		if(note==null) note = "";					//�������
	}

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--

-->
</script>
<BODY topmargin="0" leftmargin="0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif">�������º��� ����</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=right width=700><a href="javascript:self.close()"><img src="../pjt/images/bt_close.gif" border="0"></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����ڵ�</td>
			   <td width="80%" height="25" class="bg_04"><%=pjt_code%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����̸�</td>
			   <td width="80%" height="25" class="bg_04"><%=pjt_name%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��������</td>
			   <td width="80%" height="25" class="bg_04"><%=in_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�������</td>
			   <td width="80%" height="25" class="bg_04">
					<%
					String[] st_code = {"S","0","1","2","3","4"};
					String[] st_name = {"������","�����","������","�Ϸ�","DROP","HOLD"};
					String sel = "";
					for(int i=0; i<st_code.length; i++) {
						if(pjt_status.equals(st_code[i])) out.print(st_name[i]);
					}
					%>
					</td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�������</td>
			   <td width="80%" height="25" class="bg_04">
					<textarea rows=6 cols=60 name='note' value='' readonly><%=note%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>

</body>
</html>
