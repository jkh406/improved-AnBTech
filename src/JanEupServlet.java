import com.anbtech.es.janeup.db.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class JanEupServlet extends HttpServlet {
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

		//연장근무신청서 
		String doc_syear = multi.getParameter("doc_syear");	if(doc_syear == null) doc_syear = "";	//년
		String doc_smonth = multi.getParameter("doc_smonth");if(doc_smonth == null) doc_smonth = "";//월
		String doc_sdate = multi.getParameter("doc_sdate");	if(doc_sdate == null) doc_sdate = "";	//일
		String job_kind = multi.getParameter("job_kind");	if(job_kind == null) job_kind = "";	//분류
		String cost_prs = multi.getParameter("cost_prs");	if(cost_prs == null) cost_prs = "";	//식비지급확인
		String worker_cnt = multi.getParameter("work_cnt");	if(worker_cnt == null) worker_cnt = "0";
		int work_cnt = Integer.parseInt(worker_cnt);			//연장근무신청 컬럼수

		//근무자 현황 배열에 담기
		String[][] data = new String[work_cnt][5]; 
		String w_id = ""; String w_name = ""; String content = ""; String c_time = ""; String cfm = "";
		for(int i=0; i<work_cnt; i++) {
			w_id = multi.getParameter("work_id"+i); if(w_id == null) w_id = "";
			w_name = multi.getParameter("work_name"+i); if(w_name == null) w_name = "";
			content = multi.getParameter("content"+i); if(content == null) content = "";
			c_time = multi.getParameter("close_time"+i); if(c_time == null) c_time = "";
			cfm = multi.getParameter("cfm"+i); if(cfm == null) cfm = "";
			data[i][0] = w_id;		data[i][1] = w_name;	data[i][2] = content; 
			data[i][3] = c_time;	data[i][4] = cfm;
		}

		//수정용 id (임시저장결재문서 : 삭제 후 신규등록으로 처리키 위해)
		String old_id = multi.getParameter("old_id");			if(old_id == null) old_id = "";		//임시저장한 문서 관리번호

		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//연장근무신청서 상신
			if ("YEONJANG".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.janeup.db.JanEupDAO janeupDAO = new com.anbtech.es.janeup.db.JanEupDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","연장근무신청서",doc_pid,"YEONJANG");
					
					//잔업관리 TABLE (janeup_master) 입력하기 (연장근무신청서)																		
					janeupDAO.setYeonjangMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,doc_syear,doc_smonth,doc_sdate,job_kind,cost_prs);
					
					//개별관리 Table (janeup_worker) 입력하기
					for(int n=0,cid=1; n<work_cnt; n++,cid++) {
						//자코드 만들기
						String doc_cid = doc_pid + "_" + cid;
						if(data[n][0].length() != 0)
							janeupDAO.setYeonjangWorkerTable(doc_pid,doc_cid,doc_syear,doc_smonth,doc_sdate,data[n][0],data[n][1],data[n][2],data[n][3],data[n][4],job_kind);

					}
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("janeup_master","flag","ju_id",doc_pid,"EE");
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
			//연장근무신청서 임시저장
			else if ("YEONJANG_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.janeup.db.JanEupDAO janeupDAO = new com.anbtech.es.janeup.db.JanEupDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","연장근무신청",doc_pid,"YEONJANG");
					
					//잔업관리 TABLE (janeup_master) 입력하기 (연장근무신청서)																		
					janeupDAO.setYeonjangMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,doc_syear,doc_smonth,doc_sdate,job_kind,cost_prs);
					
					//개별관리 Table (janeup_worker) 입력하기
					for(int n=0,cid=1; n<work_cnt; n++,cid++) {
						//자코드 만들기
						String doc_cid = doc_pid + "_" + cid;
						if(data[n][0].length() != 0)
							janeupDAO.setYeonjangWorkerTable(doc_pid,doc_cid,doc_syear,doc_smonth,doc_sdate,data[n][0],data[n][1],data[n][2],data[n][3],data[n][4],job_kind);

					}
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
			//연장근무신청서 재 상신
			if ("R_YEONJANG".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.janeup.db.JanEupDAO janeupDAO = new com.anbtech.es.janeup.db.JanEupDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//관리번호,login_id,root_path

					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","연장근무신청서",doc_pid,"YEONJANG");
					
					//잔업관리 TABLE (janeup_master) 입력하기 (연장근무신청서)																		
					janeupDAO.setYeonjangMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,doc_syear,doc_smonth,doc_sdate,job_kind,cost_prs);
					
					//개별관리 Table (janeup_worker) 입력하기
					for(int n=0,cid=1; n<work_cnt; n++,cid++) {
						//자코드 만들기
						String doc_cid = doc_pid + "_" + cid;
						if(data[n][0].length() != 0)
							janeupDAO.setYeonjangWorkerTable(doc_pid,doc_cid,doc_syear,doc_smonth,doc_sdate,data[n][0],data[n][1],data[n][2],data[n][3],data[n][4],job_kind);

					}
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("janeup_master","flag","ju_id",doc_pid,"EE");
					con.commit(); // commit한다.

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
			//연장근무신청서 재 임시저장
			else if ("R_YEONJANG_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.janeup.db.JanEupDAO janeupDAO = new com.anbtech.es.janeup.db.JanEupDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// 임시저장분 삭제
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//관리번호,login_id,root_path

					//------------------------------
					// 신규 재상신
					//-------------------------------
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","연장근무신청",doc_pid,"YEONJANG");
					
					//잔업관리 TABLE (janeup_master) 입력하기 (연장근무신청서)																		
					janeupDAO.setYeonjangMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,doc_syear,doc_smonth,doc_sdate,job_kind,cost_prs);
					
					//개별관리 Table (janeup_worker) 입력하기
					for(int n=0,cid=1; n<work_cnt; n++,cid++) {
						//자코드 만들기
						String doc_cid = doc_pid + "_" + cid;
						if(data[n][0].length() != 0)
							janeupDAO.setYeonjangWorkerTable(doc_pid,doc_cid,doc_syear,doc_smonth,doc_sdate,data[n][0],data[n][1],data[n][2],data[n][3],data[n][4],job_kind);

					}
					con.commit(); // commit한다.

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
		}

	} //doPost()
	
}