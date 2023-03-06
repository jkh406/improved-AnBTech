import com.anbtech.bm.db.*;
import com.anbtech.bm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class BomApprovalServlet extends HttpServlet {
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
	 * post방식으로 넘어왔을 때 처리 
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

		//BOM 결재상시시
		String app_mode		= Hanguel.toHanguel(request.getParameter("app_mode"));		if(app_mode == null) app_mode = "";			//전자결재 처리모드
		String writer_id	= Hanguel.toHanguel(request.getParameter("writer_id"));		if(writer_id == null) writer_id = "";		//등록자(대신등록자 일수도 있음)
		String writer_name	= Hanguel.toHanguel(request.getParameter("writer_name"));	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid		= Hanguel.toHanguel(request.getParameter("doc_id"));		if(doc_pid == null) doc_pid = "";			//관리번호 (1차시는 관련관리번호와 동일)
		String doc_line		= Hanguel.toHanguel(request.getParameter("doc_app_line"));	if(doc_line == null) doc_line = "";			//결재선
		String doc_subj		= Hanguel.toHanguel(request.getParameter("doc_sub"));		if(doc_subj == null) doc_subj = "";			//제목
		String doc_peri		= Hanguel.toHanguel(request.getParameter("doc_per"));		if(doc_peri == null) doc_peri = "";			//보존기간
		String doc_secu		= Hanguel.toHanguel(request.getParameter("doc_sec"));		if(doc_secu == null) doc_secu = "";			//보안등급
		String writer_date	= Hanguel.toHanguel(request.getParameter("date"));			if(writer_date == null) writer_date = "";	//입력일 시분
		String id			= Hanguel.toHanguel(request.getParameter("id"));			if(id == null) id = "";						//BOM 관리번호
		String mode			= Hanguel.toHanguel(request.getParameter("mode"));			if(mode == null) mode = "";					//처리모드
		
		//설계변경 결재상신시
		String eco_no		= Hanguel.toHanguel(request.getParameter("eco_no"));		if(eco_no == null) eco_no = "";				//설계변경번호
		String attache_cnt	= Hanguel.toHanguel(request.getParameter("attache_cnt"));	if(attache_cnt == null) attache_cnt = "";	//첨부파일갯수
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			con.setAutoCommit(false);	// 트랜잭션을 시작  
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			try{
				//-----------------------------
				// BOM 결재상신 하기
				//-----------------------------
				if("info_app".equals(mode)){
					con.setAutoCommit(false);	// 트랜잭션을 시작  
					con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try{
						com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
						com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);

						//전자결재 TABEL (app_master) 입력하기
						masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","BOM승인",id,"BOM");
						
						//MBOM MASTER의 진행상태 (bom_status='4' :결재상신)으로 바꿔주기
						bomDAO.setBomStatus (id,"4","","","");

						con.commit(); // commit한다.

						//처리메시지 출력하기
						//out.println("<script>	alert('상신되었습니다.'); window.returnValue='RL';	self.close(); </script>");	out.close();
						out.println("	<script>");
						out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
						out.println("	</script>");
						out.close();
					}catch(Exception e){
						con.rollback();
						request.setAttribute("ERR_MSG",e.toString());
						getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
					}finally{
						con.setAutoCommit(true);
					}
				}
				//-----------------------------
				// 설계변경 ECR,ECO 결재상신 하기
				//-----------------------------
				else if("ecc_app".equals(mode)){
					con.setAutoCommit(false);	// 트랜잭션을 시작  
					con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try{
						com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
						com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);

						//전자결재 TABEL (app_master) 입력하기
						masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","설계변경승인",id+"|"+eco_no,"DCM");
						
						//첨부파일 수량 update하기
						int atcnt = Integer.parseInt(attache_cnt);
						masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

						//결재상신 상태 반영
						bomDAO.setEccStatus(id,"","");

						//ECC BOM의 변경방지
						bomDAO.setEccBomStatus(eco_no,"1");

						con.commit(); // commit한다.

						//처리메시지 출력하기
						//out.println("<script>	alert('상신되었습니다.'); window.returnValue='RL';	self.close(); </script>");	out.close();
						out.println("	<script>");
						out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
						out.println("	</script>");
						out.close();
					}catch(Exception e){
						con.rollback();
						request.setAttribute("ERR_MSG",e.toString());
						getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
					}finally{
						con.setAutoCommit(true);
					}				}
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
		}
	} //doPost()
}
