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

public class OutDocumentServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 20;

	/********
	 * �Ҹ���
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó�� (��Ϻ���)
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
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
		String login_division = sl.division;

		//���ڰ��� �����׸� (����)
		String app_mode = request.getParameter("app_mode");		if(app_mode == null) app_mode = "";		//���ڰ��� ó�����
		String writer_id = request.getParameter("writer_id");		if(writer_id == null) writer_id = "";	//�����(��ŵ���� �ϼ��� ����)
		String writer_name = request.getParameter("writer_name");	if(writer_name == null) writer_name = "";	//�����(��ŵ���� �ϼ��� ����)
		String doc_pid = request.getParameter("doc_id");			if(doc_pid == null) doc_pid = "";	//������ȣ (1���ô� ���ð�����ȣ�� ����)
		String doc_line = request.getParameter("doc_app_line");	if(doc_line == null) doc_line = "";	//���缱
		String doc_subj = request.getParameter("doc_sub");		if(doc_subj == null) doc_subj = "";	//����
		String doc_peri = request.getParameter("doc_per");		if(doc_peri == null) doc_peri = "";	//�����Ⱓ
		String doc_secu = request.getParameter("doc_sec");		if(doc_secu == null) doc_secu = "";	//���ȵ��
		String writer_date = request.getParameter("date");		if(writer_date == null) writer_date = "";	//�Է��� �ú�

		//�����ڵ�
		String id = Hanguel.toHanguel(request.getParameter("id"))==null?"":Hanguel.toHanguel(request.getParameter("id"));

		//��� �� ���� ������ �ĸ�����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"IND_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"subject":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//������ ���� ����path
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
		
			//��ܰ��� ��ü LIST��������
			if("OTD_L".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getDoc_List(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = docDAO.getCurrentPage();		//����������
				int Tpage = docDAO.getTotalPage();			//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/ods/OutDocument_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//��ܰ��� �������� ���� (������)
			else if("OTD_V".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocument_review.jsp").forward(request,response);
			}
			//��ܰ��� �������� ���� (������)
			else if("OTD_A".equals(mode)){	
				//��ܰ���
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);

				//���缱 ����
				com.anbtech.dms.db.OfficialDocumentAppDAO appDAO = new com.anbtech.dms.db.OfficialDocumentAppDAO(con);
				ArrayList app_list = new ArrayList();
				app_list = appDAO.getDoc_Read(doc_pid);			//���ڰ��� ������ȣ
				request.setAttribute("App_One", app_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocument_Appview.jsp").forward(request,response);
			}
			//��ܰ��� �������� �����ϱ� ���� ��������
			else if("OTD_M".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocument_modify.jsp").forward(request,response);
			}
			//��ܰ��� �������� �����ϱ�
			else if("OTD_D".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				//-------------------------------------
				// �ش� ������ȣ ���� �����ϱ�
				//-------------------------------------
				con.setAutoCommit(false);	// Ʈ������� ���� 
				try{
					docDAO.deletePid(id,upload_path);
					con.commit();
					con.setAutoCommit(true);

					//-------------------------------------
					// ���������� ������ ����Ʈ ����
					//-------------------------------------
					//��ü List
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_List(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					//����������/��ü������
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();			//��ü������
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/ods/OutDocument_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				

				} catch (Exception e) {
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}

			}
			//��ܰ��� ������ Include�ϱ�
			else if("ODS_V".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocument_include.jsp").forward(request,response);
			}
			//��ܹ��� �̸��Ϻ��������� ������ �����ϱ�
			else if("ODS_R".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocument_email.jsp").forward(request,response);
			}
			//��ܰ��� �����۽ſ� ����ϱ�  (������)
			else if("OTD_S".equals(mode)){	
				//��ܰ���
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocument_SPrint.jsp").forward(request,response);
			}

		}catch (Exception e){
			//������� �������� �б�
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} // doGet()


	/**********************************
	 * post������� �Ѿ���� �� ó�� (�Է�,����,����,���)
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
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
		String login_division = sl.division;

		//���ڰ��� �����׸� (����)
		String app_mode = Hanguel.toHanguel(request.getParameter("app_mode"));		if(app_mode == null) app_mode = "";		//���ڰ��� ó�����
		String writer_id = Hanguel.toHanguel(request.getParameter("writer_id"));		if(writer_id == null) writer_id = "";	//�����(��ŵ���� �ϼ��� ����)
		String writer_name = Hanguel.toHanguel(request.getParameter("writer_name"));	if(writer_name == null) writer_name = "";	//�����(��ŵ���� �ϼ��� ����)
		String doc_pid = Hanguel.toHanguel(request.getParameter("doc_id"));			if(doc_pid == null) doc_pid = "";	//������ȣ (1���ô� ���ð�����ȣ�� ����)
		String doc_line = Hanguel.toHanguel(request.getParameter("doc_app_line"));	if(doc_line == null) doc_line = "";	//���缱
		String doc_subj = Hanguel.toHanguel(request.getParameter("doc_sub"));		if(doc_subj == null) doc_subj = "";	//����
		String doc_peri = Hanguel.toHanguel(request.getParameter("doc_per"));		if(doc_peri == null) doc_peri = "";	//�����Ⱓ
		String doc_secu = Hanguel.toHanguel(request.getParameter("doc_sec"));		if(doc_secu == null) doc_secu = "";	//���ȵ��
		String writer_date = Hanguel.toHanguel(request.getParameter("date"));		if(writer_date == null) writer_date = "";	//�Է��� �ú�
		String attache_cnt = Hanguel.toHanguel(request.getParameter("attache_cnt"));	if(attache_cnt == null) attache_cnt = "0";	//÷������ ����
		int atcnt = Integer.parseInt(attache_cnt);

		//�����ڵ�
		String id = Hanguel.toHanguel(request.getParameter("id"))==null?"":Hanguel.toHanguel(request.getParameter("id"));

		//��� �� ���� ������ �ĸ�����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"OTD_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"subject":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//������ ���� ����path
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
		
			//��ܰ��� ��ü LIST��������
			if("OTD_L".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				//��ü List
				ArrayList table_list = new ArrayList();
				table_list = docDAO.getDoc_List(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("Data_List", table_list);

				//����������/��ü������
				int Cpage = docDAO.getCurrentPage();		//����������
				int Tpage = docDAO.getTotalPage();			//��ü������
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/ods/OutDocument_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//��ܰ��� �������� ����
			else if("OTD_V".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocument_review.jsp").forward(request,response);
			}
			//��ܰ��� �������� �����ϱ�
			else if("OTD_M".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocument_modify.jsp").forward(request,response);
			}
			//��ܰ��� �������� �����ϱ�
			else if("OTD_D".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				//-------------------------------------
				// �ش� ������ȣ ���� �����ϱ�
				//-------------------------------------
				con.setAutoCommit(false);	// Ʈ������� ���� 
				try{
					docDAO.deletePid(id,upload_path);
					con.commit();
					con.setAutoCommit(true);

					
					//-------------------------------------
					// ���������� ������ ����Ʈ ����
					//-------------------------------------
					//��ü List
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_List(login_id,sItem,sWord,page,max_display_cnt);
					request.setAttribute("Data_List", table_list);
					//����������/��ü������
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();			//��ü������
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/ods/OutDocument_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				

				} catch (Exception e) {
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			
			}
			//��ܰ��� ������ Include�ϱ�
			else if("ODS_V".equals(mode)){	
				com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
				ArrayList table_list = new ArrayList();

				table_list = docDAO.getDoc_Read(id);
				request.setAttribute("Data_One", table_list);
				
				getServletContext().getRequestDispatcher("/ods/OutDocument_include.jsp").forward(request,response);
			}
			//��ܰ��� ������ �ϱ�
			else if("ODS".equals(mode)){
				com.anbtech.gw.db.AppInputMasterDAO masterDAO = new com.anbtech.gw.db.AppInputMasterDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���ڰ��� TABEL (app_master) �Է��ϱ�
					masterDAO.setTable(app_mode,doc_pid,doc_subj,writer_id,writer_name,writer_date,doc_line,doc_peri,doc_secu,"","��ܰ���",id,"ODS");
					
					//���ڰ��� TABEL (app_master)�� ÷������ ���� update�ϱ�
					masterDAO.setAttacheCount("app_master",doc_pid,atcnt);

					//���ù��� Table�� ���ڰ��� ������ �˷� �ֱ�
					masterDAO.setTableLink("OutDocument_send","flag","id",id,"EE");
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//ó���޽��� ����ϱ�
					//out.println("<script>	alert('��ŵǾ����ϴ�.'); window.returnValue='RL';	self.close(); </script>");	out.close();
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=ASK_ING');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}

		}catch (Exception e){
			//������� �������� �б�
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} //doPost()
}

