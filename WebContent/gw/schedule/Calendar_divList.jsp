<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "�μ����� ���LIST ����"		
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
	
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������

	/*********************************************************************
	 	����� login �˾ƺ���
	*********************************************************************/
	id = login_id; 			//������

	//�ʱ�ȭ
	cal_id=cal_ot=SELyear=SELmonth=SELdate="";
	Tyear=Tmonth=Tday=items="";

	/*********************************************************************
	 	�Ѱܿ� ���� �б� (from Calendar_WriteD.jsp)   Sabun=&Date=
		��� : �μ� �ڵ�, 
	*********************************************************************/
	String Rsabun = request.getParameter("Sabun");			//�Ѱܹ��� ������� �μ��ڵ�
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
	3. ȸ�� ������� ��ȸ�ϱ�
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
	document.write("</select>��&nbsp");
	document.write("<select name=\"select_month\" class=\"select\" OnChange=\"MovePage()\">");
	
	for (var j = 0; j < arrmonth.length; j++) {
		document.write("<option value=\"" + arrmonth[j]);	
		if ( arrmonth[j] == month)
			document.write("\" selected>" + arrmonth[j] + "</option>");
		else
			document.write("\">" + arrmonth[j] + "</option>");
	}
	document.write("</select>��");	
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
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> �μ����� �˻�</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top><Form Name=goform onSubmit="return goPage()" style="margin:0">
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='300'><A href="Calendar_divList.jsp?OpenView&Start=1&count=1000&Sabun=<%=cal_id%>&Date=<%=PYear%>*<%=PMonth%>"><img src="../images/arrow_back.gif" border="0"></A>&nbsp;<script Language="javascript">SelectPage('<%=SELyear%>','<%=SELmonth%>')</script>&nbsp;<A href="Calendar_divList.jsp?OpenView&Start=1&count=1000&Sabun=<%=cal_id%>&Date=<%=NYear%>*<%=NMonth%>"><img src="../images/arrow_next.gif" border="0"></A></TD></TR></TBODY>
		</TABLE></form></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=100 align=middle class='list_title'>��¥</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>�׸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>�ð�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>��������</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<% if (bean.isEmpty()) { %>
	<TR bgColor=#ffffff><TD height='30' colspan=9 align='center' class='list_bg'>*****  �Էµ� ������ �����ϴ�. *****</TD></TR> 
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
					<% if(bean.getData("isopen").equals("1")) { //����%>
						<img src="../images/open.gif" border="0">
					<% } else {									//�����%>
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