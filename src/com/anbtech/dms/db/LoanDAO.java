package com.anbtech.dms.db;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.business.*;

import java.sql.*;
import java.util.*;

public class LoanDAO{
	private Connection con;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public LoanDAO(Connection con){
		this.con = con;
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


	/*****************************************************************************
	 * ���ǿ� �´� loan_list ���̺� ����Ʈ�� �����´�.
	 *****************************************************************************/
	public ArrayList getLoan_List(String tablename,String mode,String searchword,String searchscope,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.dms.entity.LoanTable table = new com.anbtech.dms.entity.LoanTable();
		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 10;			// ������������ ����� ���ڵ� ��
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 30;	// ������ �ִ� ǥ�ñ���

		int current_page_num =Integer.parseInt(page);

		com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);

		String where = loanBO.getWhere(mode,searchword,searchscope);

		int total = getTotalCount(tablename, where);	// ��ü ���ڵ� ����
		total = 1;
		int recNum = total;

		//�˻����ǿ� �´� �Խù��� �����´�.
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

			//���� ������ data_id�� ������ � ������ �������� ��, ���̺���� ��´�.
			String category_id = masterDAO.getCategoryId(data_id);
			String curr_tablename = masterDAO.getTableName(category_id);

			// �����ȣ�� ��ũ ����
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
	 * no�� �ش��ϴ� loan_list ���̺� ������ �����´�.
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
	 * �����ȣ�� ����Ͽ� �����Ѵ�.
	 *****************************************************************/
	public String getLoanNo() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		//���� �⵵ ���
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yy");
		String now_year	= vans.format(now);

		//��ǥ���� + �⵵
		String loan_head = "����" + now_year;

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
	 * �����Ƿ� ������ DB �� �����Ѵ�.
	 *****************************************************************/
	public void saveData(String loan_no,String doc_no,String data_id,String ver_code,String requestor,String why,String copy_num,String return_date) throws Exception{

		PreparedStatement pstmt = null;


		//��Ͻð�
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
	 * �۾�ó������� ������Ʈ�Ѵ�.
	 ******************************/
	public void updateWhy(String no,String why,String loan_day) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;

		int loan_day_int=Integer.parseInt(loan_day);
		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);
		com.anbtech.date.anbDate date_str=new com.anbtech.date.anbDate(); 
		//��ȿ��¥ ó��
		String[] token_temp;		// ���۳�¥ ��/��/�� ��ū ���� ����
			
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

			String valid_date=date_str.getDate(year,month,day,loan_day_int); // ��ȿ�Ⱓ ���(���ᳯ¥)
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
	 * loan_list�� stat �ʵ尪�� ������ ������ �����Ѵ�.
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
	 * ����ݷ� �� ����ݷ� ������ ������Ʈ�Ѵ�.
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
	 * ī�װ� �ڵ忡 �ش��ϴ� ���̺���� ������ �����Ѵ�.
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
	 * no�� �ش��ϴ� ���⹮�� ����
	 *******************************************************************/
	public void deleteLoanData(String no) throws Exception{
		Statement stmt = null;
		
		String query = "DELETE loan_list WHERE no ='"+no+"'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		
		stmt.close();
	}

	/*******************************************************************
	 * loan_list �� no�� �ش��ϴ� �����Ƿ��� ����� �����´�.
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
	 * ������ �뺸�� ���ο��� �߼��� ���� ������ DB�� �����Ѵ�.
	 *****************************************************************/
	public void saveEmail(String login_id,String requestor,String bon_path,String filename) throws Exception{

		PreparedStatement pstmt = null;

		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		String pid = "doc" + System.currentTimeMillis();

		AccessControlDAO ac = new AccessControlDAO(con);
		String requestor_s = requestor + "/" + ac.getUserName(requestor);

		//post_master ���̺� �Է�
		String query = "INSERT INTO post_master";
		query += "(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file) ";
		query += "VALUES (?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,pid);
		pstmt.setString(2,"�����Ƿ� ��� �뺸");
		pstmt.setString(3,login_id);
		pstmt.setString(4,"���� ������");
		pstmt.setString(5,w_time);
		pstmt.setString(6,requestor_s);
		pstmt.setString(7,"0");
		pstmt.setString(8,"email");
		pstmt.setString(9,bon_path);
		pstmt.setString(10,filename);
		
		pstmt.executeUpdate();
		pstmt.close();

		//post_letter ���̺� �Է�
		query = "INSERT INTO post_letter";
		query += "(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen) ";
		query += "VALUES (?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,pid);
		pstmt.setString(2,"�����Ƿ� ��� �뺸");
		pstmt.setString(3,login_id);
		pstmt.setString(4,"���� ������");
		pstmt.setString(5,w_time);
		pstmt.setString(6,requestor);
		pstmt.setString(7,"0");
		
		pstmt.executeUpdate();
		pstmt.close();

	} //saveEmail()

}		