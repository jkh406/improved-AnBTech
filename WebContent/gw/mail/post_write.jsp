<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "���ο��� �ۼ�"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage = "../../admin/errorpage.jsp" 
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

	//����ó�� ����
	String LIST="";			//�����θ��
	String RES="";			//�߼�/�̹߼� �ޱ�
	String subject="";		//��������
	String content="";		//����
	String bPath = "";		//�������� path
	String path = "";		//����path Ȯ��path ����(crp)
	String bFile = "";		//�������ϸ�

	String apath = "";		//÷������ Path
	String pad1o = "";		//÷�ε� ���ϸ�1 �����̸�	
	String pad1f = "";		//÷�ε� ���ϸ�1
	String file1_size="";	//����ũ��

	String pad2o = "";		//÷�ε� ���ϸ�2 �����̸�	
	String pad2f = "";		//÷�ε� ���ϸ�2
	String file2_size="";	//����ũ��

	String pad3o = "";		//÷�ε� ���ϸ�3 �����̸�	
	String pad3f = "";		//÷�ε� ���ϸ�3	
	String file3_size="";	//����ũ��

	String SMSG = "";		//��޼��û��� �ٽ������ϱ�

	//���޹��� pid (from post_main.jsp)
	String rpid ="";		//���޹��� pid�� ����/�����۽� �ش系�� ����Ű ����

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
		division = bean.getData("ac_name");		//������ �μ���
		tel = bean.getData("office_tel");			//������ ��ȭ��ȣ
	} //while

	/*********************************************************************
	 	�������� ���޹ޱ� (from post_main.jsp)
	*********************************************************************/
	String mPID = request.getParameter("PID");

	//pid�� �̿��Ͽ� ���ó����� �д´�.
	if(mPID != null) {
		//�ʱ�ȭ
		subject=content=LIST=rpid=SMSG=""; 
		pad1o=pad1f=pad2o=pad2f=pad3o=pad3f="";

		//����/�����۽� �ش系�� ������
		rpid = mPID;

		//�ش系���б�
		String[] mCls = {"pid","post_subj","post_receiver","post_select","bon_path","bon_file","add_1_original","add_1_file","add_2_original","add_2_file","add_3_original","add_3_file"};
		bean.setTable("POST_MASTER");			
		bean.setColumns(mCls);	
		bean.setOrder("pid ASC");	
		bean.setClear();
		bean.setSearch("pid",mPID);			
		bean.init_unique();	

		bPath = "";
		bFile = "";
		while(bean.isAll()) {
			subject = bean.getData("post_subj");
			LIST = bean.getData("post_receiver");

			String psel = bean.getData("post_select");
			if((psel != null) && (psel.length() > 0)) {
				SMSG="";
				if(psel.indexOf('F') > 0) SMSG +="CFM,";		//����Ȯ�� ��û
				if(psel.indexOf('E') > 0) SMSG +="SEC,";		//���Ȯ�� ��û
				if(psel.indexOf('P') > 0) SMSG +="RSP,";		//ȸ��Ȯ�� ��û	
			}

			bPath = bean.getData("bon_path");
			if(bPath == null) path = crp + "/";
			else 
				path = crp + bPath.substring(0,bPath.lastIndexOf('/'))+"/addfile";

			bFile = bean.getData("bon_file");

			pad1o = bean.getData("add_1_original");				//÷�ε� ���ϸ�1 �����̸�
			pad1f = bean.getData("add_1_file");					//÷�ε� ���ϸ�1
			pad2o = bean.getData("add_2_original");				//÷�ε� ���ϸ�2 �����̸�
			pad2f = bean.getData("add_2_file");					//÷�ε� ���ϸ�2
			pad3o = bean.getData("add_3_original");				//÷�ε� ���ϸ�3 �����̸�
			pad3f = bean.getData("add_3_file");					//÷�ε� ���ϸ�3			

			//÷������ path���ϱ�
			int lslash = bPath.lastIndexOf('/');
			if(lslash == -1) lslash = 0;
			apath = upload_path + crp + bPath.substring(0,lslash) + "/addfile/";	//÷������ Path

			//÷������ ����ũ�� ���ϱ�
			String fpath1 = apath + pad1f;
			File fn1 = new File(fpath1);
			file1_size = Long.toString(fn1.length());	//÷��1 ����ũ��

			String fpath2 = apath + pad2f;
			File fn2 = new File(fpath2);
			file2_size = Long.toString(fn2.length());	//÷��2 ����ũ��

			String fpath3 = apath + pad3f;
			File fn3 = new File(fpath3);
			file3_size = Long.toString(fn3.length());	//÷��3 ����ũ��
		} //while

		//Textfile �б�
		String Textfile = upload_path + crp + bPath + "/" + bFile;
		try { content = Rtext.getFileString(Textfile);	   		//Text file Read 
		} catch (Exception e) { }		
	} //if

	/*********************************************************************
		���������� ���� �ٽ����� ÷�������� �����ϰ��� �Ҷ�
	*********************************************************************/
	String del_file1 = request.getParameter("file1");			//÷������1�� �����Ұ��
	String del_file2 = request.getParameter("file2");			//÷������2�� �����Ұ��	
	String del_file3 = request.getParameter("file3");			//÷������3�� �����Ұ��	
	
	if(del_file1 != null) {
		//DB update
		String up_or1 = "update POST_MASTER set add_1_original='',";
			  up_or1 += "add_1_file='' where pid = '" + rpid + "'";
		try { bean.execute(up_or1); } catch (Exception e) { }
		//÷������ ����
		String delFile = apath + pad1f;			//÷�����ϸ�
		Rtext.delFilename(delFile);								//÷������ ����

		//÷�γ��� Clear
		pad1o=pad1f="";
	} else if(del_file2 != null) {
		//DB update
		String up_or2 = "update POST_MASTER set add_2_original='',";
			  up_or2 += "add_2_file='' where pid = '" + rpid + "'";
		try { bean.execute(up_or2); } catch (Exception e) { }

		//÷������ ����
		String delFile = apath + pad2f;			//÷�����ϸ�
		Rtext.delFilename(delFile);								//÷������ ����

		//÷�γ��� Clear
		pad2o=pad2f="";
	} else if(del_file3 != null) {
		//DB update
		String up_or3 = "update POST_MASTER set add_3_original='',";
			  up_or3 += "add_3_file='' where pid = '" + rpid + "'";
		try { bean.execute(up_or3);} catch (Exception e) { }

		//÷������ ����
		String delFile = apath + pad3f;			//÷�����ϸ�
		Rtext.delFilename(delFile);								//÷������ ����

		//÷�γ��� Clear
		pad3o=pad3f="";
	}

%>

<HTML><HEAD><TITLE>�������ۼ�</TITLE>
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
          <a href="Javascript:postTemp();"><img border="0" src="../images/bt_save_tmp.gif" align="absmiddle"></a>
          <a href="Javascript:postClose();"><img border="0" src="../images/bt_close.gif" align="absmiddle"></a></td></tr></tbody></table>

    <!--�߼�����-->
	<form action="post_write.jsp" method="post" name="sForm" encType="multipart/form-data" style="margin:0">
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
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="rec_name" rows=2 cols=75 readOnly class='text_01'><%=LIST%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><input type="text" name="SUBJECT" size=76 value="<%=subject%>" class='text_01'></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">���û���</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
			<input type="checkbox" name="ReturnReceipt" value="CFM">����Ȯ��&nbsp;
			<input type="checkbox" name="SecretSetup" value="SEC">�������&nbsp;
			<input type="checkbox" name="ReplySetup" value="RSP">�������
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="CONTENT" rows=22 cols=75 class='text_01'><%=content%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
					<img src="../images/b-attach.gif" border="0">÷��1
						<% if(pad1f.length() == 0) { %>
								<input type="file" name="UP_FILE1" size=50>
						<% } else { %>
								<a href='post_downloadp.jsp?fname=<%=pad1o%>&fsize=<%=file1_size%>&umask=<%=pad1f%>&extend=<%=path%>'><%=pad1o%></a>
								<a href=javascript:addFile1_Delete('<%=apath%><%=pad1f%>')>[����]</a>
						<% } %>
					<br><img src="../images/b-attach.gif" border="0">÷��2
					    <% if(pad2f.length() == 0) { %>
								<input type="file" name="UP_FILE2" size=50>
					    <% } else { %>
								<a href='post_downloadp.jsp?fname=<%=pad2o%>&fsize=<%=file2_size%>&umask=<%=pad2f%>&extend=<%=path%>'><%=pad2o%></a>
								<a href=javascript:addFile2_Delete('<%=apath%><%=pad2f%>')>[����]</a>
					    <% } %>
					<br><img src="../images/b-attach.gif" border="0">÷��3
					    <% if(pad3f.length() == 0) { %>
								<input type="file" name="UP_FILE3" size=50>
					    <% } else { %>
								<a href='post_downloadp.jsp?fname=<%=pad3o%>&fsize=<%=file3_size%>&umask=<%=pad3f%>&extend=<%=path%>'><%=pad3o%></a>
								<a href=javascript:addFile3_Delete('<%=apath%><%=pad3f%>')>[����] </a>
					    <% } %>  
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
<input type="hidden" name="pid" value='<%=mPID%>'>
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

//�����߼�
function postSend()
{
	if(sForm.SUBJECT.value == ""){	alert("������ �Է��Ͻʽÿ�.");	return;	}
	if(sForm.CONTENT.value == ""){	alert("������ �Է��Ͻʽÿ�.");	return;	}
	if(sForm.rec_name.value == ""){	alert("�������� �����Ͻʽÿ�.");return;	}

	var c = confirm("�ۼ��Ͻ� �������� �߼��Ͻðڽ��ϱ�?");
	if(c){
		document.all['lding'].style.visibility="visible";
		document.sForm.action="post_writeSave.jsp";
		document.sForm.res.value="SND";
		document.onmousedown=dbclick;
		document.sForm.submit();
	}
}

//�̹߼� �����ϱ�
function postTemp()
{
	if(sForm.SUBJECT.value == ""){	alert("������ �Է��Ͻʽÿ�.");	return;	}
	if(sForm.CONTENT.value == ""){	alert("������ �Է��Ͻʽÿ�.");	return;	}

	if(confirm('���� �Ͻðڽ��ϱ�?') == false) {alert("�������� �ʽ��ϴ�."); self.close()};
	document.all['lding'].style.visibility="visible";
	document.sForm.action="post_writeSave.jsp";
	document.sForm.res.value="TMP";
	document.onmousedown=dbclick;
	document.sForm.submit();
	
}

//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//������
function postReceiver()
{
	receivers = document.sForm.rec_name.value;
	wopen("post_Share.jsp?Title=Search&Rec="+receivers+"&target=sForm.rec_name","post_rSel","510","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//÷������1 ����
function addFile1_Delete(file_name)
{
	var file_add = file_name;
	document.d1Form.action="post_write.jsp?PID=<%=rpid%>";
	document.d1Form.submit();
}
//÷������2 ����
function addFile2_Delete(file_name)
{
	var file_add = file_name;
	document.d2Form.action="post_write.jsp?PID=<%=rpid%>";
	document.d2Form.submit();
}
//÷������3 ����
function addFile3_Delete(file_name)
{
	var file_add = file_name;
	document.d3Form.action="post_write.jsp?PID=<%=rpid%>";
	document.d3Form.submit();
}

//�����ưó������
function dbclick() 
{
    if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�."); 	
}

-->
</script>

