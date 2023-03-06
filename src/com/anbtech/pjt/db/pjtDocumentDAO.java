package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtDocumentDAO
{
	private Connection con;
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����ó��
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public pjtDocumentDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtDocumentDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	// �ش���� ������� List up
	//*******************************************************************/	
	public ArrayList getDocumentList(String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",user_id="",user_name="",in_date="";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_document where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
				
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));
				table.setParentNode(rs.getString("parent_node"));
				table.setChildNode(rs.getString("child_node"));	
				table.setLevelNo(rs.getString("level_no"));	
				table.setNodeName(rs.getString("node_name"));
				table.setUseDoc(rs.getString("use_doc"));
				user_id =rs.getString("user_id"); if(user_id == null) user_id="";
				table.setUserId(user_id); 
				user_name =rs.getString("user_name"); if(user_name == null) user_name="";
				table.setUserName(user_name);
				in_date =rs.getString("in_date"); if(in_date == null) in_date="";
				table.setInDate(in_date); 
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* ���ε� ������� �ݿ��ϱ� 
	*******************************************************************/
	public void updateDocument(String pid) throws Exception
	{
		String update = "",query="",pjt_code="",node_code="",user_id="",user_name="",in_date="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�������(techdoc_data���̺��� ���ó��� �б�)
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT writer,writer_s,pjt_code,node_code FROM master_data ";
		query += "where m_id = '"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			user_id = rs.getString("writer");
			user_name = rs.getString("writer_s");
			user_name = user_name.substring(0,user_name.indexOf("/"));
			pjt_code = rs.getString("pjt_code");
			node_code = rs.getString("node_code");
		}
		in_date = anbdt.getDate();
		

		//�ش系�� update�ϱ�
		update = "UPDATE pjt_document set user_id='"+user_id+"',user_name='"+user_name+"',use_doc='Y',";
		update += "in_date='"+in_date;
		update += "' where pjt_code='"+pjt_code+"' and child_node='"+node_code+"'";
		stmt.executeUpdate(update);

		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// �ش�����ڵ�� ������ ã��
	//*******************************************************************/	
	public String getProjectName(String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",pjt_name="";	

		stmt = con.createStatement();
		projectTable table = null;

		//query���� �����
		query = "SELECT pjt_name FROM prs_project where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		if(rs.next()) { 
			pjt_name = rs.getString("pjt_name");
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return pjt_name;
	}

	//*******************************************************************
	// �ش������������ڵ�� ��������� ã��
	//*******************************************************************/	
	public String getDocumentName(String pjt_code,String doc_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",doc_name="",type="";	

		stmt = con.createStatement();
		projectTable table = null;

		//���� �Ǵ� �μ����� �Ǵ��ϱ�
		query = "SELECT type FROM prs_project where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);

		if(rs.next()) { 
			type = rs.getString("type");
		}

		//��������� ã��
		query = "SELECT doc_name FROM prs_docname where doc_code='"+doc_code+"' and type='"+type+"'";	
		rs = stmt.executeQuery(query);

		if(rs.next()) { 
			doc_name = rs.getString("doc_name");
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return doc_name;
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
