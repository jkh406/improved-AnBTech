<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
	errorPage	= "../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String opener_fname	= request.getParameter("opener_fname");
	String opener_code	= request.getParameter("opener_code");
	String opener_name	= request.getParameter("opener_name");

	String code	= request.getParameter("code")==null?"na":request.getParameter("code");
	String name	= request.getParameter("name")==null?"na":request.getParameter("name");;
	
	String search_item = request.getParameter("sItem")==null?"name":request.getParameter("sItem");
	String search_word = request.getParameter("sWord")==null?"#":request.getParameter("sWord");
	if(search_word != null) 
		search_word = new String(search_word.getBytes("ISO-8859-1"), "euc-kr");	

	String sql="";
	if(search_item.equals("name")){
		sql = "SELECT code,name,name2 FROM maker_code_table WHERE name LIKE '%"+search_word+"%' or name2 LIKE '%"+search_word+"%'  and stat = '1' ORDER BY name ASC";
	}else{
		sql = "SELECT code,name,name2 FROM maker_code_table WHERE " + search_item + " LIKE '%"+search_word+"%' and stat = '1' ORDER BY name ASC";
	}
	bean.openConnection();	
	bean.executeQuery(sql);
%>

<HTML><HEAD><TITLE>승인업체코드선택</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
			<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/pop_detail_sel.gif" width="181" height="17" hspace="10"></TD></TR>
			<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--버튼-->
	<FORM name="sForm" action="searchMakerCode.jsp" method="post" style="margin:0">
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
		<TBODY>
			<TR><TD width="350" height=25 align="left">
				<SELECT name='sItem'>
					<OPTION value='name'>업체명</OPTION>
					<OPTION value='code'>업체코드</OPTION>
					
				</SELECT> 
				<%	if(!search_item.equals("")){	%>
					<SCRIPT language='javascript'>
						document.sForm.sItem.value = '<%=search_item%>';
					</SCRIPT>
				<%	}	%>			
					<INPUT type="text" name="sWord" size="15">
					<a href='javascript:document.sForm.submit();'><img src='images/bt_search3.gif' border='0' align='absmiddle' ></a>
					<a href="javascript:self.close();"><img src="images/close.gif" border="0" align='absmiddle' ></a></TD></TR></TBODY></TABLE>
					<INPUT type='hidden' name='opener_fname' value='<%=opener_fname%>'>
					<INPUT type='hidden' name='opener_code' value='<%=opener_code%>'>
					<INPUT type='hidden' name='opener_name' value='<%=opener_name%>'>
					<INPUT type='hidden' name='code' value='<%=code%>'>
					<INPUT type='hidden' name='name' value='<%=name%>'>
	</FORM>
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="99%" border=0 valign="top">
	  <TBODY>
	  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	  <TR height=200><!--리스트-->
		<TD vAlign=top  height="200">
		  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=23>
				  <TD noWrap width=80 align=middle class='list_title'>업체코드</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="images/list_tep2.gif"></TD>
				  <TD noWrap width=50% align=middle class='list_title'>업체명(영문)</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="images/list_tep2.gif"></TD>
				  <TD noWrap width=50% align=middle class='list_title'>업체명(한글)</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="images/list_tep2.gif"></TD>
				  <TD noWrap width=40 align=middle class='list_title'>선택</TD>
			   </TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=7></TD></TR>
<%
	
	while(bean.next()){	
		String sel = "<a href=\"javascript:returnValue('" + bean.getData("code") + "','" + bean.getData("name") + "');\"><img src='images/lt_sel.gif' border='0'></a>";
%>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=bean.getData("code")%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("name")%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("name2")==null?"&nbsp;":bean.getData("name2")%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sel%></TD>
				</TR>
				<TR><TD colSpan=7 background="images/dot_line.gif"></TD></TR>
	<%
		}
	%>
			</TBODY></TABLE></TD></TR></TBODY></TABLE>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
			<TR><TD width="100%" height=5 colSpan=4></TD></TR>
			<TR><TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD></TR>
			<TR><TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD></TR></TBODY></TABLE></TD></TR></TABLE>
</BODY></HTML>

<script language='javascript'>
function returnValue(code,name){
	opener.document.<%=opener_fname%>.<%=opener_code%>.value = code;
	opener.document.<%=opener_fname%>.<%=opener_name%>.value = name;
	self.close();
}
</script>