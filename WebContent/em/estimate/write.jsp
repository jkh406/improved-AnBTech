<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.em.entity.*"
%>
<%!
	EstimateInfoTable estimate;
	EmLinkUrl link;
%>

<%
	estimate = (EstimateInfoTable)request.getAttribute("EstimateInfo");
	link = (EmLinkUrl)request.getAttribute("LinkUrl");
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../em/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../em/images/blet.gif"> ������ �ۼ�</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
<!--				<a href="javascript:checkForm();"><img src="../em/images/bt_save.gif" border="0" align="absmiddle"></a>-->
				<a href="javascript:checkForm();"><img src="../em/images/bt_input_item.gif" border="0" align="absmiddle"></a>
				<a href="javascript:location.href='EstimateMgrServlet?mode=mylist'"><img src="../em/images/bt_cancel.gif" border="0" align="absmiddle"></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--����-->
<form method=post name="writeForm" action='../servlet/EstimateMgrServlet' enctype='multipart/form-data' style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
<!-- ��������-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">������ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="estimate_no" size="15" value="<%=estimate.getEstimateNo()%>" readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">ȸ���</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="company_name" size="20" value="<%=estimate.getCompanyName()%>" class="text_01" maxlength="30"><input type='hidden' name='company_id'> <a href='javascript:searchCompany();'><img src='../em/images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04" colspan="3"><input type="text" name="estimate_subj" size="50" value="<%=estimate.getEstimateSubj()%>" maxlength="50" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr><td height="5" colspan="4"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��ǰ����</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="delivery_period" size="20" value="<%=estimate.getDeliveryPeriod()%>" class="text_01" maxlength="30"> <a href="javascript:sel_item('delivery_period','delivery_period');"><img src='../em/images/bt_search.gif' border='0' align='absmiddle'></a></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��ǰ����</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="delivery_day" size="10" value="<%=estimate.getDeliveryDay()%>" class="text_01" maxlength="10" readOnly> <a href="Javascript:OpenCalendar('delivery_day');"><img src="../em/images/bt_calendar.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�ε����</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="delivery_place" size="20" value="<%=estimate.getDeliveryPlace()%>" class="text_01" maxlength="30"> <a href="javascript:sel_item('delivery_place','delivery_place');"><img src='../em/images/bt_search.gif' border='0' align='absmiddle'></a></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="payment_terms" size="20" value="<%=estimate.getPaymentTerms()%>" class="text_01" maxlength="30"> <a href="javascript:sel_item('pay_terms','payment_terms');"><img src='../em/images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">��ȿ�Ⱓ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="valid_period" size="20" value="<%=estimate.getValidPeriod()%>" class="text_01" maxlength="30"> <a href="javascript:sel_item('valid_period','valid_period');"><img src='../em/images/bt_search.gif' border='0' align='absmiddle'></a></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�����Ⱓ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="guarantee_term" size="20" value="<%=estimate.getGuaranteeTerm()%>" class="text_01" maxlength="30"> <a href="javascript:sel_item('guarantee_term','guarantee_term');"><img src='../em/images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr><td height="5" colspan="4"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">���޹��̸�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="charge_rank" size="10" value="<%=estimate.getChargeRank()%>" class="text_01" maxlength="15"> <input type="text" name="charge_name" size="10" value="<%=estimate.getChargeName()%>" class="text_01" maxlength="15"> <a href="javascript:chooseCustomer();"><img src='../em/images/bt_search.gif' border='0' align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�μ���</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="charge_div" size="20" value="<%=estimate.getChargeDiv()%>" maxlength="15"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">ȸ����ȭ��ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="charge_tel" size="15" value="<%=estimate.getChargeTel()%>" maxlength="15" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�޴�����ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="charge_mobile" size="15" value="<%=estimate.getChargeMobile()%>" maxlength="15"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�ѽ���ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="charge_fax" size="15" value="<%=estimate.getChargeFax()%>" maxlength="15"></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">���ڿ���</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="charge_email" size="20" value="<%=estimate.getChargeEmail()%>" maxlength="30"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr></tbody></table>

<%=link.getInputHidden()%>
</form>
</td></tr></table>
</body>
</html>


<script language="javascript">

//�ʼ� �Է»��� üũ
function checkForm(){ 
	var f = document.writeForm;
	var mode = f.mode.value;
	var version = f.ver.value;

	if(mode == 'modify'){
		var saveconfirm = confirm("���� ������ �����Ͻðڽ��ϱ�?");
		if(!saveconfirm){
			location.href = "../servlet/EstimateMgrServlet?mode=write_item&estimate_no=<%=estimate.getEstimateNo()%>&ver="+version;
			return;
		}

	}

	if(f.company_name.value == ''){
		alert("������ ��û�� ȸ����� �Է� �Ǵ� ã�Ƽ� �����Ͻʽÿ�.");
		f.company_name.focus();
		return;
	}

	if(f.estimate_subj.value.length < 5){
		alert("���������� 5�� �̻� �Է��Ͻʽÿ�.");
		f.estimate_subj.focus();
		return;
	}

	if(f.delivery_period.value == ''){
		alert("��ǰ������ �Է� �Ǵ� ã�Ƽ� �����Ͻʽÿ�.");
		f.delivery_period.focus();
		return;
	}

	if(f.delivery_day.value == ''){
		alert("��ǰ���ڸ� ã�Ƽ� �����Ͻʽÿ�.");
		f.delivery_day.focus();
		return;
	}

	if(f.delivery_place.value == ''){
		alert("�ε���Ҹ� �Է� �Ǵ� ã�Ƽ� �����Ͻʽÿ�.");
		f.delivery_place.focus();
		return;
	}

	if(f.payment_terms.value == ''){
		alert("���������� �Է� �Ǵ� ã�Ƽ� �����Ͻʽÿ�.");
		f.payment_terms.focus();
		return;
	}

	if(f.guarantee_term.value == ''){
		alert("��ȿ�Ⱓ�� �Է� �Ǵ� ã�Ƽ� �����Ͻʽÿ�.");
		f.guarantee_term.focus();
		return;
	}

	if(f.valid_period.value == ''){
		alert("�����Ⱓ�� �Է� �Ǵ� ã�Ƽ� �����Ͻʽÿ�.");
		f.valid_period.focus();
		return;
	}

	if(f.charge_name.value == ''){
		alert("����� ����/�̸��� �Է� �Ǵ� ã�Ƽ� �����Ͻʽÿ�.");
		f.charge_name.focus();
		return;
	}

	if(f.charge_tel.value == ''){
		alert("����� ȸ����ȭ��ȣ�� �Է��Ͻʽÿ�.");
		f.charge_tel.focus();
		return;
	}

	f.submit();
}

function sel_item(item_class,obj_name){
	var sParam = "src=../../em/admin/input_item_mgr.jsp&c_class="+item_class+"&frmWidth=380&frmHeight=450&title=sel_item";

		var sRtnValue=showModalDialog("../em/estimate/item_selModal.jsp?"+sParam,"sel_item","dialogWidth:388px;dialogHeight:400px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

		if (typeof sRtnValue != "undefined" && sRtnValue != "")
		{
			eval ("document.writeForm." + obj_name + ".value= \"" + sRtnValue + "\"");
		}

}

function searchCompany(){
	//window.open("../em/estimate/searchCompany.jsp","chooseCustomer","width=630,height=400,scrollbar=auto,toolbar=no,status=no,resizable=no");
	wopen("../crm/company/searchCompany.jsp?sf=writeForm&sid=company_id&sname=company_name","search_company",'600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function chooseCustomer(){
	var sel = document.writeForm.company_id;
	var ap_id;

	if(sel.value==''){
		alert("������ ��û�� ȸ����� ���� ã�Ƽ� �����Ͻʽÿ�.\n�̵�� ��ü�� ��� ����� ������ ���� �Է��Ͻʽÿ�.");
		return;
	}else{
		ap_id = sel.value;
		wopen("../em/estimate/chooseCustomer.jsp?ap_id="+ap_id,"choose_customer",'500','325','scrollbars=no,toolbar=no,status=no,resizable=no');
	}
}

//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
</script>