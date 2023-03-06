package com.chat.chat;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatHandler extends Thread {
	private   boolean          debug = false;
	protected Socket           s;
	protected BufferedReader   i;
	protected PrintWriter      o;
	protected ChatServer       server;
	protected boolean          stop;
	
	public ChatHandler(ChatServer server, Socket s) 
		throws IOException {
		
		this.s = s;
		this.server = server;
		InputStream ins = s.getInputStream();
		OutputStream os = s.getOutputStream();
		i = new BufferedReader(new InputStreamReader(ins));
		o = new PrintWriter(new OutputStreamWriter(os));
	}
	
	public void setStop() {
		stop = true;
		synchronized(server.handlers) {
			server.handlers.remove(this);
		}
	}
	
	public void run () {
		String name = "";
		try {
			name = i.readLine();
			broadcast (name + "¥‘¿Ã πÊπÆ«œºÃΩ¿¥œ¥Ÿ.");
			while (!stop) {
				String msg = i.readLine();
				if(msg.equals("!#%&")) {
					setStop();	
				} else {
					broadcast (name + " - " + msg);
				}
			}
		} catch (IOException ex) {  
			if(debug)
				ex.printStackTrace();
		} finally {
			server.handlers.removeElement(this);
			broadcast (name + "¥‘¿Ã ≥™∞°ºÃΩ¿¥œ¥Ÿ.");
			try {
				i.close();
				o.close();
				s.close();
			} catch (IOException ex) {  
				if(debug)
					ex.printStackTrace();
			}
		}
	}
	
	protected void broadcast (String message) {
		synchronized (server.handlers) {
			int n = server.handlers.size();
			for(int i=0; i < n; i++) {
				ChatHandler c = (ChatHandler)
					server.handlers.elementAt(i);
				try {
					synchronized (c.o) {
						c.o.println(message);
					}
					c.o.flush ();
				} catch (Exception ex) {
					c.setStop();
				}
			}
		}
	}
}
