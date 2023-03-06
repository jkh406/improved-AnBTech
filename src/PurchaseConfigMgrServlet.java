import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class PurchaseConfigMgrServlet extends HttpServlet {

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
		String tablename	= request.getParameter("tablename");
		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page");
		String searchword	= request.getParameter("searchword");
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");
		String item_code	= request.getParameter("item_code");

		if (mode == null) mode = "list_item";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";
		if (tablename == null) tablename = "";

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
			// 입출고형태 등록 및 수정 폼
			///////////////////////////////////////////////////
			if(mode.equals("write_inout_type") || mode.equals("modify_inout_type")){

				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				com.anbtech.pu.business.PurchaseConfigMgrBO puconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
				com.anbtech.pu.entity.InOutTypeTable inoutTable = new com.anbtech.pu.entity.InOutTypeTable();
				
				inoutTable = puconfigBO.getInOutTypeForm(mode,mid);
				request.setAttribute("INOUT_TYPE_INFO",inoutTable);
				
				getServletContext().getRequestDispatcher("/pu/config/write_inout_type.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 입출고형태 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("list_inout_type")){
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				com.anbtech.pu.entity.InOutTypeTable inoutTable = new com.anbtech.pu.entity.InOutTypeTable();
				ArrayList arry = new ArrayList();

				arry = (ArrayList)puconfigDAO.getInOutTypeList();

				request.setAttribute("TRANSACTION_TYPE_INFO",arry);
				getServletContext().getRequestDispatcher("/pu/config/list_inout_type.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 입출고형태 삭제
			///////////////////////////////////////////////////
			else if (mode.equals("delete_inout_type"))
			{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.deleteInOutTypeInfo(mid);
				response.sendRedirect("PurchaseConfigMgrServlet?mode=list_inout_type");
			}

			////////////////////////////////////////////////////
			// 매입형태 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if(mode.equals("write_purchase_type") || mode.equals("modify_purchase_type")){

				com.anbtech.pu.business.PurchaseConfigMgrBO puconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
				com.anbtech.pu.entity.PurchaseTypeTable purchaseTable = new com.anbtech.pu.entity.PurchaseTypeTable();
				
				purchaseTable = puconfigBO.getPurchaseTypeForm(mode,mid);
				request.setAttribute("PURCHASE_TYPE_INFO",purchaseTable);

				getServletContext().getRequestDispatcher("/pu/config/write_purchase_type.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 매입형태 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("list_purchase_type")){

				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				com.anbtech.pu.entity.PurchaseTypeTable purchaseTable = new com.anbtech.pu.entity.PurchaseTypeTable();
				ArrayList arry = new ArrayList();

				arry = (ArrayList)puconfigDAO.getPurchaseTypeList();

				request.setAttribute("PURCHASE_TYPE_INFO",arry);

				getServletContext().getRequestDispatcher("/pu/config/list_purchase_type.jsp?mode="+mode).forward(request,response);
			}
			////////////////////////////////////////////////////
			// 매입형태 삭제
			///////////////////////////////////////////////////
			else if (mode.equals("delete_purchase_type"))
			{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.deletePurchaseTypeInfo(mid);
				response.sendRedirect("PurchaseConfigMgrServlet?mode=list_purchase_type");
			}
			////////////////////////////////////////////////////
			// 발주형태 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if(mode.equals("write_order_type") || mode.equals("modify_order_type")){

				com.anbtech.pu.business.PurchaseConfigMgrBO puconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
				com.anbtech.pu.entity.OrderTypeTable orderTable = new com.anbtech.pu.entity.OrderTypeTable();
				
				orderTable = puconfigBO.getOrderTypeForm(mode,mid);
				request.setAttribute("ORDER_TYPE_INFO",orderTable);

				getServletContext().getRequestDispatcher("/pu/config/write_order_type.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 발주형태 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("list_order_type")){

				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				com.anbtech.pu.entity.OrderTypeTable orderTable = new com.anbtech.pu.entity.OrderTypeTable();
				ArrayList arry = new ArrayList();

				arry = (ArrayList)puconfigDAO.getOrderTypeList();
				request.setAttribute("ORDER_TYPE_INFO",arry);
				getServletContext().getRequestDispatcher("/pu/config/list_order_type.jsp?mode="+mode).forward(request,response);
			}
			///////////////////////////////////////////////////
			// 발주형태 삭제
			///////////////////////////////////////////////////
			else if (mode.equals("delete_order_type"))
			{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.deleteOrderTypeInfo(mid);
				response.sendRedirect("PurchaseConfigMgrServlet?mode=list_order_type");
			}

			///////////////////////////////////////////////////
			// 품목별 단가 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("list_item_cost")){
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				com.anbtech.pu.business.PurchaseConfigLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseConfigLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseConfigLinkUrl redirect = new com.anbtech.pu.entity.PurchaseConfigLinkUrl();

				ArrayList arry = new ArrayList();
				arry = (ArrayList)puconfigDAO.getItemCostInfoList(mode,searchword,searchscope,page);
				request.setAttribute("ITEM_COST_LIST",arry);
			
				redirect = redirectBO.getRedirectForItemCostList(mode,searchword,searchscope,page);
				request.setAttribute("REDIRECT",redirect);

				getServletContext().getRequestDispatcher("/pu/config/list_item_cost.jsp?mode="+mode).forward(request,response);
			}

			///////////////////////////////////////////////////
			// 품목별 단가 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if(mode.equals("write_unit_cost") || mode.equals("modify_unit_cost")){
				com.anbtech.pu.business.PurchaseConfigMgrBO puconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
				com.anbtech.st.entity.StockInfoTable table = new com.anbtech.st.entity.StockInfoTable();
				
				table = puconfigBO.getItemUnitCostAddForm(mode,item_code);
				request.setAttribute("ITEM_UNIT_COST",table);

				getServletContext().getRequestDispatcher("/pu/config/write_item_cost.jsp?mode="+mode).forward(request,response);
			}

			///////////////////////////////////////////////////
			// 품목별 공급처 및 공급가 정보 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if(mode.equals("write_item_supply_info") || mode.equals("modify_item_supply_info") || mode.equals("view_item_supply_info")){
				
				com.anbtech.pu.business.PurchaseConfigMgrBO puconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
				com.anbtech.pu.entity.ItemSupplyInfoTable itemSupplyTable = new com.anbtech.pu.entity.ItemSupplyInfoTable();
				
				itemSupplyTable = puconfigBO.getItemSupplyInfoTableForm(mode,mid);
				request.setAttribute("ITEM_SUPPLY_INFO",itemSupplyTable);

				String url = "";
				if(mode.equals("view_item_supply_info")){										
					url = "/pu/config/view_item_supply_info.jsp?mode="+mode;
				}else{
					url = "/pu/config/write_item_supply_info.jsp?mode="+mode;
				}
				getServletContext().getRequestDispatcher(url).forward(request,response);
			}


			///////////////////////////////////////////////////
			// 품목별 공급처 및 공급단가 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("list_item_supply_info")){

				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				ArrayList arry = new ArrayList();
			
				arry = (ArrayList)puconfigDAO.getItemSupplyInfoList(tablename,mode,searchword,searchscope,page);
				request.setAttribute("ITEM_SUPPLY_INFO",arry);
			
				com.anbtech.pu.business.PurchaseConfigLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseConfigLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseConfigLinkUrl redirect = new com.anbtech.pu.entity.PurchaseConfigLinkUrl();
				redirect = redirectBO.getRedirect(tablename,mode,searchword,searchscope,page);
				request.setAttribute("REDIRECT",redirect);

				getServletContext().getRequestDispatcher("/pu/config/list_item_supply_info.jsp?mode="+mode).forward(request,response);
			
			///////////////////////////////////////////////////
			//  품목별 공급처 관리 정보 삭제
			///////////////////////////////////////////////////
			}   else if (mode.equals("delete_item_supply_info")){
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.deleteItemSupplyInfo(mid);
				response.sendRedirect("PurchaseConfigMgrServlet?mode=list_item_supply_info");
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

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/pu/";

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
		
		// 공통
		String mid			= multi.getParameter("mid");
		String type			= multi.getParameter("type");			// 형태
		String name			= multi.getParameter("name");			// 형태명
		String is_enter		= multi.getParameter("is_enter");		// 입고여부("y" or "n")
		String is_import	= multi.getParameter("is_import");		// 수입여부("y" or "n")
		String is_return	= multi.getParameter("is_return");		// 반품여부("y" or "n")
		String is_sageup	= multi.getParameter("is_sageup");		// 사급여부("y" or "n")
		String is_using		= multi.getParameter("is_using");		// 사용여부("y" or "n")
				
		// 발주형태정보관련변수 (공통: 관리번호,수입여부,입고여부,반품여부,사용여부)
		String	is_shipping		= multi.getParameter("is_shipping");	// 선적여부(y or n)
		String	is_pass			= multi.getParameter("is_pass");		// 통관여부(y or n)
		String	is_purchase		= multi.getParameter("is_purchase");	// 매입여부(y or n)
		String	enter_code		= multi.getParameter("enter_code");		// 입고형태 코드(선택)
		String	enter_name		= multi.getParameter("enter_name");		// 입고형태 명
		String	outgo_code		= multi.getParameter("outgo_code");		// 출고형태 코드(선택)
		String	outgo_name		= multi.getParameter("outgo_name");		// 출고형태 명
		String	purchase_code	= multi.getParameter("purchase_code");	// 매입형태 코드(선택)
		String	purchase_name	= multi.getParameter("purchase_name");	// 매입형태 명

		// 품목별 공급처 관리 관려 변수
		String item_code			= multi.getParameter("item_code");
		String supplyer_code		= multi.getParameter("supplyer_code");
		String order_weight			= multi.getParameter("order_weight");
		String lead_time			= multi.getParameter("lead_time");
		String is_trade_now			= multi.getParameter("is_trade_now");
		String is_main_supplyer		= multi.getParameter("is_main_supplyer");
		String min_order_quantity	= multi.getParameter("min_order_quantity");
		String max_order_quantity	= multi.getParameter("max_order_quantity");
		String order_unit			= multi.getParameter("order_unit");
		String supplyer_item_code	= multi.getParameter("supplyer_item_code");
		String supplyer_item_name	= multi.getParameter("supplyer_item_name");
		String supplyer_item_desc	= multi.getParameter("supplyer_item_desc");
		String maker_name			= multi.getParameter("maker_name");
		String supply_unit_cost		= multi.getParameter("supply_unit_cost");
		String request_unit_cost	= multi.getParameter("request_unit_cost");
		String supplyer_name		= multi.getParameter("supplyer_name");
		String item_desc			= multi.getParameter("item_desc");
				
		// 입/출고형태관리관련 변수 (공통:관리번호,입고여부,반품여부,사급여부,수입여부,사용여부)		
		String stock_type	= multi.getParameter("stock_type");		// 재고형태

		// 매입형태관리관련 변수 (공통: 관리번호,매입형태코드,매입형태명,수입여부,반품여부,사용여부)
		String is_except	= multi.getParameter("is_except");		// 예외여부
		String account_type	= multi.getParameter("account_type");	// 회계처리형태

		//품목단가등록관련 파라미터들
		String item_type			= multi.getParameter("item_type");
		String item_name			= multi.getParameter("item_name");
		String unit_cost_a			= multi.getParameter("unit_cost_a");
		String unit_cost_s			= multi.getParameter("unit_cost_s");
		String unit_cost_c			= multi.getParameter("unit_cost_c");
		String last_updated_date	= multi.getParameter("last_updated_date");
	
		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////
			//	입/출고형태정보저장
			////////////////////////
			if(mode.equals("write_inout_type")){
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.saveInOutTypeInfo(type,name,is_import,is_enter,is_return,is_sageup,is_using,stock_type);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_inout_type";
			
			///////////////////////////
			//	입/출고 형태 정보 수정
			//////////////////////////
			} else if (mode.equals("modify_inout_type"))	{
				
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.updateInOutTypeInfo(mid,type,name,is_import,is_enter,is_return,is_sageup,is_using,stock_type);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_inout_type";

			///////////////////////////
			//	발주형태정보저장
			//////////////////////////
			} else if (mode.equals("write_order_type"))		{
			
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con); 
				puconfigDAO.saveOrderTypeInfo(type,name,is_import,is_shipping,is_pass,is_enter,is_purchase,is_return,is_sageup,is_using,enter_code,enter_name,outgo_code,outgo_name,purchase_code,purchase_name);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_order_type";
			
			///////////////////////////
			//	발주형태정보 수정
			//////////////////////////
			} else if (mode.equals("modify_order_type"))		{
			
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con); 
				puconfigDAO.updateOrderTypeInfo(mid,type,name,is_import,is_shipping,is_pass,is_enter,is_purchase,is_return,is_sageup,is_using,enter_code,enter_name,outgo_code,outgo_name,purchase_code,purchase_name);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_order_type";
			
			///////////////////////////
			//	매입형태정보저장
			//////////////////////////
			} else if (mode.equals("write_purchase_type") )	{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.savePurchaseTypeInfo(type,name,is_import,is_return,is_using,is_except, account_type);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_purchase_type";
			
			///////////////////////////
			//	매입형태정보Update
			//////////////////////////
			} else if (mode.equals("modify_purchase_type"))	{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.updatePurchaseTypeInfo(mid,type,name,is_import,is_return,is_using,is_except, account_type);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_purchase_type";
			
			///////////////////////////
			//	품목별 공급처 관리 저장
			//////////////////////////
			} else if (mode.equals("write_item_supply_info"))	{
						
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				boolean bool = puconfigDAO.isMainSupplyer(item_code,"");
				if (!bool)	is_main_supplyer = "n";
				puconfigDAO.saveItemSupplyInfo(item_code,supplyer_code,order_weight,lead_time,is_trade_now,is_main_supplyer,min_order_quantity,max_order_quantity,order_unit,supplyer_item_code,supplyer_item_name,supplyer_item_desc,maker_name,supply_unit_cost,request_unit_cost);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_item_supply_info";
			
			/////////////////////////////
			//	품목별 공급처 관리 Update
			////////////////////////////
			} else if (mode.equals("modify_item_supply_info"))	{

				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				
				boolean bool = puconfigDAO.isMainSupplyer(item_code,mid);
				if (!bool)	is_main_supplyer = "n";				
				puconfigDAO.updateItemSupplyInfo(mid,item_code,supplyer_code,order_weight,lead_time,is_trade_now,is_main_supplyer,min_order_quantity,max_order_quantity,order_unit,supplyer_item_code,supplyer_item_name,supplyer_item_desc,maker_name,supply_unit_cost,request_unit_cost);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_item_supply_info";
			}

			///////////////////////////
			//	품목단가정보등록
			//////////////////////////
			else if (mode.equals("write_unit_cost"))		{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con); 
				puconfigDAO.saveUnitCostInfo(item_type,item_code,item_name,item_desc,unit_cost_a,unit_cost_s,unit_cost_c,last_updated_date);

				redirectUrl = "PurchaseConfigMgrServlet?mode=list_item_cost";
			}
			
			///////////////////////////
			//	품목단가정보수정
			//////////////////////////
			else if (mode.equals("modify_unit_cost"))		{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con); 
				puconfigDAO.updateUnitCostInfo(item_type,item_code,item_name,item_desc,unit_cost_a,unit_cost_s,unit_cost_c,last_updated_date);

				redirectUrl = "PurchaseConfigMgrServlet?mode=list_item_cost&item_code="+item_code;
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
