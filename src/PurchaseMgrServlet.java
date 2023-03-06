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
	 * �Ҹ���
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó��
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
		String request_type = request.getParameter("request_type"); //��û����
		String assign_rule	= request.getParameter("assign_rule");	//�ڵ���ü�������� ����(���ֹ���ġ or �ܰ�)
		String inout_type	= request.getParameter("inout_type");
		String tablename	= request.getParameter("tablename");	
		String aid			= request.getParameter("aid");			// �����ȣ
		String supplyer_code= request.getParameter("supplyer_code");// ���޾�ü�ڵ�

		if (mode == null) mode = "list_item";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";
		if (assign_rule == null) assign_rule = "order_weight";

		//���� �������� ����� ���̵� ��������
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
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////
			//  ���� ��û �� ���� ��
			///////////////////////////////////////////////////
			if (mode.equals("request_info") || mode.equals("modify_request_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.RequestInfoTable table = new com.anbtech.pu.entity.RequestInfoTable();
				ArrayList arry =  new ArrayList();

				//���õ� ���ſ�û��ȣ�� ���� ������ �����´�.
				table = purchaseBO.getRequestInfoForm(mode,request_no,item_code,login_id,request_type);
				request.setAttribute("REQUEST_INFO",table);
			
				// ÷��ȭ�� ���� ��������
				if ("modify_request".equals(mode)){
					arry = (ArrayList)purchaseDAO.getFile_list("pu_requested_item",table.getMid());		
					request.setAttribute("ITEM_FILE",arry);
				}	

				// ���õ� ���ſ�û��ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
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
			// ���ſ�ûǰ�� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if(mode.equals("request_item") || mode.equals("request_item_add") || mode.equals("modify_request")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.RequestInfoTable table = new com.anbtech.pu.entity.RequestInfoTable();
				ArrayList arry =  new ArrayList();

				// ���õ� ���ſ�û��ȣ �� ǰ���ڵ忡 ���� ������ �����´�.
				table = purchaseBO.getRequestItemForm(mode,request_no,item_code,login_id,request_type);
				request.setAttribute("REQUEST_INFO",table);

				// ÷��ȭ�� ���� ��������
				if ("modify_request".equals(mode)){
					arry = (ArrayList)purchaseDAO.getFile_list("pu_requested_item",table.getMid());		
					request.setAttribute("ITEM_FILE",arry);
				}	

				// ���õ� ���ſ�û��ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
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
			// ǰ���� ���־�ü �ڵ����� �����ϱ�
			///////////////////////////////////////////////////
			else if(mode.equals("auto_supplyer_assign")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				purchaseBO.updateRequestItemInfoBySupplyInfo(request_no,assign_rule);

				// ���ֿ����Ѿ� ��� �� update
				String request_total_mount = purchaseDAO.calculateRequestTotalMount(request_no);
				request_total_mount = Double.toString(Math.round(Double.parseDouble(request_total_mount)));
				purchaseDAO.updateRequestTotalMount(request_no,request_total_mount);

				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no;
			}

			////////////////////////////////////////////////////
			// ���ſ�û ��ȸ ����Ʈ
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
			// ���ſ�û ���� ����Ʈ
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
			// ���õ� ���ſ�ûǰ�� �����ϱ�
			///////////////////////////////////////////////////
			else if(mode.equals("delete_request")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				mid = purchaseDAO.getMid("pu_requested_item",request_no,item_code);

				con.setAutoCommit(false);	// Ʈ������� ����
				try{
					//1.÷������ �����ϱ�
					String filepath = getServletContext().getRealPath("") + "/upload/pu/estimate/";
					for(int i=1; i<10; i++){
						java.io.File f = new java.io.File(filepath + "/" + mid + "_" + i + ".bin");
						if(f.exists()) f.delete();
					}

					//ǰ������ ����
					purchaseDAO.deleteRequestItem(request_no,item_code);

					// ���ֿ����Ѿ� ��� �� update
					String request_total_mount = purchaseDAO.calculateRequestTotalMount(request_no);
					purchaseDAO.updateRequestTotalMount(request_no,request_total_mount);

					con.commit(); // commit�Ѵ�.
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
			// ���õ� ���ſ�û�� �ϰ� �����ϱ�
			///////////////////////////////////////////////////
			else if(mode.equals("delete_request_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					purchaseBO.deleteAllRequestItems(request_no);
					purchaseDAO.deleteRequestInfo(request_no);

					con.commit(); // commit�Ѵ�.
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
			// ���ſ�û�� �������� �� �μ���
			///////////////////////////////////////////////////
			else if(mode.equals("request_app_view") || mode.equals("request_app_print") || mode.equals("request_info_print")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.RequestInfoTable table = new com.anbtech.pu.entity.RequestInfoTable();
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
				ArrayList arry = new ArrayList();

		
				// ��û����
				table = purchaseBO.getRequestInfoForm(mode,request_no,item_code,login_id,request_type);
				request.setAttribute("REQUEST_INFO",table);
				
				// ǰ������ List
				// ���õ� ���ſ�û��ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getRequestItemList(request_no);
				request.setAttribute("ITEM_LIST",item_list);

				if(mode.equals("request_app_print")) {
					//���� ������ȣ�� ������ ��, ������ȣ�� �ش��ϴ� ���� ������ �����´�.
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
			//  ���Ź��� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if (mode.equals("order_info") || mode.equals("modify_order_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//���õ� ���ֹ�ȣ�� ���� ������ �����´�.
				table = purchaseBO.getOrderInfoForm(mode,order_no,item_code,login_id);
				request.setAttribute("ORDER_INFO",table);

				//���õ� ���ֹ�ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
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
			// ����ǰ�� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if(mode.equals("order_item") || mode.equals("order_item_add") || mode.equals("modify_order")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//item_code != null ��, ���ſ�û����ȭ�鿡�� ǰ���� �� ���ֵ�� ȭ������ �б����� ��
				//item_code �� �Ѿ�� ǰ����� ���� db�� �����Ѵ�.
				//item_code == "��û��ȣ|ǰ���ȣ,��û��ȣ|ǰ���ȣ,...." ����.
				if(mode.equals("order_item") && item_code != null){
					//���ֹ�ȣ
					order_no = purchaseDAO.getOrderNo(inout_type);

					//��Ͻð�
					java.util.Date now = new java.util.Date();
					java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
					String order_date = vans.format(now);
					
					//����ǰ������ ����
					purchaseBO.saveOrderItem(mode,item_code,order_no);
					
					//���� �� �����Ѱ����� ������Ʈ
					String order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
					String vat_mount = Double.toString(Math.round(Double.parseDouble(order_total_mount) * 10 / 100));
					
					//�������� ����
					purchaseDAO.saveOrderInfo(order_no,"","","",order_date,"KRW","0",order_total_mount,"10","0","","","","",login_id,"","","1","","","","",inout_type,order_no.substring(2,4),order_no.substring(4,6),order_no.substring(7,10));
				}
				
				//���� ���ְǿ� ǰ�� �߰�
				//�Էµ� ���ְ��� ���°� S05(�����غ�)������ üũ, �ƴϸ� ���� �޽��� ���
				else if(mode.equals("order_item_add") && item_code != null && order_no != null){
					String porcess_stat = purchaseDAO.getMaxStatForOrderItem(order_no);
					if(porcess_stat.equals("S05")){
						purchaseBO.saveOrderItem(mode,item_code,order_no);
					}else{
						PrintWriter out = response.getWriter();
						out.println("<script>");
						out.println("alert('���ֹ�ȣ:" + order_no + "��(��) ���� �����غ���°� �ƴմϴ�. ǰ���� �߰��� �� �����ϴ�.');");
						out.println("history.go(-1);");
						out.println("</script>");
						out.close();
						return;
					}
				}

				//���õ� ���ֹ�ȣ �� ǰ���ڵ忡 ���� ������ �����´�.
				table = purchaseBO.getOrderItemForm(mode,order_no,item_code,request_no,login_id);
				request.setAttribute("ORDER_INFO",table);

				//���õ� ���ֹ�ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
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
			// ����ǰ���û ���� ȭ��
			///////////////////////////////////////////////////
			else if(mode.equals("order_app_view") || mode.equals("order_app_print")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

				//���õ� ���ֹ�ȣ �� ǰ���ڵ忡 ���� ������ �����´�.
				table = purchaseBO.getOrderItemForm(mode,order_no,item_code,request_no,login_id);
				request.setAttribute("ORDER_INFO",table);

				//���õ� ���ֹ�ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getOrderItemList(order_no);
				request.setAttribute("ITEM_LIST",item_list);
			
				if(mode.equals("order_app_print")) {
					//���� ������ȣ�� ������ ��, ������ȣ�� �ش��ϴ� ���� ������ �����´�.
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
			// ���ֿ�û ��ȸ ����Ʈ
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
			// ���ֿ�û ���� ����Ʈ(�԰���ǰ��)
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
			// �����԰���ǰ�� ����� �޸𸮽�Ʈ ��������
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
			// ���õ� ����ǰ�� �����ϱ�
			///////////////////////////////////////////////////
			else if(mode.equals("delete_order")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//������� ����ǰ���� ���ּ������� �����´�.
				table = purchaseDAO.getOrderItemInfo(order_no,item_code,request_no);
				String order_quantity = table.getOrderQuantity();
				
				//����ǰ�� ����ó��
				purchaseDAO.deleteOrderItem(order_no,item_code);

				//���� �� �����Ѱ����� ������Ʈ
				table = purchaseDAO.getOrderInfo(order_no);
				
				String order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
				String vat_rate = table.getVatRate();
				String vat_mount = Double.toString(Math.round(Double.parseDouble(order_total_mount) * Double.parseDouble(vat_rate) / 100));

				//�������� ������Ʈ
				purchaseDAO.updateOrderInfo(order_no,table.getOrderType(),table.getOrderDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),table.getSupplyerCode(),table.getSupplyerName(),order_total_mount,table.getMonetaryUnit(),table.getExchangeRate(),table.getVatType(),table.getVatRate(),vat_mount,table.getIsVatContained(),table.getApprovalType(),table.getApprovalPeriod(),table.getPaymentType(),table.getSupplyerInfo(),table.getSupplyerTel(),table.getOtherInfo());

				//pu_requested_item ���̺��� �����ڵ� �� ���ּ��� ������Ʈ
				purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S03");
				purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"order_quantity","-"+order_quantity);
				
				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no="+order_no;
			}

			////////////////////////////////////////////////////
			// ���õ� ���ְ� �ϰ� �����ϱ�
			///////////////////////////////////////////////////
			else if(mode.equals("delete_order_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					purchaseBO.deleteAllOrderItems(order_no);
					purchaseDAO.deleteOrderInfo(order_no);
					con.commit(); // commit�Ѵ�.
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
			//  �����԰����� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if (mode.equals("enter_info") || mode.equals("modify_enter_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
				ArrayList arry = new ArrayList();

				//���õ� �԰��ȣ �� ǰ���ڵ忡 ���� ������ �����´�.
				table = purchaseBO.getEnterInfoForm(mode,enter_no,item_code,login_id);
				request.setAttribute("ENTER_INFO",table);
				
				// ÷��ȭ�� ���� ��������
				if ("modify_enter_info".equals(mode)){
					arry = (ArrayList)purchaseDAO.getEnteredFile_list("pu_entered_info",table.getMid());
					request.setAttribute("ITEM_FILE",arry);
				}

				//���õ� �԰��ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
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
			// �����԰�ǰ�� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if(mode.equals("enter_item") || mode.equals("enter_item_add") || mode.equals("modify_enter")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//item_code != null ��, ������������ȭ�鿡�� ǰ���� �� �����԰� ȭ������ �б����� ��
					//item_code �� �Ѿ�� ǰ����� ���� db�� �����Ѵ�.
					//item_code == "��û��ȣ|���ֹ�ȣ|ǰ���ȣ,��û��ȣ|���ֹ�ȣ|ǰ���ȣ,...." ����.
					if(mode.equals("enter_item") && item_code != null){
						//�԰��ȣ
						enter_no = purchaseDAO.getEnterNo();

						//��Ͻð�
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String enter_date = vans.format(now);

						//���õ� �԰�ǰ���� �����Ѵ�.
						purchaseBO.saveEnterItem(mode,item_code,enter_no);

						//���� �� �����Ѱ����� ������Ʈ
						String enter_total_mount = purchaseDAO.calculateEnterTotalMount(enter_no);

						//���޾�ü�� ��������
						com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
						String supplyer_name = crmDAO.getCompanyNameByNo(supplyer_code);
						purchaseDAO.saveEnterInfo(enter_no,"S18",enter_date,enter_total_mount,"KRW","PI",supplyer_code,supplyer_name,enter_no.substring(2,4),enter_no.substring(4,6),enter_no.substring(7,10),"","",login_id,"");
					}
					//���� �԰�ǿ� �԰�ǰ�� �߰�
					//���� �԰���� ���°� S18(�԰���)�� �ƴϰų� 
					else if(mode.equals("enter_item_add") && item_code != null && enter_no != null){
						String porcess_stat = purchaseDAO.getMaxStatForEnterItem(enter_no);
						if(porcess_stat.equals("S18")){
							purchaseBO.saveEnterItem(mode,item_code,enter_no);
						}else{
							PrintWriter out = response.getWriter();
							out.println("<script>");
							out.println("alert('�԰��ȣ:" + enter_no + "��(��) ���� �԰��ϻ��°� �ƴմϴ�. ǰ���� �߰��� �� �����ϴ�.');");
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

				//���õ� �԰��ȣ �� ǰ���ڵ忡 ���� ������ �����´�.
				table = purchaseBO.getEnterItemForm(mode,enter_no,item_code,order_no,login_id);
				request.setAttribute("ENTER_INFO",table);

				//���õ� �԰��ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
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
			//  �����԰� ���ڰ��� ȭ��
			///////////////////////////////////////////////////
			else if (mode.equals("enter_app_view") || mode.equals("enter_app_print")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
				ArrayList arry = new ArrayList();

				//���õ� �԰��ȣ �� ǰ���ڵ忡 ���� ������ �����´�.
				table = purchaseDAO.getEnterInfo(enter_no);
				request.setAttribute("ENTER_INFO",table);
				
				//���õ� �԰��ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
				ArrayList item_list = new ArrayList();
				item_list = purchaseDAO.getEnterItemList(enter_no);
				request.setAttribute("ITEM_LIST",item_list);

				if(mode.equals("enter_app_print")) {
					//���� ������ȣ�� ������ ��, ������ȣ�� �ش��ϴ� ���� ������ �����´�.
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
			// �����԰�ǰ�� ��� �� ���� ��
			//
			// �����԰��Ͻ��� �ֿ� �帧
			// ===========================================================================
			// 1.�����԰�ǰ������ pu_entered_item ���̺� �����Ѵ�.
			// 2.pu_requested_item ���̺� �ش� ǰ���� �����ڵ�� ���ּ����� ������Ʈ
			// 3.pu_order_item ���̺� �ش� ǰ���� �����ڵ�� ���ּ����� ������Ʈ
			// 4.������ ����Ͽ� st_inout_master ���̺� ���������� ����Ѵ�.
			// 5.��������� �԰������� ������Ʈ
			// 6.ǰ��������� ������Ʈ
			// 7.ǰ���˻� �Ƿ������� ����Ѵ�.
			// ===========================================================================
			///////////////////////////////////////////////////
			else if(mode.equals("enter_item") || mode.equals("enter_item_add") || mode.equals("modify_enter")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//item_code != null ��, ������������ȭ�鿡�� ǰ���� �� �����԰� ȭ������ �б����� ��
					//item_code �� �Ѿ�� ǰ����� ���� db�� �����Ѵ�.
					//item_code == "��û��ȣ|���ֹ�ȣ|ǰ���ȣ,��û��ȣ|���ֹ�ȣ|ǰ���ȣ,...." ����.
					if(mode.equals("enter_item") && item_code != null){
						//�԰��ȣ
						enter_no = purchaseDAO.getEnterNo();

						//��Ͻð�
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String enter_date = vans.format(now);

						//���õ� �԰�ǰ���� �����Ѵ�.
						purchaseBO.saveEnterItem(mode,item_code,enter_no);

						//���� �� �����Ѱ����� ������Ʈ
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

				//���õ� �԰��ȣ �� ǰ���ڵ忡 ���� ������ �����´�.
				table = purchaseBO.getEnterItemForm(mode,enter_no,item_code,order_no,login_id);
				request.setAttribute("ENTER_INFO",table);

				//���õ� �԰��ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
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
			// �����԰����� ��ȸ ����Ʈ
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
			// �����԰����� ���� ����Ʈ
			///////////////////////////////////////////////////
			else if(mode.equals("entered_list")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				ArrayList enter_list = new ArrayList();

				//���� ����� ���ó�¥�� �԰�� ǰ���� �˻��Ѵ�.
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
				if(searchword.equals("")) searchword = "enter_date|" + vans.format(now) + vans.format(now);

				enter_list = purchaseDAO.getEnterItemList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ENTER_LIST",enter_list);

				//�� �԰�ݾ� ���
				String total_cost = purchaseDAO.getTotalEnterCost(mode,searchword,searchscope,login_id);
				request.setAttribute("TOTAL_COST",total_cost);

				getServletContext().getRequestDispatcher("/pu/enter/entered_list.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ���õ� �԰�ǰ�� �����ϱ�
			///////////////////////////////////////////////////
			else if(mode.equals("delete_enter")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//�԰���� �ľ��ϱ�
					table = purchaseDAO.getEnterItemInfo(enter_no,item_code);
					String enter_quantity = table.getEnterQuantity();

					//�԰�ǰ�� �����ϱ�
					purchaseDAO.deleteEnterItem(enter_no,item_code);

					table = purchaseDAO.getEnterInfo(enter_no);

					String enter_total_mount = purchaseDAO.calculateEnterTotalMount(enter_no);
					purchaseDAO.updateEnterInfo(enter_no,table.getEnterType(),table.getEnterDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),enter_total_mount,table.getMonetaryUnit(),table.getSupplyerCode(),table.getSupplyerName());

					//pu_requested_item ���̺��� �����ڵ� �� �԰���� ������Ʈ
					purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S13");
					purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"delivery_quantity","-"+enter_quantity);

					//pu_order_item ���̺��� �����ڵ� �� �԰���� ������Ʈ
					purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,item_code,"S13");
					purchaseDAO.updateQuantity("pu_order_item","order_no",order_no,item_code,"delivery_quantity","-"+enter_quantity);

					//������Ȳ ����� �����Ѵ�.
//					com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
//					stBO.deleteInOutInfoForPurchasedInputItem(enter_no,request_no,item_code);

					//ǰ���˻��Ƿ� ������ �����Ѵ�.
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
			// ���õ� �԰�� �ϰ� �����ϱ�
			///////////////////////////////////////////////////
			else if(mode.equals("delete_enter_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					purchaseBO.deleteAllEnterItems(enter_no);
					purchaseDAO.deleteEnterInfo(enter_no);
					con.commit(); // commit�Ѵ�.
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
			// ���ּ� ���
			///////////////////////////////////////////////////
			else if(mode.equals("order_print") || mode.equals("order_print2")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//���õ� ���ֹ�ȣ �� ǰ���ڵ忡 ���� ������ �����´�.
				table = purchaseDAO.getOrderInfo(order_no);
				request.setAttribute("ORDER_INFO",table);

				//���õ� ���ֹ�ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
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
				
				//boardpath ���� ���ϱ��� ��� ����
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
				
				//boardpath ���� ���ϱ��� ��� ����
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
	 * post������� �Ѿ���� �� ó��
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";
		String upload_folder = request.getParameter("upload_folder");
		if(upload_folder == null) upload_folder = "";
		
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/pu/" + upload_folder;

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//������������ �� �޾ƿ´�. multi���� ������
		String mode				= multi.getParameter("mode");
		String page				= multi.getParameter("page");
		String searchword		= multi.getParameter("searchword");
		String searchscope		= multi.getParameter("searchscope");
		String category			= multi.getParameter("category");
		String redirectUrl		= "";

		//�������� �Ѿ���ų� null�� �� ���鿡 ���� ó��.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

		//���� �������� ����� ���̵� ��������
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

		//���ſ�û�Ƿ� ��
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
		String supply_cost		= multi.getParameter("supply_cost");			// ��������ܰ�
		String request_cost		= multi.getParameter("request_cost");			// ���ſ�û�ݾ�
		String request_total_mount = multi.getParameter("request_total_mount");	// ���ſ�û�ѱݾ�
		String project_code		= multi.getParameter("project_code");			// ������Ʈ�ڵ�
		String project_name		= multi.getParameter("project_name");			// ������Ʈ��

		//���ֵ�� ��
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

		//�԰��� ��
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
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
						
			////////////////////////
			// ���ſ�û���ó��
			////////////////////////
			if (mode.equals("request_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);

				//request_total_mount = Double.toString(Math.round(Double.parseDouble(request_total_mount)));
				//��û��ȣ ����
				request_no = purchaseDAO.getRequestNo();
				purchaseDAO.saveRequestInfo(request_no,request_date,requester_div_code,requester_div_name,requester_id,requester_info,request_type,request_total_mount,project_code,project_name,request_no.substring(3,5),request_no.substring(5,7),request_no.substring(8,11),factory_code,factory_name);
				
				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no;
			}  

			//////////////////////////
			// ���ſ�ûǰ����ó��
			//////////////////////////
			else if(mode.equals("request_item_add")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.business.PurchaseMgrBO   purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.entity.RequestInfoTable file = new com.anbtech.pu.entity.RequestInfoTable();
				
				//�ߺ�üũ
				//���� ��û��ȣ�� ���� ǰ���� ���ſ�û�� �� ������ �Ѵ�.
				int same_item_cnt = purchaseDAO.getSameRequestItemCount(request_no,item_code);
				if(same_item_cnt > 0){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('ǰ�� " + item_code + "��(��) �̹� ���ſ�ûǰ�� ��ϵǾ� �ֽ��ϴ�. �ߺ� ����� �� �����ϴ�.');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}else{
					purchaseDAO.saveRequestItemInfo(request_no,item_code,item_name,item_desc,request_unit,delivery_date,request_quantity,"0","0","S01",supplyer_code,supplyer_name,supply_cost,request_cost);

					//request_no �� item_code �� �ش��ϴ� ������ȣ�� �����´�.
					String mid = purchaseDAO.getMid("pu_requested_item",request_no,item_code);
				
					//÷������ ���δ�
					file = (com.anbtech.pu.entity.RequestInfoTable)purchaseBO.getFile_frommulti(multi, mid, filepath);
				
					//���ε� �� ÷������ ������ DB�� �����ϱ�
					purchaseBO.updFile("pu_requested_item",mid, file.getFname(), file.getFtype(), file.getFsize(),file.getFumask());

					// ���ֿ����Ѿ� ��� �� update
					request_total_mount = purchaseDAO.calculateRequestTotalMount(request_no);
					request_total_mount = Double.toString(Math.round(Double.parseDouble(request_total_mount)));

					purchaseDAO.updateRequestTotalMount(request_no,request_total_mount);
				}

				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no+"&request_type="+request_type;
			}

			//////////////////////////
			// ���ſ�ûǰ�����ó��
			//////////////////////////
			else if(mode.equals("modify_request")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.business.PurchaseMgrBO   purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.entity.RequestInfoTable file = new com.anbtech.pu.entity.RequestInfoTable();
				purchaseDAO.updateRequestItemInfo(request_no,item_code,item_name,item_desc,request_unit,delivery_date,request_quantity,supplyer_code,supplyer_name,supply_cost,request_cost);
				
				//request_no �� item_code �� �ش��ϴ� ������ȣ�� �����´�.
				String mid = purchaseDAO.getMid("pu_requested_item",request_no,item_code);
				//multi���� ���������� �����ͼ� ó���Ѵ�.
				ArrayList file_list = purchaseDAO.getFile_list("pu_requested_item",mid);
				//���� ���δ�
				file = (com.anbtech.pu.entity.RequestInfoTable)purchaseBO.getFile_frommulti(multi, mid, filepath, file_list);
					
				// ȭ������ SAVE
				// ���ε� �� ÷������ ������ DB�� �����ϱ�
				purchaseBO.updFile("pu_requested_item",mid, file.getFname(), file.getFtype(), file.getFsize(),file.getFumask());

				// ���ֿ����Ѿ� ��� �� update
				request_total_mount = purchaseDAO.calculateRequestTotalMount(request_no);
				request_total_mount = Double.toString(Math.round(Double.parseDouble(request_total_mount)));
				purchaseDAO.updateRequestTotalMount(request_no,request_total_mount);

				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no;
			}

			//////////////////////////
			// ���ſ�û��������ó��
			//////////////////////////
			else if(mode.equals("modify_request_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				purchaseDAO.updateRequestInfo(request_no,request_date,requester_div_code,requester_div_name,requester_id,requester_info);

				redirectUrl = "PurchaseMgrServlet?mode=request_item_add&request_no="+request_no+"&request_type="+request_type;
			}

			////////////////////////
			// �������� ���ó��
			////////////////////////
			if(mode.equals("order_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();
				
				//���ֹ�ȣ ���
				order_no = purchaseDAO.getOrderNo(inout_type);
				purchaseDAO.saveOrderInfo(order_no,order_type,supplyer_code,supplyer_name,order_date,monetary_unit,exchange_rate,order_total_mount, vat_rate,vat_mount,supplyer_info,supplyer_tel,requester_div_code,requester_div_name,requester_id,requester_info,vat_type,is_vat_contained,approval_type,approval_period,payment_type,other_info,inout_type,order_no.substring(2,4),order_no.substring(4,6),order_no.substring(7,10));
					
				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}

			////////////////////////////////////////////
			// ���޾�ü ������ �������� �� ǰ�� ���޴ܰ� ������ ������Ʈ
			////////////////////////////////////////////
			else if(mode.equals("reflect_supply_info")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//ǰ�� ���ִܰ� �� ���ֱݾ� ������Ʈ
				purchaseBO.updateOrderItemUnitCost(mode,order_no,supplyer_code);

				//���޾�ü���� �� �����Ѱ����� ������Ʈ
				table = purchaseDAO.getOrderInfo(order_no);
				order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
				purchaseDAO.updateOrderInfo(order_no,table.getOrderType(),table.getOrderDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),supplyer_code,supplyer_name,order_total_mount,table.getMonetaryUnit(),table.getExchangeRate(),table.getVatType(),table.getVatRate(),table.getVatMount(),table.getIsVatContained(),table.getApprovalType(),table.getApprovalPeriod(),table.getPaymentType(),table.getSupplyerInfo(),table.getSupplyerTel(),table.getOtherInfo());

				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}
			
			//////////////////////////
			// ���ֵ��ó��(ǰ���߰�)
			//////////////////////////
			else if(mode.equals("order_item_add")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();
				purchaseDAO.saveOrderItemInfo(order_no,"��޹���",item_code,item_name,item_desc,order_quantity,order_unit,delivery_date,unit_cost,order_cost,"0","S05");

				//���� �� �����Ѱ����� ������Ʈ
				table = purchaseDAO.getOrderInfo(order_no);
				
				order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
				vat_rate = table.getVatRate();
				vat_mount = Double.toString(Math.round(Double.parseDouble(order_total_mount) * Double.parseDouble(vat_rate) / 100));
				purchaseDAO.updateOrderInfo(order_no,table.getOrderType(),table.getOrderDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),table.getSupplyerCode(),table.getSupplyerName(),order_total_mount,table.getMonetaryUnit(),table.getExchangeRate(),table.getVatType(),table.getVatRate(),vat_mount,table.getIsVatContained(),table.getApprovalType(),table.getApprovalPeriod(),table.getPaymentType(),table.getSupplyerInfo(),table.getSupplyerTel(),table.getOtherInfo());

				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}

			//////////////////////////
			// ����ǰ�����ó��
			//////////////////////////
			else if(mode.equals("modify_order")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.OrderInfoTable table = new com.anbtech.pu.entity.OrderInfoTable();

				//ǰ����������
				purchaseDAO.updateOrderItemInfo(order_no,item_code,item_name,item_desc,order_quantity,order_unit,unit_cost,"y",order_cost,delivery_date,"0",request_no);

				//���� �� �����Ѱ����� ������Ʈ
				table = purchaseDAO.getOrderInfo(order_no);
				
				order_total_mount = purchaseDAO.calculateOrderTotalMount(order_no);
				vat_rate = table.getVatRate();
				vat_mount = Double.toString(Math.round(Double.parseDouble(order_total_mount) * Double.parseDouble(vat_rate) / 100));
				purchaseDAO.updateOrderInfo(order_no,table.getOrderType(),table.getOrderDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),table.getSupplyerCode(),table.getSupplyerName(),order_total_mount,table.getMonetaryUnit(),table.getExchangeRate(),table.getVatType(),table.getVatRate(),vat_mount,table.getIsVatContained(),table.getApprovalType(),table.getApprovalPeriod(),table.getPaymentType(),table.getSupplyerInfo(),table.getSupplyerTel(),table.getOtherInfo());

				//pu_requested_item ���̺� ǰ���� ���ּ��� ������Ʈ
				int distance = Integer.parseInt(order_quantity) - Integer.parseInt(old_quantity);
				purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"order_quantity",Integer.toString(distance));

				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}

			//////////////////////////
			// ������������ó��
			//////////////////////////
			else if(mode.equals("modify_order_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				purchaseDAO.updateOrderInfo(order_no,order_type,order_date,requester_div_code,requester_div_name,requester_id,requester_info,supplyer_code,supplyer_name,order_total_mount,monetary_unit,exchange_rate,vat_type,vat_rate,vat_mount,is_vat_contained,approval_type,approval_period,payment_type,supplyer_info,supplyer_tel,other_info);

				redirectUrl = "PurchaseMgrServlet?mode=order_item_add&order_no=" + order_no;
			}

			////////////////////////////////////////////////////
			// ����ǰ�� �޸� �Է�ó��
			///////////////////////////////////////////////////
			else if(mode.equals("write_memo")){
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//item_code == "��û��ȣ|���ֹ�ȣ|ǰ���ȣ,��û��ȣ|���ֹ�ȣ|ǰ���ȣ,...." ����.
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
			// �԰���������ó��
			//////////////////////////
			else if(mode.equals("modify_enter_info")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.business.PurchaseMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseMgrBO(con);
				com.anbtech.pu.entity.EnterInfoTable file = new com.anbtech.pu.entity.EnterInfoTable();

				//request_no �� item_code �� �ش��ϴ� ������ȣ�� �����´�.
				String mid = purchaseDAO.getEnterMid("pu_entered_info",enter_no);
				//multi���� ���������� �����ͼ� ó���Ѵ�.
				ArrayList file_list = purchaseDAO.getEnteredFile_list("pu_entered_info",mid);
				//���� ���δ�
				file = (com.anbtech.pu.entity.EnterInfoTable)purchaseBO.getEnterFile_frommulti(multi, mid, filepath, file_list);
					
				// ȭ������ SAVE
				// ���ε� �� ÷������ ������ DB�� �����ϱ�
				purchaseBO.updFile("pu_entered_info",mid, file.getFname(), file.getFtype(), file.getFsize(),file.getFumask());

				purchaseDAO.updateEnterInfo(enter_no,enter_type,enter_date,requester_div_code,requester_div_name,requester_id,requester_info,enter_total_mount,monetary_unit,supplyer_code,supplyer_name);

				redirectUrl = "PurchaseMgrServlet?mode=enter_item_add&enter_no=" + enter_no;
			}

			//////////////////////////
			// �԰�ǰ�����ó��
			//////////////////////////
			else if(mode.equals("modify_enter")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.pu.entity.RequestInfoTable table2 = new com.anbtech.pu.entity.RequestInfoTable();
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
				
					//�԰�ǰ������ ����
					purchaseDAO.updateEnterItemInfo(enter_no,item_code,item_name,item_desc,enter_quantity,enter_unit,unit_cost,enter_cost,factory_code,factory_name,warehouse_code,warehouse_name,request_no,order_no);

					//�԰��������� �԰�ݾ��հ踦 ����
					table = purchaseDAO.getEnterInfo(enter_no);
					enter_total_mount = purchaseDAO.calculateEnterTotalMount(enter_no);
					purchaseDAO.updateEnterInfo(enter_no,table.getEnterType(),table.getEnterDate(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),enter_total_mount,table.getMonetaryUnit(),table.getSupplyerCode(),table.getSupplyerName());

					int distance = Integer.parseInt(enter_quantity) - Integer.parseInt(old_quantity);

					//pu_requested_item ���̺� ǰ���� �԰���� ������Ʈ
					purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"delivery_quantity",Integer.toString(distance));

					//pu_order_item ���̺� ǰ���� �԰���� ������Ʈ
					purchaseDAO.updateQuantity("pu_order_item","order_no",order_no,item_code,"delivery_quantity",Integer.toString(distance));

					//////////////////////////////////////////////////////////////
					/////// ���⼭���� ������ �� ǰ���������� �����Ǵ� �κ��� ///////////
					//////////////////////////////////////////////////////////////
					table2 = purchaseDAO.getRequestInfo(request_no);

					//��û���п� ���� ������ ������ ������ ����Ѵ�. MRP,ROP�� ���� �԰� �������� ������
					//��ģ��.
					if(table2.getRequestType().equals("MRP") || table2.getRequestType().equals("ROP")){
						//������Ȳ���� �� ���DB�� ������Ʈ�Ѵ�.
						stBO.updateInOutInfoForPurchasedInputItem("MOD",item_code,unit_cost,enter_quantity,enter_no,factory_code,factory_name,warehouse_code,warehouse_name);

						//ǰ���˻��Ƿ� ������ ������Ʈ�Ѵ�.
						//�ش� �԰��ȣ �� ǰ���ڵ忡 �ش��ϴ� ǰ���˻��Ƿڰ��� ������ �����Ѵ�.
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
			//con�Ҹ�
			close(con);
		}
	} //doPost()
}
