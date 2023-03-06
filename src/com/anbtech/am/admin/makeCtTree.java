package com.anbtech.am.admin;
import java.io.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.odbc.*;

public class makeCtTree{
	private String jdbc = "";
	private String url = "";
	private String user = "";
	private String password = "";

	private StringBuffer sb = new StringBuffer();
	private String whereStr = "&nbsp;";
	private String comboList = "";


	/********************************
	 * db.properties 파일을 읽어온다.
	 ********************************/
	public boolean openConnection() 
	{

		Properties prop = new Properties();

		try {
			//---------------------------------------------------------------------
			// STEP 1. 프로퍼티 파일을 로드한다.
			//---------------------------------------------------------------------
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
	
	/********************************************
	 * 분류 구성 트리구조 출력 (관리자 화면용)
	 ********************************************/
	public StringBuffer viewTree(int level,int parent) throws Exception{

		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = " SELECT c_no,ct_id,ct_level,ct_parent,ct_word,ct_name,dc_percent,ct_dc_bound From as_category WHERE ct_level = '"+level+"' and ct_parent = '"+parent+"'";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String s_level = "";
			String ct_id = rs.getString("ct_id");
			String c_no = rs.getString("c_no");
			String ct_name = rs.getString("ct_name");
			String ct_word = rs.getString("ct_word");
			String ct_level = rs.getString("ct_level");
			String ct_parent = rs.getString("ct_parent");
			String dc_percent = rs.getString("dc_percent");
			String ct_dc_bound = rs.getString("ct_dc_bound");

	        if (Integer.parseInt(ct_level) > 0) {
			    s_level = "└ ";
	            for (int i=1; i<Integer.parseInt(ct_level); i++) s_level = "&nbsp;&nbsp;&nbsp;" + s_level;
			}

	        String s_add = "<a href='../servlet/AssetServlet?mode=category_info_view&div=a&ct_id="+ct_id+"'><IMG src='../am/images/lt_add_s.gif' align='absmiddle' border='0'></a>";
			String s_mod = "<a href='../servlet/AssetServlet?mode=category_info_view&div=m&ct_id="+ct_id+"'><IMG src='../am/images/lt_modify.gif' align='absmiddle' border='0'></a>";
	        String s_del = "<a href='../servlet/AssetServlet?mode=category_info_view&div=d&ct_id="+ct_id+"&c_no="+c_no+"'><IMG src='../am/images/lt_del.gif' align='absmiddle' border='0'></a>";

			sb.append("<TR onmouseover=\"this.style.backgroundColor='#F5F5F5'\" onmouseout=\"this.style.backgroundColor=''\" bgColor='#ffffff'>");
			sb.append("<TD height='24' class='list_bg' align=left style='padding-left:10px'>"+s_level+ct_name+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center>"+ct_level+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center>"+ct_word+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center>"+s_add+" "+s_mod+" "+s_del+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center></TD>");
			sb.append("</TR>");
			sb.append("<TR><TD colSpan=9 background=\"../am/images/dot_line.gif\"></TD></TR>");

			viewTree(Integer.parseInt(ct_level)+1, Integer.parseInt(c_no));
		}
		stmt.close(); 
	    Conn.close();
		return sb;

	} //viewTree

	/***************************************************
	 * 해당 분류의 상위 분류을 출력 
	 ***************************************************/
	public String viewHistory(int ac_id,String where){
	  try {
		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql;
	
	    sql = " select ac_name,ac_ancestor from class_table where ac_id = '"+ac_id+"'";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			whereStr = " > <a href='main.jsp?ac_id="+ac_id+"'>"+rs.getString("ac_name")+"</a>"+where;
			viewHistory(Integer.parseInt(rs.getString("ac_ancestor")),whereStr);
		}

		stmt.close(); 
	    Conn.close();
		return whereStr;
	  }catch (Exception e){
		return whereStr;
      }
	} //viewHistory

	/****************************************************
	 * 해당 분류의 상위 분류을 출력 
	 ****************************************************/
	public String viewHistoryV(int ac_id,String where){
	  try {
		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql;
	
	    sql = " select ac_name,ac_ancestor from class_table where ac_id = '"+ac_id+"'";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			whereStr = "<font color=#FFFFFF> > </font><a href='userinfo.jsp?ac_id="+ac_id+"'>"+"<font color=#FFFFFF>"+rs.getString("ac_name")+"</font></a>"+where;
			viewHistoryV(Integer.parseInt(rs.getString("ac_ancestor")),whereStr);
		}

		stmt.close(); 
	    Conn.close();
		return whereStr;
	  }catch (Exception e){
		return whereStr;
      }
	} //viewHistoryV()

	/****************************************************
	 * 해당 분류의 상위 분류을 출력 (분기 program추가)
	 ****************************************************/
	public String viewHistoryV(String pg,String rpg,String rnm,int ac_id,String where){
	  try {
		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql;
	
	    sql = " select ac_name,ac_ancestor from class_table where ac_id = '"+ac_id+"'";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			whereStr = "<font color=#FFFFFF> > </font><a href='"+pg+"?ac_id="+ac_id+"&pg="+rpg+"&nm="+rnm+"'>"+"<font color=#FFFFFF>"+rs.getString("ac_name")+"</font></a>"+where;
			viewHistoryV(pg,rpg,rnm,Integer.parseInt(rs.getString("ac_ancestor")),whereStr);
		}

		stmt.close(); 
	    Conn.close();
		return whereStr;
	  }catch (Exception e){
		return whereStr;
      }
	} //viewHistoryV()

	/******************************************************
	 * 분류 구성 콤보박스 출력 (ct_id를 value로 가지는 경우)
	 ******************************************************/
	public String viewCombo(int ct_level,int ancestor){
	  try {
		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = " select * from as_category where ct_level = '"+ct_level+"' and ct_parent = '"+ancestor+"'";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String s_level = "";
			String c_no = rs.getString("c_no");
	        if (Integer.parseInt(rs.getString("ct_level")) > 0) {
			    s_level = "└ ";
	            for (int i=1; i<Integer.parseInt(rs.getString("ct_level")); i++) s_level = "&nbsp;&nbsp;&nbsp;" + s_level;
			}

			comboList += "<option value='"+rs.getString("c_no")+"'>"+s_level+rs.getString("ct_name")+"</option>";

			viewCombo(Integer.parseInt(rs.getString("ct_level"))+1, Integer.parseInt(rs.getString("c_no")));
		}
		stmt.close(); 
	    Conn.close();
		return comboList;
	  }catch (Exception e){
		return comboList;
      }
	} //viewCombo()


	/******************************************************
	 * 분류 구성 콤보박스 출력 (ct_word를 value로 가지는 경우)
	 ******************************************************/
	public String viewCombo2(int ct_level,int ancestor){
	  try {
		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		String sql = " select * from as_category where ct_level = '"+ct_level+"' and ct_parent = '"+ancestor+"'";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String s_level = "";

	        if (Integer.parseInt(rs.getString("ct_level")) > 0) {
			    s_level = "└";
	            for (int i=1; i<Integer.parseInt(rs.getString("ct_level")); i++) s_level = "&nbsp;&nbsp;&nbsp;" + s_level;
			}

			comboList += "<option value='"+rs.getString("ct_word")+"'>"+s_level+rs.getString("ac_name")+"</option>";

			viewCombo2(Integer.parseInt(rs.getString("ct_level"))+1, Integer.parseInt(rs.getString("ct_no")));
		}
		stmt.close(); 
	    Conn.close();
		return comboList;
	  }catch (Exception e){
		return comboList;
      }
	} //viewCombo2()

	/**********************************
	 * 현재 카테고리의 상위 분류를 출력
	 **********************************/
	public String viewCategory(String category,String where) throws Exception{
		openConnection();
		Class.forName(this.jdbc); 
	    Connection Conn = DriverManager.getConnection(this.url,this.user,this.password); 
	    Statement stmt = Conn.createStatement();

		//String sql = "select ct_name,ct_parent from as_category where c_id = '"+category+"'";
		String sql = "SELECT ct_name, ct_parent FROM as_category WHERE c_no='"+category+"' and ct_parent != '0'";
		ResultSet rs = stmt.executeQuery(sql);

		while(rs.next()){
			String ct_name = rs.getString("ct_name");
			String ct_parent = rs.getString("ct_parent");

			if(whereStr.equals("")){
				whereStr=" "+ct_name;
			} else {
				whereStr=ct_name+" > "+where;
			}

			//whereStr = ct_name + " > " + where;
			viewCategory(ct_parent,whereStr);
		}
		
		stmt.close();
		Conn.close();
		return whereStr;

	}//viewCategory()


} //makeCtTree