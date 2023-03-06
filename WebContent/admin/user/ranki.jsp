<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
/* 직급 추가 */

    bean.openConnection();	

	String query = "";
	String caption = "입력";
	String j		=	request.getParameter("j")==null?"a":request.getParameter("j");		// 모드
	String ar_id	=	request.getParameter("ar_id");
	String rank_name	=	"";
	String rank_code	=	"";
	String rank_priorty	=	"";

	if( j.equals("u")){ // 직급 정보 수정
		query = "select * from rank_table where ar_id = '"+ar_id+"'";
		bean.executeQuery(query);

		while(bean.next()){
			rank_name		= bean.getData("ar_name");
			rank_code		= bean.getData("ar_code");
			rank_priorty	= bean.getData("ar_priorty");
		}
		caption		 = "수정";
	}else if(j.equals("a")){ //사용자 추가
		caption = "추가";
	}

%>
<html>
<head><title>직급체계관리</title></head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<body>

<form name="frm1" method="get" action="rankp.jsp"">
	<input type=hidden name=j value='<%=j%>'>
	<input type=hidden name=ar_id value='<%=ar_id%>'>
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 직급체계관리(<%=caption%>)</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
					<IMG src='../images/bt_save.gif' onclick='checkForm()' align='absmiddle' border='0' style='cursor:hand'>
					<IMG src='../images/bt_cancel.gif' onclick='history.back()' align='absmiddle' border='0' style='cursor:hand'>
			  </TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<tr><td align="middle">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	   <tbody>
			<tr>
				<td width="15%" height="25" class="bg_03" background="../images/bg-01.gif">직급명</td>
				<td width="85%" height="25" class="bg_04"><input type="text" name="rank_name" value="<%=rank_name%>" size="20" maxlength="10" class="text_01"></td></tr>
			<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
			<tr>
				<td width="15%" height="25" class="bg_03" background="../images/bg-01.gif">직급코드</td>
				<td width="85%" height="25" class="bg_04"><input type="text" name="rank_code" value="<%=rank_code%>" size="10" maxlength="10" class="text_01"></td></tr>
			 <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
			 <tr>
				<td width="15%" height="25" class="bg_03" background="../images/bg-01.gif">출력순위</td>
				<td width="85%" height="25" class="bg_04"><input type="text" name="rank_priorty" value="<%=rank_priorty%>" size="2" maxlength="2" class="text_01"></td></tr>
			 <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr></tbody></table></td></tr></tbody></table>

</form>
</body>
</html>

<script>
<!--
 
function checkForm()
{
	var f = document.frm1;

	if(f.rank_name.value == ""){
			alert("직급명을 선택하십시오.");
			f.rank_name.focus();
			return false;
	}
	if(f.rank_code.value == ""){
			alert("직급 코드를 선택하십시오.");
			f.rank_code.focus();
			return false;
	}
	if(f.rank_priorty.value == ""){
			alert("출력 순위를 입력하십시오.");
			f.rank_priorty.focus();
			return false;
	}

	f.submit();
}
-->
</script>