package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class projectStatusDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public projectStatusDAO(Connection con) 
	{
		this.con = con;
	}

	public projectStatusDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ� (��ü)
	//*******************************************************************/
	public int getAllTotalCount(String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_status where type='P'";
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
	public int getDivTotalCount(String div_code,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_status where type='"+div_code+"'";
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
	public ArrayList getAllProjectList (String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		total_cnt = getAllTotalCount(sItem,sWord);
			
		//query���� �����
		query = "SELECT * FROM pjt_status where type='P'";	
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
				String in_date = rs.getString("in_date"); if(in_date == null) in_date = "";
				table.setInDate(in_date);	
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				table.setNote(rs.getString("note"));
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="",subView="";
				if(status.equals("3")) {		//���� DROP����
					subMod = "<a href=\"javascript:pjtChgStatus('"+pjt_code+"');\">[���º���]</a>";
					subDel = "<a href=\"javascript:pjtDelete('"+pjt_code+"');\">[����]</a>";
					subView = "<a href=\"javascript:pjtView('"+pjt_code+"');\">[����]</a>";
				} else {
					subMod = "<a href=\"javascript:pjtChgStatus('"+pjt_code+"');\">[���º���]</a>";
					subView = "<a href=\"javascript:pjtView('"+pjt_code+"');\">[����]</a>";
				}
				table.setModify(subMod);
				table.setDelete(subDel);
				table.setView(subView);
				
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
	public ArrayList getDivProjectList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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

		//login id�� �μ��ڵ� ã��
		String login_division = searchAcId (login_id);

		//�Ѱ��� ���ϱ�
		total_cnt = getDivTotalCount(login_division,sItem,sWord);
			
		//query���� �����
		query = "SELECT * FROM pjt_status where type='"+login_division+"'";	
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
				String in_date = rs.getString("in_date"); if(in_date == null) in_date = "";
				table.setInDate(in_date);		
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				table.setNote(rs.getString("note"));
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="",subView="";
				if(status.equals("3")) {		//���� DROP����
					subMod = "<a href=\"javascript:pjtChgStatus('"+pjt_code+"');\">[���º���]</a>";
					subDel = "<a href=\"javascript:pjtDelete('"+pjt_code+"');\">[����]</a>";
					subView = "<a href=\"javascript:pjtView('"+pjt_code+"');\">[����]</a>";
				} else {
					subMod = "<a href=\"javascript:pjtChgStatus('"+pjt_code+"');\">[���º���]</a>";
					subView = "<a href=\"javascript:pjtView('"+pjt_code+"');\">[����]</a>";
				}
				table.setModify(subMod);
				table.setDelete(subDel);
				table.setView(subView);
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ��ü ������ ���� LIST : �������� ��Ͻ� �ʿ�
	//*******************************************************************/	
	public ArrayList getAllStandbyProjectList () throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query���� �����
		query = "SELECT * FROM pjt_status where pjt_status='S' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				table.setNote(rs.getString("note"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �μ��� ������ ���� LIST : �������� ��Ͻ� �ʿ�
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
		projectTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query���� �����
		query = "SELECT * FROM pjt_status where pjt_status='S' and type='"+login_division+"' order by pjt_code asc";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));
				table.setNote(rs.getString("note"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش���� QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	public ArrayList getProjectRead (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_status where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new projectTable();
					
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				table.setPjtStatus(rs.getString("pjt_status"));		
				table.setNote(rs.getString("note"));

				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* �������� �����ϱ� 
	*******************************************************************/
	public void updateProject(String pjt_code,String pjt_status,String note,String in_date) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//�⺻���� : pjt_general
		update = "UPDATE pjt_general set pjt_status='"+pjt_status+"' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);
				
		//���μ��� �̸����̺� �����ϱ� : prs_project
		update = "UPDATE prs_project set pjt_status='"+pjt_status+"' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);

		//�������� �̸����̺� �����ϱ� : pjt_status
		update = "UPDATE pjt_status set pjt_status='"+pjt_status+"',note='"+note+"',in_date='"+in_date+"' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*******************************************************************
	* DROP���� �����ϱ� 
	*******************************************************************/
	public void deleteProject(String pjt_code) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//���������� : prs_project
		delete = "DELETE from prs_project where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//������������ : pjt_status
		delete = "DELETE from pjt_status where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//�⺻���� : pjt_general
		delete = "DELETE from pjt_general where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//�η����� : pjt_member
		delete = "DELETE from pjt_member where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//�������� : pjt_schedule
		delete = "DELETE from pjt_schedule where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//������������ : pjt_changesch
		delete = "DELETE from pjt_changesch where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//������ ���� : pjt_note
		delete = "DELETE from pjt_note where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//�̽� ���� : pjt_issue
		delete = "DELETE from pjt_issue where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//������ : pjt_cost
		delete = "DELETE from pjt_cost where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(delete);

		//���⹰ ���� : pjt_document
		delete = "DELETE from pjt_document where pjt_code='"+pjt_code+"'";
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
}

