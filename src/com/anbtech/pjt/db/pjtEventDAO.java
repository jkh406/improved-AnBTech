package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtEventDAO
{
	private Connection con;
	com.anbtech.pjt.business.pjtScheduleBO schBO = new com.anbtech.pjt.business.pjtScheduleBO(); //�ϼ����
	com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();	//���Ϸ� ���[����]
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����ó��
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��

	private String[][] item = null;				//���μ��������� �迭�� ���
	private int an = 0;							//items�� �迭 ����
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public pjtEventDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtEventDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ� [�ش����/�ش����� ��ü����]
	//*******************************************************************/
	private int getTotalCount(String pjt_code,String node_code,String pjtWord,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"'";
		query += " and wm_type like '%"+pjtWord+"%'";
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
	// �ش����/�ش����� �����̷�����
	//*******************************************************************/	
	public ArrayList getEventList (String login_id,String pjt_code,String node_code,String pjtWord,String sItem,String sWord,
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
		total_cnt = getTotalCount(pjt_code,node_code,pjtWord,sItem,sWord);

		//�������� ���ϱ�
		String todate = anbdt.getDate();
			
		//query���� �����
		query = "SELECT * FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"'";
		query += " and wm_type like '%"+pjtWord+"%'";
		query += " and "+sItem+" like '%"+sWord+"%' order by in_date desc"; 
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
				table.setNodeName(rs.getString("node_name"));
				table.setProgress(rs.getDouble("progress"));

				String user_id = rs.getString("user_id");
				table.setUserId(user_id);
				
				table.setUserName(rs.getString("user_name"));

				String in_date = rs.getString("in_date");
				table.setInDate(in_date);

				String wm_type = rs.getString("wm_type");
				table.setWmType(wm_type);
				table.setEvtContent(rs.getString("evt_content"));
				table.setEvtNote(rs.getString("evt_note"));
				table.setEvtIssue(rs.getString("evt_issue"));

				//����,����,�������� ǥ�� [��,����/������ ���Ͽ� ���� �ۼ��ڸ��� ������]
				String subMod="",subDel="",subView="";
				if(user_id.equals(login_id) && todate.equals(in_date) && !(wm_type.equals("A")) && !(wm_type.equals("R")) ) {
					subView = "<a href=\"javascript:eventView('"+pid+"','"+wm_type+"');\">����</a>";
					subMod = "<a href=\"javascript:eventModify('"+pid+"');\">����</a>";
					//subDel = "<a href=\"javascript:eventDelete('"+pid+"');\">����</a>";
				} 
				else {
					subView = "<a href=\"javascript:eventView('"+pid+"','"+wm_type+"');\">����</a>";
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
	// �ش���� �ش����� ��������󼼺���
	//*******************************************************************/	
	public ArrayList getEventRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_event where pid='"+pid+"'";	
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

	/*******************************************************************
	* �ְ�/���� ���� �Է��ϱ�
	*******************************************************************/
	public void inputEvent(String pjt_code,String pjt_name,String node_code,String node_name,String progress,
		String user_id,String user_name,String in_date,String wm_type,String evt_content,
		String evt_note,String evt_issue,String node_status) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//������ȣ ã��
		String pid = getID();

		//�����Է��ϱ� : pjt_event
		input = "INSERT INTO pjt_event(pid,pjt_code,pjt_name,node_code,node_name,progress,user_id,";
		input += "user_name,in_date,wm_type,evt_content,evt_note,evt_issue) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+node_code+"','"+node_name+"','"+progress+"','"+user_id+"','";
		input += user_name+"','"+in_date+"','"+wm_type+"','"+evt_content+"','"+evt_note+"','"+evt_issue+"')";
		stmt.executeUpdate(input);

		//������ �Է��ϱ� : pjt_schedule
		String update = "UPDATE pjt_schedule set progress='"+progress+"',node_status='"+node_status+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//step,phase,������ü ������ �����ϱ� : pjt_schedule
		updateProgress(pjt_code,node_code,progress);

		//������û��׾��� ������ ��� �����¹� ������������ �Է��Ѵ�.
		updateStatus(pjt_code,node_code);

		//step,phase�� ���������� �����ϱ� : pjt_schedule
		in_date = str.repWord(in_date,"-","");
		update = "UPDATE pjt_schedule set rst_end_date='"+in_date+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		updateResultEndDate(pjt_code,node_code,in_date);

		stmt.close();
	}
	/*******************************************************************
	* �ְ�/���� ���� �����ϱ�
	*******************************************************************/
	public void updateEvent(String pid,String pjt_code,String node_code,String progress,
		String in_date,String wm_type,String evt_content,String evt_note,String evt_issue) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//�������� �����ϱ� [pjt_event]
		update = "UPDATE pjt_event set progress='"+progress+"',in_date='"+in_date+"',wm_type='"+wm_type;
		update += "',evt_content='"+evt_content+"',evt_note='"+evt_note+"',evt_issue='"+evt_issue;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		//������ �����ϱ� : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+progress+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//step,phase,������ü ������ �����ϱ� : pjt_schedule
		updateProgress(pjt_code,node_code,progress);

		//step,phase�� ���������� �����ϱ� : pjt_schedule
		in_date = str.repWord(in_date,"-","");
		update = "UPDATE pjt_schedule set rst_end_date='"+in_date+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		updateResultEndDate(pjt_code,node_code,in_date);

		stmt.close();
	}
	/*******************************************************************
	* �ְ�/���� ���� �����ϱ�
	*******************************************************************/
	public void deleteEvent(String pid) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String delete = "",query="",update="";
		String pjt_code="",node_code="",progress="";
		stmt = con.createStatement();

		//�ֱ��� ������ ã�� : pjt_event
		query = "SELECT pjt_code,node_code FROM pjt_event where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			pjt_code=rs.getString("pjt_code");
			node_code=rs.getString("node_code");
		}
		query = "SELECT progress FROM pjt_event where pjt_code='"+pjt_code+"' and node_code='"+node_code+"' ";
		query += "order by in_date DESC";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			progress=rs.getString("progress");
		}

		//�����ϱ�
		delete = "DELETE from pjt_event where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		//������ �����ϱ� : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+progress+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//step,phase,������ü ������ �����ϱ� : pjt_schedule
		updateProgress(pjt_code,node_code,progress);

		stmt.close();
		rs.close();
	}

	/*******************************************************************
	* STEP,PHASE ������ ���Ͽ� update�ϱ� : pjt_schedule
	*******************************************************************/
	public void updateProgress(String pjt_code,String child_node,String progress) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String delete = "",query="",update="";
		String step="",phase="";
		double sp=0.0,pp=0.0,tp=0.0;			//Step������, Phase������, ����������
		double sw=0.0,pw=0.0,tw=0.0;			//Step weight, Phase weight, ����weight
		stmt = con.createStatement();

		//------------- step/phase �ڵ� ã�� ---------------//
		//1.step�ڵ�[parent_node]ã�� : pjt_schedule
		query = "SELECT parent_node FROM pjt_schedule where pjt_code='"+pjt_code+"' and child_node='"+child_node+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			step=rs.getString("parent_node");
		}

		//2.phase�ڵ� ã�� : pjt_schedule
		query = "SELECT parent_node FROM pjt_schedule where pjt_code='"+pjt_code+"' and child_node='"+step+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			phase=rs.getString("parent_node");
		}

		//------------- step/phase/��ü weight ã�� ---------------//
		//3.�ش�step�� weight���ϱ� : pjt_schedule
		query = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and child_node='"+step+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			sw=rs.getDouble("weight");
		}

		//4.�ش�phase�� weight���ϱ� : pjt_schedule
		query = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and child_node='"+phase+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			pw=rs.getDouble("weight");
		}

		//5.�ش������ weight���ϱ� : pjt_schedule
		query = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and level_no='0'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			tw=rs.getDouble("weight");
		}

		//------------- step ������ ã�� ---------------//
		//6.�ش�step�� �ش�Activity������ ���ϱ� : pjt_schedule
		query = "SELECT weight FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+step+"' and child_node ='"+child_node+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			sp=(rs.getDouble("weight") / sw) * Double.parseDouble(progress);
		}

		//7.�ش�step�� ������ ���ϱ� [����activity����] : pjt_schedule
		query = "SELECT weight,progress FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+step+"' and child_node !='"+child_node+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			sp +=(rs.getDouble("weight") / sw) * rs.getDouble("progress");
		}

		//------------- phase ������ ã�� ---------------//
		//8.�ش�Phase�� �ش�Activity������ ���ϱ� : pjt_schedule
		pp = sw / pw * sp;
		
		//9.�ش�Phase�� ������ ���ϱ� [����activity����] : pjt_schedule
		query = "SELECT weight,progress FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+phase+"' and child_node !='"+step+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			pp +=(rs.getDouble("weight") / pw) * rs.getDouble("progress");
		}

		//------------- ��ü ������ ã�� ---------------//
		//10.�ش�Phase�� �ش�Activity������ ���ϱ� : pjt_schedule
		tp = pw / tw * pp;
		
		//11.�ش�Phase�� ������ ���ϱ� [����activity����] : pjt_schedule
		query = "SELECT weight,progress FROM pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and level_no='1' and child_node !='"+phase+"'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			tp +=(rs.getDouble("weight") / tw) * rs.getDouble("progress");
		}

		//------------- ������ ��� ---------------//
		//step ������ �����ϱ� : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+sp+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+step+"'";
		stmt.executeUpdate(update);

		//phase ������ �����ϱ� : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+pp+"' ";
		update += "where pjt_code='"+pjt_code+"' and child_node='"+phase+"'";
		stmt.executeUpdate(update);

		//��ü ������ �����ϱ� : pjt_schedule
		update = "UPDATE pjt_schedule set progress='"+tp+"' ";
		update += "where pjt_code='"+pjt_code+"' and level_no='0'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// ������û��׾��� ������ ��� �����¹� ������������ �Է��Ѵ�.
	//*******************************************************************/	
	public void	updateStatus(String pjt_code,String node_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",update="",node_status="";	
		stmt = con.createStatement();

		//����� ���� �˱�
		query = "SELECT node_status FROM pjt_schedule where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		rs = stmt.executeQuery(query);

		if(rs.next()) { node_status = rs.getString("node_status");}

		//�����°� '0:������'�϶��� ���
		if(!node_status.equals("0")) { stmt.close(); rs.close(); return; }

		//������ '1:������'���� �ٲٱ�
		update = "UPDATE pjt_schedule set node_status='1' where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//���������� �Է��ϱ�
		String todate = anbdt.getDateNoformat();
		update = "UPDATE pjt_schedule set rst_start_date='"+todate+"' ";
		update +="where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		//������ step,phase�� ���������� �Է��ϱ�
		updateResultStartDate(pjt_code,node_code,todate);

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

	//*******************************************************************
	// STEP,PHASE�� ���� �����ϸ� �Է��ϱ�
	//*******************************************************************/	
	public void	updateResultEndDate(String pjt_code,String child_node,String rst_end_date) throws Exception
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

		//6.�ش� Activity�� ���Ḹ �����ϱ� [�ش����� STEP�� ���������Ͽ� �Է��ϱ�]
		data = new String[ActCnt+1];
		query  = "SELECT rst_end_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+step+"' and level_no = '3'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_end_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rst_end_date;
		rstDate = schBO.completeLastDate(data);	//������������ ����ū����
		update = "UPDATE pjt_schedule set rst_end_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and child_node = '"+step+"'";
		stmt.executeUpdate(update);

		//6.�ش� Step�� �����ϸ� �����ϱ� [�ش����� PHASE�� ���������Ͽ� �Է��ϱ�]
		data = new String[StpCnt+1];
		query  = "SELECT rst_end_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and parent_node = '"+phase+"' and level_no = '2'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_end_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rstDate;
		rstDate = schBO.completeLastDate(data);	//������������ ����ū����
		update = "UPDATE pjt_schedule set rst_end_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and child_node = '"+phase+"'";
		stmt.executeUpdate(update);

		//7.�ش� Phase�� �����ϸ� �����ϱ� [�ش����� ������ ���������Ͽ� �Է��ϱ�]
		data = new String[PhsCnt+1];
		query  = "SELECT rst_end_date FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and level_no = '1'";
		rs = stmt.executeQuery(query);
		n = 0;
		while(rs.next()){
			rdate = rs.getString("rst_end_date"); if(rdate == null) rdate = "";
			if(rdate.length() != 0) { data[n] = rdate; n++; }
		}
		data[n] = rstDate;
		rstDate = schBO.completeLastDate(data);	//������������ ����ū����
		update = "UPDATE pjt_schedule set rst_end_date='"+rstDate+"' ";
		update += "where pjt_code = '"+pjt_code+"' and level_no = '0'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// �ش���� Activity����Ʈ �����ϱ�
	//*******************************************************************/	
	public ArrayList getPjtActivityRead (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' and level_no = '3' order by child_node asc";	
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
	// �ش���� Activity����Ʈ �����ϱ�
	//*******************************************************************/	
	public ArrayList getPjtNodeRead (String pjt_code,String child_node) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_schedule where pjt_code='"+pjt_code+"' and child_node = '"+child_node+"' order by child_node asc";	
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
	/*******************************************************************
	* ����۾��Ϸ� ���ο�û�ϱ�
	*******************************************************************/
	public String nodeAppReq(String pjt_code,String pjt_name,String node_code,String node_name,
		String user_id,String user_name,String evt_content,String evt_note,String evt_issue) throws Exception
	{
		String update = "",query="",red="",node_status="",rtnData="Y";
		ResultSet rs = null;
		Statement stmt = null;
		stmt = con.createStatement();

		//�ش��� ���ο�û ���� �˾ƺ���
		query = "SELECT rst_end_date,node_status FROM pjt_schedule where pjt_code='"+pjt_code;
		query += "' and child_node = '"+node_code+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			red = rs.getString("rst_end_date");	if(red == null) red = "";
			node_status = rs.getString("node_status");
		}

		//�˻��ϱ�
		if(node_status.equals("2")) {		//��� �Ϸ����
			stmt.close();
			rs.close();
			rtnData = "A";
			return rtnData;
		}
		else rtnData = "Y";

		//���Ϸ� ���ο�û������ PM���� ���ڿ������� �˸���
		sendMailToPM(user_id,user_name,pjt_code,pjt_name,node_code,node_name,evt_content,evt_note,evt_issue);

		stmt.close();
		rs.close();
		return rtnData;
	}

	/*********************************************************************
	 	�۾��Ϸ᳻���� PM���� ���ڿ������� ������
	*********************************************************************/
	public void sendMailToPM(String u_id,String u_name,String pjt_code,String pjt_name,String node_code,String node_name,
		String evt_content,String evt_note,String evt_issue) throws Exception 
	{	
		String pid = getID();								//������ȣ
		String subject = "";								//����
		String user_id = "", user_name = "", rec = "";		//�ۼ��� ���,�̸�,������List
		String write_date = anbdt.getTime();				//���ڿ��� ��������
		String delete_date = anbdt.getAddMonthNoformat(1);	//������������

		//1.�ۼ���[����PM] ����
		user_id = u_id;											//�ۼ��� ���
		user_name = u_name;										//�ۼ��� �̸�
		subject = "���Ϸ� ���ο�û";								//����
		String bon_path = "/post/"+user_id+"/text_upload";		//�����н�
		String filename = pid;									//�������� ���ϸ�

		//2.����PM ���ϱ�
		rec = searchPjtPM(pjt_code)+";";						//������ [���/�̸�;]
		String rec_id = rec.substring(0,rec.indexOf("/"));
//		rec = rec_id+"/"+rec_name+";";							//������ 

		//3.���ڿ������� ������
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

		//4.�������� �����
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>����۾� �Ϸ���� ��û</title></head>";
			content += "<body>";
			content += "<h3>����۾� �Ϸ᳻��</h3>";
			content += "<ul>";
			content += "<li>�����̸� : "+pjt_code+" "+pjt_name+"</li>";
			content += "<li>����̸� : "+node_code+" "+node_name+"</li>";
			content += "<li>�ֿ䳻�� : <pre>"+evt_content+"</pre></li>";
			content += "<li>�ֿ乮�� : <pre>"+evt_note+"</pre></li>";
			content += "<li>�ֿ��̽� : <pre>"+evt_issue+"</pre></li>";
			content += "</ul>";
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//����� path
		write.setFilepath(path);												//directory�����ϱ�
		write.WriteHanguel(path,filename,content);								//���� ���Ϸ� �����ϱ�

		stmt.close();
	}
	/*******************************************************************
	* �ش������ PM���ϱ�
	*******************************************************************/
	public String searchPjtPM(String pjt_code) throws Exception
	{
		String query="",rtnData="";
		ResultSet rs = null;
		Statement stmt = null;
		stmt = con.createStatement();

		query = "SELECT pjt_mbr_id FROM pjt_general where pjt_code='"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			rtnData = rs.getString("pjt_mbr_id");	
		}

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



