package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class prsPhaseDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public prsPhaseDAO(Connection con) 
	{
		this.con = con;
	}

	public prsPhaseDAO() 
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

		query = "SELECT COUNT(*) FROM prs_phase where type='P'";
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

		query = "SELECT COUNT(*) FROM prs_phase where type='"+div_code+"'";
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
	public ArrayList getPhaseAllList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		query = "SELECT * FROM prs_phase where type='P'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by ph_code asc"; 
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
				String ph_code = rs.getString("ph_code");
				table.setPhCode(ph_code);							
				table.setPhName(rs.getString("ph_name"));													
				table.setType(rs.getString("type"));		
				
				//phase코드가 하부구조[step]에서 사용되었는지 검사하기
				String use = usePhaseAtStep(ph_code,"P");

				//수정 or 삭제가능 표시 [login_id가 작성자인 경우만 가능]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[삭제]</a>";
				}
				else {
					subMod = "<font color=darkred>STEP단계에서 구성되어 편집불가</font>";
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
	public ArrayList getPhaseDivList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		query = "SELECT * FROM prs_phase where type='"+login_division+"'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by ph_code asc"; 
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
				String ph_code = rs.getString("ph_code");
				table.setPhCode(ph_code);							
				table.setPhName(rs.getString("ph_name"));													
				table.setType(rs.getString("type"));	
				
				//phase코드가 하부구조[step]에서 사용되었는지 검사하기
				String use = usePhaseAtStep(ph_code,login_division);
				
				//수정 or 삭제가능 표시 [login_id가 작성자인 경우만 가능]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[삭제]</a>";
				}
				else {
					subMod = "<font color=darkred>STEP단계에서 구성되어 편집불가</font>";
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
	//	PHASE가 하부구조[STEP]에 사용했는지 판단하기
	//*******************************************************************/	
	public String usePhaseAtStep (String ph_code,String type) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT COUNT(*) FROM prs_step ";
		query += "where ph_code = '"+ph_code+"' and type = '"+type+"'";
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
	public ArrayList getPhaseRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM prs_phase where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new prsCodeTable();
					
				table.setPid(rs.getString("pid"));
				table.setPhCode(rs.getString("ph_code"));							
				table.setPhName(rs.getString("ph_name"));													
				table.setType(rs.getString("type"));							

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* PHASE 내용 저장하기 
	*******************************************************************/
	public void inputPhase(String pid,String ph_code,String ph_name,String type) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String input = "INSERT INTO prs_phase(pid,ph_code,ph_name,type) values('";
			input += pid+"','"+ph_code+"','"+ph_name+"','"+type+"')";
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*******************************************************************
	* PHASE 내용 수정하기 
	*******************************************************************/
	public boolean updatePhase(String pid,String ph_code,String ph_name,String type) throws Exception
	{
		boolean rtn = false;
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		ph_code = ph_code.toUpperCase();	//대문자로 바꾸기

		//프로세스코드가 중복되는지 검사하기
		String query = "SELECT COUNT(*) FROM prs_phase where ph_code='"+ph_code+"' and type ='"+type+"'";
		query += " and pid != '"+pid+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		//수정하기
		if(cnt == 0) {
			String update = "UPDATE prs_phase set ph_code='"+ph_code+"',ph_name='"+ph_name+"',type='"+type;
				update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			rtn = true;
		}
		
		stmt.close();
		rs.close();
		return rtn;
	}

	/*******************************************************************
	* PHASE 내용 삭제하기 
	*******************************************************************/
	public boolean deletePhase(String pid) throws Exception
	{
		boolean rtn = false;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		String delete = "";

		stmt = con.createStatement();

		//프로세스 이름테이블에서 프로세스 코드 구하기
		String ph_code = "",type="";
		query = "SELECT ph_code,type FROM prs_phase where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			ph_code = rs.getString("ph_code");
			type = rs.getString("type");
		}
		
		//프로세스 구성테이블에 구성되었는지 판단하여[자코드있음] 삭제여부 진행
		query = "SELECT COUNT(*) FROM prs_step where ph_code='"+ph_code+"' and type='"+type+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		//삭제여부 진행하기
		if(cnt < 2) {
			delete = "DELETE from prs_phase where pid='"+pid+"'";
			stmt.executeUpdate(delete);
			rtn = true;
		} 

		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// PHASE CODE구하기
	// type : A:전사, D:부서 
	//*******************************************************************/	
	public String getPhaseCode (String login_id,String div_code,String tag) throws Exception
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
			cnt = getAllTotalCount("ph_code","");
			if(cnt == 0) { rtn = "S01"; return rtn; }
		}
		else if(tag.equals("D") && prs_mgr.equals("Y")) {
			cnt = getDivTotalCount(div_code,"ph_code","");
			if(cnt == 0) { rtn = "D01"; return rtn; }
		}

		//query문장 만들기
		stmt = con.createStatement();
		if(tag.equals("A") && prs_mgr.equals("Y"))
			query = "SELECT ph_code FROM prs_phase where type='P' order by ph_code desc";		
		else if(tag.equals("D") && prs_mgr.equals("Y"))
			query = "SELECT ph_code FROM prs_phase where type='"+div_code+"' order by ph_code desc";	
		else return rtn;

		//데이터 담기
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getString("ph_code");
	
		//ph_code에 +1
		if(rtn.length() > 0) {
			com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("00");
			String no = nft.toDigits(Integer.parseInt(rtn.substring(1,3))+1);
			if(tag.equals("A")) rtn = "S"+no;
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
