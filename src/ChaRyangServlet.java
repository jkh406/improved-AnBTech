import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ChaRyangServlet extends HttpServlet {
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
		
		//��������� ���ڰ��� �����׸� (�����)
		String writer_id = multi.getParameter("writer_id");		if(writer_id == null) writer_id = "";	//�����(��ŵ���� �ϼ��� ����)
		String writer_name = multi.getParameter("writer_name");	if(writer_name == null) writer_name = "";	//�����(��ŵ���� �ϼ��� ����)
		String doc_pid = multi.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";	//������ȣ (1���ô� ���ð�����ȣ�� ����)
		String doc_line = multi.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";	//���缱
		String doc_subj = multi.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";	//����
		String doc_peri = multi.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";	//�����Ⱓ
		String doc_secu = multi.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";	//���ȵ��
		String writer_date = multi.getParameter("date");		if(writer_date == null) writer_date = "";	//�Է��� �ú�

		//2���μ� �����û�� 
		String link_id = multi.getParameter("link_id");	if(link_id == null) link_id = "";			//���ù��� ��ȣ

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//������û ���
			if ("BAE_CHA".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������û��",doc_pid,"BAE_CHA");
						
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("charyang_master","flag","cr_id",doc_pid,"EE");
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

			//������û 2���μ� ���
			else if ("BAE_CHA_SEC".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","������û��",link_id,"BAE_CHA");
					
					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("charyang_master","flag2","cr_id",link_id,"EE");
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
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} //doPost()
}
