<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info = "�����ڵ��������"
	language = "java"
	contentType = "text/html;charset=KSC5601"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
    bean.openConnection();	

	String query		= "";
	String caption		= "";
	String j			= request.getParameter("j");		// ���	
	String flag_name	= request.getParameter("flag_name");
	String flag			= request.getParameter("flag");	// �з����� flag
		
	String pid			= request.getParameter("pid");	// mbom_env Table �����ڵ�
	String m_code		= "";	// �з��� ���� �����ڵ�
	String spec			= "";	// DESCRIPTION
	String tag			= "";	// ���

	if( j.equals("u")){ // �������
		query = "SELECT * FROM mbom_env WHERE pid = '" + pid +"'";
		bean.executeQuery(query);

		while(bean.next()){
			pid		= bean.getData("pid");
			flag	= bean.getData("flag");
			m_code	= bean.getData("m_code");
			spec	= bean.getData("spec");
			tag		= bean.getData("tag");
		}
		caption = "����";
	}else if(j.equals("a")){ // ����з��� �����з� �Է¸��
		caption = "���";
	}

%>

<HTML><HEAD><TITLE>�����ڵ����� <%=caption%></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<FORM name="frm1" method="post" action="BmBase_process.jsp" onSubmit="return checkForm()">
<INPUT type=hidden name=j value='<%=j%>'>
<INPUT type=hidden name=flag value='<%=flag%>'>
<INPUT type=hidden name=pid value='<%=pid%>'>


<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
			<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../pop_add_item.gif" hspace="10" alt="�����ڵ�����(����)"></TD></TR>
			<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<TABLE cellspacing=0 cellpadding=2 width="94%" border=0>
		<TBODY><TR><TD height=20 colspan="2"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">�з�����</TD>
				<TD width="70%" height="25" class="bg_04"><%=flag_name%></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">�����ڵ�</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='m_code' value='<%=m_code%>' size='15'></TD></TR>
		    <TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">Description</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='spec' value='<%=spec%>' size='40'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">���</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='tag' value='<%=tag%>' size='40'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR></TBODY></TABLE><br>
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
        </TBODY></TABLE></form></TD></TR></TABLE></BODY></HTML>


<script language=javascript>

function checkForm()
{
	var f = document.frm1;

	if(f.m_code.value == ""){
			alert("�����ڵ� �Է��ϼ���.");
			f.m_code.focus();
			return;
	}
	if(f.spec.value == ""){
			alert("Description�� �Է��ϼ���.");
			f.spec.focus();
			return;
	}
	if(f.tag.value == ""){
			alert("�� �Է��ϼ���.");
			f.tag.focus();
			return;
	}
	f.submit();
}
</script>