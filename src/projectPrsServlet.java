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

public class projectPrsServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PNP_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//프로세스 정보 파라미터
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	

		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);		//pjt_code
			table.setPjtName(pjt_name);		//pjt_name
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
			//	인력정보에서 해당과제 LIST가져오기
			//--------------------------------------------------------------------
			//해당PM의 과제및 과제인력정보 LIST가져오기
			if("PNP_P".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				//해당PM의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_prs.jsp").forward(request,response);
			}
		
			//--------------------------------------------------------------------
			//	과제일정 해당과제 프로세스 LIST가져오기
			//--------------------------------------------------------------------
			else if("PNP_L".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);
				//기초정보 List
				ArrayList base_list = new ArrayList();
				base_list = prsDAO.getNodeBaseList(pid);
				request.setAttribute("Base_List", base_list);

				//해당과제 프로세스 전체 List
				ArrayList table_list = new ArrayList();
				table_list = prsDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//산출물 전체 List
				ArrayList doc_list = new ArrayList();
				doc_list = prsDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//해당과제 현재 노드에 등록된 산출물 전체 List
				ArrayList save_list = new ArrayList();
				save_list = prsDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp").forward(request,response);
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
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//정보입력/수정/삭제 데이터 받기
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//관리코드
		String doc_code = Hanguel.toHanguel(request.getParameter("doc_code"))==null?"":Hanguel.toHanguel(request.getParameter("doc_code"));			//산출물 코드
		String doc_name = Hanguel.toHanguel(request.getParameter("doc_name"))==null?"":Hanguel.toHanguel(request.getParameter("doc_name"));			//산출물 이름
		String parent_node = Hanguel.toHanguel(request.getParameter("parent_node"))==null?"":Hanguel.toHanguel(request.getParameter("parent_node"));//모노드
		String child_node = Hanguel.toHanguel(request.getParameter("child_node"))==null?"":Hanguel.toHanguel(request.getParameter("child_node"));	//자노드
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));		//노드명
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));			//모자노드 구성번호
		
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
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));

		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);		//pjt_code
			table.setPjtName(pjt_name);		//pjt_code
			table.setPjtword(pjtWord);		//과제상태
			table.setSitem(sItem);			//sItem
			table.setSword(sWord);			//sWord
		para_list.add(table);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	인력정보에서 해당과제 LIST가져오기
			//--------------------------------------------------------------------
			//해당PM의 과제및 과제인력정보 LIST가져오기
			if("PNP_P".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				//해당PM의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_prs.jsp").forward(request,response);
			}

			//--------------------------------------------------------------------
			//	과제관리 스케쥴 프로세스 편집하기
			//--------------------------------------------------------------------
			else if("PNP_L".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);
				//기초정보 List
				ArrayList base_list = new ArrayList();
				base_list = prsDAO.getNodeBaseList(pid);
				request.setAttribute("Base_List", base_list);

				//해당과제 프로세스 전체 List
				ArrayList table_list = new ArrayList();
				table_list = prsDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//산출물 전체 List
				ArrayList doc_list = new ArrayList();
				doc_list = prsDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//해당과제 현재 노드에 등록된 산출물 전체 List
				ArrayList save_list = new ArrayList();
				save_list = prsDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp").forward(request,response);
			}
			//과제일정 프로세스 신규입력하기
			else if("PNP_W".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = prsDAO.inputNode(pjt_code,pjt_name,parent_node,node,level_no);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = prsDAO.getNodeBaseList(spid);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = prsDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = prsDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = prsDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp?RD=Reload").forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제일정 표준프로세스  삭제하기
			else if("PNP_D".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = prsDAO.deleteNode(pid,pjt_code,parent_node,node,level_no);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = prsDAO.getNodeBaseList(spid);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = prsDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = prsDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = prsDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp?RD=Reload&comment="+data).forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제일정 표준프로세스 산출물 기술문서  신규입력하기
			else if("PND_W".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					prsDAO.inputNodeDoc(pjt_code,pjt_name,parent_node,node,level_no);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = prsDAO.getNodeBaseList(spid);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = prsDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = prsDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = prsDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp?RD=").forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제일정 표준프로세스 산출물 기술문서  삭제하기
			else if("PND_D".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = prsDAO.deleteNodeDoc(pid,pjt_code,parent_node,node,level_no);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					
					//전체 List 로 분기하기
					//기초정보 List
					ArrayList base_list = new ArrayList();
					base_list = prsDAO.getNodeBaseList(spid);
					request.setAttribute("Base_List", base_list);

					//프로세스 전체 List
					ArrayList table_list = new ArrayList();
					table_list = prsDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//산출물 전체 List
					ArrayList doc_list = new ArrayList();
					doc_list = prsDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//현재 노드에 등록된 산출물 전체 List
					ArrayList save_list = new ArrayList();
					save_list = prsDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp?RD=&comment="+data).forward(request,response);

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

