<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ ��ȹ LIST"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.mm.entity.mfgOperatorTable table;
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";

	//--------------------------------------
	//��ǰ���� ������ ������ ��������
	//--------------------------------------
	com.anbtech.mm.entity.mfgMasterTable item;
	item = (mfgMasterTable)request.getAttribute("MFG_master");
	String gid = item.getPid();
	String order_status = item.getOrderStatus();
	String mrp_no = item.getMrpNo();
	String mfg_no = item.getMfgNo();
	String item_code = item.getItemCode();
	String factory_no = item.getFactoryNo();
	String order_type = item.getOrderType();

	//--------------------------------------
	//������ ����Ʈ ��������
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ORDER_List");
	table = new mfgOperatorTable();
	Iterator table_iter = table_list.iterator();

	int od_list_cnt = table_list.size();				//��ü ��������	
	int check_cnt = 0;									//�������� �Է¿��� ����
%>

<HTML><HEAD><TITLE>������ ��ȹ LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display()'>
<form name="sForm" method="post" style="margin:0">
<!-- TITLE -->
<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" >
<TBODY>
	<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
	<TR bgcolor="#BAC2CD"><TD valign='middle' class="title" style='padding-left:5px'><img src="../mm/images/blet.gif" align="absmiddle"> ������ȹ</TD></TR></TBODY></TABLE>

<!-- �ܰ� Line -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='88%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>

<!-- ���� -->
<TABLE height="" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25><TD vAlign=center style='padding-left:5px' class='bg_05'  background='../bm/images/title_bm_bg.gif'>������ ��ȹ����</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=27>
			<TD height=27><!--��ư-->
				<TABLE cellSpacing=0 cellPadding=0 border=0>
					<TBODY>
						<TR>
							<TD width='20%' align=left style='padding-left:5px;'>
					<%		if(order_status.equals("2")) { //�������� ���� 
								out.println("<a href='javascript:sendOrder();'><img src='../mm/images/bt_order_conf.gif' align='absmiddle' border='0' alt='����Ȯ��'></a>");	
							} 
					%>
								<a href="javascript:sendCont();"><img src="../mm/images/bt_view_d.gif" align="absmiddle" border='0'></a>
							</TD>
							<TD width='80%' align=left style='padding-lefg:5px;'>
							&nbsp;&nbsp;
							<IMG src='../mm/images/product_com.gif' border='0' align='absmiddle' alt='�������ù�ȣ'><font color='#639DE9'><%=item.getMfgNo()%></font> &nbsp;&nbsp;
							<IMG src='../mm/images/model_code.gif' border='0' align='absmiddle' alt='�𵨸�'> <font color='#639DE9'><%=item.getModelCode()%></font>&nbsp;&nbsp;
							<IMG src='../mm/images/model_name.gif' border='0' align='absmiddle' alt='�𵨸�'> <font color='#639DE9'><%=item.getModelName()%></font>
						</TD>
						</TR></TBODY></TABLE></TD>
		</TR>
		<TR><TD height='2' bgcolor='#9DA8BA'></TD></TR>
		
		<!--����Ʈ-->
		<TR height=100%>
			<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR vAlign=middle height=25>
						  <TD noWrap width=40 align=middle class='list_title'>����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=60 align=middle class='list_title'>���걸��</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=400 align=middle class='list_title'>����ǰ���ȣ / ����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=200 align=middle class='list_title'>�۾��� / ����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>���ۿ�����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>���Ό����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=60 align=middle class='list_title'>�������</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>����Ȯ����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>����ڻ��</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>����ڸ�</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>���ִ����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=120 align=middle class='list_title'>���ֿ���ó</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=200 align=middle class='list_title'>���־�ü��</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=100 align=middle class='list_title'>������ڵ�</TD>
						</TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=28></TD></TR>

		<% 
					while(table_iter.hasNext()){
					table = (mfgOperatorTable)table_iter.next();
					
					String buy_type="";
					if(table.getBuyType().equals("M")) { buy_type = "�系����";		check_cnt++; }
					else if(table.getBuyType().equals("O")) { buy_type = "���ְ���";check_cnt++; }
					else if(table.getBuyType().equals("P")) { buy_type = "����ǰ";	check_cnt++; }
		%>	
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle class='list_bg'><%=table.getOpNo()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=buy_type%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=table.getAssyCode()%> <%=table.getAssySpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=table.getWorkNo()%> <%=table.getWorkName()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getOpStartDate()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getOpEndDate()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getMfgCount()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'> <%=table.getOrderDate()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getMfgId()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getMfgName()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getCompUser()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getCompTel()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getCompName()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getCompCode()%></td>
							
						</TR>
						<TR><TD colSpan=28 background="../mm/images/dot_line.gif"></TD></TR>
		<% 		}  //while 
		%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>


<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="<%=gid%>">
<INPUT type="hidden" name="op_order" size="15" value="">
<INPUT type="hidden" name="check_cnt" size="15" value="<%=check_cnt%>">
<INPUT type="hidden" name="mrp_no" size="15" value="<%=mrp_no%>">
<INPUT type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<INPUT type="hidden" name="item_code" size="15" value="<%=item_code%>">
<INPUT type="hidden" name="order_type" size="15" value="<%=order_type%>">
<INPUT type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
</TD></TR></TABLE>
</FORM>

</BODY>
</HTML>
<SCRIPT language=javascript>
<!--
//�޽��� ����
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}

//������ȹ ����
function assyView(pid)
{
	parent.reg.document.sForm.action='../servlet/mfgInfoServlet';
	parent.reg.document.sForm.mode.value='order_view';
	parent.reg.document.sForm.pid.value=pid;
	parent.reg.document.sForm.submit();
}
//�۾�����Ȯ��
function sendOrder()
{
	var od_list_cnt = '<%=od_list_cnt%>';
	var check_cnt = document.sForm.check_cnt.value; 
	if(od_list_cnt != check_cnt) { 
		alert('������ ������ ������ �ֽ��ϴ�. ���� �Է��� �����Ͻʽÿ�.'); return; 
	} 

	var order_status='<%=order_status%>';
	if(order_status != '2') { 
		if(order_status == '3') status = "����Ȯ��";
		else if(order_status == '4') status = "����������";
		else if(order_status == '5') status = "���긶��";
		alert('�۾����ü��ۼ� ���¿��� �����մϴ�. ���� '+status+' �����Դϴ�.'); return; 
	}

	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='order_act';
	document.sForm.op_order.value='1';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//��������
function sendCont()
{
	var pid = document.sForm.gid.value;
	
	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='mfg_review';
	document.sForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//������ ó���� ��ư����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}

function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//var div_h = h - 645;
	var div_h = c_h - 86;
	
	item_list.style.height = div_h;

}
-->
</SCRIPT>