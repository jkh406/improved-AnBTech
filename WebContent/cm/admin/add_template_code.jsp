<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkCM01.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*,com.anbtech.cm.entity.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%!
	SpecCodeTable TABLE;
%>

<%
	String mode = request.getParameter("mode");	// ���

	//���õ� �����׸� ���� ���� ��������
	TABLE = (SpecCodeTable)request.getAttribute("SPEC_INFO");
	String code_big_list	= TABLE.getCodeBig();
	String code_mid_list	= TABLE.getCodeMid();
	String spec_code		= TABLE.getSpecCode();
	String spec_name		= TABLE.getSpecName();
	String spec_value		= TABLE.getSpecValue();
	String spec_unit		= TABLE.getSpecUnit();
	String spec_desc		= TABLE.getSpecDesc();
	String write_exam		= TABLE.getWriteExam();

	//����Ʈ ��������
	ArrayList spec_list = new ArrayList();
	spec_list = (ArrayList)request.getAttribute("SPEC_LIST");
	TABLE = new SpecCodeTable();
	Iterator table_iter = spec_list.iterator();
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../cm/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false" onLoad="display()">
<FORM ACTION="../servlet/CodeMgrServlet" METHOD=POST enctype='multipart/form-data' style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../cm/images/blet.gif"> ǰ�� ǥ�ر԰� ����</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
			  <a href="javascript:checkForm();"><img src="../cm/images/bt_save.gif" border="0" align="absmiddle"></a> 
<%	if(mode.equals("modify_template_code")){	%>
			  <a href="javascript:history.go(-1);"><img src="../cm/images/bt_cancel.gif" border="0" align="absmiddle"></a> 
<%	}	%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
  <TR><TD align="left">
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
         <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">�з� ����</TD>
           <TD width="87%" height="25" class="bg_04" colspan="3">
					<select name="code_big" onChange="javascript:changeClass();">
						<option value="">��з� ����</option><%=code_big_list%>
					</select>
					<select name="code_mid" onChange="javascript:changeClass();">
						<option value="">�ߺз� ����</option><%=code_mid_list%>
					</select>				
					</TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">�׸��ڵ�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="spec_code" size="8" readOnly value="<%=spec_code%>"></td>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">�׸��</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="spec_name" size="25" value="<%=spec_name%>" maxlength="25" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">�׸�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="spec_value" size="35" value="<%=spec_value%>" maxlength="100"></td>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="spec_unit" size="35" value="<%=spec_unit%>" maxlength="50"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">�Է¿�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="write_exam" size="35" value="<%=write_exam%>" maxlength="25"></td>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="spec_desc" size="35" value="<%=spec_desc%>" maxlength="50"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <TR><TD height=10 colspan="4"></TD></TR></TBODY></TABLE></TD></TR>

  <TR><TD align="left" id="table1"><DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:300; overflow:auto;" name="boardframe" onload="ReSizeIframe();">
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=80 align=middle class='list_title'>�׸��ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�׸��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>�׸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�Է¿�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=300 align=middle class='list_title'>����</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	while(table_iter.hasNext()){
		TABLE = (SpecCodeTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=TABLE.getSpecCode()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=TABLE.getSpecName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=TABLE.getSpecValue()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=TABLE.getSpecUnit()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="<%=TABLE.getLinkModify()%>"><img src='../cm/images/lt_modify.gif' border='0'></a></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=TABLE.getWriteExam()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=TABLE.getSpecDesc()%></TD>
			</TR>
			<TR><TD colSpan=13 background="../cm/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></DIV></TD></TR></TABLE>

<input type=hidden name=mode value='<%=mode%>'>
</FORM>
</body>
</html>

<script language="Javascript">

function changeClass(){ 
	var f = document.forms[0];
	var code_b = f.code_big.value;
	var code_m = f.code_mid.value;

	location.href="../servlet/CodeMgrServlet?mode=add_template_code&code_big="+code_b+"&code_mid="+code_m;
}

function checkForm()
{
	var f = document.forms[0];

	if(f.spec_code.value.length != 5){
			alert("�׸��ڵ尡 �ùٸ��� �ʽ��ϴ�. �з��� ����� �����Ͻʽÿ�.");
			return;
	}
	if(f.spec_name.value == ''){
			alert("�׸���� �ݵ�� �Է��ϼž� �մϴ�.");
			f.spec_name.focus();
			return;
	}
	f.submit();
}

function viewCode(code)
{
	var f = document.forms[0];
	var code_b = f.code_big.value;
	var code_m = f.code_mid.value;

	location.href="../servlet/CodeMgrServlet?mode=modify_template_code&code_big="+code_b+"&code_mid="+code_m+"&spec_code="+code;
}

function ReSizeIframe() 
{ 
	table1.height = boardframe.document.body.scrollHeight; 
	table1.width = boardframe.document.body.scrollWidth; 
}

function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 465;
	item_list.style.height = div_h;

}
</script>