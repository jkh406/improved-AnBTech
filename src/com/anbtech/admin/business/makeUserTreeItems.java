/*******************************************************
 * ����� ���� Ʈ����¿� ��ũ��Ʈ����(tree.js)�� ����
 *******************************************************/

package com.anbtech.admin.business;
import java.io.*;
import java.sql.*;
import java.util.*;

public class makeUserTreeItems{
	private String jdbc = "";
	private String url = "";
	private String user = "";
	private String password = "";

	private int i = 0;
	private int class_count = 0;
	private int user_count = 0;
	private int total_count = 0;

	private String[][] class_info; // �μ����� ������ ���� �迭
	private String[][] total_info; // �μ����� �� ����� ������ ���� �迭

	public makeUserTreeItems() 
	{
		openConnection();
	}

	/***********************************
	 * connection ��ü ����
	 ***********************************/
	public boolean openConnection() 
	{

		//---------------------------------------------------------------------
		// STEP 1. ������Ƽ ������ �ε��Ѵ�.
		//---------------------------------------------------------------------
		Properties prop = new Properties();

		try {
			InputStream is = getClass().getResourceAsStream("../../dbconn/db.properties");
			prop.load(is);
			if(is!=null) is.close();
			//---------------------------------------------------------------------
			// STEP 2. ������Ƽ ���Ͽ��� ������Ƽ�� �д´�. 
			//---------------------------------------------------------------------
			jdbc = prop.getProperty("drivers");
			url = prop.getProperty("mssql.host");
			user = prop.getProperty("mssql.db_user");
			password = prop.getProperty("mssql.db_password");

			return true;
		} catch(IOException e) {
			System.out.println("[DbConnection] ���� ���� �� ����");
			return false;
		}
	}
	
	/******************************
	 * �μ����� ���� �ľ��ؼ� ���� 
	 ******************************/
	public void getClassTotalCount() throws Exception{

		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = "select count(*) from class_table where isuse = 'y'";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			this.class_count = rs.getInt(1);
		}

		stmt.close(); 
	    Conn.close();

	} //getClassTotalCount()


	/*****************************
	 * ����� ���� �ľ��ؼ� ���� 
	 *****************************/
	public void getUserTotalCount() throws Exception{

		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = "select count(*) from user_table";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			this.user_count = rs.getInt(1);
		}

		stmt.close(); 
	    Conn.close();

	} //getUserTotalCount()

	
	/*********************************
	 * ���� ������ �迭�� ��´�. 
	 *********************************/
	public void saveClassInfoInArray(int ac_level,int ac_ancestor) throws Exception{
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = " select ac_level,ac_name,ac_id from class_table where ac_level = '"+ac_level+"' and ac_ancestor = '"+ac_ancestor+"' and isuse = 'y'";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			class_info[i][0] = rs.getString("ac_level");
			class_info[i][1] = rs.getString("ac_name");
			class_info[i][2] = rs.getString("ac_id");
//System.out.print(class_info[i][0] + " : " + class_info[i][1] + " : " + class_info[i][2] + "\n");
			i++;
			saveClassInfoInArray(Integer.parseInt(class_info[i-1][0])+1, Integer.parseInt(class_info[i-1][2]));
		}
		stmt.close(); 
	    Conn.close();
	} //saveClassInfoInArray()


	/*********************************
	 * ���� ������ �ִ� ������ ���Ѵ�.
	 *********************************/
	public int getMaxLevel() throws Exception{
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = "SELECT MAX(ac_level) from class_table";
		ResultSet rs = stmt.executeQuery(sql);

		int max_level = 0;
		while (rs.next()) {
			max_level = rs.getInt(1);
		}
		stmt.close(); 
	    Conn.close();

		return max_level;
	} //getMaxLevel()


	/*************************************************
	 * �ش� �μ��� ���� ����� ������ �迭�� ��´�.
	 *************************************************/
	public void saveUserInfoInArray(int ac_level,int ac_ancestor) throws Exception{
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();
		getClassTotalCount();	// �μ� ���� �ľ�
		getUserTotalCount();	// ����� ���� �ľ�

		class_info = new String[this.class_count][3];
		saveClassInfoInArray(ac_level,ac_ancestor);
		
		this.total_count = this.class_count + this.user_count;
		total_info = new String[this.total_count][4];

		int u = 0;
		for (int c=0; c<this.class_count; c++)
		{
			total_info[u][0] = class_info[c][0];	// ����
			total_info[u][1] = class_info[c][1];	// �̸�
			total_info[u][2] = class_info[c][2];	// �ڵ�
			total_info[u][3] = "div";				// �μ����� ��Ÿ��.
			int class_level = Integer.parseInt(class_info[c][0]);

//System.out.print(total_info[u][0] + " : " + total_info[u][1] + " : " + total_info[u][0] + "\n");

			String sql = "SELECT a.au_id,a.ac_id,a.name,a.id,b.ar_name FROM user_table a,rank_table b WHERE a.ac_id = '" + class_info[c][2] + "' and a.rank = b.ar_code order by b.ar_priorty ASC";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				u++;
			
				total_info[u][0] = Integer.toString(class_level + 1);	// �μ����� + 1�ܰ�
				total_info[u][1] = rs.getString("ar_name") + "/" + rs.getString("name");
				total_info[u][2] = rs.getString("id");
				total_info[u][3] = "usr";				// ��������� ��Ÿ��.

//System.out.print(total_info[u][0] + " : " + total_info[u][1] + " : " + total_info[u][0] + "\n");

			}
			u++;
		}

		stmt.close(); 
	    Conn.close();

	} //saveUserInfoInArray()

	/*********************************
	 * �ش� ����ڰ� �μ��������� �Ǵ��Ѵ�.
	 *********************************/
	public boolean isChief(String id) throws Exception{
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = "SELECT COUNT(*) FROM class_table WHERE ac_level = '1' AND chief_id ='" + id + "'";
		ResultSet rs = stmt.executeQuery(sql);

		boolean is_chief = false;
		while (rs.next()) {
			if(rs.getInt(1) > 0) is_chief = true;
		}
		stmt.close(); 
	    Conn.close();

		return is_chief;
	} //isChief()

	/**********************************
	 * tree_items.js ������ �����Ѵ�.
	 **********************************/
	public boolean makeUserInfoTree(String jspath,String target,int ac_level,int ac_ancestor){
	  try {

		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		saveUserInfoInArray(ac_level,ac_ancestor);

		//String jspath = "C:/tomcat4/webapps/webffice/admin/user_tree_items.js";
		File jsexist = new File(jspath);

		if(jsexist.exists()){
			BufferedWriter bw = new BufferedWriter(new FileWriter(jspath));

			//-------------------------------------------------
			//  Tree ������ ���� �迭���� (tree_items.jp �����)
			//--------------------------------------------------
			String space = " ";
			int st = 0;		//������ level
			int cu = 0;		//�� ���� level
			int di = 0;		//���� : cu - st 

			String link = "";	// ��ũURL�� ���� ����
			
			int max_level = getMaxLevel();
			String pid_s = "";	// pid�� ���� ����
			String [] pid = new String[max_level + 1];
			String sql = "";

			bw.write("var TREE_ITEMS = [");	//���� ������
			bw.newLine();

			
			for(int bi=0; bi<this.total_count; bi++){

//			System.out.println(bi+":"+total_info[bi][0]+":"+total_info[bi][1]+":"+total_info[bi][2]);

				for(int level = 0; level <= max_level; level++){
					if(total_info[bi][0].equals(Integer.toString(level+1))) pid[level] = Integer.toString(bi);
				}

				pid_s = "";
				for(int j=0; j<Integer.parseInt(total_info[bi][0]); j++){
					pid_s += pid[j]+",";
				}

				if(total_info[bi][3].equals("div")){
					sql = "UPDATE class_table SET pid = '"+pid_s+"' where ac_id = '" + total_info[bi][2] + "'";
				}else{
					sql = "UPDATE user_table SET pid = '"+pid_s+"' where id = '" + total_info[bi][2] + "'";
				}
				stmt.executeUpdate(sql);

			
				//�ش� ����� �μ����� ������ �ִ��� üũ�Ѵ�.(�μ����� ���ڰ��缱 ������ �ڽ� ���� �����ϰ� �ϱ� ����)
				String is_chief = "N";
				if(total_info[bi][3].equals("usr") && isChief(total_info[bi][2])) is_chief = "Y";

				if(total_info[bi][3].equals("usr") && target != "NA") link = target + "?type="+total_info[bi][3]+"&sid="+total_info[bi][2]+"&pid="+pid_s;
				else if(total_info[bi][3].equals("div") && target != "NA") link = "";
				else if(total_info[bi][3].equals("usr") && target == "NA") link = "usr|" + total_info[bi][2] + "/" + total_info[bi][1] + "|" + is_chief;
				else if(total_info[bi][3].equals("div") && target == "NA") link = "div|" + total_info[bi][2] + "/" + total_info[bi][1];

				//������
				if(bi == 0) {
					st = Integer.parseInt(total_info[bi][0]);
					space = " ";
					for(int s=0; s < st; s++) space += "   ";
					bw.write(space + "['"+total_info[bi][1]+"','"+link+"'");
				} else if(bi == (this.total_count-1)) {
					cu = Integer.parseInt(total_info[bi][0]);
					di = cu - st;
				
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						bw.write(",");		//����
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'],");		//������
						bw.newLine();
					} else if(di == 0) {
						bw.write("],");		//����
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'],");		//������
						bw.newLine();
					} else {
						bw.write("],");		//����
						bw.newLine();
						for(int m=0; m > di; di++) bw.write(space + "],"); //�շ��� ����
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'],");		//������
						bw.newLine();
					}
					
					//������ ���� �ݱ�
					di = cu - 0;
					for(int e=di; e > 0; e--) {
						space = " ";
						for(int es=1; es < e; es++) space += "   ";
						bw.write(space + "],"); //����
						bw.newLine();
					}

				} else {
					cu = Integer.parseInt(total_info[bi][0]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						bw.write(",");		//����
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'");		//������
					} else if(di == 0) {
						bw.write("],");		//����
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'");		//������
					} else {
						bw.write("],");		//����
						bw.newLine();
						for(int m=0; m > di; di++) bw.write(space + "],"); //�շ��� ����
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'");		//������
					}
					st = Integer.parseInt(total_info[bi][0]);
				} //if	

			} //for
			bw.write("];");	//�� ������
			bw.newLine();
			bw.close();

			stmt.close();
			Conn.close();
		}else{
			IOException trouble = new IOException();
			throw trouble;
		}
		return true;
	  }catch (Exception e){
		return false;
      }
	} //makeUserInfoTree()	

} //makeUserTreeItems
