<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	
	info = "공정코드정보목록"
	language = "java"
	contentType = "text/html;charset=euc-kr"
	errorPage = "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"	
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String flag_name = request.getParameter("flag_name")==null?"OP_CODE":request.getParameter("flag_name");
	String sql = "";

	bean.openConnection();
	sql		= "SELECT * FROM mbom_env WHERE flag = '1'";
	bean.executeQuery(sql);
%>
<HTML>
<HEAD><TITLE>공정코드정보목록</TITLE>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'  oncontextmenu="return false">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 생산공정코드관리</TD>
					<TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
	<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='600'><A HREF='javascript:add()'><IMG src='../images/bt_reg.gif' align='absmiddle' border='0'></a></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=22>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>공정코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>공정Description</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>약어</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>편집</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<% 
	int no = 1;
	while(bean.next()) {

	String pid		= bean.getData("pid");
	String flag		= bean.getData("flag");
	String m_code	= bean.getData("m_code");
	String spec		= bean.getData("spec");
	String tag		= bean.getData("tag");	
%>
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no++%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=m_code%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5'><%=spec%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=tag%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><A HREF="javascript:modify('<%=pid%>')"><IMG src='../images/lt_modify.gif' align='absmiddle' border='0' alt='수정'></a>&nbsp;<A HREF="javascript:del('<%=pid%>')"><IMG src='../images/lt_del.gif' align='absmiddle' border='0' alt='삭제'></a></td>
			   <TD align=middle class='list_bg'></td>
			  <TD><IMG height=1 width=1></TD>
		<TR><TD colSpan=11 background="../images/dot_line.gif"></TD></TR>	
<% 
	}  //while 
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>
</body>
</html>

<script language='javascript'>
<!--
	// 공정코드정보 등록창 띄우기
	function add(){
		var url = "BmBase_input.jsp?j=a&flag_name=<%=flag_name%>&flag=1";
		wopen(url,'input','500','233');
	}

	// 공정코드정보 수정창 띄우기
	function modify(pid){
		var url = "BmBase_input.jsp?j=u&pid="+pid+"&flag_name=<%=flag_name%>&flag=1";
		wopen(url,'modify','500','233');
	}

	// 공정코드정보 삭제
	function del(pid){
		if (confirm("정말 삭제하시겠습니까?")) {
		location.href="BmBase_process.jsp?j=d&pid="+pid;	
		}
	}

	function wopen(url, t, w, h) {
		var sw;
		var sh;

		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
	}	
-->
</script>