import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class InDocumentMultiServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 20;

	/********
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * post방식으로 넘어왔을 때 처리 (입력,수정,삭제,상신)
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

		//MultipartRequest 크기, 저장디렉토리
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/ods/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory생성하기

		String maxFileSize = "50";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //해당 경로로 업로드한다
		
		//전체 공통 (사내공문)
		String mode = multi.getParameter("mode");				if(mode == null) mode = "";		//데이터처리모드
		
		//사내공문 (관리항목)	
		String user_id = multi.getParameter("user_id");			if(user_id == null) user_id = "";		//user id
		String user_name = multi.getParameter("user_name");		if(user_name == null) user_name = "";	//user_name
		String user_rank = multi.getParameter("user_rank");		if(user_rank == null) user_rank = "";	//user_rank 
		String div_id = multi.getParameter("div_id");			if(div_id == null) div_id = "";			//div_id 
		String div_name = multi.getParameter("div_name");		if(div_name == null) div_name = "";		//div_name 
		String code = multi.getParameter("code");				if(code == null) code = "";		//code 
		String div_code = multi.getParameter("div_code");		if(div_code == null) div_code = "";		//div_code
		
		//사내공문 (양식항목)
		String id = multi.getParameter("id");	if(id == null) id = "";								//id
		String serial_no = multi.getParameter("serial_no");	if(serial_no == null) serial_no = "";	//일련번호
		String class_no = multi.getParameter("class_no");	if(class_no == null) class_no = "";		//분류기호
		String doc_id = multi.getParameter("doc_id");	if(doc_id == null) doc_id = "";				//문서번호
		String slogan = multi.getParameter("slogan");	if(slogan == null) slogan = "";				//표어
		String title_name = multi.getParameter("title_name");if(title_name == null) title_name = "";//머리글
		String in_date = multi.getParameter("in_date");	if(in_date == null) in_date = "";			//작성일자
		String app_date = multi.getParameter("app_date");	if(app_date == null) app_date = "";		//결재일자
		String enforce_date = multi.getParameter("enforce_date");	if(enforce_date == null) enforce_date = "";			//시행일자
		String receive = multi.getParameter("receive");	if(receive == null) receive = "";			//수신
		String reference = multi.getParameter("reference");	if(reference == null) reference = "";	//참조
		String sending = multi.getParameter("sending");	if(sending == null) sending = "";			//발신
		String subject = multi.getParameter("subject");	if(subject == null) subject = "";			//제목
		String content = multi.getParameter("content");	if(content == null) content = "";			//내용
		String firm_name = multi.getParameter("firm_name");	if(firm_name == null) firm_name = "";	//꼬리글
		String representative = multi.getParameter("representative");	if(representative == null) representative = "";	//대표자명
		
		String module_name = multi.getParameter("module_name");	if(module_name == null) module_name = "";	//부서명으로 보낼때
		String module_add = multi.getParameter("module_add");	if(module_add == null) module_add = "";	//부서명 주소
		if(module_add.length() == 0) module_add = receive;		//수신지정내용을 입력한다.
		
		String mail = multi.getParameter("mail");	if(mail == null) mail = "";					//mail로 보낼때
		String mail_add = multi.getParameter("mail_add");	if(mail_add == null) mail_add = "";	//사번/이름;

		//저장된 파일의 root file path
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//ex>C:/tomcat4/webapps/webffice

		//수정용 id (임시저장결재문서 : 삭제 후 신규등록으로 처리키 위해)
		String fname = multi.getParameter("fname");		if(fname == null) fname = "";	//기존첨부파일명
		String sname = multi.getParameter("sname");		if(sname == null) sname = "";	//기존첨부파일 저장명
		String ftype = multi.getParameter("ftype");		if(ftype == null) ftype = "";	//기존첨부파일 type명
		String fsize = multi.getParameter("fsize");		if(fsize == null) fsize = "";	//기존첨부파일 size명
		String bon_path = multi.getParameter("bon_path");	if(bon_path == null) bon_path = "";	//확장Path
		String ext = multi.getParameter("ext");	if(ext == null) ext = "";				//첨부파일삭제 번호
		String attache_cnt = multi.getParameter("attache_cnt");		if(attache_cnt == null) attache_cnt = "0";	//첨부가능 최대수량 미만
		int atcnt = Integer.parseInt(attache_cnt);

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/****************************
			 * 공문서 등록/수정/삭제/상신 처리하기
			 ****************************/
			//사내공문 작성하기
			if ("IND_write".equals(mode)){
				com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String rtn = "";
					//사내공문 입력하기
					rtn = docDAO.inputINDTable(id,user_id,user_name,user_rank,div_id,div_code,code,div_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,subject,firm_name,representative,module_name,module_add,mail,mail_add,content,upload_path);
					//첨부파일 입력하기
					atcnt = docDAO.setAddFile(multi,id,filepath);	//atcnt : 첨부파일 수량
					if(!rtn.equals("ok")) {		//class_table chief_id즉 부서장 사번이 없는경우
						con.rollback();
						con.setAutoCommit(true);
						out.println("<script> alert('지정된 사업부의 부서장["+rtn+"] 이 미등록되었습니다. 관리자에게 문의하십시요.'); history.back(-1); </script>");
						out.close();
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//------------------------------
					//전체 List 로 분기하기
					//------------------------------
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_List(login_id,"subject","","1",20);
					request.setAttribute("Data_List", table_list);
					//현재페이지/전체페이지
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();			//전체페이지
					//분기하기
					getServletContext().getRequestDispatcher("/ods/InDocument_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//사내공문 작성즉시 상신하기
			else if ("IND_app".equals(mode)){
				com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String rtn = "";
					//사내공문 입력하기
					rtn = docDAO.inputINDTable(id,user_id,user_name,user_rank,div_id,div_code,code,div_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,subject,firm_name,representative,module_name,module_add,mail,mail_add,content,upload_path);
					//첨부파일 입력하기
					atcnt = docDAO.setAddFile(multi,id,filepath);	//atcnt : 첨부파일 수량
					if(!rtn.equals("ok")) {		//class_table chief_id즉 부서장 사번이 없는경우
						con.rollback();
						con.setAutoCommit(true);
						out.println("<script> alert('지정된 사업부의 부서장["+rtn+"] 이 미등록되었습니다. 관리자에게 문의하십시요.'); history.back(-1); </script>");
						out.close();
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//------------------------------
					//상신 분기하기
					//------------------------------
					getServletContext().getRequestDispatcher("/gw/approval/module/InDocument_AppDirect.jsp?id="+id).forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//사내사항 수정하기
			else if ("IND_modify".equals(mode)){
				com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String rtn = "";
					//사내공문 수정하기
					rtn = docDAO.updateINDTable(id,user_id,user_name,user_rank,div_id,div_code,code,div_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,subject,firm_name,representative,module_name,module_add,mail,mail_add,content,upload_path);
					//첨부파일 수정입력하기
					atcnt = docDAO.setUpdateFile(multi,id,filepath,fname,sname,ftype,fsize,attache_cnt);	//atcnt : 첨부파일 수량
					if(!rtn.equals("ok")) {		//class_table chief_id즉 부서장 사번이 없는경우
						con.rollback();
						con.setAutoCommit(true);
						out.println("<script> alert('지정된 사업부의 부서장["+rtn+"] 이 미등록되었습니다. 관리자에게 문의하십시요.'); history.back(-1); </script>");
						out.close();
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//------------------------------
					//전체 List 로 분기하기
					//------------------------------
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_List(login_id,"subject","","1",20);
					request.setAttribute("Data_List", table_list);
					//현재페이지/전체페이지
					int Cpage = docDAO.getCurrentPage();		//현재페이지
					int Tpage = docDAO.getTotalPage();			//전체페이지
					//분기하기
					getServletContext().getRequestDispatcher("/ods/InDocument_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//사내사항중 첨부파일 개별삭제하기
			else if ("IND_attachD".equals(mode)){
				com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//사내공문 수정하기
					docDAO.updateINDTable(id,user_id,user_name,user_rank,div_id,div_code,code,div_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,subject,firm_name,representative,module_name,module_add,mail,mail_add,content,upload_path);
					//첨부파일 삭제 수정하기
					docDAO.deleteAttachFile(id,fname,ftype,fsize,sname,ext,upload_path,bon_path);
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//------------------------------
					//전체 List 로 분기하기
					//------------------------------
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_Read(id);
					request.setAttribute("Data_One", table_list);
					//분기하기
					getServletContext().getRequestDispatcher("/ods/InDocument_modify.jsp").forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}

		}catch (Exception e){
			//에러출력 페이지로 분기
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

		}finally{
			//con소멸
			close(con);
			out.close();
		}
	} //doPost()
}
