<%@ include file= "../../../admin/configHead.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.crm.entity.*"
%>
<%!
	CompanyInfoTable company;
	CrmLinkUrl link;
%>

<%
	String mode = request.getParameter("mode");
	company = (CompanyInfoTable)request.getAttribute("CompanyInfo");
	link = (CrmLinkUrl)request.getAttribute("Redirect");
	String input_hidden = link.getInputHidden();
%>

<%  //÷������
	int ref_count		= 1;	// �ִ� �������� ���� ����
	int enableupload	= 1;	// ���ε� ���� ����
%>

<script language=JavaScript>
<!--
<%
	int i = 1;

	while(i < enableupload){
		if(i == enableupload-1){
%>

		function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%> size=50>"
		}
	<%
		 break;
		}
	%>
	function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=50><font id=id<%=i+1%>></font>"
	}
<%
	i++;
	}
%>
//-->
</script>

<%
	CompanyInfoTable file = new CompanyInfoTable();

	ArrayList file_list = new ArrayList();
	file_list = (ArrayList)request.getAttribute("FILE_LIST");
	Iterator file_iter = file_list.iterator();

	i = 1;

	String file_stat = "";
	if(file_iter.hasNext()){
		file = (CompanyInfoTable)file_iter.next();
		file_stat = file_stat + "<input type=file name='attachfile"+i+"' size=50>" + file.getFileName()+" ����! <input type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
		i++;
	}
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../crm/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form method=post name="writeForm" action='../servlet/CrmServlet' enctype='multipart/form-data' style="margin:0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../crm/images/blet.gif"> ����������� </TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href="javascript:checkForm();"><img src="../crm/images/bt_reg.gif" border="0"></a>
			  <a href="javascript:history.go(-1);"><img src="../crm/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/basic_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����ڹ�ȣ</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type="text" name="company_no" value="<%=company.getCompanyNo()%>" size="12" maxlength="10" onKeyPress="currency(this);" <% if(mode.equals("company_modify")) out.print("readOnly onClick=\"javascript:alert('����ڵ�Ϲ�ȣ�� ������ �� �����ϴ�. �߸� �Է��ϼ̴ٸ� ���� �� �ٽ� �Է��Ͻʽÿ�.');\""); %> class="text_01"> (-)�� ������ ����ڵ�Ϲ�ȣ 10�ڸ��� �Է��Ͻʽÿ�.</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�����1</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="name_kor" value="<%=company.getNameKor()%>" maxlength="80" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�����2</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="name_eng" value="<%=company.getNameEng()%>" maxlength="80"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ��ȭ��ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_tel_no" value="<%=company.getMainTelNo()%>" size="15" maxlength="30" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ�ѽ���ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_fax_no" value="<%=company.getMainFaxNo()%>" size="15" maxlength="30"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ�ڸ�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="chief_name" value="<%=company.getChiefName()%>" size="15" maxlength="30"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ���ֹι�ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="chief_personal_no" value="<%=company.getChiefPersonalNo()%>" maxlength="14"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04">
				<select name='business_type'>
					<option value='�����'>�����</option>
					<option value='���¾�ü'>���¾�ü</option>
					<option value='����'>����</option>
					<option value='��Ÿ'>��Ÿ</option></select>
				<%	if(!company.getBusinessType().equals("")){	%>
						<script language='javascript'>
							document.writeForm.business_type.value = '<%=company.getBusinessType()%>';
						</script>
				<%	}	%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04">
		<%
			//������ ����� ��� �迭 item_list[] ���ڸ� �����ϸ� ��.
			//�ٸ� �κ��� ������ �ʿ� ����.
			String[] item_list = {"�ý���","�ܸ���","DRYER","����","������","�ڻ�","��Ÿ"};
			int sel_cnt = item_list.length;

			//ȭ�����
			for(int m=0,k=1; m<sel_cnt; m++,k++) {
				if(company.getBusinessItem().indexOf(item_list[m]) > 0)
					 out.println("<input type='checkbox' checked name='item' value='"+item_list[m]+"'>"+item_list[m]);
				else out.println("<input type='checkbox' name='item' value='"+item_list[m]+"'>"+item_list[m]);

			}
		%>
				</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�����ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="company_post_no" value="<%=company.getCompanyPostNo()%>" size="7" maxlength="7"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">Ȩ�������ּ�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="homepage_url" value="<%=company.getHomepageUrl()%>" size="30" maxlength="50"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">ȸ���ּ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" name="company_address" value="<%=company.getCompanyAddress()%>" size="60"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>		  
		  <!-- ȭ�� ÷�� -->
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����ڵ����</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				
				<%
					if (enableupload > 0){
				%>
		            <%=file_stat%>
				<%
						if(i < enableupload){
				%>
				            <input type=file name=attachfile<%=i%> onClick='fileadd_action<%=i%>()' size="50">
				            <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <input type=file name=attachfile<%=i%> size="50">
				            <font id=id<%=i%>></font>
				<%
						}
					}
				%>		   
	     </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/detailed_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ�ڸ�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="chief_name" value="<%=company.getChiefName()%>" size="15" maxlength="30"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ���ֹι�ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="chief_personal_no" value="<%=company.getChiefPersonalNo()%>" maxlength="14"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<!--
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����(����)</td>
           <td width="37%" height="25" class="bg_04">
				<select name='business_type'>
					<option value=''>�����Ͻʽÿ�.</option>
					<option value='������'>������</option>
					<option value='���񽺾�'>���񽺾�</option>
					<option value='���Ҹž�'>���Ҹž�</option>
					<option value='��������'>��������</option>
					<option value='��Ÿ'>��Ÿ</option></select>
				<%	if(!company.getBusinessType().equals("")){	%>
						<script language='javascript'>
							document.writeForm.business_type.value = '<%=company.getBusinessType()%>';
						</script>
				<%	}	%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_04">
				<select name='business_item'>
					<option value=''>�����Ͻʽÿ�.</option>
					<option value='���α׷�'>���α׷�</option>
					<option value='�������'>�������</option>
					<option value='��Ÿ'>��Ÿ</option></select>
				<%	if(!company.getBusinessItem().equals("")){	%>
						<script language='javascript'>
							document.writeForm.business_item.value = '<%=company.getBusinessItem()%>';
						</script>
				<%	}	%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
-->
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ŷ���������</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="trade_start_time" value="<%=company.getTradeStartTime()%>" size="10" readOnly> <a href="Javascript:OpenCalendar('trade_start_time');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ŷ���������</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="trade_end_time" value="<%=company.getTradeEndTime()%>" size="10" readOnly> <a href="Javascript:OpenCalendar('trade_end_time');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ü����</td>
           <td width="37%" height="25" class="bg_04">
				<select name='company_type'>
					<option value=''>�����Ͻʽÿ�.</option>
					<option value='�������'>�������</option>
					<option value='��������'>��������</option>
					<option value='���λ����'>���λ����</option>
					<option value='��Ÿ'>��Ÿ</option></select>
				<%	if(!company.getCompanyType().equals("")){	%>
						<script language='javascript'>
							document.writeForm.company_type.value = '<%=company.getCompanyType()%>';
						</script>
				<%	}	%></td>		   
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ŷ�����</td>
           <td width="37%" height="25" class="bg_04">
			   <input type="radio" value="����" name="trade_type" <% if(company.getTradeType().equals("����")) out.print("checked");%>>���� <input type="radio" value="����" name="trade_type" <% if(company.getTradeType().equals("����")) out.print("checked");%>>���� <input type="radio" value="������" name="trade_type" <% if(company.getTradeType().equals("������")) out.print("checked");%>>����,����</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ſ뵵</td>
           <td width="37%" height="25" class="bg_04">
				<select name='credit_level'>
					<option value=''>�����Ͻʽÿ�.</option>
					<option value='��'>��</option>
					<option value='��'>��</option>
					<option value='��'>��</option>
				<%	if(!company.getCreditLevel().equals("")){	%>
						<script language='javascript'>
							document.writeForm.credit_level.value = '<%=company.getCreditLevel()%>';
						</script>
				<%	}	%></td>		   		   
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">������û��</td>
           <td width="37%" height="25" class="bg_04">
				<select name='estimate_req_level'>
					<option value=''>�����Ͻʽÿ�.</option>
					<option value='��'>��</option>
					<option value='��'>��</option>
					<option value='��'>��</option>
				<%	if(!company.getEstimateReqLevel().equals("")){	%>
						<script language='javascript'>
							document.writeForm.estimate_req_level.value = '<%=company.getEstimateReqLevel()%>';
						</script>
				<%	}	%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="worker_number" value="<%=company.getWorkerNumber()%>" size="5" maxlength="5" onKeyPress="currency(this);">��</td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ְŷ�����</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_bank_name" value="<%=company.getMainBankName()%>" maxlength="50"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ֿ����Ź�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_newspaper_name" value="<%=company.getMainNewspaperName()%>" maxlength="50"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ֿ����ǰ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_product_name" value="<%=company.getMainProductName()%>" maxlength="80"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">���ε�Ϲ�ȣ</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="corporation_no" value="<%=company.getCorporationNo()%>" maxlength="14"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="founding_day" value="<%=company.getFoundingDay()%>" size="10" readOnly> <a href="Javascript:OpenCalendar('founding_day');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">Ư�̻���</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="4" name="other_info" cols="80"><%=company.getOtherInfo()%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

<input type="hidden" name="no" value="<%=company.getMid()%>">
<input type="hidden" name="modify_history" value="">
<input type="hidden" name="business_item">
<%=input_hidden%>
</form>
</body>
</html>

<script language="javascript">

//�ʼ� �Է»��� üũ
function checkForm(){ 
	var f = document.writeForm;

	if(f.company_no.value.length != 10){
		alert("����ڵ�Ϲ�ȣ�� �ùٷ� �Է��Ͻʽÿ�.");
		f.company_no.focus();
		return;
	}

	if(!isValidOffNum(f.company_no.value)){
		alert("��ȿ�� ����ڵ�Ϲ�ȣ�� �ƴմϴ�.");
		f.company_no.focus();
		return;
	}

	if(f.name_kor.value == ''){
		alert("������� �Է��Ͻʽÿ�.");
		f.name_kor.focus();
		return;
	}

	if(f.main_tel_no.value == ''){
		alert("��ǥ��ȭ��ȣ�� �Է��Ͻʽÿ�.");
		f.main_tel_no.focus();
		return;
	}

	var items = "";
	var s_count = 0;
    for(i=0;i<f.item.length;i++){
		if(f.item[i].checked){
			items += "," + f.item[i].value;
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("������ �Ѱ� �̻� �����Ͻʽÿ�.");
	   return;
    }
	
	f.business_item.value = items;
	f.submit();

}
//����� ��ȣ üũ
function isValidOffNum(tmpStr){
	tmpSum = new Number(0);
	tmpMod = new Number(0);
	resValue = new Number(0);
	var intOffNo = new Array(0,0,0,0,0,0,0,0,0,0);
	var strChkNum = new Array(1,3,7,1,3,7,1,3,5);

	for(i = 0 ; i < 10 ; i ++){
		intOffNo[i] = new Number(tmpStr.substring(i, i+1));
	}

	for(i = 0 ; i < 9 ; i ++){
		tmpSum = tmpSum + (intOffNo[i]*strChkNum[i]);
	}

	tmpSum = tmpSum + ((intOffNo[8]*5)/10);
	tmpMod = parseInt(tmpSum%10, 10);

	if(tmpMod == 0){
		resValue = 0;
	}
	else{
		resValue = 10 - tmpMod;
	}

	if(resValue == intOffNo[9]){
		return true;
	}
	else{
		return false;
	}
} 

function sel_item(item_class,obj_name){
	var sParam = "src=../../em/admin/input_item_mgr.jsp&c_class="+item_class+"&frmWidth=400&frmHeight=500&title=sel_item";

		var sRtnValue=showModalDialog("../em/estimate/item_selModal.jsp?"+sParam,"sel_item","dialogWidth:400px;dialogHeight:500px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

		if (typeof sRtnValue != "undefined" && sRtnValue != "")
		{
			eval ("document.writeForm." + obj_name + ".value= \"" + sRtnValue + "\"");
		}

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
</script>