<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkDM01.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%

    bean.openConnection();	

	String query		= "";
	String caption		= "";
	String j			= request.getParameter("j");		// 모드	
	String c_id			= request.getParameter("c_id") == null?"1":request.getParameter("c_id");		// 클래스 테이블 관리번호
	String c_name		= "";								// 카테고리 명(기술문서,제안서,기획관련.....);
	String c_code		= "";								// 해당 카테고리명의 대표문자(T,P 등)
	String c_level		= request.getParameter("level") == null?"0":request.getParameter("level"); // 해당 분류의 레벨
	String enable_rev	= "";
	String enable_pjt	= "";
	String enable_model	= "";
	String enable_eco	= "";
	String enable_app	= "";
	String security_level	= "";
	String save_period	= "";
	String tablename	= "";
	String loan_period	= "";

	if(j.equals("u")||j.equals("a")){
		query = "select * from category_data where c_id = '"+c_id+"'";
		System.out.println(query);
		bean.executeQuery(query);

		while(bean.next()){
			c_id			= bean.getData("c_id");			// 분류아이디
			c_name			= bean.getData("c_name");		// 분류명
			c_code			= bean.getData("c_code");		// 해당 분류의 코드
			c_level			= bean.getData("c_level");		// 해당 분류의 레벨
			enable_rev		= bean.getData("enable_rev");	// Revision 관리
			enable_pjt		= bean.getData("enable_pjt");	// Project 관리
			enable_model	= bean.getData("enable_model");	// 형상(모델)관리
			enable_eco		= bean.getData("enable_eco");	// eco 관리
			enable_app		= bean.getData("enable_app");	// 결재 관리
			security_level	= bean.getData("security_level");	// 보안관리
			save_period		= bean.getData("save_period");	// 보관 
			tablename		= bean.getData("tablename");	// 테이블
			loan_period		= bean.getData("loan_period");	// 대출기간
		}
		if(j.equals("a")){
			caption = "분류 추가";
			c_name = "";
		}
		else if(j.equals("u")) caption = "분류 수정";
	}
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 문서분류관리</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300><a href="javascript:checkForm();"><img src="../images/bt_save.gif" border="0" align="absmiddle"></a> <a href="javascript:history.go(-1);"><img src="../images/bt_cancel.gif" border="0" align="absmiddle"></a></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>
<!--내용-->
<form name="fcategory" method="get" action="categoryProcess.jsp" style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">문서분류명</td>
           <td width="30%" height="25" class="bg_04"><Input type="text" name="c_name" value="<%=c_name%>" size="15" maxlength="15" class="text_01"></td>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">대표문자명</td>
           <td width="30%" height="25" class="bg_04"><Input type="text" name="c_code" value="<%=c_code%>" size="1" <% if(!c_level.equals("0"))out.print("readOnly");%> maxlength="1" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">보안등급</td>
           <td width="30%" height="25" class="bg_04">
				<SELECT name="security">
					 <OPTION value="1" <%if (security_level.equals("1")) {%>selected <%}%>> 1급
					 <OPTION value="2" <%if (security_level.equals("2")) {%>selected <%}%>> 2급
					 <OPTION value="3" <%if (security_level.equals("3")) {%>selected <%}%>> 3급
					 <OPTION value="4" <%if (security_level.equals("4")) {%>selected <%}%>> 대외비
					 <OPTION value="5" <%if (security_level.equals("5")) {%>selected <%}%>> 일반
				</SELECT>
		   </td>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">보존기간</td>
           <td width="30%" height="25" class="bg_04">
				<SELECT name="save" >
					 <OPTION value="1" <%if (save_period.equals("1")) {%>selected <%}%>> 1년
					 <OPTION value="3" <%if (save_period.equals("3")) {%>selected <%}%>> 3년
					 <OPTION value="5" <%if (save_period.equals("5")) {%>selected <%}%>> 5년
					 <OPTION value="0" <%if (save_period.equals("0")) {%>selected <%}%>> 영구
				</SELECT>
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">대출기간</td>
           <td width="30%" height="25" class="bg_04"><INPUT type="text" name="loan_period" value="<%=loan_period%>" size="3" maxlength="3" class="text_01">일</td>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">테이블명</td>
           <td width="30%" height="25" class="bg_04"><INPUT type="text" name="tablename" value="<%=tablename%>" <% if(!c_level.equals("0"))out.print("readOnly");%> size="15" maxlength="15" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">리비젼관리</td>
           <td width="30%" height="25" class="bg_04">
				<INPUT type="radio" name="revision" value="y" <% if(enable_rev.equals("y")) out.print("CHECKED");%>>관리함
				<INPUT type="radio" name="revision" value="n" <% if(enable_rev.equals("n")) out.print("CHECKED");%>>관리안함</td>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">프로젝트관리</td>
           <td width="30%" height="25" class="bg_04">
				<INPUT type="radio" name="project" value="y" <% if(enable_pjt.equals("y")) out.print("CHECKED");%>>관리함
				<INPUT type="radio" name="project" value="n" <% if(enable_pjt.equals("n")) out.print("CHECKED");%>>관리안함</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">ECO관리</td>
           <td width="30%" height="25" class="bg_04">
				<INPUT type="radio" name="eco" value="y" <% if(enable_eco.equals("y")) out.print("CHECKED");%>>관리함
				<INPUT type="radio" name="eco" value="n" <% if(enable_eco.equals("n")) out.print("CHECKED");%>>관리안함</td>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">형상(모델)관리</td>
           <td width="30%" height="25" class="bg_04">
				<INPUT type="radio" name="model" value="y" <% if(enable_model.equals("y")) out.print("CHECKED");%>>관리함
				<INPUT type="radio" name="model" value="n" <% if(enable_model.equals("n")) out.print("CHECKED");%>>관리안함</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>

		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">전자결재</td>
           <td width="30%" height="25" class="bg_04">
				<INPUT type="radio" name="eapp" value="y" <% if(enable_app.equals("y")) out.print("CHECKED");%>>관리함
				<INPUT type="radio" name="eapp" value="n" <% if(enable_app.equals("n")) out.print("CHECKED");%>>관리안함</td>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif"></td>
           <td width="30%" height="25" class="bg_04"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

<INPUT type="hidden" name="j" value='<%=j%>'>
<INPUT type="hidden" name="c_id" value='<%=c_id%>'>
<INPUT type="hidden" name="c_level" value='<%=Integer.parseInt(c_level)+1%>'>
</form>
</td></tr></table>
</body>
</html>


<script language=javascript>
function checkForm()
{
	var f = document.fcategory;

	if(f.c_name.value == ""){
			alert("문서분류명을 입력하십시오.");
			f.c_name.focus();
			return;
	}

	if(f.c_code.value == ""){
			alert("대표문자명을 입력하십시오.");
			f.c_code.focus();
			return;
	}

	if(f.loan_period.value == ""){
			alert("대출기간을 입력하십시오.");
			f.loan_period.focus();
			return;
	}

	if(f.tablename.value == ""){
			alert("테이블명을 입력하십시오.");
			f.tablename.focus();
			return;
	}
	f.submit();
}
</script>
</html>