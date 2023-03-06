import com.anbtech.gm.entity.*;
import com.anbtech.gm.db.*;
import com.anbtech.gm.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class GoodsInfoServlet extends HttpServlet {

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

		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page");
		String no			= request.getParameter("no");
		String searchword	= Hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");
		if (mode == null) mode = "list";
		if (no == null) no = "";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

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
		String redirectUrl = "";

		//������ �Ķ���͵�
		String umask		= request.getParameter("umask");
		String why_revision = request.getParameter("why_revision");	//�Ļ�����(r:������,d:�Ļ�)
		String one_class	= request.getParameter("one_class");	//��ǰ���ڵ�
		String two_class	= request.getParameter("two_class");	//��ǰ�ڵ�
		String three_class	= request.getParameter("three_class");	//�𵨱��ڵ�
		String four_class	= request.getParameter("four_class");	//���ڵ�
		String item_code	= request.getParameter("item_code");	//�׸��ڵ�

		if (one_class == null) one_class = "";
		if (two_class == null) two_class = "";
		if (three_class == null) three_class = "";
		if (four_class == null) four_class = "";


/*		//ǰ���ȣ �˻�â ������ �߰��� �Ķ���͵�(by ����)
		String fname		= request.getParameter("fname")==null?"":request.getParameter("fname");// form �̸�
		String field		= request.getParameter("field");			// form field �̸�(string)
		String one_name		= request.getParameter("one_name");			// ��ǰ����
		String two_name		= request.getParameter("two_name");			// ��ǰ��
		String three_name	= request.getParameter("three_name");		// �𵨱���
		String four_name	= request.getParameter("four_name");		// �𵨸�
		String fg_code		= request.getParameter("fg_code");			// fg_code field */
	
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////////
			// ������ �߰� �Ǵ� ���� �� �����
			////////////////////////////////////////////////////////
			if(mode.equals("add_model") || mode.equals("mod_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.business.GoodsInfoBO goodsBO = new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.entity.GoodsInfoTable table = new com.anbtech.gm.entity.GoodsInfoTable();
			
				// ������ ��������
				table = goodsBO.getAddGoodsForm(mode,one_class,two_class,three_class,no);
				request.setAttribute("GoodsInfo",table);

				//���õ� ��ǰ ������ ���ǵ� ���帮��Ʈ ��������
				ArrayList spec_list = new ArrayList();
				if(!two_class.equals("")) spec_list = goodsBO.getSpecList(mode,two_class,no);
				request.setAttribute("SpecList",spec_list);

				com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
				com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
				redirect = redirectBO.getLinkUrlForInput(mode,searchword,searchscope,category,page,no);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/gm/add_model.jsp?mode=" + mode).forward(request,response);
			}

			////////////////////////////////////////////////////////
			// ���Ļ� ����� �����
			////////////////////////////////////////////////////////
			if(mode.equals("rev_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.business.GoodsInfoBO goodsBO = new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.entity.GoodsInfoTable table = new com.anbtech.gm.entity.GoodsInfoTable();
				com.anbtech.gm.business.makeGoodsTreeItems tree = new com.anbtech.gm.business.makeGoodsTreeItems(con);

				// ������ ��������
				table = goodsBO.getRevModelForm(mode,no,why_revision);
				table.setOneClass(tree.getGoodsClassStr(Integer.parseInt(no),""));
				request.setAttribute("GoodsInfo",table);

				//��ǰ ������ ���ǵ� ���帮��Ʈ ��������
				ArrayList spec_list = new ArrayList();
				spec_list = goodsBO.getSpecList(mode,table.getTwoClass(),no);
				request.setAttribute("SpecList",spec_list);

				com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
				com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
				redirect = redirectBO.getLinkUrlForInput(mode,searchword,searchscope,category,page,no);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/gm/rev_model.jsp?mode=" + mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			//��ϵ� �� ����Ʈ ���
			/////////////////////////////////////////////////////
			else if(mode.equals("list_model")){
				com.anbtech.gm.business.GoodsInfoBO goodsBO = new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

				ArrayList model_list = new ArrayList();
				model_list = goodsDAO.getModelList(mode,searchword,searchscope,category,page);
				request.setAttribute("ModelList", model_list);

				com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
				com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
				redirect = redirectBO.getRedirect(mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/gm/list_model.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			// ���õ� ���� �� ���� ����
			/////////////////////////////////////////////////////
			if(mode.equals("view_model")){
				com.anbtech.gm.business.GoodsInfoBO goodsBO		= new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO			= new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.entity.GoodsInfoTable table		= new com.anbtech.gm.entity.GoodsInfoTable();

				//�� �⺻������ �����´�.
				table = goodsDAO.getGoodsInfoByMid(no);

				//���õ� �׸��� ���� �ƴ� ��쿡�� �޽��� ���
				if(!table.getGoodsLevel().equals("4")){
//					PrintWriter out = response.getWriter();
//					out.println("�𵨸��� Ŭ���Ͻʽÿ�.");
					redirectUrl = "GoodsInfoServlet?mode=list_model";
				}else{
					//���õ� ���� ��ǰ�з� ���ڿ��� �����´�.
					table.setOneClass(tree.getGoodsClassStr(Integer.parseInt(no),""));
					request.setAttribute("ModelInfo", table);

					//���� ���ν��������� �����´�.
					ArrayList spec_list = new ArrayList();
					spec_list = goodsBO.getSpecList(mode,two_class,no);
					request.setAttribute("SpecList",spec_list);

					com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
					com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
					redirect = redirectBO.getRedirect(mode,searchword,searchscope,category,page,no);
					request.setAttribute("Redirect",redirect);

					getServletContext().getRequestDispatcher("/gm/view_model.jsp").forward(request,response);
				}
			}

			////////////////////////////////////////////////////
			// ���õ� ������ ����ó��
			///////////////////////////////////////////////////
			else if(mode.equals("del_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO	= new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.entity.GoodsInfoTable table	= new com.anbtech.gm.entity.GoodsInfoTable();

				table = goodsDAO.getGoodsInfoByMid(no);
				String code = table.getGoodsCode();

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					goodsDAO.deleteModelCodeInfo(code);
					goodsDAO.deleteModelInfo(no);
					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//Ʈ����¿� ��ũ��Ʈ���� ����
				com.anbtech.gm.business.makeGoodsTreeItems tree	 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree2 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/gm/admin/";
				tree.makeGoodsTree(output_path + "goods_tree_items.js","GoodsInfoServlet",1,0);
				tree2.makeGoodsTree(output_path + "goodsTreeItemsForModelSearch.js","NA",1,0);
			
				redirectUrl = "GoodsInfoServlet?mode=list_model";
			}


			/////////////////////////////////////////////////////
			//  ��ǰ ���� �������� (�𵨸�, ���ڵ�, F/Gcode)
			/////////////////////////////////////////////////////
			else if(mode.equals("search_model_info")){
				com.anbtech.gm.business.GoodsInfoBO goodsBO = new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				//String[] fieldArry = new String[9];

				ArrayList model_list = new ArrayList();
				model_list = goodsDAO.searchModelList(mode,searchword,searchscope,category,page);
				request.setAttribute("ModelList", model_list);
				//fieldArry = (String[])goodsBO.getFieldArry(one_class,one_name,two_class,two_name,three_class,three_name,four_class,four_name,fg_code);

				com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
				com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
				redirect = redirectBO.searchRedirect(mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);
				
				//request.setAttribute("FIELD",fieldArry);

				getServletContext().getRequestDispatcher("/gm/searchModelInfo.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			// ��ǰ�� ǥ�����ø� �߰�,������
			/////////////////////////////////////////////////////
			else if(mode.equals("add_template") || mode.equals("upd_template")){
				com.anbtech.gm.business.GoodsInfoBO goodsBO		= new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO			= new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.entity.GoodsInfoItemTable table	= new com.anbtech.gm.entity.GoodsInfoItemTable();

				//���õ� �׸��ڵ忡 ���� ������ �����´�.
				table = goodsBO.getAddItemForm(mode,one_class,two_class,item_code);
				request.setAttribute("ItemInfo",table);

				//���õ� ��ǰ�� �����ϴ� �׸� ����Ʈ�� �����´�.
				ArrayList item_list = new ArrayList();
				item_list = goodsBO.getItemList(two_class);
				request.setAttribute("ItemList", item_list);

				getServletContext().getRequestDispatcher("/gm/admin/template_mgr.jsp?mode=" + mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			// ��ǰƮ�� ��¿� ��ũ��Ʈ ���� ����
			/////////////////////////////////////////////////////
			else if(mode.equals("make_tree")){
				com.anbtech.gm.business.makeGoodsTreeItems tree	 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree2 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/gm/admin/";
				
				try{
					tree.makeGoodsTree(output_path + "goods_tree_items.js","GoodsInfoServlet",1,0);
					tree2.makeGoodsTree(output_path + "goodsTreeItemsForModelSearch.js","NA",1,0);
				}catch (Exception e){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('��ǰ�з��� �Ϸ���� �ʾ� Ʈ�������� �� �� �����ϴ�.\n���� ��ǰ��-��ǰ-�𵨱��� 3�ܰ� �з��� �Ϸ��Ͻʽÿ�.');");
					out.println("	</script>");
					out.close();
				}

				getServletContext().getRequestDispatcher("/gm/admin/make_tree.jsp").forward(request,response);
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

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/gm/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

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

		//������������ �� �޾ƿ´�. multi���� ������
		String mode			= multi.getParameter("mode");
		String page			= multi.getParameter("page");
		String searchword	= multi.getParameter("searchword");
		String searchscope	= multi.getParameter("searchscope");
		String category		= multi.getParameter("category");
		String no			= multi.getParameter("no");

		//�������� �Ѿ���ų� null�� �� ���鿡 ���� ó��.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

		//������ ��ϰ��� �� �ʿ��� �Ķ���͵�
		String code			= multi.getParameter("code");		//���ڵ�
		String name			= multi.getParameter("name");		//�𵨸�
		String name2		= multi.getParameter("name2");		//�𵨸�2
		String short_name	= multi.getParameter("short_name");	//�𵨾��
		String color_code	= multi.getParameter("color_code");	//�𵨻����ڵ�
		String other_info	= multi.getParameter("other_info");	//��Ÿ����

		String code_str		= multi.getParameter("code_str");	//�����ڵ帮��Ʈ(1101001,1101002,1101003, ....��)
		String spec_str		= multi.getParameter("spec_str");	//�󼼽���(�ڵ�|�׸�|����, ... ��)

		//���Ļ���Ͻ� �߰��Ǵ� �Ķ���͵�
		String revision_code= multi.getParameter("revision_code");	//��ɱ����ڵ�
		String derive_code	= multi.getParameter("derive_code");	//�Ļ��ڵ�
		if(revision_code == null) revision_code = "1";
		if(derive_code == null) derive_code = "00";

		//ǥ�� ���ø� ��ϰ��� �� �ʿ��� �Ķ���͵�
		String one_class	= multi.getParameter("one_class");	//��ǰ���ڵ�
		String two_class	= multi.getParameter("two_class");	//��ǰ�ڵ�
		String three_class	= multi.getParameter("three_class");//�𵨱��ڵ�
		String item_code	= multi.getParameter("item_code");	//�׸��ڵ�
		String item_name	= multi.getParameter("item_name");	//�׸��
		String item_value	= multi.getParameter("item_value");	//�׸�
		String item_unit	= multi.getParameter("item_unit");	//�׸����
		String write_exam	= multi.getParameter("write_exam");	//�ۼ���
		String item_desc	= multi.getParameter("item_desc");	//�׸񼳸�

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			/////////////////////////////////////////////////////
			// �ű� ������ or �Ļ��� ���ó��
			/////////////////////////////////////////////////////
			if (mode.equals("add_model") || mode.equals("rev_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

				//���ڵ� �ߺ�Ȯ��
				int same_model_count = goodsDAO.getCountWithSameModelCode(code);
				if(same_model_count > 0){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('������ ���ڵ尡 �̹� ��ϵǾ� �־� ����۾��� ����� �� �����ϴ�.');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}
				
				//�𵨱԰� �ߺ�Ȯ��
				String same_model_code = goodsDAO.getModelCodeWithSameProperty(code_str,spec_str);
				if(same_model_code.length() > 1){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('������ �𵨱԰��� ������ ���� ��ϵǾ� �־� �۾��� ����� �� �����ϴ�.\\n���ڵ�:" + same_model_code + "');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}

				con.setAutoCommit(false);	// Ʈ������� ����
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//db����
					goodsDAO.saveModelCodeInfo(code,revision_code,derive_code);
					goodsDAO.saveModelInfo(three_class,code,name,name2,short_name,color_code,other_info,code_str,spec_str,login_id);
					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					//������� �������� �б�
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//Ʈ����¿� ��ũ��Ʈ���� ����
				com.anbtech.gm.business.makeGoodsTreeItems tree	 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree2 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/gm/admin/";
				tree.makeGoodsTree(output_path + "goods_tree_items.js","GoodsInfoServlet",1,0);
				tree2.makeGoodsTree(output_path + "goodsTreeItemsForModelSearch.js","NA",1,0);

				redirectUrl = "GoodsInfoServlet?mode=list_model";
			}

			/////////////////////////////////////////////////////
			// ������ ����ó��
			/////////////////////////////////////////////////////
			else if (mode.equals("mod_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

/*
				//�𵨱԰� �ߺ�Ȯ��
				String same_model_code = goodsDAO.getModelCodeWithSameProperty(code_str,spec_str);
				if(same_model_code.length() > 1){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('������ �𵨱԰��� ������ ���� ��ϵǾ� �־� �۾��� ����� �� �����ϴ�.\\n���ڵ�:" + same_model_code + "');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}
*/
				goodsDAO.modifyModelInfo(no,three_class,code,name,name2,short_name,color_code,other_info,code_str,spec_str,login_id);

				//Ʈ����¿� ��ũ��Ʈ���� ����
				com.anbtech.gm.business.makeGoodsTreeItems tree	 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree2 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/gm/admin/";
				tree.makeGoodsTree(output_path + "goods_tree_items.js","GoodsInfoServlet",1,0);
				tree2.makeGoodsTree(output_path + "goodsTreeItemsForModelSearch.js","NA",1,0);

				redirectUrl = "GoodsInfoServlet?mode=view_model&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no;
			}

			/////////////////////////////////////////////////////
			// ��ǰ�� ���� ǥ�����ø� �߰� �Ǵ� ����ó��
			/////////////////////////////////////////////////////
			else if (mode.equals("add_template") || mode.equals("upd_template")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

				if(mode.equals("add_template")) goodsDAO.saveItemInfo(item_code,item_name,item_value,item_unit,write_exam,item_desc);
				else goodsDAO.updItemInfo(item_code,item_name,item_value,item_unit,write_exam,item_desc);

				redirectUrl = "GoodsInfoServlet?mode=add_template&one_class=" + one_class + "&two_class=" + two_class;
			}

			//redirectUrl�� ���� �����ÿ��� redirectUrl��η� �̵��Ѵ�.
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			//con�Ҹ�
			close(con);
		}
	} //doPost()
}
