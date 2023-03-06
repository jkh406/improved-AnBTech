package com.anbtech.bm.db;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class BomTemplateDAO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	
	private String query="", update="";
	private ArrayList item_list = null;				//PART������ ArrayList�� ���
	private mbomStrTable mst = null;				//help class
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BomTemplateDAO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		Template ����
	//			
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_ENV ������ Flag�� LIST  
	// flag 1 : OP CODE
	// flag 2 : Template data
	// flag 3 : ���躯�� �׸���� ������
	// flag 4 : ���躯�� �׸� �з��ڵ� ������
	// flag 5 : ���躯�� ��ǰ�� �������
	//*******************************************************************/	
	public ArrayList getBomEnvList(String flag) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomEnvTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM mbom_env WHERE flag = '"+flag+"' order by m_code asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomEnvTable();		
				table.setPid(rs.getString("pid"));
				table.setMCode(rs.getString("m_code"));
				table.setSpec(rs.getString("spec"));
				table.setTag(rs.getString("tag"));	
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	�ش� Template�� �����ڵ� SPEC���� ���ϱ�
	//*******************************************************************/
	public String getOpCodeSpec(String part_code) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		query = "SELECT spec FROM mbom_env WHERE m_code='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("spec");
			
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	// MBOM_STR���� ����Template BOM ������ query
	//   �������ø� BOM�� ����ASSY�ڵ带 ����,����ϱ� ����
	//*******************************************************************/	
	public ArrayList getTempBomList(String gid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM mbom_str WHERE gid = '"+gid+"' and tag='1' order by child_code asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();		
				table.setPid(rs.getString("pid"));
				table.setGid(rs.getString("gid"));
				table.setParentCode(rs.getString("parent_code"));
				table.setChildCode(rs.getString("child_code"));
				table.setLevelNo(rs.getString("level_no"));	
				table.setPartSpec(rs.getString("part_spec"));	
				////System.out.println(rs.getString("level_no")+" : "+rs.getString("parent_code")+" : "+rs.getString("child_code"));
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		����� ���� ���ϱ�
	//						
	//---------------------------------------------------------------------

	//*******************************************************************
	//	����� '�Ҽ�/����/�̸�' ���ϱ�
	//*******************************************************************/
	public String getRegInfo(String sabun) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select a.name,b.ac_name,c.ar_name from user_table a,class_table b,rank_table c ";
		query +="where (a.id ='"+sabun+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString("ac_name")+"/"+rs.getString("ar_name")+"/"+rs.getString("name");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//--------------------------------------------------------------------
	//
	//		item master ���� ���ϱ�
	//						
	//---------------------------------------------------------------------

	//*******************************************************************
	//	ASSY�ڵ忡�� ����� �������� : �԰��� , ������ ù��° �ʵ�
	//*******************************************************************/
	public String getStateName(String item_code) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "",item_desc="";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "SELECT item_desc FROM item_master WHERE item_no='"+item_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			item_desc = rs.getString("item_desc");
		}
		
		//���������� ��������
		int comma = item_desc.indexOf(",");
		if(comma == -1) comma = item_desc.length();
		data = item_desc.substring(0,comma);			//�����

		stmt.close();
		rs.close();
		return data;			
	}

}

