<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAM02.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*,com.anbtech.am.admin.*"%>
<%
	StringBuffer sb = new StringBuffer();  
	sb = (StringBuffer)request.getAttribute("CategoryList");
%>


<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../am/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 
valign="top">
  <TBODY>
	<TR height=28>
		<TD vAlign=top><!-- Ÿ��Ʋ �� ������ ���� -->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../am/images/blet.gif" align="absmiddle"> �ڻ�з�����</TD></TR></TBODY></TABLE></TD></TR>
	<TR height=32>
		<TD vAlign=top><!--��ư �� ����¡-->
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY> 
				<TR><TD align=left width='90%' style='padding-left:5px'>
					<a href="javascript:input_ct()"><IMG src='../am/images/bt_add_b.gif' alt='��з����' border='0' align='absmiddle'></a>
					<TD width='10%' align='right' style="padding-right:10px"></TD></TR></TBODY></TABLE></TD></TR>
	<TR height=100%>
		<TD vAlign=top><!--����Ʈ-->
			<FORM name="listForm" method="get" style='magrgin:0'>
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR bgColor=#9DA9B9 height=2><TD colspan=9></TD></TR>
					<TR vAlign=middle height=23>
					  <TD noWrap width=150 align=middle class='list_title'>�з���</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>�з�����</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>����</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
					  <TD noWrap width=150 align=middle class='list_title'>���</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
					  <TD noWrap width=100% align=middle class='list_title'></TD>
				   </TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
					<%=sb.toString()%>
			</TBODY></TABLE>
</FORM>
</TD></TR></TBODY></TABLE>
</BODY>
</HTML>


<script>
function input_ct(){
   location.href ="../servlet/AssetServlet?mode=category_info_view&div=f";

}
</script>