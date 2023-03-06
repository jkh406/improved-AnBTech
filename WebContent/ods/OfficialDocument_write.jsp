<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "�������� �ۼ�"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage = "../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dms.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.file.*"
	import="com.oreilly.servlet.MultipartRequest"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	/*********************************************************************
	 	�ʱ�ȭ ����
	*********************************************************************/
	com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
	com.anbtech.date.anbDate anbdt = new anbDate();							//����
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�
	FileWriteString file = new com.anbtech.file.FileWriteString();			//���丮 �����ϱ�
	
	//-----------------------------------
	// �ۼ��� ���� ��������
	//-----------------------------------
	String user_id = "";			//�ۼ��� ���
	String user_name = "";			//�ۼ��� �̸�
	String user_rank = "";			//�ۼ��� ����
	String div_id = "";				//�ۼ��� �μ��� �����ڵ�
	String div_name = "";			//�ۼ��� �μ���
	String div_code = "";			//�ۼ��� �μ��ڵ�
	String code = "";				//�ۼ��� �μ�Tree �����ڵ�
	int attache_cnt = 4;			//÷������ �ִ밹�� (�̸�)

	/*********************************************************************
	 	�ش��� ���� �˾ƺ��� (�����) : ����� ���� [����]
	*********************************************************************/
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","b.code"};
	bean.setTable("user_table a,class_table b,rank_table c");		
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	String query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_id = login_id;								//����� ���
		user_name = bean.getData("name");				//����� ��
		user_rank = bean.getData("ar_name");			//����� ����
		div_id = bean.getData("ac_id");					//����� �μ��� �����ڵ�
		div_name = bean.getData("ac_name");				//����� �μ��� 
		div_code = bean.getData("ac_code");				//����� �μ��ڵ�
		code = bean.getData("code");					//�ۼ��� �μ�Tree �����ڵ�
	} //while

%>
<script language=javascript>
<!--
//���ڿ�������� ã��
function searchProxy()
{
	wopen("searchReceiver.jsp?target=eForm.mail_add","receiver",'510','467','scrollbars=no,toolbar=no,status=no,resizable=no');

}
//����ϱ�
function sendSave()
{
	//�Է»��� �˻��ϱ�
	var receive = document.eForm.receive.value; 
	if(receive.length == 0) { alert("������ �����Ͻʽÿ�."); return; }
	var subject = document.eForm.subject.value; 
	if(subject.length == 0) { alert("������ �Է��Ͻʽÿ�."); return; }
	var content = document.eForm.content.value; 
	if(content.length == 0) { alert("������ �Է��Ͻʽÿ�."); return; }
	var title_name = document.eForm.title_name.value; 
	if(title_name.length == 0) { alert("�Ӹ����� �Է��Ͻʽÿ�."); return; }
	var firm_name = document.eForm.firm_name.value; 
	if(firm_name.length == 0) { alert("�������� �Է��Ͻʽÿ�."); return; }
	var representative = document.eForm.representative.value; 
	if(representative.length == 0) { alert("�μ������ �Է��Ͻʽÿ�."); return; }
	var om = document.eForm.mail.checked; 
	var mm = document.eForm.module_name.checked; 
	var mail_add  = document.eForm.mail_add.value;
	if((om == false) && (mm == false)) { alert("��������� �Է��Ͻʽÿ�."); return; }
	if((om == true) && (mail_add == 0)) { alert("���ڿ��� �����ڸ� �Է��� �ֽʽÿ�."); return;}
	
	document.eForm.action='../servlet/OfficialDocumentMultiServlet';
	document.eForm.mode.value='OFD_write';	
	document.eForm.submit();
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//���� ����ϱ�
function app()
{
	//�Է»��� �˻��ϱ�
	var receive = document.eForm.receive.value; 
	if(receive.length == 0) { alert("������ �����Ͻʽÿ�."); return; }
	var subject = document.eForm.subject.value; 
	if(subject.length == 0) { alert("������ �Է��Ͻʽÿ�."); return; }
	var content = document.eForm.content.value; 
	if(content.length == 0) { alert("������ �Է��Ͻʽÿ�."); return; }
	var title_name = document.eForm.title_name.value; 
	if(title_name.length == 0) { alert("�Ӹ����� �Է��Ͻʽÿ�."); return; }
	var firm_name = document.eForm.firm_name.value; 
	if(firm_name.length == 0) { alert("�������� �Է��Ͻʽÿ�."); return; }
	var representative = document.eForm.representative.value; 
	if(representative.length == 0) { alert("�μ������ �Է��Ͻʽÿ�."); return; }
	var om = document.eForm.mail.checked; 
	var mm = document.eForm.module_name.checked; 
	var mail_add  = document.eForm.mail_add.value;
	if((om == false) && (mm == false)) { alert("��������� �Է��Ͻʽÿ�."); return; }
	if((om == true) && (mail_add == 0)) { alert("���ڿ��� �����ڸ� �Է��� �ֽʽÿ�."); return;}
	
	
	//������ �����ϱ� �� �������ϱ�
//	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���

	document.eForm.action='../servlet/OfficialDocumentMultiServlet';
	document.eForm.mode.value='OFD_app';	
	document.eForm.submit();

}
-->
</script>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ods/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ods/images/blet.gif"> ���������ۼ�</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href='javascript:app()'><img src="../ods/images/bt_sangsin.gif" border='0'></a> <a href="javascript:sendSave();"><img src="../ods/images/bt_save.gif" border="0"></a> <a href="javascript:history.go(-1)"><img src="../ods/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
<!--	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>--></TABLE>

<!--����-->
<form name="eForm" method="post" encType="multipart/form-data" style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--����1-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
<!--        <tr>
		   <td height="10" colspan="4"></td></tr> -->
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������ȣ</td>
           <td width="37%" height="25" class="bg_04">��������� �ڵ�ä��</td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='receive' value='' class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=user_name%>
					<input type='hidden' name='user_id' value='<%=user_id%>'>
					<input type='hidden' name='user_name' value='<%=user_name%>'>
					<input type='hidden' name='user_rank' value='<%=user_rank%>'>
					<input type='hidden' name='div_id' value='<%=div_id%>'>
					<input type='hidden' name='div_code' value='<%=div_code%>'>
					<input type='hidden' name='code' value='<%=code%>'>
					<input type='hidden' name='div_name' value='<%=div_name%>'>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input  size='25' type='text' name='reference' value=''></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input  size='60' type='text' name='subject' value='' class='text_01'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><textarea rows="8" name="content" cols="93"  class='text_01'></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">÷������</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
				<%
					for(int i=1; i<attache_cnt; i++) {
						out.println("&nbsp;<input type='file' name='attachfile"+i+"' size=60><br>");
					}
				%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<!--����2-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="10" colspan="4"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�Ӹ���</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='title_name' value='' class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">ǥ��</td>
           <td width="37%" height="25" class="bg_04"><input  size='35' type='text' name='slogan' value=''></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='firm_name' value='' class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�μ����</td>
           <td width="37%" height="25" class="bg_04"><input  size='10' type='text' name='representative' value='' class='text_01'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><input type="checkbox" name="module_name" value='�Խ���'>�Խ���	
					<input type="checkbox" name="mail" value='���ڿ���'>���ڿ���</td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><textarea rows="1" name="mail_add" cols='30' readOnly ></textarea>&nbsp;<a href="Javascript:searchProxy();"><img src='../ods/images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='id' value='<%=bean.getID()%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='sending' value='<%=div_name%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>
</form>

</body>
</html>
