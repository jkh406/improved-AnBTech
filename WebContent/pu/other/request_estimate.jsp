<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%!
	RequestInfoTable table;
%>

<%
	String mode			= request.getParameter("mode");
	String sender_email = request.getParameter("email");

	//����Ʈ ��������
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new RequestInfoTable();
	Iterator table_iter = item_list.iterator();
%>
<HTML>
<LINK rel="stylesheet" type="text/css" href="../pu/css/style.css">
<HEAD>
<TITLE>�����Ƿ� ���Ϲ߼�</TITLE>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display()'>
<FORM name="f1" method="post" action="PurchaseOtherMgrServlet" enctype="multipart/form-data">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
			<TD vAlign=top>
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
						<TR><TD height="3" bgcolor="0C2C55" colspan='2'></TD></TR>
						<TR><TD height="33" valign="middle" bgcolor="#73AEEF" ><img src="../pu/images/pop_gyun_mail.gif" border="0" alt="�����Ƿ� ���Ϲ߼�"></TD>
							<TD style="padding-right:10px" align='right' valign='middle'  bgcolor="#73AEEF"></TD></TR>
						<TR><TD height='1' bgcolor='#9DA8BA' colspan='2'></TD></TR></TBODY></TABLE></TD></TR>
	
		<TR height=32><!--��ư �� ����¡-->
			<TD vAlign=top>
				<TABLE height=32 cellSpacing=0 cellPadding=0 width="98%" border='0' align='center'>
					<TBODY>
						<TR><TD height='20' colspan="3"></TD></TR>
						<tr bgcolor="c7c7c7"><td height="1" colspan="3"></td></tr>
						<tr>
							<td width="30%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�߽��ڸ����ּ�</td>
							<td width="70%" height="25" class="bg_04"><input type='text' size='30' name='sender_email' value='<%=sender_email%>' class="text_01"></td></tr>
						<tr bgcolor="c7c7c7"><td height="1" colspan="3"></td></tr>
						<tr>
							<td width="30%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�����ڸ����ּ�</td>
							<td width="70%" height="25" class="bg_04"><input type='text' size='30' name='receiver_email' value='' class="text_01"></td></tr>
						<tr bgcolor="c7c7c7"><td height="1" colspan="3"></td></tr></TBODY></TABLE></TD></TR>
  
		<TR><TD align="middle">
		<TABLE border=0 width='98%'><TR><TD align=left><IMG src='../pu/images/title_req_item.gif' border='0' alt='�����Ƿ�ǰ��'></TD></TR></TABLE>
		</TD></TR>
		<TR height=100%><!--����Ʈ-->
			<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:scroll;">
				<TABLE cellSpacing=0 cellPadding=0 width="98%" align='center'>
					<TBODY>
					<TR><TD height='2' bgcolor='#9CA9BA' colspan='13'></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>ǰ���ڵ�</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>ǰ���</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>ǰ�񼳸�</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>����</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>����</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>����԰���</TD>
					</TR>			
					<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%	
			int no = 1;
				String item_codes = "";
				while(table_iter.hasNext()){
					table = (RequestInfoTable)table_iter.next();
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					  <TD align=middle height="24" class='list_bg'><%=no%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getItemCode()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getItemName()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getRequestQuantity()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getRequestUnit()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getDeliveryDate()%></TD>
					</TR>
					<TR><TD colSpan=13 background="../pu/images/dot_line.gif"></TD></TR>
<%				
				no++;
				item_codes += table.getRequestNo() + "|" + table.getItemCode() + ",";
			}
%>
				</TBODY></TABLE></DIV></TD></TR>

		<!--������-->
        <TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
			<a href="javascript:send_mail();"><img src="../pu/images/bt_export.gif" border="0" align='absmiddle' alt="���Ϲ߼�"></a> <a href="javascript:self.close();"><img src="../pu/images/bt_cancel.gif" border="0" align='absmiddle'></a></TD></TR>
        <TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR>
     </TBODY></TABLE></TD></TR></TABLE>
<input type="hidden" name="item_code" value="<%=item_codes%>">
			<input type="hidden" name="mode" value="<%=mode%>">


</FORM>
</BODY>
</HTML>

<script language=javascript>
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function send_mail() 
{ 
	var f = document.f1;

	if(f.sender_email.value == ''){
		alert("�߽��� ���ڿ����ּҸ� �Է��Ͻʽÿ�.");
		f.sender_email.focus();
		return;
	}

	if(f.receiver_email.value == ''){
		alert("������ ���ڿ����ּҸ� �Է��Ͻʽÿ�.");
		f.receiver_email.focus();
		return;
	}

	if (!CheckEmail(f.sender_email.value)){
		alert("�߽��� �����ּҸ� �ùٸ��� �Է��Ͻʽÿ�.");
		f.sender_email.focus();
		return;
    }

	if (!CheckEmail(f.receiver_email.value)){
		alert("������ �����ּҸ� �ùٸ��� �Է��Ͻʽÿ�.");
		f.receiver_email.focus();
		return;
    }

	f.submit();
}

//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//���޾�üã��
function sel_supplyer() 
{ 
	var item_code = document.request_form.item_code.value;
	url = "../pu/config/search_item_supply_info.jsp?item_code="+item_code+"&sf=request_form&sid=supplyer_code&sname=supplyer_name&scost=supply_cost";
	wopen(url,'add','600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function CheckEmail(strEmail) {
    var regDoNot = /(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/; 
    var regMust = /^[a-zA-Z0-9\-\.\_]+\@[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3})$/;
    if ( !regDoNot.test(strEmail) && regMust.test(strEmail) )
        return true;
    else
      return false;
} 

function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	var div_h = c_h - 180;
	
	item_list.style.height = div_h;
}
</script>