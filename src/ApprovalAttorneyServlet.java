import com.anbtech.gw.entity.*;
import com.anbtech.gw.db.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.anbtech.admin.SessionLib;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ApprovalAttorneyServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;

	//�Ҹ��� = con�Ҹ�
	public void close(Connection con) throws ServletException {
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó��
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		
		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;
	
		//��� �� ���� ������ �ĸ�����
		String mode = request.getParameter("mode")==null?"ATT_L":request.getParameter("mode");
		String attorney_id = Hanguel.toHanguel(request.getParameter("attorney_id"))==null?"":Hanguel.toHanguel(request.getParameter("attorney_id"));
		String attorney_name = Hanguel.toHanguel(request.getParameter("attorney_name"))==null?"":Hanguel.toHanguel(request.getParameter("attorney_name"));
		String start_date = Hanguel.toHanguel(request.getParameter("start_date"))==null?"":Hanguel.toHanguel(request.getParameter("start_date"));
		String end_date = Hanguel.toHanguel(request.getParameter("end_date"))==null?"":Hanguel.toHanguel(request.getParameter("end_date"));

		try {
			// connection ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// �븮������ ó��
			//-------------------------------------
			//���� ������ �븮������
			if("ATT_L".equals(mode)){
				com.anbtech.gw.db.AppAttorneyDAO attDAO = new com.anbtech.gw.db.AppAttorneyDAO(con);

				ArrayList table_list = new ArrayList();
				table_list = attDAO.isAttorney(login_id);
				request.setAttribute("Table_List", table_list);	
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_attorney.jsp").forward(request,response);
			
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			//con�Ҹ�
			close(con);
			out.close();
		}
	}

	/**********************************
	 * post������� �Ѿ���� �� ó��
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//��� �� ���� ������ �ĸ�����
		String mode = request.getParameter("mode")==null?"ATT_L":request.getParameter("mode");
		String attorney_id = Hanguel.toHanguel(request.getParameter("attorney_id"))==null?"":Hanguel.toHanguel(request.getParameter("attorney_id"));
		String attorney_name = Hanguel.toHanguel(request.getParameter("attorney_name"))==null?"":Hanguel.toHanguel(request.getParameter("attorney_name"));
		String start_date = Hanguel.toHanguel(request.getParameter("start_date"))==null?"":Hanguel.toHanguel(request.getParameter("start_date"));
		String end_date = Hanguel.toHanguel(request.getParameter("end_date"))==null?"":Hanguel.toHanguel(request.getParameter("end_date"));

		try {
			// connection ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// �븮������ ó��
			//-------------------------------------
			//���� ������ �븮������
			if("ATT_L".equals(mode)){
				com.anbtech.gw.db.AppAttorneyDAO attDAO = new com.anbtech.gw.db.AppAttorneyDAO(con);

				ArrayList table_list = new ArrayList();
				table_list = attDAO.isAttorney(login_id);
				request.setAttribute("Table_List", table_list);	
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_attorney.jsp").forward(request,response);
				
			}
			//�븮������ �����ϱ�
			else if("ATT_U".equals(mode)){
				com.anbtech.gw.db.AppAttorneyDAO attDAO = new com.anbtech.gw.db.AppAttorneyDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					attDAO.inputAttorney(login_id,attorney_id,attorney_name,start_date,end_date);	//�����ϱ�
					
					con.commit();
					con.setAutoCommit(true);

					//����ϵ� �븮������ ����
					ArrayList table_list = new ArrayList();
					table_list = attDAO.isAttorney(login_id);
					request.setAttribute("Table_List", table_list);	
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_attorney.jsp?Rep=U").forward(request,response);
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�븮������ �����ϱ�
			else if("ATT_D".equals(mode)){
				com.anbtech.gw.db.AppAttorneyDAO attDAO = new com.anbtech.gw.db.AppAttorneyDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					attDAO.deleteAttorney(login_id);						//��������
					//attDAO.attorneyDelete(login_id);						//���ڰ��� �̰��Կ� �����ݿ�
			
					con.commit();
					con.setAutoCommit(true);

					//����ϵ� �븮������ ����
					ArrayList table_list = new ArrayList();
					table_list = attDAO.isAttorney(login_id);
					request.setAttribute("Table_List", table_list);	
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_attorney.jsp?Rep=D").forward(request,response);
				} catch(Exception e){
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
			//con�Ҹ�
			close(con);
			out.close();
		}
	}
	//---------------------------------end ----------------------
}
