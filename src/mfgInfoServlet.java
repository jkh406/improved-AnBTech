import com.anbtech.mm.db.*;
import com.anbtech.mm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class mfgInfoServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 15;
	private int max_display_page = 5;

	/********
	 * �Ҹ���
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó�� (��Ϻ���)
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//�⺻�Ķ����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"mrp_request":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MFG MASTER������ ���� �⺻�Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String mrp_no = Hanguel.toHanguel(request.getParameter("mrp_no"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_no"));
		String mfg_no = Hanguel.toHanguel(request.getParameter("mfg_no"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_no"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String item_name = Hanguel.toHanguel(request.getParameter("item_name"))==null?"":Hanguel.toHanguel(request.getParameter("item_name"));
		String item_spec = Hanguel.toHanguel(request.getParameter("item_spec"))==null?"":Hanguel.toHanguel(request.getParameter("item_spec"));
		String item_unit = Hanguel.toHanguel(request.getParameter("item_unit"))==null?"":Hanguel.toHanguel(request.getParameter("item_unit"));
		String mfg_count = Hanguel.toHanguel(request.getParameter("mfg_count"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_count"));
		String buy_type = Hanguel.toHanguel(request.getParameter("buy_type"))==null?"":Hanguel.toHanguel(request.getParameter("buy_type"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		String factory_name = Hanguel.toHanguel(request.getParameter("factory_name"))==null?"":Hanguel.toHanguel(request.getParameter("factory_name"));
		String comp_code = Hanguel.toHanguel(request.getParameter("comp_code"))==null?"":Hanguel.toHanguel(request.getParameter("comp_code"));
		String comp_name = Hanguel.toHanguel(request.getParameter("comp_name"))==null?"":Hanguel.toHanguel(request.getParameter("comp_name"));
		String comp_user = Hanguel.toHanguel(request.getParameter("comp_user"))==null?"":Hanguel.toHanguel(request.getParameter("comp_user"));
		String comp_tel = Hanguel.toHanguel(request.getParameter("comp_tel"))==null?"":Hanguel.toHanguel(request.getParameter("comp_tel"));
		String order_status = Hanguel.toHanguel(request.getParameter("order_status"))==null?"":Hanguel.toHanguel(request.getParameter("order_status"));
		String order_type = Hanguel.toHanguel(request.getParameter("order_type"))==null?"":Hanguel.toHanguel(request.getParameter("order_type"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String reg_id = Hanguel.toHanguel(request.getParameter("reg_id"))==null?"":Hanguel.toHanguel(request.getParameter("reg_id"));
		String reg_name = Hanguel.toHanguel(request.getParameter("reg_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_name"));
		String plan_date = Hanguel.toHanguel(request.getParameter("plan_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_date"));
		String order_start_date = Hanguel.toHanguel(request.getParameter("order_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_start_date"));
		String order_end_date = Hanguel.toHanguel(request.getParameter("order_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_end_date"));
		String op_start_date = Hanguel.toHanguel(request.getParameter("op_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("op_start_date"));
		String op_end_date = Hanguel.toHanguel(request.getParameter("op_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("op_end_date"));
		String re_work = Hanguel.toHanguel(request.getParameter("re_work"))==null?"":Hanguel.toHanguel(request.getParameter("re_work"));
		String link_mfg_no = Hanguel.toHanguel(request.getParameter("link_mfg_no"))==null?"":Hanguel.toHanguel(request.getParameter("link_mfg_no"));
		String rst_total_count = Hanguel.toHanguel(request.getParameter("rst_total_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_total_count"));
		String rst_good_count = Hanguel.toHanguel(request.getParameter("rst_good_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_good_count"));
		String rst_bad_count = Hanguel.toHanguel(request.getParameter("rst_bad_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_bad_count"));
		String working_count = Hanguel.toHanguel(request.getParameter("working_count"))==null?"":Hanguel.toHanguel(request.getParameter("working_count"));
		String rst_pass_count = Hanguel.toHanguel(request.getParameter("rst_pass_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_pass_count"));
		String rst_fail_count = Hanguel.toHanguel(request.getParameter("rst_fail_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_fail_count"));
		
		//MFG ITEM ������ ���� �Ķ���� �߰�
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String assy_code = Hanguel.toHanguel(request.getParameter("assy_code"))==null?"":Hanguel.toHanguel(request.getParameter("assy_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
		String item_type = Hanguel.toHanguel(request.getParameter("item_type"))==null?"":Hanguel.toHanguel(request.getParameter("item_type"));
		String item_loss = Hanguel.toHanguel(request.getParameter("item_loss"))==null?"":Hanguel.toHanguel(request.getParameter("item_loss"));
		String draw_count = Hanguel.toHanguel(request.getParameter("draw_count"))==null?"":Hanguel.toHanguel(request.getParameter("draw_count"));
		String need_count = Hanguel.toHanguel(request.getParameter("need_count"))==null?"":Hanguel.toHanguel(request.getParameter("need_count"));
		String item_count = Hanguel.toHanguel(request.getParameter("item_count"))==null?"":Hanguel.toHanguel(request.getParameter("item_count"));
		String spare_count = Hanguel.toHanguel(request.getParameter("spare_count"))==null?"":Hanguel.toHanguel(request.getParameter("spare_count"));
		String add_count = Hanguel.toHanguel(request.getParameter("add_count"))==null?"":Hanguel.toHanguel(request.getParameter("add_count"));
		String reserve_count = Hanguel.toHanguel(request.getParameter("reserve_count"))==null?"":Hanguel.toHanguel(request.getParameter("reserve_count"));
		String request_count = Hanguel.toHanguel(request.getParameter("request_count"))==null?"":Hanguel.toHanguel(request.getParameter("request_count"));
		String need_date = Hanguel.toHanguel(request.getParameter("need_date"))==null?"":Hanguel.toHanguel(request.getParameter("need_date"));
		String order_date = Hanguel.toHanguel(request.getParameter("order_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_date"));
		
		//MFG OPERATOR ������ ���� �Ķ���� �߰�
		String mfg_unit = Hanguel.toHanguel(request.getParameter("mfg_unit"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_unit"));
		String work_no = Hanguel.toHanguel(request.getParameter("work_no"))==null?"":Hanguel.toHanguel(request.getParameter("work_no"));
		String work_name = Hanguel.toHanguel(request.getParameter("work_name"))==null?"":Hanguel.toHanguel(request.getParameter("work_name"));
		String op_no = Hanguel.toHanguel(request.getParameter("op_no"))==null?"":Hanguel.toHanguel(request.getParameter("op_no"));
		String op_name = Hanguel.toHanguel(request.getParameter("op_name"))==null?"":Hanguel.toHanguel(request.getParameter("op_name"));
		String mfg_id = Hanguel.toHanguel(request.getParameter("mfg_id"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_id"));
		String mfg_name = Hanguel.toHanguel(request.getParameter("mfg_name"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_name"));
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"":Hanguel.toHanguel(request.getParameter("note"));
		
		//MFG ORDER ������ ���� �Ķ���� �߰�
		String work_order_no = Hanguel.toHanguel(request.getParameter("work_order_no"))==null?"":Hanguel.toHanguel(request.getParameter("work_order_no"));
		String order_count = Hanguel.toHanguel(request.getParameter("order_count"))==null?"":Hanguel.toHanguel(request.getParameter("order_count"));
		String mfg_type = Hanguel.toHanguel(request.getParameter("mfg_type"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_type"));
		String work_order_status = Hanguel.toHanguel(request.getParameter("work_order_status"))==null?"":Hanguel.toHanguel(request.getParameter("work_order_status"));
		
		//MFG PRODUCT ������ ���� �Ķ���� �߰�
		String order_unit = Hanguel.toHanguel(request.getParameter("order_unit"))==null?"":Hanguel.toHanguel(request.getParameter("order_unit"));
		String worker = Hanguel.toHanguel(request.getParameter("worker"))==null?"":Hanguel.toHanguel(request.getParameter("worker"));
		String output_date = Hanguel.toHanguel(request.getParameter("output_date"))==null?"":Hanguel.toHanguel(request.getParameter("output_date"));
		String bad_type = Hanguel.toHanguel(request.getParameter("bad_type"))==null?"":Hanguel.toHanguel(request.getParameter("bad_type"));
		String bad_note = Hanguel.toHanguel(request.getParameter("bad_note"))==null?"":Hanguel.toHanguel(request.getParameter("bad_note"));

		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		//���ڿ��� '/'�����ϱ�
		com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();
		order_start_date = prs.repWord(order_start_date,"/","");
		order_end_date = prs.repWord(order_end_date,"/","");
		op_start_date = prs.repWord(op_start_date,"/","");
		op_end_date = prs.repWord(op_end_date,"/","");
		need_date = prs.repWord(need_date,"/","");
		order_date = prs.repWord(order_date,"/","");
		output_date = prs.repWord(output_date,"/","");

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MFG MASTER ����
			//------------------------------------------------------------
			//MRP ������Ȳ ����
			if ("mfg_request".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//���� �˻��ϱ�
				String mgr = mfgDAO.checkGrade("MFG",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('��� ������ �����ϴ�. ������ Ȯ���ϰų� ������ �ο������ʽÿ�.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//��ϵ� LIST ����
				ArrayList request_list = new ArrayList();
				request_list = mfgDAO.getMfgRequestList(factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("REQUEST_List", request_list); 

				//�������� �ٷΰ��� List
				com.anbtech.mm.entity.mrpMasterTable pageL = new com.anbtech.mm.entity.mrpMasterTable();
				pageL = mfgDAO.getRequestDisplayPage(factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgRequestList.jsp").forward(request,response);
			}
			//MMG �����Ȳ ����
			else if ("mfg_list".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//���� �˻��ϱ�
				String mgr = mfgDAO.checkGrade("MFG",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('��� ������ �����ϴ�. ������ Ȯ���ϰų� ������ �ο������ʽÿ�.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//��ϵ� LIST ����
				ArrayList mfg_list = new ArrayList();
				mfg_list = mfgDAO.getMfgMasterList(factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("MFG_List", mfg_list); 

				//�������� �ٷΰ��� List
				com.anbtech.mm.entity.mfgMasterTable pageL = new com.anbtech.mm.entity.mfgMasterTable();
				pageL = mfgDAO.getMfgDisplayPage(factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgMasterList.jsp").forward(request,response);
			}
			//MFG ���/����/����/Ȯ��/��ǰ���� �غ�
			else if ("mfg_presave".equals(mode) || "mfg_preview".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//���� �˻��ϱ�
				String mgr = mfgDAO.checkGrade("MFG",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('��� ������ �����ϴ�. ������ Ȯ���ϰų� ������ �ο������ʽÿ�.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//ȭ����� ������ �б�
				com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
				if("mfg_presave".equals(mode)) {		//MRP������ ����
					mfgT = mfgDAO.readRequestMfgMasterItem(pid);
				} else {								//MFG��� LIST�� ����
					mfgT = mfgDAO.readMfgMasterItem(pid,factory_no);
				}
				request.setAttribute("MFG_master", mfgT); 

				//�Ķ���� �����ϱ�
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgMasterReg.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//		������ ���õ� ����
			//------------------------------------------------------------
			//������ǰ�� / ������ ��ȹ LIST
			else if ("order_list".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//������ ��ϵ� LIST ����
				ArrayList order_list = new ArrayList();
				order_list = mfgDAO.getMfgOpetatorList(gid);
				request.setAttribute("ORDER_List",order_list);
				
				//������ǰ ������ �б�
				com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
				mfgT = mfgDAO.readMfgMasterItem(gid,factory_no);
				request.setAttribute("MFG_master",mfgT);  

				//�Ķ���� �����ϱ�
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgOperatorList.jsp").forward(request,response);
			}
			//������ǰ�� / ������ ��ȹ LIST ���� �غ�
			else if ("order_view".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//ȭ����� ������ �б�
				com.anbtech.mm.entity.mfgOperatorTable mfgOP = new com.anbtech.mm.entity.mfgOperatorTable();
				mfgOP = mfgDAO.readMfgOperator(pid);
				request.setAttribute("MFG_operator",mfgOP); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgOperatorReg.jsp").forward(request,response);
				
			}
			//���� �۾����ÿ��� �������� �ٷΰ���
			else if ("mfg_review".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//ȭ����� ������ �б�
				com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
				mfgT = mfgDAO.readMfgMasterItem(pid,factory_no);
				request.setAttribute("MFG_master", mfgT); 

				//�Ķ���� �����ϱ�
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgMasterReg.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//		���� ��ǰ �ҿ䷮���� �� ���õ� ����
			//------------------------------------------------------------
			//���� ��ǰ �ҿ䷮���� LIST
			else if ("item_list".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//����ASSY���� LIST
				ArrayList assy_list = new ArrayList();
				assy_list = mfgDAO.getAssyList(factory_no,mfg_no);
				request.setAttribute("ASSY_List",assy_list);
				if(assy_code.length() == 0) {
					com.anbtech.mm.entity.mfgItemTable table = new com.anbtech.mm.entity.mfgItemTable();
					Iterator table_iter = assy_list.iterator();
					if(table_iter.hasNext()){
						table = (com.anbtech.mm.entity.mfgItemTable)table_iter.next();
						level_no = Integer.toString(table.getLevelNo());
						assy_code = table.getAssyCode();
					}
				}

				//������ ��ǰ LIST ����
				ArrayList item_list = new ArrayList();
				item_list = mfgDAO.getSingleMfgItemList(factory_no,mfg_no,level_no,assy_code);
				request.setAttribute("ITEM_List",item_list);
			
				//������ǰ ������ �б�
				com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
				mfgT = mfgDAO.readMfgMasterItem(gid,factory_no);
				request.setAttribute("MFG_master",mfgT);  

				//�Ķ���� �����ϱ�
				request.setAttribute("assy_code",assy_code);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgItemList.jsp").forward(request,response);
			}
			//���� ��ǰ �ҿ䷮ ���� �غ�
			else if ("item_view".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//ȭ����� ������ �б�
				com.anbtech.mm.entity.mfgItemTable mfgIT = new com.anbtech.mm.entity.mfgItemTable();
				mfgIT = mfgDAO.readMfgItem(pid);
				request.setAttribute("MFG_item",mfgIT); 

				//�Ķ���� �����ϱ�
				request.setAttribute("order_type",order_type);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgItemReg.jsp").forward(request,response);
				
			}
			

			
		}catch (Exception e){
			//������� �������� �б�
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			out.close();
		}finally{
			close(con);
		}
		
	} //doGet()

	/**********************************
	 * post������� �Ѿ���� �� ó�� 
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//�⺻�Ķ����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"mrp_request":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MFG MASTER������ ���� �⺻�Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String mrp_no = Hanguel.toHanguel(request.getParameter("mrp_no"))==null?"":Hanguel.toHanguel(request.getParameter("mrp_no"));
		String mfg_no = Hanguel.toHanguel(request.getParameter("mfg_no"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_no"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String item_name = Hanguel.toHanguel(request.getParameter("item_name"))==null?"":Hanguel.toHanguel(request.getParameter("item_name"));
		String item_spec = Hanguel.toHanguel(request.getParameter("item_spec"))==null?"":Hanguel.toHanguel(request.getParameter("item_spec"));
		String item_unit = Hanguel.toHanguel(request.getParameter("item_unit"))==null?"":Hanguel.toHanguel(request.getParameter("item_unit"));
		String mfg_count = Hanguel.toHanguel(request.getParameter("mfg_count"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_count"));
		String buy_type = Hanguel.toHanguel(request.getParameter("buy_type"))==null?"":Hanguel.toHanguel(request.getParameter("buy_type"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		String factory_name = Hanguel.toHanguel(request.getParameter("factory_name"))==null?"":Hanguel.toHanguel(request.getParameter("factory_name"));
		String comp_code = Hanguel.toHanguel(request.getParameter("comp_code"))==null?"":Hanguel.toHanguel(request.getParameter("comp_code"));
		String comp_name = Hanguel.toHanguel(request.getParameter("comp_name"))==null?"":Hanguel.toHanguel(request.getParameter("comp_name"));
		String comp_user = Hanguel.toHanguel(request.getParameter("comp_user"))==null?"":Hanguel.toHanguel(request.getParameter("comp_user"));
		String comp_tel = Hanguel.toHanguel(request.getParameter("comp_tel"))==null?"":Hanguel.toHanguel(request.getParameter("comp_tel"));
		String order_status = Hanguel.toHanguel(request.getParameter("order_status"))==null?"":Hanguel.toHanguel(request.getParameter("order_status"));
		String order_type = Hanguel.toHanguel(request.getParameter("order_type"))==null?"":Hanguel.toHanguel(request.getParameter("order_type"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String reg_id = Hanguel.toHanguel(request.getParameter("reg_id"))==null?"":Hanguel.toHanguel(request.getParameter("reg_id"));
		String reg_name = Hanguel.toHanguel(request.getParameter("reg_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_name"));
		String plan_date = Hanguel.toHanguel(request.getParameter("plan_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_date"));
		String order_start_date = Hanguel.toHanguel(request.getParameter("order_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_start_date"));
		String order_end_date = Hanguel.toHanguel(request.getParameter("order_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_end_date"));
		String op_start_date = Hanguel.toHanguel(request.getParameter("op_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("op_start_date"));
		String op_end_date = Hanguel.toHanguel(request.getParameter("op_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("op_end_date"));
		String re_work = Hanguel.toHanguel(request.getParameter("re_work"))==null?"":Hanguel.toHanguel(request.getParameter("re_work"));
		String link_mfg_no = Hanguel.toHanguel(request.getParameter("link_mfg_no"))==null?"":Hanguel.toHanguel(request.getParameter("link_mfg_no"));
		String rst_total_count = Hanguel.toHanguel(request.getParameter("rst_total_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_total_count"));
		String rst_good_count = Hanguel.toHanguel(request.getParameter("rst_good_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_good_count"));
		String rst_bad_count = Hanguel.toHanguel(request.getParameter("rst_bad_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_bad_count"));
		String working_count = Hanguel.toHanguel(request.getParameter("working_count"))==null?"":Hanguel.toHanguel(request.getParameter("working_count"));
		String rst_pass_count = Hanguel.toHanguel(request.getParameter("rst_pass_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_pass_count"));
		String rst_fail_count = Hanguel.toHanguel(request.getParameter("rst_fail_count"))==null?"":Hanguel.toHanguel(request.getParameter("rst_fail_count"));
		
		//MFG ITEM ������ ���� �Ķ���� �߰�
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String assy_code = Hanguel.toHanguel(request.getParameter("assy_code"))==null?"":Hanguel.toHanguel(request.getParameter("assy_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
		String item_type = Hanguel.toHanguel(request.getParameter("item_type"))==null?"":Hanguel.toHanguel(request.getParameter("item_type"));
		String item_loss = Hanguel.toHanguel(request.getParameter("item_loss"))==null?"":Hanguel.toHanguel(request.getParameter("item_loss"));
		String draw_count = Hanguel.toHanguel(request.getParameter("draw_count"))==null?"":Hanguel.toHanguel(request.getParameter("draw_count"));
		String need_count = Hanguel.toHanguel(request.getParameter("need_count"))==null?"":Hanguel.toHanguel(request.getParameter("need_count"));
		String item_count = Hanguel.toHanguel(request.getParameter("item_count"))==null?"":Hanguel.toHanguel(request.getParameter("item_count"));
		String spare_count = Hanguel.toHanguel(request.getParameter("spare_count"))==null?"":Hanguel.toHanguel(request.getParameter("spare_count"));
		String add_count = Hanguel.toHanguel(request.getParameter("add_count"))==null?"":Hanguel.toHanguel(request.getParameter("add_count"));
		String reserve_count = Hanguel.toHanguel(request.getParameter("reserve_count"))==null?"":Hanguel.toHanguel(request.getParameter("reserve_count"));
		String request_count = Hanguel.toHanguel(request.getParameter("request_count"))==null?"":Hanguel.toHanguel(request.getParameter("request_count"));
		String need_date = Hanguel.toHanguel(request.getParameter("need_date"))==null?"":Hanguel.toHanguel(request.getParameter("need_date"));
		String order_date = Hanguel.toHanguel(request.getParameter("order_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_date"));
		
		//MFG OPERATOR ������ ���� �Ķ���� �߰�
		String mfg_unit = Hanguel.toHanguel(request.getParameter("mfg_unit"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_unit"));
		String work_no = Hanguel.toHanguel(request.getParameter("work_no"))==null?"":Hanguel.toHanguel(request.getParameter("work_no"));
		String work_name = Hanguel.toHanguel(request.getParameter("work_name"))==null?"":Hanguel.toHanguel(request.getParameter("work_name"));
		String op_no = Hanguel.toHanguel(request.getParameter("op_no"))==null?"":Hanguel.toHanguel(request.getParameter("op_no"));
		String op_name = Hanguel.toHanguel(request.getParameter("op_name"))==null?"":Hanguel.toHanguel(request.getParameter("op_name"));
		String mfg_id = Hanguel.toHanguel(request.getParameter("mfg_id"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_id"));
		String mfg_name = Hanguel.toHanguel(request.getParameter("mfg_name"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_name"));
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"":Hanguel.toHanguel(request.getParameter("note"));
		String op_order = Hanguel.toHanguel(request.getParameter("op_order"))==null?"":Hanguel.toHanguel(request.getParameter("op_order"));
		
		//MFG ORDER ������ ���� �Ķ���� �߰�
		String work_order_no = Hanguel.toHanguel(request.getParameter("work_order_no"))==null?"":Hanguel.toHanguel(request.getParameter("work_order_no"));
		String order_count = Hanguel.toHanguel(request.getParameter("order_count"))==null?"":Hanguel.toHanguel(request.getParameter("order_count"));
		String mfg_type = Hanguel.toHanguel(request.getParameter("mfg_type"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_type"));
		String work_order_status = Hanguel.toHanguel(request.getParameter("work_order_status"))==null?"":Hanguel.toHanguel(request.getParameter("work_order_status"));
		
		//MFG PRODUCT ������ ���� �Ķ���� �߰�
		String order_unit = Hanguel.toHanguel(request.getParameter("order_unit"))==null?"":Hanguel.toHanguel(request.getParameter("order_unit"));
		String worker = Hanguel.toHanguel(request.getParameter("worker"))==null?"":Hanguel.toHanguel(request.getParameter("worker"));
		String output_date = Hanguel.toHanguel(request.getParameter("output_date"))==null?"":Hanguel.toHanguel(request.getParameter("output_date"));
		String bad_type = Hanguel.toHanguel(request.getParameter("bad_type"))==null?"":Hanguel.toHanguel(request.getParameter("bad_type"));
		String bad_note = Hanguel.toHanguel(request.getParameter("bad_note"))==null?"":Hanguel.toHanguel(request.getParameter("bad_note"));

		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		//���ڿ��� '/'�����ϱ�
		com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();
		order_start_date = prs.repWord(order_start_date,"/","");
		order_end_date = prs.repWord(order_end_date,"/","");
		op_start_date = prs.repWord(op_start_date,"/","");
		op_end_date = prs.repWord(op_end_date,"/","");
		need_date = prs.repWord(need_date,"/","");
		order_date = prs.repWord(order_date,"/","");
		output_date = prs.repWord(output_date,"/","");

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MFG MASTER ����
			//------------------------------------------------------------
			//MRP ������Ȳ ����
			if ("mfg_request".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//���� �˻��ϱ�
				String mgr = mfgDAO.checkGrade("MFG",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('��� ������ �����ϴ�. ������ Ȯ���ϰų� ������ �ο������ʽÿ�.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//��ϵ� LIST ����
				ArrayList request_list = new ArrayList();
				request_list = mfgDAO.getMfgRequestList(factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("REQUEST_List", request_list); 

				//�������� �ٷΰ��� List
				com.anbtech.mm.entity.mrpMasterTable pageL = new com.anbtech.mm.entity.mrpMasterTable();
				pageL = mfgDAO.getRequestDisplayPage(factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgRequestList.jsp").forward(request,response);
			}
			//MMG �����Ȳ ����
			else if ("mfg_list".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//���� �˻��ϱ�
				String mgr = mfgDAO.checkGrade("MFG",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('��� ������ �����ϴ�. ������ Ȯ���ϰų� ������ �ο������ʽÿ�.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//��ϵ� LIST ����
				ArrayList mfg_list = new ArrayList();
				mfg_list = mfgDAO.getMfgMasterList(factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("MFG_List", mfg_list); 

				//�������� �ٷΰ��� List
				com.anbtech.mm.entity.mfgMasterTable pageL = new com.anbtech.mm.entity.mfgMasterTable();
				pageL = mfgDAO.getMfgDisplayPage(factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgMasterList.jsp").forward(request,response);
			}
			//MFG ���/����/����/Ȯ��/��ǰ���� �غ�
			else if ("mfg_presave".equals(mode) || "mfg_preview".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//���� �˻��ϱ�
				String mgr = mfgDAO.checkGrade("MFG",login_id,factory_no);
				if(mgr.length() == 0) {
					out.println("	<script>");
					out.println("	alert('��� ������ �����ϴ�. ������ Ȯ���ϰų� ������ �ο������ʽÿ�.');");
					out.println("	parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				//ȭ����� ������ �б�
				com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
				if("mfg_presave".equals(mode)) {		//MRP������ ����
					mfgT = mfgDAO.readRequestMfgMasterItem(pid);
				} else {								//MFG��� LIST�� ����
					mfgT = mfgDAO.readMfgMasterItem(pid,factory_no);
				}
				request.setAttribute("MFG_master", mfgT); 

				//�Ķ���� �����ϱ�
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgMasterReg.jsp").forward(request,response);
				
			}
			//MFG MASTER ���/����/����
			else if("mfg_save".equals(mode) || "mfg_modify".equals(mode) || "mfg_delete".equals(mode)) {
				com.anbtech.mm.business.mfgInputBO mfgBO = new com.anbtech.mm.business.mfgInputBO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//����ϱ�
					if("mfg_save".equals(mode)) {
						msg = mfgBO.insertMfg(mrp_no,model_code,model_name,fg_code,item_code,item_name,item_spec,item_unit,mfg_count,buy_type,factory_no,factory_name,comp_code,comp_name,comp_user,comp_tel,order_type,reg_date,reg_id,reg_name,plan_date,order_start_date,order_end_date,re_work,link_mfg_no);
					} 
					//�����ϱ�
					else if ("mfg_modify".equals(mode)) {
						msg = mfgBO.updateMfg(pid,plan_date,model_code,model_name,fg_code,item_code,item_name,item_spec,item_unit,mfg_count,buy_type,factory_no,factory_name,comp_code,comp_name,comp_user,comp_tel,order_type,reg_date,order_start_date,order_end_date,re_work,link_mfg_no);
					}
					//�����ϱ�
					else if("mfg_delete".equals(mode)) {
						msg = mfgBO.deleteMfg(pid,mrp_no,order_status);
					}
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��� �Ķ���� �����
					String para = "mfg_list&sItem=fg_code&sWord=";
					para += "&factory_no="+factory_no+"&msg="+msg;
					
					//�б��ϱ�
					out.println("	<script>");
					out.println("	self.location.href('mfgInfoServlet?mode="+para+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//------------------------------------------------------------
			//		������ ���õ� ����
			//------------------------------------------------------------
			//�������� (mfg_item , mfg_opetator�� ������ ���)
			else if("order_create".equals(mode)) {
				com.anbtech.mm.business.mfgInputBO mfgBO = new com.anbtech.mm.business.mfgInputBO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					msg = mfgBO.setMfgStatus(login_id,login_name,pid,mrp_no,mfg_no,item_code,item_unit,fg_code,mfg_count,order_type,order_status,order_start_date,order_end_date,factory_no,factory_name,re_work);
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��� �Ķ���� �����
					String para = "mfg_preview&pid="+pid+"&msg="+msg+"&factory_no="+factory_no;
					
					//�б��ϱ�
					out.println("	<script>");
					out.println("	self.location.href('mfgInfoServlet?mode="+para+"');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//������ǰ�� / ������ ��ȹ LIST
			else if ("order_list".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//������ ��ϵ� LIST ����
				ArrayList order_list = new ArrayList();
				order_list = mfgDAO.getMfgOpetatorList(gid);
				request.setAttribute("ORDER_List",order_list);
				
				//������ǰ ������ �б�
				com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
				mfgT = mfgDAO.readMfgMasterItem(gid,factory_no);
				request.setAttribute("MFG_master",mfgT); 

				//�Ķ���� �����ϱ�
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgOperatorList.jsp").forward(request,response);
			}
			//������ǰ�� / ������ ��ȹ LIST ���� �غ�
			else if ("order_view".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//ȭ����� ������ �б�
				com.anbtech.mm.entity.mfgOperatorTable mfgOP = new com.anbtech.mm.entity.mfgOperatorTable();
				mfgOP = mfgDAO.readMfgOperator(pid);
				request.setAttribute("MFG_operator",mfgOP); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgOperatorReg.jsp").forward(request,response);
			}
			//������ǰ�� / ������ ��ȹ LIST ���� �ϱ�
			else if("order_modify".equals(mode)) {
				com.anbtech.mm.business.mfgInputBO mfgBO = new com.anbtech.mm.business.mfgInputBO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					msg = mfgBO.updateMfgOperator(pid,op_start_date,op_end_date,buy_type,work_no,work_name,op_no,op_name,mfg_id,mfg_name,note,comp_code,comp_name,comp_user,comp_tel);
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��� �Ķ���� �����
					String para = "gid="+gid+"&msg="+msg;
					
					//�б��ϱ�
					out.println("	<script>");
					out.println("	parent.list.location.href('mfgInfoServlet?mode=order_list&"+para+"');");
					out.println("	</script>");

					out.println("	<script>");
					out.println("	parent.reg.location.href('mfgInfoServlet?mode=order_view&"+para+"');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//������ǰ�� / ������ ��ȹ Ȯ���ϱ�
			else if("order_act".equals(mode)) {
				com.anbtech.mm.business.mfgInputBO mfgBO = new com.anbtech.mm.business.mfgInputBO(con);
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//���� �˻��ϱ�
				String mgr = mfgDAO.checkGrade("MFG",login_id,factory_no);
				if(mgr.indexOf("MGR") == -1) {
					out.println("	<script>");
					out.println("	alert('��� ������ �����ϴ�. ������ Ȯ���ϰų� ������ �ο������ʽÿ�.');");
					out.println("	parent.parent.view.location.href('mpsInfoServlet?mode=cal_month&factory_no="+factory_no+"');");
					out.println("	</script>");
					out.close();
					return;
				}

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					msg = mfgBO.setMfgStatus(login_id,login_name,gid,mrp_no,mfg_no,item_code,item_unit,fg_code,mfg_count,order_type,"3",order_start_date,order_end_date,factory_no,factory_name,re_work);//mfg master(2 -> 3)
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��� �Ķ���� �����
					String para = "gid="+gid+"&msg="+msg;
					
					//�б��ϱ�
					out.println("	<script>");
					out.println("	parent.reg.location.href('mfgInfoServlet?mode=order_view&"+para+"');");
					out.println("	</script>");

					out.println("	<script>");
					out.println("	parent.list.location.href('mfgInfoServlet?mode=order_list&"+para+"');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//���� �۾����ÿ��� �������� �ٷΰ���
			else if ("mfg_review".equals(mode)){
				
				//�б��� �Ķ���� �����
				String para = "pid="+pid+"&factory_no="+factory_no;

				//�б��ϱ�
				out.println("	<script>");
				out.println("	parent.location.href('mfgInfoServlet?mode=mfg_review&"+para+"');");
				out.println("	</script>");
				out.close();
			}
			//------------------------------------------------------------
			//		���� ��ǰ �ҿ䷮���� �� ���õ� ����
			//------------------------------------------------------------
			//���� ��ǰ �ҿ䷮���� LIST
			else if ("item_list".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//������ ��ǰ LIST ����
				ArrayList item_list = new ArrayList();
				item_list = mfgDAO.getSingleMfgItemList(factory_no,mfg_no,level_no,assy_code);
				request.setAttribute("ITEM_List",item_list);
				
				//������ǰ ������ �б�
				com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
				mfgT = mfgDAO.readMfgMasterItem(gid,factory_no);
				request.setAttribute("MFG_master",mfgT);  

				//����ASSY���� LIST
				ArrayList assy_list = new ArrayList();
				assy_list = mfgDAO.getAssyList(factory_no,mfg_no);
				request.setAttribute("ASSY_List",assy_list);

				//�Ķ���� �����ϱ�
				request.setAttribute("assy_code",assy_code);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgItemList.jsp").forward(request,response);
			}
			//���� ��ǰ �ҿ䷮ ���� �غ�
			else if ("item_view".equals(mode)){
				com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);

				//ȭ����� ������ �б�
				com.anbtech.mm.entity.mfgItemTable mfgIT = new com.anbtech.mm.entity.mfgItemTable();
				mfgIT = mfgDAO.readMfgItem(pid);
				request.setAttribute("MFG_item",mfgIT); 

				//�Ķ���� �����ϱ�
				request.setAttribute("order_type",order_type);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mm/mfg/mfgItemReg.jsp").forward(request,response);
				
				
			}
			//���� ��ǰ �ҿ䷮ �����ϱ�
			else if("item_modify".equals(mode)) {
				com.anbtech.mm.business.mfgInputBO mfgBO = new com.anbtech.mm.business.mfgInputBO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					msg = mfgBO.updateMfgItem(pid,need_count,add_count);
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��� �Ķ���� �����
					String para = "gid="+gid+"&msg="+msg+"&factory_no="+factory_no+"&mfg_no="+mfg_no;
					para += "&level_no="+level_no+"&assy_code="+assy_code;
					
					//�б��ϱ�
					out.println("	<script>");
					out.println("	parent.list.location.href('mfgInfoServlet?mode=item_list&"+para+"');");
					out.println("	</script>");

					out.println("	<script>");
					out.println("	parent.reg.location.href('mfgInfoServlet?mode=item_view&"+para+"');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//��ǰ����Ƿ� �����ϱ�
			else if("item_delivery".equals(mode)) {
				com.anbtech.mm.business.mfgInputBO mfgBO = new com.anbtech.mm.business.mfgInputBO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					msg = mfgBO.setMfgStatus(login_id,login_name,gid,mrp_no,mfg_no,item_code,item_unit,fg_code,mfg_count,order_type,order_status,order_start_date,order_end_date,factory_no,factory_name,re_work);
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��� �Ķ���� �����
					String para = "gid="+gid+"&msg="+msg+"&factory_no="+factory_no+"&mfg_no="+mfg_no;
					para += "&level_no="+level_no+"&assy_code="+assy_code;
					
					//�б��ϱ�
					out.println("	<script>");
					out.println("	parent.list.location.href('mfgInfoServlet?mode=item_list&"+para+"');");
					out.println("	</script>");

					out.println("	<script>");
					out.println("	parent.reg.location.href('mfgInfoServlet?mode=item_view&"+para+"');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}

		}catch (Exception e){
			//������� �������� �б�
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			out.close();
		}finally{
			close(con);
		}
	} //doPost()
}

