import com.anbtech.gm.entity.*;
import com.anbtech.gm.db.*;
import com.anbtech.gm.business.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.util.*;
import java.io.*;
import com.anbtech.text.Hanguel;

public class GoodsInfoServlet extends HttpServlet {

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
		String searchword	= Hanguel.toHanguel(request.getParameter("searchword"));
		String searchscope	= request.getParameter("searchscope");
		String category		= request.getParameter("category");
		if (mode == null) mode = "list";
		if (no == null) no = "";
		if (page == null) page = "1";
		if (searchword == null) searchword = "";
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
		String redirectUrl = "";

		//데이터 파라미터들
		String umask		= request.getParameter("umask");
		String why_revision = request.getParameter("why_revision");	//파생사유(r:리비젼,d:파생)
		String one_class	= request.getParameter("one_class");	//제품군코드
		String two_class	= request.getParameter("two_class");	//제품코드
		String three_class	= request.getParameter("three_class");	//모델군코드
		String four_class	= request.getParameter("four_class");	//모델코드
		String item_code	= request.getParameter("item_code");	//항목코드

		if (one_class == null) one_class = "";
		if (two_class == null) two_class = "";
		if (three_class == null) three_class = "";
		if (four_class == null) four_class = "";


/*		//품목번호 검색창 구현시 추가된 파라미터들(by 종현)
		String fname		= request.getParameter("fname")==null?"":request.getParameter("fname");// form 이름
		String field		= request.getParameter("field");			// form field 이름(string)
		String one_name		= request.getParameter("one_name");			// 제품군명
		String two_name		= request.getParameter("two_name");			// 제품명
		String three_name	= request.getParameter("three_name");		// 모델군명
		String four_name	= request.getParameter("four_name");		// 모델명
		String fg_code		= request.getParameter("fg_code");			// fg_code field */
	
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			////////////////////////////////////////////////////////
			// 모델정보 추가 또는 수정 폼 만들기
			////////////////////////////////////////////////////////
			if(mode.equals("add_model") || mode.equals("mod_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.business.GoodsInfoBO goodsBO = new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.entity.GoodsInfoTable table = new com.anbtech.gm.entity.GoodsInfoTable();
			
				// 모델정보 가져오기
				table = goodsBO.getAddGoodsForm(mode,one_class,two_class,three_class,no);
				request.setAttribute("GoodsInfo",table);

				//선택된 제품 레벨에 정의된 스펙리스트 가져오기
				ArrayList spec_list = new ArrayList();
				if(!two_class.equals("")) spec_list = goodsBO.getSpecList(mode,two_class,no);
				request.setAttribute("SpecList",spec_list);

				com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
				com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
				redirect = redirectBO.getLinkUrlForInput(mode,searchword,searchscope,category,page,no);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/gm/add_model.jsp?mode=" + mode).forward(request,response);
			}

			////////////////////////////////////////////////////////
			// 모델파생 등록폼 만들기
			////////////////////////////////////////////////////////
			if(mode.equals("rev_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.business.GoodsInfoBO goodsBO = new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.entity.GoodsInfoTable table = new com.anbtech.gm.entity.GoodsInfoTable();
				com.anbtech.gm.business.makeGoodsTreeItems tree = new com.anbtech.gm.business.makeGoodsTreeItems(con);

				// 모델정보 가져오기
				table = goodsBO.getRevModelForm(mode,no,why_revision);
				table.setOneClass(tree.getGoodsClassStr(Integer.parseInt(no),""));
				request.setAttribute("GoodsInfo",table);

				//제품 레벨에 정의된 스펙리스트 가져오기
				ArrayList spec_list = new ArrayList();
				spec_list = goodsBO.getSpecList(mode,table.getTwoClass(),no);
				request.setAttribute("SpecList",spec_list);

				com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
				com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
				redirect = redirectBO.getLinkUrlForInput(mode,searchword,searchscope,category,page,no);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/gm/rev_model.jsp?mode=" + mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			//등록된 모델 리스트 출력
			/////////////////////////////////////////////////////
			else if(mode.equals("list_model")){
				com.anbtech.gm.business.GoodsInfoBO goodsBO = new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

				ArrayList model_list = new ArrayList();
				model_list = goodsDAO.getModelList(mode,searchword,searchscope,category,page);
				request.setAttribute("ModelList", model_list);

				com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
				com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
				redirect = redirectBO.getRedirect(mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);

				getServletContext().getRequestDispatcher("/gm/list_model.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			// 선택된 모델의 상세 정보 보기
			/////////////////////////////////////////////////////
			if(mode.equals("view_model")){
				com.anbtech.gm.business.GoodsInfoBO goodsBO		= new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO			= new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.entity.GoodsInfoTable table		= new com.anbtech.gm.entity.GoodsInfoTable();

				//모델 기본정보를 가져온다.
				table = goodsDAO.getGoodsInfoByMid(no);

				//선택된 항목이 모델이 아닐 경우에는 메시지 출력
				if(!table.getGoodsLevel().equals("4")){
//					PrintWriter out = response.getWriter();
//					out.println("모델명을 클릭하십시오.");
					redirectUrl = "GoodsInfoServlet?mode=list_model";
				}else{
					//선택된 모델의 제품분류 문자열을 가져온다.
					table.setOneClass(tree.getGoodsClassStr(Integer.parseInt(no),""));
					request.setAttribute("ModelInfo", table);

					//모델의 세부스펙정보를 가져온다.
					ArrayList spec_list = new ArrayList();
					spec_list = goodsBO.getSpecList(mode,two_class,no);
					request.setAttribute("SpecList",spec_list);

					com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
					com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
					redirect = redirectBO.getRedirect(mode,searchword,searchscope,category,page,no);
					request.setAttribute("Redirect",redirect);

					getServletContext().getRequestDispatcher("/gm/view_model.jsp").forward(request,response);
				}
			}

			////////////////////////////////////////////////////
			// 선택된 모델정보 삭제처리
			///////////////////////////////////////////////////
			else if(mode.equals("del_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO	= new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.entity.GoodsInfoTable table	= new com.anbtech.gm.entity.GoodsInfoTable();

				table = goodsDAO.getGoodsInfoByMid(no);
				String code = table.getGoodsCode();

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					goodsDAO.deleteModelCodeInfo(code);
					goodsDAO.deleteModelInfo(no);
					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//트리출력용 스크립트파일 생성
				com.anbtech.gm.business.makeGoodsTreeItems tree	 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree2 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/gm/admin/";
				tree.makeGoodsTree(output_path + "goods_tree_items.js","GoodsInfoServlet",1,0);
				tree2.makeGoodsTree(output_path + "goodsTreeItemsForModelSearch.js","NA",1,0);
			
				redirectUrl = "GoodsInfoServlet?mode=list_model";
			}


			/////////////////////////////////////////////////////
			//  제품 정보 가져오기 (모델명, 모델코드, F/Gcode)
			/////////////////////////////////////////////////////
			else if(mode.equals("search_model_info")){
				com.anbtech.gm.business.GoodsInfoBO goodsBO = new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);
				//String[] fieldArry = new String[9];

				ArrayList model_list = new ArrayList();
				model_list = goodsDAO.searchModelList(mode,searchword,searchscope,category,page);
				request.setAttribute("ModelList", model_list);
				//fieldArry = (String[])goodsBO.getFieldArry(one_class,one_name,two_class,two_name,three_class,three_name,four_class,four_name,fg_code);

				com.anbtech.gm.business.GmLinkUrlBO redirectBO = new com.anbtech.gm.business.GmLinkUrlBO(con);
				com.anbtech.gm.entity.GmLinkUrl redirect = new com.anbtech.gm.entity.GmLinkUrl();
				redirect = redirectBO.searchRedirect(mode,searchword,searchscope,category,page);
				request.setAttribute("Redirect",redirect);
				
				//request.setAttribute("FIELD",fieldArry);

				getServletContext().getRequestDispatcher("/gm/searchModelInfo.jsp?mode="+mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			// 제품별 표준템플릿 추가,수정폼
			/////////////////////////////////////////////////////
			else if(mode.equals("add_template") || mode.equals("upd_template")){
				com.anbtech.gm.business.GoodsInfoBO goodsBO		= new com.anbtech.gm.business.GoodsInfoBO(con);
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO			= new com.anbtech.gm.db.GoodsInfoDAO(con);
				com.anbtech.gm.entity.GoodsInfoItemTable table	= new com.anbtech.gm.entity.GoodsInfoItemTable();

				//선택된 항목코드에 대한 정보를 가져온다.
				table = goodsBO.getAddItemForm(mode,one_class,two_class,item_code);
				request.setAttribute("ItemInfo",table);

				//선택된 제품에 존재하는 항목 리스트를 가져온다.
				ArrayList item_list = new ArrayList();
				item_list = goodsBO.getItemList(two_class);
				request.setAttribute("ItemList", item_list);

				getServletContext().getRequestDispatcher("/gm/admin/template_mgr.jsp?mode=" + mode).forward(request,response);
			}

			/////////////////////////////////////////////////////
			// 제품트리 출력용 스크립트 파일 생성
			/////////////////////////////////////////////////////
			else if(mode.equals("make_tree")){
				com.anbtech.gm.business.makeGoodsTreeItems tree	 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree2 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/gm/admin/";
				
				try{
					tree.makeGoodsTree(output_path + "goods_tree_items.js","GoodsInfoServlet",1,0);
					tree2.makeGoodsTree(output_path + "goodsTreeItemsForModelSearch.js","NA",1,0);
				}catch (Exception e){
					PrintWriter out = response.getWriter();
					out.println("	<script>");
					out.println("	alert('제품분류가 완료되지 않아 트리생성을 할 수 없습니다.\n먼저 제품군-제품-모델군의 3단계 분류를 완료하십시오.');");
					out.println("	</script>");
					out.close();
				}

				getServletContext().getRequestDispatcher("/gm/admin/make_tree.jsp").forward(request,response);
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

		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/gm/";

		if (filepath == null) {
			throw new ServletException("Please supply uploadDir parameter");//경로없을시 에러출력
		}

		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(upload_size)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

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

		//전페이지에서 값 받아온다. multi에서 가져옴
		String mode			= multi.getParameter("mode");
		String page			= multi.getParameter("page");
		String searchword	= multi.getParameter("searchword");
		String searchscope	= multi.getParameter("searchscope");
		String category		= multi.getParameter("category");
		String no			= multi.getParameter("no");

		//공백으로 넘어오거나 null로 온 값들에 대한 처리.
		if (page == null) page = "";
		if (searchword == null) searchword = "";
		if (searchscope == null) searchscope = "";
		if (category == null)  category = "";

		//모델정보 등록관리 시 필요한 파라미터들
		String code			= multi.getParameter("code");		//모델코드
		String name			= multi.getParameter("name");		//모델명
		String name2		= multi.getParameter("name2");		//모델명2
		String short_name	= multi.getParameter("short_name");	//모델약어
		String color_code	= multi.getParameter("color_code");	//모델색상코드
		String other_info	= multi.getParameter("other_info");	//기타사항

		String code_str		= multi.getParameter("code_str");	//스펙코드리스트(1101001,1101002,1101003, ....식)
		String spec_str		= multi.getParameter("spec_str");	//상세스펙(코드|항목값|단위, ... 식)

		//모델파생등록시 추가되는 파라미터들
		String revision_code= multi.getParameter("revision_code");	//기능구분코드
		String derive_code	= multi.getParameter("derive_code");	//파생코드
		if(revision_code == null) revision_code = "1";
		if(derive_code == null) derive_code = "00";

		//표준 템플릿 등록관리 시 필요한 파라미터들
		String one_class	= multi.getParameter("one_class");	//제품군코드
		String two_class	= multi.getParameter("two_class");	//제품코드
		String three_class	= multi.getParameter("three_class");//모델군코드
		String item_code	= multi.getParameter("item_code");	//항목코드
		String item_name	= multi.getParameter("item_name");	//항목명
		String item_value	= multi.getParameter("item_value");	//항목값
		String item_unit	= multi.getParameter("item_unit");	//항목단위
		String write_exam	= multi.getParameter("write_exam");	//작성예
		String item_desc	= multi.getParameter("item_desc");	//항목설명

		try {
			// con생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			/////////////////////////////////////////////////////
			// 신규 모델정보 or 파생모델 등록처리
			/////////////////////////////////////////////////////
			if (mode.equals("add_model") || mode.equals("rev_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

				//모델코드 중복확인
				int same_model_count = goodsDAO.getCountWithSameModelCode(code);
				if(same_model_count > 0){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('동일한 모델코드가 이미 등록되어 있어 등록작업을 계속할 수 없습니다.');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}
				
				//모델규격 중복확인
				String same_model_code = goodsDAO.getModelCodeWithSameProperty(code_str,spec_str);
				if(same_model_code.length() > 1){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('동일한 모델규격을 가지는 모델이 등록되어 있어 작업을 계속할 수 없습니다.\\n모델코드:" + same_model_code + "');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//db저장
					goodsDAO.saveModelCodeInfo(code,revision_code,derive_code);
					goodsDAO.saveModelInfo(three_class,code,name,name2,short_name,color_code,other_info,code_str,spec_str,login_id);
					con.commit(); // commit한다.
				}catch(Exception e){
					con.rollback();
					//에러출력 페이지로 분기
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}

				//트리출력용 스크립트파일 생성
				com.anbtech.gm.business.makeGoodsTreeItems tree	 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree2 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/gm/admin/";
				tree.makeGoodsTree(output_path + "goods_tree_items.js","GoodsInfoServlet",1,0);
				tree2.makeGoodsTree(output_path + "goodsTreeItemsForModelSearch.js","NA",1,0);

				redirectUrl = "GoodsInfoServlet?mode=list_model";
			}

			/////////////////////////////////////////////////////
			// 모델정보 수정처리
			/////////////////////////////////////////////////////
			else if (mode.equals("mod_model")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

/*
				//모델규격 중복확인
				String same_model_code = goodsDAO.getModelCodeWithSameProperty(code_str,spec_str);
				if(same_model_code.length() > 1){
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('동일한 모델규격을 가지는 모델이 등록되어 있어 작업을 계속할 수 없습니다.\\n모델코드:" + same_model_code + "');");
					out.println("history.go(-1);");
					out.println("</script>");
					out.close();
					return;								
				}
*/
				goodsDAO.modifyModelInfo(no,three_class,code,name,name2,short_name,color_code,other_info,code_str,spec_str,login_id);

				//트리출력용 스크립트파일 생성
				com.anbtech.gm.business.makeGoodsTreeItems tree	 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				com.anbtech.gm.business.makeGoodsTreeItems tree2 = new com.anbtech.gm.business.makeGoodsTreeItems(con);
				String output_path = com.anbtech.admin.db.ServerConfig.getConf("context_path") + "/gm/admin/";
				tree.makeGoodsTree(output_path + "goods_tree_items.js","GoodsInfoServlet",1,0);
				tree2.makeGoodsTree(output_path + "goodsTreeItemsForModelSearch.js","NA",1,0);

				redirectUrl = "GoodsInfoServlet?mode=view_model&page="+page+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no;
			}

			/////////////////////////////////////////////////////
			// 제품에 대한 표준템플릿 추가 또는 수정처리
			/////////////////////////////////////////////////////
			else if (mode.equals("add_template") || mode.equals("upd_template")){
				com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

				if(mode.equals("add_template")) goodsDAO.saveItemInfo(item_code,item_name,item_value,item_unit,write_exam,item_desc);
				else goodsDAO.updItemInfo(item_code,item_name,item_value,item_unit,write_exam,item_desc);

				redirectUrl = "GoodsInfoServlet?mode=add_template&one_class=" + one_class + "&two_class=" + two_class;
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
