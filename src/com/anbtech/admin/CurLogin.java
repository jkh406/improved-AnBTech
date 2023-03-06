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
	
	//세션에 값이 추가될때 발생하는 이벤트 메소드
	public void valueBound(HttpSessionBindingEvent event) {
		HttpSession ss = event.getSession();
		sessionid = ss.getId();
		sm = SessionManager.getInstance();
		//Hashtable에 세션아이디와 로그인 아이디를 저장합니다.
		sm.addClient(sessionid,loginid);
	}

	//세션에 값이 소멸될때 발생하는 이벤트 메소드
	public void valueUnbound(HttpSessionBindingEvent event) {
		HttpSession ss = event.getSession();
		sessionid = ss.getId();
		sm = SessionManager.getInstance();
		//Hashtable에서 세션아이디 키값으로 삭제 합니다.
		sm.removeClient(sessionid);
	}
}