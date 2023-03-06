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

public class projectEventServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSM_EL":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"evt_content":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"":Hanguel.toHanguel(request.getParameter("pjtWord"));
		String node_status = Hanguel.toHanguel(request.getParameter("node_status"))==null?"":Hanguel.toHanguel(request.getParameter("node_status"));
		
		//���� ���� �Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));	
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));	
		String wm_type = Hanguel.toHanguel(request.getParameter("wm_type"))==null?"":Hanguel.toHanguel(request.getParameter("wm_type"));	
		String evt_content = Hanguel.toHanguel(request.getParameter("evt_content"))==null?"":Hanguel.toHanguel(request.getParameter("evt_content"));	
		String evt_note = Hanguel.toHanguel(request.getParameter("evt_note"))==null?"":Hanguel.toHanguel(request.getParameter("evt_note"));	
		String evt_issue = Hanguel.toHanguel(request.getParameter("evt_issue"))==null?"":Hanguel.toHanguel(request.getParameter("evt_issue"));	

		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setNodeCode(node_code);			//node_code
			table.setPjtword(pjtWord);				//�ְ�/���� ����
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
			table.setNodeStatus(node_status);		//��� ����
		para_list.add(table);
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	����LIST ��������
			//--------------------------------------------------------------------
			if("PSM_EL".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//�ڽ��� ������ �������� ���� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("EVENT_List", table_list);

				//����������/��ü������
				int Cpage = evtDAO.getCurrentPage();	//����������
				int Tpage = evtDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����̷� ��ϵ� ���뺸��
			else if("PSM_EV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//�ش����/��� �Էµ� ��������
				ArrayList work_list = new ArrayList();
				work_list = evtDAO.getEventRead (pid);
				request.setAttribute("WORK_List", work_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_view.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSM_EL":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"evt_content":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		String node_status = Hanguel.toHanguel(request.getParameter("node_status"))==null?"":Hanguel.toHanguel(request.getParameter("node_status"));
		
		//���� ���� �Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));
		String progress = Hanguel.toHanguel(request.getParameter("progress"))==null?"":Hanguel.toHanguel(request.getParameter("progress"));
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));	
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));	
		String wm_type = Hanguel.toHanguel(request.getParameter("wm_type"))==null?"":Hanguel.toHanguel(request.getParameter("wm_type"));	
		String evt_content = Hanguel.toHanguel(request.getParameter("evt_content"))==null?"":Hanguel.toHanguel(request.getParameter("evt_content"));	
		String evt_note = Hanguel.toHanguel(request.getParameter("evt_note"))==null?"":Hanguel.toHanguel(request.getParameter("evt_note"));	
		String evt_issue = Hanguel.toHanguel(request.getParameter("evt_issue"))==null?"":Hanguel.toHanguel(request.getParameter("evt_issue"));	

		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setNodeCode(node_code);			//node_code
			table.setPjtword(pjtWord);				//�ְ�/���� ����
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
			table.setNodeStatus(node_status);		//��� ����
		para_list.add(table);

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	����LIST ��������
			//--------------------------------------------------------------------
			//���� ��üLIST
			if("PSM_EL".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//�ڽ��� ������ �������� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("EVENT_List", table_list);

				//����������/��ü������
				int Cpage = evtDAO.getCurrentPage();	//����������
				int Tpage = evtDAO.getTotalPage();		//��ü������

				//�ش������ Activity�� ����ϱ�
				ArrayList act_list = new ArrayList();
				act_list = evtDAO.getPjtActivityRead (pjt_code);
				request.setAttribute("ACT_List", act_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�����̷� ��� �غ�
			else if("PSM_EWV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//�ش����/��� ����
				ArrayList node_list = new ArrayList();
				node_list = evtDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_write.jsp").forward(request,response);
			}
			//�����̷� ����ϱ�
			else if("PSM_EW".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					evtDAO.inputEvent(pjt_code,pjt_name,node_code,node_name,progress,user_id,user_name,in_date,wm_type,evt_content,evt_note,evt_issue,node_status);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�ڽ��� ������ �������� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("EVENT_List", table_list);

					//����������/��ü������
					int Cpage = evtDAO.getCurrentPage();	//����������
					int Tpage = evtDAO.getTotalPage();		//��ü������

					//�ش������ Activity�� ����ϱ�
					ArrayList act_list = new ArrayList();
					act_list = evtDAO.getPjtActivityRead (pjt_code);
					request.setAttribute("ACT_List", act_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//�����̷� ��ϵ� ���뺸��
			else if("PSM_EV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//�ش����/��� �Էµ� ��������
				ArrayList work_list = new ArrayList();
				work_list = evtDAO.getEventRead (pid);
				request.setAttribute("WORK_List", work_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_view.jsp").forward(request,response);
			}
			//�����̷� �����ϱ� �غ�
			else if("PSM_EMV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//�ش����/��� �Էµ� ��������
				ArrayList work_list = new ArrayList();
				work_list = evtDAO.getEventRead (pid);
				request.setAttribute("WORK_List", work_list);

				//�ش����/��� ����
				ArrayList node_list = new ArrayList();
				node_list = evtDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_modify.jsp").forward(request,response);
			}
			//�����̷� �����ϱ�
			else if("PSM_EM".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					evtDAO.updateEvent(pid,pjt_code,node_code,progress,in_date,wm_type,evt_content,evt_note,evt_issue);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�ڽ��� ������ �������� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("EVENT_List", table_list);

					//����������/��ü������
					int Cpage = evtDAO.getCurrentPage();	//����������
					int Tpage = evtDAO.getTotalPage();		//��ü������

					//�ش������ Activity�� ����ϱ�
					ArrayList act_list = new ArrayList();
					act_list = evtDAO.getPjtActivityRead (pjt_code);
					request.setAttribute("ACT_List", act_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�����̷� �����ϱ�
			else if("PSM_ED".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					evtDAO.deleteEvent(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�ڽ��� ������ �������� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("EVENT_List", table_list);

					//����������/��ü������
					int Cpage = evtDAO.getCurrentPage();	//����������
					int Tpage = evtDAO.getTotalPage();		//��ü������

					//�ش������ Activity�� ����ϱ�
					ArrayList act_list = new ArrayList();
					act_list = evtDAO.getPjtActivityRead (pjt_code);
					request.setAttribute("ACT_List", act_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//�ش����/�ش��� �Ϸ���� ��û�غ�
			else if("PSM_EAV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//�ش����/��� ����
				ArrayList node_list = new ArrayList();
				node_list = evtDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_nodeReq.jsp").forward(request,response);
			}
			//�ش����/�ش��� �Ϸ���� ��û�ϱ�
			else if("PSM_EA".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//1.��û�ۼ����� ��� : pjt_event
					evtDAO.inputEvent(pjt_code,pjt_name,node_code,node_name,progress,user_id,user_name,in_date,wm_type,evt_content,evt_note,evt_issue,node_status);
					
					//2.��ûó�� : �Ϸ����� �Է� pjt_schedule
					String ReqApp = evtDAO.nodeAppReq(pjt_code,pjt_name,node_code,node_name,user_id,user_name,evt_content,evt_note,evt_issue);
					if(ReqApp.equals("A")) {	//���οϷ����
						con.rollback();
					}
					else {
						con.commit(); // commit�Ѵ�.
					}
					con.setAutoCommit(true);

					//�ڽ��� ������ �������� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("EVENT_List", table_list);

					//����������/��ü������
					int Cpage = evtDAO.getCurrentPage();	//����������
					int Tpage = evtDAO.getTotalPage();		//��ü������

					//�ش������ Activity�� ����ϱ�
					ArrayList act_list = new ArrayList();
					act_list = evtDAO.getPjtActivityRead (pjt_code);
					request.setAttribute("ACT_List", act_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage+"&ReqApp="+ReqApp).forward(request,response);

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



