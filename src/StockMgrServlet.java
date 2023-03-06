import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class StockMgrServlet extends HttpServlet {

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
		
		String mid			= request.getParameter("mid")==null?"":request.getParameter("mid");
		String redirectUrl	= "";
		String mode			= request.getParameter("mode");
		String page			= request.getParameter("page");
		String searchword	= request.getParameter("searchword");
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");
		int l_maxlist		= request.getParameter("l_maxlist") == null?15:Integer.parseInt(request.getParameter("l_maxlist"));

		if (mode == null) mode = "list_item";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
		else searchword = com.anbtech.text.StringProcess.kwordProcess(searchword);
		if (searchscope == null) searchscope = "";
		if (category == null) category = "";

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

		//데이터 파라미터들
		String inout_no			= request.getParameter("inout_no");
		String upload_folder	= request.getParameter("upload_folder");
		String item_code		= request.getParameter("item_code");
		String in_or_out		= request.getParameter("in_or_out");
		String factory_code		= request.getParameter("factory_code");
		String factory_name		= request.getParameter("factory_name");
		String delivery_no		= request.getParameter("delivery_no");
		String inout_type		= request.getParameter("inout_type");
		String items			= request.getParameter("items"); // 자재일괄출고처리시. 출고의뢰번호1|품목코드1|수량1,출고의뢰번호2|품목코드2|수량2,

		String aid				= request.getParameter("aid");
		String out_no			= request.getParameter("out_no");
		String ref_no			= request.getParameter("ref_no");
		String delivery_quantity= request.getParameter("delivery_quantity");

		if (factory_name == null) factory_name = "";
		else factory_name = com.anbtech.text.StringProcess.kwordProcess(factory_name);
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////
			// 수불현황 조회리스트
			///////////////////////////////////////////////////
			if(mode.equals("list_inout")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				ArrayList inout_list = new ArrayList();

				inout_list = stDAO.getInOutList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("INOUT_LIST",inout_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"st_inout_master",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);
				
				getServletContext().getRequestDispatcher("/st/inout/list_inout.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 재고현황 조회리스트
			///////////////////////////////////////////////////
			else if(mode.equals("list_stock_master") || mode.equals("list_stock_real") || mode.equals("list_stock_rop") || mode.equals("list_stock_resonable")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				ArrayList stock_list = new ArrayList();

				stock_list = stDAO.getItemStockList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("STOCK_LIST",stock_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"v_item_stock_master",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);

				String url = "";
				if(mode.equals("list_stock_master")) url = "/st/stock/list_stock.jsp?mode="+mode;
				else if(mode.equals("list_stock_real")) url = "/st/stock/list_stock_real.jsp?mode="+mode;
				else if(mode.equals("list_stock_rop")) url = "/st/stock/list_stock_rop.jsp?mode="+mode;
				else if(mode.equals("list_stock_resonable")) url = "/st/stock/list_stock_resonable.jsp?mode="+mode;
				
				getServletContext().getRequestDispatcher(url).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 기타입출고정보 조회 리스트
			///////////////////////////////////////////////////
			else if(mode.equals("list_etc_inout")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				ArrayList list = new ArrayList();

				list = stDAO.getEtcInOutInfoList(mode,in_or_out,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("INOUT_LIST",list);
			
				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForEtcInOutList(mode,in_or_out,searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);

				if(in_or_out.equals("IN")) 
					getServletContext().getRequestDispatcher("/st/in/list_etc_in.jsp?mode="+mode).forward(request,response);
				else if(in_or_out.equals("OUT"))
					getServletContext().getRequestDispatcher("/st/out/list_etc_out.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			//  기타입출고정보 등록 및 수정 폼
			///////////////////////////////////////////////////
			else if (mode.equals("view_etc_inout_info") || mode.equals("write_etc_inout_info") || mode.equals("update_etc_inout_info")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.EtcInOutInfoTable table = new com.anbtech.st.entity.EtcInOutInfoTable();

				//선택된 입출고번호에 대한 정보를 가져온다.
				table = stBO.getEtcInOutInfoForm(mode,inout_no,login_id);
				request.setAttribute("INOUT_INFO",table);

				//수정모드일경우 첨부파일 리스트 가져오기
				if(mode.equals("update_etc_inout_info")){
					ArrayList arry = new ArrayList();
					arry = (ArrayList)stDAO.getEtcInOutFileList("st_etc_inout_info",inout_no);
					request.setAttribute("FILE_LIST",arry);
				}
				
				//선택된 입출고번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getEtcInOutItemList(inout_no);
				request.setAttribute("ITEM_LIST",item_list);

				if(in_or_out.equals("IN")){
					if(mode.equals("view_etc_inout_info"))
						getServletContext().getRequestDispatcher("/st/in/view_etc_in.jsp?mode="+mode).forward(request,response);
					else
						getServletContext().getRequestDispatcher("/st/in/write_etc_in.jsp?mode="+mode).forward(request,response);
				}else if(in_or_out.equals("OUT")){
					if(mode.equals("view_etc_inout_info"))
						getServletContext().getRequestDispatcher("/st/out/view_etc_out.jsp?mode="+mode).forward(request,response);
					else
						getServletContext().getRequestDispatcher("/st/out/write_etc_out.jsp?mode="+mode).forward(request,response);
				}
			}

			////////////////////////////////////////////////////
			//  기타입출고정보 삭제처리
			///////////////////////////////////////////////////
			else if (mode.equals("delete_etc_inout_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);

				//입출고품목이 존재하는지 체크
				int item_count = stDAO.getEtcInOutItemCount(inout_no);
				if(item_count > 0){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('삭제하고자 하는 입출고건에는 한개 이상의 입출고품목이 존재함으로 삭제할 수 없습니다.');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}else{
					stDAO.deleteEtcInOutInfo(inout_no);
				}
				
				redirectUrl = "StockMgrServlet?mode=list_etc_inout&in_or_out=" + in_or_out;

			}

			////////////////////////////////////////////////////
			// 기타입출고품목 등록폼 가져오기
			///////////////////////////////////////////////////
			else if(mode.equals("add_etc_inout_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.EtcInOutInfoTable table = new com.anbtech.st.entity.EtcInOutInfoTable();

				//선택된 입출고번호 및 품목코드에 대한 정보를 가져온다.
				table = stBO.getEtcInOutItemForm(mode,inout_no,item_code,login_id);
				request.setAttribute("INOUT_INFO",table);

				//선택된 입출고번호에 존재하는 품목리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getEtcInOutItemList(inout_no);
				request.setAttribute("ITEM_LIST",item_list);

				if(in_or_out.equals("IN"))
					getServletContext().getRequestDispatcher("/st/in/write_etc_in_item.jsp?mode="+mode+"&inout_type="+inout_type).forward(request,response);
				else if(in_or_out.equals("OUT"))
					getServletContext().getRequestDispatcher("/st/out/write_etc_out_item.jsp?mode="+mode+"&inout_type="+inout_type).forward(request,response);
			}

			//////////////////////////
			// ROP에 의한 구매요청처리
			//////////////////////////
			else if(mode.equals("request_rop_order")){
				com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);

				//요청번호 생성
				String request_no = purchaseDAO.getRequestNo();

				//요청시간
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String request_date = vans.format(now);

				//요청자정보
				com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
				com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
				userinfo = userDAO.getUserListById(login_id);

				con.setAutoCommit(false);
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//요청정보 저장	
					purchaseDAO.saveRequestInfo(request_no,request_date,userinfo.getAcCode(),userinfo.getDivision(),login_id,userinfo.getUserName(),"ROP","0","","",request_no.substring(3,5),request_no.substring(5,7),request_no.substring(8,11),factory_code,factory_name);
					
					//요청품목 저장
					stBO.saveRequestItem(mode,item_code,request_no);

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				getServletContext().getRequestDispatcher("/st/stock/result_request_rop.jsp").forward(request,response);
			}

			////////////////////////////////////////////////////////
			//  첨부파일 다운로드
			////////////////////////////////////////////////////////
			else if (mode.equals("download_etc_inout")){
					
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.EtcInOutInfoTable file = new com.anbtech.st.entity.EtcInOutInfoTable();
				
				file = stBO.getFileForDown("st_etc_inout_info", inout_no);
								
				String filename = file.getFname();
				String filetype = file.getFtype();
				String filesize = file.getFsize();
				String fileumask = file.getFumask();
					
				//boardpath 에서 파일까지 경로 지정
				String downFile = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/st/" + upload_folder + "/" + fileumask + ".bin";
				
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
			
			//////////////////////////
			// 공장간재고이동 폼
			//////////////////////////
			else if(mode.equals("write_item_shift_info") || mode.equals("view_item_shift_info")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.StShiftInfoTable table = new com.anbtech.st.entity.StShiftInfoTable();
				
				table=stBO.getShiftInfoForm(mode,login_id,mid);
				request.setAttribute("ITEM_SHIFT_INFO",table);
				
				if (mode.equals("view_item_shift_info"))	{
					getServletContext().getRequestDispatcher("/st/shift/view_fshift_info.jsp?mode="+mode).forward(request,response);
				} else {								
					getServletContext().getRequestDispatcher("/st/shift/write_fshift_info.jsp?mode="+mode).forward(request,response);
				}
			}

			//////////////////////////
			// 품목간재고이동 폼
			//////////////////////////
			else if(mode.equals("write_item_ishift_info")||mode.equals("view_item_ishift_info")){

				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.StShiftInfoTable table = new com.anbtech.st.entity.StShiftInfoTable();

				table=stBO.getShiftInfoForm(mode,login_id,mid);
				request.setAttribute("ITEM_SHIFT_INFO",table);
				
				if (mode.equals("view_item_ishift_info")) {
					getServletContext().getRequestDispatcher("/st/shift/view_ishift_info.jsp?mode="+mode).forward(request,response);
				} else {											
					getServletContext().getRequestDispatcher("/st/shift/write_ishift_info.jsp?mode="+mode).forward(request,response);
				}
			}

			//////////////////////////
			// 공장간재고이동정보 리스트
			//////////////////////////
			else if(mode.equals("list_item_shift_info")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				ArrayList shift_info_list = new ArrayList();

				shift_info_list = stDAO.getItemShiftInfoList(mode,searchword,searchscope,page,l_maxlist);
				request.setAttribute("SHIFT_INFO_LIST",shift_info_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"st_item_shift_info",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);

				getServletContext().getRequestDispatcher("/st/shift/list_shift_info.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 생산출고의뢰품목 리스트 출력(테이블:st_reserved_item_info)
			// 상태코드가 S53 이상이면서 기출고수량 < 요청수량 인 품목
			///////////////////////////////////////////////////
			else if(mode.equals("list_reserved_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = stDAO.getReservedItemList(mode,searchword,searchscope,login_id);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/list_reserved_item.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 선택된 생산출고의뢰품목의 전자결재를 위해 출고등록폼목에 저장
			///////////////////////////////////////////////////
			else if(mode.equals("app_reserved_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);

				//임시 전자결재 관리번호 생성
				String tmp_aid = System.currentTimeMillis() + "";

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//선택된 출고의뢰품목들을 결재품목에 등록한다.
					stBO.saveToDeliveryItem(mode,items,tmp_aid,login_id);

					con.commit();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//전자결재 화면으로 분기
				redirectUrl = "../gw/approval/module/part_TakeoutApp.jsp?out_no=" + tmp_aid;
			}

			////////////////////////////////////////////////////
			// 출고등록품목의 출고수량 수정처리
			// 요청된 수량을 모두 출고시키지 않고 나눠서 출고시킬 경우에 출고수량을 수정가능하게 한다.
			///////////////////////////////////////////////////
			else if(mode.equals("modify_delivery_quantity")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);

				stDAO.updateDeliveryQuantity(delivery_no,ref_no,item_code,aid,delivery_quantity);

				redirectUrl = "StockMgrServlet?mode=list_app_reserved_item&out_no=" + aid;
			}

			////////////////////////////////////////////////////
			// 선택된 전자결재관리번호에 속한 출고등록품목 리스트 출력(전자결재 상신 및 내용 보기화면)
			// mode == list_app_reserved_item : 전자결재 상신 화면,출고수량을 수정 가능
			// mode == view_app_reserved_item : 전자결재 내용 보기화면
			///////////////////////////////////////////////////
			else if(mode.equals("list_app_reserved_item") || mode.equals("view_app_reserved_item")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);

				//출고등록품목 리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedItemList(mode,out_no);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/list_delivery_item.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 생산출고등록품목 중 상태코드가 S57(출고결재승인완료상태)인 품목(테이블:st_deliveried_item_info)
			///////////////////////////////////////////////////
			else if(mode.equals("list_toenter_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = stDAO.getToEnterItemList(mode,searchword,searchscope,login_id);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/list_toenter_item.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 선택된 품목 일괄 출고처리
			///////////////////////////////////////////////////
			else if(mode.equals("delivery_reserved_item_all")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);

				//출고일자
				java.util.Date now = new java.util.Date();
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String delivery_date = vans.format(now);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//선택된 출고의뢰품목들을 일괄 출고시킨다.
					stBO.saveDeliveryItem(mode,items,delivery_date,login_id);

					con.commit();
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "StockMgrServlet?mode=list_toenter_item";
			}

			////////////////////////////////////////////////////
			// 생산출고완료품목 리스트 출력(테이블:st_deliveried_item_info)
			///////////////////////////////////////////////////
			else if(mode.equals("list_deliveried_item")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedItemList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"st_deliveried_item_info",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);
				
				getServletContext().getRequestDispatcher("/st/out/list_deliveried_item.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 생산출고현황 리스트 출력
			///////////////////////////////////////////////////
			else if(mode.equals("list_deliveried_info")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedInfoList(mode,searchword,searchscope,page,login_id,l_maxlist);
				request.setAttribute("ITEM_LIST",item_list);

				com.anbtech.st.business.StockLinkUrlBO redirectBO = new com.anbtech.st.business.StockLinkUrlBO(con);
				com.anbtech.st.entity.StockLinkUrl redirect = new com.anbtech.st.entity.StockLinkUrl();
				redirect = redirectBO.getRedirectForList(mode,"v_deliveried_info",searchword,searchscope,page,login_id);
				request.setAttribute("REDIRECT",redirect);
				
				getServletContext().getRequestDispatcher("/st/out/list_deliveried_info.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 생산출고현황 인쇄폼 보기
			///////////////////////////////////////////////////
			else if(mode.equals("print_deliveried_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

				//전자결재 정보 가져오기
				String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
				if(!aid.equals("")) appTable = appMgrDAO.getApprovalInfo("st_approval_info",aid,sign_path);
				request.setAttribute("Approval_Info",appTable);

				//출고등록품목 리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedItemList(mode,aid);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/print_deliveried_info.jsp?mode="+mode+"&aid="+aid).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 생산출고현황 인쇄폼 보기(Excell 폼)
			///////////////////////////////////////////////////
			else if(mode.equals("print_deliveried_info_excell")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
				com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

				//전자결재 정보 가져오기
				//String sign_path = com.anbtech.admin.db.ServerConfig.getConf("serverURL") + "/gw/approval/sign/";
				//if(!aid.equals("")) appTable = appMgrDAO.getApprovalInfo("st_approval_info",aid,sign_path);
				//request.setAttribute("Approval_Info",appTable);

				//출고등록품목 리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = stDAO.getDeliveriedItemList(mode,aid);
				request.setAttribute("ITEM_LIST",item_list);

				getServletContext().getRequestDispatcher("/st/out/print_deliveried_info_excell.jsp?mode="+mode).forward(request,response);
			}

			////////////////////////////////////////////////////
			// 긴급생산출고의뢰품목 등록 폼 및 상세보기 폼 가져오기
			///////////////////////////////////////////////////
			else if (mode.equals("write_etc_delivery") || mode.equals("add_etc_delivery") || mode.equals("modify_etc_delivery")){
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.entity.ReservedItemInfoTable table = new com.anbtech.st.entity.ReservedItemInfoTable();

				//선택된 출고의뢰번호에 대한 정보를 가져온다.
				table = stBO.getEtcDeliveryInfoForm(mode,delivery_no,item_code,login_id);
				request.setAttribute("DELIVERY_INFO",table);
				
				getServletContext().getRequestDispatcher("/st/out/write_etc_delivery.jsp?mode="+mode).forward(request,response);
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
		String upload_folder = request.getParameter("upload_folder");
		if(upload_folder == null) upload_folder = "";
		
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/st/" + upload_folder;

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//전페이지에서 값 받아온다. multi에서 가져옴
		String mode				= multi.getParameter("mode");
		String page				= multi.getParameter("page");
		String searchword		= multi.getParameter("searchword");
		String searchscope		= multi.getParameter("searchscope");
		String category			= multi.getParameter("category");
		String redirectUrl		= "";

		//공백으로 넘어오거나 null로 온 값들에 대한 처리.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

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

		//데이터 파라미터들
		String in_or_out		= multi.getParameter("in_or_out");
		String inout_no			= multi.getParameter("inout_no");
		String inout_type		= multi.getParameter("inout_type");
		String inout_date		= multi.getParameter("inout_date");
		String total_mount		= multi.getParameter("total_mount");
		String quantity			= multi.getParameter("quantity");
		String item_unit		= multi.getParameter("item_unit");
		String inout_cost		= multi.getParameter("inout_cost");
		String factory_code		= multi.getParameter("factory_code");
		String factory_name		= multi.getParameter("factory_name");
		String warehouse_code	= multi.getParameter("warehouse_code");
		String warehouse_name	= multi.getParameter("warehouse_name");
		String unit_type		= multi.getParameter("unit_type");
		String supplyer_code	= multi.getParameter("supplyer_code");
		String supplyer_name	= multi.getParameter("supplyer_name");
		String requester_div_name	= multi.getParameter("requester_div_name");
		String requester_div_code	= multi.getParameter("requester_div_code");
		String requester_id		= multi.getParameter("requester_id");
		String requester_info	= multi.getParameter("requester_info");
		String monetary_unit	= multi.getParameter("monetary_unit");
		String item_code		= multi.getParameter("item_code");
		String item_name		= multi.getParameter("item_name");
		String item_desc		= multi.getParameter("item_desc");
		String item_type		= multi.getParameter("item_type");
		String unit_cost		= multi.getParameter("unit_cost");

		
		// 재고이동관련정보
		String shift_no				= multi.getParameter("shift_no");			// 이동번호
		String shift_type			= multi.getParameter("shift_type");			// 이동유형
		String sr_factory_code		= multi.getParameter("sr_factory_code");	// source 공장코드
		String sr_factory_name		= multi.getParameter("sr_factory_name");	// source 공장명
		String sr_warehouse_code	= multi.getParameter("sr_warehouse_code");	// source 창고코드
		String sr_warehouse_name	= multi.getParameter("sr_warehouse_name");	// source 창고명
		String sr_item_code			= multi.getParameter("sr_item_code");		// source 품목코드
		String sr_item_name			= multi.getParameter("sr_item_name");		// source 품목명
		String sr_item_type			= multi.getParameter("sr_item_type");		// source 품목유형
		String sr_item_desc			= multi.getParameter("sr_item_desc");		// source 품목설명
		String dt_factory_code		= multi.getParameter("dt_factory_code");	// target 공장코드
		String dt_factory_name		= multi.getParameter("dt_factory_name");	// target 공장명
		String dt_warehouse_code	= multi.getParameter("dt_warehouse_code");	// target 창고코드
		String dt_warehouse_name	= multi.getParameter("dt_warehouse_name");	// target 창고명
		String dt_item_code			= multi.getParameter("dt_item_code");		// target 품목코드
		String dt_item_name			= multi.getParameter("dt_item_name");		// target 품목명
		String dt_item_type			= multi.getParameter("dt_item_type");		// target 품목유형
		String dt_item_desc			= multi.getParameter("dt_item_desc");		// target 품목설명
		String stock_unit			= multi.getParameter("stock_unit");			// 재고 단위
		String requestor_id			= multi.getParameter("requestor_id");		// 등록자 ID
		String requestor_info		= multi.getParameter("requestor_info");		// 등록자 NAME
		String requestor_div_code	= multi.getParameter("requestor_div_code");	// 등록자 부서코드
		String requestor_div_name	= multi.getParameter("requestor_div_name");	// 등록자 부서명
		String reg_date				= multi.getParameter("reg_date");			// 이동일자
	
		//자재출고처리관련 파라미터들
		String delivery_no			= multi.getParameter("delivery_no");		// 출고의뢰번호
		String old_quantity			= multi.getParameter("old_quantity");		// 수정전출고수량
		String ref_no				= multi.getParameter("ref_no");				// 관련번호

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//////////////////////////
			// 기타입출고정보 등록
			//////////////////////////			
			if(mode.equals("write_etc_inout_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.EtcInOutInfoTable file = new com.anbtech.st.entity.EtcInOutInfoTable();
			
				inout_no = stDAO.getEtcInOutNo(in_or_out);
				
				if(in_or_out.equals("IN"))		
					stDAO.saveEtcInOutInfo(in_or_out,inout_no,inout_date,total_mount,"KRW",inout_type,"","",inout_no.substring(3,5),inout_no.substring(5,7),inout_no.substring(8,11),"","",login_id,"");
				else if(in_or_out.equals("OUT"))
					stDAO.saveEtcInOutInfo(in_or_out,inout_no,inout_date,total_mount,"KRW",inout_type,"","",inout_no.substring(4,6),inout_no.substring(6,8),inout_no.substring(9,12),"","",login_id,"");

				//첨부파일 업로더
				file = (com.anbtech.st.entity.EtcInOutInfoTable)stBO.getFile_frommulti(multi,inout_no,filepath);
				//업로딩 된 첨부파일 정보를 DB에 저장하기
				stBO.updFile("st_etc_inout_info",inout_no,file.getFname(),file.getFtype(),file.getFsize(),file.getFumask());

				redirectUrl = "StockMgrServlet?mode=add_etc_inout_item&inout_no="+inout_no+"&in_or_out="+in_or_out+"&inout_type="+inout_type;
			}

			//////////////////////////
			// 기타입출고정보수정처리
			//////////////////////////
			else if(mode.equals("update_etc_inout_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.EtcInOutInfoTable file = new com.anbtech.st.entity.EtcInOutInfoTable();

				//multi에서 파일정보를 가져와서 처리한다.
				ArrayList file_list = stDAO.getEtcInOutFileList("st_etc_inout_info",inout_no);
				//파일 업로더
				file = (com.anbtech.st.entity.EtcInOutInfoTable)stBO.getFile_frommulti(multi,inout_no,filepath,file_list);
					
				con.setAutoCommit(false);
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					// 업로딩 된 첨부파일 정보를 DB에 저장하기
					stBO.updFile("st_etc_inout_info",inout_no,file.getFname(),file.getFtype(),file.getFsize(),file.getFumask());
					
					//수불정보 수정
					stDAO.updateEtcInOutInfo(inout_no,inout_type,inout_date,requester_div_code,requester_div_name,requester_id,requester_info,total_mount,monetary_unit,supplyer_code,supplyer_name);
					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "StockMgrServlet?mode=add_etc_inout_item&inout_no="+inout_no+"&in_or_out="+in_or_out;
			}

			//////////////////////////
			// 기타입출고품목등록처리
			//////////////////////////
			else if(mode.equals("add_etc_inout_item")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//기타입출고품목정보 기록
					stDAO.saveEtcInOutItemInfo(inout_no,item_code,item_name,item_desc,quantity,item_unit,inout_cost,unit_cost,factory_code,factory_name,warehouse_code,warehouse_name,"","");

					//수불현황에 기록
					if(in_or_out.equals("IN")){
//						inout_type = "EI";
					}
					else if(in_or_out.equals("OUT")){
//						inout_type = "EO";
						quantity = "-" + quantity;
					}

					java.util.Date now = new java.util.Date();
					java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
					inout_date = vans.format(now);
					stDAO.addInOutInfo(inout_type,inout_date,inout_no,item_code,item_name,item_desc,item_type,quantity,item_unit,unit_cost,inout_cost,factory_code,factory_name,warehouse_code,warehouse_name,"0","0");

					//해당품목의 재고정보에서 현재고수량을 수정한다.
					stBO.updateStockQuantity(item_code,quantity,factory_code,factory_name,warehouse_code,warehouse_name);

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				redirectUrl = "StockMgrServlet?mode=add_etc_inout_item&inout_no="+inout_no+"&in_or_out="+in_or_out+"&inout_type="+inout_type;
			}

			///////////////////////////////
			// 재고이동관리등록(품목간&공장간)
			//////////////////////////////
			else if(mode.equals("write_item_shift_info")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.StockInfoTable table = new com.anbtech.st.entity.StockInfoTable();
				
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//이동번호생성
					shift_no = stDAO.getShiftNo();

					//이동정보기록
					stDAO.saveItemShiftInfo(shift_no,shift_type,sr_factory_code,sr_factory_name,sr_warehouse_code,sr_warehouse_name,sr_item_code,sr_item_name,sr_item_type,sr_item_desc,dt_factory_code,dt_factory_name,dt_warehouse_code,dt_warehouse_name,dt_item_code,dt_item_name,dt_item_type,dt_item_desc,stock_unit,quantity,requestor_id,reg_date,shift_no.substring(3,5),shift_no.substring(5,7),shift_no.substring(8,11));

					//해당품목의 원가정보를 가져온다.
					table = stDAO.getItemUnitCostInfo(sr_item_code);
					inout_cost = Double.toString(Double.parseDouble(table.getUnitCostA()) * Double.parseDouble(quantity));
				
					//수불현황에 기록하고, 재고정보중 현재고수량을 감한다.
					stDAO.addInOutInfo("MV",reg_date,shift_no,sr_item_code,sr_item_name,sr_item_desc,sr_item_type,"-" + quantity,stock_unit,table.getUnitCostA(),inout_cost,sr_factory_code,sr_factory_name,sr_warehouse_code,sr_warehouse_name,"0","0");

					stBO.updateStockQuantity(sr_item_code,"-" + quantity,sr_factory_code,sr_factory_name,sr_warehouse_code,sr_warehouse_name);

					//수불현황에 기록하고, 재고정보중 현재고수량을 더한다.
					stDAO.addInOutInfo("MV",reg_date,shift_no,dt_item_code,dt_item_name,dt_item_desc,dt_item_type,quantity,stock_unit,table.getUnitCostA(),inout_cost,dt_factory_code,dt_factory_name,dt_warehouse_code,dt_warehouse_name,"0","0");

					stBO.updateStockQuantity(dt_item_code,quantity,dt_factory_code,dt_factory_name,dt_warehouse_code,dt_warehouse_name);

					con.commit();

				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				redirectUrl = "StockMgrServlet?mode=list_item_shift_info";
			}

/*
			///////////////////////////////
			// 자재출고처리
			//////////////////////////////
			else if(mode.equals("modify_reserved_item")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				com.anbtech.st.entity.StockInfoTable table = new com.anbtech.st.entity.StockInfoTable();

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//출고시간
					java.util.Date now = new java.util.Date();
					java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
					String delivery_date = vans.format(now);

					int distance = Integer.parseInt(old_quantity) - Integer.parseInt(quantity);

					//해당출고의뢰건의 출고정보를(수량,출고자정보,출고일자,상태코드 등) 업데이트
					stDAO.updateRreservedItemInfo(mode,delivery_no,item_code,quantity,item_unit,login_id,delivery_date);

					//해당품목의 원가정보를 가져온다.
					table = stDAO.getItemUnitCostInfo(item_code);
					inout_cost = Double.toString(Double.parseDouble(table.getUnitCostA()) * Double.parseDouble(quantity));


					//수불현황 기록
					stDAO.addInOutInfo("MO",delivery_date,delivery_no,item_code,item_name,item_desc,item_type,Integer.toString(distance),item_unit,table.getUnitCostA(),inout_cost,factory_code,factory_name,warehouse_code,warehouse_name,"0","0");

					//재고정보중 출고예정수량(outto_quantity) 업데이트
					stBO.updateOuttoQuantity(item_code,Integer.toString(distance),factory_code,factory_name,warehouse_code,warehouse_name);

					//재고정보중 출고수량(delivery_quantity) 업데이트
					stBO.updateDeliveryQuantity(item_code,Integer.toString(-distance),factory_code,factory_name,warehouse_code,warehouse_name);

					//생상관리 모듈의 테이블 업데이트
					stDAO.updateMfgMoudle(delivery_no,ref_no,item_code,factory_code,Integer.toString(-distance));

					//여기까지

					con.commit();
				}catch(Exception e){
					con.rollback();

					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
				redirectUrl = "StockMgrServlet?mode=list_reserved_item&searchscope="+searchscope+"&searchword="+searchword;
			}

*/
			//////////////////////////
			// 기타출고의뢰정보 등록처리
			//////////////////////////			
			if(mode.equals("write_etc_delivery") || mode.equals("add_etc_delivery")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
			
				if(mode.equals("write_etc_delivery")) delivery_no = stDAO.getDeliveryNo();
				stDAO.saveEtcDeliveryInfo(delivery_no,item_code,item_name,item_desc,item_type,quantity,item_unit,reg_date,factory_code,factory_name,warehouse_code,warehouse_name,ref_no,requestor_div_code,requestor_div_name,requestor_id,requestor_info,delivery_no.substring(2,4),delivery_no.substring(4,6),delivery_no.substring(7,10));

				redirectUrl = "StockMgrServlet?mode=add_etc_delivery&delivery_no="+delivery_no;
			}

			//////////////////////////
			// 기타출고의뢰정보 수정처리
			//////////////////////////			
			if(mode.equals("modify_etc_delivery")){
				com.anbtech.st.db.StockMgrDAO stDAO = new com.anbtech.st.db.StockMgrDAO(con);
				com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
				stDAO.updateEtcDeliveryInfo(delivery_no,item_code,item_name,item_desc,item_type,quantity,item_unit,reg_date,factory_code,factory_name,warehouse_code,warehouse_name,ref_no,requestor_div_code,requestor_div_name,requestor_id,requestor_info);

				redirectUrl = "StockMgrServlet?mode=list_etc_delivery&searchscope="+searchscope+"&searchword="+searchword;
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
