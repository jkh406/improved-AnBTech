import com.anbtech.es.insa.db.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class InSaServlet extends HttpServlet {
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

		//양식종류별 전자결재 관리항목 (공통)
		String writer_id = multi.getParameter("writer_id");		if(writer_id == null) writer_id = "";	//등록자(대신등록자 일수도 있음)
		String writer_name = multi.getParameter("writer_name");	if(writer_name == null) writer_name = "";	//등록자(대신등록자 일수도 있음)
		String doc_pid = multi.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";	//관리번호 (1차시는 관련관리번호와 동일)
		String doc_line = multi.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";	//결재선
		String doc_subj = multi.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";	//제목
		String doc_peri = multi.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";	//보존기간
		String doc_secu = multi.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";	//보안등급
		String writer_date = multi.getParameter("date");		if(writer_date == null) writer_date = "";	//입력일 시분

		//구인의뢰서 
		String job_kind = multi.getParameter("job_kind");	if(job_kind == null) job_kind = "";	//모집직종
		String job_content = multi.getParameter("job_content");	if(job_content == null) job_content = "";//업무내용
		String career = multi.getParameter("career");		if(career == null) career = "";		//학력
		String major = multi.getParameter("major");			if(major == null) major = "";		//전공
		String req_qualify = multi.getParameter("req_qualify");	if(req_qualify == null) req_qualify = "";//필요자격증
		String status = "";	//입사형태
		String status1 = multi.getParameter("status1");	if(status1 == null) status1 = "";//신입
		String status2 = multi.getParameter("status2");	if(status2 == null) status2 = "";//경력
		String status3 = multi.getParameter("status3");	if(status3 == null) status3 = "";//무관
		status = status1+":"+status2+":"+status3+":";
		String job_career = multi.getParameter("job_career");if(job_career == null) job_career = "";//요구경력
		String job_etc = multi.getParameter("job_etc");if(job_etc == null) job_etc = "";	//경력기타
		String req_count = multi.getParameter("req_count");if(req_count == null) req_count = "";//모집인원
		String marray = "";	//혼인
		String marray1 = multi.getParameter("marray1");	if(marray1 == null) marray1 = "";//미혼
		String marray2 = multi.getParameter("marray2");	if(marray2 == null) marray2 = "";//기혼
		String marray3 = multi.getParameter("marray3");	if(marray3 == null) marray3 = "";//무관
		marray = marray1+":"+marray2+":"+marray3+":";
		String employ = "";	//고용형태
		String employ1 = multi.getParameter("employ1");	if(employ1 == null) employ1 = "";//정규직
		String employ2 = multi.getParameter("employ2");	if(employ2 == null) employ2 = "";//계약직
		String employ3 = multi.getParameter("employ3");	if(employ3 == null) employ3 = "";//시간제
		String employ4 = multi.getParameter("employ4");	if(employ4 == null) employ4 = "";//파견근로
		String army = "";	//병역
		String army1 = multi.getParameter("army1");	if(army1 == null) army1 = "";//필
		String army2 = multi.getParameter("army2");	if(army2 == null) army2 = "";//무관
		army = army1+":"+army2+":";
		employ = employ1+":"+employ2+":"+employ3+":"+employ4+":";
		String employ_per = multi.getParameter("employ_per");if(employ_per == null) employ_per = "";//계약직기간
		String language_grade = "";	//외국어
		String language_grade1 = multi.getParameter("language_grade1");	if(language_grade1 == null) language_grade1 = "";//상
		String language_grade2 = multi.getParameter("language_grade2");	if(language_grade2 == null) language_grade2 = "";//중
		String language_grade3 = multi.getParameter("language_grade3");	if(language_grade3 == null) language_grade3 = "";//하
		language_grade = language_grade1+":"+language_grade2+":"+language_grade3+":";
		String language_exam = multi.getParameter("language_exam");	if(language_exam == null) language_exam = "";//공인시험
		String language_score = multi.getParameter("language_score");	if(language_score == null) language_score = "";//등급/점수
		String comp_grade = "";	//전산능력
		String comp_grade1 = multi.getParameter("comp_grade1");	if(comp_grade1 == null) comp_grade1 = "";//문서작성
		String comp_grade2 = multi.getParameter("comp_grade2");	if(comp_grade2 == null) comp_grade2 = "";//엑셀
		String comp_grade3 = multi.getParameter("comp_grade3");	if(comp_grade3 == null) comp_grade3 = "";//프리젠테이션
		String comp_grade4 = multi.getParameter("comp_grade4");	if(comp_grade4 == null) comp_grade4 = "";//인터넷
		String comp_grade5 = multi.getParameter("comp_grade5");	if(comp_grade5 == null) comp_grade5 = "";//홈페이지제작
		comp_grade = comp_grade1+":"+comp_grade2+":"+comp_grade3+":"+comp_grade4+":"+comp_grade5+":";
		String comp_etc = multi.getParameter("comp_etc");	if(comp_etc == null) comp_etc = "";//전산능력기타
		String papers = "";	//제출서류
		String papers1 = multi.getParameter("papers1");	if(papers1 == null) papers1 = "";//이력서
		String papers2 = multi.getParameter("papers2");	if(papers2 == null) papers2 = "";//자기소개서
		String papers3 = multi.getParameter("papers3");	if(papers3 == null) papers3 = "";//성적증명서
		String papers4 = multi.getParameter("papers4");	if(papers4 == null) papers4 = "";//졸업증명서
		String papers5 = multi.getParameter("papers5");	if(papers5 == null) papers5 = "";//경력증명서
		String papers6 = multi.getParameter("papers6");	if(papers6 == null) papers6 = "";//자격증사본
		papers = papers1+":"+papers2+":"+papers3+":"+papers4+":"+papers5+":"+papers6+":";
		String note = multi.getParameter("note");	if(note == null) note = "";//자격증사본

		//수정용 id (임시저장결재문서 : 삭제 후 신규등록으로 처리키 위해)
		String old_id = multi.getParameter("old_id");			if(old_id == null) old_id = "";		//임시저장한 문서 관리번호

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//구인의뢰서 상신
			if ("GUIN".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.insa.db.InSaDAO insaDAO = new com.anbtech.es.insa.db.InSaDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","구인의뢰서",doc_pid,"GUIN");
					
					//지원관리 TABLE (jiweon_master) 입력하기 (기안서)
					insaDAO.setGuinTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,job_kind,job_content,career,major,req_qualify,status,job_career,job_etc,req_count,marray,army,employ,employ_per,language_grade,language_exam,language_score,comp_grade,comp_etc,papers,note);
	
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("insa_master","flag","is_id",doc_pid,"EE");
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
			//구인의뢰서 임시저장
			else if ("GUIN_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.insa.db.InSaDAO insaDAO = new com.anbtech.es.insa.db.InSaDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","구인의뢰서",doc_pid,"GUIN");
					
					//지원관리 TABLE (jiweon_master) 입력하기 (기안서)
					insaDAO.setGuinTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,job_kind,job_content,career,major,req_qualify,status,job_career,job_etc,req_count,marray,army,employ,employ_per,language_grade,language_exam,language_score,comp_grade,comp_etc,papers,note);
	
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
			//구인의뢰서 재상신
			else if ("R_GUIN".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.insa.db.InSaDAO insaDAO = new com.anbtech.es.insa.db.InSaDAO(con);

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
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","구인의뢰서",doc_pid,"GUIN");
					
					//지원관리 TABLE (jiweon_master) 입력하기 (기안서)
					insaDAO.setGuinTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,job_kind,job_content,career,major,req_qualify,status,job_career,job_etc,req_count,marray,army,employ,employ_per,language_grade,language_exam,language_score,comp_grade,comp_etc,papers,note);
	
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("insa_master","flag","is_id",doc_pid,"EE");
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
			//구인의뢰서 재 임시저장
			else if ("R_GUIN_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.insa.db.InSaDAO insaDAO = new com.anbtech.es.insa.db.InSaDAO(con);

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
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","구인의뢰서",doc_pid,"GUIN");
					
					//지원관리 TABLE (jiweon_master) 입력하기 (기안서)
					insaDAO.setGuinTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,job_kind,job_content,career,major,req_qualify,status,job_career,job_etc,req_count,marray,army,employ,employ_per,language_grade,language_exam,language_score,comp_grade,comp_etc,papers,note);
	
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
