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

public class BomTemplateServlet extends HttpServlet {
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
		String mode = Hanguel.toHanguel(request.getParameter("mode"))==null?"temp_list":Hanguel.toHanguel(request.getParameter("mode"));
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
		String model_code = Hanguel.toHanguel(request.getParameter("model_code"))==null?"":Hanguel.toHanguel(request.getParameter("model_code"));
		
		//Template 구성을 위한 기본파라미터
		String flag = Hanguel.toHanguel(request.getParameter("flag"))==null?"":Hanguel.toHanguel(request.getParameter("flag"));
		String m_code = Hanguel.toHanguel(request.getParameter("m_code"))==null?"":Hanguel.toHanguel(request.getParameter("m_code"));
		String spec = Hanguel.toHanguel(request.getParameter("spec"))==null?"":Hanguel.toHanguel(request.getParameter("spec"));
		String tag = Hanguel.toHanguel(request.getParameter("tag"))==null?"":Hanguel.toHanguel(request.getParameter("tag"));
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");

			//------------------------------------------------------------
			//		MBOM TEMPLATE정보 등록/수정/삭제/List 처리하기
			//------------------------------------------------------------
			//MBOM STR LIST 조회 
			if ("temp_list".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				com.anbtech.bm.business.BomTemplateBO tempBO = new com.anbtech.bm.business.BomTemplateBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST 조회
				ArrayList str_list = new ArrayList();
				if(gid.length() != 0) str_list = tempBO.getStrListEleLink(gid,"0",model_code);
				request.setAttribute("STR_List", str_list); 

				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				if(gid.length() != 0) masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/tempPartList.jsp").forward(request,response);
				
			}
			//MBOM STR 등록 준비로 Template List
			else if ("temp_prewrite".equals(mode)){
				com.anbtech.bm.db.BomTemplateDAO tmpDAO = new com.anbtech.bm.db.BomTemplateDAO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//Template 데이터 읽기
				ArrayList tmpT_list = new ArrayList();
				tmpT_list = tmpDAO.getBomEnvList(flag);
				request.setAttribute("TEMP_List",tmpT_list);

				//읽은 데이터 읽기
				com.anbtech.bm.entity.mbomStrTable strT = new com.anbtech.bm.entity.mbomStrTable();
				strT = modDAO.readStrItem(pid);
				request.setAttribute("ITEM_List", strT);

				//사용권한이 있는지 판단하기 
				msg += modDAO.getFgGrade(login_id,gid);
				
				//등록/수정후 메시지 전달하기
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				request.setAttribute("model_code",model_code);
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/templateList.jsp").forward(request,response);
			}
			//미확정 BOM 리스트
			else if ("temp_search".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST 조회
				ArrayList xbom_list = new ArrayList();
				xbom_list = modDAO.getXbomList(sItem,sWord,login_id);
				request.setAttribute("XBOM_List", xbom_list); 

				//리턴하기
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
		
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/tempFgSearch.jsp").forward(request,response);
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
		String mode = multi.getParameter("mode")==null?"temp_list":multi.getParameter("mode");
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
		String model_code = multi.getParameter("model_code")==null?"":multi.getParameter("model_code");
		
		//Template 구성을 위한 기본파라미터
		String flag = multi.getParameter("flag")==null?"":multi.getParameter("flag");
		String m_code = multi.getParameter("m_code")==null?"":multi.getParameter("m_code");
		String spec = multi.getParameter("spec")==null?"":multi.getParameter("spec");
		String tag = multi.getParameter("tag")==null?"":multi.getParameter("tag");
		
		try {
			// conn 생성
			connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
			con = connMgr.getConnection("mssql");
			
			//------------------------------------------------------------
			//		MBOM TEMPLATE정보 등록/수정/삭제/List 처리하기
			//------------------------------------------------------------
			//MBOM STR LIST 조회 
			if ("temp_list".equals(mode)){
				com.anbtech.bm.business.BomInputBO inputBO = new com.anbtech.bm.business.BomInputBO(con);
				com.anbtech.bm.business.BomTemplateBO tempBO = new com.anbtech.bm.business.BomTemplateBO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//모델정보 및 FG코드 정보 전달
				com.anbtech.bm.entity.mbomMasterTable masterT = new com.anbtech.bm.entity.mbomMasterTable();
				masterT = modDAO.readMasterItem(gid);
				request.setAttribute("MASTER_List", masterT);

				//사용권한이 있는지 판단하기 
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) gid = "";

				//Template구성으로 임시BOM인 경우는 지원하지 않음
				String where = "where pid = '"+gid+"'";
				String purpose = modDAO.getColumData("MBOM_MASTER","purpose",where);
				if(purpose.equals("1")) gid = "";

				//LIST 조회
				ArrayList str_list = new ArrayList();
				if(gid.length() != 0) str_list = tempBO.getStrListEleLink(gid,"0",model_code);
				request.setAttribute("STR_List", str_list); 
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/tempPartList.jsp").forward(request,response);
				
			}
			//MBOM STR 등록 준비로 Template List
			else if ("temp_prewrite".equals(mode)){
				com.anbtech.bm.db.BomTemplateDAO tmpDAO = new com.anbtech.bm.db.BomTemplateDAO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//사용권한이 있는지 판단하기 
				msg = modDAO.getFgGrade(login_id,gid);
				if(msg.length() != 0) {
					out.println("	<script>");
					out.println("	alert('"+msg+"');");
					out.println("	history.back(-1);");
					out.println("	</script>");
					out.close();
				}

				//Template구성으로 임시BOM인 경우는 지원하지 않음
				String where = "where pid = '"+gid+"'";
				String purpose = modDAO.getColumData("MBOM_MASTER","purpose",where);
				if(purpose.equals("1")) {
					out.println("	<script>");
					out.println("	alert('임시BOM구성용으로 공정템플릿을 지원하지 않습니다.');");
					out.println("	history.back();");
					out.println("	</script>");
					out.close();
				}

				//Template 데이터 읽기
				ArrayList tmpT_list = new ArrayList();
				tmpT_list = tmpDAO.getBomEnvList(flag);
				request.setAttribute("TEMP_List",tmpT_list);

				//읽은 데이터 읽기
				com.anbtech.bm.entity.mbomStrTable strT = new com.anbtech.bm.entity.mbomStrTable();
				strT = modDAO.readStrItem(pid);
				request.setAttribute("ITEM_List", strT);
				
				//등록/수정후 메시지 전달하기
				request.setAttribute("msg",msg);
				request.setAttribute("gid",gid);
				request.setAttribute("model_code",model_code);
				
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/templateList.jsp").forward(request,response);
			}
			//미확정 BOM 리스트
			else if ("temp_search".equals(mode)){
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//LIST 조회
				ArrayList xbom_list = new ArrayList();
				xbom_list = modDAO.getXbomList(sItem,sWord,login_id);
				request.setAttribute("XBOM_List", xbom_list); 

				//리턴하기
				request.setAttribute("sItem",sItem);
				request.setAttribute("sWord",sWord);
		
				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/tempFgSearch.jsp").forward(request,response);
			}
			//MBOM STR Template 등록 / 삭제
			else if("temp_write".equals(mode) || "temp_delete".equals(mode) || "temp_assywrite".equals(mode)) {
				com.anbtech.bm.business.BomTemplateBO tmpBO = new com.anbtech.bm.business.BomTemplateBO(con);
			
				con.setAutoCommit(false);	// 트랜잭션을 시작
				con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				try{
					//등록하기
					if("temp_write".equals(mode)) {
						msg = tmpBO.inputTemplateBom(gid,parent_code,level_no,tag);
					} 
					//삭제하기
					else if("temp_delete".equals(mode)) {
						msg = tmpBO.deleteTemplateBom(gid);
					}
					//Template을 Assy코드로 바꿔등록하기
					else if("temp_assywrite".equals(mode)) {
						msg = tmpBO.changeAssyCode(gid,login_id); 
					}
					con.commit(); // commit한다.
					con.setAutoCommit(true);

					//MBOM TREE LIST 조회로 링크
					out.println("	<script>");
					out.println("	parent.tree.location.href('BomTemplateServlet?mode=temp_list&gid="+gid+"&model_code="+model_code+"');");
					out.println("	</script>");

					//MBOM TREE 입력창으로 링크
					out.println("	<script>");
					out.println("	parent.reg.location.href('BomTemplateServlet?mode=temp_prewrite&msg="+msg+"&flag="+flag+"');");
					out.println("	</script>");
					out.close();
				}catch(Exception e){
					con.rollback();
					con.setAutoCommit(true);
					out.println("MBOM STR 편집에러:"+e.toString());//에러메시지출력
				}
			}
			//MBOM STR Template을 공정Assy로 바꿔등록 준비
			else if ("temp_preassy".equals(mode)){
				com.anbtech.bm.db.BomTemplateDAO tmpDAO = new com.anbtech.bm.db.BomTemplateDAO(con);
				com.anbtech.bm.db.BomModifyDAO modDAO = new com.anbtech.bm.db.BomModifyDAO(con);

				//Template 데이터 읽기
				ArrayList tmpT_list = new ArrayList();
				tmpT_list = tmpDAO.getTempBomList(gid);
				request.setAttribute("TEMP_List",tmpT_list);

				//읽은 데이터 읽기
				com.anbtech.bm.entity.mbomStrTable strT = new com.anbtech.bm.entity.mbomStrTable();
				strT = modDAO.readStrItem(pid);
				request.setAttribute("ITEM_List", strT);

				//리턴하기
				request.setAttribute("model_code",model_code);

				//분기하기
				getServletContext().getRequestDispatcher("/bm/str/tempAssyList.jsp").forward(request,response);
			}
			
		}catch (Exception e){
			request.setAttribute("ERR_MSG",e.toString());
			getServletContext().getRequestDispatcher("/admin/exceptionpage.jsp").forward(request,response);
		}finally{
			close(con);
		}
	} //doPost()
}

