<form name=login method=post action='AnBBoard?tablename=<%=tablename%>'  enctype='multipart/form-data' onSubmit="if(!this.password.value){alert('비밀번호를 입력하세요.');this.password.focus();return false;}else{return true;}">
<%=input_hidden%>
  <table width=35% border='0' cellpadding='0' cellspacing='0'>
    <tr <%=t_topbgcolor%> class=kissofgod-head-td height=1> 
      <td colspan=2></td>
    </tr>
	<tr <%=t_rowbgcolor%>> 
      <td colspan=2 style="PADDING-BOTTOM: 0px; PADDING-LEFT: 5px; PADDING-RIGHT: 0px; PADDING-TOP: 10px"><font class=kissofgod-large-font>&nbsp;비밀번호를 입력하세요.</font></td>
    </tr>
    <tr <%=t_rowbgcolor%> class=kissofgod-line3 height=1>
      <td colspan=2></td>
    </tr>
    <tr <%=t_rowbgcolor%>>
      <td colspan=2><br></td>
    </tr>
    <tr <%=t_rowbgcolor%>>
      <td valign=middle align=right> <B>비밀번호</B>&nbsp;&nbsp;</td>
      <td valign=middle><input class=kissofgod-input type=password name=password size=10 maxlength=20></td>
    </tr>
<script language=javascript>
{
	document.login.password.focus();
}
</script>
    <tr <%=t_rowbgcolor%>>
      <td colspan=2><br></td>
    </tr>
    <tr <%=t_rowbgcolor%> class=kissofgod-line2 height=1> 
	  <td colspan=2></td>
    </tr>
  </table>
  <table width=35% border=0 cellpadding=0 cellspacing=0>
    <tr <%=t_rowbgcolor%>>
      <td valign=middle align=center>
	  <br>
      <input class=kissofgod-submit type=submit value=" OK ">
      <input class=kissofgod-submit onClick=history.go(-1) type=button value=" BACK ">
      </td>
    </tr>
  </table>
</form>