<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.admin.entity.*"
%>
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree"/>

<%!
	UserInfoTable table;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>

<%
	String year = request.getParameter("y");
	String division = request.getParameter("div");

	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new UserInfoTable();
	Iterator table_iter = table_list.iterator();
%>


<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../es/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../es/images/blet.gif" align="absmiddle"> ���޺� �ñ޼���</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'>
						<form method="get" name="sForm" action="../servlet/GeunTaeServlet" style="margin:0">
						<SELECT name='year' onChange="chkYear();">
							<OPTION value="">�⵵ ����</OPTION>
							<OPTION value="2003">2003��</OPTION>
							<OPTION value="2004">2004��</OPTION>
							<OPTION value="2005">2005��</OPTION>
							<OPTION value="2006">2006��</OPTION>
							<OPTION value="2007">2007��</OPTION>
							<OPTION value="2008">2008��</OPTION>
							<OPTION value="2009">2009��</OPTION>
							<OPTION value="2010">2010��</OPTION>
							<OPTION value="2011">2011��</OPTION>
						</SELECT>
					<%	if(!year.equals("")){	%>
							<script language='javascript'>
								document.sForm.year.value = '<%=year%>';
							</script>
					<%	}	%>

						<SELECT name='division'>
							<%=recursion.viewComboByCode(0,0)%>
						</SELECT>
					<%	if(!division.equals("")){	%>
							<script language='javascript'>
								document.sForm.division.value = '<%=division%>';
							</script>
					<%	}	%>
						<input type='button' value='����' onClick='javascript:go();'>
						<input type='hidden' name='mode' value='manager_hyuga_day'>
				  </form></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--����Ʈ-->
  <TR height=100%>
    <TD vAlign=top><form name='listForm' style='magrgin:0'>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>�μ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>�̸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�ñޱݾ�(��)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (UserInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getDivision()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getUserRank()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getUserName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getUserId()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><input type='text' value='<%=sp.getMoneyFormat(table.getHourlyPay(),"")%>' size='10' name='<%=table.getUserId()%>' maxlength="10" style="text-align:right" onKeyPress="currency(this);" onKeyup="com(this);"></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:modify('<%=table.getUserId()%>');"><img src='../es/images/lt_modify.gif' border='0' align='absmiddle'></a></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></TD>
			<TR><TD colSpan=15 background="../es/images/dot_line.gif"></TD></TR>
<% 
		no++;
	}  //while 

%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>
</BODY>
</HTML>

<script language='javascript'>
	function go(){
		var f = document.sForm;
		var year = f.year.value;
		var division = f.division.value;
		location.href = "../servlet/ExtraWorkServlet?mode=manager_hourly_pay&y="+year+"&div="+division;
	}

	function chkYear(){
		var f = document.sForm;
		var year = f.year.value;
		var now = new Date();
		var now_year = now.getYear();
		if(year > now_year){
			alert("���س⵵������ ��Ȳ�� �� �� �ֽ��ϴ�.");
			f.year.value = now_year;
			return;
		}
	}

	function modify(id){
		var f = document.sForm;
		var year = f.year.value;
		var hourly_pay  = unComma(eval("document.listForm."+id+".value"));
		wopen("../ew/admin/modify_hourly_pay.jsp?year="+year+"&id="+id+"&hourly_pay="+hourly_pay,'modify_hyuga','200','70','scrollbars=no,toolbar=no,status=no,resizable=no');
	}

	function wopen(url, t, w, h,st) {
		var sw;
		var sh;
		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
	}

	/**********************
	 * ���ڸ� �Էµǰ�
	 **********************/
	function currency(obj)
	{
		if (event.keyCode >= 48 && event.keyCode <= 57) {
			
		} else {
			event.returnValue = false
		}
	}

	function com(obj)
	{
		obj.value = unComma(obj.value);
		obj.value = Comma(obj.value);
	}

	/**********************
	 * õ���� �޸� ����
	 **********************/
	function Comma(input) {

	  var inputString = new String;
	  var outputString = new String;
	  var counter = 0;
	  var decimalPoint = 0;
	  var end = 0;
	  var modval = 0;

	  inputString = input.toString();
	  outputString = '';
	  decimalPoint = inputString.indexOf('.', 1);

	  if(decimalPoint == -1) {
		 end = inputString.length - (inputString.charAt(0)=='0' ? 1:0);
		 for (counter=1;counter <=inputString.length; counter++)
		 {
			var modval =counter - Math.floor(counter/3)*3;
			outputString = (modval==0 && counter <end ? ',' : '') + inputString.charAt(inputString.length - counter) + outputString;
		 }
	  }
	  else {
		 end = decimalPoint - ( inputString.charAt(0)=='-' ? 1 :0);
		 for (counter=1; counter <= decimalPoint ; counter++)
		 {
			outputString = (counter==0  && counter <end ? ',' : '') +  inputString.charAt(decimalPoint - counter) + outputString;
		 }
		 for (counter=decimalPoint; counter < decimalPoint+3; counter++)
		 {
			outputString += inputString.charAt(counter);
		 }
	 }
		return (outputString);
	}

	/**********************
	 * ���ڿ��� Comma ����
	 **********************/
	function unComma(input) {
	   var inputString = new String;
	   var outputString = new String;
	   var outputNumber = new Number;
	   var counter = 0;
	   if (input == '')
	   {
		return 0
	   }
	   inputString=input;
	   outputString='';
	   for (counter=0;counter <inputString.length; counter++)
	   {
		  outputString += (inputString.charAt(counter) != ',' ?inputString.charAt(counter) : '');
	   }
	   outputNumber = parseFloat(outputString);
	   return (outputNumber);
	}
</script>