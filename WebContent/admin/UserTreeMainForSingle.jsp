<%@ include file= "configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*"%>

<%
	String target = request.getParameter("target");
%>

<HTML>
<TITLE></TITLE>

<head>
	<title>����� �˻�</title>
</head>
<link rel="stylesheet" type="text/css" href="css/style.css">

<BODY background="" marginwidth="10" topmargin="10" leftmargin="10">
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="200">
  <tr>
    <td width="100%" height="270" valign='top'>
		<iframe name="class_tree" src="UserTreeForSingle.jsp" width="100%" height="350" border="0" frameborder="0">
		�������� �ζ��� �������� �������� �ʰų� ���� �ζ��� �������� ǥ������ �ʵ��� �����Ǿ� �ֽ��ϴ�.</iframe>
	</td>
  </tr>
  <tr><td width="100%" align='center' colspan='2'>
	<form name='sForm' method=post action="javascript:go_search();" onSubmit="go_search();" style="margin:0">
	<select name='sDiv'><option value='usr'>����ڸ�</option><option value='div'>�μ���</option></select> <input type='text' name='sWord' size='10'> <input type = 'button' value ='�˻�' onClick = 'javascript:go_search();'>
	<input type='hidden' name='target' value='<%=target%>'>
	</form>
  </td></tr>
</table>
</BODY></HTML>

<script language='javascript'>
<!--
function go_search()
{
	var f = document.sForm;
	var sdiv = f.sDiv.value;
	var sword = f.sWord.value;

	if(sword.length < 2){
		alert("�˻���� 2�� �̻��̾�� �մϴ�.");
		return;
	}

	class_tree.location.href = "UserTreeForSingle.jsp?sdiv="+sdiv+"&sword="+sword;
}

function go_sel(item)
{
    var fromField=item.split("|")
    var type =  fromField[0];
	var user = fromField[1];

	var fromField2=user.split("/");

	var id;
	var name;
	var rank;

	if(type == "usr"){
		id = fromField2[0];
		rank = fromField2[1];
		name = fromField2[2];
	}else{
		id = fromField2[0];
		rank = "";
		name = fromField2[1];	
	}

	//�뺸�� �ƴ� ��� �μ����� ���� ���ϰ�
	if(type == "div"){
		alert("�μ����� ������ �Ͻ� �� �����ϴ�.");
		return;
	}

	var target = document.sForm.target.value;
	var my_ret = confirm("[" + id + " " + name + "]����(��) �����Ͻðڽ��ϱ�?");
	if(my_ret == true){
		eval("opener.document." + target).value = id + "/" + name;
		self.close();
	}
}
//-->
</script>