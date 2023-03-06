<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*,java.io.*,java.util.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />


<%
	String code = request.getParameter("code");
	String name = request.getParameter("name");

	String sql = "SELECT * FROM st_conf_info";
	bean.openConnection();
	bean.executeQuery(sql);
	

%>
<HTML><HEAD><TITLE>수불유형찾기</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<FORM name=eForm>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display()'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55" colspan='2'></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF" ><img src="../images/pop_inout_type.gif" hspace="10" alt='수불유형찾기'></TD>
					<TD style="padding-right:10px" align='right' valign='middle'  bgcolor="#73AEEF"></TD></TR>
			    <TR><TD height='1' bgcolor='#9DA8BA' colspan='2'></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=25><!--버튼 및 페이징-->
		<TD vAlign=top>
			<TABLE height=25 cellSpacing=0 cellPadding=0 width="98%" border='0' align='center'>
			<TBODY>
				<TR><TD width=4>&nbsp;</TD>
					<TD align=left width='520'>
				</TD></TR></TBODY></TABLE></TD></TR>
  
	<TR height=100%><!--리스트-->
		<TD vAlign=top>
		<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
		<TABLE cellSpacing=0 cellPadding=0 width="98%" align='center'>
			<TBODY>
		    <TR><TD height='2' bgcolor='#9CA9BA' colspan='13'></TD></TR>
			
			<TR vAlign=middle height=23>
				<TD noWrap width=80 align=middle class='list_title'>수불유형코드</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>수불유형명</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>선택</TD></TR>
			</TR>			
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%	
		while(bean.next()) {
%>
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>

			<TD align=middle height="24" class='list_bg'><%=bean.getData("trade_type_code")%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=bean.getData("trade_type_name")%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><A HREF="javascript:returnValue('<%=bean.getData("trade_type_code")%>','<%=bean.getData("trade_type_name")%>')"><IMG src="../images/lt_sel.gif" border='0' align='absmiddle'></a></TD>
		</TR>
		<TR><TD colSpan=13 background="../images/dot_line.gif"></TD></TR>
<%		
		}
%>
		</TBODY></TABLE></DIV></TD></TR>

		<!--꼬릿말-->
        <TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
			<A href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></A></TD></TR>
        <TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR>
     </TBODY></TABLE></TD></TR></TABLE>
</FORM>
</BODY>
</HTML>

<script language=javascript>
function returnValue(code,name) {
	opener.document.forms[0].<%=code%>.value = code;
	opener.document.forms[0].<%=name%>.value = name;
	self.close();
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 

	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 590;
	var div_h = c_h - 108;
	item_list.style.height = div_h;

} 
</script>
