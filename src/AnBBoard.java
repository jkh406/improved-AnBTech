/*
 * AnBBoard.java
 * 게시판의 모든 기능을 담당하는 contoller역할
 */

import com.anbtech.board.entity.*;
import com.anbtech.board.db.*;
import com.anbtech.board.business.*;
import com.anbtech.dbconn.DBConnectionManager;

import com.oreilly.servlet.MultipartRequest;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.Connection;
import java.util.*;
import java.io.*;

public class AnBBoard extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;

	//소멸자 = con소멸
	public void close(Connection con) throws ServletException {
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		String boardpath = getServletContext().getRealPath("") + com.anbtech.admin.db.ServerConfig.getConf("boardpath");
		String mapping = "";//img경로를 구할 때 필요
//		if(!"".equals(com.anbtech.admin.db.ServerConfig.getConf("boardpath"))) mapping = com.anbtech.admin.db.ServerConfig.getConf("boardpath").substring(1) + "/";
//		if("/servlet/AnBBoard".equals(request.getServletPath())) mapping = "../" + mapping;

		//전페이지에서 값 받아온다.
		String tablename = request.getParameter("tablename");
		String mode = request.getParameter("mode");
		String boardpage = request.getParameter("boardpage");
		String searchword = request.getParameter("searchword");
		String searchscope = request.getParameter("searchscope");
		String password = request.getParameter("password");
		String no = request.getParameter("no");
		String multino = request.getParameter("multino");
		String category = request.getParameter("category");

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/board/" + tablename + "/";

		if (mode == null) mode = "list";	//처음에는 mode가 안 넘어오므로 mode를 list로 세팅
		if (boardpage == null) boardpage = "1";
		if (searchword == null) searchword = "";

		//검색이 안 될 경우 StringProcess의 클래스를 수정할 것
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);

		if (searchscope == null) searchscope = "";
		if (password == null) password = "";
		if (no == null) no = "";
		if (multino == null) multino = "";
		if (category == null) category = "";

		//현재 접속중인 사용자 아이디 가져오기
		//세션이 종료되었을 경우 로긴 페이지로 강제 이동시킨다.
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

			//board_env 테이블에서 해당 게시판 설정을 가져온 뒤,
			//Board_Env 빈즈에 값을 넣는다.
			com.anbtech.board.db.Board_EnvDAO board_envDAO = new com.anbtech.board.db.Board_EnvDAO(con);
			com.anbtech.board.entity.Board_Env board_env = board_envDAO.getBoard_env(tablename);
			
			board_env.setMode(mode);//추가적으로 들어가는 것들..
			board_env.setMapping(mapping);

			//request에다가 Board_Env이름 으로 board_env를 넣는다.
			//
			//setAttribute() 함수는 pageContext 객체가 제공하는 함수임.
			//setAttribute()는 원하는 scope(page,request,session,application)내에서
			//작용하고 있는 객체에 접근할수 있고, removeAttribute()함수를 사용해서
			//해당 객체의 내용을 삭제 가능하다. (aboutJSP p103)
			//아래 한줄에 의해 Board_Env 빈즈의 변수값들이 request 내에 저장되는듯...
			request.setAttribute("Board_Env", board_env);

			//따로 보낼 곳이 있을시 처리를 위한 변수 - 기본은 anbboard.jsp
			String redirectUrl = "";

			//글쓰기 글삭제 adminonly설정시 제한
			if ("write".equals(mode) || "reply".equals(mode) || "modify".equals(mode) || "delete".equals(mode)){

				//각 게시판의 관리자를 여러명 둘 때 로긴한 사용자가 글쓰기 권한이 있는 지 체크한다.
				//권한이 있으면 
				String owners_id = board_env.getOwnersId();
				boolean is_owner = false;
				
				ArrayList owners_list = new ArrayList();
				owners_list = com.anbtech.util.Token.getTokenList(owners_id);

				Iterator owners_list_iter = owners_list.iterator();

				while(owners_list_iter.hasNext()){
					if(((String)owners_list_iter.next()).equals(login_id)){
						is_owner = true;
					}
				}

				if ("y".equals(board_env.getAdminonly()) && is_owner == false){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('게시판 관리자만 사용할 수 있습니다.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;
				}
			}//여기까지 글쓰기 권한 체크


			// mode에 따라 각 항목으로 이동
			if("list".equals(mode)){
				//list의 request - Table_List, Redirect, Link_Category_List
				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = tableDAO.getTable_list(tablename,table_list,searchword,searchscope,category,boardpage,mapping);//table_list는 빈값을 이용
				request.setAttribute("Table_List", table_list);

				com.anbtech.board.business.RedirectBO redirectBO = new com.anbtech.board.business.RedirectBO(con);
				com.anbtech.board.entity.Redirect redirect = new com.anbtech.board.entity.Redirect();
				redirect = redirectBO.getRedirect(tablename,searchword,searchscope,category,boardpage, mapping);
				request.setAttribute("Redirect", redirect);

				com.anbtech.board.business.Board_EnvBO board_envBO = new com.anbtech.board.business.Board_EnvBO(con);
				ArrayList link_category_list = new ArrayList();
				link_category_list = board_envBO.getLink_category_list(tablename,category);
				request.setAttribute("Link_Category_List", link_category_list);
			}

			else if("menu".equals(mode)){
				com.anbtech.board.business.Board_EnvBO board_envBO = new com.anbtech.board.business.Board_EnvBO(con);
				ArrayList link_category_list = new ArrayList();
				link_category_list = board_envBO.getLink_category_list(tablename,category);
				request.setAttribute("Link_Category_List", link_category_list);
				getServletContext().getRequestDispatcher(com.anbtech.admin.db.ServerConfig.getConf("boardpath")+"/BbsLeft.jsp?tablename="+tablename).forward(request, response);
			}
			else if ("write".equals(mode) || "reply".equals(mode) || "modify".equals(mode)){
				//write,reply,modify의 request - Table, File_List, Redirect, Category_List
				com.anbtech.board.entity.Table table = new com.anbtech.board.entity.Table();
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				table = tableBO.getWrite_form(tablename, no, mode, category, com.anbtech.util.Module.getCookie(request));
				request.setAttribute("Table", table);

				ArrayList file_list = new ArrayList();
				if ("modify".equals(mode)){//modify일때만 File_List에 값을 넣는다.
					com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);
					file_list = tableDAO.getFile_list(tablename, no);
				}
				request.setAttribute("File_List", file_list);

				com.anbtech.board.entity.Redirect redirect = new com.anbtech.board.entity.Redirect();
				com.anbtech.board.business.RedirectBO redirectBO = new com.anbtech.board.business.RedirectBO(con);
				redirect = redirectBO.getRedirect(tablename,no,mode,searchword,searchscope,category,boardpage);
				request.setAttribute("Redirect", redirect);

				com.anbtech.board.business.Board_EnvBO board_envBO = new com.anbtech.board.business.Board_EnvBO(con);
				ArrayList category_list = new ArrayList();
				category_list = board_envBO.getCategory_list(tablename);
				category_list.remove(0);//전체보기걸러내기
				request.setAttribute("Category_List", category_list);
			}
			else if ("adminlogin".equals(mode)){
				//adminlogin의 request - Redirect
				com.anbtech.board.entity.Redirect redirect = new com.anbtech.board.entity.Redirect();
				com.anbtech.board.business.RedirectBO redirectBO = new com.anbtech.board.business.RedirectBO(con);
				redirect = redirectBO.getRedirect(tablename,no,mode,searchword,searchscope,category,boardpage);
				request.setAttribute("Redirect", redirect);
			}
			else if ("delete".equals(mode)){

					//delTable클래스호출 redirectUrl로 이동한다
					com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);
					tableDAO.delTable(tablename, no, filepath);

					redirectUrl = "AnBBoard?tablename=" + tablename + "&mode=list&boardpage=" + boardpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category;

/* 사번에 의해 글 삭제권한을 체크하므로 기존 프로세서를 수정함. (2003/03/04)
				//이곳은 admin모드로 들어왔을 경우 삭제하는 부분이다
				if((String)session.getAttribute(tablename + "_adminid") != null){

					//delTable클래스호출 redirectUrl로 이동한다
					TableDAO tableDAO = new TableDAO(con);
					tableDAO.delTable(tablename, no, boardpath);

					redirectUrl = "AnBBoard?tablename=" + tablename + "&mode=list&boardpage=" + boardpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category;

				}
				//admin모드가 아닐경우 delete페이지로 이동시킨다.
				else{
					Redirect redirect = new Redirect();
					com.anbtech.board.business.RedirectBO redirectBO = new com.anbtech.board.business.RedirectBO(con);
					redirect = redirectBO.getRedirect(tablename,no,mode,searchword,searchscope,category,boardpage);
					request.setAttribute("Redirect", redirect);
				}
*/
			}
			else if ("vote".equals(mode)){
				//vote는 redirectUrl로 이동한다.
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				tableBO.updVote(tablename, no, request.getRemoteAddr());

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=view&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no+"&multino="+multino;
			}
			else if ("download".equals(mode)){
				//따로 이동하는 곳 없이 다운로드시킨다.
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				com.anbtech.board.entity.Table file = new com.anbtech.board.entity.Table();
				file = tableBO.getFile_fordown(tablename, no);
				String filename = file.getFilename();
				String filetype = file.getFiletype();
				String filesize = file.getFilesize();
				String did = file.getDid();//did는 저장하기위한 것으로 1|2|3|4|이런식이다.

				//boardpath 에서 파일까지 경로 지정
//				String downFile = boardpath + "/upload/" + tablename + "/" + no + ".bin";
				String downFile = getServletContext().getRealPath("") + "/upload/board/" + tablename + "/" + no + ".bin";
			
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

				//다운받은 파일의 다운수를 높인다.
				tableBO.updDid(tablename,no.substring(0, no.lastIndexOf("_")), did, request.getRemoteAddr());
			}
			else if ("view".equals(mode)){
				//view가 가장 많은 request를 필요로 한다..
				// view의 request - Table_View, Redirect_View, Table_Cmt_List+i, Table_List, Redirect, Link_Category_List

				String no_type = "";
				if (multino.length() > 0)	no_type = multino;
				else if (no.length() > 0)	no_type = no;

				ArrayList no_list = com.anbtech.util.Token.getTokenList(no_type);

				com.anbtech.board.entity.Table table_cmt_form = new com.anbtech.board.entity.Table();
				String s[] = com.anbtech.util.Module.getCookie(request);
				if(s[0] != null) s[0] = com.anbtech.text.StringProcess.repWord(s[0], "\"", "&quot;");
				else s[0] = "";
				table_cmt_form.setWriter(s[0]);
				request.setAttribute("Table_Cmt_Form", table_cmt_form);
				
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				ArrayList table_view = tableBO.getTable_view(tablename, no_list, mapping);
				request.setAttribute("Table_View", table_view);

				com.anbtech.board.business.RedirectBO redirectBO = new com.anbtech.board.business.RedirectBO(con);
				ArrayList redirect_view = redirectBO.getRedirect(tablename, no_list, multino, mode, searchword, searchscope, category, boardpage);
				request.setAttribute("Redirect_View", redirect_view);

				int i = 0;
				while (i < no_list.size()){
					no = (String)no_list.get(i);
					com.anbtech.board.db.Table_CmtDAO table_cmtDAO = new com.anbtech.board.db.Table_CmtDAO(con);
					ArrayList table_cmt_list = table_cmtDAO.getTable_cmt_list(tablename, no, multino, searchword, searchscope, category, boardpage);
					request.setAttribute("Table_Cmt_List_"+i, table_cmt_list);
					i++;
				}

				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = tableDAO.getTable_list(tablename, no_list,searchword,searchscope,category,boardpage,mapping);
				request.setAttribute("Table_List", table_list);

				com.anbtech.board.entity.Redirect redirect = new com.anbtech.board.entity.Redirect();
				redirect = redirectBO.getRedirect(tablename,searchword,searchscope,category,boardpage, mapping);
				request.setAttribute("Redirect", redirect);

				com.anbtech.board.business.Board_EnvBO board_envBO = new com.anbtech.board.business.Board_EnvBO(con);
				ArrayList link_category_list = new ArrayList();
				link_category_list = board_envBO.getLink_category_list(tablename,category);
				request.setAttribute("Link_Category_List", link_category_list);

				//글읽은이수 rid 를 높인다.(글쓴이의 ip와 글 읽는 이의 ip체크후)
				tableBO.updRid(tablename,no, request.getRemoteAddr());
			}

			/**********************************************
			 * 관리자 모드에서 선태된 목록을 모두 삭제 처리
			 **********************************************/
			else if ("deleteall".equals(mode)){
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				tableBO.deleteAllChecked(tablename,no,filepath);

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=list";
			}

			//redirectUrl의 값이 있을시에는 redirectUrl경로로 이동하고
			//없을시에는 jmboard.jsp로 이동한다.
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);
			else getServletContext().getRequestDispatcher(com.anbtech.admin.db.ServerConfig.getConf("boardpath")+"/anbboard.jsp").forward(request, response);

		}catch (Exception e){
/* DAO 또는 BO에서 Exception이 발생할때 자바스크립트 소스를 사용하여 아래와 같은 처리를 하면 기능이 제대로 동작하지 않아 주석처리함.
   2004-10-07 by 박동렬
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
*/
			PrintWriter out = response.getWriter();
			out.println(e.toString());//에러메시지출력
			out.close();
		}finally{
			//con소멸
			close(con);
		}
	}

	/**********************************
	 * post방식으로 넘어왔을 때 처리
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		//업로드할 때 필요한 것들 tablename,upload_size, filepath 선언
		String tablename = request.getParameter("tablename");
		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "5";
		String boardpath = getServletContext().getRealPath("") + com.anbtech.admin.db.ServerConfig.getConf("boardpath");
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/board/" + tablename + "/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		//현재 접속중인 사용자 아이디 가져오기
		//세션이 종료되었을 경우 로긴 페이지로 강제 이동시킨다.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//전페이지에서 값 받아온다. multi에서 가져옴
		String mode = multi.getParameter("mode");
		String boardpage = multi.getParameter("boardpage");
		String searchword = multi.getParameter("searchword");
		String searchscope = multi.getParameter("searchscope");
		String no = multi.getParameter("no");
		String no_cmt = multi.getParameter("no_cmt");
		String multino = multi.getParameter("multino");

		String thread = multi.getParameter("thread");
		String pos = multi.getParameter("pos");
		String depth = multi.getParameter("depth");
		
		String writer = multi.getParameter("writer");
		String password = multi.getParameter("password");
		String email = multi.getParameter("email");
		String homepage = multi.getParameter("homepage");
		String subject = multi.getParameter("subject");
		String html = multi.getParameter("html");
		String content = multi.getParameter("content");
		String email_forward = multi.getParameter("email_forward");
		String comment = multi.getParameter("comment");
		String category = multi.getParameter("category");
		String notice = multi.getParameter("notice");
		
		String id = multi.getParameter("id");

		if (boardpage == null) boardpage = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (no == null) no = "";
		if (no_cmt == null) no_cmt = "";
		if (id == null) id = "";
		if ("http://".equals(homepage)) homepage = "";

		//공백으로 넘어오거나 null로 온 값들에 대한 처리.
		if (writer == null || writer.trim().length() == 0) writer = "";
		if (password == null || password.trim().length() == 0) password = "password";
		if (email == null || email.trim().length() == 0) email = "";
		if (homepage == null || homepage.trim().length() == 0) homepage = "";
		if (subject == null || subject.trim().length() == 0) subject = "제목없음";
		if (category == null) category = "";
		if (html == null) html = "n";	
		if (email_forward == null) email_forward="n";
		if (notice == null) notice = "n";

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//보낼 곳 처리를 위한 변수
			String redirectUrl = "";

			// mode에 따라 각 항목으로 이동

			if ("write".equals(mode)){

				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);

				//thread와 pos depth를 선언 최대값으로
				com.anbtech.board.entity.Table table_max = new com.anbtech.board.entity.Table();
				table_max = tableDAO.getTable_max(tablename);
				thread = table_max.getThread();
				pos = table_max.getPos();
				depth = "0";

				if("y".equals(notice)){
					thread = "0";
				}
				else{
					//공지사항이 있으면 공지사항 pos업 하고 pos값을 total_notice만큼 줄인다.
					int total_notice = tableDAO.getTotal(tablename, " WHERE thread=0");
					if(total_notice > 0){
						tableDAO.updTable(tablename, " SET pos=pos+1"," WHERE thread=0");
						pos = Integer.toString(Integer.parseInt(pos) - total_notice);
					}
				}

				//table insert
				tableDAO.setTable(tablename, thread, depth, pos, writer, email, homepage, request.getRemoteAddr(), password, subject, content, html, email_forward, category);

				//pos에 해당하는 no를 가져와 no값에 해당 file정보를 넣는다.
				no = tableDAO.getNo_bypos(tablename, pos);
				com.anbtech.board.entity.Table file = new com.anbtech.board.entity.Table();
				file = tableBO.getFile_frommulti(multi, no, filepath);
				tableBO.updFile(tablename, no, file.getFilename(), file.getFiletype(), file.getFilesize(), file.getDid());

				//쿠키를 저장한다.
				com.anbtech.util.Module.setCookie(response, writer, email, homepage);

				//이동할 경로 지정
				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=list&category="+category;
			}
			else if ("reply".equals(mode)){

				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);

				// no값의 table을 가져와서 emial_forward가 y시 메일 보내기 
				tableBO.sendMail(tablename, no, writer, subject, content, email);

				//입력하기전에 기존의 것 처리(pos를 하나씩 올린다 들어갈 칸 만들기)
				tableBO.updPos(tablename, pos);
				depth = Integer.toString(Integer.parseInt(depth)+1);

				tableDAO.setTable(tablename, thread, depth, pos, writer, email, homepage, request.getRemoteAddr(), password, subject, content, html, email_forward, category);

				no = tableDAO.getNo_bypos(tablename, pos);
				com.anbtech.board.entity.Table file = new com.anbtech.board.entity.Table();
				file = tableBO.getFile_frommulti(multi, no, filepath);
				tableBO.updFile(tablename, no, file.getFilename(), file.getFiletype(), file.getFilesize(), file.getDid());

				com.anbtech.util.Module.setCookie(response, writer, email, homepage);

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=list&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category;

			} else if ("modify".equals(mode)){

				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);

				//admin모드가 아닐 경우 password를 체크한다.
				if((String)session.getAttribute(tablename + "_adminid") == null) tableBO.chkWriter(tablename, multi, no, password);

				//Table UPDATE
				tableDAO.updTable(tablename, no, writer, email, homepage,subject, content, html, email_forward, request.getRemoteAddr(), category);

				//multi에서 파일정보를 가져와서 처리한다.
				ArrayList file_list = tableDAO.getFile_list(tablename,no);
				com.anbtech.board.entity.Table file = new com.anbtech.board.entity.Table();
				file = tableBO.getFile_frommulti(multi, no, filepath, file_list);
				tableBO.updFile(tablename, no, file.getFilename(), file.getFiletype(), file.getFilesize(), file.getDid());
					
				com.anbtech.util.Module.setCookie(response, writer, email, homepage);

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=list&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category;
			}
			else if("adminlogin".equals(mode)){
				//게시판에 맞는 id,password가 들어오면 true아니면 false
				com.anbtech.board.business.Board_EnvBO board_envBO = new com.anbtech.board.business.Board_EnvBO(con);
				boolean chklogin = board_envBO.chkLogin(tablename, id, password);

				if (chklogin){
					session.setAttribute(tablename+"_adminid", id);
					redirectUrl = "AnBBoard?tablename="+tablename+"&mode=list&boardpage="+boardpage+"&searchword"+searchword+"&searchscope="+searchscope+"&category="+category;
				}else {
					redirectUrl = "AnBBoard?tablename="+tablename+"&mode="+mode+"&boardpage="+boardpage+"&searchword"+searchword+"&searchscope="+searchscope+"&category="+category;
				}
			}
			else if("delete".equals(mode)){

				//password체크후 Table delete
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				tableBO.chkWriter(tablename, multi, no, password);
				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);
				tableDAO.delTable(tablename, no, boardpath);

				redirectUrl = "AnBBoard?tablename=" + tablename + "&mode=list&boardpage=" + boardpage + "&searchword=" + searchword + "&searchscope=" + searchscope+"&category="+category;

			}
			else if("comment".equals(mode)){
				
				//코맨트추가
				com.anbtech.board.db.Table_CmtDAO table_cmtDAO = new com.anbtech.board.db.Table_CmtDAO(con);
				table_cmtDAO.setTable_cmt(tablename, no, writer, comment, password);

				//쿠키를 저장한다.
				com.anbtech.util.Module.setCookie(response, writer);

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=view&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no+"&multino="+multino;
			}
			else if("comment_del".equals(mode)){

				//코맨트 삭제
				com.anbtech.board.business.Table_CmtBO table_cmtBO = new com.anbtech.board.business.Table_CmtBO(con);
				
				if((String)session.getAttribute(tablename + "_adminid") != null){//관리자일때
					table_cmtBO.delTable_cmt_admin(tablename,no_cmt);
				}else{//그냥 사용자일 때
					table_cmtBO.delTable_cmt_afterchk(tablename,no_cmt,password);
				}

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=view&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no+"&multino="+multino;
			}

			//redirectUrl의 값이 있을시에는 redirectUrl경로로 이동한다 - 100%이동
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
/* DAO 또는 BO에서 Exception이 발생할때 자바스크립트 소스를 사용하여 아래와 같은 처리를 하면 기능이 제대로 동작하지 않아 주석처리함.
   2004-10-07 by 박동렬
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
*/
			PrintWriter out = response.getWriter();
			out.println(e.toString());//에러메시지출력
			out.close();
		}finally{
			//con소멸
			close(con);
		}
	}
}