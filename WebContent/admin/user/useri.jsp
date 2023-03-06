<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java" 
	import		= "java.sql.*"
	contentType	= "text/HTML;charset=KSC5601"
	errorPage	= "../errorpage.jsp"
%>
<jsp:useBean id="recursion"  class="com.anbtech.admin.db.makeClassTree"/>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />


<%
/* 사용자 추가 */

    bean.openConnection();	

	String query = "";
	String caption = "";
	String j		=	request.getParameter("j")==null?"a":request.getParameter("j");		// 모드
	String ac_id	=	request.getParameter("ac_id");	// 사원이 포함된 조직의 ID
	String au_id	=	request.getParameter("au_id");	// 사원 ID
	String rank		=	"";
	String name		=	"";
	String id		=	"";
	String passwd	=	"";
	String email	=	"";
	String office_tel	=	"";
	String hand_tel	=	"";
	String fax		=	"";
	String main_job	=	"";
	String address	=	"";
	String post_no	=	"";
	String home_tel	=	"";
	String enter_day	=	"";
	String regi_date	=	"";
	String access_code  = "";
	String ac_code		= "";
	String code			= "";

	if( j.equals("u")){ // 사용자 정보 수정
		query = "select * from user_table where au_id = '"+au_id+"'";
		bean.executeQuery(query);
		
		while(bean.next()){
			ac_id		= bean.getData("ac_id");
			rank		= bean.getData("rank");
			name		= bean.getData("name");
			id			= bean.getData("id");
			passwd		= bean.getData("passwd");
			email		= bean.getData("email");
			office_tel	= bean.getData("office_tel");
			hand_tel	= bean.getData("hand_tel");
			fax			= bean.getData("fax");
			main_job	= bean.getData("main_job");
			address		= bean.getData("address");
			post_no		= bean.getData("post_no");
			home_tel	= bean.getData("home_tel");
			enter_day	= bean.getData("enter_day");
			regi_date	= bean.getData("regi_date");
			access_code = bean.getData("access_code");

		}
		caption		 = "수정";
	}else if(j.equals("a")){ //사용자 추가
		caption = "등록";
	}

%>
<HTML>
<head><title>사용자등록</title></head>
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
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 사용자등록</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<a href="javascript:checkForm()"><IMG src='../images/bt_save.gif' align='absmiddle' border='0' style='cursor:hand'></a>
				<IMG src='../images/bt_cancel.gif' onclick='history.back()' align='absmiddle' border='0' style='cursor:hand'>
				</TD></TR></TBODY>
		</TABLE></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TBODY></TABLE>

<form name="frm1" method="get" action="userp.jsp" style="margin:0">
<!--사용자 정보-->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR>
	<TD height=22 colspan="4"><img src="../images/user_info.gif" width="209" height="25" border="0"></TD></TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">소속</TD>
    <TD width="85%" height="25" class="bg_04" colspan='3'>
		<select name='ac_id' >
			<%=recursion.viewCombo(0,0)%>
		</select></TD>
<%		if(ac_id !=null && Integer.parseInt(ac_id) > 0){	%>
			<script language='javascript'>
				document.frm1.ac_id.value = '<%=ac_id%>';
			</script>
<%		}	%>	
	</TD>
   
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">성명</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="name" size="10" value="<%=name%>" maxlength="25" class="text_01"></TD>
     <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">직위</TD>
    <TD width="35%" height="25" class="bg_04" >
			<select name="rank" >
				<option value=''>선택</option>
<%
			query = "select ar_name,ar_code from rank_table order by ar_priorty asc";
			bean.executeQuery(query);
			while(bean.next()){
%>
				<option value='<%=bean.getData("ar_code")%>'><%=bean.getData("ar_name")%></option>
<%		}	%>
		
				</select>
<%		if(rank !=null){	%>
			<script language='javascript'>
				document.frm1.rank.value='<%=rank%>';
			</script>
<%		}	%>	
	</TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">사번</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="id" size="10" value="<%=id%>" maxlength="10" class="text_01"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">비밀번호</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="password" name="passwd" size="15" value="<%=passwd%>" maxlength="15" class="text_01"> * 대소분자 구분</TD>    
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">주요업무</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="main_job" size="25" value="<%=main_job%>" maxlength="25"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">전자우편</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="email" size="30" value="<%=email%>" maxlength="30" class="text_01"></TD>    
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">근무처 전화번호</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="office_tel" size="15" value="<%=office_tel%>" maxlength="15" class="text_01"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">근무처 팩스번호</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="fax" size="15" value="<%=fax%>" maxlength="15"></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
	<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">휴대전화번호</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="hand_tel" size="15" value="<%=hand_tel%>" maxlength="15"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">자택전화번호</TD>
    <TD width="35%" height="25" class="bg_04"><input type="text" name="home_tel" size="15" value="<%=home_tel%>" maxlength="15"></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">자택 주소</TD>
    <TD width="85%" height="25" class="bg_04" colspan=3><input type="text" name="address" size="74" value="<%=address%>" maxlength="50"></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">우편번호</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="post_no" size="10" value="<%=post_no%>" maxlength="7"> * 467-850 식으로 입력</TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">입사일</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="enter_day" size="10" value="<%=enter_day%>" maxlength="8" class="text_01"> * 반드시 20030327 식으로 입력</TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TABLE>
<input type="hidden" name="j" value="<%=j%>">
<input type="hidden" name="au_id" value="<%=au_id%>">
<input type="hidden" name="authorityString" value="NNCCA">
<input type="hidden" name="passwd_change" value="n">
<input type="hidden" name="current_passwd" value="<%=passwd%>">
</BODY>
</HTML>


<script>
<!--
 
function checkForm()
{
	var f = document.frm1;

	if(f.ac_id.value == ""){
			alert("소속을 선택하십시오.");
			f.ac_id.focus();
			return;
	}
	if(f.rank.value == ""){
			alert("직급을 선택하십시오.");
			f.rank.focus();
			return;
	}
	if(f.name.value == ""){
			alert("이름을 입력하십시오.");
			f.name.focus();
			return;
	}
	if(f.id.value == ""){
			alert("사번을 입력하십시오.");
			f.id.focus();
			return;
	}
	if(f.passwd.value == ""){
			alert("비밀번호를 입력하십시오.");
			f.passwd.focus();
			return;
	}
	if(f.office_tel.value == ""){
			alert("근무처 전화번호를 입력하십시오.");
			f.office_tel.focus();
			return;
	}
	if(f.enter_day.value == ""){
			alert("입사일을 입력하십시오.");
			f.enter_day.focus();
			return;
	}
	if(f.enter_day.value.length != 8){
			alert("날짜를 잘못 입력하셨습니다.");
			f.enter_day.focus();
			return;
	}
	if(!isNumber(f.enter_day)){
			alert("날짜에 숫만만 입력되야 합니다.");
			f.enter_day.focus();
			return;
	}

	if(f.current_passwd.value != f.passwd.value){
		f.passwd_change.value = "y";
	}

	f.submit();
}

// 입력값이 특정 문자(chars)만으로 되어있는지 체크
function containsCharsOnly(input,chars) {
	for (var inx = 0; inx < input.value.length; inx++) {
	   if (chars.indexOf(input.value.charAt(inx)) == -1)
		   return false;
	}
	return true;
}
		
// 입력값에 숫자만 있는지 체크
function isNumber(input) {
	var chars = "0123456789";
	return containsCharsOnly(input,chars);
}
-->
</script>