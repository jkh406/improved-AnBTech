import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class PurchaseOtherMgrServlet extends HttpServlet {

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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String mid			= request.getParameter("mid");
		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page");
		String searchword	= request.getParameter("searchword");
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");

		if (mode == null) mode				= "";
		if (page == null) page				= "1";
		if (searchword == null) searchword	= "";
		if (searchscope == null) searchscope= "";
		if (category == null) category		= "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);

		String redirectUrl = "";

		//���� �������� ����� ���̵� ��������
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;

		//�����Ƿڽ� �ʿ��� Parameters
		String item_code	= request.getParameter("item_code");

		//������ ���� üũ�� �ʿ��� �Ķ���͵�
		String request_no	= request.getParameter("request_no");
		String request_type	= request.getParameter("request_type");
		String project_code	= request.getParameter("project_code");
		String total_mount	= request.getParameter("total_mount");
	
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////
			// ���õ� ǰ���� �����Ƿ� ��û ȭ��
			////////////////////////////////////////////////////
			if(mode.equals("request_estimate")){
				com.anbtech.pu.business.PurchaseOtherMgrBO puotherBO = new com.anbtech.pu.business.PurchaseOtherMgrBO(con);
				com.anbtech.pu.entity.RequestInfoTable table = new com.anbtech.pu.entity.RequestInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = puotherBO.getEstimateItemList(mode,item_code);
				request.setAttribute("ITEM_LIST",item_list);

				//��û������
				com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
				com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
				userinfo = userDAO.getUserListById(login_id);
				String requester_email	= userinfo.getEmail();
				
				getServletContext().getRequestDispatcher("/pu/other/request_estimate.jsp?email="+requester_email).forward(request,response);
			}

			////////////////////////////////////////////////////
			// ������ ���� üũ ó��
			////////////////////////////////////////////////////
			else if(mode.equals("chk_budget")){
				com.anbtech.psm.business.psmProcessBO psmBO = new com.anbtech.psm.business.psmProcessBO(con);

				String err_message = "";
				
				//���� ������� üũ(1:������,2:����,3:������,4:�Ϸ�,5:����,6���)
				String pjt_stat = psmBO.checkProjectStatus(project_code);
				if(pjt_stat.equals("1") || pjt_stat.equals("4") || pjt_stat.equals("5") || pjt_stat.equals("6")){
					err_message = "�����Ͻ� ������ ���� �������� ������ �ƴմϴ�. ����� �� �����ϴ�.";
				}

				//������ �ܾ� ���ϱ�
				double rest_budget = psmBO.getBalanceMaterial(project_code);
				if(rest_budget < Double.parseDouble(total_mount)){
					err_message = "������ֱݾ��� �����Ͻ� ������ å���� �����ܾ��� �ʰ��մϴ�. ����� �� �����ϴ�.";
					err_message += "\\n(���������ܾ�:" + rest_budget + ",������ֱݾ�:" + Double.parseDouble(total_mount) + ")";
				}

				if(err_message.length() > 0){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('" + err_message + "');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}
				redirectUrl = "../gw/approval/module/pu_PurchaseApp.jsp?mode=request_app_view&request_no="+request_no+"&request_type="+request_type;

			}

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} // doGet()

	/**********************************
	 * post������� �Ѿ���� �� ó��
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/pu/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�
	
		//������������ �� �޾ƿ´�. multi���� ������
		String mode = multi.getParameter("mode");
		String page = multi.getParameter("page");
		String searchword = multi.getParameter("searchword");
		String searchscope = multi.getParameter("searchscope");
		String category = multi.getParameter("category");

		//�������� �Ѿ���ų� null�� �� ���鿡 ���� ó��.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";
			

		String redirectUrl = "";
		//���� �������� ����� ���̵� ��������
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;

		//�����Ƿ� ó���� �ʿ��� Parameters
		String item_code		= multi.getParameter("item_code");
		String sender_email		= multi.getParameter("sender_emial");
		String receiver_email	= multi.getParameter("receiver_email");

		//���ּ� �̸��� �߼۽� �ʿ��� Parameters
		String order_no			= multi.getParameter("order_no");
		String req_message		= multi.getParameter("req_message");
		String receiver_name	= multi.getParameter("receiver_name");
		

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////
			// ǰ������Ƿ� ���Ϲ߼� ó��
			////////////////////////
			if(mode.equals("request_estimate")){
				com.anbtech.pu.business.PurchaseOtherMgrBO puotherBO = new com.anbtech.pu.business.PurchaseOtherMgrBO(con);

				//���Ϻ������� �����
				String content = puotherBO.makeMailContent(item_code);
				//���Ϲ߼� �� ��� �޽��� �ޱ�
				String result = puotherBO.sendEmail(sender_email,receiver_email,content);

				PrintWriter out = response.getWriter();
				out.println("	<script>");
//				out.println("	alert('"+result+"');");
				out.println("	alert('���������� �߼۵Ǿ����ϴ�.');");
//				out.println("	opener.location.reload();");
				out.println("	self.close();");
				out.println("	</script>");
				out.close();
			}

			////////////////////////////////////////////////////
			// ���ּ� �̸��� �߼� ó��
			///////////////////////////////////////////////////
			else if(mode.equals("send_order")){
				com.anbtech.pu.business.PurchaseOtherMgrBO puotherBO = new com.anbtech.pu.business.PurchaseOtherMgrBO(con);

				//���Ϻ������� �����
				String content = puotherBO.makeMailContentforOrderForm(order_no,req_message);
				//���Ϲ߼� �� ��� �޽��� �ޱ�
				String result = puotherBO.sendEmail(sender_email,receiver_email,content);

				PrintWriter out = response.getWriter();
				out.println("	<script>");
//				out.println("	alert('"+result+"');");
				out.println("	alert('���������� �߼۵Ǿ����ϴ�.');");
//				out.println("	opener.location.reload();");
				out.println("	self.close();");
				out.println("	</script>");
				out.close();
			}

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}
