<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "�������� ����"		
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
	String subject="";				//����	
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
	String mail="";					//���ڿ�������
	String mail_add="";				//���ڿ����ּ�(���/�̸�;)
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
		subject=table.getSubject();					//����	
		bon_path=table.getBonPath();				//�������� Ȯ��path
		bon_file=table.getBonFile();				//�������� ���ϸ�
		firm_name=table.getFirmName();				//�߽źμ���
		representative=table.getRepresentative();	//�߽źμ� ��ǥ��	
		fname=table.getFname();						//����:���Ͽ�����	
		sname=table.getSname();						//����:���������		
		ftype=table.getFtype();						//����:����Ȯ���ڸ�	
		fsize=table.getFsize();						//����:����ũ��
		module_name=table.getModuleName();			//�����������
		mail=table.getMail();						//���ڿ�������
		mail_add=table.getMailAdd();				//���ڿ����ּ�(���/�̸�;)
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
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","b.code"};
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
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ods/css/style.css" type="text/css">
<script language=javascript>
<!--
//���ڿ�������� ã��
function searchProxy(users)
{
//	window.open("../ods/searchReceiver.jsp?target=eForm.mail_add|"+users,"proxy","width=510,height=460,scrollbar=yes,toolbar=no,status=no,resizable=no");
	wopen("../ods/searchReceiver.jsp?target=eForm.mail_add|"+users,"receiver",'510','467','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//�����ϱ�
function sendModify()
{
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
	document.eForm.mode.value='OFD_modify';	
	document.eForm.submit();
}
//÷������ ���������ϱ�
function attachDel(a)
{
	if(confirm("���� �Ͻðڽ��ϱ�?")) {
	document.eForm.action='../servlet/OfficialDocumentMultiServlet';
	document.eForm.mode.value='OFD_attachD';	
	document.eForm.ext.value=a;
	document.eForm.submit();
	} else {
		return;
	}
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>
</HEAD>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ods/images/blet.gif"> ������������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href="javascript:sendModify();"><img src="../ods/images/bt_save.gif" border="0"></a> <a href="javascript:history.go(-1)"><img src="../ods/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
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
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='receive' value='<%=receive%>' class='text_01'></td>
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
           <td width="87%" height="25" class="bg_04" colspan="3"><input  size='25' type='text' name='reference' value='<%=reference%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input  size='60' type='text' name='subject' value='<%=subject%>' class='text_01'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><textarea rows="8" name="content" cols="93"  class='text_01'><%=content%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">÷������</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
			<% 
				int ary_cnt = addFile.length;	//�迭�� ����
				String same = "0";				//÷�������� ����(_1,_2,_3..)�� �����ʴ�.
				int s_no = 0;					//�������� �迭��ȣ
				for(int i=0,no=1; no<attache_cnt; i++,no++) {
					for(int j=0; j<ary_cnt; j++) {
						int uno = Integer.parseInt(addFile[j][4]);
						if(no == uno) {
							same = "1";			//����.
							s_no = j;
						}
					}
					if(same.equals("0")) { //÷������ Ȯ���(_1,_2..)�� �������� ������
						out.println("&nbsp;<input type=file name=attachfile"+no+" size=60><br>");
					} else {				//�������� ������
						out.println("&nbsp;<a href='../ods/attach_download.jsp?fname="+addFile[s_no][0]+"&ftype="+addFile[s_no][1]+"&fsize="+addFile[s_no][2]+"&sname="+addFile[s_no][3]+"&extend="+bon_path+"'>"+addFile[s_no][0]+"</a>");
						out.println("<a href=javascript:attachDel('"+addFile[s_no][4]+"')>[����]<a><br>"); 
					}
					same = "0";					//clear
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
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='title_name' value='<%=title_name%>' class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">ǥ��</td>
           <td width="37%" height="25" class="bg_04"><input  size='35' type='text' name='slogan' value='<%=slogan%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='firm_name' value='<%=firm_name%>' class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�μ����</td>
           <td width="37%" height="25" class="bg_04"><input  size='10' type='text' name='representative' value='<%=representative%>' class='text_01'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04">
		   <%
					String btag="",mtag="";
					if(module_name.equals("�Խ���")) btag = "checked";
					if(mail.equals("���ڿ���")) mtag = "checked";
				
					out.println("<input type='checkbox' "+btag+" name='module_name' value='�Խ���'>�Խ���");	
					out.println("<input type='checkbox' "+mtag+" name='mail' value='���ڿ���'>���ڿ���");	
			%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><textarea rows="1" name="mail_add" cols='30' readOnly style="background:#FFFFFF;border:1 solid #787878;"><%=mail_add%></textarea>&nbsp;<a href="Javascript:searchProxy('<%=mail_add%>');"><img src='../ods/images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='id' value='<%=id%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='sending' value='<%=div_name%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>

<input type='hidden' name='ext' value=''>
<input type='hidden' name='bon_path' value='<%=bon_path%>'>
<input type='hidden' name='fname' value='<%=fname%>'>
<input type='hidden' name='sname' value='<%=sname%>'>
<input type='hidden' name='ftype' value='<%=ftype%>'>
<input type='hidden' name='fsize' value='<%=fsize%>'>
</form>

</body>
</html>

