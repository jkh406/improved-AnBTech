package com.anbtech.es.geuntae.business;

import com.anbtech.es.geuntae.entity.*;
import com.anbtech.ew.entity.*;
import com.anbtech.es.geuntae.db.*;
import com.anbtech.ew.db.*;
import com.oreilly.servlet.MultipartRequest;
import java.text.DecimalFormat;
import com.anbtech.util.CalendarBean;

import java.sql.*;
import java.util.*;
import java.io.*;

public class GeunTaeBO{

	private Connection con;

	public GeunTaeBO(Connection con){
		this.con = con;
	}

	/*****************************************************************
	 * where 구문을 만든다.
	 *****************************************************************/
	public String getWhere(String mode,String searchword, String searchscope, String category, String login_id) throws Exception{

		//검색조건에 맞게 where변수를 수정한다.
		String where = "", where_cat = "", where_and = "", where_sea = "";
		
		// 검색 조건이 없을때...
		//if(mode.equals("list") && searchword.equals("") && searchscope.equals("") && !login_id.equals("")) {
		where = " WHERE user_id='"+login_id+"' and flag = 'EF'";
		//}
		return where;
	}

	public String getWhere(String mode,String searchword, String searchscope, String category) throws Exception{

		//검색조건에 맞게 where변수를 수정한다.
		String where = "", where_cat = "", where_and = "", where_sea = "";
		
		// 검색 조건이 없을때...
		//if(mode.equals("list") && searchword.equals("") && searchscope.equals("") && !login_id.equals("")) {
		where = " WHERE flag = 'EF'";
		//}
		return where;
	}


	/*****************************************************************
	 * 출장동행자 이름 알아보기
	 *****************************************************************/
	public String getFellowName(String fellow_id) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String data = "";
		StringTokenizer ids = new StringTokenizer(fellow_id,";");
		while(ids.hasMoreTokens()) {
			String query = "select name from user_table where id='"+ids.nextToken()+"'";
			rs = stmt.executeQuery(query);
			if(rs.next()) data += rs.getString("name")+";";
		}
		stmt.close();
		rs.close();
		return data;
	}

	/*****************************************************************
	 * 휴가명 알아보기
	 *****************************************************************/
	public String getAccountName(String hd_id) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String data = "";
		String query = "select ys_value from yangsic_env where ys_name='"+hd_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data += rs.getString("ys_value");
		
		stmt.close();
		rs.close();
		return data;
	}

	/*****************************************************************
	 * 계정과목 이름 알아보기
	 *****************************************************************/
	public String getHolidayName(String at_id) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String data = "";
		String query = "select ys_value from yangsic_env where ys_name='"+at_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data += rs.getString("ys_value");
		
		stmt.close();
		rs.close();
		return data;
	}


	/*****************************************************************************
	 * 월별 개인 근태 현황 리스트를 가져와서 달력 형태로 리턴한다.
	 *****************************************************************************/
	public ArrayList getPersonalStatusByMonth(String year,String month,String login_id) throws Exception {

		GeunTaeDAO geuntaeDAO			= new GeunTaeDAO(con);
		GeunTaeInfoTable table			= new GeunTaeInfoTable();
		ExtraWorkModuleDAO ewDAO		= new ExtraWorkModuleDAO(con);
		ExtraWorkHistoryTable ewtable	= new ExtraWorkHistoryTable();

		//월별 개인 근태 현황 리스트를 가져온다.
		//근태기간이 2일 이상인 경우에는 해당 기간만큼 리스트가 생성된다.
		ArrayList table_list = new ArrayList();
		table_list = (ArrayList)geuntaeDAO.getTableListByPerson(year,month,login_id);
		
		//월별 개인 특근 현황 리스트를 가져온다.
		ArrayList ew_table = new ArrayList();
		ew_table = (ArrayList)ewDAO.getTableListByPerson(year,month,login_id);

		//########## 달력 포맷으로 출력부 시작 ##################
		DecimalFormat fmt = new DecimalFormat("00");
		Calendar curren_date,temp_date;

		int cur_year = Integer.parseInt(year);
		int cur_month = Integer.parseInt(month);

		//보고자 하는 년월을 세팅한다.
		temp_date = Calendar.getInstance();
		temp_date.set(Calendar.YEAR,cur_year);
		temp_date.set(Calendar.MONTH,cur_month-1);
		temp_date.set(Calendar.DATE,1);

		com.anbtech.util.CalendarBean calendar = new com.anbtech.util.CalendarBean();
		calendar.setNewDate(temp_date);

		int dayno = calendar.getMyWeek();
		int lastday = calendar.getMyLastDay();

		//보고자 하는 년월 지정
		String cur_yyyymm = cur_year + fmt.format(cur_month);

		int inx;  
		String wdate	 = "";		// 년월일(20030927)
		String wcontents = "";		// 근태 내용
		String url		 = "";		// 해달 날짜에 적용할 링크 문자열
		String day		 = "";		// 해당 날짜
		String ys_kind	 = "";		// 근태 구분
		String time		 = "";		// 출근시각|퇴근시각
		String time_s	 = "";		// 출근 시각
		String time_e	 = "";		// 퇴근 시작
		
		inx = ((-1) * dayno) + 2 ;


		// 달력폼의 리스트를 담을 변수 지정
		GeunTaeInfoTable table_1 = new GeunTaeInfoTable();
		ArrayList table_list_1 = new ArrayList();
		
		while (inx < lastday){
			
		
			//######일요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));
				
				//근태
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}

				//특근
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "특근";
					}
				}

				url = "<font color='red'>"+inx+"(일)</font>";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);
								
				table_list_1.add(table_1);
			}
			inx++;


			//######월요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//근태
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// 특근
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "특근";
					}
				}

				url = inx+"(월)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######화요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//근태
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// 특근
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "특근";
					}
				}

				url = inx+"(화)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######수요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//근태
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// 특근
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "특근";
					}
				}

				url = inx+"(수)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######목요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//근태
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// 특근
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "특근";
					}
				}

				url = inx+"(목)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######금요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//근태
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// 특근
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "특근";
					}
				}

				url = inx+"(금)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######토요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//근태
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// 특근
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "특근";
					}
				}

				url = "<font color='blue'>"+inx+"(토)</font>";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;
		}//end while

/* 디버깅용 코드
		Iterator table_iter = table_list_1.iterator();
		while(table_iter.hasNext()){
			table_1 = (GeunTaeInfoTable)table_iter.next();
			String day_1 = table_1.getDay();
			String ys_kind_1 = table_1.getYs_kind();
			String reason_1 = table_1.getReason();
		}
*/
		return table_list_1;
	}

	/*****************************************************************************
	 * 일별 개인 근태 현황 리스트를 가져와서 해당 일자의 근태구분문자열을 리턴한다.
	 *****************************************************************************/
	public String getPersonalStatusByDay(String year,String month,String day,String login_id) throws Exception {

		GeunTaeDAO geuntaeDAO			= new GeunTaeDAO(con);
		GeunTaeInfoTable table			= new GeunTaeInfoTable();
		ExtraWorkModuleDAO ewDAO		= new ExtraWorkModuleDAO(con);
		ExtraWorkHistoryTable ewtable	= new ExtraWorkHistoryTable();

		//월별 개인 근태 현황 리스트를 가져온다.
		//근태기간이 2일 이상인 경우에는 해당 기간만큼 리스트가 생성된다.
		ArrayList geuntae_list = new ArrayList();
		geuntae_list = (ArrayList)geuntaeDAO.getTableListByPerson(year,month,login_id);
		
		//월별 개인 특근 현황 리스트를 가져온다.
		ArrayList ew_list = new ArrayList();
		ew_list = (ArrayList)ewDAO.getTableListByPerson(year,month,login_id);

		String wdate = year + month + day;	//선택된 년월일
		String ys_kind = "";				//근태구분
		
		//근태정보 가져오기
		Iterator table_iter = geuntae_list.iterator();
		while(table_iter.hasNext()){
			table = (GeunTaeInfoTable)table_iter.next();
			String geuntae_day = table.getDay();
			if(geuntae_day.equals(wdate)){
				ys_kind += table.getYs_kind() + "<br>";
			}
		}

		//특근정보 가져오기
		Iterator table_iter2 = ew_list.iterator();
		while(table_iter2.hasNext()){
			ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
			String ew_day = ewtable.getRsdate();
			if(ew_day.equals(wdate)){
				ys_kind		+= "특근";
			}
		}

		return ys_kind;
	}

	/****************
	 * 근태 구분
	 ****************/
	 public String getHdkind(String kind) throws Exception  {
			
			Statement stmt = null;
			ResultSet rs = null;
			String hyuga_kind="";
				
		
			stmt=con.createStatement();
			String query = "SELECT ys_value FROM  yangsic_env WHERE ys_name = '"+kind+"'";
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				hyuga_kind=rs.getString("ys_value");
			}
			stmt.close();
			rs.close();
		
			return hyuga_kind;
	}

	/***************************************************************************
	  * 지정된 년월일의 이전 날짜(년월일) 값 가져오기
	*****************************************************************************/
		public String getYesterday(String year, String month, String day) throws Exception {		
		
		com.anbtech.util.CalendarBean calendar = new com.anbtech.util.CalendarBean();
		
		DecimalFormat fmt = new DecimalFormat("00");
		Calendar curren_date,temp_date;

		int cur_year  = Integer.parseInt(year);
		int cur_month = Integer.parseInt(month);
		int day_int	  = Integer.parseInt(day);

		String yesterday ="";

		if(day_int==1) { // 현재 일자가 1일인지 확인(현재월을 전월로 setting)
		
			if((cur_month - 1) <1) {     // 현재월이 1월일때 (전년도로 setting)
									
				cur_month = 12;
				cur_year  = cur_year - 1;

			} else {
				
				cur_month--;			//  현재월이 1월이 아닐때 현재월을 1월 차감
	
			}

			// 어제 일자 구하기
			temp_date = Calendar.getInstance();
			temp_date.set(Calendar.YEAR,cur_year);
			temp_date.set(Calendar.MONTH,cur_month-1);
			temp_date.set(Calendar.DATE,1);

			//com.anbtech.util.CalendarBean calendar = new com.anbtech.util.CalendarBean();
			calendar.setNewDate(temp_date);
			int lastday = calendar.getMyLastDay(); 
			month = fmt.format(cur_month);
			day = fmt.format(lastday);

			yesterday = cur_year + "" + month + "" + day;
			
		} else {
			month = fmt.format(cur_month);
			day = fmt.format(day_int-1);
		
			yesterday = cur_year + "" + month + "" + day;
		}

		return yesterday;
	}		

	/***************************************************************************
	 * ID을 구하는 메소드
	 **************************************************************************/
	public String getID() throws Exception 
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}		

}