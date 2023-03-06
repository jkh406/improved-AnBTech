<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		="java.sql.*, java.io.*, java.util.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%
	String fname	= request.getParameter("fname");
	String code		= request.getParameter("code");
	String name		= request.getParameter("name");
%>

<HTML>
<head><title>모델검색</title>
</head>
<link rel="stylesheet" type="text/css" href="css/style.css">

<HTML><HEAD><TITLE>모델코드 선택</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <TR><TD align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/pop_model_s.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<TABLE cellspacing=0 cellpadding=0 width="100%" border=0>
	   <TBODY>
         <TR>
		   <TD height=22>&nbsp;</TD></TR>
         <TR bgcolor="c7c7c7"><TD height=1></TD></TR>
         <TR>
           <TD width="100%" style="padding-left:4px" background="images/bg-011.gif"><!-- 삽입시작 -->
			<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="230">
			  <tbody>
			  <tr>
				<td valign='top'>
					<iframe name="tree" src="GoodsTreeForModelSearch.jsp" width="320" height="310" border="0" frameborder="0" scrolling=yes>
					브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe>
				</td>
			  </tr>
			  <tr><td width="100%" height="30" bgcolor="#EAF3FD" align='middle'>
				<form name='sForm' method=post action="javascript:go_search();" onSubmit="go_search();" style="margin:0">
				<select name='searchscope'><option value='name'>제품명</option></select> <input type='text' name='searchword' size='10'>
				<a href='javascript:go_search();'><img src='images/bt_search.gif' border='0' align='absmiddle'></a>
				</form>
			  </td></tr></tbody></table><!-- 삽입끝 -->   
		   </TD></TR>
         <TR bgcolor="C7C7C7"><TD height="1"></TD></TR>
         <TR><TD height=20></TD></TR></TBODY></TABLE>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></TD></TR></table></BODY></HTML>

<script language='javascript'>
<!--
function go_search()
{
	var f = document.sForm;
	var searchscope = f.searchscope.value;
	var searchword = f.searchword.value;

	if(searchword.length < 2){
		alert("검색어는 2자 이상이어야 합니다.");
		return;
	}

	tree.location.href = "GoodsTreeForModelSearch.jsp?searchscope="+searchscope+"&searchword="+searchword;
}

function sel(name)
{
	var fromField=name.split("|")
    var mid =  fromField[0];	//모델관리번호
    var name = fromField[1];	//모델명
	var code = fromField[2];	//모델코드

	var sel = confirm("제품명 '" + name + "'를 선택하시겠습니까?");

	if(sel){
		opener.document.<%=fname%>.<%=code%>.value  = code;
		opener.document.<%=fname%>.<%=name%>.value  = name;
		self.close();
	}
}
//-->
</script>