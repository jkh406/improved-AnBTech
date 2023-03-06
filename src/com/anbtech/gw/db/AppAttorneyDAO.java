package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;
import com.anbtech.dbconn.DBConnectionManager;

public class AppAttorneyDAO
{
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//���� ó��
	private DBConnectionManager connMgr;
	private Connection con;

	private String[][] app_ing = null;		//�̰Ṯ�� ������ȣ���

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public AppAttorneyDAO(Connection con) 
	{
		this.con = con;
	}

	public AppAttorneyDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}
	//--------------------------------------------------------------------------//
	//		�ܼ��� �븮�����ڸ� ó���ϰ��� �Ҷ�										//
	//		�Է�/����/�븮���系��/�븮�����ڻ�� ��									//
	//--------------------------------------------------------------------------//

	//*******************************************************************
	//	������ �븮������ ������ ������ ã�� : app_attorney
	//*******************************************************************/
	public String searchAttorney(String id) throws Exception
	{ 
		String attorney_id="";					//������ �븮�����ڷ� ������ ������ ���
		int s_date=0,e_date=0;					//������ ���� ������,������
		int t_date=0;							//����
		if(id == null) return attorney_id;		//null�̸� return

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//�����ϱ�
		String query = "SELECT attorney_id,start_date,end_date FROM APP_ATTORNEY where approval_id='"+id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			attorney_id = rs.getString("attorney_id");
			String sd = rs.getString("start_date");	if(sd == null) sd = "";
			String ed = rs.getString("end_date");	if(ed == null) ed = "";
			if(sd.length() != 0) s_date = Integer.parseInt(sd);
			if(ed.length() != 0) e_date = Integer.parseInt(ed);
		}
	
		//����üũ �ϱ�
		t_date = Integer.parseInt(anbdt.getDateNoformat());
		if(t_date < s_date)  attorney_id = "";
		else if(t_date > e_date) attorney_id = "";

		//�ݱ�
		stmt.close();
		rs.close();
		return attorney_id;
	}

	//*******************************************************************
	//	������ �븮������ ������ ������ ã�� : app_attorney
	//*******************************************************************/
	public String searchNameAttorney(String id) throws Exception
	{ 
		String attorney_name="";				//������ �븮�����ڷ� ������ ������ �̸�
		if(id == null) return attorney_name;	//null�̸� return

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT attorney_name FROM APP_ATTORNEY where approval_id='"+id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) attorney_name = rs.getString("attorney_name");
	
		stmt.close();
		rs.close();
		return attorney_name;
	}

	//*******************************************************************
	//	������ ������ ã�� : app_attorney
	//*******************************************************************/
	public ArrayList isAttorney(String login_id) throws Exception
	{
		TableAppMaster table = null;
		ArrayList table_list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//������ ������ ��찡 �ִ´�
		String query = "SELECT * FROM APP_ATTORNEY where approval_id='"+login_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			table = new TableAppMaster();							
			table.setAttorneyId(rs.getString("attorney_id"));
			table.setAttorneyName(rs.getString("attorney_name"));
			table.setStartDate(rs.getString("start_date"));
			table.setEndDate(rs.getString("end_date"));
			table_list.add(table);					
		}

		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	//	������ ������ �Է��ϱ� : app_attorney
	//*******************************************************************/
	public void inputAttorney(String login_id,String attorney_id,String attorney_name,
		String start_date,String end_date) throws Exception
	{
		String approval_id = "";				//������������
		String query="",update="",input="";
		String rtn = "N";			//�븮�����ڰ� �ߺ����� ����
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//������ ������ ��찡 �ִ��� ã��
		query = "SELECT approval_id FROM APP_ATTORNEY where approval_id='"+login_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) approval_id = rs.getString("approval_id");

		//�Է��� ��찡 �ִ°�� 
		if(approval_id.equals(login_id)) {
			update = "UPDATE APP_ATTORNEY set attorney_id='"+attorney_id+"',attorney_name='";
			update += attorney_name+"',start_date='"+start_date+"',end_date='"+end_date+"'";
			update += " where approval_id='"+login_id+"'";
			stmt.executeUpdate(update);
		}
		//���ʷ� �Է��ϴ� ���
		else {
			input = "INSERT into APP_ATTORNEY(approval_id,attorney_id,attorney_name,start_date,end_date) values('"+login_id+"','";
			input += attorney_id+"','"+attorney_name+"','"+start_date+"','"+end_date+"')";
			stmt.executeUpdate(input);
		}
	
		stmt.close();
		rs.close();
		
	}
	//*******************************************************************
	//	������ ������ �����ϱ� : app_attorney
	//*******************************************************************/
	public void deleteAttorney(String login_id) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		String update = "UPDATE APP_ATTORNEY set attorney_id='',attorney_name='',start_date='',end_date='' where approval_id='"+login_id+"'";
		stmt.executeUpdate(update);
	
		stmt.close();
	}

	//--------------------------------------------------------------------------//
	//		���ڰ��翡 �̰Ṯ���� ó���ϰ� �Ҷ�  									//
	//		(���� ������� ���� : 2003-12-10)										//
	//--------------------------------------------------------------------------//
	//*******************************************************************
	//	���� ������ �븮������ ������ ������ ã�� : app_master
	//*******************************************************************/
	public String[] getAttorney(String pid) throws Exception
	{
		String[] attorney=new String[2];			//������ �븮�����ڷ� ������ �������,�����ڻ��
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT attorney_state,attorney_id FROM APP_MASTER where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			attorney[0]= rs.getString("attorney_state");
			attorney[1]= rs.getString("attorney_id");
		}
	
		stmt.close();
		rs.close();
		return attorney;
	}
	
	//*******************************************************************
	//	������ �븮�� ������ ���� ã�� : app_attorney [������ �߽�]
	//*******************************************************************/
	public String checkAttorney(String id) throws Exception
	{
		String rtn="N";			//������ �븮������ ����
		String att_id = "";		//������ �븮������ ���
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT attorney_id FROM APP_ATTORNEY where approval_id='"+id+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) att_id = rs.getString("attorney_id");
		if(att_id.length() > 2) rtn = "Y";
	
		stmt.close();
		rs.close();
		return rtn;
	}
	//*******************************************************************
	//	�븮���� �ش繮���� ���������� ã�� : app_attorney [�븮������ �߽�]
	//*******************************************************************/
	public String checkApproval(String login_id,String pid) throws Exception
	{
		String rtn="N";			//�ش繮�� �븮������� ����
		String self_id = "";	//�����ڵ��� ���
		String att_id = "";		//�븮������ ���
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT * FROM APP_MASTER where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
	
		if(rs.next()) {
			self_id = rs.getString("reviewer")+"|";
			self_id += rs.getString("decision")+"|";
			self_id += rs.getString("agree")+"|";
			self_id += rs.getString("agree2")+"|";
			self_id += rs.getString("agree3")+"|";
			self_id += rs.getString("agree4")+"|";
			self_id += rs.getString("agree5")+"|";
			self_id += rs.getString("agree6")+"|";
			self_id += rs.getString("agree7")+"|";
			self_id += rs.getString("agree8")+"|";
			self_id += rs.getString("agree9")+"|";
			self_id += rs.getString("agree10")+"|";
			self_id += rs.getString("attorney_id");
			att_id = rs.getString("attorney_id");
		}

		//�Ǵ��ϱ�
		if(self_id.indexOf(login_id) != -1) rtn = "Y";		//�ڽ��� ���繮��
		if(att_id.length() < 3) rtn = "O";					//�븮���繮���� �ƴ�

		stmt.close();
		rs.close();
		return rtn;
	}
	//*******************************************************************
	//	������ �븮 ������ ����� �븮������¸� �ٲ��ֱ�
	// ��,������´� �԰�[APS],�ݷ�[APR]�϶��� �����Ŵ
	//*******************************************************************/
	public void statusAttorney(String pid,String attorney_state) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		update = "UPDATE APP_MASTER set attorney_state='"+attorney_state+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		update = "UPDATE APP_SAVE set attorney_state='"+attorney_state+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);
	
		stmt.close();
	}

	//*******************************************************************
	//	������ �븮���� �븮�����ڷ� �������̺� �Է��ϱ� : ���� �̰��Կ� �ݿ�(app_master)
	//*******************************************************************/
	public void attorneyApproval(String id) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//�븮������ ������ ������ ã��
		String att_id = searchAttorney(id);

		//�̰��Թ��� ã��
		getAppIngList(id);

		//�̰��Կ� �븮�����ڸ� �Է��Ѵ�.
		String pid = "",state="",a_id = att_id;
		String[] s = {"","APV","APL","APG","APG2","APG3","APG4","APG5","APG6","APG7","APG8","APG9","APG10"};
		int cnt = app_ing.length;		//�̰��� ����
		for(int i=0; i<cnt; i++) {
			for(int j=1; j<13; j++) {	//�ش��� ã��
				pid = app_ing[i][0];	//������ȣ
				if(app_ing[i][j].equals(id)) { 
					state = s[j];
					a_id = att_id;
					break; 
				}
			}
			//�Է��ϱ�
			String update = "UPDATE app_master set attorney_state='"+state+"',attorney_id='"+a_id+"' where pid='"+pid+"'";
			//System.out.println(update);
			stmt.executeUpdate(update);
		}

		stmt.close();
	}
	//*******************************************************************
	//	������ �븮���� �븮�����ڷ� �������̺� �����ϱ� : ���� �̰��Կ� �ݿ�(app_master)
	//*******************************************************************/
	public void attorneyDelete(String id) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//�̰��Թ��� ã��
		getAppIngList(id);

		//�̰��Կ� �븮�����ڸ� �Է��Ѵ�.
		String pid = "";
		int cnt = app_ing.length;		//�̰��� ����
		for(int i=0; i<cnt; i++) {
			pid = app_ing[i][0];	//������ȣ
			//�����ϱ�
			String update = "UPDATE app_master set attorney_state='',attorney_id='' where pid='"+pid+"'";
			//System.out.println(update);
			stmt.executeUpdate(update);
		}

		stmt.close();
	}

	//*******************************************************************
	//	�̰��� APP_MASTER ��ü����
	//*******************************************************************/
	private int getTotalApp(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
		//������ �����϶� (����,���� ����)
		String ING_data = "where (app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
		ING_data +=  id + "')";

		//�ϰ��� �����϶�(������ ����)
		String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "')";

		query += ING_data + PAL_data;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;
	}

	//*******************************************************************
	//	�̰Ṯ�� ��üLIST���� ������ ����� �迭�� ���
	//*******************************************************************/	
	private void getAppIngList (String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";			//���
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		stmt = con.createStatement();
		
		//�迭�����
		int total_cnt = getTotalApp(id);
		app_ing = new String[total_cnt][13];

		//query���� �����
		String ING_data = "where ((app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
			ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
			ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
			ING_data +=  id + "')";

		//�ϰ��� �����϶�(������ ����)
		String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
			PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
			PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
			PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
			PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "'))";
		query = "SELECT * FROM APP_MASTER ";
		query += ING_data + PAL_data;
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) { 
			app_ing[n][0] = rs.getString("pid");			//������ȣ
			app_ing[n][1] = rs.getString("reviewer");		//����
			app_ing[n][2] = rs.getString("decision");		//����
			app_ing[n][3] = rs.getString("agree");			//����1
			app_ing[n][4] = rs.getString("agree2");			//����2
			app_ing[n][5] = rs.getString("agree3");			//����3
			app_ing[n][6] = rs.getString("agree4");			//����4
			app_ing[n][7] = rs.getString("agree5");			//����5
			app_ing[n][8] = rs.getString("agree6");			//����6
			app_ing[n][9] = rs.getString("agree7");			//����7
			app_ing[n][10] = rs.getString("agree8");		//����8
			app_ing[n][11] = rs.getString("agree9");		//����9
			app_ing[n][12] = rs.getString("agree10");		//����10
			n++;
		} 
		stmt.close();
		rs.close();
	}
}
