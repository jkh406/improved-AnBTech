<%@ include file= "../../admin/configHead.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.es.geuntae.entity.*,com.anbtech.admin.entity.*,com.anbtech.text.Hanguel"%>
<%!
	GeunTaeInfoTable table;
	UserInfoTable user_info;
%>

<%
	String year = request.getParameter("y");
	String month = request.getParameter("m");

	//������Ȳ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new GeunTaeInfoTable();
	Iterator table_iter = table_list.iterator();

	//��������� �� ��������� ��������
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
			  <TD valign='middle' class="title"><img src="../es/images/blet.gif" align="absmiddle"> �������α�����Ȳ (<%=ac_name%>/<%=user_rank%>/<%=user_name%>)</TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
					<form method="get" name="sForm" action="../servlet/GeunTaeServlet" style="margin:0">
					<input type='hidden' name='mode' value='person_month'>
					<input type="hidden" name='eachid' value='<%=eachid%>'>
					<input type="hidden" name='a' value='<%=ac_name%>'>
					<input type="hidden" name='r' value='<%=user_rank%>'>
					<input type="hidden" name='n' value='<%=user_name%>'>
					<SELECT name='year'>
						<OPTION value="2000">2000��</OPTION>
						<OPTION value="2001">2001��</OPTION>
						<OPTION value="2002">2002��</OPTION>
						<OPTION value="2003">2003��</OPTION>
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
					</SELECT> <a href="javascript:go();"><img src="../es/images/bt_confirm.gif" border="0" align="absmiddle"></a>
				<%	if(!month.equals("")){	%>
						<script language='javascript'>
							document.sForm.month.value = '<%=month%>';
						</script>
				<%	}	%>
				</form></TD>
			  <TD width='500' align='right' style="padding-right:10px" class='list_bg'><img src='../es/images/bt_vac_y.gif' border='0' align='absmiddle'> <%=rest_year%> <img src='../es/images/bt_vac_m.gif' border='0' align='absmiddle'> <%=rest_month%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--����Ʈ-->
  <TR height=100%>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=100 align=middle class='list_title'>��¥</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���±���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>���»���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>��ٽð�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>��ٽð�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=150% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR></TBODY></TABLE>
	  <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:173; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
<%
	int k=1;
	while(table_iter.hasNext()){
		table = (GeunTaeInfoTable)table_iter.next();
		String day = table.getDay();
		String ys_kind = table.getYs_kind();
		String gt_purpose = table.getReason();
		String time_s = table.getTimeS();
		String time_e = table.getTimeE();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD noWrap width=100 align=middle height="24" class='list_bg'><%=day%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=100 align=middle class='list_bg'><%=ys_kind%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=220 align=left class='list_bg' style='padding-left:10px'><%=gt_purpose%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=120 align=middle class='list_bg'><%=time_s%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=120 align=middle class='list_bg'><%=time_e%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=100% align=middle class='list_bg'></TD>
			<TR><TD colSpan=11 background="../es/images/dot_line.gif"></TD></TR>
<%		k++;
	} 

%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</BODY>
</HTML>

<script language='javascript'>
	function go(){
		
		var f = document.sForm;
		var year = f.year.value;
		var month = f.month.value;
		var id = f.eachid.value;
		var ac_name = f.a.value;
		var user_rank = f.r.value;
		var user_name = f.n.value;
	
		location.href = "../servlet/GeunTaeServlet?mode=person_month&y="+year+"&m="+month+"&eachid="+id+"&ac_name="+ac_name+"&user_rank="+user_rank+"&user_name="+user_name;
	}

	function w_confirm(type){
		var text;
		var url;
		if(type == "in"){
			text = "���";
			url = "../servlet/GeunTaeServlet?mode=chk_in";
		}
		else{
			text = "���";
			url = "../servlet/GeunTaeServlet?mode=chk_out"
		}

		var c = confirm(text+" �ð��� ����Ͻðڽ��ϴ�.");

		if(c == true){
			location.href = url;
		}else{
		}
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


