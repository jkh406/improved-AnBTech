/* 
	코드체계와 관련해서 주의할 사항 
	===========================
	채번체계가 대분류(1)+중분류(2)+소분류(2) 가 아닐경우에는 다음을 수정해야 함.
	- CodeMgrServlet.java 의 doGet() 앞부분과 doPost() 앞부분

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
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리
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

		//표준품 및 사양품 등록 시 필요한 파라미터들
		String code_big		= request.getParameter("code_big")==null?"":request.getParameter("code_big");
		String code_mid		= request.getParameter("code_mid")==null?"":request.getParameter("code_mid");
		String code_small	= request.getParameter("code_small")==null?"":request.getParameter("code_small");
		String spec_code	= request.getParameter("spec_code");
		//채번체계가 대분류(1)+중분류(2)+소분류(2) 가 아닐경우에는 아래 3라인을 수정해야 함.
		if(item_no != null && !item_no.equals("")){
			code_big	= item_no.substring(0,1);
			code_mid	= item_no.substring(0,3);
			code_small	= item_no.substring(0,5);
		}

		//완제품코드 등록시 필요한 파라미터들
		String one_class	= request.getParameter("one_class");				//제품군코드
		String two_class	= request.getParameter("two_class");				//제품코드
		if (one_class == null) one_class = "";
		if (two_class == null) two_class = "";

		//Assy코드 등록 시 필요한 파라미터들
		String model_code	= request.getParameter("model_code");

		String redirectUrl = "";

		//현재 접속중인 사용자 아이디 가져오기
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
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////
			// 검색 조건에 맞는 품목 리스트 출력
			///////////////////////////////////////////////////
			if(mode.equals("list_item") || mode.equals("list_item_p")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);			
				ArrayList list_item = new ArrayList();

				list_item = cmDAO.getListItem(mode,category,code_big,code_mid,code_small,searchword,searchscope,page,login_id);
				request.setAttribute("LIST_ITEM",list_item);

				if(mode.equals("list_item_p")){
					//품목분류 가져오기
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
			// 원부자재품목 채번폼 및 기채번된 품목정보의 수정폼
			/////////////////////////////////////////////////////
			else if(mode.equals("reg_item") || mode.equals("modify_item")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

				//품목정보 가져오기
				part = cmBO.getPartRegForm(mode,code_big,code_mid,code_small,item_no);
				request.setAttribute("PART_INFO",part);

				//선택된 소분류 품목에 정의된 스펙리스트 가져오기
				//수정일 경우에는 값을 세팅하여..
				ArrayList spec_list = new ArrayList();
				spec_list = cmBO.getSpecList(mode,code_small,item_no);
				request.setAttribute("SPEC_LIST",spec_list);

				// 첨부화일 가져오기
				ArrayList file_list = new ArrayList();
				file_list = cmDAO.getFile_list(item_no);
				request.setAttribute("FILE_LIST", file_list);

				getServletContext().getRequestDispatcher("/cm/reg_item.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// 완제품 코드 등록 및 수정 폼
			/////////////////////////////////////////////////////
			else if(mode.equals("reg_fg") || mode.equals("modify_fg")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

				//품목정보 가져오기
				part = cmBO.getFgRegForm(mode,code_big,one_class,two_class,item_no,model_code);
				request.setAttribute("PART_INFO",part);

				//선택된 모델코드에 관련된 코드 리스트 가져오기
				ArrayList item_list = new ArrayList();
				item_list = cmDAO.getItemListByModelCode(model_code);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/cm/reg_fg.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// ASS'Y 코드 등록 및 수정 폼
			/////////////////////////////////////////////////////
			else if(mode.equals("reg_assy") || mode.equals("modify_assy")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

				//품목정보 가져오기
				part = cmBO.getAssyRegForm(mode,code_big,one_class,two_class,item_no,model_code);
				request.setAttribute("PART_INFO",part);

				//선택된 모델코드에 관련된 코드 리스트 가져오기
				ArrayList item_list = new ArrayList();
				item_list = cmDAO.getItemListByModelCode(model_code);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/cm/reg_assy.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// ASS'Y 코드 or F/G 코드 정보 수정 폼
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
			// 선택된 품목정보 상세보기
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
				
				// 첨부파일 link String 가져오기
				String downfile = clinkBO.getDownFileLink(item_no);
				request.setAttribute("DOWN_FILE", downfile);

				getServletContext().getRequestDispatcher("/cm/view_item.jsp").forward(request,response);
			}

			//////////////////////////////////////////////////
			// 선택된 품목정보 상세보기(F/G & Assay 상세보기 LINK)
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
			// 품목속성 지정에 의한 품목검색창
			////////////////////////////////
			if(mode.equals("search_by_spec")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

				//품목분류 가져오기
				part = cmBO.getPartRegForm(mode,code_big,code_mid,code_small,item_no);
				request.setAttribute("PART_INFO",part);

				//선택된 중분류 또는 소분류 품목에 정의된 스펙리스트 가져오기
				ArrayList spec_list = new ArrayList();
				spec_list = cmBO.getSpecList(code_mid,code_small);
				request.setAttribute("SPEC_LIST",spec_list);

				getServletContext().getRequestDispatcher("/cm/search_item.jsp").forward(request,response);
			}

			/////////////////////////////////////////////////////
			// 중분류 품목(부품)별 표준 템플릿 스펙항목 추가 및 수정 폼
			/////////////////////////////////////////////////////
			else if(mode.equals("add_template_code") || mode.equals("modify_template_code")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.SpecCodeTable table = new com.anbtech.cm.entity.SpecCodeTable();

				//선택된 스펙코드에 대한 정보를 가져온다.
				table = cmBO.getAddStdTemplateCodeForm(mode,code_big,code_mid,spec_code);
				request.setAttribute("SPEC_INFO",table);

				//선택된 분류에 존재하는 표준템플릿 스펙리스트를 가져온다.
				ArrayList spec_list = new ArrayList();
				spec_list = cmBO.getStdTemplateSpecList(code_mid);
				request.setAttribute("SPEC_LIST", spec_list);

				getServletContext().getRequestDispatcher("/cm/admin/add_template_code.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			// 품목 tree 출력용 스크립트 파일 생성
			/////////////////////////////////////////////////////
			else if(mode.equals("make_tree")){
			  com.anbtech.cm.business.makeCodeTreeItems tree	 = new com.anbtech.cm.business.makeCodeTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/cm/admin/";
				tree.makeCodeTree(output_path + "code_tree_items.js","CodeMgrServlet",1,0);

				getServletContext().getRequestDispatcher("/cm/admin/make_tree.jsp").forward(request,response);
			}

			/*******************************************************
			 * 첨부파일 다운로드 처리
			 ********************************************************/
			else if ("download".equals(mode)){
				
				//따로 이동하는 곳 없이 다운로드시킨다.
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.entity.PartInfoTable file = new com.anbtech.cm.entity.PartInfoTable();
				file = cmBO.getFile_fordown(item_no);
				String filename = file.getFileName();
				String filetype = file.getFileType();
				String filesize = file.getFileSize();

				//boardpath 에서 파일까지 경로 지정
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/cm/" + item_no + ".bin";
			
				if (filetype.indexOf("mage")<=0)
					filetype = "application/unknown";
				
				String strClient=request.getHeader("User-Agent");

				///////////////////////////////////////////////////////////
				//파일명을 아래와 같이 영문처리 했을때 한글이 깨지지 않았음.
				///////////////////////////////////////////////////////////
				filename = new String(filename.getBytes("euc-kr"),"8859_1"); 

				if(strClient.indexOf("MSIE 5.5")>-1) response.setHeader("Content-Disposition","filename="+filename);
				else response.setHeader("Content-Disposition","attachment; filename="+filename);
				
				//////////////////////////////////////////////////////////////////
				//db가 mysql일 때는 아래 문장으로 대치했을 때 한글이 깨지지 않았음.
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
			* 품목 삭제하기	
			*****************************************************/
			else if (mode.equals("delete_item"))  {
				con.setAutoCommit(false);	// 트랜잭션을 시작
				try{
					//1.첨부파일 삭제하기
					com.anbtech.cm.db.CodeMgrDAO codeMgrDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
					String mid = codeMgrDAO.getMid(item_no);
		

					String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/cm/";
						for(int i=1; i<10; i++){
							java.io.File f = new java.io.File(filepath + "/" + item_no + "_" + i + ".bin");
							if(f.exists()) f.delete();
						}
					
					codeMgrDAO.deleteItem(mid);
							
					con.commit(); // commit한다.
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
	 * post방식으로 넘어왔을 때 처리
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

	
		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/cm/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//전페이지에서 값 받아온다. multi에서 가져옴
		String mode = multi.getParameter("mode");
		String page = multi.getParameter("page");
		String searchword = multi.getParameter("searchword");
		String searchscope = multi.getParameter("searchscope");
		String category = multi.getParameter("category");

		//공백으로 넘어오거나 null로 온 값들에 대한 처리.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

		//품목채번의뢰관련
		String item_no			= multi.getParameter("item_no");	//품목번호
		String item_desc		= multi.getParameter("item_desc");	//품목 Description
		String mfg_no			= multi.getParameter("mfg_no");		//업체코드
		String code_str			= multi.getParameter("code_str");	//해당 소분류에 정의된 상세스펙코드 (32001,32002,32003,....)
		String spec_str			= multi.getParameter("spec_str");	//상세스펙 코드|값|단위 (32001|IDT74FCT165|na,32002|33|EA...)
		String item_type		= multi.getParameter("item_type");	//품목계정(or ASSY구분)
		if(item_type==null)	item_type = "";
		String config_name		= multi.getParameter("config_name");//형상명
		String item_name		= multi.getParameter("item_name");	//품목명
		String stock_unit		= multi.getParameter("stock_unit");	//품목재고단위
		String where_assy		= multi.getParameter("where_assy");	//품목계정
		if(item_type.equals("PH")) where_assy = "9";
	
		//품목별 표준템플릿 관리관련
		String code_big			= multi.getParameter("code_big");
		String code_mid			= multi.getParameter("code_mid");
		String code_small		= multi.getParameter("code_small");
		String spec_code		= multi.getParameter("spec_code");
		String spec_name		= multi.getParameter("spec_name");
		String spec_value		= multi.getParameter("spec_value");
		String spec_unit		= multi.getParameter("spec_unit");
		String write_exam		= multi.getParameter("write_exam");
		String spec_desc		= multi.getParameter("spec_desc");

		//채번체계가 대분류(1)+중분류(2)+소분류(2) 가 아닐경우에는 아래 3라인을 수정해야 함.
		if(item_no != null && !item_no.equals("")){
			code_big	= item_no.substring(0,1);
			code_mid	= item_no.substring(0,3);
			code_small	= item_no.substring(0,5);
		}

		//완제품코드 등록시에 추가되는 파라미터들
		String one_class	= multi.getParameter("one_class");	//제품군코드
		String two_class	= multi.getParameter("two_class");	//제품코드
		if (one_class == null) one_class = "";
		if (two_class == null) two_class = "";
		String model_code	= multi.getParameter("model_code");	 //모델코드
		String model_name	= multi.getParameter("model_name");	 //모델명
		String reg_type		= multi.getParameter("reg_type");	 //신규 or 파생

		//ASS'Y코드 등록시 추가되는 파라미터들
		String op_codes		= multi.getParameter("op_codes");	 //공정코드
		String assy_type	= multi.getParameter("assy_type");	 //ASSY TYPE

		String redirectUrl = "";



		//현재 접속중인 사용자 아이디 가져오기
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
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			////////////////////
			// 품목 등록처리
			////////////////////
			if(mode.equals("reg_item")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				//품목번호(item_no)생성
				item_no = cmDAO.calculateItemNo(code_small);

				//중복품목 등록여부 체크
				String same_item = cmBO.checkSameItemExist(item_no,code_small,mfg_no,spec_str);
				if(same_item.length() > 1){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('동일속성의 품목이 1건 존재합니다. 등록작업을 계속할 수 없습니다.\\n\\n" + same_item + "');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}else{
					//db저장
					cmDAO.savePartInfo(item_no,item_desc,mfg_no,item_name,item_type,stock_unit,spec_str,login_id);
					com.anbtech.cm.entity.PartInfoTable file = new com.anbtech.cm.entity.PartInfoTable();
					//첨부파일 업로더
					file = cmBO.getFile_frommulti(multi, item_no, filepath);
					//업로딩 된 첨부파일 정보를 DB에 저장하기
					cmBO.updFile(item_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileUmask());
				}
				getServletContext().getRequestDispatcher("/cm/reg_result.jsp?item_no=" + item_no).forward(request,response);
			}

			////////////////////
			// 품목 수정처리
			////////////////////
			else if(mode.equals("reg_item2")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				//  UPDATE
				cmDAO.updateItemInfo(item_no,item_desc,item_type,stock_unit);								
				
				redirectUrl = "CodeMgrServlet?mode=view_item2&item_no="+item_no;
			}

			////////////////////
			// 품목 수정처리
			////////////////////
			else if(mode.equals("modify_item")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

/* 품목정보 수정시 품목규격외의 정보를 수정하는 경우에도 중복품목으로 체크가 되어 수정이
   안되는 관계로 수정시에는 중복품목 체크를 하지 않음

				//중복품목 등록여부 체크
				String same_item = cmBO.checkSameItemExist(item_no,code_small,mfg_no,spec_str);
				if(same_item.length() > 1){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('동일속성의 품목이 1건 존재합니다. 등록작업을 계속할 수 없습니다.\\n\\n" + same_item + "');");
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

				// 화일정보 가져오기
				ArrayList file_list = new ArrayList();
				file_list = cmDAO.getFile_list(item_no);

				//  첨부 화일 저장
				com.anbtech.cm.entity.PartInfoTable file = new com.anbtech.cm.entity.PartInfoTable();
				//  첨부파일 업로더
				file = cmBO.getFile_frommulti(multi, item_no, filepath, file_list);
				//  업로딩 된 첨부파일 정보를 DB에 저장하기
				cmBO.updFile(item_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileUmask());

				redirectUrl = "CodeMgrServlet?mode=view_item&item_no="+item_no;
			}

			////////////////////
			// 완제품 코드등록 처리
			////////////////////
			else if(mode.equals("reg_fg")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				//파생번호 계산
				//신규일경우는 중복등록여부를 체크하고, 파생일경우는 파생번호를 가져온다.
				String derive_code = "00";
				if(reg_type.equals("n")){
					String same_item = cmDAO.getItemNoByModelCode(code_big,model_code);

					if(same_item.length() > 1){
						PrintWriter out = response.getWriter();
						out.println("<script>");
						out.println("alert('선택하신 모델코드(" + model_code + ")에 완제품 코드가 1건 존재합니다. 등록작업을 계속할 수 없습니다.\\n\\n" + same_item + "');");
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
						out.println("alert('선택하신 모델코드(" + model_code + ")에 부여된 대표 완제품 코드가 없습니다. 대표 완제품 코드를 먼저 등록하십시오.');");
						out.println("history.go(-1);");
						out.println("</script>");
						out.close();
						return;								
					}
				}

				//gcode를 가지고 code 값을 가져온다.
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				one_class = goodsDAO.getCodeByGcode(one_class);
				two_class = goodsDAO.getCodeByGcode(two_class);

				//일련번호 계산
				String serial_no = cmDAO.getSerialNo(code_big,one_class,two_class,model_code,derive_code);

				//완제품코드 생성
				item_no = code_big + one_class +  two_class + serial_no + derive_code;

				//description 생성
				com.anbtech.gm.entity.GoodsInfoTable table = new com.anbtech.gm.entity.GoodsInfoTable();
				table = goodsDAO.getGoodsInfoByCode(model_code);
				model_name = table.getGoodsName();

				item_desc = "F/G," + model_code + "," + model_name;

				//db 저장
				cmDAO.saveFgInfo(model_code,code_big,one_class,two_class,serial_no,derive_code,item_no,item_desc,login_id,item_name,stock_unit,item_type);

				String item_nos = item_no + "(" + item_desc + ")";
				request.setAttribute("ITEM_NOS",item_nos);

				getServletContext().getRequestDispatcher("/cm/reg_result.jsp?item_no=" + item_no).forward(request,response);
			}

			////////////////////
			// ASS'Y 코드등록 처리
			////////////////////
			else if(mode.equals("reg_assy")){
				com.anbtech.cm.business.CodeMgrBO cmBO = new com.anbtech.cm.business.CodeMgrBO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
									
				//gcode를 가지고 code 값을 가져온다.
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				one_class = goodsDAO.getCodeByGcode(one_class);
				two_class = goodsDAO.getCodeByGcode(two_class);

				//넘어온 공정코드를 콤마(,)로 구분하여 개개의 공정코드를 추출하여 처리
				StringTokenizer str = new StringTokenizer(op_codes, ",");
				String item_nos = "";
				while(str.hasMoreTokens()){
					String op_code = str.nextToken();

					//일련번호 계산
					String serial_no = cmDAO.getSerialNo(code_big,one_class,two_class,model_code);

					//ASS'Y코드 생성
					//item_no = code_big + assy_type + one_class +  serial_no + op_code;
					if(item_type.equals("PH")){
						item_no = "1PH" + one_class + serial_no + op_code;			
					}else{
						item_no = "1" + item_type + assy_type + one_class + serial_no + op_code;
					}

					//description 생성
					com.anbtech.gm.entity.GoodsInfoTable table = new com.anbtech.gm.entity.GoodsInfoTable();
					table = goodsDAO.getGoodsInfoByCode(model_code);
					model_name = table.getGoodsName();

					item_desc = cmBO.getDescForAssy(item_type,assy_type,op_code,model_code,model_name,config_name);

					//db 저장
					cmDAO.saveAssyInfo(model_code,code_big,one_class,two_class,serial_no,assy_type,op_code,item_no,item_desc,login_id,where_assy,item_name,stock_unit);
					
					//
					item_nos += "\\n" + item_no + " (" + item_desc + ")";
					request.setAttribute("ITEM_NOS",item_nos);
				}

				getServletContext().getRequestDispatcher("/cm/reg_result.jsp?item_no=" + item_nos).forward(request,response);
			}

			/////////////////////////////////
			// 품목별 표준템플릿스펙 추가 처리
			/////////////////////////////////
			else if (mode.equals("add_template_code")){
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				cmDAO.saveSpecInfo(code_mid,spec_code,spec_name,spec_value,spec_unit,write_exam,spec_desc);

				redirectUrl = "CodeMgrServlet?mode=add_template_code&code_big="+code_big+"&code_mid="+code_mid;

			}

			/***********************************************************
			 * 대분류 부품에 대한 스펙항목 수정 처리(관리자 모드)
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
			//con소멸
			close(con);
		}
	} //doPost()
}
