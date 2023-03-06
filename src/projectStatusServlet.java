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

public class projectStatusServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PJS_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");
		
		//�������� ���� ����/���� ������ �ޱ�
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//�����ڵ�
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));			//phase code
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));			//phase name
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));		//step code
		String mgr_id = Hanguel.toHanguel(request.getParameter("mgr_id"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_id"));		//step name
		String mgr_name = Hanguel.toHanguel(request.getParameter("mgr_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_name"));			//activity code
		String pjt_status = Hanguel.toHanguel(request.getParameter("pjt_status"))==null?"S":Hanguel.toHanguel(request.getParameter("pjt_status"));			//���⹰ �ڵ�
		String type = Hanguel.toHanguel(request.getParameter("type"))==null?"":Hanguel.toHanguel(request.getParameter("type"));						//�����P, �μ��� �μ��ڵ�
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"":Hanguel.toHanguel(request.getParameter("note"));						//�����P, �μ��� �μ��ڵ�
		
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
			//	�������º��� LIST��������
			//--------------------------------------------------------------------
			//�������º��� ������� ��ü LIST��������
			if("PJS_LA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = statusDAO.getCurrentPage();		//����������
				int Tpage = statusDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�������º��� ������� ���뺸��
			else if("PJS_VA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_view.jsp").forward(request,response);
			}
			//�������º��� �μ����� ��ü LIST��������
			else if("PJS_LD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = statusDAO.getCurrentPage();		//����������
				int Tpage = statusDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�������º��� �μ����� ���뺸��
			else if("PJS_VD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_view.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PJS_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//�������� ���� ����/���� ������ �ޱ�
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//�����ڵ�
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));			//phase code
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));			//phase name
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));		//step code
		String mgr_id = Hanguel.toHanguel(request.getParameter("mgr_id"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_id"));		//step name
		String mgr_name = Hanguel.toHanguel(request.getParameter("mgr_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_name"));			//activity code
		String pjt_status = Hanguel.toHanguel(request.getParameter("pjt_status"))==null?"S":Hanguel.toHanguel(request.getParameter("pjt_status"));			//���⹰ �ڵ�
		String type = Hanguel.toHanguel(request.getParameter("type"))==null?"":Hanguel.toHanguel(request.getParameter("type"));						//�����P, �μ��� �μ��ڵ�
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"":Hanguel.toHanguel(request.getParameter("note"));						//�����P, �μ��� �μ��ڵ�
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	�������º��� �ڵ� �����ϱ�
			//--------------------------------------------------------------------
			//�������º��� ������� ��ü LIST��������
			if("PJS_LA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = statusDAO.getCurrentPage();		//����������
				int Tpage = statusDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�������º��� ������� �������밡������
			else if("PJS_MVA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_modify.jsp").forward(request,response);
			}
			//�������º��� ������� �����ϱ�
			else if("PJS_MA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					statusDAO.updateProject(pjt_code,pjt_status,note,in_date);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = statusDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = statusDAO.getCurrentPage();		//����������
					int Tpage = statusDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������º��� ������� �����ϱ�
			else if("PJS_DA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					statusDAO.deleteProject(pjt_code);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = statusDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = statusDAO.getCurrentPage();		//����������
					int Tpage = statusDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������º��� ������� ���뺸��
			else if("PJS_VA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_view.jsp").forward(request,response);
			}
			//�������º��� �μ����� ��ü LIST��������
			else if("PJS_LD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = statusDAO.getCurrentPage();		//����������
				int Tpage = statusDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�������º��� �μ����� �������밡������
			else if("PJS_MVD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_modify.jsp").forward(request,response);
			}
			//�������º��� �μ����� �������밡������
			else if("PJS_VD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_modify.jsp").forward(request,response);
			}
			//�������º��� �μ����� �����ϱ�
			else if("PJS_MD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					statusDAO.updateProject(pjt_code,pjt_status,note,in_date);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = statusDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = statusDAO.getCurrentPage();		//����������
					int Tpage = statusDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������º��� �μ����� �����ϱ�
			else if("PJS_DD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					statusDAO.deleteProject(pjt_code);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = statusDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = statusDAO.getCurrentPage();		//����������
					int Tpage = statusDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������º��� �μ����� ���뺸��
			else if("PJS_VD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_view.jsp").forward(request,response);
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

