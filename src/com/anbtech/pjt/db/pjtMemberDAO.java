package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtMemberDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public pjtMemberDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtMemberDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	// �ش� PM�� ��ü ���� List [from pjt_general]
	//*******************************************************************/	
	public ArrayList getAllProjectList (String mgr_plm_id,String pjtWord,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query���� �����
		query = "SELECT * FROM pjt_general where pjt_mbr_id like '%"+mgr_plm_id+"%'";	
		query += " and pjt_status like '%"+pjtWord+"%'";
		query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 

		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش�����ڵ��� ��� QUERY�ϱ� 
	//*******************************************************************/	
	public ArrayList getProjectRead (String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_member where pjt_code='"+pjt_code+"' order by pjt_mbr_type asc";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new projectTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String pcode = rs.getString("pjt_code");
				table.setPjtCode(pcode);
				
				table.setPjtName(rs.getString("pjt_name"));	
				table.setPjtMbrType(rs.getString("pjt_mbr_type"));
				table.setMbrStartDate(rs.getString("mbr_start_date"));
				table.setMbrEndDate(rs.getString("mbr_end_date"));
				table.setMbrPoration(rs.getDouble("mbr_poration"));
				
				String sabun = rs.getString("pjt_mbr_id");
				table.setPjtMbrId(sabun);
				table.setPjtMbrName(rs.getString("pjt_mbr_name"));

				table.setPjtMbrJob(rs.getString("pjt_mbr_job"));
				table.setPjtMbrTel(rs.getString("pjt_mbr_tel"));
				table.setPjtMbrGrade(rs.getString("pjt_mbr_grade"));
				table.setPjtMbrDiv(rs.getString("pjt_mbr_div"));

				//���� or �������� ǥ�� [login_id�� ������������ ��츸 ����]
				String subMod="",subDel="";
				String man_sch = checkManSchedule(pcode,sabun);				//������ �Ҵ�Ǿ���.
				String pjt_pml = checkPjtPML (sabun);						//���� PM�ΰ�
				if(man_sch.equals("N") && pjt_pml.equals("N")) {			//����������,PM�ƴ�
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[����]</a>";
				} else if(man_sch.equals("N") && pjt_pml.equals("Y")) {		//��������,PM
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
				} else if(man_sch.equals("Y")) {							//��Ÿ
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[����]</a>";
				}
			
				table.setModify(subMod);
				table.setDelete(subDel);
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش��� QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	public ArrayList getMemberRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_member where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
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

	/*******************************************************************
	* �����η����� ���� �����ϱ� 
	*******************************************************************/
	public void inputMember(String pjt_code,String pjt_name,String pjt_mbr_type,String mbr_start_date,
		String mbr_end_date,String mbr_poration,String pjt_member,String pjt_mbr_job) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//�����⺻������ �����ڵ� �ٲ��ֱ�
		changeGeneralStatus(pjt_code,"0");

		//������ȣ ã��
		String pid = getID();

		//����� ã��
		String sabun = pjt_member.substring(0,pjt_member.indexOf("/"));

		//PM��������
		String[] man = new String[5];
		man = searchManinfo(sabun);
		String name="",tel="",grade="",div="";
		name=man[1]; tel=man[2]; grade=man[3]; div=man[4];

		input = "INSERT INTO pjt_member(pid,pjt_code,pjt_name,pjt_mbr_type,mbr_start_date,mbr_end_date,";
		input += "mbr_poration,pjt_mbr_id,pjt_mbr_name,pjt_mbr_job,pjt_mbr_tel,pjt_mbr_grade,pjt_mbr_div) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+pjt_mbr_type+"','"+mbr_start_date+"','"+mbr_end_date+"','";
		input += Double.parseDouble(mbr_poration)+"','"+sabun+"','"+name+"','"+pjt_mbr_job+"','"+tel+"','"+grade+"','"+div+"')";
		stmt.executeUpdate(input);

		//�⺻������ �ο��� �ݿ��ϱ�
		updateGeneralMember (pjt_code,"A");

		stmt.close();
	}

	//*******************************************************************
	//	���� �η±����� �⺻������ �����ڵ带 �ٲ��ֱ�
	//  1[PM] : S , 2 : 0 , else -- [�⺻����pjt_general)�� �����ڵ�ٲ�]
	//*******************************************************************/	
	public void changeGeneralStatus (String pjt_code,String pjt_status) throws Exception
	{
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM pjt_member ";
		query += "where pjt_code = '"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		//1�̸� �⺻����[pjt_general]�� �����ڵ带 "S"
		String update = "";
		if(cnt == 1) {			//�Է��Ҷ� [���� 1��]
			//�⺻����
			update = "UPDATE pjt_general set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
			//�����ڵ�����
			update = "UPDATE prs_project set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
			//������������
			update = "UPDATE pjt_status set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
		}
		else if(cnt == 2) {		//�����Ҷ� [������ ����2��]
			//�⺻����
			update = "UPDATE pjt_general set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
			//�����ڵ�����
			update = "UPDATE prs_project set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
			//�����ڵ�����
			update = "UPDATE pjt_status set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

	}

	/*******************************************************************
	* �η����� ���� �����ϱ� 
	*******************************************************************/
	public void updateMember(String pid,String pjt_code,String pjt_name,String pjt_mbr_type,
		String mbr_start_date,String mbr_end_date,String mbr_poration,String pjt_mbr_id,
		String pjt_mbr_name,String pjt_mbr_job,String pjt_mbr_tel,String pjt_mbr_grade,
		String pjt_mbr_div) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//�η����� �����ϱ� [pjt_member]
		update = "UPDATE pjt_member set pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"',pjt_mbr_type='"+pjt_mbr_type;
		update += "',mbr_start_date='"+mbr_start_date+"',mbr_end_date='"+mbr_end_date+"',mbr_poration='"+mbr_poration;
		update += "',pjt_mbr_id='"+pjt_mbr_id+"',pjt_mbr_name='"+pjt_mbr_name+"',pjt_mbr_job='"+pjt_mbr_job;
		update += "',pjt_mbr_tel='"+pjt_mbr_tel+"',pjt_mbr_grade='"+pjt_mbr_grade+"',pjt_mbr_div='"+pjt_mbr_div;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*******************************************************************
	* �η����� ���� �����ϱ�
	*******************************************************************/
	public void deleteMember(String pid) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//�����⺻������ �����ڵ� �ٲ��ֱ�
		String pjt_code = searchPjtCode(pid);
		changeGeneralStatus(pjt_code,"S");

		//1.������ϱ�
		String sabun = searchSabun(pid);

		//2.����PM���� �Ǵ��ϱ�
		String pjt_pml = checkPjtPML (sabun);

		//3.�⺻���� ���̺� �����ϱ� (����PM�̸� ���� �Ұ�)
		if(pjt_pml.equals("N")) {
			delete = "DELETE from pjt_member where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}

		//4.�⺻������ �ο����� �ݿ��Ѵ�.
		updateGeneralMember (pjt_code,"D");

		stmt.close();
	}
	
	//*******************************************************************
	//	�η� ���/������ ���ο��� �����⺻������ �����ο����� �ݿ��ϱ�
	//*******************************************************************/	
	public void updateGeneralMember (String pjt_code,String tag) throws Exception
	{
		String query = "",update="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//1.������ �⺻�������� �ο����� �ľ��Ѵ�.
		int gen_cnt = 0;
		query  = "SELECT mbr_exp FROM pjt_general where pjt_code = '"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			gen_cnt = Integer.parseInt(rs.getString("mbr_exp"));
		}

		//2.������ �η��������� �ο����� �ľ��Ѵ�
		int man_cnt = 0;
		query  = "SELECT COUNT(*) FROM pjt_member where pjt_code = '"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			man_cnt = rs.getInt(1);
		}

		//3.�ο����� ���Ͽ� �ο����� �ݿ��Ѵ�.
		if(tag.equals("A")) {			//�ο��߰�
			update = "UPDATE pjt_general set mbr_exp='"+man_cnt+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);	
		} else if(tag.equals("D")) {	//�ο�����
			update = "UPDATE pjt_general set mbr_exp='"+man_cnt+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);	
		}
	
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

	}

	//*******************************************************************
	//	pjt_code ���ϱ�
	//*******************************************************************/	
	public String searchPjtCode (String pid) throws Exception
	{
		String pjt_code = "";
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT pjt_code FROM pjt_member ";
		query += "where pid = '"+pid+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			pjt_code = rs.getString("pjt_code");
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return pjt_code;

	}
	//*******************************************************************
	//	�ش��ڰ� ������ �߰��Ǿ �Ǵ��ϱ�
	//*******************************************************************/	
	public String checkManSchedule (String pjt_code,String sabun) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and user_id = '"+sabun+"'";
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

	//*******************************************************************
	//	�ش������ȣ�� ��������ϱ�
	//*******************************************************************/	
	private String searchSabun(String pid) throws Exception
	{
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT pjt_mbr_id FROM pjt_member where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next())	rtn = rs.getString("pjt_mbr_id");
		
		//���� �׸� ������ (�ݱ�)
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

	//*******************************************************************
	//	�ش��� �������� �����ϱ� [���,�̸�,��ȭ��ȣ,����,�μ���]
	//*******************************************************************/	
	private String[] searchManinfo (String login_id) throws Exception
	{
		String[] rtn = new String[5];		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//PM���� �˾ƺ���
		query  = "SELECT a.name,a.office_tel,c.ar_name,b.ac_name FROM user_table a,class_table b,rank_table c ";
		query += "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
		rs = stmt.executeQuery(query);
		if(rs.next())	{
			rtn[0] = login_id;						//���
			rtn[1] = rs.getString("name");			//�̸�
			rtn[2] = rs.getString("office_tel");	//��ȭ��ȣ
			rtn[3] = rs.getString("ar_name");		//���޸�
			rtn[4] = rs.getString("ac_name");		//�μ���
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;
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


