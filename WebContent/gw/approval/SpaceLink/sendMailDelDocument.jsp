<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page		
	info= "���ο��� �ۼ�"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.text.*"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.file.textFileReader"

%>
<%@	page import="com.anbtech.date.anbDate"			%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%

	//�޽��� ���޺���
	String Message="";		//�޽��� ���� ����  

	String id = "";			//������ id
	String name = "";		//������ �̸�
	String division = "";	//������ �μ���
	String tel = "";		//������ ��ȭ��ȣ

	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	FileWriteString text = new com.anbtech.file.FileWriteString();			//������ ���Ϸ� ���
	textFileReader Rtext = new com.anbtech.file.textFileReader();			//text���� �б�

	/*********************************************************************
	 	������ login �˾ƺ���
	*********************************************************************/
	id = sl.id; 		//������ login id
	
	String[] idColumn = {"a.id","a.name","a.office_tel","b.ac_name"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	String item_data = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	while(bean.isAll()) {
		name = bean.getData("name");				//������ ��
		division = bean.getData("ac_name");			//������ �μ���
		tel = bean.getData("office_tel");			//������ ��ȭ��ȣ
	} //while

	/*********************************************************************
	 	����� �������� �ۼ��ϱ�
	*********************************************************************/
	String pid = request.getParameter("pid");
	String subject="[�����ȹ���] ��û�Ͻ� �����Դϴ�. �����Ͻñ� �ٶ��ϴ�.";		//��������
	String content="��û�Ͻ� �������� �Դϴ�.";										//����
	String rpid = anbdt.getID();
	
%>

<HTML><HEAD><TITLE>�������ۼ�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form method="post" name="sForm" encType="multipart/form-data" style="margin:0">
	
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../../images/pop_mail_n.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--��ư-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right">
 		  <a href="Javascript:postReceiver();"><img border="0" src="../../images/bt_sel_receiver.gif" align="absmiddle"></a>
		  <a href="Javascript:postSend();"><img border="0" src="../../images/bt_export.gif" align="absmiddle"></a>
          <a href="Javascript:postClose();"><img border="0" src="../../images/bt_close.gif" align="absmiddle"></a></td></tr></tbody></table>

    <!--�߼�����-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">�ۼ���</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=name%> [��ȭ: <%=tel%>,  �μ���:<%=division%>,  �ۼ���:<%=bean.getTime()%>]</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">������</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="rec_name" rows=2 cols=75  class='text_01' readOnly></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><input type="text" name="SUBJECT" size=76 value="<%=subject%>" class='text_01' ></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">���û���</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
			<input type="checkbox" name="ReturnReceipt" value="CFM">����Ȯ��&nbsp;
			<input type="checkbox" name="SecretSetup" value="SEC">�������&nbsp;
			<input type="checkbox" name="ReplySetup" value="RSP">�������
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="CONTENT" rows=22 cols=75 class='text_01'><%=content%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input size=60 type="text" name="ref_name"  class='text_01' readonly> <a href="Javascript:searchRefDocument();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../../images/bt_close.gif"  hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
<input type="hidden" name="pid" value='<%=anbdt.getID()%>'>
<input type="hidden" name="ref_id">
<input type="hidden" name="res">
</form>


</BODY></HTML>

<script>
<!--
//�߼� �����ϱ�
function postSend()
{
	if(sForm.SUBJECT.value == ""){	alert("������ �Է��Ͻʽÿ�.");	return;	}
	if(sForm.CONTENT.value == ""){	alert("������ �Է��Ͻʽÿ�.");	return;	}
	if(sForm.rec_name.value == ""){	alert("�������� �����Ͻʽÿ�.");return;	}
	if(sForm.ref_name.value == ""){	alert("��û������ �����Ͻʽÿ�.");return;	}

	var sel = confirm('�߼� �Ͻðڽ��ϱ�?');
	if(sel == false) {	alert("�߼����� �ʽ��ϴ�."); return; }

	var ref_id = document.sForm.ref_id.value;
	var content = document.sForm.CONTENT.value;
	content += "<p><p><a href=\"javascript:viewDel();\">�󼼳��뺸��</a> \n\n";
	content +="<script language=javascript><!-- \n";
	content +=" function viewDel(){ \n";
	content +="wopen('../../../../../../servlet/ApprovalDetailServlet?mode=DEL_BOX&PID="+ref_id+"','save_doc','860','650');} \n";
	content +=" function wopen(url, t, w, h) { var sw; var sh; ";
	content +=" sw = (screen.Width - w) / 2; sh = (screen.Height - h) / 2 - 50; ";
	content +=" window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+',Top='+sh+',scrollbars=yes,toolbar=no,status=no,resizable=no'); } \n";
	content +=" --><\/script>";

	document.sForm.action="saveMailDelDocument.jsp";
	document.sForm.res.value="SND";
	document.sForm.CONTENT.value=content;
	document.sForm.submit();
}
//�������ۼ� �ݱ�
function postClose()
{
	this.close();
}
//������
function postReceiver()
{
	receivers = document.sForm.rec_name.value; 
	wopen("../../mail/post_Share.jsp?Title=Search&Rec="+receivers+"&target=sForm.rec_name","post_rSel","510","467");
}
//���ù��� ã��
function searchRefDocument()
{
	var url = "searchStoreHouseDocument.jsp?target_id=sForm.ref_id&target_name=sForm.ref_name&";
	url += "rec_name=sForm.rec_name";
	wopen(url,"ref_id","520","310","scrollbars=no,toolbar=no,status=no,resizable=no");
}
//â
function wopen(url, t, w, h, st) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+st);
}
-->
</script>