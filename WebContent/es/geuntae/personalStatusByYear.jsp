<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkES02.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import=		" java.util.*,com.anbtech.es.geuntae.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree"/>

<%!
	GeunTaeInfoTable table;
%>

<%
	String division	= request.getParameter("div");
	String hd_var	= request.getParameter("hd_var");
	String year		= request.getParameter("y");
	
	//리스트 가져오기
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
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../es/images/blet.gif" align="absmiddle"> 부서별 개인 근태현황</TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
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

					
					<SELECT name='hd_var'>
						<OPTION value="">전체</OPTION>
	<%
					String sql = "SELECT code,code_name FROM system_minor_code WHERE type = 'GEUNTAE'";
					bean.openConnection();
					bean.executeQuery(sql);
					while(bean.next()){	
	%>
						<OPTION value="<%=bean.getData("code")%>"><%=bean.getData("code_name")%></OPTION>
	<%				}	
	%>
					</SELECT>

						<%	if(!hd_var.equals("")){	%>
								<script language='javascript'>
									document.sForm.hd_var.value = '<%=hd_var%>';
								</script>
						<%	}	%>

					<SELECT name='year'>
						<OPTION value="2004">2004년</OPTION>
						<OPTION value="2005">2005년</OPTION>
						<OPTION value="2006">2006년</OPTION>
						<OPTION value="2007">2007년</OPTION>
						<OPTION value="2008">2008년</OPTION>
						<OPTION value="2009">2009년</OPTION>
						<OPTION value="2010">2010년</OPTION>
						<OPTION value="2011">2011년</OPTION>
					</SELECT> <a href="javascript:go();"><img src="../es/images/bt_confirm.gif" border="0" align="absmiddle"></a>
				<%	if(!year.equals("")){	%>
						<script language='javascript'>
							document.sForm.year.value = '<%=year%>';
						</script>
				<%	}	%>				

					<input type='hidden' name='mode' value='person_year'>
				</form></TD>
			  <TD width='100%' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--리스트-->
  <TR height=100%>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=60 align=middle class='list_title'>이름</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>직급</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>부서</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=55 align=middle class='list_title'>구분</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>1월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>2월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>3월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>4월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>5월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>6월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>7월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>8월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>9월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>10월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>11월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=31 align=middle class='list_title'>12월</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=38 align=middle class='list_title'>계</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=35></TD></TR></TBODY></TABLE>
	  <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:173; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
<%
	//******************************
	//루프로 table내용을 내보내는 곳 loop  **
	//******************************
		
	while(table_iter.hasNext()){
		table = (GeunTaeInfoTable)table_iter.next();
		String user_rank = table.getUser_rank();
		String user_name = table.getUser_name();
		String ac_name = table.getAc_name();
		String user_id = table.getUser_id();
		String thisyear = table.getThisyear();
		String hdvar = table.getHd_var();
		String orikind = table.getYs_kind();
		String total = table.getSum();
		String kind = table.getKindtokor();
		
		String jan1 = table.getJan1();
		String feb2 = table.getFeb2();
		String mar3 = table.getMar3();
		String apr4 = table.getApr4();
		String may5 = table.getMay5();
		String jun6 = table.getJun6();
		String jul7 = table.getJul7();
		String aug8 = table.getAug8();
		String sep9 = table.getSep9();
		String oct10 = table.getOct10();
		String nov11 = table.getNov11();
		String dec12 = table.getDec12();
		String rest = table.getRest();
		String gsum = table.getSum();
	//	if(orikind.equals("OE_CHUL")) {
	//		total = total.substring(0,total.length()-2);
	//	}
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD noWrap width=60 align=middle height="24" class='list_bg'><%=user_name%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=80 align=middle class='list_bg'><%=user_rank%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=100 align=middle><%=ac_name%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=55 align=middle class='list_bg'><%=kind%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=jan1%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=feb2%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=mar3%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=apr4%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=may5%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=jun6%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=jul7%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=aug8%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=sep9%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=oct10%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=nov11%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=31 align=middle class='list_bg'><%=dec12%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD noWrap width=38 align=middle class='list_bg'><%=gsum%></TD>
			<TR><TD colSpan=36 background="../es/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</BODY>
</HTML>


<script language='javascript'>
	function go(sortby){
		var f = document.sForm;
		
		var division = f.division.value;
		var year = f.year.value;
		var hd_var = f.hd_var.value;
		location.href = "../servlet/GeunTaeServlet?mode=person_year&div="+division+"&hd_var="+hd_var+"&y="+year+"&sortby="+sortby;
	}

	//해상도를 구해서 div의 높이를 설정
	function display() { 
		var w = window.screen.width; 
		var h = window.screen.height; 
		var div_h = h - 374;
		item_list.style.height = div_h;

	} 
</script>