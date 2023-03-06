package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtPmCostDAO
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
	public pjtPmCostDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtPmCostDAO() 
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
	// 해당과제 그래프을 그리기위한 단위[금액]구하기 
	// [실적/예산 금액중 가장 큰금액을 이용]
	//*******************************************************************/	
	public int getCostUnit (String pjt_code) throws Exception
	{
		//변수 초기화
		int unit = 1;			//단위금액
		int cost = 100;			//쿼리금액
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;

		//쿼리문장
		stmt = con.createStatement();
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//가장큰금액은 얼마
		if(rs.next()) { 			
				if(rs.getInt("plan_labor") > cost)		cost = rs.getInt("plan_labor");
				if(rs.getInt("plan_sample") > cost)		cost = rs.getInt("plan_sample");
				if(rs.getInt("plan_metal") > cost)		cost = rs.getInt("plan_metal");
				if(rs.getInt("plan_mup") > cost)		cost = rs.getInt("plan_mup");
				if(rs.getInt("plan_oversea") > cost)	cost = rs.getInt("plan_oversea");
				if(rs.getInt("plan_plant") > cost)		cost = rs.getInt("plan_plant");

				if(rs.getInt("result_labor") > cost)	cost = rs.getInt("result_labor");
				if(rs.getInt("result_sample") > cost)	cost = rs.getInt("result_sample");
				if(rs.getInt("result_metal") > cost)	cost = rs.getInt("result_metal");
				if(rs.getInt("result_mup") > cost)		cost = rs.getInt("result_mup");
				if(rs.getInt("result_oversea") > cost)	cost = rs.getInt("result_oversea");
				if(rs.getInt("result_plant") > cost)	cost = rs.getInt("result_plant");
		}

		//단위금액 구하기
		unit = cost / 100;
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return unit;
	}

	//*******************************************************************
	// 해당과제 그래프을 그리기위해 실적및예산을 단위금액으로 나눈갯수 구하기 
	// [실적/예산 금액 / 단위금액]
	//*******************************************************************/	
	public ArrayList getAccountNumber (String pjt_code) throws Exception
	{
		//변수 초기화
		int unit = getCostUnit (pjt_code);			//단위금액
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;

		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//쿼리문장
		stmt = con.createStatement();
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//내용 담기
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

				if((rs.getInt("plan_labor")/unit) == 0) table.setPlanLaborAc("1");
				else table.setPlanLaborAc(Integer.toString(rs.getInt("plan_labor")/unit));
				if((rs.getInt("plan_sample")/unit) == 0) table.setPlanSampleAc("1");
				else table.setPlanSampleAc(Integer.toString(rs.getInt("plan_sample")/unit));
				if((rs.getInt("plan_metal")/unit) == 0) table.setPlanMetalAc("1");
				else table.setPlanMetalAc(Integer.toString(rs.getInt("plan_metal")/unit));
				if((rs.getInt("plan_mup")/unit) == 0) table.setPlanMupAc("1");
				else table.setPlanMupAc(Integer.toString(rs.getInt("plan_mup")/unit));
				if((rs.getInt("plan_oversea")/unit) == 0) table.setPlanOverseaAc("1");
				else table.setPlanOverseaAc(Integer.toString(rs.getInt("plan_oversea")/unit));
				if((rs.getInt("plan_plant")/unit) == 0) table.setPlanPlantAc("1");
				else table.setPlanPlantAc(Integer.toString(rs.getInt("plan_plant")/unit));

				if((rs.getInt("result_labor")/unit) == 0) table.setResultLaborAc("1");
				else table.setResultLaborAc(Integer.toString(rs.getInt("result_labor")/unit));
				if((rs.getInt("result_sample")/unit) == 0) table.setResultSampleAc("1");
				else table.setResultSampleAc(Integer.toString(rs.getInt("result_sample")/unit));
				if((rs.getInt("result_metal")/unit) == 0) table.setResultMetalAc("1");
				else table.setResultMetalAc(Integer.toString(rs.getInt("result_metal")/unit));
				if((rs.getInt("result_mup")/unit) == 0) table.setResultMupAc("1");
				else table.setResultMupAc(Integer.toString(rs.getInt("result_mup")/unit));
				if((rs.getInt("result_oversea")/unit) == 0) table.setResultOverseaAc("1");
				else table.setResultOverseaAc(Integer.toString(rs.getInt("result_oversea")/unit));
				if((rs.getInt("result_plant")/unit) == 0) table.setResultPlantAc("1");
				else table.setResultPlantAc(Integer.toString(rs.getInt("result_plant")/unit));
				
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




