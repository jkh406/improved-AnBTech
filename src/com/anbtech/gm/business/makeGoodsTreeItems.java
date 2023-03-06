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
	private String[][] goods; // 제품리스트를 담을 배열 선언
	private String[] treeinfo = new String[10]; // 제품분류정보 담을 배열 

	private String whereStr = "";
	private String whereStr2 = "";
	private String test = "eror";

	/********************************
	 * db connection을 생성한다.
	 ********************************/
	public void openConnection() throws Exception{
		Properties prop = new Properties();
		String jdbc		= "";
		String url		= "";
		String user		= "";
		String password	= "";

		// 프로퍼티 파일을 로드한다.
		InputStream is = getClass().getResourceAsStream("../../dbconn/db.properties");
		prop.load(is);
		if(is!=null) is.close();

		// 프로퍼티 파일에서 프로퍼티를 읽는다. 
		jdbc = prop.getProperty("drivers");
		url = prop.getProperty("mssql.host");
		user = prop.getProperty("mssql.db_user");
		password = prop.getProperty("mssql.db_password");

		// connection을 생성한다.
		Class.forName(jdbc); 
	    this.con = DriverManager.getConnection(url,user,password); 
	}

	/***********************************
	 * 전체 제품 개수를 파악해서 저장 
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
	 * 제품구성 정보를 배열에 담는다. 
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
	 * tree_items.js 파일을 생성한다.
	 **********************************/
	public void makeGoodsTree(String jspath,String target,int level,int ancestor) throws Exception{
		Statement stmt = null;

		getTotalCount();							// 전체 레코드 개수를 파악

		if(this.total_count < 3) throw new Exception();
		else{
			goods = new String[this.total_count][4];	// 제품리스트를 담을 배열 선언
			saveGoodsInfoInArray(level,ancestor);


			File jsexist = new File(jspath);

			if(jsexist.exists()){
				BufferedWriter bw = new BufferedWriter(new FileWriter(jspath));
				//-------------------------------------------------
				//  Tree 구조를 만들 배열생성 (tree_items.jp 만들기)
				//--------------------------------------------------
				String space = " ";
				int st = 0;		//시작점 level
				int cu = 0;		//현 읽은 level
				int di = 0;		//차이 : cu - st 

				String link		= "";	// 링크URL을 담을 변수
				String pid_s	= "";	// pid를 담을 변수
				String [] pid	= new String[4];	//제품분류를 4단계로 고정
				String sql		= "";

				bw.write("var TREE_ITEMS = [");	//최초 시작점
				bw.newLine();
				bw.write(" ['제품정보','GoodsInfoServlet?mode=list_model',");	//최초 시작점
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

					//시작점
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
							bw.write(",");		//끝점
							bw.newLine();
							bw.write(space + "['"+goods[bi][1]+"','"+link+"'],");		//시작점
							bw.newLine();
						} else if(di == 0) {
							bw.write("],");		//끝점
							bw.newLine();
							bw.write(space + "['"+goods[bi][1]+"','"+link+"'],");		//시작점
							bw.newLine();
						} else {
							bw.write("],");		//끝점
							bw.newLine();
							for(int m=0; m > di; di++) bw.write(space + "],"); //앞레벨 끝점
							bw.newLine();
							bw.write(space + "['"+goods[bi][1]+"','"+link+"'],");		//시작점
							bw.newLine();
						}
						
						//마지막 레벨 닫기
						di = cu - 0;
						for(int e=di; e > 0; e--) {
							space = " ";
							for(int es=1; es < e; es++) space += "   ";
							bw.write(space + "],"); //끝점
							bw.newLine();
						}

					} else {
						cu = Integer.parseInt(goods[bi][0]);
						di = cu - st;
						
						space = " ";
						for(int s=0; s < cu; s++) space += "   ";

						if(di == 1) {
							bw.write(",");		//끝점
							bw.newLine();
							bw.write(space + "['"+goods[bi][1]+"','"+link+"'");		//시작점
						} else if(di == 0) {
							bw.write("],");		//끝점
							bw.newLine();
							bw.write(space + "['"+goods[bi][1]+"','"+link+"'");		//시작점
						} else {
							bw.write("],");		//끝점
							bw.newLine();
							for(int m=0; m > di; di++) bw.write(space + "],"); //앞레벨 끝점
							bw.newLine();
							bw.write(space + "['"+goods[bi][1]+"','"+link+"'");		//시작점
						}
						st = Integer.parseInt(goods[bi][0]);
					} //if	

				} //for
				bw.write("];");	//맨 마지막
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
	 * 선택된 모델의 상위 제품분류 문자열을 가져온다.(mid 값을 가지고)
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
	 * 선택된 모델의 상위 제품분류 문자열을 가져온다.(모델코드를 가지고)
	 *******************************************/
	public String getGoodsClassStrByModelCode(int mid,String model_code,String where) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		String sql = "";

		//맨처음 실행시에 넘어온 모델코드값을 가지고 mid 값을 가져온다.
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
	 * 선택된 모델의 제품분류 정보를 가져온다.(제품코드,제품명,모델코드,모델명)
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
