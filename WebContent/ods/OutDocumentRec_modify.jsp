<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "��ܰ��� ��������"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage	= "../admin/errorpage.jsp"
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
	String serial_no="";			//������ȣ
	String doc_id="";				//������ȣ
	String in_date="";				//��������	
	String send_date="";			//�߽�����(��ü�� ����)	
	String receive="";				//����
	String sending="";				//�߽�
	String sheet_cnt="";			//�μ�
	String subject="";				//����	
	String bon_path="";				//�������� Ȯ��path
	String bon_file="";				//�������� ���ϸ�
	String content="";				//��������
	
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
		serial_no=table.getSerialNo();				//������ȣ
		doc_id=table.getDocId();					
			if(doc_id == null) doc_id = "";			//������ȣ
		
		in_date=table.getInDate();					//��������	
		send_date=table.getSendDate();				//�߽�����(��ü�� ����)
		receive=table.getReceive();;				//����
		sending=table.getSending();					//�߽�
		sheet_cnt=table.getSheetCnt();				//�߽� �μ�
		subject=table.getSubject();					//����	
		bon_path=table.getBonPath();				//�������� Ȯ��path
		bon_file=table.getBonFile();				//�������� ���ϸ�
		
		fname=table.getFname();	if(fname==null)fname="";					//����:���Ͽ�����	
		sname=table.getSname();	if(sname==null)sname="";					//����:���������		
		ftype=table.getFtype();	if(ftype==null)ftype="";					//����:����Ȯ���ڸ�	
		fsize=table.getFsize();	if(fsize==null)fsize="";					//����:����ũ��
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
//���źμ��� ã��
function searchChief()
{	//window.open("../ods/searchDivision.jsp?target=eForm.receive","division","width=490,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");
	wopen('../ods/searchDivision.jsp?target=eForm.receive',"",'510','467');
}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//�����ϱ�
function sendModify()
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
	document.eForm.mode.value='ODR_modify';	
	document.eForm.submit();
}
//÷������ ���������ϱ�
function attachDel(a)
{
	document.eForm.action='../servlet/OutDocumentRecMultiServlet';
	document.eForm.mode.value='ODR_attachD';	
	document.eForm.ext.value=a;
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
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������ȣ</td>
           <td width="40%" height="25" class="bg_04"><%=serial_no%></td>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="40%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="40%" height="25" class="bg_04">
				<input class="box" size='25' type='text' name='sending' value='<%=sending%>'></td>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�߼�����</td>
           <td width="40%" height="25" class="bg_04">
				<input class="box" size='25' type='text' name='send_date' value='<%=send_date%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������ȣ</td>
           <td width="40%" height="25" class="bg_04">
				<input size='25' type='text' name='doc_id' value='<%=doc_id%>' class='text_01'></td>
			 <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="40%" height="25" class="bg_04">
				<input class="box" size='25' type='text' name='sheet_cnt' value='<%=sheet_cnt%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="40%" height="25" class="bg_04">
				<textarea rows="3" name="receive" cols='22' readOnly style="background:#FCFCDF;border:1 solid #787878;"><%=receive%></textarea>&nbsp;<a href="Javascript:searchChief();"><img src="../ods/images/bt_search2.gif" border='0'></a>
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
				<input size='60' type='text' name='subject' class='text_01' value='<%=subject%>'></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��</td>
           <td width="90%" height="25" class="bg_04" colspan="3">
				<textarea rows="3" name="content" cols="93"><%=content%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">÷������</td>
           <td width="90%" height="25" class="bg_04" colspan="3">
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
						out.println("<input type=file name=attachfile"+no+" size=60><br>");
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
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�������</td>
           <td width="90%" height="25" class="bg_04">
				<%
					String btag="",mtag="";
					if(module_name.equals("�Խ���")) btag = "checked";
					if(module_name.equals("���źμ�")) mtag = "checked";

					out.println("<input type='radio' "+mtag+" name='module_name' value='���źμ�'>���źμ�");
					out.println("<input type='radio' "+btag+" name='module_name' value='�Խ���'>�Խ���");	
				%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='id' value='<%=id%>'>
<input type='hidden' name='serial_no' value='<%=serial_no%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>

<input type='hidden' name='ext' value=''>
<input type='hidden' name='bon_path' value='<%=bon_path%>'>
<input type='hidden' name='fname' value='<%=fname%>'>
<input type='hidden' name='sname' value='<%=sname%>'>
<input type='hidden' name='ftype' value='<%=ftype%>'>
<input type='hidden' name='fsize' value='<%=fsize%>'>
</form>

<DIV id="lding" style="position:absolute;left:300px;top:150px;width:224px;height:150px;visibility:hidden;">
	<img src='../ods/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</body>
</html>
