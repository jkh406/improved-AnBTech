import com.anbtech.mr.entity.*;
import com.anbtech.mr.db.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class asresultworkServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 15;
	private int max_display_page = 5;

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"ART_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"as_field":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//실적 정보 파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String register_no = Hanguel.toHanguel(request.getParameter("register_no"))==null?"":Hanguel.toHanguel(request.getParameter("register_no"));
		String register_date = Hanguel.toHanguel(request.getParameter("register_date"))==null?"":Hanguel.toHanguel(request.getParameter("register_date"));
		String as_field = Hanguel.toHanguel(request.getParameter("as_field"))==null?"":Hanguel.toHanguel(request.getParameter("as_field"));
		String code = Hanguel.toHanguel(request.getParameter("code"))==null?"":Hanguel.toHanguel(request.getParameter("code"));
		String request_name = Hanguel.toHanguel(request.getParameter("request_name"))==null?"":Hanguel.toHanguel(request.getParameter("request_name"));
		String serial_no = Hanguel.toHanguel(request.getParameter("serial_no"))==null?"":Hanguel.toHanguel(request.getParameter("serial_no"));
		String request_date = Hanguel.toHanguel(request.getParameter("request_date"))==null?"":Hanguel.toHanguel(request.getParameter("request_date"));
		String as_date = Hanguel.toHanguel(request.getParameter("as_date"))==null?"":Hanguel.toHanguel(request.getParameter("as_date"));
		String as_type = Hanguel.toHanguel(request.getParameter("as_type"))==null?"":Hanguel.toHanguel(request.getParameter("as_type"));
		String as_content = Hanguel.toHanguel(request.getParameter("as_content"))==null?"":Hanguel.toHanguel(request.getParameter("as_content"));
		String as_result = Hanguel.toHanguel(request.getParameter("as_result"))==null?"":Hanguel.toHanguel(request.getParameter("as_result"));
		String as_delay = Hanguel.toHanguel(request.getParameter("as_delay"))==null?"":Hanguel.toHanguel(request.getParameter("as_delay"));
		String as_issue = Hanguel.toHanguel(request.getParameter("as_issue"))==null?"":Hanguel.toHanguel(request.getParameter("as_issue"));
		String worker = Hanguel.toHanguel(request.getParameter("worker"))==null?"":Hanguel.toHanguel(request.getParameter("worker"));
		String company_no = Hanguel.toHanguel(request.getParameter("company_no"))==null?"":Hanguel.toHanguel(request.getParameter("company_no"));
		String value_request = Hanguel.toHanguel(request.getParameter("value_request"))==null?"":Hanguel.toHanguel(request.getParameter("value_request"));
		
		//다시리턴할 파라미터
		com.anbtech.mr.entity.assupportTable table = new com.anbtech.mr.entity.assupportTable();
		ArrayList para_list = new ArrayList();
			table.setCompanyNo(company_no);			//업체코드
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	실적LIST 가져오기
			//--------------------------------------------------------------------
			if("ART_L".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//업체코드별 실적 전체 List
				ArrayList table_list = new ArrayList();
				table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("WORK_List", table_list);

				//페이지로 바로가기 List
				ArrayList page_list = new ArrayList();
				page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", page_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
			}
			//실적이력 등록된 내용보기
			else if("ART_V".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//AS지원목록 정보
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);

				//입력된 실적정보
				ArrayList work_list = new ArrayList();
				work_list = workDAO.getWorkRead(pid);
				request.setAttribute("WORK_List", work_list);

				//분기하기
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKview.jsp").forward(request,response);
			}
			//실적이력 등록 준비
			else if("ART_WV".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

/*				//login user 기본정보 [업체코드,login id,login name 등]
				ArrayList node_list = new ArrayList();
				node_list = workDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);
*/
				//AS지원목록 정보
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);
				
				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKwrite.jsp").forward(request,response);
			}
		
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"ART_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"as_field":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//실적 정보 파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String register_no = Hanguel.toHanguel(request.getParameter("register_no"))==null?"":Hanguel.toHanguel(request.getParameter("register_no"));
		String register_date = Hanguel.toHanguel(request.getParameter("register_date"))==null?"":Hanguel.toHanguel(request.getParameter("register_date"));
		String as_field = Hanguel.toHanguel(request.getParameter("as_field"))==null?"":Hanguel.toHanguel(request.getParameter("as_field"));
		String code = Hanguel.toHanguel(request.getParameter("code"))==null?"":Hanguel.toHanguel(request.getParameter("code"));
		String request_name = Hanguel.toHanguel(request.getParameter("request_name"))==null?"":Hanguel.toHanguel(request.getParameter("request_name"));
		String serial_no = Hanguel.toHanguel(request.getParameter("serial_no"))==null?"":Hanguel.toHanguel(request.getParameter("serial_no"));
		String request_date = Hanguel.toHanguel(request.getParameter("request_date"))==null?"":Hanguel.toHanguel(request.getParameter("request_date"));
		String as_date = Hanguel.toHanguel(request.getParameter("as_date"))==null?"":Hanguel.toHanguel(request.getParameter("as_date"));
		String as_type = Hanguel.toHanguel(request.getParameter("as_type"))==null?"":Hanguel.toHanguel(request.getParameter("as_type"));
		String as_content = Hanguel.toHanguel(request.getParameter("as_content"))==null?"":Hanguel.toHanguel(request.getParameter("as_content"));
		String as_result = Hanguel.toHanguel(request.getParameter("as_result"))==null?"":Hanguel.toHanguel(request.getParameter("as_result"));
		String as_delay = Hanguel.toHanguel(request.getParameter("as_delay"))==null?"":Hanguel.toHanguel(request.getParameter("as_delay"));
		String as_issue = Hanguel.toHanguel(request.getParameter("as_issue"))==null?"":Hanguel.toHanguel(request.getParameter("as_issue"));
		String worker = Hanguel.toHanguel(request.getParameter("worker"))==null?"":Hanguel.toHanguel(request.getParameter("worker"));
		String company_no = Hanguel.toHanguel(request.getParameter("company_no"))==null?"":Hanguel.toHanguel(request.getParameter("company_no"));
		String value_request = Hanguel.toHanguel(request.getParameter("value_request"))==null?"":Hanguel.toHanguel(request.getParameter("value_request"));
		
		//다시리턴할 파라미터
		com.anbtech.mr.entity.assupportTable table = new com.anbtech.mr.entity.assupportTable();
		ArrayList para_list = new ArrayList();
			table.setCompanyNo(company_no);			//업체코드
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	실적LIST 가져오기
			//--------------------------------------------------------------------
			//실적 전체LIST
			if("ART_L".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//업체코드별 실적 전체 List
				ArrayList table_list = new ArrayList();
				table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("WORK_List", table_list);

				//페이지로 바로가기 List
				ArrayList page_list = new ArrayList();
				page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", page_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
			}
			//실적이력 등록 준비
			else if("ART_WV".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

/*				//login user 기본정보 [업체코드,login id,login name 등]
				ArrayList node_list = new ArrayList();
				node_list = workDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);
*/
				//AS지원목록 정보
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKwrite.jsp").forward(request,response);
			}
			//실적이력 등록하기
			else if("ART_W".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					workDAO.inputWork(as_field,code,request_name,serial_no,request_date,as_date,as_type,as_content,as_result,as_delay,as_issue,worker,company_no);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//업체코드별 실적 전체 List
					ArrayList table_list = new ArrayList();
					table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
					request.setAttribute("WORK_List", table_list);

					//페이지로 바로가기 List
					ArrayList page_list = new ArrayList();
					page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
					request.setAttribute("PAGE_List", page_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//실적이력 등록된 내용보기
			else if("ART_V".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//AS지원목록 정보
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);

				//입력된 실적정보
				ArrayList work_list = new ArrayList();
				work_list = workDAO.getWorkRead(pid);
				request.setAttribute("WORK_List", work_list);

				//분기하기
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKview.jsp").forward(request,response);
			}
			//실적이력 수정하기 준비
			else if("ART_MV".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//AS지원목록 정보
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);

				//입력된 실적정보
				ArrayList work_list = new ArrayList();
				work_list = workDAO.getWorkRead(pid);
				request.setAttribute("WORK_List", work_list);

				//받은 파라미터 다시리턴하기
				request.setAttribute("PARA_List", para_list);

				//분기하기
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKmodify.jsp").forward(request,response);
			}
			//실적이력 수정하기
			else if("ART_M".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					workDAO.updateWork(pid,as_field,serial_no,request_date,as_date,as_type,as_content,as_result,as_delay,as_issue);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//업체코드별 실적 전체 List
					ArrayList table_list = new ArrayList();
					table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
					request.setAttribute("WORK_List", table_list);

					//페이지로 바로가기 List
					ArrayList page_list = new ArrayList();
					page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
					request.setAttribute("PAGE_List", page_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//실적이력 삭제하기
			else if("ART_D".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					workDAO.deleteWork(pid);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//업체코드별 실적 전체 List
					ArrayList table_list = new ArrayList();
					table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
					request.setAttribute("WORK_List", table_list);

					//페이지로 바로가기 List
					ArrayList page_list = new ArrayList();
					page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
					request.setAttribute("PAGE_List", page_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//실적 평가 이메일 보내기
			else if("ART_S".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//workDAO.sendMailToDIV(pid);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//업체코드별 실적 전체 List
					ArrayList table_list = new ArrayList();
					table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
					request.setAttribute("WORK_List", table_list);

					//페이지로 바로가기 List
					ArrayList page_list = new ArrayList();
					page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
					request.setAttribute("PAGE_List", page_list);

					//받은 파라미터 다시리턴하기
					request.setAttribute("PARA_List", para_list);
					
					//분기하기
					getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
				
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



