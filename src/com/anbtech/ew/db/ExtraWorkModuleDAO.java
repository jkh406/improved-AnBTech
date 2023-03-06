package com.anbtech.ew.db;

import com.anbtech.ew.entity.*;
import com.anbtech.ew.business.*;
import com.anbtech.text.Hanguel;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.io.*;



public class ExtraWorkModuleDAO
{
	private Connection con;
		
	/*********
	 * ������
	 *********/
	public ExtraWorkModuleDAO(Connection con){
		this.con = con;
	}

	/***************************
	 * ���رٹ��ð����� ��������
	 ***************************/
	public StandardWorkTimeTable getStandardWTime() throws Exception {
		Statement st=null;
		ResultSet rs=null;
		StandardWorkTimeTable swtimeTable =  new StandardWorkTimeTable();
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		
		st = con.createStatement();
		String query = "SELECT * FROM standard_wtime";
		rs = st.executeQuery(query);
		while(rs.next()){
			swtimeTable.setSwNo(rs.getInt("sw_no"));
			swtimeTable.setModifyDate(rs.getString("modify_date"));
			swtimeTable.setFixStime(rs.getString("fix_stime"));		
			swtimeTable.setFixEtime(rs.getString("fix_etime"));
			swtimeTable.setFixStimeSat(rs.getString("fix_stime_sat"));
			swtimeTable.setFixEtimeSat(rs.getString("fix_etime_sat"));
			swtimeTable.setFixStimeHoliday(rs.getString("fix_stime_holiday"));
			swtimeTable.setFixEtimeHoliday(rs.getString("fix_etime_holiday"));
			swtimeTable.setOffStime(rs.getString("off_stime"));
			swtimeTable.setOffEtime(rs.getString("off_etime"));
			swtimeTable.setOffStimeSat(rs.getString("off_stime_sat"));
			swtimeTable.setOffEtimeSat(rs.getString("off_etime_sat"));
			swtimeTable.setOffStimeHoliday(rs.getString("off_stime_holiday"));
			swtimeTable.setOffEtimeHoliday(rs.getString("off_etime_holiday"));
			swtimeTable.setOverDayN(rs.getString("overday_n"));
			swtimeTable.setOverDayH(rs.getString("overday_h"));
			swtimeTable.setOverDayS(rs.getString("overday_s"));
			swtimeTable.setOverDayNName(ewBO.getStatname(rs.getString("overday_n")));
			swtimeTable.setOverDayHName(ewBO.getStatname(rs.getString("overday_h")));
			swtimeTable.setOverDaySName(ewBO.getStatname(rs.getString("overday_s")));
		}
		st.close();
		rs.close();

		return swtimeTable;
	}

	/***************************
	 * ���رٹ��ð����� ���
	 ***************************/
    public void saveStandardWtime(String  modify_date,String  fix_stime, String fix_etime, String fix_stime_sat,String fix_etime_sat,String off_stime,String off_etime,String fix_stime_holiday,String fix_etime_holiday,String off_stime_sat,String off_etime_sat,String off_stime_holiday,String off_etime_holiday,String overday_n,String overday_h,String overday_s) throws Exception {

		PreparedStatement pstmt = null;
		String query = "INSERT INTO standard_wtime (modify_date,fix_stime,fix_etime,fix_stime_sat,fix_etime_sat,off_stime,off_etime,fix_stime_holiday,fix_etime_holiday,off_stime_sat,off_etime_sat,off_stime_holiday,off_etime_holiday,overday_n,overday_h,overday_s) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,modify_date);
		pstmt.setString(2,fix_stime);
		pstmt.setString(3,fix_etime);
		pstmt.setString(4,fix_stime_sat);
		pstmt.setString(5,fix_etime_sat);
		pstmt.setString(6,off_stime);
		pstmt.setString(7,off_etime);
		pstmt.setString(8,fix_stime_holiday);
		pstmt.setString(9,fix_etime_holiday);
		pstmt.setString(10,off_stime_sat);
		pstmt.setString(11,off_etime_sat);
		pstmt.setString(12,off_stime_holiday);
		pstmt.setString(13,off_etime_holiday);
		pstmt.setString(14,overday_n);
		pstmt.setString(15,overday_h);
		pstmt.setString(16,overday_s);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*****************************************************************************
	 * �⵵��,�μ��� ����� �ð��ܱٹ� ����(�ñ�)�� ����Ͽ� ����Ѵ�.
	 * �ش� �����Ͱ� ������ ��°� ���ÿ� db�� �����Ѵ�.
	 * year:�ش�⵵, code:�μ��ڵ�(class_table�� code �ʵ尪)
	 *****************************************************************************/
	 public ArrayList getUserPayInfo(String year,String code) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";

		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable table = new com.anbtech.admin.entity.UserInfoTable();

		ArrayList table_view = new ArrayList();
		ArrayList user_list = new ArrayList();
		
		// user_table ���� ����� ����Ʈ�� �����´�.
		user_list = (ArrayList)userinfoDAO.getUserListByInnerCode(code);

		Iterator user_iter = user_list.iterator();

		// ������ ����� ID�� member_payinfo ���̺��� �˻��Ͽ� �ش� �����Ͱ� ������ �����ؼ� �����ϰ�,
		// ������ 0������ �����Ͽ� db�� ������ ���� �����Ѵ�.
		while(user_iter.hasNext()){
			table = (com.anbtech.admin.entity.UserInfoTable)user_iter.next();
			
			String id = table.getUserId();			// ����� ����� �����´�.

			// �ش� ����� ������ member_payinfo ���̺� �ִ��� üũ
			query = "SELECT hourly_pay FROM member_payinfo WHERE id = '" + id + "' AND this_year = '" + year + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			if(rs.next()){ // �ش� ����� ������ ������ �� ������ table_view�� �����Ѵ�.	
				table.setHourlyPay(rs.getString("hourly_pay"));
			}else{ // �ش� ����� ������ ������ �ټӳ���, �ܷ��� ����Ͽ� db�� ������ ���� table_view�� �����Ѵ�.
				query = "INSERT INTO member_payinfo (id,this_year,hourly_pay) VALUES('"+id+"','"+year+"','0')";
				stmt = con.createStatement();
				stmt.executeUpdate(query);
				table.setHourlyPay("0");
			}

			table_view.add(table);

		}
		stmt.close();
		rs.close();

		return table_view;
	}

	/****************************************************
	 * �ش� ����� ���س⵵ �ñ����� ��������� �����´�.
	 ***************************************************/
	public boolean enableReqWork(String this_year,String id) throws Exception {
		boolean bool = true;
		int i = 0;
		Statement st=null;
		ResultSet rs=null;
		st = con.createStatement();

		String query = "SELECT COUNT(*) FROM member_payinfo WHERE id = '"+id+"' AND this_year = '" + this_year + "'";
		rs = st.executeQuery(query);

		if(rs.next()) i = rs.getInt(1);
		if(i>0) bool = false;

		st.close();
		rs.close();

		return bool;
	}

	/************************************************
	 * Ư�� �ߺ���û���� ��������
	 ************************************************/
	public boolean enableSave(String member_id, String date) throws Exception {
		Statement st=null;
		ResultSet rs=null;
		boolean bool =false;
		
		String query = "SELECT count(o_no) FROM ew_history WHERE member_id='"+member_id+"' AND w_sdate = '"+date+"' AND status != '4'";
		st = con.createStatement();
		rs = st.executeQuery(query);
		
		int i = 0;
		if(rs.next()){
			i = rs.getInt(1);
		}
		if(i>0) bool =true;
			
		st.close();
		rs.close();

		return bool;
	}

	/***********************
	 * Ư�ٽ�û���� ����
	 ***********************/
	 public void saveHistory(String login_id,String w_sdate,String w_stime,String w_edate,String w_etime,String duty,String duty_cont,String w_type,String totaltime) throws Exception{
		Statement st=null;
		PreparedStatement pstmt = null;
		
		//��û�� ���� �������� 
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
		user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(login_id);
		String member_name = user_info.getUserName();
		String member_rankname = user_info.getUserRank();
		String division = user_info.getCode();
	    String division_name = user_info.getDivision();

		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String w_time = vans.format(now);

		String query = "INSERT INTO ew_history (member_id,member_name,member_rankname,division,division_name,w_type,w_sdate,w_stime,w_edate, w_etime,duty,duty_cont,c_date,status,r_sdate,r_stime,r_edate,r_etime,ew_confirm,total_time,result_time,salary,cal_confirm,pay_by_work,checkman,confirm_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,login_id);
		pstmt.setString(2,member_name);
		pstmt.setString(3,member_rankname);
		pstmt.setString(4,division);
		pstmt.setString(5,division_name);
		pstmt.setString(6,w_type);
		pstmt.setString(7,w_sdate);
		pstmt.setString(8,w_stime);
		pstmt.setString(9,w_edate);
		pstmt.setString(10,w_etime);
		pstmt.setString(11,duty);
		pstmt.setString(12,duty_cont);
		pstmt.setString(13,w_time);
		pstmt.setString(14,"1");
		pstmt.setString(15,w_sdate);
		pstmt.setString(16,w_stime);
		pstmt.setString(17,"");
		pstmt.setString(18,"");
		pstmt.setString(19,"");
		pstmt.setString(20,totaltime);
		pstmt.setString(21,"");
		pstmt.setString(22,"0");
		pstmt.setString(23,"n");
		pstmt.setString(24,"0");
		pstmt.setString(25,"");
		pstmt.setString(26,"");

		pstmt.executeUpdate();
		pstmt.close();
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

	/***********************
	 * ���κ� Ư�ٽ�û��Ȳ ����Ʈ ��������
	 ***********************/
	public ArrayList getMyEwReqList(String mode,String login_id,String page) throws Exception {
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ExtraWorkHistoryTable ewHistory;
		Statement st=null;
		ResultSet rs=null;

		int l_maxlist = 15;			// ������������ ����� ���ڵ� ��
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 30;	// ������ �ִ� ǥ�ñ���
		int current_page_num = Integer.parseInt(page);

		ArrayList arry = new ArrayList();
		st = con.createStatement();

		String where = ewBO.getWhere(mode,login_id);

		int total = getTotalCount("ew_history",where);
		int recNum = total;

		String query = "SELECT * FROM ew_history" + where + " ORDER BY o_no DESC";
		rs = st.executeQuery(query);

		for(int k=0; k<(current_page_num -1 )*l_maxlist; k++){
			recNum--;
			rs.next();
		}
		
		for(int k=0; k<l_maxlist; k++){
			if(!rs.next()) {break;}
			ewHistory = new ExtraWorkHistoryTable();

			ewHistory.setOno(rs.getInt("o_no"));
			ewHistory.setMemberId(rs.getString("member_id"));
			ewHistory.setMemberName(rs.getString("member_name"));
			ewHistory.setMemberRankName(rs.getString("member_rankname"));
			ewHistory.setDivision(rs.getString("division"));
			ewHistory.setDivisionName(rs.getString("division_name"));
			ewHistory.setWsdate(rs.getString("w_sdate"));
			ewHistory.setWstime(rs.getString("w_stime"));
			ewHistory.setWedate(rs.getString("w_edate"));
			ewHistory.setWetime(rs.getString("w_etime"));
			ewHistory.setCdate(rs.getString("c_date"));
			ewHistory.setStatus(rs.getString("status"));
			ewHistory.setStatusName(ewBO.getStatname(rs.getString("status")));
			ewHistory.setWtype(ewBO.getStatname(rs.getString("w_type")));
			ewHistory.setRsdate(rs.getString("r_sdate"));
			ewHistory.setRstime(rs.getString("r_stime"));
			ewHistory.setRedate(rs.getString("r_edate"));
			ewHistory.setRetime(rs.getString("r_etime"));
			ewHistory.setTotalTime(rs.getString("total_time"));
			ewHistory.setEwConfirm(rs.getString("ew_confirm"));
			ewHistory.setDuty(rs.getString("duty"));
			ewHistory.setDutyCont(rs.getString("duty_cont"));
			ewHistory.setPayByWork(rs.getString("pay_by_work"));
			
			arry.add(ewHistory);

			recNum--;
		}

		rs.close();
		st.close();

		return arry;
	}

	/*******************************************
	* �μ��� �����Ŵ�� Ư�ٽ�û�� ����Ʈ ��������
	*******************************************/
	public ArrayList getReqList(String mode,String division) throws Exception {
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ExtraWorkHistoryTable extraWorkHistoryTable ;
		ArrayList arry = new ArrayList();
		Statement st = null;
		ResultSet rs=null;

		String query = "SELECT * FROM ew_history WHERE (status='1') AND division LIKE '" + division + "%'";
		st = con.createStatement();
		rs = st.executeQuery(query);
		
		while(rs.next()){
			extraWorkHistoryTable = new ExtraWorkHistoryTable();

			extraWorkHistoryTable.setOno(rs.getInt("o_no"));
			extraWorkHistoryTable.setMemberId(rs.getString("member_id"));
			extraWorkHistoryTable.setMemberName(rs.getString("member_name"));
			extraWorkHistoryTable.setMemberRank(rs.getString("member_rank"));
			extraWorkHistoryTable.setMemberRankName(rs.getString("member_rankname"));
			extraWorkHistoryTable.setDivision(rs.getString("division"));
			extraWorkHistoryTable.setDivisionName(rs.getString("division_name"));
			extraWorkHistoryTable.setPcIp(rs.getString("pc_ip"));
			extraWorkHistoryTable.setWsdate(rs.getString("w_sdate"));
			extraWorkHistoryTable.setWstime(rs.getString("w_stime"));
			extraWorkHistoryTable.setWedate(rs.getString("w_edate"));
			extraWorkHistoryTable.setWetime(rs.getString("w_etime"));
			extraWorkHistoryTable.setCdate(rs.getString("c_date"));
			extraWorkHistoryTable.setStatus(rs.getString("status"));
			extraWorkHistoryTable.setStatusName(ewBO.getStatname(rs.getString("status")));
			extraWorkHistoryTable.setRsdate(rs.getString("r_sdate"));
			extraWorkHistoryTable.setRstime(rs.getString("r_stime"));
			extraWorkHistoryTable.setRedate(rs.getString("r_edate"));
			extraWorkHistoryTable.setRetime(rs.getString("r_etime"));
			extraWorkHistoryTable.setTotalTime(rs.getString("total_time"));
			extraWorkHistoryTable.setEwConfirm(rs.getString("ew_confirm"));
			extraWorkHistoryTable.setDuty(rs.getString("duty"));
			extraWorkHistoryTable.setDutyCont(rs.getString("duty_cont"));
			extraWorkHistoryTable.setPayByWork(rs.getString("pay_by_work"));
			
			arry.add(extraWorkHistoryTable);
		}

		rs.close();
		st.close();
			
		return arry;
	}

	/*********************************
	 * Ư�ٽ�û����� ����Ʈ ��������
	 *********************************/
	public ArrayList getWorkerList(String ono_plus) throws Exception {
		Statement st=null;
		ResultSet rs=null;

		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ExtraWorkHistoryTable extraWorkHistoryTable;
		ArrayList arry = new ArrayList();		

		st = con.createStatement();
		
		java.util.StringTokenizer stokens = new java.util.StringTokenizer(ono_plus,";");
		int tokens_count = stokens.countTokens();

		for (int j = 0 ; j<tokens_count ; j++)
		{
			String token = stokens.nextToken();	
			
			String query = "SELECT * FROM ew_history WHERE o_no='"+token+"'";
			rs = st.executeQuery(query);
			if(rs.next()) {
				extraWorkHistoryTable = new ExtraWorkHistoryTable();
				extraWorkHistoryTable.setOno(rs.getInt("o_no"));
				extraWorkHistoryTable.setMemberId(rs.getString("member_id"));
				extraWorkHistoryTable.setMemberName(rs.getString("member_name"));
				extraWorkHistoryTable.setMemberRank(rs.getString("member_rank"));
				extraWorkHistoryTable.setMemberRankName(rs.getString("member_rankname"));
				extraWorkHistoryTable.setDivision(rs.getString("division"));
				extraWorkHistoryTable.setDivisionName(rs.getString("division_name"));
				extraWorkHistoryTable.setPcIp(rs.getString("pc_ip"));
				extraWorkHistoryTable.setWsdate(rs.getString("w_sdate"));
				extraWorkHistoryTable.setWstime(rs.getString("w_stime"));
				extraWorkHistoryTable.setWedate(rs.getString("w_edate"));
				extraWorkHistoryTable.setWetime(rs.getString("w_etime"));
				extraWorkHistoryTable.setCdate(rs.getString("c_date"));
				extraWorkHistoryTable.setStatus(rs.getString("status"));
				extraWorkHistoryTable.setStatusName(ewBO.getStatname(rs.getString("status")));
				extraWorkHistoryTable.setRsdate(rs.getString("r_sdate"));
				extraWorkHistoryTable.setRstime(rs.getString("r_stime"));
				extraWorkHistoryTable.setRedate(rs.getString("r_edate"));
				extraWorkHistoryTable.setRetime(rs.getString("r_etime"));
				extraWorkHistoryTable.setTotalTime(rs.getString("total_time"));
				extraWorkHistoryTable.setEwConfirm(rs.getString("ew_confirm"));
				extraWorkHistoryTable.setDuty(rs.getString("duty"));
				extraWorkHistoryTable.setDutyCont(rs.getString("duty_cont"));
				extraWorkHistoryTable.setPayByWork(rs.getString("pay_by_work"));
				
				arry.add(extraWorkHistoryTable);
			}
		}

		st.close();
		rs.close();

		return arry;
	}

	/*******************************************
	 * ������� �����.
	 *******************************************/
	public void saveEwReqInfo(String mno,String login_id,String worker) throws Exception {
		PreparedStatement pstm = null;

		//����� ���� �������� 
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
		user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(login_id);
		String member_name = user_info.getUserName();
		String member_rankname = user_info.getUserRank();
		String division = user_info.getCode();
	    String division_name = user_info.getDivision();

		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String w_time = vans.format(now);


		String query = "INSERT INTO ew_master (mno,user_id,user_name,user_rank,ac_code,ac_name,w_date,status,worker) VALUES  (?,?,?,?,?,?,?,?,?)";
		pstm = con.prepareStatement(query);
		
		pstm.setString(1,mno);
		pstm.setString(2,login_id);
		pstm.setString(3,member_name);
		pstm.setString(4,member_rankname);
		pstm.setString(5,division);
		pstm.setString(6,division_name);
		pstm.setString(7,w_time);
		pstm.setString(8,"1");
		pstm.setString(9,worker);
		
		pstm.executeUpdate();
		pstm.close();
	}

	/****************************
	 * ew_master ���̺��� ������ȣ�� ���� Ư�ٴ����� ������ȣ ����Ʈ �����´�.
     ****************************/
	public String getOnoPlus(String mno) throws Exception {
		Statement st = con.createStatement();
		String query = "SELECT worker FROM ew_master WHERE mno= '" + mno + "'";
		ResultSet rs = st.executeQuery(query);		

		String ono_plus = "";		
		if(rs.next()) ono_plus = rs.getString("worker");
		
		st.close(); 
		rs.close();
		
		return ono_plus;
	}

	/*******************************************
	 * ew_historm ���̺��� ��������ڵ� ������Ʈ
     *******************************************/
	public void updateStatus(String ono_plus, String status,String aid) throws Exception {
		Statement st = con.createStatement();
		String query = "";
		String token = "";

		java.util.StringTokenizer stokens = new java.util.StringTokenizer(ono_plus,";");
		int tokens_count = stokens.countTokens();
	
		for (int j = 0 ; j<tokens_count ; j++){			
			token = stokens.nextToken();
			query = "UPDATE ew_history SET status='"+status+"',aid='" + aid + "' WHERE o_no='"+token+"'";
			st.executeUpdate(query);
		}
		st.close();
	}

	/*******************************************
	* ��� ó���� ew_master ���̺� ���ڰ��� ������ȣ�� �Է��ϰ�,�����ڵ带 
	********************************************/
	public void setEwid(String ewid,String mno,String status) throws Exception {
		Statement stmt = con.createStatement();
		
		String query = "UPDATE ew_master SET ew_id='"+ewid+"', status='"+status+"' WHERE mno='"+mno+"'";
		stmt.executeUpdate(query);
		
		stmt.close();
	}

	/******************************************
	* �μ��� Ư�ټ��� ������ ����Ʈ ��������
	*******************************************/
	public ArrayList getPewInfoInDivision2(String mode,String division) throws Exception {
		ExtraWorkHistoryTable extraWorkHistoryTable;
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ArrayList arry = new ArrayList();

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT * FROM ew_history WHERE cal_confirm = 'n' AND ew_confirm = '6' AND status = '3' AND division LIKE '" + division + "%'";
		rs = stmt.executeQuery(query);

		while(rs.next()){
			extraWorkHistoryTable = new ExtraWorkHistoryTable();

			extraWorkHistoryTable.setOno(rs.getInt("o_no"));
			extraWorkHistoryTable.setMemberId(rs.getString("member_id"));
			extraWorkHistoryTable.setMemberName(rs.getString("member_name"));
			extraWorkHistoryTable.setMemberRankName(rs.getString("member_rankname"));
			extraWorkHistoryTable.setDivision(rs.getString("division"));
			extraWorkHistoryTable.setDivisionName(rs.getString("division_name"));
			extraWorkHistoryTable.setWsdate(rs.getString("w_sdate"));
			extraWorkHistoryTable.setWstime(rs.getString("w_stime"));
			extraWorkHistoryTable.setWedate(rs.getString("w_edate"));
			extraWorkHistoryTable.setWetime(rs.getString("w_etime"));
			extraWorkHistoryTable.setCdate(rs.getString("c_date"));
			extraWorkHistoryTable.setStatus(rs.getString("status"));
			extraWorkHistoryTable.setStatusName(ewBO.getStatname(rs.getString("status")));
			extraWorkHistoryTable.setCalConfirm(rs.getString("cal_confirm"));
			extraWorkHistoryTable.setRsdate(rs.getString("r_sdate"));
			extraWorkHistoryTable.setRstime(rs.getString("r_stime"));
			extraWorkHistoryTable.setRedate(rs.getString("r_edate"));
			extraWorkHistoryTable.setRetime(rs.getString("r_etime"));
			extraWorkHistoryTable.setTotalTime(rs.getString("total_time"));
			extraWorkHistoryTable.setResultTime(rs.getString("result_time"));
			extraWorkHistoryTable.setEwConfirm(rs.getString("ew_confirm"));
			extraWorkHistoryTable.setDuty(rs.getString("duty"));
			extraWorkHistoryTable.setDutyCont(rs.getString("duty_cont"));
			extraWorkHistoryTable.setPayByWork(rs.getString("pay_by_work"));

			arry.add(extraWorkHistoryTable);
			
		}
		stmt.close();
		rs.close();

		return arry;
	}

	/***************************
	 * Ư�� ��û���� ��������(by ������ȣ)
	 ***************************/
	public ExtraWorkHistoryTable getHistoryInfo(String o_no) throws Exception {
		ExtraWorkHistoryTable extraWorkHistoryTable = new ExtraWorkHistoryTable();
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		Statement st=null;
		ResultSet rs=null;
		
		String query = "SELECT * FROM ew_history WHERE o_no='"+o_no+"'";
		st = con.createStatement();
		rs = st.executeQuery(query);
		if(rs.next()) {
			extraWorkHistoryTable.setOno(rs.getInt("o_no"));
			extraWorkHistoryTable.setMemberId(rs.getString("member_id"));
			extraWorkHistoryTable.setMemberName(rs.getString("member_name"));
			extraWorkHistoryTable.setMemberRank(rs.getString("member_rank"));
			extraWorkHistoryTable.setMemberRankName(rs.getString("member_rankname"));
			extraWorkHistoryTable.setDivision(rs.getString("division"));
			extraWorkHistoryTable.setDivisionName(rs.getString("division_name"));
			extraWorkHistoryTable.setPcIp(rs.getString("pc_ip"));
			extraWorkHistoryTable.setWsdate(rs.getString("w_sdate"));
			extraWorkHistoryTable.setWstime(rs.getString("w_stime"));
			extraWorkHistoryTable.setWedate(rs.getString("w_edate"));
			extraWorkHistoryTable.setWetime(rs.getString("w_etime"));
			extraWorkHistoryTable.setCdate(rs.getString("c_date"));
			extraWorkHistoryTable.setStatus(rs.getString("status"));
			extraWorkHistoryTable.setStatusName(ewBO.getStatname(rs.getString("status")));
			extraWorkHistoryTable.setRsdate(rs.getString("r_sdate"));
			extraWorkHistoryTable.setRstime(rs.getString("r_stime"));
			extraWorkHistoryTable.setRedate(rs.getString("r_edate"));
			extraWorkHistoryTable.setRetime(rs.getString("r_etime"));
			extraWorkHistoryTable.setTotalTime(rs.getString("total_time"));
			extraWorkHistoryTable.setResultTime(rs.getString("result_time"));
			extraWorkHistoryTable.setEwConfirm(rs.getString("ew_confirm"));
			extraWorkHistoryTable.setDuty(rs.getString("duty"));
			extraWorkHistoryTable.setDutyCont(rs.getString("duty_cont"));
			extraWorkHistoryTable.setPayByWork(rs.getString("pay_by_work"));
			extraWorkHistoryTable.setAid(rs.getString("aid"));
			extraWorkHistoryTable.setWtype(rs.getString("w_type"));
		}
		st.close();
		rs.close();

		return extraWorkHistoryTable;
	}

	/***************************
	 * Ư�� ��û���� �������� (by ��� �� ��������)
	 ***************************/
	public ExtraWorkHistoryTable getHistoryInfo(String member_id,String w_sdate) throws Exception {
		ExtraWorkHistoryTable extraWorkHistoryTable = new ExtraWorkHistoryTable();
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		Statement st=null;
		ResultSet rs=null;
		
		String query = "SELECT * FROM ew_history WHERE member_id = '" + member_id +"' AND w_sdate = '" + w_sdate + "'";
		st = con.createStatement();
		rs = st.executeQuery(query);
		if(rs.next()) {
			extraWorkHistoryTable.setOno(rs.getInt("o_no"));
			extraWorkHistoryTable.setMemberId(rs.getString("member_id"));
			extraWorkHistoryTable.setMemberName(rs.getString("member_name"));
			extraWorkHistoryTable.setMemberRank(rs.getString("member_rank"));
			extraWorkHistoryTable.setMemberRankName(rs.getString("member_rankname"));
			extraWorkHistoryTable.setDivision(rs.getString("division"));
			extraWorkHistoryTable.setDivisionName(rs.getString("division_name"));
			extraWorkHistoryTable.setPcIp(rs.getString("pc_ip"));
			extraWorkHistoryTable.setWsdate(rs.getString("w_sdate"));
			extraWorkHistoryTable.setWstime(rs.getString("w_stime"));
			extraWorkHistoryTable.setWedate(rs.getString("w_edate"));
			extraWorkHistoryTable.setWetime(rs.getString("w_etime"));
			extraWorkHistoryTable.setCdate(rs.getString("c_date"));
			extraWorkHistoryTable.setStatus(rs.getString("status"));
			extraWorkHistoryTable.setStatusName(ewBO.getStatname(rs.getString("status")));
			extraWorkHistoryTable.setRsdate(rs.getString("r_sdate"));
			extraWorkHistoryTable.setRstime(rs.getString("r_stime"));
			extraWorkHistoryTable.setRedate(rs.getString("r_edate"));
			extraWorkHistoryTable.setRetime(rs.getString("r_etime"));
			extraWorkHistoryTable.setTotalTime(rs.getString("total_time"));
			extraWorkHistoryTable.setResultTime(rs.getString("result_time"));
			extraWorkHistoryTable.setEwConfirm(rs.getString("ew_confirm"));
			extraWorkHistoryTable.setDuty(rs.getString("duty"));
			extraWorkHistoryTable.setDutyCont(rs.getString("duty_cont"));
			extraWorkHistoryTable.setPayByWork(rs.getString("pay_by_work"));
			extraWorkHistoryTable.setAid(rs.getString("aid"));
		}
		st.close();
		rs.close();

		return extraWorkHistoryTable;
	}

	/***************************
	 * ����� �⵵�� �������� ��������
	 ***************************/
	public MemberPayInfoTable getMemberPayInfo(String id,String year) throws Exception {
		MemberPayInfoTable memberTable = new MemberPayInfoTable();
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		Statement st=null;
		ResultSet rs=null;
		st = con.createStatement();

		String query = "SELECT * FROM member_payinfo WHERE id ='" + id + "' AND this_year = '" + year + "'";
		rs = st.executeQuery(query);

		if(rs.next()) {
			memberTable.setPno(rs.getInt("p_no"));
			memberTable.setId(rs.getString("id"));
			memberTable.setEmpType(rs.getString("emp_type"));
			memberTable.setEmpName(ewBO.getStatname(rs.getString("emp_type")));
			memberTable.setSalaryKind(rs.getString("salary_kind"));
			memberTable.setYearlyPay(rs.getString("yearly_pay"));
			memberTable.setMonthlyPay(rs.getString("monthly_pay"));
			memberTable.setHourlyPay(rs.getString("hourly_pay"));
			memberTable.setBasicPay(rs.getString("basic_pay"));
		}
		rs.close();
		st.close();

		return memberTable;
	}

	/***************************
	 * ����ó����� ������Ʈ
	 ***************************/
	public void updateJungsanResult(String no,String hourly_pay,String pay_by_work,String login_id,String login_name,String w_time,String result_time) throws Exception {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		String query= "";

		query = "UPDATE ew_history SET salary=?,cal_confirm=?,pay_by_work=?,checkman=?,confirm_date=?,result_time=? WHERE o_no='"+no+"'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,hourly_pay);
		pstmt.setString(2,"y");
		pstmt.setString(3,pay_by_work);
		pstmt.setString(4,login_id + "/" + login_name);
		pstmt.setString(5,w_time);
		pstmt.setString(6,result_time);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/******************************************
	 * �μ���,���� ����ó����� ����Ʈ ��������
	 ******************************************/
	public ArrayList getJungSanResultList(String mode,String division,String year,String month) throws Exception {
		ExtraWorkHistoryTable extraWorkHistoryTable;
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ArrayList arry = new ArrayList();

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT * FROM ew_history WHERE cal_confirm = 'y' AND ew_confirm = '6' AND status = '3' AND w_sdate LIKE '" + year + month + "%' AND division LIKE '" + division + "%' ORDER BY o_no ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()){
			extraWorkHistoryTable = new ExtraWorkHistoryTable();

			extraWorkHistoryTable.setOno(rs.getInt("o_no"));
			extraWorkHistoryTable.setMemberId(rs.getString("member_id"));
			extraWorkHistoryTable.setMemberName(rs.getString("member_name"));
			extraWorkHistoryTable.setMemberRankName(rs.getString("member_rankname"));
			extraWorkHistoryTable.setDivision(rs.getString("division"));
			extraWorkHistoryTable.setDivisionName(rs.getString("division_name"));
			extraWorkHistoryTable.setWsdate(rs.getString("w_sdate"));
			extraWorkHistoryTable.setWstime(rs.getString("w_stime"));
			extraWorkHistoryTable.setWedate(rs.getString("w_edate"));
			extraWorkHistoryTable.setWetime(rs.getString("w_etime"));
			extraWorkHistoryTable.setCdate(rs.getString("c_date"));
			extraWorkHistoryTable.setStatus(rs.getString("status"));
			extraWorkHistoryTable.setStatusName(ewBO.getStatname(rs.getString("status")));
			extraWorkHistoryTable.setCalConfirm(rs.getString("cal_confirm"));
			extraWorkHistoryTable.setRsdate(rs.getString("r_sdate"));
			extraWorkHistoryTable.setRstime(rs.getString("r_stime"));
			extraWorkHistoryTable.setRedate(rs.getString("r_edate"));
			extraWorkHistoryTable.setRetime(rs.getString("r_etime"));
			extraWorkHistoryTable.setTotalTime(rs.getString("total_time"));
			extraWorkHistoryTable.setResultTime(rs.getString("result_time"));
			extraWorkHistoryTable.setEwConfirm(rs.getString("ew_confirm"));
			extraWorkHistoryTable.setDuty(rs.getString("duty"));
			extraWorkHistoryTable.setDutyCont(rs.getString("duty_cont"));
			extraWorkHistoryTable.setPayByWork(rs.getString("pay_by_work"));
			extraWorkHistoryTable.setConfirmDate(rs.getString("confirm_date"));

			arry.add(extraWorkHistoryTable);
			
		}
		stmt.close();
		rs.close();

		return arry;
	}

	/******************************************
	 * �Ϻ� Ư�ٴ���� ����Ʈ ��������
	 ******************************************/
	public ArrayList getEwListByDay(String mode,String division,String year,String month,String day) throws Exception {
		ExtraWorkHistoryTable extraWorkHistoryTable;
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ArrayList arry = new ArrayList();

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT * FROM ew_history WHERE status = '3' AND w_sdate LIKE '" + year + month + day + "%' AND division LIKE '" + division + "%' ORDER BY w_sdate DESC";
		rs = stmt.executeQuery(query);

		while(rs.next()){
			extraWorkHistoryTable = new ExtraWorkHistoryTable();

			extraWorkHistoryTable.setOno(rs.getInt("o_no"));
			extraWorkHistoryTable.setMemberId(rs.getString("member_id"));
			extraWorkHistoryTable.setMemberName(rs.getString("member_name"));
			extraWorkHistoryTable.setMemberRankName(rs.getString("member_rankname"));
			extraWorkHistoryTable.setDivision(rs.getString("division"));
			extraWorkHistoryTable.setDivisionName(rs.getString("division_name"));
			extraWorkHistoryTable.setWsdate(rs.getString("w_sdate"));
			extraWorkHistoryTable.setWstime(rs.getString("w_stime"));
			extraWorkHistoryTable.setWedate(rs.getString("w_edate"));
			extraWorkHistoryTable.setWetime(rs.getString("w_etime"));
			extraWorkHistoryTable.setCdate(rs.getString("c_date"));
			extraWorkHistoryTable.setStatus(rs.getString("status"));
			extraWorkHistoryTable.setStatusName(ewBO.getStatname(rs.getString("status")));
			extraWorkHistoryTable.setCalConfirm(rs.getString("cal_confirm"));
			extraWorkHistoryTable.setRsdate(rs.getString("r_sdate"));
			extraWorkHistoryTable.setRstime(rs.getString("r_stime"));
			extraWorkHistoryTable.setRedate(rs.getString("r_edate"));
			extraWorkHistoryTable.setRetime(rs.getString("r_etime"));
			extraWorkHistoryTable.setTotalTime(rs.getString("total_time"));
			extraWorkHistoryTable.setResultTime(rs.getString("result_time"));
			extraWorkHistoryTable.setEwConfirm(rs.getString("ew_confirm"));
			extraWorkHistoryTable.setDuty(rs.getString("duty"));
			extraWorkHistoryTable.setDutyCont(rs.getString("duty_cont"));
			extraWorkHistoryTable.setPayByWork(rs.getString("pay_by_work"));
			extraWorkHistoryTable.setConfirmDate(rs.getString("confirm_date"));

			arry.add(extraWorkHistoryTable);
			
		}
		stmt.close();
		rs.close();

		return arry;
	}

	/**************************************
	* ���� ���� Ư����Ȳ ����Ʈ�� �����´�.
	***************************************/
	public ArrayList getTableListByPerson(String year,String month,String login_id) throws Exception {

		Statement		stmt= null;
		ResultSet		rs	= null;
		DecimalFormat	fmt	= new DecimalFormat("00");
		
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ExtraWorkHistoryTable extraWorkHistoryTable;
				
		ArrayList table_list = new ArrayList();

		// ���� Ư����Ȳ �����´�.
		String query = "SELECT * FROM ew_history WHERE member_id='"+login_id+"' AND status='3' AND w_sdate LIKE '" + year + month +"%'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			extraWorkHistoryTable = new ExtraWorkHistoryTable();

			extraWorkHistoryTable.setOno(rs.getInt("o_no"));
			extraWorkHistoryTable.setMemberId(rs.getString("member_id"));
			extraWorkHistoryTable.setMemberName(rs.getString("member_name"));
			extraWorkHistoryTable.setMemberRankName(rs.getString("member_rankname"));
			extraWorkHistoryTable.setDivision(rs.getString("division"));
			extraWorkHistoryTable.setDivisionName(rs.getString("division_name"));
			extraWorkHistoryTable.setWsdate(rs.getString("w_sdate"));
			extraWorkHistoryTable.setWstime(rs.getString("w_stime"));
			extraWorkHistoryTable.setWedate(rs.getString("w_edate"));
			extraWorkHistoryTable.setWetime(rs.getString("w_etime"));
			extraWorkHistoryTable.setCdate(rs.getString("c_date"));
			extraWorkHistoryTable.setStatus(rs.getString("status"));
			extraWorkHistoryTable.setStatusName(ewBO.getStatname(rs.getString("status")));
			extraWorkHistoryTable.setCalConfirm(rs.getString("cal_confirm"));
			extraWorkHistoryTable.setRsdate(rs.getString("r_sdate"));
			extraWorkHistoryTable.setRstime(rs.getString("r_stime"));
			extraWorkHistoryTable.setRedate(rs.getString("r_edate"));
			extraWorkHistoryTable.setRetime(rs.getString("r_etime"));
			extraWorkHistoryTable.setTotalTime(rs.getString("total_time"));
			extraWorkHistoryTable.setResultTime(rs.getString("result_time"));
			extraWorkHistoryTable.setEwConfirm(rs.getString("ew_confirm"));
			extraWorkHistoryTable.setDuty(rs.getString("duty"));
			extraWorkHistoryTable.setDutyCont(rs.getString("duty_cont"));
			extraWorkHistoryTable.setPayByWork(rs.getString("pay_by_work"));
			extraWorkHistoryTable.setConfirmDate(rs.getString("confirm_date"));

			table_list.add(extraWorkHistoryTable);
		}
		stmt.close();
		rs.close();
		return table_list;
	}

	/******************************************
	 * ������ ��¥�� ���ε� Ư�ٽ�û���� �ִ��� Ȯ��
	 ******************************************/
	public boolean checkWorked(String id, String wdate) throws Exception {
		
		boolean bool = false;
		Statement st = null;
		ResultSet rs = null;
		st = con.createStatement();

		String query = "SELECT count(o_no) FROM ew_history WHERE member_id='"+id+"' AND w_sdate='"+wdate+"' AND status='3'";
		rs = st.executeQuery(query);
		int i=0;
		if(rs.next()) i = rs.getInt(1);

		if(i>0) bool = true;

		rs.close();
		st.close();
		
		return bool;
	}

	/***************************
	 * ���ó���ÿ� ew_history ���̺��� ������Ʈ
	 ***************************/
	public void updateHistoryInfo(String mid,String r_edate,String r_etime,String ew_confirm,String total_time) throws Exception {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		String query= "";

		query = "UPDATE ew_history SET r_edate=?,r_etime=?,ew_confirm=?,total_time=? WHERE o_no='"+mid+"'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,r_edate);
		pstmt.setString(2,r_etime);
		pstmt.setString(3,ew_confirm);
		pstmt.setString(4,total_time);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/***************************
	 * Ư�ٽ�û�� ���� ó��
	 ***************************/
	public void processEWDel(String o_no) throws Exception {
		Statement stmt = con.createStatement();
		String query = "DELETE ew_history WHERE o_no='"+o_no+"'";
		stmt.executeUpdate(query);
		stmt.close();
	}

}