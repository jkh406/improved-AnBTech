<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%!
	StShiftInfoTable table;
%>

<%
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
	String mode = request.getParameter("mode");	// 모드
	table = (StShiftInfoTable)request.getAttribute("ITEM_SHIFT_INFO");
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../st/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../st/images/blet.gif"> 상세재고이동정보(품목간)</TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
					<IMG src='../st/images/bt_list.gif' onClick='javascript:go_shiftlist();' style='cursor:hand' align='absmiddle'  alt='목록보기'>
					</TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동번호</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getShiftNo()%></TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동일자</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getRegDate()%></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">등록자명</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getRequestorInfo()%></TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">등록자ID</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getRequestorId()%></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE><BR>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">대상공장코드</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getSrFactoryCode()%></TD>
		   <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">대상공장명</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getSrFactoryName()%></TD></TR>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR></TBODY></TABLE><BR>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동전품목코드</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getSrItemCode()%></TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동전품목명</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getSrItemName()%></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동전품목설명</TD>
           <TD width="85%" height="25" class="bg_04" colspan='3'><%=table.getSrItemDesc()%></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE><BR>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동후품목코드</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getDtItemCode()%></TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동후품목명</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getDtItemName()%></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동후품목설명</TD>
           <TD width="85%" height="25" class="bg_04" colspan='3'><%=table.getDtItemDesc()%></TD></TR>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR></TBODY></TABLE><BR>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동수량</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getQuantity()%></TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">단위</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getStockUnit()%></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE>

</BODY>
</HTML>


<script language=javascript>
function go_shiftlist(){
	history.go(-1);
}
</script>