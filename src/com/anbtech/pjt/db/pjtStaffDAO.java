package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtStaffDAO
{
	private Connection con;

	private String[][] item = null;				//프로세스정보를 배열로 담기
	private int an = 0;							//items의 배열 증가
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtStaffDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtStaffDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기 (전체) [자신이 포함된 과제 총갯수] : 과제조회시 사용
	//*******************************************************************/
	private int getAllTotalCount(String login_id,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_general a,pjt_member b where a.pjt_code = b.pjt_code ";
		query += " and b.pjt_mbr_id = '"+login_id+"'";
		query += " and (a."+sItem+" like '%"+sWord+"%')"; 
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}

	//*******************************************************************
	//	총 수량 파악하기 (전체) [자신이 포함된 과제중 진행중인 과제 총갯수] : 진행관리용
	//*******************************************************************/
	private int getPrsTotalCount(String login_id,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_general a,pjt_member b where a.pjt_code = b.pjt_code ";
		query += " and b.pjt_mbr_id = '"+login_id+"' and pjt_status = '1'";
		query += " and (a."+sItem+" like '%"+sWord+"%')"; 
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
	// 과제 LIST [자신이 포함된 과제 전체LIST] : 과제조회시 사용 
	//*******************************************************************/	
	public ArrayList getAllGeneralList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		total_cnt = getAllTotalCount(login_id,sItem,sWord);
			
		//query문장 만들기
		query = "SELECT a.* FROM pjt_general a,pjt_member b where a.pjt_code = b.pjt_code ";
		query += " and b.pjt_mbr_id = '"+login_id+"'";
		query += " and (a."+sItem+" like '%"+sWord+"%') order by a.pjt_code desc"; 
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

				String pjt_name = rs.getString("pjt_name");
				String pname = "<a href=\"javascript:pjtView('"+pjt_code+"');\">"+pjt_name+"</a>";
				table.setPjtName(pname);	

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
				table.setPjtStatus(rs.getString("pjt_status"));
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
				
				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 과제 LIST [자신이 포함된 과제중 진행중인 과제] : 노드 담당자 진행관리용
	//*******************************************************************/	
	public ArrayList getPrsGeneralList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		total_cnt = getAllTotalCount(login_id,sItem,sWord);
			
		//query문장 만들기
		query = "SELECT a.* FROM pjt_general a,pjt_member b where a.pjt_code = b.pjt_code ";
		query += " and b.pjt_mbr_id = '"+login_id+"' and pjt_status = '1'";
		query += " and (a."+sItem+" like '%"+sWord+"%') order by a.pjt_code desc"; 
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

				String pjt_name = rs.getString("pjt_name");
				String pname = "<a href=\"javascript:pjtView('"+pjt_code+"');\">"+pjt_name+"</a>";
				table.setPjtName(pname);	

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
				table.setPjtStatus(rs.getString("pjt_status"));
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
				
				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/**********************************************************************
	 * 해당과제중 자신이 포함된 노드만 배열에 담는다. : 노드담당자 진행관리용
	 *********************************************************************/
	private void saveItemsArray(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		query = "select * from pjt_schedule ";
		query += "where level_no = '"+level_no+"' and parent_node = '"+parent_node+"' ";
		query += "and pjt_code = '"+pjt_code+"' order by child_node asc";
		rs = stmt.executeQuery(query);
		
		int no = 0;
		while (rs.next()) {
			item[an][0]=rs.getString("pid");	
			item[an][1]=rs.getString("pjt_code");		
			item[an][2]=rs.getString("pjt_name");		
			item[an][3]=rs.getString("parent_node");	
			item[an][4]=rs.getString("child_node");
			item[an][5]=rs.getString("level_no"); 
			item[an][6]=rs.getString("node_name");
			item[an][7]=Double.toString(rs.getDouble("weight"));
			item[an][8]=rs.getString("user_id");			if(item[an][8] == null) item[an][8] = "";
			item[an][9]=rs.getString("user_name");			if(item[an][9] == null) item[an][9] = "";
			item[an][10]=rs.getString("pjt_node_mbr");		if(item[an][10] == null) item[an][10] = "";
			item[an][11]=rs.getString("plan_start_date");	if(item[an][11] == null) item[an][11] = "";
			item[an][12]=rs.getString("plan_end_date");		if(item[an][12] == null) item[an][12] = "";
			item[an][13]=rs.getString("chg_start_date");	if(item[an][13] == null) item[an][13] = "";
			item[an][14]=rs.getString("chg_end_date");		if(item[an][14] == null) item[an][14] = "";
			item[an][15]=rs.getString("rst_start_date");	if(item[an][15] == null) item[an][15] = "";
			item[an][16]=rs.getString("rst_end_date");		if(item[an][16] == null) item[an][16] = "";
			item[an][17]=rs.getString("plan_cnt");			
			item[an][18]=rs.getString("chg_cnt");			
			item[an][19]=rs.getString("result_cnt");
			item[an][20]=Double.toString(rs.getDouble("progress"));
			item[an][21]=rs.getString("node_status");		if(item[an][21] == null) item[an][21] = "";
			item[an][22]=rs.getString("remark");			if(item[an][22] == null) item[an][22] = "";
			an++;

			no = Integer.parseInt(item[an-1][5]);				//String을 정수로 바꾸기
			lno = Integer.toString(no+1);						//+1하여 정수를 String으로 바꾸기 
			saveItemsArray(pjt_code,lno,item[an-1][4]);
		}
		rs.close();
		stmt.close(); 
		
	} //saveItemsArray

	//*******************************************************************
	//	해당 과제코드의 총 수량 파악하기 : 노드담당자 진행관리용
	//*******************************************************************/
	private int getAllTotalCount(String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_schedule where pjt_code='"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}

	/**********************************************************************
	 * 해당과제의 모든항목을 배열에서 ArrayList로 담는다 : 노드담당자 진행관리용
	 *********************************************************************/
	public ArrayList getPjtSchedule(String pjt_code,String level_no,String parent_node,String login_id) throws Exception
	{
		//1.배열만들기
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][23];

		//2. 배열에 담는다.
		saveItemsArray(pjt_code,level_no,parent_node);

		//3.ArrayList에 담기
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		String node_name="",user_id="",user_name="";
		user_id = getAllProjectMember(pjt_code);		//해당project의 전체멤버
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
				
			//user_id = item[i][7]+":"+item[i][9];		//노드멤버만 볼수 있게	
			if(item[i][5].equals("3") && (user_id.indexOf(login_id) != -1)) {	//pjt_cod,parent_node,child_node
				node_name = "<a href=\"javascript:nodeView('"+item[i][1]+"','"+item[i][4]+"');\">"+item[i][4]+" "+item[i][6]+"</a>";
				//user_id = "";							//노드멤버만 볼수 있게 할 경우
			} else { node_name = item[i][4]+" "+item[i][6]; }
			table.setNodeName(node_name);
			table.setWeight(Double.parseDouble(item[i][7]));
			table.setUserId(item[i][8]);

			if(item[i][8].equals(login_id)) {			//담당자
				user_name = "<font color=blue><B>"+item[i][9]+"</B></font>";
				table.setUserName(user_name);
			} else {
				table.setUserName(item[i][9]);
			}

			table.setPjtNodeMbr(item[i][10]);
			table.setPlanStartDate(item[i][11]);
			table.setPlanEndDate(item[i][12]);
			table.setChgStartDate(item[i][13]);
			table.setChgEndDate(item[i][14]);
			table.setRstStartDate(item[i][15]);
			table.setRstEndDate(item[i][16]);
			table.setPlanCnt(Integer.parseInt(item[i][17]));
			table.setChgCnt(Integer.parseInt(item[i][18]));
			table.setResultCnt(Integer.parseInt(item[i][19]));
			table.setProgress(Double.parseDouble(item[i][20]));
			table.setNodeStatus(item[i][21]);
			table.setRemark(item[i][22]);
					
			table_list.add(table);
		}
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

	//*******************************************************************
	// 해당과제코드의 멤버 전체멤버 찾기
	//*******************************************************************/	
	public String getAllProjectMember (String pjt_code) throws Exception
	{
		//변수 초기화
		String member = "";
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;

		//query문장 만들기
		query = "SELECT pjt_mbr_id FROM pjt_member where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);
		//데이터 담기
		while(rs.next()) { 			
			member += rs.getString("pjt_mbr_id")+":";
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return member;
	}
}


