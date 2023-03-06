<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MRP�ҿ䷮ LIST"		
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

	com.anbtech.mm.entity.mrpMasterTable mrp;
	com.anbtech.mm.entity.mrpItemTable item;
	com.anbtech.mm.entity.mrpItemTable assy;
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	String assy_code = (String)request.getAttribute("assy_code"); if(assy_code == null) assy_code = "";

	//--------------------------------------
	//MRP MASTER����
	//--------------------------------------
	String pid="",mps_no="",mrp_no="",mrp_start_date="",mrp_end_date="",model_code="",model_name="";
	String fg_code="",item_code="",item_name="",item_spec="",p_count="",item_unit="",mrp_status="";
	String factory_name="",reg_date="",reg_id="",reg_name="",pu_dev_date="",pu_req_no="",stock_link="";
	String factory_no="";

	mrp = (mrpMasterTable)request.getAttribute("MRP_master");

	pid = mrp.getPid();
	mps_no = mrp.getMpsNo();
	mrp_no = mrp.getMrpNo();
	mrp_start_date = mrp.getMrpStartDate();if(mrp_start_date.length() ==0) mrp_start_date = anbdt.getDate(0);
	mrp_end_date = mrp.getMrpEndDate();
	model_code = mrp.getModelCode();
	model_name = mrp.getModelName();
	fg_code = mrp.getFgCode();

	item_code = mrp.getItemCode();
	item_name = mrp.getItemName();
	item_spec = mrp.getItemSpec();
	p_count = Integer.toString(mrp.getPCount());
	item_unit = mrp.getItemUnit();
	mrp_status = mrp.getMrpStatus();		if(mrp_status.length() ==0) mrp_status = "";

	factory_no = mrp.getFactoryNo();
	factory_name = mrp.getFactoryName();
	reg_date = mrp.getRegDate();			if(reg_date.length() ==0) reg_date = anbdt.getDate(0);
	reg_id = mrp.getRegId();				if(reg_id.length() ==0) reg_id = sl.id;
	reg_name = mrp.getRegName();			if(reg_name.length() ==0) reg_name = sl.name;
	pu_dev_date = mrp.getPuDevDate();		if(pu_dev_date.length() ==0) pu_dev_date = anbdt.getDate(0);
	pu_req_no = mrp.getPuReqNo();
	stock_link = mrp.getStockLink();

	//�ӽ÷� �ҿ䷮ �����
	if(item_code.length() == 0) {
		item_code = (String)request.getAttribute("item_code"); 
		p_count = (String)request.getAttribute("p_count"); 
	}

	//--------------------------------------
	//�ҿ䷮ ����Ʈ ��������
	//--------------------------------------
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_List");
	item = new mrpItemTable();
	Iterator item_iter = item_list.iterator();

	//--------------------------------------
	//ASSY ����Ʈ ��������
	//--------------------------------------
	ArrayList assy_list = new ArrayList();
	assy_list = (ArrayList)request.getAttribute("ASSY_List");
	assy = new mrpItemTable();
	Iterator assy_iter = assy_list.iterator();
	
	String caption = "";
	
	if(mrp_status.equals("1")){
		caption = "����";
	} else if(mrp_status.equals("4")) {
		caption = "��ȸ";
	}

%>

<HTML><HEAD><TITLE>MRP�ҿ䷮ LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display();'>
<FORM name="sForm" method="post" style="margin:0">
<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" >
<!-- ��� TITLE-->
<TBODY>
	<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
	<TR bgcolor="#BAC2CD"><TD valign='middle' class="title" style='padding-left:5px'><img src="../mm/images/blet.gif" align="absmiddle"> MRP <%=caption%></TD></TR></TBODY></TABLE>

<!-- �ܰ� line -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='95%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>

<!-- ���� -->
<TABLE height="" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25>
			<TD vAlign=center style='padding-left:5px' class='bg_05'  background='../bm/images/title_bm_bg.gif'>�ҿ䷮������Ȳ</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=27>
			<TD height=27><!--��ư-->
				<TABLE cellSpacing=0 cellPadding=0 border=0>
					<TBODY>
						<TR><TD align=left width='250' height="24" style='padding-left:5px;'> 
							<a href="javascript:sendCont();"><img src="../mm/images/bt_view_d.gif" align="absmiddle" border='0'></a>
							<select name="assy_code" style=font-size:9pt;color="black";  onChange='javascript:selectAssy();'> 
							<OPTGROUP label='------------'>
							<%
								String asel = "";
								while(assy_iter.hasNext()) {
									assy = (mrpItemTable)assy_iter.next(); 
									if(assy_code.equals(assy.getAssyCode())) asel = "selected";
									else asel = "";
									out.print("<option "+asel+" value='"+assy.getAssyCode()+"'>");
									out.println(assy.getLevelNo()+": "+assy.getAssyCode()+"</option>");
						} 
					%></select></TD>
							<TD width='580' align=left>
							<IMG src='../mm/images/factory_name.gif' border='0' align='bottom' alt='����'>
							&nbsp;<font color='#639DE9' ><%=factory_no%></font>
							&nbsp;&nbsp;
							<IMG src='../mm/images/mrp_no.gif' border='0' align='absmiddle' alt='MRP��ȣ'>
							&nbsp;<font color='#639DE9'><%=mrp_no%></font>
							&nbsp;&nbsp;
							<IMG src='../mm/images/item_code.gif' border='0' align='absmiddle' alt='ǰ���ڵ�'>
							&nbsp;<font color='#639DE9' ><%=item_code%></font>
							&nbsp;&nbsp;
							<IMG src='../mm/images/product_s.gif' border='0' align='absmiddle' alt='�������'>
							&nbsp;<font color='#639DE9' ><%=nfm.toDigits(Integer.parseInt(p_count))%></font></TD>
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
						  <TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=50 align=middle class='list_title'>LEVEL</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>��ǰ���ڵ�</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>��ǰ���ڵ�</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=300 align=middle class='list_title'>����ǰ�񼳸�</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>BOM����</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>����ҿ䷮</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>��������</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>����������</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>�߰�����</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>���ſ䱸����</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=60 align=middle class='list_title'>ǰ�����</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
						  <TD noWrap width=40 align=middle class='list_title'>����</TD>
						  <!--<TD noWrap width=80 align=middle class='list_title'>�԰�������</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD> -->
						</TR> 
						
						<TR bgColor=#9DA9B9 height=1><TD colspan=28></TD></TR>

		<%  int cnt = 1;
			while(item_iter.hasNext()) {
				item = (mrpItemTable)item_iter.next();
				int lv = item.getLevelNo();
				String space="&nbsp;";
				for(int i=1; i<lv; i++) space += "&nbsp;";
		%>	
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle class='list_bg'><%=cnt%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left ><%=space%><%=item.getLevelNo()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg'><%=item.getAssyCode()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg'><%=item.getItemCode()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg'>&nbsp;<%=item.getItemSpec()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=nfm.toDigits(item.getDrawCount())%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=nfm.toDigits(item.getNeedCount())%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=nfm.toDigits(item.getStockCount())%></TD> 
					      <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=nfm.toDigits(item.getPlanCount())%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=nfm.toDigits(item.getAddCount())%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=nfm.toDigits(item.getMrsCount())%></TD>
						  <TD><IMG height=1 width=1></TD>
						
						  <TD align=middle class='list_bg'>&nbsp;<%=item.getItemType()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=item.getItemUnit()%></TD> 
						<!--<TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=nfm.toDigits(item.getOpenCount())%></TD>-->
						</TR>
						<TR><TD colSpan=28 background="../mm/images/dot_line.gif"></TD></TR>
		<% 		cnt++;
				}  //while 
		%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value='<%=pid%>'>
<INPUT type="hidden" name="mrp_no" value='<%=mrp_no%>'>
<INPUT type="hidden" name="fg_code" value='<%=fg_code%>'>
<INPUT type="hidden" name="item_code" value=''>
<INPUT type="hidden" name="factory_no" value='<%=factory_no%>'>
<INPUT type="hidden" name="mrp_status" value='<%=mrp_status%>'>
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

//�ҿ䷮ �����غ�
function itemView(pid)
{
	var mrp_status = '<%=mrp_status%>'; 
	var factory_no = '<%=factory_no%>';
	var mrp_no = '<%=mrp_no%>';
	var fg_code = '<%=fg_code%>';
	var item_code = '<%=item_code%>';
	var gid = '<%=pid%>';
 
	//ASSY��������
	var b = document.sForm.assy_code.selectedIndex;
	if(b != -1) {
		var assy_code = document.sForm.assy_code.options[b].value;
	}


	parent.reg.document.sForm.action='../servlet/mrpInfoServlet';
	parent.reg.document.sForm.mode.value='item_presave';
	parent.reg.document.sForm.pid.value=pid;
	parent.reg.document.sForm.gid.value=gid;
	parent.reg.document.sForm.mrp_no.value=mrp_no;
	parent.reg.document.sForm.fg_code.value=fg_code;
	parent.reg.document.sForm.item_code.value=item_code;
	parent.reg.document.sForm.factory_no.value=factory_no;
	parent.reg.document.sForm.mrp_status.value=mrp_status;
	parent.reg.document.sForm.assy_code.value=assy_code;
	parent.reg.document.sForm.submit();

}
//ASSY�����ϱ�
function selectAssy()
{
	//ASSY��������
	var b = document.sForm.assy_code.selectedIndex;
	if(b != -1) {
		var assy_code = document.sForm.assy_code.options[b].value;
	}

	//���ó��� ����
	document.sForm.action='../servlet/mrpInfoServlet';
	document.sForm.mode.value='item_list';
	document.sForm.item_code.value=assy_code;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//���������� ����
function sendCont()
{
	document.sForm.action='../servlet/mrpInfoServlet';
	document.sForm.mode.value='mrp_review';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//������ ó���� ��ư����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}

// ������������ ���������� �и��� �ʵ��� ó��
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//var div_h = h - 486;
	var div_h = c_h - 91;
	item_list.style.height = div_h;
}
-->
</SCRIPT>