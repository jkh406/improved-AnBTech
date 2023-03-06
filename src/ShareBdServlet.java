/***************************************
 ShareBdServlet.java 
 #. �������� ������ ����� ��Ʈ��
****************************************/
import com.anbtech.share.entity.*;
import com.anbtech.share.db.*;
import com.anbtech.share.business.*;
import com.anbtech.text.Hanguel;

import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ShareBdServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;

	/********
	 * �Ҹ���
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************************************
	 * get������� �Ѿ���� �� ó��								  *
	 *********************************************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

	//�ʿ��� �͵� ����
	response.setContentType("text/html;charset=euc-kr");
	HttpSession session = request.getSession(true);

	String tablename	= request.getParameter("tablename");	//tablename ���� �������� ����(�������,���ȼ�,�Ϲݹ���)	
	String no			= request.getParameter("no");			//master_data ���̺��� ���ڵ� ������ȣ
	String ver_code		= request.getParameter("ver");			//�����ڵ�
	String mode			= request.getParameter("mode");			//���
	String page			= request.getParameter("page");			//������
	String umask		= request.getParameter("umask");		//Rename����

	// ����Ȯ�� ����
	boolean bool =  false;

	//�˻��ÿ� �Ѿ���� �Ķ���͵�
	String searchword	= request.getParameter("searchword");	//�˻���
	String searchscope	= request.getParameter("searchscope");	//�˻��ʵ�
	String category		= Hanguel.toHanguel(request.getParameter("category"));	//ī�װ� �ڵ�
	String boardpage	= request.getParameter("boardpage");	//
			
	if (mode == null) mode = "list";  			//ó������ mode�� �� �Ѿ���Ƿ� mode�� list�� ����
	if (page == null) page = "1";
	if (searchword == null) searchword = "";
	else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);

	if (searchscope == null) searchscope = "";
	if (category == null)		category = "";
	if (tablename == null)	   tablename = "com_rule";	// ó�� ���� Table ���(com_rule)����

	String redirectUrl = "";

	// ���� �������� ����� ���̵� ��������
	// ������ ����Ǿ��� ��� �α� �������� ���� �̵���Ų��.
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
	String login_name = sl.name;
	String login_division = sl.division;


	try {
		// conn ����
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
		
		
		com.anbtech.share.db.ShareBdDAO shbDAO = new com.anbtech.share.db.ShareBdDAO(con);
		com.anbtech.share.entity.ShareParameterTable sbpara = new com.anbtech.share.entity.ShareParameterTable();
		String categorycombo =  
			shbDAO.getCategoryItem(tablename, mode); //ī�װ�:SELECT ComboBox�� String���� �����

		sbpara.setTableName(tablename);		// ���� ����Ǵ� ���̺� name 
		sbpara.setMode(mode);				// ���� ���
		sbpara.setCategory(category);		// ���� ī�װ�
		sbpara.setId(login_id);				// ���� login_id���� setting	
		sbpara.setName(login_name);			// ���� login_name���� setting	
		sbpara.setCategoryCombo(categorycombo);		// ī�װ� SELECT ComboBOX
		sbpara.setSearchScope(searchscope);	// �˻����� 
		
		//	����üũ( ���/����/���� ������ ��������� Ȯ�� )
		com.anbtech.share.business.ShareBdBO sbdBO = new com.anbtech.share.business.ShareBdBO(con);
		bool = (boolean)sbdBO.adminValid(login_id, tablename);	
		sbpara.setBool(bool);
		
		request.setAttribute("sbParameter", sbpara);

		////////////////////////////////////////////////////////////
		//  �� ������ ��Ϻ��� 
		//	1. ���� ��������
		//	   file link �����, ����(subjec)�� '����������' link�ޱ�
		//	2. paging (����¡, ����Ʈ��, ������, ���� link �����)
		////////////////////////////////////////////////////////////
		if("list".equals(mode)) {
			
			com.anbtech.share.db.ShareBdDAO				 sbdDAO = new com.anbtech.share.db.ShareBdDAO(con);
			com.anbtech.share.business.ShareLinkBO	   shLinkBO	= new com.anbtech.share.business.ShareLinkBO(con);
			com.anbtech.share.entity.ShareLinkTable shLinkTable = new com.anbtech.share.entity.ShareLinkTable();
			ArrayList arry = new ArrayList();
			
			arry = sbdDAO.getShareBdList(tablename,mode,searchword,searchscope,category,page);		 // ���� list
			
			shLinkTable = shLinkBO.getRedirect(tablename,mode,searchword,searchscope,category,page,no); // paging	
			
			request.setAttribute("Arry",arry);
			request.setAttribute("Redirect",shLinkTable);
			
			// �� �ش� ������ �б�
			getServletContext().getRequestDispatcher("/share/"+tablename+"/list.jsp").forward(request,response);

		/////////////////////////////////////////////////////////
		//   ������ �������� 
		//	 #. ��ȸ�� counting    
		/////////////////////////////////////////////////////////
		} else if("view".equals(mode)) {

			com.anbtech.share.db.ShareBdDAO				 sbdDAO	= new com.anbtech.share.db.ShareBdDAO(con);
			com.anbtech.share.business.ShareBdBO		   shBO	= new com.anbtech.share.business.ShareBdBO(con);
			com.anbtech.share.business.ShareLinkBO	   shLinkBO	= new com.anbtech.share.business.ShareLinkBO(con);
			com.anbtech.share.entity.ShareLinkTable shLinkTable = new com.anbtech.share.entity.ShareLinkTable();
			com.anbtech.share.entity.ShareBdTable		sbTable	= new com.anbtech.share.entity.ShareBdTable();
			ArrayList arry = new ArrayList();
			
			if("view".equals(mode))	sbdDAO.countingCheck(tablename,no);	// ��������� ��ȸ�� ����(count) 
			
			shLinkTable = shLinkBO.getRedirect(tablename,mode,searchword,searchscope,category,page,no);	// link url			
			sbTable = shBO.getWrite_form(tablename,mode,no,login_id);			// ���� ��������
			arry = (ArrayList)sbdDAO.getFile_list(tablename,no);				// ÷��ȭ�� list

			request.setAttribute("sharefile",arry);
			request.setAttribute("Redirect",shLinkTable);
			request.setAttribute("shareBdTable",sbTable);
			getServletContext().getRequestDispatcher("/share/"+tablename+"/view.jsp").forward(request,response);
					
		////////////////////////////////////////////////////////
		//	�� ������ ���/���� ȭ�� 
		////////////////////////////////////////////////////////
		} else if("write".equals(mode) || "modify".equals(mode)){
		
		com.anbtech.share.business.ShareBdBO shBO			= new com.anbtech.share.business.ShareBdBO(con);
		com.anbtech.share.db.ShareBdDAO	shDAO				= new com.anbtech.share.db.ShareBdDAO(con);
		com.anbtech.share.entity.ShareBdTable shTable		= new com.anbtech.share.entity.ShareBdTable();
		com.anbtech.share.business.ShareLinkBO shLinkBO		= new com.anbtech.share.business.ShareLinkBO(con);
		com.anbtech.share.entity.ShareLinkTable shLinkTable = new com.anbtech.share.entity.ShareLinkTable();

		shTable = shBO.getWrite_form(tablename,mode,no,login_id);	// ���� ��������
		ArrayList arry = new ArrayList();							// ÷��ȭ������ ArrayList

		shLinkTable = shLinkBO.getRedirect(tablename,mode,searchword,searchscope,category,page,no);	// link url							
			
		if ("modify".equals(mode)){
			arry = (ArrayList)shDAO.getFile_list(tablename,no);		// ÷��ȭ�� ���� ��������
			request.setAttribute("sharefile",arry);
		}	
				
		request.setAttribute("Redirect",shLinkTable);	
		request.setAttribute("shareBdTable",shTable);
		getServletContext().getRequestDispatcher("/share/"+tablename+"/write.jsp").forward(request,response);				

		///////////////////////////////////////////////////////////////
		// ���� ���� ó��
		// #. �˻��� �ʿ��� ���� �� �ѱ� encoding ����
		//    servlet -> jsp �ѱ� ó�� ����,
		//    servlet -> jsp -> servlet -> servlet 
		//             1)    2)          3)
		//    3)������  1),2)���� �ߺ��� �ѱ� ó�������� 3)���� �Ѿ�
		//    ������ �ѱ��� ������ �ȵ�(����).
		//	   - solution : 2)���� parameter�� �ѱ涧 encoding(EUC-KR)��
		//                  �ѱ�(������)�� �ٽ� encoding(8859_1)�Ͽ� �ذ�
		///////////////////////////////////////////////////////////////
		} else if ("delete".equals(mode))	{
				
			com.anbtech.share.db.ShareBdDAO shBdDAO = new com.anbtech.share.db.ShareBdDAO(con);
			shBdDAO.deleteDoc(no,tablename);
			String ns = new String(category.getBytes("EUC_KR"),"8859_1");
			redirectUrl = "ShareBdServlet?tablename="+tablename+"&mode=list&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+ns;				
				
		////////////////////////////////////////////////////////
		//  download 
		////////////////////////////////////////////////////////
		} else if ("download".equals(mode)){
				
			com.anbtech.share.business.ShareBdBO shareBO = new com.anbtech.share.business.ShareBdBO(con);
			com.anbtech.share.entity.ShareBdTable file = new com.anbtech.share.entity.ShareBdTable();
			file = shareBO.getFile_fordown(tablename, no);
			String filename = file.getFname();
			String filetype = file.getFtype();
			String filesize = file.getFsize();
			
			//boardpath ���� ���ϱ��� ��� ����
			String downFile = getServletContext().getRealPath("") + "/upload/share/" + tablename + "/" + no + ".bin";
		
			if (filetype.indexOf("mage")<=0)
				filetype = "application/unknown";
			
			String strClient=request.getHeader("User-Agent");
			filename = new String(filename.getBytes("euc-kr"),"8859_1");

			if(strClient.indexOf("MSIE 5.5")>-1) 	response.setHeader("Content-Disposition","filename="+filename);
			else response.setHeader("Content-Disposition","attachment; filename="+filename);

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
		//redirectUrl�� ���� �����ÿ��� redirectUrl��η� �̵��Ѵ�.
		if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

	}catch (Exception e){
		request.setAttribute("ERR_MSG",e.toString());
		getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
	}finally{
		close(con);
	}
	} // doGet()


	/******************************************************
	 * post������� �Ѿ���� �� ó��						  *
	 *****************************************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		//���ε��� �� �ʿ��� �͵� tablename,upload_size, filepath ����
		String tablename	= request.getParameter("tablename");
		String upload_size	= request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";
		
		String filepath = getServletContext().getRealPath("") + "/upload/share/"+tablename+"/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//������������ �� �޾ƿ´�. multi���� ������
		String  mode			= multi.getParameter("mode");
		String  page			= multi.getParameter("page");
		String  searchword		= multi.getParameter("searchword");
		String  searchscope		= multi.getParameter("searchscope");
		String  category_id		= multi.getParameter("category");	//  ī�װ� �ڵ�
		String	no				= multi.getParameter("no");			//  ������ȣ 		
		String	subject			= multi.getParameter("subject");	//	����	
		String	ver				= multi.getParameter("ver");		//	����	
		String	wid				= multi.getParameter("wid");		//	�����ID	
		String	wname			= multi.getParameter("wname");		//	������̸�	
		String	wdate			= multi.getParameter("wdate");		//	�����		
		String	doc_no			= multi.getParameter("doc_no");		//	��Ϲ��� ��ȣ	
		String	ac_code			= multi.getParameter("ac_code");	//	�μ�Code		
		String	ac_name			= multi.getParameter("ac_name");	//	�μ���		
		String	category		= multi.getParameter("category");	//	ī�װ�		
		String	content			= multi.getParameter("content");	//	����			
		String	fname			= multi.getParameter("fname");		//	ȭ���̸�		
		String	fsize			= multi.getParameter("fsize");		//	ȭ�ϻ�����	
		String	ftype			= multi.getParameter("ftype");		//	ȭ��Ÿ��		
		String  fumask			= multi.getParameter("fumask");		//  Rename file 
		String  cnt				= 
					multi.getParameter("cnt")==null?"0":multi.getParameter("cnt");//	��ȸ��		
		String  mid				= multi.getParameter("mid");
		String  mname			= multi.getParameter("mname");

		//�������� �Ѿ���ų� null�� �� ���鿡 ���� ó��.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";
		if (no == null) no = "";
		
		String redirectUrl = "";

		//���� �������� ����� ���̵� ��������
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	alert('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

	try {
		// con����
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	
		//////////////////////////////////////////////////
		//	���� �ű� ��� ó��
		//////////////////////////////////////////////////
		if ("write".equals(mode)){

			String t_id = "";  // DB TABLE�� ������ȣ �ӽ� ���庯��

			com.anbtech.share.business.ShareBdBO shBdBO = new com.anbtech.share.business.ShareBdBO(con);
			com.anbtech.share.db.ShareBdDAO shBdDAO = new com.anbtech.share.db.ShareBdDAO(con);
			com.anbtech.share.entity.ShareBdTable file = new com.anbtech.share.entity.ShareBdTable();
				
			// ���(���,�޴���)�������� ���� �Է�
			shBdDAO.saveData(tablename,subject,ver,login_id,login_name,doc_no,ac_name,category,content,"0");
						
			//data_id �� ver_code �� �ش��ϴ� ������ȣ�� �����´�.
			t_id = shBdDAO.getNo(tablename,doc_no,ver);
			
			//÷������ ���δ�
			file = (com.anbtech.share.entity.ShareBdTable)shBdBO.getFile_frommulti(multi, t_id, filepath);
			
			//���ε� �� ÷������ ������ DB�� �����ϱ�
			shBdBO.updFile(tablename, t_id, file.getFname(), file.getFtype(), file.getFsize(), file.getFpath());
			String ns = new String(category.getBytes("EUC_KR"),"8859_1");	// 8859_1�������� �ٽú�ȯ
			redirectUrl="ShareBdServlet?tablename="+tablename+"&mode=list&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+ns;
						
				
		///////////////////////////////////////////////////
		// ���� ���� ó��
		//////////////////////////////////////////////////
		} else if ("modify".equals(mode)){

			com.anbtech.share.business.ShareBdBO shBdBO = new com.anbtech.share.business.ShareBdBO(con);
			com.anbtech.share.db.ShareBdDAO shBdDAO = new com.anbtech.share.db.ShareBdDAO(con);
			com.anbtech.share.entity.ShareBdTable file = new com.anbtech.share.entity.ShareBdTable();
		
			//Table UPDATE
			shBdDAO.updTable(no,tablename,subject,ver,login_id,login_name,doc_no,ac_name,category,content);

			//multi���� ���������� �����ͼ� ó���Ѵ�.
			ArrayList file_list = shBdDAO.getFile_list(tablename,no);
			
			//���� ���δ�
			file = shBdBO.getFile_frommulti(multi, no, filepath, file_list);
				
			// ȭ������ SAVE
			shBdBO.updFile(tablename, no, file.getFname(), file.getFtype(), file.getFsize(), file.getFpath());
			String ns = new String(category.getBytes("EUC_KR"),"8859_1");
			
			page="1"; // ������ �ٷ� ù ������...

			redirectUrl = "ShareBdServlet?tablename="+tablename+"&mode=list&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+ns;
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