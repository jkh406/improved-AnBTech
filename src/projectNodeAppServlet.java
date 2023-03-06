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

public class projectNodeAppServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 8;

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSN_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//노드 정보 파라미터
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String parent_node = Hanguel.toHanguel(request.getParameter("parent_node"))==null?"0":Hanguel.toHanguel(request.getParameter("parent_node"));
		String child_node = Hanguel.toHanguel(request.getParameter("child_node"))==null?"0":Hanguel.toHanguel(request.getParameter("child_node"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"0":Hanguel.toHanguel(request.getParameter("level_no"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	

		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setPjtword(pjtWord);				//과제상태
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
			table.setParentNode(parent_node);		//parent_node
			table.setChildNode(child_node);			//child_node
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
			//	과제노드 LIST가져오기
			//--------------------------------------------------------------------
			//과제노드 LIST가져오기
			if("PSN_L".equals(mode)){	
				com.anbtech.pjt.db.pjtNodeAppDAO nodeDAO = new com.anbtech.pjt.db.pjtNodeAppDAO(con);
				//해당PM의 진행중 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = nodeDAO.getProjectList(login_id,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					pjt_code = table.getPjtCode();
				}

				//해당과제 일정 List
				ArrayList sch_list = new ArrayList();
				sch_list = nodeDAO.getPjtSchedule(pjt_code,level_no,parent_node);
				request.setAttribute("SCH_List", sch_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNode_main.jsp").forward(request,response);
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
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//정보입력/수정/삭제 데이터 받기
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//관리코드
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String parent_node = Hanguel.toHanguel(request.getParameter("parent_node"))==null?"0":Hanguel.toHanguel(request.getParameter("parent_node"));
		String child_node = Hanguel.toHanguel(request.getParameter("child_node"))==null?"0":Hanguel.toHanguel(request.getParameter("child_node"));	
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));		
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"0":Hanguel.toHanguel(request.getParameter("level_no"));			
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));			
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));			
		String pjt_node_mbr = Hanguel.toHanguel(request.getParameter("pjt_node_mbr"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_node_mbr"));			
		String plan_start_date = Hanguel.toHanguel(request.getParameter("plan_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_start_date"));			
		String plan_end_date = Hanguel.toHanguel(request.getParameter("plan_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_end_date"));			
		String chg_start_date = Hanguel.toHanguel(request.getParameter("chg_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("chg_start_date"));			
		String chg_end_date = Hanguel.toHanguel(request.getParameter("chg_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("chg_end_date"));			
		String rst_start_date = Hanguel.toHanguel(request.getParameter("rst_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("rst_start_date"));			
		String rst_end_date = Hanguel.toHanguel(request.getParameter("rst_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("rst_end_date"));			
		String plan_cnt = Hanguel.toHanguel(request.getParameter("plan_cnt"))==null?"":Hanguel.toHanguel(request.getParameter("plan_cnt"));			
		String chg_cnt = Hanguel.toHanguel(request.getParameter("chg_cnt"))==null?"":Hanguel.toHanguel(request.getParameter("chg_cnt"));			
		String result_cnt = Hanguel.toHanguel(request.getParameter("result_cnt"))==null?"":Hanguel.toHanguel(request.getParameter("result_cnt"));			
		String node_status = Hanguel.toHanguel(request.getParameter("node_status"))==null?"":Hanguel.toHanguel(request.getParameter("node_status"));			
		String remark = Hanguel.toHanguel(request.getParameter("remark"))==null?"":Hanguel.toHanguel(request.getParameter("remark"));			
		
		//승인요청시 승인/반려 할 요청문서 관리번호
		String spid = Hanguel.toHanguel(request.getParameter("spid"))==null?"":Hanguel.toHanguel(request.getParameter("spid"));
		
		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setPjtword(pjtWord);				//과제상태
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
			table.setParentNode(parent_node);		//parent_node
			table.setChildNode(child_node);			//child_node
			table.setRemark(remark);				//remark
		para_list.add(table);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	과제노드 LIST가져오기
			//--------------------------------------------------------------------
			//과제노드 LIST가져오기 [TEXT로 보기]
			if("PSN_L".equals(mode)){	
				com.anbtech.pjt.db.pjtNodeAppDAO nodeDAO = new com.anbtech.pjt.db.pjtNodeAppDAO(con);
				//해당PM의 진행중 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = nodeDAO.getProjectList(login_id,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				if(pjt_code.length() == 0) {
					Iterator pjt_iter = table_list.iterator();
					if(pjt_iter.hasNext()) {
						table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
						pjt_code = table.getPjtCode();
					}
				}

				//해당과제 일정 List
				ArrayList sch_list = new ArrayList();
				sch_list = nodeDAO.getPjtSchedule(pjt_code,level_no,parent_node);
				request.setAttribute("SCH_List", sch_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNode_main.jsp").forward(request,response);
			}
			//과제노드 LIST가져오기 [GRAPH로 보기:진행율/계획일정 기준]
			else if("PSN_GPP".equals(mode)){	
				com.anbtech.pjt.db.pjtNodeAppDAO nodeDAO = new com.anbtech.pjt.db.pjtNodeAppDAO(con);
				//해당PM의 진행중 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = nodeDAO.getProjectList(login_id,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				if(pjt_code.length() == 0) {
					Iterator pjt_iter = table_list.iterator();
					if(pjt_iter.hasNext()) {
						table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
						pjt_code = table.getPjtCode();
					}
				}

				//해당과제 일정 List
				ArrayList sch_list = new ArrayList();
				sch_list = nodeDAO.getPjtGanttChart(pjt_code,level_no,parent_node);
				request.setAttribute("SCH_List", sch_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNode_graphPP.jsp").forward(request,response);
			}
			//과제노드 LIST가져오기 [GRAPH로 보기:진행율/수정일정 기준]
			else if("PSN_GPC".equals(mode)){	
				com.anbtech.pjt.db.pjtNodeAppDAO nodeDAO = new com.anbtech.pjt.db.pjtNodeAppDAO(con);
				//해당PM의 진행중 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = nodeDAO.getProjectList(login_id,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				if(pjt_code.length() == 0) {
					Iterator pjt_iter = table_list.iterator();
					if(pjt_iter.hasNext()) {
						table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
						pjt_code = table.getPjtCode();
					}
				}

				//해당과제 일정 List
				ArrayList sch_list = new ArrayList();
				sch_list = nodeDAO.getPjtGanttChart(pjt_code,level_no,parent_node);
				request.setAttribute("SCH_List", sch_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNode_graphPC.jsp").forward(request,response);
			}
			//과제노드 LIST가져오기 [GRAPH로 보기:실적일/계획일정 기준]
			else if("PSN_GSP".equals(mode)){	
				com.anbtech.pjt.db.pjtNodeAppDAO nodeDAO = new com.anbtech.pjt.db.pjtNodeAppDAO(con);
				//해당PM의 진행중 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = nodeDAO.getProjectList(login_id,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				if(pjt_code.length() == 0) {
					Iterator pjt_iter = table_list.iterator();
					if(pjt_iter.hasNext()) {
						table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
						pjt_code = table.getPjtCode();
					}
				}

				//해당과제 일정 List
				ArrayList sch_list = new ArrayList();
				sch_list = nodeDAO.getPjtBarChart(pjt_code,level_no,parent_node);
				request.setAttribute("SCH_List", sch_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNode_graphSP.jsp").forward(request,response);
			}
			//과제노드 LIST가져오기 [GRAPH로 보기:실적일/수정일정 기준]
			else if("PSN_GSC".equals(mode)){	
				com.anbtech.pjt.db.pjtNodeAppDAO nodeDAO = new com.anbtech.pjt.db.pjtNodeAppDAO(con);
				//해당PM의 진행중 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = nodeDAO.getProjectList(login_id,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				if(pjt_code.length() == 0) {
					Iterator pjt_iter = table_list.iterator();
					if(pjt_iter.hasNext()) {
						table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
						pjt_code = table.getPjtCode();
					}
				}

				//해당과제 일정 List
				ArrayList sch_list = new ArrayList();
				sch_list = nodeDAO.getPjtBarChart(pjt_code,level_no,parent_node);
				request.setAttribute("SCH_List", sch_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNode_graphSC.jsp").forward(request,response);
			}
			//과제노드 개별내용보기 : 노드승인준비
			else if("PSN_AV".equals(mode)){	
				com.anbtech.pjt.db.pjtEventDAO evtDAO = new com.anbtech.pjt.db.pjtEventDAO(con);
				com.anbtech.pjt.db.pjtNodeAppDAO nodeDAO = new com.anbtech.pjt.db.pjtNodeAppDAO(con);

				//해당노드 정보 List
				ArrayList node_list = new ArrayList();
				node_list = nodeDAO.getNodeData(pjt_code,parent_node,child_node);
				request.setAttribute("NODE_List", node_list);

				//자신의 과제중 실적 전체 List
				ArrayList table_list = new ArrayList();
				table_list = evtDAO.getEventList (login_id,pjt_code,child_node,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("EVENT_List", table_list);

				//현재페이지/전체페이지
				int Cpage = evtDAO.getCurrentPage();	//현재페이지
				int Tpage = evtDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectNode_app.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//과제노드 개별내용보기 : 노드승인하기
			else if("PSN_A".equals(mode)){	
				com.anbtech.pjt.db.pjtNodeAppDAO nodeDAO = new com.anbtech.pjt.db.pjtNodeAppDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//노드승인하기
					nodeDAO.updateNodeApproval(pid,pjt_code,parent_node,child_node,rst_start_date,rst_end_date,node_status);
					
					//노드승인 PM코맨트를 승인요청에 담기
					nodeDAO.nodeApprovalRemark(spid,pjt_code,child_node,remark,"A");

					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//해당PM의 진행중 과제전체 List
					ArrayList table_list = new ArrayList();
					table_list = nodeDAO.getProjectList(login_id,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//해당과제 일정 List
					ArrayList sch_list = new ArrayList();
					sch_list = nodeDAO.getPjtSchedule(pjt_code,"0","0");
					request.setAttribute("SCH_List", sch_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectNode_main.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제노드 개별내용보기 : 노드반려하기
			else if("PSN_R".equals(mode)){	
				com.anbtech.pjt.db.pjtNodeAppDAO nodeDAO = new com.anbtech.pjt.db.pjtNodeAppDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//노드반려하기
					nodeDAO.updateNodeReject(pid);

					//노드승인반려 PM코맨트를 승인요청에 담기
					nodeDAO.nodeApprovalRemark(spid,pjt_code,child_node,remark,"R");

					//전자우편으로 반려내용 보내기
					nodeDAO.sendMailToUser(login_id,login_name,user_id,user_name,pjt_code,pjt_name,child_node,node_name,plan_start_date,plan_end_date,chg_start_date,chg_end_date,rst_start_date,rst_end_date,remark);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//해당PM의 진행중 과제전체 List
					ArrayList table_list = new ArrayList();
					table_list = nodeDAO.getProjectList(login_id,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//해당과제 일정 List
					ArrayList sch_list = new ArrayList();
					sch_list = nodeDAO.getPjtSchedule(pjt_code,"0","0");
					request.setAttribute("SCH_List", sch_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectNode_main.jsp").forward(request,response);
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



