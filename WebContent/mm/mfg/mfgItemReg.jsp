<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ �����ǰ�ҿ䷮ �����ϱ�"		
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

	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String order_type = (String)request.getAttribute("order_type"); 
	if(order_type == null) order_type = "";

	String order_name = "";
	if(order_type.equals("MANUAL")) order_name = "[��޿���]";
	else if(order_type.equals("MRP")) order_name = "[MRP����]";

	//----------------------------------------------------
	//	�Է�/���� ���� �б�
	//----------------------------------------------------
	String pid="",gid="",mfg_no="",assy_code="",level_no="",item_code="",item_name="",item_spec="";
	String item_unit="",item_type="",item_loss="",draw_count="",mfg_count="";
	String need_count="",spare_count="",add_count="",reserve_count="",request_count="";
	String need_date="",order_date="",factory_no="",factory_name="";

	com.anbtech.mm.entity.mfgItemTable item;
	item = (mfgItemTable)request.getAttribute("MFG_item");

	pid = item.getPid();
	gid = item.getGid();
	mfg_no = item.getMfgNo();
	assy_code = item.getAssyCode();
	level_no = Integer.toString(item.getLevelNo());
	item_code = item.getItemCode();
	item_spec = item.getItemSpec();
	item_unit = item.getItemUnit();
	item_type = item.getItemType();
	need_count = Integer.toString(item.getNeedCount());
	add_count = Integer.toString(item.getAddCount());
	factory_no = item.getFactoryNo();
	order_date = item.getOrderDate();
	
	//----------------------------------------------------
	//	���� ���� �Ǵ��ϱ�
	//----------------------------------------------------
	String icon = "D";						//icon ��¿���
	String rd = "readonly";					//TEXT �������� �����Ǵ��ϱ�
	String ab = "disabled";					//���ùڽ� �������� �����Ǵ��ϱ�
	String ra = "enable";					//Radio Button �������� �Ǵ�
	if(order_date.length() == 0) {									
		rd="";ab=""; icon="E";
	}
	
%>

<HTML>
<HEAD><TITLE>������ �����ǰ�ҿ䷮ �����ϱ�</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<form name="sForm" method="post" style="margin:0" onSubmit="sendModify();return false;">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='100%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TBODY>
			<TR height=25><!-- Ÿ��Ʋ �� ������ ���� -->
				<TD vAlign="center" height=25 class="bg_05" style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'> ��ǰ�ҿ䷮���� <%=order_name%></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			<TR height="25"><TD vAlign="center" align='left' style='padding-left:5px;'  bgcolor=''>
			
			<% if(item_code.length()>1) { //����
			%>	<INPUT type='image' onfocus=blur() src="../mm/images/bt_modify.gif" border=0 align='absmiddle'>
			<% }
			%>
				
			</TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			<TR>
				<TD vAlign=top>
				<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
						<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
							<TBODY>
								<tr><td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">ǰ���ڵ�</td>
									<td width="37%" height="25" class="bg_04"><%=item_code%>
										<input type="hidden" name="factory_no" value="<%=factory_no%>" size="12">
									</td>
									<td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">ǰ��԰�</td>
									<td width="37%" height="25" class="bg_04"><%=item_spec%>
										<input type="hidden" name="item_spec" value="<%=item_spec%>" size="10"> 
									</td>
								</tr>
								<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
								<tr><td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����ʿ����</td>
									<td width="37%" height="25" class="bg_04"><%=need_count%>
										<input type="hidden" name="need_count" value="<%=need_count%>" size="10"></td>
									<td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�߰�����</td>
									<td width="37%" height="25" class="bg_04">
										<input type="text" name="add_count" value="<%=add_count%>" size="10" <%=rd%>> </td>
								</tr>
								<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></TR>
							</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='gid' value='<%=gid%>'>
<input type='hidden' name='mfg_no' value='<%=mfg_no%>'>
<input type='hidden' name='assy_code' value='<%=assy_code%>'>
<input type='hidden' name='level_no' value='<%=level_no%>'>
<input type='hidden' name='factory_no' value='<%=factory_no%>'>
<input type='hidden' name='order_type' value='<%=order_type%>'>
</form>

</BODY>
</HTML>
<script language=javascript>
<!--
//�����ϱ�
function sendModify()
{
	var add_count = document.sForm.add_count.value;
	var item_code = '<%=item_code%>';
	if(item_code.length==0){ alert(item_code.length+"������ ��ǰ�� �����Ͻʽÿ�.");return;}
	if(isNaN(add_count)) { alert('���ڸ� �Է��� �����մϴ�.'); return; }
	else if(add_count.indexOf('.') != -1) { alert('������ �Է��� �����մϴ�.'); return; }
	else if(add_count.indexOf('-') != -1) { 
		//����ɺ�ǰ(����ǰ)�� ���̳ʽ��� ����.
		item_type = '<%=item_type%>';
		if(item_type == '4') {alert('�ڿ����� �Է��� �����մϴ�.'); return; } 
	}
 
	var v = confirm('�ش系���� �����Ͻðڽ��ϱ�?'); 
	if(v == false) { return; }

	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='item_modify';
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


function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var div_h = h - 700 ;
	
	item_list.style.height = div_h;

}

-->
</script>