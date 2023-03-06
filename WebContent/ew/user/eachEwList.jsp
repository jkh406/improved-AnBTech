<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.ew.entity.*,com.anbtech.date.anbDate,com.anbtech.text.Hanguel"
%>

<%
	ExtraWorkHistoryTable ewHistoryTable;
	EWLinkTable ewLinkTable;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	
	// 특근 신청 리스트 정보
	ArrayList arry = new ArrayList();
	arry = (ArrayList)request.getAttribute("ew_array");
	Iterator itr = arry.iterator();
	int nsize = arry.size();

	//링크 문자열 가져오기
	ewLinkTable = new com.anbtech.ew.entity.EWLinkTable();
	ewLinkTable = (com.anbtech.ew.entity.EWLinkTable)request.getAttribute("Redirect");
	String view_total = ""+ewLinkTable.getViewTotal();
	String view_boardpage = ewLinkTable.getViewBoardpage();
	String view_totalpage = ""+ewLinkTable.getViewtotalpage();
	String view_pagecut = ewLinkTable.getViewPagecut();
%>
<HTML>
<HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../ew/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<FORM method='get' name='srForm' >
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign=top>
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
					<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD">
						<TD valign='middle' class="title"><img src="../ew/images/blet.gif" align="absmiddle"> 개인 특근신청 승인현황</TD>
						<TD style="padding-right:10px" align='right' valign='middle'><img src="../ew/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../ew/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../ew/images/setup_pages_nowpage.gif" border="0" align="absmiddle">
		  </TR></TBODY></TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=32><!--버튼 및 페이징-->
			<TD vAlign=top>
				<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
				<TBODY>
					<TR><TD width=4>&nbsp;</TD>
						<TD align=left width='500'><IMG src='../ew/images/bt_del.gif' onClick='javascript:del_selected();' style='cursor:hand' align='absmiddle' alt="선택항목삭제"></TD>
						<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
						</TABLE></TD></TR>
		<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
		<TR height=100%><!--리스트-->
			<TD vAlign=top>
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=23>
						<TD noWrap width=30 align=middle class='list_title'><input type="checkbox" name="checkbox" onClick="check(document.srForm.checkbox)"></TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>특근일자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
						<TD noWrap width=110 align=middle class='list_title'>특근시간</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
						<TD noWrap width=250 align=middle class='list_title'>특근사유</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
						<TD noWrap width=70 align=middle class='list_title'>근무시간</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>신청일자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>진행상태</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>상세보기</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'></TD>
					<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
<%		
		while(itr.hasNext()) {
			ewHistoryTable = (ExtraWorkHistoryTable)itr.next();

			String o_no			= ewHistoryTable.getOno()+"";
			String status		= ewHistoryTable.getStatus();
			String status_name	= ewHistoryTable.getStatusName();
			String w_sdate		= ewHistoryTable.getWsdate();
			w_sdate = w_sdate.substring(0,4) + "-" + w_sdate.substring(4,6) + "-" + w_sdate.substring(6,8);
			String w_edate		= ewHistoryTable.getWedate();
			String w_stime		= ewHistoryTable.getWstime();
			String w_etime		= ewHistoryTable.getWetime();
			String c_date		= ewHistoryTable.getCdate();
			c_date = c_date.substring(0,4) + "-" + c_date.substring(4,6) + "-" + c_date.substring(6,8);
			String r_sdate		= ewHistoryTable.getRsdate();
			String r_stime		= ewHistoryTable.getRstime();
			String r_edate		= ewHistoryTable.getRedate();
			String r_etime		= ewHistoryTable.getRetime();
			String duty			= ewHistoryTable.getDuty();
			String total_time	= ewHistoryTable.getTotalTime();
			String pay_by_work	= sp.getMoneyFormat(ewHistoryTable.getPayByWork(),"");
			String view_ew_info	= "<a href=\"javascript:view_info('" + o_no + "');\"><IMG src='../ew/images/lt_view_d.gif' border='0'></a>";
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%if(status.equals("1") || status.equals("4")){%><input type="checkbox" name="checkbox" value="<%=o_no%>"><%}%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=w_sdate%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=w_stime%> ~ <%=w_etime%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style="padding-left:5px"><%=duty%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=total_time%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=c_date%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=status_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=view_ew_info%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			  <TD><IMG height=1 width=1></TD>
			</TR>
			<TR><TD colSpan=17 background="../ew/images/dot_line.gif"></TD></TR>
<%	}%>
</TBODY></TABLE></TD></TR></TBODY></TABLE>
</form>
</body>
</html>

<script language="javascript">
var checkflag = false; 

function check(field) { 
	if (checkflag == false) { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = true; 
		} 
	checkflag = true; 
	}else { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = false; 
		} 
	checkflag = false; 
	} 
}

function del_selected() {
	var f = document.srForm.checkbox;
	var items = "";
	var s_count = 0;
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			items += f[i].value+";";
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("대상을 선택하십시오.");
	   return;
    }
	var c = confirm("선택항목을 삭제하시겠습니까?");
	if(c) location.href = "../servlet/ExtraWorkServlet?mode=delete_ew_info&ono_plus="+items
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function view_info(no)
{
	wopen("../servlet/ExtraWorkServlet?mode=print&o_no="+no,'print','660','600','scrollbars=yes,toolbar=no,status=no,resizable=no');
}
</script>