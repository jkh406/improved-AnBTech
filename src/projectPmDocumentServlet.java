import com.anbtech.pjt.entity.*;
import com.anbtech.pjt.db.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class projectPmDocumentServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 10;

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PCO_PJL":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"1":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//������� ���� �Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"0":Hanguel.toHanguel(request.getParameter("pid"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));
		
		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setPjtword(pjtWord);				//��������
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	��������������� LIST��������
			//--------------------------------------------------------------------
			//��������������� LIST�������� 
			if("PDT_L".equals(mode)){	
				com.anbtech.pjt.db.pjtDocumentDAO docDAO = new com.anbtech.pjt.db.pjtDocumentDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//�ش����� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//�ش���� ������� LIST 
				ArrayList doc_list = new ArrayList();
				doc_list = docDAO.getDocumentList(pjt_code);
				request.setAttribute("DOC_List", doc_list);
	
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectDoc_list.jsp").forward(request,response);
			}
			//������Ʈ �ڵ� ã�� : ������Ʈ �ڵ� ����Ҷ�
			else if("PDT_PL".equals(mode)){	
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//�ش����� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectDoc_listPJT.jsp").forward(request,response);
			}
			//������Ʈ Node�ڵ� ã�� : ������Ʈ Node�ڵ� ����Ҷ�
			else if("PDT_NL".equals(mode)){	
				com.anbtech.pjt.db.pjtDocumentDAO docDAO = new com.anbtech.pjt.db.pjtDocumentDAO(con);
/*				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//�ش����� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}
*/
				//�ش���� ������� LIST���� �ش����ڵ�
				ArrayList node_list = new ArrayList();
				node_list = docDAO.getDocumentList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectDoc_listNODE.jsp").forward(request,response);
			}
			
		}catch (Exception e){
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PCO_PJL":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"1":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//������� ���� �Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"0":Hanguel.toHanguel(request.getParameter("pid"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));
		String in_date = Hanguel.toHanguel(request.getParameter("in_date"))==null?"":Hanguel.toHanguel(request.getParameter("in_date"));
		
		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setPjtword(pjtWord);				//��������
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
		para_list.add(table);

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	��������������� LIST��������
			//--------------------------------------------------------------------
			//��������������� LIST�������� 
			if("PDT_L".equals(mode)){	
				com.anbtech.pjt.db.pjtDocumentDAO docDAO = new com.anbtech.pjt.db.pjtDocumentDAO(con);
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//�ش����� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}

				//�ش���� ������� LIST 
				ArrayList doc_list = new ArrayList();
				doc_list = docDAO.getDocumentList(pjt_code);
				request.setAttribute("DOC_List", doc_list);
	
				
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectDoc_list.jsp").forward(request,response);
			}
			//������Ʈ �ڵ� ã�� : ������Ʈ �ڵ� ����Ҷ�
			else if("PDT_PL".equals(mode)){	
				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//�ش����� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}
				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectDoc_listPJT.jsp").forward(request,response);
			}
			//������Ʈ Node�ڵ� ã�� : ������Ʈ Node�ڵ� ����Ҷ�
			else if("PDT_NL".equals(mode)){	
				com.anbtech.pjt.db.pjtDocumentDAO docDAO = new com.anbtech.pjt.db.pjtDocumentDAO(con);
/*				com.anbtech.pjt.db.pjtScheduleDAO schDAO = new com.anbtech.pjt.db.pjtScheduleDAO(con);

				//�ش����� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = schDAO.getProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//�����ڵ� �����׸� ã�Ƴ���
				Iterator pjt_iter = table_list.iterator();
				if(pjt_iter.hasNext()) {
					table = (com.anbtech.pjt.entity.projectTable)pjt_iter.next();
					if(pjt_code.length() == 0) pjt_code = table.getPjtCode();
				}
*/
				//�ش���� ������� LIST���� �ش����ڵ�
				ArrayList node_list = new ArrayList();
				node_list = docDAO.getDocumentList(pjt_code);
				request.setAttribute("NODE_List", node_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectDoc_listNODE.jsp").forward(request,response);
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



