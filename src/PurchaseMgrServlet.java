import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class PurchaseMgrServlet extends HttpServlet {

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
		
		String mid			= request.getParameter("mid")==null?"":request.getParameter("mid");
		
		String redirectUrl	= "";
		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page");
		String searchword	= request.getParameter("searchword");
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");
		int l_maxlist		= request.getParameter("l_maxlist") == null?15:Integer.parseInt(request.getParameter("l_maxlist"));

		String request_no	= request.getParameter("request_no");
		String item_code	= request.getParameter("item_code");
		String order_no		= request.getParameter("order_no");
		String enter_no		= request.getParameter("enter_no");
		String request_type = request.getParameter("request_type"); //요청구분
		String assign_rule	= request.getParameter("assign_rule");	//자동업체배정시의 기준(발주배정치 or 단가)
		String inout_type	= request.getParameter("inout_type");
		String tablename	= request.getParameter("tablename");	
		String aid			= request.getParameter("aid");			// 결재번호
		String supplyer_code= request.getParameter("supplyer_code");// 공급업체코드

		if (mode == null) mode = "list_item";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";
		if (assign_rule == null) assign_rule = "order_weight";

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
			//  구매 요청 및 수정 폼
			///////////////////////////////////////////////////
			if (mode.equals("request_info") || mode.equals("modify_request_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.RequestInfoTable table = new com.anbtech.pu.entity.RequestInfoTable();
				ArrayList arry =  new ArrayList();

				//선택된 구매요청번호에 대한 정보를 가져온다.
				table = purchaseBO.getRequestInfoForm(mode,request_no,item_code,login_id,request_type);
				request.setAttribute("REQUEST_INFO",table);
			
				// 첨부화일 정보 가져오기
				if ("modify_request".equals(mode)){
					arry = (ArrayList)purchaseDAO.getFile_list("pu_requested_item",table.getMid());		
					request.setAttribute("ITEM_FILE",arry);
				}	

				// 선택된 구매요청번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getRequestItemList(table.getRequestNo());
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getLinkForRequestItem(mode,searchword,searchscope,page,request_no,item_code,table.getProcessStat());
				request.setAttribute("Redirect",redirect);
				getServletContext().getRequestDispatcher("/pu/request/request_info.jsp?mode="+mode).forward(request,response);

			}
			
			////////////////////////////////////////////////////
			// 구매요청품목 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if(mode.equals("request_item") || mode.equals("request_item_add") || mode.equals("modify_request")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.RequestInfoTable table = new com.anbtech.pu.entity.RequestInfoTable();
				ArrayList arry =  new ArrayList();

				// 선택된 구매요청번호 및 품목코드에 대한 정보를 가져온다.
				table = purchaseBO.getRequestItemForm(mode,request_no,item_code,login_id,request_type);
				request.setAttribute("REQUEST_INFO",table);

				// 첨부화일 정보 가져오기
				if ("modify_request".equals(mode)){
					arry = (ArrayList)purchaseDAO.getFile_list("pu_requested_item",table.getMid());		
					request.setAttribute("ITEM_FILE",arry);
				}	

				// 선택된 구매요청번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getRequestItemList(table.getRequestNo());
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getLinkForRequestItem(mode,searchword,searchscope,page,request_no,item_code,table.getProcessStat());
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/pu/request/request_item.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 품목의 발주업체 자동배정 실행하기
			///////////////////////////////////////////////////
			else if(mode.equals("auto_supplyer_assign")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				purchaseBO.updateRequestItemInfoBySupplyInfo(request_no,assign_rule);

				// 발주예상총액 계산 및 update
				String request_total_mount = purchaseDAO.calculateRequestTotalMount(request_no);
				request_total_mount = Double.toString(Math.round(Double.parseDouble(request_total_mount)));
				purchaseDAO.updateRequestTotalMount(request_no,request_total_mount);

				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no;
			}

			////////////////////////////////////////////////////
			// 구매요청 조회 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("request_search")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				ArrayList request_list = new ArrayList();

				request_list = purchaseDAO.getRequestInfoList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("REQUEST_LIST",request_list);

				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getRedirect(mode,searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);

				getServletContext().getRequestDispatcher("/pu/request/request_search.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 구매요청 집계 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("requested_list")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				ArrayList request_list = new ArrayList();

				request_list = purchaseDAO.getRequestItemList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("REQUEST_LIST",request_list);
				
				getServletContext().getRequestDispatcher("/pu/request/requested_list.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 선택된 구매요청품목 삭제하기
			///////////////////////////////////////////////////
			else if(mode.equals("delete_request")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				mid = purchaseDAO.getMid("pu_requested_item",request_no,item_code);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				try{
					//1.첨부파일 삭제하기
					String filepath = getServletContext().getRealPath("") + "/upload/pu/estimate/";
					for(int i=1; i<10; i++){
						java.io.File f = new java.io.File(filepath + "/" + mid + "_" + i + ".bin");
						if(f.exists()) f.delete();
					}

					//품목정보 삭제
					purchaseDAO.deleteRequestItem(request_no,item_code);

					// 발주예상총액 계산 및 update
					String request_total_mount = purchaseDAO.calculateRequestTotalMount(request_no);
					purchaseDAO.updateRequestTotalMount(request_no,request_total_mount);

					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no;
			}

			////////////////////////////////////////////////////
			// 선택된 구매요청건 일괄 삭제하기
			///////////////////////////////////////////////////
			else if(mode.equals("delete_request_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					purchaseBO.deleteAllRequestItems(request_no);
					purchaseDAO.deleteRequestInfo(request_no);

					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			
				redirectUrl = "PurchaseMgrServlet?mode=request_search";
			}

			////////////////////////////////////////////////////
			// 구매요청건 결재정보 및 인쇄폼
			///////////////////////////////////////////////////
			else if(mode.equals("request_app_view") || mode.equals("request_app_print") || mode.equals("request_info_print")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.RequestInfoTable table = new com.anbtech.pu.entity.RequestInfoTable();
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
				ArrayList arry = new ArrayList();

		
				// 요청정보
				table = purchaseBO.getRequestInfoForm(mode,request_no,item_code,login_id,request_type);
				request.setAttribute("REQUEST_INFO",table);
				
				// 품목정보 List
				// 선택된 구매요청번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getRequestItemList(request_no);
				request.setAttribute("ITEM_LIST",item_list);

				if(mode.equals("request_app_print")) {
					//결재 관리번호를 가져온 후, 관리번호에 해당하는 결재 정보를 가져온다.
					aid = purchaseDAO.getAid(mode,"pu_requested_info",request_no);
					String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
					if(!aid.equals("")) appTable = appMgrDAO.getApprovalInfo("pu_approval_info",aid,sign_path);
					request.setAttribute("Approval_Info",appTable);
				}
		
				if(mode.equals("request_app_print")){
					getServletContext().getRequestDispatcher("/pu/app/request_app_print.jsp?mode="+mode).forward(request,response);
				}else if(mode.equals("request_app_view")){
					getServletContext().getRequestDispatcher("/pu/app/request_app_view.jsp?mode="+mode).forward(request,response);
				}else if(mode.equals("request_info_print")){
					getServletContext().getRequestDispatcher("/pu/request/request_info_print.jsp?mode="+mode).forward(request,response);
				}
			}

			////////////////////////////////////////////////////
			//  구매발주 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if (mode.equals("order_info") || mode.equals("modify_order_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//선택된 발주번호에 대한 정보를 가져온다.
				table = purchaseBO.getOrderInfoForm(mode,order_no,item_code,login_id);
				request.setAttribute("ORDER_INFO",table);

				//선택된 발주번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getOrderItemList(order_no);
				request.setAttribute("ITEM_LIST",item_list);
			
				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getLinkForOrderItem(mode,searchword,searchscope,page,order_no,item_code,table.getProcessStat());
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/pu/order/order_info.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 발주품목 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if(mode.equals("order_item") || mode.equals("order_item_add") || mode.equals("modify_order")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//item_code != null 즉, 구매요청집계화면에서 품목선택 후 발주등록 화면으로 분기했을 때
				//item_code 에 넘어온 품목들을 각각 db에 저장한다.
				//item_code == "요청번호|품목번호,요청번호|품목번호,...." 식임.
				if(mode.equals("order_item") && item_code != null){
					//발주번호
					order_no = purchaseDAO.getOrderNo(inout_type);

					//등록시간
					java.util.Date now = new java.util.Date();
					java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
					String order_date = vans.format(now);
					
					//발주품목정보 저장
					purchaseBO.saveOrderItem(mode,item_code,order_no);
					
					//세액 및 발주총계정보 업데이트
					String order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
					String vat_mount = Double.toString(Math.round(Double.parseDouble(order_total_mount) * 10 / 100));
					
					//발주정보 저장
					purchaseDAO.saveOrderInfo(order_no,"","","",order_date,"KRW","0",order_total_mount,"10","0","","","","",login_id,"","","1","","","","",inout_type,order_no.substring(2,4),order_no.substring(4,6),order_no.substring(7,10));
				}
				
				//기존 발주건에 품목 추가
				//입력된 발주건의 상태가 S05(발주준비)인지를 체크, 아니면 에러 메시지 출력
				else if(mode.equals("order_item_add") && item_code != null && order_no != null){
					String porcess_stat = purchaseDAO.getMaxStatForOrderItem(order_no);
					if(porcess_stat.equals("S05")){
						purchaseBO.saveOrderItem(mode,item_code,order_no);
					}else{
						PrintWriter out = response.getWriter();
						out.println("<script>");
						out.println("alert('발주번호:" + order_no + "는(은) 현재 발주준비상태가 아닙니다. 품목을 추가할 수 없습니다.');");
						out.println("history.go(-1);");
						out.println("</script>");
						out.close();
						return;
					}
				}

				//선택된 발주번호 및 품목코드에 대한 정보를 가져온다.
				table = purchaseBO.getOrderItemForm(mode,order_no,item_code,request_no,login_id);
				request.setAttribute("ORDER_INFO",table);

				//선택된 발주번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getOrderItemList(order_no);
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getLinkForOrderItem(mode,searchword,searchscope,page,order_no,item_code,table.getProcessStat());
				request.setAttribute("Redirect",redirect);
				
				if(mode.equals("order_item")){
					redirectUrl = "PurchaseMgrServlet?mode=modify_order_info&order_no="+order_no;
				}else{
					getServletContext().getRequestDispatcher("/pu/order/order_item.jsp?mode="+mode).forward(request,response);
				}
			}

			////////////////////////////////////////////////////
			// 발주품목요청 결재 화면
			///////////////////////////////////////////////////
			else if(mode.equals("order_app_view") || mode.equals("order_app_print")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

				//선택된 발주번호 및 품목코드에 대한 정보를 가져온다.
				table = purchaseBO.getOrderItemForm(mode,order_no,item_code,request_no,login_id);
				request.setAttribute("ORDER_INFO",table);

				//선택된 발주번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getOrderItemList(order_no);
				request.setAttribute("ITEM_LIST",item_list);
			
				if(mode.equals("order_app_print")) {
					//결재 관리번호를 가져온 후, 관리번호에 해당하는 결재 정보를 가져온다.
					aid = purchaseDAO.getAid(mode,"pu_order_info",order_no);
					
					String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
					if(!aid.equals("")) appTable = appMgrDAO.getApprovalInfo("pu_approval_info",aid,sign_path);
					request.setAttribute("Approval_Info",appTable);
				}
				
				if(mode.equals("order_app_print")){
					getServletContext().getRequestDispatcher("/pu/app/order_app_print.jsp?mode="+mode).forward(request,response);
				}else{
					getServletContext().getRequestDispatcher("/pu/app/order_app_view.jsp?mode="+mode).forward(request,response);
				}
				
			}
			
			////////////////////////////////////////////////////
			// 발주요청 조회 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("order_search")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				ArrayList order_list = new ArrayList();

				order_list = purchaseDAO.getOrderInfoList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ORDER_LIST",order_list);
			
				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getRedirect(mode,searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);	

				getServletContext().getRequestDispatcher("/pu/order/order_search.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 발주요청 집계 리스트(입고대상품목)
			///////////////////////////////////////////////////
			else if(mode.equals("ordered_list")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				ArrayList order_list = new ArrayList();

				order_list = purchaseDAO.getOrderItemList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ORDER_LIST",order_list);

				getServletContext().getRequestDispatcher("/pu/order/ordered_list.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 구매입고대상품목에 저장된 메모리스트 가져오기
			///////////////////////////////////////////////////
			else if(mode.equals("list_memo")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				ArrayList order_list = new ArrayList();

				order_list = purchaseDAO.getOrderItemList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ORDER_LIST",order_list);

				getServletContext().getRequestDispatcher("/pu/order/list_memo.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 선택된 발주품목 삭제하기
			///////////////////////////////////////////////////
			else if(mode.equals("delete_order")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//삭제대상 발주품목의 발주수량값을 가져온다.
				table = purchaseDAO.getOrderItemInfo(order_no,item_code,request_no);
				String order_quantity = table.getOrderQuantity();
				
				//발주품목 삭제처리
				purchaseDAO.deleteOrderItem(order_no,item_code);

				//세액 및 발주총계정보 업데이트
				table = purchaseDAO.getOrderInfo(order_no);
				
				String order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
				String vat_rate = table.getVatRate();
				String vat_mount = Double.toString(Math.round(Double.parseDouble(order_total_mount) * Double.parseDouble(vat_rate) / 100));

				//발주정보 업데이트
				purchaseDAO.updateOrderInfo(order_no,table.getOrderType(),table.getOrderDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),table.getSupplyerCode(),table.getSupplyerName(),order_total_mount,table.getMonetaryUnit(),table.getExchangeRate(),table.getVatType(),table.getVatRate(),vat_mount,table.getIsVatContained(),table.getApprovalType(),table.getApprovalPeriod(),table.getPaymentType(),table.getSupplyerInfo(),table.getSupplyerTel(),table.getOtherInfo());

				//pu_requested_item 테이블의 상태코드 및 발주수량 업데이트
				purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S03");
				purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"order_quantity","-"+order_quantity);
				
				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no="+order_no;
			}

			////////////////////////////////////////////////////
			// 선택된 발주건 일괄 삭제하기
			///////////////////////////////////////////////////
			else if(mode.equals("delete_order_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					purchaseBO.deleteAllOrderItems(order_no);
					purchaseDAO.deleteOrderInfo(order_no);
					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			
				redirectUrl = "PurchaseMgrServlet?mode=order_search";
			}

			////////////////////////////////////////////////////
			//  구매입고정보 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if (mode.equals("enter_info") || mode.equals("modify_enter_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
				ArrayList arry = new ArrayList();

				//선택된 입고번호 및 품목코드에 대한 정보를 가져온다.
				table = purchaseBO.getEnterInfoForm(mode,enter_no,item_code,login_id);
				request.setAttribute("ENTER_INFO",table);
				
				// 첨부화일 정보 가져오기
				if ("modify_enter_info".equals(mode)){
					arry = (ArrayList)purchaseDAO.getEnteredFile_list("pu_entered_info",table.getMid());
					request.setAttribute("ITEM_FILE",arry);
				}

				//선택된 입고번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getEnterItemList(enter_no);
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getLinkForEnterItem(mode,searchword,searchscope,page,enter_no,item_code,table.getProcessStat());
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/pu/enter/enter_info.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 구매입고품목 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if(mode.equals("enter_item") || mode.equals("enter_item_add") || mode.equals("modify_enter")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//item_code != null 즉, 발주정보집계화면에서 품목선택 후 구매입고 화면으로 분기했을 때
					//item_code 에 넘어온 품목들을 각각 db에 저장한다.
					//item_code == "요청번호|발주번호|품목번호,요청번호|발주번호|품목번호,...." 식임.
					if(mode.equals("enter_item") && item_code != null){
						//입고번호
						enter_no = purchaseDAO.getEnterNo();

						//등록시간
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String enter_date = vans.format(now);

						//선택된 입고품목을 저장한다.
						purchaseBO.saveEnterItem(mode,item_code,enter_no);

						//세액 및 발주총계정보 업데이트
						String enter_total_mount = purchaseDAO.calculateEnterTotalMount(enter_no);

						//공급업체명 가져오기
						com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
						String supplyer_name = crmDAO.getCompanyNameByNo(supplyer_code);
						purchaseDAO.saveEnterInfo(enter_no,"S18",enter_date,enter_total_mount,"KRW","PI",supplyer_code,supplyer_name,enter_no.substring(2,4),enter_no.substring(4,6),enter_no.substring(7,10),"","",login_id,"");
					}
					//기존 입고건에 입고품목 추가
					//기존 입고건의 상태가 S18(입고등록)이 아니거나 
					else if(mode.equals("enter_item_add") && item_code != null && enter_no != null){
						String porcess_stat = purchaseDAO.getMaxStatForEnterItem(enter_no);
						if(porcess_stat.equals("S18")){
							purchaseBO.saveEnterItem(mode,item_code,enter_no);
						}else{
							PrintWriter out = response.getWriter();
							out.println("<script>");
							out.println("alert('입고번호:" + enter_no + "는(은) 현재 입고등록상태가 아닙니다. 품목을 추가할 수 없습니다.');");
							out.println("history.go(-1);");
							out.println("</script>");
							out.close();
							return;
						}
					}

					con.commit();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//선택된 입고번호 및 품목코드에 대한 정보를 가져온다.
				table = purchaseBO.getEnterItemForm(mode,enter_no,item_code,order_no,login_id);
				request.setAttribute("ENTER_INFO",table);

				//선택된 입고번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getEnterItemList(enter_no);
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getLinkForEnterItem(mode,searchword,searchscope,page,enter_no,item_code,table.getProcessStat());
				request.setAttribute("Redirect",redirect);
				
				if(mode.equals("enter_item")){
					redirectUrl = "PurchaseMgrServlet?mode=modify_enter_info&enter_no="+enter_no;
				}else{
					getServletContext().getRequestDispatcher("/pu/enter/enter_item.jsp?mode="+mode).forward(request,response);
				}
			}

			////////////////////////////////////////////////////
			//  구매입고 전자결재 화면
			///////////////////////////////////////////////////
			else if (mode.equals("enter_app_view") || mode.equals("enter_app_print")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
				ArrayList arry = new ArrayList();

				//선택된 입고번호 및 품목코드에 대한 정보를 가져온다.
				table = purchaseDAO.getEnterInfo(enter_no);
				request.setAttribute("ENTER_INFO",table);
				
				//선택된 입고번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getEnterItemList(enter_no);
				request.setAttribute("ITEM_LIST",item_list);

				if(mode.equals("enter_app_print")) {
					//결재 관리번호를 가져온 후, 관리번호에 해당하는 결재 정보를 가져온다.
					aid = purchaseDAO.getAid(mode,"pu_entered_info",enter_no);
					
					String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
					if(!aid.equals("")) appTable = appMgrDAO.getApprovalInfo("pu_approval_info",aid,sign_path);
					request.setAttribute("Approval_Info",appTable);
				}
				
				if(mode.equals("enter_app_print")){
					getServletContext().getRequestDispatcher("/pu/app/enter_app_print.jsp?mode="+mode).forward(request,response);
				}else{
					getServletContext().getRequestDispatcher("/pu/app/enter_app_view.jsp?mode="+mode).forward(request,response);
				}
			}

/*
			////////////////////////////////////////////////////
			// 구매입고품목 등록 및 수정 폼
			//
			// 구매입고등록시의 주요 흐름
			// ===========================================================================
			// 1.구매입고품목정보 pu_entered_item 테이블에 저장한다.
			// 2.pu_requested_item 테이블내 해당 품목의 상태코드와 발주수량을 업데이트
			// 3.pu_order_item 테이블내 해당 품목의 상태코드와 발주수량을 업데이트
			// 4.원가를 계산하여 st_inout_master 테이블에 수불정보를 기록한다.
			// 5.재고정보의 입고예정수량 업데이트
			// 6.품목원가정보 업데이트
			// 7.품질검사 의뢰정보를 등록한다.
			// ===========================================================================
			///////////////////////////////////////////////////
			else if(mode.equals("enter_item") || mode.equals("enter_item_add") || mode.equals("modify_enter")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//item_code != null 즉, 발주정보집계화면에서 품목선택 후 구매입고 화면으로 분기했을 때
					//item_code 에 넘어온 품목들을 각각 db에 저장한다.
					//item_code == "요청번호|발주번호|품목번호,요청번호|발주번호|품목번호,...." 식임.
					if(mode.equals("enter_item") && item_code != null){
						//입고번호
						enter_no = purchaseDAO.getEnterNo();

						//등록시간
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String enter_date = vans.format(now);

						//선택된 입고품목을 저장한다.
						purchaseBO.saveEnterItem(mode,item_code,enter_no);

						//세액 및 발주총계정보 업데이트
						String enter_total_mount = purchaseDAO.calculateEnterTotalMount(enter_no);
						purchaseDAO.saveEnterInfo(enter_no,"S21",enter_date,enter_total_mount,"KRW","","","",enter_no.substring(2,4),enter_no.substring(4,6),enter_no.substring(7,10),"","",login_id,"");
					}

					else if(mode.equals("enter_item_add") && item_code != null && enter_no != null){
						purchaseBO.saveEnterItem(mode,item_code,enter_no);
					}

					con.commit();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//선택된 입고번호 및 품목코드에 대한 정보를 가져온다.
				table = purchaseBO.getEnterItemForm(mode,enter_no,item_code,order_no,login_id);
				request.setAttribute("ENTER_INFO",table);

				//선택된 입고번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getEnterItemList(enter_no);
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getLinkForEnterItem(mode,searchword,searchscope,page,enter_no,item_code,table.getProcessStat());
				request.setAttribute("Redirect",redirect);
				
				if(mode.equals("enter_item")){
					redirectUrl = "PurchaseMgrServlet?mode=modify_enter_info&enter_no="+enter_no;
				}else{
					getServletContext().getRequestDispatcher("/pu/enter/enter_item.jsp?mode="+mode).forward(request,response);
				}
			}
*/

			////////////////////////////////////////////////////
			// 구매입고정보 조회 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("enter_search")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				ArrayList enter_list = new ArrayList();

				enter_list = purchaseDAO.getEnterInfoList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ENTER_LIST",enter_list);
			
				com.anbtech.pu.business.PurchaseLinkUrlBO redirectBO = new com.anbtech.pu.business.PurchaseLinkUrlBO(con);
				com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
				redirect = redirectBO.getRedirect(mode,searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);	
				
				getServletContext().getRequestDispatcher("/pu/enter/enter_search.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 구매입고정보 집계 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("entered_list")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				ArrayList enter_list = new ArrayList();

				//최초 실행시 오늘날짜에 입고된 품목을 검색한다.
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
				if(searchword.equals("")) searchword = "enter_date|" + vans.format(now) + vans.format(now);

				enter_list = purchaseDAO.getEnterItemList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ENTER_LIST",enter_list);

				//총 입고금액 계산
				String total_cost = purchaseDAO.getTotalEnterCost(mode,searchword,searchscope,login_id);
				request.setAttribute("TOTAL_COST",total_cost);

				getServletContext().getRequestDispatcher("/pu/enter/entered_list.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 선택된 입고품목 삭제하기
			///////////////////////////////////////////////////
			else if(mode.equals("delete_enter")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//입고수량 파악하기
					table = purchaseDAO.getEnterItemInfo(enter_no,item_code);
					String enter_quantity = table.getEnterQuantity();

					//입고품목 삭제하기
					purchaseDAO.deleteEnterItem(enter_no,item_code);

					table = purchaseDAO.getEnterInfo(enter_no);

					String enter_total_mount = purchaseDAO.calculateEnterTotalMount(enter_no);
					purchaseDAO.updateEnterInfo(enter_no,table.getEnterType(),table.getEnterDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),enter_total_mount,table.getMonetaryUnit(),table.getSupplyerCode(),table.getSupplyerName());

					//pu_requested_item 테이블의 상태코드 및 입고수량 업데이트
					purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S13");
					purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"delivery_quantity","-"+enter_quantity);

					//pu_order_item 테이블의 상태코드 및 입고수량 업데이트
					purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,item_code,"S13");
					purchaseDAO.updateQuantity("pu_order_item","order_no",order_no,item_code,"delivery_quantity","-"+enter_quantity);

					//수불현황 기록을 삭제한다.
//					com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
//					stBO.deleteInOutInfoForPurchasedInputItem(enter_no,request_no,item_code);

					//품질검사의뢰 정보를 삭제한다.
//					com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
//					qcDAO.deleteInspectionInfo(item_code,enter_no);

					con.commit();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				
				redirectUrl = "PurchaseMgrServlet?mode=enter_item_add&enter_no="+enter_no;
			}

			////////////////////////////////////////////////////
			// 선택된 입고건 일괄 삭제하기
			///////////////////////////////////////////////////
			else if(mode.equals("delete_enter_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					purchaseBO.deleteAllEnterItems(enter_no);
					purchaseDAO.deleteEnterInfo(enter_no);
					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			
				redirectUrl = "PurchaseMgrServlet?mode=enter_search";

			}

			////////////////////////////////////////////////////
			// 발주서 출력
			///////////////////////////////////////////////////
			else if(mode.equals("order_print") || mode.equals("order_print2")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//선택된 발주번호 및 품목코드에 대한 정보를 가져온다.
				table = purchaseDAO.getOrderInfo(order_no);
				request.setAttribute("ORDER_INFO",table);

				//선택된 발주번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getOrderItemList(order_no);
				request.setAttribute("ITEM_LIST",item_list);

				String url = "";
				if(mode.equals("order_print")) url = "/pu/order/order_print.jsp?mode="+mode;
				else if(mode.equals("order_print2")) url = "/pu/order/order_print2.jsp?mode="+mode;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

						
			////////////////////////////////////////////////////////
			//  download 
			////////////////////////////////////////////////////////
			else if ("download".equals(mode)){
				
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.entity.RequestInfoTable file = new com.anbtech.pu.entity.RequestInfoTable();
				//String tablename = "pu_requested_item";
				file = purchaseBO.getFile_fordown("pu_requested_item", mid);
				
				//	file_arry = purchaseBO.getFile_Enterfordown(tablename, mid);
							
				String filename = file.getFname();
				String filetype = file.getFtype();
				String filesize = file.getFsize();
				String fileumask = file.getFumask();
				
				//boardpath 에서 파일까지 경로 지정
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/em/estimate/" + fileumask + ".bin";
			
				if (filetype.indexOf("mage")<=0)
					filetype = "application/unknown";
				
				String strClient=request.getHeader("User-Agent");
				filename = new String(filename.getBytes("euc-kr"),"8859_1");

				if(strClient.indexOf("MSIE 5.5")>-1) 	response.setHeader("Content-Disposition","filename="+filename);
				else response.setHeader("Content-Disposition","attachment; filename="+filename);

				response.setContentType(filetype);
				response.setContentLength(Integer.parseInt(filesize));
					
				byte b[] = new byte[Integer.parseInt(filesize)];
				java.io.File f = new java.io.File(downFile);
				java.io.FileInputStream fin = new java.io.FileInputStream(f);
				ServletOutputStream fout = response.getOutputStream();
				fin.read(b);
				fout.write(b,0,Integer.parseInt(filesize));
				fout.close();
			}
		
			////////////////////////////////////////////////////////
			//  download 
			////////////////////////////////////////////////////////
			else if ("enterfile_download".equals(mode)){
				
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.entity.EnterInfoTable file = new com.anbtech.pu.entity.EnterInfoTable();
				//String tablename = "pu_requested_item";
				//file_arry = purchaseBO.getFile_fordown("pu_requested_item", mid);
				
				file = purchaseBO.getFile_Enterfordown("pu_entered_info", mid);
							
				String filename = file.getFname();
				String filetype = file.getFtype();
				String filesize = file.getFsize();
				String fileumask = file.getFumask();
				
				//boardpath 에서 파일까지 경로 지정
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/em/receipt/" + fileumask + ".bin";
			
				if (filetype.indexOf("mage")<=0)
					filetype = "application/unknown";
				
				String strClient=request.getHeader("User-Agent");
				filename = new String(filename.getBytes("euc-kr"),"8859_1");

				if(strClient.indexOf("MSIE 5.5")>-1) 	response.setHeader("Content-Disposition","filename="+filename);
				else response.setHeader("Content-Disposition","attachment; filename="+filename);

				response.setContentType(filetype);
				response.setContentLength(Integer.parseInt(filesize));
					
				byte b[] = new byte[Integer.parseInt(filesize)];
				java.io.File f = new java.io.File(downFile);
				java.io.FileInputStream fin = new java.io.FileInputStream(f);
				ServletOutputStream fout = response.getOutputStream();
				fin.read(b);
				fout.write(b,0,Integer.parseInt(filesize));
				fout.close();
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
		String upload_folder = request.getParameter("upload_folder");
		if(upload_folder == null) upload_folder = "";
		
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/pu/" + upload_folder;

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//전페이지에서 값 받아온다. multi에서 가져옴
		String mode				= multi.getParameter("mode");
		String page				= multi.getParameter("page");
		String searchword		= multi.getParameter("searchword");
		String searchscope		= multi.getParameter("searchscope");
		String category			= multi.getParameter("category");
		String redirectUrl		= "";

		//공백으로 넘어오거나 null로 온 값들에 대한 처리.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

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

		//구매요청의뢰 시
		String request_no		= multi.getParameter("request_no");
		String request_date		= multi.getParameter("request_date");
		String requester_div_name	= multi.getParameter("requester_div_name");
		String requester_div_code	= multi.getParameter("requester_div_code");
		String requester_id		= multi.getParameter("requester_id");
		String requester_info	= multi.getParameter("requester_info");
		String item_code		= multi.getParameter("item_code");
		String item_name		= multi.getParameter("item_name");
		String item_desc		= multi.getParameter("item_desc");
		String request_quantity	= multi.getParameter("request_quantity");
		String request_unit		= multi.getParameter("request_unit");
		String delivery_date	= multi.getParameter("delivery_date");
		String request_type		= multi.getParameter("request_type");
		String supply_cost		= multi.getParameter("supply_cost");			// 공급희망단가
		String request_cost		= multi.getParameter("request_cost");			// 구매요청금액
		String request_total_mount = multi.getParameter("request_total_mount");	// 구매요청총금액
		String project_code		= multi.getParameter("project_code");			// 프로젝트코드
		String project_name		= multi.getParameter("project_name");			// 프로젝트명

		//발주등록 시
		String order_no			= multi.getParameter("order_no");
		String order_type		= multi.getParameter("order_type");
		String process_stat		= multi.getParameter("process_stat");
		String order_date		= multi.getParameter("order_date");
		String supplyer_code	= multi.getParameter("supplyer_code");
		String supplyer_name	= multi.getParameter("supplyer_name");
		String order_total_mount= multi.getParameter("order_total_mount");
		String monetary_unit	= multi.getParameter("monetary_unit");
		String exchange_rate	= multi.getParameter("exchange_rate");
		String vat_type			= multi.getParameter("vat_type");
		String vat_rate			= multi.getParameter("vat_rate"); 
		String vat_mount		= multi.getParameter("vat_mount");
		String is_vat_contained	= multi.getParameter("is_vat_contained");
		String approval_type	= multi.getParameter("approval_type");
		String approval_period	= multi.getParameter("approval_period");
		String payment_type		= multi.getParameter("payment_type");
		String supplyer_info	= multi.getParameter("supplyer_info");
		String supplyer_tel		= multi.getParameter("supplyer_tel");
		String other_info		= multi.getParameter("other_info");
		String inout_type		= multi.getParameter("inout_type");
		String order_quantity	= multi.getParameter("order_quantity");
		String old_quantity		= multi.getParameter("old_quantity");
		String order_unit		= multi.getParameter("order_unit");
		String unit_cost		= multi.getParameter("unit_cost");
		String order_cost		= multi.getParameter("order_cost");

		//입고등록 시
		String enter_no			= multi.getParameter("enter_no");
		String enter_type		= multi.getParameter("enter_type");
		String enter_date		= multi.getParameter("enter_date");
		String enter_total_mount= multi.getParameter("enter_total_mount");
		String enter_quantity	= multi.getParameter("enter_quantity");
		String enter_unit		= multi.getParameter("enter_unit");
		String enter_cost		= multi.getParameter("enter_cost");
		String factory_code		= multi.getParameter("factory_code");
		String factory_name		= multi.getParameter("factory_name");
		String warehouse_code	= multi.getParameter("warehouse_code");
		String warehouse_name	= multi.getParameter("warehouse_name");

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
						
			////////////////////////
			// 구매요청등록처리
			////////////////////////
			if (mode.equals("request_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);

				//request_total_mount = Double.toString(Math.round(Double.parseDouble(request_total_mount)));
				//요청번호 생성
				request_no = purchaseDAO.getRequestNo();
				purchaseDAO.saveRequestInfo(request_no,request_date,requester_div_code,requester_div_name,requester_id,requester_info,request_type,request_total_mount,project_code,project_name,request_no.substring(3,5),request_no.substring(5,7),request_no.substring(8,11),factory_code,factory_name);
				
				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no;
			}  

			//////////////////////////
			// 구매요청품목등록처리
			//////////////////////////
			else if(mode.equals("request_item_add")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.business.PurchaseMgrBO   purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.entity.RequestInfoTable file = new com.anbtech.pu.entity.RequestInfoTable();
				
				//중복체크
				//같은 요청번호에 같은 품목을 구매요청할 수 없도록 한다.
				int same_item_cnt = purchaseDAO.getSameRequestItemCount(request_no,item_code);
				if(same_item_cnt > 0){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('품목 " + item_code + "는(은) 이미 구매요청품목에 등록되어 있습니다. 중복 등록할 수 없습니다.');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}else{
					purchaseDAO.saveRequestItemInfo(request_no,item_code,item_name,item_desc,request_unit,delivery_date,request_quantity,"0","0","S01",supplyer_code,supplyer_name,supply_cost,request_cost);

					//request_no 와 item_code 에 해당하는 관리번호를 가져온다.
					String mid = purchaseDAO.getMid("pu_requested_item",request_no,item_code);
				
					//첨부파일 업로더
					file = (com.anbtech.pu.entity.RequestInfoTable)purchaseBO.getFile_frommulti(multi, mid, filepath);
				
					//업로딩 된 첨부파일 정보를 DB에 저장하기
					purchaseBO.updFile("pu_requested_item",mid, file.getFname(), file.getFtype(), file.getFsize(),file.getFumask());

					// 발주예상총액 계산 및 update
					request_total_mount = purchaseDAO.calculateRequestTotalMount(request_no);
					request_total_mount = Double.toString(Math.round(Double.parseDouble(request_total_mount)));

					purchaseDAO.updateRequestTotalMount(request_no,request_total_mount);
				}

				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no+"&request_type="+request_type;
			}

			//////////////////////////
			// 구매요청품목수정처리
			//////////////////////////
			else if(mode.equals("modify_request")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.business.PurchaseMgrBO   purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.entity.RequestInfoTable file = new com.anbtech.pu.entity.RequestInfoTable();
				purchaseDAO.updateRequestItemInfo(request_no,item_code,item_name,item_desc,request_unit,delivery_date,request_quantity,supplyer_code,supplyer_name,supply_cost,request_cost);
				
				//request_no 와 item_code 에 해당하는 관리번호를 가져온다.
				String mid = purchaseDAO.getMid("pu_requested_item",request_no,item_code);
				//multi에서 파일정보를 가져와서 처리한다.
				ArrayList file_list = purchaseDAO.getFile_list("pu_requested_item",mid);
				//파일 업로더
				file = (com.anbtech.pu.entity.RequestInfoTable)purchaseBO.getFile_frommulti(multi, mid, filepath, file_list);
					
				// 화일정보 SAVE
				// 업로딩 된 첨부파일 정보를 DB에 저장하기
				purchaseBO.updFile("pu_requested_item",mid, file.getFname(), file.getFtype(), file.getFsize(),file.getFumask());

				// 발주예상총액 계산 및 update
				request_total_mount = purchaseDAO.calculateRequestTotalMount(request_no);
				request_total_mount = Double.toString(Math.round(Double.parseDouble(request_total_mount)));
				purchaseDAO.updateRequestTotalMount(request_no,request_total_mount);

				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no;
			}

			//////////////////////////
			// 구매요청정보수정처리
			//////////////////////////
			else if(mode.equals("modify_request_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				purchaseDAO.updateRequestInfo(request_no,request_date,requester_div_code,requester_div_name,requester_id,requester_info);

				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no+"&request_type="+request_type;
			}

			////////////////////////
			// 발주정보 등록처리
			////////////////////////
			if(mode.equals("order_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();
				
				//발주번호 계산
				order_no = purchaseDAO.getOrderNo(inout_type);
				purchaseDAO.saveOrderInfo(order_no,order_type,supplyer_code,supplyer_name,order_date,monetary_unit,exchange_rate,order_total_mount, vat_rate,vat_mount,supplyer_info,supplyer_tel,requester_div_code,requester_div_name,requester_id,requester_info,vat_type,is_vat_contained,approval_type,approval_period,payment_type,other_info,inout_type,order_no.substring(2,4),order_no.substring(4,6),order_no.substring(7,10));
					
				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}

			////////////////////////////////////////////
			// 공급업체 지정시 발주정보 및 품목별 공급단가 정보를 업데이트
			////////////////////////////////////////////
			else if(mode.equals("reflect_supply_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//품목별 발주단가 및 발주금액 업데이트
				purchaseBO.updateOrderItemUnitCost(mode,order_no,supplyer_code);

				//공급업체정보 및 발주총계정보 업데이트
				table = purchaseDAO.getOrderInfo(order_no);
				order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
				purchaseDAO.updateOrderInfo(order_no,table.getOrderType(),table.getOrderDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),supplyer_code,supplyer_name,order_total_mount,table.getMonetaryUnit(),table.getExchangeRate(),table.getVatType(),table.getVatRate(),table.getVatMount(),table.getIsVatContained(),table.getApprovalType(),table.getApprovalPeriod(),table.getPaymentType(),table.getSupplyerInfo(),table.getSupplyerTel(),table.getOtherInfo());

				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}
			
			//////////////////////////
			// 발주등록처리(품목추가)
			//////////////////////////
			else if(mode.equals("order_item_add")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();
				purchaseDAO.saveOrderItemInfo(order_no,"긴급발주",item_code,item_name,item_desc,order_quantity,order_unit,delivery_date,unit_cost,order_cost,"0","S05");

				//세액 및 발주총계정보 업데이트
				table = purchaseDAO.getOrderInfo(order_no);
				
				order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
				vat_rate = table.getVatRate();
				vat_mount = Double.toString(Math.round(Double.parseDouble(order_total_mount) * Double.parseDouble(vat_rate) / 100));
				purchaseDAO.updateOrderInfo(order_no,table.getOrderType(),table.getOrderDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),table.getSupplyerCode(),table.getSupplyerName(),order_total_mount,table.getMonetaryUnit(),table.getExchangeRate(),table.getVatType(),table.getVatRate(),vat_mount,table.getIsVatContained(),table.getApprovalType(),table.getApprovalPeriod(),table.getPaymentType(),table.getSupplyerInfo(),table.getSupplyerTel(),table.getOtherInfo());

				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}

			//////////////////////////
			// 발주품목수정처리
			//////////////////////////
			else if(mode.equals("modify_order")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//품목정보수정
				purchaseDAO.updateOrderItemInfo(order_no,item_code,item_name,item_desc,order_quantity,order_unit,unit_cost,"y",order_cost,delivery_date,"0",request_no);

				//세액 및 발주총계정보 업데이트
				table = purchaseDAO.getOrderInfo(order_no);
				
				order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
				vat_rate = table.getVatRate();
				vat_mount = Double.toString(Math.round(Double.parseDouble(order_total_mount) * Double.parseDouble(vat_rate) / 100));
				purchaseDAO.updateOrderInfo(order_no,table.getOrderType(),table.getOrderDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),table.getSupplyerCode(),table.getSupplyerName(),order_total_mount,table.getMonetaryUnit(),table.getExchangeRate(),table.getVatType(),table.getVatRate(),vat_mount,table.getIsVatContained(),table.getApprovalType(),table.getApprovalPeriod(),table.getPaymentType(),table.getSupplyerInfo(),table.getSupplyerTel(),table.getOtherInfo());

				//pu_requested_item 테이블내 품목의 발주수량 업데이트
				int distance = Integer.parseInt(order_quantity) - Integer.parseInt(old_quantity);
				purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"order_quantity",Integer.toString(distance));

				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}

			//////////////////////////
			// 발주정보수정처리
			//////////////////////////
			else if(mode.equals("modify_order_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				purchaseDAO.updateOrderInfo(order_no,order_type,order_date,requester_div_code,requester_div_name,requester_id,requester_info,supplyer_code,supplyer_name,order_total_mount,monetary_unit,exchange_rate,vat_type,vat_rate,vat_mount,is_vat_contained,approval_type,approval_period,payment_type,supplyer_info,supplyer_tel,other_info);

				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}

			////////////////////////////////////////////////////
			// 발주품목에 메모 입력처리
			///////////////////////////////////////////////////
			else if(mode.equals("write_memo")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//item_code == "요청번호|발주번호|품목번호,요청번호|발주번호|품목번호,...." 식임.
					if(item_code != null) purchaseBO.saveMemoForOrderItem(mode,item_code,other_info);
					con.commit();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				getServletContext().getRequestDispatcher("/pu/order/save_result.jsp?mode="+mode).forward(request,response);

			}

			//////////////////////////
			// 입고정보수정처리
			//////////////////////////
			else if(mode.equals("modify_enter_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.entity.EnterInfoTable file = new com.anbtech.pu.entity.EnterInfoTable();

				//request_no 와 item_code 에 해당하는 관리번호를 가져온다.
				String mid = purchaseDAO.getEnterMid("pu_entered_info",enter_no);
				//multi에서 파일정보를 가져와서 처리한다.
				ArrayList file_list = purchaseDAO.getEnteredFile_list("pu_entered_info",mid);
				//파일 업로더
				file = (com.anbtech.pu.entity.EnterInfoTable)purchaseBO.getEnterFile_frommulti(multi, mid, filepath, file_list);
					
				// 화일정보 SAVE
				// 업로딩 된 첨부파일 정보를 DB에 저장하기
				purchaseBO.updFile("pu_entered_info",mid, file.getFname(), file.getFtype(), file.getFsize(),file.getFumask());

				purchaseDAO.updateEnterInfo(enter_no,enter_type,enter_date,requester_div_code,requester_div_name,requester_id,requester_info,enter_total_mount,monetary_unit,supplyer_code,supplyer_name);

				redirectUrl = "PurchaseMgrServlet?mode=enter_item_add&enter_no=" + enter_no;
			}

			//////////////////////////
			// 입고품목수정처리
			//////////////////////////
			else if(mode.equals("modify_enter")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.pu.entity.RequestInfoTable table2 = new com.anbtech.pu.entity.RequestInfoTable();
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
				
					//입고품목정보 수정
					purchaseDAO.updateEnterItemInfo(enter_no,item_code,item_name,item_desc,enter_quantity,enter_unit,unit_cost,enter_cost,factory_code,factory_name,warehouse_code,warehouse_name,request_no,order_no);

					//입고정보에서 입고금액합계를 수정
					table = purchaseDAO.getEnterInfo(enter_no);
					enter_total_mount = purchaseDAO.calculateEnterTotalMount(enter_no);
					purchaseDAO.updateEnterInfo(enter_no,table.getEnterType(),table.getEnterDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),enter_total_mount,table.getMonetaryUnit(),table.getSupplyerCode(),table.getSupplyerName());

					int distance = Integer.parseInt(enter_quantity) - Integer.parseInt(old_quantity);

					//pu_requested_item 테이블내 품목의 입고수량 업데이트
					purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"delivery_quantity",Integer.toString(distance));

					//pu_order_item 테이블내 품목의 입고수량 업데이트
					purchaseDAO.updateQuantity("pu_order_item","order_no",order_no,item_code,"delivery_quantity",Integer.toString(distance));

					//////////////////////////////////////////////////////////////
					/////// 여기서부터 재고관리 및 품질관리모듈과 연동되는 부분임 ///////////
					//////////////////////////////////////////////////////////////
					table2 = purchaseDAO.getRequestInfo(request_no);

					//요청구분에 따라 재고관리 모듈과의 연동을 고려한다. MRP,ROP에 의한 입고만 재고관리에 영향을
					//미친다.
					if(table2.getRequestType().equals("MRP") || table2.getRequestType().equals("ROP")){
						//수불현황정보 및 재고DB를 업데이트한다.
						stBO.updateInOutInfoForPurchasedInputItem("MOD",item_code,unit_cost,enter_quantity,enter_no,factory_code,factory_name,warehouse_code,warehouse_name);

						//품질검사의뢰 정보를 업데이트한다.
						//해당 입고번호 및 품목코드에 해당하는 품질검사의뢰건의 수량을 수정한다.
						qcDAO.updInspectionInfo(item_code,enter_no,enter_quantity);
					}

					con.commit();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "PurchaseMgrServlet?mode=enter_item_add&enter_no=" + enter_no;
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
