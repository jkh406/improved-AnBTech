/*****************************************************************
* board.properties 파일을 읽어온 후, 각 항목값을 리턴한다.

* 클래스 2개로 이루어져서 BoardConf는 값을 내보내주는 클래스
  AccessBoardConf는 board.properties의 값을 읽어오는 클래스이다.
******************************************************************/
package com.anbtech.board.db;
import java.io.*;
import java.util.*;

public class BoardConf {
	//name에 맞는 boardconf를 AccessBoardConf에서 가져온다.
  	public static String getConf(String name){
		String boardconf;
		AccessBoardConf conf = new AccessBoardConf();

		try{
			if(name.equals("drivers")){
				boardconf = conf.getDrivers();		
			}
			else if(name.equals("host")){
				boardconf = conf.getHost();			
			}
			else if(name.equals("db_user")){
				boardconf = conf.getDb_user();
			}
			else if(name.equals("db_password")){
				boardconf = conf.getDb_password();
			}
			else if(name.equals("boardpath")){
				boardconf = conf.getBoardpath();
			}
			else if(name.equals("servlethome")){
				boardconf = conf.getServlethome();
			}
			else if(name.equals("smtpServer")){
				boardconf = conf.getSmtpServer();
			}
			else if(name.equals("serverURL")){
				boardconf = conf.getServerURL();
			}
			else if(name.equals("board_user")){
				boardconf = conf.getBoard_user();
			}
			else if(name.equals("board_password")){
				boardconf = conf.getBoard_password();
			}
			else{
				boardconf = "당신의 요청에 응답하는 적당한 값이 없습니다.";
			}
			return boardconf;
		}catch(Exception e){
            return null;
		}
	}
}

class AccessBoardConf {
	//사용될 변수 선언
	private String drivers;		//jdbc 드라이버 (예)org.gjt.mm.mysql.Driver
	private String host;		//데이터베이스 호스트명 (예)jdbc:mysql://localhost/testdb
	private String db_user;		//데이터베이스 접근 사용자
	private String db_password;	//패스워드
	private String boardpath;	//보드가 설치된 경로 (예)/OfficeWare/notice
	private String servlethome;	//클래스 파일 위치	(예) /WEB-INF/classes/com/anbtech/board
	private String smtpServer;	//메일 수신 서버
	private String serverURL;	//웹서버 주소
	private String board_user;	//보드 관리자 아이디
	private String board_password; //관리자 패스워드

	// protected 메버
	protected String getDrivers() {
		return drivers;
	}
	protected String getHost() {
		return host;
	}		
	protected String getDb_user() {
		return db_user;
	}
	protected String getDb_password() {
		return db_password;
	}		
	protected String getBoardpath() {
		return boardpath;
	}
	protected String getServlethome() {
		return servlethome;
	}		
	protected String getSmtpServer() {
		return smtpServer;
	}	
	protected String getServerURL() {
		return serverURL;        
	}
	protected String getBoard_user() {
		return board_user;        
	}
	protected String getBoard_password() {
		return board_password;        
	}

	//처음 클래스가 호출됨과 동시에 10개의 String이 각자 위치에 들어간다.
    protected AccessBoardConf() {
        init();
    }

	//10개의 String을 board.properties에서 가져오는 메소드
	private void init() {
        InputStream is = getClass().getResourceAsStream("../../dbconn/db.properties");
        Properties props = new Properties();

        try {
            props.load(is);
			
			this.drivers = props.getProperty("drivers");

			Enumeration propNames = props.propertyNames();

			while (propNames.hasMoreElements()) {
	            String name = (String) propNames.nextElement();

				if (name.endsWith(".host")) {
					String poolName = name.substring(0, name.lastIndexOf("."));
					this.host = props.getProperty(poolName + ".host");
					this.db_user = props.getProperty(poolName + ".db_user");
					this.db_password = props.getProperty(poolName + ".db_password");
				}

				if (name.endsWith(".conf")) {
					this.boardpath = props.getProperty("boardpath.conf");
					this.servlethome = props.getProperty("servlethome.conf");
					this.smtpServer = props.getProperty("smtpServer.conf");
					this.serverURL = props.getProperty("serverURL.conf");
					this.board_user = props.getProperty("board_user.conf");
					this.board_password = props.getProperty("board_password.conf");
				}
			}
        }
        catch (Exception e) {
            System.err.println("설정파일을 찾을 수 없습니다. " +
                "board.properties의 위치를 확인하세요.");
            return;
        }
	}
}