package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtGeneralDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtGeneralDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtGeneralDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기 (전체)
	//*******************************************************************/
	public int getAllTotalCount(String pjtWord,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_general where prs_type='P'";
		query += " and pjt_status like '%"+pjtWord+"%'";
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
	public int getDivTotalCount(String div_code,String pjtWord,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_general where prs_type='"+div_code+"'";
		query += " and pjt_status like '%"+pjtWord+"%'";
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
	public ArrayList getAllGeneralList (String login_id,String pjtWord,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		
		//과제관리자 인지 판단하기
		String pjt_mgr = "N";
		pjt_mgr = checkPjtMgr (login_id);

		//총갯수 구하기
		total_cnt = getAllTotalCount(pjtWord,sItem,sWord);
			
		//query문장 만들기
		query = "SELECT * FROM pjt_general where prs_type='P'";	
		query += " and pjt_status like '%"+pjtWord+"%'";
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
				table.setOwner(rs.getString("owner"));
				table.setInDate(rs.getString("in_date"));
				table.setPjtMbrId(rs.getString("pjt_mbr_id"));
				table.setPjtClass(rs.getString("pjt_class"));
				table.setPjtTarget(rs.getString("pjt_target"));
				table.setMgtPlan(rs.getString("mgt_plan"));
				table.setParentCode(rs.getString("parent_code"));
				table.setMbrExp(rs.getInt("mbr_exp"));
				table.setCostExp(rs.getString("cost_exp"));
				table.setPlanStartDate(rs.getString("plan_start_date"));
				table.setPlanEndDate(rs.getString("plan_end_date"));
				table.setChgStartDate(rs.getString("chg_start_date"));
				table.setChgEndDate(rs.getString("chg_end_date"));
				table.setRstStartDate(rs.getString("rst_start_date"));
				table.setRstEndDate(rs.getString("rst_end_date"));
				table.setPrsCode(rs.getString("prs_code"));
				table.setPrsType(rs.getString("prs_type"));
				table.setPjtDesc(rs.getString("pjt_desc"));
				table.setPjtSpec(rs.getString("pjt_spec"));

				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				table.setFlag(rs.getString("flag"));
				table.setPlanLabor(rs.getString("plan_labor"));
				table.setPlanSample(rs.getString("plan_sample"));
				table.setPlanMetal(rs.getString("plan_metal"));
				table.setPlanMup(rs.getString("plan_mup"));
				table.setPlanOversea(rs.getString("plan_oversea"));
				table.setPlanPlant(rs.getString("plan_plant"));
				table.setResultLabor(rs.getString("result_labor"));
				table.setResultSample(rs.getString("result_sample"));
				table.setResultMetal(rs.getString("result_metal"));
				table.setResultMup(rs.getString("result_mup"));
				table.setResultOversea(rs.getString("result_oversea"));
				table.setResultPlant(rs.getString("result_plant"));
				
				//수정 or 삭제가능 표시 [login_id가 과제관리자인 경우만 가능]
				String subMod="",subDel="";
				if(status.equals("S") && pjt_mgr.equals("Y")) {				//미착수 상태
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pjt_code+"');\">[삭제]</a>";
				} else if(status.equals("3") && pjt_mgr.equals("Y")) {		//과제 DROP상태
					subDel = "<a href=\"javascript:contentDelete('"+pjt_code+"');\">[삭제]</a>";
				} else if(pjt_mgr.equals("Y")) {							//기타
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
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
	public ArrayList getDivGeneralList (String login_id,String pjtWord,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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

		//과제관리자 인지 판단하기
		String pjt_mgr = "N";
		pjt_mgr = checkPjtMgr (login_id);

		//login id의 부서코드 찾기
		String login_division = searchAcId (login_id);

		//총갯수 구하기
		total_cnt = getDivTotalCount(login_division,pjtWord,sItem,sWord);
			
		//query문장 만들기
		query = "SELECT * FROM pjt_general where prs_type='"+login_division+"'";	
		query += " and pjt_status like '%"+pjtWord+"%'";
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
				table.setOwner(rs.getString("owner"));
				table.setInDate(rs.getString("in_date"));

				String mbr_id = rs.getString("pjt_mbr_id");
				table.setPjtMbrId(mbr_id);
				table.setPjtClass(rs.getString("pjt_class"));
				table.setPjtTarget(rs.getString("pjt_target"));
				table.setMgtPlan(rs.getString("mgt_plan"));
				table.setParentCode(rs.getString("parent_code"));
				table.setMbrExp(rs.getInt("mbr_exp"));
				table.setCostExp(rs.getString("cost_exp"));
				table.setPlanStartDate(rs.getString("plan_start_date"));
				table.setPlanEndDate(rs.getString("plan_end_date"));
				table.setChgStartDate(rs.getString("chg_start_date"));
				table.setChgEndDate(rs.getString("chg_end_date"));
				table.setRstStartDate(rs.getString("rst_start_date"));
				table.setRstEndDate(rs.getString("rst_end_date"));
				table.setPrsCode(rs.getString("prs_code"));
				table.setPrsType(rs.getString("prs_type"));
				table.setPjtDesc(rs.getString("pjt_desc"));
				table.setPjtSpec(rs.getString("pjt_spec"));

				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				table.setFlag(rs.getString("flag"));
				table.setPlanLabor(rs.getString("plan_labor"));
				table.setPlanSample(rs.getString("plan_sample"));
				table.setPlanMetal(rs.getString("plan_metal"));
				table.setPlanMup(rs.getString("plan_mup"));
				table.setPlanOversea(rs.getString("plan_oversea"));
				table.setPlanPlant(rs.getString("plan_plant"));
				table.setResultLabor(rs.getString("result_labor"));
				table.setResultSample(rs.getString("result_sample"));
				table.setResultMetal(rs.getString("result_metal"));
				table.setResultMup(rs.getString("result_mup"));
				table.setResultOversea(rs.getString("result_oversea"));
				table.setResultPlant(rs.getString("result_plant"));
				
				//수정 or 삭제가능 표시 [login_id가 과제PM/PL인 경우만 가능]
				String subMod="",subDel="";
				if(status.equals("S") && pjt_mgr.equals("Y")) {			//미착수 상태
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pjt_code+"');\">[삭제]</a>";
				} else if(status.equals("3") && pjt_mgr.equals("Y")) {	//과제 DROP상태
					subDel = "<a href=\"javascript:contentDelete('"+pjt_code+"');\">[삭제]</a>";
				} else if(pjt_mgr.equals("Y")){							//기타
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
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
	// 전체 미착수 과제 LIST : 과제정보 신규등록시 필요
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
		query = "SELECT * FROM prs_project where pjt_status='S' and type='P' order by pjt_code asc";	
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
	// 전체 미착수,과제등록중 과제 LIST : 과제정보 수정시 필요
	//*******************************************************************/	
	public ArrayList getAllReadyProjectList () throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query문장 만들기
		query = "SELECT * FROM prs_project where (pjt_status='S' or pjt_status='0') and type='P' order by pjt_code asc";	
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
	// 부서별 미착수 과제 LIST : 과제정보 신규등록시 필요
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
	// 부서별 미착수,과제등록중 과제 LIST : 과제정보 수정시 필요
	//*******************************************************************/	
	public ArrayList getDivReadyProjectList (String login_id) throws Exception
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
		query = "SELECT * FROM prs_project where (pjt_status='S' or pjt_status='0') and type='"+login_division+"' order by pjt_code asc";	
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
	// 메인과제명 찾기 : 과제정보 등록시 필요
	//*******************************************************************/	
	public ArrayList getMainProjectList () throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query문장 만들기
		query = "SELECT * FROM prs_project where pjt_status='0' or pjt_status='1' order by pjt_code asc";	
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
	//	과제관리자 관리자여부 판단하기
	//*******************************************************************/	
	public String checkPjtMgr (String login_id) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where keyname = 'PJT_MGR' and owner like '%"+login_id+"%'";
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
	public ArrayList getGeneralRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_general where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setOwner(rs.getString("owner"));
				table.setInDate(rs.getString("in_date"));
				table.setPjtMbrId(rs.getString("pjt_mbr_id"));
				table.setPjtClass(rs.getString("pjt_class"));
				table.setPjtTarget(rs.getString("pjt_target"));
				table.setMgtPlan(rs.getString("mgt_plan"));
				table.setParentCode(rs.getString("parent_code"));
				table.setMbrExp(rs.getInt("mbr_exp"));
				table.setCostExp(sp.getMoneyFormat(rs.getString("cost_exp"),""));
				table.setPlanStartDate(rs.getString("plan_start_date"));
				table.setPlanEndDate(rs.getString("plan_end_date"));
				table.setChgStartDate(rs.getString("chg_start_date"));
				table.setChgEndDate(rs.getString("chg_end_date"));
				table.setRstStartDate(rs.getString("rst_start_date"));
				table.setRstEndDate(rs.getString("rst_end_date"));
				table.setPrsCode(rs.getString("prs_code"));
				table.setPrsType(rs.getString("prs_type"));
				table.setPjtDesc(rs.getString("pjt_desc"));
				table.setPjtSpec(rs.getString("pjt_spec"));
				table.setPjtStatus(rs.getString("pjt_status"));
				table.setFlag(rs.getString("flag"));
				table.setPlanLabor(sp.getMoneyFormat(rs.getString("plan_labor"),""));
				table.setPlanSample(sp.getMoneyFormat(rs.getString("plan_sample"),""));
				table.setPlanMetal(sp.getMoneyFormat(rs.getString("plan_metal"),""));
				table.setPlanMup(sp.getMoneyFormat(rs.getString("plan_mup"),""));
				table.setPlanOversea(sp.getMoneyFormat(rs.getString("plan_oversea"),""));
				table.setPlanPlant(sp.getMoneyFormat(rs.getString("plan_plant"),""));
				table.setResultLabor(sp.getMoneyFormat(rs.getString("result_labor"),""));
				table.setResultSample(sp.getMoneyFormat(rs.getString("result_sample"),""));
				table.setResultMetal(sp.getMoneyFormat(rs.getString("result_metal"),""));
				table.setResultMup(sp.getMoneyFormat(rs.getString("result_mup"),""));
				table.setResultOversea(sp.getMoneyFormat(rs.getString("result_oversea"),""));
				table.setResultPlant(sp.getMoneyFormat(rs.getString("result_plant"),""));
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제 QUERY하기 (개별 읽기 : 과제코드)
	//*******************************************************************/	
	public ArrayList getPjtRead (String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setOwner(rs.getString("owner"));
				table.setInDate(rs.getString("in_date"));
				table.setPjtMbrId(rs.getString("pjt_mbr_id"));
				table.setPjtClass(rs.getString("pjt_class"));
				table.setPjtTarget(rs.getString("pjt_target"));
				table.setMgtPlan(rs.getString("mgt_plan"));
				table.setParentCode(rs.getString("parent_code"));
				table.setMbrExp(rs.getInt("mbr_exp"));
				table.setCostExp(sp.getMoneyFormat(rs.getString("cost_exp"),""));
				table.setPlanStartDate(rs.getString("plan_start_date"));
				table.setPlanEndDate(rs.getString("plan_end_date"));
				table.setChgStartDate(rs.getString("chg_start_date"));
				table.setChgEndDate(rs.getString("chg_end_date"));
				table.setRstStartDate(rs.getString("rst_start_date"));
				table.setRstEndDate(rs.getString("rst_end_date"));
				table.setPrsCode(rs.getString("prs_code"));
				table.setPrsType(rs.getString("prs_type"));
				table.setPjtDesc(rs.getString("pjt_desc"));
				table.setPjtSpec(rs.getString("pjt_spec"));
				table.setPjtStatus(rs.getString("pjt_status"));
				table.setFlag(rs.getString("flag"));
				table.setPlanLabor(sp.getMoneyFormat(rs.getString("plan_labor"),""));
				table.setPlanSample(sp.getMoneyFormat(rs.getString("plan_sample"),""));
				table.setPlanMetal(sp.getMoneyFormat(rs.getString("plan_metal"),""));
				table.setPlanMup(sp.getMoneyFormat(rs.getString("plan_mup"),""));
				table.setPlanOversea(sp.getMoneyFormat(rs.getString("plan_oversea"),""));
				table.setPlanPlant(sp.getMoneyFormat(rs.getString("plan_plant"),""));
				table.setResultLabor(sp.getMoneyFormat(rs.getString("result_labor"),""));
				table.setResultSample(sp.getMoneyFormat(rs.getString("result_sample"),""));
				table.setResultMetal(sp.getMoneyFormat(rs.getString("result_metal"),""));
				table.setResultMup(sp.getMoneyFormat(rs.getString("result_mup"),""));
				table.setResultOversea(sp.getMoneyFormat(rs.getString("result_oversea"),""));
				table.setResultPlant(sp.getMoneyFormat(rs.getString("result_plant"),""));
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* 과제기본정보 내용 저장하기 
	*******************************************************************/
	public void inputGeneral(String pid,String pjt_code,String pjt_name,String owner,String in_date,String pjt_mbr_id,
		String pjt_class,String pjt_target,String mgt_plan,String parent_code,String mbr_exp,String cost_exp,
		String plan_start_date,String plan_end_date,String prs_code,String prs_type,String pjt_desc,String pjt_spec,
		String pjt_status,String plan_labor,String plan_sample,String plan_metal,String plan_mup,String plan_oversea,
		String plan_plant) throws Exception
	{
		String input = "",update="";
		Statement stmt = null;
		stmt = con.createStatement();

		//1.기본정보(pjt_general) 저장하기[전체]
		input = "INSERT INTO pjt_general(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,";
		input +="pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,";
		input += "prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,";
		input += "plan_mup,plan_oversea,plan_plant) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+owner+"','"+in_date+"','"+pjt_mbr_id+"','"+pjt_class+"','";
		input += pjt_target+"','"+mgt_plan+"','"+parent_code+"','"+Integer.parseInt(mbr_exp)+"','"+Double.parseDouble(cost_exp)+"','"+plan_start_date+"','"+plan_end_date+"','";
		input += prs_code+"','"+prs_type+"','"+pjt_desc+"','"+pjt_spec+"','"+pjt_status+"','"+Double.parseDouble(plan_labor)+"','"+Double.parseDouble(plan_sample)+"','";
		input += Double.parseDouble(plan_metal)+"','"+Double.parseDouble(plan_mup)+"','"+Double.parseDouble(plan_oversea)+"','"+Double.parseDouble(plan_plant)+"')";
		stmt.executeUpdate(input);

		//2.인력정보(pjt_member) 저장하기[과제PM정보]
		inputMember(pid,pjt_code,pjt_name,plan_start_date,plan_end_date,pjt_mbr_id);

		//3.일정정보(pjt_schedule) 저장하기[과제코드,과제명,해당프로세스(모노드,자노드,레벨,노드명)]
		inputSchedule(pid,pjt_code,pjt_name,prs_code,prs_type);

		//4.산출물관리(pjt_document) 저장하기[과제코드,과제명,해당프로세스(모노드,자노드,레벨,노드명)]
		inputDocument(pid,pjt_code,pjt_name,prs_code,prs_type);

		//5.권한관리(pjt_grade_mgr) 저장하기[keyname,owner,div_code]
		inputGradeMgr(pid,pjt_mbr_id);

		//6.과제명등록(prs_project) 상태 바꿔주기
		update = "UPDATE prs_project set pjt_status='0' where pjt_code='"+pjt_code+"' and type='"+prs_type+"'";
		stmt.executeUpdate(update);

		//7.일정정보(계획기간) 입력하기
		update = "UPDATE pjt_schedule set plan_start_date='"+plan_start_date+"',";
		update += "plan_end_date='"+plan_end_date+"' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		stmt.close();

	}

	/*******************************************************************
	* 과제인력정보 내용 저장하기 
	*******************************************************************/
	public void inputMember(String pid,String pjt_code,String pjt_name,String plan_start_date,String plan_end_date,String pjt_mbr_id) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//사번만 찾기
		String sabun = pjt_mbr_id.substring(0,pjt_mbr_id.indexOf("/"));

		//PM관련정보
		String[] man = new String[5];
		man = searchManinfo(sabun);
		String name="",tel="",grade="",div="";
		name=man[1]; tel=man[2]; grade=man[3]; div=man[4];
		String poration = "1.0";

		input = "INSERT INTO pjt_member(pid,pjt_code,pjt_name,pjt_mbr_type,mbr_start_date,mbr_end_date,";
		input += "mbr_poration,pjt_mbr_id,pjt_mbr_name,pjt_mbr_job,pjt_mbr_tel,pjt_mbr_grade,pjt_mbr_div) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+"A"+"','"+plan_start_date+"','"+plan_end_date+"','";
		input += Double.parseDouble(poration)+"','"+sabun+"','"+name+"','PM','"+tel+"','"+grade+"','"+div+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}

	/*******************************************************************
	* 과제일정정보 내용 저장하기 
	*******************************************************************/
	public void inputSchedule(String pid,String pjt_code,String pjt_name,String prs_code,String prs_type) throws Exception
	{
		String input = "",query="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("00");

		//해당프로세스의 총갯수 구하기
		query = "SELECT COUNT(*) FROM prs_process where prs_code='"+prs_code+"' and type='"+prs_type+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		String[][] data = new String[cnt][7];	//pid,pjt_code,pjt_name,모노드,자노드,레벨,노드명

		//해당프로세스의 모든정보를 쿼리하기
		query = "SELECT parent_node,child_node,level_no,node_name FROM prs_process ";
		query += "where prs_code='"+prs_code+"' and type='"+prs_type+"' order by level_no asc";	

		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) { 
			data[n][0] = pid + nfm.toDigits(n);
			data[n][1] = pjt_code;							
			data[n][2] = pjt_name;	
			data[n][3] = rs.getString("parent_node");	
			data[n][4] = rs.getString("child_node");	
			data[n][5] = rs.getString("level_no");	
			data[n][6] = rs.getString("node_name");	
			n++;
		}

		//일정관리에 등록하기
		for(int i=0; i<n; i++) {
			input = "INSERT INTO pjt_schedule(pid,pjt_code,pjt_name,parent_node,child_node,level_no,";
			input += "node_name) values('";
			input += data[i][0]+"','"+data[i][1]+"','"+data[i][2]+"','"+data[i][3]+"','"+data[i][4]+"','";
			input += data[i][5]+"','"+data[i][6]+"')";
			stmt.executeUpdate(input);
		}

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* 과제산출물정보 내용 저장하기 
	*******************************************************************/
	public void inputDocument(String pid,String pjt_code,String pjt_name,String prs_code,String prs_type) throws Exception
	{
		String input = "",query="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("00");

		//해당프로세스의 총갯수 구하기
		query = "SELECT COUNT(*) FROM prs_document where prs_code='"+prs_code+"' and type='"+prs_type+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		String[][] data = new String[cnt][7];	//pid,pjt_code,pjt_name,모노드,자노드,레벨,노드명

		//해당프로세스의 모든정보를 쿼리하기
		query = "SELECT parent_node,child_node,level_no,node_name FROM prs_document ";
		query += "where prs_code='"+prs_code+"' and type='"+prs_type+"' order by level_no asc";	
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) { 
			data[n][0] = pid + nfm.toDigits(n);
			data[n][1] = pjt_code;							
			data[n][2] = pjt_name;	
			data[n][3] = rs.getString("parent_node");	
			data[n][4] = rs.getString("child_node");	
			data[n][5] = rs.getString("level_no");	
			data[n][6] = rs.getString("node_name");	
			n++;
		}

		//일정관리에 등록하기
		for(int i=0; i<n; i++) {
			input = "INSERT INTO pjt_document(pid,pjt_code,pjt_name,parent_node,child_node,level_no,";
			input += "node_name) values('";
			input += data[i][0]+"','"+data[i][1]+"','"+data[i][2]+"','"+data[i][3]+"','"+data[i][4]+"','";
			input += data[i][5]+"','"+data[i][6]+"')";
			stmt.executeUpdate(input);
		}

		stmt.close();
		rs.close();
	}
	/*******************************************************************
	* 과제권한정보 내용 저장하기 
	*******************************************************************/
	public void inputGradeMgr(String pid,String pjt_mbr_id) throws Exception
	{
		String input = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//현재 PJT_PML로 등록되었는지 판단하기
		String mgr = "";
		String query = "SELECT keyname FROM pjt_grade_mgr where owner='"+pjt_mbr_id+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) mgr += rs.getString("keyname");

		//등록하기
		if(mgr.indexOf("PJT_PML") == -1) {
			//부서코드 찾기
			String sabun = pjt_mbr_id.substring(0,pjt_mbr_id.indexOf("/"));
			String div_code = searchAcId (sabun);

			input = "INSERT INTO pjt_grade_mgr(pid,keyname,owner,div_code) values('";
			input += pid+"','"+"PJT_PML"+"','"+pjt_mbr_id+"','"+div_code+"')";
			stmt.executeUpdate(input);
		}

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* 과제기본정보 내용 수정하기 
	*******************************************************************/
	public void updateGeneral(String pid,String pjt_code,String pjt_name,String owner,String in_date,String pjt_mbr_id,
		String pjt_class,String pjt_target,String mgt_plan,String parent_code,String mbr_exp,String cost_exp,
		String plan_start_date,String plan_end_date,String chg_start_date,String chg_end_date,String prs_code,
		String prs_type,String pjt_desc,String pjt_spec,String pjt_status,String plan_labor,String plan_sample,
		String plan_metal,String plan_mup,String plan_oversea,String plan_plant) throws Exception
	{
		String update = "",query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//현재 과제상태 알아보기
		String current_status = "";
		query = "SELECT pjt_status FROM pjt_general where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) current_status = rs.getString("pjt_status");

		//프로세스코드 변경여부 알아보기
		String current_pscode = "";
		query = "SELECT prs_code FROM pjt_general where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) current_pscode = rs.getString("prs_code");

		//PM 변경여부 알아보기
		String current_pm = "";
		query = "SELECT pjt_mbr_id FROM pjt_general where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) current_pm = rs.getString("pjt_mbr_id");

		//전제1.현과제상태가 미착수 일경우만 가능
		//전제2.관련테이블의 수정여부는 프로세스코드와 PM변경여부를 가지고 조합
		if(current_status.equals("S")){
			//1.PM만 바뀔때 [인력정보만 : 삭제 -> 입력]
			if(!(current_pm.equals(pjt_mbr_id)) && (current_pscode.equals(prs_code))) {
				deleteMember(pjt_code);				//인력삭제
				inputMember(pid,pjt_code,pjt_name,plan_start_date,plan_end_date,pjt_mbr_id); //인력입력
				inputGradeMgr(pid,pjt_mbr_id);		//권한부여[PM권한]
			}
			//2.프로세스만 바뀔때 [일정/산출물정보만 : 삭제 -> 입력]
			else if((current_pm.equals(pjt_mbr_id)) && !(current_pscode.equals(prs_code))) {
				deleteSchedule(pjt_code);		//일정삭제
				deleteDocument(pjt_code);		//산출물삭제
				inputSchedule(pid,pjt_code,pjt_name,prs_code,prs_type);		//일정입력
				inputDocument(pid,pjt_code,pjt_name,prs_code,prs_type);		//산출물입력
			}
			//3.PM/프로세스 둘다 바뀔때 [인력/일정/산출물정보 : 삭제 -> 입력]
			else if(!(current_pm.equals(pjt_mbr_id)) && !(current_pscode.equals(prs_code))) {
				deleteMember(pjt_code);			//인력삭제
				deleteSchedule(pjt_code);		//일정삭제
				deleteDocument(pjt_code);		//산출물삭제
				inputMember(pid,pjt_code,pjt_name,plan_start_date,plan_end_date,pjt_mbr_id); //인력입력
				inputSchedule(pid,pjt_code,pjt_name,prs_code,prs_type);		//일정입력
				inputDocument(pid,pjt_code,pjt_name,prs_code,prs_type);		//산출물입력
				inputGradeMgr(pid,pjt_mbr_id);		//권한부여[PM권한]
			}
		}

		//과제기본정보 수정하기 [pjt_general]
		update = "UPDATE pjt_general set pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"',owner='"+owner;
		update += "',in_date='"+in_date+"',pjt_mbr_id='"+pjt_mbr_id+"',pjt_class='"+pjt_class+"',pjt_target='"+pjt_target;
		update += "',mgt_plan='"+mgt_plan+"',parent_code='"+parent_code+"',mbr_exp='"+Integer.parseInt(mbr_exp)+"',cost_exp='"+Double.parseDouble(cost_exp);
		update += "',plan_start_date='"+plan_start_date+"',plan_end_date='"+plan_end_date+"',chg_start_date='"+chg_start_date+"',chg_end_date='"+chg_end_date;
		update += "',prs_code='"+prs_code+"',prs_type='"+prs_type+"',pjt_desc='"+pjt_desc+"',pjt_spec='"+pjt_spec;
		update += "',pjt_status='"+pjt_status+"',plan_labor='"+Double.parseDouble(plan_labor)+"',plan_sample='"+Double.parseDouble(plan_sample);
		update += "',plan_metal='"+Double.parseDouble(plan_metal)+"',plan_mup='"+Double.parseDouble(plan_mup);
		update += "',plan_oversea='"+Double.parseDouble(plan_oversea)+"',plan_plant='"+Double.parseDouble(plan_plant);
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//일정정보(계획기간) 입력하기
		update = "UPDATE pjt_schedule set plan_start_date='"+plan_start_date+"',";
		update += "plan_end_date='"+plan_end_date+"' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* 과제기본정보 내용 삭제하기 [연관되는 모든 테이블을 삭제한다]
	*******************************************************************/
	public void deleteGeneral(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//1.기본정보 테이블 삭제하기
		delete = "DELETE from pjt_general where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//2.인력정보 테이블 삭제하기
		deleteMember(pjt_code);

		//3.일정정보 테이블 삭제하기
		deleteSchedule(pjt_code);

		//4.산출물정보 테이블 삭제하기
		deleteDocument(pjt_code);

		//5.경비정보 테이블 삭제하기
		deleteCost(pjt_code);

		//6.실적정보 테이블 삭제하기
		deleteEvent(pjt_code);

		//7.과제명등록(prs_project) 상태 바꿔주기
		String update = "UPDATE prs_project set pjt_status='S' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}
	/*******************************************************************
	* 과제인력정보 내용 삭제하기
	*******************************************************************/
	public void deleteMember(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//프로세스 이름테이블에 삭제하기
		delete = "DELETE from pjt_member where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}
	/*******************************************************************
	* 과제일정정보 내용 삭제하기
	*******************************************************************/
	public void deleteSchedule(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//프로세스 이름테이블에 삭제하기
		delete = "DELETE from pjt_schedule where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}
	/*******************************************************************
	* 과제산출물정보 내용 삭제하기
	*******************************************************************/
	public void deleteDocument(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//프로세스 이름테이블에 삭제하기
		delete = "DELETE from pjt_document where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}
	/*******************************************************************
	* 과제경비정보 내용 삭제하기
	*******************************************************************/
	public void deleteCost(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//프로세스 이름테이블에 삭제하기
		delete = "DELETE from pjt_cost where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}
	/*******************************************************************
	* 과제실적정보 내용 삭제하기
	*******************************************************************/
	public void deleteEvent(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//프로세스 이름테이블에 삭제하기
		delete = "DELETE from pjt_event where pjt_code='"+pjt_code+"'";
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

	//*******************************************************************
	//	해당사번 관련정보 리턴하기 [사번,이름,전화번호,직급,부서명]
	//*******************************************************************/	
	private String[] searchManinfo (String login_id) throws Exception
	{
		String[] rtn = new String[5];		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//PM정보 알아보기
		query  = "SELECT a.name,a.office_tel,c.ar_name,b.ac_name FROM user_table a,class_table b,rank_table c ";
		query += "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
		rs = stmt.executeQuery(query);
		if(rs.next())	{
			rtn[0] = login_id;						//사번
			rtn[1] = rs.getString("name");			//이름
			rtn[2] = rs.getString("office_tel");	//전화번호
			rtn[3] = rs.getString("ar_name");		//직급명
			rtn[4] = rs.getString("ac_name");		//부서명
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;
	}
}


