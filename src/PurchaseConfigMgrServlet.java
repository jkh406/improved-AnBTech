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
			// ��������� ��� �� ���� ��
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
			// ��������� ����Ʈ
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
			// ��������� ����
			///////////////////////////////////////////////////
			else if (mode.equals("delete_inout_type"))
			{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.deleteInOutTypeInfo(mid);
				response.sendRedirect("PurchaseConfigMgrServlet?mode=list_inout_type");
			}

			////////////////////////////////////////////////////
			// �������� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if(mode.equals("write_purchase_type") || mode.equals("modify_purchase_type")){

				com.anbtech.pu.business.PurchaseConfigMgrBO puconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
				com.anbtech.pu.entity.PurchaseTypeTable purchaseTable = new com.anbtech.pu.entity.PurchaseTypeTable();
				
				purchaseTable = puconfigBO.getPurchaseTypeForm(mode,mid);
				request.setAttribute("PURCHASE_TYPE_INFO",purchaseTable);

				getServletContext().getRequestDispatcher("/pu/config/write_purchase_type.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// �������� ����Ʈ
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
			// �������� ����
			///////////////////////////////////////////////////
			else if (mode.equals("delete_purchase_type"))
			{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.deletePurchaseTypeInfo(mid);
				response.sendRedirect("PurchaseConfigMgrServlet?mode=list_purchase_type");
			}
			////////////////////////////////////////////////////
			// �������� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if(mode.equals("write_order_type") || mode.equals("modify_order_type")){

				com.anbtech.pu.business.PurchaseConfigMgrBO puconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
				com.anbtech.pu.entity.OrderTypeTable orderTable = new com.anbtech.pu.entity.OrderTypeTable();
				
				orderTable = puconfigBO.getOrderTypeForm(mode,mid);
				request.setAttribute("ORDER_TYPE_INFO",orderTable);

				getServletContext().getRequestDispatcher("/pu/config/write_order_type.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// �������� ����Ʈ
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
			// �������� ����
			///////////////////////////////////////////////////
			else if (mode.equals("delete_order_type"))
			{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.deleteOrderTypeInfo(mid);
				response.sendRedirect("PurchaseConfigMgrServlet?mode=list_order_type");
			}

			///////////////////////////////////////////////////
			// ǰ�� �ܰ� ����Ʈ
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
			// ǰ�� �ܰ� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if(mode.equals("write_unit_cost") || mode.equals("modify_unit_cost")){
				com.anbtech.pu.business.PurchaseConfigMgrBO puconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
				com.anbtech.st.entity.StockInfoTable table = new com.anbtech.st.entity.StockInfoTable();
				
				table = puconfigBO.getItemUnitCostAddForm(mode,item_code);
				request.setAttribute("ITEM_UNIT_COST",table);

				getServletContext().getRequestDispatcher("/pu/config/write_item_cost.jsp?mode="+mode).forward(request,response);
			}

			///////////////////////////////////////////////////
			// ǰ�� ����ó �� ���ް� ���� ��� �� ���� ��
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
			// ǰ�� ����ó �� ���޴ܰ� ����Ʈ
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
			//  ǰ�� ����ó ���� ���� ����
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
	 * post������� �Ѿ���� �� ó��
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/pu/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//������������ �� �޾ƿ´�. multi���� ������
		String mode = multi.getParameter("mode");
		String page = multi.getParameter("page");
		String searchword = multi.getParameter("searchword");
		String searchscope = multi.getParameter("searchscope");
		String category = multi.getParameter("category");

		//�������� �Ѿ���ų� null�� �� ���鿡 ���� ó��.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";
			
		String redirectUrl = "";
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
		
		// ����
		String mid			= multi.getParameter("mid");
		String type			= multi.getParameter("type");			// ����
		String name			= multi.getParameter("name");			// ���¸�
		String is_enter		= multi.getParameter("is_enter");		// �԰���("y" or "n")
		String is_import	= multi.getParameter("is_import");		// ���Կ���("y" or "n")
		String is_return	= multi.getParameter("is_return");		// ��ǰ����("y" or "n")
		String is_sageup	= multi.getParameter("is_sageup");		// ��޿���("y" or "n")
		String is_using		= multi.getParameter("is_using");		// ��뿩��("y" or "n")
				
		// ���������������ú��� (����: ������ȣ,���Կ���,�԰���,��ǰ����,��뿩��)
		String	is_shipping		= multi.getParameter("is_shipping");	// ��������(y or n)
		String	is_pass			= multi.getParameter("is_pass");		// �������(y or n)
		String	is_purchase		= multi.getParameter("is_purchase");	// ���Կ���(y or n)
		String	enter_code		= multi.getParameter("enter_code");		// �԰����� �ڵ�(����)
		String	enter_name		= multi.getParameter("enter_name");		// �԰����� ��
		String	outgo_code		= multi.getParameter("outgo_code");		// ������� �ڵ�(����)
		String	outgo_name		= multi.getParameter("outgo_name");		// ������� ��
		String	purchase_code	= multi.getParameter("purchase_code");	// �������� �ڵ�(����)
		String	purchase_name	= multi.getParameter("purchase_name");	// �������� ��

		// ǰ�� ����ó ���� ���� ����
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
				
		// ��/������°������� ���� (����:������ȣ,�԰���,��ǰ����,��޿���,���Կ���,��뿩��)		
		String stock_type	= multi.getParameter("stock_type");		// �������

		// �������°������� ���� (����: ������ȣ,���������ڵ�,�������¸�,���Կ���,��ǰ����,��뿩��)
		String is_except	= multi.getParameter("is_except");		// ���ܿ���
		String account_type	= multi.getParameter("account_type");	// ȸ��ó������

		//ǰ��ܰ���ϰ��� �Ķ���͵�
		String item_type			= multi.getParameter("item_type");
		String item_name			= multi.getParameter("item_name");
		String unit_cost_a			= multi.getParameter("unit_cost_a");
		String unit_cost_s			= multi.getParameter("unit_cost_s");
		String unit_cost_c			= multi.getParameter("unit_cost_c");
		String last_updated_date	= multi.getParameter("last_updated_date");
	
		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////
			//	��/���������������
			////////////////////////
			if(mode.equals("write_inout_type")){
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.saveInOutTypeInfo(type,name,is_import,is_enter,is_return,is_sageup,is_using,stock_type);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_inout_type";
			
			///////////////////////////
			//	��/��� ���� ���� ����
			//////////////////////////
			} else if (mode.equals("modify_inout_type"))	{
				
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.updateInOutTypeInfo(mid,type,name,is_import,is_enter,is_return,is_sageup,is_using,stock_type);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_inout_type";

			///////////////////////////
			//	����������������
			//////////////////////////
			} else if (mode.equals("write_order_type"))		{
			
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con); 
				puconfigDAO.saveOrderTypeInfo(type,name,is_import,is_shipping,is_pass,is_enter,is_purchase,is_return,is_sageup,is_using,enter_code,enter_name,outgo_code,outgo_name,purchase_code,purchase_name);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_order_type";
			
			///////////////////////////
			//	������������ ����
			//////////////////////////
			} else if (mode.equals("modify_order_type"))		{
			
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con); 
				puconfigDAO.updateOrderTypeInfo(mid,type,name,is_import,is_shipping,is_pass,is_enter,is_purchase,is_return,is_sageup,is_using,enter_code,enter_name,outgo_code,outgo_name,purchase_code,purchase_name);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_order_type";
			
			///////////////////////////
			//	����������������
			//////////////////////////
			} else if (mode.equals("write_purchase_type") )	{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.savePurchaseTypeInfo(type,name,is_import,is_return,is_using,is_except, account_type);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_purchase_type";
			
			///////////////////////////
			//	������������Update
			//////////////////////////
			} else if (mode.equals("modify_purchase_type"))	{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				puconfigDAO.updatePurchaseTypeInfo(mid,type,name,is_import,is_return,is_using,is_except, account_type);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_purchase_type";
			
			///////////////////////////
			//	ǰ�� ����ó ���� ����
			//////////////////////////
			} else if (mode.equals("write_item_supply_info"))	{
						
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				boolean bool = puconfigDAO.isMainSupplyer(item_code,"");
				if (!bool)	is_main_supplyer = "n";
				puconfigDAO.saveItemSupplyInfo(item_code,supplyer_code,order_weight,lead_time,is_trade_now,is_main_supplyer,min_order_quantity,max_order_quantity,order_unit,supplyer_item_code,supplyer_item_name,supplyer_item_desc,maker_name,supply_unit_cost,request_unit_cost);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_item_supply_info";
			
			/////////////////////////////
			//	ǰ�� ����ó ���� Update
			////////////////////////////
			} else if (mode.equals("modify_item_supply_info"))	{

				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
				
				boolean bool = puconfigDAO.isMainSupplyer(item_code,mid);
				if (!bool)	is_main_supplyer = "n";				
				puconfigDAO.updateItemSupplyInfo(mid,item_code,supplyer_code,order_weight,lead_time,is_trade_now,is_main_supplyer,min_order_quantity,max_order_quantity,order_unit,supplyer_item_code,supplyer_item_name,supplyer_item_desc,maker_name,supply_unit_cost,request_unit_cost);
				redirectUrl = "PurchaseConfigMgrServlet?mode=list_item_supply_info";
			}

			///////////////////////////
			//	ǰ��ܰ��������
			//////////////////////////
			else if (mode.equals("write_unit_cost"))		{
				com.anbtech.pu.db.PurchaseConfigMgrDAO puconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con); 
				puconfigDAO.saveUnitCostInfo(item_type,item_code,item_name,item_desc,unit_cost_a,unit_cost_s,unit_cost_c,last_updated_date);

				redirectUrl = "PurchaseConfigMgrServlet?mode=list_item_cost";
			}
			
			///////////////////////////
			//	ǰ��ܰ���������
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
			//con�Ҹ�
			close(con);
		}
	} //doPost()
}
