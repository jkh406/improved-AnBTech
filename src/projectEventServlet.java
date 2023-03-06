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

public class projectEventServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSM_EL":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"evt_content":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"":Hanguel.toHanguel(request.getParameter("pjtWord"));
		String node_status = Hanguel.toHanguel(request.getParameter("node_status"))==null?"":Hanguel.toHanguel(request.getParameter("node_status"));
		
		//실적 정보 파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));	
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));	
		String wm_type = Hanguel.toHanguel(request.getParameter("wm_type"))==null?"":Hanguel.toHanguel(request.getParameter("wm_type"));	
		String evt_content = Hanguel.toHanguel(request.getParameter("evt_content"))==null?"":Hanguel.toHanguel(request.getParameter("evt_content"));	
		String evt_note = Hanguel.toHanguel(request.getParameter("evt_note"))==null?"":Hanguel.toHanguel(request.getParameter("evt_note"));	
		String evt_issue = Hanguel.toHanguel(request.getParameter("evt_issue"))==null?"":Hanguel.toHanguel(request.getParameter("evt_issue"));	

		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setNodeCode(node_code);			//node_code
			table.setPjtword(pjtWord);				//주간/월간 구분
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
			table.setNodeStatus(node_status);		//노드 상태
		para_list.add(table);
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	실적LIST 가져오기
			//--------------------------------------------------------------------
			if("PSM_EL".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//자신의 과제중 진행중인 실적 전체 List
				ArrayList table_list = new ArrayList();
				table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("EVENT_List", table_list);

				//현재페이지/전체페이지
				int Cpage = evtDAO.getCurrentPage();	//현재페이지
				int Tpage = evtDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//실적이력 등록된 내용보기
			else if("PSM_EV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//해당과제/노드 입력된 실적정보
				ArrayList work_list = new ArrayList();
				work_list = evtDAO.getEventRead (pid);
				request.setAttribute("WORK_List", work_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_view.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSM_EL":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"evt_content":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		String node_status = Hanguel.toHanguel(request.getParameter("node_status"))==null?"":Hanguel.toHanguel(request.getParameter("node_status"));
		
		//실적 정보 파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));
		String progress = Hanguel.toHanguel(request.getParameter("progress"))==null?"":Hanguel.toHanguel(request.getParameter("progress"));
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));	
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));	
		String wm_type = Hanguel.toHanguel(request.getParameter("wm_type"))==null?"":Hanguel.toHanguel(request.getParameter("wm_type"));	
		String evt_content = Hanguel.toHanguel(request.getParameter("evt_content"))==null?"":Hanguel.toHanguel(request.getParameter("evt_content"));	
		String evt_note = Hanguel.toHanguel(request.getParameter("evt_note"))==null?"":Hanguel.toHanguel(request.getParameter("evt_note"));	
		String evt_issue = Hanguel.toHanguel(request.getParameter("evt_issue"))==null?"":Hanguel.toHanguel(request.getParameter("evt_issue"));	

		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setNodeCode(node_code);			//node_code
			table.setPjtword(pjtWord);				//주간/월간 구분
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
			table.setNodeStatus(node_status);		//노드 상태
		para_list.add(table);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	실적LIST 가져오기
			//--------------------------------------------------------------------
			//실적 전체LIST
			if("PSM_EL".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//자신의 과제중 진행중인 전체 List
				ArrayList table_list = new ArrayList();
				table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("EVENT_List", table_list);

				//현재페이지/전체페이지
				int Cpage = evtDAO.getCurrentPage();	//현재페이지
				int Tpage = evtDAO.getTotalPage();		//전체페이지

				//해당과제중 Activity만 출력하기
				ArrayList act_list = new ArrayList();
				act_list = evtDAO.getPjtActivityRead (pjt_code);
				request.setAttribute("ACT_List", act_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//실적이력 등록 준비
			else if("PSM_EWV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//해당과제/노드 정보
				ArrayList node_list = new ArrayList();
				node_list = evtDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_write.jsp").forward(request,response);
			}
			//실적이력 등록하기
			else if("PSM_EW".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					evtDAO.inputEvent(pjt_code,pjt_name,node_code,node_name,progress,user_id,user_name,in_date,wm_type,evt_content,evt_note,evt_issue,node_status);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//자신의 과제중 진행중인 전체 List
					ArrayList table_list = new ArrayList();
					table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("EVENT_List", table_list);

					//현재페이지/전체페이지
					int Cpage = evtDAO.getCurrentPage();	//현재페이지
					int Tpage = evtDAO.getTotalPage();		//전체페이지

					//해당과제중 Activity만 출력하기
					ArrayList act_list = new ArrayList();
					act_list = evtDAO.getPjtActivityRead (pjt_code);
					request.setAttribute("ACT_List", act_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//실적이력 등록된 내용보기
			else if("PSM_EV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//해당과제/노드 입력된 실적정보
				ArrayList work_list = new ArrayList();
				work_list = evtDAO.getEventRead (pid);
				request.setAttribute("WORK_List", work_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_view.jsp").forward(request,response);
			}
			//실적이력 수정하기 준비
			else if("PSM_EMV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//해당과제/노드 입력된 실적정보
				ArrayList work_list = new ArrayList();
				work_list = evtDAO.getEventRead (pid);
				request.setAttribute("WORK_List", work_list);

				//해당과제/노드 정보
				ArrayList node_list = new ArrayList();
				node_list = evtDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_modify.jsp").forward(request,response);
			}
			//실적이력 수정하기
			else if("PSM_EM".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					evtDAO.updateEvent(pid,pjt_code,node_code,progress,in_date,wm_type,evt_content,evt_note,evt_issue);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//자신의 과제중 진행중인 전체 List
					ArrayList table_list = new ArrayList();
					table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("EVENT_List", table_list);

					//현재페이지/전체페이지
					int Cpage = evtDAO.getCurrentPage();	//현재페이지
					int Tpage = evtDAO.getTotalPage();		//전체페이지

					//해당과제중 Activity만 출력하기
					ArrayList act_list = new ArrayList();
					act_list = evtDAO.getPjtActivityRead (pjt_code);
					request.setAttribute("ACT_List", act_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//실적이력 삭제하기
			else if("PSM_ED".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					evtDAO.deleteEvent(pid);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//자신의 과제중 진행중인 전체 List
					ArrayList table_list = new ArrayList();
					table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("EVENT_List", table_list);

					//현재페이지/전체페이지
					int Cpage = evtDAO.getCurrentPage();	//현재페이지
					int Tpage = evtDAO.getTotalPage();		//전체페이지

					//해당과제중 Activity만 출력하기
					ArrayList act_list = new ArrayList();
					act_list = evtDAO.getPjtActivityRead (pjt_code);
					request.setAttribute("ACT_List", act_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//해당과제/해당노드 완료승인 요청준비
			else if("PSM_EAV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);

				//해당과제/노드 정보
				ArrayList node_list = new ArrayList();
				node_list = evtDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_nodeReq.jsp").forward(request,response);
			}
			//해당과제/해당노드 완료승인 요청하기
			else if("PSM_EA".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//1.요청작성내용 등록 : pjt_event
					evtDAO.inputEvent(pjt_code,pjt_name,node_code,node_name,progress,user_id,user_name,in_date,wm_type,evt_content,evt_note,evt_issue,node_status);
					
					//2.요청처리 : 완료일자 입력 pjt_schedule
					String ReqApp = evtDAO.nodeAppReq(pjt_code,pjt_name,node_code,node_name,user_id,user_name,evt_content,evt_note,evt_issue);
					if(ReqApp.equals("A")) {	//승인완료상태
						con.rollback();
					}
					else {
						con.commit(); // commit한다.
					}
					con.setAutoCommit(true);

					//자신의 과제중 진행중인 전체 List
					ArrayList table_list = new ArrayList();
					table_list = evtDAO.getEventList (login_id,pjt_code,node_code,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("EVENT_List", table_list);

					//현재페이지/전체페이지
					int Cpage = evtDAO.getCurrentPage();	//현재페이지
					int Tpage = evtDAO.getTotalPage();		//전체페이지

					//해당과제중 Activity만 출력하기
					ArrayList act_list = new ArrayList();
					act_list = evtDAO.getPjtActivityRead (pjt_code);
					request.setAttribute("ACT_List", act_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/staff/projectStaffEvent_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage+"&ReqApp="+ReqApp).forward(request,response);

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



