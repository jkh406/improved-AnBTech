<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkES02.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.es.geuntae.entity.*"
%>
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree"/>

<%!
	GeunTaeInfoTable table;
%>

<%
	String division		= request.getParameter("div");
	String year			= request.getParameter("y");
	String month		= request.getParameter("m");
	String day			= request.getParameter("d");
	
	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new GeunTaeInfoTable();
	Iterator table_iter = table_list.iterator();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../es/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false" onLoad="display();">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../es/images/blet.gif" align="absmiddle"> �μ��� ����� ��Ȳ</TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='100%'>
					<form method="get" name="sForm" action="../servlet/GeunTaeServlet" style="margin:0">
					<SELECT name='division'>
						<%=recursion.viewComboByCode(0,0)%>
					</SELECT> 

						<%	if(!division.equals("")){	%>
								<script language='javascript'>
									document.sForm.division.value = '<%=division%>';
								</script>
						<%	}	%>

					
					<SELECT name='year'>
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
					<SELECT name='month'>
						<OPTION value="01">1��</OPTION>
						<OPTION value="02">2��</OPTION>
						<OPTION value="03">3��</OPTION>
						<OPTION value="04">4��</OPTION>
						<OPTION value="05">5��</OPTION>
						<OPTION value="06">6��</OPTION>
						<OPTION value="07">7��</OPTION>
						<OPTION value="08">8��</OPTION>
						<OPTION value="09">9��</OPTION>
						<OPTION value="10">10��</OPTION>
						<OPTION value="11">11��</OPTION>
						<OPTION value="12">12��</OPTION>
					</SELECT>		  
				<%	if(!month.equals("")){	%>
						<script language='javascript'>
							document.sForm.month.value = '<%=month%>';
						</script>
				<%	}	%>
					<SELECT name='day'>
						<OPTION value="01">1��</OPTION>
						<OPTION value="02">2��</OPTION>
						<OPTION value="03">3��</OPTION>
						<OPTION value="04">4��</OPTION>
						<OPTION value="05">5��</OPTION>
						<OPTION value="06">6��</OPTION>
						<OPTION value="07">7��</OPTION>
						<OPTION value="08">8��</OPTION>
						<OPTION value="09">9��</OPTION>
						<OPTION value="10">10��</OPTION>
						<OPTION value="11">11��</OPTION>
						<OPTION value="12">12��</OPTION>
						<OPTION value="13">13��</OPTION>
						<OPTION value="14">14��</OPTION>
						<OPTION value="15">15��</OPTION>
						<OPTION value="16">16��</OPTION>
						<OPTION value="17">17��</OPTION>
						<OPTION value="18">18��</OPTION>
						<OPTION value="19">19��</OPTION>
						<OPTION value="20">20��</OPTION>
						<OPTION value="21">21��</OPTION>
						<OPTION value="22">22��</OPTION>
						<OPTION value="23">23��</OPTION>
						<OPTION value="24">24��</OPTION>
						<OPTION value="25">25��</OPTION>
						<OPTION value="26">26��</OPTION>
						<OPTION value="27">27��</OPTION>
						<OPTION value="28">28��</OPTION>
						<OPTION value="29">29��</OPTION>
						<OPTION value="30">30��</OPTION>
						<OPTION value="31">31��</OPTION>
					</SELECT>		  
				<%	if(!day.equals("")){	%>
						<script language='javascript'>
							document.sForm.day.value = '<%=day%>';
						</script>
				<%	}	%> <a href="javascript:go();"><img src="../es/images/bt_confirm.gif" border="0" align="absmiddle"></a>
					<input type='hidden' name='mode' value='work_history'>
				</form></TD>
			  <TD width='100%' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--����Ʈ-->
  <TR height=100%>
    <TD vAlign=top><form name=frm method="get">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=120 align=middle class='list_title'>�μ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>�̸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>��ٽð�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>��ٽð�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>��ٱ����IP</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>��ٱ����IP</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR></TBODY></TABLE>
	  <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:173; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
<%
	//******************************
	//������ table������ �������� �� loop  **
	//******************************
		
	while(table_iter.hasNext()){
		table = (GeunTaeInfoTable)table_iter.next();
		String user_rank	= table.getUser_rank();
		String user_name	= table.getUser_name();
		String ac_name		= table.getAc_name();
		String user_id		= table.getUser_id();
		String c_tims_s		= table.getTimeS();
		String c_tims_e		= table.getTimeE();
		String c_ip_s		= table.getH_sdate();
		String c_ip_e		= table.getH_edate();
		String reason		= table.getReason();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD noWrap width=120 align=middle height="24" class='list_bg'><%=ac_name%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=90 align=middle class='list_bg'><%=user_rank%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=90 align=middle class='list_bg'><%=user_name%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=75 align=middle class='list_bg'><%=c_tims_s%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=75 align=middle class='list_bg'><%=c_tims_e%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=125 align=middle class='list_bg'><%=c_ip_s%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=125 align=middle class='list_bg'><%=c_ip_e%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=50 align=middle class='list_bg'><%=reason%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=100% align=middle class='list_bg'></TD>
			</TR>
			<TR><TD colSpan=17 background="../es/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</BODY>
</HTML>


<script language='javascript'>
	function go(){
		var f = document.sForm;
		
		var division = f.division.value;
		var year = f.year.value;
		var month = f.month.value;
		var day = f.day.value;
		location.href = "../servlet/GeunTaeServlet?mode=work_history&div="+division+"&y="+year+"&m="+month+"&d="+day;
	}
	//�ػ󵵸� ���ؼ� div�� ���̸� ����
	function display() { 
		var w = window.screen.width; 
		var h = window.screen.height; 
		var div_h = h - 374;
		item_list.style.height = div_h;

	} 
</script>