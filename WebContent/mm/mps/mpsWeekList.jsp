<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "1주간 생산계획일정 보기"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"	
	import="com.anbtech.mm.entity.*"
%>

<%
	
	//화면출력용 해당되는 날자 (1주간)
	String Pd0="";					//시작일
	String Pd1="";					//1일자
	String Pd2="";					//2일자
	String Pd3="";					//3일자
	String Pd4="";					//4일자
	String Pd5="";					//5일자
	String Pd6="";					//6일자
	String Pd7="";					//7일자

	//화면출력용 해당되는 날자의 내용(1주간)
	String Pc0="";					//시작일
	String Pc1="";					//1일자
	String Pc2="";					//2일자
	String Pc3="";					//3일자
	String Pc4="";					//4일자
	String Pc5="";					//5일자
	String Pc6="";					//6일자
	String Pc7="";					//7일자

	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();				//Date에 관련된 연산자
	
	/*********************************************************************
	 	파라미터 전달받기
	*********************************************************************/
	//내용받기
	com.anbtech.mm.entity.mpsMasterTable table;
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("MASTER_List");
	table = new mpsMasterTable();
	Iterator table_iter = table_list.iterator();

	String view_td = (String)request.getAttribute("view_td");			//일자(yyyy/mm/dd)
	String year = view_td.substring(0,4);
	String month = view_td.substring(5,7);

	String factory_no = (String)request.getAttribute("factory_no");		//공장번호
	String mode = (String)request.getAttribute("mode");
	String Rdate = (String)request.getAttribute("view_td");				//일자(yyyy/mm/dd)
	String cal_td = "";
	int Ryear=0;					//년도
	int Rmonth=0;					//월
	int Rday=0;						//일

	//화면에 날자를 출력하기 위해서 (7일간)
	if(Rdate != null) {
		cal_td = Rdate;						//yyyy/mm/dd
		
		Ryear = Integer.parseInt(Rdate.substring(0,4));
		Rmonth = Integer.parseInt(Rdate.substring(5,7));
		Rday = Integer.parseInt(Rdate.substring(8,10));

		//주어진 (Ryear/Rmonth,Rday)날자로 Setting후 주어진(pi)만큼 더한 날자 구하기
		Pd0= Rdate;
		Pd1 = anbdt.getDate(Ryear,Rmonth,Rday,1);	
		Pd2 = anbdt.getDate(Ryear,Rmonth,Rday,2);	
		Pd3 = anbdt.getDate(Ryear,Rmonth,Rday,3);
		Pd4 = anbdt.getDate(Ryear,Rmonth,Rday,4);
		Pd5 = anbdt.getDate(Ryear,Rmonth,Rday,5);
		Pd6 = anbdt.getDate(Ryear,Rmonth,Rday,6);	
	}


	//화면에 출력할 내용을 변수에 담기
	while(table_iter.hasNext()) {
		table = (mpsMasterTable)table_iter.next();
		String pde = table.getPlanDate();
		String pid = table.getPid();
		String fcd = table.getFgCode();
		String pct = Integer.toString(table.getPlanCount());
		String cnt = table.getModelCode()+","+table.getModelName();

		if(Pd0.equals(pde))  Pc0 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd1.equals(pde))  Pc1 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd2.equals(pde))  Pc2 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd3.equals(pde))  Pc3 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd4.equals(pde))  Pc4 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd5.equals(pde))  Pc5 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd6.equals(pde))  Pc6 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='일정 상세보기';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";

	}

	/***********************************************************************
	//서버의 시스템 날자읽기
	***********************************************************************/
	String cal_ot = anbdt.getDate();				//오늘날자 (형식:yyyy-MM-dd)
	
	/***********************************************************************
	// 화면출력용 년도 초기회하기
	***********************************************************************/
	//금년도 구하기 (항상 금년도가 기준임)
	int Cyear = Integer.parseInt(anbdt.getYear());					

	//년도 화면 Display하기 (넘겨받은년도 기준 3년전부터 10년까지)
	int Syear = Cyear - 3; 

%>

<HTML>
<HEAD><TITLE>1주간 생산계획일정</TITLE>
<link rel='stylesheet' type='text/css' href='../css/style.css'>
</HEAD>
<SCRIPT LANGUAGE="JavaScript">
<!-- 
//road시 주차 리스트
function DoEvent() {
	var yy = '<%=Syear%>'; 
	var tmp= '<%=Rdate%>';
	var tmp1= tmp;
	var tmp2=tmp.split("/");
	document.forms[0].hdYear.selectedIndex=tmp2[0]-yy	

	RefreshWeek()  //주차리스트
	
	var n=0
	while(1) {
		str=document.forms[0].hdWeekNo.options[n].text; 
		str1=str.split("(")
		strDate=str1[1].substring(0,10) 
		if(strDate==tmp1) {
			document.forms[0].hdWeekNo.selectedIndex=n
			break;
		}
		n++
	}
}

// '이전 - 금주 - 다음' 리스트 출력하기
function RefreshUrl(opt) {
	var form=document.forms[0]
	var view_td = '<%=Rdate%>';
	var factory_no = '<%=factory_no%>';
	var mode = '<%=mode%>';

	if(opt==0) {   //콤보에서 선택
		str=form.hdWeekNo.options[form.hdWeekNo.selectedIndex].text
		str1=str.split("(")
		strDate=str1[1].substring(0,10)
		var strUrl="../servlet/mpsInfoServlet?mode="+mode+"&view_td="+strDate+"&factory_no="+factory_no;
		location.href=strUrl
		return  //종료
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
		//alert(tmpY,tmpM,tmpD)
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
	if(tDate.getYear()<2000) {return;}
	smon=tDate.getMonth()+1; 	if(smon<10) {smon="0"+smon;}
	sdate=tDate.getDate();		if(sdate<10) {sdate="0"+sdate;}
	strDate=tDate.getYear() + "/" + smon + "/" + sdate;
	var strUrl="../servlet/mpsInfoServlet?mode="+mode+"&view_td="+strDate+"&factory_no="+factory_no;
	location.href=strUrl;
}
//주차리스트 실제 출력하기
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
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad="DoEvent();">

<FORM METHOD=post ACTION="" NAME="_hdForm3" style="margin:0">
<INPUT Type="Hidden" Name="hdCurrentDate" value="<%=cal_ot%>">
<INPUT NAME="hdSdate" VALUE="<%=cal_td%>" ReadOnly size=10 type="hidden">


<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=factory_no%> 공장 주간 생산계획일정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=cal_month&year=<%=year%>&month=<%=month%>&factory_no=<%=factory_no%>" onMouseOver="window.status='월간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_m.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_week1&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='주간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_w_o.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_week2&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='2주간 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_2w.gif" border="0"></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_month&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='인쇄폼 일정 보기';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_p.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=400 style="padding-left:10px" valign='middle'>
				<A href="JavaScript:RefreshUrl('1')"><img src="../mm/images/arrow_back.gif" border="0" align="absmiddle" alt="이전주"></A>
				<A href="JavaScript:RefreshUrl('2')"><img src="../mm/images/this_week.gif" border="0" align="absmiddle" alt="금주"></A>
				<A href="JavaScript:RefreshUrl('3')"><img src="../mm/images/arrow_next.gif" border="0" align="absmiddle" alt="다음주"></A>
				<SELECT NAME="hdYear" onChange="RefreshWeek()">
				<%	
					String SEL = "";
					for(int hy=0; hy < 10; hy++) {
						if(Cyear == Syear) SEL = "SELECTED";
						else SEL = "";
						out.println("<OPTION " + SEL + ">" + Syear + "</OPTION>"); 
						Syear++;
					}
					out.println("</SELECT>");
				%>
				<SELECT NAME="hdWeekNo" onChange="RefreshUrl(0)"></SELECT>				  
			  </TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!-- 내용 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TR><!--일월-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=50% align=middle class='calendar_title'><%=Pd0%> <IMG src="../mm/images/sunday_1.gif" align="absmiddle"></TD>
			  <TD width=50% align=middle class='calendar_title'><%=Pd1%> <IMG src="../mm/images/monday_1.gif" align="absmiddle"></TD>
		   </TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR>
    <TD vAlign=top>
	  <TABLE cellspacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=50% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:85px; width:100%;overflow:auto;"><%=Pc0%></DIV></TD>
			  <TD width=50% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:85px; width:100%;overflow:auto;"><%=Pc1%></DIV></TD>
		   </TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR><!--화수-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=50% align=middle class='calendar_title'><%=Pd2%> <IMG src="../mm/images/tuesday_1.gif" align="absmiddle"></TD>
			  <TD width=50% align=middle class='calendar_title'><%=Pd3%> <IMG src="../mm/images/wednesday_1.gif" align="absmiddle"></TD>
		   </TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR>
    <TD vAlign=top>
	  <TABLE cellspacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=50% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:85px; width:100%;overflow:auto;"><%=Pc2%></DIV></TD>
			  <TD width=50% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:85px; width:100%;overflow:auto;"><%=Pc3%></DIV></TD>
		   </TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR><!--목금-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=50% align=middle class='calendar_title'><%=Pd4%> <IMG src="../mm/images/thursday_1.gif" align="absmiddle"></TD>
			  <TD width=50% align=middle class='calendar_title'><%=Pd5%> <IMG src="../mm/images/friday_1.gif" align="absmiddle"></TD>
		   </TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR>
    <TD vAlign=top>
	  <TABLE cellspacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=50% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:85px; width:100%;overflow:auto;"><%=Pc4%></DIV></TD>
			  <TD width=50% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:85px; width:100%;overflow:auto;"><%=Pc5%></DIV></TD>
		   </TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR><!--토-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=50% align=middle class='calendar_title'><%=Pd6%> <IMG src="../mm/images/saturday_1.gif" align="absmiddle"></TD>
			  <TD width=50% align=middle class='calendar_title'></TD>
		   </TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
  <TR>
    <TD vAlign=top>
	  <TABLE cellspacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD width=50% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:85px; width:100%;overflow:auto;"><%=Pc6%></DIV></TD>
			  <TD width=50% bgcolor="#F5F5F5" style="padding-left:5px"></TD>
		   </TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#CCCCCC'></TD></TR></TABLE>
</BODY>
</FORM>

</HTML>
