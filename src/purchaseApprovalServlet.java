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

public class purchaseApprovalServlet extends HttpServlet {
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

		//구매관리(구매요청,발주요청) 결재상시시
		String app_mode		= Hanguel.toHanguel(request.getParameter("app_mode"));		if(app_mode == null) app_mode = "";			//전자결재 처리모드
		String writer_id	= Hanguel.toHanguel(request.getParameter("writer_id"));		if(writer_id == null) writer_id = "";		//등록자(대신등록자 일수도 있음)
		String writer_name	= Hanguel.toHanguel(request.getParameter("writer_name"));	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid		= Hanguel.toHanguel(request.getParameter("doc_id"));		if(doc_pid == null) doc_pid = "";			//관리번호 (1차시는 관련관리번호와 동일)
		String doc_line		= Hanguel.toHanguel(request.getParameter("doc_app_line"));	if(doc_line == null) doc_line = "";			//결재선
		String doc_subj		= Hanguel.toHanguel(request.getParameter("doc_sub"));		if(doc_subj == null) doc_subj = "";			//제목
		String doc_peri		= Hanguel.toHanguel(request.getParameter("doc_per"));		if(doc_peri == null) doc_peri = "";			//보존기간
		String doc_secu		= Hanguel.toHanguel(request.getParameter("doc_sec"));		if(doc_secu == null) doc_secu = "";			//보안등급
		String writer_date	= Hanguel.toHanguel(request.getParameter("date"));			if(writer_date == null) writer_date = "";	//입력일 시분
		String req_no		= Hanguel.toHanguel(request.getParameter("req_no"));		if(req_no == null) req_no = "";				//구매요청 번호
		String req_type		= Hanguel.toHanguel(request.getParameter("req_type"));		if(req_type == null) req_type = "";			//구매요청 TYPE
		String mode			= Hanguel.toHanguel(request.getParameter("mode"));			if(mode == null) mode = "";					//처리모드
		
		//구매입고결재상시
		String enter_no		= Hanguel.toHanguel(request.getParameter("enter_no"));		if(enter_no == null) enter_no = "";			//구매입고 번호
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			con.setAutoCommit(false);	// 트랜잭션을 시작  
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			try{
				//-----------------------------
				// 구매요청 결재상신 하기
				//-----------------------------
				if("purchase_app".equals(mode)){
					con.setAutoCommit(false);	// 트랜잭션을 시작  
					con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try{
						com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
						com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//구매관리
			
						//전자결재 TABEL (app_master) 입력하기
						masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","구매요청",req_no+"|"+req_type,"PCR");
						
						//구매요청에 전달하기
						purBO.puRequestAppInfoProcess("app_req","",req_no);

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
				// 발주요청 결재상신 하기
				//-----------------------------
				else if("order_app".equals(mode)){
					con.setAutoCommit(false);	// 트랜잭션을 시작  
					con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try{
						com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
						com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//구매관리
			
						//전자결재 TABEL (app_master) 입력하기
						masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","발주요청",req_no,"ODR");
						
						//발주요청 
						purBO.puOrderAppInfoProcess("app_order","",req_no);

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
				// 구매입고 결재상신 하기
				//-----------------------------
				else if("app_enter".equals(mode)){
					con.setAutoCommit(false);	// 트랜잭션을 시작  
					con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try{
						com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
						com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//구매관리
			
						//전자결재 TABEL (app_master) 입력하기
						masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","구매입고",enter_no,"PWH");
						
						//구매입고 
						//purBO.puEnterAppInfoProcess("app_enter",doc_pid,enter_no);

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
