import com.anbtech.ew.entity.*;
import com.anbtech.ew.db.*;
import com.anbtech.ew.business.*;
import com.anbtech.admin.*;
import com.anbtech.admin.entity.*;
import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ExtraWorkServlet extends HttpServlet {

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

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		// 검색시에 넘어오는 파라미터들
		String mode			= request.getParameter("mode")==null?"":request.getParameter("mode"); 
		String searchword	= Hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");
		String tablename	= "";
		String page			= request.getParameter("page");

		if ( page == null) page = "1";
		if ( searchword == null) searchword = "";
		if ( searchscope == null) searchscope = "";
		
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
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;
		String redirectUrl = "";

		//시급정보관리관련 파라미터들
		String div		= request.getParameter("div")==null?"":request.getParameter("div");
		String year		= request.getParameter("y");
		String month	= request.getParameter("m");
		String day		= request.getParameter("d");	
		
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		if (year == null) year = vans.format(now).substring(0,4);
		if (month == null) month = vans.format(now).substring(4,6);
		if (day == null) day = vans.format(now).substring(6,8);		
		
		//결재상신관련 파라미터들
		String ono_plus = request.getParameter("ono_plus");		//신청건의 관리번호들,세미콜론(;)로구분
		String mno		= request.getParameter("mno");			//ew_master 테이블 관리번호
		
		//특근현황보기관련 파라미터들
		String eachid	= request.getParameter("eachid")==null?login_id:request.getParameter("eachid");

		//인쇄보기시 필요한 파라미터들
		String o_no		= request.getParameter("o_no");			//ew_history 테이블 관리번호

		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			///////////////////////////////////////////////
			// 기준근무시간정보 보기 및 수정 폼
			///////////////////////////////////////////////
			if("standard_wtime_fix".equals(mode)){
				com.anbtech.ew.entity.StandardWorkTimeTable swtimeTable =  new com.anbtech.ew.entity.StandardWorkTimeTable();
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				
				swtimeTable = ewDAO.getStandardWTime();
								
				String temp = ""+swtimeTable.getSwNo();				// 현재 기준시간 가져오기
				if(temp==null || temp.equals("")) div = "first";	// 등록된 기준시간이 없으면 최초

				request.setAttribute("workTime",swtimeTable);
				getServletContext().getRequestDispatcher("/ew/admin/workTimeFix.jsp?div="+div).forward(request,response);
			}

			////////////////////////////////////
			// 개인별 시간외근무 시급수당 관리
			////////////////////////////////////
			else if("manager_hourly_pay".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = ewDAO.getUserPayInfo(year,div);
				request.setAttribute("Table_List", table_list);

				String url = "/ew/admin/manager_hourly_pay.jsp?y="+year+"&div="+div;
				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			/////////////////////////////////////////////////
			// 특근신청 폼
			/////////////////////////////////////////////////
			else if("req_extrawork".equals(mode)){
			
				com.anbtech.ew.db.ExtraWorkModuleDAO ewModule = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.entity.StandardWorkTimeTable swtimeTable = new com.anbtech.ew.entity.StandardWorkTimeTable();

				// 근무 시간 정보 가져오기 
				swtimeTable = (com.anbtech.ew.entity.StandardWorkTimeTable)ewModule.getStandardWTime();
				request.setAttribute("swtimeTable",swtimeTable);
				getServletContext().getRequestDispatcher("/ew/user/reqWorkForm.jsp").forward(request,response);
			}			

			///////////////////////////////////////////////////
			// 개인별 특근신청현황 리스트
			///////////////////////////////////////////////////
			else if("eachEwList".equals(mode)){
				
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				com.anbtech.ew.entity.StandardWorkTimeTable swTimeTable = new com.anbtech.ew.entity.StandardWorkTimeTable();

				ArrayList arry = new ArrayList();
				arry = ewDAO.getMyEwReqList(mode,login_id,page);
				request.setAttribute("ew_array",arry);

				com.anbtech.ew.entity.EWLinkTable ewLinkTable = new com.anbtech.ew.entity.EWLinkTable();
				ewLinkTable = ewBO.getRedirect(mode,login_id,page);
				request.setAttribute("Redirect",ewLinkTable);

			 	getServletContext().getRequestDispatcher("/ew/user/eachEwList.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// 부서별 결재상신대상 특근신청건 리스트 가져오기
			/////////////////////////////////////////////////////
			else if("ewReqList".equals(mode)){
				
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.entity.ExtraWorkHistoryTable ewHistoryTable = new com.anbtech.ew.entity.ExtraWorkHistoryTable();

				ArrayList arry = new ArrayList();
				arry = (ArrayList)ewDAO.getReqList(mode,div);
				request.setAttribute("ew_array",arry);
	
				getServletContext().getRequestDispatcher("/ew/admin/ewReqList.jsp?div=" + div).forward(request,response);
			}

			/////////////////////////////////////////////////////////
			// 전자결재용 보기 화면
			/////////////////////////////////////////////////////////
			else if("ew_req_view".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				
				if(mno != null && ono_plus == null)	ono_plus = ewDAO.getOnoPlus(mno);

				//특근신청대상자 리스트 가져오기
				ArrayList arry = new ArrayList();
				arry = (ArrayList)ewDAO.getWorkerList(ono_plus);	
				request.setAttribute("appArry",arry);

			    getServletContext().getRequestDispatcher("/ew/admin/appEwView.jsp?mno="+mno).forward(request,response);
			}			
			
			///////////////////////////////////////////////////////
			// 부서별 특근수당 정산대상 현황 출력
			///////////////////////////////////////////////////////
			else if("ew_process_list".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ArrayList arry = new ArrayList();
	
				// 부서별 정산처리 대상 가져오기
				arry = ewDAO.getPewInfoInDivision2(mode,div);
				request.setAttribute("arry",arry);

			    getServletContext().getRequestDispatcher("/ew/admin/divisionEWProcessList.jsp?div=" + div).forward(request,response);
			}
			
			//////////////////////////////////////////////////////
			//  특근 정산처리
			/////////////////////////////////////////////////////
			else if("process_jungsan".equals(mode)) {
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ewBO.processJungsan(login_id,ono_plus);

				response.sendRedirect("../servlet/ExtraWorkServlet?mode=ew_process_list&div="+div);
			}
			
			///////////////////////////////////////////////////////
			// 부서별 월별 특근수당 정산현황 출력
			///////////////////////////////////////////////////////
			else if("ew_result_list".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ArrayList arry = new ArrayList();
	
				// 부서별 정산현황 가져오기
				arry = ewDAO.getJungSanResultList(mode,div,year,month);
				request.setAttribute("arry",arry);
			    getServletContext().getRequestDispatcher("/ew/admin/ew_result_list.jsp?div="+div+"&year="+year+"&month="+month).forward(request,response);
			}
			
			///////////////////////////////////////////////////
			//  개인별 월간 특근현황 출력
			///////////////////////////////////////////////////
			else if("person_month".equals(mode)){
					
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);	
				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
				ArrayList table_list = new ArrayList();	

				// 대상자 정보를 가져온다.
				user_info = userinfoDAO.getUserListById(eachid);
				request.setAttribute("User_Info",user_info);

				// 대상자 월 특근현황을 가져온다.
				table_list = ewBO.getPersonalStatusByMonth(year,month,eachid);
				request.setAttribute("Table_List", table_list);

				getServletContext().getRequestDispatcher("/ew/admin/person_month.jsp?y="+year+"&m="+month).forward(request,response);
			}

			///////////////////////////////////////////////////////
			// 일별 특근 대상자 현황 리스트 출력
			///////////////////////////////////////////////////////
			else if("ew_daily_list".equals(mode)){
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ArrayList arry = new ArrayList();
	
				//일별 특근대상자 리스트
				arry = ewDAO.getEwListByDay(mode,div,year,month,day);
				request.setAttribute("arry",arry);
			    getServletContext().getRequestDispatcher("/ew/admin/ew_daily_list.jsp?div="+div+"&year="+year+"&month="+month+"&day="+day).forward(request,response);
			}

			//////////////////////////////////////////////////////
			//  선택된 특근신청건 삭제처리
			/////////////////////////////////////////////////////
			else if("delete_ew_info".equals(mode)) {
				com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
				ewBO.deleteSelectedEwInfo(ono_plus);

				response.sendRedirect("../servlet/ExtraWorkServlet?mode=eachEwList");
			}

			///////////////////////////////////////////////////////////
			// 인쇄폼으로 출력
			///////////////////////////////////////////////////////////
			else if(mode.equals("print")){	
				com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
				com.anbtech.ew.entity.ExtraWorkHistoryTable table = new com.anbtech.ew.entity.ExtraWorkHistoryTable();
				com.anbtech.admin.entity.ApprovalInfoTable app_table = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

				//특근정보 가져오기
				table = ewDAO.getHistoryInfo(o_no);
				request.setAttribute("EW_INFO", table);

				String aid = table.getAid();
				String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
				app_table = appDAO.getApprovalInfo("ew_app_save",aid,sign_path);
				request.setAttribute("Approval_Info", app_table);

				getServletContext().getRequestDispatcher("/ew/user/view_ew_info.jsp").forward(request,response);
			}
		} catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		} finally {
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

	String mode = request.getParameter("mode"); 
	String redirectUrl = "";

	//로긴사용자정보
	com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
	
	if(sl == null){
		PrintWriter out = response.getWriter();
		out.println("	<script>");
		out.println("	top.location.href('../admin/notice_session.jsp');");
		out.println("	</script>");
		out.close();
		return;		
	}
	String login_id = sl.id;
	String login_name = sl.name;

	// 특근 신청시 넘어오는 정보
	String duty			= Hanguel.toHanguel(request.getParameter("duty"));			// 특근사유
	String w_type		= request.getParameter("w_type");							// 근무형태(n:평일,h:휴일,s:토요일)	
	String w_sdate		= request.getParameter("w_sdate");							// 특근신청시작일자(yyyymmdd)
	String w_stime		= request.getParameter("w_stime");							// 특근신청시작시간(hh:mm)
	String to_tom		= request.getParameter("to_tom");							// 당일(1) or 명일(2)
	String w_etime		= request.getParameter("w_etime");							// 특근 신청 마침 시간
	String duty_cont	= Hanguel.toHanguel(request.getParameter("duty_cont"));		// 업무내용
	
	// 특근 기준 시간 정보 변수(StandardWorkTimeTable)
	String sw_no			 = request.getParameter("sw_no");						// 특근 기준 시간 TABLE 관리번호
	String modify_date		 = request.getParameter("modify_date");					// 특근기준시간변경 일자
	String fix_stime		 = request.getParameter("fix_stime");					// 정규 업무 시작 시간
	String fix_etime		 = request.getParameter("fix_etime");					// 정규 업무 마침 시간
	String fix_stime_sat	 = request.getParameter("fix_stime_sat");				//정규업무시작시간 
	String fix_etime_sat	 = request.getParameter("fix_etime_sat");				//정규업무마침시간
	String fix_stime_holiday = request.getParameter("fix_stime_holiday");			// 휴일 근무 시작 시간
	String fix_etime_holiday = request.getParameter("fix_etime_holiday");			// 휴일 근무 마침 시간
	String off_stime		 = request.getParameter("off_stime");					//시간외근무시작시간
	String off_etime		 = request.getParameter("off_etime");					//시간외근무마침시간
	String off_stime_sat	 = request.getParameter("off_stime_sat");				// 휴일(토) 근무 시작 시간
	String off_etime_sat	 = request.getParameter("off_etime_sat");				// 휴일(토) 근무 마침 시간
	String off_stime_holiday = request.getParameter("off_stime_holiday");			// 휴일 연장근무 시작 시간
	String off_etime_holiday = request.getParameter("off_etime_holiday");			// 휴일 연장근무 마침 시간

	String overday_n		 = request.getParameter("overday_n");					// 평일연장근무여부
	String overday_h		 = request.getParameter("overday_h");					// 휴일연장근무여부
	String overday_s		 = request.getParameter("overday_s");					// 토요일연장근무여부

	// 오늘 날짜
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
	String today = anbdt.getDateNoformat();	
	String thisyear = today.substring(0,4);
			

	try {
		// con생성
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
		
		////////////////////////////////////////////////////////
		//  기준근로시간정보 등록처리
		////////////////////////////////////////////////////////
		if("standard_wtime_save".equals(mode)){
			com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
			ewDAO.saveStandardWtime(modify_date,fix_stime,fix_etime,fix_stime_sat,fix_etime_sat,off_stime,off_etime,fix_stime_holiday,fix_etime_holiday,off_stime_sat,off_etime_sat,off_stime_holiday,off_etime_holiday,overday_n,overday_h,overday_s);

			response.sendRedirect("../servlet/ExtraWorkServlet?mode=standard_wtime_fix&div=wtime_view");
		}


		///////////////////////////////////////////////
		//	특근 신청 정보 등록 처리
		///////////////////////////////////////////////
		 else if("input_data".equals(mode)){
			
			com.anbtech.ew.db.ExtraWorkModuleDAO ewDAO = new com.anbtech.ew.db.ExtraWorkModuleDAO(con);
			com.anbtech.ew.business.ExtraWorkModuleBO ewBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);
			
			boolean bool = false;
			String err_msg = "";
			boolean bool2 = false;

/* 불필요한 기능이라 생각되어 제외시킴 2004.08.04 by 박동렬

			// 특근대상자의 시급정보가 등록되어 있는지 체크한다.
			// 등록되어 있지 않으면 추후 특근정산이 안되므로 신청을 아예 못하게 한다.
			bool = ewDAO.enableReqWork(thisyear,member_id);
			if(bool) {
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('"+login_name+"님의 시급정보를 찾을 수 없습니다. 특근정산을 위해서는 시급정보가 반드시 등록되어 있어야 합니다.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;		
			}
*/
			// 중복신청여부 확인
			bool = ewDAO.enableSave(login_id,w_sdate);
			if(bool) {
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('지정한 날짜에 특근신청정보가 이미 있습니다. 중복신청을 할 수 없습니다.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;		
				
			}

			// 특근마침일자 판단
			String w_edate = "";
			if(to_tom.equals("1")) {		// 당일일경우
				w_edate = w_sdate;
			} else if(to_tom.equals("2")) {	// 명일일 경우
				w_edate = ewBO.getWantDate(w_sdate,"1","+");
			}
								
			// db 저장
			ewDAO.saveHistory(login_id,w_sdate,w_stime,w_edate,w_etime,duty,duty_cont,w_type,"0");

			response.sendRedirect("../servlet/ExtraWorkServlet?mode=eachEwList");
		}
	}catch (Exception e){
		request.setAttribute("ERR_MSG",e.toString());
		getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
	}finally{
		close(con);
	}
  }
}