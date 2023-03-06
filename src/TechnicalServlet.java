import com.anbtech.text.Hanguel;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class TechnicalServlet extends HttpServlet {
	private DBConnectionManager connMgr;
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		
		String mode = Hanguel.toHanguel(request.getParameter("mode"));		
			if(mode == null) mode = "";													//���������
		//ó���޽��� ����ϱ� : AnBDMS���� �б�Ǿ� �Ѿ��
		out.println("	<script>");
		//out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
		out.println(" alert('��ŵǾ����ϴ�.');");
		out.println("	</script>");
		out.close();
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

		//������� ����
		String tablename = Hanguel.toHanguel(request.getParameter("tablename"));		
			if(tablename == null) tablename = "";										//table name
		String mode = Hanguel.toHanguel(request.getParameter("mode"));		
			if(mode == null) mode = "";													//���������
		String d_id = Hanguel.toHanguel(request.getParameter("d_id"));			
			if(d_id == null) d_id = "";													//������� ������ȣ
		String ver = Hanguel.toHanguel(request.getParameter("ver"));			
			if(ver == null) ver = "";													//������� version
		String td_pid = d_id+"|"+tablename+"|"+ver;
			
		
		//��������� ���ڰ��� �����׸� (����)
		String app_mode = Hanguel.toHanguel(request.getParameter("app_mode"));		if(app_mode == null) app_mode = "";		//���ڰ��� ó�����
		String writer_id = Hanguel.toHanguel(request.getParameter("writer_id"));		if(writer_id == null) writer_id = "";	//�����(��ŵ���� �ϼ��� ����)
		String writer_name = Hanguel.toHanguel(request.getParameter("writer_name"));	if(writer_name == null) writer_name = "";	//�����(��ŵ���� �ϼ��� ����)
		String doc_pid = Hanguel.toHanguel(request.getParameter("doc_id"));			if(doc_pid == null) doc_pid = "";	//������ȣ (1���ô� ���ð�����ȣ�� ����)
		String doc_line = Hanguel.toHanguel(request.getParameter("doc_app_line"));	if(doc_line == null) doc_line = "";	//���缱
		String doc_subj = Hanguel.toHanguel(request.getParameter("doc_sub"));		if(doc_subj == null) doc_subj = "";	//����
		String doc_peri = Hanguel.toHanguel(request.getParameter("doc_per"));		if(doc_peri == null) doc_peri = "";	//�����Ⱓ
		String doc_secu = Hanguel.toHanguel(request.getParameter("doc_sec"));		if(doc_secu == null) doc_secu = "";	//���ȵ��
		String writer_date = Hanguel.toHanguel(request.getParameter("date"));		if(writer_date == null) writer_date = "";	//�Է��� �ú�

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			con.setAutoCommit(false);	// Ʈ������� ����  
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
			try{
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				com.anbtech.gw.db.AppModuleAttchDAO attchDAO = new com.anbtech.gw.db.AppModuleAttchDAO(con);	//÷�μ���

				//���ڰ��� TABEL (app_master) �Է��ϱ�
				masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","�������",td_pid,"TD");
					
				//÷������ ���� update�ϱ�
				int atcnt = attchDAO.searchAttchCntTD(d_id);
				masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
					
				//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
				//response.sendRedirect("AnBDMS?mode=review&tablename="+tablename+"&d_id="+d_id+"&ver="+ver+"&aid="+doc_pid);
				com.anbtech.dms.db.MasterDAO mDAO = new com.anbtech.dms.db.MasterDAO(con);
				mDAO.updateStat(tablename,d_id,ver,"2");
				if(ver.equals("1.0")) mDAO.updateStat("","2",d_id);

				con.commit();
				con.setAutoCommit(true);

				//�б��ϱ�AnBDMS?mode=mylist&tablename=techdoc_data
				//getServletContext().getRequestDispatcher("AnBDMS?mode=review&tablename="+tablename+"&d_id="+d_id+"&ver="+ver+"&aid="+doc_pid).forward(request,response);
				getServletContext().getRequestDispatcher("/dms/result_app.jsp?tablename="+tablename).forward(request,response);
			}catch(Exception e){
				con.rollback();
				con.setAutoCommit(true);
				request.setAttribute("ERR_MSG",e.toString());
				getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
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