import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.business.*;
import com.anbtech.dms.admin.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class AnBDMS extends HttpServlet {

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

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String tablename	= request.getParameter("tablename");	//tablename 으로 문서종류 결정(기술문서,제안서,일반문서)	
		String no			= request.getParameter("no");			//master_data 테이블의 레코드 관리번호
		String t_id			= request.getParameter("t_id");			//문서종류별 테이블의 레코드 관리번호
		String data_id		= request.getParameter("d_id");			//데이터 번호
		String ver_code		= request.getParameter("ver");			//버젼코드
		String aid			= request.getParameter("aid");			//전자결재 관리번호
		
		String mode			= request.getParameter("mode");			//모드
		String page			= request.getParameter("page");			//페이지

		//검색시에 넘어오는 파라미터들
		String searchword	= request.getParameter("searchword");	//검색어
		String searchscope	= request.getParameter("searchscope");	//검색필드
		String category		= request.getParameter("category");		//카테고리 코드
		String org_category = request.getParameter("org_category");	//목록보기 버튼 링크 문자열 생성을 위한 것.

		if (mode == null) mode = "list";							//처음에는 mode가 안 넘어오므로 mode를 list로 세팅
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);

		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

		//대출 신청 사유 또는 반려 사유
		String why_revision	= request.getParameter("why");			//리비젼 하는 사유
		String loan_day		= request.getParameter("loan_day");		//대출기간
		String why			= request.getParameter("why");			//대출처리시에 관리자가 남기는 메시지
		String copy_num		= request.getParameter("copy_num");		//대출 카피수
		String return_date	= request.getParameter("return_date");	//대출 기간
		if (why == null) why = "";
		else why = com.anbtech.text.StringProcess.kwordProcess(why);
		if (copy_num == null) copy_num = "1";
		if (return_date == null) return_date = "처리중";

		String redirectUrl = "";

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
		String login_name = sl.name;
		String login_division = sl.division;


		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			///////////////////////////////////////////////////////////
			// 목록 보기 모드
			// 1.넘어온 카레코리 코드를 가지고 문서 종류를 파악한다.
			// 2.해당문서의 테이블에서 조건에 맞는 리스트를 가져온다.
			///////////////////////////////////////////////////////////
			if("list".equals(mode) || "processing".equals(mode) || "mylist".equals(mode)){

				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				//카테고리 ID에 해당하는 테이블명을 가져온다.
				if(tablename == null) tablename = masterDAO.getTableName(category);

				ArrayList table_list = new ArrayList();

				//테이블명에 따라서 조건에 맞는 데이터를 가져온다.
				//master_data(전체 문서) 내용 가져오기
				if(tablename.equals("master_data")){
					table_list = masterDAO.getMasterData_List(login_id,tablename,mode,searchword,searchscope,category,page);
					request.setAttribute("MasterData_List", table_list);
				}
				//techdoc_data(기술문서) 내용 가져오기
				else if(tablename.equals("techdoc_data")){
					com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
					table_list = techdocDAO.getTechDocData_List(login_id,tablename,mode,searchword,searchscope,category,page);
					request.setAttribute("TechdocData_List", table_list);
				}
				//proposal_data(제안서) 내용 가져오기
				else if(tablename.equals("proposal_data")){
					com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);
					table_list = proposalDAO.getProposalData_List(login_id,tablename,mode,searchword,searchscope,category,page);
					request.setAttribute("ProposalData_List", table_list);
				}

				//목록 보기 모드에 필요한 링크 생성
				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();
				redirect = redirectBO.getRedirect(tablename,mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);
				
				//모드에 따라 적절한 jsp 페이지로 분기한다.
				if(mode.equals("mylist")){
					getServletContext().getRequestDispatcher("/dms/"+tablename+"/mylist.jsp").forward(request,response);
				}else{
					getServletContext().getRequestDispatcher("/dms/"+tablename+"/list.jsp?mode="+mode).forward(request,response);				
				}
			}


			///////////////////////////////////////////////////////////
			// 대출의뢰 문서 리스트
			///////////////////////////////////////////////////////////
			else if("loan".equals(mode)){
				tablename = "loan_list"; //대출의뢰문서 리스트 테이블 이름 지정
				
				ArrayList table_list = new ArrayList();

				//loan_list 내용 가져오기
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				table_list = loanDAO.getLoan_List(tablename,mode,searchword,searchscope,page);
				request.setAttribute("Loan_List", table_list);

				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();
				redirect = redirectBO.getRedirect(tablename,mode,searchword,searchscope,page);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/dms/"+tablename+"/list.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 대출의뢰된 문서 정보 상세 보기
			///////////////////////////////////////////////////////////
			else if ("view_l".equals(mode)){
		
				com.anbtech.dms.entity.LoanTable loan = new com.anbtech.dms.entity.LoanTable();
				com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);

				loan = loanBO.getData("loan_list",no);
				request.setAttribute("LoanInfo", loan);
	

				//내용 보기시에 필요한 링크 및 버튼 URL 만들기
				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();
			
				redirect = redirectBO.getLinkForView(tablename,mode,searchword,searchscope,page,no,data_id,ver_code);
				request.setAttribute("RedirectInView",redirect);

				//대출대상 문서의 대출일수 값 가져오기
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				category = masterDAO.getCategoryId(data_id);
				com.anbtech.dms.db.DmsEnvDAO dmsenvDAO = new com.anbtech.dms.db.DmsEnvDAO(con);
				com.anbtech.dms.entity.DmsEnvTable dmsenv = new com.anbtech.dms.entity.DmsEnvTable();
				dmsenv = dmsenvDAO.getDmsEnv(category);
				request.setAttribute("DmsEnv",dmsenv);

				getServletContext().getRequestDispatcher("/dms/loan_list/view.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 문서 내용 상세보기
			//
			// mode 의미
			// mode == view		: 카테고리별 문서(techdoc_data,proposal_data...) 
			// mode == view_a	: 결재 완료되어 관리자 확인 중인 문서
			// mode == view_t	: 모든 문서	
			// mode == view_m	: 내가 작성하여 상신하기 전의 문서
			// mode == report	: 전자결재 시 보여줄 문서
			// mode == print	: 인쇄화면
			///////////////////////////////////////////////////////////
			else if ("view".equals(mode) || "view_a".equals(mode) || "view_t".equals(mode) || "view_m".equals(mode) || "report".equals(mode) || "print".equals(mode)){
/*
				boolean is_commit = true;

				//문서 검색 결과 정보 상세보기시에는 엑세스 권한이 있는지 체크한다.
				if(mode.equals("view") || mode.equals("view_t")){
					com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
					is_commit = ac.getMyPrivilege(tablename,mode,login_id,data_id,ver_code);
				}

				//엑세스권한이 없는 경우 상세보기 할 수 없다는 메시지 뿌린다.
				if(!is_commit){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('죄송합니다. 이 문서는 상세 열람할 수 없습니다.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}
*/
				com.anbtech.dms.entity.MasterTable master = new com.anbtech.dms.entity.MasterTable();
				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//master_data 테이블의 레코드 내용 가져오기
				//data_id를 가지고 no를 가져온 후, no에 해당하는 레코드 정보를 가져온다.
				if(no == null) no = masterDAO.getMid(data_id);
				master = masterBO.getData("master_data",no);
				request.setAttribute("MasterInfo", master);

				//////////////////////////////////////
				// 문서 종류에 따른 데이터를 가져온다.
				/////////////////////////////////////
				if(tablename.equals("techdoc_data")){			// 기술문서
					com.anbtech.dms.entity.TechDocTable techdoc = new com.anbtech.dms.entity.TechDocTable();
					com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);

					techdoc = techdocBO.getData(mode,tablename,data_id,ver_code);
					request.setAttribute(tablename, techdoc);
				}else if(tablename.equals("proposal_data")){	// 제안서
					com.anbtech.dms.entity.ProposalTable proposal = new com.anbtech.dms.entity.ProposalTable();
					com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);

					proposal = proposalBO.getData(mode,tablename,data_id,ver_code);
					request.setAttribute(tablename, proposal);
				}

				//현재 카테고리의 환경 가져오기(대표문자,보안등급,보존기간 등)
				if(category.equals("")) category = masterDAO.getCategoryId(data_id);
				com.anbtech.dms.db.DmsEnvDAO dmsenvDAO = new com.anbtech.dms.db.DmsEnvDAO(con);
				com.anbtech.dms.entity.DmsEnvTable dmsenv = new com.anbtech.dms.entity.DmsEnvTable();
				dmsenv = dmsenvDAO.getDmsEnv(category);
				request.setAttribute("DmsEnv",dmsenv);

				//내용 상세 보기시에 필요한 링크 및 버튼 URL 만들기
				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();

				redirect = redirectBO.getLinkForView(tablename,mode,category,searchword,searchscope,page,no,data_id,ver_code,why_revision,org_category);
				request.setAttribute("RedirectInView",redirect);

				//인쇄화면 모드일 때는 결재 정보를 가져온다.
				if(mode.equals("print")){
					com.anbtech.admin.entity.ApprovalInfoTable app_table = new com.anbtech.admin.entity.ApprovalInfoTable();
					com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
					//결재 관리번호
					aid = masterDAO.getAid(tablename,data_id,ver_code);

					String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
					app_table = appDAO.getApprovalInfo("dms_approval_info",aid,sign_path);
					request.setAttribute("Approval_Info",app_table);
				}

				//문서 열람회수를 +1 증가시키기
				if(mode.equals("view") || mode.equals("view_t"))	masterDAO.updateHit("master_data",no);

				if(mode.equals("report")) getServletContext().getRequestDispatcher("/dms/"+tablename+"/report.jsp").forward(request,response);
				else if(mode.equals("print")) getServletContext().getRequestDispatcher("/dms/"+tablename+"/print.jsp").forward(request,response);
				else getServletContext().getRequestDispatcher("/dms/"+tablename+"/view.jsp").forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 문서 신규 등록, 리비젼, 수정
			///////////////////////////////////////////////////////////
			else if ("write".equals(mode) || "revision".equals(mode) || "modify".equals(mode) || "modify_a".equals(mode)){

				boolean is_commit = true;

				//리비젼 또는 수정 시에는 수행할 수 있는 권한이 있는지 체크를 한다.
				if(mode.equals("revision")){
					com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
					is_commit = ac.getMyPrivilege(tablename,mode,login_id,data_id,ver_code);
				}
				//권한이 없는 경우 메시지 뿌린다.
				String err_message = "";
				if(mode.equals("revision")) err_message = "죄송합니다. 이 문서를 리비젼할 수 있는 권한이 없습니다.";
				if(mode.equals("modify")) err_message = "죄송합니다. 이 문서를 수정할 수 있는 권한이 없습니다.";
				if(!is_commit){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('" + err_message + "');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}

				com.anbtech.dms.entity.MasterTable master = new com.anbtech.dms.entity.MasterTable();
				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//master_data 에서 가져오기
				master = masterBO.getWrite_form("master_data", no, mode, category, why_revision);
				request.setAttribute("MasterData", master);

				ArrayList file_list = new ArrayList();
				ArrayList ref_list = new ArrayList();

				//////////////////////////////////////
				// 문서 종류에 따른 데이터를 가져온다.
				/////////////////////////////////////
				if(tablename.equals("techdoc_data")){
					com.anbtech.dms.entity.TechDocTable techdoc = new com.anbtech.dms.entity.TechDocTable();
					com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);

					techdoc = techdocBO.getWrite_form(tablename,mode,data_id,ver_code,why_revision);
					request.setAttribute(tablename, techdoc);

					//modify일때만 첨부파일 리스트와 참조자료 리스트를 가져온다.
					if ("modify".equals(mode) || "modify_a".equals(mode)){
						com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
						file_list = techdocDAO.getFile_list(tablename, data_id, ver_code);
						ref_list = techdocDAO.getReference_list(tablename, data_id, ver_code);
					}
				}else if(tablename.equals("proposal_data")){
					com.anbtech.dms.entity.ProposalTable proposal = new com.anbtech.dms.entity.ProposalTable();
					com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);

					proposal = proposalBO.getWrite_form(tablename,mode,data_id,ver_code,why_revision);
					request.setAttribute(tablename, proposal);

					//modify일때만 첨부파일 리스트와 참조자료 리스트를 가져온다.
					if ("modify".equals(mode) || "modify_a".equals(mode)){
						com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);
						file_list = proposalDAO.getFile_list(tablename, data_id, ver_code);
						ref_list = proposalDAO.getReference_list(tablename, data_id, ver_code);
					}
				}

				request.setAttribute("File_List", file_list);
				request.setAttribute("Reference_List", ref_list);

				//필요한 링크 및 버튼 URL 만들기
				com.anbtech.dms.business.LinkUrlBO redirectBO = new com.anbtech.dms.business.LinkUrlBO(con);
				com.anbtech.dms.entity.LinkUrl redirect = new com.anbtech.dms.entity.LinkUrl();
				redirect = redirectBO.getLinkForWrite(tablename,mode,category,searchword,searchscope,page,no,data_id,ver_code);
				request.setAttribute("RedirectInWrite",redirect);

				//현재 카테고리의 환경 가져오기(대표문자,보안등급,보존기간 등)
				com.anbtech.dms.db.DmsEnvDAO dmsenvDAO = new com.anbtech.dms.db.DmsEnvDAO(con);
				com.anbtech.dms.entity.DmsEnvTable dmsenv = new com.anbtech.dms.entity.DmsEnvTable();
				dmsenv = dmsenvDAO.getDmsEnv(category);
				request.setAttribute("DmsEnv",dmsenv);

				getServletContext().getRequestDispatcher("/dms/"+tablename+"/write.jsp?category="+category).forward(request,response);
			}

			///////////////////////////////////////////////////////////
			// 첨부파일 다운로드 처리
			///////////////////////////////////////////////////////////
			else if ("download".equals(mode)){

				//첨부파일 엑세스 권한 체그
				com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
				boolean is_commit = ac.getMyPrivilege(tablename,mode,login_id,data_id,ver_code);

				//권한이 없는 경우 메시지 뿌린다.
				if(!is_commit){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('죄송합니다. 첨부파일을 열람할 권한이 없습니다.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}
				
				//따로 이동하는 곳 없이 다운로드시킨다.
				com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);
				com.anbtech.dms.entity.TechDocTable file = new com.anbtech.dms.entity.TechDocTable();
				file = techdocBO.getFile_fordown(tablename, t_id);
				String filename = file.getFileName();
				String filetype = file.getFileType();
				String filesize = file.getFileSize();

				//boardpath 에서 파일까지 경로 지정
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/dms/" + tablename + "/" + t_id + ".bin";
			
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

			///////////////////////////////////////////////////////////
			// 문서 대출 신청을 접수처리한다.
			///////////////////////////////////////////////////////////
			else if("req_loan".equals(mode)){
				//1.data_id를 가지고 문서번호를 가져온다.
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				String doc_no = masterDAO.getDocNo(data_id);

				//2.대출번호를 계산한다.
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				String loan_no = loanDAO.getLoanNo();

				//3.대출의뢰정보를 loan_list 테이블에 저장한다.
				loanDAO.saveData(loan_no,doc_no,data_id,ver_code,login_id,why,copy_num,return_date);

				PrintWriter out = response.getWriter();
				out.println("	<script>");
				out.println("	alert('정상적으로 대출신청되었습니다. 처리결과는 그룹웨어-전자우편으로 통보됩니다.');");
				out.println("	history.go(-1);");
				out.println("	</script>");
				out.close();
			}

			///////////////////////////////////////////////////////////
			// 대출의뢰 문서를 대출처리시킨다.
			//
			// stat == 1 : 처리중
			// stat == 2 : 반려됨
			// stat == 3 : 대출완료
			// stat == 4 : 반납완료
			///////////////////////////////////////////////////////////
			else if("loan_commit".equals(mode)){
				// 1.대출리스트상의 해당 목록의 상태코드를 3(대출완료)로 세팅
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				loanDAO.updateStat(no,"3");

				//2.대출처리 결과를 업데이트한다.
				loanDAO.updateWhy(no,why,loan_day);

				//3.해당 목록이 첨부파일이 있을 경우 첨부파일을 다운로드할 수 있는 문자열과
				//  안내메시지 문자열을 만들어 전자우편을 발송한다.
				com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);
				loanBO.sendMail(tablename,data_id,ver_code,no,login_id,"y");

				//해당 문서의 형태가 파일타입이 아닌경우에는 문서의 상태코드를 대출중으로 변경
				//사용자로 하여금 대출 중임을 알려 이중 대출신청을 막기 위해서.
				if(!masterDAO.isFileType(tablename,data_id,ver_code)){
					//4.해당 테이블의 상태코드를 대출중(6)로 세팅한다.
					masterDAO.updateStat(tablename,data_id,ver_code,"6");

					//5.현재 문서가 최종버젼의 문서라면 master_data 테이블의 상태코드도 대출중으로
					//  변경해 준다.
					com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
					if(ac.isLastVersion(tablename,data_id,ver_code))
						masterDAO.updateStat("","6",data_id);
				}

				redirectUrl = "AnBDMS?mode=loan";
			}

			///////////////////////////////////////////////////////////
			// 대출의뢰 문서를 반려시킨다.
			///////////////////////////////////////////////////////////
			else if("loan_reject".equals(mode)){
				// 1.대출리스트상의 해당 목록의 상태코드를 2(대출반려)로 세팅
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				loanDAO.updateStat(no,"2");

				//2.반려사유를 DB에 저장한다.
				loanDAO.updateWhy(no,why,loan_day);

				//3.반려사유를 메일로 통보
				com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);
				loanBO.sendMail(tablename,data_id,ver_code,no,login_id,"n");

				redirectUrl = "AnBDMS?mode=loan";
			}

			///////////////////////////////////////////////////////////
			// 대출된 문서를 반납시킨다.
			///////////////////////////////////////////////////////////
			else if("loan_return".equals(mode)){
				// 1.대출리스트상의 해당 목록의 상태코드를 2(대출반려)로 세팅
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				loanDAO.updateStat(no,"4");

				//2.처리결과 DB에 저장한다.
				loanDAO.updateWhy(no,why,loan_day);

				//3.해당 문서의 형태가 파일타입이 아닌경우에는 문서의 상태코드를 대출중에서
				//정상으로 변경
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				if(!masterDAO.isFileType(tablename,data_id,ver_code)){
					masterDAO.updateStat(tablename,data_id,ver_code,"5");

					//4.현재 문서가 최종버젼의 문서라면 master_data 테이블의 상태코드도 정상으로
					//변경해 준다.
					com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
					if(ac.isLastVersion(tablename,data_id,ver_code))
						masterDAO.updateStat("","5",data_id);
				}

				redirectUrl = "AnBDMS?mode=loan";
			}

			///////////////////////////////////////////////////////////
			// 상신전의 문서를 삭제처리한다.
			///////////////////////////////////////////////////////////
			else if("delete".equals(mode)){
				//삭제 권한 체크
				com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
				boolean is_commit = ac.getMyPrivilege(tablename,mode,login_id,data_id,ver_code);

				if(!is_commit){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('죄송합니다. 이 문서를 삭제할 수 있는 권한이 없습니다.');");
					out.println("	history.go(-1);");
					out.println("	</script>");
					out.close();
					return;								
				}
				
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
				String prev_ver_code = masterDAO.getPrevVerCode(tablename,data_id,ver_code);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				try{
					//1.첨부파일 삭제하기
					com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
					t_id = techdocDAO.getId(tablename, data_id, ver_code);
					String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/dms/"+tablename+"/";
					for(int i=1; i<10; i++){
						java.io.File f = new java.io.File(filepath + "/" + t_id + "_" + i + ".bin");
						if(f.exists()) f.delete();
					}

					//2.db 업데이트하기
					//
					// 단일 버젼인 경우
					if(prev_ver_code == null){	
						masterDAO.deleteByDataId(data_id);						// 마스터 테이블에서 해당 항목 삭제;
						masterDAO.deletByVerCode(tablename,data_id,ver_code);	// 각 테이블에서 해당 항목 삭제;
					// 리비젼이 이루어진 경우
					}else{ 
						masterDAO.deletByVerCode(tablename,data_id,ver_code);	// 각 테이블에서 해당 항목 삭제;
						//
						if(tablename.equals("techdoc_data")){
							com.anbtech.dms.entity.TechDocTable techdoc = new com.anbtech.dms.entity.TechDocTable();
							techdoc = (com.anbtech.dms.entity.TechDocTable)techdocDAO.getTechDocData(tablename,data_id,prev_ver_code);

							String writer = techdoc.getWriter();
							String writer_s = techdoc.getWriterS();
							String register = techdoc.getRegister();
							String register_s = techdoc.getRegisterS();
							String register_day = techdoc.getRegisterDay();

							//마스터 테이블의 레코드 정보를 이전으로 되돌린다.
							masterDAO.updateDataToPrev(data_id,writer,writer_s,register,register_s,register_day,prev_ver_code);
						}
					}
					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "AnBDMS?mode=processing&category=1";
			}

			///////////////////////////////////////////////////////////
			// 대출의뢰 문서 삭제하기
			///////////////////////////////////////////////////////////
			else if("loan_del".equals(mode)){
				com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
				loanDAO.deleteLoanData(no);

				redirectUrl = "AnBDMS?mode=loan";
			}

			///////////////////////////////////////////////////////////
			// 최종승인 처리를 한다.
			// 최종 승인 시에 넘어오는 파라미터는 다음과 같다.
			// mode == commit, tablename(테이블명), data_id(데이터 번호), ver_code (문서버젼),
			// aid(전자결재 관리번호)
			//
			// 처리 절차 (기술문서(techdoc_data)를 기준으로 기술):
			// (1) dbo.groupware.app_save 테이블에서 aid 에 해당하는 레코드 정보를 가져와서
			//     approval_info 테이블에 입력한다.
			// (2) 넘어온 문건이 신규 등록인지 리비젼에 의한 등록인지를 파악한다.
			//     넘어온 버젼코드가 1.0이면 신규, 아니면 리비젼으로 간주한다.
			//
			// [신규일때]
			// (3) techdoc_data 테이블의 stat 필드값을 3(관리자 확인 대기)으로 세팅
			// (4) master_data 테이블의 stat 필드값을 3(관리자 확인 대기)으로 세팅
			// (5) techdoc_data 테이블의 aid 필드값을 aid 로 세팅
			//
			// [리비젼일때]
			// (3) techdoc_data 테이블의 stat 필드값을 3(관리자 확인 대기)으로 세팅
			// (4) master_data 테이블의 해당 레코드의 last_version 을 현재 버젼으로 세팅
			// (5) techdoc_data 테이블의 aid 필드값을 aid 로 세팅
			// 
			///////////////////////////////////////////////////////////
			else if("commit".equals(mode)){
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				try{
					com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

					masterDAO.getAppInfoAndSave(aid);

					masterDAO.updateStat(tablename,data_id,ver_code,"3");
					
					if(ver_code.equals("1.0")) masterDAO.updateStat("","3",data_id);
					
					masterDAO.updateAid(tablename,data_id,ver_code,aid);
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
			// 상신처리
			// 문서의 상태코드를 결재진행중(2)로 변경한다.
			///////////////////////////////////////////////////////////
			else if("review".equals(mode)){
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				try{
					com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
					masterDAO.updateStat(tablename,data_id,ver_code,"2");
					if(ver_code.equals("1.0")) masterDAO.updateStat("","2",data_id);
					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "TechnicalServlet";
			}

			///////////////////////////////////////////////////////////
			// 반려처리
			// 문서의 상태코드를 반려(4)로 변경한다.
			///////////////////////////////////////////////////////////
			else if("reject".equals(mode)){
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				try{
					com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
					masterDAO.updateStat(tablename,data_id,ver_code,"4");
					if(ver_code.equals("1.0")) masterDAO.updateStat("","4",data_id);
					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				
				redirectUrl = "ApprovalProcessServlet?mode=REJ";
			}

			///////////////////////////////////////////////////////////
			// 문서 관리자 최종 확인 처리
			// 문서의 상태코드를 정상(5)로 변경한다.
			///////////////////////////////////////////////////////////
			else if("commit2".equals(mode)){
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				try{
					com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
					masterDAO.updateStat(tablename,data_id,ver_code,"5");
					
					if(ver_code.equals("1.0")) masterDAO.updateStat("","5",data_id);
					else masterDAO.updateVerByDataId(ver_code,data_id);

					//프로젝트관리 모듈에 문서등록여부를 알려주기 위한 정보를 보낸다.
					com.anbtech.pjt.db.pjtDocumentDAO pjtDAO = new com.anbtech.pjt.db.pjtDocumentDAO(con);
					pjtDAO.updateDocument(no);
					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);

				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "AnBDMS?mode=processing&tablename="+tablename+"&category=1";
			}

			///////////////////////////////////////////////////////////
			// 전자결재 상신 처리
			///////////////////////////////////////////////////////////
			else if("approval".equals(mode)){
				getServletContext().getRequestDispatcher("/dms/confirm.jsp?tablename="+tablename+"&t_id="+t_id+"&data_id="+data_id+"&ver="+ver_code+"&category="+category).forward(request,response);
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
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/dms/"+tablename+"/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//전페이지에서 값 받아온다. multi에서 가져옴
		String mode				= multi.getParameter("mode");			//실행모드
		String page				= multi.getParameter("page");			//현재페이지번호
		String searchword		= multi.getParameter("searchword");		//검색어
		String searchscope		= multi.getParameter("searchscope");	//검색항목
		String category_id		= multi.getParameter("category");		//카테고리 코드
		String no				= multi.getParameter("no");				//마스터 관리번호
		String data_id			= multi.getParameter("d_id");			//데이터 번호
		String doc_no			= multi.getParameter("doc_no");			//문서번호

		String where_from		= multi.getParameter("where_from");		//문서 출처
		String model_code		= multi.getParameter("model_code");		//모델 코드
		String pjt_code			= multi.getParameter("pjt_code");		//프로젝트 코드
		String node_code		= multi.getParameter("node_code");		//노드코드
		String subject			= multi.getParameter("subject");		//문서 제목
		String ver_code			= multi.getParameter("ver_code");		//문서 버젼
		String register			= multi.getParameter("register");		//등록자 사번
		String writer			= multi.getParameter("writer");			//문서작성자 사번 or 입수자 사번
		String preview			= multi.getParameter("preview");		//문서내용 요약
		String why_revision		= multi.getParameter("why_revision");	//변경내용 요약
		String modify_info		= multi.getParameter("modify_info");	//이전 수정사항
		String modify_history	= multi.getParameter("modify_history");	//수정내용 요약
		String security_level	= multi.getParameter("security_level");	//문서 보안등급
		String save_period		= multi.getParameter("save_period");	//문서 보존기간
		String written_lang		=  multi.getParameter("written_lang");	//작성언어
		String doc_type			= multi.getParameter("doc_type");		//문서유형
		String save_url			= multi.getParameter("save_url");		//문서 보관위치
		String copy_num			= multi.getParameter("copy_num");		//카피수
		String reference		= multi.getParameter("reference");		//참조자료
		String eco_no			= multi.getParameter("eco_no");			//ECO 번호

		//제안서 관련하여 추가되는 항목
		String goods_name		= multi.getParameter("goods_name");		//제안제품명
		String why_propose		= multi.getParameter("why_propose");	//제안사유
		String country_name		= multi.getParameter("country_name");	//제안국가명
		String company_name		= multi.getParameter("company_name");	//제안회사명
		String customer_name	= multi.getParameter("customer_name");	//담당자명
		String customer_tel		= multi.getParameter("customer_tel");	//담당자전화번호
		

		//공백으로 넘어오거나 null로 온 값들에 대한 처리.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category_id == null)  category_id = "";
		if (no == null) no = "";
		if (data_id == null) data_id = "";
		if (model_code == null) model_code = "";
		if (pjt_code == null) pjt_code = "";
		if (node_code == null) node_code = "";

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
		String login_name = sl.name;

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			/////////////////////
			// 문서 신규 등록 처리
			/////////////////////
			if ("write".equals(mode)){

				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//문서번호를 구한다.
				doc_no	= masterDAO.getDocNo("master_data",category_id);

				//데이터번호를 구한다.
				data_id	= System.currentTimeMillis() + "";	// 현재 시간으로 데이터 번호를 생성

				String t_id = "";

				//등록시간
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String w_time = vans.format(now);

				//검색 문자열 생성
				//검색 문자열에 포함될 항목은 가변적일 수 있다.
				//String search_keyword = subject + "|" + writer + "|" + register + "|" + preview;
				String search_keyword = "";
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

				///////////////////////////////////////////////////////////
				//if(level == 1) 
				//	conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				//else if(level == 2) 
				//	conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				//else if(level == 3) 
				//	conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				///////////////////////////////////////////////////////////

				try{

					//////////////////////////////////////
					// 문서 종류에 따른 데이터 저장
					/////////////////////////////////////
					if(tablename.equals("techdoc_data")){
						com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);
						com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
						//techdoc_table 입력
						techdocDAO.saveData(tablename,ver_code,data_id,preview,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,subject,doc_no,category_id,"1","0");

						//data_id 와 ver_code 에 해당하는 관리번호를 가져온다.
						t_id = techdocDAO.getId(tablename,data_id, ver_code);

						com.anbtech.dms.entity.TechDocTable file = new com.anbtech.dms.entity.TechDocTable();
						//첨부파일 업로더
						file = techdocBO.getFile_frommulti(multi, t_id, filepath);

						//업로딩 된 첨부파일 정보를 DB에 저장하기
						techdocBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}else if(tablename.equals("proposal_data")){
						com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);
						com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);
						//proposal_table 입력
						proposalDAO.saveData(tablename,ver_code,data_id,preview,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,subject,doc_no,category_id,"1","0",goods_name,why_propose,country_name,company_name,customer_name,customer_tel);

						//data_id 와 ver_code 에 해당하는 관리번호를 가져온다.
						t_id = proposalDAO.getId(tablename,data_id, ver_code);

						com.anbtech.dms.entity.ProposalTable file = new com.anbtech.dms.entity.ProposalTable();
						//첨부파일 업로더
						file = proposalBO.getFile_frommulti(multi, t_id, filepath);

						//업로딩 된 첨부파일 정보를 DB에 저장하기
						proposalBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}

					//각 문서별 테이블에 정상적으로 저장되었을 경우에 master_data 테이블에 저장한다.
					masterDAO.saveData("master_data", doc_no, category_id, data_id, subject, writer, register, w_time, search_keyword, ver_code, model_code,pjt_code,node_code);

					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//등록이 완료되면 전자결재를 탄다.
				getServletContext().getRequestDispatcher("/dms/confirm.jsp?tablename="+tablename+"&category="+category_id+"&t_id="+t_id+"&data_id="+data_id+"&ver="+ver_code).forward(request,response);

			///////////////////////////////////////////////////////////
			// 문서 수정 처리
			///////////////////////////////////////////////////////////
			} else if ("modify".equals(mode) || "modify_a".equals(mode)){

				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//카테고리 분류가 변경되면 문서번호도 변경된다.
				//따라서, 카테고리 코드가 변경되었는지를 우선 검사해야 한다.
				//이전 카테고리 코드 가져오기
				String old_category_id = masterDAO.getCategoryId("master_data",no);
				//이전 카테고리 코드와 현재 카테고리 코드가 같지 않으면 문서번호 새로 생성
				if(!category_id.equals(old_category_id)){
					doc_no	= masterDAO.getDocNo("master_data",category_id);
				}

				//등록시간
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String w_time = vans.format(now);

				//검색 문자열 생성
				//검색 문자열에 포함될 항목은 가변적일 수 있다.
				//String search_keyword = subject + "|" + writer + "|" + register + "|" + preview;
				String search_keyword = "";


				//수정이력 생성
				//modify_history += modify_history + "\n" + modify_info + "(수정자:" + register + ",수정일:" + w_time + ")";
				//if(mode.equals("modify_a")) modify_history = modify_info; // 관리자 수정일 때는 이력을 안 남김.

				con.setAutoCommit(false);	// 트랜잭션을 시작 
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//////////////////////////////////////
					// 문서 종류에 따른 데이터 저장
					/////////////////////////////////////
					if(tablename.equals("techdoc_data")){
						com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);
						com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);						
						//data_id 와 ver_code 에 해당하는 관리번호를 가져온다.
						String t_id = techdocDAO.getId(tablename,data_id, ver_code);

						//techdoc_table 업데이트
						techdocDAO.updateData(tablename,t_id,preview,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,modify_history);

						//첨부파일 정보 업데이트
						ArrayList file_list = techdocDAO.getFile_list(tablename,t_id);
						com.anbtech.dms.entity.TechDocTable file = new com.anbtech.dms.entity.TechDocTable();
						file = techdocBO.getFile_frommulti(multi, t_id, filepath, file_list);
						techdocBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}else if(tablename.equals("proposal_data")){
						com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);
						com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);						
						//data_id 와 ver_code 에 해당하는 관리번호를 가져온다.
						String t_id = proposalDAO.getId(tablename,data_id, ver_code);

						proposalDAO.updateData(tablename,t_id,preview,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,modify_history,goods_name,why_propose,country_name,company_name,customer_name,customer_tel);

						//첨부파일 정보 업데이트
						ArrayList file_list = proposalDAO.getFile_list(tablename,t_id);
						com.anbtech.dms.entity.ProposalTable file = new com.anbtech.dms.entity.ProposalTable();
						file = proposalBO.getFile_frommulti(multi, t_id, filepath, file_list);
						proposalBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}

					masterDAO.updateData("master_data", doc_no, category_id, subject, writer, search_keyword, no,model_code,pjt_code,node_code);
					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				//redirectUrl = "AnBDMS?mode=processing&category=1";
				if(mode.equals("modify"))
					redirectUrl = "AnBDMS?tablename="+tablename+"&mode=view_m&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category_id+"&no="+no+"&d_id="+data_id+"&ver="+ver_code;
				else if(mode.equals("modify_a"))
					redirectUrl = "AnBDMS?tablename="+tablename+"&mode=view_a&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category_id+"&no="+no+"&d_id="+data_id+"&ver="+ver_code;
			}

			///////////////////////////////////////////////////////////
			// 문서 리비젼 처리
			///////////////////////////////////////////////////////////
			else if ("revision".equals(mode)){

				com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
				com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);

				//등록시간
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String w_time = vans.format(now);

				//검색 문자열 생성
				//검색 문자열에 포함될 항목은 가변적일 수 있다.
				//String search_keyword = subject + "|" + writer + "|" + register + "|" + preview + "|" + why_revision;
				
				con.setAutoCommit(false);	// 트랜잭션을 시작  
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//////////////////////////////////////
					// 문서 종류에 따른 데이터 저장
					/////////////////////////////////////
					if(tablename.equals("techdoc_data")){
						com.anbtech.dms.business.TechDocBO techdocBO = new com.anbtech.dms.business.TechDocBO(con);
						com.anbtech.dms.db.TechDocDAO techdocDAO = new com.anbtech.dms.db.TechDocDAO(con);
						techdocDAO.saveData(tablename,ver_code,data_id,preview,why_revision,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,subject,doc_no,category_id,"1","0");

						//data_id 와 ver_code 에 해당하는 관리번호를 가져온다.
						String t_id = techdocDAO.getId(tablename,data_id, ver_code);

						com.anbtech.dms.entity.TechDocTable file = new com.anbtech.dms.entity.TechDocTable();
						//첨부파일 업로더
						file = techdocBO.getFile_frommulti(multi, t_id, filepath);

						//업로딩 된 첨부파일 정보를 DB에 저장하기
						techdocBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());

					}else if(tablename.equals("proposal_data")){
						com.anbtech.dms.business.ProposalBO proposalBO = new com.anbtech.dms.business.ProposalBO(con);
						com.anbtech.dms.db.ProposalDAO proposalDAO = new com.anbtech.dms.db.ProposalDAO(con);
						proposalDAO.saveData(tablename,ver_code,data_id,preview,why_revision,save_period,security_level,written_lang,doc_type,save_url,where_from,writer,register,w_time,reference,eco_no,copy_num,subject,doc_no,category_id,"1","0",goods_name,why_propose,country_name,company_name,customer_name,customer_tel);

						//data_id 와 ver_code 에 해당하는 관리번호를 가져온다.
						String t_id = proposalDAO.getId(tablename,data_id, ver_code);

						com.anbtech.dms.entity.ProposalTable file = new com.anbtech.dms.entity.ProposalTable();
						//첨부파일 업로더
						file = proposalBO.getFile_frommulti(multi, t_id, filepath);

						//업로딩 된 첨부파일 정보를 DB에 저장하기
						proposalBO.updFile(tablename, t_id, file.getFileName(), file.getFileType(), file.getFileSize());
					}

					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//등록이 완료되면 전자결재를 탄다.
				getServletContext().getRequestDispatcher("/dms/confirm.jsp?tablename="+tablename+"&category="+category_id+"&data_id="+data_id+"&ver="+ver_code).forward(request,response);
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