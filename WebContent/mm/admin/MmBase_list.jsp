<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"	
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	bean.openConnection();
	String sql	= "SELECT * FROM mfg_grade_mgr order by mgr_type,factory_no asc";
	bean.executeQuery(sql);
%>
<HTML>
<HEAD><TITLE>사용자 권한관리 목록</TITLE>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 사용자 권한관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='600'><A HREF='javascript:add_mbase()'><IMG src='../images/bt_reg.gif' align='absmiddle' border='0'></a></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
<!--리스트-->
  <TR height=100%>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=22>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>작업구분</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>사용자구분코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>담당자구분명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>담당자사번</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>생산공장코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>편집</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>

		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
<% 
	int no = 1;
	while(bean.next()) {

	String pid		= bean.getData("pid");
	String mgr_type	= bean.getData("mgr_type");
	String mgr_code	= bean.getData("mgr_code");
	String mgr_name	= bean.getData("mgr_name");
	String mgr_id	= bean.getData("mgr_id");	
	String factory_no= bean.getData("factory_no");	
%>
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no++%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=mgr_type%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg' style='padding-left:5'><%=mgr_code%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg' style='padding-left:5'><%=mgr_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=mgr_id%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=factory_no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><A HREF="javascript:modify('<%=pid%>')"><IMG src='../images/lt_modify.gif' align='absmiddle' border='0' alt='수정'></a>&nbsp;<A HREF="javascript:del('<%=pid%>')"><IMG src='../images/lt_del.gif' align='absmiddle' border='0' alt='삭제'></a></td>
			  <TD align=middle class='list_bg'></td>
			  <TD><IMG height=1 width=1></TD>
		<TR><TD colSpan=15 background="../images/dot_line.gif"></TD></TR>	
<% 
	}  //while 
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>
</body>
</html>

<script language='javascript'>
<!--
	function add_mbase(){
		var url = "MmBase_input.jsp?j=a";
		wopen_b(url,'input','500','217');
	}
	function modify(pid){
		var url = "MmBase_input.jsp?j=u&pid="+pid;
		wopen_b(url,'modify','500','217');
	}
	function del(pid){
		if (confirm("정말 삭제하시겠습니까?")) {
		location.href="MmBase_process.jsp?j=d&pid="+pid;	
		}
	}

	function wopen_b(url, t, w, h) {
		var sw;
		var sh;

		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
	}	
-->
</script>