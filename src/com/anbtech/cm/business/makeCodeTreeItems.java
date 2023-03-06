package com.anbtech.cm.business;

import java.io.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.odbc.*;
import com.anbtech.cm.entity.*;
import com.anbtech.cm.db.*;
import com.anbtech.cm.business.*;

public class makeCodeTreeItems{
	private Connection con;

	public makeCodeTreeItems(Connection con){
		this.con = con;
	}

	public makeCodeTreeItems() throws Exception{
		openConnection();
	}
	
	private int i = 0;
	private int total_count = 0;
	private String[][] goods; // ����Ʈ�� ���� �迭 ����

	private String whereStr = "";
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

	/***********************************
	 * ��ü ��ǰ ������ �ľ��ؼ� ���� 
	 ***********************************/
	public void getTotalCount() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		int total_count = 0;
		String sql = "SELECT COUNT(*) FROM item_class";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			this.total_count = rs.getInt(1);
		}

		stmt.close();
		rs.close();
	} //getTotalCount

	
	/*********************************
	 * ���� ������ �迭�� ��´�. 
	 *********************************/
	public void saveCodeInfoInArray(int level,int ancestor) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT mid,item_code,item_name,item_level FROM item_class WHERE item_level = '" + level + "' and item_ancestor = '" + ancestor + "' ORDER BY mid";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			goods[i][0] = rs.getString("item_level");
			goods[i][1] = rs.getString("item_name");
			goods[i][2] = rs.getString("mid");
			goods[i][3] = rs.getString("item_code");
			i++;
			saveCodeInfoInArray(Integer.parseInt(goods[i-1][0])+1, Integer.parseInt(goods[i-1][2]));
		}
		stmt.close();
		rs.close();
	} //saveCodeInfoInArray()

	/**********************************
	 * tree_items.js ������ �����Ѵ�.
	 **********************************/
	public void makeCodeTree(String jspath,String target,int level,int ancestor) throws Exception{

		getTotalCount();							// ��ü ���ڵ� ������ �ľ�
		goods = new String[this.total_count][4];	// ��ǰ����Ʈ�� ���� �迭 ����
		saveCodeInfoInArray(level,ancestor);
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

			String link		= "";	// ��ũURL�� ���� ����
			String sql		= "";

			bw.write("var TREE_ITEMS = [");	//���� ������
			bw.newLine();
			bw.write(" ['��üǰ��','" + target + "?mode=list_item',");	//���� ������
			bw.newLine();

			for(int bi=0; bi<this.total_count; bi++){

				if(target.equals("NA")) link = goods[bi][2] + "|" + goods[bi][1];
//				else link = target + "?mode=list_item&&category=" + goods[bi][3] + "&searchscope=item_no&searchword=" + goods[bi][3];
				else link = target + "?mode=list_item&&category=" + goods[bi][3];
				

				//������
				if(bi == 0) {
					st = Integer.parseInt(goods[bi][0]);
					space = " ";
					for(int s=0; s < st; s++) space += "   ";
					bw.write(space + "['"+goods[bi][1]+"','"+link+"'");
				} else if(bi == (this.total_count-1)) {
					cu = Integer.parseInt(goods[bi][0]);
					di = cu - st;
				
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						bw.write(",");		//����
						bw.newLine();
						bw.write(space + "['"+goods[bi][1]+"','"+link+"'],");		//������
						bw.newLine();
					} else if(di == 0) {
						bw.write("],");		//����
						bw.newLine();
						bw.write(space + "['"+goods[bi][1]+"','"+link+"'],");		//������
						bw.newLine();
					} else {
						bw.write("],");		//����
						bw.newLine();
						for(int m=0; m > di; di++) bw.write(space + "],"); //�շ��� ����
						bw.newLine();
						bw.write(space + "['"+goods[bi][1]+"','"+link+"'],");		//������
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
					cu = Integer.parseInt(goods[bi][0]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						bw.write(",");		//����
						bw.newLine();
						bw.write(space + "['"+goods[bi][1]+"','"+link+"'");		//������
					} else if(di == 0) {
						bw.write("],");		//����
						bw.newLine();
						bw.write(space + "['"+goods[bi][1]+"','"+link+"'");		//������
					} else {
						bw.write("],");		//����
						bw.newLine();
						for(int m=0; m > di; di++) bw.write(space + "],"); //�շ��� ����
						bw.newLine();
						bw.write(space + "['"+goods[bi][1]+"','"+link+"'");		//������
					}
					st = Integer.parseInt(goods[bi][0]);
				} //if	

			} //for
			bw.write("];");	//�� ������
			bw.newLine();

			bw.close();
		}else{
			IOException trouble = new IOException();
			throw trouble;
		}

	} //makeGoodsTree()


} //makeCodeTreeItems
