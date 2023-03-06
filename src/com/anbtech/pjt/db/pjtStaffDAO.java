package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtStaffDAO
{
	private Connection con;

	private String[][] item = null;				//���μ��������� �迭�� ���
	private int an = 0;							//items�� �迭 ����
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public pjtStaffDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtStaffDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ� (��ü) [�ڽ��� ���Ե� ���� �Ѱ���] : ������ȸ�� ���
	//*******************************************************************/
	private int getAllTotalCount(String login_id,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_general a,pjt_member b where a.pjt_code = b.pjt_code ";
		query += " and b.pjt_mbr_id = '"+login_id+"'";
		query += " and (a."+sItem+" like '%"+sWord+"%')"; 
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ� (��ü) [�ڽ��� ���Ե� ������ �������� ���� �Ѱ���] : ���������
	//*******************************************************************/
	private int getPrsTotalCount(String login_id,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_general a,pjt_member b where a.pjt_code = b.pjt_code ";
		query += " and b.pjt_mbr_id = '"+login_id+"' and pjt_status = '1'";
		query += " and (a."+sItem+" like '%"+sWord+"%')"; 
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
	// ���� LIST [�ڽ��� ���Ե� ���� ��üLIST] : ������ȸ�� ��� 
	//*******************************************************************/	
	public ArrayList getAllGeneralList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		
		//�Ѱ��� ���ϱ�
		total_cnt = getAllTotalCount(login_id,sItem,sWord);
			
		//query���� �����
		query = "SELECT a.* FROM pjt_general a,pjt_member b where a.pjt_code = b.pjt_code ";
		query += " and b.pjt_mbr_id = '"+login_id+"'";
		query += " and (a."+sItem+" like '%"+sWord+"%') order by a.pjt_code desc"; 
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

				String pjt_name = rs.getString("pjt_name");
				String pname = "<a href=\"javascript:pjtView('"+pjt_code+"');\">"+pjt_name+"</a>";
				table.setPjtName(pname);	

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
				table.setPjtStatus(rs.getString("pjt_status"));
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
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ���� LIST [�ڽ��� ���Ե� ������ �������� ����] : ��� ����� ���������
	//*******************************************************************/	
	public ArrayList getPrsGeneralList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		
		//�Ѱ��� ���ϱ�
		total_cnt = getAllTotalCount(login_id,sItem,sWord);
			
		//query���� �����
		query = "SELECT a.* FROM pjt_general a,pjt_member b where a.pjt_code = b.pjt_code ";
		query += " and b.pjt_mbr_id = '"+login_id+"' and pjt_status = '1'";
		query += " and (a."+sItem+" like '%"+sWord+"%') order by a.pjt_code desc"; 
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

				String pjt_name = rs.getString("pjt_name");
				String pname = "<a href=\"javascript:pjtView('"+pjt_code+"');\">"+pjt_name+"</a>";
				table.setPjtName(pname);	

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
				table.setPjtStatus(rs.getString("pjt_status"));
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
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	/**********************************************************************
	 * �ش������ �ڽ��� ���Ե� ��常 �迭�� ��´�. : ������� ���������
	 *********************************************************************/
	private void saveItemsArray(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//���� �ʱ�ȭ
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

			no = Integer.parseInt(item[an-1][5]);				//String�� ������ �ٲٱ�
			lno = Integer.toString(no+1);						//+1�Ͽ� ������ String���� �ٲٱ� 
			saveItemsArray(pjt_code,lno,item[an-1][4]);
		}
		rs.close();
		stmt.close(); 
		
	} //saveItemsArray

	//*******************************************************************
	//	�ش� �����ڵ��� �� ���� �ľ��ϱ� : ������� ���������
	//*******************************************************************/
	private int getAllTotalCount(String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
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
	 * �ش������ ����׸��� �迭���� ArrayList�� ��´� : ������� ���������
	 *********************************************************************/
	public ArrayList getPjtSchedule(String pjt_code,String level_no,String parent_node,String login_id) throws Exception
	{
		//1.�迭�����
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][23];

		//2. �迭�� ��´�.
		saveItemsArray(pjt_code,level_no,parent_node);

		//3.ArrayList�� ���
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		String node_name="",user_id="",user_name="";
		user_id = getAllProjectMember(pjt_code);		//�ش�project�� ��ü���
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
				
			//user_id = item[i][7]+":"+item[i][9];		//������� ���� �ְ�	
			if(item[i][5].equals("3") && (user_id.indexOf(login_id) != -1)) {	//pjt_cod,parent_node,child_node
				node_name = "<a href=\"javascript:nodeView('"+item[i][1]+"','"+item[i][4]+"');\">"+item[i][4]+" "+item[i][6]+"</a>";
				//user_id = "";							//������� ���� �ְ� �� ���
			} else { node_name = item[i][4]+" "+item[i][6]; }
			table.setNodeName(node_name);
			table.setWeight(Double.parseDouble(item[i][7]));
			table.setUserId(item[i][8]);

			if(item[i][8].equals(login_id)) {			//�����
				user_name = "<font color=blue><B>"+item[i][9]+"</B></font>";
				table.setUserName(user_name);
			} else {
				table.setUserName(item[i][9]);
			}

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

	//*******************************************************************
	// �ش�����ڵ��� ��� ��ü��� ã��
	//*******************************************************************/	
	public String getAllProjectMember (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		String member = "";
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;

		//query���� �����
		query = "SELECT pjt_mbr_id FROM pjt_member where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);
		//������ ���
		while(rs.next()) { 			
			member += rs.getString("pjt_mbr_id")+":";
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return member;
	}
}


