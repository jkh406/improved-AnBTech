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

public class BomInputServlet extends HttpServlet {
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
		
		String mgr = "";	//������ 
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MBOM STRUCTURE���� ���/����/����/List ó���ϱ�
			//------------------------------------------------------------
			//MBOM STR LIST ��ȸ
			if ("pl_list".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST ��ȸ
				ArrayList str_list = new ArrayList();
				if(gid.length() != 0) str_list = inputBO.getStrList(gid,"0",model_code);
				request.setAttribute("STR_List", str_list); 

				//������ �� FG�ڵ� ���� ����
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(gid.length() != 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/str/plPartList.jsp").forward(request,response);
				
			}
			//MBOM STR ���/���� �غ�
			else if ("pl_prewrite".equals(mode) || "pl_premodify".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//�������� �ִ��� �Ǵ��ϱ�
				mgr = modDAO.getFgGrade(login_id,gid);

				//�Է�/������ ������ �б�
				com.anbtech.bm.entity.mbomStrTable strT = new com.anbtech.bm.entity.mbomStrTable();
				if(mgr.length() == 0) strT = modDAO.readStrItem(pid);
				request.setAttribute("ITEM_List", strT);

				//MBOM MASTER���� ������ �б�
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(mgr.length() == 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);
				

				//���/������ �޽��� �����ϱ�
				msg = mgr + msg;
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/str/plPartReg.jsp").forward(request,response);
			}
			//��Ȯ�� BOM ����Ʈ
			else if ("fg_search".equals(mode) || "fi_search_1".equals(mode) || "fi_search_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST ��ȸ
				ArrayList xbom_list = new ArrayList();
				xbom_list = modDAO.getXbomList(sItem,sWord,login_id);
				request.setAttribute("XBOM_List", xbom_list); 

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
		
				//�б��ϱ�
				if("fg_search".equals(mode))			//������ ���� �б�
					getServletContext().getRequestDispatcher("/bm/str/plFgSearch.jsp").forward(request,response);
				else if("fi_search_1".equals(mode))		//BOM LIST IMPORT (��ǰ��/��ǰ��/LOC)
					getServletContext().getRequestDispatcher("/bm/str/fiFgSearch_1.jsp").forward(request,response);
				else if("fi_search_2".equals(mode))		//PART LIST IMPORT (ǰ��/�����ڵ�/LOC)
					getServletContext().getRequestDispatcher("/bm/str/fiFgSearch_2.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		MBOM STRUCTURE���� ���� IMPORT ó���ϱ�
			//------------------------------------------------------------
			//MBOM STR ���/���� �غ�
			else if ("fi_preimport_1".equals(mode) || "fi_preimport_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//�������� �ִ��� �Ǵ��ϱ�
				msg += modDAO.getFgGrade(login_id,gid);
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				request.setAttribute("parent_code",parent_code);
				request.setAttribute("level_no",level_no);

				//�б��ϱ�
				if("fi_preimport_1".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/fiImport_1.jsp").forward(request,response);
				else if("fi_preimport_2".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/fiImport_2.jsp").forward(request,response);
			
			}
			//MBOM LIST ASSY ���� �غ�
			else if ("fi_list_1".equals(mode) || "fi_list_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST ��ȸ
				ArrayList assy_list = new ArrayList();
				if(gid.length() != 0) assy_list = modDAO.getAssyList(gid);
				request.setAttribute("ASSY_List", assy_list); 

				//������ �� FG�ڵ� ���� ����
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(gid.length() != 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);
			
				//�б��ϱ�
				if("fi_list_1".equals(mode))			//BOM LIST IMPORT (��ǰ��/��ǰ��/Location)
					getServletContext().getRequestDispatcher("/bm/str/fiAssyList_1.jsp").forward(request,response);
				else if("fi_list_2".equals(mode))		//ǰ�� LIST IMPORT (ǰ���ڵ�/�����ڵ�/Location)
					getServletContext().getRequestDispatcher("/bm/str/fiAssyList_2.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//		MBOM STRUCTURE �˻��ϱ�
			//------------------------------------------------------------
			//MBOM STR LIST ��ȸ
			else if ("chk_list".equals(mode)){
				com.anbtech.bm.business.BomApprovalBO appBO = new com.anbtech.bm.business.BomApprovalBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//��ǰ�� ����Assy�ڵ� �ߺ��Է� �˻� : Phantom Assy�˻�
				String assy_dup = appBO.checkPhantomAssy(gid);
				request.setAttribute("assy_dup",assy_dup); 

				//Location No ���� �� �ߺ��˻�
				String loc_dup = appBO.checkLocation(gid);
				request.setAttribute("loc_dup",loc_dup); 

				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/str/chkPartList.jsp").forward(request,response);
				
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

			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;						
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//MultipartRequest ũ��, ������丮
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/mbom/"+login_id+"/tmp";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory�����ϱ�
		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//PL IMPORTó���ϱ� : ���� ������
		String ck = multi.getParameter("ck")==null?"tab":multi.getParameter("ck"); 
		if(ck.equals("tab")) ck = "	";
		else if(ck.equals("semi")) ck = ";";
		else if(ck.equals("pause")) ck = ",";
		else if(ck.equals("space")) ck = " ";
		else if(ck.equals("etc")) ck = multi.getParameter("etc");

		//�⺻�Ķ����
		String mode = multi.getParameter("mode")==null?"pl_list":multi.getParameter("mode");
		String page = multi.getParameter("page")==null?"1":multi.getParameter("page");
		String sItem = multi.getParameter("sItem")==null?"fg_code":multi.getParameter("sItem");
		String sWord = multi.getParameter("sWord")==null?"":multi.getParameter("sWord");
	
		//MBOM STR������ ���� �⺻�Ķ����
		String pid = multi.getParameter("pid")==null?"":multi.getParameter("pid");
		String gid = multi.getParameter("gid")==null?"":multi.getParameter("gid");
		String parent_code = multi.getParameter("parent_code")==null?"":multi.getParameter("parent_code");
		String child_code = multi.getParameter("child_code")==null?"":multi.getParameter("child_code");
		String level_no = multi.getParameter("level_no")==null?"":multi.getParameter("level_no");
		String part_name = multi.getParameter("part_name")==null?"":multi.getParameter("part_name");
		String part_spec = multi.getParameter("part_spec")==null?"":multi.getParameter("part_spec");
		String location = multi.getParameter("location")==null?"":multi.getParameter("location");
		String op_code = multi.getParameter("op_code")==null?"":multi.getParameter("op_code");
		String qty_unit = multi.getParameter("qty_unit")==null?"":multi.getParameter("qty_unit");
		String qty = multi.getParameter("qty")==null?"":multi.getParameter("qty");
		String price_unit = multi.getParameter("price_unit")==null?"":multi.getParameter("price_unit");
		String price = multi.getParameter("price")==null?"":multi.getParameter("price");
		String adtag = multi.getParameter("adtag")==null?"":multi.getParameter("adtag");
		String eco_no = multi.getParameter("eco_no")==null?"":multi.getParameter("eco_no");
		String part_cnt = multi.getParameter("part_cnt")==null?"":multi.getParameter("part_cnt");
		String part_type = multi.getParameter("part_type")==null?"":multi.getParameter("part_type");
		String url = multi.getParameter("url")==null?"":multi.getParameter("url");
		String msg = multi.getParameter("msg")==null?"":multi.getParameter("msg");

		String mgr = "";	//������ 
		String model_code = multi.getParameter("model_code")==null?"":multi.getParameter("model_code");

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//------------------------------------------------------------
			//		MBOM STRUCTURE���� ���/����/����/List ó���ϱ�
			//------------------------------------------------------------
			//MBOM STR LIST ��ȸ
			if ("pl_list".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//������ �� FG�ڵ� ���� ����
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//�������� �ִ��� �Ǵ��ϱ�
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) gid = "";

				//LIST ��ȸ
				ArrayList str_list = new ArrayList();
				if(gid.length() != 0) str_list = inputBO.getStrList(gid,"0",model_code);
				request.setAttribute("STR_List", str_list); 
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/str/plPartList.jsp").forward(request,response);
				
			}
			//MBOM STR ���/���� �غ�
			else if ("pl_prewrite".equals(mode) || "pl_premodify".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//�������� �ִ��� �Ǵ��ϱ�
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) {
					out.println("	<script>");
					out.println("	alert('"+msg+"');");
					out.println("	history.back();");
					out.println("	</script>");
					out.close();
				}

				//�Է�/������ ������ �б�
				com.anbtech.bm.entity.mbomStrTable strT = new com.anbtech.bm.entity.mbomStrTable();
				strT = modDAO.readStrItem(pid);
				request.setAttribute("ITEM_List", strT);

				//MBOM MASTER���� ������ �б�
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//�޽��� ����
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				
				//�б��ϱ�
				getServletContext().getRequestDispatcher("/bm/str/plPartReg.jsp").forward(request,response);
			}
			//MBOM STR ��� / ���� / ����
			else if("pl_write".equals(mode) || "pl_modify".equals(mode) || "pl_delete".equals(mode) || "pl_all_delete".equals(mode)) {
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//��������ϱ�
					if("pl_write".equals(mode)) {
						msg = inputBO.insertStr(gid,parent_code,child_code,location,op_code,qty_unit,qty,part_cnt);
					} 
					//���������ϱ�
					else if ("pl_modify".equals(mode)) {
						msg = inputBO.updateStr(pid,parent_code,child_code,location,op_code,qty_unit,qty,gid,part_type);
					}
					//���������ϱ� : ��������
					else if("pl_delete".equals(mode)) {
						msg = inputBO.deleteStr(pid,gid,parent_code);
					}
					//�����ϱ� : ��ü����
					else if("pl_all_delete".equals(mode)) {
						msg = inputBO.deleteAllStr(pid,gid,parent_code);
					}
					con.commit(); // commit�Ѵ�.
					con.setAutoCommit(true);

					//MBOM TREE LIST ��ȸ�� ��ũ [�����ð����� ������]
/*					if("pl_all_delete".equals(mode)) {	//��ü������ ���÷���
						out.println("	<script>");
						out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list&gid="+gid+"&model_code="+model_code+"');");
						out.println("	</script>");
					} else if("pl_modify".equals(mode) & part_type.equals("A")) {	//��ü�����̸�
						out.println("	<script>");
						out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list&gid="+gid+"&model_code="+model_code+"');");
						out.println("	</script>");
					} else {
						msg += "  ���泻���� �ٽú����� �� ���� BOM LIST���� [�ٽú���] �� ���� �Ͻʽÿ�.";
					}
*/					
					//������ ���÷���
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list&gid="+gid+"&model_code="+model_code+"');");
					out.println("	</script>");

					//MBOM TREE �Է�â���� ��ũ
					out.println("	<script>");
					out.println("	parent.reg.location.href('BomInputServlet?mode=pl_prewrite&msg="+msg+"&gid="+gid+"&model_code="+model_code+"');");
					out.println("	</script>");
					out.close();

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//��Ȯ�� BOM ����Ʈ
			else if ("fg_search".equals(mode) || "fi_search_1".equals(mode) || "fi_search_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST ��ȸ
				ArrayList xbom_list = new ArrayList();
				xbom_list = modDAO.getXbomList(sItem,sWord,login_id);
				request.setAttribute("XBOM_List", xbom_list); 

				//�����ϱ�
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
		
				//�б��ϱ�
				if("fg_search".equals(mode))			//������ ���� �б�
					getServletContext().getRequestDispatcher("/bm/str/plFgSearch.jsp").forward(request,response);
				else if("fi_search_1".equals(mode))		//BOM LIST IMPORT (��ǰ��/��ǰ��/LOC)
					getServletContext().getRequestDispatcher("/bm/str/fiFgSearch_1.jsp").forward(request,response);
				else if("fi_search_2".equals(mode))		//PART LIST IMPORT (ǰ��/�����ڵ�/LOC)
					getServletContext().getRequestDispatcher("/bm/str/fiFgSearch_2.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		MBOM STRUCTURE���� ���� IMPORT ó���ϱ�
			//------------------------------------------------------------
			//MBOM STR ���/���� �غ�
			else if ("fi_preimport_1".equals(mode) || "fi_preimport_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//�������� �ִ��� �Ǵ��ϱ�
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) gid = "";

				//�����ϱ�
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				request.setAttribute("parent_code",parent_code);
				request.setAttribute("model_code",model_code);
				request.setAttribute("level_no",level_no);

				//�б��ϱ�
				if("fi_preimport_1".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/fiImport_1.jsp").forward(request,response);
				else if("fi_preimport_2".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/fiImport_2.jsp").forward(request,response);
			
			}
			//MBOM LIST ASSY ���� �غ�
			else if ("fi_list_1".equals(mode) || "fi_list_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//������ �� FG�ڵ� ���� ����
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(gid.length() != 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//�������� �ִ��� �Ǵ��ϱ�
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) gid = "";

				//LIST ��ȸ
				ArrayList assy_list = new ArrayList();
				if(gid.length() != 0) assy_list = modDAO.getAssyList(gid);
				request.setAttribute("ASSY_List", assy_list); 
				
				//�б��ϱ�
				if("fi_list_1".equals(mode))			//BOM LIST IMPORT (��ǰ��/��ǰ��/Location)
					getServletContext().getRequestDispatcher("/bm/str/fiAssyList_1.jsp").forward(request,response);
				else if("fi_list_2".equals(mode))		//ǰ�� LIST IMPORT (ǰ���ڵ�/�����ڵ�/Location)
					getServletContext().getRequestDispatcher("/bm/str/fiAssyList_2.jsp").forward(request,response);
				
			}
			//BOM List �б�: File Import���� �б�
			else if ("fi_import_1".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				
				//PL Import�� ���
				msg = inputBO.getImportList(multi,filepath,ck,parent_code,level_no,gid);	//DB����
				
				//PL�ø��� ���â���� ��ũ
				String data = "&gid="+gid+"&level_no=0&model_code="+model_code;
				if(msg.indexOf("������") != -1) {
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list"+data+"');");
					out.println("	</script>");
				} else {
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=fi_list_1"+data+"');");
					out.println("	</script>");
				}
				
				//MBOM TREE �Է�â���� ��ũ
				//data = "&msg=&gid="+gid+"&level_no="+level_no+"&parent_code="+parent_code;
				out.println("	<script>");
				out.println("	alert('"+msg+"');");
				out.println("	parent.reg.location.href('BomInputServlet?mode=fi_preimport_1');");
				out.println("	</script>");
				out.close();

			}
			//P/L List �б�: File Import���� �б�
			else if ("fi_import_2".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				
				//PL Import�� ���
				msg = inputBO.getImportPartList(multi,filepath,ck,gid);	//DB����
				
				//PL�ø��� ���â���� ��ũ
				String data = "&gid="+gid+"&level_no=0&model_code="+model_code;
				if(msg.indexOf("������") != -1) {
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list"+data+"');");
					out.println("	</script>");
				} else {
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=fi_list_2"+data+"');");
					out.println("	</script>");
				}
				
				//MBOM TREE �Է�â���� ��ũ
				//data = "&msg=&gid="+gid+"&level_no="+level_no+"&parent_code="+parent_code;
				out.println("	<script>");
				out.println("	alert('"+msg+"');");
				out.println("	parent.reg.location.href('BomInputServlet?mode=fi_preimport_2');");
				out.println("	</script>");
				out.close();
				

			}
			
		}catch (Exception e){
				//������� �������� �б�
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}

