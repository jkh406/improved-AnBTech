<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.ew.entity.*,com.anbtech.admin.entity.*"
%>

<%!
	UserInfoTable user_info;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>

<%
	String year = request.getParameter("y");
	String month = request.getParameter("m");
	String div = request.getParameter("div")==null?"":request.getParameter("div");

	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	com.anbtech.ew.entity.ExtraWorkHistoryTable table = new com.anbtech.ew.entity.ExtraWorkHistoryTable();
	Iterator table_iter = table_list.iterator();

	//����� ���� ��������
	user_info = new UserInfoTable();
	user_info = (UserInfoTable)request.getAttribute("User_Info");
	String eachid = user_info.getUserId();
	String ac_name = user_info.getDivision();
	String user_rank = user_info.getUserRank();
	String user_name = user_info.getUserName();
	String rest_year = user_info.getHyuGaYearRestDay();
	String rest_month = user_info.getHyuGaMonthRestDay();
	
%>


<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../ew/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false" onLoad="display();">

<form method="get" name="eForm" action="../servlet/ExtraWorkServlet" style="margin:0">
<input type='hidden' name='dive' value='view'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27>
    <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../es/images/blet.gif" align="absmiddle"> ����Ư�� ó����Ȳ (<%=ac_name%>/<%=user_rank%>/<%=user_name%>)</TD></TR></TBODY>
		</TABLE></TD></TR>
 
    <TD vAlign=top><!--��ư �� ����¡-->
	 <TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'>
				<SELECT name='year'>
					<OPTION value="2004">2004��</OPTION>
					<OPTION value="2005">2005��</OPTION>
					<OPTION value="2006">2006��</OPTION>
					<OPTION value="2007">2007��</OPTION>
					<OPTION value="2008">2008��</OPTION>
					<OPTION value="2009">2009��</OPTION>
					<OPTION value="2010">2010��</OPTION>
					<OPTION value="2011">2011��</OPTION>
				</SELECT>&nbsp;
			<%	if(!year.equals("")){	%>
					<script language='javascript'>
						document.eForm.year.value = '<%=year%>';
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
						document.eForm.month.value = '<%=month%>';
					</script>
			<%	}	%>
			<a href="javascript:go()"><img src="../ew/images/bt_confirm.gif" border="0" alt="����" align="absmiddle"></a>
		  </TD>
		  <TD width=4></TD>
          <TD align=middle width=150></TD>
          <TD width=4></TD>
		  <TD align=middle width=150></TD>
          <TD><IMG height=1 width=1></TD>
          <TD vAlign=bottom align=right>
            <TABLE height=29 cellSpacing=0 cellPadding=2 border=0>
              <TBODY>
              <TR>
                <TD noWrap></TD></TR></TBODY></TABLE></TD>
          <TD width=4></TD></TR></TBODY></TABLE>
  </TD></TR>
 <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
 <!--����Ʈ-->
  <TR height=100%>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=center height=25>
			  <TD noWrap width=80 align=middle class='list_title'>��¥</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>Ư�ٽ�û�ð�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>Ư�ٻ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>����ð�(h)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>Ư�ټ���(��)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
			  <TD noWrap width=97% align=middle class='list_title'></TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR></TBODY></TABLE>
	  <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:173; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
<%
	while(table_iter.hasNext()){
		table = (ExtraWorkHistoryTable)table_iter.next();
	
		String day			= table.getDay();
		String w_stime		= table.getWstime();
		String w_etime		= table.getWetime();
		String w_time = "";
		if(!w_stime.equals("") && !w_etime.equals("")) w_time = w_stime + " ~ " + w_etime;
		String duty			= table.getDuty();
		String total_time	= table.getTotalTime();
		String result_time	= table.getResultTime();
		String pay_by_work	= table.getPayByWork().equals("")?"":sp.getMoneyFormat(table.getPayByWork(),"");
		String confirm_date	= table.getConfirmDate().equals("")?"":table.getConfirmDate().substring(0,4) + "-" + table.getConfirmDate().substring(4,6) + "-" + table.getConfirmDate().substring(6,8);
%>
<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD noWrap width=80 align=middle height="22" class='list_bg'><%=day%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=150 align=middle class='list_bg'><%=w_time%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=150 align=middle class='list_bg'><%=duty%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=80 align=middle class='list_bg'><%=result_time%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=100 align=right class='list_bg' style='padding-right:5px'><%=pay_by_work%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=100 align=middle class='list_bg'><%=confirm_date%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=100% align=middle class='list_bg'></TD>
			  <TD><IMG height=1 width=1></TD>
			</TR>
			<TD colSpan=13 background="../es/images/dot_line.gif"></TD></TR>		
<%
	}
%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</form>
</body>
</html>

<script language='javascript'>
	function go(){
		
		var f = document.eForm;
		var year = f.year.value;
		var month = f.month.value;

		location.href = "../servlet/ExtraWorkServlet?mode=person_month&y="+year+"&m="+month;
	}

	//�ػ󵵸� ���ؼ� div�� ���̸� ����
	function display() { 
		var w = window.screen.width; 
		var h = window.screen.height; 
		var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
		//var div_h = h - 375 ;
		var div_h = c_h - 88;
		item_list.style.height = div_h;

	} 
</script>