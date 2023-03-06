/*****************************************************************
* db.properties.edms 파일을 읽어온 후, 각 항목값을 리턴한다.
* 클래스 2개로 이루어져서 BoardConf는 값을 내보내주는 클래스
  AccessBoardConf는 board.properties의 값을 읽어오는 클래스이다.
******************************************************************/
package com.anbtech.dms.db;
import java.io.*;
import java.util.*;

public class EdmsConf {
	//name에 맞는 boardconf를 AccessBoardConf에서 가져온다.
  	public static String getConf(String name){
		String edmsconf;
		AccessEdmsConf conf = new AccessEdmsConf();

		try{
			if(name.equals("drivers")){
				edmsconf = conf.getDrivers();		
			}
			else if(name.equals("host")){
				edmsconf = conf.getHost();			
			}
			else if(name.equals("db_user")){
				edmsconf = conf.getDbUser();
			}
			else if(name.equals("db_password")){
				edmsconf = conf.getDbPassword();
			}
			else if(name.equals("servlethome")){
				edmsconf = conf.getServletHome();
			}
			else if(name.equals("serverURL")){
				edmsconf = conf.getServerUrl();
			}
			else if(name.equals("admin_id")){
				edmsconf = conf.getAdminId();
			}
			else if(name.equals("admin_password")){
				edmsconf = conf.getAdminPassword();
			}
			else{
				edmsconf = "당신의 요청에 응답하는 적당한 값이 없습니다.";
			}
			return edmsconf;
		}catch(Exception e){
            return null;
		}
	}
}

class AccessEdmsConf {
	//사용될 변수 선언
	private String drivers;		//jdbc 드라이버
	private String host;		//데이터베이스 호스트명 
	private String db_user;		//데이터베이스 접근 사용자
	private String db_password;	//데이터베이스 접근 사용자 패스워드
	private String servlethome;	//클래스 파일 위치
	private String serverURL;	//웹서버 주소
	private String admin_id;	//보드 관리자 아이디
	private String admin_password; //관리자 패스워드

	// protected 메버
	protected String getDrivers() {
		return drivers;
	}
	protected String getHost() {
		return host;
	}		
	protected String getDbUser() {
		return db_user;
	}
	protected String getDbPassword() {
		return db_password;
	}		

	protected String getServletHome() {
		return servlethome;
	}		
	protected String getServerUrl() {
		return serverURL;        
	}
	protected String getAdminId() {
		return admin_id;        
	}
	protected String getAdminPassword() {
		return admin_password;        
	}

	//처음 클래스가 호출됨과 동시에 10개의 String이 각자 위치에 들어간다.
    protected AccessEdmsConf() {
        init();
    }

	//10개의 String을 board.properties에서 가져오는 메소드
	private void init() {
        InputStream is = getClass().getResourceAsStream("../../dbconn/db.properties.edms");
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
					this.servlethome = props.getProperty("servlethome.conf");
					this.serverURL = props.getProperty("serverURL.conf");
					this.admin_id = props.getProperty("admin_id.conf");
					this.admin_password = props.getProperty("admin_password.conf");
				}
			}
        }
        catch (Exception e) {
            System.err.println("설정파일을 찾을 수 없습니다. " +
                "db.properties.edms의 위치를 확인하세요.");
            return;
        }
	}
}