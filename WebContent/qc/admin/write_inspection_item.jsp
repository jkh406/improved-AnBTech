<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	import		= "java.sql.*,com.anbtech.text.Hanguel" 
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String mid			= request.getParameter("mid");
	String mode			= request.getParameter("mode");
	String class_code	= request.getParameter("class_code");
	String class_name	= request.getParameter("class_name");

	if(class_name == null) class_name = "";
	else class_name = Hanguel.toHanguel(class_name);

	String inspection_code			= "";
	String inspection_name			= "";
	String inspection_result_type	= "";

	String readonly		= "";
	String query		= "";
	
	bean.openConnection();
	
	//수정일 경우
	if( mode.equals("modify")){
		query = "SELECT * FROM qc_inspection_item WHERE mid = '" + mid + "'";
		bean.executeQuery(query);
		bean.next();
		mid				= bean.getData("mid");
		class_code		= bean.getData("inspection_class_code");
		class_name		= bean.getData("inspection_class_name");
		inspection_code	= bean.getData("inspection_code");
		inspection_name	= bean.getData("inspection_name");
		inspection_result_type	= bean.getData("inspection_result_type");
	}

	//분류항목 및 검사항목 동시 추가시
	if(mode.equals("write") && class_code.equals("")){
		class_name = "";
		query = "SELECT MAX(inspection_class_code) FROM qc_inspection_item";
		bean.executeQuery(query);
		bean.next();
		
		if(bean.getData(1) == null){
			class_code = "10";
			inspection_code = "10001";
		}else{
			class_code = Integer.toString(Integer.parseInt(bean.getData(1)) + 1);
			inspection_code = class_code + "001";
		}
	}
	//검사항목 추가시
	else if(mode.equals("write") && !class_code.equals("")){
		query = "SELECT MAX(inspection_code) FROM qc_inspection_item WHERE inspection_class_code = '" + class_code + "'";
		bean.executeQuery(query);
		bean.next();
		
		inspection_code = Integer.toString(Integer.parseInt(bean.getData(1)) + 1);
	}
%>
<HTML>
<head><title></title></head>
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
					<TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 검사항목등록</TD>
					<TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR><TD width=4>&nbsp;</TD>
				<TD align=left width='500'>
					<a href="javascript:checkForm()"><IMG src='../images/bt_save.gif' align='absmiddle' border='0' style='cursor:hand'></a>
					<a href="javascript:go_list('<%=class_code%>')"><IMG src='../images/bt_cancel.gif' align='absmiddle' border='0'></a>
				</TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TBODY></TABLE>

<form name="frm1" method="get" action="process_inspection_item.jsp" style="margin:0">
<!--사용자 정보-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TR><TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">분류코드</TD>
			<TD width="35%" height="25" class="bg_04" >
				<input type="text" name="class_code" size="16" value="<%=class_code%>" readOnly></TD>
			<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">분류명</TD>
			<TD width="35%" height="25" class="bg_04" >
				<input type="text" name="class_name" size="16" value="<%=class_name%>" <%=readonly%>></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">검사항목코드</TD>
			<TD width="35%" height="25" class="bg_04" ><input type="text" name="inspection_code" size="16" value="<%=inspection_code%>" readOnly></TD>
			<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">검사항목명</TD>
			<TD width="35%" height="25" class="bg_04" ><input type="text" name="inspection_name" size="16" value="<%=inspection_name%>"></TD></TR>
		<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">표시속성</TD>
			<TD width="35%" height="25" class="bg_04" >
				<select name='inspection_result_type'>
					<option value='정성'>정성</option>
					<option value='정량'>정량</option>
				</select>
				<%	if(!inspection_result_type.equals("")){	%>
						<script language='javascript'>
							document.frm1.inspection_result_type.value = '<%=inspection_result_type%>';
						</script>
				<%	}	%></td>
			<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif"></TD>
			<TD width="35%" height="25" class="bg_04" ></TD></TR>
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
	
	if( f.class_code.value==""){
		alert("분류코드를 입력하십시오.");
		f.class_code.focus();
		return;
	}

	if( f.class_name.value==""){
		alert("분류명을 입력하십시오.");
		f.class_name.focus();
		return;
	}

	if(f.inspection_code.value==""){
		alert("검사항목코드를 입력하십시오.");
		f.inspection_code.focus();
		return;
	}

	if(f.inspection_name.value==""){
		alert("검사항목명을 입력하십시오.");
		f.inspection_name.focus();
		return;
	}

	if(f.inspection_result_type.value==""){
		alert("표시속성을 선택하십시오.");
		f.inspection_result_type.focus();
		return;
	}

	f.submit();
}

function go_list(class_code)
{
	location.href = "list_inspection_item.jsp?class_code=" + class_code;
}
-->
</script>