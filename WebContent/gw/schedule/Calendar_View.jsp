<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�������� ����(����)"		
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
	//login ���� ����
	String id="";					//login

	//�޽��� ���޺���
	String Message="";				//�޽��� ���� ����  

	//���� ���� ����
	String cal_id = "";				//����� ���
	String cal_di = "";				//����� �μ����� �ڵ�
	String cal_ot = "";				//���ó���(����:yyyy/mm/dd)
	String cal_td = "";				//�μ��� �Ѿ�� ���� (����:yyyy/mm/dd)
	String cal_dl = "";				//�ش�� ���� ���
	String cal_hl = "";				//�ش�� ������ ���

	//������ ��,�� ����
	String selYear = "";			//������ �⵵
	String selMonth = "";			//������ ��

	//����,������ ����
	String PYear = "";				//�����⵵
	String PMonth = "";				//������
	String NYear = "";				//�����⵵
	String NMonth = "";				//������
	String PNDay = "";				//��

	//view(�ְ�,2�ְ�)�� �Ѱ��� ����(������ ���ָ� �������� �Ѱ��ش�)
	String view_td="";				//�Ѱ��� ���� (����:yyyy/mm/dd)

	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	Calendar_View view = new com.anbtech.gw.business.Calendar_View();			//�ش�� ������ �������� ������

	/*********************************************************************
	 	����� login �˾ƺ���
	*********************************************************************/
	id = login_id; 			//������

	//�μ��� �Ѿ�� ����б�
	cal_id = request.getParameter("Sabun");				//������ id
	if(cal_id == null) cal_id = id;						//�Ѿ�� �μ������� login id

	//�μ� �����ڵ� ã��
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

	//�μ� �ʱ�ȭ
	cal_ot = cal_td = cal_dl = cal_hl = "";
	PYear=PMonth=NYear=NMonth=PNDay="";

	/*********************************************************************
	 	���� ���� �����ֱ�
	*********************************************************************/	
	//���� ���� ���ϱ� (���� �޷��� �����.)
	cal_ot = anbdt.getDate();							//���ó���(����:yyyy-mm-dd)

	//�μ��� �Ѿ�� ��,��,�� �ޱ�
	String year = request.getParameter("year");
	if(year == null) year = anbdt.getYear();			//ó��open�� �ݳ⵵��������
	String month = request.getParameter("month");
	if(month == null) month = anbdt.getMonth();			//ó��open�� �ݿ���������
	String day = request.getParameter("day");
	if(day == null) day = anbdt.getDates();				//ó��open�� ���ϰ�������

	cal_td = year + "/" + month + "/" + day;			//�Ѱܹ��� ����� ����

	/*----------------------------------------
	//���ָ� �������� view(�ְ�,2�ְ�)���� �Ѱ��� ���������
	// (�ش����� �Ͽ��� ���ڸ� �Ѱ��ش�)
	-----------------------------------------*/
	int iyear = Integer.parseInt(anbdt.getYear());			//�ݳ�
	int imonth = Integer.parseInt(anbdt.getMonth());		//�ݿ�
	int iday = Integer.parseInt(anbdt.getDates());			//����
	int tmp_td = anbdt.getDay(iyear,imonth,iday);			//���ÿ��� (1:�� 2:�� ~ 7:��)

	//�־��� ���ڿ��� ������ �̿��Ͽ� ������ �Ͽ��� ���ڸ� ���Ѵ�.
	//ã���� �ϴ� ������ �Ͽ��� ���ڷ� setting�� �ְ������ �Ѱ��ش�.
	int sunday = iday-tmp_td+1;
	view_td = anbdt.setDate(iyear,imonth,sunday);	
	
	/*---------------------------------------------
	//���������� �����ϱ�
	----------------------------------------------*/
	cal_dl = "";	//������� clear(ȸ��,�μ�,����)
	//1. ȸ�� ������� �б� (����:0)
	cal_dl = view.Other_View("0",year,month,"COM");

	//2. �μ� ������� �б� (����:�μ������ڵ�)
	cal_dl += view.Other_View(cal_di,year,month,"DIV");

	//3. ���� ������� �б� (���� : ���)
	if(cal_id.equals(id)) {		//���θ� ����Ѵ�. (login�ڽ��ǰ�)
		cal_dl += view.Owner_View(cal_id,year,month);
	} else {						//������ ���븸 ����Ѵ�. (�����ڰ�)
		cal_dl += view.Other_View(cal_id,year,month,"INI");
	}

	/*---------------------------------------------
	//���ϸ�� (����� �о� �����Ѵ�.)
	----------------------------------------------*/
	String[] commonColumns = {"id","item","hdyear","hdmon","nlist"};
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(commonColumns);
	bean.setOrder("nlist DESC");	
	bean.setClear();	

	//���� ���� (where��)
	String holidy_data = "where (item='LHD' and hdmon='" + month + "')";
		holidy_data += " or (item='BHD' and hdyear='" + year + "' and hdmon='" + month + "')";
		holidy_data += " or (item='EHD' and id='" + cal_id + "' and hdyear='" + year + "' and hdmon='" + month + "')"; 

	bean.setSearchWrite(holidy_data);
	bean.init_write();
	
	while(bean.isAll()) {
		cal_hl += bean.getData("nlist")+";";
	}	

	/*********************************************************************
	 	������ �� �� ���ϱ�
	*********************************************************************/	
	//������ �⵵
	selYear = request.getParameter("year");							//���õ� �⵵
	if(selYear == null) selYear = anbdt.getYear();					//�ʱ�⵵�� �ش�⵵

	//������ ��
	selMonth = request.getParameter("month");						//���õ� ��
	if(selMonth == null) selMonth = anbdt.getMonth();				//�ʱ���� �ش��	
	if(selMonth.length() == 1) selMonth = "0" + selMonth;

	/*********************************************************************
	 	����,������ ��,��,�� ���ϱ�
	*********************************************************************/	
	int toYear = Integer.parseInt(year);							//�⵵

	String toMon = month;
	if(toMon.substring(0,1).equals("0")) toMon = toMon.substring(1,2);
	int toMonth = Integer.parseInt(toMon);							//��
	if((1 < toMonth) && (toMonth < 12)) { 							//2�� ~ 11������
		PYear = Integer.toString(toYear);							//�����⵵ (���ϳ⵵)
		PMonth = Integer.toString(toMonth - 1);						//������
		if(PMonth.length() == 1) PMonth = "0" + PMonth;

		NYear = Integer.toString(toYear);							//�����⵵ (���ϳ⵵)
		NMonth = Integer.toString(toMonth + 1);						//������
		if(NMonth.length() == 1) NMonth = "0" + NMonth;
	} else if(1 == toMonth){										//1��
		PYear = Integer.toString(toYear - 1);						//�����⵵ 
		PMonth = "12";												//������

		NYear = Integer.toString(toYear);							//�����⵵ (���ϳ⵵)
		NMonth = Integer.toString(toMonth + 1);						//������	
		if(NMonth.length() == 1) NMonth = "0" + NMonth;	
	} else if(12 == toMonth){										//12��
		PYear = Integer.toString(toYear);							//�����⵵ (���ϳ⵵)
		PMonth = "11";												//������

		NYear = Integer.toString(toYear + 1);						//�����⵵ 
		NMonth = "01";												//������		
	} 
	PNDay = anbdt.getDates();										//������
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
    //������ �ð��� �о�´�.
    var fromField = document.hidform.hdToday;
    var syear =  fromField.value.substring(0, 4);	//���� �⵵
    var smonth = fromField.value.substring(5, 7) ;	//���� ��
    var sday = fromField.value.substring(8, 10);	//���� ��¥

    //Client�ð��� setting�Ѵ�.
    now = new Date();
    now.setTime(Date.UTC(syear, smonth-1, 1))

    var day   = now.getDate();
    var month = now.getMonth();
    var year  = now.getYear();
    if(year<2000){year = year + 1900}
    year = year - 1990;  

    displayCalendar(day, month, year);  //�޷±׸���
}

function displayCalendar(day, month, year) {
    day = parseInt(day);
    month = parseInt(month);
    year = parseInt(year);
    year = year + 1990;
    
    var i = 0;
    var days = getDaysInMonth(month+1,year);  		//�� ���������� ���
    var firstOfMonth = new Date (year, month, 1);	//��, ��, 1�� �� ��ü����
    var startingPos  = firstOfMonth.getDay();		//1������ ������ ���Ѵ�.(0:��,1:��,2:ȭ ...)

    days += startingPos;

    // ������ ���� ��¥�� �������� ó�� 
    for (i = 0; i < startingPos; i++) {
         document.write(displayTable(i, "<BR>", ""));
    }

    // �޷±���
    var tmp1, tmp2;
    var content = "";
    for (i = startingPos; i < days; i++)  
    {
        if (i-startingPos+1 < 10) {
	           tmp1 = i-startingPos+1;
     	       tmp2 = "0" + tmp1;
     	       content = dispcontent(parseInt(year,10), parseInt(month+1,10), parseInt(tmp2,10)); //��/��/��
               document.write(displayTable(i, tmp2, content));	//���ó��� ȭ�鿡 ���
        } else {
               content = dispcontent(parseInt(year,10), parseInt(month+1,10), parseInt(i-startingPos+1,10));
               document.write(displayTable(i, i-startingPos+1,content));
        }        
    }
    // ���� ���� ������ ��¥������ ��¥�� �������� ó��
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

//������ �ִ� ���� ��ϵ� �������� ������ ����(�������� LINK�ϱ�)
function dispcontent(tmpYear, tmpMonth, tmpDay){
     var tmpdoclist = "";
     var tmpString = "";
     var tmpImgsrc = "";
     if (document.hidform.hdDocList.value != "") {
    	  var doclist = document.hidform.hdDocList.value.split(";")
	      for (var v=0; v < doclist.length-1; v++) {
    		    tmpdoclist = doclist[v];  //�ش���� �������
				
     		    var doclist_Data = tmpdoclist.split("*");
     		    var doclist_Date = doclist_Data[0];				//"��/��/��"
                var doclist_Date1 = doclist_Date.split("/");    //��¥ ��������(2000/03/05)
               
                var doclist_Year = doclist_Date1[0];   			//�⵵
                var doclist_Month = doclist_Date1[1]; 			//��
                var doclist_Day = doclist_Date1[2]; 			//��
               
                var tmpLink = doclist_Data[1];
                var tmpLinkItem = tmpLink.split("@");			//������ ���� ����  ex> 8A2329406E,Calendar_View,Y,�ٹ���(00:00~00:00),pid
                //document.write(tmpLinkItem);
                if (parseInt(doclist_Year,10) == tmpYear) {
               		if (parseInt(doclist_Month,10) == tmpMonth) {
               			if (parseInt(doclist_Day,10) == tmpDay) {
    				       if (tmpLinkItem[2] == "Y") {			//img �����ֱ� (����:Y  �̰���:N)
    					     	tmpImgsrc = "";
    				       } else {	
							    var sc = '../images';
								tmpImgsrc = "<img src=\""+sc+"/secret.gif\" border=\"0\">";
    				       }
				           //�� �������� Link ���� ���ϰ� 	     
							tmpString = tmpString + "<a href=\"Calendar_Modify.jsp?PID=" + tmpLinkItem[4] + "&opendocument&view=" + tmpLinkItem[1] + "\" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">" + tmpImgsrc + tmpLinkItem[3] +"</a><BR>";		     
						}
    			    }
    		    }  

    	  } //end for
    	  return tmpString;
     } else {
    	  return "";
     } 
}

//�޷±����� ���� HTML TAG ����
function displayTable(e, dayno, content) {
     var toDay = new Date()
     var month=toDay.getMonth()+1
     if(month<10) {month="0"+month}

     var tmpmon = document.goform.hdStartMonth;		//������ ���� �а�
     var tmpRt = "";					//���ϰ� (td����)
     var bgcolor = "";
     var startTR = "<TR>";
     var endTR = "</TR>";
    	
     currentday = document.hidform.hdToday.value.substring(8, 10); //���� ��¥
     cal_month =  tmpmon.options[document.goform.hdStartMonth.selectedIndex].text; //���� ȭ���� ��
               
     if (dayno == "<BR>") { //��¥���� ���� ���� ��� ����
    	bgcolor = "#F5F5F5";
     } else if (dayno == currentday && month == cal_month) { //���ó�¥ ����
		bgcolor = "#FFFFFF";
     } else if ((e % 7) == 0) { //�Ͽ����� ��� ����
    	bgcolor = "#DFEDFD";
     } else if ((e % 7) == 6) { //������� ��� ����
    	bgcolor = "#EFF0F5";
     } else { //������ ��� ����
    	bgcolor = "#FFFFFF";
     }

     var chkDay = CheckHoliday(dayno);
     var htitle = ""
     strDiv="</font><DIV ID=\"View1\" STYLE=\"position:static; visibility:visible; left:0; height:65; width:100%;overflow:auto;\">"   
      	
     if ((e % 7) == 0) { //�Ͽ���
     	if(chkDay!=null) htitle=" <font size=2 color=red>"+ chkDay + "</font><br>"
			tmpRt = startTR + "<TD width=112 height=65 bgcolor=" + bgcolor + " valign=top>" + "<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>�� <%=month%>�� " + dayno + "�Ͽ� �� ���� ���';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=red><b>" + dayno + "</b></font></a>" + htitle + strDiv + content +"</div></TD>";
     } else if ((e % 7) == 6) { //�����
		fclass="<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>�� <%=month%>�� " + dayno + "�Ͽ� �� ���� ���';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=#565656><b>" + dayno + "</b></font></a>"
     	if(chkDay!=null) {
		fclass="<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>�� <%=month%>�� " + dayno + "�Ͽ� �� ���� ���';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=#565656><b>" + dayno + "</b></font></a>"
		htitle =" <font size=2 color=red>"+ chkDay + "</font><br>"
	}	
   		tmpRt = "<TD width=112 height=65 bgcolor=" + bgcolor + " valign=top>"+fclass +  htitle+strDiv + content + "</div></TD>"+endTR;
     } else { //����
		fclass="<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>�� <%=month%>�� " + dayno + "�Ͽ� �� ���� ���';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font color=#565656><b>" + dayno + "</b></font></a>"
     	if(chkDay!=null) {
			fclass="<a href=\'Calendar_WriteP.jsp?Sabun=<%=id%>&DAY=<%=year%>/<%=month%>/" + dayno + "\' onMouseOver=\"window.status='<%=year%>�� <%=month%>�� " + dayno + "�Ͽ� �� ���� ���';return true;\" onMouseOut=\"window.status='';return true;\">" + "<font  color=black><b>" + dayno + "</b></font></a>"
			htitle =" <font size=2 color=red>"+ chkDay + "</font><br>"
		}	
     	tmpRt = "<TD width=112 height=65 bgcolor=" + bgcolor + " valign=top>"+fclass + htitle+ strDiv + content +"</div></TD>";
     }
     return (tmpRt);
} 

//������������ �ľ��Ͽ� �������̸� �����ϸ��� �����Ѵ�.
function CheckHoliday(dayno) {
	var hList = document.forms[0].hdHolidayList.value
	var idx = hList.indexOf(dayno+"#")
	if(idx > -1) {
		aStr=hList.substring(idx,hList.length).split(";")
		Str=aStr[0].split("#")
		return Str[1]	//�����ϸ�
	}
	return
}

// 2���� �ִ볯¥ ���
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

// ���� ���
function isLeapYear (Year) {
    if (((Year % 4)==0) && ((Year % 100)!=0) || ((Year % 400)==0)) {
        return (true);
    }
    else {
        return (false);
    }
}

//�ٷΰ���
function goCalendar(sform){
     var tmpYear = sform.hdStartYear.options[sform.hdStartYear.selectedIndex].text;			//������ �⵵
     var tmpMonth = sform.hdStartMonth.options[sform.hdStartMonth.selectedIndex].text;		//������ ��
     var tmpCno = document.hidform.hdUserCno.value;											//����� ���
     var tmpDay = getDaysInMonth(tmpMonth, tmpYear);										//�ش���� ���ڼ�
     var cday = document.hidform.hdOrgToday.value.substring(8, 10); 						//���� ��¥
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
	<INPUT NAME="hdOrgToday" VALUE="<%=cal_ot%>" Type="Hidden"><!���ó�¥->
	<INPUT NAME="hdToday" VALUE="<%=cal_td%>" Type="Hidden"><! �μ��� �Ѿ�� ��¥ ->
	<INPUT NAME="hdDocList" VALUE="<%=cal_dl%>" Type="Hidden">
	<INPUT NAME="hdHolidayList" VALUE="<%=cal_hl%>" Type="Hidden"><! ���ϸ���Ʈ ->	

</form>

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> ��������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="Calendar_View.jsp?Sabun=<%=cal_id%>" onMouseOver="window.status='���� ���� ����';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_m_o.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View1.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='�ְ� ���� ����';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_w.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View2.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='2�ְ� ���� ����';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_2w.gif" border="0"></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_Print.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='�μ��� ���� ����';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_p.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=200 style="padding-left:10px" valign='middle'>
			   <form name="goform" onSubmit="return false;" style="margin:0">
				   <A href="Calendar_View.jsp?PID=&openform&Sabun=<%=cal_id%>&year=<%=PYear%>&month=<%=PMonth%>&day=<%=PNDay%>" onMouseOver="window.status='�����޷� �̵�';return true;" onMouseOut="window.status='';return true;"><img src="../images/arrow_back.gif" border="0" align="absmiddle"></A>

					<SELECT NAME="hdStartYear">
					<% 
						//�⵵ ȭ�� Display�ϱ� (����⵵���� 2�������� 10�����)
						String YMD = anbdt.getYear();								//������� ���Ѵ�.
						int YYYY = Integer.parseInt(YMD) - 2; 						//����⿡�� -2���� �Ѵ�.

						String SELY = "";
						for(int yi=0; yi < 10; yi++) {
							String YEAR = Integer.toString(YYYY + yi);				//�⵵ǥ��
							if(selYear.equals(YEAR)) SELY="SELECTED";
							else SELY = "";
							out.println("<OPTION " + SELY + " >" + YEAR);
						}
						out.println("</SELECT><font color='#565656'>��</font>");
					%>
					<SELECT NAME="hdStartMonth" onChange="goCalendar(document.goform)">
					<%
						//���� ȭ�鿡 Display�ϱ� 
						String MYMD = anbdt.getMonth();							//������� ���Ѵ�.
						int MM = Integer.parseInt(MYMD); 						//�����

						String SELM = "";
						for(int mi=1; mi <= 12; mi++) {
							String MONTH = Integer.toString(mi);			//��ǥ��
							if(MONTH.length() == 1) MONTH = "0" + MONTH;
							if(selMonth.equals(MONTH)) SELM="SELECTED";
							else SELM = "";
							out.println("<OPTION " + SELM + " >" + MONTH);
						}
						out.println("</SELECT><font color='#565656'>��</font>");
					%>
					<A href="Calendar_View.jsp?PID=&openform&Sabun=<%=cal_id%>&year=<%=NYear%>&month=<%=NMonth%>&day=<%=PNDay%>" onMouseOver="window.status='�����޷� �̵�';return true;" onMouseOut="window.status='';return true;"><img src="../images/arrow_next.gif" border="0" align="absmiddle"></A></form>					  
			  </TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!-- ���� ǥ�� ���� -->
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