package com.anbtech.psm.db;
import com.anbtech.psm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class psmModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat();	//����
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public psmModifyDAO(Connection con) 
	{
		this.con = con;
	}
	//--------------------------------------------------------------------
	//
	//		���� category �� ȯ�� ���� �б�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ���� ī�װ� ���� �б�
	//*******************************************************************/	
	public ArrayList readPsmCategoryList(String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// ���� ī�װ� ���� �б�
	// env_status : �������[1], ���İ���[2]
	//*******************************************************************/	
	public ArrayList readPsmCategoryList(String env_status,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmCategoryTable table = new com.anbtech.psm.entity.psmCategoryTable();
		
		query = "SELECT * FROM psm_category WHERE "+sItem+" like '%"+sWord+"%' ";
		if(env_status.equals("1")) query += "and comp_english='����'";
		else query += "and comp_english !='����'";
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// ���� ���� ���� �б�
	// env_status : ��������(1:�����ϰ���, 2:���ĵ�ϰ���)
	//*******************************************************************/	
	public ArrayList readPsmEnvList(String env_status) throws Exception
	{
		//���� �ʱ�ȭ
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// ���� ���� ���� �б�
	// env_status : ���������� ������� ���� �б�
	//*******************************************************************/	
	public ArrayList readPsmEnvAllList() throws Exception
	{
		//���� �ʱ�ȭ
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// ���� ������� ���� ���� �б�
	//*******************************************************************/	
	public ArrayList readPsmColorList() throws Exception
	{
		//���� �ʱ�ȭ
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		PSM MASTER �� ���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� PSM_MASTER���� �б�
	//*******************************************************************/	
	public psmMasterTable readPsmMaster(String pid) throws Exception
	{
		//���� �ʱ�ȭ
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
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// �����ڵ�� �ش� PSM_MASTER���� �б�
	//*******************************************************************/	
	public psmMasterTable readPsmMasterCode(String psm_code) throws Exception
	{
		//���� �ʱ�ȭ
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
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// �����ڵ�� �ش� PSM_MASTER������ ���������� �б�
	//*******************************************************************/	
	public double[] readPsmMasterBudget(String psm_code,String budget_type) throws Exception
	{
		//���� �ʱ�ȭ
		double[] budget = new double[5];
		for(int i=0; i<5; i++) budget[i]=0.0;

		String where = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		query = "SELECT * FROM psm_master where psm_code ='"+psm_code+"'";
		rs = stmt.executeQuery(query);
		
		if(budget_type.equals("2")) {				//�����׸����� ����� �д´�.
			if(rs.next()) { 
				budget[0] = rs.getDouble("result_sum");
				budget[1] = rs.getDouble("result_labor");
				budget[2] = rs.getDouble("result_material");
				budget[3] = rs.getDouble("result_cost");
				budget[4] = rs.getDouble("result_plant");
			} 
		} else {									//�߰� �Ǵ� �谨���� ������ �д´�.
			if(rs.next()) { 
				budget[0] = rs.getDouble("plan_sum");
				budget[1] = rs.getDouble("plan_labor");
				budget[2] = rs.getDouble("plan_material");
				budget[3] = rs.getDouble("plan_cost");
				budget[4] = rs.getDouble("plan_plant");
			}
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return budget;
	}
	//*******************************************************************
	// PSM MASTER ��üLIST ��������
	//*******************************************************************/	
	public ArrayList getPsmMasterList(String sItem,String sWord,String psm_status,String login_id,
			String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		fmt.setFormat("0,000");
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();

		//���� ã��
		String statusMgr = getStatusMgr();			//�������º��� ��������
		String budgetMgr = getBudgetMgr();			//�������꺯�� ��������

		//������º� ���ǹ� �����
		if(psm_status.equals("1")) {				//������ �Ǵ� PM�� ��� ��Ŷ�
			where = "(";
			where += "(psm_user like '"+login_id+"%' and psm_status in ('1','11'))";
			if(statusMgr.indexOf(login_id) != -1) {	//�������°�������������
				where += " or (psm_status ='11')";
			}
			where += ")";
		} else {									//������
			where = "(psm_pm like '"+login_id+"%' or psm_user like '"+login_id+"%') and ";
			if(statusMgr.indexOf(login_id) != -1) {			//�������°�������������
				where = "";
			} else if(budgetMgr.indexOf(login_id) != -1) {	//���������������������
				where = "";
			}
			where += "psm_status in ('2','3','4','5','6')";
		} 
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM psm_master WHERE "+sItem+" like '%"+sWord+"%' and ";
		query += where;
		total_cnt = getTotalCount(query);

		//query���� �����
		query = "SELECT * FROM psm_master WHERE "+sItem+" like '%"+sWord+"%' and ";
		query += where;
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public psmMasterTable getDisplayPage(String sItem,String sWord,String psm_status,String login_id,
			String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="",mode="psm_list";	
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();

		//���� ã��
		String statusMgr = getStatusMgr();			//�������º��� ��������
		String budgetMgr = getBudgetMgr();			//�������꺯�� ��������

		//������º� ���ǹ� �����
		if(psm_status.equals("1")) {				//������ �Ǵ� PM�� ��� ��Ŷ�
			where = "(";
			where += "(psm_user like '"+login_id+"%' and psm_status in ('1','11'))";
			if(statusMgr.indexOf(login_id) != -1) {	//�������°�������������
				where += " or (psm_status ='11')";
			}
			where += ")";
		} else {									//������
			where = "(psm_pm like '"+login_id+"%' or psm_user like '"+login_id+"%') and ";
			if(statusMgr.indexOf(login_id) != -1) {			//�������°�������������
				where = "";
			} else if(budgetMgr.indexOf(login_id) != -1) {	//���������������������
				where = "";
			}
			where += "psm_status in ('2','3','4','5','6')";
		} 
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM psm_master WHERE "+sItem+" like '%"+sWord+"%' and ";
		query += where;
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM psm_master WHERE "+sItem+" like '%"+sWord+"%' and ";
		query += where;
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		// ��ü �������� ���� ���Ѵ�.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//������ �ٷΰ��� �����
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=PsmBaseInfoServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&psm_status="+psm_status+"&login_id="+login_id+">[Prev]</a>";
		}

		//�߰�
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

		//arraylist�� ���
		table = new psmMasterTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// �ش�����ڵ��� ��������ڵ� ��üLIST �������� :�ڽ����� [link_code����List]
	// ���Ѻ� ��ȸ : ��ȸ������ ���
	//*******************************************************************/	
	public ArrayList getPsmLinkCodeList(String login_id,String pid) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="",link_code="",psm_code="",mgr="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//link code���ϱ�
		where = "where pid = '"+pid+"'";
		psm_code = getColumData("PSM_MASTER","psm_code",where);
		link_code = psm_code+","+getColumData("PSM_MASTER","link_code",where);
		if(link_code.length() < 2) return table_list;

		//������ ���ϱ�
		StringTokenizer list = new StringTokenizer(link_code,",");
		while(list.hasMoreTokens()) {
			psm_code = list.nextToken();
			
			//query���� �����
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

					//������ �ִ��� �˻��ϱ�
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش�����ڵ��� ��������ڵ� ��üLIST �������� :�ڽ����� [link_code����List]
	// ����,���� �����ڿ����� ����������� ������ �˻����� ����
	//*******************************************************************/	
	public ArrayList getPsmLinkCodeList(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="",link_code="",psm_code="",mgr="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//link code���ϱ�
		where = "where pid = '"+pid+"'";
		psm_code = getColumData("PSM_MASTER","psm_code",where);
		link_code = psm_code+","+getColumData("PSM_MASTER","link_code",where);
		if(link_code.length() < 2) return table_list;

		//������ ���ϱ�
		StringTokenizer list = new StringTokenizer(link_code,",");
		while(list.hasMoreTokens()) {
			psm_code = list.nextToken();
			
			//query���� �����
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		PSM STATUS �� ���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� PSM_STATUS���� �б�
	//*******************************************************************/	
	public psmStatusTable readPsmStatus(String pid,String psm_code) throws Exception
	{
		//���� �ʱ�ȭ
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
			//�⺻���� ���
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
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// PSM STATUS ��üLIST ��������
	//*******************************************************************/	
	public ArrayList getPsmStatusList(String psm_code,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		psmStatusTable table = null;
		ArrayList table_list = new ArrayList();

		//�Ѱ��� ���ϱ�
		query = "SELECT count(*) FROM psm_status WHERE psm_code = '"+psm_code+"'";
		total_cnt = getTotalCount(query);

		//query���� �����
		query = "SELECT * FROM psm_status WHERE psm_code = '"+psm_code+"'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
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
				else psm_subject = psm_subject;					//��ó�� �������� ������ �� ������
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_STATUS ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public psmStatusTable getStsDisplayPage(String psm_code,String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="",mode="";	
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		psmStatusTable table = null;
		ArrayList table_list = new ArrayList();

		//�Ѱ��� ���ϱ�
		query = "SELECT count(*) FROM psm_status WHERE psm_code = '"+psm_code+"'";
		total_cnt = getTotalCount(query);

		//query���� �����
		query = "SELECT * FROM psm_status WHERE psm_code = '"+psm_code+"'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		// ��ü �������� ���� ���Ѵ�.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//������ �ٷΰ��� �����
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=PsmStatusServlet?&mode=sts_list&page="+curpage+"&psm_code="+psm_code+">[Prev]</a>";
		}

		//�߰�
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

		//arraylist�� ���
		table = new psmStatusTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// PSM STATUS ��üLIST ��������
	//*******************************************************************/	
	public ArrayList getPsmStatusList(String psm_code) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmStatusTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
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
				else psm_subject = psm_subject;					//��ó�� �������� ������ �� ������
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		PSM BUDGET �� ���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� PSM_BUDGET���� �б�
	//*******************************************************************/	
	public psmBudgetTable readPsmBudget(String pid,String psm_code) throws Exception
	{
		//���� �ʱ�ȭ
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
			//�⺻���� ���
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
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// PID�� �ش� PSM_STATUS������ ���������� �б�
	//*******************************************************************/	
	public double[] readPsmBudget(String pid) throws Exception
	{
		//���� �ʱ�ȭ
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return budget;
	}
	//*******************************************************************
	// PSM BUDGET ��üLIST ��������
	//*******************************************************************/	
	public ArrayList getPsmBudgetList(String psm_code,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		fmt.setFormat("0,000");
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		psmBudgetTable table = null;
		ArrayList table_list = new ArrayList();

		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM psm_budget WHERE psm_code='"+psm_code+"'";
		total_cnt = getTotalCount(query);

		//query���� �����
		query = "SELECT * FROM psm_budget WHERE psm_code='"+psm_code+"'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_Budget ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public psmBudgetTable getBudDisplayPage(String psm_code,String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="",mode="";	
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		psmBudgetTable table = null;
		ArrayList table_list = new ArrayList();

		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM psm_budget WHERE psm_code='"+psm_code+"'";
		total_cnt = getTotalCount(query);

		//query���� �����
		query = "SELECT * FROM psm_budget WHERE psm_code='"+psm_code+"'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		// ��ü �������� ���� ���Ѵ�.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//������ �ٷΰ��� �����
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=PsmStatusServlet?&mode=bud_list&page="+curpage+"&psm_code="+psm_code+">[Prev]</a>";
		}

		//�߰�
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

		//arraylist�� ���
		table = new psmBudgetTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// PSM BUDGET ��üLIST ��������
	//*******************************************************************/	
	public ArrayList getPsmBudgetList(String psm_code) throws Exception
	{
		//���� �ʱ�ȭ
		fmt.setFormat("0,000");
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmBudgetTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		��������� �� �������� ���� ���ϱ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	��������� ������ ���� 
	//*******************************************************************/
	public String getStatusMgr() throws Exception
	{
		String data="";
		String where ="where code_s = 'PS02'";
		data = getColumData("prg_privilege","owner",where);
		return data;
	}
	//*******************************************************************
	//	�������� ������ ���� 
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
	//		���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	���� �ľ��ϱ� 
	//*******************************************************************/
	public int getTotalCount(String query) throws Exception
	{
		//���� �ʱ�ȭ
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
	// SQL update �����ϱ�
	//*******************************************************************/	
	public void executeUpdate(String update) throws Exception
	{
		Statement stmt = con.createStatement();
		stmt.executeUpdate(update);
		stmt.close();
	}

	//*******************************************************************
	//	�־��� ���̺��� �־��� ������ �÷��� ������ �б�
	//*******************************************************************/
	public String getColumData(String tablename,String getcolumn,String where) throws Exception
	{
		//���� �ʱ�ȭ
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
	//	�־��� ���̺��� �־��� ������ �÷��� 2��° ������ �б�
	//*******************************************************************/
	public String getSecondData(String tablename,String getcolumn,String where) throws Exception
	{
		//���� �ʱ�ȭ
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
	//	�����ڵ� ���ϱ�
	//*******************************************************************/
	public String getPsmCode(String query) throws Exception
	{
		//���� �ʱ�ȭ
		fmt.setFormat("00");
		String psm_code = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		if(rs.next()) psm_code = rs.getString("psm_code");
		
		//ã�� �������� +1
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
	//	�־�������� ������ڵ��
	//*******************************************************************/
	public String getDivCode(String sabun) throws Exception
	{
		//���� �ʱ�ȭ
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
