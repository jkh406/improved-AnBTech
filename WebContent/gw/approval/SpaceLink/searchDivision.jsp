<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>

<%
	String target = Hanguel.toHanguel(request.getParameter("target"));
%>

<HTML><HEAD><TITLE>공문수신부서 선택</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../../images/pop_doc_r.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=0 width="94%" border=0>
	   <tbody>
         <tr>
		   <td height=22 colspan="2">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="50%" style="padding-left:4px" background="../../images/bg-011.gif"><iframe name="tree" src="../../../admin/UserTreeMainForMulti.jsp" width="100%" height="350" border="0" frameborder="0" scrolling=no>브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe></td>
           <td width="50%" class="bg_02"><iframe name="list" src="DivisionList.jsp?target=<%=target%>" width="100%" height="350" border="0" frameborder="0">브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:list.transferList();"><img src="../../images/bt_sel_end.gif" border="0" align="absmiddle"></a> <a href="javascript:self.close();"><img src="../../images/close.gif" border="0" align="absmiddle"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>
