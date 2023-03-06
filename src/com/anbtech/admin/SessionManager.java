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
    * Hashtable�� �α��� ����� �߰�
    * @param sessionid ���Ǿ��̵�
    * @param id ���̵�
    */
   public void addClient(String sessionid,String id)
   {
       clients.put(sessionid,id);
   }

   /**
    * Hashtable�� �α��� ����� ����
    * @param sessionid ���Ǿ��̵�
    */

   public void removeClient(String sessionid){
       clients.remove(sessionid);     
   }

   /**
    * ���� ����� ������ ���� ��ȯ�Ѵ�.
    */

	public Hashtable getClient(){
       return clients;
   }

   /**
    * ���� ����� ������ ���� ��ȯ�Ѵ�.
    */

   public int getAccessClientCount(){
       return clients.size();
   }
}