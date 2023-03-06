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
			// ���� ���� ����Ʈ
			///////////////////////////////////////////////////
			if(mode.equals("list_conf_type")){
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				ArrayList arry = new ArrayList();
			
				arry = (ArrayList)stconfigDAO.getStockConfList();
				request.setAttribute("LIST_CONF",arry);
				getServletContext().getRequestDispatcher("/st/config/list_conf_type.jsp?mode="+mode).forward(request,response);
			
			////////////////////////////////////////////////////
			// ���� ���� ��� �� ���� ��
			///////////////////////////////////////////////////
			} else if (mode.equals("write_conf_type") || mode.equals("modify_conf_type")){
				
				com.anbtech.st.business.StockConfigMgrBO stconfigBO = new com.anbtech.st.business.StockConfigMgrBO(con);	
				com.anbtech.st.entity.StockConfInfoTable table = new com.anbtech.st.entity.StockConfInfoTable();
								
				table = stconfigBO.getStockConfForm(mode,mid);
				request.setAttribute("STOCK_CONF",table);
				getServletContext().getRequestDispatcher("/st/config/write_conf_type.jsp?mode="+mode).forward(request,response);
			
			////////////////////////////////////////////////////
			// �������� ����ó��
			///////////////////////////////////////////////////
			} else if (mode.equals("delete_conf_type"))	{
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.deleteStockConfInfo(mid);
				redirectUrl = "StockConfigMgrServlet?mode=list_conf_type";
			

			////////////////////////////////////////////////////
			// ���� ����Ʈ
			///////////////////////////////////////////////////
			}else if(mode.equals("list_factory")){
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				ArrayList arry = new ArrayList();
			
				arry = (ArrayList)stconfigDAO.getFactoryInfoList();
				request.setAttribute("LIST_FACTORY",arry);
				getServletContext().getRequestDispatcher("/st/config/list_factory.jsp?mode="+mode).forward(request,response);
			
			////////////////////////////////////////////////////
			// ���� ���� ��� �� ���� ��
			///////////////////////////////////////////////////
			} else if (mode.equals("write_factory_info") || mode.equals("modify_factory_info")){
				
				com.anbtech.st.business.StockConfigMgrBO stconfigBO = new com.anbtech.st.business.StockConfigMgrBO(con);	
				com.anbtech.st.entity.FactoryInfoTable table = new com.anbtech.st.entity.FactoryInfoTable();
								
				table = stconfigBO.getFactoryInfoForm(mode,mid);
				request.setAttribute("FACTORY_INFO",table);
				getServletContext().getRequestDispatcher("/st/config/write_factory_info.jsp?mode="+mode).forward(request,response);
						
			////////////////////////////////////////////////////
			// ���� ����
			///////////////////////////////////////////////////
			} else if (mode.equals("delete_factory_info")){
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);	
				com.anbtech.st.entity.FactoryInfoTable table = new com.anbtech.st.entity.FactoryInfoTable();
				stconfigDAO.deleteFactoryInfo(mid);
				redirectUrl = "StockConfigMgrServlet?mode=list_factory";
			
			////////////////////////////////////////////////////
			// â�� ����Ʈ
			///////////////////////////////////////////////////
			}else if(mode.equals("list_warehouse")){
				
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				ArrayList arry = new ArrayList();
			
				arry = (ArrayList)stconfigDAO.getWarehouseInfoList();
				request.setAttribute("LIST_WAREHOUSE",arry);
				getServletContext().getRequestDispatcher("/st/config/list_warehouse.jsp?mode="+mode).forward(request,response);
			
			////////////////////////////////////////////////////
			// ���� ���� ��� �� ���� ��
			///////////////////////////////////////////////////
			} else if (mode.equals("write_warehouse_info") || mode.equals("modify_warehouse_info")){
				
				com.anbtech.st.business.StockConfigMgrBO stconfigBO = new com.anbtech.st.business.StockConfigMgrBO(con);	
				com.anbtech.st.entity.WarehouseInfoTable table = new com.anbtech.st.entity.WarehouseInfoTable();
								
				table = stconfigBO.getWarehouseInfoForm(mode,mid);
				request.setAttribute("WAREHOUSE_INFO",table);
				getServletContext().getRequestDispatcher("/st/config/write_warehouse_info.jsp?mode="+mode).forward(request,response);
						
			////////////////////////////////////////////////////
			// ���� ����
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
	 * post������� �Ѿ���� �� ó��
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/st/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		String mid	= multi.getParameter("mid");	// ��������
	
		String trade_type_code	= multi.getParameter("trade_type_code"); // ���� ���� �ڵ� 
		String trade_type_name	= multi.getParameter("trade_type_name"); // ���� ��                      
		String stock_rise_fall	= multi.getParameter("stock_rise_fall"); // ��� ���� ���� (1:���� 2:���� 3:����)  
		String stock_type1		= multi.getParameter("stock_type1");	 // �������1                            
		String stock_type2		= multi.getParameter("stock_type2");	 // �������2                            
		String is_cost_apply	= multi.getParameter("is_cost_apply");	 // ���ܰ��ݿ�����(1:YES 2:NO)          
		String is_count_posting	= multi.getParameter("is_count_posting");	    // ȸ�� posting ����     
		String is_wharehouse_move	= multi.getParameter("is_wharehouse_move");	// â�� �̵�����        
		String is_factory_move	= multi.getParameter("is_factory_move");		// ���尣 �̵�����        
		String is_item_move		= multi.getParameter("is_item_move");			// ǰ�� �̵�����               
		String is_no_move		= multi.getParameter("is_no_move");				// ������ �̵�����    	
		
		// �����������
		String factory_code		= multi.getParameter("factory_code");		// �����ڵ�
	    String factory_name		= multi.getParameter("factory_name");		// �����
		String production_type	= multi.getParameter("production_type");		// ����Ÿ��(���ֻ���/�ڻ����)
		String main_product		= multi.getParameter("main_product");		// �� ����ǰ��
		String factory_address	= multi.getParameter("factory_address");	// ���� �ּ�
		String product_plan_term= multi.getParameter("product_plan_term");	// �����ȹ�Ⱓ(�ϴ���)
		String mps_confirm_term = multi.getParameter("mps_confirm_term");	// mpsȮ�� �Ⱓ(�ϴ���)
		String mps_plan_term	= multi.getParameter("mps_plan_term");		// mps��ȹ �Ⱓ(�ϴ���)
		String mrp_confirm_term = multi.getParameter("mrp_confirm_term");	// mrpȮ�� �Ⱓ(�ϴ���)
		String agency_code		= multi.getParameter("agency_code");				// ������ڵ�
		String agency_name		= multi.getParameter("agency_name");				// ������

		// â���������
		String warehouse_code	= multi.getParameter("warehouse_code")	;	// â�� �ڵ�
		String warehouse_name	= multi.getParameter("warehouse_name")	;	// â���
		String warehouse_type	= multi.getParameter("warehouse_type")	;	// â�� Ÿ��-�系â��/�ŷ�â��
		String warehouse_address= multi.getParameter("warehouse_address");	// â���ּ�
		//String factory_code		= multi.getParameter("factory_code")	;	// ���� �ڵ�(������ â�� �����ִ� ����)
		String group_name		= multi.getParameter("group_name")		;	// â���� �׷��
		String manager_name		= multi.getParameter("manager_name")	;	// â�� �����ڸ�
		String manager_id		= multi.getParameter("manager_id")		;	// â�� ������ id
		String using_mrp		= multi.getParameter("using_mrp")		;	// mrp ������ �ش� â���� ��� ���� ����
		String client			= multi.getParameter("using_mrp")		;	// ����� â���� �ŷ�ó

		
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

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////
			//	������������ ���
			////////////////////////
			if (mode.equals("write_conf_type")){
			
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.saveStockConfInfo(trade_type_code,trade_type_name,stock_rise_fall,stock_type1,stock_type2,is_cost_apply,is_count_posting,is_wharehouse_move,is_factory_move,is_item_move,is_no_move);
				redirectUrl = "StockConfigMgrServlet?mode=list_conf_type";

			/////////////////////////
			//	������������ Update
			////////////////////////			
			} else if (mode.equals("modify_conf_type")) {
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.modifyStockConfInfo(mid,trade_type_code,trade_type_name,stock_rise_fall,stock_type1,stock_type2,is_cost_apply,is_count_posting,is_wharehouse_move,is_factory_move,is_item_move,is_no_move);
				
				redirectUrl = "StockConfigMgrServlet?mode=list_conf_type";

			/////////////////////////
			//	�������� ���
			////////////////////////
			} else if (mode.equals("write_factory_info")){
			
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.saveFactoryInfo(factory_code,factory_name,production_type,main_product,factory_address,product_plan_term,mps_confirm_term,mps_plan_term,mrp_confirm_term,agency_code,agency_name);
				
				redirectUrl = "StockConfigMgrServlet?mode=list_factory";

			////////////////////////
			//	�������� Update
			////////////////////////			
			} else if (mode.equals("modify_factory_info")) {
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.modifyFactoryInfo(mid,factory_code,factory_name,production_type,main_product,factory_address,product_plan_term,mps_confirm_term,mps_plan_term,mrp_confirm_term,agency_code,agency_name);
				
				redirectUrl = "StockConfigMgrServlet?mode=list_factory";
						
			/////////////////////////
			//	â������ ���
			////////////////////////
			} else if (mode.equals("write_warehouse_info")){
			
				com.anbtech.st.db.StockConfigMgrDAO stconfigDAO = new com.anbtech.st.db.StockConfigMgrDAO(con);
				stconfigDAO.saveWarehouseInfo(warehouse_code,warehouse_name,warehouse_type,factory_code,factory_name,group_name,manager_name,manager_id,using_mrp,client,warehouse_address);
				
				redirectUrl = "StockConfigMgrServlet?mode=list_warehouse";

			////////////////////////
			//	â������ Update
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
