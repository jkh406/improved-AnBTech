<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "2�ְ� �����ȹ����"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"	
	import="com.anbtech.mm.entity.*"
%>

<%
	
	//ȭ����¿� �ش�Ǵ� ���� (1�ְ�)
	String Pd0="";					//������
	String Pd1="";					//1����
	String Pd2="";					//2����
	String Pd3="";					//3����
	String Pd4="";					//4����
	String Pd5="";					//5����
	String Pd6="";					//6����
	String Pd7="";					//7����
	String Pd8="";					//8����
	String Pd9="";					//9����
	String Pd10="";					//10����
	String Pd11="";					//11����
	String Pd12="";					//12����
	String Pd13="";					//13����

	//ȭ����¿� �ش�Ǵ� ������ ����(1�ְ�)
	String Pc0="";					//������
	String Pc1="";					//1����
	String Pc2="";					//2����
	String Pc3="";					//3����
	String Pc4="";					//4����
	String Pc5="";					//5����
	String Pc6="";					//6����
	String Pc7="";					//7����
	String Pc8="";					//8����
	String Pc9="";					//9����
	String Pc10="";					//10����
	String Pc11="";					//11����
	String Pc12="";					//12����
	String Pc13="";					//13����

	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();				//Date�� ���õ� ������
	
	/*********************************************************************
	 	�Ķ���� ���޹ޱ�
	*********************************************************************/
	//����ޱ�
	com.anbtech.mm.entity.mpsMasterTable table;
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("MASTER_List");
	table = new mpsMasterTable();
	Iterator table_iter = table_list.iterator();

	String view_td = (String)request.getAttribute("view_td");			//����(yyyy/mm/dd)
	String year = view_td.substring(0,4);
	String month = view_td.substring(5,7);

	String factory_no = (String)request.getAttribute("factory_no");		//�����ȣ
	String mode = (String)request.getAttribute("mode");
	String Rdate = (String)request.getAttribute("view_td");				//����(yyyy/mm/dd)
	String cal_td = "";
	int Ryear=0;					//�⵵
	int Rmonth=0;					//��
	int Rday=0;						//��

	//ȭ�鿡 ���ڸ� ����ϱ� ���ؼ� (7�ϰ�)
	if(Rdate != null) {
		cal_td = Rdate;						//yyyy/mm/dd
		
		Ryear = Integer.parseInt(Rdate.substring(0,4));
		Rmonth = Integer.parseInt(Rdate.substring(5,7));
		Rday = Integer.parseInt(Rdate.substring(8,10));

		//�־��� (Ryear/Rmonth,Rday)���ڷ� Setting�� �־���(pi)��ŭ ���� ���� ���ϱ�
		Pd0= Rdate;
		Pd1 = anbdt.getDate(Ryear,Rmonth,Rday,1);	
		Pd2 = anbdt.getDate(Ryear,Rmonth,Rday,2);	
		Pd3 = anbdt.getDate(Ryear,Rmonth,Rday,3);
		Pd4 = anbdt.getDate(Ryear,Rmonth,Rday,4);
		Pd5 = anbdt.getDate(Ryear,Rmonth,Rday,5);
		Pd6 = anbdt.getDate(Ryear,Rmonth,Rday,6);
		Pd7 = anbdt.getDate(Ryear,Rmonth,Rday,7);	
		Pd8 = anbdt.getDate(Ryear,Rmonth,Rday,8);	
		Pd9 = anbdt.getDate(Ryear,Rmonth,Rday,9);
		Pd10 = anbdt.getDate(Ryear,Rmonth,Rday,10);
		Pd11 = anbdt.getDate(Ryear,Rmonth,Rday,11);
		Pd12 = anbdt.getDate(Ryear,Rmonth,Rday,12);
		Pd13 = anbdt.getDate(Ryear,Rmonth,Rday,13);
	}


	//ȭ�鿡 ����� ������ ������ ���
	while(table_iter.hasNext()) {
		table = (mpsMasterTable)table_iter.next();
		String pde = table.getPlanDate();
		String pid = table.getPid();
		String fcd = table.getFgCode();
		String pct = Integer.toString(table.getPlanCount());
		String cnt = table.getModelCode()+","+table.getModelName();

		if(Pd0.equals(pde))  Pc0 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd1.equals(pde))  Pc1 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd2.equals(pde))  Pc2 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd3.equals(pde))  Pc3 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd4.equals(pde))  Pc4 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd5.equals(pde))  Pc5 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd6.equals(pde))  Pc6 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd7.equals(pde))  Pc7 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd8.equals(pde))  Pc8 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd9.equals(pde))  Pc9 += "<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd10.equals(pde)) Pc10 +="<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd11.equals(pde)) Pc11 +="<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd12.equals(pde)) Pc12 +="<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";
		if(Pd13.equals(pde)) Pc13 +="<A Href=../servlet/mpsInfoServlet?mode=mps_view&factory_no="+factory_no+"&pid="+pid+" onMouseOver=\"window.status='���� �󼼺���';return true;\" onMouseOut=\"window.status='';return true;\">"+fcd+"("+pct+")&nbsp;&nbsp;"+cnt+"</font>" + "</A><br>";

	}

	/***********************************************************************
	//������ �ý��� �����б�
	***********************************************************************/
	String cal_ot = anbdt.getDate();				//���ó��� (����:yyyy-MM-dd)
	
	/***********************************************************************
	// ȭ����¿� �⵵ �ʱ�ȸ�ϱ�
	***********************************************************************/
	//�ݳ⵵ ���ϱ� (�׻� �ݳ⵵�� ������)
	int Cyear = Integer.parseInt(anbdt.getYear());					

	//�⵵ ȭ�� Display�ϱ� (�Ѱܹ����⵵ ���� 3�������� 10�����)
	int Syear = Cyear - 3; 

%>

<HTML>
<HEAD><TITLE>2�ְ� �����ȹ����</TITLE>
<link rel='stylesheet' type='text/css' href='../css/style.css'>
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad="DoEvent();">
<SCRIPT LANGUAGE="JavaScript">
<!-- 
//road�� ���� ����Ʈ
function DoEvent() {
	var yy = '<%=Syear%>'; 
	var tmp= '<%=Rdate%>';
	var tmp1= tmp;
	var tmp2=tmp.split("/");
	document.forms[0].hdYear.selectedIndex=tmp2[0]-yy	

	RefreshWeek()  //��������Ʈ
	
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

// '���� - ���� - ����' ����Ʈ ����ϱ�
function RefreshUrl(opt) {
	var form=document.forms[0]
	var view_td = '<%=Rdate%>';
	var factory_no = '<%=factory_no%>';
	var mode = '<%=mode%>';

	if(opt==0) {   //�޺����� ����
		str=form.hdWeekNo.options[form.hdWeekNo.selectedIndex].text
		str1=str.split("(")
		strDate=str1[1].substring(0,10)
		var strUrl="../servlet/mpsInfoServlet?mode="+mode+"&view_td="+strDate+"&factory_no="+factory_no;
		location.href=strUrl
		return  //����
	}
	
	if(opt==2) {  //����  
	     var fromField = document.forms[0].hdCurrentDate;
    	 var syear =  fromField.value.substring(0, 4); //���� �⵵
    	 var smonth = fromField.value.substring(5, 7) ; //���� ��
    	 var sday = fromField.value.substring(8, 10); //���� ��¥
    
		toDay = new Date();
		toDay.setTime(Date.UTC(syear, smonth-1, sday))
		wn=toDay.getDay()
		tDate.setTime(Date.UTC(toDay.getYear(), toDay.getMonth(), toDay.getDate()-wn))
	}	
	if(opt==1) {  //����  
		tmpY=document.forms[0].hdSdate.value.substring(0,4)
		tmpM=document.forms[0].hdSdate.value.substring(5,7)
		tmpD=document.forms[0].hdSdate.value.substring(8,10)
		//alert(tmpY,tmpM,tmpD)
		toDay = new Date();
		toDay.setTime(Date.UTC(tmpY, tmpM-1, tmpD))
		wn=toDay.getDay()+7
		tDate.setTime(Date.UTC(toDay.getYear(), toDay.getMonth(), toDay.getDate()-wn))
	}	
	if(opt==3) {  //����  
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
//��������Ʈ ���� ����ϱ�
function RefreshWeek() {
	var form=document.forms[0]
	tyear=form.hdYear.options[form.hdYear.selectedIndex].text

	var wk = 0
	tDate = new Date();
	tDate.setTime(Date.UTC(tyear, 0, 1))              //�������� ��ģ���
	wn=tDate.getDay()

	if(tyear>2000) {
		if(wn != 0) {
			tDate.setTime(Date.UTC(tyear, 0, -(wn-1)))
			strSdate=stringDate(tDate)
			wk++
			option=new Option(wk+" ���� (" + strSdate + ")", wk)
			form.hdWeekNo.options[wk-1]=option
		}
	}

	for (var n=1; n<367; n++) {                           //�ش翬��
		tDate.setTime(Date.UTC(tyear, 0, n))
		if(tDate.getDay()==0) { 
			if(tDate.getYear() > tyear) {continue}
			strSdate=stringDate(tDate)
			wk++
			option=new Option(wk+" ���� (" + strSdate + ")", wk)
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
<FORM METHOD=post ACTION="" NAME="_hdForm3" style="margin:0">
<INPUT Type="Hidden" Name="hdCurrentDate" value="<%=cal_ot%>">
<INPUT NAME="hdSdate" VALUE="<%=cal_td%>" ReadOnly size=10 type="hidden">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=factory_no%>���� 2�ְ� �����ȹ����</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=cal_month&year=<%=year%>&month=<%=month%>&factory_no=<%=factory_no%>" onMouseOver="window.status='����������ȹ';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_m.gif" border="0"></a></TD>
					<TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
					<TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_week1&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='�ְ�������ȹ';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_w.gif" border="0"></a></TD>
					<TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
					<TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_week2&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='2�ְ�������ȹ';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_2w_o.gif" border="0"></TD>
					<TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
					<TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_month&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='������������Ʈ';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_p.gif" border="0"></a></TD>
					<TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
					<TD align=left width=400 style="padding-left:10px" valign='middle'>
						<A href="JavaScript:RefreshUrl('1')"><img src="../mm/images/arrow_back.gif" border="0" align="absmiddle" alt="������"></A>
						<A href="JavaScript:RefreshUrl('2')"><img src="../mm/images/this_week.gif" border="0" align="absmiddle" alt="����"></A>
						<A href="JavaScript:RefreshUrl('3')"><img src="../mm/images/arrow_next.gif" border="0" align="absmiddle" alt="������"></A>
						
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

<!-- ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TR><!--��-->
		<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD width=15% align=middle class='calendar_title'><%=Pd0%> <IMG src="../mm/images/sunday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc0%></DIV></TD>
						<TD width=15% align=middle class='calendar_title'><%=Pd7%> <IMG src="../mm/images/sunday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc7%></DIV></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
	<TR><!--��-->
		<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD width=15% align=middle class='calendar_title'><%=Pd1%> <IMG src="../mm/images/monday_1.gif" align="absmiddle"></TD>
						 <TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc1%></DIV></TD>
						<TD width=15% align=middle class='calendar_title'><%=Pd8%> <IMG src="../mm/images/monday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc8%></DIV></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
	<TR><!--ȭ-->
		<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD width=15% align=middle class='calendar_title'><%=Pd2%> <IMG src="../mm/images/tuesday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc2%></DIV></TD>
						<TD width=15% align=middle class='calendar_title'><%=Pd9%> <IMG src="../mm/images/tuesday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc9%></DIV></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
	<TR><!--��-->
		<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD width=15% align=middle class='calendar_title'><%=Pd3%> <IMG src="../mm/images/wednesday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc3%></DIV></TD>
						<TD width=15% align=middle class='calendar_title'><%=Pd10%> <IMG src="../mm/images/wednesday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc10%></DIV></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
	<TR><!--��-->
		<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD width=15% align=middle class='calendar_title'><%=Pd4%> <IMG src="../mm/images/thursday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc4%></DIV></TD>
						<TD width=15% align=middle class='calendar_title'><%=Pd11%> <IMG src="../mm/images/thursday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc11%></DIV></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
	<TR><!--��-->
		<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD width=15% align=middle class='calendar_title'><%=Pd5%> <IMG src="../mm/images/friday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc5%></DIV></TD>
						<TD width=15% align=middle class='calendar_title'><%=Pd12%> <IMG src="../mm/images/friday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#F5F5F5" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc12%></DIV></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#CCCCCC'></TD></TR>
	<TR><!--��-->
		<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD width=15% align=middle class='calendar_title'><%=Pd6%> <IMG src="../mm/images/saturday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc6%></DIV></TD>
						<TD width=15% align=middle class='calendar_title'><%=Pd13%> <IMG src="../mm/images/saturday_1.gif" align="absmiddle"></TD>
						<TD width=35% bgcolor="#DFEDFD" style="padding-left:5px"><DIV ID="calendar" STYLE="position:static; visibility:visible; left:0; height:66px; width:100%;overflow:auto;"><%=Pc13%></DIV></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#CCCCCC'></TD></TR></TD></TR></TABLE>

</FORM>
</BODY>
</HTML>


