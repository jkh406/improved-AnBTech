<%@ 	page		
	info= "달력 보기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="javax.swing.*"
	import="com.anbtech.text.Hanguel"	
%>
<%@	page import="com.anbtech.date.anbDate"	%>
<%
	String Message="";				//메시지 전달 변수 
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자

%>

<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<TITLE>Calendar</TITLE>
<SCRIPT LANGUAGE="JavaScript">
<!-- 
var inDate = "";

function setDate() {
    // 현재셋팅된 날짜로 calendar조정
    // dateField는 URL에서 호출한 필드를 추출
    var tmp = eval("self.opener.document.forms[0]." + document.forms[0].dateField.value);
	var rdate = tmp.value;

	//데이터값이 없으면
	if(rdate.length == 0) {
		a = new Date();
		y = a.getYear(); m = a.getMonth()+1; 	d = a.getDate();
		rdate = y+'/'+m+'/'+d;
	}

    var fromField=rdate.split("/")
    var syear =  fromField[0]
    var smonth = fromField[1]
    if(smonth.length==1) {smonth="0"+smonth}
    var sday = fromField[2]
    if(sday.length==1) {sday="0"+sday}    
    
    UTCDate = new Date();
    var tmp2=syear+smonth+sday
    if(tmp2.length==8) {UTCDate.setTime(Date.UTC(syear, smonth-1, sday))};
    var day   = UTCDate.getDate();
    var month = UTCDate.getMonth();
    var year  = UTCDate.getYear();
    
//    alert("year=" + year + "    month=" + month + "    day="+day)
    
	if(year<2000) {year = year + 1900}

    year = year - 1990;

    this.focusDay = day;
    document.forms[0].month.selectedIndex = month; 
    document.forms[0].year.selectedIndex    = year;
    displayCalendar(day, month, year);
}

function setToday() {
    // SET DAY MONTH AND YEAR TO TODAY'S DATE
    var now   = new Date();
    var day   = now.getDate();
    var month = now.getMonth();
    var year  = now.getYear();
    if(year<2000){year = year + 1900}
    year = year - 1990;

    this.focusDay                           = day;
    document.forms[0].month.selectedIndex = month;
    document.forms[0].year.selectedIndex   = year;
    displayCalendar(day, month, year);
}


function isFourDigitYear(year) {
    if (year.length == 4) {
        alert ("년도의 길이는 4자여야 합니다.");
        document.forms[0].year.select();
        document.forms[0].year.focus();
    }
    else {
        return true;
    }
}

function selectDate() {
    var year  = document.forms[0].year.selectedIndex;
    if (isFourDigitYear(year)) {
        var day   = 0;
        var month = document.forms[0].month.selectedIndex;
        displayCalendar(day, month, year);
    }
}

function setPreviousYear() {
    var year  = document.forms[0].year.selectedIndex;
    if (isFourDigitYear(year)) {
        var day   = 0;
        var month = document.forms[0].month.selectedIndex;
        year--;
        document.forms[0].year.selectedIndex = year;
        displayCalendar(day, month, year);
    }
}

function setPreviousMonth() {
    var year  = document.forms[0].year.selectedIndex;
    if (isFourDigitYear(year)) {
        var day   = 0;
        var month = document.forms[0].month.selectedIndex;
        if (month == 0) {
            month = 11;
            if (year > 1000) {
                year--;
                document.forms[0].year.selectedIndex = year;
            }
        }
        else {
            month--;
        }
        document.forms[0].month.selectedIndex = month;
        displayCalendar(day, month, year);
    }
}

function setNextMonth() {
    var year  = document.forms[0].year.selectedIndex;
    if (isFourDigitYear(year)) {
        var day   = 0;
        var month = document.forms[0].month.selectedIndex;
        if (month == 11) {
            month = 0;
            year++;
            document.forms[0].year.selectedIndex = year;
        }
        else {
            month++;
        }
        document.forms[0].month.selectedIndex = month;
        displayCalendar(day, month, year);
    }
}

function setNextYear() {
    var year  = document.forms[0].year.selectedIndex;
    if (isFourDigitYear(year)) {
        var day   = 0;
        var month = document.forms[0].month.selectedIndex;
        year++;
        document.forms[0].year.selectedIndex = year;
        displayCalendar(day, month, year);
    }
}

function displayCalendar(day, month, year) {       

    day     = parseInt(day);
    month   = parseInt(month);
    year    = parseInt(year);
    year = year + 1990;
    var i   = 0;
    var now = new Date();

    if (day == 0) {
        var nowDay = now.getDate();
    }
    else {
        var nowDay = day;
    }
    var days         = getDaysInMonth(month+1,year);
    var firstOfMonth = new Date (year, month, 1);
    var startingPos  = firstOfMonth.getDay();
    days += startingPos;

    // MAKE BEGINNING NON-DATE BUTTONS BLANK
    for (i = 0; i < startingPos; i++) {
        document.calButtons.elements[i].value = "--";       
    }

    // SET VALUES FOR DAYS OF THE MONTH
    var tmp1, tmp2;
    for (i = startingPos; i < days; i++)  
    {
        if (i-startingPos+1 < 10)
       {
          tmp1 = i-startingPos+1;
          tmp2 = "0" + tmp1;
            document.calButtons.elements[i].value = tmp2;
        } else { 
            document.calButtons.elements[i].value = i-startingPos+1;
        }        

        document.calButtons.elements[i].onClick = "returnDate"
    }

    // MAKE REMAINING NON-DATE BUTTONS BLANK
    for (i=days; i<42; i++)  {
        document.calButtons.elements[i].value = "--";

    }

    // GIVE FOCUS TO CORRECT DAY
    document.calButtons.elements[focusDay+startingPos-1].focus();
}


// GET NUMBER OF DAYS IN MONTH
function getDaysInMonth(month,year)  {
    var days;
    if (month==1 || month==3 || month==5 || month==7 || month==8 ||
        month==10 || month==12)  days=31;
    else if (month==4 || month==6 || month==9 || month==11) days=30;
    else if (month==2)  {
        if (isLeapYear(year)) {
            days=29;
        }
        else {
            days=28;
        }
    }
    return (days);
}


// CHECK TO SEE IF YEAR IS A LEAP YEAR
function isLeapYear (Year) {
    if (((Year % 4)==0) && ((Year % 100)!=0) || ((Year % 400)==0)) {
        return (true);
    }
    else {
        return (false);
    }
}


// SET FORM FIELD VALUE TO THE DATE SELECTED
function returnDate(inDay)
{
    var day   = inDay;
    var month = (document.forms[0].month.selectedIndex)+1;
    var year  = (document.forms[0].year.selectedIndex)+1990;

    if ((""+month).length == 1)
    {
        month="0"+month;
    }
    if ((""+day).length == 1)
    {
        day="0"+day;
    }
    if (day != "--") {
      var toField = eval("self.opener.document.forms[0]." + document.forms[0].dateField.value);
      toField.value = year + "/" + month + "/" + day;
      window.close();
    }
}    

function centerWindow() 
{ 
        var sampleWidth = 180;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 280;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

// -->
</SCRIPT>
</HEAD>

<BODY TEXT="000000" BGCOLOR="FFFFFF" ONLOAD="setDate();">

<!--
<FORM METHOD=post ACTION="/webapp/hei0004s.nsf/852eb9d5847fa568492568c1003f29f1?CreateDocument" NAME="_calendar"><CENTER>
-->

<FORM NAME="calControl" onSubmit="return false;">

<INPUT NAME="dateField" VALUE="<%=request.getParameter("FieldName")%>" type=hidden> 
<TABLE CELLPADDING=0 CELLSPACING=0 BORDER=0>
<TR><TD COLSPAN=7>
<CENTER>
<SELECT NAME="month" onChange='selectDate()'>
   <OPTION>1월
   <OPTION>2월
   <OPTION>3월
   <OPTION>4월
   <OPTION>5월
   <OPTION>6월
   <OPTION>7월
   <OPTION>8월
   <OPTION>9월
   <OPTION>10월
   <OPTION>11월
   <OPTION>12월
</SELECT>

<SELECT NAME="year" onChange='selectDate()'>
   <OPTION>1990
   <OPTION>1991
   <OPTION>1992
   <OPTION>1993
   <OPTION>1994
   <OPTION>1995
   <OPTION>1996
   <OPTION>1997
   <OPTION>1998
   <OPTION>1999
   <OPTION>2000
   <OPTION>2001
   <OPTION>2002
   <OPTION>2003
   <OPTION>2004
   <OPTION>2005
   <OPTION>2006
   <OPTION>2007
   <OPTION>2008
   <OPTION>2009
   <OPTION>2010
   <OPTION>2011
   <OPTION>2012
   <OPTION>2013
   <OPTION>2014
   <OPTION>2015
   <OPTION>2016
   <OPTION>2017
   <OPTION>2018
   <OPTION>2019
   <OPTION>2020
</SELECT>년

</CENTER>
<br>
</TD>
</TR>
<TR>
<TD COLSPAN=7>
<CENTER>
<INPUT TYPE=BUTTON NAME="previousYear" VALUE="<<"    onClick="setPreviousYear()">
<INPUT TYPE=BUTTON NAME="previousYear" VALUE=" < "   onClick="setPreviousMonth()">
<INPUT TYPE=BUTTON NAME="previousYear" VALUE="오늘" onClick="setToday()">
<INPUT TYPE=BUTTON NAME="previousYear" VALUE=" > "   onClick="setNextMonth()">
<INPUT TYPE=BUTTON NAME="previousYear" VALUE=">>"    onClick="setNextYear()">
</CENTER>
</TD>
</TR>
</FORM>
<FORM NAME="calButtons">
<TR HEIGHT=10><TD></TD></TR>
<TR><TD><CENTER><FONT SIZE=-1 FACE="Arial,Helv,Helvetica"><B>일</B></FONT></CENTER></TD>
    <TD><CENTER><FONT SIZE=-1 FACE="Arial,Helv,Helvetica"><B>월</B></FONT></CENTER></TD>
    <TD><CENTER><FONT SIZE=-1 FACE="Arial,Helv,Helvetica"><B>화</B></FONT></CENTER></TD>
    <TD><CENTER><FONT SIZE=-1 FACE="Arial,Helv,Helvetica"><B>수</B></FONT></CENTER></TD>
    <TD><CENTER><FONT SIZE=-1 FACE="Arial,Helv,Helvetica"><B>목</B></FONT></CENTER></TD>
    <TD><CENTER><FONT SIZE=-1 FACE="Arial,Helv,Helvetica"><B>금</B></FONT></CENTER></TD>
    <TD><CENTER><FONT SIZE=-1 FACE="Arial,Helv,Helvetica"><B>토</B></FONT></CENTER></TD></TR>
<TR><TD><INPUT TYPE="button" NAME="but0"  value="    " onClick="returnDate(this.value)"  width = 100 height = 40 style="color:red"></TD>
    <TD><INPUT TYPE="button" NAME="but1"  value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but2"  value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but3"  value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but4"  value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but5"  value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but6"  value="    " onClick="returnDate(this.value)" width = 30 height = 30 style="color:blue"></TD></TR>
<TR><TD><INPUT TYPE="button" NAME="but7"  value="    " onClick="returnDate(this.value)" width = 50 height = 50 style="color:red"></TD>
    <TD><INPUT TYPE="button" NAME="but8"  value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but9"  value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but10" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but11" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but12" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but13" value="    " onClick="returnDate(this.value)" width = 30 height = 30 style="color:blue"></TD></TR>
<TR><TD><INPUT TYPE="button" NAME="but14" value="    " onClick="returnDate(this.value)" width = 50 height = 50 style="color:red"></TD>
    <TD><INPUT TYPE="button" NAME="but15" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but16" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but17" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but18" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but19" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but20" value="    " onClick="returnDate(this.value)" width = 30 height = 30 style="color:blue"></TD></TR>
<TR><TD><INPUT TYPE="button" NAME="but21" value="    " onClick="returnDate(this.value)" width = 50 height = 50 style="color:red"></TD>
    <TD><INPUT TYPE="button" NAME="but22" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but23" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but24" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but25" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but26" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but27" value="    " onClick="returnDate(this.value)" width = 30 height = 30 style="color:blue"></TD></TR>
<TR><TD><INPUT TYPE="button" NAME="but28" value="    " onClick="returnDate(this.value)" width = 30 height = 30 style="color:red"></TD>
    <TD><INPUT TYPE="button" NAME="but29" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but30" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but31" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but32" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but33" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but34" value="    " onClick="returnDate(this.value)" width = 30 height = 30 style="color:blue"></TD></TR>
<TR><TD><INPUT TYPE="button" NAME="but35" value="    " onClick="returnDate(this.value)" width = 30 height = 30 style="color:red"></TD>
    <TD><INPUT TYPE="button" NAME="but36" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but37" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but38" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but39" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but40" value="    " onClick="returnDate(this.value)" width = 30 height = 30 ></TD>
    <TD><INPUT TYPE="button" NAME="but41" value="    " onClick="returnDate(this.value)" width = 30 height = 30 style="color:blue"></TD></TR>
</TABLE>
</FORM>
<!--
<INPUT TYPE=submit VALUE="">-->
</FORM>
</BODY>
</HTML>
