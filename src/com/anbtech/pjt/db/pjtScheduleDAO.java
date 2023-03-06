package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtScheduleDAO
{
	com.anbtech.pjt.business.pjtScheduleBO schBO = new com.anbtech.pjt.business.pjtScheduleBO();
	com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();	//파일로 담기[메일]
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//날자처리
	private Connection con;
	
	private String query = "";
	private String[][] item = null;				//프로세스정보를 배열로 담기
	private int an = 0;							//items의 배열 증가

	private String pjt_code = "";				//프로세스 코드
	private String child_node = "";				//프로세스의 자노드
	private String level_no = "0";				//현노드의 레벨번호
	private String type = "";					//P:전사표준,  부서코드:부서표준
	private String prs_code = "";				//프로세스 코드번호

	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtScheduleDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtScheduleDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	해당 과제코드의 총 수량 파악하기 
	//*******************************************************************/
	public int getAllTotalCount(String pjt_code) throws Exception
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
	 * 해당과제의 모든항목을 배열에 담는다. 
	 *********************************************************************/
	public void saveItemsArray(String pjt_code,String level_no,String parent_node) throws Exception
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

	/**********************************************************************
	 * 해당과제의 모든항목을 배열에서 ArrayList로 담는다
	 *********************************************************************/
	public ArrayList getPjtSchedule(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//1.ArrayList에 담기
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//2.배열만들기
		int cnt = getAllTotalCount(pjt_code);
		if(cnt == 0) { return table_list; };
		item = new String[cnt][23];

		//3. 배열에 담는다.
		saveItemsArray(pjt_code,level_no,parent_node);

		String node_name="";
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
			
			if(item[i][5].equals("3")) //pjt_cod,parent_node,child_node 
				node_name = "<a href=\"javascript:detailSch('"+item[i][1]+"','"+item[i][3]+"','"+item[i][4]+"');\">"+item[i][4]+" "+item[i][6]+"</a>";
			else node_name = item[i][4]+" "+item[i][6];
			table.setNodeName(node_name);

			table.setWeight(Double.parseDouble(item[i][7]));
			table.setUserId(item[i][8]);
			table.setUserName(item[i][9]);
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

	/**********************************************************************
	 * 해당과제의 해당노드 정보 가져오기. 
	 *********************************************************************/
	public ArrayList getNodeData(String pjt_code,String parent_node,String child_node) throws Exception
	{
		//변수 초기화
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		query = "select * from pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"' ";
		query += "and child_node = '"+child_node+"' order by child_node asc";
		rs = stmt.executeQuery(query);

		projectTable table = null;
		ArrayList table_list = new ArrayList();

		while (rs.next()) {
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
		rs.close();
		stmt.close(); 
		return table_list;
		
	}

	//*******************************************************************
	// 자신의 전체 과제LIST
	//*******************************************************************/	
	public ArrayList getProjectList (String login_id,String pjtWord,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//과제PM 인지 판단하기
		String pjt_pml = "N";
		pjt_pml = checkPjtPML(login_id);

		//query문장 만들기
		if(pjt_pml.equals("Y")) {		//PM
			query = "SELECT pjt_status,pjt_code,pjt_name FROM pjt_general where pjt_mbr_id like '%"+login_id+"%'";	
			query += " and pjt_status like '%"+pjtWord+"%'";
			query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 
		} else {						//멤버
			//query = "SELECT distinct pjt_code,pjt_name FROM pjt_member where pjt_mbr_id='"+login_id+"'";	
			//query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc";
			query = "SELECT distinct a.pjt_status,b.pjt_code,b.pjt_name FROM pjt_general a,pjt_member b where b.pjt_mbr_id='"+login_id+"'";	
			query += " and a.pjt_status like '%"+pjtWord+"%' and a.pjt_code = b.pjt_code";
			query += " and (b."+sItem+" like '%"+sWord+"%') order by b.pjt_code desc";
		}
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
								
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setPjtStatus(rs.getString("pjt_status"));

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	과제PM 여부 판단하기
	//*******************************************************************/	
	public String checkPjtPML (String login_id) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where keyname = 'PJT_PML' and owner like '%"+login_id+"%'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		if(cnt > 0) rtn = "Y";
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;	
	}

	/*******************************************************************
	* 과제일정정보 세부일정 입력요청시,진행전 수정시  ---> PM
	* 핵심:담당자,멤버,계획일,수정일
	*******************************************************************/
	public void updateSchedule(String pid,String pjt_code,String parent_node,String child_node,String weight,
		String user_id,String user_name,String pjt_node_mbr,String plan_start_date,String plan_end_date,
		String chg_start_date,String chg_end_date,String rst_start_date,String rst_end_date,
		String node_status) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();
		
		String plan_cnt = schBO.getPeriodDate(plan_start_date,plan_end_date);		//계획일수
		String chg_cnt = schBO.getPeriodDate(chg_start_date,chg_end_date);			//수정일수
		String result_cnt = schBO.getPeriodDate(rst_start_date,rst_end_date);		//실적일수

		//해당노드의 상세일정 입력하기
		update = "UPDATE pjt_schedule set weight='"+weight+"',user_id='"+user_id+"',user_name='"+user_name+"',pjt_node_mbr='"+pjt_node_mbr;
		update += "',plan_start_date='"+plan_start_date+"',plan_end_date='"+plan_end_date+"',chg_start_date='"+chg_start_date;
		update += "',chg_end_date='"+chg_end_date+"',rst_start_date='"+rst_start_date+"',rst_end_date='"+rst_end_date;
		update += "',plan_cnt='"+plan_cnt+"',chg_cnt='"+chg_cnt+"',result_cnt='"+result_cnt+"',node_status='"+node_status;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//해당노드가 해당STEP의 마지막 Activity의 상세일정이면 해당STEP의 상세일정을 입력한다.
		updateStepSchedule(pjt_code,parent_node,child_node,plan_start_date,plan_end_date,chg_start_date,chg_end_date,node_status);

		stmt.close();
	}

	/*******************************************************************
	* 과제일정정보 노드시작지시 요청시 ---------------> PM
	* 핵심:실적시작일,지시사항
	*******************************************************************/
	public void updateNodeStart(String pid,String pjt_code,String parent_node,String child_node,
		String plan_start_date,String plan_end_date,String chg_start_date,String chg_end_date,
		String rst_start_date,String rst_end_date,String node_status,String remark) throws Exception
	{
		String update = "",query="",pjt_status="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//노드지시 입력하기
		update = "UPDATE pjt_schedule set chg_start_date='"+chg_start_date+"',chg_end_date='"+chg_end_date;
		update += "',rst_start_date='"+rst_start_date+"',rst_end_date='"+rst_end_date+"',node_status='1',remark='"+remark;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//과제기초정보의 진행상태 파악하기
		query  = "SELECT pjt_status FROM pjt_general where pjt_code = '"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			pjt_status = rs.getString("pjt_status");
		}

		//과제기본정보에 상태알려주기[1:과제진행중 및 실적 시작일]
		if(pjt_status.equals("0")) {	//과제진행전에만 실행함
			update = "UPDATE pjt_general set pjt_status='1',rst_start_date='"+rst_start_date+"'";
			update += " where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			update = "UPDATE prs_project set pjt_status='1' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			update = "UPDATE pjt_status set pjt_status='1' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
		}
		
		//해당노드가 해당STEP의 마지막 Activity의 상세일정이면 해당STEP의 상세일정을 입력한다.
		updateStepSchedule(pjt_code,parent_node,child_node,plan_start_date,plan_end_date,chg_start_date,chg_end_date,node_status);

		//해당노드의 상위노드step,phase및 project의 실적 시작일만 입력하기
		updateResultStartDate(pjt_code,child_node,rst_start_date);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* 해당노드가 해당STEP의 마지막 Activity의 상세일정이면 해당STEP의 상세일정을 입력한다.
	*******************************************************************/
	public void updateStepSchedule(String pjt_code,String parent_node,String child_node,
		String plan_start_date,String plan_end_date,String chg_start_date,String chg_end_date,
		String node_status) throws Exception
	{
		String query="",update="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = null;				//모든 계획일 담기
		String[][] change = null;			//수정일 담기 (단,수정일이 없으면 계획일을 담는다)
		stmt = con.createStatement();
	
		//1.STEP의 Activity전체갯수를 파악한다.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int ActCnt = rs.getInt(1);
		
		//2.현재 등록된 계획일 Activity의 갯수 파악한다.[전체갯수 - 현재개수 > 1 이면 return]
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"' and plan_cnt != 0";
		rs = stmt.executeQuery(query);
		rs.next();
		int PlanCnt = rs.getInt(1);
		if(ActCnt > PlanCnt + 1) {	stmt.close(); rs.close(); return; }

		//3.현재 등록된 Activity의 정보를 배열에 담는다. 
		//  그리고 null이면 파라미터 날자를 담는다.
		data = new String[ActCnt][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"'";
		rs = stmt.executeQuery(query);
		int n = 0, plan = 0, chg = 0;	//배열번호,plan이 null인갯수, chg가 null이갯수
		String cnode = "";				//자노드
		while(rs.next()) {
			cnode = rs.getString("child_node");

			data[n][0] = rs.getString("plan_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = plan_start_date;
			else if(data[n][0].length() == 0) plan++;
			data[n][1] = rs.getString("plan_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = plan_end_date;
			n++;
		}

		//4.계획일이 마지막이 아니면 여기서 종료
		if(plan > 0) {		//기존에 입력된 내용으로 마지막이 아님
			stmt.close(); rs.close(); return; 
		}

		//5.계획일 Activity에서 최초일과 최종일을 구한다.
		String[][] schDate = new String[1][3];	//계획일[시작,종료]
		schDate = schBO.completeFirstLastDate(data); 

		//6.일자가 0이 아니면 계획일과 수정일을 입력한다.
		//계획일[시작,종료]을 해당 STEP에 입력한다. [노드상태 : 노드 미입력]
		if(!schDate[0][0].equals("0") && (node_status.equals(""))) {	
			update = "UPDATE pjt_schedule set plan_start_date='"+schDate[0][0]+"',plan_end_date='"+schDate[0][1];
			update += "', plan_cnt='"+Integer.parseInt(schDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"' and level_no='2'";
			stmt.executeUpdate(update);
		}
		//계획일[시작,종료]을 해당 STEP에 입력한다. [노드상태 : 진행전(0)]
		else if(!schDate[0][0].equals("0") && (node_status.equals("0"))) {	
			update = "UPDATE pjt_schedule set plan_start_date='"+schDate[0][0]+"',plan_end_date='"+schDate[0][1];
			update += "', plan_cnt='"+Integer.parseInt(schDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"' and level_no='2'";
			stmt.executeUpdate(update);
		}

		//-----------------------------------------------------------------------
		//7. 수정일이 입력되면 Step의 계획일정과 수정일을 비교하여 Step의 수정일정을 입력한다.
		//-----------------------------------------------------------------------
		if(chg_start_date.length() != 0){
			//1.해당Step의 수정일 담기 (단,수정일이 없으면 계획일을 담는다)
			change = new String[ActCnt+1][2];		
			query  = "SELECT * FROM pjt_schedule ";
			query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"'";
			rs = stmt.executeQuery(query);
		
			int cn = 0;
			while(rs.next()) {
				change[cn][0] = rs.getString("chg_start_date");		if(change[cn][0] == null) change[cn][0] = "";
				if(change[cn][0].length() == 0) change[cn][0] = rs.getString("plan_start_date"); 
				change[cn][1] = rs.getString("chg_end_date");		if(change[cn][1] == null) change[cn][1] = "";
				if(change[cn][1].length() == 0) change[cn][1] = rs.getString("plan_end_date"); 
				cn++;
			}
			change[cn][0] = chg_start_date;
			change[cn][1] = chg_end_date;

			//2.수정일 최초일과 최종일을 구한다.
			String[][] chgDate = new String[1][3];	//계획일[시작,종료]
			chgDate = schBO.completeFirstLastDate(change); 

			//3.수정일[시작,종료]을 해당 STEP에 입력한다. [노드상태 : 진행중(1)]
			update = "UPDATE pjt_schedule set chg_start_date='"+chgDate[0][0]+"',chg_end_date='"+chgDate[0][1];
			update += "', chg_cnt='"+Integer.parseInt(chgDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"' and level_no='2'";
			stmt.executeUpdate(update);
		}

		//8.해당STEP이 마지막이면 해당PHASE을 입력한다.
		updatePhaseSchedule(pjt_code,parent_node,plan_start_date,plan_end_date,chg_start_date,chg_end_date,node_status);
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();	
	}

	/*******************************************************************
	* 해당노드가 해당PHASE의 마지막 STEP 일정이면 해당PHASE의 상세일정을 입력한다.
	*******************************************************************/
	public void updatePhaseSchedule(String pjt_code,String child_node,String plan_start_date,String plan_end_date,
		String chg_start_date,String chg_end_date,String node_status) throws Exception
	{
		String query="",update="",phase="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = null;
		String[][] change = null;			//수정일 담기 (단,수정일이 없으면 계획일을 담는다)
		stmt = con.createStatement();

		//0.PHASE코드 찾기
		query  = "SELECT parent_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and child_node = '"+child_node+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			phase = rs.getString("parent_node");
		}

		//1.PHASE의 STEP전체갯수를 파악한다.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no='2'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCnt = rs.getInt(1);

		//2.현재 등록된 계획일 STEP의 갯수 파악한다.[전체갯수 - 현재개수 > 1 이면 return]
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2' and plan_cnt != 0";
		rs = stmt.executeQuery(query);
		rs.next();
		int PlanCnt = rs.getInt(1);
		if(PhsCnt > PlanCnt + 1) {	stmt.close(); rs.close(); return; }
	
		//3.현재 등록된 STEP의 정보를 배열에 담는다. 
		//  그리고 null이면 파라미터 날자를 담는다.
		data = new String[PhsCnt][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
		rs = stmt.executeQuery(query);
		int n = 0, plan = 0, chg = 0;	//배열번호,plan이 null인갯수, chg가 null이갯수
		String cnode = "";				//자노드
		while(rs.next()) {
			cnode = rs.getString("child_node");

			data[n][0] = rs.getString("plan_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = plan_start_date;
			else if(data[n][0].length() == 0) plan++;
			data[n][1] = rs.getString("plan_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = plan_end_date;
			n++;
		}

		//4.계획일이 마지막이 아니면 여기서 종료
		if(plan > 0) {		//기존에 입력된 내용으로 마지막이 아님
			stmt.close(); rs.close(); return; 
		}

		//5.Step에서 최초일과 최종일을 구한다.
		String[][] schDate = new String[1][3];	//계획일[시작,종료], 수정일[시작,종료]
		schDate = schBO.completeFirstLastDate(data);

		//6.일자가 0이 아니면 계획일과 수정일을 입력한다.
		//계획일[시작,종료]을 해당 STEP에 입력한다. [노드상태 : 노드 미입력]
		if(!schDate[0][0].equals("0") && (node_status.equals(""))) {	
			update = "UPDATE pjt_schedule set plan_start_date='"+schDate[0][0]+"',plan_end_date='"+schDate[0][1];
			update += "', plan_cnt='"+Integer.parseInt(schDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+phase+"' and level_no='1'";
			stmt.executeUpdate(update);
		}
		//계획일[시작,종료]을 해당 STEP에 입력한다. [노드상태 : 진행전(0)]
		else if(!schDate[0][0].equals("0") && (node_status.equals("0"))) {	
			update = "UPDATE pjt_schedule set plan_start_date='"+schDate[0][0]+"',plan_end_date='"+schDate[0][1];
			update += "', plan_cnt='"+Integer.parseInt(schDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+phase+"' and level_no='1'";
			stmt.executeUpdate(update);
		}

		//7.과제전체일정일수 구하기
		//PHASE의 전체갯수를 파악한다.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PjtCnt = rs.getInt(1);

		//배열을 만든다
		data = new String[PjtCnt][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1' and child_node !='"+phase+"'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()) {
			data[n][0] = rs.getString("plan_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = plan_start_date;
			else if(data[n][0].length() == 0) plan++;
			data[n][1] = rs.getString("plan_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = plan_end_date;
			n++;
		}
		data[n][0] = schDate[0][0];
		data[n][1] = schDate[0][1];

		String[][] pjtDate = new String[1][3];	//계획일[시작,종료],총일수
		pjtDate = schBO.completeFirstLastDate(data);

		update = "UPDATE pjt_schedule set plan_cnt='"+Integer.parseInt(pjtDate[0][2]);
		update += "' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		//-----------------------------------------------------------------------
		//8. 수정일이 입력되면 phase의 계획일정과 수정일을 비교하여 Step의 수정일정을 입력한다.
		//-----------------------------------------------------------------------
		String[][] chgDate = null;	//수정기간[시작,종료]
		if(chg_start_date.length() != 0){
			//1.해당Step의 수정일 담기 (단,수정일이 없으면 계획일을 담는다)
			change = new String[PhsCnt+1][2];		
			query  = "SELECT * FROM pjt_schedule ";
			query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
			rs = stmt.executeQuery(query);
		
			int cn = 0;
			while(rs.next()) {
				change[cn][0] = rs.getString("chg_start_date");		if(change[cn][0] == null) change[cn][0] = "";
				if(change[cn][0].length() == 0) change[cn][0] = rs.getString("plan_start_date"); 
				change[cn][1] = rs.getString("chg_end_date");		if(change[cn][1] == null) change[cn][1] = "";
				if(change[cn][1].length() == 0) change[cn][1] = rs.getString("plan_end_date"); 
				cn++;
			}
			change[cn][0] = chg_start_date;
			change[cn][1] = chg_end_date;

			//2.수정일 최초일과 최종일을 구한다.
			chgDate = new String[1][3];	//수정기간[시작,종료]
			chgDate = schBO.completeFirstLastDate(change); 

			//3.수정일[시작,종료]을 해당 STEP에 입력한다. [노드상태 : 진행중(1)]
			update = "UPDATE pjt_schedule set chg_start_date='"+chgDate[0][0]+"',chg_end_date='"+chgDate[0][1];
			update += "', chg_cnt='"+Integer.parseInt(chgDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+phase+"' and level_no='1'";
			stmt.executeUpdate(update);
		}

		//-----------------------------------------------------------------------
		//9.해당Phase가 전부수정되면 과제기본정보의 수정기간을 자동입력한다.
		//-----------------------------------------------------------------------
		if(chg_start_date.length() != 0){
			updateGeneralSchedule(pjt_code,chgDate[0][0],chgDate[0][1]);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* 해당Phase가 전부수정되면 과제기본정보의 수정기간을 자동입력
	*******************************************************************/
	public void updateGeneralSchedule(String pjt_code,String chg_start_date,String chg_end_date) throws Exception
	{
		String query="",update="",phase="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] change = null;
		stmt = con.createStatement();

		//1.해당과제의 PHASE전체갯수를 파악한다.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCnt = rs.getInt(1);

		//2.현재 등록된 PHASE의 정보를 배열에 담는다. 
		change = new String[PhsCnt+1][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1'";
		rs = stmt.executeQuery(query);
		
		int cn = 0;
		while(rs.next()) {
			change[cn][0] = rs.getString("chg_start_date");		if(change[cn][0] == null) change[cn][0] = "";
			if(change[cn][0].length() == 0) change[cn][0] = rs.getString("plan_start_date"); 
			change[cn][1] = rs.getString("chg_end_date");		if(change[cn][1] == null) change[cn][1] = "";
			if(change[cn][1].length() == 0) change[cn][1] = rs.getString("plan_end_date"); 
			cn++;
		}
		change[cn][0] = chg_start_date;
		change[cn][1] = chg_end_date;

		//3.수정일 최초일과 최종일을 구한다.
		String[][] chgDate = new String[1][3];	//수정기간[시작,종료]
		chgDate = schBO.completeFirstLastDate(change); 

		//4.수정일[시작,종료]을 과제기본정보에 입력한다.
		update = "UPDATE pjt_general set chg_start_date='"+chgDate[0][0]+"',chg_end_date='"+chgDate[0][1];
		update += "' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);

		//5.수정일[시작,종료],수저일수 을 일정정보의 최상단에 입력한다.
		update = "UPDATE pjt_schedule set chg_start_date='"+chgDate[0][0]+"',chg_end_date='"+chgDate[0][1];
		update += "',chg_cnt='"+chgDate[0][2];
		update += "' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// STEP,PHASE의 실적 시작일만 입력하기
	//*******************************************************************/	
	public void	updateResultStartDate(String pjt_code,String child_node,String rst_start_date) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",update="",node_status="";	
		String step="",phase="",rdate="",rstDate="";
		String[] data;
		int n = 0;
		stmt = con.createStatement();

		//1.해당노드의 STEP코드 찾기
		query  = "SELECT parent_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and child_node = '"+child_node+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			step = rs.getString("parent_node");
		}

		//2.해당노드의 PHASE코드 찾기
		query  = "SELECT parent_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and child_node = '"+step+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			phase = rs.getString("parent_node");
		}

		//3.해당 STEP내의 Activity 갯수파악
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+step+"' and level_no='3'";
		rs = stmt.executeQuery(query);
		rs.next();
		int ActCnt = rs.getInt(1);

		//4.해당 PHASE내의 Step 갯수파악
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no='2'";
		rs = stmt.executeQuery(query);
		rs.next();
		int StpCnt = rs.getInt(1);

		//5.해당 과제내의 Phase 갯수파악
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCnt = rs.getInt(1);

		//6.해당 Activity의 시작일만 쿼리하기 [최초시작일 찾기 -> 해당노드의 STEP의 실적시작일에 입력하기]
		data = new String[ActCnt+1];
		query  = "SELECT rst_start_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+step+"' and level_no = '3'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_start_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rst_start_date;
		rstDate = schBO.completeFirstDate(data);	//실적시작일중 제일작일날자
		update = "UPDATE pjt_schedule set rst_start_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and child_node = '"+step+"'";
		stmt.executeUpdate(update);

		//6.해당 Step의 시작일만 쿼리하기 [최초시작일 찾기 -> 해당노드의 PHASE의 실적시작일에 입력하기]
		data = new String[StpCnt+1];
		query  = "SELECT rst_start_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_start_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rstDate;
		rstDate = schBO.completeFirstDate(data);	//실적시작일중 제일작일날자
		update = "UPDATE pjt_schedule set rst_start_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and child_node = '"+phase+"'";
		stmt.executeUpdate(update);

		//7.해당 Phase의 시작일만 쿼리하기 [최초시작일 찾기 -> 해당노드의 과제의 실적시작일에 입력하기]
		data = new String[PhsCnt+1];
		query  = "SELECT rst_start_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_start_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rstDate;
		rstDate = schBO.completeFirstDate(data);	//실적시작일중 제일작일날자
		update = "UPDATE pjt_schedule set rst_start_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and level_no = '0'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* 해당노드가 해당PHASE의 마지막 STEP의 노드완료이면 해당STEP의 완료정보을 입력한다.
	*******************************************************************/
	public String checkCompleteSchedule(String pjt_code) throws Exception
	{
		String query="",rtnData="N";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//1.PHASE단계의 전체갯수
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCnt = rs.getInt(1);
	
		//2.PHASE의 계획일수 등록갯수
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1' and plan_cnt != 0";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCompCnt = rs.getInt(1);
	
		if(PhsCnt == PhsCompCnt) rtnData="Y";

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return rtnData;
	}

	/*******************************************************************
	* 해당과제의 Activity의 첫노드와 마지막노드를 찾기
	* [계획입력시 처음과 마지막일자가 맞는지 검사하기위해]
	*******************************************************************/
	public String checkFLnodeSchedule(String pjt_code) throws Exception
	{
		String query="",rtnData="";
		int cnt = 0;
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//1.Activity만의 갯수 구하기
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '3'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			cnt = rs.getInt(1);
		}

		//2.Activity만 쿼리하기
		query  = "SELECT child_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '3' order by child_node ASC";
		rs = stmt.executeQuery(query);
		int n=1;
		while(rs.next()) {
			if(n == 1) rtnData = rs.getString("child_node")+"|";
			else if(n == cnt) rtnData += rs.getString("child_node"); 
			n++;
		}
	
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return rtnData;
	}
	/*******************************************************************
	* 해당과제의 전체 weight값 구하기
	* [해당과제의 level_no=0인값 구한면됨]
	*******************************************************************/
	public String getTotalWeight(String pjt_code) throws Exception
	{
		String query="",rtnData="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query  = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '0'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			rtnData = Double.toString(rs.getDouble("weight"));
		}

		stmt.close();
		rs.close();
		return rtnData;
	}

	/*********************************************************************
	 	작업지시내용을 노드담당자에게 전자우편으로 보내기
	*********************************************************************/
	public void sendMailToUser(String mgr_id,String mgr_name,String rec_id,String rec_name,
		String pjt_code,String pjt_name,String node_code,String node_name,
		String psd,String ped,String csd,String ced,String rsd,String remark) throws Exception 
	{	
		String pid = getID();								//관리번호
		String subject = "";								//제목
		String user_id = "", user_name = "", rec = "";		//작성자 사번,이름,수신자List
		String write_date = anbdt.getTime();				//전자우편 전송일자
		String delete_date = anbdt.getAddMonthNoformat(1);	//삭제예정일자

		//1.작성자[과제PM] 정보
		user_id = mgr_id;										//작성자 사번
		user_name = mgr_name;									//작성자 이름
		rec = rec_id+"/"+rec_name+";";							//수신자 
		subject = "["+pjt_code+" "+pjt_name+"] 에대한 작업지시 사항";	//제목
		String bon_path = "/post/"+user_id+"/text_upload";		//본문패스
		String filename = pid;									//본문저장 파일명

		//2.전자우편으로 보내기
		Statement stmt = null;
		stmt = con.createStatement();
		String letter="";
			letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
			letter += pid+"','"+subject+"','"+user_id+"','"+user_name+"','"+write_date+"','"+rec_id+"','"+"0"+"','"+delete_date+"')";
		stmt.executeUpdate(letter);	
		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + user_id + "','" + user_name + "','" + write_date + "','" + rec + "','" + "0" + "','";
			master += "email" + "','" + "CFM" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";
		stmt.executeUpdate(master);

		//3.본문파일 만들기
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>노드작업지시사항</title></head>";
			content += "<body>";
			content += "<h3>노드작업 지시사항</h3>";
			content += "<ul>";
			content += "<li>과제이름 : "+pjt_code+" "+pjt_name+"</li>";
			content += "<li>노드이름 : "+node_code+" "+node_name+"</li>";
			content += "<li>계획기간 : "+psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8)+" ~ "+ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8)+"</li>";
			if(csd.length() != 0) 
				content += "<li>수정기간 : "+csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8)+" ~ "+ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8)+"</li>";
			content += "<li>실적시작 : "+rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8)+" ~ "+"</li>";
			content += "<li>지시사항 : <pre>"+remark+"</pre></li>";
			content += "</ul>";
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//저장될 path
		write.setFilepath(path);												//directory생성하기
		write.WriteHanguel(path,filename,content);								//내용 파일로 저장하기

		stmt.close();
	}

	/*******************************************************************
	* 해당노드/해당STEP weight입력하기
	*******************************************************************/
	public void updateWeight(String pjt_code,String parent_node,String child_node,String weight) throws Exception
	{
		String query="",update="",phase="";
		double sw=0.0,pw=0.0,tw=0.0;		//step weight, phase weight, 전체 weight
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
	
		//0.STEP의 입력된 weight값
		sw = Double.parseDouble(weight);

		//1.STEP의 Activity전체 weight을 구한다.
		query  = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"' ";
		query += "and child_node !='"+child_node+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			sw += rs.getDouble("weight");
		}
		
		//2.PHASE코드 찾기
		query  = "SELECT parent_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and child_node = '"+parent_node+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			phase = rs.getString("parent_node");
		}

		//3.PHASE의 Activity전체 weight을 구한다.
		query  = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' ";
		query += "and child_node !='"+parent_node+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			pw += rs.getDouble("weight"); 
		}
		pw += sw;		//atcivity의 수정부분에 해당되는 step
		
		//4.STEP weight값 입력하기
		update = "UPDATE pjt_schedule set weight='"+sw+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"'";
		stmt.executeUpdate(update);

		//5.PHASE weight값 입력하기
		update = "UPDATE pjt_schedule set weight='"+pw+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+phase+"'";
		stmt.executeUpdate(update);

		//6.해당과제 전체phase weight구하기
		query  = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1' and child_node != '"+phase+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			tw = rs.getDouble("weight");
		}
		tw += pw;

		//7.전체 weight값 입력하기
		update = "UPDATE pjt_schedule set weight='"+tw+"' ";
		update += "where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);
	
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();	
	}

	/******************************************************************************
	// ID을 구하는 메소드
	******************************************************************************/
	private String getID()
	{
		com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("000");	
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		String y = first.format(now);
		String s = last.format(now);	
		String ID = y + nmf.toDigits(Integer.parseInt(s));
		return ID;
	}
}
