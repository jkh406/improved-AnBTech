package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtIssueDAO
{
	private Connection con;
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//날자처리
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//문자열 처리

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtIssueDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtIssueDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기 [이슈/해당과제 전체수량]
	//*******************************************************************/
	private int getTotalCount(String pjt_code,String issue_status,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_issue where pjt_code='"+pjt_code+"'";
		query += " and issue_status like '%"+issue_status+"%'";
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
	// 해당과제의 이슈 List
	//*******************************************************************/	
	public ArrayList getIssueList (String pjt_code,String issue_status,String sItem,String sWord,
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
		total_cnt = getTotalCount(pjt_code,issue_status,sItem,sWord);

		//금일일자 구하기 (수정/삭제 허가여부판단)
		String todate = anbdt.getDate();		//yyyy-MM-dd
			
		//query문장 만들기
		query = "SELECT * FROM pjt_issue where pjt_code='"+pjt_code+"'";
		query += " and issue_status like '%"+issue_status+"%'";
		query += " and "+sItem+" like '%"+sWord+"%'"; 
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
				table.setUsers(rs.getString("users"));	

				String in_date = rs.getString("in_date");    //yyyy-MM-dd
				table.setInDate(in_date);

				table.setBookDate(rs.getString("book_date"));
				table.setSolution(rs.getString("solution"));
				table.setContent(rs.getString("content"));	
				String sol_date = rs.getString("sol_date");
				table.setSolDate(sol_date);
				String note_s = rs.getString("issue_status");
				table.setNoteStatus(note_s);

				//이슈 본문 작성
				String issue = rs.getString("issue");
				if(issue.length() > 30) issue = issue.substring(0,30)+" ...";
				String vissue = "";
				vissue = "<a href=\"javascript:issueSolution('"+pid+"');\">"+issue+"</a>";		//미해결시
				if(note_s.equals("1")) {		//상태가 1 이면 해결됨
					vissue = "<a href=\"javascript:issueContent('"+pid+"');\">"+issue+"</a>";	//해결시
				}
				table.setIssue(vissue);

				//보기,수정,삭제가능 표시 [단,수정/삭제는 당일에 한해 작성자만이 가능함]
				String subMod="",subDel="",subView="";
				if(todate.equals(in_date) && (note_s.equals("0"))) {		//이슈 작성시
					subMod = "<a href=\"javascript:issueModify('"+pid+"');\">수정[N]</a>";
					subDel = "<a href=\"javascript:issueDelete('"+pid+"');\">삭제[N]</a>";
				}else if(todate.equals(sol_date) && (note_s.equals("1"))) {		//이슈 작성시
					subMod = "<a href=\"javascript:solModify('"+pid+"');\">수정[S]</a>";
					subDel = "<a href=\"javascript:solRecovery('"+pid+"');\">취소[S]</a>";
				}
				
				table.setView(subView);
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
	// 해당과제 지정된 이슈 상세히 보기
	//*******************************************************************/	
	public ArrayList getIssueRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_issue where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));
				table.setUsers(rs.getString("users"));	
				table.setInDate(rs.getString("in_date"));
				table.setBookDate(rs.getString("book_date"));
				table.setIssue(rs.getString("issue"));
				table.setSolution(rs.getString("solution"));
				table.setContent(rs.getString("content"));
				table.setSolDate(rs.getString("sol_date"));
				table.setNoteStatus(rs.getString("issue_status"));	
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* 이슈 제시 작성하기 : 신규등록
	*******************************************************************/
	public void inputIssue(String pid,String pjt_code,String pjt_name,String node_code,String users,String in_date,
		String book_date,String issue,String solution,String issue_status) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		input = "INSERT INTO pjt_issue(pid,pjt_code,pjt_name,node_code,users,in_date,book_date,issue,solution,content,sol_date,";
		input += "issue_status) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+node_code+"','"+users+"','"+in_date+"','"+book_date+"','";
		input += issue+"','"+solution+"','','','"+issue_status+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}
	/*******************************************************************
	* 이슈 제시 작성하기 : 수정하기
	*******************************************************************/
	public void updateIssue(String pid,String node_code,String book_date,String users,String issue,String solution) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		update = "UPDATE pjt_issue set node_code='"+node_code+"',book_date='"+book_date;
		update +="',users='"+users+"',issue='"+issue+"',solution='"+solution;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}
	/*******************************************************************
	* 이슈 해결 작성하기 : 수정하기
	*******************************************************************/
	public void updateContent(String pid,String content,String sol_date,String issue_status) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		update = "UPDATE pjt_issue set content='"+content+"',sol_date='"+sol_date+"',issue_status='"+issue_status;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}
	/*******************************************************************
	* 이슈 해결작성 취소하기 
	*******************************************************************/
	public void updateRecovery(String pid) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		update = "UPDATE pjt_issue set content='',sol_date='',issue_status='0' ";
		update += "where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*******************************************************************
	* 이슈 제시 작성하기 : 삭제하기
	*******************************************************************/
	public void deleteIssue(String pid) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		delete = "DELETE from pjt_issue where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}

	//*******************************************************************
	// 해당과제의 진행중/결재대기상태인 노드(activity) 쿼리하기 
	// 가장최근에 입력한 실적을 보기위해 : note입력 준비작업용
	//*******************************************************************/	
	public ArrayList getWorkActivity (String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' ";
		query += "and (node_status='1' or node_status='11') and level_no='3' order by child_node ASC";
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
									
				table.setPid(rs.getString("pid"));	
				table.setPjtCode(rs.getString("pjt_code"));		
				table.setPjtName(rs.getString("pjt_name"));		
				table.setParentNode(rs.getString("parent_node"));	
				table.setChildNode(rs.getString("child_node"));
				table.setLevelNo(rs.getString("level_no"));
				table.setNodeName(rs.getString("node_name"));
				table.setWeight(rs.getDouble("weight"));
				String user_id = rs.getString("user_id"); if(user_id == null) user_id = "";
				table.setUserId(user_id);
				String user_name = rs.getString("user_name"); if(user_name == null) user_name = "";
				table.setUserName(user_name);
				String pjt_node_mbr = rs.getString("pjt_node_mbr"); if(pjt_node_mbr == null) pjt_node_mbr = "";
				table.setPjtNodeMbr(pjt_node_mbr);
				String plan_start_date = rs.getString("plan_start_date"); if(plan_start_date == null) plan_start_date = "";
				table.setPlanStartDate(plan_start_date);
				String plan_end_date = rs.getString("plan_end_date"); if(plan_end_date == null) plan_end_date = "";
				table.setPlanEndDate(plan_end_date);
				String chg_start_date = rs.getString("chg_start_date"); if(chg_start_date == null) chg_start_date = "";
				table.setChgStartDate(chg_start_date);
				String chg_end_date = rs.getString("chg_end_date"); if(chg_end_date == null) chg_end_date = "";
				table.setChgEndDate(chg_end_date);
				String rst_start_date = rs.getString("rst_start_date"); if(rst_start_date == null) rst_start_date = "";
				table.setRstStartDate(rst_start_date);
				String rst_end_date = rs.getString("rst_end_date"); if(rst_end_date == null) rst_end_date = "";
				table.setRstEndDate(rst_end_date);
				String plan_cnt = rs.getString("plan_cnt"); if(plan_cnt == null) plan_cnt = "0";
				table.setPlanCnt(Integer.parseInt(plan_cnt));
				String chg_cnt = rs.getString("chg_cnt"); if(chg_cnt == null) chg_cnt = "0";
				table.setChgCnt(Integer.parseInt(chg_cnt));
				String result_cnt = rs.getString("result_cnt"); if(result_cnt == null) result_cnt = "";
				table.setResultCnt(Integer.parseInt(result_cnt));
				table.setProgress(rs.getDouble("progress"));
				String node_status = rs.getString("node_status"); if(node_status == null) node_status = "";
				table.setNodeStatus(node_status);
				String remark = rs.getString("remark"); if(remark == null) remark = "";
				table.setRemark(remark);
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제의 전체노드 리스트
	// note입력 준비작업용
	//*******************************************************************/	
	public ArrayList getNodeList (String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' and level_no != '0' ";
		query += "order by level_no,child_node ASC";
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
										
				table.setChildNode(rs.getString("child_node"));
				table.setNodeName(rs.getString("node_name"));
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제의 진행중/결재대기상태인 노드(activity) 가장최근 실적만 가져오기 
	// 가장최근에 입력한 실적을 보기위해 : note입력 준비작업용
	//*******************************************************************/	
	public ArrayList getLastWork (String pjt_code,String node_code,String in_date) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"' ";
		query += "and in_date='"+in_date+"' order by pid DESC";
		rs = stmt.executeQuery(query);

		//데이터 담기
		if(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));	
				table.setNodeName(rs.getString("node_name"));
				table.setProgress(rs.getDouble("progress"));
				table.setUserId(rs.getString("user_id"));	
				table.setUserName(rs.getString("user_name"));
				table.setInDate(rs.getString("in_date"));
				table.setWmType(rs.getString("wm_type"));
				table.setEvtContent(rs.getString("evt_content"));
				table.setEvtNote(rs.getString("evt_note"));
				table.setEvtIssue(rs.getString("evt_issue"));
				String remark = rs.getString("remark"); if(remark == null) remark = "";
				table.setRemark(remark);	
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제의 진행중/결재대기상태인 노드(activity) 가장최근 입력일 가져오기
	// 가장최근에 입력한 실적을 보기위해 : note입력 준비작업용
	//*******************************************************************/	
	public String getLastDate (String pjt_code,String node_code) throws Exception
	{
		//변수 초기화
		String in_date="";	
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();

		//query문장 만들기
		query = "SELECT in_date FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"' ";
		query += "order by pid DESC";
		rs = stmt.executeQuery(query);

		//데이터 담기
		if(rs.next()) { 
			in_date = rs.getString("in_date");					
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return in_date;
	}

	//*******************************************************************
	// 해당과제의 진행중/결재대기상태인 노드(activity) 날자만 쿼리하기
	// 날자별 입력한 실적을 보기위해 : note입력 준비작업용
	//*******************************************************************/	
	public ArrayList getInDate (String pjt_code,String node_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"' ";
		query += "order by pid DESC";
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));	
				table.setNodeName(rs.getString("node_name"));
				table.setProgress(rs.getDouble("progress"));
				table.setUserId(rs.getString("user_id"));	
				table.setUserName(rs.getString("user_name"));
				table.setInDate(rs.getString("in_date"));
				table.setWmType(rs.getString("wm_type"));
				table.setEvtContent(rs.getString("evt_content"));
				table.setEvtNote(rs.getString("evt_note"));
				table.setEvtIssue(rs.getString("evt_issue"));
				String remark = rs.getString("remark"); if(remark == null) remark = "";
				table.setRemark(remark);	
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제 멤버 List가져오기
	//*******************************************************************/	
	public ArrayList getPjtMember (String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		
		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_member where pjt_code='"+pjt_code+"' order by pjt_mbr_type ASC";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setPjtMbrType(rs.getString("pjt_mbr_type"));
				table.setMbrStartDate(rs.getString("mbr_start_date"));
				table.setMbrEndDate(rs.getString("mbr_end_date"));
				table.setMbrPoration(rs.getDouble("mbr_poration"));
				table.setPjtMbrId(rs.getString("pjt_mbr_id"));
				table.setPjtMbrName(rs.getString("pjt_mbr_name"));
				table.setPjtMbrJob(rs.getString("pjt_mbr_job"));
				table.setPjtMbrTel(rs.getString("pjt_mbr_tel"));
				table.setPjtMbrGrade(rs.getString("pjt_mbr_grade"));
				table.setPjtMbrDiv(rs.getString("pjt_mbr_div"));
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 프로젝트 이름 찾기
	//*******************************************************************/	
	public String getProjectName(String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",rtnData="";	
		
		stmt = con.createStatement();
		
		//query문장 만들기
		query = "SELECT pjt_name FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 	
				rtnData = rs.getString("pjt_name");
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return rtnData;
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




