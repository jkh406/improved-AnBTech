<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAP01.jsp"%>
<%@ 	page		
	info= "���ڰ��� ȯ��SETTING"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.text.Hanguel" 				%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	/*****************************************************
	//	ȯ�� SETTING
	//****************************************************/
	String pwdapl = "";				//������ ��й�ȣ ���� (0 / 1)
	String pwdapv = "";				//������ ��й�ȣ ���� (0 / 1)
	String pwdapg = "";				//������ ��й�ȣ ���� (0 / 1)
	String pwdmgr = "";				//���ڰ��� ��й�ȣ �������� ���� (0 / 1)
	String appadmin = "";			//���������� viewing�� �� �ִ� admin
	String appaps = "";				//��Ṯ�� ȭ�鿡 ����� �ִ��ϼ��� �����ϱ�
	String query = "";				//query���� �����

	/*****************************************************
	// �� Setting�ϱ�
	*****************************************************/
	String req = request.getParameter("req"); if(req == null) req ="";
	if(req.equals("SAVE")) {
		pwdapl = request.getParameter("apl");
		pwdapv = request.getParameter("apv");
		pwdapg = request.getParameter("apg");
		pwdmgr = request.getParameter("mgr");
		appadmin = request.getParameter("admin");
		appaps = request.getParameter("appaps");
		query="update app_env set env_value='"+pwdapl+"' where env_name='PWDAPL'";
		bean.execute(query);
		query="update app_env set env_value='"+pwdapv+"' where env_name='PWDAPV'";
		bean.execute(query);
		query="update app_env set env_value='"+pwdapg+"' where env_name='PWDAPG'";
		bean.execute(query);
		query="update app_env set env_value='"+pwdmgr+"' where env_name='PWDMGR'";
		bean.execute(query);
		query="update app_env set env_value='"+appadmin+"' where env_name='APPADMIN'";
		bean.execute(query);
		query="update app_env set env_value='"+appaps+"' where env_name='APPAPS'";
		bean.execute(query);
	}


	/*****************************************************
	// ���� SETTINGȯ�� ��������
	*****************************************************/
	String[] itemColumns = {"env_name","env_value"};
	bean.setTable("APP_ENV");
	bean.setColumns(itemColumns);

	//���ν� ��ȣ ����
	bean.setSearch("env_name","PWDAPL");
	bean.init();
	if(bean.isAvailable()) pwdapl = bean.getData("env_value");
	if(pwdapl == null) pwdapl = "0";

	//����� ��ȣ ����
	bean.setSearch("env_name","PWDAPV");
	bean.init();
	if(bean.isAvailable()) pwdapv = bean.getData("env_value");
	if(pwdapv == null) pwdapv = "0";

	//������ ��ȣ ����
	bean.setSearch("env_name","PWDAPG");
	bean.init();
	if(bean.isAvailable()) pwdapg = bean.getData("env_value");
	if(pwdapg == null) pwdapg = "0";

	//���ڰ��� ��й�ȣ ���� ����
	bean.setSearch("env_name","PWDMGR");
	bean.init();
	if(bean.isAvailable()) pwdmgr = bean.getData("env_value");
	if(pwdmgr == null) pwdmgr = "0";

	//�������� viewing����
	bean.setSearch("env_name","APPADMIN");
	bean.init();
	if(bean.isAvailable()) appadmin = bean.getData("env_value");
	if(appadmin == null) appadmin = "";

	//��Ṯ�� ȭ�鿡 ����� �ִ��ϼ�
	bean.setSearch("env_name","APPAPS");
	bean.init();
	if(bean.isAvailable()) appaps = bean.getData("env_value");
	if(appaps == null) appaps = "";

%>


<html>
<head><title>���ڰ��� ȯ�漳��</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form name="sForm" action="settingApproval.jsp" method="post">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> ���ڰ��� ȯ�漳��</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="javascript:chkPwd();"><IMG src='../images/bt_save.gif' border='0' align=absmiddle></a>
				<IMG src='../../admin/images/bt_cancel.gif' border='0' align='absmiddle'  style='cursor:hand'  onClick="javascript:history.back();">
			  </TD></TR></TBODY></TABLE></TD></TR>
			  <TR><TD height='1' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!--����-->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
	   <!--
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="100%" height="25" colspan='2' ><br>
				&nbsp;&nbsp;&nbsp;[��й�ȣ ����]<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - ����,����,���ν� ��й�ȣ Ȯ�ο��θ� �����մϴ�.<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - ���ڰ���� ��й�ȣ�� ������ �����մϴ�.<br>
				&nbsp;&nbsp;&nbsp;[�������� ����]<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - ��ϵ� �����ڿ� ���Ͽ� ������ ���繮���� �� �� �ֽ��ϴ�.<p></td></tr>
        <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>-->
		<!-- <tr>
			<td width="100%" height="25" colspan='4'><IMG src='../images/gw_yang_app.gif' align='absmiddle'></td></tr>
			<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>-->
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">��й�ȣ �ɼ�</td>
           <td width="80%" height="25" class="bg_04">
				  ��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��
					 <input type='radio' name='apl' <%if(pwdapl.equals("0")) out.println("checked");%> value='0'>ު��ȣ
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='apl' <%if(pwdapl.equals("1")) out.println("checked");%> value='1'>��ȣ
				  <br>��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��
					 <input type='radio' name='apv' <%if(pwdapv.equals("0")) out.println("checked");%> value='0'>ު��ȣ
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='apv' <%if(pwdapv.equals("1")) out.println("checked");%> value='1'>��ȣ
				  <br>��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��
					 <input type='radio' name='apg' <%if(pwdapg.equals("0")) out.println("checked");%> value='0'>ު��ȣ
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='apg' <%if(pwdapg.equals("1")) out.println("checked");%> value='1'>��ȣ
				  <br>��й�ȣ
		 &nbsp;<input type='radio' name='mgr' <%if(pwdmgr.equals("0")) out.println("checked");%> value='0'>�α׺�л��
		 &nbsp;&nbsp;<input type='radio' name='mgr' <%if(pwdmgr.equals("1")) out.println("checked");%> value='1'>�����л��</td>
		  </tr>
		  <!--<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
		  <tr>
			<td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">������ ID(���)</B></td>
			<td width="80%" height="25" class="bg_04">
				<input TYPE=text NAME='admin' SIZE=10 MAXLENGTH=10 value='<%=appadmin%>'><a href="javascript:chkPwd();"> <img src='../images/bt_save.gif' border='0' align=absmiddle></a><br>�����Ⱓ ����� ������ �� �� �ִ� ������ ����� �Է��մϴ�.</td></tr> -->
		<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
		<tr>
			<td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">��Ṯ������ϼ�</B></td>
			<td width="80%" height="25" class="bg_04">
				<input TYPE=text NAME='appaps' SIZE=10 MAXLENGTH=10 value='<%=appaps%>'> &nbsp;��Ṯ���� �� �� �ִ� �ϼ��� �Է��մϴ�.<font color='#FF0000'> ��) </font>30���� �̸� -30</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
		 </tbody></table>
<input type='hidden' name='req'>

</td></tr></table>
</form>
</body>
</html>

<Script language = "Javascript">
 <!-- 
//��й�ȣ Ȯ�ο���
function chkPwd()
{
	document.sForm.action="settingApproval.jsp";
	document.sForm.req.value='SAVE';
	document.sForm.submit();
}
-->
</Script>