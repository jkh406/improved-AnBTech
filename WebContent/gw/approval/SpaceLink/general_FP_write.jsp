<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "���ڰ��� �����ۼ�"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.file.textFileReader"
%>
<%
	//------------------------------------------------------------------
	//	�ʱ�ȭ & ���� upload ���丮
	//------------------------------------------------------------------
	String uploadDir = upload_path + "/gw/approval/eleApproval/"+ login_id + "/addfile";	//multipart��
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	textFileReader text = new com.anbtech.file.textFileReader();			//ID�� ���ϱ�

	/*********************************************************************
	 	���ڰ��� �޴��� �ƴ� ��Ÿ���� ���ڰ����Ž� ó��
		ex) flag�� SERVICE:�����񽺰���, BOM:��ǰ�������� 
	*********************************************************************/
	//������ ���� (SERVICE:�����񽺰���, BOM:��ǰ��������)
	String doc_flag = request.getParameter("flag");				//flag����
	if(doc_flag == null) doc_flag= "GEN";

	//������ ������ȣ
	String doc_lid = request.getParameter("plid");				//�������� ������ȣ(�ߺ��� �ĸ��� ����)
	if(doc_lid == null) doc_lid = "";
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form action="hyu_ga_FP_write.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> �Ϲݹ�������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
<% if(doc_flag.equals("GEN")) {		//�Ϲݹ��� ���ڰ��� %>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequestPara();"><img src="../../images/bt_agree_batch.gif" border="0" align="absmiddle" alt='�ϰ�����'></a>
				<a href="Javascript:eleApprovalTemp();"><img src="../../images/bt_save_tmp.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a>
<% } else {		//��Ÿ���� ���ڰ��� ��Ž� %>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a>
<% } %>
			</TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<br>��<br>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0" class='text_01'></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=36% align=left><!-- ����ĭ-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">�����</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- ����ĭ �� -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text name='doc_sub' size='70' class='text_01'></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����Ⱓ</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="doc_per">
				<OPTION SELECTED VALUE="0">ó�������
				<OPTION VALUE="1">1��
				<OPTION VALUE="2">2��
				<OPTION VALUE="3">3��
				<OPTION VALUE="5">5��
				<OPTION VALUE="EVER">����</SELECT>		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">���ȵ��</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="doc_sec"> 
				<OPTION VALUE="1">1��
				<OPTION VALUE="2">2��
				<OPTION VALUE="3">3��
				<OPTION VALUE="INDOR">��ܺ�
				<OPTION SELECTED VALUE="GENER">�Ϲ�</SELECT>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="CONTENT" rows=25 cols=88 class='text_01'></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<input type="file" size=60 name="doc_ad1">
				<input type="file" size=60 name="doc_ad2">
				<input type="file" size=60 name="doc_ad3">
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='addFileDir' value='<%=uploadDir%>'>
<input type='hidden' name='login_id' value='<%=sl.id%>'>
<input type='hidden' name='login_name' value='<%=sl.name%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_id' value='<%=text.getID()%>'>
<input type='hidden' name='doc_flag' value='<%=doc_flag%>'>
<input type='hidden' name='doc_lid' value='<%=doc_lid%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
<input type='hidden' name='mode' value=''>
</form>
</body>
</html>

<script language=javascript>
<!--
//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//���� ��� (�ϰ�����)
function eleApprovalRequest()
{
	if(eForm.doc_sub.value == ""){
		alert("������ �Է��Ͻʽÿ�.");
		return;
	}
	else if (eForm.CONTENT.value == "") {
		alert("������ �Է��Ͻʽÿ�.");
		return;
	}
	else if (eForm.doc_app_line.value =="") {
		alert("���缱�� �Է��Ͻʽÿ�.");
		return;
	}
	 
	 //���缱 �˻�
	data = eForm.doc_app_line.value;		//���缱 ����
	s = 0;								//substring������
	e = data.length;					//���ڿ� ����
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("����") != -1) decision++;
		if(rstr.indexOf("����") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) {
		alert("�����ڰ� �������ϴ�");
		return;
	}
//	if(agree < 1) {
//		alert("�����ڰ� 2�� �̻��϶� �����մϴ�.");
//		return;
//	}

	document.onmousedown=dbclick;// ����Ŭ�� check

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/ApprovalInputServlet';
	document.eForm.mode.value='ABAT';	
	document.eForm.submit();
	
}

//���� ��� (������ ����)
function eleApprovalRequestPara()
{
		if(eForm.doc_sub.value == ""){
		alert("������ �Է��Ͻʽÿ�.");
		return;
	}
	else if (eForm.CONTENT.value == "") {
		alert("������ �Է��Ͻʽÿ�.");
		return;
	}
	else if (eForm.doc_app_line.value =="") {
		alert("���缱�� �Է��Ͻʽÿ�.");
		return;
	}

	//���缱 �˻�
	data = eForm.doc_app_line.value;		//���缱 ����
	s = 0;								//substring������
	e = data.length;					//���ڿ� ����
	decision = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("����") != -1) decision++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) {
		alert("�����ڰ� �������ϴ�");
		return;
	}

	document.onmousedown=dbclick;// ����Ŭ�� check

	//����������
	document.eForm.action="../../../servlet/ApprovalInputServlet";
	document.eForm.mode.value='REQ';
	document.eForm.submit();
	
}

//���� �ӽú���
function eleApprovalTemp()
{
	document.onmousedown=dbclick;// ����Ŭ�� check

	document.eForm.action="../../../servlet/ApprovalInputServlet";
	document.eForm.mode.value='TMP';
	document.eForm.submit();
	
}

//��Ÿ���� ���ڰ���� ÷�����Ϸ� ���ϰ� ����
function openWin(url)
{
	window.open(url,'open','width=500,height=400,scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//���缱 �ݱ�
function winClose()
{
	self.close();
}

// ����Ŭ�� ����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>