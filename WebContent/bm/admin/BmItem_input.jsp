<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info = "설계변경항목관리"
	language = "java"
    errorPage = "../../admin/errorpage.jsp"
	contentType = "text/html;charset=euc-kr"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
    bean.openConnection();	
	com.anbtech.text.Hanguel hanguel = new com.anbtech.text.Hanguel();

	String query		= "";
	String caption		= "";
	String j			= request.getParameter("j");		// 모드	
	String flag_name	= hanguel.toHanguel(request.getParameter("flag_name"));
	String flag			= request.getParameter("flag");		// 분류구분 flag
		
	String pid			= request.getParameter("pid");		// mbom_env Table 관리코드
	String m_code		= "";								// 분류에 따른 관리코드
	String spec			= "";								// DESCRIPTION
	
	if( j.equals("u")){ // 수정모드
		
		query = "SELECT * FROM mbom_env WHERE pid = '" + pid +"'";
		bean.executeQuery(query);

		while(bean.next()){
			pid		= bean.getData("pid");
			flag	= bean.getData("flag");
			m_code	= bean.getData("m_code");
			spec	= bean.getData("spec");
		}
		caption = "수정";

	}else if(j.equals("a")){ // 현재분류의 하위분류 입력모드
		caption = "등록";
	}

%>
<HTML><HEAD><TITLE>설계변경항목정보 <%=caption%></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<FORM name="frm1" method="post" action="BmItem_process.jsp" onSubmit="return checkForm()">
<INPUT type=hidden name=j value='<%=j%>'>
<INPUT type=hidden name=flag value='<%=flag%>'>
<INPUT type=hidden name=pid value='<%=pid%>'>
<INPUT type=hidden name=m_code_val value=''>

<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
	<!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF">

<%	if( j.equals("a")){ // 등록 %>
			<img src="../images/pop_add_des.gif" hspace="10" alt="설계변경항목정보 등록">
<%	} else if( j.equals("u")){ // 수정 %>				
			<img src="../images/pop_modify_des.gif" hspace="10" alt="설계변경항목정보 수정">
<%	}		%>
			</TD></TR>
			<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	<TR height=110 valign='middle' align='center'><TD>
		<TABLE cellspacing=0 cellpadding=2 width="94%" border=0>
			<TBODY>
				<TR><TD height=20 colspan="2"></TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
				<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">분류구분</TD>
					<TD width="70%" height="25" class="bg_04"><%=flag_name%></TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
				<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">변경항목</TD>
					<TD width="70%" height="25" class="bg_04">
				
				<%if(j.equals("a")) {%>
				<SELECT name='m_code' >
					<OPTION value='F01'>변경이유</OPTION>
					<OPTION value='F02'>적용구분</OPTION>
					<OPTION value='F03'>적용범위</OPTION>
					<OPTION value='F04'>업무구분</OPTION>
					<OPTION value='F05'>테스트</OPTION>
				</SELECT>
				<%} else {%>
					<%=m_code%>					
				<%}%>
				</TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
				<TR><TD width="30%" height="25" class="bg_03" background="../images/bg-01.gif">Description</TD>
					<TD width="70%" height="25" class="bg_04"><INPUT type='text' name='spec' value='<%=spec%>' size='40' class='text_01'></TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
				</TBODY></TABLE><br></TD></TR>
	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
			<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"> 
					<IMG src='../images/bt_save.gif' onclick='javascript:checkForm();' align='absmiddle' style='cursor:hand'>
					<IMG src='../images/bt_cancel.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD></TR>
			<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
        </TBODY></TABLE>
</FORM></TD></TR></TABLE>
</BODY>
</HTML>


<script language=javascript>

function checkForm()
{
	var f = document.frm1;

	if(f.j.value == 'a') {
		for(i=0; i<f.m_code.length; i++){
			if(f.m_code.options[i].selected==true){
				f.m_code_val.value = f.m_code.options[i].text;
			}
		}
	}
	
	if(f.spec.value == ""){
			alert("Description을 입력하세요.");
			f.spec.focus();
			return;
	}	

	// 전송 버튼 클릭 후, 전송중에 또 버튼 누르지 못하도록 처리
	document.onmousedown=dbclick;

	f.submit();
}


// 전송 버튼 클릭 후, 전송중에 또 버튼 누르지 못하도록 처리
function dbclick(){
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
</script>