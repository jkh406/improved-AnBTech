<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "��ǰ����Ƿ� LIST"		
	contentType = "text/html; charset=KSC5601" 		
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
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	
	//--------------------------------------
	//��ǰ����Ƿ� ������ ������ ��������
	//--------------------------------------
	com.anbtech.mm.entity.mfgReqMasterTable master = new com.anbtech.mm.entity.mfgReqMasterTable();
	master = (mfgReqMasterTable)request.getAttribute("REQ_master");
	String req_status = master.getReqStatus();
	String mfg_no = master.getMfgNo();
	String mfg_req_no = master.getMfgReqNo();
	String assy_code = master.getAssyCode();
	String level_no = Integer.toString(master.getLevelNo());
	String assy_spec = master.getAssySpec();
	String factory_no = master.getFactoryNo();
	String factory_name = master.getFactoryName();

	//--------------------------------------
	//��ǰ����Ƿ� ����Ʈ ��������
	//--------------------------------------
	com.anbtech.mm.entity.mfgReqItemTable item = new com.anbtech.mm.entity.mfgReqItemTable();
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("REQ_ITEM_List");
	Iterator item_iter = item_list.iterator();

%>

<HTML><HEAD><TITLE>��ǰ����Ƿ� LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display();'>
<form name="sForm" method="post" style="margin:0">

<!--��� TITLE-->
<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" >
<TBODY>
<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
<TR bgcolor="#BAC2CD"><TD valign='middle' class="title" style='padding-left:5px'><img src="../mm/images/blet.gif" align="absmiddle"> ��ǰ ����Ƿ� ��Ȳ</TD></TR></TBODY></TABLE>

<!---�ܰ� Line--->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='93%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>

<!-- �� ���� -->
<TABLE height="" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25>
			<TD vAlign=center style='padding-left:5px' class='bg_05'  background='../bm/images/title_bm_bg.gif'> ��ǰ ����Ƿ� ��Ȳ</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=27>
			<TD height=27><!--��ư-->
				<TABLE cellSpacing=0 cellPadding=0 border=0>
					<TBODY>
						<TR>
							<TD width='10%' align=left  style='padding-left:5px;'>
							<a href="javascript:sendConfirm();"><img src="../mm/images/bt_itemout_req.gif" align="absmiddle" border='0'></a>
						</TD>
						<TD width='90%' align=left >
							<img src="../mm/images/item_out_no.gif" align="absmiddle" alt='��ǰ����ȣ' border='0'> <font color='#639DE9'><%=mfg_req_no%></font> &nbsp;
							<img src="../mm/images/selitem_code.gif" align="absmiddle" alt='��ǰ��' border='0'>
							<font color='#639DE9'><%=assy_code%></font> &nbsp;
							<img src="../mm/images/description.gif" align="absmiddle" alt='description' border='0'>
							<font color='#639DE9'><%=assy_spec%></font>
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
							<TD noWrap width=90 align=middle class='list_title'>ǰ���ڵ�</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=100% align=middle class='list_title'>ǰ�񼳸�</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>��û����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=40 align=middle class='list_title'>����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=40 align=middle class='list_title'>����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=90 align=middle class='list_title'>�����</TD>
						</TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>

		<% 
				while(item_iter.hasNext()){
				item = (mfgReqItemTable)item_iter.next();
		%>	
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
							<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getReqCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemUnit()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemType()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getReqDate()%></td>					
						</TR>
						<TR><TD colSpan=11 background="../mm/images/dot_line.gif"></TD></TR>
							<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
							<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getReqCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemUnit()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemType()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getReqDate()%></td>					
						</TR>
						<TR><TD colSpan=11 background="../mm/images/dot_line.gif"></TD></TR>
		<% 		}  //while 
		%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="pid" size="15" value="">
<input type="hidden" name="mfg_req_no" size="15" value="<%=mfg_req_no%>">
<input type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<input type="hidden" name="assy_code" size="15" value="<%=assy_code%>">
<input type="hidden" name="level_no" size="15" value="<%=level_no%>">
<input type="hidden" name="item_code" size="15" value="">
<input type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
<input type="hidden" name="factory_name" size="15" value="<%=factory_name%>">
<input type="hidden" name="req_status" size="15" value="">
</TD></TR></TABLE>
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
//�Ƿں�ǰ ��������
function itemView(pid,item_code)
{
	var req_status = '<%=req_status%>';
	if(req_status != '1') { 
		if(req_status == '2') status = "����û";
		else if(req_status == '3') status = "��ǰ���";
		alert('��ǰ����ۼ� ���¿��� �����մϴ�. ���� '+status+' �����Դϴ�.'); return; 
	}

	var mfg_no = document.sForm.mfg_no.value;
	var assy_code = document.sForm.assy_code.value;
	var level_no = document.sForm.level_no.value;
	var factory_no = document.sForm.factory_no.value;

	parent.reg.document.sForm.action='../servlet/mfgOrderServlet';
	parent.reg.document.sForm.mode.value='out_preview';
	parent.reg.document.sForm.pid.value=pid;
	parent.reg.document.sForm.mfg_no.value=mfg_no;
	parent.reg.document.sForm.assy_code.value=assy_code;
	parent.reg.document.sForm.level_no.value=level_no;
	parent.reg.document.sForm.item_code.value=item_code;
	parent.reg.document.sForm.factory_no.value=factory_no;
	parent.reg.document.sForm.submit();
}
//�Ƿں�ǰ����û
function sendConfirm()
{
	var req_status = '<%=req_status%>';
	if(req_status != '1'){
		if(req_status == '2') status = "����û";
		else if(req_status == '3') status = "��ǰ���";
		alert('��ǰ����ۼ� ���¿��� �����մϴ�. ���� '+status+' �����Դϴ�.'); return; 
	}
	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='out_confirm';
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
	//var div_h = h - 478;
	var div_h = c_h - 86;
	
	item_list.style.height = div_h;
}
-->
</script>