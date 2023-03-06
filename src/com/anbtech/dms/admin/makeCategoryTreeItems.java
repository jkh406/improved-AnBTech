/*****************************************************************
 * ���� ī�װ� �з��� Ʈ�������� ����ϱ� ���� tree_item.js
 * ������ ������Ű�� Ŭ����
 *****************************************************************/

package com.anbtech.dms.admin;
import java.io.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.odbc.*;

public class makeCategoryTreeItems{
	private Connection con;

	public makeCategoryTreeItems(Connection con){
		this.con = con;
	}

	public makeCategoryTreeItems() throws Exception{
		openConnection();
	}

	private int i = 0;
	private String[][] item;

	private String test = "eror";

	/********************************
	 * db connection�� �����Ѵ�.
	 ********************************/
	public void openConnection() throws Exception{
		Properties prop = new Properties();
		String jdbc		= "";
		String url		= "";
		String user		= "";
		String password	= "";

		// ������Ƽ ������ �ε��Ѵ�.
		InputStream is = getClass().getResourceAsStream("../../dbconn/db.properties");
		prop.load(is);
		if(is!=null) is.close();

		// ������Ƽ ���Ͽ��� ������Ƽ�� �д´�. 
		jdbc = prop.getProperty("drivers");
		url = prop.getProperty("mssql.host");
		user = prop.getProperty("mssql.db_user");
		password = prop.getProperty("mssql.db_password");

		// connection�� �����Ѵ�.
		Class.forName(jdbc); 
	    this.con = DriverManager.getConnection(url,user,password); 
	}
	
	/********************************
	 * ��ü ���ڵ��� ������ ���Ѵ�.
	 ********************************/
	public int getTotalCount() throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		int total_count = 0;
		String sql = "select count(*) from category_data";
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			total_count = rs.getInt(1);
		}
		stmt.close();
		rs.close();

		return total_count;
	} //getTotalCount

	
	/******************************************************
	 * ��ǰ���� ������ �迭�� ��´�. ��,
	 * Ʈ���������·� �����͸� ������ ��, �迭�� ��´�.
	 ******************************************************/
	public boolean saveItemsArray(int level,int ancestor) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = " select c_id,c_level,c_name from category_data where c_level = '"+level+"' and c_ancestor = '"+ancestor+"'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			item[i][2] = rs.getString("c_id");
			item[i][0] = rs.getString("c_level");
			item[i][1] = rs.getString("c_name");
			i++;
			saveItemsArray(Integer.parseInt(item[i-1][0])+1, Integer.parseInt(item[i-1][2]));

		}
		stmt.close();
		rs.close();

		return true;
	} //saveItemsArray


	
	/********************************************************
	 * saveItemsArray() ������ ����� �迭���� �̿��Ͽ�
	 * tree_items.js ������ �����Ѵ�.
	 ********************************************************/
	public boolean makeCategoryTree(int level,int ancestor,String url,String jspath) throws Exception{
		Statement stmt = con.createStatement();

		int total_count = getTotalCount();				// ��ü ���ڵ� ������ �ľ�
		item = new String[total_count][3];				// �� ī�װ� �׸��� ���� �迭 ����
		boolean data = saveItemsArray(level,ancestor);	// �迭�� ��´�.

//		String jspath = getServletContext().getRealPath("") + "/dms/admin/tree_items.js";
		File jsexist = new File(jspath);

		if(jsexist.exists()&& data){
			BufferedWriter bw = new BufferedWriter(new FileWriter(jspath));

			//-------------------------------------------------
			//  Tree ������ ���� �迭���� (tree_items.js �����)
			//--------------------------------------------------
			String space = " ";
			int st = 0;		//������ level
			int cu = 0;		//�� ���� level
			int di = 0;		//���� : cu - st 

			String link = "";	// ��ũURL�� ���� ����
			String tmp = "";	// 
			String [] pid = new String[4];
			String sql = "";

			bw.write("var TREE_ITEMS = [");	//���� ������
			bw.newLine();

			for(int bi=0; bi<total_count; bi++){


				if(item[bi][0].equals("1")) pid[0] = Integer.toString(bi);
				if(item[bi][0].equals("2")) pid[1] = Integer.toString(bi);
				if(item[bi][0].equals("3")) pid[2] = Integer.toString(bi);
				if(item[bi][0].equals("4")) pid[3] = Integer.toString(bi);
				tmp = "";
				for(int j=0;j<Integer.parseInt(item[bi][0]);j++){
					tmp += pid[j]+",";
				}
				link = url + "?category="+item[bi][2]+"&p_id="+tmp;

				sql = "update category_data set p_id = '"+tmp+"' where c_id = '"+item[bi][2]+"'";
				stmt.executeUpdate(sql);

				//������
				if(bi == 0) {
					st = Integer.parseInt(item[bi][0]);
					space = " ";
					for(int s=0; s < st; s++) space += "   ";
//					bw.write(space + "['"+item[bi][1]+"','"+"link.jsp?ag_id="+item[bi][2]+"&p_id="+tmp+"'");
					bw.write(space + "['"+item[bi][1]+"','"+link+"'");
				} else if(bi == (total_count-1)) {
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
				
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						bw.write(",");		//����
						bw.newLine();
						bw.write(space + "['"+item[bi][1]+"','"+link+"'],");		//������
						bw.newLine();
					} else if(di == 0) {
						bw.write("],");		//����
						bw.newLine();
						bw.write(space + "['"+item[bi][1]+"','"+link+"'],");		//������
						bw.newLine();
					} else {
						bw.write("],");		//����
						bw.newLine();
						for(int m=0; m > di; di++) bw.write(space + "],"); //�շ��� ����
						bw.newLine();
						bw.write(space + "['"+item[bi][1]+"','"+link+"'],");		//������
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
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						bw.write(",");		//����
						bw.newLine();
						bw.write(space + "['"+item[bi][1]+"','"+link+"'");		//������
					} else if(di == 0) {
						bw.write("],");		//����
						bw.newLine();
						bw.write(space + "['"+item[bi][1]+"','"+link+"'");		//������
					} else {
						bw.write("],");		//����
						bw.newLine();
						for(int m=0; m > di; di++) bw.write(space + "],"); //�շ��� ����
						bw.newLine();
						bw.write(space + "['"+item[bi][1]+"','"+link+"'");		//������
					}
					st = Integer.parseInt(item[bi][0]);
				} //if	

			} //for
			bw.write("];");	//�� ������
			bw.newLine();

			bw.close();
		}else{
			IOException trouble = new IOException();
			throw trouble;
		}
		stmt.close();

		return true;
	} //makeCategoryTree


} //makeCategoryTreeItems
