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

public class InDocumentMultiServlet extends HttpServlet {

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

		//MultipartRequest ũ��, ������丮
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/ods/"+login_id+"/addfile";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory�����ϱ�

		String maxFileSize = "50";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�
		
		//��ü ���� (�系����)
		String mode = multi.getParameter("mode");				if(mode == null) mode = "";		//������ó�����
		
		//�系���� (�����׸�)	
		String user_id = multi.getParameter("user_id");			if(user_id == null) user_id = "";		//user id
		String user_name = multi.getParameter("user_name");		if(user_name == null) user_name = "";	//user_name
		String user_rank = multi.getParameter("user_rank");		if(user_rank == null) user_rank = "";	//user_rank 
		String div_id = multi.getParameter("div_id");			if(div_id == null) div_id = "";			//div_id 
		String div_name = multi.getParameter("div_name");		if(div_name == null) div_name = "";		//div_name 
		String code = multi.getParameter("code");				if(code == null) code = "";		//code 
		String div_code = multi.getParameter("div_code");		if(div_code == null) div_code = "";		//div_code
		
		//�系���� (����׸�)
		String id = multi.getParameter("id");	if(id == null) id = "";								//id
		String serial_no = multi.getParameter("serial_no");	if(serial_no == null) serial_no = "";	//�Ϸù�ȣ
		String class_no = multi.getParameter("class_no");	if(class_no == null) class_no = "";		//�з���ȣ
		String doc_id = multi.getParameter("doc_id");	if(doc_id == null) doc_id = "";				//������ȣ
		String slogan = multi.getParameter("slogan");	if(slogan == null) slogan = "";				//ǥ��
		String title_name = multi.getParameter("title_name");if(title_name == null) title_name = "";//�Ӹ���
		String in_date = multi.getParameter("in_date");	if(in_date == null) in_date = "";			//�ۼ�����
		String app_date = multi.getParameter("app_date");	if(app_date == null) app_date = "";		//��������
		String enforce_date = multi.getParameter("enforce_date");	if(enforce_date == null) enforce_date = "";			//��������
		String receive = multi.getParameter("receive");	if(receive == null) receive = "";			//����
		String reference = multi.getParameter("reference");	if(reference == null) reference = "";	//����
		String sending = multi.getParameter("sending");	if(sending == null) sending = "";			//�߽�
		String subject = multi.getParameter("subject");	if(subject == null) subject = "";			//����
		String content = multi.getParameter("content");	if(content == null) content = "";			//����
		String firm_name = multi.getParameter("firm_name");	if(firm_name == null) firm_name = "";	//������
		String representative = multi.getParameter("representative");	if(representative == null) representative = "";	//��ǥ�ڸ�
		
		String module_name = multi.getParameter("module_name");	if(module_name == null) module_name = "";	//�μ������� ������
		String module_add = multi.getParameter("module_add");	if(module_add == null) module_add = "";	//�μ��� �ּ�
		if(module_add.length() == 0) module_add = receive;		//�������������� �Է��Ѵ�.
		
		String mail = multi.getParameter("mail");	if(mail == null) mail = "";					//mail�� ������
		String mail_add = multi.getParameter("mail_add");	if(mail_add == null) mail_add = "";	//���/�̸�;

		//����� ������ root file path
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//ex>C:/tomcat4/webapps/webffice

		//������ id (�ӽ�������繮�� : ���� �� �űԵ������ ó��Ű ����)
		String fname = multi.getParameter("fname");		if(fname == null) fname = "";	//����÷�����ϸ�
		String sname = multi.getParameter("sname");		if(sname == null) sname = "";	//����÷������ �����
		String ftype = multi.getParameter("ftype");		if(ftype == null) ftype = "";	//����÷������ type��
		String fsize = multi.getParameter("fsize");		if(fsize == null) fsize = "";	//����÷������ size��
		String bon_path = multi.getParameter("bon_path");	if(bon_path == null) bon_path = "";	//Ȯ��Path
		String ext = multi.getParameter("ext");	if(ext == null) ext = "";				//÷�����ϻ��� ��ȣ
		String attache_cnt = multi.getParameter("attache_cnt");		if(attache_cnt == null) attache_cnt = "0";	//÷�ΰ��� �ִ���� �̸�
		int atcnt = Integer.parseInt(attache_cnt);

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/****************************
			 * ������ ���/����/����/��� ó���ϱ�
			 ****************************/
			//�系���� �ۼ��ϱ�
			if ("IND_write".equals(mode)){
				com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String rtn = "";
					//�系���� �Է��ϱ�
					rtn = docDAO.inputINDTable(id,user_id,user_name,user_rank,div_id,div_code,code,div_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,subject,firm_name,representative,module_name,module_add,mail,mail_add,content,upload_path);
					//÷������ �Է��ϱ�
					atcnt = docDAO.setAddFile(multi,id,filepath);	//atcnt : ÷������ ����
					if(!rtn.equals("ok")) {		//class_table chief_id�� �μ��� ����� ���°��
						con.rollback();
						con.setAutoCommit(true);
						out.println("<script> alert('������ ������� �μ���["+rtn+"] �� �̵�ϵǾ����ϴ�. �����ڿ��� �����Ͻʽÿ�.'); history.back(-1); </script>");
						out.close();
					}
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//------------------------------
					//��ü List �� �б��ϱ�
					//------------------------------
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_List(login_id,"subject","","1",20);
					request.setAttribute("Data_List", table_list);
					//����������/��ü������
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();			//��ü������
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/ods/InDocument_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�系���� �ۼ���� ����ϱ�
			else if ("IND_app".equals(mode)){
				com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String rtn = "";
					//�系���� �Է��ϱ�
					rtn = docDAO.inputINDTable(id,user_id,user_name,user_rank,div_id,div_code,code,div_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,subject,firm_name,representative,module_name,module_add,mail,mail_add,content,upload_path);
					//÷������ �Է��ϱ�
					atcnt = docDAO.setAddFile(multi,id,filepath);	//atcnt : ÷������ ����
					if(!rtn.equals("ok")) {		//class_table chief_id�� �μ��� ����� ���°��
						con.rollback();
						con.setAutoCommit(true);
						out.println("<script> alert('������ ������� �μ���["+rtn+"] �� �̵�ϵǾ����ϴ�. �����ڿ��� �����Ͻʽÿ�.'); history.back(-1); </script>");
						out.close();
					}
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//------------------------------
					//��� �б��ϱ�
					//------------------------------
					getServletContext().getRequestDispatcher("/gw/approval/module/InDocument_AppDirect.jsp?id="+id).forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//�系���� �����ϱ�
			else if ("IND_modify".equals(mode)){
				com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String rtn = "";
					//�系���� �����ϱ�
					rtn = docDAO.updateINDTable(id,user_id,user_name,user_rank,div_id,div_code,code,div_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,subject,firm_name,representative,module_name,module_add,mail,mail_add,content,upload_path);
					//÷������ �����Է��ϱ�
					atcnt = docDAO.setUpdateFile(multi,id,filepath,fname,sname,ftype,fsize,attache_cnt);	//atcnt : ÷������ ����
					if(!rtn.equals("ok")) {		//class_table chief_id�� �μ��� ����� ���°��
						con.rollback();
						con.setAutoCommit(true);
						out.println("<script> alert('������ ������� �μ���["+rtn+"] �� �̵�ϵǾ����ϴ�. �����ڿ��� �����Ͻʽÿ�.'); history.back(-1); </script>");
						out.close();
					}
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//------------------------------
					//��ü List �� �б��ϱ�
					//------------------------------
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_List(login_id,"subject","","1",20);
					request.setAttribute("Data_List", table_list);
					//����������/��ü������
					int Cpage = docDAO.getCurrentPage();		//����������
					int Tpage = docDAO.getTotalPage();			//��ü������
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/ods/InDocument_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//�系������ ÷������ ���������ϱ�
			else if ("IND_attachD".equals(mode)){
				com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//�系���� �����ϱ�
					docDAO.updateINDTable(id,user_id,user_name,user_rank,div_id,div_code,code,div_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,subject,firm_name,representative,module_name,module_add,mail,mail_add,content,upload_path);
					//÷������ ���� �����ϱ�
					docDAO.deleteAttachFile(id,fname,ftype,fsize,sname,ext,upload_path,bon_path);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//------------------------------
					//��ü List �� �б��ϱ�
					//------------------------------
					ArrayList table_list = new ArrayList();
					table_list = docDAO.getDoc_Read(id);
					request.setAttribute("Data_One", table_list);
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/ods/InDocument_modify.jsp").forward(request,response);
				
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
			//con�Ҹ�
			close(con);
			out.close();
		}
	} //doPost()
}
