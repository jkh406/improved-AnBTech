package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtNodeAppDAO
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
	public pjtNodeAppDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtNodeAppDAO() 
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
		//1.배열만들기
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][23];

		//2. 배열에 담는다.
		saveItemsArray(pjt_code,level_no,parent_node);

		//3.ArrayList에 담기
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		String node_name="";
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
			
			if(item[i][5].equals("3"))		//pjt_cod,parent_node,child_node
				node_name = "<a href=\"javascript:detailNode('"+item[i][1]+"','"+item[i][3]+"','"+item[i][4]+"');\">"+item[i][4]+" "+item[i][6]+"</a>";
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
	 * 해당과제의 모든항목을 배열에서 ArrayList로 담는다 : Gantt Chart용 [진행율/계획일 or  수정일]
	 *********************************************************************/
	public ArrayList getPjtGanttChart(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//1.배열만들기
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][23];

		//2. 배열에 담는다.
		saveItemsArray(pjt_code,level_no,parent_node);

		//3.ArrayList에 담기
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		String node_name="";
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
			
			if(item[i][5].equals("1"))			//phase
				node_name = "<font color='blue'><b>"+item[i][4]+" "+item[i][6]+"</b></font>";
			else if(item[i][5].equals("2"))		//step
				node_name = "<font color='darkred'><b>"+item[i][4]+" "+item[i][6]+"</b></font>";
			else								//ativity
				node_name = "<font color='black'>"+item[i][4]+" "+item[i][6]+"</font>";
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
	 * 해당과제의 모든항목을 배열에서 ArrayList로 담는다 : Gantt Chart용 [실적일/계획일 or  수정일]
	 *********************************************************************/
	public ArrayList getPjtBarChart(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//1.배열만들기
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][23];

		//2. 배열에 담는다.
		saveItemsArray(pjt_code,level_no,parent_node);

		//3.ArrayList에 담기
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		String node_name="";
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
			
			if(item[i][5].equals("1"))			//phase
				node_name = "<font color='blue'><b>"+item[i][4]+" "+item[i][6]+"</b></font>";
			else if(item[i][5].equals("2"))		//step
				node_name = "<font color='darkred'><b>"+item[i][4]+" "+item[i][6]+"</b></font>";
			else								//ativity
				node_name = "<font color='black'>"+item[i][4]+" "+item[i][6]+"</font>";
			table.setNodeName(node_name);

			table.setWeight(Double.parseDouble(item[i][7]));
			table.setUserId(item[i][8]);
			table.setUserName(item[i][9]);
			table.setPjtNodeMbr(item[i][10]);
			table.setPlanStartDate(item[i][11]);
			table.setPlanEndDate(item[i][12]);
			table.setChgStartDate(item[i][13]);			
			table.setChgEndDate(item[i][14]);

			table.setRstStartDate(item[i][15]);					//실적시작일
			table.setRstEndDate(item[i][16]);					//실적종료일
			//최종 실적입력일 찾기
			if(item[i][16].length() == 0) {
				table.setRstEndDate(searchRstEndDate(item[i][1],item[i][4],item[i][5]));				
			}

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
	//	실적작성테이블에서 입력한 해당과제 노드의 최종실적을 찾기
	//*******************************************************************/	
	public String searchRstEndDate (String pjt_code,String node_code,String level_no) throws Exception
	{
		com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//레벨별 실적일자 구하기
		if(level_no.equals("0")) {				//과제전체 최근 실적입력일	
			query  = "SELECT in_date FROM pjt_event ";
			query += "where pjt_code = '"+pjt_code+"' order by in_date DESC";
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				rtn = rs.getString("in_date");
			}
			rtn = str.repWord(rtn,"-","");
		} else {								//해당노드의 최근 실적입력일 
			query  = "SELECT in_date FROM pjt_event ";
			query += "where pjt_code = '"+pjt_code+"' and node_code ='"+node_code+"' order by in_date DESC";
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				rtn = rs.getString("in_date");
			}
			rtn = str.repWord(rtn,"-","");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;	
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
			String node_status = rs.getString("node_status"); if(node_status == null) node_status = "";
			table.setNodeStatus(node_status);
			
			table.setProgress(rs.getDouble("progress"));
			String remark = rs.getString("remark"); if(remark == null) remark = "";
			table.setRemark(remark);
					
			table_list.add(table);
		}
		rs.close();
		stmt.close(); 
		return table_list;
		
	}

	//*******************************************************************
	// 자신의 전체 과제중 진행중인 과제LIST
	//*******************************************************************/	
	public ArrayList getProjectList (String login_id,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		query = "SELECT pjt_status,pjt_code,pjt_name FROM pjt_general where pjt_mbr_id like '%"+login_id+"%'";	
		query += " and pjt_status = '1'";
		query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 
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
	* 과제일정정보 노드작업완료 승인시  --------------> PM
	* 핵심:실적완료일,상태,실적일수
	*******************************************************************/
	public void updateNodeApproval(String pid,String pjt_code,String parent_node,String child_node,
		String rst_start_date,String rst_end_date,String node_status) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		String result_cnt = schBO.getPeriodDate(rst_start_date,rst_end_date);		//실적일수

		//해당노드의 상태코드를 바꿔준다.
		update = "UPDATE pjt_schedule set rst_end_date='"+rst_end_date+"',result_cnt='"+result_cnt;
		update += "',node_status='"+node_status+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//해당노드가 해당STEP의 마지막 Activity의 노드완료이면 해당STEP의 완료정보을 입력한다.
		updateStepApproval(pjt_code,parent_node,child_node,rst_start_date,rst_end_date);


		stmt.close();
	}

	/*******************************************************************
	* 해당노드가 해당STEP의 마지막 Activity의 노드완료이면 해당STEP의 완료정보을 입력한다.
	*******************************************************************/
	public void updateStepApproval(String pjt_code,String parent_node,String child_node,
		String rst_start_date,String rst_end_date) throws Exception
	{
		String query="",update="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = null;
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

		//3.현재 등록된 수정일 Activity의 갯수 파악한다.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"' and chg_cnt != 0";
		rs = stmt.executeQuery(query);
		rs.next();
		int ChgCnt = rs.getInt(1);

		//4.현재 등록된 Activity의 정보를 배열에 담는다. 
		//  그리고 null이면 파라미터 날자를 담는다.
		data = new String[ActCnt][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"'";
		rs = stmt.executeQuery(query);
		int n = 0, rst = 0;	//배열번호,실적종료일이 null인갯수
		String cnode = "";				//자노드
		while(rs.next()) {
			cnode = rs.getString("child_node");

			data[n][0] = rs.getString("rst_start_date");
			if(cnode.equals(child_node) && (data[n][0] == null)) data[n][0] = rst_start_date;
			if(data[n][0].length() == 0) data[n][0] = "0";	//""으로 넘어 왔을때

			data[n][1] = rs.getString("rst_end_date");
			if(cnode.equals(child_node) && (data[n][1] == null)) data[n][1] = rst_end_date;
			else if(data[n][1] == null) rst++;
			if(data[n][1].length() == 0) data[n][1] = "0";	//""으로 넘어 왔을때
			n++;
		}

		//5.파라미터 정보와 비교하여 마지막인지 아닌지 판단한다. [마지막이 아니면 return, 마지막이면 등록]
		if(rst > 0) {						//기존에 입력된 내용으로 마지막이 아님
			stmt.close(); rs.close(); return; 
		}				
		

		//5.Activity에서 최초일과 최종일을 구한다.
		String[][] comDate = new String[2][2];	//실적일[시작,종료], 수정일[시작,종료]
		comDate = schBO.completeFirstLastDate(data);

		//6.일자가 0이 아니면 실적일[시작,종료] 입력한다.
		//실적일[시작,종료]을 해당 STEP에 입력한다.
		if(!comDate[0][0].equals("0")) {	
			update = "UPDATE pjt_schedule set rst_start_date='"+comDate[0][0]+"',rst_end_date='"+comDate[0][1];
			update += "', result_cnt='"+Integer.parseInt(comDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"' and level_no='2'";
			stmt.executeUpdate(update);
		}

		//7.해당STEP이 마지막이면 해당PHASE을 입력한다.
		updatePhaseApproval(pjt_code,parent_node,rst_start_date,rst_end_date);
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();	
	}

	/*******************************************************************
	* 해당노드가 해당PHASE의 마지막 STEP의 노드완료이면 해당STEP의 완료정보을 입력한다.
	*******************************************************************/
	public void updatePhaseApproval(String pjt_code,String child_node,
		String rst_start_date,String rst_end_date) throws Exception
	{
		String query="",update="",phase="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = null;
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
		
		//2.현재 등록된 STEP의 정보를 배열에 담는다. 
		//  그리고 null이면 파라미터 날자를 담는다.
		data = new String[PhsCnt][4];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
		rs = stmt.executeQuery(query);
		int n = 0, rst = 0;	//배열번호,실적종료일이 null인갯수
		String cnode = "";				//자노드
		while(rs.next()) {
			cnode = rs.getString("child_node");

			data[n][0] = rs.getString("rst_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = rst_start_date;
			if(data[n][0].length() == 0) data[n][0] = "0";	//""으로 넘어 왔을때

			data[n][1] = rs.getString("rst_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = rst_end_date;
			else if(data[n][1].length() == 0) rst++;
			if(data[n][1].length() == 0) data[n][1] = "0";	//""으로 넘어 왔을때	
			n++;
		}

		//3.파라미터 정보와 비교하여 마지막인지 아닌지 판단한다. [마지막이 아니면 return, 마지막이면 등록]
		if(rst > 0) {						//기존에 입력된 내용으로 마지막이 아님
			stmt.close(); rs.close(); return; 
		}				
	
		//4.Step에서 최초일과 최종일을 구한다.
		String[][] comDate = new String[2][2];	//실적일[시작,종료], 수정일[시작,종료]
		comDate = schBO.completeFirstLastDate(data);
			
		//5.일자가 0이 아니면 실적일을 입력한다.
		//실적일[시작,종료]을 해당 STEP에 입력한다.
		if(!comDate[0][0].equals("0")) {	
			update = "UPDATE pjt_schedule set rst_start_date='"+comDate[0][0]+"',rst_end_date='"+comDate[0][1];
			update += "', result_cnt='"+Integer.parseInt(comDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+phase+"' and level_no='1'";
			stmt.executeUpdate(update);
		}

		//---------------------------------------------------
		//6.과제전체 실적기간,일수 구하기
		//---------------------------------------------------
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
		n = 0; rst = 0;
		while(rs.next()) {
			data[n][0] = rs.getString("rst_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = rst_start_date;
			
			data[n][1] = rs.getString("rst_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = rst_end_date;
			else if(data[n][1].length() == 0) rst++;
			n++;
		}
		data[n][0] = comDate[0][0];
		data[n][1] = comDate[0][1];

		if(rst > 0) {
			stmt.close(); rs.close(); return;
		}

		String[][] pjtDate = new String[1][3];	//실적일[시작,종료],총일수
		pjtDate = schBO.completeFirstLastDate(data);

		update = "UPDATE pjt_schedule set result_cnt='"+Integer.parseInt(pjtDate[0][2]);
		update += "' and result_start_date='"+pjtDate[0][0]+"' and result_end_date='"+pjtDate[0][1]+"'";
		update += "' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* 과제일정정보 노드작업완료 승인반려시  --------------> PM
	* 핵심: 상태코드만 진행중으로 바꿔주기
	*******************************************************************/
	public void updateNodeReject(String pid) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//해당노드의 상태코드를 바꿔준다.
		update = "UPDATE pjt_schedule set node_status='1' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*********************************************************************
	 노드승인반려시 노드담당자에게 전자우편으로 보내기 : 노드승인시는 없음 
	*********************************************************************/
	public void sendMailToUser(String mgr_id,String mgr_name,String rec_id,String rec_name,
		String pjt_code,String pjt_name,String node_code,String node_name,
		String psd,String ped,String csd,String ced,String rsd,String red,String remark) throws Exception 
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
		subject = "["+pjt_name+"]과제/["+node_name+"]노드 에대한 반려사항";	//제목
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
			content += "<li>실적시작 : "+rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8)+" ~ "+red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8)+"</li>";
			content += "<li>반려사유 : <pre>"+remark+"</pre></li>";
			content += "</ul>";
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//저장될 path
		write.setFilepath(path);												//directory생성하기
		write.WriteHanguel(path,filename,content);								//내용 파일로 저장하기

		stmt.close();
	}
	/*******************************************************************
	* 해당 노드작업완료 승인/반려에 대한 코맨트  --------------> PM
	* 핵심:실적완료일만 없애기
	*******************************************************************/
	public void nodeApprovalRemark(String pid,String pjt_code,String child_node,String remark,String tag) throws Exception
	{
		String update = "",query="";
		Statement stmt = null;
		stmt = con.createStatement();

		//코맨트를 해당 승인요청 remark에 입력하기
		if(tag.equals("A")) {						//승인
			update = "UPDATE pjt_event set wm_type='A',remark='"+remark+"' where pid='"+pid+"'";
			stmt.executeUpdate(update);
		} else if(tag.equals("R")) {				//반려
			update = "UPDATE pjt_event set wm_type='R',remark='"+remark+"' where pid='"+pid+"'";
			stmt.executeUpdate(update);
		}

		stmt.close();
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

