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

public class projectNoteServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PNT_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"note":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"1":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//문제점 정보 파라미터
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String users = Hanguel.toHanguel(request.getParameter("users"))==null?"0":Hanguel.toHanguel(request.getParameter("users"));
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"0":Hanguel.toHanguel(request.getParameter("in_date"));
		String book_date = Hanguel.toHanguel(request.getParameter("book_date"))==null?"":Hanguel.toHanguel(request.getParameter("book_date"));
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"0":Hanguel.toHanguel(request.getParameter("note"));
		String solution = Hanguel.toHanguel(request.getParameter("solution"))==null?"0":Hanguel.toHanguel(request.getParameter("solution"));
		String content = Hanguel.toHanguel(request.getParameter("content"))==null?"0":Hanguel.toHanguel(request.getParameter("content"));
		String sol_date = Hanguel.toHanguel(request.getParameter("sol_date"))==null?"0":Hanguel.toHanguel(request.getParameter("sol_date"));
		String note_status = Hanguel.toHanguel(request.getParameter("note_status"))==null?"0":Hanguel.toHanguel(request.getParameter("note_status"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"0":Hanguel.toHanguel(request.getParameter("pid"));
		
		//가장최근의 실적 읽기
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
	
		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setNoteStatus(note_status);		//note 상태
			table.setPjtword(pjtWord);				//과제상태code 및 실적종류(내용,문제점,이슈중)
			table.setNodeCode(node_code);			//노드코드
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
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
			//	문제점관리정보 LIST가져오기
			//--------------------------------------------------------------------
			//문제점관리정보 LIST가져오기
			if("PNT_L".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//해당PM의 전체과제List
				ArrayList pjt_list = new ArrayList();
				pjt_list = schDAO.getProjectList(login_id,pjtWord,"pjt_code","");
				request.setAttribute("PJT_List", pjt_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = pjt_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//해당 과제문제점 List
				ArrayList table_list = new ArrayList();
				table_list = noteDAO.getNoteList(pjt_code,note_status,sItem,sWord,page,max_display_cnt);
				request.setAttribute("NOTE_List", table_list);

				int Cpage = noteDAO.getCurrentPage();	//현재페이지
				int Tpage = noteDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//문제점관리정보 : 최근에 진행중/승인대기 중인 작업내용 보기 (문제점 등록/수정 공통항목)
			else if("PNT_WV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//진행중 or 승인대기중인 노드 쿼리하기
				ArrayList act_list = new ArrayList();
				act_list = noteDAO.getWorkActivity(pjt_code);
				request.setAttribute("ACT_List", act_list);
	
				//진행노드중 최초항목 찾아내기
				Iterator act_iter = act_list.iterator();
				if(act_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)act_iter.next();
					if(node_code.length() == 0) {
						node_code = table.getChildNode();
						in_date = noteDAO.getLastDate (pjt_code,node_code);
					}
				}

				//진행노드 실적가져오기
				ArrayList work_list = new ArrayList();
				work_list = noteDAO.getLastWork(pjt_code,node_code,in_date);
				request.setAttribute("WORK_List", work_list);

				//진행노드 실적일 가져오기
				ArrayList indate_list = new ArrayList();
				indate_list = noteDAO.getInDate(pjt_code,node_code);
				request.setAttribute("INDATE_List", indate_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_work.jsp").forward(request,response);
			}
			//문제점관리정보 : 문제점 등록준비
			else if("PNT_NV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//해당과제 전체노드 쿼리하기
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//인력멤버 가져오기
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);
				
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_write.jsp?RD=").forward(request,response);
			}
			//문제점관리정보 : 문제점 수정준비
			else if("PNT_MV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//해당과제 전체노드 쿼리하기
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//인력멤버 가져오기
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당문제점 내용 읽기
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_modify.jsp?RD=").forward(request,response);
			}
			//문제점관리정보 : 문제점 해결내용 작성 준비
			else if("PNT_CV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//해당과제 전체노드 쿼리하기
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//인력멤버 가져오기
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당문제점 내용 읽기
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_solution.jsp?RD=").forward(request,response);
			}
			//문제점관리정보 : 문제점해결 내용보기
			else if("PNT_SV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//해당문제점 내용 읽기
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);

				//과제코드 최초항목 찾아내기
				Iterator note_iter = note_list.iterator();
				if(note_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)note_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//해당과제 전체노드 쿼리하기
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//인력멤버 가져오기
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_view.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PNT_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//문제점 정보 파라미터
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String users = Hanguel.toHanguel(request.getParameter("users"))==null?"0":Hanguel.toHanguel(request.getParameter("users"));
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));
		String book_date = Hanguel.toHanguel(request.getParameter("book_date"))==null?"":Hanguel.toHanguel(request.getParameter("book_date"));
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"0":Hanguel.toHanguel(request.getParameter("note"));
		String solution = Hanguel.toHanguel(request.getParameter("solution"))==null?"":Hanguel.toHanguel(request.getParameter("solution"));
		String content = Hanguel.toHanguel(request.getParameter("content"))==null?"":Hanguel.toHanguel(request.getParameter("content"));
		String sol_date = Hanguel.toHanguel(request.getParameter("sol_date"))==null?"":Hanguel.toHanguel(request.getParameter("sol_date"));
		String note_status = Hanguel.toHanguel(request.getParameter("note_status"))==null?"0":Hanguel.toHanguel(request.getParameter("note_status"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"0":Hanguel.toHanguel(request.getParameter("pid"));
		
		//가장최근의 실적 읽기
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
	
		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setNoteStatus(note_status);		//note 상태
			table.setPjtword(pjtWord);				//과제상태code 및 실적종류(내용,문제점,이슈중)
			table.setNodeCode(node_code);			//노드코드
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	문제점관리정보 LIST가져오기
			//--------------------------------------------------------------------
			//문제점관리정보 LIST가져오기
			if("PNT_L".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//해당PM의 전체과제List
				ArrayList pjt_list = new ArrayList();
				pjt_list = schDAO.getProjectList(login_id,pjtWord,"pjt_code","");
				request.setAttribute("PJT_List", pjt_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = pjt_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//해당 과제문제점 List
				ArrayList table_list = new ArrayList();
				table_list = noteDAO.getNoteList(pjt_code,note_status,sItem,sWord,page,max_display_cnt);
				request.setAttribute("NOTE_List", table_list);

				int Cpage = noteDAO.getCurrentPage();	//현재페이지
				int Tpage = noteDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//문제점관리정보 : 최근에 진행중/승인대기 중인 작업내용 보기 (문제점 등록/수정 공통항목)
			else if("PNT_WV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//진행중 or 승인대기중인 노드 쿼리하기
				ArrayList act_list = new ArrayList();
				act_list = noteDAO.getWorkActivity(pjt_code);
				request.setAttribute("ACT_List", act_list);

				//진행노드중 최초항목 찾아내기
				Iterator act_iter = act_list.iterator();
				if(act_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)act_iter.next();
					if(node_code.length() == 0) {
						node_code = table.getChildNode();
						in_date = noteDAO.getLastDate (pjt_code,node_code);
					}
				}

				//진행노드 실적가져오기
				ArrayList work_list = new ArrayList();
				work_list = noteDAO.getLastWork(pjt_code,node_code,in_date);
				request.setAttribute("WORK_List", work_list);

				//진행노드 실적일 가져오기
				ArrayList indate_list = new ArrayList();
				indate_list = noteDAO.getInDate(pjt_code,node_code);
				request.setAttribute("INDATE_List", indate_list);
			
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_work.jsp").forward(request,response);
			}
			//문제점관리정보 : 문제점 등록준비
			else if("PNT_NV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//해당과제 전체노드 쿼리하기
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//인력멤버 가져오기
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);
				
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_write.jsp?RD=").forward(request,response);
			}
			//문제점관리정보 : 문제점 등록하기
			else if("PNT_NW".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//기본일정 입력/수정할때
					noteDAO.inputNote(pid,pjt_code,pjt_name,node_code,users,in_date,book_date,note,solution,note_status);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//해당과제 전체노드 쿼리하기
					ArrayList node_list = new ArrayList();
					node_list = noteDAO.getNodeList(pjt_code);
					request.setAttribute("NODE_List", node_list);

					//인력멤버 가져오기
					ArrayList man_list = new ArrayList();
					man_list = noteDAO.getPjtMember(pjt_code);
					request.setAttribute("MAN_List", man_list);
					
					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectNote_write.jsp?RD=R").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//문제점관리정보 : 문제점 수정준비
			else if("PNT_MV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//해당과제 전체노드 쿼리하기
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//인력멤버 가져오기
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당문제점 내용 읽기
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_modify.jsp?RD=").forward(request,response);
			}
			//문제점관리정보 : 문제점 수정하기
			else if("PNT_MW".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//기본일정 입력/수정할때
					noteDAO.updateNote(pid,node_code,book_date,users,note,solution);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기하기
					String strL = "&pjt_code="+pjt_code+"&note_status=0&sItem=note&sWord=&page=1";
					out.println("	<script>");
					out.println("	parent.location.href('../servlet/projectNoteServlet?mode=PNT_L"+strL+"');");
					out.println("	</script>");
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//문제점관리정보 : 문제점 삭제하기
			else if("PNT_D".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//기본일정 입력/수정할때
					noteDAO.deleteNote(pid);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//해당PM의 전체과제List
					ArrayList pjt_list = new ArrayList();
					pjt_list = schDAO.getProjectList(login_id,pjtWord,"pjt_code","");
					request.setAttribute("PJT_List", pjt_list);

					//과제코드 최초항목 찾아내기
					Iterator pjt_iter = pjt_list.iterator();
					if(pjt_iter.hasNext()) {
						table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
						if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
					}

					//해당 과제문제점 List
					ArrayList table_list = new ArrayList();
					table_list = noteDAO.getNoteList(pjt_code,note_status,sItem,sWord,page,max_display_cnt);
					request.setAttribute("NOTE_List", table_list);

					int Cpage = noteDAO.getCurrentPage();	//현재페이지
					int Tpage = noteDAO.getTotalPage();		//전체페이지

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectNote_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}
			}
			//문제점관리정보 : 문제점해결 작성준비
			else if("PNT_CV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//해당과제 전체노드 쿼리하기
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//인력멤버 가져오기
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당문제점 내용 읽기
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_solution.jsp?RD=").forward(request,response);
			}
			//문제점관리정보 : 문제점해결 작성 저장하기
			else if("PNT_CW".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//기본일정 입력/수정할때
					noteDAO.updateContent(pid,content,sol_date,note_status);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기하기
					String strL = "&pjt_code="+pjt_code+"&note_status=1&sItem=note&sWord=&page=1";
					out.println("	<script>");
					out.println("	parent.location.href('../servlet/projectNoteServlet?mode=PNT_L"+strL+"');");
					out.println("	</script>");
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//문제점관리정보 : 문제점해결 작성 취소하기
			else if("PNT_RW".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//기본일정 입력/수정할때
					noteDAO.updateRecovery(pid);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기하기
					String strL = "&pjt_code="+pjt_code+"&note_status=0&sItem=note&sWord=&page=1";
					out.println("	<script>");
					out.println("	location.href('../servlet/projectNoteServlet?mode=PNT_L"+strL+"');");
					out.println("	</script>");
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//문제점관리정보 : 문제점해결 내용보기
			else if("PNT_SV".equals(mode)){	
				com.anbtech.pjt.db.pjtNoteDAO noteDAO = new com.anbtech.pjt.db.pjtNoteDAO(con);

				//해당과제 전체노드 쿼리하기
				ArrayList node_list = new ArrayList();
				node_list = noteDAO.getNodeList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//인력멤버 가져오기
				ArrayList man_list = new ArrayList();
				man_list = noteDAO.getPjtMember(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당문제점 내용 읽기
				ArrayList note_list = new ArrayList();
				note_list = noteDAO.getNoteRead(pid);
				request.setAttribute("NOTE_List", note_list);
				
				
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNote_view.jsp").forward(request,response);
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



