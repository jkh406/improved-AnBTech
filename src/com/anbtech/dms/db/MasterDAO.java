package com.anbtech.dms.db;

import com.anbtech.dms.entity.*;
import com.anbtech.admin.entity.*;
import com.anbtech.dms.business.*;
import java.sql.*;
import java.util.*;

public class MasterDAO{
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public MasterDAO(Connection con){
		this.con = con;
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


	/*******************************************************************
	 * 검색 조건에 맞는 master_data 목록을 가져온다.
	 *******************************************************************/
	public ArrayList getMasterData_List(String login_id,String tablename,String mode,String searchword,String searchscope,String category,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.dms.entity.MasterTable table = new com.anbtech.dms.entity.MasterTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 10;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 35;	// 제목의 최대 표시길이

		int current_page_num =Integer.parseInt(page);

		com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
		String where = "";
		if(searchscope.equals("detail")) where = masterBO.getWhere(mode,category,searchword);
		else where = masterBO.getWhere(mode,searchword, searchscope, category);

		if(mode.equals("mylist")) where = masterBO.getWhere(login_id);		


		int total = getTotalCount("master_data", where);	// 전체 레코드 갯수
		int recNum = total;

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT m_id,doc_no,category_id,data_id,subject,writer_s,";
		query += "register_s,register_day,hit,last_version,stat";
		query += " FROM master_data " + where + " ORDER BY m_id DESC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String m_id = rs.getString("m_id");
			String doc_no = rs.getString("doc_no");
			String category_id = rs.getString("category_id");
			String data_id = rs.getString("data_id");
			String subject = rs.getString("subject");
			String writer = rs.getString("writer_s");
			String register = rs.getString("register_s");
			String register_day = rs.getString("register_day").substring(0,10);
			String hit = rs.getString("hit");
			String last_version = rs.getString("last_version");
			String stat = masterBO.getStatus(rs.getString("stat"));

			String curr_tablename = getTableName(category_id);

			// 제목의 표시 길이 제한
			if (subject.length() > l_maxsubjectlen+1) subject = subject.substring(0, l_maxsubjectlen) + "...";
			
			// 문서제목에 링크 설정
			String subject_link = "";

			if(mode.equals("list")) subject_link = "<A HREF='AnBDMS?tablename="+curr_tablename+"&mode=view_t";
			else if(mode.equals("processing") || mode.equals("mylist")) subject_link = "<A HREF='AnBDMS?tablename="+curr_tablename+"&mode=view_a";

			subject_link += "&page="+page+"&searchword="+searchword;
			subject_link += "&searchscope="+searchscope+"&category="+category_id;
			subject_link += "&no="+m_id+"&d_id="+data_id+"&ver="+last_version+"&org_category="+category+"'>";
			subject = subject_link + subject + "</a>";

			table = new com.anbtech.dms.entity.MasterTable();
			table.setMid(Integer.parseInt(m_id));
			table.setDocNo(doc_no);
			table.setCategoryId(category_id);
			table.setDataId(data_id);
			table.setSubject(subject);
			table.setWriter(writer);
			table.setRegister(register);
			table.setRegisterDay(register_day);
			table.setHit(hit);
			table.setLastVersion(last_version);
			table.setStat(stat);

			table_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return table_list;
	}

	/*******************************************************************
	 * no에 해당하는 master_data 테이블 내용을 가져온다.
	 *******************************************************************/
	public MasterTable getMasterData(String tablename, String no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.dms.entity.MasterTable table = new com.anbtech.dms.entity.MasterTable();

		String query = "SELECT * FROM "+tablename+" where m_id = '"+no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		table.setMid(Integer.parseInt(rs.getString("m_id")));
		table.setDocNo(rs.getString("doc_no"));
		table.setCategoryId(rs.getString("category_id"));
		table.setDataId(rs.getString("data_id"));
		table.setSubject(rs.getString("subject"));
		table.setWriter(rs.getString("writer"));
		table.setWrittenDay(rs.getString("written_day"));
		table.setRegister(rs.getString("register"));
		table.setRegisterDay(rs.getString("register_day"));
		table.setSearchKeyword(rs.getString("search_keyword"));
		table.setHit(rs.getString("hit"));
		table.setLastVersion(rs.getString("last_version"));
		table.setStat(rs.getString("stat"));

		table.setModelCode(rs.getString("model_code"));
		table.setPjtCode(rs.getString("pjt_code"));
		table.setNodeCode(rs.getString("node_code"));

		stmt.close();
		rs.close();
		return table;
	}


	/*****************************************************************
	 * 입력한 정보를 DB 에 저장한다.
	 *****************************************************************/
	public void saveData(String tablename, String doc_no, String catagory_id, String data_id, String subject, String writer, String register, String register_day, String search_keyword, String last_version,String model_code,String pjt_code,String node_code) throws Exception{

		PreparedStatement pstmt = null;

		AccessControlDAO ac = new AccessControlDAO(con);
		String writer_s = writer + "/" + ac.getUserName(writer);
		String register_s = register + "/" + ac.getUserName(register);

		String query = "INSERT INTO " + tablename + "(doc_no,category_id,data_id,subject,writer,writer_s,register,register_s,register_day,search_keyword,last_version,stat,hit,model_code,pjt_code,node_code) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,doc_no);
		pstmt.setString(2,catagory_id);
		pstmt.setString(3,data_id);
		pstmt.setString(4,subject);
		pstmt.setString(5,writer);
		pstmt.setString(6,writer_s);
		pstmt.setString(7,register);
		pstmt.setString(8,register_s);
		pstmt.setString(9,register_day);
		pstmt.setString(10,search_keyword);
		pstmt.setString(11,last_version);
		pstmt.setString(12,"1");
		pstmt.setString(13,"0");
		pstmt.setString(14,model_code);
		pstmt.setString(15,pjt_code);
		pstmt.setString(16,node_code);
	
		pstmt.executeUpdate();
		pstmt.close();
	} //saveData()//


	/**************************
	 * 문서의 내용을 수정한다.
	 **************************/
	public void updateData(String tablename,String doc_no,String category_id,String subject,String writer,String search_keyword,String no,String model_code,String pjt_code,String node_code) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		query = "UPDATE " + tablename + " SET doc_no=?,category_id=?,subject=?,writer=?,search_keyword=?,model_code=?,pjt_code=?,node_code=? WHERE m_id='"+no+"'";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,doc_no);
		pstmt.setString(2,category_id);
		pstmt.setString(3,subject);
		pstmt.setString(4,writer);
		pstmt.setString(5,search_keyword);
		pstmt.setString(6,model_code);
		pstmt.setString(7,pjt_code);
		pstmt.setString(8,node_code);
		pstmt.executeUpdate();
		pstmt.close();

	}//updateData()

	/*******************************************************************
	 * 리비젼 발생 시 no에 해당하는 내용을 최종 버젼정보로 업데이트한다.
	 *******************************************************************/
	public void revisionData(String tablename,String no,String writer,String register,String w_time,String search_keyword,String ver_code) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		AccessControlDAO ac = new AccessControlDAO(con);
		String writer_s = writer + "/" + ac.getUserName(writer);
		String register_s = register + "/" + ac.getUserName(register);

		query = "UPDATE " + tablename + " SET writer=?,register=?,register_day=?,search_keyword=?,last_version=?,hit='0',stat='5',writer_s=?,register_s=? WHERE m_id='"+no+"'";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,writer);
		pstmt.setString(2,register);
		pstmt.setString(3,w_time);
		pstmt.setString(4,search_keyword);
		pstmt.setString(5,ver_code);
		pstmt.setString(6,writer_s);
		pstmt.setString(7,register_s);
		pstmt.executeUpdate();
		pstmt.close();

	}//revisionData()


	/*******************************************************************
	 * 문서 삭제가 이루어진 경우에 이전 문서 정보로 업데이트한다.
	 * data_id 를 가지고
	 *******************************************************************/
	public void updateDataToPrev(String data_id,String writer,String writer_s,String register,String register_s,String register_day,String ver_code) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		query = "UPDATE master_data SET writer=?,writer_s=?,register=?,register_s=?,register_day=?,last_version=?,stat='3' WHERE data_id ='"+data_id+"'";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,writer);
		pstmt.setString(2,writer_s);
		pstmt.setString(3,register);
		pstmt.setString(4,register_s);
		pstmt.setString(5,register_day);
		pstmt.setString(6,ver_code);
		pstmt.executeUpdate();
		pstmt.close();

	}//updateDataToPrev()

	/*******************************************************************
	 * no에 해당하는 문서의 data_no를 가져와 리턴한다.
	 *******************************************************************/
	public String getDataNo(String tablename, String no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT data_id FROM "+tablename+" where m_id = '"+no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String data_no = rs.getString("data_id");
		stmt.close();
		rs.close();
		return data_no;
	}

	/*******************************************************************
	 * data_id (ancestor) 에 해당하는 문서의 관리번호(m_id)를 가져와 리턴한다.
	 *******************************************************************/
	public String getMid(String data_id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT m_id FROM master_data where data_id = '"+data_id+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String m_id = rs.getString("m_id");
		stmt.close();
		rs.close();
		return m_id;
	}

	/*******************************************************************
	 * no에 해당하는 문서의 최종 ver_code를 가져와 리턴한다.
	 *******************************************************************/
	public String getLastVersion(String tablename, String no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT last_version FROM "+tablename+" where m_id = '"+no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String last_version = rs.getString("last_version");
		stmt.close();
		rs.close();
		return last_version;
	}

	/*******************************************************************
	 * no에 해당하는 문서의 카테고리 코드를 가져와 리턴한다.
	 *******************************************************************/
	public String getCategoryId(String tablename, String no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT category_id FROM "+tablename+" where m_id = '"+no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String category_id = rs.getString("category_id");
		stmt.close();
		rs.close();
		return category_id;
	}

	/*******************************************************************
	 * data_id에 해당하는 category_id를 가져와 리턴한다.
	 *******************************************************************/
	public String getCategoryId(String data_id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT category_id FROM master_data where data_id = '"+data_id+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String category_id = rs.getString("category_id");
		stmt.close();
		rs.close();
		return category_id;
	} //getCategoryId()

	/*******************************************************************
	 * data_id에 해당하는 문서의 문서번호(doc_no)를 가져와 리턴한다.
	 *******************************************************************/
	public String getDocNo(String data_id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT doc_no FROM master_data where data_id = '"+data_id+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String doc_no = rs.getString("doc_no");
		stmt.close();
		rs.close();
		return doc_no;
	} //getDocNo()


	/*****************************************************************
	 * 해당 카테고리에 포함되는 문서번호를 계산하여 리턴한다.
	 *****************************************************************/
	public String getDocNo(String tablename,String category) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		//해당 카테고리의 대표문자를 가져온다.
		String query = "SELECT c_code FROM category_data where c_id = '"+category+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String c_code = rs.getString("c_code");

		//현재 년도 계산
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yy");
		String now_year	= vans.format(now);

		//대표문자 + 년도
		String doc_head = c_code + now_year;

		String doc_serial = "";
		String doc_no = "";
		
		query = "SELECT MAX(doc_no) FROM master_data where doc_no like '"+doc_head+"%'";
		rs = stmt.executeQuery(query);
		rs.next();
		doc_no = rs.getString(1);

		if(doc_no == null){
			doc_serial = "001";
		}else{
			doc_serial = doc_no.substring(4,7);
			com.anbtech.util.normalFormat nf = new com.anbtech.util.normalFormat("000");
			doc_serial = nf.toDigits(Integer.parseInt(doc_serial)+1);
		}

		doc_no = doc_head + "-" + doc_serial;

		stmt.close();
		rs.close();
		return doc_no;

	} //getDocNo



	/*******************************************************************
	 * no에 해당하는 문서의 카테고리 코드를 가져와 리턴한다.
	 *******************************************************************/
	public void updateHit(String tablename, String no) throws Exception{
		Statement stmt = null;

		String query = "update "+tablename+" set hit = hit+1 where m_id = '" + no + "'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	} //updateHit()


	/*******************************************************************
	 * master_data의 stat 필드값을 지정한 값으로 세팅한다. (관리번호를 가지고)
	 *******************************************************************/
	public void updateStat(String no,String stat) throws Exception{
		Statement stmt = null;
		String query= "";

		query = "UPDATE master_data SET stat='" + stat + "' WHERE m_id='"+no+"'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();

	}//updateStat()

	/*******************************************************************
	 * master_data의 stat 필드값을 지정한 값으로 세팅한다.(data_id를 가지고)
	 *******************************************************************/
	public void updateStat(String no,String stat,String data_id) throws Exception{
		Statement stmt = null;
		String query= "";

		query = "UPDATE master_data SET stat='" + stat + "' WHERE data_id='"+data_id+"'";
		
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();

	}//updateStat()

	/*******************************************************************
	 * master_data 외의 각 문서종류별 테이블의 stat 필드값을 지정한 값으로 세팅한다.
	 *******************************************************************/
	public void updateStat(String tablename,String data_id,String ver_code,String stat) throws Exception{
		Statement stmt = null;
		String query= "";

		query = "UPDATE " + tablename + " SET stat='" + stat + "' WHERE ancestor='"+data_id+"' and ver_code = '" + ver_code + "'";
		//System.out.println("tech u : " + query);
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();

	}//updateStat()

	/*******************************************************************
	 * 문서종류별 테이블의 pid 필드값을 지정한 값으로 세팅한다.
	 *******************************************************************/
	public void updateAid(String tablename,String data_id,String ver_code,String aid) throws Exception{
		Statement stmt = null;
		String query= "";

		query = "UPDATE " + tablename + " SET aid='" + aid + "' WHERE ancestor='"+data_id+"' and ver_code = '" + ver_code + "'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();

	}//updateStat()

	/*******************************************************************
	 * 문서종류별 테이블의 aid 필드값을 가져온다.
	 *******************************************************************/
	public String  getAid(String tablename,String data_id,String ver_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String aid = "";

		String query = "SELECT aid FROM " + tablename + " where ancestor = '" + data_id +"' and ver_code = '" + ver_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		
		while (rs.next()) aid = rs.getString("aid");

		stmt.close();
		rs.close();

		return aid;

	}//getAid()


	/*******************************************************************
	 * 카테고리 코드에 해당하는 테이블명을 가져와 리턴한다.
	 *******************************************************************/
	public String getTableName(String category_id) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT tablename FROM category_data where c_id = '"+category_id+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String tablename = rs.getString("tablename");
		stmt.close();
		rs.close();
		return tablename;
	} //getTableName()


	/*******************************************************************
	 * data_id와 ver_code를 통해 해당 문서의 형태가 파일인지를 파악
	 * 대출의뢰를 처리할 때 형태가 파일이 아닌 문서만 "대출중"이라는 
	 * 상태코드를 부여하기 위해 사용된다.
	 *******************************************************************/
	public boolean isFileType(String tablename,String data_id,String ver_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		boolean is_file = false;

		String query = "SELECT doc_type FROM " + tablename + " where ancestor = '"+data_id+"' and ver_code = '" + ver_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		if(rs.getString("doc_type").equals("FILE")) is_file =  true;
		stmt.close();
		rs.close();

		return is_file;
	} //isFileType()


	/*******************************************************************
	 * 현재 엑세스중인 문서의 바로 이전 버젼 코드를 가져온다.
	 * 엑세스중인 문서가 1.0 즉, 리비젼이 안 된 경우에는 null 을 던져준다.
	 *******************************************************************/
	public String getPrevVerCode(String tablename,String data_id,String ver_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT MAX(ver_code) FROM " + tablename + " where ancestor = '" + data_id +"' and not(ver_code = '" + ver_code + "')";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		String prev_ver_code = rs.getString(1);
		
		stmt.close();
		rs.close();

		return prev_ver_code;
	} //getPrevVerCode()


	/*******************************************************************
	 * master_data 에서 data_id와 일치하는 항목을 삭제한다.
	 *******************************************************************/
	public void deleteByDataId(String data_id) throws Exception{
		Statement stmt = null;
		String query= "";

		query = "delete master_data where data_id ='"+data_id+"'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();

	}//deleteByDataId()


	/*******************************************************************
	 * 해당 문서테이블에서 data_id와 ver_code이 일치하는 항목을 삭제한다.
	 *******************************************************************/
	public void deletByVerCode(String tablename,String data_id,String ver_code) throws Exception{
		Statement stmt = null;
		String query= "";

		query = "delete " + tablename + " where ancestor ='"+data_id+"' and ver_code = '" + ver_code +"'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();

	}//deletByVerCode()

	/*******************************************************************
	 * master_data의 ver_code 필드를 지정한 값으로 세팅한다.(data_id를 가지고)
	 *******************************************************************/
	public void updateVerByDataId(String ver_code,String data_id) throws Exception{
		Statement stmt = null;
		String query= "";

		query = "UPDATE master_data SET last_version ='" + ver_code + "' WHERE data_id='"+data_id+"'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();

	}//updateStat()

	/*****************************************************************************
	 * 전자결재모듈의 app_save 테이블에서 선택된 pid의 레코드 정보를 가져온 뒤,
	 * 승인원 관리모듈의 approval_info 테이블에 입력한다.
	 *****************************************************************************/
	public void getAppInfoAndSave(String pid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		String query = "SELECT writer,writer_name,writer_div,writer_rank,write_date,";
		query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
		query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
		query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
		query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
		query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
		query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
		query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date ";
		query += "FROM app_save WHERE pid='" + pid + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			query = "INSERT INTO dms_approval_info (pid,writer,writer_name,writer_div,writer_rank,write_date,";
			query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
			query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
			query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
			query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
			query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
			query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
			query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date) ";
			query += "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1,pid);
			pstmt.setString(2,rs.getString("writer"));
			pstmt.setString(3,rs.getString("writer_name"));
			pstmt.setString(4,rs.getString("writer_div"));
			pstmt.setString(5,rs.getString("writer_rank"));
			pstmt.setString(6,rs.getString("write_date"));
			pstmt.setString(7,rs.getString("reviewer"));
			pstmt.setString(8,rs.getString("reviewer_name"));
			pstmt.setString(9,rs.getString("reviewer_div"));
			pstmt.setString(10,rs.getString("reviewer_rank"));
			pstmt.setString(11,rs.getString("review_comment"));
			pstmt.setString(12,rs.getString("review_date"));
			pstmt.setString(13,rs.getString("decision"));
			pstmt.setString(14,rs.getString("decision_name"));
			pstmt.setString(15,rs.getString("decision_div"));
			pstmt.setString(16,rs.getString("decision_rank"));
			pstmt.setString(17,rs.getString("decision_comment"));
			pstmt.setString(18,rs.getString("decision_date"));
			pstmt.setString(19,rs.getString("agree"));
			pstmt.setString(20,rs.getString("agree_name"));
			pstmt.setString(21,rs.getString("agree_div"));
			pstmt.setString(22,rs.getString("agree_rank"));
			pstmt.setString(23,rs.getString("agree_comment"));
			pstmt.setString(24,rs.getString("agree_date"));
			pstmt.setString(25,rs.getString("agree2"));
			pstmt.setString(26,rs.getString("agree2_name"));
			pstmt.setString(27,rs.getString("agree2_div"));
			pstmt.setString(28,rs.getString("agree2_rank"));
			pstmt.setString(29,rs.getString("agree2_comment"));
			pstmt.setString(30,rs.getString("agree2_date"));
			pstmt.setString(31,rs.getString("agree3"));
			pstmt.setString(32,rs.getString("agree3_name"));
			pstmt.setString(33,rs.getString("agree3_div"));
			pstmt.setString(34,rs.getString("agree3_rank"));
			pstmt.setString(35,rs.getString("agree3_comment"));
			pstmt.setString(36,rs.getString("agree3_date"));
			pstmt.setString(37,rs.getString("agree4"));
			pstmt.setString(38,rs.getString("agree4_name"));
			pstmt.setString(39,rs.getString("agree4_div"));
			pstmt.setString(40,rs.getString("agree4_rank"));
			pstmt.setString(41,rs.getString("agree4_comment"));
			pstmt.setString(42,rs.getString("agree4_date"));
			pstmt.setString(43,rs.getString("agree5"));
			pstmt.setString(44,rs.getString("agree5_name"));
			pstmt.setString(45,rs.getString("agree5_div"));
			pstmt.setString(46,rs.getString("agree5_rank"));
			pstmt.setString(47,rs.getString("agree5_comment"));
			pstmt.setString(48,rs.getString("agree5_date"));
		
			pstmt.executeUpdate();
			pstmt.close();
		}
		stmt.close();
		rs.close();
	}

	/*****************************************************************************
	 * approval_info 테이블에서 선택된 관리번호(pid)의 결재정보를 가져온다.
	 *****************************************************************************/
	public ApprovalInfoTable getApprovalInfo(String pid,String sign_path) throws Exception{
		ApprovalInfoTable table = new ApprovalInfoTable();
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT writer,writer_name,writer_div,writer_rank,write_date,";
		query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
		query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
		query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
		query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
		query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
		query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
		query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date ";
		query += "FROM dms_approval_info WHERE pid='" + pid + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		String sign_src = "";
		String memo = "";
		while(rs.next()){
			table = new ApprovalInfoTable();

			table.setWriterName(rs.getString("writer_name"));
			String writer = rs.getString("writer");
			sign_src = "<img src = '" + sign_path + writer + ".gif' border='0'>";
			table.setWriterSig(sign_src);

			String reviewer = rs.getString("reviewer");
			if(reviewer == null || reviewer.equals("")){
				sign_src = "<img src = '" + sign_path + "wan.gif' border='0'>";			
				table.setReviewerName("");
			}else{
				sign_src = "<img src = '" + sign_path + reviewer + ".gif' border='0'>";
				table.setReviewerName(rs.getString("reviewer_name"));
			}
			table.setReviewerSig(sign_src);

			table.setDecisionName(rs.getString("decision_name"));
			String decision = rs.getString("decision");
			sign_src = "<img src = '" + sign_path + decision + ".gif' border='0'>";
			table.setDecisionSig(sign_src);

			memo = "기안 " + writer + " " + rs.getString("writer_name") + " " + rs.getString("writer_rank") + " " + rs.getString("writer_div") + " " + rs.getString("write_date") + "\n";
			if(!reviewer.equals("")){
				memo += "검토 " + reviewer + " " + rs.getString("reviewer_name") + " " + rs.getString("reviewer_rank") + " " + rs.getString("reviewer_div") + " " + rs.getString("review_date") + " " + rs.getString("review_comment") + "\n";
			}
			memo += "승인 " + decision + " " + rs.getString("decision_name") + " " + rs.getString("decision_rank") + " " + rs.getString("decision_div") + " " + rs.getString("decision_date") + " " + rs.getString("decision_comment") + "\n";

			String agree = rs.getString("agree");
			if(agree != null && !agree.equals("")) memo += "합의 " + agree + " " + rs.getString("agree_name") + " " + rs.getString("agree_rank") + " " + rs.getString("agree_div") + " " + rs.getString("agree_date") + " " + rs.getString("agree_comment") + "\n";

			String agree2 = rs.getString("agree2");
			if(agree2 != null && !agree2.equals("")) memo += "합의 " + agree2 + " " + rs.getString("agree2_name") + " " + rs.getString("agree2_rank") + " " + rs.getString("agree2_div") + " " + rs.getString("agree2_date") + " " + rs.getString("agree2_comment") + "\n";

			String agree3 = rs.getString("agree3");
			if(agree3 != null && !agree3.equals("")) memo += "합의 " + agree3 + " " + rs.getString("agree3_name") + " " + rs.getString("agree3_rank") + " " + rs.getString("agree3_div") + " " + rs.getString("agree3_date") + " " + rs.getString("agree3_comment") + "\n";

			String agree4 = rs.getString("agree4");
			if(agree4 != null && !agree4.equals("")) memo += "합의 " + agree4 + " " + rs.getString("agree4_name") + " " + rs.getString("agree4_rank") + " " + rs.getString("agree4_div") + " " + rs.getString("agree4_date") + " " + rs.getString("agree4_comment") + "\n";

			String agree5 = rs.getString("agree5");
			if(agree5 != null && !agree5.equals("")) memo += "합의 " + agree5 + " " + rs.getString("agree5_name") + " " + rs.getString("agree5_rank") + " " + rs.getString("agree5_div") + " " + rs.getString("agree5_date") + " " + rs.getString("agree5_comment") + "\n";

			table.setMemo(memo);

			//System.out.println(memo);

		}
		stmt.close();
		rs.close();

		return table;
	}
}		