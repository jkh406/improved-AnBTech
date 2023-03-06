package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtIssueDAO
{
	private Connection con;
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����ó��
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public pjtIssueDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtIssueDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ� [�̽�/�ش���� ��ü����]
	//*******************************************************************/
	private int getTotalCount(String pjt_code,String issue_status,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_issue where pjt_code='"+pjt_code+"'";
		query += " and issue_status like '%"+issue_status+"%'";
		query += " and "+sItem+" like '%"+sWord+"%'"; 
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
	// �ش������ �̽� List
	//*******************************************************************/	
	public ArrayList getIssueList (String pjt_code,String issue_status,String sItem,String sWord,
		String page,int max_display_cnt) throws Exception
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
		total_cnt = getTotalCount(pjt_code,issue_status,sItem,sWord);

		//�������� ���ϱ� (����/���� �㰡�����Ǵ�)
		String todate = anbdt.getDate();		//yyyy-MM-dd
			
		//query���� �����
		query = "SELECT * FROM pjt_issue where pjt_code='"+pjt_code+"'";
		query += " and issue_status like '%"+issue_status+"%'";
		query += " and "+sItem+" like '%"+sWord+"%'"; 
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

				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));
				table.setUsers(rs.getString("users"));	

				String in_date = rs.getString("in_date");    //yyyy-MM-dd
				table.setInDate(in_date);

				table.setBookDate(rs.getString("book_date"));
				table.setSolution(rs.getString("solution"));
				table.setContent(rs.getString("content"));	
				String sol_date = rs.getString("sol_date");
				table.setSolDate(sol_date);
				String note_s = rs.getString("issue_status");
				table.setNoteStatus(note_s);

				//�̽� ���� �ۼ�
				String issue = rs.getString("issue");
				if(issue.length() > 30) issue = issue.substring(0,30)+" ...";
				String vissue = "";
				vissue = "<a href=\"javascript:issueSolution('"+pid+"');\">"+issue+"</a>";		//���ذ��
				if(note_s.equals("1")) {		//���°� 1 �̸� �ذ��
					vissue = "<a href=\"javascript:issueContent('"+pid+"');\">"+issue+"</a>";	//�ذ��
				}
				table.setIssue(vissue);

				//����,����,�������� ǥ�� [��,����/������ ���Ͽ� ���� �ۼ��ڸ��� ������]
				String subMod="",subDel="",subView="";
				if(todate.equals(in_date) && (note_s.equals("0"))) {		//�̽� �ۼ���
					subMod = "<a href=\"javascript:issueModify('"+pid+"');\">����[N]</a>";
					subDel = "<a href=\"javascript:issueDelete('"+pid+"');\">����[N]</a>";
				}else if(todate.equals(sol_date) && (note_s.equals("1"))) {		//�̽� �ۼ���
					subMod = "<a href=\"javascript:solModify('"+pid+"');\">����[S]</a>";
					subDel = "<a href=\"javascript:solRecovery('"+pid+"');\">���[S]</a>";
				}
				
				table.setView(subView);
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
	// �ش���� ������ �̽� ���� ����
	//*******************************************************************/	
	public ArrayList getIssueRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_issue where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));
				table.setUsers(rs.getString("users"));	
				table.setInDate(rs.getString("in_date"));
				table.setBookDate(rs.getString("book_date"));
				table.setIssue(rs.getString("issue"));
				table.setSolution(rs.getString("solution"));
				table.setContent(rs.getString("content"));
				table.setSolDate(rs.getString("sol_date"));
				table.setNoteStatus(rs.getString("issue_status"));	
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* �̽� ���� �ۼ��ϱ� : �űԵ��
	*******************************************************************/
	public void inputIssue(String pid,String pjt_code,String pjt_name,String node_code,String users,String in_date,
		String book_date,String issue,String solution,String issue_status) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		input = "INSERT INTO pjt_issue(pid,pjt_code,pjt_name,node_code,users,in_date,book_date,issue,solution,content,sol_date,";
		input += "issue_status) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+node_code+"','"+users+"','"+in_date+"','"+book_date+"','";
		input += issue+"','"+solution+"','','','"+issue_status+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}
	/*******************************************************************
	* �̽� ���� �ۼ��ϱ� : �����ϱ�
	*******************************************************************/
	public void updateIssue(String pid,String node_code,String book_date,String users,String issue,String solution) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		update = "UPDATE pjt_issue set node_code='"+node_code+"',book_date='"+book_date;
		update +="',users='"+users+"',issue='"+issue+"',solution='"+solution;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}
	/*******************************************************************
	* �̽� �ذ� �ۼ��ϱ� : �����ϱ�
	*******************************************************************/
	public void updateContent(String pid,String content,String sol_date,String issue_status) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		update = "UPDATE pjt_issue set content='"+content+"',sol_date='"+sol_date+"',issue_status='"+issue_status;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}
	/*******************************************************************
	* �̽� �ذ��ۼ� ����ϱ� 
	*******************************************************************/
	public void updateRecovery(String pid) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		update = "UPDATE pjt_issue set content='',sol_date='',issue_status='0' ";
		update += "where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*******************************************************************
	* �̽� ���� �ۼ��ϱ� : �����ϱ�
	*******************************************************************/
	public void deleteIssue(String pid) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		delete = "DELETE from pjt_issue where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}

	//*******************************************************************
	// �ش������ ������/����������� ���(activity) �����ϱ� 
	// �����ֱٿ� �Է��� ������ �������� : note�Է� �غ��۾���
	//*******************************************************************/	
	public ArrayList getWorkActivity (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' ";
		query += "and (node_status='1' or node_status='11') and level_no='3' order by child_node ASC";
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
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

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش������ ��ü��� ����Ʈ
	// note�Է� �غ��۾���
	//*******************************************************************/	
	public ArrayList getNodeList (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' and level_no != '0' ";
		query += "order by level_no,child_node ASC";
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new projectTable();
										
				table.setChildNode(rs.getString("child_node"));
				table.setNodeName(rs.getString("node_name"));
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش������ ������/����������� ���(activity) �����ֱ� ������ �������� 
	// �����ֱٿ� �Է��� ������ �������� : note�Է� �غ��۾���
	//*******************************************************************/	
	public ArrayList getLastWork (String pjt_code,String node_code,String in_date) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"' ";
		query += "and in_date='"+in_date+"' order by pid DESC";
		rs = stmt.executeQuery(query);

		//������ ���
		if(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));	
				table.setNodeName(rs.getString("node_name"));
				table.setProgress(rs.getDouble("progress"));
				table.setUserId(rs.getString("user_id"));	
				table.setUserName(rs.getString("user_name"));
				table.setInDate(rs.getString("in_date"));
				table.setWmType(rs.getString("wm_type"));
				table.setEvtContent(rs.getString("evt_content"));
				table.setEvtNote(rs.getString("evt_note"));
				table.setEvtIssue(rs.getString("evt_issue"));
				String remark = rs.getString("remark"); if(remark == null) remark = "";
				table.setRemark(remark);	
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش������ ������/����������� ���(activity) �����ֱ� �Է��� ��������
	// �����ֱٿ� �Է��� ������ �������� : note�Է� �غ��۾���
	//*******************************************************************/	
	public String getLastDate (String pjt_code,String node_code) throws Exception
	{
		//���� �ʱ�ȭ
		String in_date="";	
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();

		//query���� �����
		query = "SELECT in_date FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"' ";
		query += "order by pid DESC";
		rs = stmt.executeQuery(query);

		//������ ���
		if(rs.next()) { 
			in_date = rs.getString("in_date");					
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return in_date;
	}

	//*******************************************************************
	// �ش������ ������/����������� ���(activity) ���ڸ� �����ϱ�
	// ���ں� �Է��� ������ �������� : note�Է� �غ��۾���
	//*******************************************************************/	
	public ArrayList getInDate (String pjt_code,String node_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"' ";
		query += "order by pid DESC";
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setNodeCode(rs.getString("node_code"));	
				table.setNodeName(rs.getString("node_name"));
				table.setProgress(rs.getDouble("progress"));
				table.setUserId(rs.getString("user_id"));	
				table.setUserName(rs.getString("user_name"));
				table.setInDate(rs.getString("in_date"));
				table.setWmType(rs.getString("wm_type"));
				table.setEvtContent(rs.getString("evt_content"));
				table.setEvtNote(rs.getString("evt_note"));
				table.setEvtIssue(rs.getString("evt_issue"));
				String remark = rs.getString("remark"); if(remark == null) remark = "";
				table.setRemark(remark);	
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش���� ��� List��������
	//*******************************************************************/	
	public ArrayList getPjtMember (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		
		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_member where pjt_code='"+pjt_code+"' order by pjt_mbr_type ASC";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setPjtMbrType(rs.getString("pjt_mbr_type"));
				table.setMbrStartDate(rs.getString("mbr_start_date"));
				table.setMbrEndDate(rs.getString("mbr_end_date"));
				table.setMbrPoration(rs.getDouble("mbr_poration"));
				table.setPjtMbrId(rs.getString("pjt_mbr_id"));
				table.setPjtMbrName(rs.getString("pjt_mbr_name"));
				table.setPjtMbrJob(rs.getString("pjt_mbr_job"));
				table.setPjtMbrTel(rs.getString("pjt_mbr_tel"));
				table.setPjtMbrGrade(rs.getString("pjt_mbr_grade"));
				table.setPjtMbrDiv(rs.getString("pjt_mbr_div"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ������Ʈ �̸� ã��
	//*******************************************************************/	
	public String getProjectName(String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",rtnData="";	
		
		stmt = con.createStatement();
		
		//query���� �����
		query = "SELECT pjt_name FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 	
				rtnData = rs.getString("pjt_name");
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return rtnData;
	}

	/***************************************************************************
	 * ID�� ���ϴ� �޼ҵ�
	 **************************************************************************/
	public String getID()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}
}




