<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*"
%>
<%
	String items = request.getParameter("items");

	//ǰ���� �и��Ͽ� �迭�� ��´�.
	StringTokenizer str = new StringTokenizer(items, ",");
	int item_count = str.countTokens();
	String item_a[] = new String[item_count];
	String[][] item = new String[item_count][3];

	for(int i=0; i<item_count; i++){ 
		item_a[i] = str.nextToken();

		StringTokenizer str2 = new StringTokenizer(item_a[i],"|");
		for(int j=0; j<3; j++){ 
			item[i][j] = str2.nextToken();
		}
	}
%>
<html>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<head>
<title>�޸��Է�</title>
</head>

<body topmargin="0" leftmargin="0" oncontextmenu="return false">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_input_memo.gif" border="0" alt="�޸��Է�"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE><BR>

<form name="f1" method="post" action="../../servlet/PurchaseMgrServlet" enctype="multipart/form-data" style='margin:0'>
<TABLE  cellspacing=0 cellpadding=0 width="98%" border="0">
	<TBODY>
		<tr bgcolor="c7c7c7"><td height="1" colspan="3"></td></tr>
		<tr>
           <td width="15%" height="25" class="bg_03" background="../images/bg-01.gif">���ǰ��</td>
           <td width="85%" height="50" class="bg_04" valign="top">
<%
	//�и��� ǰ���� db�� ��´�.
	for(int i = 0;i< item_count; i++){
		String request_no	= item[i][0]; //��û��ȣ
		String order_no		= item[i][1]; //���ֹ�ȣ
		String item_code	= item[i][2]; //ǰ���ڵ�
		out.println("ǰ���ڵ�:" + item_code + ",��û��ȣ:" + request_no + ",���ֹ�ȣ:" + order_no +"<br>");
	}
%>
		   </td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="3"></td></tr>
		<tr>
           <td width="15%" height="25" class="bg_03" background="../images/bg-01.gif">�޸𳻿�</td>
           <td width="85%" height="25" class="bg_04"><textarea rows="5" name="other_info" cols="50"></textarea></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="3"></td></tr>
	   </tbody></table>
<TABLE  cellspacing=0 cellpadding=0 width="98%" border="0"><tr><td height="30" align="left">*�� �޸� �Է½� ������ �޸𳻿��� �� �޸𳻿����� ������Ʈ�˴ϴ�.</td></tr></table>
<input type='hidden' name='mode' value='write_memo'>
<input type='hidden' name='item_code' value='<%=items%>'>

</form>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style='padding-right:10px'><a href="javascript:go('<%=items%>');"><img src="../images/bt_confirm.gif" border="0" align='absmiddle'></a> <a href="javascript:self.close();"><img src="../images/bt_cancel.gif" border="0" align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height="3" bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<Script language = "Javascript">
<!-- 
function go(items){
	var f = document.f1;

	if(f.other_info.value.length < 5){
		alert("�޸𳻿��� �Է��Ͻʽÿ�.(5�� �̻�)");
		f.other_info.focus();
		return;
	}
	f.submit();
}
-->
</Script>