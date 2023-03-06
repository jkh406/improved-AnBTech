<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%!
	RequestInfoTable table;
	PurchaseLinkUrl redirect;
%>

<%
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	String mode = request.getParameter("mode");	// ���

	table = (RequestInfoTable)request.getAttribute("REQUEST_INFO");
	String request_no			= table.getRequestNo();
	String request_date			= table.getRequestDate();
	String requester_div_code	= table.getRequesterDivCode();
	String requester_div_name	= table.getRequesterDivName();
	String requester_id			= table.getRequesterId();
	String requester_info		= table.getRequesterInfo();
	String request_total_mount	= sp.getMoneyFormat(table.getRequestTotalMount(),"");

	//String item_code			= table.getItemCode();
	String item_name			= table.getItemName();
	String item_desc			= table.getItemDesc();
	String delivery_date		= table.getDeliveryDate();
	String request_quantity		= table.getRequestQuantity();
	String request_type			= table.getRequestType();
	String project_code			= table.getProjectCode();	// �����ڵ�(�����߰�)
	String project_name			= table.getProjectName();	// ������(�����߰�)

	//��ûǰ�� ����Ʈ ��������
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new RequestInfoTable();
	Iterator table_iter = item_list.iterator();


	//��ũ ���ڿ� ��������
	redirect = new PurchaseLinkUrl();
	redirect = (PurchaseLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut			=  redirect.getViewPagecut();
	String view_total			= redirect.getViewTotal();
	String view_boardpage		= redirect.getViewBoardpage();
	String view_totalpage		= redirect.getViewTotalpage();

	String link_info_modify 	= redirect.getLinkInfoModify();
	String link_info_delete 	= redirect.getLinkInfoDelete();
	String link_item_add	 	= redirect.getLinkItemAdd();
	String link_item_modify 	= redirect.getLinkItemModify();
	String link_item_delete 	= redirect.getLinkItemDelete();
	String link_list 			= redirect.getLinkList();
	String link_approval		= redirect.getLinkApproval();
	String link_app_info		= redirect.getLinkAppInfo();
	String link_print 			= redirect.getLinkPrint();
	
	String caption = "";
	if(request_type.equals("RES")) caption = "�������� ���ſ�û";
	else if(request_type.equals("ROP")) caption = "ROP�� ���� ���ſ�û";
	else if(request_type.equals("MRP")) caption = "�������� ���ſ�û";
	else caption = "�ڻ���� ���ſ�û";
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../pu/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0" onLoad="display();">
<FORM name="request_form" method="post" action="PurchaseMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pu/images/blet.gif"> <%=caption %></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
<%=link_item_add%><%=link_info_modify%><%=link_info_delete%><%=link_app_info%><%=link_print%><%=link_list%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ�û��ȣ</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='15' name='request_no' value='<%=request_no%>' readOnly></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ�û����</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='10' name='request_date' value='<%=request_date%>' readOnly></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
		   <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ�û��</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='10' name='requester_info' value='<%=requester_info%>' readOnly></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ�û�μ�</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='20' name='requester_div_name' value='<%=requester_div_name%>' readOnly></TD>
           </TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">�����ڵ�</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='15' name='project_code' value='<%=project_code%>' readOnly <% if(request_type.equals("RES")) out.print("class=\"text_01\""); %>> <a href="javascript:sel_pjt_code();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">������</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='30' name='project_name' value='<%=project_name%>' readOnly></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
		   <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">���ſ���ݾ�</TD>
           <TD width="87%" height="25" class="bg_04" colspan="3">
				<INPUT type='text' name='request_total_mount' value='<%=request_total_mount%>' size='12' readOnly style="text-align:right;">��
		   </TD>   
		   </TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TR><TD height='5px;'></TD></TR>
	<TR><TD align=left><IMG src='../pu/images/request_item.gif' border='0' alt='���ſ�ûǰ��'></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<TR><!--����Ʈ-->
		<TD vAlign=top>
		    <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:scroll; overflow-y:auto;">
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=23>
						  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>ǰ���ڵ�</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=150 align=middle class='list_title'>ǰ���</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>�ܰ�</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=40 align=middle class='list_title'>����</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=40 align=middle class='list_title'>����</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>�ݾ�</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=200 align=middle class='list_title'>������־�ü��</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>����԰���</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>����ּ���</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>���԰����</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=60 align=middle class='list_title'>�������</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100% align=middle class='list_title'>÷��ȭ��</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
<%
			int no = 1;
			while(table_iter.hasNext()){
				table = (RequestInfoTable)table_iter.next();
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle height="24" class='list_bg'><%=no%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getItemCode()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg' style='padding-left:5px'><%=table.getItemName()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getSupplyCost(),"")%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getRequestQuantity()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getRequestUnit()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getRequestCost(),"")%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg' style='padding-left:5px'><%=table.getSupplyerName()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getDeliveryDate()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOrderQuantity()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getDeliveryQuantity()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getFileLink()%></TD>
					</TR>
					<TR><TD colSpan=25 background="../cm/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE><DIV></TD></TR></TABLE>

<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='requester_div_code' value='<%=requester_div_code%>'>
<input type='hidden' name='requester_id' value='<%=requester_id%>'>
<input type='hidden' name='request_type' value='<%=request_type%>'>
</form>
</body>
</html>


<script language=javascript>
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function modify_request() 
{ 
	var f = document.request_form;
	f.mode.value = "modify_request_info";

	f.submit();
}


// ǰ�� ���� ��������
function searchCMInfo(){
	var url = "../servlet/CodeMgrServlet?mode=list_item_p";	
	wopen(url,"return_noDesc",'800','500','scrollbars=yes,toolbar=no,status=no,resizable=no');
}


function delete_request() 
{ 
	var f = document.request_form;
	var request_no = f.request_no.value;

	var c = confirm("���ſ�ûǰ���� ��� �����˴ϴ�. ���ſ�û���� �����Ͻðڽ��ϱ�?");
	if(c) location.href = "PurchaseMgrServlet?mode=delete_request_info&request_no="+request_no;
}

function add_item() 
{ 
	var f = document.request_form;

	if((f.request_type.value == "MRP" || f.request_type.value == "RES") && f.project_code.value=="") {
		alert("�����ڵ带 ã�Ƽ� �����Ͻʽÿ�.");
		return;
	}
	
	f.submit();
}

// ���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// ��û�� ����(�̸�,id)
function searchUser()
{
wopen("../pu/searchUser.jsp?target=request_form.requester_id/request_form.requester_info","proxy","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");
}

/**********************
 * ���ڸ� �Էµǰ�
 **********************/
function currency(obj)
{
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false
	}
}

function com(obj)
{
	obj.value = unComma(obj.value);
	obj.value = Comma(obj.value);
}

/**********************
 * õ���� �޸� ����
 **********************/
function Comma(input) {

  var inputString = new String;
  var outputString = new String;
  var counter = 0;
  var decimalPoint = 0;
  var end = 0;
  var modval = 0;

  inputString = input.toString();
  outputString = '';
  decimalPoint = inputString.indexOf('.', 1);

  if(decimalPoint == -1) {
     end = inputString.length - (inputString.charAt(0)=='0' ? 1:0);
     for (counter=1;counter <=inputString.length; counter++)
     {
        var modval =counter - Math.floor(counter/3)*3;
        outputString = (modval==0 && counter <end ? ',' : '') + inputString.charAt(inputString.length - counter) + outputString;
     }
  }
  else {
     end = decimalPoint - ( inputString.charAt(0)=='-' ? 1 :0);
     for (counter=1; counter <= decimalPoint ; counter++)
     {
        outputString = (counter==0  && counter <end ? ',' : '') +  inputString.charAt(decimalPoint - counter) + outputString;
     }
     for (counter=decimalPoint; counter < decimalPoint+3; counter++)
     {
        outputString += inputString.charAt(counter);
     }
 }
    return (outputString);
}

/**********************
 * ���ڿ��� Comma ����
 **********************/
function unComma(input) {
   var inputString = new String;
   var outputString = new String;
   var outputNumber = new Number;
   var counter = 0;
   if (input == '')
   {
	return 0
   }
   inputString=input;
   outputString='';
   for (counter=0;counter <inputString.length; counter++)
   {
      outputString += (inputString.charAt(counter) != ',' ?inputString.charAt(counter) : '');
   }
   outputNumber = parseFloat(outputString);
   return (outputNumber);
}

// ����ã��
function sel_pjt_code() {
	wopen('../servlet/PsmProcessServlet?mode=search_project&target=request_form.project_code/request_form.project_name','search_pjt','400','230','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//������������
function viewAppInfo(){
		var request_no = document.request_form.request_no.value;
		var request_type = document.request_form.request_type.value;
		wopen('../servlet/PurchaseMgrServlet?mode=request_app_print&request_no='+request_no+'&request_type='+request_type,'','650','600','scrollbars=yes,toolbar=no,status=no,resizable=no');
	
}

//�μ���
function go_print(){
		var request_no = document.request_form.request_no.value;
		var request_type = document.request_form.request_type.value;
		wopen('../servlet/PurchaseMgrServlet?mode=request_info_print&request_no='+request_no+'&request_type='+request_type,'','650','600','scrollbars=yes,toolbar=no,status=no,resizable=no');
	
}

//���� ���
function request_app_view(){
	var f = document.request_form;
	var project_code = f.project_code.value;
	var request_no	 = f.request_no.value;
	var request_type = f.request_type.value;
	var total_mount  = unComma(f.request_total_mount.value);
	var no_=<%=no%> // ���ſ�û ǰ�� Check����
	
	// ���ſ�ûǰ��üũ. ������ ����� �Ѿ�� ����.
	if(no_<2) {
		alert("���ſ�ûǰ���� �����ϴ�. ǰ���� �Ѱ� �̻� �߰��Ͻ� �� ���ڰ��縦 �����Ͻʽÿ�.");	
	return;}

	//����üũ
	if((f.request_type.value == "MRP" || f.request_type.value == "RES") && f.project_code.value=="") {
		alert("�ʿ��� ���ſ�û������ ������� �ʾҽ��ϴ�. ���ſ�û������ ���� �����Ͻ� �� �����Ͻʽÿ�.");
		location.reload();
		return;
	}

	//������ ���� ������ ���� üũ�� ���� ���� ��, �������� �̷����.
	if(request_type == "MRP" || request_type == "RES") {
		location.href = "../servlet/PurchaseOtherMgrServlet?mode=chk_budget&request_no="+request_no+"&request_type="+request_type+"&project_code="+project_code+"&total_mount="+total_mount;
	
	//�Ϲ�����(request_type==GEN or ROP)�ϰ��� �ٷ� ������.
	}else{
		var para = "mode=request_app_view&request_no="+request_no+"&request_type="+request_type; 
		location.href="../gw/approval/module/pu_PurchaseApp.jsp?"+para;	
	}
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//	var div_h = h - 485;
	var div_h = c_h +68;
	item_list.style.height = div_h;

} 
</script>