<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = "�����ڵ��������"
	language = "java"
	errorPage="../../admin/errorpage.jsp"
	contentType = "text/html;charset=euc-kr"
	import = "java.sql.*, java.io.*, java.util.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
    bean.openConnection();	

	String query		= "";
	String caption		= "";
	String j			= request.getParameter("j");		// ���	
	String flag_name	= request.getParameter("flag_name");
	String flag			= request.getParameter("flag");		// �з����� flag
		
	String pid			= request.getParameter("pid");		// mbom_env Table �����ڵ�
	String m_code	= "";	// �з��� ���� �����ڵ�
	String spec		= "";	// DESCRIPTION
	String tag		= "";	// ���

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

<HTML><HEAD><TITLE>���� TEMPLATE�ڵ���� <%=caption%></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false" >
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
			<TR>
				<TD height="33" valign="middle" bgcolor="#73AEEF">
<%	if( j.equals("a")){ // ��� %>
			<img src="../images/pop_add_temp.gif" hspace="10" alt="TEMPLATE�ڵ����� ���">
<%	} else if( j.equals("u")){ // ���� %>				
			<img src="../images/pop_modify_temp.gif" hspace="10" alt="TEMPLATE�ڵ����� ����">
<%	}		%></TD></TR>
	        <TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	<TR height=131 valign='middle' align='center'><TD>
		<TABLE cellspacing=0 cellpadding=2 width="94%" border=0>
		<TBODY>
			<TR><TD height=20 colspan="2"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">�з�����</TD>
				<TD width="70%" height="25" class="bg_04"><%=flag_name%></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">�����ڵ�</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='m_code' value='<%=m_code%>' size='5' maxlength=5 class='text_01'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">����Description</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='spec' value='<%=spec%>' size='40' maxlength='40' class='text_01'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">�����ڵ����</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='tag' value='<%=tag%>' size='40' maxlength='40' class='text_01'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
		 </TBODY></TABLE><br></TD></TR>
	<!--������-->
		<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
			<TBODY>
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"> 
					<IMG src='../images/bt_save.gif' onclick='javascript:checkForm()' align='absmiddle' style='cursor:hand'>
					<IMG src='../images/bt_cancel.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD></TR>
				<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD>
				</TR>
        </TBODY></TABLE></form></TD></TR></table></BODY></HTML>


<script language=javascript>

function checkForm()
{
	var f = document.frm1;
	var tag_val = f.tag.value;
	var tag_inx = f.tag.value.length;

	
	if(f.m_code.value == ""){
			alert("TEMPLATE�ڵ� �Է��ϼ���.");
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

	if(f.tag.value.charAt(tag_inx-1)!=','){
		alert("�����ڵ�������� �� �� ���ڴ� �޸�(,)�̾�߸� �˴ϴ�.");
		f.tag.focus();
		return;
	}			

	if(!isChecking(document.frm1.tag))	{
		alert("�����ڵ���������� �Է±�Ģ�� ���� �ʽ��ϴ�.");
		f.tag.focus();
		return;
	}

	document.onmousedown = dbclick;

	f.submit();
	}	

	// ���� ��ư Ŭ�� ��, �����߿� �� ��ư ������ ���ϵ��� ó��
	function dbclick(){
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
	// �����ڵ���� �Է����� üũ(����,����,�޸� �� ����!)
	function isChecking(input) {
		var num = "0123456789";
		var comma = ",";
		var uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		var lowercase = "abcdefghijklmnopqrstuvwxyz"; 
		var str = num + comma + uppercase + lowercase;
		return containsCharsOnly(input,str);
	}

	function containsCharsOnly(input,chars) {
		for (var inx = 0; inx < input.value.length; inx++) {
		   if (chars.indexOf(input.value.charAt(inx)) == -1)
			   return false;
		}
		return true;
	}

</script>