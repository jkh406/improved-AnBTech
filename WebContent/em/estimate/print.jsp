<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.em.entity.*"
%>
<%!
	ItemInfoTable item;
	EstimateInfoTable estimate;
	EmLinkUrl link;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>

<%
	String mode = request.getParameter("mode");
	estimate = new EstimateInfoTable();
	estimate = (EstimateInfoTable)request.getAttribute("Estimate_Info");

	link = new EmLinkUrl();
	link = (EmLinkUrl)request.getAttribute("Redirect");
	String link_list = link.getLinkList();
	String input_hidden = link.getInputHidden();
	
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../em/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<div id="page_content" style="position:absolute;left:0;top:0;width:100%">
<!--����-->
<form method=get name="viewForm" action='../servlet/EstimateMgrServlet' style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
<!-- ��������-->
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
         <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">������ȣ</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getEstimateNo()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">ȸ���</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getCompanyName()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��������</TD>
           <TD width="37%" height="25" class="bg_04" colspan="3"><%=estimate.getEstimateSubj()%>
			<%
				if(mode.equals("view")) out.print(estimate.getVersionList());
				else out.print(estimate.getVersion());
			%>		   
		   </TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TBODY></TABLE>

	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
		 <TR><TD height="5" colspan="4"></TD></TR>
		 <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">����ڸ�</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getChargeRank()%> <%=estimate.getChargeName()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">����ںμ���</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getChargeDiv()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">ȸ����ȭ��ȣ</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getChargeTel()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�޴�����ȣ</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getChargeMobile()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�ѽ���ȣ</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getChargeFax()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">���ڿ���</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getChargeEmail()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��ǰ����</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getDeliveryPeriod()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��ǰ����</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getDeliveryDay()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�ε����</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getDeliveryPlace()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��������</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getPaymentTerms()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��ȿ�Ⱓ</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getValidPeriod()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�����Ⱓ</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getGuaranteeTerm()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�ۼ�����</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getWrittenDay()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�ۼ���</TD>
           <TD width="37%" height="25" class="bg_04"><%=estimate.getWriter()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
         <TR><TD height=10 colspan="4"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr><td height=10></td></tr>
		<tr><td height=10 class='list_bg'><b>�Ѱ����ݾ�: <font color='red'><%=sp.getMoneyFormat(estimate.getTotalAmount(),"")%></font> ��</b></td></tr>
		</tbody></table></TD></TR>

  <TR><TD width="100%" align="left">
<!--����ǰ�� -->
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>ǰ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�𵨸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>���߿���</TD>
<!--			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>������(%)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>���޴ܰ�</TD>-->
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>������(%)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>����(%)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�����ܰ�</TD>
<!--		  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>���޾�ü��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>�԰�</TD></TR>-->
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
	<%
		ArrayList item_list = new ArrayList();
		item_list = (ArrayList)request.getAttribute("Item_List");
		Iterator item_iter = item_list.iterator();
		int count = 0;
		while(item_iter.hasNext()){
			item = (ItemInfoTable)item_iter.next();

	%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=count+1%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getItemName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getModelName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getQuantity()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getUnit()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getBuyingCost(),"")%></TD>
<!--			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getGainsPercent()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getSupplyCost(),"")%></TD>-->
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getDiscountPercent(),"")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getTaxPercent()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getEstimateValue(),"")%></TD>
<!--		  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getSupplyerName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getStandards()%></TD></TR>-->
			<TR><TD colSpan=17 background="../em/images/dot_line.gif"></TD></TR>
	<%		count++;
		}
	%>
			<TR><TD colSpan=17 class='list_bg'><%=estimate.getSpecialInfo()%></TD></TD></TR>
		</TBODY></TABLE></TD></TR></TABLE>

<input type='hidden' name='estimate_no' value='<%=estimate.getEstimateNo()%>'>
<%=input_hidden%>
</form>

</div> 
<script language="JavaScript1.2"> 
function iframe_reset(){ 
        dataobj=document.all? document.all.page_content : document.getElementById("page_content") 
         
        dataobj.style.top=0 
        dataobj.style.left=0 

        pagelength=dataobj.offsetHeight 
        pagewidth=dataobj.offsetWidth 

        parent.document.all.iframe_main.height=pagelength 
        parent.document.all.iframe_main.width=pagewidth 
} 
window.onload=iframe_reset 
</script>
</body>
</html>

<script language="javascript">

function modify(estimate_no,version) {
	var modify_confirm = confirm("���������� �����Ͻðڽ��ϱ�?");
	if(modify_confirm){
		location.href = "../servlet/EstimateMgrServlet?mode=modify&estimate_no="+estimate_no+"&ver="+version;
		return;
	}
	else return;
}

function copy_this(estimate_no,version) {
	var copy_confirm = confirm("���� ���������� �����Ͽ� ���ο� �������� �ۼ��Ͻðڽ��ϱ�?");
	if(copy_confirm){
		location.href = "../servlet/EstimateMgrServlet?mode=copy&estimate_no="+estimate_no+"&ver="+version;
		return;
	}
	else return;
}

function revision_this(estimate_no,version) {
	var rev_confirm = confirm("���� ���������� �������Ͻðڽ��ϱ�?");
	if(rev_confirm){
		location.href = "../servlet/EstimateMgrServlet?mode=revision&estimate_no="+estimate_no+"&ver="+version;
		return;
	}
	else return;
}

function go_approval(estimate_no,version) {
	var req_confirm = confirm("���� �ۼ��Ͻ� ������ ���ڰ��縦 �����Ͻðڽ��ϱ�?");

	if(req_confirm == true){
		location.href = "../gw/approval/module/estimate_FP_App.jsp?estimate_no="+estimate_no+"&ver="+version;
	}else{
		location.href = "../servlet/EstimateMgrServlet?mode=mylist";				
	}
}


function view_value_info(item_no,supplyer) {

	var url = "../servlet/EstimateMgrServlet?mode=view_supply_info&item_no="+item_no+"&supplyer="+supplyer;

	wopen(url,'view_history','600','285','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 600;
	item_list.style.height = div_h;

} 
</script>