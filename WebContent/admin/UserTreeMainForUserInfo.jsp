<%@ include file= "configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*"%>

<HTML>
<head><title>사용자 검색</title>
</head>
<link rel="stylesheet" type="text/css" href="css/style.css">

<BODY background="" marginwidth="0" topmargin="0" leftmargin="0">
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="230">
  <tbody>
  <tr>
    <td valign='top'>
		<iframe name="class_tree" src="UserTreeForUserInfo.jsp" width="230" height="320" border="0" frameborder="0" scrolling=yes>
		브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe>
	</td>
  </tr>
  <tr><td width="100%" height="30" bgcolor="#EAF3FD">
	<form name='sForm' method=post action="javascript:go_search();" onSubmit="go_search();" style="margin:0">
	<table border='0'><tr><td><select name='sDiv'><option value='usr'>이름</option><option value='div'>부서명</option></select></td><td><input type='text' name='sWord' size='10'></td><td><a href='javascript:go_search();'><img src='images/bt_search.gif' border='0' align='absmiddle'></a></td></tr></table>
	</form>
  </td></tr></tbody></table>
</BODY></HTML>


<script language='javascript'>
<!--
function go_search()
{
	var f = document.sForm;
	var sdiv = f.sDiv.value;
	var sword = f.sWord.value;

	if(sword.length < 2){
		alert("검색어는 2자 이상이어야 합니다.");
		return;
	}

	class_tree.location.href = "UserTreeForUserInfo.jsp?sdiv="+sdiv+"&sword="+sword;
}

function go_sel(user)
{
	alert(user);
}
//-->
</script>