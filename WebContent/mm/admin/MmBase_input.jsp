<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = "�����ڵ��������"
	errorPage	= "../../admin/errorpage.jsp"
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
	
	String pid			= request.getParameter("pid");	// mgr_grade_mgr Table �����ڵ�
	String mgr_type		= "";	// �з����� (MPS,MRP,MFG)
	String mgr_code		= "";	// ���ѱ����ڵ�
	String mgr_name		= "";	// ���ѱ��и�
	String mgr_id		= "";	// ���/�̸�
	String factory_no		= "";	// �μ��ڵ�
	String c_image		= "";

	if( j.equals("u")){ // �������
		query = "SELECT * FROM mfg_grade_mgr WHERE pid = '" + pid +"'";
		bean.executeQuery(query);

		while(bean.next()){
			pid			= bean.getData("pid");
			mgr_type	= bean.getData("mgr_type");
			mgr_code	= bean.getData("mgr_code");
			mgr_name	= bean.getData("mgr_name");
			mgr_id		= bean.getData("mgr_id");
			factory_no	= bean.getData("factory_no");
		}
		caption = "����";
		c_image = "../images/pop_modify_mgr_info.gif";
	}else if(j.equals("a")){ // �Է¸��
		caption = "���";
		c_image = "../images/pop_reg_mgr_info.gif";
	}

%>

<HTML><HEAD><TITLE>����ڰ��� <%=caption%></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<FORM name="frm1" method="post" action="MmBase_process.jsp" onSubmit="return checkForm()">
<INPUT type=hidden name=j value='<%=j%>'>
<INPUT type=hidden name=pid value='<%=pid%>'>


<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
			<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="<%=c_image%>" hspace="10" alt="����� ���Ѱ��� <%=caption%>"></TD></TR>
			<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<TABLE cellspacing=0 cellpadding=2 width="94%" border=0>
		<TBODY><TR><TD height=20 colspan="2"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">�з�����</TD>
				<TD width="70%" height="25" class="bg_04">
				<SELECT NAME="mgr_type"">
			<%	String[] type = {"MPS","MRP","MFG"};
				String[] name = {"�����ȹ[MPS]","�ҿ䷮����[MRP]","������ȹ"};
				for(int si = 0; si < type.length; si++) {
					String SEL = "";
					if(mgr_type.equals(type[si])) SEL = "SELECTED";
					else SEL = "";
					out.println("<OPTION value=" + type[si] + " " + SEL + ">" + name[si]); 
				}
			%>
				</SELECT></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">���ѱ���</TD>
				<TD width="70%" height="25" class="bg_04">
				<SELECT NAME="mgr_code"">
			<%	String[] code = {"USER","MGR"};
				String[] admin = {"�����","������"};
				for(int si = 0; si < code.length; si++) {
					String SEL = "";
					if(mgr_code.equals(code[si])) SEL = "SELECTED";
					else SEL = "";
					out.println("<OPTION value=" + code[si] + " " + SEL + ">" + admin[si]); 
				}
			%>
				</SELECT></TD></TR>
		    <TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">�� ��</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='mgr_id' value='<%=mgr_id%>' size='20' readonly> <a href="Javascript:searchSabun();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">�����ȣ</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='factory_no' value='<%=factory_no%>' size='20' readonly> <a href="Javascript:searchDivision();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
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
            <TD width="100%" height=1 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></TD></TR>
</TABLE>
<input type='hidden' name='mgr_name' value=''>
<input type='hidden' name='mgr_id_name' value=''>
<input type='hidden' name='factory_name' value=''>
</form>
</BODY>
</HTML>


<script language=javascript>

function checkForm()
{
	var f = document.frm1;

	if(f.mgr_id.value == ""){
			alert("����� �Է��ϼ���.");
			f.mgr_id.focus();
			return;
	}
	if(f.factory_no.value == ""){
			alert("�����ȣ�� �Է��ϼ���.");
			f.factory_no.focus();
			return;
	}
	a = f.mgr_code.selectedIndex;
	var mgr_name = f.mgr_code.options[a].text;
	f.mgr_name.value=mgr_name;
	f.submit();
}
//������ϱ�
function searchSabun()
{
	wopen("../searchName.jsp?target=frm1.mgr_id/frm1.mgr_id_name","proxy","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//�����ȣ ã��
function searchDivision()
{
	var f = document.frm1;
	var factory_no = f.factory_no.name;
	var factory_name = f.factory_name.name;
	
	var para = "field="+factory_no+"/"+factory_name;

	url = "../../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&"+para;
	wopen(url,'enterCode','400','227','scrollbars=yes,toolbar=no,status=no,resizable=no');

}
//â
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
</script>