<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "���ο��� ȸ���ϱ�"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
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
	String msg = "";		//��ܸ������� ����

	String id = "";			//������ id
	String name = "";		//������ �̸�
	String division = "";	//������ �μ���
	String tel = "";		//������ ��ȭ��ȣ

	//����ó�� ����
	String LIST="";			//�����θ��
	String RES="";			//�߼�/�̹߼� �ޱ�
	String subject="";		//��������
	String content="";		//����

	String path = "";		//����path
	String pfie = "";		//���� ���ϸ� (window)
	String state= "";		//email ���� ��ü���ο������� �Ǵ�

	String apath = "";		//÷������ Path
	String pad1o = "";		//÷�ε� ���ϸ�1 �����̸�	
	String pad1f = "";		//÷�ε� ���ϸ�1
	String pad2o = "";		//÷�ε� ���ϸ�2 �����̸�	
	String pad2f = "";		//÷�ε� ���ϸ�2
	String pad3o = "";		//÷�ε� ���ϸ�3 �����̸�	
	String pad3f = "";		//÷�ε� ���ϸ�3	

	String SMSG = "";		//��޼��û��� �ٽ������ϱ�
	String RYN = "";		//ȸ�Ÿ޴� ���÷��̿��� (���������� ȸ�Ű���)

	//���޹��� pid (from post_view.jsp)
	String rpid ="";		//���޹��� pid�� ����/�����۽� �ش系�� ����Ű ����

	//Email�������� �����Ǵ�
	String isEmail = "";	//���ڸ������� �Ǵ�
	String host = "";		//������ ������
	String toAddress = "";	//�޴»�� �ּ�
	String fromAddress = "";//�����»�� �ּ�
	String fromName = "";	//�����»�� �̸�
	/*********************************************************************
	 	������ login �˾ƺ���
	*********************************************************************/
	id = login_id; 		//������ login id

	String[] idColumn = {"a.id","a.name","b.ac_name","a.office_tel"};
	String query = "where a.ac_id = b.ac_id and a.id='" + id + "'";
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);	
	bean.setSearchWrite(query);			
	bean.init_write();

	while(bean.isAll()) {
		name = bean.getData("name");				//������ ��
		division = bean.getData("ac_name");		//������ �μ���
		tel = bean.getData("office_tel");			//������ ��ȭ��ȣ
	} //while

	/*********************************************************************
	 	������ ���� ���޹޾� ȭ�鿡 ����ϱ� (from post_view.jsp)
	*********************************************************************/
	String mPID = request.getParameter("PID");

	//pid�� �̿��Ͽ� ���ó����� �д´�.
	if(mPID != null) {
		//�ʱ�ȭ
		subject=content=LIST=rpid=SMSG=""; 
		pad1o=pad1f=pad2o=pad2f=pad3o=pad3f="";
		path=pfie=host=toAddress=isEmail="";
		//����/�����۽� �ش系�� ������
		rpid = mPID;

		//�ش系���б�
		String[] mCls = {"pid","post_subj","writer_id","writer_name","bon_path","bon_file","post_state"};
		bean.setTable("POST_MASTER");			
		bean.setColumns(mCls);	
		bean.setOrder("pid ASC");	
		bean.setClear();
		bean.setSearch("pid",mPID);			
		bean.init_unique();	
	
		while(bean.isAll()) {
			subject = bean.getData("post_subj");			//����
			String wid = bean.getData("writer_id");			//������� ���
			if(wid == null) wid = "";
			String wna = bean.getData("writer_name");		//������� �̸�
			if(wna == null) wna = "";
			LIST = wid + "/" + wna + ";";					//�����ڷ� ����
			SMSG="CFM";										//�⺻���� ����Ȯ�� ����

			String Path = bean.getData("bon_path");			//����path
			if(Path == null) Path = "/";
			path = crp + Path;							

			pfie = bean.getData("bon_file");				//���� ���ϸ� (window)
			if(pfie == null) pfie = "";

			state = bean.getData("post_state");				//email���� �����Ǵ�

			//Email�� ���������� �Ǵ��ϱ�
			if(state.equals("email")) {
				isEmail = "Y";									//email��
				LIST = wna;										//�����ڷ� ����
				if(wna.indexOf("(") > 0) {						
					toAddress = wna.substring(wna.indexOf("(")+1,wna.indexOf(")"));
				} else toAddress = wna;							//�޴»�� �ּ�
			} else isEmail = "N";								//email �ƴ�
		} //while
	} //if
%>

<HTML><HEAD><TITLE>���������ۼ�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_n.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--��ư-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right">
		  <a href="Javascript:postReceiver();"><img border="0" src="../images/bt_sel_receiver.gif" align="absmiddle"></a>
		  <a href="Javascript:postSend();"><img border="0" src="../images/bt_export.gif" align="absmiddle"></a>
          <a href="Javascript:postClose();"><img border="0" src="../images/bt_close.gif" align="absmiddle"></a></td></tr></tbody></table>

    <!--�߼�����-->
	<form method="post" name="sForm" encType="multipart/form-data" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">�ۼ���</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=name%> [��ȭ: <%=tel%>,  �μ���:<%=division%>,  �ۼ���:<%=bean.getTime()%>]</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">������</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="rec_name" rows=2 cols=75 ><%=LIST%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><input type="text" name="SUBJECT" size=76 value="[RE]<%=subject%>"></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
		  <% 
			//state.equals("email")  //email ������			
			//Textfile �б�
			textFileReader text = new com.anbtech.file.textFileReader();			//�����б�
			String Textfile = upload_path + path + "/" + pfie;

			String buf = "";
			buf += ">";
			buf += text.getFileString(Textfile);
			buf += "\r=========================== ȸ�� ���� ================================";
	 
			//ȭ������ϱ�
			if(state.equals("email")) {
				if(buf.indexOf("><") == -1) {
					out.println("<TEXTAREA name='CONTENT' rows=22 cols=75>");		//���� ����
					out.println(buf.toString());
					out.println("</TEXTAREA>"); 
				} else {
					out.println("<TEXTAREA name='CONTENT' rows=22 cols=75>");		//���� ����
					out.println("\r=========================== ȸ�� ���� ================================");
					out.println("</TEXTAREA>"); 
				}
			} else {
				out.println("<TEXTAREA name='CONTENT' rows=22 cols=75>");		//���� ����
				out.println(buf.toString());
				out.println("</TEXTAREA>"); 
			}
			
		%>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
				<input type="file" name="UP_FILE1" size=55><br>
				<input type="file" name="UP_FILE2" size=55><br>
				<input type="file" name="UP_FILE3" size=55>
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/bt_close.gif"  hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
	<input type="hidden" name="ReturnReceipt">
	<input type="hidden" name="SecretSetup">
	<input type="hidden" name="ReplySetup">
	<input type="hidden" name="PID">
	<input type="hidden" name="res">
  </form>
  
<% //���ϻ����� ��ũ��Ʈ�� ó���ϱ� ���� �ۼ� %>
<form name=d1Form method="post" style="margin:0">
<input type="hidden" name="file1" value='<%=pad1f%>'>
</form>

<form name=d2Form method="post" style="margin:0">
<input type="hidden" name="file2" value='<%=pad2f%>'>
</form>

<form name=d3Form method="post" style="margin:0">
<input type="hidden" name="file3" value='<%=pad3f%>'>
</form>

<DIV id="lding" style="position:absolute;left:150px;top:370px;width:450px;height:200px;visibility:hidden;">
	<TABLE width="450" border="0" cellspacing=1 cellpadding=1 bgcolor="white">
	<TR><TD height="35" align="center" valign="middle"><font color='blue'>
		<marquee behavior='alternate' width='80%' scrollamount=1>
		������ ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.
		</marquee></font>
	</TD> 
	</TR>
	</TABLE>
</DIV>

</BODY></HTML>

<script>
<!--
//������â �ʱ�ȭ
function setFocus() 
{
	defaultStatus = "����������";			//������ �ϴ� ��¸޽���
	window.resizeTo(800,500);			//������ ũ������	
	window.moveTo(200,200);				//������ �����ġ ����	
	document.sForm.SUBJECT.focus();
}

//�������ۼ� �ݱ�
function postClose()
{
	self.close();
}
//������
function postReceiver()
{
	receivers = document.sForm.rec_name.value;
	wopen("post_Share.jsp?Title=Search&Rec="+receivers+"&target=sForm.rec_name","post_rSel","510","467");
}
//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
//�߼��ϱ�
function postSend()
{
	if(sForm.SUBJECT.value == ""){	alert("������ �Է��Ͻʽÿ�.");	return;	}
	if(sForm.CONTENT.value == ""){	alert("������ �Է��Ͻʽÿ�.");	return;	}
	if(sForm.rec_name.value == ""){	alert("�������� �����Ͻʽÿ�.");return;	}

	if(confirm('�߼� �Ͻðڽ��ϱ�?') == false) {alert("�߼����� �ʽ��ϴ�."); self.close()};
	document.all['lding'].style.visibility="visible";
	document.sForm.action="post_replySave.jsp";
	document.sForm.PID.value='<%=mPID%>';
	document.sForm.res.value="SND";
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//�����ưó������
function dbclick() 
{
    if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�."); 	
}
-->
</script>
