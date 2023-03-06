<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MRP ���"		
	contentType = "text/html; charset=euc-kr" 		
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
	com.anbtech.text.StringProcess str = new StringProcess();
	String factory_no = (String)request.getAttribute("factory_no"); if(factory_no == null) factory_no = "";
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";

	//----------------------------------------------------
	//	�Է�/���� ���� �б�
	//----------------------------------------------------
	String pid="",mps_no="",mrp_no="",mrp_start_date="",mrp_end_date="",model_code="",model_name="";
	String fg_code="",item_code="",item_name="",item_spec="",p_count="",plan_date="",item_unit="";
	String mrp_status="",factory_name="",reg_date="",reg_id="",reg_name="",pu_dev_date="",pu_req_no="";
	String stock_link="",pjt_code="",pjt_name="";

	com.anbtech.mm.entity.mrpMasterTable item;
	item = (mrpMasterTable)request.getAttribute("MRP_master");

	pid = item.getPid();
	mps_no = item.getMpsNo();
	mrp_no = item.getMrpNo();
	mrp_start_date = item.getMrpStartDate();if(mrp_start_date.length() ==0) mrp_start_date = anbdt.getDate(0);
	mrp_end_date = item.getMrpEndDate();
	model_code = item.getModelCode();
	model_name = item.getModelName();
	fg_code = item.getFgCode();

	item_code = item.getItemCode();
	item_name = item.getItemName();
	item_spec = item.getItemSpec();
	p_count = Integer.toString(item.getPCount());
	plan_date = item.getPlanDate();
	item_unit = item.getItemUnit();
	mrp_status = item.getMrpStatus();		if(mrp_status.length() ==0) mrp_status = "S";

	factory_no = item.getFactoryNo();
	factory_name = item.getFactoryName();
	reg_date = item.getRegDate();			if(reg_date.length() ==0) reg_date = anbdt.getDate();
	reg_id = item.getRegId();				if(reg_id.length() ==0) reg_id = sl.id;
	reg_name = item.getRegName();			if(reg_name.length() ==0) reg_name = sl.name;
	pu_dev_date = item.getPuDevDate();		if(pu_dev_date.length() ==0) pu_dev_date = anbdt.getDate(0);
	pu_req_no = item.getPuReqNo();
	stock_link = item.getStockLink();		if(stock_link.length() ==0) stock_link="0";
	pjt_code = item.getPjtCode();
	pjt_name = item.getPjtName();

	//----------------------------------------------------
	//	���� ���� �Ǵ��ϱ�
	//----------------------------------------------------
	String icon = "D";						//icon ��¿���
	String rd = "readonly";					//TEXT �������� �����Ǵ��ϱ�
	String ab = "disabled";					//���ùڽ� �������� �����Ǵ��ϱ�
	String ra = "enable";					//Radio Button �������� �Ǵ�
	String temp_caption = "";				// ���Ǽҿ䷮���� ��, 
	if(mps_no.length() != 0) {				//MPS������ ����
		if(mrp_status.equals("S") || mrp_status.equals("0")) {icon="P"; }
		else ra="disabled";
	} else {								//temp�� ����
		rd=""; ab=""; icon="E";
		//temp_caption = "�ҿ䷮������";
	}
	
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//�޽��� ����
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}

//����ϱ�
function sendSave()
{
	var mrp_status = '<%=mrp_status%>';
	if(mrp_status != 'S') { alert('MRP�������� �϶� �� �����մϴ�.'); return; }

	var f = document.eForm;
	var mrp_start_date = f.mrp_start_date.value;
	if(mrp_start_date == '') { alert('�������� �Էµ��� �ʾҽ��ϴ�.'); f.mrp_start_date.focus(); return; }
	var p_count = f.p_count.value;
	if(p_count == '') { alert('��ȹ������ �Էµ��� �ʾҽ��ϴ�.'); f.p_count.focus(); return; }
	else if(p_count == '0') { alert('��ȹ������ �Էµ��� �ʾҽ��ϴ�.'); f.p_count.focus(); return; }

	if(isNaN(p_count)) { alert('��ȹ������ ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(p_count.indexOf('.') != -1) { alert('��ȹ������ ������ �Է��� �����մϴ�.'); return; }

	var pjt_code = f.pjt_code.value;
	if(pjt_code == '') { alert('�����ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); return; }

	//�Է¸޽��� ���
	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���

	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_save';
	document.eForm.submit();
}
//�����ϱ�
function sendModify()
{
	var mrp_status = '<%=mrp_status%>';
	if(mrp_status != '0' && mrp_status != '1') { alert('MRP�ۼ� �Ǵ� �ݷ����� �϶� �� �����մϴ�.'); return; }

	var p_count = document.eForm.p_count.value;
	if(isNaN(p_count)) { alert('��ȹ������ ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(p_count.indexOf('.') != -1) { alert('��ȹ������ ������ �Է��� �����մϴ�.'); return; }

	var pjt_code = document.eForm.pjt_code.value;
	if(pjt_code == '') { alert('�����ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); return; }

	var v = confirm('�ش系���� �����Ͻðڽ��ϱ�?'); 
	if(v == false) { return; }
	
	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_modify';
	document.eForm.submit();
}
//MRP����
function mrpCount()
{
	var f = document.eForm;

	var pid = document.eForm.pid.value;
	var mrp_no = document.eForm.mrp_no.value;
	var fg_code = document.eForm.fg_code.value;			
		if(fg_code == '') { alert('FG�ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); f.fg_code.focus(); return; }
	var item_code = document.eForm.item_code.value;
		if(item_code == '') { alert('ǰ���ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); f.fg_code.focus(); return; }
	var mrp_start_date = document.eForm.mrp_start_date.value;
	var mrp_count = document.eForm.p_count.value;
	var factory_no = document.eForm.factory_no.value;
	var mrp_status = document.eForm.mrp_status.value;

	var stock_link = "";
	var nm = eForm.stock_link.length;	
	for(i=0; i<nm; i++) {
		if(eForm.stock_link[i].checked) stock_link = document.eForm.stock_link[i].value;
	}

	var p_count = document.eForm.p_count.value;
	
	var para = "pid="+pid+"&mrp_no="+mrp_no+"&fg_code="+fg_code+"&item_code="+item_code;
	para += "&mrp_start_date="+mrp_start_date+"&mrp_count="+mrp_count+"&factory_no="+factory_no;
	para += "&mrp_status="+mrp_status+"&stock_link="+stock_link+"&p_count="+p_count;

	document.eForm.action='../mm/mrp/mrpItemFrame.jsp?'+para;
	document.eForm.submit();
}
//MRPȮ���ϱ�
function mrpConfirm()
{
	var mrp_status = '<%=mrp_status%>';
	if(mrp_status == '3') { alert('MRPȮ������ �Դϴ�.'); return; }
	else if(mrp_status == '4') { alert('���Ź��ֻ��� �Դϴ�.'); return; }
	else if(mrp_status == 'S') { alert('MRP�������� �Դϴ�.'); return; }

	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_confirm';
	document.eForm.submit();
}
//MRPȮ�� ����ϱ� 
function mrpCancel()
{
	var mrp_status = '<%=mrp_status%>';
	if(mrp_status != '3') { alert('MRPȮ������ �϶� �� �����մϴ�.'); return; }

	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_cancel';
	document.eForm.submit();
}
//��Ϻ���
function List()
{
	document.eForm.action='../servlet/mrpInfoServlet';
	document.eForm.mode.value='mrp_list';
	document.eForm.submit();
}


//FG�ڵ� �˻��ϱ�
function searchBomInfo(){

	var strUrl = "../gm/openModelInfoWindow.jsp?one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=pd_name&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	
	wopen(strUrl,"search_bominfo",'820','405','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//��ȹ���� ã��
function OpenCalendar(FieldName) {
	var strUrl = "../mm/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}
// ǰ������ ��������
function searchItemInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_spec&item_unit=item_unit";
	wopen(strUrl,"open_part",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}
// ����ã��
function searchProject() {
	
	para = "&target=eForm.pjt_code/eForm.pjt_name";	wopen('../servlet/PsmProcessServlet?mode=search_project'+para,'search_pjt','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
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
<BODY topmargin="0" leftmargin="0">
<form name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=factory_name%> MRP ���</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
						<TD align=left width='20%' style='padding-left:5px;'>
				<% 
					if(mps_no.length() == 0) {			//���� �ҿ䷮ 
						out.println("<a href='javascript:mrpCount();'><img src='../mm/images/bt_mrp_unfolding.gif' border='0' alt='MRP����' align='absmiddle'></a>");
				 }  else {								//MPS�� ���� ���� 
					if(mrp_status.equals("S"))	{		//�ʱ� ���
						out.println("<a href='javascript:sendSave();'><img src='../mm/images/bt_reg.gif' border=0 align='absmiddle'></a>");
					} else if(mrp_status.equals("0") || mrp_status.equals("1")) { //����,����,Ȯ��
						out.println("<a href='javascript:sendModify();'><img src='../mm/images/bt_modify.gif' border=0 align='absmiddle'></a>");
						out.println("<a href='javascript:mrpCount();'><img src='../mm/images/bt_mrp_unfolding.gif' border=0 alt='MRP����' align='absmiddle'></a>");
						out.println("<a href='javascript:mrpConfirm();'><img src='../mm/images/bt_mrp_confirm.gif' border=0 alt='MRPȮ��' align='absmiddle'></a>");
					} else if(mrp_status.equals("2")) {	//������� ����
					} else if(mrp_status.equals("3")) {	//MRP����,MRPȮ�����
						out.println("<a href='javascript:mrpCount();'><img src='../mm/images/bt_mrp_searching.gif' border=0 alt='MRP��ȸ' align='absmiddle'></a>");
						out.println("<a href='javascript:mrpCancel();'><img src='../mm/images/bt_mrp_cancel.gif' border=0 alt='MRP���' align='absmiddle'></a>");
					} else {							//MRP����
						out.println("<a href='javascript:mrpCount();'><img src='../mm/images/bt_mrp_searching.gif' border=0 alt='MRP��ȸ' align='absmiddle'></a>");
					}
				} %>
					<a href="javascript:List();"><img src="../mm/images/bt_list.gif" border=0 align='absmiddle'></a>
					</TD>
			</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
	<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
		<TR>
			<TD align="center">
				<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
					<TBODY>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">MRP������ȣ</TD>
						<TD width="37%" height="25" class="bg_04" colspan='3'><%=mrp_no%>
							<INPUT type="hidden" name="mrp_no" value="<%=mrp_no%>" size="20"></TD>
						</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��������ڵ�</TD>
						<TD width="37%" height="25" class="bg_04"><%=factory_no%></TD>			
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��������</TD>
						<TD width="37%" height="25" class="bg_04"><%=factory_name%></TD>			
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�ۼ���</TD>
						<TD width="37%" height="25" class="bg_04"><%=reg_name%>/<%=reg_id%>
							<INPUT type="hidden" name="reg_id" value="<%=reg_id%>" size="30">
							<INPUT type="hidden" name="reg_name" value="<%=reg_name%>" size="13">
						</TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�ۼ���</TD>
						<TD width="37%" height="25" class="bg_04"><%=reg_date%>
							<INPUT type="hidden" name="reg_date" value="<%=reg_date%>" size="30"></TD>		
					</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height='5' colspan='4'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">F/G�ڵ�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='fg_code' value='<%=fg_code%>' size='13'  readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchBomInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">MPS������ȣ</TD>
			   <TD width="37%" height="25" class="bg_04"><%=mps_no%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���ڵ�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='model_code' value='<%=model_code%>' size='13' readonly></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�𵨸�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='model_name' value='<%=model_name%>' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ǰ���ڵ�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='item_code' value='<%=item_code%>' size='13' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchItemInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ǰ�񼳸�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='item_spec' value='<%=item_spec%>' size='40' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ǰ���</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='item_name' value='<%=item_name%>' readonly></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�������</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='item_unit' value='<%=item_unit%>'  size='6' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">BOM��������</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type="text" name="mrp_start_date" value="<%=anbdt.getSepDate(mrp_start_date,"/")%>" size="10" readonly> 
				<% if(icon.equals("E") || icon.equals("P")) { %>
					<A Href="Javascript:OpenCalendar('mrp_start_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����ȹ����</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='p_count' value='<%=p_count%>' <%=rd%>  size='6' ></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<!--��Ÿ���� 
			<TR>
				<TD height="25" colspan="4"><IMG src='../mm/images/title_eqinfo.gif' border='0'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>-->
			<TR><TD height='5' colspan='4'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��ǰ�����</TD>
			   <TD width="87%" height="25" class="bg_04" colspan=3>
			   <%	String sel = "";
					String[] sl_data = {"�����","�����������"};
					String[] sl_value = {"1","0"};
					for(int i=0; i<sl_data.length; i++) {
						if(stock_link.equals(sl_value[i])) sel="checked";
						else sel = "";
						out.print("<INPUT type='radio' name='stock_link' value='"+sl_value[i]+"' ");
						out.println(sel+" "+ra+">"+sl_data[i]);
					}
				%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����԰������</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type="text" name="pu_dev_date" value="<%=anbdt.getSepDate(pu_dev_date,"/")%>" size="10" readonly> 
				<% if(!mrp_status.equals("4") & !mrp_status.equals("3")) { %>
					<A Href="Javascript:OpenCalendar('pu_dev_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���ſ�û��ȣ</TD>
			   <TD width="37%" height="25" class="bg_04"><%=pu_req_no%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����ڵ�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type="text" name="pjt_code" value="<%=pjt_code%>" size="20" readonly> 
				<% if(!mrp_status.equals("4") & !mrp_status.equals("3")) { %>
					<A Href="Javascript:searchProject();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></A>
				<% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">������</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type="text" name="pjt_name" value="<%=pjt_name%>" size="20" readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�������</TD>
			   <TD width="87%" height="25" class="bg_04" colspan=3>
				<%
					String[] status_no = {"0","1","3","4"};
					String[] status_name = {"����ݷ�","MRP���","MRPȮ��(���ŵ��)","���Ź���"};
					String status_sel = "";
					for(int i=0; i<status_no.length; i++) {
						if(status_no[i].equals(mrp_status)) status_sel = "checked";
						else status_sel = "";
						out.println("<INPUT type='radio' "+status_sel+" value='' disabled>"+status_name[i]);
					} 
				%>		</TD>
			</TR>
			
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY></TABLE></TD></TR></TABLE>

<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=pid%>'>
<INPUT type='hidden' name='mps_no' value='<%=mps_no%>'>
<INPUT type='hidden' name='factory_no' value='<%=factory_no%>'>
<INPUT type='hidden' name='factory_name' value='<%=factory_name%>'>
<INPUT type='hidden' name='mrp_status' value='<%=mrp_status%>'>
<INPUT type='hidden' name='plan_date' value='<%=plan_date%>'>
</FORM>

<DIV id="lding" style="position:absolute;left:200px;top:110px;width:224px;height:150px;visibility:hidden;">
	<img src='../mm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</BODY>
</HTML>

