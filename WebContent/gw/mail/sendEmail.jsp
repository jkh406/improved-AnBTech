<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "전자메일 작성"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="javax.mail.*"
	import="javax.mail.internet.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean"	/>

<%
	String Message = "M";
	String id	= "";		//접속자 id
	String[] pid;			//관리번호
	String[] smtp;			//smtp명
	String[] name;			//보내는사람 이름
	String[] address;		//보내는사람 주소

	int rid = 0;			//리턴받은 숫자값(stmp선택시)
	String msg = "S";		//전송 메시지 결과

	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	id = sl.id; 		//접속자 login id

	//변수 초기화
	Message = "M"; msg = "S";

	/*********************************************************************
	 	보내는 메일서버명 읽어오기
	*********************************************************************/
	String[] idColumn = {"pid","id","name","address","sserver"};
	bean.setTable("emailInfo");			
	bean.setColumns(idColumn);
	bean.setOrder("pid ASC");	
	bean.setSearch("id",id);			
	bean.init_unique();
	
	int cnt = bean.getTotalCount();
	if(cnt == 0) cnt = 1;

	pid		= new String[cnt];						//관리번호
	smtp	= new  String[cnt];						//smtp명
	name	= new String[cnt];						//보내는사람 이름
	address = new String[cnt];						//보내는사람 주소

	if(bean.isEmpty()) {
		pid[0] = "";
		smtp[0] = "";
		name[0] = "";
		address[0] = "";
		Message = "NODATA";
	} else {
		int i = 0;
		while(bean.isAll()) {
			pid[i]		= bean.getData("pid");				
			smtp[i]		= bean.getData("sserver");		
			name[i]		= bean.getData("name");	
			address[i]	= bean.getData("address");
			i++;
		} //while
	}

%>

<HTML><HEAD><TITLE>새편지작성</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_n.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--버튼-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right">
 		  <a href="javascript:emailSend()"><img border="0" src="../images/bt_export.gif" align="absmiddle"></a>
          <a href="javascript:self.close()"><img border="0" src="../images/bt_close.gif" align="absmiddle"></a></td></tr></tbody></table>

    <!--발송정보-->
	<form name="mailForm" method="post" encType="multipart/form-data" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">보내는서버</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=smtp[0]%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif"><a href="Javascript:postReceiver();">작성자</a></td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=name[rid]%> (<%=address[rid]%>)
				<input type="hidden" name="strFrom" value="<%=address[rid]%>">
				<input type="hidden" name="strName" value="<%=name[rid]%>"></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">받는사람</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><textarea name="strTo" rows="1" cols="70"></textarea><br>*여러명일 경우 ;로 구분</td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">제목</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><input type="text" name="strSubject" size="76"></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">내용</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="strContent" rows=22 cols=75></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
					<img src="../images/b-attach.gif" border="0">첨부파일: <input type="file" name="file" size="47"><BR>
					<img src="../images/b-attach.gif" border="0">첨부파일: <input type="file" name="file2" size="47"><BR>
					<img src="../images/b-attach.gif" border="0">첨부파일: <input type="file" name="file3" size="47"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>
  <input type='hidden' name='SEND' >
  </form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
</BODY>
</HTML>

<script>
<!--
//전자메일 전송 처리결과 
if("<%=Message%>" == "SEND"){ alert("<%=msg%>"); self.close(); }
//계정이 없으면 메시지 전달하기
if("<%=Message%>" == "NODATA"){ alert("환경설정을 등록후 진행하십시요."); self.close(); }

//메일 보내기
function emailSend()
{
	if(mailForm.strTo.value == ""){	alert("받는사람을 입력하십시요.");	return;	}
	if(mailForm.strFrom.value == ""){	alert("보내는사람을 입력하십시요.");	return;	}
	if(mailForm.strSubject.value == ""){	alert("제목을 입력하십시요.");return;	}
	if(mailForm.strContent.value == ""){	alert("편지내용을 입력하십시요.");return;	}

	document.mailForm.action="sendEmailSave.jsp";
	document.mailForm.submit();	
	
}

//선택된 smtp로 바로가기
function toSmtp(form) 
{
    var myindex=form.smtpUrl.selectedIndex;
    if (form.smtpUrl.options[myindex].value != null) {
         window.location=form.smtpUrl.options[myindex].value;
    }
}

-->
</script>
