import com.anbtech.mm.db.*;
import com.anbtech.mm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class mpsInfoServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 15;
	private int max_display_page = 5;

	/********
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리 (목록보기)
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//기본파라미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"cal_month":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MPS MASTER구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String mps_no = Hanguel.toHanguel(request.getParameter("mps_no"))==null?"":Hanguel.toHanguel(request.getParameter("mps_no"));
		String order_no = Hanguel.toHanguel(request.getParameter("order_no"))==null?"":Hanguel.toHanguel(request.getParameter("order_no"));
		String mps_type = Hanguel.toHanguel(request.getParameter("mps_type"))==null?"":Hanguel.toHanguel(request.getParameter("mps_type"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String item_name = Hanguel.toHanguel(request.getParameter("item_name"))==null?"":Hanguel.toHanguel(request.getParameter("item_name"));
		String item_spec = Hanguel.toHanguel(request.getParameter("item_spec"))==null?"":Hanguel.toHanguel(request.getParameter("item_spec"));
		String plan_date = Hanguel.toHanguel(request.getParameter("plan_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_date"));
		String plan_count = Hanguel.toHanguel(request.getParameter("plan_count"))==null?"":Hanguel.toHanguel(request.getParameter("plan_count"));
		String sell_count = Hanguel.toHanguel(request.getParameter("sell_count"))==null?"":Hanguel.toHanguel(request.getParameter("sell_count"));
		String item_unit = Hanguel.toHanguel(request.getParameter("item_unit"))==null?"":Hanguel.toHanguel(request.getParameter("item_unit"));
		String mps_status = Hanguel.toHanguel(request.getParameter("mps_status"))==null?"":Hanguel.toHanguel(request.getParameter("mps_status"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		String factory_name = Hanguel.toHanguel(request.getParameter("factory_name"))==null?"":Hanguel.toHanguel(request.getParameter("factory_name"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String reg_id = Hanguel.toHanguel(request.getParameter("reg_id"))==null?"":Hanguel.toHanguel(request.getParameter("reg_id"));
		String reg_name = Hanguel.toHanguel(request.getParameter("reg_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_name"));
		String app_date = Hanguel.toHanguel(request.getParameter("app_date"))==null?"":Hanguel.toHanguel(request.getParameter("app_date"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String order_comp = Hanguel.toHanguel(request.getParameter("order_comp"))==null?"":Hanguel.toHanguel(request.getParameter("order_comp"));
	
		//날자에서 '/'제거하기
		com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		plan_date = prs.repWord(plan_date,"/","");
		reg_date = prs.repWord(reg_date,"-","");

		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		String year = Hanguel.toHanguel(request.getParameter("year"))==null?anbdt.getYear():Hanguel.toHanguel(request.getParameter("year"));
		String month = Hanguel.toHanguel(request.getParameter("month"))==null?anbdt.getMonth():Hanguel.toHanguel(request.getParameter("month"));
		String view_td = Hanguel.toHanguel(request.getParameter("view_td"))==null?"":Hanguel.toHanguel(request.getParameter("view_td"));

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MPS 정보
			//------------------------------------------------------------
			//MPS 월별 CALENDAR계획 보기
			if ("cal_month".equals(mode)){
				com.anbtech.mm.db.mpsModifyDAO mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);

				//화면출력 데이터 읽기
				String CAL_List = mpsDAO.getMpsCalendarList(factory_no,year,month,login_id);
				request.setAttribute("CAL_List", CAL_List);

				//공휴일 데이터 읽기
				String HLD_List = mpsDAO.getHolidayList(year,month);
				request.setAttribute("HLD_List", HLD_List);

				//파라미터 전달하기
				request.setAttribute("year",year);
				request.setAttribute("month",month);
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mps/mpsCalendar.jsp").forward(request,response);
			}
			//월간보기 LIST
			else if ("list_month".equals(mode)){
				com.anbtech.mm.db.mpsModifyDAO mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);

				//화면출력 데이터 읽기
				ArrayList master_list = new ArrayList();
				master_list = mpsDAO.getMpsMonthList(factory_no,view_td,login_id);
				request.setAttribute("MASTER_List", master_list); 

				//파라미터 전달하기
				request.setAttribute("view_td",view_td);
				request.setAttribute("factory_no",factory_no);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mps/mpsMonthList.jsp").forward(request,response);
			}
			//1,2주간보기 LIST
			else if ("list_week1".equals(mode) || "list_week2".equals(mode)){
				com.anbtech.mm.db.mpsModifyDAO mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);

				//화면출력 데이터 읽기
				ArrayList master_list = new ArrayList();
				if("list_week1".equals(mode))
					master_list = mpsDAO.getMpsWeekList(factory_no,view_td,login_id,"1");
				else if("list_week2".equals(mode))
					master_list = mpsDAO.getMpsWeekList(factory_no,view_td,login_id,"2");
				request.setAttribute("MASTER_List", master_list); 

				//파라미터 전달하기
				request.setAttribute("view_td",view_td);
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("mode",mode);

				//분기하기
				if("list_week1".equals(mode))
					getServletContext().getRequestDispatcher("/mm/mps/mpsWeekList.jsp").forward(request,response);
				else if("list_week2".equals(mode))
					getServletContext().getRequestDispatcher("/mm/mps/mpsBiWeekList.jsp").forward(request,response);
				
			}
			//상세내용 조회 (편집준비)
			else if ("mps_view".equals(mode)){
				com.anbtech.mm.db.mpsModifyDAO mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);

				//권한 검사하기
				String mgr = mpsDAO.checkGrade("MPS",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('사용 권한이 없습니다. 공장을 확인하거나 권한을 부여받으십시오.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//상세내용 데이터 읽기
				com.anbtech.mm.entity.mpsMasterTable masterT = new com.anbtech.mm.entity.mpsMasterTable();
				masterT = mpsDAO.readMasterItem(pid,factory_no,view_td);
				request.setAttribute("ITEM_List", masterT);

				//파라미터 전달하기
				request.setAttribute("GRADE_mgr",mgr);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mps/mpsMasterReg.jsp").forward(request,response);
			}
			//상세 진행상태 내용보기
			else if ("process_view".equals(mode)){
				com.anbtech.mm.business.productProcessBO prsBO = new com.anbtech.mm.business.productProcessBO(con);

/*				//권한 검사하기
				String mgr = mpsDAO.checkGrade("MPS",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('사용 권한이 없습니다. 공장을 확인하거나 권한을 부여받으십시오.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}
*/
				//파라미터 전달하기
				prsBO.getLinkNo(mps_no,factory_no);
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("fg_code",fg_code);
				request.setAttribute("model_code",model_code);
				request.setAttribute("model_name",model_name);
				request.setAttribute("mps_status",prsBO.getMpsStatus());
				request.setAttribute("mrp_status",prsBO.getMrpStatus());
				request.setAttribute("mfg_status",prsBO.getMfgStatus());
				request.setAttribute("pur_status",prsBO.getPurchaseStatus());
				request.setAttribute("pro_status",prsBO.getProductStatus());
				request.setAttribute("qc_status",prsBO.getQcStatus());


				//분기하기
				getServletContext().getRequestDispatcher("/mm/mps/ProcessStatus.jsp").forward(request,response);
			}

			
		}catch (Exception e){
			//에러출력 페이지로 분기
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
		
	} //doGet()

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
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//기본파라미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"cal_month":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MPS MASTER구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String mps_no = Hanguel.toHanguel(request.getParameter("mps_no"))==null?"":Hanguel.toHanguel(request.getParameter("mps_no"));
		String order_no = Hanguel.toHanguel(request.getParameter("order_no"))==null?"":Hanguel.toHanguel(request.getParameter("order_no"));
		String mps_type = Hanguel.toHanguel(request.getParameter("mps_type"))==null?"":Hanguel.toHanguel(request.getParameter("mps_type"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String item_name = Hanguel.toHanguel(request.getParameter("item_name"))==null?"":Hanguel.toHanguel(request.getParameter("item_name"));
		String item_spec = Hanguel.toHanguel(request.getParameter("item_spec"))==null?"":Hanguel.toHanguel(request.getParameter("item_spec"));
		String plan_date = Hanguel.toHanguel(request.getParameter("plan_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_date"));
		String plan_count = Hanguel.toHanguel(request.getParameter("plan_count"))==null?"":Hanguel.toHanguel(request.getParameter("plan_count"));
		String sell_count = Hanguel.toHanguel(request.getParameter("sell_count"))==null?"":Hanguel.toHanguel(request.getParameter("sell_count"));
		String item_unit = Hanguel.toHanguel(request.getParameter("item_unit"))==null?"":Hanguel.toHanguel(request.getParameter("item_unit"));
		String mps_status = Hanguel.toHanguel(request.getParameter("mps_status"))==null?"":Hanguel.toHanguel(request.getParameter("mps_status"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		String factory_name = Hanguel.toHanguel(request.getParameter("factory_name"))==null?"":Hanguel.toHanguel(request.getParameter("factory_name"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String reg_id = Hanguel.toHanguel(request.getParameter("reg_id"))==null?"":Hanguel.toHanguel(request.getParameter("reg_id"));
		String reg_name = Hanguel.toHanguel(request.getParameter("reg_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_name"));
		String app_date = Hanguel.toHanguel(request.getParameter("app_date"))==null?"":Hanguel.toHanguel(request.getParameter("app_date"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String order_comp = Hanguel.toHanguel(request.getParameter("order_comp"))==null?"":Hanguel.toHanguel(request.getParameter("order_comp"));
	
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		String year = Hanguel.toHanguel(request.getParameter("year"))==null?"":Hanguel.toHanguel(request.getParameter("year"));
		String month = Hanguel.toHanguel(request.getParameter("month"))==null?"":Hanguel.toHanguel(request.getParameter("month"));
		String view_td = Hanguel.toHanguel(request.getParameter("view_td"))==null?"":Hanguel.toHanguel(request.getParameter("view_td"));
		
		//날자에서 '/'제거하기
		com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();
		plan_date = prs.repWord(plan_date,"/","");
		reg_date = prs.repWord(reg_date,"-","");

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//------------------------------------------------------------
			//		MPS 정보
			//------------------------------------------------------------
			//MPS 월별 CALENDAR계획 보기
			if ("cal_month".equals(mode)){
				com.anbtech.mm.db.mpsModifyDAO mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);

				//화면출력 데이터 읽기
				String CAL_List = mpsDAO.getMpsCalendarList(factory_no,year,month,login_id);
				request.setAttribute("CAL_List", CAL_List);

				//공휴일 데이터 읽기
				String HLD_List = mpsDAO.getHolidayList(year,month);
				request.setAttribute("HLD_List", HLD_List);

				//파라미터 전달하기
				request.setAttribute("year",year);
				request.setAttribute("month",month);
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mps/mpsCalendar.jsp").forward(request,response);
			}
			//월간보기 LIST
			else if ("list_month".equals(mode)){
				com.anbtech.mm.db.mpsModifyDAO mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);

				//화면출력 데이터 읽기
				ArrayList master_list = new ArrayList();
				master_list = mpsDAO.getMpsMonthList(factory_no,view_td,login_id);
				request.setAttribute("MASTER_List", master_list); 

				//파라미터 전달하기
				request.setAttribute("view_td",view_td);
				request.setAttribute("factory_no",factory_no);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mps/mpsMonthList.jsp").forward(request,response);
			}
			//1,2주간보기 LIST
			else if ("list_week1".equals(mode) || "list_week2".equals(mode)){
				com.anbtech.mm.db.mpsModifyDAO mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);

				//화면출력 데이터 읽기
				ArrayList master_list = new ArrayList();
				if("list_week1".equals(mode))
					master_list = mpsDAO.getMpsWeekList(factory_no,view_td,login_id,"1");
				else if("list_week2".equals(mode))
					master_list = mpsDAO.getMpsWeekList(factory_no,view_td,login_id,"2");
				request.setAttribute("MASTER_List", master_list); 

				//파라미터 전달하기
				request.setAttribute("view_td",view_td);
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("mode",mode);

				//분기하기
				if("list_week1".equals(mode))
					getServletContext().getRequestDispatcher("/mm/mps/mpsWeekList.jsp").forward(request,response);
				else if("list_week2".equals(mode))
					getServletContext().getRequestDispatcher("/mm/mps/mpsBiWeekList.jsp").forward(request,response);
				
			}
			//상세내용 조회 (편집준비)
			else if ("mps_view".equals(mode)){
				com.anbtech.mm.db.mpsModifyDAO mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);

				//권한 검사하기
				String mgr = mpsDAO.checkGrade("MPS",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('사용 권한이 없습니다. 공장을 확인하거나 권한을 부여받으십시오.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//상세내용 데이터 읽기
				com.anbtech.mm.entity.mpsMasterTable masterT = new com.anbtech.mm.entity.mpsMasterTable();
				masterT = mpsDAO.readMasterItem(pid,factory_no,view_td);
				request.setAttribute("ITEM_List", masterT);

				//파라미터 전달하기
				request.setAttribute("GRADE_mgr",mgr);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mps/mpsMasterReg.jsp").forward(request,response);
			}
			//MPS MASTER 등록/수정/삭제
			else if("mps_save".equals(mode) || "mps_modify".equals(mode) || "mps_delete".equals(mode)) {
				com.anbtech.mm.business.mpsInputBO mpsBO = new com.anbtech.mm.business.mpsInputBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//등록하기
					if("mps_save".equals(mode)) {
						msg = mpsBO.insertMps(order_no,mps_type,model_code,model_name,fg_code,item_code,item_name,item_spec,plan_date,plan_count,item_unit,factory_no,factory_name,reg_date,reg_id,reg_name,order_comp);
					} 
					//수정하기
					else if ("mps_modify".equals(mode)) {
						msg = mpsBO.updateMps(pid,order_no,mps_type,model_code,model_name,fg_code,item_code,item_name,item_spec,plan_date,plan_count,item_unit,factory_no,factory_name,reg_date,order_comp);
					}
					//삭제하기
					else if("mps_delete".equals(mode)) {
						msg = mpsBO.deleteMps(pid);
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기할 파라미터 만들기
					String para = "cal_month&year="+reg_date.substring(0,4)+"&month="+reg_date.substring(4,6);
					para += "&factory_no="+factory_no+"&msg="+msg;
					
					//분기하기
					out.println("	<script>");
					out.println("	self.location.href('mpsInfoServlet?mode="+para+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//MPS MASTER 승인요청,확정승인,취소
			else if("mps_ask".equals(mode) || "mps_app".equals(mode) || "mps_cancel".equals(mode)) {
				com.anbtech.mm.business.mpsInputBO mpsBO = new com.anbtech.mm.business.mpsInputBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//승인요청
					if("mps_ask".equals(mode)) {
						msg = mpsBO.setMpsStatus(pid,"2",login_id,login_name);
					} 
					//확정승인
					else if ("mps_app".equals(mode)) {
						msg = mpsBO.setMpsStatus(pid,"3",login_id,login_name);
					}
					//확정취소
					else if("mps_cancel".equals(mode)) {
						msg = mpsBO.setMpsStatus(pid,"1",login_id,login_name);
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기할 파라미터 만들기
					String para = "cal_month&year="+reg_date.substring(0,4)+"&month="+reg_date.substring(4,6);
					para += "&factory_no="+factory_no+"&msg="+msg;
					
					//분기하기
					out.println("	<script>");
					out.println("	self.location.href('mpsInfoServlet?mode="+para+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}

		}catch (Exception e){
			//에러출력 페이지로 분기
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} //doPost()
}

