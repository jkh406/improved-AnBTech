<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
    bean.openConnection();	

	String query		= "";
	String caption		= "";
	String j			= request.getParameter("j");		// ���	
	String gcode		= request.getParameter("gcode");	// �����ڵ�

	String code			= "";								// ��ǰ�ڵ�
	String name			= "";								// ��ǰ��1

	String ancestor		= request.getParameter("ancestor") == null?"0":request.getParameter("ancestor");
	String level		= request.getParameter("level") == null?"1":request.getParameter("level"); // �ش� �з��� ����
	String pop_title	= "";

	if( j.equals("u")){ // �������
		query = "SELECT * FROM goods_structure WHERE gcode = '" + gcode +"'";
		bean.executeQuery(query);

		while(bean.next()){
			code		= bean.getData("code");
			name		= bean.getData("name");
			level		= bean.getData("glevel");
		}
		caption = "����";
		pop_title = "../images/pop_modify_item.gif";
	}else if(j.equals("a")){ // ����з��� �����з� �Է¸��
		caption = "���";
		pop_title = "../images/pop_add_item.gif";
	}
	
	String level_name = "";
	String level_code = "";
	if(level.equals("1")){
		level_code = "��ǰ���ڵ�";
		level_name = "��ǰ����";
	} else if(level.equals("2")) {
		level_code = "��ǰ�ڵ�";
		level_name = "��ǰ��";
	} else if(level.equals("3")){
		level_code = "�𵨱��ڵ�";
		level_name = "�𵨱���";
	}
%>

<HTML><HEAD><TITLE>��ǰ�з�<%=caption%></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0"  oncontextmenu="return false">
<form name="frm1" method="post" action="category_input_p.jsp" onSubmit="return checkForm()">
<input type=hidden name=j value='<%=j%>'>
<input type=hidden name=gcode value='<%=gcode%>'>
<input type=hidden name=level value='<%=level%>'>
<input type=hidden name=ancestor value='<%=ancestor%>'>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="<%=pop_title%>" hspace="10" alt="��ǰ�з����"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="frm1" style="margin:0">
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif"><%=level_code%></td>
           <td width="70%" height="25" class="bg_04"><input type='text' name='code' value='<%=code%>' size='5' class="text_01"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif"><%=level_name%></td>
           <td width="70%" height="25" class="bg_04"><input type='text' name='name' value='<%=name%>' size='20' class="text_01"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 </tbody></table><br>
	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"> 
			<IMG src='../images/bt_save.gif' onclick='javascript:checkForm()' align='absmiddle' style='cursor:hand'>
		    <IMG src='../images/bt_cancel.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></form></td></tr></table></BODY></HTML>


<script language=javascript>

function checkForm()
{
	var f = document.frm1;

	if(f.code.value == ""){
			alert("��ǰ�ڵ带 �Է��Ͻʽÿ�.");
			f.code.focus();
			return;
	}

	if(<%=level%> == '1' && f.code.value.length != 1){
			alert("��ǰ�� �ڵ�� �����빮�� �Ǵ� ���� 1�ڸ��̾�� �մϴ�.");
			f.code.focus();
			return;
	}

	if(<%=level%> == '2' && f.code.value.length != 2){
			alert("��ǰ �ڵ�� �����빮�� 2�ڸ��̾�� �մϴ�.");
			f.code.focus();
			return;
	}

	if(<%=level%> == '3' && f.code.value.length != 3){
			alert("�𵨱� �ڵ�� �����빮�� �Ǵ� ���� 3�ڸ��̾�� �մϴ�.");
			f.code.focus();
			return;
	}

	if(f.name.value == ""){
			alert("��ǰ���� �Է��Ͻʽÿ�.");
			f.name.focus();
			return;
	}
	f.submit();
}
</script>