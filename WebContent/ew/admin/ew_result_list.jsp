<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.ew.entity.*"
%>
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree"/>

<%
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	EWLinkTable ewLinkTable;
	ExtraWorkHistoryTable table ;

	String division		= request.getParameter("div");
	String year			= request.getParameter("year");
	String month		= request.getParameter("month");

	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("arry");
	Iterator table_iter = table_list.iterator();
%>


<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../ew/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form method="get" name="eForm" action="../servlet/ExtraWorkServlet" style="margin:0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ew/images/blet.gif" align="absmiddle"> 정산처리현황</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				 	<SELECT name='division'>
					<%=recursion.viewComboByCode(0,0)%>
					</SELECT>
					<%	if(!division.equals("")){	%>
						<script language='javascript'>
							document.eForm.division.value = '<%=division%>';
						</script>
					<%	}	%>			  
					<SELECT name='year'>
						<OPTION value="2004">2004년</OPTION>
						<OPTION value="2005">2005년</OPTION>
						<OPTION value="2006">2006년</OPTION>
						<OPTION value="2007">2007년</OPTION>
						<OPTION value="2008">2008년</OPTION>
						<OPTION value="2009">2009년</OPTION>
						<OPTION value="2010">2010년</OPTION>
						<OPTION value="2011">2011년</OPTION>
					</SELECT>&nbsp;
				<%	if(!year.equals("")){	%>
						<script language='javascript'>
							document.eForm.year.value = '<%=year%>';
						</script>
				<%	}	%>
					<SELECT name='month'>
						<OPTION value="01">1월</OPTION>
						<OPTION value="02">2월</OPTION>
						<OPTION value="03">3월</OPTION>
						<OPTION value="04">4월</OPTION>
						<OPTION value="05">5월</OPTION>
						<OPTION value="06">6월</OPTION>
						<OPTION value="07">7월</OPTION>
						<OPTION value="08">8월</OPTION>
						<OPTION value="09">9월</OPTION>
						<OPTION value="10">10월</OPTION>
						<OPTION value="11">11월</OPTION>
						<OPTION value="12">12월</OPTION>
					</SELECT>		  
				<%	if(!month.equals("")){	%>
						<script language='javascript'>
							document.eForm.month.value = '<%=month%>';
						</script>
				<%	}	%>
			  <a href="javascript:go()"><img src="../ew/images/bt_confirm.gif" border="0" alt="실행" align="absmiddle"></a>
			  </TD>
			  <TD width='' align='right' style="padding-right:10px"></TD>
			  <TD align=middle width=150></TD>
			  </TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
 
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>부서명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>특근자명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>특근일시</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>특근시간(M)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>정산시간(H)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>정산금액</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>정산일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (ExtraWorkHistoryTable)table_iter.next();

		String o_no				= "" + table.getOno();
		String division_name	= table.getDivisionName();
		String rank_name		= table.getMemberRankName();
		String member_name		= table.getMemberName();
		String member_id		= table.getMemberId();
		String r_sdate			= table.getRsdate();
		String r_stime			= table.getRstime();
		String r_edate			= table.getRedate();
		String r_etime			= table.getRetime();
		r_sdate = r_sdate.substring(0,4) + "-" + r_sdate.substring(4,6) + "-" + r_sdate.substring(6,8);
		r_edate = r_edate.substring(0,4) + "-" + r_edate.substring(4,6) + "-" + r_edate.substring(6,8);
		String result_time		= table.getResultTime();
		String pay_by_work		= table.getPayByWork();
		String total_time		= table.getTotalTime();
		String confirm_date		= table.getConfirmDate();
		confirm_date = confirm_date.substring(0,4) + "-" + confirm_date.substring(4,6) + "-" + confirm_date.substring(6,8);
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=division_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=member_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=r_sdate%> <%=r_stime%> ~ <%=r_edate%> <%=r_etime%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=total_time%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=result_time%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(pay_by_work,"")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=confirm_date%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=17 background="../ew/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</form>
</body>
</html>

<script language="javascript">
<!--
	var checkflag = false; 

	function check(field) { 
		if (checkflag == false) { 
			for (i = 0; i < field.length; i++) { 
			field[i].checked = true; 
			} 
		checkflag = true; 
		}else { 
			for (i = 0; i < field.length; i++) { 
			field[i].checked = false; 
			} 
		checkflag = false; 
		} 
	}
	
	function go(){
		var f = document.eForm;
		var division = f.division.value;
		var year = f.year.value;
		var month = f.month.value;
		location.href = "../servlet/ExtraWorkServlet?mode=ew_result_list&div="+division+"&y="+year+"&m="+month;
	}
//-->
</script>
