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

public class projectManServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PMA_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));

		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);		//pjt_code
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
			//	�η����� LIST��������
			//--------------------------------------------------------------------
			//�ش�PM�� ������ �����η����� LIST��������
			if("PMA_L".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				//�ش�PM�� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					pjt_code = table.getPjtCode();
				}

				//�ش���� ��������ü List
				ArrayList man_list = new ArrayList();
				man_list = manDAO.getProjectRead(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//�ش���� ������ List
				ArrayList one_list = new ArrayList();
				one_list = manDAO.getMemberRead(pid);
				request.setAttribute("ONE_List", one_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectMember_list.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PMA_L":Hanguel.toHanguel(request.getParameter("mode"));
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
		String pjt_mbr_type = Hanguel.toHanguel(request.getParameter("pjt_mbr_type"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_type"));					//pjt_mbr_type
		String mbr_start_date = Hanguel.toHanguel(request.getParameter("mbr_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("mbr_start_date"));			//�Է���
		String mbr_end_date = Hanguel.toHanguel(request.getParameter("mbr_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("mbr_end_date"));			//
		String mbr_poration = Hanguel.toHanguel(request.getParameter("mbr_poration"))==null?"":Hanguel.toHanguel(request.getParameter("mbr_poration"));			//
		String pjt_mbr_id = Hanguel.toHanguel(request.getParameter("pjt_mbr_id"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_id"));			//
		String pjt_mbr_name = Hanguel.toHanguel(request.getParameter("pjt_mbr_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_name"));			//
		String pjt_member = Hanguel.toHanguel(request.getParameter("pjt_member"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_member"));			//
		String pjt_mbr_job = Hanguel.toHanguel(request.getParameter("pjt_mbr_job"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_job"));			//
		String pjt_mbr_tel = Hanguel.toHanguel(request.getParameter("pjt_mbr_tel"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_tel"));			//
		String pjt_mbr_grade = Hanguel.toHanguel(request.getParameter("pjt_mbr_grade"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_grade"));			//
		String plan_end_date = Hanguel.toHanguel(request.getParameter("plan_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_end_date"));			//
		String pjt_mbr_div = Hanguel.toHanguel(request.getParameter("pjt_mbr_div"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_div"));			//

		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);		//pjt_code
			table.setPjtword(pjtWord);		//��������
			table.setSitem(sItem);			//sItem
			table.setSword(sWord);			//sWord
		para_list.add(table);

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	�η����� �����ϱ�
			//--------------------------------------------------------------------
			//�ش�PM�� ������ �����η����� LIST��������
			if("PMA_L".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				//�ش�PM�� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//�����ڵ� �����׸� ã�Ƴ���
				if(pjt_code.length() == 0) {
					Iterator pjt_iter = table_list.iterator();
					if(pjt_iter.hasNext()) {
						table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
						pjt_code = table.getPjtCode();
					}
				}

				//�ش���� ��������ü List
				ArrayList man_list = new ArrayList();
				man_list = manDAO.getProjectRead(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//�ش���� ������ List
				ArrayList one_list = new ArrayList();
				one_list = manDAO.getMemberRead(pid);
				request.setAttribute("ONE_List", one_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
			
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectMember_list.jsp").forward(request,response);
			}
			//�η����� �ű��Է��ϱ�
			else if("PMA_W".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					manDAO.inputMember(pjt_code,pjt_name,pjt_mbr_type,mbr_start_date,mbr_end_date,mbr_poration,pjt_member,pjt_mbr_job);
					con.commit(); // commit�Ѵ�.

					//�ش�PM�� ������ü List
					ArrayList table_list = new ArrayList();
					table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//�ش���� ��������ü List
					ArrayList man_list = new ArrayList();
					man_list = manDAO.getProjectRead(pjt_code);
					request.setAttribute("MAN_List", man_list);

					//�ش���� ������ List
					ArrayList one_list = new ArrayList();
					one_list = manDAO.getMemberRead(pid);
					request.setAttribute("ONE_List", one_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/pm/projectMember_list.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//�η����� �������밡������
			else if("PMA_V".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				//�ش�PM�� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//�ش���� ��������ü List
				ArrayList man_list = new ArrayList();
				man_list = manDAO.getProjectRead(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//�ش���� ������ List
				ArrayList one_list = new ArrayList();
				one_list = manDAO.getMemberRead(pid);
				request.setAttribute("ONE_List", one_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
					
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectMember_modify.jsp").forward(request,response);
			}
			//�η����� �����ϱ�
			else if("PMA_M".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					manDAO.updateMember(pid,pjt_code,pjt_name,pjt_mbr_type,mbr_start_date,mbr_end_date,mbr_poration,pjt_mbr_id,pjt_mbr_name,pjt_mbr_job,pjt_mbr_tel,pjt_mbr_grade,pjt_mbr_div);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�ش�PM�� ������ü List
					ArrayList table_list = new ArrayList();
					table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//�ش���� ��������ü List
					ArrayList man_list = new ArrayList();
					man_list = manDAO.getProjectRead(pjt_code);
					request.setAttribute("MAN_List", man_list);

					//�ش���� ������ List
					ArrayList one_list = new ArrayList();
					one_list = manDAO.getMemberRead(pid);
					request.setAttribute("ONE_List", one_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/pm/projectMember_list.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�η����� ������� �����ϱ�
			else if("PMA_D".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					manDAO.deleteMember(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�ش�PM�� ������ü List
					ArrayList table_list = new ArrayList();
					table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//�ش���� ��������ü List
					ArrayList man_list = new ArrayList();
					man_list = manDAO.getProjectRead(pjt_code);
					request.setAttribute("MAN_List", man_list);

					//�ش���� ������ List
					ArrayList one_list = new ArrayList();
					one_list = manDAO.getMemberRead(pid);
					request.setAttribute("ONE_List", one_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/pm/projectMember_list.jsp").forward(request,response);
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

