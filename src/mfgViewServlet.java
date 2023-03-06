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

public class mfgViewServlet extends HttpServlet {
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//검색을 위한 기본파라미터
		String start_date = Hanguel.toHanguel(request.getParameter("start_date"))==null?"":Hanguel.toHanguel(request.getParameter("start_date"));
		String end_date = Hanguel.toHanguel(request.getParameter("end_date"))==null?"":Hanguel.toHanguel(request.getParameter("end_date"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		재공품 현황 출력하기
			//------------------------------------------------------------
			//재공품 생산현황
			if ("view_run_glist".equals(mode) || "view_run_blist".equals(mode)){
				com.anbtech.mm.db.mfgViewDAO viewDAO = new com.anbtech.mm.db.mfgViewDAO(con);

				//등록된 LIST 보기
				ArrayList product_list = new ArrayList();
				product_list = viewDAO.getMfgProductMasterRunList (factory_no,sItem,sWord,start_date,end_date,page,max_display_cnt);
				request.setAttribute("PRODUCT_List", product_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgProductMasterTable pageL = new com.anbtech.mm.entity.mfgProductMasterTable();
				pageL = viewDAO.getMfgProductMasterRunDisplayPage(factory_no,sItem,sWord,start_date,end_date,mode,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("start_date",start_date);
				request.setAttribute("end_date",end_date);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				if ("view_run_glist".equals(mode)) {		//재공품 생산현황
					getServletContext().getRequestDispatcher("/mm/view/runProductGList.jsp").forward(request,response);
				}else if("view_run_blist".equals(mode)) {	//재공품 불량현황
					getServletContext().getRequestDispatcher("/mm/view/runProductBList.jsp").forward(request,response);
				}
			
			}
			//------------------------------------------------------------
			//		제품 현황 출력하기
			//------------------------------------------------------------
			//제품 생산현황
			else if ("view_pd_glist".equals(mode) || "view_pd_blist".equals(mode)){
				com.anbtech.mm.db.mfgViewDAO viewDAO = new com.anbtech.mm.db.mfgViewDAO(con);

				//등록된 LIST 보기
				ArrayList product_list = new ArrayList();
				product_list = viewDAO.getMfgProductMasterList (factory_no,sItem,sWord,start_date,end_date,page,max_display_cnt);
				request.setAttribute("PRODUCT_List", product_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgProductMasterTable pageL = new com.anbtech.mm.entity.mfgProductMasterTable();
				pageL = viewDAO.getMfgProductMasterDisplayPage(factory_no,sItem,sWord,start_date,end_date,mode,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("start_date",start_date);
				request.setAttribute("end_date",end_date);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				if ("view_pd_glist".equals(mode)) {		//재공품 생산현황
					getServletContext().getRequestDispatcher("/mm/view/ProductGList.jsp").forward(request,response);
				}else if("view_pd_blist".equals(mode)) {	//재공품 불량현황
					getServletContext().getRequestDispatcher("/mm/view/ProductBList.jsp").forward(request,response);
				}
			
			}
			
		}catch (Exception e){
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//검색을 위한 기본파라미터
		String start_date = Hanguel.toHanguel(request.getParameter("start_date"))==null?"":Hanguel.toHanguel(request.getParameter("start_date"));
		String end_date = Hanguel.toHanguel(request.getParameter("end_date"))==null?"":Hanguel.toHanguel(request.getParameter("end_date"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();
		start_date = prs.repWord(start_date,"/","");
		end_date = prs.repWord(end_date,"/","");
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		재공품 현황 출력하기
			//------------------------------------------------------------
			//재공품 생산현황
			if ("view_run_glist".equals(mode) || "view_run_blist".equals(mode)){
				com.anbtech.mm.db.mfgViewDAO viewDAO = new com.anbtech.mm.db.mfgViewDAO(con);

				//등록된 LIST 보기
				ArrayList product_list = new ArrayList();
				product_list = viewDAO.getMfgProductMasterRunList (factory_no,sItem,sWord,start_date,end_date,page,max_display_cnt);
				request.setAttribute("PRODUCT_List", product_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgProductMasterTable pageL = new com.anbtech.mm.entity.mfgProductMasterTable();
				pageL = viewDAO.getMfgProductMasterRunDisplayPage(factory_no,sItem,sWord,start_date,end_date,mode,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("start_date",start_date);
				request.setAttribute("end_date",end_date);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				if ("view_run_glist".equals(mode)) {		//재공품 생산현황
					getServletContext().getRequestDispatcher("/mm/view/runProductGList.jsp").forward(request,response);
				}else if("view_run_blist".equals(mode)) {	//재공품 불량현황
					getServletContext().getRequestDispatcher("/mm/view/runProductBList.jsp").forward(request,response);
				}
			
			}
			//------------------------------------------------------------
			//		제품 현황 출력하기
			//------------------------------------------------------------
			//제품 생산현황
			else if ("view_pd_glist".equals(mode) || "view_pd_blist".equals(mode)){
				com.anbtech.mm.db.mfgViewDAO viewDAO = new com.anbtech.mm.db.mfgViewDAO(con);

				//등록된 LIST 보기
				ArrayList product_list = new ArrayList();
				product_list = viewDAO.getMfgProductMasterList (factory_no,sItem,sWord,start_date,end_date,page,max_display_cnt);
				request.setAttribute("PRODUCT_List", product_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgProductMasterTable pageL = new com.anbtech.mm.entity.mfgProductMasterTable();
				pageL = viewDAO.getMfgProductMasterDisplayPage(factory_no,sItem,sWord,start_date,end_date,mode,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("start_date",start_date);
				request.setAttribute("end_date",end_date);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				if ("view_pd_glist".equals(mode)) {		//재공품 생산현황
					getServletContext().getRequestDispatcher("/mm/view/ProductGList.jsp").forward(request,response);
				}else if("view_pd_blist".equals(mode)) {	//재공품 불량현황
					getServletContext().getRequestDispatcher("/mm/view/ProductBList.jsp").forward(request,response);
				}
			
			}
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} //doPost()
}

