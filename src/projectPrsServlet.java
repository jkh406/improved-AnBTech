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

public class projectPrsServlet extends HttpServlet {

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

		//���� �������� ����� ���̵� ��������
		//������ ����Ǿ��� ��� �α� �������� ���� �̵���Ų��.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;
		String login_division = sl.division;

		//��� �� ���� ������ �ĸ�����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PNP_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//���μ��� ���� �Ķ����
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	

		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);		//pjt_code
			table.setPjtName(pjt_name);		//pjt_name
			table.setPjtword(pjtWord);		//��������
			table.setSitem(sItem);			//sItem
			table.setSword(sWord);			//sWord
		para_list.add(table);
		
		//�޴���� �������� �˾ƺ���
		String mgr_mode = Hanguel.toHanguel(request.getParameter("mgr_mode"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_mode"));
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	�޴���� ���� �˻��ϱ�
			//--------------------------------------------------------------------
			com.anbtech.pjt.db.pjtUseMgrDAO mgrDAO = new com.anbtech.pjt.db.pjtUseMgrDAO(con);
			boolean mgr = mgrDAO.getUseMgr(login_id,mgr_mode);
			if(!mgr) {
				out.println("	<script>");
				out.println("	alert('�������� �����ϴ�.');");
				out.println("	parent.location.href('" + com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/pjt/pjtBody.jsp');");
				out.println("	</script>");
				out.close();
			}
			//--------------------------------------------------------------------
			//	�η��������� �ش���� LIST��������
			//--------------------------------------------------------------------
			//�ش�PM�� ������ �����η����� LIST��������
			if("PNP_P".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				//�ش�PM�� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_prs.jsp").forward(request,response);
			}
		
			//--------------------------------------------------------------------
			//	�������� �ش���� ���μ��� LIST��������
			//--------------------------------------------------------------------
			else if("PNP_L".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);
				//�������� List
				ArrayList base_list = new ArrayList();
				base_list = prsDAO.getNodeBaseList(pid);
				request.setAttribute("Base_List", base_list);

				//�ش���� ���μ��� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = prsDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//���⹰ ��ü List
				ArrayList doc_list = new ArrayList();
				doc_list = prsDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//�ش���� ���� ��忡 ��ϵ� ���⹰ ��ü List
				ArrayList save_list = new ArrayList();
				save_list = prsDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp").forward(request,response);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PNP_L":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"S":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//�����Է�/����/���� ������ �ޱ�
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//�����ڵ�
		String doc_code = Hanguel.toHanguel(request.getParameter("doc_code"))==null?"":Hanguel.toHanguel(request.getParameter("doc_code"));			//���⹰ �ڵ�
		String doc_name = Hanguel.toHanguel(request.getParameter("doc_name"))==null?"":Hanguel.toHanguel(request.getParameter("doc_name"));			//���⹰ �̸�
		String parent_node = Hanguel.toHanguel(request.getParameter("parent_node"))==null?"":Hanguel.toHanguel(request.getParameter("parent_node"));//����
		String child_node = Hanguel.toHanguel(request.getParameter("child_node"))==null?"":Hanguel.toHanguel(request.getParameter("child_node"));	//�ڳ��
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));		//����
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));			//���ڳ�� ������ȣ
		
		//���μ��� �����ޱ� [�ڳ�尡 multi�ϰ��]
		String array_cnt = Hanguel.toHanguel(request.getParameter("array_cnt"))==null?"0":Hanguel.toHanguel(request.getParameter("array_cnt"));
		int acnt = Integer.parseInt(array_cnt);
		String[] node = new String[acnt];		//���ޱ� �迭
		String node_code = Hanguel.toHanguel(request.getParameter("node_code"))==null?"":Hanguel.toHanguel(request.getParameter("node_code"));
		//��� �Ķ���� �ޱ� [���μ���,���⹰����]
		StringTokenizer str = new StringTokenizer(node_code,",");
		int n = 0;
		while(str.hasMoreTokens()) {
			node[n] = str.nextToken();
			n++;
			if(n == acnt) break;
		}
		String spid = Hanguel.toHanguel(request.getParameter("spid"))==null?"":Hanguel.toHanguel(request.getParameter("spid"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));

		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);		//pjt_code
			table.setPjtName(pjt_name);		//pjt_code
			table.setPjtword(pjtWord);		//��������
			table.setSitem(sItem);			//sItem
			table.setSword(sWord);			//sWord
		para_list.add(table);

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	�η��������� �ش���� LIST��������
			//--------------------------------------------------------------------
			//�ش�PM�� ������ �����η����� LIST��������
			if("PNP_P".equals(mode)){	
				com.anbtech.pjt.db.pjtMemberDAO manDAO = new com.anbtech.pjt.db.pjtMemberDAO(con);
				//�ش�PM�� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = manDAO.getAllProjectList(login_id,pjtWord,sItem,sWord);
				request.setAttribute("PJT_List", table_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_prs.jsp").forward(request,response);
			}

			//--------------------------------------------------------------------
			//	�������� ������ ���μ��� �����ϱ�
			//--------------------------------------------------------------------
			else if("PNP_L".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);
				//�������� List
				ArrayList base_list = new ArrayList();
				base_list = prsDAO.getNodeBaseList(pid);
				request.setAttribute("Base_List", base_list);

				//�ش���� ���μ��� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = prsDAO.getNodeList();
				request.setAttribute("Data_List", table_list);

				//���⹰ ��ü List
				ArrayList doc_list = new ArrayList();
				doc_list = prsDAO.getNodeDocList();
				request.setAttribute("Doc_List", doc_list);

				//�ش���� ���� ��忡 ��ϵ� ���⹰ ��ü List
				ArrayList save_list = new ArrayList();
				save_list = prsDAO.getSaveDocList();
				request.setAttribute("Save_List", save_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp").forward(request,response);
			}
			//�������� ���μ��� �ű��Է��ϱ�
			else if("PNP_W".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = prsDAO.inputNode(pjt_code,pjt_name,parent_node,node,level_no);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = prsDAO.getNodeBaseList(spid);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = prsDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = prsDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = prsDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp?RD=Reload").forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������� ǥ�����μ���  �����ϱ�
			else if("PNP_D".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = prsDAO.deleteNode(pid,pjt_code,parent_node,node,level_no);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = prsDAO.getNodeBaseList(spid);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = prsDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = prsDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = prsDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp?RD=Reload&comment="+data).forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������� ǥ�����μ��� ���⹰ �������  �ű��Է��ϱ�
			else if("PND_W".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					prsDAO.inputNodeDoc(pjt_code,pjt_name,parent_node,node,level_no);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = prsDAO.getNodeBaseList(spid);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = prsDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = prsDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = prsDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp?RD=").forward(request,response);

				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//�������� ǥ�����μ��� ���⹰ �������  �����ϱ�
			else if("PND_D".equals(mode)){	
				com.anbtech.pjt.db.pjtProcessDAO prsDAO = new com.anbtech.pjt.db.pjtProcessDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					String data = prsDAO.deleteNodeDoc(pid,pjt_code,parent_node,node,level_no);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);
					
					//��ü List �� �б��ϱ�
					//�������� List
					ArrayList base_list = new ArrayList();
					base_list = prsDAO.getNodeBaseList(spid);
					request.setAttribute("Base_List", base_list);

					//���μ��� ��ü List
					ArrayList table_list = new ArrayList();
					table_list = prsDAO.getNodeList();
					request.setAttribute("Data_List", table_list);

					//���⹰ ��ü List
					ArrayList doc_list = new ArrayList();
					doc_list = prsDAO.getNodeDocList();
					request.setAttribute("Doc_List", doc_list);

					//���� ��忡 ��ϵ� ���⹰ ��ü List
					ArrayList save_list = new ArrayList();
					save_list = prsDAO.getSaveDocList();
					request.setAttribute("Save_List", save_list);

					//���� �Ķ���� �ٽø����ϱ�
					request.setAttribute("PARA_List", para_list);

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/pjt/pm/projectPrs_node.jsp?RD=&comment="+data).forward(request,response);

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

