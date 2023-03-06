<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "해당공장 생산계획일정보기(월간)"		
	contentType = "text/html; charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"	
%>

<%
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//Date에 관련된 연산자
	String id=login_id;				//login

	/*********************************************************************
	 	파라미터 전달받기
	*********************************************************************/	
	//금일 날자 구하기 (최초 달력을 만든다.)
	String cal_ot = anbdt.getDate();							//오늘날자(형식:yyyy-mm-dd)

	//인수로 넘어온 년,월,일 받기
	String year = (String)request.getAttribute("year");		if(year == null) year = anbdt.getYear();
	String month = (String)request.getAttribute("month");	if(month == null) month = anbdt.getMonth();	
	String day = anbdt.getDates();		
	String cal_td = year + "/" + month + "/" + day;			//넘겨받은 년월일 조합

	String factory_no = (String)request.getAttribute("factory_no");		//공장번호
	String cal_dl = (String)request.getAttribute("CAL_List");			//MPS일정
	String cal_hl = (String)request.getAttribute("HLD_List");			//휴일일정
	String msg = (String)request.getAttribute("msg");					//메시지출력

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
	String view_td = anbdt.setDate(iyear,imonth,sunday); //넘겨줄 날자 (형식:yyyy/mm/dd)	

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
	
%>

<HTML>
<HEAD><TITLE>생산계획일정</TITLE>
<link rel='stylesheet' type='text/css' href='../css/style.css'>
</HEAD>
<SCRIPT LANGUAGE="JavaScript">
<!-- 
//메시지 출력하기
var msg = '<%=msg%>';
if(msg.length !=0) { alert(msg); }

//캐린더 만들기
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
     	       tmp2 = "0" + tmp1;				//일자
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
                var tmpLinkItem = tmpLink.split("@");		//저정된 일정 내용  ex> Y,FG코드(수량),pid
                
				//화면에 출력하기
                if (parseInt(doclist_Year,10) == tmpYear) {
               		if (parseInt(doclist_Month,10) == tmpMonth) {
               			if (parseInt(doclist_Day,10) == tmpDay) {
    				       if (tmpLinkItem[0] == "Y") {			//내용을 화면에 출력하기
    					     	//tmpImgsrc = "<img src=\"../mm/images/secret.gif\" border=\"0\">";	     
								tmpString = tmpString + "<a href=\"../servlet/mpsInfoServlet?mode=mps_view&factory_no=<%=factory_no%>&pid=" + tmpLinkItem[2] + "\" onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">" + tmpImgsrc+tmpLinkItem[1] +"</a><BR>";
						   }
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
//e:요일숫자+일자,dayno:일자,content:내용
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

     var chkDay = CheckHoliday(dayno);	//공휴일명 찾기
     var htitle = ""
     strDiv="</font><DIV ID=\"View1\" STYLE=\"position:static; visibility:visible; left:0; height:65; width:100%;overflow:auto;\">"   
      	
     if ((e % 7) == 0) { //일요일
     	if(chkDay!=null) htitle=" <font size=2 color=red>"+ chkDay + "</font><br>"
		tmpRt = startTR + "<TD width=112 height=65 bgcolor=" + bgcolor + " valign=top>" + "<a href=\'../servlet/mpsInfoServlet?mode=mps_view&pid=&factory_no=<%=factory_no%>&view_td=<%=year%>/<%=month%>/"+dayno+"\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=red><b>" + dayno + "</b></font></a>" + htitle + strDiv + content +"</div></TD>";
     } else if ((e % 7) == 6) { //토요일
		fclass="<a href=\'../servlet/mpsInfoServlet?mode=mps_view&pid=&factory_no=<%=factory_no%>&view_td=<%=year%>/<%=month%>/"+dayno+"\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=#565656><b>" + dayno + "</b></font></a>"
     	if(chkDay!=null) {
			fclass="<a href=\'../servlet/mpsInfoServlet?mode=mps_view&pid=&factory_no=<%=factory_no%>&view_td=<%=year%>/<%=month%>/"+dayno+"\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=#565656><b>" + dayno + "</b></font></a>"
			htitle =" <font size=2 color=red>"+ chkDay + "</font><br>"
		}	
   		tmpRt = "<TD width=112 height=65 bgcolor=" + bgcolor + " valign=top>"+fclass +  htitle+strDiv + content + "</div></TD>"+endTR;
     } else { //평일
		fclass="<a href=\'../servlet/mpsInfoServlet?mode=mps_view&pid=&factory_no=<%=factory_no%>&view_td=<%=year%>/<%=month%>/"+dayno+"\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font color=#565656><b>" + dayno + "</b></font></a>"
     	if(chkDay!=null) {
			fclass="<a href=\'../servlet/mpsInfoServlet?mode=mps_view&pid=&factory_no=<%=factory_no%>&view_td=<%=year%>/<%=month%>/"+dayno+"\' onMouseOver=\"window.status='<%=year%>년 <%=month%>월 " + dayno + "일에 새 일정 등록';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=black><b>" + dayno + "</b></font></a>"
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
	 var factory_no = '<%=factory_no%>';
     location.href = "../servlet/mpsInfoServlet?mode=cal_month&year="+tmpYear+"&month="+tmpMonth+"&factory_no="+factory_no;
}
// -->
</SCRIPT>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<FORM name="hidform" onSubmit="return false;" style="margin:0">
	<INPUT NAME="hdUserCno" VALUE="<%=id%>" Type="Hidden">
	<INPUT NAME="hdOrgToday" VALUE="<%=cal_ot%>" Type="Hidden"><!- 오늘날짜 -->
	<INPUT NAME="hdToday" VALUE="<%=cal_td%>" Type="Hidden"><!- 인수로 넘어온 날짜 -->
	<INPUT NAME="hdDocList" VALUE="<%=cal_dl%>" Type="Hidden">
	<INPUT NAME="hdHolidayList" VALUE="<%=cal_hl%>" Type="Hidden"><!- 휴일리스트 -->	
</FORM>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=factory_no%> 공장 생산계획일정</TD></TR></TBODY>
				</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR>
					<TD align=left width=5></TD>
				    <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=cal_month&year=<%=year%>&month=<%=month%>&factory_no=<%=factory_no%>" onMouseOver="window.status='월간일정계획';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_m_o.gif" border="0"></a></TD>
					<TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
					<TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_week1&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='주간일정계획';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_w.gif" border="0"></a></TD>
					<TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
					<TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_week2&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='2주간일정계획';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_2w.gif" border="0"></TD>
					<TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
					<TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_month&view_td=<%=anbdt.getDate(0)%>&factory_no=<%=factory_no%>" onMouseOver="window.status='월간일정리스트';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_p.gif" border="0"></a></TD>
					<TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
					<TD align=left width=200 style="padding-left:10px" valign='middle'>
				<FORM name="goform" onSubmit="return false;" style="margin:0">
				   <A href="../servlet/mpsInfoServlet?mode=cal_month&year=<%=PYear%>&month=<%=PMonth%>&factory_no=<%=factory_no%>" onMouseOver="window.status='이전달로 이동';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/arrow_back.gif" border="0" align="absmiddle"></A>

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
					<A href="../servlet/mpsInfoServlet?mode=cal_month&year=<%=NYear%>&month=<%=NMonth%>&factory_no=<%=factory_no%>" onMouseOver="window.status='다음달로 이동';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/arrow_next.gif" border="0" align="absmiddle"></A></form>					  
				</TD></TR></TBODY></TABLE></TD></TR>
		<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!-- 요일 표시 시작 -->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TR>
			<TD vAlign=top>
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR vAlign=middle height=25>
							<TD width=112 align=middle class='list_title'><IMG src="../mm/images/sunday.gif"></TD>
							<TD width=112 align=middle class='list_title'><IMG src="../mm/images/monday.gif"></TD>
							<TD width=112 align=middle class='list_title'><IMG src="../mm/images/tuesday.gif"></TD>
							<TD width=112 align=middle class='list_title'><IMG src="../mm/images/wednesday.gif"></TD>
							<TD width=112 align=middle class='list_title'><IMG src="../mm/images/thursday.gif"></TD>
							<TD width=112 align=middle class='list_title'><IMG src="../mm/images/friday.gif"></TD>
							<TD width=112 align=middle class='list_title'><IMG src="../mm/images/saturday.gif"></TD>
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
