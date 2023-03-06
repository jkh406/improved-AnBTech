package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtScheduleDAO
{
	com.anbtech.pjt.business.pjtScheduleBO schBO = new com.anbtech.pjt.business.pjtScheduleBO();
	com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();	//���Ϸ� ���[����]
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����ó��
	private Connection con;
	
	private String query = "";
	private String[][] item = null;				//���μ��������� �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String pjt_code = "";				//���μ��� �ڵ�
	private String child_node = "";				//���μ����� �ڳ��
	private String level_no = "0";				//������� ������ȣ
	private String type = "";					//P:����ǥ��,  �μ��ڵ�:�μ�ǥ��
	private String prs_code = "";				//���μ��� �ڵ��ȣ

	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public pjtScheduleDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtScheduleDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	�ش� �����ڵ��� �� ���� �ľ��ϱ� 
	//*******************************************************************/
	public int getAllTotalCount(String pjt_code) throws Exception
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
	 * �ش������ ����׸��� �迭�� ��´�. 
	 *********************************************************************/
	public void saveItemsArray(String pjt_code,String level_no,String parent_node) throws Exception
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

	/**********************************************************************
	 * �ش������ ����׸��� �迭���� ArrayList�� ��´�
	 *********************************************************************/
	public ArrayList getPjtSchedule(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//1.ArrayList�� ���
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//2.�迭�����
		int cnt = getAllTotalCount(pjt_code);
		if(cnt == 0) { return table_list; };
		item = new String[cnt][23];

		//3. �迭�� ��´�.
		saveItemsArray(pjt_code,level_no,parent_node);

		String node_name="";
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
			
			if(item[i][5].equals("3")) //pjt_cod,parent_node,child_node 
				node_name = "<a href=\"javascript:detailSch('"+item[i][1]+"','"+item[i][3]+"','"+item[i][4]+"');\">"+item[i][4]+" "+item[i][6]+"</a>";
			else node_name = item[i][4]+" "+item[i][6];
			table.setNodeName(node_name);

			table.setWeight(Double.parseDouble(item[i][7]));
			table.setUserId(item[i][8]);
			table.setUserName(item[i][9]);
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

	/**********************************************************************
	 * �ش������ �ش��� ���� ��������. 
	 *********************************************************************/
	public ArrayList getNodeData(String pjt_code,String parent_node,String child_node) throws Exception
	{
		//���� �ʱ�ȭ
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		query = "select * from pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"' ";
		query += "and child_node = '"+child_node+"' order by child_node asc";
		rs = stmt.executeQuery(query);

		projectTable table = null;
		ArrayList table_list = new ArrayList();

		while (rs.next()) {
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
		rs.close();
		stmt.close(); 
		return table_list;
		
	}

	//*******************************************************************
	// �ڽ��� ��ü ����LIST
	//*******************************************************************/	
	public ArrayList getProjectList (String login_id,String pjtWord,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//����PM ���� �Ǵ��ϱ�
		String pjt_pml = "N";
		pjt_pml = checkPjtPML(login_id);

		//query���� �����
		if(pjt_pml.equals("Y")) {		//PM
			query = "SELECT pjt_status,pjt_code,pjt_name FROM pjt_general where pjt_mbr_id like '%"+login_id+"%'";	
			query += " and pjt_status like '%"+pjtWord+"%'";
			query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 
		} else {						//���
			//query = "SELECT distinct pjt_code,pjt_name FROM pjt_member where pjt_mbr_id='"+login_id+"'";	
			//query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc";
			query = "SELECT distinct a.pjt_status,b.pjt_code,b.pjt_name FROM pjt_general a,pjt_member b where b.pjt_mbr_id='"+login_id+"'";	
			query += " and a.pjt_status like '%"+pjtWord+"%' and a.pjt_code = b.pjt_code";
			query += " and (b."+sItem+" like '%"+sWord+"%') order by b.pjt_code desc";
		}
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
								
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				table.setPjtStatus(rs.getString("pjt_status"));

				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	����PM ���� �Ǵ��ϱ�
	//*******************************************************************/	
	public String checkPjtPML (String login_id) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where keyname = 'PJT_PML' and owner like '%"+login_id+"%'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		if(cnt > 0) rtn = "Y";
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;	
	}

	/*******************************************************************
	* ������������ �������� �Է¿�û��,������ ������  ---> PM
	* �ٽ�:�����,���,��ȹ��,������
	*******************************************************************/
	public void updateSchedule(String pid,String pjt_code,String parent_node,String child_node,String weight,
		String user_id,String user_name,String pjt_node_mbr,String plan_start_date,String plan_end_date,
		String chg_start_date,String chg_end_date,String rst_start_date,String rst_end_date,
		String node_status) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();
		
		String plan_cnt = schBO.getPeriodDate(plan_start_date,plan_end_date);		//��ȹ�ϼ�
		String chg_cnt = schBO.getPeriodDate(chg_start_date,chg_end_date);			//�����ϼ�
		String result_cnt = schBO.getPeriodDate(rst_start_date,rst_end_date);		//�����ϼ�

		//�ش����� ������ �Է��ϱ�
		update = "UPDATE pjt_schedule set weight='"+weight+"',user_id='"+user_id+"',user_name='"+user_name+"',pjt_node_mbr='"+pjt_node_mbr;
		update += "',plan_start_date='"+plan_start_date+"',plan_end_date='"+plan_end_date+"',chg_start_date='"+chg_start_date;
		update += "',chg_end_date='"+chg_end_date+"',rst_start_date='"+rst_start_date+"',rst_end_date='"+rst_end_date;
		update += "',plan_cnt='"+plan_cnt+"',chg_cnt='"+chg_cnt+"',result_cnt='"+result_cnt+"',node_status='"+node_status;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//�ش��尡 �ش�STEP�� ������ Activity�� �������̸� �ش�STEP�� �������� �Է��Ѵ�.
		updateStepSchedule(pjt_code,parent_node,child_node,plan_start_date,plan_end_date,chg_start_date,chg_end_date,node_status);

		stmt.close();
	}

	/*******************************************************************
	* ������������ ���������� ��û�� ---------------> PM
	* �ٽ�:����������,���û���
	*******************************************************************/
	public void updateNodeStart(String pid,String pjt_code,String parent_node,String child_node,
		String plan_start_date,String plan_end_date,String chg_start_date,String chg_end_date,
		String rst_start_date,String rst_end_date,String node_status,String remark) throws Exception
	{
		String update = "",query="",pjt_status="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//������� �Է��ϱ�
		update = "UPDATE pjt_schedule set chg_start_date='"+chg_start_date+"',chg_end_date='"+chg_end_date;
		update += "',rst_start_date='"+rst_start_date+"',rst_end_date='"+rst_end_date+"',node_status='1',remark='"+remark;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//�������������� ������� �ľ��ϱ�
		query  = "SELECT pjt_status FROM pjt_general where pjt_code = '"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			pjt_status = rs.getString("pjt_status");
		}

		//�����⺻������ ���¾˷��ֱ�[1:���������� �� ���� ������]
		if(pjt_status.equals("0")) {	//�������������� ������
			update = "UPDATE pjt_general set pjt_status='1',rst_start_date='"+rst_start_date+"'";
			update += " where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			update = "UPDATE prs_project set pjt_status='1' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);

			update = "UPDATE pjt_status set pjt_status='1' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
		}
		
		//�ش��尡 �ش�STEP�� ������ Activity�� �������̸� �ش�STEP�� �������� �Է��Ѵ�.
		updateStepSchedule(pjt_code,parent_node,child_node,plan_start_date,plan_end_date,chg_start_date,chg_end_date,node_status);

		//�ش����� �������step,phase�� project�� ���� �����ϸ� �Է��ϱ�
		updateResultStartDate(pjt_code,child_node,rst_start_date);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* �ش��尡 �ش�STEP�� ������ Activity�� �������̸� �ش�STEP�� �������� �Է��Ѵ�.
	*******************************************************************/
	public void updateStepSchedule(String pjt_code,String parent_node,String child_node,
		String plan_start_date,String plan_end_date,String chg_start_date,String chg_end_date,
		String node_status) throws Exception
	{
		String query="",update="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = null;				//��� ��ȹ�� ���
		String[][] change = null;			//������ ��� (��,�������� ������ ��ȹ���� ��´�)
		stmt = con.createStatement();
	
		//1.STEP�� Activity��ü������ �ľ��Ѵ�.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int ActCnt = rs.getInt(1);
		
		//2.���� ��ϵ� ��ȹ�� Activity�� ���� �ľ��Ѵ�.[��ü���� - ���簳�� > 1 �̸� return]
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"' and plan_cnt != 0";
		rs = stmt.executeQuery(query);
		rs.next();
		int PlanCnt = rs.getInt(1);
		if(ActCnt > PlanCnt + 1) {	stmt.close(); rs.close(); return; }

		//3.���� ��ϵ� Activity�� ������ �迭�� ��´�. 
		//  �׸��� null�̸� �Ķ���� ���ڸ� ��´�.
		data = new String[ActCnt][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"'";
		rs = stmt.executeQuery(query);
		int n = 0, plan = 0, chg = 0;	//�迭��ȣ,plan�� null�ΰ���, chg�� null�̰���
		String cnode = "";				//�ڳ��
		while(rs.next()) {
			cnode = rs.getString("child_node");

			data[n][0] = rs.getString("plan_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = plan_start_date;
			else if(data[n][0].length() == 0) plan++;
			data[n][1] = rs.getString("plan_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = plan_end_date;
			n++;
		}

		//4.��ȹ���� �������� �ƴϸ� ���⼭ ����
		if(plan > 0) {		//������ �Էµ� �������� �������� �ƴ�
			stmt.close(); rs.close(); return; 
		}

		//5.��ȹ�� Activity���� �����ϰ� �������� ���Ѵ�.
		String[][] schDate = new String[1][3];	//��ȹ��[����,����]
		schDate = schBO.completeFirstLastDate(data); 

		//6.���ڰ� 0�� �ƴϸ� ��ȹ�ϰ� �������� �Է��Ѵ�.
		//��ȹ��[����,����]�� �ش� STEP�� �Է��Ѵ�. [������ : ��� ���Է�]
		if(!schDate[0][0].equals("0") && (node_status.equals(""))) {	
			update = "UPDATE pjt_schedule set plan_start_date='"+schDate[0][0]+"',plan_end_date='"+schDate[0][1];
			update += "', plan_cnt='"+Integer.parseInt(schDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"' and level_no='2'";
			stmt.executeUpdate(update);
		}
		//��ȹ��[����,����]�� �ش� STEP�� �Է��Ѵ�. [������ : ������(0)]
		else if(!schDate[0][0].equals("0") && (node_status.equals("0"))) {	
			update = "UPDATE pjt_schedule set plan_start_date='"+schDate[0][0]+"',plan_end_date='"+schDate[0][1];
			update += "', plan_cnt='"+Integer.parseInt(schDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"' and level_no='2'";
			stmt.executeUpdate(update);
		}

		//-----------------------------------------------------------------------
		//7. �������� �ԷµǸ� Step�� ��ȹ������ �������� ���Ͽ� Step�� ���������� �Է��Ѵ�.
		//-----------------------------------------------------------------------
		if(chg_start_date.length() != 0){
			//1.�ش�Step�� ������ ��� (��,�������� ������ ��ȹ���� ��´�)
			change = new String[ActCnt+1][2];		
			query  = "SELECT * FROM pjt_schedule ";
			query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"'";
			rs = stmt.executeQuery(query);
		
			int cn = 0;
			while(rs.next()) {
				change[cn][0] = rs.getString("chg_start_date");		if(change[cn][0] == null) change[cn][0] = "";
				if(change[cn][0].length() == 0) change[cn][0] = rs.getString("plan_start_date"); 
				change[cn][1] = rs.getString("chg_end_date");		if(change[cn][1] == null) change[cn][1] = "";
				if(change[cn][1].length() == 0) change[cn][1] = rs.getString("plan_end_date"); 
				cn++;
			}
			change[cn][0] = chg_start_date;
			change[cn][1] = chg_end_date;

			//2.������ �����ϰ� �������� ���Ѵ�.
			String[][] chgDate = new String[1][3];	//��ȹ��[����,����]
			chgDate = schBO.completeFirstLastDate(change); 

			//3.������[����,����]�� �ش� STEP�� �Է��Ѵ�. [������ : ������(1)]
			update = "UPDATE pjt_schedule set chg_start_date='"+chgDate[0][0]+"',chg_end_date='"+chgDate[0][1];
			update += "', chg_cnt='"+Integer.parseInt(chgDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"' and level_no='2'";
			stmt.executeUpdate(update);
		}

		//8.�ش�STEP�� �������̸� �ش�PHASE�� �Է��Ѵ�.
		updatePhaseSchedule(pjt_code,parent_node,plan_start_date,plan_end_date,chg_start_date,chg_end_date,node_status);
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();	
	}

	/*******************************************************************
	* �ش��尡 �ش�PHASE�� ������ STEP �����̸� �ش�PHASE�� �������� �Է��Ѵ�.
	*******************************************************************/
	public void updatePhaseSchedule(String pjt_code,String child_node,String plan_start_date,String plan_end_date,
		String chg_start_date,String chg_end_date,String node_status) throws Exception
	{
		String query="",update="",phase="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = null;
		String[][] change = null;			//������ ��� (��,�������� ������ ��ȹ���� ��´�)
		stmt = con.createStatement();

		//0.PHASE�ڵ� ã��
		query  = "SELECT parent_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and child_node = '"+child_node+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			phase = rs.getString("parent_node");
		}

		//1.PHASE�� STEP��ü������ �ľ��Ѵ�.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no='2'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCnt = rs.getInt(1);

		//2.���� ��ϵ� ��ȹ�� STEP�� ���� �ľ��Ѵ�.[��ü���� - ���簳�� > 1 �̸� return]
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2' and plan_cnt != 0";
		rs = stmt.executeQuery(query);
		rs.next();
		int PlanCnt = rs.getInt(1);
		if(PhsCnt > PlanCnt + 1) {	stmt.close(); rs.close(); return; }
	
		//3.���� ��ϵ� STEP�� ������ �迭�� ��´�. 
		//  �׸��� null�̸� �Ķ���� ���ڸ� ��´�.
		data = new String[PhsCnt][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
		rs = stmt.executeQuery(query);
		int n = 0, plan = 0, chg = 0;	//�迭��ȣ,plan�� null�ΰ���, chg�� null�̰���
		String cnode = "";				//�ڳ��
		while(rs.next()) {
			cnode = rs.getString("child_node");

			data[n][0] = rs.getString("plan_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = plan_start_date;
			else if(data[n][0].length() == 0) plan++;
			data[n][1] = rs.getString("plan_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = plan_end_date;
			n++;
		}

		//4.��ȹ���� �������� �ƴϸ� ���⼭ ����
		if(plan > 0) {		//������ �Էµ� �������� �������� �ƴ�
			stmt.close(); rs.close(); return; 
		}

		//5.Step���� �����ϰ� �������� ���Ѵ�.
		String[][] schDate = new String[1][3];	//��ȹ��[����,����], ������[����,����]
		schDate = schBO.completeFirstLastDate(data);

		//6.���ڰ� 0�� �ƴϸ� ��ȹ�ϰ� �������� �Է��Ѵ�.
		//��ȹ��[����,����]�� �ش� STEP�� �Է��Ѵ�. [������ : ��� ���Է�]
		if(!schDate[0][0].equals("0") && (node_status.equals(""))) {	
			update = "UPDATE pjt_schedule set plan_start_date='"+schDate[0][0]+"',plan_end_date='"+schDate[0][1];
			update += "', plan_cnt='"+Integer.parseInt(schDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+phase+"' and level_no='1'";
			stmt.executeUpdate(update);
		}
		//��ȹ��[����,����]�� �ش� STEP�� �Է��Ѵ�. [������ : ������(0)]
		else if(!schDate[0][0].equals("0") && (node_status.equals("0"))) {	
			update = "UPDATE pjt_schedule set plan_start_date='"+schDate[0][0]+"',plan_end_date='"+schDate[0][1];
			update += "', plan_cnt='"+Integer.parseInt(schDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+phase+"' and level_no='1'";
			stmt.executeUpdate(update);
		}

		//7.������ü�����ϼ� ���ϱ�
		//PHASE�� ��ü������ �ľ��Ѵ�.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PjtCnt = rs.getInt(1);

		//�迭�� �����
		data = new String[PjtCnt][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1' and child_node !='"+phase+"'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()) {
			data[n][0] = rs.getString("plan_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = plan_start_date;
			else if(data[n][0].length() == 0) plan++;
			data[n][1] = rs.getString("plan_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = plan_end_date;
			n++;
		}
		data[n][0] = schDate[0][0];
		data[n][1] = schDate[0][1];

		String[][] pjtDate = new String[1][3];	//��ȹ��[����,����],���ϼ�
		pjtDate = schBO.completeFirstLastDate(data);

		update = "UPDATE pjt_schedule set plan_cnt='"+Integer.parseInt(pjtDate[0][2]);
		update += "' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		//-----------------------------------------------------------------------
		//8. �������� �ԷµǸ� phase�� ��ȹ������ �������� ���Ͽ� Step�� ���������� �Է��Ѵ�.
		//-----------------------------------------------------------------------
		String[][] chgDate = null;	//�����Ⱓ[����,����]
		if(chg_start_date.length() != 0){
			//1.�ش�Step�� ������ ��� (��,�������� ������ ��ȹ���� ��´�)
			change = new String[PhsCnt+1][2];		
			query  = "SELECT * FROM pjt_schedule ";
			query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
			rs = stmt.executeQuery(query);
		
			int cn = 0;
			while(rs.next()) {
				change[cn][0] = rs.getString("chg_start_date");		if(change[cn][0] == null) change[cn][0] = "";
				if(change[cn][0].length() == 0) change[cn][0] = rs.getString("plan_start_date"); 
				change[cn][1] = rs.getString("chg_end_date");		if(change[cn][1] == null) change[cn][1] = "";
				if(change[cn][1].length() == 0) change[cn][1] = rs.getString("plan_end_date"); 
				cn++;
			}
			change[cn][0] = chg_start_date;
			change[cn][1] = chg_end_date;

			//2.������ �����ϰ� �������� ���Ѵ�.
			chgDate = new String[1][3];	//�����Ⱓ[����,����]
			chgDate = schBO.completeFirstLastDate(change); 

			//3.������[����,����]�� �ش� STEP�� �Է��Ѵ�. [������ : ������(1)]
			update = "UPDATE pjt_schedule set chg_start_date='"+chgDate[0][0]+"',chg_end_date='"+chgDate[0][1];
			update += "', chg_cnt='"+Integer.parseInt(chgDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+phase+"' and level_no='1'";
			stmt.executeUpdate(update);
		}

		//-----------------------------------------------------------------------
		//9.�ش�Phase�� ���μ����Ǹ� �����⺻������ �����Ⱓ�� �ڵ��Է��Ѵ�.
		//-----------------------------------------------------------------------
		if(chg_start_date.length() != 0){
			updateGeneralSchedule(pjt_code,chgDate[0][0],chgDate[0][1]);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* �ش�Phase�� ���μ����Ǹ� �����⺻������ �����Ⱓ�� �ڵ��Է�
	*******************************************************************/
	public void updateGeneralSchedule(String pjt_code,String chg_start_date,String chg_end_date) throws Exception
	{
		String query="",update="",phase="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] change = null;
		stmt = con.createStatement();

		//1.�ش������ PHASE��ü������ �ľ��Ѵ�.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCnt = rs.getInt(1);

		//2.���� ��ϵ� PHASE�� ������ �迭�� ��´�. 
		change = new String[PhsCnt+1][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1'";
		rs = stmt.executeQuery(query);
		
		int cn = 0;
		while(rs.next()) {
			change[cn][0] = rs.getString("chg_start_date");		if(change[cn][0] == null) change[cn][0] = "";
			if(change[cn][0].length() == 0) change[cn][0] = rs.getString("plan_start_date"); 
			change[cn][1] = rs.getString("chg_end_date");		if(change[cn][1] == null) change[cn][1] = "";
			if(change[cn][1].length() == 0) change[cn][1] = rs.getString("plan_end_date"); 
			cn++;
		}
		change[cn][0] = chg_start_date;
		change[cn][1] = chg_end_date;

		//3.������ �����ϰ� �������� ���Ѵ�.
		String[][] chgDate = new String[1][3];	//�����Ⱓ[����,����]
		chgDate = schBO.completeFirstLastDate(change); 

		//4.������[����,����]�� �����⺻������ �Է��Ѵ�.
		update = "UPDATE pjt_general set chg_start_date='"+chgDate[0][0]+"',chg_end_date='"+chgDate[0][1];
		update += "' where pjt_code='"+pjt_code+"'";
		stmt.executeUpdate(update);

		//5.������[����,����],�����ϼ� �� ���������� �ֻ�ܿ� �Է��Ѵ�.
		update = "UPDATE pjt_schedule set chg_start_date='"+chgDate[0][0]+"',chg_end_date='"+chgDate[0][1];
		update += "',chg_cnt='"+chgDate[0][2];
		update += "' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// STEP,PHASE�� ���� �����ϸ� �Է��ϱ�
	//*******************************************************************/	
	public void	updateResultStartDate(String pjt_code,String child_node,String rst_start_date) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",update="",node_status="";	
		String step="",phase="",rdate="",rstDate="";
		String[] data;
		int n = 0;
		stmt = con.createStatement();

		//1.�ش����� STEP�ڵ� ã��
		query  = "SELECT parent_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and child_node = '"+child_node+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			step = rs.getString("parent_node");
		}

		//2.�ش����� PHASE�ڵ� ã��
		query  = "SELECT parent_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and child_node = '"+step+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			phase = rs.getString("parent_node");
		}

		//3.�ش� STEP���� Activity �����ľ�
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+step+"' and level_no='3'";
		rs = stmt.executeQuery(query);
		rs.next();
		int ActCnt = rs.getInt(1);

		//4.�ش� PHASE���� Step �����ľ�
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no='2'";
		rs = stmt.executeQuery(query);
		rs.next();
		int StpCnt = rs.getInt(1);

		//5.�ش� �������� Phase �����ľ�
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCnt = rs.getInt(1);

		//6.�ش� Activity�� �����ϸ� �����ϱ� [���ʽ����� ã�� -> �ش����� STEP�� ���������Ͽ� �Է��ϱ�]
		data = new String[ActCnt+1];
		query  = "SELECT rst_start_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+step+"' and level_no = '3'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_start_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rst_start_date;
		rstDate = schBO.completeFirstDate(data);	//������������ �������ϳ���
		update = "UPDATE pjt_schedule set rst_start_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and child_node = '"+step+"'";
		stmt.executeUpdate(update);

		//6.�ش� Step�� �����ϸ� �����ϱ� [���ʽ����� ã�� -> �ش����� PHASE�� ���������Ͽ� �Է��ϱ�]
		data = new String[StpCnt+1];
		query  = "SELECT rst_start_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_start_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rstDate;
		rstDate = schBO.completeFirstDate(data);	//������������ �������ϳ���
		update = "UPDATE pjt_schedule set rst_start_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and child_node = '"+phase+"'";
		stmt.executeUpdate(update);

		//7.�ش� Phase�� �����ϸ� �����ϱ� [���ʽ����� ã�� -> �ش����� ������ ���������Ͽ� �Է��ϱ�]
		data = new String[PhsCnt+1];
		query  = "SELECT rst_start_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_start_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rstDate;
		rstDate = schBO.completeFirstDate(data);	//������������ �������ϳ���
		update = "UPDATE pjt_schedule set rst_start_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and level_no = '0'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* �ش��尡 �ش�PHASE�� ������ STEP�� ���Ϸ��̸� �ش�STEP�� �Ϸ������� �Է��Ѵ�.
	*******************************************************************/
	public String checkCompleteSchedule(String pjt_code) throws Exception
	{
		String query="",rtnData="N";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//1.PHASE�ܰ��� ��ü����
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1'";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCnt = rs.getInt(1);
	
		//2.PHASE�� ��ȹ�ϼ� ��ϰ���
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1' and plan_cnt != 0";
		rs = stmt.executeQuery(query);
		rs.next();
		int PhsCompCnt = rs.getInt(1);
	
		if(PhsCnt == PhsCompCnt) rtnData="Y";

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return rtnData;
	}

	/*******************************************************************
	* �ش������ Activity�� ù���� ��������带 ã��
	* [��ȹ�Է½� ó���� ���������ڰ� �´��� �˻��ϱ�����]
	*******************************************************************/
	public String checkFLnodeSchedule(String pjt_code) throws Exception
	{
		String query="",rtnData="";
		int cnt = 0;
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//1.Activity���� ���� ���ϱ�
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '3'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			cnt = rs.getInt(1);
		}

		//2.Activity�� �����ϱ�
		query  = "SELECT child_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '3' order by child_node ASC";
		rs = stmt.executeQuery(query);
		int n=1;
		while(rs.next()) {
			if(n == 1) rtnData = rs.getString("child_node")+"|";
			else if(n == cnt) rtnData += rs.getString("child_node"); 
			n++;
		}
	
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return rtnData;
	}
	/*******************************************************************
	* �ش������ ��ü weight�� ���ϱ�
	* [�ش������ level_no=0�ΰ� ���Ѹ��]
	*******************************************************************/
	public String getTotalWeight(String pjt_code) throws Exception
	{
		String query="",rtnData="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query  = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '0'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			rtnData = Double.toString(rs.getDouble("weight"));
		}

		stmt.close();
		rs.close();
		return rtnData;
	}

	/*********************************************************************
	 	�۾����ó����� ������ڿ��� ���ڿ������� ������
	*********************************************************************/
	public void sendMailToUser(String mgr_id,String mgr_name,String rec_id,String rec_name,
		String pjt_code,String pjt_name,String node_code,String node_name,
		String psd,String ped,String csd,String ced,String rsd,String remark) throws Exception 
	{	
		String pid = getID();								//������ȣ
		String subject = "";								//����
		String user_id = "", user_name = "", rec = "";		//�ۼ��� ���,�̸�,������List
		String write_date = anbdt.getTime();				//���ڿ��� ��������
		String delete_date = anbdt.getAddMonthNoformat(1);	//������������

		//1.�ۼ���[����PM] ����
		user_id = mgr_id;										//�ۼ��� ���
		user_name = mgr_name;									//�ۼ��� �̸�
		rec = rec_id+"/"+rec_name+";";							//������ 
		subject = "["+pjt_code+" "+pjt_name+"] ������ �۾����� ����";	//����
		String bon_path = "/post/"+user_id+"/text_upload";		//�����н�
		String filename = pid;									//�������� ���ϸ�

		//2.���ڿ������� ������
		Statement stmt = null;
		stmt = con.createStatement();
		String letter="";
			letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
			letter += pid+"','"+subject+"','"+user_id+"','"+user_name+"','"+write_date+"','"+rec_id+"','"+"0"+"','"+delete_date+"')";
		stmt.executeUpdate(letter);	
		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + user_id + "','" + user_name + "','" + write_date + "','" + rec + "','" + "0" + "','";
			master += "email" + "','" + "CFM" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";
		stmt.executeUpdate(master);

		//3.�������� �����
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>����۾����û���</title></head>";
			content += "<body>";
			content += "<h3>����۾� ���û���</h3>";
			content += "<ul>";
			content += "<li>�����̸� : "+pjt_code+" "+pjt_name+"</li>";
			content += "<li>����̸� : "+node_code+" "+node_name+"</li>";
			content += "<li>��ȹ�Ⱓ : "+psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8)+" ~ "+ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8)+"</li>";
			if(csd.length() != 0) 
				content += "<li>�����Ⱓ : "+csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8)+" ~ "+ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8)+"</li>";
			content += "<li>�������� : "+rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8)+" ~ "+"</li>";
			content += "<li>���û��� : <pre>"+remark+"</pre></li>";
			content += "</ul>";
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//����� path
		write.setFilepath(path);												//directory�����ϱ�
		write.WriteHanguel(path,filename,content);								//���� ���Ϸ� �����ϱ�

		stmt.close();
	}

	/*******************************************************************
	* �ش���/�ش�STEP weight�Է��ϱ�
	*******************************************************************/
	public void updateWeight(String pjt_code,String parent_node,String child_node,String weight) throws Exception
	{
		String query="",update="",phase="";
		double sw=0.0,pw=0.0,tw=0.0;		//step weight, phase weight, ��ü weight
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
	
		//0.STEP�� �Էµ� weight��
		sw = Double.parseDouble(weight);

		//1.STEP�� Activity��ü weight�� ���Ѵ�.
		query  = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"' ";
		query += "and child_node !='"+child_node+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			sw += rs.getDouble("weight");
		}
		
		//2.PHASE�ڵ� ã��
		query  = "SELECT parent_node FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and child_node = '"+parent_node+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			phase = rs.getString("parent_node");
		}

		//3.PHASE�� Activity��ü weight�� ���Ѵ�.
		query  = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' ";
		query += "and child_node !='"+parent_node+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			pw += rs.getDouble("weight"); 
		}
		pw += sw;		//atcivity�� �����κп� �ش�Ǵ� step
		
		//4.STEP weight�� �Է��ϱ�
		update = "UPDATE pjt_schedule set weight='"+sw+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"'";
		stmt.executeUpdate(update);

		//5.PHASE weight�� �Է��ϱ�
		update = "UPDATE pjt_schedule set weight='"+pw+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+phase+"'";
		stmt.executeUpdate(update);

		//6.�ش���� ��üphase weight���ϱ�
		query  = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no='1' and child_node != '"+phase+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			tw = rs.getDouble("weight");
		}
		tw += pw;

		//7.��ü weight�� �Է��ϱ�
		update = "UPDATE pjt_schedule set weight='"+tw+"' ";
		update += "where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);
	
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();	
	}

	/******************************************************************************
	// ID�� ���ϴ� �޼ҵ�
	******************************************************************************/
	private String getID()
	{
		com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("000");	
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		String y = first.format(now);
		String s = last.format(now);	
		String ID = y + nmf.toDigits(Integer.parseInt(s));
		return ID;
	}
}
