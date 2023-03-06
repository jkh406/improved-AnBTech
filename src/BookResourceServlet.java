import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.br.entity.CarInfoTable;
import com.anbtech.br.entity.CarUseInfoTable;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;


public class BookResourceServlet extends HttpServlet {

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
		String login_id			= sl.id;
		String login_name		= sl.name;
		String login_division	= sl.division;
		String redirectUrl		= "";

		String year = request.getParameter("y");
		String month = request.getParameter("m");
		String day = request.getParameter("d");

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (year == null) year = vans.format(now).substring(0,4);
		if (month == null) month = vans.format(now).substring(5,7);
		if (day == null) day = vans.format(now).substring(8,10);

		String cid			= request.getParameter("cid")==null?"":request.getParameter("cid");
		String cr_id		= request.getParameter("cr_id")==null?"":request.getParameter("cr_id");
		String st			= request.getParameter("st")==null?"":Hanguel.toHanguel(request.getParameter("st"));
		String chg_cont		= request.getParameter("chg_cont")==null?"":Hanguel.toHanguel(request.getParameter("chg_cont"));

		String tablename	= request.getParameter("tablename")==null?"":request.getParameter("tablename");
		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page")==null?"1":request.getParameter("page");
		String no			= request.getParameter("no");
		String searchword	= request.getParameter("searchword")==null?"":request.getParameter("searchword");
		String searchscope	= request.getParameter("searchscope")==null?"":request.getParameter("searchscope");
		String category		= request.getParameter("category");

		String user_id		= request.getParameter("user_id")==null?login_id:request.getParameter("user_id");
		String write_id		= request.getParameter("write_id")==null?login_id:request.getParameter("write_id");
		String user_name	= request.getParameter("user_name")==null?login_name:request.getParameter("user_name");
		String mgr_name		= request.getParameter("mgr_name")==null?login_name:request.getParameter("mgr_name");
		String mgr_id		= request.getParameter("mgr_id")==null?login_id:request.getParameter("mgr_id");
		String return_date  = request.getParameter("return_date");
		if(return_date==null) return_date = vans.format(now);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//차량 관리
			if(category.equals("car")){
				
				/////////////////////////////////
				// 차량 리스트(관리자 모드)
				/////////////////////////////////
				if(mode.equals("list")){ 
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					ArrayList car_list = new ArrayList();

					car_list = carDAO.getAllCarList();
					request.setAttribute("CarList", car_list);
					
					getServletContext().getRequestDispatcher("/br/admin/managerCar.jsp").forward(request,response);
				}

				/////////////////////////////////
				// 신규 차량 등록 (관리자 모드)
				/////////////////////////////////
				else if(mode.equals("write_car")){ 

					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();

					request.setAttribute("CarInfo", carinfoTable);
					getServletContext().getRequestDispatcher("/br/admin/add_car.jsp?model=write_car").forward(request,response);
				}

				/////////////////////////////////
				// 차량 정보 상세 보기 및 수정(관리자)
				/////////////////////////////////
				else if(mode.equals("view_detail")){ 
					
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();

					carinfoTable = carDAO.getCarInfo(cid);
					request.setAttribute("CarInfo", carinfoTable);
					
					getServletContext().getRequestDispatcher("/br/admin/add_car.jsp?mode=modify_car").forward(request,response);
				}

				/////////////////////////////////
				// 월별 각 차량 예약현황 보기
				/////////////////////////////////
				else if(mode.equals("view_stat")){ 
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
						
					// 해당 달의 총 일수를 구한다.
					int total_day = carDAO.getDaysInMonth(Integer.parseInt(month),Integer.parseInt(year));
					// 등록 차량 확인 
					int car_no = carDAO.getTotalCount("car_info","");

					ArrayList car_list = new ArrayList();
	
					// 등록 차량이 있으면, 등록차량 정보 가져오기
					if(car_no>0){ car_list = carDAO.getResourceStat(year,month); }
					request.setAttribute("ResourceList", car_list);
					
					String url = "/br/car/viewBookingStat.jsp?y="+year+"&m="+month+"&td="+total_day+"&cid="+cid;
					getServletContext().getRequestDispatcher(url).forward(request,response);
				}

				/////////////////////////////////
				// 각 차량 정보 ( 차 정보 & 차 이력)
				/////////////////////////////////
				else if(mode.equals("eachcar")){ 
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();
					
					//페이징처리
					com.anbtech.br.business.CarLinkBO redirectBO = new com.anbtech.br.business.CarLinkBO(con);
					com.anbtech.br.entity.CarLinkTable redirect = new com.anbtech.br.entity.CarLinkTable();
					redirect = redirectBO.getRedirect(tablename,mode,year,searchscope,category,page,login_id,cid); 
					request.setAttribute("Redirect",redirect);

					String view_total = redirect.getViewTotal();
					String view_boardpage = redirect.getViewBoardpage();
					String view_totalpage = redirect.getViewTotalpage();
					
					//차량 사용이력 가져오기
					ArrayList eachcar_list = new ArrayList();
					eachcar_list = (ArrayList)carResourceDAO.getEachCarHistory(cid, page ,view_total,year);
					request.setAttribute("eachcar_list", eachcar_list);

					//차량정보 가져오기	
					carinfoTable = carResourceDAO.getCarInfo(cid);
					request.setAttribute("each_info", carinfoTable);

					String url = "/br/car/eachCarInfo.jsp?login_id="+login_id+"&year="+year;
					getServletContext().getRequestDispatcher(url).forward(request,response);
				}

				/////////////////////////////////
				// 예약신청등록폼
				/////////////////////////////////
				else if(mode.equals("add_lending")){
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();
				
					carinfoTable = carResourceDAO.getCarInfo(cid);
					request.setAttribute("Table", carinfoTable);
					String user=user_id+"/"+user_name;

					String url = "/br/car/lendingCar.jsp?user_id="+user_id+"&user_name="+user+"&year="+year+"&month="+month+"&day="+day;
					getServletContext().getRequestDispatcher(url).forward(request,response);

				}

				/////////////////////////////////
				// 예약신청 취소 처리(by 관리자)
				/////////////////////////////////
				else if(mode.equals("lending_cancel")) {
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					carResourceDAO.cancelBooking(cr_id);
												
					redirectUrl = "BookResourceServlet?category=car&mode=eachcar&cid="+cid+"&tablename=charyang_master";
				}

				/////////////////////////////////
				// 배차 처리
				/////////////////////////////////
				else if(mode.equals("lending_Process")) { 
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					carDAO.lendingProcess(cr_id,mgr_id,mgr_name); // 호출 : 배차처리 method 
					
					redirectUrl = "BookResourceServlet?category=car&mode=eachcar&cid="+cid+"&tablename=charyang_master";
				}

				/////////////////////////////////
				// 배차된 차량의 입고처리 폼
				/////////////////////////////////
				else if(mode.equals("entering_view")){
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarUseInfoTable caruseTable = new com.anbtech.br.entity.CarUseInfoTable();
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();

					caruseTable = carResourceDAO.getEachCarUseInfo(cr_id,login_id,login_name);
					request.setAttribute("each_lending_info", caruseTable);

					carinfoTable = carResourceDAO.getCarInfo(cid);
					request.setAttribute("each_car_info", carinfoTable);

					getServletContext().getRequestDispatcher("/br/car/enteringCar.jsp").forward(request,response);
				}

				/////////////////////////////////
				// 차량예약정보 및 입고정보 상세보기
				/////////////////////////////////
				else if(mode.equals("carinfo_view")){
					com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.entity.CarUseInfoTable caruseTable = new com.anbtech.br.entity.CarUseInfoTable();
					com.anbtech.br.entity.CarInfoTable carinfoTable = new com.anbtech.br.entity.CarInfoTable();

					caruseTable = carResourceDAO.getEachCarUseInfo(cr_id,login_id,login_name);
					request.setAttribute("each_lending_info", caruseTable);

					carinfoTable = carResourceDAO.getCarInfo(cid);
					request.setAttribute("each_car_info", carinfoTable);

					getServletContext().getRequestDispatcher("/br/car/carInfo.jsp").forward(request,response);
				}

				/////////////////////////////////
				// 배차된 차량의 입고처리(status == 7)
				/////////////////////////////////
				else if(mode.equals("entering_Process")) {
					com.anbtech.br.business.CarResourceBO carBO = new com.anbtech.br.business.CarResourceBO(con);
					carBO.enteringProcess(cid,cr_id,mgr_id,mgr_name,st,chg_cont,return_date);

					redirectUrl = "BookResourceServlet?category=car&mode=eachcar&cid="+cid+"&tablename=charyang_master";
				}

				/////////////////////////////////
				// 1차결재완료 후 2차결재대상 리스트 출력
				/////////////////////////////////
				else if(mode.equals("req_list")){ 
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					tablename = "charyang_master";
					ArrayList car_list = new ArrayList();
					car_list = carDAO.getFirstFlagList(tablename,mode,searchword,searchscope,category,page,login_id,cid);
					
					request.setAttribute("FirstFlag",car_list);
						
					com.anbtech.br.business.CarLinkBO redirectBO = new com.anbtech.br.business.CarLinkBO(con);
					com.anbtech.br.entity.CarLinkTable redirect = new com.anbtech.br.entity.CarLinkTable();
					redirect = redirectBO.getRedirect(tablename,mode,searchword,searchscope,category,page,login_id,cid);
					request.setAttribute("Redirect",redirect); 

					getServletContext().getRequestDispatcher("/br/car/firstAppCarList.jsp").forward(request,response);
				}
			}

			//회의실 관리
			else if(category.equals("meetroom")){
			}
			//교육실 관리
			else if(category.equals("eduroom")){
			}
			//프로젝터 빔 관리
			else if(category.equals("projector")){
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

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/br/";
		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

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
		String login_id		= sl.id;
		String login_name	= sl.name;
		String redirectUrl	= "";

		//내부 파리미터
		String tablename	= multi.getParameter("tablename");
		String mode			= multi.getParameter("mode");
		String page			= multi.getParameter("page")==null?"0":multi.getParameter("page");
		String no			= multi.getParameter("no");
		String searchword	= multi.getParameter("searchword")==null?"":multi.getParameter("searchword");
		String searchscope	= multi.getParameter("searchscope")==null?"":multi.getParameter("searchscope");
		String category		= multi.getParameter("category");

		//데이터 파리미터들(공통)
		String cid				= multi.getParameter("cid");			// 각 자원의 고유번호
		String buy_date			= multi.getParameter("buy_date");		// 구입일
		String price			= multi.getParameter("price");			// 구입가
		String model_name		= multi.getParameter("model_name");		// 모델명
		String maker_company	= multi.getParameter("maker_company");	// 제조회사명
		String stat				= multi.getParameter("stat");			// 자원의 현재 상태
		
		//차량관리에 필요한 파라리터들
		String car_type			= multi.getParameter("car_type");		// 차종(승용/승합/트럭)
		String car_no			= multi.getParameter("car_no");			// 차량번호
		String produce_year		= multi.getParameter("produce_year");	// 년식
		String fuel_type		= multi.getParameter("fuel_type");		// 연료구분
		String fuel_efficiency	= multi.getParameter("fuel_efficiency");// 연비
		String car_id			= multi.getParameter("car_id");			// 차량관리번호(회사 내부에서 관리되어질 경우)

		//배차시 사용자 정보들
		String user_id			= multi.getParameter("user_id");		// 사용자 id (작성자) - 사번
		String user_name		= multi.getParameter("user_name");		// 사용자 이름
		
		//사용자 정보
		String user_code		= multi.getParameter("user_code");		// 사용자 직급 코드
		String user_rank		= multi.getParameter("user_rank");		// 사용자 직급
		String ac_id			= multi.getParameter("ac_id");			// 사용자 부서관리번호
		String ac_code			= multi.getParameter("ac_code");		// 사용자 부서코드
		String ac_name			= multi.getParameter("ac_name");		// 사용자 부서명

		if(user_name!=null && (user_name.indexOf('/')<0)) {
			java.util.StringTokenizer token = new java.util.StringTokenizer(user_name,"/");
			int temp=token.countTokens();
			while(token.hasMoreTokens()){
				user_id=token.nextToken();
				user_name=token.nextToken();
			}
		}

		// 배차 차량 정보들
		String cr_id			= multi.getParameter("cr_id");			// 관리번호(년월일시분초)
		String fellow_names		= multi.getParameter("fellow_names");	// 동행자 사번/이름

		String sdate			= multi.getParameter("sdate");			// 시작날짜
		String edate			= multi.getParameter("edate");			// 완료날짜
		String stime			= multi.getParameter("stime");			// 시작시간
		String etime			= multi.getParameter("etime");			// 완료시간
		String write_id			= multi.getParameter("write_id");
		String write_name		= multi.getParameter("write_name");

		String v_status			= multi.getParameter("v_status");		// 차량자원상태
		String u_year			= multi.getParameter("u_year");			// 배차요청시간(년)
		String u_month			= multi.getParameter("u_month");		// 배차요청시간(월)
		String u_date			= multi.getParameter("u_date");			// 배차요청시간(일)
		String u_time			= multi.getParameter("u_time");			// 배차요청시간(시)
		String tu_year			= multi.getParameter("tu_year");		// 반납요청시간(년)
		String tu_month			= multi.getParameter("tu_month");		// 반납요청시간(월)
		String tu_date			= multi.getParameter("tu_date");		// 반납요청시간(일)
		String tu_time			= multi.getParameter("tu_time");		// 반납요청시간(시)
		String cr_purpose		= multi.getParameter("cr_purpose");		// 목적
		String cr_dest			= multi.getParameter("cr_dest");		// 행선지
		String content			= multi.getParameter("content");		// 상세내용(업무내용)
		String return_date		= multi.getParameter("return_date");	// 반납일자(년/월/일/시)
		String mgr_id			= multi.getParameter("mgr_id")==null?login_id:multi.getParameter("mgr_id");	// 반납확인자 ID
		String mgr_name			= multi.getParameter("mgr_name")==null?login_name:multi.getParameter("mgr_name");// 반납확인자 이름
		String chg_cont			= multi.getParameter("chg_cont");		// 변경사유
		String c_year			= multi.getParameter("c_year");			// 반납연장시간(년)
		String c_month			= multi.getParameter("c_month");		// 반납연장시간(월)
		String c_date			= multi.getParameter("c_date");			// 반납연장시간(일)
		String c_time			= multi.getParameter("c_time");			// 반납연장시간(시)
		String md_date			= multi.getParameter("md_date");		// 반납연장 수정일(년/월/일/시)
		String em_tel			= multi.getParameter("em_tel");			// 긴급연락처
		String del_date			= multi.getParameter("del_date");		// 삭제일자
		
		String doc_id			= multi.getParameter("doc_id");			// 문서관리번호
		String ys_kind			= multi.getParameter("ys_kind");		// BAE_CHA

		String file_path		= multi.getParameter("file_path");		// 참부파일저장path
		String fname			= multi.getParameter("fname");			// 첨부서류원래이름
		String ftype			= multi.getParameter("ftype");			// 첨부서류확장자명
		String fsize			= multi.getParameter("fsize");			// 첨부화일size
		
		String flag				= multi.getParameter("flag")==null?"":multi.getParameter("flag");	// 1차주관부서(결재flag)
		String flag2			= multi.getParameter("flag2")==null?"":multi.getParameter("flag2");;// 2차주관부서(결재flag)

		//저장 처리시 입력된 날짜 분리
		if(sdate!=null) {						
			  u_year = sdate.substring(0,4);
			  u_month = sdate.substring(4,6);
			  u_date = sdate.substring(6,8);
			  u_time = stime;
		}

		if(edate!=null) {
			  tu_year = edate.substring(0,4);
			  tu_month = edate.substring(4,6);
			  tu_date = edate.substring(6,8);
			  tu_time = etime;
		}

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			if(category.equals("car")){
				//////////////////////////////////
				// 신규 차량 등록처리
				//////////////////////////////////
				if(mode.equals("write_car")){
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					carDAO.saveNewCarInfo(car_type,car_no,model_name,produce_year,buy_date,price,fuel_type,fuel_efficiency,maker_company,car_id);

					redirectUrl = "BookResourceServlet?category=car&mode=list";
				}

				//////////////////////////////////
				// 차량정보 수정처리
				//////////////////////////////////
				else if(mode.equals("modify_car")){
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					carDAO.updateCarInfo(cid,car_type,car_no,model_name,produce_year,buy_date,price,fuel_type,fuel_efficiency,maker_company,car_id,stat);
					redirectUrl = "BookResourceServlet?category=car&mode=list";
				}

				//////////////////////////////////
				// 차량예약정보 등록처리
				//////////////////////////////////
				else if(mode.equals("lendingsave")){
					com.anbtech.br.db.CarResourceDAO carDAO = new com.anbtech.br.db.CarResourceDAO(con);
					com.anbtech.br.business.CarResourceBO carBO = new com.anbtech.br.business.CarResourceBO(con);

					com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);

					//개인 정보 com.anbtech.admin.entity
					com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
					user_info = userinfoDAO.getUserListById(user_id);
					
					user_name = user_info.getUserName();		//해당자 명
					user_code = user_info.getArCode();			//해당자 직급 code
					user_rank = user_info.getUserRank();		//해당자 직급명
					ac_id = user_info.getAcId();				//해당자 부서명 관리코드
					ac_name = user_info.getDivision();			//해당자 부서명 
					ac_code = user_info.getAcCode();			//부터코드
				
					String startdate_str	= sdate+stime.substring(0,2)+stime.substring(3,5);
					String enddate_str		= edate+etime.substring(0,2)+etime.substring(3,5);
					
					//예약가능날짜인가 체크(날짜 유효성 및 예약중복 체크)
					String check_msg ="";
					check_msg = carBO.checkLending(startdate_str,enddate_str,cid);
					
					if(!check_msg.equals(""))
					{
						PrintWriter out = response.getWriter();
						out.println("	<script>");
						out.println("	alert('"+check_msg+"');");
						out.println("	history.back()");
						out.println("	</script>");
						out.close();
						return;
					}							
					carDAO.saveLendingCar(cr_id,cid,write_id,write_name,user_id,user_name,user_code,user_rank,ac_id,ac_code,ac_name,fellow_names,u_year,u_month,u_date,u_time,tu_year,tu_month,tu_date,tu_time,cr_purpose,cr_dest,content,em_tel,flag,flag2);
					
					redirectUrl = "../gw/approval/module/baecha_sincheong_FP_App.jsp?link_id="+cr_id;
				}
			}

			//회의실 관리
			else if(category.equals("meetroom")){
			}
			//교육실 관리
			else if(category.equals("eduroom")){
			}
			//프로젝터 빔 관리
			else if(category.equals("projector")){
			}

			//redirectUrl의 값이 있을시에는 redirectUrl경로로 이동한다.
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	}
}
