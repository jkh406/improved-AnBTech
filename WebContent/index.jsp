<%@ page language="java" contentType="text/html; charset=UTF-8"
	import = "com.anbtech.admin.SessionLib"
    pageEncoding="UTF-8"%>
<%
SessionLib sl;
sl = (SessionLib)session.getAttribute(session.getId());

if(sl != null){
%>
	<script>
		top.location.href = "gw.htm";
	</script>
<%
	return;
}
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">


#wrap{width:auto;}
#left{float:left; width:907px; height:763px; background:url(./images/main/main_left.jpg); margin:0 auto;}	
#right{float:left; width:339px; height:100%; margin:0 auto;}	
#right_top{width:399px; height:553px; background:url(./images/main/main_right1.jpg);}	
#right_id{width:399px; height:24px; background:url(./images/main/main_right2.jpg); padding:0 0 0 32px;}	
#right_pw{width:367px; height:24px; background:url(./images/main/main_right3.jpg);padding:6px 0 9px 32px;}	
#right_submit{width:399px; height:147px; background:url(./images/main/main_right4.jpg);}	
	
</style>
</head>

<body>
<div id="wrap">
	<div id="left"></div>
	<div id="right">
	<form name="loginForm" method="post" action="admin/login.jsp" style="margin:0" onsubmit="return confirm_submit();">
		<div id="right_top"></div>
		<div id="right_id">		
			<input type="text" name="id" id="id" maxlength="50" value="" tabindex="1" title="아이디 입력"/>
		</div>
		<div id="right_pw">
			<input type="password" name="passwd" id="passwd" maxlength="32" value="" title="비밀번호입력" tabindex="2"/>
		</div>
		<div id="right_submit">
			<input type="image" src="images/main/main_btn.jpg" name="loginBtn" id="loginBtn" tabindex="3" title="로그인"/>
		</div>
		
	</form>
	</div>	
</div>
</body>
</html>

<Script Language="JavaScript">
function $() {
	var ret = [];
	for ( var i = 0; i < arguments.length; i++) {
		if (typeof arguments[i] == 'string') {
			ret[ret.length] = document.getElementById(arguments[i]);
		} else {
			ret[ret.length] = arguments[i];
		}
	}
	return ret[1] ? ret : ret[0];
}

function confirm_submit() {
	if ($('id').value == "") {
		alert('아이디를 입력하십시오.');
		$('id').focus();
		return false;
	} else if ($('passwd').value == "") {
		alert('패스워드를 입력하십시오.');
		$('passwd').focus();
		return false;
	}	
}


function sc_check(val) 
{
	var key = event.keyCode;        
	var strPass = val.value;
	var strLength = strPass.length;
	var lchar = val.value.charAt((strLength) - 1);
	if(lchar.search(mikExp) != -1) {
		alert("암호값에 공백과 특수문자는 사용할 수 없습니다.");
		document.fname.passwd.value = "";         
		document.fname.passwd.focus();         
		return false;
	}
}
</script>
