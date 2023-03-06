import com.anbtech.bm.db.*;
import com.anbtech.bm.business.*;
import com.anbtech.dcm.db.*;
import com.anbtech.dcm.business.*;
import com.anbtech.text.Hanguel;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class CbomChangeServlet extends HttpServlet {
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
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;							
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//�⺻�Ķ����
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"pl_list":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MBOM STR������ ���� �⺻�Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String parent_code = Hanguel.toHanguel(request.getParameter("parent_code"))==null?"":Hanguel.toHanguel(request.getParameter("parent_code"));
		String child_code = Hanguel.toHanguel(request.getParameter("child_code"))==null?"":Hanguel.toHanguel(request.getParameter("child_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
		String part_name = Hanguel.toHanguel(request.getParameter("part_name"))==null?"":Hanguel.toHanguel(request.getParameter("part_name"));
		String part_spec = Hanguel.toHanguel(request.getParameter("part_spec"))==null?"":Hanguel.toHanguel(request.getParameter("part_spec"));
		String location = Hanguel.toHanguel(request.getParameter("location"))==null?"":Hanguel.toHanguel(request.getParameter("location"));
		String op_code = Hanguel.toHanguel(request.getParameter("op_code"))==null?"":Hanguel.toHanguel(request.getParameter("op_code"));
		String qty_unit = Hanguel.toHanguel(request.getParameter("qty_unit"))==null?"":Hanguel.toHanguel(request.getParameter("qty_unit"));
		String qty = Hanguel.toHanguel(request.getParameter("qty"))==null?"":Hanguel.toHanguel(request.getParameter("qty"));
		String price_unit = Hanguel.toHanguel(request.getParameter("price_unit"))==null?"":Hanguel.toHanguel(request.getParameter("price_unit"));
		String price = Hanguel.toHanguel(request.getParameter("price"))==null?"":Hanguel.toHanguel(request.getParameter("price"));
		String adtag = Hanguel.toHanguel(request.getParameter("adtag"))==null?"":Hanguel.toHanguel(request.getParameter("adtag"));
		String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		String url = Hanguel.toHanguel(request.getParameter("url"))==null?"":Hanguel.toHanguel(request.getParameter("url"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		���� ���� ó���ϱ�
			//------------------------------------------------------------
			//���躯���� BOM STR ��ȸ
			if ("eco_bomlist".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				com.anbtech.dcm.db.CbomChangeDAO chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);

				//�����ڵ�[GID]�� ���ϱ�
				String where = "";
				if(gid.length() == 0) {
					where = "where fg_code='"+fg_code+"'";
					gid = chgDAO.getColumData("mbom_master","pid",where);
					parent_code = chgDAO.getColumData("mbom_master","model_code",where);
				}

				//���躯���� BOM ����ü ��ȸ
				ArrayList bom_list = new ArrayList();
				bom_list = chgBO.viewCbomStrList(gid,"0",parent_code);
				request.setAttribute("BOM_List", bom_list); 

				//����� ���� �����ϱ� : �����ǰ�� ���� ��µ� ��������� + �߻��� ����
				ArrayList model_list = new ArrayList();
				model_list = chgBO.targetModelList(eco_no);
				request.setAttribute("MODEL_List", model_list); 

				//���ڵ� ���� �����ϱ�
				where = "where fg_code='"+fg_code+"'";
				String model_code = chgDAO.getColumData("mbom_master","model_code",where);
				request.setAttribute("model_code",model_code);

				//ECO NO���� �� ��ü������ȣ ���� ����
				request.setAttribute("eco_no",eco_no);
				request.setAttribute("fg_code",fg_code);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/CbomEcoSearch.jsp").forward(request,response);
				
			}
			//������ ǰ���ڵ� �Է�â ����
			else if("eco_prechg".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				com.anbtech.dcm.db.CbomChangeDAO chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//�����ڵ�[GID]�� ���ϱ�
				String where = "";
				if(gid.length() == 0) {
					where = "where fg_code='"+fg_code+"'";
					gid = chgDAO.getColumData("mbom_master","pid",where);
				}

				//�ش�ǰ���ڵ� ����
				com.anbtech.bm.entity.mbomStrTable item_info = new com.anbtech.bm.entity.mbomStrTable();
				item_info = chgDAO.readStrItem(pid);
				request.setAttribute("ITEM_Info", item_info); 

				//�����[FG�ڵ�]�� ������ ���ϱ�
				where = "where eco_no='"+eco_no+"'";
				String order_date = chgDAO.getColumData("ecc_com","order_date",where);
				request.setAttribute("order_date",order_date);

				//�����[FG�ڵ�]�� �������� ���ϱ�
				where = "where eco_no='"+eco_no+"'";
				String ecc_reason = chgDAO.getColumData("ecc_com","ecc_reason",where);
				request.setAttribute("ecc_reason",ecc_reason);

				//���躯�� �����׸� ������ �б�
				ArrayList ecr_list = new ArrayList();
				ecr_list = cmodDAO.getEccItem("F01");			//[F01:��������]
				request.setAttribute("ECR_List", ecr_list); 

				//ECO NO���� �� ��ü������ȣ ���� ����
				request.setAttribute("eco_no",eco_no);
				request.setAttribute("fg_code",fg_code);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/CbomEcoChange.jsp").forward(request,response);
				
			}
			//����� ǰ�� ���â ����
			else if("eco_chglist".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				com.anbtech.dcm.db.CbomChangeDAO chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);
				
				//�����ڵ�[GID]�� ���ϱ�
				String where = "";
				if(gid.length() == 0) {
					where = "where fg_code='"+fg_code+"'";
					gid = chgDAO.getColumData("mbom_master","pid",where);
				}

				//�ش�ǰ���ڵ� ����
				ArrayList change_list = new ArrayList();
				change_list = chgDAO.readEccBomList(eco_no,gid);
				request.setAttribute("CHANGE_List", change_list); 

				//ECO NO���� �� ��ü������ȣ ���� ����
				request.setAttribute("eco_no",eco_no);
				request.setAttribute("fg_code",fg_code);
				request.setAttribute("gid",gid);
				request.setAttribute("msg",msg);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/CbomEcoPartList.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//		����� �˻� �� ���� 
			//------------------------------------------------------------
			//����� �˻��ϱ�
			else if("eco_fgsearch".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				
				//�𵨰˻����� ����
				ArrayList fg_list = new ArrayList();
				//fg_list = chgBO.viewRevFGList(gid,eco_no);		//�ش� gid�� eco no �� �ش�Ǵ°�
				fg_list = chgBO.viewRevAllFGList(eco_no);			//eco no �� �ش�Ǵ� ����
				request.setAttribute("FG_List", fg_list); 

				//ECO NO���� �� ��ü������ȣ ���� ����
				request.setAttribute("eco_no",eco_no);
				request.setAttribute("fg_code",fg_code);
				request.setAttribute("gid",gid);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/CbomEcoFgList.jsp").forward(request,response);
				
			}
			//���𵨹� ������� ��ǰ���� ���ϱ�
			else if("eco_itemcomp".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				
				//��[FG] ���ϱ�
				String fg_comp = chgBO.compareFGList(eco_no);
				request.setAttribute("fg_comp",fg_comp);

				//�����ǰ ���ϱ�
				String item_comp = chgBO.compareItemList(eco_no);
				request.setAttribute("item_comp",item_comp);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/CbomEcoItemComp.jsp").forward(request,response);
				
			}
			//BOM���� ������ ����ϱ� [html/excel]
			else if("eco_changebom".equals(mode) || "eco_changebomexcel".equals(mode)){
				com.anbtech.dcm.db.CbomChangeDAO chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
				
				//�־��� ���� ��ǰ���泻��
				ArrayList item_list = new ArrayList();
				item_list = chgDAO.outEccBomList(eco_no,gid);		
				request.setAttribute("ITEM_List", item_list); 

				//�־��� ���� ��ǰ�񺯰泻��
				ArrayList pcd_list = new ArrayList();
				pcd_list = chgDAO.outEccBomParentList(eco_no,gid);	
				request.setAttribute("PCD_List", pcd_list); 

				//���躯�� �����׸� ������ �б�
				ArrayList ecr_list = new ArrayList();
				ecr_list = cmodDAO.getEccItem("F01");			//[F01:��������]
				request.setAttribute("ECR_List", ecr_list); 

				//eco no
				request.setAttribute("eco_no",eco_no);

				//�б��ϱ�
				if("eco_changebom".equals(mode))
					getServletContext().getRequestDispatcher("/dcm/ecc/CbomChangeItem.jsp").forward(request,response);
				else 	
					getServletContext().getRequestDispatcher("/dcm/ecc/CbomChangeItemExcel.jsp").forward(request,response);
				
			}
			
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"pl_list":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//������ ����� �Ѿ���� �Ķ����
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MBOM STR������ ���� �⺻�Ķ����
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String parent_code = Hanguel.toHanguel(request.getParameter("parent_code"))==null?"":Hanguel.toHanguel(request.getParameter("parent_code"));
		String child_code = Hanguel.toHanguel(request.getParameter("child_code"))==null?"":Hanguel.toHanguel(request.getParameter("child_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
		String part_name = Hanguel.toHanguel(request.getParameter("part_name"))==null?"":Hanguel.toHanguel(request.getParameter("part_name"));
		String part_spec = Hanguel.toHanguel(request.getParameter("part_spec"))==null?"":Hanguel.toHanguel(request.getParameter("part_spec"));
		String location = Hanguel.toHanguel(request.getParameter("location"))==null?"":Hanguel.toHanguel(request.getParameter("location"));
		String op_code = Hanguel.toHanguel(request.getParameter("op_code"))==null?"":Hanguel.toHanguel(request.getParameter("op_code"));
		String qty_unit = Hanguel.toHanguel(request.getParameter("qty_unit"))==null?"EA":Hanguel.toHanguel(request.getParameter("qty_unit"));
		String qty = Hanguel.toHanguel(request.getParameter("qty"))==null?"1":Hanguel.toHanguel(request.getParameter("qty"));
		String price_unit = Hanguel.toHanguel(request.getParameter("price_unit"))==null?"��":Hanguel.toHanguel(request.getParameter("price_unit"));
		String price = Hanguel.toHanguel(request.getParameter("price"))==null?"1":Hanguel.toHanguel(request.getParameter("price"));
		String adtag = Hanguel.toHanguel(request.getParameter("adtag"))==null?"":Hanguel.toHanguel(request.getParameter("adtag"));
		String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		String url = Hanguel.toHanguel(request.getParameter("url"))==null?"":Hanguel.toHanguel(request.getParameter("url"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		
		//���躯�� ó���� ���������� ó��(����,������ ǥ��)
		String cpid = Hanguel.toHanguel(request.getParameter("cpid"))==null?"":Hanguel.toHanguel(request.getParameter("cpid"));
		String order_date = Hanguel.toHanguel(request.getParameter("order_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_date"));
		String chg_id = Hanguel.toHanguel(request.getParameter("chg_id"))==null?"":Hanguel.toHanguel(request.getParameter("chg_id"));
		String chg_name = Hanguel.toHanguel(request.getParameter("chg_name"))==null?"":Hanguel.toHanguel(request.getParameter("chg_name"));
		String ecc_reason = Hanguel.toHanguel(request.getParameter("ecc_reason"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_reason"));
		String note = Hanguel.toHanguel(request.getParameter("note"))==null?"":Hanguel.toHanguel(request.getParameter("note"));
		
		//BOM LIST
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//------------------------------------------------------------
			//		���� ���� ó���ϱ�
			//------------------------------------------------------------
			//���躯���� BOM STR ��ȸ
			if ("eco_bomlist".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				com.anbtech.dcm.db.CbomChangeDAO chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);

				//�����ڵ�[GID]�� ���ϱ�
				String where = "";
				if(gid.length() == 0) {
					where = "where fg_code='"+fg_code+"'";
					gid = chgDAO.getColumData("mbom_master","pid",where);
					model_code = chgDAO.getColumData("mbom_master","model_code",where);
				}

				//���躯���� BOM ����ü ��ȸ
				ArrayList bom_list = new ArrayList();
				bom_list = chgBO.viewCbomStrList(gid,"0",model_code);
				request.setAttribute("BOM_List", bom_list); 

				//����� ���� �����ϱ� : �����ǰ�� ���� ��µ� ��������� + �߻��� ����
				ArrayList model_list = new ArrayList();
				model_list = chgBO.targetModelList(eco_no);
				request.setAttribute("MODEL_List", model_list); 

				//ECO NO���� �� ��ü������ȣ ���� ����
				request.setAttribute("model_code",model_code);
				request.setAttribute("eco_no",eco_no);
				request.setAttribute("fg_code",fg_code);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/CbomEcoSearch.jsp").forward(request,response);
				
			}
			//������ ǰ���ڵ� �Է�â ����
			else if("eco_prechg".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				com.anbtech.dcm.db.CbomChangeDAO chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
				
				//�����ڵ�[GID]�� ���ϱ�
				String where = "";
				if(gid.length() == 0) {
					where = "where fg_code='"+fg_code+"'";
					gid = chgDAO.getColumData("mbom_master","pid",where);
				}

				//�ش�ǰ���ڵ� ����
				com.anbtech.bm.entity.mbomStrTable item_info = new com.anbtech.bm.entity.mbomStrTable();
				item_info = chgDAO.readStrItem(pid);
				request.setAttribute("ITEM_Info", item_info); 

				//�����[FG�ڵ�]�� ������ ���ϱ�
				where = "where eco_no='"+eco_no+"'";
				order_date = chgDAO.getColumData("ecc_com","order_date",where);
				request.setAttribute("order_date",order_date);

				//�����[FG�ڵ�]�� �������� ���ϱ�
				where = "where eco_no='"+eco_no+"'";
				ecc_reason = chgDAO.getColumData("ecc_com","ecc_reason",where);
				request.setAttribute("ecc_reason",ecc_reason);

				//���躯�� �����׸� ������ �б�
				ArrayList ecr_list = new ArrayList();
				ecr_list = cmodDAO.getEccItem("F01");			//[F01:��������]
				request.setAttribute("ECR_List", ecr_list); 

				//ECO NO���� �� ��ü������ȣ ���� ����
				request.setAttribute("eco_no",eco_no);
				request.setAttribute("fg_code",fg_code);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/CbomEcoChange.jsp").forward(request,response);
				
			}
			//����� ǰ�� ���â ����
			else if("eco_chglist".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				com.anbtech.dcm.db.CbomChangeDAO chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);
				
				//�����ڵ�[GID]�� ���ϱ�
				String where = "";
				if(gid.length() == 0) {
					where = "where fg_code='"+fg_code+"'";
					gid = chgDAO.getColumData("mbom_master","pid",where);
				}

				//�ش�ǰ���ڵ� ����
				ArrayList change_list = new ArrayList();
				change_list = chgDAO.readEccBomList(eco_no,gid);
				request.setAttribute("CHANGE_List", change_list); 

				//ECO NO���� �� ��ü������ȣ ���� ����
				request.setAttribute("eco_no",eco_no);
				request.setAttribute("fg_code",fg_code);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/CbomEcoPartList.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//		BOM ��ǰ���� ó���ϱ�
			//------------------------------------------------------------
			//BOM ��ǰ����
			else if("eco_add".equals(mode) || "eco_change".equals(mode) || "eco_delete".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���������ϱ�
					msg = chgBO.changeEcoPart(cpid,gid,parent_code,child_code,location,op_code,qty_unit,qty,eco_no,chg_id,chg_name,adtag,order_date,ecc_reason,note);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��ϱ�
					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	parent.change.location.href('CbomChangeServlet?mode=eco_chglist&eco_no="+eco_no+"&fg_code="+fg_code+"&gid="+gid+"&msg="+msg+"');");
					out.println("	</script>");

					//---- �ڽ��� ������ clear�Ѵ�.
					out.println("	<script>");
					out.println("	self.location.href('CbomChangeServlet?mode=eco_prechg&eco_no="+eco_no+"&fg_code="+fg_code+"&gid="+gid+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//------------------------------------------------------------
			//		����� ��ǰ�� �����ϰ��� �Ҷ�
			//------------------------------------------------------------
			//������ ��ǰ �б�
			else if("item_prechg".equals(mode)){
				com.anbtech.dcm.db.CbomChangeDAO chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
				
				//�ش�ǰ�� ����
				ArrayList item_list = new ArrayList();
				item_list = chgDAO.readEccBomItem(pid,gid,eco_no);
				request.setAttribute("ITEM_List", item_list); 

				//���躯�� �����׸� ������ �б�
				ArrayList ecr_list = new ArrayList();
				ecr_list = cmodDAO.getEccItem("F01");			//[F01:��������]
				request.setAttribute("ECR_List", ecr_list); 

				//ECO NO���� �� ��ü������ȣ ���� ����
				request.setAttribute("eco_no",eco_no);
				request.setAttribute("fg_code",fg_code);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/dcm/ecc/CbomItemChange.jsp").forward(request,response);
				
			}
			//���������ϱ�
			else if("item_change".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���������ϱ�
					msg = chgBO.modifyEcoPart(pid,gid,child_code,location,op_code,eco_no,adtag,chg_id,chg_name,ecc_reason,note);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��ϱ�
					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	parent.change.location.href('CbomChangeServlet?mode=eco_chglist&eco_no="+eco_no+"&fg_code="+fg_code+"&gid="+gid+"&msg="+msg+"');");
					out.println("	</script>");

					//---- �ڽ��� ������ clear�Ѵ�.
					out.println("	<script>");
					out.println("	self.location.href('CbomChangeServlet?mode=eco_prechg&eco_no="+eco_no+"&fg_code="+fg_code+"&gid="+gid+"');");
					out.println("	</script>");
				
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}
			}
			//���� UNDO �����ϱ�
			else if("item_undo".equals(mode)){
				com.anbtech.dcm.business.CbomChangeBO chgBO = new com.anbtech.dcm.business.CbomChangeBO(con);
				
				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//���������ϱ�
					msg = chgBO.undoEcoPart(cpid,pid,gid,adtag,eco_no);
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//�б��ϱ�
					//---- ������ ����� ������ ȭ�鿡 �״�� ����Ѵ�.----//
					out.println("	<script>");
					out.println("	parent.change.location.href('CbomChangeServlet?mode=eco_chglist&eco_no="+eco_no+"&fg_code="+fg_code+"&gid="+gid+"&msg="+msg+"');");
					out.println("	</script>");

					//---- �ڽ��� ������ clear�Ѵ�.
					out.println("	<script>");
					out.println("	self.location.href('CbomChangeServlet?mode=eco_prechg&eco_no="+eco_no+"&fg_code="+fg_code+"&gid="+gid+"');");
					out.println("	</script>");
				
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

