<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ ���������� �����ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//����

	String mfg_count = (String)request.getAttribute("mfg_count"); 
	if(mfg_count == null) mfg_count = "0";

	//--------------------------------------
	//���������� ������ ������ ��������
	//--------------------------------------
	com.anbtech.mm.entity.mfgProductMasterTable master = new com.anbtech.mm.entity.mfgProductMasterTable();
	master = (mfgProductMasterTable)request.getAttribute("PRODUCT_master");
	String gid = master.getGid();
	String mfg_no = master.getMfgNo();
	String item_code = master.getItemCode();
	String item_spec = master.getItemSpec();
	String item_name = master.getItemName();

	String order_count = Integer.toString(master.getOrderCount());
	if(order_count.equals("0")) order_count = mfg_count;			//���� �ʱ�ȭ �Ҷ� �ʿ�

	String total_count = Integer.toString(master.getTotalCount());
	String good_count = Integer.toString(master.getGoodCount());
	String bad_count = Integer.toString(master.getBadCount());
	String output_status = master.getOutputStatus();
	String factory_no = master.getFactoryNo();
	String rst_total_count = nfm.toDigits(master.getTotalCount()) + "/" + nfm.toDigits(Integer.parseInt(order_count))+" EA";

	//----------------------------------------------------
	//  ��������Է°����� �ִ����
	//----------------------------------------------------
	int check_count = Integer.parseInt(order_count) - master.getTotalCount();		

	//----------------------------------------------------
	//	�Է�/���� ���� �б�
	//----------------------------------------------------
	com.anbtech.mm.entity.mfgProductItemTable item;
	item = (mfgProductItemTable)request.getAttribute("PRODUCT_item");

	String pid = item.getPid();
	total_count = Integer.toString(item.getTotalCount());
	good_count = Integer.toString(item.getGoodCount());
	bad_count = Integer.toString(item.getBadCount());
	String bad_type = item.getBadType();
	String bad_note = item.getBadNote();
	String output_date = item.getOutputDate();		//�����

	String todate = anbdt.getDate();		//����(���ϸ����� ����)

	
%>

<HTML>
<HEAD><TITLE>������ ���������� �����ϱ�</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="sForm" method="post" style="margin:0">
<!-- TITLE -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD vAlign=top>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TBODY>
		<TR height=25><TD vAlign=center  style='padding-left:5px' class='bg_05' BACKGROUND='../bm/images/title_bm_bg.gif'> ����������</TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
		<TR><TD align=left width='100%' height='25' bgcolor='' style='padding-left:5px;'><!--��ư-->
	<%
		if(pid.length() == 0) {		//�ű� 
	%>
			<a href="javascript:sendSave();"><img src="../mm/images/bt_add.gif" border=0></a>
	<% } else {						//���� 
	%>
			<a href="javascript:sendModify();"><img src="../mm/images/bt_modify.gif" border=0></a>
			<a href="javascript:sendView();"><img src="../mm/images/bt_new.gif" border=0></a>
	<% } %>
			</TD></TR>
	
		<!--����-->
		<TR><TD>
			<TABLE cellspacing=0 cellpadding=0 width="100%" border=0>
				<TBODY>
					<TR><TD height='1' bgcolor='#9DA8BA' colspan='4'></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��������</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="total_count" value="<%=total_count%>" size="10"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������귮 : <%=rst_total_count%></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��ǰ����</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="good_count" value="<%=good_count%>" size="10"></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�ҷ�����</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="bad_count" value="<%=bad_count%>" size="10"></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�ҷ�����</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT type="text" name="bad_type" value="<%=bad_type%>" size="20"></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�ҷ�����</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
							<TEXTAREA NAME="bad_note" rows='3' cols='70'><%=bad_note%></TEXTAREA></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY></TABLE></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="<%=pid%>">
<INPUT type="hidden" name="gid" size="15" value="<%=gid%>">
<INPUT type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<INPUT type="hidden" name="assy_code" size="15" value="">
<INPUT type="hidden" name="item_code" size="15" value="<%=item_code%>">
<INPUT type="hidden" name="item_name" size="15" value="<%=item_name%>">
<INPUT type="hidden" name="item_spec" size="15" value="<%=item_spec%>">
<INPUT type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
</FORM>

</BODY>
</HTML>



<script language=javascript>
<!--
//����ϱ�
function sendSave()
{
	
	var total_count = document.sForm.total_count.value;
	if(isNaN(total_count)) { alert('[��������] ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(total_count.indexOf('.') != -1) { alert('[��������] ������ �Է��� �����մϴ�.'); return; }
	else if(total_count.indexOf('-') != -1) { alert('[��������] �ڿ����� �Է��� �����մϴ�.'); return; }
	else if(total_count == '0' || total_count.length == 0) { alert('[��������]������ �Է��Ͻʽÿ�.'); return;}

	var good_count = document.sForm.good_count.value;
	if(isNaN(good_count)) { alert('[��ǰ����] ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(good_count.indexOf('.') != -1) { alert('[��ǰ����] ������ �Է��� �����մϴ�.'); return; }
	else if(good_count.indexOf('-') != -1) { alert('[��ǰ����] �ڿ����� �Է��� �����մϴ�.'); return; } 

	var bad_count = document.sForm.bad_count.value;
	if(isNaN(bad_count)) { alert('[�ҷ�����] ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(bad_count.indexOf('.') != -1) { alert('[�ҷ�����] ������ �Է��� �����մϴ�.'); return; }
	else if(bad_count.indexOf('-') != -1) { alert('[�ҷ�����] �ڿ����� �Է��� �����մϴ�.'); return; } 

	//�����˻�
	if(eval(total_count - good_count - bad_count != 0)) { 
		alert('����,��ǰ,�ҷ����� �Է��� ��ü���� ��꿡 ������ �ֽ��ϴ�.'); return; }

	//�ҷ������� ������ �Է��׸� �˻�
	if(bad_count.length != 0) {
		if(eval(bad_count > 0)) {
			var bad_type = document.sForm.bad_type.value;
			var bad_note = document.sForm.bad_note.value;
			if(bad_type.length == 0) { alert('�ҷ������� �Է��Ͻʽÿ�.'); return; }
			if(bad_note.length == 0) { alert('�ҷ������� �Է��Ͻʽÿ�.'); return; }
		}
	}

	var check_count = '<%=check_count%>'; 
	if(eval(check_count - total_count < 0)) { alert('�����ѻ��귮�� �ʰ��Ͽ����ϴ�.'); return; }

	var v = confirm('�ش系���� ����Ͻðڽ��ϱ�?'); 
	if(v == false) { return; }

	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='product_save';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//�����ϱ�
function sendModify()
{
	var output_date = '<%=output_date%>';
	var todate = '<%=todate%>';
	if(output_date != todate) { alert('���ϸ����Ǿ����ϴ�.'); return; }

	var total_count = document.sForm.total_count.value;
	if(isNaN(total_count)) { alert('[��������] ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(total_count.indexOf('.') != -1) { alert('[��������] ������ �Է��� �����մϴ�.'); return; }
	else if(total_count.indexOf('-') != -1) { alert('[��������] �ڿ����� �Է��� �����մϴ�.'); return; } 

	var good_count = document.sForm.good_count.value;
	if(isNaN(good_count)) { alert('[��ǰ����] ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(good_count.indexOf('.') != -1) { alert('[��ǰ����] ������ �Է��� �����մϴ�.'); return; }
	else if(good_count.indexOf('-') != -1) { alert('[��ǰ����] �ڿ����� �Է��� �����մϴ�.'); return; } 

	var bad_count = document.sForm.bad_count.value;
	if(isNaN(bad_count)) { alert('[�ҷ�����] ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(bad_count.indexOf('.') != -1) { alert('[�ҷ�����] ������ �Է��� �����մϴ�.'); return; }
	else if(bad_count.indexOf('-') != -1) { alert('[�ҷ�����] �ڿ����� �Է��� �����մϴ�.'); return; } 

	//�����˻�
	if(eval(total_count - good_count - bad_count != 0)) { 
		alert('����,��ǰ,�ҷ����� �Է��� ��ü���� ��꿡 ������ �ֽ��ϴ�.'); return; }

	//�ҷ������� ������ �Է��׸� �˻�
	if(bad_count.length != 0) {
		if(eval(bad_count > 0)) {
			var bad_type = document.sForm.bad_type.value;
			var bad_note = document.sForm.bad_note.value;
			if(bad_type.length == 0) { alert('�ҷ������� �Է��Ͻʽÿ�.'); return; }
			if(bad_note.length == 0) { alert('�ҷ������� �Է��Ͻʽÿ�.'); return; }
		}
	}

	var pid = document.sForm.pid.value;
	if(pid == '') { alert('������ �׸��� �����Ͻʽÿ�.'); return; }

	var v = confirm('�ش系���� �����Ͻðڽ��ϱ�?'); 
	if(v == false) { return; }

	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='product_modify';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//�űԵ��
function sendView()
{
	var assy_code = document.sForm.item_code.value;
	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='product_preview';
	document.sForm.pid.value='';
	document.sForm.assy_code.value=assy_code;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//â
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//������ ó���� ��ư����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>