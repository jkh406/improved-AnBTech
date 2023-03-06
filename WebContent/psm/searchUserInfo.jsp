<%@ include file="../admin/configPopUp.jsp"%>
<%@ page 
	info= "임직원 검색"
	language="java" contentType="text/html;charset=KSC5601" 
	errorPage	= "../admin/errorpage.jsp"
	import="java.sql.*, java.io.*, java.util.*"%>

<%
	String target = request.getParameter("target");

	//2개로 분해하기
	String target_id = "";
	String target_name = "";
	int sh = target.indexOf("/");
	if(sh != -1) {
		target_id = target.substring(0,sh);
		target_name = target.substring(sh+1,target.length());
	} else {
		target_id = target;
		target_name = target;
	}

%>

<HTML><HEAD><TITLE>임직원 검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="./images/pop_app_u.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=0 width="94%" border=0>
	   <tbody>
         <tr><td height="12"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1></td></tr>
         <tr>
           <td width="50%" style="padding-left:4px" background="./images/bg-011.gif">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				  <tr><td width="100%" height="250" valign='top'>
						<iframe name="class_tree" src="../admin/UserTreeForSingle.jsp" width="100%" height="250" border="0" frameborder="0" scrolling=yes>
						브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe></td></tr>
				  <tr><td width="100%" height="30" bgcolor="#EAF3FD"  align="center">
					<form name='sForm' method=post action="javascript:go_search();" onSubmit="go_search();" style="margin:0">
						<select name='sDiv'>
							<option value='usr'>사용자명</option>
							<option value='div'>부서명</option>
						</select> <input type='text' name='sWord' size='10'> <a href="javascript:go_search();"><img src="./images/bt_search.gif" border="0" align="absmiddle"></a>
					<input type='hidden' name='target_id' value='<%=target_id%>'>
					<input type='hidden' name='target_name' value='<%=target_name%>'>
					</form></td></tr>
				</table>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height="13" colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="./images/close.gif" width="46" height="19" border="0"></a></TD>
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
		alert("검색어는 2자 이상이어야 합니다.");
		return;
	}
	class_tree.location.href = "../admin/UserTreeForSingle.jsp?sdiv="+sdiv+"&sword="+sword;
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
		//alert("부서단위 선택은 하실 수 없습니다.");
		return;
	}

	var target_id = document.sForm.target_id.value; 
	var target_name = document.sForm.target_name.value; 
	eval("opener.document." + target_id).value = id; 
	eval("opener.document." + target_name).value = name; 
	self.close();

}
//-->
</script>