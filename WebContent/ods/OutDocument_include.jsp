<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "��ܰ��� ���ڰ��� Include"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage = "../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dms.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.file.textFileReader"
%>
<%
	//-----------------------------------
	//�ʱ�ȭ ����
	//-----------------------------------
	com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
	com.anbtech.date.anbDate anbdt = new anbDate();							//����
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�
	
	//-----------------------------------
	// ��������
	//-----------------------------------
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
	String read_con="";				//��������
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
	
	//-----------------------------------
	//	��ܰ��� ���� �б�
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_One");
	table = new OfficialDocumentTable();
	Iterator table_iter = table_list.iterator();
	
	if(table_iter.hasNext()){
		table = (OfficialDocumentTable)table_iter.next();

		user_name=table.getUserName();				//����� �̸�
		doc_id=table.getDocId(); 
			if(doc_id == null) doc_id = "��������� �ڵ�ä��";			//������ȣ
		slogan=table.getSlogan();					//���ΰ�
		title_name=table.getTitleName();			//�μ� Title��
		in_date=table.getInDate();					//�������
			in_date = in_date.substring(0,10);
		receive=table.getReceive();;				//����
		reference=table.getReference();				//����
			if(reference == null) reference = "";
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
	read_con = text.getFileString(full_path);

	//÷�������о� �迭�� ���
	if(fname == null) fname = "";
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	addFile = new String[cnt][4];
	for(int i=0; i<cnt; i++) for(int j=0; j<4; j++) addFile[i][j]="";

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
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();
			addFile[m][3] = addFile[m][3].trim() + ".bin";			
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}	
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ods/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">

<!--����-->
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
           <td width="37%" height="25" class="bg_04"><%=doc_id%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=in_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_04"><%=receive%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=user_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=reference%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=subject%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><textarea rows="15" name="content" cols="93" readonly><%=read_con%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">÷������</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
		<%
		for(int i=0; i<cnt; i++) {
			out.println("<a href='../ods/attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a><br>");
		} 
		%>
		   </td></tr>
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
           <td width="37%" height="25" class="bg_04"><%=title_name%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">ǥ��</td>
           <td width="37%" height="25" class="bg_04"><%=slogan%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=firm_name%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��ǥ�ڸ�</td>
           <td width="37%" height="25" class="bg_04"><%=representative%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

</body>
</html>

