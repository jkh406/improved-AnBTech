package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class prsPhaseDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public prsPhaseDAO(Connection con) 
	{
		this.con = con;
	}

	public prsPhaseDAO() 
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

		query = "SELECT COUNT(*) FROM prs_phase where type='P'";
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

		query = "SELECT COUNT(*) FROM prs_phase where type='"+div_code+"'";
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
	public ArrayList getPhaseAllList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		query = "SELECT * FROM prs_phase where type='P'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by ph_code asc"; 
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
				String ph_code = rs.getString("ph_code");
				table.setPhCode(ph_code);							
				table.setPhName(rs.getString("ph_name"));													
				table.setType(rs.getString("type"));		
				
				//phase�ڵ尡 �Ϻα���[step]���� ���Ǿ����� �˻��ϱ�
				String use = usePhaseAtStep(ph_code,"P");

				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				}
				else {
					subMod = "<font color=darkred>STEP�ܰ迡�� �����Ǿ� �����Ұ�</font>";
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
	public ArrayList getPhaseDivList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		query = "SELECT * FROM prs_phase where type='"+login_division+"'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by ph_code asc"; 
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
				String ph_code = rs.getString("ph_code");
				table.setPhCode(ph_code);							
				table.setPhName(rs.getString("ph_name"));													
				table.setType(rs.getString("type"));	
				
				//phase�ڵ尡 �Ϻα���[step]���� ���Ǿ����� �˻��ϱ�
				String use = usePhaseAtStep(ph_code,login_division);
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				}
				else {
					subMod = "<font color=darkred>STEP�ܰ迡�� �����Ǿ� �����Ұ�</font>";
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
	//	PHASE�� �Ϻα���[STEP]�� ����ߴ��� �Ǵ��ϱ�
	//*******************************************************************/	
	public String usePhaseAtStep (String ph_code,String type) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM prs_step ";
		query += "where ph_code = '"+ph_code+"' and type = '"+type+"'";
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
	public ArrayList getPhaseRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM prs_phase where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new prsCodeTable();
					
				table.setPid(rs.getString("pid"));
				table.setPhCode(rs.getString("ph_code"));							
				table.setPhName(rs.getString("ph_name"));													
				table.setType(rs.getString("type"));							

				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* PHASE ���� �����ϱ� 
	*******************************************************************/
	public void inputPhase(String pid,String ph_code,String ph_name,String type) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String input = "INSERT INTO prs_phase(pid,ph_code,ph_name,type) values('";
			input += pid+"','"+ph_code+"','"+ph_name+"','"+type+"')";
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*******************************************************************
	* PHASE ���� �����ϱ� 
	*******************************************************************/
	public boolean updatePhase(String pid,String ph_code,String ph_name,String type) throws Exception
	{
		boolean rtn = false;
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		ph_code = ph_code.toUpperCase();	//�빮�ڷ� �ٲٱ�

		//���μ����ڵ尡 �ߺ��Ǵ��� �˻��ϱ�
		String query = "SELECT COUNT(*) FROM prs_phase where ph_code='"+ph_code+"' and type ='"+type+"'";
		query += " and pid != '"+pid+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		//�����ϱ�
		if(cnt == 0) {
			String update = "UPDATE prs_phase set ph_code='"+ph_code+"',ph_name='"+ph_name+"',type='"+type;
				update += "' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			rtn = true;
		}
		
		stmt.close();
		rs.close();
		return rtn;
	}

	/*******************************************************************
	* PHASE ���� �����ϱ� 
	*******************************************************************/
	public boolean deletePhase(String pid) throws Exception
	{
		boolean rtn = false;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		String delete = "";

		stmt = con.createStatement();

		//���μ��� �̸����̺��� ���μ��� �ڵ� ���ϱ�
		String ph_code = "",type="";
		query = "SELECT ph_code,type FROM prs_phase where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			ph_code = rs.getString("ph_code");
			type = rs.getString("type");
		}
		
		//���μ��� �������̺� �����Ǿ����� �Ǵ��Ͽ�[���ڵ�����] �������� ����
		query = "SELECT COUNT(*) FROM prs_step where ph_code='"+ph_code+"' and type='"+type+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		//�������� �����ϱ�
		if(cnt < 2) {
			delete = "DELETE from prs_phase where pid='"+pid+"'";
			stmt.executeUpdate(delete);
			rtn = true;
		} 

		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// PHASE CODE���ϱ�
	// type : A:����, D:�μ� 
	//*******************************************************************/	
	public String getPhaseCode (String login_id,String div_code,String tag) throws Exception
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
			cnt = getAllTotalCount("ph_code","");
			if(cnt == 0) { rtn = "S01"; return rtn; }
		}
		else if(tag.equals("D") && prs_mgr.equals("Y")) {
			cnt = getDivTotalCount(div_code,"ph_code","");
			if(cnt == 0) { rtn = "D01"; return rtn; }
		}

		//query���� �����
		stmt = con.createStatement();
		if(tag.equals("A") && prs_mgr.equals("Y"))
			query = "SELECT ph_code FROM prs_phase where type='P' order by ph_code desc";		
		else if(tag.equals("D") && prs_mgr.equals("Y"))
			query = "SELECT ph_code FROM prs_phase where type='"+div_code+"' order by ph_code desc";	
		else return rtn;

		//������ ���
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getString("ph_code");
	
		//ph_code�� +1
		if(rtn.length() > 0) {
			com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("00");
			String no = nft.toDigits(Integer.parseInt(rtn.substring(1,3))+1);
			if(tag.equals("A")) rtn = "S"+no;
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
