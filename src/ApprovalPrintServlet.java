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

public class ApprovalPrintServlet extends HttpServlet {
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

		//��� �� ���� ������ �ĸ�����
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//������ ����� �Ѿ���� �Ķ����
		String pid = request.getParameter("PID")==null?"":request.getParameter("PID");
		
		//�˻��ÿ� �Ѿ���� �Ķ���͵�
		

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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_ING").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=ASK_ING").forward(request,response);				
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=REJ_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=TMP_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_GEN").forward(request,response);	
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_SER").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=SEE_BOX").forward(request,response);
			}
			//������ : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("DEL_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=DEL_BOX").forward(request,response);
			}
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con�Ҹ�
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

		//��� �� ���� ������ �ĸ�����
		String mode = request.getParameter("mode")==null?"informCnt":request.getParameter("mode");
		String page = request.getParameter("page")==null?"1":request.getParameter("page");

		//������ ����� �Ѿ���� �Ķ����
		String pid = request.getParameter("PID")==null?"":request.getParameter("PID");

		//�˻��ÿ� �Ѿ���� �Ķ���͵�
		

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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_ING").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=ASK_ING").forward(request,response);
				
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=REJ_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=TMP_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("gw/approval/eleApproval_print.jsp?PROCESS=APP_BOX").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_GEN").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=APP_SER").forward(request,response);
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

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=SEE_BOX").forward(request,response);
			}
			//������ : ���ڰ��� ���׸� ��üLIST ���� ��� 
			else if ("DEL_BOX".equals(mode)){
				com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con);
				
				ArrayList table_list = new ArrayList();				//�󼼳���
				table_list = masterDAO.getTable_MasterPid(pid);	
				request.setAttribute("Table_List",table_list);	

				ArrayList table_line = new ArrayList();				//���缱
				table_line = masterDAO.getTable_line();			
				request.setAttribute("Table_Line",table_line);		

				getServletContext().getRequestDispatcher("/gw/approval/eleApproval_print.jsp?PROCESS=DEL_BOX").forward(request,response);
				
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);			//con�Ҹ�
		}
	}
}