package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtCodeDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtCodeDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtCodeDAO() 
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

		query = "SELECT COUNT(*) FROM prs_project where type='P'";
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

		query = "SELECT COUNT(*) FROM prs_project where type='"+div_code+"'";
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
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//총갯수 구하기
		total_cnt = getAllTotalCount(sItem,sWord);
			
		//query문장 만들기
		query = "SELECT * FROM prs_project where type='P'";	
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
				table = new pjtCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				
				//수정 or 삭제가능 표시 [login_id가 작성자인 경우만 가능]
				String subMod="",subDel="";
				if(status.equals("S")) {				//미착수 상태
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[삭제]</a>";
				} else if(status.equals("3")) {		//과제 DROP상태
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[삭제]</a>";
				}
			
				table.setModify(subMod);
				table.setDelete(subDel);	
				
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
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//login id의 부서코드 찾기
		String login_division = searchAcId (login_id);

		//총갯수 구하기
		total_cnt = getDivTotalCount(login_division,sItem,sWord);
			
		//query문장 만들기
		query = "SELECT * FROM prs_project where type='"+login_division+"'";	
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
				table = new pjtCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				
				//수정 or 삭제가능 표시 [login_id가 작성자인 경우만 가능]
				String subMod="",subDel="";
				if(status.equals("S")) {				//미착수 상태
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[삭제]</a>";
				} else if(status.equals("3")) {		//과제 DROP상태
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[삭제]</a>";
				}
				
				table.setModify(subMod);
				table.setDelete(subDel);	
				
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
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query문장 만들기
		query = "SELECT * FROM prs_project where pjt_status='S' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new pjtCodeTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				
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
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query문장 만들기
		query = "SELECT * FROM prs_project where pjt_status='S' and type='"+login_division+"' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new pjtCodeTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}


	//*******************************************************************
	//	부서별 과제관리 관리자여부 판단하기
	//*******************************************************************/	
	public String checkPjtMgr (String login_id,String login_div) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where keyname = 'PJT_PML' and owner like '%"+login_id+"%' and div_code ='"+login_div+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		if(cnt > 0) rtn = "Y";
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;	
	}

	//*******************************************************************
	// 해당과제 QUERY하기 (개별 읽기)
	//*******************************************************************/	
	public ArrayList getProjectRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM prs_project where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new pjtCodeTable();
					
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));							

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* 과제코드 내용 저장하기 
	*******************************************************************/
	public void inputProject(String pid,String pjt_code,String pjt_name,String in_date,String mgr_id,String mgr_name,
		String type,String pjt_status) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//과제코드 테이블에 저장하기 : prs_project
		input = "INSERT INTO prs_project(pid,pjt_code,pjt_name,in_date,mgr_id,mgr_name,type,pjt_status) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+in_date+"','"+mgr_id+"','"+mgr_name+"','"+type+"','"+pjt_status+"')";
		stmt.executeUpdate(input);

		//과제상태 테이블에 저장하기 : pjt_status
		input = "INSERT INTO pjt_status(pid,pjt_code,pjt_name,type,pjt_status) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+type+"','"+pjt_status+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}

	/*******************************************************************
	* 과제코드 내용 수정하기 
	*******************************************************************/
	public void updateProject(String pid,String pjt_code,String pjt_name,String in_date,String mgr_id,String mgr_name,
		String type,String pjt_status) throws Exception
	{
		String update = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//현재 과제상태 알아보기
		String current_status = "";
		String query = "SELECT pjt_status FROM prs_project where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) current_status = rs.getString("pjt_status");

		//과제관리의 관련테이블의 과제명 수정여부 판단하기 [현:미착수 전에는 모든관련테이블에 수정적용됨]
		if(!current_status.equals("S")) {
			//기본정보
			update = "UPDATE pjt_general set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//인력정보
			update = "UPDATE pjt_member set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//일정정보
			update = "UPDATE pjt_schedule set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//실적정보
			update = "UPDATE pjt_event set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//경비정보
			update = "UPDATE pjt_cost set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//산출물정보
			update = "UPDATE pjt_document set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
		}
				
		//프로세스 이름테이블에 수정하기 [prs_project]
		update = "UPDATE prs_project set pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"',in_date='"+in_date;
		update += "',mgr_id='"+mgr_id+"',mgr_name='"+mgr_name+"',type='"+type+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//과제상태 이름테이블에 수정하기 [pjt_status]
		update = "UPDATE pjt_status set pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"',type='"+type;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* 과제코드 내용 삭제하기 
	*******************************************************************/
	public void deleteProject(String pid) throws Exception
	{
		boolean rtn = false;
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//프로세스 이름테이블에 삭제하기 : prs_project
		delete = "DELETE from prs_project where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		//과제상태 이름테이블에 삭제하기 : pjt_status
		delete = "DELETE from pjt_status where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}

	//*******************************************************************
	// 과제관리 CODE구하기 [부서코드+년도(YYYY)+"-"+일련번호(3)
	// tag = A:전사공통, D:부서공통
	//*******************************************************************/	
	public String getProjectCode (String div_code,String tag) throws Exception
	{
		//변수 초기화
		String rtn = "";			//리턴데이터
		String pjhd = "";			//과제코드 선두 구분 문자
		String pjmd = "";			//과제코드 중간 구분 문자
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//과제코드 선두 구분 문자
		if(tag.equals("A")) pjhd = "PJ";
		else pjhd = div_code;

		//과제코드 중간 구분 문자
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		pjmd = anbdt.getYear();

		//신규인지 아닌지를 판단하기위해 수량을 찾는다.
		int cnt = 0;
		if(tag.equals("A")) {
			cnt = getDivTotalCount("P","pjt_code","");
			if(cnt == 0) { rtn = pjhd+pjmd+"-"+"001"; return rtn; }
		} else { 
			cnt = getDivTotalCount(div_code,"pjt_code","");
			if(cnt == 0) { rtn = pjhd+pjmd+"-"+"001"; return rtn; }
		}
		
		//query문장 만들기
		stmt = con.createStatement();
		if(tag.equals("A"))
			query = "SELECT pjt_code FROM prs_project where type='P' order by pjt_code desc";	
		else 
			query = "SELECT pjt_code FROM prs_project where type='"+div_code+"' order by pjt_code desc";	
		
		//데이터 담기
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getString("pjt_code");

		//ph_code에 +1
		if(rtn.length() > 0) {
			com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("000");
			String no = nft.toDigits(Integer.parseInt(rtn.substring(rtn.lastIndexOf("-")+1,rtn.length()))+1);
			rtn = pjhd+pjmd+"-"+no;
		}
		
		stmt.close();
		rs.close();
		return rtn;
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

