<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAP01.jsp"%>
<%@ 	page		
	info= "전자결재 환경SETTING"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.text.Hanguel" 				%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	/*****************************************************
	//	환경 SETTING
	//****************************************************/
	String pwdapl = "";				//승인자 비밀번호 묻기 (0 / 1)
	String pwdapv = "";				//승인자 비밀번호 묻기 (0 / 1)
	String pwdapg = "";				//승인자 비밀번호 묻기 (0 / 1)
	String pwdmgr = "";				//전자결재 비밀번호 별도관리 묻기 (0 / 1)
	String appadmin = "";			//삭제문서를 viewing할 수 있는 admin
	String appaps = "";				//기결문서 화면에 출력할 최대일수를 결정하기
	String query = "";				//query문장 만들기

	/*****************************************************
	// 값 Setting하기
	*****************************************************/
	String req = request.getParameter("req"); if(req == null) req ="";
	if(req.equals("SAVE")) {
		pwdapl = request.getParameter("apl");
		pwdapv = request.getParameter("apv");
		pwdapg = request.getParameter("apg");
		pwdmgr = request.getParameter("mgr");
		appadmin = request.getParameter("admin");
		appaps = request.getParameter("appaps");
		query="update app_env set env_value='"+pwdapl+"' where env_name='PWDAPL'";
		bean.execute(query);
		query="update app_env set env_value='"+pwdapv+"' where env_name='PWDAPV'";
		bean.execute(query);
		query="update app_env set env_value='"+pwdapg+"' where env_name='PWDAPG'";
		bean.execute(query);
		query="update app_env set env_value='"+pwdmgr+"' where env_name='PWDMGR'";
		bean.execute(query);
		query="update app_env set env_value='"+appadmin+"' where env_name='APPADMIN'";
		bean.execute(query);
		query="update app_env set env_value='"+appaps+"' where env_name='APPAPS'";
		bean.execute(query);
	}


	/*****************************************************
	// 현재 SETTING환경 가져오기
	*****************************************************/
	String[] itemColumns = {"env_name","env_value"};
	bean.setTable("APP_ENV");
	bean.setColumns(itemColumns);

	//승인시 암호 묻기
	bean.setSearch("env_name","PWDAPL");
	bean.init();
	if(bean.isAvailable()) pwdapl = bean.getData("env_value");
	if(pwdapl == null) pwdapl = "0";

	//검토시 암호 묻기
	bean.setSearch("env_name","PWDAPV");
	bean.init();
	if(bean.isAvailable()) pwdapv = bean.getData("env_value");
	if(pwdapv == null) pwdapv = "0";

	//협조시 암호 묻기
	bean.setSearch("env_name","PWDAPG");
	bean.init();
	if(bean.isAvailable()) pwdapg = bean.getData("env_value");
	if(pwdapg == null) pwdapg = "0";

	//전자결재 비밀번호 별도 관리
	bean.setSearch("env_name","PWDMGR");
	bean.init();
	if(bean.isAvailable()) pwdmgr = bean.getData("env_value");
	if(pwdmgr == null) pwdmgr = "0";

	//삭제문서 viewing권한
	bean.setSearch("env_name","APPADMIN");
	bean.init();
	if(bean.isAvailable()) appadmin = bean.getData("env_value");
	if(appadmin == null) appadmin = "";

	//기결문서 화면에 출력할 최대일수
	bean.setSearch("env_name","APPAPS");
	bean.init();
	if(bean.isAvailable()) appaps = bean.getData("env_value");
	if(appaps == null) appaps = "";

%>


<html>
<head><title>전자결재 환경설정</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form name="sForm" action="settingApproval.jsp" method="post">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 전자결재 환경설정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="javascript:chkPwd();"><IMG src='../images/bt_save.gif' border='0' align=absmiddle></a>
				<IMG src='../../admin/images/bt_cancel.gif' border='0' align='absmiddle'  style='cursor:hand'  onClick="javascript:history.back();">
			  </TD></TR></TBODY></TABLE></TD></TR>
			  <TR><TD height='1' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!--내용-->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
	   <!--
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
		 <tr>
           <td width="100%" height="25" colspan='2' ><br>
				&nbsp;&nbsp;&nbsp;[비밀번호 묻기]<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - 검토,협조,승인시 비밀번호 확인여부를 셋팅합니다.<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - 전자결재시 비밀번호를 별도로 관리합니다.<br>
				&nbsp;&nbsp;&nbsp;[삭제문서 보기]<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - 등록된 관리자에 한하여 삭제된 결재문서를 볼 수 있습니다.<p></td></tr>
        <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>-->
		<!-- <tr>
			<td width="100%" height="25" colspan='4'><IMG src='../images/gw_yang_app.gif' align='absmiddle'></td></tr>
			<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>-->
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">비밀번호 옵션</td>
           <td width="80%" height="25" class="bg_04">
				  승&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;인
					 <input type='radio' name='apl' <%if(pwdapl.equals("0")) out.println("checked");%> value='0'>非암호
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='apl' <%if(pwdapl.equals("1")) out.println("checked");%> value='1'>암호
				  <br>검&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;토
					 <input type='radio' name='apv' <%if(pwdapv.equals("0")) out.println("checked");%> value='0'>非암호
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='apv' <%if(pwdapv.equals("1")) out.println("checked");%> value='1'>암호
				  <br>협&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;조
					 <input type='radio' name='apg' <%if(pwdapg.equals("0")) out.println("checked");%> value='0'>非암호
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio' name='apg' <%if(pwdapg.equals("1")) out.println("checked");%> value='1'>암호
				  <br>비밀번호
		 &nbsp;<input type='radio' name='mgr' <%if(pwdmgr.equals("0")) out.println("checked");%> value='0'>로그비밀사용
		 &nbsp;&nbsp;<input type='radio' name='mgr' <%if(pwdmgr.equals("1")) out.println("checked");%> value='1'>결재비밀사용</td>
		  </tr>
		  <!--<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
		  <tr>
			<td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">관리자 ID(사번)</B></td>
			<td width="80%" height="25" class="bg_04">
				<input TYPE=text NAME='admin' SIZE=10 MAXLENGTH=10 value='<%=appadmin%>'><a href="javascript:chkPwd();"> <img src='../images/bt_save.gif' border='0' align=absmiddle></a><br>보존기간 만료된 문서를 볼 수 있는 관리자 사번을 입력합니다.</td></tr> -->
		<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
		<tr>
			<td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">기결문서출력일수</B></td>
			<td width="80%" height="25" class="bg_04">
				<input TYPE=text NAME='appaps' SIZE=10 MAXLENGTH=10 value='<%=appaps%>'> &nbsp;기결문서를 볼 수 있는 일수를 입력합니다.<font color='#FF0000'> 예) </font>30일전 이면 -30</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
		 </tbody></table>
<input type='hidden' name='req'>

</td></tr></table>
</form>
</body>
</html>

<Script language = "Javascript">
 <!-- 
//비밀번호 확인여부
function chkPwd()
{
	document.sForm.action="settingApproval.jsp";
	document.sForm.req.value='SAVE';
	document.sForm.submit();
}
-->
</Script>