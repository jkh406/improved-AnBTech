<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "부서일정 목록LIST 보기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.date.anbDate"			%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%
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
	
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자

	/*********************************************************************
	 	기안자 login 알아보기
	*********************************************************************/
	id = login_id; 			//접속자

	//초기화
	cal_id=cal_ot=SELyear=SELmonth=SELdate="";
	Tyear=Tmonth=Tday=items="";

	/*********************************************************************
	 	넘겨온 변수 읽기 (from Calendar_WriteD.jsp)   Sabun=&Date=
		사번 : 부서 코드, 
	*********************************************************************/
	String Rsabun = request.getParameter("Sabun");			//넘겨받은 사번으로 부서코드
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
	3. 회사 등록정보 조회하기
	***********************************************************************/
	String[] indColumns = {"pid","id","sub","myear","mmonth","mday","eyear","emonth","eday","mtime","item","isopen"};
	bean.setTable("CALENDAR_SCHEDULE");
	bean.setColumns(indColumns);
	bean.setOrder("mday,mtime ASC");
	bean.setClear();
	bean.setSearch("id",cal_id,"myear",SELyear,"mmonth",SELmonth);
	bean.init_unique();		
%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
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
	document.write("</select>년&nbsp");
	document.write("<select name=\"select_month\" class=\"select\" OnChange=\"MovePage()\">");
	
	for (var j = 0; j < arrmonth.length; j++) {
		document.write("<option value=\"" + arrmonth[j]);	
		if ( arrmonth[j] == month)
			document.write("\" selected>" + arrmonth[j] + "</option>");
		else
			document.write("\">" + arrmonth[j] + "</option>");
	}
	document.write("</select>월");	
//	document.write("</td>");
}

function MovePage()
{  
     var year = document.goform.select_year.value;
     var month = document.goform.select_month.value;
     var cno = document.basicinfo.hdSabun.value;
	location.replace("Calendar_divList.jsp?OpenView&Start=1&count=1000&Sabun=" + cno + "&Date=" + year + "*" + month + "&blank");

}
// -->
</SCRIPT>
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 부서일정 검색</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top><Form Name=goform onSubmit="return goPage()" style="margin:0">
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='300'><A href="Calendar_divList.jsp?OpenView&Start=1&count=1000&Sabun=<%=cal_id%>&Date=<%=PYear%>*<%=PMonth%>"><img src="../images/arrow_back.gif" border="0"></A>&nbsp;<script Language="javascript">SelectPage('<%=SELyear%>','<%=SELmonth%>')</script>&nbsp;<A href="Calendar_divList.jsp?OpenView&Start=1&count=1000&Sabun=<%=cal_id%>&Date=<%=NYear%>*<%=NMonth%>"><img src="../images/arrow_next.gif" border="0"></A></TD></TR></TBODY>
		</TABLE></form></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=100 align=middle class='list_title'>날짜</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>항목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>제목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>시간</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>공개여부</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<% if (bean.isEmpty()) { %>
	<TR bgColor=#ffffff><TD height='30' colspan=9 align='center' class='list_bg'>*****  입력된 일정이 없습니다. *****</TD></TR> 
<% } else {
		while(bean.isAll()) {	
%>	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=bean.getData("myear")%>/<%=bean.getData("mmonth")%>/<%=bean.getData("mday")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("item")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="Calendar_Modify.jsp?PID=<%=bean.getData("pid")%>&Opendocument&view=webViewW"><%=bean.getData("sub")%></a></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("mtime")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>
					<% if(bean.getData("isopen").equals("1")) { //공개%>
						<img src="../images/open.gif" border="0">
					<% } else {									//비공개%>
						<img src="../images/secret.gif" border="0">
					<% } %>			  
			  </td></TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<% 
		} //while
} //if
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>

<form name="basicinfo">
<input type="hidden" Name="hdSabun" value="<%=cal_id%>">
</form>
</body>
</html>