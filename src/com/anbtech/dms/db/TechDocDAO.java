package com.anbtech.dms.db;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.business.*;

import java.sql.*;
import java.util.*;

public class TechDocDAO{
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public TechDocDAO(Connection con){
		this.con = con;
	}


	/*****************************************************************************
	 * 조건에 맞는 techdoc_data 테이블 리스트을 가져온다.
	 *****************************************************************************/
	public ArrayList getTechDocData_List(String login_id,String tablename,String mode,String searchword,String searchscope,String category,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.dms.entity.TechDocTable table = new com.anbtech.dms.entity.TechDocTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 10;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이

		int current_page_num =Integer.parseInt(page);

		com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);
		com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);

		String where = "";
		if(searchscope.equals("detail")) where = masterBO.getWhere(mode,category,searchword);
		else if(mode.equals("mylist")) where = masterBO.getWhere(login_id);		
		else where = masterBO.getWhere(mode,searchword, searchscope, category);


		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		int total = masterDAO.getTotalCount("techdoc_data", where);	// 전체 레코드 갯수
		int recNum = total;
		
		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT t_id,ver_code,ancestor,subject,doc_no,writer_s,";
		query += "register_s,register_day,stat,hit,category_id";
		query += " FROM techdoc_data " + where + " ORDER BY register_day DESC";
//System.out.println(query);

		
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String t_id = "<input type=checkbox name=checkbox value="+tablename+"|"+rs.getString("t_id")+">";
			String ver_code = rs.getString("ver_code");
			String ancestor = rs.getString("ancestor");
			String subject = rs.getString("subject");
			String doc_no = rs.getString("doc_no");
			String writer = rs.getString("writer_s");
			String register = rs.getString("register_s");
			String register_day = rs.getString("register_day").substring(0,10);
			String stat = masterBO.getStatus(rs.getString("stat"));
			String hit = rs.getString("hit");
			String category_id = rs.getString("category_id");

			//데이터 아이뒤를 가지고 마스터 테이블의 관리번호를 가져온다.
			String m_id = masterDAO.getMid(ancestor);

			// 제목의 표시 길이 제한
			if (subject.length() > l_maxsubjectlen+1) subject = subject.substring(0, l_maxsubjectlen) + "...";
			
			// 문서제목에 링크 설정
			String subject_link = "";
			if(mode.equals("list")) subject_link = "<A HREF='AnBDMS?tablename="+tablename+"&mode=view";
			else if(mode.equals("processing")) subject_link = "<A HREF='AnBDMS?tablename="+tablename+"&mode=view_a";
			else if(mode.equals("mylist")) subject_link = "<A HREF='AnBDMS?tablename="+tablename+"&mode=view_m";

			subject_link += "&page="+page+"&searchword="+searchword;
			subject_link += "&searchscope="+searchscope+"&category="+category_id;
			subject_link += "&no="+m_id+"&d_id="+ancestor+"&ver="+ver_code+"&org_category="+category+"'>";
			subject = subject_link + subject + "</a>";


			table = new com.anbtech.dms.entity.TechDocTable();
			table.setTid(t_id);
			table.setVerCode(ver_code);
			table.setAncestor(ancestor);
			table.setSubject(subject);
			table.setDocNo(doc_no);
			table.setWriter(writer);
			table.setRegister(register);
			table.setRegisterDay(register_day);
			table.setStat(stat);
			table.setHit(hit);
	
			table_list.add(table);

			recNum--;

		}
		stmt.close();
		rs.close();

		return table_list;
	}



	/*******************************************************************
	 * 검색된 데이터번호와 버젼에 맞는 레코드 정보를 가져온다.
	 * ancestor : 데이터 번호,동일버젼에 대해서는 모두 동일 데이터번호임
	 * version  : 버젼 코드
	 *******************************************************************/
	public TechDocTable getTechDocData(String tablename,String data_no,String ver_code) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		TechDocTable table = new TechDocTable();

		String query = "SELECT * FROM " + tablename + " WHERE ancestor ='"+data_no+"' and ver_code = '"+ver_code+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		table.setTid(rs.getString("t_id"));
		table.setVerCode(rs.getString("ver_code"));
		table.setAncestor(rs.getString("ancestor"));
		table.setVerNo(rs.getString("ver_no"));
		table.setSubject(rs.getString("subject"));
		table.setDocNo(rs.getString("doc_no"));
		table.setFileName(rs.getString("fname"));
		table.setFileSize(rs.getString("fsize"));
		table.setFileType(rs.getString("ftype"));
		table.setModifyHistory(rs.getString("modify_history"));
		table.setPreview(rs.getString("preview"));
		table.setWhyRevision(rs.getString("why_revision"));
		table.setMemo(rs.getString("memo"));
		table.setSavePeriod(rs.getString("save_period"));
		table.setSecurityLevel(rs.getString("security_level"));
		table.setWrittenLang(rs.getString("written_lang"));
		table.setDocType(rs.getString("doc_type"));
		table.setSaveUrl(rs.getString("save_url"));
		table.setWhereFrom(rs.getString("where_from"));
		table.setWriter(rs.getString("writer"));
		table.setWriterS(rs.getString("writer_s"));
		table.setWrittenDay(rs.getString("written_day"));
		table.setRegister(rs.getString("register"));
		table.setRegisterS(rs.getString("register_s"));
		table.setRegisterDay(rs.getString("register_day"));
		table.setReference(rs.getString("reference"));
		table.setEcoNo(rs.getString("eco_no"));
		table.setCopyNum(rs.getString("copy_num"));
		table.setCategoryId(rs.getString("category_id"));
		table.setAid(rs.getString("aid"));

		stmt.close();
		rs.close();
		return table;
	}

	/*******************************************************************
	 * 신규 문서의 내용을 DB에 저장한다.
	 *******************************************************************/
	public boolean saveData(String tablename,String ver_code,String data_id,String preview,String save_period,String security_level,String written_lang,String doc_type,String save_url,String where_from,String writer,String register,String register_day,String reference,String eco_no,String copy_num,String subject,String doc_no,String category_id,String stat,String hit) throws Exception{

		PreparedStatement pstmt = null;

		AccessControlDAO ac = new AccessControlDAO(con);
		String writer_s = writer + "/" + ac.getUserName(writer);
		String register_s = register + "/" + ac.getUserName(register);

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String register_day_s = vans.format(now);

		String query = "INSERT INTO techdoc_data" + "(ver_code,ancestor,preview,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,register_day,reference,eco_no,copy_num,subject,doc_no,category_id,stat,hit,writer_s,register_s,register_day_s) VALUES " + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,ver_code);
		pstmt.setString(2,data_id);
		pstmt.setString(3,preview);
		pstmt.setString(4,save_period);
		pstmt.setString(5,security_level);
		pstmt.setString(6,written_lang);
		pstmt.setString(7,doc_type);
		pstmt.setString(8,save_url);
		pstmt.setString(9,where_from);
		pstmt.setString(10,writer);
		pstmt.setString(11,register);
		pstmt.setString(12,register_day);
		pstmt.setString(13,reference);
		pstmt.setString(14,eco_no);
		pstmt.setString(15,copy_num);
		pstmt.setString(16,subject);
		pstmt.setString(17,doc_no);
		pstmt.setString(18,category_id);
		pstmt.setString(19,stat);
		pstmt.setString(20,hit);
		pstmt.setString(21,writer_s);
		pstmt.setString(22,register_s);
		pstmt.setString(23,register_day_s);
		
		pstmt.executeUpdate();
		pstmt.close();
		return true;
	}//saveData()


	/*******************************************************************
	 * 리비젼되는 문서의 내용을 DB에 저장한다.
	 *******************************************************************/
	public boolean saveData(String tablename,String ver_code,String data_id,String preview,String why_revision,String save_period,String security_level,String written_lang,String doc_type,String save_url,String where_from,String writer,String register,String register_day,String reference,String eco_no,String copy_num,String subject,String doc_no,String category_id,String stat,String hit) throws Exception{

		PreparedStatement pstmt = null;

		AccessControlDAO ac = new AccessControlDAO(con);
		String writer_s = writer + "/" + ac.getUserName(writer);
		String register_s = register + "/" + ac.getUserName(register);

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String register_day_s = vans.format(now);

		String query = "INSERT INTO techdoc_data" + "(ver_code,ancestor,preview,why_revision,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,register_day,reference,eco_no,copy_num,subject,doc_no,category_id,stat,hit,writer_s,register_s,register_day_s) VALUES " + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,ver_code);
		pstmt.setString(2,data_id);
		pstmt.setString(3,preview);
		pstmt.setString(4,why_revision);
		pstmt.setString(5,save_period);
		pstmt.setString(6,security_level);
		pstmt.setString(7,written_lang);
		pstmt.setString(8,doc_type);
		pstmt.setString(9,save_url);
		pstmt.setString(10,where_from);
		pstmt.setString(11,writer);
		pstmt.setString(12,register);
		pstmt.setString(13,register_day);
		pstmt.setString(14,reference);
		pstmt.setString(15,eco_no);
		pstmt.setString(16,copy_num);
		pstmt.setString(17,subject);
		pstmt.setString(18,doc_no);
		pstmt.setString(19,category_id);
		pstmt.setString(20,stat);
		pstmt.setString(21,hit);
		pstmt.setString(22,writer_s);
		pstmt.setString(23,register_s);
		pstmt.setString(24,register_day_s);
		
		pstmt.executeUpdate();
		pstmt.close();
		return true;
	}//saveData()


	/**************************
	 * 문서의 내용을 수정한다.
	 **************************/
	public void updateData(String tablename,String t_id,String preview,String save_period,String security_level,String written_lang,String doc_type,String save_url,String where_from,String writer,String register,String w_time,String reference,String eco_no,String copy_num,String modify_history) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		query = "UPDATE " + tablename + " SET preview=?,save_period=?,security_level=?,written_lang=?,doc_type=?,save_url=?,where_from=?,writer=?,register=?,register_day=?,reference=?,eco_no=?,copy_num=?,modify_history=? WHERE t_id='"+t_id+"'";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,preview);
		pstmt.setString(2,save_period);
		pstmt.setString(3,security_level);
		pstmt.setString(4,written_lang);
		pstmt.setString(5,doc_type);
		pstmt.setString(6,save_url);
		pstmt.setString(7,where_from);
		pstmt.setString(8,writer);
		pstmt.setString(9,register);
		pstmt.setString(10,w_time);
		pstmt.setString(11,reference);
		pstmt.setString(12,eco_no);
		pstmt.setString(13,copy_num);
		pstmt.setString(14,modify_history);
		pstmt.executeUpdate();
		pstmt.close();

	}//updateData()


	/*****************************************************************
	 * data_id와 ver_code에 해당하는 데이터의 첨부파일 리스트 가져오기
	 *****************************************************************/
	public ArrayList getFile_list(String tablename, String d_id, String version) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;
		ArrayList file_list = new ArrayList();

		String query = "SELECT fname,fsize,ftype FROM "+tablename+" WHERE ancestor = '"+d_id+"' and ver_code = '"+version+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()){ 

			Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
			Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();
			Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();

			while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()){
				TechDocTable file = new TechDocTable();
				file.setFileName((String)filename_iter.next());
				file.setFileType((String)filetype_iter.next());
				file.setFileSize((String)filesize_iter.next());
				file_list.add(file);
			}
		}
		stmt.close();
		rs.close();
		return file_list;
	}//getFile_list()


	/*****************************************************************
	 * data_id와 ver_code에 해당하는 데이터의 참조자료 리스트 가져오기
	 *****************************************************************/
	public ArrayList getReference_list(String tablename, String d_id, String version) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		ArrayList ref_list = new ArrayList();
		String ref_src = "";

		String query = "SELECT reference FROM "+tablename+" WHERE ancestor = '"+d_id+"' and ver_code = '"+version+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()) ref_src = rs.getString("reference");

		StringTokenizer str = new StringTokenizer(ref_src, "^");
		int ref_count = str.countTokens();
		String ref[] = new String[ref_count];
		String ref_item[][] = new String[ref_count][4];

		for(int i=0; i<ref_count; i++){ 
			ref[i] = str.nextToken();

			StringTokenizer str2 = new StringTokenizer(ref[i],"|");
			int item_count = str2.countTokens();

			if(item_count < 1 ) break;

			for(int j=0; j<item_count; j++){ 
				ref_item[i][j] = str2.nextToken();
			}

			if(ref_item[i][1]==null) ref_item[i][1] = "";
			if(ref_item[i][2]==null) ref_item[i][2] = "";
			if(ref_item[i][3]==null) ref_item[i][3] = "";

			TechDocTable reference = new TechDocTable();
			reference.setRefSubject(ref_item[i][0]);
			reference.setRefWriter(ref_item[i][1]);
			reference.setRefPressName(ref_item[i][2]);
			reference.setRefPressYear(ref_item[i][3]);

			ref_list.add(reference);

		}

		stmt.close();
		rs.close();
		return ref_list;
	}//getReference_list()


	/*****************************************************************
	 * 데이터 관리번호에 해당하는 첨부파일 리스트 가져오기
	 *****************************************************************/
	public ArrayList getFile_list(String tablename, String t_id) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;
		ArrayList file_list = new ArrayList();

		String query = "SELECT fname,fsize,ftype FROM "+tablename+" WHERE t_id = '"+t_id+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()){ 

			Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
			Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();
			Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();

			while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()){
				TechDocTable file = new TechDocTable();
				file.setFileName((String)filename_iter.next());
				file.setFileType((String)filetype_iter.next());
				file.setFileSize((String)filesize_iter.next());
				file_list.add(file);
			}
		}
		stmt.close();
		rs.close();
		return file_list;
	} //getFile_list()


	/********************************************************************
	 * data_id와 ver_code에 해당하는 데이터의 관리번호를 가져와 리턴한다.
	 ********************************************************************/	
	public String getId(String tablename, String data_id, String ver_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT t_id FROM "+tablename+" WHERE ancestor = '"+data_id+"' and ver_code = '"+ver_code+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String t_id = rs.getString("t_id");
		stmt.close();
		rs.close();
		return t_id;
	}

	/*************************************
	 * 첨부파일 정보를 DB에 저장한다.
	 *************************************/
	public void updTable(String tablename, String set, String where) throws Exception{
		Statement stmt = null;
		String query = "UPDATE " + tablename + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*****************************************************************
	 * data_id 에 해당하는 문서의 버젼 리스트를 가져와 리턴한다.
	 *****************************************************************/
	public ArrayList getVerList(String mode, String tablename, String data_id) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		ArrayList ver_list = new ArrayList();

		String query = "";
		if(mode.equals("view_m") || mode.equals("report") || mode.equals("print") || mode.equals("view_a")) query = "SELECT ver_code FROM "+tablename+" WHERE ancestor = '"+data_id+"'";
		else query = "SELECT ver_code FROM "+tablename+" WHERE ancestor = '"+data_id+"' and stat = '5'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){ 
			TechDocTable version = new TechDocTable();
			version.setVerCode(rs.getString("ver_code"));
			ver_list.add(version);
		}
		stmt.close();
		rs.close();
		return ver_list;
	} //getVerList()

}		