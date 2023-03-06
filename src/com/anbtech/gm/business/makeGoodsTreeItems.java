package com.anbtech.gm.business;

import java.io.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.odbc.*;

public class makeGoodsTreeItems{
	private Connection con;

	public makeGoodsTreeItems(Connection con){
		this.con = con;
	}

	public makeGoodsTreeItems() throws Exception{
		openConnection();
	}
	
	private int i = 0;
	private int total_count = 0;
	private String[][] goods; // ��ǰ����Ʈ�� ���� �迭 ����
	private String[] treeinfo = new String[10]; // ��ǰ�з����� ���� �迭 

	private String whereStr = "";
	private String whereStr2 = "";
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
		String sql = "SELECT COUNT(*) FROM goods_structure";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			this.total_count = rs.getInt(1);
		}

		stmt.close();
		rs.close();
	} //getTotalCount

	
	/*********************************
	 * ��ǰ���� ������ �迭�� ��´�. 
	 *********************************/
	public void saveGoodsInfoInArray(int level,int ancestor) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT glevel,code,name,mid FROM goods_structure WHERE glevel = '" + level + "' and ancestor = '" + ancestor + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			goods[i][0] = rs.getString("glevel");
			
			if(goods[i][0].equals("4")) goods[i][1] = rs.getString("name") + "(" + rs.getString("code") + ")";
			else goods[i][1] = rs.getString("name");
			
			goods[i][2] = rs.getString("mid");
			goods[i][3] = rs.getString("code");
			i++;
			saveGoodsInfoInArray(Integer.parseInt(goods[i-1][0])+1, Integer.parseInt(goods[i-1][2]));
		}
		stmt.close();
		rs.close();
	} //saveGoodsInfoInArray()

	/**********************************
	 * tree_items.js ������ �����Ѵ�.
	 **********************************/
	public void makeGoodsTree(String jspath,String target,int level,int ancestor) throws Exception{
		Statement stmt = null;

		getTotalCount();							// ��ü ���ڵ� ������ �ľ�

		if(this.total_count < 3) throw new Exception();
		else{
			goods = new String[this.total_count][4];	// ��ǰ����Ʈ�� ���� �迭 ����
			saveGoodsInfoInArray(level,ancestor);


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
				String pid_s	= "";	// pid�� ���� ����
				String [] pid	= new String[4];	//��ǰ�з��� 4�ܰ�� ����
				String sql		= "";

				bw.write("var TREE_ITEMS = [");	//���� ������
				bw.newLine();
				bw.write(" ['��ǰ����','GoodsInfoServlet?mode=list_model',");	//���� ������
				bw.newLine();

				for(int bi=0; bi<this.total_count; bi++){

					for(int lvl = 0; lvl <= 4; lvl++){
						if(goods[bi][0].equals(Integer.toString(lvl+1))) pid[lvl] = Integer.toString(bi+1);
					}

					pid_s = "";
					for(int j=0;j<Integer.parseInt(goods[bi][0]);j++){
						pid_s += pid[j]+",";
					}

					if(target.equals("NA")) link = goods[bi][2] + "|" + goods[bi][1] + "|" + goods[bi][3];
					else link = target + "?no=" + goods[bi][2] + "&pid=" + pid_s;

					sql = "UPDATE goods_structure SET pid = '" + pid_s + "' where mid = '" + goods[bi][2] + "'";
					stmt = con.createStatement();
					stmt.executeUpdate(sql);

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

			stmt.close();
		}
	} //makeGoodsTree()


	/*******************************************
	 * ���õ� ���� ���� ��ǰ�з� ���ڿ��� �����´�.(mid ���� ������)
	 *******************************************/
	public String getGoodsClassStr(int mid,String where) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT name,ancestor,glevel FROM goods_structure WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			String name = rs.getString("name");
			int ancestor = Integer.parseInt(rs.getString("ancestor"));
			if(Integer.parseInt(rs.getString("glevel")) < 4) whereStr = name + " > " + where;
			getGoodsClassStr(ancestor,whereStr);
		}
		
		stmt.close();
		rs.close();

		return whereStr;
	}//getGoodsClassStr

	/*******************************************
	 * ���õ� ���� ���� ��ǰ�з� ���ڿ��� �����´�.(���ڵ带 ������)
	 *******************************************/
	public String getGoodsClassStrByModelCode(int mid,String model_code,String where) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String sql = "";

		//��ó�� ����ÿ� �Ѿ�� ���ڵ尪�� ������ mid ���� �����´�.
		if(!model_code.equals("")){
			sql = "SELECT mid FROM goods_structure WHERE code = '" + model_code + "'";
			rs = stmt.executeQuery(sql);
			rs.next();
			mid = rs.getInt("mid");
		}

		sql = "SELECT name,ancestor,glevel FROM goods_structure WHERE mid = '" + mid + "'";
//		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			String name = rs.getString("name");
			int ancestor = Integer.parseInt(rs.getString("ancestor"));
			if(Integer.parseInt(rs.getString("glevel")) < 4) whereStr = name + " > " + where;
			getGoodsClassStrByModelCode(ancestor,"",whereStr);
		}
		
		stmt.close();
		rs.close();

		return whereStr;
	}//getGoodsClassStrByModelCode

	/*****************************************************************
	 * ���õ� ���� ��ǰ�з� ������ �����´�.(��ǰ�ڵ�,��ǰ��,���ڵ�,�𵨸�)
	 *****************************************************************/
	public String getReturnValue(int mid,String where) throws Exception{
		Statement stmt2 = null;
		ResultSet rs2 = null;
		
		String sql = "SELECT name,ancestor,glevel,code FROM goods_structure WHERE mid = '" + mid + "'";
		stmt2 = con.createStatement();
		rs2 = stmt2.executeQuery(sql);

		while(rs2.next()){
			String name = rs2.getString("name")==null?" ":rs2.getString("name");
			String code = rs2.getString("code")==null?" ":rs2.getString("code");
			
			if(rs2.getString("glevel").equals("1")) { 
				whereStr2 = code + "|" + name + where;
			} else {
				whereStr2 = "|"+code + "|"+name + where;
			}
			int ancestor = Integer.parseInt(rs2.getString("ancestor"));
			getReturnValue(ancestor,whereStr2);
		} 
		
		stmt2.close();
		rs2.close();
		
		return whereStr2;
	}//getReturnValue


} //makeGoodsTreeItems
