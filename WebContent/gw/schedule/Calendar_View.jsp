<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "나의일정 보기(월간)"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"  
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"	
%>
<%@	page import="com.anbtech.date.anbDate"			%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<%@	page import="com.anbtech.gw.business.Calendar_View"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	//login 계정 변수
	String id="";					//login

	//메시지 전달변수
	String Message="";				//메시지 전달 변수  

	//읽을 전달 변수
	String cal_id = "";				//사용자 사번
	String cal_di = "";				//사용자 부서관리 코드
	String cal_ot = "";				//오늘날자(형식:yyyy/mm/dd)
	String cal_td = "";				//인수로 넘어온 날자 (형식:yyyy/mm/dd)
	String cal_dl = "";				//해당월 일정 목록
	String cal_hl = "";				//해당월 공휴일 목록

	//선택한 년,월 변수
	String selYear = "";			//선택한 년도
	String selMonth = "";			//선택한 월

	//이전,다음달 변수
	String PYear = "";				//이전년도
	String PMonth = "";				//이전월
	String NYear = "";				//다음년도
	String NMonth = "";				//다음월
	String PNDay = "";				//일

	//view(주간,2주간)로 넘겨줄 변수(무조건 금주를 기준으로 넘겨준다)
	String view_td="";				//넘겨줄 날자 (형식:yyyy/mm/dd)

	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	Calendar_View view = new com.anbtech.gw.business.Calendar_View();			//해당월 일정을 가져오는 연산자

	/*********************************************************************
	 	기안자 login 알아보기
	*********************************************************************/
	id = login_id; 			//접속자

	//인수로 넘어온 사번읽기
	cal_id = request.getParameter("Sabun");				//접속자 id
	if(cal_id == null) cal_id = id;						//넘어온 인수없으면 login id

	//부서 관리코드 찾기
	String[] udiColumns = {"ac_id","id"};
	bean.setTable("user_table");
	bean.setColumns(udiColumns);
	bean.setOrder("ac_id DESC");	
	bean.setClear();	
	bean.setSearch("id",cal_id);
	bean.init_unique();

	if(bean.isEmpty()) cal_di = "";
	else {
		while(bean.isAll()) cal_di = bean.getData("ac_id");
	}

	//인수 초기화
	cal_ot = cal_td = cal_dl = cal_hl = "";
	PYear=PMonth=NYear=NMonth=PNDay="";

	/*********************************************************************
	 	읽을 일정 보여주기
	*********************************************************************/	
	//금일 날자 구하기 (최초 달력을 만든다.)
	cal_ot = anbdt.getDate();							//오늘날자(형식:yyyy-mm-dd)

	//인수로 넘어온 년,월,일 받기
	String year = request.getParameter("year");
	if(year == null) year = anbdt.getYear();			//처음open시 금년도가져오기
	String month = request.getParameter("month");
	if(month == null) month = anbdt.getMonth();			//처음open시 금월가져오기
	String day = request.getParameter("day");
	if(day == null) day = anbdt.getDates();				//처음open시 금일가져오기

	cal_td = year + "/" + month + "/" + day;			//넘겨받은 년월일 조합

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
	
	/*---------------------------------------------
	//일정내용을 쿼리하기
	----------------------------------------------*/
	cal_dl = "";	//일정목록 clear(회사,부서,개인)
	//1. 회사 일정목록 읽기 (기준:0)
	cal_dl = view.Other_View("0",year,month,"COM");

	//2. 부서 일정목록 읽기 (기준:부서관리코드)
	cal_dl += view.Other_View(cal_di,year,month,"DIV");

	//3. 개인 일정목록 읽기 (기준 : 사번)
	if(cal_id.equals(id)) {		//전부를 출력한다. (login자신의것)
		cal_dl += view.Owner_View(cal_id,year,month);
	} else {						//공개된 내용만 출력한다. (공유자것)
		cal_dl += view.Other_View(cal_id,year,month,"INI");
	}

	/*---------------------------------------------
	//휴일목록 (목록을 읽어 연결한다.)
	----------------------------------------------*/
	String[] commonColumns = {"id","item","hdyear","hdmon","nlist"};
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(commonColumns);
	bean.setOrder("nlist DESC");	
	bean.setClear();	

	//쿼리 문장 (where절)
	String holidy_data = "where (item='LHD' and hdmon='" + month + "')";
		holidy_data += " or (item='BHD' and hdyear='" + year + "' and hdmon='" + month + "')";
		holidy_data += " or (item='EHD' and id='" + cal_id + "' and hdyear='" + year + "' and hdmon='" + month + "')"; 

	bean.setSearchWrite(holidy_data);
	bean.init_write();
	
	while(bean.isAll()) {
		cal_hl += bean.getData("nlist")+";";
	}	

	/*********************************************************************
	 	선택한 년 월 구하기
	*********************************************************************/	
	//선택한 년도
	selYear = request.getParameter("year");							//선택된 년도
	if(selYear == null) selYear = anbdt.getYear();					//초기년도는 해당년도

	//선택한 월
	selMonth = request.getParameter("month");						//선택된 월
	if(selMonth == null) selMonth = anbdt.getMonth();				//초기월은 해당월	
	if(selMonth.length() == 1) selMonth = "0" + selMonth;

	/*********************************************************************
	 	이전,다음달 년,월,일 구하기
	*********************************************************************/	
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
	PNDay = anbdt.getDates();										//현재일
%>

<HTML>
<HEAD>
<Script Language="JavaScript">
<!--
		var currpath = "";
		var viewname = "";
-->
</Script>

<SCRIPT LANGUAGE="JavaScript">
<!-- 
var inDate = "";

function MakeCalendar() {
    //서버의 시간을 읽어온다.
    var fromField = document.hidform.hdToday;
    var syear =  fromField.value.substring(0, 4);	//현재 년도
    var smonth = fromField.value.substring(5, 7) ;	//현재 달
    var sday = fromField.value.substring(8, 10);	//오늘 날짜

    //Client시간을 setting한다.
    now = new Date();
    now.setTime(Date.UTC(syear, smonth-1, 1))

    var day   = now.getDate();
    var month = now.getMonth();
    var year  = now.getYear();
    if(year<2000){year = year + 1900}
    year = year - 1990;  

    displayCalendar(day, month, year);  //달력그리기
}

function displayCalendar(day, month, year) {
    day = parseInt(day);
    month = parseInt(month);
    year = parseInt(year);
    year = year + 1990;
    
    var i = 0;
    var days = getDaysInMonth(month+1,year);  		//월 마지막일자 계산
    var firstOfMonth = new Date (year, month, 1);	//년, 월, 1일 로 객체생성
    var startingPos  = firstOfMonth.getDay();		//1일자의 요일을 구한다.(0:일,1:월,2:화 ...)

    days += startingPos;

    // 시작일 이전 날짜를 공백으로 처리 
    for (i = 0; i < startingPos; i++) {
         document.write(displayTable(i, "<BR>", ""));
    }

    // 달력구성
    var tmp1, tmp2;
    var content = "";
    for (i = startingPos; i < days; i++)  
    {
        if (i-startingPos+1 < 10) {
	           tmp1 = i-startingPos+1;
     	       tmp2 = "0" + tmp1;
     	       content = dispcontent(parseInt(year,10), parseInt(month+1,10), parseInt(tmp2,10)); //년/월/일
               document.write(displayTable(i, tmp2, content));	//관련내용 화면에 출력
        } else {
               content = dispcontent(parseInt(year,10), parseInt(month+1,10), parseInt(i-startingPos+1,10));
               document.write(displayTable(i, i-startingPos+1,content));
        }        
    }
    // 현재 달의 마지막 날짜이후의 날짜를 공백으로 처리
    if ((i%7) != 0) {
    	if ((i%7) == 6)  {  
	    document.write(displayTable(i, "<BR>", ""));
    	} else {
	    for (k=0; k<(7-(i%7)); k++)  {               		
		if ((i % 7) == 6) {
        		document.write(displayTable(i, "<BR>", ""));
          	} else {
             		document.write(displayTable(i, "<BR>", ""));
          	}
    	   } //end for
        } //end if
    }else{
    	return false;
    }

}

//일정이 있는 날에 등록된 일정제목 가지고 오기(일정내용 LINK하기)
function dispcontent(tmpYear, tmpMonth, tmpDay){
     var tmpdoclist = "";
     var tmpString = "";
     var tmpImgsrc = "";
     if (document.hidform.hdDocList.value != "") {
    	  var doclist = document.hidform.hdDocList.value.split(";")
	      for (var v=0; v < doclist.length-1; v++) {
    		    tmpdoclist = doclist[v];  //해당월의 일정목록
				
     		    var doclist_Data = tmpdoclist.split("*");
     		    var doclist_Date = doclist_Data[0];				//"년/월/일"
                var doclist_Date1 = doclist_Date.split("/");    //날짜 가져오기(2000/03/05)
               
                var doclist_Year = doclist_Date1[0];   			//년도
                var doclist_Month = doclist_Date1[1]; 			//달
                var doclist_Day = doclist_Date1[2]; 			//일
               
                var tmpLink = doclist_Data[1];
                var tmpLinkItem = tmpLink.split("@");			//저정된 일정 내용  ex> 8A2329406E,Calendar_View,Y,근무지(00:00~00:00),pid
                //document.write(tmpLinkItem);
                if (parseInt(doclist_Year,10) == tmpYear) {
               		if (parseInt(doclist_Month,10) == tmpMonth) {
               			if (parseInt(doclist_Day,10) == tmpDay) {
    				       if (tmpLinkItem[2] == "Y") {			//img 보여주기 (공개:Y  미공개:N)
    					     	tmpImgsrc = "";
    				       } else {	
							    var sc = '../images';
								tmpImgsrc = "<img src=\""+sc+"/secret.gif\" border=\"0\">";
    				       }
				           //상세 일정내용 Link 정보 리턴값 	     
							tmpString = tmpString + "<a href=\"Calendar_Modify.jsp?PID=" + tmpLinkItem[4] + "&opendocument&view=" + tmpLinkItem[1] + "\" onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">" + tmpImgsrc + tmpLinkItem[3] +"</a><BR>";		     
						}
    			    }
    		    }  

    	  } //end for
    	  return tmpString;
     } else {
    	  return "";
     } 
}

//달력구성을 위한 HTML TAG 생성
function displayTable(e, dayno, content) {
     var toDay = new Date()
     var month=toDay.getMonth()+1
     if(month<10) {month="0"+month}

     var tmpmon = document.goform.hdStartMonth;		//선택한 월을 읽고
     var tmpRt = "";					//리턴값 (td문장)
     var bgcolor = "";
     var startTR = "<TR>";
     var endTR = "</TR>";
    	
     currentday = document.hidform.hdToday.value.substring(8, 10); //오늘 날짜
     cal_month =  tmpmon.options[document.goform.hdStartMonth.selectedIndex].text; //현재 화면의 달
               
     if (dayno == "<BR>") { //날짜값이 없는 셀일 경우 배경색
    	bgcolor = "#F5F5F5";
     } else if (dayno == currentday && month == cal_month) { //오늘날짜 배경색
		bgcolor = "#FFFFFF";
     } else if ((e % 7) == 0) { //일요일일 경우 배경색
    	bgcolor = "#DFEDFD";
     } else if ((e % 7) == 6) { //토요일일 경우 배경색
    	bgcolor = "#EFF0F5";
     } else { //평일일 경우 배경색
    	bgcolor = "#FFFFFF";
     }

     var chkDay = CheckHoliday(dayno);
     var htitle = ""
     strDiv="</font><DIV ID=\"View1\" STYLE=\"position:static; visibility:visible; left:0; height:65; width:100%;overflow:auto;\">"   
      	
     if ((e % 7) == 0) { //일요일
     	if(chkDay!=null) htitle=" <font size=2 color=red>"+ chkDay + "</font><br>"
			tmpRt = startTR + "<TD width=112 height=65 bgcolor=" + bgcolor + " valign=top>" + "<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=red><b>" + dayno + "</b></font></a>" + htitle + strDiv + content +"</div></TD>";
     } else if ((e % 7) == 6) { //토요일
		fclass="<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=#565656><b>" + dayno + "</b></font></a>"
     	if(chkDay!=null) {
		fclass="<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=#565656><b>" + dayno + "</b></font></a>"
		htitle =" <font size=2 color=red>"+ chkDay + "</font><br>"
	}	
   		tmpRt = "<TD width=112 height=65 bgcolor=" + bgcolor + " valign=top>"+fclass +  htitle+strDiv + content + "</div></TD>"+endTR;
     } else { //평일
		fclass="<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font color=#565656><b>" + dayno + "</b></font></a>"
     	if(chkDay!=null) {
			fclass="<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=black><b>" + dayno + "</b></font></a>"
			htitle =" <font size=2 color=red>"+ chkDay + "</font><br>"
		}	
     	tmpRt = "<TD width=112 height=65 bgcolor=" + bgcolor + " valign=top>"+fclass + htitle+ strDiv + content +"</div></TD>";
     }
     return (tmpRt);
} 

//공휴일인지를 파악하여 공휴일이면 공휴일명을 리턴한다.
function CheckHoliday(dayno) {
	var hList = document.forms[0].hdHolidayList.value
	var idx = hList.indexOf(dayno+"#")
	if(idx > -1) {
		aStr=hList.substring(idx,hList.length).split(";")
		Str=aStr[0].split("#")
		return Str[1]	//공휴일명
	}
	return
}

// 2월달 최대날짜 계산
function getDaysInMonth(month,year)  {   
   var days;
    if (month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==0 || month==12)  days=31;
    else if (month==4 || month==6 || month==9 || month==11) days=30;
    else if (month==2)  {
         if (isLeapYear(year)) {
            days=29;
        } else {
            days=28;
        }
    }    
    return (days);
}

// 윤년 계산
function isLeapYear (Year) {
    if (((Year % 4)==0) && ((Year % 100)!=0) || ((Year % 400)==0)) {
        return (true);
    }
    else {
        return (false);
    }
}

//바로가기
function goCalendar(sform){
     var tmpYear = sform.hdStartYear.options[sform.hdStartYear.selectedIndex].text;			//선택한 년도
     var tmpMonth = sform.hdStartMonth.options[sform.hdStartMonth.selectedIndex].text;		//선택한 월
     var tmpCno = document.hidform.hdUserCno.value;											//사용자 사번
     var tmpDay = getDaysInMonth(tmpMonth, tmpYear);										//해당월의 날자수
     var cday = document.hidform.hdOrgToday.value.substring(8, 10); 						//현재 날짜
     if(cday>tmpDay) cday=tmpDay

     var tmp = document.URL        
     var urlarray = tmp.split("?")
     var viewarray = tmp.split("/")
     var tmppath = "/" + viewarray[3] + "/" + viewarray[4] + "/"
     location.href = "Calendar_View.jsp?PID=&Sabun=" + tmpCno + "&year=" + tmpYear + "&month=" + tmpMonth + "&day=" + cday
}
// -->
</SCRIPT>
<link rel='stylesheet' type='text/css' href='../css/style.css'>
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<form name="hidform" onSubmit="return false;" style="margin:0">
	<INPUT NAME="hdUserCno" VALUE="<%=cal_id%>" Type="Hidden">
	<INPUT NAME="hdOrgToday" VALUE="<%=cal_ot%>" Type="Hidden"><!오늘날짜->
	<INPUT NAME="hdToday" VALUE="<%=cal_td%>" Type="Hidden"><! 인수로 넘어온 날짜 ->
	<INPUT NAME="hdDocList" VALUE="<%=cal_dl%>" Type="Hidden">
	<INPUT NAME="hdHolidayList" VALUE="<%=cal_hl%>" Type="Hidden"><! 휴일리스트 ->	

</form>

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 월간일정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="Calendar_View.jsp?Sabun=<%=cal_id%>" onMouseOver="window.status='월간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_m_o.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View1.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='주간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_w.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View2.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='2주간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_2w.gif" border="0"></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_Print.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='인쇄폼 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_p.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=200 style="padding-left:10px" valign='middle'>
			   <form name="goform" onSubmit="return false;" style="margin:0">
				   <A href="Calendar_View.jsp?PID=&openform&Sabun=<%=cal_id%>&year=<%=PYear%>&month=<%=PMonth%>&day=<%=PNDay%>" onMouseOver="window.status='이전달로 이동';return true;" onMouseOut="window.status='';return true;"><img src="../images/arrow_back.gif" border="0" align="absmiddle"></A>

					<SELECT NAME="hdStartYear">
					<% 
						//년도 화면 Display하기 (현재년도기준 2년전부터 10년까지)
						String YMD = anbdt.getYear();								//현재년을 구한다.
						int YYYY = Integer.parseInt(YMD) - 2; 						//현재년에서 -2년을 한다.

						String SELY = "";
						for(int yi=0; yi < 10; yi++) {
							String YEAR = Integer.toString(YYYY + yi);				//년도표기
							if(selYear.equals(YEAR)) SELY="SELECTED";
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
							if(selMonth.equals(MONTH)) SELM="SELECTED";
							else SELM = "";
							out.println("<OPTION " + SELM + " >" + MONTH);
						}
						out.println("</SELECT><font color='#565656'>월</font>");
					%>
					<A href="Calendar_View.jsp?PID=&openform&Sabun=<%=cal_id%>&year=<%=NYear%>&month=<%=NMonth%>&day=<%=PNDay%>" onMouseOver="window.status='다음달로 이동';return true;" onMouseOut="window.status='';return true;"><img src="../images/arrow_next.gif" border="0" align="absmiddle"></A></form>					  
			  </TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!-- 요일 표시 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TR>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=112 align=middle class='list_title'><IMG src="../images/sunday.gif"></TD>
			  <TD width=112 align=middle class='list_title'><IMG src="../images/monday.gif"></TD>
			  <TD width=112 align=middle class='list_title'><IMG src="../images/tuesday.gif"></TD>
			  <TD width=112 align=middle class='list_title'><IMG src="../images/wednesday.gif"></TD>
			  <TD width=112 align=middle class='list_title'><IMG src="../images/thursday.gif"></TD>
			  <TD width=112 align=middle class='list_title'><IMG src="../images/friday.gif"></TD>
			  <TD width=112 align=middle class='list_title'><IMG src="../images/saturday.gif"></TD>
		   </TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD vAlign=top>
	   <TABLE BGCOLOR="#CCCCCC" cellspacing=1 cellPadding=0 width="100%" border=0>
	     <TBODY>
		<Script Language="Javascript">
		   MakeCalendar();
		</Script>
		 </TBODY></TABLE></TD></TR></TABLE>
</BODY>
</HTML>