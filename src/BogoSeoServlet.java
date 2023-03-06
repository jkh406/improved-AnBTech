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
		//out.println("	<script>	alert('"+mode+"');	</script>");	out.close();

		//��������� ���ڰ��� �����׸� (����)
		String writer_id = multi.getParameter("writer_id");		if(writer_id == null) writer_id = "";	//�����(��ŵ���� �ϼ��� ����)
		String writer_name = multi.getParameter("writer_name");	if(writer_name == null) writer_name = "";	//�����(��ŵ���� �ϼ��� ����)
		String doc_pid = multi.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";	//������ȣ (1���ô� ���ð�����ȣ�� ����)
		String doc_line = multi.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";	//���缱
		String doc_subj = multi.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";	//����
		String doc_peri = multi.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";	//�����Ⱓ
		String doc_secu = multi.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";	//���ȵ��
		String writer_date = multi.getParameter("date");		if(writer_date == null) writer_date = "";	//�Է��� �ú�

		//���� 
		String prj_name = multi.getParameter("prj_name");	if(prj_name == null) prj_name = "";	//������Ʈ ��
		String ap_name = multi.getParameter("ap_name");	if(ap_name == null) ap_name = "";		//����ó��
		String subject = multi.getParameter("subject");	if(subject == null) subject = "";		//����
		
		//���庸��
		String destination = multi.getParameter("destination");	if(destination == null) destination = ""; //������
		String purpose = multi.getParameter("purpose");	if(purpose == null) purpose = "";		//�������

		//��� ����
		String content = multi.getParameter("content");	if(content == null) content = "";		//����
		String upload_path = multi.getParameter("upload_path");	if(upload_path == null) upload_path = "";	//upload_path
		
		//����� ������ root file path
		String rootpath = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//ex>C:/tomcat4/webapps/webffice

		//������ id (�ӽ�������繮�� : ���� �� �űԵ������ ó��Ű ����)
		String old_id = multi.getParameter("old_id");	if(old_id == null) old_id = "";	//�ӽ������� ���� ������ȣ
		String fname = multi.getParameter("fname");		if(fname == null) fname = "";	//����÷�����ϸ�
		String sname = multi.getParameter("sname");		if(sname == null) sname = "";	//����÷������ �����
		String ftype = multi.getParameter("ftype");		if(ftype == null) ftype = "";	//����÷������ type��
		String fsize = multi.getParameter("fsize");		if(fsize == null) fsize = "";	//����÷������ size��
		String attache_cnt = multi.getParameter("attache_cnt");		if(attache_cnt == null) attache_cnt = "";	//÷�ΰ��� �ִ���� �̸�
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//���� ���
			if ("BOGO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","����",doc_pid,"BOGO");
					
					//�������� TABLE (bogoseo_master) �Է��ϱ� (����)
					bogoDAO.setBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,prj_name,ap_name,subject,content,upload_path);
					//÷������ �Է��ϱ�
					int atcnt = bogoDAO.setAddFile(multi,doc_pid,filepath);	//atcnt : ÷������ ����

					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("bogoseo_master","flag","bg_id",doc_pid,"EE");
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
			
			//���� �ӽ�����
			else if ("BOGO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","����",doc_pid,"BOGO");
					
					//�������� TABLE (bogoseo_master) �Է��ϱ� (����)
					bogoDAO.setBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,prj_name,ap_name,subject,content,upload_path);
					//÷������ �Է��ϱ�
					int atcnt = bogoDAO.setAddFile(multi,doc_pid,filepath);		//atcnt : ÷������ ����
					
					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					
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

			//���� ����
			else if ("R_BOGO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","����",doc_pid,"BOGO");
					
					//�������� TABLE (bogoseo_master) �Է��ϱ� (����)
					bogoDAO.setBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,prj_name,ap_name,subject,content,upload_path);

					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("bogoseo_master","flag","bg_id",doc_pid,"EE");
					//÷������ �ٽ� �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = bogoDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//������ȣ,login_id,root_path

					//------------------------------
					// commit�Ѵ�.
					//------------------------------
					con.commit(); 

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

			//���� �� �ӽ�����
			else if ("R_BOGO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","����",doc_pid,"BOGO");

					//�������� TABLE (bogoseo_master) �Է��ϱ� (����)
					bogoDAO.setBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,prj_name,ap_name,subject,content,upload_path);
					//÷������ �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = bogoDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//������ȣ,login_id,root_path			
					
					//------------------------------
					// commit�Ѵ�.
					//------------------------------
					con.commit(); 

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

			//���� ���� ���
			else if ("CHULJANG_BOGO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","���庸��",doc_pid,"CHULJANG_BOGO");
					
					//�������� TABLE (bogoseo_master) �Է��ϱ� (����)
					bogoDAO.setChuljangBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,destination,purpose,content,upload_path);
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("bogoseo_master","flag","bg_id",doc_pid,"EE");
					//÷������ �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = bogoDAO.setAddFile(multi,doc_pid,filepath);
					
					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

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

			//���� ���� �ӽ�����
			else if ("CHULJANG_BOGO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","���庸��",doc_pid,"CHULJANG_BOGO");
					
					//�������� TABLE (bogoseo_master) �Է��ϱ� (����)
					bogoDAO.setChuljangBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,destination,purpose,content,upload_path);
					//÷������ �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = bogoDAO.setAddFile(multi,doc_pid,filepath);
					
					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
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

			//���� ���� ����
			else if ("R_CHULJANG_BOGO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","���庸��",doc_pid,"CHULJANG_BOGO");
					
					//�������� TABLE (bogoseo_master) �Է��ϱ� (����)
					bogoDAO.setChuljangBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,destination,purpose,content,upload_path);
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("bogoseo_master","flag","bg_id",doc_pid,"EE");
					//÷������ �ٽ� �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = bogoDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//������ȣ,login_id,root_path

					//------------------------------
					// commit�Ѵ�.
					//------------------------------
					con.commit(); 

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

			//���� ���� �� �ӽ�����
			else if ("R_CHULJANG_BOGO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.bogoseo.db.BogoSeoDAO bogoDAO = new com.anbtech.es.bogoseo.db.BogoSeoDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","���庸��",doc_pid,"CHULJANG_BOGO");
					
					//�������� TABLE (bogoseo_master) �Է��ϱ� (����)
					bogoDAO.setChuljangBogoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,destination,purpose,content,upload_path);
					//÷������ �ٽ� �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = bogoDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//������ȣ,login_id,root_path

					//------------------------------
					// commit�Ѵ�.
					//------------------------------
					con.commit(); 

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
			out.close();
		}
	} //doPost()
}
