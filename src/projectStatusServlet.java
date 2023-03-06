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

public class projectStatusServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PJS_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");
		
		//과제상태 변경 수정/삭제 데이터 받기
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//관리코드
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));			//phase code
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));			//phase name
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));		//step code
		String mgr_id = Hanguel.toHanguel(request.getParameter("mgr_id"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_id"));		//step name
		String mgr_name = Hanguel.toHanguel(request.getParameter("mgr_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_name"));			//activity code
		String pjt_status = Hanguel.toHanguel(request.getParameter("pjt_status"))==null?"S":Hanguel.toHanguel(request.getParameter("pjt_status"));			//산출물 코드
		String type = Hanguel.toHanguel(request.getParameter("type"))==null?"":Hanguel.toHanguel(request.getParameter("type"));						//전사는P, 부서는 부서코드
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"":Hanguel.toHanguel(request.getParameter("note"));						//전사는P, 부서는 부서코드
		
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
			//	과제상태변경 LIST가져오기
			//--------------------------------------------------------------------
			//과제상태변경 전사공통 전체 LIST가져오기
			if("PJS_LA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = statusDAO.getCurrentPage();		//현재페이지
				int Tpage = statusDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//과제상태변경 전사공통 내용보기
			else if("PJS_VA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_view.jsp").forward(request,response);
			}
			//과제상태변경 부서공통 전체 LIST가져오기
			else if("PJS_LD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = statusDAO.getCurrentPage();		//현재페이지
				int Tpage = statusDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//과제상태변경 부서공통 내용보기
			else if("PJS_VD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_view.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PJS_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//과제상태 변경 수정/삭제 데이터 받기
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//관리코드
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));			//phase code
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));			//phase name
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));		//step code
		String mgr_id = Hanguel.toHanguel(request.getParameter("mgr_id"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_id"));		//step name
		String mgr_name = Hanguel.toHanguel(request.getParameter("mgr_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_name"));			//activity code
		String pjt_status = Hanguel.toHanguel(request.getParameter("pjt_status"))==null?"S":Hanguel.toHanguel(request.getParameter("pjt_status"));			//산출물 코드
		String type = Hanguel.toHanguel(request.getParameter("type"))==null?"":Hanguel.toHanguel(request.getParameter("type"));						//전사는P, 부서는 부서코드
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"":Hanguel.toHanguel(request.getParameter("note"));						//전사는P, 부서는 부서코드
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	과제상태변경 코드 편집하기
			//--------------------------------------------------------------------
			//과제상태변경 전사공통 전체 LIST가져오기
			if("PJS_LA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = statusDAO.getCurrentPage();		//현재페이지
				int Tpage = statusDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//과제상태변경 전사공통 수정내용가져오기
			else if("PJS_MVA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_modify.jsp").forward(request,response);
			}
			//과제상태변경 전사공통 수정하기
			else if("PJS_MA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					statusDAO.updateProject(pjt_code,pjt_status,note,in_date);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = statusDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = statusDAO.getCurrentPage();		//현재페이지
					int Tpage = statusDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제상태변경 전사공통 삭제하기
			else if("PJS_DA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					statusDAO.deleteProject(pjt_code);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					
					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = statusDAO.getAllProjectList(sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = statusDAO.getCurrentPage();		//현재페이지
					int Tpage = statusDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제상태변경 전사공통 내용보기
			else if("PJS_VA".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusAll_view.jsp").forward(request,response);
			}
			//과제상태변경 부서공통 전체 LIST가져오기
			else if("PJS_LD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = statusDAO.getCurrentPage();		//현재페이지
				int Tpage = statusDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//과제상태변경 부서공통 수정내용가져오기
			else if("PJS_MVD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_modify.jsp").forward(request,response);
			}
			//과제상태변경 부서공통 수정내용가져오기
			else if("PJS_VD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_modify.jsp").forward(request,response);
			}
			//과제상태변경 부서공통 수정하기
			else if("PJS_MD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					statusDAO.updateProject(pjt_code,pjt_status,note,in_date);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					
					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = statusDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = statusDAO.getCurrentPage();		//현재페이지
					int Tpage = statusDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제상태변경 부서공통 삭제하기
			else if("PJS_DD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					statusDAO.deleteProject(pjt_code);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					
					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = statusDAO.getDivProjectList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = statusDAO.getCurrentPage();		//현재페이지
					int Tpage = statusDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제상태변경 부서공통 내용보기
			else if("PJS_VD".equals(mode)){	
				com.anbtech.pjt.db.projectStatusDAO statusDAO = new com.anbtech.pjt.db.projectStatusDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = statusDAO.getProjectRead(pjt_code);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/pjtStatusDiv_view.jsp").forward(request,response);
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

