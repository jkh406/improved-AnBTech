import com.anbtech.mr.entity.*;
import com.anbtech.mr.db.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class asresultworkServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 15;
	private int max_display_page = 5;

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

		//���� �������� ����� ���̵� ��������
		//������ ����Ǿ��� ��� �α� �������� ���� �̵���Ų��.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	alert('����� ������ �����ϴ�. �ʱ� ȭ������ �̵��մϴ�.\n\n�� �޽����� �Ʒ��� ���� ��쿡 ��Ÿ���ϴ�.\n1.�α��� �� ��ð� �ƹ� �۾��� ���� �ʾƼ� �ڵ� ����� ���\n2.���������� �α������� �ʰ� Ư�� �������� �������Ϸ��� �õ��� ���');");
			out.println("	top.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/new/index.html');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;

		//��� �� ���� ������ �ĸ�����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"ART_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"as_field":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//���� ���� �Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String register_no = Hanguel.toHanguel(request.getParameter("register_no"))==null?"":Hanguel.toHanguel(request.getParameter("register_no"));
		String register_date = Hanguel.toHanguel(request.getParameter("register_date"))==null?"":Hanguel.toHanguel(request.getParameter("register_date"));
		String as_field = Hanguel.toHanguel(request.getParameter("as_field"))==null?"":Hanguel.toHanguel(request.getParameter("as_field"));
		String code = Hanguel.toHanguel(request.getParameter("code"))==null?"":Hanguel.toHanguel(request.getParameter("code"));
		String request_name = Hanguel.toHanguel(request.getParameter("request_name"))==null?"":Hanguel.toHanguel(request.getParameter("request_name"));
		String serial_no = Hanguel.toHanguel(request.getParameter("serial_no"))==null?"":Hanguel.toHanguel(request.getParameter("serial_no"));
		String request_date = Hanguel.toHanguel(request.getParameter("request_date"))==null?"":Hanguel.toHanguel(request.getParameter("request_date"));
		String as_date = Hanguel.toHanguel(request.getParameter("as_date"))==null?"":Hanguel.toHanguel(request.getParameter("as_date"));
		String as_type = Hanguel.toHanguel(request.getParameter("as_type"))==null?"":Hanguel.toHanguel(request.getParameter("as_type"));
		String as_content = Hanguel.toHanguel(request.getParameter("as_content"))==null?"":Hanguel.toHanguel(request.getParameter("as_content"));
		String as_result = Hanguel.toHanguel(request.getParameter("as_result"))==null?"":Hanguel.toHanguel(request.getParameter("as_result"));
		String as_delay = Hanguel.toHanguel(request.getParameter("as_delay"))==null?"":Hanguel.toHanguel(request.getParameter("as_delay"));
		String as_issue = Hanguel.toHanguel(request.getParameter("as_issue"))==null?"":Hanguel.toHanguel(request.getParameter("as_issue"));
		String worker = Hanguel.toHanguel(request.getParameter("worker"))==null?"":Hanguel.toHanguel(request.getParameter("worker"));
		String company_no = Hanguel.toHanguel(request.getParameter("company_no"))==null?"":Hanguel.toHanguel(request.getParameter("company_no"));
		String value_request = Hanguel.toHanguel(request.getParameter("value_request"))==null?"":Hanguel.toHanguel(request.getParameter("value_request"));
		
		//�ٽø����� �Ķ����
		com.anbtech.mr.entity.assupportTable table = new com.anbtech.mr.entity.assupportTable();
		ArrayList para_list = new ArrayList();
			table.setCompanyNo(company_no);			//��ü�ڵ�
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	����LIST ��������
			//--------------------------------------------------------------------
			if("ART_L".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//��ü�ڵ庰 ���� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("WORK_List", table_list);

				//�������� �ٷΰ��� List
				ArrayList page_list = new ArrayList();
				page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", page_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
			}
			//�����̷� ��ϵ� ���뺸��
			else if("ART_V".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//AS������� ����
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);

				//�Էµ� ��������
				ArrayList work_list = new ArrayList();
				work_list = workDAO.getWorkRead(pid);
				request.setAttribute("WORK_List", work_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKview.jsp").forward(request,response);
			}
			//�����̷� ��� �غ�
			else if("ART_WV".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

/*				//login user �⺻���� [��ü�ڵ�,login id,login name ��]
				ArrayList node_list = new ArrayList();
				node_list = workDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);
*/
				//AS������� ����
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKwrite.jsp").forward(request,response);
			}
		
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
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

		//���� �������� ����� ���̵� ��������
		//������ ����Ǿ��� ��� �α� �������� ���� �̵���Ų��.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	alert('����� ������ �����ϴ�. �ʱ� ȭ������ �̵��մϴ�.\n\n�� �޽����� �Ʒ��� ���� ��쿡 ��Ÿ���ϴ�.\n1.�α��� �� ��ð� �ƹ� �۾��� ���� �ʾƼ� �ڵ� ����� ���\n2.���������� �α������� �ʰ� Ư�� �������� �������Ϸ��� �õ��� ���');");
			out.println("	top.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/new/index.html');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;
		
		//��� �� ���� ������ �ĸ�����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"ART_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"as_field":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//���� ���� �Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	
		String register_no = Hanguel.toHanguel(request.getParameter("register_no"))==null?"":Hanguel.toHanguel(request.getParameter("register_no"));
		String register_date = Hanguel.toHanguel(request.getParameter("register_date"))==null?"":Hanguel.toHanguel(request.getParameter("register_date"));
		String as_field = Hanguel.toHanguel(request.getParameter("as_field"))==null?"":Hanguel.toHanguel(request.getParameter("as_field"));
		String code = Hanguel.toHanguel(request.getParameter("code"))==null?"":Hanguel.toHanguel(request.getParameter("code"));
		String request_name = Hanguel.toHanguel(request.getParameter("request_name"))==null?"":Hanguel.toHanguel(request.getParameter("request_name"));
		String serial_no = Hanguel.toHanguel(request.getParameter("serial_no"))==null?"":Hanguel.toHanguel(request.getParameter("serial_no"));
		String request_date = Hanguel.toHanguel(request.getParameter("request_date"))==null?"":Hanguel.toHanguel(request.getParameter("request_date"));
		String as_date = Hanguel.toHanguel(request.getParameter("as_date"))==null?"":Hanguel.toHanguel(request.getParameter("as_date"));
		String as_type = Hanguel.toHanguel(request.getParameter("as_type"))==null?"":Hanguel.toHanguel(request.getParameter("as_type"));
		String as_content = Hanguel.toHanguel(request.getParameter("as_content"))==null?"":Hanguel.toHanguel(request.getParameter("as_content"));
		String as_result = Hanguel.toHanguel(request.getParameter("as_result"))==null?"":Hanguel.toHanguel(request.getParameter("as_result"));
		String as_delay = Hanguel.toHanguel(request.getParameter("as_delay"))==null?"":Hanguel.toHanguel(request.getParameter("as_delay"));
		String as_issue = Hanguel.toHanguel(request.getParameter("as_issue"))==null?"":Hanguel.toHanguel(request.getParameter("as_issue"));
		String worker = Hanguel.toHanguel(request.getParameter("worker"))==null?"":Hanguel.toHanguel(request.getParameter("worker"));
		String company_no = Hanguel.toHanguel(request.getParameter("company_no"))==null?"":Hanguel.toHanguel(request.getParameter("company_no"));
		String value_request = Hanguel.toHanguel(request.getParameter("value_request"))==null?"":Hanguel.toHanguel(request.getParameter("value_request"));
		
		//�ٽø����� �Ķ����
		com.anbtech.mr.entity.assupportTable table = new com.anbtech.mr.entity.assupportTable();
		ArrayList para_list = new ArrayList();
			table.setCompanyNo(company_no);			//��ü�ڵ�
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	����LIST ��������
			//--------------------------------------------------------------------
			//���� ��üLIST
			if("ART_L".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//��ü�ڵ庰 ���� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("WORK_List", table_list);

				//�������� �ٷΰ��� List
				ArrayList page_list = new ArrayList();
				page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", page_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
			}
			//�����̷� ��� �غ�
			else if("ART_WV".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

/*				//login user �⺻���� [��ü�ڵ�,login id,login name ��]
				ArrayList node_list = new ArrayList();
				node_list = workDAO.getPjtNodeRead (pjt_code,node_code);
				request.setAttribute("NODE_List", node_list);
*/
				//AS������� ����
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKwrite.jsp").forward(request,response);
			}
			//�����̷� ����ϱ�
			else if("ART_W".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					workDAO.inputWork(as_field,code,request_name,serial_no,request_date,as_date,as_type,as_content,as_result,as_delay,as_issue,worker,company_no);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//��ü�ڵ庰 ���� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
					request.setAttribute("WORK_List", table_list);

					//�������� �ٷΰ��� List
					ArrayList page_list = new ArrayList();
					page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
					request.setAttribute("PAGE_List", page_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//�����̷� ��ϵ� ���뺸��
			else if("ART_V".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//AS������� ����
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);

				//�Էµ� ��������
				ArrayList work_list = new ArrayList();
				work_list = workDAO.getWorkRead(pid);
				request.setAttribute("WORK_List", work_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKview.jsp").forward(request,response);
			}
			//�����̷� �����ϱ� �غ�
			else if("ART_MV".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				//AS������� ����
				ArrayList field_list = new ArrayList();
				field_list = workDAO.getAsField();
				request.setAttribute("FIELD_List", field_list);

				//�Էµ� ��������
				ArrayList work_list = new ArrayList();
				work_list = workDAO.getWorkRead(pid);
				request.setAttribute("WORK_List", work_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/mr/work/asresult_WKmodify.jsp").forward(request,response);
			}
			//�����̷� �����ϱ�
			else if("ART_M".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					workDAO.updateWork(pid,as_field,serial_no,request_date,as_date,as_type,as_content,as_result,as_delay,as_issue);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//��ü�ڵ庰 ���� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
					request.setAttribute("WORK_List", table_list);

					//�������� �ٷΰ��� List
					ArrayList page_list = new ArrayList();
					page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
					request.setAttribute("PAGE_List", page_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�����̷� �����ϱ�
			else if("ART_D".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					workDAO.deleteWork(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//��ü�ڵ庰 ���� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
					request.setAttribute("WORK_List", table_list);

					//�������� �ٷΰ��� List
					ArrayList page_list = new ArrayList();
					page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
					request.setAttribute("PAGE_List", page_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}

			}
			//���� �� �̸��� ������
			else if("ART_S".equals(mode)){	
				com.anbtech.mr.db.asresultworkDAO workDAO = new com.anbtech.mr.db.asresultworkDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//workDAO.sendMailToDIV(pid);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//��ü�ڵ庰 ���� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = workDAO.getWorkList(company_no,sItem,sWord,page,max_display_cnt);
					request.setAttribute("WORK_List", table_list);

					//�������� �ٷΰ��� List
					ArrayList page_list = new ArrayList();
					page_list = workDAO.getDisplayPage(company_no,sItem,sWord,page,max_display_cnt,max_display_page);
					request.setAttribute("PAGE_List", page_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);
					
					//�б��ϱ�
					getServletContext().getRequestDispatcher("/mr/work/asresult_WKlist.jsp").forward(request,response);
				
				}catch(Exception e){
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
			close(con);
			out.close();
		}
	} //doPost()
}



