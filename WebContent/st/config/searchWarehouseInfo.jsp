<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*,java.io.*,java.util.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />


<%
	String tablename = request.getParameter("tablename");
	String field	 = request.getParameter("field");
	String factory_code = request.getParameter("factory_code");
	bean.openConnection();
	String sql = "SELECT * FROM "+tablename+" WHERE factory_code = '"+factory_code+"'";
		
	bean.executeQuery(sql);
	

%>
<HTML><HEAD><TITLE>창고찾기</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<FORM name=eForm>
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD>
	<!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" height='27' border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/*.gif" alt='BOM 정보보기'></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='30'></TD></TR>
	
	<TR><TD>
		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
			<TBODY>
				<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
				<TR vAlign=middle height=23>
					<TD noWrap width=80 align=middle class='list_title'>창고코드</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=100% align=middle class='list_title'>창고명</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=50 align=middle class='list_title'>선택</TD></TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%
	while(bean.next()) {
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=bean.getData("warehouse_code")%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg' style='padding-lefg'><%=bean.getData("warehouse_name")%></td>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><A HREF="javascript:returnValue('<%=field%>','<%=bean.getData("warehouse_code")%>','<%=bean.getData("warehouse_name")%>')"><IMG src="../images/lt_sel.gif" border='0' align='absmiddle'></a></TD></TR>
			<TR><TD colSpan=5 background="../images/dot_line.gif"></TD></TR>
<%	}
%>		<TR><TD height='30'></TD></TR></TBODY></TABLE></TD></TR>
		
		<TR><TD><!--꼬릿말-->
		<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
			<TBODY>
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					<a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD></TR>
				<TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR></TBODY></TABLE></TD></TR>

</TABLE>
</BODY></HTML>

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

</script>
