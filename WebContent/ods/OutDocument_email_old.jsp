<%@ include file="../admin/configPopUp.jsp"%>
<%@ page		
	info= "��ܰ��� �̸��ϼ����� �����ϱ�"		
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
	//-----------------------------------
	//�ʱ�ȭ ����
	//-----------------------------------
	com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
	com.anbtech.date.anbDate anbdt = new anbDate();							//����
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�
	FileWriteString file = new com.anbtech.file.FileWriteString();			//���丮 �����ϱ�

	//-----------------------------------
	// ���� ��������
	//-----------------------------------
	String id = "";					//������ȣ
	String user_name="";			//����� �̸�
	String doc_id="";				//������ȣ
	String slogan="";				//���ΰ�
	String title_name="";			//�μ� Title��
	String in_date="";				//�������		
	String receive="";				//����
	String reference="";			//����
	String sending="";				//�߽�
	String rec_name="";				//������ �� (�޸��� ����)
	String rec_mail="";				//������ �ּ� (�޸��� ����)
	String subject="";				//����
	String address="";				//�߽��� �ּ�
	String tel="";					//�߽��� ��ȭ��ȣ
	String fax="";					//�߽��� �ѽ���ȣ
	String bon_path="";				//�������� Ȯ��path
	String bon_file="";				//�������� ���ϸ�
	String content="";				//��������
	String firm_name="";			//�߽źμ���
	String representative="";		//�߽źμ� ��ǥ��	
	String fname="";				//����:���Ͽ�����	
	String sname="";				//����:���������		
	String ftype="";				//����:����Ȯ���ڸ�	
	String fsize="";				//����:����ũ��
	String[][] addFile;				//÷�ΰ��ó��� ���
	String module_name="";			//�����������
	int attache_cnt = 4;			//÷������ �ִ밹�� (�̸�)

	//-----------------------------------
	// �ۼ��� ���� ��������
	//-----------------------------------
	String user_id = "";			//�ش��� ���
	String user_rank = "";			//�ش��� ����
	String div_id = "";				//�ش��� �μ��� �����ڵ�
	String div_name = "";			//�ش��� �μ���
	String div_code = "";			//�ش��� �μ��ڵ�
	String code = "";				//�ۼ��� �μ�Tree �����ڵ�

	/*********************************************************************
	 	�������� ���� �б�
	*********************************************************************/	
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_One");
	table = new OfficialDocumentTable();
	Iterator table_iter = table_list.iterator();

	if(table_iter.hasNext()){
		table = (OfficialDocumentTable)table_iter.next();

		id=table.getId();							//������ȣ
		user_id=table.getUserId();					//����� ���
		user_name=table.getUserName();				//����� �̸�
		doc_id=table.getDocId();					
			if(doc_id == null) doc_id = "";			//������ȣ
		slogan=table.getSlogan();					//���ΰ�
		title_name=table.getTitleName();			//�μ� Title��
		in_date=table.getInDate();					//�������		
		receive=table.getReceive();;				//����
		reference=table.getReference();				
			if(reference == null) reference = "";	//����
		sending=table.getSending();					//�߽�
		rec_name=table.getRecName();				//������ �̸�(�޸��� ����)
		rec_mail=table.getRecMail();				//������ �ּ�
		subject=table.getSubject();					//����	
		address=table.getAddress();					//�߽��� ȸ���ּ�
		tel=table.getTel();							//��ȭ��ȣ
		fax=table.getFax();							//�ѽ���ȣ
		bon_path=table.getBonPath();				//�������� Ȯ��path
		bon_file=table.getBonFile();				//�������� ���ϸ�
		firm_name=table.getFirmName();				//�߽źμ���
		representative=table.getRepresentative();	//�߽źμ� ��ǥ��	
		fname=table.getFname();						//����:���Ͽ�����	
		sname=table.getSname();						//����:���������		
		ftype=table.getFtype();						//����:����Ȯ���ڸ�	
		fsize=table.getFsize();						//����:����ũ��
		module_name=table.getModuleName();			//�����������
	}

	//���������б�
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	content = text.getFileString(full_path);

	//÷�������о� �迭�� ���
	if(fname == null) fname = "";
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	addFile = new String[cnt][5];
	for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

	if(fname.length() != 0) {
		StringTokenizer f = new StringTokenizer(fname,"|");		//���ϸ� ���
		int m = 0;
		while(f.hasMoreTokens()) {
			addFile[m][0] = f.nextToken();
			addFile[m][0] = addFile[m][0].trim(); 
			if(addFile[m][0] == null) addFile[m][0] = "";
			m++;
		}
		StringTokenizer t = new StringTokenizer(ftype,"|");		//����type ���
		m = 0;
		while(t.hasMoreTokens()) {
			addFile[m][1] = t.nextToken();
			addFile[m][1] = addFile[m][1].trim();
			if(addFile[m][1] == null) addFile[m][1] = "";
			m++;
		}
		StringTokenizer s = new StringTokenizer(fsize,"|");		//����ũ�� ���
		m = 0;
		while(s.hasMoreTokens()) {
			addFile[m][2] = s.nextToken();
			addFile[m][2] = addFile[m][2].trim();
			if(addFile[m][2] == null) addFile[m][2] = "";
			m++;
		}
		StringTokenizer o = new StringTokenizer(sname,"|");		//�������� ���
		m = 0;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();
			addFile[m][3] = addFile[m][3].trim() + ".bin";			
			if(addFile[m][3].equals(".bin")) addFile[m][3] = "";
			//÷�����Ͽ��� Ȯ����(_1_2_3..)��ȣ ã��
			if(addFile[m][3].length() > 0) {
				int en = addFile[m][3].indexOf("_");
				addFile[m][4] = addFile[m][3].substring(en+1,en+2);
			} else addFile[m][4] = "0";
			m++;
		}
	}
	
	/*********************************************************************
	 	�ش��� ���� �˾ƺ��� (�����) : ����� ���� [����]
	*********************************************************************/
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","code"};
	bean.setTable("user_table a,class_table b,rank_table c");		
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	String query = "where (a.id ='"+user_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
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
//�̸��� ������
function sendEmail()
{
	document.eForm.action='../servlet/OutDocumentMultiServlet';
	document.eForm.mode.value='ODS_E';	
	document.eForm.submit();
}
//�ݱ�
function winClose()
{
	self.close();
}
-->
</script>

<HTML><HEAD><TITLE>�������Ϲ߼�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../ods/css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
		<!--Ÿ��Ʋ-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
			<TR>
				<TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../ods/images/pop_title_mail.gif" border='0'></TD></TR>
			<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE></TD></TR>

    <!--��ư-->
	<tr><TD align='right'>
		<TABLE cellSpacing=0 cellPadding=0 border=0>
		<TBODY>
			<TR><TD align=left height='32' style='padding-right:20px'>
				<a href="javascript:sendEmail();"><img src="../ods/images/bt_export.gif" border=0></a> <a href="javascript:winClose();"><img src="../ods/images/bt_close.gif" border=0></a>
			</TD></TR></TBODY></TABLE></TD></TR>

	<!--����������-->
	<TR><TD align='center'>
		<table cellspacing=0 cellpadding=2 width="94%" border=0>
		<tbody><form name="eForm" method="post" encType="multipart/form-data" style="margin:0">
		
		<!-- <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
          <tr>
		   <td height=22 colspan="2"><img src="../ods/images/mail_receiver.gif" width="209" height="25" border="0"></td></tr>-->
			<tr bgcolor="C7C7C7"><td height=1 colspan="2"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����ȸ��</td>
				<td width="87%" height="25" class="bg_04"><%=receive%></td>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�����ּ�</td>
				<td width="87%" height="25" class="bg_04">
					<textarea rows="2" name="rec_mail" cols='70' style="border:1 solid #787878;" class='text_01'><%=rec_mail%></textarea>
				<br> * �������� ������ �� ��� �޸�(,)�� ����
				</td>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>
				<input type='hidden' name='mode' value=''><input type='hidden' name='id' value='<%=id%>'>
				</form>

    <!--�߽�����-->
		<table cellspacing=0 cellpadding=2 width="94%" border=0>
		<tbody>
			<tr><td height=20 colspan="4"></td></tr>
         <!--<tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4"><img src="../ods/images/send_info.gif" width="209" height="25" border="0"></td></tr>-->
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�Ӹ���</td>
				<td width="37%" height="25" class="bg_04"><%=title_name%></td>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������</td>
				<td width="37%" height="25" class="bg_04"><%=firm_name%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">ǥ��</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=slogan%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��ǥ�ڸ�</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=representative%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��ȭ��ȣ</td>
				<td width="37%" height="25" class="bg_04"><%=tel%></td>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�ѽ���ȣ</td>
				<td width="37%" height="25" class="bg_04"><%=fax%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">ȸ���ּ�</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=address%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>

    <!--��������-->
		<table cellspacing=0 cellpadding=2 width="94%" border=0>
		<tbody>
			<tr><td height=20 colspan="4"></td></tr>
         <!--<tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4"><img src="../ods/images/doc_info.gif" width="209" height="25" border="0"></td></tr>-->
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������ȣ</td>
				<td width="87%" height="25" class="bg_04" colspan="3"><%=doc_id%></td>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�����</td>
				<td width="37%" height="25" class="bg_04"><%=user_name%></td>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�������</td>
				<td width="37%" height="25" class="bg_04"><%=in_date%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=reference%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=subject%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="10" cols="70" style="border:1 solid #787878;" readonly><%=content%></textarea></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">÷������</td>
				<td width="87%" height="25" colspan="3" class="bg_04">
		   <% 
				int ary_cnt = addFile.length;	//�迭�� ����
				for(int i=0; i<ary_cnt; i++) {
					out.println("&nbsp;<a href='../ods/attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
					out.println("<br>");
				}
			%>
				</td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--������-->
		<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
			<TR>
				<TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../ods/images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
			</TR>
			<TR>
				<TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
			</TR>
			</TBODY></TABLE></td></tr></table>
</BODY>
</HTML>