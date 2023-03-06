/*****************************************************************
* db.properties.edms ������ �о�� ��, �� �׸��� �����Ѵ�.
* Ŭ���� 2���� �̷������ BoardConf�� ���� �������ִ� Ŭ����
  AccessBoardConf�� board.properties�� ���� �о���� Ŭ�����̴�.
******************************************************************/
package com.anbtech.dms.db;
import java.io.*;
import java.util.*;

public class EdmsConf {
	//name�� �´� boardconf�� AccessBoardConf���� �����´�.
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
				edmsconf = "����� ��û�� �����ϴ� ������ ���� �����ϴ�.";
			}
			return edmsconf;
		}catch(Exception e){
            return null;
		}
	}
}

class AccessEdmsConf {
	//���� ���� ����
	private String drivers;		//jdbc ����̹�
	private String host;		//�����ͺ��̽� ȣ��Ʈ�� 
	private String db_user;		//�����ͺ��̽� ���� �����
	private String db_password;	//�����ͺ��̽� ���� ����� �н�����
	private String servlethome;	//Ŭ���� ���� ��ġ
	private String serverURL;	//������ �ּ�
	private String admin_id;	//���� ������ ���̵�
	private String admin_password; //������ �н�����

	// protected �޹�
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

	//ó�� Ŭ������ ȣ��ʰ� ���ÿ� 10���� String�� ���� ��ġ�� ����.
    protected AccessEdmsConf() {
        init();
    }

	//10���� String�� board.properties���� �������� �޼ҵ�
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
            System.err.println("���������� ã�� �� �����ϴ�. " +
                "db.properties.edms�� ��ġ�� Ȯ���ϼ���.");
            return;
        }
	}
}