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

	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_LIST");
	Iterator table_iter = table_list.iterator();
%>

<html>
<head>
<title>����û��</title>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</head>

<body topmargin="5" leftmargin="6">
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
	<TBODY>
		<TR vAlign=middle height=23>
		  <TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
		  <TD noWrap width=100 align=middle class='list_title'>ǰ���ڵ�</TD>
		  <TD noWrap width=100% align=middle class='list_title'>ǰ�񼳸�</TD>
		  <TD noWrap width=50 align=middle class='list_title'>��û<br>����</TD>
		  <TD noWrap width=50 align=middle class='list_title'>�����<br>����</TD>
		  <TD noWrap width=50 align=middle class='list_title'>���<br>����</TD>
		  <TD noWrap width=50 align=middle class='list_title'>����</TD>
		  <TD noWrap width=100 align=middle class='list_title'>�������ù�ȣ</TD>
		  <TD noWrap width=80 align=middle class='list_title'>�����</TD>
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
