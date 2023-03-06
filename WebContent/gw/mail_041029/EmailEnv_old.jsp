<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "전자메일계정"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"
	import="com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%

	//메시지 전달변수
	String Message="";			//메시지 전달 변수  
	String name = "";			//접속자 이름
	String email = "";			//접속자 email

	//입력변수
	String pid = "";			//관리번호
	String sno = "";			//선택되어 넘어온 서버선택번호
	String rtype = "";			//받은메일서버 유형(POP3,IMAP)
	String stype = "";			//보내는메일서버 유형(SMTP)
	String rserver = "";		//받은메일서버명
	String sserver = "";		//보내는메일서버명 
	String username = "";		//사용자이름
	String useraddress = "";	//사용자주소 
	String userid = "";			//로그온 ID
	String userpassword = "";	//로그온 
	String readtype = "";		//가져온 메일 삭제여부(RO, RW)

	//수정여부 변수
	String ronly="";			//보내는메일서버명 편집여부

	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자

	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	String[] idColumn = {"id","name","email"};
	bean.setTable("user_table");			
	bean.setColumns(idColumn);
	bean.setOrder("id ASC");	
	bean.setSearch("id",login_id);			
	bean.init_unique();

	while(bean.isAll()) {
		name = bean.getData("name");				//접속자 명
		email = bean.getData("email");				//접속자 email주소
	} //while

	//Clear
	rtype=stype=rserver=sserver=username=useraddress=userid=userpassword=readtype=ronly="";

	/*********************************************************************
	 	정보 수정하기 (from EmailMain.jsp)
	*********************************************************************/
	String NUM = request.getParameter("SNO");					//선택된 번호
	String PID = request.getParameter("PID");					//PID
	if(PID != null) pid = PID;
	if(NUM != null) sno = NUM;

	String[] chkemailColumn = {"pid","id","name","address","rtype","stype","rserver","sserver","loginid","loginpwd","readtype"};
	bean.setTable("emailInfo");			
	bean.setColumns(chkemailColumn);

	//PID로 읽기
	if(pid.length() > 0) {
		bean.setOrder("pid ASC");	
		bean.setSearch("pid",PID);			
		bean.init_unique();

		while(bean.isAll()) {
			rtype = bean.getData("rtype");					//받은메일서버 유형(POP3,IMAP)
			stype =  bean.getData("stype");					//보내는메일서버 유형(SMTP)
			rserver =  bean.getData("rserver");				//받은메일서버명
			sserver =  bean.getData("sserver");				//보내는메일서버명 
			username =  bean.getData("name");				//사용자이름
			useraddress =  bean.getData("address");			//사용자주소 
			userid =  bean.getData("loginid");				//로그온 ID
			userpassword =  bean.getData("loginpwd");		//로그온 비밀번호
			readtype = bean.getData("readtype");			//받아온 메일서버 삭제여부
		} //while
		if(sno.equals("0")) ronly = "";						//최초입력된 경우만 수정가능
		else ronly = "readOnly";							//수정불가능
	} else {		//받는메일서버명 및 사용자 주소 이름은 하나만 허용함
		bean.setOrder("pid ASC");	
		bean.setSearch("id",login_id);			
		bean.init_unique();

		if(bean.isEmpty()) ronly="";						//최초 신규등록
		else {
			while(bean.isAll()) {
				sserver =  bean.getData("sserver");				//보내는메일서버명 
				username =  bean.getData("name");				//사용자이름
				useraddress =  bean.getData("address");			//사용자주소 
			} //while
			ronly="readOnly";	//편집불가
		} //if
	} //if

	/*********************************************************************
	 	정보 저장하기
	*********************************************************************/
	if(request.getParameter("rtype") != null){
		//입력/수정한 내용 읽기
		rtype = Hanguel.toHanguel(request.getParameter("rtype"));			//받은메일서버 유형(POP3,IMAP)
		stype = Hanguel.toHanguel(request.getParameter("stype"));				//보내는메일서버 유형(SMTP)
		rserver = Hanguel.toHanguel(request.getParameter("rserver"));			//받은메일서버명
		sserver = Hanguel.toHanguel(request.getParameter("sserver"));			//보내는메일서버명 
		username = Hanguel.toHanguel(request.getParameter("username"));			//사용자이름
		useraddress = Hanguel.toHanguel(request.getParameter("useraddress"));	//사용자주소 
		userid = Hanguel.toHanguel(request.getParameter("userid"));				//로그온 ID
		userpassword = Hanguel.toHanguel(request.getParameter("userpassword")); //로그온 비밀번호

		readtype = request.getParameter("readtype");							//메일서버 메일 삭제여부
		if(readtype == null) readtype = "RW";
		else readtype = "RO";
	
		//저장/수정 등록하기
		if(pid.length() == 0) {					//신규 등록
			String inputs = "INSERT INTO emailInfo ";
			inputs += "(pid,id,name,address,rtype,stype,rserver,sserver,loginid,loginpwd,readtype) values('";
			inputs += bean.getID() + "','" + login_id + "','" + username + "','" + useraddress + "','";
			inputs += rtype + "','" + stype + "','" + rserver + "','" + sserver + "','";
			inputs += userid + "','" + userpassword + "','" + readtype + "')";

			if((rserver.length() != 0) && (userid.length() != 0) && (userpassword.length() != 0)){
				//계정 중복 등록 검사 (받은메일서버명,사용자ID 중복검사)
				String[] emailColumn = {"id","rtype","rserver","loginid","loginpwd"};
				bean.setTable("emailInfo");			
				bean.setColumns(emailColumn);
				bean.setOrder("id ASC");	
				bean.setSearch("id",login_id,"rserver",rserver,"loginid",userid);			
				bean.init_unique();

				if(bean.isEmpty()) {
					try { bean.execute(inputs); } catch (Exception e) { out.println("error : " + e);}
				}
				//out.println("int : " + inputs + "<br>");	
			} //if
		} else {							//수정하기
			String updata = "UPDATE emailInfo set name='" + username + "',address='";
				updata += useraddress + "',rtype='" + rtype + "',stype='" + stype + "',rserver='";
				updata += rserver + "',sserver='" + sserver + "',loginid='" + userid + "',loginpwd='";
				updata += userpassword + "',readtype='" + readtype + "' where pid='" + pid + "'";
			try { bean.execute(updata); } catch (Exception e) { out.println("error : " + e);}
			//out.println("up : " + updata + "<br>");
		}//if 
	}//if

%>


<HTML><HEAD><TITLE>POP계정 등록/수정</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_i.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name="sForm" type="post" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">받는메일</td>
           <td width="80%" height="25" class="bg_02">
			   <select name="rtype">
			<% 
				if(rtype.equals("IMAP")) {
					out.println("<option>POP3");
					out.println("<option selected>IMAP");
				} else {
					out.println("<option selected>POP3");
					out.println("<option>IMAP");
				}
			%></select> <input name="rserver" type="text" value="<%=rserver%>" size="30">
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <% if(sno.equals("0")) { %>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">보내는메일</td>
           <td width="80%" height="25" class="bg_02">
				<select name="stype"><option selected>SMTP</select> 
				<input name="sserver" type="text" value="<%=sserver%>" size="30" <%=ronly%> >
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		  <% } else { %>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">보내는메일</td>
           <td width="80%" height="25" class="bg_02">
				<select name="stype"><option selected>SMTP</select> <input name="sserver" type="text" value="<%=sserver%>" size="30" <%=ronly%> >
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
			<input name="sserver" type="hidden" value="<%=sserver%>" size="30" <%=ronly%> >
		  <% } %>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">로그인ID</td>
           <td width="80%" height="25" class="bg_02"><input name="userid" type="text" value="<%=userid%>" size="30"><br>해당 서버에 접속하기 위한 로그인 ID를 입력하세요.</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">로그인PW</td>
           <td width="80%" height="25" class="bg_02"><input name="userpassword" type="password" value="<%=userpassword%>" size="20"><br>로그인 패스워드를 입력하세요.</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">배달선택</td>
           <td width="80%" height="25" class="bg_02"><input name="readtype" type="checkbox" <% if(readtype.equals("RO")) out.println("CHECKED"); %> value="RO" size="20" >서버에 메시지 복사본 저장<br>체크 시 메시지를 가져와도 서버에 원본을 유지합니다.</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">이름</td>
           <td width="80%" height="25" class="bg_02">
		   <%
			if(username.length() == 0) out.println("<input name='username' value='" + name + "' type='text' size='10' readOnly>");
			else out.println("<input name='username' value='" + username + "' type='text' size='10' readOnly>");
		   %><br>보낸이란에 출력될 이름를 입력하세요.
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">전자우편</td>
           <td width="80%" height="25" class="bg_02">
		   <%
			if(useraddress.length() == 0) out.println("<input name='useraddress' value='" + email + "'  type='text' size='30' readOnly>");
			else out.println("<input name='useraddress' value='" + useraddress + "'  type='text' size='30'>");
		   %><br>보낸이란에 출력될 전자우편 주소를 입력하세요.
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table></form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="Javascript:envTest()"><img border="0" src="../images/bt_test.gif" align="absmiddle"></a> <a href="javascript:envSave();"><img border="0" src="../images/bt_save.gif" align="absmiddle"></a> <a href='javascript:self.close()'><img border="0" src="../images/bt_cancel.gif" align="absmiddle"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script>
<!--

function centerWindow() 
{ 
        var sampleWidth = 500;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 520;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

//저장하기
function envSave()
{
	if(sForm.rserver.value == ""){	alert("받는메일서버명을 입력하십시요.");	return;	}
	if(sForm.userid.value == ""){	alert("사용자ID를 입력하십시요.");	return;	}
	if(sForm.userpassword.value == ""){	alert("사용자 비밀번호를 지정하십시요.");return;	}

	document.sForm.action="EmailEnv.jsp";
	document.sForm.submit();
    if(confirm("저장 하시겠습니까?"))	{ self.close(); } else { return; }
}

//계정 검사하기
function envTest()
{
	rt = sForm.rtype.options[sForm.rtype.selectedIndex].text;			//받은메일 프로토콜
	
	if(sForm.rserver.value == ""){	alert("받는메일서버명을 입력하십시요.");	return;	}
	else rs = sForm.rserver.value ;

	ss = sForm.sserver.value ;			//보내는메일 서버명

	if(sForm.useraddress.value == ""){	alert("메일주소를 입력하십시요.");	return;	}
	else ua = sForm.useraddress.value;

	if(sForm.userid.value == ""){	alert("사용자ID를 입력하십시요.");	return;	}
	else ui = sForm.userid.value;

	if(sForm.userpassword.value == ""){	alert("사용자 비밀번호를 지정하십시요.");return;	}
	else up = sForm.userpassword.value; 

	inmail = rt + ";" + rs + ";" + ui + ";" + up + ";" + ss + ";" + ua + ";" + <%=sno%> + ";";
	wopen('EnvTest.jsp?INF='+inmail,'post_write','400','200','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>