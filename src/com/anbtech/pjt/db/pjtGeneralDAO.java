package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtGeneralDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public pjtGeneralDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtGeneralDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ� (��ü)
	//*******************************************************************/
	public int getAllTotalCount(String pjtWord,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_general where prs_type='P'";
		query += " and pjt_status like '%"+pjtWord+"%'";
		query += " and ("+sItem+" like '%"+sWord+"%')"; 
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
	//*******************************************************************
	//	�� ���� �ľ��ϱ� (�μ���)
	//*******************************************************************/
	public int getDivTotalCount(String div_code,String pjtWord,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_general where prs_type='"+div_code+"'";
		query += " and pjt_status like '%"+pjtWord+"%'";
		query += " and ("+sItem+" like '%"+sWord+"%')"; 
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
	// ���� LIST QUERY�ϱ� (���� LIST�б�)
	//*******************************************************************/	
	public ArrayList getAllGeneralList (String login_id,String pjtWord,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();
		
		//���������� ���� �Ǵ��ϱ�
		String pjt_mgr = "N";
		pjt_mgr = checkPjtMgr (login_id);

		//�Ѱ��� ���ϱ�
		total_cnt = getAllTotalCount(pjtWord,sItem,sWord);
			
		//query���� �����
		query = "SELECT * FROM pjt_general where prs_type='P'";	
		query += " and pjt_status like '%"+pjtWord+"%'";
		query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 
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
				table = new projectTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String pjt_code = rs.getString("pjt_code");
				table.setPjtCode(pjt_code);	

				table.setPjtName(rs.getString("pjt_name"));	
				table.setOwner(rs.getString("owner"));
				table.setInDate(rs.getString("in_date"));
				table.setPjtMbrId(rs.getString("pjt_mbr_id"));
				table.setPjtClass(rs.getString("pjt_class"));
				table.setPjtTarget(rs.getString("pjt_target"));
				table.setMgtPlan(rs.getString("mgt_plan"));
				table.setParentCode(rs.getString("parent_code"));
				table.setMbrExp(rs.getInt("mbr_exp"));
				table.setCostExp(rs.getString("cost_exp"));
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

				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				table.setFlag(rs.getString("flag"));
				table.setPlanLabor(rs.getString("plan_labor"));
				table.setPlanSample(rs.getString("plan_sample"));
				table.setPlanMetal(rs.getString("plan_metal"));
				table.setPlanMup(rs.getString("plan_mup"));
				table.setPlanOversea(rs.getString("plan_oversea"));
				table.setPlanPlant(rs.getString("plan_plant"));
				table.setResultLabor(rs.getString("result_labor"));
				table.setResultSample(rs.getString("result_sample"));
				table.setResultMetal(rs.getString("result_metal"));
				table.setResultMup(rs.getString("result_mup"));
				table.setResultOversea(rs.getString("result_oversea"));
				table.setResultPlant(rs.getString("result_plant"));
				
				//���� or �������� ǥ�� [login_id�� ������������ ��츸 ����]
				String subMod="",subDel="";
				if(status.equals("S") && pjt_mgr.equals("Y")) {				//������ ����
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pjt_code+"');\">[����]</a>";
				} else if(status.equals("3") && pjt_mgr.equals("Y")) {		//���� DROP����
					subDel = "<a href=\"javascript:contentDelete('"+pjt_code+"');\">[����]</a>";
				} else if(pjt_mgr.equals("Y")) {							//��Ÿ
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
				}
			
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
	// �μ��� LIST QUERY�ϱ� (��ü LIST�б�)
	//*******************************************************************/	
	public ArrayList getDivGeneralList (String login_id,String pjtWord,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//���������� ���� �Ǵ��ϱ�
		String pjt_mgr = "N";
		pjt_mgr = checkPjtMgr (login_id);

		//login id�� �μ��ڵ� ã��
		String login_division = searchAcId (login_id);

		//�Ѱ��� ���ϱ�
		total_cnt = getDivTotalCount(login_division,pjtWord,sItem,sWord);
			
		//query���� �����
		query = "SELECT * FROM pjt_general where prs_type='"+login_division+"'";	
		query += " and pjt_status like '%"+pjtWord+"%'";
		query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 
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
				table = new projectTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String pjt_code = rs.getString("pjt_code");
				table.setPjtCode(pjt_code);							
			
				table.setPjtName(rs.getString("pjt_name"));	
				table.setOwner(rs.getString("owner"));
				table.setInDate(rs.getString("in_date"));

				String mbr_id = rs.getString("pjt_mbr_id");
				table.setPjtMbrId(mbr_id);
				table.setPjtClass(rs.getString("pjt_class"));
				table.setPjtTarget(rs.getString("pjt_target"));
				table.setMgtPlan(rs.getString("mgt_plan"));
				table.setParentCode(rs.getString("parent_code"));
				table.setMbrExp(rs.getInt("mbr_exp"));
				table.setCostExp(rs.getString("cost_exp"));
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

				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				table.setFlag(rs.getString("flag"));
				table.setPlanLabor(rs.getString("plan_labor"));
				table.setPlanSample(rs.getString("plan_sample"));
				table.setPlanMetal(rs.getString("plan_metal"));
				table.setPlanMup(rs.getString("plan_mup"));
				table.setPlanOversea(rs.getString("plan_oversea"));
				table.setPlanPlant(rs.getString("plan_plant"));
				table.setResultLabor(rs.getString("result_labor"));
				table.setResultSample(rs.getString("result_sample"));
				table.setResultMetal(rs.getString("result_metal"));
				table.setResultMup(rs.getString("result_mup"));
				table.setResultOversea(rs.getString("result_oversea"));
				table.setResultPlant(rs.getString("result_plant"));
				
				//���� or �������� ǥ�� [login_id�� ����PM/PL�� ��츸 ����]
				String subMod="",subDel="";
				if(status.equals("S") && pjt_mgr.equals("Y")) {			//������ ����
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pjt_code+"');\">[����]</a>";
				} else if(status.equals("3") && pjt_mgr.equals("Y")) {	//���� DROP����
					subDel = "<a href=\"javascript:contentDelete('"+pjt_code+"');\">[����]</a>";
				} else if(pjt_mgr.equals("Y")){							//��Ÿ
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
				}
			
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
	// ��ü ������ ���� LIST : �������� �űԵ�Ͻ� �ʿ�
	//*******************************************************************/	
	public ArrayList getAllStandbyProjectList () throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query���� �����
		query = "SELECT * FROM prs_project where pjt_status='S' and type='P' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new pjtCodeTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ��ü ������,��������� ���� LIST : �������� ������ �ʿ�
	//*******************************************************************/	
	public ArrayList getAllReadyProjectList () throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query���� �����
		query = "SELECT * FROM prs_project where (pjt_status='S' or pjt_status='0') and type='P' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new pjtCodeTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �μ��� ������ ���� LIST : �������� �űԵ�Ͻ� �ʿ�
	//*******************************************************************/	
	public ArrayList getDivStandbyProjectList (String login_id) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//login id�� �μ��ڵ� ã��
		String login_division = searchAcId (login_id);

		stmt = con.createStatement();
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query���� �����
		query = "SELECT * FROM prs_project where pjt_status='S' and type='"+login_division+"' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new pjtCodeTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �μ��� ������,��������� ���� LIST : �������� ������ �ʿ�
	//*******************************************************************/	
	public ArrayList getDivReadyProjectList (String login_id) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//login id�� �μ��ڵ� ã��
		String login_division = searchAcId (login_id);

		stmt = con.createStatement();
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query���� �����
		query = "SELECT * FROM prs_project where (pjt_status='S' or pjt_status='0') and type='"+login_division+"' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new pjtCodeTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ���ΰ����� ã�� : �������� ��Ͻ� �ʿ�
	//*******************************************************************/	
	public ArrayList getMainProjectList () throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query���� �����
		query = "SELECT * FROM prs_project where pjt_status='0' or pjt_status='1' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new pjtCodeTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}


	//*******************************************************************
	//	���������� �����ڿ��� �Ǵ��ϱ�
	//*******************************************************************/	
	public String checkPjtMgr (String login_id) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where keyname = 'PJT_MGR' and owner like '%"+login_id+"%'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		if(cnt > 0) rtn = "Y";
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;	
	}

	//*******************************************************************
	// �ش���� QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	public ArrayList getGeneralRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_general where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
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
				table.setCostExp(sp.getMoneyFormat(rs.getString("cost_exp"),""));
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
				table.setPlanLabor(sp.getMoneyFormat(rs.getString("plan_labor"),""));
				table.setPlanSample(sp.getMoneyFormat(rs.getString("plan_sample"),""));
				table.setPlanMetal(sp.getMoneyFormat(rs.getString("plan_metal"),""));
				table.setPlanMup(sp.getMoneyFormat(rs.getString("plan_mup"),""));
				table.setPlanOversea(sp.getMoneyFormat(rs.getString("plan_oversea"),""));
				table.setPlanPlant(sp.getMoneyFormat(rs.getString("plan_plant"),""));
				table.setResultLabor(sp.getMoneyFormat(rs.getString("result_labor"),""));
				table.setResultSample(sp.getMoneyFormat(rs.getString("result_sample"),""));
				table.setResultMetal(sp.getMoneyFormat(rs.getString("result_metal"),""));
				table.setResultMup(sp.getMoneyFormat(rs.getString("result_mup"),""));
				table.setResultOversea(sp.getMoneyFormat(rs.getString("result_oversea"),""));
				table.setResultPlant(sp.getMoneyFormat(rs.getString("result_plant"),""));
				
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
	public ArrayList getPjtRead (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
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
				table.setCostExp(sp.getMoneyFormat(rs.getString("cost_exp"),""));
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
				table.setPlanLabor(sp.getMoneyFormat(rs.getString("plan_labor"),""));
				table.setPlanSample(sp.getMoneyFormat(rs.getString("plan_sample"),""));
				table.setPlanMetal(sp.getMoneyFormat(rs.getString("plan_metal"),""));
				table.setPlanMup(sp.getMoneyFormat(rs.getString("plan_mup"),""));
				table.setPlanOversea(sp.getMoneyFormat(rs.getString("plan_oversea"),""));
				table.setPlanPlant(sp.getMoneyFormat(rs.getString("plan_plant"),""));
				table.setResultLabor(sp.getMoneyFormat(rs.getString("result_labor"),""));
				table.setResultSample(sp.getMoneyFormat(rs.getString("result_sample"),""));
				table.setResultMetal(sp.getMoneyFormat(rs.getString("result_metal"),""));
				table.setResultMup(sp.getMoneyFormat(rs.getString("result_mup"),""));
				table.setResultOversea(sp.getMoneyFormat(rs.getString("result_oversea"),""));
				table.setResultPlant(sp.getMoneyFormat(rs.getString("result_plant"),""));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* �����⺻���� ���� �����ϱ� 
	*******************************************************************/
	public void inputGeneral(String pid,String pjt_code,String pjt_name,String owner,String in_date,String pjt_mbr_id,
		String pjt_class,String pjt_target,String mgt_plan,String parent_code,String mbr_exp,String cost_exp,
		String plan_start_date,String plan_end_date,String prs_code,String prs_type,String pjt_desc,String pjt_spec,
		String pjt_status,String plan_labor,String plan_sample,String plan_metal,String plan_mup,String plan_oversea,
		String plan_plant) throws Exception
	{
		String input = "",update="";
		Statement stmt = null;
		stmt = con.createStatement();

		//1.�⺻����(pjt_general) �����ϱ�[��ü]
		input = "INSERT INTO pjt_general(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,";
		input +="pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,";
		input += "prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,";
		input += "plan_mup,plan_oversea,plan_plant) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+owner+"','"+in_date+"','"+pjt_mbr_id+"','"+pjt_class+"','";
		input += pjt_target+"','"+mgt_plan+"','"+parent_code+"','"+Integer.parseInt(mbr_exp)+"','"+Double.parseDouble(cost_exp)+"','"+plan_start_date+"','"+plan_end_date+"','";
		input += prs_code+"','"+prs_type+"','"+pjt_desc+"','"+pjt_spec+"','"+pjt_status+"','"+Double.parseDouble(plan_labor)+"','"+Double.parseDouble(plan_sample)+"','";
		input += Double.parseDouble(plan_metal)+"','"+Double.parseDouble(plan_mup)+"','"+Double.parseDouble(plan_oversea)+"','"+Double.parseDouble(plan_plant)+"')";
		stmt.executeUpdate(input);

		//2.�η�����(pjt_member) �����ϱ�[����PM����]
		inputMember(pid,pjt_code,pjt_name,plan_start_date,plan_end_date,pjt_mbr_id);

		//3.��������(pjt_schedule) �����ϱ�[�����ڵ�,������,�ش����μ���(����,�ڳ��,����,����)]
		inputSchedule(pid,pjt_code,pjt_name,prs_code,prs_type);

		//4.���⹰����(pjt_document) �����ϱ�[�����ڵ�,������,�ش����μ���(����,�ڳ��,����,����)]
		inputDocument(pid,pjt_code,pjt_name,prs_code,prs_type);

		//5.���Ѱ���(pjt_grade_mgr) �����ϱ�[keyname,owner,div_code]
		inputGradeMgr(pid,pjt_mbr_id);

		//6.��������(prs_project) ���� �ٲ��ֱ�
		update = "UPDATE prs_project set pjt_status='0' where pjt_code='"+pjt_code+"' and type='"+prs_type+"'";
		stmt.executeUpdate(update);

		//7.��������(��ȹ�Ⱓ) �Է��ϱ�
		update = "UPDATE pjt_schedule set plan_start_date='"+plan_start_date+"',";
		update += "plan_end_date='"+plan_end_date+"' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		stmt.close();

	}

	/*******************************************************************
	* �����η����� ���� �����ϱ� 
	*******************************************************************/
	public void inputMember(String pid,String pjt_code,String pjt_name,String plan_start_date,String plan_end_date,String pjt_mbr_id) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//����� ã��
		String sabun = pjt_mbr_id.substring(0,pjt_mbr_id.indexOf("/"));

		//PM��������
		String[] man = new String[5];
		man = searchManinfo(sabun);
		String name="",tel="",grade="",div="";
		name=man[1]; tel=man[2]; grade=man[3]; div=man[4];
		String poration = "1.0";

		input = "INSERT INTO pjt_member(pid,pjt_code,pjt_name,pjt_mbr_type,mbr_start_date,mbr_end_date,";
		input += "mbr_poration,pjt_mbr_id,pjt_mbr_name,pjt_mbr_job,pjt_mbr_tel,pjt_mbr_grade,pjt_mbr_div) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+"A"+"','"+plan_start_date+"','"+plan_end_date+"','";
		input += Double.parseDouble(poration)+"','"+sabun+"','"+name+"','PM','"+tel+"','"+grade+"','"+div+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}

	/*******************************************************************
	* ������������ ���� �����ϱ� 
	*******************************************************************/
	public void inputSchedule(String pid,String pjt_code,String pjt_name,String prs_code,String prs_type) throws Exception
	{
		String input = "",query="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("00");

		//�ش����μ����� �Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM prs_process where prs_code='"+prs_code+"' and type='"+prs_type+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		String[][] data = new String[cnt][7];	//pid,pjt_code,pjt_name,����,�ڳ��,����,����

		//�ش����μ����� ��������� �����ϱ�
		query = "SELECT parent_node,child_node,level_no,node_name FROM prs_process ";
		query += "where prs_code='"+prs_code+"' and type='"+prs_type+"' order by level_no asc";	

		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) { 
			data[n][0] = pid + nfm.toDigits(n);
			data[n][1] = pjt_code;							
			data[n][2] = pjt_name;	
			data[n][3] = rs.getString("parent_node");	
			data[n][4] = rs.getString("child_node");	
			data[n][5] = rs.getString("level_no");	
			data[n][6] = rs.getString("node_name");	
			n++;
		}

		//���������� ����ϱ�
		for(int i=0; i<n; i++) {
			input = "INSERT INTO pjt_schedule(pid,pjt_code,pjt_name,parent_node,child_node,level_no,";
			input += "node_name) values('";
			input += data[i][0]+"','"+data[i][1]+"','"+data[i][2]+"','"+data[i][3]+"','"+data[i][4]+"','";
			input += data[i][5]+"','"+data[i][6]+"')";
			stmt.executeUpdate(input);
		}

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* �������⹰���� ���� �����ϱ� 
	*******************************************************************/
	public void inputDocument(String pid,String pjt_code,String pjt_name,String prs_code,String prs_type) throws Exception
	{
		String input = "",query="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("00");

		//�ش����μ����� �Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM prs_document where prs_code='"+prs_code+"' and type='"+prs_type+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		String[][] data = new String[cnt][7];	//pid,pjt_code,pjt_name,����,�ڳ��,����,����

		//�ش����μ����� ��������� �����ϱ�
		query = "SELECT parent_node,child_node,level_no,node_name FROM prs_document ";
		query += "where prs_code='"+prs_code+"' and type='"+prs_type+"' order by level_no asc";	
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) { 
			data[n][0] = pid + nfm.toDigits(n);
			data[n][1] = pjt_code;							
			data[n][2] = pjt_name;	
			data[n][3] = rs.getString("parent_node");	
			data[n][4] = rs.getString("child_node");	
			data[n][5] = rs.getString("level_no");	
			data[n][6] = rs.getString("node_name");	
			n++;
		}

		//���������� ����ϱ�
		for(int i=0; i<n; i++) {
			input = "INSERT INTO pjt_document(pid,pjt_code,pjt_name,parent_node,child_node,level_no,";
			input += "node_name) values('";
			input += data[i][0]+"','"+data[i][1]+"','"+data[i][2]+"','"+data[i][3]+"','"+data[i][4]+"','";
			input += data[i][5]+"','"+data[i][6]+"')";
			stmt.executeUpdate(input);
		}

		stmt.close();
		rs.close();
	}
	/*******************************************************************
	* ������������ ���� �����ϱ� 
	*******************************************************************/
	public void inputGradeMgr(String pid,String pjt_mbr_id) throws Exception
	{
		String input = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//���� PJT_PML�� ��ϵǾ����� �Ǵ��ϱ�
		String mgr = "";
		String query = "SELECT keyname FROM pjt_grade_mgr where owner='"+pjt_mbr_id+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) mgr += rs.getString("keyname");

		//����ϱ�
		if(mgr.indexOf("PJT_PML") == -1) {
			//�μ��ڵ� ã��
			String sabun = pjt_mbr_id.substring(0,pjt_mbr_id.indexOf("/"));
			String div_code = searchAcId (sabun);

			input = "INSERT INTO pjt_grade_mgr(pid,keyname,owner,div_code) values('";
			input += pid+"','"+"PJT_PML"+"','"+pjt_mbr_id+"','"+div_code+"')";
			stmt.executeUpdate(input);
		}

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* �����⺻���� ���� �����ϱ� 
	*******************************************************************/
	public void updateGeneral(String pid,String pjt_code,String pjt_name,String owner,String in_date,String pjt_mbr_id,
		String pjt_class,String pjt_target,String mgt_plan,String parent_code,String mbr_exp,String cost_exp,
		String plan_start_date,String plan_end_date,String chg_start_date,String chg_end_date,String prs_code,
		String prs_type,String pjt_desc,String pjt_spec,String pjt_status,String plan_labor,String plan_sample,
		String plan_metal,String plan_mup,String plan_oversea,String plan_plant) throws Exception
	{
		String update = "",query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//���� �������� �˾ƺ���
		String current_status = "";
		query = "SELECT pjt_status FROM pjt_general where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) current_status = rs.getString("pjt_status");

		//���μ����ڵ� ���濩�� �˾ƺ���
		String current_pscode = "";
		query = "SELECT prs_code FROM pjt_general where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) current_pscode = rs.getString("prs_code");

		//PM ���濩�� �˾ƺ���
		String current_pm = "";
		query = "SELECT pjt_mbr_id FROM pjt_general where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) current_pm = rs.getString("pjt_mbr_id");

		//����1.���������°� ������ �ϰ�츸 ����
		//����2.�������̺��� �������δ� ���μ����ڵ�� PM���濩�θ� ������ ����
		if(current_status.equals("S")){
			//1.PM�� �ٲ� [�η������� : ���� -> �Է�]
			if(!(current_pm.equals(pjt_mbr_id)) && (current_pscode.equals(prs_code))) {
				deleteMember(pjt_code);				//�η»���
				inputMember(pid,pjt_code,pjt_name,plan_start_date,plan_end_date,pjt_mbr_id); //�η��Է�
				inputGradeMgr(pid,pjt_mbr_id);		//���Ѻο�[PM����]
			}
			//2.���μ����� �ٲ� [����/���⹰������ : ���� -> �Է�]
			else if((current_pm.equals(pjt_mbr_id)) && !(current_pscode.equals(prs_code))) {
				deleteSchedule(pjt_code);		//��������
				deleteDocument(pjt_code);		//���⹰����
				inputSchedule(pid,pjt_code,pjt_name,prs_code,prs_type);		//�����Է�
				inputDocument(pid,pjt_code,pjt_name,prs_code,prs_type);		//���⹰�Է�
			}
			//3.PM/���μ��� �Ѵ� �ٲ� [�η�/����/���⹰���� : ���� -> �Է�]
			else if(!(current_pm.equals(pjt_mbr_id)) && !(current_pscode.equals(prs_code))) {
				deleteMember(pjt_code);			//�η»���
				deleteSchedule(pjt_code);		//��������
				deleteDocument(pjt_code);		//���⹰����
				inputMember(pid,pjt_code,pjt_name,plan_start_date,plan_end_date,pjt_mbr_id); //�η��Է�
				inputSchedule(pid,pjt_code,pjt_name,prs_code,prs_type);		//�����Է�
				inputDocument(pid,pjt_code,pjt_name,prs_code,prs_type);		//���⹰�Է�
				inputGradeMgr(pid,pjt_mbr_id);		//���Ѻο�[PM����]
			}
		}

		//�����⺻���� �����ϱ� [pjt_general]
		update = "UPDATE pjt_general set pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"',owner='"+owner;
		update += "',in_date='"+in_date+"',pjt_mbr_id='"+pjt_mbr_id+"',pjt_class='"+pjt_class+"',pjt_target='"+pjt_target;
		update += "',mgt_plan='"+mgt_plan+"',parent_code='"+parent_code+"',mbr_exp='"+Integer.parseInt(mbr_exp)+"',cost_exp='"+Double.parseDouble(cost_exp);
		update += "',plan_start_date='"+plan_start_date+"',plan_end_date='"+plan_end_date+"',chg_start_date='"+chg_start_date+"',chg_end_date='"+chg_end_date;
		update += "',prs_code='"+prs_code+"',prs_type='"+prs_type+"',pjt_desc='"+pjt_desc+"',pjt_spec='"+pjt_spec;
		update += "',pjt_status='"+pjt_status+"',plan_labor='"+Double.parseDouble(plan_labor)+"',plan_sample='"+Double.parseDouble(plan_sample);
		update += "',plan_metal='"+Double.parseDouble(plan_metal)+"',plan_mup='"+Double.parseDouble(plan_mup);
		update += "',plan_oversea='"+Double.parseDouble(plan_oversea)+"',plan_plant='"+Double.parseDouble(plan_plant);
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//��������(��ȹ�Ⱓ) �Է��ϱ�
		update = "UPDATE pjt_schedule set plan_start_date='"+plan_start_date+"',";
		update += "plan_end_date='"+plan_end_date+"' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* �����⺻���� ���� �����ϱ� [�����Ǵ� ��� ���̺��� �����Ѵ�]
	*******************************************************************/
	public void deleteGeneral(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//1.�⺻���� ���̺� �����ϱ�
		delete = "DELETE from pjt_general where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//2.�η����� ���̺� �����ϱ�
		deleteMember(pjt_code);

		//3.�������� ���̺� �����ϱ�
		deleteSchedule(pjt_code);

		//4.���⹰���� ���̺� �����ϱ�
		deleteDocument(pjt_code);

		//5.������� ���̺� �����ϱ�
		deleteCost(pjt_code);

		//6.�������� ���̺� �����ϱ�
		deleteEvent(pjt_code);

		//7.��������(prs_project) ���� �ٲ��ֱ�
		String update = "UPDATE prs_project set pjt_status='S' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}
	/*******************************************************************
	* �����η����� ���� �����ϱ�
	*******************************************************************/
	public void deleteMember(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//���μ��� �̸����̺� �����ϱ�
		delete = "DELETE from pjt_member where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}
	/*******************************************************************
	* ������������ ���� �����ϱ�
	*******************************************************************/
	public void deleteSchedule(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//���μ��� �̸����̺� �����ϱ�
		delete = "DELETE from pjt_schedule where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}
	/*******************************************************************
	* �������⹰���� ���� �����ϱ�
	*******************************************************************/
	public void deleteDocument(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//���μ��� �̸����̺� �����ϱ�
		delete = "DELETE from pjt_document where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}
	/*******************************************************************
	* ����������� ���� �����ϱ�
	*******************************************************************/
	public void deleteCost(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//���μ��� �̸����̺� �����ϱ�
		delete = "DELETE from pjt_cost where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}
	/*******************************************************************
	* ������������ ���� �����ϱ�
	*******************************************************************/
	public void deleteEvent(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//���μ��� �̸����̺� �����ϱ�
		delete = "DELETE from pjt_event where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}

	//*******************************************************************
	//	�ش��� �μ��ڵ� �����ϱ�
	//*******************************************************************/	
	private String searchAcId (String login_id) throws Exception
	{
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT b.ac_code FROM user_table a,class_table b ";
		query += "where a.id ='"+login_id+"' and a.ac_id = b.ac_id";
		rs = stmt.executeQuery(query);
		if(rs.next())	rtn = rs.getString("ac_code");
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;
	}

	//*******************************************************************
	//	�ش��� �������� �����ϱ� [���,�̸�,��ȭ��ȣ,����,�μ���]
	//*******************************************************************/	
	private String[] searchManinfo (String login_id) throws Exception
	{
		String[] rtn = new String[5];		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//PM���� �˾ƺ���
		query  = "SELECT a.name,a.office_tel,c.ar_name,b.ac_name FROM user_table a,class_table b,rank_table c ";
		query += "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
		rs = stmt.executeQuery(query);
		if(rs.next())	{
			rtn[0] = login_id;						//���
			rtn[1] = rs.getString("name");			//�̸�
			rtn[2] = rs.getString("office_tel");	//��ȭ��ȣ
			rtn[3] = rs.getString("ar_name");		//���޸�
			rtn[4] = rs.getString("ac_name");		//�μ���
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;
	}
}


