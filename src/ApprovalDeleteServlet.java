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

public class ApprovalDeleteServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 20;

	//�Ҹ��� = con�Ҹ�
	public void close(Connection con) throws ServletException {
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó��
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException{

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
		String pid = request.getParameter("pid")==null?"":request.getParameter("pid");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"app_subj":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//����� ������ root file path
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path");

		try {
			// connection ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// �ش� ������ȣ ���� �����ϱ�
			//-------------------------------------
			com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
			deleteDAO.deletePid(pid,login_id,filepath);	//������ȣ,login_id,root_path
			//out.println("<script> alert('"+page+"');  </script>"); out.close();

			//-------------------------------------
			// ���������� ������ ����Ʈ ����
			//-------------------------------------
			//�ӽ������� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			com.anbtech.gw.db.AppMasterListDAO masterDAO = new com.anbtech.gw.db.AppMasterListDAO(con);
			ArrayList table_list = new ArrayList();
			table_list = masterDAO.getTable_list (login_id,"TMP_BOX",sItem,sWord,page,max_display_cnt);
			request.setAttribute("Table_List", table_list);	
			int Tpage = masterDAO.getTotalPage();
			int Cpage = masterDAO.getCurrentPage();
				
			getServletContext().getRequestDispatcher("/gw/approval/eleApproval_main.jsp?PROCESS=TMP_BOX&Tpage="+Tpage+"&Cpage="+Cpage).forward(request,response);
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con�Ҹ�
			out.close();
		}
	}
}