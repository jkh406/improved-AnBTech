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
	 * �Ҹ���
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * post������� �Ѿ���� �� ó��
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//MultipartRequest ũ��, ������丮
		//String filepath = getServletContext().getRealPath("")+"/upload/es/"+login_id+"/addfile";
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/es/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory�����ϱ�

		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//��� ���� �� ����� ���� (����)
		String mode = multi.getParameter("mode");				if(mode == null) mode = "";		//������ó�����
		String app_mode = multi.getParameter("app_mode");		if(app_mode == null) app_mode = "";		//���ڰ��� ó�����	
		String user_id = multi.getParameter("user_id");			if(user_id == null) user_id = "";		//user id
		String user_name = multi.getParameter("user_name");		if(user_name == null) user_name = "";	//user_name
		String user_code = multi.getParameter("user_code");		if(user_code == null) user_code = "";	//user_rank code
		String user_rank = multi.getParameter("user_rank");		if(user_rank == null) user_rank = "";	//user_rank 
		String div_id = multi.getParameter("div_id");			if(div_id == null) div_id = "";			//div_id 
		String div_name = multi.getParameter("div_name");		if(div_name == null) div_name = "";		//div_name 
		String div_code = multi.getParameter("div_code");		if(div_code == null) div_code = "";		//div_code 

		//��������� ���ڰ��� �����׸� (����)
		String writer_id = multi.getParameter("writer_id");		if(writer_id == null) writer_id = "";	//�����(��ŵ���� �ϼ��� ����)
		String writer_name = multi.getParameter("writer_name");	if(writer_name == null) writer_name = "";	//�����(��ŵ���� �ϼ��� ����)
		String doc_pid = multi.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";	//������ȣ (1���ô� ���ð�����ȣ�� ����)
		String doc_line = multi.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";	//���缱
		String doc_subj = multi.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";	//����
		String doc_peri = multi.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";	//�����Ⱓ
		String doc_secu = multi.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";	//���ȵ��
		String writer_date = multi.getParameter("date");		if(writer_date == null) writer_date = "";	//�Է��� �ú�

		//�����Ƿڼ� 
		String job_kind = multi.getParameter("job_kind");	if(job_kind == null) job_kind = "";	//��������
		String job_content = multi.getParameter("job_content");	if(job_content == null) job_content = "";//��������
		String career = multi.getParameter("career");		if(career == null) career = "";		//�з�
		String major = multi.getParameter("major");			if(major == null) major = "";		//����
		String req_qualify = multi.getParameter("req_qualify");	if(req_qualify == null) req_qualify = "";//�ʿ��ڰ���
		String status = "";	//�Ի�����
		String status1 = multi.getParameter("status1");	if(status1 == null) status1 = "";//����
		String status2 = multi.getParameter("status2");	if(status2 == null) status2 = "";//���
		String status3 = multi.getParameter("status3");	if(status3 == null) status3 = "";//����
		status = status1+":"+status2+":"+status3+":";
		String job_career = multi.getParameter("job_career");if(job_career == null) job_career = "";//�䱸���
		String job_etc = multi.getParameter("job_etc");if(job_etc == null) job_etc = "";	//��±�Ÿ
		String req_count = multi.getParameter("req_count");if(req_count == null) req_count = "";//�����ο�
		String marray = "";	//ȥ��
		String marray1 = multi.getParameter("marray1");	if(marray1 == null) marray1 = "";//��ȥ
		String marray2 = multi.getParameter("marray2");	if(marray2 == null) marray2 = "";//��ȥ
		String marray3 = multi.getParameter("marray3");	if(marray3 == null) marray3 = "";//����
		marray = marray1+":"+marray2+":"+marray3+":";
		String employ = "";	//�������
		String employ1 = multi.getParameter("employ1");	if(employ1 == null) employ1 = "";//������
		String employ2 = multi.getParameter("employ2");	if(employ2 == null) employ2 = "";//�����
		String employ3 = multi.getParameter("employ3");	if(employ3 == null) employ3 = "";//�ð���
		String employ4 = multi.getParameter("employ4");	if(employ4 == null) employ4 = "";//�İ߱ٷ�
		String army = "";	//����
		String army1 = multi.getParameter("army1");	if(army1 == null) army1 = "";//��
		String army2 = multi.getParameter("army2");	if(army2 == null) army2 = "";//����
		army = army1+":"+army2+":";
		employ = employ1+":"+employ2+":"+employ3+":"+employ4+":";
		String employ_per = multi.getParameter("employ_per");if(employ_per == null) employ_per = "";//������Ⱓ
		String language_grade = "";	//�ܱ���
		String language_grade1 = multi.getParameter("language_grade1");	if(language_grade1 == null) language_grade1 = "";//��
		String language_grade2 = multi.getParameter("language_grade2");	if(language_grade2 == null) language_grade2 = "";//��
		String language_grade3 = multi.getParameter("language_grade3");	if(language_grade3 == null) language_grade3 = "";//��
		language_grade = language_grade1+":"+language_grade2+":"+language_grade3+":";
		String language_exam = multi.getParameter("language_exam");	if(language_exam == null) language_exam = "";//���ν���
		String language_score = multi.getParameter("language_score");	if(language_score == null) language_score = "";//���/����
		String comp_grade = "";	//����ɷ�
		String comp_grade1 = multi.getParameter("comp_grade1");	if(comp_grade1 == null) comp_grade1 = "";//�����ۼ�
		String comp_grade2 = multi.getParameter("comp_grade2");	if(comp_grade2 == null) comp_grade2 = "";//����
		String comp_grade3 = multi.getParameter("comp_grade3");	if(comp_grade3 == null) comp_grade3 = "";//���������̼�
		String comp_grade4 = multi.getParameter("comp_grade4");	if(comp_grade4 == null) comp_grade4 = "";//���ͳ�
		String comp_grade5 = multi.getParameter("comp_grade5");	if(comp_grade5 == null) comp_grade5 = "";//Ȩ����������
		comp_grade = comp_grade1+":"+comp_grade2+":"+comp_grade3+":"+comp_grade4+":"+comp_grade5+":";
		String comp_etc = multi.getParameter("comp_etc");	if(comp_etc == null) comp_etc = "";//����ɷ±�Ÿ
		String papers = "";	//���⼭��
		String papers1 = multi.getParameter("papers1");	if(papers1 == null) papers1 = "";//�̷¼�
		String papers2 = multi.getParameter("papers2");	if(papers2 == null) papers2 = "";//�ڱ�Ұ���
		String papers3 = multi.getParameter("papers3");	if(papers3 == null) papers3 = "";//��������
		String papers4 = multi.getParameter("papers4");	if(papers4 == null) papers4 = "";//��������
		String papers5 = multi.getParameter("papers5");	if(papers5 == null) papers5 = "";//�������
		String papers6 = multi.getParameter("papers6");	if(papers6 == null) papers6 = "";//�ڰ����纻
		papers = papers1+":"+papers2+":"+papers3+":"+papers4+":"+papers5+":"+papers6+":";
		String note = multi.getParameter("note");	if(note == null) note = "";//�ڰ����纻

		//������ id (�ӽ�������繮�� : ���� �� �űԵ������ ó��Ű ����)
		String old_id = multi.getParameter("old_id");			if(old_id == null) old_id = "";		//�ӽ������� ���� ������ȣ

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//�����Ƿڼ� ���
			if ("GUIN".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.insa.db.InSaDAO insaDAO = new com.anbtech.es.insa.db.InSaDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����Ƿڼ�",doc_pid,"GUIN");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (��ȼ�)
					insaDAO.setGuinTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,job_kind,job_content,career,major,req_qualify,status,job_career,job_etc,req_count,marray,army,employ,employ_per,language_grade,language_exam,language_score,comp_grade,comp_etc,papers,note);
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("insa_master","flag","is_id",doc_pid,"EE");
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
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
			//�����Ƿڼ� �ӽ�����
			else if ("GUIN_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.insa.db.InSaDAO insaDAO = new com.anbtech.es.insa.db.InSaDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����Ƿڼ�",doc_pid,"GUIN");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (��ȼ�)
					insaDAO.setGuinTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,job_kind,job_content,career,major,req_qualify,status,job_career,job_etc,req_count,marray,army,employ,employ_per,language_grade,language_exam,language_score,comp_grade,comp_etc,papers,note);
	
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
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
			//�����Ƿڼ� ����
			else if ("R_GUIN".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.insa.db.InSaDAO insaDAO = new com.anbtech.es.insa.db.InSaDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����Ƿڼ�",doc_pid,"GUIN");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (��ȼ�)
					insaDAO.setGuinTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,job_kind,job_content,career,major,req_qualify,status,job_career,job_etc,req_count,marray,army,employ,employ_per,language_grade,language_exam,language_score,comp_grade,comp_etc,papers,note);
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("insa_master","flag","is_id",doc_pid,"EE");
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('��ŵǾ����ϴ�.');	self.close();  </script>");	out.close();
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
			//�����Ƿڼ� �� �ӽ�����
			else if ("R_GUIN_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.insa.db.InSaDAO insaDAO = new com.anbtech.es.insa.db.InSaDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,"");	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�����Ƿڼ�",doc_pid,"GUIN");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (��ȼ�)
					insaDAO.setGuinTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,job_kind,job_content,career,major,req_qualify,status,job_career,job_etc,req_count,marray,army,employ,employ_per,language_grade,language_exam,language_score,comp_grade,comp_etc,papers,note);
	
					con.commit(); // commit�Ѵ�.

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('����Ǿ����ϴ�.');	self.close();  </script>");	out.close();
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
