<%@ include file= "configPopUp.jsp"%>
<%@ page		
	info= "그룹웨어 사용자 검색"		
	contentType = "text/html; charset=euc-kr" 		
%>

<%
	String to = request.getParameter("to");
	String type = request.getParameter("type");
%>
<HTML><HEAD><TITLE>사우정보</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/pop_user_info.gif"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=0 width="94%" border=0>
	   <tbody>
         <tr>
		   <td height=22 colspan="2">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="36%" style="padding-left:4px" background="images/bg-011.gif"><iframe name="tree" src="UserTreeMainForUserInfo.jsp" width="100%" height="350" border="0" frameborder="0" scrolling=no>브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe></td>
           <td width="64%"><iframe name="list" src="UserInfoView.jsp" width="100%" height="350" border="0" frameborder="0">브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</iframe></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

<form name="tmpForm" style="margin:0">
	<input type='hidden' name='to' value='<%=to%>'>
	<input type='hidden' name='type' value='<%=type%>'>
</form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="images/bt_close.gif" border="0" align="absmiddle"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

