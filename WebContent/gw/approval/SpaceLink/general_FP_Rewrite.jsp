<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "���ڰ��� ���� ���ۼ�"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="java.util.StringTokenizer"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.file.textFileReader"
%>

<%
	//------------------------------------------------------------------
	//	�ʱ�ȭ & ���� upload ���丮
	//------------------------------------------------------------------
	String uploadDir = upload_path + "/gw/approval/eleApproval/"+ login_id + "/addfile";	//multipart��
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������

	//-----------------------------------
	//	���� ����
	//-----------------------------------
	// �������� ������ �ޱ�
	String doc_pid="";			//������ȣ
	String doc_lin="";			//�������� ���缱
	String doc_sub="";			//�������� ����
	String doc_ste="";			//�������� �� ����ܰ�
	String doc_per="";			//�������� �����Ⱓ
	String doc_sec="";			//�������� �������
	String doc_bon="";			//�������� ��������
	String doc_or1="";			//�������� ÷�� �����̸�1
	String doc_ad1="";			//�������� ÷�� �����̸�1
	String doc_or2="";			//�������� ÷�� �����̸�2
	String doc_ad2="";			//�������� ÷�� �����̸�2
	String doc_or3="";			//�������� ÷�� �����̸�3
	String doc_ad3="";			//�������� ÷�� �����̸�3
	String doc_path="";			//�������� ���� path

	String lid="";				//��Ÿ���� ���ڰ����Ƿ� ������ȣ
	String doc_flag="";			//�������� ����(SERVICE:������, BOM:PartList ��)
	String bon_path = "";		//�������� path
	String file1_path = "";		//÷������1 path
	String file2_path = "";		//÷������2 path
	String file3_path = "";		//÷������3 path
	String file1_size = "";		//÷������1 ũ��
	String file2_size = "";		//÷������2 ũ��
	String file3_size = "";		//÷������3 ũ��

	//�޽��� ���޺���
	String Message="";		//�޽��� ���� ����  

	//���/����/������ �̸�ã��
	String wid = "";			//����ڻ��
	String vid = "";			//�����ڻ��
	String did = "";			//�����ڻ��
	String wname = "";			//�����
	String vname = "";			//������
	String dname = "";			//������
	String vcomm = "";			//������ �ڸ�Ʈ (������ �����ϰ� ������ ����ʵ�)
	String dcomm = "";			//������ �ڸ�Ʈ (������ �����ϰ� ������ ����ʵ�)
	
	com.anbtech.gw.entity.TableAppMaster table = new TableAppMaster();	
	ArrayList table_list = new ArrayList();

	table_list = (ArrayList)request.getAttribute("Table_List");
	Iterator table_iter = table_list.iterator();
	while(table_iter.hasNext()) {
			 table = (TableAppMaster)table_iter.next();	
			
			 doc_pid=table.getAmPid();					//������ȣ
			 doc_sub=table.getAmAppSubj();				//����
			 doc_ste=table.getAmAppStatus();			//�� ����ܰ�
			 doc_per=table.getAmSavePeriod();			//�����Ⱓ
			 doc_sec=table.getAmSecurityLevel();		//�������
			 doc_path=table.getAmBonPath();				//���� Path

			 doc_or1=table.getAmAdd1Original();			//÷�� �����̸�1
			 doc_ad1=table.getAmAdd1File();				//÷�� �����̸�1
			 file1_path = upload_path + doc_path + "/addfile/" + doc_ad1;

			 File fn1 = new File(file1_path);
			 file1_size = Long.toString(fn1.length());

			 doc_or2=table.getAmAdd2Original();			//÷�� �����̸�2
			 doc_ad2=table.getAmAdd2File();				//÷�� �����̸�2
			 file2_path = upload_path + doc_path + "/addfile/" + doc_ad2;
			 File fn2 = new File(file2_path);
			 file2_size = Long.toString(fn2.length());

			 doc_or3=table.getAmAdd3Original();			//÷�� �����̸�3
			 doc_ad3=table.getAmAdd3File();				//÷�� �����̸�3
			 file3_path = upload_path + doc_path + "/addfile/" + doc_ad3;
			 File fn3 = new File(file3_path);
			 file3_size = Long.toString(fn3.length());

			 lid=table.getAmPlid();						//��Ÿ���� ������ȣ
			 doc_flag=table.getAmFlag();				//������������
			 wid = table.getAmWriter();					//����ڻ��
			 vid = table.getAmReviewer();				//�����ڻ��
			 did = table.getAmDecision();				//�����ڻ��

			 //�������� �б�
			 com.anbtech.file.textFileReader text = new textFileReader();
			 bon_path = upload_path + doc_path + "/bonmun/" + table.getAmBonFile();
			 doc_bon = text.getFileString(bon_path);	//�������� �������� 
	} //while
	

	//������ ���� �б�
	com.anbtech.gw.entity.TableAppLine line = new TableAppLine();			
	ArrayList table_line = new ArrayList();

	table_line = (ArrayList)request.getAttribute("Table_Line");
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		line = (TableAppLine)line_iter.next();
									
		if(line.getApStatus().equals("���"))  wname = line.getApName();	//�����
		if(line.getApStatus().equals("����"))  vname = line.getApName();	//������
		if(line.getApStatus().equals("����"))  dname = line.getApName();	//������
		if(line.getApStatus().equals("����")) 
			 vcomm = line.getApComment();//�������ڸ�Ʈ (������ �����ϰ� ������ ����ʵ�)
		if(line.getApStatus().equals("����")) 
			 dcomm = line.getApComment();//�������ڸ�Ʈ (������ �����ϰ� ������ ����ʵ�)
		doc_lin += line.getApStatus()+" "+line.getApSabun()+" "+line.getApName()+" "+line.getApRank()+" "+line.getApDivision()+"\r";	
	}

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../gw/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form action="hyu_ga_FP_Rewrite.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../gw/images/blet.gif"> �Ϲݹ�������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
<% if(doc_flag.equals("GEN")) {		//�Ϲݹ��� ���ڰ��� %>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../gw/images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../gw/images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequestPara();"><img src="../gw/images/bt_agree_batch.gif" border="0" align="absmiddle" alt='�ϰ�����'></a>
				<a href="Javascript:eleApprovalTemp();"><img src="../gw/images/bt_save_tmp.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../gw/images/bt_cancel.gif" border="0" align="absmiddle"></a>
<% } else {		//��Ÿ���� ���ڰ��� ��Ž� %>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../gw/images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../gw/images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../gw/images/bt_cancel.gif" border="0" align="absmiddle"></a>
<% } %>
			</TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../gw/images/bg-01.gif">��<br>��<br>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0" class='text_01'><%=doc_lin%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../gw/images/bg-01.gif">��<p>��</TD>
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
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">��������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text name='doc_sub' size='70' value='<%=doc_sub%>' class='text_01'></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">�����Ⱓ</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="doc_per">
				<OPTION <%if(doc_per.equals("0")) out.println("SELECTED");%> VALUE="0">ó�������
				<OPTION <%if(doc_per.equals("1")) out.println("SELECTED");%> VALUE="1">1��
				<OPTION <%if(doc_per.equals("2")) out.println("SELECTED");%> VALUE="2">2��
				<OPTION <%if(doc_per.equals("3")) out.println("SELECTED");%> VALUE="3">3��
				<OPTION <%if(doc_per.equals("5")) out.println("SELECTED");%> VALUE="5">5��
				<OPTION <%if(doc_per.equals("EVER")) out.println("SELECTED");%> VALUE="EVER">����</SELECT>
		   </td>
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">���ȵ��</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="doc_sec">
				<OPTION <%if(doc_sec.equals("1")) out.println("SELECTED");%> VALUE="1">1��
				<OPTION <%if(doc_sec.equals("2")) out.println("SELECTED");%> VALUE="2">2��
				<OPTION <%if(doc_sec.equals("3")) out.println("SELECTED");%> VALUE="3">3��
				<OPTION <%if(doc_sec.equals("INDOR")) out.println("SELECTED");%> VALUE="INDOR">��ܺ�
				<OPTION <%if(doc_sec.equals("GENER")) out.println("SELECTED");%> VALUE="GENER">�Ϲ�</SELECT>
			</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="CONTENT" rows=25 cols=88 class='text_01'><%=doc_bon%></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../gw/images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<% if(doc_or1.length() == 0) { %>
					<input type="file" size=60 name="doc_ad1"><br>
				<% } else { %>
					&nbsp; <a href='../gw/approval/eleApproval_downloadp.jsp?fname=<%=doc_or1%>&fsize=<%=file1_size%>&umask=<%=doc_ad1%>&extend=<%=doc_path%>'><%=doc_or1%></a>&nbsp;&nbsp;
					<a href=javascript:addFile1_Delete('<%=upload_path%><%=doc_path%>/addfile/<%=doc_ad1%>')>����</a><br>
				<% } %>
				
				<% if(doc_or2.length() == 0) { %>
					<input type="file" size=60 name="doc_ad2"><br>
				<% } else { %>
					&nbsp; <a href='../gw/approval/eleApproval_downloadp.jsp?fname=<%=doc_or2%>&fsize=<%=file2_size%>&umask=<%=doc_ad2%>&extend=<%=doc_path%>'><%=doc_or2%></a>&nbsp;&nbsp;
					<a href=javascript:addFile2_Delete('<%=upload_path%><%=doc_path%>/addfile/<%=doc_ad2%>')>����</a><br>
				<% } %>

				<% if(doc_or3.length() == 0) { %>
					<input type="file" size=60 name="doc_ad3"><br>
				<% } else { %>
					&nbsp; <a href='../gw/approval/eleApproval_downloadp.jsp?fname=<%=doc_or3%>&fsize=<%=file3_size%>&umask=<%=doc_ad3%>&extend=<%=doc_path%>'><%=doc_or3%></a>&nbsp;&nbsp;
					<a href=javascript:addFile3_Delete('<%=upload_path%><%=doc_path%>/addfile/<%=doc_ad3%>')>����</a><br>
				<% } %>	
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='addFileDir' value='<%=uploadDir%>'>
<input type='hidden' name='login_id' value='<%=sl.id%>'>
<input type='hidden' name='login_name' value='<%=sl.name%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_id' value='<%=doc_pid%>'>
<input type='hidden' name='doc_flag' value='<%=doc_flag%>'>
<input type='hidden' name='doc_lid' value='<%=lid%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
<input type='hidden' name='mode' value=''>
</form>

<% //���ϻ����� ��ũ��Ʈ�� ó���ϱ� ���� �ۼ� %>
<form name=d1Form method="post">
<input type="hidden" name="file1" value='<%=file1_path%>'>
<input type="hidden" name="PID" value='<%=doc_pid%>'>
<input type="hidden" name="mode">
</form>

<form name=d2Form method="post">
<input type="hidden" name="file2" value='<%=file2_path%>'>
<input type="hidden" name="PID" value='<%=doc_pid%>'>
<input type="hidden" name="mode">
</form>

<form name=d3Form method="post">
<input type="hidden" name="file3" value='<%=file3_path%>'>
<input type="hidden" name="PID" value='<%=doc_pid%>'>
<input type="hidden" name="mode">
</form>
</body>
</html>

<script language=javascript>
<!--

//÷������1 ����
function addFile1_Delete(file_name)
{
	document.d1Form.action="ApprovalDetailServlet";
	document.d1Form.PID.value='<%=doc_pid%>';
	document.d1Form.mode.value='DELFILE';
	document.d1Form.submit();
}

//÷������2 ����
function addFile2_Delete(file_name)
{
	document.d2Form.action="ApprovalDetailServlet";
	document.d2Form.PID.value='<%=doc_pid%>';
	document.d2Form.mode.value='DELFILE';
	document.d2Form.submit();
}

//÷������3 ����
function addFile3_Delete(file_name)
{
	document.d3Form.action="ApprovalDetailServlet";
	document.d3Form.PID.value='<%=doc_pid%>';
	document.d3Form.mode.value='DELFILE';
	document.d3Form.submit();
}


//���缱 �ݱ�
function winClose()
{
	self.close();
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
	document.eForm.action='ApprovalInputServlet';
	document.eForm.mode.value='ABAT_UP';	
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
	document.eForm.action="ApprovalInputServlet";
	document.eForm.mode.value='REQ_UP';
	document.eForm.submit();
	
}

//���� �ӽú��� �ٽ� �ӽú����� update�ϱ�
function eleApprovalTemp()
{
	document.onmousedown=dbclick;// ����Ŭ�� check

	document.eForm.action="ApprovalInputServlet";
	document.eForm.mode.value='TMP_UP';
	document.eForm.submit();


   // document.lding.visibility="visible";
	
}

// ����Ŭ�� ����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}

-->
</script>
