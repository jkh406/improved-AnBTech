<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel, com.anbtech.gm.business.makeGoodsTreeItems"
	errorPage	= "../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<HTML><HEAD><TITLE>검색결과(중복)</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
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
	String searchscope = request.getParameter("searchscope");
	String searchword = Hanguel.toHanguel(request.getParameter("searchword"));

	bean.openConnection();
	String query = "";
	
	if(searchscope.equals("name") || searchscope.equals("name2") || searchscope.equals("code")){
		query = "SELECT mid,name,pid FROM goods_structure WHERE " + searchscope + " LIKE '%" + searchword +"%'";
		bean.executeQuery(query);
%>
  <tr><td align="center" valign="top" height='125'>
	  <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
        <TBODY>
			<TR bgColor=#9DA9B9 height=2><TD colspan=3></TD></TR>
			<TR vAlign=middle height=7>
			  <TD noWrap width=100% height="23" align=middle class='list_title'>제품분류</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="images/list_tep.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>모델명</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=3></TD></TR>

<%		while(bean.next()){	
			com.anbtech.gm.business.makeGoodsTreeItems tree = new com.anbtech.gm.business.makeGoodsTreeItems();	
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=left class='list_bg' height="23" style='padding-left:5px'><%=tree.getGoodsClassStr(Integer.parseInt(bean.getData("mid")),"")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><a href="javascript:go_sel('<%=target%>','<%=bean.getData("pid")%>');"><%=bean.getData("name")%></a></td>
			<TR><TD colSpan=3 background="images/dot_line.gif"></TD></TR>
<%		}	%>
			<TR><TD colSpan=3 height="10">
		</TBODY></TABLE>
<%	}else{

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