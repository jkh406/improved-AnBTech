import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class AcknowledgmentServlet extends HttpServlet {
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

		//���ο� ����
		String mode = Hanguel.toHanguel(request.getParameter("mode"));		if(mode == null) mode = "";		//���ο� ����
		String no = Hanguel.toHanguel(request.getParameter("no"));			if(no == null) no = "";			//���ο� ������ȣ
		String akg_pid = no+"|"+mode;	
		
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

			com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
			com.anbtech.gw.db.AppModuleAttchDAO attchDAO = new com.anbtech.gw.db.AppModuleAttchDAO(con);	//÷�μ���

			con.setAutoCommit(false);	// Ʈ������� ����  
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			try{
				//���ڰ��� TABEL (app_master) �Է��ϱ�
				masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","���ο�",akg_pid,"AKG");
				
				//÷������ ���� update�ϱ�
				int atcnt = attchDAO.searchAttchCntAKG(no);
				masterDAO.setAttacheCount("app_master",doc_pid,atcnt);
	
				//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
				if(mode.equals("report_w"))		//�űԵ��
					masterDAO.setTableLink("ca_master","aid","tmp_approval_no",no,"EE");
				con.commit(); // commit�Ѵ�.

				//ó���޽��� ����ϱ�
				//out.println("	<script>");
				//out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
				//out.println("	</script>");
				//out.close();
				getServletContext().getRequestDispatcher("/ca/result_app.jsp").forward(request,response);
			}catch(Exception e){
				con.rollback();
				request.setAttribute("ERR_MSG",e.toString());
				getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			}finally{
				con.setAutoCommit(true);
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
