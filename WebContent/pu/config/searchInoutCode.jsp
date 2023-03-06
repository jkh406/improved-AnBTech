<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String tablename = request.getParameter("tablename");
	String field	 = request.getParameter("field");
	//tablename = "pu_inout_type";
	bean.openConnection();
	String sql = "SELECT * FROM "+tablename;
		
	bean.executeQuery(sql);
	

%>

<HTML><HEAD><TITLE>입출고형태찾기</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
  <TR><TD align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_inout_type.gif"  alt="입출고형태찾기"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE><BR>

	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="98%" border=0 valign="top">
	  <TBODY>
	  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	  <TR height=215><!--리스트-->
		<TD vAlign=top>
		<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=23>
				  <TD noWrap width=80 align=middle class='list_title'>코드</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=100% align=middle class='list_title'>입출고형태명</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=80 align=middle class='list_title'>선택</TD>
			   </TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%	
	while(bean.next()) {
%>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=bean.getData("type")%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("name")%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><a HREF="javascript:returnValue('<%=field%>','<%=bean.getData("type")%>','<%=bean.getData("name")%>')"><IMG src="../images/lt_sel.gif" border='0' align='absmiddle'></a></TD>
				</TR>
				<TR><TD colSpan=5 background="../images/dot_line.gif"></TD></TR>				
	<%
		}
	%>
			</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
		   <TR>
            <TD width="100%" height=5 colSpan=4></TD>
          </TR>
		  <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></TD></TR></TABLE>
</BODY></HTML>

<script language=javascript>
	
	function returnValue(field,type,name) {
		var fromField = field.split("/");
		if(eval("opener.document.forms[0]."+fromField[0]) !=null){
			eval("opener.document.forms[0]."+fromField[0]).value = type;
		}
		
		if(eval("opener.document.forms[0]."+fromField[1])!=null){
			eval("opener.document.forms[0]."+fromField[1]).value = name;
		}
		
		this.close();
	}

function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 386 ;
	item_list.style.height = div_h;
}
</script>
