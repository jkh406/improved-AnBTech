<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkES02.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import=		" java.util.*,com.anbtech.es.geuntae.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%!
	GeunTaeInfoTable table;
%>

<%
	String year = request.getParameter("y");
	String kind = request.getParameter("k").equals("null")?"00":request.getParameter("k");

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
			  <TD valign='middle' class="title"><img src="../es/images/blet.gif" align="absmiddle"> �μ��� ������Ȳ</TD></TR></TBODY>
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
				<SELECT name='year'>
					<OPTION value="">�⵵ ����</OPTION>
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
				<SELECT name='kind'>
					<OPTION value="">��ü</OPTION>
			<%
				String sql = "SELECT code,code_name FROM system_minor_code WHERE type = 'GEUNTAE'";
				bean.openConnection();
				bean.executeQuery(sql);
				while(bean.next()){	
			%>
					<OPTION value="<%=bean.getData("code")%>"><%=bean.getData("code_name")%></OPTION>
			<%	}	%>
				</SELECT> <a href="javascript:go();"><img src="../es/images/bt_confirm.gif" border="0" align="absmiddle"></a>
			<%	if(!kind.equals("")){	%>
					<script language='javascript'>
						document.sForm.kind.value = '<%=kind%>';
					</script>
			<%	}	%>
				<input type='hidden' name='mode' value='div_month'>
				</form></TD>
			  <TD width='100%' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--����Ʈ-->
  <TR height=100%>
    <TD align="left" valign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=150 align=middle class='list_title'>�μ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>1��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>2��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>3��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>4��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>5��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>6��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>7��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>8��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>9��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>10��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>11��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=34 align=middle class='list_title'>12��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=31></TD></TR></TBODY></TABLE>
	  <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:173; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
<%
	while(table_iter.hasNext()){
		table = (GeunTaeInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD noWrap width=150 align=middle height="24" class='list_bg'><%=table.getDepartment()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=120 align=middle class='list_bg'><%=table.getHd_var()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getJan1()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getFeb2()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getMar3()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getApr4()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getMay5()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getJun6()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getJul7()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getAug8()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getSep9()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getOct10()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getNov11()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=34 align=middle class='list_bg'><%=table.getDec12()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=50 align=middle class='list_bg'><%=table.getSum()%></TD>
			<TR><TD colSpan=29 background="../es/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</BODY>
</HTML>


<script language='javascript'>
	function go(){
		var f = document.sForm;
		var year = f.year.value;
		var kind = f.kind.value;
		location.href = "../servlet/GeunTaeServlet?mode=div_month&y="+year+"&k="+kind;
	}

	//�ػ󵵸� ���ؼ� div�� ���̸� ����
	function display() { 
		var w = window.screen.width; 
		var h = window.screen.height; 
		var div_h = h - 374;
		item_list.style.height = div_h;

	} 
</script>