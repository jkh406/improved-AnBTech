import com.anbtech.qc.entity.*;
import com.anbtech.qc.db.*;
import com.anbtech.qc.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class QualityCtrlServlet extends HttpServlet {

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

		//�⺻ �Ķ����
		String mode				= request.getParameter("mode");
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
	
		//������ �Ķ����
		String request_no		= request.getParameter("request_no");
		String item_code		= request.getParameter("item_code");
		String inspection_code	= request.getParameter("inspection_code");
		String sampled_quantity	= request.getParameter("sampled_quantity");
		String bad_quantity		= request.getParameter("bad_quantity");
		String serial_no		= request.getParameter("serial_no");
		String produce_year		= request.getParameter("produce_year");
		String produce_month	= request.getParameter("produce_month");
		String lot_quantity		= request.getParameter("lot_quantity");
		String serial_no_s		= request.getParameter("serial_no_s");
		String serial_no_e		= request.getParameter("serial_no_e");	

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			///////////////////////////////////////
			// �˻����ǿ� �´� �˻��Ƿ� ����Ʈ ��������
			///////////////////////////////////////
			if(mode.equals("list_inspect") || mode.equals("list_result") || mode.equals("list_return") || mode.equals("list_rework")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.entity.InspectionMasterTable table = new com.anbtech.qc.entity.InspectionMasterTable();

				ArrayList list = new ArrayList();
				list = qcDAO.getInspectList(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("INSPECT_LIST",list);

				com.anbtech.qc.business.QualityCtrlLinkUrlBO redirectBO = new com.anbtech.qc.business.QualityCtrlLinkUrlBO(con);
				com.anbtech.qc.entity.QualityCtrlLinkUrl redirect = new com.anbtech.qc.entity.QualityCtrlLinkUrl();
				redirect = redirectBO.getLinkForList(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);

				String url = "";
				if(mode.equals("list_inspect")) url = "/qc/inspect/list.jsp?mode="+mode;
				else if(mode.equals("list_result")) url = "/qc/result/list.jsp?mode="+mode;
				else if(mode.equals("list_return")) url = "/qc/return/list.jsp?mode="+mode;
				else if(mode.equals("list_rework")) url = "/qc/rework/list.jsp?mode="+mode;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			///////////////////////////////////////
			// �˻��� �ۼ�,���� �� ���� ��
			///////////////////////////////////////
			else if(mode.equals("write_inspect") || mode.equals("view_result") || mode.equals("view_result_p") || mode.equals("write_return") || mode.equals("write_rework")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.qc.entity.InspectionMasterTable table = new com.anbtech.qc.entity.InspectionMasterTable();

				table = qcBO.getInspectionResultWriteForm(mode,request_no,item_code,login_id);
				request.setAttribute("ITEM_INFO",table);

				//���õ� ǰ�� ���ǵ� �˻��׸� ��������
				//�������� ���� ��쿡�� ���� �����Ͽ� �����´�.
				ArrayList list = new ArrayList();
				list = qcBO.getInspectionItemByItem(mode,request_no,item_code,login_id);
				request.setAttribute("INSPECTION_ITEM_LIST",list);

				//��ũ���ڿ� ��������
				com.anbtech.qc.business.QualityCtrlLinkUrlBO redirectBO = new com.anbtech.qc.business.QualityCtrlLinkUrlBO(con);
				com.anbtech.qc.entity.QualityCtrlLinkUrl redirect = new com.anbtech.qc.entity.QualityCtrlLinkUrl();
				redirect = redirectBO.getLinkForView(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);
				

				String url = "";
				if(mode.equals("write_inspect")) url = "/qc/inspect/write.jsp?mode="+mode;
				else if(mode.equals("view_result")) url = "/qc/result/view.jsp?mode="+mode;
				else if(mode.equals("view_result_p")) url = "/qc/result/view_p.jsp?mode="+mode;
				else if(mode.equals("write_return")) url = "/qc/return/write.jsp?mode="+mode;
				else if(mode.equals("write_rework")) url = "/qc/rework/write.jsp?mode="+mode;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			///////////////////////////////////////
			// �˻系�� ��� �� ���� ��
			///////////////////////////////////////
			else if(mode.equals("write_inspection_value") || mode.equals("view_inspection_value")){
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);

				ArrayList list = new ArrayList();
				list = qcBO.getInspectionValueList(request_no,item_code,inspection_code,sampled_quantity);
				request.setAttribute("INSPECTION_VALUE_LIST",list);
	
				String url = "";
				if(mode.equals("write_inspection_value")){
					url = "/qc/inspect/write_inspection_value.jsp?mode="+mode+"&request_no="+request_no+"&item_code="+item_code+"&inspection_code="+inspection_code+"&sampled_quantity="+sampled_quantity;
				}else if(mode.equals("view_inspection_value")){
					url = "/qc/result/view_inspection_value.jsp?mode="+mode+"&request_no="+request_no+"&item_code="+item_code+"&inspection_code="+inspection_code+"&sampled_quantity="+sampled_quantity;				
				}

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			///////////////////////////////////////
			// ���հ����� �Է�,����,���� ��
			///////////////////////////////////////
			else if(mode.equals("write_failure_info") || mode.equals("modify_failure_info") || mode.equals("view_failure_info")){
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.entity.FailureInfoTable table = new com.anbtech.qc.entity.FailureInfoTable();

				//�ҷ��׸� ���� ������ �����´�.
				table = qcDAO.getFailureInfo(request_no,item_code,serial_no,inspection_code);
				request.setAttribute("FAILURE_INFO",table);

				//���õ� ǰ�� �����ϴ� �ҷ�����Ʈ�� �����´�.
				ArrayList list = new ArrayList();
				list = qcDAO.getFailureList(request_no,item_code);
				request.setAttribute("FAILURE_LIST",list);
	
				String url = "";
				if(mode.equals("view_failure_info")) url = "/qc/failure/view_failure_info.jsp?mode="+mode+"&request_no="+request_no+"&item_code="+item_code+"&bad_quantity="+bad_quantity;
				else url = "/qc/failure/write_failure_info.jsp?mode="+mode+"&request_no="+request_no+"&item_code="+item_code+"&bad_quantity="+bad_quantity+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			///////////////////////////////////////
			// ���հ����� ����ó��
			///////////////////////////////////////
			else if(mode.equals("delete_failure_info")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				qcDAO.deleteFailureInfo(request_no,item_code,inspection_code);

				redirectUrl = "QualityCtrlServlet?mode=write_failure_info&request_no="+request_no+"&item_code="+item_code+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e;
			}

			///////////////////////////////////////
			// ��ǰ�� �ø��� ��ȣ ��������
			///////////////////////////////////////
			else if(mode.equals("get_serial_no")){
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);

				String[] goods_serial = new String[2];
				goods_serial = qcBO.getGoodsSerialNo(produce_year,produce_month,item_code,lot_quantity);
				request.setAttribute("SERIAL_NO",goods_serial);

				getServletContext().getRequestDispatcher("/qc/inspect/get_serial_no.jsp").forward(request,response);
			}

			////////////////////////////////////////////////////
			// ǰ���˻��� ��ȸ
			///////////////////////////////////////////////////
			else if(mode.equals("failure_info")){
				com.anbtech.qc.business.QualityCtrlBO qcBO	= new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.qc.db.QualityCtrlDAO qcDAO		= new com.anbtech.qc.db.QualityCtrlDAO(con);
				ArrayList failure_list = new ArrayList();

				//���� ����� ���ó�¥�� �˻�� ������ �˻��Ѵ�.
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
				if(searchword.equals("")) searchword = "inspect_date|" + vans.format(now) + vans.format(now);

				failure_list = qcDAO.getFailureInfoList(mode,searchword,searchscope,page,login_id);
				request.setAttribute("FAILURE_LIST",failure_list);

				getServletContext().getRequestDispatcher("/qc/failure/failure_list.jsp?mode="+mode).forward(request,response);
			}


			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			//������� �������� �б�
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

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/qc/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//�⺻ �Ķ����
		String no					= multi.getParameter("no");
		String mode					= multi.getParameter("mode");
		String page					= multi.getParameter("page");
		String searchword			= multi.getParameter("searchword");
		String searchscope			= multi.getParameter("searchscope");
		String category				= multi.getParameter("category");
		String module				= multi.getParameter("module");

		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

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

		//������ �Ķ���͵�
		String request_no			= multi.getParameter("request_no");
		String item_code			= multi.getParameter("item_code");
		String item_name			= multi.getParameter("item_name");
		String item_desc			= multi.getParameter("item_desc");
		String inspected_quantity	= multi.getParameter("inspected_quantity");
		String bad_item_quantity	= multi.getParameter("bad_item_quantity");
		String inspection_result	= multi.getParameter("inspection_result");
		String bad_percentage		= multi.getParameter("bad_percentage");
		String other_info			= multi.getParameter("other_info");
		String factory_code			= multi.getParameter("factory_code");
		String factory_name			= multi.getParameter("factory_name");
		String lot_quantity			= multi.getParameter("lot_quantity");
		String lot_no				= multi.getParameter("lot_no");
		String inspection_items		= multi.getParameter("inspection_items");	//�˻��׸��ڵ��,�޸��� ����
		String item_type			= multi.getParameter("item_type");
		String request_date			= multi.getParameter("request_date");

		//�˻系�� ��Ͻ� �߰��� �Ķ���͵�
		String inspection_code		= multi.getParameter("inspection_code");	//�˻��׸��ڵ�
		String inspection_name		= multi.getParameter("inspection_name");	//�˻��׸��

		//�ҷ����� ��Ͻ� �߰��� �Ķ���͵�
		String serial_no			= multi.getParameter("serial_no");
		String serial_no_s			= multi.getParameter("serial_no_s");
		String serial_no_e			= multi.getParameter("serial_no_e");
		String why_failure			= multi.getParameter("why_failure");

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			///////////////////////////////////////
			// �˻��� ���ó��
			///////////////////////////////////////
			if(mode.equals("write_inspect")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.pu.db.PurchaseMgrDAO puDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
				
					//�˻��� ����
					qcDAO.updInspectionInfo(request_no,item_code,inspected_quantity,bad_item_quantity,inspection_result,bad_percentage,other_info,login_id,serial_no_s,serial_no_e,lot_no);

					//�˻��� ǰ���� ǰ������ڵ带 �����´�. ��ǰ(M,1,2)�ΰ��� ������(4,5,6)�� ��쿡
					//ǰ���˻�ó�������� �޶�����.
					//
					//M  ����ǰ(��ü����) 
					//1  ����ǰ(�系����) 
					//2  ����ǰ(���ֻ���) 
					//4  ������(��ǰ) 
					//5  ������(��ǰ) 
					//6  ������(�κе���) 
					String item_typs_s = "";
					if(item_type.equals("M") || item_type.equals("1") || item_type.equals("2")) item_typs_s = "GOODS";
					else if(item_type.equals("4") || item_type.equals("5") || item_type.equals("6")) item_typs_s = "COMP";


					//��ǰ�� ��쿡 �Ϸù�ȣ���� ����
					if(item_typs_s.equals("GOODS")){
						String model_code = cmDAO.getModelCode(item_code);
						String produce_year = request_date.substring(2,4);
						String produce_month = request_date.substring(5,7);

						String no_s = serial_no_s.substring(serial_no_s.length()-4);
						String no_e = serial_no_e.substring(serial_no_e.length()-4);

						qcDAO.saveGoodsSerialInfo(produce_year,produce_month,item_code,model_code,no_s,no_e,request_no);
					}
					
					////////////////////////////////////////////////////
					//�������̰� �˻����� �հ� �Ǵ� Ưä�� ���
					////////////////////////////////////////////////////
					if(item_typs_s.equals("COMP") && (inspection_result.equals("PASS") || inspection_result.equals("PASS2"))){
						//���DB�� �԰������� ������Ʈ(�˻������ŭ �԰����������� ���ش�.)
						stBO.updateIntoQuantityWhenInspectionFinished(item_code,"-"+lot_quantity,factory_code,"","","");

						//���DB�� �������� ������Ʈ(�հݵ� ������ŭ ���������� �����ش�.)
						int stock_quantity = Integer.parseInt(lot_quantity) - Integer.parseInt(bad_item_quantity);
						stBO.updateStockQuantity(item_code,Integer.toString(stock_quantity),factory_code,"","","");

						//���DB�� �ҷ�ǰ���� ������Ʈ
						//stBO.updateBadQuantity(item_code,bad_item_quantity,factory_code,"","","");					

						//�԰�ǰ����� �����ڵ带 �԰�Ϸ�(S25)�� �ٲپ��ش�.
						puDAO.updateProcessStat("pu_entered_item","enter_no",lot_no,item_code,"S25");

						//���ֹ�ȣ �� �԰��ȣ�� �ľ��ϱ� ���� �԰�ǰ�� ������ �����´�.
						com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
						table = puDAO.getEnterItemInfo(lot_no,item_code);

						//����ǰ����� �����ڵ带 �԰�Ϸ�(S25)�� �ٲپ��ش�.
						puDAO.updateProcessStat("pu_order_item","order_no",table.getOrderNo(),item_code,"S25");
						
						//���ſ�ûǰ����� �����ڵ带 �԰�Ϸ�(S25)�� �ٲپ��ش�.
						puDAO.updateProcessStat("pu_requested_item","request_no",table.getRequestNo(),item_code,"S25");


						//���հݵ� ������ ��ǰó���ϱ� ���� �������(�������:��ǰ)�� ����Ѵ�.
						//�������� ��� ������Ȳ�� �����԰� ������ �ֱ� ������ �԰���� ���� �ҷ�������ŭ��
						//��ǰ���� ����� ��� �Ѵ�.
						if(Double.parseDouble(bad_item_quantity) > 0){
							//(1)����������ȣ ���
							String inout_no = stDAO.getEtcInOutNo("OUT");
							//(2)���ݾ� ���(�ҷ�����*ǰ��ܰ�)
							String total_mount = Double.toString(Double.parseDouble(bad_item_quantity)*Double.parseDouble(table.getUnitCost()));
							//(3)�������
							java.util.Date now = new java.util.Date();
							java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
							String w_time = vans.format(now);
							//(4)����������� ����
							stDAO.saveEtcInOutInfo("OUT",inout_no,w_time,total_mount,"KRW","RO","","",inout_no.substring(4,6),inout_no.substring(6,8),inout_no.substring(9,12),"","",login_id,"");
							//(5)�������ǰ�� ����
							stDAO.saveEtcInOutItemInfo(inout_no,item_code,table.getItemName(),table.getItemDesc(),bad_item_quantity,table.getEnterUnit(),total_mount,table.getUnitCost(),table.getFactoryCode(),table.getFactoryName(),table.getWarehouseCode(),table.getWarehouseName(),"","");

							//(6)������Ȳ�� ��ǰ������� ����
							stDAO.addInOutInfo("EO",w_time,inout_no,item_code,table.getItemName(),table.getItemDesc(),item_type,"-" + bad_item_quantity,table.getEnterUnit(),table.getUnitCost(),total_mount,table.getFactoryCode(),table.getFactoryName(),table.getWarehouseCode(),table.getWarehouseName(),"0","0");
						}
					}

					////////////////////////////////////////////////////
					//��ǰ�̰� �˻����� �հ��� ���
					////////////////////////////////////////////////////
					if(item_typs_s.equals("GOODS") && inspection_result.equals("PASS")){
						//���DB�� �������� ������Ʈ(�հݵ� ������ŭ ���������� �����ش�.)
						int stock_quantity = Integer.parseInt(lot_quantity) - Integer.parseInt(bad_item_quantity);
						stBO.updateStockQuantity(item_code,Integer.toString(stock_quantity),factory_code,factory_name,"","");

						//���հ� �������� 0���� ū ���
						//��ǰ �� ���� ��˻縦 ���� �� �԰�ǰ���� ���հ� ������ŭ ������Ƿ�ǰ��(�����ڵ�:S07)���� ���� ����Ѵ�.
						if(Double.parseDouble(bad_item_quantity) > 0) qcBO.saveReworkItemInfo(request_no,item_code,bad_item_quantity,serial_no_s,serial_no_e);

						///////////////////////////////////
						//������Ȳ�� �����԰�(MI)���� ����
						///////////////////////////////////
						//addInOutInfo(String inout_type,String inout_date,String ref_no,String item_code,String item_name,String item_desc,String item_type,String quantity,String unit,String unit_cost,String total_cost,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String stock_unit_cost,String stock_quantity)

						//��������
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String w_time = vans.format(now);

						//������ȣ�� �ش��ϴ� ���� ��������
						com.anbtech.mm.entity.mfgMasterTable table = new com.anbtech.mm.entity.mfgMasterTable();
						com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);
						table = mfgDAO.getMfgMasterItem(lot_no,factory_code);
						stDAO.addInOutInfo("MI",w_time,lot_no,item_code,item_name,item_desc,item_type,Integer.toString(stock_quantity),table.getItemUnit(),"0","0",factory_code,factory_name,"","","0","0");
						//������� �������� ����
					}

					////////////////////////////////////////////////
					//�������̰� �˻����� ��ǰ�� ���
					////////////////////////////////////////////////
					else if(item_typs_s.equals("COMP") && inspection_result.equals("RETURN")){
						//���DB�� �԰������� ������Ʈ,�԰����������� LOT ������ŭ�� ����. 
						stBO.updateIntoQuantityWhenInspectionFinished(item_code,"-"+lot_quantity,factory_code,"","","");
						
						//�԰�ǰ����� �����ڵ带 �԰�Ϸ�(S25)�� �ٲپ��ش�. ���հ��̴��� �԰�� �� ���̹Ƿ�...
						puDAO.updateProcessStat("pu_entered_item","enter_no",lot_no,item_code,"S25");

						//���ֹ�ȣ �� �԰��ȣ�� �ľ��ϱ� ���� �԰�ǰ�� ������ �����´�.
						com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
						table = puDAO.getEnterItemInfo(lot_no,item_code);

						//����ǰ����� �����ڵ带 �԰�Ϸ�(S25)�� �ٲپ��ش�.
						puDAO.updateProcessStat("pu_order_item","order_no",table.getOrderNo(),item_code,"S25");
						
						//���ſ�ûǰ����� �����ڵ带 �԰�Ϸ�(S25)�� �ٲپ��ش�.
						puDAO.updateProcessStat("pu_requested_item","request_no",table.getRequestNo(),item_code,"S25");

						//�˻���� ��θ� ��ǰó���ϱ� ���� �������(�������:��ǰ)�� ����Ѵ�.
						//
						//(1)����������ȣ ���
						String inout_no = stDAO.getEtcInOutNo("OUT");
						//(2)���ݾ� ���(�ҷ�����*ǰ��ܰ�)
						String total_mount = Double.toString(Double.parseDouble(lot_quantity)*Double.parseDouble(table.getUnitCost()));
						//(3)�������
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String w_time = vans.format(now);
						//(4)����������� ����
						stDAO.saveEtcInOutInfo("OUT",inout_no,w_time,total_mount,"KRW","RO","","",inout_no.substring(4,6),inout_no.substring(6,8),inout_no.substring(9,12),"","",login_id,"");
						//(5)�������ǰ�� ����
						stDAO.saveEtcInOutItemInfo(inout_no,item_code,table.getItemName(),table.getItemDesc(),lot_quantity,table.getEnterUnit(),total_mount,table.getUnitCost(),table.getFactoryCode(),table.getFactoryName(),table.getWarehouseCode(),table.getWarehouseName(),"","");

						//(6)������Ȳ�� ��ǰ������� ����
						stDAO.addInOutInfo("EO",w_time,inout_no,item_code,table.getItemName(),table.getItemDesc(),item_type,"-" + lot_quantity,table.getEnterUnit(),table.getUnitCost(),total_mount,table.getFactoryCode(),table.getFactoryName(),table.getWarehouseCode(),table.getWarehouseName(),"0","0");
						//������� ������� �� ������Ȳ ���
					}

					////////////////////////////////////////////////
					//��ǰ�̰� �˻����� ���۾��� ���
					////////////////////////////////////////////////
					else if(item_typs_s.equals("GOODS") && inspection_result.equals("REWORK")){
						//��ǰ �� ���� ��˻縦 ���� �� �԰�ǰ���� ���հ� ������ŭ ��˻��Ƿ�ǰ��(�����ڵ�:S07)���� ���� ����Ѵ�.
						qcBO.saveReworkItemInfo(request_no,item_code,lot_quantity,serial_no_s,serial_no_e);
					}

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "QualityCtrlServlet?mode=list_inspect";
			}

			///////////////////////////////////////
			// ���۾���� ���ó��
			///////////////////////////////////////
			else if(mode.equals("write_rework")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);

				//���۾��������
				qcDAO.updInspectionInfo(request_no,item_code,other_info,login_id,lot_no);

				redirectUrl = "QualityCtrlServlet?mode=list_rework";
			}

			///////////////////////////////////////
			// ��˻��� ���ó��
			///////////////////////////////////////
			else if(mode.equals("write_return")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.pu.db.PurchaseMgrDAO puDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
				
					//�˻��� ����
					qcDAO.updInspectionInfo(request_no,item_code,inspected_quantity,bad_item_quantity,inspection_result,bad_percentage,other_info,login_id,serial_no_s,serial_no_e,lot_no);
					
					//ǰ����� ��������
					String item_typs_s = "";
					if(item_type.equals("M") || item_type.equals("1") || item_type.equals("2")) item_typs_s = "GOODS";
					else if(item_type.equals("4") || item_type.equals("5") || item_type.equals("6")) item_typs_s = "COMP";

					///////////////////////////////////////////////////
					//��ǰ�̰� �˻����� �հ��� ���
					///////////////////////////////////////////////////
					if(item_typs_s.equals("GOODS") && inspection_result.equals("PASS")){
						//���DB�� �������� ������Ʈ(�հݵ� ������ŭ ���������� �����ش�.)
						int stock_quantity = Integer.parseInt(lot_quantity) - Integer.parseInt(bad_item_quantity);
						stBO.updateStockQuantity(item_code,Integer.toString(stock_quantity),factory_code,"","","");

						//���հ� �������� 0���� ū ���
						//��ǰ �� ���� ��˻縦 ���� �� �԰�ǰ���� ���հ� ������ŭ ��˻��Ƿ�ǰ��(�����ڵ�:S07)���� ���� ����Ѵ�.
						if(Double.parseDouble(bad_item_quantity) > 0) qcBO.saveReworkItemInfo(request_no,item_code,bad_item_quantity,serial_no_s,serial_no_e);

						///////////////////////////////////
						//������Ȳ�� �����԰�(MI)���� ����
						///////////////////////////////////
						//��������
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String w_time = vans.format(now);

						//������ȣ�� �ش��ϴ� ���� ��������
						com.anbtech.mm.entity.mfgMasterTable table = new com.anbtech.mm.entity.mfgMasterTable();
						com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);
						table = mfgDAO.getMfgMasterItem(lot_no,factory_code);
						stDAO.addInOutInfo("MI",w_time,lot_no,item_code,item_name,item_desc,item_type,Integer.toString(stock_quantity),table.getItemUnit(),"0","0",factory_code,factory_name,"","","0","0");
						//������� �������� ����
					}

					///////////////////////////////////////////////
					//��ǰ�̰� �˻��� �Ǵٽ� ���۾��� ���
					///////////////////////////////////////////////
					else if(item_typs_s.equals("GOODS") && inspection_result.equals("REWORK")){
						//��ǰ �� ���� ��˻縦 ���� �� �԰�ǰ���� ���հ� ������ŭ ��˻��Ƿ�ǰ��(�����ڵ�:S07)���� ���� ����Ѵ�.
						qcBO.saveReworkItemInfo(request_no,item_code,lot_quantity,serial_no_s,serial_no_e);
					}

					///////////////////////////////////////////////
					//��ǰ�̰� �˻��� ���ó���� ���
					///////////////////////////////////////////////
					else if(item_typs_s.equals("GOODS") && inspection_result.equals("FAIL")){

					}

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "QualityCtrlServlet?mode=list_return";
			}
			
			///////////////////////////////////////
			// �˻系�� ��� ó��
			///////////////////////////////////////
			else if(mode.equals("write_inspection_value")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//���� �˻系�������� ���� �����Ѵ�.(qc_inspection_result ���̺�)
					qcDAO.deleteInspectionValue(request_no,item_code,inspection_code);

					//�˻系������ ����
					int good_quantity = 0;	//�հݼ���
					int bad_quantity = 0;	//���հݼ���
					String value_str = "";
					for(int k=1; k<=Integer.parseInt(inspected_quantity); k++){
						String inspection_value = multi.getParameter("inspection_value_" + k);
						String is_passed		= multi.getParameter("is_passed_" + k);
						value_str += inspection_value + "|" + is_passed + "#";

						if(is_passed.equals("Y")) good_quantity++;
						else bad_quantity++;
					}
					qcDAO.saveInspectionValue(request_no,item_code,inspection_code,value_str);

					//���� �˻��������� ���� �����Ѵ�.(qc_inspection_value ���̺�)
					qcDAO.deleteInspectionResult(request_no,item_code,inspection_code);
					//�˻��������� �����Ѵ�.
					qcDAO.saveInspectionResult(request_no,item_code,inspection_code,inspected_quantity,Integer.toString(good_quantity),Integer.toString(bad_quantity));

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				getServletContext().getRequestDispatcher("/qc/inspect/saved_result.jsp").forward(request,response);
			}

			///////////////////////////////////////
			// �ҷ��������� �߰�ó��
			///////////////////////////////////////
			else if(mode.equals("write_failure_info")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.entity.InspectionItemTable table = new com.anbtech.qc.entity.InspectionItemTable();

				//�Էµ� �˻��׸��ڵ忡 ���� �˻��׸���� �����´�. ������ ���� �޽��� ���
				table = qcDAO.getInspectionItemInfo(inspection_code);
				inspection_name = table.getInspectionName();

				if(inspection_name == null){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('�������� �ʴ� �˻��׸��ڵ��Դϴ�. �ڵ带 Ȯ���Ͻʽÿ�.');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}
				
				qcDAO.saveFailureInfo(request_no,item_code,serial_no,inspection_code,inspection_name,why_failure);

				redirectUrl = "QualityCtrlServlet?mode=write_failure_info&request_no="+request_no+"&item_code="+item_code+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e;
			}

			///////////////////////////////////////
			// �ҷ��������� ����ó��
			///////////////////////////////////////
			else if(mode.equals("modify_failure_info")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				qcDAO.updateFailureInfo(request_no,item_code,serial_no,inspection_code,inspection_name,why_failure);

				redirectUrl = "QualityCtrlServlet?mode=write_failure_info&request_no="+request_no+"&item_code="+item_code+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e;
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
