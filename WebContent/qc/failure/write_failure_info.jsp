<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*,com.anbtech.qc.entity.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	FailureInfoTable table;
%>

<%
	String mode				= request.getParameter("mode");
	String serial_no_s		= request.getParameter("serial_no_s");
	String serial_no_e		= request.getParameter("serial_no_e");

	//�ҷ����� ��������
	table = (FailureInfoTable)request.getAttribute("FAILURE_INFO");
	String request_no			= table.getRequestNo();
	String item_code			= table.getItemCode();
	String serial_no			= table.getSerialNo();
	String inspection_code		= table.getInspectionCode()==null?"":table.getInspectionCode();
	String why_failure			= table.getWhyFailure();

	//�ҷ� ����Ʈ ��������
	ArrayList failure_list = new ArrayList();
	failure_list = (ArrayList)request.getAttribute("FAILURE_LIST");
	table = new FailureInfoTable();
	Iterator table_iter = failure_list.iterator();
%>

<HTML><HEAD><TITLE>�ҷ��������</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../qc/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
  <TR><TD align="top"><!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../qc/images/pop_reg_badinfo.gif" border='0' alt="�ҷ��������"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE></TD></TR>

 <TR><TD align='middle' valign="top"><!--��ư-->
	<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
        <TR><TD height="30" align="right">
<% if(mode.equals("write_failure_info")){	%>
			<IMG src='../qc/images/bt_add.gif' onClick='javascript:checkForm();' align='absmiddle' style='cursor:hand'> <IMG src='../qc/images/bt_close.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'>
<% }else if(mode.equals("modify_failure_info")){	%>
			<IMG src='../qc/images/bt_modify.gif' onClick='javascript:checkForm();' align='absmiddle' style='cursor:hand'> <IMG src='../qc/images/bt_del.gif' onClick="javascript:delete_item('<%=request_no%>','<%=item_code%>','<%=inspection_code%>','<%=serial_no_s%>','<%=serial_no_e%>');" align='absmiddle' style='cursor:hand'> <IMG src='../qc/images/bt_cancel.gif' onClick="javascript:list('<%=request_no%>','<%=item_code%>');" align='absmiddle' style='cursor:hand'>
<% } %>
		
		</TD></TR></TABLE></TD></TR>

  <TR><TD align='middle' valign="top"><!-- �˻�ǰ������ -->
	<form method=post name="writeForm" action='QualityCtrlServlet' enctype='multipart/form-data' style="margin:0">
	<TABLE  cellspacing=0 cellpadding=0 width="98%" border=0>
		<TBODY>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="20%" height="25" class="bg_03" background="../qc/images/bg-01.gif">�˻��Ƿڹ�ȣ</TD>
			   <TD width="30%" height="25" class="bg_04"><%=request_no%></TD>
			   <TD width="20%" height="25" class="bg_03" background="../qc/images/bg-01.gif">�˻�ǰ���ڵ�</TD>
			   <TD width="30%" height="25" class="bg_04"><%=item_code%></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="20%" height="25" class="bg_03" background="../qc/images/bg-01.gif">�Ϸù�ȣ����</TD>
			   <TD width="80%" height="25" class="bg_04" colspan="3"><%=serial_no_s%> ~ <%=serial_no_e%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE></TD></TR>

  <TR><TD height='5'></TD></TR>
  <TR><TD align='middle' valign="top"><!-- ���������� -->
	<TABLE  cellspacing=0 cellpadding=0 width="98%" border=0>
		<TBODY>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="20%" height="25" class="bg_03" background="../qc/images/bg-01.gif">ǰ���Ϸù�ȣ</TD>
			   <TD width="30%" height="25" class="bg_04"><input type='text' size='17' name='serial_no' class="text_01" maxlength="15" value='<%=serial_no%>' onBlur="check_valid()"></TD>
			   <TD width="20%" height="25" class="bg_03" background="../qc/images/bg-01.gif">�˻��׸��ڵ�</TD>
			   <TD width="30%" height="25" class="bg_04"><input type='text' size='15' name='inspection_code' class="text_01" maxlength="15" value='<%=inspection_code%>' <% if(mode.equals("modify_failure_info")) out.print("readOnly"); %>></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="20%" height="25" class="bg_03" background="../qc/images/bg-01.gif">�ҷ�����</TD>
			   <TD width="80%" height="25" class="bg_04" colspan="3"><input type='text' size='50' name='why_failure' class="text_01" maxlength="50" value='<%=why_failure%>'></TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE></TD></TR>

  <TR><TD height='5'></TD></TR>
  <TR><TD align='middle' valign="top" height="223"><!-- �����ո���Ʈ -->
	 <TABLE border=0 width='98%'><TR><TD align=left><IMG src='../qc/images/title_badinfo_list.gif' border='0' alt='�ҷ�����Ʈ'></TD></TR></TABLE>
	 <TABLE cellSpacing="0" cellPadding="0" width="98%" border="0">
			<TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
				<TR vAlign=middle height=23>
				  <TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
				  <TD noWrap width=80 align=middle class='list_title'>�˻��׸��ڵ�</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
				  <TD noWrap width=100 align=middle class='list_title'>�˻��׸��</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
				  <TD noWrap width=100% align=middle class='list_title'>�ҷ�����</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
				  <TD noWrap width=100 align=middle class='list_title'>ǰ���Ϸù�ȣ</TD>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (FailureInfoTable)table_iter.next();
		inspection_code = "<a href='QualityCtrlServlet?mode=modify_failure_info&request_no="+request_no+"&item_code="+item_code+"&inspection_code="+table.getInspectionCode()+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e+"'>"+table.getInspectionCode()+"</a>";
%>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					  <TD align=middle height="24" class='list_bg'><%=no%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=inspection_code%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getInspectionName()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getWhyFailure()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getSerialNo()%></TD>
				</TR>
				<TR><TD colSpan=9 background="../qc/images/dot_line.gif"></TD></TR>
<%
		no++;	
	}
%>
			</TBODY></TABLE></TD></TR>

  <TR><TD align="left"><!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"> 
		    <IMG src='../qc/images/bt_close.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD>
          </TR>
          <TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR>
        </TBODY></TABLE></TD></TR></TABLE>

<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='request_no' value='<%=request_no%>'>
<input type='hidden' name='item_code' value='<%=item_code%>'>
<input type='hidden' name='serial_no_s' value='<%=serial_no_s%>'>
<input type='hidden' name='serial_no_e' value='<%=serial_no_e%>'>
</form>
</BODY></HTML>

<script language='javascript'>
//�ʼ� �Է»��� üũ
function checkForm(){ 
	var f = document.writeForm;

	if(f.serial_no.value < 3){
		alert("ǰ���Ϸù�ȣ�� �Է��Ͻʽÿ�.");
		f.serial_no.focus();
		return;	
	}

	if(f.inspection_code.value < 3){
		alert("������ �߻��� �˻��׸��ڵ带 �Է��Ͻʽÿ�.");
		f.inspection_code.focus();
		return;	
	}

	if(f.why_failure.value < 3){
		alert("�ҷ��� �߻��� ������ �Է��Ͻʽÿ�.");
		f.why_failure.focus();
		return;	
	}

	f.submit();
}

function list(request_no,item_code){ 
	location.href = "../servlet/QualityCtrlServlet?mode=write_failure_info&request_no="+request_no+"&item_code="+item_code;
}

function delete_item(request_no,item_code,inspection_code,serial_no_s,serial_no_e){ 
	location.href = "../servlet/QualityCtrlServlet?mode=delete_failure_info&request_no="+request_no+"&item_code="+item_code+"&inspection_code="+inspection_code+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e;
}

function check_valid(){
	var f = document.writeForm;

	var serial_no_s = f.serial_no_s.value;
	var serial_no_e = f.serial_no_e.value;

	var no_s = zeroDefect(serial_no_s.substring(serial_no_s.length-4));
	var no_e = zeroDefect(serial_no_e.substring(serial_no_e.length-4));
	var serial = serial_no_s.substring(0,serial_no_s.length-4);
	var input_serial_no = f.serial_no.value;

	is_match = false;
	for(i = no_s; i <= parseInt(no_e); i++){
		if(i < 10) serial_no = serial + "000" + i;
		if(i >= 10 && i < 100) serial_no = serial + "00" + i; 
		if(i >= 100 && i < 1000) serial_no = serial + "0" + i; 
		
		if(serial_no == input_serial_no) is_match = true;
	}

	if(!is_match){
		alert("��ȿ�� �Ϸù�ȣ�� �ƴմϴ�. �Ϸù�ȣ������ Ȯ���Ͻʽÿ�.");
		return;
	}
}

//�Է��� ���ڿ��� ���� '0'�� �����Ѵ�.
function zeroDefect(str)
{
	str	= ''+str;
	var	length = str.length;
	for(var	i=0	; i	< length ; i++ )
	{
		if(str.charAt(0) ==	"0") str = str.substring(1,str.length);
		else					 break;
	}
	if(str.length == 0)	str='0';
	return str;
}

//���ڿ� �տ� '0'��	ä�� ���̸�ŭ��	���ڸ� ��ȯ�Ѵ�.
function getLengthString(str, len)
{
	var	strTemp	= '';
	for(i=0	; i	< len ;	i++) strTemp +=	'0';
	strTemp	+= str;
	return strTemp.substring(strTemp.length-len, strTemp.length);
}
</script>