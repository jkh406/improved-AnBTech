package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtEventDAO
{
	private Connection con;
	com.anbtech.pjt.business.pjtScheduleBO schBO = new com.anbtech.pjt.business.pjtScheduleBO(); //일수계산
	com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();	//파일로 담기[메일]
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//날자처리
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//문자열 처리

	private String[][] item = null;				//프로세스정보를 배열로 담기
	private int an = 0;							//items의 배열 증가
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtEventDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtEventDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기 [해당과제/해당노드의 전체수량]
	//*******************************************************************/
	private int getTotalCount(String pjt_code,String node_code,String pjtWord,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"'";
		query += " and wm_type like '%"+pjtWord+"%'";
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
	public ArrayList getEventList (String login_id,String pjt_code,String node_code,String pjtWord,String sItem,String sWord,
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
		total_cnt = getTotalCount(pjt_code,node_code,pjtWord,sItem,sWord);

		//금일일자 구하기
		String todate = anbdt.getDate();
			
		//query문장 만들기
		query = "SELECT * FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"'";
		query += " and wm_type like '%"+pjtWord+"%'";
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
				table.setProgress(rs.getDouble("progress"));

				String user_id = rs.getString("user_id");
				table.setUserId(user_id);
				
				table.setUserName(rs.getString("user_name"));

				String in_date = rs.getString("in_date");
				table.setInDate(in_date);

				String wm_type = rs.getString("wm_type");
				table.setWmType(wm_type);
				table.setEvtContent(rs.getString("evt_content"));
				table.setEvtNote(rs.getString("evt_note"));
				table.setEvtIssue(rs.getString("evt_issue"));

				//보기,수정,삭제가능 표시 [단,수정/삭제는 당일에 한해 작성자만이 가능함]
				String subMod="",subDel="",subView="";
				if(user_id.equals(login_id) && todate.equals(in_date) && !(wm_type.equals("A")) && !(wm_type.equals("R")) ) {
					subView = "<a href=\"javascript:eventView('"+pid+"','"+wm_type+"');\">보기</a>";
					subMod = "<a href=\"javascript:eventModify('"+pid+"');\">수정</a>";
					//subDel = "<a href=\"javascript:eventDelete('"+pid+"');\">삭제</a>";
				} 
				else {
					subView = "<a href=\"javascript:eventView('"+pid+"','"+wm_type+"');\">보기</a>";
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
	// 해당과제 해당노드중 지정내용상세보기
	//*******************************************************************/	
	public ArrayList getEventRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_event where pid='"+pid+"'";	
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

	/*******************************************************************
	* 주간/월간 실적 입력하기
	*******************************************************************/
	public void inputEvent(String pjt_code,String pjt_name,String node_code,String node_name,String progress,
		String user_id,String user_name,String in_date,String wm_type,String evt_content,
		String evt_note,String evt_issue,String node_status) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//관리번호 찾기
		String pid = getID();

		//실적입력하기 : pjt_event
		input = "INSERT INTO pjt_event(pid,pjt_code,pjt_name,node_code,node_name,progress,user_id,";
		input += "user_name,in_date,wm_type,evt_content,evt_note,evt_issue) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+node_code+"','"+node_name+"','"+progress+"','"+user_id+"','";
		input += user_name+"','"+in_date+"','"+wm_type+"','"+evt_content+"','"+evt_note+"','"+evt_issue+"')";
		stmt.executeUpdate(input);

		//진행율 입력하기 : pjt_schedule
		String update = "UPDATE pjt_schedule set progress='"+progress+"',node_status='"+node_status+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//step,phase,과제전체 진행율 수정하기 : pjt_schedule
		updateProgress(pjt_code,node_code,progress);

		//노드지시사항없이 진행할 경우 노드상태및 실적시작일을 입력한다.
		updateStatus(pjt_code,node_code);

		//step,phase의 실적종료일 수정하기 : pjt_schedule
		in_date = str.repWord(in_date,"-","");
		update = "UPDATE pjt_schedule set rst_end_date='"+in_date+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		updateResultEndDate(pjt_code,node_code,in_date);

		stmt.close();
	}
	/*******************************************************************
	* 주간/월간 실적 수정하기
	*******************************************************************/
	public void updateEvent(String pid,String pjt_code,String node_code,String progress,
		String in_date,String wm_type,String evt_content,String evt_note,String evt_issue) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//실적정보 수정하기 [pjt_event]
		update = "UPDATE pjt_event set progress='"+progress+"',in_date='"+in_date+"',wm_type='"+wm_type;
		update += "',evt_content='"+evt_content+"',evt_note='"+evt_note+"',evt_issue='"+evt_issue;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//진행율 수정하기 : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+progress+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//step,phase,과제전체 진행율 수정하기 : pjt_schedule
		updateProgress(pjt_code,node_code,progress);

		//step,phase의 실적종료일 수정하기 : pjt_schedule
		in_date = str.repWord(in_date,"-","");
		update = "UPDATE pjt_schedule set rst_end_date='"+in_date+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		updateResultEndDate(pjt_code,node_code,in_date);

		stmt.close();
	}
	/*******************************************************************
	* 주간/월간 실적 삭제하기
	*******************************************************************/
	public void deleteEvent(String pid) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String delete = "",query="",update="";
		String pjt_code="",node_code="",progress="";
		stmt = con.createStatement();

		//최근의 진행율 찾기 : pjt_event
		query = "SELECT pjt_code,node_code FROM pjt_event where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			pjt_code=rs.getString("pjt_code");
			node_code=rs.getString("node_code");
		}
		query = "SELECT progress FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"' ";
		query += "order by in_date DESC";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			progress=rs.getString("progress");
		}

		//삭제하기
		delete = "DELETE from pjt_event where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		//진행율 수정하기 : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+progress+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//step,phase,과제전체 진행율 수정하기 : pjt_schedule
		updateProgress(pjt_code,node_code,progress);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* STEP,PHASE 진행율 구하여 update하기 : pjt_schedule
	*******************************************************************/
	public void updateProgress(String pjt_code,String child_node,String progress) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String delete = "",query="",update="";
		String step="",phase="";
		double sp=0.0,pp=0.0,tp=0.0;			//Step진행율, Phase진행율, 과제진행율
		double sw=0.0,pw=0.0,tw=0.0;			//Step weight, Phase weight, 과제weight
		stmt = con.createStatement();

		//------------- step/phase 코드 찾기 ---------------//
		//1.step코드[parent_node]찾기 : pjt_schedule
		query = "SELECT parent_node FROM pjt_schedule where pjt_code='"+pjt_code+"' and child_node='"+child_node+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			step=rs.getString("parent_node");
		}

		//2.phase코드 찾기 : pjt_schedule
		query = "SELECT parent_node FROM pjt_schedule where pjt_code='"+pjt_code+"' and child_node='"+step+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			phase=rs.getString("parent_node");
		}

		//------------- step/phase/전체 weight 찾기 ---------------//
		//3.해당step의 weight구하기 : pjt_schedule
		query = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and child_node='"+step+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			sw=rs.getDouble("weight");
		}

		//4.해당phase의 weight구하기 : pjt_schedule
		query = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and child_node='"+phase+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			pw=rs.getDouble("weight");
		}

		//5.해당과제의 weight구하기 : pjt_schedule
		query = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and level_no='0'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			tw=rs.getDouble("weight");
		}

		//------------- step 진행율 찾기 ---------------//
		//6.해당step의 해당Activity진행율 구하기 : pjt_schedule
		query = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+step+"' and child_node ='"+child_node+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			sp=(rs.getDouble("weight") / sw) * Double.parseDouble(progress);
		}

		//7.해당step의 진행율 구하기 [현재activity제외] : pjt_schedule
		query = "SELECT weight,progress FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+step+"' and child_node !='"+child_node+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			sp +=(rs.getDouble("weight") / sw) * rs.getDouble("progress");
		}

		//------------- phase 진행율 찾기 ---------------//
		//8.해당Phase의 해당Activity진행율 구하기 : pjt_schedule
		pp = sw / pw * sp;
		
		//9.해당Phase의 진행율 구하기 [현재activity제외] : pjt_schedule
		query = "SELECT weight,progress FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+phase+"' and child_node !='"+step+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			pp +=(rs.getDouble("weight") / pw) * rs.getDouble("progress");
		}

		//------------- 전체 진행율 찾기 ---------------//
		//10.해당Phase의 해당Activity진행율 구하기 : pjt_schedule
		tp = pw / tw * pp;
		
		//11.해당Phase의 진행율 구하기 [현재activity제외] : pjt_schedule
		query = "SELECT weight,progress FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and level_no='1' and child_node !='"+phase+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			tp +=(rs.getDouble("weight") / tw) * rs.getDouble("progress");
		}

		//------------- 진행율 등록 ---------------//
		//step 진행율 수정하기 : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+sp+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+step+"'";
		stmt.executeUpdate(update);

		//phase 진행율 수정하기 : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+pp+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+phase+"'";
		stmt.executeUpdate(update);

		//전체 진행율 수정하기 : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+tp+"' ";
		update += "where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// 노드지시사항없이 진행할 경우 노드상태및 실적시작일을 입력한다.
	//*******************************************************************/	
	public void	updateStatus(String pjt_code,String node_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",update="",node_status="";	
		stmt = con.createStatement();

		//현노드 상태 알기
		query = "SELECT node_status FROM pjt_schedule where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		rs = stmt.executeQuery(query);

		if(rs.next()) { node_status = rs.getString("node_status");}

		//노드상태가 '0:진행전'일때만 계속
		if(!node_status.equals("0")) { stmt.close(); rs.close(); return; }

		//노드상태 '1:진행중'으로 바꾸기
		update = "UPDATE pjt_schedule set node_status='1' where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//실적시작일 입력하기
		String todate = anbdt.getDateNoformat();
		update = "UPDATE pjt_schedule set rst_start_date='"+todate+"' ";
		update +="where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//상위단 step,phase의 실적시작일 입력하기
		updateResultStartDate(pjt_code,node_code,todate);

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

	//*******************************************************************
	// STEP,PHASE의 실적 종료일만 입력하기
	//*******************************************************************/	
	public void	updateResultEndDate(String pjt_code,String child_node,String rst_end_date) throws Exception
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

		//6.해당 Activity의 종료만 쿼리하기 [해당노드의 STEP의 실적종료일에 입력하기]
		data = new String[ActCnt+1];
		query  = "SELECT rst_end_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+step+"' and level_no = '3'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_end_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rst_end_date;
		rstDate = schBO.completeLastDate(data);	//실적시작일중 제일큰날자
		update = "UPDATE pjt_schedule set rst_end_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and child_node = '"+step+"'";
		stmt.executeUpdate(update);

		//6.해당 Step의 종료일만 쿼리하기 [해당노드의 PHASE의 실적종료일에 입력하기]
		data = new String[StpCnt+1];
		query  = "SELECT rst_end_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_end_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rstDate;
		rstDate = schBO.completeLastDate(data);	//실적종료일중 제일큰날자
		update = "UPDATE pjt_schedule set rst_end_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and child_node = '"+phase+"'";
		stmt.executeUpdate(update);

		//7.해당 Phase의 종료일만 쿼리하기 [해당노드의 과제의 실적종료일에 입력하기]
		data = new String[PhsCnt+1];
		query  = "SELECT rst_end_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_end_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rstDate;
		rstDate = schBO.completeLastDate(data);	//실적종료일중 제일큰날자
		update = "UPDATE pjt_schedule set rst_end_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and level_no = '0'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// 해당과제 Activity리스트 쿼리하기
	//*******************************************************************/	
	public ArrayList getPjtActivityRead (String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' and level_no = '3' order by child_node asc";	
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
	// 해당과제 Activity리스트 쿼리하기
	//*******************************************************************/	
	public ArrayList getPjtNodeRead (String pjt_code,String child_node) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' and child_node = '"+child_node+"' order by child_node asc";	
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
	/*******************************************************************
	* 노드작업완료 승인요청하기
	*******************************************************************/
	public String nodeAppReq(String pjt_code,String pjt_name,String node_code,String node_name,
		String user_id,String user_name,String evt_content,String evt_note,String evt_issue) throws Exception
	{
		String update = "",query="",red="",node_status="",rtnData="Y";
		ResultSet rs = null;
		Statement stmt = null;
		stmt = con.createStatement();

		//해당노드 승인요청 상태 알아보기
		query = "SELECT rst_end_date,node_status FROM pjt_schedule where pjt_code='"+pjt_code;
		query += "' and child_node = '"+node_code+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			red = rs.getString("rst_end_date");	if(red == null) red = "";
			node_status = rs.getString("node_status");
		}

		//검사하기
		if(node_status.equals("2")) {		//노드 완료상태
			stmt.close();
			rs.close();
			rtnData = "A";
			return rtnData;
		}
		else rtnData = "Y";

		//노드완료 승인요청했음을 PM에게 전자우편으로 알리기
		sendMailToPM(user_id,user_name,pjt_code,pjt_name,node_code,node_name,evt_content,evt_note,evt_issue);

		stmt.close();
		rs.close();
		return rtnData;
	}

	/*********************************************************************
	 	작업완료내용을 PM에게 전자우편으로 보내기
	*********************************************************************/
	public void sendMailToPM(String u_id,String u_name,String pjt_code,String pjt_name,String node_code,String node_name,
		String evt_content,String evt_note,String evt_issue) throws Exception 
	{	
		String pid = getID();								//관리번호
		String subject = "";								//제목
		String user_id = "", user_name = "", rec = "";		//작성자 사번,이름,수신자List
		String write_date = anbdt.getTime();				//전자우편 전송일자
		String delete_date = anbdt.getAddMonthNoformat(1);	//삭제예정일자

		//1.작성자[과제PM] 정보
		user_id = u_id;											//작성자 사번
		user_name = u_name;										//작성자 이름
		subject = "노드완료 승인요청";								//제목
		String bon_path = "/post/"+user_id+"/text_upload";		//본문패스
		String filename = pid;									//본문저장 파일명

		//2.과제PM 구하기
		rec = searchPjtPM(pjt_code)+";";						//수신자 [사번/이름;]
		String rec_id = rec.substring(0,rec.indexOf("/"));
//		rec = rec_id+"/"+rec_name+";";							//수신자 

		//3.전자우편으로 보내기
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

		//4.본문파일 만들기
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>노드작업 완료승인 요청</title></head>";
			content += "<body>";
			content += "<h3>노드작업 완료내용</h3>";
			content += "<ul>";
			content += "<li>과제이름 : "+pjt_code+" "+pjt_name+"</li>";
			content += "<li>노드이름 : "+node_code+" "+node_name+"</li>";
			content += "<li>주요내용 : <pre>"+evt_content+"</pre></li>";
			content += "<li>주요문제 : <pre>"+evt_note+"</pre></li>";
			content += "<li>주요이슈 : <pre>"+evt_issue+"</pre></li>";
			content += "</ul>";
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//저장될 path
		write.setFilepath(path);												//directory생성하기
		write.WriteHanguel(path,filename,content);								//내용 파일로 저장하기

		stmt.close();
	}
	/*******************************************************************
	* 해당과제의 PM구하기
	*******************************************************************/
	public String searchPjtPM(String pjt_code) throws Exception
	{
		String query="",rtnData="";
		ResultSet rs = null;
		Statement stmt = null;
		stmt = con.createStatement();

		query = "SELECT pjt_mbr_id FROM pjt_general where pjt_code='"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			rtnData = rs.getString("pjt_mbr_id");	
		}

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



