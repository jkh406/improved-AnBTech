/*******************************************************
 * 사용자 정보 트리출력용 스크립트파일(tree.js)을 생성
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

	private String[][] class_info; // 부서조직 정보를 담을 배열
	private String[][] total_info; // 부서조직 및 사용자 정보를 담을 배열

	public makeUserTreeItems() 
	{
		openConnection();
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
			InputStream is = getClass().getResourceAsStream("../../dbconn/db.properties");
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
	
	/******************************
	 * 부서조직 수를 파악해서 저장 
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
	 * 사용자 수를 파악해서 저장 
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
	 * 조직 정보를 배열에 담는다. 
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
	 * 조직 구성의 최대 레벨을 구한다.
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
	 * 해당 부서에 속한 사용자 정보를 배열에 담는다.
	 *************************************************/
	public void saveUserInfoInArray(int ac_level,int ac_ancestor) throws Exception{
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();
		getClassTotalCount();	// 부서 개수 파악
		getUserTotalCount();	// 사용자 개수 파악

		class_info = new String[this.class_count][3];
		saveClassInfoInArray(ac_level,ac_ancestor);
		
		this.total_count = this.class_count + this.user_count;
		total_info = new String[this.total_count][4];

		int u = 0;
		for (int c=0; c<this.class_count; c++)
		{
			total_info[u][0] = class_info[c][0];	// 레벨
			total_info[u][1] = class_info[c][1];	// 이름
			total_info[u][2] = class_info[c][2];	// 코드
			total_info[u][3] = "div";				// 부서임을 나타냄.
			int class_level = Integer.parseInt(class_info[c][0]);

//System.out.print(total_info[u][0] + " : " + total_info[u][1] + " : " + total_info[u][0] + "\n");

			String sql = "SELECT a.au_id,a.ac_id,a.name,a.id,b.ar_name FROM user_table a,rank_table b WHERE a.ac_id = '" + class_info[c][2] + "' and a.rank = b.ar_code order by b.ar_priorty ASC";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				u++;
			
				total_info[u][0] = Integer.toString(class_level + 1);	// 부서레벨 + 1단계
				total_info[u][1] = rs.getString("ar_name") + "/" + rs.getString("name");
				total_info[u][2] = rs.getString("id");
				total_info[u][3] = "usr";				// 사용자임을 나타냄.

//System.out.print(total_info[u][0] + " : " + total_info[u][1] + " : " + total_info[u][0] + "\n");

			}
			u++;
		}

		stmt.close(); 
	    Conn.close();

	} //saveUserInfoInArray()

	/*********************************
	 * 해당 사용자가 부서장인지를 판단한다.
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
	 * tree_items.js 파일을 생성한다.
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
			//  Tree 구조를 만들 배열생성 (tree_items.jp 만들기)
			//--------------------------------------------------
			String space = " ";
			int st = 0;		//시작점 level
			int cu = 0;		//현 읽은 level
			int di = 0;		//차이 : cu - st 

			String link = "";	// 링크URL을 담을 변수
			
			int max_level = getMaxLevel();
			String pid_s = "";	// pid를 담을 변수
			String [] pid = new String[max_level + 1];
			String sql = "";

			bw.write("var TREE_ITEMS = [");	//최초 시작점
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

			
				//해당 사번이 부서장의 권한이 있는지 체크한다.(부서장은 전자결재선 지정시 자신 지정 가능하게 하기 위함)
				String is_chief = "N";
				if(total_info[bi][3].equals("usr") && isChief(total_info[bi][2])) is_chief = "Y";

				if(total_info[bi][3].equals("usr") && target != "NA") link = target + "?type="+total_info[bi][3]+"&sid="+total_info[bi][2]+"&pid="+pid_s;
				else if(total_info[bi][3].equals("div") && target != "NA") link = "";
				else if(total_info[bi][3].equals("usr") && target == "NA") link = "usr|" + total_info[bi][2] + "/" + total_info[bi][1] + "|" + is_chief;
				else if(total_info[bi][3].equals("div") && target == "NA") link = "div|" + total_info[bi][2] + "/" + total_info[bi][1];

				//시작점
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
						bw.write(",");		//끝점
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'],");		//시작점
						bw.newLine();
					} else if(di == 0) {
						bw.write("],");		//끝점
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'],");		//시작점
						bw.newLine();
					} else {
						bw.write("],");		//끝점
						bw.newLine();
						for(int m=0; m > di; di++) bw.write(space + "],"); //앞레벨 끝점
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'],");		//시작점
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
					cu = Integer.parseInt(total_info[bi][0]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						bw.write(",");		//끝점
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'");		//시작점
					} else if(di == 0) {
						bw.write("],");		//끝점
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'");		//시작점
					} else {
						bw.write("],");		//끝점
						bw.newLine();
						for(int m=0; m > di; di++) bw.write(space + "],"); //앞레벨 끝점
						bw.newLine();
						bw.write(space + "['"+total_info[bi][1]+"','"+link+"'");		//시작점
					}
					st = Integer.parseInt(total_info[bi][0]);
				} //if	

			} //for
			bw.write("];");	//맨 마지막
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
