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

		//����ٹ���û�� 
		String doc_syear = multi.getParameter("doc_syear");	if(doc_syear == null) doc_syear = "";	//��
		String doc_smonth = multi.getParameter("doc_smonth");if(doc_smonth == null) doc_smonth = "";//��
		String doc_sdate = multi.getParameter("doc_sdate");	if(doc_sdate == null) doc_sdate = "";	//��
		String job_kind = multi.getParameter("job_kind");	if(job_kind == null) job_kind = "";	//�з�
		String cost_prs = multi.getParameter("cost_prs");	if(cost_prs == null) cost_prs = "";	//�ĺ�����Ȯ��
		String worker_cnt = multi.getParameter("work_cnt");	if(worker_cnt == null) worker_cnt = "0";
		int work_cnt = Integer.parseInt(worker_cnt);			//����ٹ���û �÷���

		//�ٹ��� ��Ȳ �迭�� ���
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

		//������ id (�ӽ�������繮�� : ���� �� �űԵ������ ó��Ű ����)
		String old_id = multi.getParameter("old_id");			if(old_id == null) old_id = "";		//�ӽ������� ���� ������ȣ

		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//����ٹ���û�� ���
			if ("YEONJANG".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.janeup.db.JanEupDAO janeupDAO = new com.anbtech.es.janeup.db.JanEupDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","����ٹ���û��",doc_pid,"YEONJANG");
					
					//�ܾ����� TABLE (janeup_master) �Է��ϱ� (����ٹ���û��)																		
					janeupDAO.setYeonjangMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,doc_syear,doc_smonth,doc_sdate,job_kind,cost_prs);
					
					//�������� Table (janeup_worker) �Է��ϱ�
					for(int n=0,cid=1; n<work_cnt; n++,cid++) {
						//���ڵ� �����
						String doc_cid = doc_pid + "_" + cid;
						if(data[n][0].length() != 0)
							janeupDAO.setYeonjangWorkerTable(doc_pid,doc_cid,doc_syear,doc_smonth,doc_sdate,data[n][0],data[n][1],data[n][2],data[n][3],data[n][4],job_kind);

					}
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("janeup_master","flag","ju_id",doc_pid,"EE");
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
			//����ٹ���û�� �ӽ�����
			else if ("YEONJANG_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.janeup.db.JanEupDAO janeupDAO = new com.anbtech.es.janeup.db.JanEupDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","����ٹ���û",doc_pid,"YEONJANG");
					
					//�ܾ����� TABLE (janeup_master) �Է��ϱ� (����ٹ���û��)																		
					janeupDAO.setYeonjangMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,doc_syear,doc_smonth,doc_sdate,job_kind,cost_prs);
					
					//�������� Table (janeup_worker) �Է��ϱ�
					for(int n=0,cid=1; n<work_cnt; n++,cid++) {
						//���ڵ� �����
						String doc_cid = doc_pid + "_" + cid;
						if(data[n][0].length() != 0)
							janeupDAO.setYeonjangWorkerTable(doc_pid,doc_cid,doc_syear,doc_smonth,doc_sdate,data[n][0],data[n][1],data[n][2],data[n][3],data[n][4],job_kind);

					}
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
			//����ٹ���û�� �� ���
			if ("R_YEONJANG".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.janeup.db.JanEupDAO janeupDAO = new com.anbtech.es.janeup.db.JanEupDAO(con);

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
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","����ٹ���û��",doc_pid,"YEONJANG");
					
					//�ܾ����� TABLE (janeup_master) �Է��ϱ� (����ٹ���û��)																		
					janeupDAO.setYeonjangMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,doc_syear,doc_smonth,doc_sdate,job_kind,cost_prs);
					
					//�������� Table (janeup_worker) �Է��ϱ�
					for(int n=0,cid=1; n<work_cnt; n++,cid++) {
						//���ڵ� �����
						String doc_cid = doc_pid + "_" + cid;
						if(data[n][0].length() != 0)
							janeupDAO.setYeonjangWorkerTable(doc_pid,doc_cid,doc_syear,doc_smonth,doc_sdate,data[n][0],data[n][1],data[n][2],data[n][3],data[n][4],job_kind);

					}
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("janeup_master","flag","ju_id",doc_pid,"EE");
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
			//����ٹ���û�� �� �ӽ�����
			else if ("R_YEONJANG_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.janeup.db.JanEupDAO janeupDAO = new com.anbtech.es.janeup.db.JanEupDAO(con);

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
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","����ٹ���û",doc_pid,"YEONJANG");
					
					//�ܾ����� TABLE (janeup_master) �Է��ϱ� (����ٹ���û��)																		
					janeupDAO.setYeonjangMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,doc_syear,doc_smonth,doc_sdate,job_kind,cost_prs);
					
					//�������� Table (janeup_worker) �Է��ϱ�
					for(int n=0,cid=1; n<work_cnt; n++,cid++) {
						//���ڵ� �����
						String doc_cid = doc_pid + "_" + cid;
						if(data[n][0].length() != 0)
							janeupDAO.setYeonjangWorkerTable(doc_pid,doc_cid,doc_syear,doc_smonth,doc_sdate,data[n][0],data[n][1],data[n][2],data[n][3],data[n][4],job_kind);

					}
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