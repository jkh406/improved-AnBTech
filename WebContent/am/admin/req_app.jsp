<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*"%>
<%
	String cno = request.getParameter("c_no")==null?"":request.getParameter("c_no");
	com.anbtech.am.entity.AsInfoTable asInfoTable;
	com.anbtech.am.entity.AsHistoryTable asHistoryTable;

		ArrayList table_list = new ArrayList();
		table_list = (ArrayList)request.getAttribute("assetReqList1");
		asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
		Iterator table_iter = table_list.iterator();

	com.anbtech.am.entity.AMLinkTable amLinkTable;

	//링크 문자열 가져오기
	amLinkTable = new AMLinkTable();
	amLinkTable = (AMLinkTable)request.getAttribute("Redirect");
	String view_total = ""+amLinkTable.getViewTotal();
	String view_boardpage = amLinkTable.getViewBoardpage();
	String view_totalpage = ""+amLinkTable.getViewtotalpage();
	String view_pagecut = amLinkTable.getViewPagecut();
	String category_full = amLinkTable.getWhereCategory();

%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../am/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../am/images/blet.gif" align="absmiddle"> 반출및이관업무(결재건)</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../am/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../am/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../am/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>

					<IMG src="../am/images/bt_view_app.gif" onclick="req_app_list('req_app_list')" align='absmiddle' style='cursor:hand'>
					<IMG src="../am/images/bt_target_out.gif" onclick="javascript:go_TransOutList('TransOutList')" align='absmiddle' style='cursor:hand'>
					<IMG src="../am/images/bt_target_in.gif" onclick="javascript:go_EnteringList('EnteringList')" align='absmiddle' style='cursor:hand'>
					
			  </TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=80 align=middle class='list_title'>신청구분</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>자산번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>적용일자(기간)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=110 align=middle class='list_title'>신청자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=110 align=middle class='list_title'>신청일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>결재상신</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%	
				
		while(table_iter.hasNext()) {
			asHistoryTable = (com.anbtech.am.entity.AsHistoryTable)table_iter.next();

			int h_no = asHistoryTable.getHno();
			String asno = asHistoryTable.getAsNo();
			String wid = asHistoryTable.getWid();
			String wname = asHistoryTable.getWname();
			String wrank = asHistoryTable.getWrank();
			String writedate = asHistoryTable.getWriteDate();
			if(!writedate.equals("0")){
			writedate = writedate.substring(0,4)+"-"+writedate.substring(4,6)+"-"+writedate.substring(6,8);
			} else {writedate="";
			}
				String uid = asHistoryTable.getUid();
			String uname = asHistoryTable.getUname();
			String urank = asHistoryTable.getUrank();

			String out_reason = asHistoryTable.getTakeOutReason();
			String out_destination = asHistoryTable.getOutDestination();
			String u_date = asHistoryTable.getUdate();

			String u_year = asHistoryTable.getUyear();
			String u_month = asHistoryTable.getUmonth();
			String u_day = asHistoryTable.getUday();
			String tu_date = asHistoryTable.getTuDate();
			if(!tu_date.equals("0")){
			tu_date = " ~ "+tu_date.substring(0,4)+"-"+tu_date.substring(4,6)+"-"+tu_date.substring(6,8);
			} else {tu_date="";
			}

			String tu_year = asHistoryTable.getTuYear();
			String tu_month = asHistoryTable.getTuMonth();
			String tu_day = asHistoryTable.getTuDay();
			String in_date = asHistoryTable.getInDate();


			String c_year = asHistoryTable.getCyear();
			String c_month = asHistoryTable.getCmonth();
			String c_day = asHistoryTable.getCday();
			String as_status = asHistoryTable.getAsStatus();
			String o_status = asHistoryTable.getOstatus();
			if(o_status.equals("t")) tu_date="";
			String as_status_name = asHistoryTable.getAsStatusName();
			String o_status_name = asHistoryTable.getOstatusName();
			String asmid = asHistoryTable.getAsMid();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=o_status_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=asmid%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=u_date.substring(0,4) + "-" + u_date.substring(4,6) + "-" + u_date.substring(6,8)%> <%=tu_date%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=wname%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=writedate%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:go_app2('<%=asno%>','<%=h_no%>','<%=as_status%>','<%=o_status%>')"><img src="../am/images/lt_sangshin.gif" border="0"></a></td>
			</TR>
			<TR><TD colSpan=11 background="../am/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>

<script language="javascript">
	function go_app2(as_no,h_no,as_status,o_status){
		
		location.href ="../servlet/AssetServlet?mode=req_app2&as_no="+as_no+"&h_no="+h_no+"&as_status="+as_status+"&o_status="+o_status;

	}
	// 결재건 list
	function req_app_list(mode){
	   location.href ="../servlet/AssetServlet?mode="+mode;
	}

	// 이관/반출 처리건
	function go_EnteringList(mode){
		location.href ="../servlet/AssetServlet?mode="+mode;
	}

	// 입고 처리건
	function go_TransOutList(mode){
		location.href ="../servlet/AssetServlet?mode="+mode;
	}

</script>

