<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "�μ� ����"		
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
	//login ���� ����
	String id="";					//login

	//�޽��� ���޺���
	String Message="";				//�޽��� ���� ����  

	//���޺��� ���� ���� (from Calendar_View.jsp)
	String cal_id = "";				//����� ���
	String cal_di = "";				//����� �μ����� �ڵ�
	String cal_ot = "";				//���ó���(����:yyyy/mm/dd)
	String view_td = "";			//������ �Ͽ��ϳ��ڸ� �Ѱ��ֱ� (yyyy/mm/dd)	

	String SELyear="";				//�μ��� �Ѿ�� �⵵
	String SELmonth="";				//�μ��� �Ѿ�� ��
	String SELdate="";				//�μ��� �Ѿ�� ��

	//����,������ ����
	String PYear = "";				//�����⵵
	String PMonth = "";				//������
	String NYear = "";				//�����⵵
	String NMonth = "";				//������
	String PNDay = "";				//��

	//����� ����
	String Tyear="";				//�⵵
	String Tmonth="";				//��
	String Tday="";					//��

	//�������� ȭ�鿡 �����ֱ�
	String[][] rd;					//������ ����迭�� ��� 
	int count = 0;					//������ ����
	String searchitem="";			//�˻��� �׸�
	String items = "";				//�ش����� �����׸� ���

%>
<%	
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	Calendar_Print view = new com.anbtech.gw.business.Calendar_Print();			//�ش�� ������ �������� ������

	/*********************************************************************
	 	����� login �˾ƺ���
	*********************************************************************/
	id = login_id; 			//������

	//�μ� �����ڵ� ã��
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

	//�ʱ�ȭ
	cal_id=cal_ot=SELyear=SELmonth=SELdate="";
	Tyear=Tmonth=Tday=items="";

	/*********************************************************************
	 	�Ѱܿ� ���� �б� (from Calendar_View.jsp)   Sabun=&Date=
	*********************************************************************/
	String Rsabun = request.getParameter("Sabun");			//�Ѱܹ��� ���
	if(Rsabun != null) cal_id = Rsabun;

	//�Ѱܹ��� ����(yyyy*mm) from Calendar_View*.jsp 
	String Rdate = request.getParameter("Date");			

	//ȭ�鿡 ���ڸ� ����ϱ� ���ؼ� 
	if(Rdate != null) {
		SELyear = Rdate.substring(0,4);						//yyyy
		SELmonth = Rdate.substring(5,7);					//mm
		SELdate = "01";										//dd
	}

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

	/*********************************************************************
	 	��ü���� �������� ������ϱ�
	*********************************************************************/
	//���õ� �⵵ �� �Է¹ޱ� (ȭ�鿡��)
	// (�Ѱܹ��� ������ ��츦 �����ϱ� ���ؼ�)
	if(Rdate == null) {
		SELyear = request.getParameter("select_year");
		SELmonth = request.getParameter("select_month");
	}

	//SELyear and SELmonth�� null�� ���
	if(SELyear == null) { SELyear = Tyear; SELmonth = Tmonth; }

	/***********************************************************************
	//������ �ý��� �����б�
	***********************************************************************/
	cal_ot = anbdt.getDate();				//���ó��� (����:yyyy-MM-dd)
	Tyear = anbdt.getYear();				//�⵵
	Tmonth = anbdt.getMonth();				//��
	Tday = anbdt.getDates();				//��

	/*********************************************************************
	 	����,������ ��,��,�� ���ϱ�
	*********************************************************************/	
	int toYear = Integer.parseInt(SELyear);							//�⵵

	String toMon = SELmonth;
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
	PNDay = Tday;													//��

	/***********************************************************************
	3. ����/ȸ��/�μ� ������� ��ȸ�ϱ�
	***********************************************************************/
	String cal_dl = "";	//������� clear(ȸ��,�μ�,����)
	//1. ȸ�� ������� �б� (����:0)
	cal_dl = view.Other_Print("0",SELyear,SELmonth,"COM");

	//2. �μ� ������� �б� (����:�μ������ڵ�)
	cal_dl += view.Other_Print(cal_di,SELyear,SELmonth,"DIV");

	//3. ���� ������� �б� (���� : ���)
	if(cal_id.equals(id)) {			//���θ� ����Ѵ�. (login�ڽ��ǰ�)
		cal_dl += view.Owner_Print(cal_id,SELyear,SELmonth);
	} else {						//������ ���븸 ����Ѵ�. (�����ڰ�)
		cal_dl += view.Other_Print(cal_id,SELyear,SELmonth,"INI");
	}

	//�������� �迭�� ���
	count = 0;
	StringTokenizer pdata = new StringTokenizer(cal_dl,";");
	count = pdata.countTokens();
	rd = new String[count][7];				//�迭 �ʱ�ȭ
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

	//���ϳ��ڿ� �˻��׸� ����� ���Ͽ� �迭 rd[n][6]�� ���
	searchitem = Hanguel.toHanguel(request.getParameter("item"));
	if(searchitem == null) searchitem="all";	//��ü����	
	for(int ni=0; ni<count; ni++) {
		String RD = rd[ni][0];
		int scnt = 0;
		for(int cn=0; cn<count; cn++) {
			if(searchitem.equals("all")) {				//��ü����(���ڸ� ����)
				if(rd[cn][0].equals(RD)) scnt++;
			} else {									//������ �׸� (����,������ �׸� ����)
				if(rd[cn][3].lastIndexOf(">") == -1){	//��������(������������)
					if(rd[cn][0].equals(RD) && rd[cn][3].equals(searchitem)) scnt++;
				} else {								//ȸ��,�μ�����
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
	4. �׸� ������� ��ȸ�ϱ�
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
//***** ���⿭ �ٰ� ������ ó�� *****
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
	document.write("</select><font color='#565656'>��</font>&nbsp");
	document.write("<select name=\"select_month\" class=\"select\" OnChange=\"MovePage()\">");
	
	for (var j = 0; j < arrmonth.length; j++) {
		document.write("<option value=\"" + arrmonth[j]);	
		if ( arrmonth[j] == month)
			document.write("\" selected>" + arrmonth[j] + "</option>");
		else
			document.write("\">" + arrmonth[j] + "</option>");
	}
	document.write("</select><font color='#565656'>��</font>");	
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
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> �μ�������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="Calendar_View.jsp?Sabun=<%=cal_id%>" onMouseOver="window.status='���� ���� ����';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_m.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View1.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='�ְ� ���� ����';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_w.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_View2.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='2�ְ� ���� ����';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_2w.gif" border="0"></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=30><a href="Calendar_Print.jsp?Sabun=<%=cal_id%>&Date=<%=view_td%>" onMouseOver="window.status='�μ��� ���� ����';return true;" onMouseOut="window.status='';return true;"><img src="../images/view_p_o.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../images/11.gif"></TD>
			  <TD align=left width=200 style="padding-left:10px" valign='middle'><form name='goform' onSubmit='return goPage()' style="margin:0">
					<a href="Calendar_Print.jsp?OpenView&Start=1&count=1000&Sabun=<%=cal_id%>&Date=<%=PYear%>*<%=PMonth%>" onMouseOver="window.status='�����޷� �̵�';return true;" onMouseOut="window.status='';return true;"><img src="../images/arrow_back.gif" border="0" align="absmiddle"></A>&nbsp;<Script Language="javascript">SelectPage('<%=SELyear%>','<%=SELmonth%>')</Script>&nbsp;<a href="Calendar_Print.jsp?OpenView&Start=1&count=1000&Sabun=<%=cal_id%>&Date=<%=NYear%>*<%=NMonth%>" onMouseOver="window.status='�����޷� �̵�';return true;" onMouseOut="window.status='';return true;"><img src="../images/arrow_next.gif" border="0" align="absmiddle"></A></form></TD>
			  <TD width=7 align='center'></TD>
			  <TD align=left width=400 style="padding-left:10px" valign='middle'>
					<form name='s' method='post' action='Calendar_Print.jsp' style="margin:0">
					<SELECT name='item'>
					<option value='all' selected>��ü</option>
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

<!-- ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TR>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=120 align=middle class='list_title'>��¥</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>�׸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>�ð�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>��������</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
	//�������� ȭ�鿡 ����ϱ�
	//rd[i][0]:����,rd[i][1]:����,rd[i][2]:����,rd[i][3]:�׸�,rd[i][4]:�ð�,rd[i][5]:�����ڵ�
	int samedate = 0;
	String day = "";
	int toyear = Integer.parseInt(SELyear);
	int tomonth = Integer.parseInt(SELmonth);
	for(int ni=0; ni<31; ni++) { 
		String today = anbdt.getDate(toyear,tomonth,1,ni);			//yyyy/MM/dd
		int readmonth = Integer.parseInt(today.substring(5,7));		//MM���ϱ�

		//���� ���ϱ�
		int ddd = anbdt.getDay(toyear,tomonth,ni+1);				//���� ���ڷ� �ޱ�
		String[] D = {"","��","��","ȭ","��","��","��","��"};
		day = D[ddd];
		if(day.equals("��")) day = "<font color=red>"+day+"</font>";
		else if(day.equals("��")) day = "<font color=blue>"+day+"</font>";
		else day = ""+day;

		//�����޸� ȭ�鿡 �����
		if(tomonth == readmonth) {				//���������� �����޸� ���
			samedate = 0;
			for(int si=0; si<count; si++) {		//���ڿ� �´� ���� �񱳸� ���� �迭������ ���������� ��������
				if(searchitem.equals("all")) {		//��ü����
					if(rd[si][0].equals(today)) {	//���� ���ڿ� ��ü�׸� ���
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
				<% if(rd[si][2].equals("Y")) { //����%>
					<img src="../images/open.gif" border="0">
				<% } else { //�����%>
					<img src="../images/secret.gif" border="0">
				<% } %>			  			  
			  </TD></TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<%
					samedate++;
					} ////if (���� ���ڸ�)
				} //if (�׸��� ��ü ���ý�)
				else {		//�׸��� �ش��׸� ���ý�
					String sits = "";
					if(rd[si][3].lastIndexOf(">") == -1){	//��������(������������)
						sits = rd[si][3];
					} else {								//ȸ��,�μ�����
						int lc = rd[si][3].lastIndexOf(">"); 
						int le = rd[si][3].length();
						sits = rd[si][3].substring(lc+1,le);
					}
					if(rd[si][0].equals(today) && sits.equals(searchitem)) {	//���� ����,�׸� ���
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
				<% if(rd[si][2].equals("Y")) { //����%>
					<img src="../img/open.gif" border="0">
				<% } else { //�����%>
					<img src="../img/secret.gif" border="0">
				<% } %>			  
			  </TD></TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<%
					samedate++;
					}
					
				} ////if (�׸� ���ý�)
			} //for (���� ���ϱ�)
		} //if (������)
	} //for

%>
</TBODY></TABLE></TD></TR></TABLE>

<!-- �޴� �̵� ���� -->
<form name="basicinfo">
<input type="hidden" Name="hdSabun" value="<%=cal_id%>">
</form>

</BODY>
</HTML>