package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtProcessDAO
{
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
	public pjtProcessDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtProcessDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	/**********************************************************************
	 * ���������� ���μ��� ������ �迭�� ��´�. 
	 * Ʈ���������·� �����͸� ������ ��, �迭�� ��´�.
	 *********************************************************************/
	public void saveItemsArray(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//���� �ʱ�ȭ
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "select child_node,node_name,level_no,pid from pjt_schedule ";
		query += "where level_no = '"+level_no+"' and parent_node = '"+parent_node+"' ";
		query += "and pjt_code = '"+pjt_code+"' order by child_node asc";

		rs = stmt.executeQuery(query);

		int no=0;	String c_nd = "";
		while (rs.next()) {
			item[an][0] = rs.getString("level_no");
			c_nd = rs.getString("child_node");
			item[an][1] = c_nd + " " + rs.getString("node_name");
			item[an][2] = c_nd;
			item[an][3] = rs.getString("pid");
			//System.out.println(item[an][0]+":"+item[an][2]+":"+item[an][1]+":"+item[an][3]);
			an++;
			no = Integer.parseInt(item[an-1][0]);					//String�� ������ �ٲٱ�
			lno = Integer.toString(no+1);							//+1�Ͽ� ������ String���� �ٲٱ� 
			//if(no<2) saveItemsArray(pjt_code,lno,item[an-1][2]);	//0,1,2������ ����Ѵ�.
			saveItemsArray(pjt_code,lno,item[an-1][2]);
		}
		rs.close();
		stmt.close(); 
		
	} //saveItemsArray

	/**********************************************************************
	 * saveItemsArray() ������ ����� �迭���� �̿��Ͽ�
	 * tree_items.js �� String�� �����.
	 * pjt_code : �����ڵ�, level_no : 0, parent_node : 0, url : ��ũ
	 **********************************************************************/
	public String makeProcessTree(String pjt_code,String level_no,String parent_node,String url) throws Exception
	{
		String tree = "";				//tree
		String mode = "PNP_L";

		//��ü������ �迭 �����
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][4];

		// �迭�� ��´�.
		saveItemsArray(pjt_code,level_no,parent_node);

		//tree_items����� (an : item�� ����)
		if(an > 0){
			String space = " ";
			int st = 0;		//������ level
			int cu = 0;		//�� ���� level
			int di = 0;		//���� : cu - st 

			String link = "";	// ��ũURL�� ���� ����
			String tmp = "";	// 
			String [] pid = new String[4];

			tree = "var TREE_ITEMS = [";	//���� ������
			for(int bi=0; bi<cnt; bi++){
				if(item[bi][0].equals("1")) pid[0] = Integer.toString(bi);
				if(item[bi][0].equals("2")) pid[1] = Integer.toString(bi);
				if(item[bi][0].equals("3")) pid[2] = Integer.toString(bi);
				if(item[bi][0].equals("4")) pid[3] = Integer.toString(bi);
				tmp = "";
				for(int j=0;j<Integer.parseInt(item[bi][0]);j++){
					tmp += pid[j]+",";
				}
				if(tmp.length() == 0) tmp="0,";
				//��ũ �����
				if(item[bi][0].equals("1") || item[bi][0].equals("2")) 		//step,Activity�� ��ũ����
					link = url + "?mode="+mode+"&pid="+item[bi][3]+"&p_id="+tmp;
				else link = "";						//������,phase,step�� link����

				//������
				if(bi == 0) {
					st = Integer.parseInt(item[bi][0]);
					space = " ";
					for(int s=0; s < st; s++) space += "   ";
					tree += space + "['"+item[bi][1]+"','"+link+"'";
					if(cnt == 1) tree += "],";		//1���ϰ��
				} else if(bi == (cnt-1)) {
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
				
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//����
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//������
					} else if(di == 0) {
						tree += "],";		//����
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//������
					} else {
						tree += "],";		//����
						for(int m=0; m > di; di++) tree += space + "],"; //�շ��� ����
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//������
					}
					
					//������ ���� �ݱ�
					di = cu - 0;
					for(int e=di; e > 0; e--) {
						space = " ";
						for(int es=1; es < e; es++) space += "   ";
						tree += space + "],"; //����
					}
				} else {
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//����
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//������
					} else if(di == 0) {
						tree += "],";		//����
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//������
					} else {
						tree += "],";		//����
						for(int m=0; m > di; di++) tree += space + "],"; //�շ��� ����
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//������
					}
					st = Integer.parseInt(item[bi][0]);
				} //if	
			} //for
			tree += "];";	//�� ������
		} //if
		return tree;
	} //makeCategoryTree

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

	//*******************************************************************
	// ���������� �ش�PID�� �̿��Ͽ� �������� ã��
	//*******************************************************************/	
	public ArrayList getNodeBaseList(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//���μ����ڵ�,type���ϱ�
		searchProcessUsePid(pid);
		
		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM pjt_schedule where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
				
				table.setPid(rs.getString("pid"));
				this.pjt_code = rs.getString("pjt_code");
				table.setPjtCode(this.pjt_code);	
				table.setPjtName(rs.getString("pjt_name"));
				table.setParentNode(rs.getString("parent_node"));
				this.child_node = rs.getString("child_node");
				table.setChildNode(child_node);	
				table.setNodeName(rs.getString("node_name"));	
				this.level_no = rs.getString("level_no");
				table.setLevelNo(level_no);	
				table.setPrsCode(this.prs_code);
				table.setPrsType(this.type);
											
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ���������� �ش�PID�� �̿��Ͽ� ���μ��� code,typeã��
	//*******************************************************************/	
	public void searchProcessUsePid(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",pjt_code="";	

		stmt = con.createStatement();

		//�����ڵ� ã��
		query = "SELECT pjt_code FROM pjt_schedule where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			pjt_code = rs.getString("pjt_code");
		}
				
		//���μ����ڵ�� type���ϱ�
		query = "SELECT prs_code,prs_type FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			this.prs_code = rs.getString("prs_code");
			this.type = rs.getString("prs_type");
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// ǥ�����μ���[����,�μ�����]�� ����� ��� List��������
	//*******************************************************************/	
	public ArrayList getNodeList() throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//level�� 0 �϶� [phase����]
		if(level_no.equals("0")) {
			query = "SELECT ph_code,ph_name FROM prs_phase ";
			query += "where type='"+this.type+"' order by ph_code asc";	
			rs = stmt.executeQuery(query);
			while(rs.next()) { 
					table = new prsCodeTable();
					table.setPhCode(rs.getString("ph_code"));							
					table.setPhName(rs.getString("ph_name"));					
					table_list.add(table);
			}
		}
		//level�� 1 �϶� [step����]
		else if(level_no.equals("1")) {
			query = "SELECT step_code,step_name FROM prs_step ";
			query += "where ph_code='"+this.child_node+"' and type='"+this.type+"' order by step_code asc";	
			rs = stmt.executeQuery(query);
			while(rs.next()) { 
					table = new prsCodeTable();
					table.setStepCode(rs.getString("step_code"));							
					table.setStepName(rs.getString("step_name"));					
					table_list.add(table);
			}
		}
		//level�� 2 �϶� [activity����]
		else if(level_no.equals("2")) {
			query = "SELECT act_code,act_name FROM prs_activity ";
			query += "where step_code='"+this.child_node+"' and type='"+this.type+"' order by act_code asc";
			rs = stmt.executeQuery(query);
			while(rs.next()) { 
					table = new prsCodeTable();
					table.setActCode(rs.getString("act_code"));							
					table.setActName(rs.getString("act_name"));					
					table_list.add(table);
			}
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ǥ�����μ���[����,�μ�����]�� ����� ���⹰ ��������
	//*******************************************************************/	
	public ArrayList getNodeDocList() throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		query = "SELECT doc_code,doc_name FROM prs_docname ";
		query += "where step_code='"+this.child_node+"' and type='"+this.type+"' order by doc_code asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new prsCodeTable();
				table.setDocCode(rs.getString("doc_code"));							
				table.setDocName(rs.getString("doc_name"));					
				table_list.add(table);
		}
	
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ǥ�����μ������⹰[����,�μ�����]�� ��ϵ� ���⹰ ��������
	//*******************************************************************/	
	public ArrayList getSaveDocList() throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		query = "SELECT * FROM pjt_document ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+this.child_node+"' and child_node like '%"+this.child_node+"%' order by child_node asc";
		rs = stmt.executeQuery(query);
		
		while(rs.next()) { 
				table = new projectTable();
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setParentNode(rs.getString("parent_node"));	
				table.setChildNode(rs.getString("child_node"));							
				table.setNodeName(rs.getString("node_name"));	
				table.setLevelNo(rs.getString("level_no"));							
					
				table_list.add(table);
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ǥ�����μ��� Node ����ϱ� [��������]
	//*******************************************************************/	
	public String inputNode(String pjt_code,String pjt_name,String parent_node,String[] child_node,String level_no) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		//���μ����ڵ�,type���ϱ�
		searchProcessUsePjtCode(pjt_code);

		//������ȣ �����ϱ�
		String pid = getID();

		//�ش�child_node�� �̿��Ͽ� node_name���ϱ�
		int cnt = child_node.length;
		String[][] node = new String[cnt][2];
		//level�� 1 �϶� [phase����]
		if(level_no.equals("1")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT ph_code,ph_name FROM prs_phase ";
				query += "where ph_code='"+child_node[n]+"' and type='"+this.type+"' order by ph_code asc";	
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("ph_code");							
						node[n][1] = rs.getString("ph_name");					
				}
			}
		}
		//level�� 2 �϶� [step����]
		else if(level_no.equals("2")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT step_code,step_name FROM prs_step ";
				query += "where step_code='"+child_node[n]+"' and ph_code='"+parent_node+"' and type='"+this.type+"' order by step_code asc";	
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("step_code");							
						node[n][1] = rs.getString("step_name");					
				}
			}
		}
		//level�� 3 �϶� [activity����]
		else if(level_no.equals("3")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT act_code,act_name FROM prs_activity ";
				query += "where act_code='"+child_node[n]+"' and step_code='"+parent_node+"' and type='"+this.type+"' order by act_code asc";
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("act_code");							
						node[n][1] = rs.getString("act_name");					
				}
			}
		}


		//��� ����ϱ�
		String data = "";
		String npid = "",input="",c_node="",c_name="";
		com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("00");
		for(int i=0; i<cnt; i++) {
			c_node = node[i][0]; if(c_node == null) c_node = "";	//child node
			c_name = node[i][1]; if(c_name == null) c_name = "";	//node name
			npid = pid+nfm.toDigits(i);
			if(c_node.length() != 0) {
				data += insertNode(npid,pjt_code,pjt_name,parent_node,c_node,c_name,level_no,"pjt_schedule");
			}
		}
		
		stmt.close();
		rs.close();
		return data;
		
	}

	//*******************************************************************
	// ǥ�����μ��� ���������� Node ����ϱ� [����,�μ� ����]
	//*******************************************************************/	
	public void inputNodeDoc(String pjt_code,String pjt_name,String parent_node,String[] child_node,String level_no) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		//���μ����ڵ�,type���ϱ�
		searchProcessUsePjtCode(pjt_code);

		//������ȣ �����ϱ�
		String pid = getID();

		//�ش�child_node�� �̿��Ͽ� node_name���ϱ�
		int cnt = child_node.length;
		String[][] node = new String[cnt][2];
		//level�� 1 �϶� [phase����]
		if(level_no.equals("1")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT ph_code,ph_name FROM prs_phase ";
				query += "where ph_code='"+child_node[n]+"' and type='"+this.type+"' order by ph_code asc";	
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("ph_code");							
						node[n][1] = rs.getString("ph_name");					
				}
			}
		}
		//level�� 2 �϶� [step����]
		else if(level_no.equals("2")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT step_code,step_name FROM prs_step ";
				query += "where step_code='"+child_node[n]+"' and ph_code='"+parent_node+"' and type='"+this.type+"' order by step_code asc";	
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("step_code");							
						node[n][1] = rs.getString("step_name");					
				}
			}
		}
		//level�� 3 �϶� [activity����]
		else if(level_no.equals("3")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT doc_code,doc_name FROM prs_docname ";
				query += "where doc_code='"+child_node[n]+"' and step_code='"+parent_node+"' and type='"+this.type+"' order by doc_code asc";
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("doc_code");							
						node[n][1] = rs.getString("doc_name");					
				}
			}
		}


		//��� ����ϱ�
		String npid = "",input="",c_node="",c_name="";
		com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("00");
		for(int i=0; i<cnt; i++) {
			c_node = node[i][0]; if(c_node == null) c_node = "";	//child node
			c_name = node[i][1]; if(c_name == null) c_name = "";	//node name
			npid = pid+nfm.toDigits(i);
			if(c_node.length() != 0) {
				insertNode(npid,pjt_code,pjt_name,parent_node,c_node,c_name,level_no,"pjt_document");
			}
		}
		
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// ǥ�����μ��� Node[��������������] �Է� �����ϱ�
	//*******************************************************************/	
	public String insertNode(String npid,String pjt_code,String pjt_name,String parent_node,String child_node,String node_name,String level_no,String tablename) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//��� ����ϱ�
		String input = "",data="";
		input = "INSERT INTO "+tablename+"(pid,pjt_code,pjt_name,parent_node,child_node,node_name,level_no) values('";
		input += npid+"','"+pjt_code+"','"+pjt_name+"','"+parent_node+"','"+child_node+"','"+node_name+"','"+level_no+"')";
		//�ߺ��� ������ ��ϵ�
		if(isNoDuplicate(pjt_code,parent_node,child_node,level_no,tablename)) { 	
			data = input + "<br>";
			stmt.executeUpdate(input);
		}
		
		stmt.close();
		return data;
	}

	//*******************************************************************
	// ǥ�����μ��� Node[��������������] ��� �ߺ��˻��ϱ� [����,�μ�����]
	//*******************************************************************/	
	public boolean isNoDuplicate(String pjt_code,String parent_node,String child_node,String level_no,String tablename) throws Exception
	{
		boolean rtn = true;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		query = "SELECT COUNT(*) from "+tablename+" ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+parent_node+"' and child_node='"+child_node+"' and level_no='"+level_no+"'";	
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		if(cnt > 0) rtn = false; 

		stmt.close();
		rs.close();
		return rtn;		
	}

	
	//*******************************************************************
	// ���������� pjt_code �� �̿��Ͽ� ���μ��� code,typeã��
	//*******************************************************************/	
	public void searchProcessUsePjtCode(String pjt_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
	
		//���μ����ڵ�� type���ϱ�
		query = "SELECT prs_code,prs_type FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			this.prs_code = rs.getString("prs_code");
			this.type = rs.getString("prs_type");
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// ���������� ���μ��� Node �����ϱ�
	//*******************************************************************/	
	public String deleteNode(String pid,String pjt_code,String parent_node,String[] child_node,String level_no) throws Exception
	{
		String rtn = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//�ش�child_node ũ�� ���ϱ�
		int cnt = child_node.length;

		//�������� ���⹰�� �ִ��� �Ǵ��ϰ�, ����峻��������� ���⹰�� ���� ������� �޽��� ���
		//1. �ش��� ��ü�������� �˻�
		String cu_node = "";
		int same = 0,cu_cnt = 0;
		String query = "SELECT child_node from pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+parent_node+"' and level_no='3'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			cu_node = rs.getString("child_node");
			for(int i=0; i<child_node.length; i++) {
				if(cu_node.equals(child_node[i])) same++;
			}
			cu_cnt++;
		}

		//2.�ش����� ���⹰�� �ִ��� ����
		String cu_doc = "N";
		if(!isNoUnderTree(pjt_code,parent_node,"pjt_document")) {
			cu_doc = "Y";
		}
		
		//3.�ߴ����� ������� �Ǵ��ϱ�
		if((same == cu_cnt) && cu_doc.equals("Y")){ rtn = "DOC"; stmt.close(); 	rs.close(); return rtn; }

		//��� �����ϱ�
		String delete="",c_node="";
		for(int i=0; i<cnt; i++) {
			c_node = child_node[i];	if(c_node == null) c_node = "";
			if(c_node.length() != 0) {
				rtn += removeNode(pjt_code,parent_node,c_node,"pjt_schedule");
			}
		}
		
		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// ǥ�����μ��� ���������� Node �����ϱ� [����,�μ�����]
	//*******************************************************************/	
	public String deleteNodeDoc(String pid,String pjt_code,String parent_node,String[] child_node,String level_no) throws Exception
	{
		String rtn = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//�ش�child_node ũ�� ���ϱ�
		int cnt = child_node.length;

		//��� �����ϱ�
		String delete="",c_node="";
		for(int i=0; i<cnt; i++) {
			c_node = child_node[i];	if(c_node == null) c_node = "";
			//���⹰ ��ϵ� ����̸� �����Ұ�
			if(c_node.length() != 0) {
				rtn += removeNode(pjt_code,parent_node,c_node,"pjt_document");
			}
		}
	
		if(rtn.length() > 1) rtn = rtn.substring(0,rtn.length()-1);
		stmt.close();
		return rtn;
	}

	//*******************************************************************
	// ǥ�����μ��� Node[��������������] ���� �����ϱ�
	//*******************************************************************/	
	public String removeNode(String pjt_code,String parent_node,String child_node,String tablename) throws Exception
	{
		String delete = "",data="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//1. �ش� ���μ����� ����� ���¸� �ľ��Ѵ�.[�����°� NULL�϶��� ����] 
		String node_status = "",use_doc="N";
		if(tablename.equals("pjt_schedule")) {
			query = "SELECT node_status from pjt_schedule ";
			query += "where pjt_code='"+pjt_code+"' and parent_node='"+parent_node+"' and child_node='"+child_node+"'";	
			rs = stmt.executeQuery(query);
		
			if(rs.next()) {
				node_status = rs.getString("node_status"); if(node_status == null) node_status = "";
				
				if(node_status.equals("1")) node_status = "[node working]";
				else if(node_status.equals("2")) node_status = "[node finished]";
				else if(node_status.equals("3")) node_status = "[node drop]";
				else if(node_status.equals("4")) node_status = "[node hold]";
				else if(node_status.equals("5")) node_status = "[node skip]";
			}
			if(node_status.length() != 0) { 
				data = child_node+node_status+","; stmt.close(); 	rs.close(); 
				return data;
			}
			rs.close();
		}
		//���⹰�� ���:���⹰�� ��ϵ� ���� �����Ұ�
		else if(tablename.equals("pjt_document")) {
			query = "SELECT use_doc from pjt_document ";
			query += "where pjt_code='"+pjt_code+"' and parent_node='"+parent_node+"' and child_node='"+child_node+"'";	
			rs = stmt.executeQuery(query);
		
			if(rs.next()) {
				use_doc = rs.getString("use_doc"); if(use_doc == null) use_doc = "N";
			}	
			if(use_doc.equals("Y")) {
				data = child_node+"[approval]"+","; stmt.close(); 	rs.close(); 
				return data;
			}
			rs.close();
		}

		//��� �����ϱ�
		delete = "DELETE from "+tablename+" where pjt_code='"+pjt_code+"' ";
		delete += "and parent_node='"+parent_node+"' and child_node='"+child_node+"'";

		//�Ϻα����� ������ ������
		if(isNoUnderTree(pjt_code,child_node,tablename)) {		
			stmt.executeUpdate(delete);
		} else { data += child_node + ","; }
		
		stmt.close();
		return data;
	}

	//*******************************************************************
	// �������� ���μ��� Node[��������������]�� �Ϻα����� �ִ��� �˻��ϱ� 
	//*******************************************************************/	
	public boolean isNoUnderTree(String pjt_code,String child_node,String tablename) throws Exception
	{
		boolean rtn = true;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		query = "SELECT COUNT(*) from "+tablename+" ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+child_node+"' ";
		query += "and child_node like '%"+child_node+"%'";	
	
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);	
		if(cnt > 0) rtn = false; 

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
