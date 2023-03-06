import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class StockConfigMgrServlet extends HttpServlet {

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
		com.anbtech.text.Hanguel hanguel = new com.anbtech.text.Hanguel();

		String mid			= request.getParameter("mid");
		String tablename	= request.getParameter("tablename");
		if (tablename == null) tablename = "";
		
		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page");
		String searchword	= hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");

		if (mode == null) mode = "list_conf_type";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);

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
		String login_id = sl.id;
	
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////
			// 수불 유형 리스트
			///////////////////////////////////////////////////
			if(mode.equals("list_conf_type")){
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				ArrayList arry = new ArrayList();
			
				arry = (ArrayList)stconfigDAO.getStockConfList();
				request.setAttribute("LIST_CONF",arry);
				getServletContext().getRequestDispatcher("/st/config/list_conf_type.jsp?mode="+mode).forward(request,response);
			
			////////////////////////////////////////////////////
			// 수불 유형 등록 및 수정 폼
			///////////////////////////////////////////////////
			} else if (mode.equals("write_conf_type") || mode.equals("modify_conf_type")){
				
				com.anbtech.st.business.StockConfigMgrBO stconfigBO = new com.anbtech.st.business.StockConfigMgrBO(con);	
				com.anbtech.st.entity.StockConfInfoTable table = new com.anbtech.st.entity.StockConfInfoTable();
								
				table = stconfigBO.getStockConfForm(mode,mid);
				request.setAttribute("STOCK_CONF",table);
				getServletContext().getRequestDispatcher("/st/config/write_conf_type.jsp?mode="+mode).forward(request,response);
			
			////////////////////////////////////////////////////
			// 수불유형 삭제처리
			///////////////////////////////////////////////////
			} else if (mode.equals("delete_conf_type"))	{
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.deleteStockConfInfo(mid);
				redirectUrl = "StockConfigMgrServlet?mode=list_conf_type";
			

			////////////////////////////////////////////////////
			// 공장 리스트
			///////////////////////////////////////////////////
			}else if(mode.equals("list_factory")){
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				ArrayList arry = new ArrayList();
			
				arry = (ArrayList)stconfigDAO.getFactoryInfoList();
				request.setAttribute("LIST_FACTORY",arry);
				getServletContext().getRequestDispatcher("/st/config/list_factory.jsp?mode="+mode).forward(request,response);
			
			////////////////////////////////////////////////////
			// 공장 정보 등록 및 수정 폼
			///////////////////////////////////////////////////
			} else if (mode.equals("write_factory_info") || mode.equals("modify_factory_info")){
				
				com.anbtech.st.business.StockConfigMgrBO stconfigBO = new com.anbtech.st.business.StockConfigMgrBO(con);	
				com.anbtech.st.entity.FactoryInfoTable table = new com.anbtech.st.entity.FactoryInfoTable();
								
				table = stconfigBO.getFactoryInfoForm(mode,mid);
				request.setAttribute("FACTORY_INFO",table);
				getServletContext().getRequestDispatcher("/st/config/write_factory_info.jsp?mode="+mode).forward(request,response);
						
			////////////////////////////////////////////////////
			// 공장 삭제
			///////////////////////////////////////////////////
			} else if (mode.equals("delete_factory_info")){
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);	
				com.anbtech.st.entity.FactoryInfoTable table = new com.anbtech.st.entity.FactoryInfoTable();
				stconfigDAO.deleteFactoryInfo(mid);
				redirectUrl = "StockConfigMgrServlet?mode=list_factory";
			
			////////////////////////////////////////////////////
			// 창고 리스트
			///////////////////////////////////////////////////
			}else if(mode.equals("list_warehouse")){
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				ArrayList arry = new ArrayList();
			
				arry = (ArrayList)stconfigDAO.getWarehouseInfoList();
				request.setAttribute("LIST_WAREHOUSE",arry);
				getServletContext().getRequestDispatcher("/st/config/list_warehouse.jsp?mode="+mode).forward(request,response);
			
			////////////////////////////////////////////////////
			// 공장 정보 등록 및 수정 폼
			///////////////////////////////////////////////////
			} else if (mode.equals("write_warehouse_info") || mode.equals("modify_warehouse_info")){
				
				com.anbtech.st.business.StockConfigMgrBO stconfigBO = new com.anbtech.st.business.StockConfigMgrBO(con);	
				com.anbtech.st.entity.WarehouseInfoTable table = new com.anbtech.st.entity.WarehouseInfoTable();
								
				table = stconfigBO.getWarehouseInfoForm(mode,mid);
				request.setAttribute("WAREHOUSE_INFO",table);
				getServletContext().getRequestDispatcher("/st/config/write_warehouse_info.jsp?mode="+mode).forward(request,response);
						
			////////////////////////////////////////////////////
			// 공장 삭제
			///////////////////////////////////////////////////
			} else if (mode.equals("delete_warehouse_info")){
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);	
				com.anbtech.st.entity.WarehouseInfoTable table = new com.anbtech.st.entity.WarehouseInfoTable();
				stconfigDAO.deleteWarehouseInfo(mid);
				redirectUrl = "StockConfigMgrServlet?mode=list_warehouse";
			}

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

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/st/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		String mid	= multi.getParameter("mid");	// 관리정보
	
		String trade_type_code	= multi.getParameter("trade_type_code"); // 수불 구분 코드 
		String trade_type_name	= multi.getParameter("trade_type_name"); // 수불 명                      
		String stock_rise_fall	= multi.getParameter("stock_rise_fall"); // 재고 증감 구분 (1:증가 2:감소 3:무관)  
		String stock_type1		= multi.getParameter("stock_type1");	 // 재고유형1                            
		String stock_type2		= multi.getParameter("stock_type2");	 // 재고유형2                            
		String is_cost_apply	= multi.getParameter("is_cost_apply");	 // 재고단가반영구분(1:YES 2:NO)          
		String is_count_posting	= multi.getParameter("is_count_posting");	    // 회계 posting 구분     
		String is_wharehouse_move	= multi.getParameter("is_wharehouse_move");	// 창고간 이동여부        
		String is_factory_move	= multi.getParameter("is_factory_move");		// 공장간 이동여부        
		String is_item_move		= multi.getParameter("is_item_move");			// 품목간 이동여부               
		String is_no_move		= multi.getParameter("is_no_move");				// 제번간 이동여부    	
		
		// 공장관리정보
		String factory_code		= multi.getParameter("factory_code");		// 공장코드
	    String factory_name		= multi.getParameter("factory_name");		// 공장명
		String production_type	= multi.getParameter("production_type");		// 생산타입(외주생산/자사생산)
		String main_product		= multi.getParameter("main_product");		// 주 생산품목
		String factory_address	= multi.getParameter("factory_address");	// 공장 주소
		String product_plan_term= multi.getParameter("product_plan_term");	// 생산계획기간(일단위)
		String mps_confirm_term = multi.getParameter("mps_confirm_term");	// mps확정 기간(일단위)
		String mps_plan_term	= multi.getParameter("mps_plan_term");		// mps계획 기간(일단위)
		String mrp_confirm_term = multi.getParameter("mrp_confirm_term");	// mrp확정 기간(일단위)
		String agency_code		= multi.getParameter("agency_code");				// 사업장코드
		String agency_name		= multi.getParameter("agency_name");				// 사업장명

		// 창고관리정보
		String warehouse_code	= multi.getParameter("warehouse_code")	;	// 창고 코드
		String warehouse_name	= multi.getParameter("warehouse_name")	;	// 창고명
		String warehouse_type	= multi.getParameter("warehouse_type")	;	// 창고 타입-사내창고/거래창고
		String warehouse_address= multi.getParameter("warehouse_address");	// 창고주소
		//String factory_code		= multi.getParameter("factory_code")	;	// 공장 코드(현재의 창고를 갖고있는 공장)
		String group_name		= multi.getParameter("group_name")		;	// 창고의 그룹명
		String manager_name		= multi.getParameter("manager_name")	;	// 창고 관리자명
		String manager_id		= multi.getParameter("manager_id")		;	// 창고 관리자 id
		String using_mrp		= multi.getParameter("using_mrp")		;	// mrp 전개시 해당 창고의 재고 감안 여부
		String client			= multi.getParameter("using_mrp")		;	// 등록할 창고의 거래처

		
		//전페이지에서 값 받아온다. multi에서 가져옴
		String mode = multi.getParameter("mode");
		String page = multi.getParameter("page");
		String searchword = multi.getParameter("searchword");
		String searchscope = multi.getParameter("searchscope");
		String category = multi.getParameter("category");

		//공백으로 넘어오거나 null로 온 값들에 대한 처리.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";
			

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
		String login_id = sl.id;

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////
			//	수불유형정보 등록
			////////////////////////
			if (mode.equals("write_conf_type")){
			
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.saveStockConfInfo(trade_type_code,trade_type_name,stock_rise_fall,stock_type1,stock_type2,is_cost_apply,is_count_posting,is_wharehouse_move,is_factory_move,is_item_move,is_no_move);
				redirectUrl = "StockConfigMgrServlet?mode=list_conf_type";

			/////////////////////////
			//	수불유형정보 Update
			////////////////////////			
			} else if (mode.equals("modify_conf_type")) {
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.modifyStockConfInfo(mid,trade_type_code,trade_type_name,stock_rise_fall,stock_type1,stock_type2,is_cost_apply,is_count_posting,is_wharehouse_move,is_factory_move,is_item_move,is_no_move);
				
				redirectUrl = "StockConfigMgrServlet?mode=list_conf_type";

			/////////////////////////
			//	공장정보 등록
			////////////////////////
			} else if (mode.equals("write_factory_info")){
			
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.saveFactoryInfo(factory_code,factory_name,production_type,main_product,factory_address,product_plan_term,mps_confirm_term,mps_plan_term,mrp_confirm_term,agency_code,agency_name);
				
				redirectUrl = "StockConfigMgrServlet?mode=list_factory";

			////////////////////////
			//	공장정보 Update
			////////////////////////			
			} else if (mode.equals("modify_factory_info")) {
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.modifyFactoryInfo(mid,factory_code,factory_name,production_type,main_product,factory_address,product_plan_term,mps_confirm_term,mps_plan_term,mrp_confirm_term,agency_code,agency_name);
				
				redirectUrl = "StockConfigMgrServlet?mode=list_factory";
						
			/////////////////////////
			//	창고정보 등록
			////////////////////////
			} else if (mode.equals("write_warehouse_info")){
			
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.saveWarehouseInfo(warehouse_code,warehouse_name,warehouse_type,factory_code,factory_name,group_name,manager_name,manager_id,using_mrp,client,warehouse_address);
				
				redirectUrl = "StockConfigMgrServlet?mode=list_warehouse";

			////////////////////////
			//	창고정보 Update
			////////////////////////			
			} else if (mode.equals("modify_warehouse_info")) {
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.modifyWarehouseInfo(mid,warehouse_code,warehouse_name,warehouse_type,factory_code,factory_name,group_name,manager_name,manager_id,using_mrp,client,warehouse_address);
				
				redirectUrl = "StockConfigMgrServlet?mode=list_warehouse";
			}			
			
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}
