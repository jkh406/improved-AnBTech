package com.anbtech.share.db;

import com.anbtech.share.entity.*;
import com.anbtech.share.business.*;
import java.sql.*;
import java.util.*;

public class ShareBdDAO{
	private Connection con;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public ShareBdDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	 * ���������� ������ DB�� �����Ѵ�.
	 *******************************************************************/
	public boolean saveData(String tablename,String subject,String ver,String wid,String wname,String doc_no,String ac_name,String category,String content,String cnt) throws Exception{


		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String wdate = vans.format(now);

		String query = "INSERT INTO " + tablename;
		query = query + "(subject, ver, wid, wname, doc_no, ac_name, category, content, cnt , wdate,fname,fsize,ftype,fpath,mid,mname,mdate)";
		query = query + " VALUES ";
		query = query + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,subject);
		pstmt.setString(2,ver);
		pstmt.setString(3,wid);
		pstmt.setString(4,wname);
		pstmt.setString(5,doc_no);
		pstmt.setString(6,ac_name);
		pstmt.setString(7,category);
		pstmt.setString(8,content);
		pstmt.setString(9,cnt);
		pstmt.setString(10,wdate);
		pstmt.setString(11,"");
		pstmt.setString(12,"");
		pstmt.setString(13,"");
		pstmt.setString(14,"");
		pstmt.setString(15,"");
		pstmt.setString(16,"");
		pstmt.setString(17,"");
		
		pstmt.executeUpdate();
		pstmt.close();
		return true;
	}//saveData()


	/*******************************************************************
	 * ���������� ���� ������ �����Ѵ�.
	 *******************************************************************/
	public void updTable(String no,String tablename,String subject,String ver,String mid,String mname,String doc_no,String ac_name,String category,String content) throws Exception{
		
		String query= "";
	
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		stmt = con.createStatement();
		query = "UPDATE " + tablename + " SET subject=?, ver=?, mid=?, mname=?, doc_no=?, ac_name=?, category=?, content=?, mdate=? WHERE  no=?";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,subject);
		pstmt.setString(2,ver);
		pstmt.setString(3,mid);
		pstmt.setString(4,mname);
		pstmt.setString(5,doc_no);
		pstmt.setString(6,ac_name);
		pstmt.setString(7,category);
		pstmt.setString(8,content);
		pstmt.setString(9,w_time);
		pstmt.setString(10,no);
		
		pstmt.executeUpdate();
		pstmt.close();
		
	}

	/**************************************************	
	* �������� ���� ����
	**************************************************/
	public void deleteDoc(String no,String tablename) throws Exception {
		stmt = con.createStatement();
		String query = "DELETE FROM "+tablename+" WHERE no='"+no+"'";
		stmt.executeUpdate(query);
		stmt.close();
	}

	/**************************************************	
	* �׸� ��ȸ �� counting �ϱ�
	**************************************************/
	public void countingCheck(String tablename, String no) throws Exception{
		stmt = con.createStatement();
		String query = "SELECT cnt FROM "+tablename+" WHERE no='"+no+"'";
		rs = stmt.executeQuery(query);
		int cnt = 0;

		if(rs.next())	cnt = Integer.parseInt(rs.getString("cnt"));
		
		cnt = cnt+1;
		query = "UPDATE "+tablename+" SET cnt='"+cnt+"' WHERE no='"+no+"'";
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*************************************
	 * ÷������ ������ DB�� �����Ѵ�.
	 *************************************/
	public void updTable(String tablename, String set, String where) throws Exception{

		String query = "UPDATE " + tablename + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/********************************************************************
	 * doc_no�� ver�� �ش��ϴ� �������� ������ȣ�� ������ �����Ѵ�.
	 ********************************************************************/	
	public String getNo(String tablename, String doc_no, String ver) throws Exception{

		String query = "SELECT no FROM "+tablename+" WHERE doc_no = '"+doc_no+"' and ver = '"+ver+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String no = rs.getString("no");
		stmt.close();
		rs.close();
		return no;
	}

	/********************************************************************
	 * �ش� ������ȣ�� wid�� ������ �����Ѵ�.
	 ********************************************************************/	
	public String getWid(String tablename, String no) throws Exception{

		String query = "SELECT wid FROM "+tablename+" WHERE no = '"+no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String wid = rs.getString("wid");
		stmt.close();
		rs.close();
		return wid;
	}
		
	/*************************************************************************8
	 *  ���õ� �׸��� ���� �������� 
	 *  input : tablename(table name),no(table ������ȣ)   output: table �������
	 *************************************************************************/	
	 public ShareBdTable getShareInfo(String tablename,String no) throws Exception {
		
		Statement st = null;
		ResultSet rs = null;

		com.anbtech.share.entity.ShareBdTable sbdTable = new com.anbtech.share.entity.ShareBdTable();
		ArrayList arrylist = new ArrayList();
		String query = "SELECT * FROM "+tablename+" WHERE no = '"+no+"'";

		st = con.createStatement();
		rs = st.executeQuery(query);

		if(rs.next()){
			
			sbdTable.setNo(rs.getInt("no"));
			sbdTable.setSubject(rs.getString("subject"));
			sbdTable.setVer(rs.getString("ver"));
			sbdTable.setWid(rs.getString("wid"));
			sbdTable.setWname(rs.getString("wname"));
			sbdTable.setWdate(rs.getString("wdate"));
			sbdTable.setDocNo(rs.getString("doc_no"));
			sbdTable.setAcName(rs.getString("ac_name"));
			sbdTable.setCategory(rs.getString("category"));
			sbdTable.setContent(rs.getString("content"));
			sbdTable.setCnt(rs.getInt("cnt"));
			sbdTable.setMid(rs.getString("mid"));
			sbdTable.setMname(rs.getString("mname"));
			sbdTable.setMdate(rs.getString("mdate"));
			
			
			Iterator file_iter = getFile_list(tablename, no).iterator();
			int j = 1;
			String filelink = "";
			while(file_iter.hasNext()){
				ShareBdTable file = (ShareBdTable)file_iter.next();
				filelink += "<a href='ShareBdServlet?tablename="+tablename+"&mode=download&no="+no+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0> "+file.getFname()+"</a>("+file.getFsize()+" bytes) &nbsp;<br>";
				j++;
			}
			sbdTable.setFlink(filelink);
		}
		rs.close();
		st.close();

		return sbdTable;
	 }



	/********************************************************************
	 * ���� �������� (list)
	 ********************************************************************/	
	public ArrayList getShareBdList(String tablename,String mode,String searchword,String searchscope,String category,String page) throws Exception {
		
	Statement st = null;
	ResultSet rs = null;

		com.anbtech.share.business.ShareBdBO shbBO = new com.anbtech.share.business.ShareBdBO(con);
		com.anbtech.share.entity.ShareBdTable sbdTable;

		ArrayList arrylist = new ArrayList();
		st = con.createStatement();

		int l_maxlist = 15;			// ������������ ����� ���ڵ� ��
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 30;	// ������ �ִ� ǥ�ñ���

		String orderby = " order by no desc";

		int current_page_num =Integer.parseInt(page);
		String where = shbBO.getWhere(tablename,mode,searchword,searchscope,category);
		
		int total = getTotalCount(tablename, where);	// ��ü ���ڵ� ����
		total = 1;
		int recNum = total;

		String query = "SELECT * FROM "+tablename + where + orderby;
		rs=st.executeQuery(query);
		
		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}
		
		
		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			sbdTable = new com.anbtech.share.entity.ShareBdTable();
			
			sbdTable.setNo(rs.getInt("no"));
			sbdTable.setSubject(rs.getString("subject"));
			sbdTable.setVer(rs.getString("ver"));
			sbdTable.setWid(rs.getString("wid"));
			sbdTable.setWname(rs.getString("wname"));
			sbdTable.setWdate(rs.getString("wdate"));
			sbdTable.setDocNo(rs.getString("doc_no"));
			sbdTable.setAcName(rs.getString("ac_name"));
			sbdTable.setCategory(rs.getString("category"));
			//sbdTable.setContent(rs.getString("content"));
			sbdTable.setCnt(rs.getInt("cnt"));
			sbdTable.setMid(rs.getString("mid"));
			sbdTable.setMname(rs.getString("mname"));
			sbdTable.setMdate(rs.getString("mdate"));
						
			Iterator file_iter = getFile_list(tablename, (""+rs.getInt("no"))).iterator();
			
			int j = 1;
			String filelink = "&nbsp;";
			while(file_iter.hasNext()){

				ShareBdTable file = (ShareBdTable)file_iter.next();
				filelink += "<a href='ShareBdServlet?tablename="+tablename+"&mode=download&no="+(""+rs.getInt("no"))+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0></a>";
				j++;				
			}

			String subject_link = "";
			subject_link = "<A HREF='ShareBdServlet?tablename="+tablename+"&mode=view";
			subject_link += "&page="+page+"&searchword="+searchword;
//			subject_link += "&searchscope="+searchscope+"&category="+rs.getString("category")+"&no="+rs.getInt("no")+"' ";
			subject_link += "&searchscope="+searchscope+"&category="+category+"&no="+rs.getInt("no")+"' ";
			subject_link += "onMouseOver=\"window.status='';return true;\" ";
			subject_link += "onMouseOut=\"window.status='';return true;\" >";
			sbdTable.setSubjectLink(subject_link);
			

			//System.out.println(filelink);
			sbdTable.setFlink(filelink);

			arrylist.add(sbdTable);
		}

		rs.close();
		st.close();

		return arrylist;
	}

	/*******************************************************************
	 * ���ڵ��� ��ü ������ ���Ѵ�.
	 *******************************************************************/
	public int getTotalCount(String tablename, String where) throws Exception{
	Statement stmt = null;
	ResultSet rs = null;
	int total_count = 0;

	String query = "SELECT COUNT(*) FROM " + tablename + where;
	stmt = con.createStatement();
	rs = stmt.executeQuery(query);
	rs.next();
	
	total_count = Integer.parseInt(rs.getString(1));
	stmt.close();
	rs.close();
	return total_count;
	}
		
	/*****************************************************************
	* ÷������ ����Ʈ ��������
	*****************************************************************/
	public ArrayList getFile_list(String tablename,String no) throws Exception{

	Statement stmt = null;
	ResultSet rs = null;
	int total = 0;
	ArrayList file_list = new ArrayList();

	String query = "SELECT fname,ftype,fsize FROM "+tablename+" WHERE no = '"+no+"'";
	stmt = con.createStatement();
	rs = stmt.executeQuery(query);
	if(rs.next()){ 

		Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
		Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();
		Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();
		
		while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()){
			ShareBdTable file = new ShareBdTable();
			file.setFname((String)filename_iter.next());
			file.setFtype((String)filetype_iter.next());
			file.setFsize((String)filesize_iter.next());
			file_list.add(file);
		}
	}
		stmt.close();
		rs.close();
		return file_list;
	}//getFile_list()
	

	/******************************************************
	*	������ ������ �ִ��� Ȯ��							  *
	******************************************************/
	public String adminList(String tablename) throws Exception {
		stmt = con.createStatement();
		String query = "";
		String code_s = ""; 
		String adminlist = "";
			
		if (tablename.equals("com_rule")){
			code_s = "SH01";
		} else if (tablename.equals("form_bank")){
			code_s = "SH02";
		} else if (tablename.equals("manual_bank"))	{
			code_s = "SH03";
		}

		query = "SELECT owner FROM prg_privilege WHERE code_s='"+code_s+"'";
		rs = stmt.executeQuery(query);

		if(rs.next()){
		adminlist = rs.getString("owner");
		}

		rs.close();
		stmt.close();

		return adminlist;
	}

	
	/********************************************************
	*	ī�װ� SELECT BOX LIST ��������
	*	- ��Ϻ����� SELECT comboBOX���� '��ü'�׸��� �ְ�,
	*     ��� �� ���������� SELECT comboBOX�� '��ü'�׸��� ����
	********************************************************/	
	public String getCategoryItem(String tablename, String mode) throws Exception {
		
	Statement stmt = con.createStatement();
	String query = "";
	ResultSet rs = null;
	String category_items = "";
	int inc = 0;			// SELECT ����� '��ü'�׸� ���� ����

	query = "SELECT category_items FROM board_env WHERE tablename ='"+tablename+"'";
	rs = stmt.executeQuery(query);
	if(rs.next()){	category_items = rs.getString("category_items");}
	String selectBox = "<SELECT name = 'category'>";	

	java.util.StringTokenizer st = new java.util.StringTokenizer(category_items,"|");
	while(st.hasMoreTokens()){
		
		String token = st.nextToken();
		if((mode.equals("write") || mode.equals("modify")) && inc==0) { token = st.nextToken(); }
		String value = token;
		
		if(token.equals("admin")) { token="��ü"; value=""; }
		selectBox +="<OPTION value='"+token+"'>"+token+"</OPTION>";
		inc++;
	}
	
	selectBox += "</SELECT>";
	rs.close();
	stmt.close();
	//System.out.println("selectBox: "+selectBox);
	return selectBox;
	}

}		