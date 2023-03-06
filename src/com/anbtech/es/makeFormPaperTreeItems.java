/*******************************************************
 * 제품정보구조 트리출력용 스크립트파일(tree.js)을 생성
 * 작성자:ANB TECH. 대리 박동렬
 *******************************************************/

package com.anbtech.es;
import java.io.*;
import java.sql.*;
import java.util.*;

public class makeFormPaperTreeItems{
	private String jdbc = "";
	private String url = "";
	private String user = "";
	private String password = "";

	private int i = 0;
	private static int total_count = 0;
	private String[][] goods; // 제품리스트를 담을 배열 선언

	private String whereStr = "";

	private String test = "eror";

	public makeFormPaperTreeItems() 
	{

	}

	/***********************************
	 * connection 개체 생성
	 ***********************************/
	public boolean openConnection() 
	{

		//---------------------------------------------------------------------
		// STEP 1. 프로퍼티 파일을 로드한다.
		//---------------------------------------------------------------------
		Properties prop = new Properties();

		try {
			InputStream is = getClass().getResourceAsStream("../dbconn/db.properties");
			prop.load(is);
			if(is!=null) is.close();
			//---------------------------------------------------------------------
			// STEP 2. 프로퍼티 파일에서 프로퍼티를 읽는다. 
			//---------------------------------------------------------------------
			jdbc = prop.getProperty("drivers");
			url = prop.getProperty("mssql.host");
			user = prop.getProperty("mssql.db_user");
			password = prop.getProperty("mssql.db_password");
			return true;

		} catch(IOException e) {
			System.out.println("[DbConnection] 파일 오픈 중 에러");
			return false;
		}
	}
	
	/***********************************
	 * 전체 레코드 개수를 파악해서 저장 
	 ***********************************/
	public void getTotalCount()
	{
	  try {

		openConnection();

		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		int total_count = 0;
		String sql = "select count(*) from formpapers_structure";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			this.total_count = rs.getInt(1);
		}

		stmt.close(); 
	    Conn.close();

	  }catch (Exception e){

      }
	} //getTotalCount

	public String getTest(){
		return this.test;
	}
	
	/*********************************
	 * 제품구성 정보를 배열에 담는다. 
	 *********************************/
	public boolean saveFormPaperArray(int ag_level,int ancestor) 
	{
	  try {
		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = " select ag_level,ag_name,ag_id,ag_name2 from formpapers_structure where ag_level = '"+ag_level+"' and ag_ancestor = '"+ancestor+"'";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			goods[i][0] = rs.getString("ag_level");		//level
			goods[i][1] = rs.getString("ag_name");		//한글명
			goods[i][2] = rs.getString("ag_id");		//관리번호
			goods[i][3] = rs.getString("ag_name2");		//영문명
			i++;
			saveFormPaperArray(Integer.parseInt(goods[i-1][0])+1, Integer.parseInt(goods[i-1][2]));

		}
		stmt.close(); 
	    Conn.close();
		return true;
	  }catch (Exception e){
		return false;
      }
	} //saveGoodsArray()

	/**********************************
	 * tree_items.js 파일을 생성한다.
	 **********************************/
	public boolean makeFormPaperTree(int ag_level,int ancestor){
	  try {

		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		getTotalCount();					// 전체 레코드 개수를 파악
		goods = new String[this.total_count][4]; // 제품리스트를 담을 배열 선언
		
		boolean data = saveFormPaperArray(ag_level,ancestor);

//		String jspath = "C:/tomcat4/webapps/anb/gw/approval/SpaceLink/tree_items.js";
		String jspath = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/gw/approval/SpaceLink/tree_items.js";
		File jsexist = new File(jspath);

		if(jsexist.exists()&& data){
			BufferedWriter bw = new BufferedWriter(new FileWriter(jspath));

			//-------------------------------------------------
			//  Tree 구조를 만들 배열생성 (tree_items.jp 만들기)
			//--------------------------------------------------
			String space = " ";
			int st = 0;		//시작점 level
			int cu = 0;		//현 읽은 level
			int di = 0;		//차이 : cu - st 

			String link = "";	// 링크URL을 담을 변수
			String tmp = "";	// 
			String [] pid = new String[4];
			String sql = "";

			bw.write("var TREE_ITEMS = [");	//최초 시작점
			bw.newLine();

			for(int bi=0; bi<this.total_count; bi++){


				if(goods[bi][0].equals("1")) pid[0] = Integer.toString(bi);
				if(goods[bi][0].equals("2")) pid[1] = Integer.toString(bi);
				if(goods[bi][0].equals("3")) pid[2] = Integer.toString(bi);
				if(goods[bi][0].equals("4")) pid[3] = Integer.toString(bi);
				tmp = "";
				for(int j=0;j<Integer.parseInt(goods[bi][0]);j++){
					tmp += pid[j]+",";
				}
				link = "FormPapersInfo.jsp?ag_name2="+goods[bi][3]+"&p_id="+tmp;

				sql = "update formpapers_structure set p_id = '"+tmp+"' where ag_id = '"+goods[bi][2]+"'";
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
		return true;
	  }catch (Exception e){
		return false;
      }
	} //makeGoodsTree()


	/**********************************
	 * 현재 카테고리의 상위 분류를 출력
	 **********************************/
	public String viewCategory(int category,String where) throws Exception{
		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = "select ag_name,ag_ancestor from formpapers_structure where ag_id = '"+category+"'";
		ResultSet rs = stmt.executeQuery(sql);

		while(rs.next()){
			String ag_name = rs.getString("ag_name");
			int ag_ancestor = Integer.parseInt(rs.getString("ag_ancestor"));

			if(whereStr.equals("")){
				whereStr=" "+ag_name;
			}else {
				whereStr=ag_name+" > "+where;
			}
			viewCategory(ag_ancestor,whereStr);
		}
		
		stmt.close();
		Conn.close();

		return whereStr;
	}//viewCategory()

} //makeTreeItems