/**************************************************
 * 기술문서 모듈의 문서분류관련 트리 생성
 **************************************************/

package com.anbtech.dms.admin;
import java.io.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.odbc.*;

public class makeDocCategory{
	private Connection con;

	public makeDocCategory(Connection con){
		this.con = con;
	}

	public makeDocCategory() throws Exception{
		openConnection();
	}

	private StringBuffer sb = new StringBuffer();
	private String whereStr = "";

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
	
	/********************************
	 * 문서 카테고리 트리구조 출력 
	 ********************************/
	public StringBuffer viewCategoryTree(int level,int ancestor) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = " select * from category_data where c_level = '"+level+"' and c_ancestor = '"+ancestor+"'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String c_id = rs.getString("c_id");
			String c_level = rs.getString("c_level");
			String c_ancestor = rs.getString("c_ancestor");
			String c_name = rs.getString("c_name");
			String c_code = rs.getString("c_code");
			String enable_rev = rs.getString("enable_rev");
			if(enable_rev.equals("y")) enable_rev = "O"; else enable_rev = "X";

			String enable_pjt = rs.getString("enable_pjt");
			if(enable_pjt.equals("y")) enable_pjt = "O"; else enable_pjt = "X";

			String enable_model = rs.getString("enable_model");
			if(enable_model.equals("y")) enable_model = "O"; else enable_model = "X";

			String enable_eco = rs.getString("enable_eco");
			if(enable_eco.equals("y")) enable_eco = "O"; else enable_eco = "X";

			String enable_app = rs.getString("enable_app");
			if(enable_app.equals("y")) enable_app = "O"; else enable_app = "X";

			String security_level = rs.getString("security_level");
			String save_period = rs.getString("save_period");
			String loan_period = rs.getString("loan_period");

			String s_level = "";

	        if (Integer.parseInt(c_level) > 0) {
			    s_level = "└";
	            for (int i=1; i<Integer.parseInt(c_level); i++) s_level = "&nbsp;&nbsp;&nbsp;" + s_level;
			}

	        String s_add = "<a href='categoryInput.jsp?j=a&c_id="+c_id+"&level="+c_level+"'><img src='../images/lt_add_s.gif' border='0' align='absmiddle'></a>";
			String s_mod = "<a href='categoryInput.jsp?j=u&c_id="+c_id+"'><img src='../images/lt_modify.gif' border='0' align='absmiddle'></a>";
	        String s_del = "<a href='categoryProcess.jsp?j=d&c_id="+c_id+"'><img src='../images/lt_del.gif' border='0' align='absmiddle'></a>";

			if(security_level.equals("1")) { security_level="1급"; 
			}else if(security_level.equals("2")) { security_level="2급"; 
				}else if(security_level.equals("3")) { security_level="3급"; 
					}else if(security_level.equals("4")) { security_level="대외비"; 
						}else { security_level="일  반"; }

				
			if(save_period.equals("1"))	{ save_period="1년"; 
			} else if(save_period.equals("3")) { save_period="3년"; 
				} else if(save_period.equals("5")) { save_period="5년"; 
					} else { save_period="영  구"; }

			sb.append("<TR onmouseover=this.style.backgroundColor='#F5F5F5' onmouseout=this.style.backgroundColor='' bgColor=#ffffff>");
			sb.append("<TD align=left height='24' class='list_bg'>&nbsp;"+s_level+c_name+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+c_code+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+enable_rev+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+enable_pjt+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+enable_model+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+enable_eco+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+enable_app+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+security_level+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+save_period+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+loan_period+"</td><TD><IMG height=1 width=1></TD>");
			sb.append("<TD align=middle height='24' class='list_bg'>"+s_add+" "+s_mod+" "+s_del+"</td>");
			sb.append("</TR><TR><TD colSpan=21 background='../images/dot_line.gif'></TD></TR>");

			viewCategoryTree(Integer.parseInt(c_level)+1,Integer.parseInt(c_id));
		}
		stmt.close();
		rs.close();

		return sb;
	} //viewCategoryTree


	/**********************************
	 * 현재 카테고리의 상위 분류를 출력
	 **********************************/
	public String viewCategory(int category,String where) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = "select c_name,c_ancestor from category_data where c_id = '"+category+"'";
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			String c_name = rs.getString("c_name");
			int c_ancestor = Integer.parseInt(rs.getString("c_ancestor"));

			if(whereStr.equals("")){
				whereStr=" "+c_name;
			} else {
				whereStr=c_name+" > "+where;
			}

			//whereStr = c_name + " > " + where;
			viewCategory(c_ancestor,whereStr);
		}
		
		stmt.close();
		rs.close();

		return whereStr;
	}//viewCategory()
} //makeDocCategory