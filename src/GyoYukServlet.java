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

		//�������� 
		String e_year = multi.getParameter("doc_syear");	if(e_year == null) e_year = "";	//�������� ��
		String e_month = multi.getParameter("doc_smonth");	if(e_month == null) e_month = "";//�������� ��
		String e_date = multi.getParameter("doc_sdate");	if(e_date == null) e_date = "";	//�������� ��
		String lecturer_id = multi.getParameter("lecturer_id");	if(lecturer_id == null) lecturer_id = "";	//���� ���
		String lecturer_name = multi.getParameter("lecturer_name");	if(lecturer_name == null) lecturer_name = "";	//���� �̸�
		String major_kind = "";
		String major_kind1 = multi.getParameter("major_kind1");	if(major_kind1 == null) major_kind1 = "";//����
		String major_kind2 = multi.getParameter("major_kind2");	if(major_kind2 == null) major_kind2 = "";//�μ�
		major_kind = major_kind1+":"+major_kind2+":";
		String place = multi.getParameter("place");	if(place == null) place = "";				//���
		String part_cnt = multi.getParameter("participators_cnt");	if(part_cnt == null) part_cnt = "";	//��������ο�
		String edu_subject = multi.getParameter("edu_subject");	if(edu_subject == null) edu_subject = "";//������
		String antiprt_prs = "";	//������ ó��
		String antiprt_prs1 = multi.getParameter("antiprt_prs1");	if(antiprt_prs1 == null) antiprt_prs1 = "";	//�米��
		String antiprt_prs2 = multi.getParameter("antiprt_prs2");	if(antiprt_prs2 == null) antiprt_prs2 = "";	//���ޱ���
		String antiprt_prs3 = multi.getParameter("antiprt_prs3");	if(antiprt_prs3 == null) antiprt_prs3 = "";	//��Ÿ
		antiprt_prs = antiprt_prs1+":"+antiprt_prs2+":"+antiprt_prs3+":";
		String content = multi.getParameter("content");	if(content == null) content = "";		//����
		String upload_path = multi.getParameter("upload_path");	if(upload_path == null) upload_path = "";//path
		String people_total = multi.getParameter("people_total");	if(people_total == null) people_total = "0"; //�ѱ����ο��迭��
		int cnt = Integer.parseInt(people_total);
		//�Ǳ����� ����� �̸��� �迭�� ���
		String[][] people = new String[cnt][3];
		for(int i=0; i<cnt; i++) {
			people[i][0] = multi.getParameter("participators_id"+i);	if(people[i][0] == null) people[i][0] = "";	//�Ǳ����� ���
			people[i][1] = multi.getParameter("participators_name"+i);	if(people[i][1] == null) people[i][1] = "";	//�Ǳ����� �̸�
			people[i][2] = multi.getParameter("prt_etc"+i);	if(people[i][2] == null) people[i][2] = "";				//�����
		}
		
		//������ id (�ӽ�������繮�� : ���� �� �űԵ������ ó��Ű ����)
		String old_id = multi.getParameter("old_id");			if(old_id == null) old_id = "";		//�ӽ������� ���� ������ȣ

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//�������� ���
			if ("GYOYUK_ILJI".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.gyoyuk.db.GyoYukDAO gyoyukDAO = new com.anbtech.es.gyoyuk.db.GyoYukDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","��������",doc_pid,"GYOYUK_ILJI");
					
					//�������� TABLE (gyoyuk_master) �Է��ϱ� (��������)
					gyoyukDAO.setGyoyukiljiMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,e_year,e_month,e_date,lecturer_id,lecturer_name,major_kind,place,part_cnt,edu_subject,antiprt_prs,content,upload_path);	
					
					//�������� ���� TABLE (gyoyuk_part) �Է��ϱ� (��������)
					for(int n=0,cid=1; n<cnt; n++,cid++) {
						String doc_cid = doc_pid + "_" + cid;
						if(people[n][0].length() != 0)
							gyoyukDAO.setGyoyukiljiSubTable(doc_pid,doc_cid,e_year,e_month,e_date,major_kind,div_name,edu_subject,people[n][0],people[n][1],people[n][2]);	
					}
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("gyoyuk_master","flag","gy_id",doc_pid,"EE");
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
			//�������� �ӽ�����
			else if ("GYOYUK_ILJI_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.gyoyuk.db.GyoYukDAO gyoyukDAO = new com.anbtech.es.gyoyuk.db.GyoYukDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","��������",doc_pid,"GYOYUK_ILJI");
					
					//�������� TABLE (gyoyuk_master) �Է��ϱ� (��������)
					gyoyukDAO.setGyoyukiljiMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,e_year,e_month,e_date,lecturer_id,lecturer_name,major_kind,place,part_cnt,edu_subject,antiprt_prs,content,upload_path);	
					
					//�������� ���� TABLE (gyoyuk_part) �Է��ϱ� (��������)
					for(int n=0,cid=1; n<cnt; n++,cid++) {
						String doc_cid = doc_pid + "_" + cid;
						if(people[n][0].length() != 0)
							gyoyukDAO.setGyoyukiljiSubTable(doc_pid,doc_cid,e_year,e_month,e_date,major_kind,div_name,edu_subject,people[n][0],people[n][1],people[n][2]);	
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
			//�������� ����
			if ("R_GYOYUK_ILJI".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.gyoyuk.db.GyoYukDAO gyoyukDAO = new com.anbtech.es.gyoyuk.db.GyoYukDAO(con);

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
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","��������",doc_pid,"GYOYUK_ILJI");
					
					//�������� TABLE (gyoyuk_master) �Է��ϱ� (��������)
					gyoyukDAO.setGyoyukiljiMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,e_year,e_month,e_date,lecturer_id,lecturer_name,major_kind,place,part_cnt,edu_subject,antiprt_prs,content,upload_path);	
					
					//�������� ���� TABLE (gyoyuk_part) �Է��ϱ� (��������)
					for(int n=0,cid=1; n<cnt; n++,cid++) {
						String doc_cid = doc_pid + "_" + cid;
						if(people[n][0].length() != 0)
							gyoyukDAO.setGyoyukiljiSubTable(doc_pid,doc_cid,e_year,e_month,e_date,major_kind,div_name,edu_subject,people[n][0],people[n][1],people[n][2]);	
					}
	
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("gyoyuk_master","flag","gy_id",doc_pid,"EE");
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
			//�������� �� �ӽ�����
			else if ("R_GYOYUK_ILJI_TMP".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.es.gyoyuk.db.GyoYukDAO gyoyukDAO = new com.anbtech.es.gyoyuk.db.GyoYukDAO(con);

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
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","��������",doc_pid,"GYOYUK_ILJI");
					
					//�������� TABLE (gyoyuk_master) �Է��ϱ� (��������)
					gyoyukDAO.setGyoyukiljiMasterTable(doc_pid,user_id,user_name,user_code,user_rank,div_id,div_code,div_name,writer_date,e_year,e_month,e_date,lecturer_id,lecturer_name,major_kind,place,part_cnt,edu_subject,antiprt_prs,content,upload_path);	
					
					//�������� ���� TABLE (gyoyuk_part) �Է��ϱ� (��������)
					for(int n=0,cid=1; n<cnt; n++,cid++) {
						String doc_cid = doc_pid + "_" + cid;
						if(people[n][0].length() != 0)
							gyoyukDAO.setGyoyukiljiSubTable(doc_pid,doc_cid,e_year,e_month,e_date,major_kind,div_name,edu_subject,people[n][0],people[n][1],people[n][2]);	
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
			out.close();
		}
	} //doPost()
}