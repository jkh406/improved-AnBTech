package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtNodeAppDAO
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
	public pjtNodeAppDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtNodeAppDAO() 
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
		//1.�迭�����
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][23];

		//2. �迭�� ��´�.
		saveItemsArray(pjt_code,level_no,parent_node);

		//3.ArrayList�� ���
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		String node_name="";
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
			
			if(item[i][5].equals("3"))		//pjt_cod,parent_node,child_node
				node_name = "<a href=\"javascript:detailNode('"+item[i][1]+"','"+item[i][3]+"','"+item[i][4]+"');\">"+item[i][4]+" "+item[i][6]+"</a>";
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
	 * �ش������ ����׸��� �迭���� ArrayList�� ��´� : Gantt Chart�� [������/��ȹ�� or  ������]
	 *********************************************************************/
	public ArrayList getPjtGanttChart(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//1.�迭�����
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][23];

		//2. �迭�� ��´�.
		saveItemsArray(pjt_code,level_no,parent_node);

		//3.ArrayList�� ���
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		String node_name="";
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
			
			if(item[i][5].equals("1"))			//phase
				node_name = "<font color='blue'><b>"+item[i][4]+" "+item[i][6]+"</b></font>";
			else if(item[i][5].equals("2"))		//step
				node_name = "<font color='darkred'><b>"+item[i][4]+" "+item[i][6]+"</b></font>";
			else								//ativity
				node_name = "<font color='black'>"+item[i][4]+" "+item[i][6]+"</font>";
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
	 * �ش������ ����׸��� �迭���� ArrayList�� ��´� : Gantt Chart�� [������/��ȹ�� or  ������]
	 *********************************************************************/
	public ArrayList getPjtBarChart(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//1.�迭�����
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][23];

		//2. �迭�� ��´�.
		saveItemsArray(pjt_code,level_no,parent_node);

		//3.ArrayList�� ���
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		String node_name="";
		for(int i=0; i<an; i++) {
			table = new projectTable();

			table.setPid(item[i][0]);	
			table.setPjtCode(item[i][1]);		
			table.setPjtName(item[i][2]);		
			table.setParentNode(item[i][3]);	
			table.setChildNode(item[i][4]);
			table.setLevelNo(item[i][5]);
			
			if(item[i][5].equals("1"))			//phase
				node_name = "<font color='blue'><b>"+item[i][4]+" "+item[i][6]+"</b></font>";
			else if(item[i][5].equals("2"))		//step
				node_name = "<font color='darkred'><b>"+item[i][4]+" "+item[i][6]+"</b></font>";
			else								//ativity
				node_name = "<font color='black'>"+item[i][4]+" "+item[i][6]+"</font>";
			table.setNodeName(node_name);

			table.setWeight(Double.parseDouble(item[i][7]));
			table.setUserId(item[i][8]);
			table.setUserName(item[i][9]);
			table.setPjtNodeMbr(item[i][10]);
			table.setPlanStartDate(item[i][11]);
			table.setPlanEndDate(item[i][12]);
			table.setChgStartDate(item[i][13]);			
			table.setChgEndDate(item[i][14]);

			table.setRstStartDate(item[i][15]);					//����������
			table.setRstEndDate(item[i][16]);					//����������
			//���� �����Է��� ã��
			if(item[i][16].length() == 0) {
				table.setRstEndDate(searchRstEndDate(item[i][1],item[i][4],item[i][5]));				
			}

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
	//	�����ۼ����̺��� �Է��� �ش���� ����� ���������� ã��
	//*******************************************************************/	
	public String searchRstEndDate (String pjt_code,String node_code,String level_no) throws Exception
	{
		com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//������ �������� ���ϱ�
		if(level_no.equals("0")) {				//������ü �ֱ� �����Է���	
			query  = "SELECT in_date FROM pjt_event ";
			query += "where pjt_code = '"+pjt_code+"' order by in_date DESC";
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				rtn = rs.getString("in_date");
			}
			rtn = str.repWord(rtn,"-","");
		} else {								//�ش����� �ֱ� �����Է��� 
			query  = "SELECT in_date FROM pjt_event ";
			query += "where pjt_code = '"+pjt_code+"' and node_code ='"+node_code+"' order by in_date DESC";
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				rtn = rs.getString("in_date");
			}
			rtn = str.repWord(rtn,"-","");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;	
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
			String node_status = rs.getString("node_status"); if(node_status == null) node_status = "";
			table.setNodeStatus(node_status);
			
			table.setProgress(rs.getDouble("progress"));
			String remark = rs.getString("remark"); if(remark == null) remark = "";
			table.setRemark(remark);
					
			table_list.add(table);
		}
		rs.close();
		stmt.close(); 
		return table_list;
		
	}

	//*******************************************************************
	// �ڽ��� ��ü ������ �������� ����LIST
	//*******************************************************************/	
	public ArrayList getProjectList (String login_id,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		query = "SELECT pjt_status,pjt_code,pjt_name FROM pjt_general where pjt_mbr_id like '%"+login_id+"%'";	
		query += " and pjt_status = '1'";
		query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 
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
	* ������������ ����۾��Ϸ� ���ν�  --------------> PM
	* �ٽ�:�����Ϸ���,����,�����ϼ�
	*******************************************************************/
	public void updateNodeApproval(String pid,String pjt_code,String parent_node,String child_node,
		String rst_start_date,String rst_end_date,String node_status) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		String result_cnt = schBO.getPeriodDate(rst_start_date,rst_end_date);		//�����ϼ�

		//�ش����� �����ڵ带 �ٲ��ش�.
		update = "UPDATE pjt_schedule set rst_end_date='"+rst_end_date+"',result_cnt='"+result_cnt;
		update += "',node_status='"+node_status+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//�ش��尡 �ش�STEP�� ������ Activity�� ���Ϸ��̸� �ش�STEP�� �Ϸ������� �Է��Ѵ�.
		updateStepApproval(pjt_code,parent_node,child_node,rst_start_date,rst_end_date);


		stmt.close();
	}

	/*******************************************************************
	* �ش��尡 �ش�STEP�� ������ Activity�� ���Ϸ��̸� �ش�STEP�� �Ϸ������� �Է��Ѵ�.
	*******************************************************************/
	public void updateStepApproval(String pjt_code,String parent_node,String child_node,
		String rst_start_date,String rst_end_date) throws Exception
	{
		String query="",update="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = null;
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

		//3.���� ��ϵ� ������ Activity�� ���� �ľ��Ѵ�.
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"' and chg_cnt != 0";
		rs = stmt.executeQuery(query);
		rs.next();
		int ChgCnt = rs.getInt(1);

		//4.���� ��ϵ� Activity�� ������ �迭�� ��´�. 
		//  �׸��� null�̸� �Ķ���� ���ڸ� ��´�.
		data = new String[ActCnt][2];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+parent_node+"'";
		rs = stmt.executeQuery(query);
		int n = 0, rst = 0;	//�迭��ȣ,������������ null�ΰ���
		String cnode = "";				//�ڳ��
		while(rs.next()) {
			cnode = rs.getString("child_node");

			data[n][0] = rs.getString("rst_start_date");
			if(cnode.equals(child_node) && (data[n][0] == null)) data[n][0] = rst_start_date;
			if(data[n][0].length() == 0) data[n][0] = "0";	//""���� �Ѿ� ������

			data[n][1] = rs.getString("rst_end_date");
			if(cnode.equals(child_node) && (data[n][1] == null)) data[n][1] = rst_end_date;
			else if(data[n][1] == null) rst++;
			if(data[n][1].length() == 0) data[n][1] = "0";	//""���� �Ѿ� ������
			n++;
		}

		//5.�Ķ���� ������ ���Ͽ� ���������� �ƴ��� �Ǵ��Ѵ�. [�������� �ƴϸ� return, �������̸� ���]
		if(rst > 0) {						//������ �Էµ� �������� �������� �ƴ�
			stmt.close(); rs.close(); return; 
		}				
		

		//5.Activity���� �����ϰ� �������� ���Ѵ�.
		String[][] comDate = new String[2][2];	//������[����,����], ������[����,����]
		comDate = schBO.completeFirstLastDate(data);

		//6.���ڰ� 0�� �ƴϸ� ������[����,����] �Է��Ѵ�.
		//������[����,����]�� �ش� STEP�� �Է��Ѵ�.
		if(!comDate[0][0].equals("0")) {	
			update = "UPDATE pjt_schedule set rst_start_date='"+comDate[0][0]+"',rst_end_date='"+comDate[0][1];
			update += "', result_cnt='"+Integer.parseInt(comDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+parent_node+"' and level_no='2'";
			stmt.executeUpdate(update);
		}

		//7.�ش�STEP�� �������̸� �ش�PHASE�� �Է��Ѵ�.
		updatePhaseApproval(pjt_code,parent_node,rst_start_date,rst_end_date);
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();	
	}

	/*******************************************************************
	* �ش��尡 �ش�PHASE�� ������ STEP�� ���Ϸ��̸� �ش�STEP�� �Ϸ������� �Է��Ѵ�.
	*******************************************************************/
	public void updatePhaseApproval(String pjt_code,String child_node,
		String rst_start_date,String rst_end_date) throws Exception
	{
		String query="",update="",phase="";
		Statement stmt = null;
		ResultSet rs = null;
		String[][] data = null;
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
		
		//2.���� ��ϵ� STEP�� ������ �迭�� ��´�. 
		//  �׸��� null�̸� �Ķ���� ���ڸ� ��´�.
		data = new String[PhsCnt][4];
		query  = "SELECT * FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
		rs = stmt.executeQuery(query);
		int n = 0, rst = 0;	//�迭��ȣ,������������ null�ΰ���
		String cnode = "";				//�ڳ��
		while(rs.next()) {
			cnode = rs.getString("child_node");

			data[n][0] = rs.getString("rst_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = rst_start_date;
			if(data[n][0].length() == 0) data[n][0] = "0";	//""���� �Ѿ� ������

			data[n][1] = rs.getString("rst_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = rst_end_date;
			else if(data[n][1].length() == 0) rst++;
			if(data[n][1].length() == 0) data[n][1] = "0";	//""���� �Ѿ� ������	
			n++;
		}

		//3.�Ķ���� ������ ���Ͽ� ���������� �ƴ��� �Ǵ��Ѵ�. [�������� �ƴϸ� return, �������̸� ���]
		if(rst > 0) {						//������ �Էµ� �������� �������� �ƴ�
			stmt.close(); rs.close(); return; 
		}				
	
		//4.Step���� �����ϰ� �������� ���Ѵ�.
		String[][] comDate = new String[2][2];	//������[����,����], ������[����,����]
		comDate = schBO.completeFirstLastDate(data);
			
		//5.���ڰ� 0�� �ƴϸ� �������� �Է��Ѵ�.
		//������[����,����]�� �ش� STEP�� �Է��Ѵ�.
		if(!comDate[0][0].equals("0")) {	
			update = "UPDATE pjt_schedule set rst_start_date='"+comDate[0][0]+"',rst_end_date='"+comDate[0][1];
			update += "', result_cnt='"+Integer.parseInt(comDate[0][2]);
			update += "' where pjt_code='"+pjt_code+"' and child_node='"+phase+"' and level_no='1'";
			stmt.executeUpdate(update);
		}

		//---------------------------------------------------
		//6.������ü �����Ⱓ,�ϼ� ���ϱ�
		//---------------------------------------------------
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
		n = 0; rst = 0;
		while(rs.next()) {
			data[n][0] = rs.getString("rst_start_date");	if(data[n][0] == null) data[n][0] = "";
			if(cnode.equals(child_node) && (data[n][0].length() == 0)) data[n][0] = rst_start_date;
			
			data[n][1] = rs.getString("rst_end_date");		if(data[n][1] == null) data[n][1] = "";
			if(cnode.equals(child_node) && (data[n][1].length() == 0)) data[n][1] = rst_end_date;
			else if(data[n][1].length() == 0) rst++;
			n++;
		}
		data[n][0] = comDate[0][0];
		data[n][1] = comDate[0][1];

		if(rst > 0) {
			stmt.close(); rs.close(); return;
		}

		String[][] pjtDate = new String[1][3];	//������[����,����],���ϼ�
		pjtDate = schBO.completeFirstLastDate(data);

		update = "UPDATE pjt_schedule set result_cnt='"+Integer.parseInt(pjtDate[0][2]);
		update += "' and result_start_date='"+pjtDate[0][0]+"' and result_end_date='"+pjtDate[0][1]+"'";
		update += "' where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* ������������ ����۾��Ϸ� ���ιݷ���  --------------> PM
	* �ٽ�: �����ڵ常 ���������� �ٲ��ֱ�
	*******************************************************************/
	public void updateNodeReject(String pid) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//�ش����� �����ڵ带 �ٲ��ش�.
		update = "UPDATE pjt_schedule set node_status='1' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*********************************************************************
	 �����ιݷ��� ������ڿ��� ���ڿ������� ������ : �����νô� ���� 
	*********************************************************************/
	public void sendMailToUser(String mgr_id,String mgr_name,String rec_id,String rec_name,
		String pjt_code,String pjt_name,String node_code,String node_name,
		String psd,String ped,String csd,String ced,String rsd,String red,String remark) throws Exception 
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
		subject = "["+pjt_name+"]����/["+node_name+"]��� ������ �ݷ�����";	//����
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
			content += "<li>�������� : "+rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8)+" ~ "+red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8)+"</li>";
			content += "<li>�ݷ����� : <pre>"+remark+"</pre></li>";
			content += "</ul>";
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//����� path
		write.setFilepath(path);												//directory�����ϱ�
		write.WriteHanguel(path,filename,content);								//���� ���Ϸ� �����ϱ�

		stmt.close();
	}
	/*******************************************************************
	* �ش� ����۾��Ϸ� ����/�ݷ��� ���� �ڸ�Ʈ  --------------> PM
	* �ٽ�:�����Ϸ��ϸ� ���ֱ�
	*******************************************************************/
	public void nodeApprovalRemark(String pid,String pjt_code,String child_node,String remark,String tag) throws Exception
	{
		String update = "",query="";
		Statement stmt = null;
		stmt = con.createStatement();

		//�ڸ�Ʈ�� �ش� ���ο�û remark�� �Է��ϱ�
		if(tag.equals("A")) {						//����
			update = "UPDATE pjt_event set wm_type='A',remark='"+remark+"' where pid='"+pid+"'";
			stmt.executeUpdate(update);
		} else if(tag.equals("R")) {				//�ݷ�
			update = "UPDATE pjt_event set wm_type='R',remark='"+remark+"' where pid='"+pid+"'";
			stmt.executeUpdate(update);
		}

		stmt.close();
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

