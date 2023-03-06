package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtStfCostDAO
{
	private Connection con;
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//날자처리
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//문자열 처리
	com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("0,000");		//출력형태(금액)
	com.anbtech.util.normalFormat pro = new com.anbtech.util.normalFormat("0.0");		//출력형태(비율)

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtStfCostDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtStfCostDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기 [해당과제 비용List 전체수량]
	//*******************************************************************/
	private int getTotalCount(String pjt_code,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_cost where pjt_code='"+pjt_code+"'";
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
	// 해당과제의 비용 List
	//*******************************************************************/	
	public ArrayList getCostList (String pjt_code,String sItem,String sWord,
		String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		double plan_rst = 0.0;			//전체 계획 총비용
		double result_rst = 0.0;		//전체 실적 총비용
		double cost_rst = 0.0;			//개별 실적 총비용
		double progress = 0.0;			//비용실적 비율(사용율)

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		total_cnt = getTotalCount(pjt_code,sItem,sWord);

		//금일일자 구하기 (수정/삭제 허가여부판단)
		String todate = anbdt.getDate();		//yyyy-MM-dd
			
		//query문장 만들기
		query = "SELECT * FROM pjt_cost where pjt_code='"+pjt_code+"'";
		query += " and "+sItem+" like '%"+sWord+"%'"; 
		query += " order by pid DESC";
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

		//요청별[sWord]예산금액및 실적총금액 가져오기
		plan_rst = getPjtPlanCost(pjt_code,sWord);			//전체 계획 총비용
		result_rst = getPjtResultCost(pjt_code,sWord);		//전체 실적 총비용
	
		//페이지 skip 하기 (해당되지 않는 페이지의 내용)
		for(int i=1; i<current_page; i++) {
			if(i==1) cost_rst = result_rst;			//초기화한다.
			for(int j=0; j<max_display_cnt; j++) {
				if(rs.next()) {
					cost_rst -= rs.getDouble("node_cost");		//해당페이지만큼의 실적비용을 뺀다.
				}
			}
		}
		
		//페이지가 1페이지 일경우 초기화 하기
		if(current_page == 1)	cost_rst = result_rst;	//초기화한다.

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
				table.setUserId(rs.getString("user_id"));	
				table.setUserName(rs.getString("user_name"));
			
				table.setNodeCost(str.getMoneyFormat(rs.getString("node_cost"),""));
				table.setExchange(rs.getString("exchange"));
				String in_date = rs.getString("in_date");    //yyyy-MM-dd
				table.setInDate(in_date);
				table.setRemark(rs.getString("remark"));
				
				//비용명 작성
				String cost_type = rs.getString("cost_type");	if(cost_type == null) cost_type = "0";
				String cost_name = "기타경비";
				if(cost_type.equals("1")) cost_name = "인건비";
				else if(cost_type.equals("2")) cost_name = "SAMPLE";
				else if(cost_type.equals("3")) cost_name = "금형비";
				else if(cost_type.equals("4")) cost_name = "투자경비";
				else if(cost_type.equals("5")) cost_name = "규격승인비";
				else if(cost_type.equals("6")) cost_name = "시설투자비";
				else cost_name = "기타경비";
				table.setCostType(cost_name);

				//계획 총예산비용
				table.setCostExp(fmt.DoubleToString(plan_rst));

				//차액계산하기 (예산 - 실적)
				String dif_cost = "";
				if(show_cnt != 0) cost_rst -= rs.getDouble("node_cost");	//처음은 초기화값으로 진행
				dif_cost = fmt.DoubleToString(plan_rst-cost_rst);
				table.setDifCost(dif_cost);

				//실적비율 계산하기 : 계획값이 있을때만 실행한다.
				if(plan_rst > 0) 
					progress = Double.parseDouble(pro.DoubleToString(cost_rst/plan_rst*100));
				table.setProgress(progress);

				//보기,수정,삭제가능 표시 [단,수정/삭제는 당일에 한해 작성자만이 가능함]
				String subMod="",subDel="",subView="";
				if(todate.equals(in_date)) {		//당일
					subMod = "<a href=\"javascript:costModify('"+pid+"');\">수정</a>";
					subDel = "<a href=\"javascript:costDelete('"+pid+"');\">삭제</a>";
					subView = "<a href=\"javascript:costView('"+pid+"');\">보기</a>";
				} else {			
					subView = "<a href=\"javascript:costView('"+pid+"');\">보기</a>";
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
	// 해당과제 지정된 비용 상세히 보기
	//*******************************************************************/	
	public ArrayList getCostRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_cost where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		if(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));
				table.setNodeName(rs.getString("node_name"));	
				table.setUserId(rs.getString("user_id"));	
				table.setUserName(rs.getString("user_name"));
				table.setCostType(rs.getString("cost_type"));
				table.setNodeCost(rs.getString("node_cost"));
				table.setExchange(rs.getString("exchange"));
				table.setInDate(rs.getString("in_date"));
				table.setRemark(rs.getString("remark"));	
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* 비용 작성하기 : 신규등록
	*******************************************************************/
	public void inputCost(String pjt_code,String pjt_name,String node_code,String node_name,
		String user_id,String user_name,String cost_type,String node_cost,String exchange,
		String in_date,String remark) throws Exception
	{
		double exg=0;
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		if(exchange.length() > 0) exg = Double.parseDouble(exchange);

		String pid = getID();

		//항목별 지출비용 총금액 입력 : pjt_general
		setSumCostInput(pjt_code,cost_type,node_cost);

		//비용입력
		input = "INSERT INTO pjt_cost(pid,pjt_code,pjt_name,node_code,node_name,user_id,user_name,";
		input += "cost_type,node_cost,exchange,in_date,remark) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+node_code+"','"+node_name+"','";
		input += user_id+"','"+user_name+"','"+cost_type+"','"+Double.parseDouble(node_cost)+"','"+exg+"','";
		input += in_date+"','"+remark+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}
	/*******************************************************************
	* 비용 작성하기 : 수정하기
	*******************************************************************/
	public void updateCost(String pid,String pjt_code,String node_code,String node_name,String user_id,String user_name,
		String cost_type,String node_cost,String exchange,String in_date,String remark) throws Exception
	{
		double exg=0;
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		if(exchange.length() > 0) exg = Double.parseDouble(exchange);

		//항목별 지출비용 총금액 수정 : pjt_general
		setSumCostUpdate(pid,pjt_code,cost_type,node_cost);

		//비용수정
		update = "UPDATE pjt_cost set node_code='"+node_code+"',node_name='"+node_name;
		update +="',user_id='"+user_id+"',user_name='"+user_name+"',cost_type='"+cost_type;
		update +="',node_cost='"+Double.parseDouble(node_cost)+"',exchange='"+exg;
		update +="',in_date='"+in_date+"',remark='"+remark;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}
	
	/*******************************************************************
	* 비용 작성하기 : 삭제하기
	*******************************************************************/
	public void deleteCost(String pid) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();
		
		//해당항목의 금액을 pjt_general에서 해당계정의 금액을 차감한다.
		setSumCostDelete(pid);

		//해당항목을 삭제한다.
		delete = "DELETE from pjt_cost where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}

	//*******************************************************************
	// 해당항목 비용의 총지출 금액을 구하기 : 신규 입력시
	//*******************************************************************/	
	public void setSumCostInput(String pjt_code,String cost_type,String node_cost) throws Exception
	{
		//변수 초기화
		double sum = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",update="",cost_name="";	

		stmt = con.createStatement();

		//1.총금액을 구한다.
		query = "SELECT node_cost FROM pjt_cost where pjt_code='"+pjt_code+"' and cost_type='"+cost_type+"'";	
		rs = stmt.executeQuery(query);

		while(rs.next()) {
			sum += rs.getDouble("node_cost");
		}
		sum += Double.parseDouble(node_cost);

		//2.cost_type별 입력컬럼명을 구한다.
		if(cost_type.equals("1")) cost_name="result_labor";
		else if(cost_type.equals("2")) cost_name="result_sample";
		else if(cost_type.equals("3")) cost_name="result_metal";
		else if(cost_type.equals("4")) cost_name="result_mup";
		else if(cost_type.equals("5")) cost_name="result_oversea";
		else if(cost_type.equals("6")) cost_name="result_plant";

		//3.총금액을 pjt_general에 입력한다.
		update = "UPDATE pjt_general set "+cost_name+"='"+sum+"' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);


		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// 해당항목 비용의 총지출 금액을 구하기 : 수정 입력시
	//*******************************************************************/	
	public void setSumCostUpdate(String pid,String pjt_code,String cost_type,String node_cost) throws Exception
	{
		//변수 초기화
		double sum = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",update="",cost_name="";	

		stmt = con.createStatement();

		//1.총금액을 구한다. (단,
		query = "SELECT pid,node_cost FROM pjt_cost where pjt_code='"+pjt_code+"' and cost_type='"+cost_type+"'";	
		rs = stmt.executeQuery(query);

		String rpid = "";
		while(rs.next()) {
			rpid = rs.getString("pid");
			if(!rpid.equals(pid)) sum += rs.getDouble("node_cost");
		}
		sum += Double.parseDouble(node_cost);

		//2.cost_type별 입력컬럼명을 구한다.
		if(cost_type.equals("1")) cost_name="result_labor";
		else if(cost_type.equals("2")) cost_name="result_sample";
		else if(cost_type.equals("3")) cost_name="result_metal";
		else if(cost_type.equals("4")) cost_name="result_mup";
		else if(cost_type.equals("5")) cost_name="result_oversea";
		else if(cost_type.equals("6")) cost_name="result_plant";

		//3.총금액을 pjt_general에 입력한다.
		update = "UPDATE pjt_general set "+cost_name+"='"+sum+"' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);


		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// 해당항목 비용의 총지출 금액을 구하기 : 삭제시
	//*******************************************************************/	
	public void setSumCostDelete(String pid) throws Exception
	{
		//변수 초기화
		double dif_cost = 0.0;
		double node_cost = 0.0;
		double result_cost = 0.0;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",update="",pjt_code="",cost_type="",cost_name="";	

		stmt = con.createStatement();

		//1.해당pid로 입력된 금액및 과제코드를 구한다. : pjt_cost
		query = "SELECT node_cost,pjt_code,cost_type FROM pjt_cost where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			node_cost = rs.getDouble("node_cost");
			pjt_code = rs.getString("pjt_code");
			cost_type = rs.getString("cost_type");
		}

		//2.cost_type을 이용하여 cost_name의 column명 을 구한다
		if(cost_type.equals("1")) cost_name = "result_labor";			//인건비
		else if(cost_type.equals("2")) cost_name = "result_sample";		//sample
		else if(cost_type.equals("3")) cost_name = "result_metal";		//금형비
		else if(cost_type.equals("4")) cost_name = "result_mup";		//투자경비
		else if(cost_type.equals("5")) cost_name = "result_oversea";	//규격승인비
		else if(cost_type.equals("6")) cost_name = "result_plant";		//시설투자비

		//3.pjt_code 입력된 실적총금액을 구한다. : pjt_general
		query = "SELECT "+cost_name+" FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) result_cost = rs.getDouble(cost_name);

		//4.차액구하기
		dif_cost = result_cost - node_cost;

		//5.차액을 pjt_general에 입력한다.
		update = "UPDATE pjt_general set "+cost_name+"='"+dif_cost+"' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// 해당과제의 노드(activity) 쿼리하기 
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
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' order by level_no,child_node ASC";
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
	// 해당과제 QUERY하기 (개별 읽기 : 과제코드)
	//*******************************************************************/	
	public ArrayList getPjtRead (String pjt_code,String sWord) throws Exception
	{
		//변수 초기화
		double cost_rst = 0.0;		//총 실적비용금액
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		if(rs.next()) { 
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
				table.setCostExp(str.getMoneyFormat(rs.getString("cost_exp"),""));
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
				table.setPlanLabor(str.getMoneyFormat(rs.getString("plan_labor"),""));
				table.setPlanSample(str.getMoneyFormat(rs.getString("plan_sample"),""));
				table.setPlanMetal(str.getMoneyFormat(rs.getString("plan_metal"),""));
				table.setPlanMup(str.getMoneyFormat(rs.getString("plan_mup"),""));
				table.setPlanOversea(str.getMoneyFormat(rs.getString("plan_oversea"),""));
				table.setPlanPlant(str.getMoneyFormat(rs.getString("plan_plant"),""));
				table.setResultLabor(str.getMoneyFormat(rs.getString("result_labor"),""));
				table.setResultSample(str.getMoneyFormat(rs.getString("result_sample"),""));
				table.setResultMetal(str.getMoneyFormat(rs.getString("result_metal"),""));
				table.setResultMup(str.getMoneyFormat(rs.getString("result_mup"),""));
				table.setResultOversea(str.getMoneyFormat(rs.getString("result_oversea"),""));
				table.setResultPlant(str.getMoneyFormat(rs.getString("result_plant"),""));

				//총 실적 비용 계산하기
				cost_rst += rs.getDouble("result_labor");
				cost_rst += rs.getDouble("result_sample");
				cost_rst += rs.getDouble("result_metal");
				cost_rst += rs.getDouble("result_mup");
				cost_rst += rs.getDouble("result_oversea");
				cost_rst += rs.getDouble("result_plant");
				String rst_cost = fmt.DoubleToString(cost_rst);
				table.setCostRst(rst_cost);

				//차액계산하기 (예산 - 실적)
				String dif_cost = "";
				if(sWord.equals("")) 
					dif_cost = fmt.DoubleToString(rs.getDouble("cost_exp")-cost_rst);
				else if(sWord.equals("1")) 
					dif_cost = fmt.DoubleToString(rs.getDouble("plan_labor")-rs.getDouble("result_labor"));
				else if(sWord.equals("2")) 
					dif_cost = fmt.DoubleToString(rs.getDouble("plan_sample")-rs.getDouble("result_sample"));
				else if(sWord.equals("3")) 
					dif_cost = fmt.DoubleToString(rs.getDouble("plan_metal")-rs.getDouble("result_metal"));
				else if(sWord.equals("4")) 
					dif_cost = fmt.DoubleToString(rs.getDouble("plan_mup")-rs.getDouble("result_mup"));
				else if(sWord.equals("5")) 
					dif_cost = fmt.DoubleToString(rs.getDouble("plan_oversea")-rs.getDouble("result_oversea"));
				else if(sWord.equals("6")) 
					dif_cost = fmt.DoubleToString(rs.getDouble("plan_plant")-rs.getDouble("result_plant"));

				table.setDifCost(dif_cost);

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제 계정별 계획비용 구하기
	//*******************************************************************/	
	public double getPjtPlanCost (String pjt_code,String sWord) throws Exception
	{
		//변수 초기화
		double cost_rst = 0.0;		
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		String cost_name = "";

		stmt = con.createStatement();
		
		//sWord별 cost_name구하기
		if(sWord.equals(""))  			cost_name = "cost_exp";
		else if(sWord.equals("1"))		cost_name = "plan_labor";
		else if(sWord.equals("2"))		cost_name = "plan_sample";
		else if(sWord.equals("3"))		cost_name = "plan_metal";
		else if(sWord.equals("4"))		cost_name = "plan_mup";
		else if(sWord.equals("5"))		cost_name = "plan_oversea";
		else if(sWord.equals("6"))		cost_name = "plan_plant";

		//query문장 만들기
		query = "SELECT "+cost_name+" FROM pjt_general where pjt_code='"+pjt_code+"'";
		rs = stmt.executeQuery(query);

		//데이터 담기
		if(rs.next()) { 
			cost_rst = rs.getDouble(cost_name);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return cost_rst;
	}

	//*******************************************************************
	// 해당과제 계정별 실적비용 구하기
	//*******************************************************************/	
	public double getPjtResultCost (String pjt_code,String sWord) throws Exception
	{
		//변수 초기화
		double cost_rst = 0.0;		
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		String cost_name = "";

		stmt = con.createStatement();
		
		//sWord별 cost_name구하기
		if(sWord.equals("1"))		cost_name = "result_labor";
		else if(sWord.equals("2"))		cost_name = "result_sample";
		else if(sWord.equals("3"))		cost_name = "result_metal";
		else if(sWord.equals("4"))		cost_name = "result_mup";
		else if(sWord.equals("5"))		cost_name = "result_oversea";
		else if(sWord.equals("6"))		cost_name = "result_plant";

		//query문장 만들기
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";
		rs = stmt.executeQuery(query);

		//데이터 담기
		if(rs.next()) { 
			if(sWord.equals("")) {						//전체실적 총금액 구하기
				cost_rst += rs.getDouble("result_labor");
				cost_rst += rs.getDouble("result_sample");
				cost_rst += rs.getDouble("result_metal");
				cost_rst += rs.getDouble("result_mup");
				cost_rst += rs.getDouble("result_oversea");
				cost_rst += rs.getDouble("result_plant");
			} else cost_rst = rs.getDouble(cost_name);	//개별실적 총금액 구하기
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return cost_rst;
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




