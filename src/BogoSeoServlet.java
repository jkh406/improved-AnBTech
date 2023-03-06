import com.anbtech.es.bogoseo.db.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class BogoSeoServlet extends HttpServlet {
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

		//MultipartRequest 크기, 저장디렉토리
		//String filepath = getServletContext().getRealPath("")+"/upload/es/"+login_id+"/addfile";
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/es/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory생성하기

		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//양식 종류 및 사용자 정보 (공통)
		String mode = multi.getParameter("mode");				if(mode == null) mode = "";		//데이터처리모드
		String app_mode = multi.getParameter("app_mode");		if(app_mode == null) app_mode = "";		//전자결재 처리모드	
		String user_id = multi.getParameter("user_id");			if(user_id == null) user_id = "";		//user id
		String user_name = multi.getParameter("user_name");		if(user_name == null) user_name = "";	//user_name
		String user_code = multi.getParameter("user_code");		if(user_code == null) user_code = "";	//user_rank code
		String user_rank = multi.getParameter("user_rank");		if(user_rank == null) user_rank = "";	//user_rank 
		String div_id = multi.getParameter("div_id");			if(div_id == null) div_id = "";			//div_id 
		String div_name = multi.getParameter("div_name");		if(div_name == null) div_name = "";		//div_name 
		String div_code = multi.getParameter("div_code");		if(div_code == null) div_code = "";		//div_code 
		//out.println("	<script>	alert('"+mode+"');	</script>");	out.close();

		//양식종류별 전자결재 관리항목 (공통)
		String writer_id = multi.getParameter("writer_id");		if(writer_id == null) writer_id = "";	//등록자(대신등록자 일수도 있음)
		String writer_name = multi.getParameter("writer_name");	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid = multi.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";	//관리번호 (1차시는 관련관리번호와 동일)
		String doc_line = multi.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";	//결재선
		String doc_subj = multi.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";	//제목
		String doc_peri = multi.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";	//보존기간
		String doc_secu = multi.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";	//보안등급
		String writer_date = multi.getParameter("date");		if(writer_date == null) writer_date = "";	//입력일 시분

		//보고서 
		String prj_name = multi.getParameter("prj_name");	if(prj_name == null) prj_name = "";	//프로젝트 명
		String ap_name = multi.getParameter("ap_name");	if(ap_name == null) ap_name = "";		//발주처명
		String subject = multi.getParameter("subject");	if(subject == null) subject = "";		//제목
		
		//출장보고서
		String destination = multi.getParameter("destination");	if(destination == null) destination = ""; //출장지
		String purpose = multi.getParameter("purpose");	if(purpose == null) purpose = "";		//출장목적

		//양식 공통
		String content = multi.getParameter("content");	if(content == null) content = "";		//내용
		String upload_path = multi.getParameter("upload_path");	if(upload_path == null) upload_path = "";	//upload_path
		
		//저장된 파일의 root file path
		String rootpath = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//ex>C:/tomcat4/webapps/webffice

		//수정용 id (임시저장결재문서 : 삭제 후 신규등록으로 처리키 위해)
		String old_id = multi.getParameter("old_id");	if(old_id == null) old_id = "";	//임시저장한 문서 관리번호
		String fname = multi.getParameter("fname");		if(fname == null) fname = "";	//기존첨부파일명
		String sname = multi.getParameter("sname");		if(sname == null) sname = "";	//기존첨부파일 저장명
		String ftype = multi.getParameter("ftype");		if(ftype == null) ftype = "";	//기존첨부파일 type명
		String fsize = multi.getParameter("fsize");		if(fsize == null) fsize = "";	//기존첨부파일 size명
		String attache_cnt = multi.getParameter("attache_cnt");		if(attache_cnt == null) attache_cnt = "";	//첨부가능 최대수량 미만
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//보고서 상신
			if ("BOGO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","보고서",doc_pid,"BOGO");
					
					//보고서관리 TABLE (bogoseo_master) 입력하기 (보고서)
					bogoDAO.setBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,prj_name,ap_name,subject,content,upload_path);
					//첨부파일 입력하기
					int atcnt = bogoDAO.setAddFile(multi,doc_pid,filepath);	//atcnt : 첨부파일 수량

					//전자결재 TABEL (app_master)에 첨부파일 수량 update하기
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("bogoseo_master","flag","bg_id",doc_pid,"EE");
					con.commit(); // commit한다.

					//처리메시지 출력하기
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
			
			//보고서 임시저장
			else if ("BOGO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","보고서",doc_pid,"BOGO");
					
					//보고서관리 TABLE (bogoseo_master) 입력하기 (보고서)
					bogoDAO.setBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,prj_name,ap_name,subject,content,upload_path);
					//첨부파일 입력하기
					int atcnt = bogoDAO.setAddFile(multi,doc_pid,filepath);		//atcnt : 첨부파일 수량
					
					//전자결재 TABEL (app_master)에 첨부파일 수량 update하기
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					
					con.commit(); // commit한다.

					//처리메시지 출력하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
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

			//보고서 재상신
			else if ("R_BOGO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","보고서",doc_pid,"BOGO");
					
					//보고서관리 TABLE (bogoseo_master) 입력하기 (보고서)
					bogoDAO.setBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,prj_name,ap_name,subject,content,upload_path);

					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("bogoseo_master","flag","bg_id",doc_pid,"EE");
					//첨부파일 다시 입력하기 (atcnt : 첨부파일 수량)
					int atcnt = bogoDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
					//전자결재 TABEL (app_master)에 첨부파일 수량 update하기
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//관리번호,login_id,root_path

					//------------------------------
					// commit한다.
					//------------------------------
					con.commit(); 

					//처리메시지 출력하기
					//out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
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

			//보고서 재 임시저장
			else if ("R_BOGO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","보고서",doc_pid,"BOGO");

					//보고서관리 TABLE (bogoseo_master) 입력하기 (보고서)
					bogoDAO.setBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,prj_name,ap_name,subject,content,upload_path);
					//첨부파일 입력하기 (atcnt : 첨부파일 수량)
					int atcnt = bogoDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
					//전자결재 TABEL (app_master)에 첨부파일 수량 update하기
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//관리번호,login_id,root_path			
					
					//------------------------------
					// commit한다.
					//------------------------------
					con.commit(); 

					//처리메시지 출력하기
					//out.println("<script>	alert('저장되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
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

			//출장 보고서 상신
			else if ("CHULJANG_BOGO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","출장보고서",doc_pid,"CHULJANG_BOGO");
					
					//보고서관리 TABLE (bogoseo_master) 입력하기 (보고서)
					bogoDAO.setChuljangBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,destination,purpose,content,upload_path);
	
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("bogoseo_master","flag","bg_id",doc_pid,"EE");
					//첨부파일 입력하기 (atcnt : 첨부파일 수량)
					int atcnt = bogoDAO.setAddFile(multi,doc_pid,filepath);
					
					//전자결재 TABEL (app_master)에 첨부파일 수량 update하기
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					con.commit(); // commit한다.

					//처리메시지 출력하기
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

			//출장 보고서 임시저장
			else if ("CHULJANG_BOGO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","출장보고서",doc_pid,"CHULJANG_BOGO");
					
					//보고서관리 TABLE (bogoseo_master) 입력하기 (보고서)
					bogoDAO.setChuljangBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,destination,purpose,content,upload_path);
					//첨부파일 입력하기 (atcnt : 첨부파일 수량)
					int atcnt = bogoDAO.setAddFile(multi,doc_pid,filepath);
					
					//전자결재 TABEL (app_master)에 첨부파일 수량 update하기
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					con.commit(); // commit한다.

					//처리메시지 출력하기
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
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

			//출장 보고서 재상신
			else if ("R_CHULJANG_BOGO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","출장보고서",doc_pid,"CHULJANG_BOGO");
					
					//보고서관리 TABLE (bogoseo_master) 입력하기 (보고서)
					bogoDAO.setChuljangBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,destination,purpose,content,upload_path);
	
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("bogoseo_master","flag","bg_id",doc_pid,"EE");
					//첨부파일 다시 입력하기 (atcnt : 첨부파일 수량)
					int atcnt = bogoDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
					//전자결재 TABEL (app_master)에 첨부파일 수량 update하기
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//관리번호,login_id,root_path

					//------------------------------
					// commit한다.
					//------------------------------
					con.commit(); 

					//처리메시지 출력하기
					//out.println("<script>	alert('상신되었습니다.');	self.close();  </script>");	out.close();
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

			//출장 보고서 재 임시저장
			else if ("R_CHULJANG_BOGO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","출장보고서",doc_pid,"CHULJANG_BOGO");
					
					//보고서관리 TABLE (bogoseo_master) 입력하기 (보고서)
					bogoDAO.setChuljangBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,destination,purpose,content,upload_path);
					//첨부파일 다시 입력하기 (atcnt : 첨부파일 수량)
					int atcnt = bogoDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
					//전자결재 TABEL (app_master)에 첨부파일 수량 update하기
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//관리번호,login_id,root_path

					//------------------------------
					// commit한다.
					//------------------------------
					con.commit(); 

					//처리메시지 출력하기
					//out.println("<script>	alert('저장되었습니다.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
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
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} //doPost()
}
