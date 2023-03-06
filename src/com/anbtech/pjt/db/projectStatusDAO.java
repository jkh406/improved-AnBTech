package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class projectStatusDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public projectStatusDAO(Connection con) 
	{
		this.con = con;
	}

	public projectStatusDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기 (전체)
	//*******************************************************************/
	public int getAllTotalCount(String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_status where type='P'";
		query += " and ("+sItem+" like '%"+sWord+"%')"; 
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
	//*******************************************************************
	//	총 수량 파악하기 (부서별)
	//*******************************************************************/
	public int getDivTotalCount(String div_code,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_status where type='"+div_code+"'";
		query += " and ("+sItem+" like '%"+sWord+"%')"; 
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
	// 전사 LIST QUERY하기 (전사 LIST읽기)
	//*******************************************************************/	
	public ArrayList getAllProjectList (String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		total_cnt = getAllTotalCount(sItem,sWord);
			
		//query문장 만들기
		query = "SELECT * FROM pjt_status where type='P'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 
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
				String pjt_code = rs.getString("pjt_code");
				table.setPjtCode(pjt_code);							
				table.setPjtName(rs.getString("pjt_name"));	
				String in_date = rs.getString("in_date"); if(in_date == null) in_date = "";
				table.setInDate(in_date);	
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				table.setNote(rs.getString("note"));
				
				//수정 or 삭제가능 표시 [login_id가 작성자인 경우만 가능]
				String subMod="",subDel="",subView="";
				if(status.equals("3")) {		//과제 DROP상태
					subMod = "<a href=\"javascript:pjtChgStatus('"+pjt_code+"');\">[상태변경]</a>";
					subDel = "<a href=\"javascript:pjtDelete('"+pjt_code+"');\">[삭제]</a>";
					subView = "<a href=\"javascript:pjtView('"+pjt_code+"');\">[보기]</a>";
				} else {
					subMod = "<a href=\"javascript:pjtChgStatus('"+pjt_code+"');\">[상태변경]</a>";
					subView = "<a href=\"javascript:pjtView('"+pjt_code+"');\">[보기]</a>";
				}
				table.setModify(subMod);
				table.setDelete(subDel);
				table.setView(subView);
				
				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 부서별 LIST QUERY하기 (전체 LIST읽기)
	//*******************************************************************/	
	public ArrayList getDivProjectList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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

		//login id의 부서코드 찾기
		String login_division = searchAcId (login_id);

		//총갯수 구하기
		total_cnt = getDivTotalCount(login_division,sItem,sWord);
			
		//query문장 만들기
		query = "SELECT * FROM pjt_status where type='"+login_division+"'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 
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
				String pjt_code = rs.getString("pjt_code");
				table.setPjtCode(pjt_code);							
				table.setPjtName(rs.getString("pjt_name"));	
				String in_date = rs.getString("in_date"); if(in_date == null) in_date = "";
				table.setInDate(in_date);		
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				table.setNote(rs.getString("note"));
				
				//수정 or 삭제가능 표시 [login_id가 작성자인 경우만 가능]
				String subMod="",subDel="",subView="";
				if(status.equals("3")) {		//과제 DROP상태
					subMod = "<a href=\"javascript:pjtChgStatus('"+pjt_code+"');\">[상태변경]</a>";
					subDel = "<a href=\"javascript:pjtDelete('"+pjt_code+"');\">[삭제]</a>";
					subView = "<a href=\"javascript:pjtView('"+pjt_code+"');\">[보기]</a>";
				} else {
					subMod = "<a href=\"javascript:pjtChgStatus('"+pjt_code+"');\">[상태변경]</a>";
					subView = "<a href=\"javascript:pjtView('"+pjt_code+"');\">[보기]</a>";
				}
				table.setModify(subMod);
				table.setDelete(subDel);
				table.setView(subView);
				
				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 전체 미착수 과제 LIST : 과제정보 등록시 필요
	//*******************************************************************/	
	public ArrayList getAllStandbyProjectList () throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query문장 만들기
		query = "SELECT * FROM pjt_status where pjt_status='S' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				table.setNote(rs.getString("note"));
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 부서별 미착수 과제 LIST : 과제정보 등록시 필요
	//*******************************************************************/	
	public ArrayList getDivStandbyProjectList (String login_id) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//login id의 부서코드 찾기
		String login_division = searchAcId (login_id);

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query문장 만들기
		query = "SELECT * FROM pjt_status where pjt_status='S' and type='"+login_division+"' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				table.setNote(rs.getString("note"));
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제 QUERY하기 (개별 읽기)
	//*******************************************************************/	
	public ArrayList getProjectRead (String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_status where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
					
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));		
				table.setNote(rs.getString("note"));

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* 과제상태 수정하기 
	*******************************************************************/
	public void updateProject(String pjt_code,String pjt_status,String note,String in_date) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//기본정보 : pjt_general
		update = "UPDATE pjt_general set pjt_status='"+pjt_status+"' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);
				
		//프로세스 이름테이블에 수정하기 : prs_project
		update = "UPDATE prs_project set pjt_status='"+pjt_status+"' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);

		//과제상태 이름테이블에 수정하기 : pjt_status
		update = "UPDATE pjt_status set pjt_status='"+pjt_status+"',note='"+note+"',in_date='"+in_date+"' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*******************************************************************
	* DROP과제 삭제하기 
	*******************************************************************/
	public void deleteProject(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//과제명정보 : prs_project
		delete = "DELETE from prs_project where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//과제상태정보 : pjt_status
		delete = "DELETE from pjt_status where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//기본정보 : pjt_general
		delete = "DELETE from pjt_general where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//인력정보 : pjt_member
		delete = "DELETE from pjt_member where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//일정정보 : pjt_schedule
		delete = "DELETE from pjt_schedule where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//일정변경정보 : pjt_changesch
		delete = "DELETE from pjt_changesch where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//문제점 관리 : pjt_note
		delete = "DELETE from pjt_note where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//이슈 관리 : pjt_issue
		delete = "DELETE from pjt_issue where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//비용관리 : pjt_cost
		delete = "DELETE from pjt_cost where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//산출물 관리 : pjt_document
		delete = "DELETE from pjt_document where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}

	//*******************************************************************
	//	해당사번 부서코드 리턴하기
	//*******************************************************************/	
	private String searchAcId (String login_id) throws Exception
	{
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT b.ac_code FROM user_table a,class_table b ";
		query += "where a.id ='"+login_id+"' and a.ac_id = b.ac_id";
		rs = stmt.executeQuery(query);
		if(rs.next())	rtn = rs.getString("ac_code");
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;
	}
}

