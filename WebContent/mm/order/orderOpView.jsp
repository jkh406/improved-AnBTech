<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�۾����� �󼼳���"		
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

	//----------------------------------------------------
	//	�Է�/���� ���� �б�
	//----------------------------------------------------
	String pid="",gid="",mfg_no="",assy_code="",assy_spec="",level_no="",mfg_count="",mfg_unit="";
	String op_start_date="",op_end_date="",order_date="",buy_type="",factory_no="",factory_name="";
	String work_no="",work_name="",op_no="",op_name="",mfg_id="",mfg_name="",note="";
	String comp_code="",comp_name="",comp_user="",comp_tel="",op_order="";

	com.anbtech.mm.entity.mfgOperatorTable item;
	item = (mfgOperatorTable)request.getAttribute("MFG_operator");

	pid = item.getPid();
	gid = item.getGid();
	mfg_no = item.getMfgNo();
	assy_code = item.getAssyCode();
	assy_spec = item.getAssySpec();
	level_no = Integer.toString(item.getLevelNo());
	mfg_count = nfm.toDigits(item.getMfgCount());
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

%>

<HTML>
<HEAD><TITLE>�������� �󼼳���</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../mm/images/blet.gif"> ���۾����ó���</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<a href="javascript:itemRequest();"><IMG src='../mm/images/bt_out_reg.gif' border='0' align='absmiddle' alt='��ǰ�����'></a>
					<a href="javascript:saveProduct();"><IMG src='../mm/images/bt_product_reg.gif' border='0' align='absmiddle' alt='����������'></a>
					<a href="javascript:viewMaster();"><IMG src='../mm/images/bt_view_d.gif' border='0' align='absmiddle' alt='������'></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�������ù�ȣ</td>
			   <td width="37%" height="25" class="bg_04"><%=mfg_no%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��������</td>
			   <td width="37%" height="25" class="bg_04"><%=order_date%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��������ڵ�</td>
			   <td width="37%" height="25" class="bg_04"><%=factory_no%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��������</td>
			   <td width="37%" height="25" class="bg_04"><%=factory_name%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�۾����ȣ</td>
			   <td width="37%" height="25" class="bg_04"><%=work_no%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�۾����</td>
			   <td width="37%" height="25" class="bg_04"><%=work_name%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<TR><TD height='5px'></TD></TR>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ǰ���ڵ�</td>
			   <td width="37%" height="25" class="bg_04"><%=assy_code%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ǰ�񼳸�</td>
			   <td width="37%" height="25" class="bg_04"><%=assy_spec%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�������</td>
			   <td width="37%" height="25" class="bg_04"><%=mfg_count%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�������</td>
			   <td width="37%" height="25" class="bg_04"><%=mfg_unit%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����ȹ������</td>
			   <td width="37%" height="25" class="bg_04"><%=op_start_date%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����ȹ������</td>
			   <td width="37%" height="25" class="bg_04"><%=op_end_date%></td>
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
						out.print("<input type='radio' "+sel+" value=''>"+sl_data[i]);
					}
				%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�������</td>
			   <td width="37%" height="25" class="bg_04">
				<%	String op_sel = "";
					String[] op_data = {"�۾�����","���縶��","����Ϸ�"};
					String[] op_value = {"1","2","3"};
					for(int i=0; i<op_data.length; i++) {
						if(op_order.equals(op_value[i])) op_sel="checked";
						else op_sel = "";
						out.print("<input type='radio' "+op_sel+" value=''>"+op_data[i]);
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ó��ȣ</td>
			   <td width="37%" height="25" class="bg_04"><%=comp_code%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����ó��</td>
			   <td width="37%" height="25" class="bg_04"><%=comp_name%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���ִ����</td>
			   <td width="37%" height="25" class="bg_04"><%=comp_user%></td>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���ֿ���ó</td>
			   <td width="37%" height="25" class="bg_04"><%=comp_tel%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">���û���</td>
			   <td width="87%" height="25" class="bg_04" colspan=3>
					<TEXTAREA NAME="note" rows='4' cols='38' readonly><%=note%></TEXTAREA></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<form name="sForm" method="post" style="margin:0">
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='gid' value='<%=gid%>'>
<input type='hidden' name='mfg_no' value='<%=mfg_no%>'>
<input type='hidden' name='assy_code' value='<%=assy_code%>'>
<input type='hidden' name='assy_spec' value='<%=assy_spec%>'>
<input type='hidden' name='level_no' value='<%=level_no%>'>
<input type='hidden' name='factory_no' value='<%=factory_no%>'>
<input type='hidden' name='factory_name' value='<%=factory_name%>'>
<input type='hidden' name='mfg_count' value='<%=mfg_count%>'>
</form>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">������ ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>
<script language=javascript>
<!--
//��ǰ�����
function itemRequest()
{
	var op_order = '<%=op_order%>';
	if(op_order != '1') {
		if(op_order == '2') status = "���縶��";
		else if(op_order == '3') status = "����Ϸ�";
		alert('�۾����� ���¿��� �����մϴ�. ���� '+status+' �����Դϴ�.'); return; 
	}

	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='out_create';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//����������
function saveProduct()
{
	
	var mfg_no = document.sForm.mfg_no.value;
	var assy_code = document.sForm.assy_code.value;
	var level_no = document.sForm.level_no.value;
	var factory_no = document.sForm.factory_no.value;
	var mfg_count = document.sForm.mfg_count.value;
	var para = "mfg_no="+mfg_no+"&assy_code="+assy_code+"&level_no="+level_no;
		para +="&factory_no="+factory_no+"&mfg_count="+mfg_count;

	document.sForm.action='../mm/order/productFrame.jsp?'+para;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//�۾����� ������ ����
function viewMaster()
{
	var gid = document.sForm.gid.value;
	var url = "../servlet/mfgOrderServlet?mode=order_mview&gid="+gid;	
	wopen(url,"master_view",'700','520','scrollbars=yes,toolbar=no,status=no,resizable=no');
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