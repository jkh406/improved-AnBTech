package com.anbtech.admin;

import java.util.Hashtable;

public class SessionManager{
	public static SessionManager instance;
	public Hashtable clients;
   
	static synchronized public SessionManager getInstance() {
       if (instance == null) {
           instance = new SessionManager();
       }
       return instance;
   }

   private SessionManager() {
       init();
   }

   private void init(){
       clients = new Hashtable();
   }

   /**
    * Hashtable에 로그인 사용자 추가
    * @param sessionid 세션아이디
    * @param id 아이디
    */
   public void addClient(String sessionid,String id)
   {
       clients.put(sessionid,id);
   }

   /**
    * Hashtable에 로그인 사용자 삭제
    * @param sessionid 세션아이디
    */

   public void removeClient(String sessionid){
       clients.remove(sessionid);     
   }

   /**
    * 현재 저장된 내용을 전부 반환한다.
    */

	public Hashtable getClient(){
       return clients;
   }

   /**
    * 현재 저장된 내용을 전부 반환한다.
    */

   public int getAccessClientCount(){
       return clients.size();
   }
}