<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.br.entity.*"
%>

<%!
	CarUseInfoTable table;
	CarLinkTable redirect;
%>

<%  
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("FirstFlag");
	table = new CarUseInfoTable();
	Iterator table_iter = table_list.iterator();
	
	//링크 문자열 가져오기
	redirect = new CarLinkTable();
	redirect = (CarLinkTable)request.getAttribute("Redirect");
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String view_pagecut = redirect.getViewPagecut();

%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../br/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../br/images/blet.gif" align="absmiddle"> 차량예약신청현황</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../br/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../br/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../br/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'><a href="javascript:location.href('../servlet/BookResourceServlet?category=car&mode=view_stat')"><img src="../br/images/bt_confirm.gif" border="0" align="absmiddle"></a></TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--리스트-->
  <TR height=100%>
    <TD vAlign=top><form name=frm method="get"  style='magrgin:0'>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
<!--			  <TD noWrap width=80 align=middle class='list_title'>차종</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>-->
			  <TD noWrap width=100 align=middle class='list_title'>차량번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>모델명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>현상태</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=170 align=middle class='list_title'>사용자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=260 align=middle class='list_title'>배차기간</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>상세정보</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>상신</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	//******************************
	//루프로 table내용을 내보내는 곳 loop  **
	//******************************
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String w_time = vans.format(now);

	while(table_iter.hasNext()){
		table = (CarUseInfoTable)table_iter.next();
		String c_id = table.getCid();
		String cr_id = table.getCrId();
		String user_name = table.getUserName();
		String ac_name = table.getAcName();
		String fellow_names = table.getFellowNames();
		String v_status = table.getVstatus();
		String u_year = table.getUyear();
		String u_month = table.getUmonth();
		String u_date = table.getUdate();
		String u_time = table.getUtime();
		String tu_year = table.getTuYear();
		String tu_month = table.getTuMonth();
		String tu_date = table.getTuDate();
		String tu_time = table.getTuTime();
		String cr_dest = table.getCrDest();
		String car_type = table.getCarType();
		String car_no = table.getCarNo();
		String model_name = table.getModelName();
		String sdate = u_year+"-"+u_month+"-"+u_date+" "+u_time;
		String edate = tu_year+"-"+tu_month+"-"+tu_date+" "+tu_time;

%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
<!--			  <TD align=middle class='list_bg'><%=car_type%></TD>
			  <TD><IMG height=1 width=1></TD>-->
			  <TD align=middle class='list_bg'><%=car_no%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=model_name%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=v_status%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=ac_name%>/<%=user_name%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=sdate%> ~ <%=edate%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><a href="javascript:info(<%=c_id%>,<%=cr_id%>);"><img src='../br/images/lt_view_d.gif' border='0' align='absmiddle'></a></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:location.href('../gw/approval/module/baecha_sincheong_FP_ReApp.jsp?link_id=<%=cr_id%>')"><img src='../br/images/lt_sangsin.gif' border='0' align='absmiddle'></a></TD>
			<TR><TD colSpan=13 background="../br/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>
</BODY>
</HTML>

<script language="javascript">

function goinfo(c_id){
	
	location.href = "../servlet/BookResourceServlet?category=car&mode=eachcar&cid="+c_id;
	
}
function info(cid,cr_id){

	location.href ="../servlet/BookResourceServlet?category=car&mode=carinfo_view&cid="+cid+"&cr_id="+cr_id;	

}
</script>

