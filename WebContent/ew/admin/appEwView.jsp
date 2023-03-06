<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.ew.entity.*,com.anbtech.admin.entity.*"
%>
<%
	ExtraWorkHistoryTable ewhistory = new ExtraWorkHistoryTable();
	ArrayList arry = new ArrayList();
	arry = (ArrayList)request.getAttribute("appArry");
	Iterator iter = arry.iterator();
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ew/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
  <TR><TD height="25"><img src="../ew/images/worker_list.gif" border="0" alt="특근대상자 리스트"></TD></TR>
  <TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
  <TR height="100%"><!--리스트-->
    <TD valign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>부서명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>직급</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>특근자명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>특근일시</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>특근사유</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	int no = 1;
	while(iter.hasNext()) {
		ewhistory = (ExtraWorkHistoryTable)iter.next();
		String o_no			= ewhistory.getOno() + "";
		String member_name	= ewhistory.getMemberName();
		String rank_name	= ewhistory.getMemberRankName();
		String division		= ewhistory.getDivisionName();
		String duty			= ewhistory.getDuty();
		String w_sdate		= ewhistory.getWsdate();
		w_sdate = w_sdate.substring(0,4) + "-" + w_sdate.substring(4,6) + "-" + w_sdate.substring(6,8);
		String w_stime		= ewhistory.getWstime();
		String w_edate		= ewhistory.getWedate();
		w_edate = w_edate.substring(0,4) + "-" + w_edate.substring(4,6) + "-" + w_edate.substring(6,8);
		String w_etime		= ewhistory.getWetime();

%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=division%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=rank_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=member_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=w_sdate%> <%=w_stime%> ~ <%=w_edate%> <%=w_etime%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style="padding-left:5px"><%=duty%></td>
			</TR>
			<TR><TD colSpan=11 background="../ew/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></TD></TR></TABLE>

</body>
</html>
