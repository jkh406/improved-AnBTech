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

public class projectGenServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PBS_LA":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
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
			//	과제기본정보 LIST가져오기
			//--------------------------------------------------------------------
			//전체 LIST가져오기
			if("PBS_LA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = genDAO.getCurrentPage();		//현재페이지
				int Tpage = genDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//부서공통 전체 LIST가져오기
			else if("PBS_LD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = genDAO.getCurrentPage();		//현재페이지
				int Tpage = genDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PBS_LA":Hanguel.toHanguel(request.getParameter("mode"));
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
		String owner = Hanguel.toHanguel(request.getParameter("owner"))==null?"":Hanguel.toHanguel(request.getParameter("owner"));					//owner
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));			//입력일
		String pjt_mbr_id = Hanguel.toHanguel(request.getParameter("pjt_mbr_id"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_mbr_id"));			//
		String pjt_class = Hanguel.toHanguel(request.getParameter("pjt_class"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_class"));			//
		String pjt_target = Hanguel.toHanguel(request.getParameter("pjt_target"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_target"));			//
		String mgt_plan = Hanguel.toHanguel(request.getParameter("mgt_plan"))==null?"":Hanguel.toHanguel(request.getParameter("mgt_plan"));			//
		String parent_code = Hanguel.toHanguel(request.getParameter("parent_code"))==null?"":Hanguel.toHanguel(request.getParameter("parent_code"));			//
		String mbr_exp = Hanguel.toHanguel(request.getParameter("mbr_exp"))==null?"":Hanguel.toHanguel(request.getParameter("mbr_exp"));			//
		String cost_exp = Hanguel.toHanguel(request.getParameter("cost_exp"))==null?"":Hanguel.toHanguel(request.getParameter("cost_exp"));			//
		String plan_start_date = Hanguel.toHanguel(request.getParameter("plan_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_start_date"));			//
		String plan_end_date = Hanguel.toHanguel(request.getParameter("plan_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_end_date"));			//
		String chg_start_date = Hanguel.toHanguel(request.getParameter("chg_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("chg_start_date"));			//
		String chg_end_date = Hanguel.toHanguel(request.getParameter("chg_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("chg_end_date"));			//
		String rst_start_date = Hanguel.toHanguel(request.getParameter("rst_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("rst_start_date"));			//
		String rst_end_date = Hanguel.toHanguel(request.getParameter("rst_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("rst_end_date"));			//
		String prs_code = Hanguel.toHanguel(request.getParameter("prs_code"))==null?"":Hanguel.toHanguel(request.getParameter("prs_code"));			//
		String prs_type = Hanguel.toHanguel(request.getParameter("prs_type"))==null?"":Hanguel.toHanguel(request.getParameter("prs_type"));			//
		String pjt_desc = Hanguel.toHanguel(request.getParameter("pjt_desc"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_desc"));			//
		String pjt_spec = Hanguel.toHanguel(request.getParameter("pjt_spec"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_spec"));			//
		String pjt_status = Hanguel.toHanguel(request.getParameter("pjt_status"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_status"));			//
		String flag = Hanguel.toHanguel(request.getParameter("flag"))==null?"":Hanguel.toHanguel(request.getParameter("flag"));			//
		String plan_labor = Hanguel.toHanguel(request.getParameter("plan_labor"))==null?"":Hanguel.toHanguel(request.getParameter("plan_labor"));			//
		String plan_sample = Hanguel.toHanguel(request.getParameter("plan_sample"))==null?"":Hanguel.toHanguel(request.getParameter("plan_sample"));			//
		String plan_metal = Hanguel.toHanguel(request.getParameter("plan_metal"))==null?"":Hanguel.toHanguel(request.getParameter("plan_metal"));			//
		String plan_mup = Hanguel.toHanguel(request.getParameter("plan_mup"))==null?"":Hanguel.toHanguel(request.getParameter("plan_mup"));			//
		String plan_oversea = Hanguel.toHanguel(request.getParameter("plan_oversea"))==null?"":Hanguel.toHanguel(request.getParameter("plan_oversea"));			//
		String plan_plant = Hanguel.toHanguel(request.getParameter("plan_plant"))==null?"":Hanguel.toHanguel(request.getParameter("plan_plant"));			//
		String result_labor = Hanguel.toHanguel(request.getParameter("result_labor"))==null?"":Hanguel.toHanguel(request.getParameter("result_labor"));			//
		String result_sample = Hanguel.toHanguel(request.getParameter("result_sample"))==null?"":Hanguel.toHanguel(request.getParameter("result_sample"));			//
		String result_metal = Hanguel.toHanguel(request.getParameter("result_metal"))==null?"":Hanguel.toHanguel(request.getParameter("result_metal"));			//
		String result_mup = Hanguel.toHanguel(request.getParameter("result_mup"))==null?"":Hanguel.toHanguel(request.getParameter("result_mup"));			//
		String result_oversea = Hanguel.toHanguel(request.getParameter("result_oversea"))==null?"":Hanguel.toHanguel(request.getParameter("result_oversea"));			//
		String result_plant = Hanguel.toHanguel(request.getParameter("result_plant"))==null?"":Hanguel.toHanguel(request.getParameter("result_plant"));			//

		//다시리턴할 파라미터
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtword(pjtWord);		//과제상태
			table.setSitem(sItem);			//sItem
			table.setSword(sWord);			//sWord
		para_list.add(table);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	과제코드 코드 편집하기
			//--------------------------------------------------------------------
			//과제관리 전사공통 전체 LIST가져오기
			if("PBS_LA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = genDAO.getCurrentPage();		//현재페이지
				int Tpage = genDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//과제관리 전사공통 신규입력하기
			else if("PBS_WA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.inputGeneral(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,plan_mup,plan_oversea,plan_plant);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//현재페이지
					int Tpage = genDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제관리 전사공통 수정내용가져오기
			else if("PBS_VA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getGeneralRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_modify.jsp").forward(request,response);
			}
			//과제관리 전사공통 수정하기
			else if("PBS_MA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.updateGeneral(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,chg_start_date,chg_end_date,prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,plan_mup,plan_oversea,plan_plant);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//현재페이지
					int Tpage = genDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제관리 전사공통 삭제하기
			else if("PBS_DA".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.deleteGeneral(pjt_code);
					con.commit(); // commit한다.
					con.setAutoCommit(true);
					
					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getAllGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//현재페이지
					int Tpage = genDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenAll_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);	
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제관리 부서공통 전체 LIST가져오기
			else if("PBS_LD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = genDAO.getCurrentPage();		//현재페이지
				int Tpage = genDAO.getTotalPage();		//전체페이지

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//과제관리 부서공통 신규입력하기
			else if("PBS_WD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.inputGeneral(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,plan_mup,plan_oversea,plan_plant);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);

					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//현재페이지
					int Tpage = genDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제관리 부서공통 수정내용가져오기
			else if("PBS_VD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);
				
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//개별내용 읽어 분기하기
				ArrayList table_list = new ArrayList();
				table_list = genDAO.getGeneralRead(pid);
				request.setAttribute("Data_List", table_list);
				getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_modify.jsp").forward(request,response);
			}
			//과제관리 부서공통 수정하기
			else if("PBS_MD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.updateGeneral(pid,pjt_code,pjt_name,owner,in_date,pjt_mbr_id,pjt_class,pjt_target,mgt_plan,parent_code,mbr_exp,cost_exp,plan_start_date,plan_end_date,chg_start_date,chg_end_date,prs_code,prs_type,pjt_desc,pjt_spec,pjt_status,plan_labor,plan_sample,plan_metal,plan_mup,plan_oversea,plan_plant);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//현재페이지
					int Tpage = genDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//과제관리 부서공통 삭제하기
			else if("PBS_DD".equals(mode)){	
				com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					genDAO.deleteGeneral(pjt_code);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//전체 List 로 분기하기
					ArrayList table_list = new ArrayList();
					table_list = genDAO.getDivGeneralList(login_id,pjtWord,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					int Cpage = genDAO.getCurrentPage();		//현재페이지
					int Tpage = genDAO.getTotalPage();		//전체페이지
					getServletContext().getRequestDispatcher("/pjt/pm/projectGenDiv_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
					
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

