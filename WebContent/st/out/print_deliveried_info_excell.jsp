<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "application/vnd.ms-excel; charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*,com.anbtech.admin.entity.ApprovalInfoTable"
%>
<%!
	ReservedItemInfoTable table;
	ApprovalInfoTable app_table;
%>
<%
	String mode		= request.getParameter("mode");

	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_LIST");
	Iterator table_iter = table_list.iterator();
%>

<html>
<head>
<title>출고요청서</title>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</head>

<body topmargin="5" leftmargin="6">
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
	<TBODY>
		<TR vAlign=middle height=23>
		  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
		  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
		  <TD noWrap width=100% align=middle class='list_title'>품목설명</TD>
		  <TD noWrap width=50 align=middle class='list_title'>요청<br>수량</TD>
		  <TD noWrap width=50 align=middle class='list_title'>기출고<br>수량</TD>
		  <TD noWrap width=50 align=middle class='list_title'>출고<br>수량</TD>
		  <TD noWrap width=50 align=middle class='list_title'>단위</TD>
		  <TD noWrap width=100 align=middle class='list_title'>생산지시번호</TD>
		  <TD noWrap width=80 align=middle class='list_title'>공장명</TD>
		</TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (ReservedItemInfoTable)table_iter.next();
%>
		<TR vAlign=middle height=23>
		  <TD align=middle height="24" class='list_bg'><%=no%></TD>
		  <TD align=middle class='list_bg'><%=table.getItemCode()%></TD>
		  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
		  <TD align=middle class='list_bg'><%=table.getRequestQuantity()%></td>
		  <TD align=middle class='list_bg'><%=table.getDeliveriedQuantity()%></td>
		  <TD align=middle class='list_bg'><%=table.getDeliveryQuantity()%></td>
		  <TD align=middle class='list_bg'><%=table.getDeliveryUnit()%></td>
		  <TD align=middle class='list_bg'><%=table.getRefNo()%></td>	
		  <TD align=middle class='list_bg'><%=table.getFactoryName()%></td>

		</TR>
<%
		no++;	
	}
%>
</TBODY></TABLE>
</body>
</html>
