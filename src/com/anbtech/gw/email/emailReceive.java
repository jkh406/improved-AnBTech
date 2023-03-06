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
	//접속시 필요한 환경 정보	
	private String host;			//받는 메일 서버명 (예: rmail1.hanmir.com)
	private String sendpt;			//받는 메일 서버protocol종류 (pop3 또는 imap)
	private String username;		//사용자 ID
	private String password;		//사용자 비밀번호 
	private String isMod;			//읽기Type (읽기만:RO, 읽고 받은메일서버에서 삭제:RW) 
	
	//접속하기 
	private Store store;			//mail server에 연결 
	private Folder folder;			//폴더에 연결하기 
	private Message message;		//message전달하기 
	
	//email을 저장하기 위한 전달 변수
	private String pid;				//관리번호 
	private String id;				//작성자 사번 
	private String name;			//작성자 이름 
	private String bonfn;			//저장할 본문파일명 
	private String addpath;			//첨부파일 저장할 path
	private String textpath;		//본문내용 저장할 path					

	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public emailReceive() 
	{	
		
	}	
	
	/***************************************************************************
	 * host명 (받는 메일 서버 종류)
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
	 * user name (받는 메일서버에 등록된)
	 **************************************************************************/
	public void setUserName(String username) 
	{	
		this.username = username;
	}	

	/***************************************************************************
	 * password (받는 메일서버에 등록된)
	 **************************************************************************/
	public void setPassWord(String password) 
	{	
		this.password = password;
	}	

	/***************************************************************************
	 * 받은 메일서버의 메시지 삭제여부 (RO : Read_Only, RW : Read_Write)
	 **************************************************************************/
	public void isDelete(String isDel) 
	{	
		this.isMod = isDel;
	}	

	/***************************************************************************
	 * 전자메일 관리 번호 
	 **************************************************************************/
	public void setPID(String pid) 
	{	
		this.pid = pid;
	}	

	/***************************************************************************
	 * 전자메일 사용자 사번 (login id) 
	 **************************************************************************/
	public void setID(String id) 
	{	
		this.id = id;
	}	

	/***************************************************************************
	 * 전자메일 사용자 이름 
	 **************************************************************************/
	public void setName(String name) 
	{	
		this.name = name;
	}	

	/***************************************************************************
	 * 전자메일 본문저장할 파일명 
	 **************************************************************************/
	public void setSaveFile(String bonfn) 
	{	
		this.bonfn = bonfn;
	}	

	/***************************************************************************
	 * 전자메일 첨부파일명 저장할 path
	 **************************************************************************/
	public void setAddPath(String addpath) 
	{	
		this.addpath = addpath;
	}	

	/***************************************************************************
	 * 전자메일 본문내용 저장할 path
	 **************************************************************************/
	public void setTextPath(String textpath) 
	{	
		this.textpath = textpath;
	}	

	/***************************************************************************
	 * 메일서버에 연결하기 
	 **************************************************************************/
	public String getConnect() throws Exception 
	{	
		String mailhost = ""; 			//mail.pop3.host  or mail.imap.host
		
		//System properties
		Properties props = System.getProperties();
		mailhost = "mail." + this.sendpt + ".host";
		props.put(mailhost,this.host);
		
		//세션 구하기 
		Session session = Session.getDefaultInstance(props,null);
		
		//메일서버에 연결하기
		store = session.getStore(this.sendpt);
		store.connect(this.host,this.username,this.password);
		
		//폴더에 연결하기 
		folder = store.getDefaultFolder();
		if(folder == null) return "disconnection";
		else {
			folder = folder.getFolder("INBOX");
			if(this.isMod.equals("RO"))    			//읽기 전용 (메일서버에 메시지가 그대로 있음)
				folder.open(Folder.READ_ONLY);
			else folder.open(Folder.READ_WRITE);	//읽고(RW) 메일서버 메시지 삭제하기 			
			return "Good";	
		}
				
	}
	
	/***************************************************************************
	 * 메시지 총수량 파악하기 
	 **************************************************************************/
	 public int getMailCount() throws Exception
	 {	
		return folder.getMessageCount();	
	}
	
	/***************************************************************************
	 * 메일 가져오기 (지정된 폴더의 번호로 가져오기)
	 **************************************************************************/
	 public Message getMail(int intNumber) throws Exception
	 {	
	 	message = folder.getMessage(intNumber);
		return message;	
	}
			
	/***************************************************************************
	 * 메일 가져오기 (한번에 전부 가져 오기)
	 **************************************************************************/
/*	 public Message[] getMail() throws Exception
	 {	
		//메시지 가져오기
		return folder.getMessages();	
	}
*/	
	/***************************************************************************
	 * 메일 삭제을 위한 DELETED마크 하기
	 **************************************************************************/
	 public void markDeleted() throws Exception
	 {	
		message.setFlag(Flags.Flag.DELETED,true);
	}
	
	/***************************************************************************
	 * 메일 분류하기 (자체 Test용)
	 **************************************************************************/
	public String getDetail() throws Exception
	{ 	
		String from = "";				//보낸 사람 주소 
		String sent_date = "";			//보낸 날자 
		String subject="";				//메일 제목 
		String content="";				//메일 본문 
		String FileName="";				//첨부파일 (original명)
	
	
		//메시지 수량 파악하기 
		int mcnt = getMailCount();
		
		//처리할 메시지가 없음  
		if(mcnt == 0) { 			
			close();	//종료하기 
			return "처리할 내용이 없습니다";	
		} else { //메시지 처리 하기 									
			for(int i = 1; i <= mcnt; i++) {
				//메시지 가져오기 
				Message message = getMail(i);
				
				//1.보낸사람주소 알아보기 
				from = "";
				from = message.getFrom()[0].toString();
				
				String fname = encordingName(from);		//인코딩을 디코딩하기
				if(fname.equals("NOENC")) 
					from = new String(from.getBytes("8859_1"),"euc-kr");
				else from = fname; 	
				from = from.replace('"',' ');		//이름에서 " 없애기 
				 
				//2.보낸날자 알아보기 
				java.util.Date now = message.getSentDate();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd  HH:mm");
				sent_date = vans.format(now);
				
				//3.제목 알아보기
				String title = message.getSubject();
				subject = encordingSubject(title);
				
				//4.저장할 본문내용 저장파일명 (중복을 피하기 위해 가공함)
				String bon_filename = this.bonfn + i + ".html";
				
				//5.저장할 첨부파일 저장파일명 (중복을 피하기 위해 가공함)
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
				
				
				//6-1.첨부파일 및 내용 알아보기 
				if(message.isMimeType("multipart/*")) {			//첨부파일 처리 하기 
					Multipart multipart = (Multipart)message.getContent();
					int m = multipart.getCount();					//첨부파일 갯수 
					for(int j = 0; j < m; j++) {					
						Part part = multipart.getBodyPart(j);
						String disposition = part.getDisposition();
																	
						//메일 본문 ( j=0 : null --> 본문 내용)
						if(disposition == null) {
							//6-1-1.본문 파일로 저장 (Stream으로 처리 하여 한글 깨짐 방지)
							InputStream inFile = part.getInputStream();   //본문을 stream형태로 읽음 
							//saveTextInputStream(bon_filename,inFile);	
						} 
						
						//첨부파일 읽기 (j=1 : attachement 첨부파일1 , - - , j=3 : attachement 첨부파일3)
						if(disposition != null && (disposition.equals(part.ATTACHMENT) || disposition.equals(part.INLINE))) {

							//6-1-2.첨부파일명 
							String FN = part.getFileName();	
							FileName = "";	
							if(FN != null) FileName = new String(FN.getBytes("8859_1"),"EUC_KR");
							
							//첨부파일 확장자명 구하기 
							String extFileName = "";
							if(FileName.length() > 0) {
								extFileName = FileName.substring(FileName.indexOf("."),FileName.length());
								//System.out.println("ext : " + extFileName);	
							}
								
							//6-1-3.첨부파일 저장하기 
							BufferedInputStream in = new BufferedInputStream(part.getInputStream());
							//saveAddInputStream(add_filename,extFileName,j,in);						
							
						} //if
					} //for
				//6-2.첨부파일 없음 
				} else {											
					FileName = "";
					//6-2-1.본문 파일로 저장 (Stream으로 처리 하여 한글 깨짐 방지) 
					InputStream inFile = message.getInputStream();
					//saveTextInputStream(bon_filename,inFile);					
				}
				
				//내용 출력 하기 
				//System.out.println("보낸사람 = " + from);
//				System.out.println("보낸날자 = " + sent_date);
				//System.out.println("Mail제목 = " + subject + "\n");
//				System.out.println("Mail본문 = " + content);
//				System.out.println("첨부파일명 = " + FileName);

				//삭제하기위한 DELETED마크 하기
				markDeleted();
				
			} //for	
			
			
			//종료 하기 
			if(folder.isOpen()) {
				close();
				//System.out.println("folder is deleted");
			}else {
				//System.out.println("folder is closed");
			}
			
			return "처리가 완료되었습니다.";		
		} //if
		
		 
	}
	
	/***************************************************************************
	 * 보낸사람 이름 구하기 : 인코딩 방식에 따라 디코딩 하기 
	 * Name : 인코딩된 이름 예>?EUC-KR?Q?DF 
	 * return : 디코딩 처리한 결과 이름 (인코딩된 이름이 아니면 NOENC 를 리턴함)
	 **************************************************************************/
	 public String encordingName(String name) throws Exception
	 {	
		//방법1. 인코딩을 디코딩하기 (자체가 인코딩된 상태로 읽어들임)
		if((name.indexOf("?B?") > 0) || (name.indexOf("?Q?") > 0)) {
			name = MimeUtility.decodeText(name);
			return name;
		} else return "NOENC";			
	}
	/***************************************************************************
	 * 제목 구하기 : 인코딩 방식에 따라 한글깨짐 처리  
	 * title : 원래 제목 
	 * 정상적인 한글은 한글인코딩후 endcodeText가 안되나 그렇지않은경우는 encodeText됨 
	 **************************************************************************/	
	public String encordingSubject(String title) throws Exception
	 {
	 	//한글 인코딩을 실행한다.
		String dec = new String(title.getBytes("8859_1"),"EUC_KR");
		
		//인코딩을 실행한다.
		dec = MimeUtility.encodeText(dec,"Cp970","Q");
		
		//인코딩된경우(=?Cp970?Q?)만 한글 인코딩하고 그렇지 않으면 그대로 리턴한다.
		if(dec.indexOf("Cp970") > 0) {
			title = new String(title.getBytes("8859_1"),"EUC_KR");
			return title;
		}else return title;	
	 	
	 }

	/***************************************************************************
	 * 본문내용 저장하기 (InputStream) : Stream처리로 한글깨지지 않음 
	 * (message.getInputStream() or message.getContent() 는 본문내용을 읽음)
	 **************************************************************************/
	 public void saveTextInputStream(String bonfile,java.io.InputStream inFile) throws Exception
	 {
	 	//저장할 본문 파일명 
	 	String bon_filename = bonfile;
	 	
		//저장할 Path 지정 
		String Textpath = this.textpath;
		File dir = new File(Textpath);
		if(!dir.exists()) dir.mkdirs();
		

		//본문 파일로 저장하기 	
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
	 	//저장할 본문 파일명 
	 	String bon_filename = bonfile;
	 	
		//저장할 Path 지정 
		String Textpath = this.textpath;
		File dir = new File(Textpath);
		if(!dir.exists()) dir.mkdirs();
		
		//encoding 여부 판단하여 본문 파일로 저장하기 	 
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
	 * 첨부파일 저장하기 (InputStream) : Stream처리로 한글깨지지 않음 
	 * (message.getInputStream() or message.getContent() 는 본문내용을 읽음)
	 * 		변경할 파일명   : addfile		(200302051034001 등)
	 * 		확장자명 	    : extFileName 	(.hwp 등)
	 * 		첨부파일 갯수   : j			(1,2, 등)
	 *  	스트림으로 받기 : inFile
	 **************************************************************************/
	 public void saveAddInputStream(String addfile, String extFileName, int j, java.io.BufferedInputStream inFile) throws Exception
	 {
	 	//저장할 첨부 파일명 (다른이름으로 변경함)
	 	String add_filename = addfile + "_" + j + extFileName;
	 	
		//저장할 Path 지정 
		String Addpath = this.addpath;
		File dir = new File(Addpath);
		if(!dir.exists()) dir.mkdirs();
		

		//첨부 파일로 저장하기 	
		FileOutputStream outFile = new FileOutputStream(Addpath + "/" + add_filename);

		int c;
		while((c=inFile.read()) != -1)
			outFile.write(c);
			
		//close	
		outFile.flush();
		outFile.close();	     	 
	 	
	 }	 
	 
	/***************************************************************************
	 * 가져온 메일 DELETED마크 하고 삭제하기 (한번에 실행하기)
	 **************************************************************************/	 
	 public void removeMessage() throws MessagingException 
	 {
	 	int totalMessage = 0;			//message의 총갯수 
	 	String mailhost = ""; 			//mail.pop3.host  or mail.imap.host
		mailhost = "mail." + this.sendpt + ".host";
		
	 	try {			
			Properties props = System.getProperties();
			props.put(mailhost,this.host);
			Session session = Session.getDefaultInstance(props,null);
			store = session.getStore(this.sendpt);
			store.connect(this.host,this.username,this.password);
			folder = store.getFolder("INBOX");
			
						
			//message수량 파악하기 									
			if(folder == null || !folder.exists()) {
				System.exit(1);
			} else {
				folder.open(Folder.READ_WRITE);				//읽고(RW) 삭제 
				totalMessage =  folder.getMessageCount();	//메시지 수량 
			}	
			//System.out.println("T cnt : " + totalMessage);
			
			//삭제 DELETED마크 하기 
			for(int i = 1; i <= totalMessage; i++){
				//메일서버 DELETED마크하기(1개)  		
        		Message message = folder.getMessage(i);//해당번호 DELETED마크 하기 
	    		message.setFlag(Flags.Flag.DELETED, true);	
			} //for
					
			//전부 완전 삭제하기 
			folder.close(true);			//삭제하기 
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