package com.chat.chat;
import java.net.*;
import java.util.*;

public class ChatServer {
	private boolean debug = false;
    protected Vector handlers;	
    
    public ChatServer (int port) {
        try {
            ServerSocket server = new ServerSocket (port);
            handlers = new Vector();
            System.out.println("ChatServer is ready.");
            while (true) {
                Socket client = server.accept ();
                if(debug)
                	System.out.println ("From: "+ 
					                   client.getInetAddress());
                ChatHandler c = new ChatHandler (this, client);
                handlers.addElement(c);
                c.start ();
            }
        } catch(Exception e) {
            if(debug)
            	e.printStackTrace();
        }
    }

}
