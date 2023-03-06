import com.anbtech.text.Hanguel;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class TechnicalServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;

	/********
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}
	/**********************************
	 * get방식으로 넘어왔을 때 처리
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		
		String mode = Hanguel.toHanguel(request.getParameter("mode"));		
			if(mode == null) mode = "";													//기술문서임
		//처리메시지 출력하기 : AnBDMS에서 분기되어 넘어옴
		out.println("	<script>");
		//out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
		out.println(" alert('상신되었습니다.');");
		out.println("	</script>");
		out.close();
	}

	/**********************************
	 * post방식으로 넘어왔을 때 처리
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//기술문서 정보
		String tablename = Hanguel.toHanguel(request.getParameter("tablename"));		
			if(tablename == null) tablename = "";										//table name
		String mode = Hanguel.toHanguel(request.getParameter("mode"));		
			if(mode == null) mode = "";													//기술문서임
		String d_id = Hanguel.toHanguel(request.getParameter("d_id"));			
			if(d_id == null) d_id = "";													//기술문서 관리번호
		String ver = Hanguel.toHanguel(request.getParameter("ver"));			
			if(ver == null) ver = "";													//기술문서 version
		String td_pid = d_id+"|"+tablename+"|"+ver;
			
		
		//양식종류별 전자결재 관리항목 (공통)
		String app_mode = Hanguel.toHanguel(request.getParameter("app_mode"));		if(app_mode == null) app_mode = "";		//전자결재 처리모드
		String writer_id = Hanguel.toHanguel(request.getParameter("writer_id"));		if(writer_id == null) writer_id = "";	//등록자(대신등록자 일수도 있음)
		String writer_name = Hanguel.toHanguel(request.getParameter("writer_name"));	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid = Hanguel.toHanguel(request.getParameter("doc_id"));			if(doc_pid == null) doc_pid = "";	//관리번호 (1차시는 관련관리번호와 동일)
		String doc_line = Hanguel.toHanguel(request.getParameter("doc_app_line"));	if(doc_line == null) doc_line = "";	//결재선
		String doc_subj = Hanguel.toHanguel(request.getParameter("doc_sub"));		if(doc_subj == null) doc_subj = "";	//제목
		String doc_peri = Hanguel.toHanguel(request.getParameter("doc_per"));		if(doc_peri == null) doc_peri = "";	//보존기간
		String doc_secu = Hanguel.toHanguel(request.getParameter("doc_sec"));		if(doc_secu == null) doc_secu = "";	//보안등급
		String writer_date = Hanguel.toHanguel(request.getParameter("date"));		if(writer_date == null) writer_date = "";	//입력일 시분

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			con.setAutoCommit(false);	// 트랜잭션을 시작  
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
			try{
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.gw.db.AppModuleAttchDAO attchDAO = new com.anbtech.gw.db.AppModuleAttchDAO(con);	//첨부수량

				//전자결재 TABEL (app_master) 입력하기
				masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","기술문서",td_pid,"TD");
					
				//첨부파일 수량 update하기
				int atcnt = attchDAO.searchAttchCntTD(d_id);
				masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					
				//관련문서 Table에 전자결재 현상태 알려 주기
				//response.sendRedirect("AnBDMS?mode=review&tablename="+tablename+"&d_id="+d_id+"&ver="+ver+"&aid="+doc_pid);
				com.anbtech.dms.db.MasterDAO mDAO = new com.anbtech.dms.db.MasterDAO(con);
				mDAO.updateStat(tablename,d_id,ver,"2");
				if(ver.equals("1.0")) mDAO.updateStat("","2",d_id);

				con.commit();
				con.setAutoCommit(true);

				//분기하기AnBDMS?mode=mylist&tablename=techdoc_data
				//getServletContext().getRequestDispatcher("AnBDMS?mode=review&tablename="+tablename+"&d_id="+d_id+"&ver="+ver+"&aid="+doc_pid).forward(request,response);
				getServletContext().getRequestDispatcher("/dms/result_app.jsp?tablename="+tablename).forward(request,response);
			}catch(Exception e){
				con.rollback();
				con.setAutoCommit(true);
				request.setAttribute("ERR_MSG",e.toString());
				getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
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