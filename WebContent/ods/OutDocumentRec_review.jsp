<%@ include file="../admin/configPopUp.jsp"%>
<%@ page		
	info= "��ܰ��� ��������"		
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

<HTML><HEAD><TITLE>��ܰ��� ��������</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../ods/images/o_send_info.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--��������-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height="20" colspan="4"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������ȣ</td>
           <td width="30%" height="25" class="bg_04"><%=serial_no%></td>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">��������</td>
           <td width="30%" height="25" class="bg_04"><%=in_date%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�߼�ó</td>
           <td width="30%" height="25" class="bg_04"><%=sending%></td>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�߼�����</td>
           <td width="30%" height="25" class="bg_04"><%=send_date%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������ȣ</td>
           <td width="30%" height="25" class="bg_04"><%=doc_id%></td>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�μ�</td>
           <td width="30%" height="25" class="bg_04"><%=sheet_cnt%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����ó</td>
           <td width="80%" height="25" colspan="3" class="bg_04">
				<%
				if(receive.length() > 3) {
					StringTokenizer rec = new StringTokenizer(receive,";");
					String recs = "";
					while(rec.hasMoreTokens()){
						String rec_data = rec.nextToken();
						if(rec_data.length() > 3)
							recs += rec_data.substring(rec_data.indexOf("/")+1,rec_data.length())+",";
					}
					out.println(recs.substring(0,recs.length()-1));
				}
				%>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">����</td>
           <td width="80%" height="25" colspan="3" class="bg_04"><%=subject%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">�ǰ�</td>
           <td width="80%" height="25" colspan="3" class="bg_04"><%=content%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">÷������</td>
           <td width="80%" height="25" colspan="3" class="bg_04">
			<% 
				int ary_cnt = addFile.length;	//�迭�� ����
				for(int i=0; i<ary_cnt; i++) {
					out.println("&nbsp;<a href='../ods/attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
					out.println("<br>");
				}
			%>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">������</td>
           <td width="80%" height="25" colspan="3" class="bg_04"><%=user_name%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr><td height="20" colspan="4"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='Javascript:self.close();'><img src='../ods/images/bt_close.gif' align='absmiddle' border='0'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>