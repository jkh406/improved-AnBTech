import com.anbtech.crm.entity.*;
import com.anbtech.crm.db.*;
import com.anbtech.crm.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class CrmServlet extends HttpServlet {

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

		//기본 파라미터
		String mode				= request.getParameter("mode");
		String page				= request.getParameter("page");
		String no				= request.getParameter("no");
		String searchword		= Hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope		= request.getParameter("searchscope");
		String category			= request.getParameter("category");
		String umask			= request.getParameter("umask");
		String module			= request.getParameter("module");

		if (mode == null) mode = "list";
		if (no == null) no = "";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

		//데이터 파라미터
	
		//기본적으로 사용되는 변수 정의
		String redirectUrl = "";
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

			///////////////////////////////////////
			// 검색 조건에 맞는 고객사 목록 출력
			///////////////////////////////////////
			if(mode.equals("company_list")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				com.anbtech.crm.entity.CompanyInfoTable company = new com.anbtech.crm.entity.CompanyInfoTable();

				ArrayList company_list = new ArrayList();
				company_list = crmDAO.getCompanyList(mode,module,searchword,searchscope,category,page,login_id);
				request.setAttribute("CompanyList", company_list);

				com.anbtech.crm.business.CrmLinkUrlBO redirectBO = new com.anbtech.crm.business.CrmLinkUrlBO(con);
				com.anbtech.crm.entity.CrmLinkUrl redirect = new com.anbtech.crm.entity.CrmLinkUrl();
				redirect = redirectBO.getCompanyListLink(mode,module,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/crm/company/" + module + "/list.jsp?mode="+mode).forward(request,response);
			}

			///////////////////////////////////////
			// 선택된 고객사 정보 상세 보기
			///////////////////////////////////////
			else if(mode.equals("company_view")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				com.anbtech.crm.entity.CompanyInfoTable company = new com.anbtech.crm.entity.CompanyInfoTable();

				company = crmDAO.getCompanyInfo(no);
				request.setAttribute("CompanyInfo", company);				
				
				com.anbtech.crm.business.CrmLinkUrlBO redirectBO = new com.anbtech.crm.business.CrmLinkUrlBO(con);
				com.anbtech.crm.entity.CrmLinkUrl redirect = new com.anbtech.crm.entity.CrmLinkUrl();
				redirect = redirectBO.getCompanyViewLink(no,mode,module,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);
								
				getServletContext().getRequestDispatcher("/crm/company/" + module + "/view.jsp?mode="+mode).forward(request,response);
			}

			///////////////////////////////////////
			// 고객사 정보 신규등록 또는 수정
			///////////////////////////////////////
			else if(mode.equals("company_write") || mode.equals("company_modify")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				com.anbtech.crm.business.CrmBO crmBO = new com.anbtech.crm.business.CrmBO(con);
				com.anbtech.crm.entity.CompanyInfoTable company = new com.anbtech.crm.entity.CompanyInfoTable();

				company = crmBO.getCompanyWriteForm(mode,no,login_id);
				request.setAttribute("CompanyInfo",company);

				com.anbtech.crm.business.CrmLinkUrlBO redirectBO = new com.anbtech.crm.business.CrmLinkUrlBO(con);
				com.anbtech.crm.entity.CrmLinkUrl redirect = new com.anbtech.crm.entity.CrmLinkUrl();
				redirect = redirectBO.getCompanyWriteLink(no,mode,module,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);
								
				// 첨부화일 가져오기
				ArrayList file_list = new ArrayList();
				file_list = crmDAO.getFile_list(no);
				request.setAttribute("FILE_LIST", file_list);
								
				getServletContext().getRequestDispatcher("/crm/company/" + module + "/write.jsp?mode="+mode).forward(request,response);
			}

			///////////////////////////////////////
			// 고객사 정보 삭제 처리
			///////////////////////////////////////
			else if(mode.equals("company_delete")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				com.anbtech.crm.entity.CompanyInfoTable company = new com.anbtech.crm.entity.CompanyInfoTable();
				
				try
				{
					
					// 첨부파일 삭제하기
					company = crmDAO.getCompanyInfo(no);
					crmDAO.deleteCompanyInfo(no);
					
					String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/crm/";
					for(int i=1; i<10; i++){
						java.io.File f = new java.io.File(filepath + company.getFileUmask()+ "_" + i + ".bin");
						if(f.exists()) f.delete();
					}
					
					con.commit(); // commit한다.
				}
				catch (Exception e)	{
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				redirectUrl = "CrmServlet?mode=company_list&module="+module;
			}

			///////////////////////////////////////
			// 검색 조건에 맞는 고객 목록 출력
			///////////////////////////////////////
			else if(mode.equals("customer_list")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				com.anbtech.crm.entity.CustomerInfoTable customer = new com.anbtech.crm.entity.CustomerInfoTable();

				ArrayList customer_list = new ArrayList();
				customer_list = crmDAO.getCustomerList(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("CustomerList", customer_list);

				com.anbtech.crm.business.CrmLinkUrlBO redirectBO = new com.anbtech.crm.business.CrmLinkUrlBO(con);
				com.anbtech.crm.entity.CrmLinkUrl redirect = new com.anbtech.crm.entity.CrmLinkUrl();
				redirect = redirectBO.getCustomerListLink(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/crm/customer/list.jsp?mode="+mode).forward(request,response);
			}

			///////////////////////////////////////
			// 선택된 고객 정보 상세 보기
			///////////////////////////////////////
			else if(mode.equals("customer_view")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				com.anbtech.crm.entity.CustomerInfoTable customer = new com.anbtech.crm.entity.CustomerInfoTable();

				customer = crmDAO.getCustomerInfo(no);
				request.setAttribute("CustomerInfo", customer);				
				
				com.anbtech.crm.business.CrmLinkUrlBO redirectBO = new com.anbtech.crm.business.CrmLinkUrlBO(con);
				com.anbtech.crm.entity.CrmLinkUrl redirect = new com.anbtech.crm.entity.CrmLinkUrl();
				redirect = redirectBO.getCustomerViewLink(no,mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/crm/customer/view.jsp?mode="+mode).forward(request,response);
			}

			///////////////////////////////////////
			// 고객 정보 신규등록 또는 수정
			///////////////////////////////////////
			else if(mode.equals("customer_write") || mode.equals("customer_modify")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				com.anbtech.crm.business.CrmBO crmBO = new com.anbtech.crm.business.CrmBO(con);
				com.anbtech.crm.entity.CustomerInfoTable customer = new com.anbtech.crm.entity.CustomerInfoTable();

				customer = crmBO.getCustomerWriteForm(mode,no,login_id);
				request.setAttribute("CustomerInfo",customer);

				com.anbtech.crm.business.CrmLinkUrlBO redirectBO = new com.anbtech.crm.business.CrmLinkUrlBO(con);
				com.anbtech.crm.entity.CrmLinkUrl redirect = new com.anbtech.crm.entity.CrmLinkUrl();
				redirect = redirectBO.getCustomerWriteLink(no,mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/crm/customer/write.jsp").forward(request,response);
			}

			///////////////////////////////////////
			// 고객 정보 삭제 처리
			///////////////////////////////////////
			else if(mode.equals("customer_delete")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				crmDAO.deleteCustomerInfo(no);

				redirectUrl = "CrmServlet?mode=customer_list&module="+module;
			}

			/*******************************************************
			 * 첨부파일 다운로드 처리
			 ********************************************************/
			else if ("download".equals(mode)){
				
				//따로 이동하는 곳 없이 다운로드시킨다.
				com.anbtech.crm.business.CrmBO crmBO = new com.anbtech.crm.business.CrmBO(con);
				com.anbtech.crm.entity.CompanyInfoTable file = new com.anbtech.crm.entity.CompanyInfoTable();
				file = crmBO.getFile_fordown(no);
				String filename = file.getFileName();
				String filetype = file.getFileType();
				String filesize = file.getFileSize();

				//boardpath 에서 파일까지 경로 지정
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/crm/" + umask + ".bin";
			
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

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/crm/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//기본 파라미터
		String no					= multi.getParameter("no");
		String mode					= multi.getParameter("mode");
		String page					= multi.getParameter("page");
		String searchword			= multi.getParameter("searchword");
		String searchscope			= multi.getParameter("searchscope");
		String category				= multi.getParameter("category");
		String module				= multi.getParameter("module");

		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

		String  company_no			= multi.getParameter("company_no");
		String  passwd				= multi.getParameter("passwd");
		String  name_kor			= multi.getParameter("name_kor");
		String  name_eng			= multi.getParameter("name_eng");
		String  chief_name			= multi.getParameter("chief_name");
		String  chief_personal_no	= multi.getParameter("chief_personal_no");
		String  company_address		= multi.getParameter("company_address");
		String  company_post_no		= multi.getParameter("company_post_no");
		String  main_tel_no			= multi.getParameter("main_tel_no");
		String  main_fax_no			= multi.getParameter("main_fax_no");
		String  homepage_url		= multi.getParameter("homepage_url");
		String  business_type		= multi.getParameter("business_type");
		String  business_item		= multi.getParameter("business_item");
		String  trade_start_time	= multi.getParameter("trade_start_time");
		String  trade_end_time		= multi.getParameter("trade_end_time");
		String  company_type		= multi.getParameter("company_type");
		String  trade_type			= multi.getParameter("trade_type");
		String  credit_level		= multi.getParameter("credit_level");
		String  estimate_req_level	= multi.getParameter("estimate_req_level");
		String  worker_number		= multi.getParameter("worker_number");
		String  main_bank_name		= multi.getParameter("main_bank_name");
		String  main_newspaper_name	= multi.getParameter("main_newspaper_name");
		String  main_product_name	= multi.getParameter("main_product_name");
		String  corporation_no		= multi.getParameter("corporation_no");
		String  founding_day		= multi.getParameter("founding_day");
		String  other_info			= multi.getParameter("other_info");
		String  modify_history		= multi.getParameter("modify_history");

		if(passwd == null) passwd = company_no;
		if(name_kor	== null) name_kor = "";
		if(name_eng	== null) name_eng = "";
		if(chief_name == null) chief_name = "";
		if(chief_personal_no == null) chief_personal_no = "";
		if(company_address == null) company_address = "";
		if(company_post_no == null) company_post_no = "";
		if(main_tel_no == null) main_tel_no = "";
		if(main_fax_no == null) main_fax_no = "";
		if(homepage_url	== null) homepage_url = "";
		if(business_type == null) business_type = "기타";
		if(business_item == null) business_item = "기타";
		if(trade_start_time	== null) trade_start_time = "";
		if(trade_end_time == null) trade_end_time = "";
		if(company_type	== null) company_type = "";
		if(trade_type == null) trade_type = "";
		if(credit_level	== null) credit_level = "";
		if(estimate_req_level == null) estimate_req_level = "";
		if(worker_number == null) worker_number = "";
		if(main_bank_name == null) main_bank_name = "";
		if(main_newspaper_name	== null) main_newspaper_name = "";
		if(main_product_name == null) main_product_name = "";
		if(corporation_no == null) corporation_no = "";
		if(founding_day	== null) founding_day = "";
		if(other_info == null) other_info = "";

		String  sex					= multi.getParameter("sex");
		String  company_name		= multi.getParameter("company_name");
		String  division_name		= multi.getParameter("division_name");
		String  main_job			= multi.getParameter("main_job");
		String  position_rank		= multi.getParameter("position_rank");
		String  mobile_no			= multi.getParameter("mobile_no");
		String  company_tel_no		= multi.getParameter("company_tel_no");
		String  company_fax_no		= multi.getParameter("company_fax_no");
		String  where_to_dm			= multi.getParameter("where_to_dm");
		String  email_address		= multi.getParameter("email_address");
		String  customer_type		= multi.getParameter("customer_type");
		String  customer_class		= multi.getParameter("customer_class");
		String  home_tel_no			= multi.getParameter("home_tel_no");
		String  home_fax_no			= multi.getParameter("home_fax_no");
		String  home_address		= multi.getParameter("home_address");
		String  home_post_no		= multi.getParameter("home_post_no");
		String  personal_no			= multi.getParameter("personal_no");
		String  birthday			= multi.getParameter("birthday");
		String  whether_wedding		= multi.getParameter("whether_wedding");
		String  partner_name		= multi.getParameter("partner_name");
		String  partner_birthday	= multi.getParameter("partner_birthday");
		String  wedding_day			= multi.getParameter("wedding_day");
		String  hobby				= multi.getParameter("hobby");
		String  special_ability		= multi.getParameter("special_ability");
		String  major_field			= multi.getParameter("major_field");

		if(sex == null) sex = "";
		if(company_name	== null) company_name = "";
		if(division_name == null) division_name = "";
		if(main_job	== null) main_job = "";
		if(position_rank == null) position_rank = "";
		if(mobile_no == null) mobile_no = "";
		if(company_tel_no == null) company_tel_no = "";
		if(company_fax_no == null) company_fax_no = "";
		if(where_to_dm == null) where_to_dm = "";
		if(email_address == null) email_address = "";
		if(customer_type == null) customer_type = "";
		if(customer_class == null) customer_class = "";
		if(home_tel_no == null) home_tel_no = "";
		if(home_fax_no == null) home_fax_no = "";
		if(home_address == null) home_address = "";
		if(home_post_no	== null) home_post_no = "";
		if(personal_no == null) personal_no = "";
		if(birthday == null) birthday = "";
		if(whether_wedding == null) whether_wedding = "";
		if(partner_name == null) partner_name = "";
		if(partner_birthday == null) partner_birthday = "";
		if(wedding_day == null) wedding_day = "";
		if(hobby == null) hobby = "";
		if(special_ability == null) special_ability = "";
		if(major_field == null) major_field = "";

		//기본 변수 정의
		String redirectUrl = "";
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

			///////////////////////////////////////
			// 고객사 정보 신규등록 처리
			///////////////////////////////////////			
			if(mode.equals("company_write")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				com.anbtech.crm.business.CrmBO crmBO = new com.anbtech.crm.business.CrmBO(con);
				com.anbtech.crm.entity.CompanyInfoTable file = new com.anbtech.crm.entity.CompanyInfoTable();
				//사업자번호 중복체크
				String same_company = crmDAO.getCompanyNameByNo(company_no);
				if(!same_company.equals("")){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('입력하신 사업자번호는 이미 존재합니다. 등록작업을 계속할 수 없습니다.\\n\\n기 등록된 회사명:" + same_company + "');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}else{
					crmDAO.saveCompanyInfo(company_no,passwd,name_kor,name_eng,chief_name,chief_personal_no,company_address,company_post_no,main_tel_no,main_fax_no,homepage_url,business_type,business_item,trade_start_time,trade_end_time,company_type,trade_type,credit_level,estimate_req_level,worker_number,main_bank_name,main_newspaper_name,main_product_name,corporation_no,founding_day,other_info,login_id);
					//db 저장
					file = crmBO.getFile_frommulti(multi, company_no, filepath);
					//업로딩 된 첨부파일 정보를 DB에 저장하기
					crmBO.updFile(company_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileUmask());
					
				}

				redirectUrl = "CrmServlet?mode=company_list&module=" + module;
			}

			///////////////////////////////////////
			// 고객사 정보 수정 처리
			///////////////////////////////////////
			else if(mode.equals("company_modify")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				com.anbtech.crm.business.CrmBO crmBO = new com.anbtech.crm.business.CrmBO(con);


				crmDAO.updCompanyInfo(no,company_no,passwd,name_kor,name_eng,chief_name,chief_personal_no,company_address,company_post_no,main_tel_no,main_fax_no,homepage_url,business_type,business_item,trade_start_time,trade_end_time,company_type,trade_type,credit_level,estimate_req_level,worker_number,main_bank_name,main_newspaper_name,main_product_name,corporation_no,founding_day,other_info,login_id,modify_history);

				// 화일정보 가져오기
				ArrayList file_list = new ArrayList();
				file_list = crmDAO.getFile_list(no);

				//  첨부 화일 저장
				com.anbtech.crm.entity.CompanyInfoTable file = new com.anbtech.crm.entity.CompanyInfoTable();
				//  첨부파일 업로더
				file = crmBO.getFile_frommulti(multi, company_no, filepath, file_list);
				//  업로딩 된 첨부파일 정보를 DB에 저장하기
				crmBO.updFile(company_no, file.getFileName(), file.getFileType(), file.getFileSize(), file.getFileUmask());

				redirectUrl = "CrmServlet?mode=company_list&module="+module;
			}

			///////////////////////////////////////
			// 고객 정보 신규등록 처리
			///////////////////////////////////////			
			else if(mode.equals("customer_write")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
				crmDAO.saveCustomerInfo(name_kor,name_eng,sex,company_name,company_no,division_name,main_job,position_rank,mobile_no,company_tel_no,company_fax_no,company_address,company_post_no,where_to_dm,email_address,homepage_url,customer_type,customer_class,home_tel_no,home_fax_no,home_address,home_post_no,personal_no,birthday,whether_wedding,partner_name,partner_birthday,wedding_day,hobby,special_ability,major_field,other_info,login_id);


				redirectUrl = "CrmServlet?mode=customer_list";
			}

			///////////////////////////////////////
			// 고객 정보 수정 처리
			///////////////////////////////////////
			else if(mode.equals("customer_modify")){
				com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);

				crmDAO.updCustomerInfo(no,name_kor,name_eng,sex,company_name,company_no,division_name,main_job,position_rank,mobile_no,company_tel_no,company_fax_no,company_address,company_post_no,where_to_dm,email_address,homepage_url,customer_type,customer_class,home_tel_no,home_fax_no,home_address,home_post_no,personal_no,birthday,whether_wedding,partner_name,partner_birthday,wedding_day,hobby,special_ability,major_field,other_info,login_id,modify_history);

				redirectUrl = "CrmServlet?mode=customer_list";
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
