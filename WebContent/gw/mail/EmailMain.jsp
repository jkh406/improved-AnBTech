<%@ include file="../../admin/configPopUp.jsp"%>
<%@ 	page		
	info= "전자메일계정 메인"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%

	//메시지 전달변수
	String Message="";			//메시지 전달 변수  
	String passwd="";			//접속자 비밀번호

	//전자메일계정 정보
	String[] PID;				//관리번호
	String[] Rserver;			//받는메일 서버명
	String[] loginID;			//받는메일 로그온계정

	/*********************************************************************
	 	전자메일계정 삭제하기
	*********************************************************************/
	String RECPID = request.getParameter("PID");
	if(RECPID != null) {
		String Del = "DELETE from emailInfo where pid='" + RECPID + "'";
		try { bean.execute(Del); } catch (Exception e) { out.println("error : " + e);}
	}

	/*********************************************************************
	 	접속자 전자메일계정 알아보기
	*********************************************************************/
	String[] emailColumn = {"pid","id","rserver","loginid"};
	bean.setTable("emailInfo");			
	bean.setColumns(emailColumn);
	bean.setOrder("pid ASC");	
	bean.setSearch("id",login_id);			
	bean.init_unique();

	int cnt = bean.getTotalCount();
	PID = new String[cnt];
	Rserver = new String[cnt];
	loginID = new String[cnt];

	int i = 0;
	while(bean.isAll()) {
		PID[i] = bean.getData("pid");				//관리번호
		Rserver[i] = bean.getData("rserver");		//받은메일 서버멍
		loginID[i] = bean.getData("loginid");		//로그온 사용자 ID
		i++;
	}
%>

<HTML><HEAD><TITLE>POP계정 관리</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_p.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">등록계정</td>
           <td width="80%" height="25" class="bg_02"><form name="aForm" style="margin:0">
			   <select name="mail" multiple size=10>
				<OPTGROUP label='---------------------------------'>
<%
				for(int si=0; si<PID.length; si++){
					out.println("<option value=" + PID[si] + ">" + Rserver[si] + "   /   " + loginID[si] + "</option>");
				}
%>
			</select></form>
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:EmailAdd();"><img border="0" src="../images/bt_add.gif" align="absmiddle"></a> <a href="javascript:EmailModify();"><img border="0" src="../images/bt_modify.gif" align="absmiddle"></a> <a href="javascript:EmailDelete();"><img border="0" src="../images/bt_del.gif" align="absmiddle"></a> <a href='javascript:self.close()'><img border="0" src="../images/bt_close.gif" align="absmiddle"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
</BODY>
</HTML>

<script language="JavaScript" type="text/javascript"><!--
function wopen(url, t, w, h,st) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//전자메일계정 수정하기 
function EmailModify()
{
	var data = "";							
	var num = document.aForm.mail.selectedIndex;
	if(num < 0){
		alert("변경할 계정을 선택해 주십시오.");
		return;
	}
	data = document.aForm.mail.options[num].value;	//변경할 PID관리번호
	wopen('EmailEnv.jsp?PID='+data+'&SNO='+num,'env_write','450','385','scrollbars=no,toolbar=no,status=no,resizable=no');
	close();
}

//전자메일계정 등록하기 
function EmailAdd()
{
	wopen('EmailEnv.jsp?PID=&SNO=0','env_write','500','390','scrollbars=no,toolbar=no,status=no,resizable=no');
	close();
}

//전자메일계정 삭제하기
function EmailDelete()
{
	var data = "";							
	var num = document.aForm.mail.selectedIndex;
	if(num < 0){
		alert("삭제할 계정을 선택해 주십시오.");
		return;
	}
	data += document.aForm.mail.options[num].value;	//변경할 PID관리번호
	location.href = "EmailMain.jsp?PID="+data;
}
//-->
</script>