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

public class BomInputServlet extends HttpServlet {
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"pl_list":Hanguel.toHanguel(request.getParameter("mode"));
		String page = Hanguel.toHanguel(request.getParameter("page"))==null?"1":Hanguel.toHanguel(request.getParameter("page"));
		
		//상세정보 보기시 넘어오는 파라미터
		String sItem = Hanguel.toHanguel(request.getParameter("sItem"))==null?"fg_code":Hanguel.toHanguel(request.getParameter("sItem"));
		String sWord = Hanguel.toHanguel(request.getParameter("sWord"))==null?"":Hanguel.toHanguel(request.getParameter("sWord"));
		
		//MBOM STR구성을 위한 기본파라미터
		String pid = Hanguel.toHanguel(request.getParameter("pid"))==null?"":Hanguel.toHanguel(request.getParameter("pid"));
		String gid = Hanguel.toHanguel(request.getParameter("gid"))==null?"":Hanguel.toHanguel(request.getParameter("gid"));
		String parent_code = Hanguel.toHanguel(request.getParameter("parent_code"))==null?"":Hanguel.toHanguel(request.getParameter("parent_code"));
		String child_code = Hanguel.toHanguel(request.getParameter("child_code"))==null?"":Hanguel.toHanguel(request.getParameter("child_code"));
		String level_no = Hanguel.toHanguel(request.getParameter("level_no"))==null?"":Hanguel.toHanguel(request.getParameter("level_no"));
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
		
		String mgr = "";	//사용권한 
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MBOM STRUCTURE정보 등록/수정/삭제/List 처리하기
			//------------------------------------------------------------
			//MBOM STR LIST 조회
			if ("pl_list".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST 조회
				ArrayList str_list = new ArrayList();
				if(gid.length() != 0) str_list = inputBO.getStrList(gid,"0",model_code);
				request.setAttribute("STR_List", str_list); 

				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(gid.length() != 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/plPartList.jsp").forward(request,response);
				
			}
			//MBOM STR 등록/수정 준비
			else if ("pl_prewrite".equals(mode) || "pl_premodify".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//사용권한이 있는지 판단하기
				mgr = modDAO.getFgGrade(login_id,gid);

				//입력/수정할 데이터 읽기
				com.anbtech.bm.entity.mbomStrTable strT = new com.anbtech.bm.entity.mbomStrTable();
				if(mgr.length() == 0) strT = modDAO.readStrItem(pid);
				request.setAttribute("ITEM_List", strT);

				//MBOM MASTER정보 데이터 읽기
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(mgr.length() == 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);
				

				//등록/수정후 메시지 전달하기
				msg = mgr + msg;
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/plPartReg.jsp").forward(request,response);
			}
			//미확정 BOM 리스트
			else if ("fg_search".equals(mode) || "fi_search_1".equals(mode) || "fi_search_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST 조회
				ArrayList xbom_list = new ArrayList();
				xbom_list = modDAO.getXbomList(sItem,sWord,login_id);
				request.setAttribute("XBOM_List", xbom_list); 

				//리턴하기
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
		
				//분기하기
				if("fg_search".equals(mode))			//편집을 위한 분기
					getServletContext().getRequestDispatcher("/bm/str/plFgSearch.jsp").forward(request,response);
				else if("fi_search_1".equals(mode))		//BOM LIST IMPORT (모품목/자품목/LOC)
					getServletContext().getRequestDispatcher("/bm/str/fiFgSearch_1.jsp").forward(request,response);
				else if("fi_search_2".equals(mode))		//PART LIST IMPORT (품목/공정코드/LOC)
					getServletContext().getRequestDispatcher("/bm/str/fiFgSearch_2.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		MBOM STRUCTURE정보 파일 IMPORT 처리하기
			//------------------------------------------------------------
			//MBOM STR 등록/수정 준비
			else if ("fi_preimport_1".equals(mode) || "fi_preimport_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//사용권한이 있는지 판단하기
				msg += modDAO.getFgGrade(login_id,gid);
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				request.setAttribute("parent_code",parent_code);
				request.setAttribute("level_no",level_no);

				//분기하기
				if("fi_preimport_1".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/fiImport_1.jsp").forward(request,response);
				else if("fi_preimport_2".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/fiImport_2.jsp").forward(request,response);
			
			}
			//MBOM LIST ASSY 선택 준비
			else if ("fi_list_1".equals(mode) || "fi_list_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST 조회
				ArrayList assy_list = new ArrayList();
				if(gid.length() != 0) assy_list = modDAO.getAssyList(gid);
				request.setAttribute("ASSY_List", assy_list); 

				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(gid.length() != 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);
			
				//분기하기
				if("fi_list_1".equals(mode))			//BOM LIST IMPORT (모품목/자품목/Location)
					getServletContext().getRequestDispatcher("/bm/str/fiAssyList_1.jsp").forward(request,response);
				else if("fi_list_2".equals(mode))		//품목 LIST IMPORT (품목코드/공정코드/Location)
					getServletContext().getRequestDispatcher("/bm/str/fiAssyList_2.jsp").forward(request,response);
				
			}
			//------------------------------------------------------------
			//		MBOM STRUCTURE 검사하기
			//------------------------------------------------------------
			//MBOM STR LIST 조회
			else if ("chk_list".equals(mode)){
				com.anbtech.bm.business.BomApprovalBO appBO = new com.anbtech.bm.business.BomApprovalBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//자품목에 공정Assy코드 중복입력 검사 : Phantom Assy검사
				String assy_dup = appBO.checkPhantomAssy(gid);
				request.setAttribute("assy_dup",assy_dup); 

				//Location No 유무 및 중복검사
				String loc_dup = appBO.checkLocation(gid);
				request.setAttribute("loc_dup",loc_dup); 

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/chkPartList.jsp").forward(request,response);
				
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

		//MultipartRequest 크기, 저장디렉토리
		String filepath = com.anbtech.admin.db.ServerConfig.getConf("upload_path")+"/mbom/"+login_id+"/tmp";
		com.anbtech.file.FileWriteString text = new com.anbtech.file.FileWriteString();
		text.setFilepath(filepath);		//directory생성하기
		String maxFileSize = "10";
		MultipartRequest multi = new MultipartRequest(request, filepath, Integer.parseInt(maxFileSize)*1024*1024, "euc-kr"); //해당 경로로 업로드한다

		//PL IMPORT처리하기 : 파일 구분자
		String ck = multi.getParameter("ck")==null?"tab":multi.getParameter("ck"); 
		if(ck.equals("tab")) ck = "	";
		else if(ck.equals("semi")) ck = ";";
		else if(ck.equals("pause")) ck = ",";
		else if(ck.equals("space")) ck = " ";
		else if(ck.equals("etc")) ck = multi.getParameter("etc");

		//기본파라미터
		String mode = multi.getParameter("mode")==null?"pl_list":multi.getParameter("mode");
		String page = multi.getParameter("page")==null?"1":multi.getParameter("page");
		String sItem = multi.getParameter("sItem")==null?"fg_code":multi.getParameter("sItem");
		String sWord = multi.getParameter("sWord")==null?"":multi.getParameter("sWord");
	
		//MBOM STR구성을 위한 기본파라미터
		String pid = multi.getParameter("pid")==null?"":multi.getParameter("pid");
		String gid = multi.getParameter("gid")==null?"":multi.getParameter("gid");
		String parent_code = multi.getParameter("parent_code")==null?"":multi.getParameter("parent_code");
		String child_code = multi.getParameter("child_code")==null?"":multi.getParameter("child_code");
		String level_no = multi.getParameter("level_no")==null?"":multi.getParameter("level_no");
		String part_name = multi.getParameter("part_name")==null?"":multi.getParameter("part_name");
		String part_spec = multi.getParameter("part_spec")==null?"":multi.getParameter("part_spec");
		String location = multi.getParameter("location")==null?"":multi.getParameter("location");
		String op_code = multi.getParameter("op_code")==null?"":multi.getParameter("op_code");
		String qty_unit = multi.getParameter("qty_unit")==null?"":multi.getParameter("qty_unit");
		String qty = multi.getParameter("qty")==null?"":multi.getParameter("qty");
		String price_unit = multi.getParameter("price_unit")==null?"":multi.getParameter("price_unit");
		String price = multi.getParameter("price")==null?"":multi.getParameter("price");
		String adtag = multi.getParameter("adtag")==null?"":multi.getParameter("adtag");
		String eco_no = multi.getParameter("eco_no")==null?"":multi.getParameter("eco_no");
		String part_cnt = multi.getParameter("part_cnt")==null?"":multi.getParameter("part_cnt");
		String part_type = multi.getParameter("part_type")==null?"":multi.getParameter("part_type");
		String url = multi.getParameter("url")==null?"":multi.getParameter("url");
		String msg = multi.getParameter("msg")==null?"":multi.getParameter("msg");

		String mgr = "";	//사용권한 
		String model_code = multi.getParameter("model_code")==null?"":multi.getParameter("model_code");

		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//------------------------------------------------------------
			//		MBOM STRUCTURE정보 등록/수정/삭제/List 처리하기
			//------------------------------------------------------------
			//MBOM STR LIST 조회
			if ("pl_list".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//사용권한이 있는지 판단하기
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) gid = "";

				//LIST 조회
				ArrayList str_list = new ArrayList();
				if(gid.length() != 0) str_list = inputBO.getStrList(gid,"0",model_code);
				request.setAttribute("STR_List", str_list); 
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/plPartList.jsp").forward(request,response);
				
			}
			//MBOM STR 등록/수정 준비
			else if ("pl_prewrite".equals(mode) || "pl_premodify".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//사용권한이 있는지 판단하기
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) {
					out.println("	<script>");
					out.println("	alert('"+msg+"');");
					out.println("	history.back();");
					out.println("	</script>");
					out.close();
				}

				//입력/수정할 데이터 읽기
				com.anbtech.bm.entity.mbomStrTable strT = new com.anbtech.bm.entity.mbomStrTable();
				strT = modDAO.readStrItem(pid);
				request.setAttribute("ITEM_List", strT);

				//MBOM MASTER정보 데이터 읽기
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//메시지 전달
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/plPartReg.jsp").forward(request,response);
			}
			//MBOM STR 등록 / 수정 / 삭제
			else if("pl_write".equals(mode) || "pl_modify".equals(mode) || "pl_delete".equals(mode) || "pl_all_delete".equals(mode)) {
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//개별등록하기
					if("pl_write".equals(mode)) {
						msg = inputBO.insertStr(gid,parent_code,child_code,location,op_code,qty_unit,qty,part_cnt);
					} 
					//개별수정하기
					else if ("pl_modify".equals(mode)) {
						msg = inputBO.updateStr(pid,parent_code,child_code,location,op_code,qty_unit,qty,gid,part_type);
					}
					//개별삭제하기 : 개별삭제
					else if("pl_delete".equals(mode)) {
						msg = inputBO.deleteStr(pid,gid,parent_code);
					}
					//삭제하기 : 전체삭제
					else if("pl_all_delete".equals(mode)) {
						msg = inputBO.deleteAllStr(pid,gid,parent_code);
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//MBOM TREE LIST 조회로 링크 [쿼리시간으로 제외함]
/*					if("pl_all_delete".equals(mode)) {	//전체삭제만 리플레쉬
						out.println("	<script>");
						out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list&gid="+gid+"&model_code="+model_code+"');");
						out.println("	</script>");
					} else if("pl_modify".equals(mode) & part_type.equals("A")) {	//전체수정이면
						out.println("	<script>");
						out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list&gid="+gid+"&model_code="+model_code+"');");
						out.println("	</script>");
					} else {
						msg += "  변경내용을 다시보고자 할 경우는 BOM LIST에서 [다시보기] 를 실행 하십시오.";
					}
*/					
					//무조건 리플레쉬
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list&gid="+gid+"&model_code="+model_code+"');");
					out.println("	</script>");

					//MBOM TREE 입력창으로 링크
					out.println("	<script>");
					out.println("	parent.reg.location.href('BomInputServlet?mode=pl_prewrite&msg="+msg+"&gid="+gid+"&model_code="+model_code+"');");
					out.println("	</script>");
					out.close();

				}catch(Exception e){
					con.rollback();
					request.setAttribute("ERR_MSG",e.toString());
					getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
				}finally{
					con.setAutoCommit(true);
				}
			}
			//미확정 BOM 리스트
			else if ("fg_search".equals(mode) || "fi_search_1".equals(mode) || "fi_search_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST 조회
				ArrayList xbom_list = new ArrayList();
				xbom_list = modDAO.getXbomList(sItem,sWord,login_id);
				request.setAttribute("XBOM_List", xbom_list); 

				//리턴하기
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
		
				//분기하기
				if("fg_search".equals(mode))			//편집을 위한 분기
					getServletContext().getRequestDispatcher("/bm/str/plFgSearch.jsp").forward(request,response);
				else if("fi_search_1".equals(mode))		//BOM LIST IMPORT (모품목/자품목/LOC)
					getServletContext().getRequestDispatcher("/bm/str/fiFgSearch_1.jsp").forward(request,response);
				else if("fi_search_2".equals(mode))		//PART LIST IMPORT (품목/공정코드/LOC)
					getServletContext().getRequestDispatcher("/bm/str/fiFgSearch_2.jsp").forward(request,response);
			}
			//------------------------------------------------------------
			//		MBOM STRUCTURE정보 파일 IMPORT 처리하기
			//------------------------------------------------------------
			//MBOM STR 등록/수정 준비
			else if ("fi_preimport_1".equals(mode) || "fi_preimport_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//사용권한이 있는지 판단하기
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) gid = "";

				//리턴하기
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				request.setAttribute("parent_code",parent_code);
				request.setAttribute("model_code",model_code);
				request.setAttribute("level_no",level_no);

				//분기하기
				if("fi_preimport_1".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/fiImport_1.jsp").forward(request,response);
				else if("fi_preimport_2".equals(mode))
					getServletContext().getRequestDispatcher("/bm/str/fiImport_2.jsp").forward(request,response);
			
			}
			//MBOM LIST ASSY 선택 준비
			else if ("fi_list_1".equals(mode) || "fi_list_2".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(gid.length() != 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//사용권한이 있는지 판단하기
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) gid = "";

				//LIST 조회
				ArrayList assy_list = new ArrayList();
				if(gid.length() != 0) assy_list = modDAO.getAssyList(gid);
				request.setAttribute("ASSY_List", assy_list); 
				
				//분기하기
				if("fi_list_1".equals(mode))			//BOM LIST IMPORT (모품목/자품목/Location)
					getServletContext().getRequestDispatcher("/bm/str/fiAssyList_1.jsp").forward(request,response);
				else if("fi_list_2".equals(mode))		//품목 LIST IMPORT (품목코드/공정코드/Location)
					getServletContext().getRequestDispatcher("/bm/str/fiAssyList_2.jsp").forward(request,response);
				
			}
			//BOM List 읽기: File Import내용 읽기
			else if ("fi_import_1".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				
				//PL Import한 결과
				msg = inputBO.getImportList(multi,filepath,ck,parent_code,level_no,gid);	//DB저장
				
				//PL올리기 결과창으로 링크
				String data = "&gid="+gid+"&level_no=0&model_code="+model_code;
				if(msg.indexOf("정상적") != -1) {
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list"+data+"');");
					out.println("	</script>");
				} else {
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=fi_list_1"+data+"');");
					out.println("	</script>");
				}
				
				//MBOM TREE 입력창으로 링크
				//data = "&msg=&gid="+gid+"&level_no="+level_no+"&parent_code="+parent_code;
				out.println("	<script>");
				out.println("	alert('"+msg+"');");
				out.println("	parent.reg.location.href('BomInputServlet?mode=fi_preimport_1');");
				out.println("	</script>");
				out.close();

			}
			//P/L List 읽기: File Import내용 읽기
			else if ("fi_import_2".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				
				//PL Import한 결과
				msg = inputBO.getImportPartList(multi,filepath,ck,gid);	//DB저장
				
				//PL올리기 결과창으로 링크
				String data = "&gid="+gid+"&level_no=0&model_code="+model_code;
				if(msg.indexOf("정상적") != -1) {
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=pl_list"+data+"');");
					out.println("	</script>");
				} else {
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomInputServlet?mode=fi_list_2"+data+"');");
					out.println("	</script>");
				}
				
				//MBOM TREE 입력창으로 링크
				//data = "&msg=&gid="+gid+"&level_no="+level_no+"&parent_code="+parent_code;
				out.println("	<script>");
				out.println("	alert('"+msg+"');");
				out.println("	parent.reg.location.href('BomInputServlet?mode=fi_preimport_2');");
				out.println("	</script>");
				out.close();
				

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

