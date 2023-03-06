/*
 * AnBBoard.java
 * �Խ����� ��� ����� ����ϴ� contoller����
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

	//�Ҹ��� = con�Ҹ�
	public void close(Connection con) throws ServletException {
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get������� �Ѿ���� �� ó��
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//�ʿ��� �͵� ����
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		String boardpath = getServletContext().getRealPath("") + com.anbtech.admin.db.ServerConfig.getConf("boardpath");
		String mapping = "";//img��θ� ���� �� �ʿ�
//		if(!"".equals(com.anbtech.admin.db.ServerConfig.getConf("boardpath"))) mapping = com.anbtech.admin.db.ServerConfig.getConf("boardpath").substring(1) + "/";
//		if("/servlet/AnBBoard".equals(request.getServletPath())) mapping = "../" + mapping;

		//������������ �� �޾ƿ´�.
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

		if (mode == null) mode = "list";	//ó������ mode�� �� �Ѿ���Ƿ� mode�� list�� ����
		if (boardpage == null) boardpage = "1";
		if (searchword == null) searchword = "";

		//�˻��� �� �� ��� StringProcess�� Ŭ������ ������ ��
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);

		if (searchscope == null) searchscope = "";
		if (password == null) password = "";
		if (no == null) no = "";
		if (multino == null) multino = "";
		if (category == null) category = "";

		//���� �������� ����� ���̵� ��������
		//������ ����Ǿ��� ��� �α� �������� ���� �̵���Ų��.
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

			//board_env ���̺��� �ش� �Խ��� ������ ������ ��,
			//Board_Env ��� ���� �ִ´�.
			com.anbtech.board.db.Board_EnvDAO board_envDAO = new com.anbtech.board.db.Board_EnvDAO(con);
			com.anbtech.board.entity.Board_Env board_env = board_envDAO.getBoard_env(tablename);
			
			board_env.setMode(mode);//�߰������� ���� �͵�..
			board_env.setMapping(mapping);

			//request���ٰ� Board_Env�̸� ���� board_env�� �ִ´�.
			//
			//setAttribute() �Լ��� pageContext ��ü�� �����ϴ� �Լ���.
			//setAttribute()�� ���ϴ� scope(page,request,session,application)������
			//�ۿ��ϰ� �ִ� ��ü�� �����Ҽ� �ְ�, removeAttribute()�Լ��� ����ؼ�
			//�ش� ��ü�� ������ ���� �����ϴ�. (aboutJSP p103)
			//�Ʒ� ���ٿ� ���� Board_Env ������ ���������� request ���� ����Ǵµ�...
			request.setAttribute("Board_Env", board_env);

			//���� ���� ���� ������ ó���� ���� ���� - �⺻�� anbboard.jsp
			String redirectUrl = "";

			//�۾��� �ۻ��� adminonly������ ����
			if ("write".equals(mode) || "reply".equals(mode) || "modify".equals(mode) || "delete".equals(mode)){

				//�� �Խ����� �����ڸ� ������ �� �� �α��� ����ڰ� �۾��� ������ �ִ� �� üũ�Ѵ�.
				//������ ������ 
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
					out.println("	alert('�Խ��� �����ڸ� ����� �� �ֽ��ϴ�.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;
				}
			}//������� �۾��� ���� üũ


			// mode�� ���� �� �׸����� �̵�
			if("list".equals(mode)){
				//list�� request - Table_List, Redirect, Link_Category_List
				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);
				ArrayList table_list = new ArrayList();
				table_list = tableDAO.getTable_list(tablename,table_list,searchword,searchscope,category,boardpage,mapping);//table_list�� ���� �̿�
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
				//write,reply,modify�� request - Table, File_List, Redirect, Category_List
				com.anbtech.board.entity.Table table = new com.anbtech.board.entity.Table();
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				table = tableBO.getWrite_form(tablename, no, mode, category, com.anbtech.util.Module.getCookie(request));
				request.setAttribute("Table", table);

				ArrayList file_list = new ArrayList();
				if ("modify".equals(mode)){//modify�϶��� File_List�� ���� �ִ´�.
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
				category_list.remove(0);//��ü����ɷ�����
				request.setAttribute("Category_List", category_list);
			}
			else if ("adminlogin".equals(mode)){
				//adminlogin�� request - Redirect
				com.anbtech.board.entity.Redirect redirect = new com.anbtech.board.entity.Redirect();
				com.anbtech.board.business.RedirectBO redirectBO = new com.anbtech.board.business.RedirectBO(con);
				redirect = redirectBO.getRedirect(tablename,no,mode,searchword,searchscope,category,boardpage);
				request.setAttribute("Redirect", redirect);
			}
			else if ("delete".equals(mode)){

					//delTableŬ����ȣ�� redirectUrl�� �̵��Ѵ�
					com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);
					tableDAO.delTable(tablename, no, filepath);

					redirectUrl = "AnBBoard?tablename=" + tablename + "&mode=list&boardpage=" + boardpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category;

/* ����� ���� �� ���������� üũ�ϹǷ� ���� ���μ����� ������. (2003/03/04)
				//�̰��� admin���� ������ ��� �����ϴ� �κ��̴�
				if((String)session.getAttribute(tablename + "_adminid") != null){

					//delTableŬ����ȣ�� redirectUrl�� �̵��Ѵ�
					TableDAO tableDAO = new TableDAO(con);
					tableDAO.delTable(tablename, no, boardpath);

					redirectUrl = "AnBBoard?tablename=" + tablename + "&mode=list&boardpage=" + boardpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category;

				}
				//admin��尡 �ƴҰ�� delete�������� �̵���Ų��.
				else{
					Redirect redirect = new Redirect();
					com.anbtech.board.business.RedirectBO redirectBO = new com.anbtech.board.business.RedirectBO(con);
					redirect = redirectBO.getRedirect(tablename,no,mode,searchword,searchscope,category,boardpage);
					request.setAttribute("Redirect", redirect);
				}
*/
			}
			else if ("vote".equals(mode)){
				//vote�� redirectUrl�� �̵��Ѵ�.
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				tableBO.updVote(tablename, no, request.getRemoteAddr());

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=view&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no+"&multino="+multino;
			}
			else if ("download".equals(mode)){
				//���� �̵��ϴ� �� ���� �ٿ�ε��Ų��.
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				com.anbtech.board.entity.Table file = new com.anbtech.board.entity.Table();
				file = tableBO.getFile_fordown(tablename, no);
				String filename = file.getFilename();
				String filetype = file.getFiletype();
				String filesize = file.getFilesize();
				String did = file.getDid();//did�� �����ϱ����� ������ 1|2|3|4|�̷����̴�.

				//boardpath ���� ���ϱ��� ��� ����
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

				//�ٿ���� ������ �ٿ���� ���δ�.
				tableBO.updDid(tablename,no.substring(0, no.lastIndexOf("_")), did, request.getRemoteAddr());
			}
			else if ("view".equals(mode)){
				//view�� ���� ���� request�� �ʿ�� �Ѵ�..
				// view�� request - Table_View, Redirect_View, Table_Cmt_List+i, Table_List, Redirect, Link_Category_List

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

				//�������̼� rid �� ���δ�.(�۾����� ip�� �� �д� ���� ipüũ��)
				tableBO.updRid(tablename,no, request.getRemoteAddr());
			}

			/**********************************************
			 * ������ ��忡�� ���µ� ����� ��� ���� ó��
			 **********************************************/
			else if ("deleteall".equals(mode)){
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				tableBO.deleteAllChecked(tablename,no,filepath);

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=list";
			}

			//redirectUrl�� ���� �����ÿ��� redirectUrl��η� �̵��ϰ�
			//�����ÿ��� jmboard.jsp�� �̵��Ѵ�.
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);
			else getServletContext().getRequestDispatcher(com.anbtech.admin.db.ServerConfig.getConf("boardpath")+"/anbboard.jsp").forward(request, response);

		}catch (Exception e){
/* DAO �Ǵ� BO���� Exception�� �߻��Ҷ� �ڹٽ�ũ��Ʈ �ҽ��� ����Ͽ� �Ʒ��� ���� ó���� �ϸ� ����� ����� �������� �ʾ� �ּ�ó����.
   2004-10-07 by �ڵ���
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
*/
			PrintWriter out = response.getWriter();
			out.println(e.toString());//�����޽������
			out.close();
		}finally{
			//con�Ҹ�
			close(con);
		}
	}

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
		if(upload_size == null) upload_size = "5";
		String boardpath = getServletContext().getRealPath("") + com.anbtech.admin.db.ServerConfig.getConf("boardpath");
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/board/" + tablename + "/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//��ξ����� �������
		}

		//���� �������� ����� ���̵� ��������
		//������ ����Ǿ��� ��� �α� �������� ���� �̵���Ų��.
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			PrintWriter out = response.getWriter();
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //�ش� ��η� ���ε��Ѵ�

		//������������ �� �޾ƿ´�. multi���� ������
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

		//�������� �Ѿ���ų� null�� �� ���鿡 ���� ó��.
		if (writer == null || writer.trim().length() == 0) writer = "";
		if (password == null || password.trim().length() == 0) password = "password";
		if (email == null || email.trim().length() == 0) email = "";
		if (homepage == null || homepage.trim().length() == 0) homepage = "";
		if (subject == null || subject.trim().length() == 0) subject = "�������";
		if (category == null) category = "";
		if (html == null) html = "n";	
		if (email_forward == null) email_forward="n";
		if (notice == null) notice = "n";

		try {
			// con����
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//���� �� ó���� ���� ����
			String redirectUrl = "";

			// mode�� ���� �� �׸����� �̵�

			if ("write".equals(mode)){

				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);

				//thread�� pos depth�� ���� �ִ밪����
				com.anbtech.board.entity.Table table_max = new com.anbtech.board.entity.Table();
				table_max = tableDAO.getTable_max(tablename);
				thread = table_max.getThread();
				pos = table_max.getPos();
				depth = "0";

				if("y".equals(notice)){
					thread = "0";
				}
				else{
					//���������� ������ �������� pos�� �ϰ� pos���� total_notice��ŭ ���δ�.
					int total_notice = tableDAO.getTotal(tablename, " WHERE thread=0");
					if(total_notice > 0){
						tableDAO.updTable(tablename, " SET pos=pos+1"," WHERE thread=0");
						pos = Integer.toString(Integer.parseInt(pos) - total_notice);
					}
				}

				//table insert
				tableDAO.setTable(tablename, thread, depth, pos, writer, email, homepage, request.getRemoteAddr(), password, subject, content, html, email_forward, category);

				//pos�� �ش��ϴ� no�� ������ no���� �ش� file������ �ִ´�.
				no = tableDAO.getNo_bypos(tablename, pos);
				com.anbtech.board.entity.Table file = new com.anbtech.board.entity.Table();
				file = tableBO.getFile_frommulti(multi, no, filepath);
				tableBO.updFile(tablename, no, file.getFilename(), file.getFiletype(), file.getFilesize(), file.getDid());

				//��Ű�� �����Ѵ�.
				com.anbtech.util.Module.setCookie(response, writer, email, homepage);

				//�̵��� ��� ����
				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=list&category="+category;
			}
			else if ("reply".equals(mode)){

				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);

				// no���� table�� �����ͼ� emial_forward�� y�� ���� ������ 
				tableBO.sendMail(tablename, no, writer, subject, content, email);

				//�Է��ϱ����� ������ �� ó��(pos�� �ϳ��� �ø��� �� ĭ �����)
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

				//admin��尡 �ƴ� ��� password�� üũ�Ѵ�.
				if((String)session.getAttribute(tablename + "_adminid") == null) tableBO.chkWriter(tablename, multi, no, password);

				//Table UPDATE
				tableDAO.updTable(tablename, no, writer, email, homepage,subject, content, html, email_forward, request.getRemoteAddr(), category);

				//multi���� ���������� �����ͼ� ó���Ѵ�.
				ArrayList file_list = tableDAO.getFile_list(tablename,no);
				com.anbtech.board.entity.Table file = new com.anbtech.board.entity.Table();
				file = tableBO.getFile_frommulti(multi, no, filepath, file_list);
				tableBO.updFile(tablename, no, file.getFilename(), file.getFiletype(), file.getFilesize(), file.getDid());
					
				com.anbtech.util.Module.setCookie(response, writer, email, homepage);

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=list&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category;
			}
			else if("adminlogin".equals(mode)){
				//�Խ��ǿ� �´� id,password�� ������ true�ƴϸ� false
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

				//passwordüũ�� Table delete
				com.anbtech.board.business.TableBO tableBO = new com.anbtech.board.business.TableBO(con);
				tableBO.chkWriter(tablename, multi, no, password);
				com.anbtech.board.db.TableDAO tableDAO = new com.anbtech.board.db.TableDAO(con);
				tableDAO.delTable(tablename, no, boardpath);

				redirectUrl = "AnBBoard?tablename=" + tablename + "&mode=list&boardpage=" + boardpage + "&searchword=" + searchword + "&searchscope=" + searchscope+"&category="+category;

			}
			else if("comment".equals(mode)){
				
				//�ڸ�Ʈ�߰�
				com.anbtech.board.db.Table_CmtDAO table_cmtDAO = new com.anbtech.board.db.Table_CmtDAO(con);
				table_cmtDAO.setTable_cmt(tablename, no, writer, comment, password);

				//��Ű�� �����Ѵ�.
				com.anbtech.util.Module.setCookie(response, writer);

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=view&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no+"&multino="+multino;
			}
			else if("comment_del".equals(mode)){

				//�ڸ�Ʈ ����
				com.anbtech.board.business.Table_CmtBO table_cmtBO = new com.anbtech.board.business.Table_CmtBO(con);
				
				if((String)session.getAttribute(tablename + "_adminid") != null){//�������϶�
					table_cmtBO.delTable_cmt_admin(tablename,no_cmt);
				}else{//�׳� ������� ��
					table_cmtBO.delTable_cmt_afterchk(tablename,no_cmt,password);
				}

				redirectUrl = "AnBBoard?tablename="+tablename+"&mode=view&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no+"&multino="+multino;
			}

			//redirectUrl�� ���� �����ÿ��� redirectUrl��η� �̵��Ѵ� - 100%�̵�
			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
/* DAO �Ǵ� BO���� Exception�� �߻��Ҷ� �ڹٽ�ũ��Ʈ �ҽ��� ����Ͽ� �Ʒ��� ���� ó���� �ϸ� ����� ����� �������� �ʾ� �ּ�ó����.
   2004-10-07 by �ڵ���
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
*/
			PrintWriter out = response.getWriter();
			out.println(e.toString());//�����޽������
			out.close();
		}finally{
			//con�Ҹ�
			close(con);
		}
	}
}