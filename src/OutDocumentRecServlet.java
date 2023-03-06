import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class OutDocumentRecServlet extends HttpServlet {

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

		//Session
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;

		//전자결재 관리항목 (공통)
		String app_mode = request.getParameter("app_mode");		if(app_mode == null) app_mode = "";		//전자결재 처리모드
		String writer_id = request.getParameter("writer_id");		if(writer_id == null) writer_id = "";	//등록자(대신등록자 일수도 있음)
		String writer_name = request.getParameter("writer_name");	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid = request.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";	//관리번호 (1차시는 관련관리번호와 동일)
		String doc_line = request.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";	//결재선
		String doc_subj = request.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";	//제목
		String doc_peri = request.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";	//보존기간
		String doc_secu = request.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";	//보안등급
		String writer_date = request.getParameter("date");		if(writer_date == null) writer_date = "";	//입력일 시분

		//관리코드
		String id = Hanguel.toHanguel(request.getParameter("id"))==null?"":Hanguel.toHanguel(request.getParameter("id"));

		//모드 및 현재 페이지 파리미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"IND_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"subject":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//삭제를 위한 파일path
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");

		//공문 공유자 지정 및 배포하기 (사번/이름;...)
		//share_id : 배포면 부서정보, 공문전달이면 부서원 정보임
		String share_id = Hanguel.toHanguel(request.getParameter("share_id"));	if(share_id == null) share_id = "";			
		String tablename = Hanguel.toHanguel(request.getParameter("tablename"));	if(tablename == null) tablename = "";	
		
		//게시판의 읽은 횟수 카운터 해주기
		String no = request.getParameter("no"); if(no == null) no = "";

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
		
			//사외공문수신수신 전체 LIST가져오기
			if("ODR_L".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getDoc_List(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = docDAO.getCurrentPage();		//현재페이지
				int Tpage = docDAO.getTotalPage();			//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/ods/OutDocumentRec_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//사외공문수신 개별내용 보기 
			else if("ODR_V".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);

				//게시판에서 조회시 조회 카운더 하기
				if(no.length() > 0) {
					com.anbtech.board.business.TableBO cntBO = new com.anbtech.board.business.TableBO(con);
					cntBO.updRid("notice_board",no,request.getRemoteAddr());
				}
				
				getServletContext().getRequestDispatcher("/ods/OutDocumentRec_review.jsp").forward(request,response);
			}
			//사외공문수신 개별내용 수정하기
			else if("ODR_M".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocumentRec_modify.jsp").forward(request,response);
			}
			//사외공문수신 개별내용 삭제하기
			else if("ODR_D".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				//-------------------------------------
				// 해당 관리번호 삭제 실행하기
				//-------------------------------------
				con.setAutoCommit(false);	// 트랜잭션을 시작 
				try{
					docDAO.deletePid(id,upload_path);
					con.commit();
					con.setAutoCommit(true);

					//-------------------------------------
					// 삭제실행후 저장함 리스트 보기
					//-------------------------------------
					//전체 List
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_List(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					//현재페이지/전체페이지
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();			//전체페이지
					//분기하기
					getServletContext().getRequestDispatcher("/ods/OutDocumentRec_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				
				} catch (Exception e) {
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
				
			}

		}catch (Exception e){
			//에러출력 페이지로 분기
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

		//Session
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;

		//전자결재 관리항목 (공통)
		String app_mode = Hanguel.toHanguel(request.getParameter("app_mode"));		if(app_mode == null) app_mode = "";		//전자결재 처리모드
		String writer_id = Hanguel.toHanguel(request.getParameter("writer_id"));		if(writer_id == null) writer_id = "";	//등록자(대신등록자 일수도 있음)
		String writer_name = Hanguel.toHanguel(request.getParameter("writer_name"));	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid = Hanguel.toHanguel(request.getParameter("doc_id"));			if(doc_pid == null) doc_pid = "";	//관리번호 (1차시는 관련관리번호와 동일)
		String doc_line = Hanguel.toHanguel(request.getParameter("doc_app_line"));	if(doc_line == null) doc_line = "";	//결재선
		String doc_subj = Hanguel.toHanguel(request.getParameter("doc_sub"));		if(doc_subj == null) doc_subj = "";	//제목
		String doc_peri = Hanguel.toHanguel(request.getParameter("doc_per"));		if(doc_peri == null) doc_peri = "";	//보존기간
		String doc_secu = Hanguel.toHanguel(request.getParameter("doc_sec"));		if(doc_secu == null) doc_secu = "";	//보안등급
		String writer_date = Hanguel.toHanguel(request.getParameter("date"));		if(writer_date == null) writer_date = "";	//입력일 시분
		String attache_cnt = Hanguel.toHanguel(request.getParameter("attache_cnt"));	if(attache_cnt == null) attache_cnt = "0";	//첨부파일 갯수
		int atcnt = Integer.parseInt(attache_cnt);

		//관리코드
		String id = Hanguel.toHanguel(request.getParameter("id"))==null?"":Hanguel.toHanguel(request.getParameter("id"));

		//모드 및 현재 페이지 파리미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"ODR_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"subject":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//삭제를 위한 파일path
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");

		//공문 공유자 지정 및 배포하기 (사번/이름;...)
		//share_id : 배포면 부서정보, 공문전달이면 부서원 정보임
		String share_id = Hanguel.toHanguel(request.getParameter("share_id"));	if(share_id == null) share_id = "";			
		String tablename = Hanguel.toHanguel(request.getParameter("tablename"));	if(tablename == null) tablename = "";	
		
		//	게시판에 remote ip address 넘겨주기위해서
		String ip_addr = request.getRemoteAddr();

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
		
			//사외공문수신 전체 LIST가져오기
			if("ODR_L".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				//전체 List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getDoc_List(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//현재페이지/전체페이지
				int Cpage = docDAO.getCurrentPage();		//현재페이지
				int Tpage = docDAO.getTotalPage();			//전체페이지
				
				//분기하기
				getServletContext().getRequestDispatcher("/ods/OutDocumentRec_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//사외공문수신 개별내용 보기
			else if("ODR_V".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocumentRec_review.jsp").forward(request,response);
			}
			//사외공문수신 개별내용 수정하기 데이터 읽기
			else if("ODR_M".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocumentRec_modify.jsp").forward(request,response);
			}
			//사외공문수신 개별내용 삭제하기
			else if("ODR_D".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				//-------------------------------------
				// 해당 관리번호 삭제 실행하기
				//-------------------------------------
				con.setAutoCommit(false);	// 트랜잭션을 시작 
				try{
					docDAO.deletePid(id,upload_path);
					con.commit();
					con.setAutoCommit(true);

					//-------------------------------------
					// 삭제실행후 저장함 리스트 보기
					//-------------------------------------
					//전체 List
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_List(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					//현재페이지/전체페이지
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();			//전체페이지
					//분기하기
					getServletContext().getRequestDispatcher("/ods/OutDocumentRec_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				

				} catch (Exception e) {
					con.rollback();
					con.setAutoCommit(true);

					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
		
			}
			//사외공문수신 공문전달[공유자 지정]
			else if("ODR_S".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				//-------------------------------------
				// 공유자 지정하기
				//-------------------------------------
				con.setAutoCommit(false);	// 트랜잭션을 시작 
				try{
					docDAO.shareReceiver(login_id,id,share_id);
					con.commit();
					con.setAutoCommit(true);

					//닫기
					out.println("<script> alert('공문전달 되었습니다.'); top.close(); </script>");
					out.close();


				} catch (Exception e) {
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
				
			}
			//사외공문수신 배포전달
			else if("ODR_G".equals(mode)){	
				com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
				//-------------------------------------
				// 배포하기 : 부서장에게
				//-------------------------------------
				con.setAutoCommit(false);	// 트랜잭션을 시작 
				try {
					String rtn = docDAO.distributeODRTable(login_id,id,share_id,ip_addr);
					if(rtn.equals("ok")) {
						con.commit();
						con.setAutoCommit(true);
						out.println("<script> alert('공문배포 되었습니다.'); parent.winClose(); </script>");
						out.close();
					} else {
						con.rollback();
						con.setAutoCommit(true);
						out.println("<script> alert('지정된 사업부의 부서장["+rtn+"] 이 미등록되었습니다. 관리자에게 문의하십시요.'); history.back(-1); </script>");
						out.close();
					} 
				} catch (Exception e) {
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				} 
				
			}
		}catch (Exception e){
			//에러출력 페이지로 분기
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}

	} //doPost()
}

