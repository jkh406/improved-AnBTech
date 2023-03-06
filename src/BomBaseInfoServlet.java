import com.anbtech.bm.db.*;
import com.anbtech.bm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class BomBaseInfoServlet extends HttpServlet {
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

		//Session
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			//PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//�⺻�Ķ����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"info_list":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MBOM MASTER������ ���� �⺻�Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String modelg_code = Hanguel.toHanguel(request.getParameter("modelg_code"))==null?"":Hanguel.toHanguel(request.getParameter("modelg_code"));
		String modelg_name = Hanguel.toHanguel(request.getParameter("modelg_name"))==null?"":Hanguel.toHanguel(request.getParameter("modelg_name"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String fg_spec = Hanguel.toHanguel(request.getParameter("fg_spec"))==null?"":Hanguel.toHanguel(request.getParameter("fg_spec"));
		String pdg_code = Hanguel.toHanguel(request.getParameter("pdg_code"))==null?"":Hanguel.toHanguel(request.getParameter("pdg_code"));
		String pdg_name = Hanguel.toHanguel(request.getParameter("pdg_name"))==null?"":Hanguel.toHanguel(request.getParameter("pdg_name"));
		String pd_code = Hanguel.toHanguel(request.getParameter("pd_code"))==null?"":Hanguel.toHanguel(request.getParameter("pd_code"));
		String pd_name = Hanguel.toHanguel(request.getParameter("pd_name"))==null?"":Hanguel.toHanguel(request.getParameter("pd_name"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String reg_id = Hanguel.toHanguel(request.getParameter("reg_id"))==null?"":Hanguel.toHanguel(request.getParameter("reg_id"));
		String reg_name = Hanguel.toHanguel(request.getParameter("reg_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_name"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		String app_name = Hanguel.toHanguel(request.getParameter("app_name"))==null?"":Hanguel.toHanguel(request.getParameter("app_name"));
		String app_date = Hanguel.toHanguel(request.getParameter("app_date"))==null?"":Hanguel.toHanguel(request.getParameter("app_date"));
		String bom_status = Hanguel.toHanguel(request.getParameter("bom_status"))==null?"":Hanguel.toHanguel(request.getParameter("bom_status"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String m_status = Hanguel.toHanguel(request.getParameter("m_status"))==null?"":Hanguel.toHanguel(request.getParameter("m_status"));
		String purpose = Hanguel.toHanguel(request.getParameter("purpose"))==null?"":Hanguel.toHanguel(request.getParameter("purpose"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MBOM�⺻���� ���/����/����/List ó���ϱ�
			//------------------------------------------------------------
			//MBOM MASTER ���/���� �غ�
			if ("info_prewrite".equals(mode) || "info_premodify".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//�Է�/������ ������ �б�
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(pid);
				request.setAttribute("ITEM_List", masterT);

				//�������� �ִ��� �Ǵ��ϱ�
				msg = modDAO.getFgGrade(login_id,pid);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/baseInfoReg.jsp").forward(request,response);
			
			}
			//MBOM MASTER ��ȸ
			else if ("info_list".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//��ϵ� LIST ����
				ArrayList master_list = new ArrayList();
				master_list = modDAO.getMasterList(sItem,sWord,page,max_display_cnt);
				request.setAttribute("MASTER_List", master_list); 

				//�������� �ٷΰ��� List
				com.anbtech.bm.entity.mbomMasterTable pageL = new com.anbtech.bm.entity.mbomMasterTable();
				pageL = modDAO.getDisplayPage(sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/baseInfoList.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		�ӽ� BOM �����ϱ� : ����Ȯ��/�������
			//------------------------------------------------------------
			//MBOM MASTER �ӽ�BOM ��ȸ
			else if ("tbom_list".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//��ϵ� LIST ����
				ArrayList master_list = new ArrayList();
				master_list = modDAO.getTmpBomMasterList(sItem,sWord);
				request.setAttribute("MASTER_List", master_list); 

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/tbomInfoList.jsp").forward(request,response);
			}
			//MBOM MASTER ����Ȯ��/������� �غ�
			else if ("tbom_preapp".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//����Ȯ��/������� �� ������ �б�
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(pid);
				request.setAttribute("ITEM_List", masterT);

				//�������� �ִ��� �Ǵ��ϱ�
				msg = modDAO.getTbomMgrGrade(login_id);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/tbomInfoApp.jsp").forward(request,response);
			}
			//�ӽ�BOM PART LIST ��ϳ��� �󼼺���
			else if ("tbom_pl".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);

				//�ӽ�BOM PART LIST ������ �б�
				ArrayList str_list = new ArrayList();
				str_list = inputBO.getTbomStrList(pid,"0",model_code);
				request.setAttribute("STR_List", str_list); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/tbomPartList.jsp?model_code="+model_code).forward(request,response);
			}
			//------------------------------------------------------------
			//		BOM ����Ҷ� �ʿ��� ������ �����
			//------------------------------------------------------------
			//������ �غ��ϱ�
			else if ("info_preapp".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				com.anbtech.bm.business.BomApprovalBO appBO = new com.anbtech.bm.business.BomApprovalBO(con);
				
				//MBOM MASTER ������ �б�
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(pid);
				request.setAttribute("ITEM_List", masterT);

				//1������ BOM�� ���� �����Ǿ��� �˻��ϱ�
				msg += appBO.checkAssySet(pid);

				//BOM�˻��ϱ� : Phantom Assy���� �˻�
				msg += appBO.checkPhantomAssy(pid);

				//�����ϱ�
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/baseInfoApp.jsp").forward(request,response);
			}
			//MBOM BOM���� ����
			else if ("info_bomview".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				
				//MBOM STR ������ �б�
				ArrayList bom_list = new ArrayList();
				bom_list = modDAO.getForwardItems(pid,"0",model_code);
				request.setAttribute("BOM_List", bom_list); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/baseInfoAppBomView.jsp?model_code="+model_code).forward(request,response);
			}
		}catch (Exception e){
			//������� �������� �б�
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
			//PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//�⺻�Ķ����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"info_list":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MBOM MASTER������ ���� �⺻�Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String modelg_code = Hanguel.toHanguel(request.getParameter("modelg_code"))==null?"":Hanguel.toHanguel(request.getParameter("modelg_code"));
		String modelg_name = Hanguel.toHanguel(request.getParameter("modelg_name"))==null?"":Hanguel.toHanguel(request.getParameter("modelg_name"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String fg_spec = Hanguel.toHanguel(request.getParameter("fg_spec"))==null?"":Hanguel.toHanguel(request.getParameter("fg_spec"));
		String pdg_code = Hanguel.toHanguel(request.getParameter("pdg_code"))==null?"":Hanguel.toHanguel(request.getParameter("pdg_code"));
		String pdg_name = Hanguel.toHanguel(request.getParameter("pdg_name"))==null?"":Hanguel.toHanguel(request.getParameter("pdg_name"));
		String pd_code = Hanguel.toHanguel(request.getParameter("pd_code"))==null?"":Hanguel.toHanguel(request.getParameter("pd_code"));
		String pd_name = Hanguel.toHanguel(request.getParameter("pd_name"))==null?"":Hanguel.toHanguel(request.getParameter("pd_name"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String reg_id = Hanguel.toHanguel(request.getParameter("reg_id"))==null?"":Hanguel.toHanguel(request.getParameter("reg_id"));
		String reg_name = Hanguel.toHanguel(request.getParameter("reg_name"))==null?"":Hanguel.toHanguel(request.getParameter("reg_name"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String app_id = Hanguel.toHanguel(request.getParameter("app_id"))==null?"":Hanguel.toHanguel(request.getParameter("app_id"));
		String app_name = Hanguel.toHanguel(request.getParameter("app_name"))==null?"":Hanguel.toHanguel(request.getParameter("app_name"));
		String app_date = Hanguel.toHanguel(request.getParameter("app_date"))==null?"":Hanguel.toHanguel(request.getParameter("app_date"));
		String bom_status = Hanguel.toHanguel(request.getParameter("bom_status"))==null?"":Hanguel.toHanguel(request.getParameter("bom_status"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String m_status = Hanguel.toHanguel(request.getParameter("m_status"))==null?"":Hanguel.toHanguel(request.getParameter("m_status"));
		String purpose = Hanguel.toHanguel(request.getParameter("purpose"))==null?"":Hanguel.toHanguel(request.getParameter("purpose"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//------------------------------------------------------------
			//		MBOM�⺻���� ���/����/����/List ó���ϱ�
			//------------------------------------------------------------
			//MBOM MASTER ���/���� �غ�
			if ("info_prewrite".equals(mode) || "info_premodify".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//�Է�/������ ������ �б�
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(pid);
				request.setAttribute("ITEM_List", masterT);

				//�������� �ִ��� �Ǵ��ϱ�
				msg = modDAO.getFgGrade(login_id,pid);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/baseInfoReg.jsp").forward(request,response);
			
			}
			//MBOM MASTER ��� / ���� / ����
			else if("info_write".equals(mode) || "info_modify".equals(mode) || "info_delete".equals(mode)) {
				com.anbtech.bm.business.BomBaseInfoBO infoBO = new com.anbtech.bm.business.BomBaseInfoBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//����ϱ�
					if("info_write".equals(mode)) {
						msg = infoBO.insertMaster(pid,modelg_code,modelg_name,model_code,model_name,fg_code,fg_spec,pdg_code,pdg_name,pd_code,pd_name,pjt_code,pjt_name,reg_id,reg_name,reg_date,purpose);
					} 
					//�����ϱ�
					else if ("info_modify".equals(mode)) {
						msg = infoBO.updateMaster(pid,modelg_code,modelg_name,model_code,model_name,fg_code,fg_spec,pdg_code,pdg_name,pd_code,pd_name,pjt_code,pjt_name,reg_date,purpose);
					}
					//�����ϱ�
					else if("info_delete".equals(mode)) {
						msg = infoBO.deleteMaster(pid);
					}
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					out.println("	<script>");
					out.println("	alert('"+msg+"');");
					out.println("	</script>");

					//�б��ϱ�
					out.println("	<script>");
					out.println("	self.location.href('BomBaseInfoServlet?mode=info_list');");
					out.println("	</script>");
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//MBOM MASTER ��ȸ
			else if ("info_list".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//��ϵ� LIST ����
				ArrayList master_list = new ArrayList();
				master_list = modDAO.getMasterList(sItem,sWord,page,max_display_cnt);
				request.setAttribute("MASTER_List", master_list); 

				//�������� �ٷΰ��� List
				com.anbtech.bm.entity.mbomMasterTable pageL = new com.anbtech.bm.entity.mbomMasterTable();
				pageL = modDAO.getDisplayPage(sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/baseInfoList.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		�ӽ� BOM �����ϱ� : ����Ȯ��/�������
			//------------------------------------------------------------
			//MBOM MASTER �ӽ�BOM ��ȸ
			else if ("tbom_list".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//��ϵ� LIST ����
				ArrayList master_list = new ArrayList();
				master_list = modDAO.getTmpBomMasterList(sItem,sWord);
				request.setAttribute("MASTER_List", master_list); 

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/tbomInfoList.jsp").forward(request,response);
			}
			//MBOM MASTER ����Ȯ��/������� �غ�
			else if ("tbom_preapp".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//����Ȯ��/������� �� ������ �б�
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(pid);
				request.setAttribute("ITEM_List", masterT);

				//�������� �ִ��� �Ǵ��ϱ�
				msg = modDAO.getTbomMgrGrade(login_id);
				request.setAttribute("msg",msg);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/master/tbomInfoApp.jsp").forward(request,response);
			}
			//MBOM MASTER ����Ȯ��/������� �����ϱ�
			else if ("tbom_approval".equals(mode) || "tbom_cancel".equals(mode)){
				com.anbtech.bm.db.BomApprovalDAO appDAO = new com.anbtech.bm.db.BomApprovalDAO(con);

				//����Ȯ��/������� �����ϱ�
				appDAO.setBomStatus(pid,bom_status,app_id,app_name,app_no);

				//�б��ϱ�
				out.println("	<script>");
				out.println("	self.location.href('BomBaseInfoServlet?mode=tbom_list');");
				out.println("	</script>");
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}

