import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.gw.entity.*;
import com.anbtech.gw.db.*;
import com.anbtech.gw.business.*;
import com.anbtech.file.FileWriteString;
import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;

public class ApprovalInputServlet extends HttpServlet {

	private com.anbtech.dbconn.DBConnectionManager connMgr;
	private Connection con;

	/********
	 * �Ҹ���
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó��
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
			
	} // doGet()


	/**********************************
	 * post������� �Ѿ���� �� ó��
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//MultipartRequest ũ��, ������丮
		//String filepath = getServletContext().getRealPath("")+"/upload/eleApproval/"+login_id+"/addfile";
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/eleApproval/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory�����ϱ�

		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//������������ �� �޾ƿ´�. multi���� ������
		String mode = multi.getParameter("mode");				if(mode == null) mode = "";					//������ó�����
		//out.println("	<script>	alert('"+mode+":"+request.getServletPath()+"');	</script>");	out.close();

		String writer_id = multi.getParameter("login_id");		if(writer_id == null) writer_id = "";		//loign id
		String writer_name = multi.getParameter("login_name");	if(writer_name == null) writer_name = "";	//login name	
		String writer_date = multi.getParameter("date");		if(writer_date == null) writer_date = "";	//date	
		String doc_pid = multi.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";			//������ȣ
		String doc_subj = multi.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";			//���缱
		String doc_line = multi.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";			//���缱
		String doc_peri = multi.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";			//�����Ⱓ
		String doc_secu = multi.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";			//���ȵ��
		String doc_cont = multi.getParameter("CONTENT");		if(doc_cont == null) doc_cont = "";			//��������	
		String doc_plid = multi.getParameter("doc_lid");		if(doc_plid == null) doc_plid = "";			//���ù��� ������ȣ	
		String doc_flag = multi.getParameter("doc_flag");		if(doc_flag == null) doc_flag = "";			//���ù��� ����	
		String upload_path = multi.getParameter("upload_path");	if(upload_path == null) upload_path = "";	//���ù��� ����	
		//out.println("	<script>	alert('"+upload_path+"');	</script>");	out.close();

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			// ���ڰ��� ��� (������ ����)
			if ("REQ".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//table insert
					masterDAO.setTable(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
					
					//���ù��� Table�� ������ �˷� �ֱ�
					if(doc_flag.equals("SERVICE"))
						masterDAO.setTableLink("history_table","flag","ah_id",doc_plid,"EE");

					//���ϸ� �˾Ƴ��� �� ���ϸ� �ٲ��ֱ�
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);

					//���ϸ� DB�� update �ϱ� 
					masterDAO.updFile(file.getText(),doc_pid);

					//÷������ ���� update�ϱ� (�Ϲݹ����� �������� ó���ϰ� �������� ������ �Է¼��긴���� ó��)
					int atcnt = 0;
					if(doc_flag.equals("SERVICE")) {		//������
						com.anbtech.gw.db.AppModuleAttchDAO attchDAO = new com.anbtech.gw.db.AppModuleAttchDAO(con);
						atcnt = attchDAO.searchAttchCntSERVICE(doc_plid);
						masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					} else {								//�Ϲݹ�������
						atcnt = masterDAO.getAttacheCount();
						masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					}

					con.commit();
					con.setAutoCommit(true);

					//ó���޽��� ����ϱ�
					//if(doc_flag.equals("SERVICE")) {
					//	out.println("<script>	alert('��ŵǾ����ϴ�.');	self.close();  </script>");	out.close();
					//} else {
						out.println("	<script>");
						out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
						out.println("	</script>");
						out.close();
					//}
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���ڰ��� ��� (�ϰ��� ����)
			else if("ABAT".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//table insert
					masterDAO.setTable(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
		
					//���ù��� Table�� ������ �˷� �ֱ�
					if(doc_flag.equals("SERVICE"))
						masterDAO.setTableLink("history_table","flag","ah_id",doc_plid,"EE");

					//���ϸ� �˾Ƴ���
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);

					//���ϸ� DB�� update �ϱ� 
					masterDAO.updFile(file.getText(),doc_pid);

					//÷������ ���� update�ϱ� (�Ϲݹ����� �������� ó���ϰ� �������� ������ �Է¼��긴���� ó��)
					int atcnt = 0;
					if(doc_flag.equals("SERVICE")) {		//������
						com.anbtech.gw.db.AppModuleAttchDAO attchDAO = new com.anbtech.gw.db.AppModuleAttchDAO(con);
						atcnt = attchDAO.searchAttchCntSERVICE(doc_plid);
						masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					} else {								//�Ϲݹ�������
						atcnt = masterDAO.getAttacheCount();
						masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					}

					con.commit();
					con.setAutoCommit(true);

					//ó���޽��� ����ϱ�
					//if(doc_flag.equals("SERVICE")) {
					//	out.println("<script>	alert('��ŵǾ����ϴ�.');	self.close();  </script>");	out.close();
					//} else {
						out.println("	<script>");
						out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
						out.println("	</script>");
						out.close();
					//}
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
				
			}
			//�ӽú��� 
			else if("TMP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//table insert
					masterDAO.setTable(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);

					//����ó��
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);

					//���� update
					masterDAO.updFile(file.getText(),doc_pid);

					//÷������ ���� update�ϱ�
					int atcnt = masterDAO.getAttacheCount();
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					con.commit();
					con.setAutoCommit(true);

					//ó���޽��� ����ϱ�
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			// ���ڰ��� ��� (������ ����) ���ۼ� �ϱ�
			else if("REQ_UP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//������� update
					masterDAO.setTableUpdate(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
		
					//���ϸ� �˾Ƴ���
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);
					//out.println("	<script>	alert('"+file.getText()+"');	</script>");	out.close();

					//���ϸ� DB�� update �ϱ� 
					masterDAO.updFile(file.getText(),doc_pid);

					//÷������ ���� update�ϱ�
					int atcnt = masterDAO.getAttacheCount(doc_pid);
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					con.commit();
					con.setAutoCommit(true);

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('��ŵǾ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���ڰ��� ��� (�ϰ��� ����) ���ۼ��ϱ�
			else if("ABAT_UP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//������� update
					masterDAO.setTableUpdate(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
		
					//���ϸ� �˾Ƴ���
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);
					//out.println("	<script>	alert('"+file.getText()+"');	</script>");	out.close();

					//���ϸ� DB�� update �ϱ� 
					masterDAO.updFile(file.getText(),doc_pid);

					//÷������ ���� update�ϱ�
					int atcnt = masterDAO.getAttacheCount(doc_pid);
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					con.commit();
					con.setAutoCommit(true);

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('��ŵǾ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�ӽú��� ���ۼ��ϱ�
			else if("TMP_UP".equals(mode)) {
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				try{
					//������� update
					masterDAO.setTableUpdate(mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,upload_path,doc_cont,doc_plid,doc_flag);
		
					//���ϸ� �˾Ƴ���
					com.anbtech.gw.entity.TableText file = new com.anbtech.gw.entity.TableText();
					file = masterDAO.getFile_frommulti(multi,upload_path,writer_id,doc_pid);
					//out.println("	<script>	alert('"+file.getText()+"');	</script>");	out.close();

					//���ϸ� DB�� update �ϱ� 
					masterDAO.updFile(file.getText(),doc_pid);

					//÷������ ���� update�ϱ�
					int atcnt = masterDAO.getAttacheCount(doc_pid);
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					con.commit();
					con.setAutoCommit(true);

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('����Ǿ����ϴ�.');	self.close();  </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					out.close();
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			//con�Ҹ�
			close(con);
			out.close();
		}
	} //doPost()
}