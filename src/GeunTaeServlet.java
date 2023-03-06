import com.anbtech.es.geuntae.entity.*;
import com.anbtech.es.geuntae.db.*;
import com.anbtech.es.geuntae.business.*;
import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class GeunTaeServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;

	/********
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String tablename = "geuntae_master";
		String mode = request.getParameter("mode");
		String page = request.getParameter("page");
		String no = request.getParameter("no");
		String searchword = request.getParameter("searchword");
		String searchscope = request.getParameter("searchscope");
		String category = request.getParameter("category");

		String year = request.getParameter("y");
		String month = request.getParameter("m");
		String day = request.getParameter("d");
		
		String division = request.getParameter("div")==null?"":request.getParameter("div");
		String ys_kind = request.getParameter("k")==null?"":request.getParameter("k");
		String hd_var = request.getParameter("hd_var")==null?"":request.getParameter("hd_var");
		String sortby = request.getParameter("sortby")==null?"":request.getParameter("sortby");

		String ac_name = request.getParameter("ac_name")==null?"":request.getParameter("ac_name");
		String user_rank = request.getParameter("user_rank")==null?"":request.getParameter("user_rank");
		String user_name = request.getParameter("user_name")==null?"":request.getParameter("user_name");
		String c_end_date = "n"; // 전날 이후 계속된 연장 근무후 퇴근 (d:연장근무, n:일반퇴근(당일))

		String login_id = request.getParameter("id");		//임의로 지정할 출퇴근 대상자
		String input_time = request.getParameter("t");		//임의로 지정된 출퇴근 시각

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		if (year == null) year = vans.format(now).substring(0,4);
		if (month == null) month = vans.format(now).substring(4,6);
		if (day == null) day = vans.format(now).substring(6,8);
		
		if (mode == null) mode = "list";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

		String redirectUrl = "";

		//현재 접속중인 사용자 아이디 가져오기
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		if(login_id == null || login_id.equals("")) login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;

		String eachid = request.getParameter("eachid")==null?login_id:request.getParameter("eachid");

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////
			//근태신쳥현황 리스트
			////////////////////////////////////
			if("list".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = geuntaeDAO.getTableList(tablename,mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Table_List", table_list);

				com.anbtech.es.geuntae.business.GeunTaeLinkBO redirectBO = new com.anbtech.es.geuntae.business.GeunTaeLinkBO(con);
				com.anbtech.es.geuntae.entity.GeunTaeLink redirect = new com.anbtech.es.geuntae.entity.GeunTaeLink();
				redirect = redirectBO.getRedirect(tablename,mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/es/geuntae/list.jsp").forward(request,response);
			}

			////////////////////////////////////
			//개인 월별 근태 현황
			////////////////////////////////////
			else if("person_month".equals(mode)){
				com.anbtech.es.geuntae.business.GeunTaeBO geuntaeBO = new com.anbtech.es.geuntae.business.GeunTaeBO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);

				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
				com.anbtech.es.geuntae.db.HyuGaDayDAO hyugadayDAO = new com.anbtech.es.geuntae.db.HyuGaDayDAO(con);
				
				//개인 정보 및 년월차 잔량을 가져오기
				user_info = userinfoDAO.getUserListById(eachid);
				String year_rest	= hyugadayDAO.getPersonalHoliday(eachid,year,"HD_006",month);
				String month_rest	= hyugadayDAO.getPersonalHoliday(eachid,year,"HD_003",month);
				user_info.setHyuGaYearRestDay(year_rest);
				user_info.setHyuGaMonthRestDay(month_rest);
				request.setAttribute("User_Info", user_info);

				//월별 휴가현황 가져오기
				ArrayList table_list = new ArrayList();
				table_list = geuntaeBO.getPersonalStatusByMonth(year,month,eachid);
				request.setAttribute("Table_List", table_list);		
				getServletContext().getRequestDispatcher("/es/geuntae/personalStatusByMonth.jsp?y="+year+"&m="+month).forward(request,response);

			}

			////////////////////////////////////
			//개인 년별 근태 현황
			////////////////////////////////////
			else if("person_year".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				ArrayList table_list = new ArrayList();

				try{
					table_list = geuntaeDAO.getPersonalStatusInDeptByYear(login_id,year,hd_var,division,sortby);
				}catch( Exception e1){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('선택한 부서에는 현재 부서원이 존재하지 않습니다.');");
					out.println("   history.go(-1)");
					out.println("	</script>");
					out.close();
					return;			
				}
				request.setAttribute("Table_List", table_list);

				String url = "/es/geuntae/personalStatusByYear.jsp?div="+division+"&hd_var="+hd_var+"&y="+year;	
				getServletContext().getRequestDispatcher(url).forward(request,response);
		
			}

			////////////////////////////////////
			//부서별 근태 현황
			////////////////////////////////////
			else if("div_month".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = geuntaeDAO.getDivisionalStatusByYear(year,ys_kind);
				request.setAttribute("Table_List", table_list);
				
				String url = "/es/geuntae/divisionalStatusByMonth.jsp?y="+year+"&k="+ys_kind+"&a="+ac_name+"&r="+user_rank+"&n="+user_name;
				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			////////////////////////////////////
			//개인별 휴가 잔량을 관리(관리자 모드)
			////////////////////////////////////
			else if("manager_hyuga_day".equals(mode)){
				com.anbtech.es.geuntae.db.HyuGaDayDAO hyugadayDAO = new com.anbtech.es.geuntae.db.HyuGaDayDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = hyugadayDAO.getUserHyuGaRestDay(year,ys_kind,division);
				request.setAttribute("Table_List", table_list);

				String url = "/es/admin/managerHyuGaDay.jsp?y="+year+"&k="+ys_kind+"&div="+division;
				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			////////////////////////////////////
			//출근 시각 기록 처리
			////////////////////////////////////
			else if("chk_in".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				//해당 년월일의 출퇴근 기록이 있는지 체크한다.
				String wdate = year + month + day;
				String time = geuntaeDAO.getWorkTime(login_id,wdate);

				String time_s = time.substring(0,time.lastIndexOf("|"));			   // 출근시각
				String time_e = time.substring(time.lastIndexOf("|")+1,time.length()); // 퇴근시각

				if(time_s.equals("") || time_s==null){ // 출근시각 기록이 없으면 새로 기입한다.
					//현재 시각을 가져온다.
					java.text.SimpleDateFormat vans2 = new java.text.SimpleDateFormat("kk:mm");
					time_s = vans2.format(now);

					//임의로 지정된 시각이 있을 경우에는 입력된 시간으로 기록한다.
					if(input_time != null) time_s = input_time;

					//접속중인 클라이언트의 IP Address를 가져온다.
					String ipaddress = request.getRemoteAddr();

					//db에 저장
					geuntaeDAO.saveWorkTimeS(login_id,wdate,time_s,ipaddress);
				
				}else{					// 출근시각 기록이 이미 된 경우
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('이미 기록하셨습니다. 하루에 한번만 기록 가능합니다.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();				
				}
				redirectUrl = "GeunTaeServlet?mode=person_month&y="+year+"&m="+month;
			}

			////////////////////////////////////
			//퇴근 시각 기록 처리
			//
			//	퇴근 시각 기록시 고려된 경우
			//	============================
			//	(1)평상근무 후 퇴근하는 경우
			//	(2)특근 후 당일에 퇴근하는 경우
			//	(3)특근 후 명일(다음날)에 퇴근하는 경우
			//
			////////////////////////////////////
			else if("chk_out".equals(mode)){
				com.anbtech.es.geuntae.business.GeunTaeBO geuntaeBO = new com.anbtech.es.geuntae.business.GeunTaeBO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				com.anbtech.ew.db.ExtraWorkModuleDAO ewMDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewMBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);

				//선택된 년월일의 출퇴근 기록을 가져오기
				String wdate = year + month + day;
				String time = geuntaeDAO.getWorkTime(login_id,wdate);
				String time_s = time.substring(0,time.lastIndexOf("|"));			   // 당일 출근시각
				String time_e = time.substring(time.lastIndexOf("|")+1,time.length()); // 당일 퇴근시각

				//선택된 년월일의 전일(이전일) 출퇴근 기록 가져오기
				String yday	= geuntaeBO.getYesterday(wdate.substring(0,4),wdate.substring(4,6),wdate.substring(6,8));
				String yday_time = geuntaeDAO.getWorkTime(login_id,yday);
				String yday_time_s = yday_time.substring(0,yday_time.lastIndexOf("|"));	// 전일 출근시각
				String yday_time_e = yday_time.substring(yday_time.lastIndexOf("|")+1,yday_time.length()); // 전일 퇴근시각
				
				//현재 시각을 가져와서 시각이 정오부터 오전 6시 사이일 경우는
				//하루가 지난 퇴근으로 간주한다.
				boolean is_previous_out = false;
				java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("kk");
				int now_hour = Integer.parseInt(sf.format(now));
				
				//임의로 지정된 시각이 있을 경우에는 입력된 시간으로 판단한다.
				if(input_time != null) now_hour = Integer.parseInt(input_time.substring(0,2));

				if(now_hour > 0 && now_hour < 6) is_previous_out = true;

				///////////////////////////
				//하루가 지나서 퇴근할려고 하는 경우
				///////////////////////////
				if(is_previous_out){	
					if(yday_time_s.equals("")){ // 전일 출근시각 기록이 없으면 출근시각 기록을 먼저 하도록 유도
						PrintWriter out = response.getWriter();
						out.println("	<script>");
						out.println("	alert('출근시각 기록이 없습니다. 출근 시각 기록을 먼저 하십시요.');");
						out.println("	history.go(-1);");
						out.println("	</script>");
						out.close();
					}else{
						if(yday_time_e.equals("")){ // 퇴근시각 기록이 없으면 기록한다.
							//현재 시각을 가져온다.
							java.text.SimpleDateFormat vans2 = new java.text.SimpleDateFormat("kk:mm");
							yday_time_e = vans2.format(now);

							//임의로 지정된 시각이 있을 경우에는 입력된 시간으로 기록한다.
							if(input_time != null) yday_time_e = input_time;

							//접속중인 클라이언트의 IP Address를 가져온다.
							String ipaddress = request.getRemoteAddr();

							boolean is_over_work = ewMDAO.checkWorked(login_id,yday);
							if(is_over_work){
								con.setAutoCommit(false);	// 트랜잭션을 시작
								con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
								try{
									//특근신청이 된 경우 특근 모듈에 퇴근시각을 기록
									ewMBO.processWorkOut(login_id,yday,wdate,yday_time_e);

									//퇴근시간을 근태 관리 모듈에 저장
									geuntaeDAO.saveWorkTimeE(login_id,yday,"명일 "+yday_time_e,ipaddress);

									con.commit();
								}catch(Exception e){
									con.rollback();
									request.setAttribute("ERR_MSG",e.toString());
									getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

								}finally{
									con.setAutoCommit(true);
								}
							}else{
								//퇴근시간을 근태 관리 모듈에 저장
								geuntaeDAO.saveWorkTimeE(login_id,yday,"명일 "+yday_time_e,ipaddress);							
							}
							
							redirectUrl = "GeunTaeServlet?mode=person_month&y="+year+"&m="+month+"&eachid="+login_id;
						} else {	// 퇴근시각 기록이 이미 된 경우
							PrintWriter out = response.getWriter();
							out.println("	<script>");
							out.println("	alert('이미 기록하셨습니다. 하루에 한번만 기록 가능합니다.');");
							out.println("	history.go(-1);");
							out.println("	</script>");
							out.close();	
						}
					} 				
				////////////////////////////
				//금일 퇴근할려고 하는 경우
				////////////////////////////
				}else{					
					if(time_s.equals("")){ // 금일 출근시각 기록이 없으면 출근시각 기록을 먼저 하도록 유도
						PrintWriter out = response.getWriter();
						out.println("	<script>");
						out.println("	alert('출근시각 기록이 없습니다. 출근 시각 기록을 먼저 하십시요.');");
						out.println("	history.go(-1);");
						out.println("	</script>");
						out.close();
					}else{
						if(time_e.equals("")){ // 퇴근시각 기록이 없으면 기록한다.
							//현재 시각을 가져온다.
							java.text.SimpleDateFormat vans2 = new java.text.SimpleDateFormat("kk:mm");
							time_e = vans2.format(now);

							//임의로 지정된 시각이 있을 경우에는 입력된 시간으로 기록한다.
							if(input_time != null) time_e = input_time;


							//접속중인 클라이언트의 IP Address를 가져온다.
							String ipaddress = request.getRemoteAddr();

							boolean is_over_work = ewMDAO.checkWorked(login_id,wdate);
							if(is_over_work){
								con.setAutoCommit(false);	// 트랜잭션을 시작
								con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
								try{
									//특근신청이 된 경우 특근 모듈에 퇴근시각을 기록
									ewMBO.processWorkOut(login_id,wdate,wdate,time_e);
									//퇴근시간을 근태 관리 모듈에 저장
									geuntaeDAO.saveWorkTimeE(login_id,wdate,time_e,ipaddress);

									con.commit();
								}catch(Exception e){
									con.rollback();
									request.setAttribute("ERR_MSG",e.toString());
									getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

								}finally{
									con.setAutoCommit(true);
								}
							}else{
								//퇴근시간을 근태 관리 모듈에 저장
								geuntaeDAO.saveWorkTimeE(login_id,wdate,time_e,ipaddress);				
							}
							
							redirectUrl = "GeunTaeServlet?mode=person_month&y="+year+"&m="+month+"&eachid="+login_id;
						} else {	// 퇴근시각 기록이 이미 된 경우
							PrintWriter out = response.getWriter();
							out.println("	<script>");
							out.println("	alert('이미 기록하셨습니다. 하루에 한번만 기록 가능합니다.');");
							out.println("	history.go(-1);");
							out.println("	</script>");
							out.close();	
						}
					} 				
				}
			}
			
			////////////////////////////////////
			//개인 일별 출퇴근 시각 현황 출력
			////////////////////////////////////
			else if("work_history".equals(mode)){
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = geuntaeDAO.getWorkHistoryByDay(year,month,day,division);
				request.setAttribute("Table_List", table_list);
				getServletContext().getRequestDispatcher("/es/geuntae/workHistoryByDay.jsp?y="+year+"&m="+month+"&d="+day+"&div="+division).forward(request,response);				
			}

			//redirectUrl의 값이 있을시에는 redirectUrl경로로 이동한다.
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} // doGet()

	/**********************************
	 * post방식으로 넘어왔을 때 처리
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//MultipartRequest 크기, 저장디렉토리
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/es/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory생성하기

		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //해당 경로로 업로드한다


		//양식 종류 및 사용자 정보 (공통)
		String mode = multi.getParameter("mode");				if(mode == null) mode = "";		//데이터처리모드
		String app_mode = multi.getParameter("app_mode");		if(app_mode == null) app_mode = "";		//전자결재 처리모드	
		String user_id = multi.getParameter("user_id");			if(user_id == null) user_id = "";		//user id
		String user_name = multi.getParameter("user_name");		if(user_name == null) user_name = "";	//user_name
		String user_code = multi.getParameter("user_code");		if(user_code == null) user_code = "";	//user_rank code
		String user_rank = multi.getParameter("user_rank");		if(user_rank == null) user_rank = "";	//user_rank 
		String div_id = multi.getParameter("div_id");			if(div_id == null) div_id = "";			//div_id 
		String div_name = multi.getParameter("div_name");		if(div_name == null) div_name = "";		//div_name 
		String div_code = multi.getParameter("div_code");		if(div_code == null) div_code = "";		//div_code 
		//out.println("	<script>	alert('"+mode+"');	</script>");	out.close();

		//양식종류별 전자결재 관리항목 (등록자)
		String writer_id = multi.getParameter("writer_id");		if(writer_id == null) writer_id = "";	//등록자(대신등록자 일수도 있음)
		String writer_name = multi.getParameter("writer_name");	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid = multi.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";	//관리번호
		String doc_line = multi.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";	//결재선
		String doc_subj = multi.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";	//제목
		String doc_peri = multi.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";	//보존기간
		String doc_secu = multi.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";	//보안등급
		
		//수정용 id (임시저장결재문서 : 삭제 후 신규등록으로 처리키 위해)
		String old_id = multi.getParameter("old_id");			if(old_id == null) old_id = "";		//임시저장한 문서 관리번호

		//일반 공통	
		String writer_date = multi.getParameter("date");		if(writer_date == null) writer_date = "";	//입력일 시분
		String purpose = multi.getParameter("purpose");		if(purpose == null) purpose = "";				//사유 및 목적
		String period = multi.getParameter("period");		if(period == null) period = "";					//기간 (일수,시간 등)

		//날자 공통	
		String doc_syear = multi.getParameter("doc_syear");		if(doc_syear == null) doc_syear = "";	//시작 년도
		String doc_smonth = multi.getParameter("doc_smonth");	if(doc_smonth == null) doc_smonth = "";	//시작 월
		String doc_sdate = multi.getParameter("doc_sdate");		if(doc_sdate == null) doc_sdate = "";	//시작 일
		String doc_edyear = multi.getParameter("doc_edyear");	if(doc_edyear == null) doc_edyear = "";	//종료 년도
		String doc_edmonth = multi.getParameter("doc_edmonth");	if(doc_edmonth == null) doc_edmonth = "";//종료 월
		String doc_eddate = multi.getParameter("doc_eddate");	if(doc_eddate == null) doc_eddate = "";	//종료 일
		
		//휴(공)가원	
		String doc_huga = multi.getParameter("doc_huga");		if(doc_huga == null) doc_huga = "";		//휴가 관리코드
		String doc_receiver = multi.getParameter("doc_receiver");	if(doc_receiver == null) doc_receiver = "";	//업무인수인계자
		String doc_tel = multi.getParameter("doc_tel");			if(doc_tel == null) doc_tel = "";		//긴급연락처

		//출장신청서
		String doc_chuljang = multi.getParameter("doc_chuljang"); if(doc_chuljang == null) doc_chuljang = "";//출장 관리코드
		String fellow_names = multi.getParameter("fellow_names"); if(fellow_names == null) fellow_names = "";//동행자 사번/이름;
		String prj_code = multi.getParameter("prj_code"); 		if(prj_code == null) prj_code = "";		//프로젝트 코드
		String bistrip_kind = multi.getParameter("bistrip_kind");if(bistrip_kind == null) bistrip_kind = "국내";//출장지 구분(국내/외)
		String bistrip_country = multi.getParameter("bistrip_country"); if(bistrip_country == null) bistrip_country = "";//출장지 국가명
		String bistrip_city = multi.getParameter("bistrip_city"); if(bistrip_city == null) bistrip_city = "";//출장지 도시명
		String traffic_way = multi.getParameter("traffic_way");	if(traffic_way == null) traffic_way = "";	//교통편
		String receiver_id = multi.getParameter("receiver_id");	if(receiver_id == null) receiver_id = "";	//영수인사번
		String receiver_name = multi.getParameter("receiver_name");	if(receiver_name == null) receiver_name = "";	//영수인이름

		//2차부서 출장신청서 
		String link_id = multi.getParameter("link_id");	if(link_id == null) link_id = "";	//관련문서 번호

		//비용금액과 산출내용을 배열에 담기
		String account_cnt = multi.getParameter("account_cnt");	if(account_cnt == null) account_cnt = "0";	//account항목수
		int acnt_cnt = Integer.parseInt(account_cnt);
		String[][] cost = new String[0][3];

		//외출계
		String doc_oechul = multi.getParameter("doc_oechul"); if(doc_oechul == null) doc_oechul = "";		//외출 관리코드
		String StartTime = multi.getParameter("hdStartTime"); if(StartTime == null) StartTime = "";			//시작시간
		String EndTime = multi.getParameter("hdEndTime"); if(EndTime == null) EndTime = "";					//종료시간
		String time_period = StartTime+"~"+EndTime;	//외출시간
		String dest = multi.getParameter("dest"); if(dest == null) dest = "";								//목적지

		if(acnt_cnt > 0) {
			int ac_cnt = acnt_cnt - 1;
			cost = new String[ac_cnt][3];
			String c_code = "";		//출장코드
			String c_cost = "";		//출장비용
			String c_cont = "";		//산출내용
			for(int c=0,m=1; c < ac_cnt; c++,m++) {
				c_code = "code"+m;
				c_cost = "cost"+m;
				c_cont = "cont"+m;
				cost[c][0] = multi.getParameter(c_code);	if(cost[c][0] == null) cost[c][0] = "";		//코드	
				cost[c][1] = multi.getParameter(c_cost);	if(cost[c][1] == null) cost[c][1] = "0";	//비용	
				cost[c][2] = multi.getParameter(c_cont);	if(cost[c][2] == null) cost[c][2] = "";		//내용
			}
		}

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////
			//휴(공)가원 상신
			////////////////////////////////////
			if ("HYU_GA".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","휴가원",doc_pid,"HYU_GA");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (휴가원)
					geuntaeDAO.setGeuntaeHdyTable(doc_pid,"HYU_GA",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,doc_syear,doc_smonth,doc_sdate,
						doc_edyear,doc_edmonth,doc_eddate,purpose,period,doc_huga,doc_receiver,doc_tel);
					
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit한다.

					//처리메시지 출력하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//휴(공)가원 임시보관
			////////////////////////////////////
			else if("HYU_GA_TMP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","휴가원",doc_pid,"HYU_GA");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (휴가원)
					geuntaeDAO.setGeuntaeHdyTable(doc_pid,"HYU_GA",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,doc_syear,doc_smonth,doc_sdate,
						doc_edyear,doc_edmonth,doc_eddate,purpose,period,doc_huga,doc_receiver,doc_tel);
					
					con.commit(); // commit한다.

					//처리메시지 출력하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//휴(공)가원 임시보관 재 상신 
			////////////////////////////////////
			else if ("R_HYU_GA".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//관리번호,login_id,root_path

					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","휴가원",doc_pid,"HYU_GA");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (휴가원)
					geuntaeDAO.setGeuntaeHdyTable(doc_pid,"HYU_GA",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,doc_syear,doc_smonth,doc_sdate,
						doc_edyear,doc_edmonth,doc_eddate,purpose,period,doc_huga,doc_receiver,doc_tel);
					
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit한다.

					//처리메시지 출력하기
					//out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////			
			//휴(공)가원 임시보관 재 임시보관
			////////////////////////////////////
			else if("R_HYU_GA_TMP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//관리번호,login_id,root_path

					//------------------------------
					// 신규 재저장
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","휴가원",doc_pid,"HYU_GA");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (휴가원)
					geuntaeDAO.setGeuntaeHdyTable(doc_pid,"HYU_GA",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,doc_syear,doc_smonth,doc_sdate,
						doc_edyear,doc_edmonth,doc_eddate,purpose,period,doc_huga,doc_receiver,doc_tel);
					
					con.commit(); // commit한다.

					//처리메시지 출력하기
					//out.println("<script>	alert('저장되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}
			}
			
			////////////////////////////////////
			//출장신청서 상신
			////////////////////////////////////
			else if ("CHULJANG_SINCHEONG".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","출장신청서",doc_pid,"CHULJANG_SINCHEONG");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (출장신청서)
					geuntaeDAO.setGeuntaeBisTripTable(doc_pid,"","CHULJANG_SINCHEONG",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,prj_code,doc_syear,doc_smonth,doc_sdate,doc_edyear,doc_edmonth,doc_eddate,bistrip_city,bistrip_kind,bistrip_country,traffic_way,purpose,"","","",period,doc_chuljang,receiver_id,receiver_name,doc_receiver,doc_tel);
					//게정 TABLE (geuntae_account)에 입력하기 (출장신청서)
					for(int cj=0,cid=1; cj<cost.length; cj++,cid++) {
						//자코드 만들기
						String doc_cid = doc_pid + "_" + cid;
						if(!cost[cj][1].equals("000"))
							geuntaeDAO.setGeuntaeBisTripAccountTable(doc_pid,doc_cid,cost[cj][0],cost[cj][1],cost[cj][2]);
					}
					
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit한다.

					//처리메시지 출력하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//출장신청서 임시보관
			////////////////////////////////////
			else if ("CHULJANG_SINCHEONG_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","출장신청서",doc_pid,"CHULJANG_SINCHEONG");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (출장신청서)
					geuntaeDAO.setGeuntaeBisTripTable(doc_pid,"","CHULJANG_SINCHEONG",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,
							fellow_names,prj_code,doc_syear,doc_smonth,doc_sdate,doc_edyear,doc_edmonth,doc_eddate,
							bistrip_city,bistrip_kind,bistrip_country,traffic_way,purpose,"","","",period,doc_chuljang,receiver_id,receiver_name,doc_receiver,doc_tel);
					//게정 TABLE (geuntae_account)에 입력하기 (출장신청서)
					for(int cj=0,cid=1; cj<cost.length; cj++,cid++) {
						//자코드 만들기
						String doc_cid = doc_pid + "_" + cid;
						if(!cost[cj][1].equals("000"))
							geuntaeDAO.setGeuntaeBisTripAccountTable(doc_pid,doc_cid,cost[cj][0],cost[cj][1],cost[cj][2]);
					}
				
					con.commit(); // commit한다.

					//처리메시지 출력하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//출장신청서 재상신
			////////////////////////////////////
			else if ("R_CHULJANG_SINCHEONG".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//관리번호,login_id,root_path

					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","출장신청서",doc_pid,"CHULJANG_SINCHEONG");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (출장신청서)
					geuntaeDAO.setGeuntaeBisTripTable(doc_pid,"","CHULJANG_SINCHEONG",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,
							fellow_names,prj_code,doc_syear,doc_smonth,doc_sdate,doc_edyear,doc_edmonth,doc_eddate,
							bistrip_city,bistrip_kind,bistrip_country,traffic_way,purpose,"","","",period,doc_chuljang,receiver_id,receiver_name,doc_receiver,doc_tel);
					//게정 TABLE (geuntae_account)에 입력하기 (출장신청서)
					for(int cj=0,cid=1; cj<cost.length; cj++,cid++) {
						//자코드 만들기
						String doc_cid = doc_pid + "_" + cid;
						if(!cost[cj][1].equals("000"))
							geuntaeDAO.setGeuntaeBisTripAccountTable(doc_pid,doc_cid,cost[cj][0],cost[cj][1],cost[cj][2]);
					}
					
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit한다.

					//처리메시지 출력하기
					//out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//출장신청서 재임시저장
			////////////////////////////////////
			else if ("R_CHULJANG_SINCHEONG_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//관리번호,login_id,root_path

					//------------------------------
					// 신규 재임시저장
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","출장신청서",doc_pid,"CHULJANG_SINCHEONG");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (출장신청서)
					geuntaeDAO.setGeuntaeBisTripTable(doc_pid,"","CHULJANG_SINCHEONG",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,
							fellow_names,prj_code,doc_syear,doc_smonth,doc_sdate,doc_edyear,doc_edmonth,doc_eddate,
							bistrip_city,bistrip_kind,bistrip_country,traffic_way,purpose,"","","",period,doc_chuljang,receiver_id,receiver_name,doc_receiver,doc_tel);
					//게정 TABLE (geuntae_account)에 입력하기 (출장신청서)
					for(int cj=0,cid=1; cj<cost.length; cj++,cid++) {
						//자코드 만들기
						String doc_cid = doc_pid + "_" + cid;
						if(!cost[cj][1].equals("000"))
							geuntaeDAO.setGeuntaeBisTripAccountTable(doc_pid,doc_cid,cost[cj][0],cost[cj][1],cost[cj][2]);
					}
					
					con.commit(); // commit한다.

					//처리메시지 출력하기
					//out.println("<script>	alert('저장되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//출장신청서 2차부서 상신
			////////////////////////////////////
			else if ("CHULJANG_SINCHEONG_SEC".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				
				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","출장신청서",link_id,"CHULJANG_SINCHEONG");
					
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("geuntae_master","flag2","gt_id",link_id,"EE");
					con.commit(); // commit한다.

					//처리메시지 출력하기
					//out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//외출계 상신
			////////////////////////////////////
			else if ("OE_CHUL".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","외출계",doc_pid,"OE_CHUL");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (외출계)
					geuntaeDAO.setGeuntaeOutTable(doc_pid,"OE_CHUL",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,doc_syear,doc_smonth,doc_sdate,
						dest,traffic_way,purpose,time_period,doc_oechul,doc_receiver,doc_tel);
					
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit한다.

					//처리메시지 출력하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//외출계 임시저장
			////////////////////////////////////
			else if ("OE_CHUL_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","외출계",doc_pid,"OE_CHUL");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (외출계)
					geuntaeDAO.setGeuntaeOutTable(doc_pid,"OE_CHUL",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,doc_syear,doc_smonth,doc_sdate,
						dest,traffic_way,purpose,time_period,doc_oechul,doc_receiver,doc_tel);
					
					con.commit(); // commit한다.

					//처리메시지 출력하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//외출계 재상신
			////////////////////////////////////
			else if ("R_OE_CHUL".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//관리번호,login_id,root_path

					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","외출계",doc_pid,"OE_CHUL");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (외출계)
					geuntaeDAO.setGeuntaeOutTable(doc_pid,"OE_CHUL",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,doc_syear,doc_smonth,doc_sdate,
						dest,traffic_way,purpose,time_period,doc_oechul,doc_receiver,doc_tel);
					
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("geuntae_master","flag","gt_id",doc_pid,"EE");
					con.commit(); // commit한다.

					//처리메시지 출력하기
					//out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

			////////////////////////////////////
			//외출계 재 임시저장
			////////////////////////////////////
			else if ("R_OE_CHUL_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//관리번호,login_id,root_path

					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","외출계",doc_pid,"OE_CHUL");
					
					//근태관리 TABLE (geuntae_master) 입력하기 (외출계)
					geuntaeDAO.setGeuntaeOutTable(doc_pid,"OE_CHUL",user_id,user_name,user_code,user_rank,div_id,div_code,div_name,fellow_names,doc_syear,doc_smonth,doc_sdate,
						dest,traffic_way,purpose,time_period,doc_oechul,doc_receiver,doc_tel);
					
					con.commit(); // commit한다.

					//처리메시지 출력하기
					//out.println("<script>	alert('저장되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			//con소멸
			close(con);
		}
	} //doPost()
}