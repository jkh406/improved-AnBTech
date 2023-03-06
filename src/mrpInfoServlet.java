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

public class mrpInfoServlet extends HttpServlet {
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"mrp_request":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MRP MASTER구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String mps_no = Hanguel.toHanguel(request.getParameter("mps_no"))==null?"":Hanguel.toHanguel(request.getParameter("mps_no"));
		String mrp_no = Hanguel.toHanguel(request.getParameter("mrp_no"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_no"));
		String mrp_start_date = Hanguel.toHanguel(request.getParameter("mrp_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_start_date"));
		String mrp_end_date = Hanguel.toHanguel(request.getParameter("mrp_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_end_date"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String item_name = Hanguel.toHanguel(request.getParameter("item_name"))==null?"":Hanguel.toHanguel(request.getParameter("item_name"));
		String item_spec = Hanguel.toHanguel(request.getParameter("item_spec"))==null?"":Hanguel.toHanguel(request.getParameter("item_spec"));
		String p_count = Hanguel.toHanguel(request.getParameter("p_count"))==null?"0":Hanguel.toHanguel(request.getParameter("p_count"));
		String plan_date = Hanguel.toHanguel(request.getParameter("plan_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_date"));	
		String item_unit = Hanguel.toHanguel(request.getParameter("item_unit"))==null?"":Hanguel.toHanguel(request.getParameter("item_unit"));
		String mrp_status = Hanguel.toHanguel(request.getParameter("mrp_status"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_status"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		String factory_name = Hanguel.toHanguel(request.getParameter("factory_name"))==null?"":Hanguel.toHanguel(request.getParameter("factory_name"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String app_date = Hanguel.toHanguel(request.getParameter("app_date"))==null?"":Hanguel.toHanguel(request.getParameter("app_date"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		String reg_div_code = Hanguel.toHanguel(request.getParameter("reg_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("reg_div_code"));
		String reg_div_name = Hanguel.toHanguel(request.getParameter("reg_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_div_name"));
		String reg_id = Hanguel.toHanguel(request.getParameter("reg_id"))==null?"":Hanguel.toHanguel(request.getParameter("reg_id"));
		String reg_name = Hanguel.toHanguel(request.getParameter("reg_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_name"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String pu_dev_date = Hanguel.toHanguel(request.getParameter("pu_dev_date"))==null?"":Hanguel.toHanguel(request.getParameter("pu_dev_date"));
		String pu_req_no = Hanguel.toHanguel(request.getParameter("pu_req_no"))==null?"":Hanguel.toHanguel(request.getParameter("pu_req_no"));
		String stock_link = Hanguel.toHanguel(request.getParameter("stock_link"))==null?"":Hanguel.toHanguel(request.getParameter("stock_link"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		
		//MRP ITEM 구성을 위해 파라미터 추가
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String assy_code = Hanguel.toHanguel(request.getParameter("assy_code"))==null?"":Hanguel.toHanguel(request.getParameter("assy_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
		String item_type = Hanguel.toHanguel(request.getParameter("item_type"))==null?"":Hanguel.toHanguel(request.getParameter("item_type"));
		String draw_count = Hanguel.toHanguel(request.getParameter("draw_count"))==null?"0":Hanguel.toHanguel(request.getParameter("draw_count"));
		String mrp_count = Hanguel.toHanguel(request.getParameter("mrp_count"))==null?"0":Hanguel.toHanguel(request.getParameter("mrp_count"));
		String stock_count = Hanguel.toHanguel(request.getParameter("stock_count"))==null?"0":Hanguel.toHanguel(request.getParameter("stock_count"));
		String open_count = Hanguel.toHanguel(request.getParameter("open_count"))==null?"0":Hanguel.toHanguel(request.getParameter("open_count"));
		String plan_count = Hanguel.toHanguel(request.getParameter("plan_count"))==null?"0":Hanguel.toHanguel(request.getParameter("plan_count"));
		String buy_type = Hanguel.toHanguel(request.getParameter("buy_type"))==null?"":Hanguel.toHanguel(request.getParameter("buy_type"));
		String need_count = Hanguel.toHanguel(request.getParameter("need_count"))==null?"0":Hanguel.toHanguel(request.getParameter("need_count"));
		String add_count = Hanguel.toHanguel(request.getParameter("add_count"))==null?"0":Hanguel.toHanguel(request.getParameter("add_count"));
		String mrs_count = Hanguel.toHanguel(request.getParameter("mrs_count"))==null?"0":Hanguel.toHanguel(request.getParameter("mrs_count"));
		
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		//날자에서 '/'제거하기
		com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();
		mrp_start_date = prs.repWord(mrp_start_date,"/","");
		mrp_end_date = prs.repWord(mrp_end_date,"/","");
		pu_dev_date = prs.repWord(pu_dev_date,"/","");

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MRP MASTER 정보
			//------------------------------------------------------------
			//MPS 접수현황 보기
			if ("mrp_request".equals(mode)){
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//권한 검사하기
				String mgr = mrpDAO.checkGrade("MRP",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('사용 권한이 없습니다. 공장을 확인하거나 권한을 부여받으십시오.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//등록된 LIST 보기
				ArrayList request_list = new ArrayList();
				request_list = mrpDAO.getMrpRequestList(factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("REQUEST_List", request_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mpsMasterTable pageL = new com.anbtech.mm.entity.mpsMasterTable();
				pageL = mrpDAO.getRequestDisplayPage(factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//공장명 구하기
				String where = "where factory_code='"+factory_no+"'";
				factory_name = mrpDAO.getColumData("factory_info_table","factory_name",where);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("factory_name",factory_name);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpRequestList.jsp").forward(request,response);
			}
			//MRP 등록현황 보기
			else if ("mrp_list".equals(mode)){
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//권한 검사하기
				String mgr = mrpDAO.checkGrade("MRP",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('사용 권한이 없습니다. 공장을 확인하거나 권한을 부여받으십시오.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//등록된 LIST 보기
				ArrayList mrp_list = new ArrayList();
				mrp_list = mrpDAO.getMrpMasterList(factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("MRP_List", mrp_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mrpMasterTable pageL = new com.anbtech.mm.entity.mrpMasterTable();
				pageL = mrpDAO.getMrpDisplayPage(factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//공장명 구하기
				String where = "where factory_code='"+factory_no+"'";
				factory_name = mrpDAO.getColumData("factory_info_table","factory_name",where);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("factory_name",factory_name);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg",msg);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpMasterList.jsp").forward(request,response);
			}
			//MRP 등록/수정/삭제/확정/부품전개 준비
			else if ("mrp_presave".equals(mode) || "mrp_preview".equals(mode)){
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//권한 검사하기
				String mgr = mrpDAO.checkGrade("MRP",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('사용 권한이 없습니다. 공장을 확인하거나 권한을 부여받으십시오.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//화면출력 데이터 읽기
				com.anbtech.mm.entity.mrpMasterTable mrpT = new com.anbtech.mm.entity.mrpMasterTable();
				if("mrp_presave".equals(mode)) {		//MPS접수로 부터
					mrpT = mrpDAO.readRequestMrpMasterItem(pid);
				} else {								//MRP등록 LIST로 부터
					mrpT = mrpDAO.readMrpMasterItem(pid);
				}
				request.setAttribute("MRP_master", mrpT); 

				//파라미터 전달하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpMasterReg.jsp").forward(request,response);
				
			}
			//앞페이지 바로가기
			else if ("mrp_review".equals(mode)){
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//화면출력 데이터 읽기
				com.anbtech.mm.entity.mrpMasterTable mrpT = new com.anbtech.mm.entity.mrpMasterTable();
				mrpT = mrpDAO.readMrpMasterItem(pid);
				request.setAttribute("MRP_master", mrpT); 

				//파라미터 전달하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpMasterReg.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//		MRP ITEM 정보
			//------------------------------------------------------------
			//MRP ITEM LIST
			else if ("item_list".equals(mode)){
				com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//소요량 리스트
				ArrayList item_list = new ArrayList();
				item_list = mrpBO.getStockMrpItemList(factory_no,mrp_no,fg_code,item_code,mrp_start_date,mrp_count,stock_link,mrp_status);
				request.setAttribute("ITEM_List", item_list); 

				//ASSY LIST
				ArrayList assy_list = new ArrayList();
				assy_list = mrpBO.getMrpItemAssyList(factory_no,mrp_no);
				request.setAttribute("ASSY_List", assy_list);
				
				//MRP 정보
				com.anbtech.mm.entity.mrpMasterTable mrpM = new com.anbtech.mm.entity.mrpMasterTable();						
				mrpM = mrpDAO.readMrpMasterItem(pid);
				request.setAttribute("MRP_master", mrpM); 

				//파라미터 전달하기
				request.setAttribute("msg",msg);
				request.setAttribute("assy_code",assy_code);
				request.setAttribute("item_code",item_code);
				request.setAttribute("p_count",p_count);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpItemList.jsp").forward(request,response);
				
			}
			//MRP ITEM 수정준비
			else if ("item_presave".equals(mode)){
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//개별 소요량 수정준비
				com.anbtech.mm.entity.mrpItemTable mrpT = new com.anbtech.mm.entity.mrpItemTable();
				mrpT = mrpDAO.readMrpItem(pid,factory_no);
				request.setAttribute("MRP_item", mrpT);

				//MRP 정보
				com.anbtech.mm.entity.mrpMasterTable mrpM = new com.anbtech.mm.entity.mrpMasterTable();						
				mrpM = mrpDAO.readMrpMasterItem(gid);
				request.setAttribute("MRP_master", mrpM); 

				//파라미터 전달하기
				request.setAttribute("msg",msg);
				request.setAttribute("assy_code",assy_code);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpItemReg.jsp").forward(request,response);
				
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"mrp_request":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MRP MASTER구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String mps_no = Hanguel.toHanguel(request.getParameter("mps_no"))==null?"":Hanguel.toHanguel(request.getParameter("mps_no"));
		String mrp_no = Hanguel.toHanguel(request.getParameter("mrp_no"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_no"));
		String mrp_start_date = Hanguel.toHanguel(request.getParameter("mrp_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_start_date"));
		String mrp_end_date = Hanguel.toHanguel(request.getParameter("mrp_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_end_date"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String item_name = Hanguel.toHanguel(request.getParameter("item_name"))==null?"":Hanguel.toHanguel(request.getParameter("item_name"));
		String item_spec = Hanguel.toHanguel(request.getParameter("item_spec"))==null?"":Hanguel.toHanguel(request.getParameter("item_spec"));
		String p_count = Hanguel.toHanguel(request.getParameter("p_count"))==null?"0":Hanguel.toHanguel(request.getParameter("p_count"));
		String plan_date = Hanguel.toHanguel(request.getParameter("plan_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_date"));	
		String item_unit = Hanguel.toHanguel(request.getParameter("item_unit"))==null?"":Hanguel.toHanguel(request.getParameter("item_unit"));
		String mrp_status = Hanguel.toHanguel(request.getParameter("mrp_status"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_status"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		String factory_name = Hanguel.toHanguel(request.getParameter("factory_name"))==null?"":Hanguel.toHanguel(request.getParameter("factory_name"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String app_date = Hanguel.toHanguel(request.getParameter("app_date"))==null?"":Hanguel.toHanguel(request.getParameter("app_date"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		String reg_div_code = Hanguel.toHanguel(request.getParameter("reg_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("reg_div_code"));
		String reg_div_name = Hanguel.toHanguel(request.getParameter("reg_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_div_name"));
		String reg_id = Hanguel.toHanguel(request.getParameter("reg_id"))==null?"":Hanguel.toHanguel(request.getParameter("reg_id"));
		String reg_name = Hanguel.toHanguel(request.getParameter("reg_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_name"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String pu_dev_date = Hanguel.toHanguel(request.getParameter("pu_dev_date"))==null?"":Hanguel.toHanguel(request.getParameter("pu_dev_date"));
		String pu_req_no = Hanguel.toHanguel(request.getParameter("pu_req_no"))==null?"":Hanguel.toHanguel(request.getParameter("pu_req_no"));
		String stock_link = Hanguel.toHanguel(request.getParameter("stock_link"))==null?"":Hanguel.toHanguel(request.getParameter("stock_link"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		
		//MRP ITEM 구성을 위해 파라미터 추가
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String assy_code = Hanguel.toHanguel(request.getParameter("assy_code"))==null?"":Hanguel.toHanguel(request.getParameter("assy_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
		String item_type = Hanguel.toHanguel(request.getParameter("item_type"))==null?"":Hanguel.toHanguel(request.getParameter("item_type"));
		String draw_count = Hanguel.toHanguel(request.getParameter("draw_count"))==null?"0":Hanguel.toHanguel(request.getParameter("draw_count"));
		String mrp_count = Hanguel.toHanguel(request.getParameter("mrp_count"))==null?"0":Hanguel.toHanguel(request.getParameter("mrp_count"));
		String stock_count = Hanguel.toHanguel(request.getParameter("stock_count"))==null?"0":Hanguel.toHanguel(request.getParameter("stock_count"));
		String open_count = Hanguel.toHanguel(request.getParameter("open_count"))==null?"0":Hanguel.toHanguel(request.getParameter("open_count"));
		String plan_count = Hanguel.toHanguel(request.getParameter("plan_count"))==null?"0":Hanguel.toHanguel(request.getParameter("plan_count"));
		String buy_type = Hanguel.toHanguel(request.getParameter("buy_type"))==null?"":Hanguel.toHanguel(request.getParameter("buy_type"));
		String need_count = Hanguel.toHanguel(request.getParameter("need_count"))==null?"0":Hanguel.toHanguel(request.getParameter("need_count"));
		String add_count = Hanguel.toHanguel(request.getParameter("add_count"))==null?"0":Hanguel.toHanguel(request.getParameter("add_count"));
		String mrs_count = Hanguel.toHanguel(request.getParameter("mrs_count"))==null?"0":Hanguel.toHanguel(request.getParameter("mrs_count"));
		
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		//날자에서 '/'제거하기
		com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();
		mrp_start_date = prs.repWord(mrp_start_date,"/","");
		mrp_end_date = prs.repWord(mrp_end_date,"/","");
		pu_dev_date = prs.repWord(pu_dev_date,"/","");

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MRP MASTER 정보
			//------------------------------------------------------------
			//MPS 접수현황 보기
			if ("mrp_request".equals(mode)){
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//권한 검사하기
				String mgr = mrpDAO.checkGrade("MRP",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('사용 권한이 없습니다. 공장을 확인하거나 권한을 부여받으십시오.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//등록된 LIST 보기
				ArrayList request_list = new ArrayList();
				request_list = mrpDAO.getMrpRequestList(factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("REQUEST_List", request_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mpsMasterTable pageL = new com.anbtech.mm.entity.mpsMasterTable();
				pageL = mrpDAO.getRequestDisplayPage(factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//공장명 구하기
				String where = "where factory_code='"+factory_no+"'";
				factory_name = mrpDAO.getColumData("factory_info_table","factory_name",where);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("factory_name",factory_name);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpRequestList.jsp").forward(request,response);
			}
			//MRP 등록현황 보기
			else if ("mrp_list".equals(mode)){
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//권한 검사하기
				String mgr = mrpDAO.checkGrade("MRP",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('사용 권한이 없습니다. 공장을 확인하거나 권한을 부여받으십시오.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//등록된 LIST 보기
				ArrayList mrp_list = new ArrayList();
				mrp_list = mrpDAO.getMrpMasterList(factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("MRP_List",mrp_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mrpMasterTable pageL = new com.anbtech.mm.entity.mrpMasterTable();
				pageL = mrpDAO.getMrpDisplayPage(factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//공장명 구하기
				String where = "where factory_code='"+factory_no+"'";
				factory_name = mrpDAO.getColumData("factory_info_table","factory_name",where);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("factory_name",factory_name);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpMasterList.jsp").forward(request,response);
			}
			//MRP 등록/수정/삭제/확정/부품전개 준비
			else if ("mrp_presave".equals(mode) || "mrp_preview".equals(mode)){
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//권한 검사하기
				String mgr = mrpDAO.checkGrade("MRP",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('사용 권한이 없습니다. 공장을 확인하거나 권한을 부여받으십시오.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//화면출력 데이터 읽기
				com.anbtech.mm.entity.mrpMasterTable mrpT = new com.anbtech.mm.entity.mrpMasterTable();
				if("mrp_presave".equals(mode)) {		//MPS접수로 부터
					mrpT = mrpDAO.readRequestMrpMasterItem(pid);
				} else {								//MRP등록 LIST로 부터
					mrpT = mrpDAO.readMrpMasterItem(pid);
				}
				request.setAttribute("MRP_master", mrpT); 

				//파라미터 전달하기
				request.setAttribute("factory_no",factory_no);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpMasterReg.jsp").forward(request,response);
				
			}
			//MRP MASTER 등록/수정/삭제
			else if("mrp_save".equals(mode) || "mrp_modify".equals(mode) || "mrp_delete".equals(mode)) {
				com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//등록하기
					if("mrp_save".equals(mode)) {
						msg = mrpBO.insertMrp(mps_no,mrp_start_date,model_code,model_name,fg_code,item_code,item_name,item_spec,p_count,plan_date,item_unit,factory_no,factory_name,reg_date,reg_id,reg_name,pu_dev_date,stock_link,pjt_code,pjt_name);
					} 
					//수정하기
					else if ("mrp_modify".equals(mode)) {
						msg = mrpBO.updateMrp(pid,mrp_start_date,reg_date,pu_dev_date,stock_link,pjt_code,pjt_name);
					}
					//삭제하기
					else if("mrp_delete".equals(mode)) {
						msg = mrpBO.deleteMrp(pid,mps_no,mrp_no,mrp_status,factory_no);
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기할 파라미터 만들기
					String para = "mrp_list&sItem=fg_code&sWord=";
					para += "&factory_no="+factory_no+"&msg="+msg;
					
					//분기하기
					out.println("	<script>");
					out.println("	self.location.href('mrpInfoServlet?mode="+para+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//재고부품예약하기,재고부품예약취소하기
			else if("stock_confirm".equals(mode) || "stock_cancel".equals(mode)) {
				com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					if("stock_confirm".equals(mode)) {			//재고예약
						mrpBO.updateMrp(pid,mrp_start_date,reg_date,pu_dev_date,stock_link,pjt_code,pjt_name);//재고예약
						msg = mrpBO.setMrpStatus(pid,mrp_status,"2","");	//상태변경
					} else if("stock_cancel".equals(mode)) {	//예약취소
						msg = mrpBO.setMrpStatus(pid,mrp_status,"1","A");
					}
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기할 파라미터 만들기
					String para = "mrp_preview&pid="+pid+"&msg="+msg;
					
					//분기하기
					out.println("	<script>");
					out.println("	self.location.href('mrpInfoServlet?mode="+para+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//MRP확정하기(소요량 일괄확정),MRP확정취소하기
			else if("mrp_confirm".equals(mode) || "mrp_cancel".equals(mode)) {
				com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					if("mrp_confirm".equals(mode)) {			//MRP확정 : 구매등록
						msg = mrpBO.setMrpStatus(pid,mrp_status,"3","");
					} else if("mrp_cancel".equals(mode)) {		//MRP취소 : 구매취소
						msg = mrpBO.setMrpStatus(pid,mrp_status,"1","");
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기할 파라미터 만들기
					String para = "mrp_preview&pid="+pid+"&msg="+msg+"&factory_no="+factory_no;
					
					//분기하기
					if("mrp_confirm".equals(mode)) {	//구매모듈로
						out.println("	<script>");
						//out.println("	parent.location.href('/webffice/pu/PuBody.htm');");
						out.println("	self.location.href('PurchaseMgrServlet?mode=request_search');");
						out.println("	</script>");

					} else {
						out.println("	<script>");
						out.println("	self.location.href('mrpInfoServlet?mode="+para+"');");
						out.println("	</script>");
					}
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//앞페이지 바로가기
			else if ("mrp_review".equals(mode)){
				
				//분기할 파라미터 만들기
				String para = "pid="+pid+"&factory_no="+factory_no;

				//분기하기
				out.println("	<script>");
				out.println("	parent.location.href('mrpInfoServlet?mode=mrp_review&"+para+"');");
				out.println("	</script>");
				out.close();
			}
	
			//------------------------------------------------------------
			//		MRP ITEM 정보
			//------------------------------------------------------------
			//MRP ITEM LIST
			else if ("item_list".equals(mode)){
				com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//소요량 리스트
				ArrayList item_list = new ArrayList();
				item_list = mrpBO.getStockMrpItemList(factory_no,mrp_no,fg_code,item_code,mrp_start_date,mrp_count,stock_link,mrp_status);
				request.setAttribute("ITEM_List", item_list); 

				//ASSY LIST
				ArrayList assy_list = new ArrayList();
				assy_list = mrpBO.getMrpItemAssyList(factory_no,mrp_no);
				request.setAttribute("ASSY_List", assy_list); 

				//MRP 정보
				com.anbtech.mm.entity.mrpMasterTable mrpM = new com.anbtech.mm.entity.mrpMasterTable();						
				mrpM = mrpDAO.readMrpMasterItem(pid);
				request.setAttribute("MRP_master", mrpM); 

				//파라미터 전달하기
				request.setAttribute("msg",msg);
				request.setAttribute("assy_code",assy_code);
		

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpItemList.jsp").forward(request,response);
				
			}
			//MRP ITEM 수정준비
			else if ("item_presave".equals(mode)){
				com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);

				//개별 소요량 수정준비
				com.anbtech.mm.entity.mrpItemTable mrpT = new com.anbtech.mm.entity.mrpItemTable();
				mrpT = mrpDAO.readMrpItem(pid,factory_no);
				request.setAttribute("MRP_item", mrpT);

				//MRP 정보
				com.anbtech.mm.entity.mrpMasterTable mrpM = new com.anbtech.mm.entity.mrpMasterTable();						
				mrpM = mrpDAO.readMrpMasterItem(gid);
				request.setAttribute("MRP_master", mrpM); 

				//파라미터 전달하기
				request.setAttribute("msg",msg);
				request.setAttribute("assy_code",assy_code);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/mrp/mrpItemReg.jsp").forward(request,response);
				
			}
			//MRP ITEM 개별수량 수정하기
			else if("item_modify".equals(mode)) {
				com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					msg = mrpBO.updateMrpItem(pid,plan_count,add_count);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					String para = "&pid="+gid+"&factory_no="+factory_no+"&mrp_no="+mrp_no;
					para += "&fg_code="+fg_code+"&item_code="+assy_code+"&assy_code="+assy_code;
					para += "&msg="+msg;

					//MRP ITEM LIST 조회로 링크
					out.println("	<script>");
					out.println("	parent.list.location.href('mrpInfoServlet?mode=item_list"+para+"');");
					out.println("	</script>");

					//MBOM TREE 입력창으로 링크
					out.println("	<script>");
					out.println("	parent.reg.location.href('mrpInfoServlet?mode=item_presave&pid=');");
					out.println("	</script>");
					out.close();
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//개별 재고부품예약 취소하기
			else if("item_cancel".equals(mode)) {
				com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					msg = mrpBO.setMrpStatus(pid,mrp_status,"1","I");
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					String para = "&pid="+gid+"&factory_no="+factory_no+"&mrp_no="+mrp_no;
					para += "&fg_code="+fg_code+"&item_code="+assy_code+"&assy_code="+assy_code;
					para += "&msg="+msg;

					//MRP ITEM LIST 조회로 링크
					out.println("	<script>");
					out.println("	parent.list.location.href('mrpInfoServlet?mode=item_list"+para+"');");
					out.println("	</script>");

					//MBOM TREE 입력창으로 링크
					out.println("	<script>");
					out.println("	parent.reg.location.href('mrpInfoServlet?mode=item_presave&pid=');");
					out.println("	</script>");
					out.close();
				
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

