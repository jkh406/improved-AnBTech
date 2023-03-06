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
	 * 생성자
	 *******************************************************************/
	public ShareBdDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	 * 업무공유의 내용을 DB에 저장한다.
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
	 * 업무공유의 수정 내용을 저장한다.
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
	* 업무공유 내용 삭제
	**************************************************/
	public void deleteDoc(String no,String tablename) throws Exception {
		stmt = con.createStatement();
		String query = "DELETE FROM "+tablename+" WHERE no='"+no+"'";
		stmt.executeUpdate(query);
		stmt.close();
	}

	/**************************************************	
	* 항목 조회 수 counting 하기
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
	 * 첨부파일 정보를 DB에 저장한다.
	 *************************************/
	public void updTable(String tablename, String set, String where) throws Exception{

		String query = "UPDATE " + tablename + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/********************************************************************
	 * doc_no와 ver에 해당하는 데이터의 관리번호를 가져와 리턴한다.
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
	 * 해당 관리번호의 wid를 가져와 리턴한다.
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
	 *  선택된 항목의 정보 가져오기 
	 *  input : tablename(table name),no(table 관리번호)   output: table 모든정보
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
	 * 정보 가져오기 (list)
	 ********************************************************************/	
	public ArrayList getShareBdList(String tablename,String mode,String searchword,String searchscope,String category,String page) throws Exception {
		
	Statement st = null;
	ResultSet rs = null;

		com.anbtech.share.business.ShareBdBO shbBO = new com.anbtech.share.business.ShareBdBO(con);
		com.anbtech.share.entity.ShareBdTable sbdTable;

		ArrayList arrylist = new ArrayList();
		st = con.createStatement();

		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이

		String orderby = " order by no desc";

		int current_page_num =Integer.parseInt(page);
		String where = shbBO.getWhere(tablename,mode,searchword,searchscope,category);
		
		int total = getTotalCount(tablename, where);	// 전체 레코드 갯수
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
	 * 레코드의 전체 개수를 구한다.
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
	* 첨부파일 리스트 가져오기
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
	*	관리자 권한이 있는지 확인							  *
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
	*	카테고리 SELECT BOX LIST 가져오기
	*	- 목록보기의 SELECT comboBOX에는 '전체'항목이 있고,
	*     등록 및 수정폼에서 SELECT comboBOX는 '전체'항목은 제외
	********************************************************/	
	public String getCategoryItem(String tablename, String mode) throws Exception {
		
	Statement stmt = con.createStatement();
	String query = "";
	ResultSet rs = null;
	String category_items = "";
	int inc = 0;			// SELECT 목록의 '전체'항목 관련 변수

	query = "SELECT category_items FROM board_env WHERE tablename ='"+tablename+"'";
	rs = stmt.executeQuery(query);
	if(rs.next()){	category_items = rs.getString("category_items");}
	String selectBox = "<SELECT name = 'category'>";	

	java.util.StringTokenizer st = new java.util.StringTokenizer(category_items,"|");
	while(st.hasMoreTokens()){
		
		String token = st.nextToken();
		if((mode.equals("write") || mode.equals("modify")) && inc==0) { token = st.nextToken(); }
		String value = token;
		
		if(token.equals("admin")) { token="전체"; value=""; }
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