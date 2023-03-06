<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	import		= "java.sql.*,com.anbtech.text.Hanguel" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	String inspection_class_code	= request.getParameter("class_code");	
%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<form name='searchForm' action="search_inspection_code.jsp" method="post">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55" colspan='2'></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF" ><img src="../images/pop_chk_item_search.gif" hspace="10" alt='검사항목찾기'></TD>
					<TD style="padding-right:10px" align='right' valign='middle'  bgcolor="#73AEEF"></TD></TR>
			    <TR><TD height='1' bgcolor='#9DA8BA' colspan='2'></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="98%" border='0' align='center'>
			<TBODY>
				<TR><TD width=4>&nbsp;</TD>
					<TD align=left width='520'>
					<SELECT name='class_code' onchange='javascript:document.frml.submit()'>
						<option value=''>선택</option>
<%
	String sql = "SELECT DISTINCT inspection_class_code, inspection_class_name FROM qc_inspection_item";
	bean.openConnection();	
	bean.executeQuery(sql);
	while(bean.next()){
%>
						<OPTION value='<%=bean.getData("inspection_class_code")%>'><%=bean.getData("inspection_class_name")%></option>
<%	}
%>
					</SELECT>
					<% if(!inspection_class_code.equals("")) {	%>
						<script>
							document.searchForm.class_code.value = '<%=inspection_class_code%>';
						</script>
					<% } %>
				</TD></TR></TBODY></TABLE></TD></TR>
  
	<TR height=100%><!--리스트-->
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="98%" align='center'>
			<TBODY>
		    <TR><TD height='2' bgcolor='#9CA9BA' colspan='7'></TD></TR>
			<TR vAlign=middle height=23>
				<TD noWrap width=30 align=middle class='list_title'>번호</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>검사항목코드</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>검사항목명</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>선택</TD>
			</TR>			
			<TR bgColor=#9DA9B9 height=1><TD colspan='7'></TD></TR>
<%	
	//선택된 분류에 속한 검사항목 리스트 가져오기
	if(inspection_class_code == null || inspection_class_code.equals("")) {
		sql = "SELECT * FROM qc_inspection_item ORDER BY inspection_class_code ASC";
	} else {
		sql = "SELECT * FROM qc_inspection_item WHERE inspection_class_code = '" + inspection_class_code + "' ORDER BY inspection_code ASC";
	}
	bean.executeQuery(sql);

	int no = 1 ;	
	while(bean.next()){	
%>
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>

			<TD height='24' class='list_bg' align=center><%=no%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center><%=bean.getData("inspection_code")%></TD>
		    <TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center><%=bean.getData("inspection_name")%></TD>
		    <TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center><a href="javascript:returnValue('<%=bean.getData("inspection_code")%>','<%=bean.getData("inspection_name")%>');"><img src='../images/lt_sel.gif' border='0'></a></TD>
		</TR>
		<TR><TD colSpan='7' background="../images/dot_line.gif"></TD></TR>
<%		no++;
	}
%>
		</TBODY></TABLE></TD></TR>

		<!--꼬릿말-->
        <TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
			<A href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></A></TD></TR>
        <TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR>
     </TBODY></TABLE></TD></TR></TABLE>
</form>
</BODY>
</HTML>


<SCRIPT language="javascript">

function returnValue(inspection_code,inspection_name)
{           
	opener.document.writeForm.inspection_code.value = inspection_code;
	opener.document.writeForm.inspection_name.value = inspection_name;
	self.close();
}

</SCRIPT>