import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.gw.entity.*;
import com.anbtech.gw.db.*;
import com.anbtech.gw.business.*;
import com.anbtech.file.FileWriteString;
import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;

public class ApprovalInputServlet extends HttpServlet {

	private com.anbtech.dbconn.DBConnectionManager connMgr;
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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
			
	} // doGet()


	/**********************************
	 * post방식으로 넘어왔을 때 처리
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//MultipartRequest 크기, 저장디렉토리
		//String filepath = getServletContext().getRealPath("")+"/upload/eleApproval/"+login_id+"/addfile";
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/eleApproval/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory생성하기

		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//전페이지에서 값 받아온다. multi에서 가져옴
		String mode = multi.getParameter("mode");				if(mode == null) mode = "";					//데이터처리모드
		//out.println("	<script>	alert('"+mode+":"+request.getServletPath()+"');	</script>");	out.close();

		String writer_id = multi.getParameter("login_id");		if(writer_id == null) writer_id = "";		//loign id
		String writer_name = multi.getParameter("login_name");	if(writer_name == null) writer_name = "";	//login name	
		String writer_date = multi.getParameter("date");		if(writer_date == null) writer_date = "";	//date	
		String doc_pid = multi.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";			//관리번호
		String doc_subj = multi.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";			//결재선
		String doc_line = multi.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";			//결재선
		String doc_peri = multi.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";			//보존기간
		String doc_secu = multi.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";			//보안등급
		String doc_cont = multi.getParameter("CONTENT");		if(doc_cont == null) doc_cont = "";			//본문내용	
		String doc_plid = multi.getParameter("doc_lid");		if(doc_plid == null) doc_plid = "";			//관련문서 관리번호	
		String doc_flag = multi.getParameter("doc_flag");		if(doc_flag == null) doc_flag = "";			//관련문서 종류	
		String upload_path = multi.getParameter("upload_path");	if(upload_path == null) upload_path = "";	//관련문서 종류	
		//out.println("	<script>	alert('"+upload_path+"');	</script>");	out.close();

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			// 전자결재 상신 (순차적 협조)
			if ("REQ".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//table insert
					masterDAO.setTable(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
					
					//관련문서 Table에 현상태 알려 주기
					if(doc_flag.equals("SERVICE"))
						masterDAO.setTableLink("history_table","flag","ah_id",doc_plid,"EE");

					//파일명 알아내기 및 파일명 바꿔주기
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);

					//파일명 DB에 update 하기 
					masterDAO.updFile(file.getText(),doc_pid);

					//첨부파일 수량 update하기 (일반문서와 고객관리만 처리하고 나머지는 각각의 입력서브릿에서 처리)
					int atcnt = 0;
					if(doc_flag.equals("SERVICE")) {		//고객관리
						com.anbtech.gw.db.AppModuleAttchDAO attchDAO = new com.anbtech.gw.db.AppModuleAttchDAO(con);
						atcnt = attchDAO.searchAttchCntSERVICE(doc_plid);
						masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					} else {								//일반문서보고
						atcnt = masterDAO.getAttacheCount();
						masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					}

					con.commit();
					con.setAutoCommit(true);

					//처리메시지 출력하기
					//if(doc_flag.equals("SERVICE")) {
					//	out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
					//} else {
						out.println("	<script>");
						out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
						out.println("	</script>");
						out.close();
					//}
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//전자결재 상신 (일괄적 협조)
			else if("ABAT".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//table insert
					masterDAO.setTable(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
		
					//관련문서 Table에 현상태 알려 주기
					if(doc_flag.equals("SERVICE"))
						masterDAO.setTableLink("history_table","flag","ah_id",doc_plid,"EE");

					//파일명 알아내기
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);

					//파일명 DB에 update 하기 
					masterDAO.updFile(file.getText(),doc_pid);

					//첨부파일 수량 update하기 (일반문서와 고객관리만 처리하고 나머지는 각각의 입력서브릿에서 처리)
					int atcnt = 0;
					if(doc_flag.equals("SERVICE")) {		//고객관리
						com.anbtech.gw.db.AppModuleAttchDAO attchDAO = new com.anbtech.gw.db.AppModuleAttchDAO(con);
						atcnt = attchDAO.searchAttchCntSERVICE(doc_plid);
						masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					} else {								//일반문서보고
						atcnt = masterDAO.getAttacheCount();
						masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					}

					con.commit();
					con.setAutoCommit(true);

					//처리메시지 출력하기
					//if(doc_flag.equals("SERVICE")) {
					//	out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
					//} else {
						out.println("	<script>");
						out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
						out.println("	</script>");
						out.close();
					//}
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
				
			}
			//임시보관 
			else if("TMP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				
				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//table insert
					masterDAO.setTable(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);

					//파일처리
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);

					//파일 update
					masterDAO.updFile(file.getText(),doc_pid);

					//첨부파일 수량 update하기
					int atcnt = masterDAO.getAttacheCount();
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					con.commit();
					con.setAutoCommit(true);

					//처리메시지 출력하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			// 전자결재 상신 (순차적 협조) 재작성 하기
			else if("REQ_UP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//내용수정 update
					masterDAO.setTableUpdate(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
		
					//파일명 알아내기
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);
					//out.println("	<script>	alert('"+file.getText()+"');	</script>");	out.close();

					//파일명 DB에 update 하기 
					masterDAO.updFile(file.getText(),doc_pid);

					//첨부파일 수량 update하기
					int atcnt = masterDAO.getAttacheCount(doc_pid);
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					con.commit();
					con.setAutoCommit(true);

					//처리메시지 출력하기
					//out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//전자결재 상신 (일괄적 협조) 재작성하기
			else if("ABAT_UP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//내용수정 update
					masterDAO.setTableUpdate(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
		
					//파일명 알아내기
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);
					//out.println("	<script>	alert('"+file.getText()+"');	</script>");	out.close();

					//파일명 DB에 update 하기 
					masterDAO.updFile(file.getText(),doc_pid);

					//첨부파일 수량 update하기
					int atcnt = masterDAO.getAttacheCount(doc_pid);
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					con.commit();
					con.setAutoCommit(true);

					//처리메시지 출력하기
					//out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//임시보관 재작성하기
			else if("TMP_UP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//내용수정 update
					masterDAO.setTableUpdate(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
		
					//파일명 알아내기
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);
					//out.println("	<script>	alert('"+file.getText()+"');	</script>");	out.close();

					//파일명 DB에 update 하기 
					masterDAO.updFile(file.getText(),doc_pid);

					//첨부파일 수량 update하기
					int atcnt = masterDAO.getAttacheCount(doc_pid);
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					con.commit();
					con.setAutoCommit(true);

					//처리메시지 출력하기
					//out.println("<script>	alert('저장되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
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
			//con소멸
			close(con);
			out.close();
		}
	} //doPost()
}