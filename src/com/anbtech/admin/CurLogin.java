package com.anbtech.admin;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CurLogin implements HttpSessionBindingListener {
	String loginid;
	String sessionid;
	SessionManager sm;

	public CurLogin(String loginid){
		this.loginid = loginid;
	}
	
	//���ǿ� ���� �߰��ɶ� �߻��ϴ� �̺�Ʈ �޼ҵ�
	public void valueBound(HttpSessionBindingEvent event) {
		HttpSession ss = event.getSession();
		sessionid = ss.getId();
		sm = SessionManager.getInstance();
		//Hashtable�� ���Ǿ��̵�� �α��� ���̵� �����մϴ�.
		sm.addClient(sessionid,loginid);
	}

	//���ǿ� ���� �Ҹ�ɶ� �߻��ϴ� �̺�Ʈ �޼ҵ�
	public void valueUnbound(HttpSessionBindingEvent event) {
		HttpSession ss = event.getSession();
		sessionid = ss.getId();
		sm = SessionManager.getInstance();
		//Hashtable���� ���Ǿ��̵� Ű������ ���� �մϴ�.
		sm.removeClient(sessionid);
	}
}