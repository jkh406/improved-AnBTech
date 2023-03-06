package com.anbtech.email;
import java.util.Properties;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Service;

public class emailSend
{
	private String strFileName 		= "";		//÷������ ��1
	private String strFileName2 	= "";		//÷������ ��2
    private String strFileName3 	= "";		//÷������ ��3
	private	String strPath 			= "";		//÷������ �ִ� ���丮1
    
    private String strFileFullName 	= "";		//���丮 + ���ϸ� ���� 
    private String strFileFullName2 = "";		//���丮 + ���ϸ� ����2 
    private String strFileFullName3 = "";		//���丮 + ���ϸ� ����3 
          
	private String strSmtpUrl 		= "";		//Host name
	private String addressFrom 		= "";		//������ ��� �ּ� 
	private String nameFrom 		= "";		//������ ��� �̸� 
	private String addressTo 		= "";		//�޴»�� 
	private String strSubject 		= "";		//���� 
	private String strContent 		= "";		//���� 

	/**************************************************************
	/*		������ 
	**************************************************************/		
	public emailSend()
	{

	}	

	/**************************************************************
	/*		������ ���ϼ��� ���� �˻� �ϱ� 
	**************************************************************/
	public String SetupMailServer(String smtpHost,String addr,String sub,String smg) throws Exception
	{	
		try {
			
			Properties props = System.getProperties();						
			props.put("mail.smtp.host",smtpHost);				
			Session session = Session.getDefaultInstance(props,null);

	      	MimeMessage message = new MimeMessage(session);
			InternetAddress address = new InternetAddress(addr);
      		message.setRecipient(Message.RecipientType.TO, address);
      		message.setFrom     (new InternetAddress("admin@anbtech.co.kr"));
      		message.setSubject  (sub, "EUC-KR");	
      		message.setContent(smg, "text/plain; charset=EUC-KR");
      		Transport.send(message);	
			
			return "Good";
		} catch (Exception e) { return "Error.... smtpHost Name." + e;}		
	}	

	/**************************************************************
	/*		message ������  : �ؽ�Ʈ
	**************************************************************/
  	public String sendMessage() throws MessagingException {
    	String blnSend = "";
    	
    	try {
      		Properties p = new Properties();
      		p.put("mail.smtp.host", this.strSmtpUrl.toString());

      		Session session = Session.getDefaultInstance(p, null);

      		MimeMessage message = new MimeMessage(session);
			InternetAddress address = new InternetAddress(this.addressTo.toString());
      		message.setRecipient(Message.RecipientType.TO, address);
      		message.setFrom (new InternetAddress(this.addressFrom,this.nameFrom));
      		message.setSubject (this.strSubject,"EUC-KR");
      		
      		//�ѱ۱����� �� �Ẹ�� 
//     		message.setSubject(MimeUtility.encodeText(strSubject,"KSC5601","B"));
 

			//�ܼ� ���� (�������븸 �ۼ�)
      		if (this.strFileName.equals("") && this.strFileName2.equals("") && this.strFileName3.equals("") ) {
        		message.setContent(this.strContent,"text/plain; charset=EUC-KR");
      		} else {
      		//÷������ ���� 
        		Multipart multipart = new MimeMultipart();
        					
				//���� ���� �ۼ�  
        		MimeBodyPart mbpContent = new MimeBodyPart();
        		mbpContent.setContent(this.strContent,"text/plain; charset=EUC-KR");
				multipart.addBodyPart(mbpContent);
				
				//÷������1 (����Ʈ�� ������)
				this.strFileFullName = this.strPath + "/" + this.strFileName;
				this.strFileName = new String(this.strFileName.getBytes("EUC_KR"), "Cp1252");//���ϸ� �ѱۺ��� 
				
        		MimeBodyPart mbpAttach = new MimeBodyPart();
        		FileDataSource fileDs = new FileDataSource(this.strFileFullName);
        		mbpAttach.setDataHandler(new DataHandler(fileDs));
        		mbpAttach.setFileName(this.strFileName);
        		multipart.addBodyPart(mbpAttach);
        		
				//÷������2 
				if(!this.strFileName2.equals("")) {
					this.strFileFullName2 = this.strPath + "/" + this.strFileName2;
					this.strFileName2 = new String(this.strFileName2.getBytes("EUC_KR"), "Cp1252");//���ϸ� �ѱۺ��� 
				
        			MimeBodyPart mbpAttach2 = new MimeBodyPart();
        			FileDataSource fileDs2 = new FileDataSource(this.strFileFullName2);
        			mbpAttach2.setDataHandler(new DataHandler(fileDs2));
        			mbpAttach2.setFileName(this.strFileName2);
        			multipart.addBodyPart(mbpAttach2); 
        		}  
        		
        		//÷������3
				if(!this.strFileName3.equals("")) {
					this.strFileFullName3 = this.strPath + "/" + this.strFileName3;
					this.strFileName3 = new String(this.strFileName3.getBytes("EUC_KR"), "Cp1252");//���ϸ� �ѱۺ��� 
				
        			MimeBodyPart mbpAttach3 = new MimeBodyPart();
        			FileDataSource fileDs3 = new FileDataSource(this.strFileFullName3);
        			mbpAttach3.setDataHandler(new DataHandler(fileDs3));
        			mbpAttach3.setFileName(this.strFileName3);
        			multipart.addBodyPart(mbpAttach3); 
        		}       		

				//������ ÷�������� ���� 
		        message.setContent(multipart);    
		        
		    }
			//�޽��� ������ 
      		Transport.send(message);
      		blnSend = "Send OK";
    	} catch (Exception e) {
      		blnSend = e.toString();
    	}

    	return blnSend;
  	}

	/**************************************************************
	/*		message ������  : html
	**************************************************************/
  	public String sendMessageHtml() throws MessagingException {
    	String blnSend = "";
    	
    	try {
      		Properties p = new Properties();
      		p.put("mail.smtp.host", this.strSmtpUrl.toString());

      		Session session = Session.getDefaultInstance(p, null);

      		MimeMessage message = new MimeMessage(session);
			InternetAddress address = new InternetAddress(this.addressTo.toString());
      		message.setRecipient(Message.RecipientType.TO, address);
      		message.setFrom (new InternetAddress(this.addressFrom,this.nameFrom));
      		message.setSubject (this.strSubject,"EUC-KR");
      		
      		//�ѱ۱����� �� �Ẹ�� 
//     		message.setSubject(MimeUtility.encodeText(strSubject,"KSC5601","B"));
 

			//�ܼ� ���� (�������븸 �ۼ�)
      		if (this.strFileName.equals("") && this.strFileName2.equals("") && this.strFileName3.equals("") ) {
        		message.setContent(this.strContent,"text/html; charset=EUC-KR");
      		} else {
      		//÷������ ���� 
        		Multipart multipart = new MimeMultipart();
        					
				//���� ���� �ۼ�  
        		MimeBodyPart mbpContent = new MimeBodyPart();
        		mbpContent.setContent(this.strContent,"text/html; charset=EUC-KR");
				multipart.addBodyPart(mbpContent);
				
				//÷������1 (����Ʈ�� ������)
				this.strFileFullName = this.strPath + "/" + this.strFileName;
				this.strFileName = new String(this.strFileName.getBytes("EUC_KR"), "Cp1252");//���ϸ� �ѱۺ��� 
				
        		MimeBodyPart mbpAttach = new MimeBodyPart();
        		FileDataSource fileDs = new FileDataSource(this.strFileFullName);
        		mbpAttach.setDataHandler(new DataHandler(fileDs));
        		mbpAttach.setFileName(this.strFileName);
        		multipart.addBodyPart(mbpAttach);
        		
				//÷������2 
				if(!this.strFileName2.equals("")) {
					this.strFileFullName2 = this.strPath + "/" + this.strFileName2;
					this.strFileName2 = new String(this.strFileName2.getBytes("EUC_KR"), "Cp1252");//���ϸ� �ѱۺ��� 
				
        			MimeBodyPart mbpAttach2 = new MimeBodyPart();
        			FileDataSource fileDs2 = new FileDataSource(this.strFileFullName2);
        			mbpAttach2.setDataHandler(new DataHandler(fileDs2));
        			mbpAttach2.setFileName(this.strFileName2);
        			multipart.addBodyPart(mbpAttach2); 
        		}  
        		
        		//÷������3
				if(!this.strFileName3.equals("")) {
					this.strFileFullName3 = this.strPath + "/" + this.strFileName3;
					this.strFileName3 = new String(this.strFileName3.getBytes("EUC_KR"), "Cp1252");//���ϸ� �ѱۺ��� 
				
        			MimeBodyPart mbpAttach3 = new MimeBodyPart();
        			FileDataSource fileDs3 = new FileDataSource(this.strFileFullName3);
        			mbpAttach3.setDataHandler(new DataHandler(fileDs3));
        			mbpAttach3.setFileName(this.strFileName3);
        			multipart.addBodyPart(mbpAttach3); 
        		}       		

				//������ ÷�������� ���� 
		        message.setContent(multipart);    
		        
		    }
			//�޽��� ������ 
      		Transport.send(message);
      		blnSend = "Send OK";
    	} catch (Exception e) {
      		blnSend = e.toString();
    	}

    	return blnSend;
  	}
	
 	/**************************************************************
	/*		Host name setting
	**************************************************************/ 	
  	public String setSmtpUrl(String url)
  	{
  		this.strSmtpUrl = "";
  		this.strSmtpUrl = url;
  		return this.strSmtpUrl;
  	}	
  	
	/**************************************************************
	/*		������ ��� address
	**************************************************************/ 	
  	public void setFrom(String from)
  	{
  		this.addressFrom = "";
  		this.addressFrom = from;
  	}
  		
	/**************************************************************
	/*		������ ��� �̸� 
	**************************************************************/ 	
  	public void setFromName(String fromName)
  	{
  		this.nameFrom = "";
  		this.nameFrom = fromName;
  	}	
  	    	  	
	/**************************************************************
	/*		�޴»��  address
	**************************************************************/ 	
  	public void setTo(String to)
  	{
  		this.addressTo = "";
  		this.addressTo = to;
  	}	
    	
	/**************************************************************
	/*		���� 
	**************************************************************/ 	
  	public void setSubject(String subject)
  	{
  		this.strSubject = "";
  		this.strSubject = subject;
  	}	
   
	/**************************************************************
	/*		���� 
	**************************************************************/ 	
  	public void setContent (String content)
  	{
  		this.strContent = "";
  		this.strContent = content;
  	}	
 
	/**************************************************************
	/*		÷������ ��1
	**************************************************************/ 	
  	public void setFileName (String filename)
  	{
  		this.strFileName = "";
  		this.strFileName = filename;
  	}	
 	/**************************************************************
	/*		÷������ ��2
	**************************************************************/ 	
  	public void setFileName2 (String filename)
  	{
  		this.strFileName2 = "";
  		this.strFileName2 = filename;
  	}
	/**************************************************************
	/*		÷������ ��3
	**************************************************************/ 	
  	public void setFileName3 (String filename)
  	{
  		this.strFileName3 = "";
  		this.strFileName3 = filename;
  	}   	
	/**************************************************************
	/*		÷������ �ִ� ���丮 �� 
	**************************************************************/ 	
  	public void setPath (String path)
  	{
  		this.strPath = "";
  		this.strPath = path;
  	}		


}