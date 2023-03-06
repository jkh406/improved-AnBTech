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
import java.util.StringTokenizer;

public class prsStandardServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSN_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"prs_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//���μ��� ���� �Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"));	
		
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
			//	���μ����� ��� LIST��������
			//--------------------------------------------------------------------
			//���μ����� ������� ��ü LIST��������
			if("PSN_LA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = pnameDAO.getCurrentPage();		//����������
				int Tpage = pnameDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//���μ����� �μ����� ��ü LIST��������
			else if("PSN_LD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = pnameDAO.getCurrentPage();		//����������
				int Tpage = pnameDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}

			//--------------------------------------------------------------------
			//	ǥ�� ���μ��� LIST��������
			//--------------------------------------------------------------------
			//ǥ�� ���μ��� ������뿡 �Է��� LIST��������
			else if("STD_LA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);
				//�������� List
				ArrayList base_list = new ArrayList();
				base_list = stdDAO.getNodeBaseList(pid,"P",login_id);
				request.setAttribute("Base_List", base_list);

				//���μ��� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = stdDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//���⹰ ��ü List
				ArrayList doc_list = new ArrayList();
				doc_list = stdDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//���� ��忡 ��ϵ� ���⹰ ��ü List
				ArrayList save_list = new ArrayList();
				save_list = stdDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp").forward(request,response);
			}
			//ǥ�� ���μ��� �μ����뿡 �Է��� LIST��������
			else if("STD_LD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);
				//�������� List
				ArrayList base_list = new ArrayList();
				base_list = stdDAO.getNodeBaseList(pid,"D",login_id);
				request.setAttribute("Base_List", base_list);

				//���μ��� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = stdDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//���⹰ ��ü List
				ArrayList doc_list = new ArrayList();
				doc_list = stdDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//���� ��忡 ��ϵ� ���⹰ ��ü List
				ArrayList save_list = new ArrayList();
				save_list = stdDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSN_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"prs_code":Hanguel.toHanguel(request.getParameter("sItem"));
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
		
		//���μ��� �����ޱ� [�ڳ�尡 multi�ϰ��]
		String array_cnt = Hanguel.toHanguel(request.getParameter("array_cnt"))==null?"0":Hanguel.toHanguel(request.getParameter("array_cnt"));
		int acnt = Integer.parseInt(array_cnt);
		String[] node = new String[acnt];		//���ޱ� �迭
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		//��� �Ķ���� �ޱ� [���μ���,���⹰����]
		StringTokenizer str = new StringTokenizer(node_code,",");
		int n = 0;
		while(str.hasMoreTokens()) {
			node[n] = str.nextToken();
			n++;
			if(n == acnt) break;
		}
		String spid = Hanguel.toHanguel(request.getParameter("spid"))==null?"":Hanguel.toHanguel(request.getParameter("spid"));

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	���μ����� �ڵ� �����ϱ�
			//--------------------------------------------------------------------
			//���μ����� ������� ��ü LIST��������
			if("PSN_LA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = pnameDAO.getCurrentPage();		//����������
				int Tpage = pnameDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//���μ����� ������� �ű��Է��ϱ�
			else if("PSN_WA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pnameDAO.inputPrsname(pid,prs_code,prs_name,type);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pnameDAO.getCurrentPage();		//����������
					int Tpage = pnameDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���μ����� ������� �������밡������
			else if("PSN_VA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/pnameAll_modify.jsp").forward(request,response);
			}
			//���μ����� ������� �����ϱ�
			else if("PSN_MA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = pnameDAO.updatePrsname(pid,prs_code,prs_name,type);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('���μ����ڵ尡 �ߺ��Ǿ� ������ �� �����ϴ�.');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = pnameDAO.getCurrentPage();		//����������
						int Tpage = pnameDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���μ����� ������� �����ϱ�
			else if("PSN_DA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = pnameDAO.deletePrsname(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('���� ǥ�����μ����� ��ϵǾ� ������ �� �����ϴ�');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = pnameDAO.getCurrentPage();		//����������
						int Tpage = pnameDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���μ����� �μ����� ��ü LIST��������
			else if("PSN_LD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = pnameDAO.getCurrentPage();		//����������
				int Tpage = pnameDAO.getTotalPage();		//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//���μ����� �μ����� �ű��Է��ϱ�
			else if("PSN_WD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pnameDAO.inputPrsname(pid,prs_code,prs_name,type);
					con.commit(); // commit�Ѵ�.

					//��ü List �� �б��ϱ�
					ArrayList table_list = new ArrayList();
					table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pnameDAO.getCurrentPage();		//����������
					int Tpage = pnameDAO.getTotalPage();		//��ü������
					getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//���μ����� �μ����� �������밡������
			else if("PSN_VD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				//�������� �о� �б��ϱ�
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_modify.jsp").forward(request,response);
			}
			//���μ����� �μ����� �����ϱ�
			else if("PSN_MD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = pnameDAO.updatePrsname(pid,prs_code,prs_name,type);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('���μ����ڵ尡 �ߺ��Ǿ� ������ �� �����ϴ�.');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = pnameDAO.getCurrentPage();		//����������
						int Tpage = pnameDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���μ����� �μ����� �����ϱ�
			else if("PSN_DD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = pnameDAO.deletePrsname(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('�μ� ǥ�����μ����� ��ϵǾ� ������ �� �����ϴ�');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//��ü List �� �б��ϱ�
						ArrayList table_list = new ArrayList();
						table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = pnameDAO.getCurrentPage();		//����������
						int Tpage = pnameDAO.getTotalPage();		//��ü������
						getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}	
			//--------------------------------------------------------------------
			//	���μ����� �ڵ� �����ϱ�
			//--------------------------------------------------------------------
			//ǥ�� ���μ��� ������뿡 �Է��� LIST��������
			else if("STD_LA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);
				//�������� List
				ArrayList base_list = new ArrayList();
				base_list = stdDAO.getNodeBaseList(pid,"P",login_id);
				request.setAttribute("Base_List", base_list);

				//���μ��� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = stdDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//���⹰ ��ü List
				ArrayList doc_list = new ArrayList();
				doc_list = stdDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//���� ��忡 ��ϵ� ���⹰ ��ü List
				ArrayList save_list = new ArrayList();
				save_list = stdDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp").forward(request,response);
			}
			//ǥ�����μ��� ������� �ű��Է��ϱ�
			else if("STD_WA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.inputNode(prs_code,parent_node,node,level_no,type);
					con.commit(); // commit�Ѵ�.
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"P",login_id);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp?RD=Reload").forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//ǥ�����μ��� ������� �����ϱ�
			else if("STD_DA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.deleteNode(pid,prs_code,parent_node,node,level_no,type);
					con.commit(); // commit�Ѵ�.
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"P",login_id);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp?RD=Reload&comment="+data).forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//ǥ�����μ��� ���⹰ ������� ������� �ű��Է��ϱ�
			else if("SDC_WA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stdDAO.inputNodeDoc(prs_code,parent_node,node,level_no,type);
					con.commit(); // commit�Ѵ�.
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"P",login_id);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp?RD=").forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//ǥ�����μ��� ���⹰ ������� ������� �����ϱ�
			else if("SDC_DA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.deleteNodeDoc(pid,prs_code,parent_node,node,level_no,type);
					con.commit(); // commit�Ѵ�.
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"P",login_id);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp?RD=&comment="+data).forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//ǥ�� ���μ��� �μ����뿡 �Է��� LIST��������
			else if("STD_LD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);
				//�������� List
				ArrayList base_list = new ArrayList();
				base_list = stdDAO.getNodeBaseList(pid,"D",login_id);
				request.setAttribute("Base_List", base_list);

				//���μ��� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = stdDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//���⹰ ��ü List
				ArrayList doc_list = new ArrayList();
				doc_list = stdDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//���� ��忡 ��ϵ� ���⹰ ��ü List
				ArrayList save_list = new ArrayList();
				save_list = stdDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp").forward(request,response);
			}
			//ǥ�����μ��� �μ����� �ű��Է��ϱ�
			else if("STD_WD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stdDAO.inputNode(prs_code,parent_node,node,level_no,type);
					con.commit(); // commit�Ѵ�.
	
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"D",login_id);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp?RD=Reload").forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//ǥ�����μ��� �μ����� �����ϱ�
			else if("STD_DD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.deleteNode(pid,prs_code,parent_node,node,level_no,type);
					con.commit(); // commit�Ѵ�.
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"D",login_id);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp?RD=Reload&comment="+data).forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//ǥ�����μ��� ���⹰ ������� �μ����� �ű��Է��ϱ�
			else if("SDC_WD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stdDAO.inputNodeDoc(prs_code,parent_node,node,level_no,type);
					con.commit(); // commit�Ѵ�.
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"D",login_id);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp?RD=").forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//ǥ�����μ��� ���⹰ ������� �μ����� �����ϱ�
			else if("SDC_DD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.deleteNodeDoc(pid,prs_code,parent_node,node,level_no,type);
					con.commit(); // commit�Ѵ�.
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"D",login_id);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp?RD=&comment="+data).forward(request,response);

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

