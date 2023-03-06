package com.anbtech.psm.db;
import com.anbtech.psm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class psmModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat();	//포멧
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public psmModifyDAO(Connection con) 
	{
		this.con = con;
	}
	//--------------------------------------------------------------------
	//
	//		과제 category 및 환경 정보 읽기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// 과제 카테고리 정보 읽기
	//*******************************************************************/	
	public ArrayList readPsmCategoryList(String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmCategoryTable table = new com.anbtech.psm.entity.psmCategoryTable();
		
		query = "SELECT * FROM psm_category WHERE "+sItem+" like '%"+sWord+"%' order by comp_english,comp_korea asc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new psmCategoryTable();
			table.setPid(rs.getString("pid"));	
			table.setKoreaName(rs.getString("korea_name"));	
			table.setEnglishName(rs.getString("english_name"));	
			table.setKeyWord(rs.getString("key_word"));	
			table.setCompNo(rs.getString("comp_no"));	
			table.setCompKorea(rs.getString("comp_korea"));	
			table.setCompEnglish(rs.getString("comp_english"));	
			table_list.add(table);
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// 과제 카테고리 정보 읽기
	// env_status : 예비과제[1], 정식과제[2]
	//*******************************************************************/	
	public ArrayList readPsmCategoryList(String env_status,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmCategoryTable table = new com.anbtech.psm.entity.psmCategoryTable();
		
		query = "SELECT * FROM psm_category WHERE "+sItem+" like '%"+sWord+"%' ";
		if(env_status.equals("1")) query += "and comp_english='예비'";
		else query += "and comp_english !='예비'";
		query += " order by comp_korea asc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new psmCategoryTable();
			table.setPid(rs.getString("pid"));	
			table.setKoreaName(rs.getString("korea_name"));	
			table.setEnglishName(rs.getString("english_name"));	
			table.setKeyWord(rs.getString("key_word"));	
			table.setCompNo(rs.getString("comp_no"));	
			table.setCompKorea(rs.getString("comp_korea"));	
			table.setCompEnglish(rs.getString("comp_english"));	
			table_list.add(table);
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// 과제 종류 정보 읽기
	// env_status : 과제종류(1:예비등록과제, 2:정식등록과제)
	//*******************************************************************/	
	public ArrayList readPsmEnvList(String env_status) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmEnvTable table = new com.anbtech.psm.entity.psmEnvTable();
		
		query = "SELECT * FROM psm_env where env_type='P' and env_status='"+env_status+"' order by env_name asc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new psmEnvTable();
			table.setPid(rs.getString("pid"));	
			table.setEnvType(rs.getString("env_type"));	
			table.setEnvStatus(rs.getString("env_status"));	
			table.setEnvName(rs.getString("env_name"));	
			table_list.add(table);
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// 과제 종류 정보 읽기
	// env_status : 과제종류에 관계없이 전부 읽기
	//*******************************************************************/	
	public ArrayList readPsmEnvAllList() throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmEnvTable table = new com.anbtech.psm.entity.psmEnvTable();
		
		query = "SELECT * FROM psm_env where env_type='P' order by env_status asc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new psmEnvTable();
			table.setPid(rs.getString("pid"));	
			table.setEnvType(rs.getString("env_type"));	
			table.setEnvStatus(rs.getString("env_status"));	
			table.setEnvName(rs.getString("env_name"));	
			table_list.add(table);
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// 과제 진행상태 색상 정보 읽기
	//*******************************************************************/	
	public ArrayList readPsmColorList() throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmEnvTable table = new com.anbtech.psm.entity.psmEnvTable();
		
		query = "SELECT * FROM psm_env where env_type='C' order by env_status asc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new psmEnvTable();
			table.setPid(rs.getString("pid"));	
			table.setEnvType(rs.getString("env_type"));	
			table.setEnvStatus(rs.getString("env_status"));	
			table.setEnvName(rs.getString("env_name"));	
			table_list.add(table);
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		PSM MASTER 에 관한 메소드 정의
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 PSM_MASTER정보 읽기
	//*******************************************************************/	
	public psmMasterTable readPsmMaster(String pid) throws Exception
	{
		//변수 초기화
		String where = "";
		fmt.setFormat("0,000");
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmMasterTable table = new com.anbtech.psm.entity.psmMasterTable();
		
		query = "SELECT * FROM psm_master where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setPsmCode(rs.getString("psm_code"));	
			table.setPsmType(rs.getString("psm_type"));	
			table.setCompName(rs.getString("comp_name"));	
			table.setCompCategory(rs.getString("comp_category"));	
			table.setPsmKorea(rs.getString("psm_korea"));	
			table.setPsmEnglish(rs.getString("psm_english"));
			table.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
			table.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));

			table.setPsmPm(rs.getString("psm_pm"));
			table.setPsmPmDiv(rs.getString("psm_pm_div"));
			table.setPsmMgr(rs.getString("psm_mgr"));
			table.setPsmMgrDiv(rs.getString("psm_mgr_div"));
			table.setPsmBudget(rs.getString("psm_budget"));
			table.setPsmBudgetDiv(rs.getString("psm_budget_div"));
			table.setPsmUser(rs.getString("psm_user"));
			table.setPsmUserDiv(rs.getString("psm_user_div"));
			table.setPsmDesc(rs.getString("psm_desc"));
	
			table.setPlanSum(fmt.DoubleToString(rs.getDouble("plan_sum")));
			table.setPlanLabor(fmt.DoubleToString(rs.getDouble("plan_labor")));
			table.setPlanMaterial(fmt.DoubleToString(rs.getDouble("plan_material")));
			table.setPlanCost(fmt.DoubleToString(rs.getDouble("plan_cost")));
			table.setPlanPlant(fmt.DoubleToString(rs.getDouble("plan_plant")));

			table.setResultSum(fmt.DoubleToString(rs.getDouble("result_sum")));
			table.setResultLabor(fmt.DoubleToString(rs.getDouble("result_labor")));
			table.setResultMaterial(fmt.DoubleToString(rs.getDouble("result_material")));
			table.setResultCost(fmt.DoubleToString(rs.getDouble("result_cost")));
			table.setResultPlant(fmt.DoubleToString(rs.getDouble("result_plant")));

			table.setDiffSum(fmt.DoubleToString(rs.getDouble("plan_sum")-rs.getDouble("result_sum")));
			table.setDiffLabor(fmt.DoubleToString(rs.getDouble("plan_labor")-rs.getDouble("result_labor")));
			table.setDiffMaterial(fmt.DoubleToString(rs.getDouble("plan_material")-rs.getDouble("result_material")));
			table.setDiffCost(fmt.DoubleToString(rs.getDouble("plan_cost")-rs.getDouble("result_cost")));
			table.setDiffPlant(fmt.DoubleToString(rs.getDouble("plan_plant")-rs.getDouble("result_plant")));

			table.setContractDate(anbdt.getSepDate(rs.getString("contract_date"),"/"));
			table.setContractName(rs.getString("contract_name"));
			table.setContractPrice(fmt.DoubleToString(rs.getDouble("contract_price")));
			table.setCompleteDate(anbdt.getSepDate(rs.getString("complete_date"),"/"));
			table.setFname(rs.getString("fname"));
			table.setSname(rs.getString("sname"));
			table.setFtype(rs.getString("ftype"));
			table.setFsize(rs.getString("fsize"));
			table.setPsmStatus(rs.getString("psm_status"));
			table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
			table.setAppDate(anbdt.getSepDate(rs.getString("app_date"),"-"));
			table.setPdCode(rs.getString("pd_code"));
			table.setPdName(rs.getString("pd_name"));
			table.setPsmKind(rs.getString("psm_kind"));
			table.setPsmView(rs.getString("psm_view"));
			table.setLinkCode(rs.getString("link_code"));
		} else {
			table.setPid(anbdt.getID());	
			table.setPsmCode("");	
			table.setPsmType("");	
			table.setCompName("");	
			table.setCompCategory("");	
			table.setPsmKorea("");	
			table.setPsmEnglish("");
			table.setPsmStartDate(anbdt.getDate(10));
			table.setPsmEndDate(anbdt.getDate(150));

			table.setPsmPm("");
			table.setPsmPmDiv("");
			table.setPsmMgr("");
			table.setPsmMgrDiv("");
			table.setPsmBudget("");
			table.setPsmBudgetDiv("");
			table.setPsmUser("");
			table.setPsmUserDiv("");
			table.setPsmDesc("");

			table.setPlanSum("0,000");
			table.setPlanLabor("0,000");
			table.setPlanMaterial("0,000");
			table.setPlanCost("0,000");
			table.setPlanPlant("0,000");

			table.setResultSum("0,000");
			table.setResultLabor("0,000");
			table.setResultMaterial("0,000");
			table.setResultCost("0,000");
			table.setResultPlant("0,000");

			table.setDiffSum("0,000");
			table.setDiffLabor("0,000");
			table.setDiffMaterial("0,000");
			table.setDiffCost("0,000");
			table.setDiffPlant("0,000");

			table.setContractDate("");
			table.setContractName("");
			table.setContractPrice("0,000");
			table.setCompleteDate("");
			table.setFname("");
			table.setSname("");
			table.setFtype("");
			table.setFsize("");
			table.setPsmStatus("S");
			table.setRegDate(anbdt.getDate());
			table.setAppDate("");

			table.setPdCode("");
			table.setPdName("");
			table.setPsmKind("M");
			table.setPsmView("V");
			table.setLinkCode("");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// 과제코드로 해당 PSM_MASTER정보 읽기
	//*******************************************************************/	
	public psmMasterTable readPsmMasterCode(String psm_code) throws Exception
	{
		//변수 초기화
		String where = "";
		fmt.setFormat("0,000");
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmMasterTable table = new com.anbtech.psm.entity.psmMasterTable();
		
		query = "SELECT * FROM psm_master where psm_code ='"+psm_code+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setPsmCode(rs.getString("psm_code"));	
			table.setPsmType(rs.getString("psm_type"));	
			table.setCompName(rs.getString("comp_name"));	
			table.setCompCategory(rs.getString("comp_category"));	
			table.setPsmKorea(rs.getString("psm_korea"));	
			table.setPsmEnglish(rs.getString("psm_english"));
			table.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
			table.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));
			table.setPsmPm(rs.getString("psm_pm"));
			table.setPsmPmDiv(rs.getString("psm_pm_div"));
			table.setPsmMgr(rs.getString("psm_mgr"));
			table.setPsmMgrDiv(rs.getString("psm_mgr_div"));
			table.setPsmBudget(rs.getString("psm_budget"));
			table.setPsmBudgetDiv(rs.getString("psm_budget_div"));
			table.setPsmUser(rs.getString("psm_user"));
			table.setPsmUserDiv(rs.getString("psm_user_div"));
			table.setPsmDesc(rs.getString("psm_desc"));
	
			table.setPlanSum(fmt.DoubleToString(rs.getDouble("plan_sum")));
			table.setPlanLabor(fmt.DoubleToString(rs.getDouble("plan_labor")));
			table.setPlanMaterial(fmt.DoubleToString(rs.getDouble("plan_material")));
			table.setPlanCost(fmt.DoubleToString(rs.getDouble("plan_cost")));
			table.setPlanPlant(fmt.DoubleToString(rs.getDouble("plan_plant")));

			table.setResultSum(fmt.DoubleToString(rs.getDouble("result_sum")));
			table.setResultLabor(fmt.DoubleToString(rs.getDouble("result_labor")));
			table.setResultMaterial(fmt.DoubleToString(rs.getDouble("result_material")));
			table.setResultCost(fmt.DoubleToString(rs.getDouble("result_cost")));
			table.setResultPlant(fmt.DoubleToString(rs.getDouble("result_plant")));

			table.setDiffSum(fmt.DoubleToString(rs.getDouble("plan_sum")-rs.getDouble("result_sum")));
			table.setDiffLabor(fmt.DoubleToString(rs.getDouble("plan_labor")-rs.getDouble("result_labor")));
			table.setDiffMaterial(fmt.DoubleToString(rs.getDouble("plan_material")-rs.getDouble("result_material")));
			table.setDiffCost(fmt.DoubleToString(rs.getDouble("plan_cost")-rs.getDouble("result_cost")));
			table.setDiffPlant(fmt.DoubleToString(rs.getDouble("plan_plant")-rs.getDouble("result_plant")));

			table.setContractDate(anbdt.getSepDate(rs.getString("contract_date"),"/"));
			table.setContractName(rs.getString("contract_name"));
			table.setContractPrice(fmt.DoubleToString(rs.getDouble("contract_price")));
			table.setCompleteDate(anbdt.getSepDate(rs.getString("complete_date"),"/"));
			table.setFname(rs.getString("fname"));
			table.setSname(rs.getString("sname"));
			table.setFtype(rs.getString("ftype"));
			table.setFsize(rs.getString("fsize"));
			table.setPsmStatus(rs.getString("psm_status"));
			table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
			table.setAppDate(anbdt.getSepDate(rs.getString("app_date"),"-"));

			table.setPdCode(rs.getString("pd_code"));
			table.setPdName(rs.getString("pd_name"));
			table.setPsmKind(rs.getString("psm_kind"));
			table.setPsmView(rs.getString("psm_view"));
			table.setLinkCode(rs.getString("link_code"));
		} else {
			table.setPid("");	
			table.setPsmCode("");	
			table.setPsmType("");	
			table.setCompName("");	
			table.setCompCategory("");	
			table.setPsmKorea("");	
			table.setPsmEnglish("");
			table.setPsmStartDate("");
			table.setPsmEndDate("");
			table.setPsmPm("");
			table.setPsmPmDiv("");
			table.setPsmMgr("");
			table.setPsmMgrDiv("");
			table.setPsmBudget("");
			table.setPsmBudgetDiv("");
			table.setPsmUser("");
			table.setPsmUserDiv("");
			table.setPsmDesc("");

			table.setPlanSum("0,000");
			table.setPlanLabor("0,000");
			table.setPlanMaterial("0,000");
			table.setPlanCost("0,000");
			table.setPlanPlant("0,000");

			table.setResultSum("0,000");
			table.setResultLabor("0,000");
			table.setResultMaterial("0,000");
			table.setResultCost("0,000");
			table.setResultPlant("0,000");

			table.setDiffSum("0,000");
			table.setDiffLabor("0,000");
			table.setDiffMaterial("0,000");
			table.setDiffCost("0,000");
			table.setDiffPlant("0,000");

			table.setContractDate("");
			table.setContractName("");
			table.setContractPrice("0,000");
			table.setCompleteDate("");
			table.setFname("");
			table.setSname("");
			table.setFtype("");
			table.setFsize("");
			table.setPsmStatus("");
			table.setRegDate("");
			table.setAppDate("");

			table.setPdCode("");
			table.setPdName("");
			table.setPsmKind("M");
			table.setPsmView("V");
			table.setLinkCode("");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// 과제코드로 해당 PSM_MASTER정보의 예산정보만 읽기
	//*******************************************************************/	
	public double[] readPsmMasterBudget(String psm_code,String budget_type) throws Exception
	{
		//변수 초기화
		double[] budget = new double[5];
		for(int i=0; i<5; i++) budget[i]=0.0;

		String where = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		query = "SELECT * FROM psm_master where psm_code ='"+psm_code+"'";
		rs = stmt.executeQuery(query);
		
		if(budget_type.equals("2")) {				//지출항목으로 결과를 읽는다.
			if(rs.next()) { 
				budget[0] = rs.getDouble("result_sum");
				budget[1] = rs.getDouble("result_labor");
				budget[2] = rs.getDouble("result_material");
				budget[3] = rs.getDouble("result_cost");
				budget[4] = rs.getDouble("result_plant");
			} 
		} else {									//추가 또는 삭감으로 예산을 읽는다.
			if(rs.next()) { 
				budget[0] = rs.getDouble("plan_sum");
				budget[1] = rs.getDouble("plan_labor");
				budget[2] = rs.getDouble("plan_material");
				budget[3] = rs.getDouble("plan_cost");
				budget[4] = rs.getDouble("plan_plant");
			}
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return budget;
	}
	//*******************************************************************
	// PSM MASTER 전체LIST 가져오기
	//*******************************************************************/	
	public ArrayList getPsmMasterList(String sItem,String sWord,String psm_status,String login_id,
			String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		fmt.setFormat("0,000");
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();

		//권한 찾기
		String statusMgr = getStatusMgr();			//과제상태변경 관리권한
		String budgetMgr = getBudgetMgr();			//과제예산변경 관리권한

		//진행상태별 조건문 만들기
		if(psm_status.equals("1")) {				//미진행 또는 PM의 경우 상신때
			where = "(";
			where += "(psm_user like '"+login_id+"%' and psm_status in ('1','11'))";
			if(statusMgr.indexOf(login_id) != -1) {	//과제상태관리권한있을때
				where += " or (psm_status ='11')";
			}
			where += ")";
		} else {									//나머지
			where = "(psm_pm like '"+login_id+"%' or psm_user like '"+login_id+"%') and ";
			if(statusMgr.indexOf(login_id) != -1) {			//과제상태관리권한있을때
				where = "";
			} else if(budgetMgr.indexOf(login_id) != -1) {	//과제예산관리권한있을때
				where = "";
			}
			where += "psm_status in ('2','3','4','5','6')";
		} 
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM psm_master WHERE "+sItem+" like '%"+sWord+"%' and ";
		query += where;
		total_cnt = getTotalCount(query);

		//query문장 만들기
		query = "SELECT * FROM psm_master WHERE "+sItem+" like '%"+sWord+"%' and ";
		query += where;
		query += " order by pid desc";
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
				table = new psmMasterTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setPsmCode(rs.getString("psm_code"));	

				psm_type = rs.getString("psm_type");
				table.setPsmType(psm_type);	
				table.setCompName(rs.getString("comp_name"));	
				table.setCompCategory(rs.getString("comp_category"));
				
				psm_subject = rs.getString("psm_korea");
				psm_subject = "<a href=\"javascript:psmView('"+pid+"','"+psm_type+"');\">"+psm_subject+"</a>";
				table.setPsmKorea(psm_subject);	

				table.setPsmEnglish(rs.getString("psm_english"));

				table.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
				table.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));

				table.setPsmPm(rs.getString("psm_pm"));
				table.setPsmPmDiv(rs.getString("psm_pm_div"));
				table.setPsmMgr(rs.getString("psm_mgr"));
				table.setPsmMgrDiv(rs.getString("psm_mgr_div"));
				table.setPsmBudget(rs.getString("psm_budget"));
				table.setPsmBudgetDiv(rs.getString("psm_budget_div"));
				table.setPsmUser(rs.getString("psm_user"));
				table.setPsmUserDiv(rs.getString("psm_user_div"));
				table.setPsmDesc(rs.getString("psm_desc"));

				table.setPlanSum(fmt.DoubleToString(rs.getDouble("plan_sum")));
				table.setPlanLabor(fmt.DoubleToString(rs.getDouble("plan_labor")));
				table.setPlanMaterial(fmt.DoubleToString(rs.getDouble("plan_material")));
				table.setPlanCost(fmt.DoubleToString(rs.getDouble("plan_cost")));
				table.setPlanPlant(fmt.DoubleToString(rs.getDouble("plan_plant")));

				table.setResultSum(fmt.DoubleToString(rs.getDouble("result_sum")));
				table.setResultLabor(fmt.DoubleToString(rs.getDouble("result_labor")));
				table.setResultMaterial(fmt.DoubleToString(rs.getDouble("result_material")));
				table.setResultCost(fmt.DoubleToString(rs.getDouble("result_cost")));
				table.setResultPlant(fmt.DoubleToString(rs.getDouble("result_plant")));

				table.setContractDate(anbdt.getSepDate(rs.getString("contract_date"),"-"));
				table.setContractName(rs.getString("contract_name"));
				table.setContractPrice(fmt.DoubleToString(rs.getDouble("contract_price")));
				table.setCompleteDate(anbdt.getSepDate(rs.getString("complete_date"),"-"));
				table.setFname(rs.getString("fname"));
				table.setSname(rs.getString("sname"));
				table.setFtype(rs.getString("ftype"));
				table.setFsize(rs.getString("fsize"));
				table.setPsmStatus(rs.getString("psm_status"));
				table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
				table.setAppDate(anbdt.getSepDate(rs.getString("app_date"),"-"));

				table.setPdCode(rs.getString("pd_code"));
				table.setPdName(rs.getString("pd_name"));
				table.setPsmKind(rs.getString("psm_kind"));
				table.setPsmView(rs.getString("psm_view"));
				table.setLinkCode(rs.getString("link_code"));

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public psmMasterTable getDisplayPage(String sItem,String sWord,String psm_status,String login_id,
			String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//변수 초기화
		String query="",where="",mode="psm_list";	
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();

		//권한 찾기
		String statusMgr = getStatusMgr();			//과제상태변경 관리권한
		String budgetMgr = getBudgetMgr();			//과제예산변경 관리권한

		//진행상태별 조건문 만들기
		if(psm_status.equals("1")) {				//미진행 또는 PM의 경우 상신때
			where = "(";
			where += "(psm_user like '"+login_id+"%' and psm_status in ('1','11'))";
			if(statusMgr.indexOf(login_id) != -1) {	//과제상태관리권한있을때
				where += " or (psm_status ='11')";
			}
			where += ")";
		} else {									//나머지
			where = "(psm_pm like '"+login_id+"%' or psm_user like '"+login_id+"%') and ";
			if(statusMgr.indexOf(login_id) != -1) {			//과제상태관리권한있을때
				where = "";
			} else if(budgetMgr.indexOf(login_id) != -1) {	//과제예산관리권한있을때
				where = "";
			}
			where += "psm_status in ('2','3','4','5','6')";
		} 
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM psm_master WHERE "+sItem+" like '%"+sWord+"%' and ";
		query += where;
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM psm_master WHERE "+sItem+" like '%"+sWord+"%' and ";
		query += where;
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		// 전체 페이지의 값을 구한다.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// 페이지 이동관련 문자열을 담을 변수. 즉, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//페이지 바로가기 만들기
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=PsmBaseInfoServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&psm_status="+psm_status+"&login_id="+login_id+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=PsmBaseInfoServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&psm_status="+psm_status+"&login_id="+login_id+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=PsmBaseInfoServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&psm_status="+psm_status+"&login_id="+login_id+">[Next]</a>";
		}

		//arraylist에 담기
		table = new psmMasterTable();
		table.setPageCut(pagecut);							//선택할 수 있는 페이지 표현
		table.setTotalPage(total_page);						//총페이지수
		table.setCurrentPage(Integer.parseInt(page));		//현재페이지
		table.setTotalArticle(total_cnt);					//총 조항갯수

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// 해당과제코드의 서브과제코드 전체LIST 가져오기 :자신포함 [link_code과제List]
	// 권한별 조회 : 조회용으로 사용
	//*******************************************************************/	
	public ArrayList getPsmLinkCodeList(String login_id,String pid) throws Exception
	{
		//변수 초기화
		String query="",where="",link_code="",psm_code="",mgr="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//link code구하기
		where = "where pid = '"+pid+"'";
		psm_code = getColumData("PSM_MASTER","psm_code",where);
		link_code = psm_code+","+getColumData("PSM_MASTER","link_code",where);
		if(link_code.length() < 2) return table_list;

		//데이터 구하기
		StringTokenizer list = new StringTokenizer(link_code,",");
		while(list.hasMoreTokens()) {
			psm_code = list.nextToken();
			
			//query문장 만들기
			query = "SELECT * FROM psm_master WHERE psm_code='"+psm_code+"' ";
			query += "and psm_status in('2','3','4','5','6') order by pid asc";
			rs = stmt.executeQuery(query);

			if(rs.next()) { 
					table = new psmMasterTable();
									
					table.setPid(rs.getString("pid"));
					table.setPsmCode(rs.getString("psm_code"));	
					table.setPsmType(rs.getString("psm_type"));	
					table.setCompName(rs.getString("comp_name"));	
					table.setCompCategory(rs.getString("comp_category"));
					table.setPsmKorea(rs.getString("psm_korea"));	
					table.setPsmEnglish(rs.getString("psm_english"));

					//권한이 있는지 검사하기
					where ="where user_id='"+login_id+"'";
					mgr = getColumData("psm_view_mgr","pjt_type",where);
					if(mgr.equals("A")) table_list.add(table);
					else {
						where ="where user_id='"+login_id+"' and psm_code='"+psm_code+"'";
						mgr = getColumData("psm_view_mgr","pjt_type",where);
						if(mgr.equals("G")) table_list.add(table);
					}
			}
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제코드의 서브과제코드 전체LIST 가져오기 :자신포함 [link_code과제List]
	// 예산,상태 관리자용으로 각서브과제별 권한을 검사하지 않음
	//*******************************************************************/	
	public ArrayList getPsmLinkCodeList(String pid) throws Exception
	{
		//변수 초기화
		String query="",where="",link_code="",psm_code="",mgr="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//link code구하기
		where = "where pid = '"+pid+"'";
		psm_code = getColumData("PSM_MASTER","psm_code",where);
		link_code = psm_code+","+getColumData("PSM_MASTER","link_code",where);
		if(link_code.length() < 2) return table_list;

		//데이터 구하기
		StringTokenizer list = new StringTokenizer(link_code,",");
		while(list.hasMoreTokens()) {
			psm_code = list.nextToken();
			
			//query문장 만들기
			query = "SELECT * FROM psm_master WHERE psm_code='"+psm_code+"' ";
			query += "and psm_status in('2','3','4','5','6') order by pid asc";
			rs = stmt.executeQuery(query);

			if(rs.next()) { 
					table = new psmMasterTable();
									
					table.setPid(rs.getString("pid"));
					table.setPsmCode(rs.getString("psm_code"));	
					table.setPsmType(rs.getString("psm_type"));	
					table.setCompName(rs.getString("comp_name"));	
					table.setCompCategory(rs.getString("comp_category"));
					table.setPsmKorea(rs.getString("psm_korea"));	
					table.setPsmEnglish(rs.getString("psm_english"));

					table_list.add(table);
			}
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		PSM STATUS 에 관한 메소드 정의
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 PSM_STATUS정보 읽기
	//*******************************************************************/	
	public psmStatusTable readPsmStatus(String pid,String psm_code) throws Exception
	{
		//변수 초기화
		String where = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmStatusTable table = new com.anbtech.psm.entity.psmStatusTable();
		
		query = "SELECT * FROM psm_status where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setPsmCode(rs.getString("psm_code"));	
			table.setPsmType(rs.getString("psm_type"));	
			table.setCompName(rs.getString("comp_name"));	
			table.setCompCategory(rs.getString("comp_category"));	
			table.setPsmKorea(rs.getString("psm_korea"));	
			table.setPsmEnglish(rs.getString("psm_english"));
			table.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
			table.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));

			table.setPsmPm(rs.getString("psm_pm"));
			table.setPsmPmDiv(rs.getString("psm_pm_div"));
			table.setPsmMgr(rs.getString("psm_mgr"));
			table.setPsmMgrDiv(rs.getString("psm_mgr_div"));
			table.setPsmBudget(rs.getString("psm_budget"));
			table.setPsmBudgetDiv(rs.getString("psm_budget_div"));
			table.setPsmUser(rs.getString("psm_user"));
			table.setPsmUserDiv(rs.getString("psm_user_div"));
			table.setChangeDesc(rs.getString("Change_desc"));
	
			table.setPsmStatus(rs.getString("psm_status"));
			table.setChangeDate(anbdt.getSepDate(rs.getString("change_date"),"-"));
		} else {
			//기본정보 담기
			com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
			masterT = readPsmMasterCode(psm_code);

			table.setPid(anbdt.getID());	
			table.setPsmCode(psm_code);	
			table.setPsmType(masterT.getPsmType());	
			table.setCompName(masterT.getCompName());	
			table.setCompCategory(masterT.getCompCategory());	
			table.setPsmKorea(masterT.getPsmKorea());	
			table.setPsmEnglish(masterT.getPsmEnglish());
			table.setPsmStartDate(masterT.getPsmStartDate());
			table.setPsmEndDate(masterT.getPsmEndDate());

			table.setPsmPm(masterT.getPsmPm());
			table.setPsmPmDiv(masterT.getPsmPmDiv());
			table.setPsmMgr(masterT.getPsmMgr());
			table.setPsmMgrDiv(masterT.getPsmMgrDiv());
			table.setPsmBudget(masterT.getPsmBudget());
			table.setPsmBudgetDiv(masterT.getPsmBudgetDiv());
			table.setPsmUser("");
			table.setPsmUserDiv("");

			table.setChangeDesc("");
			table.setPsmStatus("");
			table.setChangeDate(anbdt.getDate());
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// PSM STATUS 전체LIST 가져오기
	//*******************************************************************/	
	public ArrayList getPsmStatusList(String psm_code,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		psmStatusTable table = null;
		ArrayList table_list = new ArrayList();

		//총갯수 구하기
		query = "SELECT count(*) FROM psm_status WHERE psm_code = '"+psm_code+"'";
		total_cnt = getTotalCount(query);

		//query문장 만들기
		query = "SELECT * FROM psm_status WHERE psm_code = '"+psm_code+"'";
		query += " order by pid desc";
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
				table = new psmStatusTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setPsmCode(rs.getString("psm_code"));	

				psm_type = rs.getString("psm_type");
				table.setPsmType(psm_type);	
				table.setCompName(rs.getString("comp_name"));	
				table.setCompCategory(rs.getString("comp_category"));
				
				psm_subject = rs.getString("psm_korea");
				if(!rs.getString("psm_status").equals("1")) 
					psm_subject = "<a href=\"javascript:psmView('"+pid+"');\">"+psm_subject+"</a>";
				else psm_subject = psm_subject;					//맨처음 미진행은 수정할 수 없도록
				table.setPsmKorea(psm_subject);	

				table.setPsmEnglish(rs.getString("psm_english"));

				table.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
				table.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));

				table.setPsmPm(rs.getString("psm_pm"));
				table.setPsmPmDiv(rs.getString("psm_pm_div"));
				table.setPsmMgr(rs.getString("psm_mgr"));
				table.setPsmMgrDiv(rs.getString("psm_mgr_div"));
				table.setPsmBudget(rs.getString("psm_budget"));
				table.setPsmBudgetDiv(rs.getString("psm_budget_div"));
				table.setPsmUser(rs.getString("psm_user"));
				table.setPsmUserDiv(rs.getString("psm_user_div"));
				table.setChangeDesc(rs.getString("Change_desc"));

				table.setPsmStatus(rs.getString("psm_status"));
				table.setChangeDate(anbdt.getSepDate(rs.getString("change_date"),"-"));

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_STATUS 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public psmStatusTable getStsDisplayPage(String psm_code,String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//변수 초기화
		String query="",where="",mode="";	
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		psmStatusTable table = null;
		ArrayList table_list = new ArrayList();

		//총갯수 구하기
		query = "SELECT count(*) FROM psm_status WHERE psm_code = '"+psm_code+"'";
		total_cnt = getTotalCount(query);

		//query문장 만들기
		query = "SELECT * FROM psm_status WHERE psm_code = '"+psm_code+"'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		// 전체 페이지의 값을 구한다.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// 페이지 이동관련 문자열을 담을 변수. 즉, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//페이지 바로가기 만들기
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=PsmStatusServlet?&mode=sts_list&page="+curpage+"&psm_code="+psm_code+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=PsmStatusServlet?&mode=sts_list&page="+curpage+"&psm_code="+psm_code+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=PsmStatusServlet?&mode=sts_list&page="+curpage+"&psm_code="+psm_code+">[Next]</a>";
		}

		//arraylist에 담기
		table = new psmStatusTable();
		table.setPageCut(pagecut);							//선택할 수 있는 페이지 표현
		table.setTotalPage(total_page);						//총페이지수
		table.setCurrentPage(Integer.parseInt(page));		//현재페이지
		table.setTotalArticle(total_cnt);					//총 조항갯수

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// PSM STATUS 전체LIST 가져오기
	//*******************************************************************/	
	public ArrayList getPsmStatusList(String psm_code) throws Exception
	{
		//변수 초기화
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmStatusTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM psm_status WHERE psm_code = '"+psm_code+"'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new psmStatusTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setPsmCode(rs.getString("psm_code"));	

				psm_type = rs.getString("psm_type");
				table.setPsmType(psm_type);	
				table.setCompName(rs.getString("comp_name"));	
				table.setCompCategory(rs.getString("comp_category"));
				
				psm_subject = rs.getString("psm_korea");
				if(!rs.getString("psm_status").equals("1")) 
					psm_subject = "<a href=\"javascript:psmView('"+pid+"');\">"+psm_subject+"</a>";
				else psm_subject = psm_subject;					//맨처음 미진행은 수정할 수 없도록
				table.setPsmKorea(psm_subject);	

				table.setPsmEnglish(rs.getString("psm_english"));

				table.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
				table.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));

				table.setPsmPm(rs.getString("psm_pm"));
				table.setPsmPmDiv(rs.getString("psm_pm_div"));
				table.setPsmMgr(rs.getString("psm_mgr"));
				table.setPsmMgrDiv(rs.getString("psm_mgr_div"));
				table.setPsmBudget(rs.getString("psm_budget"));
				table.setPsmBudgetDiv(rs.getString("psm_budget_div"));
				table.setPsmUser(rs.getString("psm_user"));
				table.setPsmUserDiv(rs.getString("psm_user_div"));
				table.setChangeDesc(rs.getString("Change_desc"));

				table.setPsmStatus(rs.getString("psm_status"));
				table.setChangeDate(anbdt.getSepDate(rs.getString("change_date"),"-"));

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		PSM BUDGET 에 관한 메소드 정의
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 PSM_BUDGET정보 읽기
	//*******************************************************************/	
	public psmBudgetTable readPsmBudget(String pid,String psm_code) throws Exception
	{
		//변수 초기화
		String where = "";
		fmt.setFormat("0,000");
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmBudgetTable table = new com.anbtech.psm.entity.psmBudgetTable();
		
		query = "SELECT * FROM psm_budget where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setPsmCode(rs.getString("psm_code"));	
			table.setPsmType(rs.getString("psm_type"));	
			table.setCompName(rs.getString("comp_name"));	
			table.setCompCategory(rs.getString("comp_category"));	
			table.setPsmKorea(rs.getString("psm_korea"));	
			table.setPsmEnglish(rs.getString("psm_english"));
			table.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
			table.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));
			table.setPsmPm(rs.getString("psm_pm"));
			table.setPsmPmDiv(rs.getString("psm_pm_div"));
			table.setPsmMgr(rs.getString("psm_mgr"));
			table.setPsmMgrDiv(rs.getString("psm_mgr_div"));
			table.setPsmBudget(rs.getString("psm_budget"));
			table.setPsmBudgetDiv(rs.getString("psm_budget_div"));
			table.setPsmUser(rs.getString("psm_user"));
			table.setPsmUserDiv(rs.getString("psm_user_div"));
	
			table.setPlanSum(fmt.DoubleToString(rs.getDouble("plan_sum")));
			table.setPlanLabor(fmt.DoubleToString(rs.getDouble("plan_labor")));
			table.setPlanMaterial(fmt.DoubleToString(rs.getDouble("plan_material")));
			table.setPlanCost(fmt.DoubleToString(rs.getDouble("plan_cost")));
			table.setPlanPlant(fmt.DoubleToString(rs.getDouble("plan_plant")));

			table.setChangeDesc(rs.getString("change_desc"));
			table.setChangeDate(anbdt.getSepDate(rs.getString("change_date"),"-"));
			table.setBudgetType(rs.getString("budget_type"));
		} else {
			//기본정보 담기
			com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
			masterT = readPsmMasterCode(psm_code);

			table.setPid(anbdt.getID());	
			table.setPsmCode(psm_code);	
			table.setPsmType(masterT.getPsmType());	
			table.setCompName(masterT.getCompName());	
			table.setCompCategory(masterT.getCompCategory());	
			table.setPsmKorea(masterT.getPsmKorea());	
			table.setPsmEnglish(masterT.getPsmEnglish());
			table.setPsmStartDate(masterT.getPsmStartDate());
			table.setPsmEndDate(masterT.getPsmEndDate());

			table.setPsmPm(masterT.getPsmPm());
			table.setPsmPmDiv(masterT.getPsmPmDiv());
			table.setPsmMgr(masterT.getPsmMgr());
			table.setPsmMgrDiv(masterT.getPsmMgrDiv());
			table.setPsmBudget(masterT.getPsmBudget());
			table.setPsmBudgetDiv(masterT.getPsmBudgetDiv());
			table.setPsmUser("");
			table.setPsmUserDiv("");
			
			table.setPlanSum("0,000");
			table.setPlanLabor("0,000");
			table.setPlanMaterial("0,000");
			table.setPlanCost("0,000");
			table.setPlanPlant("0,000");
			
			table.setChangeDesc("");
			table.setChangeDate(anbdt.getDate());
			table.setBudgetType("1");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// PID로 해당 PSM_STATUS정보의 예산정보만 읽기
	//*******************************************************************/	
	public double[] readPsmBudget(String pid) throws Exception
	{
		//변수 초기화
		double[] budget = new double[5];
		String where = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		query = "SELECT * FROM psm_budget where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			budget[0] = rs.getDouble("plan_sum");
			budget[1] = rs.getDouble("plan_labor");
			budget[2] = rs.getDouble("plan_material");
			budget[3] = rs.getDouble("plan_cost");
			budget[4] = rs.getDouble("plan_plant");
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return budget;
	}
	//*******************************************************************
	// PSM BUDGET 전체LIST 가져오기
	//*******************************************************************/	
	public ArrayList getPsmBudgetList(String psm_code,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		fmt.setFormat("0,000");
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		psmBudgetTable table = null;
		ArrayList table_list = new ArrayList();

		//총갯수 구하기
		query = "SELECT COUNT(*) FROM psm_budget WHERE psm_code='"+psm_code+"'";
		total_cnt = getTotalCount(query);

		//query문장 만들기
		query = "SELECT * FROM psm_budget WHERE psm_code='"+psm_code+"'";
		query += " order by pid desc";
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
				table = new psmBudgetTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setPsmCode(rs.getString("psm_code"));	

				psm_type = rs.getString("psm_type");
				table.setPsmType(psm_type);	
				table.setCompName(rs.getString("comp_name"));	
				table.setCompCategory(rs.getString("comp_category"));
				
				psm_subject = rs.getString("psm_korea");
				psm_subject = "<a href=\"javascript:psmView('"+pid+"');\">"+psm_subject+"</a>";
				table.setPsmKorea(psm_subject);	

				table.setPsmEnglish(rs.getString("psm_english"));

				table.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
				table.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));
				table.setPsmPm(rs.getString("psm_pm"));
				table.setPsmPmDiv(rs.getString("psm_pm_div"));
				table.setPsmMgr(rs.getString("psm_mgr"));
				table.setPsmMgrDiv(rs.getString("psm_mgr_div"));
				table.setPsmBudget(rs.getString("psm_budget"));
				table.setPsmBudgetDiv(rs.getString("psm_budget_div"));
				table.setPsmUser(rs.getString("psm_user"));
				table.setPsmUserDiv(rs.getString("psm_user_div"));

				table.setPlanSum(fmt.DoubleToString(rs.getDouble("plan_sum")));
				table.setPlanLabor(fmt.DoubleToString(rs.getDouble("plan_labor")));
				table.setPlanMaterial(fmt.DoubleToString(rs.getDouble("plan_material")));
				table.setPlanCost(fmt.DoubleToString(rs.getDouble("plan_cost")));
				table.setPlanPlant(fmt.DoubleToString(rs.getDouble("plan_plant")));

				table.setChangeDesc(rs.getString("change_desc"));
				table.setChangeDate(anbdt.getSepDate(rs.getString("change_date"),"-"));
				table.setBudgetType(rs.getString("budget_type"));

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_Budget 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public psmBudgetTable getBudDisplayPage(String psm_code,String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//변수 초기화
		String query="",where="",mode="";	
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		psmBudgetTable table = null;
		ArrayList table_list = new ArrayList();

		//총갯수 구하기
		query = "SELECT COUNT(*) FROM psm_budget WHERE psm_code='"+psm_code+"'";
		total_cnt = getTotalCount(query);

		//query문장 만들기
		query = "SELECT * FROM psm_budget WHERE psm_code='"+psm_code+"'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		// 전체 페이지의 값을 구한다.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// 페이지 이동관련 문자열을 담을 변수. 즉, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//페이지 바로가기 만들기
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=PsmStatusServlet?&mode=bud_list&page="+curpage+"&psm_code="+psm_code+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=PsmStatusServlet?&mode=bud_list&page="+curpage+"&psm_code="+psm_code+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=PsmStatusServlet?&mode=bud_list&page="+curpage+"&psm_code="+psm_code+">[Next]</a>";
		}

		//arraylist에 담기
		table = new psmBudgetTable();
		table.setPageCut(pagecut);							//선택할 수 있는 페이지 표현
		table.setTotalPage(total_page);						//총페이지수
		table.setCurrentPage(Integer.parseInt(page));		//현재페이지
		table.setTotalArticle(total_cnt);					//총 조항갯수

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// PSM BUDGET 전체LIST 가져오기
	//*******************************************************************/	
	public ArrayList getPsmBudgetList(String psm_code) throws Exception
	{
		//변수 초기화
		fmt.setFormat("0,000");
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmBudgetTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM psm_budget WHERE psm_code='"+psm_code+"'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new psmBudgetTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setPsmCode(rs.getString("psm_code"));	

				psm_type = rs.getString("psm_type");
				table.setPsmType(psm_type);	
				table.setCompName(rs.getString("comp_name"));	
				table.setCompCategory(rs.getString("comp_category"));
				
				psm_subject = rs.getString("psm_korea");
				psm_subject = "<a href=\"javascript:psmView('"+pid+"');\">"+psm_subject+"</a>";
				table.setPsmKorea(psm_subject);	

				table.setPsmEnglish(rs.getString("psm_english"));

				table.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
				table.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));
				table.setPsmPm(rs.getString("psm_pm"));
				table.setPsmPmDiv(rs.getString("psm_pm_div"));
				table.setPsmMgr(rs.getString("psm_mgr"));
				table.setPsmMgrDiv(rs.getString("psm_mgr_div"));
				table.setPsmBudget(rs.getString("psm_budget"));
				table.setPsmBudgetDiv(rs.getString("psm_budget_div"));
				table.setPsmUser(rs.getString("psm_user"));
				table.setPsmUserDiv(rs.getString("psm_user_div"));

				table.setPlanSum(fmt.DoubleToString(rs.getDouble("plan_sum")));
				table.setPlanLabor(fmt.DoubleToString(rs.getDouble("plan_labor")));
				table.setPlanMaterial(fmt.DoubleToString(rs.getDouble("plan_material")));
				table.setPlanCost(fmt.DoubleToString(rs.getDouble("plan_cost")));
				table.setPlanPlant(fmt.DoubleToString(rs.getDouble("plan_plant")));

				table.setChangeDesc(rs.getString("change_desc"));
				table.setChangeDate(anbdt.getSepDate(rs.getString("change_date"),"-"));
				table.setBudgetType(rs.getString("budget_type"));

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		과제담당자 및 예산담당자 권한 구하기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	과제담당자 권한자 쿼리 
	//*******************************************************************/
	public String getStatusMgr() throws Exception
	{
		String data="";
		String where ="where code_s = 'PS02'";
		data = getColumData("prg_privilege","owner",where);
		return data;
	}
	//*******************************************************************
	//	예산담당자 권한자 쿼리 
	//*******************************************************************/
	public String getBudgetMgr() throws Exception
	{
		String data="";
		String where ="where code_s = 'PS03'";
		data = getColumData("prg_privilege","owner",where);
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		공통 메소드 정의
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	수량 파악하기 
	//*******************************************************************/
	public int getTotalCount(String query) throws Exception
	{
		//변수 초기화
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
	
	//*******************************************************************
	// SQL update 실행하기
	//*******************************************************************/	
	public void executeUpdate(String update) throws Exception
	{
		Statement stmt = con.createStatement();
		stmt.executeUpdate(update);
		stmt.close();
	}

	//*******************************************************************
	//	주어진 테이블에서 주어진 조건의 컬럼의 데이터 읽기
	//*******************************************************************/
	public String getColumData(String tablename,String getcolumn,String where) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString(getcolumn);
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	주어진 테이블에서 주어진 조건의 컬럼의 2번째 데이터 읽기
	//*******************************************************************/
	public String getSecondData(String tablename,String getcolumn,String where) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		int n=0;
		while(rs.next()) {
			if(n == 1) {
				data = rs.getString(getcolumn);
				break;
			}
			n++;
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	과제코드 구하기
	//*******************************************************************/
	public String getPsmCode(String query) throws Exception
	{
		//변수 초기화
		fmt.setFormat("00");
		String psm_code = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		if(rs.next()) psm_code = rs.getString("psm_code");
		
		//찾은 순서에서 +1
		if(psm_code.length() == 0) {
			psm_code = fmt.toDigits(1);
		} else {
			psm_code = fmt.toDigits(Integer.parseInt(psm_code)+1);
		}
		
		stmt.close();
		rs.close();
		return psm_code;			
	}
	//*******************************************************************
	//	주어진사번의 사업부코드는
	//*******************************************************************/
	public String getDivCode(String sabun) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select b.ac_code,b.ac_name from user_table a,class_table b where (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString("ac_code");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
}
