<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "����۾��Ϸ� ���ο�û �ۼ��ϱ�"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();

	//---------------------------------------------------------
	//	�Ķ���� �ޱ�
	//--------------------------------------------------------
	String pjt_code="",pjt_name="",node_code="",pjtWord="",sItem="",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");

	para = new projectTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();				//������Ʈ�ڵ�
		pjt_name = para.getPjtName();				//������Ʈ��
		node_code = para.getNodeCode();				//����ڵ�
		pjtWord = para.getPjtword();				//�ְ�[W]/����[M] ����
		sItem = para.getSitem();		
		sWord = para.getSword();
	}

	//----------------------------------------------------
	//	�ش����/����� ���� �Է����� �б� 
	//----------------------------------------------------
	String node_name="";
	String psd="",ped="",csd="",ced="",rsd="",red="";		//DB���� ��ȹ��,������,�Ϸ���
	String dpsd="",dped="",dcsd="",dced="",drsd="",dred="";	//ȭ����¿�

	com.anbtech.pjt.entity.projectTable node;
	ArrayList node_list = new ArrayList();
	node_list = (ArrayList)request.getAttribute("NODE_List");
	node = new projectTable();
	Iterator node_iter = node_list.iterator();
	
	if(node_iter.hasNext()) {
		node = (projectTable)node_iter.next();
		node_name = node.getNodeName();

		psd = node.getPlanStartDate();	if(psd == null) psd = "";
		if(psd.length() != 0) dpsd = psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		else dpsd = "";
		ped = node.getPlanEndDate();	if(ped == null) ped = "";
		if(ped.length() != 0) dped = ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);
		else dped = "";

		csd = node.getChgStartDate();	if(csd == null) csd = "";
		if(csd.length() != 0) dcsd = csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		else dcsd = "";
		ced = node.getChgEndDate();	if(ced == null) ced = "";
		if(ced.length() != 0) dced = ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		else dced = "";

		rsd = node.getRstStartDate();	if(rsd == null) rsd = "";
		if(rsd.length() != 0) drsd = rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		else drsd = "";
		red = node.getRstEndDate();	if(red == null) red = "";
		if(red.length() != 0) dred = red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);
		else dred = anbdt.getDate(0);
	}

	//���ϱ��ϱ�
	int yy = Integer.parseInt(anbdt.getYear());
	int mm = Integer.parseInt(anbdt.getMonth());
	int dd = Integer.parseInt(anbdt.getDates());
	int day = anbdt.getDay(yy,mm,dd);
	String days = "��";
	if(day == 1) days = "��";	else if(day == 2) days = "��";	else if(day == 3) days = "ȭ";
	else if(day == 4) days = "��";	else if(day == 5) days = "��";	else if(day == 6) days = "��";
	else if(day == 7) days = "��";
	days = days + "����";
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//���� ��û�ϱ�
function nodeAppReq()
{
	var con = document.eForm.evt_content.value;
	if(con.length < 10) { alert('���೻������ 10���̻��Դϴ�.\n\n�Է��� �ٽ� ����Ͻʽÿ�.'); return; }

	document.eForm.action='../servlet/projectEventServlet';
	document.eForm.mode.value='PSM_EA';
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
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> ���Ϸ� ���ο�û �ۼ�</TD>
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
					<TD align=left width='80%'>������ : <%=pjt_code%> <%=pjt_name%> </TD>
					<TD align=left width='20%'><a href="javascript:nodeAppReq();">���ο�û</a>
					<a href="javascript:history.go(-1);"><img src="../pjt/images/bt_cancel.gif" border=0></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����̸�</td>
			   <td width="40%" height="25" class="bg_04"><%=node_code%> <%=node_name%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�ۼ���</td>
			   <td width="40%" height="25" class="bg_04"><%=login_name%>
					<input type='hidden' name='user_id' value='<%=login_id%>'>
					<input type='hidden' name='user_name' value='<%=login_name%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��ȹ�Ⱓ</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_start_date' value='<%=dpsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='plan_end_date' value='<%=dped%>' size=10 readonly></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='chg_start_date' value='<%=dcsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=dced%>' size=10 readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='rst_start_date' value='<%=drsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='rst_end_date' value='<%=dred%>' size=10 readonly></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�Է±���</td>
			   <td width="40%" height="25" class="bg_04">���ο�û &nbsp;&nbsp;[<%=anbdt.getDate()%> <%=days%>] <input type='hidden' name='wm_type' value='N' size=10></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���೻��<br>�� ��</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_content' rows='7' cols='80'></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">������<br>�� ��</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_note' rows='7' cols='80'></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�̽�����<br>�� ��</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_issue' rows='7' cols='80'></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value=''>
<input type='hidden' name='in_date' value='<%=anbdt.getDate()%>'>
<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
<input type='hidden' name='node_code' value='<%=node_code%>'>
<input type='hidden' name='node_name' value='<%=node_name%>'>
<input type='hidden' name='pjtWord' value='<%=pjtWord%>'>
<input type='hidden' name='sItem' value='<%=sItem%>'>
<input type='hidden' name='sWord' value='<%=sWord%>'>
<input type='hidden' name='node_status' value='11'>
<input type='hidden' name='progress' value='1.0'>
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
