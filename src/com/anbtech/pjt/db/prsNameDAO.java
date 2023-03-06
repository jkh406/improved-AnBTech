package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class prsNameDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public prsNameDAO(Connection con) 
	{
		this.con = con;
	}

	public prsNameDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기 (전사공통)
	//*******************************************************************/
	public int getAllTotalCount(String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM prs_name where type='P'";
		query += " and ("+sItem+" like '%"+sWord+"%')"; 
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
	//*******************************************************************
	//	총 수량 파악하기 (부서공통)
	//*******************************************************************/
	public int getDivTotalCount(String div_code,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM prs_name where type='"+div_code+"'";
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
	// 전사공통 LIST QUERY하기 (전체 LIST읽기)
	//*******************************************************************/	
	public ArrayList getPrsnameAllList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//전사공통 프로세스 권한이 있는지 판단하기
		String prs_mgr = "N";	//Y:있음, N:없음
		prs_mgr = checkPrsMgr(login_id);

		//총갯수 구하기
		total_cnt = getAllTotalCount(sItem,sWord);
			
		//query문장 만들기
		query = "SELECT * FROM prs_name where type='P'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by prs_code asc"; 
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
				table = new prsCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);
				String prs_code = rs.getString("prs_code"); 
				table.setPrsCode(prs_code);							
				table.setPrsName(rs.getString("prs_name"));													
				table.setType(rs.getString("type"));
				
				//프로세스코드가 하부구조[process]에서 사용되었는지 검사하기
				String use = usePrsnameAtProcess(prs_code,"P");
				
				//수정 or 삭제가능 표시 [login_id가 작성자인 경우만 가능]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[삭제]</a>";
				}
				else {
					subMod = "<font color=darkred>표준 프로세스가 구성되어 편집불가</font>";
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
	// 부서공통 LIST QUERY하기 (전체 LIST읽기)
	//*******************************************************************/	
	public ArrayList getPrsnameDivList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//login id의 부서코드 찾기
		String login_division = searchAcId (login_id);

		//부서공통 프로세스 권한이 있는지 판단하기
		String prs_mgr = "N";	//Y:있음, N:없음
		prs_mgr = checkPrsMgr(login_id,login_division);

		//총갯수 구하기
		total_cnt = getDivTotalCount(login_division,sItem,sWord);
			
		//query문장 만들기
		query = "SELECT * FROM prs_name where type='"+login_division+"'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by prs_code asc"; 
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
				table = new prsCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);
				String prs_code = rs.getString("prs_code"); 
				table.setPrsCode(prs_code);							
				table.setPrsName(rs.getString("prs_name"));													
				table.setType(rs.getString("type"));
				
				//프로세스코드가 하부구조[process]에서 사용되었는지 검사하기
				String use = usePrsnameAtProcess(prs_code,login_division);
				
				//수정 or 삭제가능 표시 [login_id가 작성자인 경우만 가능]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[삭제]</a>";
				}
				else {
					subMod = "<font color=darkred>표준 프로세스가 구성되어 편집불가</font>";
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
	//	프로세스이름이 표준 프로세스[prs_process]에 사용했는지 판단하기
	//*******************************************************************/	
	public String usePrsnameAtProcess (String prs_code,String type) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT COUNT(*) FROM prs_process ";
		query += "where parent_node = '"+prs_code+"' and type = '"+type+"'";
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
	//	전사공통 프로세스 관리자여부 판단하기
	//*******************************************************************/	
	public String checkPrsMgr (String login_id) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where keyname = 'PRS_MGR' and owner like '%"+login_id+"%'";
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
	//	부서공통 프로세스 관리자여부 판단하기
	//*******************************************************************/	
	public String checkPrsMgr (String login_id,String login_div) throws Exception
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
	// 해당phase QUERY하기 (개별 읽기)
	//*******************************************************************/	
	public ArrayList getPrsnameRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM prs_name where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new prsCodeTable();
					
				table.setPid(rs.getString("pid"));
				table.setPrsCode(rs.getString("prs_code"));							
				table.setPrsName(rs.getString("prs_name"));													
				table.setType(rs.getString("type"));							

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* 프로세스이름 내용 저장하기 
	*******************************************************************/
	public void inputPrsname(String pid,String prs_code,String prs_name,String type) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//프로세스 이름테이블에 저장하기
		input = "INSERT INTO prs_name(pid,prs_code,prs_name,type) values('";
		input += pid+"','"+prs_code+"','"+prs_name+"','"+type+"')";
		stmt.executeUpdate(input);

		//프로세스 구성테이블에 저장하기
		input = "INSERT INTO prs_process(pid,prs_code,parent_node,child_node,node_name,level_no,type) values('";
		input += pid+"','"+prs_code+"','0','"+prs_code+"','"+prs_name+"','0','"+type+"')";
		stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*******************************************************************
	* 프로세스이름 내용 수정하기 
	*******************************************************************/
	public boolean updatePrsname(String pid,String prs_code,String prs_name,String type) throws Exception
	{
		boolean rtn = false;
		String update = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		prs_code = prs_code.toUpperCase();		//대문자로 바꾸기

		//프로세스코드가 중복되는지 검사하기
		int code_cnt = 0;
		String query = "SELECT COUNT(*) FROM prs_name where prs_code='"+prs_code+"' and type ='"+type+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) code_cnt = rs.getInt(1);

		//코드와 이름을 바꿔준다.
		if(code_cnt == 0) {
			//프로세스 이름테이블에 수정하기
			update = "UPDATE prs_name set prs_code='"+prs_code+"',prs_name='"+prs_name+"',type='"+type;
			update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);

			//프로세스 구성테이블에 수정하기
			update = "UPDATE prs_process set prs_code='"+prs_code+"',child_node='"+prs_code+"',node_name='"+prs_name;
			update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			rtn = true;
		}
		//이름만 바꿔준다.
		else if(code_cnt == 1) {
			//프로세스 이름테이블에 수정하기
			update = "UPDATE prs_name set prs_name='"+prs_name+"',type='"+type;
			update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);

			//프로세스 구성테이블에 수정하기
			update = "UPDATE prs_process set child_node='"+prs_code+"',node_name='"+prs_name;
			update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			rtn = true;
		}
		stmt.close();
		rs.close();
		return rtn;
	}

	/*******************************************************************
	* 프로세스이름 내용 삭제하기 
	*******************************************************************/
	public boolean deletePrsname(String pid) throws Exception
	{
		boolean rtn = false;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		String delete = "";

		stmt = con.createStatement();

		//프로세스 이름테이블에서 프로세스 코드 구하기
		String prs_code = "";
		query = "SELECT prs_code FROM prs_name where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) prs_code = rs.getString("prs_code");
		
		//프로세스 구성테이블에 구성되었는지 판단하여[자코드있음] 삭제여부 진행
		query = "SELECT COUNT(*) FROM prs_process where prs_code='"+prs_code+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		//삭제여부 진행하기
		if(cnt < 2) {
			//프로세스 이름테이블에 삭제하기
			delete = "DELETE from prs_name where pid='"+pid+"'";
			stmt.executeUpdate(delete);

			//프로세스 구성테이블에 삭제하기
			delete = "DELETE from prs_process where pid='"+pid+"'";
			stmt.executeUpdate(delete);
			
			rtn = true;
		} 

		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// PRSNAME CODE구하기
	// type : A:전사, D:부서 
	//*******************************************************************/	
	public String getPrsnameCode (String login_id,String div_code,String tag) throws Exception
	{
		//변수 초기화
		String rtn = "";			//리턴데이터
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//권한찾기
		String prs_mgr = "N";
		if(tag.equals("A")) prs_mgr = checkPrsMgr (login_id);					//전사공통
		else if(tag.equals("D")) prs_mgr = checkPrsMgr(login_id,div_code);		//부서공통

		//신규인지 아닌지를 판단하기위해 수량을 찾는다.
		int cnt = 0;
		if(tag.equals("A") && prs_mgr.equals("Y")) {
			cnt = getAllTotalCount("prs_code","");
			if(cnt == 0) { rtn = "P001"; return rtn; }
		}
		else if(tag.equals("D") && prs_mgr.equals("Y")) {
			cnt = getDivTotalCount(div_code,"prs_code","");
			if(cnt == 0) { rtn = "D001"; return rtn; }
		}

		//query문장 만들기
		stmt = con.createStatement();
		if(tag.equals("A") && prs_mgr.equals("Y"))
			query = "SELECT prs_code FROM prs_name where type='P' order by prs_code desc";		
		else if(tag.equals("D") && prs_mgr.equals("Y"))
			query = "SELECT prs_code FROM prs_name where type='"+div_code+"' order by prs_code desc";	
		else return rtn;

		//데이터 담기
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getString("prs_code");

		//ph_code에 +1
		if(rtn.length() > 0) {
			com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("000");
			String no = nft.toDigits(Integer.parseInt(rtn.substring(1,4))+1);
			if(tag.equals("A")) rtn = "P"+no;
			else if(tag.equals("D")) rtn = "D"+no;
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
