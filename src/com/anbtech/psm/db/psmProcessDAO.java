package com.anbtech.psm.db;
import com.anbtech.psm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class psmProcessDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat();	//����
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	private psmMasterTable pmt = null;				//help class
	private ArrayList pjt_list = new ArrayList();
	private int max_count = 0;						//������ ���� �ִ������
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public psmProcessDAO(Connection con) 
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
	public ArrayList readPsmCategoryList() throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmCategoryTable table = new com.anbtech.psm.entity.psmCategoryTable();
		
		query = "SELECT * FROM psm_category order by comp_english asc";
		rs = stmt.executeQuery(query);
		
		ArrayList category_list = new ArrayList();
		while(rs.next()) { 
			table = new psmCategoryTable();
			table.setPid(rs.getString("pid"));	
			table.setKoreaName(rs.getString("korea_name"));	
			table.setEnglishName(rs.getString("english_name"));	
			table.setKeyWord(rs.getString("key_word"));	
			table.setCompNo(rs.getString("comp_no"));	
			table.setCompKorea(rs.getString("comp_korea"));	
			table.setCompEnglish(rs.getString("comp_english"));	
			category_list.add(table);
		} 

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return category_list;
	}
	//*******************************************************************
	// ���� ���� ���� �б�
	//*******************************************************************/	
	public ArrayList readPsmProjectList() throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		psmEnvTable table = new com.anbtech.psm.entity.psmEnvTable();
		
		query = "SELECT * FROM psm_env where env_type='P' order by env_status asc";
		rs = stmt.executeQuery(query);
		
		ArrayList project_list = new ArrayList();
		while(rs.next()) { 
			table = new psmEnvTable();
			table.setPid(rs.getString("pid"));	
			table.setEnvType(rs.getString("env_type"));	
			table.setEnvStatus(rs.getString("env_status"));	
			table.setEnvName(rs.getString("env_name"));	
			project_list.add(table);
		} 

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return project_list;
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
		
		ArrayList color_list = new ArrayList();
		while(rs.next()) { 
			table = new psmEnvTable();
			table.setPid(rs.getString("pid"));	
			table.setEnvType(rs.getString("env_type"));	
			table.setEnvStatus(rs.getString("env_status"));	
			table.setEnvName(rs.getString("env_name"));	
			color_list.add(table);
		} 

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return color_list;
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
			table.setPsmKind("");
			table.setPsmView("");
			table.setLinkCode("");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	
	//*******************************************************************
	// PSM MASTER ��üLIST �������� : ���Ѻ� ������
	// �������۳⵵�� �����Ͽ� ...
	//*******************************************************************/	
	public ArrayList getPsmMasterList(String login_id,String psm_start_date,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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

		//���Ѻ� ���� ��ȸ
		String mgr = checkProjectGradeView(login_id); 
		if(mgr.equals("A")) {		//������ ��ȸ����
			//�Ѱ��� ���ϱ�
			query = "SELECT COUNT(*) FROM psm_master WHERE "+sItem+" like '%"+sWord+"%'";
			query += "and psm_start_date like '"+psm_start_date+"%' and psm_view in('V','VM')";
			total_cnt = getTotalCount(query);

			//query���� �����
			query = "SELECT * FROM psm_master WHERE "+sItem+" like '%"+sWord+"%'";
			query += " and psm_start_date like '"+psm_start_date+"%' and psm_view in('V','VM')";
			query += " order by pid desc";
			rs = stmt.executeQuery(query);
		} else {					//�ش���� ��ȸ����
			//�Ѱ��� ���ϱ�
			query = "SELECT COUNT(*) FROM psm_master a,psm_view_mgr b WHERE a."+sItem+" like '%"+sWord+"%'";
			query += " and psm_start_date like '"+psm_start_date+"%' and psm_view in('V','VM')";
			query += " and a.psm_code = b.psm_code and b.user_id='"+login_id+"'";
			total_cnt = getTotalCount(query);

			//query���� �����
			query = "SELECT * FROM psm_master a,psm_view_mgr b WHERE a."+sItem+" like '%"+sWord+"%'";
			query += " and psm_start_date like '"+psm_start_date+"%' and psm_view in('V','VM')";
			query += " and a.psm_code = b.psm_code and b.user_id='"+login_id+"'";
			query += " order by a.pid desc";
			rs = stmt.executeQuery(query);
		}

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
	// PSM_MASTER ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ� : ���Ѻ� ������
	//*******************************************************************/	
	public psmMasterTable getDisplayPage(String login_id,String psm_start_date,String sItem,String sWord,String mode,
			String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="";	
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();

		//���Ѻ� ���� ��ȸ
		String mgr = checkProjectGradeView(login_id); 
		if(mgr.equals("A")) {		//������ ��ȸ����
			//�Ѱ��� ���ϱ�
			query = "SELECT COUNT(*) FROM psm_master WHERE "+sItem+" like '%"+sWord+"%'";
			query += "and psm_start_date like '"+psm_start_date+"%' and psm_view in('V','VM')";
			total_cnt = getTotalCount(query);

			//query���� �����
			query = "SELECT * FROM psm_master WHERE "+sItem+" like '%"+sWord+"%'";
			query += " and psm_start_date like '"+psm_start_date+"%' and psm_view in('V','VM')";
			query += " order by pid desc";
			rs = stmt.executeQuery(query);
		} else {					//�ش���� ��ȸ����
			//�Ѱ��� ���ϱ�
			query = "SELECT COUNT(*) FROM psm_master a,psm_view_mgr b WHERE a."+sItem+" like '%"+sWord+"%'";
			query += " and psm_start_date like '"+psm_start_date+"%' and psm_view in('V','VM')";
			query += " and a.psm_code = b.psm_code and b.user_id='"+login_id+"'";
			total_cnt = getTotalCount(query);

			//query���� �����
			query = "SELECT * FROM psm_master a,psm_view_mgr b WHERE a."+sItem+" like '%"+sWord+"%'";
			query += " and psm_start_date like '"+psm_start_date+"%' and psm_view in('V','VM')";
			query += " and a.psm_code = b.psm_code and b.user_id='"+login_id+"'";
			query += " order by a.pid desc";
			rs = stmt.executeQuery(query);
		}

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
			pagecut = "<a href=PsmProcessServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&psm_start_date="+psm_start_date+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=PsmProcessServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&psm_start_date="+psm_start_date+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=PsmProcessServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&psm_start_date="+psm_start_date+">[Next]</a>";
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
	//	��ü������ ��ü��/īŸ���� ���������� : ��������� ����
	//*******************************************************************/
	public void getPsmCategoryQuery(String login_id,String psm_start_date,String comp_english,String comp_category) throws Exception
	{
		//���� �ʱ�ȭ
		fmt.setFormat("0,000");
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//���Ѻ� ���� ��ȸ
		String mgr = checkProjectGradeView(login_id); 
		if(mgr.equals("A")) {		//������ ��ȸ����
			//������ ���� �ִ� ������ ã��
			query = "SELECT count(*) FROM psm_master WHERE comp_name like '"+comp_english+"%'";
			query += " and comp_category like '"+comp_category+"%' and psm_start_date like '"+psm_start_date+"%'";
			query += " and psm_view in('V','VM')";
			int total_cnt = getTotalCount(query);
			if(total_cnt > max_count) max_count = total_cnt;

			//query���� �����
			query = "SELECT * FROM psm_master WHERE comp_name like '"+comp_english+"%'";
			query += " and comp_category like '"+comp_category+"%' and psm_start_date like '"+psm_start_date+"%'";
			query += " and psm_view in('V','VM')";
			query += " order by comp_name,psm_code asc";
			rs = stmt.executeQuery(query);
		} else {				//�ش������ ��ȸ����
			//������ ���� �ִ� ������ ã��
			query = "SELECT count(*) FROM psm_master a,psm_view_mgr b WHERE comp_name like '"+comp_english+"%'";
			query += " and comp_category like '"+comp_category+"%' and psm_start_date like '"+psm_start_date+"%'";
			query += " and psm_view in('V','VM') and a.psm_code=b.psm_code and b.user_id='"+login_id+"'";
			int total_cnt = getTotalCount(query);
			if(total_cnt > max_count) max_count = total_cnt;

			//query���� �����
			query = "SELECT * FROM psm_master a,psm_view_mgr b WHERE comp_name like '"+comp_english+"%'";
			query += " and comp_category like '"+comp_category+"%' and psm_start_date like '"+psm_start_date+"%'";
			query += " and psm_view in('V','VM') and a.psm_code=b.psm_code and b.user_id='"+login_id+"'";
			query += " order by comp_name,b.psm_code asc";
			rs = stmt.executeQuery(query);
		}

		while (rs.next()) {
			pmt = new psmMasterTable();
			
			pmt.setPid(rs.getString("pid"));	
			pmt.setPsmCode(rs.getString("psm_code"));	
			pmt.setPsmType(rs.getString("psm_type"));	
			pmt.setCompName(rs.getString("comp_name"));	
			pmt.setCompCategory(rs.getString("comp_category"));	
			pmt.setPsmKorea(rs.getString("psm_korea"));	
			pmt.setPsmEnglish(rs.getString("psm_english"));
			pmt.setPsmStartDate(anbdt.getSepDate(rs.getString("psm_start_date"),"/"));
			pmt.setPsmEndDate(anbdt.getSepDate(rs.getString("psm_end_date"),"/"));

			pmt.setPsmPm(rs.getString("psm_pm"));
			pmt.setPsmPmDiv(rs.getString("psm_pm_div"));
			pmt.setPsmMgr(rs.getString("psm_mgr"));
			pmt.setPsmMgrDiv(rs.getString("psm_mgr_div"));
			pmt.setPsmBudget(rs.getString("psm_budget"));
			pmt.setPsmBudgetDiv(rs.getString("psm_budget_div"));
			pmt.setPsmUser(rs.getString("psm_user"));
			pmt.setPsmUserDiv(rs.getString("psm_user_div"));
			pmt.setPsmDesc(rs.getString("psm_desc"));
	
			pmt.setPlanSum(fmt.DoubleToString(rs.getDouble("plan_sum")));
			pmt.setPlanLabor(fmt.DoubleToString(rs.getDouble("plan_labor")));
			pmt.setPlanMaterial(fmt.DoubleToString(rs.getDouble("plan_material")));
			pmt.setPlanCost(fmt.DoubleToString(rs.getDouble("plan_cost")));
			pmt.setPlanPlant(fmt.DoubleToString(rs.getDouble("plan_plant")));

			pmt.setResultSum(fmt.DoubleToString(rs.getDouble("result_sum")));
			pmt.setResultLabor(fmt.DoubleToString(rs.getDouble("result_labor")));
			pmt.setResultMaterial(fmt.DoubleToString(rs.getDouble("result_material")));
			pmt.setResultCost(fmt.DoubleToString(rs.getDouble("result_cost")));
			pmt.setResultPlant(fmt.DoubleToString(rs.getDouble("result_plant")));

			pmt.setDiffSum(fmt.DoubleToString(rs.getDouble("plan_sum")-rs.getDouble("result_sum")));
			pmt.setDiffLabor(fmt.DoubleToString(rs.getDouble("plan_labor")-rs.getDouble("result_labor")));
			pmt.setDiffMaterial(fmt.DoubleToString(rs.getDouble("plan_material")-rs.getDouble("result_material")));
			pmt.setDiffCost(fmt.DoubleToString(rs.getDouble("plan_cost")-rs.getDouble("result_cost")));
			pmt.setDiffPlant(fmt.DoubleToString(rs.getDouble("plan_plant")-rs.getDouble("result_plant")));

			pmt.setContractDate(anbdt.getSepDate(rs.getString("contract_date"),"/"));
			pmt.setContractName(rs.getString("contract_name"));
			pmt.setContractPrice(fmt.DoubleToString(rs.getDouble("contract_price")));
			pmt.setCompleteDate(anbdt.getSepDate(rs.getString("complete_date"),"/"));
			pmt.setFname(rs.getString("fname"));
			pmt.setSname(rs.getString("sname"));
			pmt.setFtype(rs.getString("ftype"));
			pmt.setFsize(rs.getString("fsize"));
			pmt.setPsmStatus(rs.getString("psm_status"));
			pmt.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
			pmt.setAppDate(anbdt.getSepDate(rs.getString("app_date"),"-"));

			pmt.setPdCode(rs.getString("pd_code"));
			pmt.setPdName(rs.getString("pd_name"));
			pmt.setPsmKind(rs.getString("psm_kind"));
			pmt.setPsmView(rs.getString("psm_view"));
			pmt.setLinkCode(rs.getString("link_code"));

			pjt_list.add(pmt);
		}
		rs.close();
		stmt.close(); 


	}

	/**********************************************************************
	 * ��ü��/īŸ���� ������ ������ü�� ArrayList�� ���
	 *********************************************************************/
	public ArrayList getCategoryProjects(String login_id,String psm_start_date) throws Exception
	{
		pjt_list = new ArrayList();

		//���İ���
		psmCategoryTable category = new psmCategoryTable();
		ArrayList category_list = new ArrayList();
		category_list = readPsmCategoryList();
		Iterator category_iter = category_list.iterator();
		while(category_iter.hasNext()) {
			category = (psmCategoryTable)category_iter.next();
			if(!category.getCompEnglish().equals("����")) {
				getPsmCategoryQuery(login_id,psm_start_date,category.getCompEnglish(),category.getKoreaName());
			}
		}

		//�������
		category_list = readPsmCategoryList();
		Iterator ready_iter = category_list.iterator();
		while(ready_iter.hasNext()) {
			category = (psmCategoryTable)ready_iter.next();
			if(category.getCompEnglish().equals("����")) {
				getPsmCategoryQuery(login_id,psm_start_date,"����",category.getKoreaName());	
			}
		}


/*		//����غ���
		psmMasterTable table = new psmMasterTable();
		Iterator pjt_iter = pjt_list.iterator();
		while(pjt_iter.hasNext()) {
			table = (psmMasterTable)pjt_iter.next();
			System.out.println(table.getCompName()+":"+table.getCompCategory()+":"+table.getPsmKorea());
		}
		System.out.println("max data : " + max_count);
*/
		return pjt_list;
	}

	/**********************************************************************
	 * ��ü��/īŸ���� ������ �� �ִ�� ���� ��������
	 *********************************************************************/
	public int getMaxCountProjects() throws Exception
	{
		return max_count;
	}

	//*******************************************************************
	//	������� ���� ���۳⵵ ���ϱ�
	//*******************************************************************/
	public String getPsmFirstYear() throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select psm_start_date from psm_master order by psm_start_date asc";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString("psm_start_date");
		}

		//���� ���۳⵵�� ���ϱ�
		if(data.length() == 0) data = anbdt.getYear();
		else data = data.substring(0,4);
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	������� �����ֱ� ��ϳ⵵ ���ϱ�
	//*******************************************************************/
	public String getPsmLastYear() throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select psm_start_date from psm_master order by psm_start_date desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString("psm_start_date");
		}

		//�����ֱ� ��ϳ⵵�� ���ϱ�
		if(data.length() == 0) data = anbdt.getYear();
		else data = data.substring(0,4);
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	// PSM MASTER ��üLIST �������� : EXCEL ����ϱ� : A��� ����
	// �������۳⵵�� �����Ͽ� ...
	//*******************************************************************/	
	public ArrayList getPsmMasterExcelList(String login_id,String psm_start_date,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		fmt.setFormat("0,000");
		String query="",where="",psm_subject="",psm_type="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();

		//������ȸ ���Ѻ� �����ϱ�
		String mgr = checkProjectGradeView(login_id); 
		if(mgr.equals("A")) {		//������ ��ȸ����
			query = "SELECT * FROM psm_master WHERE "+sItem+" like '%"+sWord+"%'";
			query += " and psm_start_date like '"+psm_start_date+"%' and psm_view in ('V','VM')";
			query += " order by pid desc";
		} else {					//�ش���� ��ȸ����
			query = "SELECT * FROM psm_master a,psm_view_mgr b WHERE a."+sItem+" like '%"+sWord+"%'";
			query += " and psm_start_date like '"+psm_start_date+"%' and psm_view in ('V','VM')";
			query += " and a.psm_code = b.psm_code and b.user_id='"+login_id+"'";
			query += " order by a.pid desc";
		}
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new psmMasterTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setPsmCode(rs.getString("psm_code"));	

				psm_type = rs.getString("psm_type");
				table.setPsmType(psm_type);	
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
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// PSM MASTER ��üLIST �������� : ����ã��(������ : ����,�����������)
	// �������۳⵵�� �����Ͽ� ...
	//*******************************************************************/	
	public ArrayList getProjectlList(String psm_start_date,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		fmt.setFormat("0,000");
		String query="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM psm_master WHERE "+sItem+" like '%"+sWord+"%'";
		query += " and psm_status in ('2','3') ";
		query += " and psm_start_date like '"+psm_start_date+"%'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new psmMasterTable();
								
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
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// PSM MASTER ��üLIST �������� : ���ΰ���ã��(�������µ�Ͻ� ���ΰ����� ������ ������)
	// �������۳⵵�� �����Ͽ� ...
	//*******************************************************************/	
	public ArrayList getSingleList(String psm_start_date,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		fmt.setFormat("0,000");
		String query="";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		psmMasterTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM psm_master WHERE "+sItem+" like '%"+sWord+"%'";
		query += " and psm_status in ('1','11','2','3') and psm_view like 'V%'";
		query += " and psm_start_date like '"+psm_start_date+"%'";
		query += " order by pid desc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new psmMasterTable();
								
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
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	
	//--------------------------------------------------------------------
	//
	//		���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	���� ��ȸ ���� �˻��ϱ� 
	//*******************************************************************/
	public String checkProjectView(String user_id) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select * from psm_view_mgr where user_id = '"+user_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString("user_id");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	���� ��ȸ ���� ��ޱ��� �˻��ϱ� 
	//  ��� G : �ش������ , ��� A : ������ ��ȸ������
	//*******************************************************************/
	public String checkProjectGradeView(String user_id) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "G";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select pjt_type from psm_view_mgr where user_id = '"+user_id+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data = rs.getString("pjt_type");
			if(data.equals("A")) break;
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
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
