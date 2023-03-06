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

	//소멸자 = con소멸
	public void close(Connection con) throws ServletException {
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException{

	}

	/**********************************
	 * post방식으로 넘어왔을 때 처리
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//모드 및 현재 페이지 파리미터
		String pid = request.getParameter("pid")==null?"":request.getParameter("pid");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"app_subj":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//저장된 파일의 root file path
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path");

		try {
			// connection 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// 해당 관리번호 삭제 실행하기
			//-------------------------------------
			com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
			deleteDAO.deletePid(pid,login_id,filepath);	//관리번호,login_id,root_path
			//out.println("<script> alert('"+page+"');  </script>"); out.close();

			//-------------------------------------
			// 삭제실행후 저장함 리스트 보기
			//-------------------------------------
			//임시저장함 : 전자결재 각항목별 전체LIST 보기 모드 
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
			close(con);			//con소멸
			out.close();
		}
	}
}