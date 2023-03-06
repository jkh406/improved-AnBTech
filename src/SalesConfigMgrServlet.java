import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class SalesConfigMgrServlet extends HttpServlet {

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

		String mid			= request.getParameter("mid");
		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page");
		String searchword	= request.getParameter("searchword");
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");

		if (mode == null) mode = "list_item";
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
		String login_id = sl.id;
	
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");


			////////////////////////////////////////////////////
			// 수주형태 등록현황 리스트 출력
			///////////////////////////////////////////////////
			if(mode.equals("list_booking_type")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				com.anbtech.bs.entity.BookingTypeTable table = new com.anbtech.bs.entity.BookingTypeTable();
				ArrayList arry = new ArrayList();

				arry = (ArrayList)scDAO.getBookingTypeList();
				request.setAttribute("BOOKING_TYPE_LIST",arry);
				getServletContext().getRequestDispatcher("/bs/config/list_booking_type.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 수주형태 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if (mode.equals("write_booking_type") || mode.equals("modify_booking_type")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.BookingTypeTable table = new com.anbtech.bs.entity.BookingTypeTable();
								
				table = scBO.getBookingTypeForm(mode,mid);
				request.setAttribute("SALES_CONF",table);
				getServletContext().getRequestDispatcher("/bs/config/write_booking_type.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 수주형태 삭제처리
			///////////////////////////////////////////////////
			else if (mode.equals("delete_booking_type"))	{
				
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.deleteBookingTypeInfo(mid);
				redirectUrl = "SalesConfigMgrServlet?mode=list_booking_type";
			}

			///////////////////////////////////////////////////
			// 품목별 단가정보 리스트 출력
			///////////////////////////////////////////////////
			else if(mode.equals("list_item_unit_cost")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				com.anbtech.bs.business.SalesConfigLinkUrlBO redirectBO = new com.anbtech.bs.business.SalesConfigLinkUrlBO(con);
				com.anbtech.bs.entity.SalesConfigLinkUrl redirect = new com.anbtech.bs.entity.SalesConfigLinkUrl();

				ArrayList arry = new ArrayList();
				arry = (ArrayList)scDAO.getItemUnitCostList(mode,searchword,searchscope,page);
				request.setAttribute("ITEM_UNIT_COST_LIST",arry);
			
				redirect = redirectBO.getRedirectForList("bs_item_unit_cost",mode,searchword,searchscope,page);
				request.setAttribute("REDIRECT",redirect);
				getServletContext().getRequestDispatcher("/bs/config/list_item_unit_cost.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 품목 단가정보 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if (mode.equals("write_item_unit_cost") || mode.equals("modify_item_unit_cost")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();
							
				table = scBO.getItemUnitCostForm(mode,mid);
				request.setAttribute("UNIT_COST",table);
				getServletContext().getRequestDispatcher("/bs/config/write_item_unit_cost.jsp?mode="+mode).forward(request,response);
			}
			////////////////////////////////////////////////////
			// 품목 단가정보 삭제 처리
			///////////////////////////////////////////////////
			else if (mode.equals("delete_item_unit_cost"))	{
				
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.deleteItemUnitCostInfo(mid);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost";
			}


			///////////////////////////////////////////////////
			// 고객별 품목 단가정보 리스트 출력
			///////////////////////////////////////////////////
			else if(mode.equals("list_item_unit_cost_by_customer")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				com.anbtech.bs.business.SalesConfigLinkUrlBO redirectBO = new com.anbtech.bs.business.SalesConfigLinkUrlBO(con);
				com.anbtech.bs.entity.SalesConfigLinkUrl redirect = new com.anbtech.bs.entity.SalesConfigLinkUrl();

				ArrayList arry = new ArrayList();
				arry = (ArrayList)scDAO.getItemUnitCostByCustomerList(mode,searchword,searchscope,page);
				request.setAttribute("ITEM_UNIT_COST_LIST",arry);
			
				redirect = redirectBO.getRedirectForList("bs_item_unit_cost_by_customer",mode,searchword,searchscope,page);
				request.setAttribute("REDIRECT",redirect);

				getServletContext().getRequestDispatcher("/bs/config/list_item_unit_cost_by_customer.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 고객별 품목 단가정보 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if (mode.equals("write_item_unit_cost_by_customer") || mode.equals("modify_item_unit_cost_by_customer")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();
							
				table = scBO.getItemUnitCostByCustomerForm(mode,mid);
				
				request.setAttribute("COST_CUSTOMER",table);
				getServletContext().getRequestDispatcher("/bs/config/write_item_unit_cost_by_customer.jsp?mode="+mode).forward(request,response);
			}
			////////////////////////////////////////////////////
			// 고객별 품목 단가정보 삭제 처리
			///////////////////////////////////////////////////
			else if (mode.equals("delete_item_unit_cost_by_customer"))	{
				
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.deleteItemUnitCostByCustomerInfo(mid);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost_by_customer";
			}

			///////////////////////////////////////////////////
			// 품목별 할증정보 리스트 출력
			///////////////////////////////////////////////////
			else if(mode.equals("list_item_premium")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				com.anbtech.bs.business.SalesConfigLinkUrlBO redirectBO = new com.anbtech.bs.business.SalesConfigLinkUrlBO(con);
				com.anbtech.bs.entity.SalesConfigLinkUrl redirect = new com.anbtech.bs.entity.SalesConfigLinkUrl();

				ArrayList arry = new ArrayList();
				arry = (ArrayList)scDAO.getItemPremiumList(mode,searchword,searchscope,page);
				request.setAttribute("ITEM_PREMIUM_LIST",arry);
			
				redirect = redirectBO.getRedirectForList("bs_item_premium",mode,searchword,searchscope,page);
				request.setAttribute("REDIRECT",redirect);
				getServletContext().getRequestDispatcher("/bs/config/list_item_premium.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 품목 할증정보 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if (mode.equals("write_item_premium") || mode.equals("modify_item_premium")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();
							
				table = scBO.getItemPremiumForm(mode,mid);
				request.setAttribute("ITEM_PREMIUM",table);
				getServletContext().getRequestDispatcher("/bs/config/write_item_premium.jsp?mode="+mode).forward(request,response);
			}
			////////////////////////////////////////////////////
			// 품목 할증정보 삭제 처리
			///////////////////////////////////////////////////
			else if (mode.equals("delete_item_premium"))	{
				
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.deleteItemPremiumInfo(mid);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium";
			}

			///////////////////////////////////////////////////
			// 고객별 품목 할증정보 리스트 출력
			///////////////////////////////////////////////////
			else if(mode.equals("list_item_premium_by_customer")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				com.anbtech.bs.business.SalesConfigLinkUrlBO redirectBO = new com.anbtech.bs.business.SalesConfigLinkUrlBO(con);
				com.anbtech.bs.entity.SalesConfigLinkUrl redirect = new com.anbtech.bs.entity.SalesConfigLinkUrl();

				ArrayList arry = new ArrayList();
				arry = (ArrayList)scDAO.getItemPremiumListByCustomer(mode,searchword,searchscope,page);
				request.setAttribute("ITEM_PREMIUM_LIST",arry);
			
				redirect = redirectBO.getRedirectForList("bs_item_premium_by_customer",mode,searchword,searchscope,page);
				request.setAttribute("REDIRECT",redirect);

				getServletContext().getRequestDispatcher("/bs/config/list_item_premium_by_customer.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 고객별 품목 할증정보 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if (mode.equals("write_item_premium_by_customer") || mode.equals("modify_item_premium_by_customer")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();
							
				table = scBO.getItemPremiumByCustomerForm(mode,mid);
				request.setAttribute("PREMIUM_CUSTOMER",table);
				getServletContext().getRequestDispatcher("/bs/config/write_item_premium_by_customer.jsp?mode="+mode).forward(request,response);
			}
			////////////////////////////////////////////////////
			// 고객별 품목 할증정보 삭제 처리
			///////////////////////////////////////////////////
			else if (mode.equals("delete_item_premium_by_customer"))	{
				
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.deleteItemPremiumByCustomerInfo(mid);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium_by_customer";
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

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/bs/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

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
		
		// 데이터 파라미터
        // 수주형태 정보 파라미터
		String  mid				= multi.getParameter("mid");			// 관리정보
		String  order_code		= multi.getParameter("order_code");		// 수주형태코드 
		String  order_name		= multi.getParameter("order_name");		// 수주형태명 
		String	is_export		= multi.getParameter("is_export");		// 수출여부    
		String	is_return		= multi.getParameter("is_return");		// 반품여부
		String	is_entry		= multi.getParameter("is_entry");		// 통관여부     
		String	is_shipping		= multi.getParameter("is_shipping");	// 출하여부
		String	is_auto_ship	= multi.getParameter("is_auto_ship");	// 자동출하생성여부
		String	is_sale			= multi.getParameter("is_sale");		// 매출여부      
		String	shipping_type	= multi.getParameter("shipping_type");	// 출하형태
		String	is_use			= multi.getParameter("is_use");			// 사용여부

		// 품목코드별 단가정보 파라미터
		String  item_code		= multi.getParameter("item_code");		// 품목코드    
		String  item_name		= multi.getParameter("item_name");		// 품목명
		String  sale_type		= multi.getParameter("sale_type");		// 판매유형코드, 일반/직영/특판 등
		String  approval_type	= multi.getParameter("approval_type");  // 결재유형코드, 현금/수표/어음 등
		String  apply_date		= multi.getParameter("apply_date");		// 적용일자, 품목단가가 적용될 시점
		String  sale_unit		= multi.getParameter("sale_unit");		// 판매단위
		String  money_type		= multi.getParameter("money_type");		// 화폐유형
		String  sale_unit_cost	= multi.getParameter("sale_unit_cost"); // 판매단가
		String  customer_code	= multi.getParameter("customer_code");  // 거래처코드
		String  customer_name	= multi.getParameter("customer_name");  // 거래처명

		// 품목코드별 할증정보
		String premium_type				= multi.getParameter("premium_type");		// 할증유형          
		String premium_name				= multi.getParameter("premium_name");		// 할증유형명          
		String premium_standard_quantity= multi.getParameter("premium_standard_quantity");		// 할증적용기준수량
		String premium_value			= multi.getParameter("premium_value");		// 할증값

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			/////////////////////////
			//  수주형태정보 신규등록 
			////////////////////////
			if(mode.equals("write_booking_type")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveBookingTypeInfo(order_code,order_name,is_export,is_return,is_entry,is_shipping,is_auto_ship,is_sale,shipping_type,is_use);
				redirectUrl = "SalesConfigMgrServlet?mode=list_booking_type";
			}
			
			/////////////////////////
			//  수주형태정보 수정 
			////////////////////////
			else if(mode.equals("modify_booking_type")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.modifyBookingTypeInfo(mid,order_code,order_name,is_export,is_return,is_entry,is_shipping,is_auto_ship,is_sale,shipping_type,is_use);
				redirectUrl = "SalesConfigMgrServlet?mode=list_booking_type";
			}			

			//////////////////////////////
			//  품목 단가정보 신규등록 
			//////////////////////////////
			if(mode.equals("write_item_unit_cost")){

				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveItemUnitCostInfo(item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost";
			}
			
			///////////////////////////
			//  품목 단가정보 수정 
			///////////////////////////
			else if(mode.equals("modify_item_unit_cost")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.modifyItemUnitCostInfo(mid,item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost";
			}
			
			//////////////////////////////
			//  고객별 품목 단가정보 신규등록 
			//////////////////////////////
			if(mode.equals("write_item_unit_cost_by_customer")){

				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveItemUnitCostByCustomerInfo(item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost,customer_code,customer_name);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost_by_customer";
			}
			
			///////////////////////////
			//  고객별 품목 단가정보 수정
			///////////////////////////
			else if(mode.equals("modify_item_unit_cost_by_customer")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.modifyItemUnitCostByCustomerInfo(mid,item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost,customer_code,customer_name);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost_by_customer";
			}			

			//////////////////////////////
			//  품목 할증정보 등록
			//////////////////////////////
			if(mode.equals("write_item_premium")){

				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveItemPremiumInfo(item_code,item_name,approval_type,apply_date,sale_unit,premium_type,premium_name,premium_standard_quantity,premium_value);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium";
			}
			
			///////////////////////////
			//  품목 할증정보 수정
			///////////////////////////
			else if(mode.equals("modify_item_premium")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.modifyItemPremiumInfo(mid,item_code,item_name,approval_type,apply_date,sale_unit,premium_type,premium_name,premium_standard_quantity,premium_value);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium";
			}			

			//////////////////////////////
			//  고객별 품목 할증정보 등록
			//////////////////////////////
			if(mode.equals("write_item_premium_by_customer")){

				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveItemPremiumByCustomerInfo(item_code,item_name,approval_type,apply_date,sale_unit,premium_type,premium_name,premium_standard_quantity,premium_value,customer_code,customer_name);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium_by_customer";
			}
			
			///////////////////////////
			//  고객별 품목 할증정보 수정
			///////////////////////////
			else if(mode.equals("modify_item_premium_by_customer")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.modifyItemPremiumByCustomerInfo(mid,item_code,item_name,approval_type,apply_date,sale_unit,premium_type,premium_name,premium_standard_quantity,premium_value,customer_code,customer_name);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium_by_customer";
			}			

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			//con소멸
			close(con);
		}
	} //doPost()
}
