<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
/* �з� �߰� ���� */

    bean.openConnection();	

	String query = "";
	String caption = "";
	String j			=	request.getParameter("j");	// ���	
	String ac_id	=	request.getParameter("ac_id");	// Ŭ���� ���̺� ������ȣ
	String ac_name	= "";								// �̸�(�ϵ����׷�,�������߽�..)
	String ac_code	= "";								// �ش� ������ �ڵ�
	String chief_id = "";								// �μ����� ���
	String code		= request.getParameter("code");
	String ac_level = request.getParameter("level") == null?"0":request.getParameter("level"); // �ش� �з��� ����
	String isuse	= request.getParameter("isuse") == null?"y":request.getParameter("isuse");;

	if( j.equals("u")){ // �������
		query = "select * from class_table where ac_id = '"+ac_id+"'";
		bean.executeQuery(query);

		while(bean.next()){
			ac_name		= bean.getData("ac_name");
			ac_code		= bean.getData("ac_code");
			ac_level	= bean.getData("ac_level");
			chief_id	= bean.getData("chief_id");
			code		= bean.getData("code");
			if(chief_id == null) chief_id = "";
		}
		caption ="����";
	}else if(j.equals("")){ // �ֻ��� �з� �Է¸��
		caption = "���";
	}else if(j.equals("a")){ // ����з��� �����з� �Է¸��
		caption = "�߰�";
	}

%>
<HTML>
<head><title>�μ���������</title></head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<BODY leftmargin=0 topmargin=0 oncontextmenu='return false'>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"> <img src="../images/blet.gif" align="absmiddle"> �μ���������(<%=caption%>)</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
					<IMG src='../images/bt_save.gif' onclick='checkForm()' align='absmiddle' border='0' style='cursor:hand'>
					<IMG src='../images/bt_cancel.gif' onclick='history.back()' align='absmiddle' border='0' style='cursor:hand'>
			  </TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9CA9BA'></TD></TR></TBODY></TABLE>

<FORM name="frm1" method="get" action="classp.jsp" style="margin:0">
	
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
   <TBODY>
	<%if(ac_level.equals("0") && (j.equals("u")||j.equals("d"))) {%>
		<input type='hidden' name='j' value='<%=j%>'>
		<input type='hidden' name='ac_level' value='<%=Integer.parseInt(ac_level)%>'>
		<input type='hidden' name='ac_code' value='<%=ac_code%>'>	
		<input type='hidden' name='ac_id' value='<%=ac_id%>'>
		<input type='hidden' name='code' value='<%=code%>'>	
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">ȸ���</TD>
			<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_name" value="<%=ac_name%>" class="text_01" maxlength="25" size="20"></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">��뿩��</TD>
			<TD width="90%" height="25" class="bg_04">
				<select name='isuse'>
					<option value='y'>Ȱ��ȭ</option>
					<option value='n'>��Ȱ��ȭ</option>
				</select>
				<script>document.frm1.isuse.value = '<%=isuse%>'</script></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
	<%} else if(j.equals("")) { %>
		<input type='hidden' name='j' value='<%=j%>'>
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">ȸ���</TD>
			<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_name" value="<%=ac_name%>" class="text_01" maxlength="25" size="20"></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">ȸ���ڵ�</TD>
			<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_code" value="<%=ac_code%>" size="1" maxlength="1" class="text_01"></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
	<% } else {
	%>
	<input type='hidden' name='j' value='<%=j%>'>
	<input type='hidden' name='ac_id' value='<%=ac_id%>'>
	<input type='hidden' name='ac_level' value='<%=Integer.parseInt(ac_level)+1%>'>
	<input type='hidden' name='code' value='<%=code%>'>	
	<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�μ���</TD>
		<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_name" value="<%=ac_name%>" class="text_01" maxlength="25" size="20"></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
	<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�μ��ڵ�</TD>
		<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_code" value="<%=ac_code%>" size="10" maxlength="10" class="text_01"></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
	<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�μ�����</TD>
		<TD width="90%" height="25" class="bg_04"><input type=text name='chief_id' value='<%=chief_id%>' size="10"> * �ش� �μ����� ����� �Է��ϼ���. �Է����� ���� �� �Ϻθ���� ���������� �������� ���� �� �ֽ��ϴ�.</TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
		<!-- ���� -->
		<%if(j.equals("u")) {%>
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">��뿩��</TD>
			<TD width="90%" height="25" class="bg_04">
				<select name='isuse'>
					<option value='y'>Ȱ��ȭ</option>
					<option value='n'>��Ȱ��ȭ</option>
				</select>
				<script>document.frm1.isuse.value = '<%=isuse%>'</script></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
		<%}%>
	    <!---->
	<%}%>	
	</TBODY></TABLE>
	
</FORM>

</BODY></HTML>
<script language=javascript>

function checkForm()
{
	var f = document.frm1;

	if(f.ac_name.value == ""){
		alert("�μ����� �Է��Ͻʽÿ�.");
		f.ac_name.focus();
		return false;
	}
	if(f.ac_code.value == ""){
		alert("�μ��ڵ带 �Է��Ͻʽÿ�.");
		f.ac_code.focus();
		return false;
	}

	if(f.isuse && f.isuse.value == "n"){
		var m = confirm("��Ȱ��ȭ ó���� ���� ���� �� �������� ������ ��ĥ �� �ֽ��ϴ�. ����Ͻðڽ��ϱ�?");
		if(!m) return false;
	}
	f.submit();
}
</script>