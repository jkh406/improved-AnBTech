<%@ include file="../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.ca.entity.*,com.anbtech.cm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	CaMasterTable master;
	CaHistoryInfoTable history;
	CaLinkUrl redirect;
	PartInfoTable part;
%>

<%
	String no = request.getParameter("no");
	String item_no = request.getParameter("item_no");

	//품목정보 가져오기
	part = (PartInfoTable)request.getAttribute("PART_INFO");
	
	//history_info 에서 가져오기
	ArrayList history_list = new ArrayList();
	history_list = (ArrayList)request.getAttribute("History_List");
	history = new CaHistoryInfoTable();
	Iterator history_iter = history_list.iterator();
	
	//ca_master 에서 가져오기
	ArrayList master_list = new ArrayList();
	master_list = (ArrayList)request.getAttribute("Approval_List");
	master = new CaMasterTable();
	Iterator master_iter = master_list.iterator();
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../ca/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ca/images/blet.gif" align="absmiddle"> 부품승인상세정보</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
					<a href="javascript:history.go(-1);"><img src="../ca/images/bt_list.gif" border="0" align="absmiddle"></a>
					<a href="javascript:add_comp('<%=no%>','<%=item_no%>');"><img src="../ca/images/bt_add_comp.gif" border="0" align="absmiddle"></a>
			  </TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>

  <TR><!--품목정보-->
    <TD valign=top>
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>		 
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인부품번호</td>
           <td width="37%" height="25" class="bg_04"><%=part.getItemNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인부품명</td>
           <td width="37%" height="25" class="bg_04"><%=part.getItemName()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인부품설명</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=part.getItemDesc()%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table></td></tr>

  <TR><TD height="25"><img src="../ca/images/history_u.gif" width="209" height="25" border="0"></TD></TR>
  <TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>		 
  <TR><!--리스트-->
    <TD valign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'>NO</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>변경사항</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>적용일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>담당자</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=7></TD></TR>
		<%
			int i = 1;
			while(history_iter.hasNext()){
				history = (CaHistoryInfoTable)history_iter.next();
		%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=i%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'>&nbsp;<%=history.getContents()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=history.getApplyDate()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=history.getRequestorInfo()%></td>
			</TR>
			<TR><TD colSpan=7 background="../ca/images/dot_line.gif"></TD></TR>
<%
			i++;
	}
%>
		</TBODY></TABLE></TD></TR>
  <TR><TD height="25"></TD></TR>
  <TR><TD height="25"><img src="../ca/images/ca_list.gif" width="209" height="25" border="0"></TD></TR>
  <TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
  <TR height="100%"><!--리스트-->
    <TD valign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=120 align=middle class='list_title'>승인번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>승인업체</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>승인구분</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=180 align=middle class='list_title'>승인자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>승인일자</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
		<%
			while(master_iter.hasNext()){
				master = (CaMasterTable)master_iter.next();
		%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=master.getApprovalNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>[<%=master.getMakerCode()%>] <%=master.getMakerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=master.getApproveType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=master.getApproverInfo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=master.getApproveDate()%></td>
			</TR>
			<TR><TD colSpan=9 background="../ca/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>

<script language='javascript'>
//승인업체추가
function add_comp(no,item_no){
	location.href='../servlet/ComponentApprovalServlet?mode=write_a&no='+no+'&item_no='+item_no;
}
</script>