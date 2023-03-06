<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*,com.anbtech.admin.entity.ApprovalInfoTable"
%>
<%!
	RequestInfoTable table;
	PurchaseLinkUrl redirect;
    ApprovalInfoTable app_table;
%>
<%
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();

	app_table = new ApprovalInfoTable();
	app_table = (ApprovalInfoTable)request.getAttribute("Approval_Info");

	String writer_sign		= app_table.getWriterSig()==null?"":app_table.getWriterSig();
	String writer_name		= app_table.getWriterName()==null?"":app_table.getWriterName();
	String reviewer_sign	= app_table.getReviewerSig()==null?"":app_table.getReviewerSig();
	String reviewer_name	= app_table.getReviewerName()==null?"":app_table.getReviewerName();
	String decision_sign	= app_table.getDecisionSig()==null?"":app_table.getDecisionSig();
	String decision_name	= app_table.getDecisionName()==null?"":app_table.getDecisionName();
	String memo				= app_table.getMemo()==null?"":app_table.getMemo();

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
<TITLE>���γ�������</TITLE>
</HEAD>

<BODY topmargin="5" leftmargin="5">
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../pu/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">�����Ƿڼ�</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
		<a href='Javascript:winprint();'><img src="../pu/images/bt_print.gif" border="0"></a>
		<a href='Javascript:self.close();'><img src="../pu/images/bt_close.gif" border="0"></a></div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>
<!-- �������� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=53 readOnly style="border:0"><%=memo%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=50 align=middle class="bg_07">�����</TD>
		<TD noWrap width=50 align=middle class="bg_07">������</TD>
		<TD noWrap width=50 align=middle class="bg_07">������</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><%=writer_sign%><img width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=reviewer_sign%><img width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=decision_sign%><img width='0' height='0'></TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=writer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=reviewer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=decision_name%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE><BR>

<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">���ſ�û��ȣ</td>
				<td width="35%" class="bg_06">&nbsp;<%=request_no%></td>
				<td width="15%" height="25" align="middle" class="bg_05">���ſ�û����</td>
				<td width="35%" class="bg_06">&nbsp;<%=request_date%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">���ſ�û��</td>
				<td width="35%" class="bg_06">&nbsp;<%=requester_info%></td>
				<td width="15%" height="25" align="middle" class="bg_05">���ſ�û�μ�</td>
				<td width="35%" class="bg_06">&nbsp;<%=requester_div_name%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�����ڵ�</td>
				<td width="35%" class="bg_06"><img src='' width='0' height='0'>&nbsp;<%=project_code%></td>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><img src='' width='0' height='0'>&nbsp;<%=project_name%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��û����</td>
				<td width="35%" class="bg_06">&nbsp;<%=request_type%></td>
				<td width="15%" height="25" align="middle" class="bg_05">���ſ���ݾ�</td>
				<td width="35%" class="bg_06">&nbsp;<%=request_total_mount%> ��</td></tr><table><br>

	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<TBODY>
				<TR vAlign=middle height=23>
				  <TD noWrap width=100 align=middle class='bg_05'>ǰ���ڵ�</TD>
				  <TD noWrap width=100% align=middle class='bg_05'>ǰ�񼳸�</TD>
				  <TD noWrap width=70 align=middle class='bg_05'>�ܰ�</TD>
				  <TD noWrap width=40 align=middle class='bg_05'>����</TD>
				  <TD noWrap width=40 align=middle class='bg_05'>����</TD>
				  <TD noWrap width=80 align=middle class='bg_05'>�ݾ�</TD>
				  <TD noWrap width=100 align=middle class='bg_05'>����԰���</TD>
				</TR>
			
	<%
		int no = 1;
		while(table_iter.hasNext()){
			table = (RequestInfoTable)table_iter.next();
	%>
				<TR vAlign=middle height=23>
					  <TD align=middle class='bg_06' height="25"><%=table.getItemCode()%></TD>
					  <TD align=left class='bg_06' style='padding-left:5px'>&nbsp;<%=table.getItemDesc()%></TD>
					  <TD align=right class='bg_06' style='padding-right:5px'>&nbsp;<%=sp.getMoneyFormat(table.getSupplyCost(),"")%></TD>
					  <TD align=middle class='bg_06'>&nbsp;<%=table.getRequestQuantity()%></TD>
					  <TD align=middle class='bg_06'>&nbsp;<%=table.getRequestUnit()%></TD>
					  <TD align=right class='bg_06' style='padding-right:5px'>&nbsp;<%=sp.getMoneyFormat(table.getRequestCost(),"")%></TD>
					  <TD align=middle class='bg_06'>&nbsp;<%=table.getDeliveryDate()%><img width='0' height='0'></TD>
				</TR>
	<%
			no++;	
		}
	%>
			</TBODY></TABLE>
	</td></tr></table>
</body>
</html>

<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
</script>