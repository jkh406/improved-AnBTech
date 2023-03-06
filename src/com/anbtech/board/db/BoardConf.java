/*****************************************************************
* board.properties ������ �о�� ��, �� �׸��� �����Ѵ�.

* Ŭ���� 2���� �̷������ BoardConf�� ���� �������ִ� Ŭ����
  AccessBoardConf�� board.properties�� ���� �о���� Ŭ�����̴�.
******************************************************************/
package com.anbtech.board.db;
import java.io.*;
import java.util.*;

public class BoardConf {
	//name�� �´� boardconf�� AccessBoardConf���� �����´�.
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
				boardconf = "����� ��û�� �����ϴ� ������ ���� �����ϴ�.";
			}
			return boardconf;
		}catch(Exception e){
            return null;
		}
	}
}

class AccessBoardConf {
	//���� ���� ����
	private String drivers;		//jdbc ����̹� (��)org.gjt.mm.mysql.Driver
	private String host;		//�����ͺ��̽� ȣ��Ʈ�� (��)jdbc:mysql://localhost/testdb
	private String db_user;		//�����ͺ��̽� ���� �����
	private String db_password;	//�н�����
	private String boardpath;	//���尡 ��ġ�� ��� (��)/OfficeWare/notice
	private String servlethome;	//Ŭ���� ���� ��ġ	(��) /WEB-INF/classes/com/anbtech/board
	private String smtpServer;	//���� ���� ����
	private String serverURL;	//������ �ּ�
	private String board_user;	//���� ������ ���̵�
	private String board_password; //������ �н�����

	// protected �޹�
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

	//ó�� Ŭ������ ȣ��ʰ� ���ÿ� 10���� String�� ���� ��ġ�� ����.
    protected AccessBoardConf() {
        init();
    }

	//10���� String�� board.properties���� �������� �޼ҵ�
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
            System.err.println("���������� ã�� �� �����ϴ�. " +
                "board.properties�� ��ġ�� Ȯ���ϼ���.");
            return;
        }
	}
}