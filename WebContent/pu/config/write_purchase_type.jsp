<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%
	PurchaseTypeTable table = new PurchaseTypeTable();
	table = (PurchaseTypeTable)request.getAttribute("PURCHASE_TYPE_INFO");

	String mode = request.getParameter("mode");

	String mid			= table.getMid();
	String type			= table.getPurchaseType();
	String name			= table.getPurchaseName();
	String is_import	= table.getIsImport();
	String is_except	= table.getIsExcept();
	String is_return	= table.getIsReturn();
	String is_using		= table.getIsUsing();
	String account_type	= table.getAccountType();

	String caption ="�������µ��";
	if(mode.equals("modify_purchase_type")) caption = "�������¼���";
%>

<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0">

<form name="eForm" method="post" action="PurchaseConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> <%=caption%></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../pu/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
					<IMG src='../pu/images/bt_cancel.gif' onClick='javascript:history.back();' style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ǰ��������� -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���������ڵ�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' maxlength='5' name='type' value='<%=type%>' class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�������¸�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='name' value='<%=name%>' class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���Կ���</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" <%if(is_import.equals("y")) out.print("checked"); %> name="is_import">�� <input type="radio" value="n" <%if(is_import.equals("n")) out.print("checked"); %> name="is_import">�ƴϿ�</td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���ܿ���</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" <%if(is_except.equals("y")) out.print("checked"); %> name="is_except">�� <input type="radio" value="n" <%if(is_except.equals("n")) out.print("checked"); %> name="is_except">�ƴϿ�</td>
		</tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		
		<tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">��ǰ����</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" <%if(is_return.equals("y")) out.print("checked"); %> name="is_return">�� <input type="radio" value="n" <%if(is_return.equals("n")) out.print("checked"); %> name="is_return">�ƴϿ�</td>
		   <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">��뿩��</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" <%if(is_using.equals("y")) out.print("checked"); %> name="is_using">�� <input type="radio" value="n" <%if(is_using.equals("n")) out.print("checked"); %> name="is_using">�ƴϿ�</td>
          </tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='mid' value='<%=mid%>'>
<input type='hidden' name='account_type' value='<%=account_type%>'>
</form>
</body>
</html>


<script language=javascript>
function checkForm(){
	var f=document.eForm;

	if(f.type.value==""){
		alert("���������ڵ带 �Է��Ͻʽÿ�.");
		f.type.focus();
		return;
	}
	if(f.name.value==""){
		alert("�������¸��� �Է��Ͻʽÿ�.");
		f.name.focus();
		return;
	}
/*
	if(f.account_type.value==""){
		alert("ȸ��ó���ڵ带 �Է��Ͻʽÿ�.");
		f.account_type.focus();
		return;
	}
*/
	f.submit();
}
</script>