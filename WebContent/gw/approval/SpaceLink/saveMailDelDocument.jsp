<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page		
	info= "�������� ���ۼ� �����ϱ�"		
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

	//����ó�� ����
	String LIST="";			//�����θ��
	String RES="";			//�߼�/�̹߼� �ޱ�
	String subject="";		//��������
	String content="";		//����
	String selopt = "";		//�ɼǻ���
	String bPath = "";		//�������� path
	String bFile = "";		//�������ϸ�

	String apath = "";		//÷������ Path
	String pad1o = "";		//÷�ε� ���ϸ�1 �����̸�	
	String pad1f = "";		//÷�ε� ���ϸ�1
	String pad2o = "";		//÷�ε� ���ϸ�2 �����̸�	
	String pad2f = "";		//÷�ε� ���ϸ�2
	String pad3o = "";		//÷�ε� ���ϸ�3 �����̸�	
	String pad3f = "";		//÷�ε� ���ϸ�3	

	String SMSG = "";		//��޼��û��� �ٽ������ϱ�

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

	/********************************************************************
		 MultipartRequest ���� ������Ű�� ����.
	*********************************************************************/
	String DIR = bean.getID();	//Directory���� PID (text file and upload file�� ������ ���ϸ����� ���´�. ����Dir�� �ٸ�)
	int maxUploadSize	= 10; 								// ÷�������� �ִ� ũ�⸦ ����(����:Mbyte)
	String saveDir = upload_path+crp+"/email/"+id+"/addfile";// ÷�������� ���� ���丮 ����(Full path�� ����.)
	text.setFilepath(saveDir);		//directory�����ϱ�

	//���ǿ����� ������ ����
	com.oreilly.servlet.MultipartRequest multi;
	multi = new MultipartRequest(request,saveDir,maxUploadSize*1024*1024,"euc-kr");

	/*********************************************************************
	// �ӽ����� ó��Ű ���� �غ� 
	// [�ӽ����峻�� �����ϰ� �ٽ� ����/�������ϱ�,	��, ÷�������� ������ �����ϱ� ]
	*********************************************************************/
	String rpid ="";		//���޹��� pid�� ����/�����۽� �ش系�� ����Ű ����
	String path = "";		//÷������ path
	String Bpath = "";		//�������� path
	String pfie = "";		//���� ���ϸ� (window)

	rpid = multi.getParameter("pid");		//from post_write.jsp
	if(rpid == null) rpid = "";				//�ű� �����϶�
	if(rpid.equals("null")) rpid = "";

	if(rpid.length() != 0) {				
		String[] pidColumn = {"pid","post_select","bon_path","bon_file","add_1_original","add_1_file","add_2_original","add_2_file","add_3_original","add_3_file"};
		
		bean.setTable("POST_MASTER");
		bean.setColumns(pidColumn);
		bean.setOrder("pid ASC");	
		bean.setSearch("pid",rpid);	
		bean.init();

		if(bean.isEmpty()) Message="NO_DATA";
		else if (bean.isAll()) {	
			selopt = bean.getData("post_select");		//��޼��û���
			String Path = bean.getData("bon_path");		//����path
			if(Path == null) path = crp + "/";
			else {
				path = crp + Path.substring(0,Path.lastIndexOf('/'))+"/addfile";	//÷������ path
				Bpath = crp + Path;													//�������� path
			}

			pfie = bean.getData("bon_file");			//���� ���ϸ� (window)
			if(pfie == null) pfie = "";

			pad1o = bean.getData("add_1_original");		//÷�ε� ���ϸ�1 �����̸�
			pad1f = bean.getData("add_1_file");			//÷�ε� ���ϸ�1

			pad2o = bean.getData("add_2_original");		//÷�ε� ���ϸ�2 �����̸�
			pad2f = bean.getData("add_2_file");			//÷�ε� ���ϸ�2

			pad3o = bean.getData("add_3_original");		//÷�ε� ���ϸ�3 �����̸�
			pad3f = bean.getData("add_3_file");			//÷�ε� ���ϸ�3			
		} //while
	}


	/*********************************************************************
		��޼��û��װ��б� (from post_select.jsp)
	*********************************************************************/
	String cfm = multi.getParameter("ReturnReceipt");
	String sec = multi.getParameter("SecretSetup");
	String rsp = multi.getParameter("ReplySetup");

	String SEL_DATA = "";			//�����ϱ�
	if(cfm != null) SEL_DATA += cfm + ","; 
	if(sec != null) SEL_DATA += sec + ","; 
	if(rsp != null) SEL_DATA += rsp + ","; 

	//from post_main.jsp�� ���������� �� ������
	if(SEL_DATA.length() == 0) {
		if(SMSG.length() > 0) SEL_DATA = SMSG;
	} 

	//�ӽ��������� ���� �о����� ��޼����� ������ ���ŵ����ͷ� ����
	if((SEL_DATA.length() == 3) && (rpid.length() != 0)) SEL_DATA = selopt;

	/*********************************************************************
	 	���� �����ϱ�
	*********************************************************************/
	//--------------------------------
	// �߼�/�̹߼�[����] ó�� (���� �޴��Է½� ���⼭ �ߴ�)
	//---------------------------------
	RES = multi.getParameter("res");						//�߼�(SND) �̹߼�(TMP)

	//---------------------------------
	//���� �б�
	//----------------------------------
	subject = multi.getParameter("SUBJECT");			//���� 

	//---------------------------------
	//�������� �б�
	//----------------------------------
	content=multi.getParameter("CONTENT");								//������(unix oracle���� �ѱ��Է�)
	//	�������� ���Ϸ� ����
	String contentDir = upload_path + crp + "/email/" + id + "/text";	//���� Dir  
	text.setFilepath(contentDir);											//directory�����ϱ�
	String text_file = DIR;													//���� ���ϸ� (full path)
	text.WriteHanguel(contentDir,text_file,content);						//���� ���Ϸ� �����ϱ�

	//-----------------------------------
	// 	������ ���κ� �Է¹ޱ�
	//------------------------------------
	LIST=multi.getParameter("rec_name");
	if(LIST == null) LIST = "";

	//--------------------------------
	//����path,�Է���, ���������� ����
	//---------------------------------
	String conDir = "/email/" + id + "/text";				//���� path
	String up_date = anbdt.getTime();							//�Է���
	String del_date = anbdt.getAddMonthNoformat(6);				//���������� (6������ : post_letter)
	String del_year = anbdt.getAddYearNoformat(1);				//���������� (1����   : post_master) 

	//--------------------------------
	// ÷������1 ó��
	//---------------------------------/
	String File_Name1 = multi.getFilesystemName("UP_FILE1");//÷�����ϸ�
	if(File_Name1 == null) File_Name1 = "";

	//�������� �̸� �ٲٱ�
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

	//--------------------------------
	// ÷������2 ó��
	//---------------------------------/
	String File_Name2 = multi.getFilesystemName("UP_FILE2");//÷�����ϸ�
	if(File_Name2 == null) File_Name2 = "";

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

	//--------------------------------
	// ÷������3 ó��
	//---------------------------------/
	String File_Name3 = multi.getFilesystemName("UP_FILE3");//÷�����ϸ�
	if(File_Name3 == null) File_Name3 = "";

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

	//--------------------------------
	// ������ȣ ����
	//---------------------------------/
	String pid = bean.getID();											//������ȣ

	//----------------------------------
	// �����ڸ� �����ؼ� �����ϱ� (�̸�/���;)
	//-----------------------------------/
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

	//--------------------------------
	// ���ó����� DB�� �����ϱ�
	// (INFROM_BRD)
	//---------------------------------/	
	//	DB������ ���� Ư������ �ٲٱ� (' -> `) 
	subject = str.quoteReplace(subject);		//����

	String inputs = ""; 		//POST_LETTER Table
	String m_inputs = "";		//POST_MASTER Table
	String rpid_del = "";		//from post_main.jsp�� ���������� ���� ���� �����ϰ��
	
	if(RES.equals("SND")) {					//����
		for(int k=0; k < Rcnt; k++) {		//POST_LETTER����
			inputs = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_select,delete_date) values('";
			inputs += pid + "','" + subject + "','" + id + "','" + name + "','" + up_date + "','" + LIST_ID[k] + "','" + "0" + "','" + SEL_DATA + "','" + del_date + "')";
			try { bean.execute(inputs); } catch (Exception e) { out.println("������ ���� : " + e); } 
			//out.println("inputs : " + inputs + "<br>");
		}
		// POST_MASTER����
		m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
		m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
		m_inputs += pid + "','" + subject + "','" + id + "','" + name + "','" + up_date + "','" + LIST + "','" + "0" + "','";
		m_inputs += "email" + "','" + SEL_DATA + "','" + conDir + "','" + text_file + "','" + File_Name1 + "','" + nFile1 + "','";
		m_inputs += File_Name2 + "','" + nFile2 + "','" + File_Name3 + "','" + nFile3 + "','" + del_year + "')";
		//���������� ���� ���� ������ ��� �ش系��(POST_MASTER) �����ϱ�
		if(rpid.length() > 5) {
			rpid_del = "delete from POST_MASTER where pid='" + rpid + "'";
		}
	} 
	//out.println("m_inputs : " + m_inputs + "<br>");
	
	//-----------------------------------------------------
	//  �����׸� ���� �����ϱ�
	///------------------------------------------------------
	try { 	
		bean.execute(m_inputs);							//�����ϱ� (����,������)
		if(rpid.length() > 0) {
			bean.execute(rpid_del);	//�����ۿ��� �����۽�
		}
	} 
	catch (Exception e) { 
		//������н� ÷������ �����ϱ�
		String Filedir1 = upload_path + crp + "/email/" + id + "/addfile/" + nFile1;
		String Filedir2 = upload_path + crp + "/email/" + id + "/addfile/" + nFile2;
		String Filedir3 = upload_path + crp + "/email/" + id + "/addfile/" + nFile3;
		Rtext.delFilename(Filedir1);	Rtext.delFilename(Filedir2);	Rtext.delFilename(Filedir3);	
		
		//�������� �����ϱ� (Windows)
		String bFileD = upload_path + crp + "/email/" + id + "/text/" + text_file;
		Rtext.delFilename(bFileD);	
	}
%>

<!-- ****************** �޽��� ���޺κ� ****************************** -->
<script>
self.close()
</script>

