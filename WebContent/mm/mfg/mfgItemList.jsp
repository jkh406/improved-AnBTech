<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ �ҿ䷮���� ��ȹ LIST"		
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
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//����
	com.anbtech.mm.entity.mfgItemTable assy;
	com.anbtech.mm.entity.mfgItemTable item;
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	String assy_code = (String)request.getAttribute("assy_code"); if(assy_code == null) assy_code = "";

	//--------------------------------------
	//��ǰ���� ������ ������ ��������
	//--------------------------------------
	com.anbtech.mm.entity.mfgMasterTable master;
	master = (mfgMasterTable)request.getAttribute("MFG_master");
	String gid = master.getPid();
	String order_status = master.getOrderStatus();
	String mfg_no = master.getMfgNo();
	String factory_no = master.getFactoryNo();
	String factory_name = master.getFactoryName();
	String item_code = master.getItemCode();
	String order_type = master.getOrderType();

	//--------------------------------------
	//���� ����Ʈ ��������
	//--------------------------------------
	ArrayList assy_list = new ArrayList();
	assy_list = (ArrayList)request.getAttribute("ASSY_List");
	assy = new mfgItemTable();
	Iterator assy_iter = assy_list.iterator();

	//--------------------------------------
	//�ش������ ��ǰ ����Ʈ ��������
	//--------------------------------------
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_List");
	item = new mfgItemTable();
	Iterator item_iter = item_list.iterator();

%>

<HTML><HEAD><TITLE>������ �ҿ䷮���� ��ȹ LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display();'>
<form name="sForm" method="post" style="margin:0">

<!--��� TITLE-->
<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" >
<TBODY>
<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
<TR bgcolor="#BAC2CD"><TD valign='middle' class="title" style='padding-left:5px'><img src="../mm/images/blet.gif" align="absmiddle"> ��ǰ����</TD></TR></TBODY></TABLE>

<!---�ܰ� Line--->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='93%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>

<!-- �� ���� -->
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25>
			<TD vAlign=center style='padding-left:5px' class='bg_05'  background='../bm/images/title_bm_bg.gif'>������ ����ǰ �ҿ䷮����</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=27>
			<TD height=27><!--��ư-->
				<TABLE cellSpacing=0 cellPadding=0 border=0>
					<TBODY>
						<TR>
							<TD width='10%' align=left noWrap class="bg_03" style='padding-left:5px;'>
		
							<a href="javascript:sendCont();"><img src="../mm/images/bt_view_d.gif" align="absmiddle" border='0'></a>
			<%
						if(order_status.equals("3")) {		//����Ȯ������ 
						out.println("<a href='javascript:itemDelivery();'><img src='../mm/images/bt_itemout_req.gif' align='absmiddle' border='0' alt='��������Ƿ�'></a>");
						} 
			%>
						<select name="assy" style=font-size:9pt;color="black"; onChange='javascript:goAssy();'>  
			<%
						String sel = "";
						while(assy_iter.hasNext()){
							assy = (mfgItemTable)assy_iter.next();
							if(assy_code.equals(assy.getAssyCode())) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+assy.getAssyCode()+"|"+assy.getLevelNo()+"'>");
							out.println(assy.getAssyCode()+"</option>");
						}
				%>
						</select>	
						</TD>
						<TD width='90%' align=left noWrap>
							<IMG src='../mm/images/product_com.gif' border='0' align='absmiddle' alt='�������ù�ȣ'><font color='#639DE9'><%=master.getMfgNo()%></font>&nbsp;&nbsp;
							<IMG src='../mm/images/model_code.gif' border='0' align='absmiddle' alt='�𵨸�'> <font color='#639DE9'><%=master.getModelCode()%></font>&nbsp;&nbsp;
							<IMG src='../mm/images/model_name.gif' border='0' align='absmiddle' alt='�𵨸�'> <font color='#639DE9'><%=master.getModelName()%></font>

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
			  <TD noWrap width=90 align=middle class='list_title'>ǰ���ȣ</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>DESC</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=30 align=middle class='list_title'>����</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
					    <TD noWrap width=60 align=middle class='list_title'>BOM����</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=65 align=middle class='list_title'>����ҿ䷮</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=60 align=middle class='list_title'>�߰���</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=65 align=middle class='list_title'>����û��</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=40 align=middle class='list_title'>����</TD>
						</TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>

		<% 
					while(item_iter.hasNext()){
					item = (mfgItemTable)item_iter.next();
		%>	
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle class='list_bg'><%=item.getItemCode()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemType()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getDrawCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getNeedCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getAddCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getReserveCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemUnit()%></td>
							
						</TR>
						<TR><TD colSpan=15 background="../mm/images/dot_line.gif"></TD></TR>					
		<% 		}  //while 
		%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

</TD></TR></TABLE>
<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="<%=gid%>">
<INPUT type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<INPUT type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
<INPUT type="hidden" name="factory_name" size="15" value="<%=factory_name%>">
<INPUT type="hidden" name="level_no" size="15" value="">
<INPUT type="hidden" name="assy_code" size="15" value="">
<INPUT type="hidden" name="order_status" size="15" value="">
<INPUT type="hidden" name="item_code" size="15" value="<%=item_code%>">
<INPUT type="hidden" name="order_type" size="15" value="<%=order_type%>">

</FORM>

</BODY>
</HTML>


<script language=javascript>
<!--
//�޽��� ����
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}
//�����ٲٱ�
function goAssy()
{
	var assy = document.sForm.assy.value.split("|");

	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='item_list';
	document.sForm.assy_code.value=assy[0];
	document.sForm.level_no.value=assy[1];
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//������ǰ�ҿ䷮���� ����
function itemView(pid) 
{
	var order_status = '<%=order_status%>';
	if(order_status != '2') { 
		if(order_status == '3') status = "����Ȯ��";
		else if(order_status == '4') status = "��ǰ���";
		else if(order_status == '5') status = "����������";
		else if(order_status == '6') status = "���긶��";
		alert('�������ü��ۼ� ���¿��� �����մϴ�. ���� '+status+' �����Դϴ�.'); return; 
	}

	var order_type = document.sForm.order_type.value;

	parent.reg.document.sForm.action='../servlet/mfgInfoServlet';
	parent.reg.document.sForm.mode.value='item_view';
	parent.reg.document.sForm.pid.value=pid;
	parent.reg.document.sForm.order_type.value=order_type;
	parent.reg.document.sForm.submit();
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
//�����ϰ� ����Ƿ�
function itemDelivery() 
{
	var order_status = '<%=order_status%>';
	if(order_status != '3') { 
		if(order_status == '2') status = "�������ü��ۼ�";
		else if(order_status == '4') status = "��ǰ���";
		else if(order_status == '5') status = "����������";
		else if(order_status == '6') status = "���긶��";
		alert('����Ȯ�� ���¿��� �����մϴ�. ���� '+status+' �����Դϴ�.'); return; 
	}
	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='item_delivery';
	document.sForm.order_status.value='4';
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
	//var div_h = h - 485;
	var div_h = c_h - 86;
	
	item_list.style.height = div_h;
}

-->
</script>