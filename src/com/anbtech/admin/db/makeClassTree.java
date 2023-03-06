package com.anbtech.admin.db;
import java.io.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.odbc.*;

public class makeClassTree{
	private Connection con;

	public makeClassTree(Connection con){
		this.con = con;
	}

	public makeClassTree() throws Exception{
		openConnection();
	}

	
	private StringBuffer sb = new StringBuffer();
	private String whereStr = "&nbsp;";
	private String comboList = "";

	private ArrayList div_list = new ArrayList();
	private com.anbtech.es.geuntae.entity.GeunTaeInfoTable table = new com.anbtech.es.geuntae.entity.GeunTaeInfoTable();

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
	
	/********************************************
	 * ������ ȭ���� ����Ʈ�� ������ ǥ ���·� ���
	 ********************************************/
	public StringBuffer viewTree(int level,int ancestor) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = "SELECT * FROM class_table WHERE ac_level = '" + level + "' and ac_ancestor = '" + ancestor + "' and isuse = 'y'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String s_level = "";
			String ac_id	= rs.getString("ac_id");
			String ac_name	= rs.getString("ac_name");
			String ac_code	= rs.getString("ac_code");
			String ac_level = rs.getString("ac_level");
			String code		= rs.getString("code");
			String chief_id	= rs.getString("chief_id")==null?"":rs.getString("chief_id");
			String isuse	= rs.getString("isuse").equals("y")?"Ȱ��ȭ":"��Ȱ��ȭ";

	        if (Integer.parseInt(ac_level) > 0) {
			    s_level = "��";
	            for (int i=1; i<Integer.parseInt(ac_level); i++) s_level = "&nbsp;&nbsp;&nbsp;" + s_level;
			}

	        String s_add = "<a href='classi.jsp?j=a&ac_id="+ac_id+"&level="+ac_level+"&code="+code+"'><img src='../images/lt_add_s.gif' border='0' align='absmiddle'></a>";
			String s_mod = "<a href='classi.jsp?j=u&ac_id="+ac_id+"'><img src='../images/lt_modify.gif' border='0' align='absmiddle'></a>";
	        String s_del = "<a href='classp.jsp?j=d&ac_id="+ac_id+"'><img src='../images/lt_del.gif' border='0' align='absmiddle'></a>";

			sb.append("<tr onmouseover=\"this.style.backgroundColor='#F5F5F5'\" onmouseout=\"this.style.backgroundColor=''\" bgColor='#ffffff'>");
			sb.append("<TD height='24' class='list_bg' align=left style='padding-left:10px'>"+s_level+ac_name+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center>"+ac_level+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center>"+ac_code+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center>" +chief_id+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center>" +isuse+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center>"+s_add+" "+s_mod+" "+s_del+"</TD>");
			sb.append("<TD><IMG height=1 width=1></TD>");
			sb.append("<TD class='list_bg' align=center></TD>");
			sb.append("</TR>");
			sb.append("<TR><TD colSpan=13 background=\"../images/dot_line.gif\"></TD></TR>");

			viewTree(Integer.parseInt(ac_level)+1, Integer.parseInt(ac_id));
		}
		stmt.close(); 
	    rs.close();

		return sb;

	} //viewTree

	/***************************************************
	 * ������ ȭ���� ����� �������� �ش� ������ ����
	 * ���� ���ڿ��� �����. 
	 * ��) �ٹ���ũ > EDA ����� > ���1�� >
	 ***************************************************/
	public String viewHistory(int ac_id,String where) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = "select ac_name,ac_ancestor from class_table where ac_id = '"+ac_id+"' and isuse = 'y'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			whereStr = "<a href='userl.jsp?ac_id=" + ac_id + "'>" + rs.getString("ac_name") + "</a> <img src='../images/arrow_next.gif' border='0' align='absmiddle' hspace='2'> " + where;
			viewHistory(Integer.parseInt(rs.getString("ac_ancestor")),whereStr);
		}

		stmt.close(); 
	    rs.close();

		return whereStr;
	} //viewHistory

	/***************************************************
	 * ������ ȭ���� ����� �������� �ش� ������ ����
	 * ���� ���ڿ��� �����. 
	 * ��) �ٹ���ũ > EDA ����� > ���1�� >
	 ***************************************************/
	public String viewHistory_str(int ac_id,String where) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = "select ac_name,ac_ancestor from class_table where ac_id = '"+ac_id+"' and isuse = 'y'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			whereStr = "<a href='#'" + ac_id + "'>" + rs.getString("ac_name") + "</a> <img src='../images/arrow_next.gif' border='0' align='absmiddle' hspace='2'> " + where;
			viewHistory_str(Integer.parseInt(rs.getString("ac_ancestor")),whereStr);
		}

		stmt.close(); 
	    rs.close();

		return whereStr;
	} //viewHistory_str

	/****************************************************
	 * �ش� ������ ���� ������ ��� 
	 ****************************************************/
	public String viewHistoryV(int ac_id,String where) throws Exception{ 
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = " select ac_name,ac_ancestor from class_table where ac_id = '"+ac_id+"' and isuse = 'y'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			whereStr = "<font color=#FFFFFF> > </font><a href='userinfo.jsp?ac_id="+ac_id+"'>"+"<font color=#FFFFFF>"+rs.getString("ac_name")+"</font></a>"+where;
			viewHistoryV(Integer.parseInt(rs.getString("ac_ancestor")),whereStr);
		}

		stmt.close(); 
	    rs.close();

		return whereStr;
	} //viewHistoryV()

	/****************************************************
	 * �ش� ������ ���� ������ ��� (�б� program�߰�)
	 ****************************************************/
	public String viewHistoryV(String pg,String rpg,String rnm,int ac_id,String where) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = " select ac_name,ac_ancestor from class_table where ac_id = '"+ac_id+"' and isuse = 'y'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			whereStr = "<font color=#FFFFFF> > </font><a href='"+pg+"?ac_id="+ac_id+"&pg="+rpg+"&nm="+rnm+"'>"+"<font color=#FFFFFF>"+rs.getString("ac_name")+"</font></a>"+where;
			viewHistoryV(pg,rpg,rnm,Integer.parseInt(rs.getString("ac_ancestor")),whereStr);
		}

		stmt.close(); 
	    rs.close();

		return whereStr;
	} //viewHistoryV()

	/******************************************************
	 * ���� ���� �޺��ڽ� ��� (ac_id�� value�� ������ ���)
	 ******************************************************/
	public String viewCombo(int ac_level,int ancestor) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = " select * from class_table where ac_level = '"+ac_level+"' and ac_ancestor = '"+ancestor+"' and isuse = 'y'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String s_level = "";

	        if (Integer.parseInt(rs.getString("ac_level")) > 0) {
			    s_level = "";
	            for (int i=1; i<Integer.parseInt(rs.getString("ac_level")); i++) s_level = "&nbsp;&nbsp;&nbsp;" + s_level;
			}

			comboList += "<option value='"+rs.getString("ac_id")+"'>"+s_level+rs.getString("ac_name")+"</option>";

			viewCombo(Integer.parseInt(rs.getString("ac_level"))+1, Integer.parseInt(rs.getString("ac_id")));
		}
		stmt.close(); 
	    rs.close();

		return comboList;
	} //viewCombo()


	/******************************************************
	 * ���� ���� �޺��ڽ� ��� (ac_code�� value�� ������ ���)
	 ******************************************************/
	public String viewCombo2(int ac_level,int ancestor) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = " select * from class_table where ac_level = '"+ac_level+"' and ac_ancestor = '"+ancestor+"' and isuse = 'y'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String s_level = "";

	        if (Integer.parseInt(rs.getString("ac_level")) > 0) {
			    s_level = "";
	            for (int i=1; i<Integer.parseInt(rs.getString("ac_level")); i++) s_level = "&nbsp;&nbsp;&nbsp;" + s_level;
			}

			comboList += "<option value='"+rs.getString("ac_code")+"'>"+s_level+rs.getString("ac_name")+"</option>";

			viewCombo2(Integer.parseInt(rs.getString("ac_level"))+1, Integer.parseInt(rs.getString("ac_id")));
			
		}

		stmt.close(); 
	    rs.close();

		return comboList;
	} //viewCombo2()


	/******************************************************
	 * ���� ���� �޺��ڽ� ���
	 * class_table�� code �ʵ尪�� �׸����� �Ѱ��ִ� �޼�����.
	 ******************************************************/
	public String viewComboByCode(int ac_level,int ancestor) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = " select * from class_table where ac_level = '"+ac_level+"' and ac_ancestor = '"+ancestor+"' and isuse = 'y'";
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String s_level = "";

	        if (Integer.parseInt(rs.getString("ac_level")) > 0) {
			    s_level = "";
	            for (int i=1; i<Integer.parseInt(rs.getString("ac_level")); i++) s_level = "&nbsp;&nbsp;&nbsp;" + s_level;
			}

			comboList += "<option value='"+rs.getString("code")+"'>"+s_level+rs.getString("ac_name")+"</option>";

			viewComboByCode(Integer.parseInt(rs.getString("ac_level"))+1, Integer.parseInt(rs.getString("ac_id")));
			
		}

		stmt.close(); 
	    rs.close();

		return comboList;
	} // viewComboByCode()


	/******************************************************
	 * ���°��� ��⿡�� �μ��� �����׸� ��� �� hierachy�� �μ���
	 * ���Ѵ�. GeunTaeDAO �� getDivisionalStatusByYear �޼��忡��
	 * ȣ���.
	 ******************************************************/
	public ArrayList getDivNameByTreeForGeuntaeInfo(int ac_level,int ancestor) throws Exception{
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String sql = " select * from class_table where ac_level = '"+ac_level+"' and ac_ancestor = '"+ancestor+"' and isuse = 'y'";
		rs = stmt.executeQuery(sql);

		while(rs.next()) {
			table = new com.anbtech.es.geuntae.entity.GeunTaeInfoTable();
			table.setAc_name(rs.getString("code"));			//�����ڵ�
			table.setDepartment(rs.getString("ac_name"));	//�μ���
			div_list.add(table);

			getDivNameByTreeForGeuntaeInfo(Integer.parseInt(rs.getString("ac_level"))+1, Integer.parseInt(rs.getString("ac_id")));
			
		}

		stmt.close();
	    rs.close();

		return div_list;
	} //getDivNameByTreeForGeuntaeInfo()
} //makeClassTree