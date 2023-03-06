import com.anbtech.mm.db.*;
import com.anbtech.mm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class mfgOrderServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 15;
	private int max_display_page = 5;

	/********
	 * 소멸자
	 ********/
	public void close(Connection con) throws ServletException{
		connMgr.freeConnection("mssql",con);
	}

	/**********************************
	 * get방식으로 넘어왔을 때 처리 (목록보기)
	 **********************************/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//기본파라미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"order_receive":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MFG_REQ_MASTER및 MFG_REQ_ITEM구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String mfg_no = Hanguel.toHanguel(request.getParameter("mfg_no"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_no"));
		String mfg_req_no = Hanguel.toHanguel(request.getParameter("mfg_req_no"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_req_no"));
		String assy_code = Hanguel.toHanguel(request.getParameter("assy_code"))==null?"":Hanguel.toHanguel(request.getParameter("assy_code"));
		String assy_spec = Hanguel.toHanguel(request.getParameter("assy_spec"))==null?"":Hanguel.toHanguel(request.getParameter("assy_spec"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String item_name = Hanguel.toHanguel(request.getParameter("item_name"))==null?"":Hanguel.toHanguel(request.getParameter("item_name"));
		String item_spec = Hanguel.toHanguel(request.getParameter("item_spec"))==null?"":Hanguel.toHanguel(request.getParameter("item_spec"));
		String item_unit = Hanguel.toHanguel(request.getParameter("item_unit"))==null?"":Hanguel.toHanguel(request.getParameter("item_unit"));
		String item_type = Hanguel.toHanguel(request.getParameter("item_type"))==null?"":Hanguel.toHanguel(request.getParameter("item_type"));
		String ad_tag = Hanguel.toHanguel(request.getParameter("ad_tag"))==null?"":Hanguel.toHanguel(request.getParameter("ad_tag"));
		String req_type = Hanguel.toHanguel(request.getParameter("req_type"))==null?"":Hanguel.toHanguel(request.getParameter("req_type"));
		String req_status = Hanguel.toHanguel(request.getParameter("req_status"))==null?"":Hanguel.toHanguel(request.getParameter("req_status"));
		String req_count = Hanguel.toHanguel(request.getParameter("req_count"))==null?"":Hanguel.toHanguel(request.getParameter("req_count"));
		String req_date = Hanguel.toHanguel(request.getParameter("req_date"))==null?"":Hanguel.toHanguel(request.getParameter("req_date"));
		String req_div_code = Hanguel.toHanguel(request.getParameter("req_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("req_div_code"));
		String req_div_name = Hanguel.toHanguel(request.getParameter("req_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("req_div_name"));
		String req_user_id = Hanguel.toHanguel(request.getParameter("req_user_id"))==null?"":Hanguel.toHanguel(request.getParameter("req_user_id"));
		String req_user_name = Hanguel.toHanguel(request.getParameter("req_user_name"))==null?"":Hanguel.toHanguel(request.getParameter("req_user_name"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		String factory_name = Hanguel.toHanguel(request.getParameter("factory_name"))==null?"":Hanguel.toHanguel(request.getParameter("factory_name"));
		String op_no = Hanguel.toHanguel(request.getParameter("op_no"))==null?"":Hanguel.toHanguel(request.getParameter("op_no"));
		String mfg_count = Hanguel.toHanguel(request.getParameter("mfg_count"))==null?"0":Hanguel.toHanguel(request.getParameter("mfg_count"));
		
		//MFG PRODUCT 구성을 위해 파라미터 추가
		String total_count = Hanguel.toHanguel(request.getParameter("total_count"))==null?"":Hanguel.toHanguel(request.getParameter("total_count"));
		String good_count = Hanguel.toHanguel(request.getParameter("good_count"))==null?"":Hanguel.toHanguel(request.getParameter("good_count"));
		String bad_count = Hanguel.toHanguel(request.getParameter("bad_count"))==null?"":Hanguel.toHanguel(request.getParameter("bad_count"));
		String output_date = Hanguel.toHanguel(request.getParameter("output_date"))==null?"":Hanguel.toHanguel(request.getParameter("output_date"));
		String bad_type = Hanguel.toHanguel(request.getParameter("bad_type"))==null?"":Hanguel.toHanguel(request.getParameter("bad_type"));
		String bad_note = Hanguel.toHanguel(request.getParameter("bad_note"))==null?"":Hanguel.toHanguel(request.getParameter("bad_note"));

		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		//재공중인 모든자재산출및 원가산출
		String factory_code = Hanguel.toHanguel(request.getParameter("factory_code"))==null?factory_no:Hanguel.toHanguel(request.getParameter("factory_code"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?factory_no:Hanguel.toHanguel(request.getParameter("model_code"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MFG_REQ_ITEM 작업지시정보
			//------------------------------------------------------------
			//작지 수신현황보기
			if ("order_receive".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//등록된 LIST 보기
				if(sItem.length() == 0) sItem = "op_order";
				ArrayList request_list = new ArrayList();
				request_list = odrDAO.getMfgOrderList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("REQUEST_List", request_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgOperatorTable pageL = new com.anbtech.mm.entity.mfgOperatorTable();
				pageL = odrDAO.getRequestDisplayPage(login_id,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/orderReceiveList.jsp").forward(request,response);
			}
			//작업지시서 상세내용보기
			else if ("order_view".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//화면출력 데이터 읽기
				com.anbtech.mm.entity.mfgOperatorTable mfgOP = new com.anbtech.mm.entity.mfgOperatorTable();
				mfgOP = odrDAO.readMfgOperator(pid);
				request.setAttribute("MFG_operator",mfgOP); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/orderOpView.jsp").forward(request,response);
				
			}
			//작업지시서 마스터 상세내용보기
			else if ("order_mview".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//화면출력 데이터 읽기
				com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
				mfgT = odrDAO.readMfgMasterItem(gid);
				request.setAttribute("MFG_master",mfgT); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/orderMaView.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//	부품출고의뢰정보
			//------------------------------------------------------------
			//부품 출고의뢰 목록보기
			else if ("out_delivery".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//등록된 LIST 보기
				if(sItem.length() == 0) sItem = "mfg_no";
				ArrayList req_master_list = new ArrayList();
				req_master_list = odrDAO.getMfgReqMasterList(login_id,factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("REQ_MASTER_List", req_master_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgReqMasterTable pageL = new com.anbtech.mm.entity.mfgReqMasterTable();
				pageL = odrDAO.getMfgReqMasterDisplayPage(login_id,factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/itemDeliveryList.jsp").forward(request,response);
			}
			//부품 출고의뢰 상세LIST
			else if ("out_list".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//등록된 LIST 보기
				ArrayList req_item_list = new ArrayList();
				req_item_list = odrDAO.getMfgReqItemList(mfg_req_no,factory_no);
				request.setAttribute("REQ_ITEM_List", req_item_list); 

				//부품출고의뢰 마스터 정보
				com.anbtech.mm.entity.mfgReqMasterTable reqMT = new com.anbtech.mm.entity.mfgReqMasterTable();
				reqMT = odrDAO.readMfgReqMaster(mfg_req_no,factory_no);
				request.setAttribute("REQ_master",reqMT); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/itemOutList.jsp").forward(request,response);
			}
			//출고부품 대상 개별읽기 : 수정준비
			else if ("out_preview".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//부품출고의뢰 마스터 정보
				com.anbtech.mm.entity.mfgReqItemTable reqIT = new com.anbtech.mm.entity.mfgReqItemTable();
				reqIT = odrDAO.readMfgReqItemRead(pid);
				request.setAttribute("REQ_item",reqIT); 

				//부품출고의뢰 가능부품수량을 알아보기위해
				com.anbtech.mm.entity.mfgItemTable mfgIT = new com.anbtech.mm.entity.mfgItemTable();
				mfgIT = odrDAO.readMfgItem(mfg_no,assy_code,level_no,item_code,factory_no);
				request.setAttribute("MFG_item",mfgIT); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/itemOutReg.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//	실적등록 관리
			//------------------------------------------------------------
			//부품 출고의뢰 목록보기
			else if ("product_out_list".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//등록된 LIST 보기
				if(sItem.length() == 0) sItem = "mfg_no";
				ArrayList product_master_list = new ArrayList();
				product_master_list = odrDAO.getMfgProductMasterList(login_id,factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("PRODUCT_MASTER_List", product_master_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgProductMasterTable pageL = new com.anbtech.mm.entity.mfgProductMasterTable();
				pageL = odrDAO.getMfgProductMasterDisplayPage(login_id,factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/productOutList.jsp").forward(request,response);
			}
			//실적등록 LIST 보기
			else if ("product_list".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);
				com.anbtech.mm.business.mfgOrderBO odrBO = new com.anbtech.mm.business.mfgOrderBO(con);

				//실적등록 마스터 초기화 등록하기
				if(level_no.length() !=0) {
					con.setAutoCommit(false);	// 트랜잭션을 시작
					con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try{
						odrBO.saveInitProduct(login_id,login_name,mfg_no,assy_code,level_no,factory_no);
						con.commit(); // commit한다.
						con.setAutoCommit(true);
					} catch(Exception e){
						con.rollback();
						con.setAutoCommit(true);
						request.setAttribute("ERR_MSG",e.toString());
						getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
					}
				}

				//등록된 LIST 보기
				ArrayList product_item_list = new ArrayList();
				product_item_list = odrDAO.getProductItemList(mfg_no,assy_code,factory_no);
				request.setAttribute("PRODUCT_ITEM_List", product_item_list); 

				//실적등록 마스터 정보
				com.anbtech.mm.entity.mfgProductMasterTable productMT = new com.anbtech.mm.entity.mfgProductMasterTable();
				productMT = odrDAO.readMfgProductMasterRead(mfg_no,assy_code,factory_no);
				request.setAttribute("PRODUCT_master",productMT); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/productList.jsp").forward(request,response);
			
			}
			//실적등록 대상 개별읽기 : 수정준비
			else if ("product_preview".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//생산제품 실적등록 정보
				com.anbtech.mm.entity.mfgProductItemTable productIT = new com.anbtech.mm.entity.mfgProductItemTable();
				productIT = odrDAO.readMfgProductItemRead(pid);
				request.setAttribute("PRODUCT_item",productIT); 

				//실적등록 마스터 정보
				com.anbtech.mm.entity.mfgProductMasterTable productMT = new com.anbtech.mm.entity.mfgProductMasterTable();
				productMT = odrDAO.readMfgProductMasterRead(mfg_no,assy_code,factory_no);
				request.setAttribute("PRODUCT_master",productMT); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("mfg_count",mfg_count);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/productReg.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		재공자재 부품출고수량및 원가산출 정보 조회하기
			//------------------------------------------------------------
			//재공자재 부품출고수량및 원가산출 내용
			else if ("run_item".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);
				com.anbtech.mm.business.mfgOrderBO odrBO = new com.anbtech.mm.business.mfgOrderBO(con);

				//공장 조회
				request.setAttribute("Factory_List",odrDAO.getFactoryList());

				//재공중인 모델/FG List조회
				String model_list = odrDAO.getRunningModelCode(factory_code);
				request.setAttribute("MODEL_List",model_list);

				//재공중인 작업지시 List조회
				String mfg_list = odrDAO.getRunningMfgNo(factory_code);
				request.setAttribute("MFG_List",mfg_list);

				//재공중인 부품출고현황및 원가내용
				ArrayList item_list = new ArrayList();
				item_list = odrBO.getUniqueRunningItemList(model_code,mfg_list,factory_code);
				request.setAttribute("ITEM_List", item_list); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("factory_code",factory_code);
				request.setAttribute("mfg_no",mfg_no);
				request.setAttribute("model_code",model_code);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/view/runItemList.jsp").forward(request,response);
			}
			//재공자재 부품출고수량및 원가산출 내용 Excel출력
			else if ("run_item_excel".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);
				com.anbtech.mm.business.mfgOrderBO odrBO = new com.anbtech.mm.business.mfgOrderBO(con);
				
				//재공중인 부품출고현황및 원가내용
				ArrayList item_list = new ArrayList();
				item_list = odrBO.getUniqueRunningItemList(model_code,mfg_no,factory_code);
				request.setAttribute("ITEM_List", item_list); 

				//재공중인 모델/FG List조회
				String model_list = odrDAO.getRunningModelCode(factory_code);
				request.setAttribute("MODEL_List",model_list);

				//리턴하기
				request.setAttribute("factory_code",factory_code);
				request.setAttribute("mfg_no",mfg_no);
				request.setAttribute("model_code",model_code);

				//분기하기
				getServletContext().getRequestDispatcher("/mm/view/runItemExcel.jsp").forward(request,response);
			}
			
		}catch (Exception e){
			//에러출력 페이지로 분기
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
		
	} //doGet()

	/**********************************
	 * post방식으로 넘어왔을 때 처리 
	 **********************************/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		//필요한 것들 선언
		response.setContentType("text/html;charset=euc-kr");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();

		//Session
		com.anbtech.admin.SessionLib sl = (com.anbtech.admin.SessionLib)session.getAttribute(session.getId());
		if(sl == null){
			out.println("	<script>");
			out.println("	top.location.href('../admin/notice_session.jsp');");
			out.println("	</script>");
			out.close();
			return;				
		}
		String login_id = sl.id;
		String login_name = sl.name;

		//기본파라미터
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"order_receive":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MFG_REQ_ITEM구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String mfg_no = Hanguel.toHanguel(request.getParameter("mfg_no"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_no"));
		String mfg_req_no = Hanguel.toHanguel(request.getParameter("mfg_req_no"))==null?"":Hanguel.toHanguel(request.getParameter("mfg_req_no"));
		String assy_code = Hanguel.toHanguel(request.getParameter("assy_code"))==null?"":Hanguel.toHanguel(request.getParameter("assy_code"));
		String assy_spec = Hanguel.toHanguel(request.getParameter("assy_spec"))==null?"":Hanguel.toHanguel(request.getParameter("assy_spec"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		String item_name = Hanguel.toHanguel(request.getParameter("item_name"))==null?"":Hanguel.toHanguel(request.getParameter("item_name"));
		String item_spec = Hanguel.toHanguel(request.getParameter("item_spec"))==null?"":Hanguel.toHanguel(request.getParameter("item_spec"));
		String item_unit = Hanguel.toHanguel(request.getParameter("item_unit"))==null?"":Hanguel.toHanguel(request.getParameter("item_unit"));
		String item_type = Hanguel.toHanguel(request.getParameter("item_type"))==null?"":Hanguel.toHanguel(request.getParameter("item_type"));
		String ad_tag = Hanguel.toHanguel(request.getParameter("ad_tag"))==null?"":Hanguel.toHanguel(request.getParameter("ad_tag"));
		String req_type = Hanguel.toHanguel(request.getParameter("req_type"))==null?"":Hanguel.toHanguel(request.getParameter("req_type"));
		String req_status = Hanguel.toHanguel(request.getParameter("req_status"))==null?"":Hanguel.toHanguel(request.getParameter("req_status"));
		String req_count = Hanguel.toHanguel(request.getParameter("req_count"))==null?"":Hanguel.toHanguel(request.getParameter("req_count"));
		String req_date = Hanguel.toHanguel(request.getParameter("req_date"))==null?"":Hanguel.toHanguel(request.getParameter("req_date"));
		String req_div_code = Hanguel.toHanguel(request.getParameter("req_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("req_div_code"));
		String req_div_name = Hanguel.toHanguel(request.getParameter("req_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("req_div_name"));
		String req_user_id = Hanguel.toHanguel(request.getParameter("req_user_id"))==null?"":Hanguel.toHanguel(request.getParameter("req_user_id"));
		String req_user_name = Hanguel.toHanguel(request.getParameter("req_user_name"))==null?"":Hanguel.toHanguel(request.getParameter("req_user_name"));
		String factory_no = Hanguel.toHanguel(request.getParameter("factory_no"))==null?"":Hanguel.toHanguel(request.getParameter("factory_no"));
		String factory_name = Hanguel.toHanguel(request.getParameter("factory_name"))==null?"":Hanguel.toHanguel(request.getParameter("factory_name"));
		String op_no = Hanguel.toHanguel(request.getParameter("op_no"))==null?"":Hanguel.toHanguel(request.getParameter("op_no"));
		
		//MFG PRODUCT 구성을 위해 파라미터 추가
		String total_count = Hanguel.toHanguel(request.getParameter("total_count"))==null?"":Hanguel.toHanguel(request.getParameter("total_count"));
		String good_count = Hanguel.toHanguel(request.getParameter("good_count"))==null?"":Hanguel.toHanguel(request.getParameter("good_count"));
		String bad_count = Hanguel.toHanguel(request.getParameter("bad_count"))==null?"":Hanguel.toHanguel(request.getParameter("bad_count"));
		String output_date = Hanguel.toHanguel(request.getParameter("output_date"))==null?"":Hanguel.toHanguel(request.getParameter("output_date"));
		String bad_type = Hanguel.toHanguel(request.getParameter("bad_type"))==null?"":Hanguel.toHanguel(request.getParameter("bad_type"));
		String bad_note = Hanguel.toHanguel(request.getParameter("bad_note"))==null?"":Hanguel.toHanguel(request.getParameter("bad_note"));

		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		//재공중인 모든자재산출및 원가산출
		String factory_code = Hanguel.toHanguel(request.getParameter("factory_code"))==null?factory_no:Hanguel.toHanguel(request.getParameter("factory_code"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?factory_no:Hanguel.toHanguel(request.getParameter("model_code"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MFG_REQ_ITEM 작업지시정보
			//------------------------------------------------------------
			//작지 수신현황보기
			if ("order_receive".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//등록된 LIST 보기
				ArrayList request_list = new ArrayList();
				request_list = odrDAO.getMfgOrderList(login_id,sItem,sWord,page,max_display_cnt);
				request.setAttribute("REQUEST_List", request_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgOperatorTable pageL = new com.anbtech.mm.entity.mfgOperatorTable();
				pageL = odrDAO.getRequestDisplayPage(login_id,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/orderReceiveList.jsp").forward(request,response);
			}
			//작업지시서 상세내용보기
			else if ("order_view".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//화면출력 데이터 읽기
				com.anbtech.mm.entity.mfgOperatorTable mfgOP = new com.anbtech.mm.entity.mfgOperatorTable();
				mfgOP = odrDAO.readMfgOperator(pid);
				request.setAttribute("MFG_operator",mfgOP); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/orderOpView.jsp").forward(request,response);
				
			}
			//작업지시서 마스터 상세내용보기
			else if ("order_mview".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//화면출력 데이터 읽기
				com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
				mfgT = odrDAO.readMfgMasterItem(gid);
				request.setAttribute("MFG_master",mfgT); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/orderMaView.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//	부품출고의뢰정보
			//------------------------------------------------------------
			//부품 출고의뢰 목록보기
			else if ("out_delivery".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//등록된 LIST 보기
				ArrayList req_master_list = new ArrayList();
				req_master_list = odrDAO.getMfgReqMasterList(login_id,factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("REQ_MASTER_List", req_master_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgReqMasterTable pageL = new com.anbtech.mm.entity.mfgReqMasterTable();
				pageL = odrDAO.getMfgReqMasterDisplayPage(login_id,factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/itemDeliveryList.jsp").forward(request,response);
			}
			//부품출고의뢰 초기화 등록진행하기
			else if("out_create".equals(mode)) {
				com.anbtech.mm.business.mfgOrderBO odrBO = new com.anbtech.mm.business.mfgOrderBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					msg = odrBO.saveInitDelivery(login_id,login_name,gid,mfg_no,assy_code,level_no,assy_spec,factory_no,factory_name);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//리턴하기
					request.setAttribute("factory_no",factory_no);
					request.setAttribute("mfg_req_no",odrBO.getDeliveryNo());
					request.setAttribute("msg",msg); 
					
					//분기하기
					getServletContext().getRequestDispatcher("/mm/order/itemOutFrame.jsp").forward(request,response);
			
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//부품 출고의뢰 상세LIST
			else if ("out_list".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//등록된 LIST 보기
				ArrayList req_item_list = new ArrayList();
				req_item_list = odrDAO.getMfgReqItemList(mfg_req_no,factory_no);
				request.setAttribute("REQ_ITEM_List", req_item_list); 

				//부품출고의뢰 마스터 정보
				com.anbtech.mm.entity.mfgReqMasterTable reqMT = new com.anbtech.mm.entity.mfgReqMasterTable();
				reqMT = odrDAO.readMfgReqMaster(mfg_req_no,factory_no);
				request.setAttribute("REQ_master",reqMT); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/itemOutList.jsp").forward(request,response);
			}
			//출고부품 대상 개별읽기 : 수정준비
			else if ("out_preview".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//부품출고의뢰 마스터 정보
				com.anbtech.mm.entity.mfgReqItemTable reqIT = new com.anbtech.mm.entity.mfgReqItemTable();
				reqIT = odrDAO.readMfgReqItemRead(pid);
				request.setAttribute("REQ_item",reqIT); 

				//부품출고의뢰 가능부품수량을 알아보기위해
				com.anbtech.mm.entity.mfgItemTable mfgIT = new com.anbtech.mm.entity.mfgItemTable();
				mfgIT = odrDAO.readMfgItem(mfg_no,assy_code,level_no,item_code,factory_no);
				request.setAttribute("MFG_item",mfgIT); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/itemOutReg.jsp").forward(request,response);
			}
			//부품출고 수량 수정하기
			else if("out_modify".equals(mode)) {
				com.anbtech.mm.business.mfgOrderBO odrBO = new com.anbtech.mm.business.mfgOrderBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					msg = odrBO.saveDeliveryItem(pid,req_count);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기할 파라미터 만들기
					String para = "msg="+msg+"&factory_no="+factory_no+"&mfg_req_no="+mfg_req_no;
					
					//분기하기
					out.println("	<script>");
					out.println("	parent.list.location.href('mfgOrderServlet?mode=out_list&"+para+"');");
					out.println("	</script>");

					out.println("	<script>");
					out.println("	parent.reg.location.href('mfgOrderServlet?mode=out_preview&"+para+"');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//부품출고 요청하기
			else if("out_confirm".equals(mode)) {
				com.anbtech.mm.business.mfgOrderBO odrBO = new com.anbtech.mm.business.mfgOrderBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					msg = odrBO.confirmDelivery(mfg_no,mfg_req_no,assy_code,level_no,factory_no);
					
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기할 파라미터 만들기
					String para = "msg="+msg+"&factory_no="+factory_no+"&mfg_req_no="+mfg_req_no;
					
					//분기하기
					out.println("	<script>");
					out.println("	parent.list.location.href('mfgOrderServlet?mode=out_list&"+para+"');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//------------------------------------------------------------
			//	실적등록 관리
			//------------------------------------------------------------
			//부품 출고의뢰 목록보기
			else if ("product_out_list".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//등록된 LIST 보기
				ArrayList product_master_list = new ArrayList();
				product_master_list = odrDAO.getMfgProductMasterList(login_id,factory_no,sItem,sWord,page,max_display_cnt);
				request.setAttribute("PRODUCT_MASTER_List", product_master_list); 

				//페이지로 바로가기 List
				com.anbtech.mm.entity.mfgProductMasterTable pageL = new com.anbtech.mm.entity.mfgProductMasterTable();
				pageL = odrDAO.getMfgProductMasterDisplayPage(login_id,factory_no,sItem,sWord,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord); 

				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/productOutList.jsp").forward(request,response);
			}
			//실적등록 LIST 보기
			else if ("product_list".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//등록된 LIST 보기
				ArrayList product_item_list = new ArrayList();
				product_item_list = odrDAO.getProductItemList(mfg_no,assy_code,factory_no);
				request.setAttribute("PRODUCT_ITEM_List", product_item_list); 

				//실적등록 마스터 정보
				com.anbtech.mm.entity.mfgProductMasterTable productMT = new com.anbtech.mm.entity.mfgProductMasterTable();
				productMT = odrDAO.readMfgProductMasterRead(mfg_no,assy_code,factory_no);
				request.setAttribute("PRODUCT_master",productMT); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("msg",msg);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/productList.jsp").forward(request,response);
			
			}
			//실적등록 대상 개별읽기 : 수정준비
			else if ("product_preview".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);

				//생산제품 실적등록 정보
				com.anbtech.mm.entity.mfgProductItemTable productIT = new com.anbtech.mm.entity.mfgProductItemTable();
				productIT = odrDAO.readMfgProductItemRead(pid);
				request.setAttribute("PRODUCT_item",productIT); 

				//실적등록 마스터 정보
				com.anbtech.mm.entity.mfgProductMasterTable productMT = new com.anbtech.mm.entity.mfgProductMasterTable();
				productMT = odrDAO.readMfgProductMasterRead(mfg_no,assy_code,factory_no);
				request.setAttribute("PRODUCT_master",productMT); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/order/productReg.jsp").forward(request,response);
			}
			//생산실적 등록/수정/마감 하기
			else if("product_save".equals(mode) || "product_modify".equals(mode) || "product_confirm".equals(mode)) {
				com.anbtech.mm.business.mfgOrderBO odrBO = new com.anbtech.mm.business.mfgOrderBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					if("product_save".equals(mode)) {
						msg = odrBO.saveProductItem(login_id,login_name,gid,mfg_no,item_code,item_name,item_spec,total_count,good_count,bad_count,bad_type,bad_note,factory_no);
					} else if("product_modify".equals(mode)) {
						msg = odrBO.updateProductItem(gid,pid,mfg_no,item_code,total_count,good_count,bad_count,bad_type,bad_note,factory_no);
					} else if("product_confirm".equals(mode)) {
						msg = odrBO.closeProductItem(login_id,login_name,mfg_no,item_code,factory_no);
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//분기할 파라미터 만들기
					String para = "msg="+msg+"&factory_no="+factory_no+"&mfg_no="+mfg_no+"&assy_code="+item_code;
					
					//분기하기
					out.println("	<script>");
					out.println("	parent.list.location.href('mfgOrderServlet?mode=product_list&"+para+"');");
					out.println("	</script>");

					out.println("	<script>");
					out.println("	parent.reg.location.href('mfgOrderServlet?mode=product_preview&"+para+"');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
			
				}
			}
			//------------------------------------------------------------
			//		재공자재 부품출고수량및 원가산출 정보 조회하기
			//------------------------------------------------------------
			//재공자재 부품출고수량및 원가산출시 모델선택시 해당 mfg no 출력하기
			else if ("run_item_search".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);
				com.anbtech.mm.business.mfgOrderBO odrBO = new com.anbtech.mm.business.mfgOrderBO(con);

				//공장 조회
				request.setAttribute("Factory_List",odrDAO.getFactoryList());

				//재공중인 모델/FG List조회
				String model_list = odrDAO.getRunningModelCode(factory_code);
				request.setAttribute("MODEL_List",model_list);

				//재공중인 작업지시 List조회
				String mfg_list = odrDAO.getRunningMfgNo(model_code,factory_code);
				request.setAttribute("MFG_List",mfg_list);
				mfg_no = mfg_list;

				//재공중인 부품출고현황및 원가내용
				ArrayList item_list = new ArrayList();
				item_list = odrBO.getUniqueRunningItemList(model_code,mfg_no,factory_code);
				request.setAttribute("ITEM_List", item_list); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("factory_code",factory_code);
				request.setAttribute("mfg_no",mfg_no);
				request.setAttribute("model_code",model_code);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/view/runItemList.jsp").forward(request,response);
			}
			//재공자재 부품출고수량및 원가산출 내용
			else if ("run_item".equals(mode)){
				com.anbtech.mm.db.mfgOrderDAO odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);
				com.anbtech.mm.business.mfgOrderBO odrBO = new com.anbtech.mm.business.mfgOrderBO(con);

				//공장 조회
				request.setAttribute("Factory_List",odrDAO.getFactoryList());

				//재공중인 모델/FG List조회
				String model_list = odrDAO.getRunningModelCode(factory_code);
				request.setAttribute("MODEL_List",model_list);

				//재공중인 작업지시 List조회
				String mfg_list = odrDAO.getRunningMfgNo(model_code,factory_code);
				request.setAttribute("MFG_List",mfg_list);
				if(mfg_no.length() == 0) mfg_no = mfg_list;

				//재공중인 부품출고현황및 원가내용
				ArrayList item_list = new ArrayList();
				item_list = odrBO.getUniqueRunningItemList(model_code,mfg_no,factory_code);
				request.setAttribute("ITEM_List", item_list); 

				//리턴하기
				request.setAttribute("factory_no",factory_no);
				request.setAttribute("factory_code",factory_code);
				request.setAttribute("mfg_no",mfg_no);
				request.setAttribute("model_code",model_code);
				
				//분기하기
				getServletContext().getRequestDispatcher("/mm/view/runItemList.jsp").forward(request,response);
			}
			
			

		}catch (Exception e){
			//에러출력 페이지로 분기
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
			out.close();
		}
	} //doPost()
}

