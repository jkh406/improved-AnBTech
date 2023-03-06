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

public class projectSchServlet extends HttpServlet {

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
		
		//프로세스 정보 파라미터
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
			//	일정정보 LIST가져오기
			//--------------------------------------------------------------------
			//일정정보 LIST가져오기
			if("PSC_L".equals(mode)){	
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);
				//해당멤버의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//과제코드 최초항목 찾아내기
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					pjt_code = table.getPjtCode();
				}

				//해당과제 일정 List
				ArrayList sch_list = new ArrayList();
				sch_list = schDAO.getPjtSchedule(pjt_code,level_no,parent_node);
				request.setAttribute("SCH_List", sch_list); 

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectSch_main.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSC_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//정보입력/수정/삭제 데이터 받기
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//관리코드
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String parent_node = Hanguel.toHanguel(request.getParameter("parent_node"))==null?"0":Hanguel.toHanguel(request.getParameter("parent_node"));
		String child_node = Hanguel.toHanguel(request.getParameter("child_node"))==null?"0":Hanguel.toHanguel(request.getParameter("child_node"));	
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));		
		String weight = Hanguel.toHanguel(request.getParameter("weight"))==null?"":Hanguel.toHanguel(request.getParameter("weight"));		
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
		String chg_note = Hanguel.toHanguel(request.getParameter("chg_note"))==null?"":Hanguel.toHanguel(request.getParameter("chg_note"));			
		
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
			table.setChgNote(chg_note);				//일정변경사유 
		para_list.add(table);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	일정정보 LIST가져오기
			//--------------------------------------------------------------------
			//일정정보 LIST가져오기
			if("PSC_L".equals(mode)){	
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);
				//해당멤버의 과제전체 List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
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
				sch_list = schDAO.getPjtSchedule(pjt_code,level_no,parent_node);
				request.setAttribute("SCH_List", sch_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectSch_main.jsp").forward(request,response);
			}
			//일정정보 개별Node 내용보기 : 상세일정 입력준비할때
			else if("PSC_VS".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);

				//해당과제 과제정보(계획일 및 과제상태 등)
				ArrayList gen_list = new ArrayList();
				gen_list = genDAO.getPjtRead(pjt_code);
				request.setAttribute("GEN_List", gen_list);

				//해당과제 구성원전체 List(개발멤버 구성)
				ArrayList man_list = new ArrayList();
				man_list = manDAO.getProjectRead(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당노드 정보 List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getNodeData(pjt_code,parent_node,child_node);
				request.setAttribute("NODE_List", table_list);

				//해당노드 일정변경 이력
				ArrayList chg_list = new ArrayList();
				chg_list = chgDAO.getChangeSchList (login_id,pjt_code,child_node,sItem,sWord,page,5);
				request.setAttribute("CHG_List", chg_list);
				int Cpage = chgDAO.getCurrentPage();	//현재페이지
				int Tpage = chgDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//상세일정 등록이 끝났는지 보기
				String Complete_Schedule = schDAO.checkCompleteSchedule(pjt_code);

				//첫노드와 마지막노드 찾기
				String FLnode = schDAO.checkFLnodeSchedule(pjt_code);

				//전체 weight값 찾기
				String tWeight = schDAO.getTotalWeight(pjt_code);
			
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectSch_schedule.jsp?Complete_Schedule="+Complete_Schedule+"&FLnode="+FLnode+"&tWeight="+tWeight+"&Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//일정정보 개별내용보기 : 노드 작업지시 할때
			else if("PSC_VJ".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//해당과제 과제정보(계획일 및 과제상태 등)
				ArrayList gen_list = new ArrayList();
				gen_list = genDAO.getPjtRead(pjt_code);
				request.setAttribute("GEN_List", gen_list);

				//해당과제 구성원전체 List
				ArrayList man_list = new ArrayList();
				man_list = manDAO.getProjectRead(pjt_code);
				request.setAttribute("MAN_List", man_list);

				//해당노드 정보 List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getNodeData(pjt_code,parent_node,child_node);
				request.setAttribute("NODE_List", table_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
			
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectSch_nodeOrder.jsp").forward(request,response);
			}
			//상세일정 입력[등록/수정]하기
			else if("PSC_S".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//기본일정 입력/수정할때
					schDAO.updateSchedule(pid,pjt_code,parent_node,child_node,weight,user_id,user_name,pjt_node_mbr,plan_start_date,plan_end_date,chg_start_date,chg_end_date,rst_start_date,rst_end_date,node_status);
					schDAO.updateWeight(pjt_code,parent_node,child_node,weight);	//step,phase의 weight등록

					//일정수정시 수정이력입력할때
					if((chg_start_date.length() != 0) && (chg_end_date.length() != 0) && (chg_note.length() > 3)) {
						chgDAO.inputChangeSch(pjt_code,pjt_name,child_node,node_name,login_id,login_name,chg_note);
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//해당멤버의 과제전체 List
					ArrayList table_list = new ArrayList();
					table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//해당과제 일정 List
					ArrayList sch_list = new ArrayList();
					sch_list = schDAO.getPjtSchedule(pjt_code,"0","0");
					request.setAttribute("SCH_List", sch_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectSch_main.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//노드시작지시 입력[등록/수정]하기
			else if("PSC_J".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);
				com.anbtech.pjt.db.pjtChangeSchDAO chgDAO = new com.anbtech.pjt.db.pjtChangeSchDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//노드지시내용 입력
					schDAO.updateNodeStart(pid,pjt_code,parent_node,child_node,plan_start_date,plan_end_date,chg_start_date,chg_end_date,rst_start_date,rst_end_date,node_status,remark);
					
					//노드지시내용 전자메일로 통보하기
					schDAO.sendMailToUser(login_id,login_name,user_id,user_name,pjt_code,pjt_name,child_node,node_name,plan_start_date,plan_end_date,chg_start_date,chg_end_date,rst_start_date,remark);
					
					//일정수정시 수정이력 입력할때
					if((chg_start_date.length() != 0) && (chg_end_date.length() != 0) && (chg_note.length() > 3)) {
						chgDAO.inputChangeSch(pjt_code,pjt_name,child_node,node_name,login_id,login_name,chg_note);
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//해당멤버의 과제전체 List
					ArrayList table_list = new ArrayList();
					table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
					request.setAttribute("PJT_List", table_list);

					//해당과제 일정 List
					ArrayList sch_list = new ArrayList();
					sch_list = schDAO.getPjtSchedule(pjt_code,"0","0");
					request.setAttribute("SCH_List", sch_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/pjt/pm/projectSch_main.jsp").forward(request,response);
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


