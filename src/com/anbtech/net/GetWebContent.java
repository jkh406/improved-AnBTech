package com.anbtech.net;

import java.io.*;
import java.net.*;

public class GetWebContent {
	private String destUrl;
	private String content;
	
	public void setDestUrl(String destUrl){
		this.destUrl = destUrl;
		setContent();
	}
	
	private void setContent(){
		try {
			URL u = new URL(destUrl);
			BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
			
			String temp = "";
			StringBuffer sf = new StringBuffer("");
			
			while ((temp=br.readLine())!=null){
				sf.append(temp);
			}
			content = sf.toString();
		}catch (Exception e){
			content = "화면을 표시할 수 없습니다.";
		}
	}

	public String getContent(){
		return content;
	}
}