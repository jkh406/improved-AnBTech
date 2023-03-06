<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "정전개 Report Excel 출력"		
	contentType = "application/vnd.ms-excel; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.bm.entity.mbomStrTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("0.0");		//출력형태

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String model_code = (String)request.getAttribute("model_code");
	String fg_code = (String)request.getAttribute("fg_code");
	
	//-----------------------------------
	//	정전개 PART 리스트
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD><title>리포트</title>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' colspan="7"> BOM 리포트</TD>
			</TR></TBODY>
		</TABLE></TD></TR>
  <TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<td align=left height="25" colspan="7">
				모델코드 : <%=model_code%>
			    FG코드 : <%=fg_code%></td>
				</TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
<!--리스트-->
  <TR height=100%>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=1>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=30 align=middle >LEVEL</TD>
			  <TD noWrap width=100 align=middle >모품목코드</TD>
			  <TD noWrap width=100 align=middle >자품목코드</TD>
			  <TD noWrap width=350 align=middle >품목규격</TD>
			  <TD noWrap width=300 align=middle >Location No</TD>
			  <TD noWrap width=40 align=middle >단위</TD>
			  <TD noWrap width=40 align=middle >갯수</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=7></TD></TR>

	<%  
		while(table_iter.hasNext()) {
			table = (mbomStrTable)table_iter.next();
			int lv = Integer.parseInt(table.getLevelNo());
			String space="&nbsp;";
			for(int i=1; i<lv; i++) space += "&nbsp;";
	%>	
			<TR bgColor=#ffffff>
			  <TD align=left height="24"><%=space%><%=table.getLevelNo()%></TD>
			  <TD align=left class='list_bg'><%=table.getParentCode()%></TD>
			  <TD align=left class='list_bg'><%=table.getChildCode()%></TD>
			  <TD align=left class='list_bg'><%=table.getPartSpec()%></TD>
			  <TD align=left class='list_bg'><%=table.getLocation()%></TD>
			  <TD align=middle class='list_bg'><%=table.getQtyUnit()%></TD>
			  <TD align=middle class='list_bg'><%=table.getQty()%></TD>
			</TR>
	<% 
	
		}  //while 

	%>
</TBODY></TABLE></TD></TR></TBODY></TABLE>

</body>
</html>

