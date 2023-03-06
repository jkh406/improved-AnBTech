<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제조회권한관리"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//--------------------
	//	초기화 정보
	//--------------------
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	bean.openConnection();

	/*********************************************************************
	 	내부데이터 받기
	*********************************************************************/
	String query = "",msg="";

	/*********************************************************************
	 	과제조회권한 LIST가져오기
	*********************************************************************/
	query = "SELECT count(*) FROM psm_view_mgr";
	bean.executeQuery(query);
	bean.next();
	int cnt = Integer.parseInt(bean.getData(1));
	String[][] data = new String[cnt][9];

	query = "SELECT * FROM psm_view_mgr order by user_id asc";
	bean.executeQuery(query);
	int n=0;
	while(bean.next()) {
		data[n][0] = bean.getData("pid");
		data[n][1] = bean.getData("pjt_type"); 
			if(data[n][1].equals("A")) data[n][1]="전과제조회";
			else data[n][1]="지정과제조회";
		data[n][2] = bean.getData("pjt_grade");
		data[n][3] = bean.getData("user_id");
		data[n][4] = bean.getData("user_name");
		data[n][5] = bean.getData("div_code");
		data[n][6] = bean.getData("div_name");
		data[n][7] = bean.getData("psm_code");
		data[n][8] = bean.getData("psm_korea");
		n++;
	} //while

%>
<HTML>
<HEAD><TITLE>PSM 과제조회권한관리자 목록</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0" oncontextmenu="return false">
<FORM name="sForm" method="post" style="margin:0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../images/blet.gif"> 과제조회관리자 목록</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<a href="javascript:write();"><img src="../images/bt_reg.gif" border=0></a>
					<!--
					<a href="javascript:contentModify();"><img src="../images/bt_modify.gif" border=0></a>
					<a href="javascript:contentDelete();"><img src="../images/bt_del.gif" border=0></a>-->
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!-- 리스트 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
    <TBODY>
		<TR vAlign=middle height=25>
			<TD noWrap width=30 align=middle class='list_title'>번호</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100 align=middle class='list_title'>구분</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100 align=middle class='list_title'>사번</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=80 align=middle class='list_title'>이름</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=120 align=middle class='list_title'>과제코드</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=120 align=middle class='list_title'>과제명</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100% align=middle class='list_title'>부서명</TD>

		</TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
	<% if (cnt == 0) { %>
		<TR vAlign=center height=22>
			 <td colspan='15' align="middle">***** 내용이 없습니다. ****</td>
		</tr> 
	<% } %>	

	<% 
		for(int i=0; i<data.length; i++) {
	%>	
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			<TD align=middle height="24" class='list_bg'><%=i+1%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=data[i][1]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><a href="javascript:contentView('<%=data[i][0]%>');"><%=data[i][3]%></a></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=data[i][4]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=data[i][7]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=data[i][8]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;&nbsp;<%=data[i][6]%></TD>
		</TR>
		<TR><TD colSpan=15 background="../images/dot_line.gif"></TD></TR>
	<% 
		}  //for

	%>
	</TBODY>
</TABLE>

<input type="hidden" name="mode" value=''>
<input type="hidden" name="pid" value=''>
</form>
</body>
</html>

<script language=javascript>
<!--
var msg = '<%=msg%>';
if(msg.length != 0) {alert(msg); }
//등록하기
function write()
{
	var f = document.sForm;
	document.sForm.action='psmViewMgrInput.jsp';
	document.sForm.mode.value='ADD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//내용보기
function contentView(pid)
{
	document.sForm.action='psmViewMgrInput.jsp';
	document.sForm.mode.value='VIEW';
	document.sForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//사원정보 찾기
function selectUserInfo()
{

	var strUrl = "../searchUserInfo.jsp?target=sForm.user_id/sForm.user_name";
	newWIndow = wopen(strUrl, "PsmUserInot", "260", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>