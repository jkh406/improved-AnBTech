import com.anbtech.gw.db.*;
import com.anbtech.gw.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ApprovalProcessServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	
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

		//�⺻�Ķ����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"APP":Hanguel.toHanguel(request.getParameter("mode"));
		String pid = Hanguel.toHanguel(request.getParameter("PID"))==null?"":Hanguel.toHanguel(request.getParameter("PID"));
		String comment = Hanguel.toHanguel(request.getParameter("comment"))==null?"":Hanguel.toHanguel(request.getParameter("comment"));
		String passwd = Hanguel.toHanguel(request.getParameter("passwd"))==null?"":Hanguel.toHanguel(request.getParameter("passwd"));
		
		//���ΰ����� email�� ������� �������� ����Ű ���� ���� Directory �˷��ֱ� (upload + crp )
		String path = Hanguel.toHanguel(request.getParameter("path"))==null?"":Hanguel.toHanguel(request.getParameter("path"));
		String link_id = Hanguel.toHanguel(request.getParameter("link_id"))==null?"":Hanguel.toHanguel(request.getParameter("link_id"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		
		//����� ��й�ȣ ���� ���� ����
		String pass = "OK";	//��й�ȣ ���� ��� ���� ���� (pass ok)
		String chk = "";	//��й�ȣ ���⿩�� �Ǵ��ϱ�
		String step = "";	//APV,APL,APG (����,����,����)

		String ip_addr = request.getRemoteAddr();	//�Խ��ǿ� remote ip address �Ѱ��ֱ����ؼ�
		

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//-------------------------------------
			// ���ڰ��� ��й�ȣ�� ���⸦ ������ ���ΰ� (�����) 
			// (* �ݷ��� �б⸸ �ٸ�)
			//-------------------------------------
			if("PRS".equals(mode) || "PRS_REJ".equals(mode)){
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
			
				//���� ����ܰ� ã��
				step = prsDAO.checkStatus(pid);					//APV,APL,APG (����,����,����)

				//�� �ܰ躰 ��й�ȣ ��������?
				if(step.equals("APL")) chk = prsDAO.applyPwdAPL();				//���δܰ� ��й�ȣ ���⿩��
				else if(step.equals("APV")) chk = prsDAO.applyPwdAPV();			//����ܰ� ��й�ȣ ���⿩��
				else if(step.indexOf("APG") != -1) chk = prsDAO.applyPwdAPG();	//�����ܰ� ��й�ȣ ���⿩��
				else chk = "0";													//APG2, ....10 (������ ���� 2��°���ʹ� �˻����)

				//�Ǵ��ϱ�
				if(chk.equals("1")) pass = "NO";		//��й�ȣ ���� 
				else pass = "OK";						//�����ȣ ���� ����
				
				//��й�ȣ ������ ��й�ȣ�� �³� �ȸ³� �˻��Ͽ� ��й�ȣ ���⿩���Ǵ��ϱ�
				String cpd = "";
				if(passwd.length() != 0) {
					cpd = prsDAO.checkPasswd(login_id,passwd);	
					if(cpd.length() == 0) pass = "NO";
					else pass = "OK";
				}	
				
				//�Ķ���� �����ϱ�
				request.setAttribute("pass",pass);
				request.setAttribute("pid",pid);
				request.setAttribute("link_id",link_id);
				request.setAttribute("app_id",app_id);

				//�б��ϱ�
				if("PRS".equals(mode)) {
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_decision.jsp").forward(request,response);
				} else if("PRS_REJ".equals(mode)){
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_reject.jsp").forward(request,response);
				}
			
			}
			//-------------------------------------
			// ���ڰ��� ��������
			//-------------------------------------
			else if("APP".equals(mode)) {
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//�ش��⺰ ���ڰ��� ����ó��
				com.anbtech.gw.business.ModuleApprovalBO mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);	//������ڰ���ó��

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//���� ����ܰ� ã��
					step = prsDAO.checkStatus(pid);					//APV,APL,APG (����,����,����)

					//�� �ܰ� ó���ϱ�
					if(step.equals("APV")) {		//����ܰ�
						prsSmt.processAPV(pid,comment);
					}
					else if(step.equals("APL")) {	//���δܰ�
						prsSmt.setMailFullPath(path);	//�Ẹ����(������û)
						prsSmt.setIPADDR(ip_addr);		//�Խ��ǿ� ������(��������,�ܺΰ���)
						prsSmt.processAPL(pid,comment);	//�������
					}
					else if(step.equals("APG")) {	//���Ǵܰ�
						prsSmt.processAPG(pid,comment,login_id);
					}
					else if(step.equals("APG2")) prsSmt.eleSerialAgree("APG2",pid,comment);
					else if(step.equals("APG3")) prsSmt.eleSerialAgree("APG3",pid,comment);
					else if(step.equals("APG4")) prsSmt.eleSerialAgree("APG4",pid,comment);
					else if(step.equals("APG5")) prsSmt.eleSerialAgree("APG5",pid,comment);
					else if(step.equals("APG6")) prsSmt.eleSerialAgree("APG6",pid,comment);
					else if(step.equals("APG7")) prsSmt.eleSerialAgree("APG7",pid,comment);
					else if(step.equals("APG8")) prsSmt.eleSerialAgree("APG8",pid,comment);
					else if(step.equals("APG9")) prsSmt.eleSerialAgree("APG9",pid,comment);
					else if(step.equals("APG10")) prsSmt.eleSerialAgree("APG10",pid,comment);

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�Ķ���� �����ϱ�
					request.setAttribute("Reload","R");

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_decision.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		
				}
			}
			//-------------------------------------
			// ���ڰ��� �ݷ�����
			//-------------------------------------
			else if("REJ".equals(mode)){
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//�ش��⺰ ���ڰ��� ����ó��
				com.anbtech.gw.business.ModuleApprovalBO mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);	//������ڰ���ó��

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.setMailFullPath(path);	//�Ẹ����(������û �������)
					prsSmt.processAPR(pid,comment);	//����ݷ�

					//commit ó���ϱ�
					con.commit();
					con.setAutoCommit(true);

					//�Ķ���� �����ϱ�
					request.setAttribute("Reload","R");

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_reject.jsp").forward(request,response);
			
				} catch(Exception e){
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
		}

	} //doGet()

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

		//�⺻�Ķ����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"APP":Hanguel.toHanguel(request.getParameter("mode"));
		String pid = Hanguel.toHanguel(request.getParameter("PID"))==null?"":Hanguel.toHanguel(request.getParameter("PID"));
		String comment = Hanguel.toHanguel(request.getParameter("comment"))==null?"":Hanguel.toHanguel(request.getParameter("comment"));
		String passwd = Hanguel.toHanguel(request.getParameter("passwd"))==null?"":Hanguel.toHanguel(request.getParameter("passwd"));
		
		//���ΰ����� email�� ������� �������� ����Ű ���� ���� Directory �˷��ֱ� (upload + crp )
		String path = Hanguel.toHanguel(request.getParameter("path"))==null?"":Hanguel.toHanguel(request.getParameter("path"));
		String link_id = Hanguel.toHanguel(request.getParameter("link_id"))==null?"":Hanguel.toHanguel(request.getParameter("link_id"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		
		//����� ��й�ȣ ���� ���� ����
		String pass = "OK";	//��й�ȣ ���� ��� ���� ���� (pass ok)
		String chk = "";	//��й�ȣ ���⿩�� �Ǵ��ϱ�
		String step = "";	//APV,APL,APG (����,����,����)

		String ip_addr = request.getRemoteAddr();	//�Խ��ǿ� remote ip address �Ѱ��ֱ����ؼ�
		
		//�뺸�� ������ ó���ϱ�
		String receivers = Hanguel.toHanguel(request.getParameter("receivers"))==null?"":Hanguel.toHanguel(request.getParameter("receivers"));
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//-------------------------------------
			// ���ڰ��� ��й�ȣ�� ���⸦ ������ ���ΰ� (�����) 
			// (* �ݷ��� �б⸸ �ٸ�)
			//-------------------------------------
			if("PRS".equals(mode) || "PRS_REJ".equals(mode)){
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
			
				//���� ����ܰ� ã��
				step = prsDAO.checkStatus(pid);					//APV,APL,APG (����,����,����)

				//�� �ܰ躰 ��й�ȣ ��������?
				if(step.equals("APL")) chk = prsDAO.applyPwdAPL();				//���δܰ� ��й�ȣ ���⿩��
				else if(step.equals("APV")) chk = prsDAO.applyPwdAPV();			//����ܰ� ��й�ȣ ���⿩��
				else if(step.indexOf("APG") != -1) chk = prsDAO.applyPwdAPG();	//�����ܰ� ��й�ȣ ���⿩��
				else chk = "0";													//APG2, ....10 (������ ���� 2��°���ʹ� �˻����)

				//�Ǵ��ϱ�
				if(chk.equals("1")) pass = "NO";		//��й�ȣ ���� 
				else pass = "OK";						//�����ȣ ���� ����
				
				//��й�ȣ ������ ��й�ȣ�� �³� �ȸ³� �˻��Ͽ� ��й�ȣ ���⿩���Ǵ��ϱ�
				String cpd = "";
				if(passwd.length() != 0) {
					cpd = prsDAO.checkPasswd(login_id,passwd);	
					if(cpd.length() == 0) pass = "NO";
					else pass = "OK";
				}	
				
				//�Ķ���� �����ϱ�
				request.setAttribute("pass",pass);
				request.setAttribute("pid",pid);
				request.setAttribute("link_id",link_id);
				request.setAttribute("app_id",app_id);

				//�б��ϱ�
				if("PRS".equals(mode)) {
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_decision.jsp").forward(request,response);
				} else if("PRS_REJ".equals(mode)){
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_reject.jsp").forward(request,response);
				}
			
			}
			//-------------------------------------
			// ���ڰ��� ��������
			//-------------------------------------
			else if("APP".equals(mode)) {
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//�ش��⺰ ���ڰ��� ����ó��
				com.anbtech.gw.business.ModuleApprovalBO mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);	//������ڰ���ó��

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//���� ����ܰ� ã��
					step = prsDAO.checkStatus(pid);					//APV,APL,APG (����,����,����)

					//�� �ܰ� ó���ϱ�
					if(step.equals("APV")) {		//����ܰ�
						prsSmt.processAPV(pid,comment);
					}
					else if(step.equals("APL")) {	//���δܰ�
						prsSmt.setMailFullPath(path);	//�Ẹ����(������û)
						prsSmt.setIPADDR(ip_addr);		//�Խ��ǿ� ������(��������,�ܺΰ���)
						prsSmt.processAPL(pid,comment);	//�������
					}
					else if(step.equals("APG")) {	//���Ǵܰ�
						prsSmt.processAPG(pid,comment,login_id);
					}
					else if(step.equals("APG2")) prsSmt.eleSerialAgree("APG2",pid,comment);
					else if(step.equals("APG3")) prsSmt.eleSerialAgree("APG3",pid,comment);
					else if(step.equals("APG4")) prsSmt.eleSerialAgree("APG4",pid,comment);
					else if(step.equals("APG5")) prsSmt.eleSerialAgree("APG5",pid,comment);
					else if(step.equals("APG6")) prsSmt.eleSerialAgree("APG6",pid,comment);
					else if(step.equals("APG7")) prsSmt.eleSerialAgree("APG7",pid,comment);
					else if(step.equals("APG8")) prsSmt.eleSerialAgree("APG8",pid,comment);
					else if(step.equals("APG9")) prsSmt.eleSerialAgree("APG9",pid,comment);
					else if(step.equals("APG10")) prsSmt.eleSerialAgree("APG10",pid,comment);

					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�Ķ���� �����ϱ�
					request.setAttribute("Reload","R");

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_decision.jsp").forward(request,response);
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		
				}
			}
			//-------------------------------------
			// ���ڰ��� �ݷ�����
			//-------------------------------------
			else if("REJ".equals(mode)){
				com.anbtech.gw.db.AppCheckProcessDAO prsDAO = new com.anbtech.gw.db.AppCheckProcessDAO(con);
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//�ش��⺰ ���ڰ��� ����ó��
				com.anbtech.gw.business.ModuleApprovalBO mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);	//������ڰ���ó��

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.setMailFullPath(path);	//�Ẹ����(������û �������)
					prsSmt.processAPR(pid,comment);	//����ݷ�

					//commit ó���ϱ�
					con.commit();
					con.setAutoCommit(true);

					//�Ķ���� �����ϱ�
					request.setAttribute("Reload","R");

					//�б��ϱ�
					getServletContext().getRequestDispatcher("/gw/approval/eleApproval_reject.jsp").forward(request,response);
			
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//-------------------------------------
			// ���ڰ��� �ӽ����� �� �ݷ����� ��� �������
			//-------------------------------------
			else if("APP_DELETE".equals(mode)){
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//�ش��⺰ ���ڰ��� ����ó��
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.appDelete(pid);

					//commit ó���ϱ�
					con.commit();
					con.setAutoCommit(true);

					//�б��ϱ�
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//-------------------------------------
			// ���ڰ��� �Ϸ��� �뺸�� �߰� �����ϱ�
			//-------------------------------------
			else if("RE_API".equals(mode)){
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//�ش��⺰ ���ڰ��� ����ó��
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.addInformReceiver(pid,receivers);	//�뺸�� �߰�����
				
					//commit ó���ϱ�
					con.commit();
					con.setAutoCommit(true);

					//�б��ϱ�
					out.println("<script language=javascript>");
					//out.println("location.href ='ApprovalDetailServlet?mode=APP_BOX&PID="+pid+"'");
					out.println("top.opener.location.href ='ApprovalMenuServlet?mode=APP_BOX'");
					out.println("top.close();");
					out.println("</script>");
					
				} catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//-------------------------------------
			// ���ڰ��� �������ϱ�
			//-------------------------------------
			else if("APP_CANCEL".equals(mode)){
				com.anbtech.gw.db.AppProcessSubmitDAO prsSmt = new com.anbtech.gw.db.AppProcessSubmitDAO(con);		//�ش��⺰ ���ڰ��� ����ó��
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				try{
					prsSmt.appCancel(pid);

					//commit ó���ϱ�
					con.commit();
					con.setAutoCommit(true);

					//�б��ϱ�
					out.println("	<script>");
					out.println("	parent.location.href('../gw/approval/ApprovalBody.jsp?mode=TMP_BOX');");
					out.println("	</script>");
					
				} catch(Exception e){
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
		}
	} //doPost()
}