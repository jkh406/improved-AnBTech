<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.crm.entity.*"
%>
<%!
	CustomerInfoTable customer;
	CrmLinkUrl link;
%>

<%
	String mode = request.getParameter("mode");
	customer = new CustomerInfoTable();
	customer = (CustomerInfoTable)request.getAttribute("CustomerInfo");

	link = new CrmLinkUrl();
	link = (CrmLinkUrl)request.getAttribute("Redirect");
	String link_list = link.getLinkList();
	String link_modify = link.getLinkModify();
	String link_delete = link.getLinkDelete();

	// ������ ���� üũ
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("CR01");
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
			  <TD valign='middle' class="title"><img src="../crm/images/blet.gif"> �������� </TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="javascript:location.href='<%=link_list%>'"><img src="../crm/images/bt_list.gif" border="0"></a></TD>
			  <TD align=left width=30><a href="javascript:location.href='<%=link_modify%>'"><img src="../crm/images/bt_modify.gif" border="0"></a></TD>
			  <% if(login_id.equals(customer.getWriter()) || idx >= 0){ //����� �Ǵ� �������ڸ� ���� ���� %>
			  <TD align=left width=30><a href="javascript:confirm_del();"><img src="../crm/images/bt_del.gif" border="0"></a></TD>
			  <% } %>
			  </TR></TBODY></TABLE></TD></TR>
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
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����(�ѱ�)</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getNameKor()%>(<%=customer.getSex()%>)</td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����(����)</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getNameEng()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">ȸ���</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCompanyName()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�μ���</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getDivisionName()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��å(����)</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getPositionRank()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ֿ����</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getMainJob()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">ȸ����ȭ��ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCompanyTelNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">ȸ���ѽ���ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCompanyFaxNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�̵���ȭ��ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getMobileNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">ȸ������ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCompanyPostNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">ȸ���ּ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=customer.getCompanyAddress()%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">���ڿ���</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getEmailAddress()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">Ȩ������</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHomepageUrl()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCustomerType()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">���з�</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCustomerClass().substring(1)%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--������-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/detailed_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">������ȭ��ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHomeTelNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�����ѽ���ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHomeFaxNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">DM�߼�ó</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getWhereToDm().equals("1")?"ȸ��":"����"%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">���ÿ����ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHomePostNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�����ּ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=customer.getHomeAddress()%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�ֹε�Ϲ�ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getPersonalNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getBirthday()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ȥ����</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getWhetherWedding()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">��ȥ�����</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getWeddingDay()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">������̸�</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getPartnerName()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">����ڻ������</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getPartnerBirthday()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">���</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHobby()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�����о�</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getMajorField()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">Ư�̻���</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=customer.getOtherInfo()%></td></tr>
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
           <td width="37%" height="25" class="bg_04"><%=customer.getWriterInfo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getWrittenDay()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">���������̷�</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><div style="position:relative; visibility:visible; width:100%; height:50; overflow:auto;"><table><tr><td><%=customer.getModifyHistory()%></td></tr></table></div></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
</body>
</html>

<script language="javascript">
function confirm_del(){
	var f = confirm("�����ϰ��� �ϴ� �������� �ٸ� ��⿡ �̹� ���Ǿ��� �� �ֽ��ϴ�.\n�� ��� �ٸ� ��⿡ ����ġ ���� ������ ������ �� ������ �����Ͻʽÿ�.\n������ �����Ͻðڽ��ϱ�?");
	if(f) location.href = "<%=link_delete%>";
}
</script>