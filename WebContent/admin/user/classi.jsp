<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
/* 분류 추가 로직 */

    bean.openConnection();	

	String query = "";
	String caption = "";
	String j			=	request.getParameter("j");	// 모드	
	String ac_id	=	request.getParameter("ac_id");	// 클래스 테이블 관리번호
	String ac_name	= "";								// 이름(하드웨어그룹,무선개발실..)
	String ac_code	= "";								// 해당 조직의 코드
	String chief_id = "";								// 부서장의 사번
	String code		= request.getParameter("code");
	String ac_level = request.getParameter("level") == null?"0":request.getParameter("level"); // 해당 분류의 레벨
	String isuse	= request.getParameter("isuse") == null?"y":request.getParameter("isuse");;

	if( j.equals("u")){ // 수정모드
		query = "select * from class_table where ac_id = '"+ac_id+"'";
		bean.executeQuery(query);

		while(bean.next()){
			ac_name		= bean.getData("ac_name");
			ac_code		= bean.getData("ac_code");
			ac_level	= bean.getData("ac_level");
			chief_id	= bean.getData("chief_id");
			code		= bean.getData("code");
			if(chief_id == null) chief_id = "";
		}
		caption ="수정";
	}else if(j.equals("")){ // 최상위 분류 입력모드
		caption = "등록";
	}else if(j.equals("a")){ // 현재분류의 하위분류 입력모드
		caption = "추가";
	}

%>
<HTML>
<head><title>부서조직관리</title></head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<BODY leftmargin=0 topmargin=0 oncontextmenu='return false'>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"> <img src="../images/blet.gif" align="absmiddle"> 부서조직관리(<%=caption%>)</TD>
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
  <TR><TD height='1' bgcolor='#9CA9BA'></TD></TR></TBODY></TABLE>

<FORM name="frm1" method="get" action="classp.jsp" style="margin:0">
	
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
   <TBODY>
	<%if(ac_level.equals("0") && (j.equals("u")||j.equals("d"))) {%>
		<input type='hidden' name='j' value='<%=j%>'>
		<input type='hidden' name='ac_level' value='<%=Integer.parseInt(ac_level)%>'>
		<input type='hidden' name='ac_code' value='<%=ac_code%>'>	
		<input type='hidden' name='ac_id' value='<%=ac_id%>'>
		<input type='hidden' name='code' value='<%=code%>'>	
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">회사명</TD>
			<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_name" value="<%=ac_name%>" class="text_01" maxlength="25" size="20"></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">사용여부</TD>
			<TD width="90%" height="25" class="bg_04">
				<select name='isuse'>
					<option value='y'>활성화</option>
					<option value='n'>비활성화</option>
				</select>
				<script>document.frm1.isuse.value = '<%=isuse%>'</script></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
	<%} else if(j.equals("")) { %>
		<input type='hidden' name='j' value='<%=j%>'>
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">회사명</TD>
			<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_name" value="<%=ac_name%>" class="text_01" maxlength="25" size="20"></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">회사코드</TD>
			<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_code" value="<%=ac_code%>" size="1" maxlength="1" class="text_01"></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
	<% } else {
	%>
	<input type='hidden' name='j' value='<%=j%>'>
	<input type='hidden' name='ac_id' value='<%=ac_id%>'>
	<input type='hidden' name='ac_level' value='<%=Integer.parseInt(ac_level)+1%>'>
	<input type='hidden' name='code' value='<%=code%>'>	
	<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">부서명</TD>
		<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_name" value="<%=ac_name%>" class="text_01" maxlength="25" size="20"></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
	<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">부서코드</TD>
		<TD width="90%" height="25" class="bg_04"><input type="text" name="ac_code" value="<%=ac_code%>" size="10" maxlength="10" class="text_01"></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
	<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">부서장사번</TD>
		<TD width="90%" height="25" class="bg_04"><input type=text name='chief_id' value='<%=chief_id%>' size="10"> * 해당 부서장의 사번을 입력하세요. 입력하지 않을 시 일부모듈이 정상적으로 동작하지 않을 수 있습니다.</TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
		<!-- 수정 -->
		<%if(j.equals("u")) {%>
		<TR><TD width="10%" height="25" class="bg_03" background="../images/bg-01.gif">사용여부</TD>
			<TD width="90%" height="25" class="bg_04">
				<select name='isuse'>
					<option value='y'>활성화</option>
					<option value='n'>비활성화</option>
				</select>
				<script>document.frm1.isuse.value = '<%=isuse%>'</script></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="2"></TD></TR>
		<%}%>
	    <!---->
	<%}%>	
	</TBODY></TABLE>
	
</FORM>

</BODY></HTML>
<script language=javascript>

function checkForm()
{
	var f = document.frm1;

	if(f.ac_name.value == ""){
		alert("부서명을 입력하십시오.");
		f.ac_name.focus();
		return false;
	}
	if(f.ac_code.value == ""){
		alert("부서코드를 입력하십시오.");
		f.ac_code.focus();
		return false;
	}

	if(f.isuse && f.isuse.value == "n"){
		var m = confirm("비활성화 처리는 하위 조직 및 구성원에 영향을 미칠 수 있습니다. 계속하시겠습니까?");
		if(!m) return false;
	}
	f.submit();
}
</script>