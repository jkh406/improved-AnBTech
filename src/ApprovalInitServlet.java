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

	//소멸자 = con소멸
	public void close(Connection con) throws ServletException {
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		
		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;
	
		//모드 및 현재 페이지 파리미터
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");

		try {
			// connection 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			//전자결재 메뉴로 가기
			//-------------------------------------
			if("menu".equals(mode)){
				com.anbtech.gw.db.AppMenuDAO menuDAO = new com.anbtech.gw.db.AppMenuDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = menuDAO.getTotal(login_id);
				request.setAttribute("Table_List", table_list);	//전자결재 수량

				getServletContext().getRequestDispatcher("/gw/approval/ApprovalLeft.jsp").forward(request,response);
				
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			//con소멸
			close(con);
			out.close();
		}
	}
}