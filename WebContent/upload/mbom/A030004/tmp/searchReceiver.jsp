<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" 
    contentType="text/html;charset=euc-kr" 
	errorPage="../admin/errorpage.jsp"
%>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>

<%
	String target = Hanguel.toHanguel(request.getParameter("target"));
%>

<html>

<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=euc-kr">
<META name="GENERATOR" content="Microsoft FrontPage 4.0">
<META name="ProgId" content="FrontPage.Editor.Document">
<TITLE>BOM 관리자 선택</TITLE>
</HEAD>
<BODY background="" marginwidth="0" topmargin="0" leftmargin="0"  oncontextmenu="return false">
	<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
		<TR><TD align="center">
			<!--타이틀-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
		        <TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/*.gif" width="181" height="17" alt='BOM 관리자 설정' hspace="10"></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
    <!--버튼
	<TABLE cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <TR><TD height=20 colspan="4"></TD></TR>
		 <TR><TD height=25 colspan="4" align="right"><a href="javascript:list.submitWinCall();;"><img src='../img/go_line_call.gif' border='0'></a> <a href="javascript:list.submitWin();;"><img src='../img/go_line_save.gif' border='0'></a> <a href="javascript:list.returnSelected();"><img src="../images/bt_sel_finish.gif" border="0"></a> <a href="javascript:self.close();"><img src="../images/close.gif" width="46" height="19" border="0"></a></TD></TR></TBODY></TABLE>-->

	<TABLE cellspacing=0 cellpadding=0 width="94%" border=0>
		<TBODY>
		<!--<TR bgcolor=#60A3EC><TD height="2" colspan="2"></TD></TR>-->
			<TR><TD height=22 colspan="2">&nbsp;</TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="60%" style="padding-left:5px" background="../bm/images/bg-011.gif">
				<IFRAME name="tree" src="../admin/UserTreeMainForMulti.jsp" width="100%" height="350" border="0" frameborder="0">
    브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe></TD>
				<TD width="40%" class="bg_02">
					<iframe name="list" src="ReceiverList.jsp?target=<%=target%>" width="100%" height="350" border="0" frameborder="0">
    브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe></TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="2"></TD></TR>
			<TR><TD height=20 colspan="2"></TD></TR></TBODY></TABLE>

	<!--꼬릿말-->
	<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR><TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px">
			 <A href="javascript:sel_mgr();"><img src="../bm/images/bt_sel_finish.gif" border="0" align="absmiddle"></a> <A href="javascript:self.close();"><img src="../bm/images/close.gif" border="0" align="absmiddle"></a></TD>
          </TR>
		  <TR><TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR></TBODY></TABLE></TD></TR></TABLE>
</BODY>
</HTML>

<script language='javascript'>
	// 관리자 선택
	function sel_mgr(){
		document.list.transferList();
	}

</script>