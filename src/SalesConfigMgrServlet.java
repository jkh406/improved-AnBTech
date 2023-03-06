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
			// �������� �����Ȳ ����Ʈ ���
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
			// �������� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if (mode.equals("write_booking_type") || mode.equals("modify_booking_type")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.BookingTypeTable table = new com.anbtech.bs.entity.BookingTypeTable();
								
				table = scBO.getBookingTypeForm(mode,mid);
				request.setAttribute("SALES_CONF",table);
				getServletContext().getRequestDispatcher("/bs/config/write_booking_type.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// �������� ����ó��
			///////////////////////////////////////////////////
			else if (mode.equals("delete_booking_type"))	{
				
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.deleteBookingTypeInfo(mid);
				redirectUrl = "SalesConfigMgrServlet?mode=list_booking_type";
			}

			///////////////////////////////////////////////////
			// ǰ�� �ܰ����� ����Ʈ ���
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
			// ǰ�� �ܰ����� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if (mode.equals("write_item_unit_cost") || mode.equals("modify_item_unit_cost")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();
							
				table = scBO.getItemUnitCostForm(mode,mid);
				request.setAttribute("UNIT_COST",table);
				getServletContext().getRequestDispatcher("/bs/config/write_item_unit_cost.jsp?mode="+mode).forward(request,response);
			}
			////////////////////////////////////////////////////
			// ǰ�� �ܰ����� ���� ó��
			///////////////////////////////////////////////////
			else if (mode.equals("delete_item_unit_cost"))	{
				
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.deleteItemUnitCostInfo(mid);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost";
			}


			///////////////////////////////////////////////////
			// ���� ǰ�� �ܰ����� ����Ʈ ���
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
			// ���� ǰ�� �ܰ����� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if (mode.equals("write_item_unit_cost_by_customer") || mode.equals("modify_item_unit_cost_by_customer")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();
							
				table = scBO.getItemUnitCostByCustomerForm(mode,mid);
				
				request.setAttribute("COST_CUSTOMER",table);
				getServletContext().getRequestDispatcher("/bs/config/write_item_unit_cost_by_customer.jsp?mode="+mode).forward(request,response);
			}
			////////////////////////////////////////////////////
			// ���� ǰ�� �ܰ����� ���� ó��
			///////////////////////////////////////////////////
			else if (mode.equals("delete_item_unit_cost_by_customer"))	{
				
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.deleteItemUnitCostByCustomerInfo(mid);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost_by_customer";
			}

			///////////////////////////////////////////////////
			// ǰ�� �������� ����Ʈ ���
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
			// ǰ�� �������� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if (mode.equals("write_item_premium") || mode.equals("modify_item_premium")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();
							
				table = scBO.getItemPremiumForm(mode,mid);
				request.setAttribute("ITEM_PREMIUM",table);
				getServletContext().getRequestDispatcher("/bs/config/write_item_premium.jsp?mode="+mode).forward(request,response);
			}
			////////////////////////////////////////////////////
			// ǰ�� �������� ���� ó��
			///////////////////////////////////////////////////
			else if (mode.equals("delete_item_premium"))	{
				
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.deleteItemPremiumInfo(mid);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium";
			}

			///////////////////////////////////////////////////
			// ���� ǰ�� �������� ����Ʈ ���
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
			// ���� ǰ�� �������� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if (mode.equals("write_item_premium_by_customer") || mode.equals("modify_item_premium_by_customer")){
				
				com.anbtech.bs.business.SalesConfigMgrBO scBO = new com.anbtech.bs.business.SalesConfigMgrBO(con);
				com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();
							
				table = scBO.getItemPremiumByCustomerForm(mode,mid);
				request.setAttribute("PREMIUM_CUSTOMER",table);
				getServletContext().getRequestDispatcher("/bs/config/write_item_premium_by_customer.jsp?mode="+mode).forward(request,response);
			}
			////////////////////////////////////////////////////
			// ���� ǰ�� �������� ���� ó��
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
	 * post������� �Ѿ���� �� ó��
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/bs/";

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
		
		// ������ �Ķ����
        // �������� ���� �Ķ����
		String  mid				= multi.getParameter("mid");			// ��������
		String  order_code		= multi.getParameter("order_code");		// ���������ڵ� 
		String  order_name		= multi.getParameter("order_name");		// �������¸� 
		String	is_export		= multi.getParameter("is_export");		// ���⿩��    
		String	is_return		= multi.getParameter("is_return");		// ��ǰ����
		String	is_entry		= multi.getParameter("is_entry");		// �������     
		String	is_shipping		= multi.getParameter("is_shipping");	// ���Ͽ���
		String	is_auto_ship	= multi.getParameter("is_auto_ship");	// �ڵ����ϻ�������
		String	is_sale			= multi.getParameter("is_sale");		// ���⿩��      
		String	shipping_type	= multi.getParameter("shipping_type");	// ��������
		String	is_use			= multi.getParameter("is_use");			// ��뿩��

		// ǰ���ڵ庰 �ܰ����� �Ķ����
		String  item_code		= multi.getParameter("item_code");		// ǰ���ڵ�    
		String  item_name		= multi.getParameter("item_name");		// ǰ���
		String  sale_type		= multi.getParameter("sale_type");		// �Ǹ������ڵ�, �Ϲ�/����/Ư�� ��
		String  approval_type	= multi.getParameter("approval_type");  // ���������ڵ�, ����/��ǥ/���� ��
		String  apply_date		= multi.getParameter("apply_date");		// ��������, ǰ��ܰ��� ����� ����
		String  sale_unit		= multi.getParameter("sale_unit");		// �ǸŴ���
		String  money_type		= multi.getParameter("money_type");		// ȭ������
		String  sale_unit_cost	= multi.getParameter("sale_unit_cost"); // �ǸŴܰ�
		String  customer_code	= multi.getParameter("customer_code");  // �ŷ�ó�ڵ�
		String  customer_name	= multi.getParameter("customer_name");  // �ŷ�ó��

		// ǰ���ڵ庰 ��������
		String premium_type				= multi.getParameter("premium_type");		// ��������          
		String premium_name				= multi.getParameter("premium_name");		// ����������          
		String premium_standard_quantity= multi.getParameter("premium_standard_quantity");		// ����������ؼ���
		String premium_value			= multi.getParameter("premium_value");		// ������

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			/////////////////////////
			//  ������������ �űԵ�� 
			////////////////////////
			if(mode.equals("write_booking_type")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveBookingTypeInfo(order_code,order_name,is_export,is_return,is_entry,is_shipping,is_auto_ship,is_sale,shipping_type,is_use);
				redirectUrl = "SalesConfigMgrServlet?mode=list_booking_type";
			}
			
			/////////////////////////
			//  ������������ ���� 
			////////////////////////
			else if(mode.equals("modify_booking_type")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.modifyBookingTypeInfo(mid,order_code,order_name,is_export,is_return,is_entry,is_shipping,is_auto_ship,is_sale,shipping_type,is_use);
				redirectUrl = "SalesConfigMgrServlet?mode=list_booking_type";
			}			

			//////////////////////////////
			//  ǰ�� �ܰ����� �űԵ�� 
			//////////////////////////////
			if(mode.equals("write_item_unit_cost")){

				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveItemUnitCostInfo(item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost";
			}
			
			///////////////////////////
			//  ǰ�� �ܰ����� ���� 
			///////////////////////////
			else if(mode.equals("modify_item_unit_cost")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.modifyItemUnitCostInfo(mid,item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost";
			}
			
			//////////////////////////////
			//  ���� ǰ�� �ܰ����� �űԵ�� 
			//////////////////////////////
			if(mode.equals("write_item_unit_cost_by_customer")){

				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveItemUnitCostByCustomerInfo(item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost,customer_code,customer_name);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost_by_customer";
			}
			
			///////////////////////////
			//  ���� ǰ�� �ܰ����� ����
			///////////////////////////
			else if(mode.equals("modify_item_unit_cost_by_customer")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.modifyItemUnitCostByCustomerInfo(mid,item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost,customer_code,customer_name);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_unit_cost_by_customer";
			}			

			//////////////////////////////
			//  ǰ�� �������� ���
			//////////////////////////////
			if(mode.equals("write_item_premium")){

				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveItemPremiumInfo(item_code,item_name,approval_type,apply_date,sale_unit,premium_type,premium_name,premium_standard_quantity,premium_value);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium";
			}
			
			///////////////////////////
			//  ǰ�� �������� ����
			///////////////////////////
			else if(mode.equals("modify_item_premium")){
				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.modifyItemPremiumInfo(mid,item_code,item_name,approval_type,apply_date,sale_unit,premium_type,premium_name,premium_standard_quantity,premium_value);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium";
			}			

			//////////////////////////////
			//  ���� ǰ�� �������� ���
			//////////////////////////////
			if(mode.equals("write_item_premium_by_customer")){

				com.anbtech.bs.db.SalesConfigMgrDAO scDAO = new com.anbtech.bs.db.SalesConfigMgrDAO(con);
				scDAO.saveItemPremiumByCustomerInfo(item_code,item_name,approval_type,apply_date,sale_unit,premium_type,premium_name,premium_standard_quantity,premium_value,customer_code,customer_name);
				redirectUrl = "SalesConfigMgrServlet?mode=list_item_premium_by_customer";
			}
			
			///////////////////////////
			//  ���� ǰ�� �������� ����
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
			//con�Ҹ�
			close(con);
		}
	} //doPost()
}
