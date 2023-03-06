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

public class prsCodeServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PHA_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"ph_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

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
			//	개발단계[PHASE] 코드 LIST가져오기
			//--------------------------------------------------------------------
			//개발단계(Phase) 전사공통 전체 LIST가져오기
			if("PHA_LA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = phaseDAO.getCurrentPage();		//현재페이지
				int Tpage = phaseDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//개발단계(Phase) 부서공통 전체 LIST가져오기
			else if("PHA_LD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = phaseDAO.getCurrentPage();		//현재페이지
				int Tpage = phaseDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//--------------------------------------------------------------------
			//	관리항목[STEP] 코드 LIST가져오기
			//--------------------------------------------------------------------
			//관리항목(Step) 전사공통 전체 LIST가져오기
			else if("STP_LA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = stepDAO.getCurrentPage();		//현재페이지
				int Tpage = stepDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//관리항목(Step) 부서공통 전체 LIST가져오기
			else if("STP_LD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = stepDAO.getCurrentPage();		//현재페이지
				int Tpage = stepDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//--------------------------------------------------------------------
			//	실행항목[Activity] 코드 LIST가져오기
			//--------------------------------------------------------------------
			//실행항목(Activity) 전사공통 전체 LIST가져오기
			else if("ACT_LA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = actDAO.getCurrentPage();		//현재페이지
				int Tpage = actDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//실행항목(Activity) 부서공통 전체 LIST가져오기
			else if("ACT_LD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = actDAO.getCurrentPage();		//현재페이지
				int Tpage = actDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//--------------------------------------------------------------------
			//	관리항목 산출물 LIST가져오기
			//--------------------------------------------------------------------
			//관리항목 산출물 전사공통 전체 LIST가져오기
			else if("DOC_LA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = docDAO.getCurrentPage();		//현재페이지
				int Tpage = docDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//관리항목 산출물 부서공통 전체 LIST가져오기
			else if("DOC_LD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = docDAO.getCurrentPage();		//현재페이지
				int Tpage = docDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PHA_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"ph_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//정보입력/수정/삭제 데이터 받기
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//관리코드
		String ph_code = Hanguel.toHanguel(request.getParameter("ph_code"))==null?"":Hanguel.toHanguel(request.getParameter("ph_code"));			//phase code
		String ph_name = Hanguel.toHanguel(request.getParameter("ph_name"))==null?"":Hanguel.toHanguel(request.getParameter("ph_name"));			//phase name
		String step_code = Hanguel.toHanguel(request.getParameter("step_code"))==null?"":Hanguel.toHanguel(request.getParameter("step_code"));		//step code
		String step_name = Hanguel.toHanguel(request.getParameter("step_name"))==null?"":Hanguel.toHanguel(request.getParameter("step_name"));		//step name
		String act_code = Hanguel.toHanguel(request.getParameter("act_code"))==null?"":Hanguel.toHanguel(request.getParameter("act_code"));			//activity code
		String act_name = Hanguel.toHanguel(request.getParameter("act_name"))==null?"":Hanguel.toHanguel(request.getParameter("act_name"));			//activity name
		String doc_code = Hanguel.toHanguel(request.getParameter("doc_code"))==null?"":Hanguel.toHanguel(request.getParameter("doc_code"));			//산출물 코드
		String doc_name = Hanguel.toHanguel(request.getParameter("doc_name"))==null?"":Hanguel.toHanguel(request.getParameter("doc_name"));			//산출물 이름
		String prs_code = Hanguel.toHanguel(request.getParameter("prs_code"))==null?"":Hanguel.toHanguel(request.getParameter("prs_code"));			//프로세스 코드
		String prs_name = Hanguel.toHanguel(request.getParameter("prs_name"))==null?"":Hanguel.toHanguel(request.getParameter("prs_name"));			//프로세스 이름
		String parent_node = Hanguel.toHanguel(request.getParameter("parent_node"))==null?"":Hanguel.toHanguel(request.getParameter("parent_node"));//모노드
		String child_node = Hanguel.toHanguel(request.getParameter("child_node"))==null?"":Hanguel.toHanguel(request.getParameter("child_node"));	//자노드
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));		//노드명
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));			//모자노드 구성번호
		String dip_no = Hanguel.toHanguel(request.getParameter("dip_no"))==null?"":Hanguel.toHanguel(request.getParameter("dip_no"));				//화면출력 순서
		String type = Hanguel.toHanguel(request.getParameter("type"))==null?"":Hanguel.toHanguel(request.getParameter("type"));						//전사는P, 부서는 부서코드
		
		//전사공통A, 부서공통D
		String tag = Hanguel.toHanguel(request.getParameter("tag"))==null?"":Hanguel.toHanguel(request.getParameter("tag"));						
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	개발단계[PHASE] 코드 편집하기
			//--------------------------------------------------------------------
			//개발단계(Phase) 전사공통 전체 LIST가져오기
			if("PHA_LA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = phaseDAO.getCurrentPage();		//현재페이지
				int Tpage = phaseDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//개발단계(Phase) 전사공통 신규입력하기
			else if("PHA_WA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					phaseDAO.inputPhase(pid,ph_code,ph_name,type);
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = phaseDAO.getCurrentPage();		//현재페이지
					int Tpage = phaseDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//개발단계(Phase) 전사공통 수정내용가져오기
			else if("PHA_VA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/phaseAll_modify.jsp").forward(request,response);
			}
			//개발단계(Phase) 전사공통 수정하기
			else if("PHA_MA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = phaseDAO.updatePhase(pid,ph_code,ph_name,type);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('개발단계코드가 중복되어 수정할 수 없습니다.');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = phaseDAO.getCurrentPage();		//현재페이지
						int Tpage = phaseDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//개발단계(Phase) 전사공통 삭제하기
			else if("PHA_DA".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = phaseDAO.deletePhase(pid);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('관리항목과 연결되어 삭제할 수 없습니다');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = phaseDAO.getPhaseAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = phaseDAO.getCurrentPage();		//현재페이지
						int Tpage = phaseDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/phaseAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//개발단계(Phase) 부서공통 전체 LIST가져오기
			else if("PHA_LD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = phaseDAO.getCurrentPage();		//현재페이지
				int Tpage = phaseDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//개발단계(Phase) 부서공통 신규입력하기
			else if("PHA_WD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					phaseDAO.inputPhase(pid,ph_code,ph_name,type);
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = phaseDAO.getCurrentPage();		//현재페이지
					int Tpage = phaseDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//개발단계(Phase) 부서공통 수정내용가져오기
			else if("PHA_VD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = phaseDAO.getPhaseRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_modify.jsp").forward(request,response);
			}
			//개발단계(Phase) 부서공통 수정하기
			else if("PHA_MD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = phaseDAO.updatePhase(pid,ph_code,ph_name,type);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('개발단계코드가 중복되어 수정할 수 없습니다.');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = phaseDAO.getCurrentPage();		//현재페이지
						int Tpage = phaseDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//개발단계(Phase) 부서공통 삭제하기
			else if("PHA_DD".equals(mode)){	
				com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = phaseDAO.deletePhase(pid);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('관리항목과 연결되어 삭제할 수 없습니다');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = phaseDAO.getPhaseDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = phaseDAO.getCurrentPage();		//현재페이지
						int Tpage = phaseDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/phaseDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}

			//--------------------------------------------------------------------
			//	관리항목[STEP] 코드 편집하기
			//--------------------------------------------------------------------
			//관리항목(Step) 전사공통 전체 LIST가져오기
			else if("STP_LA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = stepDAO.getCurrentPage();		//현재페이지
				int Tpage = stepDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//관리항목(Step) 전사공통 신규입력하기
			else if("STP_WA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stepDAO.inputStep(pid,ph_code,step_code,step_name,type);
					stepDAO.updateStepCode (login_id,type,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = stepDAO.getCurrentPage();		//현재페이지
					int Tpage = stepDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//관리항목(Step) 전사공통 수정내용가져오기
			else if("STP_VA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/stepAll_modify.jsp").forward(request,response);
			}
			//관리항목(Step) 전사공통 수정하기
			else if("STP_MA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stepDAO.updateStep(pid,ph_code,step_code,step_name,type);
					stepDAO.updateStepCode (login_id,type,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = stepDAO.getCurrentPage();		//현재페이지
					int Tpage = stepDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//관리항목(Step) 전사공통 삭제하기
			else if("STP_DA".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = stepDAO.deleteStep(pid);
					if(data == true) stepDAO.updateStepCode (login_id,type,tag);	//노드코드 순차적으로 update
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('실행항목과 연결되어 삭제할 수 없습니다');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = stepDAO.getStepAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = stepDAO.getCurrentPage();		//현재페이지
						int Tpage = stepDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/stepAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//관리항목(Step) 부서공통 전체 LIST가져오기
			else if("STP_LD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = stepDAO.getCurrentPage();		//현재페이지
				int Tpage = stepDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//관리항목(Step) 부서공통 신규입력하기
			else if("STP_WD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stepDAO.inputStep(pid,ph_code,step_code,step_name,type);
					stepDAO.updateStepCode (login_id,type,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = stepDAO.getCurrentPage();		//현재페이지
					int Tpage = stepDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//관리항목(Step) 부서공통 수정내용가져오기
			else if("STP_VD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = stepDAO.getStepRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/stepDiv_modify.jsp").forward(request,response);
			}
			//관리항목(Step) 부서공통 수정하기
			else if("STP_MD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stepDAO.updateStep(pid,ph_code,step_code,step_name,type);
					stepDAO.updateStepCode (login_id,type,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = stepDAO.getCurrentPage();		//현재페이지
					int Tpage = stepDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//관리항목(Step) 부서공통 삭제하기
			else if("STP_DD".equals(mode)){	
				com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = stepDAO.deleteStep(pid);
					if(data == true) stepDAO.updateStepCode (login_id,type,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('실행항목과 연결되어 삭제할 수 없습니다');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = stepDAO.getStepDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = stepDAO.getCurrentPage();		//현재페이지
						int Tpage = stepDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/stepDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}

			//--------------------------------------------------------------------
			//	실행항목[activity] 코드 편집하기
			//--------------------------------------------------------------------
			//실행항목(activity) 전사공통 전체 LIST가져오기
			else if("ACT_LA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = actDAO.getCurrentPage();		//현재페이지
				int Tpage = actDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//실행항목(activity) 전사공통 신규입력하기
			else if("ACT_WA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.inputActivity(pid,ph_code,step_code,act_code,act_name,type);
					actDAO.updateActivityCode(login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//현재페이지
					int Tpage = actDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//실행항목(activity) 전사공통 수정내용가져오기
			else if("ACT_VA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/activityAll_modify.jsp").forward(request,response);
			}
			//실행항목(activity) 전사공통 수정하기
			else if("ACT_MA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.updateActivity(pid,ph_code,step_code,act_code,act_name,type);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//현재페이지
					int Tpage = actDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//실행항목(activity) 전사공통 삭제하기
			else if("ACT_DA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.deleteActivity(pid);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//현재페이지
					int Tpage = actDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/activityAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//실행항목(activity) 부서공통 전체 LIST가져오기
			else if("ACT_LD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = actDAO.getCurrentPage();		//현재페이지
				int Tpage = actDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//실행항목(activity) 부서공통 신규입력하기
			else if("ACT_WD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.inputActivity(pid,ph_code,step_code,act_code,act_name,type);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//현재페이지
					int Tpage = actDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//실행항목(activity) 부서공통 수정내용가져오기
			else if("ACT_VD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = actDAO.getActivityRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/activityDiv_modify.jsp").forward(request,response);
			}
			//실행항목(activity) 부서공통 수정하기
			else if("ACT_MD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.updateActivity(pid,ph_code,step_code,act_code,act_name,type);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//현재페이지
					int Tpage = actDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//실행항목(activity) 부서공통 삭제하기
			else if("ACT_DD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDAO actDAO = new com.anbtech.pjt.db.prsActivityDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					actDAO.deleteActivity(pid);
					actDAO.updateActivityCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = actDAO.getActivityDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = actDAO.getCurrentPage();		//현재페이지
					int Tpage = actDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/activityDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//--------------------------------------------------------------------
			//	관리항목 산출물 편집하기
			//--------------------------------------------------------------------
			//관리항목 산출물 전사공통 전체 LIST가져오기
			else if("DOC_LA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = docDAO.getCurrentPage();		//현재페이지
				int Tpage = docDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//관리항목 산출물 전사공통 신규입력하기
			else if("DOC_WA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.inputActivityDoc(pid,ph_code,step_code,doc_code,doc_name,type);
					docDAO.updateActivityDocCode(login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//관리항목 산출물 전사공통 수정내용가져오기
			else if("DOC_VA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/docAll_modify.jsp").forward(request,response);
			}
			//관리항목 산출물 전사공통 수정하기
			else if("DOC_MA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.updateActivityDoc(pid,ph_code,step_code,doc_code,doc_name,type);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//관리항목 산출물 전사공통 삭제하기
			else if("DOC_DA".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.deleteActivityDoc(pid);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/docAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//관리항목 산출물 부서공통 전체 LIST가져오기
			else if("DOC_LD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = docDAO.getCurrentPage();		//현재페이지
				int Tpage = docDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//관리항목 산출물 부서공통 신규입력하기
			else if("DOC_WD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.inputActivityDoc(pid,ph_code,step_code,doc_code,doc_name,type);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//관리항목 산출물 부서공통 수정내용가져오기
			else if("DOC_VD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getActivityDocRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/docDiv_modify.jsp").forward(request,response);
			}
			//관리항목 산출물 부서공통 수정하기
			else if("DOC_MD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.updateActivityDoc(pid,ph_code,step_code,doc_code,doc_name,type);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//관리항목 산출물 부서공통 삭제하기
			else if("DOC_DD".equals(mode)){	
				com.anbtech.pjt.db.prsActivityDocDAO docDAO = new com.anbtech.pjt.db.prsActivityDocDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					docDAO.deleteActivityDoc(pid);
					docDAO.updateActivityDocCode (login_id,type,step_code,tag);					//노드코드 순차적으로 update
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getActivityDocDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/docDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
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

