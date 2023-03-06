package com.anbtech.crm.db;

import com.anbtech.crm.entity.*;
import com.anbtech.crm.business.*;
import com.anbtech.crm.db.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class CrmDAO{
	private Connection con;

	public CrmDAO(Connection con){
		this.con = con;
	}

	/************************************
	 * 검색조건에 맞는 고객사 목록 가져오기
	 ************************************/
	public ArrayList getCompanyList(String mode,String module,String searchword,String searchscope,String category,String page,String login_id) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
		int current_page_num =Integer.parseInt(page);

		ArrayList company_list = new ArrayList();
		CrmBO crmBO = new CrmBO(con);
		CompanyInfoTable company = new CompanyInfoTable();
		
		String where = crmBO.getWhere(mode,searchword, searchscope, category,login_id);

		int total = getTotalCount("company_customer", where);
		int recNum = total;

		String query = "SELECT * FROM company_customer " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String mid					= rs.getString("mid");				//관리 번호
			String name_kor				= rs.getString("name_kor");			//한글 상호명
			String name_eng				= rs.getString("name_eng");			//영문 상호명
			String chief_name			= rs.getString("chief_name");		//대표자 이름
			String main_tel_no			= rs.getString("main_tel_no");		//대표전화
			String main_fax_no			= rs.getString("main_fax_no");		//대표팩스
			String homepage_url			= rs.getString("homepage_url");		//홈페이지 주소
			String trade_type			= rs.getString("trade_type");		//거래구분(매입/매출)

			String name_link = "<a href = 'CrmServlet?mode=company_view&module="+module;
			name_link += "&page="+page+"&searchword="+searchword;
			name_link += "&searchscope="+searchscope+"&category="+category;
			name_link += "&no="+mid+"'>";
			name_link = name_link + name_kor + "</a>";

			String link_homepage_url = "<a href = '" + homepage_url + "' target = '_blank'>" + homepage_url + "</a>";

			company = new CompanyInfoTable();
			company.setMid(mid);
			company.setNameKor(name_link);
			company.setNameEng(name_eng);
			company.setChiefName(chief_name);
			company.setMainTelNo(main_tel_no);
			company.setMainFaxNo(main_fax_no);
			company.setHomepageUrl(link_homepage_url);
			company.setTradeType(trade_type);

			company_list.add(company);
			recNum--;
		}
		stmt.close();
		rs.close();

		return company_list;
	}

	/************************************
	 * 선택된 관리번호의 회사 정보 가져오기
	 ************************************/
	public CompanyInfoTable getCompanyInfo(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		CompanyInfoTable company = new CompanyInfoTable();

		String query = "SELECT * FROM company_customer WHERE mid = '" + mid +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		company.setMid(rs.getString("mid"));
		company.setCompanyNo(rs.getString("company_no"));
		company.setPasswd(rs.getString("passwd"));
		company.setNameKor(rs.getString("name_kor"));
		company.setNameEng(rs.getString("name_eng"));
		company.setChiefName(rs.getString("chief_name"));
		company.setChiefPersonalNo(rs.getString("chief_personal_no"));
		company.setCompanyAddress(rs.getString("company_address"));
		company.setCompanyPostNo(rs.getString("company_post_no"));
		company.setMainTelNo(rs.getString("main_tel_no"));
		company.setMainFaxNo(rs.getString("main_fax_no"));
		company.setHomepageUrl(rs.getString("homepage_url"));
		company.setBusinessType(rs.getString("business_type"));
		company.setBusinessItem(rs.getString("business_item"));
		company.setTradeStartTime(rs.getString("trade_start_time"));
		company.setTradeEndTime(rs.getString("trade_end_time"));
		company.setCompanyType(rs.getString("company_type"));
		company.setTradeType(rs.getString("trade_type"));
		company.setCreditLevel(rs.getString("credit_level"));
		company.setEstimateReqLevel(rs.getString("estimate_req_level"));
		company.setWorkerNumber(rs.getString("worker_number"));
		company.setMainBankName(rs.getString("main_bank_name"));
		company.setMainNewspaperName(rs.getString("main_newspaper_name"));
		company.setMainProductName(rs.getString("main_product_name"));
		company.setCorporationNo(rs.getString("corporation_no"));
		company.setFoundingDay(rs.getString("founding_day"));
		company.setOtherInfo(rs.getString("other_info"));
		company.setWriter(rs.getString("writer"));
		company.setWriterInfo(rs.getString("writer_info"));
		company.setWrittenDay(rs.getString("written_day"));
		company.setModifier(rs.getString("modifier"));
		company.setModifierInfo(rs.getString("modifier_info"));
		company.setModifiedDay(rs.getString("modified_day"));
		company.setModifyHistory(rs.getString("modify_history"));
		company.setFileName(rs.getString("fname"));
		company.setFileType(rs.getString("ftype"));
		company.setFileSize(rs.getString("fsize"));
		company.setFileUmask(rs.getString("umask"));

		stmt.close();
		rs.close();
		return company;
	}

	/*******************************
	 * 고객사 정보 입력 처리
	 *******************************/
	public void saveCompanyInfo(String company_no,String passwd,String name_kor,String name_eng,String chief_name,String chief_personal_no,String company_address,String company_post_no,String main_tel_no,String main_fax_no,String homepage_url,String business_type,String business_item,String trade_start_time,String trade_end_time,String company_type,String trade_type,String credit_level,String estimate_req_level,String worker_number,String main_bank_name,String main_newspaper_name,String main_product_name,String corporation_no,String founding_day,String other_info,String login_id) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO company_customer(company_no,passwd,name_kor,name_eng,chief_name,chief_personal_no,company_address,company_post_no,main_tel_no,main_fax_no,homepage_url,business_type,business_item,trade_start_time,trade_end_time,company_type,trade_type,credit_level,estimate_req_level,worker_number,main_bank_name,main_newspaper_name,main_product_name,corporation_no,founding_day,other_info,writer,written_day,modifier,modified_day,writer_info,modifier_info,modify_history) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		//작성 시간 구하기
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//작성자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = division+"/"+user_rank+"/"+user_name;

		String modify_history = "최초등록 " + w_time + "/" + division + "/" + user_rank + "/" + user_name + "/" + login_id;

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,company_no);
		pstmt.setString(2,passwd);
		pstmt.setString(3,name_kor);
		pstmt.setString(4,name_eng);
		pstmt.setString(5,chief_name);
		pstmt.setString(6,chief_personal_no);
		pstmt.setString(7,company_address);
		pstmt.setString(8,company_post_no);
		pstmt.setString(9,main_tel_no);
		pstmt.setString(10,main_fax_no);
		pstmt.setString(11,homepage_url);
		pstmt.setString(12,business_type);
		pstmt.setString(13,business_item);
		pstmt.setString(14,trade_start_time);
		pstmt.setString(15,trade_end_time);
		pstmt.setString(16,company_type);
		pstmt.setString(17,trade_type);
		pstmt.setString(18,credit_level);
		pstmt.setString(19,estimate_req_level);
		pstmt.setString(20,worker_number);
		pstmt.setString(21,main_bank_name);
		pstmt.setString(22,main_newspaper_name);
		pstmt.setString(23,main_product_name);
		pstmt.setString(24,corporation_no);
		pstmt.setString(25,founding_day);
		pstmt.setString(26,other_info);
		pstmt.setString(27,login_id);
		pstmt.setString(28,w_time);
		pstmt.setString(29,"");
		pstmt.setString(30,"");
		pstmt.setString(31,writer_info);
		pstmt.setString(32,"");
		pstmt.setString(33,modify_history);

		pstmt.executeUpdate();
		pstmt.close();
	}


	/*******************************
	 * 고객사 정보 수정 처리
	 *******************************/
	public void updCompanyInfo(String no,String company_no,String passwd,String name_kor,String name_eng,String chief_name,String chief_personal_no,String company_address,String company_post_no,String main_tel_no,String main_fax_no,String homepage_url,String business_type,String business_item,String trade_start_time,String trade_end_time,String company_type,String trade_type,String credit_level,String estimate_req_level,String worker_number,String main_bank_name,String main_newspaper_name,String main_product_name,String corporation_no,String founding_day,String other_info,String login_id,String modify_history) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		//작성 시간 구하기
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//작성자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = division+"/"+user_rank+"/"+user_name;

		modify_history += "<br>정보변경 " + w_time + "/" + division + "/" + user_rank + "/" + user_name + "/" + login_id;


		query = "UPDATE company_customer SET company_no=?,passwd=?,name_kor=?,name_eng=?,chief_name=?,chief_personal_no=?,company_address=?,company_post_no=?,main_tel_no=?,main_fax_no=?,homepage_url=?,business_type=?,business_item=?,trade_start_time=?,trade_end_time=?,company_type=?,trade_type=?,credit_level=?,estimate_req_level=?,worker_number=?,main_bank_name=?,main_newspaper_name=?,main_product_name=?,corporation_no=?,founding_day=?,other_info=?,modifier=?,modified_day=?,modifier_info=?,modify_history=? WHERE mid='" + no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,company_no);
		pstmt.setString(2,passwd);
		pstmt.setString(3,name_kor);
		pstmt.setString(4,name_eng);
		pstmt.setString(5,chief_name);
		pstmt.setString(6,chief_personal_no);
		pstmt.setString(7,company_address);
		pstmt.setString(8,company_post_no);
		pstmt.setString(9,main_tel_no);
		pstmt.setString(10,main_fax_no);
		pstmt.setString(11,homepage_url);
		pstmt.setString(12,business_type);
		pstmt.setString(13,business_item);
		pstmt.setString(14,trade_start_time);
		pstmt.setString(15,trade_end_time);
		pstmt.setString(16,company_type);
		pstmt.setString(17,trade_type);
		pstmt.setString(18,credit_level);
		pstmt.setString(19,estimate_req_level);
		pstmt.setString(20,worker_number);
		pstmt.setString(21,main_bank_name);
		pstmt.setString(22,main_newspaper_name);
		pstmt.setString(23,main_product_name);
		pstmt.setString(24,corporation_no);
		pstmt.setString(25,founding_day);
		pstmt.setString(26,other_info);
		pstmt.setString(27,login_id);
		pstmt.setString(28,w_time);
		pstmt.setString(29,writer_info);
		pstmt.setString(30,modify_history);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 고객사정보 삭제 처리(관리번호)
	 *******************************/
	public void deleteCompanyInfo(String mid) throws Exception{
		Statement stmt = null;
		String query = "DELETE company_customer WHERE mid = '" + mid + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}


	/************************************
	 * 검색조건에 맞는 고객 목록 가져오기
	 ************************************/
	public ArrayList getCustomerList(String mode,String searchword,String searchscope,String category,String page,String login_id) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
		int current_page_num =Integer.parseInt(page);

		ArrayList customer_list = new ArrayList();
		CrmBO crmBO = new CrmBO(con);
		CustomerInfoTable customer = new CustomerInfoTable();
		
		String where = crmBO.getWhere(mode,searchword, searchscope, category,login_id);

		int total = getTotalCount("personal_customer", where);
		int recNum = total;

		String query = "SELECT * FROM personal_customer " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String mid					= rs.getString("mid");				//관리 번호
			String name_kor				= rs.getString("name_kor");			//한글 명
			String name_eng				= rs.getString("name_eng");			//영문 명
			String company_name			= rs.getString("company_name");		//회사 명
			String position_rank		= rs.getString("position_rank");	//직급
			String company_tel_no		= rs.getString("company_tel_no");	//회사전화
			String mobile_no			= rs.getString("mobile_no");		//핸드폰
			String email_address		= rs.getString("email_address");	//전자우편 주소

			String name_link = "<a href = 'CrmServlet?mode=customer_view";
			name_link += "&page="+page+"&searchword="+searchword;
			name_link += "&searchscope="+searchscope+"&category="+category;
			name_link += "&no="+mid+"'>";
			name_link = name_link + name_kor + "</a>";

			String link_email_address = "<a href = 'mailto:" + email_address + "'>" + email_address + "</a>";

			customer = new CustomerInfoTable();
			customer.setMid(mid);
			customer.setNameKor(name_link);
			customer.setNameEng(name_eng);
			customer.setCompanyName(company_name);
			customer.setPositionRank(position_rank);
			customer.setCompanyTelNo(company_tel_no);
			customer.setMobileNo(mobile_no);
			customer.setEmailAddress(email_address);

			customer_list.add(customer);
			recNum--;
		}
		stmt.close();
		rs.close();

		return customer_list;
	}

	/************************************
	 * 선택된 관리번호의 고객 정보 가져오기
	 ************************************/
	public CustomerInfoTable getCustomerInfo(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		CustomerInfoTable customer = new CustomerInfoTable();

		String query = "SELECT * FROM personal_customer WHERE mid = '" + mid +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		customer.setMid(rs.getString("mid"));
		customer.setNameKor(rs.getString("name_kor"));
		customer.setNameEng(rs.getString("name_eng"));
		customer.setSex(rs.getString("sex"));
		customer.setCompanyName(rs.getString("company_name"));
		customer.setCompanyNo(rs.getString("company_no"));
		customer.setDivisionName(rs.getString("division_name"));
		customer.setMainJob(rs.getString("main_job"));
		customer.setPositionRank(rs.getString("position_rank"));
		customer.setMobileNo(rs.getString("mobile_no"));
		customer.setCompanyTelNo(rs.getString("company_tel_no"));
		customer.setCompanyFaxNo(rs.getString("company_fax_no"));
		customer.setCompanyAddress(rs.getString("company_address"));
		customer.setCompanyPostNo(rs.getString("company_post_no"));
		customer.setWhereToDm(rs.getString("where_to_dm"));
		customer.setEmailAddress(rs.getString("email_address"));
		customer.setHomepageUrl(rs.getString("homepage_url"));
		customer.setCustomerType(rs.getString("customer_type"));
		customer.setCustomerClass(rs.getString("customer_class"));
		customer.setHomeTelNo(rs.getString("home_tel_no"));
		customer.setHomeFaxNo(rs.getString("home_fax_no"));
		customer.setHomeAddress(rs.getString("home_address"));
		customer.setHomePostNo(rs.getString("home_post_no"));
		customer.setPersonalNo(rs.getString("personal_no"));
		customer.setBirthday(rs.getString("birthday"));
		customer.setWhetherWedding(rs.getString("whether_wedding"));
		customer.setPartnerName(rs.getString("partner_name"));
		customer.setPartnerBirthday(rs.getString("partner_birthday"));
		customer.setWeddingDay(rs.getString("wedding_day"));
		customer.setHobby(rs.getString("hobby"));
		customer.setSpecialAbility(rs.getString("special_ability"));
		customer.setMajorField(rs.getString("major_field"));
		customer.setOtherInfo(rs.getString("other_info"));
		customer.setWriter(rs.getString("writer"));
		customer.setWriterInfo(rs.getString("writer_info"));
		customer.setWrittenDay(rs.getString("written_day"));
		customer.setModifier(rs.getString("modifier"));
		customer.setModifierInfo(rs.getString("modifier_info"));
		customer.setModifiedDay(rs.getString("modified_day"));
		customer.setModifyHistory(rs.getString("modify_history"));

		stmt.close();
		rs.close();
		return customer;
	}

	/*******************************
	 * 고객 정보 입력 처리
	 *******************************/
	public void saveCustomerInfo(String name_kor,String name_eng,String sex,String company_name,String company_no,String division_name,String main_job,String position_rank,String mobile_no,String company_tel_no,String company_fax_no,String company_address,String company_post_no,String where_to_dm,String email_address,String homepage_url,String customer_type,String customer_class,String home_tel_no,String home_fax_no,String home_address,String home_post_no,String personal_no,String birthday,String whether_wedding,String partner_name,String partner_birthday,String wedding_day,String hobby,String special_ability,String major_field,String other_info,String login_id) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO personal_customer(name_kor,name_eng,sex,company_name,company_no,division_name,main_job,position_rank,mobile_no,company_tel_no,company_fax_no,company_address,company_post_no,where_to_dm,email_address,homepage_url,customer_type,customer_class,home_tel_no,home_fax_no,home_address,home_post_no,personal_no,birthday,whether_wedding,partner_name,partner_birthday,wedding_day,hobby,special_ability,major_field,other_info,writer,written_day,modifier,modified_day,writer_info,modifier_info,modify_history) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		//작성 시간 구하기
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//작성자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = division+"/"+user_rank+"/"+user_name;

		String modify_history = "최초등록 " + w_time + "/" + division + "/" + user_rank + "/" + user_name + "/" + login_id;

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,name_kor);
		pstmt.setString(2,name_eng);
		pstmt.setString(3,sex);
		pstmt.setString(4,company_name);
		pstmt.setString(5,company_no);
		pstmt.setString(6,division_name);
		pstmt.setString(7,main_job);
		pstmt.setString(8,position_rank);
		pstmt.setString(9,mobile_no);
		pstmt.setString(10,company_tel_no);
		pstmt.setString(11,company_fax_no);
		pstmt.setString(12,company_address);
		pstmt.setString(13,company_post_no);
		pstmt.setString(14,where_to_dm);
		pstmt.setString(15,email_address);
		pstmt.setString(16,homepage_url);
		pstmt.setString(17,customer_type);
		pstmt.setString(18,customer_class);
		pstmt.setString(19,home_tel_no);
		pstmt.setString(20,home_fax_no);
		pstmt.setString(21,home_address);
		pstmt.setString(22,home_post_no);
		pstmt.setString(23,personal_no);
		pstmt.setString(24,birthday);
		pstmt.setString(25,whether_wedding);
		pstmt.setString(26,partner_name);
		pstmt.setString(27,partner_birthday);
		pstmt.setString(28,wedding_day);
		pstmt.setString(29,hobby);
		pstmt.setString(30,special_ability);
		pstmt.setString(31,major_field);
		pstmt.setString(32,other_info);
		pstmt.setString(33,login_id);
		pstmt.setString(34,w_time);
		pstmt.setString(35,"");
		pstmt.setString(36,"");
		pstmt.setString(37,writer_info);
		pstmt.setString(38,"");
		pstmt.setString(39,modify_history);

		pstmt.executeUpdate();
		pstmt.close();
	}


	/*******************************
	 * 고객 정보 수정 처리
	 *******************************/
	public void updCustomerInfo(String no,String name_kor,String name_eng,String sex,String company_name,String company_no,String division_name,String main_job,String position_rank,String mobile_no,String company_tel_no,String company_fax_no,String company_address,String company_post_no,String where_to_dm,String email_address,String homepage_url,String customer_type,String customer_class,String home_tel_no,String home_fax_no,String home_address,String home_post_no,String personal_no,String birthday,String whether_wedding,String partner_name,String partner_birthday,String wedding_day,String hobby,String special_ability,String major_field,String other_info,String login_id,String modify_history) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		//작성 시간 구하기
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//작성자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = division+"/"+user_rank+"/"+user_name;

		modify_history += "<br>정보변경 " + w_time + "/" + division + "/" + user_rank + "/" + user_name + "/" + login_id;

		query = "UPDATE personal_customer SET name_kor=?,name_eng=?,sex=?,company_name=?,company_no=?,division_name=?,main_job=?,position_rank=?,mobile_no=?,company_tel_no=?,company_fax_no=?,company_address=?,company_post_no=?,where_to_dm=?,email_address=?,homepage_url=?,customer_type=?,customer_class=?,home_tel_no=?,home_fax_no=?,home_address=?,home_post_no=?,personal_no=?,birthday=?,whether_wedding=?,partner_name=?,partner_birthday=?,wedding_day=?,hobby=?,special_ability=?,major_field=?,other_info=?,modifier=?,modified_day=?,modifier_info=?,modify_history=? WHERE mid='" + no + "'";

		pstmt = con.prepareStatement(query);

		pstmt.setString(1,name_kor);
		pstmt.setString(2,name_eng);
		pstmt.setString(3,sex);
		pstmt.setString(4,company_name);
		pstmt.setString(5,company_no);
		pstmt.setString(6,division_name);
		pstmt.setString(7,main_job);
		pstmt.setString(8,position_rank);
		pstmt.setString(9,mobile_no);
		pstmt.setString(10,company_tel_no);
		pstmt.setString(11,company_fax_no);
		pstmt.setString(12,company_address);
		pstmt.setString(13,company_post_no);
		pstmt.setString(14,where_to_dm);
		pstmt.setString(15,email_address);
		pstmt.setString(16,homepage_url);
		pstmt.setString(17,customer_type);
		pstmt.setString(18,customer_class);
		pstmt.setString(19,home_tel_no);
		pstmt.setString(20,home_fax_no);
		pstmt.setString(21,home_address);
		pstmt.setString(22,home_post_no);
		pstmt.setString(23,personal_no);
		pstmt.setString(24,birthday);
		pstmt.setString(25,whether_wedding);
		pstmt.setString(26,partner_name);
		pstmt.setString(27,partner_birthday);
		pstmt.setString(28,wedding_day);
		pstmt.setString(29,hobby);
		pstmt.setString(30,special_ability);
		pstmt.setString(31,major_field);
		pstmt.setString(32,other_info);
		pstmt.setString(33,login_id);
		pstmt.setString(34,w_time);
		pstmt.setString(35,writer_info);
		pstmt.setString(36,modify_history);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 고객정보 삭제 처리(관리번호)
	 *******************************/
	public void deleteCustomerInfo(String mid) throws Exception{
		Statement stmt = null;
		String query = "DELETE personal_customer WHERE mid = '" + mid + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}


	/*******************************
	 * 레코드의 전체 개수를 구한다.
	 *******************************/
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

	/******************************************************************
	 * 선택된 사업자번호에 해당하는 회사명을 가져온다.
	 ******************************************************************/
	public String getCompanyNameByNo(String company_no) throws Exception{
		Statement stmt	= null;
		ResultSet rs	= null;
		String sql		= "";
		String name		= "";

		sql = "SELECT name_kor FROM company_customer WHERE company_no = '" + company_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		while(rs.next()) name = rs.getString("name_kor");
		
		stmt.close();
		rs.close();

		return name;
	}


	/*************************************
	 * 첨부파일 정보를 DB에 저장한다.
	 *************************************/
	public void updTable(String set, String where) throws Exception{
		Statement stmt = null;
		String query = "UPDATE company_customer " + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*****************************************************************
	 * item_no에 해당하는 데이터의 첨부파일 리스트 가져오기
	 *****************************************************************/
	public ArrayList getFile_list(String mid) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;
		ArrayList file_list = new ArrayList();

		String query = "SELECT fname,fsize,ftype,umask FROM company_customer WHERE mid = '"+mid+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
	
		if(rs.next()){ 
			Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
			Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();
			Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();
			//Iterator fileumask_iter = com.anbtech.util.Token.getTokenList(rs.getString("umask")).iterator();
			while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()){
				CompanyInfoTable file = new CompanyInfoTable();
				file.setFileName((String)filename_iter.next());
				file.setFileType((String)filetype_iter.next());
				file.setFileSize((String)filesize_iter.next());
				//file.setFileUmask((String)fileumask_iter.next());
				//System.out.println("umask:"+rs.getString("umask"));
				file_list.add(file);
			}
		}
		stmt.close();
		rs.close();
		return file_list;
	}//getFile_list()

}		