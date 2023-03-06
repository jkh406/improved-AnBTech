/*****************************************************************
* server.properties ������ �о�� ��, �� �׸��� �����Ѵ�.

* Ŭ���� 2���� �̷������ Config�� ���� �������ִ� Ŭ����
  AccessConf�� server.properties�� ���� �о���� Ŭ�����̴�.
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
				conf_value = "����� ��û�� �����ϴ� ������ ���� �����ϴ�.";
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
	private String board_path;				//���尡 ��ġ�� ���
	private String servlet_home;			//Ŭ���� ���� ��ġ
	private String upload_path;				//÷������ ���ε� ���
	private String context_path;			//���ؽ�Ʈ ���
	private String server_url;				//������ �ּ�
	private String admin_user;				//���۰����� ���̵�
	private String admin_password;			//���۰����� �н�����

	// protected �޹�
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

	//ó�� Ŭ������ ȣ��ʰ� ���ÿ� 10���� String�� ���� ��ġ�� ����.
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

				//���� ������ �ʿ��� �Ӽ����� �о�´�.
				if (name.endsWith(".conf")) {
					this.servlet_home	= props.getProperty("servlethome.conf");			//���� Ȩ ���
					this.upload_path	= props.getProperty("uploadpath.conf");				//���� ���ε� ���
					this.context_path	= props.getProperty("contextpath.conf");			//���ؽ�Ʈ ���� ���
					this.board_path		= props.getProperty("boardpath.conf");				//board(����,����..) ���
					this.server_url		= props.getProperty("serverURL.conf");				//���� URL
					this.admin_user		= props.getProperty("admin_user.conf");				//���۰����� ���̵�
					this.admin_password	= props.getProperty("admin_password.conf");			//���۰����� �н�����
				}
			}
        }
        catch (Exception e) {
            System.err.println("���������� ã�� �� �����ϴ�. " + "server.properties�� ��ġ�� Ȯ���ϼ���.");
            return;
        }
	}
}