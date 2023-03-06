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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PMA_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));

		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);		//pjt_code
			table.setPjtword(pjtWord);		//과제상태
			table.setSitem(sItem);			//sItem
			table.setSword(sWord);			//sWord
		para_list.add(table);

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
			//	인력정보 LIST가져오기
			//--------------------------------------------------------------------
			//해당PM의 과제및 과제인력정보 LIST가져오기
			if("PMA_L".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				//해당PM의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					pjt_code = table.getPjtCode();
				}

				//해당과제 구성원전체 List
				ArrayList man_list = new ArrayList();
				man_list = manDAO.getProjectRead(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당과제 구성원 List
				ArrayList one_list = new ArrayList();
				one_list = manDAO.getMemberRead(pid);
				request.setAttribute("ONE_List", one_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
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
	 * post방식으로 넘어왔을 때 처리 (입력,수정,삭제)
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PMA_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//기본정보 입력/수정/삭제 데이터 받기
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//관리코드
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));			//phase code
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));			//phase name
		String pjt_mbr_type = Hanguel.toHanguel(request.getParameter("pjt_mbr_type"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_type"));					//pjt_mbr_type
		String mbr_start_date = Hanguel.toHanguel(request.getParameter("mbr_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("mbr_start_date"));			//입력일
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

		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);		//pjt_code
			table.setPjtword(pjtWord);		//과제상태
			table.setSitem(sItem);			//sItem
			table.setSword(sWord);			//sWord
		para_list.add(table);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	인력정보 편집하기
			//--------------------------------------------------------------------
			//해당PM의 과제및 과제인력정보 LIST가져오기
			if("PMA_L".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				//해당PM의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				if(pjt_code.length() == 0) {
					Iterator pjt_iter = table_list.iterator();
					if(pjt_iter.hasNext()) {
						table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
						pjt_code = table.getPjtCode();
					}
				}

				//해당과제 구성원전체 List
				ArrayList man_list = new ArrayList();
				man_list = manDAO.getProjectRead(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당과제 구성원 List
				ArrayList one_list = new ArrayList();
				one_list = manDAO.getMemberRead(pid);
				request.setAttribute("ONE_List", one_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
			
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectMember_list.jsp").forward(request,response);
			}
			//인력정보 신규입력하기
			else if("PMA_W".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					manDAO.inputMember(pjt_code,pjt_name,pjt_mbr_type,mbr_start_date,mbr_end_date,mbr_poration,pjt_member,pjt_mbr_job);
					con.commit(); // commit한다.

					//해당PM의 과제전체 List
					ArrayList table_list = new ArrayList();
					table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//해당과제 구성원전체 List
					ArrayList man_list = new ArrayList();
					man_list = manDAO.getProjectRead(pjt_code);
					request.setAttribute("MAN_List", man_list);

					//해당과제 구성원 List
					ArrayList one_list = new ArrayList();
					one_list = manDAO.getMemberRead(pid);
					request.setAttribute("ONE_List", one_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectMember_list.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//인력정보 수정내용가져오기
			else if("PMA_V".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				//해당PM의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//해당과제 구성원전체 List
				ArrayList man_list = new ArrayList();
				man_list = manDAO.getProjectRead(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당과제 구성원 List
				ArrayList one_list = new ArrayList();
				one_list = manDAO.getMemberRead(pid);
				request.setAttribute("ONE_List", one_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
					
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectMember_modify.jsp").forward(request,response);
			}
			//인력정보 수정하기
			else if("PMA_M".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					manDAO.updateMember(pid,pjt_code,pjt_name,pjt_mbr_type,mbr_start_date,mbr_end_date,mbr_poration,pjt_mbr_id,pjt_mbr_name,pjt_mbr_job,pjt_mbr_tel,pjt_mbr_grade,pjt_mbr_div);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//해당PM의 과제전체 List
					ArrayList table_list = new ArrayList();
					table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//해당과제 구성원전체 List
					ArrayList man_list = new ArrayList();
					man_list = manDAO.getProjectRead(pjt_code);
					request.setAttribute("MAN_List", man_list);

					//해당과제 구성원 List
					ArrayList one_list = new ArrayList();
					one_list = manDAO.getMemberRead(pid);
					request.setAttribute("ONE_List", one_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectMember_list.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//인력정보 전사공통 삭제하기
			else if("PMA_D".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					manDAO.deleteMember(pid);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//해당PM의 과제전체 List
					ArrayList table_list = new ArrayList();
					table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//해당과제 구성원전체 List
					ArrayList man_list = new ArrayList();
					man_list = manDAO.getProjectRead(pjt_code);
					request.setAttribute("MAN_List", man_list);

					//해당과제 구성원 List
					ArrayList one_list = new ArrayList();
					one_list = manDAO.getMemberRead(pid);
					request.setAttribute("ONE_List", one_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
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

