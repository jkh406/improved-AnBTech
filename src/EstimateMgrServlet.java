import com.anbtech.em.entity.*;
import com.anbtech.em.db.*;
import com.anbtech.em.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class EstimateMgrServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;

	/***********
	 * �Ҹ���
	 ***********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/********************************
	 * get������� �Ѿ���� �� ó��
	 ********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		//���ε���
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/em/";

		//�⺻ �Ķ����
		String mode				= request.getParameter("mode");
		String tablename		= request.getParameter("tablename");
		String page				= request.getParameter("page");
		String no				= request.getParameter("no");
		String searchword		= Hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope		= request.getParameter("searchscope");
		String category			= request.getParameter("category");
		String umask			= request.getParameter("umask");

		if (mode == null) mode = "list";
		if (no == null) no = "";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

		//������ �Ķ����
		String company_name		= Hanguel.toHanguel(request.getParameter("company_name"));	// ������û ȸ���
		String estimate_no		= request.getParameter("estimate_no");	// ������ȣ
		String version			= request.getParameter("ver");			// ��������
		String item_class		= request.getParameter("item_class");	// ǰ�񱸺�(1:�ڻ�ǰ��,2:��������ǰ��)
		String supplyer_code	= Hanguel.toHanguel(request.getParameter("supplyer_code"));// ���޾�ü�ڵ�
		String supplyer_name	= Hanguel.toHanguel(request.getParameter("supplyer_name"));// ���޾�ü��

		String item_name		= Hanguel.toHanguel(request.getParameter("item_name"));	// ǰ���
		String buying_cost		= request.getParameter("buying_cost");	// ����(����)����
		String gains_percent	= request.getParameter("gains_percent");// ������
		String gains_value		= request.getParameter("gains_value");	// ���ͱݾ�
		String supply_cost		= request.getParameter("supply_cost");	// ���޴ܰ�
		String tax_percent		= request.getParameter("tax_percent");	// ����
		String tax_value		= request.getParameter("tax_value");	// ����
		String discount_percent = request.getParameter("discount_percent");// ������
		String discount_value	= request.getParameter("discount_value");// ���αݾ�
		String quantity			= request.getParameter("quantity");		// ����
		String unit				= request.getParameter("unit");			// ����
		String estimate_value	= request.getParameter("estimate_value");// ������
		String standards		= Hanguel.toHanguel(request.getParameter("standards"));	// �԰�
		String model_name		= Hanguel.toHanguel(request.getParameter("model_name"));// �𵨸�
		String model_code		= Hanguel.toHanguel(request.getParameter("model_code"));// ���ڵ�

		String special_change	= request.getParameter("special_change");// Ư�����αݾ�
		String cut_unit			= request.getParameter("cut_unit");		 // ���� ����

		//�⺻������ ���Ǵ� ���� ����
		String redirectUrl = "";
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

			////////////////////////////////////////////////////////////
			// �ܺα������� ǰ�� ����Ʈ ��� ��
			// �ܺα������� ǰ�� ã��(����ǰ�� �߰��ÿ� ������ �˾�â)
			////////////////////////////////////////////////////////////
			if(mode.equals("list_out_item") || mode.equals("search_out_item")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);

				ArrayList item_list = new ArrayList();
				item_list = itemDAO.getOutItemList(mode,searchword,searchscope,category,page);
				request.setAttribute("Item_List", item_list);

				com.anbtech.em.business.EmLinkUrlBO redirectBO = new com.anbtech.em.business.EmLinkUrlBO(con);
				com.anbtech.em.entity.EmLinkUrl redirect = new com.anbtech.em.entity.EmLinkUrl();
				redirect = redirectBO.getOutItemListLink(mode,searchword,searchscope,category,page);
				request.setAttribute("OutItemRedirect",redirect);

				String url = "/em/out_item/list_out_item.jsp";
				if(mode.equals("search_out_item")) url = "/em/out_item/search_out_item.jsp";

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			/////////////////////////////////
			// �ܺα������� ǰ�� ��� �� ���� ��
			/////////////////////////////////
			else if(mode.equals("add_out_item")){
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				item = itemBO.getWriteOutItemForm(mode,model_code,supplyer_code,category);
				request.setAttribute("ItemInfo", item);
				
				getServletContext().getRequestDispatcher("/em/out_item/add_out_item.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////
			// �ܺα������� ǰ���� �������� �󼼺��� ��
			// ǰ�� ���޴ܰ����� ������Ʈ ��
			/////////////////////////////////
			else if(mode.equals("view_out_item_supply_info") || mode.equals("modify_out_item_supply_info")){
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				//ǰ������ �� �������� ��������
				item = itemDAO.getOutItemInfo(model_code,supplyer_code);
				request.setAttribute("ItemInfo", item);

				//�������� �����̷� ��������
				ArrayList list_supply_info = new ArrayList();
				list_supply_info = itemDAO.getListSupplyInfo(model_code,supplyer_code);
				request.setAttribute("ListSupplyInfo", list_supply_info);

				String url = "/em/out_item/view_item_supply_info.jsp?mode="+mode;
				if(mode.equals("modify_out_item_supply_info")) url = "/em/out_item/modify_item_supply_info.jsp?mode="+mode;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}
			
			////////////////////////////////////////////////
			// ÷�� ������ ���� �ٿ�ε�
			////////////////////////////////////////////////
			else if ("download".equals(mode)){
				//���� �̵��ϴ� �� ���� �ٿ�ε��Ų��.
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.entity.ItemInfoTable file = new com.anbtech.em.entity.ItemInfoTable();
				file = itemBO.getFile_fordown(tablename, no);
				String filename = file.getFileName();
				String filetype = file.getFileType();
				String filesize = file.getFileSize();

				//boardpath ���� ���ϱ��� ��� ����
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/em/" + no + ".bin";
			
				if (filetype.indexOf("mage")<=0)
					filetype = "application/unknown";
				
				String strClient=request.getHeader("User-Agent");

				filename = new String(filename.getBytes("euc-kr"),"8859_1"); 

				if(strClient.indexOf("MSIE 5.5")>-1) response.setHeader("Content-Disposition","filename="+filename);
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

			/////////////////////////////////
			// �ܺα������� ǰ���� �������� ����ó��
			/////////////////////////////////
			else if(mode.equals("delete_out_item_supply_info")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				itemDAO.deleteOutItemSupplyInfo(no,filepath);
					
				redirectUrl = "EstimateMgrServlet?mode=view_out_item_supply_info&model_code="+model_code+"&supplyer_code="+supplyer_code;
			}

			/////////////////////////////////
			// �ܺα������� ǰ������ �󼼺���
			// �ܺα������� ǰ������ ���� ��
			// �ܺα������� ǰ���� ����ó �߰� ��
			/////////////////////////////////
			else if(mode.equals("view_out_item") || mode.equals("modify_out_item") || mode.equals("add_out_item_supply_info")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				//ǰ������ ��������
				item = itemDAO.getOutItemInfo(model_code);
				request.setAttribute("ItemInfo", item);

				//ǰ��������� ����Ʈ ��������
				ArrayList list = new ArrayList();
				list = itemDAO.getOutItemSupplyList(model_code);
				request.setAttribute("SupplyerList", list);

				String url = "/em/out_item/view_out_item.jsp?mode="+mode;
				if(mode.equals("modify_out_item")) url = "/em/out_item/modify_out_item.jsp?mode="+mode;
				else if(mode.equals("add_out_item_supply_info")) url = "/em/out_item/add_out_item_supply_info.jsp?mode="+mode;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			/////////////////////////////////
			// �ܺα������� ǰ������ ����ó��
			/////////////////////////////////
			else if(mode.equals("delete_out_item")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ���� 
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					itemDAO.deleteAllOutItemSupplyInfo(model_code);
					itemDAO.deleteOutItemInfo(model_code);

					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "EstimateMgrServlet?mode=list_out_item";
			}

			////////////////////////////////////////////////////////////
			// ������ ��� ���
			////////////////////////////////////////////////////////////
			else if(mode.equals("list") || mode.equals("mylist")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);
				com.anbtech.em.entity.EstimateInfoTable estimate = new com.anbtech.em.entity.EstimateInfoTable();

				ArrayList estimate_list = new ArrayList();
				estimate_list = estimateDAO.getEstimateList(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Estimate_List", estimate_list);

				com.anbtech.em.business.EmLinkUrlBO redirectBO = new com.anbtech.em.business.EmLinkUrlBO(con);
				com.anbtech.em.entity.EmLinkUrl redirect = new com.anbtech.em.entity.EmLinkUrl();
				redirect = redirectBO.getListLink(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/em/estimate/list.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////////////
			// ���õ� ������ȣ �� ������ �������� �� ����ǰ�� ����Ʈ�� ���
			////////////////////////////////////////////////////////////
			else if(mode.equals("view") || mode.equals("view_my") || mode.equals("print") || mode.equals("ef_view")){
				com.anbtech.em.db.EstimateItemViewDAO item_viewDAO = new com.anbtech.em.db.EstimateItemViewDAO(con);
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);
				com.anbtech.em.entity.EstimateInfoTable estimate = new com.anbtech.em.entity.EstimateInfoTable();
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				//�������� ��������
				estimate = estimateDAO.getEstimateInfo(estimate_no,version);
				request.setAttribute("Estimate_Info", estimate);				
				
				//��ũ���ڿ� ��������
				com.anbtech.em.business.EmLinkUrlBO redirectBO = new com.anbtech.em.business.EmLinkUrlBO(con);
				com.anbtech.em.entity.EmLinkUrl redirect = new com.anbtech.em.entity.EmLinkUrl();
				redirect = redirectBO.getViewLink(mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);

				//�����׸� ��������
				ArrayList item_list = new ArrayList();
				item_list = item_viewDAO.getEstimateItemList(estimate_no,version,estimate.getCutUnit());
				request.setAttribute("Item_List", item_list);

				//������ �����
				//mode == ef_view(ǰ������) or mode == ef_view_d(����ǰ��������)�϶��� ���������� �����´�.
				if(mode.equals("ef_view")||mode.equals("ef_view_d")){
					com.anbtech.admin.entity.ApprovalInfoTable app_table = new com.anbtech.admin.entity.ApprovalInfoTable();
					com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

					//���� ������ȣ�� ������ ��, ������ȣ�� �ش��ϴ� ���� ������ �����´�.
					String aid = estimateDAO.getAid(estimate_no,version);
					String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
					app_table = appDAO.getApprovalInfo("em_approval_info",aid,sign_path);
					request.setAttribute("Approval_Info",app_table);
				}
				
				String go_url = "";
				//���� ���
				if(mode.equals("view")||mode.equals("view_my")) go_url = "/em/estimate/view.jsp?mode="+mode;
				//���ڰ��� ���� ���
				else if(mode.equals("print")) go_url = "/em/estimate/print.jsp?mode="+mode;
				//������ ����� ���� ���
				else if(mode.equals("ef_view")) go_url = "/em/estimate/ef_view.jsp?mode="+mode;
				getServletContext().getRequestDispatcher(go_url).forward(request,response);
			}

			/////////////////////////////////
			// �ű� ������ �ۼ� (���� ����)
			//////////////////////////////////
			else if(mode.equals("write") || mode.equals("modify") || mode.equals("copy") || mode.equals("revision")){
				com.anbtech.em.business.EstimateBO estimateBO = new com.anbtech.em.business.EstimateBO(con);
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);
				com.anbtech.em.entity.EstimateInfoTable estimate = new com.anbtech.em.entity.EstimateInfoTable();

				//���õ� ������ȣ�� �������� �������� �ִ��� üũ�Ͽ� ������ ���������� ���ϰ� �Ѵ�.
				if(mode.equals("revision")){
					int ing_count = estimateDAO.getRevisioningCount(estimate_no);
					if(ing_count > 0){
						PrintWriter out = response.getWriter();
						out.println("	<script>");
						out.println("	alert('�������� �������� �̹� �������߿� �־� �������� �� �����ϴ�.');");
						out.println("	history.go(-1);");
						out.println("	</script>");
						out.close();					
					}
				}

				// �������� ��������
				estimate = estimateBO.getWriteForm(mode,estimate_no,version,login_id);
				request.setAttribute("EstimateInfo",estimate);

				com.anbtech.em.business.EmLinkUrlBO linkBO = new com.anbtech.em.business.EmLinkUrlBO(con);
				com.anbtech.em.entity.EmLinkUrl link = new com.anbtech.em.entity.EmLinkUrl();
				link = linkBO.getWriteLink(mode,estimate.getVersion());
				request.setAttribute("LinkUrl",link);

				getServletContext().getRequestDispatcher("/em/estimate/write.jsp").forward(request,response);
			}

			////////////////////////////////////////////////////////////
			// ����ǰ�� �߰� �� 
			////////////////////////////////////////////////////////////
			else if(mode.equals("write_item")){
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);
				com.anbtech.em.business.EstimateBO estimateBO = new com.anbtech.em.business.EstimateBO(con);
				com.anbtech.em.db.EstimateItemViewDAO item_viewDAO = new com.anbtech.em.db.EstimateItemViewDAO(con);
				com.anbtech.em.entity.EstimateInfoTable estimate = new com.anbtech.em.entity.EstimateInfoTable();

				//�������� ��������
				estimate = estimateBO.getWriteForm(mode,estimate_no,version,login_id);
				request.setAttribute("EstimateInfo",estimate);

				//ǰ������ ��������
				ArrayList item_list = new ArrayList();
				item_list = item_viewDAO.getEstimateItemList(estimate_no,version,estimate.getCutUnit());
				request.setAttribute("Item_List", item_list);

				getServletContext().getRequestDispatcher("/em/estimate/write_item.jsp").forward(request,response);
			}

			/////////////////////////////////
			// Ư������ �� �ݾ����� ó��
			/////////////////////////////////
			else if(mode.equals("apply_special")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.updateSpecialChange(estimate_no,version,special_change,cut_unit);
				
				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;
			}

			/////////////////////////////////
			// ����ǰ�� �߰� �� ���� �� ��������
			/////////////////////////////////
			else if(mode.equals("add_estimate_item") || mode.equals("modify_estimate_item")){
				com.anbtech.em.business.EstimateBO estimateBO = new com.anbtech.em.business.EstimateBO(con);
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				item = estimateBO.getAddEstimateItemForm(mode,estimate_no,version,item_class,no);
				request.setAttribute("ItemInfo", item);
				
				getServletContext().getRequestDispatcher("/em/estimate/add_item.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////
			// ����ǰ�� ���� ó��
			/////////////////////////////////
			else if(mode.equals("delete_estimate_item")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.deleteEstimateItemInfo(no);
					
				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;
			}

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	}

	/************************************
	 * post������� �Ѿ���� �� ó��
	 ************************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/em/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//�⺻ �Ķ����
		String no				= multi.getParameter("no");
		String mode				= multi.getParameter("mode");
		String page				= multi.getParameter("page");
		String searchword		= multi.getParameter("searchword");
		String searchscope		= multi.getParameter("searchscope");
		String category			= multi.getParameter("category");

		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

		//������������ �Ķ����
		String estimate_no		= multi.getParameter("estimate_no");	//������ȣ
		String version			= multi.getParameter("ver");			//��������
		String company_name		= multi.getParameter("company_name");	//ȸ���
		String estimate_subj	= multi.getParameter("estimate_subj");	//��������
		String charge_name		= multi.getParameter("charge_name");	//����� �̸�
		String charge_rank		= multi.getParameter("charge_rank");	//����� ����
		String charge_div		= multi.getParameter("charge_div");		//����� �μ���
		String charge_mobile	= multi.getParameter("charge_mobile");	//����� �޴���ȭ��ȣ
		String charge_tel		= multi.getParameter("charge_tel");		//����� ��ȭ��ȣ
		String charge_fax		= multi.getParameter("charge_fax");		//����� �ѽ�
		String charge_email		= multi.getParameter("charge_email");	//����� �̸���
		String delivery_place	= multi.getParameter("delivery_place");	//�ε����
		String payment_terms	= multi.getParameter("payment_terms");	//��������
		String guarantee_term	= multi.getParameter("guarantee_term");	//�����Ⱓ
		String delivery_period	= multi.getParameter("delivery_period");//��ǰ�Ⱓ
		String valid_period		= multi.getParameter("valid_period");	//��ȿ�Ⱓ
		String delivery_day		= multi.getParameter("delivery_day");	//��ǰ����

		//����ǰ�� ���� �Ķ����
		String item_name		= multi.getParameter("item_name");		//ǰ���
		String maker_name		= multi.getParameter("maker_name");		//����ȸ���
		String model_name		= multi.getParameter("model_name");		//�𵨸�
		String model_code		= multi.getParameter("model_code");		//���ڵ�
		String unit				= multi.getParameter("unit");			//����
		String standards		= multi.getParameter("standards");		//�԰�
		String item_class		= multi.getParameter("item_class");		//ǰ�񱸺�(��ü���� or �������� or ��Ű��)
		String gains_percent	= multi.getParameter("gains_percent");	// ������
		String gains_value		= multi.getParameter("gains_value");	// ���ͱݾ�
		String tax_value		= multi.getParameter("tax_value");		// ����
		String discount_value	= multi.getParameter("discount_value");	// ���αݾ�
		String quantity			= multi.getParameter("quantity");		// ����
		String estimate_value	= multi.getParameter("estimate_value");	// ������

		//ǰ�� ���� ���� ���� �Ķ����
		String supplyer_code	= multi.getParameter("supplyer_code");	//���޾�ü�ڵ�
		String supplyer_name	= multi.getParameter("supplyer_name");	//���޾�ü��
		String buying_cost		= multi.getParameter("buying_cost");	//���Կ���
		String tax_percent		= multi.getParameter("tax_percent");	//����
		String discount_percent	= multi.getParameter("discount_percent");	//������
		String supply_cost		= multi.getParameter("supply_cost");	//���޴ܰ�
		String other_info		= multi.getParameter("other_info");		//��Ÿ����

		//�⺻ ���� ����
		String redirectUrl = "";
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
			
			////////////////////
			// �ܺ�����ǰ�� ���ó��
			////////////////////
			if(mode.equals("add_out_item")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.entity.ItemInfoTable file = new com.anbtech.em.entity.ItemInfoTable();
				
				con.setAutoCommit(false);	// Ʈ������� ���� 
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//ǰ������ ����
					itemDAO.saveOutItemInfo(item_name,unit,model_name,model_code,standards,maker_name,login_id);

					//�������� ����
					String mid = System.currentTimeMillis() + ""; //������ȣ����
					itemDAO.addSupplyInfo(mid,model_code,supplyer_code,supplyer_name,supply_cost,other_info,login_id);

					//÷������ ó��
					file = itemBO.getFile_frommulti(multi,mid,filepath);
					itemBO.updFile(mid,file.getFileName(),file.getFileType(),file.getFileSize());

					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "EstimateMgrServlet?mode=list_out_item";
			}

			////////////////////
			// �ܺ�����ǰ���� ���޴ܰ����� ������Ʈó�� ��
			// �ܺ�����ǰ���� ���޴ܰ����� �߰�ó��
			////////////////////
			else if(mode.equals("modify_out_item_supply_info") || mode.equals("add_out_item_supply_info")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.entity.ItemInfoTable file = new com.anbtech.em.entity.ItemInfoTable();

				con.setAutoCommit(false);	// Ʈ������� ���� 
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���޴ܰ����� ����
					String mid = System.currentTimeMillis() + ""; //������ȣ����
					itemDAO.addSupplyInfo(mid,model_code,supplyer_code,supplyer_name,supply_cost,other_info,login_id);

					//÷������ ó��
					file = itemBO.getFile_frommulti(multi,mid,filepath);
					itemBO.updFile(mid,file.getFileName(),file.getFileType(),file.getFileSize());

					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				if(mode.equals("modify_out_item_supply_info")) redirectUrl = "EstimateMgrServlet?mode=view_out_item_supply_info&model_code="+model_code+"&supplyer_code="+supplyer_code;
				else redirectUrl = "EstimateMgrServlet?mode=view_out_item&model_code="+model_code;
			}

			////////////////////
			// �ܺ�����ǰ������ ����ó��
			////////////////////
			else if(mode.equals("modify_out_item")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				
				//��������
				itemDAO.modifyOutItemInfo(item_name,unit,model_code,model_name,standards,maker_name,login_id);

				redirectUrl = "EstimateMgrServlet?mode=view_out_item&model_code="+model_code;
			}

			//////////////////////
			 // �������� ���ó��
			 /////////////////////
			else if(mode.equals("write")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.saveEstimateInfo(company_name,estimate_subj,estimate_no,version,charge_name,charge_rank,charge_div,charge_tel,charge_mobile,charge_fax,charge_email,delivery_place,payment_terms,guarantee_term,delivery_period,valid_period,delivery_day,login_id);

				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;
			}

			///////////////////////////
			 // ���� ����,����,������ ó��
			 //////////////////////////
			else if(mode.equals("modify") || mode.equals("copy") || mode.equals("revision")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.updEstimateInfo(company_name,estimate_subj,estimate_no,version,charge_name,charge_rank,charge_div,charge_tel,charge_mobile,charge_fax,charge_email,delivery_place,payment_terms,guarantee_term,delivery_period,valid_period,delivery_day,login_id);

				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;
			}

			/////////////////////////////////
			// ����ǰ�� �߰�ó��
			/////////////////////////////////
			else if(mode.equals("add_estimate_item")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.addEstimateItem(estimate_no,version,item_class,item_name,model_code,model_name,quantity,unit,standards,maker_name,supplyer_code,supplyer_name,buying_cost,gains_percent,gains_value,supply_cost,discount_percent,discount_value,tax_percent,tax_value,estimate_value);
				
				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;

				PrintWriter out = response.getWriter();
				out.println("	<script>");
				out.println("	alert('���������� �ԷµǾ����ϴ�.');");
				out.println("	opener.location.href('" + redirectUrl + "');");
				out.println("	self.close();");
				out.println("	</script>");
				out.close();
			}

			/////////////////////////////////
			// ����ǰ�� ����ó��
			/////////////////////////////////
			else if(mode.equals("modify_estimate_item")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.modifyEstimateItem(no,item_class,item_name,model_code,model_name,quantity,unit,standards,maker_name,supplyer_code,supplyer_name,buying_cost,gains_percent,gains_value,supply_cost,discount_percent,discount_value,tax_percent,tax_value,estimate_value);
					
				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;

				PrintWriter out = response.getWriter();
				out.println("	<script>");
				out.println("	alert('���������� �����Ǿ����ϴ�.');");
				out.println("	opener.location.href('" + redirectUrl + "');");
				out.println("	self.close();");
				out.println("	</script>");
				out.close();
			}

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	}
}
