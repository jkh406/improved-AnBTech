import com.anbtech.gw.entity.*;
import com.anbtech.gw.db.*;
import com.anbtech.dbconn.DBConnectionManager;
//import com.anbtech.gw.business.*;
import com.anbtech.text.Hanguel;
import com.anbtech.admin.SessionLib;

import com.oreilly.servlet.MultipartRequest;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ApprovalDetailServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;

	//�Ҹ��� = con�Ҹ�
	public void close(Connection con) throws ServletException {
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

		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//��� �� ���� ������ �ĸ�����
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//������ ����� �Ѿ���� �Ķ����
		String pid = request.getParameter("PID")==null?"":request.getParameter("PID");

		//÷������ �����ÿ� �Ѿ���� �Ķ���͵� (from eleAproval_Rewrite.jsp)
		String file1 = request.getParameter("file1"); if(file1 == null) file1 = ""; //÷��1
		String file2 = request.getParameter("file2"); if(file2 == null) file2 = ""; //÷��2
		String file3 = request.getParameter("file3"); if(file3 == null) file3 = ""; //÷��3

		try {
			// connection ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// �� �׸� �� ����
			//-------------------------------------
			//�̰��� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			if ("APP_ING".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);	
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_ING").forward(request,response);
			}
			//������ : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("ASK_ING".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=ASK_ING").forward(request,response);
				
			}
			//�ݷ��� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("REJ_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=REJ_BOX").forward(request,response);
			}
			//�ӽ������� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("TMP_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=TMP_BOX").forward(request,response);
			}
			//����� ��ü : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_BOX".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();	
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_BOX").forward(request,response);
			}
			//����� �Ϲ� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_GEN".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_GEN").forward(request,response);
			}
			//����� ������ : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_SER".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_SER").forward(request,response);
			}
			//����� ����� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_OUT".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_OUT").forward(request,response);
			}
			//����� �����û�� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_BTR".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_BTR").forward(request,response);
			}
			//����� �ް��� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_HDY".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_HDY").forward(request,response);
			}
			//����� ������û : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_CAR".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_CAR").forward(request,response);
			}
			//����� ���� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_REP".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_REP").forward(request,response);
			}
			//����� ���庸�� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_BRP".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_BRP").forward(request,response);
			}
			//����� ��ȼ� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_DRF".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_DRF").forward(request,response);
			}
			//����� ���Խ�û�� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_CRD".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_CRD").forward(request,response);
			}
			//����� ������ : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_RSN".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_RSN").forward(request,response);
			}
			//����� ������ : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_HLP".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_HLP").forward(request,response);
			}
			//����� ����ٹ���û�� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_OTW".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_OTW").forward(request,response);
			}
			//����� �����Ƿڼ� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_OFF".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_OFF").forward(request,response);
			}
			//����� �������� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_EDU".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_EDU").forward(request,response);
			}
			//����� ���ο� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_AKG".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_AKG").forward(request,response);
			}
			//����� ������� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_TD".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_TD").forward(request,response);
			}
			//����� �������� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_ODT".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_ODT").forward(request,response);
			}
			//����� �系���� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_IDS".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_IDS").forward(request,response);
			}
			//����� ��ܰ��� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_ODS".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_ODS").forward(request,response);
			}
			//����� �ڻ���� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_AST".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_AST").forward(request,response);
			}
			//����� �������� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_EST".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_EST").forward(request,response);
			}
			//����� Ư�ٰ��� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_EWK".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_EWK").forward(request,response);
			}
			//����� BOM���� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_BOM".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_BOM").forward(request,response);
			}
			//����� ���躯����� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_DCM".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_DCM").forward(request,response);
			}
			//����� ���ſ�û���� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_PCR".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_PCR").forward(request,response);
			}
			//����� ���ֿ�û���� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_ODR".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_ODR").forward(request,response);
			}
			//����� �����԰���� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_PWH".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_PWH").forward(request,response);
			}
			//����� ��ǰ������ : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("APP_TGW".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	
				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_TGW").forward(request,response);
			}
			//�뺸�� : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("SEE_BOX".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);	
				
				saveDAO.setReadInform(login_id);					//�뺸���� �������� Ȯ���ϱ�

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=SEE_BOX").forward(request,response);
			}
			//������ : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("DEL_BOX".equals(mode)){
				com.anbtech.gw.db.AppStoreHouseDetailDAO houseDAO = new com.anbtech.gw.db.AppStoreHouseDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = houseDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = houseDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=DEL_BOX").forward(request,response);
				
			}
			//����ϱ�
			else if ("APP_PNT".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);	
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_PNT").forward(request,response);
			}
			//���ù��� LINK���� ����
			else if ("APP_LNK".equals(mode)){
				com.anbtech.gw.db.AppSaveDetailDAO saveDAO = new com.anbtech.gw.db.AppSaveDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = saveDAO.getTable_SavePid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = saveDAO.getTable_line();	
				request.setAttribute("Table_Line",table_line);	
				
				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_view.jsp?PROCESS=APP_LNK").forward(request,response);
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con�Ҹ�
			out.close();
		}
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

		com.anbtech.admin.SessionLib sl;
		sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		String login_id = sl.id;

		//��� �� ���� ������ �ĸ�����
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//������ ����� �Ѿ���� �Ķ����
		String pid = request.getParameter("PID")==null?"":request.getParameter("PID");

		//÷������ �����ÿ� �Ѿ���� �Ķ���͵� (from eleAproval_Rewrite.jsp)
		String file1 = request.getParameter("file1"); if(file1 == null) file1 = ""; //÷��1
		String file2 = request.getParameter("file2"); if(file2 == null) file2 = ""; //÷��2
		String file3 = request.getParameter("file3"); if(file3 == null) file3 = ""; //÷��3

		try {
			// connection ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// �� �׸� �� ����
			//-------------------------------------
			
			//���ڰ��� ���ۼ��ϱ� ����
			if("REW".equals(mode)) {
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/SpaceLink/general_FP_Rewrite.jsp").forward(request,response);
			}

			//���ڰ��� ���ۼ� ÷������ �����ϱ�
			else if("DELFILE".equals(mode)) {
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				//÷������ �����ϱ� (����DB update)
				masterDAO.deleteFile(pid,file1,file2,file3);
		
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/SpaceLink/general_FP_Rewrite.jsp").forward(request,response);
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con�Ҹ�
			out.close();
		}
	}

}