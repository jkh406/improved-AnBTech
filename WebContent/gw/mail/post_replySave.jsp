<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "���ο��� �ۼ� �����ϱ�"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.text.*"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.email.emailSend"
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


	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	FileWriteString text = new com.anbtech.file.FileWriteString();			//������ ���Ϸ� ���
	textFileReader Rtext = new com.anbtech.file.textFileReader();			//text���� �б�
	emailSend email = new com.anbtech.email.emailSend();					//�ܺθ��� ������

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

	/********************************************************************
		 MultipartRequest ���� ������Ű�� ����.
	*********************************************************************/
	String DIR = bean.getID();	//Directory���� PID (text file and upload file�� ������ ���ϸ����� ���´�. ����Dir�� �ٸ�)
	int maxUploadSize	= 50; 								// ÷�������� �ִ� ũ�⸦ ����(����:Mbyte)
	String saveDir = upload_path+crp+"/post/"+id+"/addfile";// ÷�������� ���� ���丮 ����(Full path�� ����.)
	text.setFilepath(saveDir);		//directory�����ϱ�

	//���ǿ����� ������ ����
	com.oreilly.servlet.MultipartRequest multi;
	multi = new MultipartRequest(request,saveDir,maxUploadSize*1024*1024,"euc-kr");


	/*********************************************************************
	 	������ ���� ���޹޾� ȭ�鿡 ����ϱ� (from post_view.jsp)
	*********************************************************************/
	String mPID = multi.getParameter("PID");
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

	/*********************************************************************
	 	���ϼ��� �����ϱ����� �������� �������� (Emailȸ���� ��츸)
		���ʷ� ��ϵ� ������ ������� �̸�,�ּҸ� ���´�.
	*********************************************************************/	
	if(isEmail.equals("Y")) {
		String[] emailColumn = {"pid","id","sserver","name","address"};
		bean.setTable("emailInfo");			
		bean.setColumns(emailColumn);
		bean.setOrder("pid ASC");	
		bean.setSearch("id",id);			
		bean.init_unique();

		if(bean.isEmpty()) Message="NO_EMAIL";
		else {
			if(bean.isAll()) {								//ó�� �ϳ��� �����´�.
				host = bean.getData("sserver");				//������ ���ϼ�����
				fromName = bean.getData("name");			//�̸�			
				fromAddress = bean.getData("address");		//�ּ�	
			}// if
		} //if
	} //if

	/*********************************************************************
		��޼��û��װ� ����Ʈ�� ����Ȯ�θ� �����
	*********************************************************************/
	String SEL_DATA = SMSG;			

	/*********************************************************************
	 	���� �����ϱ�
	*********************************************************************/
	/*--------------------------------
	// �߼�/�̹߼�[����] ó�� (���� �޴��Է½� ���⼭ �ߴ�)
	---------------------------------*/
	RES = multi.getParameter("res");						//�߼�(SND) �̹߼�(TMP)
	
	/*---------------------------------
	//���� �б�
	----------------------------------*/
	subject = multi.getParameter("SUBJECT");			//���� 

	/*---------------------------------
	//�������� �б�
	----------------------------------*/
	content=multi.getParameter("CONTENT");								//������(unix oracle���� �ѱ��Է�)
	//	�������� ���Ϸ� ����
	String contentDir = upload_path + crp + "/post/" + id + "/text_upload";	//���� Dir  
	text.setFilepath(contentDir);											//directory�����ϱ�
	String text_file = DIR;													//���� ���ϸ� (full path)
	text.WriteHanguel(contentDir,text_file,content);						//���� ���Ϸ� �����ϱ�

	/*-----------------------------------
	 	������ ���κ� �Է¹ޱ�
	------------------------------------*/
	LIST=multi.getParameter("rec_name");

	/*--------------------------------
	//����path,�Է���, ���������� ����
	---------------------------------*/
	String conDir = "/post/" + id + "/text_upload";				//���� path
	String up_date = anbdt.getTime();							//�Է���
	String del_date = anbdt.getAddMonthNoformat(6);				//���������� (6������ : post_letter)
	String del_year = anbdt.getAddYearNoformat(1);				//���������� (1����   : post_master) 

	/*--------------------------------
	// ÷������1 ó��
	---------------------------------*/
	String File_Name1 = multi.getFilesystemName("UP_FILE1");//÷�����ϸ�
	if(File_Name1 == null) File_Name1 = "";

	/*--------------------------------
	// ÷������2 ó��
	---------------------------------*/
	String File_Name2 = multi.getFilesystemName("UP_FILE2");//÷�����ϸ�
	if(File_Name2 == null) File_Name2 = "";

	/*--------------------------------
	// ÷������3 ó��
	---------------------------------*/
	String File_Name3 = multi.getFilesystemName("UP_FILE3");//÷�����ϸ�
	if(File_Name3 == null) File_Name3 = "";

	/*--------------------------------
	// ������ȣ ����
	---------------------------------*/
	String pid = bean.getID();											//������ȣ

	/*----------------------------------
	// �����ڸ� �����ؼ� �����ϱ� (�̸�/���;)
	-----------------------------------*/
	//�ݷ����� ������ ������ �ο��� �ľ�
	int Rcnt = 0;														//������ ���ο��ľ�
	for(int i=0; i < LIST.length(); i++) 
		if(LIST.charAt(i) == ';') Rcnt++;

	//������� �迭�� ���	
	int Scnt = 0;														//ã�� �ݷ� ���Ϲ�ȣ ����
	int Tcnt = 0;
	String[] LIST_ID = new String[LIST.length()];						//�迭�����
	for(int j=0; j < LIST.length(); j++) {
		if(LIST.charAt(j) == ';') {
			String FID = LIST.substring(Scnt,j);						//���κ� ��ü : �̸�/���
			String CID = FID.substring(0,FID.indexOf('/'));				//�����      : ���
			LIST_ID[Tcnt] = CID;										//����� �迭�� ���
			//out.println("ID : " + LIST_ID[Tcnt] + "<br>");
			Scnt = j+3;				//����Ű(2) + ;��(1) = 3�� ���Ѵ�.  
			Tcnt++;
		}
	}

	/*--------------------------------
	// ���ó����� DB�� �����ϱ�
	// (INFROM_BRD)
	---------------------------------*/	
	//	DB������ ���� Ư������ �ٲٱ� (' -> `) 
	subject = str.quoteReplace(subject);		//����

	String inputs = ""; 		//POST_LETTER Table
	String m_inputs = "";		//POST_MASTER Table
	String rpid_del = "";		//from post_main.jsp�� ���������� ���� ���� �����ϰ��
	
	/*******************************************************
	//----------- �系������ ��� �����ϱ� ----------------//
	********************************************************/
	if(RES.equals("SND") && isEmail.equals("N")) {				//����(�系����)
		//�������� �̸� �ٲٱ�1
		String nFile1 = "";													//���������� ���ϸ�
		if(File_Name1.length() != 0) {
			int d = File_Name1.indexOf(".");
			String Hfile = File_Name1.substring(0,d);						//���ϸ�
			String Ext = File_Name1.substring(d+1,File_Name1.length());		//Ȯ���ڸ�

			nFile1 = DIR + "_1." + Ext;										//���ο� ���ϸ�
			String oFilename = saveDir + "/" + File_Name1;					//�������ϸ�(path ����)
			String nFilename = saveDir + "/" + nFile1;						//���ο����ϸ�(path ����)
			Rtext.chgFilename(oFilename,nFilename);
		}

		//�������� �̸� �ٲٱ�
		String nFile2 = "";													//���������� ���ϸ�
		if(File_Name2.length() != 0) {
			int d = File_Name2.indexOf(".");
			String Hfile = File_Name2.substring(0,d);						//���ϸ�
			String Ext = File_Name2.substring(d+1,File_Name2.length());		//Ȯ���ڸ�

			nFile2 = DIR + "_2." + Ext;										//���ο� ���ϸ�
			String oFilename = saveDir + "/" + File_Name2;					//�������ϸ�(path ����)
			String nFilename = saveDir + "/" + nFile2;						//���ο����ϸ�(path ����)
			Rtext.chgFilename(oFilename,nFilename);
		}

		//�������� �̸� �ٲٱ�
		String nFile3 = "";													//���������� ���ϸ�
		if(File_Name3.length() != 0) {
			int d = File_Name3.indexOf(".");
			String Hfile = File_Name3.substring(0,d);						//���ϸ�
			String Ext = File_Name3.substring(d+1,File_Name3.length());		//Ȯ���ڸ�

			nFile3 = DIR + "_3." + Ext;										//���ο� ���ϸ�
			String oFilename = saveDir + "/" + File_Name3;					//�������ϸ�(path ����)
			String nFilename = saveDir + "/" + nFile3;						//���ο����ϸ�(path ����)
			Rtext.chgFilename(oFilename,nFilename);
		}

		bean.setAutoCommit(false);
		bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		try {
			for(int k=0; k < Rcnt; k++) {		//POST_LETTER����
				inputs = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_select,delete_date) values('";
				inputs += pid + "','" + subject + "','" + id + "','" + name + "','" + up_date + "','" + LIST_ID[k] + "','" + "0" + "','" + SEL_DATA + "','" + del_date + "')";
				bean.execute(inputs);
				//out.println("inputs : " + inputs + "<br>");
			}
			// POST_MASTER����
			m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
			m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			m_inputs += pid + "','" + subject + "','" + id + "','" + name + "','" + up_date + "','" + LIST + "','" + "0" + "','";
			m_inputs += RES + "','" + SEL_DATA + "','" + conDir + "','" + text_file + "','" + File_Name1 + "','" + nFile1 + "','";
			m_inputs += File_Name2 + "','" + nFile2 + "','" + File_Name3 + "','" + nFile3 + "','" + del_year + "')";

			bean.execute(m_inputs);							//�����ϱ� (����,������)
			LIST = "";
			if(RES.equals("SND")) Message = "SEND";
			else if(RES.equals("TMP")) Message = "SAVE";

			bean.commit();
		} catch (Exception e) { 
			bean.rollback();

			//������н� ÷������ �����ϱ�
			String Filedir1 = upload_path + crp + "/post/" + id + "/addfile/" + nFile1;
			String Filedir2 = upload_path + crp + "/post/" + id + "/addfile/" + nFile2;
			String Filedir3 = upload_path + crp + "/post/" + id + "/addfile/" + nFile3;
			Rtext.delFilename(Filedir1);	Rtext.delFilename(Filedir2);	Rtext.delFilename(Filedir3);

			//�������� �����ϱ� (Windows)
			String bFileD = upload_path + crp + "/post/" + id + "/text_upload/" + text_file;
			Rtext.delFilename(bFileD);	
		
			//LIST CLEAR
			LIST = "";
			Message = "QUERY";
			
			out.println("�޽��� : " + e);
		} finally{
			bean.setAutoCommit(true);
		}
	}
	/*******************************************************
	//----------- ��ܸ����� ��� �����ϱ� ----------------//
	********************************************************/
	else if(RES.equals("SND") && isEmail.equals("Y")) {	//��ܸ���
		content = content;

		email.setSmtpUrl(host);				//smtp host��
		email.setFrom(fromAddress);			//������ ��� �ּ�
		email.setFromName(fromName);		//������ ��� �̸�
		email.setTo(toAddress);				//������� �ּ�
		email.setSubject(subject);			//����
		email.setContent(content);			//����

		email.setFileName(File_Name1);		//÷�����ϸ�1
		email.setFileName2(File_Name2);		//÷�����ϸ�2
		email.setFileName3(File_Name3);		//÷�����ϸ�3
		email.setPath(saveDir);				//÷������ ���丮

		//�޽��� ���Ϸ� ������
		String Result = email.sendMessage();//�޽��� ������
		
		//�޽��� �����ϱ�
		msg = toAddress + ":" + Result + "; ";

		//ó����� ȭ�鿡 ����ϱ�
		msg = msg.replace('\t',' ');
		msg = msg.replace('\r',' ');
		msg = msg.replace('\n',' ');	
		
		//---------------------------------------
		//������� ���� �����ϱ�
		//---------------------------------------
		if(msg.indexOf("Send OK") > 0) {
				//�������� �̸� �ٲٱ�1
				String nFile1 = "";													//���������� ���ϸ�
				if(File_Name1.length() != 0) {
					int d = File_Name1.indexOf(".");
					String Hfile = File_Name1.substring(0,d);						//���ϸ�
					String Ext = File_Name1.substring(d+1,File_Name1.length());		//Ȯ���ڸ�

					nFile1 = DIR + "_1." + Ext;										//���ο� ���ϸ�
					String oFilename = saveDir + "/" + File_Name1;					//�������ϸ�(path ����)
					String nFilename = saveDir + "/" + nFile1;						//���ο����ϸ�(path ����)
					Rtext.chgFilename(oFilename,nFilename);
				}

				//�������� �̸� �ٲٱ�
				String nFile2 = "";													//���������� ���ϸ�
				if(File_Name2.length() != 0) {
					int d = File_Name2.indexOf(".");
					String Hfile = File_Name2.substring(0,d);						//���ϸ�
					String Ext = File_Name2.substring(d+1,File_Name2.length());		//Ȯ���ڸ�

					nFile2 = DIR + "_2." + Ext;										//���ο� ���ϸ�
					String oFilename = saveDir + "/" + File_Name2;					//�������ϸ�(path ����)
					String nFilename = saveDir + "/" + nFile2;						//���ο����ϸ�(path ����)
					Rtext.chgFilename(oFilename,nFilename);
				}

				//�������� �̸� �ٲٱ�
				String nFile3 = "";													//���������� ���ϸ�
				if(File_Name3.length() != 0) {
					int d = File_Name3.indexOf(".");
					String Hfile = File_Name3.substring(0,d);						//���ϸ�
					String Ext = File_Name3.substring(d+1,File_Name3.length());		//Ȯ���ڸ�

					nFile3 = DIR + "_3." + Ext;										//���ο� ���ϸ�
					String oFilename = saveDir + "/" + File_Name3;					//�������ϸ�(path ����)
					String nFilename = saveDir + "/" + nFile3;						//���ο����ϸ�(path ����)
					Rtext.chgFilename(oFilename,nFilename);
				}


				bean.setAutoCommit(false);
				bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try {
					//1. DB������ ���� Ư������ �ٲٱ� (' -> `) 
					subject = str.quoteReplace(subject);		//����

					//2. POST_MASTER���� �����ϱ�
					m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
					m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
					m_inputs += pid + "','" + subject + "','" + id + "','" + fromName + "','" + up_date + "','" + toAddress + "','" + "0" + "','";
					m_inputs += "SND" + "','" + "" + "','" + conDir + "','" + text_file + "','" + File_Name1 + "','" + nFile1 + "','";
					m_inputs += File_Name2 + "','" + nFile2 + "','" + File_Name3 + "','" + nFile3 + "','" + del_year + "')";
	
					bean.execute(m_inputs);							//�����ϱ� (����,������)
					Message = "ESEND";

					bean.commit();
				} catch (Exception e){
					bean.rollback();

					//������н� ÷������ �����ϱ�
					String Filedir1 = upload_path + crp + "/post/" + id + "/addfile/" + nFile1;
					String Filedir2 = upload_path + crp + "/post/" + id + "/addfile/" + nFile2;
					String Filedir3 = upload_path + crp + "/post/" + id + "/addfile/" + nFile3;
					Rtext.delFilename(Filedir1);Rtext.delFilename(Filedir2);Rtext.delFilename(Filedir3);

					//�������� �����ϱ� (Windows)
					String bFileD = upload_path + crp + "/post/" + id + "/text_upload/" + text_file;
					Rtext.delFilename(bFileD);	   		
					out.println("�޽��� : " + e);
				} finally{
					bean.setAutoCommit(true);
				}

		} else {
				//�������� �����ϱ� (Windows)
				String bFileD = upload_path + crp + "/post/" + id + "/text_upload/" + text_file;
				Rtext.delFilename(bFileD);	
				
				//÷������ ���۽��з� �����ϱ�
				String Filed1 = upload_path + crp + "/post/" + id + "/addfile/" + File_Name1;
				String Filed2 = upload_path + crp + "/post/" + id + "/addfile/" + File_Name2;
				String Filed3 = upload_path + crp + "/post/" + id + "/addfile/" + File_Name3;
				Rtext.delFilename(Filed1);Rtext.delFilename(Filed2);Rtext.delFilename(Filed3);

				Message = "FSEND";
		}// if :������� ���� �����ϱ�	
		
	} //���Ϻ����� (���/�系)

%>
<script>
<!--
var Message = '<%=Message%>';
if(Message == 'SEND') { 
	alert("�߼� �Ǿ����ϴ�."); 
	opener.parent.Left.location.reload();
	opener.document.location.reload();
	self.close(); 
} else if(Message == "NO_EMAIL") {
	alert("�ܺθ��Ͽ� ���� ȯ�漳���� �����ϴ�. \n ���� ȯ�漳������ ����Ͻʽÿ�.");
	self.close();
} else if(Message == "ESEND") {
	alert("<%=msg%>");
	opener.parent.Left.location.reload();
	opener.document.location.reload();
	self.close();
} else if(Message == "FSEND") {
	alert("<%=msg%>");
	opener.parent.Left.location.reload();
	opener.document.location.reload();
	self.close();
}
-->
</script>

