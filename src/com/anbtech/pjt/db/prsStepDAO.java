package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class prsStepDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public prsStepDAO(Connection con) 
	{
		this.con = con;
	}

	public prsStepDAO() 
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

		query = "SELECT COUNT(*) FROM prs_step where type='P'";
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

		query = "SELECT COUNT(*) FROM prs_step where type='"+div_code+"'";
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
	public ArrayList getStepAllList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		query = "SELECT a.pid,a.ph_code,b.ph_name,a.step_code,a.step_name,a.type ";
		query += "FROM prs_step a,prs_phase b where a.type='P' and a.ph_code=b.ph_code";	
		query += " and (a."+sItem+" like '%"+sWord+"%') order by a.ph_code,a.step_code asc"; 
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
		String pcd = "",pnm="";
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
				table.setStepCode(step_code);	
				table.setStepName(rs.getString("step_name"));
				table.setType(rs.getString("type"));	
				
				//step�ڵ尡 �Ϻα���[activity]���� ���Ǿ����� �˻��ϱ�
				String use = useStepAtActivity(step_code,"P");
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				}
				else {
					subMod = "<font color=darkred>Activity�ܰ迡�� �����Ǿ� �����Ұ�</font>";
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
	public ArrayList getStepDivList (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		query = "SELECT a.pid,a.ph_code,b.ph_name,a.step_code,a.step_name,a.type ";
		query += "FROM prs_step a,prs_phase b where a.type='"+login_division+"' and a.ph_code=b.ph_code";	
		query += " and (a."+sItem+" like '%"+sWord+"%') order by a.ph_code,a.step_code asc";
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
		String pcd = "",pnm="";
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
				table.setStepCode(step_code);	
				table.setStepName(rs.getString("step_name"));
				table.setType(rs.getString("type"));	
				
				//step�ڵ尡 �Ϻα���[activity]���� ���Ǿ����� �˻��ϱ�
				String use = useStepAtActivity(step_code,login_division);
				
				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(prs_mgr.equals("Y") && use.equals("N")) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				}
				else {
					subMod = "<font color=darkred>Activity�ܰ迡�� �����Ǿ� �����Ұ�</font>";
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
	//	STEP �Ϻα���[Activity]�� ����ߴ��� �Ǵ��ϱ�
	//*******************************************************************/	
	public String useStepAtActivity (String step_code,String type) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM prs_activity ";
		query += "where step_code = '"+step_code+"' and type = '"+type+"'";
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
	// �ش�Step QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	public ArrayList getStepRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT a.pid,a.ph_code,b.ph_name,a.step_code,a.step_name,a.type ";
		query += "FROM prs_step a,prs_phase b where a.pid='"+pid+"' and a.ph_code=b.ph_code";		
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new prsCodeTable();
					
				table.setPid(rs.getString("pid"));
				table.setPhCode(rs.getString("ph_code"));	
				table.setPhName(rs.getString("ph_name"));
				table.setStepCode(rs.getString("step_code"));	
				table.setStepName(rs.getString("step_name"));												
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
	public void inputStep(String pid,String ph_code,String step_code,String step_name,String type) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String input = "INSERT INTO prs_step(pid,ph_code,step_code,step_name,type) values('";
			input += pid+"','"+ph_code+"','"+step_code+"','"+step_name+"','"+type+"')";
//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*******************************************************************
	* PHASE ���� �����ϱ� 
	*******************************************************************/
	public void updateStep(String pid,String ph_code,String step_code,String step_name,String type) throws Exception
	{

		Statement stmt = null;
		stmt = con.createStatement();
		String update = "UPDATE prs_step set ph_code='"+ph_code+"',step_code='"+step_code+"',step_name='"+step_name;
			update += "',type='"+type+"' where pid='"+pid+"'";
//System.out.println("update : " + update );
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}

	/*******************************************************************
	* PHASE ���� �����ϱ� 
	*******************************************************************/
	public boolean deleteStep(String pid) throws Exception
	{
		boolean rtn = false;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		String delete = "";

		stmt = con.createStatement();

		//���μ��� �̸����̺��� ���μ��� �ڵ� ���ϱ�
		String step_code = "",type="";
		query = "SELECT step_code,type FROM prs_step where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()){
			step_code = rs.getString("step_code");
			type = rs.getString("type");
		}
		
		//���μ��� �������̺� �����Ǿ����� �Ǵ��Ͽ�[���ڵ�����] �������� ����
		query = "SELECT COUNT(*) FROM prs_activity where step_code='"+step_code+"' and type='"+type+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		//�������� �����ϱ�
		if(cnt < 2) {
			delete = "DELETE from prs_step where pid='"+pid+"'";
			stmt.executeUpdate(delete);
			rtn = true;
		} 

		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// STEP CODE���ϱ�
	// type : A:����, D:�μ� 
	//*******************************************************************/	
	public String getStepCode (String login_id,String div_code,String tag) throws Exception
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
			query = "SELECT step_code FROM prs_step where type='P' order by step_code desc";		
		else if(tag.equals("D") && prs_mgr.equals("Y"))
			query = "SELECT step_code FROM prs_step where type='"+div_code+"' order by step_code desc";	
		else return rtn;

		//������ ���
		rs = stmt.executeQuery(query);
		if(rs.next()) rtn = rs.getString("step_code");
	
		//ph_code�� +1
		if(rtn.length() > 0) {
			com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("00");
			String no = nft.toDigits(Integer.parseInt(rtn.substring(1,3))+1);
			rtn = "N"+no;
		}
		
		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// STEP CODE ����� �Ϸù�ȣ�� �ٽ� ä���ϱ�
	// ������� : �߰�����,�߰�
	//*******************************************************************/	
	public void updateStepCode (String login_id,String type,String tag) throws Exception
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
			cnt = getAllTotalCount("ph_code","");
			if(cnt == 0) return;
		}
		else if(tag.equals("D") && prs_mgr.equals("Y")) {
			cnt = getDivTotalCount(type,"ph_code","");
			if(cnt == 0) return;
		}

		//query���� �����
		stmt = con.createStatement();
		if(tag.equals("A") && prs_mgr.equals("Y"))
			query = "SELECT * FROM prs_step where type='P' order by ph_code,step_code asc";		
		else if(tag.equals("D") && prs_mgr.equals("Y"))
			query = "SELECT * FROM prs_step where type='"+type+"' order by ph_code,step_code asc";	
		else return;

		//������ ���
		String[][] data = new String[cnt][2];
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) {
			data[n][0] = rs.getString("pid");
			data[n][1] = rs.getString("step_code");
			n++;
		}
	
		//ph_code�� +1 : ���������� �ٽñ����Ͽ� �Է��ϱ� ��, �Ϻα������� ������� �ʾ�����...
		com.anbtech.util.normalFormat nft = new com.anbtech.util.normalFormat("00");
		String no = "",update="",use="";
		for(int i=0,j=1; i<n; i++,j++) {
			use = useStepAtActivity (data[i][1],type);
//System.out.println("use : " + use + " : " + data[i][1] + " : " + type);
			//�Ϻα����� ������ �ʾ����� ���� ��,N04A�� �������� ���ĺ��� ������ ���ܵ�.
			if(use.equals("N") && (data[i][1].length() == 3)) {			
				no = "N"+nft.toDigits(j);
				update = "update prs_step set step_code='"+no+"' where pid='"+data[i][0]+"'";
//System.out.println("���� ������ : " + update);
				stmt.executeUpdate(update);		
			}
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

	//*******************************************************************
	//	�űԷ� ����� STEP CODE ����� : �Ϻα��� ������ �űԷ� �ش�PHASE�� ������
	//*******************************************************************/	
	public String searchStepCode (String phase_code,String step_code,String type) throws Exception
	{
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//������ ���ĺ��� �����δ�.
		String[] A = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		
		//�ߺ��ڵ� �ִ��� ã��
		for(int i=0; i<A.length; i++) {
			query  = "SELECT COUNT(*) FROM prs_step ";
			query += "where ph_code ='"+phase_code+"' and step_code ='"+step_code+"' and type='"+type+"'";
			rs = stmt.executeQuery(query);
			rs.next();
			int cnt = rs.getInt(1);
			if(cnt == 0) break;
			step_code = step_code.substring(0,3) + A[i];
		}
		rtn = step_code;
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;
	}
}
