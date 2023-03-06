package com.anbtech.gw.email;
import java.util.Properties;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Service;

public class emailSendTest
{
	private String strFileName 		= "";		//첨부파일 명1
	private String strFileName2 	= "";		//첨부파일 명2
    private String strFileName3 	= "";		//첨부파일 명3
	private	String strPath 			= "";		//첨부파일 있는 디렉토리1
    
    private String strFileFullName 	= "";		//디렉토리 + 파일명 조합 
    private String strFileFullName2 = "";		//디렉토리 + 파일명 조합2 
    private String strFileFullName3 = "";		//디렉토리 + 파일명 조합3 
          
	private String strSmtpUrl 		= "";		//Host name
	private String addressFrom 		= "";		//보내는 사람 주소 
	private String nameFrom 		= "";		//보내는 사람 이름 
	private String addressTo 		= "";		//받는사람 
	private String strSubject 		= "";		//제목 
	private String strContent 		= "";		//내용 

	/**************************************************************
	/*		생성자 
	**************************************************************/		
	public emailSendTest()
	{

	}	

	/**************************************************************
	/*		보내는 메일서버 계정 검사 하기 
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
	/*		message 보내기 (단순메시지)
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
      		
      		//한글깨지면 함 써보길 
//     		message.setSubject(MimeUtility.encodeText(strSubject,"KSC5601","B"));


			//단순 메일 (본문내용만 작성)
      		if (this.strFileName.equals("") && this.strFileName2.equals("") && this.strFileName3.equals("") ) {
        		message.setContent(this.strContent,"text/plain; charset=EUC-KR");
      		} else {
      		//첨부파일 메일 
        		Multipart multipart = new MimeMultipart();
        					
				//본문 내용 작성  
        		MimeBodyPart mbpContent = new MimeBodyPart();
        		mbpContent.setContent(this.strContent,"text/plain; charset=EUC-KR");
				multipart.addBodyPart(mbpContent);
				
				//첨부파일1 (디폴트로 보내기)
				this.strFileFullName = this.strPath + "/" + this.strFileName;
				this.strFileName = new String(this.strFileName.getBytes("EUC_KR"), "Cp1252");//파일명 한글보상 
				
        		MimeBodyPart mbpAttach = new MimeBodyPart();
        		FileDataSource fileDs = new FileDataSource(this.strFileFullName);
        		mbpAttach.setDataHandler(new DataHandler(fileDs));
        		mbpAttach.setFileName(this.strFileName);
        		multipart.addBodyPart(mbpAttach);
        		
				//첨부파일2 
				if(!this.strFileName2.equals("")) {
					this.strFileFullName2 = this.strPath + "/" + this.strFileName2;
					this.strFileName2 = new String(this.strFileName2.getBytes("EUC_KR"), "Cp1252");//파일명 한글보상 
				
        			MimeBodyPart mbpAttach2 = new MimeBodyPart();
        			FileDataSource fileDs2 = new FileDataSource(this.strFileFullName2);
        			mbpAttach2.setDataHandler(new DataHandler(fileDs2));
        			mbpAttach2.setFileName(this.strFileName2);
        			multipart.addBodyPart(mbpAttach2); 
        		}  
        		
        		//첨부파일3
				if(!this.strFileName3.equals("")) {
					this.strFileFullName3 = this.strPath + "/" + this.strFileName3;
					this.strFileName3 = new String(this.strFileName3.getBytes("EUC_KR"), "Cp1252");//파일명 한글보상 
				
        			MimeBodyPart mbpAttach3 = new MimeBodyPart();
        			FileDataSource fileDs3 = new FileDataSource(this.strFileFullName3);
        			mbpAttach3.setDataHandler(new DataHandler(fileDs3));
        			mbpAttach3.setFileName(this.strFileName3);
        			multipart.addBodyPart(mbpAttach3); 
        		}       		

				//본문과 첨부파일을 묶기 
		        message.setContent(multipart);    
		        
		    }
			//메시지 보내기 
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
	/*		보내는 사람 address
	**************************************************************/ 	
  	public void setFrom(String from)
  	{
  		this.addressFrom = "";
  		this.addressFrom = from;
  	}
  		
	/**************************************************************
	/*		보내는 사람 이름 
	**************************************************************/ 	
  	public void setFromName(String fromName)
  	{
  		this.nameFrom = "";
  		this.nameFrom = fromName;
  	}	
  	    	  	
	/**************************************************************
	/*		받는사람  address
	**************************************************************/ 	
  	public void setTo(String to)
  	{
  		this.addressTo = "";
  		this.addressTo = to;
  	}	
    	
	/**************************************************************
	/*		제목 
	**************************************************************/ 	
  	public void setSubject(String subject)
  	{
  		this.strSubject = "";
  		this.strSubject = subject;
  	}	
   
	/**************************************************************
	/*		내용 
	**************************************************************/ 	
  	public void setContent (String content)
  	{
  		this.strContent = "";
  		this.strContent = content;
  	}	
 
	/**************************************************************
	/*		첨부파일 명1
	**************************************************************/ 	
  	public void setFileName (String filename)
  	{
  		this.strFileName = "";
  		this.strFileName = filename;
  	}	
 	/**************************************************************
	/*		첨부파일 명2
	**************************************************************/ 	
  	public void setFileName2 (String filename)
  	{
  		this.strFileName2 = "";
  		this.strFileName2 = filename;
  	}
	/**************************************************************
	/*		첨부파일 명3
	**************************************************************/ 	
  	public void setFileName3 (String filename)
  	{
  		this.strFileName3 = "";
  		this.strFileName3 = filename;
  	}   	
	/**************************************************************
	/*		첨부파일 있는 디렉토리 명 
	**************************************************************/ 	
  	public void setPath (String path)
  	{
  		this.strPath = "";
  		this.strPath = path;
  	}		
 			
	/**************************************************************
	/*		message 보내기 TEST
	**************************************************************/
	public static void main(String agrs[]) throws Exception
	{
		emailSendTest app = new emailSendTest();
		
		app.setSmtpUrl("rmail1.hanmir.com");
		app.setFrom("kimes@hanmir.com");
		app.setTo("yukjm64@hanmir.com");
		app.setSubject("메일  sending ... ");
		app.setContent("메일 ");		
//		app.setFileName("Text텍스트.txt");
//		app.setFileName2("");
//		app.setFileName3("ToExcel.java");
//		app.setPath("/download");
		
		System.out.println(app.sendMessage());

//		String sub = "외부메일 전송 테스트 메시지";
//		String smg = "SMTP계정 설정을 테스트하는 동안 자동으로 보낸 전자 메일 메시지입니다.";
		
//	System.out.println("R : " + app.SetupMailServer("rmail1.hanmir.com","yukjm64@hanmir.com",sub,smg));
		
	}
	

	/*************** end ************/		
}