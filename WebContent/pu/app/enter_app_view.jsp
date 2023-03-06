<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>

<%!
	EnterInfoTable table;
	PurchaseLinkUrl redirect;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
%>

<%
	String mode = request.getParameter("mode");	// ���

	table = (EnterInfoTable)request.getAttribute("ENTER_INFO");
	String enter_no				= table.getEnterNo();
	String supplyer_code		= table.getSupplyerCode();
	String supplyer_name		= table.getSupplyerName();
	String enter_date			= table.getEnterDate();
	String enter_total_mount	= sp.getMoneyFormat(table.getEnterTotalMount(),"");
	String requestor_div_code	= table.getRequestorDivCode();
	String requestor_div_name	= table.getRequestorDivName();
	String requestor_id			= table.getRequestorId();
	String requester_info		= table.getRequestorInfo();
	String enter_type			= table.getEnterType();
	String monetary_unit		= table.getMonetaryUnit();
	String filelink				= table.getFileLink();

	//����Ʈ ��������
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new EnterInfoTable();
	Iterator table_iter = item_list.iterator();
%>
<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display()">
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰��ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=enter_no%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰�����</td>
           <td width="37%" height="25" class="bg_04"><%=enter_date%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���޾�ü�ڵ�</td>
           <td width="37%" height="25" class="bg_04"><%=supplyer_code%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���޾�ü��</td>
           <td width="37%" height="25" class="bg_04"><%=supplyer_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰�����</td>
           <td width="37%" height="25" class="bg_04"><%=requester_info%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�԰���μ�</td>
           <td width="37%" height="25" class="bg_04"><%=requestor_div_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>

		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�����Ѿ�</td>
           <td width="37%" height="25" class="bg_04"><%=enter_total_mount%></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">÷������</td>
           <td width="37%" height="25" class="bg_04"><%=filelink%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>

<!-- �԰�ǰ�� -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../pu/images/title_input_item.gif' border='0' alt='�԰�ǰ��'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:168; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>ǰ�񼳸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>�԰����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>�԰����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�԰�ܰ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���Աݾ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>ó������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���ſ�û��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���Ź��ֹ�ȣ</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (EnterInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getEnterQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getEnterUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getUnitCost(),"")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getEnterCost(),"")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRequestNo()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getOrderNo()%></td>
			</TR>
			<TR><TD colSpan=19 background="../pu/images/dot_line.gif"></TD></TR>
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
