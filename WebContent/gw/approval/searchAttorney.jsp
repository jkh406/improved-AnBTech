<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*"%>

<%
	String target = request.getParameter("target");

	//2���� �����ϱ�
	String target_pg = "";
	String target_id = "";
	int sh = target.indexOf("/");
	if(sh != -1) {
		target_pg = target.substring(0,sh);
		target_id = target.substring(sh+1,target.length());
	} else {
		target_pg = target;
		target_id = target;
	}
%>

<HTML><HEAD><TITLE>������ �˻�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_app_u.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=0 width="94%" border=0>
	   <tbody>
         <tr><td height="12"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1></td></tr>
         <tr>
           <td width="50%" style="padding-left:4px" background="../images/bg-011.gif">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				  <tr><td width="100%" height="250" valign='top'>
						<iframe name="class_tree" src="../../admin/UserTreeForSingle.jsp" width="100%" height="250" border="0" frameborder="0" scrolling=yes>
						�������� �ζ��� �������� �������� �ʰų� ���� �ζ��� �������� ǥ������ �ʵ��� �����Ǿ� �ֽ��ϴ�.</iframe></td></tr>
				  <tr><td width="100%" height="30" bgcolor="#EAF3FD"  align="center">
					<form name='sForm' method=post action="javascript:go_search();" onSubmit="go_search();" style="margin:0">
						<select name='sDiv'>
							<option value='usr'>����ڸ�</option>
							<option value='div'>�μ���</option>
						</select> <input type='text' name='sWord' size='10'> <a href="javascript:go_search();"><img src='../images/bt_search2.gif' border='0' align='absmiddle'></a>
					<input type='hidden' name='target_pg' value='<%=target_pg%>'>
					<input type='hidden' name='target_id' value='<%=target_id%>'>
					</form></td></tr>
				</table>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height="13" colspan="2"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="../images/bt_close.gif" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>


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

	class_tree.location.href = "../../admin/UserTreeForSingle.jsp?sdiv="+sdiv+"&sword="+sword;
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

	var target_pg = document.sForm.target_pg.value;
	var target_id = document.sForm.target_id.value;

//	opener.document.eForm.action=target_pg;
	eval("opener.document." + target_id).value = id+'/'+name;
//	opener.document.eForm.submit();

	self.close();

}
//-->
</script>