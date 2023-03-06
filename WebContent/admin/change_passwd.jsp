<%@ include file= "configHead.jsp"%>
<%@ page
	language = "java"
	info = "비빌번호 변경"		
	contentType = "text/html; charset=euc-kr"
%>
<HTML><HEAD><TITLE>비밀번호 변경</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/change_pwd.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--정보-->
	<form name="f1" method="post" action="change_passwdp.jsp" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=10 colspan="4"></td></tr>

         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">사번</td>
           <td width="70%" height="25" colspan="3" class="bg_02"><input type=text name=id size=10 maxlength=12 value="" onBlur="document.f1.id.value = document.f1.id.value.toUpperCase();"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">현재비밀번호</td>
           <td width="70%" height="25" colspan="3" class="bg_02"><input type=password name=old_passwd size=8 maxlength=12 style="width:80"></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">새비밀번호</td>
           <td width="70%" height="25" colspan="3" class="bg_02"><input type=password name=passwd size=8 maxlength=12 style="width:80" onKeyUp="javascript:sc_check(passwd);">&nbsp;4~12자 이내의 영문이나 숫자</td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">비밀번호확인</td>
           <td width="70%" height="25" colspan="3" class="bg_02"><input type=password name=repasswd size=8 maxlength=12 value="" style="width:80"></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table></form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:checkForm();'><img src='images/bt_modify.gif' border='0'></a>&nbsp;<a href='javascript:self.close();'><img src='images/bt_close.gif' border='0'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>
<script language=javascript>

function checkForm()
{
	var f = document.f1;
	if(f.id.value==""){
		alert("사번(ID)을 입력하세요.");
		f.id.focus();
		return;
	}
		

	if(f.old_passwd.value==""){
		alert("이전 비밀번호를 입력하세요.");
		f.old_passwd.focus();
		return;
	}

	if(f.passwd.value==""){
		alert("새 비밀번호를 입력하세요.");
		f.passwd.focus();
		return;
	}

	if(f.passwd.value.length < 4){
		alert("비밀번호는 4자 이상 입력하셔야 합니다.");
		f.passwd.focus();
		return;
	}
		
	if(f.passwd.value != f.repasswd.value){
		alert("비밀번호와 비밀번호확인의 값이 서로 같지 않습니다.(대소문자 구분함)");
		f.repasswd.focus();
		return;
	}
	f.submit();
}

// 암호값에 특수문자 제한 (추가시에서 \와 \사이에 제한할 특수문자를 입력)
var mikExp = /[$\\@\\\#%\^\&\*\(\)\[\]\+\_\{\}\`\~\=\|\.\!\-\:\;\'\"\?\<\>\ \/]/; 

function sc_check(val) 
{
	var key = event.keyCode;        
	var strPass = val.value;
	var strLength = strPass.length;
	var lchar = val.value.charAt((strLength) - 1);
	if(lchar.search(mikExp) != -1) {
		alert("암호값에 공백과 특수문자는 사용할 수 없습니다.");
		document.f1.passwd.value = "";         
		document.f1.passwd.focus();         
		return false;
	}
}
</script>