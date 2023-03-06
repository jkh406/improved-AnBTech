<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%!
	RequestInfoTable table;
	PurchaseLinkUrl redirect;
%>
<%
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();

	String mode = request.getParameter("mode");	// ���

	table = (RequestInfoTable)request.getAttribute("REQUEST_INFO");
	String request_no			= table.getRequestNo();
	String request_date			= table.getRequestDate();
	String requester_div_code	= table.getRequesterDivCode();
	String requester_div_name	= table.getRequesterDivName();
	String requester_id			= table.getRequesterId();
	String requester_info		= table.getRequesterInfo();
	String request_total_mount  = sp.getMoneyFormat(table.getRequestTotalMount(),"");
	String request_type			= table.getRequestType();
	String project_code			= table.getProjectCode();	// �����ڵ�(�����߰�)
	String project_name			= table.getProjectName();	// ������(�����߰�)
	
	//����Ʈ ��������
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new RequestInfoTable();
	Iterator table_iter = item_list.iterator();	
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../pu/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0" onLoad="display()">

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ�û��ȣ</TD>
           <TD width="37%" height="25" class="bg_04"><%=request_no%></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ�û����</TD>
           <TD width="37%" height="25" class="bg_04"><%=request_date%></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
		   <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ�û��</TD>
           <TD width="37%" height="25" class="bg_04"><%=requester_info%></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ�û�μ�</TD>
           <TD width="37%" height="25" class="bg_04"><%=requester_div_name%></TD>
           </TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�����ڵ�</TD>
           <TD width="37%" height="25" class="bg_04"><%=project_code%></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">������</TD>
           <TD width="37%" height="25" class="bg_04"><%=project_name%></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">��û����</TD>
           <TD width="37%" height="25" class="bg_04"><%=request_type%></TD>
		   <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ���ݾ�</TD>
           <TD width="37%" height="25" class="bg_04"><%=request_total_mount%> ��</TD>   
	    </TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	    </TBODY></TABLE>
<!-- ���ſ�ûǰ�� -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../pu/images/request_item.gif' border='0' alt='���ſ�ûǰ��'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>ǰ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>�ܰ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>�ݾ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>������־�ü��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>����԰���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>����ּ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>���԰����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>÷��ȭ��</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (RequestInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemName()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getSupplyCost(),"")%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRequestQuantity()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRequestUnit()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getRequestCost(),"")%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getSupplyerName()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveryDate()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getOrderQuantity()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveryQuantity()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getFileLink()%></TD>
			</TR>
			<TR><TD colSpan=25 background="../pu/images/dot_line.gif"></TD></TR>
<%
		no++;	
	}
%>
		</TBODY></TABLE></DIV>
</body>
</html>

<script language="javascript">
//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 590;
	item_list.style.height = div_h;
}
</script>
