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

public class projectNoteServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PNT_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"note":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"1":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//������ ���� �Ķ����
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String users = Hanguel.toHanguel(request.getParameter("users"))==null?"0":Hanguel.toHanguel(request.getParameter("users"));
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"0":Hanguel.toHanguel(request.getParameter("in_date"));
		String book_date = Hanguel.toHanguel(request.getParameter("book_date"))==null?"":Hanguel.toHanguel(request.getParameter("book_date"));
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"0":Hanguel.toHanguel(request.getParameter("note"));
		String solution = Hanguel.toHanguel(request.getParameter("solution"))==null?"0":Hanguel.toHanguel(request.getParameter("solution"));
		String content = Hanguel.toHanguel(request.getParameter("content"))==null?"0":Hanguel.toHanguel(request.getParameter("content"));
		String sol_date = Hanguel.toHanguel(request.getParameter("sol_date"))==null?"0":Hanguel.toHanguel(request.getParameter("sol_date"));
		String note_status = Hanguel.toHanguel(request.getParameter("note_status"))==null?"0":Hanguel.toHanguel(request.getParameter("note_status"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"0":Hanguel.toHanguel(request.getParameter("pid"));
		
		//�����ֱ��� ���� �б�
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
	
		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setNoteStatus(note_status);		//note ����
			table.setPjtword(pjtWord);				//��������code �� ��������(����,������,�̽���)
			table.setNodeCode(node_code);			//����ڵ�
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
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
			//	�������������� LIST��������
			//--------------------------------------------------------------------
			//�������������� LIST��������
			if("PNT_L".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//�ش�PM�� ��ü����List
				ArrayList pjt_list = new ArrayList();
				pjt_list = schDAO.getProjectList(login_id,pjtWord,"pjt_code","");
				request.setAttribute("PJT_List", pjt_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator pjt_iter = pjt_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//�ش� ���������� List
				ArrayList table_list = new ArrayList();
				table_list = noteDAO.getNoteList(pjt_code,note_status,sItem,sWord,page,max_display_cnt);
				request.setAttribute("NOTE_List", table_list);

				int Cpage = noteDAO.getCurrentPage();	//����������
				int Tpage = noteDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�������������� : �ֱٿ� ������/���δ�� ���� �۾����� ���� (������ ���/���� �����׸�)
			else if("PNT_WV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//������ or ���δ������ ��� �����ϱ�
				ArrayList act_list = new ArrayList();
				act_list = noteDAO.getWorkActivity(pjt_code);
				request.setAttribute("ACT_List", act_list);
	
				//�������� �����׸� ã�Ƴ���
				Iterator act_iter = act_list.iterator();
				if(act_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)act_iter.next();
					if(node_code.length() == 0) {
						node_code = table.getChildNode();
						in_date = noteDAO.getLastDate (pjt_code,node_code);
					}
				}

				//������ ������������
				ArrayList work_list = new ArrayList();
				work_list = noteDAO.getLastWork(pjt_code,node_code,in_date);
				request.setAttribute("WORK_List", work_list);

				//������ ������ ��������
				ArrayList indate_list = new ArrayList();
				indate_list = noteDAO.getInDate(pjt_code,node_code);
				request.setAttribute("INDATE_List", indate_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_work.jsp").forward(request,response);
			}
			//�������������� : ������ ����غ�
			else if("PNT_NV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//�ش���� ��ü��� �����ϱ�
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//�η¸�� ��������
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_write.jsp?RD=").forward(request,response);
			}
			//�������������� : ������ �����غ�
			else if("PNT_MV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//�ش���� ��ü��� �����ϱ�
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//�η¸�� ��������
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//�ش繮���� ���� �б�
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_modify.jsp?RD=").forward(request,response);
			}
			//�������������� : ������ �ذ᳻�� �ۼ� �غ�
			else if("PNT_CV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//�ش���� ��ü��� �����ϱ�
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//�η¸�� ��������
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//�ش繮���� ���� �б�
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_solution.jsp?RD=").forward(request,response);
			}
			//�������������� : �������ذ� ���뺸��
			else if("PNT_SV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//�ش繮���� ���� �б�
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator note_iter = note_list.iterator();
				if(note_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)note_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//�ش���� ��ü��� �����ϱ�
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//�η¸�� ��������
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_view.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PNT_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//������ ���� �Ķ����
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String users = Hanguel.toHanguel(request.getParameter("users"))==null?"0":Hanguel.toHanguel(request.getParameter("users"));
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));
		String book_date = Hanguel.toHanguel(request.getParameter("book_date"))==null?"":Hanguel.toHanguel(request.getParameter("book_date"));
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"0":Hanguel.toHanguel(request.getParameter("note"));
		String solution = Hanguel.toHanguel(request.getParameter("solution"))==null?"":Hanguel.toHanguel(request.getParameter("solution"));
		String content = Hanguel.toHanguel(request.getParameter("content"))==null?"":Hanguel.toHanguel(request.getParameter("content"));
		String sol_date = Hanguel.toHanguel(request.getParameter("sol_date"))==null?"":Hanguel.toHanguel(request.getParameter("sol_date"));
		String note_status = Hanguel.toHanguel(request.getParameter("note_status"))==null?"0":Hanguel.toHanguel(request.getParameter("note_status"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"0":Hanguel.toHanguel(request.getParameter("pid"));
		
		//�����ֱ��� ���� �б�
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
	
		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setNoteStatus(note_status);		//note ����
			table.setPjtword(pjtWord);				//��������code �� ��������(����,������,�̽���)
			table.setNodeCode(node_code);			//����ڵ�
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	�������������� LIST��������
			//--------------------------------------------------------------------
			//�������������� LIST��������
			if("PNT_L".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//�ش�PM�� ��ü����List
				ArrayList pjt_list = new ArrayList();
				pjt_list = schDAO.getProjectList(login_id,pjtWord,"pjt_code","");
				request.setAttribute("PJT_List", pjt_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator pjt_iter = pjt_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//�ش� ���������� List
				ArrayList table_list = new ArrayList();
				table_list = noteDAO.getNoteList(pjt_code,note_status,sItem,sWord,page,max_display_cnt);
				request.setAttribute("NOTE_List", table_list);

				int Cpage = noteDAO.getCurrentPage();	//����������
				int Tpage = noteDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//�������������� : �ֱٿ� ������/���δ�� ���� �۾����� ���� (������ ���/���� �����׸�)
			else if("PNT_WV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//������ or ���δ������ ��� �����ϱ�
				ArrayList act_list = new ArrayList();
				act_list = noteDAO.getWorkActivity(pjt_code);
				request.setAttribute("ACT_List", act_list);

				//�������� �����׸� ã�Ƴ���
				Iterator act_iter = act_list.iterator();
				if(act_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)act_iter.next();
					if(node_code.length() == 0) {
						node_code = table.getChildNode();
						in_date = noteDAO.getLastDate (pjt_code,node_code);
					}
				}

				//������ ������������
				ArrayList work_list = new ArrayList();
				work_list = noteDAO.getLastWork(pjt_code,node_code,in_date);
				request.setAttribute("WORK_List", work_list);

				//������ ������ ��������
				ArrayList indate_list = new ArrayList();
				indate_list = noteDAO.getInDate(pjt_code,node_code);
				request.setAttribute("INDATE_List", indate_list);
			
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_work.jsp").forward(request,response);
			}
			//�������������� : ������ ����غ�
			else if("PNT_NV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//�ش���� ��ü��� �����ϱ�
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//�η¸�� ��������
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_write.jsp?RD=").forward(request,response);
			}
			//�������������� : ������ ����ϱ�
			else if("PNT_NW".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//�⺻���� �Է�/�����Ҷ�
					noteDAO.inputNote(pid,pjt_code,pjt_name,node_code,users,in_date,book_date,note,solution,note_status);
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�ش���� ��ü��� �����ϱ�
					ArrayList node_list = new ArrayList();
					node_list = noteDAO.getNodeList(pjt_code);
					request.setAttribute("NODE_List", node_list);

					//�η¸�� ��������
					ArrayList man_list = new ArrayList();
					man_list = noteDAO.getPjtMember(pjt_code);
					request.setAttribute("MAN_List", man_list);
					
					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/pm/projectNote_write.jsp?RD=R").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������������� : ������ �����غ�
			else if("PNT_MV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//�ش���� ��ü��� �����ϱ�
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//�η¸�� ��������
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//�ش繮���� ���� �б�
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_modify.jsp?RD=").forward(request,response);
			}
			//�������������� : ������ �����ϱ�
			else if("PNT_MW".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//�⺻���� �Է�/�����Ҷ�
					noteDAO.updateNote(pid,node_code,book_date,users,note,solution);
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��ϱ�
					String strL = "&pjt_code="+pjt_code+"&note_status=0&sItem=note&sWord=&page=1";
					out.println("	<script>");
					out.println("	parent.location.href('../servlet/projectNoteServlet?mode=PNT_L"+strL+"');");
					out.println("	</script>");
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������������� : ������ �����ϱ�
			else if("PNT_D".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//�⺻���� �Է�/�����Ҷ�
					noteDAO.deleteNote(pid);
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�ش�PM�� ��ü����List
					ArrayList pjt_list = new ArrayList();
					pjt_list = schDAO.getProjectList(login_id,pjtWord,"pjt_code","");
					request.setAttribute("PJT_List", pjt_list);

					//�����ڵ� �����׸� ã�Ƴ���
					Iterator pjt_iter = pjt_list.iterator();
					if(pjt_iter.hasNext()) {
						table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
						if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
					}

					//�ش� ���������� List
					ArrayList table_list = new ArrayList();
					table_list = noteDAO.getNoteList(pjt_code,note_status,sItem,sWord,page,max_display_cnt);
					request.setAttribute("NOTE_List", table_list);

					int Cpage = noteDAO.getCurrentPage();	//����������
					int Tpage = noteDAO.getTotalPage();		//��ü������

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/pm/projectNote_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}
			}
			//�������������� : �������ذ� �ۼ��غ�
			else if("PNT_CV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//�ش���� ��ü��� �����ϱ�
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//�η¸�� ��������
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//�ش繮���� ���� �б�
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_solution.jsp?RD=").forward(request,response);
			}
			//�������������� : �������ذ� �ۼ� �����ϱ�
			else if("PNT_CW".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//�⺻���� �Է�/�����Ҷ�
					noteDAO.updateContent(pid,content,sol_date,note_status);
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��ϱ�
					String strL = "&pjt_code="+pjt_code+"&note_status=1&sItem=note&sWord=&page=1";
					out.println("	<script>");
					out.println("	parent.location.href('../servlet/projectNoteServlet?mode=PNT_L"+strL+"');");
					out.println("	</script>");
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������������� : �������ذ� �ۼ� ����ϱ�
			else if("PNT_RW".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//�⺻���� �Է�/�����Ҷ�
					noteDAO.updateRecovery(pid);
					
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��ϱ�
					String strL = "&pjt_code="+pjt_code+"&note_status=0&sItem=note&sWord=&page=1";
					out.println("	<script>");
					out.println("	location.href('../servlet/projectNoteServlet?mode=PNT_L"+strL+"');");
					out.println("	</script>");
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������������� : �������ذ� ���뺸��
			else if("PNT_SV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//�ش���� ��ü��� �����ϱ�
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//�η¸�� ��������
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//�ش繮���� ���� �б�
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_view.jsp").forward(request,response);
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



