import com.anbtech.psm.db.*;
import com.anbtech.psm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class PsmProcessServlet extends HttpServlet {
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"view_search":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"psm_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//PSM MASTER������ ���� �⺻�Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String psm_code = Hanguel.toHanguel(request.getParameter("psm_code"))==null?"":Hanguel.toHanguel(request.getParameter("psm_code"));
		String psm_type = Hanguel.toHanguel(request.getParameter("psm_type"))==null?"":Hanguel.toHanguel(request.getParameter("psm_type"));
		String comp_name = Hanguel.toHanguel(request.getParameter("comp_name"))==null?"":Hanguel.toHanguel(request.getParameter("comp_name"));
		String comp_category = Hanguel.toHanguel(request.getParameter("comp_category"))==null?"":Hanguel.toHanguel(request.getParameter("comp_category"));
		String psm_korea = Hanguel.toHanguel(request.getParameter("psm_korea"))==null?"":Hanguel.toHanguel(request.getParameter("psm_korea"));
		String psm_english = Hanguel.toHanguel(request.getParameter("psm_english"))==null?"":Hanguel.toHanguel(request.getParameter("psm_english"));
		String psm_start_date = Hanguel.toHanguel(request.getParameter("psm_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("psm_start_date"));
		String psm_end_date = Hanguel.toHanguel(request.getParameter("psm_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("psm_end_date"));
		String psm_pm = Hanguel.toHanguel(request.getParameter("psm_pm"))==null?"":Hanguel.toHanguel(request.getParameter("psm_pm"));
		String psm_mgr = Hanguel.toHanguel(request.getParameter("psm_mgr"))==null?"":Hanguel.toHanguel(request.getParameter("psm_mgr"));
		String psm_budget = Hanguel.toHanguel(request.getParameter("psm_budget"))==null?"":Hanguel.toHanguel(request.getParameter("psm_budget"));
		String psm_user = Hanguel.toHanguel(request.getParameter("psm_user"))==null?"":Hanguel.toHanguel(request.getParameter("psm_user"));
		String psm_desc = Hanguel.toHanguel(request.getParameter("psm_desc"))==null?"":Hanguel.toHanguel(request.getParameter("psm_desc"));
		String plan_sum = Hanguel.toHanguel(request.getParameter("plan_sum"))==null?"0":Hanguel.toHanguel(request.getParameter("plan_sum"));
		String plan_labor = Hanguel.toHanguel(request.getParameter("plan_labor"))==null?"0":Hanguel.toHanguel(request.getParameter("plan_labor"));
		String plan_material = Hanguel.toHanguel(request.getParameter("plan_material"))==null?"0":Hanguel.toHanguel(request.getParameter("plan_material"));
		String plan_cost = Hanguel.toHanguel(request.getParameter("plan_cost"))==null?"0":Hanguel.toHanguel(request.getParameter("plan_cost"));
		String plan_plant = Hanguel.toHanguel(request.getParameter("plan_plant"))==null?"0":Hanguel.toHanguel(request.getParameter("plan_plant"));
		String result_plan = Hanguel.toHanguel(request.getParameter("result_plan"))==null?"0":Hanguel.toHanguel(request.getParameter("result_plan"));
		String result_labor = Hanguel.toHanguel(request.getParameter("result_labor"))==null?"0":Hanguel.toHanguel(request.getParameter("result_labor"));
		String result_material = Hanguel.toHanguel(request.getParameter("result_material"))==null?"0":Hanguel.toHanguel(request.getParameter("result_material"));
		String result_cost = Hanguel.toHanguel(request.getParameter("result_cost"))==null?"0":Hanguel.toHanguel(request.getParameter("result_cost"));
		String result_plant = Hanguel.toHanguel(request.getParameter("result_plant"))==null?"0":Hanguel.toHanguel(request.getParameter("result_plant"));
		String contract_date = Hanguel.toHanguel(request.getParameter("contract_date"))==null?"0":Hanguel.toHanguel(request.getParameter("contract_date"));
		String contract_name = Hanguel.toHanguel(request.getParameter("contract_name"))==null?"0":Hanguel.toHanguel(request.getParameter("contract_name"));
		String contract_price = Hanguel.toHanguel(request.getParameter("contract_price"))==null?"0":Hanguel.toHanguel(request.getParameter("contract_price"));
		String complete_date = Hanguel.toHanguel(request.getParameter("complete_date"))==null?"":Hanguel.toHanguel(request.getParameter("complete_date"));
		
		String fname = Hanguel.toHanguel(request.getParameter("fname"))==null?"":Hanguel.toHanguel(request.getParameter("fname"));
		String sname = Hanguel.toHanguel(request.getParameter("sname"))==null?"":Hanguel.toHanguel(request.getParameter("sname"));
		String ftype = Hanguel.toHanguel(request.getParameter("ftype"))==null?"":Hanguel.toHanguel(request.getParameter("ftype"));
		String fsize = Hanguel.toHanguel(request.getParameter("fsize"))==null?"":Hanguel.toHanguel(request.getParameter("fsize"));
		String psm_status = Hanguel.toHanguel(request.getParameter("psm_status"))==null?"1":Hanguel.toHanguel(request.getParameter("psm_status"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String app_date = Hanguel.toHanguel(request.getParameter("app_date"))==null?"":Hanguel.toHanguel(request.getParameter("app_date"));
		
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		String env_status = Hanguel.toHanguel(request.getParameter("env_status"))==null?"1":Hanguel.toHanguel(request.getParameter("env_status"));
		String target = Hanguel.toHanguel(request.getParameter("target"))==null?"":Hanguel.toHanguel(request.getParameter("target"));
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		PSM MASTER ��Ȳ�� �󼼺��� ó���ϱ�
			//------------------------------------------------------------
			//PSM MASTER �����󼼳��뺸��
			if ("view_project".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				com.anbtech.psm.db.psmModifyDAO psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);

				//PSM MASTER ������ �б�
				com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
				masterT = prsDAO.readPsmMaster(pid);
				request.setAttribute("MASTER_table", masterT);

				//�������� ���� 
				ArrayList link_list = new ArrayList();
				link_list = psmDAO.getPsmLinkCodeList(login_id,pid);			
				request.setAttribute("LINK_List", link_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmMasterView.jsp").forward(request,response);
			}
			//��ü �������� ����Ʈ ����
			else if ("view_search".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				
				//���Ѻ� ���� ��ȸ 
				//��ü �������� ����Ʈ �б�
				ArrayList psm_list = new ArrayList();
				psm_list = prsDAO.getPsmMasterList(login_id,psm_start_date,sItem,sWord,page,max_display_cnt);			
				request.setAttribute("PSM_List", psm_list); 

				//�������� �ٷΰ��� List
				com.anbtech.psm.entity.psmMasterTable pageL = new com.anbtech.psm.entity.psmMasterTable();
				pageL = prsDAO.getDisplayPage(login_id,psm_start_date,sItem,sWord,mode,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�������� COLOR LIST �б�
				ArrayList color_list = new ArrayList();
				color_list = prsDAO.readPsmColorList();			
				request.setAttribute("COLOR_List", color_list);

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 
				request.setAttribute("psm_start_date",psm_start_date);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmSearchList.jsp").forward(request,response);
			}
			//��ü �������� ����Ʈ ����
			else if ("view_matrix".equals(mode)){
				com.anbtech.psm.business.psmProcessBO prsBO = new com.anbtech.psm.business.psmProcessBO(con);
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);

				//���Ѻ� ���� ��ȸ 
				//��ü �������� ����Ʈ �б�
				ArrayList psm_list = new ArrayList();
				psm_list = prsBO.getTotalProjects(login_id,psm_start_date);			
				request.setAttribute("PSM_List", psm_list); 

				//����ī�װ��� �ִ���� ���ϱ�
				String max_cnt = Integer.toString(prsBO.getMaxCountProjects());
				request.setAttribute("max_cnt",max_cnt);
				

				//�������� COLOR LIST �б�
				ArrayList color_list = new ArrayList();
				color_list = prsDAO.readPsmColorList();			
				request.setAttribute("COLOR_List", color_list);

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�����ϱ�
				request.setAttribute("psm_start_date",psm_start_date);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmMatrixList.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		PSM MASTER ��Ȳ�� �󼼺��� EXCEL���
			//------------------------------------------------------------
			//PSM MASTER �����󼼳��뺸��
			else if ("excel_project".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				com.anbtech.psm.db.psmModifyDAO psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);

				//PSM MASTER ������ �б�
				com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
				masterT = prsDAO.readPsmMaster(pid);
				request.setAttribute("MASTER_table", masterT);

				//�������� ���� 
				ArrayList link_list = new ArrayList();
				link_list = psmDAO.getPsmLinkCodeList(login_id,pid);			
				request.setAttribute("LINK_List", link_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmMasterExcel.jsp").forward(request,response);
			}
			//��ü �������� ����Ʈ ����
			else if ("excel_search".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				com.anbtech.psm.db.psmModifyDAO psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);

				//���Ѻ� ���� ��ȸ 
				ArrayList psm_list = new ArrayList();
				psm_list = prsDAO.getPsmMasterExcelList(login_id,psm_start_date,sItem,sWord,page,max_display_cnt);			
				request.setAttribute("PSM_List", psm_list); 

				//�������� COLOR LIST �б�
				ArrayList color_list = new ArrayList();
				color_list = prsDAO.readPsmColorList();			
				request.setAttribute("COLOR_List", color_list);

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�������� ���� 
				ArrayList link_list = new ArrayList();
				link_list = psmDAO.getPsmLinkCodeList(login_id,pid);			
				request.setAttribute("LINK_List", link_list);


				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 
				request.setAttribute("psm_start_date",psm_start_date);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmSearchExcel.jsp").forward(request,response);
			}
			//��ü �������� ����Ʈ ����
			else if ("excel_matrix".equals(mode)){
				com.anbtech.psm.business.psmProcessBO prsBO = new com.anbtech.psm.business.psmProcessBO(con);
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				com.anbtech.psm.db.psmModifyDAO psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);

				//���Ѻ� ���� ��ȸ 
				ArrayList psm_list = new ArrayList();
				psm_list = prsBO.getTotalProjects(login_id,psm_start_date);			
				request.setAttribute("PSM_List", psm_list); 
				

				//����ī�װ��� �ִ���� ���ϱ�
				String max_cnt = Integer.toString(prsBO.getMaxCountProjects());
				request.setAttribute("max_cnt",max_cnt);

				//�������� COLOR LIST �б�
				ArrayList color_list = new ArrayList();
				color_list = prsDAO.readPsmColorList();			
				request.setAttribute("COLOR_List", color_list);

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�����ϱ�
				request.setAttribute("psm_start_date",psm_start_date);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmMatrixExcel.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		�ش� ����ã��
			//------------------------------------------------------------
			//�ش���� ã��
			else if ("search_project".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();

				//��ü �������� ����Ʈ �б�
				if(psm_start_date.length() == 0) psm_start_date = anbdt.getYear();
				ArrayList psm_list = new ArrayList();
				psm_list = prsDAO.getProjectlList(psm_start_date,sItem,sWord);			
				request.setAttribute("PSM_List", psm_list); 

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�����ϱ�
				request.setAttribute("target",target);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 
				request.setAttribute("psm_start_date",psm_start_date);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmSearchProject.jsp").forward(request,response);
			}
			//�ش���� ã�� : ���� ���
			else if ("search_single".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();

				//��ü �������� ����Ʈ �б�
				if(psm_start_date.length() == 0) psm_start_date = anbdt.getYear();
				ArrayList psm_list = new ArrayList();
				psm_list = prsDAO.getSingleList(psm_start_date,sItem,sWord);			
				request.setAttribute("PSM_List", psm_list); 

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�����ϱ�
				request.setAttribute("target",target);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 
				request.setAttribute("psm_start_date",psm_start_date);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmSearchSingle.jsp").forward(request,response);
			}

		}catch (Exception e){
			//������� �������� �б�
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
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

		//MultipartRequest ũ��, ������丮
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/psm/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory�����ϱ�
		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//�⺻�Ķ����
		String mode = multi.getParameter("mode")==null?"view_search":multi.getParameter("mode");
		String page = multi.getParameter("page")==null?"1":multi.getParameter("page");
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = multi.getParameter("sItem")==null?"psm_code":multi.getParameter("sItem");
		String sWord = multi.getParameter("sWord")==null?"":multi.getParameter("sWord");
		
		//PSM MASTER������ ���� �⺻�Ķ����
		String pid = multi.getParameter("pid")==null?"":multi.getParameter("pid");
		String psm_code = multi.getParameter("psm_code")==null?"":multi.getParameter("psm_code");
		String psm_type = multi.getParameter("psm_type")==null?"":multi.getParameter("psm_type");
		String comp_name = multi.getParameter("comp_name")==null?"":multi.getParameter("comp_name");
		String comp_category = multi.getParameter("comp_category")==null?"":multi.getParameter("comp_category");
		String ecr_code = multi.getParameter("ecr_code")==null?"":multi.getParameter("ecr_code");
		String psm_korea = multi.getParameter("psm_korea")==null?"":multi.getParameter("psm_korea");
		String psm_english = multi.getParameter("psm_english")==null?"":multi.getParameter("psm_english");
		String psm_start_date = multi.getParameter("psm_start_date")==null?"":multi.getParameter("psm_start_date");
		String psm_end_date = multi.getParameter("psm_end_date")==null?"":multi.getParameter("psm_end_date");
		String psm_pm = multi.getParameter("psm_pm")==null?"":multi.getParameter("psm_pm");
		String psm_mgr = multi.getParameter("psm_mgr")==null?"":multi.getParameter("psm_mgr");
		String psm_budget = multi.getParameter("psm_budget")==null?"":multi.getParameter("psm_budget");
		String psm_user = multi.getParameter("psm_user")==null?"":multi.getParameter("psm_user");
		String psm_desc = multi.getParameter("psm_desc")==null?"":multi.getParameter("psm_desc");
		String plan_sum = multi.getParameter("plan_sum")==null?"0":multi.getParameter("plan_sum");
		String plan_labor = multi.getParameter("plan_labor")==null?"0":multi.getParameter("plan_labor");
		String plan_material = multi.getParameter("plan_material")==null?"0":multi.getParameter("plan_material");
		String plan_cost = multi.getParameter("plan_cost")==null?"0":multi.getParameter("plan_cost");
		String plan_plant = multi.getParameter("plan_plant")==null?"0":multi.getParameter("plan_plant");
		String result_plan = multi.getParameter("result_plan")==null?"0":multi.getParameter("result_plan");
		String result_labor = multi.getParameter("result_labor")==null?"0":multi.getParameter("result_labor");
		String result_material = multi.getParameter("result_material")==null?"0":multi.getParameter("result_material");
		String result_cost = multi.getParameter("result_cost")==null?"0":multi.getParameter("result_cost");
		String result_plant = multi.getParameter("result_plant")==null?"0":multi.getParameter("result_plant");
		String contract_date = multi.getParameter("contract_date")==null?"":multi.getParameter("contract_date");
		String contract_name = multi.getParameter("contract_name")==null?"":multi.getParameter("contract_name");
		String contract_price = multi.getParameter("contract_price")==null?"0":multi.getParameter("contract_price");
		String complete_date = multi.getParameter("complete_date")==null?"":multi.getParameter("complete_date");
		
		String fname = multi.getParameter("fname")==null?"":multi.getParameter("fname");
		String sname = multi.getParameter("sname")==null?"":multi.getParameter("sname");
		String ftype = multi.getParameter("ftype")==null?"":multi.getParameter("ftype");
		String fsize = multi.getParameter("fsize")==null?"":multi.getParameter("fsize");
		String psm_status = multi.getParameter("psm_status")==null?"1":multi.getParameter("psm_status");
		String reg_date = multi.getParameter("reg_date")==null?"":multi.getParameter("reg_date");
		String app_date = multi.getParameter("app_date")==null?"":multi.getParameter("app_date");
		String attache_cnt = multi.getParameter("attache_cnt")==null?"0":multi.getParameter("attache_cnt");	//÷�ΰ��� �ִ���� �̸�
		String msg = multi.getParameter("msg")==null?"":multi.getParameter("msg");
		String env_status = multi.getParameter("env_status")==null?"":multi.getParameter("env_status");
		String target = multi.getParameter("target")==null?"":multi.getParameter("target");


		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//------------------------------------------------------------
			//		PSM MASTER ��Ȳ�� �󼼺��� ó���ϱ�
			//------------------------------------------------------------
			//PSM MASTER �����󼼳��뺸��
			if ("view_project".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				com.anbtech.psm.db.psmModifyDAO psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);

				//PSM MASTER ������ �б�
				com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
				masterT = prsDAO.readPsmMaster(pid);
				request.setAttribute("MASTER_table", masterT);

				//�������� ���� 
				ArrayList link_list = new ArrayList();
				link_list = psmDAO.getPsmLinkCodeList(login_id,pid);			
				request.setAttribute("LINK_List", link_list);


				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmMasterView.jsp").forward(request,response);
			}
			//��ü �������� ����Ʈ ����
			else if ("view_search".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);

				//��ü �������� ����Ʈ �б�
				ArrayList psm_list = new ArrayList();
				psm_list = prsDAO.getPsmMasterList(login_id,psm_start_date,sItem,sWord,page,max_display_cnt);			
				request.setAttribute("PSM_List", psm_list); 

				//�������� �ٷΰ��� List
				com.anbtech.psm.entity.psmMasterTable pageL = new com.anbtech.psm.entity.psmMasterTable();
				pageL = prsDAO.getDisplayPage(login_id,psm_start_date,sItem,sWord,mode,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�������� COLOR LIST �б�
				ArrayList color_list = new ArrayList();
				color_list = prsDAO.readPsmColorList();			
				request.setAttribute("COLOR_List", color_list);

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 
				request.setAttribute("psm_start_date",psm_start_date);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmSearchList.jsp").forward(request,response);
			}
			//��ü �������� ����Ʈ ����
			else if ("view_matrix".equals(mode)){
				com.anbtech.psm.business.psmProcessBO prsBO = new com.anbtech.psm.business.psmProcessBO(con);
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);

				//��ü �������� ����Ʈ �б�
				ArrayList psm_list = new ArrayList();
				psm_list = prsBO.getTotalProjects(login_id,psm_start_date);			
				request.setAttribute("PSM_List", psm_list); 

				//����ī�װ��� �ִ���� ���ϱ�
				String max_cnt = Integer.toString(prsBO.getMaxCountProjects());
				request.setAttribute("max_cnt",max_cnt);

				//�������� COLOR LIST �б�
				ArrayList color_list = new ArrayList();
				color_list = prsDAO.readPsmColorList();			
				request.setAttribute("COLOR_List", color_list);

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�����ϱ�
				request.setAttribute("psm_start_date",psm_start_date);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmMatrixList.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		�ش� ����ã��
			//------------------------------------------------------------
			//�ش���� ã�� : ���� ���
			else if ("search_project".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();

				//��ü �������� ����Ʈ �б�
				if(psm_start_date.length() == 0) psm_start_date = anbdt.getYear();
				ArrayList psm_list = new ArrayList();
				psm_list = prsDAO.getProjectlList(psm_start_date,sItem,sWord);			
				request.setAttribute("PSM_List", psm_list); 

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�����ϱ�
				request.setAttribute("target",target);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 
				request.setAttribute("psm_start_date",psm_start_date);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmSearchProject.jsp").forward(request,response);
			}
			//�ش���� ã�� : ���� ���
			else if ("search_single".equals(mode)){
				com.anbtech.psm.db.psmProcessDAO prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
				com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();

				//��ü �������� ����Ʈ �б�
				if(psm_start_date.length() == 0) psm_start_date = anbdt.getYear();
				ArrayList psm_list = new ArrayList();
				psm_list = prsDAO.getSingleList(psm_start_date,sItem,sWord);			
				request.setAttribute("PSM_List", psm_list); 

				//�������ʽ��۳⵵�� �ֱٳ⵵ ���ϱ�
				String first_year = prsDAO.getPsmFirstYear();
				String last_year = prsDAO.getPsmLastYear();
				request.setAttribute("first_year",first_year);
				request.setAttribute("last_year",last_year);

				//�����ϱ�
				request.setAttribute("target",target);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 
				request.setAttribute("psm_start_date",psm_start_date);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/psm/view/psmSearchSingle.jsp").forward(request,response);
			}

		}catch (Exception e){
			//������� �������� �б�
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} //doPost()
}


