package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class prsNameDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public prsNameDAO(Connection con) 
	{
		this.con = con;
	}

	public prsNameDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ� (�������)
	//*******************************************************************/
	public int getAllTotalCount(String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM prs_name where type='P'";
		query += " and ("+sItem+" like '%"+sWord+"%')"; 
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
	//*******************************************************************
	//	�� ���� �ľ��ϱ� (�μ�����)
	//*******************************************************************/
	public int getDivTotalCount(String div_code,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM prs_name where type='"+div_code+"'";
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
	// ������� LIST QUERY�ϱ� (��ü LIST�б�)
	//*******************************************************************/	
	public ArrayList getPrsnameAllList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//������� ���μ��� ������ �ִ��� �Ǵ��ϱ�
		String prs_mgr = "N";	//Y:����, N:����
		prs_mgr = checkPrsMgr(login_id);

		//�Ѱ��� ���ϱ�
		total_cnt = getAllTotalCount(sItem,sWord);
			
		//query���� �����
		query = "SELECT * FROM prs_name where type='P'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by prs_code asc"; 
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
				table = new prsCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);
				String prs_code = rs.getString("prs_code"); 
				table.setPrsCode(prs_code);							
				table.setPrsName(rs.getString("prs_name"));													
				table.setType(rs.getString("type"));
				
				//���μ����ڵ尡 �Ϻα���[process]���� ���Ǿ����� �˻��ϱ�
				String use = usePrsnameAtProcess(prs_code,"P");
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				}
				else {
					subMod = "<font color=darkred>ǥ�� ���μ����� �����Ǿ� �����Ұ�</font>";
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
	// �μ����� LIST QUERY�ϱ� (��ü LIST�б�)
	//*******************************************************************/	
	public ArrayList getPrsnameDivList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//login id�� �μ��ڵ� ã��
		String login_division = searchAcId (login_id);

		//�μ����� ���μ��� ������ �ִ��� �Ǵ��ϱ�
		String prs_mgr = "N";	//Y:����, N:����
		prs_mgr = checkPrsMgr(login_id,login_division);

		//�Ѱ��� ���ϱ�
		total_cnt = getDivTotalCount(login_division,sItem,sWord);
			
		//query���� �����
		query = "SELECT * FROM prs_name where type='"+login_division+"'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by prs_code asc"; 
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
				table = new prsCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);
				String prs_code = rs.getString("prs_code"); 
				table.setPrsCode(prs_code);							
				table.setPrsName(rs.getString("prs_name"));													
				table.setType(rs.getString("type"));
				
				//���μ����ڵ尡 �Ϻα���[process]���� ���Ǿ����� �˻��ϱ�
				String use = usePrsnameAtProcess(prs_code,login_division);
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				}
				else {
					subMod = "<font color=darkred>ǥ�� ���μ����� �����Ǿ� �����Ұ�</font>";
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
	//	���μ����̸��� ǥ�� ���μ���[prs_process]�� ����ߴ��� �Ǵ��ϱ�
	//*******************************************************************/	
	public String usePrsnameAtProcess (String prs_code,String type) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM prs_process ";
		query += "where parent_node = '"+prs_code+"' and type = '"+type+"'";
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
	//	������� ���μ��� �����ڿ��� �Ǵ��ϱ�
	//*******************************************************************/	
	public String checkPrsMgr (String login_id) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where keyname = 'PRS_MGR' and owner like '%"+login_id+"%'";
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
	//	�μ����� ���μ��� �����ڿ��� �Ǵ��ϱ�
	//*******************************************************************/	
	public String checkPrsMgr (String login_id,String login_div) throws Exception
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
	// �ش�phase QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	public ArrayList getPrsnameRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM prs_name where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new prsCodeTable();
					
				table.setPid(rs.getString("pid"));
				table.setPrsCode(rs.getString("prs_code"));							
				table.setPrsName(rs.getString("prs_name"));													
				table.setType(rs.getString("type"));							

				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* ���μ����̸� ���� �����ϱ� 
	*******************************************************************/
	public void inputPrsname(String pid,String prs_code,String prs_name,String type) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//���μ��� �̸����̺� �����ϱ�
		input = "INSERT INTO prs_name(pid,prs_code,prs_name,type) values('";
		input += pid+"','"+prs_code+"','"+prs_name+"','"+type+"')";
		stmt.executeUpdate(input);

		//���μ��� �������̺� �����ϱ�
		input = "INSERT INTO prs_process(pid,prs_code,parent_node,child_node,node_name,level_no,type) values('";
		input += pid+"','"+prs_code+"','0','"+prs_code+"','"+prs_name+"','0','"+type+"')";
		stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*******************************************************************
	* ���μ����̸� ���� �����ϱ� 
	*******************************************************************/
	public boolean updatePrsname(String pid,String prs_code,String prs_name,String type) throws Exception
	{
		boolean rtn = false;
		String update = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		prs_code = prs_code.toUpperCase();		//�빮�ڷ� �ٲٱ�

		//���μ����ڵ尡 �ߺ��Ǵ��� �˻��ϱ�
		int code_cnt = 0;
		String query = "SELECT COUNT(*) FROM prs_name where prs_code='"+prs_code+"' and type ='"+type+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) code_cnt = rs.getInt(1);

		//�ڵ�� �̸��� �ٲ��ش�.
		if(code_cnt == 0) {
			//���μ��� �̸����̺� �����ϱ�
			update = "UPDATE prs_name set prs_code='"+prs_code+"',prs_name='"+prs_name+"',type='"+type;
			update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);

			//���μ��� �������̺� �����ϱ�
			update = "UPDATE prs_process set prs_code='"+prs_code+"',child_node='"+prs_code+"',node_name='"+prs_name;
			update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			rtn = true;
		}
		//�̸��� �ٲ��ش�.
		else if(code_cnt == 1) {
			//���μ��� �̸����̺� �����ϱ�
			update = "UPDATE prs_name set prs_name='"+prs_name+"',type='"+type;
			update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);

			//���μ��� �������̺� �����ϱ�
			update = "UPDATE prs_process set child_node='"+prs_code+"',node_name='"+prs_name;
			update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			rtn = true;
		}
		stmt.close();
		rs.close();
		return rtn;
	}

	/*******************************************************************
	* ���μ����̸� ���� �����ϱ� 
	*******************************************************************/
	public boolean deletePrsname(String pid) throws Exception
	{
		boolean rtn = false;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		String delete = "";

		stmt = con.createStatement();

		//���μ��� �̸����̺��� ���μ��� �ڵ� ���ϱ�
		String prs_code = "";
		query = "SELECT prs_code FROM prs_name where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) prs_code = rs.getString("prs_code");
		
		//���μ��� �������̺� �����Ǿ����� �Ǵ��Ͽ�[���ڵ�����] �������� ����
		query = "SELECT COUNT(*) FROM prs_process where prs_code='"+prs_code+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		//�������� �����ϱ�
		if(cnt < 2) {
			//���μ��� �̸����̺� �����ϱ�
			delete = "DELETE from prs_name where pid='"+pid+"'";
			stmt.executeUpdate(delete);

			//���μ��� �������̺� �����ϱ�
			delete = "DELETE from prs_process where pid='"+pid+"'";
			stmt.executeUpdate(delete);
			
			rtn = true;
		} 

		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// PRSNAME CODE���ϱ�
	// type : A:����, D:�μ� 
	//*******************************************************************/	
	public String getPrsnameCode (String login_id,String div_code,String tag) throws Exception
	{
		//���� �ʱ�ȭ
		String rtn = "";			//���ϵ�����
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//����ã��
		String prs_mgr = "N";
		if(tag.equals("A")) prs_mgr = checkPrsMgr (login_id);					//�������
		else if(tag.equals("D")) prs_mgr = checkPrsMgr(login_id,div_code);		//�μ�����

		//�ű����� �ƴ����� �Ǵ��ϱ����� ������ ã�´�.
		int cnt = 0;
		if(tag.equals("A") && prs_mgr.equals("Y")) {
			cnt = getAllTotalCount("prs_code","");
			if(cnt == 0) { rtn = "P001"; return rtn; }
		}
		else if(tag.equals("D") && prs_mgr.equals("Y")) {
			cnt = getDivTotalCount(div_code,"prs_code","");
			if(cnt == 0) { rtn = "D001"; return rtn; }
		}

		//query���� �����
		stmt = con.createStatement();
		if(tag.equals("A") && prs_mgr.equals("Y"))
			query = "SELECT prs_code FROM prs_name where type='P' order by prs_code desc";		
		else if(tag.equals("D") && prs_mgr.equals("Y"))
			query = "SELECT prs_code FROM prs_name where type='"+div_code+"' order by prs_code desc";	
		else return rtn;

		//������ ���
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getString("prs_code");

		//ph_code�� +1
		if(rtn.length() > 0) {
			com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("000");
			String no = nft.toDigits(Integer.parseInt(rtn.substring(1,4))+1);
			if(tag.equals("A")) rtn = "P"+no;
			else if(tag.equals("D")) rtn = "D"+no;
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
