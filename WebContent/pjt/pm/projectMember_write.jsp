<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�����η����� ���"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	
	//----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String pjt_code = request.getParameter("pjt_code"); if(pjt_code == null) pjt_code = "";
	String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name")); if(pjt_name == null) pjt_name = "";
	String worker_list = Hanguel.toHanguel(request.getParameter("worker_list"));
			if(worker_list == null)worker_list = "";	
	String sItem = request.getParameter("sItem"); if(sItem == null) sItem = "";
	String sWord = Hanguel.toHanguel(request.getParameter("sWord")); if(sWord == null) sWord = "";
	String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord")); if(pjtWord == null) pjtWord = "";	

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//����ϱ�
function sendSave()
{
	if(document.eForm.pjt_member.value == '')		{alert('���߸���� �Է��Ͻʽÿ�.');			return;}
	if(document.eForm.pjt_mbr_job.value == '')		{alert('�������� �Է��Ͻʽÿ�.');			return;}

	//���ڿ��� '/'����
	var msd = document.eForm.mbr_start_date.value;		//��ȹ�Ⱓ ������
	for(i=0;i<2;i++) msd = msd.replace('/','');	
	var med = document.eForm.mbr_end_date.value;		//��ȹ�Ⱓ �Ϸ���
	for(i=0;i<2;i++) med = med.replace('/','');	

	//�ߺ����� ã��
	var mbr = document.eForm.pjt_member.value;		
	var mbr_id = mbr.split('/');				//���õ� ������ ���
	var worker_list = '<%=worker_list%>';	
	var worker = worker_list.split('|');	
	for(i=0; i<worker.length; i++) {
		if(worker[i] == mbr_id[0]) { alert('�̹� ���߸���� ��ϵǾ����ϴ�.'); return; }
	}

	document.eForm.action='../../servlet/projectManServlet';
	document.eForm.mode.value='PMA_W';
	document.eForm.mbr_start_date.value=msd;
	document.eForm.mbr_end_date.value=med;
	document.eForm.submit();

}
//��� ã��
function searchMan()
{
	window.open("../searchPM.jsp?target=eForm.pjt_member","proxy","width=460,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
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
					<TD valign='middle' class="title"><img src="../images/blet.gif">�����η����� ���</TD>
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
					<TD align=left width=200><a href="javascript:sendSave();"><img src="../images/bt_add.gif" border="0"></a><a href="javascript:history.go(-1)"><img src="../images/bt_cancel.gif" border="0"></a>
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
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�� �� ��</td>
			   <td width="80%" height="25" class="bg_04"><%=pjt_code%> <%=pjt_name%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�� å</td>
			   <td width="80%" height="25" class="bg_04">
					<select name='pjt_mbr_type'>
				<%
					String[] grade_no = {"B","C","D","E","F","G"};
					String[] grade_name = {"��� PL","���� PL","SUB PL","���㰳��","��������","�ܺ��η�"};
					for(int i=0; i<grade_no.length; i++) {
						out.print("<OPTION value='"+grade_no[i]+"'>");
						out.println(grade_name[i]+"</OPTION>");
					}
				%>
				</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">������ �̸�</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='pjt_member' value='' readonly>
					&nbsp;<a href="Javascript:searchMan();"><img src="../images/bt_search2.gif" border="0" align="absmiddle"></a></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�� �� ��</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='mbr_start_date' value='<%=anbdt.getDate(0)%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('mbr_start_date');"><img src="../images/bt_calendar.gif" border="0" valign='absbottom'></A></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�� �� ��</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='mbr_end_date' value='<%=anbdt.getDate(120)%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('mbr_end_date');"><img src="../images/bt_calendar.gif" border="0" valign='absbottom'></A></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�� �� ��</td>
			   <td width="80%" height="25" class="bg_04">
					<select name='mbr_poration'>
				<%
					String[] por = {"0.1","0.2","0.3","0.4","0.5","0.6","0.7","0.8","0.9","1.0"};
					for(int i=0; i<por.length; i++) {
						out.print("<OPTION value='"+por[i]+"'>");
						out.println(por[i]+"</OPTION>");
					}
				%>
				</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">��� ����</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='pjt_mbr_job' value=''></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
<input type='hidden' name='pjtWord' value='<%=pjtWord%>'>
<input type='hidden' name='sItem' value='<%=sItem%>'>
<input type='hidden' name='sWord' value='<%=sWord%>'>
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
