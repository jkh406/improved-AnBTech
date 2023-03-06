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

public class prsStandardServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSN_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"prs_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//프로세스 정보 파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"));	
		
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
			//	프로세스명 등록 LIST가져오기
			//--------------------------------------------------------------------
			//프로세스명 전사공통 전체 LIST가져오기
			if("PSN_LA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = pnameDAO.getCurrentPage();		//현재페이지
				int Tpage = pnameDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//프로세스명 부서공통 전체 LIST가져오기
			else if("PSN_LD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = pnameDAO.getCurrentPage();		//현재페이지
				int Tpage = pnameDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}

			//--------------------------------------------------------------------
			//	표준 프로세스 LIST가져오기
			//--------------------------------------------------------------------
			//표준 프로세스 전사공통에 입력할 LIST가져오기
			else if("STD_LA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);
				//기초정보 List
				ArrayList base_list = new ArrayList();
				base_list = stdDAO.getNodeBaseList(pid,"P",login_id);
				request.setAttribute("Base_List", base_list);

				//프로세스 전체 List
				ArrayList table_list = new ArrayList();
				table_list = stdDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//산출물 전체 List
				ArrayList doc_list = new ArrayList();
				doc_list = stdDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//현재 노드에 등록된 산출물 전체 List
				ArrayList save_list = new ArrayList();
				save_list = stdDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp").forward(request,response);
			}
			//표준 프로세스 부서공통에 입력할 LIST가져오기
			else if("STD_LD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);
				//기초정보 List
				ArrayList base_list = new ArrayList();
				base_list = stdDAO.getNodeBaseList(pid,"D",login_id);
				request.setAttribute("Base_List", base_list);

				//프로세스 전체 List
				ArrayList table_list = new ArrayList();
				table_list = stdDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//산출물 전체 List
				ArrayList doc_list = new ArrayList();
				doc_list = stdDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//현재 노드에 등록된 산출물 전체 List
				ArrayList save_list = new ArrayList();
				save_list = stdDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSN_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"prs_code":Hanguel.toHanguel(request.getParameter("sItem"));
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
		
		//프로세스 정보받기 [자노드가 multi일경우]
		String array_cnt = Hanguel.toHanguel(request.getParameter("array_cnt"))==null?"0":Hanguel.toHanguel(request.getParameter("array_cnt"));
		int acnt = Integer.parseInt(array_cnt);
		String[] node = new String[acnt];		//노드받기 배열
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		//노드 파라미터 받기 [프로세스,산출물포함]
		StringTokenizer str = new StringTokenizer(node_code,",");
		int n = 0;
		while(str.hasMoreTokens()) {
			node[n] = str.nextToken();
			n++;
			if(n == acnt) break;
		}
		String spid = Hanguel.toHanguel(request.getParameter("spid"))==null?"":Hanguel.toHanguel(request.getParameter("spid"));

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	프로세스명 코드 편집하기
			//--------------------------------------------------------------------
			//프로세스명 전사공통 전체 LIST가져오기
			if("PSN_LA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = pnameDAO.getCurrentPage();		//현재페이지
				int Tpage = pnameDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//프로세스명 전사공통 신규입력하기
			else if("PSN_WA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pnameDAO.inputPrsname(pid,prs_code,prs_name,type);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pnameDAO.getCurrentPage();		//현재페이지
					int Tpage = pnameDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//프로세스명 전사공통 수정내용가져오기
			else if("PSN_VA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/pnameAll_modify.jsp").forward(request,response);
			}
			//프로세스명 전사공통 수정하기
			else if("PSN_MA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = pnameDAO.updatePrsname(pid,prs_code,prs_name,type);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('프로세스코드가 중복되어 수정할 수 없습니다.');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = pnameDAO.getCurrentPage();		//현재페이지
						int Tpage = pnameDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//프로세스명 전사공통 삭제하기
			else if("PSN_DA".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = pnameDAO.deletePrsname(pid);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('전사 표준프로세스로 등록되어 삭제할 수 없습니다');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = pnameDAO.getPrsnameAllList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = pnameDAO.getCurrentPage();		//현재페이지
						int Tpage = pnameDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/pnameAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//프로세스명 부서공통 전체 LIST가져오기
			else if("PSN_LD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = pnameDAO.getCurrentPage();		//현재페이지
				int Tpage = pnameDAO.getTotalPage();		//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//프로세스명 부서공통 신규입력하기
			else if("PSN_WD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					pnameDAO.inputPrsname(pid,prs_code,prs_name,type);
					con.commit(); // commit한다.

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = pnameDAO.getCurrentPage();		//현재페이지
					int Tpage = pnameDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//프로세스명 부서공통 수정내용가져오기
			else if("PSN_VD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = pnameDAO.getPrsnameRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_modify.jsp").forward(request,response);
			}
			//프로세스명 부서공통 수정하기
			else if("PSN_MD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = pnameDAO.updatePrsname(pid,prs_code,prs_name,type);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('프로세스코드가 중복되어 수정할 수 없습니다.');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = pnameDAO.getCurrentPage();		//현재페이지
						int Tpage = pnameDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//프로세스명 부서공통 삭제하기
			else if("PSN_DD".equals(mode)){	
				com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					boolean data = pnameDAO.deletePrsname(pid);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					if(data == false) {
						out.println("<script>");
						out.println("alert('부서 표준프로세스로 등록되어 삭제할 수 없습니다');");
						out.println("history.go(-1)");
						out.println("</script>");
						out.close();
						return;
					} else {
						//전체 List 로 분기하기
						ArrayList table_list = new ArrayList();
						table_list = pnameDAO.getPrsnameDivList(login_id,sItem,sWord,page,max_display_cnt);
						request.setAttribute("Data_List", table_list);
						int Cpage = pnameDAO.getCurrentPage();		//현재페이지
						int Tpage = pnameDAO.getTotalPage();		//전체페이지
						getServletContext().getRequestDispatcher("/pjt/process/pnameDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					}
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}	
			//--------------------------------------------------------------------
			//	프로세스명 코드 편집하기
			//--------------------------------------------------------------------
			//표준 프로세스 전사공통에 입력할 LIST가져오기
			else if("STD_LA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);
				//기초정보 List
				ArrayList base_list = new ArrayList();
				base_list = stdDAO.getNodeBaseList(pid,"P",login_id);
				request.setAttribute("Base_List", base_list);

				//프로세스 전체 List
				ArrayList table_list = new ArrayList();
				table_list = stdDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//산출물 전체 List
				ArrayList doc_list = new ArrayList();
				doc_list = stdDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//현재 노드에 등록된 산출물 전체 List
				ArrayList save_list = new ArrayList();
				save_list = stdDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp").forward(request,response);
			}
			//표준프로세스 전사공통 신규입력하기
			else if("STD_WA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.inputNode(prs_code,parent_node,node,level_no,type);
					con.commit(); // commit한다.
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"P",login_id);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp?RD=Reload").forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//표준프로세스 전사공통 삭제하기
			else if("STD_DA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.deleteNode(pid,prs_code,parent_node,node,level_no,type);
					con.commit(); // commit한다.
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"P",login_id);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp?RD=Reload&comment="+data).forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//표준프로세스 산출물 기술문서 전사공통 신규입력하기
			else if("SDC_WA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stdDAO.inputNodeDoc(prs_code,parent_node,node,level_no,type);
					con.commit(); // commit한다.
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"P",login_id);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp?RD=").forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//표준프로세스 산출물 기술문서 전사공통 삭제하기
			else if("SDC_DA".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.deleteNodeDoc(pid,prs_code,parent_node,node,level_no,type);
					con.commit(); // commit한다.
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"P",login_id);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/process/processAll_node.jsp?RD=&comment="+data).forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//표준 프로세스 부서공통에 입력할 LIST가져오기
			else if("STD_LD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);
				//기초정보 List
				ArrayList base_list = new ArrayList();
				base_list = stdDAO.getNodeBaseList(pid,"D",login_id);
				request.setAttribute("Base_List", base_list);

				//프로세스 전체 List
				ArrayList table_list = new ArrayList();
				table_list = stdDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//산출물 전체 List
				ArrayList doc_list = new ArrayList();
				doc_list = stdDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//현재 노드에 등록된 산출물 전체 List
				ArrayList save_list = new ArrayList();
				save_list = stdDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp").forward(request,response);
			}
			//표준프로세스 부서공통 신규입력하기
			else if("STD_WD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stdDAO.inputNode(prs_code,parent_node,node,level_no,type);
					con.commit(); // commit한다.
	
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"D",login_id);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp?RD=Reload").forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//표준프로세스 부서공통 삭제하기
			else if("STD_DD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.deleteNode(pid,prs_code,parent_node,node,level_no,type);
					con.commit(); // commit한다.
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"D",login_id);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp?RD=Reload&comment="+data).forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//표준프로세스 산출물 기술문서 부서공통 신규입력하기
			else if("SDC_WD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					stdDAO.inputNodeDoc(prs_code,parent_node,node,level_no,type);
					con.commit(); // commit한다.
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"D",login_id);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp?RD=").forward(request,response);

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//표준프로세스 산출물 기술문서 부서공통 삭제하기
			else if("SDC_DD".equals(mode)){	
				com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = stdDAO.deleteNodeDoc(pid,prs_code,parent_node,node,level_no,type);
					con.commit(); // commit한다.
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = stdDAO.getNodeBaseList(spid,"D",login_id);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = stdDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = stdDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = stdDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/process/processDiv_node.jsp?RD=&comment="+data).forward(request,response);

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

