<%@ include file= "../../checkAdmin.jsp"%>
<%@ include file= "../../configHead.jsp"%>
<%@ page
	language	= "java" 
	import		= "java.sql.*" 
	contentType	= "text/HTML;charset=KSC5601"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	bean.openConnection();	

	com.anbtech.text.Hanguel hanguel = new com.anbtech.text.Hanguel();

	String query		= "";
	String caption		= "";
	String mid			= request.getParameter("mid")==null?"":request.getParameter("mid");
	String mode			= request.getParameter("mode");		// 모드
	String type			= request.getParameter("type")==null?"":hanguel.toHanguel(request.getParameter("type"));
	String type_name	=request.getParameter("type_name")==null?"":hanguel.toHanguel(request.getParameter("type_name"));
	String code			=	"";
	String code_name	=	"";
	String readonly		=	"";
	String rd			=	"";			//문서구분시 minor name은고정 minor code는 변동처리
	

	if(type_name==null || type_name.equals("전체"))	type_name ="";

	if( mode.equals("modify")){ // 사용자 정보 수정
		query = "SELECT * FROM system_minor_code WHERE mid = '"+mid+"'";
		bean.executeQuery(query);
		
		while(bean.next()){
			mid			= bean.getData("mid");
			type		= bean.getData("type");
			type_name	= bean.getData("type_name");
			code		= bean.getData("code");
			code_name	= bean.getData("code_name");
		}
		caption		= "수정";
		
	}else if(mode.equals("write")){ //사용자 추가
		caption		= "등록";
	}

	if( mode.equals("modify") || !type.equals("")) readonly	= "readonly";

	//문서구분시 minor name은고정 minor code는 변동처리
	if(type.equals("DOCUMENT") && mode.equals("modify")) rd = "readonly";
	
%>
<HTML>
<head><title>Minor 코드 등록</title></head>
<link rel="stylesheet" type="text/css" href="../../css/style.css">
<BODY>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title">&nbsp;<img src="../../images/blet.gif" align="absmiddle"> Minor 코드 등록</TD>
					<TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR><TD width=4>&nbsp;</TD>
				<TD align=left width='500'>
					<a href="javascript:checkForm()"><IMG src='../../images/bt_save.gif' align='absmiddle' border='0' style='cursor:hand'></a>
					<a href="javascript:go_list('<%=type%>')"><IMG src='../../images/bt_cancel.gif' align='absmiddle' border='0'></a>
				</TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TBODY></TABLE>

<form name="frm1" method="get" action="minor_process.jsp" style="margin:0">
<!--사용자 정보-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TR><TD width="15%" height="25" class="bg_03" background="../../images/bg-01.gif">구분코드</TD>
			<TD width="35%" height="25" class="bg_04" >
				<input type="text" name="type" size="16" value="<%=type%>" <%=readonly%>></TD>
			<TD width="15%" height="25" class="bg_03" background="../../images/bg-01.gif">구분명</TD>
			<TD width="35%" height="25" class="bg_04" >
				<input type="text" name="type_name" size="16" value="<%=type_name%>" <%=readonly%>></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../../images/bg-01.gif">Minor코드</TD>
			<TD width="35%" height="25" class="bg_04" ><input type="text" name="code" size="16" value="<%=code%>"></TD>
			<TD width="15%" height="25" class="bg_03" background="../../images/bg-01.gif">Minor코드명</TD>
			<TD width="35%" height="25" class="bg_04" ><input type="text" name="code_name" size="16" value="<%=code_name%>" <%=rd%>></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>   
</TABLE>

<input type="hidden" name="mode" value="<%=mode%>">
<input type="hidden" name="mid" value="<%=mid%>">
</FORM>
</BODY>
</HTML>


<script>
<!--
function checkForm()
{
	var f = document.frm1;
	
	if( f.type.value==""){
		alert("구분코드를 입력하십시오.");
		f.type.focus();
		return;
	}

	if( f.type_name.value==""){
		alert("구분명을 입력하십시오.");
		f.type_name.focus();
		return;
	}

	if(f.code.value==""){
		alert("Minor코드를 입력하십시오.");
		f.code.focus();
		return;
	}

	if(f.code_name.value==""){
		alert("Minor코드명을 입력하십시오.");
		f.code_name.focus();
		return;
	}
	f.submit();
}

function go_list(type)
{
	location.href = "minor_list.jsp?type=" + type;
}
-->
</script>