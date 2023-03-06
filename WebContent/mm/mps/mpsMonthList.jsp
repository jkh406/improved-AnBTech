<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "월별 MPS LIST 보기"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.text.Hanguel"
	import="java.util.StringTokenizer"
%>

<%	
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//Date에 관련된 연산자

	/*********************************************************************
	 	파라미터 전달받기
	*********************************************************************/	
	//인수로 넘어온 년,월,일 받기
	String view_td = (String)request.getAttribute("view_td"); if(view_td == null) view_td=anbdt.getDate(0);
	String year = view_td.substring(0,4);
	String month = view_td.substring(5,7);
	String day = view_td.substring(8,10);		
	String cal_td = view_td;

	String factory_no = (String)request.getAttribute("factory_no");	//공장번호
	
	/*----------------------------------------
	//금주를 기준으로 view(주간,2주간)으로 넘겨줄 변수만들기
	// (해당주의 일요일 날자를 넘겨준다)
	-----------------------------------------*/
	int iyear = Integer.parseInt(anbdt.getYear());			//금년
	int imonth = Integer.parseInt(anbdt.getMonth());		//금월
	int iday = Integer.parseInt(anbdt.getDates());			//금일
	int tmp_td = anbdt.getDay(iyear,imonth,iday);			//오늘요일 (1:일 2:월 ~ 7:토)

	//주어진 날자에서 요일을 이용하여 금주의 일요일 날자를 구한다.
	//찾고자 하는 날자의 일요일 날자로 setting후 주간보기로 넘겨준다.
	int sunday = iday-tmp_td+1;
	view_td = anbdt.setDate(iyear,imonth,sunday); //넘겨줄 날자 (형식:yyyy/mm/dd)	

	/*********************************************************************
	 	이전,다음달 년,월,일 구하기
	*********************************************************************/	
	String PYear = "";				//이전년도
	String PMonth = "";				//이전월
	String NYear = "";				//다음년도
	String NMonth = "";				//다음월
	int toYear = Integer.parseInt(year);							//년도

	String toMon = month;
	if(toMon.substring(0,1).equals("0")) toMon = toMon.substring(1,2);
	int toMonth = Integer.parseInt(toMon);							//월
	if((1 < toMonth) && (toMonth < 12)) { 							//2월 ~ 11월까지
		PYear = Integer.toString(toYear);							//이전년도 (동일년도)
		PMonth = Integer.toString(toMonth - 1);						//이전월
		if(PMonth.length() == 1) PMonth = "0" + PMonth;

		NYear = Integer.toString(toYear);							//다음년도 (동일년도)
		NMonth = Integer.toString(toMonth + 1);						//다음월
		if(NMonth.length() == 1) NMonth = "0" + NMonth;
	} else if(1 == toMonth){										//1월
		PYear = Integer.toString(toYear - 1);						//이전년도 
		PMonth = "12";												//이전월

		NYear = Integer.toString(toYear);							//다음년도 (동일년도)
		NMonth = Integer.toString(toMonth + 1);						//다음월	
		if(NMonth.length() == 1) NMonth = "0" + NMonth;	
	} else if(12 == toMonth){										//12월
		PYear = Integer.toString(toYear);							//이전년도 (동일년도)
		PMonth = "11";												//이전월

		NYear = Integer.toString(toYear + 1);						//다음년도 
		NMonth = "01";												//다음월		
	} 
	
	//내용받기
	com.anbtech.mm.entity.mpsMasterTable table;
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("MASTER_List");
	table = new mpsMasterTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD><TITLE>월별 MPS LIST 보기</TITLE>
<link rel='stylesheet' type='text/css' href='../css/style.css'>
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=factory_no%>공장 인쇄폼 MPS일정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=cal_month&year=<%=year%>&month=<%=month%>&factory_no=<%=factory_no%>" onMouseOver="window.status='월간일정계획';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_m.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_week1&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='주간일정계획';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_w.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_week2&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='2주간일정계획';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_2w.gif" border="0"></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_month&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='월간일정리스트';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_p_o.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=200 style="padding-left:10px" valign='middle'>
			  <form name="goform" onSubmit="return false;" style="margin:0">
				   <A href="../servlet/mpsInfoServlet?mode=list_month&view_td=<%=PYear%>/<%=PMonth%>/<%=day%>&factory_no=<%=factory_no%>" onMouseOver="window.status='이전달로 이동';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/arrow_back.gif" border="0" align="absmiddle"></A>

					<SELECT NAME="hdStartYear">
					<% 
						//년도 화면 Display하기 (현재년도기준 2년전부터 10년까지)
						String YMD = anbdt.getYear();								//현재년을 구한다.
						int YYYY = Integer.parseInt(YMD) - 2; 						//현재년에서 -2년을 한다.

						String SELY = "";
						for(int yi=0; yi < 10; yi++) {
							String YEAR = Integer.toString(YYYY + yi);				//년도표기
							if(year.equals(YEAR)) SELY="SELECTED";
							else SELY = "";
							out.println("<OPTION " + SELY + " >" + YEAR);
						}
						out.println("</SELECT><font color='#565656'>년</font>");
					%>
					<SELECT NAME="hdStartMonth" onChange="goCalendar(document.goform)">
					<%
						//월을 화면에 Display하기 
						String MYMD = anbdt.getMonth();							//현재월을 구한다.
						int MM = Integer.parseInt(MYMD); 						//현재월

						String SELM = "";
						for(int mi=1; mi <= 12; mi++) {
							String MONTH = Integer.toString(mi);			//월표기
							if(MONTH.length() == 1) MONTH = "0" + MONTH;
							if(month.equals(MONTH)) SELM="SELECTED";
							else SELM = "";
							out.println("<OPTION " + SELM + " >" + MONTH);
						}
						out.println("</SELECT><font color='#565656'>월</font>");
					%>
					<A href="../servlet/mpsInfoServlet?mode=list_month&view_td=<%=NYear%>/<%=NMonth%>/<%=day%>&factory_no=<%=factory_no%>" onMouseOver="window.status='다음달로 이동';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/arrow_next.gif" border="0" align="absmiddle"></A></form>					  
			  </TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!-- 내용 -->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TR>
			<TD vAlign=top>
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR vAlign=middle height=25>
							<TD noWrap width=60 align=middle class='list_title'>생산공장코드</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>생산품목코드</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=100% align=middle class='list_title'>생산품목설명</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=60 align=middle class='list_title'>생산계획수량</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>진행상태</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>생산계획일자</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>계획등록일</TD></TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=14></TD></TR>
<%
					while(table_iter.hasNext()) {
						table = (mpsMasterTable)table_iter.next();
						String status = table.getMpsStatus();
						if(status.equals("1")) status = "작성";
						else if(status.equals("2")) status = "확정요청";
						else if(status.equals("3")) status = "승인확정";
						else if(status.equals("4")) status = "MRP생성";
						else if(status.equals("5")) status = "MRP승인";
						else if(status.equals("6")) status = "생산오더확정";
						else if(status.equals("7")) status = "생산마감";
%>
						<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
							<TD align=middle height="24" class='list_bg'><%=table.getFactoryNo()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'>
							<a href='javascript:view(<%=table.getPid()%>)'><%=table.getItemCode()%></a></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'> <%=table.getItemSpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=Integer.toString(table.getPlanCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=status%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getPlanDate()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getRegDate()%></td>
						</TR>
			<TR><TD colspan=14 background="../mm/images/dot_line.gif"></TD></TR>
<%	} //while
%>
</TBODY></TABLE></TD></TR></TABLE>
</BODY>
</HTML>

<SCRIPT LANGUAGE="JavaScript">
<!-- 
//수정하기
function view(pid) 
{
	location.href = "../servlet/mpsInfoServlet?mode=mps_view&pid="+pid+"&factory_no="+'<%=factory_no%>';
}
//바로가기
function goCalendar(sform)
{
     var tmpYear = sform.hdStartYear.options[sform.hdStartYear.selectedIndex].text;			//선택한 년도
     var tmpMonth = sform.hdStartMonth.options[sform.hdStartMonth.selectedIndex].text;		//선택한 월
	 var tmpDate = '<%=day%>';
	 var view_td = tmpYear+"/"+tmpMonth+"/"+tmpDate;
	 var factory_no = '<%=factory_no%>';
     location.href = "../servlet/mpsInfoServlet?mode=list_month&view_td="+view_td+"&factory_no="+factory_no;
}
-->
</SCRIPT>