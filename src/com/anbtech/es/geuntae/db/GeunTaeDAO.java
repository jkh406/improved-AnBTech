package com.anbtech.es.geuntae.db;

import com.anbtech.es.geuntae.business.GeunTaeBO;
import com.anbtech.es.geuntae.entity.*;
import com.anbtech.date.anbDate;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class GeunTaeDAO{
	private Connection con;
	private com.anbtech.date.anbDate anbdt;
	private com.anbtech.es.geuntae.business.GeunTaeBO geuntaeBO;
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public GeunTaeDAO(Connection con){
		this.con = con;
		anbdt = new com.anbtech.date.anbDate();
		geuntaeBO = new com.anbtech.es.geuntae.business.GeunTaeBO(con);
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
	 * 검색 조건에 맞는 geuntae_master 테이블 리스트을 가져온다.
	 *****************************************************************************/
	public ArrayList getTableList(String tablename,String mode,String searchword,String searchscope,String category,String page,String login_id) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.es.geuntae.entity.GeunTaeTable table = new com.anbtech.es.geuntae.entity.GeunTaeTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 20;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이

		int current_page_num =Integer.parseInt(page);

		com.anbtech.es.geuntae.business.GeunTaeBO geuntaeBO = new com.anbtech.es.geuntae.business.GeunTaeBO(con);
		String where = geuntaeBO.getWhere(mode,searchword, searchscope, category);

		int total = getTotalCount(tablename, where);	// 전체 레코드 갯수
		int recNum = total;

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT gt_id,ys_kind,user_id,user_name,user_rank,ac_id,ac_code,ac_name,";
		query += "in_date,u_year,u_month,u_date,tu_year,tu_month,tu_date,gt_purpose,gt_time_per,hd_var,hd_name,fellow_names";
		query += " FROM " + tablename + where + " ORDER BY gt_id DESC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String gt_id = rs.getString("gt_id");
			String ys_kind = rs.getString("ys_kind");
			String user_info = rs.getString("user_id") + "/" + rs.getString("user_name");
			String written_day = rs.getString("in_date");
			String gt_period = rs.getString("u_year") + "년 " + rs.getString("u_month") + "월 " + rs.getString("u_date") + "일 ~ " + rs.getString("tu_year") + "년 " + rs.getString("tu_month") + "월 " + rs.getString("tu_date") + "일";
			String gt_purpose = rs.getString("gt_purpose");
			String hd_var = rs.getString("hd_var");
			String gt_time = rs.getString("u_year") + "년 " + rs.getString("u_month") + "월 " + rs.getString("u_date") + "일 " + rs.getString("gt_time_per");
			String hd_name = rs.getString("hd_name");
			// login_id setting
			String user_id = rs.getString("user_id");	
			String fellow_names = rs.getString("fellow_names");

			table = new com.anbtech.es.geuntae.entity.GeunTaeTable();
			table.setGtId(gt_id);
			table.setYsKind(hd_name);
			table.setUserInfo(user_info);
			table.setWrittenDay(written_day);
			// 외출일 경우에는 일자 및 시간을 세팅하고 나머지는 기간을 세팅한다.
			if(hd_var.equals("OT_001")) table.setGtPeriod(gt_time);
			else table.setGtPeriod(gt_period);
			table.setGtPurpose(gt_purpose);
			table.setUserId(user_id);
			table.setFellowNames(fellow_names);

			table_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return table_list;
	}

	/*****************************************************************************
	 * geuntae_master 테이블에 저장하기 (휴가원)
	 *****************************************************************************/
	 public void setGeuntaeHdyTable(String gt_id,String ys_kind,String user_id,String user_name,String user_code,
		String user_rank,String div_id,String div_name,String div_code,String u_year,String u_month,String u_date,
		String tu_year,String tu_month,String tu_date,String gt_purpose,String period,String hd_var,String proxy,String em_tel) throws Exception
	{
		//.없애기
		gt_purpose = str.repWord(gt_purpose,"'","`");

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO GEUNTAE_MASTER(gt_id,ys_kind,user_id,user_name,user_code,";
			incommon += "user_rank,ac_id,ac_code,ac_name,in_date,u_year,u_month,u_date,tu_year,tu_month,tu_date,";
			incommon += "gt_purpose,gt_time_per,hd_var,hd_name,proxy,em_tel) values('";
		String input = "";
		
		//휴가종류 이름구하기
		String hd_name = "";
		if(hd_var == null) hd_var = "";
		if(hd_var.length() != 0) {
			hd_name = geuntaeBO.getHolidayName(hd_var);
		}

		input = incommon+gt_id+"','"+ys_kind+"','"+user_id+"','"+user_name+"','"+user_code+"','";
		input += user_rank+"','"+div_id+"','"+div_name+"','"+div_code+"','"+anbdt.getTime()+"','";
		input += u_year+"','"+u_month+"','"+u_date+"','"+tu_year+"','";
		input += tu_month+"','"+tu_date+"','"+gt_purpose+"','"+period+"','";
		input += hd_var+"','"+hd_name+"','"+proxy+"','"+em_tel+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}
	/*****************************************************************************
	 * geuntae_master 테이블에 저장하기 (출장)
	 *****************************************************************************/
	 public void setGeuntaeBisTripTable(String gt_id,String gt_cid,String ys_kind,String user_id,String user_name,String user_code,
		String user_rank,String div_id,String div_name,String div_code,String fellow_names,String prj_code,String u_year,
		String u_month,String u_date,String tu_year,String tu_month,String tu_date,String gt_dest,String country_class,
		String gt_country,String traffic_way,String gt_purpose,String at_var,String gt_cost,String cost_cont,
		String period,String hd_var,String receiver_id,String receiver_name,String proxy,String em_tel) throws Exception
	{
		//.없애기
		gt_purpose = str.repWord(gt_purpose,"'","`");

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO GEUNTAE_MASTER(gt_id,gt_cid,ys_kind,user_id,user_name,user_code,";
			incommon += "user_rank,ac_id,ac_code,ac_name,fellow_names,prj_code,in_date,";
			incommon += "u_year,u_month,u_date,tu_year,tu_month,tu_date,gt_dest,country_class,gt_country,";
			incommon += "traffic_way,gt_purpose,at_var,at_name,gt_cost,cost_cont,";
			incommon += "gt_time_per,hd_var,hd_name,receiver_id,receiver_name,proxy,em_tel) values('";
		String input = "";
		
		//계정과목 이름구하기
		String at_name = "";
		if(at_var == null) at_var = "";
		if(at_var.length() != 0) {
			at_name = geuntaeBO.getAccountName(at_var); 
		}

		//휴가종류 이름구하기
		String hd_name = "";
		if(hd_var == null) hd_var = "";
		if(hd_var.length() != 0) {
			hd_name = geuntaeBO.getHolidayName(hd_var);
		}

		if(gt_cid == null) gt_cid = "";
		input = incommon+gt_id+"','"+gt_cid+"','"+ys_kind+"','"+user_id+"','"+user_name+"','"+user_code+"','";
		input += user_rank+"','"+div_id+"','"+div_name+"','"+div_code+"','";
		input += fellow_names+"','"+prj_code+"','"+anbdt.getTime()+"','";
		input += u_year+"','"+u_month+"','"+u_date+"','"+tu_year+"','";
		input += tu_month+"','"+tu_date+"','"+gt_dest+"','"+country_class+"','"+gt_country+"','"+traffic_way+"','";
		input += gt_purpose+"','"+at_var+"','"+at_name+"','"+gt_cost+"','"+cost_cont+"','"+period+"','";
		input += hd_var+"','"+hd_name+"','"+receiver_id+"','"+receiver_name+"','"+proxy+"','"+em_tel+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*****************************************************************************
	 * geuntae_account 테이블에 저장하기 (출장 :비용신청)
	 *****************************************************************************/
	 public void setGeuntaeBisTripAccountTable(String gt_id,String gt_cid,String at_var,String gt_cost,String cost_cont) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO GEUNTAE_ACCOUNT(gt_id,gt_cid,at_var,at_name,gt_cost,cost_cont";
			incommon += ") values('";
		String input = "";
		
		//계정과목 이름구하기
		String at_name = "";
		if(at_var == null) at_var = "";
		if(at_var.length() != 0) {
			at_name = geuntaeBO.getAccountName(at_var); 
		}

		if(gt_cid == null) gt_cid = "";
		input = incommon+gt_id+"','"+gt_cid+"','";
		input += at_var+"','"+at_name+"','"+gt_cost+"','"+cost_cont+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}
	/*****************************************************************************
	 * geuntae_master 테이블에 저장하기 (외출계)
	 *****************************************************************************/
	 public void setGeuntaeOutTable(String gt_id,String ys_kind,String user_id,String user_name,String user_code,
		String user_rank,String div_id,String div_name,String div_code,String fellow_names,String u_year,String u_month,String u_date,
		String gt_dest,String traffic_way,String gt_purpose,String period,String hd_var,String proxy,String em_tel) throws Exception
	{
		//.없애기
		gt_purpose = str.repWord(gt_purpose,"'","`");
		gt_dest = str.repWord(gt_dest,"'","`");

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO GEUNTAE_MASTER(gt_id,ys_kind,user_id,user_name,user_code,";
			incommon += "user_rank,ac_id,ac_code,ac_name,fellow_names,in_date,u_year,u_month,u_date,";
			incommon += "gt_dest,traffic_way,gt_purpose,gt_time_per,hd_var,hd_name,proxy,em_tel) values('";
		String input = "";
		
		//휴가종류 이름구하기
		String hd_name = "";
		if(hd_var == null) hd_var = "";
		if(hd_var.length() != 0) {
			hd_name = geuntaeBO.getHolidayName(hd_var);
		}

		input = incommon+gt_id+"','"+ys_kind+"','"+user_id+"','"+user_name+"','"+user_code+"','";
		input += user_rank+"','"+div_id+"','"+div_name+"','"+div_code+"','"+fellow_names+"','"+anbdt.getTime()+"','";
		input += u_year+"','"+u_month+"','"+u_date+"','"+gt_dest+"','"+traffic_way+"','"+gt_purpose+"','";
		input += period+"','"+hd_var+"','"+hd_name+"','"+proxy+"','"+em_tel+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*****************************************************************************
	 * 월별 개인 근태 현황 리스트를 가져온다.
	 *****************************************************************************/
	public ArrayList getTableListByPerson(String year,String month,String login_id) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		DecimalFormat fmt		= new DecimalFormat("00");
		GeunTaeInfoTable table	= new GeunTaeInfoTable();
		ArrayList table_list	= new ArrayList();

		//근태 대상자가 로긴한 사원이거나 동행자에 로긴한 사원이 포함된 리스트를 쿼리해 온다.
		String query = "SELECT ys_kind,u_year,u_month,u_date,tu_year,tu_month,tu_date,gt_purpose,ac_name,user_name,user_rank,";
		query += "gt_time_per,hd_var,hd_name FROM geuntae_master where (u_year = '" + year + "' or tu_year = '" + year + "') and (u_month = '" + month + "'";
		query += " or tu_month = '" + month + "') and (user_id = '" + login_id + "' or fellow_names like '%" + login_id + "%')";
		query += " and flag = 'EF'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			String hd_name	= rs.getString("hd_name");						// 근태구분
			String hd_var	= rs.getString("hd_var");						// 근태구분 코드
			int u_year		= Integer.parseInt(rs.getString("u_year"));		// 시작년도
			int u_month		= Integer.parseInt(rs.getString("u_month"));	// 시작월
			int u_date		= Integer.parseInt(rs.getString("u_date"));		// 시작일

			//외출(코드:OT_001)일경우에는 종료년월일 필드에 null 값이 들어간다.
			//따라서 외출이 아닌 경우에만 종료일을 계산해야 한다. 외출일 경우에는 시작년월일과 같게 세팅
			int tu_year		= u_year;
			int tu_month	= u_month;
			int tu_date		= u_date;
			
			if(!hd_var.equals("OT_001")){
				tu_year		= Integer.parseInt(rs.getString("tu_year"));	// 종료년
				tu_month	= Integer.parseInt(rs.getString("tu_month"));	// 종료월
				tu_date		= Integer.parseInt(rs.getString("tu_date"));	// 종료일
			}
			
			String gt_purpose	= rs.getString("gt_purpose");				// 근태 사유

			int ilsu = 0;			

			// 같은년, 같은 달이고 시작일과 종료일이 동일한 경우(당일인 경우)
			if((u_year == tu_year) && (u_month == tu_month) && (u_date == tu_date)){
				table = new GeunTaeInfoTable();

				table.setDay(u_year + fmt.format(u_month) + fmt.format(u_date));
				table.setYs_kind(hd_name);
				table.setReason(gt_purpose);
	
				table_list.add(table);
			
			// 같은년, 같은 달 내에서 2일 이상인 경우
			}else if((u_year == tu_year) && (u_month == tu_month) && (u_date != tu_date)){ 
				while(ilsu <= (tu_date - u_date)){
					table = new com.anbtech.es.geuntae.entity.GeunTaeInfoTable();
					table.setDay(u_year + fmt.format(u_month) + fmt.format(u_date + ilsu));
					table.setYs_kind(hd_name);
					table.setReason(gt_purpose);
			
					table_list.add(table);
					
					ilsu++;
				}
			// 같은년에서 종료일이 현재 달에 포함되는 경우
			}else if((u_year == tu_year) && (u_month != tu_month) && (tu_month == Integer.parseInt(month))){ 
				while(ilsu < tu_date){
					table = new com.anbtech.es.geuntae.entity.GeunTaeInfoTable();

					table.setDay(u_year + fmt.format(tu_month) + fmt.format(ilsu + 1));
					table.setYs_kind(hd_name);
					table.setReason(gt_purpose);
				
					table_list.add(table);
					
					ilsu++;
				}
			//같은년에서 시작일은 현재 달이지만 종료일이 다음달로 넘어가는 경우
			//31보다 적은 달이 있지만, 달력포맷으로 출력시에 해당달의 월수가 아니면 출력되지 않을 것이므로
			//31일로 고정해도 무방함.
			}else if((u_year == tu_year) && (u_month != tu_month) && (u_month == Integer.parseInt(month))){
				while(ilsu <= 31 - u_date){
					table = new com.anbtech.es.geuntae.entity.GeunTaeInfoTable();

					table.setDay(u_year + fmt.format(u_month) + fmt.format(u_date + ilsu));
					table.setYs_kind(hd_name);
					table.setReason(gt_purpose);
		
					table_list.add(table);
					
					ilsu++;
				}

			//해가 바뀌면서 시작일은 현재 달이지만, 종료일이 다음달로 넘어가는 경우
			//31보다 적은 달이 있지만, 달력포맷으로 출력시에 해당달의 월수가 아니면 출력되지 않을 것이므로
			//31일로 고정해도 무방함.
			}else if((u_year != tu_year) && (u_month != tu_month) && (u_month == Integer.parseInt(month))){
				while(ilsu <= 31 - u_date){
					table = new com.anbtech.es.geuntae.entity.GeunTaeInfoTable();

					table.setDay(u_year + fmt.format(u_month) + fmt.format(u_date + ilsu));
					table.setYs_kind(hd_name);
					table.setReason(gt_purpose);
		
					table_list.add(table);
					
					ilsu++;
				}

			//해가 바뀌면서 시작일은 작년이지만, 종료일이 현재 달인 경우
			}else if((u_year != tu_year) && (u_month != tu_month) && (tu_month == Integer.parseInt(month))){
				while(ilsu < tu_date){
					table = new com.anbtech.es.geuntae.entity.GeunTaeInfoTable();

					table.setDay(tu_year + fmt.format(tu_month) + fmt.format(ilsu + 1));
					table.setYs_kind(hd_name);
					table.setReason(gt_purpose);
				
					table_list.add(table);
					
					ilsu++;
				}
			}
		}

		stmt.close();
		rs.close();
		
		return table_list;
	}

	/*****************************************
	 * 월별 부서별 개인 근태 현황 리스트를 가져온다.
	 *
	 * 입력파라미터
	 * id		: 
	 * year		: 선택된 년도(2004)
	 * hd_var	: 근태구분코드(HD_003:월차,HD_006:년차...)
	 * code		: 부서코드(class_table 의 code 필드값임. 특정조직 선택시 하위조직도 나와야 하기 때문에 실제부서코드값을 사용해서는 안됨)
	 * sortby	: 정렬조건 필드값
	 *****************************************/
	public ArrayList getPersonalStatusInDeptByYear(String id, String year, String hd_var, String code, String sortby) throws Exception {
		com.anbtech.admin.db.UserInfoDAO userinfoDAO		= new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable user_table	= new com.anbtech.admin.entity.UserInfoTable();
		com.anbtech.admin.db.SysConfigDAO cfgDAO			= new com.anbtech.admin.db.SysConfigDAO(con);

		Statement stmt	=con.createStatement();
		ResultSet rs	= null;
		String query	= "";
		double sum		=0.0;

		//출력리스트를 담을 배열 선언
		ArrayList table_list	= new ArrayList();
		GeunTaeInfoTable table	= new GeunTaeInfoTable();
		GeunTaeBO geuntaeBO		= new GeunTaeBO(con);

		//1.선택된 부서코드값(code)을 가지고 해당 부서 및 하위부서의 부서원 리스트를 가져온다.
		ArrayList user_list = new ArrayList();
		user_list = (ArrayList)userinfoDAO.getUserListByInnerCode(code);
		Iterator user_iter = user_list.iterator();

		//2.각각의 사용자 사번으로 geuntae_count 테이블을 검색하여 해당 사번의 데이터가 있으면 선택해서 가져오고,
		//없으면 0으로 세팅하여 출력리스트를 만든다.
		while(user_iter.hasNext()){
			user_table = (com.anbtech.admin.entity.UserInfoTable)user_iter.next();
			String user_id = user_table.getUserId();	//사번

			query = "SELECT * FROM geuntae_count WHERE thisyear = '"+year+"' AND hd_var LIKE '%"+hd_var+"%' AND user_id = '"+user_id+"' ";
			rs = stmt.executeQuery(query);
			
			//해당 사용자의 근태 데이터가 있을 경우
			int sel_count = 0;
			while(rs.next()){
				table = new GeunTaeInfoTable();

				table.setGt_id(rs.getString("gt_id"));				//관리번호
				table.setUser_rank(rs.getString("user_rank"));		//직급명(입력된 당시의 직급명을 그대로 출력한다.)
				table.setUser_name(sel_count==0?rs.getString("user_name"):"");		//사용자 이름
				table.setAc_name(rs.getString("ac_name"));			//부서명(입력된 당시의 부서명을 그대로 출력한다.)
				table.setKindtokor(cfgDAO.getMinorCodeName("GEUNTAE",rs.getString("hd_var")));	//근태구분(월차,년차...)
				table.setUser_id(rs.getString("user_id"));			//사용자 사번
				table.setUser_code(rs.getString("user_code"));		//사용자 직급코드
				table.setThisyear(rs.getString("thisyear"));		//선택년도
	
				String[] m=new String[12];							//각월별 사용수량
				m[0] = rs.getString("jan1");
				m[1] = rs.getString("feb2");
				m[2] = rs.getString("mar3");
				m[3] = rs.getString("apr4");
				m[4] = rs.getString("may5");
				m[5] = rs.getString("jun6");
				m[6] = rs.getString("jul7");
				m[7] = rs.getString("aug8");
				m[8] = rs.getString("sep9");
				m[9] = rs.getString("oct10");
				m[10] = rs.getString("nov11");
				m[11] = rs.getString("dec12");

				int len = 0;
				if(rs.getString("hd_var").equals("OT_001")){
					for(int i=0;i<12;i++){
						len = m[i].length();
						m[i] = m[i].substring(0,len-2);
					}
				}

				table.setJan1("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=01&eachid="+rs.getString("user_id")+"'>"+m[0]+"</a>");
				table.setFeb2("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=02&eachid="+rs.getString("user_id")+"'>"+m[1]+"</a>");
				table.setMar3("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=03&eachid="+rs.getString("user_id")+"'>"+m[2]+"</a>");
				table.setApr4("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=04&eachid="+rs.getString("user_id")+"'>"+m[3]+"</a>");
				table.setMay5("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=05&eachid="+rs.getString("user_id")+"'>"+m[4]+"</a>");
				table.setJun6("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=06&eachid="+rs.getString("user_id")+"'>"+m[5]+"</a>");
				table.setJul7("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=07&eachid="+rs.getString("user_id")+"'>"+m[6]+"</a>");
				table.setAug8("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=08&eachid="+rs.getString("user_id")+"'>"+m[7]+"</a>");
				table.setSep9("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=09&eachid="+rs.getString("user_id")+"'>"+m[8]+"</a>");
				table.setOct10("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=10&eachid="+rs.getString("user_id")+"'>"+m[9]+"</a>");
				table.setNov11("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=11&eachid="+rs.getString("user_id")+"'>"+m[10]+"</a>");
				table.setDec12("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=12&eachid="+rs.getString("user_id")+"'>"+m[11]+"</a>");
				
				sum=Double.parseDouble(rs.getString("jan1"))+Double.parseDouble(rs.getString("feb2"))+Double.parseDouble(rs.getString("mar3"))+Double.parseDouble(rs.getString("apr4"))+Double.parseDouble(rs.getString("may5"))+Double.parseDouble(rs.getString("jun6"))+Double.parseDouble(rs.getString("jul7"))+Double.parseDouble(rs.getString("aug8"))+Double.parseDouble(rs.getString("sep9"))+Double.parseDouble(rs.getString("oct10"))+Double.parseDouble(rs.getString("nov11"))+Double.parseDouble(rs.getString("dec12"));			
				if(rs.getString("hd_var").equals("OT_001")){
					int slen=Double.toString(sum).length();
					table.setSum(Double.toString(sum).substring(0,slen-2)+"회");
				}else {
					table.setSum(Double.toString(sum));
				}

				table_list.add(table);
				sel_count++;
			}

			//해당 사용자의 근태 데이터가 없는 경우
			if(sel_count == 0){
				table = new com.anbtech.es.geuntae.entity.GeunTaeInfoTable();

				table.setGt_id("");									//관리번호
				table.setUser_rank(user_table.getUserRank());		//직급명
				table.setUser_name(user_table.getUserName());		//사용자 이름
				table.setAc_name(user_table.getDivision());			//부서명
				table.setKindtokor(geuntaeBO.getHdkind(hd_var));	//근태구분(월차,년차...)
				table.setUser_id(user_id);							//사용자 사번
				table.setUser_code("");								//사용자 직급코드
				table.setThisyear(year);							//선택년도
		
				table.setJan1("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=01&eachid="+user_id+"'>0</a>");
				table.setFeb2("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=02&eachid="+user_id+"'>0</a>");
				table.setMar3("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=03&eachid="+user_id+"'>0</a>");
				table.setApr4("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=04&eachid="+user_id+"'>0</a>");
				table.setMay5("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=05&eachid="+user_id+"'>0</a>");
				table.setJun6("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=06&eachid="+user_id+"'>0</a>");
				table.setJul7("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=07&eachid="+user_id+"'>0</a>");
				table.setAug8("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=08&eachid="+user_id+"'>0</a>");
				table.setSep9("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=09&eachid="+user_id+"'>0</a>");
				table.setOct10("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=10&eachid="+user_id+"'>0</a>");
				table.setNov11("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=11&eachid="+user_id+"'>0</a>");
				table.setDec12("<a href='GeunTaeServlet?mode=person_month&y="+year+"&m=12&eachid="+user_id+"'>0</a>");
				table.setSum("");

				table_list.add(table);
			}
		}

		rs.close();
		stmt.close();

		return table_list;
	}

	/*******************************************
	 * 월별 부서별 근태 현황 리스트를 가져온다.
	 * 입력 파라미터
	 * year		: 선택된 년도(2004)
	 * hd_var	: 근태구분
	 *******************************************/
	public ArrayList getDivisionalStatusByYear(String year, String hd_var) throws Exception {
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		GeunTaeInfoTable div = new GeunTaeInfoTable();		// 트리형태의 부서명을 담을 개체
		GeunTaeInfoTable table = new GeunTaeInfoTable();	// 부서명+월별근태수량 즉, 넘겨줄 데이터를 담을 개체
		ArrayList table_list = new ArrayList();				// 넘겨줄 데이터를 담을 개체
		GeunTaeBO geuntaeBO = new GeunTaeBO(con);

		//1.hierachy한 부서리스트를 가져온다.
		com.anbtech.admin.db.makeClassTree mct = new com.anbtech.admin.db.makeClassTree(con);
		ArrayList div_list = new ArrayList();
		div_list = (ArrayList)mct.getDivNameByTreeForGeuntaeInfo(0,0);
		Iterator div_iter = div_list.iterator();

		//2.각각의 부서조직별로 선택된 근태항목에 대한 수량을 계산하여 table_list에 설정한다.
		//근태항목이 설정되지 않으면 디폴트로 모든 근태항목에 대한 수량을 출력한다.
		while(div_iter.hasNext()){
			div = (GeunTaeInfoTable)div_iter.next();
			String code		= div.getAc_name();		//부서내부코드를 가져온다.
			String div_name = div.getDepartment();	//부서명을 가져온다.

			//근태구분을 선택하지 않은 경우 즉, 전체가 선택된 경우
			//근태항목을 가져온 뒤, 가각의 수량을 계산하여 설정한다.
			if(hd_var.equals("") || hd_var == null){
				String sql = "SELECT code FROM system_minor_code WHERE type = 'GEUNTAE'";
				rs = stmt.executeQuery(sql);
				String hd_vars = "";
				while(rs.next()){
					hd_vars += rs.getString("code")+"|";
				}

				//중첩 resultset 구조를 허용하지 않기 때문에 근태항목을 별도의 배열로 저장한 다음 배열의 크기만큼
				//반복하는 구조를 취함.
				ArrayList hd_var_list = com.anbtech.util.Token.getTokenList(hd_vars);
				int i = 0;
				while(i < hd_var_list.size()){
					String hd_var_n = (String)hd_var_list.get(i);

					String query = "";
					query = "select SUM(jan1) 'jan1',SUM(feb2) 'feb2',SUM(mar3) 'mar3',SUM(apr4) 'apr4',";
					query += "SUM(may5) 'may5',SUM(jun6) 'jun6',SUM(jul7) 'jul7',SUM(aug8) 'aug8',";
					query += "SUM(sep9) 'sep9',SUM(oct10) 'oct10',SUM(nov11) 'nov11',SUM(dec12) 'dec12' ";
					query += "from geuntae_count where thisyear = '" + year + "' and hd_var = '"+hd_var_n+"' ";
					query += "and code like '" + code + "%'";

					rs = stmt.executeQuery(query);
					while(rs.next()) {
						table = new GeunTaeInfoTable();

						String url = "<a href='GeunTaeServlet?mode=person_year&div="+code+"&hd_var="+hd_var_n+"&y="+year+"'>"+div_name+"</a>";
						if(i > 0) url = "";
						table.setDepartment(url);

						//월별 근태수량 설정
						String hd_name = geuntaeBO.getHdkind(hd_var_n);
						table.setHd_var(hd_name);
						table.setJan1(rs.getString("jan1")==null?"0":rs.getString("jan1"));
						table.setFeb2(rs.getString("feb2")==null?"0":rs.getString("feb2"));
						table.setMar3(rs.getString("mar3")==null?"0":rs.getString("mar3"));
						table.setApr4(rs.getString("apr4")==null?"0":rs.getString("apr4"));
						table.setMay5(rs.getString("may5")==null?"0":rs.getString("may5"));
						table.setJun6(rs.getString("jun6")==null?"0":rs.getString("jun6"));
						table.setJul7(rs.getString("jul7")==null?"0":rs.getString("jul7"));
						table.setAug8(rs.getString("aug8")==null?"0":rs.getString("aug8"));
						table.setSep9(rs.getString("sep9")==null?"0":rs.getString("sep9"));
						table.setOct10(rs.getString("oct10")==null?"0":rs.getString("oct10"));
						table.setNov11(rs.getString("nov11")==null?"0":rs.getString("nov11"));
						table.setDec12(rs.getString("dec12")==null?"0":rs.getString("dec12"));

						//각 근태별 합계수량 계산 및 설정
						double total = 0.0;
						total += Double.parseDouble(rs.getString("jan1")==null?"0.0":rs.getString("jan1"));
						total += Double.parseDouble(rs.getString("feb2")==null?"0.0":rs.getString("feb2"));
						total += Double.parseDouble(rs.getString("mar3")==null?"0.0":rs.getString("mar3"));
						total += Double.parseDouble(rs.getString("apr4")==null?"0.0":rs.getString("apr4"));
						total += Double.parseDouble(rs.getString("may5")==null?"0.0":rs.getString("may5"));
						total += Double.parseDouble(rs.getString("jun6")==null?"0.0":rs.getString("jun6"));
						total += Double.parseDouble(rs.getString("jul7")==null?"0.0":rs.getString("jul7"));
						total += Double.parseDouble(rs.getString("aug8")==null?"0.0":rs.getString("aug8"));
						total += Double.parseDouble(rs.getString("sep9")==null?"0.0":rs.getString("sep9"));
						total += Double.parseDouble(rs.getString("oct10")==null?"0.0":rs.getString("oct10"));
						total += Double.parseDouble(rs.getString("nov11")==null?"0.0":rs.getString("nov11"));
						total += Double.parseDouble(rs.getString("dec12")==null?"0.0":rs.getString("dec12"));
						
						table.setSum(Double.toString(total));

						table_list.add(table);
					}//end while
					i++;
				}//end whild
			
			//근태항목이 선택된 경우 해당항목에 대한 수량을 계산하여 설정한다.
			}else{
				String query = "";
				query = "select SUM(jan1) 'jan1',SUM(feb2) 'feb2',SUM(mar3) 'mar3',SUM(apr4) 'apr4',";
				query += "SUM(may5) 'may5',SUM(jun6) 'jun6',SUM(jul7) 'jul7',SUM(aug8) 'aug8',";
				query += "SUM(sep9) 'sep9',SUM(oct10) 'oct10',SUM(nov11) 'nov11',SUM(dec12) 'dec12' ";
				query += "from geuntae_count where thisyear = '" + year + "' and hd_var = '"+hd_var+"' ";
				query += "and code like '" + code + "%'";


				rs = stmt.executeQuery(query);
				while(rs.next()) {
					table = new GeunTaeInfoTable();
					
					String url = "<a href='GeunTaeServlet?mode=person_year&div="+code+"&hd_var="+hd_var+"&y="+year+"'>"+div_name+"</a>";
					table.setDepartment(url);

					//월별 근태수량 설정
					String hd_name = geuntaeBO.getHdkind(hd_var);
					table.setHd_var(hd_name);
					table.setJan1(rs.getString("jan1")==null?"0":rs.getString("jan1"));
					table.setFeb2(rs.getString("feb2")==null?"0":rs.getString("feb2"));
					table.setMar3(rs.getString("mar3")==null?"0":rs.getString("mar3"));
					table.setApr4(rs.getString("apr4")==null?"0":rs.getString("apr4"));
					table.setMay5(rs.getString("may5")==null?"0":rs.getString("may5"));
					table.setJun6(rs.getString("jun6")==null?"0":rs.getString("jun6"));
					table.setJul7(rs.getString("jul7")==null?"0":rs.getString("jul7"));
					table.setAug8(rs.getString("aug8")==null?"0":rs.getString("aug8"));
					table.setSep9(rs.getString("sep9")==null?"0":rs.getString("sep9"));
					table.setOct10(rs.getString("oct10")==null?"0":rs.getString("oct10"));
					table.setNov11(rs.getString("nov11")==null?"0":rs.getString("nov11"));
					table.setDec12(rs.getString("dec12")==null?"0":rs.getString("dec12"));

					//각 근태별 합계수량 계산 및 설정
					double total = 0.0;
					total += Double.parseDouble(rs.getString("jan1")==null?"0.0":rs.getString("jan1"));
					total += Double.parseDouble(rs.getString("feb2")==null?"0.0":rs.getString("feb2"));
					total += Double.parseDouble(rs.getString("mar3")==null?"0.0":rs.getString("mar3"));
					total += Double.parseDouble(rs.getString("apr4")==null?"0.0":rs.getString("apr4"));
					total += Double.parseDouble(rs.getString("may5")==null?"0.0":rs.getString("may5"));
					total += Double.parseDouble(rs.getString("jun6")==null?"0.0":rs.getString("jun6"));
					total += Double.parseDouble(rs.getString("jul7")==null?"0.0":rs.getString("jul7"));
					total += Double.parseDouble(rs.getString("aug8")==null?"0.0":rs.getString("aug8"));
					total += Double.parseDouble(rs.getString("sep9")==null?"0.0":rs.getString("sep9"));
					total += Double.parseDouble(rs.getString("oct10")==null?"0.0":rs.getString("oct10"));
					total += Double.parseDouble(rs.getString("nov11")==null?"0.0":rs.getString("nov11"));
					total += Double.parseDouble(rs.getString("dec12")==null?"0.0":rs.getString("dec12"));
					
					table.setSum(Double.toString(total));

					table_list.add(table);
				}//end while
			}//end if
		}//end while

		stmt.close();
		rs.close();

		return table_list;
	}

	/*****************************************
	 * 일별 부서별 출퇴근 시각현황을 가져온다.
	 *
	 * 입력파라미터
	 * year		: 선택된 년도(2004)
	 * month	: 선택된 월(03)
	 * day		: 선택된 일(25)
	 * division	: 부서코드(class_table 의 code 필드값임. 특정조직 선택시 하위조직도 나와야 하기 때문에 실제부서코드값을 사용해서는 안됨)
	 *
	 *****************************************/
	public ArrayList getWorkHistoryByDay(String year,String month,String day,String division) throws Exception {
		Statement stmt	= con.createStatement();
		ResultSet rs	= null;
		String query	= "";
		String reason	= "출근";

		ArrayList table_list = new ArrayList();
		GeunTaeInfoTable table = new GeunTaeInfoTable();
		GeunTaeBO geuntaeBO = new GeunTaeBO(con);

		//오늘날짜
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String today = vans.format(now);
		String selected_day = year + month + day;
		
		//선택된 부서코드값(code)을 가지고 해당 부서 및 하위부서의 부서원 리스트를 가져온다.
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable user_table = new com.anbtech.admin.entity.UserInfoTable();
		ArrayList user_list = new ArrayList();
		user_list = (ArrayList)userinfoDAO.getUserListByInnerCode(division);
		Iterator user_iter = user_list.iterator();

		//각각의 사용자 사번으로 geuntae_count 테이블을 검색하여 해당 사번의 데이터를 가져온다.
		while(user_iter.hasNext()){
			user_table = (com.anbtech.admin.entity.UserInfoTable)user_iter.next();
			String user_id		= user_table.getUserId();	//사용자 사번
			String user_name	= "<a href='GeunTaeServlet?mode=person_month&y="+year+"&m="+month+"&eachid="+user_id+"'>"+user_table.getUserName()+"</a>";
			String user_rank	= user_table.getUserRank();	//사용자 직급명
			String user_div		= user_table.getDivision();	//사용자 부서명

			//해당 사용자의 근태 및 특근정보를 가져온다.
			String ys_kind = geuntaeBO.getPersonalStatusByDay(year,month,day,user_id);

			//출퇴근 기록정보를 가져온다.
			query = "SELECT * FROM work_history WHERE c_id = '" + user_id + "' and c_year = '" + year + "' and c_month = '" + month + "' and c_day = '" + day + "'";
			rs = stmt.executeQuery(query);
			
			if(rs.next()){
				table = new GeunTaeInfoTable();

				table.setUser_rank(user_rank);						//직급명
				table.setUser_name(user_name);						//사용자이름
				table.setAc_name(user_div);							//부서명
				table.setUser_id(user_id);							//사용자사번
				table.setTimeS(rs.getString("c_time_s"));			//출근시각
				table.setTimeE(rs.getString("c_time_e"));			//퇴근시각
				table.setH_sdate(rs.getString("c_ip_s"));			//출근기록한 컴의 IP
				table.setH_edate(rs.getString("c_ip_e")==null?"":rs.getString("c_ip_e"));			//퇴근기록한 컴의 IP
				
				if(!rs.getString("c_time_s").equals("") && rs.getString("c_time_e").equals("")) reason = "출근";
				if(!rs.getString("c_time_s").equals("") && !rs.getString("c_time_e").equals("")) reason = "퇴근";
				
				//출근시각 기록이 있고, 퇴근시각 기록이 없으면서 지난일자일 경우에는
				//무단조퇴라고 간주
				if(rs.getString("c_time_s").length() > 1 && rs.getString("c_time_e").length() < 1 && !today.equals(selected_day)) reason = "<font color='blue'>조퇴</font>";

				//근태기록이 있으면 근태정보를 출력
				if(ys_kind.length() > 1) reason = ys_kind;

				table.setReason(reason);							//비고
	
				table_list.add(table);
			}

			//해당 사용자의 기록이 없는 경우
			else{
				table = new GeunTaeInfoTable();
				
				//출퇴근기록이 없으면 결근으로 간주
				reason = "<font color='red'>결근</font>";
				
				//근태기록이 있으면 근태정보를 출력
				if(ys_kind.length() > 1) reason = ys_kind;
				
				table.setUser_rank(user_rank);						//직급명
				table.setUser_name(user_name);						//사용자이름
				table.setAc_name(user_div);							//부서명
				table.setUser_id(user_id);							//사용자사번
				table.setTimeS("");									//출근시각
				table.setTimeE("");									//퇴근시각
				table.setH_sdate("");								//출근기록한 컴의 IP
				table.setH_edate("");								//퇴근기록한 컴의 IP
				table.setReason(reason);							//비고

				table_list.add(table);
			}
		}

		rs.close();
		stmt.close();

		return table_list;
	}

	/*****************************************************************************
	 * 현재 휴가 잔량													  ---#####  pjh
	 *****************************************************************************/
	 public double getRestHd(String id, String kind, String year) throws Exception  {
			
		Statement stmt = null;
		ResultSet rs = null;
		double rest=0.0;
			
		String query = "SELECT * FROM geuntae_hd_max WHERE id ='"+id+"' and kind='"+kind+"' and byyear='"+year+"'";
			
		stmt = con.createStatement();
		rs=stmt.executeQuery(query);
			
		if(rs.next()) {
			rest=Double.parseDouble(rs.getString("hdmax"));
		} 
		
		rs.close();
		stmt.close();
		
		return rest;
	}

	/*****************************************************************************
	 * 각 개인별 해당 년월일의 출근 시각 및 퇴근 시각을 가져온다.
	 *****************************************************************************/
	public String getWorkTime(String login_id,String wdate) throws Exception {

		GeunTaeBO geuntaeBO = new GeunTaeBO(con);
	
		Statement stmt = null;
		ResultSet rs = null;
		String year		= wdate.substring(0,4);
		String month	= wdate.substring(4,6);
		String day		= wdate.substring(6,8);

		String yesterday = geuntaeBO.getYesterday(year,month,day);

		String time = "|";
		String query = "SELECT c_time_s,c_time_e,c_end_date FROM work_history WHERE c_id = '" + login_id + "' and c_year = '" + year + "' and c_month = '" + month + "' and c_day = '" + day + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			time = rs.getString("c_time_s") + "|" + rs.getString("c_time_e");
		}
		
		stmt.close();
		rs.close();

		return time;
	}


	/***********************************************
	 * 각 개인별 해당 년월일의 출근 시각을 기록한다.
	 ***********************************************/
	public void saveWorkTimeS(String login_id,String wdate,String time_s,String ipaddress) throws Exception {

		Statement stmt = null;

		String query = "INSERT INTO work_history (c_id,c_year,c_month,c_day,c_time_s,c_time_e,c_ip_s,c_ip_e) VALUES ('"+login_id+"','"+wdate.substring(0,4)+"','"+wdate.substring(4,6)+"','"+wdate.substring(6,8)+"','"+time_s+"','','"+ipaddress+"','')";

		stmt = con.createStatement();
		stmt.executeUpdate(query);

		stmt.close();
	}

	/***********************************************
	 * 각 개인별 해당 년월일의 퇴근 시각을 기록한다.
	 ***********************************************/
	public void saveWorkTimeE(String login_id,String wdate,String time_e,String ipaddress) throws Exception {

		Statement stmt = null;

		String query = "UPDATE work_history SET c_time_e = '" + time_e + "',c_ip_e = '" + ipaddress + "', c_end_date ='"+wdate+"' WHERE c_id = '" + login_id + "' and c_year = '" + wdate.substring(0,4) + "' and c_month = '" + wdate.substring(4,6) + "' and c_day = '" + wdate.substring(6,8) + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);

		stmt.close();
	}


	/*****************************************************************************
	 * 각 개인별 해당 년월일의 퇴근 일자를 가져온다.
	 *****************************************************************************/
	public String getEndDate(String login_id,String wdate) throws Exception {
		
		Statement st = con.createStatement();
		
		String query = "SELECT c_end_date FROM work_history WHERE c_id = '" + login_id + "' and c_year = '" + wdate.substring(0,4) + "' and c_month = '" + wdate.substring(4,6) + "' and c_day = '" + wdate.substring(6,8) + "'";
		
		ResultSet rs = st.executeQuery(query);

		String c_end_date="";
		
		while(rs.next()){
			c_end_date = rs.getString("c_end_date");
		}
		
		st.close();
		rs.close();

		return c_end_date;
	}

}		