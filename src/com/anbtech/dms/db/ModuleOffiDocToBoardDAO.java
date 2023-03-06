package com.anbtech.dms.db;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.anbtech.dbconn.*;
import com.anbtech.date.*;
import com.anbtech.util.normalFormat;
import com.anbtech.file.*;
import com.anbtech.text.*;
import java.lang.SecurityException;
	
public class ModuleOffiDocToBoardDAO
{
	// Database Wrapper Class ����
	private Connection con;
	private ArrayList table_list = new ArrayList();					//��������

	private com.anbtech.date.anbDate anbdt = null;					//���� ó��
	private com.anbtech.util.normalFormat nmf = null;				//�������
	private com.anbtech.text.StringProcess str = null;				//���ڿ� ó��
	private com.anbtech.file.FileWriteString write;					//������ ���Ϸ� ���
	private com.anbtech.file.textFileReader read;					//������ ���Ϸ� ���

	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public ModuleOffiDocToBoardDAO(Connection con) 
	{	
		this.con = con;

		anbdt = new com.anbtech.date.anbDate();				//����ó��
		nmf = new com.anbtech.util.normalFormat("000");		//������ȣ �Ϸù�ȣ
		str = new com.anbtech.text.StringProcess();			//���ڿ� ó��
		write = new com.anbtech.file.FileWriteString();		//������ ���Ϸ� ��� (���ο������� ���)
		read = new com.anbtech.file.textFileReader();		//������ ���Ϸ� �б�
	}
	
	/***************************************************************************
	 * �������� �б� (id:�����ڵ�,flag:������������,app_date:��������) / �ݿ��ϱ�
	 **************************************************************************/
	public void readODT(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OfficialDocumentDAO docDAO = new com.anbtech.dms.db.OfficialDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		writeNoticeBoard(flag,ip_addr);				//�Խ���[��������]���� ������ �����ϱ�
	}

	/***************************************************************************
	 * �系���� �б� / �ݿ��ϱ�
	 **************************************************************************/
	public void readIDS(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		writeNoticeBoard(flag,ip_addr);				//�Խ���[��������]���� ������ �����ϱ�
	}

	/***************************************************************************
	 * ��ܰ����ۼ� �б� / �ݿ��ϱ�
	 **************************************************************************/
	public void readODS(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		writeNoticeBoard(flag,ip_addr);				//�Խ���[��������]���� ������ �����ϱ�
	}

	/***************************************************************************
	 * ��ܰ������� �б� / �ݿ��ϱ�
	 **************************************************************************/
	public void readODR(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		writeNoticeBoard(flag,ip_addr);				//�Խ���[��������]���� ������ �����ϱ�
	}

	/***************************************************************************
	 * �Խ���[��������] ���� �����ϱ�
	 **************************************************************************/
	public void writeNoticeBoard(String flag,String ip_addr) throws Exception  
	{	
		String subject = "";						//��������
		String od_id = "", app_id = "";				//����������ȣ,������� ������ȣ
		String user_id = "";						//�ۼ��� ���
		
		String content = "",writer = "";			//�Խ��� ����,�ۼ���
		int thread = 0, pos = 0;					//�Խ��� thread, pos
		String category = "";						//�Խ��� category
		String passwd = "";							//�Խ��� ��й�ȣ

		//1. ���������� �ʿ����� �б� [�Խ��ǿ� ������ �ۼ���,����,�������� �����]
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(flag.equals("ODT")) {				//��������
			if(od_list.hasNext()){
				table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

				subject = table.getSubject();	//����
				od_id = table.getId();			//������ȣ
				app_id = table.getAppId();		//���� ������ȣ
				user_id = table.getUserId();
				writer = table.getAcName()+"/"+table.getUserName()+"/"+table.getUserId();
//				content = "ODT|"+od_id+"|"+app_id;
				content = "OfficialDocumentServlet?mode=OFD_A&id="+od_id+"&doc_id="+app_id;
			}
		} else if(flag.equals("ODR")) {			//��ܰ���
			if(od_list.hasNext()){
				table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

				subject = table.getSubject();	//����
				od_id = table.getId();			//������ȣ
				user_id = table.getUserId();
				writer = table.getAcName()+"/"+table.getUserName()+"/"+table.getUserId();
//				content = "ODR|"+od_id;
				content = "OutDocumentRecServlet?mode=ODR_V&id="+od_id;
			}
		}

		//2. �Խ����� �Ϸù�ȣ ���ϱ�
		thread = getThread();
		pos = thread;

		//3. category��ȣ ���ϱ�
		category = getCategory(flag);

		//4. �ۼ��� ��й�ȣ ���ϱ�
		passwd = getPasswd(user_id);

		//�Խ��ǿ� �����ϱ�
		Statement stmt = null;
		stmt = con.createStatement();
		String input = "";

		input = "insert into notice_board ";
		input +="(thread,depth,pos,rid,vid,cid,writer,email,homepage,ip_addr,passwd,subject,";
		input +="content,html,email_forward,filename,filesize,filetype,did,category,w_time,u_time)";
		input +="values('"+thread+"','"+"0"+"','"+pos+"','"+"0"+"','"+"0"+"','"+"0"+"','";
		input +=writer+"','"+""+"','"+""+"','"+ip_addr+"','"+passwd+"','"+subject+"','";
		input +=content+"','"+"d"+"','"+""+"','"+""+"','"+""+"','"+""+"','";
		input +=""+"','"+category+"','"+anbdt.getTime()+"','"+""+"')";
		
		stmt.executeUpdate(input);
		stmt.close();
		//System.out.println("�Խ��� input : " + input );

	}

	/***************************************************************************
	 * �Խ����� �Ϸù�ȣ ���ϱ�
	 **************************************************************************/
	private int getThread() throws Exception  
	{
		int rtn = 0;

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		String query = "select thread from notice_board order by thread desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getInt("thread");
		stmt.close();
		rs.close();

		rtn++;
		return rtn;
	}

	/***************************************************************************
	 * category��ȣ ���ϱ�
	 **************************************************************************/
	private String getCategory(String flag) throws Exception  
	{
		String rtn = "";
		String ctno = "";

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		String query = "select category_items from board_env where tablename='notice_board'";
		rs = stmt.executeQuery(query);
		if(rs.next()) ctno = rs.getString("category_items");
		stmt.close();
		rs.close();

		if(flag.equals("ODT")) {				//��������
			if(ctno.indexOf("��������") != -1) {
				StringTokenizer cat = new StringTokenizer(ctno,"|");
				int n = 0;
				while(cat.hasMoreTokens()) {
					String name = cat.nextToken();
					if(name.equals("��������")) rtn = Integer.toString(n);
					n++;
				}
			}
		} else if(flag.equals("ODR")) {			//��ܰ���
			if(ctno.indexOf("��ܰ���") != -1) {
				StringTokenizer cat = new StringTokenizer(ctno,"|");
				int n = 0;
				while(cat.hasMoreTokens()) {
					String name = cat.nextToken();
					if(name.equals("��ܰ���")) rtn = Integer.toString(n);
					n++;
				}
			}
		}

		return rtn;
	}

	/***************************************************************************
	 * �ۼ��� ��й�ȣ ���ϱ�
	 **************************************************************************/
	private String getPasswd(String user_id) throws Exception  
	{
		String rtn = "";

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		String query = "select passwd from user_table where id='"+user_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getString("passwd");
		stmt.close();
		rs.close();

		return rtn;
	}

	/******************************************************************************
	// ID�� ���ϴ� �޼ҵ�
	******************************************************************************/
	private String getID()
	{
		String ID;
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		String y = first.format(now);
		String s = last.format(now);
		nmf.setFormat("000");		//�Ϸù�ȣ ��� ����(6�ڸ�)		
		ID = y + nmf.toDigits(Integer.parseInt(s));
		return ID;
	}

}	

