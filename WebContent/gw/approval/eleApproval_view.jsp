<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "���ڰ��� ��������"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
%>

<%
	//-----------------------------------
	//	���� ����
	//-----------------------------------
	// �������� ������ �ޱ�
	String doc_pid="";			//������ȣ
	String doc_lin="";			//�������� ���缱
	String send_line = "";		//���缱 ��İ���� �Ѱ��ֱ�
	String doc_sub="";			//�������� ����
	String doc_ste="";			//�������� �� ����ܰ�
	String doc_per="";			//�������� �����Ⱓ
	String doc_sec="";			//�������� �������
	String doc_bon="";			//�������� ��������
	String doc_or1="";			//�������� ÷�� �����̸�1
	String doc_ad1="";			//�������� ÷�� �����̸�1
	String doc_or2="";			//�������� ÷�� �����̸�2
	String doc_ad2="";			//�������� ÷�� �����̸�2
	String doc_or3="";			//�������� ÷�� �����̸�3
	String doc_ad3="";			//�������� ÷�� �����̸�3
	String doc_path="";			//�������� ���� path

	String lid="";				//��Ÿ���� ���ڰ����Ƿ� ������ȣ
	String doc_flag="";			//�������� ����(SERVICE:������, BOM:PartList ��)
	String bon_path = "";		//�������� path
	String file1_path = "";		//÷������1 path
	String file2_path = "";		//÷������2 path
	String file3_path = "";		//÷������3 path
	String file1_size = "";		//÷������1 ũ��
	String file2_size = "";		//÷������2 ũ��
	String file3_size = "";		//÷������3 ũ��

	//���/����/������ �̸�ã��
	String wid = "";			//����ڻ��
	String vid = "";			//�����ڻ��
	String did = "";			//�����ڻ��
	String wname = "";			//�����
	String vname = "";			//������
	String dname = "";			//������
	String vcomm = "";			//������ �ڸ�Ʈ 
	String dcomm = "";			//������ �ڸ�Ʈ 
	String vdate = "";			//������ ���� ����
	String ddate = "";			//������ ���� ���� 
	String PROCESS_NAME = "";	//process�̸�

	//-----------------------------------
	//	���ڰ��� ���� & ��Ÿ ���� �ޱ�
	//-----------------------------------
	String PROCESS = request.getParameter("PROCESS");	//������ ����

	if(PROCESS.equals("APP_ING")) PROCESS_NAME = "�̰���";
	else if(PROCESS.equals("ASK_ING")) PROCESS_NAME = "������";
	else if(PROCESS.equals("APP_BOX")) PROCESS_NAME = "�����";
	else if(PROCESS.equals("APP_GEN")) PROCESS_NAME = "����� (�Ϲݹ���)";
	else if(PROCESS.equals("APP_SER")) PROCESS_NAME = "����� (������)";
	else if(PROCESS.equals("REJ_BOX")) PROCESS_NAME = "�ݷ���";
	else if(PROCESS.equals("TMP_BOX")) PROCESS_NAME = "������";
	else if(PROCESS.equals("SEE_BOX")) PROCESS_NAME = "�뺸��";
	else if(PROCESS.equals("DEL_BOX")) PROCESS_NAME = "������";	
	else if(PROCESS.equals("APP_PNT")) PROCESS_NAME = "����ϱ�";
	else if(PROCESS.equals("APP_LNK")) PROCESS_NAME = "���ù�������";
	else PROCESS_NAME = "����� (����İ���)";

	//app_master���� ������ �б�
	if(PROCESS.equals("APP_ING") || (PROCESS.equals("ASK_ING")) || (PROCESS.equals("REJ_BOX")) || (PROCESS.equals("TMP_BOX")) || PROCESS.equals("APP_PNT")) {
		com.anbtech.gw.entity.TableAppMaster table = new TableAppMaster();	
		ArrayList table_list = new ArrayList();

		table_list = (ArrayList)request.getAttribute("Table_List");
		Iterator table_iter = table_list.iterator();
		while(table_iter.hasNext()) {
			 table = (TableAppMaster)table_iter.next();	
			
			 doc_pid=table.getAmPid();					//������ȣ
			 doc_sub=table.getAmAppSubj();				//����
			 doc_ste=table.getAmAppStatus();			//�� ����ܰ�
			 doc_per=table.getAmSavePeriod();			//�����Ⱓ
			 doc_sec=table.getAmSecurityLevel();		//�������
			 doc_path=table.getAmBonPath();				//���� Path

			 doc_or1=table.getAmAdd1Original();			//÷�� �����̸�1
			 doc_ad1=table.getAmAdd1File();				//÷�� �����̸�1
			 file1_path = upload_path + doc_path + "/addfile/" + doc_ad1;

			 File fn1 = new File(file1_path);
			 file1_size = Long.toString(fn1.length());

			 doc_or2=table.getAmAdd2Original();			//÷�� �����̸�2
			 doc_ad2=table.getAmAdd2File();				//÷�� �����̸�2
			 file2_path = upload_path + doc_path + "/addfile/" + doc_ad2;
			 File fn2 = new File(file2_path);
			 file2_size = Long.toString(fn2.length());

			 doc_or3=table.getAmAdd3Original();			//÷�� �����̸�3
			 doc_ad3=table.getAmAdd3File();				//÷�� �����̸�3
			 file3_path = upload_path + doc_path + "/addfile/" + doc_ad3;
			 File fn3 = new File(file3_path);
			 file3_size = Long.toString(fn3.length());

			 lid=table.getAmPlid();						//��Ÿ���� ������ȣ
			 doc_flag=table.getAmFlag();				//������������
			 wid = table.getAmWriter();					//����ڻ��
			 vid = table.getAmReviewer();				//�����ڻ��
			 did = table.getAmDecision();				//�����ڻ��

			 //�������� �б�
			 com.anbtech.file.textFileReader text = new textFileReader();
			 bon_path = upload_path + doc_path + "/bonmun/" + table.getAmBonFile();
			 doc_bon = text.getFileString(bon_path);	//�������� �������� 
		} //while
	} 
	//app_save���� ������ �б�
	else {
		com.anbtech.gw.entity.TableAppSave tableS = new TableAppSave();	
		ArrayList table_list = new ArrayList();

		table_list = (ArrayList)request.getAttribute("Table_List");
		Iterator table_iter = table_list.iterator();
		while(table_iter.hasNext()) {
			 tableS = (TableAppSave)table_iter.next();	

			 doc_pid=tableS.getAmPid();					//������ȣ
			 doc_sub=tableS.getAmAppSubj();				//����
			 doc_ste=tableS.getAmAppStatus();			//�� ����ܰ�
			 doc_per=tableS.getAmSavePeriod();			//�����Ⱓ
			 doc_sec=tableS.getAmSecurityLevel();		//�������
			 doc_path=tableS.getAmBonPath();			//���� Path
			 doc_or1=tableS.getAmAdd1Original();		//÷�� �����̸�1
			 doc_ad1=tableS.getAmAdd1File();			//÷�� �����̸�1
			 file1_path = upload_path + doc_path + "/addfile/" + doc_ad1;
			 File fn1 = new File(file1_path);
			 file1_size = Long.toString(fn1.length());

			 doc_or2=tableS.getAmAdd2Original();		//÷�� �����̸�2
			 doc_ad2=tableS.getAmAdd2File();			//÷�� �����̸�2
			 file2_path = upload_path + doc_path + "/addfile/" + doc_ad2;
			 File fn2 = new File(file2_path);
			 file2_size = Long.toString(fn2.length());

			 doc_or3=tableS.getAmAdd3Original();		//÷�� �����̸�3
			 doc_ad3=tableS.getAmAdd3File();			//÷�� �����̸�3
			 file3_path = upload_path + doc_path + "/addfile/" + doc_ad3;
			 File fn3 = new File(file3_path);
			 file3_size = Long.toString(fn3.length());

			 lid=tableS.getAmPlid();					//��Ÿ���� ������ȣ
			 doc_flag=tableS.getAmFlag();				//������������
			 wid = tableS.getAmWriter();				//����ڻ��
			 vid = tableS.getAmReviewer();				//�����ڻ��
			 did = tableS.getAmDecision();				//�����ڻ��

			 //�������� �б�
			 com.anbtech.file.textFileReader text = new textFileReader();
			 bon_path = upload_path + doc_path + "/bonmun/" + tableS.getAmBonFile();
			 doc_bon = text.getFileString(bon_path);	//�������� �������� 
		} //while

	} //if

	//������ ���� �б�
	com.anbtech.gw.entity.TableAppLine line = new TableAppLine();			
	ArrayList table_line = new ArrayList();

	table_line = (ArrayList)request.getAttribute("Table_Line");
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		line = (TableAppLine)line_iter.next();
									
		if(line.getApStatus().equals("���"))  wname = line.getApName();	//�����
		if(line.getApStatus().equals("����"))  vname = line.getApName();	//������
		if(line.getApStatus().equals("����"))  dname = line.getApName();	//������
		if(line.getApStatus().equals("����")) {
			 vcomm = line.getApComment();	//�������ڸ�Ʈ 
			 vdate = line.getApDate();		//������ �������� (������ �����ϰ� ������ ����ʵ�)
		}
		if(line.getApStatus().equals("����")) {
			 dcomm = line.getApComment();	//�������ڸ�Ʈ 
			 ddate = line.getApDate();		//������ �������� (������ �����ϰ� ������ ����ʵ�)\
		}
 			
		doc_lin += line.getApStatus()+" "+line.getApSabun()+" "+line.getApName()+" "+line.getApRank()+" "+line.getApDivision()+" "+line.getApDate()+" "+line.getApComment()+"\r";
		
		send_line += line.getApStatus()+" "+line.getApSabun()+" "+line.getApName()+" "+line.getApRank()+" "+line.getApDivision()+" "+line.getApDate()+" "+line.getApComment()+"@";
	}
	
	//-----------------------------------
	//	������� ���ڰ��� ���뺸�� (�б��ϱ�)
	//-----------------------------------
	if(doc_flag.equals("GEN") || doc_flag.equals("SERVICE")) lid = doc_pid;		//�Ϲݹ��� �� ������

	//�����ͷ� ����ϱ�
	if(PROCESS.equals("APP_PNT")) {
		response.sendRedirect("../gw/approval/eleApproval_PrintLink.jsp?doc_id="+lid+"&flag="+doc_flag+"&line="+send_line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+doc_pid);
	}
	//�������ڰ��繮�� ����
	else if(PROCESS.equals("APP_LNK")) {
		response.sendRedirect("../gw/approval/eleApproval_OtherLink.jsp?doc_id="+lid+"&flag="+doc_flag+"&line="+send_line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+doc_pid);
	}
	//�ش系�� ���� : ��� ó������ ����
	else {
		response.sendRedirect("../gw/approval/eleApproval_ViewLink.jsp?doc_id="+lid+"&flag="+doc_flag+"&line="+send_line+"&vdate="+vdate+"&ddate="+ddate+"&wid="+wid+"&vid="+vid+"&did="+did+"&wname="+wname+"&vname="+vname+"&dname="+dname+"&PROCESS="+PROCESS+"&doc_ste="+doc_ste+"&pid="+doc_pid);
	}

%>
