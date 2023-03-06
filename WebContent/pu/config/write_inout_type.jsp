<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%
	InOutTypeTable table = new InOutTypeTable();
	table = (InOutTypeTable)request.getAttribute("INOUT_TYPE_INFO");

	String mode = request.getParameter("mode");

	String mid			= table.getMid();
	String type			= table.getType();
	String name			= table.getName();
	String is_import	= table.getIsImport();
	String is_enter		= table.getIsEnter();
	String is_return	= table.getIsReturn();
	String is_sageup	= table.getIsSageup();
	String is_using		= table.getIsUsing();
	String stock_type	= table.getStockType();

	String caption ="입출고형태등록";
	if(mode.equals("modify_inout_type")) caption="입출고형태수정";
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../pu/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>

<BODY topmargin="0" leftmargin="0">

<FORM name="eForm" method="post" action="PurchaseConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pu/images/blet.gif"> <%=caption%></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
						<IMG src='../pu/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
						<IMG src='../pu/images/bt_cancel.gif' onClick='javascript:history.back();' style='cursor:hand' align='absmiddle'></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif"><%=caption%></TD>
            <TD width="37%" height="25" class="bg_04">
				<INPUT type='text' size='5' maxlength='5' name='type' value='<%=type%>' class="text_01"></TD>
            <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입출고형태명</TD>
            <TD width="37%" height="25" class="bg_04">
				<INPUT type='text' size='15' name='name' value='<%=name%>' class="text_01"></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">수입여부</TD>
           <TD width="37%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_import.equals("y")) out.print("checked"); %> name="is_import">예 
				<INPUT type="radio" value="n" <%if(is_import.equals("n")) out.print("checked"); %> name="is_import">아니오</TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고여부</TD>
           <TD width="37%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_enter.equals("y")) out.print("checked"); %> name="is_enter">예 <INPUT type="radio" value="n" <%if(is_enter.equals("n")) out.print("checked"); %> name="is_enter">아니오</TD>
		</TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">반품여부</TD>
           <TD width="37%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_return.equals("y")) out.print("checked"); %> name="is_return">예 
				<INPUT type="radio" value="n" <%if(is_return.equals("n")) out.print("checked"); %> name="is_return">아니오</TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">사급여부</TD>
           <TD width="37%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_sageup.equals("y")) out.print("checked"); %> name="is_sageup">예 
				<INPUT type="radio" value="n" <%if(is_sageup.equals("n")) out.print("checked"); %> name="is_sageup">아니오</TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">사용여부</TD>
           <TD width="87%" height="25" class="bg_04" colspan="3">
				<INPUT type="radio" value="y" <%if(is_using.equals("y")) out.print("checked"); %> name="is_using">예 <INPUT type="radio" value="n" <%if(is_using.equals("n")) out.print("checked"); %> name="is_using">아니오</TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	   </TBODY></TABLE>
<INPUT type='hidden' name='mode' value='<%=mode%>'>
<INPUT type='hidden' name='mid' value='<%=mid%>'>
<INPUT type='hidden' name='stock_type' value='<%=stock_type%>'>
</FORM>
</BODY>
</HTML>


<SCRIPT LANGUAGE=JAVASCRIPT>

function checkForm(){
	var f=document.eForm;

	if(f.type.value==""){
		alert("입출고형태코드를 입력하십시요.");
		f.type.focus();
		return;
	}
	if(f.name.value==""){
		alert("입출고형태명을 입력하십시요.");
		f.name.focus();
		return;
	}
/*
	if(f.stock_type.value==""){
		alert("재고처리 형태코드를 입력하십시요.");
		f.stock_type.focus();
		return;
	}
*/
	f.submit();
}
</script>