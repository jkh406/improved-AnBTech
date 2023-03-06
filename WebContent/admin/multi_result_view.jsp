<%@ include file= "configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<HTML><HEAD><TITLE>검색결과(중복)</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/pop_search_d.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR><TD height="2" bgcolor="2167B6"></TD></TR>
		<TR><TD height="5"></TD></TR></TBODY></TABLE></td></tr>

<%
	String target = request.getParameter("target");
	String sDiv = request.getParameter("sDiv");
	String sWord = Hanguel.toHanguel(request.getParameter("sWord"));

	bean.openConnection();
	String query = "";
	
	if(sDiv.equals("usr")){
		query = "SELECT a.id,a.name,a.pid,b.ar_name,c.ac_name FROM user_table a,rank_table b,class_table c WHERE a.name LIKE '%" + sWord +"%' and a.rank = b.ar_code and a.ac_id = c.ac_id";
		bean.executeQuery(query);
%>
  <tr><td align="center" valign="top">
	  <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
        <TBODY>
			<TR bgColor=#9DA9B9 height=2><TD colspan=7></TD></TR>
			<TR vAlign=middle height=7>
			  <TD noWrap width=100% height="23" align=middle class='list_title'>부서명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="images/list_tep.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>직급</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="images/list_tep.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>사번</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="images/list_tep.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>이름</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=7></TD></TR>

<%		while(bean.next()){	%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle class='list_bg' height="23"><%=bean.getData("ac_name")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("ar_name")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("id")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:go_sel('<%=target%>','<%=bean.getData("pid")%>');"><%=bean.getData("name")%></a></td>
			<TR><TD colSpan=7 background="images/dot_line.gif"></TD></TR>
<%		}	%>
			<TR><TD colSpan=7 height="10">
		</TBODY></TABLE>
<%	}else{
		query = "SELECT ac_name,pid FROM class_table WHERE ac_name LIKE '%" + sWord +"%'";
		bean.executeQuery(query);
%>
	  <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
        <TBODY>
			<TR bgColor=#9DA9B9 height=2><TD></TD></TR>
			<TR vAlign=middle height=7>
			  <TD noWrap width=100% height="23" align=middle class='list_title'>부서명</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD></TD></TR>

<%		while(bean.next()){	%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle class='list_bg' height="23"><a href="javascript:go_sel('<%=target%>','<%=bean.getData("pid")%>');"><%=bean.getData("ac_name")%></a></td>
			<TR><TD colSpan=7 background="images/dot_line.gif"></TD></TR>
<%		}	%>
			<TR><TD height="10">
		</TBODY></TABLE>

<%
	}
%>

</TD></TR>

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


<script language='javascript'>
<!--
function go_sel(target,pid)
{
	opener.location.href =  target+"?p_id="+pid;
	self.close();
}
//-->
</script>