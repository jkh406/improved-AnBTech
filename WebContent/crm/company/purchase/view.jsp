<%@ include file= "../../../admin/configHead.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.crm.entity.*"
%>
<%!
	CompanyInfoTable company;
	CrmLinkUrl link;
%>

<%
	String mode = request.getParameter("mode");
	company = new CompanyInfoTable();
	company = (CompanyInfoTable)request.getAttribute("CompanyInfo");

	String company_no = company.getCompanyNo().substring(0,3) + "-" + company.getCompanyNo().substring(3,5) + "-" + company.getCompanyNo().substring(5,10);

	link = new CrmLinkUrl();
	link = (CrmLinkUrl)request.getAttribute("Redirect");
	String link_list = link.getLinkList();
	String link_modify = link.getLinkModify();
	String link_delete = link.getLinkDelete();
	String file_link   = link.getFileLink();
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../crm/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../crm/images/blet.gif"> ���޾�ü ������ </TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=300><a href="javascript:location.href='<%=link_list%>'"><img src="../crm/images/bt_list.gif" border="0"></a>
			  <a href="javascript:location.href='<%=link_modify%>'"><img src="../crm/images/bt_modify.gif" border="0"></a>
			 <a href="javascript:confirm_del();"><img src="../crm/images/bt_del.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
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
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����ڵ�Ϲ�ȣ</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=company_no%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">���ؾ�ü��1</td>
           <td width="37%" height="25" class="bg_04"><%=company.getNameKor()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">���޾�ü��2</td>
           <td width="37%" height="25" class="bg_04"><%=company.getNameEng()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ��ȭ��ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=company.getMainTelNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ�ѽ���ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=company.getMainFaxNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=company.getBusinessType()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=company.getBusinessItem().substring(1)%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�����ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=company.getCompanyPostNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">Ȩ�������ּ�</td>
           <td width="37%" height="25" class="bg_04"><%=company.getHomepageUrl()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">ȸ���ּ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=company.getCompanyAddress()%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		  <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����ڵ����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=file_link%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--������-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/detailed_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ�ڸ�</td>
           <td width="37%" height="25" class="bg_04"><%=company.getChiefName()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ǥ���ֹι�ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=company.getChiefPersonalNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ŷ�������</td>
           <td width="37%" height="25" class="bg_04"><%=company.getTradeStartTime()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ŷ�������</td>
           <td width="37%" height="25" class="bg_04"><%=company.getTradeEndTime()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ü����</td>
           <td width="37%" height="25" class="bg_04"><%=company.getCompanyType()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ŷ�����</td>
           <td width="37%" height="25" class="bg_04"><%=company.getTradeType()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ſ뵵</td>
           <td width="37%" height="25" class="bg_04"><%=company.getCreditLevel()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">������û��</td>
           <td width="37%" height="25" class="bg_04"><%=company.getEstimateReqLevel()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><%=company.getWorkerNumber()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ְŷ�����</td>
           <td width="37%" height="25" class="bg_04"><%=company.getMainBankName()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ֿ����Ź�</td>
           <td width="37%" height="25" class="bg_04"><%=company.getMainNewspaperName()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ֿ����ǰ</td>
           <td width="37%" height="25" class="bg_04"><%=company.getMainProductName()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">���ε�Ϲ�ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=company.getCorporationNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><%=company.getFoundingDay()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">Ư�̻���</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=company.getOtherInfo()%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--�������-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/reg_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=company.getWriterInfo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=company.getWrittenDay()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����������</td>
           <td width="37%" height="25" class="bg_04"><%=company.getModifierInfo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">������������</td>
           <td width="37%" height="25" class="bg_04"><%=company.getModifiedDay()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
</body>
</html>


<script language="javascript">
function confirm_del(){
	var f = confirm("�����ϰ��� �ϴ� ���� ������ �ٸ� ��⿡ �̹� ���Ǿ��� �� �ֽ��ϴ�.\n�� ��� �ٸ� ��⿡ ����ġ ���� ������ ������ �� ������ �����Ͻʽÿ�.\n������ �����Ͻðڽ��ϱ�?");
	if(f) location.href = "<%=link_delete%>";
}
</script>
