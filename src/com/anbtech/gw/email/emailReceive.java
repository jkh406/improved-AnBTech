package com.anbtech.gw.email;
import javax.mail.*;
import java.util.*;
import java.io.*;
import javax.mail.internet.*;
import java.sql.*;
import java.text.*;
import javax.activation.*;


public class emailReceive extends Object
{	
	//���ӽ� �ʿ��� ȯ�� ����	
	private String host;			//�޴� ���� ������ (��: rmail1.hanmir.com)
	private String sendpt;			//�޴� ���� ����protocol���� (pop3 �Ǵ� imap)
	private String username;		//����� ID
	private String password;		//����� ��й�ȣ 
	private String isMod;			//�б�Type (�б⸸:RO, �а� �������ϼ������� ����:RW) 
	
	//�����ϱ� 
	private Store store;			//mail server�� ���� 
	private Folder folder;			//������ �����ϱ� 
	private Message message;		//message�����ϱ� 
	
	//email�� �����ϱ� ���� ���� ����
	private String pid;				//������ȣ 
	private String id;				//�ۼ��� ��� 
	private String name;			//�ۼ��� �̸� 
	private String bonfn;			//������ �������ϸ� 
	private String addpath;			//÷������ ������ path
	private String textpath;		//�������� ������ path					

	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public emailReceive() 
	{	
		
	}	
	
	/***************************************************************************
	 * host�� (�޴� ���� ���� ����)
	 **************************************************************************/
	public void setHost(String host) 
	{	
		this.host = host;
	}	
	
	/***************************************************************************
	 * protocal type (pop3  or imap)
	 **************************************************************************/
	public void setProtocol(String pt) 
	{	
		this.sendpt = pt;	
	}	

	/***************************************************************************
	 * user name (�޴� ���ϼ����� ��ϵ�)
	 **************************************************************************/
	public void setUserName(String username) 
	{	
		this.username = username;
	}	

	/***************************************************************************
	 * password (�޴� ���ϼ����� ��ϵ�)
	 **************************************************************************/
	public void setPassWord(String password) 
	{	
		this.password = password;
	}	

	/***************************************************************************
	 * ���� ���ϼ����� �޽��� �������� (RO : Read_Only, RW : Read_Write)
	 **************************************************************************/
	public void isDelete(String isDel) 
	{	
		this.isMod = isDel;
	}	

	/***************************************************************************
	 * ���ڸ��� ���� ��ȣ 
	 **************************************************************************/
	public void setPID(String pid) 
	{	
		this.pid = pid;
	}	

	/***************************************************************************
	 * ���ڸ��� ����� ��� (login id) 
	 **************************************************************************/
	public void setID(String id) 
	{	
		this.id = id;
	}	

	/***************************************************************************
	 * ���ڸ��� ����� �̸� 
	 **************************************************************************/
	public void setName(String name) 
	{	
		this.name = name;
	}	

	/***************************************************************************
	 * ���ڸ��� ���������� ���ϸ� 
	 **************************************************************************/
	public void setSaveFile(String bonfn) 
	{	
		this.bonfn = bonfn;
	}	

	/***************************************************************************
	 * ���ڸ��� ÷�����ϸ� ������ path
	 **************************************************************************/
	public void setAddPath(String addpath) 
	{	
		this.addpath = addpath;
	}	

	/***************************************************************************
	 * ���ڸ��� �������� ������ path
	 **************************************************************************/
	public void setTextPath(String textpath) 
	{	
		this.textpath = textpath;
	}	

	/***************************************************************************
	 * ���ϼ����� �����ϱ� 
	 **************************************************************************/
	public String getConnect() throws Exception 
	{	
		String mailhost = ""; 			//mail.pop3.host  or mail.imap.host
		
		//System properties
		Properties props = System.getProperties();
		mailhost = "mail." + this.sendpt + ".host";
		props.put(mailhost,this.host);
		
		//���� ���ϱ� 
		Session session = Session.getDefaultInstance(props,null);
		
		//���ϼ����� �����ϱ�
		store = session.getStore(this.sendpt);
		store.connect(this.host,this.username,this.password);
		
		//������ �����ϱ� 
		folder = store.getDefaultFolder();
		if(folder == null) return "disconnection";
		else {
			folder = folder.getFolder("INBOX");
			if(this.isMod.equals("RO"))    			//�б� ���� (���ϼ����� �޽����� �״�� ����)
				folder.open(Folder.READ_ONLY);
			else folder.open(Folder.READ_WRITE);	//�а�(RW) ���ϼ��� �޽��� �����ϱ� 			
			return "Good";	
		}
				
	}
	
	/***************************************************************************
	 * �޽��� �Ѽ��� �ľ��ϱ� 
	 **************************************************************************/
	 public int getMailCount() throws Exception
	 {	
		return folder.getMessageCount();	
	}
	
	/***************************************************************************
	 * ���� �������� (������ ������ ��ȣ�� ��������)
	 **************************************************************************/
	 public Message getMail(int intNumber) throws Exception
	 {	
	 	message = folder.getMessage(intNumber);
		return message;	
	}
			
	/***************************************************************************
	 * ���� �������� (�ѹ��� ���� ���� ����)
	 **************************************************************************/
/*	 public Message[] getMail() throws Exception
	 {	
		//�޽��� ��������
		return folder.getMessages();	
	}
*/	
	/***************************************************************************
	 * ���� ������ ���� DELETED��ũ �ϱ�
	 **************************************************************************/
	 public void markDeleted() throws Exception
	 {	
		message.setFlag(Flags.Flag.DELETED,true);
	}
	
	/***************************************************************************
	 * ���� �з��ϱ� (��ü Test��)
	 **************************************************************************/
	public String getDetail() throws Exception
	{ 	
		String from = "";				//���� ��� �ּ� 
		String sent_date = "";			//���� ���� 
		String subject="";				//���� ���� 
		String content="";				//���� ���� 
		String FileName="";				//÷������ (original��)
	
	
		//�޽��� ���� �ľ��ϱ� 
		int mcnt = getMailCount();
		
		//ó���� �޽����� ����  
		if(mcnt == 0) { 			
			close();	//�����ϱ� 
			return "ó���� ������ �����ϴ�";	
		} else { //�޽��� ó�� �ϱ� 									
			for(int i = 1; i <= mcnt; i++) {
				//�޽��� �������� 
				Message message = getMail(i);
				
				//1.��������ּ� �˾ƺ��� 
				from = "";
				from = message.getFrom()[0].toString();
				
				String fname = encordingName(from);		//���ڵ��� ���ڵ��ϱ�
				if(fname.equals("NOENC")) 
					from = new String(from.getBytes("8859_1"),"euc-kr");
				else from = fname; 	
				from = from.replace('"',' ');		//�̸����� " ���ֱ� 
				 
				//2.�������� �˾ƺ��� 
				java.util.Date now = message.getSentDate();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd  HH:mm");
				sent_date = vans.format(now);
				
				//3.���� �˾ƺ���
				String title = message.getSubject();
				subject = encordingSubject(title);
				
				//4.������ �������� �������ϸ� (�ߺ��� ���ϱ� ���� ������)
				String bon_filename = this.bonfn + i + ".html";
				
				//5.������ ÷������ �������ϸ� (�ߺ��� ���ϱ� ���� ������)
				String add_filename = this.bonfn + i;
				
				//Test
				Object o = message.getContent();
				if(o instanceof String) { 
					//System.out.println("msg : String");
					//System.out.println((String)o);
				} else if( o instanceof Multipart) {
					//System.out.println("msg : multipart");
				} else if( o instanceof InputStream) {
					//System.out.println("msg : InputStream");
					//System.out.println((String)o);
				}
				
				
				//6-1.÷������ �� ���� �˾ƺ��� 
				if(message.isMimeType("multipart/*")) {			//÷������ ó�� �ϱ� 
					Multipart multipart = (Multipart)message.getContent();
					int m = multipart.getCount();					//÷������ ���� 
					for(int j = 0; j < m; j++) {					
						Part part = multipart.getBodyPart(j);
						String disposition = part.getDisposition();
																	
						//���� ���� ( j=0 : null --> ���� ����)
						if(disposition == null) {
							//6-1-1.���� ���Ϸ� ���� (Stream���� ó�� �Ͽ� �ѱ� ���� ����)
							InputStream inFile = part.getInputStream();   //������ stream���·� ���� 
							//saveTextInputStream(bon_filename,inFile);	
						} 
						
						//÷������ �б� (j=1 : attachement ÷������1 , - - , j=3 : attachement ÷������3)
						if(disposition != null && (disposition.equals(part.ATTACHMENT) || disposition.equals(part.INLINE))) {

							//6-1-2.÷�����ϸ� 
							String FN = part.getFileName();	
							FileName = "";	
							if(FN != null) FileName = new String(FN.getBytes("8859_1"),"EUC_KR");
							
							//÷������ Ȯ���ڸ� ���ϱ� 
							String extFileName = "";
							if(FileName.length() > 0) {
								extFileName = FileName.substring(FileName.indexOf("."),FileName.length());
								//System.out.println("ext : " + extFileName);	
							}
								
							//6-1-3.÷������ �����ϱ� 
							BufferedInputStream in = new BufferedInputStream(part.getInputStream());
							//saveAddInputStream(add_filename,extFileName,j,in);						
							
						} //if
					} //for
				//6-2.÷������ ���� 
				} else {											
					FileName = "";
					//6-2-1.���� ���Ϸ� ���� (Stream���� ó�� �Ͽ� �ѱ� ���� ����) 
					InputStream inFile = message.getInputStream();
					//saveTextInputStream(bon_filename,inFile);					
				}
				
				//���� ��� �ϱ� 
				//System.out.println("������� = " + from);
//				System.out.println("�������� = " + sent_date);
				//System.out.println("Mail���� = " + subject + "\n");
//				System.out.println("Mail���� = " + content);
//				System.out.println("÷�����ϸ� = " + FileName);

				//�����ϱ����� DELETED��ũ �ϱ�
				markDeleted();
				
			} //for	
			
			
			//���� �ϱ� 
			if(folder.isOpen()) {
				close();
				//System.out.println("folder is deleted");
			}else {
				//System.out.println("folder is closed");
			}
			
			return "ó���� �Ϸ�Ǿ����ϴ�.";		
		} //if
		
		 
	}
	
	/***************************************************************************
	 * ������� �̸� ���ϱ� : ���ڵ� ��Ŀ� ���� ���ڵ� �ϱ� 
	 * Name : ���ڵ��� �̸� ��>?EUC-KR?Q?DF 
	 * return : ���ڵ� ó���� ��� �̸� (���ڵ��� �̸��� �ƴϸ� NOENC �� ������)
	 **************************************************************************/
	 public String encordingName(String name) throws Exception
	 {	
		//���1. ���ڵ��� ���ڵ��ϱ� (��ü�� ���ڵ��� ���·� �о����)
		if((name.indexOf("?B?") > 0) || (name.indexOf("?Q?") > 0)) {
			name = MimeUtility.decodeText(name);
			return name;
		} else return "NOENC";			
	}
	/***************************************************************************
	 * ���� ���ϱ� : ���ڵ� ��Ŀ� ���� �ѱ۱��� ó��  
	 * title : ���� ���� 
	 * �������� �ѱ��� �ѱ����ڵ��� endcodeText�� �ȵǳ� �׷����������� encodeText�� 
	 **************************************************************************/	
	public String encordingSubject(String title) throws Exception
	 {
	 	//�ѱ� ���ڵ��� �����Ѵ�.
		String dec = new String(title.getBytes("8859_1"),"EUC_KR");
		
		//���ڵ��� �����Ѵ�.
		dec = MimeUtility.encodeText(dec,"Cp970","Q");
		
		//���ڵ��Ȱ��(=?Cp970?Q?)�� �ѱ� ���ڵ��ϰ� �׷��� ������ �״�� �����Ѵ�.
		if(dec.indexOf("Cp970") > 0) {
			title = new String(title.getBytes("8859_1"),"EUC_KR");
			return title;
		}else return title;	
	 	
	 }

	/***************************************************************************
	 * �������� �����ϱ� (InputStream) : Streamó���� �ѱ۱����� ���� 
	 * (message.getInputStream() or message.getContent() �� ���������� ����)
	 **************************************************************************/
	 public void saveTextInputStream(String bonfile,java.io.InputStream inFile) throws Exception
	 {
	 	//������ ���� ���ϸ� 
	 	String bon_filename = bonfile;
	 	
		//������ Path ���� 
		String Textpath = this.textpath;
		File dir = new File(Textpath);
		if(!dir.exists()) dir.mkdirs();
		

		//���� ���Ϸ� �����ϱ� 	
		FileOutputStream outFile = new FileOutputStream(Textpath + "/" + bon_filename);

		int d;
		while((d=inFile.read()) != -1)
			outFile.write(d);
			
		//close	
		outFile.flush();
		outFile.close();	     	 
	 	
	 }	 
	 
	 public void saveTextInputStream(String bonfile,java.io.InputStream inFile,String cont) throws Exception
	 {
	 	//������ ���� ���ϸ� 
	 	String bon_filename = bonfile;
	 	
		//������ Path ���� 
		String Textpath = this.textpath;
		File dir = new File(Textpath);
		if(!dir.exists()) dir.mkdirs();
		
		//encoding ���� �Ǵ��Ͽ� ���� ���Ϸ� �����ϱ� 	 
		if((cont.indexOf("?B?") > 0) || (cont.indexOf("?Q?") > 0) || (cont.indexOf("Content-Transfer-Encoding: quoted-printable") > 0)) {
			cont = MimeUtility.decodeText(cont);
			File f = new File(Textpath + "/" + bon_filename);
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(f));
			output.writeObject(cont);
			output.flush();
			output.close();
		} else {
			FileOutputStream outFile = new FileOutputStream(Textpath + "/" + bon_filename);
			int d;
			while((d=inFile.read()) != -1)
				outFile.write(d);
			outFile.flush();
			outFile.close();
		}	     	 
	 }	 
	/***************************************************************************
	 * ÷������ �����ϱ� (InputStream) : Streamó���� �ѱ۱����� ���� 
	 * (message.getInputStream() or message.getContent() �� ���������� ����)
	 * 		������ ���ϸ�   : addfile		(200302051034001 ��)
	 * 		Ȯ���ڸ� 	    : extFileName 	(.hwp ��)
	 * 		÷������ ����   : j			(1,2, ��)
	 *  	��Ʈ������ �ޱ� : inFile
	 **************************************************************************/
	 public void saveAddInputStream(String addfile, String extFileName, int j, java.io.BufferedInputStream inFile) throws Exception
	 {
	 	//������ ÷�� ���ϸ� (�ٸ��̸����� ������)
	 	String add_filename = addfile + "_" + j + extFileName;
	 	
		//������ Path ���� 
		String Addpath = this.addpath;
		File dir = new File(Addpath);
		if(!dir.exists()) dir.mkdirs();
		

		//÷�� ���Ϸ� �����ϱ� 	
		FileOutputStream outFile = new FileOutputStream(Addpath + "/" + add_filename);

		int c;
		while((c=inFile.read()) != -1)
			outFile.write(c);
			
		//close	
		outFile.flush();
		outFile.close();	     	 
	 	
	 }	 
	 
	/***************************************************************************
	 * ������ ���� DELETED��ũ �ϰ� �����ϱ� (�ѹ��� �����ϱ�)
	 **************************************************************************/	 
	 public void removeMessage() throws MessagingException 
	 {
	 	int totalMessage = 0;			//message�� �Ѱ��� 
	 	String mailhost = ""; 			//mail.pop3.host  or mail.imap.host
		mailhost = "mail." + this.sendpt + ".host";
		
	 	try {			
			Properties props = System.getProperties();
			props.put(mailhost,this.host);
			Session session = Session.getDefaultInstance(props,null);
			store = session.getStore(this.sendpt);
			store.connect(this.host,this.username,this.password);
			folder = store.getFolder("INBOX");
			
						
			//message���� �ľ��ϱ� 									
			if(folder == null || !folder.exists()) {
				System.exit(1);
			} else {
				folder.open(Folder.READ_WRITE);				//�а�(RW) ���� 
				totalMessage =  folder.getMessageCount();	//�޽��� ���� 
			}	
			//System.out.println("T cnt : " + totalMessage);
			
			//���� DELETED��ũ �ϱ� 
			for(int i = 1; i <= totalMessage; i++){
				//���ϼ��� DELETED��ũ�ϱ�(1��)  		
        		Message message = folder.getMessage(i);//�ش��ȣ DELETED��ũ �ϱ� 
	    		message.setFlag(Flags.Flag.DELETED, true);	
			} //for
					
			//���� ���� �����ϱ� 
			folder.close(true);			//�����ϱ� 
		} catch (Exception e) {
			System.out.println("Error : " + e.toString());
		} 
	}
  
	/***************************************************************************
	 * close
	 **************************************************************************/
	public void close() throws Exception
	{
			if(this.isMod.equals("RO")) {
				folder.close(false);
				store.close();
			} else {
				folder.close(true);
				store.close();		
			}		
	}

}