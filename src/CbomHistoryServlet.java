import com.anbtech.dcm.db.*;
import com.anbtech.dcm.business.*;
import com.anbtech.text.Hanguel;
import com.oreilly.servlet.MultipartRequest;
import javax.servlet.*;
import javax.servlet.http.*;
import com.anbtech.dbconn.DBConnectionManager;
import java.sql.Connection;
import java.util.*;
import java.io.*;

public class CbomHistoryServlet extends HttpServlet {
	private DBConnectionManager connMgr;
	private Connection con;
	private int max_display_cnt = 6;
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"ecc_premodify":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"ecc_subject":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//ECC_COM구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String ecc_subject = Hanguel.toHanguel(request.getParameter("ecc_subject"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_subject"));
		String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
		String ecr_id = Hanguel.toHanguel(request.getParameter("ecr_id"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_id"));
		String ecr_name = Hanguel.toHanguel(request.getParameter("ecr_name"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_name"));
		String ecr_div_code = Hanguel.toHanguel(request.getParameter("ecr_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_div_code"));
		String ecr_div_name = Hanguel.toHanguel(request.getParameter("ecr_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_div_name"));
		String ecr_tel = Hanguel.toHanguel(request.getParameter("ecr_tel"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_tel"));
		String ecr_date = Hanguel.toHanguel(request.getParameter("ecr_date"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_date"));
		String mgr_id = Hanguel.toHanguel(request.getParameter("mgr_id"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_id"));
		String mgr_name = Hanguel.toHanguel(request.getParameter("mgr_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_name"));
		String mgr_code = Hanguel.toHanguel(request.getParameter("mgr_code"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_code"));
		String mgr_div_code = Hanguel.toHanguel(request.getParameter("mgr_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_div_code"));
		String mgr_div_name = Hanguel.toHanguel(request.getParameter("mgr_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_div_name"));
		String eco_id = Hanguel.toHanguel(request.getParameter("eco_id"))==null?"":Hanguel.toHanguel(request.getParameter("eco_id"));
		String eco_name = Hanguel.toHanguel(request.getParameter("eco_name"))==null?"":Hanguel.toHanguel(request.getParameter("eco_name"));
		String eco_code = Hanguel.toHanguel(request.getParameter("eco_code"))==null?"":Hanguel.toHanguel(request.getParameter("eco_code"));
		String eco_div_code = Hanguel.toHanguel(request.getParameter("eco_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("eco_div_code"));
		String eco_div_name = Hanguel.toHanguel(request.getParameter("eco_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("eco_div_name"));
		String eco_tel = Hanguel.toHanguel(request.getParameter("eco_tel"))==null?"":Hanguel.toHanguel(request.getParameter("eco_tel"));
		String ecc_reason = Hanguel.toHanguel(request.getParameter("ecc_reason"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_reason"));
		String ecc_factor = Hanguel.toHanguel(request.getParameter("ecc_factor"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_factor"));
		String ecc_scope = Hanguel.toHanguel(request.getParameter("ecc_scope"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_scope"));
		String ecc_kind = Hanguel.toHanguel(request.getParameter("ecc_kind"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_kind"));
		String pdg_code = Hanguel.toHanguel(request.getParameter("pdg_code"))==null?"":Hanguel.toHanguel(request.getParameter("pdg_code"));
		String pd_code = Hanguel.toHanguel(request.getParameter("pd_code"))==null?"":Hanguel.toHanguel(request.getParameter("pd_code"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String part_code = Hanguel.toHanguel(request.getParameter("part_code"))==null?"":Hanguel.toHanguel(request.getParameter("part_code"));
		String order_date = Hanguel.toHanguel(request.getParameter("order_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_date"));
		String fix_date = Hanguel.toHanguel(request.getParameter("fix_date"))==null?"":Hanguel.toHanguel(request.getParameter("fix_date"));
		String ecc_status = Hanguel.toHanguel(request.getParameter("ecc_status"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_status"));
		
		//ECC_REQ/ECC_ORD구성을 위한 기본파라미터
		String chg_position = Hanguel.toHanguel(request.getParameter("chg_position"))==null?"":Hanguel.toHanguel(request.getParameter("chg_position"));
		String trouble = Hanguel.toHanguel(request.getParameter("trouble"))==null?"":Hanguel.toHanguel(request.getParameter("trouble"));
		String condition = Hanguel.toHanguel(request.getParameter("condition"))==null?"":Hanguel.toHanguel(request.getParameter("condition"));
		String solution = Hanguel.toHanguel(request.getParameter("solution"))==null?"":Hanguel.toHanguel(request.getParameter("solution"));
		String fname = Hanguel.toHanguel(request.getParameter("fname"))==null?"":Hanguel.toHanguel(request.getParameter("fname"));
		String sname = Hanguel.toHanguel(request.getParameter("sname"))==null?"":Hanguel.toHanguel(request.getParameter("sname"));
		String ftype = Hanguel.toHanguel(request.getParameter("ftype"))==null?"":Hanguel.toHanguel(request.getParameter("ftype"));
		String fsize = Hanguel.toHanguel(request.getParameter("fsize"))==null?"":Hanguel.toHanguel(request.getParameter("fsize"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		//기간관련 기본파라미터
		String ecr_s_date = Hanguel.toHanguel(request.getParameter("ecr_s_date"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_s_date"));
		String ecr_e_date = Hanguel.toHanguel(request.getParameter("ecr_e_date"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_e_date"));
		String eco_s_date = Hanguel.toHanguel(request.getParameter("eco_s_date"))==null?"":Hanguel.toHanguel(request.getParameter("eco_s_date"));
		String eco_e_date = Hanguel.toHanguel(request.getParameter("eco_e_date"))==null?"":Hanguel.toHanguel(request.getParameter("eco_e_date"));
		String e_fg_code = Hanguel.toHanguel(request.getParameter("e_fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("e_fg_code"));
		String a_fg_code = Hanguel.toHanguel(request.getParameter("a_fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("a_fg_code"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		검색리스트
			//------------------------------------------------------------
			//기본검색 LIST
			if ("sch_base".equals(mode)){
				com.anbtech.dcm.db.CbomHistoryDAO histDAO = new com.anbtech.dcm.db.CbomHistoryDAO(con);

				//설계변경 기본정보
				ArrayList ecc_list = new ArrayList();
				ecc_list = histDAO.getBaseEccComList(ecc_subject,eco_no,ecr_s_date,ecr_e_date,ecr_name,eco_s_date,eco_e_date,eco_name,ecc_status,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//페이지로 바로가기 List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = histDAO.getBaseDisplayPage(ecc_subject,eco_no,ecr_s_date,ecr_e_date,ecr_name,eco_s_date,eco_e_date,eco_name,ecc_status,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);
			
				//파라미터
				String para = "&ecc_subject="+ecc_subject+"&eco_no="+eco_no+"&ecr_s_date="+ecr_s_date+"&ecr_e_date=";
				para += ecr_e_date+"&ecr_name="+ecr_name+"&eco_s_date="+eco_s_date+"&eco_e_date="+eco_e_date;
				para += "&eco_name="+eco_name+"&ecc_status="+ecc_status;

				//분기하기
				getServletContext().getRequestDispatcher("/dcm/search/searchResult.jsp?mode=sch_base"+para).forward(request,response);
			}
			//선택조건 LIST
			else if ("sch_condition".equals(mode)){
				com.anbtech.dcm.db.CbomHistoryDAO histDAO = new com.anbtech.dcm.db.CbomHistoryDAO(con);

				//설계변경 기본정보
				ArrayList ecc_list = new ArrayList();
				ecc_list = histDAO.getConditionEccComList(pdg_code,pd_code,e_fg_code,a_fg_code,part_code,item_code,ecc_reason,ecc_factor,ecc_scope,ecc_kind,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//페이지로 바로가기 List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = histDAO.getConditionDisplayPage(pdg_code,pd_code,e_fg_code,a_fg_code,part_code,item_code,ecc_reason,ecc_factor,ecc_scope,ecc_kind,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//파라미터
				String para = "&pdg_code="+pdg_code+"&pd_code="+pd_code+"&e_fg_code="+e_fg_code+"&a_fg_code=";
				para += a_fg_code+"&ecc_reason="+ecc_reason+"&ecc_factor="+ecc_factor+"&ecc_scope="+ecc_scope;
				para += "&ecc_kind="+ecc_kind+"&part_code="+part_code+"&item_code="+item_code;

				//분기하기
				getServletContext().getRequestDispatcher("/dcm/search/searchResult.jsp?mode=sch_condition&"+para).forward(request,response);
			}
			//내용검색 LIST
			else if ("sch_content".equals(mode)){
				com.anbtech.dcm.db.CbomHistoryDAO histDAO = new com.anbtech.dcm.db.CbomHistoryDAO(con);

				//설계변경 기본정보
				ArrayList ecc_list = new ArrayList();
				ecc_list = histDAO.getContentEccComList(condition,solution,chg_position,trouble,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//페이지로 바로가기 List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = histDAO.getContentDisplayPage(condition,solution,chg_position,trouble,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);
					
				//파라미터
				String para = "&condition="+condition+"&solution="+solution+"&chg_position=";
				para += chg_position+"&trouble="+trouble;

				//분기하기
				getServletContext().getRequestDispatcher("/dcm/search/searchResult.jsp?mode=sch_content&"+para).forward(request,response);
			}
			//------------------------------------------------------------
			//		내용보기
			//------------------------------------------------------------
			//해당내용 보기 
			else if ("sch_view".equals(mode)){
				com.anbtech.dcm.db.CbomHistoryDAO histDAO = new com.anbtech.dcm.db.CbomHistoryDAO(con);
				com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);

				//ECC_COM 수정할 데이터 읽기
				com.anbtech.dcm.entity.eccComTable eccT = new com.anbtech.dcm.entity.eccComTable();
				eccT = histDAO.readEccCom(pid);
				request.setAttribute("COM_List", eccT);

				//ECC_MODEL 데이터 읽기
				String where = "where pid = '"+pid+"'";
				eco_no = histDAO.getColumData("ECC_COM","eco_no",where);
				ArrayList eccM = new ArrayList();
				eccM = histDAO.getEcoModel(eco_no);
				request.setAttribute("MODEL_List", eccM);

				//ECC_REQ 수정할 데이터 읽기
				com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
				ecrT = histDAO.readEccReq(pid);
				request.setAttribute("REQ_List", ecrT);

				//ECC_ORD 수정할 데이터 읽기
				com.anbtech.dcm.entity.eccOrdTable ecoT = new com.anbtech.dcm.entity.eccOrdTable();
				ecoT = histDAO.readEccOrd(pid);
				request.setAttribute("ORD_List", ecoT);

				//gid 
				where = "where eco_no = '"+eco_no+"'";
				String fg_code_list = cmodDAO.getColumData("ecc_com","fg_code",where);
				StringTokenizer list = new StringTokenizer(fg_code_list,"\n");
				if(list.hasMoreTokens()) fg_code = list.nextToken();
			
				where = "where fg_code = '"+fg_code+"'";
				String gid = cmodDAO.getColumData("mbom_master","pid",where);
				request.setAttribute("gid",gid);

				//분기하기
				getServletContext().getRequestDispatcher("/dcm/search/searchView.jsp").forward(request,response);
				
			}
		
		}catch (Exception e){
			//에러출력 페이지로 분기
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"ecc_premodify":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"ecc_subject":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//ECC_COM구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String ecc_subject = Hanguel.toHanguel(request.getParameter("ecc_subject"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_subject"));
		String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
		String ecr_id = Hanguel.toHanguel(request.getParameter("ecr_id"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_id"));
		String ecr_name = Hanguel.toHanguel(request.getParameter("ecr_name"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_name"));
		String ecr_div_code = Hanguel.toHanguel(request.getParameter("ecr_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_div_code"));
		String ecr_div_name = Hanguel.toHanguel(request.getParameter("ecr_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_div_name"));
		String ecr_tel = Hanguel.toHanguel(request.getParameter("ecr_tel"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_tel"));
		String ecr_date = Hanguel.toHanguel(request.getParameter("ecr_date"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_date"));
		String mgr_id = Hanguel.toHanguel(request.getParameter("mgr_id"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_id"));
		String mgr_name = Hanguel.toHanguel(request.getParameter("mgr_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_name"));
		String mgr_code = Hanguel.toHanguel(request.getParameter("mgr_code"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_code"));
		String mgr_div_code = Hanguel.toHanguel(request.getParameter("mgr_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_div_code"));
		String mgr_div_name = Hanguel.toHanguel(request.getParameter("mgr_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("mgr_div_name"));
		String eco_id = Hanguel.toHanguel(request.getParameter("eco_id"))==null?"":Hanguel.toHanguel(request.getParameter("eco_id"));
		String eco_name = Hanguel.toHanguel(request.getParameter("eco_name"))==null?"":Hanguel.toHanguel(request.getParameter("eco_name"));
		String eco_code = Hanguel.toHanguel(request.getParameter("eco_code"))==null?"":Hanguel.toHanguel(request.getParameter("eco_code"));
		String eco_div_code = Hanguel.toHanguel(request.getParameter("eco_div_code"))==null?"":Hanguel.toHanguel(request.getParameter("eco_div_code"));
		String eco_div_name = Hanguel.toHanguel(request.getParameter("eco_div_name"))==null?"":Hanguel.toHanguel(request.getParameter("eco_div_name"));
		String eco_tel = Hanguel.toHanguel(request.getParameter("eco_tel"))==null?"":Hanguel.toHanguel(request.getParameter("eco_tel"));
		String ecc_reason = Hanguel.toHanguel(request.getParameter("ecc_reason"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_reason"));
		String ecc_factor = Hanguel.toHanguel(request.getParameter("ecc_factor"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_factor"));
		String ecc_scope = Hanguel.toHanguel(request.getParameter("ecc_scope"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_scope"));
		String ecc_kind = Hanguel.toHanguel(request.getParameter("ecc_kind"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_kind"));
		String pdg_code = Hanguel.toHanguel(request.getParameter("pdg_code"))==null?"":Hanguel.toHanguel(request.getParameter("pdg_code"));
		String pd_code = Hanguel.toHanguel(request.getParameter("pd_code"))==null?"":Hanguel.toHanguel(request.getParameter("pd_code"));
		String fg_code = Hanguel.toHanguel(request.getParameter("fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("fg_code"));
		String part_code = Hanguel.toHanguel(request.getParameter("part_code"))==null?"":Hanguel.toHanguel(request.getParameter("part_code"));
		String order_date = Hanguel.toHanguel(request.getParameter("order_date"))==null?"":Hanguel.toHanguel(request.getParameter("order_date"));
		String fix_date = Hanguel.toHanguel(request.getParameter("fix_date"))==null?"":Hanguel.toHanguel(request.getParameter("fix_date"));
		String ecc_status = Hanguel.toHanguel(request.getParameter("ecc_status"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_status"));
		
		//ECC_REQ/ECC_ORD구성을 위한 기본파라미터
		String chg_position = Hanguel.toHanguel(request.getParameter("chg_position"))==null?"":Hanguel.toHanguel(request.getParameter("chg_position"));
		String trouble = Hanguel.toHanguel(request.getParameter("trouble"))==null?"":Hanguel.toHanguel(request.getParameter("trouble"));
		String condition = Hanguel.toHanguel(request.getParameter("condition"))==null?"":Hanguel.toHanguel(request.getParameter("condition"));
		String solution = Hanguel.toHanguel(request.getParameter("solution"))==null?"":Hanguel.toHanguel(request.getParameter("solution"));
		String fname = Hanguel.toHanguel(request.getParameter("fname"))==null?"":Hanguel.toHanguel(request.getParameter("fname"));
		String sname = Hanguel.toHanguel(request.getParameter("sname"))==null?"":Hanguel.toHanguel(request.getParameter("sname"));
		String ftype = Hanguel.toHanguel(request.getParameter("ftype"))==null?"":Hanguel.toHanguel(request.getParameter("ftype"));
		String fsize = Hanguel.toHanguel(request.getParameter("fsize"))==null?"":Hanguel.toHanguel(request.getParameter("fsize"));
		String app_no = Hanguel.toHanguel(request.getParameter("app_no"))==null?"":Hanguel.toHanguel(request.getParameter("app_no"));
		String msg = Hanguel.toHanguel(request.getParameter("msg"))==null?"":Hanguel.toHanguel(request.getParameter("msg"));
		
		//기간관련 기본파라미터
		String ecr_s_date = Hanguel.toHanguel(request.getParameter("ecr_s_date"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_s_date"));
		String ecr_e_date = Hanguel.toHanguel(request.getParameter("ecr_e_date"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_e_date"));
		String eco_s_date = Hanguel.toHanguel(request.getParameter("eco_s_date"))==null?"":Hanguel.toHanguel(request.getParameter("eco_s_date"));
		String eco_e_date = Hanguel.toHanguel(request.getParameter("eco_e_date"))==null?"":Hanguel.toHanguel(request.getParameter("eco_e_date"));
		String e_fg_code = Hanguel.toHanguel(request.getParameter("e_fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("e_fg_code"));
		String a_fg_code = Hanguel.toHanguel(request.getParameter("a_fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("a_fg_code"));
		String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		검색리스트
			//------------------------------------------------------------
			//기본검색 LIST
			if ("sch_base".equals(mode)){
				com.anbtech.dcm.db.CbomHistoryDAO histDAO = new com.anbtech.dcm.db.CbomHistoryDAO(con);
				
				//설계변경 기본정보
				ArrayList ecc_list = new ArrayList();
				ecc_list = histDAO.getBaseEccComList(ecc_subject,eco_no,ecr_s_date,ecr_e_date,ecr_name,eco_s_date,eco_e_date,eco_name,ecc_status,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//페이지로 바로가기 List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = histDAO.getBaseDisplayPage(ecc_subject,eco_no,ecr_s_date,ecr_e_date,ecr_name,eco_s_date,eco_e_date,eco_name,ecc_status,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);
				
				///////////////////////////////////////////////////////////
				//파일명을 아래와 같이 영문처리 했을때 한글이 깨지지 않았음.
				///////////////////////////////////////////////////////////
				ecc_subject =new String(ecc_subject.getBytes("euc-kr"),"8859_1");
								
				//파라미터
				String para = "&ecc_subject="+ecc_subject+"&eco_no="+eco_no+"&ecr_s_date="+ecr_s_date+"&ecr_e_date=";
				para += ecr_e_date+"&ecr_name="+ecr_name+"&eco_s_date="+eco_s_date+"&eco_e_date="+eco_e_date;
				para += "&eco_name="+eco_name+"&ecc_status="+ecc_status;
							
				response.sendRedirect("CbomHistoryServlet?mode=sch_base"+para);

			}
			//선택조건 LIST
			else if ("sch_condition".equals(mode)){
				com.anbtech.dcm.db.CbomHistoryDAO histDAO = new com.anbtech.dcm.db.CbomHistoryDAO(con);

				//설계변경 기본정보
				ArrayList ecc_list = new ArrayList();
				ecc_list = histDAO.getConditionEccComList(pdg_code,pd_code,e_fg_code,a_fg_code,part_code,item_code,ecc_reason,ecc_factor,ecc_scope,ecc_kind,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//페이지로 바로가기 List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = histDAO.getConditionDisplayPage(pdg_code,pd_code,e_fg_code,a_fg_code,part_code,item_code,ecc_reason,ecc_factor,ecc_scope,ecc_kind,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);

				//파라미터
				String para = "&pdg_code="+pdg_code+"&pd_code="+pd_code+"&e_fg_code="+e_fg_code+"&a_fg_code=";
				para += a_fg_code+"&ecc_reason="+ecc_reason+"&ecc_factor="+ecc_factor+"&ecc_scope="+ecc_scope;
				para += "&ecc_kind="+ecc_kind+"&part_code="+part_code+"&item_code="+item_code;

				response.sendRedirect("CbomHistoryServlet?mode=sch_condition"+para);

			}
			//내용검색 LIST
			else if ("sch_content".equals(mode)){
				com.anbtech.dcm.db.CbomHistoryDAO histDAO = new com.anbtech.dcm.db.CbomHistoryDAO(con);

				//설계변경 기본정보
				ArrayList ecc_list = new ArrayList();
				ecc_list = histDAO.getContentEccComList(condition,solution,chg_position,trouble,page,max_display_cnt);
				request.setAttribute("ECC_List", ecc_list); 

				//파라미터
				String para = "&condition="+condition+"&solution="+solution+"&chg_position=";
				para += chg_position+"&trouble="+trouble;

				//페이지로 바로가기 List
				com.anbtech.dcm.entity.eccComTable pageL = new com.anbtech.dcm.entity.eccComTable();
				pageL = histDAO.getContentDisplayPage(condition,solution,chg_position,trouble,page,max_display_cnt,max_display_page);
				request.setAttribute("PAGE_List", pageL);
				
				response.sendRedirect("CbomHistoryServlet?mode=sch_content"+para);
			}
		}catch (Exception e){
			//에러출력 페이지로 분기
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}

