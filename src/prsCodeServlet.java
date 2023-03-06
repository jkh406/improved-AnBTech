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

public class prsCodeServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PHA_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"ph_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

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
			//	���ߴܰ�[PHASE] �ڵ� LIST��������
			//--------------------------------------------------------------------
			//���ߴܰ�(Phase) ������� ��ü LIST��������
			if("PHA_LA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = phaseDAO.getCurrentPage();		//����������
				int Tpage = phaseDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//���ߴܰ�(Phase) �μ����� ��ü LIST��������
			else if("PHA_LD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = phaseDAO.getCurrentPage();		//����������
				int Tpage = phaseDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//--------------------------------------------------------------------
			//	�����׸�[STEP] �ڵ� LIST��������
			//--------------------------------------------------------------------
			//�����׸�(Step) ������� ��ü LIST��������
			else if("STP_LA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = stepDAO.getCurrentPage();		//����������
				int Tpage = stepDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����׸�(Step) �μ����� ��ü LIST��������
			else if("STP_LD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = stepDAO.getCurrentPage();		//����������
				int Tpage = stepDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//--------------------------------------------------------------------
			//	�����׸�[Activity] �ڵ� LIST��������
			//--------------------------------------------------------------------
			//�����׸�(Activity) ������� ��ü LIST��������
			else if("ACT_LA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = actDAO.getCurrentPage();		//����������
				int Tpage = actDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����׸�(Activity) �μ����� ��ü LIST��������
			else if("ACT_LD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = actDAO.getCurrentPage();		//����������
				int Tpage = actDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//--------------------------------------------------------------------
			//	�����׸� ���⹰ LIST��������
			//--------------------------------------------------------------------
			//�����׸� ���⹰ ������� ��ü LIST��������
			else if("DOC_LA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = docDAO.getCurrentPage();		//����������
				int Tpage = docDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����׸� ���⹰ �μ����� ��ü LIST��������
			else if("DOC_LD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = docDAO.getCurrentPage();		//����������
				int Tpage = docDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
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
	 * post������� �Ѿ���� �� ó�� (�Է�,����,����,���)
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PHA_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"ph_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//�����Է�/����/���� ������ �ޱ�
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//�����ڵ�
		String ph_code = Hanguel.toHanguel(request.getParameter("ph_code"))==null?"":Hanguel.toHanguel(request.getParameter("ph_code"));			//phase code
		String ph_name = Hanguel.toHanguel(request.getParameter("ph_name"))==null?"":Hanguel.toHanguel(request.getParameter("ph_name"));			//phase name
		String step_code = Hanguel.toHanguel(request.getParameter("step_code"))==null?"":Hanguel.toHanguel(request.getParameter("step_code"));		//step code
		String step_name = Hanguel.toHanguel(request.getParameter("step_name"))==null?"":Hanguel.toHanguel(request.getParameter("step_name"));		//step name
		String act_code = Hanguel.toHanguel(request.getParameter("act_code"))==null?"":Hanguel.toHanguel(request.getParameter("act_code"));			//activity code
		String act_name = Hanguel.toHanguel(request.getParameter("act_name"))==null?"":Hanguel.toHanguel(request.getParameter("act_name"));			//activity name
		String doc_code = Hanguel.toHanguel(request.getParameter("doc_code"))==null?"":Hanguel.toHanguel(request.getParameter("doc_code"));			//���⹰ �ڵ�
		String doc_name = Hanguel.toHanguel(request.getParameter("doc_name"))==null?"":Hanguel.toHanguel(request.getParameter("doc_name"));			//���⹰ �̸�
		String prs_code = Hanguel.toHanguel(request.getParameter("prs_code"))==null?"":Hanguel.toHanguel(request.getParameter("prs_code"));			//���μ��� �ڵ�
		String prs_name = Hanguel.toHanguel(request.getParameter("prs_name"))==null?"":Hanguel.toHanguel(request.getParameter("prs_name"));			//���μ��� �̸�
		String parent_node = Hanguel.toHanguel(request.getParameter("parent_node"))==null?"":Hanguel.toHanguel(request.getParameter("parent_node"));//����
		String child_node = Hanguel.toHanguel(request.getParameter("child_node"))==null?"":Hanguel.toHanguel(request.getParameter("child_node"));	//�ڳ��
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));		//����
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));			//���ڳ�� ������ȣ
		String dip_no = Hanguel.toHanguel(request.getParameter("dip_no"))==null?"":Hanguel.toHanguel(request.getParameter("dip_no"));				//ȭ����� ����
		String type = Hanguel.toHanguel(request.getParameter("type"))==null?"":Hanguel.toHanguel(request.getParameter("type"));						//�����P, �μ��� �μ��ڵ�
		
		//�������A, �μ�����D
		String tag = Hanguel.toHanguel(request.getParameter("tag"))==null?"":Hanguel.toHanguel(request.getParameter("tag"));						
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	���ߴܰ�[PHASE] �ڵ� �����ϱ�
			//--------------------------------------------------------------------
			//���ߴܰ�(Phase) ������� ��ü LIST��������
			if("PHA_LA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = phaseDAO.getCurrentPage();		//����������
				int Tpage = phaseDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//���ߴܰ�(Phase) ������� �ű��Է��ϱ�
			else if("PHA_WA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					phaseDAO.inputPhase(pid,ph_code,ph_name,type);
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = phaseDAO.getCurrentPage();		//����������
					int Tpage = phaseDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//���ߴܰ�(Phase) ������� �������밡������
			else if("PHA_VA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/phaseAll_modify.jsp").forward(request,response);
			}
			//���ߴܰ�(Phase) ������� �����ϱ�
			else if("PHA_MA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = phaseDAO.updatePhase(pid,ph_code,ph_name,type);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('���ߴܰ��ڵ尡 �ߺ��Ǿ� ������ �� �����ϴ�.');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = phaseDAO.getCurrentPage();		//����������
						int Tpage = phaseDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���ߴܰ�(Phase) ������� �����ϱ�
			else if("PHA_DA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = phaseDAO.deletePhase(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('�����׸�� ����Ǿ� ������ �� �����ϴ�');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = phaseDAO.getCurrentPage();		//����������
						int Tpage = phaseDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���ߴܰ�(Phase) �μ����� ��ü LIST��������
			else if("PHA_LD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = phaseDAO.getCurrentPage();		//����������
				int Tpage = phaseDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//���ߴܰ�(Phase) �μ����� �ű��Է��ϱ�
			else if("PHA_WD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					phaseDAO.inputPhase(pid,ph_code,ph_name,type);
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = phaseDAO.getCurrentPage();		//����������
					int Tpage = phaseDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//���ߴܰ�(Phase) �μ����� �������밡������
			else if("PHA_VD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_modify.jsp").forward(request,response);
			}
			//���ߴܰ�(Phase) �μ����� �����ϱ�
			else if("PHA_MD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = phaseDAO.updatePhase(pid,ph_code,ph_name,type);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('���ߴܰ��ڵ尡 �ߺ��Ǿ� ������ �� �����ϴ�.');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = phaseDAO.getCurrentPage();		//����������
						int Tpage = phaseDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���ߴܰ�(Phase) �μ����� �����ϱ�
			else if("PHA_DD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = phaseDAO.deletePhase(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('�����׸�� ����Ǿ� ������ �� �����ϴ�');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = phaseDAO.getCurrentPage();		//����������
						int Tpage = phaseDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}

			//--------------------------------------------------------------------
			//	�����׸�[STEP] �ڵ� �����ϱ�
			//--------------------------------------------------------------------
			//�����׸�(Step) ������� ��ü LIST��������
			else if("STP_LA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = stepDAO.getCurrentPage();		//����������
				int Tpage = stepDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����׸�(Step) ������� �ű��Է��ϱ�
			else if("STP_WA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stepDAO.inputStep(pid,ph_code,step_code,step_name,type);
					stepDAO.updateStepCode (login_id,type,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = stepDAO.getCurrentPage();		//����������
					int Tpage = stepDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸�(Step) ������� �������밡������
			else if("STP_VA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/stepAll_modify.jsp").forward(request,response);
			}
			//�����׸�(Step) ������� �����ϱ�
			else if("STP_MA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stepDAO.updateStep(pid,ph_code,step_code,step_name,type);
					stepDAO.updateStepCode (login_id,type,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = stepDAO.getCurrentPage();		//����������
					int Tpage = stepDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸�(Step) ������� �����ϱ�
			else if("STP_DA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = stepDAO.deleteStep(pid);
					if(data == true) stepDAO.updateStepCode (login_id,type,tag);	//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('�����׸�� ����Ǿ� ������ �� �����ϴ�');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = stepDAO.getCurrentPage();		//����������
						int Tpage = stepDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�����׸�(Step) �μ����� ��ü LIST��������
			else if("STP_LD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = stepDAO.getCurrentPage();		//����������
				int Tpage = stepDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����׸�(Step) �μ����� �ű��Է��ϱ�
			else if("STP_WD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stepDAO.inputStep(pid,ph_code,step_code,step_name,type);
					stepDAO.updateStepCode (login_id,type,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = stepDAO.getCurrentPage();		//����������
					int Tpage = stepDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸�(Step) �μ����� �������밡������
			else if("STP_VD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/stepDiv_modify.jsp").forward(request,response);
			}
			//�����׸�(Step) �μ����� �����ϱ�
			else if("STP_MD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stepDAO.updateStep(pid,ph_code,step_code,step_name,type);
					stepDAO.updateStepCode (login_id,type,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = stepDAO.getCurrentPage();		//����������
					int Tpage = stepDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸�(Step) �μ����� �����ϱ�
			else if("STP_DD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = stepDAO.deleteStep(pid);
					if(data == true) stepDAO.updateStepCode (login_id,type,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('�����׸�� ����Ǿ� ������ �� �����ϴ�');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = stepDAO.getCurrentPage();		//����������
						int Tpage = stepDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}

			//--------------------------------------------------------------------
			//	�����׸�[activity] �ڵ� �����ϱ�
			//--------------------------------------------------------------------
			//�����׸�(activity) ������� ��ü LIST��������
			else if("ACT_LA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = actDAO.getCurrentPage();		//����������
				int Tpage = actDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����׸�(activity) ������� �ű��Է��ϱ�
			else if("ACT_WA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.inputActivity(pid,ph_code,step_code,act_code,act_name,type);
					actDAO.updateActivityCode(login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//����������
					int Tpage = actDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸�(activity) ������� �������밡������
			else if("ACT_VA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/activityAll_modify.jsp").forward(request,response);
			}
			//�����׸�(activity) ������� �����ϱ�
			else if("ACT_MA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.updateActivity(pid,ph_code,step_code,act_code,act_name,type);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//����������
					int Tpage = actDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸�(activity) ������� �����ϱ�
			else if("ACT_DA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.deleteActivity(pid);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//����������
					int Tpage = actDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸�(activity) �μ����� ��ü LIST��������
			else if("ACT_LD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = actDAO.getCurrentPage();		//����������
				int Tpage = actDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����׸�(activity) �μ����� �ű��Է��ϱ�
			else if("ACT_WD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.inputActivity(pid,ph_code,step_code,act_code,act_name,type);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//����������
					int Tpage = actDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸�(activity) �μ����� �������밡������
			else if("ACT_VD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/activityDiv_modify.jsp").forward(request,response);
			}
			//�����׸�(activity) �μ����� �����ϱ�
			else if("ACT_MD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.updateActivity(pid,ph_code,step_code,act_code,act_name,type);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//����������
					int Tpage = actDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸�(activity) �μ����� �����ϱ�
			else if("ACT_DD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.deleteActivity(pid);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//����������
					int Tpage = actDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//--------------------------------------------------------------------
			//	�����׸� ���⹰ �����ϱ�
			//--------------------------------------------------------------------
			//�����׸� ���⹰ ������� ��ü LIST��������
			else if("DOC_LA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = docDAO.getCurrentPage();		//����������
				int Tpage = docDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����׸� ���⹰ ������� �ű��Է��ϱ�
			else if("DOC_WA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.inputActivityDoc(pid,ph_code,step_code,doc_code,doc_name,type);
					docDAO.updateActivityDocCode(login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸� ���⹰ ������� �������밡������
			else if("DOC_VA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/docAll_modify.jsp").forward(request,response);
			}
			//�����׸� ���⹰ ������� �����ϱ�
			else if("DOC_MA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.updateActivityDoc(pid,ph_code,step_code,doc_code,doc_name,type);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸� ���⹰ ������� �����ϱ�
			else if("DOC_DA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.deleteActivityDoc(pid);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸� ���⹰ �μ����� ��ü LIST��������
			else if("DOC_LD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = docDAO.getCurrentPage();		//����������
				int Tpage = docDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����׸� ���⹰ �μ����� �ű��Է��ϱ�
			else if("DOC_WD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.inputActivityDoc(pid,ph_code,step_code,doc_code,doc_name,type);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸� ���⹰ �μ����� �������밡������
			else if("DOC_VD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/docDiv_modify.jsp").forward(request,response);
			}
			//�����׸� ���⹰ �μ����� �����ϱ�
			else if("DOC_MD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.updateActivityDoc(pid,ph_code,step_code,doc_code,doc_name,type);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����׸� ���⹰ �μ����� �����ϱ�
			else if("DOC_DD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.deleteActivityDoc(pid);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//����ڵ� ���������� update
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
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

