/***************************************
 ShareBdServlet.java 
 #. 업무공유 관리의 모든기능 컨트롤
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
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************************************
	 * get방식으로 넘어왔을 때 처리								  *
	 *********************************************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

	//필요한 것들 선언
	response.setContentType("text/html;charset=euc-kr");
	HttpSession session = request.getSession(true);

	String tablename	= request.getParameter("tablename");	//tablename 으로 문서종류 결정(기술문서,제안서,일반문서)	
	String no			= request.getParameter("no");			//master_data 테이블의 레코드 관리번호
	String ver_code		= request.getParameter("ver");			//버젼코드
	String mode			= request.getParameter("mode");			//모드
	String page			= request.getParameter("page");			//페이지
	String umask		= request.getParameter("umask");		//Rename파일

	// 권한확인 변수
	boolean bool =  false;

	//검색시에 넘어오는 파라미터들
	String searchword	= request.getParameter("searchword");	//검색어
	String searchscope	= request.getParameter("searchscope");	//검색필드
	String category		= Hanguel.toHanguel(request.getParameter("category"));	//카테고리 코드
	String boardpage	= request.getParameter("boardpage");	//
			
	if (mode == null) mode = "list";  			//처음에는 mode가 안 넘어오므로 mode를 list로 세팅
	if (page == null) page = "1";
	if (searchword == null) searchword = "";
	else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);

	if (searchscope == null) searchscope = "";
	if (category == null)		category = "";
	if (tablename == null)	   tablename = "com_rule";	// 처음 접근 Table 사규(com_rule)업무

	String redirectUrl = "";

	// 현재 접속중인 사용자 아이디 가져오기
	// 세션이 종료되었을 경우 로긴 페이지로 강제 이동시킨다.
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
		// conn 생성
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
		
		
		com.anbtech.share.db.ShareBdDAO shbDAO = new com.anbtech.share.db.ShareBdDAO(con);
		com.anbtech.share.entity.ShareParameterTable sbpara = new com.anbtech.share.entity.ShareParameterTable();
		String categorycombo =  
			shbDAO.getCategoryItem(tablename, mode); //카테고리:SELECT ComboBox를 String으로 만들기

		sbpara.setTableName(tablename);		// 현재 적용되는 테이블 name 
		sbpara.setMode(mode);				// 현재 모드
		sbpara.setCategory(category);		// 현재 카테고리
		sbpara.setId(login_id);				// 현재 login_id정보 setting	
		sbpara.setName(login_name);			// 현재 login_name정보 setting	
		sbpara.setCategoryCombo(categorycombo);		// 카테고리 SELECT ComboBOX
		sbpara.setSearchScope(searchscope);	// 검색범위 
		
		//	권한체크( 등록/수정/삭제 가능한 사용자인지 확인 )
		com.anbtech.share.business.ShareBdBO sbdBO = new com.anbtech.share.business.ShareBdBO(con);
		bool = (boolean)sbdBO.adminValid(login_id, tablename);	
		sbpara.setBool(bool);
		
		request.setAttribute("sbParameter", sbpara);

		////////////////////////////////////////////////////////////
		//  각 업무별 목록보기 
		//	1. 정보 가져오기
		//	   file link 만들기, 제목(subjec)에 '상세정보보기' link달기
		//	2. paging (페이징, 리스트폼, 수정폼, 삭제 link 만들기)
		////////////////////////////////////////////////////////////
		if("list".equals(mode)) {
			
			com.anbtech.share.db.ShareBdDAO				 sbdDAO = new com.anbtech.share.db.ShareBdDAO(con);
			com.anbtech.share.business.ShareLinkBO	   shLinkBO	= new com.anbtech.share.business.ShareLinkBO(con);
			com.anbtech.share.entity.ShareLinkTable shLinkTable = new com.anbtech.share.entity.ShareLinkTable();
			ArrayList arry = new ArrayList();
			
			arry = sbdDAO.getShareBdList(tablename,mode,searchword,searchscope,category,page);		 // 정보 list
			
			shLinkTable = shLinkBO.getRedirect(tablename,mode,searchword,searchscope,category,page,no); // paging	
			
			request.setAttribute("Arry",arry);
			request.setAttribute("Redirect",shLinkTable);
			
			// 각 해당 업무로 분기
			getServletContext().getRequestDispatcher("/share/"+tablename+"/list.jsp").forward(request,response);

		/////////////////////////////////////////////////////////
		//   업무별 정보보기 
		//	 #. 조회수 counting    
		/////////////////////////////////////////////////////////
		} else if("view".equals(mode)) {

			com.anbtech.share.db.ShareBdDAO				 sbdDAO	= new com.anbtech.share.db.ShareBdDAO(con);
			com.anbtech.share.business.ShareBdBO		   shBO	= new com.anbtech.share.business.ShareBdBO(con);
			com.anbtech.share.business.ShareLinkBO	   shLinkBO	= new com.anbtech.share.business.ShareLinkBO(con);
			com.anbtech.share.entity.ShareLinkTable shLinkTable = new com.anbtech.share.entity.ShareLinkTable();
			com.anbtech.share.entity.ShareBdTable		sbTable	= new com.anbtech.share.entity.ShareBdTable();
			ArrayList arry = new ArrayList();
			
			if("view".equals(mode))	sbdDAO.countingCheck(tablename,no);	// 정보보기시 조회수 증가(count) 
			
			shLinkTable = shLinkBO.getRedirect(tablename,mode,searchword,searchscope,category,page,no);	// link url			
			sbTable = shBO.getWrite_form(tablename,mode,no,login_id);			// 정보 가져오기
			arry = (ArrayList)sbdDAO.getFile_list(tablename,no);				// 첨부화일 list

			request.setAttribute("sharefile",arry);
			request.setAttribute("Redirect",shLinkTable);
			request.setAttribute("shareBdTable",sbTable);
			getServletContext().getRequestDispatcher("/share/"+tablename+"/view.jsp").forward(request,response);
					
		////////////////////////////////////////////////////////
		//	각 업무별 등록/수정 화면 
		////////////////////////////////////////////////////////
		} else if("write".equals(mode) || "modify".equals(mode)){
		
		com.anbtech.share.business.ShareBdBO shBO			= new com.anbtech.share.business.ShareBdBO(con);
		com.anbtech.share.db.ShareBdDAO	shDAO				= new com.anbtech.share.db.ShareBdDAO(con);
		com.anbtech.share.entity.ShareBdTable shTable		= new com.anbtech.share.entity.ShareBdTable();
		com.anbtech.share.business.ShareLinkBO shLinkBO		= new com.anbtech.share.business.ShareLinkBO(con);
		com.anbtech.share.entity.ShareLinkTable shLinkTable = new com.anbtech.share.entity.ShareLinkTable();

		shTable = shBO.getWrite_form(tablename,mode,no,login_id);	// 정보 가져오기
		ArrayList arry = new ArrayList();							// 첨부화일정보 ArrayList

		shLinkTable = shLinkBO.getRedirect(tablename,mode,searchword,searchscope,category,page,no);	// link url							
			
		if ("modify".equals(mode)){
			arry = (ArrayList)shDAO.getFile_list(tablename,no);		// 첨부화일 정보 가져오기
			request.setAttribute("sharefile",arry);
		}	
				
		request.setAttribute("Redirect",shLinkTable);	
		request.setAttribute("shareBdTable",shTable);
		getServletContext().getRequestDispatcher("/share/"+tablename+"/write.jsp").forward(request,response);				

		///////////////////////////////////////////////////////////////
		// 문서 삭제 처리
		// #. 검색시 필요한 변수 중 한글 encoding 문제
		//    servlet -> jsp 한글 처리 가능,
		//    servlet -> jsp -> servlet -> servlet 
		//             1)    2)          3)
		//    3)에서는  1),2)에서 중복된 한글 처리로인해 3)으로 넘어
		//    갈때는 한글이 복구가 안됨(깨짐).
		//	   - solution : 2)에서 parameter를 넘길때 encoding(EUC-KR)된
		//                  한글(변수값)을 다시 encoding(8859_1)하여 해결
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
			
			//boardpath 에서 파일까지 경로 지정
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
		//redirectUrl의 값이 있을시에는 redirectUrl경로로 이동한다.
		if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

	}catch (Exception e){
		request.setAttribute("ERR_MSG",e.toString());
		getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
	}finally{
		close(con);
	}
	} // doGet()


	/******************************************************
	 * post방식으로 넘어왔을 때 처리						  *
	 *****************************************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		//업로드할 때 필요한 것들 tablename,upload_size, filepath 선언
		String tablename	= request.getParameter("tablename");
		String upload_size	= request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";
		
		String filepath = getServletContext().getRealPath("") + "/upload/share/"+tablename+"/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//전페이지에서 값 받아온다. multi에서 가져옴
		String  mode			= multi.getParameter("mode");
		String  page			= multi.getParameter("page");
		String  searchword		= multi.getParameter("searchword");
		String  searchscope		= multi.getParameter("searchscope");
		String  category_id		= multi.getParameter("category");	//  카테고리 코드
		String	no				= multi.getParameter("no");			//  관리번호 		
		String	subject			= multi.getParameter("subject");	//	제목	
		String	ver				= multi.getParameter("ver");		//	버젼	
		String	wid				= multi.getParameter("wid");		//	등록자ID	
		String	wname			= multi.getParameter("wname");		//	등록자이름	
		String	wdate			= multi.getParameter("wdate");		//	등록일		
		String	doc_no			= multi.getParameter("doc_no");		//	등록문서 번호	
		String	ac_code			= multi.getParameter("ac_code");	//	부서Code		
		String	ac_name			= multi.getParameter("ac_name");	//	부서명		
		String	category		= multi.getParameter("category");	//	카테고리		
		String	content			= multi.getParameter("content");	//	내용			
		String	fname			= multi.getParameter("fname");		//	화일이름		
		String	fsize			= multi.getParameter("fsize");		//	화일사이즈	
		String	ftype			= multi.getParameter("ftype");		//	화일타입		
		String  fumask			= multi.getParameter("fumask");		//  Rename file 
		String  cnt				= 
					multi.getParameter("cnt")==null?"0":multi.getParameter("cnt");//	조회수		
		String  mid				= multi.getParameter("mid");
		String  mname			= multi.getParameter("mname");

		//공백으로 넘어오거나 null로 온 값들에 대한 처리.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";
		if (no == null) no = "";
		
		String redirectUrl = "";

		//현재 접속중인 사용자 아이디 가져오기
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
		// con생성
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	
		//////////////////////////////////////////////////
		//	문서 신규 등록 처리
		//////////////////////////////////////////////////
		if ("write".equals(mode)){

			String t_id = "";  // DB TABLE의 관리번호 임시 저장변수

			com.anbtech.share.business.ShareBdBO shBdBO = new com.anbtech.share.business.ShareBdBO(con);
			com.anbtech.share.db.ShareBdDAO shBdDAO = new com.anbtech.share.db.ShareBdDAO(con);
			com.anbtech.share.entity.ShareBdTable file = new com.anbtech.share.entity.ShareBdTable();
				
			// 사규(양식,메뉴얼)업무공유 정보 입력
			shBdDAO.saveData(tablename,subject,ver,login_id,login_name,doc_no,ac_name,category,content,"0");
						
			//data_id 와 ver_code 에 해당하는 관리번호를 가져온다.
			t_id = shBdDAO.getNo(tablename,doc_no,ver);
			
			//첨부파일 업로더
			file = (com.anbtech.share.entity.ShareBdTable)shBdBO.getFile_frommulti(multi, t_id, filepath);
			
			//업로딩 된 첨부파일 정보를 DB에 저장하기
			shBdBO.updFile(tablename, t_id, file.getFname(), file.getFtype(), file.getFsize(), file.getFpath());
			String ns = new String(category.getBytes("EUC_KR"),"8859_1");	// 8859_1형식으로 다시변환
			redirectUrl="ShareBdServlet?tablename="+tablename+"&mode=list&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+ns;
						
				
		///////////////////////////////////////////////////
		// 문서 수정 처리
		//////////////////////////////////////////////////
		} else if ("modify".equals(mode)){

			com.anbtech.share.business.ShareBdBO shBdBO = new com.anbtech.share.business.ShareBdBO(con);
			com.anbtech.share.db.ShareBdDAO shBdDAO = new com.anbtech.share.db.ShareBdDAO(con);
			com.anbtech.share.entity.ShareBdTable file = new com.anbtech.share.entity.ShareBdTable();
		
			//Table UPDATE
			shBdDAO.updTable(no,tablename,subject,ver,login_id,login_name,doc_no,ac_name,category,content);

			//multi에서 파일정보를 가져와서 처리한다.
			ArrayList file_list = shBdDAO.getFile_list(tablename,no);
			
			//파일 업로더
			file = shBdBO.getFile_frommulti(multi, no, filepath, file_list);
				
			// 화일정보 SAVE
			shBdBO.updFile(tablename, no, file.getFname(), file.getFtype(), file.getFsize(), file.getFpath());
			String ns = new String(category.getBytes("EUC_KR"),"8859_1");
			
			page="1"; // 저장후 바로 첫 페이지...

			redirectUrl = "ShareBdServlet?tablename="+tablename+"&mode=list&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+ns;
		}
				//redirectUrl의 값이 있을시에는 redirectUrl경로로 이동한다.
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