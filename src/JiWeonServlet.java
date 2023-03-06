import com.anbtech.es.jiweon.db.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class JiWeonServlet extends HttpServlet {
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

		//��� ���� (��ȼ�,������,������)
		String content = multi.getParameter("content");	if(content == null) content = "";		//����
		String note = multi.getParameter("note");	if(note == null) note = "";		//�ǰ�
		String upload_path = multi.getParameter("upload_path");	if(upload_path == null) upload_path = "";	//upload_path
		
		//��ȼ� (������)
		String period = multi.getParameter("period");	if(period == null) period = "";	//����
		String kind = multi.getParameter("kind");	if(kind == null) kind = "";			//����
		String doc_syear = multi.getParameter("doc_syear");	if(doc_syear == null) doc_syear = "";//��
		String doc_smonth = multi.getParameter("doc_smonth");if(doc_smonth == null) doc_smonth = "";//��
		String doc_sdate = multi.getParameter("doc_sdate");	if(doc_sdate == null) doc_sdate = "";//��
		String subject = multi.getParameter("subject");	if(subject == null) subject = "";		//����
		
		//���Խ�û��
		String applicant_id = multi.getParameter("applicant_id");	if(applicant_id == null) applicant_id = ""; //��û�� ���
		String applicant_name = multi.getParameter("applicant_name");if(applicant_name == null) applicant_name = ""; //��û�� �̸�
		String purpose = multi.getParameter("purpose");	if(purpose == null) purpose = "";		//����
		String content_cnt = multi.getParameter("con_cnt");	if(content_cnt == null) content_cnt = "0";//���泻�� ���μ�
		int con_cnt = Integer.parseInt(content_cnt);

		String chg_content = "";	//���Ժ��泻�� 
		String item = ""; String before = ""; String after = "";
		for(int i=0; i<con_cnt; i++){
			item = multi.getParameter("item"+i); if(item == null) item = "";
			chg_content += item+" |";
			before = multi.getParameter("before"+i); if(before == null) before = "";
			chg_content += before+" |";
			after = multi.getParameter("after"+i); if(after == null) after = "";
			chg_content += after+" ;"; 
		}
		
		//������
		String dest_ac_name = multi.getParameter("dest_ac_name");	if(dest_ac_name == null) dest_ac_name = "";	//���źμ�

		//������ id (�ӽ�������繮�� : ���� �� �űԵ������ ó��Ű ����)
		String old_id = multi.getParameter("old_id");			if(old_id == null) old_id = "";		//�ӽ������� ���� ������ȣ
		String fname = multi.getParameter("fname");		if(fname == null) fname = "";	//����÷�����ϸ�
		String sname = multi.getParameter("sname");		if(sname == null) sname = "";	//����÷������ �����
		String ftype = multi.getParameter("ftype");		if(ftype == null) ftype = "";	//����÷������ type��
		String fsize = multi.getParameter("fsize");		if(fsize == null) fsize = "";	//����÷������ size��
		String attache_cnt = multi.getParameter("attache_cnt");		if(attache_cnt == null) attache_cnt = "";	//÷�ΰ��� �ִ���� �̸�
		//����� ������ root file path
		String rootpath = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//ex>C:/tomcat4/webapps/webffice

		//���ñٰŹ���
		String ref_id = multi.getParameter("ref_id") == null?"":multi.getParameter("ref_id");
		String ref_name = multi.getParameter("ref_name") == null?"":multi.getParameter("ref_name");

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//��ȼ� ���
			if ("GIAN".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","��ȼ�",doc_pid,"GIAN");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (��ȼ�)
					jiweonDAO.setGianTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,period,doc_syear,doc_smonth,doc_sdate,kind,subject,content,upload_path);
					//÷������ �Է��ϱ�
					int atcnt = jiweonDAO.setAddFile(multi,doc_pid,filepath);	//atcnt : ÷������ ����

					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("jiweon_master","flag","jw_id",doc_pid,"EE");
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
			//��ȼ� �ӽ�����
			else if ("GIAN_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","��ȼ�",doc_pid,"GIAN");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (��ȼ�)
					jiweonDAO.setGianTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,period,doc_syear,doc_smonth,doc_sdate,kind,subject,content,upload_path);
					//÷������ �Է��ϱ�
					int atcnt = jiweonDAO.setAddFile(multi,doc_pid,filepath);	//atcnt : ÷������ ����

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
			//��ȼ� �� ���
			else if ("R_GIAN".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","��ȼ�",doc_pid,"GIAN");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (��ȼ�)
					jiweonDAO.setGianTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,period,doc_syear,doc_smonth,doc_sdate,kind,subject,content,upload_path);
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("jiweon_master","flag","jw_id",doc_pid,"EE");
					//÷������ �ٽ� �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = jiweonDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
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
			//��ȼ� �� �ӽ�����
			else if ("R_GIAN_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","��ȼ�",doc_pid,"GIAN");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (��ȼ�)
					jiweonDAO.setGianTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,period,doc_syear,doc_smonth,doc_sdate,kind,subject,content,upload_path);
					//÷������ �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = jiweonDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
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
			
			//���Խ�û�� ���
			else if ("MYEONGHAM".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","���Խ�û",doc_pid,"MYEONGHAM");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (���Խ�û��)												
					jiweonDAO.setMyeonghamTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,applicant_id,applicant_name,purpose,chg_content,upload_path);
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("jiweon_master","flag","jw_id",doc_pid,"EE");
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
			//���Խ�û�� �ӽ�����
			else if ("MYEONGHAM_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","���Խ�û",doc_pid,"MYEONGHAM");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (���Խ�û��)												
					jiweonDAO.setMyeonghamTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,applicant_id,applicant_name,purpose,chg_content,upload_path);
	
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
			//���Խ�û�� �� ���
			else if ("R_MYEONGHAM".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","���Խ�û",doc_pid,"MYEONGHAM");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (���Խ�û��)												
					jiweonDAO.setMyeonghamTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,applicant_id,applicant_name,purpose,chg_content,upload_path);
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("jiweon_master","flag","jw_id",doc_pid,"EE");
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
			//���Խ�û�� �� �ӽ�����
			else if ("R_MYEONGHAM_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ӽ������ ����
					//------------------------------
					com.anbtech.gw.db.AppTmpDeleteDAO deleteDAO = new com.anbtech.gw.db.AppTmpDeleteDAO(con);
					deleteDAO.deletePid(old_id,login_id,rootpath);	//������ȣ,login_id,root_path

					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","���Խ�û",doc_pid,"MYEONGHAM");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (���Խ�û��)												
					jiweonDAO.setMyeonghamTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,applicant_id,applicant_name,purpose,chg_content,upload_path);
	
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

			//������ ���
			else if ("SAYU".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������",doc_pid,"SAYU");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (������)												
					jiweonDAO.setSayuTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,content,note,upload_path);
					//÷������ �Է��ϱ�
					int atcnt = jiweonDAO.setAddFile(multi,doc_pid,filepath);	//atcnt : ÷������ ����

					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("jiweon_master","flag","jw_id",doc_pid,"EE");
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
			//������ �ӽ�����
			else if ("SAYU_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������",doc_pid,"SAYU");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (������)												
					jiweonDAO.setSayuTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,content,note,upload_path);
					//÷������ �Է��ϱ�
					int atcnt = jiweonDAO.setAddFile(multi,doc_pid,filepath);	//atcnt : ÷������ ����

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
			//������ �� ���
			else if ("R_SAYU".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������",doc_pid,"SAYU");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (������)												
					jiweonDAO.setSayuTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,content,note,upload_path);
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("jiweon_master","flag","jw_id",doc_pid,"EE");
					//÷������ �ٽ� �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = jiweonDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
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
			//������ �� �ӽ�����
			else if ("R_SAYU_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������",doc_pid,"SAYU");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (������)												
					jiweonDAO.setSayuTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,content,note,upload_path);
					//÷������ �ٽ� �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = jiweonDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
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

			//������ ���
			else if ("HYEOPJO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������",doc_pid,"HYEOPJO");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (������)												
					jiweonDAO.setHyeopjoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,dest_ac_name,period,kind,subject,content,note,upload_path,ref_id,ref_name);
					//÷������ �Է��ϱ�
					int atcnt = jiweonDAO.setAddFile(multi,doc_pid,filepath);	//atcnt : ÷������ ����

					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("jiweon_master","flag","jw_id",doc_pid,"EE");
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
			//������ �ӽ�����
			else if ("HYEOPJO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������",doc_pid,"HYEOPJO");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (������)												
					jiweonDAO.setHyeopjoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,dest_ac_name,period,kind,subject,content,note,upload_path,ref_id,ref_name);
					//÷������ �Է��ϱ�
					int atcnt = jiweonDAO.setAddFile(multi,doc_pid,filepath);	//atcnt : ÷������ ����

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
			//������ �� ���
			else if ("R_HYEOPJO".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������",doc_pid,"HYEOPJO");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (������)												
					jiweonDAO.setHyeopjoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,dest_ac_name,period,kind,subject,content,note,upload_path,ref_id,ref_name);
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("jiweon_master","flag","jw_id",doc_pid,"EE");
					//÷������ �ٽ� �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = jiweonDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
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
			//������ �� �ӽ�����
			else if ("R_HYEOPJO_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.jiweon.db.JiWeonDAO jiweonDAO = new com.anbtech.es.jiweon.db.JiWeonDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//------------------------------
					// �ű� ����
					//-------------------------------
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������",doc_pid,"HYEOPJO");
					
					//�������� TABLE (jiweon_master) �Է��ϱ� (������)												
					jiweonDAO.setHyeopjoTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,dest_ac_name,period,kind,subject,content,note,upload_path,ref_id,ref_name);
					//÷������ �ٽ� �Է��ϱ� (atcnt : ÷������ ����)
					int atcnt = jiweonDAO.setUpdateFile(multi,doc_pid,filepath,fname,sname,ftype,fsize,attache_cnt);
					
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
		}
	} //doPost()
}
