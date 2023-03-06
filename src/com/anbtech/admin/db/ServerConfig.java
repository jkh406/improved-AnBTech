/*****************************************************************
* server.properties 파일을 읽어온 후, 각 항목값을 리턴한다.

* 클래스 2개로 이루어져서 Config는 값을 내보내주는 클래스
  AccessConf는 server.properties의 값을 읽어오는 클래스이다.
******************************************************************/
package com.anbtech.admin.db;
import java.io.*;
import java.util.*;

public class ServerConfig {

	public static String getConf(String name){
		String conf_value;
		AccessConf conf = new AccessConf();

		try{
			if(name.equals("boardpath")){
				conf_value = conf.getBoardPath();
			}
			else if(name.equals("servlethome")){
				conf_value = conf.getServletHome();
			}
			else if(name.equals("serverURL")){
				conf_value = conf.getServerURL();
			}
			else if(name.equals("admin_user")){
				conf_value = conf.getAdminUser();
			}
			else if(name.equals("admin_password")){
				conf_value = conf.getAdminPassword();
			}
			else if(name.equals("upload_path")){
				conf_value = conf.getUploadPath();
			}
			else if(name.equals("context_path")){
				conf_value = conf.getContextPath();
			}
			else{
				conf_value = "당신의 요청에 응답하는 적당한 값이 없습니다.";
			}
			return conf_value;
		}catch(Exception e){
            return null;
		}
	}

	public static void main(String args[])
	{
		ServerConfig tst = new ServerConfig();
		try{
			String test = tst.getConf("admin_user");
			System.out.println(test);
		}catch (Exception e){
			System.out.println(e);
		}
	}
}

class AccessConf {
	private String board_path;				//보드가 설치된 경로
	private String servlet_home;			//클래스 파일 위치
	private String upload_path;				//첨부파일 업로드 경로
	private String context_path;			//컨텍스트 경로
	private String server_url;				//웹서버 주소
	private String admin_user;				//슈퍼관리자 아이디
	private String admin_password;			//슈퍼관리자 패스워드

	// protected 메버
	protected String getBoardPath() {
		return board_path;
	}
	protected String getServletHome() {
		return servlet_home;
	}		
	protected String getUploadPath() {
		return upload_path;
	}
	protected String getContextPath() {
		return context_path;
	}	
	protected String getServerURL() {
		return server_url;        
	}
	protected String getAdminUser() {
		return admin_user;        
	}
	protected String getAdminPassword() {
		return admin_password;        
	}

	//처음 클래스가 호출됨과 동시에 10개의 String이 각자 위치에 들어간다.
    protected AccessConf() {
        init();
    }

	private void init() {
        InputStream is = getClass().getResourceAsStream("../../dbconn/server.properties");
        Properties props = new Properties();

        try {
            props.load(is);
			Enumeration propNames = props.propertyNames();

			while (propNames.hasMoreElements()) {
	            String name = (String) propNames.nextElement();

				//서버 구동에 필요한 속성들을 읽어온다.
				if (name.endsWith(".conf")) {
					this.servlet_home	= props.getProperty("servlethome.conf");			//서블릿 홈 경로
					this.upload_path	= props.getProperty("uploadpath.conf");				//파일 업로드 경로
					this.context_path	= props.getProperty("contextpath.conf");			//컨텍스트 절대 경로
					this.board_path		= props.getProperty("boardpath.conf");				//board(공지,자유..) 경로
					this.server_url		= props.getProperty("serverURL.conf");				//서버 URL
					this.admin_user		= props.getProperty("admin_user.conf");				//슈퍼관리자 아이디
					this.admin_password	= props.getProperty("admin_password.conf");			//슈퍼관리자 패스워드
				}
			}
        }
        catch (Exception e) {
            System.err.println("설정파일을 찾을 수 없습니다. " + "server.properties의 위치를 확인하세요.");
            return;
        }
	}
}