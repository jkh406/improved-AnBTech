import com.anbtech.bm.db.*;
import com.anbtech.bm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class BomShowServlet extends HttpServlet {
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"MBS_S":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pid":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
	
		//MBOM MASTER구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String app_date = Hanguel.toHanguel(request.getParameter("app_date"))==null?"":Hanguel.toHanguel(request.getParameter("app_date"));
		String status = Hanguel.toHanguel(request.getParameter("status"))==null?"":Hanguel.toHanguel(request.getParameter("status"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String app_flag = Hanguel.toHanguel(request.getParameter("app_flag"))==null?"":Hanguel.toHanguel(request.getParameter("app_flag"));

		//MBOM STR구성을 위한 기본파라미터
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String parent_code = Hanguel.toHanguel(request.getParameter("parent_code"))==null?"0":Hanguel.toHanguel(request.getParameter("parent_code"));
		String child_code = Hanguel.toHanguel(request.getParameter("child_code"))==null?"":Hanguel.toHanguel(request.getParameter("child_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"0":Hanguel.toHanguel(request.getParameter("level_no"));
		String part_name = Hanguel.toHanguel(request.getParameter("part_name"))==null?"":Hanguel.toHanguel(request.getParameter("part_name"));
		String part_spec = Hanguel.toHanguel(request.getParameter("part_spec"))==null?"":Hanguel.toHanguel(request.getParameter("part_spec"));
		String location = Hanguel.toHanguel(request.getParameter("location"))==null?"":Hanguel.toHanguel(request.getParameter("location"));
		String op_code = Hanguel.toHanguel(request.getParameter("op_code"))==null?"":Hanguel.toHanguel(request.getParameter("op_code"));
		String qty_unit = Hanguel.toHanguel(request.getParameter("qty_unit"))==null?"":Hanguel.toHanguel(request.getParameter("qty_unit"));
		String qty = Hanguel.toHanguel(request.getParameter("qty"))==null?"":Hanguel.toHanguel(request.getParameter("qty"));
		String price_unit = Hanguel.toHanguel(request.getParameter("price_unit"))==null?"":Hanguel.toHanguel(request.getParameter("price_unit"));
		String price = Hanguel.toHanguel(request.getParameter("price"))==null?"":Hanguel.toHanguel(request.getParameter("price"));
		String adtag = Hanguel.toHanguel(request.getParameter("adtag"))==null?"":Hanguel.toHanguel(request.getParameter("adtag"));
		String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		String url = Hanguel.toHanguel(request.getParameter("url"))==null?"":Hanguel.toHanguel(request.getParameter("url"));
		String sel_date = Hanguel.toHanguel(request.getParameter("sel_date"))==null?"":Hanguel.toHanguel(request.getParameter("sel_date"));
		String step = Hanguel.toHanguel(request.getParameter("step"))==null?"M":Hanguel.toHanguel(request.getParameter("step"));
		String bundle = Hanguel.toHanguel(request.getParameter("bundle"))==null?"8":Hanguel.toHanguel(request.getParameter("bundle"));

		//날자에서 '/'제거하기
		com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();
		sel_date = prs.repWord(sel_date,"/","");
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			
			//------------------------------------------------------------
			//		MBOM TREE 구조체계 정보 조회하기
			//------------------------------------------------------------
			//정전개 FG/ASSY 검색하기
			if ("frd_fgsearch".equals(mode)){
				com.anbtech.bm.db.BomShowDAO showDAO = new com.anbtech.bm.db.BomShowDAO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//FG LIST 조회
				ArrayList fg_list = new ArrayList();
				fg_list = showDAO.getBomFGList(sItem,sWord);
				request.setAttribute("FG_List", fg_list); 

				//Assy List조회
				ArrayList assy_list = new ArrayList();
				assy_list = modDAO.getAssyListCP(gid);
				request.setAttribute("ASSY_List", assy_list); 

				//리턴하기
				request.setAttribute("sel_date",sel_date);
				request.setAttribute("sWord",sWord);
				request.setAttribute("gid",gid);
				request.setAttribute("parent_code",parent_code);
				request.setAttribute("step",step);
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomFrdFgSearch.jsp").forward(request,response);
			}
			//정전개 TREE 구조체계 보기 : TEXT
			else if ("frd_text".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				
				//결과LIST
				if(step.equals("M")) {		//다단계
					ArrayList part_list = new ArrayList();
					part_list = showBO.viewStrList(gid,level_no,parent_code,sel_date);
					request.setAttribute("PART_List", part_list); 
				} else {					//단단계
					ArrayList part_list = new ArrayList();
					part_list = showBO.viewSingleStrList(gid,level_no,parent_code,sel_date);
					request.setAttribute("PART_List", part_list); 
				}

				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(gid.length() != 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);
			
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomFrdText.jsp").forward(request,response);
	
			}
			//정전개 TREE 구조체계 보기
			else if ("frd_tree".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				
				//결과LIST
				String TREE = showBO.makeFrdTree(gid,level_no,parent_code,sel_date,url);
				String cnt = showBO.getArrayCount(); 
				request.setAttribute("TREE",TREE);
				request.setAttribute("COUNT",cnt);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomFrdTree.jsp").forward(request,response);
			}
			//역전개 부품/ASSY 검색하기
			else if ("rev_fgsearch".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//FG LIST 조회
				ArrayList component_list = new ArrayList();
				component_list = modDAO.getComponentCode(sWord);
				request.setAttribute("COMPONENT_List", component_list); 

				//리턴하기
				request.setAttribute("sel_date",sel_date);
				request.setAttribute("sWord",sWord);
				request.setAttribute("parent_code",parent_code);
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomRevFgSearch.jsp").forward(request,response);
			}
			//역전개 TREE 구조체계 보기 : TEXT
			else if ("rev_text".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				
				//결과LIST
				ArrayList part_list = new ArrayList();
				if(child_code.length() != 0) part_list = showBO.viewRevStrList(child_code,sel_date);
				request.setAttribute("PART_List", part_list); 

				//품목의 규격정보구하기
				if(child_code.length() != 0) part_spec = modDAO.getComponentSpec(child_code);

				//리턴하기
				request.setAttribute("child_code",child_code);
				request.setAttribute("part_spec",part_spec);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomRevText.jsp").forward(request,response);
			}
			//역전개 TREE 구조체계 보기
			else if ("rev_tree".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				
				//결과LIST
				String TREE = showBO.makeRevTree(child_code,sel_date,url);
				String cnt = showBO.getArrayCount(); 
				request.setAttribute("TREE",TREE);
				request.setAttribute("COUNT",cnt);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomRevTree.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		MBOM TREE 구조체계 리포트로 출력하기
			//------------------------------------------------------------
			//정전개 출력하기 : Location no 8개씩 구분하여
			else if ("report_n".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				
				//리포트 리스트
				ArrayList item_list = new ArrayList();
				item_list = showBO.sortArrayStrList(gid,level_no,parent_code,sel_date,bundle);
				request.setAttribute("ITEM_List", item_list); 

				//리턴하기
				String where = "where pid='"+gid+"'";
				model_code = modDAO.getColumData("mbom_master","model_code",where);
				fg_code = modDAO.getColumData("mbom_master","fg_code",where);
			
				request.setAttribute("model_code",model_code);
				request.setAttribute("fg_code",fg_code);
				
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/reportBomBundle.jsp").forward(request,response);
			}
			//정전개 출력하기 : Location no 8개씩 구분하여
			else if ("report_excel".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				
				//리포트 리스트
				ArrayList item_list = new ArrayList();
				item_list = showBO.sortArrayStrList(gid,level_no,parent_code,sel_date,bundle);
				request.setAttribute("ITEM_List", item_list); 

				//리턴하기
				String where = "where pid='"+gid+"'";
				model_code = modDAO.getColumData("mbom_master","model_code",where);
				fg_code = modDAO.getColumData("mbom_master","fg_code",where);
			
				request.setAttribute("model_code",model_code);
				request.setAttribute("fg_code",fg_code);
				
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/reportBomExcel.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		MBOM 원가산출 정보 조회하기
			//------------------------------------------------------------
			//BOM원가산출 FG/ASSY 검색하기
			else if ("cost_fgsearch".equals(mode)){
				com.anbtech.bm.db.BomShowDAO showDAO = new com.anbtech.bm.db.BomShowDAO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//FG LIST 조회
				ArrayList fg_list = new ArrayList();
				fg_list = showDAO.getFGList(sItem,sWord);
				request.setAttribute("FG_List", fg_list); 

				//Assy List조회
				ArrayList assy_list = new ArrayList();
				assy_list = modDAO.getAssyListCP(gid);
				request.setAttribute("ASSY_List", assy_list); 

				//리턴하기
				request.setAttribute("sel_date",sel_date);
				request.setAttribute("sWord",sWord);
				request.setAttribute("gid",gid);
				request.setAttribute("parent_code",parent_code);
				request.setAttribute("step",step);
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/cost/bomCostFgSearch.jsp").forward(request,response);
			}
			//BOM원가산출 산출내용 보기
			else if ("cost_list".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				
				//결과LIST
				String where = "where pid='"+gid+"'";
				String bom_status = modDAO.getColumData("mbom_master","bom_status",where);
				if(step.equals("M")) {		//다단계
					ArrayList part_list = new ArrayList();
					if(bom_status.equals("5")) {	//확정BOM
						part_list = showBO.getUniqueMultiLevelBom(gid,level_no,parent_code,sel_date);
					} else {						//그외BOM
						part_list = inputBO.getUniqueMultiLevelBom(gid,level_no,parent_code);
					}
					request.setAttribute("PART_List", part_list); 
				} else {					//단단계
					ArrayList part_list = new ArrayList();
					if(bom_status.equals("5")) {	//확정BOM
						part_list = showBO.getUniqueSingleLevelBom(gid,level_no,parent_code,sel_date);
					} else {						//그외BOM
						part_list = inputBO.getUniqueSingleLevelBom(gid,level_no,parent_code);
					}
					request.setAttribute("PART_List", part_list); 
				}
				
				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/cost/bomCostList.jsp").forward(request,response);		
			}
			//BOM원가산출 Excel출력
			else if ("cost_excel".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				
				//결과LIST
				String where = "where pid='"+gid+"'";
				String bom_status = modDAO.getColumData("mbom_master","bom_status",where);
				if(step.equals("M")) {		//다단계
					ArrayList part_list = new ArrayList();
					if(bom_status.equals("5")) {	//확정BOM
						part_list = showBO.getUniqueMultiLevelBom(gid,level_no,parent_code,sel_date);
					} else {						//그외BOM
						part_list = inputBO.getUniqueMultiLevelBom(gid,level_no,parent_code);
					}
					request.setAttribute("PART_List", part_list); 
				} else {					//단단계
					ArrayList part_list = new ArrayList();
					if(bom_status.equals("5")) {	//확정BOM
						part_list = showBO.getUniqueSingleLevelBom(gid,level_no,parent_code,sel_date);
					} else {						//그외BOM
						part_list = inputBO.getUniqueSingleLevelBom(gid,level_no,parent_code);
					}
					request.setAttribute("PART_List", part_list); 
				}
				
				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/cost/bomCostExcel.jsp").forward(request,response);		
			}
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"MBS_S":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"pid":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
	
		//MBOM MASTER구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		String model_name = Hanguel.toHanguel(request.getParameter("model_name"))==null?"":Hanguel.toHanguel(request.getParameter("model_name"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String pjt_code = Hanguel.toHanguel(request.getParameter("pjt_code"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_code"));
		String pjt_name = Hanguel.toHanguel(request.getParameter("pjt_name"))==null?"":Hanguel.toHanguel(request.getParameter("pjt_name"));
		String reg_date = Hanguel.toHanguel(request.getParameter("reg_date"))==null?"":Hanguel.toHanguel(request.getParameter("reg_date"));
		String app_date = Hanguel.toHanguel(request.getParameter("app_date"))==null?"":Hanguel.toHanguel(request.getParameter("app_date"));
		String status = Hanguel.toHanguel(request.getParameter("status"))==null?"":Hanguel.toHanguel(request.getParameter("status"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String app_flag = Hanguel.toHanguel(request.getParameter("app_flag"))==null?"":Hanguel.toHanguel(request.getParameter("app_flag"));

		//MBOM STR구성을 위한 기본파라미터
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String parent_code = Hanguel.toHanguel(request.getParameter("parent_code"))==null?"0":Hanguel.toHanguel(request.getParameter("parent_code"));
		String child_code = Hanguel.toHanguel(request.getParameter("child_code"))==null?"":Hanguel.toHanguel(request.getParameter("child_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"0":Hanguel.toHanguel(request.getParameter("level_no"));
		String part_name = Hanguel.toHanguel(request.getParameter("part_name"))==null?"":Hanguel.toHanguel(request.getParameter("part_name"));
		String part_spec = Hanguel.toHanguel(request.getParameter("part_spec"))==null?"":Hanguel.toHanguel(request.getParameter("part_spec"));
		String location = Hanguel.toHanguel(request.getParameter("location"))==null?"":Hanguel.toHanguel(request.getParameter("location"));
		String op_code = Hanguel.toHanguel(request.getParameter("op_code"))==null?"":Hanguel.toHanguel(request.getParameter("op_code"));
		String qty_unit = Hanguel.toHanguel(request.getParameter("qty_unit"))==null?"":Hanguel.toHanguel(request.getParameter("qty_unit"));
		String qty = Hanguel.toHanguel(request.getParameter("qty"))==null?"":Hanguel.toHanguel(request.getParameter("qty"));
		String price_unit = Hanguel.toHanguel(request.getParameter("price_unit"))==null?"":Hanguel.toHanguel(request.getParameter("price_unit"));
		String price = Hanguel.toHanguel(request.getParameter("price"))==null?"":Hanguel.toHanguel(request.getParameter("price"));
		String adtag = Hanguel.toHanguel(request.getParameter("adtag"))==null?"":Hanguel.toHanguel(request.getParameter("adtag"));
		String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		String url = Hanguel.toHanguel(request.getParameter("url"))==null?"":Hanguel.toHanguel(request.getParameter("url"));
		String sel_date = Hanguel.toHanguel(request.getParameter("sel_date"))==null?"":Hanguel.toHanguel(request.getParameter("sel_date"));
		String step = Hanguel.toHanguel(request.getParameter("step"))==null?"M":Hanguel.toHanguel(request.getParameter("step"));
		
		//날자에서 '/'제거하기
		com.anbtech.text.StringProcess prs = new com.anbtech.text.StringProcess();
		sel_date = prs.repWord(sel_date,"/","");

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//------------------------------------------------------------
			//		MBOM TREE 구조체계 정보 조회하기
			//------------------------------------------------------------
			//정전개 FG/ASSY 검색하기
			if ("frd_fgsearch".equals(mode)){
				com.anbtech.bm.db.BomShowDAO showDAO = new com.anbtech.bm.db.BomShowDAO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//FG LIST 조회
				ArrayList fg_list = new ArrayList();
				fg_list = showDAO.getBomFGList(sItem,sWord);
				request.setAttribute("FG_List", fg_list); 

				//첫번째 값 gid 구하기
				if(gid.length() == 0) {
					com.anbtech.bm.entity.mbomMasterTable fg = new com.anbtech.bm.entity.mbomMasterTable();
					Iterator fg_iter = fg_list.iterator();
					if(fg_iter.hasNext()) {
						fg = (com.anbtech.bm.entity.mbomMasterTable)fg_iter.next(); 
						gid = fg.getPid();
					}
				}

				//Assy List조회
				ArrayList assy_list = new ArrayList();
				assy_list = modDAO.getAssyListCP(gid);
				request.setAttribute("ASSY_List", assy_list); 

				//리턴하기
				request.setAttribute("sel_date",sel_date);
				request.setAttribute("sWord",sWord);
				request.setAttribute("gid",gid);
				request.setAttribute("parent_code",parent_code);
				request.setAttribute("step",step);
				
				//분기하기
				String para = "&gid="+gid+"&level_no="+level_no+"&parent_code="+parent_code+"&sel_date="+sel_date;
					  para += "&url=../servlet/BomShowServlet&sWord="+sWord+"&sItem="+sItem+"&step="+step;

				out.println("	<script>");
				out.println("	location.href('BomShowServlet?mode=frd_fgsearch"+para+"');");
				out.println("	</script>");
			
			}
			//정전개 TREE 구조체계 보기 : TEXT
			else if ("frd_text".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				
				//결과LIST
				if(step.equals("M")) {		//다단계
					ArrayList part_list = new ArrayList();
					part_list = showBO.viewStrList(gid,level_no,parent_code,sel_date);
					request.setAttribute("PART_List", part_list); 
				} else {					//단단계
					ArrayList part_list = new ArrayList();
					part_list = showBO.viewSingleStrList(gid,level_no,parent_code,sel_date);
					request.setAttribute("PART_List", part_list); 
				}
				
				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomFrdText.jsp").forward(request,response);		
			}
			//정전개 TREE 구조체계 보기
			else if ("frd_tree".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				
				//결과LIST
				String TREE = showBO.makeFrdTree(gid,level_no,parent_code,sel_date,url);
				String cnt = showBO.getArrayCount(); 
				request.setAttribute("TREE",TREE);
				request.setAttribute("COUNT",cnt);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomFrdTree.jsp").forward(request,response);
			}
			//역전개 부품/ASSY 검색하기
			else if ("rev_fgsearch".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//품목 LIST 조회
				ArrayList component_list = new ArrayList();
				component_list = modDAO.getComponentCode(sWord);
				request.setAttribute("COMPONENT_List", component_list); 

				//리턴하기
				request.setAttribute("sel_date",sel_date);
				request.setAttribute("sWord",sWord);
				request.setAttribute("parent_code",parent_code);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomRevFgSearch.jsp").forward(request,response);
			}
			//역전개 TREE 구조체계 보기 : TEXT
			else if ("rev_text".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				
				//결과LIST
				ArrayList part_list = new ArrayList();
				part_list = showBO.viewRevStrList(child_code,sel_date);
				request.setAttribute("PART_List", part_list); 

				//품목의 규격정보구하기
				part_spec = modDAO.getComponentSpec(child_code);

				//리턴하기
				request.setAttribute("child_code",child_code);
				request.setAttribute("part_spec",part_spec);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomRevText.jsp").forward(request,response);
			}
			//역전개 TREE 구조체계 보기
			else if ("rev_tree".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				
				//결과LIST
				String TREE = showBO.makeRevTree(child_code,sel_date,url);
				String cnt = showBO.getArrayCount(); 
				request.setAttribute("TREE",TREE);
				request.setAttribute("COUNT",cnt);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/bomRevTree.jsp").forward(request,response);
			}

			//------------------------------------------------------------
			//		MBOM 원가산출 정보 조회하기
			//------------------------------------------------------------
			//BOM원가산출 FG/ASSY 검색하기
			else if ("cost_fgsearch".equals(mode)){
				com.anbtech.bm.db.BomShowDAO showDAO = new com.anbtech.bm.db.BomShowDAO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//FG LIST 조회
				ArrayList fg_list = new ArrayList();
				fg_list = showDAO.getFGList(sItem,sWord);
				request.setAttribute("FG_List", fg_list); 

				//첫번째 값 gid 구하기
				if(gid.length() == 0) {
					com.anbtech.bm.entity.mbomMasterTable fg = new com.anbtech.bm.entity.mbomMasterTable();
					Iterator fg_iter = fg_list.iterator();
					if(fg_iter.hasNext()) {
						fg = (com.anbtech.bm.entity.mbomMasterTable)fg_iter.next(); 
						gid = fg.getPid();
					}
				}

				//Assy List조회
				ArrayList assy_list = new ArrayList();
				assy_list = modDAO.getAssyListCP(gid);
				request.setAttribute("ASSY_List", assy_list); 

				//리턴하기
				request.setAttribute("sel_date",sel_date);
				request.setAttribute("sWord",sWord);
				request.setAttribute("gid",gid);
				request.setAttribute("parent_code",parent_code);
				request.setAttribute("step",step);
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/cost/bomCostFgSearch.jsp").forward(request,response);
			}
			//BOM원가산출 산출내용 보기
			else if ("cost_list".equals(mode)){
				com.anbtech.bm.business.BomShowBO showBO = new com.anbtech.bm.business.BomShowBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				
				//결과LIST
				String where = "where pid='"+gid+"'";
				String bom_status = modDAO.getColumData("mbom_master","bom_status",where);
				if(step.equals("M")) {		//다단계
					ArrayList part_list = new ArrayList();
					if(bom_status.equals("5")) {	//확정BOM
						part_list = showBO.getUniqueMultiLevelBom(gid,level_no,parent_code,sel_date);
					} else {						//그외BOM
						part_list = inputBO.getUniqueMultiLevelBom(gid,level_no,parent_code);
					}
					request.setAttribute("PART_List", part_list); 
				} else {					//단단계
					ArrayList part_list = new ArrayList();
					if(bom_status.equals("5")) {	//확정BOM
						part_list = showBO.getUniqueSingleLevelBom(gid,level_no,parent_code,sel_date);
					} else {						//그외BOM
						part_list = inputBO.getUniqueSingleLevelBom(gid,level_no,parent_code);
					}
					request.setAttribute("PART_List", part_list); 
				}
				
				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/cost/bomCostList.jsp").forward(request,response);		
			}
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}
