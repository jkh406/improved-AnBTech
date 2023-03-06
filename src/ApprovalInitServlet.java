import com.anbtech.gw.entity.*;
import com.anbtech.gw.db.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.anbtech.admin.SessionLib;

import com.oreilly.servlet.MultipartRequest;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ApprovalInitServlet extends HttpServlet {
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
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");

		try {
			// connection ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			//���ڰ��� �޴��� ����
			//-------------------------------------
			if("menu".equals(mode)){
				com.anbtech.gw.db.AppMenuDAO menuDAO = new com.anbtech.gw.db.AppMenuDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = menuDAO.getTotal(login_id);
				request.setAttribute("Table_List", table_list);	//���ڰ��� ����

				getServletContext().getRequestDispatcher("/gw/approval/ApprovalLeft.jsp").forward(request,response);
				
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
}