import com.anbtech.em.entity.*;
import com.anbtech.em.db.*;
import com.anbtech.em.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class EstimateMgrServlet extends HttpServlet {

	private DBConnectionManager connMgr;
	private Connection con;

	/***********
	 * 소멸자
	 ***********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/********************************
	 * get방식으로 넘어왔을 때 처리
	 ********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		//업로드경로
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/em/";

		//기본 파라미터
		String mode				= request.getParameter("mode");
		String tablename		= request.getParameter("tablename");
		String page				= request.getParameter("page");
		String no				= request.getParameter("no");
		String searchword		= Hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope		= request.getParameter("searchscope");
		String category			= request.getParameter("category");
		String umask			= request.getParameter("umask");

		if (mode == null) mode = "list";
		if (no == null) no = "";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

		//데이터 파라미터
		String company_name		= Hanguel.toHanguel(request.getParameter("company_name"));	// 견적요청 회사명
		String estimate_no		= request.getParameter("estimate_no");	// 견적번호
		String version			= request.getParameter("ver");			// 견적버젼
		String item_class		= request.getParameter("item_class");	// 품목구분(1:자사품목,2:외주조달품목)
		String supplyer_code	= Hanguel.toHanguel(request.getParameter("supplyer_code"));// 공급업체코드
		String supplyer_name	= Hanguel.toHanguel(request.getParameter("supplyer_name"));// 공급업체명

		String item_name		= Hanguel.toHanguel(request.getParameter("item_name"));	// 품목명
		String buying_cost		= request.getParameter("buying_cost");	// 구입(개발)원가
		String gains_percent	= request.getParameter("gains_percent");// 이익율
		String gains_value		= request.getParameter("gains_value");	// 이익금액
		String supply_cost		= request.getParameter("supply_cost");	// 공급단가
		String tax_percent		= request.getParameter("tax_percent");	// 세율
		String tax_value		= request.getParameter("tax_value");	// 세액
		String discount_percent = request.getParameter("discount_percent");// 할인율
		String discount_value	= request.getParameter("discount_value");// 할인금액
		String quantity			= request.getParameter("quantity");		// 수량
		String unit				= request.getParameter("unit");			// 단위
		String estimate_value	= request.getParameter("estimate_value");// 견적가
		String standards		= Hanguel.toHanguel(request.getParameter("standards"));	// 규격
		String model_name		= Hanguel.toHanguel(request.getParameter("model_name"));// 모델명
		String model_code		= Hanguel.toHanguel(request.getParameter("model_code"));// 모델코드

		String special_change	= request.getParameter("special_change");// 특별할인금액
		String cut_unit			= request.getParameter("cut_unit");		 // 절삭 단위

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

			////////////////////////////////////////////////////////////
			// 외부구입조달 품목 리스트 출력 및
			// 외부구입조달 품목 찾기(견적품목 추가시에 열리는 팝업창)
			////////////////////////////////////////////////////////////
			if(mode.equals("list_out_item") || mode.equals("search_out_item")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);

				ArrayList item_list = new ArrayList();
				item_list = itemDAO.getOutItemList(mode,searchword,searchscope,category,page);
				request.setAttribute("Item_List", item_list);

				com.anbtech.em.business.EmLinkUrlBO redirectBO = new com.anbtech.em.business.EmLinkUrlBO(con);
				com.anbtech.em.entity.EmLinkUrl redirect = new com.anbtech.em.entity.EmLinkUrl();
				redirect = redirectBO.getOutItemListLink(mode,searchword,searchscope,category,page);
				request.setAttribute("OutItemRedirect",redirect);

				String url = "/em/out_item/list_out_item.jsp";
				if(mode.equals("search_out_item")) url = "/em/out_item/search_out_item.jsp";

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			/////////////////////////////////
			// 외부구입조달 품목 등록 및 수정 폼
			/////////////////////////////////
			else if(mode.equals("add_out_item")){
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				item = itemBO.getWriteOutItemForm(mode,model_code,supplyer_code,category);
				request.setAttribute("ItemInfo", item);
				
				getServletContext().getRequestDispatcher("/em/out_item/add_out_item.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////
			// 외부구입조달 품목의 공급정보 상세보기 및
			// 품목 공급단가정보 업데이트 폼
			/////////////////////////////////
			else if(mode.equals("view_out_item_supply_info") || mode.equals("modify_out_item_supply_info")){
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				//품목정보 및 공급정보 가져오기
				item = itemDAO.getOutItemInfo(model_code,supplyer_code);
				request.setAttribute("ItemInfo", item);

				//공급정보 변경이력 가져오기
				ArrayList list_supply_info = new ArrayList();
				list_supply_info = itemDAO.getListSupplyInfo(model_code,supplyer_code);
				request.setAttribute("ListSupplyInfo", list_supply_info);

				String url = "/em/out_item/view_item_supply_info.jsp?mode="+mode;
				if(mode.equals("modify_out_item_supply_info")) url = "/em/out_item/modify_item_supply_info.jsp?mode="+mode;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}
			
			////////////////////////////////////////////////
			// 첨부 견적서 파일 다운로드
			////////////////////////////////////////////////
			else if ("download".equals(mode)){
				//따로 이동하는 곳 없이 다운로드시킨다.
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.entity.ItemInfoTable file = new com.anbtech.em.entity.ItemInfoTable();
				file = itemBO.getFile_fordown(tablename, no);
				String filename = file.getFileName();
				String filetype = file.getFileType();
				String filesize = file.getFileSize();

				//boardpath 에서 파일까지 경로 지정
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/em/" + no + ".bin";
			
				if (filetype.indexOf("mage")<=0)
					filetype = "application/unknown";
				
				String strClient=request.getHeader("User-Agent");

				filename = new String(filename.getBytes("euc-kr"),"8859_1"); 

				if(strClient.indexOf("MSIE 5.5")>-1) response.setHeader("Content-Disposition","filename="+filename);
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

			/////////////////////////////////
			// 외부구입조달 품목의 공급정보 삭제처리
			/////////////////////////////////
			else if(mode.equals("delete_out_item_supply_info")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				itemDAO.deleteOutItemSupplyInfo(no,filepath);
					
				redirectUrl = "EstimateMgrServlet?mode=view_out_item_supply_info&model_code="+model_code+"&supplyer_code="+supplyer_code;
			}

			/////////////////////////////////
			// 외부구입조달 품목정보 상세보기
			// 외부구입조달 품목정보 수정 폼
			// 외부구입조달 품목의 공급처 추가 폼
			/////////////////////////////////
			else if(mode.equals("view_out_item") || mode.equals("modify_out_item") || mode.equals("add_out_item_supply_info")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				//품목정보 가져오기
				item = itemDAO.getOutItemInfo(model_code);
				request.setAttribute("ItemInfo", item);

				//품목공급정보 리스트 가져오기
				ArrayList list = new ArrayList();
				list = itemDAO.getOutItemSupplyList(model_code);
				request.setAttribute("SupplyerList", list);

				String url = "/em/out_item/view_out_item.jsp?mode="+mode;
				if(mode.equals("modify_out_item")) url = "/em/out_item/modify_out_item.jsp?mode="+mode;
				else if(mode.equals("add_out_item_supply_info")) url = "/em/out_item/add_out_item_supply_info.jsp?mode="+mode;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			/////////////////////////////////
			// 외부구입조달 품목정보 삭제처리
			/////////////////////////////////
			else if(mode.equals("delete_out_item")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작 
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					itemDAO.deleteAllOutItemSupplyInfo(model_code);
					itemDAO.deleteOutItemInfo(model_code);

					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "EstimateMgrServlet?mode=list_out_item";
			}

			////////////////////////////////////////////////////////////
			// 견적서 목록 출력
			////////////////////////////////////////////////////////////
			else if(mode.equals("list") || mode.equals("mylist")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);
				com.anbtech.em.entity.EstimateInfoTable estimate = new com.anbtech.em.entity.EstimateInfoTable();

				ArrayList estimate_list = new ArrayList();
				estimate_list = estimateDAO.getEstimateList(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Estimate_List", estimate_list);

				com.anbtech.em.business.EmLinkUrlBO redirectBO = new com.anbtech.em.business.EmLinkUrlBO(con);
				com.anbtech.em.entity.EmLinkUrl redirect = new com.anbtech.em.entity.EmLinkUrl();
				redirect = redirectBO.getListLink(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/em/estimate/list.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////////////
			// 선택된 견적번호 및 버젼의 견적정보 및 견적품목 리스트를 출력
			////////////////////////////////////////////////////////////
			else if(mode.equals("view") || mode.equals("view_my") || mode.equals("print") || mode.equals("ef_view")){
				com.anbtech.em.db.EstimateItemViewDAO item_viewDAO = new com.anbtech.em.db.EstimateItemViewDAO(con);
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);
				com.anbtech.em.entity.EstimateInfoTable estimate = new com.anbtech.em.entity.EstimateInfoTable();
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				//견적정보 가져오기
				estimate = estimateDAO.getEstimateInfo(estimate_no,version);
				request.setAttribute("Estimate_Info", estimate);				
				
				//링크문자열 가져오기
				com.anbtech.em.business.EmLinkUrlBO redirectBO = new com.anbtech.em.business.EmLinkUrlBO(con);
				com.anbtech.em.entity.EmLinkUrl redirect = new com.anbtech.em.entity.EmLinkUrl();
				redirect = redirectBO.getViewLink(mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);

				//견적항목 가져오기
				ArrayList item_list = new ArrayList();
				item_list = item_viewDAO.getEstimateItemList(estimate_no,version,estimate.getCutUnit());
				request.setAttribute("Item_List", item_list);

				//견적서 폼출력
				//mode == ef_view(품목상세출력) or mode == ef_view_d(하위품목생략출력)일때는 결재정보를 가져온다.
				if(mode.equals("ef_view")||mode.equals("ef_view_d")){
					com.anbtech.admin.entity.ApprovalInfoTable app_table = new com.anbtech.admin.entity.ApprovalInfoTable();
					com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

					//결재 관리번호를 가져온 후, 관리번호에 해당하는 결재 정보를 가져온다.
					String aid = estimateDAO.getAid(estimate_no,version);
					String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
					app_table = appDAO.getApprovalInfo("em_approval_info",aid,sign_path);
					request.setAttribute("Approval_Info",app_table);
				}
				
				String go_url = "";
				//보기 모드
				if(mode.equals("view")||mode.equals("view_my")) go_url = "/em/estimate/view.jsp?mode="+mode;
				//전자결재 보기 모드
				else if(mode.equals("print")) go_url = "/em/estimate/print.jsp?mode="+mode;
				//견적서 출력폼 보기 모드
				else if(mode.equals("ef_view")) go_url = "/em/estimate/ef_view.jsp?mode="+mode;
				getServletContext().getRequestDispatcher(go_url).forward(request,response);
			}

			/////////////////////////////////
			// 신규 견적서 작성 (견적 정보)
			//////////////////////////////////
			else if(mode.equals("write") || mode.equals("modify") || mode.equals("copy") || mode.equals("revision")){
				com.anbtech.em.business.EstimateBO estimateBO = new com.anbtech.em.business.EstimateBO(con);
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);
				com.anbtech.em.entity.EstimateInfoTable estimate = new com.anbtech.em.entity.EstimateInfoTable();

				//선택된 견적번호로 진행중인 견적건이 있는지 체크하여 있으면 리비젼되지 못하게 한다.
				if(mode.equals("revision")){
					int ing_count = estimateDAO.getRevisioningCount(estimate_no);
					if(ing_count > 0){
						PrintWriter out = response.getWriter();
						out.println("	<script>");
						out.println("	alert('열람중인 견적건은 이미 리비젼중에 있어 리비젼할 수 없습니다.');");
						out.println("	history.go(-1);");
						out.println("	</script>");
						out.close();					
					}
				}

				// 견적정보 가져오기
				estimate = estimateBO.getWriteForm(mode,estimate_no,version,login_id);
				request.setAttribute("EstimateInfo",estimate);

				com.anbtech.em.business.EmLinkUrlBO linkBO = new com.anbtech.em.business.EmLinkUrlBO(con);
				com.anbtech.em.entity.EmLinkUrl link = new com.anbtech.em.entity.EmLinkUrl();
				link = linkBO.getWriteLink(mode,estimate.getVersion());
				request.setAttribute("LinkUrl",link);

				getServletContext().getRequestDispatcher("/em/estimate/write.jsp").forward(request,response);
			}

			////////////////////////////////////////////////////////////
			// 견적품목 추가 폼 
			////////////////////////////////////////////////////////////
			else if(mode.equals("write_item")){
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);
				com.anbtech.em.business.EstimateBO estimateBO = new com.anbtech.em.business.EstimateBO(con);
				com.anbtech.em.db.EstimateItemViewDAO item_viewDAO = new com.anbtech.em.db.EstimateItemViewDAO(con);
				com.anbtech.em.entity.EstimateInfoTable estimate = new com.anbtech.em.entity.EstimateInfoTable();

				//견적정보 가져오기
				estimate = estimateBO.getWriteForm(mode,estimate_no,version,login_id);
				request.setAttribute("EstimateInfo",estimate);

				//품목정보 가져오기
				ArrayList item_list = new ArrayList();
				item_list = item_viewDAO.getEstimateItemList(estimate_no,version,estimate.getCutUnit());
				request.setAttribute("Item_List", item_list);

				getServletContext().getRequestDispatcher("/em/estimate/write_item.jsp").forward(request,response);
			}

			/////////////////////////////////
			// 특별할인 및 금액절사 처리
			/////////////////////////////////
			else if(mode.equals("apply_special")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.updateSpecialChange(estimate_no,version,special_change,cut_unit);
				
				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;
			}

			/////////////////////////////////
			// 견적품목 추가 및 수정 폼 가져오기
			/////////////////////////////////
			else if(mode.equals("add_estimate_item") || mode.equals("modify_estimate_item")){
				com.anbtech.em.business.EstimateBO estimateBO = new com.anbtech.em.business.EstimateBO(con);
				com.anbtech.em.entity.ItemInfoTable item = new com.anbtech.em.entity.ItemInfoTable();

				item = estimateBO.getAddEstimateItemForm(mode,estimate_no,version,item_class,no);
				request.setAttribute("ItemInfo", item);
				
				getServletContext().getRequestDispatcher("/em/estimate/add_item.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////
			// 견적품목 삭제 처리
			/////////////////////////////////
			else if(mode.equals("delete_estimate_item")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.deleteEstimateItemInfo(no);
					
				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;
			}

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	}

	/************************************
	 * post방식으로 넘어왔을 때 처리
	 ************************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);

		String upload_size = request.getParameter("upload_size");
		if(upload_size == null) upload_size = "50";

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/em/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//기본 파라미터
		String no				= multi.getParameter("no");
		String mode				= multi.getParameter("mode");
		String page				= multi.getParameter("page");
		String searchword		= multi.getParameter("searchword");
		String searchscope		= multi.getParameter("searchscope");
		String category			= multi.getParameter("category");

		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

		//견적정보관련 파라미터
		String estimate_no		= multi.getParameter("estimate_no");	//견적번호
		String version			= multi.getParameter("ver");			//견적버젼
		String company_name		= multi.getParameter("company_name");	//회사명
		String estimate_subj	= multi.getParameter("estimate_subj");	//견적제목
		String charge_name		= multi.getParameter("charge_name");	//담당자 이름
		String charge_rank		= multi.getParameter("charge_rank");	//담당자 직급
		String charge_div		= multi.getParameter("charge_div");		//담당자 부서명
		String charge_mobile	= multi.getParameter("charge_mobile");	//담당자 휴대전화번호
		String charge_tel		= multi.getParameter("charge_tel");		//담당자 전화번호
		String charge_fax		= multi.getParameter("charge_fax");		//담당자 팩스
		String charge_email		= multi.getParameter("charge_email");	//담당자 이메일
		String delivery_place	= multi.getParameter("delivery_place");	//인도장소
		String payment_terms	= multi.getParameter("payment_terms");	//지불조건
		String guarantee_term	= multi.getParameter("guarantee_term");	//보증기간
		String delivery_period	= multi.getParameter("delivery_period");//납품기간
		String valid_period		= multi.getParameter("valid_period");	//유효기간
		String delivery_day		= multi.getParameter("delivery_day");	//납품일자

		//견적품목 관련 파라미터
		String item_name		= multi.getParameter("item_name");		//품목명
		String maker_name		= multi.getParameter("maker_name");		//제조회사명
		String model_name		= multi.getParameter("model_name");		//모델명
		String model_code		= multi.getParameter("model_code");		//모델코드
		String unit				= multi.getParameter("unit");			//단위
		String standards		= multi.getParameter("standards");		//규격
		String item_class		= multi.getParameter("item_class");		//품목구분(자체개발 or 외주조달 or 패키지)
		String gains_percent	= multi.getParameter("gains_percent");	// 이익율
		String gains_value		= multi.getParameter("gains_value");	// 이익금액
		String tax_value		= multi.getParameter("tax_value");		// 세액
		String discount_value	= multi.getParameter("discount_value");	// 할인금액
		String quantity			= multi.getParameter("quantity");		// 수량
		String estimate_value	= multi.getParameter("estimate_value");	// 견적가

		//품목 공급 정보 관련 파라미터
		String supplyer_code	= multi.getParameter("supplyer_code");	//공급업체코드
		String supplyer_name	= multi.getParameter("supplyer_name");	//공급업체명
		String buying_cost		= multi.getParameter("buying_cost");	//구입원가
		String tax_percent		= multi.getParameter("tax_percent");	//세율
		String discount_percent	= multi.getParameter("discount_percent");	//할인율
		String supply_cost		= multi.getParameter("supply_cost");	//공급단가
		String other_info		= multi.getParameter("other_info");		//기타정보

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
			
			////////////////////
			// 외부조달품목 등록처리
			////////////////////
			if(mode.equals("add_out_item")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.entity.ItemInfoTable file = new com.anbtech.em.entity.ItemInfoTable();
				
				con.setAutoCommit(false);	// 트랜잭션을 시작 
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//품목정보 저장
					itemDAO.saveOutItemInfo(item_name,unit,model_name,model_code,standards,maker_name,login_id);

					//공급정보 저장
					String mid = System.currentTimeMillis() + ""; //관리번호생성
					itemDAO.addSupplyInfo(mid,model_code,supplyer_code,supplyer_name,supply_cost,other_info,login_id);

					//첨부파일 처리
					file = itemBO.getFile_frommulti(multi,mid,filepath);
					itemBO.updFile(mid,file.getFileName(),file.getFileType(),file.getFileSize());

					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "EstimateMgrServlet?mode=list_out_item";
			}

			////////////////////
			// 외부조달품목의 공급단가정보 업데이트처리 및
			// 외부조달품목의 공급단가정보 추가처리
			////////////////////
			else if(mode.equals("modify_out_item_supply_info") || mode.equals("add_out_item_supply_info")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				com.anbtech.em.business.EstimateItemBO itemBO = new com.anbtech.em.business.EstimateItemBO(con);
				com.anbtech.em.entity.ItemInfoTable file = new com.anbtech.em.entity.ItemInfoTable();

				con.setAutoCommit(false);	// 트랜잭션을 시작 
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//공급단가정보 저장
					String mid = System.currentTimeMillis() + ""; //관리번호생성
					itemDAO.addSupplyInfo(mid,model_code,supplyer_code,supplyer_name,supply_cost,other_info,login_id);

					//첨부파일 처리
					file = itemBO.getFile_frommulti(multi,mid,filepath);
					itemBO.updFile(mid,file.getFileName(),file.getFileType(),file.getFileSize());

					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				if(mode.equals("modify_out_item_supply_info")) redirectUrl = "EstimateMgrServlet?mode=view_out_item_supply_info&model_code="+model_code+"&supplyer_code="+supplyer_code;
				else redirectUrl = "EstimateMgrServlet?mode=view_out_item&model_code="+model_code;
			}

			////////////////////
			// 외부조달품목정보 수정처리
			////////////////////
			else if(mode.equals("modify_out_item")){
				com.anbtech.em.db.EstimateItemDAO itemDAO = new com.anbtech.em.db.EstimateItemDAO(con);
				
				//정보수정
				itemDAO.modifyOutItemInfo(item_name,unit,model_code,model_name,standards,maker_name,login_id);

				redirectUrl = "EstimateMgrServlet?mode=view_out_item&model_code="+model_code;
			}

			//////////////////////
			 // 견적정보 등록처리
			 /////////////////////
			else if(mode.equals("write")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.saveEstimateInfo(company_name,estimate_subj,estimate_no,version,charge_name,charge_rank,charge_div,charge_tel,charge_mobile,charge_fax,charge_email,delivery_place,payment_terms,guarantee_term,delivery_period,valid_period,delivery_day,login_id);

				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;
			}

			///////////////////////////
			 // 견적 복사,수정,리비젼 처리
			 //////////////////////////
			else if(mode.equals("modify") || mode.equals("copy") || mode.equals("revision")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.updEstimateInfo(company_name,estimate_subj,estimate_no,version,charge_name,charge_rank,charge_div,charge_tel,charge_mobile,charge_fax,charge_email,delivery_place,payment_terms,guarantee_term,delivery_period,valid_period,delivery_day,login_id);

				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;
			}

			/////////////////////////////////
			// 견적품목 추가처리
			/////////////////////////////////
			else if(mode.equals("add_estimate_item")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.addEstimateItem(estimate_no,version,item_class,item_name,model_code,model_name,quantity,unit,standards,maker_name,supplyer_code,supplyer_name,buying_cost,gains_percent,gains_value,supply_cost,discount_percent,discount_value,tax_percent,tax_value,estimate_value);
				
				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;

				PrintWriter out = response.getWriter();
				out.println("	<script>");
				out.println("	alert('정상적으로 입력되었습니다.');");
				out.println("	opener.location.href('" + redirectUrl + "');");
				out.println("	self.close();");
				out.println("	</script>");
				out.close();
			}

			/////////////////////////////////
			// 견적품목 수정처리
			/////////////////////////////////
			else if(mode.equals("modify_estimate_item")){
				com.anbtech.em.db.EstimateDAO estimateDAO = new com.anbtech.em.db.EstimateDAO(con);

				estimateDAO.modifyEstimateItem(no,item_class,item_name,model_code,model_name,quantity,unit,standards,maker_name,supplyer_code,supplyer_name,buying_cost,gains_percent,gains_value,supply_cost,discount_percent,discount_value,tax_percent,tax_value,estimate_value);
					
				redirectUrl = "EstimateMgrServlet?mode=write_item&estimate_no="+estimate_no+"&ver="+version;

				PrintWriter out = response.getWriter();
				out.println("	<script>");
				out.println("	alert('정상적으로 수정되었습니다.');");
				out.println("	opener.location.href('" + redirectUrl + "');");
				out.println("	self.close();");
				out.println("	</script>");
				out.close();
			}

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	}
}
