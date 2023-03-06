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
	// Database Wrapper Class 선언
	private Connection con;
	private ArrayList table_list = new ArrayList();					//읽은내용

	private com.anbtech.date.anbDate anbdt = null;					//일자 처리
	private com.anbtech.util.normalFormat nmf = null;				//출력포멧
	private com.anbtech.text.StringProcess str = null;				//문자열 처리
	private com.anbtech.file.FileWriteString write;					//내용을 파일로 담기
	private com.anbtech.file.textFileReader read;					//내용을 파일로 담기

	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public ModuleOffiDocToBoardDAO(Connection con) 
	{	
		this.con = con;

		anbdt = new com.anbtech.date.anbDate();				//날자처리
		nmf = new com.anbtech.util.normalFormat("000");		//문서번호 일련번호
		str = new com.anbtech.text.StringProcess();			//문자열 처리
		write = new com.anbtech.file.FileWriteString();		//내용을 파일로 담기 (개인우편보낼때 사용)
		read = new com.anbtech.file.textFileReader();		//내용을 파일로 읽기
	}
	
	/***************************************************************************
	 * 공지공문 읽기 (id:관리코드,flag:공문종류구분,app_date:승인일자) / 반영하기
	 **************************************************************************/
	public void readODT(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OfficialDocumentDAO docDAO = new com.anbtech.dms.db.OfficialDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		writeNoticeBoard(flag,ip_addr);				//게시판[공지사항]으로 데이터 전송하기
	}

	/***************************************************************************
	 * 사내공문 읽기 / 반영하기
	 **************************************************************************/
	public void readIDS(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		writeNoticeBoard(flag,ip_addr);				//게시판[공지사항]으로 데이터 전송하기
	}

	/***************************************************************************
	 * 사외공문작성 읽기 / 반영하기
	 **************************************************************************/
	public void readODS(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		writeNoticeBoard(flag,ip_addr);				//게시판[공지사항]으로 데이터 전송하기
	}

	/***************************************************************************
	 * 사외공문접수 읽기 / 반영하기
	 **************************************************************************/
	public void readODR(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		writeNoticeBoard(flag,ip_addr);				//게시판[공지사항]으로 데이터 전송하기
	}

	/***************************************************************************
	 * 게시판[공지사항] 내용 저장하기
	 **************************************************************************/
	public void writeNoticeBoard(String flag,String ip_addr) throws Exception  
	{	
		String subject = "";						//공문제목
		String od_id = "", app_id = "";				//공문관리번호,결재승인 관리번호
		String user_id = "";						//작성자 사번
		
		String content = "",writer = "";			//게시판 내용,작성자
		int thread = 0, pos = 0;					//게시판 thread, pos
		String category = "";						//게시판 category
		String passwd = "";							//게시판 비밀번호

		//1. 공문종류별 필요정보 읽기 [게시판에 저장할 작성자,제목,본문내용 만들기]
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(flag.equals("ODT")) {				//공지공문
			if(od_list.hasNext()){
				table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

				subject = table.getSubject();	//제목
				od_id = table.getId();			//관리번호
				app_id = table.getAppId();		//결재 관리번호
				user_id = table.getUserId();
				writer = table.getAcName()+"/"+table.getUserName()+"/"+table.getUserId();
//				content = "ODT|"+od_id+"|"+app_id;
				content = "OfficialDocumentServlet?mode=OFD_A&id="+od_id+"&doc_id="+app_id;
			}
		} else if(flag.equals("ODR")) {			//사외공문
			if(od_list.hasNext()){
				table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

				subject = table.getSubject();	//제목
				od_id = table.getId();			//관리번호
				user_id = table.getUserId();
				writer = table.getAcName()+"/"+table.getUserName()+"/"+table.getUserId();
//				content = "ODR|"+od_id;
				content = "OutDocumentRecServlet?mode=ODR_V&id="+od_id;
			}
		}

		//2. 게시판의 일련번호 구하기
		thread = getThread();
		pos = thread;

		//3. category번호 구하기
		category = getCategory(flag);

		//4. 작성자 비밀번호 구하기
		passwd = getPasswd(user_id);

		//게시판에 저장하기
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
		//System.out.println("게시판 input : " + input );

	}

	/***************************************************************************
	 * 게시판의 일련번호 구하기
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
	 * category번호 구하기
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

		if(flag.equals("ODT")) {				//공지공문
			if(ctno.indexOf("공지공문") != -1) {
				StringTokenizer cat = new StringTokenizer(ctno,"|");
				int n = 0;
				while(cat.hasMoreTokens()) {
					String name = cat.nextToken();
					if(name.equals("공지공문")) rtn = Integer.toString(n);
					n++;
				}
			}
		} else if(flag.equals("ODR")) {			//사외공문
			if(ctno.indexOf("사외공문") != -1) {
				StringTokenizer cat = new StringTokenizer(ctno,"|");
				int n = 0;
				while(cat.hasMoreTokens()) {
					String name = cat.nextToken();
					if(name.equals("사외공문")) rtn = Integer.toString(n);
					n++;
				}
			}
		}

		return rtn;
	}

	/***************************************************************************
	 * 작성자 비밀번호 구하기
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
	// ID을 구하는 메소드
	******************************************************************************/
	private String getID()
	{
		String ID;
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		String y = first.format(now);
		String s = last.format(now);
		nmf.setFormat("000");		//일련번호 출력 형식(6자리)		
		ID = y + nmf.toDigits(Integer.parseInt(s));
		return ID;
	}

}	

