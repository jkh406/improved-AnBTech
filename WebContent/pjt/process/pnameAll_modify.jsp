<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������� ���μ����� ����"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.pjt.entity.*"
%>

<%
	/*********************************************************************
	 	�ʱ�ȭ ����
	*********************************************************************/
	com.anbtech.pjt.entity.prsCodeTable table;
	String pid = "";
	String prs_code = "";
	String prs_name = "";
	String type = "";

	//-----------------------------------
	//	�������� ���� & ��ü ���� �ľ��ϱ�
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_List");
	table = new prsCodeTable();
	Iterator table_iter = table_list.iterator();

	if(table_iter.hasNext()){
		table = (prsCodeTable)table_iter.next();

		pid=table.getPid();							//������ȣ
		prs_code=table.getPrsCode();				//process name code
		prs_name=table.getPrsName();				//process name
		type=table.getType();						//P:�������, �μ��ڵ�:�μ�����
	}

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//�����ϱ�
function sendModify()
{
	//�����˻�
	var rtn = '';
	rtn = document.eForm.prs_code.value;
	var hs = rtn.substring(0,1);	//���ι���
	var ts = rtn.substring(1,4);	//�Ϸù�ȣ 3�ڸ� ����
	if(rtn.length == 0) { alert('���μ����ڵ带 �Է��Ͻʽÿ�.');  return; }
	else if(rtn.length != 4) { alert('4�ڸ��� �����մϴ�.');  return; }
	else if(!isNaN(hs)) { alert('ù���ڴ� �����빮�ڷ� �Է��Ͻʽÿ�.');  return; }
	else if(isNaN(ts)) { alert('�����ڵ��� ���ڸ��� ���ڷ� �Է��Ͻʽÿ�.');  return; }
	
	rtn = document.eForm.prs_name.value;
	if(rtn.length == 0) { alert('���μ����̸��� �Է��Ͻʽÿ�.');  return; }

	//ó���� �޽��� ���
	document.all['lding'].style.visibility="visible";

	document.eForm.action='../servlet/prsStandardServlet';
	document.eForm.mode.value='PSN_MA';	
	document.eForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif">������� ���μ��������</TD>
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
					<TD align=left width=200><a href="javascript:sendModify();"><img src="../pjt/images/bt_modify.gif" border="0"></a><a href="javascript:history.go(-1)"><img src="../pjt/images/bt_cancel.gif" border="0"></a>
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
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���μ����� �ڵ�</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='prs_code' value='<%=prs_code%>'> �����빮��(1)+����(3)�� �����մϴ�.</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���μ��� �̸�</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='prs_name' value='<%=prs_name%>'></td>
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
<input type='hidden' name='type' value='<%=type%>'>
</form>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">������ ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>
