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

public class projectStaffServlet extends HttpServlet {

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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"PSM_PL":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));

		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pjt_name":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		
		//���μ��� ���� �Ķ����
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String parent_node = Hanguel.toHanguel(request.getParameter("parent_node"))==null?"0":Hanguel.toHanguel(request.getParameter("parent_node"));
		String child_node = Hanguel.toHanguel(request.getParameter("child_node"))==null?"0":Hanguel.toHanguel(request.getParameter("child_node"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"0":Hanguel.toHanguel(request.getParameter("level_no"));
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));	

		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setPjtword(pjtWord);				//��������
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
			table.setParentNode(parent_node);		//parent_node
			table.setChildNode(child_node);			//child_node
		para_list.add(table);
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//--------------------------------------------------------------------
			//	�ڽ��� ���� ��ü ����LIST�� �������� ������ ��ô����
			//--------------------------------------------------------------------
			//����LIST
			if("PSM_PL".equals(mode)){	
				com.anbtech.pjt.db.pjtStaffDAO stfDAO = new com.anbtech.pjt.db.pjtStaffDAO(con);

				//�ڽ��� ������ �������� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = stfDAO.getPrsGeneralList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("PJT_List", table_list);

				//����������/��ü������
				int Cpage = stfDAO.getCurrentPage();	//����������
				int Tpage = stfDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffPrs_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//--------------------------------------------------------------------
			//	�ڽ��� ���� ��ü ����LIST�� �̿��� ������ȸ
			//--------------------------------------------------------------------
			//����LIST
			else if("PSM_BL".equals(mode)){	
				com.anbtech.pjt.db.pjtStaffDAO stfDAO = new com.anbtech.pjt.db.pjtStaffDAO(con);
				//�ڽż��� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = stfDAO.getAllGeneralList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("PJT_List", table_list);

				//����������/��ü������
				int Cpage = stfDAO.getCurrentPage();	//����������
				int Tpage = stfDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffView_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
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
		String pjtWord = Hanguel.toHanguel(request.getParameter("pjtWord"))==null?"0":Hanguel.toHanguel(request.getParameter("pjtWord"));
		//out.println("<script> alert('"+mode+":"+page+":"+sItem+":"+sWord+"'); </script>");

		//�����Է�/����/���� ������ �ޱ�
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));						//�����ڵ�
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String parent_node = Hanguel.toHanguel(request.getParameter("parent_node"))==null?"0":Hanguel.toHanguel(request.getParameter("parent_node"));
		String child_node = Hanguel.toHanguel(request.getParameter("child_node"))==null?"0":Hanguel.toHanguel(request.getParameter("child_node"));	
		String node_name = Hanguel.toHanguel(request.getParameter("node_name"))==null?"":Hanguel.toHanguel(request.getParameter("node_name"));		
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"0":Hanguel.toHanguel(request.getParameter("level_no"));			
		String user_id = Hanguel.toHanguel(request.getParameter("user_id"))==null?"":Hanguel.toHanguel(request.getParameter("user_id"));			
		String user_name = Hanguel.toHanguel(request.getParameter("user_name"))==null?"":Hanguel.toHanguel(request.getParameter("user_name"));			
		String pjt_node_mbr = Hanguel.toHanguel(request.getParameter("pjt_node_mbr"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_node_mbr"));			
		String plan_start_date = Hanguel.toHanguel(request.getParameter("plan_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_start_date"));			
		String plan_end_date = Hanguel.toHanguel(request.getParameter("plan_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("plan_end_date"));			
		String chg_start_date = Hanguel.toHanguel(request.getParameter("chg_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("chg_start_date"));			
		String chg_end_date = Hanguel.toHanguel(request.getParameter("chg_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("chg_end_date"));			
		String rst_start_date = Hanguel.toHanguel(request.getParameter("rst_start_date"))==null?"":Hanguel.toHanguel(request.getParameter("rst_start_date"));			
		String rst_end_date = Hanguel.toHanguel(request.getParameter("rst_end_date"))==null?"":Hanguel.toHanguel(request.getParameter("rst_end_date"));			
		String plan_cnt = Hanguel.toHanguel(request.getParameter("plan_cnt"))==null?"":Hanguel.toHanguel(request.getParameter("plan_cnt"));			
		String chg_cnt = Hanguel.toHanguel(request.getParameter("chg_cnt"))==null?"":Hanguel.toHanguel(request.getParameter("chg_cnt"));			
		String result_cnt = Hanguel.toHanguel(request.getParameter("result_cnt"))==null?"":Hanguel.toHanguel(request.getParameter("result_cnt"));			
		String node_status = Hanguel.toHanguel(request.getParameter("node_status"))==null?"":Hanguel.toHanguel(request.getParameter("node_status"));			
		String remark = Hanguel.toHanguel(request.getParameter("remark"))==null?"":Hanguel.toHanguel(request.getParameter("remark"));			
		
		String spid = Hanguel.toHanguel(request.getParameter("spid"))==null?"":Hanguel.toHanguel(request.getParameter("spid"));
		
		//�ٽø����� �Ķ����
		com.anbtech.pjt.entity.projectTable table = new com.anbtech.pjt.entity.projectTable();
		ArrayList para_list = new ArrayList();
			table.setPjtCode(pjt_code);				//pjt_code
			table.setPjtName(pjt_name);				//pjt_name
			table.setPjtword(pjtWord);				//��������
			table.setSitem(sItem);					//sItem
			table.setSword(sWord);					//sWord
			table.setParentNode(parent_node);		//parent_node
			table.setChildNode(child_node);			//child_node
		para_list.add(table);

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//--------------------------------------------------------------------
			//	�ڽ��� ���� ��ü ����LIST�� �������� ������ ��ô����
			//--------------------------------------------------------------------
			//����LIST
			if("PSM_PL".equals(mode)){	
				com.anbtech.pjt.db.pjtStaffDAO stfDAO = new com.anbtech.pjt.db.pjtStaffDAO(con);

				//�ڽ��� ������ �������� ��ü List
				ArrayList table_list = new ArrayList();
				table_list = stfDAO.getPrsGeneralList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("PJT_List", table_list);

				//����������/��ü������
				int Cpage = stfDAO.getCurrentPage();	//����������
				int Tpage = stfDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffPrs_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
			}
			//������ ������ �������������� : ��ô���� �غ�
			else if("PSM_PV".equals(mode)){	
				com.anbtech.pjt.db.pjtStaffDAO stfDAO = new com.anbtech.pjt.db.pjtStaffDAO(con);

				//������ ���� ��ô��������
				ArrayList sch_list = new ArrayList();
				sch_list = stfDAO.getPjtSchedule(pjt_code,level_no,parent_node,login_id);
				request.setAttribute("SCH_List", sch_list);

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffPrs_main.jsp").forward(request,response);
			}
			//--------------------------------------------------------------------
			//	�ڽ��� ���� ��ü ����LIST�� �̿��� ������ȸ
			//--------------------------------------------------------------------
			//����LIST
			else if("PSM_BL".equals(mode)){	
				com.anbtech.pjt.db.pjtStaffDAO stfDAO = new com.anbtech.pjt.db.pjtStaffDAO(con);

				//�ڽ��� ������ü List
				ArrayList table_list = new ArrayList();
				table_list = stfDAO.getAllGeneralList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("PJT_List", table_list);

				//����������/��ü������
				int Cpage = stfDAO.getCurrentPage();	//����������
				int Tpage = stfDAO.getTotalPage();		//��ü������

				//���� �Ķ���� �ٽø����ϱ�
				request.setAttribute("PARA_List", para_list);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/pjt/staff/projectStaffView_list.jsp?Cpage="+Cpage+"&Tpage="+Tpage).forward(request,response);
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



