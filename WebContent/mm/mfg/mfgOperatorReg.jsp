<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ ��ȹ���� �����ϱ�"		
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

	//----------------------------------------------------
	//	�Է�/���� ���� �б�
	//----------------------------------------------------
	String pid="",gid="",mfg_no="",assy_code="",assy_spec="",mfg_count="",mfg_unit="",op_start_date="";
	String op_end_date="",order_date="",buy_type="",factory_no="",factory_name="";
	String work_no="",work_name="",op_no="",op_name="",mfg_id="",mfg_name="",note="";
	String comp_code="",comp_name="",comp_user="",comp_tel="",op_order="";

	com.anbtech.mm.entity.mfgOperatorTable item;
	item = (mfgOperatorTable)request.getAttribute("MFG_operator");

	pid = item.getPid();
	gid = item.getGid();
	mfg_no = item.getMfgNo();
	assy_code = item.getAssyCode();
	assy_spec = item.getAssySpec();
	mfg_count = Integer.toString(item.getMfgCount());
	mfg_unit = item.getMfgUnit();
	op_start_date = item.getOpStartDate();
	op_end_date = item.getOpEndDate();
	order_date = item.getOrderDate();
	buy_type = item.getBuyType();
	factory_no = item.getFactoryNo();
	factory_name = item.getFactoryName();
	work_no = item.getWorkNo();
	work_name = item.getWorkName();
	op_no = item.getOpNo();
	op_name = item.getOpName();
	mfg_id = item.getMfgId();
	mfg_name = item.getMfgName();
	note = item.getNote();
	comp_code = item.getCompCode();
	comp_name = item.getCompName();
	comp_user = item.getCompUser();
	comp_tel = item.getCompTel();
	op_order = item.getOpOrder();

	//----------------------------------------------------
	//	���� ���� �Ǵ��ϱ�
	//----------------------------------------------------
	String icon = "D";						//icon ��¿���
	String rd = "readonly";					//TEXT �������� �����Ǵ��ϱ�
	String ab = "disabled";					//���ùڽ� �������� �����Ǵ��ϱ�
	String ra = "enable";					//Radio Button �������� �Ǵ�
	if(op_order.equals("0")) {									
		rd="";ab=""; icon="E";
	}
	
%>

<HTML>
<HEAD><TITLE>������ ��ȹ���� �����ϱ�</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="sForm" method="post" style="margin:0" onsubmit='return false;'>

<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=25><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign="center" height=25 class="bg_05" style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'> ������ȹ���</TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height="25"><TD vAlign="center" align='left' style='padding-left:5px;'  bgcolor=''>
		<% if(op_order.equals("0")) { //����%>
				<a href="javascript:sendModify();"><img src="../mm/images/bt_modify.gif" align="absmiddle" border='0'></a>
		<% }  %>
		</TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<!--����Ʈ-->
	<TR height=100%>
		<TD vAlign=top>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			
			<TR>
			  <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="factory_no" value="<%=factory_no%>" size="12" readonly>
					<input type="text" name="factory_name" value="<%=factory_name%>" size="20" readonly>
					</td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�۾���ȹ����</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type="text" name="op_start_date" value="<%=op_start_date%>" size="10" readonly> 
				<% if(icon.equals("E")) { %>
					<A Href="Javascript:OpenCalendar('op_start_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %> ~ 
					<input class='text_01' type="text" name="op_end_date" value="<%=op_end_date%>" size="10" readonly> 
				<% if(icon.equals("E")) { %>
					<A Href="Javascript:OpenCalendar('op_end_date');"><img src="../mm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<% } %></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�۾����ù�ȣ</td>
			   <td width="37%" height="25" class="bg_04"><%=mfg_no%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ǰ��</td>
			   <td width="37%" height="25" class="bg_04"><%=assy_code%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���걸��</td>
			   <td width="37%" height="25" class="bg_04">
				<%	String sel = "";
					String[] sl_data = {"�系����ǰ","���ְ���ǰ","����ǰ"};
					String[] sl_value = {"M","O","P"};
					for(int i=0; i<sl_data.length; i++) {
						if(buy_type.equals(sl_value[i])) sel="checked";
						else sel = "";
						out.print("<input type='radio' name='buy_type' value='"+sl_value[i]+"' ");
						out.println(sel+" "+ra+">"+sl_data[i]);
					}
				%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�������</td>
			   <td width="37%" height="25" class="bg_04"><%=mfg_count%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='mfg_id' value='<%=mfg_id%>' size='10' readonly>
					<input class='text_01' type='text' name='mfg_name' value='<%=mfg_name%>' size='20' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchMfgInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='op_no' value='<%=op_no%>' size='5' readonly>
					<input class='text_01' type='text' name='op_name' value='<%=op_name%>' size='25' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchOpInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�۾���</td>
			   <td width="37%" height="25" class="bg_04">
					<input class='text_01' type='text' name='work_no' value='<%=work_no%>' size='10' readonly>
					<input class='text_01' type='text' name='work_name' value='<%=work_name%>' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchWorkInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���ִ����</td>
			   <td width="37%" height="25" class="bg_04">
					<input type='text' name='comp_user' value='<%=comp_user%>' size='10' readonly>
					<input type='text' name='comp_tel' value='<%=comp_tel%>' readonly>
				<% if(icon.equals("E")) { %>
					<a href="javascript:searchCompInfo();"><img src="../mm/images/bt_search.gif" border="0" align='absmiddle'></a>
				<% } %></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ó��</td>
			   <td width="37%" height="25" class="bg_04">
					<input type='text' name='comp_name' value='<%=comp_name%>' size='30' readonly>
			   </td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ڹ�ȣ</td>
			   <td width="37%" height="25" class="bg_04">
					<input type='text' name='comp_code' value='<%=comp_code%>' readonly>
					</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���û���</td>
			   <td width="37%" height="25" class="bg_04" >
					<TEXTAREA NAME="note" rows='3' cols='40' <%=rd%>><%=note%></TEXTAREA></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����Ȯ����</td>
			   <td width="37%" height="25" class="bg_04"><%=anbdt.getSepDate(order_date,"-")%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></TR>
	</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='gid' value='<%=gid%>'>
</form>

<DIV id="lding" style="position:absolute;left:300px;top:50px;width:250px;height:100px;visibility:hidden;">
		<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</BODY>
</HTML>

<script language=javascript>
<!--
//�����ϱ�
function sendModify()
{
	var f = document.sForm;

	var buy_type = "";
	var nm = sForm.buy_type.length;	
	for(i=0; i<nm; i++) {
		if(sForm.buy_type[i].checked) buy_type = document.sForm.buy_type[i].value;
	}
	if(buy_type == '') { alert('���걸���� �Է��Ͻʽÿ�.'); return; }

	//���ְ���ǰ�ΰ�� ����ó ���� �Է�
	if(buy_type == 'O') {
		var comp_code = f.comp_code.value;
		if(comp_code == '') { alert('����ó�ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); f.comp_code.focus(); return; }
	} else {
		var comp_code="";
		var comp_name="";
		var comp_user="";
		var comp_tel ="";
	}

	var op_no = f.op_no.value;
	if(op_no == '') { alert('���������� �Էµ��� �ʾҽ��ϴ�.'); return; }

	var mfg_id = f.mfg_id.value;
	if(mfg_id == '') { alert('����������� �Էµ��� �ʾҽ��ϴ�.'); return; }

	if(buy_type != 'P') {	//����ǰ�� �ƴϸ�
		var work_no = f.work_no.value;
		if(work_no == '') { alert('�۾��������� �Էµ��� �ʾҽ��ϴ�.'); return; }
	}
	

	var v = confirm('�ش系���� �����Ͻðڽ��ϱ�?'); 
	if(v == false) { return; }

	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='order_modify';
	if(buy_type != 'O') {
		document.sForm.comp_code.value=comp_code;
		document.sForm.comp_name.value=comp_name;
		document.sForm.comp_user.value=comp_user;
		document.sForm.comp_tel.value=comp_tel;
	}
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//��ȹ���� ã��
function OpenCalendar(FieldName) {
	var strUrl = "../mm/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = wopen(strUrl, "Calendar", "200","280","scrollbars=no,toolbar=no,status=no,resizable=no");
}
//�������� ��������
function searchOpInfo() {
	wopen("../mm/searchOPcode.jsp?target=sForm.op_no/sForm.op_name","opcode","250","350","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//��������� ��������
function searchMfgInfo() {
	wopen("../mm/searchName.jsp?target=sForm.mfg_id/sForm.mfg_name","proxy","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//����ó���� ��������
function searchCompInfo() {
	wopen("../mm/searchIndustry.jsp?target=sForm.comp_user/sForm.comp_tel/sForm.comp_name/sForm.comp_code","comp","250","378","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//�۾������� ��������
function searchWorkInfo() {

	var buy_type = "";
	var nm = sForm.buy_type.length;	
	for(i=0; i<nm; i++) {
		if(sForm.buy_type[i].checked) buy_type = document.sForm.buy_type[i].value;
	}
	var factory_no = document.sForm.factory_no.value;
	wopen("../mm/searchWork.jsp?target=sForm.work_no/sForm.work_name/"+factory_no+"/"+buy_type,"comp","250","378","scrollbar=yes,toolbar=no,status=no,resizable=no");
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