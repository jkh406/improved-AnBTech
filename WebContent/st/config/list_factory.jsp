<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%
	FactoryInfoTable factoryTable = new FactoryInfoTable();
	ArrayList type_list = new ArrayList();
	type_list = (ArrayList)request.getAttribute("LIST_FACTORY");
	Iterator type_iter = type_list.iterator();
%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../st/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../st/images/blet.gif" align="absmiddle"> 공장현황</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'><a href="javascript:add()"><IMG src='../st/images/bt_add.gif' border='0' align='absmiddle'></a></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>공장코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>공장명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>생산타입</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>주요생산품목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>사업장</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>관리</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=15</TD></TR>
<%
	while(type_iter.hasNext()) {
		factoryTable = (FactoryInfoTable)type_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=factoryTable.getMid()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=factoryTable.getFactoryCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=factoryTable.getFactoryName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=factoryTable.getProductionType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=factoryTable.getMainProduct()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=factoryTable.getAgencyName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'>
				  <a href="javascript:modify_factory_info('<%=factoryTable.getMid()%>')"><IMG src='../st/images/lt_modify.gif' border='0' align='absmiddle'></a>
				  <a href="javascript:delete_factory_info('<%=factoryTable.getMid()%>')"><IMG src='../st/images/lt_del.gif' border='0' align='absmiddle'></a>
			  </td>			 
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'>
			</TR>
			<TR><TD colSpan=15 background="../st/images/dot_line.gif"></TD></TR>
<%
	}
%>			
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>


<SCRIPT language='javascript'>

function add(){
	location.href="../servlet/StockConfigMgrServlet?mode=write_factory_info";
}

function modify_factory_info(mid){
	location.href="../servlet/StockConfigMgrServlet?mode=modify_factory_info&mid="+mid;
}

function delete_factory_info(mid){
	if(confirm("정말 삭제 하시겠습니까?")) {
	location.href="../servlet/StockConfigMgrServlet?mode=delete_factory_info&mid="+mid;
	}
}


</script>