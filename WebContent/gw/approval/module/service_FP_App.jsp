<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info		= "������ �����ۼ�"		
	contentType = "text/html; charset=euc-kr" 		
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.file.textFileReader"
	errorPage	= "../../../admin/errorpage.jsp"
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
	if(doc_flag == null) doc_flag= "SERVICE";

	//������ ������ȣ
	String doc_lid = request.getParameter("plid");				//�������� ������ȣ(�ߺ��� �ĸ��� ����)
	if(doc_lid == null) doc_lid = "";
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> �������̷�</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<form action="" name="eForm" method="post" encType="multipart/form-data" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<br>��<br>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"></TEXTAREA></TD>
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
    <!--����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text name='doc_sub' size='40' maxlength="40" class="text_01"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=3 colspan="4"></td></tr></tbody></table>  

    <!--�̷�����-->
	<table cellspacing=0 cellpadding=0 width="100%" height='100%' border=0>
	   <tbody>
		 <tr>
           <td width="100%" height="100%" valign="top">
				<%
						StringTokenizer strid = new StringTokenizer(doc_lid,",");
						if(doc_flag.equals("SERVICE")) {	
							int vcnt=1;
							while(strid.hasMoreTokens()) {
				%>
									<jsp:include page='service_viewHistory.jsp' flush='true'>
									<jsp:param name='ah_id' value='<%=strid.nextToken()%>' />
									<jsp:param name='vcnt' value='<%=vcnt%>' />
									</jsp:include>

				<%	
									vcnt++;
							} //while
						} //if
				%>
		   </td></tr></tbody></table>  

  </td></tr></table>

<input type='hidden' name='addFileDir' value='<%=uploadDir%>'>
<input type='hidden' name='login_id' value='<%=sl.id%>'>
<input type='hidden' name='login_name' value='<%=sl.name%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_id' value='<%=text.getID()%>'>
<input type='hidden' name='doc_flag' value='SERVICE'>
<input type='hidden' name='doc_lid' value='<%=doc_lid%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>

<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='CONTENT' value='����������'>
<input type='hidden' name='mode' value=''>
</form>

</body>
</html>


<script language=javascript>
<!--
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	wopen("../eleApproval_Share.jsp?target=eForm.doc_app_line","eleA_app_search_select","520","467");
}


//���� ��� (�ϰ�����)
function eleApprovalRequest()
{
	var f = document.eForm;

	if(f.doc_sub.value == ""){
		alert("������ �Է��Ͻʽÿ�.");
		return;
	}
	if (f.doc_app_line.value =="") {
		alert("���缱�� �Է��Ͻʽÿ�.");
		return;
	}
	 
	 //���缱 �˻�
	data = f.doc_app_line.value;		//���缱 ����
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

	//�ϰ����� ����������
	f.action='../../../servlet/ApprovalInputServlet';
	f.mode.value='ABAT';	
	f.submit();
	
}

//���� ��� (������ ����)
function eleApprovalRequestPara()
{
	var f = document.eForm;

	if(f.doc_sub.value == ""){
		alert("������ �Է��Ͻʽÿ�.");
		return;
	}
	if (f.doc_app_line.value =="") {
		alert("���缱�� �Է��Ͻʽÿ�.");
		return;
	}

	//���缱 �˻�
	data = f.doc_app_line.value;	//���缱 ����
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

	//����������
	f.action="../../../servlet/ApprovalInputServlet";
	f.mode.value='REQ';
	f.submit();
	
}

//���� �ӽú���
function eleApprovalTemp()
{

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
-->
</script>