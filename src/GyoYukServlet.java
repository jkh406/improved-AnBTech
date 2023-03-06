import com.anbtech.es.gyoyuk.db.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class GyoYukServlet extends HttpServlet {
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

		//교육일지 
		String e_year = multi.getParameter("doc_syear");	if(e_year == null) e_year = "";	//교육일자 년
		String e_month = multi.getParameter("doc_smonth");	if(e_month == null) e_month = "";//교육일자 월
		String e_date = multi.getParameter("doc_sdate");	if(e_date == null) e_date = "";	//교육일자 일
		String lecturer_id = multi.getParameter("lecturer_id");	if(lecturer_id == null) lecturer_id = "";	//강사 사번
		String lecturer_name = multi.getParameter("lecturer_name");	if(lecturer_name == null) lecturer_name = "";	//강사 이름
		String major_kind = "";
		String major_kind1 = multi.getParameter("major_kind1");	if(major_kind1 == null) major_kind1 = "";//집합
		String major_kind2 = multi.getParameter("major_kind2");	if(major_kind2 == null) major_kind2 = "";//부서
		major_kind = major_kind1+":"+major_kind2+":";
		String place = multi.getParameter("place");	if(place == null) place = "";				//장소
		String part_cnt = multi.getParameter("participators_cnt");	if(part_cnt == null) part_cnt = "";	//교육대상인원
		String edu_subject = multi.getParameter("edu_subject");	if(edu_subject == null) edu_subject = "";//교육명
		String antiprt_prs = "";	//불참자 처리
		String antiprt_prs1 = multi.getParameter("antiprt_prs1");	if(antiprt_prs1 == null) antiprt_prs1 = "";	//재교육
		String antiprt_prs2 = multi.getParameter("antiprt_prs2");	if(antiprt_prs2 == null) antiprt_prs2 = "";	//전달교육
		String antiprt_prs3 = multi.getParameter("antiprt_prs3");	if(antiprt_prs3 == null) antiprt_prs3 = "";	//기타
		antiprt_prs = antiprt_prs1+":"+antiprt_prs2+":"+antiprt_prs3+":";
		String content = multi.getParameter("content");	if(content == null) content = "";		//내용
		String upload_path = multi.getParameter("upload_path");	if(upload_path == null) upload_path = "";//path
		String people_total = multi.getParameter("people_total");	if(people_total == null) people_total = "0"; //총교육인원배열수
		int cnt = Integer.parseInt(people_total);
		//피교육생 사번과 이름을 배열로 담기
		String[][] people = new String[cnt][3];
		for(int i=0; i<cnt; i++) {
			people[i][0] = multi.getParameter("participators_id"+i);	if(people[i][0] == null) people[i][0] = "";	//피교육생 사번
			people[i][1] = multi.getParameter("participators_name"+i);	if(people[i][1] == null) people[i][1] = "";	//피교육생 이름
			people[i][2] = multi.getParameter("prt_etc"+i);	if(people[i][2] == null) people[i][2] = "";				//비고내용
		}
		
		//수정용 id (임시저장결재문서 : 삭제 후 신규등록으로 처리키 위해)
		String old_id = multi.getParameter("old_id");			if(old_id == null) old_id = "";		//임시저장한 문서 관리번호

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//교육일지 상신
			if ("GYOYUK_ILJI".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.gyoyuk.db.GyoYukDAO gyoyukDAO = new com.anbtech.es.gyoyuk.db.GyoYukDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","교육일지",doc_pid,"GYOYUK_ILJI");
					
					//교육관리 TABLE (gyoyuk_master) 입력하기 (교육일지)
					gyoyukDAO.setGyoyukiljiMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,e_year,e_month,e_date,lecturer_id,lecturer_name,major_kind,place,part_cnt,edu_subject,antiprt_prs,content,upload_path);	
					
					//교육관리 서브 TABLE (gyoyuk_part) 입력하기 (교육일지)
					for(int n=0,cid=1; n<cnt; n++,cid++) {
						String doc_cid = doc_pid + "_" + cid;
						if(people[n][0].length() != 0)
							gyoyukDAO.setGyoyukiljiSubTable(doc_pid,doc_cid,e_year,e_month,e_date,major_kind,div_name,edu_subject,people[n][0],people[n][1],people[n][2]);	
					}
	
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("gyoyuk_master","flag","gy_id",doc_pid,"EE");
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
			//교육일지 임시저장
			else if ("GYOYUK_ILJI_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.gyoyuk.db.GyoYukDAO gyoyukDAO = new com.anbtech.es.gyoyuk.db.GyoYukDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//전자결재 TABEL (app_master) 입력하기
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","교육일지",doc_pid,"GYOYUK_ILJI");
					
					//교육관리 TABLE (gyoyuk_master) 입력하기 (교육일지)
					gyoyukDAO.setGyoyukiljiMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,e_year,e_month,e_date,lecturer_id,lecturer_name,major_kind,place,part_cnt,edu_subject,antiprt_prs,content,upload_path);	
					
					//교육관리 서브 TABLE (gyoyuk_part) 입력하기 (교육일지)
					for(int n=0,cid=1; n<cnt; n++,cid++) {
						String doc_cid = doc_pid + "_" + cid;
						if(people[n][0].length() != 0)
							gyoyukDAO.setGyoyukiljiSubTable(doc_pid,doc_cid,e_year,e_month,e_date,major_kind,div_name,edu_subject,people[n][0],people[n][1],people[n][2]);	
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
			//교육일지 재상신
			if ("R_GYOYUK_ILJI".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.gyoyuk.db.GyoYukDAO gyoyukDAO = new com.anbtech.es.gyoyuk.db.GyoYukDAO(con);

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
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","교육일지",doc_pid,"GYOYUK_ILJI");
					
					//교육관리 TABLE (gyoyuk_master) 입력하기 (교육일지)
					gyoyukDAO.setGyoyukiljiMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,e_year,e_month,e_date,lecturer_id,lecturer_name,major_kind,place,part_cnt,edu_subject,antiprt_prs,content,upload_path);	
					
					//교육관리 서브 TABLE (gyoyuk_part) 입력하기 (교육일지)
					for(int n=0,cid=1; n<cnt; n++,cid++) {
						String doc_cid = doc_pid + "_" + cid;
						if(people[n][0].length() != 0)
							gyoyukDAO.setGyoyukiljiSubTable(doc_pid,doc_cid,e_year,e_month,e_date,major_kind,div_name,edu_subject,people[n][0],people[n][1],people[n][2]);	
					}
	
					//관련문서 Table에 전자결재 현상태 알려 주기
					masterDAO.setTableLink("gyoyuk_master","flag","gy_id",doc_pid,"EE");
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
			//교육일지 재 임시저장
			else if ("R_GYOYUK_ILJI_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.gyoyuk.db.GyoYukDAO gyoyukDAO = new com.anbtech.es.gyoyuk.db.GyoYukDAO(con);

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
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","교육일지",doc_pid,"GYOYUK_ILJI");
					
					//교육관리 TABLE (gyoyuk_master) 입력하기 (교육일지)
					gyoyukDAO.setGyoyukiljiMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,e_year,e_month,e_date,lecturer_id,lecturer_name,major_kind,place,part_cnt,edu_subject,antiprt_prs,content,upload_path);	
					
					//교육관리 서브 TABLE (gyoyuk_part) 입력하기 (교육일지)
					for(int n=0,cid=1; n<cnt; n++,cid++) {
						String doc_cid = doc_pid + "_" + cid;
						if(people[n][0].length() != 0)
							gyoyukDAO.setGyoyukiljiSubTable(doc_pid,doc_cid,e_year,e_month,e_date,major_kind,div_name,edu_subject,people[n][0],people[n][1],people[n][2]);	
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
			out.close();
		}
	} //doPost()
}