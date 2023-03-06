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

public class projectChangeSchServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSC_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//�������� ������� ���� �Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String chg_note = Hanguel.toHanguel(request.getParameter("chg_note"))==null?"":Hanguel.toHanguel(request.getParameter("chg_note"));	
		
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
			//	�������� �̷����� LIST��������
			//--------------------------------------------------------------------
			//�������� �̷����� �󼼺���
			if("PCS_V".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);
				
				//�ش纯����̷� ����
				ArrayList sch_list = new ArrayList();
				sch_list = chgDAO.getChangeSchRead(pid);
				request.setAttribute("SCH_List", sch_list); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectChangeSch_view.jsp").forward(request,response);
			}
			//�������� �̷����� �󼼺��� : �����غ�
			else if("PCS_MV".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);
				
				//�ش纯����̷� ����
				ArrayList sch_list = new ArrayList();
				sch_list = chgDAO.getChangeSchRead(pid);
				request.setAttribute("SCH_List", sch_list); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectChangeSch_modify.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PNP_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//�������� ������� ���� �Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String chg_note = Hanguel.toHanguel(request.getParameter("chg_note"))==null?"":Hanguel.toHanguel(request.getParameter("chg_note"));	

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	�������� �̷����� LIST��������
			//--------------------------------------------------------------------
			//�������� �̷����� �󼼺���
			if("PCS_V".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);
				
				//�ش纯����̷� ����
				ArrayList sch_list = new ArrayList();
				sch_list = chgDAO.getChangeSchRead(pid);
				request.setAttribute("SCH_List", sch_list); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectChangeSch_view.jsp").forward(request,response);
			}
			//�������� �̷����� �󼼺��� : �����غ�
			else if("PCS_MV".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);
				
				//�ش纯����̷� ����
				ArrayList sch_list = new ArrayList();
				sch_list = chgDAO.getChangeSchRead(pid);
				request.setAttribute("SCH_List", sch_list); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectChangeSch_modify.jsp").forward(request,response);
			}
			//�������� �̷����� �󼼺��� : �����ϱ�
			else if("PCS_M".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					chgDAO.updateChangeSch(pid,chg_note);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�ش纯����̷� ����
					ArrayList sch_list = new ArrayList();
					sch_list = chgDAO.getChangeSchRead(pid);
					request.setAttribute("SCH_List", sch_list); 

					//�б��ϱ�
					//ó���޽��� ����ϱ�
					out.println("<script>	alert('�����Ǿ����ϴ�.'); window.returnValue='RL'; self.close(); </script>");	out.close();
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



