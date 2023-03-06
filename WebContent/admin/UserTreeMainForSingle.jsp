<%@ include file= "configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*"%>

<%
	String target = request.getParameter("target");
%>

<HTML>
<TITLE></TITLE>

<head>
	<title>사용자 검색</title>
</head>
<link rel="stylesheet" type="text/css" href="css/style.css">

<BODY background="" marginwidth="10" topmargin="10" leftmargin="10">
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="200">
  <tr>
    <td width="100%" height="270" valign='top'>
		<iframe name="class_tree" src="UserTreeForSingle.jsp" width="100%" height="350" border="0" frameborder="0">
		브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe>
	</td>
  </tr>
  <tr><td width="100%" align='center' colspan='2'>
	<form name='sForm' method=post action="javascript:go_search();" onSubmit="go_search();" style="margin:0">
	<select name='sDiv'><option value='usr'>사용자명</option><option value='div'>부서명</option></select> <input type='text' name='sWord' size='10'> <input type = 'button' value ='검색' onClick = 'javascript:go_search();'>
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
		alert("검색어는 2자 이상이어야 합니다.");
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

	//통보가 아닌 경우 부서단위 선택 못하게
	if(type == "div"){
		alert("부서단위 선택은 하실 수 없습니다.");
		return;
	}

	var target = document.sForm.target.value;
	var my_ret = confirm("[" + id + " " + name + "]님을(를) 선택하시겠습니까?");
	if(my_ret == true){
		eval("opener.document." + target).value = id + "/" + name;
		self.close();
	}
}
//-->
</script>