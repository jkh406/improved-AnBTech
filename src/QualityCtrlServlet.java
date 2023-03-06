import com.anbtech.qc.entity.*;
import com.anbtech.qc.db.*;
import com.anbtech.qc.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class QualityCtrlServlet extends HttpServlet {

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

		if (mode == null) mode = "list";
		if (no == null) no = "";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

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
	
		//데이터 파라미터
		String request_no		= request.getParameter("request_no");
		String item_code		= request.getParameter("item_code");
		String inspection_code	= request.getParameter("inspection_code");
		String sampled_quantity	= request.getParameter("sampled_quantity");
		String bad_quantity		= request.getParameter("bad_quantity");
		String serial_no		= request.getParameter("serial_no");
		String produce_year		= request.getParameter("produce_year");
		String produce_month	= request.getParameter("produce_month");
		String lot_quantity		= request.getParameter("lot_quantity");
		String serial_no_s		= request.getParameter("serial_no_s");
		String serial_no_e		= request.getParameter("serial_no_e");	

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			///////////////////////////////////////
			// 검색조건에 맞는 검사의뢰 리스트 가져오기
			///////////////////////////////////////
			if(mode.equals("list_inspect") || mode.equals("list_result") || mode.equals("list_return") || mode.equals("list_rework")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.entity.InspectionMasterTable table = new com.anbtech.qc.entity.InspectionMasterTable();

				ArrayList list = new ArrayList();
				list = qcDAO.getInspectList(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("INSPECT_LIST",list);

				com.anbtech.qc.business.QualityCtrlLinkUrlBO redirectBO = new com.anbtech.qc.business.QualityCtrlLinkUrlBO(con);
				com.anbtech.qc.entity.QualityCtrlLinkUrl redirect = new com.anbtech.qc.entity.QualityCtrlLinkUrl();
				redirect = redirectBO.getLinkForList(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);

				String url = "";
				if(mode.equals("list_inspect")) url = "/qc/inspect/list.jsp?mode="+mode;
				else if(mode.equals("list_result")) url = "/qc/result/list.jsp?mode="+mode;
				else if(mode.equals("list_return")) url = "/qc/return/list.jsp?mode="+mode;
				else if(mode.equals("list_rework")) url = "/qc/rework/list.jsp?mode="+mode;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			///////////////////////////////////////
			// 검사결과 작성,보기 및 수정 폼
			///////////////////////////////////////
			else if(mode.equals("write_inspect") || mode.equals("view_result") || mode.equals("view_result_p") || mode.equals("write_return") || mode.equals("write_rework")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.qc.entity.InspectionMasterTable table = new com.anbtech.qc.entity.InspectionMasterTable();

				table = qcBO.getInspectionResultWriteForm(mode,request_no,item_code,login_id);
				request.setAttribute("ITEM_INFO",table);

				//선택된 품목에 정의된 검사항목 가져오기
				//기존값이 있을 경우에는 값을 세팅하여 가져온다.
				ArrayList list = new ArrayList();
				list = qcBO.getInspectionItemByItem(mode,request_no,item_code,login_id);
				request.setAttribute("INSPECTION_ITEM_LIST",list);

				//링크문자열 가져오기
				com.anbtech.qc.business.QualityCtrlLinkUrlBO redirectBO = new com.anbtech.qc.business.QualityCtrlLinkUrlBO(con);
				com.anbtech.qc.entity.QualityCtrlLinkUrl redirect = new com.anbtech.qc.entity.QualityCtrlLinkUrl();
				redirect = redirectBO.getLinkForView(mode,searchword,searchscope,category,page,login_id);
				request.setAttribute("Redirect",redirect);
				

				String url = "";
				if(mode.equals("write_inspect")) url = "/qc/inspect/write.jsp?mode="+mode;
				else if(mode.equals("view_result")) url = "/qc/result/view.jsp?mode="+mode;
				else if(mode.equals("view_result_p")) url = "/qc/result/view_p.jsp?mode="+mode;
				else if(mode.equals("write_return")) url = "/qc/return/write.jsp?mode="+mode;
				else if(mode.equals("write_rework")) url = "/qc/rework/write.jsp?mode="+mode;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			///////////////////////////////////////
			// 검사내역 등록 및 수정 폼
			///////////////////////////////////////
			else if(mode.equals("write_inspection_value") || mode.equals("view_inspection_value")){
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);

				ArrayList list = new ArrayList();
				list = qcBO.getInspectionValueList(request_no,item_code,inspection_code,sampled_quantity);
				request.setAttribute("INSPECTION_VALUE_LIST",list);
	
				String url = "";
				if(mode.equals("write_inspection_value")){
					url = "/qc/inspect/write_inspection_value.jsp?mode="+mode+"&request_no="+request_no+"&item_code="+item_code+"&inspection_code="+inspection_code+"&sampled_quantity="+sampled_quantity;
				}else if(mode.equals("view_inspection_value")){
					url = "/qc/result/view_inspection_value.jsp?mode="+mode+"&request_no="+request_no+"&item_code="+item_code+"&inspection_code="+inspection_code+"&sampled_quantity="+sampled_quantity;				
				}

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			///////////////////////////////////////
			// 불합격정보 입력,수정,보기 폼
			///////////////////////////////////////
			else if(mode.equals("write_failure_info") || mode.equals("modify_failure_info") || mode.equals("view_failure_info")){
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.entity.FailureInfoTable table = new com.anbtech.qc.entity.FailureInfoTable();

				//불량항목에 대한 정보를 가져온다.
				table = qcDAO.getFailureInfo(request_no,item_code,serial_no,inspection_code);
				request.setAttribute("FAILURE_INFO",table);

				//선택된 품목에 존재하는 불량리스트를 가져온다.
				ArrayList list = new ArrayList();
				list = qcDAO.getFailureList(request_no,item_code);
				request.setAttribute("FAILURE_LIST",list);
	
				String url = "";
				if(mode.equals("view_failure_info")) url = "/qc/failure/view_failure_info.jsp?mode="+mode+"&request_no="+request_no+"&item_code="+item_code+"&bad_quantity="+bad_quantity;
				else url = "/qc/failure/write_failure_info.jsp?mode="+mode+"&request_no="+request_no+"&item_code="+item_code+"&bad_quantity="+bad_quantity+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e;

				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			///////////////////////////////////////
			// 불합격정보 삭제처리
			///////////////////////////////////////
			else if(mode.equals("delete_failure_info")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				qcDAO.deleteFailureInfo(request_no,item_code,inspection_code);

				redirectUrl = "QualityCtrlServlet?mode=write_failure_info&request_no="+request_no+"&item_code="+item_code+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e;
			}

			///////////////////////////////////////
			// 제품의 시리얼 번호 가져오기
			///////////////////////////////////////
			else if(mode.equals("get_serial_no")){
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);

				String[] goods_serial = new String[2];
				goods_serial = qcBO.getGoodsSerialNo(produce_year,produce_month,item_code,lot_quantity);
				request.setAttribute("SERIAL_NO",goods_serial);

				getServletContext().getRequestDispatcher("/qc/inspect/get_serial_no.jsp").forward(request,response);
			}

			////////////////////////////////////////////////////
			// 품질검사결과 조회
			///////////////////////////////////////////////////
			else if(mode.equals("failure_info")){
				com.anbtech.qc.business.QualityCtrlBO qcBO	= new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.qc.db.QualityCtrlDAO qcDAO		= new com.anbtech.qc.db.QualityCtrlDAO(con);
				ArrayList failure_list = new ArrayList();

				//최초 실행시 오늘날짜에 검사된 정보를 검색한다.
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
				if(searchword.equals("")) searchword = "inspect_date|" + vans.format(now) + vans.format(now);

				failure_list = qcDAO.getFailureInfoList(mode,searchword,searchscope,page,login_id);
				request.setAttribute("FAILURE_LIST",failure_list);

				getServletContext().getRequestDispatcher("/qc/failure/failure_list.jsp?mode="+mode).forward(request,response);
			}


			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			//에러출력 페이지로 분기
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

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/qc/";

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

		//데이터 파라미터들
		String request_no			= multi.getParameter("request_no");
		String item_code			= multi.getParameter("item_code");
		String item_name			= multi.getParameter("item_name");
		String item_desc			= multi.getParameter("item_desc");
		String inspected_quantity	= multi.getParameter("inspected_quantity");
		String bad_item_quantity	= multi.getParameter("bad_item_quantity");
		String inspection_result	= multi.getParameter("inspection_result");
		String bad_percentage		= multi.getParameter("bad_percentage");
		String other_info			= multi.getParameter("other_info");
		String factory_code			= multi.getParameter("factory_code");
		String factory_name			= multi.getParameter("factory_name");
		String lot_quantity			= multi.getParameter("lot_quantity");
		String lot_no				= multi.getParameter("lot_no");
		String inspection_items		= multi.getParameter("inspection_items");	//검사항목코드들,콤마로 구분
		String item_type			= multi.getParameter("item_type");
		String request_date			= multi.getParameter("request_date");

		//검사내역 등록시 추가된 파라미터들
		String inspection_code		= multi.getParameter("inspection_code");	//검사항목코드
		String inspection_name		= multi.getParameter("inspection_name");	//검사항목명

		//불량정보 등록시 추가된 파라미터들
		String serial_no			= multi.getParameter("serial_no");
		String serial_no_s			= multi.getParameter("serial_no_s");
		String serial_no_e			= multi.getParameter("serial_no_e");
		String why_failure			= multi.getParameter("why_failure");

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			///////////////////////////////////////
			// 검사결과 등록처리
			///////////////////////////////////////
			if(mode.equals("write_inspect")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.pu.db.PurchaseMgrDAO puDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
				
					//검사결과 저장
					qcDAO.updInspectionInfo(request_no,item_code,inspected_quantity,bad_item_quantity,inspection_result,bad_percentage,other_info,login_id,serial_no_s,serial_no_e,lot_no);

					//검사대상 품목의 품목계정코드를 가져온다. 제품(M,1,2)인경우와 원자재(4,5,6)인 경우에
					//품질검사처리과정이 달라진다.
					//
					//M  완제품(자체생산) 
					//1  반제품(사내생산) 
					//2  반제품(외주생산) 
					//4  원자재(단품) 
					//5  원자재(상품) 
					//6  원자재(부분도급) 
					String item_typs_s = "";
					if(item_type.equals("M") || item_type.equals("1") || item_type.equals("2")) item_typs_s = "GOODS";
					else if(item_type.equals("4") || item_type.equals("5") || item_type.equals("6")) item_typs_s = "COMP";


					//제품인 경우에 일련번호정보 저장
					if(item_typs_s.equals("GOODS")){
						String model_code = cmDAO.getModelCode(item_code);
						String produce_year = request_date.substring(2,4);
						String produce_month = request_date.substring(5,7);

						String no_s = serial_no_s.substring(serial_no_s.length()-4);
						String no_e = serial_no_e.substring(serial_no_e.length()-4);

						qcDAO.saveGoodsSerialInfo(produce_year,produce_month,item_code,model_code,no_s,no_e,request_no);
					}
					
					////////////////////////////////////////////////////
					//원자재이고 검사결과가 합격 또는 특채인 경우
					////////////////////////////////////////////////////
					if(item_typs_s.equals("COMP") && (inspection_result.equals("PASS") || inspection_result.equals("PASS2"))){
						//재고DB의 입고예정수량 업데이트(검사수량만큼 입고예정수량에서 빼준다.)
						stBO.updateIntoQuantityWhenInspectionFinished(item_code,"-"+lot_quantity,factory_code,"","","");

						//재고DB의 현재고수량 업데이트(합격된 수량만큼 현재고수량을 더해준다.)
						int stock_quantity = Integer.parseInt(lot_quantity) - Integer.parseInt(bad_item_quantity);
						stBO.updateStockQuantity(item_code,Integer.toString(stock_quantity),factory_code,"","","");

						//재고DB의 불량품수량 업데이트
						//stBO.updateBadQuantity(item_code,bad_item_quantity,factory_code,"","","");					

						//입고품목들의 상태코드를 입고완료(S25)로 바꾸어준다.
						puDAO.updateProcessStat("pu_entered_item","enter_no",lot_no,item_code,"S25");

						//발주번호 및 입고번호를 파악하기 위해 입고품목 정보를 가져온다.
						com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
						table = puDAO.getEnterItemInfo(lot_no,item_code);

						//발주품목들의 상태코드를 입고완료(S25)로 바꾸어준다.
						puDAO.updateProcessStat("pu_order_item","order_no",table.getOrderNo(),item_code,"S25");
						
						//구매요청품목들의 상태코드를 입고완료(S25)로 바꾸어준다.
						puDAO.updateProcessStat("pu_requested_item","request_no",table.getRequestNo(),item_code,"S25");


						//불합격된 수량은 반품처리하기 위해 예외출고(출고유형:반품)로 등록한다.
						//원자재의 경우 수불현황에 구매입고 정보가 있기 때문에 입고되지 못한 불량수량만큼은
						//반품출고로 등록해 줘야 한다.
						if(Double.parseDouble(bad_item_quantity) > 0){
							//(1)예외입출고번호 계산
							String inout_no = stDAO.getEtcInOutNo("OUT");
							//(2)출고금액 계산(불량수량*품목단가)
							String total_mount = Double.toString(Double.parseDouble(bad_item_quantity)*Double.parseDouble(table.getUnitCost()));
							//(3)등록일자
							java.util.Date now = new java.util.Date();
							java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
							String w_time = vans.format(now);
							//(4)예외출고정보 저장
							stDAO.saveEtcInOutInfo("OUT",inout_no,w_time,total_mount,"KRW","RO","","",inout_no.substring(4,6),inout_no.substring(6,8),inout_no.substring(9,12),"","",login_id,"");
							//(5)예외출고품목 저장
							stDAO.saveEtcInOutItemInfo(inout_no,item_code,table.getItemName(),table.getItemDesc(),bad_item_quantity,table.getEnterUnit(),total_mount,table.getUnitCost(),table.getFactoryCode(),table.getFactoryName(),table.getWarehouseCode(),table.getWarehouseName(),"","");

							//(6)수불현황에 반품출고정보 저장
							stDAO.addInOutInfo("EO",w_time,inout_no,item_code,table.getItemName(),table.getItemDesc(),item_type,"-" + bad_item_quantity,table.getEnterUnit(),table.getUnitCost(),total_mount,table.getFactoryCode(),table.getFactoryName(),table.getWarehouseCode(),table.getWarehouseName(),"0","0");
						}
					}

					////////////////////////////////////////////////////
					//제품이고 검사결과가 합격인 경우
					////////////////////////////////////////////////////
					if(item_typs_s.equals("GOODS") && inspection_result.equals("PASS")){
						//재고DB의 현재고수량 업데이트(합격된 수량만큼 현재고수량을 더해준다.)
						int stock_quantity = Integer.parseInt(lot_quantity) - Integer.parseInt(bad_item_quantity);
						stBO.updateStockQuantity(item_code,Integer.toString(stock_quantity),factory_code,factory_name,"","");

						//불합격 수량값이 0보다 큰 경우
						//반품 및 사후 재검사를 위해 현 입고품목을 불합격 수량만큼 재생산의뢰품목(상태코드:S07)으로 새로 등록한다.
						if(Double.parseDouble(bad_item_quantity) > 0) qcBO.saveReworkItemInfo(request_no,item_code,bad_item_quantity,serial_no_s,serial_no_e);

						///////////////////////////////////
						//수불현황에 생산입고(MI)정보 저장
						///////////////////////////////////
						//addInOutInfo(String inout_type,String inout_date,String ref_no,String item_code,String item_name,String item_desc,String item_type,String quantity,String unit,String unit_cost,String total_cost,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String stock_unit_cost,String stock_quantity)

						//수불일자
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String w_time = vans.format(now);

						//작지번호에 해당하는 정보 가져오기
						com.anbtech.mm.entity.mfgMasterTable table = new com.anbtech.mm.entity.mfgMasterTable();
						com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);
						table = mfgDAO.getMfgMasterItem(lot_no,factory_code);
						stDAO.addInOutInfo("MI",w_time,lot_no,item_code,item_name,item_desc,item_type,Integer.toString(stock_quantity),table.getItemUnit(),"0","0",factory_code,factory_name,"","","0","0");
						//여기까지 수불정보 저장
					}

					////////////////////////////////////////////////
					//원자재이고 검사결과가 반품인 경우
					////////////////////////////////////////////////
					else if(item_typs_s.equals("COMP") && inspection_result.equals("RETURN")){
						//재고DB의 입고예정수량 업데이트,입고예정수량에서 LOT 수량만큼을 뺀다. 
						stBO.updateIntoQuantityWhenInspectionFinished(item_code,"-"+lot_quantity,factory_code,"","","");
						
						//입고품목들의 상태코드를 입고완료(S25)로 바꾸어준다. 불합격이더라도 입고는 된 것이므로...
						puDAO.updateProcessStat("pu_entered_item","enter_no",lot_no,item_code,"S25");

						//발주번호 및 입고번호를 파악하기 위해 입고품목 정보를 가져온다.
						com.anbtech.pu.entity.EnterInfoTable table = new com.anbtech.pu.entity.EnterInfoTable();
						table = puDAO.getEnterItemInfo(lot_no,item_code);

						//발주품목들의 상태코드를 입고완료(S25)로 바꾸어준다.
						puDAO.updateProcessStat("pu_order_item","order_no",table.getOrderNo(),item_code,"S25");
						
						//구매요청품목들의 상태코드를 입고완료(S25)로 바꾸어준다.
						puDAO.updateProcessStat("pu_requested_item","request_no",table.getRequestNo(),item_code,"S25");

						//검사수량 모두를 반품처리하기 위해 예외출고(출고유형:반품)로 등록한다.
						//
						//(1)예외입출고번호 계산
						String inout_no = stDAO.getEtcInOutNo("OUT");
						//(2)출고금액 계산(불량수량*품목단가)
						String total_mount = Double.toString(Double.parseDouble(lot_quantity)*Double.parseDouble(table.getUnitCost()));
						//(3)등록일자
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String w_time = vans.format(now);
						//(4)예외출고정보 저장
						stDAO.saveEtcInOutInfo("OUT",inout_no,w_time,total_mount,"KRW","RO","","",inout_no.substring(4,6),inout_no.substring(6,8),inout_no.substring(9,12),"","",login_id,"");
						//(5)예외출고품목 저장
						stDAO.saveEtcInOutItemInfo(inout_no,item_code,table.getItemName(),table.getItemDesc(),lot_quantity,table.getEnterUnit(),total_mount,table.getUnitCost(),table.getFactoryCode(),table.getFactoryName(),table.getWarehouseCode(),table.getWarehouseName(),"","");

						//(6)수불현황에 반품출고정보 저장
						stDAO.addInOutInfo("EO",w_time,inout_no,item_code,table.getItemName(),table.getItemDesc(),item_type,"-" + lot_quantity,table.getEnterUnit(),table.getUnitCost(),total_mount,table.getFactoryCode(),table.getFactoryName(),table.getWarehouseCode(),table.getWarehouseName(),"0","0");
						//여기까지 예외출고 및 수불현황 기록
					}

					////////////////////////////////////////////////
					//제품이고 검사결과가 재작업인 경우
					////////////////////////////////////////////////
					else if(item_typs_s.equals("GOODS") && inspection_result.equals("REWORK")){
						//반품 및 사후 재검사를 위해 현 입고품목을 불합격 수량만큼 재검사의뢰품목(상태코드:S07)으로 새로 등록한다.
						qcBO.saveReworkItemInfo(request_no,item_code,lot_quantity,serial_no_s,serial_no_e);
					}

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "QualityCtrlServlet?mode=list_inspect";
			}

			///////////////////////////////////////
			// 재작업결과 등록처리
			///////////////////////////////////////
			else if(mode.equals("write_rework")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);

				//재작업결과저장
				qcDAO.updInspectionInfo(request_no,item_code,other_info,login_id,lot_no);

				redirectUrl = "QualityCtrlServlet?mode=list_rework";
			}

			///////////////////////////////////////
			// 재검사결과 등록처리
			///////////////////////////////////////
			else if(mode.equals("write_return")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.pu.db.PurchaseMgrDAO puDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
				
					//검사결과 저장
					qcDAO.updInspectionInfo(request_no,item_code,inspected_quantity,bad_item_quantity,inspection_result,bad_percentage,other_info,login_id,serial_no_s,serial_no_e,lot_no);
					
					//품목계정 가져오기
					String item_typs_s = "";
					if(item_type.equals("M") || item_type.equals("1") || item_type.equals("2")) item_typs_s = "GOODS";
					else if(item_type.equals("4") || item_type.equals("5") || item_type.equals("6")) item_typs_s = "COMP";

					///////////////////////////////////////////////////
					//제품이고 검사결과가 합격인 경우
					///////////////////////////////////////////////////
					if(item_typs_s.equals("GOODS") && inspection_result.equals("PASS")){
						//재고DB의 현재고수량 업데이트(합격된 수량만큼 현재고수량을 더해준다.)
						int stock_quantity = Integer.parseInt(lot_quantity) - Integer.parseInt(bad_item_quantity);
						stBO.updateStockQuantity(item_code,Integer.toString(stock_quantity),factory_code,"","","");

						//불합격 수량값이 0보다 큰 경우
						//반품 및 사후 재검사를 위해 현 입고품목을 불합격 수량만큼 재검사의뢰품목(상태코드:S07)으로 새로 등록한다.
						if(Double.parseDouble(bad_item_quantity) > 0) qcBO.saveReworkItemInfo(request_no,item_code,bad_item_quantity,serial_no_s,serial_no_e);

						///////////////////////////////////
						//수불현황에 생산입고(MI)정보 저장
						///////////////////////////////////
						//수불일자
						java.util.Date now = new java.util.Date();
						java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
						String w_time = vans.format(now);

						//작지번호에 해당하는 정보 가져오기
						com.anbtech.mm.entity.mfgMasterTable table = new com.anbtech.mm.entity.mfgMasterTable();
						com.anbtech.mm.db.mfgModifyDAO mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);
						table = mfgDAO.getMfgMasterItem(lot_no,factory_code);
						stDAO.addInOutInfo("MI",w_time,lot_no,item_code,item_name,item_desc,item_type,Integer.toString(stock_quantity),table.getItemUnit(),"0","0",factory_code,factory_name,"","","0","0");
						//여기까지 수불정보 저장
					}

					///////////////////////////////////////////////
					//제품이고 검사결과 또다시 재작업인 경우
					///////////////////////////////////////////////
					else if(item_typs_s.equals("GOODS") && inspection_result.equals("REWORK")){
						//반품 및 사후 재검사를 위해 현 입고품목을 불합격 수량만큼 재검사의뢰품목(상태코드:S07)으로 새로 등록한다.
						qcBO.saveReworkItemInfo(request_no,item_code,lot_quantity,serial_no_s,serial_no_e);
					}

					///////////////////////////////////////////////
					//제품이고 검사결과 폐기처리인 경우
					///////////////////////////////////////////////
					else if(item_typs_s.equals("GOODS") && inspection_result.equals("FAIL")){

					}

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "QualityCtrlServlet?mode=list_return";
			}
			
			///////////////////////////////////////
			// 검사내역 등록 처리
			///////////////////////////////////////
			else if(mode.equals("write_inspection_value")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{

					//기존 검사내역정보를 먼저 삭제한다.(qc_inspection_result 테이블)
					qcDAO.deleteInspectionValue(request_no,item_code,inspection_code);

					//검사내역정보 저장
					int good_quantity = 0;	//합격수량
					int bad_quantity = 0;	//불합격수량
					String value_str = "";
					for(int k=1; k<=Integer.parseInt(inspected_quantity); k++){
						String inspection_value = multi.getParameter("inspection_value_" + k);
						String is_passed		= multi.getParameter("is_passed_" + k);
						value_str += inspection_value + "|" + is_passed + "#";

						if(is_passed.equals("Y")) good_quantity++;
						else bad_quantity++;
					}
					qcDAO.saveInspectionValue(request_no,item_code,inspection_code,value_str);

					//기존 검사결과정보를 먼저 삭제한다.(qc_inspection_value 테이블)
					qcDAO.deleteInspectionResult(request_no,item_code,inspection_code);
					//검사결과정보를 저장한다.
					qcDAO.saveInspectionResult(request_no,item_code,inspection_code,inspected_quantity,Integer.toString(good_quantity),Integer.toString(bad_quantity));

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				getServletContext().getRequestDispatcher("/qc/inspect/saved_result.jsp").forward(request,response);
			}

			///////////////////////////////////////
			// 불량원인정보 추가처리
			///////////////////////////////////////
			else if(mode.equals("write_failure_info")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				com.anbtech.qc.entity.InspectionItemTable table = new com.anbtech.qc.entity.InspectionItemTable();

				//입력된 검사항목코드에 대한 검사항목명을 가져온다. 없으면 에러 메시지 출력
				table = qcDAO.getInspectionItemInfo(inspection_code);
				inspection_name = table.getInspectionName();

				if(inspection_name == null){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('존재하지 않는 검사항목코드입니다. 코드를 확인하십시오.');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}
				
				qcDAO.saveFailureInfo(request_no,item_code,serial_no,inspection_code,inspection_name,why_failure);

				redirectUrl = "QualityCtrlServlet?mode=write_failure_info&request_no="+request_no+"&item_code="+item_code+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e;
			}

			///////////////////////////////////////
			// 불량원인정보 수정처리
			///////////////////////////////////////
			else if(mode.equals("modify_failure_info")){
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				qcDAO.updateFailureInfo(request_no,item_code,serial_no,inspection_code,inspection_name,why_failure);

				redirectUrl = "QualityCtrlServlet?mode=write_failure_info&request_no="+request_no+"&item_code="+item_code+"&serial_no_s="+serial_no_s+"&serial_no_e="+serial_no_e;
			}

			if (redirectUrl.length() > 0) response.sendRedirect(redirectUrl);

		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}
