package com.chat.chat;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class ChatApplet extends Applet 
	implements Runnable, ActionListener {
	private    boolean            debug = false;
	protected  BufferedReader  	  i;
	protected  PrintWriter 	      o;
	protected  TextArea           output;
	protected  TextField          input, logtext;
	protected  Thread             listener;
	protected  CardLayout         card;
	protected  boolean            stop;

	public void init () {
		card = new CardLayout();
		setLayout (card);
		Panel login = new Panel(new BorderLayout());
		Panel bottom = new Panel();
		logtext = new TextField(15);
		logtext.addActionListener(this);
		bottom.add(new Label("로그인:"));
		bottom.add(logtext);
		login.add("South", bottom);
		login.add("Center", new Label("자바 애플릿 채팅", 
		                               Label.CENTER));

		Panel chat = new Panel(new BorderLayout());
		chat.add ("Center", output = new TextArea ());
		output.setEditable (false);
		chat.add ("South", input = new TextField ());
		input.addActionListener(this);

		add(login, "login");
		add(chat, "chat");
		card.show(this, "login");
	}

	public void start () {
		stop = false;
		listener = new Thread (this);
		listener.start ();
	}

	public void stop () {
		if (listener != null) {
			stop = true;
			try {
				if(o != null) {
					o.println("!#%&");
					o.flush();
					i.close();
					o.close();
				}
			} catch (Exception e) { }
		}
		listener = null;
	}

	public void destroy() {
		if(o != null) {
			stop();
		}		
	}
	
	public void run () {
		try {
			String host = getCodeBase().getHost ();
			String port = getParameter ("port");
			if (port == null)
				port = "9830";
			Socket s = new Socket (host, Integer.parseInt (port));
			InputStream ins = s.getInputStream();
			OutputStream os = s.getOutputStream();
			i = new BufferedReader(new InputStreamReader(ins));
			o = new PrintWriter(new OutputStreamWriter(os));
			execute ();
		} catch (IOException ex) {
			if(debug)
				ex.printStackTrace (System.out);
		}
	}

	public void execute () {
		try {
			while (!stop) {
				String line = i.readLine();
				output.append (line + "\n");
			}
		} catch (IOException ex) {
			if(debug)
				ex.printStackTrace (System.out);
		} finally {
			listener = null;
			o.close();
		}
	}

	public void actionPerformed (ActionEvent e) {
		Component c = (Component) e.getSource();
		if(c == logtext) {
			String loginname = logtext.getText();
			loginname = loginname.trim();
			if(loginname == null || loginname.length() == 0) {
				return;
			}
			o.println(loginname);
			o.flush();
			card.show(this, "chat");
			input.requestFocus();
		} else if(c == input) {
			o.println(input.getText());
			o.flush ();
			input.setText ("");
		}
	}
}
