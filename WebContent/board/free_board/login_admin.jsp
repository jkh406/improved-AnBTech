<form name=login method=post action='AnBBoard?tablename=<%=tablename%>' enctype='multipart/form-data' onSubmit="if(!this.id.value){alert('ID�� �Է��ϼ���.');this.id.focus();return false;}else if(!this.password.value){alert('��й�ȣ�� �Է��ϼ���.');this.password.focus();return false;}else{return true;}">
<%=input_hidden%>
<table border='0' width='100%'>
<tr><td align='center'>
  <table  width='50%' border='0' cellpadding='0' cellspacing='0' align=center>
    <tr>
      <td colspan=2><br><br></td>
    </tr>
	<tr> 
      <td colspan=2 style="PADDING-BOTTOM: 0px; PADDING-LEFT: 5px; PADDING-RIGHT: 0px; PADDING-TOP: 10px"><center>�Խ��� �����ڷ� �α����մϴ�.</center></td>
    </tr>
    <tr height=1>
      <td colspan=2><hr></td>
    </tr>
    <tr>
      <td colspan=2><br></td>
    </tr>
    <tr>
      <td valign=bottom align=right><B>���̵�</B>&nbsp;&nbsp;</td>
      <td valign=bottom><input type=text name=id size=10 maxlength=20></td>
    </tr>
<script language=javascript>
{
	document.login.id.focus();
}
</script>
    <tr>
      <td colspan=2><br></td>
    </tr>
    <tr>
      <td valign=bottom align=right><B>��й�ȣ</B>&nbsp;&nbsp;</td>
      <td valign=bottom><input type=password name=password size=10 maxlength=20></td>
    </tr>
    <tr>
      <td colspan=2><br></td>
    </tr>
    <tr>
      <td colspan=2><br></td>
    </tr>
    <tr height=1> 
	  <td colspan=2><hr></td>
    </tr>
  </table>
  <table width='50%' border=0 cellpadding=0 cellspacing=0 align=center>
    <tr>
      <td valign=middle align=center>
      <br>
      <input type=submit value="Ȯ��">
      <input onClick=history.go(-1) type=button value="���">
      </td>
    </tr>
  </table>
</td></tr></table>
</form>