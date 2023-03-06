/* 
	�ڵ�ü��� �����ؼ� ������ ���� 
	===========================
	ä��ü�谡 ��з�(1)+�ߺз�(2)+�Һз�(2) �� �ƴҰ�쿡�� ������ �����ؾ� ��.
	- CodeMgrServlet.java �� doGet() �պκа� doPost() �պκ�

*/
import com.anbtech.cm.entity.*;
import com.anbtech.cm.db.*;
import com.anbtech.cm.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class CodeMgrServlet extends HttpServlet {

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
		String searchword	= request.getParameter("searchword");
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");
		String umask		= request.getParameter("umask");

		if (mode == null) mode = "list_item";
		if (no == null) no = "";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

		String item_no		= request.getParameter("item_no")==null?"":request.getParameter("item_no");

		//ǥ��ǰ �� ���ǰ ��� �� �ʿ��� �Ķ���͵�
		String code_big		= request.getParameter("code_big")==null?"":request.getParameter("code_big");
		String code_mid		= request.getParameter("code_mid")==null?"":request.getParameter("code_mid");
		String code_small	= request.getParameter("code_small")==null?"":request.getParameter("code_small");
		String spec_code	= request.getParameter("spec_code");
		//ä��ü�谡 ��з�(1)+�ߺз�(2)+�Һз�(2) �� �ƴҰ�쿡�� �Ʒ� 3������ �����ؾ� ��.
		if(item_no != null && !item_no.equals("")){
			code_big	= item_no.substring(0,1);
			code_mid	= item_no.substring(0,3);
			code_small	= item_no.substring(0,5);
		}

		//����ǰ�ڵ� ��Ͻ� �ʿ��� �Ķ���͵�
		String one_class	= request.getParameter("one_class");				//��ǰ���ڵ�
		String two_class	= request.getParameter("two_class");				//��ǰ�ڵ�
		if (one_class == null) one_class = "";
		if (two_class == null) two_class = "";

		//Assy�ڵ� ��� �� �ʿ��� �Ķ���͵�
		String model_code	= request.getParameter("model_code");

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
	
		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////
			// �˻� ���ǿ� �´� ǰ�� ����Ʈ ���
			///////////////////////////////////////////////////
			if(mode.equals("list_item") || mode.equals("list_item_p")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);			
				ArrayList list_item = new ArrayList();

				list_item = cmDAO.getListItem(mode,category,code_big,code_mid,code_small,searchword,searchscope,page,login_id);
				request.setAttribute("LIST_ITEM",list_item);

				if(mode.equals("list_item_p")){
					//ǰ��з� ��������
					com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

					part = cmBO.getItemClass(mode,code_big,code_mid,code_small);
					request.setAttribute("ITEM_CLASS",part);
				}

				
				com.anbtech.cm.business.CodeLinkUrlBO redirectBO = new com.anbtech.cm.business.CodeLinkUrlBO(con);
				com.anbtech.cm.entity.CodeLinkUrl redirect = new com.anbtech.cm.entity.CodeLinkUrl();
				redirect = redirectBO.getRedirect(mode,category,code_big,code_mid,code_small,searchword,searchscope,page,login_id);
				request.setAttribute("Redirect",redirect);

				if(mode.equals("list_item")){
					getServletContext().getRequestDispatcher("/cm/list_item.jsp?mode="+mode).forward(request,response);
				}else if(mode.equals("list_item_p")){
					getServletContext().getRequestDispatcher("/cm/list_item_p.jsp?mode="+mode).forward(request,response);
				}
			}

			/////////////////////////////////////////////////////
			// ��������ǰ�� ä���� �� ��ä���� ǰ�������� ������
			/////////////////////////////////////////////////////
			else if(mode.equals("reg_item") || mode.equals("modify_item")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

				//ǰ������ ��������
				part = cmBO.getPartRegForm(mode,code_big,code_mid,code_small,item_no);
				request.setAttribute("PART_INFO",part);

				//���õ� �Һз� ǰ�� ���ǵ� ���帮��Ʈ ��������
				//������ ��쿡�� ���� �����Ͽ�..
				ArrayList spec_list = new ArrayList();
				spec_list = cmBO.getSpecList(mode,code_small,item_no);
				request.setAttribute("SPEC_LIST",spec_list);

				// ÷��ȭ�� ��������
				ArrayList file_list = new ArrayList();
				file_list = cmDAO.getFile_list(item_no);
				request.setAttribute("FILE_LIST", file_list);

				getServletContext().getRequestDispatcher("/cm/reg_item.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// ����ǰ �ڵ� ��� �� ���� ��
			/////////////////////////////////////////////////////
			else if(mode.equals("reg_fg") || mode.equals("modify_fg")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

				//ǰ������ ��������
				part = cmBO.getFgRegForm(mode,code_big,one_class,two_class,item_no,model_code);
				request.setAttribute("PART_INFO",part);

				//���õ� ���ڵ忡 ���õ� �ڵ� ����Ʈ ��������
				ArrayList item_list = new ArrayList();
				item_list = cmDAO.getItemListByModelCode(model_code);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/cm/reg_fg.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// ASS'Y �ڵ� ��� �� ���� ��
			/////////////////////////////////////////////////////
			else if(mode.equals("reg_assy") || mode.equals("modify_assy")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

				//ǰ������ ��������
				part = cmBO.getAssyRegForm(mode,code_big,one_class,two_class,item_no,model_code);
				request.setAttribute("PART_INFO",part);

				//���õ� ���ڵ忡 ���õ� �ڵ� ����Ʈ ��������
				ArrayList item_list = new ArrayList();
				item_list = cmDAO.getItemListByModelCode(model_code);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/cm/reg_assy.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// ASS'Y �ڵ� or F/G �ڵ� ���� ���� ��
			/////////////////////////////////////////////////////
			else if(mode.equals("reg_item2")){
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.business.CodeLinkUrlBO clinkBO = new com.anbtech.cm.business.CodeLinkUrlBO(con);

				part = cmBO.getViewForm(item_no);
				request.setAttribute("PART_INFO", part);

				getServletContext().getRequestDispatcher("/cm/reg_item2.jsp").forward(request,response);
			}

			///////////////////////////
			// ���õ� ǰ������ �󼼺���
			///////////////////////////
			else if(mode.equals("view_item")){
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.business.CodeLinkUrlBO clinkBO = new com.anbtech.cm.business.CodeLinkUrlBO(con);

				part = cmBO.getViewForm(item_no);
				request.setAttribute("PART_INFO", part);

				ArrayList spec_list = new ArrayList();
				spec_list = cmBO.getSpecList(mode,code_small,item_no);
				request.setAttribute("SPEC_LIST",spec_list);

				com.anbtech.cm.business.CodeLinkUrlBO redirectBO = new com.anbtech.cm.business.CodeLinkUrlBO(con);
				com.anbtech.cm.entity.CodeLinkUrl redirect = new com.anbtech.cm.entity.CodeLinkUrl();
				redirect = redirectBO.getRedirectForView(mode,category,item_no,searchword,searchscope,page,login_id);
				request.setAttribute("Redirect",redirect);
				
				// ÷������ link String ��������
				String downfile = clinkBO.getDownFileLink(item_no);
				request.setAttribute("DOWN_FILE", downfile);

				getServletContext().getRequestDispatcher("/cm/view_item.jsp").forward(request,response);
			}

			//////////////////////////////////////////////////
			// ���õ� ǰ������ �󼼺���(F/G & Assay �󼼺��� LINK)
			//////////////////////////////////////////////////
			else if(mode.equals("view_item2")){
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.business.CodeLinkUrlBO clinkBO = new com.anbtech.cm.business.CodeLinkUrlBO(con);

				part = cmBO.getViewForm(item_no);
				request.setAttribute("PART_INFO", part);

				com.anbtech.cm.business.CodeLinkUrlBO redirectBO = new com.anbtech.cm.business.CodeLinkUrlBO(con);
				com.anbtech.cm.entity.CodeLinkUrl redirect = new com.anbtech.cm.entity.CodeLinkUrl();
				redirect = redirectBO.getRedirectForView(mode,category,item_no,searchword,searchscope,page,login_id);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/cm/view_item2.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////
			// ǰ��Ӽ� ������ ���� ǰ��˻�â
			////////////////////////////////
			if(mode.equals("search_by_spec")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

				//ǰ��з� ��������
				part = cmBO.getPartRegForm(mode,code_big,code_mid,code_small,item_no);
				request.setAttribute("PART_INFO",part);

				//���õ� �ߺз� �Ǵ� �Һз� ǰ�� ���ǵ� ���帮��Ʈ ��������
				ArrayList spec_list = new ArrayList();
				spec_list = cmBO.getSpecList(code_mid,code_small);
				request.setAttribute("SPEC_LIST",spec_list);

				getServletContext().getRequestDispatcher("/cm/search_item.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// �ߺз� ǰ��(��ǰ)�� ǥ�� ���ø� �����׸� �߰� �� ���� ��
			/////////////////////////////////////////////////////
			else if(mode.equals("add_template_code") || mode.equals("modify_template_code")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.SpecCodeTable table = new com.anbtech.cm.entity.SpecCodeTable();

				//���õ� �����ڵ忡 ���� ������ �����´�.
				table = cmBO.getAddStdTemplateCodeForm(mode,code_big,code_mid,spec_code);
				request.setAttribute("SPEC_INFO",table);

				//���õ� �з��� �����ϴ� ǥ�����ø� ���帮��Ʈ�� �����´�.
				ArrayList spec_list = new ArrayList();
				spec_list = cmBO.getStdTemplateSpecList(code_mid);
				request.setAttribute("SPEC_LIST", spec_list);

				getServletContext().getRequestDispatcher("/cm/admin/add_template_code.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			// ǰ�� tree ��¿� ��ũ��Ʈ ���� ����
			/////////////////////////////////////////////////////
			else if(mode.equals("make_tree")){
			  com.anbtech.cm.business.makeCodeTreeItems tree	 = new com.anbtech.cm.business.makeCodeTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/cm/admin/";
				tree.makeCodeTree(output_path + "code_tree_items.js","CodeMgrServlet",1,0);

				getServletContext().getRequestDispatcher("/cm/admin/make_tree.jsp").forward(request,response);
			}

			/*******************************************************
			 * ÷������ �ٿ�ε� ó��
			 ********************************************************/
			else if ("download".equals(mode)){
				
				//���� �̵��ϴ� �� ���� �ٿ�ε��Ų��.
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.entity.PartInfoTable file = new com.anbtech.cm.entity.PartInfoTable();
				file = cmBO.getFile_fordown(item_no);
				String filename = file.getFileName();
				String filetype = file.getFileType();
				String filesize = file.getFileSize();

				//boardpath ���� ���ϱ��� ��� ����
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/cm/" + item_no + ".bin";
			
				if (filetype.indexOf("mage")<=0)
					filetype = "application/unknown";
				
				String strClient=request.getHeader("User-Agent");

				///////////////////////////////////////////////////////////
				//���ϸ��� �Ʒ��� ���� ����ó�� ������ �ѱ��� ������ �ʾ���.
				///////////////////////////////////////////////////////////
				filename = new String(filename.getBytes("euc-kr"),"8859_1"); 

				if(strClient.indexOf("MSIE 5.5")>-1) response.setHeader("Content-Disposition","filename="+filename);
				else response.setHeader("Content-Disposition","attachment; filename="+filename);
				
				//////////////////////////////////////////////////////////////////
				//db�� mysql�� ���� �Ʒ� �������� ��ġ���� �� �ѱ��� ������ �ʾ���.
				//response.setHeader("Content-Type", "application/octet-stream;"); 
				//response.setHeader("Content-Disposition", "attachment; filename=" + filename + ";"); 
				//////////////////////////////////////////////////////////////////

				response.setContentType(filetype);
				response.setContentLength(Integer.parseInt(filesize));
					
				byte b[] = new byte[Integer.parseInt(filesize)];
				java.io.File f = new java.io.File(downFile);
				java.io.FileInputStream fin = new java.io.FileInputStream(f);
				ServletOutputStream fout = response.getOutputStream();
				fin.read(b);
				fout.write(b,0,Integer.parseInt(filesize));
				fout.close();

			}

			/****************************************************
			* ǰ�� �����ϱ�	
			*****************************************************/
			else if (mode.equals("delete_item"))  {
				con.setAutoCommit(false);	// Ʈ������� ����
				try{
					//1.÷������ �����ϱ�
					com.anbtech.cm.db.CodeMgrDAO codeMgrDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
					String mid = codeMgrDAO.getMid(item_no);
		

					String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/cm/";
						for(int i=1; i<10; i++){
							java.io.File f = new java.io.File(filepath + "/" + item_no + "_" + i + ".bin");
							if(f.exists()) f.delete();
						}
					
					codeMgrDAO.deleteItem(mid);
							
					con.commit(); // commit�Ѵ�.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "CodeMgrServlet?mode=list_item";
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

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/cm/";

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

		//ǰ��ä���Ƿڰ���
		String item_no			= multi.getParameter("item_no");	//ǰ���ȣ
		String item_desc		= multi.getParameter("item_desc");	//ǰ�� Description
		String mfg_no			= multi.getParameter("mfg_no");		//��ü�ڵ�
		String code_str			= multi.getParameter("code_str");	//�ش� �Һз��� ���ǵ� �󼼽����ڵ� (32001,32002,32003,....)
		String spec_str			= multi.getParameter("spec_str");	//�󼼽��� �ڵ�|��|���� (32001|IDT74FCT165|na,32002|33|EA...)
		String item_type		= multi.getParameter("item_type");	//ǰ�����(or ASSY����)
		if(item_type==null)	item_type = "";
		String config_name		= multi.getParameter("config_name");//�����
		String item_name		= multi.getParameter("item_name");	//ǰ���
		String stock_unit		= multi.getParameter("stock_unit");	//ǰ��������
		String where_assy		= multi.getParameter("where_assy");	//ǰ�����
		if(item_type.equals("PH")) where_assy = "9";
	
		//ǰ�� ǥ�����ø� ��������
		String code_big			= multi.getParameter("code_big");
		String code_mid			= multi.getParameter("code_mid");
		String code_small		= multi.getParameter("code_small");
		String spec_code		= multi.getParameter("spec_code");
		String spec_name		= multi.getParameter("spec_name");
		String spec_value		= multi.getParameter("spec_value");
		String spec_unit		= multi.getParameter("spec_unit");
		String write_exam		= multi.getParameter("write_exam");
		String spec_desc		= multi.getParameter("spec_desc");

		//ä��ü�谡 ��з�(1)+�ߺз�(2)+�Һз�(2) �� �ƴҰ�쿡�� �Ʒ� 3������ �����ؾ� ��.
		if(item_no != null && !item_no.equals("")){
			code_big	= item_no.substring(0,1);
			code_mid	= item_no.substring(0,3);
			code_small	= item_no.substring(0,5);
		}

		//����ǰ�ڵ� ��Ͻÿ� �߰��Ǵ� �Ķ���͵�
		String one_class	= multi.getParameter("one_class");	//��ǰ���ڵ�
		String two_class	= multi.getParameter("two_class");	//��ǰ�ڵ�
		if (one_class == null) one_class = "";
		if (two_class == null) two_class = "";
		String model_code	= multi.getParameter("model_code");	 //���ڵ�
		String model_name	= multi.getParameter("model_name");	 //�𵨸�
		String reg_type		= multi.getParameter("reg_type");	 //�ű� or �Ļ�

		//ASS'Y�ڵ� ��Ͻ� �߰��Ǵ� �Ķ���͵�
		String op_codes		= multi.getParameter("op_codes");	 //�����ڵ�
		String assy_type	= multi.getParameter("assy_type");	 //ASSY TYPE

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

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			////////////////////
			// ǰ�� ���ó��
			////////////////////
			if(mode.equals("reg_item")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				//ǰ���ȣ(item_no)����
				item_no = cmDAO.calculateItemNo(code_small);

				//�ߺ�ǰ�� ��Ͽ��� üũ
				String same_item = cmBO.checkSameItemExist(item_no,code_small,mfg_no,spec_str);
				if(same_item.length() > 1){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('���ϼӼ��� ǰ���� 1�� �����մϴ�. ����۾��� ����� �� �����ϴ�.\\n\\n" + same_item + "');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}else{
					//db����
					cmDAO.savePartInfo(item_no,item_desc,mfg_no,item_name,item_type,stock_unit,spec_str,login_id);
					com.anbtech.cm.entity.PartInfoTable file = new com.anbtech.cm.entity.PartInfoTable();
					//÷������ ���δ�
					file = cmBO.getFile_frommulti(multi, item_no, filepath);
					//���ε� �� ÷������ ������ DB�� �����ϱ�
					cmBO.updFile(item_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileUmask());
				}
				getServletContext().getRequestDispatcher("/cm/reg_result.jsp?item_no=" + item_no).forward(request,response);
			}

			////////////////////
			// ǰ�� ����ó��
			////////////////////
			else if(mode.equals("reg_item2")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				//  UPDATE
				cmDAO.updateItemInfo(item_no,item_desc,item_type,stock_unit);								
				
				redirectUrl = "CodeMgrServlet?mode=view_item2&item_no="+item_no;
			}

			////////////////////
			// ǰ�� ����ó��
			////////////////////
			else if(mode.equals("modify_item")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

/* ǰ������ ������ ǰ��԰ݿ��� ������ �����ϴ� ��쿡�� �ߺ�ǰ������ üũ�� �Ǿ� ������
   �ȵǴ� ����� �����ÿ��� �ߺ�ǰ�� üũ�� ���� ����

				//�ߺ�ǰ�� ��Ͽ��� üũ
				String same_item = cmBO.checkSameItemExist(item_no,code_small,mfg_no,spec_str);
				if(same_item.length() > 1){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('���ϼӼ��� ǰ���� 1�� �����մϴ�. ����۾��� ����� �� �����ϴ�.\\n\\n" + same_item + "');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}else{
					//db update
					cmDAO.updatePartInfo(item_no,item_desc,mfg_no,spec_str,login_id);
				}
*/
				cmDAO.updatePartInfo(item_no,item_desc,mfg_no,item_name,item_type,stock_unit,spec_str,login_id);

				// ȭ������ ��������
				ArrayList file_list = new ArrayList();
				file_list = cmDAO.getFile_list(item_no);

				//  ÷�� ȭ�� ����
				com.anbtech.cm.entity.PartInfoTable file = new com.anbtech.cm.entity.PartInfoTable();
				//  ÷������ ���δ�
				file = cmBO.getFile_frommulti(multi, item_no, filepath, file_list);
				//  ���ε� �� ÷������ ������ DB�� �����ϱ�
				cmBO.updFile(item_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileUmask());

				redirectUrl = "CodeMgrServlet?mode=view_item&item_no="+item_no;
			}

			////////////////////
			// ����ǰ �ڵ��� ó��
			////////////////////
			else if(mode.equals("reg_fg")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				//�Ļ���ȣ ���
				//�ű��ϰ��� �ߺ���Ͽ��θ� üũ�ϰ�, �Ļ��ϰ��� �Ļ���ȣ�� �����´�.
				String derive_code = "00";
				if(reg_type.equals("n")){
					String same_item = cmDAO.getItemNoByModelCode(code_big,model_code);

					if(same_item.length() > 1){
						PrintWriter out = response.getWriter();
						out.println("<script>");
						out.println("alert('�����Ͻ� ���ڵ�(" + model_code + ")�� ����ǰ �ڵ尡 1�� �����մϴ�. ����۾��� ����� �� �����ϴ�.\\n\\n" + same_item + "');");
						out.println("history.go(-1);");
						out.println("</script>");
						out.close();
						return;		
					}
				}else if(reg_type.equals("d")){
					derive_code = cmDAO.getDeriveCode(code_big,model_code);

					if(derive_code.equals("00")){
						PrintWriter out = response.getWriter();
						out.println("<script>");
						out.println("alert('�����Ͻ� ���ڵ�(" + model_code + ")�� �ο��� ��ǥ ����ǰ �ڵ尡 �����ϴ�. ��ǥ ����ǰ �ڵ带 ���� ����Ͻʽÿ�.');");
						out.println("history.go(-1);");
						out.println("</script>");
						out.close();
						return;								
					}
				}

				//gcode�� ������ code ���� �����´�.
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				one_class = goodsDAO.getCodeByGcode(one_class);
				two_class = goodsDAO.getCodeByGcode(two_class);

				//�Ϸù�ȣ ���
				String serial_no = cmDAO.getSerialNo(code_big,one_class,two_class,model_code,derive_code);

				//����ǰ�ڵ� ����
				item_no = code_big + one_class +  two_class + serial_no + derive_code;

				//description ����
				com.anbtech.gm.entity.GoodsInfoTable table = new com.anbtech.gm.entity.GoodsInfoTable();
				table = goodsDAO.getGoodsInfoByCode(model_code);
				model_name = table.getGoodsName();

				item_desc = "F/G," + model_code + "," + model_name;

				//db ����
				cmDAO.saveFgInfo(model_code,code_big,one_class,two_class,serial_no,derive_code,item_no,item_desc,login_id,item_name,stock_unit,item_type);

				String item_nos = item_no + "(" + item_desc + ")";
				request.setAttribute("ITEM_NOS",item_nos);

				getServletContext().getRequestDispatcher("/cm/reg_result.jsp?item_no=" + item_no).forward(request,response);
			}

			////////////////////
			// ASS'Y �ڵ��� ó��
			////////////////////
			else if(mode.equals("reg_assy")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
									
				//gcode�� ������ code ���� �����´�.
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				one_class = goodsDAO.getCodeByGcode(one_class);
				two_class = goodsDAO.getCodeByGcode(two_class);

				//�Ѿ�� �����ڵ带 �޸�(,)�� �����Ͽ� ������ �����ڵ带 �����Ͽ� ó��
				StringTokenizer str = new StringTokenizer(op_codes, ",");
				String item_nos = "";
				while(str.hasMoreTokens()){
					String op_code = str.nextToken();

					//�Ϸù�ȣ ���
					String serial_no = cmDAO.getSerialNo(code_big,one_class,two_class,model_code);

					//ASS'Y�ڵ� ����
					//item_no = code_big + assy_type + one_class +  serial_no + op_code;
					if(item_type.equals("PH")){
						item_no = "1PH" + one_class + serial_no + op_code;			
					}else{
						item_no = "1" + item_type + assy_type + one_class + serial_no + op_code;
					}

					//description ����
					com.anbtech.gm.entity.GoodsInfoTable table = new com.anbtech.gm.entity.GoodsInfoTable();
					table = goodsDAO.getGoodsInfoByCode(model_code);
					model_name = table.getGoodsName();

					item_desc = cmBO.getDescForAssy(item_type,assy_type,op_code,model_code,model_name,config_name);

					//db ����
					cmDAO.saveAssyInfo(model_code,code_big,one_class,two_class,serial_no,assy_type,op_code,item_no,item_desc,login_id,where_assy,item_name,stock_unit);
					
					//
					item_nos += "\\n" + item_no + " (" + item_desc + ")";
					request.setAttribute("ITEM_NOS",item_nos);
				}

				getServletContext().getRequestDispatcher("/cm/reg_result.jsp?item_no=" + item_nos).forward(request,response);
			}

			/////////////////////////////////
			// ǰ�� ǥ�����ø����� �߰� ó��
			/////////////////////////////////
			else if (mode.equals("add_template_code")){
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				cmDAO.saveSpecInfo(code_mid,spec_code,spec_name,spec_value,spec_unit,write_exam,spec_desc);

				redirectUrl = "CodeMgrServlet?mode=add_template_code&code_big="+code_big+"&code_mid="+code_mid;

			}

			/***********************************************************
			 * ��з� ��ǰ�� ���� �����׸� ���� ó��(������ ���)
			 ***********************************************************/
			else if (mode.equals("modify_template_code")){
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				cmDAO.updateSpecInfo(code_mid,spec_code,spec_name,spec_value,spec_unit,write_exam,spec_desc);

				redirectUrl = "CodeMgrServlet?mode=add_template_code&code_big="+code_big+"&code_mid="+code_mid;
			}

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
