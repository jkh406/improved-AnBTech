<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "인쇄 보기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.date.anbDate"			%>
<%@	page import="com.anbtech.gw.business.Calendar_Print"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%!
	//login 계정 변수
	String id="";					//login

	//메시지 전달변수
	String Message="";				//메시지 전달 변수  

	//전달변수 읽은 변수 (from Calendar_View.jsp)
	String cal_id = "";				//사용자 사번
	String cal_di = "";				//사용자 부서관리 코드
	String cal_ot = "";				//오늘날자(형식:yyyy/mm/dd)
	String view_td = "";			//금주의 일요일날자를 넘겨주기 (yyyy/mm/dd)	

	String SELyear="";				//인수로 넘어온 년도
	String SELmonth="";				//인수로 넘어온 월
	String SELdate="";				//인수로 넘어온 일

	//이전,다음달 변수
	String PYear = "";				//이전년도
	String PMonth = "";				//이전월
	String NYear = "";				//다음년도
	String NMonth = "";				//다음월
	String PNDay = "";				//일

	//년월일 변수
	String Tyear="";				//년도
	String Tmonth="";				//월
	String Tday="";					//일

	//쿼리내용 화면에 보여주기
	String[][] rd;					//쿼리한 내용배열에 담기 
	int count = 0;					//쿼리한 갯수
	String searchitem="";			//검색할 항목
	String items = "";				//해당자의 일정항목 목록

%>
<%	
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	Calendar_Print view = new com.anbtech.gw.business.Calendar_Print();			//해당월 일정을 가져오는 연산자

	/*********************************************************************
	 	기안자 login 알아보기
	*********************************************************************/
	id = login_id; 			//접속자

	//부서 관리코드 찾기
	String[] udiColumns = {"ac_id","id"};
	bean.setTable("user_table");
	bean.setColumns(udiColumns);
	bean.setOrder("ac_id DESC");	
	bean.setClear();	
	bean.setSearch("id",id);
	bean.init_unique();

	if(bean.isEmpty()) cal_di = "";
	else {
		while(bean.isAll()) cal_di = bean.getData("ac_id");
	}

	//초기화
	cal_id=cal_ot=SELyear=SELmonth=SELdate="";
	Tyear=Tmonth=Tday=items="";

	/*********************************************************************
	 	넘겨온 변수 읽기 (from Calendar_View.jsp)   Sabun=&Date=
	*********************************************************************/
	String Rsabun = request.getParameter("Sabun");			//넘겨받은 사번
	if(Rsabun != null) cal_id = Rsabun;

	//넘겨받은 날자(yyyy*mm) from Calendar_View*.jsp 
	String Rdate = request.getParameter("Date");			

	//화면에 날자를 출력하기 위해서 
	if(Rdate != null) {
		SELyear = Rdate.substring(0,4);						//yyyy
		SELmonth = Rdate.substring(5,7);					//mm
		SELdate = "01";										//dd
	}

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
	view_td = anbdt.setDate(iyear,imonth,sunday);

	/*********************************************************************
	 	자체적인 선택으로 년월구하기
	*********************************************************************/
	//선택된 년도 월 입력받기 (화면에서)
	// (넘겨받은 변수인 경우를 제외하기 위해서)
	if(Rdate == null) {
		SELyear = request.getParameter("select_year");
		SELmonth = request.getParameter("select_month");
	}

	//SELyear and SELmonth가 null일 경우
	if(SELyear == null) { SELyear = Tyear; SELmonth = Tmonth; }

	/***********************************************************************
	//서버의 시스템 날자읽기
	***********************************************************************/
	cal_ot = anbdt.getDate();				//오늘날자 (형식:yyyy-MM-dd)
	Tyear = anbdt.getYear();				//년도
	Tmonth = anbdt.getMonth();				//월
	Tday = anbdt.getDates();				//일

	/*********************************************************************
	 	이전,다음달 년,월,일 구하기
	*********************************************************************/	
	int toYear = Integer.parseInt(SELyear);							//년도

	String toMon = SELmonth;
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
	PNDay = Tday;													//일

	/***********************************************************************
	3. 개인/회사/부서 등록정보 조회하기
	***********************************************************************/
	String cal_dl = "";	//일정목록 clear(회사,부서,개인)
	//1. 회사 일정목록 읽기 (기준:0)
	cal_dl = view.Other_Print("0",SELyear,SELmonth,"COM");

	//2. 부서 일정목록 읽기 (기준:부서관리코드)
	cal_dl += view.Other_Print(cal_di,SELyear,SELmonth,"DIV");

	//3. 개인 일정목록 읽기 (기준 : 사번)
	if(cal_id.equals(id)) {			//전부를 출력한다. (login자신의것)
		cal_dl += view.Owner_Print(cal_id,SELyear,SELmonth);
	} else {						//공개된 내용만 출력한다. (공유자것)
		cal_dl += view.Other_Print(cal_id,SELyear,SELmonth,"INI");
	}

	//일정내용 배열에 담기
	count = 0;
	StringTokenizer pdata = new StringTokenizer(cal_dl,";");
	count = pdata.countTokens();
	rd = new String[count][7];				//배열 초기화
	int mi = 0;
	while(pdata.hasMoreTokens()) {
		StringTokenizer pele = new StringTokenizer(pdata.nextToken(),"@");
		int ei = 0;
		while(pele.hasMoreTokens()) {
			rd[mi][ei] = pele.nextToken();
			ei++;
		}
		mi++;
	}

	//동일날자에 검색항목별 몇개인지 구하여 배열 rd[n][6]에 담기
	searchitem = Hanguel.toHanguel(request.getParameter("item"));
	if(searchitem == null) searchitem="all";	//전체내용	
	for(int ni=0; ni<count; ni++) {
		String RD = rd[ni][0];
		int scnt = 0;
		for(int cn=0; cn<count; cn++) {
			if(searchitem.equals("all")) {				//전체내용(날자만 구분)
				if(rd[cn][0].equals(RD)) scnt++;
			} else {									//선택한 항목만 (날자,선택한 항목 구분)
				if(rd[cn][3].lastIndexOf(">") == -1){	//개인일정(공유일정포함)
					if(rd[cn][0].equals(RD) && rd[cn][3].equals(searchitem)) scnt++;
				} else {								//회사,부서일정
					int lc = rd[cn][3].lastIndexOf(">"); 
					int le = rd[cn][3].length();
					String its = rd[cn][3].substring(lc+1,le);
					if(rd[cn][0].equals(RD) && searchitem.equals(its))	scnt++;	
				}
			}
		} //for
		rd[ni][6] = Integer.toString(scnt);
	} //for

	/***********************************************************************
	4. 항목별 등록정보 조회하기
	***********************************************************************/
	String[] itemColumns = {"id","item","nlist"};
	String query = "where (item='CIT') or (item='IIT' and id='" + cal_id + "')"; 
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(query);
	bean.init_write();

	if(bean.isEmpty()) items = "";
	else {
		while(bean.isAll()) items += bean.getData("nlist");
	}	

%>

<HTML>
<HEAD>

<SCRIPT LANGUAGE="JavaScript">
<!-- 
//***** 보기열 줄간 색구분 처리 *****
function ColorChang(bgcolor, nextcolor) {
  var RetColor ='';
  RetColor = (ColorMap % 2 ? bgcolor:nextcolor);
  ColorMap++;
  document.write('<TR bgcolor='+RetColor);
  return;
}

function SelectPage(year, month)
{
     var i = 0, j=0;
     var arryear = new Array ('2000','2001','2002','2003','2004','2005','2006','2007','2008','2009','2010','2011','2012','2013','2014','2015','2016','2017','2018','2019','2020');
     var arrmonth = new Array ('01','02','03','04','05','06','07','08','09','10','11','12');
     
//     document.write("<td width=35% valign=bottom align=left>");	
     document.write("<select name=\"select_year\" class=\"select\">");
	
	for (var i = 0; i < arryear.length; i++) {
		document.write("<option value=\"" + arryear[i]);	
		if ( arryear[i] == year)
			document.write("\" selected>" + arryear[i] + "</option>");
		else
			document.write("\">" + arryear[i] + "</option>");
	}
	document.write("</select><font color='#565656'>년</font>&nbsp");
	document.write("<select name=\"select_month\" class=\"select\" OnChange=\"MovePage()\">");
	
	for (var j = 0; j < arrmonth.length; j++) {
		document.write("<option value=\"" + arrmonth[j]);	
		if ( arrmonth[j] == month)
			document.write("\" selected>" + arrmonth[j] + "</option>");
		else
			document.write("\">" + arrmonth[j] + "</option>");
	}
	document.write("</select><font color='#565656'>월</font>");	
}

function MovePage()
{  
     var year = document.goform.select_year.value;
     var month = document.goform.select_month.value;
     var cno = document.basicinfo.hdSabun.value;
	location.replace("Calendar_Print.jsp?OpenView&Start=1&count=1000&Sabun=" + cno + "&Date=" + year + "*" + month + "&blank");

}
// -->
</SCRIPT>
<link rel='stylesheet' type='text/css' href='../css/style.css'>
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 인쇄폼일정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="Calendar_View.jsp?Sabun=<%=cal_id%>" onMouseOver="window.status='월간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_m.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View1.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='주간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_w.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View2.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='2주간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_2w.gif" border="0"></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_Print.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='인쇄폼 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_p_o.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=200 style="padding-left:10px" valign='middle'><form name='goform' onSubmit='return goPage()' style="margin:0">
					<a href="Calendar_Print.jsp?OpenView&Start=1&count=1000&Sabun=<%=cal_id%>&Date=<%=PYear%>*<%=PMonth%>" onMouseOver="window.status='이전달로 이동';return true;" onMouseOut="window.status='';return true;"><img src="../images/arrow_back.gif" border="0" align="absmiddle"></A>&nbsp;<Script Language="javascript">SelectPage('<%=SELyear%>','<%=SELmonth%>')</Script>&nbsp;<a href="Calendar_Print.jsp?OpenView&Start=1&count=1000&Sabun=<%=cal_id%>&Date=<%=NYear%>*<%=NMonth%>" onMouseOver="window.status='다음달로 이동';return true;" onMouseOut="window.status='';return true;"><img src="../images/arrow_next.gif" border="0" align="absmiddle"></A></form></TD>
			  <TD width=7 align='center'></TD>
			  <TD align=left width=400 style="padding-left:10px" valign='middle'>
					<form name='s' method='post' action='Calendar_Print.jsp' style="margin:0">
					<SELECT name='item'>
					<option value='all' selected>전체</option>
				<%
					String SEL = "";
					StringTokenizer itemsData = new StringTokenizer(items,";");
					while(itemsData.hasMoreTokens()) {
						String itd = itemsData.nextToken();
						if(itd.equals(searchitem)) SEL = "selected";
						else SEL = "";
						out.println("<option value='"+itd+"' "+SEL+">"+itd+"</option>");
					}
				%>
					</select> <a href='javascript:document.s.submit();'><img src='../images/bt_search.gif' border='0' align='absmiddle'></a>
					<input type='hidden' name='Sabun' value='<%=cal_id%>'>
					<input type='hidden' name='Date' value='<%=SELyear%>/<%=SELmonth%>/<%=SELdate%>'>
					</form></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!-- 내용 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TR>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=120 align=middle class='list_title'>날짜</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>항목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>제목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>시간</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>공개여부</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
	//일정내용 화면에 출력하기
	//rd[i][0]:일자,rd[i][1]:제목,rd[i][2]:공유,rd[i][3]:항목,rd[i][4]:시간,rd[i][5]:관리코드
	int samedate = 0;
	String day = "";
	int toyear = Integer.parseInt(SELyear);
	int tomonth = Integer.parseInt(SELmonth);
	for(int ni=0; ni<31; ni++) { 
		String today = anbdt.getDate(toyear,tomonth,1,ni);			//yyyy/MM/dd
		int readmonth = Integer.parseInt(today.substring(5,7));		//MM구하기

		//요일 구하기
		int ddd = anbdt.getDay(toyear,tomonth,ni+1);				//요일 숫자로 받기
		String[] D = {"","일","월","화","수","목","금","토"};
		day = D[ddd];
		if(day.equals("일")) day = "<font color=red>"+day+"</font>";
		else if(day.equals("토")) day = "<font color=blue>"+day+"</font>";
		else day = ""+day;

		//같은달만 화면에 출력함
		if(tomonth == readmonth) {				//쿼리내용중 같은달만 출력
			samedate = 0;
			for(int si=0; si<count; si++) {		//일자에 맞는 내용 비교를 위해 배열내용을 순서적으로 가져오기
				if(searchitem.equals("all")) {		//전체내용
					if(rd[si][0].equals(today)) {	//같은 일자에 전체항목 출력
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=rd[si][0]%> (<%=day%>)</td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=rd[si][3]%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="Calendar_Modify.jsp?PID=<%=rd[si][5]%>&Opendocument&view=webViewW'"><%=rd[si][1]%></a></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=rd[si][4]%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>
				<% if(rd[si][2].equals("Y")) { //공개%>
					<img src="../images/open.gif" border="0">
				<% } else { //비공개%>
					<img src="../images/secret.gif" border="0">
				<% } %>			  			  
			  </TD></TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<%
					samedate++;
					} ////if (같은 일자만)
				} //if (항목중 전체 선택시)
				else {		//항목중 해당항목 선택시
					String sits = "";
					if(rd[si][3].lastIndexOf(">") == -1){	//개인일정(공유일정포함)
						sits = rd[si][3];
					} else {								//회사,부서일정
						int lc = rd[si][3].lastIndexOf(">"); 
						int le = rd[si][3].length();
						sits = rd[si][3].substring(lc+1,le);
					}
					if(rd[si][0].equals(today) && sits.equals(searchitem)) {	//같은 일자,항목만 출력
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=rd[si][0]%> (<%=day%>)</td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=rd[si][3]%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="Calendar_Modify.jsp?PID=<%=rd[si][5]%>&Opendocument&view=webViewW'"><%=rd[si][1]%></a></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=rd[si][4]%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>
				<% if(rd[si][2].equals("Y")) { //공개%>
					<img src="../img/open.gif" border="0">
				<% } else { //비공개%>
					<img src="../img/secret.gif" border="0">
				<% } %>			  
			  </TD></TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<%
					samedate++;
					}
					
				} ////if (항목 선택시)
			} //for (일자 비교하기)
		} //if (같은달)
	} //for

%>
</TBODY></TABLE></TD></TR></TABLE>

<!-- 메뉴 이동 시작 -->
<form name="basicinfo">
<input type="hidden" Name="hdSabun" value="<%=cal_id%>">
</form>

</BODY>
</HTML>