package com.anbtech.es.geuntae.db;
import com.anbtech.es.geuntae.business.HyuGaDayBO;
import com.anbtech.es.geuntae.entity.*;
import com.anbtech.date.anbDate;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.admin.entity.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.lang.Double;

public class HyuGaDayDAO{
	private Connection con;
	private com.anbtech.date.anbDate anbdt;
	private com.anbtech.es.geuntae.business.HyuGaDayBO hdBO;
	private com.anbtech.dbconn.DBConnectionManager connMgr;	
	private com.anbtech.admin.db.UserInfoDAO userDAO;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public HyuGaDayDAO(Connection con){
		this.con = con;
		anbdt = new com.anbtech.date.anbDate();
		hdBO = new com.anbtech.es.geuntae.business.HyuGaDayBO();
		userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
	}

	public HyuGaDayDAO() 
	{	
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
		anbdt = new com.anbtech.date.anbDate();
		hdBO = new com.anbtech.es.geuntae.business.HyuGaDayBO();
	}	

	/*******************************************************************
	 * 년도 column name찾기
	 *******************************************************************/
	public String searchColumn(String month) 
	{
		String column = "";
		if(month.equals("01")) column = "jan1";
		else if (month.equals("02")) column = "feb2";
		else if (month.equals("03")) column = "mar3";
		else if (month.equals("04")) column = "apr4";
		else if (month.equals("05")) column = "may5";
		else if (month.equals("06")) column = "jun6";
		else if (month.equals("07")) column = "jul7";
		else if (month.equals("08")) column = "aug8";
		else if (month.equals("09")) column = "sep9";
		else if (month.equals("10")) column = "oct10";
		else if (month.equals("11")) column = "nov11";
		else if (month.equals("12")) column = "dec12";
		return column;
	}

	/*******************************************************************
	 * 해당자의 년도/근태종류별 데이터 존재여부 판단
	 *******************************************************************/
	public boolean isEmpty(String ys_kind, String hd_var, String user_id, String ac_code, String year) throws Exception
	{
		boolean tag = true;
		Statement stmt = null;
		ResultSet rs = null;
		int total_count = 0;

		String query = "SELECT COUNT(*) FROM geuntae_count where (ys_kind='"+ys_kind+"') and ";
			query += "(hd_var = '"+hd_var+"') and (user_id = '"+user_id+"') and ";
			query += "(ac_code = '"+ac_code+"') and (thisyear='"+year+"')";
		//System.out.println("isEmpty q : " + query);
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		total_count = Integer.parseInt(rs.getString(1));
		stmt.close();
		rs.close();

		if(total_count == 0) tag = true;
		else tag = false;
		return tag;
	}
	
	/*******************************************************************
	 * 해당자의 년도/근태종류별 데이터 존재시 관리코드,현재수량 파악하기
	 *******************************************************************/
	public String[] getGTCount(String ys_kind, String hd_var, String user_id, String ac_code, String year, String month) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String[] data = new String[2];	//관리코드, 현재수량
		String month_column = searchColumn(month);

		String query = "SELECT gt_id,"+month_column+" FROM geuntae_count where (ys_kind='"+ys_kind+"') and ";
		query += "(hd_var = '"+hd_var+"') and (user_id = '"+user_id+"') and ";
		query += "(ac_code = '"+ac_code+"') and (thisyear='"+year+"')";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data[0] = rs.getString("gt_id");
			data[1] = rs.getString(month_column);
		}
		if(data[1] == null) data[1] = "0";
		if(data[1].length() == 0) data[1] = "0";
	
		stmt.close();
		rs.close();

		return data;
	}

	/*****************************************************************************
	 * 신규 등록하기(geuntae_count 테이블)
	 *****************************************************************************/
	 public void insertCountTable(String gt_id,String ys_kind,String hd_var,String user_id,String user_name,String user_code,
		String user_rank,String div_code,String code,String div_name,String this_year,String month_column,String count) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO GEUNTAE_COUNT(gt_id,ys_kind,hd_var,user_id,user_name,user_code,";
			incommon += "user_rank,ac_code,code,ac_name,thisyear,"+month_column+") values('";
		String input = "";
		
		input = incommon+gt_id+"','"+ys_kind+"','"+hd_var+"','"+user_id+"','"+user_name+"','"+user_code+"','";
		input += user_rank+"','"+div_code+"','"+code+"','"+div_name+"','"+this_year+"','"+count+"')";

		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*****************************************************************************
	 * 수정 등록하기(geuntae_count 테이블)
	 *****************************************************************************/
	 public void updateCountTable(String gt_id,String month_column,String count) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "UPDATE GEUNTAE_COUNT SET "+month_column+"='"+count+"' WHERE ";
			update += "gt_id='"+gt_id+"'";
		
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}
	
	/*****************************************************************************
	 * 사용자별 근태종류별 데이터 처리하기
	 *****************************************************************************/
	 public void processCount(String doc_lid) throws Exception
	{
		String gt_id,ys_kind,hd_var;
		String user_id,user_name,ac_id,user_code,user_rank,code;
		String div_name,div_code;
		String u_year,u_month,u_date;
		String tu_year,tu_month,tu_date;
		String input1,input2,data;
		//초기화 하기
		gt_id=ys_kind=hd_var=user_id=user_name=user_code=user_rank=ac_id=code="";
		div_name=div_code=u_year=u_month=u_date=tu_year=tu_month=tu_date="";
		input1=input2=data="";

		//------------------------------------
		// geuntae_master에서 관련내용 구하기
		//------------------------------------
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT gt_id,ys_kind,hd_var,user_id,user_name,user_code,user_rank,";
			query += "ac_id,ac_code,ac_name,u_year,u_month,u_date,tu_year,tu_month,tu_date FROM ";
			query += "geuntae_master where gt_id='"+doc_lid+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			gt_id = rs.getString("gt_id");
			ys_kind = rs.getString("ys_kind");
			hd_var = rs.getString("hd_var");
			user_id = rs.getString("user_id");
			user_name = rs.getString("user_name");
			user_code = rs.getString("user_code");
			user_rank = rs.getString("user_rank");
			ac_id = rs.getString("ac_id");
			div_code = rs.getString("ac_code");
			div_name = rs.getString("ac_name");
			u_year = rs.getString("u_year");
			u_month = rs.getString("u_month");
			u_date = rs.getString("u_date");
			tu_year = rs.getString("tu_year");
			tu_month = rs.getString("tu_month");
			tu_date = rs.getString("tu_date");

			//휴일일수 구하기
			input1 = u_year + u_month + u_date;
			input2 = tu_year + tu_month + tu_date;
			data = hdBO.getCount(input1,input2);			//등록할 해당년월별 수량가져오기(200308,24|200309,12| ...) 
		}
		stmt.close();
		rs.close();

		//------------------------------------
		//	부서 관리코드을 이용 내부관리 코드 구하기
		//------------------------------------
		code = userDAO.getCode(ac_id);

		//------------------------------------
		//휴일 일수를 년도/월별 배열에 담기
		//------------------------------------
		StringTokenizer bar_cnt = new StringTokenizer(data,"|");
		int tot_cnt = bar_cnt.countTokens();
		String[][] list = new String[tot_cnt][3];
		int i = 0;
		while(bar_cnt.hasMoreTokens()) {
			String ea_ym = bar_cnt.nextToken();
			StringTokenizer ea_data = new StringTokenizer(ea_ym,",");
			int ea = 0;
			while(ea_data.hasMoreTokens()) {
				if(ea == 0) {
					String ym = ea_data.nextToken();
					list[i][0] = ym.substring(0,4);						//년도
					list[i][1] = ym.substring(4,6);						//월
				} else if(ea == 1) list[i][2] = ea_data.nextToken();	//수량
				ea++;
			}
			i++;
		}

		//------------------------------------
		//사용자/근태별 등록하기
		//------------------------------------
		for(int n=0; n<i; n++) {
			String month_col_name = searchColumn(list[n][1]);		//해당월의 컬럼명
			//신규 등록하기
			if(isEmpty(ys_kind,hd_var,user_id,div_code,list[n][0])) {
				insertCountTable(gt_id,ys_kind,hd_var,user_id,user_name,user_code,user_rank,div_code,code,div_name,list[n][0],month_col_name,list[n][2]);
			} 
			//수정 하기 (update)
			else {
				String[] idCnt = new String[2];			//관리코드,해당수량
				idCnt = getGTCount(ys_kind,hd_var,user_id,div_code,list[n][0],list[n][1]);

				double t_cnt = Double.parseDouble(idCnt[1]) + Double.parseDouble(list[n][2]);//기존+신규
				String u_cnt = Double.toString(t_cnt);
				updateCountTable(idCnt[0],month_col_name,u_cnt);
			}
		} //for

		//con 닫기
		//close();
	}

	/*****************************************************************************
	 * 총 근태 일수 구하기
	 *****************************************************************************/
	 public double getAACount(String sy,String sm,String sd,String ey,String em,String ed) throws Exception
	{
		//휴일일수 구하기
		String input1 = sy + sm + sd;
		String input2 = ey + em + ed;
		String data = hdBO.getCount(input1,input2);			//등록할 해당년월별 수량가져오기(200308,24|200309,12| ...) 
		
		double total_cnt = 0.0;
		StringTokenizer bar_cnt = new StringTokenizer(data,"|");
		while(bar_cnt.hasMoreTokens()) {
			String ea_ym = bar_cnt.nextToken();
			StringTokenizer ea_data = new StringTokenizer(ea_ym,",");
			int ea = 0;
			while(ea_data.hasMoreTokens()) {
				if(ea == 0) {
					ea_data.nextToken();
				} else if(ea == 1) total_cnt += Double.parseDouble(ea_data.nextToken());	//수량
				ea++;
			}
		}
		return total_cnt;
	}


	/*****************************************************************************
	 * 년도별,근태구분별,부서별 사용자 휴가잔량을 계산하여 출력한다.
	 * 해당 데이터가 없으면 출력과 동시에 db에 저장한다.
	 * year:해당년도, kind:휴가종류, code:부서코드(class_table의 code 필드값)
	 *****************************************************************************/
	 public ArrayList getUserHyuGaRestDay(String year,String kind,String code) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";

		if(kind == null || kind.equals("")) kind = "HD_003"; // 디폴트로 월차로 세팅한다.

		com.anbtech.es.geuntae.business.HyuGaDayBO hyugadayBO = new com.anbtech.es.geuntae.business.HyuGaDayBO(con);
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable table = new com.anbtech.admin.entity.UserInfoTable();

		ArrayList table_view = new ArrayList();
		ArrayList user_list = new ArrayList();
		
		// user_table 에서 사용자 리스트를 가져온다.
		user_list = (ArrayList)userinfoDAO.getUserListByInnerCode(code);

		Iterator user_iter = user_list.iterator();

		// 각각의 사용자 ID로 geuntae_hd_max 테이블을 검색하여 해당 데이터가 있으면 선택해서 세팅하고,
		// 없으면 계산하여 db에 저장한 다음 세팅하여 리턴한다.
		while(user_iter.hasNext()){
			table = (UserInfoTable)user_iter.next();
			
			String id = table.getUserId();			// 사용자 사번을 가져온다.
			String enter_day = table.getEnterDay();	// 사용자 입사일을 가져온다.
			int continous_year = hyugadayBO.getContinousYear(enter_day,year); // 근속 년수 계산
			table.setContinuousYear(Integer.toString(continous_year));

			// 해당 사용자 정보가 geuntae_hd_max 테이블에 있는지 체크
			query = "select hdmax from geuntae_hd_max where id = '" + id + "' and byyear = '" + year + "' and kind = '" + kind + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			// 해당 사용자 정보가 있으면 그 내용을 table_view에 저장한다.				
			String rest_day = "";

			if(rs.next()){
				rest_day = rs.getString("hdmax");
				table.setHyuGaRestDay(rest_day);
			}
				// 해당 사용자 정보가 없으면 근속년한, 잔량을 계산하여 db에 저장한 다음 table_view에 저장한다.
			else{

				String previous_year = Integer.toString(Integer.parseInt(year)-1); // 이전 년도 계산
				// 이전년도의 휴가 잔량값을 가져온다.
				query = "select hdmax from geuntae_hd_max where id = '" + id + "' and byyear = '" + previous_year + "' and kind = '" + kind + "'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				double previous_rest_day = 0.0;
				while(rs.next()){
					previous_rest_day = Double.parseDouble(rs.getString("hdmax"));
				}

				// 이전년도에 사용한 휴가량을 가져온다.
				query = "select jan1,feb2,mar3,apr4,may5,jun6,jul7,aug8,sep9,oct10,nov11,dec12 ";
				query += "from geuntae_count where thisyear = '" + previous_year + "' and hd_var = '" + kind + "' and user_id = '" + id + "'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				double used_day = 0.0;
				while(rs.next()){
						used_day = Double.parseDouble(rs.getString("jan1"));
						used_day += Double.parseDouble(rs.getString("feb2"));
						used_day += Double.parseDouble(rs.getString("mar3"));
						used_day += Double.parseDouble(rs.getString("apr4"));
						used_day += Double.parseDouble(rs.getString("may5"));
						used_day += Double.parseDouble(rs.getString("jun6"));
						used_day += Double.parseDouble(rs.getString("jul7"));
						used_day += Double.parseDouble(rs.getString("aug8"));
						used_day += Double.parseDouble(rs.getString("sep9"));
						used_day += Double.parseDouble(rs.getString("oct10"));
						used_day += Double.parseDouble(rs.getString("nov11"));
						used_day += Double.parseDouble(rs.getString("dec12"));			
				}

				// 해당년도의 년차 잔량은 = 이전년도 휴가 잔량 + 10(기본) + 근속년수 - 이전년도 휴가사용량
				// 해당년도의 월차 잔량은 = 이전년도 휴가 잔량 + 12(기본) - 이전년도 휴가사용량
				double restday = 0.0;
				if(kind.equals("HD_006")) restday = previous_rest_day + 10.0 + continous_year - used_day; //년차
				else if(kind.equals("HD_003")) restday = previous_rest_day + 12.0 - used_day;	//월차

				//db에 저장한다.
				query = "insert into geuntae_hd_max (id,byyear,hdmax,kind) values('"+id+"','"+year+"','"+restday+"','"+kind+"')";
				stmt = con.createStatement();
				stmt.executeUpdate(query);
				table.setHyuGaRestDay(Double.toString(restday));
			}

			table_view.add(table);

		}
		stmt.close();
		rs.close();

		return table_view;
	}

	/***************************************
	 * 개인 월차 및 년차 잔량 계산
	 ***************************************/
	public String getPersonalHoliday(String id, String year,String hd_var,String month) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		
		// 해당년도의 잔량(계획수량)을 가져온다.
		double rest_day		= getHdMax(id,year,hd_var);
		String rest_maxday	= Double.toString(rest_day);

		// 해당년월의 이전달까지 사용된 휴가 일수를 계산한다.
		String query = "SELECT jan1,feb2,mar3,apr4,may5,jun6,jul7,aug8,sep9,oct10,nov11,dec12,user_id,hd_var FROM geuntae_count WHERE thisyear='"+year+"' and hd_var='"+hd_var+"' and user_id='"+id+"'";
		stmt=con.createStatement();
		rs=stmt.executeQuery(query);

		double sum = 0.0;
		if(rs.next()){
			for(int i=1; i < Integer.parseInt(month); i++){
				sum += Double.parseDouble(rs.getString(i));
			}
		}

		// 최종 잔량 =  해당년도 잔량 - 사용량
		rest_day = rest_day - sum;
		String rest = Double.toString(rest_day) + "/" + rest_maxday;

		rs.close();
		stmt.close();
		
		return rest;
	}


	/***************************************************
	 * geuntae_hd_max 테이블에서 hdmax 값을 가져온다.
	 ***************************************************/
	 public double getHdMax(String id, String year, String kind) throws Exception  {
			
		Statement stmt = null;
		ResultSet rs = null;
		double rest=0.0;
			
		String query = "SELECT hdmax FROM geuntae_hd_max WHERE id ='" + id + "' and byyear = '" + year + "' and kind='" + kind + "'";
		stmt = con.createStatement();
		rs=stmt.executeQuery(query);
			
		if(rs.next()) {
			rest = Double.parseDouble(rs.getString("hdmax"));
		} 
		
		rs.close();
		stmt.close();
		
		return rest;
	}

	/*********************************************************************
	 * 소멸자
	 *********************************************************************/
	public void close() throws Exception{
		connMgr.freeConnection("mssql",con);
	}

}
