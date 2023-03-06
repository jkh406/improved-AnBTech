<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "2주간 나의일정 보기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="javax.swing.*"
	import="com.anbtech.text.Hanguel"	
%>
<%@	page import="com.anbtech.date.anbDate"	%>
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
	String cal_td = "";				//인수로 넘어온 날자 (형식:yyyy/mm/dd)
	int Ryear=0;					//년도
	int Rmonth=0;					//월
	int Rday=0;						//일

	//화면출력용 해당되는 날자 (2주간)
	String Pd0="";					//시작일
	String Pd1="";					//1일자
	String Pd2="";					//2일자
	String Pd3="";					//3일자
	String Pd4="";					//4일자
	String Pd5="";					//5일자
	String Pd6="";					//6일자

	String Pd7="";					//7일자
	String Pd8="";					//8일자
	String Pd9="";					//9일자
	String Pd10="";					//10일자
	String Pd11="";					//11일자
	String Pd12="";					//12일자
	String Pd13="";					//13일자

	//화면출력용 해당되는 날자의 내용(2주간)
	String Pc0="";					//시작일
	String Pc1="";					//1일자
	String Pc2="";					//2일자
	String Pc3="";					//3일자
	String Pc4="";					//4일자
	String Pc5="";					//5일자
	String Pc6="";					//6일자

	String Pc7="";					//7일자
	String Pc8="";					//8일자
	String Pc9="";					//9일자
	String Pc10="";					//10일자
	String Pc11="";					//11일자
	String Pc12="";					//12일자
	String Pc13="";					//13일자

	//년월일 변수
	String Tyear="";				//년도
	String Tmonth="";				//월
	String Tday="";					//일

	//쿼리내용 화면에 보여주기
	String[][] rd;					//쿼리한 내용배열에 담기 
	int count = 0;					//쿼리한 갯수

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
	cal_id=cal_ot=cal_td="";
	Ryear=Rmonth=Rday=0;
	Tyear=Tmonth=Tday="";
	Pd0=Pd1=Pd2=Pd3=Pd4=Pd5=Pd6=Pd7=Pd8=Pd9=Pd10=Pd11=Pd12=Pd13="";
	Pc0=Pc1=Pc2=Pc3=Pc4=Pc5=Pc6=Pc7=Pc8=Pc9=Pc10=Pc11=Pc12=Pc13="";
	/*********************************************************************
	 	넘겨온 변수 읽기 (from Calendar_View.jsp)   Sabun=&Date=
	*********************************************************************/
	String Rsabun = request.getParameter("Sabun");			//넘겨받은 사번
	if(Rsabun != null) cal_id = Rsabun;

	//넘겨받은 날자(yyyy/mm/dd) from Calendar_View.jsp 또는 자체 Script(RefreshUrl)로 부터 넘겨받음
	//(해당주의 일요일을 기준날자로 넘어온다)
	String Rdate = request.getParameter("Date");			

	//화면에 날자를 출력하기 위해서 (14일간)
	if(Rdate != null) {
		cal_td = Rdate;						//yyyy/mm/dd
		
		Ryear = Integer.parseInt(Rdate.substring(0,4));
		Rmonth = Integer.parseInt(Rdate.substring(5,7));
		Rday = Integer.parseInt(Rdate.substring(8,10));
	
		//주어진 (Ryear/Rmonth,Rday)날자로 Setting후 주어진(pi)만큼 더한 날자 구하기
		Pd0= Rdate;
		Pd1 = bean.getDate(Ryear,Rmonth,Rday,1);	
		Pd2 = bean.getDate(Ryear,Rmonth,Rday,2);	
		Pd3 = bean.getDate(Ryear,Rmonth,Rday,3);
		Pd4 = bean.getDate(Ryear,Rmonth,Rday,4);
		Pd5 = bean.getDate(Ryear,Rmonth,Rday,5);
		Pd6 = bean.getDate(Ryear,Rmonth,Rday,6);		

		Pd7 = bean.getDate(Ryear,Rmonth,Rday,7);	
		Pd8 = bean.getDate(Ryear,Rmonth,Rday,8);	
		Pd9 = bean.getDate(Ryear,Rmonth,Rday,9);
		Pd10 = bean.getDate(Ryear,Rmonth,Rday,10);
		Pd11 = bean.getDate(Ryear,Rmonth,Rday,11);
		Pd12 = bean.getDate(Ryear,Rmonth,Rday,12);
		Pd13 = bean.getDate(Ryear,Rmonth,Rday,13);
	}

	/***********************************************************************
	3. 개인/회사/부서 등록정보 조회하기
	***********************************************************************/
	String SELyear = "";					//yyyy
	String SELmonth = "";					//MM
	String ADDyear = "";					//1개월 더한 년도(yyyy)
	String ADDmonth = "";					//1개월 더한 월 (MM)
	if(Rdate != null) {
		//해당 년,월
		SELyear = Rdate.substring(0,4);		//yyyy
		SELmonth = Rdate.substring(5,7);	//MM

		//13일을 더한 년,월(2주의 마지막 날자)
		String ADDM = anbdt.getDate(Ryear,Rmonth,Rday,13);
		ADDyear = ADDM.substring(0,4);
		ADDmonth = ADDM.substring(5,7);
	}

	String cal_dl = "";	//일정목록 clear(회사,부서,개인)
	//1. 회사 일정목록 읽기 (기준:0)
	cal_dl = view.Other_Print("0",SELyear,SELmonth,"COM");
	if(!SELmonth.equals(ADDmonth)) 
		cal_dl += view.Other_Print("0",ADDyear,ADDmonth,"COM");

	//2. 부서 일정목록 읽기 (기준:부서관리코드)
	cal_dl += view.Other_Print(cal_di,SELyear,SELmonth,"DIV");
	if(!SELmonth.equals(ADDmonth))
		cal_dl += view.Other_Print("0",ADDyear,ADDmonth,"DIV");

	//3. 개인 일정목록 읽기 (기준 : 사번)
	if(cal_id.equals(id)) {			//전부를 출력한다. (login자신의것)
		cal_dl += view.Owner_Print(cal_id,SELyear,SELmonth);
		if(!SELmonth.equals(ADDmonth))
			cal_dl += view.Owner_Print(cal_id,ADDyear,ADDmonth);
	} else {						//공개된 내용만 출력한다. (공유자것)
		cal_dl += view.Other_Print(cal_id,SELyear,SELmonth,"INI");
		if(!SELmonth.equals(ADDmonth))
			cal_dl += view.Other_Print(cal_id,ADDyear,ADDmonth,"INI");
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

	//화면에 출력할 내용을 변수에 담기
	for(int di=0; di<count; di++){
		if(Pd0.equals(rd[di][0]))  Pc0 += "<A Href=Calendar_Modify.jsp?PID="+rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd1.equals(rd[di][0]))  Pc1 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd2.equals(rd[di][0]))  Pc2 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd3.equals(rd[di][0]))  Pc3 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd4.equals(rd[di][0]))  Pc4 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd5.equals(rd[di][0]))  Pc5 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd6.equals(rd[di][0]))  Pc6 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";

		if(Pd7.equals(rd[di][0]))  Pc7 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd8.equals(rd[di][0]))  Pc8 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd9.equals(rd[di][0]))  Pc9 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd10.equals(rd[di][0]))  Pc10 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd11.equals(rd[di][0]))  Pc11 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd12.equals(rd[di][0]))  Pc12 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
		if(Pd13.equals(rd[di][0]))  Pc13 += "<A Href=Calendar_Modify.jsp?PID=" + rd[di][5] + "&opendocument&view=hdForm3 onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+rd[di][3]+"("+rd[di][4]+")&nbsp;&nbsp;"+rd[di][1]+"</font>" + "</A><br>";
	}


	/***********************************************************************
	//서버의 시스템 날자읽기
	***********************************************************************/
	cal_ot = anbdt.getDate();				//오늘날자 (형식:yyyy-MM-dd)
	Tyear = anbdt.getYear();				//년도
	Tmonth = anbdt.getMonth();				//월
	Tday = anbdt.getDates();				//일

	/***********************************************************************
	// 화면출력용 년도 초기회하기
	***********************************************************************/
	//금년도 구하기 (항상 금년도가 기준임)
	int Cyear = Integer.parseInt(anbdt.getYear());					

	//년도 화면 Display하기 (넘겨받은년도 기준 3년전부터 10년까지)
	int Syear = Cyear - 3; 

%>
<HTML>
<HEAD>

<SCRIPT LANGUAGE="JavaScript">
<!-- 
function DoEvent() {
	var yy = '<%=Syear%>';
	var tmp=document.URL.split("&Date=")
	var tmp1=tmp[1].split("&") 
	var tmp2=tmp1[0].split("/")
	document.forms[0].hdYear.selectedIndex=tmp2[0]-yy	
	RefreshWeek()  //주차리스트
	var n=0
	while(1) {
		str=document.forms[0].hdWeekNo.options[n].text
		str1=str.split("(")
		strDate=str1[1].substring(0, 10)
		if(strDate==tmp1[0]) {
			document.forms[0].hdWeekNo.selectedIndex=n
			break;
		}
		n++
	}
}

function RefreshUrl(opt) {
	var form=document.forms[0]
	var tmp=document.URL.split("&Date=")
	if(opt==0) {   //콤보에서 선택
		str=form.hdWeekNo.options[form.hdWeekNo.selectedIndex].text
		str1=str.split("(")
		strDate=str1[1].substring(0, 10)
		var strUrl=tmp[0] + "&Date=" + strDate + "&blank"
		location.href=strUrl
		return
	}
	
	if(opt==2) {  //금주  
		var fromField = document.forms[0].hdCurrentDate;
    		var syear =  fromField.value.substring(0, 4); //현재 년도
    		var smonth = fromField.value.substring(5, 7) ; //현재 달
    		var sday = fromField.value.substring(8, 10); //오늘 날짜
    
		toDay = new Date();
		toDay.setTime(Date.UTC(syear, smonth-1, sday))
		wn=toDay.getDay()
		tDate.setTime(Date.UTC(toDay.getYear(), toDay.getMonth(), toDay.getDate()-wn))

	}	
	if(opt==1) {  //이전  
		tmpY=document.forms[0].hdSdate.value.substring(0,4)
		tmpM=document.forms[0].hdSdate.value.substring(5,7)
		tmpD=document.forms[0].hdSdate.value.substring(8,10)
		toDay = new Date();
		toDay.setTime(Date.UTC(tmpY, tmpM-1, tmpD))
		wn=toDay.getDay()+7
		tDate.setTime(Date.UTC(toDay.getYear(), toDay.getMonth(), toDay.getDate()-wn))
	}	
	if(opt==3) {  //다음  
		tmpY=document.forms[0].hdSdate.value.substring(0,4)
		tmpM=document.forms[0].hdSdate.value.substring(5,7)
		tmpD=document.forms[0].hdSdate.value.substring(8,10)
		toDay = new Date();
		toDay.setTime(Date.UTC(tmpY, tmpM-1, tmpD))
		wn=toDay.getDay()
		wn=7-wn 
		tDate.setTime(Date.UTC(toDay.getYear(), toDay.getMonth(), toDay.getDate()+wn))
	}		
	if(tDate.getYear()<2000) {return}
	smon=tDate.getMonth()+1
	if(smon<10) {smon="0"+smon}
	sdate=tDate.getDate()
	if(sdate<10) {sdate="0"+sdate}
	strDate=tDate.getYear() + "/" + smon + "/" + sdate
	var strUrl=tmp[0] + "&Date=" + strDate + "&blank"
	location.href=strUrl
}

function RefreshWeek() {
	var form=document.forms[0]
	tyear=form.hdYear.options[form.hdYear.selectedIndex].text
	var wk = 0
	tDate = new Date();
	tDate.setTime(Date.UTC(tyear, 0, 1))              //전연도에 걸친경우
	wn=tDate.getDay()
	if(tyear>2000) {
		if(wn != 0) {
			tDate.setTime(Date.UTC(tyear, 0, -(wn-1)))
			strSdate=stringDate(tDate)
			wk++
			option=new Option(wk+" 주차 (" + strSdate + ")", wk)
			form.hdWeekNo.options[wk-1]=option
		}
	}	

	for (var n=1; n<367; n++) {                           //해당연도
		tDate.setTime(Date.UTC(tyear, 0, n))
		if(tDate.getDay()==0) { 
			if(tDate.getYear() > tyear) {continue}
			strSdate=stringDate(tDate)
			wk++
			option=new Option(wk+" 주차 (" + strSdate + ")", wk)
			form.hdWeekNo.options[wk-1]=option
		}	
	}
}

function stringDate(tDate) {
	qYear=tDate.getYear()
	if(qYear<2000) {qYear="19"+qYear}
	qMonth=tDate.getMonth()+1
	if(qMonth < 10) {qMonth="0"+qMonth}
	qDate=tDate.getDate()
	if(qDate < 10) {qDate="0"+qDate}
		
	tDate2 = new Date();
	tDate2.setTime(Date.UTC(tDate.getYear(), tDate.getMonth(), tDate.getDate()+6))
	qYear2=tDate2.getYear()
	if(qYear2<2000) {qYear2="19"+qYear2}
	qMonth2=tDate2.getMonth()+1
	if(qMonth2 < 10) {qMonth2="0"+qMonth2}
	qDate2=tDate2.getDate()
	if(qDate2 < 10) {qDate2="0"+qDate2}
	
	return qYear+"/" + qMonth + "/" + qDate + " ~ " + qYear2 + "/" + qMonth2 + "/" + qDate2
}
// -->
</SCRIPT>
<link rel='stylesheet' type='text/css' href='../css/style.css'>
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad="DoEvent();">

<FORM METHOD=post ACTION="" NAME="_hdForm4" style="margin:0">
<INPUT NAME="hdSabun" VALUE="<%=cal_id%>" type="hidden"><Input Type="Hidden" Name="hdCurrentDate" value="<%=cal_ot%>">
<INPUT NAME="hdSdate" VALUE="<%=cal_td%>" ReadOnly size=10 type="hidden">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 2주간일정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="Calendar_View.jsp?Sabun=<%=cal_id%>" onMouseOver="window.status='월간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_m.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View1.jsp?Sabun=<%=cal_id%>&Date=<%=cal_td%>" onMouseOver="window.status='주간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_w.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View2.jsp?Sabun=<%=cal_id%>&Date=<%=cal_td%>" onMouseOver="window.status='2주간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_2w_o.gif" border="0"></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_Print.jsp?Sabun=<%=cal_id%>&Date=<%=cal_td%>" onMouseOver="window.status='인쇄폼 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_p.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=400 style="padding-left:10px" valign='middle'>
				<A href="JavaScript:RefreshUrl('1')"><img src="../images/arrow_back.gif" border="0" align="absmiddle" alt="이전주"></A>
				<A href="JavaScript:RefreshUrl('2')"><img src="../images/this_week.gif" border="0" align="absmiddle" alt="금주"></A>
				<A href="JavaScript:RefreshUrl('3')"><img src="../images/arrow_next.gif" border="0" align="absmiddle" alt="다음주"></A>
				<SELECT NAME="hdYear" onChange="RefreshWeek()">
				<%	
					String SEL = "";
					for(int hy=0; hy < 10; hy++) {
						if(Cyear == Syear) SEL = "SELECTED";
						else SEL = "";
						out.println("<OPTION " + SEL + ">" + Syear); 
						Syear++;
					}
					out.println("</SELECT>");
				%>
				<SELECT NAME="hdWeekNo" onChange="RefreshUrl(0)"></SELECT>				  
			  </TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!-- 내용 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TR><!--일-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=15% align=middle class='calendar_title'><%=Pd0%> <IMG src="../images/sunday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc0%></DIV></TD>
			  <TD width=15% align=middle class='calendar_title'><%=Pd7%> <IMG src="../images/sunday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc7%></DIV></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR><!--월-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=15% align=middle class='calendar_title'><%=Pd1%> <IMG src="../images/monday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc1%></DIV></TD>
			  <TD width=15% align=middle class='calendar_title'><%=Pd8%> <IMG src="../images/monday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc8%></DIV></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR><!--화-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=15% align=middle class='calendar_title'><%=Pd2%> <IMG src="../images/tuesday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc2%></DIV></TD>
			  <TD width=15% align=middle class='calendar_title'><%=Pd9%> <IMG src="../images/tuesday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc9%></DIV></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR><!--수-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=15% align=middle class='calendar_title'><%=Pd3%> <IMG src="../images/wednesday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc3%></DIV></TD>
			  <TD width=15% align=middle class='calendar_title'><%=Pd10%> <IMG src="../images/wednesday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc10%></DIV></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR><!--목-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=15% align=middle class='calendar_title'><%=Pd4%> <IMG src="../images/thursday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc4%></DIV></TD>
			  <TD width=15% align=middle class='calendar_title'><%=Pd11%> <IMG src="../images/thursday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc11%></DIV></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR><!--금-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=15% align=middle class='calendar_title'><%=Pd5%> <IMG src="../images/friday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc5%></DIV></TD>
			  <TD width=15% align=middle class='calendar_title'><%=Pd12%> <IMG src="../images/friday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc12%></DIV></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR><!--토-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=15% align=middle class='calendar_title'><%=Pd6%> <IMG src="../images/saturday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc6%></DIV></TD>
			  <TD width=15% align=middle class='calendar_title'><%=Pd13%> <IMG src="../images/saturday_1.gif" align="absmiddle"></TD>
			  <TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc13%></DIV></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR></TD></TR></TABLE>


</FORM>
</BODY>
</HTML>