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

public class pjtCodeServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PJC_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_code":Hanguel.toHanguel(request.getParameter("sItem"));
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
			//	�����ڵ� LIST��������
			//--------------------------------------------------------------------
			//�����ڵ� ������� ��ü LIST��������
			if("PJC_LA".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = pjtDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = pjtDAO.getCurrentPage();		//����������
				int Tpage = pjtDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����ڵ� �μ����� ��ü LIST��������
			else if("PJC_LD".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = pjtDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = pjtDAO.getCurrentPage();		//����������
				int Tpage = pjtDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PJC_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//�����Է�/����/���� ������ �ޱ�
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//�����ڵ�
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));			//phase code
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));			//phase name
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));		//step code
		String mgr_id = Hanguel.toHanguel(request.getParameter("mgr_id"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_id"));		//step name
		String mgr_name = Hanguel.toHanguel(request.getParameter("mgr_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_name"));			//activity code
		String pjt_status = Hanguel.toHanguel(request.getParameter("pjt_status"))==null?"S":Hanguel.toHanguel(request.getParameter("pjt_status"));			//���⹰ �ڵ�
		String type = Hanguel.toHanguel(request.getParameter("type"))==null?"":Hanguel.toHanguel(request.getParameter("type"));						//�����P, �μ��� �μ��ڵ�
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	�����ڵ� �ڵ� �����ϱ�
			//--------------------------------------------------------------------
			//�����ڵ� ������� ��ü LIST��������
			if("PJC_LA".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = pjtDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = pjtDAO.getCurrentPage();		//����������
				int Tpage = pjtDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����ڵ� ������� �ű��Է��ϱ�
			else if("PJC_WA".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pjtDAO.inputProject(pid,pjt_code,pjt_name,in_date,mgr_id,mgr_name,type,pjt_status);
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = pjtDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pjtDAO.getCurrentPage();		//����������
					int Tpage = pjtDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����ڵ� ������� �������밡������
			else if("PJC_VA".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = pjtDAO.getProjectRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeAll_modify.jsp").forward(request,response);
			}
			//�����ڵ� ������� �����ϱ�
			else if("PJC_MA".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pjtDAO.updateProject(pid,pjt_code,pjt_name,in_date,mgr_id,mgr_name,type,pjt_status);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = pjtDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pjtDAO.getCurrentPage();		//����������
					int Tpage = pjtDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�����ڵ� ������� �����ϱ�
			else if("PJC_DA".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pjtDAO.deleteProject(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = pjtDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pjtDAO.getCurrentPage();		//����������
					int Tpage = pjtDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�����ڵ� �μ����� ��ü LIST��������
			else if("PJC_LD".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = pjtDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = pjtDAO.getCurrentPage();		//����������
				int Tpage = pjtDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����ڵ� �μ����� �ű��Է��ϱ�
			else if("PJC_WD".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pjtDAO.inputProject(pid,pjt_code,pjt_name,in_date,mgr_id,mgr_name,type,pjt_status);
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = pjtDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pjtDAO.getCurrentPage();		//����������
					int Tpage = pjtDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�����ڵ� �μ����� �������밡������
			else if("PJC_VD".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = pjtDAO.getProjectRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeDiv_modify.jsp").forward(request,response);
			}
			//�����ڵ� �μ����� �����ϱ�
			else if("PJC_MD".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pjtDAO.updateProject(pid,pjt_code,pjt_name,in_date,mgr_id,mgr_name,type,pjt_status);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = pjtDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pjtDAO.getCurrentPage();		//����������
					int Tpage = pjtDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�����ڵ� �μ����� �����ϱ�
			else if("PJC_DD".equals(mode)){	
				com.anbtech.pjt.db.pjtCodeDAO pjtDAO = new com.anbtech.pjt.db.pjtCodeDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pjtDAO.deleteProject(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = pjtDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pjtDAO.getCurrentPage();		//����������
					int Tpage = pjtDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtcodeDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
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

