<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = "공정코드정보등록"
	language = "java"
	errorPage="../../admin/errorpage.jsp"
	contentType = "text/html;charset=euc-kr"
	import = "java.sql.*, java.io.*, java.util.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
    bean.openConnection();	

	String query		= "";
	String caption		= "";
	String j			= request.getParameter("j");		// 모드	
	String flag_name	= request.getParameter("flag_name");
	String flag			= request.getParameter("flag");		// 분류구분 flag
		
	String pid			= request.getParameter("pid");		// mbom_env Table 관리코드
	String m_code	= "";	// 분류에 따른 관리코드
	String spec		= "";	// DESCRIPTION
	String tag		= "";	// 약어

	if( j.equals("u")){ // 수정모드
		query = "SELECT * FROM mbom_env WHERE pid = '" + pid +"'";
		bean.executeQuery(query);

		while(bean.next()){
			pid		= bean.getData("pid");
			flag	= bean.getData("flag");
			m_code	= bean.getData("m_code");
			spec	= bean.getData("spec");
			tag		= bean.getData("tag");
		}
		caption = "수정";
	}else if(j.equals("a")){ // 현재분류의 하위분류 입력모드
		caption = "등록";
	}

%>

<HTML><HEAD><TITLE>공정 TEMPLATE코드관리 <%=caption%></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false" >
<FORM name="frm1" method="post" action="BmBase_process.jsp" onSubmit="return checkForm()">
<INPUT type=hidden name=j value='<%=j%>'>
<INPUT type=hidden name=flag value='<%=flag%>'>
<INPUT type=hidden name=pid value='<%=pid%>'>


<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
			<TR>
				<TD height="33" valign="middle" bgcolor="#73AEEF">
<%	if( j.equals("a")){ // 등록 %>
			<img src="../images/pop_add_temp.gif" hspace="10" alt="TEMPLATE코드정보 등록">
<%	} else if( j.equals("u")){ // 수정 %>				
			<img src="../images/pop_modify_temp.gif" hspace="10" alt="TEMPLATE코드정보 수정">
<%	}		%></TD></TR>
	        <TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	<TR height=131 valign='middle' align='center'><TD>
		<TABLE cellspacing=0 cellpadding=2 width="94%" border=0>
		<TBODY>
			<TR><TD height=20 colspan="2"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">분류구분</TD>
				<TD width="70%" height="25" class="bg_04"><%=flag_name%></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">관리코드</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='m_code' value='<%=m_code%>' size='5' maxlength=5 class='text_01'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">공정Description</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='spec' value='<%=spec%>' size='40' maxlength='40' class='text_01'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">공정코드순서</TD>
				<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='tag' value='<%=tag%>' size='40' maxlength='40' class='text_01'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
		 </TBODY></TABLE><br></TD></TR>
	<!--꼬릿말-->
		<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
			<TBODY>
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"> 
					<IMG src='../images/bt_save.gif' onclick='javascript:checkForm()' align='absmiddle' style='cursor:hand'>
					<IMG src='../images/bt_cancel.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD></TR>
				<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD>
				</TR>
        </TBODY></TABLE></form></TD></TR></table></BODY></HTML>


<script language=javascript>

function checkForm()
{
	var f = document.frm1;
	var tag_val = f.tag.value;
	var tag_inx = f.tag.value.length;

	
	if(f.m_code.value == ""){
			alert("TEMPLATE코드 입력하세요.");
			f.m_code.focus();
			return;
	}
	if(f.spec.value == ""){
			alert("Description을 입력하세요.");
			f.spec.focus();
			return;
	}
	if(f.tag.value == ""){
			alert("약어를 입력하세요.");
			f.tag.focus();
			return;
	}

	if(f.tag.value.charAt(tag_inx-1)!=','){
		alert("공정코드순서에서 맨 끝 문자는 콤마(,)이어야만 됩니다.");
		f.tag.focus();
		return;
	}			

	if(!isChecking(document.frm1.tag))	{
		alert("공정코드순서형식이 입력규칙에 맞지 않습니다.");
		f.tag.focus();
		return;
	}

	document.onmousedown = dbclick;

	f.submit();
	}	

	// 전송 버튼 클릭 후, 전송중에 또 버튼 누르지 못하도록 처리
	function dbclick(){
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
	// 공정코드순서 입력형식 체크(숫자,영문,콤마 만 가능!)
	function isChecking(input) {
		var num = "0123456789";
		var comma = ",";
		var uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		var lowercase = "abcdefghijklmnopqrstuvwxyz"; 
		var str = num + comma + uppercase + lowercase;
		return containsCharsOnly(input,str);
	}

	function containsCharsOnly(input,chars) {
		for (var inx = 0; inx < input.value.length; inx++) {
		   if (chars.indexOf(input.value.charAt(inx)) == -1)
			   return false;
		}
		return true;
	}

</script>