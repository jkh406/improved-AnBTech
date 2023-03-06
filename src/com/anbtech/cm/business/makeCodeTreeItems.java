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
	private String[][] goods; // 리스트를 담을 배열 선언

	private String whereStr = "";
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
	 * 구성 정보를 배열에 담는다. 
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
	 * tree_items.js 파일을 생성한다.
	 **********************************/
	public void makeCodeTree(String jspath,String target,int level,int ancestor) throws Exception{

		getTotalCount();							// 전체 레코드 개수를 파악
		goods = new String[this.total_count][4];	// 제품리스트를 담을 배열 선언
		saveCodeInfoInArray(level,ancestor);
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
			String sql		= "";

			bw.write("var TREE_ITEMS = [");	//최초 시작점
			bw.newLine();
			bw.write(" ['전체품목','" + target + "?mode=list_item',");	//최초 시작점
			bw.newLine();

			for(int bi=0; bi<this.total_count; bi++){

				if(target.equals("NA")) link = goods[bi][2] + "|" + goods[bi][1];
//				else link = target + "?mode=list_item&&category=" + goods[bi][3] + "&searchscope=item_no&searchword=" + goods[bi][3];
				else link = target + "?mode=list_item&&category=" + goods[bi][3];
				

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

	} //makeGoodsTree()


} //makeCodeTreeItems
