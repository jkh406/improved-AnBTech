<%@ include file="../../admin/configHead.jsp"%>
<%@ page
	language = "java"
	info = "���ڰ��� ��й�ȣ ����"		
	contentType = "text/html; charset=euc-kr"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	/********************************************************************
		 Get the parameter
	*********************************************************************/
	String old_passwd = request.getParameter("old_passwd");	if(old_passwd == null) old_passwd = "";
	String passwd = request.getParameter("passwd");			if(passwd == null) passwd = "";
	String req = request.getParameter("req");	
	String query = "";
	String Message = "";

	if(req != null) {
		String[] columns = {"env_name","env_value"};
		bean.setTable("APP_ENV");
		bean.setColumns(columns);
		bean.setSearch("env_name",login_id);
		bean.init_unique();
		String cur_pwd = "";
		if(bean.isAvailable()) cur_pwd = bean.getData("env_value"); if(cur_pwd == null) cur_pwd = "";

		//�ű� ���
		if(cur_pwd.length() == 0) {
			query = "insert into app_env(env_name,env_value) values('"+login_id+"','"+passwd+"')";
			bean.execute(query);
			//response.sendRedirect(servlet_path+"/ApprovalMenuServlet?mode=APP_ING");
			out.print("<script>alert('���������� �����Ǿ����ϴ�.');self.close();</script>");
		} 
		//����
		else {
			//������й�ȣ Ȯ��
			if(cur_pwd.equals(old_passwd)) {
				query = "update app_env set env_value='"+passwd+"' where env_name='"+login_id+"'";
				bean.execute(query);
				//response.sendRedirect(servlet_path+"/ApprovalMenuServlet?mode=APP_ING");
				out.print("<script>alert('���������� ����Ǿ����ϴ�.');self.close();</script>");
			}//if
			else Message="NO_PWD";
		}
	}
	
%>
<HTML><HEAD><TITLE>�����й�ȣ ����</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_app_p.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name=f1 method=post action="eleApproval_passwordMgr.jsp" onSubmit="return checkForm()" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_01" background="../images/bg-01.gif">�����й�ȣ</td>
           <td width="65%" height="25" class="bg_02"><input type=password name=old_passwd size=8 maxlength=12></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_01" background="../images/bg-01.gif">����й�ȣ</td>
           <td width="65%" height="25" class="bg_02"><input type=password name=passwd size=8 maxlength=12> &nbsp;4~12�� [����,����]</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_01" background="../images/bg-01.gif">��й�ȣȮ��</td>
           <td width="65%" height="25" class="bg_02"><input type=password name=repasswd size=8 maxlength=12></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
				  <input type="hidden" name="mode" value="passwd">
				  <input type='hidden' name='req'>
                  <a href="javascript:document.f1.submit();"><img src='../images/bt_save.gif' border='0' align='absmiddle'></a> 
				  <a href="javascript:self.close();"><img src='../images/bt_cancel.gif' border='0' align='absmiddle'></a></TD></TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></form></BODY></HTML>

<script language=javascript>
<!--
msg = '<%=Message%>';
if(msg.length !=0) alert("������й�ȣ�� ���� �ʽ��ϴ�.");
function checkForm()
{
	var f = document.f1;
	if(f.passwd.value==""){
		alert("�� ��й�ȣ�� �Է��ϼ���.");
		f.passwd.focus();
		return false;
	}

	if(f.passwd.value.length < 4){
		alert("��й�ȣ�� 4�� �̻� �Է��ϼž� �մϴ�.");
		f.passwd.focus();
		return false;
	}
		
	if(f.passwd.value != f.repasswd.value){
		alert("��й�ȣ�� ��й�ȣȮ���� ���� ���� ���� �ʽ��ϴ�.(��ҹ��� ������)");
		f.repasswd.focus();
		return false;
	}
}
-->
</script>
