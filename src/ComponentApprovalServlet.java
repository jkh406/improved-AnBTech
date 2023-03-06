import com.anbtech.ca.entity.*;
import com.anbtech.ca.db.*;
import com.anbtech.ca.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class ComponentApprovalServlet extends HttpServlet {

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
		String aid			= request.getParameter("aid");				// 전자결재 관리번호
		String item_no		= request.getParameter("item_no");

		if (mode == null) mode = "list";
		if (no == null) no = "";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

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

			///////////////////////////////////////////////////////////
			// 승인문건 리스트 보기
			///////////////////////////////////////////////////////////
			if(mode.equals("list")){	
				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
				ArrayList ca_list = new ArrayList();

				ca_list = caDAO.getCaMasterList(mode,searchword,searchscope,category,page);
				request.setAttribute("CA_List", ca_list);

				com.anbtech.ca.business.CaLinkUrlBO redirectBO = new com.anbtech.ca.business.CaLinkUrlBO(con);
				com.anbtech.ca.entity.CaLinkUrl redirect = new com.anbtech.ca.entity.CaLinkUrl();
				redirect = redirectBO.getRedirect(mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/ca/list.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 마이폴더 리스트
			///////////////////////////////////////////////////////////
			else if(mode.equals("mylist")){	
				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
				ArrayList ca_list = new ArrayList();

				ca_list = caDAO.getCaMasterListByRequestorId(login_id,page);
				request.setAttribute("CA_List", ca_list);

				com.anbtech.ca.business.CaLinkUrlBO redirectBO = new com.anbtech.ca.business.CaLinkUrlBO(con);
				com.anbtech.ca.entity.CaLinkUrl redirect = new com.anbtech.ca.entity.CaLinkUrl();
				redirect = redirectBO.getRedirect(login_id,page);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/ca/mylist.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 승인번호별 상세정보 보기
			///////////////////////////////////////////////////////////
			else if(mode.equals("view_a")){	
				com.anbtech.ca.entity.CaMasterTable table = new com.anbtech.ca.entity.CaMasterTable();
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);

				table = caBO.getViewForm(no);
				request.setAttribute("CA_Info", table);

				getServletContext().getRequestDispatcher("/ca/view_a.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 품목번호별 상세정보 보기
			///////////////////////////////////////////////////////////
			else if(mode.equals("view_i")){	
				com.anbtech.ca.entity.CaMasterTable master_table = new com.anbtech.ca.entity.CaMasterTable();
				com.anbtech.ca.entity.CaHistoryInfoTable history_table = new com.anbtech.ca.entity.CaHistoryInfoTable();
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);

				//품목정보 가져오기
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
				com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();
				part = cmDAO.getItemInfo(item_no);
				request.setAttribute("PART_INFO", part);

				ArrayList history_list = new ArrayList();
				history_list = caDAO.getHistoryList(item_no);
				request.setAttribute("History_List", history_list);

				ArrayList approval_list = new ArrayList();
				approval_list = caDAO.getApprovalListByItemNo(item_no);
				request.setAttribute("Approval_List", approval_list);

				getServletContext().getRequestDispatcher("/ca/view_i.jsp?no="+no+"&item_no="+item_no).forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 신규승인의뢰,신규승인의뢰(다중),사양변경,수정,승인업체추가
			///////////////////////////////////////////////////////////
			else if(mode.equals("write_s") || mode.equals("write_m") || mode.equals("write_r") || mode.equals("write_a") || mode.equals("modify")){	

				com.anbtech.ca.entity.CaMasterTable table = new com.anbtech.ca.entity.CaMasterTable();
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);

				//신규등록 모드인 경우 로긴 정보를 가져와서 세팅한다.
				table = caBO.getWriteForm(mode,no,item_no);
				if(mode.equals("write_s") || mode.equals("write_m") || mode.equals("write_r") || mode.equals("write_a")){
					com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
					com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
					userinfo = userDAO.getUserListById(login_id);
					String division = userinfo.getDivision();
					String user_name = userinfo.getUserName();
					String user_rank = userinfo.getUserRank();

					table.setRequestorInfo(division+"/"+user_rank+"/"+user_name);
				}
				request.setAttribute("CA_Info", table);

				//수정모드인 경우 첨부파일 리스르를 가져온다.
				ArrayList file_list = new ArrayList();
				if(mode.equals("modify")) file_list = caDAO.getFile_list(no);
				request.setAttribute("File_List", file_list);

				//다중승인의뢰 모드인 경우, 이전에 등록된 문건들의 리스트를 가져온다.
				ArrayList approval_list = new ArrayList();
				if(mode.equals("write_m")) approval_list = caBO.getApprovalList(no);
				request.setAttribute("Approval_List", approval_list);

				getServletContext().getRequestDispatcher("/ca/write.jsp?no="+no).forward(request,response);
			}

			///////////////////////////////////////////////////////////
			//사용금지 신청 화면 처리
			///////////////////////////////////////////////////////////
			else if(mode.equals("write_d")){	
				com.anbtech.ca.entity.CaMasterTable table = new com.anbtech.ca.entity.CaMasterTable();
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);

				table = caBO.getViewForm(no);
				request.setAttribute("CA_Info", table);

				getServletContext().getRequestDispatcher("/ca/write_d.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 첨부파일 다운로드
			///////////////////////////////////////////////////////////
			else if (mode.equals("download")){	

				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				com.anbtech.ca.entity.CaMasterTable file = new com.anbtech.ca.entity.CaMasterTable();
				file = caBO.getFile_fordown(no);
				String filename = file.getFileName();
				String filetype = file.getFileType();
				String filesize = file.getFileSize();

				//boardpath 에서 파일까지 경로 지정
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/ca/" + umask + ".bin";
			
				if (filetype.indexOf("mage")<=0)
					filetype = "application/unknown";
				
				String strClient=request.getHeader("User-Agent");

				///////////////////////////////////////////////////////////
				//파일명을 아래와 같이 영문처리 했을때 한글이 깨지지 않았음.
				///////////////////////////////////////////////////////////
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

			///////////////////////////////////////////////////////////
			// 승인 처리
			///////////////////////////////////////////////////////////
			else if (mode.equals("commit")){	

				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
				com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

				String write_type = no.substring(no.lastIndexOf("|")+1, no.length());
				no = no.substring(0, no.lastIndexOf("|"));

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				/************************************************************************
				if(level == 1) 
					conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				else if(level == 2) 
					conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				else if(level == 3) 
					conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				*************************************************************************/

				try{

					// 1.전자결재 모듈에서 결재 정보(app_save table)를 가져온 후, approval_info 테이블에 저장한다.
					appDAO.getAppInfoAndSave("ca_approval_info",aid);

					// 2.임시승인번호를 입력으로 받아 정식승인 번호를 구한다.
					String approval_no = caBO.getApprovalNo(no);

					//////////////////////////////////////
					// 3.신청모드에 따른 처리
					/////////////////////////////////////
					if(write_type.equals("report_w") || write_type.equals("report_r")){
						caBO.updateApprovalAndSaveHistoryInfo(write_type,no,approval_no,aid);
					}else if(write_type.equals("report_d")){
						caBO.deleteApprovalAndSaveHistoryInfo(no,aid);
					}

					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "ApprovalProcessServlet?mode=APP";
			}

			///////////////////////////////////////////////////////////
			// 상신 처리
			///////////////////////////////////////////////////////////
			else if(mode.equals("eapproval")){	

				String tmp_approval_no	= System.currentTimeMillis() + "";	// 현재 시간으로 임시승인번호를 생성				
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				caBO.updateApprovalInfos(no,tmp_approval_no);

				getServletContext().getRequestDispatcher("/ca/confirm_approval.jsp?mode=report_w&no="+tmp_approval_no).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 전자결재용 화면 (신규 등록 의뢰시)
			///////////////////////////////////////////////////
			else if (mode.equals("report_w")){	
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				ArrayList approval_list = new ArrayList();
				approval_list = caBO.getApprovalList(no);
				request.setAttribute("Approval_List", approval_list);

				getServletContext().getRequestDispatcher("/ca/report_w.jsp?no="+no).forward(request,response);			
			}

			///////////////////////////////////////////////////////////
			// 전자결재용 화면 (사양 변경 의뢰)
			///////////////////////////////////////////////////////////
			else if (mode.equals("report_r")){	
				com.anbtech.ca.entity.CaMasterTable new_table = new com.anbtech.ca.entity.CaMasterTable();
				com.anbtech.ca.entity.CaMasterTable old_table = new com.anbtech.ca.entity.CaMasterTable();
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);

				String new_mid = caDAO.getMidByTmpApprovalNo(no);
				new_table = caBO.getViewForm(new_mid);
				request.setAttribute("NEW_Info", new_table);

				String old_mid = new_table.getAncestor();
				old_table = caBO.getViewForm(old_mid);
				request.setAttribute("OLD_Info", old_table);

				getServletContext().getRequestDispatcher("/ca/report_r.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 전자결재용 화면 (사용금지 의뢰)
			///////////////////////////////////////////////////////////
			else if (mode.equals("report_d")){	
				com.anbtech.ca.entity.CaMasterTable table = new com.anbtech.ca.entity.CaMasterTable();
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);

				table = caBO.getViewForm(no);
				request.setAttribute("CA_Info", table);

				getServletContext().getRequestDispatcher("/ca/report_d.jsp").forward(request,response);			
			}

			///////////////////////////////////////////////////////////
			// 삭제 처리
			///////////////////////////////////////////////////////////
			else if(mode.equals("drop")){	
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				caBO.dropApprovalInfos(no);
				redirectUrl = "ComponentApprovalServlet?mode=mylist";
			}

			///////////////////////////////////////////////////////////
			// 인쇄폼으로 출력
			///////////////////////////////////////////////////////////
			else if(mode.equals("print")){	
				com.anbtech.ca.entity.CaMasterTable table = new com.anbtech.ca.entity.CaMasterTable();
				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);

				table = caBO.getViewForm(no);
				request.setAttribute("CA_Info", table);

				com.anbtech.admin.entity.ApprovalInfoTable app_table = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
				aid = table.getAid();
//				String sign_path = "http://192.168.1.103/anb/gw/approval/sign/";
				String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
				app_table = appDAO.getApprovalInfo("ca_approval_info",aid,sign_path);
				request.setAttribute("Approval_Info", app_table);

				getServletContext().getRequestDispatcher("/ca/print.jsp").forward(request,response);
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
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/ca/";
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
		String no = multi.getParameter("no");

		String no_type			= multi.getParameter("no_type");
		String pjt_code			= multi.getParameter("pjt_code");
		String pjt_name			= multi.getParameter("pjt_name");
		String model_code		= multi.getParameter("model_code");
		String model_name		= multi.getParameter("model_name");
		String item_no			= multi.getParameter("item_no");
		String item_name		= multi.getParameter("item_name");
		String item_desc		= multi.getParameter("item_desc");
		String item_type		= multi.getParameter("item_type");
		String item_unit		= multi.getParameter("item_unit");
		String maker_code		= multi.getParameter("maker_code");
		String maker_name		= multi.getParameter("maker_name");
		String maker_part_no	= multi.getParameter("maker_part_no");
		String approve_type		= multi.getParameter("approve_type");
		String apply_date		= multi.getParameter("apply_date");
		String apply_quantity	= multi.getParameter("apply_quantity");
		String why_approve		= multi.getParameter("why_approve");
		String other_info		= multi.getParameter("other_info");
		String change_info		= multi.getParameter("change_info");
		String is_file_same		= multi.getParameter("is_file_same")==null?"":multi.getParameter("is_file_same");


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
			
			// mode에 따라 각 항목으로 이동

			///////////////////////////////////////////////////////////
			// 승인의뢰정보 등록 처리
			///////////////////////////////////////////////////////////
			if (mode.equals("write_s") || mode.equals("write_m") || mode.equals("write_a")){

				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
				com.anbtech.ca.entity.CaMasterTable table = new com.anbtech.ca.entity.CaMasterTable();

				//mode == write_s  일 경우 임시승인번호를 구한다.
				if(mode.equals("write_s") || mode.equals("write_a")){
					no	= System.currentTimeMillis() + "";	// 현재 시간으로 임시승인번호를 생성
				}


				//바로 이전에 등록된 문건과 첨부파일이 동일할 경우
				if(is_file_same.equals("same")){
					String mid = caDAO.getMaxMid(no);			// 이전에 등록된 문건의 mid를 가져온다.
					table = caDAO.getApprovalInfoByMid(mid);	//첨부파일 정보를 가져온다.
					String umask = table.getUmask();
					String fname = table.getFileName();
					String fsize = table.getFileSize();
					String ftype = table.getFileType();

					caDAO.saveApprovalInfo(no,login_id,pjt_code,pjt_name,model_code,model_name,item_no,item_name,item_type,item_unit,maker_code,maker_name,maker_part_no,item_desc,approve_type,apply_date,apply_quantity,why_approve,other_info,no_type,umask,"NEW");

					caBO.updFile(umask, fname, ftype, fsize);

				}else{
					//umask 를 생성한다.
					String umask = System.currentTimeMillis() + "";

					caDAO.saveApprovalInfo(no,login_id,pjt_code,pjt_name,model_code,model_name,item_no,item_name,item_type,item_unit,maker_code,maker_name,maker_part_no,item_desc,approve_type,apply_date,apply_quantity,why_approve,other_info,no_type,umask,"NEW");

					com.anbtech.ca.entity.CaMasterTable file = new com.anbtech.ca.entity.CaMasterTable();
					//첨부파일 업로더
					file = caBO.getFile_frommulti(multi, umask, filepath);

					//업로딩 된 첨부파일 정보를 DB에 저장하기
					caBO.updFile(umask, file.getFileName(), file.getFileType(), file.getFileSize());				
				}

				getServletContext().getRequestDispatcher("/ca/confirm.jsp?mode=report_w&no="+no).forward(request,response);

			}
			
			///////////////////////////////////////////////////////////
			//사양변경 의뢰 처리
			///////////////////////////////////////////////////////////
			if (mode.equals("write_r")){

				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);

				String tmp_approval_no	= System.currentTimeMillis() + "";	// 현재 시간으로 임시승인번호를 생성

				//umask 를 생성한다.
				String umask = System.currentTimeMillis() + "";

				caDAO.saveApprovalInfo(tmp_approval_no,login_id,pjt_code,pjt_name,model_code,model_name,item_no,item_name,item_type,item_unit,maker_code,maker_name,maker_part_no,item_desc,approve_type,apply_date,apply_quantity,why_approve,other_info,no_type,umask,no);

				com.anbtech.ca.entity.CaMasterTable file = new com.anbtech.ca.entity.CaMasterTable();
				//첨부파일 업로더
				file = caBO.getFile_frommulti(multi, umask, filepath);

				//업로딩 된 첨부파일 정보를 DB에 저장하기
				caBO.updFile(umask, file.getFileName(), file.getFileType(), file.getFileSize());


				getServletContext().getRequestDispatcher("/ca/confirm.jsp?mode=report_r&no="+tmp_approval_no).forward(request,response);

			}

			///////////////////////////////////////////////////////////
			//사용금지 신청 의뢰 처리
			///////////////////////////////////////////////////////////
			if (mode.equals("write_d")){

				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
				caDAO.updateApprovalInfo(no,login_id,change_info);

				getServletContext().getRequestDispatcher("/ca/confirm.jsp?mode=report_d&no="+no).forward(request,response);

			}

			///////////////////////////////////////////////////////////
			//수정 처리
			///////////////////////////////////////////////////////////
			if (mode.equals("modify")){

				com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
				com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);

				caDAO.updateApprovalInfo(no,pjt_code,model_code,item_no,item_unit,maker_code,maker_part_no,item_desc,approve_type,apply_date,apply_quantity,why_approve,other_info,no_type);

				//첨부파일 정보 업데이트
				ArrayList file_list = caDAO.getFile_list(no);
				String umask = caDAO.getUmask(no);

				com.anbtech.ca.entity.CaMasterTable file = new com.anbtech.ca.entity.CaMasterTable();
				file = caBO.getFile_frommulti(multi,umask,filepath,file_list);

				caBO.updFile(umask, file.getFileName(), file.getFileType(), file.getFileSize());

				redirectUrl = "ComponentApprovalServlet?mode=mylist";
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
