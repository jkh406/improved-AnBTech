package com.anbtech.net;

import java.io.*;
import java.net.*;

public class Mail {
	// 메일을 보내준다.
	public void sendMail(String smtpServer, String from, String writer, String to, String subject, String content){
		try {
			Socket s = new Socket(smtpServer,25);
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			
			pw.println("HELO www.anbtech.co.kr");
			pw.println("MAIL FROM: " + from);
			pw.println("RCPT TO: " + to);
			pw.println("DATA");
			//pw.println("From: " +"\"" + writer + "\""+ "<"+from+">");
			//pw.println("To: " + to);
			pw.println("Subject: " + subject);
			pw.println("Content-Type: text/html; charset=EUC-KR\r\n");
			
			pw.println(content);
			pw.println(".");
			pw.println("quit");
			pw.flush();
			
			String temp = "";
			
			InputStream is = s.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((temp = br.readLine()) != null && is.available()>0){
				System.out.println(temp);
			}
			
			s.close();
		}catch (Exception e){
			System.out.println("메일을 보내는중 에러 발생");
			e.printStackTrace();
		}		
	}
}