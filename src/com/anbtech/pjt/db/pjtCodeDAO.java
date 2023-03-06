package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtCodeDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public pjtCodeDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtCodeDAO() 
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

		query = "SELECT COUNT(*) FROM prs_project where type='P'";
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

		query = "SELECT COUNT(*) FROM prs_project where type='"+div_code+"'";
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
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//�Ѱ��� ���ϱ�
		total_cnt = getAllTotalCount(sItem,sWord);
			
		//query���� �����
		query = "SELECT * FROM prs_project where type='P'";	
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
				table = new pjtCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(status.equals("S")) {				//������ ����
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				} else if(status.equals("3")) {		//���� DROP����
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
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
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//login id�� �μ��ڵ� ã��
		String login_division = searchAcId (login_id);

		//�Ѱ��� ���ϱ�
		total_cnt = getDivTotalCount(login_division,sItem,sWord);
			
		//query���� �����
		query = "SELECT * FROM prs_project where type='"+login_division+"'";	
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
				table = new pjtCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setInDate(rs.getString("in_date"));
				table.setMgrId(rs.getString("mgr_id"));
				table.setMgrName(rs.getString("mgr_name"));
				table.setType(rs.getString("type"));	
				String status = rs.getString("pjt_status");
				table.setPjtStatus(status);
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(status.equals("S")) {				//������ ����
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				} else if(status.equals("3")) {		//���� DROP����
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
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
	// ��ü ������ ���� LIST : �������� ��Ͻ� �ʿ�
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
		query = "SELECT * FROM prs_project where pjt_status='S' order by pjt_code asc";	
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
	//	�μ��� �������� �����ڿ��� �Ǵ��ϱ�
	//*******************************************************************/	
	public String checkPjtMgr (String login_id,String login_div) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where keyname = 'PJT_PML' and owner like '%"+login_id+"%' and div_code ='"+login_div+"'";
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
	public ArrayList getProjectRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		pjtCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM prs_project where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
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

	/*******************************************************************
	* �����ڵ� ���� �����ϱ� 
	*******************************************************************/
	public void inputProject(String pid,String pjt_code,String pjt_name,String in_date,String mgr_id,String mgr_name,
		String type,String pjt_status) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//�����ڵ� ���̺� �����ϱ� : prs_project
		input = "INSERT INTO prs_project(pid,pjt_code,pjt_name,in_date,mgr_id,mgr_name,type,pjt_status) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+in_date+"','"+mgr_id+"','"+mgr_name+"','"+type+"','"+pjt_status+"')";
		stmt.executeUpdate(input);

		//�������� ���̺� �����ϱ� : pjt_status
		input = "INSERT INTO pjt_status(pid,pjt_code,pjt_name,type,pjt_status) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+type+"','"+pjt_status+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}

	/*******************************************************************
	* �����ڵ� ���� �����ϱ� 
	*******************************************************************/
	public void updateProject(String pid,String pjt_code,String pjt_name,String in_date,String mgr_id,String mgr_name,
		String type,String pjt_status) throws Exception
	{
		String update = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//���� �������� �˾ƺ���
		String current_status = "";
		String query = "SELECT pjt_status FROM prs_project where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) current_status = rs.getString("pjt_status");

		//���������� �������̺��� ������ �������� �Ǵ��ϱ� [��:������ ������ ���������̺� ���������]
		if(!current_status.equals("S")) {
			//�⺻����
			update = "UPDATE pjt_general set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//�η�����
			update = "UPDATE pjt_member set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//��������
			update = "UPDATE pjt_schedule set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//��������
			update = "UPDATE pjt_event set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//�������
			update = "UPDATE pjt_cost set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			//���⹰����
			update = "UPDATE pjt_document set pjt_name='"+pjt_name+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
		}
				
		//���μ��� �̸����̺� �����ϱ� [prs_project]
		update = "UPDATE prs_project set pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"',in_date='"+in_date;
		update += "',mgr_id='"+mgr_id+"',mgr_name='"+mgr_name+"',type='"+type+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//�������� �̸����̺� �����ϱ� [pjt_status]
		update = "UPDATE pjt_status set pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"',type='"+type;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* �����ڵ� ���� �����ϱ� 
	*******************************************************************/
	public void deleteProject(String pid) throws Exception
	{
		boolean rtn = false;
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//���μ��� �̸����̺� �����ϱ� : prs_project
		delete = "DELETE from prs_project where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		//�������� �̸����̺� �����ϱ� : pjt_status
		delete = "DELETE from pjt_status where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}

	//*******************************************************************
	// �������� CODE���ϱ� [�μ��ڵ�+�⵵(YYYY)+"-"+�Ϸù�ȣ(3)
	// tag = A:�������, D:�μ�����
	//*******************************************************************/	
	public String getProjectCode (String div_code,String tag) throws Exception
	{
		//���� �ʱ�ȭ
		String rtn = "";			//���ϵ�����
		String pjhd = "";			//�����ڵ� ���� ���� ����
		String pjmd = "";			//�����ڵ� �߰� ���� ����
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//�����ڵ� ���� ���� ����
		if(tag.equals("A")) pjhd = "PJ";
		else pjhd = div_code;

		//�����ڵ� �߰� ���� ����
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		pjmd = anbdt.getYear();

		//�ű����� �ƴ����� �Ǵ��ϱ����� ������ ã�´�.
		int cnt = 0;
		if(tag.equals("A")) {
			cnt = getDivTotalCount("P","pjt_code","");
			if(cnt == 0) { rtn = pjhd+pjmd+"-"+"001"; return rtn; }
		} else { 
			cnt = getDivTotalCount(div_code,"pjt_code","");
			if(cnt == 0) { rtn = pjhd+pjmd+"-"+"001"; return rtn; }
		}
		
		//query���� �����
		stmt = con.createStatement();
		if(tag.equals("A"))
			query = "SELECT pjt_code FROM prs_project where type='P' order by pjt_code desc";	
		else 
			query = "SELECT pjt_code FROM prs_project where type='"+div_code+"' order by pjt_code desc";	
		
		//������ ���
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getString("pjt_code");

		//ph_code�� +1
		if(rtn.length() > 0) {
			com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("000");
			String no = nft.toDigits(Integer.parseInt(rtn.substring(rtn.lastIndexOf("-")+1,rtn.length()))+1);
			rtn = pjhd+pjmd+"-"+no;
		}
		
		stmt.close();
		rs.close();
		return rtn;
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

