<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	/* 해당 분류에 속한 사원 목록 출력 */
	bean.openConnection();
	String sql = "select * from rank_table order by ar_priorty asc";
	bean.executeQuery(sql);
%>
<HTML>
<head><title>직급체계관리</title></head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<BODY leftmargin=0 topmargin=0 oncontextmenu='return false'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 직급체계관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<TD width="100%" align="left"><a href="ranki.jsp?j=a"><IMG src='../images/bt_add.gif' border='0' align='absmiddle'></a>
			  </TD></TR></TBODY>
		</TABLE></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>


<TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	  <TBODY>
	  <TR><TD width="100%" height="40" align="left" colspan=9>
			&nbsp;&nbsp;* 출력순위가 낮을수록 먼저 출력됩니다.<br>
			&nbsp;&nbsp;* 한번 입력된 직급 코드는 변경시 사용자의 직급에 영향을 미치니 유의하여 변경하십시요.
	  </TD></TR>
	  <TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
	  <TR vAlign=middle height=23>
		<TD noWrap width=150 align=middle class='list_title'>직급명</TD>
	    <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<TD noWrap width=80 align=middle class='list_title'>직급코드</TD>
		<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<TD noWrap width=80 align=middle class='list_title'>출력순위</TD>
	    <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<TD noWrap width=100 align=middle class='list_title'>비고</TD>
		<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
		<TD noWrap width=100% align=middle class='list_title'></TD>
	  </TR>
	  <TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%	while(bean.next()){	%>
	  <TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor="#ffffff">
		<TD height='24' class='list_bg' align=center><%=bean.getData("ar_name")%></TD>
		<TD><IMG height=1 width=1></TD>
		<TD height='24' class='list_bg' align=center><%=bean.getData("ar_code")%></TD>
		<TD><IMG height=1 width=1></TD>
		<TD height='24' class='list_bg' align=center><%=bean.getData("ar_priorty")%></TD>
		<TD><IMG height=1 width=1></TD>
		<TD height='24' class='list_bg' align=center><a href="ranki.jsp?j=u&ar_id=<%=bean.getData("ar_id")%>"><img src='../images/lt_modify.gif' border='0' align='abmiddle'></a> <a href="rankp.jsp?j=d&ar_id=<%=bean.getData("ar_id")%>"><img src='../images/lt_del.gif' border='0' align='abmiddle'></a></TD>
		<TD><IMG height=1 width=1></TD>
		<TD height='24' class='list_bg' align=center></TD>
		<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
	  </TR>
<%	}	%>
	</TBODY>
	</TABLE></TD></TR>
</BODY>
</HTML>
