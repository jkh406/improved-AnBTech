<%@ include file="../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page language="java" contentType="text/HTML;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*"%>
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree" />

<HTML>
<head><title>�μ���������</title></head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<BODY leftmargin=0 topmargin=0 oncontextmenu='return false'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> �μ���������</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
					<a href="classi.jsp?j="><IMG src='../images/bt_add_c.gif' border='0' align='absmiddle'></a>
			  </TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	  <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=200 align=middle class='list_title'>����(�μ�)��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�����ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>��å�����ڻ��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>��뿩��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
		<%=recursion.viewTree(0,0)%>

		</TBODY></TABLE>
	</TD></TR>
</TABLE>
</BODY>
</HTML>


