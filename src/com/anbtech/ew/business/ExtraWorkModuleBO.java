package com.anbtech.ew.business;

import com.anbtech.ew.entity.*;
import com.anbtech.ew.db.*;
import java.text.DecimalFormat;
import com.anbtech.util.CalendarBean;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.text.Hanguel;
import java.text.NumberFormat;

import java.sql.*;
import java.util.*;
import java.io.*;


public class ExtraWorkModuleBO
{
	private Connection con;

	public ExtraWorkModuleBO(Connection con){
		this.con = con;
	}

	/**************************
	 * 코드별 코드명 가져오기
	 **************************/
	public String getStatname(String code) throws Exception {
		String stat_name = "";
		if(code.equals("1"))		stat_name = "상신전";
		else if(code.equals("2"))	stat_name = "결재진행중";
		else if(code.equals("3"))	stat_name = "승인";
		else if(code.equals("4"))	stat_name = "반려";
		else if(code.equals("5"))   stat_name = "시작확인";
		else if(code.equals("6"))   stat_name = "마침확인";
		else if(code.equals("7"))   stat_name = "";
		else if(code.equals("9"))	stat_name = "취소";
		else if(code.equals("0"))	stat_name = "삭제";
		
		else if(code.equals("n"))   stat_name = "평일";
		else if(code.equals("s"))   stat_name = "토요일";
		else if(code.equals("h"))   stat_name = "휴일";
		
		else if(code.equals("d"))   stat_name = "금일";
		else if(code.equals("t"))   stat_name = "명일";  // 내일

		else if(code.equals("yp"))  stat_name = "년봉";
		else if(code.equals("mp"))  stat_name = "월봉";
		else if(code.equals("tp"))  stat_name = "시간급";

		else if(code.equals("ds"))  stat_name = "기본급";
		else if(code.equals("ts"))  stat_name = "통상급";

		else if(code.equals("r"))   stat_name = "정규직";
		else if(code.equals("b"))   stat_name = "비정규직";
		else if(code.equals("c"))   stat_name = "계약직";
		else if(code.equals("a"))   stat_name = "아르바이트";
		else if(code.equals("f"))   stat_name = "파견직";
		else stat_name="";

		return stat_name;
	}

	/***********************
	 * 조건절 쿼리구문 생성
	 ***********************/
	public String getWhere(String mode,String login_id) throws Exception {
		
//		String where= " WHERE member_id = '" + login_id + "' AND status IN('1','4')";
		String where= " WHERE member_id = '" + login_id + "'";

		return where;
	}

	/***************************************
	 * 특근신청 리스트를 볼 때 필요한 링크 생성
	 ***************************************/
	public EWLinkTable getRedirect(String mode,String login_id,String page) throws Exception{
		int l_maxlist = 15;
		int l_maxpage = 7;
		
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);

		String where = ewBO.getWhere(mode,login_id);
		int total = ewDAO.getTotalCount("ew_history",where);

		// 전체 페이지의 값을 구한다.
		int totalpage = (int)(total / l_maxlist);
		if(totalpage*l_maxlist  != total)
			totalpage = totalpage + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(page) - 1) / l_maxpage) * l_maxpage + 1;
		int endpage= (int)((((startpage - 1) + l_maxpage) / l_maxpage) * l_maxpage);
	
		// 페이지 이동관련 문자열을 담을 변수. 즉, [prev] [1][2][3] [next]
		String pagecut = "";
		
		int curpage = 1;
		if (totalpage <= endpage)
			endpage = totalpage;
		
		if (Integer.parseInt(page) > l_maxpage){
			curpage = startpage -1;
			pagecut = "<a href=ExtraWorkServlet?mode="+mode+"&page=" + curpage +  ">[Prev]</a>";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=ExtraWorkServlet?mode="+mode+"&page=" + curpage  +">[" + curpage + "]</a>";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=ExtraWorkServlet?mode="+mode+"&page=" + curpage + ">[Next]</a>";
		}
		
		EWLinkTable link =  new EWLinkTable();
	
		link.setViewPagecut(pagecut);
		link.setViewTotal(total);
		link.setViewBoardpage(page);
		link.setViewtotalpage(totalpage);
		
		return link;
	}

	/************
	 * 결재 처리
	 ************/
	public void ewAppInfoProcess(String ewid, String mno, String mode) throws Exception {	
		ExtraWorkModuleDAO extraWorkModuleDAO = new ExtraWorkModuleDAO(con);
		com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
		
		String status="";
		
		//결재 상신시
		if("submit".equals(mode)){
			// 1차 이면 status = "2", 2차면 status = "4" 상신
			status = "2";
			
			//특근신청건의 관리번호를 가져와서 각 건에 대해 상태코드를 업데이트한다.
			String ono_plus = extraWorkModuleDAO.getOnoPlus(mno);
			extraWorkModuleDAO.updateStatus(ono_plus,status,"");
		}

		//결재 반려시
		else if("reject".equals(mode)){
			status = "4"; // 반려 처리코드
		
			// 가져온 aid로 결재 정보 불러온후 저장하기
			appDAO.getAppInfoAndSave("ew_app_save",ewid);

			//ew_master 테이블에 전자결재 관리번호를 입력하고 상태코드를 변경한다.
			extraWorkModuleDAO.setEwid(ewid,mno,status);

			//특근신청건의 관리번호를 가져와서 각 건에 대해 상태코드를 업데이트한다.
			String ono_plus = extraWorkModuleDAO.getOnoPlus(mno);
			extraWorkModuleDAO.updateStatus(ono_plus,status,ewid);
			
			//메일발송처리
//			String decision2 = extraWorkModuleDAO.getDecision(mno); // 결재 처리자 ID가져오기
//			sendMail("",mno,decision2,"n");
		}

		//결재 승인시
		else if("approval".equals(mode)) {
			status = "3"; // 1차 이면 status = "3", 2차면 status = "5"
		
			// 가져온 aid로 결재 정보 불러온후 저장하기
			appDAO.getAppInfoAndSave("ew_app_save",ewid);

			//ew_master 테이블에 전자결재 관리번호를 입력하고 상태코드를 변경한다.
			extraWorkModuleDAO.setEwid(ewid,mno,status);

			//특근신청건의 관리번호를 가져와서 각 건에 대해 상태코드를 업데이트한다.
			String ono_plus = extraWorkModuleDAO.getOnoPlus(mno);
			extraWorkModuleDAO.updateStatus(ono_plus,status,ewid);

			//geuntae_count 테이블의 수량을 개인별,월별 특근수량을 업데이트한다.
			updateGeunTaeCount(ono_plus);

			//메일발송처리
			//String decision = extraWorkModuleDAO.getDecision(mno);			// 작성자 ID가져오기
			//sendMail("",mno,decision,"y");
		}
	}

	/******************************************
	* 선택된 특근건에 대해 정산처리를 한다.
	*******************************************/
	public void processJungsan(String login_id,String nos) throws Exception {
		ExtraWorkHistoryTable history_table = new ExtraWorkHistoryTable();
		MemberPayInfoTable member_table = new MemberPayInfoTable();;
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);

		//특근신청건별로 분리
		java.util.StringTokenizer st = new java.util.StringTokenizer(nos,";");
		int tokens_count = st.countTokens();

		//현재시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String w_time = vans.format(now);
		String w_year = w_time.substring(0,4);

		//특근정산자 정보 가져오기 
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
		user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(login_id);
		String login_name = user_info.getUserName();

		for (int j = 0 ; j<tokens_count ; j++)
		{
			String no = st.nextToken();
			//특근신청정보 가져오기
			history_table = ewDAO.getHistoryInfo(no);
			//특근시간 가져오기
			String total_time = history_table.getTotalTime();

			//정산시간 계산 - 60분 미만인 특근은 정산대상에서 제외함.
			int result_time = Integer.parseInt(total_time) / 60;
			//특근자의 시급정보 가져오기
			member_table = ewDAO.getMemberPayInfo(history_table.getMemberId(),w_year);
			String hourly_pay = member_table.getHourlyPay();
			//특근수당계산(시급*근무시간)
			String pay_by_work = Double.toString(Double.parseDouble(hourly_pay) * result_time);

			//정산결과 업데이트
			ewDAO.updateJungsanResult(no,hourly_pay,pay_by_work,login_id,login_name,w_time,Integer.toString(result_time));
		}
	}

	/******************************************
	* 개인별 월간 특근현황을 달력형태로 출력
	*******************************************/
	public ArrayList getPersonalStatusByMonth(String year,String month,String login_id) throws Exception {
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);
		ExtraWorkHistoryTable ewtable = new ExtraWorkHistoryTable();
		
		//월별 개인 특근 현황
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
		String wdate		= "";	// 년월일(20030927)
		String wcontents	= "";	// 근태 내용
		String url			= "";	// 해달 날짜에 적용할 링크 문자열
		String day			= "";	// 해당 날짜
		String ys_kind		= "";	// 근태 구분
		
		String w_sdate		= "";	// 시작일자
		String w_stime		= "";	// 시작시각
		String w_edate		= "";	// 마침일자
		String w_etime		= "";	// 마침시각
		String total_time	= "";	// 실제근무시간
		String result_time	= "";	// 정산시간
		String pay_by_work	= "";	// 특근수당
		String confirm_date	= "";	// 정산일자

		inx = ((-1) * dayno) + 2 ;


		// 달력폼의 리스트를 담을 변수 지정
		com.anbtech.ew.entity.ExtraWorkHistoryTable table_1 = new com.anbtech.ew.entity.ExtraWorkHistoryTable();
		ArrayList table_list_1 = new ArrayList();
		
		while (inx < lastday){
					
			//######일요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "명일 " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = "<font color='red'>"+inx+"(일)</font>";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;


			//######월요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "명일 " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(월)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;


			//######화요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "명일 " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(화)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;

			//######수요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "명일 " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(수)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
			}
			inx++;

			//######목요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "명일 " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(목)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;

			//######금요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "명일 " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(금)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;

			//######토요일########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "명일 " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = "<font color='blue'>"+inx+"(토)</font>";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;
		}//end while

		return table_list_1;
	}

	/******************************************
	 * 퇴근처리시 특근관련 정보를 업데이트한다.
	 *
	 * (1) ew_history 테이블 업데이트
	 *     - 실 근무마침일자 r_edate 업데이트
	 *	   - 실 근무마침시각 r_etime 업데이트
	 *     - 당직자 확인코드 ew_confirm = '6' 으로 설정
	 *      (당직자 확인절차를 생략함으로 인해 퇴근시 자동으로 확인으로 설정하되
	 *	     마침시각이 특근신청 시작시각보다 1시간 이후라야만 특근으로 인정한다.)
	 *	   - 실제 근무시간 total_time 을 계산하여 업데이트
     *
	 * (2) 특근으로 인정된 건에 한하여 특근시간을 geuntae_count 테이블에 기록(업데이트)한다.
	 ******************************************/
	public void processWorkOut(String user_id,String w_sdate,String r_edate,String r_etime) throws Exception{
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ExtraWorkHistoryTable table = new ExtraWorkHistoryTable();

		//근태신청정보를 가져온다.
		table = ewDAO.getHistoryInfo(user_id,w_sdate);

		//실제 근무시간
		long total_time = getTimeDistance(w_sdate,table.getRstime(),r_edate,r_etime);

		if(total_time >= 60){ //실제 근무시간이 한시간 이상일 경우
			ewDAO.updateHistoryInfo(Integer.toString(table.getOno()),r_edate,r_etime,"6",Long.toString(total_time));
		}else{
			ewDAO.updateHistoryInfo(Integer.toString(table.getOno()),r_edate,r_etime,"5","0");		
		}
	}


	/******************************
	 * 두 시간 사이의 시간차를 계산
	 ******************************/
	public long getTimeDistance(String w_sdate,String w_stime,String w_edate,String w_etime) throws Exception{
	   String s_time = w_sdate + " " + w_stime;
	   String e_time = w_edate + " " + w_etime;

	   java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd hh:mm"); 
	   java.util.Date s_date = dateFormat.parse(s_time);
	   java.util.Date e_date = dateFormat.parse(e_time);

	   long millisDifference = e_date.getTime() - s_date.getTime();
	   long distance = millisDifference/(1000*60);

	   return distance;
	}

	/******************************
	 * 이전(며칠전)날자 알아오기
	 * input  : 현재일자( 20031214 ),날수
	 * output : 알고 싶은 일자
	 ******************************/
	public String getWantDate( String current_date, String day, String type ) throws Exception {
	
		int input_year = Integer.parseInt(current_date.substring(0,4));
		int input_month = Integer.parseInt(current_date.substring(4,6));
		int input_day = Integer.parseInt(current_date.substring(6,8));
		
		Calendar want_date;
		String s="";
		Calendar calendar = Calendar.getInstance(); 
		//** 년월일을 세팅한다.
		want_date = Calendar.getInstance();
		want_date.set(Calendar.YEAR,input_year);
		want_date.set(Calendar.MONTH,input_month-1);
		want_date.set(Calendar.DATE,input_day);
		
        if(type.equals("-"))  want_date.add(want_date.DATE, -(Integer.parseInt(day))); 
		else if(type.equals("+")) want_date.add(want_date.DATE, Integer.parseInt(day)); 
         
        String year = String.valueOf(want_date.get(Calendar.YEAR)); 
        String month = String.valueOf(want_date.get(Calendar.MONTH)+1); 
        String day_ = String.valueOf(want_date.get(Calendar.DATE)); 
		int day_int = Integer.parseInt(day_);
		int month_int = Integer.parseInt(month);

		DecimalFormat fmt = new DecimalFormat("00");
		
		String t = (String)fmt.format(day_int);
		month = (String)fmt.format(month_int);		
		String temp = year+""+month+""+t;

		return temp;

	}

	/**************************
	 * 결재승인 시점에서 geuntae_count 테이블의 특근일수 업데이트
	 **************************/
	public void updateGeunTaeCount(String ono_plus) throws Exception {
		ExtraWorkHistoryTable table = new ExtraWorkHistoryTable();
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);
		com.anbtech.es.geuntae.db.HyuGaDayDAO hyugaDAO = new com.anbtech.es.geuntae.db.HyuGaDayDAO(con);
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
		String mid = "";

		java.util.StringTokenizer stokens = new java.util.StringTokenizer(ono_plus,";");
		int tokens_count = stokens.countTokens();
	
		for (int j = 0 ; j<tokens_count ; j++){			
			mid = stokens.nextToken();

			//특근신청정보 가져오기
			table = ewDAO.getHistoryInfo(mid);

			String member_id = table.getMemberId();
			String member_name = table.getMemberName();
			String rank_name = table.getMemberRankName();
			String division	= table.getDivision();		//내부부서코드
			String division_name = table.getDivisionName();
			String this_year = table.getWsdate().substring(0,4);
			String this_month = table.getWsdate().substring(4,6);
			String column_name = hyugaDAO.searchColumn(this_month);

			//특근대상자의 인적정보 가져오기
			user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(member_id);
			String rank_code = user_info.getArCode();	//직급코드
			String ac_code = user_info.getAcCode();		//부서코드

			//해당정보가 있는지 체크한다.
			boolean is_empty = hyugaDAO.isEmpty("EXTRAWORK","EW_001",member_id,ac_code,this_year);
			
			if(is_empty){ //없을경우 새로 추가
				String gt_id = System.currentTimeMillis() + "";
				hyugaDAO.insertCountTable(gt_id,"EXTRAWORK","EW_001",member_id,member_name,rank_code,rank_name,ac_code,division,division_name,this_year,column_name,"1");

			}else{ //있을경우 수량 업데이트
				//기존 수량 가져오기
				String[] data = new String[2];	//관리코드, 현재수량
				data = hyugaDAO.getGTCount("EXTRAWORK","EW_001",member_id,ac_code,this_year,this_month);

				String new_count = Double.toString(Double.parseDouble(data[1]) + 1);
				//업데이트
				hyugaDAO.updateCountTable(data[0],column_name,new_count);
			}

		}
	}

	/******************************************
	* 선택된 특근건을 삭제처리한다.
	*******************************************/
	public void deleteSelectedEwInfo(String nos) throws Exception {
		ExtraWorkHistoryTable history_table = new ExtraWorkHistoryTable();
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);

		//특근신청건별로 분리
		java.util.StringTokenizer st = new java.util.StringTokenizer(nos,";");
		int tokens_count = st.countTokens();

		for (int j = 0 ; j<tokens_count ; j++)
		{
			String no = st.nextToken();
			//특근신청정보 가져오기
			history_table = ewDAO.getHistoryInfo(no);

			//상태코드 가져오기
			String status = history_table.getStatus();
			
			//상태가 상신전 또는 반려된 것만 삭제처리한다.
			if(status.equals("1") || status.equals("4")){
				ewDAO.processEWDel(no);
			}
		}
	}
}
