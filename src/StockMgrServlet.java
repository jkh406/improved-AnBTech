import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class StockMgrServlet extends HttpServlet {

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

		if (mode == null) mode = "list_item";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

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

		//������ �Ķ���͵�
		String inout_no			= request.getParameter("inout_no");
		String upload_folder	= request.getParameter("upload_folder");
		String item_code		= request.getParameter("item_code");
		String in_or_out		= request.getParameter("in_or_out");
		String factory_code		= request.getParameter("factory_code");
		String factory_name		= request.getParameter("factory_name");
		String delivery_no		= request.getParameter("delivery_no");
		String inout_type		= request.getParameter("inout_type");
		String items			= request.getParameter("items"); // �����ϰ����ó����. ����Ƿڹ�ȣ1|ǰ���ڵ�1|����1,����Ƿڹ�ȣ2|ǰ���ڵ�2|����2,

		String aid				= request.getParameter("aid");
		String out_no			= request.getParameter("out_no");
		String ref_no			= request.getParameter("ref_no");
		String delivery_quantity= request.getParameter("delivery_quantity");

		if (factory_name == null) factory_name = "";
		else factory_name = com.anbtech.text.StringProcess.kwordProcess(factory_name);
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////
			// ������Ȳ ��ȸ����Ʈ
			///////////////////////////////////////////////////
			if(mode.equals("list_inout")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				ArrayList inout_list = new ArrayList();

				inout_list = stDAO.getInOutList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("INOUT_LIST",inout_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"st_inout_master",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);
				
				getServletContext().getRequestDispatcher("/st/inout/list_inout.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// �����Ȳ ��ȸ����Ʈ
			///////////////////////////////////////////////////
			else if(mode.equals("list_stock_master") || mode.equals("list_stock_real") || mode.equals("list_stock_rop") || mode.equals("list_stock_resonable")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				ArrayList stock_list = new ArrayList();

				stock_list = stDAO.getItemStockList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("STOCK_LIST",stock_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"v_item_stock_master",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);

				String url = "";
				if(mode.equals("list_stock_master")) url = "/st/stock/list_stock.jsp?mode="+mode;
				else if(mode.equals("list_stock_real")) url = "/st/stock/list_stock_real.jsp?mode="+mode;
				else if(mode.equals("list_stock_rop")) url = "/st/stock/list_stock_rop.jsp?mode="+mode;
				else if(mode.equals("list_stock_resonable")) url = "/st/stock/list_stock_resonable.jsp?mode="+mode;
				
				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ��Ÿ��������� ��ȸ ����Ʈ
			///////////////////////////////////////////////////
			else if(mode.equals("list_etc_inout")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				ArrayList list = new ArrayList();

				list = stDAO.getEtcInOutInfoList(mode,in_or_out,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("INOUT_LIST",list);
			
				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForEtcInOutList(mode,in_or_out,searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);

				if(in_or_out.equals("IN")) 
					getServletContext().getRequestDispatcher("/st/in/list_etc_in.jsp?mode="+mode).forward(request,response);
				else if(in_or_out.equals("OUT"))
					getServletContext().getRequestDispatcher("/st/out/list_etc_out.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			//  ��Ÿ��������� ��� �� ���� ��
			///////////////////////////////////////////////////
			else if (mode.equals("view_etc_inout_info") || mode.equals("write_etc_inout_info") || mode.equals("update_etc_inout_info")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.EtcInOutInfoTable table = new com.anbtech.st.entity.EtcInOutInfoTable();

				//���õ� ������ȣ�� ���� ������ �����´�.
				table = stBO.getEtcInOutInfoForm(mode,inout_no,login_id);
				request.setAttribute("INOUT_INFO",table);

				//��������ϰ�� ÷������ ����Ʈ ��������
				if(mode.equals("update_etc_inout_info")){
					ArrayList arry = new ArrayList();
					arry = (ArrayList)stDAO.getEtcInOutFileList("st_etc_inout_info",inout_no);
					request.setAttribute("FILE_LIST",arry);
				}
				
				//���õ� ������ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getEtcInOutItemList(inout_no);
				request.setAttribute("ITEM_LIST",item_list);

				if(in_or_out.equals("IN")){
					if(mode.equals("view_etc_inout_info"))
						getServletContext().getRequestDispatcher("/st/in/view_etc_in.jsp?mode="+mode).forward(request,response);
					else
						getServletContext().getRequestDispatcher("/st/in/write_etc_in.jsp?mode="+mode).forward(request,response);
				}else if(in_or_out.equals("OUT")){
					if(mode.equals("view_etc_inout_info"))
						getServletContext().getRequestDispatcher("/st/out/view_etc_out.jsp?mode="+mode).forward(request,response);
					else
						getServletContext().getRequestDispatcher("/st/out/write_etc_out.jsp?mode="+mode).forward(request,response);
				}
			}

			////////////////////////////////////////////////////
			//  ��Ÿ��������� ����ó��
			///////////////////////////////////////////////////
			else if (mode.equals("delete_etc_inout_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);

				//�����ǰ���� �����ϴ��� üũ
				int item_count = stDAO.getEtcInOutItemCount(inout_no);
				if(item_count > 0){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('�����ϰ��� �ϴ� �����ǿ��� �Ѱ� �̻��� �����ǰ���� ���������� ������ �� �����ϴ�.');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}else{
					stDAO.deleteEtcInOutInfo(inout_no);
				}
				
				redirectUrl = "StockMgrServlet?mode=list_etc_inout&in_or_out=" + in_or_out;

			}

			////////////////////////////////////////////////////
			// ��Ÿ�����ǰ�� ����� ��������
			///////////////////////////////////////////////////
			else if(mode.equals("add_etc_inout_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.EtcInOutInfoTable table = new com.anbtech.st.entity.EtcInOutInfoTable();

				//���õ� ������ȣ �� ǰ���ڵ忡 ���� ������ �����´�.
				table = stBO.getEtcInOutItemForm(mode,inout_no,item_code,login_id);
				request.setAttribute("INOUT_INFO",table);

				//���õ� ������ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getEtcInOutItemList(inout_no);
				request.setAttribute("ITEM_LIST",item_list);

				if(in_or_out.equals("IN"))
					getServletContext().getRequestDispatcher("/st/in/write_etc_in_item.jsp?mode="+mode+"&inout_type="+inout_type).forward(request,response);
				else if(in_or_out.equals("OUT"))
					getServletContext().getRequestDispatcher("/st/out/write_etc_out_item.jsp?mode="+mode+"&inout_type="+inout_type).forward(request,response);
			}

			//////////////////////////
			// ROP�� ���� ���ſ�ûó��
			//////////////////////////
			else if(mode.equals("request_rop_order")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);

				//��û��ȣ ����
				String request_no = purchaseDAO.getRequestNo();

				//��û�ð�
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String request_date = vans.format(now);

				//��û������
				com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
				com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
				userinfo = userDAO.getUserListById(login_id);

				con.setAutoCommit(false);
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//��û���� ����	
					purchaseDAO.saveRequestInfo(request_no,request_date,userinfo.getAcCode(),userinfo.getDivision(),login_id,userinfo.getUserName(),"ROP","0","","",request_no.substring(3,5),request_no.substring(5,7),request_no.substring(8,11),factory_code,factory_name);
					
					//��ûǰ�� ����
					stBO.saveRequestItem(mode,item_code,request_no);

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				getServletContext().getRequestDispatcher("/st/stock/result_request_rop.jsp").forward(request,response);
			}

			////////////////////////////////////////////////////////
			//  ÷������ �ٿ�ε�
			////////////////////////////////////////////////////////
			else if (mode.equals("download_etc_inout")){
					
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.EtcInOutInfoTable file = new com.anbtech.st.entity.EtcInOutInfoTable();
				
				file = stBO.getFileForDown("st_etc_inout_info", inout_no);
								
				String filename = file.getFname();
				String filetype = file.getFtype();
				String filesize = file.getFsize();
				String fileumask = file.getFumask();
					
				//boardpath ���� ���ϱ��� ��� ����
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/st/" + upload_folder + "/" + fileumask + ".bin";
				
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
			
			//////////////////////////
			// ���尣����̵� ��
			//////////////////////////
			else if(mode.equals("write_item_shift_info") || mode.equals("view_item_shift_info")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.StShiftInfoTable table = new com.anbtech.st.entity.StShiftInfoTable();
				
				table=stBO.getShiftInfoForm(mode,login_id,mid);
				request.setAttribute("ITEM_SHIFT_INFO",table);
				
				if (mode.equals("view_item_shift_info"))	{
					getServletContext().getRequestDispatcher("/st/shift/view_fshift_info.jsp?mode="+mode).forward(request,response);
				} else {								
					getServletContext().getRequestDispatcher("/st/shift/write_fshift_info.jsp?mode="+mode).forward(request,response);
				}
			}

			//////////////////////////
			// ǰ������̵� ��
			//////////////////////////
			else if(mode.equals("write_item_ishift_info")||mode.equals("view_item_ishift_info")){

				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.StShiftInfoTable table = new com.anbtech.st.entity.StShiftInfoTable();

				table=stBO.getShiftInfoForm(mode,login_id,mid);
				request.setAttribute("ITEM_SHIFT_INFO",table);
				
				if (mode.equals("view_item_ishift_info")) {
					getServletContext().getRequestDispatcher("/st/shift/view_ishift_info.jsp?mode="+mode).forward(request,response);
				} else {											
					getServletContext().getRequestDispatcher("/st/shift/write_ishift_info.jsp?mode="+mode).forward(request,response);
				}
			}

			//////////////////////////
			// ���尣����̵����� ����Ʈ
			//////////////////////////
			else if(mode.equals("list_item_shift_info")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				ArrayList shift_info_list = new ArrayList();

				shift_info_list = stDAO.getItemShiftInfoList(mode,searchword,searchscope,page,l_maxlist);
				request.setAttribute("SHIFT_INFO_LIST",shift_info_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"st_item_shift_info",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);

				getServletContext().getRequestDispatcher("/st/shift/list_shift_info.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ��������Ƿ�ǰ�� ����Ʈ ���(���̺�:st_reserved_item_info)
			// �����ڵ尡 S53 �̻��̸鼭 �������� < ��û���� �� ǰ��
			///////////////////////////////////////////////////
			else if(mode.equals("list_reserved_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = stDAO.getReservedItemList(mode,searchword,searchscope,login_id);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/list_reserved_item.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ���õ� ��������Ƿ�ǰ���� ���ڰ��縦 ���� ��������� ����
			///////////////////////////////////////////////////
			else if(mode.equals("app_reserved_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);

				//�ӽ� ���ڰ��� ������ȣ ����
				String tmp_aid = System.currentTimeMillis() + "";

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���õ� ����Ƿ�ǰ����� ����ǰ�� ����Ѵ�.
					stBO.saveToDeliveryItem(mode,items,tmp_aid,login_id);

					con.commit();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//���ڰ��� ȭ������ �б�
				redirectUrl = "../gw/approval/module/part_TakeoutApp.jsp?out_no=" + tmp_aid;
			}

			////////////////////////////////////////////////////
			// �����ǰ���� ������ ����ó��
			// ��û�� ������ ��� ����Ű�� �ʰ� ������ ����ų ��쿡 �������� ���������ϰ� �Ѵ�.
			///////////////////////////////////////////////////
			else if(mode.equals("modify_delivery_quantity")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);

				stDAO.updateDeliveryQuantity(delivery_no,ref_no,item_code,aid,delivery_quantity);

				redirectUrl = "StockMgrServlet?mode=list_app_reserved_item&out_no=" + aid;
			}

			////////////////////////////////////////////////////
			// ���õ� ���ڰ��������ȣ�� ���� �����ǰ�� ����Ʈ ���(���ڰ��� ��� �� ���� ����ȭ��)
			// mode == list_app_reserved_item : ���ڰ��� ��� ȭ��,�������� ���� ����
			// mode == view_app_reserved_item : ���ڰ��� ���� ����ȭ��
			///////////////////////////////////////////////////
			else if(mode.equals("list_app_reserved_item") || mode.equals("view_app_reserved_item")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);

				//�����ǰ�� ����Ʈ�� �����´�.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedItemList(mode,out_no);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/list_delivery_item.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ���������ǰ�� �� �����ڵ尡 S57(��������οϷ����)�� ǰ��(���̺�:st_deliveried_item_info)
			///////////////////////////////////////////////////
			else if(mode.equals("list_toenter_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = stDAO.getToEnterItemList(mode,searchword,searchscope,login_id);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/list_toenter_item.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ���õ� ǰ�� �ϰ� ���ó��
			///////////////////////////////////////////////////
			else if(mode.equals("delivery_reserved_item_all")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);

				//�������
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String delivery_date = vans.format(now);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���õ� ����Ƿ�ǰ����� �ϰ� ����Ų��.
					stBO.saveDeliveryItem(mode,items,delivery_date,login_id);

					con.commit();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "StockMgrServlet?mode=list_toenter_item";
			}

			////////////////////////////////////////////////////
			// �������Ϸ�ǰ�� ����Ʈ ���(���̺�:st_deliveried_item_info)
			///////////////////////////////////////////////////
			else if(mode.equals("list_deliveried_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedItemList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"st_deliveried_item_info",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);
				
				getServletContext().getRequestDispatcher("/st/out/list_deliveried_item.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ���������Ȳ ����Ʈ ���
			///////////////////////////////////////////////////
			else if(mode.equals("list_deliveried_info")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedInfoList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"v_deliveried_info",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);
				
				getServletContext().getRequestDispatcher("/st/out/list_deliveried_info.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ���������Ȳ �μ��� ����
			///////////////////////////////////////////////////
			else if(mode.equals("print_deliveried_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

				//���ڰ��� ���� ��������
				String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
				if(!aid.equals("")) appTable = appMgrDAO.getApprovalInfo("st_approval_info",aid,sign_path);
				request.setAttribute("Approval_Info",appTable);

				//�����ǰ�� ����Ʈ�� �����´�.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedItemList(mode,aid);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/print_deliveried_info.jsp?mode="+mode+"&aid="+aid).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ���������Ȳ �μ��� ����(Excell ��)
			///////////////////////////////////////////////////
			else if(mode.equals("print_deliveried_info_excell")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

				//���ڰ��� ���� ��������
				//String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
				//if(!aid.equals("")) appTable = appMgrDAO.getApprovalInfo("st_approval_info",aid,sign_path);
				//request.setAttribute("Approval_Info",appTable);

				//�����ǰ�� ����Ʈ�� �����´�.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedItemList(mode,aid);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/print_deliveried_info_excell.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ��޻�������Ƿ�ǰ�� ��� �� �� �󼼺��� �� ��������
			///////////////////////////////////////////////////
			else if (mode.equals("write_etc_delivery") || mode.equals("add_etc_delivery") || mode.equals("modify_etc_delivery")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				//���õ� ����Ƿڹ�ȣ�� ���� ������ �����´�.
				table = stBO.getEtcDeliveryInfoForm(mode,delivery_no,item_code,login_id);
				request.setAttribute("DELIVERY_INFO",table);
				
				getServletContext().getRequestDispatcher("/st/out/write_etc_delivery.jsp?mode="+mode).forward(request,response);
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
		
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/st/" + upload_folder;

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

		//������ �Ķ���͵�
		String in_or_out		= multi.getParameter("in_or_out");
		String inout_no			= multi.getParameter("inout_no");
		String inout_type		= multi.getParameter("inout_type");
		String inout_date		= multi.getParameter("inout_date");
		String total_mount		= multi.getParameter("total_mount");
		String quantity			= multi.getParameter("quantity");
		String item_unit		= multi.getParameter("item_unit");
		String inout_cost		= multi.getParameter("inout_cost");
		String factory_code		= multi.getParameter("factory_code");
		String factory_name		= multi.getParameter("factory_name");
		String warehouse_code	= multi.getParameter("warehouse_code");
		String warehouse_name	= multi.getParameter("warehouse_name");
		String unit_type		= multi.getParameter("unit_type");
		String supplyer_code	= multi.getParameter("supplyer_code");
		String supplyer_name	= multi.getParameter("supplyer_name");
		String requester_div_name	= multi.getParameter("requester_div_name");
		String requester_div_code	= multi.getParameter("requester_div_code");
		String requester_id		= multi.getParameter("requester_id");
		String requester_info	= multi.getParameter("requester_info");
		String monetary_unit	= multi.getParameter("monetary_unit");
		String item_code		= multi.getParameter("item_code");
		String item_name		= multi.getParameter("item_name");
		String item_desc		= multi.getParameter("item_desc");
		String item_type		= multi.getParameter("item_type");
		String unit_cost		= multi.getParameter("unit_cost");

		
		// ����̵���������
		String shift_no				= multi.getParameter("shift_no");			// �̵���ȣ
		String shift_type			= multi.getParameter("shift_type");			// �̵�����
		String sr_factory_code		= multi.getParameter("sr_factory_code");	// source �����ڵ�
		String sr_factory_name		= multi.getParameter("sr_factory_name");	// source �����
		String sr_warehouse_code	= multi.getParameter("sr_warehouse_code");	// source â���ڵ�
		String sr_warehouse_name	= multi.getParameter("sr_warehouse_name");	// source â���
		String sr_item_code			= multi.getParameter("sr_item_code");		// source ǰ���ڵ�
		String sr_item_name			= multi.getParameter("sr_item_name");		// source ǰ���
		String sr_item_type			= multi.getParameter("sr_item_type");		// source ǰ������
		String sr_item_desc			= multi.getParameter("sr_item_desc");		// source ǰ�񼳸�
		String dt_factory_code		= multi.getParameter("dt_factory_code");	// target �����ڵ�
		String dt_factory_name		= multi.getParameter("dt_factory_name");	// target �����
		String dt_warehouse_code	= multi.getParameter("dt_warehouse_code");	// target â���ڵ�
		String dt_warehouse_name	= multi.getParameter("dt_warehouse_name");	// target â���
		String dt_item_code			= multi.getParameter("dt_item_code");		// target ǰ���ڵ�
		String dt_item_name			= multi.getParameter("dt_item_name");		// target ǰ���
		String dt_item_type			= multi.getParameter("dt_item_type");		// target ǰ������
		String dt_item_desc			= multi.getParameter("dt_item_desc");		// target ǰ�񼳸�
		String stock_unit			= multi.getParameter("stock_unit");			// ��� ����
		String requestor_id			= multi.getParameter("requestor_id");		// ����� ID
		String requestor_info		= multi.getParameter("requestor_info");		// ����� NAME
		String requestor_div_code	= multi.getParameter("requestor_div_code");	// ����� �μ��ڵ�
		String requestor_div_name	= multi.getParameter("requestor_div_name");	// ����� �μ���
		String reg_date				= multi.getParameter("reg_date");			// �̵�����
	
		//�������ó������ �Ķ���͵�
		String delivery_no			= multi.getParameter("delivery_no");		// ����Ƿڹ�ȣ
		String old_quantity			= multi.getParameter("old_quantity");		// ������������
		String ref_no				= multi.getParameter("ref_no");				// ���ù�ȣ

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//////////////////////////
			// ��Ÿ��������� ���
			//////////////////////////			
			if(mode.equals("write_etc_inout_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.EtcInOutInfoTable file = new com.anbtech.st.entity.EtcInOutInfoTable();
			
				inout_no = stDAO.getEtcInOutNo(in_or_out);
				
				if(in_or_out.equals("IN"))		
					stDAO.saveEtcInOutInfo(in_or_out,inout_no,inout_date,total_mount,"KRW",inout_type,"","",inout_no.substring(3,5),inout_no.substring(5,7),inout_no.substring(8,11),"","",login_id,"");
				else if(in_or_out.equals("OUT"))
					stDAO.saveEtcInOutInfo(in_or_out,inout_no,inout_date,total_mount,"KRW",inout_type,"","",inout_no.substring(4,6),inout_no.substring(6,8),inout_no.substring(9,12),"","",login_id,"");

				//÷������ ���δ�
				file = (com.anbtech.st.entity.EtcInOutInfoTable)stBO.getFile_frommulti(multi,inout_no,filepath);
				//���ε� �� ÷������ ������ DB�� �����ϱ�
				stBO.updFile("st_etc_inout_info",inout_no,file.getFname(),file.getFtype(),file.getFsize(),file.getFumask());

				redirectUrl = "StockMgrServlet?mode=add_etc_inout_item&inout_no="+inout_no+"&in_or_out="+in_or_out+"&inout_type="+inout_type;
			}

			//////////////////////////
			// ��Ÿ�������������ó��
			//////////////////////////
			else if(mode.equals("update_etc_inout_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.EtcInOutInfoTable file = new com.anbtech.st.entity.EtcInOutInfoTable();

				//multi���� ���������� �����ͼ� ó���Ѵ�.
				ArrayList file_list = stDAO.getEtcInOutFileList("st_etc_inout_info",inout_no);
				//���� ���δ�
				file = (com.anbtech.st.entity.EtcInOutInfoTable)stBO.getFile_frommulti(multi,inout_no,filepath,file_list);
					
				con.setAutoCommit(false);
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					// ���ε� �� ÷������ ������ DB�� �����ϱ�
					stBO.updFile("st_etc_inout_info",inout_no,file.getFname(),file.getFtype(),file.getFsize(),file.getFumask());
					
					//�������� ����
					stDAO.updateEtcInOutInfo(inout_no,inout_type,inout_date,requester_div_code,requester_div_name,requester_id,requester_info,total_mount,monetary_unit,supplyer_code,supplyer_name);
					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "StockMgrServlet?mode=add_etc_inout_item&inout_no="+inout_no+"&in_or_out="+in_or_out;
			}

			//////////////////////////
			// ��Ÿ�����ǰ����ó��
			//////////////////////////
			else if(mode.equals("add_etc_inout_item")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//��Ÿ�����ǰ������ ���
					stDAO.saveEtcInOutItemInfo(inout_no,item_code,item_name,item_desc,quantity,item_unit,inout_cost,unit_cost,factory_code,factory_name,warehouse_code,warehouse_name,"","");

					//������Ȳ�� ���
					if(in_or_out.equals("IN")){
//						inout_type = "EI";
					}
					else if(in_or_out.equals("OUT")){
//						inout_type = "EO";
						quantity = "-" + quantity;
					}

					java.util.Date now = new java.util.Date();
					java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
					inout_date = vans.format(now);
					stDAO.addInOutInfo(inout_type,inout_date,inout_no,item_code,item_name,item_desc,item_type,quantity,item_unit,unit_cost,inout_cost,factory_code,factory_name,warehouse_code,warehouse_name,"0","0");

					//�ش�ǰ���� ����������� ���������� �����Ѵ�.
					stBO.updateStockQuantity(item_code,quantity,factory_code,factory_name,warehouse_code,warehouse_name);

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "StockMgrServlet?mode=add_etc_inout_item&inout_no="+inout_no+"&in_or_out="+in_or_out+"&inout_type="+inout_type;
			}

			///////////////////////////////
			// ����̵��������(ǰ��&���尣)
			//////////////////////////////
			else if(mode.equals("write_item_shift_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.StockInfoTable table = new com.anbtech.st.entity.StockInfoTable();
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//�̵���ȣ����
					shift_no = stDAO.getShiftNo();

					//�̵��������
					stDAO.saveItemShiftInfo(shift_no,shift_type,sr_factory_code,sr_factory_name,sr_warehouse_code,sr_warehouse_name,sr_item_code,sr_item_name,sr_item_type,sr_item_desc,dt_factory_code,dt_factory_name,dt_warehouse_code,dt_warehouse_name,dt_item_code,dt_item_name,dt_item_type,dt_item_desc,stock_unit,quantity,requestor_id,reg_date,shift_no.substring(3,5),shift_no.substring(5,7),shift_no.substring(8,11));

					//�ش�ǰ���� ���������� �����´�.
					table = stDAO.getItemUnitCostInfo(sr_item_code);
					inout_cost = Double.toString(Double.parseDouble(table.getUnitCostA()) * Double.parseDouble(quantity));
				
					//������Ȳ�� ����ϰ�, ��������� ���������� ���Ѵ�.
					stDAO.addInOutInfo("MV",reg_date,shift_no,sr_item_code,sr_item_name,sr_item_desc,sr_item_type,"-" + quantity,stock_unit,table.getUnitCostA(),inout_cost,sr_factory_code,sr_factory_name,sr_warehouse_code,sr_warehouse_name,"0","0");

					stBO.updateStockQuantity(sr_item_code,"-" + quantity,sr_factory_code,sr_factory_name,sr_warehouse_code,sr_warehouse_name);

					//������Ȳ�� ����ϰ�, ��������� ���������� ���Ѵ�.
					stDAO.addInOutInfo("MV",reg_date,shift_no,dt_item_code,dt_item_name,dt_item_desc,dt_item_type,quantity,stock_unit,table.getUnitCostA(),inout_cost,dt_factory_code,dt_factory_name,dt_warehouse_code,dt_warehouse_name,"0","0");

					stBO.updateStockQuantity(dt_item_code,quantity,dt_factory_code,dt_factory_name,dt_warehouse_code,dt_warehouse_name);

					con.commit();

				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				redirectUrl = "StockMgrServlet?mode=list_item_shift_info";
			}

/*
			///////////////////////////////
			// �������ó��
			//////////////////////////////
			else if(mode.equals("modify_reserved_item")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.StockInfoTable table = new com.anbtech.st.entity.StockInfoTable();

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ð�
					java.util.Date now = new java.util.Date();
					java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
					String delivery_date = vans.format(now);

					int distance = Integer.parseInt(old_quantity) - Integer.parseInt(quantity);

					//�ش�����Ƿڰ��� ���������(����,���������,�������,�����ڵ� ��) ������Ʈ
					stDAO.updateRreservedItemInfo(mode,delivery_no,item_code,quantity,item_unit,login_id,delivery_date);

					//�ش�ǰ���� ���������� �����´�.
					table = stDAO.getItemUnitCostInfo(item_code);
					inout_cost = Double.toString(Double.parseDouble(table.getUnitCostA()) * Double.parseDouble(quantity));


					//������Ȳ ���
					stDAO.addInOutInfo("MO",delivery_date,delivery_no,item_code,item_name,item_desc,item_type,Integer.toString(distance),item_unit,table.getUnitCostA(),inout_cost,factory_code,factory_name,warehouse_code,warehouse_name,"0","0");

					//��������� ���������(outto_quantity) ������Ʈ
					stBO.updateOuttoQuantity(item_code,Integer.toString(distance),factory_code,factory_name,warehouse_code,warehouse_name);

					//��������� ������(delivery_quantity) ������Ʈ
					stBO.updateDeliveryQuantity(item_code,Integer.toString(-distance),factory_code,factory_name,warehouse_code,warehouse_name);

					//������� ����� ���̺� ������Ʈ
					stDAO.updateMfgMoudle(delivery_no,ref_no,item_code,factory_code,Integer.toString(-distance));

					//�������

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				redirectUrl = "StockMgrServlet?mode=list_reserved_item&searchscope="+searchscope+"&searchword="+searchword;
			}

*/
			//////////////////////////
			// ��Ÿ����Ƿ����� ���ó��
			//////////////////////////			
			if(mode.equals("write_etc_delivery") || mode.equals("add_etc_delivery")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
			
				if(mode.equals("write_etc_delivery")) delivery_no = stDAO.getDeliveryNo();
				stDAO.saveEtcDeliveryInfo(delivery_no,item_code,item_name,item_desc,item_type,quantity,item_unit,reg_date,factory_code,factory_name,warehouse_code,warehouse_name,ref_no,requestor_div_code,requestor_div_name,requestor_id,requestor_info,delivery_no.substring(2,4),delivery_no.substring(4,6),delivery_no.substring(7,10));

				redirectUrl = "StockMgrServlet?mode=add_etc_delivery&delivery_no="+delivery_no;
			}

			//////////////////////////
			// ��Ÿ����Ƿ����� ����ó��
			//////////////////////////			
			if(mode.equals("modify_etc_delivery")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				stDAO.updateEtcDeliveryInfo(delivery_no,item_code,item_name,item_desc,item_type,quantity,item_unit,reg_date,factory_code,factory_name,warehouse_code,warehouse_name,ref_no,requestor_div_code,requestor_div_name,requestor_id,requestor_info);

				redirectUrl = "StockMgrServlet?mode=list_etc_delivery&searchscope="+searchscope+"&searchword="+searchword;
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
