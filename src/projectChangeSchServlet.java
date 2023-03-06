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
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리 (목록보기)
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//현재 접속중인 사용자 아이디 가져오기
		//세션이 종료되었을 경우 로긴 페이지로 강제 이동시킨다.
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

		//모드 및 현재 페이지 파리미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSC_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//변경일정 변경사유 정보 파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String chg_note = Hanguel.toHanguel(request.getParameter("chg_note"))==null?"":Hanguel.toHanguel(request.getParameter("chg_note"));	
		
		//메뉴사용 권한정보 알아보기
		String mgr_mode = Hanguel.toHanguel(request.getParameter("mgr_mode"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_mode"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	메뉴사용 권한 검사하기
			//--------------------------------------------------------------------
			com.anbtech.pjt.db.pjtUseMgrDAO mgrDAO = new com.anbtech.pjt.db.pjtUseMgrDAO(con);
			boolean mgr = mgrDAO.getUseMgr(login_id,mgr_mode);
			if(!mgr) {
				out.println("	<script>");
				out.println("	alert('사용권한이 없습니다.');");
				out.println("	parent.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/pjt/pjtBody.jsp');");
				out.println("	</script>");
				out.close();
			}

			//--------------------------------------------------------------------
			//	일정변경 이력정보 LIST가져오기
			//--------------------------------------------------------------------
			//일정변경 이력정보 상세보기
			if("PCS_V".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);
				
				//해당변경상세이력 일정
				ArrayList sch_list = new ArrayList();
				sch_list = chgDAO.getChangeSchRead(pid);
				request.setAttribute("SCH_List", sch_list); 

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectChangeSch_view.jsp").forward(request,response);
			}
			//일정변경 이력정보 상세보기 : 수정준비
			else if("PCS_MV".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);
				
				//해당변경상세이력 일정
				ArrayList sch_list = new ArrayList();
				sch_list = chgDAO.getChangeSchRead(pid);
				request.setAttribute("SCH_List", sch_list); 

				//분기하기
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
	 * post방식으로 넘어왔을 때 처리 (입력,수정,삭제,상신)
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//현재 접속중인 사용자 아이디 가져오기
		//세션이 종료되었을 경우 로긴 페이지로 강제 이동시킨다.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	alert('사용자 정보가 없습니다. 초기 화면으로 이동합니다.\n\n이 메시지는 아래와 같은 경우에 나타납니다.\n1.로그인 후 장시간 아무 작업을 하지 않아서 자동 종료된 경우\n2.정상적으로 로그인하지 않고 특정 페이지를 엑세스하려고 시도한 경우');");
			out.println("	top.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/new/index.html');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;
		
		//모드 및 현재 페이지 파리미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PNP_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//변경일정 변경사유 정보 파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String chg_note = Hanguel.toHanguel(request.getParameter("chg_note"))==null?"":Hanguel.toHanguel(request.getParameter("chg_note"));	

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	일정변경 이력정보 LIST가져오기
			//--------------------------------------------------------------------
			//일정변경 이력정보 상세보기
			if("PCS_V".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);
				
				//해당변경상세이력 일정
				ArrayList sch_list = new ArrayList();
				sch_list = chgDAO.getChangeSchRead(pid);
				request.setAttribute("SCH_List", sch_list); 

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectChangeSch_view.jsp").forward(request,response);
			}
			//일정변경 이력정보 상세보기 : 수정준비
			else if("PCS_MV".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);
				
				//해당변경상세이력 일정
				ArrayList sch_list = new ArrayList();
				sch_list = chgDAO.getChangeSchRead(pid);
				request.setAttribute("SCH_List", sch_list); 

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectChangeSch_modify.jsp").forward(request,response);
			}
			//일정변경 이력정보 상세보기 : 수정하기
			else if("PCS_M".equals(mode)){	
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					chgDAO.updateChangeSch(pid,chg_note);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//해당변경상세이력 일정
					ArrayList sch_list = new ArrayList();
					sch_list = chgDAO.getChangeSchRead(pid);
					request.setAttribute("SCH_List", sch_list); 

					//분기하기
					//처리메시지 출력하기
					out.println("<script>	alert('수정되었습니다.'); window.returnValue='RL'; self.close(); </script>");	out.close();
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



