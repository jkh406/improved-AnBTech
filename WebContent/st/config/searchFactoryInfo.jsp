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
	String tablename = request.getParameter("tablename");
	String field	 = request.getParameter("field");
	bean.openConnection();
	String sql = "SELECT * FROM "+tablename;
		
	bean.executeQuery(sql);
	

%>

<HTML>
<HEAD><TITLE>공장찾기</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display();'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=5 width="100%" border=0>
				<TBODY>
					<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
					<TR><TD valign='middle' height='32' bgcolor="#73AEEF"><img src="../images/pop_find_factory.gif" align="absmiddle" alt='공장찾기'> </TD></TR>
					<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
					</TD></TR>
		<TR bgColor='' height=19><TD></TD></TR>
  
	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		    <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align=center>
			    <TBODY>
					<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=80 align=middle class='list_title'>공장코드</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>공장명</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
						<TD noWrap width=50 align=middle class='list_title'>선택</TD></TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%
					while(bean.next()) {
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="23" class='list_bg'><%=bean.getData("factory_code")%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=left height="23" class='list_bg' style='padding-lefg'><%=bean.getData("factory_name")%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><A HREF="javascript:returnValue('<%=field%>','<%=bean.getData("factory_code")%>','<%=bean.getData("factory_name")%>')"><IMG src="../images/lt_sel.gif" border='0' align='absmiddle'></a></td>
					</TR>
					<TR><TD colSpan=5 background="../images/dot_line.gif"></TD></TR>
									
<%			}
%>	
				</TBODY></TABLE></DIV></TD></TR>
				<!--꼬릿말-->
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					<a href='javascript:self.close()'>
					<img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD></TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
			</TD></TR></TABLE></TBODY></TABLE>
</BODY>
</HTML>


<script language=javascript>
	
	function returnValue(field,code,name) {
		var fromField = field.split("/");
		if(eval("opener.document.forms[0]."+fromField[0]) !=null){
			eval("opener.document.forms[0]."+fromField[0]).value = code;
		}
		
		if(eval("opener.document.forms[0]."+fromField[1])!=null){
			eval("opener.document.forms[0]."+fromField[1]).value = name;
		}
		
		this.close();
	}

	
	function display() { 
		var w = window.screen.width; 
		var h = window.screen.height; 
		var div_h = h - 631 ;
		item_list.style.height = div_h;
	}
</script>
