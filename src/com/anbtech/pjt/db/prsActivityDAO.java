package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class prsActivityDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public prsActivityDAO(Connection con) 
	{
		this.con = con;
	}

	public prsActivityDAO() 
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

		query = "SELECT COUNT(*) FROM prs_activity where type='P'";
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

		query = "SELECT COUNT(*) FROM prs_activity where type='"+div_code+"'";
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
	public ArrayList getActivityAllList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		query = "SELECT a.pid,a.ph_code,c.ph_name,a.step_code,b.step_name,a.act_code,a.act_name,a.type ";
		query += "FROM prs_activity a,prs_step b,prs_phase c ";
		query += "where a.type='P' and a.ph_code=c.ph_code and a.step_code=b.step_code and b.ph_code=c.ph_code";	
		query += " and (a."+sItem+" like '%"+sWord+"%') order by a.ph_code,a.step_code,a.act_code asc"; 
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
		String pcd="",pnm="",scd="",snm="";
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new prsCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String ph_code = rs.getString("ph_code");
				if(ph_code.equals(pcd)) table.setPhCode("");
				else { pcd = ph_code;   table.setPhCode(ph_code);}

				String ph_name = rs.getString("ph_name");
				if(ph_name.equals(pnm)) table.setPhName("");
				else { pnm = ph_name;   table.setPhName(ph_name);}

				String step_code = rs.getString("step_code");
				if(step_code.equals(scd)) table.setStepCode("");
				else { scd = step_code;   table.setStepCode(step_code);}

				String step_name = rs.getString("step_name");
				if(step_name.equals(snm)) table.setStepName("");
				else { snm = step_name;   table.setStepName(step_name);}
				
				String act_code = rs.getString("act_code");
				table.setActCode(act_code);
				table.setActName(rs.getString("act_name"));
				table.setType(rs.getString("type"));	
				
				//activity�ڵ尡 �Ϻα���[process]���� ���Ǿ����� �˻��ϱ�
				String use = useActivityAtProcess(act_code,"P");
					
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				}
				else {
					subMod = "<font color=darkred>�����Ұ�</font>";
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
	public ArrayList getActivityDivList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		query = "SELECT a.pid,a.ph_code,c.ph_name,a.step_code,b.step_name,a.act_code,a.act_name,a.type ";
		query += "FROM prs_activity a,prs_step b,prs_phase c ";
		query += "where a.type='"+login_division+"' and a.ph_code=c.ph_code and a.step_code=b.step_code and b.ph_code=c.ph_code";	
		query += " and (a."+sItem+" like '%"+sWord+"%') order by a.ph_code,a.step_code,a.act_code asc"; 
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
		String pcd="",pnm="",scd="",snm="";
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new prsCodeTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String ph_code = rs.getString("ph_code");
				if(ph_code.equals(pcd)) table.setPhCode("");
				else { pcd = ph_code;   table.setPhCode(ph_code);}

				String ph_name = rs.getString("ph_name");
				if(ph_name.equals(pnm)) table.setPhName("");
				else { pnm = ph_name;   table.setPhName(ph_name);}

				String step_code = rs.getString("step_code");
				if(step_code.equals(scd)) table.setStepCode("");
				else { scd = step_code;   table.setStepCode(step_code);}

				String step_name = rs.getString("step_name");
				if(step_name.equals(snm)) table.setStepName("");
				else { snm = step_name;   table.setStepName(step_name);}
				
				String act_code = rs.getString("act_code");
				table.setActCode(act_code);
				table.setActName(rs.getString("act_name"));
				table.setType(rs.getString("type"));	
				
				//activity�ڵ尡 �Ϻα���[process]���� ���Ǿ����� �˻��ϱ�
				String use = useActivityAtProcess(act_code,login_division);
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				}
				else {
					subMod = "<font color=darkred>�����Ұ�</font>";
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
	//	Activity�� ǥ�� ���μ���[prs_process]�� ����ߴ��� �Ǵ��ϱ�
	//*******************************************************************/	
	public String useActivityAtProcess (String act_code,String type) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM prs_process ";
		query += "where child_node = '"+act_code+"' and type = '"+type+"'";
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
	// �ش�activity QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	public ArrayList getActivityRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT a.pid,a.ph_code,c.ph_name,a.step_code,b.step_name,a.act_code,a.act_name,a.type ";
		query += "FROM prs_activity a,prs_step b,prs_phase c ";
		query += "where a.pid='"+pid+"' and a.ph_code=c.ph_code and a.step_code=b.step_code and b.ph_code=c.ph_code";		
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new prsCodeTable();
					
				table.setPid(rs.getString("pid"));
				table.setPhCode(rs.getString("ph_code"));	
				table.setPhName(rs.getString("ph_name"));
				table.setStepCode(rs.getString("step_code"));	
				table.setStepName(rs.getString("step_name"));	
				table.setActCode(rs.getString("act_code"));	
				table.setActName(rs.getString("act_name"));
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
	public void inputActivity(String pid,String ph_code,String step_code,String act_code,String act_name,String type) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String input = "INSERT INTO prs_activity(pid,ph_code,step_code,act_code,act_name,type) values('";
			input += pid+"','"+ph_code+"','"+step_code+"','"+act_code+"','"+act_name+"','"+type+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*******************************************************************
	* PHASE ���� �����ϱ� 
	*******************************************************************/
	public void updateActivity(String pid,String ph_code,String step_code,String act_code,String act_name,String type) throws Exception
	{

		Statement stmt = null;
		stmt = con.createStatement();
		String update = "UPDATE prs_activity set ph_code='"+ph_code+"',step_code='"+step_code+"',act_code='"+act_code;
			update += "',act_name='"+act_name+"', type='"+type+"' where pid='"+pid+"'";
		//System.out.println("update : " + update );
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}

	/*******************************************************************
	* PHASE ���� �����ϱ� 
	*******************************************************************/
	public void deleteActivity(String pid) throws Exception
	{

		Statement stmt = null;
		stmt = con.createStatement();
		String delete = "DELETE from prs_activity where pid='"+pid+"'";
		//System.out.println("delete : " + delete );
		int er = stmt.executeUpdate(delete);
		
		stmt.close();
	}

	//*******************************************************************
	// ACTIVITY CODE���ϱ�
	// type : A:����, D:�μ� 
	//*******************************************************************/	
	public String getActivityCode (String login_id,String div_code,String step_code,String tag) throws Exception
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
			if(cnt == 0) { rtn = "N01"; return rtn; }
		}
		else if(tag.equals("D") && prs_mgr.equals("Y")) {
			cnt = getDivTotalCount(div_code,"ph_code","");
			if(cnt == 0) { rtn = "N01"; return rtn; }
		}

		//query���� �����
		stmt = con.createStatement();
		if(tag.equals("A") && prs_mgr.equals("Y"))
			query = "SELECT act_code FROM prs_activity where type='P' and step_code='"+step_code+"' order by act_code desc";		
		else if(tag.equals("D") && prs_mgr.equals("Y"))
			query = "SELECT act_code FROM prs_activity where type='"+div_code+"' and step_code='"+step_code+"' order by act_code desc";	
		else return rtn;

		//������ ���
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getString("act_code");
	
		//ph_code�� +1
		if(rtn.length() > 0) {
			com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("0000");
			String no = nft.toDigits(Integer.parseInt(rtn.substring(1,5))+1);
			rtn = "N"+no;
		}
		
		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// Activity CODE ����� �Ϸù�ȣ�� �ٽ� ä���ϱ�
	// ������� : �߰�����,�߰�
	//*******************************************************************/	
	public void updateActivityCode (String login_id,String type,String step_code,String tag) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//����ã��
		String prs_mgr = "N";
		if(tag.equals("A")) prs_mgr = checkPrsMgr (login_id);					//�������
		else if(tag.equals("D")) prs_mgr = checkPrsMgr(login_id,type);		//�μ�����

		//�ű����� �ƴ����� �Ǵ��ϱ����� ������ ã�´�.
		int cnt = 0;
		if(tag.equals("A") && prs_mgr.equals("Y")) {
			cnt = getAllTotalCount("act_code","");
			if(cnt == 0) return;
		}
		else if(tag.equals("D") && prs_mgr.equals("Y")) {
			cnt = getDivTotalCount(type,"act_code","");
			if(cnt == 0) return;
		}

		//query���� �����
		stmt = con.createStatement();
		if(tag.equals("A") && prs_mgr.equals("Y"))
			query = "SELECT * FROM prs_activity where type='P' and step_code='"+step_code+"' order by ph_code,step_code,act_code asc";		
		else if(tag.equals("D") && prs_mgr.equals("Y"))
			query = "SELECT * FROM prs_activity where type='"+type+"' and step_code='"+step_code+"' order by ph_code,step_code,act_code asc";	
		else return;

		//������ ���
		String[][] data = new String[cnt][2];
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) {
			data[n][0] = rs.getString("pid");
			data[n][1] = rs.getString("act_code");
			n++;
		}
	
		//act_code�� +1
		com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("00");
		String no = "",update="",NH="";
		for(int i=0,j=1; i<n; i++,j++) {
			if(data[i][1].length() == 5) NH = data[i][1].substring(0,3);	//��,N0100..
			else NH = data[i][1].substring(0,4);		//step code�� �������� ���ĺ��� �ִ°�� ��,N01A00..
			no = NH+nft.toDigits(j);
			update = "update prs_activity set act_code='"+no+"' where pid='"+data[i][0]+"'";
			stmt.executeUpdate(update);		
		}
		
		stmt.close();
		rs.close();
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

