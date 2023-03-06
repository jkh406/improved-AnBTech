import com.anbtech.gw.db.*;
import com.anbtech.gw.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ApprovalProcessServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	
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

		//기본파라미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"APP":Hanguel.toHanguel(request.getParameter("mode"));
		String pid = Hanguel.toHanguel(request.getParameter("PID"))==null?"":Hanguel.toHanguel(request.getParameter("PID"));
		String comment = Hanguel.toHanguel(request.getParameter("comment"))==null?"":Hanguel.toHanguel(request.getParameter("comment"));
		String passwd = Hanguel.toHanguel(request.getParameter("passwd"))==null?"":Hanguel.toHanguel(request.getParameter("passwd"));
		
		//승인결재후 email을 보낼경우 본문내용 저장키 위한 저장 Directory 알려주기 (upload + crp )
		String path = Hanguel.toHanguel(request.getParameter("path"))==null?"":Hanguel.toHanguel(request.getParameter("path"));
		String link_id = Hanguel.toHanguel(request.getParameter("link_id"))==null?"":Hanguel.toHanguel(request.getParameter("link_id"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		
		//결재시 비밀번호 묻기 여부 변수
		String pass = "OK";	//비밀번호 묻기 결과 묻지 않음 (pass ok)
		String chk = "";	//비밀번호 묻기여부 판단하기
		String step = "";	//APV,APL,APG (검토,승인,헙조)

		String ip_addr = request.getRemoteAddr();	//게시판에 remote ip address 넘겨주기위해서
		

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// 전자결재 비밀번호를 묻기를 진행할 것인가 (결재시) 
			// (* 반려와 분기만 다름)
			//-------------------------------------
			if("PRS".equals(mode) || "PRS_REJ".equals(mode)){
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
			
				//현재 결재단계 찾기
				step = prsDAO.checkStatus(pid);					//APV,APL,APG (검토,승인,헙조)

				//각 단계별 비밀번호 묻기할지?
				if(step.equals("APL")) chk = prsDAO.applyPwdAPL();				//승인단계 비밀번호 묻기여부
				else if(step.equals("APV")) chk = prsDAO.applyPwdAPV();			//검토단계 비밀번호 묻기여부
				else if(step.indexOf("APG") != -1) chk = prsDAO.applyPwdAPG();	//협조단계 비밀번호 묻기여부
				else chk = "0";													//APG2, ....10 (순차적 묻기 2번째부터는 검사안함)

				//판단하기
				if(chk.equals("1")) pass = "NO";		//비밀번호 묻기 
				else pass = "OK";						//비빌번호 묻기 없음
				
				//비밀번호 묻기후 비밀번호가 맞나 안맞나 검사하여 비밀번호 묻기여부판단하기
				String cpd = "";
				if(passwd.length() != 0) {
					cpd = prsDAO.checkPasswd(login_id,passwd);	
					if(cpd.length() == 0) pass = "NO";
					else pass = "OK";
				}	
				
				//파라미터 전달하기
				request.setAttribute("pass",pass);
				request.setAttribute("pid",pid);
				request.setAttribute("link_id",link_id);
				request.setAttribute("app_id",app_id);

				//분기하기
				if("PRS".equals(mode)) {
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_decision.jsp").forward(request,response);
				} else if("PRS_REJ".equals(mode)){
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_reject.jsp").forward(request,response);
				}
			
			}
			//-------------------------------------
			// 전자결재 결재진행
			//-------------------------------------
			else if("APP".equals(mode)) {
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//해당모듈별 전자결재 승인처리
				com.anbtech.gw.business.ModuleApprovalBO mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);	//모듈전자결재처리

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//현재 결재단계 찾기
					step = prsDAO.checkStatus(pid);					//APV,APL,APG (검토,승인,헙조)

					//각 단계 처리하기
					if(step.equals("APV")) {		//검토단계
						prsSmt.processAPV(pid,comment);
					}
					else if(step.equals("APL")) {	//승인단계
						prsSmt.setMailFullPath(path);	//멜보낼때(배차신청)
						prsSmt.setIPADDR(ip_addr);		//게시판에 보낼때(공지공문,외부공문)
						prsSmt.processAPL(pid,comment);	//결재승인
					}
					else if(step.equals("APG")) {	//합의단계
						prsSmt.processAPG(pid,comment,login_id);
					}
					else if(step.equals("APG2")) prsSmt.eleSerialAgree("APG2",pid,comment);
					else if(step.equals("APG3")) prsSmt.eleSerialAgree("APG3",pid,comment);
					else if(step.equals("APG4")) prsSmt.eleSerialAgree("APG4",pid,comment);
					else if(step.equals("APG5")) prsSmt.eleSerialAgree("APG5",pid,comment);
					else if(step.equals("APG6")) prsSmt.eleSerialAgree("APG6",pid,comment);
					else if(step.equals("APG7")) prsSmt.eleSerialAgree("APG7",pid,comment);
					else if(step.equals("APG8")) prsSmt.eleSerialAgree("APG8",pid,comment);
					else if(step.equals("APG9")) prsSmt.eleSerialAgree("APG9",pid,comment);
					else if(step.equals("APG10")) prsSmt.eleSerialAgree("APG10",pid,comment);

					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//파라미터 전달하기
					request.setAttribute("Reload","R");

					//분기하기
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_decision.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		
				}
			}
			//-------------------------------------
			// 전자결재 반려진행
			//-------------------------------------
			else if("REJ".equals(mode)){
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//해당모듈별 전자결재 승인처리
				com.anbtech.gw.business.ModuleApprovalBO mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);	//모듈전자결재처리

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.setMailFullPath(path);	//멜보낼때(배차신청 예약취소)
					prsSmt.processAPR(pid,comment);	//결재반려

					//commit 처리하기
					con.commit();
					con.setAutoCommit(true);

					//파라미터 전달하기
					request.setAttribute("Reload","R");

					//분기하기
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_reject.jsp").forward(request,response);
			
				} catch(Exception e){
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
		}

	} //doGet()

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

		//기본파라미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"APP":Hanguel.toHanguel(request.getParameter("mode"));
		String pid = Hanguel.toHanguel(request.getParameter("PID"))==null?"":Hanguel.toHanguel(request.getParameter("PID"));
		String comment = Hanguel.toHanguel(request.getParameter("comment"))==null?"":Hanguel.toHanguel(request.getParameter("comment"));
		String passwd = Hanguel.toHanguel(request.getParameter("passwd"))==null?"":Hanguel.toHanguel(request.getParameter("passwd"));
		
		//승인결재후 email을 보낼경우 본문내용 저장키 위한 저장 Directory 알려주기 (upload + crp )
		String path = Hanguel.toHanguel(request.getParameter("path"))==null?"":Hanguel.toHanguel(request.getParameter("path"));
		String link_id = Hanguel.toHanguel(request.getParameter("link_id"))==null?"":Hanguel.toHanguel(request.getParameter("link_id"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		
		//결재시 비밀번호 묻기 여부 변수
		String pass = "OK";	//비밀번호 묻기 결과 묻지 않음 (pass ok)
		String chk = "";	//비밀번호 묻기여부 판단하기
		String step = "";	//APV,APL,APG (검토,승인,헙조)

		String ip_addr = request.getRemoteAddr();	//게시판에 remote ip address 넘겨주기위해서
		
		//통보자 재지정 처리하기
		String receivers = Hanguel.toHanguel(request.getParameter("receivers"))==null?"":Hanguel.toHanguel(request.getParameter("receivers"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//-------------------------------------
			// 전자결재 비밀번호를 묻기를 진행할 것인가 (결재시) 
			// (* 반려와 분기만 다름)
			//-------------------------------------
			if("PRS".equals(mode) || "PRS_REJ".equals(mode)){
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
			
				//현재 결재단계 찾기
				step = prsDAO.checkStatus(pid);					//APV,APL,APG (검토,승인,헙조)

				//각 단계별 비밀번호 묻기할지?
				if(step.equals("APL")) chk = prsDAO.applyPwdAPL();				//승인단계 비밀번호 묻기여부
				else if(step.equals("APV")) chk = prsDAO.applyPwdAPV();			//검토단계 비밀번호 묻기여부
				else if(step.indexOf("APG") != -1) chk = prsDAO.applyPwdAPG();	//협조단계 비밀번호 묻기여부
				else chk = "0";													//APG2, ....10 (순차적 묻기 2번째부터는 검사안함)

				//판단하기
				if(chk.equals("1")) pass = "NO";		//비밀번호 묻기 
				else pass = "OK";						//비빌번호 묻기 없음
				
				//비밀번호 묻기후 비밀번호가 맞나 안맞나 검사하여 비밀번호 묻기여부판단하기
				String cpd = "";
				if(passwd.length() != 0) {
					cpd = prsDAO.checkPasswd(login_id,passwd);	
					if(cpd.length() == 0) pass = "NO";
					else pass = "OK";
				}	
				
				//파라미터 전달하기
				request.setAttribute("pass",pass);
				request.setAttribute("pid",pid);
				request.setAttribute("link_id",link_id);
				request.setAttribute("app_id",app_id);

				//분기하기
				if("PRS".equals(mode)) {
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_decision.jsp").forward(request,response);
				} else if("PRS_REJ".equals(mode)){
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_reject.jsp").forward(request,response);
				}
			
			}
			//-------------------------------------
			// 전자결재 결재진행
			//-------------------------------------
			else if("APP".equals(mode)) {
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//해당모듈별 전자결재 승인처리
				com.anbtech.gw.business.ModuleApprovalBO mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);	//모듈전자결재처리

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//현재 결재단계 찾기
					step = prsDAO.checkStatus(pid);					//APV,APL,APG (검토,승인,헙조)

					//각 단계 처리하기
					if(step.equals("APV")) {		//검토단계
						prsSmt.processAPV(pid,comment);
					}
					else if(step.equals("APL")) {	//승인단계
						prsSmt.setMailFullPath(path);	//멜보낼때(배차신청)
						prsSmt.setIPADDR(ip_addr);		//게시판에 보낼때(공지공문,외부공문)
						prsSmt.processAPL(pid,comment);	//결재승인
					}
					else if(step.equals("APG")) {	//합의단계
						prsSmt.processAPG(pid,comment,login_id);
					}
					else if(step.equals("APG2")) prsSmt.eleSerialAgree("APG2",pid,comment);
					else if(step.equals("APG3")) prsSmt.eleSerialAgree("APG3",pid,comment);
					else if(step.equals("APG4")) prsSmt.eleSerialAgree("APG4",pid,comment);
					else if(step.equals("APG5")) prsSmt.eleSerialAgree("APG5",pid,comment);
					else if(step.equals("APG6")) prsSmt.eleSerialAgree("APG6",pid,comment);
					else if(step.equals("APG7")) prsSmt.eleSerialAgree("APG7",pid,comment);
					else if(step.equals("APG8")) prsSmt.eleSerialAgree("APG8",pid,comment);
					else if(step.equals("APG9")) prsSmt.eleSerialAgree("APG9",pid,comment);
					else if(step.equals("APG10")) prsSmt.eleSerialAgree("APG10",pid,comment);

					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//파라미터 전달하기
					request.setAttribute("Reload","R");

					//분기하기
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_decision.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		
				}
			}
			//-------------------------------------
			// 전자결재 반려진행
			//-------------------------------------
			else if("REJ".equals(mode)){
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//해당모듈별 전자결재 승인처리
				com.anbtech.gw.business.ModuleApprovalBO mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);	//모듈전자결재처리

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.setMailFullPath(path);	//멜보낼때(배차신청 예약취소)
					prsSmt.processAPR(pid,comment);	//결재반려

					//commit 처리하기
					con.commit();
					con.setAutoCommit(true);

					//파라미터 전달하기
					request.setAttribute("Reload","R");

					//분기하기
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_reject.jsp").forward(request,response);
			
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//-------------------------------------
			// 전자결재 임시저장 및 반려함의 경우 삭제기능
			//-------------------------------------
			else if("APP_DELETE".equals(mode)){
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//해당모듈별 전자결재 승인처리
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.appDelete(pid);

					//commit 처리하기
					con.commit();
					con.setAutoCommit(true);

					//분기하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//-------------------------------------
			// 전자결재 완료후 통보자 추가 전송하기
			//-------------------------------------
			else if("RE_API".equals(mode)){
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//해당모듈별 전자결재 승인처리
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.addInformReceiver(pid,receivers);	//통보자 추가전송
				
					//commit 처리하기
					con.commit();
					con.setAutoCommit(true);

					//분기하기
					out.println("<script language=javascript>");
					//out.println("location.href ='ApprovalDetailServlet?mode=APP_BOX&PID="+pid+"'");
					out.println("top.opener.location.href ='ApprovalMenuServlet?mode=APP_BOX'");
					out.println("top.close();");
					out.println("</script>");
					
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//-------------------------------------
			// 전자결재 상신취소하기
			//-------------------------------------
			else if("APP_CANCEL".equals(mode)){
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//해당모듈별 전자결재 승인처리
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.appCancel(pid);

					//commit 처리하기
					con.commit();
					con.setAutoCommit(true);

					//분기하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					
				} catch(Exception e){
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
		}
	} //doPost()
}