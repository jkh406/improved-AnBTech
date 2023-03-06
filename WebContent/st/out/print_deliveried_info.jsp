<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*,com.anbtech.admin.entity.ApprovalInfoTable"
%>
<%!
	ReservedItemInfoTable table;
	ApprovalInfoTable app_table;
%>
<%
	String mode		= request.getParameter("mode");
	String aid		= request.getParameter("aid");

	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_LIST");
	Iterator table_iter = table_list.iterator();

	//���ڰ��� ����
	app_table = new ApprovalInfoTable();
	app_table = (ApprovalInfoTable)request.getAttribute("Approval_Info");

	String writer_sign		= app_table.getWriterSig()==null?"":app_table.getWriterSig();
	String writer_name		= app_table.getWriterName()==null?"":app_table.getWriterName();
	String reviewer_sign	= app_table.getReviewerSig()==null?"":app_table.getReviewerSig();
	String reviewer_name	= app_table.getReviewerName()==null?"":app_table.getReviewerName();
	String decision_sign	= app_table.getDecisionSig()==null?"":app_table.getDecisionSig();
	String decision_name	= app_table.getDecisionName()==null?"":app_table.getDecisionName();
	String memo				= app_table.getMemo()==null?"":app_table.getMemo();
%>

<html>
<link rel="stylesheet" type="text/css" href="../st/css/style.css">
<head>
<title>����û��</title>
</head>

<body topmargin="5" leftmargin="6">

<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../pu/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">����û��</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
		<a href='Javascript:winprint();'><img src="../pu/images/bt_print.gif" border="0"></a>
		<a href='Javascript:goExcel();'><img src="../pu/images/bt_excel.gif" border="0" alt='�������'></a>
		<a href='Javascript:self.close();'><img src="../pu/images/bt_close.gif" border="0"></a></div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>
<!-- �������� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=52 readOnly style="border:0"><%=memo%></TEXTAREA></TD>
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
<form name="srForm" method="post" action="StockMgrServlet" enctype="multipart/form-data">
	  <TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<TBODY>
				<TR vAlign=middle height=23>
				  <TD noWrap width=30 align=middle class='BG_05'>��ȣ</TD>
				  <TD noWrap width=100 align=middle class='BG_05'>ǰ���ڵ�</TD>
				  <TD noWrap width=100% align=middle class='BG_05'>ǰ�񼳸�</TD>
				  <TD noWrap width=50 align=middle class='BG_05'>��û<br>����</TD>
				  <TD noWrap width=50 align=middle class='BG_05'>���<br>����</TD>
				  <TD noWrap width=50 align=middle class='BG_05'>����</TD>
				  <TD noWrap width=100 align=middle class='BG_05'>�������ù�ȣ</TD>
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
				  <TD align=middle class='list_bg'><%=table.getDeliveryQuantity()%></td>
				  <TD align=middle class='list_bg'><%=table.getDeliveryUnit()%></td>
				  <TD align=middle class='list_bg'><%=table.getRefNo()%></td>			 
				</TR>
	<%
			no++;	
		}
	%>
			</TBODY></TABLE>
	</td></tr></table>
</body>
</html>
<script language=javascript>
//�˾�â ����
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

//��������Ʈ
function goExcel()
{
	wopen('../servlet/StockMgrServlet?mode=print_deliveried_info_excell&aid=<%=aid%>','proxy','850','600','scrollbars=yes,toolbar=no,status=no,resizable=no');
}
</script>
