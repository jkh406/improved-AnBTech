<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "���� MPS LIST ����"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.text.Hanguel"
	import="java.util.StringTokenizer"
%>

<%	
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//Date�� ���õ� ������

	/*********************************************************************
	 	�Ķ���� ���޹ޱ�
	*********************************************************************/	
	//�μ��� �Ѿ�� ��,��,�� �ޱ�
	String view_td = (String)request.getAttribute("view_td"); if(view_td == null) view_td=anbdt.getDate(0);
	String year = view_td.substring(0,4);
	String month = view_td.substring(5,7);
	String day = view_td.substring(8,10);		
	String cal_td = view_td;

	String factory_no = (String)request.getAttribute("factory_no");	//�����ȣ
	
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
	view_td = anbdt.setDate(iyear,imonth,sunday); //�Ѱ��� ���� (����:yyyy/mm/dd)	

	/*********************************************************************
	 	����,������ ��,��,�� ���ϱ�
	*********************************************************************/	
	String PYear = "";				//�����⵵
	String PMonth = "";				//������
	String NYear = "";				//�����⵵
	String NMonth = "";				//������
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
	
	//����ޱ�
	com.anbtech.mm.entity.mpsMasterTable table;
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("MASTER_List");
	table = new mpsMasterTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD><TITLE>���� MPS LIST ����</TITLE>
<link rel='stylesheet' type='text/css' href='../css/style.css'>
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=factory_no%>���� �μ��� MPS����</TD></TR></TBODY>
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
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_week2&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='2�ְ�������ȹ';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_2w.gif" border="0"></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=30><a href="../servlet/mpsInfoServlet?mode=list_month&view_td=<%=view_td%>&factory_no=<%=factory_no%>" onMouseOver="window.status='������������Ʈ';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/view_p_o.gif" border="0"></a></TD>
			  <TD width=7 align='center'><IMG src="../mm/images/11.gif"></TD>
			  <TD align=left width=200 style="padding-left:10px" valign='middle'>
			  <form name="goform" onSubmit="return false;" style="margin:0">
				   <A href="../servlet/mpsInfoServlet?mode=list_month&view_td=<%=PYear%>/<%=PMonth%>/<%=day%>&factory_no=<%=factory_no%>" onMouseOver="window.status='�����޷� �̵�';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/arrow_back.gif" border="0" align="absmiddle"></A>

					<SELECT NAME="hdStartYear">
					<% 
						//�⵵ ȭ�� Display�ϱ� (����⵵���� 2�������� 10�����)
						String YMD = anbdt.getYear();								//������� ���Ѵ�.
						int YYYY = Integer.parseInt(YMD) - 2; 						//����⿡�� -2���� �Ѵ�.

						String SELY = "";
						for(int yi=0; yi < 10; yi++) {
							String YEAR = Integer.toString(YYYY + yi);				//�⵵ǥ��
							if(year.equals(YEAR)) SELY="SELECTED";
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
							if(month.equals(MONTH)) SELM="SELECTED";
							else SELM = "";
							out.println("<OPTION " + SELM + " >" + MONTH);
						}
						out.println("</SELECT><font color='#565656'>��</font>");
					%>
					<A href="../servlet/mpsInfoServlet?mode=list_month&view_td=<%=NYear%>/<%=NMonth%>/<%=day%>&factory_no=<%=factory_no%>" onMouseOver="window.status='�����޷� �̵�';return true;" onMouseOut="window.status='';return true;"><img src="../mm/images/arrow_next.gif" border="0" align="absmiddle"></A></form>					  
			  </TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!-- ���� -->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TR>
			<TD vAlign=top>
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR vAlign=middle height=25>
							<TD noWrap width=60 align=middle class='list_title'>��������ڵ�</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>����ǰ���ڵ�</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=100% align=middle class='list_title'>����ǰ�񼳸�</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=60 align=middle class='list_title'>�����ȹ����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>�������</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>�����ȹ����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>��ȹ�����</TD></TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=14></TD></TR>
<%
					while(table_iter.hasNext()) {
						table = (mpsMasterTable)table_iter.next();
						String status = table.getMpsStatus();
						if(status.equals("1")) status = "�ۼ�";
						else if(status.equals("2")) status = "Ȯ����û";
						else if(status.equals("3")) status = "����Ȯ��";
						else if(status.equals("4")) status = "MRP����";
						else if(status.equals("5")) status = "MRP����";
						else if(status.equals("6")) status = "�������Ȯ��";
						else if(status.equals("7")) status = "���긶��";
%>
						<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
							<TD align=middle height="24" class='list_bg'><%=table.getFactoryNo()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'>
							<a href='javascript:view(<%=table.getPid()%>)'><%=table.getItemCode()%></a></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'> <%=table.getItemSpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=Integer.toString(table.getPlanCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=status%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getPlanDate()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getRegDate()%></td>
						</TR>
			<TR><TD colspan=14 background="../mm/images/dot_line.gif"></TD></TR>
<%	} //while
%>
</TBODY></TABLE></TD></TR></TABLE>
</BODY>
</HTML>

<SCRIPT LANGUAGE="JavaScript">
<!-- 
//�����ϱ�
function view(pid) 
{
	location.href = "../servlet/mpsInfoServlet?mode=mps_view&pid="+pid+"&factory_no="+'<%=factory_no%>';
}
//�ٷΰ���
function goCalendar(sform)
{
     var tmpYear = sform.hdStartYear.options[sform.hdStartYear.selectedIndex].text;			//������ �⵵
     var tmpMonth = sform.hdStartMonth.options[sform.hdStartMonth.selectedIndex].text;		//������ ��
	 var tmpDate = '<%=day%>';
	 var view_td = tmpYear+"/"+tmpMonth+"/"+tmpDate;
	 var factory_no = '<%=factory_no%>';
     location.href = "../servlet/mpsInfoServlet?mode=list_month&view_td="+view_td+"&factory_no="+factory_no;
}
-->
</SCRIPT>