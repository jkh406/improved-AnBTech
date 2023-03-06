package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtChangeSchDAO
{
	private Connection con;
	com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();	//파일로 담기[메일]
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//날자처리

	private String[][] item = null;				//프로세스정보를 배열로 담기
	private int an = 0;							//items의 배열 증가
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtChangeSchDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtChangeSchDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기 [해당과제/해당노드의 전체수량]
	//*******************************************************************/
	private int getTotalCount(String pjt_code,String node_code,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_changesch where pjt_code='"+pjt_code+"' and node_code='"+node_code+"'";
		query += " and "+sItem+" like '%"+sWord+"%'"; 
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
	
	//*******************************************************************
	//	총 페이지 수 구하기
	//*******************************************************************/
	public int getTotalPage() 
	{
		return this.total_page;
	}

	//*******************************************************************
	//	현 페이지 수 구하기
	//*******************************************************************/
	public int getCurrentPage() 
	{
		return this.current_page;
	}

	//*******************************************************************
	// 해당과제/해당노드의 실적이력정보
	//*******************************************************************/	
	public ArrayList getChangeSchList (String login_id,String pjt_code,String node_code,String sItem,String sWord,
		String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		total_cnt = getTotalCount(pjt_code,node_code,sItem,sWord);

		//금일일자 구하기
		String todate = anbdt.getDate();
			
		//query문장 만들기
		query = "SELECT * FROM pjt_changesch where pjt_code='"+pjt_code+"' and node_code='"+node_code+"'";
		query += " and "+sItem+" like '%"+sWord+"%' order by in_date desc"; 
		rs = stmt.executeQuery(query);

		//페이지 정수로 바꿔주기
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//출력할 페이지

		//전체 page 구하기
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//페이지에 따른 query 계산하기
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//페이지 skip 하기 (해당되지 않는 페이지의 내용)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//데이터 담기
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new projectTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));	
				table.setNodeName(rs.getString("node_name"));

				String user_id = rs.getString("user_id");
				table.setUserId(user_id);
				
				table.setUserName(rs.getString("user_name"));

				String in_date = rs.getString("in_date");
				table.setInDate(in_date);

				table.setChgNote(rs.getString("chg_note"));

				//보기,수정,삭제가능 표시 [단,수정/삭제는 당일에 한해 작성자만이 가능함]
				String subMod="",subView="";
				if(user_id.equals(login_id) && todate.equals(in_date)) {
					subView = "<a href=\"javascript:changeView('"+pid+"');\">보기</a>";
					subMod = "<a href=\"javascript:changeModify('"+pid+"');\">수정</a>";
				} 
				else {
					subView = "<a href=\"javascript:changeView('"+pid+"');\">보기</a>";
				}
				table.setView(subView);
				table.setModify(subMod);
				
				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제 해당노드중 지정내용상세보기
	//*******************************************************************/	
	public ArrayList getChangeSchRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_changesch where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));	
				table.setNodeName(rs.getString("node_name"));
				table.setUserId(rs.getString("user_id"));	
				table.setUserName(rs.getString("user_name"));
				table.setInDate(rs.getString("in_date"));
				String chg_note = rs.getString("chg_note"); if(chg_note == null) chg_note = "";
				table.setChgNote(chg_note);	
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* 일정변경 내용 입력하기
	*******************************************************************/
	public void inputChangeSch(String pjt_code,String pjt_name,String node_code,String node_name,
		String user_id,String user_name,String chg_note) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//관리번호 찾기
		String pid = getID();
		String in_date = anbdt.getDate();

		input = "INSERT INTO pjt_changesch(pid,pjt_code,pjt_name,node_code,node_name,user_id,";
		input += "user_name,in_date,chg_note) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+node_code+"','"+node_name+"','"+user_id+"','";
		input += user_name+"','"+in_date+"','"+chg_note+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}
	/*******************************************************************
	* 일정변경내용 수정하기
	*******************************************************************/
	public void updateChangeSch(String pid,String chg_note) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		update = "UPDATE pjt_changesch set chg_note='"+chg_note+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}
	/***************************************************************************
	 * ID을 구하는 메소드
	 **************************************************************************/
	public String getID()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}
}



