<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MPS �����ϱ�"		
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
	com.anbtech.text.StringProcess str = new StringProcess();

	//���Ѿ˾ƺ��� USER:�����,MGR:������
	String GRADE_mgr = (String)request.getAttribute("GRADE_mgr");	
	String GRADE = "N";
	if(GRADE_mgr.indexOf("MGR") != -1) GRADE = "Y";

	//----------------------------------------------------
	//	�Է�/���� ���� �б�
	//----------------------------------------------------
	String pid="",mps_no="",order_no="",mps_type="",model_code="",model_name="",fg_code="";
	String item_code="",item_name="",item_spec="",plan_date="",plan_count="",item_unit="",mps_status="";
	String factory_no="",factory_name="",reg_date="",reg_id="",reg_name="",order_comp="";
	String app_date="",app_id="";

	com.anbtech.mm.entity.mpsMasterTable item;
	item = (mpsMasterTable)request.getAttribute("ITEM_List");

	pid = item.getPid();
	mps_no = item.getMpsNo();
	order_no = item.getOrderNo();
	mps_type = item.getMpsType();
	model_code = item.getModelCode();
	model_name = item.getModelName();
	fg_code = item.getFgCode();

	item_code = item.getItemCode();
	item_name = item.getItemName();
	item_spec = item.getItemSpec();
	plan_date = item.getPlanDate();			if(plan_date.length() ==0) plan_date = anbdt.getDate(0);
	plan_count = Integer.toString(item.getPlanCount());
	item_unit = item.getItemUnit();
	mps_status = item.getMpsStatus();		if(mps_status.length() ==0) mps_status = "0";

	factory_no = item.getFactoryNo();
	factory_name = item.getFactoryName();
	reg_date = item.getRegDate();			if(reg_date.length() ==0) reg_date = anbdt.getDate();
	reg_id = item.getRegId();				if(reg_id.length() ==0) reg_id = sl.id;
	reg_name = item.getRegName();			if(reg_name.length() ==0) reg_name = sl.name;
	app_date= anbdt.getSepDate(item.getAppDate(),"-");
	app_id = item.getAppId();
	order_comp = item.getOrderComp();

	//----------------------------------------------------
	//	���� ���� �Ǵ��ϱ�
	//----------------------------------------------------
	String icon = "D";						//icon ��¿���
	String rd = "readonly";					//TEXT �������� �����Ǵ��ϱ�
	String ab = "disabled";					//���ùڽ� �������� �����Ǵ��ϱ�
	if(mps_status.equals("0") || mps_status.equals("1")) { rd=""; ab=""; icon="E";}
%>

<HTML>
<HEAD><TITLE>MPS �����ϱ�</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> <%=factory_name%> �����ȹ���</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;' >
				<% if(mps_status.equals("0")) {				// �ʱ��� %>
					<a href="javascript:sendSave();"><img src="../mm/images/bt_reg.gif" border=0 align='absmiddle'></a>
				<% } else if(mps_status.equals("1")) {		// ����,��� %>
					<a href="javascript:sendModify();"><img src="../mm/images/bt_modify.gif" border=0 align='absmiddle'></a>
					<a href="javascript:sendDelete();"><img src="../mm/images/bt_del.gif" border=0 align='absmiddle'></a>
					<a href="javascript:sendRequest();"><img src="../mm/images/bt_sangsin.gif" border=0 align='absmiddle'></a>
				<% } else if(mps_status.equals("2")) {		// ����,�ݷ� %> 
					<a href="javascript:sendApproval();"><img src="../mm/images/bt_commit_app.gif" border=0 align='absmiddle'></a>
					<a href="javascript:sendCancel();"><img src="../mm/images/bt_reject_app.gif" border=0 align='absmiddle'></a>
				<% } %>
					<a href="javascript:List();"><img src="../mm/images/bt_list.gif" border=0 align='absmiddle'></a>
					<a href="javascript:Process();"><img src="../mm/images/bt_view_d.gif" border=0 align='absmiddle'></a>
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
			<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
				<TBODY>
					<TR><TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">MPS������ȣ</TD>
						<TD width="37%" height="25" class="bg_04"><%=mps_no%>
							<INPUT type="hidden" name="mps_no" value="<%=mps_no%>" size="13"></TD>
					    <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��������</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="factory_no" value="<%=factory_no%>" size="10" readonly>
							<INPUT class='text_01' type="text" name="factory_name" value="<%=factory_name%>" size="15" readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchFactoryInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></TD>						
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
					<TR><TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����ȹ����</TD>
						<TD width="37%" height="25" class="bg_04">
						<SELECT name="mps_type" style=font-size:9pt;color="black"; <%=ab%>>
					<%
						String[] pp_no = {"SP","SO"};
						String[] pp_name = {"�ǸŰ�ȹ����","���ֻ���"};
						String sel = "";
						for(int i=0; i<pp_no.length; i++) {
							if(pp_no[i].equals(mps_type)) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+pp_no[i]+"'>");
							out.println(pp_name[i]+"</option>");
						} 
					%></select></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���ֹ�ȣ</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT type='text' name='order_no' maxlength='13' value='<%=order_no%>' <%=rd%>></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR><TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">F/G�ڵ�</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='fg_code' value='<%=fg_code%>' size='13' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchBomInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���־�ü</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT type='text' name='order_comp' maxlength='50' value='<%=order_comp%>' <%=rd%>></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���ڵ�</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='model_code' value='<%=model_code%>' size='13' readonly></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�𵨸�</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='model_name' value='<%=model_name%>' size='25' readonly></TD>
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
							<INPUT class='text_01' type='text' name='item_spec' value='<%=item_spec%>' size='40' readonly></TD></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ǰ���</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='item_name' value='<%=item_name%>' size='13' readonly></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�������</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='item_unit' value='<%=item_unit%>' size='6' readonly></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����ȹ����</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="plan_date" value="<%=anbdt.getSepDate(plan_date,"/")%>" size="10" readonly> 
				<% if(icon.equals("E")) { %>
					<A Href="Javascript:OpenCalendar('plan_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����ȹ����</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type='text' name='plan_count' value='<%=plan_count%>' size='6' <%=rd%>></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">������</TD>
						<TD width="37%" height="25" class="bg_04"><%=app_id%></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��������</TD>
						<TD width="37%" height="25" class="bg_04"><%=app_date%></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�������</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
	<%
		String[] status_no = {"1","2","3","4","5","6","7"};
		String[] status_name = {"MPS�ۼ�","MPS���ο�û","MPSȮ��","MRP����","MRP����","��������","��������"};
		String status_sel = "";
		for(int i=0; i<status_no.length; i++) {
			if(status_no[i].equals(mps_status)) status_sel = "checked";
			else status_sel = "";
			out.println("<INPUT type='radio' "+status_sel+" value=''>"+status_name[i]);
		} 
	%>					</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					
			</TBODY></TABLE></TD></TR></TABLE>
<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=pid%>'>
<INPUT type='hidden' name='year' value='<%=anbdt.getYear()%>'>
<INPUT type='hidden' name='month' value='<%=anbdt.getMonth()%>'>
</FORM>
</BODY>
</HTML>

<script language=javascript>
<!--
//����ϱ�
function sendSave()
{
	var f = document.eForm;

	var factory_no = f.factory_no.value;
	if(factory_no == '') { alert('�����ȣ�� �Էµ��� �ʾҽ��ϴ�.'); f.factory_no.focus(); return; }
	var factory_name = f.factory_name.value;
	if(fg_code == '') { alert('�����̸��� �Էµ��� �ʾҽ��ϴ�.'); f.factory_name.focus(); return; }
	var fg_code = f.fg_code.value;
	if(fg_code == '') { alert('FG�ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); f.fg_code.focus(); return; }
	var item_code = f.item_code.value;
	if(item_code == '') { alert('ǰ���ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); f.item_code.focus(); return; }
	var plan_date = f.plan_date.value;
	if(plan_date == '') { alert('��ȹ���� �Էµ��� �ʾҽ��ϴ�.'); f.plan_date.focus(); return; }
	var plan_count = f.plan_count.value;
	if(plan_count == '') { alert('��ȹ������ �Էµ��� �ʾҽ��ϴ�.'); f.plan_count.focus(); return; }
	else if(plan_count == '0') { alert('��ȹ������ �Էµ��� �ʾҽ��ϴ�.'); f.plan_count.focus(); return; }

	if(isNaN(plan_count)) { alert('��ȹ������ ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(plan_count.indexOf('.') != -1) { alert('��ȹ������ ������ �Է��� �����մϴ�.'); return; }

	var mps_status = '<%=mps_status%>';
	if(mps_status != '0') { alert('�ʱ� ��ϻ��� �϶� �� �����մϴ�.'); return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_save';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//��ǰ �����ϱ�
function sendModify()
{
	var mps_status = '<%=mps_status%>';
	if(mps_status != '1') { alert('MPS�ۼ����� �϶� �� �����մϴ�.'); return; }

	var f = document.eForm;
	var plan_count = document.eForm.plan_count.value;
	if(isNaN(plan_count)) { alert('��ȹ������ ���ڸ� �Է��� �����մϴ�.'); return; }
	else if(plan_count.indexOf('.') != -1) { alert('��ȹ������ ������ �Է��� �����մϴ�.'); return; }
	else if(plan_count == '0') { alert('��ȹ������ �Էµ��� �ʾҽ��ϴ�.'); f.plan_count.focus(); return; }

	var v = confirm('�ش系���� �����Ͻðڽ��ϱ�?'); 
	if(v == false) { return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_modify';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//��ǰ �����ϱ�
function sendDelete()
{
	var mps_status = '<%=mps_status%>';
	if(mps_status != '1') { alert('MPS�ۼ����� �϶� �� �����մϴ�.'); return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_delete';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//���ο�û�ϱ�
function sendRequest()
{
	var mps_status = '<%=mps_status%>';
	if(mps_status != '1') { alert('MPS�ۼ����� �϶� �� �����մϴ�.'); return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_ask';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//����Ȯ���ϱ�
function sendApproval()
{
	var grade = '<%=GRADE%>';
	if(grade == 'N') { alert('����Ȯ�� ������ �����ϴ�.'); return; }

	var mps_status = '<%=mps_status%>';
	if(mps_status != '2') { alert('MPS���ο�û���� �϶� �� �����մϴ�.'); return; }

 	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_app';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//��������ϱ�
function sendCancel()
{
	var grade = '<%=GRADE%>';
	if(grade == 'N') { alert('������� ������ �����ϴ�.'); return; }

	var mps_status = '<%=mps_status%>';
	if(mps_status != '2') { alert('MPS���ο�û���� �϶� �� �����մϴ�.'); return; }

	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='mps_cancel';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//��Ϻ���
function List()
{
	document.eForm.action='../servlet/mpsInfoServlet';
	document.eForm.mode.value='cal_month';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//��������� ����
function Process() {
	var f = document.eForm;
	var factory_no = f.factory_no.value;
	var mps_no = f.mps_no.value;
	var fg_code = f.fg_code.value;
	var model_code = f.model_code.value;
	var model_name = f.model_name.value;
	
	var para = "factory_no="+factory_no+"&mps_no="+mps_no+"&fg_code="+fg_code;
	para += "&model_code="+model_code+"&model_name="+model_name;

	url = "../servlet/mpsInfoServlet?mode=process_view&"+para;
	wopen(url,'pv','600','265','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//�������� ã��
function searchFactoryInfo() {
	var f = document.eForm;
	var factory_no = f.factory_no.name;
	var factory_name = f.factory_name.name;
	
	var para = "field="+factory_no+"/"+factory_name;

	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&"+para;
	wopen(url,'enterCode','400','227','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//FG�ڵ� �˻��ϱ�
function searchBomInfo(){
	var strUrl = "../gm/openModelInfoWindow.jsp?one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=pd_name&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	
	wopen(strUrl,"search_bominfo",'820','405','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//��ȹ���� ã��
function OpenCalendar(FieldName) {
	var strUrl = "../mm/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = wopen(strUrl, "Calendar", "200","280","scrollbars=no,toolbar=no,status=no,resizable=no");
}
// ǰ������ ��������
function searchItemInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_spec&item_unit=item_unit";
	wopen(strUrl,"open_part",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
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