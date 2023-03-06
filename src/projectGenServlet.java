import com.anbtech.pjt.entity.*;
import com.anbtech.pjt.db.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class projectGenServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 20;

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

		//���� �������� ����� ���̵� ��������
		//������ ����Ǿ��� ��� �α� �������� ���� �̵���Ų��.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;

		//��� �� ���� ������ �ĸ�����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PBS_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtword(pjtWord);		//��������
			table.setSitem(sItem);			//sItem
			table.setSword(sWord);			//sWord
		para_list.add(table);

		//�޴���� �������� �˾ƺ���
		String mgr_mode = Hanguel.toHanguel(request.getParameter("mgr_mode"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_mode"));
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	�޴���� ���� �˻��ϱ�
			//--------------------------------------------------------------------
			com.anbtech.pjt.db.pjtUseMgrDAO mgrDAO = new com.anbtech.pjt.db.pjtUseMgrDAO(con);
			boolean mgr = mgrDAO.getUseMgr(login_id,mgr_mode);
			if(!mgr) {
				out.println("	<script>");
				out.println("	alert('�������� �����ϴ�.');");
				out.println("	parent.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/pjt/pjtBody.jsp');");
				out.println("	</script>");
				out.close();
			}
		
			//--------------------------------------------------------------------
			//	�����⺻���� LIST��������
			//--------------------------------------------------------------------
			//��ü LIST��������
			if("PBS_LA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = genDAO.getCurrentPage();		//����������
				int Tpage = genDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�μ����� ��ü LIST��������
			else if("PBS_LD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = genDAO.getCurrentPage();		//����������
				int Tpage = genDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} // doGet()


	/**********************************
	 * post������� �Ѿ���� �� ó�� (�Է�,����,����)
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//���� �������� ����� ���̵� ��������
		//������ ����Ǿ��� ��� �α� �������� ���� �̵���Ų��.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	alert('����� ������ �����ϴ�. �ʱ� ȭ������ �̵��մϴ�.\n\n�� �޽����� �Ʒ��� ���� ��쿡 ��Ÿ���ϴ�.\n1.�α��� �� ��ð� �ƹ� �۾��� ���� �ʾƼ� �ڵ� ����� ���\n2.���������� �α������� �ʰ� Ư�� �������� �������Ϸ��� �õ��� ���');");
			out.println("	top.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/new/index.html');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;

		//��� �� ���� ������ �ĸ�����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PBS_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//�⺻���� �Է�/����/���� ������ �ޱ�
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//�����ڵ�
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));			//phase code
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));			//phase name
		String owner = Hanguel.toHanguel(request.getParameter("owner"))==null?"":Hanguel.toHanguel(request.getParameter("owner"));					//owner
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));			//�Է���
		String pjt_mbr_id = Hanguel.toHanguel(request.getParameter("pjt_mbr_id"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_id"));			//
		String pjt_class = Hanguel.toHanguel(request.getParameter("pjt_class"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_class"));			//
		String pjt_target = Hanguel.toHanguel(request.getParameter("pjt_target"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_target"));			//
		String mgt_plan = Hanguel.toHanguel(request.getParameter("mgt_plan"))==null?"":Hanguel.toHanguel(request.getParameter("mgt_plan"));			//
		String parent_code = Hanguel.toHanguel(request.getParameter("parent_code"))==null?"":Hanguel.toHanguel(request.getParameter("parent_code"));			//
		String mbr_exp = Hanguel.toHanguel(request.getParameter("mbr_exp"))==null?"":Hanguel.toHanguel(request.getParameter("mbr_exp"));			//
		String cost_exp = Hanguel.toHanguel(request.getParameter("cost_exp"))==null?"":Hanguel.toHanguel(request.getParameter("cost_exp"));			//
		String plan_start_date = Hanguel.toHanguel(request.getParameter("plan_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_start_date"));			//
		String plan_end_date = Hanguel.toHanguel(request.getParameter("plan_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_end_date"));			//
		String chg_start_date = Hanguel.toHanguel(request.getParameter("chg_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("chg_start_date"));			//
		String chg_end_date = Hanguel.toHanguel(request.getParameter("chg_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("chg_end_date"));			//
		String rst_start_date = Hanguel.toHanguel(request.getParameter("rst_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("rst_start_date"));			//
		String rst_end_date = Hanguel.toHanguel(request.getParameter("rst_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("rst_end_date"));			//
		String prs_code = Hanguel.toHanguel(request.getParameter("prs_code"))==null?"":Hanguel.toHanguel(request.getParameter("prs_code"));			//
		String prs_type = Hanguel.toHanguel(request.getParameter("prs_type"))==null?"":Hanguel.toHanguel(request.getParameter("prs_type"));			//
		String pjt_desc = Hanguel.toHanguel(request.getParameter("pjt_desc"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_desc"));			//
		String pjt_spec = Hanguel.toHanguel(request.getParameter("pjt_spec"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_spec"));			//
		String pjt_status = Hanguel.toHanguel(request.getParameter("pjt_status"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_status"));			//
		String flag = Hanguel.toHanguel(request.getParameter("flag"))==null?"":Hanguel.toHanguel(request.getParameter("flag"));			//
		String plan_labor = Hanguel.toHanguel(request.getParameter("plan_labor"))==null?"":Hanguel.toHanguel(request.getParameter("plan_labor"));			//
		String plan_sample = Hanguel.toHanguel(request.getParameter("plan_sample"))==null?"":Hanguel.toHanguel(request.getParameter("plan_sample"));			//
		String plan_metal = Hanguel.toHanguel(request.getParameter("plan_metal"))==null?"":Hanguel.toHanguel(request.getParameter("plan_metal"));			//
		String plan_mup = Hanguel.toHanguel(request.getParameter("plan_mup"))==null?"":Hanguel.toHanguel(request.getParameter("plan_mup"));			//
		String plan_oversea = Hanguel.toHanguel(request.getParameter("plan_oversea"))==null?"":Hanguel.toHanguel(request.getParameter("plan_oversea"));			//
		String plan_plant = Hanguel.toHanguel(request.getParameter("plan_plant"))==null?"":Hanguel.toHanguel(request.getParameter("plan_plant"));			//
		String result_labor = Hanguel.toHanguel(request.getParameter("result_labor"))==null?"":Hanguel.toHanguel(request.getParameter("result_labor"));			//
		String result_sample = Hanguel.toHanguel(request.getParameter("result_sample"))==null?"":Hanguel.toHanguel(request.getParameter("result_sample"));			//
		String result_metal = Hanguel.toHanguel(request.getParameter("result_metal"))==null?"":Hanguel.toHanguel(request.getParameter("result_metal"));			//
		String result_mup = Hanguel.toHanguel(request.getParameter("result_mup"))==null?"":Hanguel.toHanguel(request.getParameter("result_mup"));			//
		String result_oversea = Hanguel.toHanguel(request.getParameter("result_oversea"))==null?"":Hanguel.toHanguel(request.getParameter("result_oversea"));			//
		String result_plant = Hanguel.toHanguel(request.getParameter("result_plant"))==null?"":Hanguel.toHanguel(request.getParameter("result_plant"));			//

		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtword(pjtWord);		//��������
			table.setSitem(sItem);			//sItem
			table.setSword(sWord);			//sWord
		para_list.add(table);

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	�����ڵ� �ڵ� �����ϱ�
			//--------------------------------------------------------------------
			//�������� ������� ��ü LIST��������
			if("PBS_LA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = genDAO.getCurrentPage();		//����������
				int Tpage = genDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�������� ������� �ű��Է��ϱ�
			else if("PBS_WA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.inputGeneral(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,plan_mup,plan_oversea,plan_plant);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//����������
					int Tpage = genDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������� ������� �������밡������
			else if("PBS_VA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getGeneralRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_modify.jsp").forward(request,response);
			}
			//�������� ������� �����ϱ�
			else if("PBS_MA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.updateGeneral(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,chg_start_date,chg_end_date,prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,plan_mup,plan_oversea,plan_plant);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//����������
					int Tpage = genDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������� ������� �����ϱ�
			else if("PBS_DA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.deleteGeneral(pjt_code);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//����������
					int Tpage = genDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);	
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������� �μ����� ��ü LIST��������
			else if("PBS_LD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = genDAO.getCurrentPage();		//����������
				int Tpage = genDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�������� �μ����� �ű��Է��ϱ�
			else if("PBS_WD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.inputGeneral(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,plan_mup,plan_oversea,plan_plant);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//����������
					int Tpage = genDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������� �μ����� �������밡������
			else if("PBS_VD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getGeneralRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_modify.jsp").forward(request,response);
			}
			//�������� �μ����� �����ϱ�
			else if("PBS_MD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.updateGeneral(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,chg_start_date,chg_end_date,prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,plan_mup,plan_oversea,plan_plant);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//����������
					int Tpage = genDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������� �μ����� �����ϱ�
			else if("PBS_DD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.deleteGeneral(pjt_code);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//����������
					int Tpage = genDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} //doPost()
}

