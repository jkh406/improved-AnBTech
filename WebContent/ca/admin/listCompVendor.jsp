<%@ include file="../../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 승인업체코드관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'><a href="javascript:openWin('addCompVendor.jsp?mode=add')"><img src="../images/bt_add_comp2.gif" border="0" align="absmiddle"></a></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=80 align=middle class='list_title'>업체코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50% align=middle class='list_title'>업체명1</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=50% align=middle class='list_title'>업체명2</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>현상태</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>비고</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
	String query = "SELECT * FROM maker_code_table ORDER BY name ASC";
	bean.openConnection();
	bean.executeQuery(query);

	while(bean.next()){	
		String code	= bean.getData("code");
		String name	= bean.getData("name");
		String name2	= bean.getData("name2");
		if(name2 == null) name2 = "&nbsp;";
		String stat	= bean.getData("stat");
		if(stat.equals("1")) stat = "정상";
		else if(stat.equals("0")) stat = "<font color='red'>정지</font>";

		String mod_url = "<a href=javascript:openWin('addCompVendor.jsp?mode=update&code="+code+"')><img src='../images/lt_modify.gif' border='0' align='absmiddle'></a>";
		String del_url = "<a href=javascript:go_del('"+code+"')><img src='../images/lt_del.gif' border='0' align='absmiddle'></a>";
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=code%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=name2%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=stat%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=mod_url%> <%=del_url%></td>
			</TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</BODY>
</HTML>

<script language='javascript'>

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function openWin(url)
{
	wopen(url,'modify','400','224','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function go_del(code){ 
	var url = "addCompVendorProcess.jsp?mode=delete&code="+code;
	var is_confirm = confirm("정말 삭제하시게습니까?");
	if(is_confirm) window.open(url,'open','width=1,height=1,scrollbars=yes,toolbar=no,status=no,resizable=no');
}
</script>