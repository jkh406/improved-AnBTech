<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���� BOM���/�����ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();

	//----------------------------------------------------
	// MBOM MASTER���� �б�
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomMasterTable masterT;
	masterT = (mbomMasterTable)request.getAttribute("MASTER_List");
	String pdg_code = masterT.getPdgCode();
	String model_code = masterT.getModelCode();

	//----------------------------------------------------
	//	�Է�/���� ���� �б�
	//----------------------------------------------------
	String pid="",gid="",parent_code="",child_code="",part_name="";
	String part_spec="",location="",op_code="",qty_unit="EA";
	String part_type="";
	String level_no="0";
	String qty="0";

	com.anbtech.bm.entity.mbomStrTable item;
	item = (mbomStrTable)request.getAttribute("ITEM_List");

	pid = item.getPid();
	gid = item.getGid();
	parent_code = item.getParentCode();
	child_code = item.getChildCode();
	level_no = item.getLevelNo();
	part_name = item.getPartName();
	part_spec = item.getPartSpec();
	location = item.getLocation();
	op_code = item.getOpCode();
	qty_unit = item.getQtyUnit();
	qty = item.getQty();
	part_type = item.getPartType();		//A:Assy, P:��ǰ

	//----------------------------------------------------
	//	��� �� ����,���� ��� ���� 
	//----------------------------------------------------
	String sTitle = "���";
	String stage = "W";							//��ϻ���
	String file_tag = "w";
	if(gid.length() != 0) {
		sTitle = "����/����";
		stage = "M";							//����,��������
		file_tag = "m";
	}

	//-----------------------------------
	//	�Ķ���� �ޱ� 
	//-----------------------------------
	if(gid.length() == 0) gid = (String)request.getAttribute("gid");
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	int tg = msg.indexOf("������");		//������ �ִ��� �Ǵ��ϱ�

%>

<HTML>
<HEAD><TITLE>���� BOM���/�����ϱ�</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD vAlign=top>
	<TABLE border=0 cellspacing=0 cellpadding=1 width="100%" height='100%'>
	<TBODY>
		<TR height=25><TD vAlign=center  style='padding-left:5px' class='bg_05' BACKGROUND='../bm/images/title_bm_bg.gif'>��ǰ���</TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
		<TR><TD align=left width='100%' height='25' bgcolor=''><!--��ư-->
	<%	
				if(stage.equals("W")) {		//����϶�
					out.print("&nbsp;<a href='javascript:sendSave();'><img src='../bm/images/bt_add.gif' border=0 align='absmiddle'></a>");
				} else {					//�����϶�
					out.print("&nbsp;<a href='javascript:sendModify();'><img src='../bm/images/bt_modify.gif' border=0 align='absmiddle'></a>");
					if(part_type.equals("P")) //��ǰ�϶� ��������
						out.print("&nbsp;<a href='javascript:sendDelete();'><img src='../bm/images/bt_del.gif' border=0 align='absmiddle'></a>");
					else if(part_type.equals("A")) //Assy�϶� ��ü���� ���
						out.print("&nbsp;<a href='javascript:sendAllDelete();'><img src='../bm/images/bt_totaldel.gif' border=0 alt='��ü����' align='absmiddle'></a>");
					out.print("&nbsp;<a href='javascript:SaveForm();'><img src='../bm/images/bt_add_new2.gif' border=0 align='absmiddle'></a>");
				} 
%>		</TD></TR><TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	
		<!--����-->
		<TR><TD valign="top">
			<TABLE cellspacing=0 cellpadding=1 width="100%" border=0>
			<TBODY>
				<TR><TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">��ǰ���ڵ�</TD>
					<TD width="35%" height="24" class="bg_04">

			   <% if(stage.equals("W")) { //�űԵ�� �϶�%>
					<INPUT class="text_01" type='text' name='parent_code' value='<%=parent_code%>'></TD>
			   <% } else { //����,���� �϶�%>
					<%=parent_code%>
					<INPUT type='hidden' name='parent_code' value='<%=parent_code%>'></TD>
			   <% } %>

						   <TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">��ǰ���ڵ�</TD>
						   <TD width="35%" height="24" class="bg_04">
			   <% 
				//if(part_type.equals("P")) {		//��ǰ���� ���������� 
					out.print("<INPUT class='text_01' type='text' name='child_code' value='"+child_code+"' readonly>");
					out.print("&nbsp;<a href='javascript:searchItemInfo();'><img src='../crm/images/bt_search.gif' border='0' align='absbottom'></a>");
			   // } else {						//Assy�� ���� �Ұ����� 
				//	out.print("<INPUT class='text_01' type='text' name='child_code' value='"+child_code+"' readonly>");
			   // } 
				%></TD></TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">LOC NO</TD>
						<TD width="35%" height="24" class="bg_04">
							<INPUT  type='text' name='location' value='<%=location%>'></TD>
						<TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">�����ڵ�</TD>
						<TD width="35%" height="24" class="bg_04">
							<INPUT class="text_01" type='text' name='op_code' value='<%=op_code%>' readonly>
							<A href='javascript:searchOPInfo();'><img src='../bm/images/bt_search.gif' border='0' align='absbottom'></a></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>			
		<%  // �űԵ���϶�
			if(stage.equals("W")) {  
		%>
			<TR><TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">��ǰ����</TD>
				<TD width="85%" height="24" class="bg_04" colspan=3>
					<INPUT type='text' name='part_cnt' size='5' value='1'class="text_01"> ��</TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
<%		}
%>
			</TBODY></TABLE></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>

<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=pid%>'>
<INPUT type='hidden' name='gid' value='<%=gid%>'>
<INPUT type='hidden' name='level_no' value='<%=level_no%>'>
<INPUT type='hidden' name='part_name' value=''>
<INPUT type='hidden' name='part_spec' value=''>
<INPUT type='hidden' name='price_unit' value='��'>
<INPUT type='hidden' name='price' value='0'>
<INPUT type='hidden' name='qty_unit' value=''>
<INPUT type='hidden' name='qty' value='1'>
<INPUT type='hidden' name='part_type' value='<%=part_type%>'>
<INPUT type='hidden' name='op_name' value=''>
<INPUT type='hidden' name='model_code' value='<%=model_code%>'>
<% 
	if(tg != -1) out.println("<INPUT type='hidden' name='msg' value='"+msg+"'>");
	else out.println("<INPUT type='hidden' name='msg' value=''>");
%>
</FORM>



</td></tr></table>
</BODY>
</HTML>

<script language=javascript>
<!--
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
//	parent.tree.document.all['saving'].style.visibility="hidden";	//�޴���ư enable [LIST]
	parent.tree.document.body.style.overflow='';					//LIST�� ��ũ�ѹ� �ٽú����ֱ�
}

//��ǰ ����ϱ�
function sendSave()
{
	var pdg_code = '<%=pdg_code%>';		//��ǰ�� ��ǰ���ڵ�
	if(pdg_code.length == 0) { alert('�ش� FG�ڵ带 �����Ͻʽÿ�.'); return; }

	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	var pcode = document.eForm.parent_code.value;
	if(pcode.length == 0) { alert('��ǰ���ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); return; }

	var pcode_ini = pcode.substring(0,1);
	if(pcode_ini != '1' && pcode_ini != 'F') { 
		alert('��ǰ���ڵ�� �ݵ�� ASSY�ڵ常 ����� �� �ֽ��ϴ�.'); return; 
	}

	var ccode = document.eForm.child_code.value;
	if(ccode.length == 0) { alert('��ǰ���ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); return; }

	//��ǰ���� Assy�ڵ��϶� ��ǰ������ ������ 1���� ����
	var pcnt = document.eForm.part_cnt.value;
	var chead = ccode.substring(0,1);
	if((chead == '1') && (pcnt > 1))  { 
		alert('ASSY�ڵ�� ���Ϻ�ǰ������ �Ѱ��̻� �Է��� �� �����ϴ�.'); return; 
	}

	if(chead == '1') {
		var op_code = document.eForm.op_code.value;
		if(op_code.length == 0) { alert('�����ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); return; }
	}

	//������ ��ǰ������ �ڵ����� �Ǵ�
	var pdg_code = '<%=pdg_code%>';		//��ǰ�� ��ǰ���ڵ�
	var chd_pdg = ccode.substring(3,4);	//��ǰ�� ��ǰ���ڵ�
	if((chead == '1') && (pdg_code != chd_pdg)) { 
		alert('������ ��ǰ������ �ڵ带 ����ؾ� �մϴ�.'); return; 
	}

	//��ǰ���� ����Assy�϶� BOM������ �� ����
	var phead = pcode.substring(0,3); 
	if(phead == '1PH') { alert('Phantom Assy�� �Ϻα����� ������ �� �����ϴ�.'); return; }

	//ó���� �޽��� ���
//	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
//	document.all['saving'].style.visibility="visible";				//�޴���ư Disable [�ڽ�]
//	parent.tree.document.all['saving'].style.visibility="visible";	//�޴���ư Disable [LIST]
//	parent.tree.document.body.style.overflow='hidden';				//LIST�� ��ũ�ѹ� �����

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_write';
	document.eForm.submit();
}
//��ǰ �����ϱ�
function sendModify()
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	//��ǰ���� Assy�ڵ��϶� �����ڵ� �Է¿��� �˻�
	var ccode = document.eForm.child_code.value;
	var chead = ccode.substring(0,1);
	if(chead == '1') {
		var op_code = document.eForm.op_code.value;
		if(op_code.length == 0) { alert('�����ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); return; }
	}

	//������ ��ǰ������ �ڵ����� �Ǵ�
	var pdg_code = '<%=pdg_code%>';		//��ǰ�� ��ǰ���ڵ�
	var chd_pdg = ccode.substring(3,4);	//��ǰ�� ��ǰ���ڵ�
	if((chead == '1') && (pdg_code != chd_pdg)) { 
		alert('������ ��ǰ������ �ڵ带 ����ؾ� �մϴ�.'); return; 
	}

	var part_type = '<%=part_type%>';
	if(part_type == 'A') {
		var cn = confirm('ASSY�ڵ�� �ش�Ǵ� ����ڵ尡 �����˴ϴ�. ����Ͻðڽ��ϱ�?');
		if(cn == false) return;
	}

	//ó���� �޽��� ���
//	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
//	document.all['saving'].style.visibility="visible";				//�޴���ư Disable [�ڽ�]
//	parent.tree.document.all['saving'].style.visibility="visible";	//�޴���ư Disable [LIST]
//	parent.tree.document.body.style.overflow='hidden';				//LIST�� ��ũ�ѹ� �����

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_modify';
	document.eForm.submit();
}
//��ǰ �����ϱ�
function sendDelete()
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	var cn = confirm('�����Ͻðڽ��ϱ�?');
	if(cn == false) return;
	
	//ó���� �޽��� ���
//	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
//	document.all['saving'].style.visibility="visible";				//�޴���ư Disable [�ڽ�]
//	parent.tree.document.all['saving'].style.visibility="visible";	//�޴���ư Disable [LIST]
//	parent.tree.document.body.style.overflow='hidden';				//LIST�� ��ũ�ѹ� �����

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_delete';
	document.eForm.submit();
}
//Assy��ǰ �����ϱ�
function sendAllDelete()
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	var cn = confirm('�ش�Assy�ڵ����� ��� ��ǰ�� �����Ͻðڽ��ϱ�?');
	if(cn == false) return;
	
	//ó���� �޽��� ���
//	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
//	document.all['saving'].style.visibility="visible";				//�޴���ư Disable [�ڽ�]
//	parent.tree.document.all['saving'].style.visibility="visible";	//�޴���ư Disable [LIST]
//	parent.tree.document.body.style.overflow='hidden';				//LIST�� ��ũ�ѹ� �����

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_all_delete';
	document.eForm.submit();
}
//������忡�� �Է¸��� �ٲ��ֱ�
function SaveForm()
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_prewrite';
	document.eForm.pid.value='';
	document.eForm.submit();
}
// ǰ������ ��������
function searchItemInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=child_code&item_name=part_name&item_type=item_type&item_desc=part_spec&item_unit=qty_unit";
	wopen(strUrl,"open_part",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//�������� ��������
function searchOPInfo() {
	wopen("../mm/searchOPcode.jsp?target=eForm.op_code/eForm.op_name","opcode","250","350","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//â
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>