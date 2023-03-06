import com.anbtech.am.entity.*;
import com.anbtech.am.db.*;
import com.anbtech.am.business.*;
import com.anbtech.am.admin.*;
import com.anbtech.admin.entity.*;
import com.anbtech.text.Hanguel;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class AssetServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;

	/**********************************
	 * �Ҹ���
	 ***********************************/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó��
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException{

		// �ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		
		// ī�װ� ���� ����
		String mode				= request.getParameter("mode"); 
		String div				= request.getParameter("div")==null?"":request.getParameter("div");	// ī�װ� �Է�/����/����
		String c_no				= request.getParameter("c_no")==null?"0":request.getParameter("c_no");	// ī�װ� ������ȣ
		String ct_id			= request.getParameter("ct_id")==null?"0":request.getParameter("ct_id");	// ī�װ� ID
		String ct_level			= request.getParameter("ct_level");	// ī�װ� LEVEL
		String ct_parent		= request.getParameter("ct_parent");	// �θ� ī�װ�
		String ct_word			= request.getParameter("ct_word");	// ī�װ� ����
		String ct_name			= request.getParameter("ct_name");	// ī�װ� �̸�
		String dc_percent		= request.getParameter("dc_percent");	// ī�װ� ���� ����(%)
		String as_no			= request.getParameter("as_no")==null?"0":request.getParameter("as_no");	// �ڻ��ȣ
		String apply_dc			= request.getParameter("apply_dc");	// ���� ���� ���� (count)

		// ����/�̰���û����
		String w_id				= request.getParameter("w_id");						// ����� id 
		String w_name			= Hanguel.toHanguel(request.getParameter("w_name"));	// ����� �̸�()
		String w_rank			= Hanguel.toHanguel(request.getParameter("w_rank"));	// ����� �μ���	
		String u_id				= request.getParameter("u_id");						// �ڻ� ����� ����(id)
		String u_name			= Hanguel.toHanguel(request.getParameter("u_name"));	// �ڻ� ����� ����(�̸�)
		String u_rank			= Hanguel.toHanguel(request.getParameter("u_rank"));	// �ڻ� ����� �μ���
		String takeout_reason	= Hanguel.toHanguel(request.getParameter("takeout_reason"));	// �ڻ� ó�� ��û ����
		String out_destination	= request.getParameter("out_destination")==null?"":Hanguel.toHanguel(request.getParameter("out_destination"));	// ���ó/�������..	
		String as_status		= request.getParameter("as_status");					// �ڻ� ���� �ڵ�
		String o_status			= request.getParameter("o_status")==null?"o":request.getParameter("o_status") ;	// �ڻ� ��û ���� �ڵ�
		String as_statusinfo	= request.getParameter("as_statusinfo")==null? "":Hanguel.toHanguel(request.getParameter("as_statusinfo"));						
		String w_date			= request.getParameter("c_date");						// ��û����
		String wi_date			= request.getParameter("wi_date");					// �ݳ�ó������
		String in_date			= request.getParameter("in_date")==null?"0":request.getParameter("in_date");	// �ݳ�/�԰�����
		String udate			= request.getParameter("u_date");						
		String u_date			= request.getParameter("sdate")==null?"0":request.getParameter("sdate"); // �� ����(��û) ��������
		String tu_date			= request.getParameter("edate")==null?"0":request.getParameter("edate");
		String h_no				= request.getParameter("h_no");				// �ڻ� �̷� ����Ʈ ������ȣ
		String handle			= request.getParameter("handle");				// �ڻ� ���� ���� ���� �ڵ�
		String mode_temp		= request.getParameter("mode_temp")==null?"":request.getParameter("mode_temp");	// �б� �����ڵ�

		if(in_date.length()>8)  in_date= in_date.substring(0,4)+in_date.substring(5,7)+in_date.substring(8,10);
		if(u_date.length()>8)	u_date= u_date.substring(0,4)+u_date.substring(5,7)+u_date.substring(8,10);
		if(tu_date.length()>8)  tu_date= tu_date.substring(0,4)+tu_date.substring(5,7)+tu_date.substring(8,10);

		//��� �� ���� ������ �ĸ�����
		String page				= request.getParameter("page");
		String no				= request.getParameter("no");
		String t_id				= request.getParameter("t_id");
		
		// �ڻ� ��� �ڻ� ������� �Ķ���͵�
		String year				= request.getParameter("year");
		String month			= request.getParameter("month");
		String assetupdate		= request.getParameter("assetupdate")==null?"":request.getParameter("assetupdate"); // �ڻ� ������Ʈ ���ΰ���
		String value			= request.getParameter("value");

		//�˻��ÿ� �Ѿ���� �Ķ���͵�
		String searchword		= Hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope		= request.getParameter("searchscope");
		String category			= request.getParameter("category");
		String tablename		= "";

		if ( page == null) page = "1";
		if ( searchword == null) searchword = "";
		if ( searchscope == null) searchscope = "";

		String redirectUrl	= "";
		String pid	= request.getParameter("pid")==null?"":request.getParameter("pid");	 // �����ȣ
		String pid2	= request.getParameter("pid2")==null?"":request.getParameter("pid2");// �����ȣ

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
		String login_id			= sl.id;
		String login_name		= sl.name;
		String login_division	= sl.division;

		try {
			// conn ����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////////////////////////////////
			// �ڻ�з���Ȳ ����Ʈ ���
			/////////////////////////////////////////////////////
			if("category_list".equals(mode)) { 
				com.anbtech.am.admin.makeCtTree makeCtTree = new com.anbtech.am.admin.makeCtTree();
				StringBuffer sb = new StringBuffer();
				sb = makeCtTree.viewTree(1,1);
				request.setAttribute("CategoryList", sb);
				getServletContext().getRequestDispatcher("/am/admin/managerCategory.jsp").forward(request,response);
			}
						
			////////////////////////////////////////////////////
			//  �ڻ�з� �Է���(ī�װ� ���� ����/����/����/���)
			//  * ó��������(div) 
			//    f:�ֻ��� ī�װ� �߰�  a:�߰�, m:����, d:����
			////////////////////////////////////////////////////
			else if("category_info_view".equals(mode)){ 
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO	= new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsCategoryTable asCategoryTable = new com.anbtech.am.entity.AsCategoryTable();
				
				if( "a".equals(div) || "m".equals(div) || "d".equals(div) ) { // ī�װ� �߰�or����or����
					asCategoryTable = (com.anbtech.am.entity.AsCategoryTable)assetModuleDAO.getCtInfo(ct_id);//ī�װ�������������
					request.setAttribute("asCtTableM", asCategoryTable);
					getServletContext().getRequestDispatcher("/am/admin/inputCtForm.jsp?div="+div).forward(request,response);
				} else if("f".equals(div)) { // �ֻ��� ī�װ� �߰�
					getServletContext().getRequestDispatcher("/am/admin/inputCtForm.jsp?div="+div).forward(request,response);
				}
			}
					
			//////////////////////////////////////////////////////////
			//   �ڻ�����Ȳ ����Ʈ ���
			//	   (mode:'asset_list'  div: 'detail' or 'general')	
			//	 
			//	 - ���� List���� �������� ��û�� ��������
			//     �⺻�� �� Base�� ��������, 
			//     But, ���� ������ �ڻ��� 1��(12 ����)�� �Ѱܾ� ��������
			//   
			//   - �ڻ����� list ��û�� �˻����� ������ div
			//		(div: 'general'-�Ϲݰ˻�, 'detail'-�󼼰˻�)
			//////////////////////////////////////////////////////////
			else if("asset_list".equals(mode)){
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.am.db.AssetModuleDAO     assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable       asInfoTable = new com.anbtech.am.entity.AsInfoTable();			
				com.anbtech.am.admin.makeCtTree makeCtTree			= new com.anbtech.am.admin.makeCtTree();
				com.anbtech.am.business.AMLinkBO amLinkBO			= new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable		= new com.anbtech.am.entity.AMLinkTable();

				ArrayList searchList = new ArrayList();
				tablename = "as_info";
				String sb = "";					
				ct_id = assetModuleDAO.getCtId(c_no);

				//  ī�װ� �������� 
				sb = makeCtTree.viewCombo(1,1);             
					
				// ���� ���� '���� �ڻ� update ��û�� ����'
				// (parameter: �⵵(���س�), ��(���ش�), ��������ġ )
				if("update".equals(assetupdate)) 
					assetModuleBO.getAutoUpdate(year,month,value);
				
				// �˻���� �������� 
				searchList = assetModuleDAO.getAssetList(tablename,mode,searchword,searchscope,page,c_no,div,ct_id);
				
				// link String �� paging
				amLinkTable = amLinkBO.getRedirect(tablename,mode,searchword,searchscope,page,c_no,div,ct_id,login_division);
				
				request.setAttribute("CategoryList", sb);
				request.setAttribute("assetList", searchList);
				request.setAttribute("Redirect",amLinkTable);

				//	�ڻ� �Ѿ� �������� => ���� �������� ����.
				//	String value ="";
				//	value = assetModuleDAO.sumAsValue();
				getServletContext().getRequestDispatcher("/am/admin/managerAsset.jsp?c_no="+c_no).forward(request,response);
			}
						
			////////////////////////////////////////////////////////////////////
			//   �ڻ����� ��� �� ���� ��
			//   - ó�� ���� CODE (div)
			//		input:�����, modify:������, view:����, delete:������
			//      delete_view: ����ڻ� �������� ��    download: ÷��ȭ�ϴٿ�ε��
			////////////////////////////////////////////////////////////////////
			else if("asset_form".equals(mode)){
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO	= new com.anbtech.am.db.AssetModuleDAO(con);	// �ڻ����DAO
				com.anbtech.am.admin.makeCtTree makeCtTree		= new com.anbtech.am.admin.makeCtTree();
				com.anbtech.am.entity.AMUserTable amuserTable	= new com.anbtech.am.entity.AMUserTable();		// ���������
				com.anbtech.am.entity.AsCategoryTable asCategoryTable	= new com.anbtech.am.entity.AsCategoryTable();
				com.anbtech.am.entity.AsInfoTable asInfoTable	= new com.anbtech.am.entity.AsInfoTable();
				
				// * �ڻ� ���� ����� *
				// 1. �����(�����) ���� setting : jsp�� parameter �ѱ涧 �ѱ۱�����...
				// 2. ī�װ� ���� �������� (�Է������� select list BOX)
				// 3. �������� ���� �������� (�ش� ī�װ� �� ǰ�� ����Ǵ� ��������)
				// 4. ��������� �̵�
				if("input".equals(div)) { 
					String sb		= "";
					String percent	= "";
				
					// ����� ���� Setting
					amuserTable.setUserId(login_id);
					amuserTable.setUserName(login_name);
					amuserTable.setUserRank(login_division);
								
					// ī�װ� ���� ��������
					asCategoryTable=(com.anbtech.am.entity.AsCategoryTable)assetModuleDAO.getCtInfoByCno(c_no);
					
					// ������������ (�ڻ� ��Ͻ� ī�װ� �� ǰ�� ����������������)
					percent = asCategoryTable.getDcPercent();	
											
					sb = makeCtTree.viewCombo(1,1);	

					request.setAttribute("user",amuserTable);
					request.setAttribute("CategoryList", sb);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div=input&DcPercent="+percent+"&c_no="+c_no).forward(request,response);
			
				// * ���� ���� �� *
				// 1. ���� ����Ʈ ��������
				// 2. �ڻ����� ��������
				// 3. �ش� ǰ�� �� ī�װ� ��ü String ��������(��: "slink>�������>��ǻ��")
				} else if("view".equals(div)){
					String sb			= "";
					ArrayList arry		= new ArrayList();
					ArrayList file_list = new ArrayList();
					
					file_list = (ArrayList)assetModuleDAO.getFileList(as_no);		  // ���� ����Ʈ ����
					asInfoTable = 
					(com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no); // �ڻ� ����
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);
					sb = makeCtTree.viewCategory(c_no,"");							  // ī�װ� Ǯ-��Ʈ��

					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("file",file_list);
					request.setAttribute("assetfile",arry);
					request.setAttribute("CategoryList", sb);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div=view&c_no"+c_no+"&login_id="+login_id).forward(request,response);

				// ��� �ڻ� ����Ʈ���� ��������� ����...
				} else if("delete_view".equals(div)){
									
					ArrayList arry = new ArrayList();
					ArrayList file_list = new ArrayList();

					com.anbtech.am.db.AssetModuleDAO assetModuleDAO2 = new com.anbtech.am.db.AssetModuleDAO(con);
					file_list = (ArrayList)assetModuleDAO2.getFileList(as_no);
					asInfoTable = (com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no);
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);
					
					// ī�װ� Ǯ-��Ʈ��
					String sb = "";
					sb = makeCtTree.viewCategory(c_no,"");

					request.setAttribute("file",file_list);
					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("assetfile",arry);
					request.setAttribute("CategoryList", sb);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div="+div+"&c_no"+c_no+"&login_id="+login_id).forward(request,response);

					// * ���������� *
					// 1. ī�װ� select list BOX ������������
					// 2. �ڻ����� ��������
					// 3. ÷��ȭ������ ��������
				} else if("modify".equals(div)){
					ArrayList arry = new ArrayList();
					String sb = "";

					sb = makeCtTree.viewCombo(1,1);
					asInfoTable = (com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no);
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);

					request.setAttribute("CategoryList", sb);
					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("assetfile",arry);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div=modify&c_no="+c_no).forward(request,response);
				
					// * ������ ���������� *
					// 1. ī�װ� ������
					// 2. �ڻ����� ��������
					// 3. ÷��ȭ������ ��������
				}else if("delete".equals(div)){
					ArrayList arry = new ArrayList();
					ArrayList file_list = new ArrayList();
					String sb = "";

					sb = makeCtTree.viewCategory(c_no,"");								  // ī�װ�
					asInfoTable = 
						(com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no); // �ڻ�����
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);				  // ÷��ȭ������						
					
					request.setAttribute("CategoryList", sb);
					request.setAttribute("assetInfo",asInfoTable);						
					request.setAttribute("assetfile",arry);

					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?div=delete").forward(request,response);
				}else if ("download".equals(div)){ // AssetInfoForm.jsp���� ���������ϰ�� ÷��ȭ�� �ٿ�ε��Ҷ�...
					com.anbtech.am.business.AssetModuleBO assetBO = new com.anbtech.am.business.AssetModuleBO(con);
					com.anbtech.am.entity.AsInfoTable file = new com.anbtech.am.entity.AsInfoTable();
					
					file = assetBO.getFile_fordown(as_no); // as_no (��: 15_1,15_2.....)
								
					String filename = file.getFileName();
					String filetype = file.getFileType();
					String filesize = file.getFileSize();

					// �ٿ�ε� ���� ���
					String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/am/" + as_no + ".bin";

					if (filetype.indexOf("mage")<=0)
						filetype = "application/unknown";					
					
					String strClient=request.getHeader("User-Agent");

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
			}
		
			/////////////////////////////////////////////////	
			//	[ ������ ���	 ]
			//	��� �ڻ� ����Ʈ
			//  1. ī�װ����� ��������
			//  2. �˻����(��� �ڻ� ����) 
			//     - as_status�� '10'(���)�� �ڻ�
			/////////////////////////////////////////////////
			 else if("asset_del_list".equals(mode)){
					tablename = "as_info";

					com.anbtech.am.business.AssetModuleBO  assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
					com.anbtech.am.db.AssetModuleDAO      assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.entity.AsInfoTable		 asInfoTable = new com.anbtech.am.entity.AsInfoTable();	
					com.anbtech.am.business.AMLinkBO			amLinkBO = new com.anbtech.am.business.AMLinkBO(con);
					com.anbtech.am.entity.AMLinkTable		 amLinkTable = new com.anbtech.am.entity.AMLinkTable();
					com.anbtech.am.admin.makeCtTree			  makeCtTree = new com.anbtech.am.admin.makeCtTree();
					ArrayList searchList = new ArrayList();
					String sb = "";
					
					sb = makeCtTree.viewCombo(1,1);    // ī�װ� �������� 
						
					ct_id = assetModuleDAO.getCtId(c_no);
					//  �˻���� �������� 
					searchList = assetModuleDAO.getAssetList(tablename,mode,searchword,searchscope,page,c_no,div,ct_id);
					//  link String �� paging
					amLinkTable = amLinkBO.getRedirect(tablename,mode,searchword,searchscope,page,c_no,div,ct_id,login_division);
					
					request.setAttribute("CategoryList", sb);
					request.setAttribute("assetList", searchList);
					request.setAttribute("Redirect",amLinkTable);
														
					getServletContext().getRequestDispatcher("/am/admin/managerDelAsset.jsp?c_no="+c_no).forward(request,response);

				  
			////////////////////////////////////////////////////////////////
			//  [ ������ ���	 ]
			//	���Ǿ��� �ڿ� ����
			//  - as_info TABLE�� '������'CODE('10')�� ���� CODE('6')�� ����
			////////////////////////////////////////////////////////////////
			}	else if("asset_repair".equals(mode)){
				  
					com.anbtech.am.db.AssetModuleDAO  assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					assetModuleDAO.assetRepair(as_no);
					response.sendRedirect("AssetServlet?mode=asset_del_list");				


			/////////////////////////////////////////////////////////
			//  [ ������ ��� ]  
			//	DB�� ������ ���� ����(as_history TABLE)
			//  - ����/�̰�/�뿩 ��û��(as_status:'1') ������� ���� DATA��
			//    ��� ����(DELETE)
			////////////////////////////////////////////////////////
			}	else if("cleanDb".equals(mode)){
					com.anbtech.am.db.AssetModuleDAO  assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					assetModuleDAO.cleanDB();
					response.sendRedirect("AssetServlet?mode=asset_list");				
			

			/////////////////////////////////////////////////
			//	[ ����� ��� ]
			//	�ڻ� ����Ʈ
			//	1. ī�װ� ����Ʈ ��������(select list BOX)
			//  2. �ڻ����� ��������
			//	   - �ڻ����� list ��û�� �˻����� ������ div
			//		 (div: 'general'-�Ϲݰ˻�, 'detail'-�󼼰˻�)
			/////////////////////////////////////////////////
			}  else if("user_asset_list".equals(mode)){
					
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.admin.makeCtTree makeCtTree = new com.anbtech.am.admin.makeCtTree();
				com.anbtech.am.business.AMLinkBO amLinkBO = new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable = new com.anbtech.am.entity.AMLinkTable();
				
				String sb = "";
				tablename = "as_info";
				ArrayList searchList = new ArrayList();
				
				sb = makeCtTree.viewCombo(1,1);				// ī�װ� ��������
				ct_id = assetModuleDAO.getCtId(c_no);		// as_category���� ct_id��������

				// �ڻ�����
				searchList = assetModuleDAO.getAssetList(tablename,mode,searchword,searchscope,page,c_no,div,ct_id);
				
				// paging 
				amLinkTable = amLinkBO.getRedirect(tablename,mode,searchword,searchscope,page,c_no,div,ct_id,login_division);
				
				request.setAttribute("CategoryList", sb); 
				request.setAttribute("assetList", searchList);
				request.setAttribute("Redirect",amLinkTable);
				getServletContext().getRequestDispatcher("/am/as_user/managerAsReq.jsp?c_no="+c_no+"&login_id="+login_id).forward(request,response);

				
			/////////////////////////////////////////////////////
			//  [ ����� ��� ]
			//  ���õ� �ڻ�(1��)�� ������
			//	1. ī�װ� ����Ʈ
			//  2. ÷��ȭ�� ��ƮƮ ��������
			//  3. �ڻ����� ��������
			/////////////////////////////////////////////////////
			}	else if("user_asset_view".equals(mode)){
					
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
					com.anbtech.am.admin.makeCtTree makeCtTree = new com.anbtech.am.admin.makeCtTree();
					ArrayList arry = new ArrayList();
					ArrayList file_list = new ArrayList();
					String sb = "";

					sb = makeCtTree.viewCategory(c_no,"");							// ī�װ� ��Ʈ��
					file_list = (ArrayList)assetModuleDAO.getFileList(as_no);		// ÷������ ����Ʈ
					asInfoTable = (com.anbtech.am.entity.AsInfoTable)assetModuleDAO.getInfo(as_no);
					arry = (ArrayList)assetModuleDAO.getFileList(as_no);			

					request.setAttribute("CategoryList", sb);
					request.setAttribute("file",file_list);
					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("assetfile",arry);
					getServletContext().getRequestDispatcher("/am/admin/AssetInfoForm.jsp?mode=user_asset_view&div=view&c_no="+c_no+"&login_id="+login_id).forward(request,response);
			
				
			/////////////////////////////////////////////////
			//  [ ����� ��� ]
			//  �ڻ��� ��� ���� & �̷� ����
			//  1. ī�װ� ����Ʈ ��������
			//  2. �ڻ����� ��������(mode:'user_each_history' div:'each')
			//  3. paging(mode:'user_each_history' div:'each')
			/////////////////////////////////////////////////
			}	else if("user_each_history".equals(mode)){
					
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
					com.anbtech.am.admin.makeCtTree makeCtTree = new com.anbtech.am.admin.makeCtTree();
					com.anbtech.am.business.AMLinkBO amLinkBO = new com.anbtech.am.business.AMLinkBO(con);		// ��ũ����
					com.anbtech.am.entity.AMLinkTable amLinkTable = new com.anbtech.am.entity.AMLinkTable();	// ��ũ���� helper..

					ArrayList reqList = new ArrayList();
					String sb = "";
					tablename = "as_history";

					sb = makeCtTree.viewCategory(c_no,"");			// ī�װ� ��Ʈ��
					asInfoTable = assetModuleDAO.getInfo(as_no);	// �ڻ� ���� �������� 
					//�ڻ��� �̷� ����(����Ʈ) ��������
					reqList = assetModuleDAO.getInfoList(tablename,mode,login_id,searchscope,page,as_no,"each",login_division);
					// paging
					amLinkTable = amLinkBO.getRedirect(tablename,mode,login_id,c_no,page,as_no,"each",ct_id,login_division);

					request.setAttribute("CategoryList", sb);
					request.setAttribute("assetInfo",asInfoTable);
					request.setAttribute("assetReqList1",reqList);
					request.setAttribute("Redirect",amLinkTable);
					getServletContext().getRequestDispatcher("/am/as_user/eachHistory.jsp?login_id="+login_id).forward(request,response);

					
			/////////////////////////////////////////////////
			//	[ ����� ��� ]
			//  - �ڻ� ����/�̰�/�뿩 ��û��
			//  - ���õ� �ڻ��� ���� ��������
			//  - ��û������ �̵�
			/////////////////////////////////////////////////
			}	else if("user_moving_req".equals(mode)){
					
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
					com.anbtech.am.entity.AMUserTable amuserTable = new com.anbtech.am.entity.AMUserTable();
					// �����(��û �����) ���� SETTING
					amuserTable.setUserId(login_id);
					amuserTable.setUserName(login_name);
					amuserTable.setUserRank(login_division);
					
					asInfoTable = assetModuleDAO.getInfo(as_no);	// �ڻ� ���� �������� 

					request.setAttribute("user",amuserTable);
					request.setAttribute("assetInfo",asInfoTable);
					getServletContext().getRequestDispatcher("/am/as_user/each_moving_req.jsp?as_no="+as_no+"&o_status="+o_status).forward(request,response);

			//////////////////////////////////////////////////////////////////////////
			//  [ �����(�ڻ� ��/�� ������) ��� ] 
			//	
			//	�ڻ� ����/�̰�/�뿩 ���� ���� & ��� (as_status='2' => ���)
			//	  
			//	1. ����/�뿩 ���� ���� �˾ƺ���(��û���� �ߺ� CHECK)
			//     - ó�� ���� 
			//     - �Ұ� �޽���: msg-�Ұ� �޽���
			//
			//  2. * ������ ���
			//       - �ڻ� ��û���� ���(assetModuleDAO.saveAsHistory())
			//		   : ��û������� �� ��������� �ʿ��� ������ SELECT�ؼ� �迭�� ��ƿ´�.
			//		   (h_no-�̷����� ������ȣ, as_no-�ڻ� ������ȣ, o_status-��û�����ڵ�,     
			//          as_status-�ڻ�����ڵ�)
			//       - ��û ���� ����
			//		   : ���� ���� ������ confirm.jsp�̵� -> �������� ���� Ȯ��
			//     * �Ұ����� ���
			//       - ���� �������� �̵�
			//		
			//////////////////////////////////////////////////////////////////////////
			}   else if("user_asreq_process".equals(mode)){
				   
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new  com.anbtech.am.entity.AsHistoryTable();
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO		= new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);	// ����� ���� DB Access
				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable(); // ���������Helper..
				String msg ="enable";
				
				if(!tu_date.equals("0")) { 
					in_date = tu_date;	    
					msg = assetModuleBO.checkOut(u_date,in_date,as_no,o_status);  // ����/�뿩 ���� ���� �˾ƺ���
				} else {
					// msg = assetModuleBO.checkTranse(u_date,as_no);			  // �ڵ屸�� �ȵ�(2/18)
				}

				// ��� ������ ���
				if(msg.equals("enable")) {

				 // ���� �ڻ��� ����� ����� ���� �������� 
				 u_id	= (String)assetModuleBO.getUserInfo(u_name);
				 user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(u_id);
				 u_name = user_info.getUserName();
				 u_rank = user_info.getDivision();
				 u_name = u_id+"/"+u_name;

				 String[] app = new String[4];
				 String m_id = assetModuleDAO.getManagerId(as_no); // �ڻ��� ������ ����(as_info) �������� ID

				 // �����(�ڻ� ������) ���� ��������
				 user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(m_id);
				 String m_name = user_info.getUserName();
				 String m_rank = user_info.getDivision();
				 m_name = m_id+"/"+m_name;
				
				 // ��û���� ���
				 app=(String[])assetModuleDAO.saveAsHistory(as_no,m_id,m_name,m_rank,w_id, w_name, w_rank, u_id, u_name, u_rank ,takeout_reason ,out_destination ,as_status,o_status ,w_date ,u_date ,tu_date);

				 // ��������
				 getServletContext().getRequestDispatcher("/am/admin/confirm.jsp?h_no="+app[0]+"&as_no="+app[1]+"&o_status="+app[2]+"&as_status="+app[3]).forward(request,response);	
				
				} else {
						// �ƴϸ� ��û �Ұ��� �˷��ذ� �ڷ� �̵�
						PrintWriter out7 = response.getWriter();
						out7.println("	<script>");
						out7.println("		alert('"+msg+"');");
						out7.println("		history.back()");
						out7.println("	</script>");
						out7.close();
				}
			
				
			/////////////////////////////////////////////////
			//	[ ����� (�ڻ� ��û ��/�� ������) ��� ]
			//	�ڻ� ����/�̰� ��û ���� ��� LIST
			//  1. ���� ��� list (mode:'req_app_list'  div:'app')
			//  2. paging (mode:'req_app_list'  div:'app')
			//  3. ���縮��Ʈ ȭ������ �̵�
			/////////////////////////////////////////////////
			}	else if("req_app_list".equals(mode)){

				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AMLinkBO amLinkBO = new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable = new com.anbtech.am.entity.AMLinkTable();
				ArrayList reqList = new ArrayList();
				tablename = "as_history";
				
				// 1. ���õ� �ڻ��� �̷� ����(����Ʈ) ��������
				reqList = assetModuleDAO.getInfoList(tablename,mode,searchword,searchscope,page,as_no,"app",login_division);
				request.setAttribute("assetReqList1",reqList);
									
				// 2. paging
				amLinkTable = amLinkBO.getRedirect(tablename,mode,c_no,searchscope,page,as_no,"app",ct_id,login_division);
				request.setAttribute("Redirect",amLinkTable);
				
				// 3. �̵�
				getServletContext().getRequestDispatcher("/am/admin/req_app.jsp").forward(request,response);

			/////////////////////////////////////////////////
			//  [ ����� (�ڻ� ��û ��/�� ������) ��� ]
			//	2�� ���� ��û 
			/////////////////////////////////////////////////
			}	else if("req_app2".equals(mode)){
										
				PrintWriter out7 = response.getWriter();
				out7.println("	<script>");
				out7.println("	location.href('../gw/approval/module/Asset_ReApp.jsp?mode=app_asset_view&h_no="+h_no+"&as_no="+as_no+"&o_status="+o_status+"&as_status="+as_status+"');");
				out7.println("	</script>");
				out7.close();
					
			/////////////////////////////////////////////////
			//  [ ����� (�ڻ� ��û ��/�� ������) ��� ]
			//  �ڻ� �뿩 ���� ���� (�뿩 ����Ʈ)
			//  1. �ڻ� �뿩 �������� ���� list 
			//	   (mode:'lending_list' div='lending')
			//  2. paging (mode:'lending_list' div='lending')
			/////////////////////////////////////////////////
			}	else if("lending_list".equals(mode)){

				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AMLinkBO amLinkBO		= new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable	= new com.anbtech.am.entity.AMLinkTable();
				ArrayList reqList = new ArrayList();
				tablename = "as_history";

				// �ڻ��� ����(����Ʈ) ��������
				reqList = assetModuleDAO.getInfoList(tablename,mode,login_id,searchscope,page,as_no,"lending",login_division);
				// paging
				amLinkTable = amLinkBO.getRedirect(tablename,mode,login_id,searchscope,page,as_no,"lending",ct_id,login_division);
				
				request.setAttribute("assetReqList1",reqList);
				request.setAttribute("Redirect",amLinkTable);
				getServletContext().getRequestDispatcher("/am/admin/req_app_lending.jsp").forward(request,response);
				
			/////////////////////////////////////////////////
			//	[����� (�ڻ� ��û ��/�� ������) ���]
			//	�̰�/����/�뿩 ��û Form���� �̵�  
			//  1. �ڻ����� �������� 
			//  2. �ڻ��̷� ���� ��������
			//  3. ī�װ� ����
			//  4. ��û������ �̵�
			/////////////////////////////////////////////////
			}	else if("out_InputForm".equals(mode)){
					
				com.anbtech.am.entity.AMUserTable amuserTable	= new com.anbtech.am.entity.AMUserTable();
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable	= new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
				com.anbtech.am.admin.makeCtTree makeCtTree		= new com.anbtech.am.admin.makeCtTree();
				String sb = "";
				
				// ����� ���� setting�ؼ� �ѱ�� (? servlet ���� jsp�� �Ѿ�� �ѱ��� ������....)
				amuserTable.setUserId(login_id);
				amuserTable.setUserName(login_name);
				amuserTable.setUserRank(login_division);

				asInfoTable		= assetModuleDAO.getInfo(as_no);		// �ڻ� ���� �������� 
				asHistoryTable	= assetModuleDAO.getHistory(h_no);		// �̷�������������
				sb = makeCtTree.viewCategory(c_no,"");					// ī�װ� ����
				
				request.setAttribute("user",amuserTable);
				request.setAttribute("assetInfo",asInfoTable);
				request.setAttribute("historyInfo",asHistoryTable);
				request.setAttribute("CategoryList", sb);
				getServletContext().getRequestDispatcher("/am/admin/asEntering.jsp?as_no="+as_no+"&div="+div+"&as_status="+as_status).forward(request,response);	

			///////////////////////////////////////////////////////
			//	[����� ���] - ����/�뿩 �԰� ���� ���� (popup window)
			//  1. �ڻ����� ��������
			//  2. �̷����� ��������
			//  3. ī�װ� ���� ��������
			///////////////////////////////////////////////////////
			} else if ("entering_info".equals(mode)) {

				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable	= new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
				com.anbtech.am.admin.makeCtTree makeCtTree		= new com.anbtech.am.admin.makeCtTree();
				String sb = "";
				
				asInfoTable = assetModuleDAO.getInfo(as_no);		// �ڻ� ���� �������� 
				asHistoryTable = assetModuleDAO.getHistory(h_no);	// �̷�������������
				sb = makeCtTree.viewCategory(c_no,"");
				
				request.setAttribute("assetInfo",asInfoTable);
				request.setAttribute("historyInfo",asHistoryTable);
				request.setAttribute("CategoryList", sb);
				
				getServletContext().getRequestDispatcher("/am/as_user/entering_info.jsp?as_status="+as_status).forward(request,response);					
				
			//////////////////////////////////////////////////////////////////////
			//  [����� (�ڻ� ��û ��/�� ������) ���]
			//  #. �԰�/�ݳ�(div:lending) ����ó����list          mode : EnteringList 
			//  #. �̰�/����/�뿩(div:lending) ���� ó���� list    mode : TransOutList  
			//////////////////////////////////////////////////////////////////////
			}   else if("EnteringList".equals(mode) || "TransOutList".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AMLinkBO amLinkBO = new com.anbtech.am.business.AMLinkBO(con);
				com.anbtech.am.entity.AMLinkTable amLinkTable = new com.anbtech.am.entity.AMLinkTable();
				tablename = "as_history";
				ArrayList reqList = new ArrayList();
				
				//-- ���õ� �ڻ��� �̷� ����(����Ʈ) ��������
				//   div(lending) :�뿩����ó�� ����Ʈ
				//       else     :����/�̰� ���� ó�� ����Ʈ
				if("lending".equals(div)) {  
					reqList = assetModuleDAO.getInfoList(tablename,mode,login_id,searchscope,page,as_no,"lending",login_division);
				} else {	
					reqList = assetModuleDAO.getInfoList(tablename,mode,login_id,searchscope,page,as_no,"",login_division);
				}
				request.setAttribute("assetReqList1",reqList);
									
				// paging
				amLinkTable = amLinkBO.getRedirect(tablename,mode,login_id,c_no,page,"9",div,ct_id,login_division);
				request.setAttribute("Redirect",amLinkTable);
				
				if("lending".equals(div)){
					getServletContext().getRequestDispatcher("/am/admin/req_lending_process.jsp?mode="+mode+"&c_no="+c_no).forward(request,response);	
				} else {																				
					getServletContext().getRequestDispatcher("/am/admin/req_app_process.jsp?mode="+mode+"&c_no="+c_no).forward(request,response);
				}
					
			/////////////////////////////////////////////////////////////////////////////////
			//	[�ڻ� ������ ���]	
			//	
			//	1. �̰� ���ɿ��� �Ǵ�
			//     - ��û���� Ȯ��	  : ����� ��¥�� �´��� Ȯ��
			//     - ���� �ڻ���� Ȯ�� : �� �ڻ��� ������/�뿩/����/������ Ȯ��
			//     - �ߺ� ��¥ Ȯ��	  : ���� ������ �ٸ���û�ǰ� �ߺ��Ǿ������� ������ Ȯ��
			//
			//	2. �̰� �����ϸ� ó��
			//		 �̰�ó�� : as_info TABLE �� crr_id(������)�� u_id(�̰���)�� ����
			//                 as_history TABLE �� as_status(�ڻ� �����ڵ�) �� '11'(�̰�)���� ����
			//	3. �̵�
			//     �̷¸���Ʈȭ�鿡�� ó���Ǿ����� 
			//     (eachHistory.jsp)
			//	   �̰�/����/�뿩���� ȭ�� ����Ʈ���� ó���Ǿ�����
			//	   (req_app.jsp,req_app_process,req_app_lending,req_lending_process)
			//     ���� ó���� ������ �̵�
			//
			///////////////////////////////////////////////////////////////////////////////////
			}	else if("transfer_process".equals(mode)){
					
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					com.anbtech.am.business.AssetModuleBO asBO	= new com.anbtech.am.business.AssetModuleBO(con);
									
					String enable  = "";

					// �̰� ���� ���� �Ǵ�
					enable = asBO.transEnable(udate,as_no,o_status);

					if(enable.equals("enable")) {
						// �̰�ó��	
						assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
					} else {
					PrintWriter out7 = response.getWriter();
						out7.println("	<script>");
						out7.println("  alert('"+enable+"')");
						out7.println("	history.back();");
						out7.println("	</script>");
						out7.close();
					}
					
					// �����ڰ� �ڻ� ���� ó���� �������(����/�뿩/�̰�/�ݳ�/�԰�) ���� ȭ�鿡 ���� �б�
					if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
						response.sendRedirect("AssetServlet?mode="+mode_temp);
					} else {	
						response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
					}
						

			/////////////////////////////////////////////////
			//	[�ڻ������ ���]  
			//	�̰� ��� 
			//  1. as_history Table�� as_status�� '9'(���)�� ��ȯ
			//  2.  ���� ó���� ������ �̵�
			/////////////////////////////////////////////////
			}	else if("cancel_transfer".equals(mode)){
			
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				// �̰����ó��
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
				
				// �����ڰ� �ڻ� ���� ó���� ������� - ���� ȭ�鿡 ���� �б�
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp);
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
					
				
			/////////////////////////////////////////////////////
			// [ �ڻ������ ��� ]
			// ���� ó�� 
			// 1. as_history TABLE�� as_status�� '7'�� ��ȯ(������)  
			// 2.  ���� ó���� ������ �̵�
			/////////////////////////////////////////////////////
			}	else if("out_process".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
									
				// �����ڰ� �ڻ� ���� ó���� ������� - ���� ȭ�鿡 ���� �б�
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp);
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
									
			/////////////////////////////////////////////////
			//  [ �ڻ������ ��� ]
			//  ���� ��� 
			//	1. as_history�� as_status�� '9'�� ��ȯ(���)
			//  2.  ���� ó���� ������ �̵� 
			/////////////////////////////////////////////////
			}	else if("cancel_out".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
			
				// �����ڰ� �ڻ� ���� ó���� ������� - ���� ȭ�鿡 ���� �б�
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp);
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
		
			//////////////////////////////////////////////////////////////////
			//
			//  [ �ڻ������ ��� ]@
			//  ** �԰� ó��  **
			//	1. - as_history�� as_status�� '12'�� ��ȯ(�԰�).
			//     - ���� �ڻ� �����(as_info TABLE�� u_id)�� 
			//       ������ �̸�(as_info TABLE�� crr_id)���� ����(�ݳ�)�Ѵ�.
			//     
			//  2. - �԰�� �ڻ���� ��û�� ������,
			//       as_history & as_info TABLE�� as_status�� '13'���� ����
			//     - ���� ����� ������ ������ ������ ����(�ݳ�)�Ѵ�.
			//
			///////////////////////////////////////////////////////////////////
			}	else if("out_Input".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asInputProcess(h_no,as_status,o_status,as_statusinfo,as_no, in_date,wi_date);
				
				// �����ڰ� �ڻ� ���� ó���� ������� - ���� ȭ�鿡 ���� �б�
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp);
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
			
			//////////////////////////////////////////////////
			//  [ �ڻ� ������ ��� ]
			//
			//  ** �뿩 ó�� **
			//	- ���� ����� ������ ���� �ڻ� ������ ������ �ݳ�
			//  - as_status�� '16'(�뿩��)�� ��ȯ
			//
			//////////////////////////////////////////////////
			}	else if("lending_process".equals(mode)){
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
				
				// �����ڰ� �ڻ� ���� ó���� ������� - ���� ȭ�鿡 ���� �б�
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp+"&div=lending");
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
				
			//////////////////////////////////////////////////
			//	[ �ڻ� ������ ��� ] 
			//
			//	** �뿩 �ݳ� **
			//  - ���� ����� ������ ���� �ڻ� ������ ������ �ݳ�
			//  - as_status�� '17'(�ݳ��Ϸ�)�� ��ȯ
			//////////////////////////////////////////////////
			}   else if("lending_input".equals(mode)){
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asInputProcess(h_no,as_status,o_status,as_statusinfo,as_no, in_date,wi_date);
				
				// �����ڰ� �ڻ� ���� ó���� ������� - ���� ȭ�鿡 ���� �б�
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp+"&div=lending");
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}				
				
			//////////////////////////////////////////////////
			//  [ �ڻ� ������ ��� ]
			//
			//  ** �뿩 ��� **
			//  - as_history�� as_status�� 9�� ��ȯ(���)
			//////////////////////////////////////////////////
			}  else if("cancel_lending".equals(mode)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				assetModuleDAO.asManagerProcess(u_id,as_no,h_no,o_status,as_status,mode);
			
				// �����ڰ� �ڻ� ���� ó���� ������� - ���� ȭ�鿡 ���� �б�
				if("EnteringList".equals(mode_temp) || "TransOutList".equals(mode_temp)) {
					response.sendRedirect("AssetServlet?mode="+mode_temp+"&div=lending");
				} else { 
					response.sendRedirect("AssetServlet?mode=user_each_history&as_no="+as_no+"&c_no="+c_no);
				}
							
			/////////////////////////////////////////////////
			//  ** ���� ����� ���� ȭ�� **
			//  1. �ڻ����� ��������
			//  2. �̷����� ��������
			/////////////////////////////////////////////////
			}	else if("app_asset_view".equals(mode)){
						
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
				
				// �ڻ����� ��������
				asInfoTable = assetModuleDAO.getInfo(as_no);
				// history ���� �������� 
				asHistoryTable = assetModuleDAO.getHistory(h_no);

				request.setAttribute("assetInfo",asInfoTable);
				request.setAttribute("historyInfo",asHistoryTable);
				
				getServletContext().getRequestDispatcher("/am/as_user/appAsView.jsp?as_no="+as_no).forward(request,response);	
				
			/////////////////////////////////////////////////
			//  ** ���� ����� ����Ʈ ȭ�� **
			//  1. �ڻ����� ��������
			//  2. �̷����� ��������
			//  3. ��������(�����ȣ) ��������
			/////////////////////////////////////////////////
			}	else if("AppViewPrint".equals(mode)){
									
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO		= new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.entity.AsInfoTable asInfoTable		= new com.anbtech.am.entity.AsInfoTable();
				com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
				com.anbtech.am.entity.AsApprovalInfoTable app		= new com.anbtech.am.entity.AsApprovalInfoTable();
				com.anbtech.am.entity.AsApprovalInfoTable app2		= new com.anbtech.am.entity.AsApprovalInfoTable();

				String sign_path = "../gw/approval/sign/";
				
				// �ڻ� ���� �������� 
				asInfoTable = assetModuleDAO.getInfo(as_no);

				// history ���� ��������
				asHistoryTable = assetModuleDAO.getHistory(h_no);
				
				// ���� ���� ���� ��������
				pid = (String)assetModuleDAO.getPid(h_no);
				pid2 = (String)assetModuleDAO.getPid2(h_no);

				//1�� �������� ��������
				app = assetModuleDAO.getApprovalInfo(pid,sign_path);
				
				//2�� �������� ��������
				app2 = assetModuleDAO.getApprovalInfo(pid2,sign_path);

				request.setAttribute("assetInfo",asInfoTable);
				request.setAttribute("historyInfo",asHistoryTable);
				request.setAttribute("appInfo",app);
				request.setAttribute("appInfo2",app2);
							
				getServletContext().getRequestDispatcher("/am/as_user/appViewPrint.jsp?as_no="+as_no+"&pid="+pid+"&pid2="+pid2).forward(request,response);	
								
				}				
		} catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		} finally {
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

		//���ε��� �� �ʿ��� �͵� tablename,upload_size, filepath ����
		String tablename = request.getParameter("tablename");
		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		//String filepath = getServletContext().getRealPath("") + "/upload/am/";
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/am/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		// ���� ��¥
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
		String today	= anbdt.getDateNoformat();		// oooo**@@
		String thisyear = today.substring(0,4);			// �⵵ 0000

		String mode = multi.getParameter("mode"); 
		String div	= multi.getParameter("div");		// ī�װ�(/�ڻ�) �Է�/����/����
		String redirectUrl = "";						

		////- ī�װ����� ���� ���� - ////
		String c_no			= multi.getParameter("c_no")==null?
							  "0":multi.getParameter("c_no");	//ī�װ� ������ȣ
		String ct_id		= multi.getParameter("ct_id")==null?
							  "":multi.getParameter("ct_id");	// ī�װ� ID
		String ct_level		= multi.getParameter("ct_level");	// ī�װ� LEVEL
		String ct_parent	= multi.getParameter("ct_parent");	// �θ� ī�װ�
		String ct_word		= multi.getParameter("ct_word");	// ī�װ� ����
		String ct_name		= multi.getParameter("ct_name");	// ī�װ� �̸�
		String apply_dc		= multi.getParameter("apply_dc");

		////- �ڻ��̷����� ���� ���� - ////
		String  as_no		= multi.getParameter("as_no")==null?
							  "":multi.getParameter("as_no");	// �ڻ��̷� ������ȣ
		String  as_mid		= multi.getParameter("as_mid")==null?
							  "":multi.getParameter("as_no");	// �ڻ��ȣ 
							  // (��:SL-"+ī�װ�����+�������(��)+"-"+serial_no)
		String  as_item_no	= multi.getParameter("as_item_no")==null?
							  "":multi.getParameter("as_no");	      // �ڻ� ǰ�� ���� ��ȣ
		String  b_id		= multi.getParameter("b_id")==null?
							  "":multi.getParameter("b_id");		  // ������ID	
		String  b_name		= multi.getParameter("b_name")==null?
							  "":multi.getParameter("b_name");		  // ������ �̸�
		String  b_rank		= multi.getParameter("b_rank")==null?
							  "":multi.getParameter("b_rank");		  // ������ �μ���
		String  w_id		= multi.getParameter("w_id")==null?
							  "":multi.getParameter("w_id");		  // �ۼ��� ID
		String  w_name		= multi.getParameter("w_name")==null?
							  "":multi.getParameter("w_name");		  // �ۼ��� �̸�
		String  w_rank		= multi.getParameter("w_rank")==null?
							  "":multi.getParameter("w_rank");		  // �ۼ��� �μ���
		String  as_name		= multi.getParameter("as_name")==null?
							  "":multi.getParameter("as_name");		  // ǰ���(����ī�װ� ��)
		String  model_name	= multi.getParameter("model_name")==null?
							  "":multi.getParameter("model_name");	  // �𵨸�
		String  as_serial	= multi.getParameter("as_serial")==null?
							  "":multi.getParameter("as_serial");	  // �ڻ� ���� serial number
		String  buy_date	= multi.getParameter("buy_date")==null?
							  "":multi.getParameter("buy_date");	  // ��������
		String  as_price	= multi.getParameter("as_price")==null?
							  "0":multi.getParameter("as_price");	  // �ڻ� ���� ����
		String  dc_count	= multi.getParameter("dc_count")==null?
							  "0":multi.getParameter("dc_count");		// ���� ����Ƚ��
		//String  dc_bound	= multi.getParameter("dc_bound")==null?
		//					  "0":multi.getParameter("dc_bound");		// 
		String  as_each_dc	= multi.getParameter("as_each_dc")==null?
							  "0":multi.getParameter("as_each_dc");		// ���� ���� ���� ����
		String  dc_percent	= multi.getParameter("dc_percent")==null?
							  "0":multi.getParameter("dc_percent");		// (ī�װ�)���� ���� ����
		String  as_value	= multi.getParameter("as_value")==null?
							  "1000":multi.getParameter("as_value");	// �ڻ� ��ġ
		String  crr_id		= multi.getParameter("crr_id")==null?
							  "":multi.getParameter("crr_id");			// �ڻ� å���� ID
		String  crr_name	= multi.getParameter("crr_name")==null?
							  "":multi.getParameter("crr_name");		// �ڻ� å���� �̸�		
		String  crr_rank	= multi.getParameter("crr_rank")==null?
							  "":multi.getParameter("crr_rank");		// �ڻ� å���� �μ�
		String  buy_where	= multi.getParameter("buy_where")==null?
							  "":multi.getParameter("buy_where");		// �ڻ� ����ó
		String  as_maker	= multi.getParameter("as_maker")==null?
							  "":multi.getParameter("as_maker");		// �ڻ� ����Ŀ
		String  as_setting	= multi.getParameter("as_setting")==null?
							  "":multi.getParameter("as_setting");		// �ڻ� �԰�/ ���
		String  bw_tel		= multi.getParameter("bw_tel")==null?
							  "":multi.getParameter("bw_tel");			// ����ó ��ȭ��ȣ
		String  bw_address	= multi.getParameter("bw_address")==null?
							  "":multi.getParameter("bw_address");		// ����ó �ּ�
		String  bw_employee	= multi.getParameter("bw_employee")==null?
							  "":multi.getParameter("bw_employee");		// �Ǹ� ����� �̸�
		String  bw_mgr_tel	= multi.getParameter("bw_mgr_tel")==null?
							  "":multi.getParameter("bw_mgr_tel");		// �Ǹ� ����� ��ȭ��ȣ
		String  etc			= multi.getParameter("etc")==null?
							  "":multi.getParameter("etc");				// ��Ÿ/Ư�̻��� ����
		String  as_status	= multi.getParameter("as_status")==null?
							  "":multi.getParameter("as_status");		// �ڻ� ���� �ڵ�
		String  as_except_day= multi.getParameter("as_except_day");		// ��� ����
		String  apply_dcdate = multi.getParameter("apply_dcdate");		// ���� ���� ����									
		String  handle		= multi.getParameter("handle");				// ���� ���� ����
		String  del_form	= multi.getParameter("del_form");			// ��� ����
		String  del_reason	= multi.getParameter("del_reason");			// ��� �ٰ�
		String  as_except_reason = multi.getParameter("as_except_reason")==null?
							"":multi.getParameter("as_except_reason"); // ������
		String  file_se= multi.getParameter("file_se");					// ÷��ȭ�� ����
		String  file_name= multi.getParameter("file_name");				// �����̸� string
		String  file_type= multi.getParameter("file_type");				// ����Ÿ�� 
		String  file_size= multi.getParameter("file_size");				// ���ϻ�����( 234|23423|....)
		String  file_umask= multi.getParameter("file_umask");			// Renamed ����
		String  file_path= multi.getParameter("file_path");				// file ���
		
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
		String login_name = sl.name;

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////////////////////////////////////
			// ī�װ� ����
			// 1. ���� �������� ������ ������, ��������� '0'
			// 2. ī�װ� ó�� ������(div: a-�߰�, f-���ʵ��, m-����)
			//
			/////////////////////////////////////////////////////
			if("category_manage".equals(mode)){
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.am.entity.AsCategoryTable asCategoryTable = new com.anbtech.am.entity.AsCategoryTable();
							
				if(dc_percent.equals("")) dc_percent="0"; // ���� ���� ���� (�������� ������ ������ '0' ���� setting)

				// ī�װ� ����/�߰�/���
				if(div.equals("a") || div.equals("f") || div.equals("m") ){		
					assetModuleBO.setCtBusiness(div, c_no, ct_id, ct_level, ct_parent, ct_word, ct_name, dc_percent, apply_dc);
				//ī�װ� ����
				} else if(div.equals("d")){					
						String msg = assetModuleDAO.delete_ct(c_no,ct_id);
						PrintWriter out2 = response.getWriter();
						out2.println("	<script>");
						out2.println("	alert('"+msg+"');");
						out2.println("	location.href='../servlet/AssetServlet?mode=category_list'");
						out2.println("	</script>");
						out2.close();
				}
				response.sendRedirect("AssetServlet?mode=category_list");
			}

			/////////////////////////////////////////////////////////////////////////
			//	[ ������ ���  ]
			//  �ڻ���� (ó�� ������ div : input-���, modify-����, delete_process-����)
			//   
			//	1. �ڻ��ȣ ��������
			//  2. �ڻ���� Ȯ��
			//  3. ǰ��������
			//  4. �ڻ� ��ġ 
			//  5. �ڻ� ��� ���ɿ��� Ȯ��
			//  6. ���� ���
			//  
			/////////////////////////////////////////////////////////////////////////
			else if("asset_manager".equals(mode)){				
			
			con.setAutoCommit(false);	// Ʈ������� ����
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			try
			{
				if("input".equals(div)){
					
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
				com.anbtech.am.entity.AsInfoTable file = new com.anbtech.am.entity.AsInfoTable();

				tablename = "as_info";
				
				as_mid = assetModuleBO.getAsMid(c_no);	// 1. ī�װ��� �ش��ϴ� �ڻ��ȣ ��������
				int j = as_mid.length();				//    �ڻ��ȣ ����
				as_item_no = as_mid.substring(j-3,j);	// 2. �ڻ��ȣ ���� ���� ��������(�ش� ī�װ��� ����)
				as_name = (String)assetModuleDAO.getAsName(c_no);	// 3. ǰ�� ��������
										
				// �����(������) ���� setting 
				b_id		= (String)assetModuleBO.getUserInfo(b_name);
				user_info	= (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(b_id);
				b_name		= user_info.getUserName();
				b_rank		= user_info.getDivision();
				b_name		= b_id+"/"+b_name;

				// �ڻ� å���� ���� setting
				crr_id		= assetModuleBO.getUserInfo(crr_name);
				user_info	= userinfoDAO.getUserListById(crr_id);
				crr_name	= user_info.getUserName();
				crr_rank	= user_info.getDivision();
				crr_name	= crr_id+"/"+crr_name;
				
				as_value = as_price; // 4. ���� �ڻ� ��(���� ��)�� �ٷ� �ڻ� ��ġ
				
				// 5. ���� �ڻ� ��� ���ɿ��� �Ǵ� ��, ������ ī�װ� �׸񿡼��� ��� ����
				if(assetModuleDAO.assetChk(c_no)) {
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('���� ī�װ��� �����մϴ�.');");
					out.println("	history.back();");
					out.println("	</script>");
					out.close();
				} else {
			
				ct_id = assetModuleDAO.getCtId(c_no);// as_category���� ct_id��������
				
				// �ڻ����� ����ϱ�
				assetModuleDAO.saveAssetInfo(as_mid, c_no,  as_item_no,  w_id,  w_name,  w_rank, b_id, b_name,b_rank,model_name, as_name,as_serial, buy_date, as_price, dc_count,  as_each_dc, as_value, crr_id,crr_name, crr_rank, buy_where, as_maker, as_setting, bw_tel, bw_address, bw_employee, bw_mgr_tel, etc,handle,ct_id);
				as_no = assetModuleDAO.getId(tablename,as_mid);
			
				// �������� ��������
				file = (com.anbtech.am.entity.AsInfoTable)assetModuleBO.getFile_frommulti(multi, as_no, filepath);
				
				// �������� �����ϱ�
				assetModuleBO.updFile(tablename, as_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileSe(),file.getFileUmask(),file.getFilePath());
				}
				
			/////////////////////////////////////////////
			//	[ ������ ���� ]	
			//	�ڻ� ���� ����
			//  1. ����å��
			//  2. �ݾ׿� �޸��ֱ�
			//  3. ī�װ� ���濡 ���� �ڻ��ȣ ���� 
			//  4. �ڻ� ��� ���ɿ��� �Ǵ�
			//     �Ұ� - �������� �̵�
			//     ���� - ���� ����
			/////////////////////////////////////////////
			}	else if("modify".equals(div)){
			
				com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
				com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
				com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
				com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
				
				tablename = "as_info";
				
				// 1. �������� Ƚ���� '0'�̸� �ڻ� ���԰��� �ڻ� ��ġå��
				if(dc_count.equals("0")) { as_value = as_price; }	
				
				// 2. �ݾ׿� õ������ ','�ֱ�
				as_value = assetModuleBO.getStringWon(as_value);	

				String temp_cno =  assetModuleDAO.getCno(as_no);	// c_no �� ��������

				// 3. ī�װ� ���濡���� �ڻ��ȣ �ֱ�
				//	  - ī�װ��� ������� �ʾ��� ��� ������ �ڻ��ȣ�� ����Ѵ�. 
				//    - ����Ǿ�����쿡�� ���ο� �ڻ��ȣ�� �����Ѵ�.
				if (temp_cno.equals(c_no))	{
					as_mid = assetModuleDAO.getInfoAsMid(as_no);	
				} else {
					as_mid = assetModuleBO.getAsMid(c_no);
				}
				int j = as_mid.length();
				as_item_no = as_mid.substring(j-3,j);
				as_name = (String)assetModuleDAO.getAsName(c_no);

				// ����� ���� �������� 
				b_id	= (String)assetModuleBO.getUserInfo(b_name);
				user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(b_id);
				b_name = user_info.getUserName();
				b_rank = user_info.getDivision();
				b_name = b_id+"/"+b_name;

				crr_id = assetModuleBO.getUserInfo(crr_name);
				user_info = userinfoDAO.getUserListById(crr_id);
				crr_name = user_info.getUserName();
				crr_rank = user_info.getDivision();
				crr_name = crr_id+"/"+crr_name;
				
				// 4. ���� �ڻ� ��� ���ɿ��� �Ǵ� ��, ������ ī�װ� �׸񿡼��� ����/��� ����
				if(assetModuleDAO.assetChk(c_no)) {
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('���� ī�װ��� �����մϴ�.');");
					out.println("	history.back();");
					out.println("	</script>");
					out.close();
				} else {
					ct_id = assetModuleDAO.getCtId(c_no);// as_category���� ct_id��������
										
					// �ڻ� ���� ����
					assetModuleDAO.modifyAssetInfo(as_no,as_mid, c_no,as_item_no,w_id,w_name,w_rank,b_id,b_name,b_rank,model_name, as_name,as_serial, buy_date, as_price, dc_count, as_each_dc, as_value, crr_id,crr_name, crr_rank, buy_where, as_maker, as_setting, bw_tel, bw_address, bw_employee, bw_mgr_tel, etc,handle,ct_id,as_status);

					// ÷��ȭ������ ��������
					ArrayList file_list = assetModuleDAO.getFileList(as_no);

					com.anbtech.am.entity.AsInfoTable file = new com.anbtech.am.entity.AsInfoTable();
					file = (com.anbtech.am.entity.AsInfoTable)assetModuleBO.getFile_frommulti(multi, as_no, filepath, file_list);

					// ÷������ ���� ������Ʈ				
					assetModuleBO.updFile(tablename, as_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileSe(),file.getFileUmask(),file.getFilePath());					
					}
			
				/////////////////////////////////////////////
				//	[ ������ ���]	
				//	�ڻ� ��� ���� ����
				//  - as_info�� as_status�� '10'���� ����
				/////////////////////////////////////////////
				}	else if("delete_process".equals(div)){
				
					com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
					assetModuleDAO.asDelete(as_no,as_except_day,as_except_reason,del_form,del_reason);
				}

				response.sendRedirect("AssetServlet?mode=asset_list");
				con.commit(); // commit�Ѵ�.
			
			} catch(Exception e) {
				con.rollback();
				request.setAttribute("ERR_MSG",e.toString());
				getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);		
			} finally {
				con.setAutoCommit(true);
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