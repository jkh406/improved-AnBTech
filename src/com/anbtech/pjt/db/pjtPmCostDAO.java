package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtPmCostDAO
{
	private Connection con;
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����ó��
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��
	com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("0,000");		//�������(�ݾ�)
	com.anbtech.util.normalFormat pro = new com.anbtech.util.normalFormat("0.0");		//�������(����)

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
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
	//	�� ���� �ľ��ϱ� [�ش���� ���List ��ü����]
	//*******************************************************************/
	private int getTotalCount(String pjt_code,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
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
	//	�� ������ �� ���ϱ�
	//*******************************************************************/
	public int getTotalPage() 
	{
		return this.total_page;
	}

	//*******************************************************************
	//	�� ������ �� ���ϱ�
	//*******************************************************************/
	public int getCurrentPage() 
	{
		return this.current_page;
	}

	//*******************************************************************
	// �ش������ ��� List
	//*******************************************************************/	
	public ArrayList getCostList (String pjt_code,String sItem,String sWord,
		String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		double plan_rst = 0.0;			//��ü ��ȹ �Ѻ��
		double result_rst = 0.0;		//��ü ���� �Ѻ��
		double cost_rst = 0.0;			//���� ���� �Ѻ��
		double progress = 0.0;			//������ ����(�����)

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		total_cnt = getTotalCount(pjt_code,sItem,sWord);

		//�������� ���ϱ� (����/���� �㰡�����Ǵ�)
		String todate = anbdt.getDate();		//yyyy-MM-dd
			
		//query���� �����
		query = "SELECT * FROM pjt_cost where pjt_code='"+pjt_code+"'";
		query += " and "+sItem+" like '%"+sWord+"%'"; 
		query += " order by pid DESC";
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

		//��û��[sWord]����ݾ׹� �����ѱݾ� ��������
		plan_rst = getPjtPlanCost(pjt_code,sWord);			//��ü ��ȹ �Ѻ��
		result_rst = getPjtResultCost(pjt_code,sWord);		//��ü ���� �Ѻ��
	
		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) {
			if(i==1) cost_rst = result_rst;			//�ʱ�ȭ�Ѵ�.
			for(int j=0; j<max_display_cnt; j++) {
				if(rs.next()) {
					cost_rst -= rs.getDouble("node_cost");		//�ش���������ŭ�� ��������� ����.
				}
			}
		}
		
		//�������� 1������ �ϰ�� �ʱ�ȭ �ϱ�
		if(current_page == 1)	cost_rst = result_rst;	//�ʱ�ȭ�Ѵ�.

		//������ ���
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
				
				//���� �ۼ�
				String cost_type = rs.getString("cost_type");	if(cost_type == null) cost_type = "0";
				String cost_name = "��Ÿ���";
				if(cost_type.equals("1")) cost_name = "�ΰǺ�";
				else if(cost_type.equals("2")) cost_name = "SAMPLE";
				else if(cost_type.equals("3")) cost_name = "������";
				else if(cost_type.equals("4")) cost_name = "���ڰ��";
				else if(cost_type.equals("5")) cost_name = "�԰ݽ��κ�";
				else if(cost_type.equals("6")) cost_name = "�ü����ں�";
				else cost_name = "��Ÿ���";
				table.setCostType(cost_name);

				//��ȹ �ѿ�����
				table.setCostExp(fmt.DoubleToString(plan_rst));

				//���װ���ϱ� (���� - ����)
				String dif_cost = "";
				if(show_cnt != 0) cost_rst -= rs.getDouble("node_cost");	//ó���� �ʱ�ȭ������ ����
				dif_cost = fmt.DoubleToString(plan_rst-cost_rst);
				table.setDifCost(dif_cost);

				//�������� ����ϱ� : ��ȹ���� �������� �����Ѵ�.
				if(plan_rst > 0) 
					progress = Double.parseDouble(pro.DoubleToString(cost_rst/plan_rst*100));
				table.setProgress(progress);

				//����,����,�������� ǥ�� [��,����/������ ���Ͽ� ���� �ۼ��ڸ��� ������]
				String subMod="",subDel="",subView="";
				if(todate.equals(in_date)) {		//����
					subMod = "<a href=\"javascript:costModify('"+pid+"');\">����</a>";
					subDel = "<a href=\"javascript:costDelete('"+pid+"');\">����</a>";
					subView = "<a href=\"javascript:costView('"+pid+"');\">����</a>";
				} else {			
					subView = "<a href=\"javascript:costView('"+pid+"');\">����</a>";
				}
				
				table.setView(subView);
				table.setModify(subMod);
				table.setDelete(subDel);
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش���� �׷����� �׸������� ����[�ݾ�]���ϱ� 
	// [����/���� �ݾ��� ���� ū�ݾ��� �̿�]
	//*******************************************************************/	
	public int getCostUnit (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		int unit = 1;			//�����ݾ�
		int cost = 100;			//�����ݾ�
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;

		//��������
		stmt = con.createStatement();
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//����ū�ݾ��� ��
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

		//�����ݾ� ���ϱ�
		unit = cost / 100;
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return unit;
	}

	//*******************************************************************
	// �ش���� �׷����� �׸������� �����׿����� �����ݾ����� �������� ���ϱ� 
	// [����/���� �ݾ� / �����ݾ�]
	//*******************************************************************/	
	public ArrayList getAccountNumber (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		int unit = getCostUnit (pjt_code);			//�����ݾ�
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;

		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//��������
		stmt = con.createStatement();
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//���� ���
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	

	//*******************************************************************
	// �ش���� QUERY�ϱ� (���� �б� : �����ڵ�)
	//*******************************************************************/	
	public ArrayList getPjtRead (String pjt_code,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		double cost_rst = 0.0;		//�� �������ݾ�
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
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

				//�� ���� ��� ����ϱ�
				cost_rst += rs.getDouble("result_labor");
				cost_rst += rs.getDouble("result_sample");
				cost_rst += rs.getDouble("result_metal");
				cost_rst += rs.getDouble("result_mup");
				cost_rst += rs.getDouble("result_oversea");
				cost_rst += rs.getDouble("result_plant");
				String rst_cost = fmt.DoubleToString(cost_rst);
				table.setCostRst(rst_cost);

				//���װ���ϱ� (���� - ����)
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش���� ������ ��ȹ��� ���ϱ�
	//*******************************************************************/	
	public double getPjtPlanCost (String pjt_code,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		double cost_rst = 0.0;		
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		String cost_name = "";

		stmt = con.createStatement();
		
		//sWord�� cost_name���ϱ�
		if(sWord.equals(""))  			cost_name = "cost_exp";
		else if(sWord.equals("1"))		cost_name = "plan_labor";
		else if(sWord.equals("2"))		cost_name = "plan_sample";
		else if(sWord.equals("3"))		cost_name = "plan_metal";
		else if(sWord.equals("4"))		cost_name = "plan_mup";
		else if(sWord.equals("5"))		cost_name = "plan_oversea";
		else if(sWord.equals("6"))		cost_name = "plan_plant";

		//query���� �����
		query = "SELECT "+cost_name+" FROM pjt_general where pjt_code='"+pjt_code+"'";
		rs = stmt.executeQuery(query);

		//������ ���
		if(rs.next()) { 
			cost_rst = rs.getDouble(cost_name);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return cost_rst;
	}

	//*******************************************************************
	// �ش���� ������ ������� ���ϱ�
	//*******************************************************************/	
	public double getPjtResultCost (String pjt_code,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		double cost_rst = 0.0;		
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		String cost_name = "";

		stmt = con.createStatement();
		
		//sWord�� cost_name���ϱ�
		if(sWord.equals("1"))		cost_name = "result_labor";
		else if(sWord.equals("2"))		cost_name = "result_sample";
		else if(sWord.equals("3"))		cost_name = "result_metal";
		else if(sWord.equals("4"))		cost_name = "result_mup";
		else if(sWord.equals("5"))		cost_name = "result_oversea";
		else if(sWord.equals("6"))		cost_name = "result_plant";

		//query���� �����
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";
		rs = stmt.executeQuery(query);

		//������ ���
		if(rs.next()) { 
			if(sWord.equals("")) {						//��ü���� �ѱݾ� ���ϱ�
				cost_rst += rs.getDouble("result_labor");
				cost_rst += rs.getDouble("result_sample");
				cost_rst += rs.getDouble("result_metal");
				cost_rst += rs.getDouble("result_mup");
				cost_rst += rs.getDouble("result_oversea");
				cost_rst += rs.getDouble("result_plant");
			} else cost_rst = rs.getDouble(cost_name);	//�������� �ѱݾ� ���ϱ�
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return cost_rst;
	}

	//*******************************************************************
	// �ش������ ���(activity) �����ϱ� 
	//*******************************************************************/	
	public ArrayList getNodeList (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' order by level_no,child_node ASC";
		rs = stmt.executeQuery(query);

		//������ ���
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش���� ��� List��������
	//*******************************************************************/	
	public ArrayList getPjtMember (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		
		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_member where pjt_code='"+pjt_code+"' order by pjt_mbr_type ASC";	
		rs = stmt.executeQuery(query);

		//������ ���
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// �ش���� ������ ��� ���� ����
	//*******************************************************************/	
	public ArrayList getCostRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_cost where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	/***************************************************************************
	 * ID�� ���ϴ� �޼ҵ�
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




