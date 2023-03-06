<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "���ڸ��� �ۼ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="javax.mail.*"
	import="javax.mail.internet.*"
	import="com.oreilly.servlet.MultipartRequest"
	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.file.FileWriteString"
%>
<%@	page import="com.anbtech.text.Hanguel"				%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<%@	page import="com.anbtech.email.emailSend"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean"	/>

<%
	String Message = "M";
	String id	= "";		//������ id
	String[] pid;			//������ȣ
	String[] smtp;			//smtp��
	String[] name;			//�����»�� �̸�
	String[] address;		//�����»�� �ּ�

	int rid = 0;			//���Ϲ��� ���ڰ�(stmp���ý�)
	String msg = "S";		//���� �޽��� ���

	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	FileWriteString text = new com.anbtech.file.FileWriteString();			//������ ���Ϸ� ���
	textFileReader Rtext = new com.anbtech.file.textFileReader();			//text���� �б�
	emailSend email = new com.anbtech.email.emailSend();					//�Ẹ����

	/*********************************************************************
	 	������ login �˾ƺ���
	*********************************************************************/
	id = sl.id; 		//������ login id

	//���� �ʱ�ȭ
	Message = "M"; msg = "S";
	/********************************************************************
		 MultipartRequest ���� ������Ű�� ����.
	*********************************************************************/
	int maxUploadSize	= 10; 								// ÷�������� �ִ� ũ�⸦ ����(����:Mbyte)
	String saveDir = upload_path + crp + "/post/" + id + "/addfile";	// ÷�������� ���� ���丮 ����
	text.setFilepath(saveDir);		//directory�����ϱ�

	//���ǿ����� ������ ����
	com.oreilly.servlet.MultipartRequest multi;
	multi = new MultipartRequest(request,saveDir,maxUploadSize*1024*1024,"euc-kr");

	/*********************************************************************
	 	������ ���ϼ����� �о����
	*********************************************************************/
	String[] idColumn = {"pid","id","name","address","sserver"};
	bean.setTable("emailInfo");			
	bean.setColumns(idColumn);
	bean.setOrder("pid ASC");	
	bean.setSearch("id",id);			
	bean.init_unique();
	
	int cnt = bean.getTotalCount();
	if(cnt == 0) cnt = 1;

	pid		= new String[cnt];						//������ȣ
	smtp	= new  String[cnt];						//smtp��
	name	= new String[cnt];						//�����»�� �̸�
	address = new String[cnt];						//�����»�� �ּ�

	if(bean.isEmpty()) {
		pid[0] = "";
		smtp[0] = "";
		name[0] = "";
		address[0] = "";
		Message = "NODATA";
	} else {
		int i = 0;
		while(bean.isAll()) {
			pid[i]		= bean.getData("pid");				
			smtp[i]		= bean.getData("sserver");		
			name[i]		= bean.getData("name");	
			address[i]	= bean.getData("address");
			i++;
		} //while
	}

	/********************************************************************
		���ڸ��� ������
	*********************************************************************/
	String SEND = "";
	SEND = multi.getParameter("SEND");
	if(SEND != null){
		msg = "";
		String toAddress = multi.getParameter("strTo");							//�޴»�� �ּ�
		if(toAddress == null) toAddress = "";
		String fromAddress = multi.getParameter("strFrom");						//�����»�� �ּ�
		if(fromAddress == null) fromAddress = "";
		String fromName = multi.getParameter("strName");						//�����»�� �̸�
		if(fromName == null) fromName = "";
		String subject = multi.getParameter("strSubject");						//����   
		if(subject == null) subject = "";
		String content = multi.getParameter("strContent");						//����
		if(content == null) content = "";

		String File_Name1 = multi.getFilesystemName("file");					//÷�����ϸ�1
		if(File_Name1 == null) File_Name1 = "";

		String File_Name2 = multi.getFilesystemName("file2");					//÷�����ϸ�2
		if(File_Name2 == null) File_Name2 = "";

		String File_Name3 = multi.getFilesystemName("file3");					//÷�����ϸ�3
		if(File_Name3 == null) File_Name3 = "";

		String host = smtp[rid];												//������ smtp��

		//������� �ּ� �����Ͽ� ������
		toAddress = toAddress.replace(',',  ' ');
		toAddress = toAddress.replace(';',  ' ');
		toAddress = toAddress.replace('\t', ' ');
		toAddress = toAddress.replace('\r', ' ');
		toAddress = toAddress.replace('\n', ' ');

		//---------------------------------------
		//�����ϱ� 
		//---------------------------------------
		StringTokenizer To = new StringTokenizer(toAddress," ");
		while(To.hasMoreTokens()){
			String toAdd = To.nextToken().trim();
			toAdd = str.repWord(toAdd,";","");

			email.setSmtpUrl(host);				//smtp host��
			email.setFrom(fromAddress);			//������ ��� �ּ�
			email.setFromName(fromName);		//������ ��� �̸�
			email.setTo(toAdd);					//������� �ּ�
			email.setSubject(subject);			//����
			email.setContent(content);			//����

			email.setFileName(File_Name1);		//÷�����ϸ�1
			email.setFileName2(File_Name2);		//÷�����ϸ�2
			email.setFileName3(File_Name3);		//÷�����ϸ�3
			email.setPath(saveDir);				//÷������ ���丮


			//�޽��� ���Ϸ� ������
			String Result = email.sendMessage();//�޽��� ������

			//�޽��� �����ϱ�
			msg += toAdd + ":" + Result + " \n ";
		}

		//---------------------------------------
		//�������� �����ϱ�
		//---------------------------------------
		if(msg.indexOf("Send OK") > 0) {
				//1. �������Ϸ� �����ϱ�
				content=multi.getParameter("strSubject");
				String DIR = bean.getID();

				//	�������� ���Ϸ� ����
				String contentDir = upload_path + crp + "/post/" + id + "/text_upload";	//���� Dir  
				text.setFilepath(contentDir);										//directory�����ϱ�
				String text_file = DIR;												//���� ���ϸ� (full path)
				text.WriteHanguel(contentDir,text_file,content);					//���� ���Ϸ� �����ϱ�
				

				//2. ����path,�Է���, ���������� ����
				String conDir = "/post/" + id + "/text_upload";				//���� path
				String up_date = anbdt.getTime();							//�Է���
				String del_date = anbdt.getAddMonthNoformat(6);		//���������� (6������ : post_letter)
				String del_year = anbdt.getAddYearNoformat(1);		//���������� (1����   : post_master) 

				//3. ÷������ �̸��ٲ� �����ϱ�
				String nFile1 = "";									//���������� ���ϸ�1
				if(File_Name1.length() != 0) {
					int d = File_Name1.indexOf(".");
					String Hfile = File_Name1.substring(0,d);						//���ϸ�
					String Ext = File_Name1.substring(d+1,File_Name1.length());		//Ȯ���ڸ�

					nFile1 = DIR + "_1." + Ext;										//���ο� ���ϸ�
					String oFilename = saveDir + "/" + File_Name1;					//�������ϸ�(path ����)
					String nFilename = saveDir + "/" + nFile1;						//���ο����ϸ�(path ����)
					Rtext.chgFilename(oFilename,nFilename);
				}

				String nFile2 = "";									//���������� ���ϸ�2
				if(File_Name2.length() != 0) {
					int d = File_Name2.indexOf(".");
					String Hfile = File_Name2.substring(0,d);						//���ϸ�
					String Ext = File_Name2.substring(d+1,File_Name2.length());		//Ȯ���ڸ�

					nFile2 = DIR + "_2." + Ext;										//���ο� ���ϸ�
					String oFilename = saveDir + "/" + File_Name2;					//�������ϸ�(path ����)
					String nFilename = saveDir + "/" + nFile2;					//���ο����ϸ�(path ����)
					Rtext.chgFilename(oFilename,nFilename);
				}

				String nFile3 = "";									//���������� ���ϸ�3
				if(File_Name3.length() != 0) {
					int d = File_Name3.indexOf(".");
					String Hfile = File_Name3.substring(0,d);						//���ϸ�
					String Ext = File_Name3.substring(d+1,File_Name3.length());		//Ȯ���ڸ�

					nFile3 = DIR + "_3." + Ext;										//���ο� ���ϸ�
					String oFilename = saveDir + "/" + File_Name3;					//�������ϸ�(path ����)
					String nFilename = saveDir + "/" + nFile3;					//���ο����ϸ�(path ����)
					Rtext.chgFilename(oFilename,nFilename);
				}

				//4. ������ȣ ����
				String spid = bean.getID();											//������ȣ

				//5. DB������ ���� Ư������ �ٲٱ� (' -> `) 
				subject = str.quoteReplace(subject);		//����

				//6. POST_MASTER���� �����ϱ�
				String m_inputs = "";		//POST_MASTER Table
				m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
				m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
				m_inputs += spid + "','" + subject + "','" + id + "','" + fromName + "','" + up_date + "','" + toAddress + "','" + "0" + "','";
				m_inputs += "SND" + "','" + "" + "','" + conDir + "','" + text_file + "','" + File_Name1 + "','" + nFile1 + "','";
				m_inputs += File_Name2 + "','" + nFile2 + "','" + File_Name3 + "','" + nFile3 + "','" + del_year + "')";

				try { 	
					bean.execute(m_inputs);							//�����ϱ� (����,������)
				} 
				catch (Exception e) { 
					//������н� ÷������ �����ϱ�
					String Filedir1 = upload_path + crp + "/post/" + id + "/addfile/" + nFile1;
					String Filedir2 = upload_path + crp + "/post/" + id + "/addfile/" + nFile2;
					String Filedir3 = upload_path + crp + "/post/" + id + "/addfile/" + nFile3;
					Rtext.delFilename(Filedir1);Rtext.delFilename(Filedir2);Rtext.delFilename(Filedir3);

					//�������� �����ϱ� (Windows)
					String bFileD = upload_path + crp + "/post/" + id + "/text_upload/" + text_file;
					Rtext.delFilename(bFileD);
				} //try

		} // if :�������� �����ϱ�

		//ó����� ȭ�鿡 ����ϱ�
		msg = msg.replace('\t',' ');
		msg = msg.replace('\r',' ');
		msg = msg.replace('\n',' ');
		Message = "SEND";

	} //if

%>

<script>
<!--
//���ڸ��� ���� ó����� 
if("<%=Message%>" == "SEND"){ alert("<%=msg%>"); self.close(); }
//������ ������ �޽��� �����ϱ�
if("<%=Message%>" == "NODATA"){ alert("ȯ�漳���� ����� �����Ͻʽÿ�."); self.close(); }
-->
</script>
