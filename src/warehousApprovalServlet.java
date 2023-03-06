import com.anbtech.st.db.*;
import com.anbtech.st.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class warehousApprovalServlet extends HttpServlet {
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

		//부품출고 결재상시시
		String app_mode		= Hanguel.toHanguel(request.getParameter("app_mode"));		if(app_mode == null) app_mode = "";			//전자결재 처리모드
		String writer_id	= Hanguel.toHanguel(request.getParameter("writer_id"));		if(writer_id == null) writer_id = "";		//등록자(대신등록자 일수도 있음)
		String writer_name	= Hanguel.toHanguel(request.getParameter("writer_name"));	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid		= Hanguel.toHanguel(request.getParameter("doc_id"));		if(doc_pid == null) doc_pid = "";			//관리번호 (1차시는 관련관리번호와 동일)
		String doc_line		= Hanguel.toHanguel(request.getParameter("doc_app_line"));	if(doc_line == null) doc_line = "";			//결재선
		String doc_subj		= Hanguel.toHanguel(request.getParameter("doc_sub"));		if(doc_subj == null) doc_subj = "";			//제목
		String doc_peri		= Hanguel.toHanguel(request.getParameter("doc_per"));		if(doc_peri == null) doc_peri = "";			//보존기간
		String doc_secu		= Hanguel.toHanguel(request.getParameter("doc_sec"));		if(doc_secu == null) doc_secu = "";			//보안등급
		String writer_date	= Hanguel.toHanguel(request.getParameter("date"));			if(writer_date == null) writer_date = "";	//입력일 시분
		String out_no		= Hanguel.toHanguel(request.getParameter("out_no"));		if(out_no == null) out_no = "";				//구매요청 번호
		String mode			= Hanguel.toHanguel(request.getParameter("mode"));			if(mode == null) mode = "";					//처리모드
		
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			con.setAutoCommit(false);	// 트랜잭션을 시작  
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			try{
				//-----------------------------
				// 부품출고 결재상신 하기
				//-----------------------------
				if("app_delivery".equals(mode)){
					con.setAutoCommit(false);	// 트랜잭션을 시작  
					con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try{
						com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
						com.anbtech.st.business.StockMgrBO stcBO = new com.anbtech.st.business.StockMgrBO(con);//재고관리
			
						//전자결재 TABEL (app_master) 입력하기
						masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","부품출고",doc_pid,"TGW");
						
						//부품출고에 전달하기
						stcBO.DeliveryAppInfoProcess("app_delivery",out_no,doc_pid);

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
