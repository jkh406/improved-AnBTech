import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ApprovalAssetServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;

	/********
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
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

		//자산 반출/이관 정보
		String h_no = request.getParameter("h_no");			if(h_no == null) h_no = "";				//관리번호
		String as_no = request.getParameter("as_no");		if(as_no == null) as_no = "";			//자산번호
		String as_status = request.getParameter("as_status");if(as_status == null) as_status = "";	//결재상태

		//양식종류별 전자결재 관리항목 (공통)
		String app_mode		= Hanguel.toHanguel(request.getParameter("app_mode"));		if(app_mode == null) app_mode = "";		//전자결재 처리모드
		String writer_id	= Hanguel.toHanguel(request.getParameter("writer_id"));		if(writer_id == null) writer_id = "";	//등록자(대신등록자 일수도 있음)
		String writer_name	= Hanguel.toHanguel(request.getParameter("writer_name"));	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid		= Hanguel.toHanguel(request.getParameter("doc_id"));			if(doc_pid == null) doc_pid = "";	//관리번호 (1차시는 관련관리번호와 동일)
		String doc_line		= Hanguel.toHanguel(request.getParameter("doc_app_line"));	if(doc_line == null) doc_line = "";	//결재선
		String doc_subj		= Hanguel.toHanguel(request.getParameter("doc_sub"));		if(doc_subj == null) doc_subj = "";	//제목
		String doc_peri		= Hanguel.toHanguel(request.getParameter("doc_per"));		if(doc_peri == null) doc_peri = "";	//보존기간
		String doc_secu		= Hanguel.toHanguel(request.getParameter("doc_sec"));		if(doc_secu == null) doc_secu = "";	//보안등급
		String writer_date = Hanguel.toHanguel(request.getParameter("date"));		if(writer_date == null) writer_date = "";	//입력일 시분
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
			com.anbtech.am.business.AssetModuleBO appAs = new com.anbtech.am.business.AssetModuleBO(con);

			con.setAutoCommit(false);	// 트랜잭션을 시작  
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			try{
				//전자결재 TABEL (app_master) 입력하기
				masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","자산관리",h_no,"ASSET");

				//자산관리의 전자상신 상태코드를 입력한다.
				if(as_status.equals("1")) {		//1차상신
					appAs.asAppInfoProcess(doc_pid,h_no,"submit","1");
				} else {						//2차상신
					appAs.asAppInfoProcess(doc_pid,h_no,"submit","2");
				}
				con.commit(); // commit한다.
				
				//처리메시지 출력하기
				out.println("	<script>");
				if(as_status.equals("1")) 		//1차상신
					out.println("	location.href('AssetServlet?mode=req_app_list');");
				else							//2차상신
					out.println("	location.href('AssetServlet?mode=lending_list');");
				out.println("	</script>");
				out.close();
			}catch(Exception e){
				con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			}finally{
				con.setAutoCommit(true);
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