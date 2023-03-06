<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>

<%
	String FieldName = request.getParameter("FieldName");
	String pjt_member = Hanguel.toHanguel(request.getParameter("pjt_member"));

	String man_info = "";					//개인정보
	StringTokenizer mbr = new StringTokenizer(pjt_member,";");
	int mbr_cnt = mbr.countTokens();		//개발인원수
	String[][] member = new String[mbr_cnt][4];
	int m = 0;
	while(mbr.hasMoreTokens()) {
		man_info = mbr.nextToken();
		StringTokenizer man = new StringTokenizer(man_info,"|");
		int n = 0;
		while(man.hasMoreTokens()) {
			member[m][n] = man.nextToken();
			n++;
		}
		m++;
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//크기조절
function centerWindow() 
{ 
        var sampleWidth = 420;                        // 윈도우의 가로 사이즈 지정
        var sampleHeight = 280;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 
//멤버 일괄 선택하기
function mbrcheck(field) 
{
	if (field[0].checked == true) { 
		for (i = 1; i < field.length; i++) { 
			field[i].checked = true;
		} 
	}else { 
		for (i = 1; i < field.length; i++) { 
			field[i].checked = false; 
		} 
	}
} 
//멤버 입력하기(sForm)
function selMember()
{
	
	var f = document.sForm.mbr;
	var sel_mbr = "";
	for(i=1; i<f.length; i++) if(f[i].checked) sel_mbr += f[i].value+";\n";

	opener.document.eForm.<%=FieldName%>.value = sel_mbr;
	self.close();
}
-->
</script>

<LINK href="../pjt/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onLoad="javascript:centerWindow();">
<form name="sForm" method="post" style="margin:0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> 해당노드의 개발멤버를 선택합니다.</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD width=4>&nbsp;</TD>
				<TD align=left width='400'>
					<a href='Javascript:selMember();'><img src='../pjt/images/bt_save.gif' border='0' align='absmiddle'></a>  <a href='javascript:self.close()'><img src="../pjt/images/bt_close.gif" border='0' align='absmiddle'></a>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=25>
				<TD noWrap width=50 align=middle class='list_title'>
				<input type=checkbox name='mbr' onClick="javascript:mbrcheck(document.sForm.mbr);">선택</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=60 align=middle class='list_title'>이 름</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>직 책</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>담당업무</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
		<% if (mbr_cnt == 0) { %>
		<TR vAlign=center height=22>
			 <td colspan='11' align="middle">***** 개발멤버가 없습니다. ****</td>
		</tr> 
		<% } %>	

		<% 
			for(int si=0; si<mbr_cnt; si++) {
				String[] grade_no = {"A","B","C","D","E","F","G"};
				String[] grade_name = {"과제PM","상기 PL","개발 PL","SUB-PL","전담개발","지원개발","외부인력"};
				String gname = "";	//직책
				for(int i=0; i<grade_no.length; i++) {
					if(member[si][0].equals(grade_no[i])) gname = grade_name[i];
				}
		%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'>
					<input type='checkbox' name='mbr' value='<%=member[si][1]%>/<%=member[si][2]%>'></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'><%=member[si][2]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'>&nbsp;&nbsp;<%=gname%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'>&nbsp;&nbsp;<%=member[si][3]%></TD>
			</TR>
			<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			}  //for

		%>
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

</BODY>
</HTML>

