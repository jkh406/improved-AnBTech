package com.anbtech.dms.db;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.business.*;

import java.sql.*;
import java.util.*;

public class LoanDAO{
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public LoanDAO(Connection con){
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


	/*****************************************************************************
	 * 조건에 맞는 loan_list 테이블 리스트을 가져온다.
	 *****************************************************************************/
	public ArrayList getLoan_List(String tablename,String mode,String searchword,String searchscope,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.dms.entity.LoanTable table = new com.anbtech.dms.entity.LoanTable();
		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 10;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이

		int current_page_num =Integer.parseInt(page);

		com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);

		String where = loanBO.getWhere(mode,searchword,searchscope);

		int total = getTotalCount(tablename, where);	// 전체 레코드 갯수
		total = 1;
		int recNum = total;

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT no,loan_no,data_id,ver_code,doc_no,requestor_s,req_date,";
		query += "return_date,stat,end_date";
		query += " FROM " + tablename + where + " ORDER BY no DESC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String no = rs.getString("no");
			String loan_no = rs.getString("loan_no");
			String data_id = rs.getString("data_id");
			String ver_code = rs.getString("ver_code");
			String doc_no = rs.getString("doc_no");
			String requestor = rs.getString("requestor_s");
			String req_date = rs.getString("req_date");
			String return_date = rs.getString("return_date");
			String stat = loanBO.getStatus(rs.getString("stat"));
			String end_date = rs.getString("end_date");

			//현재 문서의 data_id를 가지고 어떤 종류의 문서인지 즉, 테이블명을 얻는다.
			String category_id = masterDAO.getCategoryId(data_id);
			String curr_tablename = masterDAO.getTableName(category_id);

			// 대출번호에 링크 설정
			String loan_no_link = "<A HREF='AnBDMS?tablename="+curr_tablename+"&mode=view_l";
			loan_no_link += "&page="+page+"&searchword="+searchword;
			loan_no_link += "&searchscope="+searchscope;
			loan_no_link += "&no="+no+"&d_id="+data_id+"&ver="+ver_code+"'>";
			loan_no = loan_no_link + loan_no + "</a>";


			table = new com.anbtech.dms.entity.LoanTable();
			table.setNo(no);
			table.setLoanNo(loan_no);
			table.setDataId(data_id);
			table.setVerCode(ver_code);
			table.setDocNo(doc_no);
			table.setRequestor(requestor);
			table.setReqDate(req_date);
			table.setReturnDate(return_date);
			table.setLoanEndDate(end_date);
			table.setStat(stat);

			table_list.add(table);

			recNum--;

		}

		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	 * no에 해당하는 loan_list 테이블 내용을 가져온다.
	 *******************************************************************/
	public LoanTable getLoanData(String tablename, String no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.dms.entity.LoanTable table = new com.anbtech.dms.entity.LoanTable();
		String query = "SELECT * FROM "+tablename+" where no = '"+no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		table.setNo(rs.getString("no"));
		table.setLoanNo(rs.getString("loan_no"));
		table.setDataId(rs.getString("data_id"));
		table.setVerCode(rs.getString("ver_code"));
		table.setDocNo(rs.getString("doc_no"));
		table.setRequestor(rs.getString("requestor_s"));
		table.setReqDate(rs.getString("req_date"));
		table.setReturnDate(rs.getString("return_date"));
		table.setWhyLoan(rs.getString("why_loan"));
		table.setCopyNum(rs.getString("copy_num"));
		table.setStat(rs.getString("stat"));
		table.setWhyReject(rs.getString("why"));
		table.setLoanEndDate(rs.getString("end_date"));

		stmt.close();
		rs.close();
		return table;
	}

	/*****************************************************************
	 * 대출번호를 계산하여 리턴한다.
	 *****************************************************************/
	public String getLoanNo() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		//현재 년도 계산
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yy");
		String now_year	= vans.format(now);

		//대표문자 + 년도
		String loan_head = "문서" + now_year;

		String loan_serial = "";
		String loan_no = "";
		
		String query = "SELECT MAX(loan_no) FROM loan_list where loan_no like '"+loan_head+"%'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		loan_no = rs.getString(1);

		if(loan_no == null){
			loan_serial = "001";
		}else{
			loan_serial = loan_no.substring(5,8);
			com.anbtech.util.normalFormat nf = new com.anbtech.util.normalFormat("000");
			loan_serial = nf.toDigits(Integer.parseInt(loan_serial)+1);
		}

		loan_no = loan_head + "-" + loan_serial;

		stmt.close();
		rs.close();
		return loan_no;

	} //getLoanNo


	/*****************************************************************
	 * 대출의뢰 정보를 DB 에 저장한다.
	 *****************************************************************/
	public void saveData(String loan_no,String doc_no,String data_id,String ver_code,String requestor,String why,String copy_num,String return_date) throws Exception{

		PreparedStatement pstmt = null;


		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		AccessControlDAO ac = new AccessControlDAO(con);
		String requestor_s = ac.getUserName(requestor) + "/" + requestor;

		String query = "INSERT INTO loan_list";
		query += "(loan_no,doc_no,data_id,ver_code,requestor,requestor_s,req_date,return_date,why_loan,copy_num,stat) ";
		query += "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,loan_no);
		pstmt.setString(2,doc_no);
		pstmt.setString(3,data_id);
		pstmt.setString(4,ver_code);
		pstmt.setString(5,requestor);
		pstmt.setString(6,requestor_s);
		pstmt.setString(7,w_time);
		pstmt.setString(8,return_date);
		pstmt.setString(9,why);
		pstmt.setString(10,copy_num);
		pstmt.setString(11,"1");
		
		pstmt.executeUpdate();
		pstmt.close();
	} //saveData()//


	/******************************
	 * 작업처리결과를 업데이트한다.
	 ******************************/
	public void updateWhy(String no,String why,String loan_day) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;

		int loan_day_int=Integer.parseInt(loan_day);
		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);
		com.anbtech.date.anbDate date_str=new com.anbtech.date.anbDate(); 
		//유효날짜 처리
		String[] token_temp;		// 시작날짜 년/월/일 토큰 저장 변수
			
			java.util.StringTokenizer today_token=new java.util.StringTokenizer(w_time,"-");
			
			int i=0;
			int j=today_token.countTokens();
			token_temp=new String[j];

			while(today_token.hasMoreTokens()){
				token_temp[i]=today_token.nextToken();
				i++;
			}
			
			int year=Integer.parseInt(token_temp[0]);
			int month=Integer.parseInt(token_temp[1]);
			int day=Integer.parseInt(token_temp[2]);

			String valid_date=date_str.getDate(year,month,day,loan_day_int); // 유효기간 계산(종료날짜)
			valid_date=valid_date.replace('/','-');

		String query= "UPDATE loan_list SET why=?,return_date=?,end_date=? WHERE no='"+no+"'";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,why);
		pstmt.setString(2,w_time);
		pstmt.setString(3,valid_date);
		pstmt.executeUpdate();
		pstmt.close();

	}//updateWhy()


	/*******************************************************************
	 * loan_list의 stat 필드값을 지정한 값으로 세팅한다.
	 *******************************************************************/
	public void updateStat(String no,String stat) throws Exception{
		Statement stmt = null;
		String query= "";

		query = "UPDATE loan_list SET stat='" + stat + "' WHERE no='"+no+"'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();

	}//updateStat()


	/*******************************************************************
	 * 대출반려 시 대출반려 사유를 업데이트한다.
	 *******************************************************************/
	public void updateWhyReject(String no,String why_reject) throws Exception{
		Statement stmt = null;
		String query= "";

		query = "UPDATE loan_list SET why ='" + why_reject + "' WHERE no='"+no+"'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();

	}//updateWhyReject()


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
	 * no에 해당하는 대출문서 삭제
	 *******************************************************************/
	public void deleteLoanData(String no) throws Exception{
		Statement stmt = null;
		
		String query = "DELETE loan_list WHERE no ='"+no+"'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		
		stmt.close();
	}

	/*******************************************************************
	 * loan_list 의 no에 해당하는 대출의뢰자 사번을 가져온다.
	 *******************************************************************/
	public String getRequestorId(String no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT requestor FROM loan_list where no = '"+no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String requestor_name = rs.getString("requestor");
		stmt.close();
		rs.close();
		return requestor_name;
	} //getRequestorId()


	/*****************************************************************
	 * 대출결과 통보용 개인우편 발송을 위한 내용을 DB에 저장한다.
	 *****************************************************************/
	public void saveEmail(String login_id,String requestor,String bon_path,String filename) throws Exception{

		PreparedStatement pstmt = null;

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		String pid = "doc" + System.currentTimeMillis();

		AccessControlDAO ac = new AccessControlDAO(con);
		String requestor_s = requestor + "/" + ac.getUserName(requestor);

		//post_master 테이블 입력
		String query = "INSERT INTO post_master";
		query += "(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file) ";
		query += "VALUES (?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,pid);
		pstmt.setString(2,"대출의뢰 결과 통보");
		pstmt.setString(3,login_id);
		pstmt.setString(4,"문서 관리자");
		pstmt.setString(5,w_time);
		pstmt.setString(6,requestor_s);
		pstmt.setString(7,"0");
		pstmt.setString(8,"email");
		pstmt.setString(9,bon_path);
		pstmt.setString(10,filename);
		
		pstmt.executeUpdate();
		pstmt.close();

		//post_letter 테이블 입력
		query = "INSERT INTO post_letter";
		query += "(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen) ";
		query += "VALUES (?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,pid);
		pstmt.setString(2,"대출의뢰 결과 통보");
		pstmt.setString(3,login_id);
		pstmt.setString(4,"문서 관리자");
		pstmt.setString(5,w_time);
		pstmt.setString(6,requestor);
		pstmt.setString(7,"0");
		
		pstmt.executeUpdate();
		pstmt.close();

	} //saveEmail()

}		