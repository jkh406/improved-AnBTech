<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "��ܰ��� ���"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage	= "../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.util.*"
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
	normalFormat nmf = new com.anbtech.util.normalFormat("000");			//������ȣ �Ϸù�ȣ
	
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

	/*********************************************************************
	 	��ܰ��� ������ȣ �ڵ�ä���ϱ�
	*********************************************************************/
	String[] recColumn = {"serial_no"};
	bean.setTable("OutDocument_receive");		
	bean.setColumns(recColumn);
	bean.setOrder("serial_no DESC");	
	bean.setSearch("","");
	bean.init_unique();

	String serial_no = "00-000";
	if(bean.isAll()) serial_no = bean.getData("serial_no");			//�Ϸù�ȣ
	int sno = Integer.parseInt(serial_no.substring(serial_no.length()-3,serial_no.length()))+1;
	String year = anbdt.getYear(); 	String yy = year.substring(2,4);
	serial_no = yy+"-"+nmf.toDigits(sno);
	
%>

<script language=javascript>
<!--
//���źμ��� ã��
function searchChief()
{
//window.open("searchDivision.jsp?target=eForm.receive","division","width=510,height=467,scrollbar=no,toolbar=no,status=no,resizable=no");
	wopen('searchDivision.jsp?target=eForm.receive','','510','467');
}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//����ϱ�
function sendSave()
{

	var f = document.eForm;

	if(f.doc_id.value == ""){
		alert("������ȣ�� �Է��Ͻʽÿ�.");
		f.doc_id.focus();
		return;
	}

	if(f.receive.value == ""){
		alert("���źμ��� �Է��Ͻʽÿ�.");
		f.receive.focus();
		return;
	}

	if(f.subject.value == ""){
		alert("������ �Է��Ͻʽÿ�.");
		f.subject.focus();
		return;
	}

	//ó���� �޽��� ���
	document.all['lding'].style.visibility="visible";

	document.eForm.action='../servlet/OutDocumentRecMultiServlet';
	document.eForm.mode.value='ODR_write';	
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
			  <TD valign='middle' class="title"><img src="../ods/images/blet.gif"> ��ܰ��� ���� ����</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href="javascript:sendSave();"><img src="../ods/images/bt_save.gif" border="0"></a> <a href="javascript:history.go(-1)"><img src="../ods/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
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
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������ȣ</td>
           <td width="40%" height="25" class="bg_04"><%=serial_no%></td>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="40%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="40%" height="25" class="bg_04">
				<input class="box" size='25' type='text' name='sending' value=''></td>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�߼�����</td>
           <td width="40%" height="25" class="bg_04">
				<input class="box" size='25' type='text' name='send_date' value=''></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������ȣ</td>
           <td width="40%" height="25" class="bg_04">
				<input class="text_01" size='25' type='text' name='doc_id' value=''></td>
			 <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="40%" height="25" class="bg_04">
				<input size='25' type='text' name='sheet_cnt' value=''></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="40%" height="25" class="bg_04">
				<textarea rows="3" name="receive" cols='22' readOnly class="text_01"></textarea>&nbsp;<a href="Javascript:searchChief();"><img src="../ods/images/bt_search2.gif" border='0'></a>
		   <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�� �� ��</td>
           <td width="40%" height="25" class="bg_04"><%=user_name%>
				<input type='hidden' name='user_id' value='<%=user_id%>'>
				<input type='hidden' name='user_name' value='<%=user_name%>'>
				<input type='hidden' name='user_rank' value='<%=user_rank%>'>
				<input type='hidden' name='div_id' value='<%=div_id%>'>
				<input type='hidden' name='div_code' value='<%=div_code%>'>
				<input type='hidden' name='code' value='<%=code%>'>
				<input type='hidden' name='div_name' value='<%=div_name%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="90%" height="25" class="bg_04" colspan="3">
				<input class="text_01" size='60' type='text' name='subject' value=''></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="90%" height="25" class="bg_04" colspan="3">
				<textarea rows="3" name="content" cols="93"></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">÷������</td>
           <td width="90%" height="25" class="bg_04" colspan="3">
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
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�������</td>
           <td width="90%" height="25" class="bg_04">
					<input type="radio" checked name="module_name" value='���źμ�'>���źμ�
					<input type="radio" name="module_name" value='�Խ���'>�Խ���</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='id' value='<%=bean.getID()%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='serial_no' value='<%=serial_no%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>
</form>

<DIV id="lding" style="position:absolute;left:300px;top:150px;width:224px;height:150px;visibility:hidden;">
	<img src='../ods/images/loading8.gif' border='0' width='214' height='200'>
</DIV>


</body>
</html>
