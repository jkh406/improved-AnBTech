<%@ include file="../admin/configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%@ page import="java.util.*,com.anbtech.ca.entity.*,com.anbtech.admin.entity.*"%>
<%@	page import="com.anbtech.admin.SessionLib"%>
<%!
	CaMasterTable master;
	ApprovalInfoTable app_table;
%>

<%
	app_table = new ApprovalInfoTable();
	app_table = (ApprovalInfoTable)request.getAttribute("Approval_Info");

	String writer_sign		= app_table.getWriterSig();
	String writer_name		= app_table.getWriterName();
	String reviewer_sign	= app_table.getReviewerSig();
	String reviewer_name	= app_table.getReviewerName();
	String decision_sign	= app_table.getDecisionSig();
	String decision_name	= app_table.getDecisionName();
	String memo				= app_table.getMemo();

	master = new CaMasterTable();
	master = (CaMasterTable)request.getAttribute("CA_Info");

	String approval_no		= master.getApprovalNo();
	String item_no			= master.getItemNo();
	String item_name			= master.getItemName();
	String approver_info	= master.getApproverInfo();
	String approve_date		= master.getApproveDate();
	String requestor_info	= master.getRequestorInfo();
	String request_date		= master.getRequestDate();
	String maker_code		= master.getMakerCode();
	String maker_name		= master.getMakerName();
	String maker_part_no	= master.getMakerPartNo();
	String item_desc		= master.getItemDesc();
	String prj_code			= master.getPrjCode();
	String prj_name			= master.getPrjName();
	String model_code		= master.getModelCode();
	String model_name		= master.getModelName();
	String item_unit		= master.getItemUnit();
	String approve_type		= master.getApproveType();
	if(approve_type.equals("A")) approve_type = "�հ�";
	else if(approve_type.equals("B")) approve_type = "���Ǻν���";
	else if(approve_type.equals("F")) approve_type = "������";
	String apply_date		= master.getApplyDate()==null?"":master.getApplyDate();
	String apply_quantity	= master.getApplyQuantity()==null?"":master.getApplyQuantity();
	String why_approve		= com.anbtech.text.StringProcess.getContentTxt(master.getWhyApprove());
	String file_link		= master.getFileLink();
	String other_info		= com.anbtech.text.StringProcess.getContentTxt(master.getOtherInfo());

%>

<html>
<head>
<title>���ο�</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ca/css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="700" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../ca/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">���ο�</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
		<a href='Javascript:winprint();'><img src="../ca/images/bt_print.gif" border="0"></a>
		<a href='Javascript:self.close();'><img src="../ca/images/bt_close.gif" border="0"></a></div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- �������� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="700" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=memo%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=50 align=middle class="bg_07">�����</TD>
		<TD noWrap width=50 align=middle class="bg_07">������</TD>
		<TD noWrap width=50 align=middle class="bg_07">������</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><%=writer_sign%></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=reviewer_sign%></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=decision_sign%></TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=writer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=reviewer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=decision_name%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE><BR>

<!-- ���� ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="700" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">���κ�ǰ��ȣ</td>
				<td width="35%" class="bg_06"><%=item_no%></td>
				<td width="15%" height="25" align="middle" class="bg_05">���κ�ǰ��</td>
				<td width="35%" class="bg_06"><%=item_name%></td></tr>
		    <tr>
				<td width="15%" height="25" align="middle" class="bg_05">���κ�ǰ����</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><%=item_desc%></td></tr><table><br>

		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">			
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">���ι�ȣ</td>
				<td width="35%" class="bg_06"><%=approval_no%></td>
				<td width="15%" height="25" align="middle" class="bg_05">���ξ�ü</td>
				<td width="35%" class="bg_06">[<%=maker_code%>] <%=maker_name%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��ü��ǰ��ȣ</td>
				<td width="35%" class="bg_06"><%=maker_part_no%></td>
				<td width="15%" height="25" align="middle" class="bg_05">���α���</td>
				<td width="35%" class="bg_06"><%=approve_type%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��������</td>
				<td width="35%" class="bg_06"><img src='' width='0' height='0'><%=apply_date%></td>
				<td width="15%" height="25" align="middle" class="bg_05">�������</td>
				<td width="35%" class="bg_06"><img src='' width='0' height='0'><%=apply_quantity%></td></tr>
		    <tr>
				<td width="15%" height="25" align="middle" class="bg_05">������������</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><%=why_approve%></td></tr>
		    <tr>
				<td width="15%" height="25" align="middle" class="bg_05">���ù���</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><%=file_link%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">���</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><%=other_info%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><%=approver_info%></td>
				<td width="15%" height="25" align="middle" class="bg_05">�����Ͻ�</td>
				<td width="35%" class="bg_06"><%=approve_date%></td></tr><table><br>


		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">���ð���</td>
				<td width="35%" class="bg_06">[<%=prj_code%>] <%=prj_name%></td>
				<td width="15%" height="25" align="middle" class="bg_05">���ø�</td>
				<td width="35%" class="bg_06">[<%=model_code%>] <%=model_name%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�Ƿ���</td>
				<td width="35%" class="bg_06"><%=requestor_info%></td>
				<td width="15%" height="25" align="middle" class="bg_05">�Ƿ�����</td>
				<td width="35%" class="bg_06"><%=request_date%></td></tr></table>
<!-- ���� ���� �Է� �� -->
	</td></tr></table>
</body></html>

<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
</script>